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

package jsdai.query;

import java.io.Serializable;

/**
 * SerializableRef.java
 *
 *
 * Created: Tue Sep 30 14:26:57 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public interface SerializableRef extends Serializable {

	/**
	 * Reference to <code>SdaiRepository</code> type.
	 */
	public static final int SDAI_REPOSITORY_REF = 0;

	/**
	 * Reference to <code>SchemaInstance</code> type.
	 */
	public static final int SCHEMA_INSTANCE_REF = 1;

	/**
	 * Reference to <code>SdaiModel</code> type.
	 */
	public static final int SDAI_MODEL_REF = 2;

	/**
	 * Reference to <code>EEntity</code> type.
	 */
	public static final int EENTITY_REF = 3;

	/**
	 * Reference to <code>ASchemaInstance</code> type.
	 */
	public static final int ASCHEMA_INSTANCE_REF = 4;

	/**
	 * Reference to <code>ASdaiModel</code> type.
	 */
	public static final int ASDAI_MODEL_REF = 5;

	/**
	 * Reference to <code>AEntity</code> type.
	 */
	public static final int AENTITY_REF = 6;

	/**
	 * Returns type of this reference.
	 *
	 * @return the type of the reference as 
	 *         <code>SDAI_REPOSITORY_REF</code>
	 *         <code>SCHEMA_INSTANCE_REF</code>,
	 *         <code>SDAI_MODEL_REF</code>,
	 *         <code>EENTITY_REF</code>,
	 *         <code>ASCHEMA_INSTANCE_REF</code>,
	 *         <code>ASDAI_MODEL_REF</code>, or
	 *         <code>AENTITY_REF</code>
	 */
	public int getType();

	/**
	 * Returns <code>SdaiRepository</code> identifier on remote database.
	 *
	 * @return the <code>SdaiRepository</code> identifier or -1 is this reference
	 *         does not contain the reference to <code>SdaiRepository</code>
	 */
	public long getRepositoryId();

    /**
	 * Returns <code>SdaiModel</code> identifier on remote database.
	 *
	 * @return the <code>SdaiModel</code> identifier or -1 is this reference
	 *         does not contain the reference to <code>SdaiModel</code>
	 */
	public long getModelId();

    /**
	 * Returns <code>SchemaInstance</code> identifier on remote database.
	 *
	 * @return the <code>SchemaInstance</code> identifier or -1 is this reference
	 *         does not contain the reference to <code>SchemaInstance</code>
	 */
	public long getSchemaInstanceId();

    /**
	 * Returns <code>EEntity</code> instance persistent label.
	 *
	 * @return the <code>EEntity</code> instance persistent label or -1 is this reference
	 *         does not contain the reference to <code>EEntity</code>
	 */
	public long getPersistentLabel();

    /**
	 * Returns <code>EEntity</code> instance type index which is the internal
	 * JSDAI index to increase the instance searching speed.
	 *
	 * @return the <code>EEntity</code> instance type index or -1 is this reference
	 *         does not contain this index
	 */
	public int getTypeIndex();

	/**
	 * Returns array of <code>SerializableRef</code> this reference contains in the
	 * case it is a reference to <code>ASchemaInstance</code>, <code>ASdaiModel</code> or
	 * <code>AEntity</code>.
	 *
	 * @return the array of <code>SerializableRef</code> or null is this reference
	 *         does not contain the above described information
	 */
	public SerializableRef[] getMembers();

} // SerializableRef
