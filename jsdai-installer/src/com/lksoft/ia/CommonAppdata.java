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
import com.zerog.ia.api.pub.ResourceAccess;
import com.zerog.ia.api.pub.UninstallerProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created: Tue Dec 30 10:34:18 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class CommonAppdata extends CustomCodeAction {

	private static final String COMMON_APPDATA_NATIVE = "/com_lksoft_ia_CommonAppdata.dll";
	private static volatile boolean nativeLoaded = false;

	public CommonAppdata() {
		
	}

	
	// Implementation of com.zerog.ia.api.pub.CustomCodeAction

	/**
	 * Describe <code>getInstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getInstallStatusMessage() {
		return "Getting Common Application Data...";
	}

	/**
	 * Describe <code>getUninstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getUninstallStatusMessage() {
		return "Getting Common Application Data...";
	}

	/**
	 * Describe <code>install</code> method here.
	 *
	 * @param installerProxy an <code>InstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void install(InstallerProxy ip) throws InstallException {
		try {
			loadNative(ip);
			ip.setVariable("$COMMON_APPDATA_DIR$", getCommonAppdataDir());
		} catch (IOException e) {
			e.printStackTrace();
			throw (InstallException)new FatalInstallException("CommonAppdata.install").initCause(e);
		}
	}


	/**
	 * Describe <code>uninstall</code> method here.
	 *
	 * @param uninstallerProxy an <code>UninstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void uninstall(UninstallerProxy up) throws InstallException {
		try {
			loadNative(up);
			up.setVariable("$COMMON_APPDATA_DIR$", getCommonAppdataDir());
		} catch (IOException e) {
			e.printStackTrace();
			throw (InstallException)new FatalInstallException("CommonAppdata.uninstall").initCause(e);
		}
	}

	private static void loadNative(ResourceAccess ra) throws IOException {
		if(!nativeLoaded) {
			synchronized (CommonAppdata.class) {
				if(!nativeLoaded) {
					InputStream inStrem = CommonAppdata.class.getResourceAsStream(COMMON_APPDATA_NATIVE);
					File nativeLibFile = new File(ra.getTempDirectory(), COMMON_APPDATA_NATIVE);
					FileOutputStream outStrem = new FileOutputStream(nativeLibFile);
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
					
					System.load(nativeLibFile.getAbsolutePath());
					nativeLoaded = true;
				}
			}
		}
	}

	private static native String getCommonAppdataDir(); 

}
