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

import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
public class NewExpressProjectWizardTempPage extends WizardPage {

		String fSystemTempDirectory;
	
    boolean useDefaultsForTemp = true;
    int tempLocationType = 0;
    boolean useDefaultsForExpressCompilerRepo = true;
	
		boolean deleteTempOnExitIfNotDefault = true;
		boolean deleteRepoOnExitIfNotDefault = true;

    // initial value stores
//    private String initialTempFieldValue;
//    private String initialExpressCompilerRepoFieldValue;

    private String initialTempLocationFieldValue;
//    private String initialExpressCompilerRepoLocationFieldValue;

    // the value the user has entered
    String customTempLocationFieldValue;
    String customExpressCompilerRepoLocationFieldValue;

    // widgets

    Text locationPathFieldForTemp;
    Text locationPathFieldForExpressCompilerRepo;

    Label locationLabelForTemp;
    Label locationLabelForExpressCompilerRepo;

    Button browseButtonForTemp;
    Button browseButtonForExpressCompilerRepo;

//    Button deleteOnExitTempButton;
    Button deleteOnExitRepoButton;
		boolean fDeleteOnExit;

    private Listener tempLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid);
//            if (valid) ;
//            	setExpressCompilerRepoLocationForSelection();
        }
    };

/*    
    private Listener expressCompilerRepoLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validatePage());
        }
    };
*/    
    
    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;
	
	
	
	public NewExpressProjectWizardTempPage(String pageName) {
		super(pageName);
        setPageComplete(false);
        customTempLocationFieldValue = ""; //$NON-NLS-1$
        customExpressCompilerRepoLocationFieldValue = ""; //$NON-NLS-1$

	//-------------------------
        useDefaultsForTemp = false;
				tempLocationType = 0;
//    		tempLocationType = 0;
        customTempLocationFieldValue = "";
//        customTempLocationFieldValue = getTempDirectoryPathString();
        // default system or default Eclipse can be calculated when Finish is pressed
        initialTempLocationFieldValue = customTempLocationFieldValue; 
				// to remove - let's initialize this value only once, later, no need to show the specific path here
				fSystemTempDirectory = ExpressCompilerUtils.getSystemTempDirectoryString();
//				initialExpressCompilerRepoLocationFieldValue = initialTempLocationFieldValue + "ExpressCompilerRepo";	
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

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.NEW_EXPRESS_PROJECT_WIZARD_TEMP_PAGE);

        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);


        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

//        createProjectNameGroup(composite);
//        createProjectLocationGroup(composite);
        createTempLocationGroup(composite);

        /*
        Composite composite2 = new Composite(composite, SWT.NULL);
		composite2.setBackground(new Color(parent.getDisplay(),128,128,128));			

		GridLayout gl2 = new GridLayout();
		composite2.setLayout(gl2);
//		GridData gl2gd = new GridData(GridData.FILL_BOTH);
		GridData gl2gd = new GridData(GridData.);
        composite2.setLayoutData(gl2gd);
        */
        /*
        Font font = composite.getFont();
        // project specification group
        Group lGroup = new Group(composite, SWT.SHADOW_IN);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        lGroup.setLayout(layout);
        lGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
       // lGroup.setText("Location/mode of dictionary repository");
        */
        
				Label note1 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
				Label note2 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
				Label note3 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
	    	note1.setText("HINT: you may want to change the temp location:");
	    	note2.setText("if you want to use a different drive for large projects because of free space considerations");
	    	note3.setText("if you need the generated java files for debugging or modifying purposes");
	    	note1.setForeground(new Color(parent.getDisplay(),0,80,80));			
	    	note2.setForeground(new Color(parent.getDisplay(),0,80,80));			
	    	note3.setForeground(new Color(parent.getDisplay(),0,80,80));			
        GridData labelgd = new GridData();
        GridData labelgd2 = new GridData();
        GridData labelgd3 = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.verticalIndent = 10;
        labelgd.horizontalIndent = 10;
        labelgd2.horizontalSpan = 2;
        labelgd2.horizontalIndent = 40;
        labelgd3.horizontalSpan = 2;
        labelgd3.horizontalIndent = 40;
        // labelgd.verticalIndent = 10;
        note1.setLayoutData(labelgd);
        note2.setLayoutData(labelgd2);
        note3.setLayoutData(labelgd3);


