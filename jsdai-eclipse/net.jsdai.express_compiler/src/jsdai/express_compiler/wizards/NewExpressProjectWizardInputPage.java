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

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

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
public class NewExpressProjectWizardInputPage extends WizardPage {


		static NewExpressProjectWizardInputPage fgNewExpressProjectWizardInputPage;

    int fInputType = 0;
    int fUseExcludeList = 0;
  int fRecurseDirectory;

	String fProjectName = "";

		Button inputExcludeDefaultButton;
		Button inputExcludeYesButton;
		Button inputExcludeNoButton;

	Button inputRecurseDefaultButton;
	Button inputRecurseYesButton;
	Button inputRecurseNoButton;


    boolean useDefaultsForExpressFiles = true;
    boolean useDefaultsForComplexEntities = true;
    boolean useDefaultsForShortNames = true;

    // initial value stores
//    private String initialExpressFileFieldValue;
//    private String initialComplexEntityFieldValue;
//    private String initialShortNameFieldValue;

    private String initialExpressFileLocationFieldValue;
    private String initialComplexEntityLocationFieldValue;
    private String initialShortNameLocationFieldValue;

    // the value the user has entered
    String customExpressFileLocationFieldValue;
    String customComplexEntityLocationFieldValue;
    String customShortNameLocationFieldValue;

    // widgets

    Text locationPathFieldForExpressFiles;
    Text locationPathFieldForComplexEntities;
    Text locationPathFieldForShortNames;

    Label locationLabelForExpressFiles;
    Label locationLabelForComplexEntities;
    Label locationLabelForShortNames;

    Button browseButtonForExpressFiles;
    Button browseButtonForComplexEntities;
    Button browseButtonForShortNames;

    boolean fGlobal_is_list_input;
//  private static final int SIZING_TEXT_FIELD_WIDTH = 400;


    private Listener expressFileLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid);
            if (valid)
            	setShortNameLocationForSelection();
        }
    };
    
    private Listener shortNameLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid);
            if (valid)
            	setComplexEntityLocationForSelection();
        }
    };

    private Listener complexEntityLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validatePage());
        }
    };
    
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;


	
	public NewExpressProjectWizardInputPage(String pageName) {
		super(pageName);
    
    setPageComplete(false);

    fInputType = 0;
    fUseExcludeList = 0;

        customExpressFileLocationFieldValue = ""; //$NON-NLS-1$
        customShortNameLocationFieldValue = ""; //$NON-NLS-1$
        customComplexEntityLocationFieldValue = ""; //$NON-NLS-1$


	//-------------------------
        useDefaultsForExpressFiles = true;
        useDefaultsForShortNames = true;
        useDefaultsForComplexEntities = true;
//        customExpressFileLocationFieldValue = getExpressFileDirectoryPathString();

//        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files";

//System.out.println("instance ((())): " + NewExpressProjectWizard.getInstance());

//        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getInstance().fProjectPath;

        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getInstance().fProjectPath +
                                               File.separator +
                                               NewExpressProjectWizard.getInstance().fProjectName +
                                               File.separator +
																							 "Express files";
				initialExpressFileLocationFieldValue = toSystem(initialExpressFileLocationFieldValue);																			 
                                               
        initialShortNameLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names";
        initialShortNameLocationFieldValue = toSystem(initialShortNameLocationFieldValue);
        initialComplexEntityLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities";
				initialComplexEntityLocationFieldValue = toSystem(initialComplexEntityLocationFieldValue);

				fProjectName = NewExpressProjectWizard.getInstance().fProjectName;
				if (fProjectName == null) fProjectName = "";

		fgNewExpressProjectWizardInputPage = this;


	}


	public static NewExpressProjectWizardInputPage getInstance() {
		return fgNewExpressProjectWizardInputPage;
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

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.NEW_EXPRESS_PROJECT_WIZARD_IO_PAGE);

        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);


        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));



        createInputGroup(composite);   // includes also use exclude list or not
        createShortNameLocationGroup(composite);
        createComplexEntityLocationGroup(composite);





				Label note = new Label(composite, SWT.WRAP);
