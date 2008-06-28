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

package jsdai.express_compiler.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.osgi.service.environment.Constants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.runtime.RuntimePlugin;

public class ExpressCompilerRepo {

	protected static WizardNewProjectCreationPage projectPage;

	
	static public String getPluginDirectory() {
		ExpressCompilerPlugin the_plugin = ExpressCompilerPlugin.getDefault();
		String source_path = "";	
	
		URL plugin_url = the_plugin.getBundle().getEntry("/");
		URL local_plugin_url = null;
		try {
			local_plugin_url = Platform.asLocalURL(plugin_url);
	
			if (Platform.getOS().equals(Constants.OS_WIN32)) {
			    source_path = local_plugin_url.toExternalForm().substring(6);
			} else {
				source_path = local_plugin_url.getFile();
			}
		} catch (IOException e1) {
				ExpressCompilerPlugin.log(e1);
			e1.printStackTrace();
		}
		return source_path;
	}
	
	static public void copy(IFolder dest_folder, IResource resource) {

			String source_path = RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.ext_dict_lib"), "ExpressCompilerRepo.sdai");
			File source_file = new File(source_path);
//			String destination_path = resource.getProject().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";
			String destination_path = dest_folder.getLocation().toOSString();
	
	
			File destination_dir = new File(destination_path);

//			System.out.println("Source: " + source_path);				
//			System.out.println("Destination: " + destination_path);				
	
			copyFileToDir(source_file, destination_dir);
	
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e) {
				ExpressCompilerPlugin.log(e);
			e.printStackTrace();
		}
	
		
		}

	static public void copy(String  destination_str, boolean to_delete) {

			String source_path = RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.ext_dict_lib"), "ExpressCompilerRepo.sdai");
			File source_file = new File(source_path);
//			String destination_path = resource.getProject().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";
			String destination_path = destination_str;
	
	
			File destination_file = new File(destination_path);
			
//			System.out.println("Source: " + source_path);				
//			System.out.println("Destination: " + destination_path);				
	
			copyFileToDir(source_file, destination_file);
			if (to_delete) {
				destination_file.deleteOnExit();
			}
	
//		try {
//			resource.refreshLocal(IResource.DEPTH_INFINITE, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
		
		}
	
	

	static public void copy(String  destination_str) {

			String source_path = RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.ext_dict_lib"), "ExpressCompilerRepo.sdai");
			File source_file = new File(source_path);

			//			String destination_path = resource.getProject().getLocation().toOSString() + File.separator + "ExpressCompilerRepo";
			String destination_path = destination_str;
	
	
			File destination_file = new File(destination_path);

//			System.out.println("Source: " + source_path);				
//			System.out.println("Destination: " + destination_path);				
	
			copyFileToDir(source_file, destination_file);
	
//		try {
//			resource.refreshLocal(IResource.DEPTH_INFINITE, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
		
		}


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        if (!dir.getName().equals("ExpressCompilerRepo")) {
        	return dir.delete();
        } else {
        	return true;
        }
     }		



// Copies all files under srcDir to dstDir.
// If dstDir does not exist, it will be created.
	static void copyDirectory(File srcDir, File dstDir) {
    	if (srcDir.isDirectory()) {
    		if (!dstDir.exists()) {
    			dstDir.mkdirs();
    		}

    		String[] children = srcDir.list();
    		for (int i=0; i<children.length; i++) {
    			copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
    		}
    	} else {
            	copyFile(srcDir, dstDir);
    	}
	}	

	public static void copyFileToDir(File in, File outDir) {
		outDir.mkdirs();
		copyFile(in, new File(outDir, in.getName()));
	}
	
	public static void copyFile(File in, File out) {

		try {
			FileInputStream inStrem = new FileInputStream(in);
			FileOutputStream outStrem = new FileOutputStream(out);
			try {
				byte[] buffer = new byte[8 * 1024];
				int count = 0;
				do {
					outStrem.write(buffer, 0, count);
					count = inStrem.read(buffer, 0, buffer.length);
				} while (count != -1);
			} finally {
				inStrem.close();
				outStrem.close();
			}
		} catch (IOException e) {
			ExpressCompilerPlugin.log(e);
			e.printStackTrace();
		}
	}



  
  

} // class
