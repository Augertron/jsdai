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

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
 * Example useage:
 * <pre>
 * mainPage = new ExpressProjectWizardPage("basicExpressProjectPage");
 * mainPage.setTitle("Project");
 * mainPage.setDescription("Create a new project resource.");
 * </pre>
 * </p>
 */
//public class NewExpressProjectWizardPage extends WizardNewProjectCreationPage {
public class ExpressProjectCompilerPropertyPage extends PropertyPage {

  
  IProject fProject;

	int fSpecifyMemory;
	int fMemorySizeInitial;
	int fMemorySizeMax;

	String fMemorySizeInitialStrGlobal; // global value
	String fMemorySizeInitialStr;  // what is displayed/modified in fields
	String fMemorySizeInitialStrLast; // last individual value
	String fMemorySizeMaxStrGlobal; // global value
	String fMemorySizeMaxStr;      // what is displayed/modified in fields
	String fMemorySizeMaxStrLast; // last individual value


	int fUseStepmodSwitch;
	int fUseArmSwitch;
	int fUseMimSwitch;
	int fEnableExpressions;
	int fOriginalCase;
	int fSeparateProcess;
	boolean fEnableAdvanced;

	Button memoryDefaultButton;
	Button memoryNoButton;
	Button memoryYesButton;
	Button enableAdvancedButton;
	
	Label lmin;  
	Text memmin;
	Label lmax;  
	Text memmax;
  Label advanced_separator;
  Group fProcessGroup;
  Group fStepmodGroup;

	boolean fSeparateProcessGlobal;
	boolean fUseCustomMemorySizeGlobal;
	int fInitialMemorySizeGlobal;
	int fMaximumMemorySizeGlobal;



 
  
  public ExpressProjectCompilerPropertyPage() {
    super();
    
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

    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }

    
  if (fProject != null) {
    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);




    String s_enable_advanced = prefs.get("fEnableAdvanced", "default");
    if (s_enable_advanced != null) {
      if (s_enable_advanced.equalsIgnoreCase("yes")) {
        fEnableAdvanced = true;
      } else 
      if (s_enable_advanced.equalsIgnoreCase("no")) {
        fEnableAdvanced = false;
      } else {
	      fEnableAdvanced = false;
      }
    } else {
      fEnableAdvanced = false;
    }     

    String s_separate_process = prefs.get("fSeparateProcess", "default");
    if (s_separate_process != null) {
      if (s_separate_process.equalsIgnoreCase("default")) {
        fSeparateProcess = 0;
      } else 
      if (s_separate_process.equalsIgnoreCase("yes")) {
        fSeparateProcess = 1;
      } else 
      if (s_separate_process.equalsIgnoreCase("no")) {
        fSeparateProcess = 2;
      }
    } else {
      fSeparateProcess = 0;
    }     


    String s_enable_expressions = prefs.get("fEnableExpressions", "default");
    if (s_enable_expressions != null) {
      if (s_enable_expressions.equalsIgnoreCase("default")) {
        fEnableExpressions = 0;
      } else 
      if (s_enable_expressions.equalsIgnoreCase("yes")) {
        fEnableExpressions = 1;
      } else 
      if (s_enable_expressions.equalsIgnoreCase("no")) {
        fEnableExpressions = 2;
      }
    } else {
      fEnableExpressions = 0;
    }     

    String s_original_case = prefs.get("fOriginalCase", "default");
    if (s_original_case != null) {
      if (s_original_case.equalsIgnoreCase("default")) {
        fOriginalCase = 0;
      } else 
      if (s_original_case.equalsIgnoreCase("yes")) {
        fOriginalCase = 1;
      } else 
      if (s_original_case.equalsIgnoreCase("no")) {
        fOriginalCase = 2;
      }
    } else {
      fOriginalCase = 0;
    }     


    String s_switch_stepmod = prefs.get("fSwitchStepmod", "default");
    if (s_switch_stepmod != null) {
      if (s_switch_stepmod.equalsIgnoreCase("default")) {
        fUseStepmodSwitch = 0;
      } else 
      if (s_switch_stepmod.equalsIgnoreCase("yes")) {
        fUseStepmodSwitch = 1;
      } else 
      if (s_switch_stepmod.equalsIgnoreCase("no")) {
        fUseStepmodSwitch = 2;
      }
    } else {
      fUseStepmodSwitch = 0;
//      System.out.println("fSwitchStepmod in null");
    }     

    String s_switch_arm = prefs.get("fSwitchArm", "default");
    if (s_switch_arm != null) {
      if (s_switch_arm.equalsIgnoreCase("default")) {
        fUseArmSwitch = 0;
      } else 
      if (s_switch_arm.equalsIgnoreCase("yes")) {
        fUseArmSwitch = 1;
      } else 
      if (s_switch_arm.equalsIgnoreCase("no")) {
        fUseArmSwitch = 2;
      }
    } else {
      fUseArmSwitch = 0;
//      System.out.println("fSwitchArm in null");
    }     

