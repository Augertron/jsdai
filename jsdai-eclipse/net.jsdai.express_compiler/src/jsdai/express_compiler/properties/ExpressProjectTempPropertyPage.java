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

package jsdai.express_compiler.properties;

import java.io.File;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.preferences.ExpressProjectPreferences;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Standard main page for a wizard that is creates a project resource.
 * <p>
 * This page may be used by clients as-is; it may be also be subclassed to suit.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * mainPage = new ExpressProjectWizardPage("basicExpressProjectPage");
 * mainPage.setTitle("Project");
 * mainPage.setDescription("Create a new project resource.");
 * </pre>
 * </p>
 */
public class ExpressProjectTempPropertyPage extends PropertyPage {

	 IProject fProject;


    String fSystemTempDirectory;
  
    boolean useDefaultsForTemp = true;
    boolean useDefaultsForExpressCompilerRepo = true;
  
    boolean deleteTempOnExitIfNotDefault = true;
    boolean deleteRepoOnExitIfNotDefault = true;

    // initial value stores
//    private String initialTempFieldValue;
//    private String initialExpressCompilerRepoFieldValue;

//    private String initialTempLocationFieldValue;
//    private String initialExpressCompilerRepoLocationFieldValue;

    // the value the user has entered
    String customExpressCompilerRepoLocationFieldValue;


    int tempLocationType = 0;
		int tempLocationGlobalType = 0;
		String systemTempLocationFieldValue;
		String eclipseTempLocationFieldValue;
		String projectTempLocationFieldValue;
    String customTempLocationFieldValue;
		String currentTempLocationFieldValue;
		String original_temp_location;
		boolean was_original_temp_location_in_project;
		int original_temp_location_type;

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
//            boolean valid = validatePage();
//            setPageComplete(valid);
//            if (valid) ;
//              setExpressCompilerRepoLocationForSelection();
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
  
  
  
  public ExpressProjectTempPropertyPage() {
    super();
//        setPageComplete(false);
        customTempLocationFieldValue = ""; //$NON-NLS-1$
        customExpressCompilerRepoLocationFieldValue = ""; //$NON-NLS-1$

  //-------------------------
        useDefaultsForTemp = false;
        tempLocationType = 0;
//        tempLocationType = 0;
        customTempLocationFieldValue = "";
//        customTempLocationFieldValue = getTempDirectoryPathString();
        // default system or default Eclipse can be calculated when Finish is pressed
//        initialTempLocationFieldValue = customTempLocationFieldValue; 
        // to remove - let's initialize this value only once, later, no need to show the specific path here
        fSystemTempDirectory = ExpressCompilerUtils.getSystemTempDirectoryString();
//        initialExpressCompilerRepoLocationFieldValue = initialTempLocationFieldValue + "ExpressCompilerRepo"; 



    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }

    					String s_temp_location_global_type = null;					
    					String s_temp_location_type = null;
//						String s_delete_temp_on_exit = null;
						String s_temp_location_system = null;
						String s_temp_location_eclipse = null;
						String s_temp_location_project = null;


						try {

						    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);

							// SYSTEM ECLIPSE PROJECT
							s_temp_location_global_type = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);
//System.out.println("TEMP global type: " + s_temp_location_global_type);

              // DEFAULT SYSTEM ECLIPSE PROJECT CUSTOM
						  s_temp_location_type = prefs.get("fTempLocation", "DEFAULT");
//System.out.println("TEMP local type: " + s_temp_location_type);

							s_temp_location_system = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationSystem"));
							s_temp_location_eclipse = null;
							s_temp_location_project = null;
							
							// may duplicate any of the above three or be custom (depending on the type flags) 

//System.out.println("TEMP system: " + s_temp_location_system);
//System.out.println("TEMP eclipse: " + s_temp_location_eclipse);
//System.out.println("TEMP project: " + s_temp_location_project);
//System.out.println("TEMP location: " + s_temp_location);
						
