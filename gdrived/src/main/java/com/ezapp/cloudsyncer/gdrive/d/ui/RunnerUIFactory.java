package com.ezapp.cloudsyncer.gdrive.d.ui;

import com.ezapp.cloudsyncer.gdrive.d.ui.impl.SimpleRunnerUIFactory;

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
	 * Get RunnerUIFactory instance
	 * 
	 * @return RunnerUIFactory
	 */
	public static RunnerUIFactory getInstance() {
		if (null == selfInstance) {
			selfInstance = SimpleRunnerUIFactory.getInstance();
			//TODO bring DI!!!
		}
		return selfInstance;
	}

	/**
	 * Get UI Instance
	 * 
	 * @return UI Instance
	 */
	public abstract RunnerUI getUIInstance();

}
