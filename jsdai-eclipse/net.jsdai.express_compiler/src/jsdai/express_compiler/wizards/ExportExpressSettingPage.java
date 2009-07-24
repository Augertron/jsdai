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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class ExportExpressSettingPage extends WizardPage {
	private static final String NONE_AVAILABLE = "<nothing selected>";
	
	public static final String NAME_BROWSE = "Browse...";
	private static final String INVALID_SOURCE_FILE_MESSAGE = "No valid source file selected";
	private static final String INVALID_DESTINATION_DIRECTORY_MESSAGE = "No valid generated express directory selected";
	private static final String[] DEFAULT_EXTENSION_FILTER = new String[]{"*.*"};

	public static final int DIALOG_FILE = 1;
	public static final int DIALOG_DIRECTORY = 2;

	private String project_path = null;
	private IPath projectPath = null;
	private FileSelectGroup sourceFileGroup;
	private FileSelectGroup destDirectoryGroup;
	// private Button generateJavaDocPart;

	/**
	 * changing of this string may require clearing metadata info associated with ExpressDocSettingPage.STORE_SOURCE_NAMES_ID
	 */
	public final static String STORE_SOURCE_NAMES_ID =	"ExportExpressSettingPage.STORE_SOURCE_NAMES_ID"; //$NON-NLS-1$
	public final static String STORE_LAST_SOURCE_NAME_ID =	"ExportExpressSettingPage.STORE_LAST_SOURCE_NAME_ID"; //$NON-NLS-1$
	public final static String STORE_JAVA_OPTION_ID =	"ExportExpressSettingPage.STORE_JAVA_OPTION_ID"; //$NON-NLS-1$

	/**
	 * 
	 * @param name
	 */
//	public ExportExpressSettingPage(IPath projectPath, String name) {
	public ExportExpressSettingPage(String project_path, String name) {
		super(name);
		// this.projectPath = projectPath;
		this.project_path = project_path;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite mainComposite = new Composite(parent, SWT.NULL);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(
			GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		mainComposite.setSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		mainComposite.setFont(parent.getFont());
		
		sourceFileGroup =
			new FileSelectGroup(mainComposite, "repoFile", "Select Express Data file", "File:", "Select Express Data File",
					DIALOG_FILE, SWT.OPEN, new String[]{"*.exd;*.exg;*.sdai"}, INVALID_SOURCE_FILE_MESSAGE);
        sourceFileGroup.createGroupControls();
        
		
		destDirectoryGroup =
			new FileSelectGroup(mainComposite, "prettyDir", "Select Generated Express directory", "Directory:",
					"Select Generated Express directory", DIALOG_DIRECTORY, SWT.SAVE,
					null, INVALID_DESTINATION_DIRECTORY_MESSAGE);
		destDirectoryGroup.createGroupControls();
        
		
		//Group optionGroup = new Group(mainComposite, SWT.NONE);
        //optionGroup.setText("Generation Options");
        //optionGroup.setLayout(new GridLayout(1, false));
        //optionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
		
        // generateJavaDocPart = new Button(optionGroup, SWT.CHECK);
        // generateJavaDocPart.setText("Generate Java documentation part");
        
		restoreWidgetValues();
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());

		setControl(mainComposite);

	}
	
	public void setSourceFileName(String name) {
		try {
			sourceFileGroup.setFileName(name);
			if(destDirectoryGroup.getSelectedFile() == null) {
				setInitialDestDirectory();
			}
		} catch (IOException e) {
			throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
		}
	}

	public String getSourceFileName() {
		try {
			File selectedFile = sourceFileGroup.getSelectedFile();
			return selectedFile != null ? selectedFile.getCanonicalPath() : null;
		} catch (IOException e) {
			throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
		}
	}

	public String getDestinationDirectory() {
		try {
			File selectedFile = destDirectoryGroup.getSelectedFile();
			return selectedFile != null ? selectedFile.getCanonicalPath() : null;
		} catch (IOException e) {
			throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
		}
	}

	/*
	public boolean isGenerateJavaDocPart() {
		return generateJavaDocPart.getSelection();
	}
    */
	
	/**
	 *	Use the dialog store to restore widget values to the values that they held
	 *	last time this wizard was used to completion
	 */
	public void restoreWidgetValues() {
		try {
			IDialogSettings settings = getDialogSettings();
			if (settings != null) {
				sourceFileGroup.restoreSettings(settings);
				destDirectoryGroup.restoreSettings(settings);
				if(destDirectoryGroup.getSelectedFile() == null) {
					setInitialDestDirectory();
				}
				//generateJavaDocPart.setSelection(settings.getBoolean(STORE_JAVA_OPTION_ID));
				updateWidgetEnablements();
			}
		} catch (IOException e) {
			throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
		}
	}

	/**
	 *	Use the dialog store to restore widget values to the values that they held
	 *	last time this wizard was used to completion
	 */
	public void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			sourceFileGroup.saveSettings(settings);
			destDirectoryGroup.saveSettings(settings);
			// settings.put(STORE_JAVA_OPTION_ID, generateJavaDocPart.getSelection());
		}
	}

	/**
	 * Check if widgets are enabled or disabled by a change in the dialog.
	 */
	protected void updateWidgetEnablements() {

		boolean pageComplete = determinePageCompletion();
		setPageComplete(pageComplete);
		if (pageComplete) setMessage(null);
	}

	/**
	 * Returns whether this page is complete. This determination is made based upon
	 * the current contents of this page's controls.  Subclasses wishing to include
	 * their controls in this determination should override the hook methods 
	 * <code>validateSourceGroup</code> and/or <code>validateOptionsGroup</code>.
	 *
	 * @return <code>true</code> if this page is complete, and <code>false</code> if
	 *   incomplete
	 * @see #validateSourceGroup
	 * @see #validateOptionsGroup
	 */
	protected boolean determinePageCompletion() {
		boolean complete =
			sourceFileGroup.validateSourceGroup() && destDirectoryGroup.validateSourceGroup();

		// Avoid draw flicker by not clearing the error
		// message unless all is valid.
		if (complete)
			setErrorMessage(null);
		return complete;
	}	

	private void fileSelectGroupChanged(FileSelectGroup fileSelectGroup) throws IOException {
		if(fileSelectGroup == sourceFileGroup) {
			setInitialDestDirectory();
		}
	}

	private void setInitialDestDirectory() throws IOException {
		
		String destination_path = ExportExpressWizard.getGeneratedExpressDir();
//		File sourceFile = sourceFileGroup.getSelectedFile();
//		if(sourceFile != null) {
        if (true) {
//		if(sourceFile.isFile()) {
//				sourceFile = sourceFile.getParentFile();
//			}
//			File destDir = new File(sourceFile, "exp");
//			if(!destDir.exists()) {
//				destDir = sourceFile;
//			}
//			destDirectoryGroup.setFileName(destDir.getCanonicalPath());
			destDirectoryGroup.setFileName(destination_path);
		}
	}

	private class FileSelectGroup implements ModifyListener, FocusListener, SelectionListener {
		private final Group thisGroup;
		private final String groupId;
		private final String groupName;
		private final String fileLabel;
		private final String browseDialogTitle;
		private final int browseDialogType;
		private final int browseDialogStyle;
		private final String[] extensionFilter;
		private final String invalidFileMessage;

		private Combo fileNameField;
		private String currentFileName;
		private Button fileBrowseButton;
		
		private FileSelectGroup(Composite parent, String groupId, String groupName, String fileLabel, String browseDialogTitle,
				int browseDialogType, int browseDialogStyle, String[] extensionFilter, String invalidFileMessage) {
			thisGroup = new Group(parent, SWT.NONE);
			this.groupId = groupId;
			this.groupName = groupName;
			this.fileLabel = fileLabel;
			this.browseDialogTitle = browseDialogTitle;
			this.browseDialogType = browseDialogType;
			this.browseDialogStyle = browseDialogStyle;
			this.invalidFileMessage = invalidFileMessage;
			this.extensionFilter = extensionFilter != null ? extensionFilter : DEFAULT_EXTENSION_FILTER;
			currentFileName = "";
		}

		/**
		 *	Create the import source specification widgets
		 */
		private void createGroupControls() {

			thisGroup.setText(groupName);
//	        thisGroup.setLayout(new GridLayout(1, false));
//	        thisGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			thisGroup.setLayout(new GridLayout(3, false));
			thisGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

			Label groupLabel = new Label(thisGroup, SWT.NONE);
			groupLabel.setText(fileLabel);

			// source name entry field
			fileNameField = new Combo(thisGroup, SWT.BORDER);
			GridData data =
				new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			data.widthHint = 250;
			fileNameField.setLayoutData(data);
			fileNameField.addModifyListener(this);
			fileNameField.addFocusListener(this);
//			if (projectPath != null) {
//				currentFileName = projectPath.toOSString();
//				fileNameField.setText(currentFileName);
//			}
			if (project_path != null)
				fileNameField.setText(ExportExpressWizard.getGeneratedExpressDir());
			fileNameField.setEnabled(false);
			
			// source browse button
			fileBrowseButton = new Button(thisGroup, SWT.PUSH);
			fileBrowseButton.setText(NAME_BROWSE); //$NON-NLS-1$
			fileBrowseButton.addSelectionListener(this);
			fileBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
			setButtonLayoutData(fileBrowseButton);
		}

		// File name field event handling
		public void modifyText(ModifyEvent event) {
			try {
				if(getSelectedFile() != null) {
//					updateFromFileNameField();
					updateWidgetEnablements();
				} else {
					setPageComplete(false);
					setMessage(invalidFileMessage);
				}
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
		}

		// File name field event handling
		public void focusGained(FocusEvent e){
			//Do nothing when getting focus
		}
		
		// File name field event handling
		public void focusLost(FocusEvent event){
			try {
				String prevFileName = currentFileName; 
				updateFromFileNameField();
				if(!prevFileName.equals(fileNameField.getText())) {
					fileSelectGroupChanged(this);
				}
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
		}

		// Button event handling
		public void widgetSelected(SelectionEvent e) {
			handleSourceBrowseButtonPressed();
			updateWidgetEnablements();
		}

		// Button event handling
		public void widgetDefaultSelected(SelectionEvent e) {
			handleSourceBrowseButtonPressed();
			updateWidgetEnablements();
		}
		/**
		 *	Open an appropriate source browser so that the user can specify a source
		 *	to import from
		 */
		private void handleSourceBrowseButtonPressed() {

			try {
				String currentSource = this.fileNameField.getText();
				File currentFile = new File(currentSource).getCanonicalFile();
				switch (browseDialogType) {
					case DIALOG_FILE :
						FileDialog fileDialog = new FileDialog(fileNameField.getShell(), browseDialogStyle);
						fileDialog.setText(browseDialogTitle);
						if(currentFile.isFile()) {
							fileDialog.setFilterPath(currentFile.getParent());
							fileDialog.setFileName(currentFile.getName());
						} else {
							fileDialog.setFilterPath(currentFile.getCanonicalPath());
						}
						fileDialog.setFilterExtensions(extensionFilter);

						String selectedFile = fileDialog.open();
						if (selectedFile != null) {
							//Just quit if the directory is not valid
							if ((getSelectedFile(selectedFile) == null)
								|| selectedFile.equals(currentSource)) {
								return;
							} else { //If it is valid then proceed to populate
								setErrorMessage(null);
								setFileName(selectedFile);
								fileSelectGroupChanged(this);
//							selectionGroup.setFocus();
							}
						}
						break;
					case DIALOG_DIRECTORY :
						DirectoryDialog directoryDialog = new DirectoryDialog(fileNameField.getShell(), browseDialogStyle);
						directoryDialog.setText(browseDialogTitle);
						String sourceDir = getCanonicalFileName(currentSource);
						if (currentSource.length() == 0 || sourceDir == null) {
							if (projectPath != null) sourceDir = projectPath.toOSString();
						}
/*				if ((sourceDir == null)||(sourceDir == "")) {
							PropertySharing prop = EGEPlugin.getDefault().getApplication();
							if (prop != null) sourceDir = prop.getRepositoryHandler().getProjectPath(); 
						}*/
						directoryDialog.setFilterPath(sourceDir);

						String selectedDirectory = directoryDialog.open();
						if (selectedDirectory != null) {
							//Just quit if the directory is not valid
							if ((getSelectedFile(selectedDirectory) == null)
								|| selectedDirectory.equals(currentSource)) {
								return;
							} else { //If it is valid then proceed to populate
								setErrorMessage(null);
								setFileName(selectedDirectory);
								fileSelectGroupChanged(this);
//							selectionGroup.setFocus();
							}
						}
						break;
				}
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
		}

		/**
		 * Update the receiver from the source name field.
		 * @throws IOException if file exception occurs
		 */
		private void updateFromFileNameField() throws IOException {
			File selectedFile = getSelectedFile();
			if(selectedFile != null) {
				setFileName(selectedFile.getCanonicalPath());
			}
			//Update enablements when this is selected
//			updateWidgetEnablements();
		}		

		/**
		 *	Answer the directory name specified as being the import source.
		 *	Note that if it ends with a separator then the separator is first
		 *	removed so that java treats it as a proper directory
		 * @throws IOException if file name construction exception occurs
		 */
		private String getCanonicalFileName(String sourceName) throws IOException {
			return new File(sourceName).getCanonicalPath();
		}

		/**
		 * Returns a File object representing the currently-named source directory iff
		 * it exists as a valid directory, or <code>null</code> otherwise.
		 * @throws IOException 
		 */
		private File getSelectedFile() throws IOException {
			return getSelectedFile(fileNameField.getText());
		}

		/**
		 * Returns a File object representing the currently-named source directory iff
		 * it exists as a valid directory, or <code>null</code> otherwise.
		 *
		 * @param path a String not yet formatted for java.io.File compatability
		 * @throws IOException 
		 */
		private File getSelectedFile(String path) throws IOException {
			if(path.length() != 0) {
				File sourceDirectory = new File(getCanonicalFileName(path));
				if ((browseDialogStyle == SWT.OPEN)&&(!sourceDirectory.exists()) || 
						((browseDialogType == DIALOG_FILE)&&(sourceDirectory.isDirectory())) ||
						((browseDialogType == DIALOG_DIRECTORY)&&(!sourceDirectory.isDirectory()))) {			
					return null;
				}
				return sourceDirectory;
			} else {
				return null;
			}
		}

		/**
		 * Sets the source name of the import to be the supplied path.
		 * Adds the name of the path to the list of items in the
		 * source combo and selects it.
		 *
		 * @param path the path to be added
		 */
		private void setFileName(String path) {

			if (path.length() > 0) {

				String[] currentItems = fileNameField.getItems();
				int selectionIndex = -1;
				for (int i = 0; i < currentItems.length; i++) {
					if (currentItems[i].equals(path))
						selectionIndex = i;
				}
				if (selectionIndex < 0) {
					int oldLength = currentItems.length;
					String[] newItems = new String[oldLength + 1];
					System.arraycopy(currentItems, 0, newItems, 0, oldLength);
					newItems[oldLength] = path;
					fileNameField.setItems(newItems);
					selectionIndex = oldLength;
				}
				fileNameField.select(selectionIndex);
				currentFileName = path;
//				resetSelection();
			}
		}

		/**
		 *	Answer a boolean indicating whether self's source specification
		 *	widgets currently all contain valid values.
		 */
		protected boolean validateSourceGroup() {
			try {
				File sourceDirectory = getSelectedFile();
				if (sourceDirectory == null) {
					setMessage(invalidFileMessage);
//				enableButtonGroup(false);
					return false;
				}
				
//			enableButtonGroup(true);
				return true;
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
		}
		private void restoreSettings(IDialogSettings settings) {
			String[] sourceNames = settings.getArray(groupId + STORE_SOURCE_NAMES_ID);
			if (sourceNames != null) {
				for (int i = 0; i < sourceNames.length; i++)
					if (!NONE_AVAILABLE.equalsIgnoreCase(sourceNames[i]))
						fileNameField.add(sourceNames[i]);
//				String lastName = settings.get(groupId + STORE_LAST_SOURCE_NAME_ID);
//				String lastName = ExportExpressWizard.getProjectPath() + File.separator + "Generated express";
				String lastName = project_path + File.separator + "Generated express";
				if(lastName != null) {
					setFileName(lastName);
				}
			}
		}

		private void saveSettings(IDialogSettings settings) {
			try {
				updateFromFileNameField();
				String[] items = fileNameField.getItems();
				settings.put(groupId + STORE_SOURCE_NAMES_ID, items);
				settings.put(groupId + STORE_LAST_SOURCE_NAME_ID, getSelectedFile().getCanonicalPath());
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
		}


	}
}


