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

import java.util.ArrayList;
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
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.CGeneric_attribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 16:36:13 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class MappingVal extends Val {
	protected ASdaiModel mappingDomain;
	protected ESchema_definition schema;
	protected EAttribute attribute;

	MappingVal(ASdaiModel mappingDomain, ESchema_definition schema) {
		this.mappingDomain = mappingDomain;
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName, String selectNames,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;

		attribute = 
			LocalSdaiQuery.getAttribute(schema, entityName, attributeName,
										containerNode, localContext, true);

		// Getting select path
		if(selectNames != null) {
			throw new SdaiException(SdaiException.AT_NVLD, SdaiQueryEngine.formatMessage
									(containerNode,
									 "Attribute select path is not supported in mapping val constraint",
									 selectNames));
		}

		AGeneric_attribute_mapping genAttrMappings = new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinSource(null, attribute, mappingDomain, genAttrMappings);
		SdaiIterator genAttrMappingIter = genAttrMappings.createIterator();
		Iterator ctsIter = localContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			if(cType.isEmpty()) continue;
			boolean foundValues = false;
			genAttrMappingIter.beginning();
			while(genAttrMappingIter.next()) {
				EGeneric_attribute_mapping genAttrMapping = 
					genAttrMappings.getCurrentMember(genAttrMappingIter);
				if(genAttrMapping instanceof EAttribute_mapping) {
					EAttribute_mapping attrMapping = (EAttribute_mapping)genAttrMapping;
					if(attrMapping.testDomain(null)) {
						EEntity domain = attrMapping.getDomain(null);
						if(!(domain instanceof EEntity_mapping)
						   && !(domain instanceof EEntity_definition)) {
							foundValues = true;
						}
					} else {
						foundValues = true;
					}
				}
			}
			if(!foundValues) {
				throw new SdaiException(SdaiException.AT_NVLD, SdaiQueryEngine
										.formatMessage(containerNode,
													   "Attribute is a reference to the instance",
													   attribute.toString()));
			}
		}
	}

	/**
	 * Execute mapping based <i>val</i> constraint
	 *
	 * @param context executiom context
	 * @exception SdaiException if an error occurs
	 */
	public void execute(Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		ListIterator cisIter = localContext.currentTypes.listIterator();
		localContext.values = new ArrayList();
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				Object[] instVals = 
					currInst.getMappedAttribute(attribute, localContext.getLocalQuery().domain, 
												mappingDomain, EEntity.NO_RESTRICTIONS);
				boolean matched = false;
				if(instVals != null) {
					for(int i = 0; i < instVals.length; i++) {
						Object instVal = instVals[i];
						if(!(instVal instanceof EEntity)) {
							localContext.values.add(instVal);
							matched = true;
						}
					}
				}
				if(!matched) {
					currInstIter.remove();
				}
			}
		}

		boolean executeValueResult = valueResult && context.contextConstraint.isValueResult();
		if((containsValConstraints() || context.contextConstraint.containsValConstraints() 
			|| executeValueResult) && !localContext.values.isEmpty()) {

			Iterator childValIter;
			if(executeValueResult) {
				childValIter = localContext.values.iterator();
			} else {
				Context childContext = context.dupVal();
				if(constraints != null) {
					constraints.execute(childContext);
				}
				if(this != context.contextConstraint) {
					context.contextConstraint.executeChildren(childContext, false);
				}
				childValIter = ((LocalContext)childContext).values.iterator();
			}

			cisIter = localContext.currentTypes.listIterator();
			while(cisIter.hasNext()) {
				Set cis = (Set)cisIter.next();
				Iterator currInstIter = cis.iterator();
				if(executeValueResult) {
					Set valueSet = new HashSet();
					while(currInstIter.hasNext()) {
						currInstIter.next();
						Object childVal = childValIter.next();
						if(childVal != null) {
							valueSet.add(childVal);
						}
					}
					cisIter.set(valueSet);
				} else {
					while(currInstIter.hasNext()) {
						currInstIter.next();
						Object childVal = childValIter.next();
						if(childVal == null) {
							currInstIter.remove();
						}
					}
				}
			}
		}
	}
	
} // MappingVal
