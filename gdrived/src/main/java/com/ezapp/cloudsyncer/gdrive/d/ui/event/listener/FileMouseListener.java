package com.ezapp.cloudsyncer.gdrive.d.ui.event.listener;

import java.awt.event.MouseListener;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Label;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppFileException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.IFileUIHandler;
import com.ezapp.cloudsyncer.gdrive.d.ui.impl.FilePanel;
import com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl.NaiveComposite;
import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

/**
 * Date: October 29, 2015
 * 
 * @author gokul
 *
 */
public class FileMouseListener implements MouseListener,
		org.eclipse.swt.events.MouseListener {

	/**
	 * Logger
	 */
	private Logger LOGGER = LogManager.getLogger(FileMouseListener.class);

	/**
	 * Holds file UI Handler
	 */
	private IFileUIHandler fileUIHandler;

	/**
	 * Has custom event runner
	 */
	private Runnable eventRunner;

	/**
	 * Initialize listener with file UI Handler
	 * 
	 * @param fileUIHandler
	 */
	public FileMouseListener(IFileUIHandler fileUIHandler) {
		this.fileUIHandler = fileUIHandler;
	}

	/**
	 * Initialize listener with file UI Handler and event runner
	 * 
	 * @param fileUIHandler
	 * @param eventRunner
	 */
	public FileMouseListener(IFileUIHandler fileUIHandler, Runnable eventRunner) {
		this.fileUIHandler = fileUIHandler;
		this.eventRunner = eventRunner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt
	 * .events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		try {
			fileUIHandler.doMisc();
			NaiveComposite parent = null;
			String className = e.getSource().getClass().getCanonicalName();
			if (className.indexOf(Label.class.getCanonicalName()) > -1) {
				Label source = (Label) e.getSource();
				parent = (NaiveComposite) source.getParent();
			} else if (className.indexOf(NaiveComposite.class
					.getCanonicalName()) > -1) {
				parent = (NaiveComposite) e.getSource();
			}
			if (null != parent) {
				RemoteFile file = parent.getFile();
				String type = file.getFileMimeType();
				if (type.equals("application/vnd.google-apps.folder")) {
					fileUIHandler.drawFiles(file.getFileId());
				} else {
					// TODO Construct else if ladder for various format
					// or bring ??? design pattern
				}
			} else {
				Main.showInfoMessage("Double clicked: "
						+ e.getSource().getClass().getCanonicalName());
			}
		} catch (AppFileException | IOException e1) {
			if (null != eventRunner) {
				eventRunner.run();
			}
		} catch (Exception e1) {
			LOGGER.error("Error while processing file metadata: ", e);
			Main.showErrorMessage("Error while processing file meta");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
	 * .MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		// Do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		// Do nothing

	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 *      If eventRunner is null then it calls fileUIHandler.drawFiles()
	 */
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		FilePanel orginPane = null;
		final RemoteFile fileReferedInThePane;
		if (e.getComponent() instanceof FilePanel) {
			orginPane = (FilePanel) e.getComponent();
			fileReferedInThePane = orginPane.getRemoteFile();
		} else {
			fileReferedInThePane = null;
		}
		if (null != orginPane && null != fileReferedInThePane) {
			String type = fileReferedInThePane.getFileMimeType();
			if (e.getClickCount() == 2
					&& type.equals("application/vnd.google-apps.folder")) {
				Thread runner;
				if (eventRunner != null) {
					runner = new Thread(eventRunner);
				} else {
					runner = new Thread(new Runnable() {
						public void run() {
							try {
								fileUIHandler.doMisc();
								fileUIHandler.drawFiles(fileReferedInThePane
										.getFileId());
							} catch (IOException | AppFileException e1) {
								LOGGER.error("Folder traversal failed! ", e1);
								Main.showErrorMessage("Folder traversal failed!");
							}
						}
					});
				}
				runner.start();
			} else {
				// TODO Construct else if ladder for various format
				// or bring ??? design pattern
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		// Do nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// DO nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param eventRunner
	 *            the eventRunner to set
	 */
	public void setEventRunner(Runnable eventRunner) {
		this.eventRunner = eventRunner;
	}

}
