package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;

/**
 * Native interface definition
 * 
 * Date: May 15, 2015
 * 
 * @author grangarajan
 *
 */
class NativeRunnerUI implements RunnerUI {

	private MainFrame mainFrame;

	private static final Logger LOGGER = LogManager
			.getLogger(NativeRunnerUI.class);

	NativeRunnerUI() {
		mainFrame = new MainFrame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#start()
	 */
	public void start() {
		if (null != mainFrame) {
			mainFrame.openAndStart();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#shutdown(int)
	 */
	public int shutdown(int statusCode) {
		if (null != mainFrame) {
			try {
				mainFrame.close();
			} catch (Exception e) {
				LOGGER.warn("Error while closing instance: " + e.getMessage(),
						e);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setOAuthURL(java.lang.String)
	 */
	public void setOAuthURL(String url) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setImageIco(java.net.URL)
	 */
	public void setImageIco(URL url) {
		System.out.println("Set image");
		if (null != mainFrame) {
			mainFrame.setAppImageIcon(url);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openAddAccountWindow()
	 */
	public void openAddAccountWindow() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#toggleShowHideMainUI()
	 */
	public void toggleShowHideMainUI() {
		mainFrame.toggleVisible();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showError(java.lang.String)
	 */
	public void showError(String message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showWarning(java.lang.String)
	 */
	public void showWarning(String message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showInfo(java.lang.String)
	 */
	public void showInfo(String message) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#updateUserAccountConfig()
	 */
	public void updateUserAccountConfig() {
		// TODO Auto-generated method stub

	}

}