//        createExpressCompilerRepoLocationGroup(composite);
        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

/*    
    private final void createExpressCompilerRepoLocationGroup(Composite parent) {

        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
        projectGroup.setText("Location/mode of dictionary repository");

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsButton.setText("Use default (temporary)");
        useDefaultsButton.setSelection(useDefaultsForExpressCompilerRepo);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedExpressCompilerRepoLocationGroup(projectGroup, !useDefaultsForExpressCompilerRepo);

        deleteOnExitRepoButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        deleteOnExitRepoButton.setText("delete on exit");
        deleteOnExitRepoButton.setSelection(deleteRepoOnExitIfNotDefault);
        deleteOnExitRepoButton.setFont(font);

        GridData buttonData2 = new GridData();
        buttonData2.horizontalSpan = 3;
        deleteOnExitRepoButton.setLayoutData(buttonData2);

		if (locationPathFieldForExpressCompilerRepo.getText().equalsIgnoreCase(fSystemTempDirectory + File.separator + "ExpressCompilerRepo")) {
			deleteOnExitRepoButton.setSelection(true);
			deleteOnExitRepoButton.setEnabled(false);
		} else {
			deleteOnExitRepoButton.setEnabled(true);
		}

        
        
        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForExpressCompilerRepo = useDefaultsButton.getSelection();
                browseButtonForExpressCompilerRepo.setEnabled(!useDefaultsForExpressCompilerRepo);
                locationPathFieldForExpressCompilerRepo.setEnabled(!useDefaultsForExpressCompilerRepo);
                locationLabelForExpressCompilerRepo.setEnabled(!useDefaultsForExpressCompilerRepo);
                if (useDefaultsForExpressCompilerRepo) {
                    customExpressCompilerRepoLocationFieldValue = locationPathFieldForExpressCompilerRepo.getText();
                    setLocationForSelection();
                } else {
                    locationPathFieldForExpressCompilerRepo.setText(customExpressCompilerRepoLocationFieldValue);
                }
                deleteRepoOnExitIfNotDefault = deleteOnExitRepoButton.getSelection();
        		if (locationPathFieldForExpressCompilerRepo.getText().equalsIgnoreCase(fSystemTempDirectory + File.separator + "ExpressCompilerRepo")) {
        			deleteOnExitRepoButton.setSelection(true);
        			deleteOnExitRepoButton.setEnabled(false);
        		} else {
        			deleteOnExitRepoButton.setEnabled(true);
        		}
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }
    
*/    
    
    private final void createTempLocationGroup(Composite parent) {
//IProject ttt;
        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
        projectGroup.setText("Location of the temporary files for this project");

/*        
        
        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsButton.setText("put inside the project, otherwise - specify (default: system temp))");
        useDefaultsButton.setSelection(useDefaultsForTemp);
        useDefaultsButton.setFont(font);

        
        
        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

*/
				/*
						let's have 3 radio buttons:
						- use system default temp location, deleted when Eclipse is closed  (default)
						- use Eclipse project-specific persistent temp location, deleted when the project itself is deleted
						- put temp directory inside the project, deleted with the project only, unless "delete on exit" is checked
						- specify custom temp location, never deleted unless "delete on exit" is checked
						
						and check box overriting the default delete action:
						
						- delete on exit

				*/


        final Button useGlobalDefaultTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
        useGlobalDefaultTempButton.setText("use the setting in the global express project preferences");
        useGlobalDefaultTempButton.setSelection(true);
				tempLocationType = 0;
        useGlobalDefaultTempButton.setFont(font);


        final Button useSystemTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useSystemTempButton.setText("use system temp location, deleted when Eclipse is closed");
        useSystemTempButton.setText("use system temp location");
        useSystemTempButton.setSelection(false);
//				tempLocationType = 0;
        useSystemTempButton.setFont(font);
//			  useSystemTempButton.addSelectionListener(tempButtonSelectionListener);

        final Button useEclipseTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useEclipseTempButton.setText("use project-life-long-persistent temp location in the Eclipse workspace");
        useEclipseTempButton.setText("use temp location in the Eclipse workspace");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location, deleted when the project is deleted");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location, deleted when the project itself is deleted");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location");
        useEclipseTempButton.setSelection(false);
        useEclipseTempButton.setFont(font);
