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
import java.io.StringBufferInputStream;
import java.lang.reflect.InvocationTargetException;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.preferences.ExpressProjectPreferences;
import jsdai.express_compiler.utils.ExpressCompilerRepo;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.eclipse.core.resources.IResource;
import org.osgi.service.prefs.BackingStoreException;

public class NewExpressProjectWizard extends BasicNewResourceWizard implements INewWizard, IExecutableExtension {


	static NewExpressProjectWizard fNewExpressProjectWizard;

	private static final String PREFIX= "ExpressProjectWizard"; //$NON-NLS-1$

//	protected WizardNewProjectCreationPage fProjectPage;
	protected NewExpressProjectWizardMainPage fProjectMainPage; 
	protected static NewExpressProjectWizardMainPage fgProjectMainPage; 
	protected NewExpressProjectWizardPermanentPage fProjectPermanentPage; 
	protected NewExpressProjectWizardTempPage fProjectTempPage; 
	protected NewExpressProjectWizardInputPage fProjectInputPage; 
	protected NewExpressProjectWizardOptionPage fProjectOptionPage; 
	protected IConfigurationElement fConfigurationElement;
	protected IProject fProject;

  // persistent properties
	String fProjectName;
	
	String fProjectPath;
	String fDefaultProjectPath;
	boolean fIsDefaultProjectPath;
	
	String fExpressFileLocation;
	String fDefaultExpressFileLocation;
	boolean fIsDefaultExpressFileLocation;
	
	String fShortNameLocation;
	String fDefaultShortNameLocation;
	boolean fIsDefaultShortNameLocation;

	String fComplexEntityLocation;
	String fDefaultComplexEntityLocation;
	boolean fIsDefaultComplexEntityLocation;

	String fTempLocation;
	String fDefaultTempLocation;
	String fDefaultPersistentTempLocation;
	boolean fIsDefaultTempLocation;
	boolean fIsDefaultPersistentTempLocation;
	
	String fRepoLocation;
	String fDefaultRepoLocation;
	String fDefaultPersistentRepoLocation;
	boolean fIsDefaultRepoLocation;
	boolean fIsDefaultPersistentRepoLocation;

