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
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
 * Example useage:
 * <pre>
 * mainPage = new ExpressProjectWizardPage("basicExpressProjectPage");
 * mainPage.setTitle("Project");
 * mainPage.setDescription("Create a new project resource.");
 * </pre>
 * </p>
 */


/*
 * 
 * 
 *              // default/list/flat/recurse -  affects two flags - fUseInclude, fRecursiveFlagGlobal
              s_input_type = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fInputType"));
              
              // default/yes/no - affects fUseExclude
              s_use_exclude = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fUseExclude"));

              // default/yes/no - affects fNoJava (and fCreateJar)
              s_create_jar = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fCreateJar"));


 */


//public class NewExpressProjectWizardPage extends WizardNewProjectCreationPage {
public class ExpressProjectIOPropertyPage extends PropertyPage {

  IProject fProject;
  
  
  int fGenerateIncludeList;
  int fGenerateExcludeList;
  int fInputType;
  int fUseExcludeList;
  int fRecurseDirectory;
  int fCreateJar;

  Button inputExcludeDefaultButton;
  Button inputExcludeYesButton;
  Button inputExcludeNoButton;
  Button useDefaultsButton;


	Button inputRecurseDefaultButton;
	Button inputRecurseYesButton;
	Button inputRecurseNoButton;
  


    boolean useDefaultsForExpressFiles = true;
    boolean useDefaultsForComplexEntities = true;
    boolean useDefaultsForShortNames = true;

    private String initialExpressFileLocationFieldValue;
    private String initialComplexEntityLocationFieldValue;
    private String initialShortNameLocationFieldValue;


    // the value the user has entered
    String customExpressFileLocationFieldValue;
    String customComplexEntityLocationFieldValue;
    String customShortNameLocationFieldValue;

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

  private static final int SIZING_TEXT_FIELD_WIDTH = 400;

    private Listener expressFileLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
//            setPageComplete(valid);
            if (valid);
//              setShortNameLocationForSelection();
        }
    };

    private Listener shortNameLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