//			  useEclipseTempButton.addSelectionListener(tempButtonSelectionListener);

        final Button useProjectTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useProjectTempButton.setText("use temp subdirectory inside the project");
        useProjectTempButton.setText("use \"Temp\" subdirectory inside the project");
//        useProjectTempButton.setText("put temp directory inside the project, deleted with the project only, unless \"delete on exit\" is checked");
//        useProjectTempButton.setText("put temp directory inside the project");
        useProjectTempButton.setSelection(false);
        useProjectTempButton.setFont(font);
//			  useProjectTempButton.addSelectionListener(tempButtonSelectionListener);
        
        final Button useCustomTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useCustomTempButton.setText("specify custom temp location, never deleted unless \"delete on exit\" is checked");
        useCustomTempButton.setText("specify custom temp location");
//        useCustomTempButton.setText("specify custom temp location, never deleted unless \"delete on exit\" is checked");
//        useCustomTempButton.setText("specify custom temp location");
        useCustomTempButton.setSelection(false);
        useCustomTempButton.setFont(font);
//			  useCustomTempButton.addSelectionListener(tempButtonSelectionListener);


        GridData buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 3;
        useGlobalDefaultTempButton.setLayoutData(buttonDataR);

        buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 3;
        useSystemTempButton.setLayoutData(buttonDataR);

        buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 3;
        useEclipseTempButton.setLayoutData(buttonDataR);

        buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 3;
        useProjectTempButton.setLayoutData(buttonDataR);

        buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 3;
        useCustomTempButton.setLayoutData(buttonDataR);
        
        
        
//        createUserSpecifiedTempLocationGroup(projectGroup, !useDefaultsForTemp);
        createUserSpecifiedTempLocationGroup(projectGroup, false);



//        deleteOnExitTempButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
////        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
//        deleteOnExitTempButton.setText("delete on exit");
//        deleteOnExitTempButton.setSelection(deleteTempOnExitIfNotDefault);
//        deleteOnExitTempButton.setFont(font);

//        GridData buttonData2 = new GridData();
//        buttonData2.horizontalSpan = 3;
//        deleteOnExitTempButton.setLayoutData(buttonData2);

