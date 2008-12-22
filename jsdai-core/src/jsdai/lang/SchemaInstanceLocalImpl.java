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

import jsdai.dictionary.*;

/**
 *
 * @author  Vaidas Nargėlas
 */
class SchemaInstanceLocalImpl extends SchemaInstance {

	SchemaInstanceLocalImpl(String given_name, CSchema_definition schema,
			SdaiRepository given_repository) throws SdaiException {
		super(given_name, schema, given_repository);
	}


	SchemaInstanceLocalImpl(String given_name, SdaiRepository given_repository,
			SdaiModel given_dictionary) throws SdaiException {
		super(given_name, given_repository, given_dictionary);
	}



	public SdaiPermission checkPermission() {
		return SdaiPermission.WRITE;
	}

	public void checkRead() {
		// Local schema instance always has read permission
	}

	public void checkWrite() {
		// Local schema instance always has write permission
	}

	public ASdaiModel getAssociatedModels() throws SdaiException {
//		synchronized (syncObject) {
		check_schemaInstance();
		if (associated_models == null) {
			associated_models = new ASdaiModel(SdaiSession.setType0toN, this);
		}
		return associated_models;
//		} // syncObject
	}

	public ASchemaInstance getIncludedSchemaInstances() throws SdaiException {
		check_schemaInstance();
		if (included_schemas == null) {
			included_schemas = new ASchemaInstance(SdaiSession.setType0toN, this);
		}
		return included_schemas;
	}


	public void delete() throws SdaiException {
//		synchronized (syncObject) {
			deleteCommon();
			repository = null;
//		} // syncObject
	}


	public void addSdaiModel(SdaiModel model) throws SdaiException {
//		synchronized (syncObject) {
			addSdaiModelCommon(model);
//		} // syncObject
	}

	protected ASdaiModel getAssociatedModelsPrivateCommon() throws SdaiException {
		return null;
	}

	protected boolean committingInternal(boolean commit_bridge) throws SdaiException {
		return commit_bridge;
	}

	protected void committingAssocModels() throws SdaiException {
	}

	protected boolean deletingInternal(boolean commit_bridge, SdaiRepository repo) throws SdaiException {
		return commit_bridge;
	}

	protected boolean abortingInternal(boolean modif) throws SdaiException {
		if (!committed) {
			delete();
			return true;
		}
		return false;
	}

	protected boolean isRemoteInternal() throws SdaiException {
		return false;
	}

	protected boolean checkSchInstanceInternal(Object current_member) throws SdaiException {
		return false;
	}

	protected void attachRemoteSchInstance(Object current_member) throws SdaiException {
	}

	protected void startAssociatedModelsInternal(Object domain) throws SdaiException {
	}

	protected void refreshDomainInternal(boolean read_write, ASdaiModel rwModels) throws SdaiException {
	}

	protected void closeDomainInternal() throws SdaiException {
	}

}
