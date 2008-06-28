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

package com.lksoft.ia;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.FatalInstallException;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * Created: Tue Dec 30 10:34:18 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CopyDeleteFiles extends CustomCodeAction {

	public CopyDeleteFiles() {
		
	}

	
	// Implementation of com.zerog.ia.api.pub.CustomCodeAction

	/**
	 * Describe <code>getInstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getInstallStatusMessage() {
		return "Copying files...";
	}

	/**
	 * Describe <code>getUninstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getUninstallStatusMessage() {
		return "Deleting files...";
	}

	/**
	 * Describe <code>install</code> method here.
	 *
	 * @param installerProxy an <code>InstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void install(InstallerProxy ip) throws InstallException {
		try {
			String copyFrom = ip.substitute("$COPY_FILES_FROM$");
			String copyTo = ip.substitute("$COPY_FILES_TO$");
			System.err.println("$COPY_FILES_FROM$=" + copyFrom + " $COPY_FILES_TO$=" + copyTo);
			if(copyFrom.length() != 0 && copyTo.length() != 0) {
				File copyFromFile = new File(copyFrom);
				File copyToFile = new File(copyTo, copyFromFile.getName());
				copyFileRecursive(copyFromFile, copyToFile);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			throw (InstallException)new FatalInstallException("Copying files").initCause(e);
		}
	}


	/**
	 * Describe <code>uninstall</code> method here.
	 *
	 * @param uninstallerProxy an <code>UninstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void uninstall(UninstallerProxy up) throws InstallException {
		String deleteFiles = up.substitute("$DELETE_FILES$");
		System.err.println("$DELETE_FILES$=" + deleteFiles);
		if(deleteFiles.length() != 0) {
			File deleteFilesFile = new File(deleteFiles);
			deleteFileRecursive(deleteFilesFile);
		}
	}

	static void copyFileRecursive(File srcDir, File dstDir) throws IOException {
    	if (srcDir.isDirectory()) {
			dstDir.mkdirs();

    		String[] children = srcDir.list();
    		for (int i=0; i<children.length; i++) {
    			copyFileRecursive(new File(srcDir, children[i]), new File(dstDir, children[i]));
    		}
    	} else {
            	copyFile(srcDir, dstDir);
    	}
	}	

	public static void copyFile(File in, File out) throws IOException {
		out.getParentFile().mkdirs();

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
	}

    public static boolean deleteFileRecursive(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFileRecursive(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
    	return dir.delete();
     }		

}
