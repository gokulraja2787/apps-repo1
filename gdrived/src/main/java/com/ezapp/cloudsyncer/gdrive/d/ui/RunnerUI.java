package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.net.URL;

/**
 * 
 * UI event/behaviour Definition
 * 
 * @author grangarajan
 * 
 *         Date: May 22, 2015
 *
 */
public interface RunnerUI {

	/**
	 * Start UI initialization
	 */
	public void start();

	/**
	 * Shutdown/destroy UI
	 * 
	 * @param statusCode
	 * @return statusCode
	 */
	public int shutdown(int statusCode);

	/**
	 * Set oAuth URL
	 * 
	 * @param url
	 */
	public void setOAuthURL(String url);

	/**
	 * Set image icon
	 * 
	 * @param url
	 */
	public void setImageIco(URL url);

	/**
	 * Open's add account window
	 */
	public void openAddAccountWindow();

	/**
	 * Show / hide main UI
	 */
	public void toggleShowHideMainUI();

	/**
	 * Show error message
	 * 
	 * @param message
	 */
	public void showError(String message);

	/**
	 * Show warning message
	 * 
	 * @param message
	 */
	public void showWarning(String message);

	/**
	 * Show info message
	 * 
	 * @param message
	 */
	public void showInfo(String message);

	/**
	 * Refreshes configured user accounts
	 */
	public void updateUserAccountConfig();
	
	/**
	 * Open configuration window
	 */
	public void openConfigurationFrame();
	
	/**
	 * Open remote browser frame
	 */
	public void openRemoteBrowserFrame();

}
