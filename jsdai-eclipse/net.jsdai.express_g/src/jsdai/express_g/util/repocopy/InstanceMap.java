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

package jsdai.express_g.util.repocopy;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import jsdai.SExtended_dictionary_schema.EAlgorithm_declaration;
import jsdai.SExtended_dictionary_schema.EAlgorithm_definition;
import jsdai.SExtended_dictionary_schema.EAnd_subtype_expression;
import jsdai.SExtended_dictionary_schema.EAndor_subtype_expression;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EConstant_declaration;
import jsdai.SExtended_dictionary_schema.EConstant_definition;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EData_type_declaration;
import jsdai.SExtended_dictionary_schema.EDeclaration;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EGeneric_schema_definition;
import jsdai.SExtended_dictionary_schema.EGlobal_rule;
import jsdai.SExtended_dictionary_schema.EInner_declaration;
import jsdai.SExtended_dictionary_schema.EInterfaced_declaration;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ELocal_declaration;
import jsdai.SExtended_dictionary_schema.EMap_declaration;
import jsdai.SExtended_dictionary_schema.EMap_definition;
import jsdai.SExtended_dictionary_schema.EOneof_subtype_expression;
import jsdai.SExtended_dictionary_schema.ERule_declaration;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.SExtended_dictionary_schema.ESubtype_constraint_declaration;
import jsdai.SExtended_dictionary_schema.ESubtype_expression;
import jsdai.SExtended_dictionary_schema.EView_attribute;
import jsdai.express_g.SdaieditPlugin;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

/**
 * @author Mantas Balnys
 *
 */
public class InstanceMap {
	protected SdaiRepository repoNew;
	
	/**
	 * if true loads to Map only classes of requered type
	 */
	public boolean SINGLE_LOADING = false;
	private Class requered_class = null;

	/**
	 * 
	 */
	public InstanceMap(SdaiRepository newRepository) throws SdaiException {
		repoNew = newRepository;
		if (!repoNew.isActive()) repoNew.openRepository();
	}
	
	private static final String INSTANCE_MAP_KEY = "JSDAI INSTANCE MAP KEY ";
	
	public int MAX_OPEN_MODELS = 12;
	  
	/**
	 * for cleaning tempData purposes
	 */
	private HashSet tempSet = new HashSet();
	  
	protected void setTemp(EEntity entity, EEntity data) {
		entity.setTemp(INSTANCE_MAP_KEY, data);
	  	tempSet.add(entity);
	}
	  
	protected EEntity getTemp(EEntity entity) {
		return (EEntity)entity.getTemp(INSTANCE_MAP_KEY);
	}
	  
	protected void cleanTemp() {
	  	Iterator iter = tempSet.iterator();
	  	while (iter.hasNext()) {
	  		EEntity entity = (EEntity)iter.next();
	  		entity.setTemp(INSTANCE_MAP_KEY, null);
	  	}
	  	tempSet.clear();
	}
	
