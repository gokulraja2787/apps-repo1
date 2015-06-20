package com.ezapp.cloudsyncer.gdrive.d.cloud.google;

import com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil;

/**
 * Factory class for GDriveRemoteFileImpl
 * 
 * Date June 19, 2015
 * 
 * @author gokul
 *
 */
public class GDriveRemoteFileUtilImplFactory {

	/**
	 * Self instance
	 */
	private static GDriveRemoteFileUtilImplFactory selfInstance;

	/**
	 * Singleton
	 */
	private GDriveRemoteFileUtilImplFactory() {
	}

	/**
	 * 
	 * @return selfInstance
	 */
	public static GDriveRemoteFileUtilImplFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = new GDriveRemoteFileUtilImplFactory();
		}
		return selfInstance;
	}

	/**
	 * Get Google Drive Remote file instance
	 * 
	 * @return instance
	 */
	public IRemoteFileUtil getGDriveRemoteFileUtilInstance() {
		IRemoteFileUtil instance = new GDriveRemoteFileUtilImpl();
		return instance;
	}

}
