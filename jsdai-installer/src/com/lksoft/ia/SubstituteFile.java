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
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.NonfatalInstallException;
import com.zerog.ia.api.pub.UninstallerProxy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * Created: Tue Dec 30 10:34:18 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SubstituteFile extends CustomCodeAction {

	public SubstituteFile() {
		
	}

	
	// Implementation of com.zerog.ia.api.pub.CustomCodeAction

	/**
	 * Describe <code>getInstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getInstallStatusMessage() {
		return "Substituting file...";
	}

	/**
	 * Describe <code>getUninstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getUninstallStatusMessage() {
		return "";
	}

	/**
	 * Describe <code>install</code> method here.
	 *
	 * @param installerProxy an <code>InstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void install(InstallerProxy ip) throws InstallException {
		try {
			String substituteFile = ip.substitute("$SUBSTITUTE_FILE$");
			String substituteFileCharset = ip.substitute("$SUBSTITUTE_FILE_CHARSET$");
			Reader substituteFileReader = 
				new InputStreamReader(new FileInputStream(substituteFile), substituteFileCharset);
			StringWriter bufferWriter = new StringWriter();
			char[] buffer = new char[8 * 1024];
			int count = 0;
			do {
			    bufferWriter.write(buffer, 0, count);
			    count = substituteFileReader.read(buffer, 0, buffer.length);
			} while (count != -1);
			substituteFileReader.close();
			bufferWriter.close();
			String substituteFileString = bufferWriter.toString();
			substituteFileString = ip.substitute(substituteFileString);
			Writer substituteFileWriter = 
				new OutputStreamWriter(new FileOutputStream(substituteFile), substituteFileCharset);
			StringReader bufferReader = new StringReader(substituteFileString);
			count = 0;
			do {
			    substituteFileWriter.write(buffer, 0, count);
			    count = bufferReader.read(buffer, 0, buffer.length);
			} while (count != -1);
			substituteFileWriter.close();
			bufferReader.close();
		} catch(IOException e) {
			e.printStackTrace(System.err);
			throw new NonfatalInstallException(e.getMessage());
		}
		return;
	}

	/**
	 * Describe <code>uninstall</code> method here.
	 *
	 * @param uninstallerProxy an <code>UninstallerProxy</code> value
	 * @exception InstallException if an error occurs
	 */
	public void uninstall(UninstallerProxy up) throws InstallException {
		
	}
	
}
