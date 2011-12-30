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


package jsdai.xim.validation.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import jsdai.util.UtilMonitor;
import jsdai.apache.ApachePlugin;
import jsdai.common.utils.CommonUtils;
import jsdai.common.utils.UtilMonitorImpl;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdai.runtime.RuntimePlugin;
import jsdai.xim.validation.ValidationPlugin;
import jsdai.xim.validation.utils.IsolatedRunnableThread;
import jsdai.xim.validation.utils.ValidationUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;

import jsdaix.processor.xim_aim.pre.Importer;


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

public class ValidateBgAction extends Action implements IWorkbenchWindowActionDelegate {


  IncrementalValidationMessageParser fParser = null;
	IFile fFile = null;
	String fDestination;
	String fStepFile;
	String fXimFile;
	ISelection fSelection;
	IAction fAction;
	IProject fProject;
	boolean fIn_express_project;
	boolean fIn_project;
	boolean flagDeleteAllJSDAIMarkers;
	boolean flagDeleteAllP21Markers;
	int fValidationLength = 0;
	int fValidationLengthPrev = 0;
	

  boolean flag_in_express_project = false;
  boolean flag_mim_xim = true;
  boolean flag_mim = true;
  boolean flag_xim = false;
  boolean flag_2nd_validation = false;
  int fValidationCount = 0;
	String fLog_out;
	String fLog_err;
	String fLog_out_xim;
	String fLog_err_xim;

	String fProject_comments;
	String fWorking_directory;
	String fOutput;
	private static boolean fUseIsolatedThread = true;

	String fValidateOutput;
	String fValidateOutput_xim;
	IWorkbenchPage fPage;

	MessageBox fErrorBox;
	Shell fShell;
	SdaiException fSdaiException;
	int flag_exception = 0;
	

//	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		Job job = new Job("STEP Validation") {
//		    @Override
		    protected IStatus run(IProgressMonitor monitor) {


		String path = null;
		flag_in_express_project = true;
		flag_mim_xim = false;
		flag_mim = false;
		flag_xim = false;
  	flag_2nd_validation = false;

    // let's try to change the flags above for general validation as opposed to mim-xim validation
    // how to deterimene it?
    
    // this is a simple temporary way:
    // 1. if the name of the step file contains "mim" or "xim"  - then mim-xim validation
    // 2. if not 1, but if the fProject != 0, then if the name of the project contains "mim" or "xim" - then mim-xim validation
    // 3. all the rest - a regular validation

    // 

		if (fProject != null) {
    	String library_jar = fProject.getProject().getLocation().toOSString();
    	if (library_jar.endsWith("\\") || library_jar.endsWith("/")) {
	    	library_jar += fProject.getName() + ".jar";
    	} else {
	    	library_jar += File.separator + fProject.getName() + ".jar";
    	}
				File fff = new File(library_jar);
				if (fff.isFile()) {  // single validation
					flag_in_express_project = true;
					flag_mim_xim = false;
					flag_mim = false;
					flag_xim = false;
			  	flag_2nd_validation = false;
				} else { // mim+xim validation using the library in a separate plugin
					flag_in_express_project = false;
					flag_mim_xim = true;
					flag_mim = true; 
					flag_xim = false;
			  	flag_2nd_validation = false;
				}
		} else { // when fProject == null probably it makes no sense to proceed anyway

	 		if (fStepFile != null) {

					if ((fStepFile.toUpperCase().contains("MIM")) || (fStepFile.toUpperCase().contains("XIM"))) {
						flag_in_express_project = false;
						flag_mim_xim = true;
						flag_mim = true; 
						flag_xim = false;
				  	flag_2nd_validation = false;
					} else
					if (fProject != null) {
						if ((fProject.getName().toUpperCase().contains("MIM")) || (fProject.getName().toUpperCase().contains("MIM"))) {
							flag_in_express_project = false;
							flag_mim_xim = true;
							flag_mim = true; 
							flag_xim = false;
					  	flag_2nd_validation = false;
						} 
					}

			}
		
		}


		if (flag_in_express_project) {

    	fDestination = fProject.getProject().getLocation().toOSString();
    	if (fDestination.endsWith("\\") || fDestination.endsWith("/")) {
	    	fDestination += fProject.getName() + ".jar";
    	} else {
	    	fDestination += File.separator + fProject.getName() + ".jar";
				
    	}
//System.out.println("lib - EXPRESS PROJECT: " + fDestination);

		}
    else if (flag_mim_xim) {
      	// use jsdai_xim_full.jar in net.jsdai.xim.jlib plugin	

			fDestination = CommonUtils.getClassPath(Platform.getBundle("net.jsdai.xim.jlib"),"jsdai_xim_full.jar");

//System.out.println("lib - XIM-MIM: " + fDestination);
		} else {
		}

		if (fStepFile == null) {
			fStepFile = "";
		}
        fValidationCount = 0; 

        IEditorRegistry edreg = PlatformUI.getWorkbench().getEditorRegistry();
        IEditorDescriptor eddes0 = edreg.getDefaultEditor("*.stp");
        String edlabel0 = eddes0.getLabel();
// PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.express_compiler.p21_editor.P21Editor");
 PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.step_editor.editor.P21Editor");
//       IEditorDescriptor eddes = edreg.findEditor("net.jsdai.express_compiler.p21_editor.P21Editor");
       IEditorDescriptor eddes = edreg.findEditor("net.jsdai.step_editor.editor.P21Editor");
       String edlabel = eddes.getLabel();
       IEditorDescriptor eddes2 = edreg.getDefaultEditor("*.stp");
       String edlabel2 = eddes2.getLabel();
//System.out.println("<<>><<OOOOOOOOOOOOO>>>>>>label: " + edlabel);       
//System.out.println("<<>><<OOOOOOOOOOOOO>>>>>>DEFAULT label BEFORE: " + edlabel0);       
//System.out.println("<<>><<OOOOOOOOOOOOO>>>>>>DEFAULT label AFTER: " + edlabel2);       

		        monitor.beginTask("Validating " + fStepFile, IProgressMonitor.UNKNOWN);
						try {
							doRunValidation(monitor);
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							ValidationPlugin.log(e);
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							ValidationPlugin.log(e);
							e.printStackTrace();
						}
		        /*
		        for (int i = 0; i < 100; i++) {
		            try {
		                Thread.sleep(200);
		            } catch (InterruptedException e) {} // ignore
		            monitor.worked(1);
		        }
		        */
		        monitor.done();
				CommonUtils.printResultToConsole(fValidateOutput, fPage, fOutput);

		    flag_exception = 0;

if (flag_mim_xim) {
				
				try {
					doRunMim2Xim(monitor);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					ValidationPlugin.log(e);
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					ValidationPlugin.log(e);
					e.printStackTrace();
				}

    flag_xim = true;
    flag_mim = false;
    fValidationCount = 1;
  	flag_2nd_validation = true;  // this is really not needed

					try {
						doRunValidation(monitor);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						ValidationPlugin.log(e);
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						ValidationPlugin.log(e);
						e.printStackTrace();
					}

	if (flag_xim)
		CommonUtils.printResultToConsole(fValidateOutput_xim, fPage, fOutput);

} // if mim-xim validation

		        return new Status(IStatus.OK, ValidationPlugin.VALIDATION_PLUGIN_ID, "Job finished");
		    }

		};
		job.setUser(true);
		job.schedule();

		
	}

