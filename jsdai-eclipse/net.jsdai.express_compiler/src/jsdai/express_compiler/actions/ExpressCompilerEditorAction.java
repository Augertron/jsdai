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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jsdai.expressCompiler.ECMonitor;
import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.actions.ExpressCompilerProjectAction.ECMonitorImpl;
import jsdai.express_compiler.editor.ExpressCompilerMessageParser;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;
import jsdai.express_compiler.utils.ExpressCompilerRepo;
import jsdai.express_compiler.utils.ExpressCompilerUtils;
import jsdai.express_compiler.utils.IsolatedRunnableThread;
import jsdai.express_compiler_core.ExpressCompilerCorePlugin;
import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

public class ExpressCompilerEditorAction implements IWorkbenchWindowActionDelegate {

	
	String fCompilerOutput;
	private static boolean fUseIsolatedThread = true;
	private IFile currentFile;
	private IPath currentPath;
	private IEditorPart currentEditor;
	private IProject fProject = null;
	
	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		currentFile = null;
		currentPath = null;
		currentEditor = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sturcturedSelection = (IStructuredSelection) selection;
			List selResources = IDE.computeSelectedResources(sturcturedSelection);
			for (Iterator i = selResources.iterator(); i.hasNext();) {
				IResource resource = (IResource) i.next();
				if (resource instanceof IFile) {
					if(currentFile == null) {
						currentFile = (IFile) resource; 
					} else {
						break;
					}
				}
			}
		} else if (selection instanceof ITextSelection) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			
			currentEditor = page.getActiveEditor();
			if (currentEditor != null) {
				IEditorInput currentEditorInput = currentEditor.getEditorInput();
				if (currentEditorInput instanceof FileEditorInput) {
					currentFile = ((IFileEditorInput)currentEditorInput).getFile();
				} else if (currentEditorInput instanceof IPathEditorInput) {
					currentPath = ((IPathEditorInput)currentEditorInput).getPath();
				}
			}
		}
	}

	public void run(IAction action) {

		if((currentEditor != null && !PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().saveEditor(currentEditor, true))
				|| !IDE.saveAllEditors(new IResource[] { currentFile }, true)) {
			return;
		}
		
		IWorkbenchPage fPage;
		boolean in_express_project = false;
		IFile fileToCompile = findCurrentExpressFile();
		if (fileToCompile != null) {
			IProject prj = fileToCompile.getProject();


			try {
				if (prj.hasNature("net.jsdai.express_compiler.expressNature")) {
//System.out.println("Express Project - nature found");
					in_express_project = true;
					fProject = prj;
				} else {
//System.out.println("Express Project - not express project - nature NOT found");
				}
			} catch (CoreException e1) {
	ExpressCompilerPlugin.log(e1);
			// TODO Auto-generated catch block
//			System.out.println("Exception when checking project nature:" + e1);
				e1.printStackTrace();
			}
		}
		if (!in_express_project) {
//			String current_directory = getWorkingDirectory();
			String current_directory = ExpressCompilerUtils.getWorkingDirectory();

//System.out.println("<O>NOT express project, working directory: " + current_directory);

			if (current_directory == null ) {
				MessageBox infoBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
				infoBox.setMessage(ExpressCompilerPlugin.getResourceString("CompilerAction.messageBoxContent"));
				infoBox.setText(ExpressCompilerPlugin.getResourceString("CompilerAction.messageBoxTitle"));
				infoBox.open();
				return;
			}
		}		
		
		
		
		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
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
		memoryBox.setMessage("Express compilation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		memoryBox.setText("Out of memory error");
		try {
			(new ProgressMonitorDialog(null)).run(true, true, op);
		} catch (InterruptedException e) {
				ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
//				ExpressCompilerPlugin.log(e);
//System.out.println("Express Compilation CANCELED: " + e);
			return;
		} catch (InvocationTargetException e) {
			ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
			ExpressCompilerPlugin.log(e);
			Throwable realException = e.getTargetException();
//System.out.println("ExpressCompilerEditorAction exception: " + e);
//System.out.println("ExpressCompilerEditorAction real exception: " + realException);
realException.printStackTrace();
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());

//			ExpressCompilerPlugin.log(e);
//			ExpressCompilerPlugin.console(realException.toString());
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
		}

		ExpressCompilerUtils.printResultInConsole(fCompilerOutput, fPage);
//		printResultInConsole(fCompilerOutput);

	}


		private void doRun(IProgressMonitor monitor) throws CoreException, InterruptedException {
  	

 	

			boolean in_project = false;
//			boolean in_express_project = false;

		
			IFile fileToCompile = findCurrentExpressFile();
			IPath pathToCompile = null;
		
			if (fileToCompile == null) {
				pathToCompile = findCurrentExpressFilePath();
				in_project = false;
//				in_express_project = false;
			} else {
				pathToCompile = fileToCompile.getFullPath();
				in_project = true;
			}
  
			if (pathToCompile == null) {
		
				System.err.println("ERROR : no file in editor");
				return;
			}

//			String x_project_location = null;
			String x_temp_location = null;
			String x_repo_location = null;


			//String current_directory = ExpressCompilerUtils.getWorkingDirectory();
			String current_directory = ExpressCompilerPlugin.getDefault().getWorkingDirectory();


			if (in_project) {
				// see if it is express project, if so, get all the locations, etc.
				// if not - use working directory
				// very stupid temp implementation that stores stuff in the comment of the project description
			
				IProject prj = fileToCompile.getProject();
	
				if (prj.hasNature("net.jsdai.express_compiler.expressNature")) {

//					in_express_project = true;

//					String s_temp_location_type = "_no_value_";
//					String s_delete_temp_on_exit = "_no_value_";
//					String s_temp_location = "_no_value_";
//					String s_temp_location_system = "_no_value_";
//					String s_temp_location_eclipse = "_no_value_";
//					String s_temp_location_project = "_no_value_";

//					String s_is_default_repo_location = "_no_value_";
//					String s_delete_repo_on_exit = "_no_value_";
//					String s_repo_location = "_no_value_";
//					String s_repo_location_default = "_no_value_";



//					try {


//							s_temp_location_system = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationSystem"));
//							s_temp_location_eclipse = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationEclipse"));
//							s_temp_location_project = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationProject"));
//						s_temp_location = prj.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocation"));
//						  s_temp_location_type = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fTempLocation"));
//						  s_delete_temp_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteOnExit"));

//							s_repo_location_default = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".repoLocationDefault"));
//						s_repo_location = prj.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".repoLocation"));
//						  s_is_default_repo_location = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDefaultRepoLocation"));
//						  s_delete_repo_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteRepoOnExit"));

//					} catch (CoreException e1) {
//						ExpressCompilerPlugin.log(e1);
//						System.out.println("Exception - Problems with reading of persistent properties: " + e1);
//						e1.printStackTrace();
//					}

//					x_project_location = prj.getLocation().toOSString();
					// x_temp_location = s_temp_location;
					// x_repo_location = s_repo_location;
	

					x_temp_location = current_directory;
					x_repo_location  = x_temp_location;
					if (x_temp_location.endsWith("\\") || x_temp_location.endsWith("/")) {
						x_repo_location += "ExpressCompilerRepo";
					} else {
						x_repo_location +=  File.separator + "ExpressCompilerRepo";
					}

			
					// that is not all, now we need to process and transform locations:
					// default temp = project
					// default repo = temp
//				if (x_temp_location)

				} else { 
					// a project, but not an express project, use working directory

					x_temp_location = current_directory;
//				x_temp_location = ExpressCompilerUtils.getWorkingDirectory();
					x_repo_location  = x_temp_location;
					if (x_temp_location.endsWith("\\") || x_temp_location.endsWith("/")) {
						x_repo_location += "ExpressCompilerRepo";
					} else {
						x_repo_location +=  File.separator + "ExpressCompilerRepo";
					}
				}
			} else {
				// not a project - an external express file - use working directory

				x_temp_location = current_directory;
//			x_temp_location = ExpressCompilerUtils.getWorkingDirectory();
				x_repo_location  = x_temp_location;
				if (x_temp_location.endsWith("\\") || x_temp_location.endsWith("/")) {
					x_repo_location += "ExpressCompilerRepo";
				} else {
					x_repo_location +=  File.separator + "ExpressCompilerRepo";
				}
			}

//		String last_segment = pathToCompile.lastSegment();

			Runtime r = Runtime.getRuntime();

			try {

				// String command = buildCommand(fileToCompile);
				// String command = "java -version";

			
			String filePath = fileToCompile != null ? fileToCompile.getLocation().toOSString() : pathToCompile.toOSString();
//			String filePath = pathToCompile.toOSString();
//			String filePath = pathToCompile.toString();
//		String fileFolderPath =	pathToCompile.removeLastSegments(1).toOSString();			
//			String fileFolderPath =	pathToCompile.removeLastSegments(1).toString();			
//			if (fileToCompile != null) {
//				filePath = fileToCompile.getLocation().toOSString();
////			filePath = fileToCompile.getName();
//				fileFolderPath =	filePath.substring(0, filePath.length() - fileToCompile.getName().length());			
//				if (fileFolderPath.endsWith("Express files\\")) {
//					fileFolderPath = fileFolderPath.substring(0, fileFolderPath.length()- 14);
//				}
//			}
//		String current_directory = null;

			String sdaireposDirectory = null;
			if (false) {
//			if (in_express_project) {
				// still better copy ExpressCompilerRepo to its location, we do not support incremental for now
				// make temp the current directory
        IProject prj = fileToCompile.getProject();
//				if (x_temp_location.equalsIgnoreCase("DEFAULT")) {
				if (false) {
					IFolder internal_src = prj.getFolder("temp");
					current_directory = internal_src.getLocation().toOSString();
//					if (x_repo_location.equalsIgnoreCase("DEFAULT")) {
					if (false) {
						ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo");
					} else {
						ExpressCompilerRepo.copy(x_repo_location + "/ExpressCompilerRepo");
					}
				} else {
					// custom temp location - make it current directory
					current_directory = x_temp_location;
//					if (x_repo_location.equalsIgnoreCase("DEFAULT")) {
					if (false) {
						ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo");
					} else {
						String xcr_destination = x_repo_location;
						if (xcr_destination.endsWith("\\") || xcr_destination.endsWith("/")) {
							xcr_destination += "ExpressCompilerRepo";
						} else {
							xcr_destination += File.separator + "ExpressCompilerRepo";
						}
//System.out.println("REPO destination: " + xcr_destination);
//						ExpressCompilerRepo.copy(xcr_destination);
						ExpressCompilerRepo.copy(x_repo_location);
	
//						ExpressCompilerRepo.copy(x_repo_location + "/ExpressCompilerRepo");
					}
				}
			} else {
				// copy ExpressCompilerRepo to the working directory
				// make working directory the current directory
				// make the appropriate jsdai.properties file there

				// current_directory = ExpressCompilerPlugin.getDefault().getWorkingDirectory();

				// current_directory = getWorkingDirectory();

//				current_directory = ExpressCompilerUtils.getWorkingDirectory();

				String xcr_destination = current_directory;
				if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
					xcr_destination += "ExpressCompilerRepo";
				} else {
					xcr_destination += File.separator + "ExpressCompilerRepo";
				}
				ExpressCompilerRepo.copy(xcr_destination);
				if(fUseIsolatedThread) {
					sdaireposDirectory = xcr_destination;
				} else {
					ExpressCompilerUtils.createJsdaiProperties(current_directory, xcr_destination);
				}

//				ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo");
			}

			// runs the command
//			Process p = r.exec(command.toString(), new String[] {}, new File(fileFolderPath));
//System.out.println("before exec, file path " + filePath + ", folder: " + fileFolderPath);			

// Process p = r.exec("java -version", null, null);
//Process p = r.exec("type " + fileToCompile.getName() + " > _log1", null, new File(fileFolderPath));

// String java_directory = "java";
 //String java_directory = current_directory + File.separator + "java";
 

 
 String java_directory = current_directory;
 if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
 		java_directory += "java";
 } else {
  	java_directory +=  File.separator + "java";
 }
 ExpressCompilerUtils.deleteAllFilesAndDirectories(java_directory);



