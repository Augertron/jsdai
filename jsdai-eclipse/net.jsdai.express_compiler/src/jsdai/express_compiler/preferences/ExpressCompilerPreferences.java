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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
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
public class ExpressCompilerPreferences extends PreferencePage   implements IWorkbenchPreferencePage {


	static ExpressCompilerPreferences fExpressCompilerPreferences;
	IPreferenceStore fStore;

	public static final String EXPRESS_WORKING_DIRECTORY = "express.working_directory";


	public static final String RECURSIVE_COMPILE = "express.recurseDirectories";
	public static final String CREATE_JAR = "express.createJar";

	public static final String USE_EXCLUDE = "express.useExclude";
	public static final String USE_INCLUDE = "express.useInclude";
	public static final String SWITCH_STEPMOD = "express.switchStepmod";
	public static final String SWITCH_ARM = "express.switchArm";
	public static final String SWITCH_MIM = "express.switchMim";

	public static final String  USE_CUSTOM_MEMORY_SIZE = "express.useCustomMemorySize";
	public static final String  INITIAL_MEMORY_SIZE = "express.initialMemorySize";
	public static final String  MAXIMUM_MEMORY_SIZE = "express.maximumMemorySize";

	public static final String ENABLE_EXPRESSIONS = "express.enableExpressions";
	public static final String SEPARATE_PROCESS = "express.separateProcess";
	public static final String ENABLE_ADVANCED = "express.enableAdvanced";

  
  IProject fProject;

    boolean fSpecifyMemory;
    int fMemorySizeInitial;
    int fMemorySizeMax;
    String fMemorySizeInitialStr;
    String fMemorySizeMaxStr;
    boolean fUseStepmodSwitch;
    boolean fUseArmSwitch;
    boolean fUseMimSwitch;
    boolean fEnableExpressions;
		boolean fSeparateProcess;
		boolean fEnableAdvanced;

    boolean currentEnableExpressions;
    boolean currentSeparateProcess;
    boolean currentEnableAdvanced;
    boolean currentSpecifyMemory;
    int currentMemorySizeInitial;
    int currentMemorySizeMax;
    String currentMemorySizeInitialStr;
    String currentMemorySizeMaxStr;
    boolean currentUseStepmodSwitch;
    boolean currentUseArmSwitch;
    boolean currentUseMimSwitch;

    Button enableExpressionsButton;
    Button separateProcessButton;
		Button enableAdvancedButton;

    
		Button stepmodDefaultButton;
		Button armDefaultButton;		
		Button mimDefaultButton;
		
		Button memoryUseButton;
		Text memmin;
		Text memmax;		
		Label lmin;  
		Label lmax;  
		Label advanced_separator;
		
		Composite fComposite;
		Group fMemoryGroup;		
		Group fProcessGroup;
		Group fAdvancedSeparatorGroup;
		
  public ExpressCompilerPreferences() {
    super();

		setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
    setDescription("Settings for Express Compiler");
//		initDefaults();
		fExpressCompilerPreferences = this;
		initDefaults();
    
//    setPageComplete(false);

/*
    fSpecifyMemory = 0;
    fMemorySizeInitial = 0;
    fMemorySizeMax = 0;
    fMemorySizeInitialStr = "";
    fMemorySizeMaxStr = "";
    fUseStepmodSwitch = 0;
    fUseArmSwitch = 0;
    fUseMimSwitch = 0;
*/

		

  }

