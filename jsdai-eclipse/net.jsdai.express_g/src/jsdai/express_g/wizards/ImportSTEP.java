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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.util.repocopy.RepoCopy;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Mantas Balnys
 *
 */
public class ImportSTEP extends Wizard implements IImportWizard {
	private SdaiEditor editor;
	
	private WizardFileSelectionPage page = null;

	/**
	 * 
	 */
	public ImportSTEP() {
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

		page = new WizardFileSelectionPage(editor.getRepositoryHandler().getRepoPath().removeLastSegments(1),
				"Select STEP file", "STEP File:", "Select STEP File", WizardFileSelectionPage.DIALOG_FILE, 
				SWT.OPEN | SWT.SINGLE, new String[]{"*.step;*.stp;*.p21"}, false);
		page.setTitle("Import STEP file");
		page.setDescription("Importing STEP file into current repository.");
		addPage(page);
	}
	
	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public boolean performFinish() {
		boolean ok = true;
		RepositoryHandler handler = editor.getRepositoryHandler();
		RepositoryChanger changer = new RepositoryChanger(handler.getModels(), "Importing STEP file");
		String msg = handler.startRWChanger(changer);
		if (msg != null) {
			MessageDialog.openWarning(getShell(), "Import STEP Action", msg);
		} else {
			try {
				Runnable runnable = new Runnable(handler, page.getFileName());
// TODO running progress monitor task in separate thread invokes exceptions in jsdai.lang				
				new ProgressMonitorDialog(getShell()).run(false, false, runnable); // TODO calcelable
				ok = runnable.niceDone();
			} catch (InvocationTargetException e) {
				SdaieditPlugin.log(e);
				ok = false;
				SdaieditPlugin.console(e.toString());
			} catch (InterruptedException e) {
				SdaieditPlugin.log(e);
				ok = false;
				SdaieditPlugin.console(e.toString());
			}
		}
		changer.done();
		handler.endRWChanger(changer);
		try { handler.update(); } catch (SdaiException sex) { 
			SdaieditPlugin.log(sex);
			SdaieditPlugin.console(sex.toString());
		}
		editor.refreshOutline();
		
		return ok;
	}
	
	private static class Runnable implements IRunnableWithProgress {
		private RepositoryHandler handler;
		private String file;
		private boolean ok = true;
		
		/**
		 * @param handler
		 * @param file
		 */
		public Runnable(RepositoryHandler handler, String file) {
			this.handler = handler;
			this.file = file;
		}

		public void run(IProgressMonitor progress) {
			progress.beginTask("Importing STEP file", IProgressMonitor.UNKNOWN);
			if (file != null) {
				try {
 					SdaiRepository repository = handler.getRepository();
					SdaiSession session = repository.getSession();
					File tempFile = EGToolKit.getTempFile(null, "sdai");
					SdaiRepository repoTMP = session.createRepository("", tempFile.getAbsolutePath());
					repoTMP.openRepository();
					session.importClearTextEncoding(new FileInputStream(file), repoTMP);
//			        RepoFixer.update17_18(repoTMP);

					RepoCopy.synchronizedRepoCopy(handler, repoTMP, progress);

			        repoTMP.closeRepository();
			        repoTMP.unlinkRepository();
			        
			        handler.saveAll();
					EGToolKit.delTempFile(tempFile);
				} catch (SdaiException e) {
					SdaieditPlugin.log(e);
					ok = false;
					SdaieditPlugin.console(e.toString());
				} catch (FileNotFoundException e) {
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
		setWindowTitle("Import STEP file"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return (editor != null)&&(page != null)&&(page.determinePageCompletion());
	}
}
