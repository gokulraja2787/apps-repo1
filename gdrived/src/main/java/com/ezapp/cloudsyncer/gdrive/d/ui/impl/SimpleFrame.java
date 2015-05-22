package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * Frame to hold UI details
 * 
 * @author grangarajan
 *
 */
class SimpleFrame extends JFrame implements Runnable {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -6851815624716027668L;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setVisible(true);
	}

	/**
	 * @throws HeadlessException
	 */
	public SimpleFrame() throws HeadlessException {
		super("gdrived");
	}

	
	
}
