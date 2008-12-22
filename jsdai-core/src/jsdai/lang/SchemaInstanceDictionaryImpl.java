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
class SchemaInstanceDictionaryImpl extends SchemaInstance {
	
	SchemaInstanceDictionaryImpl(String given_name, CSchema_definition schema, 
			SdaiRepository given_repository) throws SdaiException {
		super(given_name, schema, given_repository);
	}

	SchemaInstanceDictionaryImpl(String given_name, SdaiRepository given_repository, 
			SdaiModel given_dictionary, SdaiModel given_schema_model) throws SdaiException {
		super(given_name, given_repository, given_dictionary, given_schema_model);
	}



	public SdaiPermission checkPermission() {
		return SdaiPermission.READ;
	}

	public void checkRead() {
		// Dictionary schema instance always has read permission
	}

	public void checkWrite() throws SdaiException {
		throw new SdaiException(SdaiException.SY_SEC, "Dictionary schema instance can not be modified");
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


	public ASdaiModel getReferencedModels() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"Method is not actual for system repository SchemaInstances; please, use getAssociatedModels() instead of.");
	}


	public ASchemaInstance getIncludedSchemaInstances() throws SdaiException {
		check_schemaInstance();
		if (included_schemas == null) {
			included_schemas = new ASchemaInstance(SdaiSession.setType0toN, this);
		}
		return included_schemas;
	}


	public void delete() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"Schema instances in the system repository cannot be deleted by the user.");
	}


	public void addSdaiModel(SdaiModel model) throws SdaiException {
//		synchronized (syncObject) {
		if (!allow_model) {
			throw new SdaiException(SdaiException.FN_NAVL,
				"Schema instances in the system repository cannot be modified by the user.");
		}
		addSdaiModelCommon(model);
//		} // syncObject
	}


	public void addSchemaInstance(SchemaInstance schemaInstance) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	protected ASdaiModel getAssociatedModelsPrivateCommon() throws SdaiException {
		return null;
	}


	protected boolean committingInternal(boolean commit_bridge) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected void committingAssocModels() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected boolean deletingInternal(boolean commit_bridge, SdaiRepository repo) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected boolean abortingInternal(boolean modif) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
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
