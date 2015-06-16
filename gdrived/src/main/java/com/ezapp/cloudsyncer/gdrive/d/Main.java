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
import com.google.api.services.drive.model.File;
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
			driveUtil.reOAauth(userAccount.getUserEmail());
//			testFiles(driveUtil.getDrive());
			return true;
		} catch (AppGDriveException e) {
			showErrorMessage("Reauth failed: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Builds client credentials
	 * 
	 * @param userKey
	 * @return
	 */
	public static Account buildCredential(String userKey) {
		Drive drive;
		About about;
		User user;
		Account account = null;
		try {
			driveUtil.buildCredentials(userKey);
			drive = driveUtil.getDrive();
			account = new Account();
			about = drive.about().get().execute();
			user = about.getUser();
			account.setAuthToken(userKey);
			account.setUserName(user.getDisplayName());
			account.setUserEmail(user.getEmailAddress());
			if (null != user.getPicture()) {
				account.setPictureUrl(user.getPicture().getUrl());
			}

			// TODO remove below two lines
			testAuth(account);
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
	 */
	public static boolean buildCredentialAndPersist(String userKey) {
		try {
			Account account = buildCredential(userKey);
			if (null != account) {
				appDB.addAccount(account);
				runnerUI.updateUserAccountConfig();
			}
			return true;
		} catch (AppDBException e) {
			LOGGER.error("Error while testing auth: " + e.getMessage(), e);
			showWarningMessage("Your account is not added to application DB. You can still continue to work. "
					+ "However, you have relogin again once application ran again");
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private static void testAuth(Account account) {
		LOGGER.info("Display Name: " + account.getUserName());
		LOGGER.info("Email : " + account.getUserEmail());
	}

	// TODO remove this method
	private static void testFiles(Drive drive) {
		Files files = drive.files();
		FileList fileList;
		try {
			fileList = files.list().execute();
			for (File file : fileList.getItems()) {
				LOGGER.info("Name " + file.getOriginalFilename() + " size "
						+ file.getFileSize() + " type " + file.getKind());
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Reinitialize entire UI
	 */
	public static void reInitUI() {
		runnerUI.shutdown(0);
		SysTray.shutDown();
		initUI();
	}
	
}
