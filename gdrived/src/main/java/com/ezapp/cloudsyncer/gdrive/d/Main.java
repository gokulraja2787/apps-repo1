package com.ezapp.cloudsyncer.gdrive.d;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

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
		
		LOGGER.debug("App starts");
		LOGGER.error("What nonsence");
		LOGGER.debug("App Ends");
	}

}
