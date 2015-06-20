package com.ezapp.cloudsyncer.gdrive.d.vo;

import com.google.api.services.drive.model.File;

/**
 * Holds Remote file details
 * 
 * Date: June 19, 2015
 * 
 * @author gokul
 *
 */
public class RemoteFile {

	/**
	 * Get remote file from google gile
	 * 
	 * @param file
	 */
	public RemoteFile(File file) {
		setFileName(file.getOriginalFilename());
		setFileMimeType(file.getMimeType());
		setFileLink(file.getDownloadUrl());
		setIconUrl(file.getIconLink());
		setFileTitle(file.getTitle());
		setFileId(file.getId());
		if (null != file.getFileSize()) {
			setFileSize(file.getFileSize());
		}
	}

	/**
	 * Holds fine name
	 */
	private String fileName;

	/**
	 * Holds file title
	 */
	private String fileTitle;

	/**
	 * Holds file MIME type
	 */
	private String fileMimeType;

	/**
	 * Holds ICON URL
	 */
	private String iconUrl;

	/**
	 * Holds size
	 */
	private long fileSize;

	/**
	 * Holds file link
	 */
	private String fileLink;

	/**
	 * Holds File ID
	 */
	private String fileId;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileTitle
	 */
	public String getFileTitle() {
		return fileTitle;
	}

	/**
	 * @param fileTitle
	 *            the fileTitle to set
	 */
	private void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	/**
	 * @return the fileMimeType
	 */
	public String getFileMimeType() {
		return fileMimeType;
	}

	/**
	 * @param fileMimeType
	 *            the fileMimeType to set
	 */
	private void setFileMimeType(String fileMimeType) {
		this.fileMimeType = fileMimeType;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl
	 *            the iconUrl to set
	 */
	private void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	private void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileLink
	 */
	public String getFileLink() {
		return fileLink;
	}

	/**
	 * @param fileLink
	 *            the fileLink to set
	 */
	private void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	private void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