	public void init(IWorkbench workbench) {
		fStore = getPreferenceStore();

    
    // try to read values for these:
    fSpecifyMemory = fStore.getBoolean(USE_CUSTOM_MEMORY_SIZE);
    fMemorySizeInitial = fStore.getInt(INITIAL_MEMORY_SIZE);
    fMemorySizeMax = fStore.getInt(MAXIMUM_MEMORY_SIZE);
    fMemorySizeInitialStr = "" + fMemorySizeInitial; 
    fMemorySizeMaxStr = "" + fMemorySizeMax;         

    fUseStepmodSwitch = fStore.getBoolean(SWITCH_STEPMOD);
    fUseArmSwitch = fStore.getBoolean(SWITCH_ARM); 
    fUseMimSwitch = fStore.getBoolean(SWITCH_MIM); 

    fEnableExpressions = fStore.getBoolean(ENABLE_EXPRESSIONS);
		fSeparateProcess = fStore.getBoolean(SEPARATE_PROCESS);
		fEnableAdvanced = fStore.getBoolean(ENABLE_ADVANCED);

		currentSpecifyMemory = fSpecifyMemory;
		currentMemorySizeInitial = fMemorySizeInitial;
		currentMemorySizeMax = fMemorySizeMax;
		currentMemorySizeInitialStr = fMemorySizeInitialStr;
		currentMemorySizeMaxStr = fMemorySizeMaxStr;
		
		currentUseStepmodSwitch = fUseStepmodSwitch;
		currentUseArmSwitch = fUseArmSwitch;
		currentUseMimSwitch = fUseMimSwitch;
		currentEnableExpressions = fEnableExpressions;
		currentSeparateProcess = fSeparateProcess;
		currentEnableAdvanced = fEnableAdvanced;

	}

	void initDefaults() {
	
		IPreferenceStore store = getPreferenceStore();

		// belong to this page
		store.setDefault(SWITCH_STEPMOD, false);
		store.setDefault(SWITCH_ARM, false);
		store.setDefault(SWITCH_MIM, false);
		store.setDefault(ENABLE_EXPRESSIONS, true);
		store.setDefault(SEPARATE_PROCESS, false);
		store.setDefault(ENABLE_ADVANCED, false);
		
		// next 3 were commented out
		store.setDefault(USE_CUSTOM_MEMORY_SIZE, false);
		store.setDefault(INITIAL_MEMORY_SIZE, 64);
		store.setDefault(MAXIMUM_MEMORY_SIZE, 256);

		// and where are setting to generate include/exclude lists or not?
		// perhaps, moved to project page

	
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

			fComposite = parent;

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_COMPILER_PREFERENCE_PAGE);


        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);

//        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
//                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

          createExpressionsGroup(composite);
          createStepmodGroup(composite); // includes the 3 switches
//					createEnableAdvancedGroup(composite);

// ------------------- create advanced  ------------------------------------

	      Font font = composite.getFont();
  
        enableAdvancedButton = new Button(composite, SWT.CHECK | SWT.LEFT);
        enableAdvancedButton.setText("Show advanced settings");
        enableAdvancedButton.setSelection(fEnableAdvanced);
        enableAdvancedButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 1;
        enableAdvancedButton.setLayoutData(buttonData);

        
       
        advanced_separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				GridData sData = new GridData(GridData.FILL_HORIZONTAL);
      	advanced_separator.setLayoutData(sData);
        
        
        SelectionListener listener = new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
          	fEnableAdvanced = enableAdvancedButton.getSelection();
//          	fAdvancedSeparatorGroup.setVisible(fEnableAdvanced);
          	advanced_separator.setVisible(fEnableAdvanced);
//          	fProcessGroup.setVisible(fEnableAdvanced);
          	fMemoryGroup.setVisible(fEnableAdvanced);
						//			fMemoryGroup.redraw();
			     	// if (!fSeparateProcess) {
						//			     	fMemoryGroup.dispose();
			    	// }
						//          	createContents(fComposite);
          }
        };


        enableAdvancedButton.addSelectionListener(listener);



// ------------------- create advanced --- end ---------------------------------

//					createAdvancedSeparatorGroup(composite);
//					createProcessGroup(composite);
//          if (fSeparateProcess) {
          	createMemoryGroup(composite);
//          }

        advanced_separator.setVisible(fEnableAdvanced);
//				fProcessGroup.setVisible(fEnableAdvanced);
				fMemoryGroup.setVisible(fEnableAdvanced);
/*
        Label note = new Label(composite, SWT.NONE);
        note.setText("NOTE: \"default\" means the corresponding setting in the global Express preferences");
        //note.setColor(blue);
        note.setForeground(new Color(parent.getDisplay(),0,0,100));     
            GridData labelgd = new GridData();
                labelgd.horizontalIndent = 10;
                labelgd.verticalIndent = 10;
            note.setLayoutData(labelgd);
*/

      setValid(isValid());        
//        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
//        setControl(composite);

