package com.ezapp.cloudsyncer.gdrive.d.vo;

import java.io.Serializable;

/**
 * @author grangarajan
 *
 */
public class Account implements Serializable {

	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = 4980774520063683021L;

	/**
	 * Holds User Name
	 */
	private String userName;

	/**
	 * Holds User Email
	 */
	private String userEmail;

	/**
	 * Holds authentication token
	 */
	private String authToken;

	/**
	 * Holds Picture URL
	 */
	private String pictureUrl;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail
	 *            the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * @param pictureUrl
	 *            the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return userName;
	}

}
