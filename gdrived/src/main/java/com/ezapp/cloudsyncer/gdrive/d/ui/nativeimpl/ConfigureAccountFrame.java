package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

/**
 * Frame to hold Configure window
 * 
 * Date: June 17, 2015
 * 
 * @author grangarajan
 *
 */
class ConfigureAccountFrame {

	/**
	 * Holds shell
	 */
	private Shell shell;

	/**
	 * Holds display
	 */
	private Display display;

	/**
	 * Self refernce
	 */
	private ConfigureAccountFrame self;
	
	ConfigureAccountFrame(Display display) {
		this.display = display;
		self = this;
		initUI();
	}
	
	/**
	 * Initializes UI
	 * @param display
	 */
	private void initUI() {
		shell = new Shell(display);
		shell.setText("gdrive-d: Manager your accounts");
		shell.setSize(433, 441);
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				self.close();
			}
		});
		btnClose.setBounds(168, 364, 88, 29);
		btnClose.setText("Close");
		
		List configuredAccountList = new List(shell, SWT.BORDER);
		configuredAccountList.setBounds(10, 52, 217, 306);
		
		{
			java.util.List<Account> accounts = Main.getConfiguredAccounts();
			for(Account account : accounts) {			
				configuredAccountList.add(account.toString());
			}
		}
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(12, 31, 163, 17);
		lblNewLabel.setText("Account(s) Configured");
	}
	
	/**
	 * Opens the shell
	 */
	public void openAndStart() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (null == shell || shell.isDisposed()) {
					initUI();
				}
				shell.open();
				while (null != shell && !shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
			}

		});
	}
	
	/**
	 * Closes display & shell
	 */
	public void close() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (null != shell && !shell.isDisposed()) {
					shell.close();
				}
				shell = null;
			}
		});
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
}
