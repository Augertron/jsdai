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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.events.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.jface.viewers.*;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

public class NewExpressFileWizardPage extends WizardPage {

	private Text fContainerText;
	private Text fFileText;
	private ISelection fSelection;
	private IProject selected_project;

	/**
	 * @param pageName
	 */
	protected NewExpressFileWizardPage(ISelection selection) {
		//super(pageName);
		super("wizardPage");
		setTitle(ExpressCompilerPlugin.getResourceString("WizardPage.title"));
		setDescription(ExpressCompilerPlugin.getResourceString("WizardPage.description"));
		fSelection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.NEW_EXPRESS_FILE_WIZARD_PAGE);

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText(ExpressCompilerPlugin.getResourceString("WizardPage.containerLabel"));

		fContainerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		fContainerText.setLayoutData(gd);
		fContainerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(ExpressCompilerPlugin.getResourceString("WizardPage.browseButtonText"));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(ExpressCompilerPlugin.getResourceString("WizardPage.fileLabel"));

		fFileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fFileText.setLayoutData(gd);
		fFileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable
	 * container to use.
	 */
	
	private void initialize() {
		if (fSelection != null && fSelection.isEmpty() == false && fSelection instanceof IStructuredSelection) {
			IStructuredSelection sselection = (IStructuredSelection)fSelection;
			if (sselection.size()>1) return;
			Object obj = sselection.getFirstElement();
			IFolder express_src = null; 
			String express_src_str = null;
			if (obj instanceof IResource) {

				IProject the_selected_project = ((IResource)obj).getProject();
				selected_project = the_selected_project;
//				String s_defaultExpressFileLocationFieldValue = "_no_value_";
				String s_customExpressFileLocationFieldValue = "_no_value_";;
				String s_is_default = "_no_value_";;

			    IEclipsePreferences prefs = new ProjectScope(the_selected_project).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);

				s_is_default = prefs.get("fDefaultExpressFileLocation", "true").toLowerCase();
				if("true".equals(s_is_default)) {
					IFolder projectExpressFilesLocation = the_selected_project.getFolder("Express files");
					s_customExpressFileLocationFieldValue = projectExpressFilesLocation.getLocation().toOSString();
				} else {
					s_customExpressFileLocationFieldValue = prefs.get("expressFileLocation", "");
				}

		if (s_is_default.equalsIgnoreCase("true")) {
//System.out.println("<1>");
			
			express_src = the_selected_project.getFolder("Express files");
			express_src_str = express_src.getFullPath().toString();
			fContainerText.setText(express_src_str);
		} else 
		if (s_customExpressFileLocationFieldValue.replace('\\','/').equalsIgnoreCase(the_selected_project.getLocation().toString().replace('\\','/'))) {
				// the whole project itself
//System.out.println("<2> project: " + the_selected_project);
			express_src_str = the_selected_project.getFullPath().toString();
//System.out.println("<2-B> project string: " + express_src_str);
			fContainerText.setText(express_src_str);
			//express_src = (IFolder)the_selected_project;
			
		} else {
			// see if inside the project = resource, or outside project losing the advantages of being resource, don't bother if in other projects, for now
			if (s_customExpressFileLocationFieldValue.replace('\\','/').startsWith(the_selected_project.getLocation().toString().replace('\\','/'))) {
//System.out.println("<3>");

				String express_files_folder = s_customExpressFileLocationFieldValue.substring(the_selected_project.getLocation().toString().length()+1,s_customExpressFileLocationFieldValue.length()); 
				express_src = the_selected_project.getFolder(express_files_folder);
				express_src_str = express_src.getFullPath().toString();			
				fContainerText.setText(express_src_str);
			} else {
//System.out.println("<4>");
					fContainerText.setText("");

				// let's treat it as a non-resource location  - although I should not allo that in the new project wizard
			}
		}

/*
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer)obj;
				else
					container = ((IResource)obj).getParent();
*/
//				fContainerText.setText(container.getFullPath().toString());
				if (express_src != null) {
//					fContainerText.setText(express_src.getFullPath().toString());
//					fContainerText.setText(express_src_str);
				} else {
//					fContainerText.setText("");
				}
			}
		}
		fFileText.setText("new_schema.exp");
	}


	/**
	 * Just use the standard container selection dialog 
	 * 
	 */

	private void browse() {
		ContainerSelectionDialog dialog =
			new ContainerSelectionDialog(
				getShell(),
				ResourcesPlugin.getWorkspace().getRoot(),
				false,
				ExpressCompilerPlugin.getResourceString("WizardPage.selectNewFileContainer"));
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				fContainerText.setText(((Path)result[0]).toOSString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		String container = getContainerName();
		String fileName = getFileName();

		if (container.length() == 0) {
			updateStatus(ExpressCompilerPlugin.getResourceString("WizardPage.containerMustBeSpecified"));
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("WizardPage.nameMustBeSpecified");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("exp") == false) {
				updateStatus(ExpressCompilerPlugin.getResourceString("WizardPage.mustBeExp"));
				return;
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return fContainerText.getText();
	}
	public String getFileName() {
		return fFileText.getText();
	}



	/**
	 * @see WizardPage#isPageComplete()
	 */
	public boolean isPageComplete() {
		return !checkFolderForExistingFile() && super.isPageComplete();
	}


	/**
	 * Finds the current directory where the file should be created
	 */
	protected boolean checkFolderForExistingFile() {
		boolean result = false;
		
		if (fContainerText.getText() != null) {
			IPath containerPath = new Path(fContainerText.getText().trim());
			if (containerPath.segmentCount() > 1) {
				IFolder container = ResourcesPlugin.getWorkspace().getRoot().getFolder(containerPath);
				if (container != null && container.exists()) {
					IResource file = container.getFile(fFileText.getText().trim());
					if (file != null && file.exists()) {
						this.setErrorMessage(ExpressCompilerPlugin.getResourceString("WizardPage.fileAlreadyExists"));
						result = true;
					}
				}
			} else {
				// this is a project
				IProject project = null;
				project = selected_project;

/*
				try {

System.out.println(">>><<<< container text: " + fContainerText.getText());

					project = ResourcesPlugin.getWorkspace().getRoot().getProject(fContainerText.getText().trim());
				} catch (java.lang.IllegalArgumentException e) {
				ExpressCompilerPlugin.log(e);
					this.setErrorMessage(ExpressCompilerPlugin.getResourceString("WizardPage.containerMustBeSpecified"));
					result = true;
				} 
	*/			
				
				if (project != null && project.exists()) {
					IResource file = project.getFile(fFileText.getText().trim());
					if (file != null && file.exists()) {
						this.setErrorMessage(ExpressCompilerPlugin.getResourceString("WizardPage.fileAlreadyExists"));
						result = true;
					}
				}
			}
		}
		
		if (!result)
			((NewExpressFileWizard) this.getWizard()).setFileName(fFileText.getText().trim());
		
		return result;
	}


}