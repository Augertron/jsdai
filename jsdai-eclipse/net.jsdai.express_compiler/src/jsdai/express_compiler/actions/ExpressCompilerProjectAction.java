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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import jsdai.common.utils.CommonUtils;
import jsdai.expressCompiler.ECMonitor;
import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.editor.ExpressCompilerMessageParser;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;
import jsdai.express_compiler.preferences.ExpressPreferences;
import jsdai.express_compiler.properties.ExpressProjectTempPropertyPage;
import jsdai.express_compiler.utils.ExpressCompilerRepo;
import jsdai.express_compiler.utils.ExpressCompilerUtils;
import jsdai.express_compiler.utils.IsolatedRunnableThread;
import jsdai.express_compiler_core.ExpressCompilerCorePlugin;
import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.internal.compiler.batch.Main;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class ExpressCompilerProjectAction implements IWorkbenchWindowActionDelegate {

//	private static ExpressCompilerProjectAction fgInstance = new ExpressCompilerProjectAction();
//	IWorkbenchPage fPage;
//	String fCompilerOutput;
	String fCompilerProcessErrors;
	String fJavacErrors;
	
	IWorkbenchPart fPart;
	IWorkbenchWindow fWindow;
	IWorkbenchPage fPage;
	String fCompilerOutput;
	String fOutput;
	
	boolean fJavacOutOfMemoryFlag;	
	
	ISelection fSelection;
	IAction fAction;;
	IProject fProject;
	
	//String fLicense;
	boolean fIn_express_project;
	boolean fRecursiveFlagGlobal;
	boolean fCreateJar;
	boolean fNoJava;
	boolean fUseExclude;
	boolean fOriginalCase;
	boolean fSwitchStepmod;
	boolean fSwitchArm;
	boolean fSwitchMim;
	boolean fUseInclude;
	boolean fEnableExpressions;
	boolean fSeparateProcess;
	boolean flagDeleteAllJSDAIMarkers;
	boolean flagDeleteAllExpressMarkers;
	
	boolean fUseCustomMemorySize;
//	private static boolean fUseIsolatedThread = true;
	private static boolean fUseIsolatedThread;
	int fInitialMemorySize;
	int fMaximumMemorySize;

	Shell fShell;
	
	// boolean fRecursiveExpress;

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {
		fWindow = window;
		
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		fPart = targetPart;
	}
	

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		fSelection = selection;
		fAction = action;

		boolean enabled = false;
		if (selection instanceof IStructuredSelection) {
//System.out.println(">> structured selection");
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object obj = sel.getFirstElement();
			if (obj instanceof IResource) {
// System.out.println(">> structured selection- 1st element = resource");
				enabled = true;
				fProject = ((IResource)obj).getProject();
			} else {
// System.out.println(">> structured selection- 1st element = NOT resource: " + selection);
				enabled = false;
			}
		} else
		if (selection instanceof ITextSelection) {
			// ITextSelection ts1 = (ITextSelection)selection;
//			TextSelection ts1 = (TextSelection)selection;

			IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

			IEditorPart active = page.getActiveEditor();
			IEditorInput active_input = null;
			if (active != null) {
				active_input = active.getEditorInput();
				if (active_input instanceof FileEditorInput) {
					FileEditorInput fedin = (FileEditorInput)active_input;
					IFile ifile = fedin.getFile();
//System.out.println("IFILE: : "  + ifile);
					IProject prj = ifile.getProject();
// System.out.println("IPROJECT: : "  + prj);
					if (prj != null) {
						enabled = true;
						fProject = prj;
					}
				} else {
					fProject = null;
					enabled = false;
					// JavaFileEditorInput
// System.out.println("active input : "  + active_input);
				}
			}

/*
			String e_active_name = active_input.getName();
System.out.println("THE MOST active editor input: " + active_input);
		}
		IEditorPart[] editors = page.getEditors();
		for (int k = 0; k < editors.length; k++) {
			IEditorPart current = editors[k];
//			EditorPart current_part = (EditorPart)editors[k];
			IEditorInput input = current.getEditorInput();
			String e_name = input.getName();
			// if the name ends with .exp, close it and open again
System.out.println("active editor input: " + e_name);
			if (e_name.endsWith(".exp")) {
//				page.closeEditor(current, true);
//				IEditorPart new_editor = page.openEditor(input, "net.jsdai.xdt.ui.editor.ExpressEditor", true);
//				if (active_input == input) {
//					active = new_editor;
//				}
				
			}
		}
//33333333333
*/

// System.out.println(">> Text selection: " + selection);
		} else {
// System.out.println(">> NOT structured selection: " + selection);

		}
		action.setEnabled(enabled);
		
		
		
//		action.setEnabled(true);

//		System.out.println("<PA>Selection changed: " + selection + ", action; " + action);		
	}
		
/*
	public void run(IAction action) {
		// TODO Auto-generated method stub
		action.setEnabled(false);
		System.out.println("<PA>Run: " + fSelection);
	
	}
*/
	
	public void run(IAction action) {

		//fLicense = CommonPlugin.getLicensingInfo().getLicenseKeyPath();

		fIn_express_project = false;
		fRecursiveFlagGlobal  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.RECURSIVE_COMPILE);

		fCreateJar  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.CREATE_JAR);
		fNoJava = !fCreateJar;
		fUseExclude  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_EXCLUDE);
		fUseInclude  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_INCLUDE);
		fSwitchStepmod  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_STEPMOD);
		fSwitchArm  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_ARM);
		fSwitchMim  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_MIM);
		fUseCustomMemorySize = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_CUSTOM_MEMORY_SIZE);
		fInitialMemorySize = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.INITIAL_MEMORY_SIZE);
		fMaximumMemorySize = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.MAXIMUM_MEMORY_SIZE);
		fEnableExpressions = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.ENABLE_EXPRESSIONS);
		fOriginalCase = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.ORIGINAL_CASE);
		fSeparateProcess = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SEPARATE_PROCESS);
		flagDeleteAllJSDAIMarkers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_JSDAI_MARKERS);
		flagDeleteAllExpressMarkers = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_EXPRESS_MARKERS);



		if(!PlatformUI.getWorkbench().saveAllEditors(true)) {
			return;
		}

		try {
			if (fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
//System.out.println("Express Project - nature found");
				fIn_express_project = true;
		


			} else {
//System.out.println("Express Project - not express project - nature NOT found");
//				ISelection current_selection = page.getSelection();
//				if (current_selection instanceof ITextSelection) {
					// the compilation has been invoked from an open editor, may be a mistake, ask for confirmation				
					// no, it is also dangerous when a non-express project is compiled after dragging files from it to inclusion list - it is still selected then
				if (true) {
					MessageBox confirmBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION | SWT.YES |SWT.NO);
					confirmBox.setMessage(ExpressCompilerPlugin.getResourceString("CompilerAction.confirmBoxContent"));
					confirmBox.setText(ExpressCompilerPlugin.getResourceString("CompilerAction.confirmBoxTitle"));
					int confirmation = confirmBox.open();
					// yes - 64
					// no - 128
// System.out.println(" confirmation: " + confirmation);
          if (confirmation == 128) {
          	return;
          }
					
				} 
			}
		} catch (CoreException e1) {
				ExpressCompilerPlugin.log(e1);
			// TODO Auto-generated catch block
//			System.out.println("Exception when checking project nature:" + e1);
//			e1.printStackTrace();
		}