    String s_switch_mim = prefs.get("fSwitchMim", "default");
    if (s_switch_mim != null) {
      if (s_switch_mim.equalsIgnoreCase("default")) {
        fUseMimSwitch = 0;
      } else 
      if (s_switch_mim.equalsIgnoreCase("yes")) {
        fUseMimSwitch = 1;
      } else 
      if (s_switch_mim.equalsIgnoreCase("no")) {
        fUseMimSwitch = 2;
      }   
    } else {
      fUseMimSwitch = 0;
//      System.out.println("fSwitchMim is null");
    }     

  
  	/*
  			specify custom memory size - let's do it this way:
  			if "default" is chosen: display global values in disabled state
  			if "yes" is chosen - dislay last custom value
  			if "no" is chosen - then what? Perhaps clear the field completely?
  			
  			Also, if separate process  "default" is chosen:
  			 if global value is "in separate process", then  "default" behaves as "no" - disables memory subgroup
  			 if global value is NOT "in separate process", then "default" behaves as "yes" - enables memory subgroup
  			
  	*/
  	
		fUseCustomMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_CUSTOM_MEMORY_SIZE);
		fInitialMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.INITIAL_MEMORY_SIZE);
		fMaximumMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.MAXIMUM_MEMORY_SIZE);
		fSeparateProcessGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SEPARATE_PROCESS);
  	
  	if (fInitialMemorySizeGlobal > 0) {
  		fMemorySizeInitialStrGlobal = "" + fInitialMemorySizeGlobal;
  	} else {
  		fMemorySizeInitialStrGlobal = "64";
  	}
  	if (fMaximumMemorySizeGlobal > 0) {
  		fMemorySizeMaxStrGlobal = "" + fMaximumMemorySizeGlobal;
  	} else {
  		fMemorySizeMaxStrGlobal = "256";
  	}
  	
  	
    String s_memory_specify = prefs.get("fMemorySpecify", null);

    // valid only if fSpecifyMemory == 1
    fMemorySizeInitialStr = prefs.get("fMemoryInitial", null);
    fMemorySizeMaxStr = prefs.get("fMemoryMax", null);
    Integer int_fMemorySizeInitial = null;
    try {
      int_fMemorySizeInitial = Integer.valueOf(fMemorySizeInitialStr);
//        fMemorySizeInitial =  Integer.valueOf(fMemorySizeInitialStr).intValue();
    } catch (NumberFormatException e) {
      fMemorySizeInitial = 64;
    }
    if (int_fMemorySizeInitial != null) 
      fMemorySizeInitial =  int_fMemorySizeInitial.intValue();
    else {  
      fMemorySizeInitial = 64;
//      System.out.println("fMemorySizeInitial is null");
    }
    
    Integer int_fMemorySizeMax = null;
    try {
      int_fMemorySizeMax = Integer.valueOf(fMemorySizeMaxStr);
//      fMemorySizeMax =  Integer.valueOf(fMemorySizeMaxStr).intValue();
    } catch (NumberFormatException e) {
      fMemorySizeMax = 256;
    }
     if (int_fMemorySizeMax != null) 
      fMemorySizeMax =  int_fMemorySizeMax.intValue();
    else {
      fMemorySizeMax = 256;
//      System.out.println("fMemorySizeMax is null");
    }
    if (s_memory_specify != null) {
      if (s_memory_specify.equalsIgnoreCase("default")) {
        fSpecifyMemory = 0;
      } else 
      if (s_memory_specify.equalsIgnoreCase("yes")) {
        fSpecifyMemory = 1;
      } else 
      if (s_memory_specify.equalsIgnoreCase("no")) {
        fSpecifyMemory = 2;
      }   
    } else {
      fSpecifyMemory = 0;
      fMemorySizeMax = 256;
      fMemorySizeInitial = 64;
      fMemorySizeInitialStr = "64";
      fMemorySizeMaxStr = "256";
//      System.out.println("fSpecifyMemery is null");
    }     

		fMemorySizeInitialStrLast = fMemorySizeInitialStr;
		fMemorySizeMaxStrLast = fMemorySizeMaxStr;


//  System.out.println("fSpecifyMemory - starting 1: " + fSpecifyMemory);    


  }

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

			PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_PROJECT_COMPILER_PROPERTY_PAGE);


