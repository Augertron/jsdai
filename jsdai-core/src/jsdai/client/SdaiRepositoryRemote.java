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

public interface SdaiRepositoryRemote extends SdaiEventSource {

	/** return error-description oin the case of an error */
	public String openRemoteRepository(String name) throws SdaiException;

    public long getRemoteId() throws SdaiException;

    public String getRemoteName() throws SdaiException;
    
    public String getRemoteLocation() throws SdaiException;

	public SdaiRepositoryHeader getRemoteHeader() throws SdaiException;

	/** To be applied during SdaiTransaction.committ() */
	public String writeRemoteHeader(SdaiRepositoryHeader newHeader) throws SdaiException;

	public String deleteRemoteRepository() throws SdaiException;

	public String closeRemoteRepository() throws SdaiException;

	/** To be applied during SdaiTransaction.commit() */
	public SchemaInstanceRemote createRemoteSchemaInstance(SchemaInstanceHeader header
														   ) throws SdaiException;

	// Remote model creation is now moved to
	// jsdai.clientbase.SdaiModelBase.verifySchemasTakeAction()
// 	public SdaiModelRemote createRemoteModel(SdaiModelHeader header,
// 											 String[] names, long[] crcs) throws SdaiException;

	//If get model is invoked with null then the "first" model is returned
	//Otherwise next SdaiModel after the current model is returned
	public SdaiModelRemote getRemoteModel(SdaiModelRemote current)throws SdaiException;

	public SdaiModelRemote getRemoteModel(String name)throws SdaiException;
    
	public int getRemoteModelCount() throws SdaiException;

	public int getRemoteSchemaInstanceCount() throws SdaiException;

	public SchemaInstanceRemote getRemoteSchemaInstance(SchemaInstanceRemote current
														) throws SdaiException;

	public SchemaInstanceRemote getRemoteSchemaInstance(String name) throws SdaiException;
    
	// Locks and UnLocks this repository with all its attributes and contents (model and schema instances).
	// Some privileges may be needed for this
	// Locking is performed on database level(not bridge)

	public void lockRemote () throws SdaiException;

	public void unlockRemote () throws SdaiException;

	public SdaiModelRemote findRemoteSdaiModelBySessionIdentif(long identif) throws SdaiException;

}