/*
//		IFile fileToCompile = findCurrentExpressFile();
		IProjectDescription descrip = null;
		try {
			descrip = fProject.getDescription();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String prj_comments = descrip.getComment();
		if (prj_comments.indexOf("EXPRESS PROJECT") >= 0) {
			fIn_express_project = true;
		}
*/

		if (!fIn_express_project) {
			String current_directory = ExpressCompilerUtils.getWorkingDirectory();
			if (current_directory == null ) {
				MessageBox infoBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
				infoBox.setMessage(ExpressCompilerPlugin.getResourceString("CompilerAction.messageBoxContent"));
				infoBox.setText(ExpressCompilerPlugin.getResourceString("CompilerAction.messageBoxTitle"));
				infoBox.open();
				return;
			}
		}		
		
		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		fShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		fJavacOutOfMemoryFlag = false;
		MessageBox memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		memoryBox.setMessage("Express compilation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		memoryBox.setText("Out of memory error");
		try {

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException,  InterruptedException  {
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
//		try {
//			ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
//			pmd.run(true, true, op);
			(new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell())).run(true, true, op);
			showProblems();		

			
			//			(new ProgressMonitorDialog(null)).run(true, false, op); // CANCEL button disabled
//			(new ProgressMonitorDialog(null)).run(false, true, op); // the same thread - worse
		} catch (InterruptedException e) {
//System.out.println("Express Compilation CANCELED: " + e);
//				ExpressCompilerPlugin.log(e);
//			return;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			ExpressCompilerPlugin.log(realException);
			ExpressCompilerPlugin.console(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());

//				MessageBox errBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
//				errBox.setMessage("exception: " + realException);
//				errBox.setText("InvokationTargetException when compiling");
//				errBox.open();

			return;
		} catch (OutOfMemoryError e) {
					memoryBox.open();
					return;
		} finally {
			ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
		}

		if (fJavacOutOfMemoryFlag) {
//System.out.println("<4> printing javac out of error message");
			memoryBox.open();
		}
		
		CommonUtils.printResultToConsole(fCompilerOutput, fPage, fOutput);
//		ExpressCompilerUtils.printResultInConsole(fCompilerOutput, fPage);
	
	    if (fCompilerProcessErrors != null) {
	    	if (fCompilerProcessErrors.length() > 0) {
	    		// probably an exception occured
	    		if (fCompilerProcessErrors.indexOf("OutOfMemoryError") >= 0) {
	    			// let's inform the user about this problem
					MessageBox ecMemoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
					if (fUseIsolatedThread) {
						ecMemoryBox.setMessage("Express compilation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
					} else {
						ecMemoryBox.setMessage("Express compiler needs more memory to compile this project. Please specify a larger memory size in the project properties / compiler settings");
					}
					ecMemoryBox.setText("Out of memory error");
					ecMemoryBox.open();

	    		} else {
	    			// still some kind of exception, probably
					MessageBox problemBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
					problemBox.setMessage("Please read the \"Express Compiler errors\" section in the console");
					problemBox.setText("A problem while compiling the project");
					problemBox.open();
	    		}
	    	}
	    } else
		
	    if (fJavacErrors != null) {
	    	if (fJavacErrors.length() > 0) {
	    		// probably an exception occured
	    		if (fJavacErrors.indexOf("OutOfMemoryError") >= 0) {
	    			// let's inform the user about this problem
					MessageBox javacMemoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
					javacMemoryBox.setMessage("Eclipse java compiler needs more memory to compile the generated java files for this project. Please restart Eclipse with more memory specified in eclipse.ini file");
					javacMemoryBox.setText("Out of memory error");
					javacMemoryBox.open();

	    		} else {
	    			// still some kind of exception, probably
					MessageBox problemBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK);
					problemBox.setMessage("Please read the \"Java errors\" section in the console");
					problemBox.setText("A problem while compiling java files");
					problemBox.open();
	    		}
	    	}
	    }


//			showProblems();		
	}


	


		private void doRun(IProgressMonitor monitor) throws CoreException, InterruptedException  {
	  	

	 	

			boolean in_project = true;
			boolean in_express_project = false;


			
			// old stuff
//			String x_project_location = null;
			String x_temp_location = null;
			String x_repo_location = null;

			// new stuff
			String sx_project_location = null;
			String sx_express_file_location = null;
			String sx_short_name_location = null;
			String sx_complex_entity_location = null;
			String sx_temp_location = null;
			String sx_repo_location = null;
			boolean bx_is_default_express_file_location = false;



			if (in_project) {
				// see if it is express project, if so, get all the locations, etc.
				// if not - use working directory
				
				// NO LONGER USED - very stupid temp implementation that stores stuff in the comment of the project description

					if (fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
//	System.out.println("Express Project - nature found");
						in_express_project = true;
						/*
							 what do we need here?
							 location of express files
							 location of short names
							 location of complex entities
							 location of temp directory to put generated stuff
							 location of ExpressCompilerRepo
							 let's try getting them
						*/
//						String s_defaultExpressFileLocationFieldValue = "_no_value_";
						String s_customExpressFileLocationFieldValue = "_no_value_";;
						String s_is_default = "_no_value_";;

//						String s_defaultShortNameLocationFieldValue = "_no_value_";
						String s_customShortNameLocationFieldValue = "_no_value_";;
						String s_is_default_shortNameLocation = "_no_value_";;

//						String s_defaultComplexEntityLocationFieldValue = "_no_value_";
						String s_customComplexEntityLocationFieldValue = "_no_value_";;
						String s_is_default_complexEntityLocation = "_no_value_";;

//						String s_temp_location_type = "_no_value_";
//						String s_delete_temp_on_exit = "_no_value_";
						String s_temp_location = "_no_value_";
//						String s_temp_location_system = "_no_value_";
//						String s_temp_location_eclipse = "_no_value_";
//						String s_temp_location_project = "_no_value_";

//						String s_is_default_repo_location = "_no_value_";
//						String s_delete_repo_on_exit = "_no_value_";
						String s_repo_location = "_no_value_";
//						String s_repo_location_default = "_no_value_";

						// I will check if not null
						String s_input_type = null;
						String s_use_exclude = null;
						String s_create_jar = null;

						String s_switch_stepmod = null;
						String s_switch_arm = null;
						String s_switch_mim = null;					
						
						String s_memory_specify	 = null;					
						String s_memory_initial  = null;
						String s_memory_max = null;

						String s_enable_expressions = null;
						String s_original_case = null;
						String s_separate_process = null;

						
						try {
						    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);

							s_is_default = prefs.get("fDefaultExpressFileLocation", "true").toLowerCase();
							if("true".equals(s_is_default)) {
								IFolder projectExpressFilesLocation = fProject.getFolder("Express files");
								s_customExpressFileLocationFieldValue = projectExpressFilesLocation.getLocation().toOSString();
							} else {
								s_customExpressFileLocationFieldValue = prefs.get("expressFileLocation", "");
							}

						  s_is_default_shortNameLocation = prefs.get("fDefaultShortNameLocation", "true").toLowerCase();
							if("true".equals(s_is_default_shortNameLocation)) {
								IFolder projectShortNamesLocation = fProject.getFolder("Short names");
								s_customShortNameLocationFieldValue  = projectShortNamesLocation.getLocation().toOSString();
							} else {
								s_customShortNameLocationFieldValue  = prefs.get("shortNameLocation", "");
							}

							s_is_default_complexEntityLocation = prefs.get("fDefaultComplexEntityLocation", "true").toLowerCase();
							if("true".equals(s_is_default_complexEntityLocation)) {
								IFolder projectComplexEntitiesLocation = fProject.getFolder("Complex entities");
								s_customComplexEntityLocationFieldValue  = projectComplexEntitiesLocation.getLocation().toOSString();
							} else {
								s_customComplexEntityLocationFieldValue  = prefs.get("complexEntityLocation", "");
							}

							s_temp_location = ExpressProjectTempPropertyPage.getTempFileLocation(fProject);
//						  s_temp_location_type = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fTempLocation"));
//						  s_delete_temp_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteOnExit"));

							s_repo_location = new File(s_temp_location, "ExpressCompilerRepo").getCanonicalPath();
//						  s_is_default_repo_location = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDefaultRepoLocation"));
//						  s_delete_repo_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteRepoOnExit"));

							
							// override global settings with project-specific settings
				

							/*
									fSwitchStepmod
									fSwitchArm
									fSwitchMim
	
									fUseCustomMemorySize
									fInitialMemorySize
									fMaximumMemorySize
							*/


							// default/list/flat/recurse -  affects two flags - fUseInclude, fRecursiveFlagGlobal
						  s_input_type = prefs.get("fInputType", "default");
							
							// default/yes/no - affects fUseExclude
						  s_use_exclude = prefs.get("fUseExclude", "default");

							// default/yes/no - affects fNoJava (and fCreateJar)
						  s_create_jar = prefs.get("fCreateJar", "default");


							s_enable_expressions = prefs.get("fEnableExpressions", "default");
							s_original_case = prefs.get("fOriginalCase", "default");
							s_separate_process = prefs.get("fSeparateProcess", "default");

							s_switch_stepmod = prefs.get("fSwitchStepmod", "default");
							s_switch_arm = prefs.get("fSwitchArm", "default");
							s_switch_mim = prefs.get("fSwitchMim", "default");

							s_memory_specify = prefs.get("fMemorySpecify", null);
							s_memory_initial = prefs.get("fMemoryInitial", null);
							s_memory_max = prefs.get("fMemoryMax", null);


						} catch (CoreException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						} catch (IOException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						}



              // ------------- memory stuff -----------------------------


		if (s_memory_specify != null) {
			if (s_memory_specify.equalsIgnoreCase("yes")) {
				fUseCustomMemorySize = true;
				// need to get the sizes
		    Integer int_fMemorySizeInitial = null;
		    Integer int_fMemorySizeMax = null;
		    try {
		    	if (s_memory_initial != null) {
		    		int_fMemorySizeInitial = Integer.valueOf(s_memory_initial);
		  		}
		  		if (s_memory_max != null) {
		  			int_fMemorySizeMax = Integer.valueOf(s_memory_max);
		    	}
		    } catch (NumberFormatException e) {
					fUseCustomMemorySize = false;
				}
		   	if (int_fMemorySizeInitial != null) {
		   		fInitialMemorySize =  int_fMemorySizeInitial.intValue();
				} else {
					fUseCustomMemorySize = false;
				}
	    	if (int_fMemorySizeMax != null) {
		     	fMaximumMemorySize =  int_fMemorySizeMax.intValue();
	     	} else {
					fUseCustomMemorySize = false;
	      }
			} else 
			if (s_memory_specify.equalsIgnoreCase("no")) {
				fUseCustomMemorySize = false;
			}		
		}




              // ------------- end of memory stuff ----------------------

							if (s_separate_process != null) {
								if (s_separate_process.equalsIgnoreCase("yes")) {
									fSeparateProcess = true;
								} else 
								if (s_separate_process.equalsIgnoreCase("no")) {
									fSeparateProcess = false;
								}
							}
//System.out.println("Invoking in a separate process: " + fSeparateProcess);
							fUseIsolatedThread = !fSeparateProcess; 

							if (s_enable_expressions != null) {
								if (s_enable_expressions.equalsIgnoreCase("yes")) {
									fEnableExpressions = true;
								} else 
								if (s_enable_expressions.equalsIgnoreCase("no")) {
									fEnableExpressions = false;
								}
							}

							if (s_original_case != null) {
								if (s_original_case.equalsIgnoreCase("yes")) {
									fOriginalCase = true;
								} else 
								if (s_original_case.equalsIgnoreCase("no")) {
									fOriginalCase = false;
								}
							}


							if (s_switch_stepmod != null) {
								if (s_switch_stepmod.equalsIgnoreCase("yes")) {
									fSwitchStepmod = true;
								} else 
								if (s_switch_stepmod.equalsIgnoreCase("no")) {
									fSwitchStepmod = false;
								}
							}

							if (s_switch_arm != null) {
								if (s_switch_arm.equalsIgnoreCase("yes")) {
									fSwitchArm = true;
								} else 
								if (s_switch_arm.equalsIgnoreCase("no")) {
									fSwitchArm = false;
								}
							}

							if (s_switch_mim != null) {
								if (s_switch_mim.equalsIgnoreCase("yes")) {
									fSwitchMim = true;
								} else 
								if (s_switch_mim.equalsIgnoreCase("no")) {
									fSwitchMim = false;
								}		
							}



							/*
				
								+ fRecursiveFlagGlobal
								+ fCreateJar
								+ fNoJava = !fCreateJar;
								+ fUseExclude
								+ fUseInclude
							*/
						
						if (s_input_type != null) {
							// default - already have default values, so just ignore
							if (s_input_type.equalsIgnoreCase("list")) {
								fUseInclude = true;
								fRecursiveFlagGlobal = false; // does not matter
							} else
							if (s_input_type.equalsIgnoreCase("flat")) {
								fUseInclude = false;
								fRecursiveFlagGlobal = false;
							} else
							if (s_input_type.equalsIgnoreCase("recurse")) {
								fUseInclude = false;
								fRecursiveFlagGlobal = true;
							}
						}
						if (s_create_jar != null) {
							// do nothing if default - already have its value	
							if (s_create_jar.equalsIgnoreCase("yes")) {
								fCreateJar = true;
								fNoJava = false;
							} else
							if (s_create_jar.equalsIgnoreCase("no")) {
								fCreateJar = false;
								fNoJava = true;
							}
						}

						if (s_use_exclude != null) {
							// default already have, see if yes or no
							if (s_use_exclude.equalsIgnoreCase("yes")) {
								fUseExclude = true;
							} else
							if (s_use_exclude.equalsIgnoreCase("no")) {
								fUseExclude = false;
							}
						}

					 s_repo_location  = s_temp_location;
						if (s_temp_location.endsWith("\\") || s_temp_location.endsWith("/")) {
							s_repo_location += "ExpressCompilerRepo";
						} else {
					   	s_repo_location +=  File.separator + "ExpressCompilerRepo";
						}
/*
				File custom_temp_dir = new File(custom_temp_location);
	  	  custom_temp_dir.mkdirs();
		  ExpressCompilerUtils.verifyPath(custom_temp_dir, false);
*/


						sx_project_location = fProject.getLocation().toOSString();
						sx_express_file_location = s_customExpressFileLocationFieldValue;
						sx_short_name_location = s_customShortNameLocationFieldValue;
						sx_complex_entity_location = s_customComplexEntityLocationFieldValue;
						sx_temp_location = s_temp_location;
						sx_repo_location = s_repo_location;
            bx_is_default_express_file_location = true;
            if (s_is_default.equalsIgnoreCase("false")) {
            	bx_is_default_express_file_location = false;
            }

//System.out.println("compiling X project - project location: " + sx_project_location);
//System.out.println("compiling X project - express file location: " + sx_express_file_location);
//System.out.println("compiling X project - short name location: " + sx_short_name_location);
//System.out.println("compiling X project - complex entity location: " + sx_complex_entity_location);
//System.out.println("compiling X project - temp location: " + sx_temp_location);
//System.out.println("compiling X project - repo location: " + sx_repo_location);

					
//					x_project_location = sx_project_location;
					x_temp_location = sx_temp_location;
					x_repo_location = sx_repo_location;


					} else {
//	System.out.println("Express Project - not express project - nature NOT found");
					// a project, but not an express project, use working directory
					}


/*
				
				IProjectDescription descrip = null;
				try {
					descrip = fProject.getDescription();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String prj_comments = descrip.getComment();
				if (prj_comments.indexOf("EXPRESS PROJECT") >= 0) {
					in_express_project = true;
					x_project_location = prj_comments.substring(prj_comments.indexOf("P)")+2, prj_comments.indexOf("(P"));
					x_temp_location = prj_comments.substring(prj_comments.indexOf("T)")+2, prj_comments.indexOf("(T"));
					x_repo_location = prj_comments.substring(prj_comments.indexOf("R)")+2, prj_comments.indexOf("(R"));

//	System.out.println("Express Project");
//	System.out.println("project location: " + x_project_location);
//	System.out.println("temp location: " + x_temp_location);
//	System.out.println("repo location: " + x_repo_location);
					
					// that is not all, now we need to process and transform locations:
					// default temp = project
					// default repo = temp
//					if (x_temp_location)

				} else {
				}
				
				
*/				
				
				
			} else {
				// not a project - an external express file - use working directory
			}



