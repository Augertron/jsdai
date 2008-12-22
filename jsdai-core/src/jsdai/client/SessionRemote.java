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

import java.util.Properties;

import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiQuery;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.query.SerializableRef;
import org.w3c.dom.Element;

/**  This interface represents a remote Server (bridge) through
	which remote repositories and their contents can be accessed.
	The whole transaction responsibility and multi-user looking is
	the task of the remote server.
	An SdaiSession has at most one SdaiBridgeRemote.
	All request for remote repositories are managed by this one SdaiBridgeRemote.*/

public interface SessionRemote {
	
	public static final int COMMITS_TABLE_REP_ID = -9;
	public static final int USERS_TABLE_REP_ID = -8;

	/* retrieving all remote repositories from the server.
		This method shall be invoked during SdaiSession.linkDataBaseBridge().
		These repositories shall be included into SdaiSession.knownServers. */
	public SdaiRepositoryRemote[] repositoriesList() throws SdaiException;


	public SdaiRepositoryRemote linkRepository(String repo_name) throws SdaiException;
    
    public SdaiRepositoryRemote linkRepository(long repositoryId) throws SdaiException;   //--VV--030523--

    public SdaiRepositoryRemote linkRepository(SerializableRef repositoryRef) throws SdaiException;

	/**  Create a remote repository. This is done inside the transaction.*/
	//public SdaiRepositoryRemote createRepository(String repo_name) throws SdaiException;

	public SdaiRepository createSdaiRepositoryImpl(SdaiSession session, String name, String location, 
		boolean fCheckNameLocation) throws SdaiException;

	public void registerCreatedRepository(SdaiRepository createdRepository) throws SdaiException;

	public boolean isStartMultiModelAccessAvailable() throws SdaiException;

	/**
	 * 
	 * @param models for any <code>model</code> in <code>models</code> condition
	 *               <code>model.getModRemote() != null &amp;&amp; model.getMode() == {@link SdaiModel#NO_ACCESS}</code>
	 *               should be <code>true</code>
	 * @param modelsLength condition <code>modelsLength &lt;= models.length</code> should be <code>true</code>
	 * @param mode
	 */
	public void startMultiModelAccess(SdaiModel[] models, int modelsLength, int mode) throws SdaiException;

	public void abort() throws SdaiException;

	public void commitStart() throws SdaiException;

 	// ... do write operations in between

	public void commitPostModelDelete() throws SdaiException;

	public void commitEnd(String appComment) throws SdaiException;

	public void switchToCommit(SerializableRef commitRef) throws SdaiException;

	public SdaiQuery newQuery(SdaiSession session, Element el) throws SdaiException;

	public void close() throws SdaiException;

	public Properties getProperties() throws SdaiException;

	void lock(ASdaiRepository repositories, ASchemaInstance schInstances,
			ASdaiModel models, int maxwait) throws SdaiException;

	void unlockAll() throws SdaiException;

}
