package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import org.eclipse.swt.widgets.Composite;

import com.ezapp.cloudsyncer.gdrive.d.vo.RemoteFile;

/**
 * "Naive" Composite to hold file representation
 * 
 * Date: November 02, 2015
 * 
 * @author grangarajan
 *
 */
public class NaiveComposite extends Composite {

	/**
	 * Represents Remote file
	 */
	private RemoteFile file;

	/**
	 * @param parent
	 * @param style
	 */
	public NaiveComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param file
	 */
	public NaiveComposite(Composite parent, int style, RemoteFile file) {
		super(parent, style);
		this.file = file;
	}

	/**
	 * @return the file
	 */
	public RemoteFile getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(RemoteFile file) {
		this.file = file;
	}

}
