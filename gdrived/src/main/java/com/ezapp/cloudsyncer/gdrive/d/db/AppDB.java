package com.ezapp.cloudsyncer.gdrive.d.db;

import com.ezapp.cloudsyncer.gdrive.d.vo.Account;


/**
 * Application specific database handler/utility
 * 
 * Date: May 28th, 2015
 * 
 * @author grangarajan
 *
 */
public interface AppDB {

	/**
	 * Checks if DB & schema exists for the application
	 * 
	 * @return true if exist else false
	 */
	public boolean isAppDBExist();
	
	/**
	 * Check and create schema if schema doesn't exist
	 */
	public void checkAndCreateBasicSchema();
	
	/**
	 * Add account into the database
	 * @param account
	 */
	public void addAccount(Account account);
	
}
