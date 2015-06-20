package com.ezapp.cloudsyncer.gdrive.d.exceptions;

/**
 * Specifies File related exception
 * 
 * Date: June 19, 2015
 * 
 * @author gokul
 *
 */
public class AppFileException extends Exception {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4207588223915985331L;

	/**
	 * 
	 */
	public AppFileException() {
	}

	/**
	 * @param message
	 */
	public AppFileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AppFileException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AppFileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AppFileException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
