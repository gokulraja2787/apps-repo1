package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog to show messages
 * 
 * @author grangarajan
 *
 */
class AppDialog {

	/**
	 * Holds parent
	 */
	private Shell parent;
	
	/**
	 * @param parent
	 */
	public AppDialog(Shell parent) {
		this.parent = parent;
	}

	/**
	 * Opens dialog
	 */
	public void open(String message, int icon) {
		if (null != parent && !parent.isDisposed()) {
			MessageBox box = new MessageBox(parent, icon | SWT.OK);
			box.setText("Error");
			box.setMessage(message);
			box.open();
		}
	}

}