//						  s_delete_temp_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteOnExit"));



							// try to initialize standard location types, in no success - create new ones here
							if (s_temp_location_system == null) {
								// make a new one, if it does not exist
								s_temp_location_system = ExpressCompilerUtils.getSystemTempDirectoryString();
								fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".tempLocationSystem"), s_temp_location_system);
							}
							systemTempLocationFieldValue = s_temp_location_system;

							if (s_temp_location_eclipse == null) {
//							    IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());
						    IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getUniqueIdentifier());
							 	String working_location_str = working_location.toString();
								s_temp_location_eclipse = working_location_str;
							}
							eclipseTempLocationFieldValue = s_temp_location_eclipse;

							if (s_temp_location_project == null) {
								s_temp_location_project = fProject.getLocation().toOSString() + File.separator + "temp";
							}
							projectTempLocationFieldValue = s_temp_location_project;
						  
						  
						  
						    prefs.flush();

					    } catch (BackingStoreException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						} catch (CoreException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						}



						was_original_temp_location_in_project = false;
						original_temp_location_type = 0;					
						
						if (s_temp_location_global_type == null) {
							s_temp_location_global_type = "SYSTEM";
						}

				  	if (s_temp_location_global_type.equalsIgnoreCase("SYSTEM")) {
				  		tempLocationGlobalType = 0;
				  	} else 
				  	if (s_temp_location_global_type.equalsIgnoreCase("ECLIPSE")) {
				  		tempLocationGlobalType = 1;
				  	} else 
				  	if (s_temp_location_global_type.equalsIgnoreCase("PROJECT")) {
				  		tempLocationGlobalType = 2;
				  	} else {
				  		// should not occur
				  		tempLocationGlobalType = 0;
				  	} 







					  if (s_temp_location_type != null) {
					  	if (s_temp_location_type.equalsIgnoreCase("DEFAULT")) {
								original_temp_location_type = 0;					
						  	tempLocationType = 0;
						  	if (s_temp_location_global_type.equalsIgnoreCase("SYSTEM")) {
						  		currentTempLocationFieldValue = systemTempLocationFieldValue;
						  		tempLocationGlobalType = 0;
						  	} else 
						  	if (s_temp_location_global_type.equalsIgnoreCase("ECLIPSE")) {
								  currentTempLocationFieldValue = eclipseTempLocationFieldValue;            
						  		tempLocationGlobalType = 1;
						  	} else 
						  	if (s_temp_location_global_type.equalsIgnoreCase("PROJECT")) {
						  		currentTempLocationFieldValue = projectTempLocationFieldValue;
						  		tempLocationGlobalType = 2;
									was_original_temp_location_in_project = true;
						  	} else {
						  		// should not occur
						  		currentTempLocationFieldValue = systemTempLocationFieldValue;
						  		tempLocationGlobalType = 0;
						  	} 
						  	
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("SYSTEM")) {
						  	tempLocationType = 1;
								original_temp_location_type = 1;					
					  		currentTempLocationFieldValue = systemTempLocationFieldValue;
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("ECLIPSE")) {
						  	tempLocationType = 2;
								original_temp_location_type = 2;					
							  currentTempLocationFieldValue = eclipseTempLocationFieldValue;            
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("PROJECT")) {
						  	tempLocationType = 3;
								original_temp_location_type = 3;					
					  		currentTempLocationFieldValue = projectTempLocationFieldValue;
								was_original_temp_location_in_project = true;
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("CUSTOM")) {
						  	tempLocationType = 4;
								original_temp_location_type = 4;					
					  		currentTempLocationFieldValue = customTempLocationFieldValue;
					  	} else {
						  	// should not occur
						  	tempLocationType = 0;
								original_temp_location_type = -1;					
					  		currentTempLocationFieldValue = systemTempLocationFieldValue;
					  	}
					  }	else {
					  	// should not occur
					  	tempLocationType = 0;
							original_temp_location_type = -1;					
					  	currentTempLocationFieldValue = systemTempLocationFieldValue;
					  }					
						original_temp_location = currentTempLocationFieldValue;
					
  }

    /**
     * Creates a new project creation wizard page.
     *
     * @param pageName the name of this page
     */
    /** (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public Control createContents(Composite parent) {

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_PROJECT_TEMP_PROPERTY_PAGE);


    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }
    					String s_temp_location_global_type = null;
						String s_temp_location_type = null;
//						String s_delete_temp_on_exit = null;
						String s_temp_location = null;
						String s_temp_location_system = null;
						String s_temp_location_eclipse = null;
						String s_temp_location_project = null;


						try {

// where better to put this initialization - to test, perhaps

						    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
							// SYSTEM ECLIPSE PROJECT
							s_temp_location_global_type = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);

              // DEFAULT SYSTEM ECLIPSE PROJECT CUSTOM
						  s_temp_location_type = prefs.get("fTempLocation", "DEFAULT");

							s_temp_location_system = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationSystem"));
							s_temp_location_eclipse = null;
							s_temp_location_project = null;
							
							// may duplicate any of the above three or be custom (depending on the type flags) 
							s_temp_location = prefs.get("tempLocation", null);

						
//						  s_delete_temp_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteOnExit"));




							// try to initialize standard location types, in no success - create new ones here
							if (s_temp_location_system == null) {
								// make a new one, if it does not exist
								s_temp_location_system = ExpressCompilerUtils.getSystemTempDirectoryString();
								fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".tempLocationSystem"), s_temp_location_system);
							}
							systemTempLocationFieldValue = s_temp_location_system;

							if (s_temp_location_eclipse == null) {
//							    IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());
						    IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getUniqueIdentifier());
							 	String working_location_str = working_location.toString();
								s_temp_location_eclipse = working_location_str;
							}
							eclipseTempLocationFieldValue = s_temp_location_eclipse;

							if (s_temp_location_project == null) {
								s_temp_location_project = fProject.getLocation().toOSString() + File.separator + "temp";
							}
							projectTempLocationFieldValue = s_temp_location_project;
						  
						    prefs.flush();

					    } catch (BackingStoreException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						} catch (CoreException e1) {
							ExpressCompilerPlugin.log(e1);
//							System.out.println("Exception - Problems with reading of persistent properties: " + e1);
							e1.printStackTrace();
						}


					
					was_original_temp_location_in_project = false;
					original_temp_location_type = 0;					

					if (s_temp_location == null) {
							s_temp_location = systemTempLocationFieldValue;
						}

						if (s_temp_location_global_type == null) {
							s_temp_location_global_type = "SYSTEM";
						}


				  	if (s_temp_location_global_type.equalsIgnoreCase("SYSTEM")) {
				  		tempLocationGlobalType = 0;
				  	} else 
				  	if (s_temp_location_global_type.equalsIgnoreCase("ECLIPSE")) {
				  		tempLocationGlobalType = 1;
				  	} else 
				  	if (s_temp_location_global_type.equalsIgnoreCase("PROJECT")) {
				  		tempLocationGlobalType = 2;
				  	} else {
				  		// should not occur
				  		tempLocationGlobalType = 0;
				  	} 



					  if (s_temp_location_type != null) {
					  	if (s_temp_location_type.equalsIgnoreCase("DEFAULT")) {
						  	tempLocationType = 0;
								original_temp_location_type = 0;					
						  	if (s_temp_location_global_type.equalsIgnoreCase("SYSTEM")) {
						  		currentTempLocationFieldValue = systemTempLocationFieldValue;
						  		tempLocationGlobalType = 0;
						  	} else 
						  	if (s_temp_location_global_type.equalsIgnoreCase("ECLIPSE")) {
								  currentTempLocationFieldValue = eclipseTempLocationFieldValue;            
						  		tempLocationGlobalType = 1;
						  	} else 
						  	if (s_temp_location_global_type.equalsIgnoreCase("PROJECT")) {
						  		currentTempLocationFieldValue = projectTempLocationFieldValue;
									was_original_temp_location_in_project = true;
						  		tempLocationGlobalType = 2;
						  	} else {
						  		// should not occur
						  		currentTempLocationFieldValue = systemTempLocationFieldValue;
						  		tempLocationGlobalType = 0;
						  	} 
						  	
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("SYSTEM")) {
						  	tempLocationType = 1;
								original_temp_location_type = 1;					
					  		currentTempLocationFieldValue = systemTempLocationFieldValue;
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("ECLIPSE")) {
						  	tempLocationType = 2;
								original_temp_location_type = 2;					
							  currentTempLocationFieldValue = eclipseTempLocationFieldValue;            
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("PROJECT")) {
						  	tempLocationType = 3;
								original_temp_location_type = 3;					
					  		currentTempLocationFieldValue = projectTempLocationFieldValue;
								was_original_temp_location_in_project = true;
					  	} else
					  	if (s_temp_location_type.equalsIgnoreCase("CUSTOM")) {
						  	tempLocationType = 4;
								original_temp_location_type = 4;					
					  		currentTempLocationFieldValue = customTempLocationFieldValue;
					  	} else {
						  	// should not occur
						  	tempLocationType = 0;
								original_temp_location_type = -1;					
					  		currentTempLocationFieldValue = systemTempLocationFieldValue;
					  	}
					  }	else {
					  	// should not occur
					  	tempLocationType = 0;
							original_temp_location_type = -1;					
					  	currentTempLocationFieldValue = systemTempLocationFieldValue;
					  }					

						original_temp_location = currentTempLocationFieldValue;

// end of the initialization


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
//    GridData gl2gd = new GridData(GridData.FILL_BOTH);
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
        
        /*
        
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

			*/


