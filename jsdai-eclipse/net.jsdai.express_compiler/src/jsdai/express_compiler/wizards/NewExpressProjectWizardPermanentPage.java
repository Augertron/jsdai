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

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;
import jsdai.express_compiler.preferences.ExpressProjectPreferences;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
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
public class NewExpressProjectWizardPermanentPage extends WizardPage {
//public class NewExpressProjectWizardPermanentPage extends WizardNewProjectCreationPage {

		static NewExpressProjectWizardPermanentPage fgNewExpressProjectWizardPermanentPage;


		int fCreateJar = 0;
		int fGenerateIncludeList = 0;
		int fGenerateExcludeList = 0;




    
    public static Shell fgShell;


    

    
    
    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;
	
	
	
	public NewExpressProjectWizardPermanentPage(String pageName) {
		super(pageName);
        setPageComplete(false);

			fCreateJar = 0;
			fGenerateIncludeList = 0;
			fGenerateExcludeList = 0;

//System.out.println("PERMANENT - CONSTRUCTOR");
		fgNewExpressProjectWizardPermanentPage = this;
	}

	public static NewExpressProjectWizardPermanentPage getInstance() {
		return fgNewExpressProjectWizardPermanentPage;
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

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.NEW_EXPRESS_PROJECT_WIZARD_PERMANENT_PAGE);

        Composite composite = new Composite(parent, SWT.NULL);
				fgShell = composite.getShell();
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);


        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

//        createProjectNameGroup(composite);
//        createProjectLocationGroup(composite);
					createJarGroup(composite);            

					createExlGenerationGroup(composite); // includes  both include and exclude lists

        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
