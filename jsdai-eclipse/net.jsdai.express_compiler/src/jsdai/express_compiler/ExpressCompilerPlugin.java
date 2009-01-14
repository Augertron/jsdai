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

package jsdai.express_compiler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jsdai.express_compiler.editor.ExpressColorProvider;
import jsdai.express_compiler.editor.ExpressPartitionScanner;
import jsdai.express_compiler.list_editor.ExpressListColorProvider;
import jsdai.express_compiler.list_editor.ExpressListPartitionScanner;
import jsdai.express_compiler.p21_editor.P21CodeScanner;
import jsdai.express_compiler.p21_editor.P21ColorProvider;
import jsdai.express_compiler.preferences.ExpressCompilerIOPreferences;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;
import jsdai.express_compiler.preferences.ExpressEditorPreferences;
import jsdai.express_compiler.preferences.ExpressPreferences;
import jsdai.express_compiler.preferences.ExpressProjectPreferences;
import jsdai.express_compiler.preferences.P21EditorPreferences;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.osgi.framework.BundleContext;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Ok,  this will be our  plugin class
 */
public class ExpressCompilerPlugin extends AbstractUIPlugin implements IStartup, IResourceChangeListener {


	// private String fWorking_directory = "F:\\WORKING";  // TODO - get it from property page, also enable to change it in there
	// ok, for now - if null, invoke a dialog to choose it, afterwards - take the same - to change - from the property page, perhaps
	private String fWorking_directory = null;  // TODO - get it from property page, also enable to change it in there

	public final static String EXPRESS_PARTITIONING = "__express_partitioning";   //$NON-NLS-1$
	public final static String EXPRESS_LIST_PARTITIONING = "__express_list_partitioning";   //$NON-NLS-1$
	public final static String ID_EXPRESS_COMPILER_LICENSE_LAST_DIR = "net.jsdai.express_compiler.last_dir";
	
	private static ExpressCompilerPlugin fgPlugin;
	private ResourceBundle fResourceBundle;
	public static final String EXPRESS_COMPILER_PLUGIN_ID = "net.jsdai.express_compiler";
	public static final String PLUGIN_ID = "net.jsdai.express_compiler";
//	public String fWorking_directory;


	/** The name of the JSDAI_RUNTIME classpath variable */
//	public static final String JSDAI_RUNTIME = "JSDAI_RUNTIME";

	
	// for providing singletons
	private ExpressPartitionScanner fPartitionScanner;
	private ExpressListPartitionScanner fListPartitionScanner;
	private ExpressColorProvider fColorProvider;
	private ExpressListColorProvider fListColorProvider;
	//private ExpressCodeScanner fCodeScanner;
	private P21ColorProvider fP21ColorProvider;
	private P21CodeScanner fP21CodeScanner;


	private IAction fExportToJarAction;
//  private IAction fCompileExpressProjectAction;
//  private IAction fCompileExpressEditorAction;
//  private IAction fCreateExpressFileAction;
  
//  private boolean fExportToHtmlEnabled;
//  private boolean fExportToJarEnabled;
//  private boolean fCompileExpressProjectEnabled;
//  private boolean fCompileExpressEditorEnabled;


	/**
	 * The express_compiler plugin constructor.
	 */
	public ExpressCompilerPlugin() {
		super();
		fgPlugin = this;
		try {
			fResourceBundle = ResourceBundle.getBundle("jsdai.express_compiler.ExpressCompilerPluginMessages");
		} catch (MissingResourceException e) {
			fResourceBundle = null;
			log(e);
		}
	}

	/**
	 * Returns the default plug-in instance.
	 * 
	 * @return the default plug-in instance
	 */
	public static ExpressCompilerPlugin getDefault() {
		return fgPlugin;
	}

