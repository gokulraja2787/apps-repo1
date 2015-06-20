package com.ezapp.cloudsyncer.gdrive.d.cloud;

import com.ezapp.cloudsyncer.gdrive.d.cloud.google.GDriveRemoteFileUtilImplFactory;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * Factory class for remote file implementation
 * 
 * Date: June 19, 2015
 * 
 * @author gokul
 *
 */
public class RemoteFileUtilImplFactory {

	/**
	 * Self instance
	 */
	private static RemoteFileUtilImplFactory selfInstance;

	/**
	 * Singleton
	 */
	private RemoteFileUtilImplFactory() {
	}

	/**
	 * 
	 * @return Remote file Impl factory
	 */
	public static RemoteFileUtilImplFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = new RemoteFileUtilImplFactory();
		}
		return selfInstance;
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	public IRemoteFileUtil getRemoteFileUtilInstance(Account account) {
		IRemoteFileUtil instance = null;
		// TODO Bring DI!!!
		instance = GDriveRemoteFileUtilImplFactory.getInstance()
				.getGDriveRemoteFileUtilInstance();
		instance.init(account);
		return instance;
	}

}
