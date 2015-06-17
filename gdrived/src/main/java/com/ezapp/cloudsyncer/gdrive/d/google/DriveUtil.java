package com.ezapp.cloudsyncer.gdrive.d.google;

//import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppGDriveException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
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
	 * Credential
	 */
	private Credential accountCredential;

	/**
	 * Drive
	 */
	private static Drive drive;

	public Drive getDrive() {
		if (null == drive) {
			if (null == accountCredential) {
				LOGGER.error("Credential is null!! Add account first!!");
				Main.showErrorMessage("Add account first!!");
			}
			Drive.Builder builder = new Drive.Builder(httpTransport,
					jsonFactory, accountCredential);
			builder.setApplicationName(APPLICATION_NAME);
			drive = builder.build();
			builder = null;
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
			TokenRequest tokenRequest = new TokenRequest(flow.getTransport(),
					flow.getJsonFactory(), new GenericUrl(
							flow.getTokenServerEncodedUrl()), "refresh_token");
			Map<String, Object> unknownFields = new TreeMap<String, Object>();
			unknownFields.put("refresh_token", userAccount.getAuthToken());
			unknownFields.put("client_id", CLIENT_ID);
			unknownFields.put("client_secret", CLIENT_SECRET);
			tokenRequest.setUnknownKeys(unknownFields);
			System.out.println(tokenRequest.toString());
			tokenResponse = tokenRequest.execute();
			accountCredential = flow.createAndStoreCredential(tokenResponse,
					userAccount.getUserEmail());
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

			accountCredential = flow.createAndStoreCredential(tokenResponse,
					userId);
			refToken = tokenResponse.getRefreshToken();

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
		drive = null;
		accountCredential = null;
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
}
