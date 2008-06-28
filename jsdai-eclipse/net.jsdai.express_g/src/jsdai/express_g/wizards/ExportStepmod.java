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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.editors.outline.SdaiEditorOutline;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.ExportToStepmod;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @author Mantas Balnys
 *
 */
public class ExportStepmod extends Wizard implements IExportWizard {
	private SdaiEditor editor;
	
	private ActiveAll_FileSelectionPage page = null;

	/**
	 * 
	 */
	public ExportStepmod() {
		super();
		IDialogSettings workbenchSettings = SdaieditPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("StepmodWizard");//$NON-NLS-1$
		if(section == null)
			section = workbenchSettings.addNewSection("StepmodWizard");//$NON-NLS-1$
		setDialogSettings(section);
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = false;
		if ((editor != null) && (page != null)) {
//			String fileName = page.getFileName();
			if (page.isExportSingle()) {
				RepositoryHandler handler = editor.getRepositoryHandler();
				RepositoryChanger changer = new RepositoryChanger(handler.getModels(), "Exporting Stepmod");
				String msg = handler.startROChanger(changer);
				if (msg != null) {
					MessageDialog.openWarning(getShell(), "Export to Stepmod Action", msg);
				} else {
					try {
						Runnable runnable = new Runnable(page.getFileName(), editor);
						getContainer().run(false, false, runnable);
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
				handler.endROChanger(changer);
			} else {
				RepositoryHandler handler = editor.getRepositoryHandler();
				RepositoryChanger changer = new RepositoryChanger(handler.getModels(), "Exporting Stepmod");
				String msg = handler.startROChanger(changer);
				if (msg != null) {
					MessageDialog.openWarning(getShell(), "Export to Stepmod Action", msg);
				} else {
					Object adapt = editor.getAdapter(IContentOutlinePage.class);
					if (adapt instanceof SdaiEditorOutline) {
						SdaiEditorOutline outline = (SdaiEditorOutline)adapt;
						String[] schemas = handler.getSchemas();
						Arrays.sort(schemas, new StringCompare());
						for (int i = 0; i < schemas.length; i++) if (schemas[i] != null) {
							String[] diagrams = handler.getDiagrams(schemas[i]);
							Arrays.sort(diagrams);
							for (int j = 0; j < diagrams.length; j++) {
								ModelHandler mh = handler.getModelHandler(diagrams[j]);
								int edit_mode = PropertySharing.MODE_LAYOUT_COMPLETE_MASK;
								try {
									edit_mode = Integer.parseInt(mh.getProperties().
											getProperty(PropertySharing.PROP_EDITING_MODE, 
											String.valueOf(PropertySharing.MODE_LAYOUT_COMPLETE_MASK)));
								} catch (NumberFormatException nfe) {}
//								if ((edit_mode & PropertySharing.MODE_EXTENDED_MASK) != 0) {
//								} else if ((edit_mode & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0) {
//								} else 
								if ((edit_mode & PropertySharing.MODE_LAYOUT_COMPLETE_MASK) != 0) {
									outline.setSelection(new StructuredSelection(mh));
									if (editor.getActiveInternalPart() instanceof ExpressGEditor) try {
										Runnable runnable = new Runnable(page.getFileName(), editor);
										new ProgressMonitorDialog(getShell()).run(false, false, runnable);
									} catch (InvocationTargetException e) {
										SdaieditPlugin.log(e);
										SdaieditPlugin.console(e.toString());
										Throwable c = e.getCause();
										if (c != null) {
											SdaieditPlugin.log(c);
											SdaieditPlugin.console(c.toString());
										}
									} catch (InterruptedException e) {
										SdaieditPlugin.log(e);
										SdaieditPlugin.console(e.toString());
									}
									j = diagrams.length; // break iterations
								}
							}
						}
						ok = true;
					} else {
						MessageDialog.openWarning(getShell(), "Export to Stepmod Action", "Exporting all is not implemented yet");
					}
				}
				changer.done();
				handler.endROChanger(changer);
			}
			try {
				IPath path = new Path(page.getFileName());
				IResource file = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(path);
				file.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (Throwable t) {
				t.printStackTrace();
			}
	} 
		return ok;
	}

	/**
	 * compares Strings, similar to simple String.compare() but this one supports null objects
	 * @author Mantas Balnys
	 *
	 */
	private static class StringCompare implements Comparator {
		/** 
		 * @param a1 can be null or any othe type of Object
		 * @param a2 can be null or any othe type of Object
		 * @return
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		
		public int compare(Object a1, Object a2) {
			String cmp1 = "";
			String cmp2 = "";
			if (a1 instanceof String) {
				cmp1 = (String)a1;
			} else if (a1 != null) {
				cmp1 = a1.toString();
			}
			if (a2 instanceof String) {
				cmp2 = (String)a2;
			} else if (a2 != null) {
				cmp2 = a2.toString();
			}
			return cmp1.compareTo(cmp2);
		}
	}
	
	private static class Runnable implements IRunnableWithProgress, CommandInvoker {
		private String file;
		private PropertySharing prop;
		private boolean ok = true;
		
		/**
		 * @param handler
		 * @param file
		 */
		public Runnable(String file, SdaiEditor editor) {
			this.file = file;
			prop = ((ExpressGEditor)editor.getActiveInternalPart()).getProperties();
		}

		public void run(IProgressMonitor progress) {
			progress.beginTask("Exporting to Stepmod \t" + prop.getName(), IProgressMonitor.UNKNOWN);
			prop.status().setProgressMonitor(progress);
			if (file != null) {
				ExportToStepmod export = new ExportToStepmod(this);
				export.setDirectory(file);
				prop.handler().startCommand(export);
			} else {
				ok = false;
			}
			progress.done();
			prop.status().setProgressMonitor(null);
		}
		
		public boolean niceDone() {
			return ok;
		}

		/* (non-Javadoc)
		 * @see jsdai.express_g.exp2.ui.command.CommandInvoker#commandDone(jsdai.express_g.exp2.ui.command.Command)
		 */
		public void commandDone(Command command) {
		}
	}


	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();

		if (editor != null) {
			page = new ActiveAll_FileSelectionPage(
					editor.getRepositoryHandler().getRepoPath().removeLastSegments(1).append("modules"),
					"Select Stepmod directory", "Stepmod directory:", "Select Stepmod directory", 
					ActiveAll_FileSelectionPage.DIALOG_DIRECTORY, false);
			page.setTitle("Export to Stepmod");
			page.setDescription("Exporting schemas to Stepmod");
			addPage(page);
		} else {
			WizardInfoPage wp = new WizardInfoPage("Error", "");//"This wizard can be started only on opened Express-G file (*.exg)");
			wp.setTitle("Export to Stepmod");
			wp.setDescription("This wizard can be started only on opened Express-G file (*.exg)");
			addPage(wp);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		IEditorPart part = workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof SdaiEditor) {
			editor = (SdaiEditor)part;
		}

		setWindowTitle("Export to stepmod"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		if ((page != null) && (editor != null))
			page.setSchemaSelection(editor.getActiveInternalPart() instanceof ExpressGEditor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return (editor != null)&&(page != null)&&(page.determinePageCompletion());
	}
}
