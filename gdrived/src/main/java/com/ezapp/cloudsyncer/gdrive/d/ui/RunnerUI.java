package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.net.URL;

/**
 * 
 * UI event/behaviour Definition 
 * 
 * @author grangarajan
 * 
 * Date: May 22, 2015
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
	 * @param url
	 */
	public void setOAuthURL(String url);
	
	/**
	 * Set image icon
	 * @param url
	 */
	public void setImageIco(URL url);
	
}
