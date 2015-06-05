package com.ezapp.cloudsyncer.gdrive.d.db.impl.sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
					return (cnt == 3);
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
			dropAllTables();
			PreparedStatement statement = null;
			try {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating table __APPCONFIG");
				}
				statement = connection
						.prepareStatement(QUERIES.CREATE_APP_CONFIG_TABLE);
				statement.execute();
				statement.close();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating table __APPCONFIG_VALUE");
				}
				statement = connection
						.prepareStatement(QUERIES.CREATE_APP_CONFIG_VALUE_TABLE);
				statement.execute();
				statement.close();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating table __USER_ACC");
				}
				statement = connection
						.prepareStatement(QUERIES.CREATE_ACCOUNT_TABLE);
				statement.execute();
				statement.close();
			} catch (SQLException e) {
				LOGGER.error("Error while creating tables: " + e.getMessage(),
						e);
			} finally {
				if (null != statement) {
					try {
						statement.close();
					} catch (SQLException e) {
						LOGGER.warn(e.getMessage(), e);
					} finally {
						statement = null;
					}
				}
			}
		} else {
			throw new AppDBException("No DB found");
		}
	}

	/**
	 * Drops all existing table if exist
	 * 
	 * @throws AppDBException
	 */
	private void dropAllTables() throws AppDBException {
		PreparedStatement statement = null;
		LOGGER.info("Dropping existing table");
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dropping __USER_ACC");
			}
			statement = connection.prepareStatement(QUERIES.DROP_TABLE
					+ "__USER_ACC");
			statement.execute();
			statement.close();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dropping __APPCONFIG_VALUE");
			}
			statement = connection.prepareStatement(QUERIES.DROP_TABLE
					+ "__APPCONFIG_VALUE");
			statement.execute();
			statement.close();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dropping __APPCONFIG");
			}
			statement = connection.prepareStatement(QUERIES.DROP_TABLE
					+ "__APPCONFIG");
			statement.execute();
		} catch (SQLException e) {
			LOGGER.error("Error while droping tables: " + e.getMessage(), e);
			throw new AppDBException("Error while droping tables: "
					+ e.getMessage(), e);
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					LOGGER.warn(e.getMessage(), e);
				} finally {
					statement = null;
				}
			}
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
		PreparedStatement statement = null;
		LOGGER.info("Adding " + account.getUserName() + " to db");
		if (null != connection) {
			try {
				statement = connection
						.prepareStatement(QUERIES.INSERT_INTO_ACCOUNT);
				statement.setString(1, account.getUserEmail());
				statement.setString(2, account.getUserName());
				statement.setString(3, account.getAuthToken());
				if (null != account.getPictureUrl()) {
					statement.setString(4, account.getPictureUrl());
				} else {
					statement.setString(4, null);
				}
				statement.execute();
			} catch (SQLException e) {
				LOGGER.error(
						"Error while inserting account into DB"
								+ e.getMessage(), e);
				throw new AppDBException(
						"Error while inserting account into DB"
								+ e.getMessage(), e);
			} finally {
				if (null != statement) {
					try {
						statement.close();
					} catch (SQLException e) {
						LOGGER.warn(e.getMessage(), e);
					} finally {
						statement = null;
					}
				}
			}
		} else {
			throw new AppDBException("No DB found");
		}
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
		String CHECK_TABLE = "SELECT COUNT(name) __CNT FROM sqlite_master WHERE type='table'";

		// Drops the table
		String DROP_TABLE = "DROP TABLE IF EXISTS ";

		// Create app config table
		String CREATE_APP_CONFIG_TABLE = "CREATE TABLE __APPCONFIG ("
				+ "ID INTEGER PRIMARY KEY NOT NULL,"
				+ "KEY VARCHAR(10) NOT NULL)";

		// Create app config value table
		String CREATE_APP_CONFIG_VALUE_TABLE = "CREATE TABLE __APPCONFIG_VALUE ("
				+ "ID INTEGER NOT NULL,"
				+ "VALUE TEXT, "
				+ "FOREIGN KEY(ID) REFERENCES __APPCONFIG(ID))";

		// Create account table
		String CREATE_ACCOUNT_TABLE = "CREATE TABLE __USER_ACC ("
				+ "USER_EMAIL TEXT PRIMARY KEY NOT NULL,"
				+ "USER_NAME TEXT NOT NULL," + "O_AUTH TEXT NOT NULL,"
				+ "PIC_URL DEFAULT TEXT)";

		// Insert an account
		String INSERT_INTO_ACCOUNT = "INSERT INTO __USER_ACC "
				+ "VALUES (?, ?, ?, ?)";

		// Query to get all accounts
		String SELECT_ALL_ACCOUNT = "SELECT USER_EMAIL, USER_NAME, O_AUTH, PIC_URL FROM __USER_ACC";

		// Delete User account
		String DELETE_ACCOUNT = "DELETE FROM __USER_ACC WHERE USER_EMAIL = ?";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#getAllAccounts()
	 */
	public List<Account> getAllAccounts() throws AppDBException {
		List<Account> accountList = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LOGGER.info("Get all accounts");
		if (null != connection) {
			try {
				statement = connection
						.prepareStatement(QUERIES.SELECT_ALL_ACCOUNT);
				resultSet = statement.executeQuery();
				accountList = new ArrayList<Account>();
				Account account;
				while (resultSet.next()) {
					account = new Account();
					account.setAuthToken(resultSet.getString("O_AUTH"));
					account.setUserName(resultSet.getString("USER_NAME"));
					account.setUserEmail(resultSet.getString("USER_EMAIL"));
					accountList.add(account);
				}
			} catch (SQLException e) {
				throw new AppDBException(
						"Error while fetching account details! "
								+ e.getMessage(), e);
			} finally {
				try {
					if (null != resultSet) {
						resultSet.close();
					}
					if (null != statement) {
						statement.close();
					}
				} catch (SQLException e) {
				} finally {
					resultSet = null;
					statement = null;
				}
			}
		} else {
			throw new AppDBException("No DB found");
		}
		return accountList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#deleteAccount(java.lang.String)
	 */
	public void deleteAccount(String userEmail) throws AppDBException {
		PreparedStatement statement = null;
		if(LOGGER.isDebugEnabled())		{
			LOGGER.debug("Deleting " + userEmail);
		}
		if (null != connection) {
			try {
				statement = connection.prepareStatement(QUERIES.DELETE_ACCOUNT);
				statement.setString(1, userEmail);
				statement.execute();
			} catch (SQLException e) {
				throw new AppDBException(
						"Error while deleting account details! "
								+ e.getMessage(), e);
			} finally {
				try {
					if (null != statement) {
						statement.close();
					}
				} catch (SQLException e) {
				} finally {
					statement = null;
				}
			}
		} else {
			throw new AppDBException("No DB found");
		}
		LOGGER.info("Deleted " + userEmail);
	}

}
