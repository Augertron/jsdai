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

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.Resources;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * @author Mantas Balnys
 *
 */
public class NewEXGFile extends BasicNewResourceWizard {
	/**
	 * name of express-g file
	 */
	private WizardNewFileCreationPage page_name = null;
	/**
	 * import express dictionary file at once
	 */
	private RepoCopyFileSelectionPage page_data = null;

	/**
	 * 
	 */
	public NewEXGFile() {
		super();
		IDialogSettings workbenchSettings = SdaieditPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("RepositoryFileWizard");//$NON-NLS-1$
		if(section == null)
			section = workbenchSettings.addNewSection("RepositoryFileWizard");//$NON-NLS-1$
		setDialogSettings(section);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = false;
/*		try {
			IFile file = page_name.createNewFile();
			IPath rpath = new Path(RuntimePlugin.getEmptyRepository());
			IFile rfile = ResourcesPlugin.getWorkspace().getRoot().getFile(rpath);
			Runnable run = new Runnable(file.getLocation(), rfile);
			new ProgressMonitorDialog(null).run(false, false, run);
			ok = true;
			selectAndReveal(file);

	        // Open editor on new file.
			IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
			if (dw != null) {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null)
					IDE.openEditor(page, file, true);
			}
        } catch (Exception e) {
        	System.err.println(e);
        	SdaieditPlugin.console(e);
        	SdaieditPlugin.log(e);
            MessageDialog.openError(null, null, "Error creating Express-G file");
		}/*/
		
		File data_file = null;
		String data_file_name = null;
		if (!page_data.isNone_selected()) data_file_name = page_data.getFileName();
		if (data_file_name != null) data_file = new File(data_file_name);
		if (data_file == null || !data_file.exists()) data_file = new File(RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.ext_dict_lib"), "ExpressCompilerRepo.sdai"));
		
		String fileName = page_name.getFileName();
		if (fileName != null) {
			IPath filePath = page_name.getContainerFullPath().append(fileName);
			if (!"exg".equalsIgnoreCase(filePath.getFileExtension()))
					filePath = filePath.addFileExtension("exg");
			IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
			File dest = new File(ifile.getLocation().toOSString());
			if (!dest.exists() || MessageDialog.openQuestion(getShell(), "Warning", "File exists, overwrite?")) {
					if (EGToolKit.copyFile(data_file, dest)) {//filePath.makeAbsolute().toOSString())) {
						ok = true;
						try {
							selectAndReveal(ifile);

						        // Open editor on new file.
							IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
							if (dw != null) {
								IWorkbenchPage page = dw.getActivePage();
								if (page != null) {
									IDE.openEditor(page, ifile, true);
								}
					    	}
				        } catch (RuntimeException re) {
				        	throw re;
						} catch (Exception e) {
				        	SdaieditPlugin.log(e);
				        	SdaieditPlugin.console(e);
				            MessageDialog.openError(getShell(), null, "Error creating Express-G file");
						}
					} else {
			        	SdaieditPlugin.log("Error creating Express-G file", IStatus.WARNING);
			        	SdaieditPlugin.console("Error creating Express-G file");
			            MessageDialog.openError(getShell(), null, "Error creating Express-G file");
					}
			}
		}/**/
        return ok;
	}

	/* UPGRADE TODO hiden 2005-12-20
	private class Runnable implements IRunnableWithProgress {
		private IFile rfile = null;
		private IPath filePath = null;
		public boolean ok = false;
		
		/**
		 * @param path
		 * @param rfile
		 *
		public Runnable(IPath path, IFile rfile) {
			filePath = path;
			this.rfile = rfile;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 *
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			try {
				rfile.copy(filePath, true, monitor);
	        } catch (Exception e) {
	        	SdaieditPlugin.log(e);
	        	SdaieditPlugin.console(e);
	            MessageDialog.openError(getShell(), null, "Error creating Express-G file");
			}
		}
	}
*/
    /* (non-Javadoc)
     * Method declared on BasicNewResourceWizard.
     */
    protected void initializeDefaultPageImageDescriptor() {
       ImageDescriptor desc = Resources.getImageDescriptor(Resources.JSDAI);//$NON-NLS-1$
	   setDefaultPageImageDescriptor(desc);
    }

    /* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();

        page_name = new WizardNewFileCreationPage("New Express-G file", getSelection());//$NON-NLS-1$
        page_name.setTitle("New Express-G file");
        
//	        page_name.setDescription("New Express-G file"); 
        addPage(page_name);
        
		page_data = new RepoCopyFileSelectionPage(ResourcesPlugin.getWorkspace().getRoot().getLocation(),
				"Select Express dictionary file", "File:", "Select SDAI File", RepoCopyFileSelectionPage.DIALOG_FILE, 
				SWT.OPEN, new String[]{"*.exd", "*.exg", "*.sdai"}, true);
		page_data.setTitle("Import data from Express repository");
		page_data.setDescription("Importing data from Express repository");
		
		addPage(page_data);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection select) {
		super.init(workbench, select);
		setWindowTitle("New Express-G file"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return page_name != null && page_name.isPageComplete();
	}
	
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		
		// TODO enable selection (needs implementation in RepoCopy class)
		page_data.setEnabledDict(false);
		page_data.setEnabledEG(false);

//		WorkbenchHelp.setHelp(page_data.getControl(), SdaieditPlugin.ID_SDAIEDIT + ".ImportRepositoryContextId");

		
		if (getSelection() != null && getSelection().getFirstElement() instanceof IFile) {
			IFile file = (IFile)getSelection().getFirstElement();
			String fext = file.getFileExtension();
			if (fext.equalsIgnoreCase("exd") || fext.equalsIgnoreCase("exg") || 
					fext.equalsIgnoreCase("sdai")) {
				page_data.setSourceName(file.getLocation().toOSString());
			}
		}
	}

}
