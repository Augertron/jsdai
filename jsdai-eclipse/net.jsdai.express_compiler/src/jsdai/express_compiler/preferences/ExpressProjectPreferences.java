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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ExpressProjectPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {


	static ExpressProjectPreferences fExpressProjectPreferences;

	public static final String TEMP_TYPE = "express.tempType";
	public static final String CREATE_EXCLUDE = "express.createExclude";
	public static final String CREATE_INCLUDE = "express.createInclude";

	String original_temp_type = null;
	String new_temp_type = null;


  public ExpressProjectPreferences() {
    super(GRID);
		setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
    setDescription("Global settings for Express projects, may be overriden in properties of individual Express projects");
		initializeDefaults();
		fExpressProjectPreferences = this;
  }
  
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();

		store.setDefault(TEMP_TYPE, "SYSTEM");
		store.setDefault(CREATE_EXCLUDE, true);
		store.setDefault(CREATE_INCLUDE, false);

	}  

  protected void createFieldEditors() {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFieldEditorParent(), IExpressCompilerHelpContextIds.EXPRESS_PROJECT_PREFERENCE_PAGE);

		Composite parent = getFieldEditorParent();
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		separator.setLayoutData(gd);

		addField(new BooleanFieldEditor(CREATE_INCLUDE, ExpressCompilerPlugin.getResourceString("preferences.createInclude"), getFieldEditorParent()));
		addField(new BooleanFieldEditor(CREATE_EXCLUDE, ExpressCompilerPlugin.getResourceString("preferences.createExclude"), getFieldEditorParent()));


    // add if needed anything for general preferences


/*

		temp_type = new RadioGroupFieldEditor(
				TEMP_TYPE, 
				ExpressCompilerPlugin.getResourceString("preferences.tempType"), 
                1,
                new String[][] {
						{"in system temp", "SYSTEM"},
						{"temp in Eclipse workspace", "ECLIPSE"},
						{"temp directory in Express project", "PROJECT"}
				},
				getFieldEditorParent(),
				true
			);


		addField(temp_type);
*/
	
	
		addField(new RadioGroupFieldEditor(
				TEMP_TYPE, 
				ExpressCompilerPlugin.getResourceString("preferences.tempType"), 
                1,
                new String[][] {
						{"in system temp", "SYSTEM"},
						{"temp in Eclipse workspace", "ECLIPSE"},
						{"temp directory in Express project", "PROJECT"}
				},
				getFieldEditorParent(),
				true
			)
		);



  }

	public static ExpressProjectPreferences getExpressProjectPreferences() {
		return fExpressProjectPreferences;
	}

  public void init(IWorkbench workbench) {
    // see above, also may be needed or not

  }

//    public boolean performOk() {
//				super.performOk();
//
//			if (original_temp_type == null) {
//				// obviously, there was no change, so nothing needs to be done
//				return true;
//			}
//			if (new_temp_type == null) {
//				// will do it only once
//				new_temp_type = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);
//				if (original_temp_type.equals(new_temp_type)) { // already checked that original is no null
//					// property not changed, nothing to do
//					return true;
//				}
//				// ok, at this point we know that something has changed, what we want to do?
//				// 1. to delete the old temp in all the project that use temp setting as default
//				// 2. if new type is PROJECT, to create temp directory in all projects that use temp setting as default
//				// refresh all the projects
//
//				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//				IProject[] projects = root.getProjects();
//				for (int i = 0; i < projects.length; i++) {
//					IProject project = projects[i];
//					
//					// NOTE: it would be good to add try/catch blocks for each of the internal errors for recovery (where == null) is checked
//					// instead, added logging, so exception has to be examined in such a case
//					try {
//						if (project.hasNature("net.jsdai.express_compiler.expressNature")) {
//						    IEclipsePreferences prefs = new ProjectScope(project).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
//							// it is an express project
//							// see if it is using DEFAULT setting for temp location
//							String temp_location_type = project.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fTempLocation"));
//							if (temp_location_type == null) continue;	
//							if (temp_location_type.equalsIgnoreCase("DEFAULT")) {
//								// yes, this project is affected
//
//								// 1. delete the old location
//
//								String temp_location = project.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocation"));
//								if (temp_location != null) {
//										ExpressCompilerUtils.deleteAllFilesAndDirectories(temp_location);
//										// if == null - not sure what to delete, but at least we will try to set new value so that it is no longer null
//								}
//
//								// 3. change the temp location property in the project 
//								//    - to ensure backward compatibility, 
//								//    the compiler action, etc, could check and not use directly, but it is too dangerous to hope for that
//
//							    if (new_temp_type.equals("SYSTEM")) {
//									String temp_location_system = project.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".tempLocationSystem"));
//									if (temp_location_system == null) {
//						// should not have occurred, but now let's try to come up with a value for this case
//										temp_location_system = ExpressCompilerUtils.getSystemTempDirectoryString();
//										project.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".tempLocationSystem"), temp_location_system);
//									} 						  
//									prefs.put("tempLocation", temp_location_system);
//							  		} else
//							        if (new_temp_type.equals("ECLIPSE")) {
//									String temp_location_eclipse = prefs.get("tempLocationEclipse", null);
//									if (temp_location_eclipse == null) {
//										// again, need a value to try to recover
////									    IPath working_location = project.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());
//								    IPath working_location = project.getWorkingLocation(ExpressCompilerPlugin.getUniqueIdentifier());
//									 	temp_location_eclipse = working_location.toString();
//									 	prefs.put("tempLocationEclipse", temp_location_eclipse );
//									}
//									prefs.put("tempLocation", temp_location_eclipse);
//							  } else
//							  if (new_temp_type.equals("PROJECT")) {
//									String temp_location_project = prefs.get("tempLocationProject", null);
//									if (temp_location_project == null) {
//										// and yet again, compute the location string ourselves
//										temp_location_project = project.getLocation().toOSString() + File.separator + "temp";
//										prefs.get("tempLocationProject", temp_location_project);
//									}
//									prefs.get("tempLocation", temp_location_project);
//									
//									// 2. create temp directory, if new type is PROJECT
//									IFolder temp_folder = project.getFolder("temp");
//									if (!temp_folder.exists()) {
//										temp_folder.create(true, true, null);
//									}
//							  } else {
//							  	// internal error, embarrassing, especially here, where this new value had to be just set.
//							  }
//
//
//							}
//						    prefs.flush();
//
//							// 4. refresh the project
//						project.refreshLocal(IResource.DEPTH_ONE, null);
//
//						}
//					} catch (CoreException e) {
//						ExpressCompilerPlugin.log(e);
//						e.printStackTrace();
//					} catch (BackingStoreException e) {
//						ExpressCompilerPlugin.log(e);
//						e.printStackTrace();
//					}
//				} // for - loop through all projects
//			}
//      	
//
//        return true;
//    }

	 public void propertyChange(PropertyChangeEvent event) {
		 super.propertyChange(event);
		 // let's do it only once, not 3 times on each property change
		 if (original_temp_type == null) {
				original_temp_type = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);
		 }
	}

}

