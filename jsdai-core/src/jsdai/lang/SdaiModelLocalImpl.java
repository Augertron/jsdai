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
class SdaiModelLocalImpl extends SdaiModel {
	/** Used to create application (through the method createSdaiModel) and dictionary
	 *  SdaiModels.
	 *  Also using this constructor some special SdaiModel called sessionModel is created.
	 */
	SdaiModelLocalImpl(String model_name, CSchema_definition schema, SdaiRepository rep) throws SdaiException {
		super(model_name, schema, rep);
	}
	
	/** Used to create an SdaiModel when reading a repository binary file.
	 */
	SdaiModelLocalImpl(String model_name, SdaiRepository rep, SdaiModel dict) throws SdaiException {
		super(model_name, rep, dict);
	}
	
	/** Used to create an SdaiModel when in the binary file being loaded the name of
	 *  a model and its repository (or only its name) are found but this repository
	 *  misses a model with this given name.
	 *  Also, used to create virtual models.
	 */
	SdaiModelLocalImpl(String model_name, SdaiRepository rep) {
		super(model_name, rep);
	}

	protected boolean committingHeaderInternal(boolean commit_bridge,
											   boolean modif_remote) throws SdaiException {
		return commit_bridge;
	} 
	
	protected boolean committingInternal(boolean b1, boolean b2, boolean modif_all) throws SdaiException {
		if (modif_all) {
			saveLocal();
		}
		set_fields(modif_all);
		return b1;
	}

	protected boolean deletingInternal(boolean commit_bridge, SdaiRepository rep_for_del_mod) throws SdaiException {
		if (rep_for_del_mod.location instanceof String) {
			String del_mod_rep_loc = (String)rep_for_del_mod.location;
			File handle = new File(del_mod_rep_loc, "m" + identifier);
if (SdaiSession.debug) System.out.println("model deleted = " + identifier);
			if (handle.exists()) {
				if (!handle.delete()) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DELF + 
						SdaiSession.line_separator + AdditionalMessages.RD_MODL + name + 
						SdaiSession.line_separator + AdditionalMessages.RD_FILE + handle.getAbsolutePath();
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		return commit_bridge;
	}

	protected boolean abortingInternal(SdaiTransaction trans, boolean modif, 
			boolean modif_outside_contents, boolean endTransaction) throws SdaiException {
		if (committed) {
			if (modified && (mode & MODE_MODE_MASK) != NO_ACCESS) {
				int old_mode = mode;
				exists = true;
//				deleteContents(true);
				refreshContents();
//for (int i = 0; i < instances_sim.length; i++) {
//lengths[i] = 0;
//}
//instances_sim = null;
				long max_ident = loadLocal(old_mode);
				if (repository.getPersistentLabel() < max_ident) {
					repository.setNextPersistentLabel(max_ident+1);
				}
			} else if (repository.model_deleted && (mode & MODE_MODE_MASK) == NO_ACCESS) {
				for (int j = 0; j < trans.stack_length; j++) {
					SdaiRepository rep_for_del_mod = trans.stack_del_mods_rep[j];
					if (rep_for_del_mod != repository || identifier != trans.stack_del_mods[j].identifier) {
						continue;
					}
					if ((trans.stack_del_mods[j].mode_before_deletion & MODE_MODE_MASK) == READ_ONLY) {
						startReadOnlyAccess();
					} else if ((trans.stack_del_mods[j].mode_before_deletion & MODE_MODE_MASK) == READ_WRITE) {
						startReadWriteAccess();
					}
					break;
				}
			}
			return false;
		}
		abortingCreated();
//		mode = READ_ONLY;
//		exists = false;
//		deleteSdaiModelWork(false, true);
//		resolveInConnectors(true);
//		created = false;
		return true;
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


	/**
	 * This overload methods adds all instances to the removal list
	 * of external data in the repository
	 */
	protected void scheduledForDeletion() throws SdaiException {
		SdaiRepositoryLocalImpl localRepository = (SdaiRepositoryLocalImpl)repository;
		if ((mode & MODE_MODE_MASK) != NO_ACCESS) {
			for (int i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null) {
					continue;
				}
				CEntity [] row_of_instances = instances_sim[i];
				for (int j = 0; j < lengths[i]; j++) {
					CEntity instance = row_of_instances[j];
					localRepository.testAndRemoveEntityExternalData(instance.instance_identifier);
				}
			}
		} else {
			boolean loaded = false;
			if (inst_idents == null) {
				loadInstanceIdentifiers();
				loaded = true;
			}
			for (int i = 0; i < (int)all_inst_count; i++) {
				localRepository.testAndRemoveEntityExternalData(inst_idents[i]);
			}
			if (loaded) {
				inst_idents = null;
				all_inst_count = 0;
			}
		}
	}
	
//	protected void entityDeletedInternal(CEntity instance) throws SdaiException { }

	public SdaiPermission checkPermission() {
		return SdaiPermission.WRITE;
	}

	public void checkRead() {
		// Local model always has read permission
	}

	public void checkWrite() {
		// Local model always has write permission
	}

	public void deleteSdaiModel() throws SdaiException {
//		synchronized (syncObject) {
			deleteSdaiModelCommon(false);
//		} // syncObject
	}


	public void startReadOnlyAccess() throws SdaiException {
//		synchronized (syncObject) {
			startAccess(READ_ONLY);
//		} // syncObject
	}


	public void startReadWriteAccess() throws SdaiException {
//		synchronized (syncObject) {
			startAccess(READ_WRITE);
//		} // syncObject
	}


    public void startPartialReadOnlyAccess() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"Partial access is not available for local model " + name);
    }


