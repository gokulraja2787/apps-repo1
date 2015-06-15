package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.ezapp.cloudsyncer.gdrive.d.Main;

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

	MainFrame() {
		display = new Display();
		shell = new Shell(display);
		shell.setText("gdrive-d Cloud Syncer");
		shell.setSize(429, 387);
		shell.setLayout(null);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmApplication = new MenuItem(menu, SWT.CASCADE);
		mntmApplication.setText("Application");

		Menu menu_1 = new Menu(mntmApplication);
		mntmApplication.setMenu(menu_1);

		MenuItem mntmAddAccount = new MenuItem(menu_1, SWT.NONE);
		mntmAddAccount.setText("Add Account");

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Main.shutDown();
			}
		});
		mntmExit.setText("Exit");
		shell.addListener(SWT.Close, new Listener() {

			public void handleEvent(Event event) {
				event.doit = false;
				shell.setVisible(false);
			}
		});
	}

	/**
	 * Opens the shell
	 */
	public void openAndStart() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Closes display & shell
	 */
	public void close() {
		if (null != display) {
			display.close();
		}
		if (null != shell && !shell.isDisposed()) {
			shell.close();
		}
		display = null;
		shell = null;
	}

	public void setAppImageIcon(URL url) {
		shell.setImage(SWTResourceManager.getImage(MainFrame.class,
				url.toString()));
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
}
