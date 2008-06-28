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

package jsdai.express_compiler.preferences;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ExpressCompilerIOPreferences extends PreferencePage  implements IWorkbenchPreferencePage {

	static ExpressCompilerIOPreferences fExpressCompilerIOPreferences;
	IPreferenceStore fStore;
	
	int fInputType;
	boolean fUseListInput;
	boolean fRecursive;
	boolean fUseExcludeList;
	boolean fCreateJar;

	int fGenerateIncludeList;
	int fGenerateExcludeList;

	int currentInputType;
	boolean currentUseListInput;
	boolean currentRecursive;
	boolean currentUseExcludeList;
	boolean currentCreateJar;

	Button inputListButton;
	Button inputDirButton;
	Button inputRecursiveButton;

	Button jarYesButton;	
	Button jarNoButton;
	
	Button inputExcludeButton;

	
	
	public ExpressCompilerIOPreferences () {
		super();
		fExpressCompilerIOPreferences = this;
		// I want the store to be set before this pages is ever opened
		setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
	  setDescription("Input - output settings for Express Compiler");
//System.out.println("<IO> constructor, jar: " + getPreferenceStore().getBoolean(ExpressCompilerPreferences.CREATE_JAR));
		initDefaults();
	}
	
	public void init(IWorkbench workbench) {
		fStore = getPreferenceStore();

		fCreateJar  = fStore.getBoolean(ExpressCompilerPreferences.CREATE_JAR);
		fUseListInput = fStore.getBoolean(ExpressCompilerPreferences.USE_INCLUDE);
    fRecursive = fStore.getBoolean(ExpressCompilerPreferences.RECURSIVE_COMPILE);					
		fUseExcludeList = fStore.getBoolean(ExpressCompilerPreferences.USE_EXCLUDE);

		// don't want to make changes in other places, so continue using booleans, have to transform to radio button style
		// 0 - list, 1 - directory flat, 2 - directory recursive
		if (fUseListInput) {
			fInputType = 0;
		} else
		if (fRecursive) {
			fInputType = 2;
		} else {
			fInputType = 1;
		}
	
		currentCreateJar = fCreateJar;
		currentUseListInput = fUseListInput;
		currentRecursive = fRecursive;
		currentUseExcludeList = fUseExcludeList;
		currentInputType = fInputType;
	

	}

	void initDefaults() {
		IPreferenceStore store = getPreferenceStore();

		// move to IO page (not necessary, actually)
		store.setDefault(ExpressCompilerPreferences.RECURSIVE_COMPILE, false);
		store.setDefault(ExpressCompilerPreferences.CREATE_JAR, true);
		store.setDefault(ExpressCompilerPreferences.USE_EXCLUDE, true);
		store.setDefault(ExpressCompilerPreferences.USE_INCLUDE, false);

	}

	
	protected Control createContents(Composite parent) {


		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_COMPILER_IO_PREFERENCE_PAGE);
		
	
//System.out.println("<IO> createContents, jar: " + fCreateJar);

		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
	
		// do we need this?
		// initializeDialogUnits(parent);
	
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
	
		createInputGroup(composite);   // includes also use exclude list or not
		createJarGroup(composite);            
	//	createExlGenerationGroup(composite); // includes  both include and exclude lists

		// setErrorMessage(null);
		// setMessage(null);

		return composite;
	}


    private final void createInputGroup(Composite parent) {
        Font font = parent.getFont();
        Group inputGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        inputGroup.setLayout(layout);
        inputGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inputGroup.setFont(font);
        inputGroup.setText("Input of express files for the compiler");

        Composite inputInnerGroup = new Composite(inputGroup, SWT.NONE);
        GridLayout layout3 = new GridLayout();
        layout3.numColumns = 1;
        inputInnerGroup.setLayout(layout3);
//        inputInnerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        //    gd.widthHint = 150;
//        gd.horizontalAlignment = SWT.END;
//        gd.horizontalSpan = 1;
         gd.widthHint = 80;        
 
         gd.grabExcessHorizontalSpace = true;
         gd.minimumWidth = convertWidthInCharsToPixels(40);
         inputInnerGroup.setLayoutData(gd);
//        inputInnerGroup.setFont(font);
//        inputInnerGroup.setText("Input of express files for the compiler");


        inputListButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
        inputListButton.setText("the list file");
        if (fInputType == 0) 
            inputListButton.setSelection(true);
        else
          inputListButton.setSelection(false);
        inputListButton.setFont(font);


        inputDirButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
        inputDirButton.setText("the Express directory (non-recursively)");
        if (fInputType == 1) 
          inputDirButton.setSelection(true);
        else
          inputDirButton.setSelection(false);
        inputDirButton.setFont(font);


        inputRecursiveButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
        inputRecursiveButton.setText("the Express directory (recursively)");
        if (fInputType == 2) 
          inputRecursiveButton.setSelection(true);
        else
          inputRecursiveButton.setSelection(false);
        inputRecursiveButton.setFont(font);



//    buttonDataR = new GridData();
    GridData buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
    buttonDataR.widthHint = 70;
        buttonDataR.horizontalSpan = 1;
     inputListButton.setLayoutData(buttonDataR);


    buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//    buttonDataR = new GridData();
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
    inputRecursiveButton.setLayoutData(buttonDataR);

        //gd.grabExcessHorizontalSpace = true;
        //gd.minimumWidth = convertWidthInCharsToPixels(40);



    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              if (inputListButton.getSelection()) {
                fInputType = 0; // list
                fUseListInput = true;
                fRecursive = false;
                inputExcludeButton.setEnabled(false);
								inputExcludeButton.setSelection(false);
              } else
              if (inputDirButton.getSelection()) {
                fInputType = 1; //  directory - flat
                fRecursive = false;
                fUseListInput = false;
                inputExcludeButton.setEnabled(true);
								inputExcludeButton.setSelection(fUseExcludeList);
              } else
              if (inputRecursiveButton.getSelection()) {
                fInputType = 2; //  directory - recursive
                fRecursive = true;
                fUseListInput = false;
                inputExcludeButton.setEnabled(true);
								inputExcludeButton.setSelection(fUseExcludeList);
              } 
            }
        };

        inputListButton.addSelectionListener(listener);
        inputDirButton.addSelectionListener(listener);
        inputRecursiveButton.addSelectionListener(listener);

    createInputUseExcludeSubgroup(inputGroup);


    }


  private final void createInputUseExcludeSubgroup(Composite inputGroup) {

        Font font = inputGroup.getFont();


        Group inputExcludeGroup = new Group(inputGroup, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 1;
        inputExcludeGroup.setLayout(layout2);
//        GridData inputExcludeGroupData = new GridData(GridData.FILL_HORIZONTAL);
        GridData inputExcludeGroupData = new GridData();
//        GridData inputExcludeGroupData = new GridData();
//        inputExcludeGroupData.widthHint = 100;
        //inputExcludeGroupData.horizontalAlignment = SWT.END;
        //inputExcludeGroupData.horizontalSpan = 1;

        inputExcludeGroup.setLayoutData(inputExcludeGroupData);
        inputExcludeGroup.setFont(font);
        inputExcludeGroup.setText("Using exclusion list with directory input");
        

        inputExcludeButton = new Button(inputExcludeGroup, SWT.CHECK | SWT.LEFT);
//        inputExcludeYesButton.setText("use the exclusion list");
        inputExcludeButton.setText("use exclusion list");
        inputExcludeButton.setSelection(fUseExcludeList);
        inputExcludeButton.setFont(font);


       GridData buttonDataR2 = new GridData(GridData.FILL_HORIZONTAL);
        buttonDataR2.widthHint = 70;
        buttonDataR2.horizontalSpan = 1;
        inputExcludeButton.setLayoutData(buttonDataR2);



    if (fInputType == 0) {
      inputExcludeButton.setEnabled(false);
			inputExcludeButton.setSelection(false);
    }


    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

              fUseExcludeList = inputExcludeButton.getSelection();
            }
        };

        inputExcludeButton.addSelectionListener(listener);


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


      jarYesButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      jarYesButton.setText("yes, generate and compile java and create jar as well as exd");
