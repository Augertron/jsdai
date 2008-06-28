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
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.mapping.AAttribute_mapping;
import jsdai.mapping.CAttribute_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 16:46:40 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class MappingInv extends Inv {
	protected ASdaiModel mappingDomain;
	protected ESchema_definition schema;
	protected AAttribute_mapping attributeMappings;

	MappingInv(ASdaiModel mappingDomain, ESchema_definition schema) {
		this.mappingDomain = mappingDomain;
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;

		EEntity_definition entity = JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, entityName);
		EAttribute attribute = JsdaiLangAccessorLocal.findAttribute(entity, attributeName);
		attributeMappings = new AAttribute_mapping();
		CAttribute_mapping.usedinSource(null, attribute, mappingDomain, attributeMappings);
		SdaiIterator attrMappingIter = attributeMappings.createIterator();
		Iterator ctsIter = localContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			if(cType.isEmpty()) continue;
			cType.clear();
			attrMappingIter.beginning();
			while(attrMappingIter.next()) {
				EAttribute_mapping attributeMapping = attributeMappings.getCurrentMember(attrMappingIter);
				cType.add(LocalContext.entityType(attributeMapping.getParent_entity(null)));
			}
		}
	}

	/**
	 * Execute mapping based <i>inv</i> constraint
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
			Set invSet = new HashSet();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				AEntity invVal = 
					currInst.findMappedUsers(null, attributeMappings, 
											 localContext.getLocalQuery().domain, mappingDomain, 
											 null, EEntity.NO_RESTRICTIONS);
				SdaiIterator invValIter = invVal.createIterator();
				while(invValIter.next()) {
					Object invInst = invVal.getCurrentMemberObject(invValIter);
					invSet.add(LocalContext.entityInstance((EEntity)invInst));
				}
			}
			cisIter.set(invSet);
		}
		executeChildren(context, true);
	}
	
} // MappingInv
