package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.cloud.IRemoteFileUtil;
import com.ezapp.cloudsyncer.gdrive.d.cloud.RemoteFileUtilImplFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.vo.Account;
import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

/**
 * Directory browser frame
 * 
 * Date: June 20, 2015
 * 
 * @author gokul
 *
 */
class DirectoryBrowserFrame {

	/**
	 * Holds shell
	 */
	private Shell shell;
	/**
	 * Holds display
	 */
	private Display display;
	/**
	 * Holds scrolled composite
	 */
	private Composite fileListComposite;
	private ScrolledComposite scrolledComposite;
	/**
	 * Holds Toolbar
	 */
	private ToolBar fileActionToolBar;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(DirectoryBrowserFrame.class);

	/**
	 * 
	 */
	DirectoryBrowserFrame(Display display) {
		this.display = display;
		initUI();
	}

	/**
	 * Initializes UI
	 */
	private void initUI() {
		shell = new Shell(display);
		shell.setText("gdrive-d: Browse remote directory");
		shell.setSize(800, 744);
		shell.setLayout(new FormLayout());

		fileActionToolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		FormData fd_fileActionToolBar = new FormData();
		fd_fileActionToolBar.right = new FormAttachment(0, 790);
		fd_fileActionToolBar.top = new FormAttachment(0, 10);
		fd_fileActionToolBar.left = new FormAttachment(0, 10);
		fileActionToolBar.setLayoutData(fd_fileActionToolBar);

		ToolItem tltmClose = new ToolItem(fileActionToolBar, SWT.NONE);
		tltmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		tltmClose.setToolTipText("Close");
		tltmClose.setText("X");

		initScrollPane();

		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		FormData fd_btnClose = new FormData();
		fd_btnClose.bottom = new FormAttachment(100, -10);
		fd_btnClose.right = new FormAttachment(fileActionToolBar, 0, SWT.RIGHT);

		ToolItem toolItem = new ToolItem(fileActionToolBar, SWT.SEPARATOR);
		toolItem.setToolTipText(null);
		btnClose.setLayoutData(fd_btnClose);
		btnClose.setText("Close");

		List<Account> accountList = Main.getConfiguredAccounts();
		if (null != accountList) {
			for (Account account : accountList) {
				drawAccount(fileActionToolBar, account);
			}
		}
	}