	/**
	 *  Called upon plug-in activation, used to be useful with multiple interconnected plugins to deal with Mantas
	 *  may need it or not, we'll see
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	
	// old
	/*
		try {
			RegUtils.runRegCheckThread_inline(RuntimePlugin.getLicensingInfo());
		} catch (LicenseException ex) {
			MessageDialog.openError(null, LicensingInfo.TITLE, LicensingInfo.MESSAGE);   
			try {
				LicensingInfo linfo = RuntimePlugin.getLicensingInfo();
				LicensingDialog dialog = new LicensingDialog(linfo);
				if (dialog.open() == LicensingDialog.OK) {
					RegUtils.runRegCheckThread_inline(RuntimePlugin.getLicensingInfo());
					File dir = linfo.getLastUsedDir();
					if (dir != null && dir.isDirectory()) {
						getPreferenceStore().setValue(ID_EXPRESS_COMPILER_LICENSE_LAST_DIR, dir.getAbsolutePath());
						savePluginPreferences();
					}
				} else throw new LicenseException("Licensing aborted");
			} catch (Throwable t) {
				log(t);
				throw new SWTException(SWT.ERROR_FAILED_EXEC);
			}
		}
*/




// currently used, but problems

/*
		ApplicationInfoProvider info = CommonPlugin.getLicensingInfo();
		Object[] data = RegUtils.getRegData(info);
		Object plugins = data[LcConstants.PLUGINS_INDEX];
		console("check " + LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " Plugin license");		
		if (!(plugins instanceof String && ((String)plugins).indexOf(LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME) >= 0)) { // Express-G plugin is not registered
			if (MessageDialog.openConfirm(null, LicensingInfo.TITLE, 
					"Your registration does not apply for " + LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " Plugin\nDo You want to install new license file?")) {
				String result = CommonPlugin.getDefault().runRegistration();
				if (result == null) {
					console("license accepted");		
				} else {
					console(result);		
					throw new SWTException(SWT.ERROR_FAILED_EXEC, result);
				}
			} else {
				String message = "registration does not apply for " + LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " Plugin";
				console(message);		
				throw new SWTException(SWT.ERROR_FAILED_EXEC, message);
			}
		} else {
			console(LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME + " Plugin license accepted");
		}
*/
// end of currently used

	
	
	
	
	
		/*
		 *  ok, so let's create temp directories and files for all the express projects
		 *  that already exist, and therefore, their temp directories and files were deleted
		 *  when the eclipse was exited
		 *  
		 *  Alternative - do it on demand only, perhaps better, so do nothing here
		 *   
		 */
//			boolean createJar  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferencePage.CREATE_JAR);
//			System.out.println("to create jar: " + createJar);
			ExpressPreferences ep = ExpressPreferences.getExpressPreferences();
			if (ep == null) {
				ep = new ExpressPreferences();
			}
			ExpressCompilerPreferences ecp = ExpressCompilerPreferences.getExpressCompilerPreferences();
			if (ecp == null) {
				ecp = new ExpressCompilerPreferences();
			}
			ExpressCompilerIOPreferences eciop = ExpressCompilerIOPreferences.getExpressCompilerIOPreferences();
			if (eciop == null) {
				eciop = new ExpressCompilerIOPreferences();
			}
			ExpressProjectPreferences epp = ExpressProjectPreferences.getExpressProjectPreferences();
			if (epp == null) {
				epp = new ExpressProjectPreferences();
			}
			ExpressEditorPreferences eep = ExpressEditorPreferences.getEditorPreferences();
			if (eep == null) {
			eep = new ExpressEditorPreferences();
			}
			P21EditorPreferences p21ep = P21EditorPreferences.getP21EditorPreferences();
			if (p21ep == null) {
				p21ep = new P21EditorPreferences();
			}
	
//			createClasspathVariable("net.jsdai.runtime", JSDAI_RUNTIME);
	
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE);
	}