/*
		if (tempLocationType == 0) {
		// if (locationPathFieldForTemp.getText().equalsIgnoreCase(fSystemTempDirectory)) {
			deleteOnExitTempButton.setSelection(true);
			deleteOnExitTempButton.setEnabled(false);
			fDeleteOnExit = true;
		} else
		if (tempLocationType == 1) {
			deleteOnExitTempButton.setSelection(false);
			deleteOnExitTempButton.setEnabled(false);
			fDeleteOnExit = false;
		} else {
			deleteOnExitTempButton.setEnabled(true);
			deleteOnExitTempButton.setSelection(false);
			fDeleteOnExit = false;
		}
*/

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
//                useDefaultsForTemp = useDefaultsButton.getSelection();
						
							if (useGlobalDefaultTempButton.getSelection()) {
								tempLocationType = 0;
                locationPathFieldForTemp.setText("as specified in global preferences for express projects");
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
	            	browseButtonForTemp.setEnabled(false);
								
            	} else
            	if (useSystemTempButton.getSelection()) {
//      				deleteOnExitTempButton.setSelection(true);
//       				deleteOnExitTempButton.setEnabled(false);
//								fDeleteOnExit = true;
								tempLocationType = 1;
//                locationPathFieldForTemp.setText("in system temp, deleted on exit");
                locationPathFieldForTemp.setText("in the system temp");
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
	            	browseButtonForTemp.setEnabled(false);
            	} else
               	if (useEclipseTempButton.getSelection()) {
//        				deleteOnExitTempButton.setSelection(false);
//        				deleteOnExitTempButton.setEnabled(false);
//								fDeleteOnExit = false;
								tempLocationType = 2;
//                locationPathFieldForTemp.setText("temp in the Eclipse workspace, deleted when project deleted");
                locationPathFieldForTemp.setText("temp in the Eclipse workspace");
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
	            	browseButtonForTemp.setEnabled(false);
            	} else
              if (useProjectTempButton.getSelection()) {
//        				deleteOnExitTempButton.setSelection(false);
//        				deleteOnExitTempButton.setEnabled(true);
//								fDeleteOnExit = false;
								tempLocationType = 3;
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
	            	browseButtonForTemp.setEnabled(false);
                locationPathFieldForTemp.setText("\"Temp\" subdirectory inside the project");
              } else
              if (useCustomTempButton.getSelection()) {
//        				deleteOnExitTempButton.setSelection(false);
//        				deleteOnExitTempButton.setEnabled(true);
//								fDeleteOnExit = false;

//System.out.println("SELECTED CUSTEM - hahahaha");
								tempLocationType = 4;
                locationPathFieldForTemp.setText(customTempLocationFieldValue);
                locationPathFieldForTemp.setEnabled(true);
                locationLabelForTemp.setEnabled(true);
	            	browseButtonForTemp.setEnabled(true);
              }
              

/*            	
            	browseButtonForTemp.setEnabled(!useDefaultsForTemp);
                locationPathFieldForTemp.setEnabled(!useDefaultsForTemp);
                locationLabelForTemp.setEnabled(!useDefaultsForTemp);
                if (useDefaultsForTemp) {
                    customTempLocationFieldValue = locationPathFieldForTemp.getText();
                    setLocationForSelection();
                } else {
                    locationPathFieldForTemp.setText(customTempLocationFieldValue);
                }
                deleteTempOnExitIfNotDefault = deleteOnExitTempButton.getSelection();
    						if (locationPathFieldForTemp.getText().equalsIgnoreCase(fSystemTempDirectory)) {
    							deleteOnExitTempButton.setSelection(true);
    							deleteOnExitTempButton.setEnabled(false);
    						} else {
    							deleteOnExitTempButton.setEnabled(true);
    						}
*/
            }
        };
//        useDefaultsButton.addSelectionListener(listener);
				useGlobalDefaultTempButton.addSelectionListener(listener);
			  useSystemTempButton.addSelectionListener(listener);
			  useEclipseTempButton.addSelectionListener(listener);
			  useProjectTempButton.addSelectionListener(listener);
			  useCustomTempButton.addSelectionListener(listener);
    }



