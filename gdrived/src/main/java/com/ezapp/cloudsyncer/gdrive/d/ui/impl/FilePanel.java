package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

/**
 * Represents inner panel in directory browser frame
 * 
 * Date: October 29, 2015
 * 
 * @author gokul
 *
 */
public class FilePanel extends JPanel {

	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = 7417583528074391555L;

	/**
	 * Holds remote file
	 */
	private RemoteFile remoteFile;

	/**
	 * 
	 * @param remoteFile
	 */
	public FilePanel(RemoteFile remoteFile) {
		super();
		this.remoteFile = remoteFile;
	}

	/**
	 * 
	 * @param isDoubleBuffered
	 * @param remoteFile
	 */
	public FilePanel(boolean isDoubleBuffered, RemoteFile remoteFile) {
		super(isDoubleBuffered);
		this.remoteFile = remoteFile;
	}

	/**
	 * 
	 * @param layout
	 * @param isDoubleBuffered
	 * @param remoteFile
	 */
	public FilePanel(LayoutManager layout, boolean isDoubleBuffered,
			RemoteFile remoteFile) {
		super(layout, isDoubleBuffered);
		this.remoteFile = remoteFile;
	}

	/**
	 * 
	 * @param layout
	 * @param remoteFile
	 */
	public FilePanel(LayoutManager layout, RemoteFile remoteFile) {
		super(layout);
		this.remoteFile = remoteFile;
	}

	/**
	 * @return the remoteFile
	 */
	public RemoteFile getRemoteFile() {
		return remoteFile;
	}

	/**
	 * @param remoteFile
	 *            the remoteFile to set
	 */
	public void setRemoteFile(RemoteFile remoteFile) {
		this.remoteFile = remoteFile;
	}

}
