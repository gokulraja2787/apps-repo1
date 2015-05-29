package com.ezapp.cloudsyncer.gdrive.d.db.impl.sqllite;

import java.sql.Connection;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * Application specific DB implementation
 *  
 *  Date: May 29th, 2015
 *  
 * @author grangarajan
 *
 */
class AppDBLiteImpl implements AppDB {

	/**
	 * Self instance
	 */
	private static AppDB selfInstance;
	
	/**
	 * Connection
	 */
	private Connection connection;
	
	/**
	 * Singleton
	 */
	private AppDBLiteImpl() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#isAppDBExist()
	 */
	public boolean isAppDBExist() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#checkAndCreateBasicSchema()
	 */
	public void checkAndCreateBasicSchema() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#addAccount(com.ezapp.cloudsyncer.gdrive.d.vo.Account)
	 */
	public void addAccount(Account account) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Gets singleton instance of {@link AppDBLiteImpl} 
	 * @return
	 */
	public static AppDB getInstance() {
		if(null == selfInstance) {
			selfInstance = new AppDBLiteImpl();
		}
		return selfInstance;
	}

}
