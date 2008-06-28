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

package jsdai.express_g;

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
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;

/**
 * The main plugin class to be used in the desktop.
 */
public class SdaieditPlugin extends AbstractUIPlugin {
	public static final String ID_SDAIEDIT = "net.jsdai.express_g";
	public static final String ID_EXPRESS_G_OUTLINE = ID_SDAIEDIT + ".editors.outline.InternalContentOutline";
	public static final String EXPRESS_G_PLUGIN_NAME = "Express-G Editor";

	//The shared instance.
	private static SdaieditPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	// SdaiSession
	private SdaiSession sdaiSession = null;
	
	/**
	 * The constructor.
	 */
	public SdaieditPlugin() {
		super();

		plugin = this;
		
		try {
			resourceBundle = ResourceBundle.getBundle("jsdai.express_g.SdaieditPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
/* UPGRADE 2005-11-17 moved registration checking to editors, wizards, actions, etc.
		String message = CommonPlugin.checkRegistration(new String[]{LicensingInfo.EXPRESS_G_PLUGIN_NAME}); 
		if (message != null) {
			throw new SWTException(SWT.ERROR_FAILED_EXEC, message);
		};
*/		
		
/* UPGRADE disabled 2005-11-08 using checkRegistration
		ApplicationInfoProvider info = RuntimePlugin.getLicensingInfo();
		Object[] data = RegUtils.getRegData(info);
		Object plugins = data[LcConstants.PLUGINS_INDEX];
		console("check " + LicensingInfo.EXPRESS_G_PLUGIN_NAME + " Plugin license");		
		if (!(plugins instanceof String && ((String)plugins).indexOf(LicensingInfo.EXPRESS_G_PLUGIN_NAME) >= 0)) { // Express-G plugin is not registered
			if (MessageDialog.openConfirm(null, LicensingInfo.TITLE, 
					"Your registration does not apply for " + LicensingInfo.EXPRESS_G_PLUGIN_NAME + " Plugin\nDo You want to install new license file?")) {
				String result = RuntimePlugin.getDefault().runRegistration();
				if (result == null) {
					console("license accepted");		
				} else {
					console(result);		
					throw new SWTException(SWT.ERROR_FAILED_EXEC, result);
				}
			} else {
				String message = "registration does not apply for " + LicensingInfo.EXPRESS_G_PLUGIN_NAME + " Plugin";
				console(message);		
				throw new SWTException(SWT.ERROR_FAILED_EXEC, message);
			}
		} else {
			console(LicensingInfo.EXPRESS_G_PLUGIN_NAME + " Plugin license accepted");
		}
		
/** disabled licensing 2005-09-23
 *  registration not tested 
 * License checking and registering moved to Runtime plugin 2005-10-19
//console("START");		
		
						
						
						
		try {
console("check license");		
			RegUtils.runRegCheckThread_inline(RuntimePlugin.getLicensingInfo());
console("license ok");		
		} catch (LicenseException ex) {
//console("open warning");		
			MessageDialog.openError(null, LicensingInfo.TITLE, LicensingInfo.MESSAGE);   
			try {
//console("creating license info");		
				LicensingInfo linfo = RuntimePlugin.getLicensingInfo();
//console("creating license dialog");		
				LicensingDialog dialog = new LicensingDialog(linfo);
//console("open licensing");		
				if (dialog.open() == LicensingDialog.OK) {
//console("dialog ended on OK");		
//console("check license2");		
					RegUtils.runRegCheckThread_inline(RuntimePlugin.getLicensingInfo());
					File dir = linfo.getLastUsedDir();
//console("license file:" + dir.toString());		
					if (dir != null && dir.isDirectory()) {
						getPreferenceStore().setValue(ID_EXPRESS_G_LICENSE_LAST_DIR, dir.getAbsolutePath());
						savePluginPreferences();
					}
				} else throw new LicenseException("Licensing aborted");
			} catch (Throwable t) {
console("Licensing aborted");
//console(t);
				log(t);
				throw new SWTException(SWT.ERROR_FAILED_EXEC);
			}
		}
//console("STARTUP finished");
		/**@regcheck*/
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		// TODO maybe delete temporary repository directory, see getSdaiSession()
	}

	/**
	 * Returns the shared instance.
	 */
	public static SdaieditPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = SdaieditPlugin.getDefault().getResourceBundle();
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
	 * (.metadata/.plugins/jsdai.express_g/sdairepos)
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
//			System.setProperty("jsdai.properties", ppath);
//			JSDAIProperties.createJSDAIProperties(ppath);
				sdaiSession = SdaiSession.openSession();
			}
		}
		return sdaiSession;
	}

	private MessageConsoleStream consoleStream = null;
	
	public MessageConsoleStream getConsole() {
		if (consoleStream == null) {
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			if (plugin != null) {
				MessageConsole console = new MessageConsole(EXPRESS_G_PLUGIN_NAME, Resources.getImageDescriptor(Resources.JSDAI));
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
	
	public static void log(String message, int severity) {
//		if (severity == IStatus.INFO)
//			System.out.println(message);
//		else
//			System.err.println(message);
		IStatus status = new Status(severity, ID_SDAIEDIT, IStatus.OK, message, null);
		log(status);
	}
	
	public static void log(Throwable t) {
		t.printStackTrace();
		IStatus status = new Status(IStatus.ERROR, ID_SDAIEDIT, IStatus.OK, EXPRESS_G_PLUGIN_NAME + " ERROR:" + t, t);
		log(status);
	}
}