//			String last_segment = pathToCompile.lastSegment();

			Runtime r = Runtime.getRuntime();

			try {

				// String command = buildCommand(fileToCompile);
				// String command = "java -version";

				
//				String filePath = fileToCompile.getLocation().toOSString();
//				String filePath = pathToCompile.toOSString();

				
				String dirPath = null;
				if (in_express_project) {
//					IFolder express_src = fProject.getFolder("Express files");
//					dirPath = express_src.getLocation().toOSString();
					dirPath = sx_express_file_location;
				} else {
					dirPath = fProject.getLocation().toOSString();
				}

				String current_directory = null;
				String sdaireposDirectory = null;
				if (in_express_project) {
					// still better copy ExpressCompilerRepo to its location, we do not support incremental for now
					// make temp the current directory

//					if (x_temp_location.equalsIgnoreCase("DEFAULT")) {
					if (false) {   // handle default as any other
						IFolder internal_src = fProject.getFolder("temp");
						current_directory = internal_src.getLocation().toOSString();
						if (x_repo_location.equalsIgnoreCase("DEFAULT")) {
							ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo");
						} else {
							ExpressCompilerRepo.copy(x_repo_location + "/ExpressCompilerRepo");
						}
					} else {
						// custom temp location - make it current directory
						current_directory = x_temp_location;
					  

					  File f_current_directory = new File(current_directory);
					  ExpressCompilerUtils.verifyPath(f_current_directory, false);
					  String custom_java_location = current_directory;
						if (custom_java_location.endsWith("\\") || custom_java_location.endsWith("/")) {
							custom_java_location += "java";
						} else {
					   	custom_java_location +=  File.separator + "java";
						}

		        File custom_java_dir = new File(custom_java_location);
		        custom_java_dir.mkdirs();
	          ExpressCompilerUtils.verifyPath(custom_java_dir, false);
	          String custom_class_location = current_directory;
						if (custom_class_location.endsWith("\\") || custom_class_location.endsWith("/")) {
	            custom_class_location += "classes";
						} else {
	           custom_class_location += File.separator + "classes";
						}
	          File custom_class_dir = new File(custom_class_location);
	          custom_class_dir.mkdirs();
	          ExpressCompilerUtils.verifyPath(custom_class_dir, false);

//						String jsdai_properties_contents = null;
						
						ExpressCompilerRepo.copy(x_repo_location);
						if(fUseIsolatedThread) {
							sdaireposDirectory = x_repo_location;
						} else {
							ExpressCompilerUtils.createJsdaiProperties(current_directory, x_repo_location);
						}
					}
				} else {
					// copy ExpressCompilerRepo to the working directory
					// make working directory the current directory
					// make the appropriate jsdai.properties file there

					// current_directory = ExpressCompilerPlugin.getDefault().getWorkingDirectory();

					current_directory = ExpressCompilerUtils.getWorkingDirectory();

					String xcr_destination = current_directory;
					if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
						xcr_destination += "ExpressCompilerRepo";
					} else {
						xcr_destination += File.separator + "ExpressCompilerRepo";
					}
//					ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo");
					ExpressCompilerRepo.copy(xcr_destination);
					if(fUseIsolatedThread) {
						sdaireposDirectory = xcr_destination;
					} else {
						ExpressCompilerUtils.createJsdaiProperties(current_directory, xcr_destination);
					}
				}


         String exl_list_abs = current_directory;
				 if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
			 		exl_list_abs += "list.exl";
	 			} else {
	  			exl_list_abs  +=  File.separator + "list.exl";
	 			}


				// runs the command
//				Process p = r.exec(command.toString(), new String[] {}, new File(fileFolderPath));
//	System.out.println("before exec, dir path " + dirPath);			

//	 Process p = r.exec("java -version", null, null);
//	Process p = r.exec("type " + fileToCompile.getName() + " > _log1", null, new File(fileFolderPath));