//	@Override
		  public void selectionChanged(IAction action, ISelection selection) {
				fSelection = selection;
				fAction = action;
				fStepFile = null;
				fXimFile = null;
//				String java_directory = null;
				

				boolean enabled = true;
//				try {
					if (selection instanceof IStructuredSelection) {

//		System.out.println("in structured selection - ############");


						IStructuredSelection sel = (IStructuredSelection) selection;
						List selResources = IDE.computeSelectedResources(sel);
						for (Iterator i = selResources.iterator(); i.hasNext();) {
							IResource resource = (IResource) i.next();
							if (resource instanceof IFile) {
								IFile file = (IFile) resource;
								fFile = file;
								String fileName = file.getName();
								if(fileName.endsWith(".p21") || fileName.endsWith(".stp")  || fileName.endsWith(".step") || fileName.endsWith(".pf")) {
									if(fStepFile == null) {
										fStepFile = file.getLocation().toOSString();
										fXimFile = fStepFile.substring(0,fStepFile.lastIndexOf(".")) + "_XIM.stp";

									} else {
										fStepFile = "";
										fXimFile = "";
									}
								}
							}

							fProject = resource.getProject();
							fIn_project = true;
							// let's change the xim step file name to a fixed one:

              // no, let's don't - commenting out:
							// fXimFile = fProject.getLocation().toString()	+ File.separator + "_temporary_xim.pf"; 				

		/*		
							// true, if express project, 
							// if not - check working directory:
							// if it is defined, if it contains classes folder
							enabled = checkEnabledForSelectedProject();
		*/
//						enabled = checkEnabledForSelectedProject();
		
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
								if(fileName.endsWith(".p21") || fileName.endsWith(".stp")  || fileName.endsWith(".step") || fileName.endsWith(".pf")) {
									if(fStepFile == null) {
										fStepFile = file.getLocation().toOSString();
										fXimFile = fStepFile.substring(0,fStepFile.lastIndexOf(".")) + "_XIM.stp";
									} else {
										fStepFile = "";
										fXimFile = "";
									}
								}
//								fIn_project = true;
//								fProject = file.getProject();
//								enabled = checkEnabledForSelectedProject();
							}
						}
					}

//				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					//ValidationPlugin.log(e1);
//					ExpressCompilerPlugin.console(e1.toString());
//					System.out.println("ExportToJarAction - Exception when checking project nature:" + e1);

					//e1.printStackTrace();
//				}