    public void startPartialReadWriteAccess() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL,
								"Partial access is not available for local model " + name);
    }

	private void startAccess(int mode_to_start) throws SdaiException {
//setOptimized(true);
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < repository.models.myLength; i++) {
				SdaiModel mod = (SdaiModel)repository.models.myData[i];
				if ((mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					count++;
				}
			}
			if (count >= 1) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OMZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, session.active_transaction);
		}
		initializeContents();
		if (committed) {
			long max_ident;
			try {
				max_ident = loadLocal(mode_to_start);
			} catch (SdaiException ex) {
				mode = NO_ACCESS;
				throw ex;
			}
            if (repository.getPersistentLabel() < max_ident) {
				repository.setNextPersistentLabel(max_ident+1);
			}
		} else {
			prepareInitialContens(mode_to_start);
			setMode(mode_to_start);
		}
		session.active_models.addUnorderedRO(this);
	}


	protected void initializeContents() throws SdaiException {
		initializeDataModelContents();
	}


	protected void deleteAllInstances() throws SdaiException {
		if (instances_sim == null) {
			return;
		}
		for (int i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] <= 0) {
				continue;
			}
			CEntity [] row_of_instances = instances_sim[i];
			for (int j = 0; j < lengths[i]; j++) {
				CEntity instance = row_of_instances[j];
				instance.owning_model = null;
				instance.fireSdaiEvent(SdaiEvent.INVALID, -1, null);
			}
			instances_sim[i] = emptyArray;
			lengths[i] = 0;
		}
	}


