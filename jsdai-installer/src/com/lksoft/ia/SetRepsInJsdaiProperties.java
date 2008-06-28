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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 *
 * Created: Tue Dec 30 10:34:18 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SetRepsInJsdaiProperties extends CustomCodeAction {

	public SetRepsInJsdaiProperties() {
		
	}

	
	// Implementation of com.zerog.ia.api.pub.CustomCodeAction

	/**
	 * Describe <code>getInstallStatusMessage</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getInstallStatusMessage() {
		return "Setting repositories in jsdai.properties file...";
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
			String jsdaiPropertiesPath = 
				ip.substitute("$JSDAI_PROPERTIES$");
			FileReader jsdaiPropertiesReader = new FileReader(jsdaiPropertiesPath);
			StringWriter bufferWriter = new StringWriter();
			char[] buffer = new char[8 * 1024];
			int count = 0;
			do {
			    bufferWriter.write(buffer, 0, count);
			    count = jsdaiPropertiesReader.read(buffer, 0, buffer.length);
			} while (count != -1);
			jsdaiPropertiesReader.close();
			bufferWriter.close();
			String jsdaiPropertiesString = bufferWriter.toString();
			String jsdaiRepositoryDir = replaceAll(ip.substitute("$JSDAI_REPOSITORY_DIR$"), "\\", "\\\\");
			ip.setVariable("JSDAI_REPOSITORY_DIR_BACKSLASHED", jsdaiRepositoryDir);
			jsdaiPropertiesString = ip.substitute(jsdaiPropertiesString);
			FileWriter jsdaiPropertiesWriter = new FileWriter(jsdaiPropertiesPath);
			StringReader bufferReader = new StringReader(jsdaiPropertiesString);
			count = 0;
			do {
			    jsdaiPropertiesWriter.write(buffer, 0, count);
			    count = bufferReader.read(buffer, 0, buffer.length);
			} while (count != -1);
			jsdaiPropertiesWriter.close();
			bufferReader.close();
		} catch(IOException e) {
			e.printStackTrace(System.err);
			throw new FatalInstallException(e.getMessage());
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

	static private String replaceAll(String text, String original, String replacement) {
		int curr_pos = 0;
		int find_pos = 0;
		StringBuffer sb = new StringBuffer();
  	 
		while (find_pos >= 0) {
			find_pos = text.indexOf( original, curr_pos);
			if (find_pos >= 0) {
				sb.append( text.substring( curr_pos, find_pos)).append( replacement);
				curr_pos = find_pos + original.length();
			}
			else {
				sb.append( text.substring( curr_pos));
			}
		}
		return sb.toString();
	}
	
} // SetRepsInJsdaiProperties
