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

package jsdai.express_doc.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jsdai.express_doc.ExpressDocPlugin;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.tools.ExpressDoc;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;

/**
 * @author Mantas Balnys
 * 
 */
public class ExportDocumentation extends Wizard implements IExportWizard {
	private IStructuredSelection selection = null;
//	private IWorkbench workbench = null;
	
	private ExpressDocSettingPage expressDocSettingPage = null;

	/**
	 * 
	 */
	public ExportDocumentation() {
		super();
		IDialogSettings workbenchSettings = ExpressDocPlugin.getDefault().getDialogSettings();
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

		expressDocSettingPage = new ExpressDocSettingPage(ResourcesPlugin.getWorkspace().getRoot().getLocation(), "expressDocExport");
		expressDocSettingPage.setTitle("Create Express Data documentation");
//			page_from.setDescription("Select Express Data file to export its contents to html documentation.");
		addPage(expressDocSettingPage);

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
			new Runnable(expressDocSettingPage.getDestinationDirectory(),
					expressDocSettingPage.getSourceFileName(), expressDocSettingPage.isGenerateJavaDocPart());
		try {
			getContainer().run(true, false, op);
			ok = op.isOk();
			try {
				IPath path = new Path(expressDocSettingPage.getDestinationDirectory());
				IResource file = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(path);
				file.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} catch (InterruptedException e) {
//System.err.println("Export to html CANCELED: " + e);
			MessageDialog.openError(getShell(), "Export Documentation Error", "Interupted by user");
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
//System.err.println("ExportToHtmlAction excption: " + e);
//System.err.println("ExportToHtmlAction real excption: " + realException);
			String message = "Exporting failed, " + e.getLocalizedMessage();
			if (realException != null) {
				message = "Exporting failed, " + realException.getLocalizedMessage();
				if (realException instanceof SdaiException && 
						((SdaiException)realException).getErrorId() == SdaiException.RP_DUP) {
					message = "Exporting failed, file is opened. Close " + expressDocSettingPage.getSourceFileName() + " file and try again.";
				}
			}
			MessageDialog.openError(getShell(), "Export Documentation Error", message);
		}
		expressDocSettingPage.saveWidgetValues();
		return ok;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish() {
		return expressDocSettingPage != null && expressDocSettingPage.determinePageCompletion();
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
		if(selection != null) {
			IProject project = null;
			List selResources = IDE.computeSelectedResources(selection);
			for(Iterator i = selResources.iterator(); i.hasNext();) {
				IResource resource = (IResource) i.next();
				if (resource instanceof IFile) {
					IFile file = (IFile) resource;
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
			}
			if(dictionaryFileName == null && project != null) {
				dictionaryFileName = project.getLocation().toOSString();
			}
		}
		if(dictionaryFileName != null) {
			expressDocSettingPage.setSourceFileName(dictionaryFileName);
		}
	}

	private static class Runnable implements IRunnableWithProgress {
		private boolean ok = false;
		
		private String repo_location = null;
		private String html_destination_path = null;
		private boolean generateJavaDocPart;
		
		/**
		 * @param html_destination_path
		 * @param repo_location
		 */
		public Runnable(String html_destination_path, String repo_location, boolean generateJavaDocPart) {
			this.html_destination_path = html_destination_path;
			this.repo_location = repo_location;
			this.generateJavaDocPart = generateJavaDocPart;
		}

		public boolean isOk() {
			return ok;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			monitor.beginTask("Generating html documentation", IProgressMonitor.UNKNOWN);

			String [] args;

			if (true) {  // if not a single schema (how to know if a single schema, and
							// its name?
				args = new String[generateJavaDocPart ? 15  : 16];
				
			} else {
				args = new String[9];
				
			}

			int ii = 0;



			args[ii++] = "-location";
//			 args[ii++] = "ExpressCompilerRepo";
			args[ii++] = repo_location;
//			 args[ii++] = repo_location + File.separator + "ExpressCompilerRepo.sdai";
			args[ii++] = "-output";
			args[ii++] = html_destination_path;
			args[ii++] = "-title";
			args[ii++] = "Express Data"; // TODO project name
			if(!generateJavaDocPart) {
				args[ii++] = "-noJava";
			}

			if (true) {
//			 if (fSchema_name == null) {

//System.out.println("<ALL>");	
				args[ii++] = "-generate_summary";
				args[ii++] = "-complex_schema";
				args[ii++] = "-complex_index";
				args[ii++] = "Express Data Index"; // TODO project name
				args[ii++] = "-exclude";
				args[ii++] = "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA";
//			 args[ii++] = "-exclude";
				args[ii++] = "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA";
				args[ii++] = "EXTENDED_DICTIONARY_SCHEMA_DICTIONARY_DATA";
				args[ii++] = "MAPPING_SCHEMA_DICTIONARY_DATA";
			} else {
				args[ii++] = "-include";
//			 args[ii++] = fSchema_name.toUpperCase() + "_DICTIONARY_DATA";
//System.out.println("<1ONLY>: " + args[ii-1]);	
			}

//			System.setProperty("jsdai.properties", ExpressDocPlugin.class.getResource("jsdai.properties").getFile());

			try {
				try {
				Properties prop = new Properties();
				File repoDir = ExpressDocPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
				if (!repoDir.exists()) repoDir.mkdirs();
				prop.setProperty("repositories", repoDir.getAbsolutePath());
				SdaiSession.setSessionProperties(prop);
				} catch (SdaiException sex) {};
				
/*
				File repoDir = ExpressDocPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
				if (!repoDir.exists()) repoDir.mkdirs();
				System.setProperty("repositories", repoDir.getAbsolutePath());
/**/
				
//				System.out.println("Invoking ExpressDoc with arguments");
//				System.out.println("---------------------------------------");
//				for (int ia = 0; ia < args.length; ia++) {
//					System.out.println("argument " + ia + ": " + args[ia]);	
//				}
//
//				System.out.println("-----------------------------------end-");

				ExpressDoc.main(args);
				ok = true;
			} catch (SdaiException e) {
//				System.out.println("problems with ExpressDoc: " + e);
//				e.printStackTrace();
				
				throw new InvocationTargetException(e);
			}
			
			
			monitor.done();

		}
		
		
		
	}
}
