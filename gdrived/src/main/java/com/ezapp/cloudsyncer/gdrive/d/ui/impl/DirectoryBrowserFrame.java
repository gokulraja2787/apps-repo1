package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil;
import com.ezapp.cloudsyncer.gdrive.d.cloud.RemoteFileUtilImplFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

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
		btnX.setToolTipText("Close");
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
			btnAcc.setToolTipText(account.getUserId());
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
		LOGGER.info("Starting to root draw files");
		if (null != account) {
			IRemoteFileUtil remoteFileUtil = RemoteFileUtilImplFactory
					.getInstance().getRemoteFileUtilInstance(account);
			String rootFolderId;
			try {
				rootFolderId = (String) remoteFileUtil.getRootFolder();
				drawFiles(account, rootFolderId);
			} catch (AppFileException | MalformedURLException e) {
				LOGGER.error("Error while processing file metadata: ", e);
				Main.showErrorMessage("Error while processing file meta");
			} finally {
				remoteFileUtil = null;
			}

		}
	}

	private void drawFiles(final Account account, String folderId)
			throws AppFileException, MalformedURLException {
		LOGGER.info("Starting to draw files for " + folderId);
		GridLayout layout = new GridLayout();
		if (null != folderId) {
			IRemoteFileUtil remoteFileUtil = RemoteFileUtilImplFactory
					.getInstance().getRemoteFileUtilInstance(account);
			List<RemoteFile> fileList;
			try {
				layout.setColumns(2);
				layout.setRows(0);
				filePane.setLayout(layout);
				filePane.add(new JLabel("Loading please wait..."));
				filePane.repaint();
				filePane.revalidate();
				self.repaint();
				self.revalidate();
				fileList = remoteFileUtil.getAllFiles(folderId);
				filePane.removeAll();
				JPanel innerPanel;
				GridLayout innerLayout;
				filePane.add(getUIParentItem(account, folderId));
				for (final RemoteFile file : fileList) {
					String fname = file.getFileName();
					String type = file.getFileMimeType();
					if (fname == null || fname.equals("")) {
						fname = file.getFileTitle();
					}
					// LOGGER.debug(fname + " - " + type);
					JLabel nam = new JLabel();
					nam.setText(fname);
					String iconUrl = file.getIconUrl();
					JLabel imgLab = new JLabel();
					if (null != iconUrl) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Drawing: " + iconUrl);
						}
						URL iconPic = new URL(iconUrl);
						ImageIcon icon = new ImageIcon(iconPic);
						imgLab.setIcon(icon);
					}
					innerPanel = new JPanel();
					innerLayout = new GridLayout(2, 1);
					innerPanel.setToolTipText(fname);
					innerPanel.setLayout(innerLayout);
					innerPanel.add(imgLab);
					innerPanel.add(nam);
					innerPanel.setSize(30, 60);
					innerPanel.setBackground(Color.WHITE);
					innerPanel.setBorder(new LineBorder(Color.BLACK));
					if (type.equals("application/vnd.google-apps.folder")) {
						innerPanel.setToolTipText("Double click to open "
								+ fname);
						innerPanel.addMouseListener(new MouseListener() {

							@Override
							public void mouseReleased(MouseEvent e) {
								// Not implemented

							}

							@Override
							public void mousePressed(MouseEvent e) {
								// Not implemented

							}

							@Override
							public void mouseExited(MouseEvent e) {
								// Not implemented

							}

							@Override
							public void mouseEntered(MouseEvent e) {
								// Not implemented

							}

							@Override
							public void mouseClicked(MouseEvent e) {
								if (e.getClickCount() == 2) {
									try {
										drawFiles(account, file.getFileId());
									} catch (MalformedURLException
											| AppFileException e1) {
										LOGGER.error(
												"Folder traversal failed! ", e1);
										Main.showErrorMessage("Folder traversal failed!");
									}
								}

							}
						});
					}
					filePane.add(innerPanel);
				}
			} finally {
				fileList = null;
			}

		}
	}

	/**
	 * Draws up folder
	 * 
	 * @param account
	 * @param folderId
	 * @return
	 */
	private JPanel getUIParentItem(final Account account, String folderId) {
		JPanel innerPanel;
		GridLayout innerLayout;
		innerPanel = new JPanel();
		innerLayout = new GridLayout(2, 1);
		innerPanel.setToolTipText("Go up");
		innerPanel.setLayout(innerLayout);
		List<RemoteFile> parentList;
		IRemoteFileUtil remoteFileUtil = RemoteFileUtilImplFactory
				.getInstance().getRemoteFileUtilInstance(account);
		try {
			parentList = remoteFileUtil.getParent(folderId);
			if (null != parentList && parentList.size() > 0) {
				final RemoteFile parent = parentList.get(0);
				if (null != parent.getIconUrl()) {
					JLabel imgLab = new JLabel();
					try {
						imgLab.setIcon(new ImageIcon(new URL(parent
								.getIconUrl())));
					} catch (MalformedURLException e) {
						LOGGER.error("Folder traversal failed! ", e);
					}
					innerPanel.add(imgLab);
				}
				innerPanel.add(new JLabel(".."));
				innerPanel.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// Not implemented

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// Not implemented

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// Not implemented

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// Not implemented

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							drawFiles(account, parent.getFileId());
						} catch (MalformedURLException | AppFileException e1) {
							LOGGER.error("Folder traversal failed! ", e);
							Main.showErrorMessage("Folder traversal failed!");
						}

					}
				});
			}
		} catch (AppFileException e) {
			LOGGER.error("Folder traversal failed! ", e);
			Main.showErrorMessage("Folder traversal failed!");
		}
		return innerPanel;
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
