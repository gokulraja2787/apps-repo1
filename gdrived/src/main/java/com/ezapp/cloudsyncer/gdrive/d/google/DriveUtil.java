package com.ezapp.cloudsyncer.gdrive.d.google;

//import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppGDriveException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

/**
 * Utility to build up Drive
 * 
 * Date: May 28th, 2015
 * 
 * @author grangarajan
 *
 */
public class DriveUtil {

	/**
	 * Holds scope list
	 */
	private static final List<String> SCOPE_LIST = Arrays.asList(
			DriveScopes.DRIVE, DriveScopes.DRIVE_FILE,
			DriveScopes.DRIVE_METADATA, DriveScopes.DRIVE_APPDATA);

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(DriveUtil.class);

	/**
	 * Google App credentials
	 */
	private static final String CLIENT_ID = "157725393237-ld0tohu1nqd93i0eguqu39k093p615ac.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "Kjon8HZlmkUR5xBG3hb4q1Sn";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static final String APPLICATION_NAME = "gdrived";

	private HttpTransport httpTransport = new NetHttpTransport();
	private JsonFactory jsonFactory = new JacksonFactory();

	/**
	 * Authorization flow
	 */
	private GoogleAuthorizationCodeFlow flow;
	private GoogleAuthorizationCodeFlow.Builder flowBuilder;

	/**
	 * User drive map
	 */
	private Map<String, UserDriveStructure> userDriveMap = new HashMap<String, DriveUtil.UserDriveStructure>();

	/**
	 * Gets drive of give user id
	 * 
	 * @param userId
	 * @return
	 */
	public Drive getDrive(String userId) {
		Drive drive = null;
		UserDriveStructure driveStruct = userDriveMap.get(userId);
		if (null != driveStruct) {
			if (null == driveStruct.accountCredential) {
				LOGGER.error("Credential is null!! Add account first!!");
				Main.showErrorMessage("Add account first!!");
			} else {
				if (null == driveStruct.drive) {
					Drive.Builder builder = new Drive.Builder(httpTransport,
							jsonFactory, driveStruct.accountCredential);
					builder.setApplicationName(APPLICATION_NAME);
					drive = builder.build();
					builder = null;
					driveStruct.drive = drive;
				} else {
					drive = driveStruct.drive;
				}
			}
		} else {
			LOGGER.error("Credential is null!! Add account first!!");
			Main.showErrorMessage("Add account first!!");
		}
		return drive;
	}

	/**
	 * Get URL for authentication
	 * 
	 * @return authentication URL
	 */
	public String getOAuthHttpURL() {

		flowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPE_LIST)
				.setAccessType("offline").setApprovalPrompt("auto");
		// File credFile = new File("outh2.json");
		// try {
		// if (!credFile.exists()) {
		// credFile.createNewFile();
		// }
		// FileDataStoreFactory credStorefactory = new FileDataStoreFactory(
		// credFile);
		// flowBuilder.setDataStoreFactory(credStorefactory);
		// } catch (IOException e) {
		// LOGGER.error("Cred file creation error: " + e.getMessage(), e);
		// } finally {
		// credFile = null;
		// }
		flow = flowBuilder.build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI)
				.build();

		return url;
	}

	/**
	 * Generate OAuth HTTP URL for Reauthentication
	 */
	public void generateOAuthHttpURLForReauth() {
		flowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPE_LIST);
		flow = flowBuilder.build();
	}

	/**
	 * 
	 * @param userAccount
	 * @throws AppGDriveException
	 */
	public void reOAauth(Account userAccount) throws AppGDriveException {
		TokenResponse tokenResponse = null;
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Start reAuth");
				LOGGER.debug("Finding credential: "
						+ userAccount.getUserEmail());
			}
			generateOAuthHttpURLForReauth();
			GoogleRefreshTokenRequest tokenRequest = new GoogleRefreshTokenRequest(
					flow.getTransport(), flow.getJsonFactory(),
					userAccount.getAuthToken(), CLIENT_ID, CLIENT_SECRET);
			tokenResponse = tokenRequest.execute();
			UserDriveStructure driveStruct = userDriveMap.get(userAccount
					.getUserId());
			if (null == driveStruct) {
				driveStruct = new UserDriveStructure();
				userDriveMap.put(userAccount.getUserId(), driveStruct);
			}
			Credential accountCredential = flow.createAndStoreCredential(
					tokenResponse, userAccount.getUserEmail());
			driveStruct.accountCredential = accountCredential;
		} catch (IOException e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException("Invalid authentication token");
		} catch (Exception e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException(e);
		} finally {
			tokenResponse = null;
		}
	}

	/**
	 * Builds google credentials
	 * 
	 * @param authorizationCode
	 * @param userId
	 * @return refresh Token
	 * @throws AppGDriveException
	 */
	public String buildCredentials(String authorizationCode, String userId)
			throws AppGDriveException {
		GoogleTokenResponse tokenResponse;
		String refToken = null;
		try {
			if (null == flow) {
				getOAuthHttpURL();
			}
			tokenResponse = flow.newTokenRequest(authorizationCode)
					.setRedirectUri(REDIRECT_URI).execute();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(tokenResponse.getTokenType());
				LOGGER.debug(new Date(tokenResponse.getExpiresInSeconds()));
			}
			UserDriveStructure driveStruct = new UserDriveStructure();
			Credential accountCredential = flow.createAndStoreCredential(
					tokenResponse, userId);
			refToken = tokenResponse.getRefreshToken();
			driveStruct.accountCredential = accountCredential;
			userDriveMap.put(userId, driveStruct);
		} catch (IOException e) {
			LOGGER.error(
					"Exception while generating credentials! " + e.getMessage(),
					e);
			throw new AppGDriveException("Invalid authentication token");
		} finally {
			authorizationCode = null;
			tokenResponse = null;
		}
		return refToken;
	}

	/**
	 * Cleans up object and close
	 */
	public void closeCleanUp() {
		userDriveMap.clear();
		flow = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		closeCleanUp();
		super.finalize();
	}

	/**
	 * Revokes token for the given account
	 * 
	 * @param account
	 * @return
	 */
	public boolean revokeToken(Account account) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start revokeToken");
			LOGGER.debug("Finding credential: " + account.getUserEmail());
		}
		GoogleTokenResponse tokenResponse = null;
		try {
			generateOAuthHttpURLForReauth();
			GoogleRefreshTokenRequest request = new GoogleRefreshTokenRequest(
					flow.getTransport(), flow.getJsonFactory(),
					account.getAuthToken(), CLIENT_ID, CLIENT_SECRET);
			tokenResponse = request.execute();
			String accToken = tokenResponse.getAccessToken();
			LOGGER.info("Revoke token: " + accToken);
			String uri = "https://accounts.google.com/o/oauth2/revoke?token="
					+ accToken;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			StatusLine status = httpResponse.getStatusLine();
			LOGGER.debug("Removed status: " + status);
		} catch (IOException e) {
			LOGGER.error("Failed to revoke token " + e.getMessage(), e);
		} finally {
			userDriveMap.remove(account.getUserId());
		}
		return false;
	}

	private class UserDriveStructure {

		/**
		 * Account credential
		 */
		private Credential accountCredential;

		/**
		 * Drive
		 */
		private Drive drive;

	}
}