//System.out.println("PERMANENT - Create Control");
    }

		// express files


    private final void createExlIncludeGenerationGroup(Composite listGroup) {
	    Font font = listGroup.getFont();
     	Group includeGroup = new Group(listGroup, SWT.NONE);
     	GridLayout layout2 = new GridLayout();
     	layout2.numColumns = 1;
     	includeGroup.setLayout(layout2);
     	includeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
     	includeGroup.setFont(font);
     	includeGroup.setText("Input list");

     	final Button includeDefaultButton = new Button(includeGroup, SWT.RADIO | SWT.LEFT);


			boolean global_create_include  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressProjectPreferences.CREATE_INCLUDE);
			String default_include_button_value = "default";
			if (global_create_include) {
        	default_include_button_value += " (create)";
      } else {
	       	default_include_button_value += " (do not create)";
      }
   
 

     	includeDefaultButton.setText(default_include_button_value);
     	includeDefaultButton.setSelection(true);
			fGenerateIncludeList = 0;
			includeDefaultButton.setFont(font);

     	final Button includeYesButton = new Button(includeGroup, SWT.RADIO | SWT.LEFT);
     	includeYesButton.setText("create");
     	includeYesButton.setSelection(false);
     	includeYesButton.setFont(font);

     	final Button includeNoButton = new Button(includeGroup, SWT.RADIO | SWT.LEFT);
     	includeNoButton.setText("do not create");
     	includeNoButton.setSelection(false);
     	includeNoButton.setFont(font);

    	GridData buttonDataR1 = new GridData();
    	buttonDataR1.horizontalSpan = 1;
    	includeDefaultButton.setLayoutData(buttonDataR1);
    	buttonDataR1 = new GridData();
    	buttonDataR1.horizontalSpan = 1;
    	includeYesButton.setLayoutData(buttonDataR1);
    	buttonDataR1 = new GridData();
    	buttonDataR1.horizontalSpan = 1;
    	includeNoButton.setLayoutData(buttonDataR1);

     	SelectionListener listener = new SelectionAdapter() {
	 			public void widgetSelected(SelectionEvent e) {
					if (includeDefaultButton.getSelection()) {
						fGenerateIncludeList = 0; // default
     			} else
     			if (includeYesButton.getSelection()) {
						fGenerateIncludeList = 1; // create input list
	      	} else
  	    	if (includeNoButton.getSelection()) {
						fGenerateIncludeList = 2; // do NOT create input list
					}
  	   	}
      };

     	includeDefaultButton.addSelectionListener(listener);
			includeYesButton.addSelectionListener(listener);
			includeNoButton.addSelectionListener(listener);
		}

    private final void createExlExcludeGenerationGroup(Composite listGroup) {
  	  Font font = listGroup.getFont();
     	Group excludeGroup = new Group(listGroup, SWT.NONE);
     	GridLayout layout3 = new GridLayout();
     	layout3.numColumns = 1;
     	excludeGroup.setLayout(layout3);
     	excludeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//        excludeGroup.setLayoutData(new GridData());
     	excludeGroup.setFont(font);
     	excludeGroup.setText("Exclusion list");

     	final Button excludeDefaultButton = new Button(excludeGroup, SWT.RADIO | SWT.LEFT);


			boolean global_create_exclude  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressProjectPreferences.CREATE_EXCLUDE);
			String default_exclude_button_value = "default";
			if (global_create_exclude) {
        	default_exclude_button_value += " (create)";
      } else {
	       	default_exclude_button_value += " (do not create)";
      }
   
  

     	excludeDefaultButton.setText(default_exclude_button_value);
     	excludeDefaultButton.setSelection(true);
			fGenerateExcludeList = 0;
			excludeDefaultButton.setFont(font);

     	final Button excludeYesButton = new Button(excludeGroup, SWT.RADIO | SWT.LEFT);
     	excludeYesButton.setText("create");
     	excludeYesButton.setSelection(false);
     	excludeYesButton.setFont(font);

     	final Button excludeNoButton = new Button(excludeGroup, SWT.RADIO | SWT.LEFT);
     	excludeNoButton.setText("do not create");
     	excludeNoButton.setSelection(false);
     	excludeNoButton.setFont(font);

     	GridData buttonDataR2 = new GridData();
     	buttonDataR2.horizontalSpan = 1;
     	excludeDefaultButton.setLayoutData(buttonDataR2);
     	buttonDataR2 = new GridData();
     	buttonDataR2.horizontalSpan = 1;
     	excludeYesButton.setLayoutData(buttonDataR2);
     	buttonDataR2 = new GridData();
     	buttonDataR2.horizontalSpan = 1;
    	excludeNoButton.setLayoutData(buttonDataR2);

     	SelectionListener listener = new SelectionAdapter() {
	 			public void widgetSelected(SelectionEvent e) {
     			if (excludeDefaultButton.getSelection()) {
  	 				fGenerateExcludeList = 0; // default
	     		} else
  	   		if (excludeYesButton.getSelection()) {
	  	 			fGenerateExcludeList = 1; // create exclude list
					} else
     			if (excludeNoButton.getSelection()) {
						fGenerateExcludeList = 2; // do NOT create exclude list
	     		}
  	   	}
      };

			excludeDefaultButton.addSelectionListener(listener);
			excludeYesButton.addSelectionListener(listener);
			excludeNoButton.addSelectionListener(listener);
		}

    private final void createExlGenerationGroup(Composite parent) {

       Font font = parent.getFont();
       Group listGroup = new Group(parent, SWT.NONE);
       GridLayout layout = new GridLayout();
       layout.numColumns = 2;
       listGroup.setLayout(layout);
       listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
       listGroup.setFont(font);
       listGroup.setText("Creation of empty inclusion and exclusion list files");


		Label note1 = new Label(listGroup, SWT.SHADOW_IN);
//		Label note2 = new Label(listGroup, SWT.SHADOW_IN | SWT.WRAP);
//		Label note3 = new Label(listGroup, SWT.SHADOW_IN | SWT.WRAP);
//		Label note4 = new Label(listGroup, SWT.SHADOW_IN | SWT.WRAP);
		Label note5 = new Label(listGroup, SWT.SHADOW_IN | SWT.WRAP);
		note1.setText("To be created by this new project wizard - one time only proposition");
//		note2.setText("These are initially empty files with some comments inside only");
//		note3.setText("The input list is needed if input from the list is chosen");
//		note4.setText("The exclusion list may be used if input from the directory is chosen");
	    note5.setText("HINT: you may choose to create both and decide later whether to use any of them");
	    note1.setForeground(new Color(listGroup.getDisplay(),0,80,80));			
	    note5.setForeground(new Color(listGroup.getDisplay(),0,80,80));			
		//note.setColor(blue);
		//note1.setForeground(new Color(parent.getDisplay(),0,0,128));			
        GridData labelgd = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.horizontalIndent = 10;
        // labelgd.verticalIndent = 10;
        note1.setLayoutData(labelgd);
//		note2.setForeground(new Color(parent.getDisplay(),0,0,100));			
        labelgd = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.horizontalIndent = 10;
//        note2.setLayoutData(labelgd);
        labelgd = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.horizontalIndent = 10;
//        note3.setLayoutData(labelgd);
        labelgd = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.horizontalIndent = 10;
//        note4.setLayoutData(labelgd);
        labelgd = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.horizontalIndent = 10;
        note5.setLayoutData(labelgd);
        
			createExlIncludeGenerationGroup(listGroup);
    	createExlExcludeGenerationGroup(listGroup);
        

    }



    private final void createJarGroup(Composite parent) {


			Font font = parent.getFont();
			Group listGroup = new Group(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			listGroup.setLayout(layout);
			listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			listGroup.setFont(font);
			listGroup.setText("Creating the library jar");

			final Button jarDefaultButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);

			boolean global_create_jar  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.CREATE_JAR);
			String default_jar_button_value = "default";
			if (global_create_jar) {
        	default_jar_button_value += " (yes)";
      } else {
	       	default_jar_button_value += " (no)";
      }
   
  
			jarDefaultButton.setText(default_jar_button_value);
			jarDefaultButton.setSelection(true);
			fCreateJar = 0;
			jarDefaultButton.setFont(font);

			final Button jarYesButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
			jarYesButton.setText("yes, generate and compile java and create jar as well as exd");
			jarYesButton.setSelection(false);
			jarYesButton.setFont(font);

			final Button jarNoButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
			jarNoButton.setText("no, just check for express errors and, if success, create exd only");
			jarNoButton.setSelection(false);
			jarNoButton.setFont(font);

			GridData buttonData = new GridData();
			buttonData.horizontalSpan = 1;
			jarDefaultButton.setLayoutData(buttonData);
			buttonData = new GridData();
			buttonData.horizontalSpan = 1;
			jarYesButton.setLayoutData(buttonData);
			buttonData = new GridData();
			buttonData.horizontalSpan = 1;
			jarNoButton.setLayoutData(buttonData);

  
			SelectionListener listener = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (jarDefaultButton.getSelection()) {
						fCreateJar = 0;
					} else
					if (jarYesButton.getSelection()) {
						fCreateJar = 1;
					} else
					if (jarNoButton.getSelection()) {
						fCreateJar = 2;
					}
				}
			};
			
			jarDefaultButton.addSelectionListener(listener);
			jarYesButton.addSelectionListener(listener);
			jarNoButton.addSelectionListener(listener);

		}






    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
  if (true) return true;
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


		public int getIfCreateJar() {
			return fCreateJar;
		}

		public int getIfCreateInputList() {
			return fGenerateIncludeList;
		}
		public int getIfCreateExcludeList() {
			return fGenerateExcludeList;
		}



}





