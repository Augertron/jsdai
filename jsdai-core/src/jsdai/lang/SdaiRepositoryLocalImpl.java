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

import java.io.*;
import java.util.*;

import jsdai.dictionary.*;
import jsdai.client.*;

/**
 * Local SdaiRepository implementation
 */

class SdaiRepositoryLocalImpl extends SdaiRepository {
	Properties extDataProps = null;
	//    static String rep_path;     // path to concrete repository
	static final String line_separator = System.getProperty("line.separator");

	/**
	   The (standard) name of the binary file for repository.
	*/
	private static final String REPOSITORY_FILE_NAME = "repository";


	SdaiRepositoryLocalImpl(SdaiSession session, String name, Object location,
							boolean fCheckNameLocation) throws SdaiException {
		super(session, name, location, fCheckNameLocation);
	}

	SdaiRepositoryLocalImpl(SdaiSession session, String name) throws SdaiException {
		super(session, name);
	}

	protected SdaiModel createModel(String model_name, CSchema_definition schema) throws SdaiException {
		return new SdaiModelLocalImpl(model_name, schema, this);
	}

	protected SdaiModel createModel(String model_name) {
		return new SdaiModelLocalImpl(model_name, this);
	}

	protected SdaiModel createModel(String model_name, SdaiModel dict) throws SdaiException {
		return new SdaiModelLocalImpl(model_name, this, dict);
	}

	/**
	 * Creates a repository called 'virtual'. In this repository only
	 * the following fields from those defined in Part 22 are set with values:
	 * - name;
	 * - session;
	 * - models.
	 * This method is used to create a repository for the repository name
	 * found when reading model's binary file provided there is no repository
	 * with this name in the set 'known_servers'.
	 * The method is invoked in one of variations of extractModel in class
	 * SdaiModel.
	 */
	protected SdaiRepository createVirtualRepository(String name) throws SdaiException {
		SdaiRepository repo = new SdaiRepositoryLocalImpl(session, name);
		addVirtualRepository(repo);
		return repo;
	}


	/* finalize by calling commit() for all entities */
	protected void commitExternalData() throws SdaiException {
		getExtDataProps();
		Iterator externalDataIter;

		if(entityRemovedExternalData != null) {
			externalDataIter = entityRemovedExternalData.values().iterator();
			while(externalDataIter.hasNext()) {
				Object externalDataObject = externalDataIter.next();
				if(externalDataObject instanceof Long) {
					removeEntityExternalDataLocalImpl(((Long)externalDataObject).longValue());
				} else if(externalDataObject instanceof CEntity) {
					removeEntityExternalDataLocalImpl(((CEntity)externalDataObject).instance_identifier);
				} else {
					ExternalDataLocalImpl externalData =
						(ExternalDataLocalImpl)externalDataObject;
					externalData.removed();
					removeEntityExternalDataLocalImpl(externalData);
				}
			}
			entityRemovedExternalData.clear();
		}

		if(entityExternalData != null) {
			externalDataIter = entityExternalData.values().iterator();
			while(externalDataIter.hasNext()) {
				ExternalData externalData = (ExternalData)externalDataIter.next();
				externalData.commit();
			}
		}
		updateExtDataProperties();
	}

	/* deletes external data associated with given entity */
	private void removeEntityExternalDataLocalImpl(long instIdent)
		throws SdaiException {

		String str_inst_id = String.valueOf(instIdent);
		File removeFile = new File(this.getLocation(), "e"+ str_inst_id);
		removeFile.delete();

		//delete entry from extdata.properties file
		extDataProps.remove(str_inst_id);
	}

	/* deletes what we have just created */
	private void removeEntityExternalDataLocalImpl(ExternalDataLocalImpl externalDataObject)
		throws SdaiException {

		File f = new File(this.getLocation() , "extdata.properties");
		if(f.exists()){
			if((externalDataObject.getFD() != null) && externalDataObject.getFD().exists()){
				externalDataObject.getFD().delete();
				//System.out.println("removed2");
			}
			externalDataObject.unsetFD();
			extDataProps.remove(String.valueOf(((CEntity)externalDataObject.getInstance()).instance_identifier));
		}
	}

	/* creates/gets external data for entity according to existing parameter, if its */
	/* "true" - that means external data for entity already exist and only needs to be taken,*/
	/* if its "false" - we create and return external data */
	protected ExternalData createNewEntityExternalData(CEntity entity, boolean existing)
		throws SdaiException {

		ExternalDataLocalImpl newEntityExternalData = null;
		Long instanceIdentifier = new Long(entity.instance_identifier);
		File fileD = new File( this.getLocation(), "e" + instanceIdentifier);

		if(existing){
			String name = getExtDataProps().getProperty(String.valueOf(entity.instance_identifier));

			if (name == null) {
				throw new SdaiException(SdaiException.VA_NSET, "ExternalData is not set for this instance");
			}

			if(entityExternalData != null){
				newEntityExternalData = (ExternalDataLocalImpl) entityExternalData.get(instanceIdentifier);
			}

			if(newEntityExternalData == null){
				newEntityExternalData = new ExternalDataLocalImpl(entity, false);
				entityExternalData.put(instanceIdentifier, newEntityExternalData);
			}
		}else{
// 			if(fileD.exists()) {
// 				fileD.delete();
// 			}
			if(entityExternalData.containsKey(instanceIdentifier)){
				throw new SdaiException(SdaiException.SI_DUP);
			}else{
				getExtDataProps().setProperty(String.valueOf(instanceIdentifier), "");
				newEntityExternalData = new ExternalDataLocalImpl(entity, true);
				entityExternalData.put(instanceIdentifier, newEntityExternalData);
			}
		}

		newEntityExternalData.setFD(fileD);
		//if(entityRemovedExternalData != null) entityRemovedExternalData.remove(instanceIdentifier);

		return newEntityExternalData;
	}