//				note.setText("NOTE: \"default\" means the corresponding setting in the global Express preferences");
        note.setText("NOTE: \"default\" radio button means the corresponding setting in the global Express preferences");
				//note.setColor(blue);
				note.setForeground(new Color(parent.getDisplay(),0,0,100));			
		        GridData labelgd4 = new GridData();
                labelgd4.horizontalIndent = 10;
                labelgd4.horizontalAlignment = SWT.FILL;
		        note.setLayoutData(labelgd4);


        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    private final void createInputGroup(Composite parent) {
        Font font = parent.getFont();
        Group inputGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        inputGroup.setLayout(layout);
        inputGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inputGroup.setFont(font);
//        inputGroup.setText("Input of express files for the compiler");
        inputGroup.setText("Express files");

        Composite inputInnerGroup = new Composite(inputGroup, SWT.NONE);
        GridLayout layout3 = new GridLayout();
        layout3.numColumns = 1;
        inputInnerGroup.setLayout(layout3);
//        inputInnerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        //		gd.widthHint = 150;
//        gd.horizontalAlignment = SWT.END;
//        gd.horizontalSpan = 1;
         gd.widthHint = 80;        
 
         gd.grabExcessHorizontalSpace = true;
         gd.minimumWidth = convertWidthInCharsToPixels(40);
         inputInnerGroup.setLayoutData(gd);
//        inputInnerGroup.setFont(font);
//        inputInnerGroup.setText("Input of express files for the compiler");



        final Button inputDefaultButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputDefaultButton.setText("use the setting in the global express project preferences");



        String default_button_value = "default";
				boolean global_is_list_input = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_INCLUDE);
				//fGlobal_is_list_input = global_is_list_input;
				if (global_is_list_input) {
        	default_button_value += " (list)";
        } else {
	       	default_button_value += " (directory)";
        }

        inputDefaultButton.setText(default_button_value);


//        inputDefaultButton.setText("default");
        inputDefaultButton.setSelection(true);
    		fInputType = 0;
        inputDefaultButton.setFont(font);

        final Button inputListButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputListButton.setText("the list file");
				String list_string = "list (" +  fProjectName + ".exl file)";


        inputListButton.setText(list_string);


        inputListButton.setSelection(false);
        inputListButton.setFont(font);

        final Button inputDirButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputDirButton.setText("the Express directory (non-recursively)");
        inputDirButton.setText("directory");
        inputDirButton.setSelection(false);
        inputDirButton.setFont(font);

//        final Button inputRecursiveButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputRecursiveButton.setText("the Express directory (recursively)");
//        inputRecursiveButton.setSelection(false);
//        inputRecursiveButton.setFont(font);


        createExpressFileLocationGroup(inputInnerGroup);


		GridData buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//		GridData buttonDataR = new GridData();
		buttonDataR.widthHint = 70;
//        GridData buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 1;
        inputDefaultButton.setLayoutData(buttonDataR);

//		buttonDataR = new GridData();
		buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
		buttonDataR.widthHint = 70;
        buttonDataR.horizontalSpan = 1;
        inputListButton.setLayoutData(buttonDataR);

		buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//		buttonDataR = new GridData();
		buttonDataR.widthHint = 70;
        buttonDataR.horizontalSpan = 1;
		buttonDataR.grabExcessHorizontalSpace = true;
		buttonDataR.minimumWidth = convertWidthInCharsToPixels(40);
        inputDirButton.setLayoutData(buttonDataR);

		buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//        buttonDataR = new GridData();
		buttonDataR.widthHint = 70;
		buttonDataR.grabExcessHorizontalSpace = true;
		buttonDataR.minimumWidth = convertWidthInCharsToPixels(40);
		buttonDataR.horizontalSpan = 1;
