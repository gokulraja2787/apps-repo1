package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;

/**
 * Unit responsible for Sys tray
 * 
 * @author grangarajan
 *
 */
public class SysTray {

	/**
	 * LOGGER
	 */
	private static Logger LOGGER = LogManager.getLogger(SysTray.class);

	/**
	 * Sets up System Tray
	 */
	public static void initSysTray() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			URL fileUrl = Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"com/ezapp/cloudsyncer/gdrive/d/images/app40X40-ico.png");
			ImageIcon imageIcon = new ImageIcon(fileUrl);
			Image image = imageIcon.getImage();
			PopupMenu popup = new PopupMenu();
			MenuItem exitItem = new MenuItem(
					GdrivedContextListener.ACTION_COMMAND.EXIT.getValue());
			popup.add(exitItem);
			exitItem.addActionListener(new GdrivedContextListener());
			exitItem.setActionCommand(GdrivedContextListener.ACTION_COMMAND.EXIT
					.getValue());
			TrayIcon trayIcon = new TrayIcon(image,
					"gdrive-d: Cloud syncer for Google drive", popup);
			trayIcon.setImageAutoSize(true);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				LOGGER.error("Error while setting up tray: " + e.getMessage(),
						e);
			}
		} else {
			LOGGER.warn("System Tray is not supported");
		}
	}

}

/**
 * Application context listener
 * 
 * @author grangarajan
 *
 */
class GdrivedContextListener implements ActionListener {

	/**
	 * Enums for context commands
	 * 
	 * @author grangarajan
	 *
	 */
	static enum ACTION_COMMAND {
		/**
		 * Exit
		 */
		EXIT("Exit");

		/**
		 * Holds context value
		 */
		private String value;

		/**
		 * Constructor
		 * 
		 * @param value
		 */
		ACTION_COMMAND(String value) {
			this.value = value;
		}

		/**
		 * Get ENUM value
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		if (command.equals(GdrivedContextListener.ACTION_COMMAND.EXIT
				.getValue())) {
			Main.shutDown(1);
		}
	}

}