/*

    private void createUserSpecifiedExpressCompilerRepoLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForExpressCompilerRepo = new Label(projectGroup, SWT.NONE);
//        locationLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationLabel);
        locationLabelForExpressCompilerRepo.setText("Directory:");
        locationLabelForExpressCompilerRepo.setEnabled(enabled);
        locationLabelForExpressCompilerRepo.setFont(font);

        // project location entry field
        locationPathFieldForExpressCompilerRepo = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForExpressCompilerRepo.setLayoutData(data);
        locationPathFieldForExpressCompilerRepo.setEnabled(enabled);
        locationPathFieldForExpressCompilerRepo.setFont(font);

        // browse button
        browseButtonForExpressCompilerRepo = new Button(projectGroup, SWT.PUSH);
//        browseButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_browseLabel);
        browseButtonForExpressCompilerRepo.setText("Browse...");
        browseButtonForExpressCompilerRepo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleExpressCompilerRepoLocationBrowseButtonPressed();
            }
        });

        browseButtonForExpressCompilerRepo.setEnabled(enabled);
        browseButtonForExpressCompilerRepo.setFont(font);
        setButtonLayoutData(browseButtonForExpressCompilerRepo);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialExpressCompilerRepoLocationFieldValue == null)
            locationPathFieldForExpressCompilerRepo.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForExpressCompilerRepo.setText(initialExpressCompilerRepoLocationFieldValue);
        //locationPathFieldForExpressCompilerRepo.addListener(SWT.Modify, locationModifyListener);
//        locationPathFieldForExpressCompilerRepo.addListener(SWT.Modify, expressCompilerRepoLocationModifyListener);
    }
    
*/    
    private void createUserSpecifiedTempLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForTemp = new Label(projectGroup, SWT.NONE);
//        locationLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationLabel);
        locationLabelForTemp.setText("Directory:");
        locationLabelForTemp.setEnabled(enabled);
        locationLabelForTemp.setFont(font);

        // project location entry field
        locationPathFieldForTemp = new Text(projectGroup, SWT.BORDER);
        locationPathFieldForTemp.setEditable(false);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForTemp.setLayoutData(data);
        locationPathFieldForTemp.setEnabled(enabled);
        locationPathFieldForTemp.setFont(font);

        // browse button
        browseButtonForTemp = new Button(projectGroup, SWT.PUSH);
