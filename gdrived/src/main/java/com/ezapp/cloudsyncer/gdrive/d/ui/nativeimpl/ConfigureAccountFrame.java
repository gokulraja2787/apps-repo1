package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

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
	 * Holds group settings
	 */
	private Group grpSettings;

	/**
	 * Self refernce
	 */
	private ConfigureAccountFrame self;

	/**
	 * Holds selected account
	 */
	private Account selectedAccount;

	/**
	 * Holds account list
	 */
	private java.util.List<Account> accountList;

	/**
	 * Configured account UI list
	 */
	private List configuredAccountList;

	/**
	 * Label to hold selected directory
	 */
	private Label lblDir;

	/**
	 * Holds old dir
	 */
	private String oldDir;

	ConfigureAccountFrame(Display display) {
		this.display = display;
		self = this;
		initUI();
	}

	private class AccountModel {

		/**
		 * Holds values
		 */
		java.util.List<Account> values;

		AccountModel() {
			values = Main.getConfiguredAccounts();
			accountList = values;
		}

		String[] getUserNames() {
			String[] unames = new String[values.size()];
			for (int i = 0; i < unames.length; i++) {
				unames[i] = values.get(i).getUserName();
			}
			return unames;
		}
	}

	/**
	 * Initializes UI
	 * 
	 * @param display
	 */
	private void initUI() {
		shell = new Shell(display);
		shell.setText("gdrive-d: Manager your accounts");
		shell.setSize(604, 441);
		shell.setLayout(new FormLayout());

		Button btnClose = new Button(shell, SWT.NONE);
		FormData fd_btnClose = new FormData();
		fd_btnClose.bottom = new FormAttachment(0, 393);
		fd_btnClose.right = new FormAttachment(0, 256);
		fd_btnClose.top = new FormAttachment(0, 364);
		fd_btnClose.left = new FormAttachment(0, 168);
		btnClose.setLayoutData(fd_btnClose);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				self.close();
			}
		});
		btnClose.setText("Close");

		configuredAccountList = new List(shell, SWT.BORDER);
		configuredAccountList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (configuredAccountList.getSelectionIndex() != -1) {
					selectedAccount = accountList.get(configuredAccountList
							.getSelectionIndex());
					if (null != selectedAccount) {
						oldDir = Main.getDirMount(selectedAccount);
						lblDir.setText(oldDir);
						enableSettingsComposite();
					}
				}
			}
		});
		FormData fd_configuredAccountList = new FormData();
		fd_configuredAccountList.bottom = new FormAttachment(0, 358);
		fd_configuredAccountList.right = new FormAttachment(0, 227);
		fd_configuredAccountList.top = new FormAttachment(0, 52);
		fd_configuredAccountList.left = new FormAttachment(0, 10);
		configuredAccountList.setLayoutData(fd_configuredAccountList);

		{
			String[] unames = new AccountModel().getUserNames();
			for (String userName : unames) {
				configuredAccountList.add(userName);
			}
		}

		Label lblNewLabel = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(0, 48);
		fd_lblNewLabel.right = new FormAttachment(0, 175);
		fd_lblNewLabel.top = new FormAttachment(0, 31);
		fd_lblNewLabel.left = new FormAttachment(0, 12);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Account(s) Configured");

		grpSettings = new Group(shell, SWT.BORDER);
		grpSettings.setText("Settings");
		grpSettings.setLayout(new FormLayout());
		FormData fd_grpSettings = new FormData();
		fd_grpSettings.bottom = new FormAttachment(btnClose, -55);
		fd_grpSettings.left = new FormAttachment(configuredAccountList, 16);
		fd_grpSettings.top = new FormAttachment(0, 52);
		fd_grpSettings.right = new FormAttachment(100, -22);
		grpSettings.setLayoutData(fd_grpSettings);

		Label lblMountInDrive = new Label(grpSettings, SWT.NONE);
		FormData fd_lblMountInDrive = new FormData();
		fd_lblMountInDrive.top = new FormAttachment(0, 10);
		fd_lblMountInDrive.left = new FormAttachment(0);
		lblMountInDrive.setLayoutData(fd_lblMountInDrive);
		lblMountInDrive.setText("Mount in drive");

		lblDir = new Label(grpSettings, SWT.WRAP);
		FormData fd_lblDir = new FormData();
		fd_lblDir.top = new FormAttachment(lblMountInDrive, 4);
		fd_lblDir.bottom = new FormAttachment(lblMountInDrive, 132, SWT.BOTTOM);
		fd_lblDir.left = new FormAttachment(0, 6);
		fd_lblDir.right = new FormAttachment(100, -7);
		lblDir.setLayoutData(fd_lblDir);
		lblDir.setText("/");

		Button btnBrowse = new Button(grpSettings, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog diag = new DirectoryDialog(shell);
				String result = diag.open();
				if (null != result) {
					lblDir.setText(result);
				}
			}
		});
		FormData fd_btnBrowse = new FormData();
		fd_btnBrowse.top = new FormAttachment(lblDir, 6);
		fd_btnBrowse.left = new FormAttachment(0);
		btnBrowse.setLayoutData(fd_btnBrowse);
		btnBrowse.setText("Change Mount Folder");

		Button btnSave = new Button(grpSettings, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Main.setDirMount(selectedAccount, lblDir.getText(), oldDir)) {
					oldDir = lblDir.getText();
				} else {
					lblDir.setText(oldDir);
				}
			}
		});
		FormData fd_btnSave = new FormData();
		fd_btnSave.bottom = new FormAttachment(btnBrowse, 0, SWT.BOTTOM);
		fd_btnSave.left = new FormAttachment(btnBrowse, 6);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");

		Button btnDelete = new Button(grpSettings, SWT.NONE);
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.top = new FormAttachment(lblDir, 6);
		fd_btnDelete.left = new FormAttachment(btnSave, 6);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Delete");

		disableSettingsComposite();
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

	/**
	 * Disables settings composite
	 */
	private void disableSettingsComposite() {
		disableComps(grpSettings);
		shell.layout(true);
		shell.redraw();
	}

	/**
	 * Disable components
	 * 
	 * @param control
	 */
	private void disableComps(Control control) {
		for (Control ctrl : grpSettings.getChildren()) {
			if (ctrl instanceof Composite) {
				disableComps(ctrl);
			} else {
				ctrl.setEnabled(false);
			}
		}
		control.setEnabled(false);
	}

	/**
	 * Enable settings composite
	 */
	private void enableSettingsComposite() {
		enableComps(grpSettings);
		shell.layout(true);
		shell.redraw();
	}

	/**
	 * ensable components
	 * 
	 * @param control
	 */
	private void enableComps(Control control) {
		for (Control ctrl : grpSettings.getChildren()) {
			if (ctrl instanceof Composite) {
				disableComps(ctrl);
			} else {
				ctrl.setEnabled(true);
			}
		}
		control.setEnabled(true);
	}
}
