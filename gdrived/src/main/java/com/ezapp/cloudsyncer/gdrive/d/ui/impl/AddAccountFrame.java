package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Add account UI
 * 
 * @author grangarajan
 *
 */
class AddAccountFrame extends JFrame implements Runnable {

	/**
	 * Logger
	 */
	private static Logger LOGGER = LogManager.getLogger(AddAccountFrame.class);

	/**
	 * Simple browser
	 */
	private SimpleBrowser authBrowser;

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -6851815624716027668L;

	/**
	 * Field to hold generated oauth URL
	 */
	private String oauthURLField = new String(
			"Please wait while URL is generated.");
	private JTextField oauthField;

	/**
	 * Self instance
	 */
	private AddAccountFrame self;
	private JTextField userIdField;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setVisible(true);
	}

	/**
	 * @throws HeadlessException
	 */
	AddAccountFrame() throws HeadlessException {
		super("gdrive-d");
		setTitle("Add Account");
		self = this;

		setSize(400, 367);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblGdrived = new JLabel("Add Google Account");
		lblGdrived.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblOpenTheGenerated = new JLabel(
				"<html><body style='width: 280px'>In order to start using gdrive cloud syncer you need to login to google account first. Click on the below button to authenticate using your google credential, then copy the code and paste it below.</body></html>");
		lblOpenTheGenerated.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpenTheGenerated.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton btnLogin = new JButton("Authentical using a Google credential");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("Opening browser");
				authBrowser = new SimpleBrowser(null);
				authBrowser.openUrl(oauthURLField);
				authBrowser.openBrowser();
			}
		});

		JLabel lblPaste = new JLabel("Paste the code here:");
		lblPaste.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnAddAccount = new JButton("Add Account");
		btnAddAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("Got key!!");
				String clientKey = oauthField.getText();
				String userId = userIdField.getText();
				if (Main.buildCredentialAndPersist(clientKey, userId)) {
					clientKey = null;
					self.dispose();
				}
				oauthField.setText("");
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.dispose();
			}
		});

		oauthField = new JTextField();
		oauthField.setColumns(10);

		JLabel lblEnterTheGoogle = new JLabel("Enter the google user id:");
		lblEnterTheGoogle.setFont(new Font("Tahoma", Font.PLAIN, 12));

		userIdField = new JTextField();
		userIdField.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblOpenTheGenerated,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGap(65)
																										.addComponent(
																												btnLogin))
																						.addGroup(
																								Alignment.TRAILING,
																								groupLayout
																										.createSequentialGroup()
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												97,
																												GroupLayout.PREFERRED_SIZE)
																										.addComponent(
																												lblGdrived)
																										.addGap(89))
																						.addGroup(
																								Alignment.TRAILING,
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												lblEnterTheGoogle,
																												GroupLayout.PREFERRED_SIZE,
																												166,
																												GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												38,
																												Short.MAX_VALUE)
																										.addComponent(
																												userIdField,
																												GroupLayout.PREFERRED_SIZE,
																												160,
																												GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												lblPaste)
																										.addGroup(
																												groupLayout
																														.createSequentialGroup()
																														.addGap(7)
																														.addComponent(
																																oauthField,
																																GroupLayout.DEFAULT_SIZE,
																																211,
																																Short.MAX_VALUE)))))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(78)
																		.addComponent(
																				btnAddAccount)
																		.addGap(18)
																		.addComponent(
																				btnCancel)))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(46)
										.addComponent(lblGdrived)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblOpenTheGenerated)
										.addGap(18)
										.addComponent(btnLogin)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				userIdField,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(13)
																		.addComponent(
																				lblEnterTheGoogle,
																				GroupLayout.PREFERRED_SIZE,
																				15,
																				GroupLayout.PREFERRED_SIZE)))
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(lblPaste)
														.addComponent(
																oauthField,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																btnAddAccount)
														.addComponent(btnCancel))
										.addContainerGap(326, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * Set oAuth URL
	 * 
	 * @param url
	 */
	public void setOAuthURL(String url) {
		if (null != oauthURLField) {
			oauthURLField = url;
			if (null != authBrowser && !authBrowser.isClosed()) {
				authBrowser.openUrl(oauthURLField);
			}
		}
	}
}
