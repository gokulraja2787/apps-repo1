package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Simple web browser
 * 
 * @author grangarajan
 *
 */
public class SimpleBrowser {

	/**
	 * Shell to hold components
	 */
	private final Shell bshell;
	/**
	 * HTTP, HTTPS browser
	 */
	private final Browser browser;
	/**
	 * Display to hold shell
	 */
	private final Display bdisplay;

	/**
	 * Holds LOGGER
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(SimpleBrowser.class);

	/**
	 * Initialize the browser
	 */
	public SimpleBrowser() {
		bdisplay = new Display();
		bshell = new Shell(bdisplay);
		bshell.setText("Google Authenticate");
		bshell.setSize(666, 542);
		bshell.setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(bshell, SWT.NONE);
		bshell.addListener(SWT.Dispose, new Listener() {

			public void handleEvent(Event event) {
				close();
			}
		});
	}

	/**
	 * Opens the browser window
	 */
	public void openBrowser() {
		LOGGER.info("Opening browser window");
		bshell.open();
	}

	/**
	 * Opens the browser window with URL to load
	 * 
	 * @param url
	 */
	public void openBrowser(String url) {
		bshell.open();
		openUrl(url);
	}

	/**
	 * Loads the browser URL
	 * 
	 * @param url
	 */
	public void openUrl(String url) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Opening URL: " + url);
		}
		try {
			bdisplay.wake();
			browser.setUrl(url);
			while (!bshell.isDisposed()) {
				if (!bdisplay.readAndDispatch())
					bdisplay.sleep();
			}
		} catch (Exception e) {
			LOGGER.error("Error while opeing URL: " + e.getMessage(), e);
		} finally {
			if (!bshell.isDisposed()) {
				bshell.dispose();
			}
		}
	}

	/**
	 * Close the browser
	 */
	public void close() {
		bdisplay.dispose();
	}

	/**
	 * Checks if browser is closed
	 * 
	 * @return true if browser is closed or no instance of browser else false
	 */
	public boolean isClosed() {
		return (bdisplay == null || bdisplay.isDisposed());
	}

}