//	 String java_directory = "java";
	 String java_directory = current_directory;
	 if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
	 		java_directory += "java";
	 } else {
	  	java_directory +=  File.separator + "java";
	 }
	 // delete java_directory
	 ExpressCompilerUtils.deleteAllFilesAndDirectories(java_directory);


	 /*
	  *   let's get all the express files - 
	  *   we could use the list as input, so that the user could possibly
	  *   modify the list and remove some stuff?
	  *   
	  *   In any case, even if we use directory as an input,
	  *   we need all the IFile objects for the files 
	  *   to clear/set error markers
	  *   
	  *   The simplest case - take the whole project directory or
	  *   the whole Express files directory
	  *   
	  *   We could also take only selected express files,
	  *   but perhaps too complicated for the users
	  *   
	  */
	 
	 HashMap express_files = new HashMap();
	 
	 if (in_express_project) {
		 // take only files in Express files directory - non recursive
		 IFolder express_file_folder = null;
		 IContainer express_file_container = null;
		 /*
		 		assume here, that express files may be in the default location Express Files inside project,
		 		in the project itself, or in any other (even inner) subfolder in the project
		 		Try to get that folder from the path string alone
		 */
		 
			if (bx_is_default_express_file_location) {
				express_file_folder = fProject.getFolder("Express files");
			} else 
			if (sx_express_file_location.replace('\\','/').equalsIgnoreCase(fProject.getLocation().toString().replace('\\','/'))) {
				// the whole project itself
				// System.out.println("Express files = project itself");
//			express_file_folder = (IFolder)fProject.getRoot().getFolder(fProject.getName());
//			express_file_folder = (IFolder)fProject.getFolder(fProject.getName());
				express_file_container = (IContainer)fProject;
				express_file_folder = null;
//System.out.println("Location: " + fProject.getLocation());
//System.out.println("Parent: " + fProject.getParent());
//			express_file_folder = (IFolder)fProject.getFolder(fProject.getLocation());
//			express_file_folder = (IFolder)fProject.getParent().getFolder(fProject.getPath());

		
			} else {
				// see if inside the project = resource, or outside project losing the advantages of being resource, don't bother if in other projects, for now
				if (sx_express_file_location.replace('\\','/').startsWith(fProject.getLocation().toString().replace('\\','/'))) {
//				System.out.println("yes, it starts");
					String express_files_folder = sx_express_file_location.substring(fProject.getLocation().toString().length()+1, sx_express_file_location.length()); 
//				System.out.println("express subfolder: " + express_files_folder);
					express_file_folder = fProject.getFolder(express_files_folder);
				} else {
					// see if it is another Eclipse project, therefore may be a resource
				
					IWorkspace workspace = fProject.getWorkspace();
					IWorkspaceRoot root = workspace.getRoot();
					IPath express_location_path = new Path(sx_express_file_location);
					express_file_container = root.getContainerForLocation(express_location_path);
		
					// let's treat it as a non-resource location
//				System.out.println("no, it does not start");
//				System.out.println("custom: " + s_customExpressFileLocationFieldValue);
//				System.out.println("project: " + resource.getProject().getLocation().toString());
				
					// still no protection from non-resource cases. 

				}
			}
		 
		 
		 
		 
//		 getExpressFiles(fProject.getFolder("Express files"), express_files);

			if (fUseInclude) {
				String include_file_path = fProject.getLocation().toString() + File.separator + fProject.getName() + ".exl";		

				getExpressFilesFromList(include_file_path, express_files, exl_list_abs);
			} else {	

				if (express_file_folder == null) {
					if (fRecursiveFlagGlobal) {
						getExpressFilesRecursive(express_file_container, express_files);
					} else {
						getExpressFiles(express_file_container, express_files);
					}
			
				} else {
					if (fRecursiveFlagGlobal) {
						getExpressFiles(express_file_folder, express_files);
					} else {
						getExpressFilesRecursive(express_file_folder, express_files);
					}			
				}
			}
		} else {
			// non express project
	     // take all the express files in the whole project, recursive
			if (fRecursiveFlagGlobal) {
				getExpressFilesRecursive(fProject, express_files);
			} else {
				getExpressFiles(fProject, express_files);
			}
	 	}
	 
		
	 
	 
	String exec_string = null;
	if (in_express_project) {
//		exec_string = "java  -Xms128m -Xmx256m jsdai.expressCompiler.Main -eclipse -write_files -dir \"" + dirPath + "\" -binaries -index_file -java -out " + java_directory;
		exec_string = "java jsdai.expressCompiler.Main -eclipse -write_files -dir \"" + dirPath + "\" -binaries -index_file -java -out " + java_directory;
	} else {
//		exec_string = "java  -Xms128m -Xmx256m jsdai.expressCompiler.Main -eclipse -write_files -dr \"" + dirPath + "\" -binaries -index_file -java -out " + java_directory;
		exec_string = "java  jsdai.expressCompiler.Main -eclipse -write_files -dr \"" + dirPath + "\" -binaries -index_file -java -out " + java_directory;
	}
//	System.out.println("exec string: " + exec_string);			
//	System.out.println("current_directory: " + current_directory);			


			
//..........................
  


	    	
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
		
		//System.setProperty("jsdai.properties", jsdai_properties_path);





		    String env[] = new String[2]; 
			env[0] = "jsdai.properties=" + jsdai_properties_path;
			env[1] = "CLASSPATH=." + File.pathSeparator + classpath_string;
	    	 
//System.out.println(">jsdai.properties to exec>: " + env[0]);			
//System.out.println(">classpath to exec>: " + env[1]);			
			
		exec_string += " " + "-jsdaiproperties " + jsdai_properties_path; 



		

		String [] exec_strings;
		
		// see if havs complex entities
		
//		File the_complex_file = null;
		String complex_file_str = null;


//System.out.println("complex dir: " + sx_complex_entity_location);		
		File complex_dir =  null;
		if (in_express_project) {
			complex_dir = new File(sx_complex_entity_location);
  	  if (complex_dir.isDirectory()) {
    		String[] complex_files = complex_dir.list();
				
  	    for (int i = 0; i < complex_files.length; i++) {
				//if (comlex_files.length > 0) {
					String the_complex_file_str = complex_files[i];
					if (the_complex_file_str.endsWith(".ce")) {
						// the_complex_file = new File(complex_dir, the_complex_file_str);
						complex_file_str = the_complex_file_str;
						break;
					}
				}
			}
		}

		/*
			  put all the complex entities into one .ce file in temp directory
		*/	

		
		String complex__actual_name = fProject.getName() + ".ce";
		String complex__output_file = x_temp_location; // file in temp directory, let's say project_name.ce
	  if (x_temp_location.endsWith("\\") || x_temp_location.endsWith("/")) {
				complex__output_file += complex__actual_name;
		} else {
			complex__output_file += File.separator + complex__actual_name;
		}
		String complex__input_directory = sx_complex_entity_location; // complex entity directory
		String complex__file_mask = ".ce";
		ExpressCompilerUtils.makeSingleFile(complex__output_file, complex__input_directory, complex__file_mask);
	
		String monitor_exec_string = "";
		
String exclude_file_path = fProject.getLocation().toString() + File.separator + fProject.getName() + "_excluded.exl";		
String express_list_file = fProject.getLocation().toString() + File.separator + fProject.getName() + ".exl";		

//System.out.println("complex entities: " + complex_file_str);		
//		if (complex_file_str == null) {
		if (fUseIsolatedThread) {
			 exec_strings = new String[24];
		} else if (fUseCustomMemorySize) {
//		 exec_strings = new String[19];
		 exec_strings = new String[28];
		} else {
//		 exec_strings = new String[21];
		 exec_strings = new String[26];
		}
		int ii = 0;
		
		if (!fUseIsolatedThread) {
			exec_strings[ii++] = "java";
			// custom memory size
			if (fUseCustomMemorySize) {
				exec_strings[ii++] = "-Xms" + fInitialMemorySize + "M";
				exec_strings[ii++] = "-Xmx" + fMaximumMemorySize + "M";
			}

			exec_strings[ii++] = "jsdai.expressCompiler.Main";
		}

		exec_strings[ii++] = "-eclipse";
		exec_strings[ii++] = "-write_files";
