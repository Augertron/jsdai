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

package jsdai.express_g.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jsdai.common.CommonPlugin;
import jsdai.common.utils.IsolatedRunnableThread;
//import jsdai.express_doc.SdaieditPlugin;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.runtime.RuntimePlugin;
//import jsdai.tools.ExpressXmlIndex;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;

import jsdai.common.utils.*;
import jsdai.express_g.SdaieditPlugin;

/**
 * @author Mantas Balnys
 * 
 */
public class ExportConformanceTables extends Wizard implements IExportWizard {
	private IStructuredSelection selection = null;
//	private IWorkbench workbench = null;
	
	private ExportConformanceTablesSettingPage exportConformanceTablesSettingPage = null;

 	static IProject fProject = null;
	
	/**
	 * 
	 */
	public ExportConformanceTables() {
		super();
		IDialogSettings workbenchSettings = SdaieditPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("RepositoryFileWizard");//$NON-NLS-1$
		if(section == null)
			section = workbenchSettings.addNewSection("RepositoryFileWizard");//$NON-NLS-1$
		setDialogSettings(section);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		super.addPages();

		exportConformanceTablesSettingPage = new ExportConformanceTablesSettingPage(ResourcesPlugin.getWorkspace().getRoot().getLocation(), "ConformanceTablesExport");
		exportConformanceTablesSettingPage.setTitle("Create Conformance Tables");
//			page_from.setDescription("Select Express Data file to export its contents to html documentation.");
		addPage(exportConformanceTablesSettingPage);

//			page_to.setDescription("Select directory to export documentation.");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = false;
		Runnable op =
			new Runnable(exportConformanceTablesSettingPage.getDestinationDirectory(),
					exportConformanceTablesSettingPage.getSourceFileNameCss(), 
					exportConformanceTablesSettingPage.getSourceFileNameArm(), 
					exportConformanceTablesSettingPage.getSourceFileNameMim(), 
					exportConformanceTablesSettingPage.isGenerateJavaDocPart());
		try {
			getContainer().run(true, false, op);
			ok = op.isOk();
			try {
				IPath path = new Path(exportConformanceTablesSettingPage.getDestinationDirectory());
				IResource file = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(path);
				if (file != null) {
					file.refreshLocal(IResource.DEPTH_INFINITE, null);
				} // else - destination location is not in eclipse workspace, therefore no refreshing can be done
			} catch (Throwable t) {
//				t.printStackTrace();
//					System.out.println("problems with ExpressDoc 1, see log: " + t);
					SdaieditPlugin.log("problems with conformance table export 1: " + t,1);				
					SdaieditPlugin.log(t);				
			}
		} catch (InterruptedException e) {
//System.err.println("Export to html CANCELED: " + e);
			MessageDialog.openError(getShell(), "Export Conformance Tables Error", "Interupted by user");
//					System.out.println("problems with ExpressDoc export 2, see log: " + e);
					// e2.printStackTrace();
					SdaieditPlugin.log("Conformance table export interrupted by user 2",1);				
					//SdaieditPlugin.log(e);				
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
System.err.println("Export to conformance tables excption: " + e);
System.err.println("Export to conformance tables real excption: " + realException);
			String message = "Exporting to conformance tables failed, " + e.getLocalizedMessage();
			if (realException != null) {
				message = "Exporting to conformance tables failed, " + realException.getLocalizedMessage();
				if (realException instanceof SdaiException && 
						((SdaiException)realException).getErrorId() == SdaiException.RP_DUP) {
					message = "Exporting to conformance tables failed, file is opened. Close " + exportConformanceTablesSettingPage.getSourceFileNameCss() + " file and try again.";
				}
			}
			MessageDialog.openError(getShell(), "Export Conformance tables Error", message);
			System.out.println("problems with conformance tables export 4, see log: " + e);
					// e2.printStackTrace();
			SdaieditPlugin.log("problems with conformance tables export 4: " + e,1);				
			SdaieditPlugin.log(e);				
		}
		exportConformanceTablesSettingPage.saveWidgetValues();
		return ok;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish() {
		return exportConformanceTablesSettingPage != null && exportConformanceTablesSettingPage.determinePageCompletion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
//		this.workbench = workbench;
		setWindowTitle("Export");
		setNeedsProgressMonitor(true);
	}
	
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);

