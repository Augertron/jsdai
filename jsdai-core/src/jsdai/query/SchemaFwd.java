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
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 10:26:39 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class SchemaFwd extends Fwd {
	protected ESchema_definition schema;
	protected EAttribute attribute;

	SchemaFwd(ESchema_definition schema) {
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName, String targetName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
//		SdaiModel owningModel = localContext.getLocalQuery().owningModel;

		attribute = 
			LocalSdaiQuery.getAttribute(schema, entityName, attributeName,
										containerNode, localContext, false);

		// Getting target
		if(targetName != null) {
			EEntity_definition targetEntity =
				JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, targetName);
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(cType.isEmpty()) continue;
				cType.clear(); //FIXME: Check if it matches with actual type
				cType.add(LocalContext.entityType(targetEntity));
			}
		} else {
			EEntity domain = (EEntity)attribute.get_object(attribute.getAttributeDefinition("domain"));
			Set finalTypes = new HashSet();
			((LocalSdaiQuery)context.query).getFinalDomainTypes(domain, finalTypes);
			Iterator ctsIter = localContext.currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(cType.isEmpty()) continue;
				cType.clear();
				Iterator ftIter = finalTypes.iterator();
				while(ftIter.hasNext()) {
					Object finalType = ftIter.next();
					if(finalType instanceof EEntity_definition) {
						cType.add(LocalContext.entityType((EEntity_definition)finalType));
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

	protected void execute(Context context) throws SdaiException {
		SchemaFwd.execute(context, attribute);
		executeChildren(context, true);
	}

	protected void executeInv(Context context) throws SdaiException {
		executeChildren(context, true);
		SchemaInv.execute(context, attribute);
	}

	static protected void execute(Context context, EAttribute attribute) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		ListIterator cisIter = localContext.currentTypes.listIterator();
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			Set fwdSet = new HashSet();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				Object fwdVal = currInst.get_object(attribute);
				if(fwdVal instanceof Aggregate) {  // FIXME: Enhance aggregate support
					Aggregate fwdAgg = (Aggregate)fwdVal;
					SdaiIterator fwdAggIter = fwdAgg.createIterator();
					while(fwdAggIter.next()) {
						Object fwdAggVal = fwdAgg.getCurrentMemberObject(fwdAggIter);
						if(fwdAggVal instanceof EEntity) {
							fwdSet.add(LocalContext.entityInstance((EEntity)fwdAggVal));
						}
					}
				} else if(fwdVal instanceof EEntity) {
					fwdSet.add(LocalContext.entityInstance((EEntity)fwdVal));
				}
			}
			cisIter.set(fwdSet);
		}
	}

} // SchemaFwd