//        inputRecursiveButton.setLayoutData(buttonDataR);

        //gd.grabExcessHorizontalSpace = true;
        //gd.minimumWidth = convertWidthInCharsToPixels(40);

    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
							if (inputDefaultButton.getSelection()) {
    						fInputType = 0; // default
								inputExcludeDefaultButton.setEnabled(true);
						  	inputExcludeYesButton.setEnabled(true);
							  inputExcludeNoButton.setEnabled(true);
            	} else
							if (inputListButton.getSelection()) {
    						fInputType = 1; // list
								inputExcludeDefaultButton.setEnabled(false);
						  	inputExcludeYesButton.setEnabled(false);
							  inputExcludeNoButton.setEnabled(false);
            	} else
							if (inputDirButton.getSelection()) {
    						fInputType = 2; //  directory - flat
								inputExcludeDefaultButton.setEnabled(true);
						  	inputExcludeYesButton.setEnabled(true);
							  inputExcludeNoButton.setEnabled(true);
  //          	} else
	//					if (inputRecursiveButton.getSelection()) {
  //  						fInputType = 3; //  directory - recursive
	//							inputExcludeDefaultButton.setEnabled(true);
	//					  	inputExcludeYesButton.setEnabled(true);
	//						  inputExcludeNoButton.setEnabled(true);
            	}
            }
        };
				inputDefaultButton.addSelectionListener(listener);
			  inputListButton.addSelectionListener(listener);
			  inputDirButton.addSelectionListener(listener);
//			  inputRecursiveButton.addSelectionListener(listener);

//			createInputGroupUseExcludeSubgroup(inputGroup);
    createInputGroupUseRecursionSubgroup(inputInnerGroup);
    createInputGroupUseExcludeSubgroup(inputInnerGroup);

    }


