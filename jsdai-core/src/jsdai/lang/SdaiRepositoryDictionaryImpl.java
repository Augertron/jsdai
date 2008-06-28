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
import jsdai.client.*;
import java.io.*;
import java.util.*;

/**
 * SdaiRepository system dictionary implementation
 */
class SdaiRepositoryDictionaryImpl extends SdaiRepository {

/**
	An auxiliary array used to resolve indexing when preparing set 'associated_models'
	for a schema instance while reading special binary file 'jsdai/repository'.
*/
	private SdaiModel [] models_from_compiler_repo;

	SdaiRepositoryDictionaryImpl(SdaiSession session, String name, Object location, 
	boolean fCheckNameLocation)	throws SdaiException {
		super(session, name, location, fCheckNameLocation);
	}

	SdaiRepositoryDictionaryImpl(SdaiSession session, String name) throws SdaiException {
		super(session, name);
	}

	protected SdaiModel createModel(String model_name, CSchema_definition schema) throws SdaiException {
		return new SdaiModelDictionaryImpl(model_name, schema, this);
	}
	
	protected SdaiModel createModel(String model_name) {
		return new SdaiModelDictionaryImpl(model_name, this);
	}
	
	protected SdaiModel createModel(String model_name, SdaiModel dict) throws SdaiException {
		return new SdaiModelDictionaryImpl(model_name, this, dict);
	}
	
/**
	Creates a repository called 'virtual'. In this repository only
	the following fields from those defined in Part 22 are set with values:
	- name;
	- session;
	- models.
	This method is used to create a repository for the repository name
	found when reading model's binary file provided there is no repository
	with this name in the set 'known_servers'.
	The method is invoked in one of variations of extractModel in class
	SdaiModel.
*/
	protected SdaiRepository createVirtualRepository(String name) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repository");
	}
	protected void commitExternalData() throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repository");
	}

	protected ExternalData createNewEntityExternalData(CEntity entity, boolean existing)
	throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repository");
	}

	protected boolean testNewEntityExternalData(CEntity entity) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repository");
	}


	protected boolean doCheckCreateSchemaInstance(ESchema_definition nativeSchema) {
		// Dictionary repository does not allow to create schema instances
		return false;
	}

	protected boolean doCheckCreateSdaiModel(ESchema_definition underlyingSchema)  {
		// Dictionary repository does not allow to create models
		return false;
	}

	public SdaiPermission checkPermission() {
		return SdaiPermission.READ;
	}

	public void checkRead() {
		// Dictionary repository always has read permission
	}

	public void checkWrite() throws SdaiException {
		throw new SdaiException(SdaiException.SY_SEC, "Dictionary repository can not be modified");
	}

	public void openRepository() throws SdaiException {
		if (active) {
			throw new SdaiException(SdaiException.RP_OPN, this);
		}
	}


	public SdaiModel createSdaiModel(String model_name, ESchema_definition schema) throws  SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"This method is not available for the system repository.");
	}


	public SdaiModel createSdaiModel(String model_name, Class schema) throws  SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"This method is not available for the system repository.");
	}


	public SdaiModel findSdaiModel(String name) throws SdaiException {
//		synchronized (syncObject) {
			return findSdaiModelCommon(name);
//		} // syncObject
	}


	public SchemaInstance createSchemaInstance(String name, ESchema_definition schema) throws  SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"This method is not available for the system repository.");
	}


	public SchemaInstance createSchemaInstance(String name, Class schema) throws  SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
			"This method is not available for the system repository.");
	}

	SchemaInstance createDictionarySchemaInstance(String given_name, CSchema_definition schema)
	throws SdaiException {
		return new SchemaInstanceDictionaryImpl(given_name, schema, this);
	}

	public SchemaInstance findSchemaInstance(String name) throws SdaiException {
//		synchronized (syncObject) {
			return findSchemaInstanceCommon(name);
//		} // syncObject
	}


	protected void closeRepositoryInternal(boolean delete_repo) throws SdaiException {
		throw new SdaiException(SdaiException.RP_NAVL, this);
	}


	public EEntity getSessionIdentifier(String label) throws SdaiException {
//		synchronized (syncObject) {
		long identifier = getSessionIdentifierInit(label);
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			if (model.getMode() == SdaiModel.NO_ACCESS) {
				((SdaiModelDictionaryImpl)model).loadInstanceIdentifiersSystemRepo();
				int ind = model.find_instance_id(identifier);
				if (ind < 0) {
					continue;
				}
				try {
					model.startReadOnlyAccess();
				} catch (SdaiException ex) {
					if (!model.bin_file_missing) {
						throw ex;
					}
				}
			}
			CEntity inst = model.quick_find_instance(identifier);
			if(inst != null) {
				return (EEntity)inst;
			}
		}
		throw new SdaiException(SdaiException.EI_NEXS);