//        createExpressCompilerRepoLocationGroup(composite);
//        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
//        setControl(composite);
        noDefaultAndApplyButton();

        return composite;
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
        if (tempLocationType == 0)
	        useGlobalDefaultTempButton.setSelection(true);
				else
	        useGlobalDefaultTempButton.setSelection(false);
        useGlobalDefaultTempButton.setFont(font);


        final Button useSystemTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useSystemTempButton.setText("use system temp location, deleted when Eclipse is closed");
        useSystemTempButton.setText("use system temp location");
        if (tempLocationType == 1)
	        useSystemTempButton.setSelection(true);
  			else
  	      useSystemTempButton.setSelection(false);
//        tempLocationType = 0;
        useSystemTempButton.setFont(font);
//        useSystemTempButton.addSelectionListener(tempButtonSelectionListener);

        final Button useEclipseTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useEclipseTempButton.setText("use project-life-long-persistent temp location in the Eclipse workspace");
        useEclipseTempButton.setText("use temp location in the Eclipse workspace");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location, deleted when the project is deleted");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location, deleted when the project itself is deleted");
//        useEclipseTempButton.setText("use Eclipse project-specific persistent temp location");
        if (tempLocationType == 2)
        	useEclipseTempButton.setSelection(true);
				else
        	useEclipseTempButton.setSelection(false);
        useEclipseTempButton.setFont(font);
