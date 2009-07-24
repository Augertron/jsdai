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
import java.util.List;
import java.util.Properties;

import jsdai.util.UtilMonitor;
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

public class ValidateAllAction extends Action implements IWorkbenchWindowActionDelegate {
	

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

  boolean flag_in_express_project = false;
  boolean flag_mim_xim = true;
  boolean flag_mim = true;
  boolean flag_xim = false;
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
		fStepFile = null;
		fXimFile = null;
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
						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
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
					fXimFile = fProject.getLocation().toString()	+ File.separator + "_temporary_xim.pf"; 				

/*		
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
						if(fileName.endsWith(".p21") || fileName.endsWith(".stp") || fileName.endsWith(".pf")) {
							if(fStepFile == null) {
								fStepFile = file.getLocation().toOSString();
								fXimFile = fStepFile.substring(0,fStepFile.lastIndexOf(".")) + "_XIM.stp";
							} else {
								fStepFile = "";
								fXimFile = "";
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

	  System.out.println("<><02>Running validation, validation plugin: " + ValidationPlugin.class);
	    
		Shell shell = ValidationPlugin.getDefault().getShell();
		
	
	//  	Shell shell = null;
	  
		String path = null;
//		path = fProject.getProject().getLocation().toOSString();


    // currently express_project and mim_xim are two opposite states, but other logic perhaps could be possible as well
		flag_in_express_project = false;
		flag_mim_xim = true;
		flag_mim = true;
		flag_xim = false;


// disabling the special handling of express projects - lets have the same MIM XIM validation in express projects as well		
/*
		try {
			if (fProject.isAccessible() && fProject.hasNature("net.jsdai.express_compiler.expressNature")) {
//				flag_in_express_project = true;
				flag_in_express_project = false;
//				flag_mim_xim = false;
				flag_mim_xim = true;
//				flag_mim = false;
				flag_mim = true;
				flag_xim = false;
			}
		} catch (CoreException e1) {
			// so this is not a valid express project, nothing else is needed
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
*/