//String exec_string = "java jsdai.expressCompiler.Main -eclipse -write_files -express \"" + filePath + "\" -binaries -index_file -java -out " + java_directory;
//System.out.println("exec string: " + exec_string);			
//System.out.println("current_directory: " + current_directory);			

 		String monitor_exec_string = "Compiling the express file";
		monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);

/*
		envp      array of strings, each element of which 
	     has environment variable settings in format
	     name=value.
*/
	     

/*33333333333333333333333333333333333333333333333333		
		
		
		String jsdai_properties_path = null;
		IFile jsdai_properties = the_project.getFile("jsdai.properties");
		if (jsdai_properties != null) {
			jsdai_properties_path = jsdai_properties.getParent().getLocation()
					.toOSString();
			System.setProperty("jsdai.properties", jsdai_properties_path);





			args[arg_index++] = jsdai_properties_path;

	// the classpath to the binary files in the plugin
			
//	Bundle pluginBundle = ExpressCorePlugin.getDefault().getBundle();
	String classpath_string = "";
	String cpath = getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
	if (cpath != null) classpath_string += cpath + ";";
	cpath = getClassPath(ExpressPlugin.getDefault().getBundle(), "jsdai_express.jar");
	if (cpath != null) classpath_string += cpath + ";";
	cpath = getClassPath(RuntimePlugin.getDefault().getBundle(), "SExtended_dictionary_schema.zip");
	if (cpath != null) classpath_string += cpath + ";";
	args[arg_index] = classpath_string;
			
		
		
		
		
*///444444444444444444444444444444444444444444444444444	    	 
	    	
