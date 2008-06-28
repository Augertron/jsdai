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

package jsdai.client;

import jsdai.lang.SdaiEventSource;
import jsdai.lang.SdaiException;

public interface SchemaInstanceRemote extends SdaiEventSource {
	// Schema instance methods

	public String getRemoteName() throws SdaiException;

	public long getRemoteId() throws SdaiException;

	// Deleting remote schema instance is now moved to
	// jsdai.lang.SchemaInstance.deletingInternal()
// 	public void deleteRemote() throws SdaiException;

	public SdaiRepositoryRemote getRemoteRepository() throws SdaiException;

	public void writeRemoteHeader(SchemaInstanceHeader header) throws SdaiException;

	// Remote associated model setting is now moved to
	// jsdai.clientbase.SchemaInstanceBase.committingAssocModels()
// 	public void setRemoteAssociatedModels(SdaiModelRemote[] models) throws SdaiException;

	// Locks and UnLocks this schema instance, together with it's attributes and all associated models
	//  attributes and contents (model and schema instances).
	// Some privileges may be needed for this 	
	// Locking is performed on database level(not bridge)
	
	public void lockRemote () throws SdaiException;
	public void unlockRemote () throws SdaiException;

	public SchemaInstanceHeader getRemoteHeader() throws SdaiException;
}
