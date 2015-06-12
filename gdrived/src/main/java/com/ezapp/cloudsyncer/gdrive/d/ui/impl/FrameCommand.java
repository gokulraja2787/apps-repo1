package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

/**
 * Command definition for MainFrame
 * 
 * @author grangarajan
 * 
 *         Date: May 12, 2015
 *
 */
interface FrameCommand extends Runnable {

	/**
	 * Update configured accounts
	 */
	void updateConfiguredAccounts();

}