/**
	Loads the data contained in the binary file to this SdaiModel when this SdaiModel 
	belongs to a local repository.
*/
	long loadLocal(int mode_to_start) throws SdaiException {
		setMode(READ_WRITE);
		if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			ex_models = new SdaiModel[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
		File handle = null;
		FileInputStream file;
		long res;
		try {
			handle = new File((String)repository.location, "m" + identifier);
			if(handle.exists()) {
				file = new FileInputStream(handle);
				DataInput stream = new DataInputStream( new BufferedInputStream(file));
				try {
					res = loadStream(stream, mode_to_start);
				} finally {
					setMode(mode_to_start);
					((DataInputStream)stream).close();
					file.close();
				}
			} else {
				String f_name = "m" + identifier;
				if (repository.session.logWriterSession != null) {
					repository.session.printlnSession(AdditionalMessages.BF_MIF0 + f_name + AdditionalMessages.BF_MIF1);
				} else {
					SdaiSession.println(AdditionalMessages.BF_MIF0 + f_name + AdditionalMessages.BF_MIF1);
				}
				prepareInitialContens(mode_to_start);
				setMode(mode_to_start);
				modified = false;
				deleteAllInstances();
				res = -1;
			}
		} catch (IOException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_MNF1 +
				handle.getAbsolutePath() + AdditionalMessages.BF_MNF2;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		resolveInConnectors(false);
		return res;
	}


/**
	Loads the data contained in the binary file to this SdaiModel.
*/
    protected long loadStream(DataInput stream, int mode_to_start) throws SdaiException {
        //long lST, lCT;
        //lST = System.currentTimeMillis();
        long res = loadStreamCommon(stream, mode_to_start);
        //lCT = System.currentTimeMillis();
        //System.out.println("lSL t="+(float)(lCT-lST)/1000);
		return res;
	}
    
/**
	Writes the data contained in this SdaiModel to the binary file.
*/
    protected void saveStream(DataOutput stream) throws SdaiException {
        //long lST, lCT;
        //lST = System.currentTimeMillis();
        saveStreamCommon(stream);
        //lCT = System.currentTimeMillis();
        //System.out.println("sSL t="+(float)(lCT-lST)/1000);
    }
    
	public void promoteSdaiModelToRW() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < repository.models.myLength; i++) {
				SdaiModel mod = (SdaiModel)repository.models.myData[i];
				if ((mod.mode & MODE_MODE_MASK) != NO_ACCESS && mod != this) {
					count++;
				}
			}
			if (count >= 1) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OMZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		setMode(READ_WRITE);
		promoteSdaiModelFromSmall();
//		} // syncObject
	}


	public void reduceSdaiModelToRO() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if (modified) {
			throw new SdaiException(SdaiException.TR_RW, repository.session.active_transaction);
		}
		setMode(READ_ONLY);
		reduceSdaiModelToSmall();
//		} // syncObject
	}


	public void endReadOnlyAccess() throws SdaiException {
//		synchronized (syncObject) {
			endReadOnlyAccessCommon();
//		} // syncObject
	}


	public void endReadWriteAccess() throws SdaiException {
//		synchronized (syncObject) {
			endReadWriteAccessCommon();
//		} // syncObject
	}


