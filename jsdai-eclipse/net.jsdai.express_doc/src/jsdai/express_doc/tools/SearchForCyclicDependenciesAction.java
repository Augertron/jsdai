/*
    Copyright (C) 2005 LKSoftWare GmbH
*/

package jsdai.express_doc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jsdai.util.UtilMonitor;
import jsdai.runtime.RuntimePlugin;
import jsdai.express_doc.ExpressDocPlugin;
//import jsdai.common.utils.IsolatedRunnableThread;
import jsdai.express_doc.utils.IsolatedRunnableThread;
import jsdai.common.utils.CommonUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.core.runtime.CoreException;

/*

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jsdai.common.CommonPlugin;
import jsdai.common.licensing.LicensingInfo;
import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.p21_editor.ValidationMessageParser;
import jsdai.express_compiler.preferences.ExpressPreferences;
import jsdai.express_compiler.properties.ExpressProjectTempPropertyPage;
import jsdai.express_compiler.utils.ExpressCompilerUtils;
import jsdai.express_compiler.utils.IsolatedRunnableThread;
import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.swt.widgets.Shell;

*/

public class SearchForCyclicDependenciesAction extends Action implements IWorkbenchWindowActionDelegate {
	

	String fDestination;
	String fExdFile;
	ISelection fSelection;
	IAction fAction;
//	IProject fProject;
	boolean fIn_express_project;
	boolean fIn_project;
	boolean flagDeleteAllJSDAIMarkers;
	boolean flagDeleteAllP21Markers;

	String fProject_comments;
	String fWorking_directory;
	String fOutput;
	private static boolean fUseIsolatedThread = true;

	String fCycliccheckOutput;
	IWorkbenchPage fPage;

	
  public void dispose() {
    // TODO Auto-generated method stub
    
  }
  

  public void init(IWorkbenchWindow window) {
    // TODO Auto-generated method stub
	  
		//ExpressCompilerPlugin.getDefault().setExportToJarlAction(this);
//	  IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();		help.setHelp(fAction, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	  WorkbenchHelp.setHelp(this, ValidationPlugin.VALIDATION_PLUGIN_ID + ".ValidateContextId");
	  
//		help.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	 //   net.jsdai.express_compiler.actions.ExportToJarAction
  }

  public void selectionChanged(IAction action, ISelection selection) {
		fSelection = selection;
		fAction = action;
		fExdFile = null;
//		String java_directory = null;
		

		boolean enabled = false;
//		try {
			if (selection instanceof IStructuredSelection) {

System.out.println("in structured selection - ############");


				IStructuredSelection sel = (IStructuredSelection) selection;
				List selResources = IDE.computeSelectedResources(sel);
				for (Iterator i = selResources.iterator(); i.hasNext();) {
					IResource resource = (IResource) i.next();
					if (resource instanceof IFile) {
						IFile file = (IFile) resource;
						String fileName = file.getName();
//						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
						if(fileName.endsWith(".exd")) {
							if(fExdFile == null) {
								fExdFile = file.getLocation().toOSString();
							} else {
								fExdFile = "";
							}
						}
					}
/*
					fProject = resource.getProject();
					fIn_project = true;
					// true, if express project, 
					// if not - check working directory:
					// if it is defined, if it contains classes folder
					enabled = checkEnabledForSelectedProject();
*/
				}

			} else if (selection instanceof ITextSelection) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				
				IEditorPart active = page.getActiveEditor();
				if (active != null) {
					IEditorInput active_input = active.getEditorInput();
					if (active_input instanceof FileEditorInput) {
						FileEditorInput fedin = (FileEditorInput)active_input;
						IFile file = fedin.getFile();
						String fileName = file.getName();
//						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
						if(fileName.endsWith(".exd")) {
							if(fExdFile == null) {
								fExdFile = file.getLocation().toOSString();
							} else {
								fExdFile = "";
							}
						}
//						fIn_project = true;
//						fProject = file.getProject();
//						enabled = checkEnabledForSelectedProject();
					}
				}
			}

//		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			//ValidationPlugin.log(e1);
//			ExpressCompilerPlugin.console(e1.toString());
//			System.out.println("ExportToJarAction - Exception when checking project nature:" + e1);

