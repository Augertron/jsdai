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
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * @author Mantas Balnys
 *
 */
public class ExportSTEP extends Wizard implements IExportWizard {
	private SdaiEditor editor = null;
	private String selectedPath = null; 
	
	private ExportSTEPPage1 page = null;


	/**
	 * 
	 */
	public ExportSTEP() {
		super();
		IDialogSettings workbenchSettings = SdaieditPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("StepFileWizard");//$NON-NLS-1$
		if(section == null)
			section = workbenchSettings.addNewSection("StepFileWizard");//$NON-NLS-1$
		setDialogSettings(section);
	}

	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();

		IPath startPath = null;
		if (editor != null) {
			startPath = editor.getRepositoryHandler().getRepoPath().removeLastSegments(1);
		} else {
			startPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		}

		page = new ExportSTEPPage1(startPath, "expressDocExport");
		page.setTitle("Export Express Data to STEP file");
//			page_from.setDescription("Select Express Data file to export its contents to html documentation.");
		addPage(page);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = true;
		try {
			Runnable runnable = new Runnable(page.getSourceFileName(), page.getDestinationFile());
			getContainer().run(true, false, runnable);
			ok = runnable.niceDone();
			try {
				IPath path = new Path(page.getDestinationFile());
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
				file.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (Throwable t) {
				SdaieditPlugin.log(t);
				SdaieditPlugin.console(t.toString());
			}
		} catch (InvocationTargetException e) {
			SdaieditPlugin.log(e);
			ok = false;
			SdaieditPlugin.console(e.toString());
		} catch (InterruptedException e) {
			SdaieditPlugin.log(e);
			ok = false;
			SdaieditPlugin.console(e.toString());
		}
		
		if (ok) page.saveWidgetValues();
		
		return ok;
	}
	
	private static class Runnable implements IRunnableWithProgress {
		private String repoName;
		private String file;
		private boolean ok = true;
		
		/**
		 * @param handler
		 * @param file
		 */
		public Runnable(String repoName, String file) {
			this.repoName = repoName;
			this.file = file;
		}

		public void run(IProgressMonitor progress) {
			progress.beginTask("Exporting STEP file", IProgressMonitor.UNKNOWN);
			if (file != null) {
				try {
					if (SdaiSession.getSession() == null) {
						Properties prop = new Properties();
						File repoDir = SdaieditPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
						if (!repoDir.exists()) repoDir.mkdirs();
						prop.setProperty("repositories", repoDir.getAbsolutePath());
						SdaiSession.setSessionProperties(prop); 
					}
					SdaiSession session = SdaiSession.openSession();

					SdaiTransaction transaction = null;
					if (session.testActiveTransaction()) {
						transaction = session.getActiveTransaction();
					} else {
						transaction = session.startTransactionReadOnlyAccess();
					}
					ASdaiRepository arep = session.getActiveServers();
					SdaiIterator repi = arep.createIterator();
					SdaiRepository repository = null;
					while (repi.next() && repository == null) {
						SdaiRepository rep = arep.getCurrentMember(repi);
						String loc = rep.getLocation();
						if (loc != null && loc.equals(repoName)) {
							repository = rep;
						}
					}

					if (repository == null)	repository = session.linkRepository("", repoName);

					if (!repository.isActive()) repository.openRepository();

					repository.exportClearTextEncoding(file);
					
					session.closeSession();
				} catch (SdaiException e) {
					SdaieditPlugin.log(e);
					ok = false;
					SdaieditPlugin.console(e.toString());
				}
			} else {
				ok = false;
			}
			progress.done();
		}
		
		public boolean niceDone() {
			return ok;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection select) {
		IEditorPart part = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof SdaiEditor) {
			editor = (SdaiEditor)part;
		}
		if (select != null) {
			Object selection = select.getFirstElement();
			if (selection instanceof IFile) {
				String extension = ((IFile)selection).getFileExtension();
				if (extension.equalsIgnoreCase("exg") || extension.equalsIgnoreCase("exd") || extension.equalsIgnoreCase("sdai")) {
					selectedPath = ((IFile)selection).getLocation().toOSString();
				}
			}
		}
		setWindowTitle("Export STEP file"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return page != null && page.determinePageCompletion();
	}
	
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		
		if (selectedPath != null) {
			page.setSourceFileName(selectedPath);
		} else if (editor != null) {
			page.setSourceFileName(editor.getRepositoryHandler().getRepoPath().toOSString());
		}
	}
}