//      if (fCreateJar == 1) 
      jarYesButton.setSelection(fCreateJar);
      
//      if (fCreateJar) 
//        jarYesButton.setSelection(true);
//      else
//        jarYesButton.setSelection(false);
      jarYesButton.setFont(font);

      jarNoButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      jarNoButton.setText("no, just check for express errors and, if success, create exd only");
//      if (fCreateJar == 2) 
      jarNoButton.setSelection(!fCreateJar);
//       	if (!fCreateJar) 
//        jarNoButton.setSelection(true);
//      else
//        jarNoButton.setSelection(false);
      jarNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      jarYesButton.setLayoutData(buttonData);

      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      jarNoButton.setLayoutData(buttonData);

//      System.out.println("<IO> createJarGroup, jar: " + fCreateJar);
      
  
      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (jarYesButton.getSelection()) {
            fCreateJar = true;
//            System.out.println("<IO> selected YES, jar: " + fCreateJar);
          } else
          if (jarNoButton.getSelection()) {
            fCreateJar = false;
//            System.out.println("<IO> selected NO, jar: " + fCreateJar);
          }
        }
      };
      
      jarYesButton.addSelectionListener(listener);
      jarNoButton.addSelectionListener(listener);

    }

/*    
    private final void createExlIncludeGenerationGroup(Composite listGroup) {
	    Font font = listGroup.getFont();
     	Group includeGroup = new Group(listGroup, SWT.NONE);
     	GridLayout layout2 = new GridLayout();
     	layout2.numColumns = 1;
     	includeGroup.setLayout(layout2);
     	includeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
     	includeGroup.setFont(font);
     	includeGroup.setText("Input list");


     	final Button includeYesButton = new Button(includeGroup, SWT.RADIO | SWT.LEFT);
     	includeYesButton.setText("create");
		if (fGenerateIncludeList == 0)
			includeYesButton.setSelection(true);
		else
			includeYesButton.setSelection(false);
     	includeYesButton.setFont(font);

     	final Button includeNoButton = new Button(includeGroup, SWT.RADIO | SWT.LEFT);
     	includeNoButton.setText("do not create");
		if (fGenerateIncludeList == 1)
			includeNoButton.setSelection(true);
		else
			includeNoButton.setSelection(false);
		includeNoButton.setFont(font);


    	GridData buttonDataR1 = new GridData();
    	buttonDataR1.horizontalSpan = 1;
    	includeYesButton.setLayoutData(buttonDataR1);

    	buttonDataR1 = new GridData();
    	buttonDataR1.horizontalSpan = 1;
    	includeNoButton.setLayoutData(buttonDataR1);

     	SelectionListener listener = new SelectionAdapter() {
     		public void widgetSelected(SelectionEvent e) {
     			if (includeYesButton.getSelection()) {
						fGenerateIncludeList = 0; // create input list
     			} else
     				if (includeNoButton.getSelection()) {
						fGenerateIncludeList = 1; // do NOT create input list
				}
  	   		}
      };

		includeYesButton.addSelectionListener(listener);
		includeNoButton.addSelectionListener(listener);
		}
*/
/*  
    
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


     	final Button excludeYesButton = new Button(excludeGroup, SWT.RADIO | SWT.LEFT);
     	excludeYesButton.setText("create");
		if (fGenerateExcludeList == 0) 
			excludeYesButton.setSelection(true);
		else
			excludeYesButton.setSelection(false);
     	excludeYesButton.setFont(font);

     	final Button excludeNoButton = new Button(excludeGroup, SWT.RADIO | SWT.LEFT);
     	excludeNoButton.setText("do not create");
		if (fGenerateExcludeList == 1) 
			excludeNoButton.setSelection(true);
     	else
			excludeNoButton.setSelection(false);
		excludeNoButton.setFont(font);

     	GridData buttonDataR2 = new GridData();
     	buttonDataR2.horizontalSpan = 1;
     	excludeYesButton.setLayoutData(buttonDataR2);

     	buttonDataR2 = new GridData();
     	buttonDataR2.horizontalSpan = 1;
    	excludeNoButton.setLayoutData(buttonDataR2);

     	SelectionListener listener = new SelectionAdapter() {
     		public void widgetSelected(SelectionEvent e) {
  	   			if (excludeYesButton.getSelection()) {
	  	 			fGenerateExcludeList = 0; // create exclude list
				} else
     			if (excludeNoButton.getSelection()) {
					fGenerateExcludeList = 1; // do NOT create exclude list
	     		}
  	   		}
      };

		excludeYesButton.addSelectionListener(listener);
		excludeNoButton.addSelectionListener(listener);
		}

*/    
    
