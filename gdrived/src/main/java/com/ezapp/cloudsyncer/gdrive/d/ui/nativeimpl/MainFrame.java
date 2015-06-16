package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;
import com.ezapp.cloudsyncer.gdrive.d.ui.event.listener.ThemeContextListener;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * Frame to hold main UI
 * 
 * Date May 15, 2015
 * 
 * @author grangarajan
 *
 */
class MainFrame {

	/**
	 * Holds shell
	 */
	private Shell shell;

	/**
	 * Holds display
	 */
	private Display display;

	/**
	 * Holds user accounts
	 */
	private Composite usrAccPane;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(MainFrame.class);

	MainFrame() {
		display = new Display();
		shell = new Shell(display);
		shell.setText("gdrive-d Cloud Syncer");
		shell.setSize(429, 387);
		shell.setLayout(new FormLayout());

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmApplication = new MenuItem(menu, SWT.CASCADE);
		mntmApplication.setText("Application");

		Menu menu_1 = new Menu(mntmApplication);
		mntmApplication.setMenu(menu_1);

		MenuItem mntmAddAccount = new MenuItem(menu_1, SWT.NONE);
		mntmAddAccount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Main.addAccount();
			}
		});
		mntmAddAccount.setText("Add Account");

		MenuItem mntmConfigureMenu = new MenuItem(menu_1, SWT.CASCADE);
		mntmConfigureMenu.setText("Configure");

		Menu menu_2 = new Menu(mntmConfigureMenu);
		mntmConfigureMenu.setMenu(menu_2);

		MenuItem mntmThemeSubmenu = new MenuItem(menu_2, SWT.CASCADE);
		mntmThemeSubmenu.setText("Theme");

		Menu menu_3 = new Menu(mntmThemeSubmenu);
		mntmThemeSubmenu.setMenu(menu_3);

		String[] impls = RunnerUIFactory.getImpls();
		if (null != impls) {
			ThemeContextListener listener = new ThemeContextListener();
			for (String impl : impls) {
				MenuItem mnuThItn = new MenuItem(menu_3, SWT.NONE);
				mnuThItn.setText(impl);
				mnuThItn.addSelectionListener(listener);
			}
			listener = null;
		}

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Main.shutDown();
			}
		});
		mntmExit.setText("Exit");

		initAccPane();
		shell.addListener(SWT.Close, new Listener() {

			public void handleEvent(Event event) {
				event.doit = false;
				shell.setVisible(false);
			}
		});
	}

	/**
	 * Initializes acc pane
	 */
	private void initAccPane() {
		usrAccPane = new Composite(shell, SWT.BORDER);
		FormData fd_usrAccPane = new FormData();
		fd_usrAccPane.bottom = new FormAttachment(0, 319);
		fd_usrAccPane.right = new FormAttachment(0, 403);
		fd_usrAccPane.top = new FormAttachment(0, 77);
		fd_usrAccPane.left = new FormAttachment(0, 10);
		usrAccPane.setLayoutData(fd_usrAccPane);
		GridLayout gl_usrAccPane = new GridLayout(1, false);
		gl_usrAccPane.verticalSpacing = 2;
		usrAccPane.setLayout(gl_usrAccPane);

		List<Account> userAccounts = Main.getConfiguredAccounts();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of accounts found: " + userAccounts.size());
		}
		for (Account acc : userAccounts) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Adding " + acc.getUserEmail());
			}
			String url = acc.getPictureUrl();
			if (null != url && !url.equals("")) {
				Label picLbl = new Label(usrAccPane, SWT.BORDER);
				try {
					picLbl.setImage(SWTResourceManager.getImage(new URL(url)
							.openStream()));
				} catch (IOException e) {
					LOGGER.error("Failed to draw immage: " + e.getMessage(), e);
				}
			}
			Label userLbl = new Label(usrAccPane, SWT.BORDER | SWT.WRAP);
			userLbl.setText(acc.getUserEmail());
		}
		shell.redraw();
		shell.layout(true);
	}

	/**
	 * Opens the shell
	 */
	public void openAndStart() {
		if (null != display) {
			shell.open();
			while (null != shell && !shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
	}

	/**
	 * Closes display & shell
	 */
	public void close() {
		SWTResourceManager.dispose();
		if (null != display) {
			if (null != shell && !shell.isDisposed()) {
				shell.close();
			}
			if (null != display && !display.isDisposed()) {
				display.dispose();
			}
			shell = null;
			display = null;
		}
	}

	/**
	 * Set app image icon
	 * 
	 * @param url
	 */
	public void setAppImageIcon(URL url) {
		String urlloc = url.toString();
		try {
			urlloc = urlloc.substring(urlloc.indexOf("com/ezapp") - 1);
		} catch (Exception e) {
			urlloc = "/com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png";
		}
		final String urlLc = urlloc;
		if (null != display && !display.isDisposed()) {
			display.asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					shell.setImage(SWTResourceManager.getImage(MainFrame.class,
							urlLc));

				}
			});
		}
	}

	/**
	 * Toggles visibility
	 */
	public void toggleVisible() {
		if (null != shell && null != display && !display.isDisposed()) {
			display.asyncExec(new Runnable() {

				public void run() {
					if (shell.isVisible()) {
						shell.setVisible(false);
					} else {
						shell.setVisible(true);
					}
				}
			});
		}
	}

	/**
	 * Get display
	 * 
	 * @return display
	 */
	Display getDisplay() {
		return display;
	}

	/**
	 * Update configured account
	 */
	public void updateConfiguredAccounts() {
		usrAccPane.dispose();
		initAccPane();
	}
}
