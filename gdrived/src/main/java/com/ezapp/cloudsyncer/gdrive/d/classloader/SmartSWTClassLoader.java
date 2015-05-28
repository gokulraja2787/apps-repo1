package com.ezapp.cloudsyncer.gdrive.d.classloader;

/**
 * Class Loader to detect the underlying OS environment and load platform
 * dependent SWT classes
 * 
 * @author grangarajan
 *
 */
public class SmartSWTClassLoader extends ClassLoader {

	/**
	 * 
	 */
	public SmartSWTClassLoader() {
		super();
	}

	/**
	 * @param parent
	 */
	public SmartSWTClassLoader(ClassLoader parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}

}
