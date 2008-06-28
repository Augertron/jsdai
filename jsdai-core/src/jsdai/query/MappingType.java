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

package jsdai.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.ASdaiModel;
import jsdai.lang.CEntityDefinition;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.CEntity_mapping;
import jsdai.mapping.EEntity_mapping;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 12:55:01 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class MappingType extends Type {
	protected ASdaiModel mappingDomain;
	protected ESchema_definition schema;
	protected EEntity_definition entity;

	MappingType(ASdaiModel mappingDomain, ESchema_definition schema) {
		this.mappingDomain = mappingDomain;
		this.schema = schema;
	}

	protected void setParameters(String entityName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		entity = JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, entityName);
		AEntity_mapping entityMappings = new AEntity_mapping();
		CEntity_mapping.usedinSource(null, entity, mappingDomain, entityMappings);
		SdaiIterator entMappingIter = entityMappings.createIterator();
		ListIterator ctsIter = localContext.currentTypes.listIterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Iterator currTypeIter = cType.iterator();
			Set typeSet = new HashSet();
			while(currTypeIter.hasNext()) {
				Object currTypeObject = currTypeIter.next();
				EEntity_definition currType = 
					currTypeObject == LocalSdaiQuery.allTypesToken ? null : 
					LocalContext.getSchemaEntityType(currTypeObject);
				entMappingIter.beginning();
				while(entMappingIter.next()) {
					EEntity_mapping entityMapping = entityMappings.getCurrentMember(entMappingIter);
					EEntity targetType = entityMapping.getTarget(null);
					if(currType == null 
					   || currType == targetType
					   || (targetType instanceof EEntity_definition 
						   && ((CEntityDefinition)targetType).isSubtypeOf(currType))) {
						typeSet.add(LocalContext.entityType(entityMapping));
					}
				}
			}
			ctsIter.set(typeSet);
		}
	}


	/**
	 * Execute mapping based <i>type</i> constraint
	 *
	 * @param context executiom context
	 * @exception SdaiException if an error occurs
	 */
	public void execute(Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		Iterator cisIter = localContext.currentTypes.iterator();
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				AEntity_mapping instMappings = 
					currInst.testMappedEntity(entity, localContext.getLocalQuery().domain, 
											  mappingDomain, EEntity.NO_RESTRICTIONS);
				if(instMappings == null) {
					currInstIter.remove();
				} else if(exact) {
					boolean matches = false;
					AEntity_mapping specificMappings =
						currInst.findMostSpecificMappings(localContext.getLocalQuery().domain,
														  mappingDomain, instMappings, 
														  EEntity.NO_RESTRICTIONS);
					SdaiIterator mappingIter = specificMappings.createIterator();
					while(mappingIter.next()) {
						EEntity_mapping mapping = specificMappings.getCurrentMember(mappingIter);
						if(mapping.getSource(null) == entity) {
							matches = true;
							break;
						}
					}
					if(!matches) {
						currInstIter.remove();
					}
				}
			}
		}
		executeChildren(context, true);
	}
	
} // MappingType