		String dictionaryFileName = null;
//		System.out.println("createPageControls - selection: " + selection);
		if(selection != null) {
			IProject project = null;
			List selResources = IDE.computeSelectedResources(selection);
			for(Iterator i = selResources.iterator(); i.hasNext();) {
				IResource resource = (IResource) i.next();
//				System.out.println("createPageControls - resource: " + resource);
				if (fProject == null) {
					// not sure why setting project is after continue - not reached
//					System.out.println("<I> attemptingto set fProject: " + resource);
					fProject = resource.getProject();
				}
				if (resource instanceof IFile) {
					IFile file = (IFile) resource;
//					System.out.println("createPageControls - file: " + file);
					String fext = file.getFileExtension();
					if (fext.equalsIgnoreCase("exd") || fext.equalsIgnoreCase("exg") || 
							fext.equalsIgnoreCase("sdai")) {
						if(dictionaryFileName == null) {
							dictionaryFileName = file.getLocation().toOSString();
						}
						continue;
					}
				}
				if(project == null) {
					project = resource.getProject();
				}
//				System.out.println("<O> attemptingto set fProject: " + resource + ", project: " + resource.getProject());
			}
			if(dictionaryFileName == null && project != null) {
				dictionaryFileName = project.getLocation().toOSString();
			}
		}
		if(dictionaryFileName != null) {
			//exportConformanceTablesSettingPage.setSourceFileNameArm(dictionaryFileName);
		}
	}

	private static class Runnable implements IRunnableWithProgress {
		private boolean ok = false;
		
		private String css_location = null;
		private String arm_location = null;
		private String mim_location = null;
		private String coarm_comim_destination_path = null;
		private boolean generateJavaDocPart;
		
		/**
		 * @param html_destination_path
		 * @param repo_location
		 */
		public Runnable(String coarm_comim_destination_path, String css_location, String arm_location, String mim_location, boolean generateJavaDocPart) {
			this.coarm_comim_destination_path = coarm_comim_destination_path;
			this.css_location = css_location;
			this.arm_location = arm_location;
			this.mim_location = mim_location;
			this.generateJavaDocPart = generateJavaDocPart;
		}

		public boolean isOk() {
			return ok;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//SdaieditPlugin.log("in run - 01",1);				
			monitor.beginTask("Generating conformance tables", IProgressMonitor.UNKNOWN);


//  this is when iso_db is called iso_db and is located in the root of the express_doc plugin
//			String iso_db_path = RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.express_doc"), "iso_db");
// new specification requires iso_db to be called document_reference.txt and be used when present in the corresponding project root,
// if not found, do not use it (btw, perhaps no need to check if present, if an unsuccessful attempt is made to use it from ExpressDoc itself - ok)

//			String iso_db_path = fProject.getLocation().toOSString() + File.separator + "document_reference.txt";


//      System.out.println("ISO_DB: " + iso_db_path);

      // have to have two additional inputs
//      String arm_repo_path = "G:\\ARM_MIM_REPOS\\armdd.sdai";  
//      String mim_repo_path = "G:\\ARM_MIM_REPOS\\mimdd.sdai"; 
      String arm_repo_path = arm_location;  
      String mim_repo_path = mim_location; 
			
//			String ccs_path = "G:\\_STEPMOD\\2009-03-19-sourceforge\\stepmod\\data\\application_protocols\\electronic_assembly_interconnect_and_packaging_design\\ccs.xml";   // must be a real path to a stepmod
			String ccs_path = css_location;
      
      // for now let's have a single output directory and add only different files there
      String coarm_path = coarm_comim_destination_path  + File.separator + "coarm_new.xml";   
      String comim_path = coarm_comim_destination_path  + File.separator + "comim_new.xml";
      String stdout_path = coarm_comim_destination_path  + File.separator + "conformance_table_generator_output.txt";
      String stderr_path = coarm_comim_destination_path  + File.separator + "conformance_table_generator_errors.txt";
      
			String [] args;

			args = new String[10];
				

			int ii = 0;

			args[ii++] = "-arm_repo";    
			args[ii++] = arm_repo_path;  // exd or sdai file
			args[ii++] = "-mim_repo";
			args[ii++] = mim_repo_path;  // exd or sdai file
			args[ii++] = "-ccs";
			args[ii++] = ccs_path;       // the input - must be in stepmod with apppropriate accompanying files
			args[ii++] = "-coarm";
			args[ii++] = coarm_path;     // output - the new coarm file, may be different than the one next to ccs file
			args[ii++] = "-comim";
			args[ii++] = comim_path;     // output - the new comim file, may be different than the one next to ccs file

//			args[ii++] = "-stdout";      			
//			args[ii++] = stdout_path;    // path for log, may be redirected to console
//			args[ii++] = "-stderr";      			
//			args[ii++] = stderr_path;    // path for error log, may be redirected to console

 
      String [] args2 = new String[2];
      args2[0] = stdout_path;
      args2[1] = stderr_path;


			try {
				try {
				Properties prop = new Properties();
				File repoDir = SdaieditPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
				if (!repoDir.exists()) repoDir.mkdirs();
//System.out.println("<>JSDAI-PROPERTIES: " + repoDir.getAbsolutePath());
				prop.setProperty("repositories", repoDir.getAbsolutePath());
				SdaiSession.setSessionProperties(prop);
//SdaieditPlugin.log("in run - 03 - after repodir",1);				
				} catch (SdaiException sex) {
//					SdaieditPlugin.log(,1);
					System.out.println("problems with conformance tables export 5, see log: " + sex);
					// e2.printStackTrace();
					SdaieditPlugin.log("problems with conformance tables export 5: " + sex,1);				
					SdaieditPlugin.log(sex);				
				};
				


       	 String [] exceptions = null;
       	 

	    	 	File[] classPathJars = {
			  	    new File(CommonUtils.getClassPath(
	    						Platform.getBundle("net.jsdai.tools"), "jsdai_tools.jar")),
//	    				new File(getClassPath(
//	    						SdaieditPlugin.getDefault().getBundle(), "jsdai_xml_index.jar")),
	    				new File(getClassPath(
	    						RuntimePlugin.getDefault().getBundle(), "jsdai_runtime.jar")),
	    				new File(getClassPath(
	    						Platform.getBundle("net.jsdai.ext_dict_lib"), "SExtended_dictionary_schema.zip"))
						// new File(jar_path),
	    			};

// SdaieditPlugin.log("in run - 06 - after classpaths",1);				

//				ClassLoader loader = new JarFileClassLoader(...);

//	    	 ClassLoader loader =

				try {

//					System.out.println("classPathJars: " + classPathJars);					
                    for (int ihi = 0; ihi < classPathJars.length; ihi++) {
                    	
//    					System.out.println("classPathJars - i: " + ihi + " - " + classPathJars[ihi]);					
                    }


    	IsolatedRunnableThread expressdocThread = null;

//		File repoDir = SdaieditPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
		String repoDirStr = SdaieditPlugin.getDefault().getStateLocation().append("sdairepos").toOSString();
		// if (!repoDir.exists()) repoDir.mkdirs();
    	
	    	 	expressdocThread =


	    	 		IsolatedRunnableThread.newInstance("jsdai.tools.ConformanceOptionsGenerator", "initAsRunnable",
//	    	 		IsolatedRunnableThread.newInstance("jsdai.express_g.tools.ConformanceOptionsGenerator", "initAsRunnable",
	    	 				new Class[] { String.class, String[].class, String[].class },
	    	 				new Object[] { repoDirStr, args, args2},
	    	 				classPathJars, null);
// SdaieditPlugin.log("in run - 07 - before thread start",1);				

	    		expressdocThread.start();
// SdaieditPlugin.log("in run - 08 - after thread start",1);				

		if (true) {

//					p.waitFor();

			boolean is_cancel = false;
//			int exit_code = -555;
			for (;;) {
				if(!Thread.interrupted()) {
						try {
							expressdocThread.join(1000);
						} catch (InterruptedException e) {
							//System.out.println("problems with ExpressDoc 2, see log: " + e2);
							// e2.printStackTrace();
							SdaieditPlugin.log("Conformance tables export interrupted by user 1", 1);				
						}
						if(!expressdocThread.isAlive()) {
							break;
						}
				}	
//				is_cancel = monitor.isCanceled();
				if (is_cancel) {
//					fValidateOutput += "Validation canceled";
						expressdocThread.interrupt();
					throw (new InterruptedException());
				}
			}	// for	
		} // if true (OS)




				} catch (Exception e2) {
					System.out.println("problems with Conformance tables export 6, see log: " + e2);
					// e2.printStackTrace();
					SdaieditPlugin.log("problems with Conformance tables  export 6: " + e2,1);				
					SdaieditPlugin.log(e2);				
				}

// -------- end of using JarFileClassLoader ---------------------------

//				ExpressDoc.main(args);

				ok = true;
//			} catch (SdaiException e) {
			} catch (Exception e) {
				System.out.println("problems with Conformance tables export 3, see log: " + e);
//				e.printStackTrace();
				SdaieditPlugin.log("problems with Conformance tables export  3: " + e,1);				
				SdaieditPlugin.log(e);				
				throw new InvocationTargetException(e);
			}
			
			
			monitor.done();

		}
		
		
		
	}
	
	static String getClassPath(Bundle bundle, String jarFile) {
		try {
			URL classpath = Platform.asLocalURL(bundle.getEntry(jarFile));
			String classpath_string = classpath.getFile().toString();

		if (Platform.getOS().equals("win32")) {
			classpath_string = classpath_string.substring(1);
		}
			return classpath_string;
		} catch (IOException e) {
				// ExpressCompilerPlugin.log(e);
				// e.printStackTrace();
				System.out.println("problems with Conformance tables export 7, see log: " + e);
				// e2.printStackTrace();
				SdaieditPlugin.log("problems with Conformance tables export 7: " + e,1);				
				SdaieditPlugin.log(e);				
				// SdaieditPlugin.log(e.toString(),1);				
				return null;
		}
	}
	
	
}
