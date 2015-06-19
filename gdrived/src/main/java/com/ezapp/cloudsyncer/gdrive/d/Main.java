package com.ezapp.cloudsyncer.gdrive.d;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppGDriveException;
import com.ezapp.cloudsyncer.gdrive.d.google.DriveUtil;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;
import com.ezapp.cloudsyncer.gdrive.d.ui.SysTray;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.User;

/**
 * Main for gdrive cloud syncer for Desktop
 * 
 * @author grangarajan
 * 
 *         Date: May 22, 2015
 *
 */
public class Main {

	/**
	 * Holds UI
	 */
	private static RunnerUI runnerUI;

	/**
	 * Holds APP DB
	 */
	private static AppDB appDB;

	/**
	 * No instance of main allowed
	 */
	private Main() {
	};

	/**
	 * Application logger
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	/**
	 * Drive Util
	 */
	private static DriveUtil driveUtil = new DriveUtil();

	/**
	 * Entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("App Initializing...");
			LOGGER.debug("Initializing... DB");
		}
		initializeDB();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Initializing.... UI!!!!");
		}
		initUI();
		LOGGER.info("App initialized");
	}

	/**
	 * Initializes the UI
	 */
	private static void initUI() {
		SysTray.initSysTray();
		runnerUI = RunnerUIFactory.getInstance().getUIInstance();
		URL fileUrl = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(
						"com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png");
		runnerUI.setImageIco(fileUrl);
		String oauthURL = driveUtil.getOAuthHttpURL();
		runnerUI.setOAuthURL(oauthURL);
		runnerUI.updateUserAccountConfig();
		runnerUI.start();
	}

	/**
	 * Turns down application
	 */
	public static void shutDown() {
		shutDown(0);
	}

	/**
	 * Initializes DB
	 */
	private static void initializeDB() {
		try {
			appDB = AppDBFactory.getInstance().getAppDBInstance();
			if (!appDB.isAppConfigExist()) {
				LOGGER.info("Initializing appconfig in schema");
				appDB.reCreateBasicSchema();
			}
		} catch (AppDBException e) {
			LOGGER.error("DB Initialization failure: " + e.getMessage(), e);
		}
	}

	/**
	 * Turns down application
	 * 
	 * @param status
	 */
	public static void shutDown(int status) {
		if (null != runnerUI) {
			runnerUI.shutdown(status);
		}
		switch (status) {
		case 0:
			LOGGER.info("Exit by UI");
			break;
		case 1:
			LOGGER.info("Exit by System Tray");
			break;
		default:
			LOGGER.warn("Application quit with status: " + status);
		}
		driveUtil.closeCleanUp();
		driveUtil = null;
		SysTray.shutDown();
		System.exit(status);
	}

	/**
	 * Opens add account frame
	 */
	public static void addAccount() {
		runnerUI.openAddAccountWindow();
	}

	/**
	 * Shows / hides Main UI
	 */
	public static void toggleShowHideMainUI() {
		runnerUI.toggleShowHideMainUI();
	}

	/**
	 * Show error message
	 * 
	 * @param message
	 */
	public static void showErrorMessage(String message) {
		runnerUI.showError(message);
	}

	/**
	 * Show info message
	 * 
	 * @param message
	 */
	public static void showInfoMessage(String message) {
		runnerUI.showInfo(message);
	}

	/**
	 * Show warning message
	 * 
	 * @param message
	 */
	public static void showWarningMessage(String message) {
		runnerUI.showWarning(message);
	}

	/**
	 * 
	 * @param userAccount
	 * @return
	 */
	public static boolean reAuthenticateWithExistingAccount(Account userAccount) {
		try {
			driveUtil.reOAauth(userAccount);
			testFiles(driveUtil.getDrive(userAccount.getUserId()));
			return true;
		} catch (AppGDriveException e) {
			showErrorMessage("Reauth failed! Maybe authentication expired. Re-login required for "
					+ userAccount.getUserEmail());
		}
		return false;
	}

	/**
	 * Builds client credentials
	 * 
	 * @param userKey
	 * @param userId
	 * @return user account
	 */
	public static Account buildCredential(String userKey, String userId) {
		Drive drive;
		About about;
		User user;
		Account account = null;
		try {
			String refToken = driveUtil.buildCredentials(userKey, userId);
			drive = driveUtil.getDrive(userId);
			account = new Account();
			about = drive.about().get().execute();
			user = about.getUser();
			account.setAuthToken(refToken);
			account.setUserName(user.getDisplayName());
			account.setUserEmail(user.getEmailAddress());
			if (null != user.getPicture()) {
				account.setPictureUrl(user.getPicture().getUrl());
			}
			account.setUserId(userId);

			// TODO remove below two lines
			testFiles(drive);
			return account;
		} catch (IOException e) {
			LOGGER.error("Error while testing auth: " + e.getMessage(), e);
			showErrorMessage("Reauthentication failed - please readd the account");
		} catch (AppGDriveException e) {
			showErrorMessage(e.getMessage());
		} finally {
			userKey = null;
			drive = null;
			about = null;
			user = null;
			account = null;
		}
		return null;
	}

