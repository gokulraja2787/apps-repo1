package com.ezapp.cloudsyncer.gdrive.d.ui;

/**
 * Factory class for Runner UI
 * 
 * @author grangarajan
 *
 */
public class RunnerUIFactory {

	/**
	 * Self instance
	 */
	private static RunnerUIFactory selfInstance;
	
	/**
	 * Singleton
	 */
	private RunnerUIFactory(){}

	/**
	 * Get RunnerUIFactory instance
	 * @return RunnerUIFactory
	 */
	public static RunnerUIFactory getInstance(){
		if (null == selfInstance) {
			selfInstance = new RunnerUIFactory();
		}
		return selfInstance;
	}
	
}
