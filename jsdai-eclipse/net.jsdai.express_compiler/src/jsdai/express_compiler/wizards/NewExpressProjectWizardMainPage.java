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
import java.net.URI;

import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.filesystem.FileSystemConfiguration;
import org.eclipse.ui.internal.ide.filesystem.FileSystemSupportRegistry;

/**
 * Standard main page for a wizard that is creates a project resource.
 * <p>
 * This page may be used by clients as-is; it may be also be subclassed to suit.
 * </p>
 * <p>
 * Example useage:
 * <pre>
 * mainPage = new ExpressProjectWizardPage("basicExpressProjectPage");
 * mainPage.setTitle("Project");
 * mainPage.setDescription("Create a new project resource.");
 * </pre>
 * </p>
 */
//public class NewExpressProjectWizardPage extends WizardNewProjectCreationPage {
public class NewExpressProjectWizardMainPage extends WizardPage {

    public boolean useDefaultsForProject = true;

    // initial value stores
    private String initialProjectFieldValue;
//    private String initialTempFieldValue;
//    private String initialExpressCompilerRepoFieldValue;

    private String initialProjectLocationFieldValue;

    // the value the user has entered
    String customProjectLocationFieldValue;

    // widgets
    Text projectNameField;

    Text locationPathFieldForProject;

    Label locationLabelForProject;

