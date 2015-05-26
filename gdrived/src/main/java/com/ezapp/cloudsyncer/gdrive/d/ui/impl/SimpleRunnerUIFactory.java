package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;

/**
 * Factory class for Simple Runner UI
 * 
 * @author gokul
 *
 */
public class SimpleRunnerUIFactory extends RunnerUIFactory {

	/**
	 * Self Instance
	 */
	private static SimpleRunnerUIFactory selfInstance;

	/**
	 * UI Instance
	 */
	private static RunnerUI uiInstance;

	/**
	 * Singleton
	 */
	private SimpleRunnerUIFactory() {
	}

	/**
	 * 
	 * @return SimpleRunnerFactory UI
	 */
	public static SimpleRunnerUIFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = new SimpleRunnerUIFactory();
		}
		return selfInstance;
	}

	/**
	 * Gets UI implementation
	 * 
	 * @return runner UI
	 */
	@Override
	public RunnerUI getUIInstance() {
		if (null == uiInstance) {
			uiInstance = new SimpleRunnerUI();
		}
		return uiInstance;
	}

}
