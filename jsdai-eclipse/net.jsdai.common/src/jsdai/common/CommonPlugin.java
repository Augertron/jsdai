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
package jsdai.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jsdai.runtime.RuntimePlugin;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonPlugin extends AbstractUIPlugin {
	public static final boolean CONSOLE_OUT_REGISTRATION = false;
	
	public static final String PLUGIN_ID = "net.jsdai.common";
	
	public static final String JSDAI_PUBLIC_PATHS = "JSDAI";

	//The shared instance.
	private static CommonPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public CommonPlugin() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("jsdai.common.CommonPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		Bundle bundle= RuntimePlugin.getDefault().getBundle(); //$NON-NLS-1$
		
		if (bundle == null) {
			JavaCore.removeClasspathVariable(JSDAI_PUBLIC_PATHS, null);
		} else {
			URL installLocation= bundle.getEntry("/"); //$NON-NLS-1$
			URL local= null;
			try {
				local = Platform.asLocalURL(installLocation);
				try {
					String fullPath = new File(local.getPath()).getAbsolutePath();
					JavaCore.setClasspathVariable(JSDAI_PUBLIC_PATHS, new Path(fullPath), null);
				} catch (JavaModelException e1) {
					JavaCore.removeClasspathVariable(JSDAI_PUBLIC_PATHS, null);
				}
			} catch (IOException e) {
				JavaCore.removeClasspathVariable(JSDAI_PUBLIC_PATHS, null);
			}
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CommonPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("net.jsdai.common", path);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	private MessageConsoleStream consoleStream = null;

	public static final String EXPRESS_APPLICATION_NAME = "JSDAI-Developer";
	
	public MessageConsoleStream getConsole() {
		if (consoleStream == null) {
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			if (plugin != null) {
				ImageDescriptor idesc = imageDescriptorFromPlugin(PLUGIN_ID, "icons" + File.separator + "jsdai_icon_16.png");
				MessageConsole console = new MessageConsole(CommonPlugin.EXPRESS_APPLICATION_NAME, idesc);
				IConsoleManager manager = plugin.getConsoleManager();
				manager.addConsoles(new IConsole[]{console});
				consoleStream = console.newMessageStream();
			}
		}
		return consoleStream;
	}
	
	public static void console(String message) {
		MessageConsoleStream stream = getDefault().getConsole();
		if (stream != null) {
			stream.println(message);
			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(stream.getConsole());
		}
	}
	
	public static void console(Throwable t) {
		MessageConsoleStream stream = getDefault().getConsole();
		if (stream != null) {
			stream.println(t.toString());
			StackTraceElement[] stack = t.getStackTrace();
			for (int i = 0; i < stack.length; i++) stream.println("\t" + stack[i].toString());
			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(stream.getConsole());
		}
	}
	
	public static void log(IStatus status) {
		ILog log = getDefault().getLog();
		log.log(status);
	}
	
	public static void log(Throwable t) {
		t.printStackTrace();
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, CommonPlugin.EXPRESS_APPLICATION_NAME + " ERROR:" + t, t);
		log(status);
	}
}
