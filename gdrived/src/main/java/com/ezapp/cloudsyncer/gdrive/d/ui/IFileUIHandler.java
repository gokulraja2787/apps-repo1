package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.io.IOException;

import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * Interface definition for UI file handler.
 * 
 * Date: October 29, 2015
 * 
 * @author grangarajan
 *
 */
public interface IFileUIHandler {

	/**
	 * Show files in the Pane
	 * 
	 */
	public void drawFiles();

	/**
	 * Iterate and show files in the folder
	 * 
	 * @param folderId
	 * @throws AppFileException
	 * @throws IOException
	 */
	public void drawFiles(String folderId) throws AppFileException, IOException;

	/**
	 * Gets the for account for which file UI Handler is created for
	 * 
	 * @return account
	 */
	public Account getForAccount();

	/**
	 * Do other operation
	 */
	public void doMisc();

}