private final void createInputGroupUseRecursionSubgroup(Composite inputGroup) {

        Font font = inputGroup.getFont();


        Group inputRecurseGroup = new Group(inputGroup, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 3;
        inputRecurseGroup.setLayout(layout2);
				GridData inputRecurseGroupData = new GridData(GridData.FILL_HORIZONTAL);
        // GridData inputRecurseGroupData = new GridData();
//        GridData inputExcludeGroupData = new GridData();
//        inputExcludeGroupData.widthHint = 100;
        //inputExcludeGroupData.horizontalAlignment = SWT.END;
        //inputExcludeGroupData.horizontalSpan = 1;

        inputRecurseGroup.setLayoutData(inputRecurseGroupData);
        inputRecurseGroup.setFont(font);
        inputRecurseGroup.setText("recurse the directory in search of express files");
        
        inputRecurseDefaultButton = new Button(inputRecurseGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeDefaultButton.setText("use the setting in the global express project preferences");

        String recurse_directory_default_value = "default";
				boolean global_is_recurse = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.RECURSIVE_COMPILE);
        if (global_is_recurse) {
        	recurse_directory_default_value += " (yes)";
        } else {
	       	recurse_directory_default_value += " (no)";
        }


        inputRecurseDefaultButton.setText(recurse_directory_default_value);
      if (fRecurseDirectory == 0)
        inputRecurseDefaultButton.setSelection(true);
      else
        inputRecurseDefaultButton.setSelection(false);
//        fUseExcludeList = 0;
        inputRecurseDefaultButton.setFont(font);

        inputRecurseYesButton = new Button(inputRecurseGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeYesButton.setText("use the exclusion list");
        inputRecurseYesButton.setText("yes");
      if (fRecurseDirectory == 1)
        inputRecurseYesButton.setSelection(true);
      else
        inputRecurseYesButton.setSelection(false);
        inputRecurseYesButton.setFont(font);

        inputRecurseNoButton = new Button(inputRecurseGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeNoButton.setText("do not use the exclusion list");
        inputRecurseNoButton.setText("no");
      if (fRecurseDirectory == 2)
        inputRecurseNoButton.setSelection(true);
      else
        inputRecurseNoButton.setSelection(false);
        inputRecurseNoButton.setFont(font);

        GridData buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
//        GridData buttonDataR2 = new GridData();
        buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputRecurseDefaultButton.setLayoutData(buttonDataR2);

        buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
        buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputRecurseYesButton.setLayoutData(buttonDataR2);

        buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
        buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputRecurseNoButton.setLayoutData(buttonDataR2);


    if (fInputType == 1) {
      inputRecurseDefaultButton.setEnabled(false);
      inputRecurseYesButton.setEnabled(false);
      inputRecurseNoButton.setEnabled(false);
     } else 
     if (fInputType == 0) {
     		if (fGlobal_is_list_input) {
  		    inputRecurseDefaultButton.setEnabled(false);
	    	  inputRecurseYesButton.setEnabled(false);
      		inputRecurseNoButton.setEnabled(false);
				} else {
			  inputRecurseDefaultButton.setEnabled(true);
	    	  inputRecurseYesButton.setEnabled(true);
      		  inputRecurseNoButton.setEnabled(true);
				}
			} else {
				inputRecurseDefaultButton.setEnabled(true);
	   	  		inputRecurseYesButton.setEnabled(true);
	   	  		inputRecurseNoButton.setEnabled(true);
			}


    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

              if (inputRecurseDefaultButton.getSelection()) {
                fRecurseDirectory = 0; // default
//System.out.println("use exclude - default");
              } else
              if (inputRecurseYesButton.getSelection()) {
                fRecurseDirectory = 1; // yes, use
//                System.out.println("use exclude - yes");
              } else
              if (inputRecurseNoButton.getSelection()) {
                fRecurseDirectory = 2; // no, do not use
//                System.out.println("use exclude - no");
              }
            }
        };

        inputRecurseDefaultButton.addSelectionListener(listener);
        inputRecurseYesButton.addSelectionListener(listener);
        inputRecurseNoButton.addSelectionListener(listener);


  }



    private final void createInputGroupUseExcludeSubgroup(Composite inputGroup) {

        Font font = inputGroup.getFont();

        Group inputExcludeGroup = new Group(inputGroup, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 3;
        inputExcludeGroup.setLayout(layout2);
        GridData inputExcludeGroupData = new GridData(GridData.FILL_HORIZONTAL);
//        GridData inputExcludeGroupData = new GridData();
//        GridData inputExcludeGroupData = new GridData();
//				inputExcludeGroupData.widthHint = 100;
        //inputExcludeGroupData.horizontalAlignment = SWT.END;
        //inputExcludeGroupData.horizontalSpan = 1;

        inputExcludeGroup.setLayoutData(inputExcludeGroupData);
        inputExcludeGroup.setFont(font);
//        inputExcludeGroup.setText("Using exclusion list with directory input");
//        inputExcludeGroup.setText("use exclusion list (file " + fProject.getName() + "_excluded.exl) with directory input");
        inputExcludeGroup.setText("use exclusion list (file " + fProjectName + "_excluded.exl) with directory input");
        
        inputExcludeDefaultButton = new Button(inputExcludeGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeDefaultButton.setText("use the setting in the global express project preferences");

        String use_exclusion_default_value = "default";
				boolean global_is_recurse = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_EXCLUDE);
        if (global_is_recurse) {
        	use_exclusion_default_value += " (yes)";
        } else {
	       	use_exclusion_default_value += " (no)";
        }


        inputExcludeDefaultButton.setText(use_exclusion_default_value);

//        inputExcludeDefaultButton.setText("default");
        inputExcludeDefaultButton.setSelection(true);
    		fUseExcludeList = 0;
        inputExcludeDefaultButton.setFont(font);

        inputExcludeYesButton = new Button(inputExcludeGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeYesButton.setText("use the exclusion list");
        inputExcludeYesButton.setText("use");
        inputExcludeYesButton.setSelection(false);
        inputExcludeYesButton.setFont(font);

        inputExcludeNoButton = new Button(inputExcludeGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeNoButton.setText("do not use the exclusion list");
        inputExcludeNoButton.setText("do not use");
        inputExcludeNoButton.setSelection(false);
        inputExcludeNoButton.setFont(font);

				GridData buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
//        GridData buttonDataR2 = new GridData();
				buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputExcludeDefaultButton.setLayoutData(buttonDataR2);

				buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
				buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputExcludeYesButton.setLayoutData(buttonDataR2);

				buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
				buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputExcludeNoButton.setLayoutData(buttonDataR2);

    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
							if (inputExcludeDefaultButton.getSelection()) {
    						fUseExcludeList = 0; // default
            	} else
							if (inputExcludeYesButton.getSelection()) {
    						fUseExcludeList = 1; // yes, use
            	} else
							if (inputExcludeNoButton.getSelection()) {
    						fUseExcludeList = 2; // no, do not use
            	}
            }
        };

				inputExcludeDefaultButton.addSelectionListener(listener);
			  inputExcludeYesButton.addSelectionListener(listener);
			  inputExcludeNoButton.addSelectionListener(listener);
		
		}
    
    


    private final void createExpressFileLocationGroup(Composite parent) {

        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
//        projectGroup.setText("Location of express files");
        projectGroup.setText("specify the directory with express files");

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsButton.setText("use default directory \"Express files\"");
        useDefaultsButton.setSelection(useDefaultsForExpressFiles);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedExpressFileLocationGroup(projectGroup, !useDefaultsForExpressFiles);


//// note


				Label note = new Label(projectGroup, SWT.SHADOW_IN | SWT.WRAP);
//				Label note3 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
	    	note.setText("HINT: you may specify as the location of express files another Eclipse project " +
	    			"or a directory in it where you already have the express files you need for this project");
//	    	note3.setText("if you need the generated java files for debugging or modifying purposes");
				//note1.setForeground(new Color(parent.getDisplay(),0,0,128));			
				//note2.setForeground(new Color(parent.getDisplay(),0,0,128));			
				//note3.setForeground(new Color(parent.getDisplay(),0,0,128));			
	    	note.setForeground(new Color(projectGroup.getDisplay(),0,80,80));			
//	    	note3.setForeground(new Color(parent.getDisplay(),0,80,80));			
        GridData labelgd = new GridData();
  //      GridData labelgd3 = new GridData();
        labelgd.horizontalSpan = 3;
        labelgd.grabExcessHorizontalSpace = true;
        labelgd.horizontalAlignment = SWT.FILL;
        labelgd.horizontalIndent = 10;
        note.setLayoutData(labelgd);
//        note3.setLayoutData(labelgd3);



////




        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForExpressFiles = useDefaultsButton.getSelection();
                browseButtonForExpressFiles.setEnabled(!useDefaultsForExpressFiles);
                locationPathFieldForExpressFiles.setEnabled(!useDefaultsForExpressFiles);
                locationLabelForExpressFiles.setEnabled(!useDefaultsForExpressFiles);
                if (useDefaultsForExpressFiles) {
                    customExpressFileLocationFieldValue = locationPathFieldForExpressFiles.getText();
                    setLocationForSelection();
                } else {
//                	if ((customExpressFileLocationFieldValue == null) || (customExpressFileLocationFieldValue.equals(""))) {
	                		customExpressFileLocationFieldValue = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString());
//                	}
                  locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
                }
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }

    private void createUserSpecifiedExpressFileLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForExpressFiles = new Label(projectGroup, SWT.NONE);
//        locationLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationLabel);
        locationLabelForExpressFiles.setText("Directory:");
        locationLabelForExpressFiles.setEnabled(enabled);
        locationLabelForExpressFiles.setFont(font);

        // project location entry field
        locationPathFieldForExpressFiles = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForExpressFiles.setLayoutData(data);
        locationPathFieldForExpressFiles.setEnabled(enabled);
        locationPathFieldForExpressFiles.setFont(font);

        // browse button
        browseButtonForExpressFiles = new Button(projectGroup, SWT.PUSH);
//        browseButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_browseLabel);
        browseButtonForExpressFiles.setText("Browse...");
        browseButtonForExpressFiles.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleExpressFileLocationBrowseButtonPressed();
            }
        });

        browseButtonForExpressFiles.setEnabled(enabled);
        browseButtonForExpressFiles.setFont(font);
        setButtonLayoutData(browseButtonForExpressFiles);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialExpressFileLocationFieldValue == null)
            locationPathFieldForExpressFiles.setText(toSystem(Platform.getLocation().toOSString()));
        else
            locationPathFieldForExpressFiles.setText(toSystem(initialExpressFileLocationFieldValue));
        		locationPathFieldForExpressFiles.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForExpressFiles.addListener(SWT.Modify, expressFileLocationModifyListener);
    }

		
    // short names
    private final void createShortNameLocationGroup(Composite parent) {

        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
//        projectGroup.setText("Location of short names");
        projectGroup.setText("Short names");

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
//        useDefaultsButton.setText("Use default (directory \"Short names\" inside the project)");
        useDefaultsButton.setText("use default location (directory \"Short names\" inside the project)");
        useDefaultsButton.setSelection(useDefaultsForShortNames);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedShortNameLocationGroup(projectGroup, !useDefaultsForShortNames);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForShortNames = useDefaultsButton.getSelection();
                browseButtonForShortNames.setEnabled(!useDefaultsForShortNames);
                locationPathFieldForShortNames.setEnabled(!useDefaultsForShortNames);
                locationLabelForShortNames.setEnabled(!useDefaultsForShortNames);
                if (useDefaultsForShortNames) {
                    customShortNameLocationFieldValue = locationPathFieldForShortNames.getText();
                    setLocationForSelection();
                } else {
                		customShortNameLocationFieldValue = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString());
                    locationPathFieldForShortNames.setText(customShortNameLocationFieldValue);
                }
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }





    private void createUserSpecifiedShortNameLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForShortNames = new Label(projectGroup, SWT.NONE);