		if (flag_in_express_project) {

    	fDestination = fProject.getProject().getLocation().toOSString();
    	if (fDestination.endsWith("\\") || fDestination.endsWith("/")) {
	    	fDestination += fProject.getName() + ".jar";
    	} else {
	    	fDestination += File.separator + fProject.getName() + ".jar";
				
    	}
System.out.println("lib - EXPRESS PROJECT: " + fDestination);

		}
//		if (false) {
    else if (flag_mim_xim) {
      	// use jsdai_xim_full.jar in net.jsdai.xim.jlib plugin	

			fDestination = CommonUtils.getClassPath(Platform.getBundle("net.jsdai.xim.jlib"),"jsdai_xim_full.jar");

System.out.println("lib - XIM-MIM: " + fDestination);

// experiment - try to convert to xim

/*

       String stepFileName = fStepFile;
       String sdaireposDirectory = "G:\\REPOSITORIES";

 		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);
  		try {
			ValidationPlugin.log("<CONVERSION>00-PRE", 1);
			SdaiSession.setSessionProperties(jsdaiProperties);
			ValidationPlugin.log("<CONVERSION>00-AFTER", 1);
		} catch (SdaiException e1) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>00-E", 1);
			ValidationPlugin.log(e1);
			e1.printStackTrace();
		}

  		SdaiSession session = null;
		SdaiTransaction transaction = null;
		SdaiRepository repo = null;
		try {
			ValidationPlugin.log("<CONVERSION>01-PRE", 1);
			session = SdaiSession.openSession();
			ValidationPlugin.log("<CONVERSION>01-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>01-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		Importer importer = new Importer();
		try {
			ValidationPlugin.log("<CONVERSION>02-PRE", 1);
			transaction = session.startTransactionReadWriteAccess();
			ValidationPlugin.log("<CONVERSION>02-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>02-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
//		SdaiTransaction transaction = importer.startTransactionReadWriteAccess(session);
		try {
			ValidationPlugin.log("<CONVERSION>03-PRE", 1);
			repo = session.importClearTextEncoding("", stepFileName, null);
			ValidationPlugin.log("<CONVERSION>03-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>03-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
			ValidationPlugin.log("<CONVERSION>04-PRE", 1);
			importer.runImport(repo);
			ValidationPlugin.log("<CONVERSION>04-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>04-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		// transaction.endTransactionAccessAbort();
		// probably need to commit to export
		try {
			ValidationPlugin.log("<CONVERSION>05-PRE", 1);
			transaction.commit();
			ValidationPlugin.log("<CONVERSION>05-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>05-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
			ValidationPlugin.log("<CONVERSION>06-PRE", 1);
			repo.exportClearTextEncoding(stepFileName+"_");
			ValidationPlugin.log("<CONVERSION>06-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>06-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
			ValidationPlugin.log("<CONVERSION>07-PRE", 1);
			repo.closeRepository();
			ValidationPlugin.log("<CONVERSION>07-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>07-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
			ValidationPlugin.log("<CONVERSION>08-PRE", 1);
			repo.deleteRepository();
			ValidationPlugin.log("<CONVERSION>08-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>08-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}
		try {
			ValidationPlugin.log("<CONVERSION>09-PRE", 1);
			session.closeSession();
			ValidationPlugin.log("<CONVERSION>09-AFTER", 1);
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			ValidationPlugin.log("<CONVERSION>09-E", 1);
			ValidationPlugin.log(e);
			e.printStackTrace();
		}

*/



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
//ValidationPlugin.log("label: " + edlabel, 1);
       
 //		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
//System.out.println("<><07>Running validation");
					doRunValidation(monitor);
				} catch (CoreException e) {
//					ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		MessageBox memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		if (flag_mim)
			memoryBox.setMessage("MIM Validation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		else if (flag_xim)
			memoryBox.setMessage("XIM Validation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
		else
			memoryBox.setMessage("Validation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
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
			ValidationPlugin.log(realException);
//			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
		}

		CommonUtils.printResultToConsole(fValidateOutput, fPage, fOutput);

		// let's put mim-xim conversion here ---------------------------------------------------

		flag_exception = 0;
		
		op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
//System.out.println("<><07>Running validation");
					doRunMim2Xim(monitor);
				} catch (CoreException e) {
//					ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		memoryBox.setMessage("MIM-XIM conversion needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
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
			ValidationPlugin.log(realException);
//			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			fErrorBox = new MessageBox(ValidationPlugin.getDefault().getShell(), SWT.ICON_ERROR | SWT.OK);
			fErrorBox.setMessage("See .log for details");
			fErrorBox.setText("MIM-XIM conversion encountered a problem");
			fErrorBox.open();
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
//		} catch (SdaiException e) {
//			fErrorBox = new MessageBox(ValidationPlugin.getDefault().getShell(), SWT.ICON_ERROR | SWT.OK);
//			fErrorBox.setMessage("MIM-XIM conversion encountered a SdaiException");
//			fErrorBox.setText("See .log for details");
//			fErrorBox.open();
//			return;
		}

		if (flag_exception > 0) {
		    ValidationPlugin.log("exception in: " + flag_exception, 1);
			fErrorBox = new MessageBox(ValidationPlugin.getDefault().getShell(), SWT.ICON_ERROR | SWT.YES | SWT.NO);
			fErrorBox.setText("MIM-XIM conversion problem");
			fErrorBox.setMessage("" + fSdaiException + "\n\n\t\tSee .log for details\n\n\tContinue with XIM validation anyway?");
			int result = fErrorBox.open(); // yes - 64, no - 128
//			 PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.express_compiler.p21_editor.P21Editor");
			 PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.step_editor.editor.P21Editor");
			if (result > 64) {
			   return;
			}
		}
//		 PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.express_compiler.p21_editor.P21Editor");
		 PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.stp", "net.jsdai.step_editor.editor.P21Editor");
		
		// end of mim-xim conversion ----------------------------------------------------
    // and let's put xim validation here --------------------------------------------
    flag_xim = true;
    flag_mim = false;
    fValidationCount = 1;
    
		op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
//System.out.println("<><07>Running validation");
					doRunValidation(monitor);
				} catch (CoreException e) {
//					ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		memoryBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		memoryBox.setMessage("Validation needs more memory. Please restart Eclipse with more memory specified in eclipse.ini file");
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
			ValidationPlugin.log(realException);
//			realException.printStackTrace();
//			ExpressCompilerPlugin.log(realException);
//			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return;
		} catch (OutOfMemoryError e) {
			memoryBox.open();
			return;
		}

		

    // end of xim validation		
		
//System.out.println("<3>fValidateOutput: " + fValidateOutput);
			//ValidationPlugin.log("<3>kuku-bebe: " + fValidateOutput, 1);
//		CommonUtils.printResultToConsole(fValidateOutput, fPage, fOutput);
	if (flag_xim)
		CommonUtils.printResultToConsole(fValidateOutput_xim, fPage, fOutput);
			//ValidationPlugin.log("<5>kuku-bebe: " + fValidateOutput, 1);

    
  }

	void doRunValidation(IProgressMonitor monitor) throws CoreException, InterruptedException {

	 if (!flag_mim_xim && fValidationCount > 0) return; 
		
//		monitor.beginTask("Validating a step file", IProgressMonitor.UNKNOWN);
		


    String current_directory;

//	if (fIn_express_project) {	
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
				}
				current_time = System.currentTimeMillis();
				elapsed_time = (current_time - start_time) / 1000;
			
      
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
			if (flag_xim)
				createMarkers(validateOutput, fXimFile, monitor);
			else
				createMarkers(validateOutput, fStepFile, monitor);

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