//        noDefaultAndApplyButton();
        return composite;
    }


		private final void createAdvancedSeparatorGroup(Composite parent) {
			


        Font font = parent.getFont();

        
        Group listGroup = new Group(parent, SWT.NONE);
        fAdvancedSeparatorGroup = listGroup;
        //GridLayout layout = new GridLayout();
        //layout.numColumns = 1;
        //listGroup.setLayout(layout);
        //listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //listGroup.setFont(font);
        listGroup.setText("Advanced settings");

//        advanced_separator = new Label(listGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
	      // advanced_separator.setText("Advanced Settings");
//				GridData sData = new GridData(GridData.FILL_HORIZONTAL);
//      	advanced_separator.setLayoutData(sData);
			
		}



		// this version incorporates separate process group and memory group together
    private final void createMemoryGroup(Composite parent) {


      Font font = parent.getFont();
      Group listGroup = new Group(parent, SWT.NONE);
	    fMemoryGroup = listGroup;
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      listGroup.setLayout(layout);
      listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      listGroup.setFont(font);
//      listGroup.setText("Memory settings for the Express compiler");

//      listGroup.setText("Memory size when run in a separate process");
      listGroup.setText("Settings for invoking the express compiler");


      separateProcessButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
      separateProcessButton.setText("run the compiler in a separate process");
      separateProcessButton.setSelection(fSeparateProcess);
      separateProcessButton.setFont(font);

      GridData buttonDataP = new GridData();
      buttonDataP.horizontalSpan = 2;
      separateProcessButton.setLayoutData(buttonDataP);



      memoryUseButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
//      memoryUseButton.setText("use custom memory size when run in a separate process");
      memoryUseButton.setText("use custom memory size when running in a separate process");
      memoryUseButton.setSelection(fSpecifyMemory);
      memoryUseButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 2;
      memoryUseButton.setLayoutData(buttonData);
      
      
      lmin = new Label(listGroup, SWT.NONE);
      lmin.setText("initial memory size (MB)");
      GridData lData = new GridData();
      lData.horizontalIndent = convertWidthInCharsToPixels(4);
      lData.horizontalSpan = 1;
      lmin.setLayoutData(lData);
      
      
      // need input feelds
      
      memmin = new Text(listGroup, SWT.SINGLE | SWT.BORDER);
      memmin.setTextLimit(5);
//      memmin.setSize(4,1);
      //      memmin.setBounds(0,0,convertWidthInCharsToPixels(4),convertWidthInCharsToPixels(1));
      memmin.setText(fMemorySizeInitialStr);

      GridData memData = new GridData();
//      memData.
      memData.horizontalSpan = 1;
      memData.widthHint = convertWidthInCharsToPixels(6);
      memmin.setLayoutData(memData);

      lmax = new Label(listGroup, SWT.NONE);
      lmax.setText("maximum memory size (MB)");
      lData = new GridData();
      lData.horizontalSpan = 1;
      lData.horizontalIndent = convertWidthInCharsToPixels(4);
      lmax.setLayoutData(lData);
      
      
      memmax = new Text(listGroup, SWT.SINGLE | SWT.BORDER);
      memmax.setTextLimit(5);
//      memmin.setSize(4,1);
      //      memmin.setBounds(0,0,convertWidthInCharsToPixels(4),convertWidthInCharsToPixels(1));
      memmax.setText(fMemorySizeMaxStr);

      memData = new GridData();
//      memData.
      memData.horizontalSpan = 1;
      memData.widthHint = convertWidthInCharsToPixels(6);
      memmax.setLayoutData(memData);
      
      
//      if (fSeparateProcess) {
      if (fSpecifyMemory) {
        lmin.setEnabled(true);
        lmax.setEnabled(true);
        memmin.setEnabled(true);
        memmax.setEnabled(true);
      } else {
        lmin.setEnabled(false);
        lmax.setEnabled(false);
        memmin.setEnabled(false);
        memmax.setEnabled(false);
      }
    



      
      memmin.addModifyListener(new ModifyListener() {
        public void modifyText(ModifyEvent e) {
//          System.out.println("Modified: " + memmin.getText());
          fMemorySizeInitialStr = memmin.getText();
          setValid(isValid());
          //                setPageComplete(validatePage());
        } 
      });
      memmin.addVerifyListener(new VerifyListener() {
        public void verifyText(VerifyEvent e) {
          char [] input_chars = e.text.toCharArray();
          for (int i = 0; i < input_chars.length; i++) {
            if (!Character.isDigit(input_chars[i])) {
              e.doit = false;
              break;
            }
//            memmin.
/*
            if ((i == 0) && (input_chars[0] == '0')) {
              e.doit = false;
              break;
            }
*/
          }
/*          
          if (e.text.equals("*")) {
            System.out.println("Cannot type *");
            e.doit = false;
          }
*/
        }
      });     
      
      memmax.addModifyListener(new ModifyListener() {
        public void modifyText(ModifyEvent e) {
          fMemorySizeMaxStr = memmax.getText();
//          System.out.println("Modified at " + e.time);
          setValid(isValid());
//                setPageComplete(validatePage());
          
        } 
      });
      memmax.addVerifyListener(new VerifyListener() {
        public void verifyText(VerifyEvent e) {
          char [] input_chars = e.text.toCharArray();
          for (int i = 0; i < input_chars.length; i++) {
            if (!Character.isDigit(input_chars[i])) {
              e.doit = false;
              break;
            }
/*
            if ((i == 0) && (input_chars[0] == '0')) {
              e.doit = false;
              break;
            }
*/
          }
/*          
          if (e.text.equals("*")) {
            System.out.println("Cannot type *");
            e.doit = false;
          }
*/
        }
      });     
      
      
      
      
  
      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          fSpecifyMemory = memoryUseButton.getSelection();
          if (fSpecifyMemory) {
            lmin.setEnabled(true);
            lmax.setEnabled(true);
            memmin.setEnabled(true);
            memmax.setEnabled(true);
            setValid(isValid());
					} else {
            lmin.setEnabled(false);
            lmax.setEnabled(false);
            memmin.setEnabled(false);
            memmax.setEnabled(false);
            setValid(isValid());
					}
//                  setPageComplete(validatePage());
        }
      };

        SelectionListener listener_p = new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
          	fSeparateProcess = separateProcessButton.getSelection();
          }
        };

      
      memoryUseButton.addSelectionListener(listener);
      separateProcessButton.addSelectionListener(listener_p);

    }





    private final void createExpressionsGroup(Composite parent) {
        Font font = parent.getFont();
        Group listGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        listGroup.setLayout(layout);
        listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        listGroup.setFont(font);
        listGroup.setText("Settings for handling expressions");

        enableExpressionsButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
        enableExpressionsButton.setText("enable expressions");
        enableExpressionsButton.setSelection(fEnableExpressions);
        enableExpressionsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 1;
        enableExpressionsButton.setLayoutData(buttonData);

        SelectionListener listener = new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
          	fEnableExpressions = enableExpressionsButton.getSelection();
          }
        };


        enableExpressionsButton.addSelectionListener(listener);

        
    }

    private final void createEnableAdvancedGroup(Composite parent) {
        Font font = parent.getFont();
        Group listGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        listGroup.setLayout(layout);
        listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        listGroup.setFont(font);
        listGroup.setText("Enable advanced settings");

        enableAdvancedButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
        enableAdvancedButton.setText("Enable advanced settings");
        enableAdvancedButton.setSelection(fEnableAdvanced);
        enableAdvancedButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 1;
        enableAdvancedButton.setLayoutData(buttonData);

        SelectionListener listener = new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
          	fEnableAdvanced = enableAdvancedButton.getSelection();
//						fMemoryGroup.setVisible(fEnableAdvanced);
						//			fMemoryGroup.redraw();
			     	// if (!fSeparateProcess) {
						//			     	fMemoryGroup.dispose();
			    	// }
						//          	createContents(fComposite);
          }
        };


        enableAdvancedButton.addSelectionListener(listener);

        
    }




    private final void createProcessGroup(Composite parent) {
        Font font = parent.getFont();
        Group listGroup = new Group(parent, SWT.NONE);
  	  	fProcessGroup = listGroup;
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        listGroup.setLayout(layout);
        listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        listGroup.setFont(font);
        listGroup.setText("Settings for invoking the express compiler");

        separateProcessButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
        separateProcessButton.setText("run the compiler in a separate process");
        separateProcessButton.setSelection(fSeparateProcess);
        separateProcessButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 1;
        separateProcessButton.setLayoutData(buttonData);

        SelectionListener listener = new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
          	fSeparateProcess = separateProcessButton.getSelection();
          }
        };


        separateProcessButton.addSelectionListener(listener);

        
    }


    private final void createStepmodGroup(Composite parent) {

      /*
        
        compile stepmod schemas
        compile arm files only 
        compile stepmod mim files 
        
      */


      Font font = parent.getFont();
      Group listGroup = new Group(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      listGroup.setLayout(layout);
      listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      listGroup.setFont(font);
      listGroup.setText("Stepmod-specific settings");

//      createStepmodStepmodGroup(listGroup);
//      createStepmodArmGroup(listGroup);
//      createStepmodMimGroup(listGroup);


      stepmodDefaultButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
      stepmodDefaultButton.setText("compile stepmod schemas");
      stepmodDefaultButton.setSelection(fUseStepmodSwitch);
      stepmodDefaultButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      stepmodDefaultButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
        	fUseStepmodSwitch = stepmodDefaultButton.getSelection();
        }
      };


      stepmodDefaultButton.addSelectionListener(listener);



      armDefaultButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
      armDefaultButton.setText("compile arm files only");
      armDefaultButton.setSelection(fUseArmSwitch);
      armDefaultButton.setFont(font);


      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      armDefaultButton.setLayoutData(buttonData);


      SelectionListener listener2 = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
        	fUseArmSwitch = armDefaultButton.getSelection();
        }
      };

      armDefaultButton.addSelectionListener(listener2);



      mimDefaultButton = new Button(listGroup, SWT.CHECK | SWT.LEFT);
      mimDefaultButton.setText("compile stepmod mim files");
      mimDefaultButton.setSelection(fUseMimSwitch);
      mimDefaultButton.setFont(font);

      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      mimDefaultButton.setLayoutData(buttonData);

      SelectionListener listener3 = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
        	fUseMimSwitch = mimDefaultButton.getSelection();
        }
      };


      mimDefaultButton.addSelectionListener(listener3);


    }

    public boolean isValid() {
        return validatePage();
    }


    protected boolean validatePage() {
      // nothing can go wrong on this page
            
      if (fSpecifyMemory) {
        try {
          fMemorySizeInitial =  Integer.valueOf(fMemorySizeInitialStr).intValue();
        } catch (NumberFormatException e) {
          return false;
        }
          if (fMemorySizeInitial <= 0) {
            return false;
          }
          try {
            fMemorySizeMax =  Integer.valueOf(fMemorySizeMaxStr).intValue();
          } catch (NumberFormatException e) {
            return false;
          }
          if (fMemorySizeMax <= 0) {
            return false;
          }
          if (fMemorySizeMax < fMemorySizeInitial) {
            return false;
          }
      }
      
      return true;
  }

    
    /*
    public int getInputType() {
      return fInputType;
    }
    public int getIfUseExcludeList() {
      return fUseExcludeList;
    }
    public int getIfGenerateInputList() {
      return fGenerateIncludeList;
    }
    public int getIfGenerateExcludeList() {
      return fGenerateExcludeList;
    }
*/


    public boolean performOk() {
      

		// next 5 were commented out
    // fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fMemoryInitial"),  fMemorySizeInitialStr);
    // fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fMemoryMax"),  fMemorySizeMaxStr);
    fStore.setValue(USE_CUSTOM_MEMORY_SIZE,fSpecifyMemory);
    fStore.setValue(INITIAL_MEMORY_SIZE,fMemorySizeInitial);
    fStore.setValue(MAXIMUM_MEMORY_SIZE, fMemorySizeMax);
    
    fStore.setValue(SWITCH_STEPMOD,fUseStepmodSwitch); 
    fStore.setValue(SWITCH_ARM,fUseArmSwitch);
    fStore.setValue(SWITCH_MIM,fUseMimSwitch);

    fStore.setValue(ENABLE_EXPRESSIONS, fEnableExpressions);
    fStore.setValue(SEPARATE_PROCESS, fSeparateProcess);
    fStore.setValue(ENABLE_ADVANCED, fEnableAdvanced);

     return true;
   
    }


    protected void performDefaults() {
    	super.performDefaults();

		// next 3 were commented out
    	fStore.setToDefault(USE_CUSTOM_MEMORY_SIZE);
    	fStore.setToDefault(INITIAL_MEMORY_SIZE);
    	fStore.setToDefault(MAXIMUM_MEMORY_SIZE);
    
    	fStore.setToDefault(SWITCH_STEPMOD); 
    	fStore.setToDefault(SWITCH_ARM);
    	fStore.setToDefault(SWITCH_MIM);

    	fStore.setToDefault(ENABLE_EXPRESSIONS);
    	fStore.setToDefault(SEPARATE_PROCESS);
  //  	fStore.setToDefault(ENABLE_ADVANCED);
    	
    	fSpecifyMemory = fStore.getBoolean(USE_CUSTOM_MEMORY_SIZE);
    	fMemorySizeInitial = fStore.getInt(INITIAL_MEMORY_SIZE);
    	fMemorySizeMax = fStore.getInt(MAXIMUM_MEMORY_SIZE);
    	fMemorySizeInitialStr = "" + fMemorySizeInitial; 
    	fMemorySizeMaxStr = "" + fMemorySizeMax;         

    	fUseStepmodSwitch = fStore.getBoolean(SWITCH_STEPMOD);
    	fUseArmSwitch = fStore.getBoolean(SWITCH_ARM); 
    	fUseMimSwitch = fStore.getBoolean(SWITCH_MIM); 

    	fEnableExpressions = fStore.getBoolean(ENABLE_EXPRESSIONS); 
    	fSeparateProcess = fStore.getBoolean(SEPARATE_PROCESS); 
//    	fEnableAdvanced = fStore.getBoolean(ENABLE_ADVANCED); 

			stepmodDefaultButton.setSelection(fUseStepmodSwitch);
			armDefaultButton.setSelection(fUseArmSwitch);		
			mimDefaultButton.setSelection(fUseMimSwitch);

			enableExpressionsButton.setSelection(fEnableExpressions);
			separateProcessButton.setSelection(fSeparateProcess);
//			enableAdvancedButton.setSelection(fEnableAdvanced);
		
			memoryUseButton.setSelection(fSpecifyMemory);
      memmin.setText(fMemorySizeInitialStr);
      memmax.setText(fMemorySizeMaxStr);
			memmin.setEnabled(fSpecifyMemory);
			memmax.setEnabled(fSpecifyMemory);
      lmin.setEnabled(fSpecifyMemory);
      lmax.setEnabled(fSpecifyMemory);


    }


	    public boolean performCancel() {
	    	super.performCancel(); // probably not needed

		// next 3 were commented out
		    fStore.setValue(USE_CUSTOM_MEMORY_SIZE,currentSpecifyMemory);
		    fStore.setValue(INITIAL_MEMORY_SIZE,currentMemorySizeInitial);
    		fStore.setValue(MAXIMUM_MEMORY_SIZE, currentMemorySizeMax);
    
		    fStore.setValue(SWITCH_STEPMOD,currentUseStepmodSwitch); 
    		fStore.setValue(SWITCH_ARM,currentUseArmSwitch);
    		fStore.setValue(SWITCH_MIM,currentUseMimSwitch);

    		fStore.setValue(ENABLE_EXPRESSIONS,currentEnableExpressions);
    		fStore.setValue(SEPARATE_PROCESS,currentSeparateProcess);
    		fStore.setValue(ENABLE_ADVANCED,currentEnableAdvanced);



	    	
	    	return true;
	    	
	    }

	protected void performApply() {
        super.performApply(); 


		currentSpecifyMemory = fSpecifyMemory;
		currentMemorySizeInitial = fMemorySizeInitial;
		currentMemorySizeMax = fMemorySizeMax;
		currentMemorySizeInitialStr = fMemorySizeInitialStr;
		currentMemorySizeMaxStr = fMemorySizeMaxStr;
		
		currentUseStepmodSwitch = fUseStepmodSwitch;
		currentUseArmSwitch = fUseArmSwitch;
		currentUseMimSwitch = fUseMimSwitch;
	        
		currentEnableExpressions = fEnableExpressions;
		currentSeparateProcess = fSeparateProcess;
		currentEnableAdvanced = fEnableAdvanced;
	
	}

    
	public static ExpressCompilerPreferences getExpressCompilerPreferences() {
		return fExpressCompilerPreferences;
	}

}