//            setPageComplete(valid);
            if (valid);
  //            setComplexEntityLocationForSelection();
        }
    };

    private Listener complexEntityLocationModifyListener = new Listener() {
        public void handleEvent(Event e) {
//            setPageComplete(validatePage());
        }
    };
    

  
  public ExpressProjectIOPropertyPage() {
    super();


    customExpressFileLocationFieldValue = ""; //$NON-NLS-1$
    customShortNameLocationFieldValue = ""; //$NON-NLS-1$
    customComplexEntityLocationFieldValue = ""; //$NON-NLS-1$

		useDefaultsForExpressFiles = true;
    useDefaultsForShortNames = true;
    useDefaultsForComplexEntities = true;

    
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
   // default/list/flat/recurse -  affects two flags - fUseInclude, fRecursiveFlagGlobal
    String s_input_type = prefs.get("fInputType", "default");
    if (s_input_type != null) {
      if (s_input_type.equalsIgnoreCase("default")) {
        fInputType = 0;
      } else
      if (s_input_type.equalsIgnoreCase("list")) {
        fInputType = 1;
      } else
      if (s_input_type.equalsIgnoreCase("flat")) {
         fInputType = 2;
      } else
      if (s_input_type.equalsIgnoreCase("recurse")) {
         fInputType = 3;
      }
    } else {
        fInputType = 0;
    } 
    
    
    // default/yes/no - affects fUseExclude
    String s_use_exclude = prefs.get("fUseExclude", "default");
    if (s_use_exclude != null) { 
      if (s_use_exclude.equalsIgnoreCase("default")) {
          fUseExcludeList = 0;
      } else 
      if (s_use_exclude.equalsIgnoreCase("yes")) {
            fUseExcludeList = 1;
      } else 
      if (s_use_exclude.equalsIgnoreCase("no")) {
        fUseExcludeList = 2;
      }
    } else {
          fUseExcludeList = 0;
    }
            
      
  


		String s_customExpressFileLocationFieldValue = null;
		String s_is_default = null;

		String s_customShortNameLocationFieldValue = null;
  	String s_is_default_shortNameLocation = null;

		String s_customComplexEntityLocationFieldValue = null;
		String s_is_default_complexEntityLocation = null;

  
		s_customExpressFileLocationFieldValue = prefs.get("expressFileLocation", null);
		s_is_default = prefs.get("fDefaultExpressFileLocation", "true");

		s_customShortNameLocationFieldValue = prefs.get("shortNameLocation", null);
		s_is_default_shortNameLocation = prefs.get("fDefaultShortNameLocation", "true");

		s_customComplexEntityLocationFieldValue = prefs.get("complexEntityLocation", null);
		s_is_default_complexEntityLocation = prefs.get("fDefaultComplexEntityLocation", "true");

		initialExpressFileLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Express files";
		initialShortNameLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Short names";
		initialComplexEntityLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Complex entities";

		if (s_is_default != null) {
			if (s_is_default.equalsIgnoreCase("false")) {
			  useDefaultsForExpressFiles = false;
			} else {
				useDefaultsForExpressFiles = true;
			}
		} else {
	    useDefaultsForExpressFiles = true;
		}

		if (s_customExpressFileLocationFieldValue != null) {
			customExpressFileLocationFieldValue = s_customExpressFileLocationFieldValue;
		} else {
			customExpressFileLocationFieldValue = initialExpressFileLocationFieldValue;
		}

						if (s_is_default_shortNameLocation != null) {
							if (s_is_default_shortNameLocation.equalsIgnoreCase("false")) {
								useDefaultsForShortNames = false;
							} else {
								useDefaultsForShortNames = true;
							}
						} else {
							useDefaultsForShortNames = true;
						}

						if (s_customShortNameLocationFieldValue != null) {
							customShortNameLocationFieldValue = s_customShortNameLocationFieldValue;
						} else {
							customShortNameLocationFieldValue = initialShortNameLocationFieldValue;
						}

						if (s_is_default_complexEntityLocation != null) {
							if (s_is_default_complexEntityLocation.equalsIgnoreCase("false")) {
								useDefaultsForComplexEntities = false;
							} else {
								useDefaultsForComplexEntities = true;
							}
						} else {
							useDefaultsForComplexEntities = true;
						}

						if (s_customComplexEntityLocationFieldValue != null) {
							customComplexEntityLocationFieldValue = s_customComplexEntityLocationFieldValue;
						} else {
							customComplexEntityLocationFieldValue = initialComplexEntityLocationFieldValue;
						}




  
  }   
    