/**
	Writes the data contained in this SdaiModel to the binary file when this 
	SdaiModel belongs to a local repository.
*/
	void saveLocal() throws SdaiException {
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
		try {
			File dir = new File((String)repository.location);
			if (repository.temporary) {
//				dir.deleteOnExit();
			}
			boolean return_value = true;
			if (!dir.isDirectory()) {
	    		return_value = dir.mkdir();
			}
			if (!return_value) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NOD1 +
					(String)repository.location + AdditionalMessages.BF_NOD2;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			File handle = new File(dir, "m" + identifier);
//String sss = handle.getAbsolutePath();
//System.out.println(" sss = " + sss);
			if (repository.temporary) {
//				handle.deleteOnExit();
			}
			FileOutputStream file = new FileOutputStream(handle);
			DataOutput stream = new DataOutputStream(new BufferedOutputStream(file));
			try {
				saveStream(stream);
			} finally {
				((DataOutputStream)stream).close();
				file.close();
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}

    
    protected static class ConnectorLocalImpl extends Connector  {
    
        protected String entity_name;
        
        public ConnectorLocalImpl(SdaiModelConnector modelInConnector, String ent_name, 
                long inst_identifier, CEntity instance) throws SdaiException {
            super(modelInConnector, inst_identifier, instance);
            entity_name = ent_name;
        }
        
        protected Connector copyConnector(CEntity owning_instance) throws SdaiException {
            return new ConnectorLocalImpl(modelInConnector, entity_name,
										  instance_identifier, owning_instance);
        }
        
        protected CEntity resolveConnector(boolean remove_con, boolean providePartial,
										   boolean aborting) throws SdaiException {
            SdaiModel mod = resolveModelIn();
			if(mod == null) {
				disconnect();
				return null;
			}
            SdaiRepository	rep = mod.repository;
            if (!rep.active) {
                throw new SdaiException(SdaiException.RP_NOPN, rep);
            }
            long con_instance_identifier = instance_identifier;
            String con_entity_name = entity_name.toUpperCase();
            if (mod.getMode() == SdaiModel.NO_ACCESS) {
                mod.startReadOnlyAccess();
            }
            SchemaData sch_data = mod.underlying_schema.modelDictionary.schemaData;
            int index;
            if (mod.repository != SdaiSession.systemRepository && ((mod.mode & MODE_MODE_MASK) == READ_ONLY)) {
					index = mod.find_entityRO(con_entity_name);
				} else {
            	index = sch_data.find_entity(0, sch_data.sNames.length - 1, con_entity_name);
            }
            if (index < 0) {
                String base = SdaiSession.line_separator + AdditionalMessages.BF_EINC + con_entity_name;
                throw new SdaiException(SdaiException.SY_ERR, base);
            }
            int ind = -1;
            CEntity inst = null;
            CEntity [] row_of_instances = mod.instances_sim[index];
            //if (mod.sorted[index]) {
            if ((mod.sim_status[index] & SdaiModel.SIM_SORTED) != 0) {
                ind = mod.find_instance(0, mod.lengths[index] - 1, index,
                    con_instance_identifier);
                if (ind >= 0) {
                    inst = row_of_instances[ind];
                }
            } else {
                for (int j = 0; j < mod.lengths[index]; j++) {
                    inst = row_of_instances[j];
                    if (inst.instance_identifier == con_instance_identifier) {
                        ind = j;
                        break;
                    }
                }
            }
				if (ind >= 0) {
					if (owning_instance == null) {
						return inst;
					}
					if (remove_con) {
						if (!(inst.owning_model != null && inst.owning_model.optimized)) {
							inst.inverseAdd(owning_instance);
						}
						disconnect();
					}
					return inst;
				}
			if(aborting) {
				if(remove_con) {
					disconnect();
				}
				return null;
			} else {
//				String base =
//					SdaiSession.line_separator + AdditionalMessages.BF_IINC + con_instance_identifier;
//				throw new SdaiException(SdaiException.SY_ERR, base);
				return null;
			}
        }

		protected String getEntityNameUpperCase() {
			return entity_name.toUpperCase();
		}
                                           
        final void print_connector() throws SdaiException {
            System.out.println();
            System.out.println("   Owning instance: ");
            System.out.println("       instance identifier = #" + owning_instance.instance_identifier);
            EEntity_definition def = owning_instance.getInstanceType();
            System.out.println("       instance type name  = " + ((CEntityDefinition)def).getCorrectName());
            if (owning_instance.owning_model != null) {
                System.out.println("       instance model name = " + owning_instance.owning_model.name);
            }
            System.out.println("   Referenced model: " + modelInConnector);
            System.out.println("   Referenced instance type name: " + entity_name);
            System.out.println("   Referenced instance identifier: #" + instance_identifier);
        }
    }

    protected Connector newConnector(SdaiModel model, String entityName, long instanceIdentifier, CEntity owningInstance) throws SdaiException {
        return new ConnectorLocalImpl(model, entityName, instanceIdentifier, owningInstance);
    }

    protected Connector newConnector(SdaiRepository repository, long modelId, long instanceIdentifier,
									 CEntity owningInstance) throws SdaiException {
		throw new SdaiException(SdaiException.SY_ERR,
								"Local model can not handle this type of connector");
	}

    protected Connector newConnector(long repositoryId, long modelId, long instanceIdentifier,
									 CEntity owningInstance) throws SdaiException {
		throw new SdaiException(SdaiException.SY_ERR,
								"Local model can not handle this type of connector");
	}

}