// adding this here, better see where it is really needed

    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }

    
  if (fProject != null) {
    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);


    String s_enable_advanced = prefs.get("fEnableAdvanced", "default");
    if (s_enable_advanced != null) {
      if (s_enable_advanced.equalsIgnoreCase("yes")) {
        fEnableAdvanced = true;
      } else 
      if (s_enable_advanced.equalsIgnoreCase("no")) {
        fEnableAdvanced = false;
      } else {
	      fEnableAdvanced = false;
      }
    } else {
      fEnableAdvanced = false;
    }     

    String s_separate_process = prefs.get("fSeparateProcess", "default");
    if (s_separate_process != null) {
      if (s_separate_process.equalsIgnoreCase("default")) {
        fSeparateProcess = 0;
      } else 
      if (s_separate_process.equalsIgnoreCase("yes")) {
        fSeparateProcess = 1;
      } else 
      if (s_separate_process.equalsIgnoreCase("no")) {
        fSeparateProcess = 2;
      }
    } else {
      fSeparateProcess = 0;
    }     

    String s_enable_expressions = prefs.get("fEnableExpressions", "default");
    if (s_enable_expressions != null) {
      if (s_enable_expressions.equalsIgnoreCase("default")) {
        fEnableExpressions = 0;
      } else 
      if (s_enable_expressions.equalsIgnoreCase("yes")) {
        fEnableExpressions = 1;
      } else 
      if (s_enable_expressions.equalsIgnoreCase("no")) {
        fEnableExpressions = 2;
      }
    } else {
      fEnableExpressions = 0;
    }     

    String s_original_case = prefs.get("fOriginalCase", "default");
    if (s_original_case != null) {
      if (s_original_case.equalsIgnoreCase("default")) {
        fOriginalCase = 0;
      } else 
      if (s_original_case.equalsIgnoreCase("yes")) {
        fOriginalCase = 1;
      } else 
      if (s_original_case.equalsIgnoreCase("no")) {
        fOriginalCase = 2;
      }
    } else {
      fOriginalCase = 0;
    }     



    String s_switch_stepmod = prefs.get("fSwitchStepmod", "default");
    if (s_switch_stepmod != null) {
      if (s_switch_stepmod.equalsIgnoreCase("default")) {
        fUseStepmodSwitch = 0;
      } else 
      if (s_switch_stepmod.equalsIgnoreCase("yes")) {
        fUseStepmodSwitch = 1;
      } else 
      if (s_switch_stepmod.equalsIgnoreCase("no")) {
        fUseStepmodSwitch = 2;
      }
    } else {
      fUseStepmodSwitch = 0;
//      System.out.println("fSwitchStepmod in null");
    }     

    String s_switch_arm = prefs.get("fSwitchArm", "default");
    if (s_switch_arm != null) {
      if (s_switch_arm.equalsIgnoreCase("default")) {
        fUseArmSwitch = 0;
      } else 
      if (s_switch_arm.equalsIgnoreCase("yes")) {
        fUseArmSwitch = 1;
      } else 
      if (s_switch_arm.equalsIgnoreCase("no")) {
        fUseArmSwitch = 2;
      }
    } else {
      fUseArmSwitch = 0;
//      System.out.println("fSwitchArm in null");
    }     

    String s_switch_mim = prefs.get("fSwitchMim", "default");
    if (s_switch_mim != null) {
      if (s_switch_mim.equalsIgnoreCase("default")) {
        fUseMimSwitch = 0;
      } else 
      if (s_switch_mim.equalsIgnoreCase("yes")) {
        fUseMimSwitch = 1;
      } else 
      if (s_switch_mim.equalsIgnoreCase("no")) {
        fUseMimSwitch = 2;
      }   
    } else {
      fUseMimSwitch = 0;
//      System.out.println("fSwitchMim is null");
    }     


  	/*
  			specify custom memory size - let's do it this way:
  			if "default" is chosen: display global values in disabled state
  			if "yes" is chosen - dislay last custom value
  			if "no" is chosen - then what? Perhaps clear the field completely? - yes do that
  			
  			Also, if separate process  "default" is chosen:
  			 if global value is "in separate process", then  "default" behaves as "no" - disables memory subgroup
  			 if global value is NOT "in separate process", then "default" behaves as "yes" - enables memory subgroup
  			
  	*/
  	
		fUseCustomMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_CUSTOM_MEMORY_SIZE);
		fInitialMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.INITIAL_MEMORY_SIZE);
		fMaximumMemorySizeGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getInt(ExpressCompilerPreferences.MAXIMUM_MEMORY_SIZE);
		fSeparateProcessGlobal = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SEPARATE_PROCESS);

  	if (fInitialMemorySizeGlobal > 0) {
  		fMemorySizeInitialStrGlobal = "" + fInitialMemorySizeGlobal;
  	} else {
  		fMemorySizeInitialStrGlobal = "64";
  	}
  	if (fMaximumMemorySizeGlobal > 0) {
  		fMemorySizeMaxStrGlobal = "" + fMaximumMemorySizeGlobal;
  	} else {
  		fMemorySizeMaxStrGlobal = "256";
  	}

    String s_memory_specify = prefs.get("fMemorySpecify", null);

    // valid only if fSpecifyMemory == 1
    fMemorySizeInitialStr = prefs.get("fMemoryInitial", null);
    fMemorySizeMaxStr = prefs.get("fMemoryMax", null);
    Integer int_fMemorySizeInitial = null;
    try {
      int_fMemorySizeInitial = Integer.valueOf(fMemorySizeInitialStr);
//        fMemorySizeInitial =  Integer.valueOf(fMemorySizeInitialStr).intValue();
    } catch (NumberFormatException e) {
      fMemorySizeInitial = 64;
    }
    if (int_fMemorySizeInitial != null) 
      fMemorySizeInitial =  int_fMemorySizeInitial.intValue();
    else {  
      fMemorySizeInitial = 64;
//      System.out.println("fMemorySizeInitial is null");
    }
    
    Integer int_fMemorySizeMax = null;
    try {
      int_fMemorySizeMax = Integer.valueOf(fMemorySizeMaxStr);
//      fMemorySizeMax =  Integer.valueOf(fMemorySizeMaxStr).intValue();
    } catch (NumberFormatException e) {
      fMemorySizeMax = 256;
    }
     if (int_fMemorySizeMax != null) 
      fMemorySizeMax =  int_fMemorySizeMax.intValue();
    else {
      fMemorySizeMax = 256;
//      System.out.println("fMemorySizeMax is null");
    }
    if (s_memory_specify != null) {
      if (s_memory_specify.equalsIgnoreCase("default")) {
        fSpecifyMemory = 0;
      } else 
      if (s_memory_specify.equalsIgnoreCase("yes")) {
        fSpecifyMemory = 1;
      } else 
      if (s_memory_specify.equalsIgnoreCase("no")) {
        fSpecifyMemory = 2;
      }   
    } else {
      fSpecifyMemory = 0;
      fMemorySizeMax = 265;
      fMemorySizeInitial = 64;
      fMemorySizeInitialStr = "64";
      fMemorySizeMaxStr = "256";
//      System.out.println("fSpecifyMemery is null");
    }     

		fMemorySizeInitialStrLast = fMemorySizeInitialStr;
		fMemorySizeMaxStrLast = fMemorySizeMaxStr;


