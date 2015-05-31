package com.ezapp.cloudsyncer.gdrive.d.db.impl.sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * Application specific DB implementation
 * 
 * Date: May 29th, 2015
 * 
 * @author grangarajan
 *
 */
class AppDBLiteImpl implements AppDB {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(AppDBLiteImpl.class);

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
	 * 
	 * @throws AppDBException
	 */
	private AppDBLiteImpl() throws AppDBException {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:grdive.db");
		} catch (ClassNotFoundException e) {
			LOGGER.error(
					"Error while initializing Database: " + e.getMessage(), e);
			throw new AppDBException("Error while initializing Database", e);
		} catch (SQLException e) {
			LOGGER.error(
					"Error while initializing Database: " + e.getMessage(), e);
			throw new AppDBException("Error while initializing Database", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#isAppConfigExist()
	 */
	public boolean isAppConfigExist() throws AppDBException {
		if (null != connection) {
			PreparedStatement statement = null;
			ResultSet result = null;
			try {
				statement = connection.prepareStatement(QUERIES.CHECK_TABLE);
				result = statement.executeQuery();
				if (result.next()) {
					int cnt = result.getInt("__CNT");
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Count result: " + cnt);
					}
					return (cnt == 2);
				}
			} catch (SQLException e) {
				LOGGER.error(
						"Error while initializing Database: " + e.getMessage(),
						e);
				throw new AppDBException("Error while initializing Database", e);
			} finally {
				try {
					if (null != result) {
						result.close();
					}
					if (null != statement) {
						statement.close();
					}
				} catch (SQLException e) {
					LOGGER.warn(e.getMessage(), e);
				} finally {
					result = null;
					statement = null;
				}
			}
		} else {
			throw new AppDBException("No DB found");
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#checkAndCreateBasicSchema()
	 */
	public void checkAndCreateBasicSchema() throws AppDBException {
		if (null != connection) {
			PreparedStatement statement = null;
			try {
				statement = connection
						.prepareStatement(QUERIES.CREATE_APP_CONFIG);
				statement.execute();
				statement = connection
						.prepareStatement(QUERIES.CREATE_APP_CONFIG_VALUE);
				statement.execute();
			} catch (SQLException e) {
				LOGGER.error("Error while creating tables: " + e.getMessage(),
						e);
			}
		} else {
			throw new AppDBException("No DB found");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#addAccount(com.ezapp.cloudsyncer
	 * .gdrive.d.vo.Account)
	 */
	public void addAccount(Account account) throws AppDBException {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets singleton instance of {@link AppDBLiteImpl}
	 * 
	 * @return AppDB impl instance
	 * @throws AppDBException
	 */
	public static AppDB getInstance() throws AppDBException {
		if (null == selfInstance) {
			selfInstance = new AppDBLiteImpl();
		}
		return selfInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (null != connection) {
			connection.close();
		}
		connection = null;
		System.out.println("DB Connection cleaned up!!!");
		super.finalize();
	}

	/**
	 * Holds queries for Lite DB
	 * 
	 * @author gokul
	 *
	 */
	private static interface QUERIES {

		// Check table existance
		String CHECK_TABLE = "SELECT COUNT(name) __CNT FROM sqlite_master WHERE type='table' AND name IN ('__APPCONFIG','__APPCONFIG_VALUE')";

		// Create app config table
		String CREATE_APP_CONFIG = "CREATE TABLE __APPCONFIG ("
				+ "ID INTEGER PRIMARY KEY NOT NULL,"
				+ "KEY VARCHAR(10) NOT NULL)";

		// Create app config value table
		String CREATE_APP_CONFIG_VALUE = "CREATE TABLE __APPCONFIG_VALUE ("
				+ "ID INTEGER NOT NULL," + "VALUE TEXT, "
				+ "FOREIGN KEY(ID) REFERENCES __APPCONFIG(ID))";

	}

}
