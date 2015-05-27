package com.ezapp.cloudsyncer.gdrive.d;

import java.net.URL;
import java.util.Arrays;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;
import com.ezapp.cloudsyncer.gdrive.d.ui.SysTray;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;

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
	 * Google App credentials
	 */
	private static String CLIENT_ID = "157725393237-ld0tohu1nqd93i0eguqu39k093p615ac.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "Kjon8HZlmkUR5xBG3hb4q1Sn";
	private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

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
	 * Entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.debug("Initializing... Please wait!!!!");
		runnerUI = RunnerUIFactory.getInstance().getUIInstance();
		URL fileUrl = Thread.currentThread().getContextClassLoader()
				.getResource("com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png");
		runnerUI.start();
		runnerUI.setImageIco(fileUrl);
		SysTray.initSysTray();
		String oauthURL = getOAuthHttpURL();
		runnerUI.setOAuthURL(oauthURL);
		SWT swt = new SWT();
		System.out.println(swt);
		LOGGER.info("App initialized");
	}

	/**
	 * 
	 * @return
	 */
	private static String getOAuthHttpURL() {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
				Arrays.asList(DriveScopes.DRIVE)).setAccessType("online")
				.setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI)
				.build();

		return url;
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
		System.exit(status);
	}

}