	/* checks if given enity has external data */
	protected boolean testNewEntityExternalData(CEntity entity) throws SdaiException {
		String name = getExtDataProps().getProperty(String.valueOf(entity.instance_identifier));
		if (name == null) {
			return false;
		}
		return true;
	}

	boolean testAndRemoveEntityExternalData(long instIdent) throws SdaiException {
		if(entityRemovedExternalData == null) {
			entityRemovedExternalData = new HashMap();
		}
		Long instIdentObject = new Long(instIdent);
		Object externalDataObject = (entityExternalData != null) ?
			entityExternalData.remove(instIdentObject) : null;
		if(externalDataObject == null) {
			if(!entityRemovedExternalData.containsKey(instIdentObject)
			   && getExtDataProps().containsKey(String.valueOf(instIdent))) {
				entityRemovedExternalData.put(instIdentObject, instIdentObject);
				return true;
			}
		} else {
			entityRemovedExternalData.put(instIdentObject, externalDataObject);
			((ExternalData)externalDataObject).removed();
			return true;
		}
		return false;
	}

	/**
	 * Returns an object of java class Properties obtained
	 * from the file 'extdata.properties'.
	 * This file belongs to the conrete repository directory.
	 */

	protected Properties takeExtDataProperties() throws SdaiException {
		extDataProps = new Properties();
		File f = new File(this.getLocation(), "extdata.properties");

		if (f.exists()) {
			try {
				InputStream istr = new BufferedInputStream(new FileInputStream(f));
				extDataProps.load(istr);
				istr.close();
			} catch (FileNotFoundException e) {
				String base = line_separator + AdditionalMessages.IO_IOFF;
				throw new SdaiException(SdaiException.SY_ERR, base, e);
			} catch (IOException e) {
				String base = line_separator + AdditionalMessages.IO_ERRP;
				throw new SdaiException(SdaiException.SY_ERR, base, e);
			}
		}
		return extDataProps;
	}

	/**
	 * Saves updated properties to "extdata.properties" file in the special
	 * repositories directory.
	 */
	protected void updateExtDataProperties() throws SdaiException {
		File f = new File(this.getLocation() , "extdata.properties");
		try {
			OutputStream ostr = new BufferedOutputStream(new FileOutputStream(f));
			extDataProps.store(ostr, "External data properties");
			ostr.close();
		} catch (FileNotFoundException e) {
			String base = line_separator + AdditionalMessages.IO_IOFF;
			throw new SdaiException(SdaiException.SY_ERR, base, e);
		} catch (IOException e) {
			String base = line_separator + AdditionalMessages.IO_ERWP;
			throw new SdaiException(SdaiException.SY_ERR, base, e);
		}
	}

	Properties getExtDataProps() throws SdaiException {
		if(extDataProps == null)  extDataProps =  takeExtDataProperties();
		return extDataProps;
	}

	protected HashMap getEntityRemovedExternalData(){
		return entityRemovedExternalData;
	}

	protected boolean doCheckCreateSchemaInstance(ESchema_definition nativeSchema) {
		return true;
	}

	protected boolean doCheckCreateSdaiModel(ESchema_definition underlyingSchema)  {
		return true;
	}

	public SdaiPermission checkPermission() {
		return SdaiPermission.WRITE;
	}

	public void checkRead() {
		// Local repository always has read permission
	}

	public void checkWrite() {
		// Local repository always has write permission
	}