//		String jsdai_properties_path = current_directory + File.separator + "jsdai.properties";
		String jsdai_properties_path = current_directory;
	 	if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
		 jsdai_properties_path += "jsdai.properties";
		} else {
		 jsdai_properties_path += File.separator + "jsdai.properties";
		}


//		Bundle pluginBundle = ExpressCorePlugin.getDefault().getBundle();
		String classpath_string = "";
		
		if(!fUseIsolatedThread) {
			String cpath = ExpressCompilerUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ExpressCompilerUtils.getClassPath(ExpressCompilerCorePlugin.getDefault().getBundle(), "jsdai_express.jar");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
			cpath = ExpressCompilerUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
			if (cpath != null) classpath_string += cpath + File.pathSeparator;
		}
		
		

		
//		System.setProperty("jsdai.properties", jsdai_properties_path);


	


		String env[] = null; 
		if(!fUseIsolatedThread) {
			env = new String[2]; 
			env[0] = "jsdai.properties=" + jsdai_properties_path;
			env[1] = "CLASSPATH=." + File.pathSeparator + classpath_string;

//			System.out.println(">jsdai.properties to exec>: " + env[0]);			
//			System.out.println(">classpath to exec>: " + env[1]);			
		}			
//		exec_string += " " + "-jsdaiproperties " + jsdai_properties_path; 

