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

package jsdai.step_editor;

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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
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
import jsdai.step_editor.editor.P21CodeScanner;
import jsdai.step_editor.editor.P21ColorProvider;
import jsdai.step_editor.preferences.P21EditorPreferences;

/**
 * The main plugin class to be used in the desktop.
 */
//public class Step_editorPlugin extends AbstractUIPlugin {
//public class Step_editorPlugin extends AbstractUIPlugin implements IStartup, IResourceChangeListener {
public class Step_editorPlugin extends AbstractUIPlugin implements IResourceChangeListener {
	public static final String ID_STEP_EDITOR = "net.jsdai.step_editor";
	public static final String STEP_EDITOR_PLUGIN_ID = "net.jsdai.step_editor";
	public static final String PLUGIN_ID = "net.jsdai.step_editor";
	//The shared instance.
	private static Step_editorPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	// SdaiSession
	private SdaiSession sdaiSession = null;
	
	private P21ColorProvider fP21ColorProvider;
	private P21CodeScanner fP21CodeScanner;
	
	
	/**
	 * The constructor.
	 */
	public Step_editorPlugin() {
		super();

		plugin = this;
		
		try {
			resourceBundle = ResourceBundle.getBundle("jsdai.step_editor.Step_editorPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		P21EditorPreferences p21ep = P21EditorPreferences.getP21EditorPreferences();
		if (p21ep == null) {
			p21ep = new P21EditorPreferences();
		}
	
  	ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE);

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Step_editorPlugin getDefault() {
System.out.println("<<>> Step_editorPlugin getDefault() - plugin: " + plugin);
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = Step_editorPlugin.getDefault().getResourceBundle();
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
	 * (.metadata/.plugins/jsdai.step_editor/sdairepos)
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
				MessageConsole console = new MessageConsole(LicensingInfo.STEP_EDITOR_PLUGIN_NAME, Resources.getImageDescriptor(Resources.JSDAI));
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
				MessageConsole console = new MessageConsole("Step Editor", imageDescriptor);
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
		IStatus status = new Status(severity, ID_STEP_EDITOR, IStatus.OK, message, null);
		log(status);
	}
	
	public static void log(Throwable t) {
		t.printStackTrace();
//		IStatus status = new Status(IStatus.ERROR, ID_STEP_EDITOR, IStatus.OK, LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " ERROR:" + t, t);
		IStatus status = new Status(IStatus.ERROR, ID_STEP_EDITOR, IStatus.OK, "step editor ERROR:" + t, t);
		log(status);
	}

	 public P21CodeScanner getP21CodeScanner() {
	 	if (fP21CodeScanner == null)
			fP21CodeScanner= new P21CodeScanner(getP21ColorProvider());
		return fP21CodeScanner;
	 }

	 public P21ColorProvider getP21ColorProvider() {
	 	if (fP21ColorProvider == null)
			fP21ColorProvider= new P21ColorProvider();
		return fP21ColorProvider;
	 }


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */

  // TODO see here what about express nature etc
	// express nature perhaps can be used here, to see, it should be accessable from non-related plugins as well
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			IResource eventResource = event.getResource();
			if (eventResource instanceof IProject) {
				IProject eventProject = (IProject) eventResource;
				if(eventProject.isAccessible() && eventProject.hasNature("net.jsdai.express_compiler.expressNature")) {
					String system_temp_location_str =
						eventProject.getPersistentProperty(new QualifiedName(PLUGIN_ID,".tempLocationSystem"));
					if(system_temp_location_str != null) {
						deleteRecursive(new File(system_temp_location_str));
					}
				}
				IPath working_location = eventProject.getWorkingLocation(getUniqueIdentifier());
				deleteRecursive(working_location.toFile());
			}
		} catch (CoreException e) {
			log(e);
		}
	}

	  private static void deleteRecursive(File fileOrDir) {
		    File subFiles[] = fileOrDir.listFiles();
		    if(subFiles != null) {
		      for (int i = 0; i < subFiles.length; i++) {
		        deleteRecursive(subFiles[i]);
		      }
		    }
		    fileOrDir.delete();
		  }
	  /**
	     * Convenience method which returns the unique identifier of this plugin.
	     */
	  public static String getUniqueIdentifier() {
	    if (getDefault() == null) {
	      // If the default instance is not yet initialized,
	      // return a static identifier. This identifier must
	      // match the plugin id defined in plugin.xml
	      return "net.jsdai.express_compiler"; //$NON-NLS-1$
	    }
	    return getDefault().getBundle().getSymbolicName();
	  }

	  
}