//	if (in_express_project) {
		if (fUseInclude) {
			fUseExclude = false;
			exec_strings[ii++] = "-files";
//			exec_strings[ii++] = express_list_file;
			exec_strings[ii++] = exl_list_abs;
			monitor_exec_string += "Compiling express from the list input";
		} else {
			if (fRecursiveFlagGlobal) {
				exec_strings[ii++] = "-dr";
				monitor_exec_string += "Compiling express from the recursive directory input";
			} else {
				exec_strings[ii++] = "-dir";
				monitor_exec_string += "Compiling express from the directory input";
			}
			exec_strings[ii++] = dirPath;
		}
  if (fNoJava) {
		monitor_exec_string += ", parser only";
		exec_strings[ii++] = "-nothing";
		exec_strings[ii++] = "-nothing";
		exec_strings[ii++] = "-nothing";
//		exec_strings[ii++] = "-nothing";
//		exec_strings[ii++] = "-nothing";
// seems that -out is needed in order for the compiler to write the result files (should be fixed in the core)
		exec_strings[ii++] = "-out";
		exec_strings[ii++] = java_directory;
  } else {
		exec_strings[ii++] = "-binaries";
		exec_strings[ii++] = "-index_file";
		exec_strings[ii++] = "-java";
		exec_strings[ii++] = "-out";
		exec_strings[ii++] = java_directory;
}

		exec_strings[ii++] = "-jsdaiproperties";
		exec_strings[ii++] = jsdai_properties_path;
		if (fOriginalCase) {
			exec_strings[ii++] = "-original_case";
		} else {
			exec_strings[ii++] = "-nothing";
		}
		if (fUseExclude) {
			monitor_exec_string += ", with exclusion list";
			exec_strings[ii++] = "-exclude";
			exec_strings[ii++] = exclude_file_path;
		} else {
			exec_strings[ii++] = "-nothing";
			exec_strings[ii++] = "-nothing";
		}
		if (fSwitchStepmod) {
			exec_strings[ii++] = "-stepmod";
		} else {
			exec_strings[ii++] = "-nothing";
		}
		if (fSwitchArm) {
			exec_strings[ii++] = "-arm";
		} else {
			exec_strings[ii++] = "-nothing";
		}
		if (fSwitchMim) {
			exec_strings[ii++] = "-mim";
		} else {
			exec_strings[ii++] = "-nothing";
		}
		if (complex_file_str != null) {
			exec_strings[ii++] = "-complex";
			String final_complex_file_str = sx_complex_entity_location;
			if (sx_complex_entity_location.endsWith("\\") || sx_complex_entity_location.endsWith("/")) {
				final_complex_file_str += complex_file_str;
			} else {
				final_complex_file_str += File.separator + complex_file_str;
			}
			exec_strings[ii++] = complex__output_file;
//			exec_strings[ii++] = final_complex_file_str;
			
		} else {
			exec_strings[ii++] = "-nothing";
			exec_strings[ii++] = "-nothing";
		}

		exec_strings[ii++]= "-stdout";
		exec_strings[ii++]= new File(current_directory, "log_output").getAbsolutePath();
		exec_strings[ii++]= "-stderr";
		exec_strings[ii++]= new File(current_directory, "log_errors").getAbsolutePath();
		if (fEnableExpressions) {
			exec_strings[ii++]= "-inst";
		} else {
			monitor_exec_string += ", expressions disabled";
			exec_strings[ii++]= "-nothing";
		}
			
//...................................

			
			
  // fCompilerOutput = "Starting express compiler with the following arguments:\n";
  fCompilerOutput = "";
  // fCompilerOutput += "Exec string: " + exec_string + "\n";			
  // fCompilerOutput += "Environment[0]: " + env[0] + "\n";			
  // fCompilerOutput += "Environment[1]: " + env[1] + "\n";			
  // fCompilerOutput += "Current directory: " + current_directory + "\n\n";			
			

// try to delete these files, just in case there are old ones

				String output_file_path = current_directory; 
				String error_file_path =  current_directory;
				String temp_result_path = current_directory;
				
				if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
					output_file_path  += "log_output";
					error_file_path   += "log_errors";
					temp_result_path  += "temp_rezult";
				} else {
					output_file_path  += File.separator + "log_output";
					error_file_path   += File.separator + "log_errors";
					temp_result_path  += File.separator + "temp_rezult";
				}

    fOutput = output_file_path;
		

//   	    InputStream error_stream = new FileInputStream(error_file_path);
//				InputStream stream = new FileInputStream(output_file_path);

				File error_stream_file = new File(error_file_path);
				File output_stream_file = new File(output_file_path);
				File temp_result_file = new File(temp_result_path);

        error_stream_file.delete();
        output_stream_file.delete();
        temp_result_file.delete();

// also try to delete .exd and .jar files for the project

    if (in_express_project) {
				String old_exd_file = sx_project_location;
	 			String old_jar_file = sx_project_location;
	 			if (sx_project_location.endsWith("\\") || sx_project_location.endsWith("/")) {
			 		old_exd_file +=  fProject.getName() + ".exd";
			 		old_jar_file +=  fProject.getName() + ".jar";
				} else {
			 		old_exd_file += File.separator + fProject.getName() + ".exd";
			 		old_jar_file += File.separator + fProject.getName() + ".jar";
				}	
				File f_old_exd_file = new File(old_exd_file);
				File f_old_jar_file = new File(old_jar_file);
        f_old_exd_file.delete();
        f_old_jar_file.delete();

		}
			
//  fCompilerOutput += "Running the express compiler\n";
//	monitor.beginTask(monitor_exec_string, 10);


	boolean monitor_task_started = false;
	
	// an experiment to see if it is possible to move this inside the loop
	// yes, it is possible
	monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);

	IsolatedRunnableThread expressCompilerThread = null;
	Process p = null;
	ECMonitorImpl expressCompilerMonitor = new ECMonitorImpl(Thread.currentThread());
	if(fUseIsolatedThread) {
//		System.out.println("invoking in a separate Thread");
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
System.out.println("invoking by Exec");
		p = r.exec(exec_strings, env, new File(current_directory));
//				Process p = r.exec(exec_string, null, new File(current_directory));
	}


//if (!Platform.getOS().equals("win32")) {
		long start_time, current_time, elapsed_time = 0;
	    if (true) {

/*	
	
	try {
//System.out.println("Starting to wait");
		System.out.println("is canceled: " + monitor.isCanceled());
		p.waitFor();
		System.out.println("is now canceled: " + monitor.isCanceled());
		
//System.out.println("Waiting ends");
//		 int exit_value = p.exitValue();
//		 fCompilerOutput += "Exit value: " + exit_value + "\n";
	} catch (InterruptedException e1) {
System.out.println("Trying to CANCEL express compiler");
//	 throw (new InterruptedException());
fCompilerOutput += "Express compilation canceled";
		p.destroy();
  	//fCompilerOutput += "Exception while waiting for the process to end: " + e1 + "\n";
//  	fCompilerOutput += "Express compilation canceled";
		return;
//System.out.println("Waiting breaks");
//				ExpressCompilerPlugin.log(e1);
		// TODO Auto-generated catch block
//		e1.printStackTrace();
	}
*/

	
	
	int exit_code = -555;
//	int number_of_steps = 0;
	boolean is_cancel = false;

	start_time = System.currentTimeMillis();
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
						//number_of_steps++;
					} catch (InterruptedException e) {
					}

					try {

		/*			
						File temp_result_file2 = new File(temp_result_path);
						if (temp_result_file2.exists()) {
							break;
						} else {
							is_cancel = monitor.isCanceled();
							if (is_cancel) {
				  			  fCompilerOutput += "Express compilation canceled";
							  p.destroy();
							  // return;
							  throw (new InterruptedException());
							
							}
						}
						//if (ExpressCompilerUtils.hasCompilerEnded(current_directory)) break;
		*/
						exit_code = p.exitValue();
						if (exit_code != -555) break;

							
					} catch (IllegalThreadStateException e1) {
					}
				}
			}
			// not yet finished
//			monitor.worked(number_of_steps++);
			String subtaskMessage;
			synchronized (expressCompilerMonitor) {
				subtaskMessage = expressCompilerMonitor.message;
			}
			current_time = System.currentTimeMillis();
			elapsed_time = (current_time - start_time) / 1000;

	
	// an experiment to see if it is possible to move this inside the loop
  // yes, success
//	    if (monitor_task_started) {
			if (true) {
				monitor.setTaskName((subtaskMessage != null ? subtaskMessage : monitor_exec_string) + ": " + elapsed_time + "s");
			} else {
				monitor.beginTask(monitor_exec_string + ": 0s", IProgressMonitor.UNKNOWN);
				monitor_task_started = true;
				System.out.println("starting task - delayed");
			}
			is_cancel = monitor.isCanceled();
			if (is_cancel) {
				fCompilerOutput += "Express compilation canceled";
				if(fUseIsolatedThread) {
					expressCompilerThread.interrupt();
				} else {
					p.destroy();
				}
				// return;
				throw (new InterruptedException());
			}
			
		} // for
	} finally {
		Thread.interrupted();
		if(fUseIsolatedThread) {
			expressCompilerThread.close();
		}
	}

} // true (or OS type)

//		 int exit_value = p.exitValue();
//		 fCompilerOutput += "Exit value: " + exit_value + "\n";


//	System.out.println("exit value: " + exit_value);
				
			monitor.worked(1);	
			monitor.setTaskName(("Getting the express compiler messages"));


//				Process p = r.exec("java jsdai.expressCompiler.Main -express " + filePath + " -java", null, new File(fileFolderPath));
//				Process p = r.exec("java -version",  new String[] {}, new File(fileFolderPath));
//	System.out.println("after exec, process: " + p);			

				// gets the input stream to have the post-compile-time information



//   	    InputStream error_stream = p.getErrorStream();
//				InputStream stream = p.getInputStream();

/*
				String output_file_path = current_directory; 
				String error_file_path =  current_directory;
				
				if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
					output_file_path  += "log_output";
					error_file_path   += "log_errors";
				} else {
					output_file_path  += File.separator + "log_output";
					error_file_path   += File.separator + "log_errors";
				}
*/		

//   	    InputStream error_stream = new FileInputStream(error_file_path);
//				InputStream stream = new FileInputStream(output_file_path);

				error_stream_file = new File(error_file_path);
				output_stream_file = new File(output_file_path);
				InputStream error_stream = new FileInputStream(error_stream_file);
				InputStream stream = new FileInputStream(output_stream_file);



//	System.out.println("stream: " + ((BufferedInputStream)stream).available());			

	monitor.worked(1);
//	monitor.setTaskName(("getting string from stream"));

				// and get the string from it

//System.out.println("Error stream: " + ((FileInputStream)error_stream).available());