//        useEclipseTempButton.addSelectionListener(tempButtonSelectionListener);

        final Button useProjectTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useProjectTempButton.setText("use temp subdirectory inside the project");
        useProjectTempButton.setText("use \"temp\" subdirectory inside the project");
//        useProjectTempButton.setText("put temp directory inside the project, deleted with the project only, unless \"delete on exit\" is checked");
//        useProjectTempButton.setText("put temp directory inside the project");
        if (tempLocationType == 3)
        	useProjectTempButton.setSelection(true);
				else
        	useProjectTempButton.setSelection(false);
        useProjectTempButton.setFont(font);
//        useProjectTempButton.addSelectionListener(tempButtonSelectionListener);
        
        final Button useCustomTempButton = new Button(projectGroup, SWT.RADIO | SWT.LEFT);
//        useCustomTempButton.setText("specify custom temp location, never deleted unless \"delete on exit\" is checked");
        useCustomTempButton.setText("specify custom temp location");
//        useCustomTempButton.setText("specify custom temp location, never deleted unless \"delete on exit\" is checked");
//        useCustomTempButton.setText("specify custom temp location");
        if (tempLocationType == 4)
        	useCustomTempButton.setSelection(true);
				else
        	useCustomTempButton.setSelection(false);
        useCustomTempButton.setFont(font);