//    setPageComplete(false);
/*
    fGenerateIncludeList = 0;
    fGenerateExcludeList = 0;
    fInputType = 0;
    fUseExcludeList = 0;
  fCreateJar = 0;
*/
  }


  protected Control createContents(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_PROJECT_IO_PROPERTY_PAGE);
    
    // do we need it here? 
    
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
    // default/list/flat/recurse -  affects two flags - fUseInclude, fRecursiveFlagGlobal
    String s_input_type = prefs.get("fInputType", "default");
    if (s_input_type != null) {
      if (s_input_type.equalsIgnoreCase("default")) {
        fInputType = 0;
      } else
      if (s_input_type.equalsIgnoreCase("list")) {
        fInputType = 1;
      } else
      if (s_input_type.equalsIgnoreCase("flat")) {
         fInputType = 2;
      } else
      if (s_input_type.equalsIgnoreCase("recurse")) {
         fInputType = 3;
      }
    } else {
        fInputType = 0;
    } 
    
    
    // default/yes/no - affects fUseExclude
    String s_use_exclude = prefs.get("fUseExclude", "default");
    if (s_use_exclude != null) { 
      if (s_use_exclude.equalsIgnoreCase("default")) {
          fUseExcludeList = 0;
      } else 
      if (s_use_exclude.equalsIgnoreCase("yes")) {
            fUseExcludeList = 1;
      } else 
      if (s_use_exclude.equalsIgnoreCase("no")) {
        fUseExcludeList = 2;
      }
    } else {
          fUseExcludeList = 0;
    }
            
      
    // default/yes/no - affects fNoJava (and fCreateJar)
    String s_create_jar = prefs.get("fCreateJar", "default");
    if (s_create_jar != null) {
      if (s_create_jar.equalsIgnoreCase("default")) {
        fCreateJar = 0;
      } else 
      if (s_create_jar.equalsIgnoreCase("yes")) {
        fCreateJar = 1;
      } else 
      if (s_create_jar.equalsIgnoreCase("no")) {
        fCreateJar = 2;
      }
    } else {
        fCreateJar = 0;
    }
    
    
		String s_customExpressFileLocationFieldValue = null;
		String s_is_default = null;

		String s_customShortNameLocationFieldValue = null;
		String s_is_default_shortNameLocation = null;

		String s_customComplexEntityLocationFieldValue = null;
		String s_is_default_complexEntityLocation = null;


		s_customExpressFileLocationFieldValue = prefs.get("expressFileLocation", null);
		s_is_default = prefs.get("fDefaultExpressFileLocation", "true");


		s_customShortNameLocationFieldValue = prefs.get("shortNameLocation", null);
		s_is_default_shortNameLocation = prefs.get("fDefaultShortNameLocation", "true");

		s_customComplexEntityLocationFieldValue = prefs.get("complexEntityLocationDefault", null);
		s_is_default_complexEntityLocation = prefs.get("fDefaultComplexEntityLocation", "true");


		initialExpressFileLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Express files";
		initialShortNameLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Short names";
		initialComplexEntityLocationFieldValue = fProject.getLocation().toOSString() + File.separator + "Complex entities";

						if (s_is_default != null) {
							if (s_is_default.equalsIgnoreCase("false")) {
				        useDefaultsForExpressFiles = false;
							} else {
				        useDefaultsForExpressFiles = true;
							}
						} else {
				      useDefaultsForExpressFiles = true;
						}

						if (s_customExpressFileLocationFieldValue != null) {
							customExpressFileLocationFieldValue = s_customExpressFileLocationFieldValue;
						} else {
							customExpressFileLocationFieldValue = initialExpressFileLocationFieldValue;
						}

						if (s_is_default_shortNameLocation != null) {
							if (s_is_default_shortNameLocation.equalsIgnoreCase("false")) {
								useDefaultsForShortNames = false;
							} else {
								useDefaultsForShortNames = true;
							}
						} else {
							useDefaultsForShortNames = true;
						}

						if (s_customShortNameLocationFieldValue != null) {
							customShortNameLocationFieldValue = s_customShortNameLocationFieldValue;
						} else {
							customShortNameLocationFieldValue = initialShortNameLocationFieldValue;
						}

						if (s_is_default_complexEntityLocation != null) {
							if (s_is_default_complexEntityLocation.equalsIgnoreCase("false")) {
								useDefaultsForComplexEntities = false;
							} else {
								useDefaultsForComplexEntities = true;
							}
						} else {
							useDefaultsForComplexEntities = true;
						}

						if (s_customComplexEntityLocationFieldValue != null) {
							customComplexEntityLocationFieldValue = s_customComplexEntityLocationFieldValue;
						} else {
							customComplexEntityLocationFieldValue = initialComplexEntityLocationFieldValue;
						}






  }   
    
    // the end of this nonsense   
    
    
    
//  public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);


        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createInputGroup(composite);   // includes also use exclude list or not
        createShortNameLocationGroup(composite);
        createComplexEntityLocationGroup(composite);

// probably will be moved to a separate page
//          createJarGroup(composite);            


//          createExlGenerationGroup(composite); // includes  both include and exclude lists
//          createJarGroup(composite);            
//          createMemoryGroup(composite);
//          createStepmodGroup(composite); // includes the 3 switches


        Label note = new Label(composite, SWT.NONE);
        note.setText("NOTE: \"default\" radio button means the corresponding setting in the global Express preferences");
        //note.setColor(blue);
        note.setForeground(new Color(parent.getDisplay(),0,0,100));     
            GridData labelgd = new GridData();
                labelgd.horizontalIndent = 10;
                labelgd.verticalIndent = 10;
            note.setLayoutData(labelgd);