//System.out.println("Getting output string");
		String compilerOutput = ExpressCompilerUtils.getStringFromStreamAvailable(stream);
//		System.out.println("Getting error string");
	    String compilerProcessErrors = ExpressCompilerUtils.getStringFromStreamAvailable(error_stream);
//	    System.out.println("Errors: " + compilerProcessErrors);
//		System.out.println("Output: " + compilerOutput);			
		stream.close();
		error_stream.close();

// now deleting before running the compiler, so better leave them here, in case we want to read them for debugging purposes
//		error_stream_file.delete();
//		output_stream_file.delete();



				// prints out the information
	monitor.worked(1);
//	monitor.setTaskName(("practically nothing"));

//			printResultInConsole(compilerOutput);

//			fCompilerOutput = compilerOutput;

	
	    if (compilerProcessErrors.length() > 0) {
				fCompilerOutput += "--- Express Compiler errors ---\n\n";
				fCompilerOutput += compilerProcessErrors;
			}
			fCompilerOutput += "--- Express Compiler Output ---\n\n";
			fCompilerOutput += compilerOutput;
			fCompilerOutput += "\n--- End of Express Compiler Output ---\n";
			fCompilerOutput += "express compilation time: " + elapsed_time + " seconds\n";	
			fCompilerProcessErrors = compilerProcessErrors;
			



	monitor.worked(1);
	monitor.setTaskName(("Parsing the express compiler messages"));
			// parse the buffer to find the errors and create markers


// fCompilerOutput += "Before creating markers\n";

				createMarkers(compilerOutput, express_files);
// fCompilerOutput += "After creating markers\n";

	monitor.worked(1);

	//TODO prevent file no found exception: temp_rezult
	boolean no_errors = ExpressCompilerUtils.getCompilerExitCode(current_directory);

// fCompilerOutput += "After getting exit code\n";

//	System.out.println("compiler result: " + no_errors);

//	if ((!no_errors) || (fNoJava)) {

		//TODO prevent file not found exception: temp_schemas
		Vector compiled_schemas = ExpressCompilerUtils.getCompiledSchemas(current_directory);

// fCompilerOutput += "After getting compiled schemas\n";


	if ((!no_errors) || (compiled_schemas.size() < 1)) {

    // there are errors, do nothing else, but first disable export to html

		ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
	  ExpressCompilerUtils.deleteAllFilesAndDirectories(java_directory);
//System.out.println("disabling export to html action: " + ExpressCompilerPlugin.getDefault().getExportToHtmlAction());
		
	
	} else
	if (fNoJava) {
		// no errors, but NoJava option - in this case,  ExpressCompilerRepo created OK,
		// so we can create exd file and enable export to html, but do not compile java nor create jar

			
		ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
	  ExpressCompilerUtils.deleteAllFilesAndDirectories(java_directory);

    if (in_express_project) {


			// copy temp/ExpressCompilerRepo/ExpressCompilerRepo.sdai to project_directory/project_name.exd
			

			String sdai_file_path = current_directory;
		 	if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
			 sdai_file_path += "ExpressCompilerRepo" + File.separator + "ExpressCompilerRepo.sdai";
			} else {
			 sdai_file_path += File.separator + "ExpressCompilerRepo" + File.separator + "ExpressCompilerRepo.sdai";
			}
			String exd_file_path = sx_project_location;
		 	if (sx_project_location.endsWith("\\") || sx_project_location.endsWith("/")) {
			 exd_file_path +=  fProject.getName() + ".exd";
			} else {
			 exd_file_path += File.separator + fProject.getName() + ".exd";
			}	
			File source_file = new File(sdai_file_path);
			File destination_file = new File(exd_file_path);
			ExpressCompilerRepo.copyFile(source_file, destination_file);




//			ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo/ExpressCompilerRepo.sdai", sx_project_location);
		}


	} else {
		//  no errors and no NoJava, so enable everything, create exd, compile java and make jar	

//		Vector compiled_schemas = ExpressCompilerUtils.getCompiledSchemas(current_directory);
		

		ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(true);

    if (in_express_project) {


			// copy temp/ExpressCompilerRepo/ExpressCompilerRepo.sdai to project_directory/project_name.exd
			

			String sdai_file_path = current_directory;
		 	if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
			 sdai_file_path += "ExpressCompilerRepo" + File.separator + "ExpressCompilerRepo.sdai";
			} else {
			 sdai_file_path += File.separator + "ExpressCompilerRepo" + File.separator + "ExpressCompilerRepo.sdai";
			}
			String exd_file_path = sx_project_location;
		 	if (sx_project_location.endsWith("\\") || sx_project_location.endsWith("/")) {
			 exd_file_path +=  fProject.getName() + ".exd";
			} else {
			 exd_file_path += File.separator + fProject.getName() + ".exd";
			}	
			File source_file = new File(sdai_file_path);
			File destination_file = new File(exd_file_path);
// fCompilerOutput += "Before copying exd\n";
			ExpressCompilerRepo.copyFile(source_file, destination_file);




//			ExpressCompilerRepo.copy(current_directory + "/ExpressCompilerRepo/ExpressCompilerRepo.sdai", sx_project_location);
		}

		monitor.setTaskName(("Compiling the generated java files: 0s"));

// fCompilerOutput += "Java compiler - about to start on this thing\n";    

//	System.out.println("compiled_schemas: " + compiled_schemas);
		Iterator iter = compiled_schemas.iterator();
//	System.out.println("<01>before getRuntime");

//		Runtime rt = Runtime.getRuntime();

//	System.out.println("<02>after getRuntime: " + rt);

