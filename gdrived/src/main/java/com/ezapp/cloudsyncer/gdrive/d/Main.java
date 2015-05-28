package com.ezapp.cloudsyncer.gdrive.d;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.Logger;

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
		LOGGER.debug("Initializing... Please wait!!!!");
		runnerUI = RunnerUIFactory.getInstance().getUIInstance();
		URL fileUrl = Thread
				.currentThread()
				.getContextClassLoader()
				.getResource(
						"com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png");
		runnerUI.start();
		runnerUI.setImageIco(fileUrl);
		SysTray.initSysTray();
		String oauthURL = driveUtil.getOAuthHttpURL();
		runnerUI.setOAuthURL(oauthURL);
		LOGGER.info("App initialized");
	}

	/**
	 * Turns down application
	 */
	public static void shutDown() {
		shutDown(0);
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
	 * Builds client credentials
	 * 
	 * @param userKey
	 */
	public static void buildCredential(String userKey) {
		driveUtil.buildCredentials(userKey);
		Drive drive = driveUtil.getDrive();
		About about;
		User user;
		Account account = new Account();
		try {
			about = drive.about().get().execute();
			user = about.getUser();
			account.setAuthToken(userKey);
			account.setUserName(user.getDisplayName());
			account.setUserEmail(user.getEmailAddress());
			account.setPictureUrl(user.getPicture().getUrl());
			testAuth(account);
			testFiles(drive);
		} catch (IOException e) {
			LOGGER.error("Error while testing auth: " + e.getMessage(), e);
		} finally {
			userKey = null;
			drive = null;
			about = null;
			user = null;
			account = null;
		}
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

}