//        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        //setControl(composite);

        noDefaultAndApplyButton();

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
        inputGroup.setText("Express files");

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



        final Button inputDefaultButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputDefaultButton.setText("use the setting in the global express project preferences");

        String default_button_value = "default";
		boolean global_is_list_input = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressCompilerPreferences.USE_INCLUDE);
		fGlobal_is_list_input = global_is_list_input;
		if (global_is_list_input) {
        	default_button_value += " (list)";
        } else {
	       	default_button_value += " (directory)";
        }

        inputDefaultButton.setText(default_button_value);
        if (fInputType == 0) 
          inputDefaultButton.setSelection(true);
        else 
          inputDefaultButton.setSelection(false);
        inputDefaultButton.setFont(font);

        final Button inputListButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputListButton.setText("a list");
//        String inputListButtonText = "the list of express files (in " + fProject.getName() + ".exl file)"; 
        String inputListButtonText = "list (file " + fProject.getName() + ".exl)"; 
        inputListButton.setText(inputListButtonText);
        if (fInputType == 1) 
            inputListButton.setSelection(true);
        else
          inputListButton.setSelection(false);
        inputListButton.setFont(font);

        final Button inputDirButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputDirButton.setText("a directory");
        inputDirButton.setText("directory");
        if (fInputType == 2) 
          inputDirButton.setSelection(true);
        else
          inputDirButton.setSelection(false);
        inputDirButton.setFont(font);


        createExpressFileLocationGroup(inputInnerGroup);

// do a separate group, perhaps horizontally


//        final Button inputRecursiveButton = new Button(inputInnerGroup, SWT.RADIO | SWT.LEFT);
//        inputRecursiveButton.setText("the Express directory (recursively)");
//        if (fInputType == 3) 
//          inputRecursiveButton.setSelection(true);
//        else
//          inputRecursiveButton.setSelection(false);
//        inputRecursiveButton.setFont(font);




    GridData buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//    GridData buttonDataR = new GridData();
    buttonDataR.widthHint = 70;
//        GridData buttonDataR = new GridData();
        buttonDataR.horizontalSpan = 1;
     inputDefaultButton.setLayoutData(buttonDataR);

//    buttonDataR = new GridData();
    buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
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

//    buttonDataR = new GridData(GridData.FILL_HORIZONTAL);
//        buttonDataR = new GridData();
//    buttonDataR.widthHint = 70;
//    buttonDataR.grabExcessHorizontalSpace = true;
//    buttonDataR.minimumWidth = convertWidthInCharsToPixels(40);
//    buttonDataR.horizontalSpan = 1;
//    inputRecursiveButton.setLayoutData(buttonDataR);

        //gd.grabExcessHorizontalSpace = true;
        //gd.minimumWidth = convertWidthInCharsToPixels(40);



    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              if (inputDefaultButton.getSelection()) {
                fInputType = 0; // default
                if (fGlobal_is_list_input) {
	                inputExcludeDefaultButton.setEnabled(false);
  	              inputExcludeYesButton.setEnabled(false);
    	            inputExcludeNoButton.setEnabled(false);
      	        	inputRecurseDefaultButton.setEnabled(false);
        	      	inputRecurseYesButton.setEnabled(false);
          	    	inputRecurseNoButton.setEnabled(false);
	              	useDefaultsButton.setEnabled(false);
                } else {
	                inputExcludeDefaultButton.setEnabled(true);
  	              inputExcludeYesButton.setEnabled(true);
    	            inputExcludeNoButton.setEnabled(true);
      	        	inputRecurseDefaultButton.setEnabled(true);
        	      	inputRecurseYesButton.setEnabled(true);
          	    	inputRecurseNoButton.setEnabled(true);
	              	useDefaultsButton.setEnabled(true);
                }
              } else
              if (inputListButton.getSelection()) {
                fInputType = 1; // list
                inputExcludeDefaultButton.setEnabled(false);
                inputExcludeYesButton.setEnabled(false);
                inputExcludeNoButton.setEnabled(false);
              	inputRecurseDefaultButton.setEnabled(false);
              	inputRecurseYesButton.setEnabled(false);
              	inputRecurseNoButton.setEnabled(false);
              	useDefaultsButton.setEnabled(false);
              } else
              if (inputDirButton.getSelection()) {
                fInputType = 2; //  directory - flat
                inputExcludeDefaultButton.setEnabled(true);
                inputExcludeYesButton.setEnabled(true);
                inputExcludeNoButton.setEnabled(true);
              	inputRecurseDefaultButton.setEnabled(true);
              	inputRecurseYesButton.setEnabled(true);
              	inputRecurseNoButton.setEnabled(true);
              	useDefaultsButton.setEnabled(true);
//              } else
//              if (inputRecursiveButton.getSelection()) {
//                fInputType = 3; //  directory - recursive
//                inputExcludeDefaultButton.setEnabled(true);
//                inputExcludeYesButton.setEnabled(true);
//                inputExcludeNoButton.setEnabled(true);
              } 
            }
        };

        inputDefaultButton.addSelectionListener(listener);
        inputListButton.addSelectionListener(listener);
        inputDirButton.addSelectionListener(listener);
