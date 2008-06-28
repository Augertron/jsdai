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

import jsdai.dictionary.*;
import java.lang.reflect.*;
 
/**
 * This class gives a general information on this SDAI implementation.
 * The attributes contain only static information which do not change during runtime.
 * @see "ISO 10303-22::7.4.2 implementation"
 */
public final class Implementation extends SdaiCommon {

/**
 * The current build number of this implementation.
 */
	public static final int build = 270; //268;

/**
 * The copyright information.
 */
	public static final String copyright="@JSDAI.COPYRIGHT@";
	static final String version = "Version @JSDAI.VERSION.MAJOR@.@JSDAI.VERSION.MIDDLE@.@JSDAI.VERSION.MINOR@ (Build " + build + ", @TODAY@)";
/**
 * The major number of SDAI implementation version.
 */
    static public final short major_version = getMajorVersion();
/**
 * The middle number of SDAI implementation version.
 */
	static public final short middle_version = getMiddleVersion();
/**
 * The minor number of SDAI implementation version.
 */
	static public final short minor_version = getMinorVersion();

	/* @NO.MAPPING.OFF@ */ static final boolean mappingSupport = true;
	/* @NO.REMOTE.OFF@ */ static final boolean remoteSupport = true;

	/* @NO.MAPPING.ON.START@ static final boolean mappingSupport = false; @NO.MAPPING.ON.END@ */
	/* @NO.REMOTE.ON.START@ static final boolean remoteSupport = false; @NO.REMOTE.ON.END@ */

	SdaiSession session;

    private static short getMajorVersion() {
        try {
            return (short)Integer.parseInt("@JSDAI.VERSION.MAJOR@");
        } catch(NumberFormatException e) {
            return 0;
        }
    }
    
    private static short getMiddleVersion() {
        try {
            return (short)Integer.parseInt("@JSDAI.VERSION.MIDDLE@");
        } catch(NumberFormatException e) {
            return 0;
        }
    }
    
    private static short getMinorVersion() {
        try {
            return (short)Integer.parseInt("@JSDAI.VERSION.MINOR@");
        } catch(NumberFormatException e) {
            return 0;
        }
    }
    
	SdaiCommon getOwner() {
		return session;
	}

	void modified() throws SdaiException {
		// dummy, skip
	}


	Implementation() {
	}


/**
 * Returns the name of the implementation as a <code>String</code>.
 * @return the name of this implementation.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public String getName() throws SdaiException {
//		synchronized (syncObject) {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		String str = null;
		switch (feature_level) {
			case 0: str = "SINGLE";
						break;
			case 1: str = "MULTIPLE";
						break;
			case 2: str = "NETWORK";
						break;
		}
		return "JSDAI " + str;
//		} // syncObject
	}


/**
 * Returns the software version level of this implementation.
 * @return a <code>String</code> specifying the software version level.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public String getLevel() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return version;
	}


/**
 * Returns the version of SDAI to which this implementation conforms.
 * @return a <code>String</code> specifying the version of SDAI.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public String getSdaiVersion() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return "{ iso standard 10303 part(22) version(1) }";
	}


/**
 * Returns the version of the SDAI language binding for this implementation.
 * @return a <code>String</code> specifying the version of the SDAI
 * language binding.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public String getBindingVersion() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return "{ iso standard 10303 part(27) version(1) }";
	}


/**
 * Returns the implementation class to which this implementation conforms.
 * @return an integer specifying the implementation class.
 * @throws SdaiException SS_NOPN, session is not open.
 * @see "ISO 10303-22::13.2 Implementations class specification"
 */
	public int getImplementationClass() throws SdaiException {
//		synchronized (syncObject) {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (feature_level == 0) {
			return 1;
		} else {
			return 5;
		}
//		} // syncObject
	}


/**
 * Returns the level of transaction supported by this implementation.
 * @return an integer specifying the level of transaction.
 * @throws SdaiException SS_NOPN, session is not open.
 * @see "ISO 10303-22::13.1.1 Levels of transaction"
 */
	public int getTransactionLevel() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return 3;
	}


/**
 * Returns the level of expression evaluation supported by this implementation.
 * @return an integer specifying the level of expression evaluation.
 * @throws SdaiException SS_NOPN, session is not open.
 * @see "ISO 10303-22::13.1.2 Levels of expression evaluation for validation and derived attributes"
 */
	public int getExpressionLevel() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return 2;
	}


/**
 * Returns the level of domain equivalence supported by this implementation.
 * @return an integer specifying the level of domain equivalence.
 * @throws SdaiException SS_NOPN, session is not open.
 * @see "ISO 10303-22::13.1.5 Levels of domain equivalence support"
 */
	public int getDomainEquivalenceLevel() throws SdaiException {
//		synchronized (syncObject) {
		if (session == null) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (feature_level == 0) {
			return 1;
		} else {
			return 2;
		}
//		} // syncObject
	}

	static int mappingReadCacheSize = 20 * 1024;
	static int userConcurrencyCompatibility = 0;

	/**
	 * Sets JSDAI feature with the integer value.
	 * List of features is implementation specific.
	 * If JSDAI implementation does not support feature with this name
	 * this method does nothing.<br>
	 * Example:
	 * <code>SdaiSession.getSession().getSdaiImplementation()
	 * .setFeature("mapping-read-cache-size", 8096);</code>
	 *
	 * @param name feature name
	 * @param value feature value
	 */
	public void setFeature(String name, int value) {
		if(name.equals("mapping-read-cache-size")) {
			mappingReadCacheSize = value;
		} else if(name.equals("user-concurrency-compatibility")) {
			if(value > 1) {
				value = 1;
			} else if(value < 0) {
				value = 0;
			}
			
		}
	}

	/**
	 * Gets JSDAI feature.
	 * 
	 *
	 * @param name feature name
	 * @return value of the feature or null if JSDAI implementation 
	 * does not support feature with this name
	 */
	public Object getFeature(String name) {
		if(name.equals("mapping-read-cache-size")) {
			return new Integer(mappingReadCacheSize);
		}
		return null;
	}

}