    Button browseButtonForProject;

    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            if (valid) {
            	NewExpressProjectWizard.getInstance().fProjectName = getProjectNameFieldValue();
            }
            setPageComplete(valid);
            if (valid)
                setLocationForSelection();
//            NewExpressProjectWizardPermanentPage.getInstance().locationPathFieldForExpressFiles.setText(getProjectLocationFieldValue() + File.separator + getProjectNameFieldValue() + File.separator + "Express files");
            if (NewExpressProjectWizardInputPage.getInstance().useDefaultsForExpressFiles) {
            	NewExpressProjectWizardInputPage.getInstance().locationPathFieldForExpressFiles.setText(getProjectLocationFieldValue() + File.separator + "Express files");
        		}
            if (NewExpressProjectWizardInputPage.getInstance().useDefaultsForShortNames) {
            	NewExpressProjectWizardInputPage.getInstance().locationPathFieldForShortNames.setText(getProjectLocationFieldValue() + File.separator + "Short names");
        		}
            if (NewExpressProjectWizardInputPage.getInstance().useDefaultsForComplexEntities) {
            	NewExpressProjectWizardInputPage.getInstance().locationPathFieldForComplexEntities.setText(getProjectLocationFieldValue() + File.separator + "Complex entities");
        		}
        }
    };

    // for project location 	
    private Listener locationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            if (valid) {
            	NewExpressProjectWizard.getInstance().fProjectPath = getProjectLocationFieldValue();
            }
            setPageComplete(valid);
        }
    };

    
    
    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;
	
	
	
	public NewExpressProjectWizardMainPage(String pageName) {
		super(pageName);
        setPageComplete(false);
        customProjectLocationFieldValue = ""; //$NON-NLS-1$

	//-------------------------
//        useDefaultsForTemp = false;
	
	}

    /**
     * Creates a new project creation wizard page.
     *
     * @param pageName the name of this page
     */
    /** (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public void createControl(Composite parent) {

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.NEW_EXPRESS_PROJECT_WIZARD_MAIN_PAGE);

        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createProjectNameGroup(composite);
        createProjectLocationGroup(composite);
//        createTempLocationGroup(composite);
//        createExpressCompilerRepoLocationGroup(composite);
        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    /**
     * Creates the project location specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectLocationGroup(Composite parent) {

        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
        projectGroup.setText("Project location");

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
        useDefaultsButton.setText(IDEWorkbenchMessages.ProjectLocationSelectionDialog_useDefaultLabel);
        useDefaultsButton.setSelection(useDefaultsForProject);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedProjectLocationGroup(projectGroup, !useDefaultsForProject);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForProject = useDefaultsButton.getSelection();
                browseButtonForProject.setEnabled(!useDefaultsForProject);
                locationPathFieldForProject.setEnabled(!useDefaultsForProject);
                locationLabelForProject.setEnabled(!useDefaultsForProject);
                if (useDefaultsForProject) {
                    customProjectLocationFieldValue = locationPathFieldForProject.getText();
                    setLocationForSelection();
                } else {
                    locationPathFieldForProject.setText(customProjectLocationFieldValue);
                }
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }

    /**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectNameGroup(Composite parent) {
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel);
        projectLabel.setFont(parent.getFont());

        // new project name entry field
        projectNameField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(data);
        projectNameField.setFont(parent.getFont());

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null)
            projectNameField.setText(initialProjectFieldValue);
        projectNameField.addListener(SWT.Modify, nameModifyListener);
    }

    /**
     * Creates the project location specification controls.
     *
     * @param projectGroup the parent composite
     * @param enabled the initial enabled state of the widgets created
     */
    private void createUserSpecifiedProjectLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForProject = new Label(projectGroup, SWT.NONE);
        locationLabelForProject.setText(IDEWorkbenchMessages.ProjectLocationSelectionDialog_locationLabel);
        locationLabelForProject.setEnabled(enabled);
        locationLabelForProject.setFont(font);

        // project location entry field
        locationPathFieldForProject = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForProject.setLayoutData(data);
        locationPathFieldForProject.setEnabled(enabled);
        locationPathFieldForProject.setFont(font);

        // browse button
        browseButtonForProject = new Button(projectGroup, SWT.PUSH);
        browseButtonForProject.setText(IDEWorkbenchMessages.ProjectLocationSelectionDialog_browseLabel);
        browseButtonForProject.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleProjectLocationBrowseButtonPressed();
            }
        });

        browseButtonForProject.setEnabled(enabled);
        browseButtonForProject.setFont(font);
        setButtonLayoutData(browseButtonForProject);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectLocationFieldValue == null)
            locationPathFieldForProject.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForProject.setText(initialProjectLocationFieldValue);
        locationPathFieldForProject.addListener(SWT.Modify, locationModifyListener);
    }


    /**
     * Returns the current project location path as entered by 
     * the user, or its anticipated initial value.
     * Note that if the default has been returned the path
     * in a project description used to create a project
     * should not be set.
     *
     * @return the project location path or its anticipated initial value.
     */
    public IPath getProjectLocationPath() {
//        if (useDefaultsForProject)
//            return Platform.getLocation();
//			String project_location = getProjectLocationFieldValue();
//System.out.println(">><< project location: " + project_location);
        return new Path(getProjectLocationFieldValue());
    }

    /**
     * Creates a project resource handle for the current project name field value.
     * <p>
     * This method does not create the project resource; this is the responsibility
     * of <code>IProject::create</code> invoked by the new project resource wizard.
     * </p>
     *
     * @return the new project resource handle
     */
    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(
                getProjectName());
    }

    /**
     * Returns the current project name as entered by the user, or its anticipated
     * initial value.
     *
     * @return the project name, its anticipated initial value, or <code>null</code>
     *   if no project name is known
     */
    public String getProjectName() {
        if (projectNameField == null)
            return initialProjectFieldValue;

        return getProjectNameFieldValue();
    }

    /**
     * Returns the value of the project name field
     * with leading and trailing spaces removed.
     * 
     * @return the project name in the field
     */
    public String getProjectNameFieldValue() {
        if (projectNameField == null)
            return ""; //$NON-NLS-1$

        return projectNameField.getText().trim();
    }



    /**
     * Returns the value of the project location field
     * with leading and trailing spaces removed.
     * 
     * @return the project location directory in the field
     */
    protected String getProjectLocationFieldValue() {
        if (locationPathFieldForProject == null)
            return ""; //$NON-NLS-1$

        return locationPathFieldForProject.getText().trim();
    }


    /**
     *	Open an appropriate directory browser
     */
    void handleProjectLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForProject
                .getShell());
        dialog.setMessage(IDEWorkbenchMessages.ProjectLocationSelectionDialog_directoryLabel);

        String dirName = getProjectLocationFieldValue();
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customProjectLocationFieldValue = selectedDirectory;
            locationPathFieldForProject.setText(customProjectLocationFieldValue);
        }
    }

    /**
     * Sets the initial project name that this page will use when
     * created. The name is ignored if the createControl(Composite)
     * method has already been called. Leading and trailing spaces
     * in the name are ignored.
     * 
     * @param name initial project name for this page
     */
    public void setInitialProjectName(String name) {
        if (name == null)
            initialProjectFieldValue = null;
        else {
            initialProjectFieldValue = name.trim();
            initialProjectLocationFieldValue = getDefaultLocationForName(initialProjectFieldValue);
        }
    }


    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    void setLocationForSelection() {
        String project_default = getDefaultLocationForName(getProjectNameFieldValue());
        if (useDefaultsForProject) {
            
            locationPathFieldForProject
                    .setText(project_default);
        }
    }

    void setProjectLocationForSelection() {
        if (useDefaultsForProject)
            locationPathFieldForProject
                    .setText(getDefaultLocationForName(getProjectNameFieldValue()));
    }

    /**
     * Get the defualt location for the provided name.
     * 
     * @param nameValue the name
     * @return the location
     */
    public String getDefaultLocationForName(String nameValue) {
        IPath defaultPath = Platform.getLocation().append(nameValue);
        return defaultPath.toOSString();
    }

    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents,
                IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }

        if (!useDefaults()) {

	        String locationFieldContents = getProjectLocationFieldValue();
	
			if (locationFieldContents.length() == 0) {
				setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectLocationEmpty);
				return false;
			}
	
			FileSystemConfiguration configuration = 
				FileSystemSupportRegistry.getInstance().getDefaultConfiguration();
			URI newPath = configuration != null ?
					configuration.getContributor().getURI(locationFieldContents) : null;
			if (newPath == null) {
				setErrorMessage(IDEWorkbenchMessages.ProjectLocationSelectionDialog_locationError);
				return false;
			}
	
			IProject project;
			String name = new Path(locationFieldContents).lastSegment();
			if (name != null && Path.EMPTY.isValidSegment(name)){
				project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
			}
			else {
				setErrorMessage(IDEWorkbenchMessages.ProjectLocationSelectionDialog_locationError);
				return false;
			}
	
			IStatus locationStatus = project.getWorkspace()
					.validateProjectLocationURI(project, newPath);
	
			if (!locationStatus.isOK()) {
				setErrorMessage(locationStatus.getMessage());
				return false;
			}
        }

        setErrorMessage(null);
        setMessage(null);
		return true;
    }

    /*
     * see @DialogPage.setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible)
            projectNameField.setFocus();
    }

    /**
     * Returns the useDefaults.
     * @return boolean
     */
    public boolean useDefaults() {
        return useDefaultsForProject;
    }

	public boolean isDefaultProjectLocation() {
		return useDefaultsForProject;
	} 

	public String getCustomProjectLocation() {
		return customProjectLocationFieldValue;
	}

}