//	System.out.println("RuntimePlugin: ");
//	System.out.println("RuntimePlugin - default: " + RuntimePlugin.getDefault());
//	System.out.println("RuntimePlugin - bundle: " + RuntimePlugin.getDefault().getBundle());

		
		String  jsdai_runtime_classpath = ExpressCompilerUtils.getClassPath(RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar");
//	System.out.println("<03>jsdai_runtime_classpath: " + jsdai_runtime_classpath);
		String  jsdai_extended_dictionary_schema_classpath = ExpressCompilerUtils.getClassPath(Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip");
//	System.out.println("<04>jsdai_extended_dictionary_schema_classpath: " + jsdai_extended_dictionary_schema_classpath);
		
//	 for plugin project in development
		// perhaps no longer needed:
//		String jsdai_plugin_classpath = ExpressCompilerRepo.getPluginDirectory()+ "ExpressCompilerPlugin.jar";


		 
		String total_classpath; // E:\eclipse\runtime-workbench-workspace\RR1\classes;E:\eclipse\workspace\jsdai.express_compiler\bin
		String binary_file_name;
		
		String sourcepath = java_directory; 
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


		/*
				no longer java compiler in the loop, just give @file with sources to it
		*/
	
		
		String javac_out_path = current_directory;;
		String javac_err_path = current_directory;;
		String javac_source_file_path = current_directory;;
		if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
			javac_out_path  += "log_javac_output";
			javac_err_path  += "log_javac_errors";
			javac_source_file_path  += "java_files";
		} else {
			javac_out_path  += File.separator + "log_javac_output";
			javac_err_path  += File.separator + "log_javac_errors";
			javac_source_file_path  += File.separator + "java_files";
		}

// fCompilerOutput += "Java compiler - about to make source file\n";    

		ExpressCompilerUtils.makeSourceFile(sourcepath, javac_source_file_path);

// fCompilerOutput += "Java compiler - made source file\n";    

			total_classpath = classpath + 
							  File.pathSeparator + jsdai_runtime_classpath + 
							  File.pathSeparator +  jsdai_extended_dictionary_schema_classpath; 
//							  File.pathSeparator +  jsdai_extended_dictionary_schema_classpath + 
//							  File.pathSeparator +  jsdai_plugin_classpath;


		// compile(String commandLine, PrintWriter outWriter, PrintWriter errWriter) {

//		String commandLine = " -sourcepath " + sourcepath
//			+ " -classpath " + total_classpath + " -d " + outputdir
//			+ " " + sourcefiles;

		String commandLine[] = { 
				"-classpath",
				total_classpath,
				"-d",
				outputdir,
				"-warn:none",  // disable warnings
				"@" + javac_source_file_path
		};


		File javac_out_file = new File(javac_out_path);
		File javac_err_file = new File(javac_err_path);
        javac_out_file.delete();
        javac_err_file.delete();

		
		PrintWriter outWriter = ExpressCompilerUtils.getPrintWriter(javac_out_path);
		PrintWriter errWriter = ExpressCompilerUtils.getPrintWriter(javac_err_path);

		// JDTCompilerAdapter jdtadapter;		
		
		
		// Main.compile(commandLine, outWriter, errWriter);

// fCompilerOutput += "Java compiler class instance created\n";    
 	JavaCompiler jc = new JavaCompiler(commandLine, outWriter, errWriter);
// fCompilerOutput += "Java compiler thread created\n";    
    Thread jct = new Thread(jc);
// fCompilerOutput += "Java compiler about to start\n";    
    jct.start();

// fCompilerOutput += "Started java compiler\n";    
    
	  start_time = System.currentTimeMillis();
	  //long current_time;
	  //long elapsed_time;
	  boolean is_cancel = false;
	  elapsed_time = 0;
	  for (;;) {
//    	try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//			}
			jct.join(1000);
			if (!jct.isAlive()) break;

			current_time = System.currentTimeMillis();
			elapsed_time = (current_time - start_time) / 1000;
			monitor.setTaskName(("Compiling the generated java files: " + elapsed_time + " s"));
			is_cancel = monitor.isCanceled();
			if (is_cancel) {
  			  fCompilerOutput += "Java compilation canceled";
			  //jct.destroy(); // not implemented
  			  // jct.stop(); // deprecated
  			  jct.interrupt();
			  // return;
			  throw (new InterruptedException());
			}

			
		}

		javac_out_file = new File(javac_out_path);
		javac_err_file = new File(javac_err_path);

		InputStream javac_out_stream = new FileInputStream(javac_out_file);
		InputStream javac_err_stream = new FileInputStream(javac_err_file);

		String javacOutput = ExpressCompilerUtils.getStringFromStreamAvailable(javac_out_stream);
		String javacErrors = ExpressCompilerUtils.getStringFromStreamAvailable(javac_err_stream);
		javac_out_stream.close();
		javac_err_stream.close();

		if (javacErrors != null) {
			if (javacErrors.length() > 0) {
				fCompilerOutput += "--- Java errors  ---\n\n";
				fCompilerOutput += javacErrors;
			}
		}
		if (javacOutput != null) {
			if (javacOutput.length() > 0) {
				fCompilerOutput += "--- Java compiler output ---\n\n";
				fCompilerOutput += javacOutput;
			}
		}
		// fCompilerOutput += "\n--- End of Express Compiler Output ---\n";
		fCompilerOutput += "java compilation time: " + elapsed_time + " seconds\n";	

		fJavacErrors = javacErrors;

	    if (fJavacErrors != null) {
//System.out.println("<1> not null");
		if (fJavacErrors.length() > 0) {
//			System.out.println("<2> length: " + fJavacErrors.length());
	    		// probably an exception occured
	    		if (fJavacErrors.indexOf("OutOfMemoryError") >= 0) {
                    fJavacOutOfMemoryFlag = true;   		
//	    			System.out.println("<3> out of memory");
	    			// out of memory error occured
	    			ExpressCompilerPlugin.getDefault().getExportToJarAction().setEnabled(false);
// will not work in this thread
//	    			MessageBox memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);

//	    			MessageBox memoryBox = new MessageBox(fShell, SWT.ICON_ERROR | SWT.OK);
//	    			memoryBox.setMessage("Eclipse java compiler needs more memory to compile the generated java files for this project. Please restart Eclipse with more memory specified in eclipse.ini file");
//	    			memoryBox.setText("Out of memory error");
//	    			memoryBox.open();
	    			return;
	    		}
	    	}
	    }

		while (iter.hasNext()) {
			String schema_name = (String) iter.next();
//	System.out.println("current compiled schema: " + schema_name);

			String package_name = "S"
				+ schema_name.substring(0, 1).toUpperCase()
				+ schema_name.substring(1).toLowerCase();

			String sourcefiles = sourcepath + File.separator + "jsdai"
			+ File.separator + package_name + File.separator
			+ "*.java";

//			String javac_string = "javac -sourcepath " + sourcepath
//			+ " -classpath " + total_classpath + " -d " + outputdir
//			+ " " + sourcefiles;

			
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
			  


//			Process compileJob = null;
			
			
/*
 *  let's try the internal compiler
 *  
 *  
 */			
	
/*

		// compile(String commandLine, PrintWriter outWriter, PrintWriter errWriter) {
		String commandLine = " -sourcepath " + sourcepath
			+ " -classpath " + total_classpath + " -d " + outputdir
			+ " " + sourcefiles;
		File javac_out_file = new File(javac_out_path);
		File javac_err_file = new File(javac_err_path);
        javac_out_file.delete();
        javac_err_file.delete();

		
		PrintWriter outWriter = ExpressCompilerUtils.getPrintWriter(javac_out_path);
		PrintWriter errWriter = ExpressCompilerUtils.getPrintWriter(javac_err_path);

		JDTCompilerAdapter jdtadapter;		
		
		
		Main.compile(commandLine, outWriter, errWriter);
*/

			
// javac version		
/*		
			try {
				compileJob = rt.exec(javac_strings, null, null);
			} catch (IOException e) {
					ExpressCompilerPlugin.log(e);
					e.printStackTrace();
			}
*/




/*
			if (Platform.getOS().equals(Constants.OS_WIN32)) {
				try {
//	System.out.println("javac string: " + javac_string);
					compileJob = rt.exec(javac_string, null, null);
				} catch (IOException e) {
					ExpressCompilerPlugin.log(e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					compileJob = rt.exec(new String[] { "/bin/sh", "-c", javac_string }, null, null);
				} catch (IOException e) {
					ExpressCompilerPlugin.log(e);
				e.printStackTrace();
				}
			}
*/
		
/* javac version		
			try {
				compileJob.waitFor();
			} catch (InterruptedException e) {
				ExpressCompilerPlugin.log(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
			
		monitor.worked(1);
		monitor.setTaskName(("Copying the binary files"));


			binary_file_name = schema_name.toUpperCase() + "_DICTIONARY_DATA";

			String binary_file_source_path = sourcepath + File.separator + "jsdai" + File.separator + package_name + File.separator + binary_file_name;
			String binary_file_destination_path = classpath + File.separator + "jsdai" + File.separator + package_name + File.separator + binary_file_name;
			File source_binary_file = new File(binary_file_source_path);
			File destination_binary_file = new File(binary_file_destination_path);
			ExpressCompilerRepo.copyFile(source_binary_file, destination_binary_file);
		} // while - through java packages

		// now we could move repository.properties file to classes

		String repository_properties_source_path = sourcepath + File.separator + "jsdai" + File.separator + "repository.properties";
		String repository_properties_destination_path = classpath + File.separator + "jsdai" + File.separator + "repository.properties";
		File source_repository_properties = new File(repository_properties_source_path);
		File destination_repository_properties = new File(repository_properties_destination_path);
		if (source_repository_properties.exists()) {
			ExpressCompilerRepo.copyFile(source_repository_properties, destination_repository_properties);
		}

		// make jar now

		monitor.worked(1);

    if (in_express_project) {
			// creating jar only for express projects, perhaps should consider doing it for other projects as well,
			// and for non-projects also - into system/temp in that case

		monitor.setTaskName(("Creating the library jar"));
		
		// temp_location/classes
		String jar_source_path = current_directory;
		if (current_directory.endsWith("\\") || current_directory.endsWith("/")) {
		 jar_source_path += "classes";
		} else {
		 jar_source_path += File.separator + "classes";
		}

		// put in the root directory of the project with the same name as the project name
		String jar_destination_path = sx_project_location;
		 	if (sx_project_location.endsWith("\\") || sx_project_location.endsWith("/")) {
			 jar_destination_path +=  fProject.getName() + ".jar";
			} else {
			 jar_destination_path += File.separator + fProject.getName() + ".jar";
			}	


		try {
			MakingJar.makeJar(jar_source_path, jar_destination_path, null);
			// Create source archive as project-name.src.jar
			MakingJar.makeJar(new File(current_directory, "java").getCanonicalPath(),
					new File(sx_project_location, fProject.getName() + ".src.jar").getCanonicalPath(), ".+\\.java");
		} catch (FileNotFoundException e) {
			ExpressCompilerPlugin.log(e);
			e.printStackTrace();
		} catch (IOException e) {
			ExpressCompilerPlugin.log(e);
			e.printStackTrace();
		}
	}
	
	
		
		
		monitor.setTaskName(("Refreshing"));
	 } // if express compiler ended without errors

			// And refresh the compilation unit folder
				fProject.refreshLocal(IResource.DEPTH_ONE, null);
			} catch (IOException e) {
				ExpressCompilerPlugin.log(e);
				// $$$ should throw the exception again
//				System.err.println("Problem");
//				e.printStackTrace();
			}



//	/######################

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
	
	void getExpressFilesFromList(String list_file, HashMap express_files, String list_file2) throws CoreException {

		IWorkspace workspace = fProject.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
    
//    IPath root_path = root.getFullPath();
    IPath absolute_path_prefix_p = root.getLocation();
	String absolute_path_prefix = absolute_path_prefix_p.toOSString() + File.separatorChar;
//System.out.println("<>ROOT PREFIX: " + absolute_path_prefix);
//System.out.println("<>ROOT 2nd file: " + list_file2);

    try {
      FileWriter fw = new FileWriter(list_file2);
      FileReader fr = new FileReader(list_file);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br =  new BufferedReader(fr);
			for (;;) {
				String current_line = br.readLine();
				if (current_line == null) break;
				current_line = current_line.trim();
				if (current_line.equalsIgnoreCase("")) continue;
				if (current_line.startsWith("#")) continue;
        if (!current_line.endsWith(".exp")) continue;			
				current_line.replace('/', File.separatorChar).replace('\\', File.separatorChar);			
				current_line = absolute_path_prefix + current_line;
				bw.write(current_line+"\n");
			
				IPath current_line_path = new Path(current_line);
//				IContainer an_express_file_container = root.getContainerForLocation(current_line_path);
//        IFile current_line_file = (IFile)an_express_file_container;
					IFile current_line_file = root.getFileForLocation(current_line_path);
//				IFile current_line_file = root.getFile(current_line_path);
//System.out.println("IFile: " + current_line_file + " for IPath: " + current_line_path + " for path: " + current_line);
//				IPath key01 = current_line_file.getLocation();
//System.out.println("IPath: " + key01);
				
				String key = current_line_file.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//				String key = current_line.toLowerCase().replace('\\','$').replace('/','$');
				express_files.put(key, current_line_file);
			}
			bw.write("\n");
			bw.close();
			fw.close();			
		} catch (FileNotFoundException e) {
//      System.out.println("file " + list_file + " not found.");
			ExpressCompilerPlugin.log(e);
			
    } catch (IOException e) {
//      System.out.println("file " + list_file + " caused exception:");
			ExpressCompilerPlugin.log(e);
    }


		
	}
	
	void getExpressFilesFromListOLD(String list_file, HashMap express_files) throws CoreException {

		IWorkspace workspace = fProject.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

    try {
      FileReader fr = new FileReader(list_file);
			BufferedReader br =  new BufferedReader(fr);
			for (;;) {
				String current_line = br.readLine();
				if (current_line == null) break;
				current_line = current_line.trim();
				if (current_line.equalsIgnoreCase("")) continue;
				if (current_line.startsWith("#")) continue;
        if (!current_line.endsWith(".exp")) continue;			
				current_line.replace('/', File.separatorChar).replace('\\', File.separatorChar);			
				IPath current_line_path = new Path(current_line);
//				IContainer an_express_file_container = root.getContainerForLocation(current_line_path);
//        IFile current_line_file = (IFile)an_express_file_container;
					IFile current_line_file = root.getFileForLocation(current_line_path);
//				IFile current_line_file = root.getFile(current_line_path);
//System.out.println("IFile: " + current_line_file + " for IPath: " + current_line_path + " for path: " + current_line);
//				IPath key01 = current_line_file.getLocation();
//System.out.println("IPath: " + key01);
				
				String key = current_line_file.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//				String key = current_line.toLowerCase().replace('\\','$').replace('/','$');
				express_files.put(key, current_line_file);
			}
		} catch (FileNotFoundException e) {
//      System.out.println("file " + list_file + " not found.");
			ExpressCompilerPlugin.log(e);
			
    } catch (IOException e) {
//      System.out.println("file " + list_file + " caused exception:");
			ExpressCompilerPlugin.log(e);
    }


		
	}
	
	

		void getExpressFiles(IFolder folder, HashMap express_files) throws CoreException {

			if (folder == null) {
//System.out.println("non-resource (or from a different project) used for Express Files folder");			
				return;
			}
			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				IResource member = members[i];
				if (member instanceof IFile) {
					String ext = member.getFileExtension();
					if (ext != null) {
						if (ext.equalsIgnoreCase("exp")) {
							String key = member.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//System.out.println("KEY: (" + key + ")");
							express_files.put(key, member);
						}
					}
				}
			}
		}

		void getExpressFiles(IContainer folder, HashMap express_files) throws CoreException {

			if (folder == null) {
//System.out.println("non-resource (or from a different project) used for Express Files folder");			
				return;
			}
			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				IResource member = members[i];
				if (member instanceof IFile) {
					String ext = member.getFileExtension();
					if (ext != null) {
						if (ext.equalsIgnoreCase("exp")) {
							String key = member.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//System.out.println("KEY: (" + key + ")");
							express_files.put(key, member);
						}
					}
				}
			}
		}
		
		
		
		void getExpressFilesRecursive(IFolder folder, HashMap express_files) throws CoreException {

			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				IResource member = members[i];
				if (member instanceof IFile) {
					String ext = member.getFileExtension();
					if (ext != null) {
						if (ext.equalsIgnoreCase("exp")) {
							String key = member.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//System.out.println("KEY: (" + key + ")");
							express_files.put(key, member);
						}
					}
				} else
				if (member instanceof IFolder) {
					getExpressFilesRecursive((IFolder)member, express_files);
				}
			} // for
			
			
		}
		

		void getExpressFilesRecursive(IContainer folder, HashMap express_files) throws CoreException {

			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				IResource member = members[i];
				if (member instanceof IFile) {
					String ext = member.getFileExtension();
					if (ext != null) {
						if (ext.equalsIgnoreCase("exp")) {
							String key = member.getLocation().toOSString().toLowerCase().replace('\\','$').replace('/','$');
//System.out.println("KEY: (" + key + ")");
							express_files.put(key, member);
						}
					}
				} else
				if (member instanceof IFolder) {
					getExpressFilesRecursive((IFolder)member, express_files);
				}
			} // for
			
			
		}


		
		protected void createMarkers(String output, HashMap express_files) throws CoreException {
			// first delete all the previous markers

			Collection values = express_files.values();


// System.out.println("Markers-values: " + values.size() + ", values: " + values);
			Iterator iter = values.iterator();
			IFile exp_file = null;
			while(iter.hasNext()) {
				exp_file = (IFile)iter.next();
// System.out.println("Markers-value: " + exp_file);

// we now delete all the markers, but of our custom type
//				exp_file.deleteMarkers(IMarker.PROBLEM, false, 0);
				
			}

			/*
					delete old markers
					1) flagDeleteAllMarkers - delete all express and p21 markers
					2) if not flagDeleteAllMarkers:
						flagDeleteAllExpressMarkers - either delete all express markers or only those belonging to the project
			*/



			// remove all JSDAI Express Problem markers, from all the projects
			IWorkspace workspace = fProject.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IMarker[] xproblems = root.findMarkers("net.jsdai.express_compiler.expressproblem", true, IResource.DEPTH_INFINITE);
			String project_name = fProject.getName();
			
			for (int i = 0; i < xproblems.length; i++) {
					IMarker marker = xproblems[i];
//System.out.println("<<!!!>> found a marker");
					if (marker.exists()) { // this check may not be needed
//System.out.println("<<!!!>> found an existing marker, deleting");
						if (flagDeleteAllJSDAIMarkers || flagDeleteAllExpressMarkers) {
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

			if (flagDeleteAllJSDAIMarkers) {
				// delete also all p21 problem markers
				IMarker[] p21problems = root.findMarkers("net.jsdai.express_compiler.p21problem", true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < p21problems.length; i++) {
					IMarker marker = p21problems[i];
					if (marker.exists()) { // this check may not be needed
						marker.delete();
					}
				}
			}


			if (values.size() == 1)	{
				(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, exp_file, output, null);
			} else 
			if (values.size() > 1) {	
				(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, express_files, output);
			}
		}


		protected void createMarkersOld(String output, HashMap express_files) throws CoreException {
			// first delete all the previous markers

			Collection values = express_files.values();
// System.out.println("Markers-values: " + values.size() + ", values: " + values);
			Iterator iter = values.iterator();
			IFile exp_file = null;
			while(iter.hasNext()) {
				exp_file = (IFile)iter.next();
// System.out.println("Markers-value: " + exp_file);
				exp_file.deleteMarkers(IMarker.PROBLEM, false, 0);
				
			}
			//CompilerMessageParser lc_compilerMessageParser = getCompilerMessageParser();
			if (values.size() == 1)	{
				(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, exp_file, output, null);
			} else 
			if (values.size() > 1) {	
				(new ExpressCompilerMessageParser()).parseCompilerMessages(fProject, express_files, output);
			}
		}


		void showProblems() {

		IWorkbench workbench =PlatformUI.getWorkbench();
  		IWorkbenchWindow [] windows = workbench.getWorkbenchWindows();
  		boolean all_done = false;
  		for (int kw = 0; kw < windows.length; kw++) {
  			IWorkbenchWindow window = windows[kw];
//System.out.println("window: " + window);
  			IWorkbenchPage [] pages = window.getPages();
  			for (int kp = 0; kp < pages.length; kp++) {
  				IWorkbenchPage page = pages[kp];
//System.out.println("\tpage: " + page);
//  				page.showView(IPageLayout.ID_PROBLEM_VIEW);
  				IWorkbenchPart part = page.findView(IPageLayout.ID_PROBLEM_VIEW);
//System.out.println("\t\tview: " + part);
  				if (part != null) {
  				  //page.bringToTop(part);
  				  //page.showView(IPageLayout.ID_PROBLEM_VIEW);
  				  page.activate(part);
  					all_done = true;
  				  break;
  				}
  			}
  			if (all_done) {
  			  break;
  			}
  		}
}
	

		static final class ECMonitorImpl implements ECMonitor {
			String message;
			private Thread parentThread;
			
			public ECMonitorImpl(Thread parentThread) {
				this.message = null;
				this.parentThread = parentThread;
			}

			public void subTask(String message) {
				synchronized (this) {
					this.message = message;
				}
				parentThread.interrupt();
			}

			public void worked(int value) {
			}
		}

		private static class JavaCompiler implements Runnable {
			String command_line[];
			PrintWriter out;
			PrintWriter err;
			
			JavaCompiler(String command_line[], PrintWriter out, PrintWriter err) {
				this.command_line = command_line;
				this.out = out;
				this.err = err;
		  }
		 
		  public void run() {
//				try {
			  new Main(out, err, false).compile(command_line);
		/*
				} catch (OutOfMemoryError e) {
					MessageBox memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
					memoryBox.setMessage("Eclipse java compiler needs more memory to compile the generated java files for this project. Please restart Eclipse with more memory specified in eclipse.ini file");
					memoryBox.setText("Out of memory error");
					memoryBox.open();
		  	}
		*/
		  }
		}
}