	/**
	 * Builds client credentials and add the built credentials to Database
	 * 
	 * @param userKey
	 * @param userId
	 */
	public static boolean buildCredentialAndPersist(String userKey,
			String userId) {
		try {
			Account account = buildCredential(userKey, userId);
			if (null != account) {
				appDB.addAccount(account);
				runnerUI.updateUserAccountConfig();
			}
			return true;
		} catch (AppDBException e) {
			LOGGER.error("Error while testing auth: " + e.getMessage(), e);
			showWarningMessage("Your account is not added to application DB. You can still continue to work. "
					+ "However, you have relogin again once application ran again");
		} finally {
			userKey = null;
		}
		return false;
	}

	/**
	 * Get all configured accounts
	 * 
	 * @return List of configured accounts
	 */
	public static List<Account> getConfiguredAccounts() {
		List<Account> configuredAccounts = null;
		try {
			configuredAccounts = appDB.getAllAccounts();
		} catch (AppDBException e) {
			LOGGER.error("Error while testing auth: " + e.getMessage(), e);
		}
		return configuredAccounts;
	}

	// TODO remove this method
	private static void testFiles(Drive drive) {
		Files files = drive.files();
		FileList fileList;
		try {
			About about = drive.about().get().execute();
			User user = about.getUser();
			LOGGER.info("Display Name: " + user.getDisplayName());
			fileList = files.list().execute();
			LOGGER.info("File count: " + fileList.getItems().size());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Reinitialize entire UI
	 */
	public static void reInitUI() {
		runnerUI.shutdown(0);
		initUI();
	}

	/**
	 * Deletes account
	 * 
	 * @param account
	 */
	public static void deleteAccount(Account account) {
		if (null != appDB) {
			String email = account.getUserEmail();
			try {
				appDB.deleteAccount(email);
				runnerUI.updateUserAccountConfig();
				driveUtil.revokeToken(account);
				showInfoMessage(email + " removed");
			} catch (AppDBException e) {
				runnerUI.showError(email + " delete failed");
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Delete account by email
	 * 
	 * @param email
	 */
	public static void deleteAccount(String email) {
		try {
			List<Account> accounts = appDB.getAllAccounts();
			if (null != accounts) {
				for (Account account : appDB.getAllAccounts()) {
					if (account.getUserEmail().equals(email)) {
						deleteAccount(account);
						break;
					}
				}
			}
		} catch (AppDBException e) {
			LOGGER.error(e.getMessage(), e);
			showErrorMessage("Account removal failed!");
		}
	}

	/**
	 * Open configure account window
	 */
	public static void configureAccount() {
		runnerUI.openConfigurationFrame();
	}

	/**
	 * Holds app config key to mount dir.
	 */
	private static final String MOUNT_DIR_KEY = ".mount.dir";

	/**
	 * 
	 * @param account
	 * @return
	 */
	public static String getDirMount(Account account) {
		String dirMounted = null;
		String appValues[];
		try {
			dirMounted = ((appValues = appDB.getValues(account.getUserEmail()
					+ MOUNT_DIR_KEY)) != null && appValues.length > 0) ? appValues[appValues.length - 1]
					: "--";
		} catch (AppDBException e) {
			LOGGER.error("Error while fetching mount drive: " + e.getMessage(),
					e);
		}
		return dirMounted;
	}

	/**
	 * 
	 * @param account
	 * @param dir
	 * @param oldDir
	 * @return
	 */
	public static boolean setDirMount(Account account, String dir, String oldDir) {
		String key = account.getUserEmail() + MOUNT_DIR_KEY;
		try {
			if (appDB.isAppConfigKeyValueExist(key, oldDir)) {
				appDB.updateAppConfig(key, 1, dir);
			} else {
				appDB.addAppConfig(key, dir);
			}
		} catch (AppDBException e) {
			String msg = "Error while saving configuration";
			LOGGER.error(msg, e);
			showErrorMessage(msg + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Opens remote browser frame
	 */
	public static void openRemoteBrowser() {
		runnerUI.openRemoteBrowserFrame();
	}

	/**
	 * Get driveUtil
	 * 
	 * @return Drive util
	 */
	public static DriveUtil getDriveUtil() {
		return driveUtil;
	}

}
