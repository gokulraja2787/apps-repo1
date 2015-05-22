package com.ezapp.cloudsyncer.gdrive.d.log;

import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Wrapper for log manager
 * 
 * @author grangarajan
 *
 */
public class LogManager {

	/**
	 * Initialize log4j
	 */
	static {
		String config = "com/ezapp/cloudsyncer/gdrive/d/log/log4j2.xml";
		URL fileUrl = Thread.currentThread().getContextClassLoader()
				.getResource(config);
		System.out.println(fileUrl);
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(config);
		ConfigurationSource source = new ConfigurationSource(stream, fileUrl);
		Configurator.initialize(null, source);
	}

	/**
	 * 
	 * @param clazz
	 * @return logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		return org.apache.logging.log4j.LogManager.getLogger(clazz);
	}

	/**
	 * 
	 * @param className
	 * @return logger
	 */
	public static Logger getLogger(String className) {
		return org.apache.logging.log4j.LogManager.getLogger(className);
	}

}