//        inputRecursiveButton.addSelectionListener(listener);

//    createInputUseExcludeSubgroup(inputGroup);
    createInputUseRecursionSubgroup(inputInnerGroup);
    createInputUseExcludeSubgroup(inputInnerGroup);


    }


  private final void createInputUseRecursionSubgroup(Composite inputGroup) {

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



  private final void createInputUseExcludeSubgroup(Composite inputGroup) {

        Font font = inputGroup.getFont();


        Group inputExcludeGroup = new Group(inputGroup, SWT.NONE);
        GridLayout layout2 = new GridLayout();
        layout2.numColumns = 3;
        inputExcludeGroup.setLayout(layout2);
        GridData inputExcludeGroupData = new GridData(GridData.FILL_HORIZONTAL);
//        GridData inputExcludeGroupData = new GridData();
//        GridData inputExcludeGroupData = new GridData();
//        inputExcludeGroupData.widthHint = 100;
        //inputExcludeGroupData.horizontalAlignment = SWT.END;
        //inputExcludeGroupData.horizontalSpan = 1;

        inputExcludeGroup.setLayoutData(inputExcludeGroupData);
        inputExcludeGroup.setFont(font);
        inputExcludeGroup.setText("use exclusion list (file " + fProject.getName() + "_excluded.exl) with directory input");

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
      if (fUseExcludeList == 0)
        inputExcludeDefaultButton.setSelection(true);
      else
        inputExcludeDefaultButton.setSelection(false);
//        fUseExcludeList = 0;
        inputExcludeDefaultButton.setFont(font);

        inputExcludeYesButton = new Button(inputExcludeGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeYesButton.setText("use the exclusion list");
        inputExcludeYesButton.setText("yes");
      if (fUseExcludeList == 1)
        inputExcludeYesButton.setSelection(true);
      else
        inputExcludeYesButton.setSelection(false);
        inputExcludeYesButton.setFont(font);

        inputExcludeNoButton = new Button(inputExcludeGroup, SWT.RADIO | SWT.LEFT);
//        inputExcludeNoButton.setText("do not use the exclusion list");
        inputExcludeNoButton.setText("no");
      if (fUseExcludeList == 2)
        inputExcludeNoButton.setSelection(true);
      else
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



    if (fInputType == 1) {
      inputExcludeDefaultButton.setEnabled(false);
      inputExcludeYesButton.setEnabled(false);
      inputExcludeNoButton.setEnabled(false);
     } else 
     if (fInputType == 0) {
     		if (fGlobal_is_list_input) {
		      inputExcludeDefaultButton.setEnabled(false);
    		  inputExcludeYesButton.setEnabled(false);
      		inputExcludeNoButton.setEnabled(false);
				} else {
		      inputExcludeDefaultButton.setEnabled(true);
    		  inputExcludeYesButton.setEnabled(true);
      		inputExcludeNoButton.setEnabled(true);
				}
			} else {
	      inputExcludeDefaultButton.setEnabled(true);
   		  inputExcludeYesButton.setEnabled(true);
     		inputExcludeNoButton.setEnabled(true);
			}


    SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

              if (inputExcludeDefaultButton.getSelection()) {
                fUseExcludeList = 0; // default
//System.out.println("use exclude - default");
              } else
              if (inputExcludeYesButton.getSelection()) {
                fUseExcludeList = 1; // yes, use
//                System.out.println("use exclude - yes");
              } else
              if (inputExcludeNoButton.getSelection()) {
                fUseExcludeList = 2; // no, do not use
//                System.out.println("use exclude - no");
              }
            }
        };

        inputExcludeDefaultButton.addSelectionListener(listener);
        inputExcludeYesButton.addSelectionListener(listener);
        inputExcludeNoButton.addSelectionListener(listener);


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
    public int getIfCreateInputList() {
      return fGenerateIncludeList;
    }
    public int getIfCreateExcludeList() {
      return fGenerateExcludeList;
    }

    public int getIfCreateJar() {
      return fCreateJar;
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

        useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsButton.setText("use default directory \"Express files\"");
        useDefaultsButton.setSelection(useDefaultsForExpressFiles);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);



    if (fInputType == 1) {
      useDefaultsButton.setEnabled(false);
     } else 
     if (fInputType == 0) {
     		if (fGlobal_is_list_input) {
		      useDefaultsButton.setEnabled(false);
				} else {
		      useDefaultsButton.setEnabled(true);
				}
			} else {
	      useDefaultsButton.setEnabled(true);
			}



        createUserSpecifiedExpressFileLocationGroup(projectGroup, !useDefaultsForExpressFiles);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForExpressFiles = useDefaultsButton.getSelection();
                browseButtonForExpressFiles.setEnabled(!useDefaultsForExpressFiles);
                locationPathFieldForExpressFiles.setEnabled(!useDefaultsForExpressFiles);
                locationLabelForExpressFiles.setEnabled(!useDefaultsForExpressFiles);

/*
                if (useDefaultsForExpressFiles) {
                    customExpressFileLocationFieldValue = locationPathFieldForExpressFiles.getText();
                    setLocationForSelection();
                } else {
//                  if ((customExpressFileLocationFieldValue == null) || (customExpressFileLocationFieldValue.equals(""))) {
//RRTODO                      customExpressFileLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
//                  }
                  locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
                }

*/

                if (useDefaultsForExpressFiles) {
                  locationPathFieldForExpressFiles.setText(initialExpressFileLocationFieldValue);
								} else {
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


/*
        if (initialExpressFileLocationFieldValue == null)
            locationPathFieldForExpressFiles.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForExpressFiles.setText(initialExpressFileLocationFieldValue);
*/

				if (useDefaultsForExpressFiles) {
            locationPathFieldForExpressFiles.setText(initialExpressFileLocationFieldValue);
				} else {
					locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
				}



            locationPathFieldForExpressFiles.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForExpressFiles.addListener(SWT.Modify, expressFileLocationModifyListener);
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
        projectGroup.setText("Complex entities");

        final Button useDefaultsCButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsCButton.setText("use default location (directory \"Complex entities\" inside the project)");
        useDefaultsCButton.setSelection(useDefaultsForComplexEntities);
        useDefaultsCButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsCButton.setLayoutData(buttonData);

        createUserSpecifiedComplexEntityLocationGroup(projectGroup, !useDefaultsForComplexEntities);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForComplexEntities = useDefaultsCButton.getSelection();
                browseButtonForComplexEntities.setEnabled(!useDefaultsForComplexEntities);
                locationPathFieldForComplexEntities.setEnabled(!useDefaultsForComplexEntities);
                locationLabelForComplexEntities.setEnabled(!useDefaultsForComplexEntities);



/*
                if (useDefaultsForComplexEntities) {
                    customComplexEntityLocationFieldValue = locationPathFieldForComplexEntities.getText();
                    setLocationForSelection();
                } else {
//RRTODO                    customComplexEntityLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
                    locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
                }

*/

                if (useDefaultsForComplexEntities) {
                  locationPathFieldForComplexEntities.setText(initialComplexEntityLocationFieldValue);
                } else {
                  locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
                }



            }
        };
        useDefaultsCButton.addSelectionListener(listener);
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