/*
	protected void createClasspathVariable(String pluginName, String variableName) {
		Bundle bundle= Platform.getBundle(pluginName); //$NON-NLS-1$
		
		if (bundle == null) {
			JavaCore.removeClasspathVariable(variableName, null);
			return;
		}
		else {
			URL installLocation= bundle.getEntry("/"); //$NON-NLS-1$
			URL local= null;
			try {
				local= Platform.asLocalURL(installLocation);
			} catch (IOException e) {
				JavaCore.removeClasspathVariable(variableName, null);
				return;
			}
			try {
				String fullPath= new File(local.getPath()).getAbsolutePath();
				JavaCore.setClasspathVariable(variableName, new Path(fullPath), null);
			} catch (JavaModelException e1) {
				JavaCore.removeClasspathVariable(variableName, null);
			}
		}		
	}
*/

	/**
	 *  Called when the plug-in is stopped, may be needed to save stuff, etc.
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		/* lets delete all the projects in temp directory
		  	perhaps not all the projects, because more than one Eclipse may be running
		  	at the same time, let's delete only express projects that belong to this eclipse
		  	
		  	To do that, just go through all the projects and delete them if they are express projects
		  	However, if a project is created outside the workspace, can we still get it?
		*/

		if(false) { // This section depends on no longer used EXPRESS PROJECT comment so is commented out
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		IResource resource = root.findMember(new Path(containerName));
		IProject[] projects = root.getProjects();
//System.out.println("<PROJECTS: >");
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
//System.out.println("<PROJECT:> " + project.getName());
			// check if it is an express project, if so - delete its temp location

			IProjectDescription descrip = null;
			try {
				descrip = project.getDescription();
				String prj_comments = descrip.getComment();
				if (prj_comments.indexOf("EXPRESS PROJECT") >= 0) {
					// OK, this is express project
					String x_temp_location = prj_comments.substring(prj_comments.indexOf("T)")+2, prj_comments.indexOf("(T"));
					if (!x_temp_location.equalsIgnoreCase("DEFAULT")) {
						// delete this directory and everything inside it
						ExpressCompilerUtils.deleteAllFilesAndDirectories(x_temp_location);
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				log(e);
				e.printStackTrace();
			}
		
		
		
		}
		}
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		
	}

	/**
	 * Tries to return the string from the resource bundle of the plugin, I think better to handle this stuff on per-package level, though
	 *
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ExpressCompilerPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			log(e);
			return key;
		}
	}

	/**
	 * Returns the resource bundle
	 */
	public ResourceBundle getResourceBundle() {
		return fResourceBundle;
	}


	// we may want or not to handle here creation and providing of the stuff for the editor here,
	// but we might do it another way too
	// if we do, we might have factory methods here to ensure singleton partition, code scanners and color provider - if we use it
	
	/**
	 * Return the singleton Express partition scanner.
	 * 
	 * @return the singleton Express partition scanner
	 */

	 public ExpressPartitionScanner getExpressPartitionScanner() {
		if (fPartitionScanner == null)
			fPartitionScanner= new ExpressPartitionScanner();
		return fPartitionScanner;
	 }

	 public ExpressListPartitionScanner getExpressListPartitionScanner() {
		if (fListPartitionScanner == null)
			fListPartitionScanner= new ExpressListPartitionScanner();
		return fListPartitionScanner;
	 }
	
	/**
	 * Returns the singleton Express code scanner.
	 * 
	 * @return the singleton Express code scanner
	 */
