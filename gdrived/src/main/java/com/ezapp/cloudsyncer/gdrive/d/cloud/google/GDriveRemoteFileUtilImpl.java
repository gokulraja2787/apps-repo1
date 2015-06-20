package com.ezapp.cloudsyncer.gdrive.d.cloud.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

/**
 * Remote file Utility implementation
 * 
 * Date: June 19, 2015
 * 
 * @author gokul
 *
 */
class GDriveRemoteFileUtilImpl implements IRemoteFileUtil {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(GDriveRemoteFileUtilImpl.class);

	/**
	 * Account to which this impl should look for
	 */
	private Account account;

	/**
	 * 
	 */
	GDriveRemoteFileUtilImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFile#getRootFolder()
	 */
	@Override
	public String getRootFolder() throws AppFileException {
		String rootFolderId = null;
		if (null == account) {
			throw new AppFileException(
					"Please initialize an account using init(account)");
		}
		Drive drive = Main.getDriveUtil().getDrive(account.getUserId());
		About about;
		try {
			about = drive.about().get().execute();
			rootFolderId = about.getRootFolderId();
		} catch (IOException e) {
			LOGGER.error(
					"Error while proccessing file info from google "
							+ e.getMessage(), e);
			throw new AppFileException(
					"Error while proccessing file info from google "
							+ e.getMessage(), e);
		} finally {
			about = null;
			drive = null;
		}
		return rootFolderId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFile#init(com.ezapp.cloudsyncer
	 * .gdrive.d.vo.Account)
	 */
	@Override
	public void init(Account account) {
		this.account = account;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil#getAllFiles(java
	 * .lang.Object)
	 */
	@Override
	public List<RemoteFile> getAllFiles(Object folder) throws AppFileException {
		String folderId = null;
		if (null == account) {
			throw new AppFileException(
					"Please initialize an account using init(account)");
		}
		if (!(folder instanceof String)) {
			throw new AppFileException("Invalid parameter");
		}
		try {
			folderId = (String) folder;
		} catch (ClassCastException e) {
			LOGGER.error("Invalid parameter", e);
			throw new AppFileException("Invalid parameter", e);
		}
		Drive drive = Main.getDriveUtil().getDrive(account.getUserId());
		ChildList list;
		List<ChildReference> childListRef;
		List<RemoteFile> fileList;
		try {
			list = drive.children().list(folderId).execute();
			childListRef = list.getItems();
			fileList = new ArrayList<>(childListRef.size());
			for (ChildReference reference : childListRef) {
				String fileId = reference.getId();
				Files dfiles = drive.files();
				Files.Get fgd = dfiles.get(fileId);
				File actualFile = fgd.execute();
				RemoteFile rfile = new RemoteFile(actualFile);
				fileList.add(rfile);
			}
			return fileList;
		} catch (IOException e) {
			LOGGER.error("Error while getting folder list", e);
			throw new AppFileException("Error while getting folder list", e);
		} finally {
			fileList = null;
			childListRef = null;
			list = null;
			drive = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil#getFile(java.lang
	 * .Object)
	 */
	@Override
	public RemoteFile getFile(Object fileId) throws AppFileException {
		String folderId = null;
		if (null == account) {
			throw new AppFileException(
					"Please initialize an account using init(account)");
		}
		if (!(fileId instanceof String)) {
			throw new AppFileException("Invalid parameter");
		}
		try {
			folderId = (String) fileId;
		} catch (ClassCastException e) {
			LOGGER.error("Invalid parameter", e);
			throw new AppFileException("Invalid parameter", e);
		}
		Drive drive = Main.getDriveUtil().getDrive(account.getUserId());
		Files files;
		File file;
		files = drive.files();
		try {
			file = files.get(folderId).execute();
			RemoteFile rfile = new RemoteFile(file);
			return rfile;
		} catch (IOException e) {
			LOGGER.error("File meta error ", e);
			throw new AppFileException("File meta error ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil#getParent(java.lang
	 * .Object)
	 */
	@Override
	public List<RemoteFile> getParent(Object fileId) throws AppFileException {
		String folderId = null;
		List<RemoteFile> result = null;
		if (null == account) {
			throw new AppFileException(
					"Please initialize an account using init(account)");
		}
		if (!(fileId instanceof String)) {
			throw new AppFileException("Invalid parameter");
		}
		try {
			folderId = (String) fileId;
		} catch (ClassCastException e) {
			LOGGER.error("Invalid parameter", e);
			throw new AppFileException("Invalid parameter", e);
		}
		Drive drive = Main.getDriveUtil().getDrive(account.getUserId());
		Files files;
		File file;
		List<ParentReference> parentRefernce;
		try {
			files = drive.files();
			file = files.get(folderId).execute();
			parentRefernce = file.getParents();
			result = new ArrayList<>(parentRefernce.size());
			{
				for (ParentReference parentRef : parentRefernce) {
					folderId = parentRef.getId();
					File parentFolder = files.get(folderId).execute();
					RemoteFile rfile = new RemoteFile(parentFolder);
					result.add(rfile);
				}
			}
			return result;
		} catch (IOException e) {
			LOGGER.error("File meta error ", e);
			throw new AppFileException("File meta error ", e);
		} finally {
			result = null;
			parentRefernce = null;
			file = null;
			files = null;
			drive = null;
		}
	}

}
