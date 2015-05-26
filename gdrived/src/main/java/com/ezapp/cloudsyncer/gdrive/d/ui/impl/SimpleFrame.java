package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Frame to hold UI details
 * 
 * @author grangarajan
 *
 */
class SimpleFrame extends JFrame implements Runnable {

	/**
	 * Logger
	 */
	private static Logger LOGGER = LogManager.getLogger(SimpleFrame.class);
	
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -6851815624716027668L;

	/**
	 * Field to hold generated oauth URL
	 */
	private JTextArea oauthURLField;
	private JTextField authCode;

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
	SimpleFrame() throws HeadlessException {
		super("gdrive-d");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent evt) {
				LOGGER.info("App exit...");
				Main.shutDown();
			}
		});
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblGdrived = new JLabel("gdrive-d");
		lblGdrived.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblOpenTheGenerated = new JLabel(
				"<html><body style='width: 200px'>Open the generated URL in your browser. Then copy paste the code generated in the browser.</body></html>");
		lblOpenTheGenerated.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOpenTheGenerated.setVerticalAlignment(SwingConstants.TOP);

		oauthURLField = new JTextArea();
		oauthURLField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		oauthURLField.setEditable(false);
		oauthURLField.setWrapStyleWord(true);
		oauthURLField.setColumns(15);
		oauthURLField.setRows(8);
		oauthURLField.setText("Please wait while URL is generated.");
		oauthURLField.setLineWrap(true);
		
		authCode = new JTextField();
		authCode.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(155)
					.addComponent(lblGdrived)
					.addContainerGap(156, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(59, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(oauthURLField, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOpenTheGenerated, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE))
					.addGap(52))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(111)
					.addComponent(authCode, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(138, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(148)
					.addComponent(btnLogin)
					.addContainerGap(179, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGdrived)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblOpenTheGenerated)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(oauthURLField, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(authCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLogin)
					.addContainerGap(55, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * Set oAuth URL
	 * 
	 * @param url
	 */
	public void setOAuthURL(String url) {
		if (null != oauthURLField) {
			oauthURLField.setText(url);
		}
	}
}
