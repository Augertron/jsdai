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
import java.io.IOException;

import jsdai.express_doc.ExpressDocPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.Text;
//import org.eclipse.compare.CompareEditorInput;

/**
 * @author Mantas Balnys
 *
 */
public class ExpressDocSettingPage extends WizardPage {
	private static final String NONE_AVAILABLE = "<nothing selected>";
	
	public static final String NAME_BROWSE = "Browse...";
	private static final String INVALID_SOURCE_FILE_MESSAGE = "No valid source file selected";
	private static final String INVALID_DESTINATION_DIRECTORY_MESSAGE = "No valid documentation directory selected";
	private static final String[] DEFAULT_EXTENSION_FILTER = new String[]{"*.*"};

	public static final int DIALOG_FILE = 1;
	public static final int DIALOG_DIRECTORY = 2;

	private IPath projectPath = null;
	private FileSelectGroup sourceFileGroup;
	private FileSelectGroup destDirectoryGroup;
	private Button generateJavaDocPart;
	private Button enableIncremental;
	IProject fProject = null;


//	private Composite titleGroup = null;
	private Group titleGroup = null;
	protected Text titleTextField;
  boolean textEntryChanged = false;
  String initialTitle = "Express Data";


	/**
	 * changing of this string may require clearing metadata info associated with ExpressDocSettingPage.STORE_SOURCE_NAMES_ID
	 */

// do we still need those?

//	public final static String STORE_SOURCE_NAMES_ID =	"ExpressDocSettingPage.STORE_SOURCE_NAMES_ID"; //$NON-NLS-1$
//	public final static String STORE_LAST_SOURCE_NAME_ID =	"ExpressDocSettingPage.STORE_LAST_SOURCE_NAME_ID"; //$NON-NLS-1$
//	public final static String STORE_JAVA_OPTION_ID =	"ExpressDocSettingPage.STORE_JAVA_OPTION_ID"; //$NON-NLS-1$
//	public final static String STORE_INCREMENTAL_DOCS_ID =	"ExpressDocSettingPage.STORE_INCREMENTAL_DOCS_ID"; //$NON-NQualifiedName
//	public final static String STORE_DOCS_TITLE_ID = "ExpressDocSettingPage.STORE_DOCS_TITLE_ID";
//	public final static String STORE_DOCS_DESTINATION_ID = "ExpressDocSettingPage.STORE_DOCS_DESTINATION_ID";

	/**
	 * 
	 * @param name
	 */
	/*
	public ExpressDocSettingPage(IPath projectPath, String name) {
		super(name);
		this.projectPath = projectPath;
	}
	*/
	public ExpressDocSettingPage(IPath projectPath, String name, IProject fProject) {
		super(name);
		this.projectPath = projectPath;
		this.fProject = fProject;
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
			new FileSelectGroup(mainComposite, "docDir", "Select Express Documentation directory", "Directory:",
					"Select Express Documentation directory", DIALOG_DIRECTORY, SWT.SAVE,
					null, INVALID_DESTINATION_DIRECTORY_MESSAGE);
		destDirectoryGroup.createGroupControls();
		
		createTitleGroup(mainComposite);

//        generateJavaDocPart = new Button(optionGroup, SWT.CHECK);
//       generateJavaDocPart.setText("Generate Java documentation part");
  

		Group optionGroup = new Group(mainComposite, SWT.NONE);
        optionGroup.setText("Documentation Options");
        optionGroup.setLayout(new GridLayout(1, false));
        optionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        generateJavaDocPart = new Button(optionGroup, SWT.CHECK);
        generateJavaDocPart.setText("Generate Java documentation part");

		Group incGroup = new Group(mainComposite, SWT.NONE);
        incGroup.setText("Incremental Generation Options");
        incGroup.setLayout(new GridLayout(1, false));
        incGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        enableIncremental = new Button(incGroup, SWT.CHECK);
        enableIncremental.setText("Enable incremental generation");


		restoreWidgetValues();
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());

