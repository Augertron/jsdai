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
import java.util.Arrays;
import java.util.Comparator;

import jsdai.common.CommonPlugin;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.editors.outline.SdaiEditorOutline;
import jsdai.express_g.exp2.ui.Application;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.ExportToStepmod;
import jsdai.express_g.util.xml.IsoDbTools;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @author Mantas Balnys
 *
 */
public class ExportStepmod extends Wizard implements IExportWizard {
	private SdaiEditor editor;
	
	private ActiveAll_FileSelectionPage page = null;

	private static boolean fStepmod = true;

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
	
	public static boolean isStepmod() {
		return fStepmod;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = false;
		if ((editor != null) && (page != null)) {
//			String fileName = page.getFileName();
			if (page.isExportStepmod()) {
				fStepmod = true;
			} else {
				fStepmod = false;
			}

			// let's add here initialization of iso_db:

			IProject fProject = null;

			IProject activeProject = null;
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if ( window != null ) {
				IWorkbenchPage page = window.getActivePage();
				if ( page != null ) {
					IEditorPart editor = page.getActiveEditor();
					if ( editor != null ) {
						IEditorInput input = editor.getEditorInput();
						if ( input instanceof IFileEditorInput ) {
							IFileEditorInput fileInput = (IFileEditorInput) input;
							fProject = fileInput.getFile().getProject();
						}
					}
				}					
			}
			if (fProject == null) {
				ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
				if (selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					if (obj instanceof IResource) {
						fProject = ((IResource)obj).getProject();
					} else {
						// System.out.println("selection element is NOT a resource: " + obj);
					}
				}
			}
			if (fProject != null) {
				String iso_db_path = fProject.getLocation().toOSString() + File.separator + "document_reference.txt";
				MessageConsoleStream stream = CommonPlugin.getDefault().getConsole();
				IsoDbTools.readIsoIdsAndPartNamesOfSchemas(iso_db_path, stream);						
			}

			
			if (page.isExportSingle()) {
				RepositoryHandler handler = editor.getRepositoryHandler();
				RepositoryChanger changer = new RepositoryChanger(handler.getModels(), "Exporting Stepmod");
				String msg = handler.startROChanger(changer);
				if (msg != null) {
//					MessageDialog.openWarning(getShell(), "Export to Stepmod Action", msg);
					MessageDialog.openWarning(getShell(), "Exporting Express-G diagrams Action", msg);
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
//					MessageDialog.openWarning(getShell(), "Export to Stepmod Action", msg);
					MessageDialog.openWarning(getShell(), "Exporting Express-G diagrams Action", msg);
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
//						MessageDialog.openWarning(getShell(), "Export to Stepmod Action", "Exporting all is not implemented yet");
						MessageDialog.openWarning(getShell(), "Exporting Express-G diagrams Action", "Exporting all is not implemented yet");
					}
				}
				changer.done();
				handler.endROChanger(changer);
			}
			try {
				IPath path = new Path(page.getFileName());
				IResource file = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(path);
				if (file != null) {
					file.refreshLocal(IResource.DEPTH_INFINITE, null);
				} else {
					// This is the case when the chosed target directory for generated gif/xml files is outside the existing projects in the workspace
					// System.out.println("REFRESHING - file is NULL, path: " + path);
				}
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
//System.out.println("<>prop: " + prop);
      if (prop instanceof Application) {
//      	System.out.println("is modified at the start?: " + ((Application)prop).isModified());
      }
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

      if (prop instanceof Application) {
//      	System.out.println("is modified? at the end?: " + ((Application)prop).isModified());
      	if (!((Application)prop).isModified()) {
      		((Application)prop).setModified(false); 
      	}
      }

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
//					"Select Stepmod directory", "Stepmod directory:", "Select Stepmod directory", 
					"Choose a directory to export to", "Export to directory:", "Choose a directory to export to", 
					ActiveAll_FileSelectionPage.DIALOG_DIRECTORY, false);
//			page.setTitle("Export to Stepmod");
			page.setTitle("Export Express-G diagrams");
			page.setDescription("Exporting Express-G diagrams");
			addPage(page);
		} else {
			WizardInfoPage wp = new WizardInfoPage("Error", "");//"This wizard can be started only on opened Express-G file (*.exg)");
//			wp.setTitle("Export to Stepmod");
			wp.setTitle("Export Express-G diagrams");
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

//		setWindowTitle("Export to stepmod"); //$NON-NLS-1$
		setWindowTitle("Export Express-G diagrams"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		if ((page != null) && (editor != null))
			page.setSchemaSelection(editor.getActiveInternalPart() instanceof ExpressGEditor);
		page.setStepmodSelection(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish() {
		return (editor != null)&&(page != null)&&(page.determinePageCompletion());
	}
}
