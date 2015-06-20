package com.ezapp.cloudsyncer.gdrive.d.cloud;

import java.util.List;

import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

/**
 * Interface to hold remote file util
 * 
 * Date: June 19, 2015
 * 
 * @author gokul
 *
 */
public interface IRemoteFileUtil {

	/**
	 * Gets Root folder
	 * 
	 * @return Root folder
	 * 
	 * @throws AppFileException
	 */
	public Object getRootFolder() throws AppFileException;

	/**
	 * initializes give account
	 * 
	 * @param account
	 */
	public void init(Account account);

	/**
	 * Gets all file of given folder
	 * 
	 * @param folder
	 * @return
	 * @throws AppFileException
	 */
	public List<RemoteFile> getAllFiles(Object folder) throws AppFileException;

	/**
	 * Gets file / folder of given file ID
	 * 
	 * @param fileId
	 * @return
	 * @throws AppFileException
	 */
	public RemoteFile getFile(Object fileId) throws AppFileException;
	
	/**
	 * Gets parent of given file / folder
	 * 
	 * @param fileId
	 * @return
	 * @throws AppFileException
	 */
	public List<RemoteFile> getParent(Object fileId) throws AppFileException;

}