/*	
	 public RuleBasedScanner getExpressCodeScanner() {
	 	if (fCodeScanner == null)
			fCodeScanner= new ExpressCodeScanner(getExpressColorProvider());
		return fCodeScanner;
	 }
*/	

	 public P21CodeScanner getP21CodeScanner() {
	 	if (fP21CodeScanner == null)
			fP21CodeScanner= new P21CodeScanner(getP21ColorProvider());
		return fP21CodeScanner;
	 }


	/**
	 * Returns the singleton Express color provider.
	 * 
	 * @return the singleton Express color provider
	 */

	 public ExpressColorProvider getExpressColorProvider() {
	 	if (fColorProvider == null)
			fColorProvider= new ExpressColorProvider();
		return fColorProvider;
	 }
	 public ExpressListColorProvider getExpressListColorProvider() {
	 	if (fListColorProvider == null)
			fListColorProvider= new ExpressListColorProvider();
		return fListColorProvider;
	 }
	 public P21ColorProvider getP21ColorProvider() {
	 	if (fP21ColorProvider == null)
			fP21ColorProvider= new P21ColorProvider();
		return fP21ColorProvider;
	 }

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public synchronized String getWorkingDirectory() {
		if(fWorking_directory == null) {
			fWorking_directory =
				getPreferenceStore().getString(ExpressCompilerPreferences.EXPRESS_WORKING_DIRECTORY);
			if(fWorking_directory == null || fWorking_directory.length() == 0) {
				fWorking_directory = ExpressCompilerUtils.getSystemTempDirectoryString();;
				getPreferenceStore().setValue(ExpressCompilerPreferences.EXPRESS_WORKING_DIRECTORY, fWorking_directory);
			}
			new File(fWorking_directory).mkdirs();
		}
		return fWorking_directory;
	}
	public synchronized void setWorkingDirectory(String value) {
		fWorking_directory = value;
	}

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
	
	public static void log_alt(IStatus status) {
		ResourcesPlugin.getPlugin().getLog().log(status);
	}

	public static void log(IStatus status) {
		ILog log = getDefault().getLog();
		log.log(status);
	}

	public static void log(String message, int severity) {
		IStatus status = new Status(severity, EXPRESS_COMPILER_PLUGIN_ID, IStatus.OK, message, null);
		log(status);
	}


	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getUniqueIdentifier(), IStatus.ERROR, message, null));
	}

	public static void logException(Throwable e, final String title, String message) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else {
			if (message == null)
				message = e.getMessage();
			if (message == null)
				message = e.toString();
			status = new Status(IStatus.ERROR, getUniqueIdentifier(), IStatus.OK, message, e);
		}
		ResourcesPlugin.getPlugin().getLog().log(status);
		Display display;
		display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		final IStatus fstatus = status;
		display.asyncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(null, title, null, fstatus);
			}
		});
	}

	public static void logException(Throwable e) {
		logException(e, null, null);
	}

	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException)
			e = ((InvocationTargetException) e).getTargetException();

		IStatus status = new Status(IStatus.ERROR, EXPRESS_COMPILER_PLUGIN_ID, IStatus.OK, "Express Compiler ERROR: " + e, e);
		log(status);
	
		console(e.toString());
		console("See .log in .metadata for details");

	}

	/**
	* Utility method with conventions
	*/
	public static void errorDialog(Shell shell, String title, String message, IStatus s) {
		log(s);
		// if the 'message' resource string and the IStatus' message are the same,
		// don't show both in the dialog
		if (s != null && message.equals(s.getMessage())) {
			message = null;
		}
		ErrorDialog.openError(shell, title, message, s);
	}

	/**
	* Utility method with conventions
	*/
	public static void errorDialog(Shell shell, String title, String message, Throwable t) {
		log(t);
		IStatus status;
		if (t instanceof CoreException) {
			status = ((CoreException) t).getStatus();
			// if the 'message' resource string and the IStatus' message are the same,
			// don't show both in the dialog
			if (status != null && message.equals(status.getMessage())) {
				message = null;
			}
		} else {
			status = new Status(IStatus.ERROR, getUniqueIdentifier(), -1, "Internal Error: ", t); //$NON-NLS-1$	
		}
		ErrorDialog.openError(shell, title, message, status);
	}
	

	public IProject createExpressProject(
			final IProjectDescription description,
			final IProject projectHandle,
			IProgressMonitor monitor,
			final String projectID)
			throws CoreException, OperationCanceledException {

			getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						if (monitor == null) {
							monitor = new NullProgressMonitor();
						}
						monitor.beginTask("Creating Express Project...", 3); //$NON-NLS-1$
						if (!projectHandle.exists()) {
							projectHandle.create(description, new SubProgressMonitor(monitor, 1));
						}
						
						if (monitor.isCanceled()) {
							throw new OperationCanceledException();
						}
						
						// Open first.
						projectHandle.open(new SubProgressMonitor(monitor, 1));

//						mapExpressProjectOwner(projectHandle, projectID, false);

						// Add Express Nature ... does not add duplicates
//						ExpressProjectNature.addExpressNature(projectHandle, new SubProgressMonitor(monitor, 1));
					} finally {
						monitor.done();
					}
				}
			}, getWorkspace().getRoot(), 0, monitor);
			return projectHandle;
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
	
	public static void console(Throwable t) {
		MessageConsoleStream stream = getDefault().getConsole();
		if (stream != null) {
			stream.println(t.toString());
			StackTraceElement[] stack = t.getStackTrace();
			for (int i = 0; i < stack.length; i++) stream.println("\t" + stack[i].toString());
			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(stream.getConsole());
		}
	}
	
	
	public IAction getExportToJarAction() {
		return fExportToJarAction;
	}
	
	public void setExportToJarAction(IAction exportToJarAction) {
		fExportToJarAction = exportToJarAction;
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
	
} // plugin class ends here




