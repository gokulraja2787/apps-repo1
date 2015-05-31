package com.ezapp.cloudsyncer.gdrive.d.db.impl.sqllite;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;

/**
 * Factory class for {@link AppDBLiteImpl}
 * 
 * Date: May 30th, 2015
 * 
 * @author gokul
 *
 */
public class AppDBLiteFactory extends AppDBFactory {

	/**
	 * Self instance
	 */
	private static AppDBLiteFactory selfInstance;

	/**
	 * Gets instance of AppDBLiteFactory
	 * 
	 * @return instance of AppDBLiteFactory
	 */
	public static AppDBFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = new AppDBLiteFactory();
		}
		return selfInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory#getAppDBInstance()
	 */
	@Override
	public AppDB getAppDBInstance() throws AppDBException {
		return AppDBLiteImpl.getInstance();
	}

}
