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


package jsdai.xim.validation;

/*
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import jsdai.express_g.common.Resources;
*/

import java.io.File;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

// import jsdai.common.licensing.LicensingInfo;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiException;

/**
 * The main plugin class to be used in the desktop.
 */
public class ValidationPlugin extends AbstractUIPlugin {
	public static final String ID_VALIDATION = "net.jsdai.xim.validation";
	public static final String VALIDATION_PLUGIN_ID = "net.jsdai.xim.validation";
	//The shared instance.
	private static ValidationPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	// SdaiSession
	private SdaiSession sdaiSession = null;
	
	/**
	 * The constructor.
	 */
	public ValidationPlugin() {
		super();

		plugin = this;
		
		try {
			resourceBundle = ResourceBundle.getBundle("jsdai.xim.validation.ValidationPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		/*
			 let's delete _temporary_xim.stp file from all the projects, if present
		*/
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			String xim_file_str = project.getLocation().toString()	+ File.separator + "_temporary_xim.pf"; 				
			File xim_file = new File(xim_file_str);
			xim_file.delete();
			project.refreshLocal(IResource.DEPTH_ONE, null);
		} // for - loop through all the projects in the workspace
	}




	/**
	 * Returns the shared instance.
	 */
	public static ValidationPlugin getDefault() {
//System.out.println("<<>> ValidationPlugin getDefault() - plugin: " + plugin);
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ValidationPlugin.getDefault().getResourceBundle();
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
	
	/**
	 * use this method to get SdaiSession instance in this plugin
	 * SdaiSession temporary repository dir is placed in .metadata
	 * (.metadata/.plugins/jsdai.xim.validation/sdairepos)
	 * @return open SdaiSession
	 * @throws SdaiException
	 */
	public SdaiSession getSdaiSession() throws SdaiException {
		if (sdaiSession == null) {
			sdaiSession = SdaiSession.getSession();
			if (sdaiSession == null) {
				Properties prop = new Properties();
				File repoDir = getStateLocation().append("sdairepos").toFile();
				if (!repoDir.exists()) repoDir.mkdirs();
				prop.setProperty("repositories", repoDir.getAbsolutePath());
				SdaiSession.setSessionProperties(prop);
				sdaiSession = SdaiSession.openSession();
			}
		}
		return sdaiSession;
	}

/*	
	private MessageConsoleStream consoleStream = null;
	
	public MessageConsoleStream getConsole() {
		if (consoleStream == null) {
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			if (plugin != null) {
				MessageConsole console = new MessageConsole(LicensingInfo.VALIDATION_PLUGIN_NAME, Resources.getImageDescriptor(Resources.JSDAI));
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
*/	

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	
	
	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}
	
	
	public Shell getShell() {
		if (getActiveWorkbenchShell() != null) {
			return getActiveWorkbenchShell();
		} else {
			IWorkbenchWindow[] windows = getDefault().getWorkbench().getWorkbenchWindows();
			return windows[0].getShell();
		}
	}
	


	private MessageConsoleStream consoleStream = null;
	
	public MessageConsoleStream getConsole() {
		if (consoleStream == null) {
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			if (plugin != null) {
				ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this.getClass(), "jsdai_icon_16.png");
//				MessageConsole console = new MessageConsole("Express Compiler", Resources.getImageDescriptor(Resources.JSDAI));
				MessageConsole console = new MessageConsole("Express Compiler", imageDescriptor);
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
	public static void console2(String message) {
		MessageConsoleStream stream = getDefault().getConsole();
		if (stream != null) {
			stream.print(message);
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
	
	public static void log(String message, int severity) {
		IStatus status = new Status(severity, ID_VALIDATION, IStatus.OK, message, null);
		log(status);
	}
	
	public static void log(Throwable t) {
		t.printStackTrace();
//		IStatus status = new Status(IStatus.ERROR, ID_VALIDATION, IStatus.OK, LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " ERROR:" + t, t);
		IStatus status = new Status(IStatus.ERROR, ID_VALIDATION, IStatus.OK, "Validation ERROR:" + t, t);
		log(status);
	}
}