	/**
	 * Initialize scroll pane
	 */
	private void initScrollPane() {
		scrolledComposite = new ScrolledComposite(shell, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		fileListComposite = new Composite(scrolledComposite, SWT.NONE);
		fileListComposite.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.right = new FormAttachment(fileActionToolBar, 0,
				SWT.RIGHT);
		fd_scrolledComposite.bottom = new FormAttachment(fileActionToolBar,
				609, SWT.BOTTOM);
		fd_scrolledComposite.top = new FormAttachment(fileActionToolBar, 6);
		fd_scrolledComposite.left = new FormAttachment(fileActionToolBar, 0,
				SWT.LEFT);
		scrolledComposite.setLayoutData(fd_scrolledComposite);
		// fileListComposite.setSize(scrolledComposite.getSize());
		fileListComposite.setLayout(new FillLayout());
		scrolledComposite.setContent(fileListComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
	}

	private void reInitScrollPane() {
		/*
		 * fileListComposite.setBackground(SWTResourceManager
		 * .getColor(SWT.COLOR_WHITE));
		 */
		FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.right = new FormAttachment(fileActionToolBar, 0,
				SWT.RIGHT);
		fd_scrolledComposite.bottom = new FormAttachment(fileActionToolBar,
				609, SWT.BOTTOM);
		fd_scrolledComposite.top = new FormAttachment(fileActionToolBar, 6);
		fd_scrolledComposite.left = new FormAttachment(fileActionToolBar, 0,
				SWT.LEFT);
		scrolledComposite.setLayoutData(fd_scrolledComposite);
		scrolledComposite.setContent(fileListComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	/**
	 * 
	 * @param toolBar
	 * @param account
	 */
	private void drawAccount(ToolBar toolBar, final Account account) {
		final ToolItem itm = new ToolItem(toolBar, SWT.CHECK);
		String picUrl = account.getPictureUrl();
		URL userPicURL;
		itm.setToolTipText(account.getUserId());
		try {
			if (null == picUrl || picUrl.trim().equals("")) {
				userPicURL = Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								"com/ezapp/cloudsyncer/gdrive/d/images/defaultuser.jpg");
			} else {
				userPicURL = new URL(picUrl);
			}
			Image image = SWTResourceManager.getImage(userPicURL.openStream());
			image = scaleImage(image, 20, 20);
			itm.setImage(image);
			itm.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (null != shell && !shell.isDisposed()) {
						display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (itm.getSelection()) {
									drawFiles(account);
								} else {
									fileListComposite.dispose();
									scrolledComposite.dispose();
									scrolledComposite = new ScrolledComposite(shell, SWT.NONE);
									fileListComposite = new Composite(scrolledComposite, SWT.NONE);
								}
							}
						});
					} else {
						Main.showErrorMessage("This shouldn't come");
					}

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// Nothing todo

				}
			});
		} catch (IOException e) {
			LOGGER.error("Unable render account item in toolbar", e);
			Main.showErrorMessage("Unable render account item in toolbar");
		}
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
			} catch (AppFileException | IOException e) {
				LOGGER.error("Error while processing file metadata: ", e);
				Main.showErrorMessage("Error while processing file meta");
			} finally {
				remoteFileUtil = null;
			}

		}
	}

	private void drawFiles(final Account account, String folderId)
			throws AppFileException, IOException {
		LOGGER.info("Starting to draw files for " + folderId);
		if (null != folderId) {
			IRemoteFileUtil remoteFileUtil = RemoteFileUtilImplFactory
					.getInstance().getRemoteFileUtilInstance(account);
			List<RemoteFile> fileList;
			if (null == scrolledComposite) {
				scrolledComposite = new ScrolledComposite(shell, SWT.BORDER
						| SWT.H_SCROLL | SWT.V_SCROLL);
			} else {
				scrolledComposite.dispose();
				scrolledComposite = new ScrolledComposite(shell, SWT.BORDER
						| SWT.H_SCROLL | SWT.V_SCROLL);
			}
			if (null == fileListComposite) {
				fileListComposite = new Composite(scrolledComposite, SWT.NONE);
			} else {
				fileListComposite.dispose();
				fileListComposite = new Composite(scrolledComposite, SWT.NONE);
			}
			fileListComposite.setLayout(new GridLayout(2, false));
			fileList = remoteFileUtil.getAllFiles(folderId);
			getParentUIFolder(account, folderId);
			for (final RemoteFile file : fileList) {
				Composite innerComposite = new Composite(fileListComposite,
						SWT.BORDER);
				GridLayout innerLayout = new GridLayout(2, false);
				innerComposite.setLayout(innerLayout);
				innerComposite.setSize(60, 60);
				String fname = file.getFileName();
				String type = file.getFileMimeType();
				if (fname == null || fname.equals("")) {
					fname = file.getFileTitle();
				}
				String iconUrl = file.getIconUrl();
				if (null != iconUrl) {
					LOGGER.debug(iconUrl);
					URL icURL = new URL(iconUrl);
					Image image = SWTResourceManager.getImage(icURL
							.openStream());
					// image = scaleImage(image, width, height);
					new Label(innerComposite, SWT.NONE).setImage(image);
				}
				Label nam = new Label(innerComposite, SWT.NONE);
				nam.setText(fname);
				innerComposite.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				innerComposite.setToolTipText(fname);
				if (type.equals("application/vnd.google-apps.folder")) {
					innerComposite.setToolTipText("Double click to go inside "
							+ fname);
					nam.setToolTipText(innerComposite.getToolTipText());
					MouseListener lsitener = new MouseListener() {

						@Override
						public void mouseUp(MouseEvent e) {
							// Nothing todo

						}

						@Override
						public void mouseDown(MouseEvent e) {
							// Nothing todo

						}

						@Override
						public void mouseDoubleClick(MouseEvent e) {
							try {
								drawFiles(account, file.getFileId());
							} catch (AppFileException | IOException e1) {
								LOGGER.error(
										"Error while processing file metadata: ",
										e);
								Main.showErrorMessage("Error while processing file meta");
							}

						}
					};
					innerComposite.addMouseListener(lsitener);
					nam.addMouseListener(lsitener);
					nam.setToolTipText(innerComposite.getToolTipText());
				}
			}
			reInitScrollPane();
			shell.layout(true);
		}
	}

	/**
	 * Get parent UI
	 * 
	 * @param account
	 * @param folderId
	 * @return
	 */
	private Composite getParentUIFolder(final Account account, String folderId) {
		List<RemoteFile> parentList;
		IRemoteFileUtil remoteFileUtil = RemoteFileUtilImplFactory
				.getInstance().getRemoteFileUtilInstance(account);
		try {
			Composite innerComposite = new Composite(fileListComposite,
					SWT.BORDER);
			parentList = remoteFileUtil.getParent(folderId);
			GridLayout innerLayout = new GridLayout(2, false);
			innerComposite.setLayout(innerLayout);
			innerComposite.setSize(60, 60);
			if (null != parentList && parentList.size() > 0) {
				final RemoteFile parent = parentList.get(0);
				if (null != parent.getIconUrl()) {
					URL icURL = new URL(parent.getIconUrl());
					Image image = SWTResourceManager.getImage(icURL
							.openStream());
					// image = scaleImage(image, width, height);
					new Label(innerComposite, SWT.NONE).setImage(image);
				}
				Label nam = new Label(innerComposite, SWT.NONE);
				nam.setText("..");
				innerComposite.setBackground(SWTResourceManager
						.getColor(SWT.COLOR_WHITE));
				innerComposite.setToolTipText("Go to parent");
				MouseListener listener = new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						// Nothing todo

					}

					@Override
					public void mouseDown(MouseEvent e) {
						// Nothing todo

					}

					@Override
					public void mouseDoubleClick(MouseEvent e) {
						try {
							drawFiles(account, parent.getFileId());
						} catch (AppFileException | IOException e1) {
							LOGGER.error(
									"Error while processing file metadata: ", e);
							Main.showErrorMessage("Error while processing file meta");
						}

					}
				};
				innerComposite.addMouseListener(listener);
				nam.addMouseListener(listener);
				nam.setToolTipText(innerComposite.getToolTipText());
			}
		} catch (AppFileException | IOException e) {
			LOGGER.error("Folder traversal failed! ", e);
			Main.showErrorMessage("Folder traversal failed!");
		}
		return null;
	}

	/**
	 * +
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	private Image scaleImage(Image image, int width, int height) {

		ImageData data = image.getImageData();

		float img_height = data.height;
		float img_width = data.width;
		float container_height = height;
		float container_width = width;

		float dest_height_f = container_height;
		float factor = img_height / dest_height_f;

		int dest_width = (int) Math.floor(img_width / factor);
		int dest_height = (int) dest_height_f;

		if (dest_width > container_width) {
			dest_width = (int) container_width;
			factor = img_width / dest_width;
			dest_height = (int) Math.floor(img_height / factor);

		}

		// Image resize
		data = data.scaledTo(dest_width, dest_height);
		Image scaled = new Image(Display.getDefault(), data);
		image.dispose();
		return scaled;
	}

	/**
	 * Opens the shell
	 */
	public void openAndStart() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (null == shell || shell.isDisposed()) {
					initUI();
				}
				shell.open();
				while (null != shell && !shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
			}

		});
	}

	/**
	 * Closes display & shell
	 */
	public void close() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (null != shell && !shell.isDisposed()) {
					shell.close();
				}
				shell = null;
			}
		});
	}

	/**
	 * Set app image icon
	 * 
	 * @param url
	 */
	public void setAppImageIcon(URL url) {
		String urlloc = url.toString();
		try {
			urlloc = urlloc.substring(urlloc.indexOf("com/ezapp") - 1);
		} catch (Exception e) {
			urlloc = "/com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png";
		}

		final String urlLc = urlloc;
		if (null != display && !display.isDisposed()) {
			display.asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					shell.setImage(SWTResourceManager.getImage(MainFrame.class,
							urlLc));

				}
			});
		}
	}
}
