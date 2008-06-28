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
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.mapping.AAttribute_mapping;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.CEntity_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 16:21:29 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class MappingFwd extends Fwd {
	protected ASdaiModel mappingDomain;
	protected ESchema_definition schema;
	protected EAttribute attribute;

	MappingFwd(ASdaiModel mappingDomain, ESchema_definition schema) {
		this.mappingDomain = mappingDomain;
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName, String targetName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
//		SdaiModel owningModel = localContext.getLocalQuery().owningModel;

		attribute = LocalSdaiQuery.getAttribute(schema, entityName, attributeName, 
												containerNode, localContext, true);

		// Getting target
		if(targetName != null) {
			EEntity_definition targetEntity =
				JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, targetName);
			AEntity_mapping targetEntMappings = new AEntity_mapping();
			CEntity_mapping.usedinSource(null, targetEntity, mappingDomain, targetEntMappings);
			SdaiIterator targetEntMappingIter = targetEntMappings.createIterator();
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(cType.isEmpty()) continue;
				cType.clear(); //FIXME: Check if it matches with actual type
				targetEntMappingIter.beginning();
				while(targetEntMappingIter.next()) {
					EEntity_mapping targetEntMapping = 
						targetEntMappings.getCurrentMember(targetEntMappingIter);
					cType.add(LocalContext.entityType(targetEntMapping));
				}
			}
		} else {
			AGeneric_attribute_mapping genAttrMappings = new AGeneric_attribute_mapping();
			CGeneric_attribute_mapping.usedinSource(null, attribute, mappingDomain, genAttrMappings);
			AAttribute_mapping attrMappings = new AAttribute_mapping();
			SdaiIterator genAttrMappingIter = genAttrMappings.createIterator();
			while(genAttrMappingIter.next()) {
				EGeneric_attribute_mapping genAttrMapping = 
					genAttrMappings.getCurrentMember(genAttrMappingIter);
				if(genAttrMapping instanceof EAttribute_mapping) {
					attrMappings.addUnordered(genAttrMapping);
				}
			}
			SdaiIterator attrMappingIter = attrMappings.createIterator();
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(cType.isEmpty()) continue;
				cType.clear();
				attrMappingIter.beginning();
				while(attrMappingIter.next()) {
					EAttribute_mapping attrMapping = attrMappings.getCurrentMember(attrMappingIter);
					if(attrMapping.testDomain(null)) {
						EEntity domain = attrMapping.getDomain(null);
						if(domain instanceof EEntity_mapping) {
							cType.add(LocalContext.entityType((EEntity_mapping)domain));
						} else if(domain instanceof EEntity_definition) {
							cType.add(LocalContext.entityType((EEntity_definition)domain));
						}
					}
				}
				if(cType.isEmpty()) {
					throw new SdaiException(SdaiException.AT_NVLD, SdaiQueryEngine
											.formatMessage(containerNode,
														   "Attribute is not a reference to the instance",
														   attribute.toString()));
				}
			}
		}
	}

	/**
	 * Execute mapping based <i>fwd</i> constraint
	 *
	 * @param context executiom context
	 * @exception SdaiException if an error occurs
	 */
	public void execute(Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		ListIterator cisIter = localContext.currentTypes.listIterator();
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			Set fwdSet = new HashSet();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				Object[] fwdVals = 
					currInst.getMappedAttribute(attribute, localContext.getLocalQuery().domain, 
												mappingDomain, EEntity.NO_RESTRICTIONS);
				if(fwdVals != null) {
					for(int i = 0; i < fwdVals.length; i++) {
						Object fwdVal = fwdVals[i];
						if(fwdVal instanceof EEntity) {
							fwdSet.add(LocalContext.entityInstance((EEntity)fwdVal));
						}
					}
				}
			}
			cisIter.set(fwdSet);
		}
		executeChildren(context, true);
	}
	
} // MappingFwd
