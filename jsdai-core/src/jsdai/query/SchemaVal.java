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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.Aggregate;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 10:31:14 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class SchemaVal extends Val {
	protected ESchema_definition schema;
	protected EAttribute attribute;
	protected EDefined_type selects[];

	SchemaVal(ESchema_definition schema) {
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName, String selectNames,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;

		attribute = 
			LocalSdaiQuery.getAttribute(schema, entityName, attributeName,
										containerNode, localContext, false);

		// Getting select path
		if(selectNames != null) {
			StringTokenizer selTokenizer = new StringTokenizer(selectNames);
			List selectList = new ArrayList();
			while (selTokenizer.hasMoreTokens()) {
				String selectStr = selTokenizer.nextToken();
				EDefined_type selectType = 
					JsdaiLangAccessorLocal.findSchemaDefinedType(schema, selectStr);
				selectList.add(selectType);
			}
			selects = (EDefined_type[])selectList.toArray(new EDefined_type[selectList.size()]);
		} else {
			selects = null;
		}

		EEntity domain = (EEntity)attribute.get_object(attribute.getAttributeDefinition("domain"));
		Set finalTypes = new HashSet();
		((LocalSdaiQuery)context.query).getFinalDomainTypes(domain, finalTypes);
		boolean simpleType = false;
		Iterator ftIter = finalTypes.iterator();
		while(ftIter.hasNext()) {
			Object finalType = ftIter.next();
			if(!(finalType instanceof EEntity_definition)) {
				simpleType = true;
				break;
			}
		}
		if(!simpleType) {
			throw new SdaiException(SdaiException.AT_NVLD, SdaiQueryEngine
									.formatMessage(containerNode,
												   "Attribute is a reference to the instance",
												   attribute.toString()));
		}
		// According to specification val emits the input type list
// 		Iterator ctsIter = localContext.currentTypes.iterator();
// 		while(ctsIter.hasNext()) {
// 			Set cType = (Set)ctsIter.next();
// 			cType.clear();
// 		}
	}

	protected void execute(Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		ListIterator cisIter = localContext.currentTypes.listIterator();
		localContext.values = new ArrayList();
		EDefined_type[] testSelects = new EDefined_type[20];
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				Arrays.fill(testSelects, null);
				int type = currInst.testAttribute(attribute, testSelects);
				if(type != 0 && selects != null) {
					for (int i = 0; i < selects.length; i++) {
						if(selects[i] != testSelects[i]) {
							type = 0;
							break;
						}
					}
				}
				switch (type) {
				case 0: //unset
					currInstIter.remove();
					break;

				case 1: //Object
					Object instVal = currInst.get_object(attribute);
					// FIXME: Add support for the selects of entities and simple types
					if(instVal instanceof Aggregate) {  // FIXME: Enhance aggregate support
						Aggregate instAgg = (Aggregate)instVal;
						SdaiIterator instAggIter = instAgg.createIterator();
						Object[] valArr = new Object[instAgg.getMemberCount()];
						int valArrIdx = 0;
						while(instAggIter.next()) {
							Object instAggVal = instAgg.getCurrentMemberObject(instAggIter);
							valArr[valArrIdx++] = instAggVal;
						}
						localContext.values.add(valArr);
					} else {
						localContext.values.add(instVal);
					}
					break;

				case 2: //int
					localContext.values.add(Integer.toString(currInst.get_int(attribute)));
					break;

				case 3: //double
					localContext.values.add(Double.toString(currInst.get_double(attribute)));
					break;

				case 4: //boolean
					localContext.values.add(Boolean.toString(currInst.get_boolean(attribute)));
					break;

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

	protected void executeChildren(Context context, boolean split) throws SdaiException {
	}

} // SchemaVal