		setControl(mainComposite);

	}



	protected void createTitleGroup(Composite parent) {

		titleGroup = new Group(parent, SWT.NONE);
//		titleGroup = new Composite(parent, SWT.NONE);
    titleGroup.setText("Specify the title");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		titleGroup.setLayout(layout);
		titleGroup.setFont(parent.getFont());
		titleGroup.setLayoutData(
			new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		Label titleLabel = new Label(titleGroup, SWT.NONE);
		titleLabel.setText("Title:");
		titleLabel.setFont(parent.getFont());


		// source Text entry field (for diagram name)
		
		titleTextField = new Text(titleGroup, SWT.SINGLE | SWT.BORDER);
		GridData data =
			new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = 250;
		titleTextField.setLayoutData(data);
		titleTextField.setFont(parent.getFont());


/*
    String title_value = null;
		if (fProject == null) {
			fProject = ExportDocumentation.getProject();
			System.out.println("fProject: " + fProject);
		}
		
		if (fProject != null) {
			try {
				title_value = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_doc",".fDocumentTitle"));
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			System.out.println("fProject = null");
			
		}
			
	
    if (title_value == null) {
    	title_value = initialTitle;
    }
    titleTextField.setText(title_value);
*/


		titleTextField.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateFromSourceField();
			}
		});
		
		titleTextField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateWidgetEnablements();
			}
		});
		
		titleTextField.addKeyListener(new KeyListener(){
			/*
			 * @see KeyListener.keyPressed
			 */
			public void keyPressed(KeyEvent e){
				//If there has been a key pressed then mark as dirty
				textEntryChanged = true;
			}

			/*
			 * @see KeyListener.keyReleased
			 */
			public void keyReleased(KeyEvent e){}
		});
		
		titleTextField.addFocusListener(new FocusListener(){
			/*
			 * @see FocusListener.focusGained(FocusEvent)
			 */
			public void focusGained(FocusEvent e){
				//Do nothing when getting focus
			}
			
			/*
			 * @see FocusListener.focusLost(FocusEvent)
			 */
			public void focusLost(FocusEvent e){
				//Clear the flag to prevent constant update
				if(textEntryChanged){
					textEntryChanged = false;
					updateFromSourceField();
				}
				
			}	
		});


	}
	void updateFromSourceField(){
		
		//setSourceName(sourceNameField.getText());
		//Update enablements when this is selected
		updateWidgetEnablements();
	}		

	public void setDestinationDirectory(IResource documentResource) {
		String destination_path = null;
		try {
			destination_path = documentResource.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".destinationPath"));

	    if (destination_path == null) {
				setInitialDestDirectory();
    	} else {
				File destDir = new File(destination_path);
				if(destDir.exists()) {
					destDirectoryGroup.setFileName(destDir.getCanonicalPath());
				} else {
					setInitialDestDirectory();
				}
			}
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	public void setDestinationDirectory(String exd_string) {
		Path exd_path = new Path(exd_string);
		IFile exd_file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(exd_path);
		if (exd_file != null ) {
			setDestinationDirectory(exd_file);
		} else {
			try {
				setInitialDestDirectory();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	public void setDocumentTitle(IResource documentResource) {
		String title_value = null;
		try {
			title_value = documentResource.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".documentTitle"));

// experiment - a temporary workaround - start (this also sets two checkboxes together with the document title - it is actually working, 

						String flag_generate_java = documentResource.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".flagGenerateJava"));
						String flag_enable_incremental = documentResource.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".flagEnableIncremental"));
						if (flag_generate_java != null) {
//System.out.println("flag_generate_java: " + flag_generate_java);
							if (flag_generate_java.equals("true")) {
								generateJavaDocPart.setSelection(true);
							} else {
								generateJavaDocPart.setSelection(false);
							}
						}
						if (flag_enable_incremental != null) {
							if (flag_enable_incremental.equals("true")) {
								enableIncremental.setSelection(true);
							} else {
								enableIncremental.setSelection(false);
							}
						}


