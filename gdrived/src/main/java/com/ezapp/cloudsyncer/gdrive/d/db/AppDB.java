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
	 * re-create schema if schema doesn't exist
	 * 
	 * @throws AppDBException
	 */
	public void reCreateBasicSchema() throws AppDBException;

	/**
	 * Add account into the database
	 * 
	 * @param account
	 * @throws AppDBException
	 */
	public void addAccount(Account account) throws AppDBException;
	
	/**
	 * Update account
	 * 
	 * @param account
	 * @throws AppDBException
	 */
	public void updateAccount(Account account) throws AppDBException;

	/**
	 * Get all account from the database
	 * 
	 * @return Gets list of accounts from the database
	 * @throws AppDBException
	 */
	public List<Account> getAllAccounts() throws AppDBException;

	/**
	 * Deletes Given account
	 * 
	 * @param userEmail
	 * @throws AppDBException
	 */
	public void deleteAccount(String userEmail) throws AppDBException;

	/**
	 * Add app config of given keys and values
	 * 
	 * @param key
	 * @param values
	 * @throws AppDBException
	 */
	public void addAppConfig(String key, String... values)
			throws AppDBException;

	/**
	 * Update app config of given key and value
	 * 
	 * @param key
	 * @param sequence
	 * @param value
	 * @throws AppDBException
	 */
	public void updateAppConfig(String key, Integer sequence, String value)
			throws AppDBException;

	/**
	 * Get values of givem keys
	 * 
	 * @param key
	 * @return values of given key else null
	 * @throws AppDBException
	 */
	public String[] getValues(String key) throws AppDBException;

	/**
	 * Deletes app config values by key
	 * 
	 * @param key
	 * @throws AppDBException
	 */
	public void deleteAppConfig(String key) throws AppDBException;

}
