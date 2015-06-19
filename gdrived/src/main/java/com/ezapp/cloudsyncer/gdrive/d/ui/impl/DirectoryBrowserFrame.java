package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.google.DriveUtil;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;

/**
 * @author grangarajan
 *
 */
class DirectoryBrowserFrame extends JFrame implements Runnable {

	/**
	 * Self reference
	 */
	private JFrame self;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(DirectoryBrowserFrame.class);

	/**
	 * Scroll pane to display files
	 */
	private JPanel filePane;

	private JScrollPane scrollPane;

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = -973363210749277555L;

	DirectoryBrowserFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("gdrive-d: Browse Remote directory");
		setSize(800, 710);
		this.self = this;

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.dispose();
			}
		});

		JToolBar fileActionToolBar = new JToolBar();

		JSeparator separator = new JSeparator();

		filePane = new JPanel();
		filePane.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(filePane);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setViewportBorder(new EtchedBorder(EtchedBorder.RAISED,
				null, null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(337)
																		.addComponent(
																				btnClose))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				fileActionToolBar,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				scrollPane,
																				GroupLayout.PREFERRED_SIZE,
																				763,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				separator,
																				GroupLayout.DEFAULT_SIZE,
																				1,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(fileActionToolBar,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																separator,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																scrollPane,
																GroupLayout.DEFAULT_SIZE,
																580,
																Short.MAX_VALUE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(btnClose)
										.addContainerGap()));

		JButton btnX = new JButton("X");
		btnX.setSize(20, 20);
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				self.dispose();
			}
		});
		fileActionToolBar.add(btnX);
		List<Account> accountList = Main.getConfiguredAccounts();
		if (null != accountList) {
			for (Account account : accountList) {
				drawAccount(fileActionToolBar, account);
			}
		}
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * @param fileActionToolBar
	 * @param account
	 */
	private void drawAccount(final JToolBar fileActionToolBar,
			final Account account) {
		final JToggleButton btnAcc = new JToggleButton();
		try {
			String userPicUrl = account.getPictureUrl();
			URL userPicURL;
			if (null == userPicUrl || userPicUrl.trim().equals("")) {
				userPicURL = Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								"com/ezapp/cloudsyncer/gdrive/d/images/defaultuser.jpg");
			} else {
				userPicURL = new URL(userPicUrl);
			}
			btnAcc.setSize(20, 20);
			ImageIcon icon = new ImageIcon(userPicURL);
			Image img = icon.getImage();
			img = img.getScaledInstance(btnAcc.getWidth(), btnAcc.getHeight(),
					Image.SCALE_FAST);
			icon = new ImageIcon(img);
			btnAcc.setIcon(icon);
			btnAcc.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (btnAcc.isSelected()) {
						drawFiles(account);
					} else {
						LOGGER.info("Clearing pane");
						filePane.removeAll();
					}
					scrollPane.setViewportView(filePane);
					filePane.repaint();
					filePane.revalidate();
					self.repaint();
					self.revalidate();
				}
			});
		} catch (MalformedURLException e1) {
			LOGGER.error("Error while forming icon: " + e1.getMessage(), e1);
		}
		fileActionToolBar.add(btnAcc);
	}

	/**
	 * Show files in the Pane
	 */
	private void drawFiles(Account account) {
		LOGGER.info("Starting to draw files");
		GridLayout layout = new GridLayout();
		if (null != account) {
			DriveUtil dutil = Main.getDriveUtil();
			Drive drive = dutil.getDrive(account.getUserId());
			try {
				About about = drive.about().get().execute();
				ChildList list = drive.children().list(about.getRootFolderId())
						.execute();
				List<ChildReference> childListRef = list.getItems();
				layout.setColumns(1);
				layout.setRows(0);
				filePane.setLayout(layout);
				for (ChildReference file : childListRef) {
					String fileId = file.getId();
					Files dfiles = drive.files();
					Files.Get fgd = dfiles.get(fileId);
					File actualFile = fgd.execute();
					String fname = actualFile.getOriginalFilename();
					// String type = actualFile.getMimeType();
					if (fname == null || fname.equals("")) {
						fname = actualFile.getTitle();
					}
					LOGGER.debug(fname);
					filePane.add(new JLabel(fname));
				}
			} catch (IOException e) {
				LOGGER.error("Error while processing file metadata: ", e);
				Main.showErrorMessage("Error while processing file meta");
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		setVisible(true);
	}
}
