package com.ezapp.cloudsyncer.gdrive.d.db.impl;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.sqllite.AppDBLiteFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;

/**
 * Factory class for {@link AppDB}
 * 
 * Date: May 30th, 2015
 * 
 * @author gokul
 *
 */
public abstract class AppDBFactory {

	/**
	 * Self Instance
	 */
	private static AppDBFactory selfInstance;

	/**
	 * 
	 * @return AppDBLiteImplFacory instance
	 */
	public static AppDBFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = AppDBLiteFactory.getInstance();
			// TODO change this to DI
		}
		return selfInstance;
	}

	/**
	 * Gets instance of AppDB
	 * 
	 * @return App DB Instance
	 * @throws AppDBException
	 */
	public abstract AppDB getAppDBInstance() throws AppDBException;
}
