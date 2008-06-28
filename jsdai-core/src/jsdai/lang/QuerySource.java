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
 * This interface is a query source according to "ISO 10303-22::9.3.12 query_source"
 * It defines the source of a query execution. In the current JSDAI version
 * it is supported only for local queries. This interface should not be considered
 * stable.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public interface QuerySource {


	/**
	 * SDAI query according to part 22.
	 * <p>
	 * This method is not implemented.
	 * SdaiException FN_NAVL will be thrown if this method is invoked.
	 * @param where a <code>String</code> value
	 * @param entity an <code>EEntity</code> value
	 * @param result an <code>AEntity</code> value
	 * @return an <code>int</code> value
	 * @exception SdaiException FN_NAVL, function not available.
	 * @see "ISO 10303-22::10.4.14 SDAI query"
	 */
	int query(String where, EEntity entity, AEntity result) throws SdaiException;

	/**
	 * Returns domain in which to perform a search.
	 *
	 * @return ASdaiModel containing the domain
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	ASdaiModel getQuerySourceDomain() throws SdaiException;

	/**
	 * Returns starting instances which serve as
	 * the first input instance set for a query.
	 *
	 * @return <code>AEntity</code> containing the instances or null
	 *         this aggregate consists all instances in query source domain
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	AEntity getQuerySourceInstances() throws SdaiException;

	/**
     * @since 3.6.0
     */
    SerializableRef getQuerySourceInstanceRef() throws SdaiException;

    /**
     * @since 3.6.0
     */
	SerializableRef getQuerySourceDomainRef() throws SdaiException;
} // QuerySource