//        locationLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationLabel);
        locationLabelForShortNames.setText("Directory:");
        locationLabelForShortNames.setEnabled(enabled);
        locationLabelForShortNames.setFont(font);

        // project location entry field
        locationPathFieldForShortNames = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForShortNames.setLayoutData(data);
        locationPathFieldForShortNames.setEnabled(enabled);
        locationPathFieldForShortNames.setFont(font);

        // browse button
        browseButtonForShortNames = new Button(projectGroup, SWT.PUSH);
//        browseButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_browseLabel);
        browseButtonForShortNames.setText("Browse...");
        browseButtonForShortNames.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleShortNameLocationBrowseButtonPressed();
            }
        });

        browseButtonForShortNames.setEnabled(enabled);
        browseButtonForShortNames.setFont(font);
        setButtonLayoutData(browseButtonForShortNames);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialShortNameLocationFieldValue == null)
            locationPathFieldForShortNames.setText(toSystem(Platform.getLocation().toOSString()));
        else
            locationPathFieldForShortNames.setText(toSystem(initialShortNameLocationFieldValue));
        locationPathFieldForShortNames.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForShortNames.addListener(SWT.Modify, shortNameLocationModifyListener);
    }


// complex entities

    private final void createComplexEntityLocationGroup(Composite parent) {

        Font font = parent.getFont();
        // project specification group
        Group projectGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
//        projectGroup.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectContentsGroupLabel);
//        projectGroup.setText("Location of complex entities");
        projectGroup.setText("Complex entities");

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsButton.setText("use default location (directory \"Complex entities\" inside the project)");
        useDefaultsButton.setSelection(useDefaultsForComplexEntities);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedComplexEntityLocationGroup(projectGroup, !useDefaultsForComplexEntities);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForComplexEntities = useDefaultsButton.getSelection();
                browseButtonForComplexEntities.setEnabled(!useDefaultsForComplexEntities);
                locationPathFieldForComplexEntities.setEnabled(!useDefaultsForComplexEntities);
                locationLabelForComplexEntities.setEnabled(!useDefaultsForComplexEntities);
                if (useDefaultsForComplexEntities) {
                    customComplexEntityLocationFieldValue = locationPathFieldForComplexEntities.getText();
                    setLocationForSelection();
                } else {
                		customComplexEntityLocationFieldValue = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString());
                    locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
                }
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }

    private void createUserSpecifiedComplexEntityLocationGroup(
            Composite projectGroup, boolean enabled) {

        Font font = projectGroup.getFont();

        // location label
        locationLabelForComplexEntities = new Label(projectGroup, SWT.NONE);
//        locationLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_locationLabel);
        locationLabelForComplexEntities.setText("Directory:");
        locationLabelForComplexEntities.setEnabled(enabled);
        locationLabelForComplexEntities.setFont(font);

        // project location entry field
        locationPathFieldForComplexEntities = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathFieldForComplexEntities.setLayoutData(data);
        locationPathFieldForComplexEntities.setEnabled(enabled);
        locationPathFieldForComplexEntities.setFont(font);

        // browse button
        browseButtonForComplexEntities = new Button(projectGroup, SWT.PUSH);
//        browseButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_browseLabel);
        browseButtonForComplexEntities.setText("Browse...");
        browseButtonForComplexEntities.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleComplexEntityLocationBrowseButtonPressed();
            }
        });

        browseButtonForComplexEntities.setEnabled(enabled);
        browseButtonForComplexEntities.setFont(font);
        setButtonLayoutData(browseButtonForComplexEntities);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialComplexEntityLocationFieldValue == null)
            locationPathFieldForComplexEntities.setText(toSystem(Platform.getLocation().toOSString()));
        else
            locationPathFieldForComplexEntities.setText(toSystem(initialComplexEntityLocationFieldValue));
        locationPathFieldForComplexEntities.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForComplexEntities.addListener(SWT.Modify, complexEntityLocationModifyListener);
    }




    public IPath getExpressFileLocationPath() {
//      if (useDefaultsForProject)
//          return Platform.getLocation();

      return new Path(getExpressFileLocationFieldValue());
  }

    public IPath getShortNameLocationPath() {
//        if (useDefaultsForProject)
//            return Platform.getLocation();

        return new Path(getShortNameLocationFieldValue());
    }

    public IPath getComplexEntityLocationPath() {
//        if (useDefaultsForProject)
//            return Platform.getLocation();

        return new Path(getComplexEntityLocationFieldValue());
    }




    private String getExpressFileLocationFieldValue() {
        if (locationPathFieldForExpressFiles == null)
            return ""; //$NON-NLS-1$

        return toSystem(locationPathFieldForExpressFiles.getText().trim());
    }

    private String getShortNameLocationFieldValue() {
        if (locationPathFieldForShortNames == null)
            return ""; //$NON-NLS-1$

        return toSystem(locationPathFieldForShortNames.getText().trim());
    }

    private String getComplexEntityLocationFieldValue() {
        if (locationPathFieldForComplexEntities == null)
            return ""; //$NON-NLS-1$

        return toSystem(locationPathFieldForComplexEntities.getText().trim());
    }

   

    void handleExpressFileLocationBrowseButtonPressed_new(Shell shell) {

    	IContainer root = ResourcesPlugin.getWorkspace().getRoot();
    	ContainerSelectionDialog dialog = new ContainerSelectionDialog(
         shell, root, false, "kuku-la-la");		

		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				customExpressFileLocationFieldValue = ((Path)result[0]).toOSString();
        locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
			}
		}