	public void openRepository() throws SdaiException {
//		synchronized (syncObject) {
			if (session == null) {
				throw new SdaiException(SdaiException.RP_NEXS);
			}
			if(Implementation.userConcurrencyCompatibility == 1 && session.active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}

			int i;
			if (feature_level == 0) {
				int count = 0;
				for (i = 0; i < session.active_servers.myLength; i++) {
					if (session.active_servers.myData[i] != SdaiSession.systemRepository) {
						count++;
					}
				}
				if (count >= 1) {
					String base = SdaiSession.line_separator + AdditionalMessages.FL_ORZE;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
			if (virtual) {
				throw new SdaiException(SdaiException.RP_NAVL, this);
			}
			if (active) {
				throw new SdaiException(SdaiException.RP_OPN, this);
			}
			if (committed || !created_or_imported) {
				loadRepositoryLocal();
				if (build_number < 220) {
					checkModels();
				}
			}
			active = true;
			modified = false;
			session.active_servers.addUnorderedRO(this);
			for (i = 0; i < models.myLength; i++) {
				SdaiModel model = (SdaiModel)models.myData[i];
				model.committed = true;
			}
			for (i = 0; i < schemas.myLength; i++) {
				SchemaInstance sch = (SchemaInstance)schemas.myData[i];
				sch.committed = true;
			}
//		} // syncObject
	}


	/**
	   Runs through all binary files in the directory of this repository
	   and changes the names of the files with model data to the form "mx",
	   where 'x' is some positive integer called identifier.
	*/
	private void checkModels() throws SdaiException {
		File dir = new File((String)location);
		if (dir.exists()) {
			String [] files = dir.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].equals(REPOSITORY_FILE_NAME) ||
					files[i].equals("header") || files[i].equals("contents")) {
					continue;
				}
				if (files[i].substring(0,1).equals("m")) {
					try {
						int numb = Integer.parseInt(files[i].substring(1));
						if (numb > 0) {
							continue;
						}
					} catch (NumberFormatException ex) {
					}
				}
				internal_usage = true;
				SdaiModel model_matched = findSdaiModel(files[i]);
				internal_usage = false;
				int id;
				if (model_matched == null) {
					model_count++;
					id = model_count;
				} else {
					if (model_matched.identifier <= 0) {
						model_count++;
						model_matched.identifier = model_count;
					}
					id = model_matched.identifier;
				}
				String file_name = "m" + id;
				File mod_file = new File(dir, files[i]);
				File new_mod_file = new File(dir, file_name);
				if (!mod_file.renameTo(new_mod_file)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


	public SdaiModel createSdaiModel(String model_name, ESchema_definition schema) throws  SdaiException {
//		synchronized (syncObject) {
			createSdaiModelCommonChecking(model_name, schema, false, model_created_during_simulation ?
										  SdaiTransaction.READ_ONLY : SdaiTransaction.READ_WRITE);
			return createSdaiModelCommonCreating(model_name, schema, false);
//		} // syncObject
	}


	public SdaiModel createSdaiModel(String model_name, Class schema) throws SdaiException {
//		synchronized (syncObject) {
			if (schema == null) {
				throw new SdaiException(SdaiException.SD_NDEF);
			}
			if (session == null) {
				throw new SdaiException(SdaiException.RP_NEXS);
			}
			CSchema_definition schema_def = session.getSchemaDefinition(schema);
			if (schema_def != null) {
				createSdaiModelCommonChecking(model_name, schema_def, false,
											  model_created_during_simulation ?
											  SdaiTransaction.READ_ONLY : SdaiTransaction.READ_WRITE);
				return createSdaiModelCommonCreating(model_name, schema_def, false);
			}
//		} // syncObject
		throw new SdaiException(SdaiException.SD_NDEF);
	}


	public SdaiModel findSdaiModel(String name) throws SdaiException {
//		synchronized (syncObject) {
			return findSdaiModelCommon(name);
//		} // syncObject
	}


	public SchemaInstance createSchemaInstance(String name, ESchema_definition schema) throws  SdaiException {
//		synchronized (syncObject) {
			createSchemaInstanceCommon(name, schema, false, sch_inst_created_during_simulation ?
									   SdaiTransaction.READ_ONLY : SdaiTransaction.READ_WRITE);
			SchemaInstance instance = new SchemaInstanceLocalImpl(name, (CSchema_definition)schema, this);
			postCreateSchemaInstance(instance, false);
			return instance;
//		} // syncObject
	}


	public SchemaInstance createSchemaInstance(String name, Class schema) throws SdaiException {
//		synchronized (syncObject) {
			if (schema == null) {
				throw new SdaiException(SdaiException.SD_NDEF);
			}
			if (session == null) {
				throw new SdaiException(SdaiException.RP_NEXS);
			}
			CSchema_definition schema_def = session.getSchemaDefinition(schema);
			return createSchemaInstance(name, schema_def);
//		} // syncObject
	}


	public SchemaInstance findSchemaInstance(String name) throws SdaiException {
//		synchronized (syncObject) {
			return findSchemaInstanceCommon(name);
//		} // syncObject
	}


	protected void closeRepositoryInternal(boolean delete_repo) throws SdaiException {
		closeRepositoryCommon(delete_repo);
	}


	public EEntity getSessionIdentifier(String label) throws SdaiException {
//		synchronized (syncObject) {
			long identifier = getSessionIdentifierInit(label);
			EEntity instance = getSessionIdentifierCommon(identifier);
			if(instance == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			} else {
				return instance;
			}
//		} // syncObject
	}


	protected void deleteRepositoryInternal() throws SdaiException {
		deleteRepositoryCommon(false);
		String directory_name;
		if (session.repos_path != null) {
			directory_name = session.repos_path + java.io.File.separatorChar + dir_name;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.SS_NODR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		File dir = new File(directory_name);
		boolean return_value = true;
		if (dir.isDirectory()) {
			String [] files_in_dir = dir.list();
			for (int i = 0; i < files_in_dir.length; i++) {
				File f = new File(dir, files_in_dir[i]);
					return_value = f.delete();
				if (!return_value) {
					break;
				}
			}
			if (return_value) {
				return_value = dir.delete();
			}
		}
		if (!return_value) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_DELF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (dir_name != null) {
			SdaiSession.removeFromProperties(this);
		}
		session = null;
	}


	/**
	   Writes the data contained in this repository but not in models within it to the
	   binary file when this repository is a local one.
	*/
	void saveRepositoryLocal() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		//		if (!active) {
		//			throw new SdaiException(SdaiException.RP_NOPN, this);
		//		}
		if (location == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		try {
			File dir = new File((String)location);
			boolean return_value = true;
			if (!dir.isDirectory()) {
				return_value = dir.mkdir();
			}
			if (!return_value) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NOD1 +
					(String)location + AdditionalMessages.BF_NOD2;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			File handle = new File(dir, "repository");
			FileOutputStream file = new FileOutputStream(handle);
			DataOutput stream = new DataOutputStream(file);
			try {
				saveRepositoryStream(stream);
				deleteOldFiles(dir);
			} finally {
				((DataOutputStream)stream).close();
				file.close();
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}


	/**
	   Deletes the binary files with the names "header" and "contents".
	   They are replaced by the binary file "repository".
	   These operations are performed within saveRepositoryLocal method.
	*/
	private void deleteOldFiles(File dir) throws SdaiException {
		String [] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			if (!(files[i].equals("header") || files[i].equals("contents"))) {
				continue;
			}
			File handle = new File(dir, files[i]);
			System.gc();
			if (handle.exists()) {
				if (!handle.delete()) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DELF;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
	}


	/**
	   Loads the data contained in the binary file 'repository' to this repository
	   when this repository is a local one.
	   This method is used in:
	   openRepository() in SdaiRepository;
	   abort() in SdaiTransaction.
	   In the second case, during session some models and some schemas may
	   be modified (for example they may be renamed). Abort invokes
	   loadRepositoryLocal which restores models and schemas, which were modified,
	   from the binary file.
	*/
	void loadRepositoryLocal() throws SdaiException {
		File handle = null;
		FileInputStream file;
		DataInput stream;
		try {
			handle = new File((String)location, "repository");
			if (handle.isFile()) {
				file = new FileInputStream(handle);
				stream = new DataInputStream(file);
				try {
					loadRepositoryStream(stream);
				} finally {
					((DataInputStream)stream).close();
					file.close();
				}
				physical_file_name = name;
				return;
			}
			handle = new File((String)location, "header");
			if (!handle.isFile()) {
				return;
			}
			file = new FileInputStream(handle);
			stream = new DataInputStream(file);
			try {
				loadHeaderStream(stream);
				handle = new File((String)location, "contents");
				if (!handle.isFile()) {
					return;
				}
				file = new FileInputStream(handle);
				stream = new DataInputStream(file);
				loadContentsStream(stream);
			} finally {
				((DataInputStream)stream).close();
				file.close();
			}
		} catch (IOException ex) {
			//			String base = SdaiSession.line_separator + AdditionalMessages.BF_IERR +
			//				handle.getAbsolutePath();
			throw new SdaiException(SdaiException.RP_NAVL, this);
		}
		physical_file_name = name;
	}


	/**
	   Loads the data contained in the binary file 'repository' to this repository.
	*/
	void loadRepositoryStream(DataInput stream) throws SdaiException {
		int i, j, k;
		int index;
		int count;
		int context_count;
		int schemas_count;
		int is_count;
		byte bt;
		boolean found;
		String str;
		SECTION_LANGUAGE sec_lang;
		SECTION_CONTEXT sec_con;
		SdaiModel model, mod;
		SchemaInstance sch_instance, sch;
		ASchemaInstance inc_schemas;
		A_string strings;

		for (i = 0; i < schemas.myLength; i++) {
			sch = (SchemaInstance)schemas.myData[i];
			sch.exists = false;
		}
		for (i = 0; i < models.myLength; i++) {
			mod = (SdaiModel)models.myData[i];
			mod.exists = false;
		}
		try {
			bt = stream.readByte();
			if (bt != 'R') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			build_number = stream.readInt();
			//System.out.println(" SdaiRepository  build_number: " + build_number);
			short something = stream.readShort();
			something = stream.readShort();
			something = stream.readShort();
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = stream.readShort();
			//System.out.println(" SdaiRepository  count of desriptions: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (description == null) {
				description = new A_string(SdaiSession.listTypeSpecial, this);
			} else {
				description.myLength = 0;
			}
			//String sss;
			for (i = 1; i <= count; i++) {
				//sss = stream.readUTF();
				//description.addByIndexPrivate(i, sss);
				//System.out.println(" SdaiRepository  desription: " + sss);
				description.addByIndexPrivate(i, stream.readUTF());
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			String name_from_file = stream.readUTF();
			if (!validateNameFromFile(name_from_file)) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_REND + name_from_file;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (file_name == null) {
				file_name = new FILE_NAME(this);
			} else {
				file_name.author.myLength = 0;
				file_name.organization.myLength = 0;
			}
			file_name.name = name_from_file;
			file_name.time_stamp = stream.readUTF();
			//System.out.println(" SdaiRepository  repo name: " + name +
			//"    time_stamp: " + ent_name.time_stamp);
			count = stream.readShort();
			//System.out.println(" SdaiRepository  count of authors: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				//sss = stream.readUTF();
				//ent_name.author.addByIndexPrivate(i, sss);
				//System.out.println(" SdaiRepository  author: " + sss);
				file_name.author.addByIndexPrivate(i, stream.readUTF());
			}
			count = stream.readShort();
			//System.out.println(" SdaiRepository  count of organizations: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				//sss = stream.readUTF();
				//ent_name.organization.addByIndexPrivate(i, sss);
				//System.out.println(" SdaiRepository  organization: " + sss);
				file_name.organization.addByIndexPrivate(i, stream.readUTF());
			}
			file_name.preprocessor_version = stream.readUTF();
			//System.out.println(" SdaiRepository  preprocessor_version: " + ent_name.preprocessor_version);
			file_name.originating_system = stream.readUTF();
			//System.out.println(" SdaiRepository  originating_system: " + ent_name.originating_system);
			file_name.authorization = stream.readUTF();
			//System.out.println(" SdaiRepository  authorization: " + ent_name.authorization);
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (file_schema == null) {
				file_schema = new FILE_SCHEMA(this);
			} else {
				file_schema.schema_identifiers.myLength = 0;
			}
			count = stream.readShort();
			//System.out.println(" SdaiRepository  count of schemas: " + count);
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= count; i++) {
				//sss = stream.readUTF();
				//file_sch.schema_identifiers.addByIndexPrivate(i, sss);
				//System.out.println(" SdaiRepository  schema: " + sss);
				file_schema.schema_identifiers.addByIndexPrivate(i, stream.readUTF());
			}
			mod = get_schema_model();
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			bt = stream.readByte();
			languages_count = 0;
			contexts_count = 0;
			if (bt == 'L') {
				sec_lang = new SECTION_LANGUAGE(null, stream.readUTF());
				if (languages == null) {
					languages = new SECTION_LANGUAGE[LANGUAGES_ARRAY_SIZE];
				}
				languages[languages_count++] = sec_lang;
				language = sec_lang.default_language;
				//System.out.println(" SdaiRepository  default_language: " + language);
				bt = stream.readByte();
			}
			if (bt == 'C') {
				strings = new A_string(SdaiSession.listTypeSpecial, this);
				context_count = stream.readShort();
				if (context_count < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				//System.out.println(" SdaiRepository  context_count: " + context_count);
				for (j = 1; j <= context_count; j++) {
					//sss = stream.readUTF();
					//strings.addByIndexPrivate(j, sss);
					//System.out.println(" SdaiRepository  context: " + sss);
					strings.addByIndexPrivate(j, stream.readUTF());
				}
				sec_con = new SECTION_CONTEXT(null, strings);
				if (contexts == null) {
					contexts = new SECTION_CONTEXT[CONTEXTS_ARRAY_SIZE];
				}
				contexts[contexts_count++] = sec_con;
				context_identifiers = strings;
				bt = stream.readByte();
			}
			if (bt != 'I') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			//largest_identifier = stream.readLong();    //--VV--030310--
			setNextPersistentLabel(stream.readLong()+1);    //--VV--030311--
			//System.out.println(" SdaiRepository  largest_identifier: " + largest_identifier);

			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			schemas_count = stream.readShort();
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

			String name_of_def;
			String dict_name;
			SdaiModel dict_model;
			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			model_count = stream.readInt();
			//System.out.println(" SdaiRepository  model_count: " + model_count);
			int md_count = stream.readShort();
			//System.out.println(" SdaiRepository  no of models: " + md_count);
			if (md_count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (md_count > 1 && SdaiCommon.feature_level == 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OSZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
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
				//System.out.println(" SdaiRepository  model name: " + str);
				int ident = stream.readInt();
				//System.out.println(" SdaiRepository  model id: " + ident);
				index = stream.readShort();
				//System.out.println(" SdaiRepository  index to underlying schema: " + index);
				if (index < 0 || index >= schemas_count) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				name_of_def = staticFields.schema_names[index];
				//System.out.println(" SdaiRepository  name_of_def: " + name_of_def);
				dict_name = name_of_def.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				model = null;
				found = false;
				//for (j = 0; j < models.myLength; j++) {
				//mod = (SdaiModel)models.myData[j];
				//System.out.println(" SdaiRepository ===== model: " + mod.name + "   identifier: " + mod.identifier);
				//}
				for (j = 0; j < models.myLength; j++) {
					mod = (SdaiModel)models.myData[j];
					if (!mod.exists && str.equals(mod.name) && ident == mod.identifier) {
						mod.exists = true;
						model = mod;
						found = true;
						break;
					}
				}
				if (!found) {
					model = session.active_transaction.restoreDeletedModel(str, ident, this);
					if (model != null) {
						model.repository = this;
						models.addUnorderedRO(model);
						insertModel();
						found = true;
					}
				}
				if (!found) {
					model = createModel(str, dict_model);
					models.addUnorderedRO(model);
					insertModel();
					model.exists = true;
				} else if (model.underlying_schema == null && model.dictionary == null) {
					model.promoteVirtualToOrdinary(dict_model);
					//System.out.println(" SdaiRepository  repo: " + name +
					//"  model_promoted: " + model.name + "   identifier: " + model.identifier +
					//"   mod: " + model);
					model.exists = true;
				} else {
					//					model.mode = SdaiModel.NO_ACCESS;
					if (model.associated_with == null) {
						model.associated_with = new ASchemaInstance(SdaiSession.setType0toN, model);
					} else {
						model.associated_with.shrink(this);
					}
					//					model.underlying_schema = null;
					model.dictionary = dict_model;
					model.underlying_schema = dict_model.described_schema;
				}
				model.identifier = ident;
				model.committed = true;
				bt = stream.readByte();
				if (bt != 'D') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				if (str.equals("")) {
					model.change_date = -1;
				} else {
					model.change_date = session.cal.timeStampToLong(str);
				}
				bt = stream.readByte();
				if (bt == 'L') {
					sec_lang = new SECTION_LANGUAGE(model.name, stream.readUTF());
					if (languages == null) {
						languages = new SECTION_LANGUAGE[LANGUAGES_ARRAY_SIZE];
					} else if (languages_count >= languages.length) {
						enlarge_languages();
					}
					languages[languages_count++] = sec_lang;
					model.language = sec_lang.default_language;
					//System.out.println(" SdaiRepository  default_language: " + language);
					bt = stream.readByte();
				}
				if (bt == 'C') {
					strings = new A_string(SdaiSession.listTypeSpecial, model);
					context_count = stream.readShort();
					if (context_count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					//System.out.println(" SdaiRepository  context_count: " + context_count);
					for (j = 1; j <= context_count; j++) {
						//sss = stream.readUTF();
						//strings.addByIndexPrivate(j, sss);
						//System.out.println(" SdaiRepository  context: " + sss);
						strings.addByIndexPrivate(j, stream.readUTF());
					}
					sec_con = new SECTION_CONTEXT(model.name, strings);
					if (contexts == null) {
						contexts = new SECTION_CONTEXT[CONTEXTS_ARRAY_SIZE];
					} else if (contexts_count >= contexts.length) {
						enlarge_contexts();
					}
					contexts[contexts_count++] = sec_con;
					model.context_identifiers = strings;
					bt = stream.readByte();
				}
				byte_accepted = true;
				if (found) {
					model.modified_outside_contents = false;
				}
			}
			index = 0;
			for (j = 0; j < models.myLength; j++) {
				mod = (SdaiModel)models.myData[j];
				if (mod.exists) {
					((SdaiModel)models.myData[index]).e_type_ind = j;
					index++;
				}
			}

			SdaiRepository repo;
			String rep_name;
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
			if (sch_count > 1 && SdaiCommon.feature_level == 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OSZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			byte sym = ' ';
			for (i = 0; i < sch_count; i++) {
				if (i == 0) {
					bt = stream.readByte();
				} else {
					bt = sym;
				}
				if (bt != 'B') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				//System.out.println(" SdaiRepository  schema instance name: " + str);
				index = stream.readShort();
				if (index < 0 || index >= schemas_count) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				name_of_def = staticFields.schema_names[index];
				//System.out.println(" SdaiRepository  native schemas index: " + index +
				//"   name_of_def: " + name_of_def);
				dict_name = name_of_def.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				sch_instance = null;
				found = false;
				for (j = 0; j < schemas.myLength; j++) {
					sch = (SchemaInstance)schemas.myData[j];
					if (!sch.exists && str.equals(sch.name) &&
						name_of_def.toUpperCase().equals(sch.getNativeSchemaString())) {
						sch.exists = true;
						sch_instance = sch;
						if (sch_instance.associated_models == null) {
							sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						} else {
							sch_instance.associated_models.myLength = 0;
						}
						found = true;
						break;
					}
				}
				if (!found) {
					sch_instance = session.active_transaction.restoreDeletedSchemaInstance(str, this);
					if (sch_instance != null) {
						sch_instance.repository = this;
						sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						sch_instance.exists = true;
						schemas.addUnorderedRO(sch_instance);
						insertSchemaInstance();
						found = true;
					}
				}
				if (!found) {
					sch_instance = new SchemaInstanceLocalImpl(str, this, dict_model);
					sch_instance.exists = true;
				}
				bt = stream.readByte();
				if (bt != 'D') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				if (str.equals("")) {
					sch_instance.change_date = -1;
				} else {
					sch_instance.change_date = session.cal.timeStampToLong(str);
				}
				sch_instance.validation_date = session.cal.timeStampToLong(stream.readUTF());
				sch_instance.validation_result = (int)stream.readByte();
				sch_instance.validation_level = stream.readShort();
				if (build_number >= BUILD_WITH_SCHEMA_INSTANCE_EXTENSION) {
					count = stream.readShort();  // new code added from here
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						sch_instance.description = new A_string(SdaiSession.listTypeSpecial, sch_instance);
						for (j = 1; j <= count; j++) {
							sch_instance.description.addByIndexPrivate(j, stream.readUTF());
						}
					}
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						sch_instance.author = new A_string(SdaiSession.listTypeSpecial, sch_instance);
						for (j = 1; j <= count; j++) {
							sch_instance.author.addByIndexPrivate(j, stream.readUTF());
						}
					}
					count = stream.readShort();
					if (count < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					if (count > 0) {
						sch_instance.organization = new A_string(SdaiSession.listTypeSpecial, sch_instance);
						for (j = 1; j <= count; j++) {
							sch_instance.organization.addByIndexPrivate(j, stream.readUTF());
						}
					}
					bt = stream.readByte();
					if (bt == 'P') {
						sch_instance.preprocessor_version = stream.readUTF();
						bt = stream.readByte();
					}
					if (bt == 'O') {
						sch_instance.originating_system = stream.readUTF();
						bt = stream.readByte();
					}
					if (bt == 'A') {
						sch_instance.authorization = stream.readUTF();
						bt = stream.readByte();
					}
					if (bt == 'L') {
						sch_instance.language = stream.readUTF();
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
						sch_instance.context_identifiers = new A_string(SdaiSession.listTypeSpecial, sch_instance);
						for (j = 1; j <= count; j++) {
							sch_instance.context_identifiers.addByIndexPrivate(j, stream.readUTF());
						}
					}     // new code added until here
				}
				//System.out.println(" SdaiRepository  change_date: " + str +
				//"   validation_date: " + sch_instance.validation_date +
				//"   validation_result: " + sch_instance.validation_result +
				//"   validation_level: " + sch_instance.validation_level);
				count = stream.readShort();
				//System.out.println(" SdaiRepository  count of assoc models: " + count);
				if (count < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				for (j = 0; j < count; j++) {
					bt = stream.readByte();
					if (bt == 'L') {
						mod = (SdaiModel)models.myData[((SdaiModel)models.myData[stream.readInt()]).e_type_ind];
						mod.associated_with.addUnorderedRO(sch_instance);
						if (sch_instance.associated_models == null) {
							sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						}
						sch_instance.associated_models.addUnorderedRO(mod);
					} else {
						str = stream.readUTF();
						rep_name = stream.readUTF();
						repo = null;
						for (k = 0; k < session.known_servers.myLength; k++) {
							SdaiRepository r = (SdaiRepository)session.known_servers.myData[k];
							if (r.name.equals(rep_name)) {
								repo = r;
								break;
							}
						}
						if (repo == null) {
							printWarningToLogo(session, AdditionalMessages.BF_RENF, rep_name, str, sch_instance);
							continue;
						}
						model = null;
						if (repo.active) {
							repo.internal_usage = true;
							model = repo.findSdaiModel(str);
							repo.internal_usage = false;
							//							for (k = 0; k < repo.models.myLength; k++) {
							//								mod = (SdaiModel)repo.models.myData[k];
							//								if (mod.name.equals(str)) {
							//									model = mod;
							//									break;
							//								}
							//							}
						}
						if (model == null) {
							model = repo.createVirtualModel(str);
						}
						model.associated_with.addUnorderedRO(sch_instance);
						if (sch_instance.associated_models == null) {
							sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						}
						sch_instance.associated_models.addUnorderedRO(model);
					}
				}
				if (!found) {
					schemas.addUnorderedRO(sch_instance);
					insertSchemaInstance();
				} else {
					sch_instance.modified = false;
				}
				sym = stream.readByte();
				if (sym != 'I') {
					continue;
				}
				is_count = stream.readShort();
				//System.out.println("SdaiRepository  count of included schemas: " + is_count);
				if (is_count <= 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				SchemaInstance incl_schema;
				for (j = 0; j < is_count; j++) {
					bt = stream.readByte();
					if (bt == 'L') {
						Integer pointer_to_sch_inst = new Integer(stream.readInt());
						if (sch_instance.included_schemas == null) {
							sch_instance.included_schemas = new ASchemaInstance(SdaiSession.setType0toN, sch_instance);
						}
						inc_schemas = sch_instance.included_schemas;
						inc_schemas.addUnorderedRO(pointer_to_sch_inst);
					} else {
						str = stream.readUTF();
						rep_name = stream.readUTF();
						repo = null;
						for (k = 0; k < session.known_servers.myLength; k++) {
							SdaiRepository r = (SdaiRepository)session.known_servers.myData[k];
							if (r.name.equals(rep_name)) {
								repo = r;
								break;
							}
						}
						if (repo == null) {
							printWarningToLogo2(session, AdditionalMessages.BF_RPNF, rep_name, sch_instance, str);
							continue;
						}
						incl_schema = null;
						if (repo.active) {
							repo.internal_usage = true;
							incl_schema = repo.findSchemaInstance(str);
							repo.internal_usage = false;
						}
						if (incl_schema == null) {
							printWarningToLogo2(session, AdditionalMessages.BF_RCLO, rep_name, sch_instance, str);
							continue;
						}
						if (sch_instance.included_schemas == null) {
							sch_instance.included_schemas = new ASchemaInstance(SdaiSession.setType0toN, sch_instance);
						}
						inc_schemas = sch_instance.included_schemas;
						inc_schemas.addUnorderedRO(incl_schema);
					}
				}
				sym = stream.readByte();
			}

			if (sch_count <= 0) {
				bt = stream.readByte();
			} else {
				bt = sym;
			}
			if (bt != 'E') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = schemas.myLength;
			int deleted = 0;
			for (j = 0; j < count; j++) {
				sch = (SchemaInstance)schemas.myData[j - deleted];
				if (!sch.exists) {
					sch.delete();
					deleted++;
				}
			}
			count = models.myLength;
			index = 0;
			for (j = 0; j < count; j++) {
				mod = (SdaiModel)models.myData[j];
				if (mod.exists) {
					models.myData[index] = models.myData[j];
					index++;
				} else {
					mod.deleteSdaiModelWork(false, false, false, currently_aborting);
					mod.resolveInConnectors(true);
					mod.repository = null;
					models.myData[j] = null;
				}
			}
			models.myLength = index;
			for (i = 0; i < schemas.myLength; i++) {
				sch_instance = (SchemaInstance)schemas.myData[i];
				if (sch_instance.included_schemas == null) {
					continue;
				}
				inc_schemas = sch_instance.included_schemas;
				for (j = 0; j < inc_schemas.myLength; j++) {
					Object obj = inc_schemas.myData[j];
					if (!(obj instanceof Integer)) {
						continue;
					}
					index = ((Integer)obj).intValue();
					if (index < 0 || index >= schemas.myLength) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_WISI;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					inc_schemas.myData[j] = (SchemaInstance)schemas.myData[index];
				}
			}
			committed = true;
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


	/**
	   Loads the data contained in the binary file 'contents' to this repository.
	   The method is invoked while opening repositories created with an old build of JSDAI
	   (220 or earlier).
	*/
	void loadContentsStream(DataInput stream) throws SdaiException {
		int i,j,k;
		boolean found;
		String str;
		String special_name, file_name;
		Class special;

		SchemaInstance sch;
		SdaiModel mod;
		String name_of_def;
		String dict_name;
		SdaiModel dict_model;
		for (j = 0; j < schemas.myLength; j++) {
			sch = (SchemaInstance)schemas.myData[j];
			sch.exists = false;
		}
		for (j = 0; j < models.myLength; j++) {
			mod = (SdaiModel)models.myData[j];
			mod.exists = false;
		}
		try {
			byte bt;
			int length, length_inner;
			if (SdaiSession.debug) System.out.println(" ++++++++ before readByte");
			bt = stream.readByte();
			if (SdaiSession.debug) System.out.println(" ++++++++ bt = " + (char)bt);
			if (bt != 'C') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			build_number = stream.readInt();
			if (build_number < session.valid_from) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_OLD0 + location + ".contents" +
					AdditionalMessages.BF_OLD1 + build_number + ". " + SdaiSession.line_separator +
					AdditionalMessages.BF_OLD2 + session.valid_from;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			bt = stream.readByte();
			if (bt == 'I') {
				//largest_identifier = stream.readLong();    //--VV--030310--
				setNextPersistentLabel(stream.readLong()+1);    //--VV--030311--
				bt = stream.readByte();
			}
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 0; i < length; i++) {
				str = stream.readUTF();
			}

			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (length > 1 && SdaiCommon.feature_level == 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OSZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 0; i < length; i++) {
				bt = stream.readByte();
				if (bt != 'B') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();

				/* find native_schema of type CSchema_definition */
				name_of_def = stream.readUTF();
				//System.out.println(" !!!!!!!!! In loadContentsStream 1  name_of_def: " + name_of_def);
				//				CSchema_definition schema_def = session.getSchemaDefinition(name_of_def);
				dict_name = name_of_def.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				SchemaInstance sch_instance = null;
				found = false;
				for (j = 0; j < schemas.myLength; j++) {
					sch = (SchemaInstance)schemas.myData[j];
					if (!sch.exists && str.equals(sch.name) &&
						name_of_def.toUpperCase().equals(sch.getNativeSchemaString())) {
						sch.exists = true;
						sch_instance = sch;
						if (sch_instance.associated_models == null) {
							sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						} else {
							sch_instance.associated_models.myLength = 0;
						}
						found = true;
						break;
					}
				}
				if (!found) {
					sch_instance = new SchemaInstanceLocalImpl(str, this, dict_model);
					sch_instance.exists = true;
				}
				sch_instance.change_date = session.cal.timeStampToLong(stream.readUTF());
				//String tm = stream.readUTF();
				//System.out.println("  SdaiRepository   time: " + tm);
				//sch_instance.change_date = session.get_time_from_string(tm);
				sch_instance.validation_date = session.cal.timeStampToLong(stream.readUTF());
				sch_instance.validation_result = (int)stream.readByte();
				//				bt = stream.readByte();
				//				if (bt > 0) {
				//					sch_instance.validation_result = true;
				//				} else {
				//					sch_instance.validation_result = false;
				//				}
				sch_instance.validation_level = stream.readInt();
				length_inner = stream.readInt();
				if (length_inner < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				for (j = 0; j < length_inner; j++) {
					str = stream.readUTF();
					str = stream.readUTF();
				}
				if (!found) {
					schemas.addUnorderedRO(sch_instance);
					insertSchemaInstance();
				} else {
					sch_instance.modified = false;
				}
			}

			bt = stream.readByte();
			if (bt != 'S') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			model_count = 0;
			for (i = 0; i < length; i++) {
				bt = stream.readByte();
				if (bt != 'B') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				str = stream.readUTF();
				String sch_name = null;
				bt = stream.readByte();
				if (bt == 'P') {
					sch_name = stream.readUTF();
				} else if (bt != 'M') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				name_of_def = stream.readUTF();
				//System.out.println(" !!!!!!!!! In loadContentsStream 2  name_of_def: " + name_of_def);
				//				CSchema_definition schema_def = session.getSchemaDefinition(name_of_def);
				dict_name = name_of_def.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				SdaiModel model = null;
				found = false;
				for (j = 0; j < models.myLength; j++) {
					mod = (SdaiModel)models.myData[j];
					//					if (!mod.exists && str.equals(mod.name) && (mod.underlying_schema == null ||
					//							schema_def.getName(null).equals(mod.underlying_schema.getName(null)))) {
					if (!mod.exists && str.equals(mod.name)) {
						mod.exists = true;
						model = mod;
						found = true;
						break;
					}
				}
				if (!found) {
					model = createModel(str, dict_model);
					models.addUnorderedRO(model);
					insertModel();
					model.exists = true;
				} else if (model.underlying_schema == null && model.dictionary == null) {
					model.promoteVirtualToOrdinary(dict_model);
					//					found = false;
				} else {
					//					model.mode = SdaiModel.NO_ACCESS;
					if (model.associated_with == null) {
						model.associated_with = new ASchemaInstance(SdaiSession.setType0toN, model);
					} else {
						model.associated_with.shrink(this);
					}
					//					model.underlying_schema = null;
					model.dictionary = dict_model;
					model.underlying_schema = dict_model.described_schema;
				}
				model.schema_name = sch_name;
				str = stream.readUTF();
				if (str.equals("")) {
					model.change_date = -1;
				} else {
					model.change_date = session.cal.timeStampToLong(str);
				}
				//				model.initializeContents();

				length_inner = stream.readInt();
				if (length_inner < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				for (j = 0; j < length_inner; j++) {
					str = stream.readUTF();
					internal_usage = true;
					SchemaInstance sch_instance = findSchemaInstance(str);
					internal_usage = false;
					if (sch_instance != null) {
						model.associated_with.addUnorderedRO(sch_instance);
						if (sch_instance.associated_models == null) {
							sch_instance.associated_models = new ASdaiModel(SdaiSession.setType0toN, sch_instance);
						}
						sch_instance.associated_models.addUnorderedRO(model);
					}
					/*					for (k = 0; k < schemas.myLength; k++) {
										SchemaInstance sch_instance = (SchemaInstance)schemas.myData[k];
										if (sch_instance.name.equals(str)) {
										model.associated_with.addUnorderedRO(sch_instance);
										sch_instance.associated_models.addUnorderedRO(model);
										break;
										}
										} */
				}
				if (found) {
					model.modified_outside_contents = false;
				}
				model_count++;
				model.identifier = model_count;
			}

			bt = stream.readByte();
			if (bt != 'E') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			int count = schemas.myLength;
			int deleted = 0;
			for (j = 0; j < count; j++) {
				sch = (SchemaInstance)schemas.myData[j - deleted];
				if (!sch.exists) {
					sch.delete();
					deleted++;
				}
			}
			count = models.myLength;
			int ind = 0;
			for (j = 0; j < count; j++) {
				mod = (SdaiModel)models.myData[j];
				if (mod.exists) {
					models.myData[ind] = models.myData[j];
					ind++;
				} else {
					mod.deleteSdaiModelWork(false, false, false, currently_aborting);
					mod.resolveInConnectors(true);
					mod.repository = null;
					models.myData[j] = null;
				}
			}
			models.myLength = ind;
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		for (j = 0; j < languages_count; j++) {
			SECTION_LANGUAGE language = languages[j];
			if (language.section == null) {
				continue;
			}
			internal_usage = true;
			mod = findSdaiModel(language.section);
			internal_usage = false;
			if (mod != null) {
				mod.language = language.default_language;
			}
		}
		for (j = 0; j < contexts_count; j++) {
			SECTION_CONTEXT context = contexts[j];
			if (context.section == null) {
				continue;
			}
			internal_usage = true;
			mod = findSdaiModel(context.section);
			internal_usage = false;
			if (mod != null) {
				mod.context_identifiers = context.context_identifiers;
			}
		}
	}


	protected boolean committingInternal(boolean commit_bridge, boolean modified) throws SdaiException {
		saveRepositoryLocal();
		SdaiSession.addToProperties(this, true);
		return commit_bridge;
	}


	protected void abortingInternal(boolean modif, boolean contents_modified) throws SdaiException {
		if (committed && (modif || contents_modified || model_deleted || schema_instance_deleted)) {
			currently_aborting = true;
			loadRepositoryLocal();
			currently_aborting = false;
		}
		//FIXME: These operations should be perfomed only in the case external data modification
		//       flag is set. However this flag should be mainained in the right way.
		extDataProps = null;
		if(entityExternalData != null) {
			for (Iterator i = entityExternalData.values().iterator(); i.hasNext();) {
				ExternalData exData = (ExternalData) i.next();
				exData.owningEntity = null;
			}
			entityExternalData.clear();
		}
		if(entityRemovedExternalData != null) {
			entityRemovedExternalData.clear();
		}
	}


	protected boolean isRemote() throws SdaiException {
		return false;
	}


	protected void restoringInternal(SdaiModel model, int mode) throws SdaiException {
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

	boolean validateNameFromFile(String nameFromFile) throws SdaiException {
		return name.equals(nameFromFile);
	}


	/**
	   Loads the repository name contained in the binary file 'repository'.
	*/
	static String getRepositoryNameFromStream(DataInput stream) throws SdaiException {
		int count;
		byte bt;
		short something;
		String nameFromFile = "";

		try {
			bt = stream.readByte();
			if (bt != 'R') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}

			stream.skipBytes(10);
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			count = stream.readShort();
			if (count < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (int i = 1; i <= count; i++) {
				stream.readUTF();
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM + " ";
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			nameFromFile = stream.readUTF();
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR, base).initCause(ex);
		}
		return nameFromFile;
	}

}
