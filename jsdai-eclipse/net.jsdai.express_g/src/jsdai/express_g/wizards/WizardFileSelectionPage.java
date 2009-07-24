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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

/**
 * @author Mantas Balnys
 *
 */
public class WizardFileSelectionPage extends WizardPage implements Listener {
	/**
	 * changing of this string may requere clearing metadata info associated with WizardFileSelectionPage.STORE_SOURCE_NAMES_ID
	 */
	private static final String NONE_AVAILABLE = "<nothing selected>";
	
	private static final String NAME_BROWSE = "Browse...";
	private static final String NAME_SOURCE_EMPTY_MESSAGE = "No file selected";

	protected String NAME_LABEL = "File:";
	protected String NAME_DIALOG_TITLE = "Select File";
	
	protected String[] EXTENSIONS_FILTER = new String[]{"*.*"};
	protected int DIALOG_STYLE = SWT.OPEN | SWT.SINGLE;
	public static final int DIALOG_FILE = 1;
	public static final int DIALOG_DIRECTORY = 2;
	protected int DIALOG_TYPE = DIALOG_FILE;

	protected Combo sourceNameField;
	protected Button sourceBrowseButton;
	
	private IPath projectPath = null;
	
	private boolean none_available = false;
	
	//A boolean to indicate if the user has typed anything
	boolean entryChanged = false;

	// dialog store id constants
	public final static String STORE_SOURCE_NAMES_ID =	"WizardFileSelectionPage.STORE_SOURCE_NAMES_ID"; //$NON-NLS-1$
	public final static String STORE_LAST_SOURCE_NAME_ID =	"WizardFileSelectionPage.STORE_LAST_SOURCE_NAME_ID"; //$NON-NLS-1$

	/**
	 * 
	 * @param name
	 */
	public WizardFileSelectionPage(IPath projectPath, String name) {
		super(name);
		this.projectPath = projectPath;
	}

	/**
	 * 
	 * @param name
	 * @param label
	 * @param dialog
	 */
	public WizardFileSelectionPage(IPath projectPath, String name, String label, String dialog, int dialogType, boolean none_available) {
		this(projectPath, name);
		NAME_LABEL = label;
		NAME_DIALOG_TITLE = dialog;
		DIALOG_TYPE = dialogType;
		this.none_available = none_available;
	}

	/**
	 * 
	 * @param name
	 * @param label
	 * @param dialog
	 * @param dialogStyle
	 * @param extensions
	 */
	public WizardFileSelectionPage(IPath projectPath, String name, String label, String dialog, int dialogType, int dialogStyle, String[] extensions, boolean none_available) {
		this(projectPath, name, label, dialog, dialogType, none_available);
		DIALOG_STYLE = dialogStyle;
		if (extensions != null) EXTENSIONS_FILTER = (String[])extensions.clone();
	}

	private Composite main_composite = null;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		main_composite = new Composite(parent, SWT.NULL);
		main_composite.setLayout(new GridLayout());
		main_composite.setLayoutData(new GridData(
			GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		main_composite.setSize(main_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		main_composite.setFont(parent.getFont());
		
		Button useit = null;
		if (isNone_available()) {
			useit = new Button(main_composite, SWT.CHECK);
			useit.setText("Import dictionary data at once");
		}

		createSourceGroup(main_composite);
		Composite extension = new Composite(main_composite, SWT.NULL);
		extension.setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		extension.setLayoutData(new GridData(
			GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		extension.setSize(main_composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		extension.setFont(parent.getFont());
		fillInExtension(extension);
		stepmodOrNotExtension(extension);

//		createDestinationGroup(composite);

//		createOptionsGroup(composite);

		restoreWidgetValues();
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());

		setControl(main_composite);

		if (useit != null) {
			useit.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setSourceGroupEnabled(((Button)e.widget).getSelection());
				}
			});
			useit.setSelection(false);
			setSourceGroupEnabled(false);
//			useit.setEnabled(true);
		}
	}
	
	private void setSourceGroupEnabled(boolean enabled) {
		sourceContainerGroup.setEnabled(enabled);
		sourceNameField.setEnabled(enabled);
		sourceBrowseButton.setEnabled(enabled);
	}
	
	protected void fillInExtension(Composite parent) {
	}
	protected void stepmodOrNotExtension(Composite parent) {
	}
	
	
	private Composite sourceContainerGroup = null;
	/**
	 *	Create the import source specification widgets
	 */
	protected void createSourceGroup(Composite parent) {

		sourceContainerGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		sourceContainerGroup.setLayout(layout);
		sourceContainerGroup.setFont(parent.getFont());
		sourceContainerGroup.setLayoutData(
			new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		Label groupLabel = new Label(sourceContainerGroup, SWT.NONE);
		groupLabel.setText(NAME_LABEL);
		groupLabel.setFont(parent.getFont());

		// source name entry field
		sourceNameField = new Combo(sourceContainerGroup, SWT.BORDER);
		GridData data =
			new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = 250;
		sourceNameField.setLayoutData(data);
		sourceNameField.setFont(parent.getFont());

		sourceNameField.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateFromSourceField();
			}
		});
		
		sourceNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateWidgetEnablements();
			}
		});
		
		sourceNameField.addKeyListener(new KeyListener(){
			/*
			 * @see KeyListener.keyPressed
			 */
			public void keyPressed(KeyEvent e){
				//If there has been a key pressed then mark as dirty
				entryChanged = true;
			}

			/*
			 * @see KeyListener.keyReleased
			 */
			public void keyReleased(KeyEvent e){}
		});
		
		sourceNameField.addFocusListener(new FocusListener(){
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
				if(entryChanged){
					entryChanged = false;
					updateFromSourceField();
				}
				
			}	
		});

		// source browse button
		sourceBrowseButton = new Button(sourceContainerGroup, SWT.PUSH);
		sourceBrowseButton.setText(NAME_BROWSE); //$NON-NLS-1$
		sourceBrowseButton.addListener(SWT.Selection, this);
		sourceBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		sourceBrowseButton.setFont(parent.getFont());
		setButtonLayoutData(sourceBrowseButton);
	}

	/**
	 *	Handle all events and enablements for widgets in this dialog
	 *
	 * @param event Event
	 */
	public void handleEvent(Event event) {
		if (event.widget == sourceBrowseButton)
			handleSourceBrowseButtonPressed();
		updateWidgetEnablements();
	}
	/**
	 *	Open an appropriate source browser so that the user can specify a source
	 *	to import from
	 */
	protected void handleSourceBrowseButtonPressed() {

		String currentSource = this.sourceNameField.getText();
		switch (DIALOG_TYPE) {
			case DIALOG_FILE :
				FileDialog dialogF = new FileDialog(sourceNameField.getShell(), DIALOG_STYLE);
				dialogF.setText(NAME_DIALOG_TITLE);
				dialogF.setFilterPath(getSourceDirectoryName(currentSource));
				dialogF.setFilterExtensions(EXTENSIONS_FILTER);

				String selectedFile = dialogF.open();
				if (selectedFile != null) {
					//Just quit if the directory is not valid
					if ((getSourceDirectory(selectedFile) == null)
						|| selectedFile.equals(currentSource)) {
						return;
					} else { //If it is valid then proceed to populate
						setErrorMessage(null);
						setSourceName(selectedFile);
//						selectionGroup.setFocus();
					}
				}
				break;
			case DIALOG_DIRECTORY :
				DirectoryDialog dialogD = new DirectoryDialog(sourceNameField.getShell(), DIALOG_STYLE);
				dialogD.setText(NAME_DIALOG_TITLE);
				String sourceDir = getSourceDirectoryName(currentSource);
				if ((sourceDir == null)||("".equals(sourceDir))) {
					if (projectPath != null) sourceDir = projectPath.toOSString();
				}
/*				if ((sourceDir == null)||(sourceDir == "")) {
					PropertySharing prop = EGEPlugin.getDefault().getApplication();
					if (prop != null) sourceDir = prop.getRepositoryHandler().getProjectPath(); 
				}*/
				dialogD.setFilterPath(sourceDir);

				String selectedDirectory = dialogD.open();
				if (selectedDirectory != null) {
					//Just quit if the directory is not valid
					if ((getSourceDirectory(selectedDirectory) == null)
						|| selectedDirectory.equals(currentSource)) {
						return;
					} else { //If it is valid then proceed to populate
						setErrorMessage(null);
						setSourceName(selectedDirectory);
//						selectionGroup.setFocus();
					}
				}
				break;
		}
	}
	
	/**
	 * Sets the source name of the import to be the supplied path.
	 * Adds the name of the path to the list of items in the
	 * source combo and selects it.
	 *
	 * @param path the path to be added
	 */
	protected void setSourceName(String path) {

		if (path.length() > 0) {

			String[] currentItems = this.sourceNameField.getItems();
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
				this.sourceNameField.setItems(newItems);
				selectionIndex = oldLength;
			}
			this.sourceNameField.select(selectionIndex);

			updateWidgetEnablements();
//			resetSelection();
		}
	}

	/**
	 * Update the receiver from the source name field.
	 */

	void updateFromSourceField(){
		
		setSourceName(sourceNameField.getText());
		//Update enablements when this is selected
		updateWidgetEnablements();
	}		

	/**
	 *	Use the dialog store to restore widget values to the values that they held
	 *	last time this wizard was used to completion
	 */
	protected void restoreWidgetValues() {
		if (none_available) sourceNameField.add(NONE_AVAILABLE);
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			String[] sourceNames = settings.getArray(STORE_SOURCE_NAMES_ID);
			if (sourceNames != null) {
				for (int i = 0; i < sourceNames.length; i++)
					if (!NONE_AVAILABLE.equalsIgnoreCase(sourceNames[i]))
						sourceNameField.add(sourceNames[i]);
				String lastName = settings.get(STORE_LAST_SOURCE_NAME_ID);
				if (lastName != null)
					setSourceName(lastName);
			}
			// set filenames history
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
	public boolean determinePageCompletion() {
		//Check for valid projects before making the user do anything 
		if (noOpenProjects()) {
			setErrorMessage(IDEWorkbenchMessages.WizardImportPage_noOpenProjects); //$NON-NLS-1$
			return false;
		}
		boolean complete =
			validateSourceGroup()
/*				&& validateDestinationGroup()
				&& validateOptionsGroup()*/;

		// Avoid draw flicker by not clearing the error
		// message unless all is valid.
		if (complete)
			setErrorMessage(null);

		return complete;
	}	

	/**
	 * Returns whether or not the passed workspace has any 
	 * open projects
	 * @return boolean
	 */
	private boolean noOpenProjects(){
		IProject[] projects = IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getProjects();
		for(int i = 0; i < projects.length; i ++){
			if(projects[i].isOpen())
				return false;
		}
		return true;
	}

	/**
	 *	Answer a boolean indicating whether self's source specification
	 *	widgets currently all contain valid values.
	 */
	protected boolean validateSourceGroup() {
		File sourceDirectory = getSourceDirectory();
		if (sourceDirectory == null) {
			setMessage(NAME_SOURCE_EMPTY_MESSAGE);
//			enableButtonGroup(false);
			return false;
		}
		
//		enableButtonGroup(true);
		return true;
	}
	
	/**
	 * Returns a File object representing the currently-named source directory iff
	 * it exists as a valid directory, or <code>null</code> otherwise.
	 */
	protected File getSourceDirectory() {
		return getSourceDirectory(this.sourceNameField.getText());
	}
	/**
	 * Returns a File object representing the currently-named source directory iff
	 * it exists as a valid directory, or <code>null</code> otherwise.
	 *
	 * @param path a String not yet formatted for java.io.File compatability
	 */
	private File getSourceDirectory(String path) {
		File sourceDirectory = new File(getSourceDirectoryName(path));
		if ((DIALOG_STYLE == SWT.OPEN)&&(!sourceDirectory.exists()) || 
				((DIALOG_TYPE == DIALOG_FILE)&&(sourceDirectory.isDirectory())) ||
				((DIALOG_TYPE == DIALOG_DIRECTORY)&&(!sourceDirectory.isDirectory()))) {			
			return null;
		}

		return sourceDirectory;
	}
	
	/**
	 *	Answer the directory name specified as being the import source.
	 *	Note that if it ends with a separator then the separator is first
	 *	removed so that java treats it as a proper directory
	 * XXX
	private String getSourceDirectoryName() {
		return getSourceDirectoryName(this.sourceNameField.getText());
	}
	
	/**
	 *	Answer the directory name specified as being the import source.
	 *	Note that if it ends with a separator then the separator is first
	 *	removed so that java treats it as a proper directory
	 */
	private String getSourceDirectoryName(String sourceName) {
		IPath result = new Path(sourceName.trim());

		if (result.getDevice() != null && result.segmentCount() == 0)	// something like "c:"
			result = result.addTrailingSeparator();
		else
			result = result.removeTrailingSeparator();

		return result.toOSString();
	}
	
	public String getFileName() {
		return this.sourceNameField.getText();
	}
	
	public void dispose() {
		IDialogSettings settings = getDialogSettings();
		if (settings != null) {
			String[] items = sourceNameField.getItems();
			settings.put(STORE_SOURCE_NAMES_ID, items);
			settings.put(STORE_LAST_SOURCE_NAME_ID, getFileName());
		}
		super.dispose();
	}

	/**
	 * @return Returns the none_available.
	 */
	public boolean isNone_available() {
		return none_available;
	}
	
	public boolean isNone_selected() {
		return !sourceNameField.isEnabled();
	}
}


