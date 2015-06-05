package com.ezapp.cloudsyncer.gdrive.d.db;

import java.util.List;

import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
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
	 * @throws AppDBException
	 */
	public boolean isAppConfigExist() throws AppDBException;
	
	/**
	 * Check and create schema if schema doesn't exist
	 * 
	 * @throws AppDBException
	 */
	public void checkAndCreateBasicSchema() throws AppDBException;
	
	/**
	 * Add account into the database
	 * 
	 * @param account
	 * @throws AppDBException
	 */
	public void addAccount(Account account) throws AppDBException;
	
	/**
	 * Get all account from the database
	 * 
	 * @return Gets list of accounts from the database
	 * @throws AppDBException
	 */
	public List<Account> getAllAccounts() throws AppDBException;
	
	/**
	 * Deletes Given account
	 * @param userEmail
	 * @throws AppDBException
	 */
	public void deleteAccount(String userEmail) throws AppDBException;
	
}
