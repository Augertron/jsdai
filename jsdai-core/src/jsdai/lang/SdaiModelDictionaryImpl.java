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
import java.io.*;

/**
 *
 */
class SdaiModelDictionaryImpl extends SdaiModel {

    
/**
	Version of model.
*/
	public long version;

/**
	This internal array is introduced to collect data dictionary models which 
	contain entities or defined types declared(referenced, used, or 
	implicitly interfaced) in this SdaiModel.
	Of course, if this array is used, then this model shall be data dictionary
	one (belong to SystemRepository).
*/
	private SdaiModel [] aInterfacedModel;

/**
	The count of models in the array 'aInterfacedModel'.
*/
	private int iInterfacedModel;

/**
	The initial length of an internal array 'aInterfacedModel' (see below in this class).
*/
	private final int LIMIT_ON_INTERFACED_MOD = 200;

/**
	The following matrix may be nonempty only for data dictionary models.
	It gives the names of other EXPRESS schemas which are represented by 
	this (data dictionary) SdaiModel. Alternatively, each of these names 
	may be an alias of the EXPRESS schema described by the model.
	Also, the model may represent a combination of two or more EXPRESS schemas.
	The names are extracted from the jsdai.properties file. 
   Each name, including the last one, is followed by a semicolon. 
   The names in the combination are separated by commas. 
	Each name or a combination of names is contained in a single row 
	of the matrix.
	Example.
	Assume that this model contains 'automotive_design' data and 
	jsdai.properties file includes the following statement:
	jsdai.SAutomotive_design=AUTOMOTIVE_DESIGN;AUTOMOTIVE_DESIGN_CC1;
   	     AUTOMOTIVE_DESIGN_CC06;XXX,CCC,FFF;PDM_SCHEMA;
	Then 
		property_schemas[0][0] has value "AUTOMOTIVE_DESIGN",
		property_schemas[1][0] has value "AUTOMOTIVE_DESIGN_CC1",
		property_schemas[2][0] has value "AUTOMOTIVE_DESIGN_CC06",
		property_schemas[3][0] has value "XXX",
		property_schemas[3][1] has value "CCC",
		property_schemas[3][2] has value "FFF",
		property_schemas[4][0] has value "PDM_SCHEMA".
*/
	String [] property_schemas [];



	/** Used to create application (through the method createSdaiModel) and dictionary
	 *  SdaiModels.
	 *  Also using this constructor some special SdaiModel called sessionModel is created.
	 */
	SdaiModelDictionaryImpl(String model_name, CSchema_definition schema, SdaiRepository rep) throws SdaiException {
		super(model_name, schema, rep);
	}
	
	/** Used to create an SdaiModel when reading a repository binary file.
	 */
	SdaiModelDictionaryImpl(String model_name, SdaiRepository rep, SdaiModel dict) throws SdaiException {
		super(model_name, rep, dict);
	}
	
	/** Used to create an SdaiModel when in the binary file being loaded the name of
	 *  a model and its repository (or only its name) are found but this repository
	 *  misses a model with this given name.
	 *  Also, used to create virtual models.
	 */
	SdaiModelDictionaryImpl(String model_name, SdaiRepository rep) {
		super(model_name, rep);
	}

	protected void initializeContents() throws SdaiException {
		initializeDictionaryModelContents();
	}

