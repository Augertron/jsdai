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

package jsdai.express_compiler.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import jsdai.util.UtilMonitor;
import jsdai.common.utils.CommonUtils;
import jsdai.common.utils.UtilMonitorImpl;
import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.p21_editor.ValidationMessageParser;
import jsdai.express_compiler.preferences.ExpressPreferences;
import jsdai.express_compiler.properties.ExpressProjectTempPropertyPage;
import jsdai.express_compiler.utils.ExpressCompilerUtils;
import jsdai.express_compiler.utils.IsolatedRunnableThread;
//import jsdai.common.utils.IsolatedRunnableThread;
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

public class ValidateAction extends Action implements IWorkbenchWindowActionDelegate {
	

	String fDestination;
	String fStepFile;
	ISelection fSelection;
	IAction fAction;
	IProject fProject;
	boolean fIn_express_project;
	boolean fIn_project;
	boolean flagDeleteAllJSDAIMarkers;
	boolean flagDeleteAllP21Markers;

	String fProject_comments;
	String fWorking_directory;
	String fOutput;
	private static boolean fUseIsolatedThread = true;

	String fValidateOutput;
	IWorkbenchPage fPage;

	
  public void dispose() {
    // TODO Auto-generated method stub
    
  }
  

  public void init(IWorkbenchWindow window) {
    // TODO Auto-generated method stub
	  
		//ExpressCompilerPlugin.getDefault().setExportToJarlAction(this);
//	  IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();		help.setHelp(fAction, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	  WorkbenchHelp.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ValidateContextId");
	  
//		help.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	 //   net.jsdai.express_compiler.actions.ExportToJarAction
  }

  public void selectionChanged(IAction action, ISelection selection) {
		fSelection = selection;
		fAction = action;
		fStepFile = null;
//		String java_directory = null;
		

		boolean enabled = false;
		try {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection sel = (IStructuredSelection) selection;
				List selResources = IDE.computeSelectedResources(sel);
				for (Iterator i = selResources.iterator(); i.hasNext();) {
					IResource resource = (IResource) i.next();
					if (resource instanceof IFile) {
						IFile file = (IFile) resource;
						String fileName = file.getName();
						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
							if(fStepFile == null) {
								fStepFile = file.getLocation().toOSString();
							} else {
								fStepFile = "";
							}
						}
					}
					fProject = resource.getProject();
					fIn_project = true;
					// true, if express project, 
					// if not - check working directory:
					// if it is defined, if it contains classes folder
					enabled = checkEnabledForSelectedProject();
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
						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
							if(fStepFile == null) {
								fStepFile = file.getLocation().toOSString();
							} else {
								fStepFile = "";
							}
						}
						fIn_project = true;
						fProject = file.getProject();
						enabled = checkEnabledForSelectedProject();
					}
				}
			}

		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			ExpressCompilerPlugin.log(e1);
//			ExpressCompilerPlugin.console(e1.toString());
//			System.out.println("ExportToJarAction - Exception when checking project nature:" + e1);
			e1.printStackTrace();
		}

		ExpressCompilerPlugin.getDefault().setExportToJarAction(action);
    
    // it is really enabled only if java directory is present - that indicates that express has been successfully compiled
    // it could be done in a different way - working only with ExpressCompilerRepo.sdai file, as it is the only input that is needed. 
		action.setEnabled(enabled);
    
  }

  public void run(IAction action) {

//System.out.println("<><01>Running validation");
    // TODO Auto-generated method stub

//	    IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();
//	    help.setHelp(action, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    help.setHelp(action, "net.jsdai.express_compiler.actions.ExportToJarAction" + ".ExportJarContextId");
//	    help.displayHelp(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    IContext context;	    



		flagDeleteAllJSDAIMarkers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_JSDAI_MARKERS);
		flagDeleteAllP21Markers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_P21_MARKERS);

