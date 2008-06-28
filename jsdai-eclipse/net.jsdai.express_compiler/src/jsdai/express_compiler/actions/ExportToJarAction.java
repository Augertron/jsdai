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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.properties.ExpressProjectTempPropertyPage;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
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
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.swt.widgets.Shell;

public class ExportToJarAction extends Action implements IWorkbenchWindowActionDelegate {

	String fDestination;
	ISelection fSelection;
	IAction fAction;
	IProject fProject;
	boolean fIn_express_project;
	boolean fIn_project;
	String fProject_comments;
	String fWorking_directory;
	
  public void dispose() {
    // TODO Auto-generated method stub
    
  }
  

  public void init(IWorkbenchWindow window) {
    // TODO Auto-generated method stub
	  
		//ExpressCompilerPlugin.getDefault().setExportToJarlAction(this);
//	  IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();		help.setHelp(fAction, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	  WorkbenchHelp.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	  
//		help.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
	 //   net.jsdai.express_compiler.actions.ExportToJarAction
  }

  public void selectionChanged(IAction action, ISelection selection) {
		fSelection = selection;
		fAction = action;
//		String java_directory = null;
		

		boolean enabled = false;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object obj = sel.getFirstElement();
			if (obj instanceof IResource) {
				fProject = ((IResource)obj).getProject();
        fIn_project = true;
				// true, if express project, 
				// if not - check working directory:
				// if it is defined, if it contains classes folder

				try {
					if (fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
//System.out.println("Express Project - nature found");
 						fIn_express_project = true;

   					String current_directory = null;
						current_directory = ExpressProjectTempPropertyPage.getTempFileLocation(fProject);


	 					String java_directory = current_directory;
	 					if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
	 					java_directory += "java";
	 				} else {
	  				java_directory +=  File.separator + "java";
	 				}

					File f_java_dir = new File(java_directory);
					if (f_java_dir.exists()) {
						enabled = true;
					} else {
						enabled = false;
					}

//					enabled = true;






					} else {
//System.out.println("Express Project - not express project - nature NOT found");

							// non-express project
							// checking working directory
	 						fIn_express_project = false;
							fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
							if (fWorking_directory == null ) {
								enabled = false;
							} else {
//								String classes_str = fWorking_directory + File.separator + "classes";
								String java_str = fWorking_directory + File.separator + "java";
//								File classes_dir = new File(classes_str);
								File java_dir = new File(java_str);
//								if (classes_dir.exists()) {
								if (java_dir.exists()) {
									enabled = true;
								} else {
									enabled = false;
								}
						// check if classes already present
							}

					}
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					ExpressCompilerPlugin.log(e1);
//					ExpressCompilerPlugin.console(e1.toString());
//					System.out.println("ExportToJarAction - Exception when checking project nature:" + e1);
					e1.printStackTrace();
				}

				
				
				
				
			} else {
				// just a file - check working directory
					
				if (isExternalExpressFileOpen()) {
				
				
					fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
					if (fWorking_directory == null ) {
						enabled = false;
					} else {
						// check if classes already present
						String classes_str = fWorking_directory + File.separator + "java";
//						String classes_str = fWorking_directory + File.separator + "classes";
						File classes_dir = new File(classes_str);
						if (classes_dir.exists()) {
							enabled = true;
						} else {
							enabled = false;
						}
					} // fWorking is not NULL
				} // isExternalExpressFileOpen
				
			}
		} else {
			   if (isExternalExpressFileOpen()) {

					fWorking_directory = ExpressCompilerUtils.getWorkingDirectory();
					if (fWorking_directory == null ) {
						enabled = false;
					} else {
						// check if classes already present
//						String classes_str = fWorking_directory + File.separator + "classes";
						String classes_str = fWorking_directory + File.separator + "java";
						File classes_dir = new File(classes_str);
						if (classes_dir.exists()) {
							enabled = true;
						} else {
							enabled = false;
						}
					}
				   
				   
			   } else {
					enabled = false;
			   
			   }
		}

		ExpressCompilerPlugin.getDefault().setExportToJarAction(action);
    
    // it is really enabled only if java directory is present - that indicates that express has been successfully compiled
    // it could be done in a different way - working only with ExpressCompilerRepo.sdai file, as it is the only input that is needed. 
		action.setEnabled(enabled);
    
  }

  public void run(IAction action) {
    // TODO Auto-generated method stub

//	    IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();
//	    help.setHelp(action, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    help.setHelp(action, "net.jsdai.express_compiler.actions.ExportToJarAction" + ".ExportJarContextId");
//	    help.displayHelp(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".ExportJarContextId");
//	    IContext context;	    

	    
		Shell shell = ExpressCompilerPlugin.getDefault().getShell();
	
	//  	Shell shell = null;
	  
		String path = null;
		path = fProject.getProject().getLocation().toOSString();
		
		FileDialog dialog = new FileDialog(shell, SWT.SINGLE);
//		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
	    dialog.setFilterPath(path);
	    dialog.setFileName(fProject.getName() + ".jar");
	    dialog.setFilterExtensions(new String[] {"*.jar"});
	    String name = dialog.open();
	    if (!name.endsWith(".jar")) {
	    	name = name + ".jar";
	    }
    	fDestination = name;

		
//		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doRun(monitor);
				} catch (CoreException e) {
					ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			(new ProgressMonitorDialog(null)).run(true, true, op);
		} catch (InterruptedException e) {
			ExpressCompilerPlugin.log(e);
System.out.println("Export to jar CANCELED: " + e);
			return;
		} catch (InvocationTargetException e) {
			ExpressCompilerPlugin.log(e);
			Throwable realException = e.getTargetException();
//			System.out.println("ExportToJarAction excption: " + e);
//			System.out.println("ExportToJarAction real excption: " + realException);
			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return;
		}


    
  }

	void doRun(IProgressMonitor monitor) throws CoreException {

	
		monitor.beginTask("Exporting JSDAI library to jar", 12);
		


		String jar_source_path = null;

// and create a jar
// monitor.worked(1);
// monitor.subTask("creating the jar");

	if (fIn_express_project) {	

//		String s_delete_temp_on_exit = "_no_value_";
//		String s_temp_location = "_no_value_";
//		String s_temp_location_system = "_no_value_";
//		String s_temp_location_eclipse = "_no_value_";
//		String s_temp_location_project = "_no_value_";

		try {

			jar_source_path =
				new File(ExpressProjectTempPropertyPage.getTempFileLocation(fProject), "classes").getCanonicalPath();
//	  	s_delete_temp_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteOnExit"));

		} catch (CoreException e1) {
			ExpressCompilerPlugin.log(e1);
			System.out.println("ExportToJarAction - Exception - Problems with reading of persistent properties: " + e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			ExpressCompilerPlugin.log(e1);
			System.out.println("ExportToJarAction - Exception - Problems with reading of persistent properties: " + e1);
			e1.printStackTrace();
		}
		




//		String x_temp_location =  fProject_comments.substring(fProject_comments.indexOf("T)")+2, fProject_comments.indexOf("(T"));
//		String x_temp_location =  s_temp_location;
	
//		if (x_temp_location.equalsIgnoreCase("DEFAULT")) {
		// could be handled the same way as other cases
	
		
		
	} else {
		// in working_directory
//		jar_source_path = fWorking_directory + File.separator + "classes"; 
		if (fWorking_directory.endsWith("\\") || fWorking_directory.endsWith("/")) {
			jar_source_path = fWorking_directory  + "classes"; 
		} else {
			jar_source_path = fWorking_directory + File.separator + "classes"; 
		}
	}
//    jar_source_path = jar_source_folder.getLocation().toOSString();
// String jar_destination_path = jar_destination_folder.getLocation().toOSString();
String jar_destination_path = fDestination;

//String project_name = the_project.getName();
//String jar_name = jar_destination_path + File.separator+ project_name + ".jar";

//String jar_name = fDestination;
try {
	MakingJar.makeJar(jar_source_path, jar_destination_path, null);
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	ExpressCompilerPlugin.log(e);
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	ExpressCompilerPlugin.log(e);
//	ExpressCompilerPlugin.console("See .log file in .metadata");
}

		try {
	
			fProject.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (Exception e) {
			ExpressCompilerPlugin.log(e);
			e.printStackTrace();
        }
	

	
	}


	
	boolean isExternalExpressFileOpen() {
//		ITextEditor editor = getTextEditor();

		
		

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
	
	
	
	
	
	
	
}

