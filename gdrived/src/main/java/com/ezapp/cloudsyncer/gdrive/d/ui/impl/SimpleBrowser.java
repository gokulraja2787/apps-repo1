package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Simple web browser
 * 
 * @author grangarajan
 *
 */
public class SimpleBrowser {

	/**
	 * Specifies shared display
	 */
	private boolean sharedDisplay = true;

	/**
	 * Shell to hold components
	 */
	private Shell bshell;
	/**
	 * HTTP, HTTPS browser
	 */
	private Browser browser;
	/**
	 * Display to hold shell
	 */
	private Display bdisplay;

	/**
	 * Holds LOGGER
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(SimpleBrowser.class);

	/**
	 * initUI
	 */
	private void initUI() {
		bshell.setText("Google Authenticate");
		bshell.setSize(617, 558);
		bshell.setLayout(null);
		browser = new Browser(bshell, SWT.NONE);
		browser.setBounds(0, 0, 618, 508);

		Button btnCancel = new Button(bshell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				bdisplay.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (!bshell.isDisposed()) {
							bshell.close();
						}
					}
				});
			}
		});
		btnCancel.setBounds(261, 514, 88, 29);
		btnCancel.setText("Cancel");
		bshell.addListener(SWT.Dispose, new Listener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.
			 * widgets.Event)
			 */
			@Override
			public void handleEvent(Event event) {
				close();
			}
		});
	}

	/**
	 * 
	 * @param display
	 */
	public SimpleBrowser(Display display) {
		if (null == display) {
			sharedDisplay = false;
			display = new Display();
		}
		this.bdisplay = display;
		this.bshell = new Shell(this.bdisplay);
		initUI();
	}

	/**
	 * Opens the browser window
	 */
	public void openBrowser() {
		LOGGER.info("Opening browser window");
		/*
		 * bdisplay.asyncExec(new Runnable() {
		 * 
		 * @Override public void run() {
		 */

		bshell.open();
		while (!bshell.isDisposed()) {
			if (!bdisplay.readAndDispatch())
				bdisplay.sleep();
		}
		// }
		// });
	}

	/**
	 * Opens the browser window with URL to load
	 * 
	 * @param url
	 */
	public void openBrowser(String url) {
		openBrowser();
		openUrl(url);
	}

	/**
	 * Loads the browser URL
	 * 
	 * @param url
	 */
	public void openUrl(final String url) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Opening URL: " + url);
		}
		try {
			// bdisplay.wake();
			bdisplay.asyncExec(new Runnable() {

				@Override
				public void run() {
					browser.setUrl(url);

				}
			});

		} catch (Exception e) {
			LOGGER.error("Error while opeing URL: " + e.getMessage(), e);
		}
	}

	/**
	 * Close the browser
	 */
	public void close() {
		if (null != bdisplay && !sharedDisplay) {
			bdisplay.dispose();
		}
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
