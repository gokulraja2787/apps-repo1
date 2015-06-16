package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog to show messages
 * 
 * @author grangarajan
 *
 */
class AppDialog extends Dialog {

	/**
	 * Holds parnt
	 */
	private Shell parent;

	/**
	 * Holds self
	 */
	private Shell self;

	/**
	 * Holds message
	 */
	private Label lblMessage;

	/**
	 * Holds image
	 */
	private Label lblImg;

	/**
	 * @param parent
	 */
	public AppDialog(Shell parent) {
		super(parent);
		this.parent = parent;
		initUI();
	}

	/**
	 * @param parent
	 * @param style
	 */
	public AppDialog(Shell parent, int style) {
		super(parent, style);
		this.parent = parent;
		initUI();
	}

	/**
	 * Initialize the dialog UI
	 */
	private void initUI() {
		self = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		self.setSize(521, 166);
		self.setText("gdrive");

		lblMessage = new Label(self, SWT.WRAP);
		lblMessage.setImage(null);
		lblMessage.setFont(SWTResourceManager.getFont("Calibri", 12, SWT.BOLD));
		lblMessage.setBounds(71, 10, 417, 64);
		lblMessage.setText("message");

		Composite composite = new Composite(self, SWT.NONE);
		composite.setBounds(71, 80, 417, 51);

		Button btnOk = new Button(composite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			/**
			 * Close the widget
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null != self && null != self.getDisplay()) {
					if (!self.isDisposed()) {
						self.dispose();
					}
				}
			}
		});
		btnOk.setBounds(166, 10, 75, 25);
		btnOk.setText("Ok");

		lblImg = new Label(self, SWT.NONE);
		lblImg.setImage(null);
		lblImg.setBounds(10, 13, 48, 48);
	}

	/**
	 * Opens dialog
	 */
	public void open() {
		if (null != self && !self.isDisposed() && null != parent
				&& !parent.isDisposed()) {
			self.open();
			Display display = self.getDisplay();
			while (!self.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		self.setText(title);
	}

	/**
	 * Set message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		lblMessage.setText(message);
	}

	/**
	 * Set icon
	 * 
	 * @param url
	 */
	public void setIcon(String url) {
		lblImg.setImage(SWTResourceManager.getImage(AppDialog.class, url));
	}
}