	protected boolean committingHeaderInternal(boolean commit_bridge,
											   boolean modif_remote) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repostory");
	} 

	protected boolean committingInternal(boolean b1, boolean b2, boolean b3) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repostory");
	}

	protected boolean deletingInternal(boolean commit_bridge, SdaiRepository repo) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repostory");
	}

	protected boolean abortingInternal(SdaiTransaction trans, boolean modif, 
			boolean modif_outside_contents, boolean endTransaction) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL, 
			"Method is not available for the system repository.");
	}

	protected boolean isRemoteInternal() throws SdaiException {
		return false;
	}

	protected boolean checkModelInternal(Object current_member) throws SdaiException {
		return false;
	}

	protected void attachRemoteModel(Object current_member) throws SdaiException {
	}

	protected void endingAccess() throws SdaiException {
	}
	
	protected void entityDeletedInternal(CEntity instance) throws SdaiException {
		throw new SdaiException(SdaiException.SS_NAVL, 
			"This method can not be used with system repostory");
	}

	public SdaiPermission checkPermission() {
		return SdaiPermission.READ;
	}

	public void checkRead() {
		// Dictionary model always has read permission
	}

	public void checkWrite() throws SdaiException {
		throw new SdaiException(SdaiException.SY_SEC, "Dictionary model can not be modified");
	}

	public void deleteSdaiModel() throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD,
			"Models in the system repository cannot be deleted by the user.");
	}

	public int getMode() throws SdaiException {
		synchronized (SdaiSession.systemRepository) {
			return super.getMode();
		}
	}

	public void startReadOnlyAccess() throws SdaiException {
		synchronized (SdaiSession.systemRepository) {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			return; // This is a workaround for multithreaded access,
			        // since testing for NO_ACCESS and then invoking startReadOnlyAccess
			        // is not safe
			//throw new SdaiException(SdaiException.MX_RO, this);
		}
//System.out.println("SdaiModelDictionaryImpl ?????? before initializeContents() model: " + name);
		initializeContents();
		setMode(READ_ONLY);
		if (this != SdaiSession.baseDictionaryModel) {
			String str;
			if (this == SdaiSession.baseMappingModel) {
				str = SdaiSession.MAPPING_PACKAGE_SPEC;
			} else {
				if (underlying_schema == SdaiSession.mappingSchemaDefinition) {
					int ln = name.length() - SdaiSession.ENDING_FOR_MAPPING;
					str = name.substring(0, ln);
					str = SdaiSession.MAPPING_PREFIX_SPEC + CEntity.normalise(str) + "/";
				} else {
					int ln = name.length() - SdaiSession.ENDING_FOR_DICT;
					str = name.substring(0, ln);
					str = SdaiSession.SCHEMA_PREFIX_SPEC + CEntity.normalise(str) + "/";
				}
			}
			if (schemaData == null) {
				schemaData = new SchemaData(this);
			}
			try {
				loadResource(str);
				for (int i = 0; i < lengths[SdaiSession.ENTITY_DEFINITION]; i++) {
					((DataType)instances_sim[SdaiSession.ENTITY_DEFINITION][i]).express_type = DataType.ENTITY;
				}
				if (underlying_schema != SdaiSession.mappingSchemaDefinition) {
					CSchema_definition new_sch = (CSchema_definition)instances_sim[SdaiSession.SCHEMA_DEFINITION][0];
					described_schema = new_sch;
					schemaData.schema = new_sch;
					new_sch.modelDictionary = this;
//System.out.println("SdaiModelDictionaryImpl ++++++ before initSchema() model: " + name);
					initSchema();
				}
			} catch (SdaiException ex) {
				mode = NO_ACCESS;
//System.out.println("SdaiModelDictionaryImpl !!!!!!!!!!!!!!!!!!! SdaiException is thrown  for model: " + name 
//+ "  mode: " + mode + "  error_id: " + ex.getErrorId());
				throw ex;
			}
		}
		repository.session.active_models.addUnorderedRO(this);
		} // SdaiSession.systemRepository
	}


	public void startReadWriteAccess() throws SdaiException {
//		synchronized (syncObject) {
			throw new SdaiException(SdaiException.FN_NAVL,
				"The system repository cannot be modified by the user.");
//		} // syncObject
	}
    
    public void startPartialReadOnlyAccess() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"Partial access is not available for dictionary model " + name);
    }

    public void startPartialReadWriteAccess() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"Partial access is not available for dictionary model " + name);
    }

	public void promoteSdaiModelToRW() throws SdaiException {
//		synchronized (syncObject) {
			if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, this);
			}
			throw new SdaiException(SdaiException.FN_NAVL,
				"The system repository cannot be modified by the user.");
//		} // syncObject
	}


	public void reduceSdaiModelToRO() throws SdaiException {
//		synchronized (syncObject) {
			if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, this);
			}
			if ((mode & MODE_MODE_MASK) == READ_ONLY) {
				throw new SdaiException(SdaiException.MX_RO, this);
			}
