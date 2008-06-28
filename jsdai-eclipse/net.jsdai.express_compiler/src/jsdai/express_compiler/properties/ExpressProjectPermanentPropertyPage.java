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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
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
public class ExpressProjectPermanentPropertyPage extends PropertyPage {
//public class NewExpressProjectWizardPermanentPage extends WizardNewProjectCreationPage {

	 IProject fProject;

  int fCreateJar;
		

    static ExpressProjectPermanentPropertyPage fgExpressProjectPermanentPropertyPage;



    // widgets

    
    public static Shell fgShell;


   
    
    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;
  
  
  
  public ExpressProjectPermanentPropertyPage() {
    super();
//        setPageComplete(false);

    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }



					    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);



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
    


//        customExpressFileLocationFieldValue = getExpressFileDirectoryPathString();

//        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files";

//System.out.println("instance ((())): " + NewExpressProjectWizard.getInstance());

//        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getInstance().fProjectPath;

// different kind of initialization will be needed        
/*         
        
        initialExpressFileLocationFieldValue = NewExpressProjectWizard.getInstance().fProjectPath +
                                               File.separator +
                                               NewExpressProjectWizard.getInstance().fProjectName +
                                               File.separator +
                                               "Express files";
                                               
        initialShortNameLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names";
        initialComplexEntityLocationFieldValue = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities";
System.out.println("PERMANENT - CONSTRUCTOR");
    fgNewExpressProjectWizardPermanentPage = this;
*/
  
  }

  public static ExpressProjectPermanentPropertyPage getInstance() {
    return fgExpressProjectPermanentPropertyPage;
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

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.EXPRESS_PROJECT_PERMANENT_PROPERTY_PAGE);

    ISelection sel = ExpressCompilerPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

    if (sel instanceof IStructuredSelection) {
      IStructuredSelection ssel = (IStructuredSelection) sel;
      Object obj = ssel.getFirstElement();
      if (obj instanceof IProject) {
        fProject = (IProject)obj;
      } 

    }



	    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);


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



        Composite composite = new Composite(parent, SWT.NULL);
				fgShell = composite.getShell();
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);


        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));


          createJarGroup(composite);            


        Label note = new Label(composite, SWT.NONE);
        note.setText("NOTE: \"default\" radio button means the corresponding setting in the global Express preferences");
        //note.setColor(blue);
        note.setForeground(new Color(parent.getDisplay(),0,0,100));     
            GridData labelgd = new GridData();
                labelgd.horizontalIndent = 10;
                labelgd.verticalIndent = 10;
            note.setLayoutData(labelgd);


//        createProjectNameGroup(composite);
//        createProjectLocationGroup(composite);

/*
        Label note1 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
        Label note2 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
//        Label note3 = new Label(composite, SWT.SHADOW_IN | SWT.WRAP);
        note1.setText("HINT: you may specify as the location of express files another Eclipse project or a directory in it");
        note2.setText("where you already have the express files you need for this project");
//        note3.setText("if you need the generated java files for debugging or modifying purposes");
        //note1.setForeground(new Color(parent.getDisplay(),0,0,128));      
        //note2.setForeground(new Color(parent.getDisplay(),0,0,128));      
        //note3.setForeground(new Color(parent.getDisplay(),0,0,128));      
        note1.setForeground(new Color(composite.getDisplay(),0,80,80));     
        note2.setForeground(new Color(composite.getDisplay(),0,80,80));     
//        note3.setForeground(new Color(parent.getDisplay(),0,80,80));      
        GridData labelgd = new GridData();
        GridData labelgd2 = new GridData();
  //      GridData labelgd3 = new GridData();
        labelgd.horizontalSpan = 2;
        labelgd.verticalIndent = 10;
        labelgd.horizontalIndent = 10;
        labelgd2.horizontalSpan = 2;
        labelgd2.horizontalIndent = 40;
//        labelgd3.horizontalSpan = 2;
//        labelgd3.horizontalIndent = 40;
        // labelgd.verticalIndent = 10;
        note1.setLayoutData(labelgd);
        note2.setLayoutData(labelgd2);
//        note3.setLayoutData(labelgd3);

*/

//        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
//        setControl(composite);

        noDefaultAndApplyButton();

//System.out.println("PERMANENT - Create Control");
      return composite;
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
      if (fCreateJar == 0) 
        jarDefaultButton.setSelection(true);
      else
        jarDefaultButton.setSelection(false);
      jarDefaultButton.setFont(font);

      final Button jarYesButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      jarYesButton.setText("yes, generate and compile java and create jar as well as exd");
      if (fCreateJar == 1) 
        jarYesButton.setSelection(true);
      else
        jarYesButton.setSelection(false);
      jarYesButton.setFont(font);

      final Button jarNoButton = new Button(listGroup, SWT.RADIO | SWT.LEFT);
      jarNoButton.setText("no, just check the express for errors and, if success, create exd only");
      if (fCreateJar == 2) 
        jarNoButton.setSelection(true);
      else
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





      public boolean performOk() {
    

        try {


        	IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);


        
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

        prefs.flush();
      } catch (BackingStoreException e) {
				ExpressCompilerPlugin.log(e);
        e.printStackTrace();
      }

    return true;
    
     }
    



}