//		} // syncObject
	}


	protected void deleteRepositoryInternal() throws SdaiException {
		String base = SdaiSession.line_separator + AdditionalMessages.RP_DICT;
		throw new SdaiException(SdaiException.RP_NAVL, this, base);
	}


/**
	Extracts schema instances, corresponding to the Express schemas in JSDAI library,
	from the file 'repository' produced by Express compiler.
	The schema instances are put into the set 'schemas'.
	This method is invoked in SdaiSession for SystemRepository only.
*/
    void loadSchemaInstances(DataInput stream) throws SdaiException {
		int i, j;
		int count;
		int index;
		byte bt;
		String str, some_string;
		SdaiModel model;
		SchemaInstance sch_instance;

		try {
			bt = stream.readByte();
			if (bt != 'R') {
                String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			build_number = stream.readInt();
			short something = stream.readShort();
			something = stream.readShort();
			something = stream.readShort();
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = stream.readShort();
//System.out.println(" SdaiRepository  count of descriptions: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  description: " + some_string);
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			some_string = stream.readUTF();
//System.out.println(" SdaiRepository  repo name: " + some_string);
			some_string = stream.readUTF();
//System.out.println(" SdaiRepository  time_stamp: " + some_string);
			count = stream.readShort();
//System.out.println(" SdaiRepository  count of authors: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  author: " + some_string);
			}
			count = stream.readShort();
//System.out.println(" SdaiRepository  count of organizations: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  organization: " + some_string);
			}
			some_string = stream.readUTF();
//System.out.println(" SdaiRepository  preprocessor_version: " + some_string);
			some_string = stream.readUTF();
//System.out.println(" SdaiRepository  originating_system: " + some_string);
			some_string = stream.readUTF();
//System.out.println(" SdaiRepository  authorization: " + some_string);
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = stream.readShort();
//System.out.println(" SdaiRepository  count of schemas: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  schema: " + some_string);
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			bt = stream.readByte();
			if (bt == 'L') {
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  default_language: " + some_string);
				bt = stream.readByte();
			}
			if (bt == 'C') {
				count = stream.readShort();
				if (count < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
//System.out.println(" SdaiRepository  context_count: " + count);
				for (j = 1; j <= count; j++) {
					some_string = stream.readUTF();
//System.out.println(" SdaiRepository  context: " + some_string);
				}
				bt = stream.readByte();
			}
			if (bt != 'I') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			largest_identifier = stream.readLong();
//System.out.println(" SdaiRepository  largest_identifier: " + largest_identifier);

			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			int schemas_count = stream.readShort();
//System.out.println(" SdaiRepository  schemas_count: " + schemas_count);
			StaticFields staticFields = StaticFields.get();
			if (staticFields.schema_names == null) {
				if (schemas_count > SCHEMAS_ARRAY_SIZE) {
					staticFields.schema_names = new String[schemas_count];
				} else {
					staticFields.schema_names = new String[SCHEMAS_ARRAY_SIZE];
				}
			} else if (schemas_count > staticFields.schema_names.length) {
				staticFields.schema_names = new String[schemas_count];
			}
			for (i = 0; i < schemas_count; i++) {
				staticFields.schema_names[i] = stream.readUTF();
//System.out.println(" SdaiRepository  schema_name: " + schema_names[i]);
			}

			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = stream.readInt();
//System.out.println(" SdaiRepository  model_count: " + count);
			int md_count = stream.readShort();
//System.out.println(" SdaiRepository  no of models: " + md_count);
			if (models_from_compiler_repo == null || models_from_compiler_repo.length < md_count) {
				models_from_compiler_repo = new SdaiModel[md_count];
			}
			boolean byte_accepted = false;
			for (i = 0; i < md_count; i++) {
				if (!byte_accepted) {
					bt = stream.readByte();
					if (bt != 'B') {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				str = stream.readUTF();
				int str_l = str.length();
				if (str_l > SdaiSession.ENDING_FOR_DICT &&
						str.substring(str_l-SdaiSession.ENDING_FOR_DICT).equalsIgnoreCase(SdaiSession.DICTIONARY_NAME_SUFIX)) {
					internal_usage = true;
					model = findSdaiModel(str);
					internal_usage = false;
					if (model == null) {
						model = createModel(str, SdaiSession.baseDictionaryModel.described_schema);
						session.data_dictionary.addSdaiModel(model);
						session.data_mapping.addSdaiModel(model);
						models.addUnorderedRO(model);
						insertModel();
					}
					models_from_compiler_repo[i] = model;
//System.out.println(" SdaiRepository accepted dictionary model name: " + str + "   i = " + i);
				} else if (str_l > SdaiSession.ENDING_FOR_MAPPING &&
						str.substring(str_l-SdaiSession.ENDING_FOR_MAPPING).equalsIgnoreCase(SdaiSession.MAPPING_NAME_SUFIX)) {
					internal_usage = true;
					model = findSdaiModel(str);
					internal_usage = false;
					if (model == null) {
						model = createModel(str, SdaiSession.mappingSchemaDefinition);
						session.data_mapping.addSdaiModel(model);
						models.addUnorderedRO(model);
						insertModel();
					}
					models_from_compiler_repo[i] = model;
//System.out.println(" SdaiRepository accepted mapping model name: " + str + "   i = " + i);
				} else {
//System.out.println(" SdaiRepository rejected model name: " + str + "   i = " + i);
				}
				count = stream.readInt();
//System.out.println(" SdaiRepository  model id: " + count);
				count = stream.readShort();
//System.out.println(" SdaiRepository  index to underlying schema: " + count);
				bt = stream.readByte();
				if (bt != 'D') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				some_string = stream.readUTF();
//System.out.println(" SdaiRepository  change_date: " + some_string);
				bt = stream.readByte();
				if (bt == 'L') {
					some_string = stream.readUTF();
//System.out.println(" SdaiRepository  default_language: " + some_string);
					bt = stream.readByte();
				}
				if (bt == 'C') {
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
//System.out.println(" SdaiRepository  context_count: " + count);
					for (j = 1; j <= count; j++) {
						some_string = stream.readUTF();
//System.out.println(" SdaiRepository  context: " + some_string);
					}
					bt = stream.readByte();
				}
				byte_accepted = true;
			}

			boolean accepted;
			if (!byte_accepted) {
				bt = stream.readByte();
			}
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			int sch_count = stream.readShort();
//System.out.println(" SdaiRepository  schema instances count: " + sch_count);
			if (sch_count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 0; i < sch_count; i++) {
				bt = stream.readByte();
				if (bt != 'B') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				int str_l = str.length();
				if (str_l <= SdaiSession.ENDING_FOR_INST ||
						!str.substring(str_l-SdaiSession.ENDING_FOR_INST).equalsIgnoreCase(SdaiSession.SCHEMA_INSTANCE_SUFIX)) {
					internal_usage = true;
					sch_instance = findSchemaInstance(str);
					internal_usage = false;
					if (sch_instance != null) {
						accepted = false;
					} else {
						accepted = true;
					}
//System.out.println(" SdaiRepository accepted instance name: " + str);
				} else {
					accepted = false;
//System.out.println(" SdaiRepository rejected instance name: " + str);
				}
				index = stream.readShort();
				if (accepted) {
					if (index < 0 || index >= schemas_count) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					String name_of_def = staticFields.schema_names[index];
//System.out.println(" SdaiRepository  native schemas index: " + index +
//"   name_of_def: " + name_of_def);
					String dict_name = name_of_def.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
					SdaiModel dict_model = findDictionarySdaiModel(dict_name);
					String def_model_name;
					if (str_l > SdaiSession.ENDING_FOR_MAPPING &&
						str.substring(str_l-SdaiSession.ENDING_FOR_MAPPING).equalsIgnoreCase(SdaiSession.MAPPING_NAME_SUFIX)) {
						def_model_name = str.toUpperCase();
					} else {
						def_model_name = str.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
					}
					internal_usage = true;
					SdaiModel def_model = findSdaiModel(def_model_name);
					internal_usage = false;
					if (def_model == null) {
//System.out.println(" SdaiRepository  NOT FOUND!!!!!!!! def_model_name: " + def_model_name +
//"   dict_model name: " + dict_name +
//"   name_of_def: " + name_of_def + "  str: " + str);
						throw new SdaiException(SdaiException.SY_ERR);
					}
//System.out.println(" SdaiRepository  def_model: " + def_model.name);
					sch_instance = new SchemaInstanceDictionaryImpl(str, this, dict_model, def_model);
//System.out.println(" SdaiRepository  sch_instance created: " + sch_instance.name);
//System.out.println(" SdaiRepository ***** DefiningSchema: " + sch_instance.getDefiningSchema().getName(null));
				} else {
					sch_instance = null;
				}
				bt = stream.readByte();
				if (bt != 'D') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				if (accepted) {
					if (str.equals("")) {
						sch_instance.change_date = -1;
					} else {
						sch_instance.change_date = session.cal.timeStampToLong(str);
					}
					sch_instance.validation_date = session.cal.timeStampToLong(stream.readUTF());
					sch_instance.validation_result = (int)stream.readByte();
					sch_instance.validation_level = stream.readShort();
				} else {
					stream.readUTF();
					stream.readByte();
					stream.readShort();
				}
				if (build_number >= BUILD_WITH_SCHEMA_INSTANCE_EXTENSION) {
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						if (accepted) {
							sch_instance.description = new A_string(SdaiSession.listTypeSpecial, sch_instance);
							for (j = 1; j <= count; j++) {
								sch_instance.description.addByIndexPrivate(j, stream.readUTF());
							}
						} else {
							for (j = 1; j <= count; j++) {
								stream.readUTF();
							}
						}
					}
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						if (accepted) {
							sch_instance.author = new A_string(SdaiSession.listTypeSpecial, sch_instance);
							for (j = 1; j <= count; j++) {
								sch_instance.author.addByIndexPrivate(j, stream.readUTF());
							}
						} else {
							for (j = 1; j <= count; j++) {
								stream.readUTF();
							}
						}
					}
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						if (accepted) {
							sch_instance.organization = new A_string(SdaiSession.listTypeSpecial, sch_instance);
							for (j = 1; j <= count; j++) {
								sch_instance.organization.addByIndexPrivate(j, stream.readUTF());
							}
						} else {
							for (j = 1; j <= count; j++) {
								stream.readUTF();
							}
						}
					}
					bt = stream.readByte();
					if (bt == 'P') {
						str = stream.readUTF();
						if (accepted) {
							sch_instance.preprocessor_version = str;
						}
						bt = stream.readByte();
					}
					if (bt == 'O') {
						str = stream.readUTF();
						if (accepted) {
							sch_instance.originating_system = str;
						}
						bt = stream.readByte();
					}
					if (bt == 'A') {
						str = stream.readUTF();
						if (accepted) {
							sch_instance.authorization = str;
						}
						bt = stream.readByte();
					}
					if (bt == 'L') {
						str = stream.readUTF();
						if (accepted) {
							sch_instance.language = str;
						}
						bt = stream.readByte();
					}
					if (bt != 'C') {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						if (accepted) {
							sch_instance.context_identifiers = new A_string(SdaiSession.listTypeSpecial, sch_instance);
							for (j = 1; j <= count; j++) {
								sch_instance.context_identifiers.addByIndexPrivate(j, stream.readUTF());
							}
						} else {
							for (j = 1; j <= count; j++) {
								stream.readUTF();
							}
						}
					}
				}
				count = stream.readShort();
//System.out.println(" SdaiRepository  count of assoc models: " + count);
				if (count < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				for (j = 0; j < count; j++) {
					bt = stream.readByte();
					if (bt == 'L') {
						index = stream.readInt();
						if (accepted) {
							model = models_from_compiler_repo[index];
							if (model != null) {
								model.associated_with.addUnorderedRO(sch_instance);
								if (sch_instance.associated_models == null) {
									sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
								}
								sch_instance.associated_models.addUnorderedRO(model);
//System.out.println(" SdaiRepository  +++++ assoc model added: " + model.name);
							} else {
//System.out.println(" SdaiRepository  ----- assoc model not found    sch_instance.name: " +
//sch_instance.name);
								if (session.logWriterSession != null) {
									session.printlnSession(AdditionalMessages.SS_UMOD + "'" + sch_instance.name + "'");
								} else {
									SdaiSession.println(AdditionalMessages.SS_UMOD + "'" + sch_instance.name + "'");
								}
							}
						}
					} else {
						some_string = stream.readUTF();
						some_string = stream.readUTF();
					}
				}
				if (accepted) {
					schemas.addUnorderedRO(sch_instance);
					insertSchemaInstance();
				}
			}

			bt = stream.readByte();
			if (bt != 'E') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			committed = true;
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}
    

/**
	Extracts models	from the index file produced by Express compiler.
	The models are put into the set 'models'.
    Also sets next persistent label using value from index file.
	This method is invoked in SdaiSession for SystemRepository only.
*/
	void loadModels(Properties index, HashMap models_from_index_files) throws SdaiException {
        String prop_key;
        String str;
        
		SdaiModelDictionaryImpl model;
        
        //-- setting max persistent label from index file -- commented -- 030516 --
        //long maxPL=Long.parseLong(index.getProperty("max_persistent_label", "0"));
        //setNextPersistentLabel(maxPL+1);   //--VV--030311--
        //System.out.println("loadSI > maxPL="+maxPL);

        //-- models
        for (Enumeration e = index.propertyNames(); e.hasMoreElements();) {
            prop_key = e.nextElement().toString();
            str = prop_key.substring(3);
            if (prop_key.startsWith("DM.")) { //-- dictionary model
                //System.out.println("loadSI > SI > dic_model="+str);
                if (!models_from_index_files.containsKey(str)) { //-- creates new model with name from index file.
                    model = (SdaiModelDictionaryImpl)createModel(str, SdaiSession.baseDictionaryModel.described_schema);
                    model.setVersion(Long.parseLong(index.getProperty(prop_key,"-1")));
                    session.data_dictionary.addSdaiModel(model);
                    session.data_mapping.addSdaiModel(model);
                    models.addUnorderedRO(model);
                    insertModel();
                    models_from_index_files.put(str,model);
                }
                else {  //-- if model already exists, MO_DUP exception is thrown
                    throw new SdaiException(SdaiException.MO_DUP, "Dictionary model: " + str);
                }
            }
            if (prop_key.startsWith("MM.")) { //-- mapping model
                //System.out.println("loadSI > SI > map_model="+str);
                if (!models_from_index_files.containsKey(str)) {    //-- creates new model with name from index file.
                    model = (SdaiModelDictionaryImpl)createModel(str, SdaiSession.mappingSchemaDefinition);
                    model.setVersion(Long.parseLong(index.getProperty(prop_key,"-1")));
                    session.data_mapping.addSdaiModel(model);
                    models.addUnorderedRO(model);
                    insertModel();
                    models_from_index_files.put(str, model);
                }
                else {  //-- if model already exists, MO_DUP exception is thrown
                    throw new SdaiException(SdaiException.MO_DUP, "Dictionary model: " + str);
                }
            }
        }
        committed = true;
    }

/**
	Extracts schema instances, corresponding to the Express schemas in JSDAI library,
	from the index file produced by Express compiler.
	The schema instances are put into the set 'schemas'.
	This method is invoked in SdaiSession for SystemRepository only.
*/
	void loadSchemaInstances(Properties index, HashMap models_from_index_files) throws SdaiException {
        
		String prop_key;
        String str;
        
		SdaiModelDictionaryImpl model;
		SchemaInstance sch_instance;
        
        //-- schema instances and imported models
        for (Enumeration e = index.propertyNames(); e.hasMoreElements();) {
            prop_key = e.nextElement().toString();
            str = prop_key.substring(3);
            if (prop_key.startsWith("DS.") || prop_key.startsWith("MS.")) { //-- schema instance
                //System.out.println("loadSI > SI > schema="+str);
                int str_l = str.length();
                String dict_name = "SDAI_DICTIONARY_SCHEMA" + SdaiSession.DICTIONARY_NAME_SUFIX;
                if (prop_key.startsWith("MS.")) {
                    dict_name = "SDAI_MAPPING_SCHEMA" + SdaiSession.DICTIONARY_NAME_SUFIX;
                }
                SdaiModel dict_model = findDictionarySdaiModel(dict_name);
                String def_model_name;
                if (str_l > SdaiSession.ENDING_FOR_MAPPING && str.substring(str_l-SdaiSession.ENDING_FOR_MAPPING).equalsIgnoreCase(SdaiSession.MAPPING_NAME_SUFIX)) {
                    def_model_name = str.toUpperCase();
                } else {
                    def_model_name = str.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
                }
                internal_usage = true;
                SdaiModel def_model = findSdaiModel(def_model_name);
                internal_usage = false;
                if (def_model == null) {
                    throw new SdaiException(SdaiException.SY_ERR);
                }
                sch_instance = new SchemaInstanceDictionaryImpl(str, this, dict_model, def_model);
                
                //-- associated models of schema instance
                StringTokenizer model_list = new StringTokenizer( index.getProperty(prop_key, ""));
                while (model_list.hasMoreTokens()) {
                    str = model_list.nextToken();
                    //System.out.println("loadSI > SI > AM > str="+str);
                    //-- checks for model name (with prefix DM.*, MM.* or IM.*) existance in current index file.
                    if (!index.containsKey("DM."+str) && !index.containsKey("MM."+str) && !index.containsKey("IM."+str)) {
                        throw new SdaiException(SdaiException.MO_NEXS);
                    }
                    model = (SdaiModelDictionaryImpl)models_from_index_files.get(str);
                    if (model != null) { //-- associates model with coresponding schema instance and vise versa 
                        model.associated_with.addUnorderedRO(sch_instance);
                        if (sch_instance.associated_models == null) {
                            sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
                        }
                        sch_instance.associated_models.addUnorderedRO(model);
                    }
                    else {  //-- if associated model is not defined, MO_NEXS exception is thrown
                        throw new SdaiException(SdaiException.MO_NEXS);
                    }
                }
                schemas.addUnorderedRO(sch_instance);
                insertSchemaInstance();
            }
            if (prop_key.startsWith("IM.")) { //-- imported model
                if (!prop_key.equals("IM."+SdaiSession.DICT_MODEL_NAME) && !prop_key.equals("IM."+SdaiSession.MAPP_MODEL_NAME) && !prop_key.equals("IM."+SdaiSession.COMP_MODEL_NAME)) {
                    //System.out.println("loadSI > SI > imported_model="+str);
                    long modelVersion = Long.parseLong(index.getProperty(prop_key,"-1"));
                    SdaiModelDictionaryImpl m = (SdaiModelDictionaryImpl)models_from_index_files.get(str);
                    if (m != null) {
                        if (modelVersion != m.getVersion()) {   //-- validation of imported model version
                            String base = SdaiSession.line_separator + AdditionalMessages.MO_VDIF;
                            throw new SdaiException(SdaiException.SY_ERR, base);
                        }
                    }
                    else {
                        throw new SdaiException(SdaiException.MO_NEXS, prop_key);
                    }
                }
            }
        }
        committed = true;
	}


	protected boolean committingInternal(boolean commit_bridge, boolean modified) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected void abortingInternal(boolean modif, boolean contents_modified) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected boolean isRemote() throws SdaiException {
		return false;
	}


	protected void restoringInternal(SdaiModel model, int mode) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}


	protected Object getNextModelInternal(Object current_member) throws SdaiException {
		return null;
	}


	protected Object getNextSchInstanceInternal(Object current_member) throws SdaiException {
		return null;
	}


	protected int getModCountInternal() throws SdaiException {
		return 0;
	}


	protected SdaiModelHeader takeModelHeaderInternal(Object current_member) throws SdaiException {
		return null;
	}


	protected int getSchInstCountInternal() throws SdaiException {
		return 0;
	}


	protected SchemaInstanceHeader takeSchInstHeaderInternal(Object current_member) throws SdaiException {
		return null;
	}


	protected SdaiModelRemote findSdaiModelBySessionIdentif(long identif) throws SdaiException {
		return null;
	}


	protected boolean updateRemoteModel(SdaiModel model, String name, SdaiModelRemote modRem) throws SdaiException {
		return false;
	}


    public void setNextPersistentLabel( long newPersistentLabel) throws SdaiException {
        newPersistentLabel--;
//        synchronized (syncObject) {
            //System.out.println("setPL > SdaiRepositoryLocalImpl");
            //System.out.println("setPL > MODEL: largest="+largest_identifier);
            if (newPersistentLabel < largest_identifier) {
                //System.out.println("setPL > newPersistentLabel="+newPersistentLabel);
                int i,m;
                SdaiModel model;
                long next_used_pl=-1;
                for (i = 0; i < models.myLength; i++) {
                    model = (SdaiModel)models.myData[i];
                    //System.out.println("setPL > MODEL: name="+model.getName());
                    if (model.inst_idents != null) {
                        /*System.out.print("setPL > MODEL: PLs=");
                        for(int l=0; l<model.inst_idents.length; l++) {
                            System.out.print( model.inst_idents[l]+" ");
                        }
                        System.out.println();*/
                        int idx = Arrays.binarySearch( model.inst_idents, newPersistentLabel+1);
                        //System.out.println("setPL > MODEL: idx="+idx);
                        if (idx < 0) {      // pl is not found in model
                            idx = -idx -1;  // index of closest bigger pl
                            //System.out.println("setPL > MODEL: inst_idents.length="+model.inst_idents.length);
                            if (idx < model.inst_idents.length) {   // if index is equal to array length, then there is no bigger pl
                                if (model.inst_idents[idx] < next_used_pl || next_used_pl == -1) {    // choosing minimal of used bigger pl
                                    next_used_pl = model.inst_idents[idx];
                                    //System.out.println("setPL > MODEL: next_used_pl="+next_used_pl);
                                }
                            }
                        }
                        else {              // pl is found in model
                            throw new SdaiException(SdaiException.VA_NVLD);
                        }
                    }
                }
                
                next_used_identifier = next_used_pl;
            }
            else {
                largest_identifier = newPersistentLabel;
                next_used_identifier = -1;
            }
            current_identifier = newPersistentLabel;
            //System.out.println("setPL > MODEL: current="+current_identifier);
            //System.out.println("setPL > MODEL: next_used="+next_used_identifier);
            //System.out.println("setPL > MODEL: largest="+largest_identifier);                
//		} // syncObject
    }
    
}
