package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;

/**
 * Native interface definition
 * 
 * Date: May 15, 2015
 * 
 * @author grangarajan
 *
 */
class NativeRunnerUI implements RunnerUI {

	/**
	 * Main frame
	 */
	private MainFrame mainFrame;

	/**
	 * Add account frame
	 */
	private AddAccountFrame addAccountFrame;

	/**
	 * Holds configure account frame
	 */
	private ConfigureAccountFrame configureAccountFrame;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(NativeRunnerUI.class);

	NativeRunnerUI() {
		mainFrame = new MainFrame();
		addAccountFrame = new AddAccountFrame(mainFrame.getDisplay());
		configureAccountFrame = new ConfigureAccountFrame(
				mainFrame.getDisplay());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#start()
	 */
	public void start() {
		if (null != mainFrame) {
			mainFrame.openAndStart();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#shutdown(int)
	 */
	public int shutdown(int statusCode) {
		if (null != configureAccountFrame) {
			try {
				configureAccountFrame.close();
			} catch (Exception e) {
				LOGGER.warn("Error while closing instance: " + e.getMessage(),
						e);
			}
		}
		if (null != addAccountFrame) {
			try {
				addAccountFrame.close();
			} catch (Exception e) {
				LOGGER.warn("Error while closing instance: " + e.getMessage(),
						e);
			}
		}
		if (null != mainFrame) {
			try {
				mainFrame.close();
			} catch (Exception e) {
				LOGGER.warn("Error while closing instance: " + e.getMessage(),
						e);
			}
		}

		addAccountFrame = null;
		mainFrame = null;
		configureAccountFrame = null;
		return statusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setOAuthURL(java.lang.String)
	 */
	public void setOAuthURL(String url) {
		if (null != addAccountFrame) {
			addAccountFrame.setOAuthURL(url);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setImageIco(java.net.URL)
	 */
	public void setImageIco(URL url) {
		if (null != mainFrame) {
			mainFrame.setAppImageIcon(url);
		}
		if (null != addAccountFrame) {
			addAccountFrame.setAppImageIcon(url);
		}
		if (null != configureAccountFrame) {
			configureAccountFrame.setAppImageIcon(url);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openAddAccountWindow()
	 */
	public void openAddAccountWindow() {
		if (null != addAccountFrame) {
			addAccountFrame.openAndStart();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#toggleShowHideMainUI()
	 */
	public void toggleShowHideMainUI() {
		mainFrame.toggleVisible();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showError(java.lang.String)
	 */
	public void showError(String message) {
		AppDialog dialog = new AppDialog(getShell());
		dialog.open(message, SWT.ICON_ERROR);
	}

	/**
	 * Gets shell for window
	 * 
	 * @return shell
	 */
	private Shell getShell() {
		Shell shell = null;
		if (null == mainFrame.getDisplay().getActiveShell()) {
			Display display = mainFrame.getDisplay();
			shell = new Shell(display);

		} else {
			shell = mainFrame.getDisplay().getActiveShell();
		}
		return shell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showWarning(java.lang.String)
	 */
	public void showWarning(String message) {
		AppDialog dialog = new AppDialog(getShell());
		dialog.open(message, SWT.ICON_WARNING);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showInfo(java.lang.String)
	 */
	public void showInfo(String message) {
		AppDialog dialog = new AppDialog(getShell());
		dialog.open(message, SWT.ICON_INFORMATION);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#updateUserAccountConfig()
	 */
	public void updateUserAccountConfig() {
		if (null != mainFrame) {
			Display display = mainFrame.getDisplay();
			if (null != display && !display.isDisposed()) {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						mainFrame.updateConfiguredAccounts();
					}
				});
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openConfigurationFrame()
	 */
	@Override
	public void openConfigurationFrame() {
		if (null != configureAccountFrame) {
			configureAccountFrame.openAndStart();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openRemoteBrowserFrame()
	 */
	@Override
	public void openRemoteBrowserFrame() {
		// TODO Impl pending

	}

}
