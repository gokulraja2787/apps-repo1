package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

/**
 * Frame to hold Configure window
 * 
 * Date: June 17, 2015
 * 
 * @author grangarajan
 *
 */
class ConfigureAccountFrame extends JFrame implements Runnable {

	/**
	 * AccountModel for JList
	 * 
	 * @author gokul
	 *
	 */
	private class AccountModel extends AbstractListModel<Account> {

		/**
		 * Holds values
		 */
		List<Account> values;

		AccountModel() {
			values = Main.getConfiguredAccounts();
		}

		/**
		 * Serial number
		 */
		private static final long serialVersionUID = -6044879459819424566L;

		@Override
		public int getSize() {
			return values.size();
		}

		@Override
		public Account getElementAt(int index) {
			return values.get(index);
		}

	}

	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = 170124090837740014L;

	/**
	 * Account list
	 */
	private JList<Account> accountList;

	/**
	 * File Browse button
	 */
	private JButton btnBrowse;

	/**
	 * Refers self
	 */
	private JFrame self;

	/**
	 * Holds choosen dir
	 */
	private JLabel lblDir;

	/**
	 * Holds settings
	 */
	private JPanel settingsPanel;

	/**
	 * Holds selected account
	 */
	private Account selectedAccount;

	/**
	 * Holds old dir
	 */
	private String oldDir;

	ConfigureAccountFrame() {
		super("gdrive-d: Manager your accounts");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		self = this;
		accountList = new JList<Account>();
		accountList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				enableSettingPanel();
				selectedAccount = accountList.getSelectedValue();
				if(null != selectedAccount) {
					updatedUIToSelectedAccount();
				}
			}
		});
		accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		accountList.setBorder(new LineBorder(new Color(0, 0, 0)));

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.dispose();
			}
		});

		JLabel lblConfiguredAccount = new JLabel("Account(s) Configured");

		settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
				0)), "Settings", TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																lblConfiguredAccount)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(200)
																		.addComponent(
																				btnClose))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				accountList,
																				GroupLayout.PREFERRED_SIZE,
																				201,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				settingsPanel,
																				GroupLayout.DEFAULT_SIZE,
																				267,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap(56, Short.MAX_VALUE)
										.addComponent(lblConfiguredAccount)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																accountList,
																GroupLayout.PREFERRED_SIZE,
																275,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																settingsPanel,
																GroupLayout.PREFERRED_SIZE,
																220,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(btnClose)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		settingsPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		JLabel lblMount = new JLabel("Mount in drive");
		settingsPanel.add(lblMount, "2, 2, default, bottom");

		lblDir = new JLabel("/");
		settingsPanel.add(lblDir, "2, 4, 3, 7, fill, fill");

		btnBrowse = new JButton("Change Mount Folder");
		settingsPanel.add(btnBrowse, "2, 12, right, bottom");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose Folder");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(self);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					lblDir.setText("<html><body width='50px;'>"
							+ selectedFile.getAbsolutePath() + "</body></html>");
				}
				fileChooser = null;
				self.repaint();
			}
		});

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Main.setDirMount(selectedAccount, lblDir.getText(), oldDir)) {
					oldDir = lblDir.getText();
				} else {
					lblDir.setText(oldDir);
				}
			}
		});
		settingsPanel.add(btnSave, "2, 14, left, bottom");

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(self, "Are you sure to remove "
						+ selectedAccount.getUserEmail() + "?", "Remove account", JOptionPane.YES_NO_OPTION);
				if(action == JOptionPane.YES_OPTION) {
					Main.deleteAccount(selectedAccount);
					refershAccountList();
					selectedAccount = null;
				}
			}
		});
		settingsPanel.add(btnDelete, "4, 14, left, bottom");
		setSize(514, 429);
		getContentPane().setLayout(groupLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		refershAccountList();
		setVisible(true);
	}

	/**
	 * Refresh account list
	 */
	private void refershAccountList() {
		accountList.removeAll();
		accountList.setModel(new AccountModel());
		lblDir.setText("/");
		disableSettingPanel();
	}

	/**
	 * Update UI to selected account
	 */
	private void updatedUIToSelectedAccount() {
		disableSettingPanel();
		oldDir = Main.getDirMount(selectedAccount);
		lblDir.setText(oldDir);
		enableSettingPanel();
	}

	/**
	 * Disable setting panel and its components
	 */
	private void disableSettingPanel() {
		disableComponent(settingsPanel);
	}

	/**
	 * Disables container
	 * 
	 * @param container
	 */
	private void disableComponent(Container container) {
		for (Component comp : container.getComponents()) {
			comp.setEnabled(false);
			if (comp instanceof Container) {
				disableComponent((Container) comp);
			}
		}
		container.setEnabled(false);
	}

	/**
	 * Enable setting panel and its components
	 */
	private void enableSettingPanel() {
		settingsPanel.setEnabled(true);
		for (Component comps : settingsPanel.getComponents()) {
			comps.setEnabled(true);
		}
	}
}
