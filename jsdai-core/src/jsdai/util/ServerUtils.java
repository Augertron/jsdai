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

package jsdai.util;

/**
* This class contains some utility functions that help to do some common operations with JSDAI-Server.
*/
public class ServerUtils {
	static String user = "", passwd = "", address = "", port = "";
	public static String encodeConString(String user, String passwd, String address, String port) {
		String con = "";
		con += "//"+user+":"+passwd+"@"+address;
		if (!port.equals("")) {
			con += ":"+port;
		}
		return  con;
	}

	public static void decodeConString(String connectionString) {
		if ((connectionString == null) || connectionString.equals("")) return;
		address = "";
		user = connectionString.substring(connectionString.indexOf("//")+2, connectionString.indexOf(":"));
		passwd = connectionString.substring(connectionString.indexOf(":")+1, connectionString.indexOf("@"));
 		if(!connectionString.endsWith("@")) {
 			address = connectionString.substring(connectionString.indexOf("@")+1);
		}
		if(address.indexOf(":") != -1) {
			port = address.substring(address.indexOf(":")+1);
			address = address.substring(0, address.indexOf(":"));
		}
	}

	static public String getUser() {
		return user;
	}

	static public String getServer() {
		return address;
	}

	static public String getPasswd() {
		return passwd;
	}

	static public String getPort() {
		return port;
	}
}