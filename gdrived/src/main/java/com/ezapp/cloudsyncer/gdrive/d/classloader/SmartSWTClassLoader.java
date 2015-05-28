package com.ezapp.cloudsyncer.gdrive.d.classloader;

import java.io.IOException;
import java.util.jar.JarFile;

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
		System.out.println("R Loading " + name.startsWith("org.eclipse.swt"));
		if (name.startsWith("org.eclipse.swt")) {
		}
		return super.loadClass(name, resolve);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		System.out.println("Loading " + name.startsWith("org.eclipse.swt"));
		if (name.startsWith("org.eclipse.swt")) {
			System.out.println("Loading " + name);
			byte classData[];
			Class<?> result = null;
			result = findLoadedClass(name);
			if(null != result) {
				return result;
			}
			String platformFile = "org.eclipse.swt.";
			String os = getSystemProperty("os.name");
			String arch = getSystemProperty("os.arch");
			String desktop = null;
			
			if(os.contains("win")) {
				os = "win32";
				desktop = "win32";
			} else if(os.contains("mac")) {
				os = "macosx";
				desktop = "cocoa";
			} else if(os.contains("linux")) {
				os = "linux";
				desktop = "gtk";
			} else if(os.contains("solaris")) {
				os = "solaris";
				desktop = "gtk";
			}
			System.out.println(os+arch+desktop);
			platformFile += ".jar";
			try {
				JarFile jar = new JarFile(platformFile);
			} catch (IOException e) {
				System.err.println("Class loading Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return super.loadClass(name);
	}

	/**
	 * Gets System property
	 * @param key
	 * @return
	 */
	private String getSystemProperty(String key){
		return System.getProperty(key);
	}
	
}