/*
        if (initialComplexEntityLocationFieldValue == null)
            locationPathFieldForComplexEntities.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForComplexEntities.setText(initialComplexEntityLocationFieldValue);
*/

				if (useDefaultsForComplexEntities) {
					locationPathFieldForComplexEntities.setText(initialComplexEntityLocationFieldValue);
				} else {
					locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
				}


        locationPathFieldForComplexEntities.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForComplexEntities.addListener(SWT.Modify, complexEntityLocationModifyListener);
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
        projectGroup.setText("Short names");

        final Button useDefaultsSButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
//        useDefaultsButton.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_useDefaultLabel);
        useDefaultsSButton.setText("use default location (directory \"Short names\" inside the project)");
        useDefaultsSButton.setSelection(useDefaultsForShortNames);
        useDefaultsSButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsSButton.setLayoutData(buttonData);

        createUserSpecifiedShortNameLocationGroup(projectGroup, !useDefaultsForShortNames);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useDefaultsForShortNames = useDefaultsSButton.getSelection();
                browseButtonForShortNames.setEnabled(!useDefaultsForShortNames);
                locationPathFieldForShortNames.setEnabled(!useDefaultsForShortNames);
                locationLabelForShortNames.setEnabled(!useDefaultsForShortNames);


