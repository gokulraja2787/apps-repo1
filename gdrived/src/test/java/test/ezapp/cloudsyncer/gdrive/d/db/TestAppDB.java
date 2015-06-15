package test.ezapp.cloudsyncer.gdrive.d.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * @author gokul
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAppDB {

	/**
	 * DB
	 */
	private AppDB appDB;

	private String appConfigValues[] = { "Test value 1", "Test value 2" };
	private String appConfigKey = "Test Key";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		appDB = AppDBFactory.getInstance().getAppDBInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		appDB = null;
	}

	/**
	 * Test if appconfig exist
	 */
	@Test
	public void test1IfAppConfigExist() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				boolean result = appDB.isAppConfigExist();
				if (result) {
					assertTrue("App config Does exist", true);
				} else {
					assertTrue("App config Doesn't exist", true);
					appDB.reCreateBasicSchema();
					result = appDB.isAppConfigExist();
					if (result) {
						assertTrue("App config Does exist", true);
					} else {
						fail("Failed creating appconfig!!!");
					}
				}
			} catch (AppDBException e) {
				fail(e.getMessage() + " failed!!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Test insert account
	 */
	@Test
	public void test2InsertAccount() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			Account account = new Account();
			List<Account> accountList = null;
			account.setUserName("Gokul Rangarajan");
			account.setUserEmail("gokulraja2006@gmail.com");
			account.setAuthToken("dummy155345355655345244424424224346556756");
			try {
				boolean found;
				appDB.addAccount(account);
				accountList = appDB.getAllAccounts();
				for (Account element : accountList) {
					found = element.getUserEmail().equals(
							account.getUserEmail());
					if (found) {
						appDB.deleteAccount(account.getUserEmail());
						assertTrue("Inserted record found!!!", found);
						return;
					}
				}
				fail("Inserted record not found !!!");
			} catch (AppDBException e) {
				fail(e.getMessage() + " failed!!");
			} finally {
				accountList = null;
				account = null;
			}
		}
	}

	/**
	 * Test add app config
	 */
	@Test
	public void test3AddAppConfig() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				appDB.addAppConfig(appConfigKey, appConfigValues);
			} catch (AppDBException e) {
				e.printStackTrace(System.err);
				fail(e.getMessage() + " failed!!");
			}
		}
	}

	/**
	 * Test get app config
	 */
	@Test
	public void test4GetAppConfig() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				String[] vals = appDB.getValues(appConfigKey);
				for(String v : vals) {
					System.out.println(v);
				}
				assertFalse(
						"Either no values found or app config values doesn't match",
						(null == vals || vals.length != appConfigValues.length));
			} catch (AppDBException e) {
				e.printStackTrace();
				fail(e.getMessage() + " failed!!");
			}
		}
	}
	
	/**
	 * Test update app config value
	 */
	@Test
	public void test5UpdateAppConfig() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				appDB.updateAppConfig(appConfigKey, 1, "Updated Value");
				String vals[] = appDB.getValues(appConfigKey);
				for(String v : vals) {
					System.out.println(v);
				}
				assertFalse(
						"Either no values found or app config values doesn't match",
						(null == vals || vals.length != appConfigValues.length));
				assertFalse("Updated Failed!", (null == vals) || !vals[1].equals(appConfigValues[1]));
			} catch (AppDBException e) {
				e.printStackTrace();
				fail(e.getMessage() + " failed!!");
			}
		}
	}
	
	/**
	 * Test delete app config
	 */
	@Test
	public void test6DeleteAppConfig() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				appDB.deleteAppConfig(appConfigKey);
				String vals[] = appDB.getValues(appConfigKey);
				assertFalse(
						"Delete failed",
						(null != vals && vals.length != 0));
			} catch (AppDBException e) {
				e.printStackTrace();
				fail(e.getMessage() + " failed!!");
			}
		}
	}
}