	public NewExpressProjectWizard() {
		fNewExpressProjectWizard = this;
		setWindowTitle("New Express Project");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
/*
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
*/

	public static NewExpressProjectWizard getInstance() {
		return fNewExpressProjectWizard;
	}

	public void initDefaultValues(String project_name) {
	}


	public boolean performFinish() {

		IRunnableWithProgress projectCreationOperation =
			new WorkspaceModifyDelegatingOperation(
				getProjectCreationRunnable());
		try {
			getContainer().run(false, true, projectCreationOperation);
		} catch (Exception e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("perform Finish problems: " + e);
//		Throwable ue = e.getCause();
//		System.out.println("perform Finish problems, cause: " + ue);
	return false;
		}


		ExpressCompilerUtils.switchProjectPerspective();

		//BasicNewProjectResourceWizard.updatePerspective(fConfigurationElement);
		selectAndReveal(fProject);

//		String containerName = fProjectMainPage.getProjectName();

//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		IResource resource = root.findMember(new Path(containerName));

			IResource resource = (IResource)fProject;

//        IProject project = resource.getProject();


		// Express files
		// probably the only case that is interesting to us is default, because we need to create a subdirectory "Express files"
		// in all the other cases we only choose a directory that already exists, or do we?
		
		
		// let's get the directory:
		
	String s_customExpressFileLocationFieldValue = "_no_value_";;
	String s_is_default = "_no_value_";;
    IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);

	s_is_default = prefs.get("fDefaultExpressFileLocation", "true").toLowerCase();
	if("true".equals(s_is_default)) {
		IFolder projectExpressFilesLocation = fProject.getFolder("Express files");
		s_customExpressFileLocationFieldValue = projectExpressFilesLocation.getLocation().toOSString();
	} else {
		s_customExpressFileLocationFieldValue = prefs.get("expressFileLocation", "");
	}

//System.out.println("Express files - custom: " + s_customExpressFileLocationFieldValue);
//System.out.println("Express files - is default: " + s_is_default);
		if (s_is_default.equalsIgnoreCase("true")) {
			IFolder express_src = resource.getProject().getFolder("Express files");
			if(!express_src.exists()) {
				try {
					express_src.create(true, true, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
					// TODO Auto-generated catch block
//			System.out.println("Exception - Problems with Express Files - creating folder: " + e1);
//					e1.printStackTrace();
				}
			}
		} else 
		if (s_customExpressFileLocationFieldValue.replace('\\','/').equalsIgnoreCase(resource.getProject().getLocation().toString().replace('\\','/'))) {
				// the whole project itself
				// System.out.println("Express files = project itself");
		} else {
			// see if inside the project = resource, or outside project losing the advantages of being resource, don't bother if in other projects, for now
			if (s_customExpressFileLocationFieldValue.replace('\\','/').startsWith(resource.getProject().getLocation().toString().replace('\\','/'))) {
//				System.out.println("yes, it starts");
				String express_files_folder = s_customExpressFileLocationFieldValue.substring(resource.getProject().getLocation().toString().length()+1,s_customExpressFileLocationFieldValue.length()); 
//				System.out.println("express subfolder: " + express_files_folder);
//				IFolder express_src = resource.getProject().getFolder(express_files_folder);
			} else {
				// let's treat it as a non-resource location
//				System.out.println("no, it does not start");
//				System.out.println("custom: " + s_customExpressFileLocationFieldValue);
//				System.out.println("project: " + resource.getProject().getLocation().toString());
			}
		}


		// 0 - default, 1 - create, 2 - do not create
		int if_create_exclude_list = fProjectPermanentPage.getIfCreateExcludeList(); 

		boolean flagCreateExclude  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressProjectPreferences.CREATE_EXCLUDE);
		// try again
		//flagCreateExclude  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressProjectPreferences.CREATE_EXCLUDE);
    if (((if_create_exclude_list == 0) && flagCreateExclude) || (if_create_exclude_list == 1)) {
   
			String exclusion_file_name = fProject.getName() + "_excluded.exl";
//			String exclude_file_path = fProject.getLocation().toString() + File.separator + exclusion_file_name;		
			String starting_directory = s_customExpressFileLocationFieldValue.replace('/', File.separatorChar).replace('\\', File.separatorChar) + File.separator;
			String exclude_file_contents = "\n# List of names with full paths of express files to be excluded from the compilation\n# The starting directory with express files is: \n# " + starting_directory + "\n# You may also drag the express files to be excluded from the Navigator and drop them here\n\n\n\n#### end of the list ####\n\n";
			StringBufferInputStream exs = new StringBufferInputStream(exclude_file_contents);
			IFile exclude_file = resource.getProject().getFile(exclusion_file_name);	
			try {
				exclude_file.create(exs, false, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}


		int if_create_input_list = fProjectPermanentPage.getIfCreateInputList();

		boolean flagCreateList  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressProjectPreferences.CREATE_INCLUDE);
    // if (flagCreateList) {
    if (((if_create_input_list == 0) && flagCreateList) || (if_create_input_list == 1)) {

//  		if (true) {
   
			String inclusion_file_name = fProject.getName() + ".exl";
//			String list_file_path = fProject.getLocation().toString() + File.separator + inclusion_file_name;		
//			String starting_directory = s_customExpressFileLocationFieldValue.replace('/', File.separatorChar).replace('\\', File.separatorChar) + File.separator;
			String list_file_contents = "\n# List of names with full paths of express files to be included into the compilation\n# You may drag and drop express files here from the Navigator\n# You may comment out the files that are to be temporarily excluded\n\n\n#### end of the list ####\n\n";			
			StringBufferInputStream exs = new StringBufferInputStream(list_file_contents);
			IFile list_file = resource.getProject().getFile(inclusion_file_name);	
			try {
				list_file.create(exs, false, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}

		// short names
		String s_customShortNameLocationFieldValue = "_no_value_";;
		String s_is_default_shortNameLocation = "_no_value_";;
	    s_is_default_shortNameLocation = prefs.get("fDefaultShortNameLocation", "true").toLowerCase();
		if("true".equals(s_is_default_shortNameLocation)) {
			IFolder projectShortNamesLocation = fProject.getFolder("Short names");
			s_customShortNameLocationFieldValue  = projectShortNamesLocation.getLocation().toOSString();
		} else {
			s_customShortNameLocationFieldValue  = prefs.get("shortNameLocation", "");
		}
//System.out.println("Short names - custom: " + s_customShortNameLocationFieldValue);
//System.out.println("Short names  - is default: " + s_is_default_shortNameLocation);
		if (s_is_default_shortNameLocation.equalsIgnoreCase("true")) {
			IFolder short_names = resource.getProject().getFolder("Short names");
			if(!short_names.exists()) {
				try {
					short_names.create(true, true, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
//			System.out.println("Exception - Problems with Short Names - creating folder: " + e1);
//					e1.printStackTrace();
				}
			}
		} else {
			// not important here, do not create new folders
		}

		// complex entities
		String s_customComplexEntityLocationFieldValue = "_no_value_";;
		String s_is_default_complexEntityLocation = "_no_value_";;
		s_is_default_complexEntityLocation = prefs.get("fDefaultComplexEntityLocation", "true").toLowerCase();
		if("true".equals(s_is_default_complexEntityLocation)) {
			IFolder projectShortNamesLocation = fProject.getFolder("Complex entities");
			s_customComplexEntityLocationFieldValue  = projectShortNamesLocation.getLocation().toOSString();
		} else {
			s_customComplexEntityLocationFieldValue  = prefs.get("complexEntityLocation", "");
		}
//System.out.println("Complex entities - custom: " + s_customComplexEntityLocationFieldValue);
//System.out.println("Complex entities  - is default: " + s_is_default_complexEntityLocation);
		if (!"false".equalsIgnoreCase(s_is_default_complexEntityLocation)) {
			IFolder complex_lists = resource.getProject().getFolder("Complex entities");
			if(!complex_lists.exists()) {
				try {
					complex_lists.create(true, true, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
//			System.out.println("Exception - Problems with Complex Entities - creating folder: " + e1);
//					e1.printStackTrace();
				}
			}
		} else {
			// not interested because no need to create a new folder
		}



		// temp location
// fTempLocation  - SYSTEM/ECLIPSE/PROJECT/CUSTOM    OS default / Eclipse workspace .metadata project-specific / temp inside project / custom
// fDeleteOnExit  - true/false
// tempLocation   - may be custom or the same as Eclipse or system
// tempLocationEclipse - project-specific location in workspace/.metatada
// tempLocationProject - temp inside project
// tempLocationSystem  - OS default temp location, project specific unique subdirectory
	if(false) {
	 String s_temp_location_type = "_no_value_";
	 String s_delete_temp_on_exit = "_no_value_";
	 String s_temp_location = "_no_value_";
	 String s_temp_location_global_type = null;	
	 s_temp_location_global_type = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);

	 s_temp_location = prefs.get("tempLocation", "");
	 s_temp_location_type = prefs.get("fTempLocation", "SYSTEM");
	 s_delete_temp_on_exit = prefs.get("fDeleteOnExit", "false");
//System.out.println("temp location - type: " + s_temp_location_type);
//System.out.println("temp location - delete on exit: " + s_delete_temp_on_exit);
//System.out.println("temp location - custom: " + s_temp_location);

		if (s_temp_location_global_type == null) {
			s_temp_location_global_type = "SYSTEM";
		}

		String custom_temp_location = s_temp_location;

		if ((s_temp_location_type.equalsIgnoreCase("PROJECT")) || (s_temp_location_type.equalsIgnoreCase("DEFAULT") && s_temp_location_global_type.equalsIgnoreCase("PROJECT"))) {
			IFolder internal_src = resource.getProject().getFolder("temp");
//			IFolder java_output = resource.getProject().getFolder("temp/java");
//			IFolder class_compiled = resource.getProject().getFolder("temp/classes");

			if(!internal_src.exists()) {
				try {
					internal_src.create(true, true, null);
//					java_output.create(true, true, null);
//					class_compiled.create(true, true, null);
				} catch (CoreException e1) {
					ExpressCompilerPlugin.log(e1);
//			System.out.println("Exception - Problems with temp location - creating folders: " + e1);
//					e1.printStackTrace();
				}
			}
		} else {
			// not resource, but we could have handled the above case this way as well

//				String custom_temp_location = fProjectTempPage.getCustomTempLocation();
//				String custom_temp_location = fProjectPage.getCustomTempLocation() + File.separator + "temp";
//				String custom_temp_location = s_temp_location;
				File custom_temp_dir = new File(custom_temp_location);
	  	  custom_temp_dir.mkdirs();
		  ExpressCompilerUtils.verifyPath(custom_temp_dir, false);
//	  	  String custom_java_location = custom_temp_location + File.separator + "java";
//				File custom_java_dir = new File(custom_java_location);
//  	  	custom_java_dir.mkdirs();
//	  	ExpressCompilerUtils.verifyPath(custom_java_dir, false);

  	  	String custom_class_location = custom_temp_location + File.separator + "classes";
				File custom_class_dir = new File(custom_class_location);
  		  custom_class_dir.mkdirs();
  	  	ExpressCompilerUtils.verifyPath(custom_class_dir, false);
		}

		}

/* **************************************************************** repository ****************************************************************

		// ExpressCompilerRepo
// fDefaultRepoLocation - true/false
// repoLocation         
// repoLocationDefault  - inside temp directory, subdirectory ExpressCompilerRepo - copy tempLocation, or store nothing, lookup when needed
	 String s_is_default_repo_location = "_no_value_";
	 String s_delete_repo_on_exit = "_no_value_";
	 String s_repo_location = "_no_value_";
	 String s_repo_location_default = "_no_value_";
	 
	try {
		s_repo_location_default = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".repoLocationDefault"));
		s_repo_location = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".repoLocation"));
	  s_is_default_repo_location = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDefaultRepoLocation"));
	  s_delete_repo_on_exit = fProject.getPersistentProperty(new QualifiedName("net.jsdai.express_compiler",".fDeleteRepoOnExit"));
	
	} catch (CoreException e1) {
				ExpressCompilerPlugin.log(e1);
		System.out.println("Exception - Problems with Repo location persistent properties: " + e1);
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
System.out.println("repo location - is default: " + s_is_default_repo_location);
System.out.println("repo location - to delete: " + s_delete_repo_on_exit);
System.out.println("repo location - custom: " + s_repo_location);
System.out.println("repo location - default: " + s_repo_location_default);



			String jsdai_properties_contents = null;
			String new_jsdai_properties_contents = null;
			int ind = 0;
			int ind0 = 0;


 
		if ((s_temp_location_type.equalsIgnoreCase("PROJECT")) && (s_is_default_repo_location.equalsIgnoreCase("YES")))  {
			// handle as resource (without any reason, though)
//		IFolder jars = resource.getProject().getFolder("temp/jars");
//		IFolder html_expressdoc = resource.getProject().getFolder("ExpressDoc html");
			IFolder repository = resource.getProject().getFolder("temp/ExpressCompilerRepo");
			IFile jsdai_properties = resource.getProject().getFile("temp/jsdai.properties");	

			try {
					repository.create(true, true, null);
					ExpressCompilerRepo.copy(repository, resource);
					jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";

					new_jsdai_properties_contents = "";
					for (;;) {
						ind = jsdai_properties_contents.indexOf(File.separator, ind0);
						if (ind >=0) {
							new_jsdai_properties_contents += jsdai_properties_contents.substring(ind0,ind + 1);
							new_jsdai_properties_contents += File.separator;
							ind0 = ind+1;
						} else {
							new_jsdai_properties_contents +=  jsdai_properties_contents.substring(ind0);
							break;
						}
					}
					// StringReader - prefered to use instead of StringBufferInputStream
					StringBufferInputStream is = new StringBufferInputStream(new_jsdai_properties_contents);
					jsdai_properties.create(is, false, null);



			} catch (CoreException e1) {
				ExpressCompilerPlugin.log(e1);
		System.out.println("Exception - Problems with Repo - creating folder etc: " + e1);
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else {
//					String custom_repo_location  = fProjectTempPage.getCustomRepoLocation() + File.separator + "ExpressCompilerRepo";
					String custom_repo_location  = s_repo_location;
					boolean f_delete_on_exit = true;
					if (s_delete_repo_on_exit.equalsIgnoreCase("false")) {
						f_delete_on_exit = false;
					}
					ExpressCompilerRepo.copy(custom_repo_location, f_delete_on_exit);
					jsdai_properties_contents = 	"repositories=" + custom_repo_location;

				new_jsdai_properties_contents = "";
				for (;;) {
					ind = jsdai_properties_contents.indexOf(File.separator, ind0);
					if (ind >=0) {
						new_jsdai_properties_contents += jsdai_properties_contents.substring(ind0,ind + 1);
						new_jsdai_properties_contents += File.separator;
						ind0 = ind+1;
					} else {
						new_jsdai_properties_contents +=  jsdai_properties_contents.substring(ind0);
						break;
					}
				}
				PrintWriter pw;
				try {
					pw = ExpressCompilerRepo.getPrintWriter(custom_temp_location + File.separator + "jsdai.properties");
					pw.println(new_jsdai_properties_contents);
					pw.flush();
					pw.close();
				} catch (IOException e) {
				ExpressCompilerPlugin.log(e);
		System.out.println("Exception - Problems with Repo - PrintWriter: " + e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


		}

		


//		IFile jsdai_properties = resource.getProject().getFile("jsdai.properties");	
//		public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException;



*/







/*

//			jars.create(true, true, null);
//			html_expressdoc.create(true, true, null);


//		String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "temp" + File.separator + "ExpressCompilerRepo";
		String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";

//			String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";
//			String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation() + "\\ExpressCompilerRepo";

			int ind = 0;
			int ind0 = 0;
			String new_jsdai_properties_contents = "";
			for (;;) {
				ind = jsdai_properties_contents.indexOf(File.separator, ind0);
// System.out.println("ind: " + ind + ", ind0: " + ind0 + ", new: " + new_jsdai_properties_contents);
				if (ind >=0) {
					new_jsdai_properties_contents += jsdai_properties_contents.substring(ind0,ind + 1);
					new_jsdai_properties_contents += File.separator;
					ind0 = ind+1;
				} else {
					new_jsdai_properties_contents +=  jsdai_properties_contents.substring(ind0);
					break;
				}
			}
			
//				"repositories=E:\\eclipse11\\runtime-workbench-workspace\\RR1\\ExpressCompilerRepo";

//			StringBufferInputStream is = new StringBufferInputStream(jsdai_properties_contents);
			StringBufferInputStream is = new StringBufferInputStream(new_jsdai_properties_contents);

			//			StringReader sr = new StringReader(jsdai_properties_contents);
			jsdai_properties.create(is, false, null);
//			jsdai_properties.create(sr, false, null);

*/

//			ExpressNature.addNature(fProject, new SubProgressMonitor(monitor, 1));
//			ExpressNature.addNature(fProject, "net.jsdai.express_compiler.expressNature", null);

/*			
			
			IProjectDescription descrip =
				fProject.getDescription();
			
			String[] natures = descrip.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(
				natures,
				0,
				newNatures,
				0,
				natures.length);
			newNatures[natures.length] =
				"net.jsdai.express_compiler.expressNature";
			
//			String newNatures [] = new String [1];
//			newNatures[0] = "net.jsdai.express_compiler.expressNature";
			System.out.println("setting natures: " + newNatures[0] + " to " + descrip);
			descrip.setNatureIds(newNatures);
			System.out.println("done, natures set");
//			fProject.setDescription(descrip, null);
//			fProject.setDescription(descrip, 0, null);
			System.out.println("done, description set");
			
*/			

//	     IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());
//			 String working_location_str = working_location.toString();

//System.out.println("Working location: "  + working_location_str);
		
		
		
			
//		} catch (Exception e) {
//System.out.println("so what is wrong now?");
//			e.printStackTrace();
//		}
		
				
				
				

				 try {
				
				fProject.refreshLocal(IResource.DEPTH_INFINITE, null);
//				repository.refreshLocal(IResource.DEPTH_INFINITE, null);
			        } catch (Exception e) {
				ExpressCompilerPlugin.log(e);
//		System.out.println("Exception - Problems with refresh: " + e);
//						e.printStackTrace();
			        }

				
				
//				System.out.println("URL: " + plugin_url);
//				System.out.println("local URL: " + local_plugin_url);

//				System.out.println("external form URL: " + local_plugin_url.toExternalForm());
				
//				IFolder source_folder = Folder(source_path, (Workspace) null);
				
				/*
		 * end of experiments
		 */		
		
//		ExpressCompilerRepo.copy();
//		ExpressCompilerRepo.copy(resource);
	
//		ExpressCompilerRepo.copy(repository);


//		ExpressCompilerRepo.copy(repository, resource);
		
		
		return true;
	}


	public boolean performFinish_old() {
		IRunnableWithProgress projectCreationOperation =
			new WorkspaceModifyDelegatingOperation(
				getProjectCreationRunnable());
		try {
			getContainer().run(false, true, projectCreationOperation);
		} catch (Exception e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("perform Finish problems: " + e);
//		Throwable ue = e.getCause();
//		System.out.println("perform Finish problems, cause: " + ue);
	return false;
		}

		BasicNewProjectResourceWizard.updatePerspective(fConfigurationElement);
		selectAndReveal(fProject);

//		String containerName = fProjectMainPage.getProjectName();

//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		IResource resource = root.findMember(new Path(containerName));
		IResource resource = (IResource)fProject;


//        IProject project = resource.getProject();

		IFolder internal_src = resource.getProject().getFolder("temp");

		
		IFolder express_src = resource.getProject().getFolder("Express files");
		IFolder complex_lists = resource.getProject().getFolder("Complex entities");
		IFolder short_names = resource.getProject().getFolder("Short names");

		IFolder java_output = resource.getProject().getFolder("temp/java");
		IFolder class_compiled = resource.getProject().getFolder("temp/classes");
//		IFolder jars = resource.getProject().getFolder("temp/jars");
//		IFolder html_expressdoc = resource.getProject().getFolder("ExpressDoc html");
		IFolder repository = resource.getProject().getFolder("temp/ExpressCompilerRepo");

//		IFile jsdai_properties = resource.getProject().getFile("jsdai.properties");	
		IFolder jsdai_properties_dir = resource.getProject().getFolder("temp");	
//		public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException;
		try {

			express_src.create(true, true, null);
			complex_lists.create(true, true, null);
			short_names.create(true, true, null);


			String custom_repo_location = null;
//			File custom_repo_dir = null;
			
			if (fProjectTempPage.isDefaultTempLocation()) {
				internal_src.create(true, true, null);
				java_output.create(true, true, null);
				class_compiled.create(true, true, null);
				if (fProjectTempPage.isDefaultRepoLocation()) {
					repository.create(true, true, null);
					ExpressCompilerRepo.copy(repository, resource);
					ExpressCompilerUtils.createJsdaiProperties(jsdai_properties_dir.getLocation().toOSString(),
							repository.getLocation().toOSString());
				} else {
					custom_repo_location  = fProjectTempPage.getCustomRepoLocation() + File.separator + "ExpressCompilerRepo";
					ExpressCompilerRepo.copy(custom_repo_location);
					ExpressCompilerUtils.createJsdaiProperties(jsdai_properties_dir.getLocation().toOSString(), custom_repo_location);
				}
			} else {
//				String custom_temp_location = fProjectPage.getCustomTempLocation() + File.separator + "temp";
				String custom_temp_location = fProjectTempPage.getCustomTempLocation();
//				File custom_temp_dir = new File(custom_temp_location);
	  	//  custom_temp_dir.mkdirs();
//ha		  ExpressCompilerUtils.verifyPath(custom_temp_dir, false);
//	  	  String custom_java_location = custom_temp_location + File.separator + "java";
//				File custom_java_dir = new File(custom_java_location);
  	  	//custom_java_dir.mkdirs();
//ha	  	ExpressCompilerUtils.verifyPath(custom_java_dir, false);

//  	  	String custom_class_location = custom_temp_location + File.separator + "classes";
//				File custom_class_dir = new File(custom_class_location);
//  		  custom_class_dir.mkdirs();
//ha  	  	ExpressCompilerUtils.verifyPath(custom_class_dir, false);

  		  if (fProjectTempPage.isDefaultRepoLocation()) {
					custom_repo_location = custom_temp_location +  File.separator + "ExpressCompilerRepo";
					ExpressCompilerRepo.copy(custom_repo_location, true);
				} else {
					custom_repo_location  = fProjectTempPage.getCustomRepoLocation() + File.separator + "ExpressCompilerRepo";
					ExpressCompilerRepo.copy(custom_repo_location, false);
				}
				ExpressCompilerUtils.createJsdaiProperties(custom_temp_location, custom_repo_location);
			}





/*

//			jars.create(true, true, null);
//			html_expressdoc.create(true, true, null);


//		String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "temp" + File.separator + "ExpressCompilerRepo";
		String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";

//			String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";
//			String jsdai_properties_contents = 	"repositories=" + jsdai_properties.getParent().getLocation() + "\\ExpressCompilerRepo";

			int ind = 0;
			int ind0 = 0;
			String new_jsdai_properties_contents = "";
			for (;;) {
				ind = jsdai_properties_contents.indexOf(File.separator, ind0);
// System.out.println("ind: " + ind + ", ind0: " + ind0 + ", new: " + new_jsdai_properties_contents);
				if (ind >=0) {
					new_jsdai_properties_contents += jsdai_properties_contents.substring(ind0,ind + 1);
					new_jsdai_properties_contents += File.separator;
					ind0 = ind+1;
				} else {
					new_jsdai_properties_contents +=  jsdai_properties_contents.substring(ind0);
					break;
				}
			}
			
//				"repositories=E:\\eclipse11\\runtime-workbench-workspace\\RR1\\ExpressCompilerRepo";

//			StringBufferInputStream is = new StringBufferInputStream(jsdai_properties_contents);
			StringBufferInputStream is = new StringBufferInputStream(new_jsdai_properties_contents);

			//			StringReader sr = new StringReader(jsdai_properties_contents);
			jsdai_properties.create(is, false, null);
//			jsdai_properties.create(sr, false, null);

*/

//			ExpressNature.addNature(fProject, new SubProgressMonitor(monitor, 1));
//			ExpressNature.addNature(fProject, "net.jsdai.express_compiler.expressNature", null);

/*			
			
			IProjectDescription descrip =
				fProject.getDescription();
			
			String[] natures = descrip.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(
				natures,
				0,
				newNatures,
				0,
				natures.length);
			newNatures[natures.length] =
				"net.jsdai.express_compiler.expressNature";
			
//			String newNatures [] = new String [1];
//			newNatures[0] = "net.jsdai.express_compiler.expressNature";
			System.out.println("setting natures: " + newNatures[0] + " to " + descrip);
			descrip.setNatureIds(newNatures);
			System.out.println("done, natures set");
//			fProject.setDescription(descrip, null);
//			fProject.setDescription(descrip, 0, null);
			System.out.println("done, description set");
			
*/			

//		     IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());
	     IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getUniqueIdentifier());
			 String working_location_str = working_location.toString();

//System.out.println("Working location: "  + working_location_str);
			
		} catch (Exception e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("so what is wrong now?");
//			e.printStackTrace();
		}
		
				
				
				

				 try {
				
				repository.refreshLocal(IResource.DEPTH_INFINITE, null);
			        } catch (Exception e) {
				ExpressCompilerPlugin.log(e);
						e.printStackTrace();
			        }

				
				
//				System.out.println("URL: " + plugin_url);
//				System.out.println("local URL: " + local_plugin_url);

//				System.out.println("external form URL: " + local_plugin_url.toExternalForm());
				
//				IFolder source_folder = Folder(source_path, (Workspace) null);
				
				/*
		 * end of experiments
		 */		
		
//		ExpressCompilerRepo.copy();
//		ExpressCompilerRepo.copy(resource);
	
//		ExpressCompilerRepo.copy(repository);


//		ExpressCompilerRepo.copy(repository, resource);
		
		
		return true;
	}


	protected IRunnableWithProgress getProjectCreationRunnable() {
		return new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
				int remainingWorkUnits = 10;
				monitor.beginTask("", remainingWorkUnits);

				IWorkspace workspace = ExpressCompilerPlugin.getWorkspace();
				fProject = fProjectMainPage.getProjectHandle();

//		ExpressNature.addNature(fProject, new SubProgressMonitor(monitor, 1));
/*
		try {
			ExpressNature.addNature(fProject, "net.jsdai.express_compiler.expressNature", new SubProgressMonitor(monitor, 1));
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
System.out.println("Problems with adding nature");
			e1.printStackTrace();
		}
*/
//System.out.println("before description");
				IProjectDescription description =
					workspace.newProjectDescription(fProject.getName());
//System.out.println("new project description: " + description);
				IPath path = Platform.getLocation();
				IPath customPath = fProjectMainPage.getProjectLocationPath();
				if (!path.equals(customPath)) {
					path = customPath;
//System.out.println("<<<>>> path: " + path);
					if (!fProjectMainPage.useDefaultsForProject) {
						description.setLocation(path);
			  	}
			  }

				String[] natures = description.getNatureIds();
				String[] newNatures = new String[natures.length + 1];
				System.arraycopy(
					natures,
					0,
					newNatures,
					0,
					natures.length);
				newNatures[natures.length] =
					"net.jsdai.express_compiler.expressNature";
//				String newNatures [] = new String [1];
//				newNatures[0] = "net.jsdai.express_compiler.expressNature";
//System.out.println("before setNatureIds: " + newNatures);
				description.setNatureIds(newNatures);
//System.out.println("after setNatureIds");
				
				
				
				/* 
						let's put some useful information into the comment, just for now
						let's make it look as if it was meant to be this way  :)
				
						EXPRESS PROJECT
						P)(P
						T)(T
						R)(R
						
				
				*/
				String comment_p, comment_t, comment_r;

				String comment_x = "\n\t\tEXPRESS PROJECT\n";
		    if (fProjectMainPage.isDefaultProjectLocation()) 
					comment_p = "\t\tP)DEFAULT(P\n";
				else
					comment_p = "\t\tP)" + fProjectMainPage.getCustomProjectLocation() + "(P\n";
		    if (fProjectTempPage.isDefaultTempLocation()) 
		    	comment_t = "\t\tT)DEFAULT(T\n";
				else	
					comment_t = "\t\tT)" + fProjectTempPage.getCustomTempLocation() + "(T\n";
		    if (fProjectTempPage.isDefaultRepoLocation()) 
			    comment_r = "\t\tR)DEFAULT(R\n\t";
				else 
					comment_r = "\t\tR)" + fProjectTempPage.getCustomRepoLocation() + "(R\n";
				
				String project_comment = comment_x + comment_p + comment_t + comment_r;

				try {
					if (!fProject.exists()) {
//System.out.println("before create project: " + description);
						fProject.create(
							description,
							new SubProgressMonitor(monitor, 1));
						remainingWorkUnits--;
//System.out.println("after create project");
					}
					if (!fProject.isOpen()) {
						fProject.open(new SubProgressMonitor(monitor, 1));
						remainingWorkUnits--;
					}

					try {
						IProjectDescription descrip =
							fProject.getDescription();
						/*
						String[] natures = descrip.getNatureIds();
						String[] newNatures = new String[natures.length + 1];
						System.arraycopy(
							natures,
							0,
							newNatures,
							0,
							natures.length);
						newNatures[natures.length] =
							"net.jsdai.express_compiler.ExpressProject";
						*/
//						String newNatures [] = new String [1];
//						newNatures[0] = "net.jsdai.express_compiler.expressNature";
//						descrip.setNatureIds(newNatures);
//System.out.println("before set comment: " + descrip);

//						descrip.setComment(project_comment);
//System.out.println("after set comment: " + project_comment);

//--------------------------

IPath path2 = Platform.getLocation();
IPath customPath2 = fProjectMainPage.getProjectLocationPath();
if (!path.equals(customPath)) 
	path2 = customPath2;
//System.out.println("<<<>>> path: " + path);
	description.setLocation(path2);



//--------------------------------

/*

fDefaultExpressFileLocation   - true/false
expressFileLocation        - may be custom or the same as default
expressFileLocationDefault  - inside project subdirectory "Express Files"

fDefaultShortNameLocation  - true/false
shortNameLocation          - may be custom or the same as default
shortNameLocationDefault   - inside project subdirectory "Short Names"

fDefaultComplexEntityLocation - true/false
complexEntityLocation      - may be custom or the same as default
complexEntityLocationDefault  - inside project subdirectory "Complex Entities"

fTempLocation  - SYSTEM/ECLIPSE/PROJECT/CUSTOM    OS default / Eclipse workspace .metadata project-specific / temp inside project / custom
fDeleteOnExit  - true/false
tempLocation   - may be custom or the same as Eclipse or system
tempLocationEclipse - project-specific location in workspace/.metatada
tempLocationProject - temp inside project
tempLocationSystem  - OS default temp location, project specific unique subdirectory

fDefaultRepoLocation - true/false
repoLocation         
repoLocationDefault  - inside temp directory, subdirectory ExpressCompilerRepo - copy tempLocation, or store nothing, lookup when needed

*/


					// saving the location of the directory of express files as persistent property of the project
					IEclipsePreferences prefs = new ProjectScope(fProject).getNode(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID);
					String express_file_location;
					String default_express_file_location = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Express files";
					if (fProjectInputPage.isDefaultExpressFileLocation()) {
						express_file_location = default_express_file_location;
						prefs.put("fDefaultExpressFileLocation", "true");
					} else {
						express_file_location = fProjectInputPage.getCustomExpressFileLocation();
						prefs.put("fDefaultExpressFileLocation", "false" );
						prefs.put("expressFileLocation", express_file_location );
					}

					// saving the location of the short name directory as a persistent property of the project
					String short_name_location;
					String default_short_name_location = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Short names";
					if (fProjectInputPage.isDefaultShortNameLocation()) {
						short_name_location = default_short_name_location;
						prefs.put("fDefaultShortNameLocation", "true");
					} else {
						short_name_location = fProjectInputPage.getCustomShortNameLocation();
						prefs.put("fDefaultShortNameLocation", "false");
						prefs.put("shortNameLocation", short_name_location);
					}

					// saving the location of the complex entity directory as a persistent property of the project
					String complex_entity_location;
					String default_complex_entity_location = NewExpressProjectWizard.getMainPage().getProjectLocationPath() + File.separator + "Complex entities";
					if (fProjectInputPage.isDefaultComplexEntityLocation()) {
						complex_entity_location = default_complex_entity_location;
						prefs.put("fDefaultComplexEntityLocation", "true");
					} else {
						complex_entity_location = fProjectInputPage.getCustomComplexEntityLocation();
						prefs.put("fDefaultComplexEntityLocation", "false" );
						prefs.put("complexEntityLocation", complex_entity_location);
					}

					/*
							saving the settings for input: 
							
							default/list/flat directory/recursive directory
					
					*/
					int input_type = fProjectInputPage.getInputType();
					switch (input_type) {
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
					/*
							saving the settings for usage of exclusion list  
							
							default/yes/no
					
					*/
					int exclude_list_value = fProjectInputPage.getIfUseExcludeList();
					switch (exclude_list_value) {
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


// no need for create input/exclude to go to properties, they are needed right here only

/*

					int if_create_input_list = fProjectInputPage.getIfCreateInputList();
					switch (if_create_input_list) {
						case 0: // default
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateInput"), "default" );
							break;
						case 1: // yes - use
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateInput"), "yes" );
							break;
						case 2: // no - do not use
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateInput"), "no" );
							break;
						default:
							// internal error
							break;
					}

					int if_create_exclude_list = fProjectInputPage.getIfCreateExcludeList();
					switch (if_create_exclude_list) {
						case 0: // default
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateExclude"), "default" );
							break;
						case 1: // yes - use
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateExclude"), "yes" );
							break;
						case 2: // no - do not use
							fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fCreateExclude"), "no" );
							break;
						default:
							// internal error
							break;
					}	
			
*/					
					/*
						 default/yes/no
					*/
					int if_create_jar = fProjectPermanentPage.getIfCreateJar();
					switch (if_create_jar) {
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

					/*
							ok, what about stepmod/arm/mim
					*/


/*

		int fSpecifyMemory = 0;
		int fMemorySizeInitial = 0;
		int fMemorySizeMax = 0;
		String fMemorySizeInitialStr = "";
		String fMemorySizeMaxStr = "";
		int fUseStepmodSwitch = 0;
		int fUseArmSwitch = 0;
		int fUseMimSwitch = 0;
*/


//				if(false) {  // temp disabled
				//Memory setting are not used when external java processes are not started for express compilation etc.
				int if_memory_specified = fProjectOptionPage.getIfMemorySpecified();
				// those values are valid for case 1 only:
				prefs.put("fMemoryInitial",  fProjectOptionPage.getInitialMemorySizeString());
				prefs.put("fMemoryMax",  fProjectOptionPage.getMaxMemorySizeString());
				switch (if_memory_specified) {
					case 0: // default
						prefs.put("fMemorySpecify", "default");
							break;
					case 1: // yes
						prefs.put("fMemorySpecify", "yes");
							break;
					case 2: // no
						prefs.put("fMemorySpecify", "no");
							break;
					default: //error
							break;
				}
//				}


				int if_switch_separate_process = fProjectOptionPage.getIfSeparateProcess();
				switch (if_switch_separate_process) {
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

	    boolean if_advanced_enabled = fProjectOptionPage.getIfAdvancedEnabled();
	    if (if_advanced_enabled) {
    	  prefs.put("fEnableAdvanced", "yes");
  	  } else {
    		  prefs.put("fEnableAdvanced", "no");
    	}


				int if_switch_stepmod = fProjectOptionPage.getIfStempodSwitchUsed();
				switch (if_switch_stepmod) {
					case 0: // default
						prefs.put("fSwitchStepmod", "default" );
							break;
					case 1: // yes
						prefs.put("fSwitchStepmod", "yes" );
							break;
					case 2: // no
						prefs.put("fSwitchStepmod", "no" );
							break;
					default: // error
							break;
				}
	
				int if_switch_arm = fProjectOptionPage.getIfArmSwitchUsed();
				switch (if_switch_arm) {
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

				int if_switch_mim = fProjectOptionPage.getIfMimSwitchUsed();
				switch (if_switch_mim) {
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
	

				int if_enable_expressions = fProjectOptionPage.getIfExpressionsEnabled();
				switch (if_enable_expressions) {
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


					// saving the location of the temp directory as a persistent property of the project
					// String temp_location;
					
					// possible values: SYSTEM/ECLIPSE/PROJECT/CUSTOM (also DEFAULT = SYSTEM)
					String temp_location_type_str = fProjectTempPage.getTempLocationTypeStr();
					prefs.put("fTempLocation", temp_location_type_str);
					int temp_location_type = fProjectTempPage.getTempLocationType();

//				    IPath working_location = fProject.getWorkingLocation(ExpressCompilerPlugin.getDefault().getUniqueIdentifier());



					if(false) {
					String temp_location_str = null;
					switch (temp_location_type) {
						case 0: // DEFAULT
							// can not be saved here, because it may be changed at any time in global preferences
							// however, to avoid problems, better save here as system, 
							// the using code is responsible to check the type and get the location from global preferences instead
							// but if it does not do that (for example, not yet updated) - nothing bad happens, system temp is used

							/*
									in global preferences, we have only a flag - to use system or eclipse, but the specific path is known only here,
									and both versions are saved for future use (.tempLocationSystem, .tempLocationEclipse, .tempLocationProject
									it can be saved correctly at this time, but if the global preference is changed, a different path has te be used,
									so saving it here has only one purpose - to make it compatible with not-yet-updated code elsewhere
							*/
							String flagGlobalDefaultTempLocationType  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressProjectPreferences.TEMP_TYPE);
							if (flagGlobalDefaultTempLocationType != null) {
								if (flagGlobalDefaultTempLocationType.equalsIgnoreCase("SYSTEM")) {
//									prefs.put("fDeleteOnExit", "true");
								} else
								if (flagGlobalDefaultTempLocationType.equalsIgnoreCase("ECLIPSE")) {
								} else
								if (flagGlobalDefaultTempLocationType.equalsIgnoreCase("PROJECT")) {
								} else {
									// should not occur, but use system temp in such a case
//									prefs.put("fDeleteOnExit", "true");
								}
							} else {
								// say, system temp, as it used to be default anyway
//								prefs.put("fDeleteOnExit", "true");
							}
							
							// this is used further to put repository stuff 
							// (although it may not be needed at this stage - repository is deleted and copied again when running the compiler, anyway
							// but we could play it safe and try to get the global location first, if no success - then the system temp
							break;
						case 1: // SYSTEM
							// can be calculated when needed
							prefs.put("fDeleteOnExit", "true");
							break;
						case 2: // ECLIPSE
							// can be calculated when needed
							break;
						case 3: //PROJECT
							// can be calculated when needed
							break;
						case 4: // CUSTOM		
							String custom_temp_location_str = fProjectTempPage.getCustomTempLocation();
							prefs.put("tempLocation", custom_temp_location_str);
							break;
						default:
							//  should not happen, if does, assume SYSTEM or something - can be calculated when needed
							break; 
					}
					}

					// saving the location of the repo directory as a persistent property of the project
					// fDefaultRepoLocation - true/false
					// repoLocation - if default, can be calculated - inside temp, subdirectory ExpressCompilerRepo         
          // repoLocationDefault  - inside temp directory, subdirectory ExpressCompilerRepo - copy tempLocation, or store nothing, lookup when needed

//					String default_repo_location = temp_location_str;
//					if (default_repo_location.endsWith("\\") || default_repo_location.endsWith("/")) {
//					 default_repo_location += "ExpressCompilerRepo";
//					} else {
//					 default_repo_location += File.separator + "ExpressCompilerRepo";
//					}
//					String delete_repo_on_exit_str = "true";
////					prefs.put("repoLocationDefault", default_repo_location);
//					if (fProjectTempPage.isDefaultRepoLocation()) {
//						prefs.put("repoLocation", default_repo_location);
//						prefs.put("fDefaultRepoLocation", "true");
//					} else {
//						String custom_repo_location  = fProjectTempPage.getCustomRepoLocation();
//						if (custom_repo_location.endsWith("\\") || custom_repo_location.endsWith("/")) {
//							custom_repo_location += "ExpressCompilerRepo";
//						} else {
//						 	custom_repo_location += File.separator + "ExpressCompilerRepo";
//						}
//						prefs.put("repoLocation", custom_repo_location);
//						prefs.put("fDefaultRepoLocation", "false");
//						delete_repo_on_exit_str = "false";  // actually, read the checkbox value - TODO
//					}
//					prefs.put("fDeleteRepoOnExit", delete_repo_on_exit_str);

					
					/*
					String default_temp_location = NewExpressProjectWizard.getTempPage().getProjectLocationPath() + File.separator + "Complex entities";
					if (fProjectPermanentPage.isDefaultComplexEntityLocation()) {
						complex_entity_location = default_complex_entity_location;
						fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fDefaultComplexEntityLocation"), "true" );
					} else {
					  complex_entity_location = fProjectPermanentPage.getCustomComplexEntityLocation();
    				  fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".fDefaultComplexEntityLocation"), "false" );
					}
					fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".complexEntityLocation"), complex_entity_location );
					fProject.setPersistentProperty(new QualifiedName(ExpressCompilerPlugin.PLUGIN_ID,".complexEntityLocationDefault"), default_complex_entity_location );
					*/
				    prefs.flush();

//System.out.println("before set description 2: " + descrip);
					

// new approach does not require using those comments inside description
 fProject.setDescription(descrip, null);
//System.out.println("after set description 2: " + descrip);
					} catch (CoreException e) {
				ExpressCompilerPlugin.log(e);
//						System.out.println("hey, cannot set nature IDs: " + e + ", trace: ");
//						e.printStackTrace();
					}

					if (!fProject.hasNature("ExpressProject")) {

					}

			    } catch (BackingStoreException e) {
					ExpressCompilerPlugin.log(e);
//					System.out.println("hey, core exception while handling description: " + e + ", trace: ");
//					e.printStackTrace();
					throw new InvocationTargetException(e);
				} catch (CoreException e) {
				ExpressCompilerPlugin.log(e);
//					System.out.println("hey, core exception while handling description: " + e + ", trace: ");
//					e.printStackTrace();
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
	}



	public void addPages() {
		super.addPages();
//		fProjectPage = new WizardNewProjectCreationPage("NewProjectExpress");
		fProjectMainPage = new NewExpressProjectWizardMainPage(ExpressCompilerPlugin.getResourceString(PREFIX + ".main"));
		fgProjectMainPage = fProjectMainPage;
		fProjectMainPage.setTitle("Create new Express project");
		fProjectMainPage.setDescription("provide the name and the location for the project");
		addPage(fProjectMainPage);

		

		fProjectInputPage = new NewExpressProjectWizardInputPage(ExpressCompilerPlugin.getResourceString(PREFIX + ".input"));
		fProjectInputPage.setTitle("Input settings");
		fProjectInputPage.setDescription("specify the input for the Express compiler");
		addPage(fProjectInputPage);

		fProjectPermanentPage = new NewExpressProjectWizardPermanentPage(ExpressCompilerPlugin.getResourceString(PREFIX + ".permanent"));
		fProjectPermanentPage.setTitle("Output settings");
		fProjectPermanentPage.setDescription("specify the output for the Express compiler");
		addPage(fProjectPermanentPage);


		fProjectOptionPage = new NewExpressProjectWizardOptionPage(ExpressCompilerPlugin.getResourceString(PREFIX + ".options"));
		fProjectOptionPage.setTitle("Express compiler settings");
		fProjectOptionPage.setDescription("specify various Express compiler settings");
		addPage(fProjectOptionPage);

		fProjectTempPage = new NewExpressProjectWizardTempPage(ExpressCompilerPlugin.getResourceString(PREFIX + ".temp"));
		fProjectTempPage.setTitle("Temporary files");
		fProjectTempPage.setDescription("directories for temporary project files");
		addPage(fProjectTempPage);
	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
/*
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		

	}
*/
	public void setInitializationData(
		IConfigurationElement config,
		String propertyName,
		Object data)
		throws CoreException {
		fConfigurationElement = config;
	}

	protected IStatus isValidName(String name) {
		return new Status(IStatus.OK, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID, 0, "", null); //$NON-NLS-1$
	}

	/**
	 * Method isValidLocation.
	 * @param projectFieldContents
	 * @return IStatus
	 */
	protected IStatus isValidLocation(String projectFieldContents) {
		return new Status(IStatus.OK, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID, 0, "", null); //$NON-NLS-1$
	}

	public static NewExpressProjectWizardMainPage getMainPage() {
		return fgProjectMainPage;
	}

	

}

