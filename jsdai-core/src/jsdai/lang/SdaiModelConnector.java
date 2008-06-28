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
 * This interface provides the abstraction of <code>SdaiModel</code>
 * connections for instance references crossing <code>SdaiModel</code>
 * boundaries. The interface is implemented by <code>SdaiModel</code>
 * and internal connector classes. It is meant for  internal use only.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @since 4.0.0
 */

public interface SdaiModelConnector {

	/**
	 * Connects connector to the incomming connector ring.
	 * This method is intended for internal use only.
	 *
	 * @param connector a connector to connect.
	 * @since 4.0.0
	 */
	public void connectInConnector(SdaiModel.Connector connector);

	/**
	 * Disconnects connector from the incomming connector ring.
	 * This method is intended for internal use only.
	 *
	 * @param connector a connector to disconnect.
	 * @since 4.0.0
	 */
	public void disconnectInConnector(SdaiModel.Connector connector);

	/**
	 * Resolve to connected <code>SdaiModel</code>.
	 * This method is intended for internal use only.
	 *
	 * @return resolved <code>SdaiModel</code>
	 * @exception SdaiException if an error occurs
	 * @since 4.0.0
	 */
	public SdaiModel resolveConnectedModel() throws SdaiException;

	/**
	 * Gets the reference to the connected <code>SdaiModel</code>.
	 * This method is intended for internal use only.
	 *
	 * @return the reference to <code>SdaiModel</code>
	 * @exception SdaiException if an error occurs
	 */
	public SerializableRef getQuerySourceInstanceRef() throws SdaiException;
	
} // SdaiModelConnector