/*
                if (useDefaultsForShortNames) {
                    customShortNameLocationFieldValue = locationPathFieldForShortNames.getText();
                    setLocationForSelection();
                } else {
//RRTODO                    customShortNameLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
                    locationPathFieldForShortNames.setText(customShortNameLocationFieldValue);
                }
*/

                if (useDefaultsForShortNames) {
                  locationPathFieldForShortNames.setText(initialShortNameLocationFieldValue);
								} else {
                  locationPathFieldForShortNames.setText(customShortNameLocationFieldValue);
								}

            }
        };
        useDefaultsSButton.addSelectionListener(listener);
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

/*
        if (initialShortNameLocationFieldValue == null)
            locationPathFieldForShortNames.setText(Platform.getLocation().toOSString());
        else
            locationPathFieldForShortNames.setText(initialShortNameLocationFieldValue);
*/

				if (useDefaultsForShortNames) {
					locationPathFieldForShortNames.setText(initialShortNameLocationFieldValue);
				} else {
					locationPathFieldForShortNames.setText(customShortNameLocationFieldValue);
				}


        locationPathFieldForShortNames.setEditable(false);
        //locationPathFieldForTemp.addListener(SWT.Modify, locationModifyListener);
        locationPathFieldForShortNames.addListener(SWT.Modify, shortNameLocationModifyListener);
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
        dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customShortNameLocationFieldValue = selectedDirectory;
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
        dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customComplexEntityLocationFieldValue = selectedDirectory;
            locationPathFieldForComplexEntities.setText(customComplexEntityLocationFieldValue);
        }
    }






    void handleExpressFileLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(locationPathFieldForExpressFiles.getShell(), SWT.SINGLE);
//        dialog.setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_directoryLabel);
        dialog.setMessage("Select a directory for the express files of this project, preferably, inside the workspace");

        String dirName = getExpressFileLocationFieldValue();
        if (dirName.equals("")) {
//RRTODO          dirName = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
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
        dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());

      


