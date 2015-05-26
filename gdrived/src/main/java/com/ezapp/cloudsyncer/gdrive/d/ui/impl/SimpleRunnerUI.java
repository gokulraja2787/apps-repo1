package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

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
		if (statusCode != 0) {
			mainFrame.dispose();
		}
		return statusCode;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setImageIco(java.net.URL)
	 */
	public void setImageIco(URL url) {
		ImageIcon imageIcon = new ImageIcon(url);
		Image image = imageIcon.getImage();
		mainFrame.setIconImage(image);
	}

}
