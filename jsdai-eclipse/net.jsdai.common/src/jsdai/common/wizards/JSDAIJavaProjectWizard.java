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

package jsdai.common.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jsdai.common.CommonPlugin;
import jsdai.common.natures.JSDAIProjectNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Mantas Balnys
 *
 */
public class JSDAIJavaProjectWizard extends JavaProjectWizard {

	/**
	 * 
	 */
	public JSDAIJavaProjectWizard() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		boolean ok = super.performFinish();
		if (ok) {
			IWizardPage[] pages = getPages();
			if (pages != null && pages.length >= 2 && pages[1] instanceof JavaCapabilityConfigurationPage) {
				IJavaProject java_project = ((JavaCapabilityConfigurationPage)pages[1]).getJavaProject();
				IPath path = java_project.getProject().getLocation();
//				final IPath runtime_cp = path.append("jsdai_runtime.jar");
				FileOutputStream fout = null;
				try {
					File prop = path.append("jsdai.properties").toFile();
					fout = new FileOutputStream(prop);
/*					String repoTemp = RuntimePlugin.getDefault().getStateLocation().append("sdairepos").toOSString();
					repoTemp = repoTemp.replaceAll(File.separator, "\\\\");
					File repoDir = new File(repoTemp);
System.err.println(repoDir);					
System.err.println(repoDir.getAbsolutePath());					
System.err.println(repoDir.getCanonicalPath());					
System.err.println(repoDir.getName());					
System.err.println(repoDir.getParent());					
System.err.println(repoDir.getPath());					
System.err.println("repo temp=" + repoTemp);					
//					fout.write(("repositories = " + repoTemp).getBytes());*/
					fout.write(("repositories = .repositories.tmp").getBytes());
					fout.close();
					
					
/* XXX 1					
					URL url = RuntimePlugin.getDefault().getBundle().getEntry("jsdai_runtime.jar");
					try {
						url = Platform.asLocalURL(url);
					} catch (IOException iox) {
						RuntimePlugin.log(iox);
					}
					String runtime_file = url.getFile();
					FileInputStream in = new FileInputStream(runtime_file);
					FileOutputStream out = new FileOutputStream(runtime_cp.toFile());
					byte[] copyBuffer = new byte[65536];
					long bytesCopied = 0;
					int read = -1;
					while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
						out.write(copyBuffer, 0, read);
						bytesCopied += read;
					}
					in.close();
					out.close();					

/*					
					File cp = path.append(".classpath").toFile();
					File cpbak = path.append(".classpath.bak").toFile();
					BufferedReader br = new BufferedReader(new FileReader(cp));
					BufferedWriter bw = new BufferedWriter(new FileWriter(cpbak));
					for (int i = 0; i < 2; i++) 
						bw.write(br.readLine() + "\n");
					bw.write("\t<classpathentry kind=\"lib\" path=\"jsdai_runtime.jar\"/>\n");
					String line = br.readLine();
					while (line != null) {
						bw.write(line + "\n");
						line = br.readLine();
					}
					br.close();
					bw.close();
					cp.delete();
					cpbak.renameTo(cp);*/
				} catch (IOException iox) {
					CommonPlugin.log(iox);
					if (fout != null) try {
						fout.close();
					} catch (IOException iox2) {
						CommonPlugin.log(iox2);
					}
				}
				
				
				try {
					IClasspathEntry cpe = JavaCore.newVariableEntry(new Path(CommonPlugin.JSDAI_PUBLIC_PATHS + "/jsdai_runtime.jar"), null, null);
					IClasspathEntry[] cp = java_project.getRawClasspath();
					IClasspathEntry[] cp2 = new IClasspathEntry[cp.length + 1];
					System.arraycopy(cp, 0, cp2, 0, cp.length);
					cp2[cp2.length - 1] = cpe;
					java_project.setRawClasspath(cp2, null);
					java_project.save(null, true);
				} catch (JavaModelException jme) {
					CommonPlugin.log(jme);
				}
				
				IProject project = java_project.getProject();

				// upgrades nature to JSDAI
		    	JSDAIProjectNature.addNature(project, null);
/*
				try {
					String natureId = JSDAIProjectNature.JSDAI_NATURE_ID;
					IProjectDescription description = project.getDescription();
					String[] prevNatures = description.getNatureIds();
					String[] newNatures = new String[prevNatures.length + 1];
					System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
					newNatures[prevNatures.length] = natureId;
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
				} catch (CoreException ce) {
					RuntimePlugin.log(ce);
				}*/
				
				
				try {
					project.refreshLocal(IProject.DEPTH_INFINITE, null);
				} catch (CoreException ce) {
					CommonPlugin.log(ce);
				}
		 		selectAndReveal(project);
			}
		}
		return ok;
	}

}