/*
                  if ((customExpressFileLocationFieldValue == null) || (customExpressFileLocationFieldValue.equals(""))) {
                    customExpressFileLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath().toString();
                  }
                  locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);

*/
        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customExpressFileLocationFieldValue = selectedDirectory;
            locationPathFieldForExpressFiles.setText(customExpressFileLocationFieldValue);
        }
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

        return locationPathFieldForExpressFiles.getText().trim();
    }

    private String getShortNameLocationFieldValue() {
        if (locationPathFieldForShortNames == null)
            return ""; //$NON-NLS-1$

        return locationPathFieldForShortNames.getText().trim();
    }

    private String getComplexEntityLocationFieldValue() {
        if (locationPathFieldForComplexEntities == null)
            return ""; //$NON-NLS-1$

        return locationPathFieldForComplexEntities.getText().trim();
    }

    void setExpressFileLocationForSelection() {
//RRTODO        String express_file_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files";
String express_file_default = fProject.getLocation().toOSString() + File.separator + "Express files";

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
//RRTODO        String short_name_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names";
String short_name_default = "";
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
//RRTODO        String complex_entity_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities";
String complex_entity_default = "";
//        String complex_entity_default = getTempLocationPath() + File.separator + "ExpressCompilerRepo";
        if (useDefaultsForComplexEntities)
            locationPathFieldForComplexEntities
                    .setText(complex_entity_default);
    }

    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    void setLocationForSelection() {
//RRTODO        String project_default = NewExpressProjectWizard.getMainPage().getDefaultLocationForName(NewExpressProjectWizard.getMainPage().getProjectNameFieldValue());
      String project_default = "";
      String express_file_default = project_default + File.separator + "Express files";
//RRTODO        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) express_file_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files";
        if (useDefaultsForExpressFiles) {
            locationPathFieldForExpressFiles
                    .setText(express_file_default);
        }
        String short_name_default = project_default + File.separator + "Short names";
//RRTODO        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) short_name_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names";
//        if (!useDefaultsForExpressFiles) short_name_default = getExpressFileLocationPath() + File.separator + "Short names";
        if (useDefaultsForShortNames) {
            locationPathFieldForShortNames
                    .setText(short_name_default);
        }
        String complex_entity_default = project_default + File.separator + "Complex entities";
//RRTODO        if (!NewExpressProjectWizard.getMainPage().useDefaultsForProject) complex_entity_default = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities";
//        if (!useDefaultsForExpressFiles) short_name_default = getExpressFileLocationPath() + File.separator + "Short names";
        if (useDefaultsForComplexEntities) {
            locationPathFieldForComplexEntities
                    .setText(complex_entity_default);
        }
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
    return customExpressFileLocationFieldValue;
  }

  public String getCustomShortNameLocation() {
    return customShortNameLocationFieldValue;
  }

  public String getCustomComplexEntityLocation() {
    return customComplexEntityLocationFieldValue;
  }

   



      public boolean performOk() {
    
			String express_file_location = locationPathFieldForExpressFiles.getText();
			String default_express_file_location = initialExpressFileLocationFieldValue;
			
			String short_name_location = locationPathFieldForShortNames.getText();
			String default_short_name_location = initialShortNameLocationFieldValue;
			
			String complex_entity_location = locationPathFieldForComplexEntities.getText();
			String default_complex_entity_location = initialComplexEntityLocationFieldValue;		


        try {


        	IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);

          switch (fInputType) {
            case 0: // default
            	prefs.put("fInputType", "default");
              break;
            case 1: // list
            	prefs.put("fInputType", "list");
              break;
            case 2: // directory - flat
            	prefs.put("fInputType", "flat");
              break;
            case 3: // directory - recursive
            	prefs.put("fInputType", "recurse");
              break;
            default:
              // internal error
              break;
            
          }   




        switch (fUseExcludeList) {
        case 0: // default
        	prefs.put("fUseExclude", "default");
          break;
        case 1: // yes - use
        	prefs.put("fUseExclude", "yes");
          break;
        case 2: // no - do not use
        	prefs.put("fUseExclude", "no");
          break;
        default:
          // internal error
          break;
      }
          
        
          switch (fCreateJar) {
      case 0: // default
    	  prefs.put("fCreateJar", "default");
      break;
    case 1: // yes - use
    	prefs.put("fCreateJar", "yes");
      break;
    case 2: // no - do not use
    	prefs.put("fCreateJar", "no");
      break;
    default:
      // internal error
      break;
    }

				if (useDefaultsForExpressFiles) {
					prefs.put("fDefaultExpressFileLocation", "true");
				} else {
					prefs.put("fDefaultExpressFileLocation", "false");
					prefs.put("expressFileLocation", express_file_location);
				}

				if (useDefaultsForShortNames) {
					prefs.put("fDefaultShortNameLocation", "true");
				} else {
					prefs.put("fDefaultShortNameLocation", "false");
					prefs.put("shortNameLocation", short_name_location);
				}

				if (useDefaultsForComplexEntities) {
					prefs.put("fDefaultComplexEntityLocation", "true");
				} else {
					prefs.put("fDefaultComplexEntityLocation", "false");
					prefs.put("complexEntityLocation", complex_entity_location);
				}

        prefs.flush();
      } catch (BackingStoreException e) {
				ExpressCompilerPlugin.log(e);
        e.printStackTrace();
      }

    return true;
    
     }
    

    
}