//    	DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForExpressFiles.getShell());
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);

//        dialog.setMessage("express files message 1");

/*
        String dirName = getExpressFileLocationFieldValue();
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
//            if (path.exists())
//                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = null;
//        String selectedDirectory = dialog.open();
        
        if (selectedDirectory != null) {
            customExpressFileLocationFieldValue = selectedDirectory;
            locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
        }
*/

    }



    void handleExpressFileLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForExpressFiles.getShell(), SWT.SINGLE);
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("Select a directory for the express files of this project, preferably, inside the workspace");

        String dirName = getExpressFileLocationFieldValue();
        if (dirName.equals("")) {
	        dirName = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString());
        }
//System.out.println(">S< project directory: " + dirName);
      /*
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }
     */
     		// just set the path to workspace root, because the current project directory does not yet exist, and therefore
     		// the directory dialog will open under windows on "my computer"    
				dialog.setFilterPath(toSystem(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()));

			


/*
                	if ((customExpressFileLocationFieldValue == null) || (customExpressFileLocationFieldValue.equals(""))) {
                		customExpressFileLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
                	}
                  locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);

*/
        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customExpressFileLocationFieldValue = toSystem(selectedDirectory);
            locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
        }
    }







    void handleShortNameLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForShortNames
                .getShell());
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("Select a directory for the short name files of this project");

