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

package jsdai.xml;

import java.lang.reflect.Field;
import java.io.IOException;
import java.util.Map;

import jsdai.dictionary.*;
import jsdai.lang.*;
import jsdai.xml.InstanceReader;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXNotSupportedException;

/**
 * This class is an ISO 10303-28 Edition 1 late binding representation
 * reader. It can be used to create XML in late binding from JSDAI population.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class LateBindingReader extends InstanceReader {
	private static final int TYPE_LENGTH = "type".length();

	protected boolean featureVerboseSchemaNames;

	/**
	 * Creates the late binding reader.
	 */
	public LateBindingReader()
	{
		featureVerboseSchemaNames = false;
	}

	/**
	 * Sets the state of the feature for this <code>LateBindingReader</code>.
	 * It recognizes all features supported by {@link InstanceReader#setFeature}.
	 * In addition the following features are recognized:
	 * <dl>
	 * <dt>verbose-schema-names</dt>
	 * <dd>If true, the schema of entity or defined type is added as optional
	 *     <code>&lt;schema_name&gt;</code> attribute to <code>&lt;entity_instance&gt;</code>,
	 *     <code>&lt;partial_entity_instance&gt;</code>, and <code>&lt;type_literal&gt;</code>
	 *     elements. The default value is <code>false</code>.</dd>
	 * </dl>
	 *
	 * @param name The feature name, see the list above for recognized features.
	 * @param value The <code>true</code> or <code>false</code> value.
	 */
	public void setFeature(String name, boolean value)
	{
		if(name.equals("verbose-schema-names")) {
			featureVerboseSchemaNames = value;
		} else {
			super.setFeature(name, value);
		}
	}

	protected void setRootAttributes(Attributes attr)
	{
		attributes.addAttribute(null, "representation_category", "representation_category", "CDATA", "LB");
		attributes.addAttribute(null, "version", "version", "CDATA", "PDTS");
	}

	protected void parseExpressSchema(ESchema_definition schema, String schema_id)
			throws IOException, SAXException, SdaiException
	{
		//well it does nothing, but it has to be overriden to make use of it
	}

	protected void parseSchInst(SchemaInstance currSchInst, Map mIds, Map sIds)
			throws IOException, SAXException, SdaiException
	{
		attributes.clear();
		attributes.addAttribute(null, "governing_schema", "governing_schema", "IDREF",
				currSchInst.getNativeSchema().getName(null));
		ASdaiModel assocMods = currSchInst.getAssociatedModels();
		SdaiIterator assocModIter = assocMods.createIterator();
		StringBuffer governedSections = new StringBuffer();
		while(assocModIter.next()) {
			SdaiModel assocMod = assocMods.getCurrentMember(assocModIter);
			Object modelId = mIds.get(assocMod);
			if(modelId != null) {
				if(governedSections.length() > 0) {
					governedSections.append(' ');
				}
				governedSections.append((String)modelId);
			} else {
				throw
						new SAXNotSupportedException("Schema instance " + currSchInst.toString() +
						" containes models that are not in the exported scope: " +
						assocMod.toString());
			}
		}
		attributes.addAttribute(null, "governed_sections", "governed_sections", "IDREFS",
				governedSections.toString());
		if(featureSchemaPopulationNames) {
			attributes.addAttribute(null, "name", "name", "NMTOKEN",
					currSchInst.getName());
		}
		contentHandler.startElement(null, "schema_population", "schema_population", attributes);
		contentHandler.endElement(null, "schema_population", "schema_population");
	}

	protected void parseModel(SdaiModel currModel, String currModelId, String nativeSchemaId)
	throws IOException, SAXException, SdaiException
	{
		boolean noAccess = currModel.getMode() == SdaiModel.NO_ACCESS;
		if(noAccess) {
			currModel.startReadOnlyAccess();
		}
		try {
			attributes.clear();
			attributes.addAttribute(null, "name", "name", "CDATA",
					currModel.getName());
			attributes.addAttribute(null, "id", "id", "ID",
					currModelId);
			contentHandler.startElement(null, "express_data", "express_data", attributes);

			attributes.clear();
			attributes.addAttribute(null, "schema_name", "schema_name", "NMTOKEN",
					currModel.getUnderlyingSchema().getName(null));
			contentHandler.startElement(null, "schema_instance", "schema_instance", attributes);

			parseModelInstances(currModel);

			contentHandler.endElement(null, "schema_instance", "schema_instance");
			contentHandler.endElement(null, "express_data", "express_data");
		} finally {
			if(noAccess) {
				currModel.endReadOnlyAccess();
			}
		}

	}

	private void parseModelInstances(SdaiModel currModel)
	throws SdaiException, SAXException {
		AEntity instances = currModel.getInstances();
		SdaiIterator instanceIter = instances.createIterator();
		while(instanceIter.next()) {
			EEntity instance = instances.getCurrentMemberEntity(instanceIter);
			EEntity_definition definition = instance.getInstanceType();
			attributes.clear();
			attributes.addAttribute(null, "id", "id", "ID",
									"e" + Long.toString(getInstanceIdentifier(instance)));
			attributes.addAttribute(null, "express_entity_name", "express_entity_name", "NMTOKEN",
									definition.getName(null));
			if(featureVerboseSchemaNames) {
				attributes.addAttribute(null, "schema_name", "schema_name", "NMTOKEN",
										getDefinitionSchemaName(definition));
			}
			contentHandler.startElement(null, "entity_instance", "entity_instance", attributes);
			EExplicit_attribute[] entityAttr = getEntityExplicitAttributes(definition);
			Field[] entityAttrFields = getEntityAttributeFields(definition);
			EEntity_definition partialEntType = definition;
			for(int i = entityAttr.length - 1; i >= 0 ; i--) {
				if(entityAttrFields[i] == null) continue;
				EEntity_definition attrEntType = entityAttr[i].getParent_entity(null);
				if(attrEntType != partialEntType) {
					if(partialEntType != definition) {
						contentHandler.endElement(null, "partial_entity_instance",
												  "partial_entity_instance");
					}
					attributes.clear();
					attributes.addAttribute(null, "express_entity_name", "express_entity_name",
											"NMTOKEN", attrEntType.getName(null));
					if(featureVerboseSchemaNames) {
						attributes.addAttribute(null, "schema_name", "schema_name", "NMTOKEN",
												getDefinitionSchemaName(attrEntType));
					}
					contentHandler.startElement(null, "partial_entity_instance",
												"partial_entity_instance", attributes);
					partialEntType = attrEntType;
				}
				parseAttributes(instance, entityAttr[i], partialEntType != definition);
			}
			if(partialEntType != definition) {
				contentHandler.endElement(null, "partial_entity_instance", "partial_entity_instance");
			}
			contentHandler.endElement(null, "entity_instance", "entity_instance");
// 			contentHandler.ignorableWhitespace("\n".toCharArray(), 0, 1);
		}
	}

	private void parseAttributes(EEntity instance, EExplicit_attribute entityAttr, boolean inherited)
	throws SdaiException, SAXException {
		EDefined_type[] entityAttrSelect = new EDefined_type[20];
		String entityAttrElemName = inherited ? "inherited_attribute_instance" : "attribute_instance";
		attributes.clear();
		attributes.addAttribute(null, "express_attribute_name", "express_attribute_name", "NMTOKEN",
								entityAttr.getName(null));
		contentHandler.startElement(null, entityAttrElemName, entityAttrElemName, attributes);
		if(instance.testAttribute(entityAttr, entityAttrSelect) != 0) {
			Object value = instance.get_object(entityAttr);
			EEntity domain = entityAttr.getDomain(null);
			parseValue(value, domain, entityAttrSelect);
		} else {
			attributes.clear();
			contentHandler.startElement(null, "unset", "unset", attributes);
			contentHandler.endElement(null, "unset", "unset");
		}
		contentHandler.endElement(null, entityAttrElemName, entityAttrElemName);
	}

	private void parseValue(Object value, EEntity domain, EDefined_type[] select)
	throws SdaiException, SAXException {
		if(domain == null || domain instanceof EEntity_definition) {
			EEntity instance = (EEntity)value;
			attributes.clear();
			attributes.addAttribute(null, "refid", "refid", "IDREF",
									"e" + Long.toString(getInstanceIdentifier(instance)));
			contentHandler.startElement(null, "entity_instance_ref",
										"entity_instance_ref", attributes);
			contentHandler.endElement(null, "entity_instance_ref", "entity_instance_ref");
		} else if(domain instanceof ESimple_type) {
			String simpleName = domain.getInstanceType().getName(null);
			simpleName = getLiteralElementName(simpleName);
			attributes.clear();
			contentHandler.startElement(null, simpleName, simpleName, attributes);
			if(domain instanceof ELogical_type || domain instanceof EBoolean_type) {
				String valueElemName = ELogical.toString(((Integer)value).intValue()).toLowerCase();
				contentHandler.startElement(null, valueElemName, valueElemName, attributes);
				contentHandler.endElement(null, valueElemName, valueElemName);
			} else {
				String attrString = value.toString();
				if(attrString.length() > 0) {
					contentHandler.characters(attrString.toCharArray(), 0, attrString.length());
				}
			}
			contentHandler.endElement(null, simpleName, simpleName);
		} else if(domain instanceof EAggregation_type) {
			String aggregateName = domain.getInstanceType().getName(null);
			aggregateName = getLiteralElementName(aggregateName);
			EEntity elementType = ((EAggregation_type)domain).getElement_type(null);
			attributes.clear();
			contentHandler.startElement(null, aggregateName, aggregateName, attributes);
			Aggregate aggregate = (Aggregate)value;
			SdaiIterator aggregateIter = aggregate.createIterator();
			EDefined_type[] memberSelect = new EDefined_type[20];
			while(aggregateIter.next()) {
				int memberType = aggregate.testCurrentMember(aggregateIter, memberSelect);
				switch(memberType) {
				case 0: {
					attributes.clear();
					contentHandler.startElement(null, "unset", "unset", attributes);
					contentHandler.endElement(null, "unset", "unset");
					break;
				}
				case 1: {
					Object aggregateMember = aggregate.getCurrentMemberObject(aggregateIter);
					parseValue(aggregateMember, elementType, memberSelect);
					break;
				}
				case 2: {
					int aggregateMember = aggregate.getCurrentMemberInt(aggregateIter);
					parseValue(new Integer(aggregateMember), elementType, memberSelect);
					break;
				}
				case 3: {
					double aggregateMember = aggregate.getCurrentMemberDouble(aggregateIter);
					parseValue(new Double(aggregateMember), elementType, memberSelect);
					break;
				}
				case 4: {
					boolean aggregateMember = aggregate.getCurrentMemberBoolean(aggregateIter);
					parseValue(Boolean.valueOf(aggregateMember), elementType, memberSelect);
					break;
				}
				}
			}
			contentHandler.endElement(null, aggregateName, aggregateName);
		} else if(domain instanceof ESelect_type) {
			int selectIdx;
			EDefined_type selectType = null;
			for(selectIdx = 0; select[selectIdx] != null; selectIdx++) {
				selectType = select[selectIdx];
				attributes.clear();
				attributes.addAttribute(null, "express_type_name", "express_type_name", "NMTOKEN",
										selectType.getName(null));
				if(featureVerboseSchemaNames) {
					attributes.addAttribute(null, "schema_name", "schema_name", "NMTOKEN",
											getDefinitionSchemaName(selectType));
				}
				contentHandler.startElement(null, "type_literal", "type_literal", attributes);
			}
			parseValue(value, selectType != null ? selectType.getDomain(null) : null, select);
			for(selectIdx = 0; select[selectIdx] != null; selectIdx++) {
				contentHandler.endElement(null, "type_literal", "type_literal");
			}
		} else if(domain instanceof EEnumeration_type) {
			EEnumeration_type enumType = (EEnumeration_type)domain;
			int enumValue = ((Integer)value).intValue();
			String enumElement = enumType.getElements(null).getByIndex(enumValue);
			attributes.clear();
			contentHandler.startElement(null, "enumeration_ref", "enumeration_ref", attributes);
			contentHandler.characters(enumElement.toCharArray(), 0, enumElement.length());
			contentHandler.endElement(null, "enumeration_ref", "enumeration_ref");
		} else if(domain instanceof EDefined_type) {
			EDefined_type definedType = (EDefined_type)domain;
			attributes.clear();
			attributes.addAttribute(null, "express_type_name", "express_type_name",
									"NMTOKEN", definedType.getName(null));
			if(featureVerboseSchemaNames) {
				attributes.addAttribute(null, "schema_name", "schema_name", "NMTOKEN",
										getDefinitionSchemaName(definedType));
			}
			contentHandler.startElement(null, "type_literal", "type_literal", attributes);
			parseValue(value, definedType.getDomain(null), select);
			contentHandler.endElement(null, "type_literal", "type_literal");
		}
	}

	protected String getLiteralElementName(String expressId) {
// 		Object literalName = literalNames.get(expressId);
// 		if(literalName != null) {
// 			return (String)literalName;
// 		}
		String literalNameStr = expressId.substring(0, expressId.length() - TYPE_LENGTH) + "literal";
// 		literalNames.put(expressId, literalNameStr);
		return literalNameStr;
	}

} // LateBindingReader
