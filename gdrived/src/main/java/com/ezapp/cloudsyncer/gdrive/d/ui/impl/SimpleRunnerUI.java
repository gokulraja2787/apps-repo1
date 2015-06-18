package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
	 * Add account frame
	 */
	private AddAccountFrame addAccountFrame;
	/**
	 * Main frame
	 */
	private MainFrame mainFrame;
	/**
	 * Holds configure account frame
	 */
	private ConfigureAccountFrame configureAccountFrame;

	/**
	 * Initializes simple runner UI
	 */
	SimpleRunnerUI() {
		mainFrame = new MainFrame();
		addAccountFrame = new AddAccountFrame();
		configureAccountFrame = new ConfigureAccountFrame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#start()
	 */
	public void start() {
		invokeLater(mainFrame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#shutdown(int)
	 */
	public int shutdown(int statusCode) {
		configureAccountFrame.dispose();
		addAccountFrame.dispose();
		mainFrame.dispose();
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
		ImageIcon imageIcon = new ImageIcon(url);
		Image image = imageIcon.getImage();
		mainFrame.setIconImage(image);
		addAccountFrame.setIconImage(image);
		configureAccountFrame.setIconImage(image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openAddAccountWindow()
	 */
	public void openAddAccountWindow() {
		invokeLater(addAccountFrame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showHideMainUI()
	 */
	public void toggleShowHideMainUI() {
		if (mainFrame.isVisible()) {
			mainFrame.setVisible(false);
		} else {
			mainFrame.setVisible(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showError(java.lang.String)
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showWarning(java.lang.String)
	 */
	public void showWarning(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Error",
				JOptionPane.WARNING_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showInfo(java.lang.String)
	 */
	public void showInfo(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Error",
				JOptionPane.INFORMATION_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#updateUserAccountConfig()
	 */
	public void updateUserAccountConfig() {
		FrameCommand mainFrameCommand = mainFrame;
		mainFrameCommand.updateConfiguredAccounts();
		mainFrameCommand = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openConfigurationFrame()
	 */
	@Override
	public void openConfigurationFrame() {
		invokeLater(configureAccountFrame);
	}

}