//System.out.println("<exec_string:> " + exec_string);		
//System.out.println("<runtime:> " + r);		

		String [] exec_strings;
		if (fUseIsolatedThread) {
			exec_strings = new String[14];
		} else {
			exec_strings = new String[16];
		}
//		String [] exec_strings = new String[9];
		int ii = 0;
		
		if (!fUseIsolatedThread) {
			exec_strings[ii++] = "java";
			exec_strings[ii++] = "jsdai.expressCompiler.Main";
		}
		exec_strings[ii++] = "-eclipse";
		exec_strings[ii++] = "-write_files";
		exec_strings[ii++] = "-express";
		exec_strings[ii++] = filePath;
// with stand-alone files, we do not generate anything
//		exec_strings[6] = "-binaries";
//		exec_strings[7] = "-index_file";
//		exec_strings[8] = "-java";
//		exec_strings[9] = "-out";
//		exec_strings[10] = java_directory;

//		exec_strings[11] = "-jsdaiproperties";
//		exec_strings[12] = jsdai_properties_path;
//		exec_strings[13] = "-original_case";

		exec_strings[ii++] = "-jsdaiproperties";
		exec_strings[ii++] = jsdai_properties_path;
		exec_strings[ii++] = "-original_case";

		exec_strings[ii++]= "-stdout";
		exec_strings[ii++]= new File(current_directory, "log_output").getAbsolutePath();
		exec_strings[ii++]= "-stderr";
		exec_strings[ii++]= new File(current_directory, "log_errors").getAbsolutePath();
		exec_strings[ii++]= "-inst";
		exec_strings[ii++] = "-out";
		exec_strings[ii++] = java_directory;


  fCompilerOutput = "Starting express compiler on a single file with the following arguments:\n";
//  fCompilerOutput += "Exec string: " + exec_string + "\n";			
	if(!fUseIsolatedThread) {
		fCompilerOutput += "Environment[0]: " + env[0] + "\n";			
		fCompilerOutput += "Environment[1]: " + env[1] + "\n";
	}
  fCompilerOutput += "Current directory: " + current_directory + "\n\n";			
			


			
  fCompilerOutput += "Running the compiler!\n";




// try to delete these files, just in case there are old ones

				String output_file_path = current_directory; 
				String error_file_path =  current_directory;
				
				if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
					output_file_path  += "log_output";
					error_file_path   += "log_errors";
				} else {
					output_file_path  += File.separator + "log_output";
					error_file_path   += File.separator + "log_errors";
				}
		

//   	    InputStream error_stream = new FileInputStream(error_file_path);
//				InputStream stream = new FileInputStream(output_file_path);

				File error_stream_file = new File(error_file_path);
				File output_stream_file = new File(output_file_path);

        error_stream_file.delete();
        output_stream_file.delete();




		