//        String dirName = getShortNameLocationFieldValue();
/*
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }
*/
				dialog.setFilterPath(toSystem(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()));

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customShortNameLocationFieldValue = toSystem(selectedDirectory);
            locationPathFieldForShortNames.setText(customShortNameLocationFieldValue);
        }
    }

    void handleComplexEntityLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForComplexEntities
                .getShell());
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("Select a directory for the complex entity files of this project");

//        String dirName = getComplexEntityLocationFieldValue();
/*
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }
*/	
				dialog.setFilterPath(toSystem(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()));

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customComplexEntityLocationFieldValue = toSystem(selectedDirectory);
            locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
        }
    }



    

    void setExpressFileLocationForSelection() {
        String express_file_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files");

        if (useDefaultsForExpressFiles)
            locationPathFieldForExpressFiles
                    .setText(express_file_default);

/*
        String expre_default = temp_default + File.separator + "ExpressCompilerRepo";
        if (!useDefaultsForTemp) express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo) {
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    		}
*/
    }

    void setShortNameLocationForSelection() {
        String short_name_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names");

        if (useDefaultsForShortNames)
            locationPathFieldForShortNames
                    .setText(short_name_default);

/*
        String express_compiler_repo_default = temp_default + File.separator + "ExpressCompilerRepo";
        if (!useDefaultsForTemp) express_compiler_repo_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForExpressCompilerRepo) {
            locationPathFieldForExpressCompilerRepo
                    .setText(express_compiler_repo_default);
    		}
*/

    }


    void setComplexEntityLocationForSelection() {
        String complex_entity_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities");
//        String complex_entity_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForComplexEntities)
            locationPathFieldForComplexEntities
                    .setText(complex_entity_default);
    }

    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    void setLocationForSelection() {
        String project_default = toSystem(NewExpressProjectWizard.getMainPage().getDefaultLocationForName(NewExpressProjectWizard.getMainPage().getProjectNameFieldValue()));
        String express_file_default = toSystem(project_default + File.separator + "Express files");
        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) express_file_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files");
        if (useDefaultsForExpressFiles) {
            locationPathFieldForExpressFiles
                    .setText(express_file_default);
        }
        String short_name_default = toSystem(project_default + File.separator + "Short names");
        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) short_name_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names");