    // String sdaireposDirectory = "G:\\REPOSITORIES";
    String sdaireposDirectory = fWorking_directory; // should be created by the previous mim validation
 		Properties jsdaiProperties = new Properties();
  	jsdaiProperties.setProperty("repositories", sdaireposDirectory);

  	jsdaiProperties.setProperty("mapping.schema.IDA_STEP_SCHEMA_XIM","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
  	jsdaiProperties.setProperty("mapping.schema.IDA_STEP_AIM_SCHEMA","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
		jsdaiProperties.setProperty("mapping.schema.AUTOMOTIVE_DESIGN","IDA_STEP_SCHEMA_XIM_MAPPING_DATA");
		jsdaiProperties.setProperty("jsdai.SIda_step_schema_xim","AC*;AI*;AP*;ASS*;AU*;B*;C*;D*;E*;F*;G*;H*;IDA_STEP_AIM*;ISO*;IN*;J*;K*;L*;M*;N*;O*;P*;Q*;R*;S*;T*;U*;V*;W*;X*;Y*;Z*;");


  		try {
//			ValidationPlugin.log("<CONVERSION>00-PRE", 1);
			SdaiSession.setSessionProperties(jsdaiProperties);
//			ValidationPlugin.log("<CONVERSION>00-AFTER", 1);
		} catch (SdaiException e1) {
			// TODO Auto-generated catch block
            fSdaiException = e1;
            flag_exception = 1; 
			ValidationPlugin.log("<CONVERSION>00-E", 1);
			ValidationPlugin.log(e1);
			e1.printStackTrace();
		}

 		SdaiSession session = null;
		SdaiTransaction transaction = null;
		SdaiRepository repo = null;
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

/*
		static final class UtilMonitorImpl implements UtilMonitor {
			String message;
//			long l_count;
//			long l_counter;
			int count;
			int counter;
			private Thread parentThread;
			
			public UtilMonitorImpl(Thread parentThread) {
				this.message = null;
				this.count = -1;
				this.counter = -1;
//				this.l_count = -1;
//				this.l_counter = -1;
				int step = 1;
				this.parentThread = parentThread;
			}

			public void subTask(String message, long count, long counter) {
				synchronized (this) {
					this.message = message;
					// progress monitor in eclipse allows only int values, so we have to convert long to int
					if (count > Integer.MAX_VALUE) {
						step = count/Integer.MAX_VALUE;
						if (count%Integer.MAX_VALUE > 0) {
							step++;
						}
						this.count = (int)count/step;
						this.counter = (int)counter/step;
					} else {
						this.count = (int)count;
						this.counter = (int)counter;
					}
				}
				parentThread.interrupt();
			}

			public void worked(int value) {
			}
		}

*/

/*
		static final class UtilMonitorImpl_prev implements UtilMonitor {
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

/*
		static final class UtilMonitorImpl implements UtilMonitor {
			String message;
			int count = -1;
			int counter = -1;
			private Thread parentThread;
			
			public UtilMonitorImpl(Thread parentThread) {
				this.message = null;
				this.parentThread = parentThread;
			}

			public void subTask(String message) {
				synchronized (this) {
					this.message = message;
//			    ValidationPlugin.log("UtilMonitor: " + message, 1);
					int index1 = message.indexOf("<");
					int index2 = message.indexOf(">");
//			    ValidationPlugin.log("UtilMonitor - index1: " + index1, 1);
//			    ValidationPlugin.log("UtilMonitor - index2: " + index2, 1);
					if (index1 > 0) {
				  	this.message = message.substring(0,index1);
//  			    ValidationPlugin.log("UtilMonitor - message: " + this.message, 1);
//				    ValidationPlugin.log("UtilMonitor - count: " + message.substring(index1,index2), 1);
				  	count = (new Integer(message.substring(index1+1,index2))).intValue();
				  	if (index2 < message.length()) {
//				    ValidationPlugin.log("UtilMonitor - counter: " + message.substring(index2), 1);
					  	counter = (new Integer(message.substring(index2+1))).intValue();
				  	}
				  } else
				  if(index1 == 0) {
				  	this.message = "";
				  	count = (new Integer(message.substring(index1+1,index2))).intValue();
				  } else {
				  	this.message = null;
				  }
				}
				parentThread.interrupt();
			}

			public void worked(int value) {
			}
		}

		static final class UtilMonitorImpl_OLD implements UtilMonitor {
			String message;
			private Thread parentThread;
			
			public UtilMonitorImpl_OLD(Thread parentThread) {
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
*/

}

