package com.ezapp.cloudsyncer.gdrive.d.exceptions;

/**
 * Exception definition for GDrive API
 * 
 * Date: June 13, 2015
 * 
 * @author gokul
 *
 */
public class AppGDriveException extends Exception {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -6988002657401809482L;

	/**
	 * 
	 */
	public AppGDriveException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AppGDriveException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AppGDriveException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public AppGDriveException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AppGDriveException(Throwable cause) {
		super(cause);
	}

}