//        useCustomTempButton.addSelectionListener(tempButtonSelectionListener);


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
//System.out.println("selected: DEFAULT - global: " +  tempLocationGlobalType);               
                switch (tempLocationGlobalType) {
                	case 0: // SYSTEM
		                locationPathFieldForTemp.setText(systemTempLocationFieldValue);
                		break;
                	case 1: // ECLIPSE
		                locationPathFieldForTemp.setText(eclipseTempLocationFieldValue);
                		break;
                	case 2: // PROJECT
		                locationPathFieldForTemp.setText(projectTempLocationFieldValue);
                		break;
                	default: // ERROR = SYSTEM
		                locationPathFieldForTemp.setText(systemTempLocationFieldValue);
                		break;
                }
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
                browseButtonForTemp.setEnabled(false);
                
              } else
              if (useSystemTempButton.getSelection()) {
//              deleteOnExitTempButton.setSelection(true);
//              deleteOnExitTempButton.setEnabled(false);
//                fDeleteOnExit = true;
                tempLocationType = 1;
//                locationPathFieldForTemp.setText("in system temp, deleted on exit");
                locationPathFieldForTemp.setText(systemTempLocationFieldValue);
//                locationPathFieldForTemp.setText("in the system temp");
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
                browseButtonForTemp.setEnabled(false);
              } else
                if (useEclipseTempButton.getSelection()) {
//                deleteOnExitTempButton.setSelection(false);
//                deleteOnExitTempButton.setEnabled(false);
//                fDeleteOnExit = false;
                tempLocationType = 2;
//                locationPathFieldForTemp.setText("temp in the Eclipse workspace, deleted when project deleted");
                locationPathFieldForTemp.setText(eclipseTempLocationFieldValue);
//                locationPathFieldForTemp.setText("temp in the Eclipse workspace");
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
                browseButtonForTemp.setEnabled(false);
              } else
              if (useProjectTempButton.getSelection()) {
//                deleteOnExitTempButton.setSelection(false);
//                deleteOnExitTempButton.setEnabled(true);
//                fDeleteOnExit = false;
                tempLocationType = 3;
                locationPathFieldForTemp.setEnabled(false);
                locationLabelForTemp.setEnabled(false);
                browseButtonForTemp.setEnabled(false);
                locationPathFieldForTemp.setText(projectTempLocationFieldValue);
//                locationPathFieldForTemp.setText("\"temp\" subdirectory inside the project");
              } else
              if (useCustomTempButton.getSelection()) {
//                deleteOnExitTempButton.setSelection(false);
//                deleteOnExitTempButton.setEnabled(true);
//                fDeleteOnExit = false;

//System.out.println("SELECTED CUSTEM - hahahaha");
                tempLocationType = 4;
                locationPathFieldForTemp.setText(currentTempLocationFieldValue);
//                locationPathFieldForTemp.setText(customTempLocationFieldValue);
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

/*
        if (initialTempLocationFieldValue == null)
            locationPathFieldForTemp.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForTemp.setText(initialTempLocationFieldValue);
*/

				
				
				switch (tempLocationType) {
					case 0: // DEFAULT
						switch (tempLocationGlobalType) {
							case 0: // SYSTEM
				        locationPathFieldForTemp.setText(systemTempLocationFieldValue);
								break;
							case 1: // ECLIPSE
				        locationPathFieldForTemp.setText(eclipseTempLocationFieldValue);
								break;
							case 2: // PROJECT
				        locationPathFieldForTemp.setText(projectTempLocationFieldValue);
								break;
							default: // ERROR = SYSTEM
				        locationPathFieldForTemp.setText(systemTempLocationFieldValue);
								break;
						}
						break;
					case 1: // SYSTEM
		        locationPathFieldForTemp.setText(systemTempLocationFieldValue);
						break;
					case 2: // ECLIPSE
		        locationPathFieldForTemp.setText(eclipseTempLocationFieldValue);
						break;
					case 3: // PROJECT
		        locationPathFieldForTemp.setText(projectTempLocationFieldValue);
						break;
					case 4: // CUSTOM
		        locationPathFieldForTemp.setText(customTempLocationFieldValue);
						break;
					default: // ERROR = DEFAULT
						switch (tempLocationGlobalType) {
							case 0: // SYSTEM
				        locationPathFieldForTemp.setText(systemTempLocationFieldValue);
								break;
							case 1: // ECLIPSE
				        locationPathFieldForTemp.setText(eclipseTempLocationFieldValue);
								break;
							case 2: // PROJECT
				        locationPathFieldForTemp.setText(projectTempLocationFieldValue);
								break;
							default: // ERROR = SYSTEM
				        locationPathFieldForTemp.setText(systemTempLocationFieldValue);
								break;
						}
						break;
				}
				


        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);

