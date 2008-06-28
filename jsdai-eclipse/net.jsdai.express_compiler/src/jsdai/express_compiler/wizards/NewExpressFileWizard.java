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

package jsdai.express_compiler.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import jsdai.express_compiler.ExpressCompilerPlugin;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class NewExpressFileWizard extends Wizard implements INewWizard {

	private NewExpressFileWizardPage fPage;
	private ISelection fSelection;
	private String fFileName;


	/**
	 * Constructor for NewExpressFileWizard.
	 */
	public NewExpressFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	public void setFileName(String name) {
		fFileName = name;	
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		fSelection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		fPage = new NewExpressFileWizardPage(fSelection);
		addPage(fPage);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	/*
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
/*
	public void createPageControls(Composite pageContainer) {
		// TODO Auto-generated method stub
		
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#dispose()
	 */
/*
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getContainer()
	 */
/*
	public IWizardContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getDefaultPageImage()
	 */
/*
	public Image getDefaultPageImage() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getDialogSettings()
	 */
/*
	public IDialogSettings getDialogSettings() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
/*
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPage(java.lang.String)
	 */
/*
	public IWizardPage getPage(String pageName) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPageCount()
	 */
/*
	public int getPageCount() {
		// TODO Auto-generated method stub
		return 0;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPages()
	 */
/*
	public IWizardPage[] getPages() {
		// TODO Auto-generated method stub
		return null;
	}
*/


	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
/*
	public IWizardPage getPreviousPage(IWizardPage page) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getStartingPage()
	 */
/*
	public IWizardPage getStartingPage() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getTitleBarColor()
	 */
/*
	public RGB getTitleBarColor() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#getWindowTitle()
	 */
/*
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#isHelpAvailable()
	 */
/*
	public boolean isHelpAvailable() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#needsPreviousAndNextButtons()
	 */
/*
	public boolean needsPreviousAndNextButtons() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#needsProgressMonitor()
	 */
/*
	public boolean needsProgressMonitor() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performCancel()
	 */
/*
	public boolean performCancel() {
		// TODO Auto-generated method stub
		return false;
	}
*/
	/* (non-Javadoc) when pressed "Finish" button in the wizzard
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {

		final String containerName = fPage.getContainerName();
		final String fileName = fPage.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					performingFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
				ExpressCompilerPlugin.log(e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
				ExpressCompilerPlugin.log(e);
			return false;
		} catch (InvocationTargetException e) {
				ExpressCompilerPlugin.log(e);
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), ("Wizard.error"), realException.getMessage());
			return false;
		}
		return true;
	}

	/**
			actually creating an express file
	 */

	private void performingFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
		// create an (almost) empty express file
		monitor.beginTask(("Wizard.Monitor.creating") + " "+fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(("Wizard.Monitor.containerDoesNotExistException") + containerName);
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
				ExpressCompilerPlugin.log(e);
		}
		monitor.worked(1);
		monitor.setTaskName(("Wizard.Monitor.openingFile"));
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					ExpressCompilerPlugin.log(e);
				}
			}
		});
		monitor.worked(1);
	}

	/**
	 *   adding contents to the new file - just an empty schema 
	 */

	private InputStream openContentStream() {
		String schema_name = fFileName.substring(0, fFileName.length()-4);
		StringBuffer contents = new StringBuffer("\n");
		contents.append("SCHEMA ");
		contents.append(schema_name);
		contents.append(";\n\n");
		contents.append("END_SCHEMA;\n\n");
		return new ByteArrayInputStream(contents.toString().getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "net.jsdai.express_compiler.editor", IStatus.OK, message, null);
		throw new CoreException(status);
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#setContainer(org.eclipse.jface.wizard.IWizardContainer)
	 */
/*
	public void setContainer(IWizardContainer wizardContainer) {
		// TODO Auto-generated method stub
		
	}
*/
}