//			Process p = r.exec(exec_string, null, new File(current_directory));
    	IsolatedRunnableThread expressCompilerThread = null;
    	Process p = null;
    	ECMonitorImpl expressCompilerMonitor = new ECMonitorImpl(Thread.currentThread());
    	if(fUseIsolatedThread) {
    		//V.N. Load express compiler using custom class loading
    	 	File[] expressCompilerJars = {
    				new File(ExpressCompilerUtils.getClassPath(
    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
    				new File(ExpressCompilerUtils.getClassPath(
    						ExpressCompilerCorePlugin.getDefault().getBundle(), "jsdai_express.jar")),
    				new File(ExpressCompilerUtils.getClassPath(
    						Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip")),
    			};
    	 	expressCompilerThread =
    	 		IsolatedRunnableThread.newInstance("jsdai.expressCompiler.Main", "initAsRunnable",
    	 				new Class[] { String.class, String[].class, ECMonitor.class },
    	 				new Object[] { sdaireposDirectory, exec_strings, expressCompilerMonitor },
    	 				expressCompilerJars,
    	 				new String[] { "jsdai/expressCompiler/ECMonitor.class", "com/lksoft/util/licensing/" });
    		expressCompilerThread.start();
    	} else {
			p = r.exec(exec_strings, env, new File(current_directory));
    	}


    	if (true) {
    		//if (!Platform.getOS().equals("win32")) {


    			long start_time = System.currentTimeMillis();
    			try {
    				for (;;) {
    					if(!Thread.interrupted()) {
    						if(fUseIsolatedThread) {
    							try {
    								expressCompilerThread.join(1000);
    							} catch (InterruptedException e) {
    							}
    							if(!expressCompilerThread.isAlive()) {
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
    					synchronized (expressCompilerMonitor) {
    						subtaskMessage = expressCompilerMonitor.message;
    					}
    					long current_time = System.currentTimeMillis();
    					long elapsed_time = (current_time - start_time) / 1000;
    					monitor.setTaskName((subtaskMessage != null ? subtaskMessage : monitor_exec_string) +
    							": " + elapsed_time + "s");
    					boolean is_cancel = monitor.isCanceled();
    					if (is_cancel) {
    						fCompilerOutput += "Express compilation canceled";
    						if(fUseIsolatedThread) {
    							expressCompilerThread.interrupt();
    						} else {
    							p.destroy();
    						}
    						throw (new InterruptedException());
    					}
    				}
    			} finally {
    				Thread.interrupted();
    				if(fUseIsolatedThread) {
    					expressCompilerThread.close();
    				}
    			}
    		}




//			int exit_value = p.exitValue();
//System.out.println("exit value: " + exit_value);
			
		monitor.worked(1);
		monitor.setTaskName(("Getting the express compiler messages"));


//			Process p = r.exec("java jsdai.expressCompiler.Main -express " + filePath + " -java", null, new File(fileFolderPath));
//			Process p = r.exec("java -version",  new String[] {}, new File(fileFolderPath));
//System.out.println("after exec, process: " + p);			

			// gets the input stream to have the post-compile-time information
//	    InputStream error_stream = p.getErrorStream();
//			InputStream stream = p.getInputStream();


				error_stream_file = new File(error_file_path);
				output_stream_file = new File(output_file_path);
   	    InputStream error_stream = new FileInputStream(error_stream_file);
				InputStream stream = new FileInputStream(output_stream_file);


//System.out.println("stream: " + stream);			

monitor.worked(1);
//monitor.setTaskName(("getting string from stream"));

			// and get the string from it
	//    String compilerProcessErrors = ExpressCompilerUtils.getStringFromStream(error_stream);
//			String compilerOutput = ExpressCompilerUtils.getStringFromStream(stream);
			  String compilerOutput = ExpressCompilerUtils.getStringFromStreamAvailable(stream);
		    String compilerProcessErrors = ExpressCompilerUtils.getStringFromStreamAvailable(error_stream);


		stream.close();
		error_stream.close();

	//		String compilerOutput = getStringFromStream(stream);

//System.out.println("output: " + compilerOutput);			
			// prints out the information
monitor.worked(1);
//monitor.setTaskName(("practically nothing"));

//		printResultInConsole(compilerOutput);

			fCompilerOutput += "--- Express Compiler process errors, if present ---\n\n";
			fCompilerOutput += compilerProcessErrors;
			fCompilerOutput += "--- Express Compiler Output ---\n\n";
			fCompilerOutput += compilerOutput;
			fCompilerOutput += "\n--- End of Express Compiler Output ---\n";
	
//		fCompilerOutput = compilerOutput;

monitor.worked(1);
monitor.setTaskName(("Parsing the express compiler messages"));
		// parse the buffer to find the errors and create markers
			createMarkers(compilerOutput, fileToCompile, pathToCompile);

monitor.worked(1);

//boolean no_errors = getCompilerExitCode(current_directory);

//System.out.println("compiler result: " + no_errors);



// currently we do not generate anything for express files outside projects
	if (true) {
//	if (!no_errors) {

		ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
	  ExpressCompilerUtils.deleteAllFilesAndDirectories(java_directory);


	} else {

		ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(true);

	monitor.setTaskName(("Compiling the generated java files"));


	Vector compiled_schemas = getCompiledSchemas(current_directory);
//System.out.println("compiled_schemas: " + compiled_schemas);
	Iterator iter = compiled_schemas.iterator();
//System.out.println("<01>before getRuntime");
	Runtime rt = Runtime.getRuntime();
//	System.out.println("<02>after getRuntime: " + rt);

//System.out.println("RuntimePlugin: ");
//System.out.println("RuntimePlugin - default: " + RuntimePlugin.getDefault());
//System.out.println("RuntimePlugin - bundle: " + RuntimePlugin.getDefault().getBundle());

	
	String  jsdai_runtime_classpath = ExpressCompilerUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
//System.out.println("<03>jsdai_runtime_classpath: " + jsdai_runtime_classpath);
	String  jsdai_extended_dictionary_schema_classpath = ExpressCompilerUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
//System.out.println("<04>jsdai_extended_dictionary_schema_classpath: " + jsdai_extended_dictionary_schema_classpath);
	
// for plugin project in development
	// perhaps no longer needed:
	String jsdai_plugin_classpath = ExpressCompilerRepo.getPluginDirectory()+ "ExpressCompilerPlugin.jar";


	 
	String total_classpath; // E:\eclipse\runtime-workbench-workspace\RR1\classes;E:\eclipse\workspace\jsdai.express_compiler\bin
	String binary_file_name;
	
	String sourcepath = java_directory; 
//	String classpath = current_directory + File.separator + "classes";
		String classpath = current_directory;
		if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
		 classpath += "classes";
		} else {
		 classpath += File.separator + "classes";
		}
	File classes_dir = new File(classpath);
	classes_dir.mkdir();
	
	
	
	String outputdir = classpath;
	ExpressCompilerUtils.clearDirectory(outputdir);

	while (iter.hasNext()) {
		String schema_name = (String) iter.next();
//System.out.println("current compiled schema: " + schema_name);

		String package_name = "S"
			+ schema_name.substring(0, 1).toUpperCase()
			+ schema_name.substring(1).toLowerCase();

		total_classpath = classpath + 
						  File.pathSeparator + jsdai_runtime_classpath + 
						  File.pathSeparator +  jsdai_extended_dictionary_schema_classpath + 
						  File.pathSeparator +  jsdai_plugin_classpath;
		String sourcefiles = sourcepath + File.separator + "jsdai"
		+ File.separator + package_name + File.separator
		+ "*.java";

		String javac_string = "javac -sourcepath " + sourcepath
		+ " -classpath " + total_classpath + " -d " + outputdir
		+ " " + sourcefiles;

			String [] javac_strings;
			if (Platform.getOS().equals(Constants.OS_WIN32)) {
				javac_strings = new String[8];
				javac_strings[0] = "javac";
				javac_strings[1] = "-sourcepath";
				javac_strings[2] = sourcepath;
				javac_strings[3] = "-classpath";
				javac_strings[4] = total_classpath;
				javac_strings[5] = "-d";
				javac_strings[6] = outputdir;
				javac_strings[7] = sourcefiles; 
			} else {
				javac_strings = new String[10];
				javac_strings[0] = "/bin/sh";
				javac_strings[1] = "-c";
				javac_strings[2] = "javac";
				javac_strings[3] = "-sourcepath";
				javac_strings[4] = sourcepath;
				javac_strings[5] = "-classpath";
				javac_strings[6] = total_classpath;
				javac_strings[7] = "-d";
				javac_strings[8] = outputdir;
				javac_strings[9] = sourcefiles; 
			}

			Process compileJob = null;
			try {
				compileJob = rt.exec(javac_strings, null, null);
			} catch (IOException e) {
					ExpressCompilerPlugin.log(e);
					e.printStackTrace();
			}



/*
		if (Platform.getOS().equals(Constants.OS_WIN32)) {
			try {
//System.out.println("javac string: " + javac_string);
				compileJob = rt.exec(javac_string, null, null);
			} catch (IOException e) {
					ExpressCompilerPlugin.log(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				compileJob = rt.exec(new String[] { "/bin/sh", "-c",
						javac_string }, null, null);
			} catch (IOException e) {
				ExpressCompilerPlugin.log(e);
				e.printStackTrace();
			}
		}

*/
		try {
			compileJob.waitFor();
		} catch (InterruptedException e) {
			ExpressCompilerPlugin.log(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		binary_file_name = schema_name.toUpperCase() + "_DICTIONARY_DATA";

		String binary_file_source_path = sourcepath + File.separator
			+ "jsdai" + File.separator + package_name
			+ File.separator + binary_file_name;
		String binary_file_destination_path = classpath
			+ File.separator + "jsdai" + File.separator
			+ package_name + File.separator + binary_file_name;
		File source_binary_file = new File(binary_file_source_path);
		File destination_binary_file = new File(binary_file_destination_path);
		ExpressCompilerRepo.copyFile(source_binary_file, destination_binary_file);
	} // while - through java packages

	// now we could move repository.properties file to classes

	String repository_properties_source_path = sourcepath
			+ File.separator + "jsdai" + File.separator
			+ "repository.properties";
	String repository_properties_destination_path = classpath
			+ File.separator + "jsdai" + File.separator
			+ "repository.properties";
	File source_repository_properties = new File(repository_properties_source_path);
	File destination_repository_properties = new File(repository_properties_destination_path);
	if (source_repository_properties.exists()) {
		ExpressCompilerRepo.copyFile(source_repository_properties, destination_repository_properties);
	}

	
	
	monitor.setTaskName(("refreshing"));
 } // if express compiler ended without errors
			// And refresh the compilation unit folder
		if (fileToCompile != null) {			
			fileToCompile.getParent().refreshLocal(IResource.DEPTH_ONE, null);
		}
		} catch (IOException e) {
			ExpressCompilerPlugin.log(e);
			// $$$ should throw the exception again
//			System.err.println("Problem");
//			e.printStackTrace();
		} catch (CoreException e) {
			ExpressCompilerPlugin.log(e);
			// $$$ do something here !
//System.out.println("Hey, core exception: " + e);
		}



///######################

/*
		// ok, now we have the file, try to set some markers or something
		printResultInConsole("kuku - test");

		// also test
        Map attributes = new HashMap();
        
                attributes.put(IMarker.SEVERITY, new Integer(
                        IMarker.SEVERITY_ERROR));
            MarkerUtilities.setLineNumber(attributes, 5);
            MarkerUtilities.setMessage(attributes, "kuku tralialia");
            try {
				MarkerUtilities.createMarker(fileToCompile, attributes,
				        IMarker.PROBLEM);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
*/
		
		// end of test -------------------------------------------
		
		
		







		
	}

	/**
	 * Prints out the string represented by the string buffer
	 */
/*  no longer used
  protected void printResultInConsole(String output) {
//System.out.println("<TO-Console-01> output: " + output);
		try {
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IWorkbenchPage page = fPage;
//			System.out.println("<TO-Console-02> page: " + page);
			ExpressConsoleView console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
//System.out.println("<TO-Console-03> console: " + console);
			
			if (console!=null) {
				console.setOutputText(output);
//System.out.println("<TO-Console-04>");
//RR			} else if (ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferencePage.SHOW_OUTPUT_IN_CONSOLE)==true) {
			} else {
				page.showView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
				console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);			
//System.out.println("<TO-Console-05> console: " + console);
				console.setOutputText(output);
//System.out.println("<TO-Console-06>");
			}
		} catch (PartInitException e) {
			ExpressCompilerPlugin.log(e);
//System.out.println("<TO-Console-07 exception: >" + e);
			ExpressCompilerPlugin.getDefault().getLog().log(
				new Status(
					IStatus.ERROR,
					ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID,
					0,
					ExpressCompilerPlugin.getResourceString("CompilerAction.consoleViewOpeningProblem"),
					e));
		} catch (Throwable thr) {
			ExpressCompilerPlugin.log(thr);
			System.out.println("<TO-Console-08 exception: >" + thr);
		}
		
	}
*/	
	
	/**
	 * Finds the file that is currently open in the express editor
	 */
	protected IFile findCurrentExpressFile() {
		return currentFile;
	}

	protected IPath findCurrentExpressFilePath() {
		if(currentFile != null) {
			IEditorInput editorInput = currentEditor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				return ((IFileEditorInput)editorInput).getFile().getFullPath();
			}
		}
		return currentPath;
	}
	
	
	
	
	/**
	 * Creates a string buffer from the given input stream
	 */
	protected String getStringFromStream_original(InputStream stream) throws IOException {
		StringBuffer buffer = new StringBuffer();
//rr		byte[] b = new byte[100];
//		byte[] b = new byte[1000];
//		byte[] b = new byte[10000];
//		byte[] b = new byte[100000];
//		byte[] b = new byte[1000000];
		byte[] b = new byte[10000000];
//		byte[] b = new byte[Integer.MAX_VALUE/2];
		int finished = 0;
		while (finished != -1) {
			finished = stream.read(b);
//System.out.println("byte array: " + b);
			if (finished != -1) {
				String current = new String(b, 0, finished);
				buffer.append(current);
			}
		}
		return buffer.toString();
	}

	
	protected String getStringFromStream_alt(InputStream stream) throws IOException {

	String textString;
    Reader in = new BufferedReader(new InputStreamReader(stream));
    char[] readBuffer= new char[2048];
    StringBuffer buffer= new StringBuffer(2048);
    int n;
    while ((n = in.read(readBuffer)) > 0) {
      buffer.append(readBuffer, 0, n);
    }
    textString = buffer.toString();
//    stream.close();
	  return textString;
	}
	

	protected String getStringFromStream(InputStream stream) throws IOException {
		int k;
		// seems to be the fastest way - the largest chunks, hopefully, in a single one
//		int aBuffSize = Integer.MAX_VALUE;
//		int aBuffSize = 10000000; // ok
//		int aBuffSize = 100000000; // not working
//		int aBuffSize = 50000000; // not working
//		int aBuffSize =   28000000; // OK
		int aBuffSize =   1000; // OK
//		int aBuffSize = 2147483647; // max - error
		byte buff[] = new byte[aBuffSize];
		OutputStream xOutputStream = new ByteArrayOutputStream(aBuffSize); 
		while ((k = stream.read(buff) ) != -1) { 
			xOutputStream.write(buff,0,k);
		}
		return xOutputStream.toString();
	}




	
	
	
	/**
	 * Create markers according to the compiler output
	 */
	protected void createMarkers_old(String output, IFile file) throws CoreException {
		// first delete all the previous markers
		file.deleteMarkers(IMarker.PROBLEM, false, 0);

		//CompilerMessageParser lc_compilerMessageParser = getCompilerMessageParser();
			
		(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, file, output, null);

	}

	protected void createMarkers(String output, IFile file, IPath path) throws CoreException {
		// first delete all the previous markers
		if (file != null) {
			file.deleteMarkers(IMarker.PROBLEM, false, 0);
		}
		//CompilerMessageParser lc_compilerMessageParser = getCompilerMessageParser();
			
		(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, file, output, path);

	}


	
    /**
	 * @return Returns the working directory.
	 */
	public static String getWorkingDirectory() {
		String working_directory  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressCompilerPreferences.EXPRESS_WORKING_DIRECTORY);
		
		working_directory = "".equals(working_directory) ? null : working_directory;
		
		return working_directory;
		
	}

/*	
	private boolean getCompilerExitCode(String current_directory) {

//		IFile result_file = fProject.getFile("Internal/temp_rezult");
//		String file_name = result_file.getLocation().toOSString();
		String file_name = current_directory + File.separator + "temp_rezult";
		String result = "ERROR";
		
		try {
			FileInputStream ins = new FileInputStream(file_name);
		    InputStreamReader isr = new InputStreamReader(ins);
		    StreamTokenizer st = new StreamTokenizer(isr);
		    st.eolIsSignificant(true);
		    st.wordChars('_', '_');
		    st.wordChars('.', '.');
		    st.wordChars(':', ':');
		    st.wordChars('/', '/');
		    st.wordChars('\\', '\\');
		    st.commentChar('#');

		    while (st.ttype != StreamTokenizer.TT_EOF) {
		    	st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					result = st.sval;
					break;
				} // if		
			} // while
		  } // try
		
		    //  catch (FileNotFoundException e) 
		  catch (IOException e) {
				ExpressCompilerPlugin.log(e);
System.out.println("<X> exception: " + e);						
		    return false;
		   }
		  if (result.equals("OK")) {
		  	return true;
		  } else {
		  	return false;
		  }
		}

*/	
	
	private Vector getCompiledSchemas(String current_directory) {
		Vector result = new Vector();

		String file_name = current_directory + File.separator + "temp_schemas";
	
		
		try {
			FileInputStream ins = new FileInputStream(file_name);
		    InputStreamReader isr = new InputStreamReader(ins);
		    StreamTokenizer st = new StreamTokenizer(isr);
		    st.eolIsSignificant(true);
		    st.wordChars('_', '_');
		    st.wordChars('.', '.');
		    st.wordChars(':', ':');
		    st.wordChars('/', '/');
		    st.wordChars('\\', '\\');
		    st.commentChar('#');

		    while (st.ttype != StreamTokenizer.TT_EOF) {
		    	st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					result.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
		  } // try

		    //  catch (FileNotFoundException e) 
		  catch (IOException e) {
				ExpressCompilerPlugin.log(e);
//		    System.out.println("the file " + file_name + " with the models to include not found.");
					
		    return null;
		  }
	
		
		
		
		return result;
	}

	
/*	
	private String getClassPath(Bundle bundle, String jarFile) {
		try {
			URL classpath = Platform.asLocalURL(bundle.getEntry(jarFile));
			String classpath_string = classpath.getFile().toString();
			classpath_string = classpath_string.substring(1);
			return classpath_string;
		} catch (IOException e) {
			ExpressCompilerPlugin.log(e);
			e.printStackTrace();
			return null;
		}
	}
*/	
		
}

	