			//e1.printStackTrace();
//		}

//		ExpressCompilerPlugin.getDefault().setExportToJarAction(action);
    
    // it is really enabled only if java directory is present - that indicates that express has been successfully compiled
    // it could be done in a different way - working only with ExpressCompilerRepo.sdai file, as it is the only input that is needed. 
//		action.setEnabled(enabled);
    
  }

  public void run(IAction action) {

//System.out.println("<><01>Running validation");
    // TODO Auto-generated method stub

//	    IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();
//	    help.setHelp(action, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    help.setHelp(action, "net.jsdai.express_compiler.actions.ExportToJarAction" + ".ExportJarContextId");
//	    help.displayHelp(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    IContext context;	    



//		String message = CommonPlugin.checkRegistration(new String[]{LicensingInfo.EXPRESS_COMPILER_PLUGIN_NAME}); 

//		flagDeleteAllJSDAIMarkers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_JSDAI_MARKERS);
//		flagDeleteAllP21Markers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_P21_MARKERS);


	  
//	  if (message != null)  { //  exception description
// System.out.println(message);
//			return;
//		}

//	  System.out.println("<><02>Running validation, validation plugin: " + ValidationPlugin.class);
	    
		Shell shell = ExpressDocPlugin.getDefault().getShell();
	
	//  	Shell shell = null;
	  
		String path = null;
//		path = fProject.getProject().getLocation().toOSString();



		if (false) {
//		if (fIn_express_project) {
//System.out.println("<><03>Running validation, in Express Project");
//		if (false) {


/*
    	fDestination = fProject.getProject().getLocation().toOSString();
    	if (fDestination.endsWith("\\") || fDestination.endsWith("/")) {
	    	fDestination += fProject.getName() + ".jar";
    	} else {
	    	fDestination += File.separator + fProject.getName() + ".jar";
				
    	}
*/

// System.out.println("here's jar: " + fDestination); 
// here's jar: H:\eclipse\runtime-EclipseApplication\expressions_schema\.jar
//System.out.println("<><03-2>Running validation - IF end");
		} else {

/*

//System.out.println("<><04>Running validation, in Express Project");
			FileDialog dialog = new FileDialog(shell, SWT.SINGLE);
//		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
			dialog.setText("select the library jar");
	    dialog.setFilterPath(path);
//	    dialog.setFileName(fProject.getName() + ".jar");
	    dialog.setFilterExtensions(new String[] {"*.jar"});
	    String name = dialog.open();

			if (name == null) { // probably hit CANCEL
				return;
			}
			
	    if (!name.endsWith(".jar")) {
	    	name = name + ".jar";
	    }
  
    	fDestination = name;
// System.out.println("here's jar: " + fDestination); 
// here's jar: H:\eclipse\runtime-EclipseApplication\expressions_schema\expressions_schema.jar


*/
		 }

//System.out.println("<><03-3>Running validation, after Express Project check, fStepFile: " + fStepFile);

		if (fExdFile == null) {
			fExdFile = "";
		}
		
		
		
		if (fExdFile.equals("")) {
//System.out.println("<><05>Running validation");

		FileDialog dialog2 = new FileDialog(shell, SWT.SINGLE);
			dialog2.setText("select the exd file");
//		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
	    dialog2.setFilterPath(path);
//	    dialog.setFileName(fProject.getName() + ".jar");
//	    dialog2.setFilterExtensions(new String[] {"*.stp","*.p21","*.pf" });
//	    dialog2.setFilterExtensions(new String[] {"*.stp;*.p21;*.pf" });
	    dialog2.setFilterExtensions(new String[] {"*.exd" });
	    if(fExdFile != null && fExdFile.length() != 0) {
	    	dialog2.setFilterPath(new File(fExdFile).getParent());
	    	dialog2.setFileName(new File(fExdFile).getName());
	    }
	    String name2 = dialog2.open();
			
			if (name2 == null) { // probably hit CANCEL
				return;
			}
			
//	    if (!name.endsWith(".jar")) {
//	    	name = name + ".jar";
//	    }
    	fExdFile = name2;
	 } else {
//System.out.println("<><06>Running validation");
	 }
 
 
 
		
//		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
//System.out.println("<><07>Running validation");
					doRun(monitor);
				} catch (CoreException e) {
//					ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		MessageBox memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		memoryBox.setMessage("Cyclic checking needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		memoryBox.setText("Out of memory error");
		try {
//System.out.println("<2>fValidateOutput: " + fValidateOutput);
			//ValidationPlugin.log("<1>kuku-bebe: " + fValidateOutput, 1);
			(new ProgressMonitorDialog(null)).run(true, true, op);
//System.out.println("<3>fValidateOutput: " + fValidateOutput);
			//ValidationPlugin.log("<2>kuku-bebe: " + fValidateOutput, 1);
		} catch (InterruptedException e) {
//			ExpressCompilerPlugin.log(e);
//System.out.println("Validation CANCELED: " + e);
			return;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
//			System.out.println("ValidateAction excption: " + e);
//			System.out.println("ValidateAction real excption: " + realException);
			ExpressDocPlugin.log(realException);
//			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
		}

//System.out.println("<3>fValidateOutput: " + fValidateOutput);
			//ValidationPlugin.log("<3>kuku-bebe: " + fValidateOutput, 1);
	CommonUtils.printResultToConsole(fCycliccheckOutput, fPage, fOutput);
			//ValidationPlugin.log("<5>kuku-bebe: " + fValidateOutput, 1);

    
  }

	void doRun(IProgressMonitor monitor) throws CoreException, InterruptedException {

	
//		monitor.beginTask("Validating a step file", IProgressMonitor.UNKNOWN);
		


    String current_directory;

//	if (fIn_express_project) {	
	if (false) {	

		String s_temp_location = "_no_value_";

//		try {
//			s_temp_location = ExpressProjectTempPropertyPage.getTempFileLocation(fProject);
			s_temp_location = CommonUtils.getSystemTempDirectoryString();
//		} catch (CoreException e1) {
//			ValidationPlugin.log(e1);
//			System.out.println("ValidateAction - Exception - Problems with reading of persistent properties: " + e1);
//			e1.printStackTrace();
//		}
		current_directory = s_temp_location;
		
		// do this here for now:
		fWorking_directory = current_directory;


	} else {
		String s_temp_location = CommonUtils.getSystemTempDirectoryString();
		fWorking_directory = s_temp_location;
//		fWorking_directory = "C:\\temp";
        
		// for now
		new File(fWorking_directory).mkdirs();
		
		if (fWorking_directory.endsWith("\\") || fWorking_directory.endsWith("/")) {
			current_directory = fWorking_directory + "temp";
		} else {
			current_directory = fWorking_directory + File.separator + "temp";
		}
		// for now
		new File(current_directory).mkdirs();
		

System.out.println("working directory: " + fWorking_directory);		
System.out.println("current directory: " + current_directory);		
		
	
	}


		String monitor_exec_string = "Reading the step file";

		
		
		String jar_path = fDestination;

		String output_file_path = current_directory; 
		String error_file_path =  current_directory;
				
		if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
					output_file_path  += "log_output";
					error_file_path   += "log_errors";
		} else {
					output_file_path  += File.separator + "log_output";
					error_file_path   += File.separator + "log_errors";
		}

    fOutput = output_file_path;
