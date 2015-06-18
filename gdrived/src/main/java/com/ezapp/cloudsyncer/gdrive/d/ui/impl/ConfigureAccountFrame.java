package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;

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
	 * Refers self
	 */
	private JFrame self;

	ConfigureAccountFrame(){
		super("gdrive-d: Manager your accounts");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		self = this;
		accountList = new JList<Account>();
		accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		accountList.setBorder(new LineBorder(new Color(0, 0, 0)));
		accountList.setModel(new AccountModel());
		accountList.setSelectedIndex(1);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.dispose();
			}
		});
		
		JLabel lblConfiguredAccount = new JLabel("Account(s) Configured");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(180, 180, Short.MAX_VALUE)
					.addComponent(btnClose)
					.addGap(180))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(accountList, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(220, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblConfiguredAccount)
					.addContainerGap(351, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(57)
					.addComponent(lblConfiguredAccount)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(accountList, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClose)
					.addContainerGap(21, Short.MAX_VALUE))
		);
		setSize(433, 403);
		getContentPane().setLayout(groupLayout);
	}

	@Override
	public void run() {
		setVisible(true);
	}
}
