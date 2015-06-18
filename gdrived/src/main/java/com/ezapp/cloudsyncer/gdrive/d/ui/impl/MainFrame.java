package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;
import com.ezapp.cloudsyncer.gdrive.d.ui.event.listener.ThemeContextListener;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import javax.swing.JSeparator;

/**
 * Frame to hold main UI
 * 
 * @author grangarajan
 * 
 *         Date: 28th May, 2015
 *
 */
class MainFrame extends JFrame implements FrameCommand {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1559968717965106427L;

	/**
	 * User account pane
	 */
	private JPanel userAccPane;
	/**
	 * User account layout for userAccPane
	 */
	private GridLayout userAccLayout;
	
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(MainFrame.class);

	MainFrame() {
		setTitle("gdrive-d Cloud Syncer");
		setSize(429, 387);

		userAccPane = new JPanel();
		userAccPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(userAccPane, GroupLayout.DEFAULT_SIZE,
								453, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap(90, Short.MAX_VALUE)
						.addComponent(userAccPane, GroupLayout.PREFERRED_SIZE,
								227, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		getContentPane().setLayout(groupLayout);

		JMenuBar appMenuBar = new JMenuBar();
		setJMenuBar(appMenuBar);

		JMenu appMenu = new JMenu("Application");
		appMenuBar.add(appMenu);

		JMenuItem appMenuExit = new JMenuItem("Exit");
		appMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.shutDown();
			}
		});

		JMenuItem appAddAccount = new JMenuItem("Add Account");
		appAddAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.addAccount();
			}
		});
		appMenu.add(appAddAccount);
		
		JMenu mnConfigure = new JMenu("Configure");
		appMenu.add(mnConfigure);
		
		JMenu mnThemeSubmenu = new JMenu("Theme");
		ThemeContextListener themeContextListner = new ThemeContextListener();
		String themes[] = RunnerUIFactory.getImpls();
		if (null != themes) {
			for (String theme : themes) {
				JMenuItem mnThemeItem = new JMenuItem(theme);
				mnThemeItem.setActionCommand(theme);
				mnThemeItem.addActionListener(themeContextListner);
				mnThemeSubmenu.add(mnThemeItem);
			}
		}
		
		JMenuItem mntmConfigureAccounts = new JMenuItem("Configure Account(s)");
		mntmConfigureAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.configureAccount();
			}
		});
		mnConfigure.add(mntmConfigureAccounts);
		mnConfigure.add(mnThemeSubmenu);
		
		JSeparator separator = new JSeparator();
		appMenu.add(separator);
		appMenu.add(appMenuExit);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setVisible(true);
	}

	/**
	 * Builds configured accounts & displays in UI
	 */
	private void buildConfiguredAccountNamesAndDisplay() {
		userAccPane.removeAll();
		JLabel userNameLabel;
		List<Account> userAccounts = Main.getConfiguredAccounts();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of accounts found: " + userAccounts.size());
		}
		userAccLayout = new GridLayout();
		userAccLayout.setColumns(3);
		int rowSize = (null != userAccounts && userAccounts.size() > 0) ? (int) Math
				.ceil((userAccounts.size() / 3.0)) : 1;
		userAccLayout.setRows(rowSize);
		userAccPane.setLayout(userAccLayout);
		for (Account acc : userAccounts) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Adding " + acc.getUserEmail());
			}
			userNameLabel = new JLabel(acc.getUserName());
			String url = acc.getPictureUrl();
			if (null != url && !url.equals("")) {
				try {
					userNameLabel.setIcon(new ImageIcon(new URL(url)));
				} catch (MalformedURLException e) {
					LOGGER.error(
							"unable to set image " + url + ": "
									+ e.getMessage(), e);
				}
			}
			userNameLabel
					.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
			userAccPane.add(userNameLabel);
			Main.reAuthenticateWithExistingAccount(acc);
		}
		userAccPane.repaint();
		userAccPane.revalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.impl.FrameCommand#updateConfiguredAccounts
	 * ()
	 */
	public void updateConfiguredAccounts() {
		buildConfiguredAccountNamesAndDisplay();
	}
}