//System.out.println("<><02>Running validation");
	    
		Shell shell = ExpressCompilerPlugin.getDefault().getShell();
	
	//  	Shell shell = null;
	  
		String path = null;
		path = fProject.getProject().getLocation().toOSString();


		if (fIn_express_project) {
//System.out.println("<><03>Running validation, in Express Project");
//		if (false) {
    	fDestination = fProject.getProject().getLocation().toOSString();
    	if (fDestination.endsWith("\\") || fDestination.endsWith("/")) {
	    	fDestination += fProject.getName() + ".jar";
    	} else {
	    	fDestination += File.separator + fProject.getName() + ".jar";
				
    	}
// System.out.println("here's jar: " + fDestination); 
// here's jar: H:\eclipse\runtime-EclipseApplication\expressions_schema\.jar
//System.out.println("<><03-2>Running validation - IF end");
		} else {
//System.out.println("<><04>Running validation, in Express Project");
			FileDialog dialog = new FileDialog(shell, SWT.SINGLE);
//		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
			dialog.setText("select the library jar");
	    dialog.setFilterPath(path);
	    dialog.setFileName(fProject.getName() + ".jar");
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
		 }

//System.out.println("<><03-3>Running validation, after Express Project check, fStepFile: " + fStepFile);

		if (fStepFile == null) {
			fStepFile = "";
		}
		
		if (fStepFile.equals("")) {
//System.out.println("<><05>Running validation");

		FileDialog dialog2 = new FileDialog(shell, SWT.SINGLE);
			dialog2.setText("select the part 21 file to validate");
//		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
	    dialog2.setFilterPath(path);
//	    dialog.setFileName(fProject.getName() + ".jar");
//	    dialog2.setFilterExtensions(new String[] {"*.stp","*.p21","*.pf" });
	    dialog2.setFilterExtensions(new String[] {"*.stp;*.p21;*.pf" });
	    if(fStepFile != null && fStepFile.length() != 0) {
	    	dialog2.setFilterPath(new File(fStepFile).getParent());
	    	dialog2.setFileName(new File(fStepFile).getName());
	    }
	    String name2 = dialog2.open();
			
			if (name2 == null) { // probably hit CANCEL
				return;
			}
			
//	    if (!name.endsWith(".jar")) {
//	    	name = name + ".jar";
//	    }
    	fStepFile = name2;
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
		memoryBox.setMessage("Validation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		memoryBox.setText("Out of memory error");
		try {
			(new ProgressMonitorDialog(null)).run(true, true, op);
		} catch (InterruptedException e) {
//			ExpressCompilerPlugin.log(e);
//System.out.println("Validation CANCELED: " + e);
			return;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
//			System.out.println("ValidateAction excption: " + e);
//			System.out.println("ValidateAction real excption: " + realException);
			ExpressCompilerPlugin.log(realException);
//			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
		}

		CommonUtils.printResultToConsole(fValidateOutput, fPage, fOutput);
//		ExpressCompilerUtils.printResultInConsole(fValidateOutput, fPage);

    
  }

	void doRun(IProgressMonitor monitor) throws CoreException, InterruptedException {

	
		monitor.beginTask("Validating a step file", IProgressMonitor.UNKNOWN);
		


    String current_directory;

	if (fIn_express_project) {	

		String s_temp_location = "_no_value_";
		try {
			s_temp_location = ExpressProjectTempPropertyPage.getTempFileLocation(fProject);
		} catch (CoreException e1) {
			ExpressCompilerPlugin.log(e1);
//			System.out.println("ValidateAction - Exception - Problems with reading of persistent properties: " + e1);
//			e1.printStackTrace();
		}
		current_directory = s_temp_location;


	} else {
		current_directory = fWorking_directory + File.separator + "temp";
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
			args = new String[5];
		} else {
			args = new String[7];
		}
//		args = new String[3];

		int ii = 0;

		if (!fUseIsolatedThread) {
			args[ii++] = "java";
			args[ii++] = "jsdai.util.Validate";
		}
		args[ii++] = fStepFile;
		args[ii++] = "-stdout";
		args[ii++] = new File(current_directory, "log_output").getAbsolutePath();
		args[ii++] = "-stderr";
		args[ii++] = new File(current_directory, "log_errors").getAbsolutePath();


		String env[] = null; 
		
		if(!fUseIsolatedThread) {
			String classpath_string = "";
			String cpath = ExpressCompilerUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ExpressCompilerUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
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

		fValidateOutput = "Running validation!\n";


		Runtime r = Runtime.getRuntime();

	try {
   	IsolatedRunnableThread validationThread = null;
	  monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);
	  boolean monitor_task_started = true;
		Process p = null;
		UtilMonitorImpl validationMonitor = new UtilMonitorImpl(Thread.currentThread());
		try {
			
	    	if(fUseIsolatedThread) {
	    		//V.N. Load express compiler using custom class loading
	    	 	File[] validationJars = {
	    				new File(ExpressCompilerUtils.getClassPath(
	    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
	    				new File(ExpressCompilerUtils.getClassPath(
	    						Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip")),
						new File(jar_path),
	    			};
	    	 	String sdaireposDirectory = new File(current_directory, "ExpressCompilerRepo").getCanonicalPath();
	    	 	validationThread =
	    	 		IsolatedRunnableThread.newInstance("jsdai.util.Validate", "initAsRunnable",
//	    	 				new Class[] { String.class, String[].class },
      	 				new Class[] { String.class, String[].class, UtilMonitor.class },
//	    	 				new Object[] { sdaireposDirectory, args },
      	 				new Object[] { sdaireposDirectory, args, validationMonitor },
	    	 				validationJars, 
	 				      new String[] { "jsdai/util/UtilMonitor.class", "com/lksoft/util/licensing/" });
//	    	 				null);
	    		validationThread.start();
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
							validationThread.join(1000);
						} catch (InterruptedException e) {
						}
						if(!validationThread.isAlive()) {
							if(validationThread != null) {
							validationThread.close();
						}
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
				synchronized (validationMonitor) {
					subtaskMessage = validationMonitor.getMessage();
					subtaskCount = validationMonitor.getCount();
					subtaskCounter = validationMonitor.getCounter();
					if (subtaskCounter > 0) {
					 subtaskCount -= subtaskCounter;
			    }
			    // ValidationPlugin.log("UtilMonitor - message: " + subtaskMessage + ", count: " + subtaskCount + ", counter: " + subtaskCounter, 1);
				}
				current_time = System.currentTimeMillis();
				elapsed_time = (current_time - start_time) / 1000;

	    if (monitor_task_started) {
					if (subtaskCount > 0) {
						if (monitor_reinitialized) {
						} else {
							monitor.beginTask("starting actual validation", subtaskCount);
							monitor_reinitialized = true;
						}
					}
					if (subtaskMessage != null) {
						monitor.setTaskName("Validating " + subtaskMessage);
						monitor.worked(subtaskCounter - monitor_count);
  					monitor_count = subtaskCounter;
					} else {
						// should not happen
 					  monitor.setTaskName("Reading the step file: " + elapsed_time + "s");
						monitor.worked(1);
					}

				} else {
					monitor.beginTask("Reading the step file: 0s", IProgressMonitor.UNKNOWN);
					monitor_task_started = true;
					System.out.println("starting task - delayed");
					monitor_count = 1;
				}


//				monitor.setTaskName((subtaskMessage != null ? subtaskMessage : monitor_exec_string) + ": " + elapsed_time + "s");
//				monitor.setTaskName(("Validating a step file: " + elapsed_time + " s"));
				is_cancel = monitor.isCanceled();
				if (is_cancel) {
					fValidateOutput += "Validation canceled";
					if(fUseIsolatedThread) {
						validationThread.interrupt();
					} else {
						p.destroy();
					}
					throw (new InterruptedException());
				}
			}		
		} // if true (OS)
		monitor.beginTask("Please wait, processing validation results", IProgressMonitor.UNKNOWN);

		error_stream_file = new File(error_file_path);
		output_stream_file = new File(output_file_path);
    InputStream error_stream = new FileInputStream(error_stream_file);
		InputStream stream = new FileInputStream(output_stream_file);
		String validateOutput = ExpressCompilerUtils.getStringFromStreamAvailable(stream);

    String validateProcessErrors = ExpressCompilerUtils.getStringFromStreamAvailable(error_stream);

		stream.close();

		error_stream.close();

		
		monitor.worked(1);

			fValidateOutput += "--- Validation process errors, if present ---\n\n";
			fValidateOutput += validateProcessErrors;
			fValidateOutput += "--- Validation Results ---\n\n";
			fValidateOutput += validateOutput;
			fValidateOutput += "\n--- End of Validation Results ---\n";

			createMarkers(validateOutput, fStepFile, monitor);

		} finally {
			Thread.interrupted();
		if(validationThread != null) {
				validationThread.close();
			}
		}
	} catch (IOException e) {
		ExpressCompilerPlugin.log(e);
		e.printStackTrace();
	}

	//monitor.worked(1);





			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);

	
	}


	
	boolean isExternalExpressFileOpen() {

		
		

		ITextEditor editor = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench.getActiveWorkbenchWindow() != null) {
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if( page != null) {
				IEditorPart currentEditor = page.getActiveEditor();
				if (currentEditor != null) {
					if (currentEditor instanceof ITextEditor) {
						editor = (ITextEditor)currentEditor;
					}
				}		
			}
		}
       if (editor == null) {
    	   return false;
       }
		
		
		
		
		
		
		
		
		
		IEditorInput editorInput = null;
		if (editor != null) {
			editorInput = editor.getEditorInput();
		}

		if (editorInput instanceof IFileEditorInput) {
			// go select project instead, it is not external file
			return false;
			//			return ((IFileEditorInput) editorInput).getFile();
		} else
		if (editorInput instanceof IPathEditorInput) {
			IPath the_path = ((IPathEditorInput) editorInput).getPath();
			String path_str = the_path.toString();
			if (path_str.endsWith(".exp") || path_str.endsWith(".EXP")) {
				return true;
			} else {
				return false;
				// System.out.println("EDITOR INPUT: " + editorInput);
			}
		// if nothing was found, perhaps an external file not a resource in the workspace
		}
		return false;
	}
	
	
//	protected void createMarkers(String output, String step_file_str) throws CoreException {
	protected void createMarkers(String output, String step_file_str, IProgressMonitor monitor) throws CoreException {

//		monitor.setTaskName(("Creating Markers - 01"));
//		System.out.println("<<Creating Markers>>");
		
		
		IWorkspace workspace = fProject.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath step_file_path = new Path(step_file_str);
		IFile step_files[] = root.findFilesForLocation(step_file_path);

		String project_name = fProject.getName();
		// deleting old markers --------------------

// for subtyped markers, abandoned	
/*		
			// remove all JSDAI Problem markers, from all the projects
		IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.jsdaiproblem", true, IResource.DEPTH_INFINITE);
			
		for (int i = 0; i < xproblems.length; i++) {
			IMarker marker = xproblems[i];
//System.out.println("<<!!!>> found a marker");
			if (marker.exists()) { // this check may not be needed
//System.out.println("<<!!!>> found an existing marker, deleting");
				if (flagDeleteAllJSDAIMarkers) {
					marker.delete();
				} else
						if (marker.getType().equals("net.jsdai.express_compiler.p21problem")) {
							if (flagDeleteAllP21Markers) {
								marker.delete();
							} else {
								// only p21 markers for this project
								String project_attribute = marker.getAttribute("project", "");
								if (project_name.equals(project_attribute)) {
									marker.delete();
								}
							}
						} else
						if (marker.getType().equals("net.jsdai.express_compiler.expressproblem")) {
							// currently we do not have such a flag which would allow to remove, for example,
							// both express and p21 markers set from a single but the same express project only
							// express markers are removed when running validation, only if delete all JSDAI markers flag is set to true
							// perhaps such a combination could be added, it could work well with selected resource and children filter
						} else {
							// should not happen
						}
					}				
			}
		
*/

//		monitor.setTaskName(("Creating Markers - 02"));
		
//			IMarker[] p21problems = root.findMarkers("net.jsdai.express_compiler.p21problem", true, IResource.DEPTH_INFINITE);
			IMarker[] p21problems = root.findMarkers("net.jsdai.step_editor.p21problem", true, IResource.DEPTH_INFINITE);

//			monitor.setTaskName(("Creating Markers - 03"));
			
			monitor.beginTask("Deleting old error markers", p21problems.length);
			for (int i = 0; i < p21problems.length; i++) {
				monitor.worked(1);
//				  monitor.setTaskName("Deleting old error markers: " + i);
					IMarker marker = p21problems[i];
//System.out.println("<<!!!>> found a marker");
					if (marker.exists()) { // this check may not be needed
//System.out.println("<<!!!>> found an existing marker, deleting");
						if (flagDeleteAllJSDAIMarkers || flagDeleteAllP21Markers) {
							marker.delete();
						} else {
							// only express markers for this project
							String project_attribute = marker.getAttribute("project", "");
							if (project_name.equals(project_attribute)) {
								marker.delete();
							}
						}				
					}
			} // for

//			monitor.setTaskName(("Creating Markers - 04"));
			
			
			if (flagDeleteAllJSDAIMarkers) {
				// delete also all p21 problem markers
//				IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.expressproblem", true, IResource.DEPTH_INFINITE);
				IMarker[] xproblems = root.findMarkers("net.jsdai.step_editor.expressproblem", true, IResource.DEPTH_INFINITE);
				monitor.beginTask("Deleting other jsdai error markers", xproblems.length);
				for (int i = 0; i < xproblems.length; i++) {
					monitor.worked(1);
					IMarker marker = xproblems[i];
					if (marker.exists()) { // this check may not be needed
						marker.delete();
					}
				}
			}



//			monitor.setTaskName(("Creating Markers - 05"));


/*

		// deleting all p21 and express markers
		IMarker[] p21problems = root.findMarkers("net.jsdai.express_compiler.p21problem", true, IResource.DEPTH_INFINITE);
		for (int i = 0; i < p21problems.length; i++) {
			IMarker marker = p21problems[i];
			if (marker.exists()) { // this check may not be needed
				marker.delete();
			}				
		}
		// let's delete also express markers
		IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.expressproblem", true, IResource.DEPTH_INFINITE);
		for (int i = 0; i < xproblems.length; i++) {
			IMarker marker = xproblems[i];
			if (marker.exists()) { // this check may not be needed
				marker.delete();
			}				
		}
		
*/


		for (int i = 0; i < step_files.length; i++) {
			// step_files[i].deleteMarkers(IMarker.PROBLEM, false, 0);
//			(new ValidationMessageParser()).parseValidationMessages(fProject, step_files[i], output, null);
			(new ValidationMessageParser()).parseValidationMessages(fProject, step_files[i], output, null, monitor);
		}
//		monitor.setTaskName(("Creating Markers - 06"));

	
	}
		
	private boolean checkEnabledForSelectedProject() throws CoreException {
//System.out.println("enabling-disabling, project: " + fProject);
		if (fProject.isAccessible() && fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
				fIn_express_project = true;
				return fProject.getFile(fProject.getName() + ".jar").exists();
		} else {
			// non-express project
			// checking working directory
			boolean enabled = false;
			fIn_express_project = false;
			fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
			if (fWorking_directory == null ) {
				enabled = false;
			} else {
//				String classes_str = fWorking_directory + File.separator + "classes";
				String java_str = fWorking_directory + File.separator + "java";
//				File classes_dir = new File(classes_str);
				File java_dir = new File(java_str);
//				if (classes_dir.exists()) {
				if (java_dir.exists()) {
					enabled = true;
				} else {
					enabled = false;
				}
				// check if classes already present
			}
			return enabled;
		}
	}

	private boolean checkEnabledForSelectedProject2() throws CoreException {
//System.out.println("enabling-disabling, project: " + fProject);
//		if (fProject.isAccessible() && fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
		if (fProject.isAccessible()) {
			if (fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
				fIn_express_project = true;
			} else {
				fIn_express_project = false;
				fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
				if (fWorking_directory == null ) {
					return false;
				}
			}
//			return fProject.getFile(fProject.getName() + ".jar").exists();
			return true;
		} else {
			// non-express project
			// checking working directory
			boolean enabled = false;
			fIn_express_project = false;
			fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
			if (fWorking_directory == null ) {
				enabled = false;
			} else {
//				String classes_str = fWorking_directory + File.separator + "classes";
				String java_str = fWorking_directory + File.separator + "java";
//				File classes_dir = new File(classes_str);
				File java_dir = new File(java_str);
//				if (classes_dir.exists()) {
				if (java_dir.exists()) {
					enabled = true;
				} else {
					enabled = false;
				}
				// check if classes already present
			}
			return enabled;
		}
	}

/*

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

*/

}