// System.out.println("fSpecifyMemory - starting 2: " + fSpecifyMemory);    


  }


        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
//        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	      //composite.numColumns = 2;

					createExpressionsGroup(composite);
					createIdCaseGroup(composite);
//          createStepmodGroup(composite); // includes the 3 switches

// advanced settings stuff --------------------------------------------------------


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
          	fProcessGroup.setVisible(fEnableAdvanced);
          	fStepmodGroup.setVisible(fEnableAdvanced);
						//			fMemoryGroup.redraw();
			     	// if (!fSeparateProcess) {
						//			     	fMemoryGroup.dispose();
			    	// }
						//          	createContents(fComposite);
          }
        };


        enableAdvancedButton.addSelectionListener(listener);




// advanced settings ends here ------------------------------------------------




          createProcessGroup(composite);
          createStepmodGroup(composite); // includes the 3 switches

       	advanced_separator.setVisible(fEnableAdvanced);
       	fProcessGroup.setVisible(fEnableAdvanced);
       	fStepmodGroup.setVisible(fEnableAdvanced);

        Label note = new Label(composite, SWT.NONE);
        note.setText("NOTE: \"default\" means the corresponding setting in the global Express preferences");
        //note.setColor(blue);
        note.setForeground(new Color(parent.getDisplay(),0,0,100));     
            GridData labelgd = new GridData();
                labelgd.horizontalIndent = 10;
                labelgd.verticalIndent = 10;
            note.setLayoutData(labelgd);


      setValid(isValid());        
