package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;

/**
 * Factory class for Native Runner UI
 * 
 * @author grangarajan
 *
 */
public class NativeRunnerUIFactory extends RunnerUIFactory {

	/**
	 * Self Instance
	 */
	private static NativeRunnerUIFactory selfInstance;

	/**
	 * UI Instance
	 */
	private static RunnerUI uiInstance;
	
	/**
	 * Singleton
	 */
	private NativeRunnerUIFactory() {
	
	}
	
	/* (non-Javadoc)
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory#getUIInstance()
	 */
	@Override
	public RunnerUI getUIInstance() {
		if(null == uiInstance) {
			uiInstance = new NativeRunnerUI();
		}
		return uiInstance;
	}

	/**
	 * Gives instance of Native Runner UI Factory
	 * 
	 * @return self instance
	 */
	public static RunnerUIFactory getInstance() {
		if(null == selfInstance) {
			selfInstance = new NativeRunnerUIFactory();
		}
		return selfInstance;
	}
	
}