//				ExpressCompilerPlugin.getDefault().setExportToJarAction(action);
		    
		    // it is really enabled only if java directory is present - that indicates that express has been successfully compiled
		    // it could be done in a different way - working only with ExpressCompilerRepo.sdai file, as it is the only input that is needed. 
				action.setEnabled(enabled);
		    
		  }


	void doRunValidation(IProgressMonitor monitor) throws CoreException, InterruptedException {

	 if (!flag_mim_xim && fValidationCount > 0) return; 
		
//		monitor.beginTask("Validating a step file", IProgressMonitor.UNKNOWN);
		


    String current_directory;


// it seems that we do not need a separate processing for non mim-xim conversion, the same for both:
// if (fIn_express_project) {	
if (false) {	

		String s_temp_location = "_no_value_";

//		try {
//			s_temp_location = ExpressProjectTempPropertyPage.getTempFileLocation(fProject);
			s_temp_location = ValidationUtils.getSystemTempDirectoryString();
//		} catch (CoreException e1) {
//			ValidationPlugin.log(e1);
//			System.out.println("ValidateAction - Exception - Problems with reading of persistent properties: " + e1);
//			e1.printStackTrace();
//		}
		current_directory = s_temp_location;
		
		// do this here for now:
		fWorking_directory = current_directory;


	} else {
		String s_temp_location = ValidationUtils.getSystemTempDirectoryString();
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


		String monitor_exec_string;
		if (flag_mim) 
			monitor_exec_string = "Reading the MIM step file";
		else if (flag_xim)
			monitor_exec_string = "Reading the XIM step file";
		else
			monitor_exec_string = "Reading the step file";

		
		
		String jar_path = fDestination;

		String output_file_path = current_directory; 
		String error_file_path =  current_directory;
				
		if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
    	if (flag_xim) {
					output_file_path  += "log_output_xim";
					error_file_path   += "log_errors_xim";
    	} else {	
					output_file_path  += "log_output";
					error_file_path   += "log_errors";
			}
		} else {
			if (flag_xim) {
					output_file_path  += File.separator + "log_output_xim";
					error_file_path   += File.separator + "log_errors_xim";
			} else {
					output_file_path  += File.separator + "log_output";
					error_file_path   += File.separator + "log_errors";
			}
		}

//    fOutput = output_file_path;
    
    // hm, above stuff now could be eleminated
    
    if (flag_xim) {
	    fLog_out_xim = new File(current_directory, "log_output_xim").getAbsolutePath();
  	  fLog_err_xim = new File(current_directory, "log_errors_xim").getAbsolutePath();
    } else {
	    fLog_out = new File(current_directory, "log_output").getAbsolutePath();
  	  fLog_err = new File(current_directory, "log_errors").getAbsolutePath();
	  }

    if (flag_xim)
    	fOutput = fLog_out_xim;
    else
    	fOutput = fLog_out;

    
//System.out.println("output: " + output_file_path);
//System.out.println("errors: " + error_file_path);

		String [] args;	
		if (fUseIsolatedThread) {
			args = new String[6];
		} else {
			args = new String[8];
		}
//		args = new String[3];

		int ii = 0;

		if (!fUseIsolatedThread) {
			args[ii++] = "java";
			args[ii++] = "jsdai.util.Validate";
		}
		if (flag_xim)
			args[ii++] = fXimFile;
		else
			args[ii++] = fStepFile;
		args[ii++] = "-stdout";
//		args[ii++] = new File(current_directory, "log_output").getAbsolutePath();
		if (flag_xim)
			args[ii++] = fLog_out_xim;
		else
			args[ii++] = fLog_out;
//		args[ii++] = new File(current_directory, "log_errors").getAbsolutePath();
		args[ii++] = "-stderr";
		if (flag_xim)
			args[ii++] = fLog_err_xim;
		else 
			args[ii++] = fLog_err;
		if (flag_mim)
			args[ii++] = "-MIM";
		else if (flag_xim)
			args[ii++] = "-XIM";
		else
			args[ii++] = "-dummy";

		String env[] = null; 
		
		if(!fUseIsolatedThread) {
			String classpath_string = "";
			String cpath = ValidationUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ValidationUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
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

		if (flag_mim)
			fValidateOutput = "Running MIM validation!\n";
		else if (flag_xim)
			fValidateOutput_xim = "Running XIM validation!\n";
		else
			fValidateOutput = "Running validation!\n";


			if (flag_xim)
				deleteMarkers(fXimFile, monitor);
			else
				deleteMarkers(fStepFile, monitor);

		InputStream istream = null;
        output_stream_file = null;
    	long fValidationLength = 0;
    	long fValidationLengthPrev = 0;

		Runtime r = Runtime.getRuntime();



	try {
    IsolatedRunnableThread validationThread = null;
		Process p = null;

	  boolean monitor_task_started = true;
	
	  // an experiment to see if it is possible to move this inside the loop
	  // yes, it is possible
	  monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);
		//	monitor.beginTask(monitor_exec_string, 10);

		UtilMonitorImpl validationMonitor = new UtilMonitorImpl(Thread.currentThread());
		try {
			
	    	if(fUseIsolatedThread) {
	    		//V.N. Load express compiler using custom class loading
	    	 	File[] validationJars = {
	    				new File(ValidationUtils.getClassPath(
	    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
	    				new File(ValidationUtils.getClassPath(
	    						Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip")),
						new File(jar_path),
	    			};
	    	 	String sdaireposDirectory = new File(current_directory, "ExpressCompilerRepo").getCanonicalPath();
	    	 	validationThread =
	    	 		IsolatedRunnableThread.newInstance("jsdai.util.Validate", "initAsRunnable",
      	 				new Class[] { String.class, String[].class, UtilMonitor.class },
	    	 				// new Class[] { String.class, String[].class },
//	    	 				new Object[] { sdaireposDirectory, args },
      	 				new Object[] { sdaireposDirectory, args, validationMonitor },
	    	 				validationJars, 
	 				      new String[] { "jsdai/util/UtilMonitor.class", "com/lksoft/util/licensing/" });
	    	 				// null);
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
			fParser = new IncrementalValidationMessageParser();
			fParser.clearAllFlags();
//			fParser.parseViolationLine("--- Reading time=55555", fFile);
//		ValidationPlugin.log("################## SETTING MARKERS THE NEW WAY ###################",1);
			
			for (;;) {
				if(!Thread.interrupted()) {
					if(fUseIsolatedThread) {
						try {
							validationThread.join(1000);
						} catch (InterruptedException e) {
						}
						if(!validationThread.isAlive()) {
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
				// ValidationPlugin.log("Subtask in the loop",1);
				String subtaskMessage;
				int subtaskCount;
				int subtaskCounter;
//				BlockingQueue subtaskQueue;
				List subtaskQueue;
				synchronized (validationMonitor) {
					subtaskMessage = validationMonitor.getMessage();
					subtaskCount = validationMonitor.getCount();
					subtaskCounter = validationMonitor.getCounter();
					subtaskQueue = validationMonitor.getQueue();
					if (subtaskCounter > 0) {
					 subtaskCount -= subtaskCounter;
			    }
				}


				if (flag_2nd_validation) {
					IWorkspace workspace= ResourcesPlugin.getWorkspace(); 
					 IPath location= Path.fromOSString(fXimFile); 
					 fFile= workspace.getRoot().getFileForLocation(location);										
				}

				if (subtaskQueue != null) {
					//ValidationPlugin.log("Queue: " + subtaskQueue.toString(),1);
				/*	
					Queue violations = new LinkedList();
					int violation_ln_nr = subtaskQueue.drainTo(violations);
					for (;;) {
						String a_violation_line = (String)violations.poll();
						if (a_violation_line != null) {
							// invoke the incremental error parser
				//ValidationPlugin.log("Parsing line: " + a_violation_line,1);
							fParser.parseViolationLine(a_violation_line, fFile);
						} else {
							break;
						}
					}
				*/
					while (!subtaskQueue.isEmpty()) {
						String a_violation_line = (String)subtaskQueue.remove(0);
						if (a_violation_line != null) {
				// ValidationPlugin.log("Parsing INPUT line: " + a_violation_line,1);
							fParser.parseViolationLine(a_violation_line, fFile);
						}
						// if (!subtaskQueue.isEmpty()) {
						//	subtaskQueue.remove(0);
						// }
					}
				}
				current_time = System.currentTimeMillis();
				elapsed_time = (current_time - start_time) / 1000;
			
        // let's try parsing violations while still validating
        // if (output_stream_file == null) {
        //	output_stream_file = new File(output_file_path);
				//	istream = new FileInputStream(output_stream_file);
				//}
				// ParseViolationsIncrementally(istream, fStepFile, monitor);
      
	    if (monitor_task_started) {
					if (subtaskCount > 0) {
						if (monitor_reinitialized) {
						} else {
							if (flag_mim)
								monitor.beginTask("starting actual MIM validation", subtaskCount);
							else if (flag_xim) 
								monitor.beginTask("starting actual XIM validation", subtaskCount);
							else 
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
	 					if (flag_mim)  
	 						monitor.setTaskName("Reading the MIM step file: " + elapsed_time + "s");
	 					if (flag_xim)
	 						monitor.setTaskName("Reading the XIM step file: " + elapsed_time + "s");
	 					else 
	 						monitor.setTaskName("Reading the step file: " + elapsed_time + "s");
						monitor.worked(1);
					}

				} else {
					if (flag_mim)
						monitor.beginTask("Reading the MIM step file: 0s", IProgressMonitor.UNKNOWN);
					else if (flag_xim)
						monitor.beginTask("Reading the XIM step file: 0s", IProgressMonitor.UNKNOWN);
					else
						monitor.beginTask("Reading the step file: 0s", IProgressMonitor.UNKNOWN);
					monitor_task_started = true;
					System.out.println("starting task - delayed");
					monitor_count = 1;
				}
				is_cancel = monitor.isCanceled();
				if (is_cancel) {
					if (flag_mim)
						fValidateOutput += "MIM Validation canceled";
					else if (flag_xim)
						fValidateOutput += "XIM Validation canceled";
					else
						fValidateOutput += "Validation canceled";
					if(fUseIsolatedThread) {
						validationThread.interrupt();
					} else {
						p.destroy();
					}
				//	throw (new InterruptedException());
				//	return Status.CANCEL_STATUS;
					return;
				}
			}		
		} // if true (OS)


        // ParseViolationsIncrementally(istream, fStepFile, monitor);
        // istream.close();

        // if (error_stream_file != null) error_stream_file.close();
        // if (output_stream_file != null) output_stream_file.close();
        
				error_stream_file = new File(error_file_path);
				output_stream_file = new File(output_file_path);
   	    InputStream error_stream = new FileInputStream(error_stream_file);
				InputStream stream = new FileInputStream(output_stream_file);


//   	    InputStream error_stream = p.getErrorStream();
//				InputStream stream = p.getInputStream();



System.out.println("Getting output string");
					if (flag_mim)
						monitor.beginTask("Getting MIM validation output", IProgressMonitor.UNKNOWN);
					else if (flag_xim)
						monitor.beginTask("Getting XIM validation output", IProgressMonitor.UNKNOWN);
					else
						monitor.beginTask("Getting validation output", IProgressMonitor.UNKNOWN);
//					monitor.setTaskName("getting validation output");

				String validateOutput = ValidationUtils.getStringFromStreamAvailable(stream);

System.out.println("Got output string: " + validateOutput);
					if (flag_mim)
						monitor.setTaskName("got MIM validation output");
					else if (flag_xim)
						monitor.setTaskName("got XIM validation output");
					else 
						monitor.setTaskName("got validation output");
		
		//		String validateOutput = ExpressCompilerUtils.getStringFromStream(stream);
//		System.out.println("Getting error string");

		
		String validateProcessErrors = ValidationUtils.getStringFromStreamAvailable(error_stream);
		if (flag_mim)      
			monitor.setTaskName("got MIM validation errors");
		else if (flag_xim)
			monitor.setTaskName("got XIM validation errors");
		else
			monitor.setTaskName("got validation errors");

		
//	    String validateProcessErrors = ExpressCompilerUtils.getStringFromStream(error_stream);
//	    System.out.println("Errors: " + validateProcessErrors);
//		System.out.println("Output: " + validateOutput);			
		stream.close();
					monitor.setTaskName("output stream closed");
		error_stream.close();
					monitor.setTaskName("error stream closed");

	monitor.worked(1);

// we could calculate sizes now and make cuts before adding out+err, so that err part is not cut out
// but perhaps we can cut far enought from the end to make sure and don't bother
// because after mim validation it might be difficult to tell how much to cut, not knowing the size of xim validation to come
// so at least for now we are doing nothing
// we will probably dump the whole issue to the console output method in utils

			if (flag_mim)
				fValidateOutput += "--- MIM Validation process errors, if present ---\n\n";
			else if (flag_xim)
				fValidateOutput_xim += "--- XIM Validation process errors, if present ---\n\n";
			else
				fValidateOutput += "--- Validation process errors, if present ---\n\n";
			if (flag_xim)
				fValidateOutput_xim += validateProcessErrors;
			else
				fValidateOutput += validateProcessErrors;
			if (flag_mim)
				fValidateOutput += "--- MIM Validation Results ---\n\n";
			else if (flag_xim)
				fValidateOutput_xim += "--- XIM Validation Results ---\n\n";
			else 
				fValidateOutput += "--- Validation Results ---\n\n";
			if (flag_xim)
				fValidateOutput_xim += validateOutput;
			else
				fValidateOutput += validateOutput;
			if (flag_mim)
				fValidateOutput += "\n--- End of MIM Validation Results ---\n";
			else if (flag_xim)
				fValidateOutput_xim += "\n--- End of XIM Validation Results ---\n";
			else
				fValidateOutput += "\n--- End of Validation Results ---\n";

			monitor.setTaskName("fValidateOutput size: " + fValidateOutput.length());

//			createMarkers(validateOutput, fStepFile);

//		ValidationPlugin.log("################## SETTING MARKERS THE OLD WAY ###################",1);

/*
			if (flag_xim)
				createImportMarkers(validateOutput, fXimFile, monitor);
			else
				createImportMarkers(validateOutput, fStepFile, monitor);
*/
			
		} catch (Exception e1) {
			ValidationPlugin.log(e1);
			e1.printStackTrace();
		} finally {
			Thread.interrupted();
			if(validationThread != null) {
				validationThread.close();
			}
		}
	} catch (IOException e) {
		ValidationPlugin.log(e);
		e.printStackTrace();
	}

	monitor.worked(1);





//			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);

	
	}

	void doRunMim2Xim(IProgressMonitor monitor) throws CoreException, InterruptedException {
	  String monitor_exec_string = "MIM to XIM conversion";
	  monitor.beginTask(monitor_exec_string + " ", IProgressMonitor.UNKNOWN);

		File xim_file = new File(fXimFile);
		xim_file.delete();
    String sdaireposDirectory = fWorking_directory; // should be created by the previous mim validation

		// String stepFileName = args[0];

		//###############################################################################

		String jar_path = fDestination;
//System.out.println("<CONVERSION> - jar_path: " + jar_path);		
    String current_directory;
		String s_temp_location = ValidationUtils.getSystemTempDirectoryString();
		fWorking_directory = s_temp_location;
        
		new File(fWorking_directory).mkdirs();
		
		if (fWorking_directory.endsWith("\\") || fWorking_directory.endsWith("/")) {
			current_directory = fWorking_directory + "temp";
		} else {
			current_directory = fWorking_directory + File.separator + "temp";
		}
		new File(current_directory).mkdirs();
		
    //............................

		String [] args;	
		if (fUseIsolatedThread) {
			args = new String[1];
		} else {
			args = new String[3];
		}

		int ii = 0;


    fUseIsolatedThread = true;
		if (!fUseIsolatedThread) {
			args[ii++] = "java";
			args[ii++] = "jsdaix.processor.xim_aim.pre.Importer";
		}
		args[ii++] = fStepFile;

		String env[] = null; 
		
		if(!fUseIsolatedThread) {
			String classpath_string = "";
			String cpath = ValidationUtils.getClassPath(Platform.getBundle("net.jsdai.xim.aim_processor.base"), "jsdaix_xim_util.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ValidationUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ValidationUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ValidationUtils.getClassPath(ApachePlugin.getDefault().getBundle(),"commons-collections.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ValidationUtils.getClassPath(ApachePlugin.getDefault().getBundle(),"commons-lang-1.0.1.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = jar_path;
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			String new_classpath = "." + File.pathSeparator + classpath_string;
			
			env = new String[2]; 
			env[0] = "jsdai.properties=" + current_directory;
			env[1] = "CLASSPATH=" + new_classpath;
		}

		Runtime r = Runtime.getRuntime();



	try {
    IsolatedRunnableThread conversionThread = null;
		Process p = null;

	
		UtilMonitorImpl conversionMonitor = new UtilMonitorImpl(Thread.currentThread());
		
		try {
			
	    	if(fUseIsolatedThread) {
	    		//V.N. Load express compiler using custom class loading
	    	 	File[] conversionJars = {

					    new File(ValidationUtils.getClassPath(
					    		Platform.getBundle("net.jsdai.xim.aim_processor.base"), "jsdaix_xim_util.jar")),
	    				new File(ValidationUtils.getClassPath(
	    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
	    				//new File(ValidationUtils.getClassPath(
	    				//		Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip")),
						  new File(ValidationUtils.getClassPath(
						  		ApachePlugin.getDefault().getBundle(),"commons-collections.jar")),
						  new File(ValidationUtils.getClassPath(
						  		ApachePlugin.getDefault().getBundle(),"commons-lang-1.0.1.jar")),
						  new File(jar_path),
	    			};
	    	 	//String sdaireposDirectory = new File(current_directory, "ExpressCompilerRepo").getCanonicalPath();
	    	 	conversionThread =
	    	 		IsolatedRunnableThread.newInstance("jsdaix.processor.xim_aim.pre.Importer", "initAsRunnable",
      	 				new Class[] { String.class, String[].class, UtilMonitor.class },
      	 				//new Class[] { String.class, String[].class, null },
      	 				new Object[] { sdaireposDirectory, args, conversionMonitor },
      	 				//new Object[] { sdaireposDirectory, args, null },
	    	 				conversionJars, 
	 				      new String[] { "jsdai/util/UtilMonitor.class", "com/lksoft/util/licensing/" });
	    	 				// null);
	    		conversionThread.start();
	    	} else {
	    		p = r.exec(args, env, new File(current_directory));
	    	}

		if (true) {

			boolean is_cancel = false;
			
			for (;;) {
				if(!Thread.interrupted()) {
					if(fUseIsolatedThread) {
						try {
							conversionThread.join(1000);
						} catch (InterruptedException e) {
						}
						if(!conversionThread.isAlive()) {
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

				is_cancel = monitor.isCanceled();
				if (is_cancel) {
					if(fUseIsolatedThread) {
						conversionThread.interrupt();
					} else {
						p.destroy();
					}
					return;
				}
			}		
		} // if true (OS)


			
		} catch (Exception e1) {
			ValidationPlugin.log(e1);
			e1.printStackTrace();
		} finally {
			Thread.interrupted();
			if(conversionThread != null) {
				conversionThread.close();
			}
		}
	} catch (IOException e) {
		ValidationPlugin.log(e);
		e.printStackTrace();
	}

	monitor.worked(1);

  // as the result it exports to file stepFileName+"_"
  // we would like to have
  // fXimFile which should be "_temporary_xim.pf"; 				
  // so perhaps lets try to rename
 
    File file_old_name = new File(fStepFile + "_");
    File file_new_name = new File(fXimFile);
    boolean success = file_old_name.renameTo(file_new_name);
    if (!success) {
			ValidationPlugin.log("<CONVERSION> - the conversion result NOT succesfully renamed", 1);
    }
    
 
 
    // show the conversion result:
		if (fProject != null)
			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);

/* 
				if (flag_2nd_validation) { // this should not be necessary


					IWorkspace workspace= ResourcesPlugin.getWorkspace(); 
					 IPath location= Path.fromOSString(fXimFile); 
					 fFile= workspace.getRoot().getFileForLocation(location);										
				
				}

*/		
		
		//###############################################################################
   
		
	}

	void doRunMim2Xim_old(IProgressMonitor monitor) throws CoreException, InterruptedException {
	  String monitor_exec_string = "MIM to XIM conversion";
	  monitor.beginTask(monitor_exec_string + " ", IProgressMonitor.UNKNOWN);

		File xim_file = new File(fXimFile);
		xim_file.delete();


    // String sdaireposDirectory = "G:\\REPOSITORIES";
    String sdaireposDirectory = fWorking_directory; // should be created by the previous mim validation
 		Properties jsdaiProperties = new Properties();
  	jsdaiProperties.setProperty("repositories", sdaireposDirectory);

  	jsdaiProperties.setProperty("mapping.schema.IDA_STEP_SCHEMA_XIM","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
  	jsdaiProperties.setProperty("mapping.schema.IDA_STEP_AIM_SCHEMA","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
		jsdaiProperties.setProperty("mapping.schema.AUTOMOTIVE_DESIGN","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
		jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");

 		SdaiSession session = null;
		SdaiTransaction transaction = null;
		SdaiRepository repo = null;


    for (int irr = 1;;irr++) {
	    session = SdaiSession.getSession();
  	  if (session != null) {
				ValidationPlugin.log("<CONVERSION>SESSION OPEN, closing: " + irr, 	1);
    		try {
				session.closeSession();
			} catch (SdaiException e) {
				// TODO Auto-generated catch block
				ValidationPlugin.log("<CONVERSION>PRE-00", 	1);
				ValidationPlugin.log(e);
				e.printStackTrace();
			}  
    		session = null;
	    } else {
	    	break;
	    }
		}

  		try {
//			ValidationPlugin.log("<CONVERSION>00-PRE", 1);
			SdaiSession.setSessionProperties(jsdaiProperties);
//			ValidationPlugin.log("<CONVERSION>00-AFTER", 1);
		} catch (SdaiException e1) {
			// TODO Auto-generated catch block
            fSdaiException = e1;
            flag_exception = 1; 
			ValidationPlugin.log("<CONVERSION>00-E", 	1);
			ValidationPlugin.log(e1);
			e1.printStackTrace();
		}

		try {
//			ValidationPlugin.log("<CONVERSION>01-PRE", 1);
			session = SdaiSession.openSession();
//			ValidationPlugin.log("<CONVERSION>01-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 2; 
			ValidationPlugin.log("<CONVERSION>01-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		Importer importer = new Importer();
		try {
//			ValidationPlugin.log("<CONVERSION>02-PRE", 1);
			transaction = session.startTransactionReadWriteAccess();
//			ValidationPlugin.log("<CONVERSION>02-AFTER", 1);
		} catch (SdaiException e) {
			try {
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 3; 
			ValidationPlugin.log("<CONVERSION>02-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
//		SdaiTransaction transaction = importer.startTransactionReadWriteAccess(session);
		try {
//			ValidationPlugin.log("<CONVERSION>03-PRE", 1);
			repo = session.importClearTextEncoding("", fStepFile, null);
//			ValidationPlugin.log("<CONVERSION>03-AFTER", 1);
		} catch (SdaiException e) {
			try {
				transaction.endTransactionAccessAbort();
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 4; 
			ValidationPlugin.log("<CONVERSION>03-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
//			ValidationPlugin.log("<CONVERSION>04-PRE", 1);
			importer.runImport(repo);
//			ValidationPlugin.log("<CONVERSION>04-AFTER", 1);
		} catch (SdaiException e) {
			try {
				transaction.endTransactionAccessAbort();
				repo.closeRepository();
				repo.deleteRepository();
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 5; 
			ValidationPlugin.log("<CONVERSION>04-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
			//			throw(e);
		}
		// transaction.endTransactionAccessAbort();
		// probably need to commit to export
		try {
//			ValidationPlugin.log("<CONVERSION>05-PRE", 1);
			transaction.commit();
//			ValidationPlugin.log("<CONVERSION>05-AFTER", 1);
		} catch (SdaiException e) {
			try {
				transaction.endTransactionAccessAbort();
				repo.closeRepository();
				repo.deleteRepository();
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 6;
			ValidationPlugin.log("<CONVERSION>05-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
//			ValidationPlugin.log("<CONVERSION>06-PRE", 1);
			repo.exportClearTextEncoding(fXimFile);
//			ValidationPlugin.log("<CONVERSION>06-AFTER", 1);
		} catch (SdaiException e) {
			try {
				repo.closeRepository();
				repo.deleteRepository();
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 7;
			ValidationPlugin.log("<CONVERSION>06-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
//			ValidationPlugin.log("<CONVERSION>07-PRE", 1);
			repo.closeRepository();
//			ValidationPlugin.log("<CONVERSION>07-AFTER", 1);
		} catch (SdaiException e) {
			try {
				repo.deleteRepository(); // maybe not
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 8;
			ValidationPlugin.log("<CONVERSION>07-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
//			ValidationPlugin.log("<CONVERSION>08-PRE", 1);
			repo.deleteRepository();
//			ValidationPlugin.log("<CONVERSION>08-AFTER", 1);
		} catch (SdaiException e) {
			try {
				session.closeSession();
			} catch (SdaiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 9;
			ValidationPlugin.log("<CONVERSION>08-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
//			ValidationPlugin.log("<CONVERSION>09-PRE", 1);
			session.closeSession();
//			ValidationPlugin.log("<CONVERSION>09-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
            fSdaiException = e;
            flag_exception = 10;
			ValidationPlugin.log("<CONVERSION>09-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		if (fProject != null)
			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);


	}




	protected void deleteMarkers(String step_file_str, IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName(("Deleting Markers"));
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath step_file_path = new Path(step_file_str);
		IFile step_files[] = root.findFilesForLocation(step_file_path);
		if (fValidationCount == 0) {
		
		IMarker[] p21problems = root.findMarkers("net.jsdai.step_editor.p21problem", true, IResource.DEPTH_INFINITE);
			
		for (int i = 0; i < p21problems.length; i++) {
			IMarker marker = p21problems[i];
			if (marker.exists()) { // this check may not be needed
			if (flagDeleteAllJSDAIMarkers || flagDeleteAllP21Markers) {
				marker.delete();
			} else {
				// only express markers for this project
				String project_attribute = marker.getAttribute("project", "");
  			marker.delete();
			}				
		}
	} // for

	if (flagDeleteAllJSDAIMarkers) {
				// delete also all p21 problem markers
		IMarker[] xproblems = root.findMarkers("net.jsdai.step_editor.expressproblem", true, IResource.DEPTH_INFINITE);
		for (int i = 0; i < xproblems.length; i++) {
			IMarker marker = xproblems[i];
			if (marker.exists()) { // this check may not be needed
				marker.delete();
			}
		}
	}

	}
		
	 // delete markers
	}



	protected void createMarkers(String output, String step_file_str, IProgressMonitor monitor) throws CoreException {

		monitor.setTaskName(("Creating Markers"));
//		System.out.println("<<Creating Markers>>");
		

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
//		IWorkspace workspace = fProject.getWorkspace();
//		IWorkspaceRoot root = workspace.getRoot();
		IPath step_file_path = new Path(step_file_str);
		IFile step_files[] = root.findFilesForLocation(step_file_path);

		// String project_name = fProject.getName();
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

		//monitor.setTaskName(("Creating Markers - 02"));

		if (fValidationCount == 0) {
		
//			IMarker[] p21problems = root.findMarkers("net.jsdai.express_compiler.p21problem", true, IResource.DEPTH_INFINITE);
			IMarker[] p21problems = root.findMarkers("net.jsdai.step_editor.p21problem", true, IResource.DEPTH_INFINITE);

			//monitor.setTaskName(("Creating Markers - 03"));
			
			for (int i = 0; i < p21problems.length; i++) {
					IMarker marker = p21problems[i];
//System.out.println("<<!!!>> found a marker");
					if (marker.exists()) { // this check may not be needed
//System.out.println("<<!!!>> found an existing marker, deleting");
						if (flagDeleteAllJSDAIMarkers || flagDeleteAllP21Markers) {
							marker.delete();
						} else {
							// only express markers for this project
							String project_attribute = marker.getAttribute("project", "");
                            // TODO - maybe check if it is the same project as the one where p21 file is in, any project, not just express project
							//if (project_name.equals(project_attribute)) {
								marker.delete();
							//}
						}				
					}
			} // for

			// monitor.setTaskName(("Creating Markers - 04"));
			
			
			if (flagDeleteAllJSDAIMarkers) {
				// delete also all p21 problem markers
//				IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.expressproblem", true, IResource.DEPTH_INFINITE);
				IMarker[] xproblems = root.findMarkers("net.jsdai.step_editor.expressproblem", true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < xproblems.length; i++) {
					IMarker marker = xproblems[i];
					if (marker.exists()) { // this check may not be needed
						marker.delete();
					}
				}
			}

	} // delete markers

			//monitor.setTaskName(("Creating Markers - 05"));


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
//			(new ValidationMessageParser()).parseValidationMessages(fProject, step_files[i], output, null, monitor);
			// do not pass the project at this point - no project available
			if (flag_mim)
				monitor.setTaskName(("Parsing MIM validation messages"));
			else if (flag_xim)
				monitor.setTaskName(("Parsing XIM validation messages"));
			else
				monitor.setTaskName(("Parsing validation messages"));
			(new ValidationMessageParser()).parseValidationMessages(null, step_files[i], output, null, monitor);
		}
		//monitor.setTaskName(("Creating Markers - 06"));

	
	}


	protected void createImportMarkers(String output, String step_file_str, IProgressMonitor monitor) throws CoreException {

		monitor.setTaskName(("Creating Markers"));
  	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
  	IPath step_file_path = new Path(step_file_str);
		IFile step_files[] = root.findFilesForLocation(step_file_path);

		if (false) {
		// if (fValidationCount == 0) {
		
			IMarker[] p21problems = root.findMarkers("net.jsdai.step_editor.p21problem", true, IResource.DEPTH_INFINITE);

			//monitor.setTaskName(("Creating Markers - 03"));
			
			for (int i = 0; i < p21problems.length; i++) {
					IMarker marker = p21problems[i];
					if (marker.exists()) { // this check may not be needed
					if (flagDeleteAllJSDAIMarkers || flagDeleteAllP21Markers) {
							marker.delete();
						} else {
							// only express markers for this project
							String project_attribute = marker.getAttribute("project", "");
                            // TODO - maybe check if it is the same project as the one where p21 file is in, any project, not just express project
							//if (project_name.equals(project_attribute)) {
								marker.delete();
							//}
						}				
					}
			} // for

			// monitor.setTaskName(("Creating Markers - 04"));
			
			
			if (flagDeleteAllJSDAIMarkers) {
				// delete also all p21 problem markers
//				IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.expressproblem", true, IResource.DEPTH_INFINITE);
				IMarker[] xproblems = root.findMarkers("net.jsdai.step_editor.expressproblem", true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < xproblems.length; i++) {
					IMarker marker = xproblems[i];
					if (marker.exists()) { // this check may not be needed
						marker.delete();
					}
				}
			}

	} // delete markers



		for (int i = 0; i < step_files.length; i++) {
			if (flag_mim)
				monitor.setTaskName(("Parsing MIM validation messages"));
			else if (flag_xim)
				monitor.setTaskName(("Parsing XIM validation messages"));
			else
				monitor.setTaskName(("Parsing validation messages"));
			(new ValidationMessageParser()).parseValidationImportMessages(null, step_files[i], output, null, monitor);
//			fParser.parseValidationImportMessages(null, step_files[i], output, null, monitor);
		}
		//monitor.setTaskName(("Creating Markers - 06"));

	
	}

    void ParseViolationsIncrementally(InputStream istream, String step_file_str, IProgressMonitor monitor) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IPath step_file_path = new Path(step_file_str);
		IFile step_files[] = root.findFilesForLocation(step_file_path);
	   try {
		String validateOutput = ValidationUtils.getStringFromStreamAvailable(istream);
		fValidationLength = validateOutput.length();
		if (fValidationLength < 0) fValidationLength = 0;
		if (fValidationLengthPrev < 0) fValidationLengthPrev = 0;
		if (fValidationLength <= fValidationLengthPrev) return;
//		System.out.println("previous: " + fValidationLengthPrev + "current: " + fValidationLength);
//		ValidationPlugin.log("previous: " + fValidationLengthPrev + ", current: " + fValidationLength, 1);
//		ValidationPlugin.log(validateOutput,1);
		String output = validateOutput.substring(fValidationLengthPrev,fValidationLength);
		for (int i = 0; i < step_files.length; i++) {
		try {
//			(new ValidationMessageParser()).parseValidationMessages(null, step_files[i], output, null, monitor);
			(new ValidationMessageParser()).parseValidationMessages(null, step_files[i], validateOutput, null, monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if (fValidationLength > fValidationLengthPrev) 
			fValidationLengthPrev = fValidationLength;
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }

		
	}
