/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package com.lksoft.util.logging;

import java.io.*;
import java.util.logging.*;

/**
 * @author Viktoras Kovaliovas
 */
public class LoggerFactory
{
	private LoggerFactory() {}
	
	/**
	 * Sets level, filter and formatter for specified handler
	 * to values, that can be used to publish info messages.
	 * @param h specified handler
	 */
	public static void setupInfoHandler(Handler h) {
		h.setLevel(Level.ALL);
		h.setFilter(InfoFilter.getInstance());
		h.setFormatter(new SimpleFormatter2());
	}
	
	public static Logger createLogger(String fileName, Level level) {
		// we use anonymous loggers here, because they
		// are not shared. we get problems with shared
		// loggers in eclipse, because since converter
		// can be invoked numerous times in same JVM
		// session, we shall decorate it with logging
		// handlers each time and so will get dublicated
		// lines in the output
		Logger logger = Logger.getAnonymousLogger();
		logger.setUseParentHandlers(false);

		Handler h = new SystemOutHandler();
		setupInfoHandler(h);
		logger.addHandler(h);
		
		if (fileName != null) {
			try {
				h = new FileHandler(fileName);
				h.setLevel(level);
				h.setFormatter(new SimpleFormatter2());
				logger.addHandler(h);
			}
			catch (IOException e) {
				logger.throwing("Factory", "createLogger", e);
			}
		}

		logger.setLevel(Level.ALL);
		
		return logger;
	}

	public static Logger createLogger(String fileName) {
		return createLogger(fileName, Level.ALL);
	}
	
	/**
	 * @deprecated
	 */
	public static Logger createLogger(String name, String fileName, Level level) {
		return createLogger(fileName, level);
	}
	
	/**
	 * @deprecated
	 */
	public static Logger createLogger(String name, String fileName) {
		return createLogger(name, fileName, Level.ALL);
	}
	
	/**
	 * @deprecated
	 */
	public static void close(Logger logger) {
		Handler h[] = logger.getHandlers();
		for (int i = 0; i < h.length; i++)
			if (h[i] instanceof FileHandler)
				h[i].close();
	}

	public static void close(Logger logger, String fileName) {
		Handler h[] = logger.getHandlers();
		for (int i = 0; i < h.length; i++)
			if (h[i] instanceof FileHandler)
				h[i].close();
			
		if (fileName != null) {
			fileName += ".lck";
			File file = new File(fileName);
			if (file.exists())
				file.delete();
		}
	}
}