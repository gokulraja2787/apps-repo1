package com.ezapp.cloudsyncer.gdrive.d.ui.event.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.db.impl.AppDBFactory;
import com.ezapp.cloudsyncer.gdrive.d.exceptions.AppDBException;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUIFactory;

/**
 * Action listener for Theme changer
 * 
 * Date: June 15, 2015
 * 
 * @author gokul
 *
 */
public class ThemeContextListener extends SelectionAdapter implements
		ActionListener {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(ThemeContextListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		try {
			String[] vals = AppDBFactory.getInstance().getAppDBInstance()
					.getValues(command);
			if (null != vals && vals.length > 0) {
				AppDBFactory
						.getInstance()
						.getAppDBInstance()
						.updateAppConfig(RunnerUIFactory.INS_KEY_ACC, 1,
								vals[vals.length - 1]);
				Main.reInitUI();
			}
		} catch (AppDBException e) {
			LOGGER.error("Exception while applying theme: " + e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent evt) {
		String command = ((MenuItem) evt.getSource()).getText();
		try {
			String[] vals = AppDBFactory.getInstance().getAppDBInstance()
					.getValues(command);
			if (null != vals && vals.length > 0) {
				AppDBFactory
						.getInstance()
						.getAppDBInstance()
						.updateAppConfig(RunnerUIFactory.INS_KEY_ACC, 1,
								vals[vals.length - 1]);
				Main.reInitUI();
			}
		} catch (AppDBException e) {
			LOGGER.error("Exception while applying theme: " + e.getMessage(), e);
		}
	}

}
