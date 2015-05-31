package com.ezapp.cloudsyncer.gdrive.d.exceptions;

/**
 * Exception definition for gdrive-d cloud syncer
 * 
 * Date: May 30, 2015
 *  
 * @author gokul
 *
 */
public class AppDBException extends Exception {

	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = 6704851169650410432L;

	/**
	 * 
	 */
	public AppDBException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AppDBException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AppDBException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public AppDBException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AppDBException(Throwable cause) {
		super(cause);
	}
	
	

}
