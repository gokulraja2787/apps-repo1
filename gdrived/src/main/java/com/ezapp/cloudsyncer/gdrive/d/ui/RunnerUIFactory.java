package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.db.AppDB;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Factory class for Runner UI
 * 
 * @author grangarajan
 *
 */
public abstract class RunnerUIFactory {

	/**
	 * Self instance
	 */
	private static RunnerUIFactory selfInstance;

	/**
	 * Holds UI impl selected app config key
	 */
	private static final String INS_KEY_ACC = "ui.impl.acc";
	
	/**
	 * Holds UI Impl List app config key 
	 */
	private static final String UI_IMPLS_KEY = "ui.impl.list";

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(RunnerUIFactory.class);

	/**
	 * Get RunnerUIFactory instance
	 * 
	 * @return RunnerUIFactory
	 */
	public static RunnerUIFactory getInstance() {
		if (null == selfInstance) {
			String insImpl;
			try {
				insImpl = getImplFromDB();
			} catch (AppDBException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				insImpl = "com.ezapp.cloudsyncer.gdrive.d.ui.impl.SimpleRunnerUIFactory";
			}
			try {
				@SuppressWarnings("unchecked")
				Class<? extends RunnerUI> clazz = (Class<? extends RunnerUI>) Class
						.forName(insImpl);
				Method meth = clazz.getMethod("getInstance", new Class[0]);
				selfInstance = (RunnerUIFactory) meth.invoke(null,
						new Object[0]);
			} catch (ClassNotFoundException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			} catch (SecurityException e) {
				LOGGER.error(
						"DI Error while finding instance. Defaulting to Simple Runner: "
								+ e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return selfInstance;
	}

	/**
	 * Get UI Instance
	 * 
	 * @return UI Instance
	 */
	public abstract RunnerUI getUIInstance();

	private static String getImplFromDB() throws AppDBException {
		AppDB appDB = AppDBFactory.getInstance().getAppDBInstance();
		String[] vals = appDB.getValues(INS_KEY_ACC);
		if (null == vals || vals.length == 0) {
			LOGGER.info("No UI Impl configured using Simple Runner UI");
			return "com.ezapp.cloudsyncer.gdrive.d.ui.impl.SimpleRunnerUIFactory";
		} else {
			return vals[0];
		}
	}

	/**
	 * Gets LIST of impls
	 * 
	 * @return
	 */
	public static String[] getImpls() {
		String[] impls = null;
		try {
			AppDB appDB = AppDBFactory.getInstance().getAppDBInstance();
			String[] vals = appDB.getValues(UI_IMPLS_KEY);
			if (null == vals || vals.length < 2) {
				impls = new String[]{"NIMBUS", "NATIVE"};
				appDB.addAppConfig(UI_IMPLS_KEY, impls);
				appDB.addAppConfig("NIMBUS", new String[]{"com.ezapp.cloudsyncer.gdrive.d.ui.impl.SimpleRunnerUIFactory"});
				appDB.addAppConfig("NATIVE", new String[]{"com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl.NativeRunnerUIFactory"});
			}
		} catch (AppDBException e) {
			LOGGER.error("DB Error while getting Impls : " + e.getMessage(), e);
		}
		return impls;
	}

}
