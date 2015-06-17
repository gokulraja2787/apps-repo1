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
		checkAndThrowNullDB();
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
					"Error while initializing Database: " + e.getMessage(), e);
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
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#reCreateBasicSchema()
	 */
	public void reCreateBasicSchema() throws AppDBException {
		checkAndThrowNullDB();
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
			LOGGER.error("Error while creating tables: " + e.getMessage(), e);
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
		checkAndThrowNullDB();
		PreparedStatement statement = null;
		LOGGER.info("Adding " + account.getUserName() + " to db");
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
					"Error while inserting account into DB" + e.getMessage(), e);
			throw new AppDBException("Error while inserting account into DB"
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
				+ "KEY VARCHAR(10) UNIQUE)";

		// Create app config value table
		String CREATE_APP_CONFIG_VALUE_TABLE = "CREATE TABLE __APPCONFIG_VALUE ("
				+ "ID INTEGER NOT NULL,"
				+ "SEQ INTEGER NOT NULL,"
				+ "VALUE TEXT, "
				+ "PRIMARY KEY (ID, SEQ), "
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

		// Update user
		String UPDATE_ACCOUNT = "UPDATE __USER_ACC "
				+ "SET USER_NAME = ?, O_AUTH = ?, PIC_URL = ?"
				+ "WHERE USER_EMAIL = ?";

		// Insert app config key
		String ADD_APP_CONFIG_KEY = "INSERT INTO __APPCONFIG (ID, KEY) VALUES (?, ?)";

		// Insert app config value for the key
		String ADD_APP_CONFIG_VALUE = "INSERT INTO __APPCONFIG_VALUE (ID, SEQ, VALUE) VALUES (?,?,?)";

		// Update app config value of given key and sequence
		String UPDATE_APP_CONFIG_VALUE = "UPDATE __APPCONFIG_VALUE "
				+ "SET VALUE = ? "
				+ "WHERE ID = (SELECT ID FROM __APPCONFIG WHERE KEY = ?) AND SEQ = ?";

		// GET ID for Given KEY
		String GET_APP_CONFIG_KEY_ID = "SELECT ID FROM __APPCONFIG WHERE KEY = ?";

		// GET Max of ID
		String GET_APP_CONFIG_KEY_MAX_ID = "SELECT MAX(ID) AS ID FROM __APPCONFIG";

		// Get Max of SEQ for value of given key
		String GET_APP_CONFIG_VALUE_SEQ_MAX = "SELECT MAX(SEQ) AS SEQ FROM __APPCONFIG_VALUE"
				+ " WHERE ID = ?";

		// Get app config values of given key
		String GET_APP_CONFIG_VALUES = "SELECT VALUE FROM __APPCONFIG_VALUE WHERE ID = (SELECT ID FROM __APPCONFIG WHERE KEY = ?) ORDER BY SEQ";

		// Delete App config values
		String DELETE_APP_CONFIG_VALUES = "DELETE FROM __APPCONFIG_VALUE WHERE ID = (SELECT ID FROM __APPCONFIG WHERE KEY = ?)";

		// Delete app config
		String DELETE_APP_CONFIG = "DELETE FROM __APPCONFIG WHERE KEY = ?";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#getAllAccounts()
	 */
	public List<Account> getAllAccounts() throws AppDBException {
		checkAndThrowNullDB();
		List<Account> accountList = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LOGGER.info("Get all accounts");
		try {
			statement = connection.prepareStatement(QUERIES.SELECT_ALL_ACCOUNT);
			resultSet = statement.executeQuery();
			accountList = new ArrayList<Account>();
			Account account;
			while (resultSet.next()) {
				account = new Account();
				account.setAuthToken(resultSet.getString("O_AUTH"));
				account.setUserName(resultSet.getString("USER_NAME"));
				account.setUserEmail(resultSet.getString("USER_EMAIL"));
				account.setPictureUrl(resultSet.getString("PIC_URL"));
				accountList.add(account);
			}
		} catch (SQLException e) {
			throw new AppDBException("Error while fetching account details! "
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
		return accountList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#deleteAccount(java.lang.String)
	 */
	public void deleteAccount(String userEmail) throws AppDBException {
		checkAndThrowNullDB();
		PreparedStatement statement = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting " + userEmail);
		}
		try {
			statement = connection.prepareStatement(QUERIES.DELETE_ACCOUNT);
			statement.setString(1, userEmail);
			statement.execute();
		} catch (SQLException e) {
			throw new AppDBException("Error while deleting account details! "
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
		LOGGER.info("Deleted " + userEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#addAppConfig(java.lang.String,
	 * java.lang.String[])
	 */
	public void addAppConfig(String key, String... values)
			throws AppDBException {
		checkAndThrowNullDB();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addAppConfig: " + key);
		}
		try {
			Integer id;
			id = getKeyId(key);
			for (String value : values) {
				insertAppConfigValue(id, value);
			}
		} catch (SQLException e) {
			throw new AppDBException("Error while creating app config "
					+ e.getMessage(), e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("END OF addAppConfig");
		}
	}

	/**
	 * Inserts App config value
	 * 
	 * @param id
	 * @param value
	 * @throws SQLException
	 */
	private void insertAppConfigValue(Integer id, String value)
			throws SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("insertAppConfigValue: " + value);
		}
		try {
			statement = connection
					.prepareStatement(QUERIES.GET_APP_CONFIG_VALUE_SEQ_MAX);
			Integer seq;
			statement.setInt(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				seq = result.getInt("SEQ") + 1;
			} else {
				seq = 1;
			}
			result.close();
			result = null;
			statement.close();
			statement = connection
					.prepareStatement(QUERIES.ADD_APP_CONFIG_VALUE);
			statement.setInt(1, id);
			statement.setInt(2, seq);
			statement.setString(3, value);
			statement.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (null != result) {
					result.close();
				}
				if (null != statement) {
					statement.close();
				}
			} catch (SQLException e) {
			}
			result = null;
			statement = null;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("insertAppConfigValue: END");
		}
	}

	/**
	 * Gets ID for the given key. If key doesn't exist then it creates a new ID
	 * 
	 * @param key
	 * @return ID
	 * @throws SQLException
	 */
	private Integer getKeyId(String key) throws SQLException {
		Integer resultId;
		PreparedStatement statement = null;
		ResultSet result = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getKeyId: " + key);
		}
		try {
			resultId = null;
			boolean insert_key = false;
			statement = connection
					.prepareStatement(QUERIES.GET_APP_CONFIG_KEY_ID);
			statement.setString(1, key);
			result = statement.executeQuery();
			if (result.next()) {
				resultId = result.getInt("ID");
			} else {
				insert_key = true;
				result.close();
				statement.close();
				result = null;
			}
			if (insert_key) {
				statement = connection
						.prepareStatement(QUERIES.GET_APP_CONFIG_KEY_MAX_ID);
				result = statement.executeQuery();
				if (result.next()) {
					resultId = result.getInt("ID") + 1;
				} else {
					resultId = new Integer(1);
				}
				result.close();
				statement.close();
				statement = connection
						.prepareStatement(QUERIES.ADD_APP_CONFIG_KEY);
				statement.setInt(1, resultId);
				statement.setString(2, key);
				statement.execute();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (null != result)
					result.close();
				if (null != statement) {
					statement.close();
				}
			} catch (SQLException e) {
			}
			result = null;
			statement = null;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getKeyId: END");
		}
		return resultId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#updateAppConfig(java.lang.String,
	 * java.lang.Integer, java.lang.String)
	 */
	public void updateAppConfig(String key, Integer sequence, String value)
			throws AppDBException {
		checkAndThrowNullDB();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateAppConfig: " + key);
		}
		PreparedStatement statement = null;

		try {
			statement = connection
					.prepareStatement(QUERIES.UPDATE_APP_CONFIG_VALUE);
			statement.setString(1, value);
			statement.setString(2, key);
			statement.setInt(3, sequence);
			statement.execute();
		} catch (SQLException e) {
			throw new AppDBException(
					"Error while updating app config value for " + key + " "
							+ e.getMessage(), e);
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			statement = null;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateAppConfig: END");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.db.AppDB#getValues(java.lang.String)
	 */
	public String[] getValues(String key) throws AppDBException {
		checkAndThrowNullDB();
		String values[] = new String[0];
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getValues: " + key);
		}
		PreparedStatement statement = null;
		ResultSet result = null;

		try {
			statement = connection
					.prepareStatement(QUERIES.GET_APP_CONFIG_VALUES);
			statement.setString(1, key);
			result = statement.executeQuery();
			List<String> res = new ArrayList<String>();
			while (result.next()) {
				res.add(result.getString("VALUE"));
			}
			values = res.toArray(values);
			res.clear();
			res = null;
		} catch (SQLException e) {
			throw new AppDBException(
					"Error while getting app config value for " + key + " "
							+ e.getMessage(), e);
		} finally {
			try {
				if (null != result) {
					result.close();
				}
				if (null != statement) {
					statement.close();
				}
			} catch (SQLException e) {
			}
			result = null;
			statement = null;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getValues: END");
		}
		return values;
	}

	/**
	 * If connection is null then it throws Exception
	 * 
	 * @throws AppDBException
	 */
	private void checkAndThrowNullDB() throws AppDBException {
		if (null == connection) {
			throw new AppDBException("No DB found");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#deleteAppConfig(java.lang.String)
	 */
	public void deleteAppConfig(String key) throws AppDBException {
		checkAndThrowNullDB();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteAppConfig: " + key);
		}
		PreparedStatement statement = null;
		try {
			statement = connection
					.prepareStatement(QUERIES.DELETE_APP_CONFIG_VALUES);
			statement.setString(1, key);
			statement.execute();
			statement.close();
			statement = null;
			statement = connection.prepareStatement(QUERIES.DELETE_APP_CONFIG);
			statement.setString(1, key);
			statement.execute();
		} catch (SQLException e) {
			throw new AppDBException("Error while deleting app config for "
					+ key + " " + e.getMessage(), e);
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			statement = null;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleteAppConfig: End");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.db.AppDB#updateAccount(com.ezapp.cloudsyncer
	 * .gdrive.d.vo.Account)
	 */
	@Override
	public void updateAccount(Account account) throws AppDBException {
		checkAndThrowNullDB();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updateAccount: Updating " + account.getUserEmail());
		}
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(QUERIES.UPDATE_ACCOUNT);
			statement.setString(4, account.getUserEmail());
			statement.setString(1, account.getUserName());
			statement.setString(2, account.getAuthToken());
			statement.setString(3, account.getPictureUrl());
			statement.execute();

		} catch (SQLException e) {
			throw new AppDBException("Error while updating user info "
					+ e.getMessage(), e);
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			statement = null;
		}

	}

}
