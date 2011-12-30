/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jsdai.dictionary.AEntity_definition;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

/**
 * @author Eva
 *
 */
public class Utilities {
	
	private Utilities() {}
	
	public static List findInconsistentEntities(AEntity instances, String schemaName) throws SdaiException {
		APInstancesProcessor processor = null;
		if(schemaName.startsWith("automotive_design")){
			processor = AP214Entities.getInstance();
		}else if(schemaName.startsWith("ap203")){
			processor = AP203Entities.getInstance();
		}else if(schemaName.startsWith("ap242")){
			processor = AP242Entities.getInstance();
		}

		if(processor != null){
			return processor.findInconsistentEntities(instances);
		}
		
		List inconsistentEntities = new ArrayList(); 
		HashMap testedDefinitions = new HashMap();

		
		SdaiRepository systemRepo = SdaiSession.getSession().getSystemRepository();
		SdaiModel dictMod = systemRepo.findSdaiModel(String.valueOf(schemaName + "_DICTIONARY_DATA").toUpperCase());
		dictMod.startReadOnlyAccess();
		ESchema_definition schema = dictMod.getDefinedSchema();
		SdaiIterator it = instances.createIterator();
		while (it.next()) {
			EEntity instance = instances.getCurrentMemberEntity(it);
			
			EEntity_definition eDef = instance.getInstanceType();

			Boolean wasConsistent = (Boolean) testedDefinitions.get(eDef);
			if (wasConsistent == null) {
				// still not checked ..
				// check for availability of eDefinition
				wasConsistent = new Boolean(schema.testEntityDefinition(eDef.getName(null)));
				testedDefinitions.put(eDef, wasConsistent);
			}

			if (!wasConsistent.booleanValue()) {
				inconsistentEntities.add(instance);
				continue;
			}
		}
		
		return inconsistentEntities;
	}

	/**
	 * Returns <code>Set</code> of non-abstract <code>EEntity_definition</code>s that are supertypes
	 * of the specified entity definition and are declared in the specified schema. If the
	 * specified entity definition is itself declared in the specified schema, then the
	 * returned <code>Set</code> will contain it as an only element.
	 * 
	 * @param schemaName specified schema name.
	 * @param eInputType specified entity definition.
	 * @return <code>Set</code> of non-abstract <code>EEntity_definition</code>s.
	 * @throws SdaiException
	 */
	public static Set getConsistentTypes(String schemaName,
		EEntity_definition eInputType) throws SdaiException {
		
		if (schemaName == null) {
			throw new IllegalArgumentException();
		}
		if (eInputType == null) {
			throw new IllegalArgumentException();
		}
		
		SdaiRepository systemRepo = SdaiSession.getSession().getSystemRepository();
		SdaiModel dictMod = systemRepo.findSdaiModel(String.valueOf(schemaName + "_DICTIONARY_DATA").toUpperCase());
		dictMod.startReadOnlyAccess();
		ESchema_definition schema = dictMod.getDefinedSchema();

		Set consistentTypes = new HashSet();
		Set uncheckedTypes = new HashSet();
		uncheckedTypes.add(eInputType);
		while (uncheckedTypes.size() > 0) {
			Iterator iterator = uncheckedTypes.iterator();
			EEntity_definition eDef = (EEntity_definition) iterator.next();
			iterator.remove();

			if (schema.testEntityDefinition(eDef.getName(null))) {
				if (!eDef.getAbstract_entity(null)) {
					consistentTypes.add(eDef);
				}
			} else {
				AEntity_definition aSupertypes = eDef.getSupertypes(null);
				for (SdaiIterator i = aSupertypes.createIterator(); i.next();) {
					EEntity_definition eSupertype = aSupertypes.getCurrentMember(i);
					if (!consistentTypes.contains(eSupertype)) {
						uncheckedTypes.add(eSupertype);
					}
				}
			}
		}
		
		return consistentTypes;
	}

	private static ASdaiModel getSystemDomain(String schemaName) throws SdaiException {

		ASdaiModel domain = new ASdaiModel();
		SdaiRepository systemRepo = SdaiSession.getSession().getSystemRepository();
		SdaiModel systemModel = systemRepo.findSdaiModel(
			String.valueOf(schemaName + "_DICTIONARY_DATA").toUpperCase());
		if (systemModel == null) {
			return null;
		}
		if (systemModel.getMode() == SdaiModel.NO_ACCESS) {
			systemModel.startReadOnlyAccess();
		}

		domain.addUnordered(systemModel, null);
		return domain;
	}

}
