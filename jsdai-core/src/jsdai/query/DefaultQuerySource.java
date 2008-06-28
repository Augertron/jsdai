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

import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.QuerySource;
import jsdai.lang.SdaiException;

/**
 *
 * Created: Wed Oct  1 15:53:31 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class DefaultQuerySource implements QuerySource {
	
	// Implementation of jsdai.lang.QuerySource

	/**
	 * Describe <code>query</code> method here.
	 *
	 * @param where a <code>String</code> value
	 * @param entity an <code>EEntity</code> value
	 * @param result an <code>AEntity</code> value
	 * @return an <code>int</code> value
	 * @exception SdaiException if an error occurs
	 */
	public int query(String where, EEntity entity, AEntity result) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	/**
	 * Describe <code>getQuerySourceDomain</code> method here.
	 *
	 * @return an <code>ASdaiModel</code> value
	 * @exception SdaiException if an error occurs
	 */
	public ASdaiModel getQuerySourceDomain() throws SdaiException {
		return null;
	}

	/**
	 * Describe <code>getQuerySourceInstances</code> method here.
	 *
	 * @return an <code>AEntity</code> value
	 * @exception SdaiException if an error occurs
	 */
	public AEntity getQuerySourceInstances() throws SdaiException {
		return null;
	}

	/**
	 * Describe <code>getQuerySourceInstanceRef</code> method here.
	 *
	 * @return a <code>SerializableRef</code> value
	 * @exception SdaiException if an error occurs
	 */
	public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		return null;
	}

	/**
	 * Describe <code>getQuerySourceDomainRef</code> method here.
	 *
	 * @return a <code>SerializableRef</code> value
	 * @exception SdaiException if an error occurs
	 */
	public SerializableRef getQuerySourceDomainRef() throws SdaiException {
		return null;
	}
	
} // DefaultQuerySource
