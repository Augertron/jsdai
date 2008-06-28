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

import jsdai.query.SerializableRef;

/**
 * This interface defines the results of a query operation.
 * That is the preferred way to obtain the results of a query.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see SdaiQuery
 */
public interface QueryResultSet {

	/**
	 * Moves to next result. In the beginning the result set is
	 * positioned before the first result therefore invoking this
	 * method sets the result set on the first result. If the
	 * position after last result set was reached it implicitly
	 * invokes <code>close</code> method.
	 *
	 * @return True if the next result is available and false 
	 *         if results are exhausted
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #close()
	 */
	public boolean next() throws SdaiException;

	/**
	 * Returns the item of the current result identified
	 * by the <code>indexPos</code>.
	 *
	 * @param itemPos the index of the item. Items are numbered starting from 1.
	 * @return the item value. The object type is specific to a query.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public Object getItem(int itemPos) throws SdaiException;

	/**
	 * Returns the item of the current result identified
	 * by the <code>indexPos</code> to specified <code>ItemStruct</code>.
	 *
	 * @param itemPos the index of the item. Items are numbered starting from 1.
	 * @param itemStruct the <code>ItemStruct</code> to put item value in
	 * @return a reference to <code>itemStruct</code> parameter
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
     * @since 3.6.0
	 */
	public ItemStruct getItemStruct(int itemPos, ItemStruct itemStruct) throws SdaiException;

	/**
	 * Closes this result set. This releases all resources connected to
	 * the result set and puts it into invalid state. Any method called
	 * on the result set after it was closed other than <code>close</code>
	 * may give unpredictable result. This method does not have to be
	 * called if the last call to <code>next</code> method returned false
	 * because it closes the result set implicitly.  
	 *
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #next()
     * @since 4.1.0
	 */
	public void close() throws SdaiException;

	/**
	 * This class is a structure to store query result item
	 * value in an efficient way. It implements interface
	 * <code>SerializableRef</code> therefore objects of class can
	 * be passed to <code>getXxxByRef</code> methods directly as long as
	 * they contain reference to JSDAI object of interest.
	 *
	 * @see SdaiSession#getRepositoryByRef(SerializableRef)
	 * @see SdaiRepository#getSdaiModelByRef(SerializableRef)
	 * @see SdaiRepository#getSchemaInstanceByRef(SerializableRef)
	 * @see SdaiRepository#getInstanceByRef(SerializableRef)
	 *
     * @since 3.6.0
	 */
	public static class ItemStruct implements SerializableRef {

		/**
		 * This structure contains a reference to entity instance.
		 * The following fields are filled in it:
		 * <code>repositoryId</code>, <code>modelId</code>,
		 * <code>persistentLabel</code> and <code>typeIndex</code>.
		 * The structure is available for use as <code>SerializableRef</code>.
		 */
		public static final char TYPE_ENTITY = 'E';

		/**
		 * This structure contains a reference to numeric value.
		 * The field <code>numericValue</code> is filled in.
		 */
		public static final char TYPE_NUMERIC = 'N';

		/**
		 * This structure contains a reference to string value.
		 * The field <code>stringValue</code> is filled in.
		 */
		public static final char TYPE_STRING = 's';

		/**
		 * This field containts the item type.
		 * It can be one of:
		 * <code>TYPE_ENTITY</code>,
		 * <code>TYPE_NUMERIC</code> or
		 * <code>TYPE_STRING</code>
		 */
		public char itemType;

		/**
		 * A database id of the remote repository which contains the referenced entity instance.
		 * @see #TYPE_ENTITY
		 */
		public long repositoryId;

		/**
		 * A database id of the remote model which contains the referenced entity instance.
		 * @see #TYPE_ENTITY
		 */
		public long modelId;

		/**
		 * A persistent label of a remote entity instance
		 * @see #TYPE_ENTITY
		 */
		public long persistentLabel;

		/**
		 * An entity instance type index or -1 if this information is not available
		 * @see #TYPE_ENTITY
		 */
		public int typeIndex;

		/**
		 * A numeric value
		 * @see #TYPE_NUMERIC
		 */
		transient public double numericValue;

		/**
		 * A string value
		 * @see #TYPE_STRING
		 */
		transient public String stringValue;

		// SerializableRef methods

		/**
		 * Returns type of this reference.
		 *
		 * @return <code>EENTITY_REF</code> if this is a reference to entity instance
		 *         or -1 otherwise
		 */
		public int getType() {
			return itemType == TYPE_ENTITY ? EENTITY_REF : -1;
		}

		/**
		 * Returns <code>SdaiRepository</code> identifier on remote database.
		 *
		 * @return the <code>SdaiRepository</code> identifier or -1 is this reference
		 *         does not contain the reference to <code>SdaiRepository</code>
		 */
		public long getRepositoryId() {
			return repositoryId;
		}

		/**
		 * Returns <code>SdaiModel</code> identifier on remote database.
		 *
		 * @return the <code>SdaiModel</code> identifier or -1 is this reference
		 *         does not contain the reference to <code>SdaiModel</code>
		 */
		public long getModelId() {
			return modelId;
		}

		/**
		 * Returns <code>EEntity</code> instance persistent label.
		 *
		 * @return the <code>EEntity</code> instance persistent label or -1 is this reference
		 *         does not contain the reference to <code>EEntity</code>
		 */
		public long getPersistentLabel() {
			return persistentLabel;
		}

		/**
		 * Returns <code>EEntity</code> instance type index which is the internal
		 * JSDAI index to increase the instance searching speed.
		 *
		 * @return the <code>EEntity</code> instance type index or -1 is this reference
		 *         does not contain this index
		 */
		public int getTypeIndex() {
			return typeIndex;
		}

		/**
		 * Not implemented method of <code>SerializableRef</code>
		 * because this structure can not contain this type of information.
		 *
		 * @return -1
		 */
		public long getSchemaInstanceId() {
			return -1;
		}

		/**
		 * Not implemented method of <code>SerializableRef</code>
		 * because this structure can not contain this type of information.
		 *
		 * @return null
		 */
		public SerializableRef[] getMembers() {
			return null;
		}
	}
} // QueryResultSet
