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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import jsdai.dictionary.*;
import jsdai.lang.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author : Kazimieras Vaina
 */
public class EarlyBindingV2Reader
 		extends InstanceReader {

	public static final String BINARY = "ex:hexBinary-wrapper"; //not supported
	public static final String BOOLEAN = "ex:boolean-wrapper";
	public static final String INTEGER = "ex:long-wrapper";
	public static final String NUMBER = "ex:decimal-wrapper";
	public static final String STRING = "ex:string-wrapper";
	public static final String LOGICAL = "ex:logical-wrapper";
	public static final String REAL = "ex:double-wrapper";

	public static final String BOOLEAN_LIST = "Seq-boolean";
	public static final String INTEGER_LIST = "Seq-long";
	public static final String NUMBER_LIST = "Seq-decimal";
	public static final String STRING_LIST = "Seq-string";
	public static final String LOGICAL_LIST = "Seq-logical";
	public static final String REAL_LIST = "Seq-double";
	public static final String AGGREGATE_LIST = "Seq-aggregate";
	public static final String LIST_PREFIX = "Seq-";

	protected void setRootAttributes(Attributes attr)
	{
		defaultNamespacePrefix = "ex:";
		attributes.addAttribute(null, "version", "version", "CDATA", "2.0");
		attributes.addAttribute(null, "xmlns", "xmlns:ex", "CDATA", "urn:iso10303-28:ex");
		attributes.addAttribute(null, "xmlns", "xmlns:xsi", "CDATA", "http://www.w3.org/2001/XMLSchema");
	}

	protected void parseExpressSchema(ESchema_definition schema, String schema_id)
			throws IOException, SAXException, SdaiException
	{
		attributes.clear();
		attributes.addAttribute(null, "id", "id", "ID", schema_id);
		String schema_name = schema.getName(null);
		attributes.addAttribute(null, "schema_name", "schema_name", "CDATA", schema_name);
        if(schema.testIdentification(null))
			attributes.addAttribute(null, "schema_version", "schema_version", "CDATA", schema.getIdentification(null));
//		String schema_version =  //no versio attribute at this moment
		attributes.addAttribute(null, "schemaLocation", "schemaLocation", "CDATA",
				"URI:www.lksoft.com/Schema/" + schema_name + ".xml");
		attributes.addAttribute(null, "xsi:nil", "xsi:nil", "CDATA", "true");
		contentHandler.startElement(null, "express", "ex:express", attributes);
		contentHandler.endElement(null, "express", "ex:express");
	}

	protected void parseSchInst(SchemaInstance currSchInst, Map mIds, Map sIds)
			throws IOException, SAXException, SdaiException
	{
		attributes.clear();
		String governing_schema = String.valueOf(sIds.get(currSchInst.getNativeSchema()));
		attributes.addAttribute(null, "governing_schema", "governing_schema", "IDREF",	governing_schema);
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
		attributes.addAttribute(null, "determination_methods", "determination_methods", "CDATA",
				"section_boundary");
		if(featureSchemaPopulationNames) {
			attributes.addAttribute(null, "name", "name", "NMTOKEN",
					currSchInst.getName());
		}
		contentHandler.startElement(null, "schema_population", "ex:schema_population", attributes);
		contentHandler.endElement(null, "schema_population", "ex:schema_population");
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
			attributes.addAttribute(null, "id", "id", "ID",	currModelId);
			attributes.addAttribute(null, "express", "express", "IDREFS", nativeSchemaId);

			contentHandler.startElement(null, "uos", "ex:uos", attributes);

			attributes.clear();
			parseModelInstances(currModel);

			contentHandler.endElement(null, "uos", "ex:uos");
		} finally {
			if(noAccess) {
				currModel.endReadOnlyAccess();
			}
		}

	}

	protected void parseModelInstances(SdaiModel currModel)
			throws SdaiException, SAXException
	{
		AEntity instances = currModel.getInstances();
		SdaiIterator instanceIter = instances.createIterator();
		while(instanceIter.next()) {
			EEntity instance = instances.getCurrentMemberEntity(instanceIter);
			EEntity_definition definition = instance.getInstanceType();

			attributes.clear();
			attributes.addAttribute(null, "id", "id", "ID", getID(instance)); //the only attribute I guess

			String theElement = formatEntityName(definition.getName(null));
			contentHandler.startElement(null, theElement, theElement, attributes);
			EExplicit_attribute[] entityAttr = getEntityExplicitAttributes(definition);
			Field[] entityAttrFields = getEntityAttributeFields(definition);
			Map entAttrNameAmbiguity = new HashMap(entityAttr.length);
			for(int i = entityAttr.length - 1; i >= 0 ; i--) {
				if(entityAttrFields[i] != null) {
					String attrName = entityAttr[i].getName(null);
					if(entAttrNameAmbiguity.containsKey(attrName)) {
						entAttrNameAmbiguity.put(attrName, Boolean.TRUE);
					} else {
						entAttrNameAmbiguity.put(attrName, Boolean.FALSE);
					}
				}
			}

			for(int i = entityAttr.length - 1; i >= 0 ; i--) {
				if(entityAttrFields[i] != null) {
					boolean ambiguousName = 
						((Boolean)entAttrNameAmbiguity.get(entityAttr[i].getName(null))).booleanValue();
					parseAttribute(instance, entityAttr[i], ambiguousName);
				}
			}


			contentHandler.endElement(null, theElement, theElement);

		}
	}

	private void parseAttribute(EEntity instance, EExplicit_attribute attr, boolean ambiguousName)
			throws SdaiException, SAXException {
		EDefined_type[] attrSelect = new EDefined_type[20];
		int attrType = instance.testAttribute(attr, attrSelect);
		if(attrType != 0) {
			attributes.clear();
			String attributeName = formatAttributeName(attr.getName(null));
			if(ambiguousName) {
				attributeName =
					formatEntityName(attr.getParent_entity(null).getName(null)) + "." + attributeName;
			}
			EEntity domain = attr.getDomain(null);
			Object value = instance.get_object(attr);

			int selectIndex = 0;
			for(; attrSelect[selectIndex] != null; selectIndex++);
			if(selectIndex > 1) {
				StringBuffer path = new StringBuffer();
				selectIndex--;
				for(int i = 0; i < selectIndex; i++) {
					if(i > 0) {
						path.append(' ');
					}
					path.append(attrSelect[i].getName(null));
				}
				attributes.addAttribute(null, "path", "path", "IDPATH", path.toString());
			}

			if(value instanceof EEntity){
				EEntity entity_ref = (EEntity)value;
				attributes.addAttribute(null, "ref", "ref", "IDREF", getID(entity_ref));
				attributes.addAttribute(null, "xsi:nil", "xsi:nil", "CDATA", "true");
				contentHandler.startElement(null, attributeName, attributeName, attributes);
				contentHandler.endElement(null, attributeName, attributeName);
			} else {
            	contentHandler.startElement(null, attributeName, attributeName, attributes);
				parseAttributeValue(value, domain, attrSelect);
				contentHandler.endElement(null, attributeName, attributeName);
			}
		}
	}

	private void parseAttributeValue(Object value, EEntity domain, EDefined_type[] select)
		throws SdaiException, SAXException {
		if(domain instanceof ESimple_type) {
			if(domain instanceof ELogical_type || domain instanceof EBoolean_type) {
				String stringValue = ELogical.toString(((Integer)value).intValue()).toLowerCase();
				contentHandler.characters(stringValue.toCharArray(), 0, stringValue.length());
			} else {
				String stringValue = value.toString();
				contentHandler.characters(stringValue.toCharArray(), 0, stringValue.length());
			}
		} else if(domain instanceof EAggregation_type) {
			Aggregate aggregate = (Aggregate)value;
			EData_type elementType = ((EAggregation_type)domain).getElement_type(null);

			//determine if the aggregate is an array and if needed upper bound
			boolean array_type = false;
			int pos = 0;
//			EAggregation_type aggregation_type = aggregate.getAggregationType();
            if(domain instanceof EArray_type){
				array_type = true;
				pos = ((EArray_type)domain).getUpper_index(null).getBound_value(null);
			}

			for (SdaiIterator it = aggregate.createIterator(); it.next();) {
				EDefined_type[] memberSelect = new EDefined_type[20];
				if(aggregate.testCurrentMember(it, memberSelect) != 0){
					Object aggregateMember = aggregate.getCurrentMemberObject(it);

					attributes.clear();
					//determine proper member type
					EData_type memberType = null;
					StringBuffer path = new StringBuffer();
					if(memberSelect[0] != null) { //just to be sure on branching
						StringBuffer prevMemberName = null;
						for(int selectIndex = 0; memberSelect[selectIndex] != null; selectIndex++){
							if(prevMemberName == null) {
								prevMemberName = new StringBuffer(memberSelect[selectIndex].getName(null));
							} else {
								path.append(prevMemberName);
								prevMemberName.setLength(0);
								prevMemberName.append(' ').append(memberSelect[selectIndex].getName(null));
							}
							memberType = memberSelect[selectIndex];
						}
					} else {
						memberType = elementType;
					}
					//this is set outside iteration
					if(array_type)
						attributes.addAttribute(null, "pos", "pos", "IDPOS", String.valueOf(pos));

					//this is dertermined in every cycle step
					if(path.length() > 0)
						attributes.addAttribute(null, "path", "path", "IDPATH", path.toString());

					if(aggregateMember instanceof EEntity){
						EEntity entity_ref = (EEntity)aggregateMember;
						attributes.addAttribute(null, "ref", "ref", "IDREF", getID(entity_ref));
						attributes.addAttribute(null, "xsi:nil", "xsi:nil", "CDATA", "true");
						String elementTypeString = formatEntityName(entity_ref.getInstanceType().getName(null));
						contentHandler.startElement(null, elementTypeString, elementTypeString, attributes);
						contentHandler.endElement(null, elementTypeString, elementTypeString);

					} else if (aggregateMember instanceof Aggregate){
						EEntity aggregateElementType =
								((Aggregate)aggregateMember).getAggregationType().getElement_type(null); // should work

						String stringElementType = null;
						if(aggregateElementType instanceof EAggregation_type)
							stringElementType = AGGREGATE_LIST;
						else if(aggregateElementType instanceof EString_type)
							stringElementType = STRING_LIST;
						if(aggregateElementType instanceof EInteger_type)
							stringElementType = INTEGER_LIST;
						else if(aggregateElementType instanceof ENumber_type)
							stringElementType = NUMBER_LIST;
						else if(aggregateElementType instanceof EReal_type)
							stringElementType = REAL_LIST;
						else if(aggregateElementType instanceof EBoolean_type)
							stringElementType = BOOLEAN_LIST;
						else if(aggregateElementType instanceof ELogical_type)
							stringElementType = LOGICAL_LIST;
						else
							stringElementType = LIST_PREFIX + formatEntityName(memberType.getName(null)); //defined type

                        //no member count not supported at this time
						contentHandler.startElement(null, stringElementType, stringElementType, attributes);
						parseAttributeValue(aggregateMember, memberType, memberSelect);
						contentHandler.endElement(null, stringElementType, stringElementType);

					} else if(memberType instanceof EDefined_type){
						//see page 129 in pdf
						EData_type tmpMemberType = memberType;
						//there no sens to test end type if there is a complex type
						if(memberSelect[0] == null)
							while(tmpMemberType instanceof EDefined_type)
								tmpMemberType = (EData_type) ((EDefined_type)tmpMemberType).getDomain(null);

						if((memberSelect[0] == null) && (tmpMemberType instanceof ESimple_type)){
							String stringMemeberType = getSimpleTypeSerializedName(tmpMemberType);
							contentHandler.startElement(null, stringMemeberType, stringMemeberType, attributes);
							parseAttributeValue(aggregateMember, tmpMemberType, memberSelect);
							contentHandler.endElement(null, stringMemeberType, stringMemeberType);
						} else {
							String stringMemeberType = formatEntityName(memberType.getName(null));
							contentHandler.startElement(null, stringMemeberType, stringMemeberType, attributes);
							parseAttributeValue(aggregateMember, ((EDefined_type)memberType).getDomain(null), memberSelect);
							contentHandler.endElement(null, stringMemeberType, stringMemeberType);
						}
					}else{
						//I assume there is only simple type
						String stringMemeberType = getSimpleTypeSerializedName(memberType);

//                        System.out.println("got type:" + stringMemeberType + " className:" + memberType.getClass().getName());
						contentHandler.startElement(null, stringMemeberType, stringMemeberType, attributes);
						parseAttributeValue(aggregateMember, memberType, memberSelect);
						contentHandler.endElement(null, stringMemeberType, stringMemeberType);
					}
				}
				pos++;
			}

		} else if(domain instanceof ESelect_type) {
			EDefined_type selectType = null;
			for(int selectIdx = 0; select[selectIdx] != null; selectIdx++)
				selectType = select[selectIdx];

			String definedTypeString = formatEntityName(selectType.getName(null));
			attributes.clear();
			contentHandler.startElement(null, definedTypeString, definedTypeString, attributes);
			parseAttributeValue(value, selectType.getDomain(null), select);
			contentHandler.endElement(null, definedTypeString, definedTypeString);

		} else if(domain instanceof EEnumeration_type) {
			EEnumeration_type enumType = (EEnumeration_type)domain;
			int enumValue = ((Integer)value).intValue();
			String enumElement = enumType.getElements(null).getByIndex(enumValue);
			contentHandler.characters(enumElement.toCharArray(), 0, enumElement.length());
		} else if(domain instanceof EDefined_type) {
			EDefined_type definedType = (EDefined_type)domain;
			parseAttributeValue(value, definedType.getDomain(null), select);
		}
	}

	static private String getSimpleTypeSerializedName(EData_type type)
	throws SdaiException
	{
		if(type instanceof EString_type)
			return STRING;
		else if(type instanceof EInteger_type)
			return INTEGER;
		else if(type instanceof ENumber_type)
			return NUMBER;
		else if(type instanceof EReal_type)
			return REAL;
		else if(type instanceof EBoolean_type)
			return BOOLEAN;
		else if(type instanceof ELogical_type){
			return LOGICAL;
		}else if(type instanceof EBinary_type)
			return BINARY;
		else
			return type.getName(null);
	}

    static private String getID(EEntity instance)
	{
		return "e" + Long.toString(getInstanceIdentifier(instance));
	}

	static private String formatAttributeName(String name)
	{
		StringBuffer buf = new StringBuffer(name);
		if ((buf.length() >2) && (buf.charAt(0) == 'x') && (buf.charAt(1) == 'm') && (buf.charAt(2) == 'l'))
			buf = buf.replace(0, 3, "x-m-l");
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		return buf.toString();
	}

	static private String formatEntityName(String name)
	{
		StringBuffer buf = new StringBuffer(name);
		if ((buf.length() >2) && (buf.charAt(0) == 'x') && (buf.charAt(1) == 'm') && (buf.charAt(2) == 'l'))
			buf = buf.replace(0, 3, "x-m-l");
		buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		for (int i = 1; i < buf.length(); i++)
			if (buf.charAt(i) == '+'){
				buf.setCharAt(i, '-');
				buf.setCharAt(i+1, Character.toUpperCase(buf.charAt(i+1)));
			}
		return buf.toString();
	}
}
