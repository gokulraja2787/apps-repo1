package test.ezapp.cloudsyncer.gdrive.d.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * @author gokul
 *
 */
public class TestAppDB {

	/**
	 * DB
	 */
	private AppDB appDB;

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
	public void testIfAppConfigExist() {
		if (null == appDB) {
			fail("No DB Connection");
		} else {
			try {
				boolean result = appDB.isAppConfigExist();
				if (result) {
					assertTrue("App config Does exist", true);
				} else {
					assertTrue("App config Doesn't exist", true);
					appDB.checkAndCreateBasicSchema();
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
	public void testInsertAccount() {
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

}