/*    
    private final void createExlGenerationGroup(Composite parent) {

       Font font = parent.getFont();
       Group listGroup = new Group(parent, SWT.NONE);
       GridLayout layout = new GridLayout();
       layout.numColumns = 2;
       listGroup.setLayout(layout);
       listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
       listGroup.setFont(font);
       listGroup.setText("Creation of empty inclusion and exclusion list files");

       createExlIncludeGenerationGroup(listGroup);
       createExlExcludeGenerationGroup(listGroup);
        

    }
*/
    protected void performDefaults() {
        super.performDefaults();


			fStore.setToDefault(ExpressCompilerPreferences.RECURSIVE_COMPILE);
			fStore.setToDefault(ExpressCompilerPreferences.CREATE_JAR);
			fStore.setToDefault(ExpressCompilerPreferences.USE_EXCLUDE);
			fStore.setToDefault(ExpressCompilerPreferences.USE_INCLUDE);

			fCreateJar  = fStore.getBoolean(ExpressCompilerPreferences.CREATE_JAR);
			fUseListInput = fStore.getBoolean(ExpressCompilerPreferences.USE_INCLUDE);
    	fRecursive = fStore.getBoolean(ExpressCompilerPreferences.RECURSIVE_COMPILE);					
			fUseExcludeList = fStore.getBoolean(ExpressCompilerPreferences.USE_EXCLUDE);

			if (fUseListInput) {
				fInputType = 0;
			} else
			if (fRecursive) {
				fInputType = 2;
			} else {
				fInputType = 1;
			}

			switch (fInputType) {
				case 0: // list
					inputListButton.setSelection(true);
					inputDirButton.setSelection(false);
					inputRecursiveButton.setSelection(false);

					inputExcludeButton.setSelection(false);
					inputExcludeButton.setEnabled(false);
					break;
				case 1: // flat
					inputListButton.setSelection(false);
					inputDirButton.setSelection(true);
					inputRecursiveButton.setSelection(false);

					inputExcludeButton.setSelection(fUseExcludeList);
					inputExcludeButton.setEnabled(true);
					break;
				case 2: // recursive
					inputListButton.setSelection(false);
					inputDirButton.setSelection(false);
					inputRecursiveButton.setSelection(true);

					inputExcludeButton.setSelection(fUseExcludeList);
					inputExcludeButton.setEnabled(true);
					break;
				default: // should not occur
					inputListButton.setSelection(true);
					inputDirButton.setSelection(false);
					inputRecursiveButton.setSelection(false);

					inputExcludeButton.setSelection(false);
					inputExcludeButton.setEnabled(false);
					break;	
			}


			jarYesButton.setSelection(fCreateJar);	
			jarNoButton.setSelection(!fCreateJar);
	

        
    }


    public boolean performOk() {
    	super.performOk(); // I think it does nothing 

    	fStore.setValue(ExpressCompilerPreferences.CREATE_JAR, fCreateJar);
			fStore.setValue(ExpressCompilerPreferences.USE_INCLUDE, fUseListInput);
			fStore.setValue(ExpressCompilerPreferences.RECURSIVE_COMPILE, fRecursive);
			fStore.setValue(ExpressCompilerPreferences.USE_EXCLUDE, fUseExcludeList);
			
			return true;
    }



		public boolean performCancel() {
			super.performCancel(); // probably not needed

    	fStore.setValue(ExpressCompilerPreferences.CREATE_JAR, currentCreateJar);
			fStore.setValue(ExpressCompilerPreferences.USE_INCLUDE, currentUseListInput);
			fStore.setValue(ExpressCompilerPreferences.RECURSIVE_COMPILE, currentRecursive);
			fStore.setValue(ExpressCompilerPreferences.USE_EXCLUDE, currentUseExcludeList);
	    	
			return true;
	    	
		}

	protected void performApply() {
        super.performApply(); 

		currentCreateJar = fCreateJar;
		currentUseListInput = fUseListInput;
		currentRecursive = fRecursive;
		currentUseExcludeList = fUseExcludeList;

	
	}



	public static ExpressCompilerIOPreferences getExpressCompilerIOPreferences() {
		return fExpressCompilerIOPreferences;
	}
    



}

/*
 
 way to handle default values?
 
 just thinking:
 
 1. in the constructor, check with contains(), then, if not, set default value,
 or we could always set default values.
 I want it done before the page is opened, because values may have to be read without
 opening the page, so I want to handle this in the constructor, as I invoke it explicitly to be sure
 
 
 
 
 
 
 
 
 */



