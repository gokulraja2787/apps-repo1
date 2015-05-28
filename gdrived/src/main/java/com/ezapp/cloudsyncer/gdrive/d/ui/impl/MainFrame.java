package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.ezapp.cloudsyncer.gdrive.d.Main;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Frame to hold main UI
 * 
 * @author grangarajan
 * 
 *         Date: 28th May, 2015
 *
 */
class MainFrame extends JFrame implements Runnable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1559968717965106427L;

	MainFrame() {
		setTitle("gdrive-d Cloud Syncer");
		setSize(200, 150);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGap(0, 434, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGap(0, 262, Short.MAX_VALUE));
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

}
