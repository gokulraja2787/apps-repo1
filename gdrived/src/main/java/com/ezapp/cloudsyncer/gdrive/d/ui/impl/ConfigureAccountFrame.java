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

/**
 * Frame to hold Configure window
 * 
 * Date: June 17, 2015
 * 
 * @author grangarajan
 *
 */
class ConfigureAccountFrame extends JFrame {

	private class AccountModel extends AbstractListModel<Account> {

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

	ConfigureAccountFrame(){
		super("gdrive-d: Manager your accounts");
		
		accountList = new JList<Account>();
		accountList.setBorder(new LineBorder(new Color(0, 0, 0)));
		accountList.setModel(new AccountModel());
		accountList.setSelectedIndex(1);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(accountList, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(223, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(accountList, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
}