//		} // syncObject
	}


	public void endReadOnlyAccess() throws SdaiException {
//		synchronized (syncObject) {
			throw new SdaiException(SdaiException.FN_NAVL,
				"A model in the system repository cannot be closed by the user.");
//		} // syncObject
	}


    private long loadStream(DataInput stream, int mode_to_start) throws SdaiException {
        //System.out.println("DictImpl model="+getName());
        //long lST, lCT;
        //lST = System.currentTimeMillis();
        long res = loadStreamCommon(stream, mode_to_start);
        //lCT = System.currentTimeMillis();
        //System.out.println("Time="+(float)(lCT-lST)/1000);
		return res;
	}

	public void endReadWriteAccess() throws SdaiException {
//		synchronized (syncObject) {
			throw new SdaiException(SdaiException.FN_NAVL,
				"A model in the system repository cannot be closed by the user.");
//		} // syncObject
	}


//	static long timeLoadResource = 0;
/**
	Loads the data contained in the binary file to this SdaiModel when this SdaiModel 
	is a data dictionary one (and belongs to SystemRepository).
*/
	void loadResource(String superClassName) throws SdaiException {
//		long time = System.currentTimeMillis();
		Class cl;
		if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			ex_models = new SdaiModel[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
		String full_name = superClassName + name;
		InputStream istream;
		ClassLoader classLoader = SdaiClassLoaderProvider.getDefault().getClassLoader();
		istream = classLoader.getResourceAsStream(full_name);
		if (istream == null) {
			bin_file_missing = true;
			int ending;
			if (underlying_schema == repository.session.mappingSchemaDefinition) {
				ending = SdaiSession.ENDING_FOR_MAPPING;
			} else {
				ending = SdaiSession.ENDING_FOR_DICT;
			}
			String sch_name = name.substring(0, name.length() - ending).toLowerCase();
			String text = SdaiSession.line_separator + AdditionalMessages.BF_DDM1 +
				sch_name + AdditionalMessages.BF_DDM2 + " (" + full_name + ")";
//			throw new SdaiException(SdaiException.SY_ERR, base);
			Object base[] = new Object[2];
			base[0] = new Integer(1);
			base[1] = sch_name;
			throw new SdaiException(SdaiException.SY_ERR, base, text);
		}
		bin_file_missing = false;
		try {
			DataInput stream = new DataInputStream(new BufferedInputStream(istream));
			try {
				loadStream(stream, READ_ONLY);
			} finally {
				((DataInputStream)stream).close();
				istream.close();
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		resolveInConnectors(false);
//		time = System.currentTimeMillis() - time;
//		timeLoadResource += time;
//System.out.println("load +" + time + "=" + timeLoadResource + " <" + name + ">");
	}


/**
	Prepares data dictionary information for this SdaiModel and all models in 
	the array 'aInterfacedModel' constructed (repeatedly) using method 
	collectInterfacedModels. The data for this preparation is loaded from the 
	binary file using method loadResource.
*/
	private void initSchema() throws SdaiException {
//		long time1, time2, time3, time4, time5;
//		time1 = System.currentTimeMillis();

		iInterfacedModel = 1;
		if (aInterfacedModel == null) {
			aInterfacedModel = new SdaiModel[LIMIT_ON_INTERFACED_MOD];
		}
		aInterfacedModel[0] = this;
		if (this != SdaiSession.baseComplexModel) {
			collectInterfacedModels(SdaiSession.ENTITY_DECL_REFERENCED_DECL);
			collectInterfacedModels(SdaiSession.ENTITY_DECL_USED_DECL);
			collectInterfacedModels(SdaiSession.ENTITY_DECL_IMPLICIT_DECL);
			collectInterfacedModels(SdaiSession.REFERENCED_DECL_TYPE_DECL);
			collectInterfacedModels(SdaiSession.TYPE_DECL_USED_DECL);
			collectInterfacedModels(SdaiSession.IMPLICIT_DECL_TYPE_DECL);
		}
		// open also all other directly or indirectly interfaced schemas
		// we use all defined schemas, needs to be improved later
		int i;
		SdaiModel m;
//		time2 = System.currentTimeMillis();
		for (i = 0; i < iInterfacedModel; i++) {
			m = aInterfacedModel[i];
//System.out.println(" SdaiModel ::::::: m = " + m.name +
//"   m.schemaData.noOfEntityDataTypes = " + m.schemaData.noOfEntityDataTypes);
			if (m != SdaiSession.baseDictionaryModel && m.schemaData.noOfEntityDataTypes < 0) {
				int count = m.lengths[SdaiSession.ENTITY_DECL_LOCAL_DECL];
				if (m != SdaiSession.baseComplexModel) {
					count +=
					  m.lengths[SdaiSession.ENTITY_DECL_REFERENCED_DECL] +
					  m.lengths[SdaiSession.ENTITY_DECL_USED_DECL] +
					  m.lengths[SdaiSession.ENTITY_DECL_IMPLICIT_DECL];
				}
				m.schemaData.initializeSchemaData(count, count);
				m.schemaData.init();
//System.out.println("SdaiModelDictionaryImpl^^^^^^^After init() MODEL: " + m.name + "    count: " + count);
			}
		}

//		time3 = System.currentTimeMillis();
		for (i = 0; i < iInterfacedModel; i++) {
			m = aInterfacedModel[i];
			if (m != SdaiSession.baseDictionaryModel && !(m.early_binding_linked_init) &&
             	m.described_schema != null) {
				if (m == this) {
					m.schemaData.linkEarlyBindingInit(null);
				} else {
					m.schemaData.linkEarlyBindingInit(this);
				}
				m.early_binding_linked_init = true;
			}
		}

//		time4 = System.currentTimeMillis();
		for (i = 0; i < iInterfacedModel; i++) {
			m = aInterfacedModel[i];
			if (m != SdaiSession.baseDictionaryModel && !(m.early_binding_linked) &&
					m.described_schema != null) {
				if (m == this) {
					m.schemaData.linkEarlyBinding(null);
				} else {
					m.schemaData.linkEarlyBinding(this);
				}
				m.early_binding_linked = true;
			}
		}

//		time5 = System.currentTimeMillis();
//SdaiSession.println("initSchema =" + name + ":" +
//(time2 - time1)/1000 + "+" +
//(time3 - time2)/1000 + "+" +
//(time4 - time3)/1000 + "+" +
//(time5 - time4)/1000 + "=" +
//(time5 - time1)/1000 + " sec");
	}


/**
	Appends to the specified array all (data dictionary) models which 
	are owning for entities and defined types declared (but not defined) 
	in the schema described by this (also data dictionary) SdaiModel.
*/
	private void collectInterfacedModels(int index) throws SdaiException {
		CEntity [] instances = instances_sim[index];
		for (int i = 0; i < lengths[index]; i++) {
			EDeclaration decl = (EDeclaration)instances[i];
			CEntity interfacedType = (CEntity)decl.getDefinition(null);
			SdaiModel m = interfacedType.owning_model;
			if (!m.initialized && !m.early_binding_linking) {
				if (iInterfacedModel >= aInterfacedModel.length) {
					int new_length = iInterfacedModel * 2;
					SdaiModel [] new_mods = new SdaiModel[new_length];
					System.arraycopy(aInterfacedModel, 0, new_mods, 0, aInterfacedModel.length);
					aInterfacedModel = new_mods;
				}
				aInterfacedModel[iInterfacedModel++] = m;
				m.initialized = true;
			}
		}
	}


/**
	Prepares the array inst_idents consisting of identifiers of all instances 
   contained in this model under the condition that this model belongs to 
   SystemRepository. The array is created by reading the beginning of 
   the model's binary file. In the case of exceptions a warning message is 
   printed and the array is made empty. This method is nontrivially executed 
   only for binary files formed with build 249 or later. Otherwise, again the 
   array is left empty.	The method is invoked only in one method: 
   getSessionIdentifier.
*/
	void loadInstanceIdentifiersSystemRepo() throws SdaiException {
		synchronized (SdaiSession.systemRepository) {
			if (inst_idents != null) {
				return;
			}
			int ln;
			String full_name;
			if (underlying_schema == SdaiSession.mappingSchemaDefinition) {
				ln = name.length() - SdaiSession.ENDING_FOR_MAPPING;
				full_name = SdaiSession.MAPPING_PREFIX_SPEC + CEntity.normalise(name.substring(0, ln)) + "/" + name;
			} else {
				ln = name.length() - SdaiSession.ENDING_FOR_DICT;
				full_name = SdaiSession.SCHEMA_PREFIX_SPEC + CEntity.normalise(name.substring(0, ln)) + "/" + name;
			}

			int ending;
			String sch_name;
			InputStream istream;
			ClassLoader classLoader = SdaiClassLoaderProvider.getDefault().getClassLoader();
			istream = classLoader.getResourceAsStream(full_name);
			if (istream == null) {
				if (underlying_schema == SdaiSession.mappingSchemaDefinition) {
					ending = SdaiSession.ENDING_FOR_MAPPING;
				} else {
					ending = SdaiSession.ENDING_FOR_DICT;
				}
				sch_name = name.substring(0, name.length() - ending).toLowerCase();
				if (repository.session.logWriterSession != null) {
					repository.session.printlnSession(AdditionalMessages.BF_DDM1 + sch_name + AdditionalMessages.BF_DDM2);
				} else {
					SdaiSession.println(AdditionalMessages.BF_DDM1 + sch_name + AdditionalMessages.BF_DDM2);
				}
				all_inst_count = 0;
				return;
			}
			try {
				DataInput stream = new DataInputStream(new BufferedInputStream(istream));
				try {
					loadInstanceIdentifiers(stream);
				} finally {
					((DataInputStream)stream).close();
					istream.close();
				}
			} catch (IOException ex) {
				if (underlying_schema == SdaiSession.mappingSchemaDefinition) {
					ending = SdaiSession.ENDING_FOR_MAPPING;
				} else {
					ending = SdaiSession.ENDING_FOR_DICT;
				}
				sch_name = name.substring(0, name.length() - ending).toLowerCase();
				if (repository.session.logWriterSession != null) {
					repository.session.printlnSession(AdditionalMessages.BF_DDE1 + sch_name + AdditionalMessages.BF_DDE2);
				} else {
					SdaiSession.println(AdditionalMessages.BF_DDE1 + sch_name + AdditionalMessages.BF_DDE2);
				}
				all_inst_count = 0;
			}
		}
	}


/**
	Creates entity extents for baseDictionaryModel. 
	Used in the constructor of SdaiSession.
*/
	void formFoldersForBaseDictModel() throws SdaiException {
		if (folders == null) {
			createFolders();
		}
		for (int i = 0; i < schemaData.noOfEntityDataTypes; i++) {
			CEntity_definition def = (CEntity_definition)schemaData.entities[i];
			if (def.complex != 2) {
				EntityExtent extent = new EntityExtent(def, this, i);
				folders.addUnorderedRO(extent);
			}
		}
	}


/**
	Gets model version. 
*/
    long getVersion() {
        return version;
    }
    
/**
	Sets model version. 
*/
    void setVersion(long version) {
        this.version = version;
    }
    
    protected Connector newConnector(SdaiModel model, String entityName, long instanceIdentifier, CEntity owningInstance) throws SdaiException {
        return new SdaiModelLocalImpl.ConnectorLocalImpl(model, entityName, instanceIdentifier, owningInstance);
    }

    protected Connector newConnector(SdaiRepository repository, long modelId, long instanceIdentifier,
									 CEntity owningInstance) throws SdaiException {
		throw new SdaiException(SdaiException.SY_ERR,
								"Dictionary model can not handle this type of connector");
	}

    protected Connector newConnector(long repositoryId, long modelId, long instanceIdentifier,
									 CEntity owningInstance) throws SdaiException {
		throw new SdaiException(SdaiException.SY_ERR,
								"Dictionary model can not handle this type of connector");
	}

}