//        if (!enabled) {
//          locationPathFieldForTemp.setText("in the system temp");
//        }
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

//        String dirName = getTempLocationFieldValue();
       
       /*
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }
				*/
        
        dialog.setFilterPath(locationPathFieldForTemp.getText());


        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customTempLocationFieldValue = selectedDirectory;
						currentTempLocationFieldValue = customTempLocationFieldValue;
            locationPathFieldForTemp.setText(customTempLocationFieldValue);
        }

    if (locationPathFieldForTemp.getText().equalsIgnoreCase(fSystemTempDirectory)) {
//      deleteOnExitTempButton.setSelection(true);
//      deleteOnExitTempButton.setEnabled(false);
    } else {
//      deleteOnExitTempButton.setEnabled(true);
    }
    
    }


    void setExpressCompilerRepoLocationForSelection() {
        String express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo)
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    }
    

    void setTempLocationForSelection() {
//RRTODO        String temp_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "temp";
      String temp_default = "";
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
//RRTODO        String project_default = NewExpressProjectWizard.getMainPage().getDefaultLocationForName(NewExpressProjectWizard.getMainPage().getProjectNameFieldValue());
String project_default = "";
        String temp_default = project_default + File.separator + "temp";
//RRTODO        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) temp_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "temp";
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

            if    (tempLocationType == 3) {
  
      if (customTempLocationFieldValue == null) {
        return false;
        
      }
 
      if (customTempLocationFieldValue.equals("")) {
        return false;
        
      }
 }
 
//    return true;
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
        result = "SYSTEM";
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

		/*
			
				ok, so what we want to do here?
				1) to save back the temp type
				2) to save the value so that it is directly accessable,
				if the type is eclipse, then the value = eclipse as well.
				3) to save the standart type values?
				perhaps not, perhaps only if their reading was not successful, and that is already done in the initialization stage,
				otherwise we may get new and new temp locations, especially with tose unique names of directories.
		
		
		*/

		public boolean performOk() {


			/*

					I also want to handle deletion/creation of directories here:
					if temp directory is changed, then it makes sense to delete the old one and everything inside it,
					except if the old one was custom - perhaps leave those to the user
					Also, if the new one is temp inside the project, it actually has to be created so that the compiler can work.
					
					so we need to know 2 hings:
					1) if the temp location is actually being changed, and if so, what is the old one?
					2) if the previous location was not temp in the project, but the new one - is.
					3) (the 3rd of 2): if previous was temp in the project and no longer is, or if previous was not temp in project and new one is,
						 then also refresh the project in Navigator after deletion/creation
					
					
					IMPORTANT - if in global preferences the location is changed to PROJECT, then also temp directories are needed in all express projects
					that use default temp type - sholud we search for all such projects and create temp?
					Or rather support temp creation on-the-fly, if not found, when running compilation?
					I think, better support in the compilator, although the first solution is also possible:
					 - get all the projects from the workspace
					 - go in the loop through the projects
					 	- check if the project is Express project (has express nature)
					 		- if so, check if the project uses DEFAULT temp location
					 			- if so, create temp, also delete previous location, if not custom
					 			  also, if previous global was temp in project and now is not - refresh all the projects.
					 			   
	       Refreshing perhaps is a good idea anyway, you may have custom location inside this or other project, and if you add one, you may want to see it.
	       
	
	
			*/
			
			String location_to_save = null;

			try {

			    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
			    prefs.put("fTempLocation", getTempLocationTypeStr());
			    if(tempLocationType == 5) {
			    	prefs.put("tempLocation", customTempLocationFieldValue);
			    }


//			  switch (tempLocationType) {
//			  	case 0: // DEFAULT
//			  		switch (tempLocationGlobalType) { // SYSTEM ECLIPSE PROJECT
//							case 0: // SYSTEM
//					  		location_to_save = systemTempLocationFieldValue;
//				  			break;
//							case 1: // ECLIPSE
//					  		location_to_save = eclipseTempLocationFieldValue;
//					  		break;
//							case 2: // PROJECT
//					  		location_to_save = projectTempLocationFieldValue;
//					  		break;
//							default: // ERROR = SYSTEM
//				  			location_to_save = systemTempLocationFieldValue;
//				  			break;
//						}
//			  		break; 
//			  	case 1: // SYSTEM
//		  			location_to_save = systemTempLocationFieldValue;
//		  			break;
//			  	case 2: // ECLIPSE
//			  		location_to_save = eclipseTempLocationFieldValue;
//			  		break;
//		  		case 3: // PROJECT
//		  			location_to_save = projectTempLocationFieldValue;
//		  			break;
//			  	case 4: // CUSTOM
//			  		location_to_save = customTempLocationFieldValue;
//			  		break;
//		  		default: // ERROR = DEFAULT
//		  			prefs.put("fTempLocation", "DEFAULT");
//			  		switch (tempLocationGlobalType) { // SYSTEM ECLIPSE PROJECT
//							case 0: // SYSTEM
//					  		location_to_save = systemTempLocationFieldValue;
//				  			break;
//							case 1: // ECLIPSE
//					  		location_to_save = systemTempLocationFieldValue;
//					  		break;
//							case 2: // PROJECT
//					  		location_to_save = projectTempLocationFieldValue;
//					  		break;
//							default: // ERROR = SYSTEM
//				  			location_to_save = systemTempLocationFieldValue;
//				  			break;
//						}
//
//		  			break;
//		  	}
//				
//				if (location_to_save == null) {
//					location_to_save = systemTempLocationFieldValue;
//				}
//				
////				prefs.put("tempLocation", location_to_save);
//
//				if (!location_to_save.equalsIgnoreCase(original_temp_location)) {
//					// location of temp changed
//					if (original_temp_location_type != 4) {
//						// delete the old one if it is not CUSTOM (BTW, global does not support custom, so no need to handle DEFAULT)
//						ExpressCompilerUtils.deleteAllFilesAndDirectories(original_temp_location);
//					}
//		
//					if ((tempLocationType == 3) || (tempLocationType == 0) && (tempLocationGlobalType == 2)) {
//						// create temp in project/temp if the new one is in project
//						IFolder temp_folder = fProject.getFolder("temp");
//						if (!temp_folder.exists()) {
//							temp_folder.create(true, true, null);
//						}
//					}
//				}
		    prefs.flush();
		    fProject.refreshLocal(IResource.DEPTH_ONE, null);



		    } catch (BackingStoreException e) {
				ExpressCompilerPlugin.log(e);
				// e.printStackTrace();
			} catch (CoreException e) {
				ExpressCompilerPlugin.log(e);
				// e.printStackTrace();
			}


	
/*		
		if (tempLocationType != original_temp_location_type) {
			// if ==, then either the location has not changed, or one custom location changed to another custom location, 
			// and we do not touch custom locations
		
			// there might be a situation where the location is still not changed:
			// if the previous/new local type == the global type and new/previous was default
			// therefore, bettter just compare actual directories, less checks needed
		}
*/

			return true;
		}


	/**
	 * Returns temp file location for this project based on projects property settings.
	 * 
	 * @param project a project to get temp location for
	 * @return temp file location name associated with the project 
	 * @throws CoreException
	 */
	public static final String getTempFileLocation(IProject project) throws CoreException {
	    IEclipsePreferences prefs = new ProjectScope(project).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
		String tempLocationTypeStr = prefs.get("fTempLocation", "DEFAULT");
		String tempLocation = prefs.get("tempLocation", null);
		int tempLocationTypeInt; // 0 - SYSTEM, 1 - ECLIPSE, 2 - PROJECT, 3 - TEMP
		if (tempLocationTypeStr.equalsIgnoreCase("DEFAULT")) {
			String flagGlobalDefaultTempLocationType  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);
			if (flagGlobalDefaultTempLocationType == null || flagGlobalDefaultTempLocationType.equalsIgnoreCase("SYSTEM")) {
				tempLocationTypeInt = 0;
			} else if (flagGlobalDefaultTempLocationType.equalsIgnoreCase("ECLIPSE")) {
				tempLocationTypeInt = 1;
			} else if (flagGlobalDefaultTempLocationType.equalsIgnoreCase("PROJECT")) {
				tempLocationTypeInt = 2;
			} else {
				tempLocationTypeInt = 3;
			}
		} else if (tempLocationTypeStr.equalsIgnoreCase("SYSTEM")) {
			tempLocationTypeInt = 0;
		} else if (tempLocationTypeStr.equalsIgnoreCase("ECLIPSE")) {
			tempLocationTypeInt = 1;
		} else if (tempLocationTypeStr.equalsIgnoreCase("PROJECT")) {
			tempLocationTypeInt = 2;
		} else {
			tempLocationTypeInt = 3;
		}

		if(tempLocationTypeInt == 3 && tempLocation == null) {
			tempLocationTypeInt = 0;
		}

		String tempFileLocation;
		switch (tempLocationTypeInt) {
		case 0:
			String system_temp_location_str = project.getPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".tempLocationSystem"));
			if(system_temp_location_str == null) {
				system_temp_location_str = ExpressCompilerUtils.getSystemTempDirectoryString();
				project.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".tempLocationSystem"), system_temp_location_str);
			}
			tempFileLocation = system_temp_location_str;
			break;

		case 1:
		    IPath working_location = project.getWorkingLocation(ExpressCompilerPlugin.getUniqueIdentifier());
		    tempFileLocation = working_location.toString();
			break;

		case 2:
			IFolder projectTempLocation = project.getFolder("temp");
			tempFileLocation = projectTempLocation.getLocation().toOSString();
			break;

		default:
			tempFileLocation = tempLocation;
			break;
		}
		new File(tempFileLocation).mkdirs();
		return tempFileLocation;
	}

}