//        if (!useDefaultsForExpressFiles) short_name_default = getExpressFileLocationPath() + File.separator + "Short names";
        if (useDefaultsForShortNames) {
            locationPathFieldForShortNames
                    .setText(short_name_default);
    		}
        String complex_entity_default = toSystem(project_default + File.separator + "Complex entities");
        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) complex_entity_default = toSystem(NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities");
//        if (!useDefaultsForExpressFiles) short_name_default = getExpressFileLocationPath() + File.separator + "Short names";
        if (useDefaultsForComplexEntities) {
            locationPathFieldForComplexEntities
                    .setText(complex_entity_default);
    		}
    }




    protected boolean validatePage() {
			// nothing can go wrong on this page
			return true;
		}

		public int getInputType() {
			return fInputType;
		}
		public int getIfUseExcludeList() {
			return fUseExcludeList;
		}

		

    public boolean useDefaultsForExpressFiles() {
        return useDefaultsForExpressFiles;
    }
	
    public boolean useDefaultsForShortNames() {
        return useDefaultsForShortNames;
    }

    public boolean useDefaultsForComplexEntities() {
        return useDefaultsForComplexEntities;
    }
	
	
	


	public boolean isDefaultExpressFileLocation() {
		return useDefaultsForExpressFiles;
	} 

	public boolean isDefaultShortNameLocation() {
		return useDefaultsForShortNames;
	} 

	public boolean isDefaultComplexEntityLocation() {
		return useDefaultsForComplexEntities;
	} 
	


	public String getCustomExpressFileLocation() {
		return toSystem(customExpressFileLocationFieldValue);
	}

	public String getCustomShortNameLocation() {
		return toSystem(customShortNameLocationFieldValue);
	}

	public String getCustomComplexEntityLocation() {
		return toSystem(customComplexEntityLocationFieldValue);
	}


		String toSystem(String str) {
			if (str == null) return null;
			return str.replace('/', File.separatorChar).replace('\\', File.separatorChar);			

		}

		
}





