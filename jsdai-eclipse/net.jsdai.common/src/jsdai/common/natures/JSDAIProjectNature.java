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

package jsdai.common.natures;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jsdai.common.CommonPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class JSDAIProjectNature implements IProjectNature {

	public static final String JSDAI_NATURE_ID = CommonPlugin.PLUGIN_ID + ".JSDAIProjectNature"; //$NON-NLS-1$

	private IProject fProject;

	public JSDAIProjectNature() {
	}

	public void configure() throws CoreException {
	}

	public void deconfigure() throws CoreException {
	}

	public IProject getProject() {
		return fProject;
	}

	public void setProject(IProject project) {
		fProject = project;
	}



	public static void addJSDAINature(IProject project, IProgressMonitor mon) throws CoreException {
		addNature(project, JSDAI_NATURE_ID, mon);
	}

	public static void removeJSDAINature(IProject project, IProgressMonitor mon) throws CoreException {
		removeNature(project, JSDAI_NATURE_ID, mon);
	}
	

	/**
	 * Utility method for adding a nature to a project.
	 * 
	 * @param proj
	 *            the project to add the nature
	 * @param natureId
	 *            the id of the nature to assign to the project
	 * @param monitor
	 *            a progress monitor to indicate the duration of the operation,
	 *            or <code>null</code> if progress reporting is not required.
	 *  
	 */
	public static void addNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		for (int i = 0; i < prevNatures.length; i++) {
			if (natureId.equals(prevNatures[i]))
				return;
		}
		String[] newNatures = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = natureId;
		description.setNatureIds(newNatures);
		project.setDescription(description, monitor);
	}

	/**
	 * Utility method for removing a project nature from a project.
	 * 
	 * @param proj
	 *            the project to remove the nature from
	 * @param natureId
	 *            the nature id to remove
	 * @param monitor
	 *            a progress monitor to indicate the duration of the operation,
	 *            or <code>null</code> if progress reporting is not required.
	 */
	public static void removeNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		List newNatures = new ArrayList(Arrays.asList(prevNatures));
		newNatures.remove(natureId);
		description.setNatureIds((String[]) newNatures.toArray(new String[newNatures.size()]));
		project.setDescription(description, monitor);
	}

	// handling jsdai nature
	
	/**
	 * Add the JSDAI nature to a project
	 * @param aProject The project to add it to
	 * @param aMonitor A progess monitor
	 */
	public static void addNature(IProject aProject, IProgressMonitor aMonitor) {
		if (aProject != null) {
			try {
				// if the project is a java project and does not have JSDAI enhancement
				// add JSDAI nature (perhaps will be useful in the future?),
				// but more importantly - add classpath to jsdai_runtime.jar, and create jsdai.properties file and REPOSITORIES folder
				
				if (aProject.hasNature(JavaCore.NATURE_ID)) {  // if java project
					if (!aProject.hasNature(JSDAI_NATURE_ID)) {  // and if it is not yet made JSDAI project
			    	IProjectDescription desc = aProject.getDescription();
			    	List natures = new ArrayList(Arrays.asList(desc.getNatureIds()));
			    	//natures.add(0, "net.jsdai.express_compiler.expressNature");
			    	natures.add(0, JSDAI_NATURE_ID);
						desc.setNatureIds((String[])natures.toArray(new String[natures.size()]));
						aProject.setDescription(desc, aMonitor);
						// set up the classpath variable for JSDAI_RUNTIME - the jsdai_runtime.jar plugin
						IJavaProject javaProject = JavaCore.create(aProject);
						List rawClasspath = new ArrayList(Arrays.asList(javaProject.getRawClasspath()));
						boolean hasJsdaiRuntimeJar = false;
						for (Iterator i = rawClasspath.iterator(); i.hasNext();) {
							IClasspathEntry entry = (IClasspathEntry) i.next();
							if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
								String segment0 = entry.getPath().segment(0);
								if (CommonPlugin.JSDAI_PUBLIC_PATHS.equals(segment0)) {
									hasJsdaiRuntimeJar = true;
									break;
								}
							}
						}
						if (!hasJsdaiRuntimeJar)
							rawClasspath.add(JavaCore.newVariableEntry(
									new Path(CommonPlugin.JSDAI_PUBLIC_PATHS + "/jsdai_runtime.jar"),
									null, null));
/* XXX							rawClasspath.add(JavaCore.newVariableEntry(
									new Path(RuntimePlugin.JSDAI_PUBLIC_PATHS + "/SExtended_dictionary_schema.jar"),
									null, null));*/

						
						if (!hasJsdaiRuntimeJar)
							javaProject.setRawClasspath((IClasspathEntry[]) rawClasspath.toArray(new IClasspathEntry[rawClasspath.size()]),null);
					
						// and perhaps we can create REPOSITORIES folder and jsdai.properties file pointing to it
						IFolder repo_folder = aProject.getFolder(".repositories.tmp");
						if (!repo_folder.exists()) {
							repo_folder.create(true, true, null);
						}
						IFile jsdai_properties = aProject.getFile("jsdai.properties");	
						if (!jsdai_properties.getLocation().toFile().exists()) {
							String jsdai_properties_contents = 	"repositories = .repositories.tmp";

							ByteArrayInputStream is = new ByteArrayInputStream(jsdai_properties_contents.getBytes());
							jsdai_properties.create(is, false, null);
						}
						
						aProject.refreshLocal(IResource.DEPTH_ONE, null);
						
					}
				}
			} catch(CoreException e) {
				CommonPlugin.log(e);
			}
		}
	}
	
	/**
	 * Remove the JSDAI nature from a project
	 * @param aProject The project to remove the nature from
	 * @param aMonitor A progress monitor
	 */
	public static void removeNature(IProject aProject, IProgressMonitor aMonitor) {
		if (aProject != null) {
			try {
				if (aProject.hasNature(JSDAI_NATURE_ID)) {
			    	// it must be a java project, but just in case, remove from any project that might have JSDAI nature
			    	IProjectDescription desc = aProject.getDescription();
			    	List natures = new ArrayList(Arrays.asList(desc.getNatureIds()));
			    	natures.remove(JSDAI_NATURE_ID);
						desc.setNatureIds((String[])natures.toArray(new String[natures.size()]));
						aProject.setDescription(desc, aMonitor);
					
					// if it's a Java Project, remove the JSDAI_RUNTIME class path variable
					if (aProject.hasNature(JavaCore.NATURE_ID)) {
						IJavaProject javaProject = JavaCore.create(aProject);
						List rawClasspath = new ArrayList(Arrays.asList(javaProject.getRawClasspath()));
						boolean hadJsdaiRuntimeJar = false;
						for (Iterator i = rawClasspath.iterator(); i.hasNext();) {
							IClasspathEntry entry = (IClasspathEntry) i.next();
							if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
								String segment0 = entry.getPath().segment(0);
								if (CommonPlugin.JSDAI_PUBLIC_PATHS.equals(segment0)) {
									i.remove();
									hadJsdaiRuntimeJar = true;
								}
							}
						}
						if (hadJsdaiRuntimeJar) {
							javaProject.setRawClasspath((IClasspathEntry[]) rawClasspath.toArray(new IClasspathEntry[rawClasspath.size()]),null);
						}
					}
					IFolder repo_folder = aProject.getFolder(".repositories.tmp");
					IFile jsdai_properties = aProject.getFile("jsdai.properties");	
					

					if (repo_folder.exists()) {
						repo_folder.delete(true, aMonitor);
					}
					if (jsdai_properties.exists()) {
						jsdai_properties.delete(true, aMonitor);
					}
					aProject.refreshLocal(IResource.DEPTH_ONE, null);

				}
			} catch(CoreException e) {
				CommonPlugin.log(e);
			}
		} 
	}

	/**
	 * Returns true if given project has a JSDAI project nature.
	 * @param aProject The project to check
	 * @return true if it is a JSDAI project; false - if not
	 * @see IProject#hasNature(String)
	 */
	public static boolean hasNature(IProject aProject) {
	    boolean hasNature;
		try {
			hasNature = aProject.hasNature(JSDAI_NATURE_ID);
		} catch (CoreException e) {
			CommonPlugin.log(e);
			hasNature = false;
		}
		return hasNature;
	}
}