//        browseButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_browseLabel);
        browseButtonForTemp.setText("Browse...");
        browseButtonForTemp.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleTempLocationBrowseButtonPressed();
            }
        });

        browseButtonForTemp.setEnabled(enabled);
        browseButtonForTemp.setFont(font);
        setButtonLayoutData(browseButtonForTemp);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialTempLocationFieldValue == null)
            locationPathFieldForTemp.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForTemp.setText(initialTempLocationFieldValue);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);

        if (!enabled) {
        	locationPathFieldForTemp.setText("in the system temp");
				}
        locationPathFieldForTemp.addListener(SWT.Modify, tempLocationModifyListener);
    }


    public IPath getExpressCompilerRepoLocationPath() {
//      if (useDefaultsForProject)
//          return Platform.getLocation();

      return new Path(getExpressCompilerRepoLocationFieldValue());
  }

    public IPath getTempLocationPath() {
//        if (useDefaultsForProject)
//            return Platform.getLocation();

        return new Path(getTempLocationFieldValue());
    }





    private String getExpressCompilerRepoLocationFieldValue() {
        if (locationPathFieldForExpressCompilerRepo == null)
            return ""; //$NON-NLS-1$

        return locationPathFieldForExpressCompilerRepo.getText().trim();
    }

    private String getTempLocationFieldValue() {
        if (locationPathFieldForTemp == null)
            return ""; //$NON-NLS-1$

        return locationPathFieldForTemp.getText().trim();
    }

    void handleExpressCompilerRepoLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForExpressCompilerRepo
                .getShell());
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("ExpressCompilerRepo message 1");

        String dirName = getExpressCompilerRepoLocationFieldValue();
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customExpressCompilerRepoLocationFieldValue = selectedDirectory;
            locationPathFieldForExpressCompilerRepo.setText(customExpressCompilerRepoLocationFieldValue);
        }

        if (locationPathFieldForExpressCompilerRepo.getText().equalsIgnoreCase(fSystemTempDirectory + File.separator + "ExpressCompilerRepo")) {
			deleteOnExitRepoButton.setSelection(true);
			deleteOnExitRepoButton.setEnabled(false);
		} else {
			deleteOnExitRepoButton.setEnabled(true);
		}

    
    }
    

    void handleTempLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForTemp
                .getShell());
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("Select directory for temp files of this project");

        String dirName = getTempLocationFieldValue();
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customTempLocationFieldValue = selectedDirectory;
            locationPathFieldForTemp.setText(customTempLocationFieldValue);
        }

		if (locationPathFieldForTemp.getText().equalsIgnoreCase(fSystemTempDirectory)) {
//			deleteOnExitTempButton.setSelection(true);
//			deleteOnExitTempButton.setEnabled(false);
		} else {
//			deleteOnExitTempButton.setEnabled(true);
		}
    
    }


    void setExpressCompilerRepoLocationForSelection() {
        String express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo)
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    }
    

    void setTempLocationForSelection() {
        String temp_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "temp";

        if (useDefaultsForTemp)
            locationPathFieldForTemp
                    .setText(temp_default);

        String express_compiler_repo_default = temp_default + File.separator + "ExpressCompilerRepo";
        if (!useDefaultsForTemp) express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo) {
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    		}

    }

    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    void setLocationForSelection() {
        String project_default = NewExpressProjectWizard.getMainPage().getDefaultLocationForName(NewExpressProjectWizard.getMainPage().getProjectNameFieldValue());
        String temp_default = project_default + File.separator + "temp";
        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) temp_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "temp";
        if (useDefaultsForTemp) {
            locationPathFieldForTemp
                    .setText(temp_default);
        }
        String express_compiler_repo_default = temp_default + File.separator + "ExpressCompilerRepo";
        if (!useDefaultsForTemp) express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo) {
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    		}
    }



    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {

			if		(tempLocationType == 4) {
  
  			if (customTempLocationFieldValue == null) {
  				return false;
  			
  			}
 
  			if (customTempLocationFieldValue.equals("")) {
  				return false;
  			
  			}
 		}
 
//  	return true;
//  if (true) return true;
/*
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

        String locationFieldContents = getProjectLocationFieldValue();

        if (locationFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectLocationEmpty);
            return false;
        }

        IPath path = new Path(""); //$NON-NLS-1$
        if (!path.isValidPath(locationFieldContents)) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationError);
            return false;
        }

        IPath projectPath = new Path(locationFieldContents);
        if (!useDefaultsForProject && Platform.getLocation().isPrefixOf(projectPath)) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_defaultLocationError);
            return false;
        }

        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }
*/

        /*
         * If not using the default value validate the location.
         */

 /*
  if (!NewExpressProjectWizard.getMainPage().useDefaults()) {
            IStatus locationStatus = workspace.validateProjectLocation(handle,
                    projectPath);
            if (!locationStatus.isOK()) {
                setErrorMessage(locationStatus.getMessage()); //$NON-NLS-1$
                return false;
            }
        }
*/
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    /*
     * see @DialogPage.setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
  //      if (visible)
//            projectNameField.setFocus();
    }


    public boolean useDefaultsForTemp() {
        return useDefaultsForTemp;
    }
	
    public boolean useDefaultsForExpressCompilerRepo() {
        return useDefaultsForExpressCompilerRepo;
    }
	
	
	


	public boolean isDefaultTempLocation() {
		return useDefaultsForTemp;
	} 
	
	public int getTempLocationType() {
		return tempLocationType;
	}

	public String getTempLocationTypeStr() {
		String result = null;
		switch (tempLocationType) {
			case 0: //
				result = "DEFAULT";
				break;
			case 1: //
				result = "SYSTEM";
				break;
			case 2: //
				result = "ECLIPSE";
				break;
			case 3: //
				result = "PROJECT";
				break;
			case 4: //
				result = "CUSTOM";
				break;
			default: 
				result = "DEFAULT";
				break;
		}
		return result;
	}
	
	public boolean isDefaultRepoLocation() {
		return useDefaultsForExpressCompilerRepo;
	} 


	public String getCustomTempLocation() {
		return customTempLocationFieldValue;
	}

	public String getCustomRepoLocation() {
		return customExpressCompilerRepoLocationFieldValue;
	}

}





