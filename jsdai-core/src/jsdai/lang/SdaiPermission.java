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

import java.lang.Comparable;

/**
 * Enumeration class for representing access rights to remote <code>SdaiRepositories</code>, 
 * <code>SdaiModels</code> and <code>SchemaInstances</code>. On JSDAI-DB access rights can
 * be assigned to above mentioned SDAI objects as well as to user groups.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 4.0.0
 */

public class SdaiPermission implements Comparable {

	/**
	 * Administrative permission as integer value. <code>ADMIN_INT</code> is greater than <code>WRITE_INT</code>
	 */
	public static final int ADMIN_INT = 4;

	/**
	 * Administrative permission as <code>SdaiPermission</code> object.
	 */
	public static final SdaiPermission ADMIN = new SdaiPermission(ADMIN_INT, "Administrative");

	/**
	 * Read and write permission as integer value.
	 * <code>WRITE_INT</code> is less than <code>ADMIN_INT</code> and greater than <code>READ_INT</code>
	 */
	public static final int WRITE_INT = 3;

	/**
	 * Read and write permission as <code>SdaiPermission</code> object.
	 */
	public static final SdaiPermission WRITE = new SdaiPermission(WRITE_INT, "Read Write");

	/**
	 * Read only permission as integer value.
	 * <code>READ_INT</code> is less than <code>WRITE_INT</code> and greater than <code>NOACCESS_INT</code>
	 */
	public static final int READ_INT = 2;

	/**
	 * Read only permission as <code>SdaiPermission</code> object.
	 */
	public static final SdaiPermission READ = new SdaiPermission(READ_INT, "Read Only");

	/**
	 * No access permission as integer value.
	 * <code>NOACCESS_INT</code> is less than <code>READ_INT</code> and greater than <code>HIDDEN_INT</code>
	 */
	public static final int NOACCESS_INT = 1;

	/**
	 * No access permission as <code>SdaiPermission</code> object.
	 */
	public static final SdaiPermission NOACCESS = new SdaiPermission(NOACCESS_INT, "No Access");

	/**
	 * No access permission as integer value.
	 * <code>HIDDEN_INT</code> is less than <code>NOACCESS_INT</code> and greater than <code>HIDDEN_INT</code>
	 */
	public static final int HIDDEN_INT = 0;

	/**
	 * Hidden permission as <code>SdaiPermission</code> object.
	 */
	public static final SdaiPermission HIDDEN = new SdaiPermission(HIDDEN_INT, "Hidden");

	/**
	 * Default permission as integer value. See {@link #DEFAULT} for more information on default permission.
	 */
	public static final int DEFAULT_INT = -1;

	/**
	 * Default permission as <code>SdaiPermission</code> object. Default permission can be used as
	 * assigned permission to reflect the fact that effective permission is inherited from higher level
	 * assigned permission. Default permission can be returned by <code>checkPermission</code> methods
	 * when the effective permission can not be known more specifically at the moment, eg.
	 * if remote <code>SdaiModel</code> was just created and not yet committed to JSDAI-DB. 
	 */
	public static final SdaiPermission DEFAULT = new SdaiPermission(DEFAULT_INT, "Default");

	private final int type;
	private final String name;

	private SdaiPermission(int type, String name) {
		this.type = type;
		this.name = name;
	}

	protected SdaiPermission() { // Invalid constructor to use by subclass
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns type of the permission.
	 *
	 * @return type of the permission as integer value
	 * @see #ADMIN_INT
	 * @see #WRITE_INT
	 * @see #READ_INT
	 * @see #NOACCESS_INT
	 * @see #HIDDEN_INT
	 * @see #DEFAULT_INT
	 */
	public final int getType() {
		return type;
	}

	/**
	 * Compares this <code>SdaiPermission</code> object to another object. If the object is
	 * a <code>SdaiPermission</code>, permission type values are compared numerically as
	 * returned by method <code>getType</code>. Otherwise it throws a <code>ClassCastException</code>.
	 *
	 * @param other the object to be compared
	 * @return the value 0 if other permission is of the same type; a value less than 0 is this permission's
	 *         type is numerically less than other permission's type; and a value greater than 0 if
	 *         this permission's type is numerically greater than other permission's type.
	 * @throws ClassCastException if the argument is not a <code>SdaiPermission</code>
	 */
	public int compareTo(Object other) {
		SdaiPermission otherPermission = (SdaiPermission)other;
		return type - otherPermission.type;
	}

	/**
	 * Returns a string representation of this <code>SdaiPermission</code> object.
	 *
	 * @return a string representation of this object.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Returns a hash code for this <code>SdaiPermission</code> object. The result is 
	 * this permission's type XORed with prime number 21101..
	 *
	 * @return a hash value for this object.
	 */
	public int hashCode() {
		return type ^ 21101;
	}

	/**
	 * Compares this object against the specified object. The result is <code>true</code>
	 * if and only if the argument is a <code>SdaiPermission</code> and is of type equal to
	 * this permission's type.
	 *
	 * @param other the object to be compared
	 * @return <code>true</code> if objects are equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object other) {
		if(!(other instanceof SdaiPermission)) {
			return false;
		}
		SdaiPermission otherPermission = (SdaiPermission)other;
		return type == otherPermission.type;
	}

	/**
	 * Returns a <code>SdaiPermission</code> corresponding to specified integer value.
	 * The following is true for any returned object except DEFAULT:
	 * <code>getPermissionByType(type).getType() == type</code>. If no
	 * <code>SdaiPermission</code> matches the specified integer value, DEFAULT is returned.
	 *
	 * @param type the type of <code>SdaiPermission</code>
	 * @return the corresponding <code>SdaiPermission</code>.
	 */
	public static SdaiPermission getPermissionByType(int type) {
		switch(type) {
		case ADMIN_INT: return ADMIN;
		case WRITE_INT: return WRITE;
		case READ_INT: return READ;
		case NOACCESS_INT: return NOACCESS;
		case HIDDEN_INT: return HIDDEN;
		default: return DEFAULT;
		}
	}


} // SdaiPermission