//System.out.println("output: " + output_file_path);
//System.out.println("errors: " + error_file_path);

		String [] args;	
		if (fUseIsolatedThread) {
			args = new String[8];
		} else {
			args = new String[10];
		}
//		args = new String[3];

		int ii = 0;

		if (!fUseIsolatedThread) {
			args[ii++] = "java";
			args[ii++] = "jsdai.tools.SearchForCyclicDependencies";
		}
		args[ii++] = "-location";
		args[ii++] = fExdFile;
		args[ii++] = "-stdout";
		args[ii++] = new File(current_directory, "log_output").getAbsolutePath();
		args[ii++] = "-stderr";
		args[ii++] = new File(current_directory, "log_errors").getAbsolutePath();

    args[ii++] = "-oc";
    args[ii++] = "-redundant";

		String env[] = null; 
		
		if(!fUseIsolatedThread) {
			String classpath_string = "";

//			String cpath = CommonUtils.getClassPath(ExpressDocPlugin.getDefault().getBundle(), "jsdai_tools.jar");
			String cpath = CommonUtils.getClassPath(Platform.getBundle("net.jsdai.tools"),"jsdai_tools.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = CommonUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = CommonUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = jar_path;
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			String new_classpath = "." + File.pathSeparator + classpath_string;
			
			env = new String[2]; 
			env[0] = "jsdai.properties=" + current_directory;
			env[1] = "CLASSPATH=" + new_classpath;
		}


		File error_stream_file = new File(error_file_path);
		File output_stream_file = new File(output_file_path);
    error_stream_file.delete();
    output_stream_file.delete();

		fCycliccheckOutput = "Running Cyclic Dependencies check!\n";


		Runtime r = Runtime.getRuntime();

	try {
    IsolatedRunnableThread cycliccheckThread = null;
		Process p = null;

	  boolean monitor_task_started = false;
	
	  // an experiment to see if it is possible to move this inside the loop
	  // yes, it is possible
//	  monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);
		//	monitor.beginTask(monitor_exec_string, 10);

		UtilMonitorImpl cycliccheckMonitor = new UtilMonitorImpl(Thread.currentThread());
		try {
			
	    	if(fUseIsolatedThread) {
	    		//V.N. Load express compiler using custom class loading
	    	 	File[] cycliccheckJars = {
	  	    new File(CommonUtils.getClassPath(
	    						Platform.getBundle("net.jsdai.tools"), "jsdai_tools.jar")),
//	    						ExpressDocPlugin.getDefault().getBundle(), "jsdai_doc.jar")),
  				new File(CommonUtils.getClassPath(
	    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
	    				new File(CommonUtils.getClassPath(
	    						Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip")),
//						new File(jar_path),
	    			};
	    	 	String sdaireposDirectory = new File(current_directory, "ExpressCompilerRepo").getCanonicalPath();
	    	 	cycliccheckThread =
	    	 		IsolatedRunnableThread.newInstance("jsdai.tools.SearchForCyclicDependencies", "initAsRunnable",
      	 				new Class[] { String.class, String[].class, UtilMonitor.class },
	    	 				// new Class[] { String.class, String[].class },
//	    	 				new Object[] { sdaireposDirectory, args },
      	 				new Object[] { sdaireposDirectory, args, cycliccheckMonitor },
	    	 				cycliccheckJars, 
	 				      new String[] { "jsdai/util/UtilMonitor.class", "com/lksoft/util/licensing/" });
	    	 				// null);
	    		cycliccheckThread.start();
	    	} else {
	    		p = r.exec(args, env, new File(current_directory));
	    	}

		long start_time, current_time, elapsed_time = 0;
//			if (!Platform.getOS().equals("win32")) {

		if (true) {

//					p.waitFor();

			int monitor_count = 0;
      boolean monitor_reinitialized = false;
			boolean is_cancel = false;
//			int exit_code = -555;
			start_time = System.currentTimeMillis();
			for (;;) {
				if(!Thread.interrupted()) {
					if(fUseIsolatedThread) {
						try {
							cycliccheckThread.join(1000);
						} catch (InterruptedException e) {
						}
						if(!cycliccheckThread.isAlive()) {
							break;
						}
					} else {
					    try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}

						try {
							/*int exit_code = */p.exitValue();
							break;
						} catch (IllegalThreadStateException e1) {
						}
					}
				}
				String subtaskMessage;
				int subtaskCount;
				int subtaskCounter;
				synchronized (cycliccheckMonitor) {
					subtaskMessage = cycliccheckMonitor.message;
					subtaskCount = (int)cycliccheckMonitor.count;
					subtaskCounter = (int)cycliccheckMonitor.counter;
					if (subtaskCounter > 0) {
					 subtaskCount -= subtaskCounter;
			    }
			    // ValidationPlugin.log("UtilMonitor - message: " + subtaskMessage + ", count: " + subtaskCount + ", counter: " + subtaskCounter, 1);
				}
				current_time = System.currentTimeMillis();
				elapsed_time = (current_time - start_time) / 1000;
//				monitor.setTaskName(("Validating a step file: " + elapsed_time + " s"));
				// putting the initialization inside the loop so that we can get from Gintars the number of steps in the very first message
			
      
	    if (monitor_task_started) {
//	    if ((monitor_count < 10) && (monitor_count > 0)) {
			// if (true) {
//					if (subtaskMessage != null) {
					if (subtaskCount > 0) {
						if (monitor_reinitialized) {
						} else {
							monitor.beginTask("starting cyclic check", subtaskCount);
							monitor_reinitialized = true;
						}
					}
					if (subtaskMessage != null) {
						monitor.setTaskName("Checking " + subtaskMessage);
						monitor.worked(subtaskCounter - monitor_count);
  					monitor_count = subtaskCounter;
					} else {
						// should not happen
 					  monitor.setTaskName("Cyclic check: " + elapsed_time + "s");
						monitor.worked(1);
					}
//					monitor.setTaskName((subtaskMessage != null ? subtaskMessage : monitor_exec_string) + ": " + elapsed_time + "s");
//					monitor.worked(1);

//					monitor_count++;
				} else {
//					if (monitor_count != 0) {
						//monitor.endTask
//					}
					monitor.beginTask("Cyclic check: 0s", IProgressMonitor.UNKNOWN);
//					monitor.beginTask(monitor_exec_string + ": 0s", 10);
					monitor_task_started = true;
					System.out.println("starting task - delayed");
					monitor_count = 1;
				}
				is_cancel = monitor.isCanceled();
				if (is_cancel) {
					fCycliccheckOutput += "Cyclic check canceled";
					if(fUseIsolatedThread) {
						cycliccheckThread.interrupt();
					} else {
						p.destroy();
					}
					throw (new InterruptedException());
				}
			}		
		} // if true (OS)

				error_stream_file = new File(error_file_path);
				output_stream_file = new File(output_file_path);
   	    InputStream error_stream = new FileInputStream(error_stream_file);
				InputStream stream = new FileInputStream(output_stream_file);


//   	    InputStream error_stream = p.getErrorStream();
//				InputStream stream = p.getInputStream();



System.out.println("Getting output string");
					monitor.beginTask("Getting validation output", IProgressMonitor.UNKNOWN);
//					monitor.setTaskName("getting validation output");

				String cycliccheckOutput = CommonUtils.getStringFromStreamAvailable(stream);

System.out.println("Got output string: " + cycliccheckOutput);
					monitor.setTaskName("got cyclic check output");
		
		//		String validateOutput = ExpressCompilerUtils.getStringFromStream(stream);
//		System.out.println("Getting error string");

		
		String cycliccheckProcessErrors = CommonUtils.getStringFromStreamAvailable(error_stream);
					monitor.setTaskName("got cyclic check errors");

		
//	    String validateProcessErrors = ExpressCompilerUtils.getStringFromStream(error_stream);
//	    System.out.println("Errors: " + validateProcessErrors);
//		System.out.println("Output: " + validateOutput);			
		stream.close();
					monitor.setTaskName("output stream closed");
		error_stream.close();
					monitor.setTaskName("error stream closed");

	monitor.worked(1);

			fCycliccheckOutput += "--- Cyclic Check process errors, if present ---\n\n";
			fCycliccheckOutput += cycliccheckProcessErrors;
			fCycliccheckOutput += "--- Cyclic Check Results ---\n\n";
			fCycliccheckOutput += cycliccheckOutput;
			fCycliccheckOutput += "\n--- End of Cyclic Check Results ---\n";

					monitor.setTaskName("fCyclickcheckOutput size: " + fCycliccheckOutput.length());

//			createMarkers(validateOutput, fStepFile);
//			createMarkers(validateOutput, fStepFile, monitor);

//		} catch (Exception e1) {
//			ExpressCompilerPlugin.log(e1);
//			e1.printStackTrace();
		} finally {
			Thread.interrupted();
			if(cycliccheckThread != null) {
				cycliccheckThread.close();
			}
		}
	} catch (IOException e) {
		ExpressDocPlugin.log(e);
		e.printStackTrace();
	}

	monitor.worked(1);





//			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);

	
	}


		static final class UtilMonitorImpl implements UtilMonitor {
			String message;
			long count;
			long counter;
			private Thread parentThread;
			
			public UtilMonitorImpl(Thread parentThread) {
				this.message = null;
				this.count = -1;
				this.counter = -1;
				this.parentThread = parentThread;
			}

			public void subTask(String message, long count, long counter) {
				synchronized (this) {
					this.message = message;
					this.count = count;
					this.counter = counter;
				}
				parentThread.interrupt();
			}

			public void worked(int value) {
			}
		}

}

