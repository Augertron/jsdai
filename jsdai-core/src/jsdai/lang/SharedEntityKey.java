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

package jsdai.lang;

import java.util.*;

/**
 * This class extends the usage of user defined entity instance data.
 * It allows to store and retrieve entity instance user data using
 * <code>String</code> key. The methods of this class are thread safe.
 * This class is primarily intended for internal JSDAI use.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see EEntity#getTemp(Object)
 * @see EEntity#setTemp(Object, Object)
 */
public class SharedEntityKey {
    static Map strKeyMap = new HashMap();

    /**
	 * Returns the user data stored by the instance
	 * and identified by a key.
	 *
	 * @param instance The entity instance for which user data has to be returned
	 * @param keyName The name of the key
	 * @return The user defined data
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public static Object getKeyValue(EEntity instance, String keyName) throws SdaiException {
		Object key = null;
		synchronized(strKeyMap) {
			if (strKeyMap.containsKey(keyName)) {
				key =  strKeyMap.get(keyName);
			} else {
				key = new Object();
				strKeyMap.put(keyName, key);
			}
		}
		return ((CEntity) instance).getTemp(key);
    }

    /**
	 * Stores the user data by the instance which is identified by a key.
	 *
	 * @param instance The entity instance for which user data has to be stored
	 * @param keyName The name of the key
	 * @param value The user defined data
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public static void setKeyValue(EEntity instance, String keyName, Object value) throws SdaiException {
		Object key = null;
		synchronized(strKeyMap) {
			if (strKeyMap.containsKey(keyName)) {
				key =  strKeyMap.get(keyName);
			} else {
				key = new Object();
				strKeyMap.put(keyName, key);
			}
		}
		((CEntity) instance).setTemp(key, value);
	}
}
