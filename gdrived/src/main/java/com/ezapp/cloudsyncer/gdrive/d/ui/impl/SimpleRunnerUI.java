package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import static javax.swing.SwingUtilities.invokeLater;

import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;

/**
 * Simple interface definition
 * 
 * @author grangarajan
 * 
 *         Date: Mat 22, 2015
 *
 */
class SimpleRunnerUI implements RunnerUI {

	/**
	 * Frame to display
	 */
	private SimpleFrame mainFrame;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#start()
	 */
	public void start() {
		mainFrame = new SimpleFrame();
		/*
		 * mainFrame.setSize(400, 400);
		 * mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		 */

		invokeLater(mainFrame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#shutdown(int)
	 */
	public int shutdown(int statusCode) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setOAuthURL(java.lang.String)
	 */
	public void setOAuthURL(String url) {
		if (null != mainFrame) {
			mainFrame.setOAuthURL(url);
		}

	}

}