//        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
//        setControl(composite);

        noDefaultAndApplyButton();
        return composite;
    }



    private final void createMemoryGroup(Composite parent) {


      Font font = parent.getFont();
      Group listGroup = new Group(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      listGroup.setLayout(layout);
      listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      listGroup.setFont(font);
      listGroup.setText("Memory settings for the Express compiler");

      memoryDefaultButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);

      String default_specify_memory_size_value = "default";
			if (fUseCustomMemorySizeGlobal) {
				default_specify_memory_size_value += " (custom size)";
			} else {
				default_specify_memory_size_value += " (default size)";
			}
      memoryDefaultButton.setText(default_specify_memory_size_value);
      if (fSpecifyMemory == 0)
        memoryDefaultButton.setSelection(true);
      else
        memoryDefaultButton.setSelection(false);
      memoryDefaultButton.setFont(font);

      memoryNoButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      memoryNoButton.setText("do not specify custom memory size");
      if (fSpecifyMemory == 2)
        memoryNoButton.setSelection(true);
      else
        memoryNoButton.setSelection(false);
      memoryNoButton.setFont(font);

      memoryYesButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      memoryYesButton.setText("specify custom memory size for this project");
      if (fSpecifyMemory == 1)
        memoryYesButton.setSelection(true);
      else
        memoryYesButton.setSelection(false);
      memoryYesButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 2;
      memoryDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 2;
      memoryYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 2;
      memoryNoButton.setLayoutData(buttonData);
      
      
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

			switch (fSpecifyMemory) {
				case 0:
				default:
		      memmin.setText(fMemorySizeInitialStrGlobal);
					break;
				case 1:
		      memmin.setText(fMemorySizeInitialStr);
					break;
				case 2:
		      memmin.setText("");
					break;		
			}




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

			switch (fSpecifyMemory) {
				case 0:
				default:
		      memmax.setText(fMemorySizeMaxStrGlobal);
					break;
				case 1:
		      memmax.setText(fMemorySizeMaxStr);
					break;
				case 2:
		      memmax.setText("");
					break;		
			}



      memData = new GridData();
//      memData.
      memData.horizontalSpan = 1;
      memData.widthHint = convertWidthInCharsToPixels(6);
      memmax.setLayoutData(memData);
      
      if (fSpecifyMemory == 1) {
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
					if (fSpecifyMemory == 1) {

          	fMemorySizeInitialStr = memmin.getText();
          	setValid(isValid());
					}
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
					if (fSpecifyMemory == 1) {
	          fMemorySizeMaxStr = memmax.getText();
//          System.out.println("Modified at " + e.time);
  	        setValid(isValid());
        }
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
          if (memoryDefaultButton.getSelection()) {
            fSpecifyMemory = 0;
            lmin.setEnabled(false);
            lmax.setEnabled(false);
			      memmin.setText(fMemorySizeInitialStrGlobal);
			      memmax.setText(fMemorySizeMaxStrGlobal);
            memmin.setEnabled(false);
            memmax.setEnabled(false);

            // display current global values
//            setValid(isValid());

//                  setPageComplete(validatePage());
          } else
          if (memoryYesButton.getSelection()) {
            lmin.setEnabled(true);
            lmax.setEnabled(true);
            memmin.setEnabled(true);
            memmax.setEnabled(true);
			      memmin.setText(fMemorySizeInitialStr);
			      memmax.setText(fMemorySizeMaxStr);
            fSpecifyMemory = 1;
            // display current individual last used values
            setValid(isValid());
//                  setPageComplete(validatePage());
          } else
          if (memoryNoButton.getSelection()) {
            lmin.setEnabled(false);
            lmax.setEnabled(false);
			      fMemorySizeInitialStrLast = fMemorySizeInitialStr;
			      fMemorySizeMaxStrLast = fMemorySizeMaxStr;
			      memmin.setText("");
			      memmax.setText("");
            memmin.setEnabled(false);
            memmax.setEnabled(false);
			      fMemorySizeInitialStr = fMemorySizeInitialStrLast;
			      fMemorySizeMaxStr = fMemorySizeMaxStrLast;
            fSpecifyMemory = 2;
            // display no values at all
//            setValid(isValid());
//                  setPageComplete(validatePage());
          }
        }
      };
      
      memoryDefaultButton.addSelectionListener(listener);
      memoryYesButton.addSelectionListener(listener);
      memoryNoButton.addSelectionListener(listener);

    }




    private final void createProcessGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group processGroup = new Group(listGroup, SWT.NONE);
			fProcessGroup = processGroup;
      GridLayout layout2 = new GridLayout();
      layout2.numColumns = 2;
      processGroup.setLayout(layout2);
      processGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      processGroup.setFont(font);
      processGroup.setText("Run the compiler in a separate process");


      Composite processInnerGroup = new Composite(processGroup, SWT.NONE);
      GridLayout layout3 = new GridLayout();
      layout3.numColumns = 1;
      processInnerGroup.setLayout(layout3);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.widthHint = 80;        
 
      gd.grabExcessHorizontalSpace = true;
     	gd.minimumWidth = convertWidthInCharsToPixels(40);
     	processInnerGroup.setLayoutData(gd);


      final Button processDefaultButton = new Button(processInnerGroup, SWT.RADIO | SWT.LEFT);
      
      String default_separate_process = "default";
			if (fSeparateProcessGlobal) {
				default_separate_process += " (yes)";
			} else {
				default_separate_process += " (no)";
			}      
      processDefaultButton.setText(default_separate_process);
      if (fSeparateProcess == 0)
        processDefaultButton.setSelection(true);
      else
        processDefaultButton.setSelection(false);
      processDefaultButton.setFont(font);

      final Button processYesButton = new Button(processInnerGroup, SWT.RADIO | SWT.LEFT);
      processYesButton.setText("yes");
      if (fSeparateProcess == 1)
        processYesButton.setSelection(true);
      else
        processYesButton.setSelection(false);
      processYesButton.setFont(font);

      final Button processNoButton = new Button(processInnerGroup, SWT.RADIO | SWT.LEFT);
      processNoButton.setText("no");
      if (fSeparateProcess == 2)
        processNoButton.setSelection(true);
      else
        processNoButton.setSelection(false);
      processNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      processDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      processYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      processNoButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (processDefaultButton.getSelection()) {
            fSeparateProcess = 0;
						// if default here, let's make memory stuff dependent on the global value of separate process setting
						if (fSeparateProcessGlobal) {
							memoryDefaultButton.setEnabled(true);
							memoryNoButton.setEnabled(true);
							memoryYesButton.setEnabled(true);

	      			if (fSpecifyMemory == 1) {
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

						} else {
							memoryDefaultButton.setEnabled(false);
							memoryNoButton.setEnabled(false);
							memoryYesButton.setEnabled(false);

        			lmin.setEnabled(false);
        			lmax.setEnabled(false);
        			memmin.setEnabled(false);
        			memmax.setEnabled(false);
						}
						


          } else
          if (processYesButton.getSelection()) {
            fSeparateProcess = 1;
						memoryDefaultButton.setEnabled(true);
						memoryNoButton.setEnabled(true);
						memoryYesButton.setEnabled(true);

      			if (fSpecifyMemory == 1) {
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

          } else
          if (processNoButton.getSelection()) {
            fSeparateProcess = 2;
						memoryDefaultButton.setEnabled(false);
						memoryNoButton.setEnabled(false);
						memoryYesButton.setEnabled(false);

	      	  lmin.setEnabled(false);
  	      	lmax.setEnabled(false);
	    	    memmin.setEnabled(false);
  	    	  memmax.setEnabled(false);
          }
        }
      };


      processDefaultButton.addSelectionListener(listener);
      processYesButton.addSelectionListener(listener);
      processNoButton.addSelectionListener(listener);
      createMemoryGroup(processGroup);
			switch (fSeparateProcess) {
				case 0:
				default:
						if (fSeparateProcessGlobal) {
							memoryDefaultButton.setEnabled(true);
							memoryNoButton.setEnabled(true);
							memoryYesButton.setEnabled(true);

	      			if (fSpecifyMemory == 1) {
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

						} else {
							memoryDefaultButton.setEnabled(false);
							memoryNoButton.setEnabled(false);
							memoryYesButton.setEnabled(false);

        			lmin.setEnabled(false);
        			lmax.setEnabled(false);
        			memmin.setEnabled(false);
        			memmax.setEnabled(false);
						}
					break;
				case 1:
						memoryDefaultButton.setEnabled(true);
						memoryNoButton.setEnabled(true);
						memoryYesButton.setEnabled(true);

      			if (fSpecifyMemory == 1) {
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
					break;
				case 2:
						memoryDefaultButton.setEnabled(false);
						memoryNoButton.setEnabled(false);
						memoryYesButton.setEnabled(false);

	      	  lmin.setEnabled(false);
  	      	lmax.setEnabled(false);
	    	    memmin.setEnabled(false);
  	    	  memmax.setEnabled(false);
					break;
			}	

    }



    private final void createExpressionsGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group expressionsGroup = new Group(listGroup, SWT.NONE);
      GridLayout layout2 = new GridLayout();
      layout2.numColumns = 1;
      expressionsGroup.setLayout(layout2);
      expressionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      expressionsGroup.setFont(font);
      expressionsGroup.setText("Enable Express expressions");

      final Button expressionsDefaultButton = new Button(expressionsGroup, SWT.RADIO | SWT.LEFT);
      String default_expressions_value = "default";
			boolean global_enable_expressions = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.ENABLE_EXPRESSIONS);
      if (global_enable_expressions) {
        	default_expressions_value += " (yes)";
        } else {
	       	default_expressions_value += " (no)";
        }
  
      expressionsDefaultButton.setText(default_expressions_value);
      if (fEnableExpressions == 0)
        expressionsDefaultButton.setSelection(true);
      else
        expressionsDefaultButton.setSelection(false);
      expressionsDefaultButton.setFont(font);

      final Button expressionsYesButton = new Button(expressionsGroup, SWT.RADIO | SWT.LEFT);
      expressionsYesButton.setText("yes");
      if (fEnableExpressions == 1)
        expressionsYesButton.setSelection(true);
      else
        expressionsYesButton.setSelection(false);
      expressionsYesButton.setFont(font);

      final Button expressionsNoButton = new Button(expressionsGroup, SWT.RADIO | SWT.LEFT);
      expressionsNoButton.setText("no");
      if (fEnableExpressions == 2)
        expressionsNoButton.setSelection(true);
      else
        expressionsNoButton.setSelection(false);
      expressionsNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      expressionsDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      expressionsYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      expressionsNoButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (expressionsDefaultButton.getSelection()) {
            fEnableExpressions = 0;
          } else
          if (expressionsYesButton.getSelection()) {
            fEnableExpressions = 1;
          } else
          if (expressionsNoButton.getSelection()) {
            fEnableExpressions = 2;
          }
        }
      };


      expressionsDefaultButton.addSelectionListener(listener);
      expressionsYesButton.addSelectionListener(listener);
      expressionsNoButton.addSelectionListener(listener);
    }


    
    private final void createIdCaseGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group idCaseGroup = new Group(listGroup, SWT.NONE);
      GridLayout layout2 = new GridLayout();
      layout2.numColumns = 1;
      idCaseGroup.setLayout(layout2);
      idCaseGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      idCaseGroup.setFont(font);
      idCaseGroup.setText("Treat (*REFERENCE FROM*) USE FROM as REFERENCE FROM");

      final Button idCaseDefaultButton = new Button(idCaseGroup, SWT.RADIO | SWT.LEFT);
      String default_case_value = "default";
			boolean global_original_case = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.ORIGINAL_CASE);
      if (global_original_case) {
        	default_case_value += " (yes)";
        } else {
	       	default_case_value += " (no)";
        }
  
      idCaseDefaultButton.setText(default_case_value);
      if (fOriginalCase == 0)
        idCaseDefaultButton.setSelection(true);
      else
        idCaseDefaultButton.setSelection(false);
      idCaseDefaultButton.setFont(font);

      final Button idCaseYesButton = new Button(idCaseGroup, SWT.RADIO | SWT.LEFT);
      idCaseYesButton.setText("yes");
      if (fOriginalCase == 1)
        idCaseYesButton.setSelection(true);
      else
        idCaseYesButton.setSelection(false);
      idCaseYesButton.setFont(font);

      final Button idCaseNoButton = new Button(idCaseGroup, SWT.RADIO | SWT.LEFT);
      idCaseNoButton.setText("no");
      if (fOriginalCase == 2)
        idCaseNoButton.setSelection(true);
      else
        idCaseNoButton.setSelection(false);
      idCaseNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      idCaseDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      idCaseYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      idCaseNoButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (idCaseDefaultButton.getSelection()) {
            fOriginalCase = 0;
          } else
          if (idCaseYesButton.getSelection()) {
            fOriginalCase = 1;
          } else
          if (idCaseNoButton.getSelection()) {
            fOriginalCase = 2;
          }
        }
      };


      idCaseDefaultButton.addSelectionListener(listener);
      idCaseYesButton.addSelectionListener(listener);
      idCaseNoButton.addSelectionListener(listener);
    }
    

    private final void createStepmodStepmodGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group stepmodGroup = new Group(listGroup, SWT.NONE);
      GridLayout layout2 = new GridLayout();
      layout2.numColumns = 1;
      stepmodGroup.setLayout(layout2);
      stepmodGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      stepmodGroup.setFont(font);
      stepmodGroup.setText("Compile stepmod schemas");

      final Button stepmodDefaultButton = new Button(stepmodGroup, SWT.RADIO | SWT.LEFT);

			String  default_stempod_stepmod_value = "default";
		  boolean global_stepmod_stepmod = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_STEPMOD);
			if (global_stepmod_stepmod) {
				default_stempod_stepmod_value += " (yes)";
			} else {
				default_stempod_stepmod_value += " (no)";
			}

      stepmodDefaultButton.setText(default_stempod_stepmod_value);
      if (fUseStepmodSwitch == 0)
        stepmodDefaultButton.setSelection(true);
      else
        stepmodDefaultButton.setSelection(false);
      stepmodDefaultButton.setFont(font);

      final Button stepmodYesButton = new Button(stepmodGroup, SWT.RADIO | SWT.LEFT);
      stepmodYesButton.setText("yes");
      if (fUseStepmodSwitch == 1)
        stepmodYesButton.setSelection(true);
      else
        stepmodYesButton.setSelection(false);
      stepmodYesButton.setFont(font);

      final Button stepmodNoButton = new Button(stepmodGroup, SWT.RADIO | SWT.LEFT);
      stepmodNoButton.setText("no");
      if (fUseStepmodSwitch == 2)
        stepmodNoButton.setSelection(true);
      else
        stepmodNoButton.setSelection(false);
      stepmodNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      stepmodDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      stepmodYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      stepmodNoButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (stepmodDefaultButton.getSelection()) {
            fUseStepmodSwitch = 0;
          } else
          if (stepmodYesButton.getSelection()) {
            fUseStepmodSwitch = 1;
          } else
          if (stepmodNoButton.getSelection()) {
            fUseStepmodSwitch = 2;
          }
        }
      };


      stepmodDefaultButton.addSelectionListener(listener);
      stepmodYesButton.addSelectionListener(listener);
      stepmodNoButton.addSelectionListener(listener);
    }

    private final void createStepmodArmGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group armGroup = new Group(listGroup, SWT.NONE);
      GridLayout layout3 = new GridLayout();
      layout3.numColumns = 1;
      armGroup.setLayout(layout3);
      armGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//  excludeGroup.setLayoutData(new GridData());
      armGroup.setFont(font);
      armGroup.setText("Compile arm files only");

      final Button armDefaultButton = new Button(armGroup, SWT.RADIO | SWT.LEFT);

			String  default_stempod_arm_value = "default";
		  boolean global_stepmod_arm = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_ARM);
			if (global_stepmod_arm) {
				default_stempod_arm_value += " (yes)";
			} else {
				default_stempod_arm_value += " (no)";
			}
      armDefaultButton.setText(default_stempod_arm_value);
      if (fUseArmSwitch == 0)
        armDefaultButton.setSelection(true);
      else
        armDefaultButton.setSelection(false);
      armDefaultButton.setFont(font);

      final Button armYesButton = new Button(armGroup, SWT.RADIO | SWT.LEFT);
      armYesButton.setText("yes");
      if (fUseArmSwitch == 1)
        armYesButton.setSelection(true);
      else
        armYesButton.setSelection(false);
      armYesButton.setFont(font);

      final Button armNoButton = new Button(armGroup, SWT.RADIO | SWT.LEFT);
      armNoButton.setText("no");
      if (fUseArmSwitch == 2)
        armNoButton.setSelection(true);
      else
        armNoButton.setSelection(false);
      armNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      armDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      armYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      armNoButton.setLayoutData(buttonData);


      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (armDefaultButton.getSelection()) {
            fUseArmSwitch = 0;
          } else
          if (armYesButton.getSelection()) {
            fUseArmSwitch = 1;
          } else
          if (armNoButton.getSelection()) {
            fUseArmSwitch = 2;
          }
        }
      };

      armDefaultButton.addSelectionListener(listener);
      armYesButton.addSelectionListener(listener);
      armNoButton.addSelectionListener(listener);
    }

    private final void createStepmodMimGroup(Composite listGroup) {
      Font font = listGroup.getFont();
      Group mimGroup = new Group(listGroup, SWT.NONE);
      GridLayout layout4 = new GridLayout();
      layout4.numColumns = 1;
      mimGroup.setLayout(layout4);
      mimGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//  excludeGroup.setLayoutData(new GridData());
      mimGroup.setFont(font);
      mimGroup.setText("Compile stepmod mim files");

      final Button mimDefaultButton = new Button(mimGroup, SWT.RADIO | SWT.LEFT);

			String  default_stempod_mim_value = "default";
		  boolean global_stepmod_mim = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.SWITCH_MIM);
			if (global_stepmod_mim) {
				default_stempod_mim_value += " (yes)";
			} else {
				default_stempod_mim_value += " (no)";
			}

      mimDefaultButton.setText(default_stempod_mim_value);
      if (fUseMimSwitch == 0)
        mimDefaultButton.setSelection(true);
      else
        mimDefaultButton.setSelection(false);
      mimDefaultButton.setFont(font);

      final Button mimYesButton = new Button(mimGroup, SWT.RADIO | SWT.LEFT);
      mimYesButton.setText("yes");
      if (fUseMimSwitch == 1)
        mimYesButton.setSelection(true);
      else
        mimYesButton.setSelection(false);
      mimYesButton.setFont(font);

      final Button mimNoButton = new Button(mimGroup, SWT.RADIO | SWT.LEFT);
      mimNoButton.setText("no");
      if (fUseMimSwitch == 2)
        mimNoButton.setSelection(true);
      else
        mimNoButton.setSelection(false);
      mimNoButton.setFont(font);

      GridData buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      mimDefaultButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      mimYesButton.setLayoutData(buttonData);
      buttonData = new GridData();
      buttonData.horizontalSpan = 1;
      mimNoButton.setLayoutData(buttonData);

      SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (mimDefaultButton.getSelection()) {
            fUseMimSwitch = 0;
          } else
          if (mimYesButton.getSelection()) {
            fUseMimSwitch = 1;
          } else
          if (mimNoButton.getSelection()) {
            fUseMimSwitch = 2;
          }
        }
      };


      mimDefaultButton.addSelectionListener(listener);
      mimYesButton.addSelectionListener(listener);
      mimNoButton.addSelectionListener(listener);
    }

    private final void createStepmodGroup(Composite parent) {

      /*
        
        compile stepmod schemas
        compile arm files only 
        compile stepmod mim files 
        
      */


      Font font = parent.getFont();
      Group listGroup = new Group(parent, SWT.NONE);
			fStepmodGroup = listGroup;
      GridLayout layout = new GridLayout();
      layout.numColumns = 3;
      listGroup.setLayout(layout);
      listGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      listGroup.setFont(font);
      listGroup.setText("Stepmod-specific settings");

      createStepmodStepmodGroup(listGroup);
      createStepmodArmGroup(listGroup);
      createStepmodMimGroup(listGroup);


    }

    public boolean isValid() {
        return validatePage();
    }


    protected boolean validatePage() {
      // nothing can go wrong on this page
            
      if (fSpecifyMemory == 1) {
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
      
      try {


    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
//    if(false) { //Memory setting are not used when external java processes are not started for express compilation etc.
    prefs.put("fMemoryInitial", fMemorySizeInitialStr);
    prefs.put("fMemoryMax", fMemorySizeMaxStr);
    switch (fSpecifyMemory) {
      case 0: // default
    	  prefs.put("fMemorySpecify", "default");
        break;
      case 1: // yes
    	  prefs.put("fMemorySpecify", "yes");
        break;
      case 2: // no
    	  prefs.put("fMemorySpecify", "no");
        break;
      default: // error
        break;      
    }
// System.out.println("fSpecifyMemory - button OK: " + fSpecifyMemory);    
//    } // if false - removed saving of memory settings

    
    if (fEnableAdvanced) {
    	  prefs.put("fEnableAdvanced", "yes");
    } else {
    	  prefs.put("fEnableAdvanced", "no");
    }

    switch (fSeparateProcess) {
      case 0: // default
    	  prefs.put("fSeparateProcess", "default");
        break;
      case 1: // yes
    	  prefs.put("fSeparateProcess", "yes");
        break;
      case 2: // no
    	  prefs.put("fSeparateProcess", "no");
        break;
      default: // error
        break;      
    }


    switch (fEnableExpressions) {
      case 0: // default
    	  prefs.put("fEnableExpressions", "default");
        break;
      case 1: // yes
    	  prefs.put("fEnableExpressions", "yes");
        break;
      case 2: // no
    	  prefs.put("fEnableExpressions", "no");
        break;
      default: // error
        break;      
    }

    switch (fOriginalCase) {
      case 0: // default
    	  prefs.put("fOriginalCase", "default");
        break;
      case 1: // yes
    	  prefs.put("fOriginalCase", "yes");
        break;
      case 2: // no
    	  prefs.put("fOriginalCase", "no");
        break;
      default: // error
        break;      
    }

    switch (fUseStepmodSwitch) {
      case 0: // default
    	  prefs.put("fSwitchStepmod", "default");
        break;
      case 1: // yes
    	  prefs.put("fSwitchStepmod", "yes");
        break;
      case 2: // no
    	  prefs.put("fSwitchStepmod", "no");
        break;
      default: // error
        break;      
    }

    switch (fUseArmSwitch) {
      case 0: // default
    	  prefs.put("fSwitchArm", "default");
        break;
      case 1: // yes
    	  prefs.put("fSwitchArm", "yes");
        break;
      case 2: // no
    	  prefs.put("fSwitchArm", "no");
        break;
      default: // error
        break;      
    }

    switch (fUseMimSwitch) {
      case 0: // default
    	  prefs.put("fSwitchMim", "default");
        break;
      case 1: // yes
    	  prefs.put("fSwitchMim", "yes");
        break;
      case 2: // no
    	  prefs.put("fSwitchMim", "no");
        break;
      default: // error
        break;      
    }

    prefs.flush();

    } catch (BackingStoreException e) {
		e.printStackTrace();
	}

      return true;

    
    }

    


}