	/**
	 * get entity in new repository by having one from the old
	 * returns null on any kind of error
	 * @param oldent
	 * @return
	 */
	public EEntity get(EEntity oldent) {
		EEntity newent = getTemp(oldent);
		if (newent == null) try {
			requered_class = oldent.getClass();
			Object key = getKey(oldent);
			newent = getMapped(oldent.findEntityInstanceSdaiModel().getName(), key);
			if (newent != null) {
				setTemp(oldent, newent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			newent = null;
		}
		return newent;
	}
	
	/**
	 * creates an unique key for a given entity in this model, like name for entity_definition
	 * !key must be unique in this model
	 * @param entity
	 * @return
	 */
	protected String getKey(EEntity entity) throws SdaiException {
		StringBuffer key1 = getKey1(entity);
		String key = key1.length() == 0 ? null : key1.toString().intern();
		return key;
	}
	
	/**
	 * creates an unique key for a given entity in this model, like name for entity_definition
	 * !key must be unique in this model
	 * @param entity
	 * @return
	 */
	private StringBuffer getKey1(EEntity entity) throws SdaiException {
		StringBuffer key = new StringBuffer(64);
		key.append(entity.findEntityInstanceSdaiModel().getName());
//		key.append("_");
		if (entity instanceof EData_type) {
			key.append("EData_type_");
			key.append(((EData_type)entity).getName(null));
		} else
		if (entity instanceof EAttribute) {
			EEntity domain = null;
			if (entity instanceof EExplicit_attribute) {
				domain = ((EExplicit_attribute)entity).getDomain(null);
			} else
			if (entity instanceof EDerived_attribute) {
				domain = ((EDerived_attribute)entity).getDomain(null);
			} else
			if (entity instanceof EInverse_attribute) {
				domain = ((EInverse_attribute)entity).getDomain(null);
			} else
			if (entity instanceof EView_attribute) {
				domain = ((EView_attribute)entity).getDomain(null);
			}
			key.append("EAttribute_");
			key.append(((EAttribute)entity).getName(null));
			key.append(getKey1(((EAttribute)entity).getParent(null)));
			if (domain == null)
				key.append("null_");
			else
				key.append(getKey1(domain));
		} else
		if (entity instanceof EGeneric_schema_definition) {
			key.append("EGeneric_schema_definition_");
			key.append(((EGeneric_schema_definition)entity).getName(null));
		} else
		if (entity instanceof EDeclaration) {
			if (entity instanceof EInterfaced_declaration) {
				key.append("EInterfaced_declaration_");
			} else
			if (entity instanceof EInner_declaration) {
				key.append("EInner_declaration_");
			} else
			if (entity instanceof ELocal_declaration) {
				key.append("ELocal_declaration_");
			}
			if (entity instanceof EData_type_declaration) {
				key.append("EData_type_declaration_");
			} else  
			if (entity instanceof ERule_declaration) {
				key.append("ERule_declaration_");
			} else  
			if (entity instanceof EAlgorithm_declaration) {
				key.append("EAlgorithm_declaration_");
			} else  
			if (entity instanceof EConstant_declaration) {
				key.append("EConstant_declaration_");
			} else  
			if (entity instanceof EMap_declaration) {
				key.append("EMap_declaration_");
			} else  
			if (entity instanceof ESubtype_constraint_declaration) {
				key.append("ESubtype_constraint_declaration_");
			}  
			key.append("EDeclaration_");
			key.append(getKey1(((EDeclaration)entity).getDefinition(null)));
		} else
		if (entity instanceof ESub_supertype_constraint) {
			ESub_supertype_constraint sc = (ESub_supertype_constraint)entity;
			key.append("ESub_supertype_constraint_");
			key.append(getKey1(sc.getGeneric_supertype(null)));
			if (sc.testName(null)) {
//				key.append("_");
				key.append(sc.getName(null));
			}
			if (sc.testConstraint(null)) {
//				key.append("_");
				key.append(getKey1(sc.getConstraint(null)));
			}
			// XXX can be extended for total_cover 
		} else
		if (entity instanceof ESubtype_expression) {
			ESubtype_expression exp = (ESubtype_expression)entity;
			if (exp instanceof EAndor_subtype_expression) {
				key.append("EAndor_subtype_expression");
			} else
			if (exp instanceof EAnd_subtype_expression) {
				key.append("EAnd_subtype_expression");
			} else
			if (exp instanceof EOneof_subtype_expression) {
				key.append("EOneof_subtype_expression");
			}
//			key.append("_");
			key.append(getKey(exp.getGeneric_operands(null)));
		} else
		if (entity instanceof EGlobal_rule) {
			key.append("EGlobal_rule_");
			key.append(((EGlobal_rule)entity).getName(null));
		} else
		if (entity instanceof EAlgorithm_definition) {
			key.append("EAlgorithm_definition_");
			key.append(((EAlgorithm_definition)entity).getName(null));
		} else
		if (entity instanceof EConstant_definition) {
			key.append("EConstant_definition_");
			key.append(((EConstant_definition)entity).getName(null));
		} else
		if (entity instanceof EMap_definition) {
			key.append("EMap_definition_");
			key.append(((EMap_definition)entity).getName(null));
		}
		// TODO extend for other
		return key;
	}

	/**
	 * calls getKey1 for all entities in this agregate
	 * @param entities
	 * @return
	 * @throws SdaiException
	 */
	private StringBuffer getKey(AEntity entities) throws SdaiException {
		StringBuffer key = new StringBuffer();
		key.append("Agregate_");
		key.append(entities.getMemberCount());
		SdaiIterator sid = entities.createIterator();
		while (sid.next()) {
			key.append("_");
			key.append(getKey1(entities.getCurrentMemberEntity(sid)));	
		}
		return key;
	}
	
	/**
	 * entities are splitted to maps depending on model
	 */
	protected Map modelLinks = new Hashtable(); 
	
	/**
	 * returns entity instance in new repository
	 * @param modelName
	 * @param key
	 * @return
	 */
	protected EEntity getMapped(String modelName, Object key) {
		Map modelMap = (Map)modelLinks.get(modelName);
		if (modelMap == null) {
			modelMap = parseModel(modelName);
			if (modelMap != null) modelLinks.put(modelName, modelMap);
		}
		EEntity entity = null;
		if (modelMap != null) {
			entity = (EEntity)modelMap.get(key);
			if (SINGLE_LOADING && entity == null) {
				modelMap = parseModel(modelName);
				if (modelMap != null) {
					modelLinks.put(modelName, modelMap);
					entity = (EEntity)modelMap.get(key);
				}
			}
		} 
		return entity;
	}

	/**
	 * parsing model from new repository
	 * parses Entities by putting them all to map, where 
	 * key = getKey() - local method;
	 * value = entity
	 * @param modelName
	 * @return
	 */
	protected Map parseModel(String modelName) {
		if (modelLinks.size() > MAX_OPEN_MODELS) reset();
//if (modelName.startsWith("ANALYTICAL")) System.out.println("\n\nPARSE MODEL: " + modelName);		
		Map map = null;
		try {
			SdaiModel model = repoNew.findSdaiModel(modelName);
			if (model != null) {
//if (modelName.startsWith("ANALYTICAL")) System.out.println("model found: " + model);				
				if (model.getMode() == SdaiModel.NO_ACCESS)	model.startReadOnlyAccess();
				AEntity entities;
				if (SINGLE_LOADING && requered_class != null) entities = model.getInstances(requered_class);
				else entities = model.getInstances();
/*	XXX checking memory overflow 
 * possible to use this with more accurate precission
 * 2005-12-07			
				
				Runtime r = Runtime.getRuntime();
				long mem = r.maxMemory() - r.totalMemory() + r.freeMemory();
				int ents = entities.getMemberCount();
				if (mem < ents * 1600) {
					reset();
					r.gc();
					mem = r.maxMemory() - r.totalMemory() + r.freeMemory();
					if (mem < ents * 1600) {
						if (MessageDialog.openQuestion(null, "Warning", "Memory low. System can become unstable.\nIt is recomended to cancel current job and restart eclipse with more memory (example: eclipse -vmargs -Xmx512M)\nStop current job?")) {
							throw new SecurityException("Aborted by user");
						}
					}					
				}
*/
				
				SdaiIterator it = entities.createIterator();
				map = new Hashtable(entities.getMemberCount());
				while (it.next()) {
					EEntity entity = entities.getCurrentMemberEntity(it);
					Object key = getKey(entity);
//if (modelName.startsWith("ANALYTICAL")) System.out.println(entity + " <-> " + key);					
					if (key != null) map.put(key, entity);
/*if (key != null) try {					
if (!entity.findEntityInstanceSdaiModel().getName().equals(modelName) 
		|| entity.findEntityInstanceSdaiModel().getRepository() != repoNew 
		|| model != entity.findEntityInstanceSdaiModel()) 
System.err.println("Error with entity repository:\n" 
		+ repoNew + Integer.toHexString(repoNew.hashCode()) 
		+ (entity.findEntityInstanceSdaiModel().getRepository() != repoNew ? " != " : " == ")  
		+ entity.findEntityInstanceSdaiModel().getRepository() 
		+ Integer.toHexString(entity.findEntityInstanceSdaiModel().getRepository().hashCode()) + "\n" 

		+ modelName + modelName.hashCode() 
		+ (!entity.findEntityInstanceSdaiModel().getName().equals(modelName) ? " != " : " == ")
		+ entity.findEntityInstanceSdaiModel().getName() + "\n" 
		
		+ model + model.hashCode() 
		+ (model != entity.findEntityInstanceSdaiModel() ? " != " : " == ") 
		+ entity.findEntityInstanceSdaiModel() 
		+ entity.findEntityInstanceSdaiModel().hashCode() + "\n" 
		+ entity);
} catch (Throwable t) { t.printStackTrace(); }*/
				}
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			map = null;
		}
//if (modelName.startsWith("ANALYTICAL")) System.out.println("DONE.");		
		return map;
	}
	
	/**
	 * sets state to the starting state
	 * removes all EEntity temp objects and clears internal status
	 * 
	 */
	public void reset() {
		cleanTemp();
		Iterator it = modelLinks.values().iterator();
		while (it.hasNext()) {
			Map map = (Map)it.next();
			map.clear();
		}
		modelLinks.clear();
//		System.gc();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		reset();
		super.finalize();
	}
}