// experiment - end



		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    if (title_value == null) {
    	title_value = initialTitle;
    	//System.out.println("FAILED TO GET TITLE");
    }
    titleTextField.setText(title_value);
	}
	public void setDocumentTitle(String exd_string) {
		// we need to get IResource from String and then invoke the method just above
		//IPath path = ...;
		//IWorkspace workspace = ResourcesPlugin.getWorkspace();
		//IWorkspaceRoot root = workspace.getRoot();
		//IResource resource = root.findMember(path);

		//String myPathStr = "/abc/aa/a.c";
		Path exd_path = new Path(exd_string);
		IFile exd_file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(exd_path);
		if (exd_file != null ) {
			setDocumentTitle(exd_file);
		} else {
			setDefaultDocumentTitle();
		}
	}

	public void setDefaultDocumentTitle() {
    titleTextField.setText(initialTitle);
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

	public boolean isGenerateJavaDocPart() {
		return generateJavaDocPart.getSelection();
	}
	public boolean isEnableIncremental() {
		return enableIncremental.getSelection();
	}
	public String getDocumentTitle() {
		return this.titleTextField.getText();
	}

	/**
	 *	Use the dialog store to restore widget values to the values that they held
	 *	last time this wizard was used to completion
	 */
	public void restoreWidgetValues() {
		try {
//System.out.println("@@@ 1 @@@");
//			IDialogSettings settings = getDialogSettings();
//			if (settings != null) {
//				sourceFileGroup.restoreSettings(settings);
//				destDirectoryGroup.restoreSettings(settings);
				if(destDirectoryGroup.getSelectedFile() == null) {
					setInitialDestDirectory();
//				}


				// we need the resource from somewhere
				File sourceFile = sourceFileGroup.getSelectedFile();

// THIS IS NOT WORKING - sourceFile is null 

				if(sourceFile != null) {
//System.out.println("@@@ 1 @@@");

					Path exd_path = new Path(sourceFile.getCanonicalPath());
//System.out.println("trying to get exd_file: " + exd_path);
					IFile exd_file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(exd_path);
					if (exd_file != null) {
//System.out.println("@@@ 2 @@@");

						String flag_generate_java = exd_file.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".flagGenerateJava"));
						String flag_enable_incremental = exd_file.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".flagEnableIncremental"));
						if (flag_generate_java != null) {
//System.out.println("flag_generate_java: " + flag_generate_java);
							if (flag_generate_java.equals("true")) {
								generateJavaDocPart.setSelection(true);
							} else {
								generateJavaDocPart.setSelection(false);
							}
						}
						if (flag_enable_incremental != null) {
							if (flag_enable_incremental.equals("true")) {
								enableIncremental.setSelection(true);
							} else {
								enableIncremental.setSelection(false);
							}
						}
					}
				}

				// old implementation
				//generateJavaDocPart.setSelection(settings.getBoolean(STORE_JAVA_OPTION_ID));
				//enableIncremental.setSelection(settings.getBoolean(STORE_INCREMENTAL_DOCS_ID));

				updateWidgetEnablements();
			}
		} catch (CoreException e1) {
			throw (IllegalArgumentException)new IllegalArgumentException(e1.getMessage()).initCause(e1);
		} catch (IOException e2) {
			throw (IllegalArgumentException)new IllegalArgumentException(e2.getMessage()).initCause(e2);
		}
	}

	/**
	 *	Use the dialog store to restore widget values to the values that they held
	 *	last time this wizard was used to completion
	 */
	public void saveWidgetValues() {
		return;
/*	Getting rid of settings altogether	
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			sourceFileGroup.saveSettings(settings);
			destDirectoryGroup.saveSettings(settings);
			settings.put(STORE_JAVA_OPTION_ID, generateJavaDocPart.getSelection());
			settings.put(STORE_INCREMENTAL_DOCS_ID, enableIncremental.getSelection());
		}
*/
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
    // added for the title field
		if (this.titleTextField != null) {
			if ((this.titleTextField.getText() == null) || (this.titleTextField.getText().equals(""))) {
				return false;
			}
		}
		boolean complete =
//			sourceFileGroup.validateSourceGroup() && destDirectoryGroup.validateSourceGroup() && titleGroup.validateSourceGroup();
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
		File sourceFile = sourceFileGroup.getSelectedFile();
		String dest_dir_value = null;
		boolean done = false;

//System.out.println("setting initial directory, sourceFile: " + sourceFile);

//System.out.println("projectPath: " + projectPath);
//System.out.println("fProject: " + fProject);

//				currentFileName = projectPath.toOSString();

		
				try {

		if(sourceFile != null) {
	
			Path exd_path = new Path(sourceFile.getCanonicalPath());
			IFile exd_file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(exd_path);

//System.out.println("exd_file: " + exd_file);


			if (exd_file != null ) {

					dest_dir_value = exd_file.getPersistentProperty(new QualifiedName(ExpressDocPlugin.ID_EXPRESS_DOC,".destinationPath"));
//System.out.println("dest_dir_value" + dest_dir_value);
				
				if (dest_dir_value != null) {
					File destDir = new File(dest_dir_value);
					if(destDir.exists()) {
						destDirectoryGroup.setFileName(destDir.getCanonicalPath());
						done = true;
					} else {
					}
				} else {
				}

			
			} else {
			} // exd_file is null
	
		 if (!done) {
	   // in the case of falure continue with this
			if(sourceFile.isFile()) {
				sourceFile = sourceFile.getParentFile();
			}
			File destDir = new File(sourceFile, "html");
			if(!destDir.exists()) {
				destDir = sourceFile;
			}
			destDirectoryGroup.setFileName(destDir.getCanonicalPath());
		
		}
		
		}
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
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
			if (projectPath != null) {
				currentFileName = projectPath.toOSString();
				fileNameField.setText(currentFileName);
			}

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
								setDocumentTitle(selectedFile);
								setDestinationDirectory(selectedFile);
								// experiment:

								
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
			return;
/*
			String[] sourceNames = settings.getArray(groupId + STORE_SOURCE_NAMES_ID);
			if (sourceNames != null) {
				for (int i = 0; i < sourceNames.length; i++)
					if (!NONE_AVAILABLE.equalsIgnoreCase(sourceNames[i]))
						fileNameField.add(sourceNames[i]);
				String lastName = settings.get(groupId + STORE_LAST_SOURCE_NAME_ID);
				if(lastName != null) {
					setFileName(lastName);
				}
			}
*/		
		}

		private void saveSettings(IDialogSettings settings) {
			return; // we don't want use settings anymore
/*
			try {
				updateFromFileNameField();
				if (settings == null) return;
				String[] items = fileNameField.getItems();
				settings.put(groupId + STORE_SOURCE_NAMES_ID, items);
				File selected_file = getSelectedFile();
				if (selected_file != null) {
					settings.put(groupId + STORE_LAST_SOURCE_NAME_ID, selected_file.getCanonicalPath());
				} else {
					// perhaps to print to log something
				}
			} catch (IOException e) {
				throw (IllegalArgumentException)new IllegalArgumentException(e.getMessage()).initCause(e);
			}
*/
		
		}


	}
}


