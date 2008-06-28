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

import java.io.*;
import java.util.Hashtable;
import jsdai.lang.SdaiEventSource;
import jsdai.lang.SdaiException;
import jsdai.lang.EEntity;

/** This interface is representing a SdaiModel which is hosted remotly on some server.
	The interface is independent of some particular implementation.
	There is a one to one correspondence between a remote SdaiModel and the
	SdaiModelRemote object - in other words, jsdai.lang shall keep the reference to this object
	as long as the owning SdaiRepository is open (?) and the
	corresponding SdaiModel exisit. */
public interface SdaiModelRemote  extends SdaiEventSource {

	public String getRemoteName() throws SdaiException;

	public long getRemoteId() throws SdaiException;

	/** Returns all info of the model which are not part of the contents.
		The returned object is not further updated.
		Therefor jsda.lang shall not keep it*/
	public SdaiModelHeader getRemoteHeader() throws SdaiException; // returns model header

	// not to be implemented in the first phase */
	// public SdaiModelHeaderDataInput startReadOnlyAccessWithHeader() throws SdaiException;

	// Remote model starting access is now moved to
	// jsdai.clientbase.SdaiModelBase.verifySchemasTakeAction()
// 	/** Request model contents from the server for RO access.
// 		Returns null if no access can be granted.
// 		The returned DataInput stream must be read till the end before
// 		invoking any other method from jsdai.client package.*/
// 	public DataInput startRemoteReadOnlyAccess(String[] names, long[] crcs) throws SdaiException;

	// Remote model starting access is now moved to
	// jsdai.clientbase.SdaiModelBase.verifySchemasTakeAction()
// 	/** Request model contents from the server for RW access.
// 		Returns null if no access can be granted.
// 		The returned DataInput stream must be read till the end before
// 		invoking any other method from jsdai.client package.*/
// 	public DataInput startRemoteReadWriteAccess(String[] names, long[] crcs) throws SdaiException;
    
// 	/**
// 	 * Returns remote representation of entity intance object
// 	 * For one EEntity object it can be called only once.
// 	 */
// 	public EntityRemote getRemoteEntity(EEntity entity) throws SdaiException;

	/** Infroms the server that RO or RW access is ended */
	public void endRemoteAccess() throws SdaiException;

	/** Request the server to go from RO to RW access.
		Returns TRUE on success. */
	public boolean promoteRemoteSdaiModelToRW() throws SdaiException;

	/** Informs the server/bridge that RW access is downgraded to RO access */
	public void reduceRemoteSdaiModelToRO() throws SdaiException;


	// public void rename() throws SdaiException; // maybe not needed

	// Deleting remote model is now moved to
	// jsdai.lang.SdaiModel.deletingInternal()
	/** This model is deleted,
		To be invoked during SDAI-commit. */
// 	public void deleteRemote() throws SdaiException;

	/** writeHeader must be followed by either writeData or writeEnd.
		This covers all changes which are not on the model contents,
		including renameSdaiModel (but not delete).
		To be invoked during SDAI-commit.*/
	public void writeRemoteHeader(SdaiModelHeader data) throws SdaiException;

	// Remote model data writing is now moved to
	// jsdai.clientbase.SdaiModelBase.performWriteData()
// 	/** To be invoked during SDAI-commit. */
// 	public void writeRemoteData() throws SdaiException;

	/** writeEnd() terminates a write operation which was started
		before by writeHeader and/or writeData.
		To be invoked during SDAI-commit. */
// 	public void writeEnd() throws SdaiException;

	/** Returns the SdaiRepositoryRemote of the owning SdaiRepository.*/
	public SdaiRepositoryRemote getRemoteRepository() throws SdaiException;

	// Locks and UnLocks this model with all its attributes and contents (entity instances).
	// Some privileges may be needed for this
	// Locking is performed on database level(not bridge)
	public void lockRemote () throws SdaiException;
	public void unlockRemote () throws SdaiException;

}
