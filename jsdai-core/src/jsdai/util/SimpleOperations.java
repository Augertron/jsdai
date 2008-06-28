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

package jsdai.util;

import java.util.*;

import jsdai.lang.*;
import jsdai.dictionary.*;

/**
* This class contains some usefull mothods to supprot some simple operations on attributes of entity.
* It cann't be instatiated.
*/
public class SimpleOperations {

/**Method returns final type of attribute.*/
	public static EEntity getAttributeDomain(EAttribute attribute) throws SdaiException {
		EEntity result = null;
		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attribute;
			result = getExplicit_attributeDomain(ea);
		} else if (attribute instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attribute;
			result = ia.getDomain(null);
		}
		else if (attribute instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attribute;
			result = getBase_typeDomain(da.getDomain(null));
		}
		return result;
	}

/**Method returns final type of explicit_attribute.*/
	public static EEntity getExplicit_attributeDomain(EExplicit_attribute e_attribute) throws SdaiException {
		return getBase_typeDomain(e_attribute.getDomain(null));
	}

/**Method returns final type of base_type.*/
	public static EEntity getBase_typeDomain(EEntity domain) throws SdaiException {
		EEntity result = null;
		if (domain instanceof EAggregation_type) {
			result = getAggregation_typeDomain((EAggregation_type)domain);
		}
		else if (domain instanceof ESimple_type) {
			result = getSimple_typeDomain((ESimple_type)domain);
		}
		else if (domain instanceof ENamed_type) {
			result = getNamed_typeDomain((ENamed_type)domain);
		}
		return result;
	}

/**Method returns final type of aggregation_type.*/
	public static EEntity getAggregation_typeDomain(EAggregation_type domain) throws SdaiException {
		EEntity result = null;
		if (domain instanceof ESet_type) {
			result = (ESet_type)domain;
		}
		else if (domain instanceof EBag_type) {
			result = (EBag_type)domain;
		}
		else if (domain instanceof EList_type) {
			result = (EList_type)domain;
		}
		else if (domain instanceof EArray_type) {
			result = (EArray_type)domain;
		}
		return result;
	}

/**Method returns final type of simple_type.*/
	public static EEntity getSimple_typeDomain(ESimple_type domain) throws SdaiException {
		EEntity result = null;
		if (domain instanceof ENumber_type) {
			result = (ENumber_type)domain;
		}
		else if (domain instanceof EInteger_type) {
			result = (EInteger_type)domain;
		}
		else if (domain instanceof EReal_type) {
			result = (EReal_type)domain;
		}
		else if (domain instanceof EBoolean_type) {
			result = (EBoolean_type)domain;
		}
		else if (domain instanceof ELogical_type) {
			result = (ELogical_type)domain;
		}
		else if (domain instanceof EBinary_type) {
			result = (EBinary_type)domain;
		}
		else if (domain instanceof EString_type) {
			result = (EString_type)domain;
		}
		return result;
	}

/**Method returns final type of named_type.*/
	public static EEntity getNamed_typeDomain(ENamed_type domain) throws SdaiException {
		EEntity result = null;
		if (domain instanceof EDefined_type) {
			result = getDefined_typeDomain((EDefined_type)domain);
		}
		else if (domain instanceof EEntity_definition) {
			result = (EEntity_definition)domain;
		}
		return result;
	}

/**Method returns final type of defined_type.*/
	public static EEntity getDefined_typeDomain(EDefined_type domain) throws SdaiException {
		return getUnderlying_typeDomain(domain.getDomain(null));
	}

/**Method returns final type of underlying_type.*/
	public static EEntity getUnderlying_typeDomain(EEntity domain) throws SdaiException {
		EEntity result = null;
		if (domain instanceof EAggregation_type) {
			result = getAggregation_typeDomain((EAggregation_type)domain);
		}
		else if (domain instanceof ESimple_type) {
			result = getSimple_typeDomain((ESimple_type)domain);
		}
		else if (domain instanceof EDefined_type) {
			result = getDefined_typeDomain((EDefined_type)domain);
		}
		else if (domain instanceof ESelect_type) {
			result = (ESelect_type)domain;
		}
		else if (domain instanceof EEnumeration_type) {
			result = (EEnumeration_type)domain;
		}
		return result;
	}

/**Method returns value of attribute in string representation.*/
	public static String getAttributeString(EEntity entity, EAttribute attribute) throws SdaiException {
		String result = "";
		EDefined_type dt[] = new EDefined_type[10];
		EEntity domain = getAttributeDomain(attribute);
		if (entity.testAttribute(attribute, dt) == 0) {
			result = "UNSET";
		}
		else {
			if (
				(domain instanceof ESet_type) ||
				(domain instanceof EBag_type) ||
				(domain instanceof EList_type) ||
				(domain instanceof EArray_type)
			) {

//				result = "("+getAggregateString((Aggregate)getAttributeObject(entity, attribute), false, ", ")+")";
				result += ((Aggregate)getAttributeObject(entity, attribute)).toString();
			}
			else if (domain instanceof ESelect_type) {
				result = getSelect_typeValueString(getAttributeObject(entity, attribute));
			}
			else if (
				(domain instanceof EBinary_type) ||
				(domain instanceof EString_type) ||
				(domain instanceof EDefined_type) ||
				(domain instanceof EEntity_definition)
			) {
				result = getAttributeObject(entity, attribute).toString();
			}
			else if (
				(domain instanceof EInteger_type) ||
				(domain instanceof EBoolean_type) ||
				(domain instanceof ELogical_type) ||
				(domain instanceof EEnumeration_type)
			) {
				result = String.valueOf(getAttributeInt(entity, attribute));
			}
			else if (
				(domain instanceof ENumber_type) ||
				(domain instanceof EReal_type)
			) {
				result = String.valueOf(getAttributeDouble(entity, attribute));
			}
		}
		return result;
	}

/**Method returns value of aggregate in string representation.*/
	public static String getAggregateString(Aggregate aggregate, boolean indexed, String separator) throws SdaiException {
		EAggregation_type domain_aggregate = aggregate.getAggregationType();
		String result = "";
		SdaiIterator iterator = aggregate.createIterator();
		EEntity domain = null;
		if ((domain_aggregate != null) && (domain_aggregate.testElement_type(null))) {
			domain = getBase_typeDomain(domain_aggregate.getElement_type(null));
		}
		if (aggregate.getMemberCount() == 0) {
			result = "EMPTY";
			return result;
		}
		int i = 0;
		boolean first = true;
		while (iterator.next()) {
			if (first) {
				first = false;
			} else {
				result += separator;
			}
			if (indexed) {
				result += String.valueOf(i)+": ";
			}
			if (aggregate.testCurrentMember(iterator) == 0) {
				result += "UNSET";
			} else {
				if (
					(domain instanceof ESet_type) ||
					(domain instanceof EBag_type) ||
					(domain instanceof EList_type) ||
					(domain instanceof EArray_type)
				) {
					result += "("+getAggregateString((Aggregate)aggregate.getCurrentMemberObject(iterator), false, ", ")+")";
				}
				else if (domain instanceof ESelect_type) {
					result += getSelect_typeValueString(aggregate.getCurrentMemberObject(iterator));
				}
				else if (domain instanceof EEntity_definition) {
					result += ((EEntity)aggregate.getCurrentMemberObject(iterator)).getPersistentLabel();
				}
				else if (
					(domain instanceof EBinary_type) ||
					(domain instanceof EString_type) ||
					(domain instanceof EDefined_type)
				) {
					result += aggregate.getCurrentMemberObject(iterator).toString();
				}
				else if (
					(domain instanceof EInteger_type) ||
					(domain instanceof EBoolean_type) ||
					(domain instanceof ELogical_type) ||
					(domain instanceof EEnumeration_type)
				) {
					result += String.valueOf(aggregate.getCurrentMemberInt(iterator));
				}
				else if (
					(domain instanceof ENumber_type) ||
					(domain instanceof EReal_type)
				) {
					result += String.valueOf(aggregate.getCurrentMemberDouble(iterator));
				} else {
					result += aggregate.getCurrentMemberObject(iterator).toString();
				}
			}
			i++;
		}
		return result;
	}

/**Method returns aggregate representation as string.*/
	public static String getAEntityString(AEntity aggregate) throws SdaiException {
		String result = "";
		SdaiIterator iterator = aggregate.createIterator();
		while (iterator.next()) {
			result += aggregate.getCurrentMemberEntity(iterator).toString();
		}
		return result;
	}

	private static String getSelect_typeValueString(Object value) throws SdaiException {
		String result = "";
		if (value instanceof Aggregate) {
			result = "("+getAggregateString((Aggregate)value, false, ", ")+")";
		} else {
			result = value.toString();
		}
		return result;
	}

/**Method returns value of attribute in object representation.*/
	public static Object getAttributeObject(EEntity entity, EAttribute attribute) throws SdaiException {
		return entity.get_object(attribute);
	}

/**Method returns value of attribute in int representation.*/
	public static int getAttributeInt(EEntity entity, EAttribute attribute) throws SdaiException {
		return entity.get_int(attribute);
	}

/**Method returns value of attribute in double representation.*/
	public static double getAttributeDouble(EEntity entity, EAttribute attribute) throws SdaiException {
		return entity.get_double(attribute);
	}

/**Method sets value of attribute to string representation.*/
	public static void setAttributeString(EEntity entity, EAttribute attribute, String value, EDefined_type[] defs) throws SdaiException {
		if (value.equalsIgnoreCase("unset") || value.equalsIgnoreCase("")) {
			unsetAttribute(entity, attribute);
		} else {
			EEntity domain = getAttributeDomain(attribute);
			setAttributeStringWithDomain(entity, attribute, value, defs, domain);
		}
	}

	private static void setAttributeStringWithDomain(EEntity entity, EAttribute attribute, String value, EDefined_type[] defs, EEntity domain) throws SdaiException {
		if (domain instanceof EString_type) {
			setAttributeObject(entity, attribute, value, defs);
		}
		else if (
			(domain instanceof ESet_type) ||
			(domain instanceof EBag_type) ||
			(domain instanceof EList_type) ||
			(domain instanceof EArray_type)
		) {
		}
		else if (domain instanceof ESelect_type) {
			if ((defs == null) || (defs.length < 2) || (getDefined_typeDomain(defs[defs.length-2]) instanceof ESelect_type)) {
				setAttributeObject(entity, attribute, entity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier(findIdentifier(value)), defs);
			} else {
				setAttributeStringWithDomain(entity, attribute, value, defs, getDefined_typeDomain(defs[defs.length-2]));
			}
		}
		else if (
			(domain instanceof EDefined_type)
		){
			setAttributeStringWithDomain(entity, attribute, value, defs, getDefined_typeDomain((EDefined_type)domain));
		}
		else if (
			(domain instanceof EEntity_definition)
		) {
			setAttributeObject(entity, attribute, entity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier(findIdentifier(value)), defs);
		}
		else if (
			(domain instanceof EBinary_type)
		) {
			setAttributeObject(entity, attribute, new Binary(value), defs);
		}
		else if (
			(domain instanceof EInteger_type) ||
			(domain instanceof EBoolean_type) ||
			(domain instanceof ELogical_type)
		) {
			try {
				setAttributeInt(entity, attribute, Integer.parseInt(value), defs);
			} catch (NumberFormatException ex) { throw(new SdaiException(SdaiException.VT_NVLD, attribute.getName(null))); }
		}
		else if
			(domain instanceof EEnumeration_type)
		{
			if (isNumber(value)) {
				setAttributeInt(entity, attribute, Integer.parseInt(value), defs);
			} else {
				setAttributeInt(entity, attribute, LangUtils.convertEnumerationStringToInt(value, (EEnumeration_type)domain), defs);
			}
		}
		else if (
			(domain instanceof ENumber_type) ||
			(domain instanceof EReal_type)
		) {
			try {
				setAttributeDouble(entity, attribute, Double.parseDouble(value), defs);
			} catch (NumberFormatException ex) { throw(new SdaiException(SdaiException.VT_NVLD, attribute.getName(null))); }
		}
	}

/**Method sets value of attribute to object representation.*/
	public static void setAttributeObject(EEntity entity, EAttribute attribute, Object value, EDefined_type[] defs) throws SdaiException {
		entity.set(attribute, value, defs);
	}

/**Method sets value of attribute to int representation.*/
	public static void setAttributeInt(EEntity entity, EAttribute attribute, int value, EDefined_type[] defs) throws SdaiException {
		entity.set(attribute, value, defs);
	}

/**Method sets value of attribute to double representation.*/
	public static void setAttributeDouble(EEntity entity, EAttribute attribute, double value, EDefined_type[] defs) throws SdaiException {
		entity.set(attribute, value, defs);
	}

/**Method unsets value of attribute.*/
	public static void unsetAttribute(EEntity entity, EAttribute attribute) throws SdaiException {
		entity.unsetAttributeValue(attribute);
	}

/**Method produces a string describing an elemant of the aggregate.*/
	public static String getElementString(Aggregate aggregate, int index) throws SdaiException {
		String result = "";
		EEntity domain = getBase_typeDomain(aggregate.getAggregationType().getElement_type(null));
		if (
			(domain instanceof ESet_type) ||
			(domain instanceof EBag_type) ||
			(domain instanceof EList_type) ||
			(domain instanceof EArray_type)
		) {
			result = getAggregateString((Aggregate)aggregate.getByIndexObject(index), false, ", ");
		}
		else if (
			(domain instanceof EBinary_type) ||
			(domain instanceof EString_type) ||
			(domain instanceof ESelect_type) ||
			(domain instanceof EDefined_type) ||
			(domain instanceof EEntity_definition)
		) {
			result = aggregate.getByIndexObject(index).toString();
		}
		else if (
			(domain instanceof EInteger_type) ||
			(domain instanceof EBoolean_type) ||
			(domain instanceof ELogical_type) ||
			(domain instanceof EEnumeration_type)
		) {
			result = String.valueOf(aggregate.getByIndexInt(index));
		}
		else if (
			(domain instanceof ENumber_type) ||
			(domain instanceof EReal_type)
		) {
			result = String.valueOf(aggregate.getByIndexDouble(index));
		}
		return result;
	}

/**Method gets value of aggregate element as Object.*/
	public static Object getElementObject(Aggregate aggregate, int index) throws SdaiException {
		Object result = "";
		if (aggregate.testByIndex(index) == 0) {
			result = "UNSET";
		} else {
			EEntity domain = getBase_typeDomain(aggregate.getAggregationType().getElement_type(null));
			if (
				(domain instanceof ESet_type) ||
				(domain instanceof EBag_type) ||
				(domain instanceof EList_type) ||
				(domain instanceof EArray_type)
			) {
//				result = aggregate.getByIndexObject(index);
				result = getAggregateString((Aggregate)aggregate.getByIndexObject(index), false, ", ");
			}
			else if (
				(domain instanceof EBinary_type) ||
				(domain instanceof EString_type) ||
				(domain instanceof ESelect_type) ||
				(domain instanceof EDefined_type) ||
				(domain instanceof EEntity_definition)
			) {
				result = aggregate.getByIndexObject(index);
			}
			else if (
				(domain instanceof EInteger_type) ||
				(domain instanceof EBoolean_type) ||
				(domain instanceof ELogical_type) ||
				(domain instanceof EEnumeration_type)
			) {
				result = String.valueOf(aggregate.getByIndexInt(index));
			}
			else if (
				(domain instanceof ENumber_type) ||
				(domain instanceof EReal_type)
			) {
				result = String.valueOf(aggregate.getByIndexDouble(index));
			}
			else {
				result = aggregate.getByIndexObject(index);
			}
		}
		return result;
	}

/**Method removes value from aggregate.*/
	public static void removeElement(Aggregate aggregate, int index) throws SdaiException {
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			Object value = aggregate.getByIndexObject(index);
			aggregate.removeUnordered(value, null);
		}
		else if (domain instanceof EList_type) {
			aggregate.removeByIndex(index);
		}
		else if (domain instanceof EArray_type) {
			aggregate.unsetValueByIndex(index);
		}
	}

/**Method sets value for aggregate to String representation.*/
	public static void addElementString(EEntity entity, Aggregate aggregate, int index, String value, EDefined_type[] defs) throws SdaiException {
		EEntity domain = getBase_typeDomain(aggregate.getAggregationType().getElement_type(null));
		addElementStringWithDomain(entity, aggregate, index, value, defs, domain);
	}

	private static void addElementStringWithDomain(EEntity entity, Aggregate aggregate, int index, String value, EDefined_type[] defs, EEntity domain) throws SdaiException {
		if (domain instanceof EString_type) {
			addElementObject(aggregate, index, value, defs);
		}
		else if (domain instanceof EBinary_type) {
			addElementObject(aggregate, index, new Binary(value), defs);
		}
		else if (
			(domain instanceof ESet_type) ||
			(domain instanceof EBag_type) ||
			(domain instanceof EList_type) ||
			(domain instanceof EArray_type)
		) {
			createElementAggregate(aggregate, index, defs);
		}
		else if (domain instanceof ESelect_type) {
			if ((defs == null) || (defs.length < 2) || (getDefined_typeDomain(defs[defs.length-2]) instanceof ESelect_type)) {
				addElementObject(aggregate, index, entity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier(findIdentifier(value)), defs);
			} else {
				addElementStringWithDomain(entity, aggregate, index, value, defs, getDefined_typeDomain(defs[defs.length-2]));
			}
		}
		else if (
			(domain instanceof EDefined_type) ||
			(domain instanceof EEntity_definition)
		) {
			addElementObject(aggregate, index, entity.findEntityInstanceSdaiModel().getRepository().getSessionIdentifier(findIdentifier(value)), defs);
		}
		else if (
			(domain instanceof EInteger_type) ||
			(domain instanceof ELogical_type)
		) {
			try {
				addElementInteger(aggregate, index, Integer.parseInt(value), defs);
			} catch (NumberFormatException ex) { throw(new SdaiException(SdaiException.AT_NVLD)); }
		}
		else if (domain instanceof EBoolean_type) {
			addElementBoolean(aggregate, index, Boolean.getBoolean(value), defs);
		}
		else if
			(domain instanceof EEnumeration_type)
		{
			if (isNumber(value)) {
				addElementInteger(aggregate, index, Integer.parseInt(value), defs);
			} else {
				addElementInteger(aggregate, index, LangUtils.convertEnumerationStringToInt(value, (EEnumeration_type)domain), defs);
			}
		}
		else if (
			(domain instanceof ENumber_type) ||
			(domain instanceof EReal_type)
		) {
			try {
				addElementDouble(aggregate, index, Double.parseDouble(value), defs);
			} catch (NumberFormatException ex) { throw(new SdaiException(SdaiException.AT_NVLD)); }
		}
	}

/**Method adds value to aggregate as Object.*/
	public static void addElementObject(Aggregate aggregate, int index, Object value, EDefined_type[] defs) throws SdaiException {
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			aggregate.addUnordered(value, defs);
		}
		else if (domain instanceof EList_type) {
			aggregate.addByIndex(index, value, defs);
		}
		else if (domain instanceof EArray_type) {
			aggregate.setByIndex(index, value, defs);
		}
	}

/**Method adds value to aggregate as Integer.*/
	public static void addElementInteger(Aggregate aggregate, int index, int value, EDefined_type[] defs) throws SdaiException {
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			aggregate.addUnordered(value, defs);
		}
		else if (domain instanceof EList_type) {
			aggregate.addByIndex(index, value, defs);
		}
		else if (domain instanceof EArray_type) {
			aggregate.setByIndex(index, value, defs);
		}
	}

/**Method adds value to aggregate as boolean.*/
	public static void addElementBoolean(Aggregate aggregate, int index, boolean value, EDefined_type[] defs) throws SdaiException {
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			aggregate.addUnordered(value, defs);
		}
		else if (domain instanceof EList_type) {
			aggregate.addByIndex(index, value, defs);
		}
		else if (domain instanceof EArray_type) {
			aggregate.setByIndex(index, value, defs);
		}
	}

/**Method adds value to aggregate as Double.*/
	public static void addElementDouble(Aggregate aggregate, int index, double value, EDefined_type[] defs) throws SdaiException {
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			aggregate.addUnordered(value, defs);
		}
		else if (domain instanceof EList_type) {
			aggregate.addByIndex(index, value, defs);
		}
		else if (domain instanceof EArray_type) {
			aggregate.setByIndex(index, value, defs);
		}
	}

/**Mothod creates nested aggregate.*/
	public static Aggregate createElementAggregate(Aggregate aggregate, int index, EDefined_type[] defs) throws SdaiException {
		Aggregate result = null;
		EAggregation_type domain = aggregate.getAggregationType();
		if ((domain instanceof ESet_type) || (domain instanceof EBag_type)) {
			result = aggregate.createAggregateUnordered(defs);
		}
		else if (domain instanceof EList_type) {
/*			printDefs(defs);
			System.out.println(aggregate);
			System.out.println(String.valueOf(index));*/
			result = aggregate.addAggregateByIndex(index, defs);
		}
		else if (domain instanceof EArray_type) {
			result = aggregate.createAggregateByIndex(index, defs);
		}
		return result;
	}

/**Method returns attribute of entity by index, when counting attributes from highest supertype down.*/
	public static EAttribute getAttributeByIndex(EEntity_definition definition, int index) throws SdaiException {
		MyInteger integer = new MyInteger(1);
		EAttribute result = getAttributeByIndex(definition, index, integer);
/*		System.out.println(definition.getName(null));
		System.out.println(String.valueOf(index));
		System.out.println(String.valueOf(integer.integer));
		System.out.println(result.getName(null));*/
		return result;
	}

	private static EAttribute getAttributeByIndex(EEntity_definition definition, int index, MyInteger currIndex) throws SdaiException {
		EAttribute result = null;
		AEntity_definition supertypes = definition.getSupertypes(null);
		SdaiIterator supertypes_it = supertypes.createIterator();
		while (supertypes_it.next()) {
			EAttribute temp_result = getAttributeByIndex(supertypes.getCurrentMember(supertypes_it), index, currIndex);
			if (index == currIndex.integer) {
//				System.out.println(String.valueOf(currIndex.integer));
				result = temp_result;
			}
		}
//		System.out.println(String.valueOf(currIndex.integer));
		AExplicit_attribute attributes = definition.getExplicit_attributes(null);
		SdaiIterator attributes_it = attributes.createIterator();
		while (attributes_it.next()) {
			if (index == currIndex.integer) {
				result = attributes.getCurrentMember(attributes_it);
			}
			currIndex.integer++;
		}
		return result;
	}

/**Finds instance which type is schema_definition. Eqvivalent of model.getDefinedSchema()*/
	public static ESchema_definition findDefinedSchema(SdaiModel model) throws SdaiException {
		Aggregate instances = model.getInstances();
		SdaiIterator it = instances.createIterator();
		while (it.next()) {
			if (instances.getCurrentMemberObject(it) instanceof ESchema_definition) {
				return (ESchema_definition)instances.getCurrentMemberObject(it);
			}
		}
		return null;
	}

/**Finds entity_definition for entity_definition.name of underlying_schema.*/
	public static EEntity_definition findEntity_definition(String entity_name, ESchema_definition schema) throws SdaiException {
		AEntity_declaration declarations = schema.getEntity_declarations(null, null);
		SdaiIterator it_declarations = declarations.createIterator();
		while (it_declarations.next()) {
			EEntity_definition definition = (EEntity_definition)declarations.getCurrentMember(it_declarations).getDefinition(null);
			if (definition.getName(null).equalsIgnoreCase(entity_name)) {
				return definition;
			}
		}
		return null;
	}

/**Finds schema_definition for entity_definition in domain.*/
	public static ESchema_definition findSchema_definition(ENamed_type nt, ASchemaInstance schemas) throws SdaiException {
		ADeclaration declarations = new ADeclaration();
		CDeclaration.usedinDefinition(null, nt, schemas.getAssociatedModels(), declarations);
		SdaiIterator it_declarations = declarations.createIterator();
		while (it_declarations.next()) {
			if (declarations.getCurrentMember(it_declarations) instanceof ELocal_declaration) {
				return declarations.getCurrentMember(it_declarations).getParent_schema(null);
			}
		}
		return null;
	}

	public static void printDefs(EDefined_type[] defs) throws SdaiException {
		if (defs == null) {
			System.out.println("nulllllll");
		} else {
			for (int i = 0; i < defs.length; i++) {
				if (defs[i] == null) {
					System.out.println("null");
				} else {
					System.out.println(defs[i].getName(null));
				}
			}
		}
	}

/**Returns identifier of Entity.toString() representation.*/
	public static String findIdentifier(String s) {
		if (s.indexOf('#') != 0) s = '#'+s;
		int k = 0;
		for (int i = 0; i < s.length(); i++) {
			if (((s.charAt(i) >= '0') && (s.charAt(i) <= '9')) || (s.charAt(i) == '#')) {
				k++;
			} else {
				return s.substring(0, k);
			}

		}
		return s;
	}

	public static String getAttributeName(EAttribute attribute) throws SdaiException {
//		return ((getRedeclaring(attribute)==null)?attribute.getName(null)
//			:"SELF\\"+getRedeclaring(attribute).getParent_entity(null).getName(null)+"."+getRedeclaring(attribute).getName(null));
		return attribute.getName(null);
	}

/**Returns attribute type in string as express.*/
	public static String getAttributeDomainString(EAttribute attribute) throws SdaiException {
//		EEntity domain = getAttributeDomain(attribute);
		String rt = "";/*"SELF\\"+((getRedeclaring(attribute)!=null)?getRedeclaring(attribute).getParent_entity(null).getName(null)
			+"."+getRedeclaring(attribute).getName(null)+" ":"");*/

		EEntity domain = null;
		String attr = "";
		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attribute;
			domain = ea.getDomain(null);
//			attr += ((ea.getOptional_flag(null))?"OPT ":"");
			attr += ((ea.testRedeclaring(null))?rt:"");
		   attr += getDomainString(domain);
		} else if (attribute instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attribute;
			domain = ia.getDomain(null);
//			attr += "INV ";
			attr += ((ia.testRedeclaring(null))?rt:"");
			if (ia.testMin_cardinality(null)) {
				attr += ((ia.getDuplicates(null))?"BAG[":"SET[");
				attr += getAggregateBoundString((ia.testMin_cardinality(null))?ia.getMin_cardinality(null):null)+":";
				attr += getAggregateBoundString((ia.testMax_cardinality(null))?ia.getMax_cardinality(null):null)+"] OF ";
		   }
		   attr += getDomainString(domain)+" FOR "+ia.getInverted_attr(null).getName(null);

		} else if (attribute instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attribute;
			domain = da.getDomain(null);
//			attr += "DER ";
			attr += ((da.testRedeclaring(null))?rt:"");
		   attr += getDomainString(domain);
		}
		return attr;
	}

	public static String getAttributePrefix(EAttribute attribute) throws SdaiException {
		String attr = "";
		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attribute;
			attr += ((ea.getOptional_flag(null))?"(OPT)":"");
			attr += ((ea.testRedeclaring(null))?"(RT)":"");
		} else if (attribute instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attribute;
			attr += "(INV)";
			attr += ((ia.testRedeclaring(null))?"(RT)":"");
		} else if (attribute instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attribute;
			attr += "(DER)";
			attr += ((da.testRedeclaring(null))?"(RT)":"");
		}
		return attr;
	}

/**Returns redeclaring attribute.*/
	public static EAttribute getRedeclaring(EAttribute attr) throws SdaiException {
		if (attr instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attr;
			if (da.testRedeclaring(null)) {
				EAttribute a = (EAttribute)da.getRedeclaring(null);
				return (testRedeclaring(a))?getRedeclaring(a):a;
			}
		} else if (attr instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attr;
			if (ea.testRedeclaring(null)) {
				EAttribute a = ea.getRedeclaring(null);
				return (testRedeclaring(a))?getRedeclaring(a):a;
			}
		} else if (attr instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attr;
			if (ia.testRedeclaring(null)) {
				EAttribute a = ia.getRedeclaring(null);
				return (testRedeclaring(a))?getRedeclaring(a):a;
			}
		}
		return null;
	}

/**Tests is attribute redeclared.*/
	public static boolean testRedeclaring(EAttribute attr) throws SdaiException {
		if (attr instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attr;
			return da.testRedeclaring(null);
		} else if (attr instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attr;
			return ea.testRedeclaring(null);
		} else if (attr instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attr;
			return ia.testRedeclaring(null);
		}
		return false;
	}

/**Returns type domain in string as express.*/
	public static String getDomainString(EEntity domain) throws SdaiException {
		String result = "";
		if (domain instanceof EAggregation_type) {
			EBound lower = null;
			EBound upper = null;
			boolean unique = false;
			boolean optional = false;
			if (domain instanceof EVariable_size_aggregation_type) {
				if (domain instanceof ESet_type) {
					result += "SET [";
				} else if (domain instanceof EBag_type) {
					result += "BAG [";
				} else if (domain instanceof EList_type) {
					EList_type lt = (EList_type)domain;
					unique = lt.getUnique_flag(null);
					result += "LIST [";
				}
				EVariable_size_aggregation_type vt = (EVariable_size_aggregation_type)domain;
				lower = vt.getLower_bound(null);
				upper = ((vt.testUpper_bound(null))?vt.getUpper_bound(null):null);
			} else if (domain instanceof EArray_type) {
				EArray_type rt = (EArray_type)domain;
				unique = rt.getUnique_flag(null);
				optional = rt.getOptional_flag(null);
				lower = rt.getLower_index(null);
				upper = ((rt.testUpper_index(null))?rt.getUpper_index(null):null);
				result += "ARRAY [";
			}
			result += getAggregateBoundString(lower)+":"+getAggregateBoundString(upper)+"]";
			result += ((optional)?" OPTIONAL ":"")+((unique)?" UNIQUE ":"");
			EAggregation_type at = (EAggregation_type)domain;
			result += " OF "+getDomainString(at.getElement_type(null));
		} else if (domain instanceof EDefined_type) {
			EDefined_type dt = (EDefined_type)domain;
//			result += getDomainString(getDefined_typeDomain(dt));
			result += dt.getName(null);
		} else if (domain instanceof EEntity_definition) {
			EEntity_definition ed = (EEntity_definition)domain;
			result += ed.getName(null);
		} else if (domain instanceof ENumber_type) {
			result += "NUMBER";
		} else if (domain instanceof EInteger_type) {
			result += "INTEGER";
		} else if (domain instanceof EReal_type) {
			result += "REAL";
		} else if (domain instanceof EBoolean_type) {
			result += "BOOLEAN";
		} else if (domain instanceof ELogical_type) {
			result += "LOGICAL";
		} else if (domain instanceof EBinary_type) {
			result += "BINARY";
		} else if (domain instanceof EString_type) {
			result += "STRING";
		} else if (domain instanceof ESelect_type) {
			ESelect_type st = (ESelect_type)domain;
			result += "SELECT (";
			ANamed_type ant = st.getSelections(null);
			SdaiIterator it = ant.createIterator();
			boolean first = true;
			while (it.next()) {
				if (first) {
					first = false;
				} else {
					result += ", ";
				}
				result += getDomainString(ant.getCurrentMember(it));
			}
			result += ")";
		} else if (domain instanceof EEnumeration_type) {
			EEnumeration_type et = (EEnumeration_type)domain;
			result += "ENUMERATION";
/*			A_string ant = et.getElements(null);
			SdaiIterator it = ant.createIterator();
			boolean first = true;
			while (it.next()) {
				if (first) {
					first = false;
				} else {
					result += ", ";
				}
				result += ant.getCurrentMember(it);
			}
			result += " )";*/
		}
		return result;
	}

/**Returns string representation of aggregation type boud.*/
	private static String getAggregateBoundString(EBound bound) throws SdaiException {
		String result = "";
		if (bound == null) {
			result += "?";
		} else if (bound instanceof EInteger_bound) {
			result += String.valueOf(((EInteger_bound)bound).getBound_value(null));
		} else {
			result += "??";
		}
		return result;
	}

/**Collects available nodes for select_type.*/
	public static boolean getNodes(ESelect_type st, Vector nodes, Vector path) throws SdaiException {
		return getNodes(null, st, nodes, path);
	}

	public static boolean getNodes(SdaiContext sdaiContext, ESelect_type st, Vector nodes, Vector path) throws SdaiException {
		boolean result = false;
		ANamed_type ant = sdaiContext != null ? st.getSelections(null, sdaiContext) : st.getSelections(null);
		SdaiIterator it_ant = ant.createIterator();
		boolean case_entity = false;
		int entity_position = nodes.size();
		while(it_ant.next()) {
			ENamed_type nt = ant.getCurrentMember(it_ant);
			if (nt instanceof EDefined_type) {
				EDefined_type dt = (EDefined_type)nt;
				EEntity domain = dt.getDomain(null);
				Vector element = new Vector(path);
				if (!(domain instanceof ESelect_type)) {
					element.addElement(dt);
				}
				while (domain instanceof EDefined_type) {
					domain = ((EDefined_type)domain).getDomain(null);
				};
				if (domain instanceof ESelect_type) {
					result = getNodes((ESelect_type)domain, nodes, element) || result;
				} else {
					result = true;
					element.addElement((EDefined_type)null);
					nodes.addElement(element);
				}
			} else {
				case_entity = true;
			}
		}
		if (case_entity) {
			Vector element = new Vector(path);
			element.addElement((EDefined_type)null);
			nodes.insertElementAt(element, entity_position);
		}
		return result;
	}

/**If type or type domain is select_type than returns it, else returns null.*/
	public static ESelect_type isSelectInside(EEntity type) throws SdaiException {
		if (type instanceof ESelect_type) {
			return (ESelect_type)type;
		} else if (type instanceof EDefined_type) {
			EDefined_type dt = (EDefined_type)type;
			return isSelectInside(dt.getDomain(null));
		}
		return null;
	}

/**If type or type domain is mixed select_type than returns it, else returns null.*/
	public static boolean isMixedSelectInside(EEntity type) throws SdaiException {
		if (type instanceof ESelect_type) {
			ESelect_type st = (ESelect_type)type;
			return isMixedSelectInside(null, st);
		}
		return false;
	}

	public static boolean isMixedSelectInside(SdaiContext sdaiContext, EEntity type) throws SdaiException {
		if (type instanceof ESelect_type) {
			ESelect_type st = (ESelect_type)type;
			return isMixedSelectInside(sdaiContext, st);
		}
		return false;
	}

	private static boolean isMixedSelectInside(SdaiContext sdaiContext, ESelect_type st) throws SdaiException {
		ANamed_type sels = sdaiContext != null ? st.getSelections(null, sdaiContext) : st.getSelections(null);
		SdaiIterator it_sels = sels.createIterator();
		while (it_sels.next()) {
			ENamed_type nt = sels.getCurrentMember(it_sels);
			if (nt instanceof EDefined_type) {
				if (isMixedSelectInside(sdaiContext, (EDefined_type)nt)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isMixedSelectInside(SdaiContext sdaiContext, EDefined_type dt) throws SdaiException {
		EEntity domain = dt.getDomain(null);
		if (domain instanceof EDefined_type) {
			return isMixedSelectInside(sdaiContext, (EDefined_type)domain);
		} else if ((domain instanceof ESimple_type) || (domain instanceof EAggregation_type) || (domain instanceof EEnumeration_type)) {
			return true;
		} else if (domain instanceof ESelect_type) {
			return isMixedSelectInside(sdaiContext, (ESelect_type)domain);
		}
		return false;
	}

/**Determines is specified string number*/
	public static boolean isNumber(String s) {
		boolean result = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= '0') && (c <= '9')) {
			} else {
				result = false;
			}
		}
		return result;
	}

/**Returns name of sdai object*/
	public static String getSdaiName(Object sdai) {
		String result = "";
		try  {
			if (sdai instanceof SdaiSession) {
				result = ((SdaiSession)sdai).getSdaiImplementation().getName();
			} else if (sdai instanceof SdaiRepository) {
				result = ((SdaiRepository)sdai).getName();
			} else if (sdai instanceof SdaiModel) {
				result = ((SdaiModel)sdai).getName();
			} else if (sdai instanceof SchemaInstance) {
				result = ((SchemaInstance)sdai).getName();
			} else if (sdai instanceof EntityExtent) {
				result = ((EntityExtent)sdai).getDefinition().getName(null);
			} else {
				result = String.valueOf(sdai);
			}
		} catch (SdaiException e) {}
		return result;
	}

	/**
	* Finds explicit attributes of entity including inherited attributes.
	*/
	public static EExplicit_attribute[] findExplicitAttributes(EEntity_definition entity) throws SdaiException {
		ArrayList rv = new ArrayList();
		findExplicitAttributes(entity, rv);
		return (EExplicit_attribute[])rv.toArray(new EExplicit_attribute[rv.size()]);
	}

	/**
	* Finds explicit attributes of entity including inherited attributes. They are in specified vector.
	*/
	public static void findExplicitAttributes(EEntity_definition entity, List v) throws SdaiException {
		findAllExplicitAttributes(entity, v);
		removeAllRedeclaredAttributes(entity, v);
	}

	// It does not remove redeclered attributes.
	private static void findAllExplicitAttributes(EEntity_definition entity, List v) throws SdaiException {
		AEntity_definition supertypes = entity.getSupertypes(null);
		SdaiIterator i = supertypes.createIterator();
		while (i.next()) {
			findExplicitAttributes(supertypes.getCurrentMember(i), v);
		}
		AExplicit_attribute attributes = entity.getExplicit_attributes(null);
		i = attributes.createIterator();
		while (i.next()) {
			if (!v.contains(attributes.getCurrentMember(i))) { // To avoid dublication of attributes for multiple inheritance with the same root.
				v.add(attributes.getCurrentMember(i));
			}
		}
	}

	private static void removeAllRedeclaredAttributes(EEntity_definition entity, List v) throws SdaiException {
		AEntity_definition supertypes = entity.getSupertypes(null);
		SdaiIterator i = supertypes.createIterator();
		while (i.next()) {
			removeAllRedeclaredAttributes(supertypes.getCurrentMember(i), v);
		}
      // Remove derived-redeclared attributes.
		AAttribute allAttributes = entity.getAttributes(null, null);
		i = allAttributes.createIterator();
		while (i.next()) {
			EAttribute attribute = allAttributes.getCurrentMember(i);
			if (attribute instanceof EDerived_attribute) {
				removeDerivedAttribute((EDerived_attribute)attribute, v);
			}
		}
	}

	private static void removeDerivedAttribute(EDerived_attribute attribute, List v) throws SdaiException {
		if (attribute.testRedeclaring(null)) {
			EEntity redAttribute = (EEntity)attribute.getRedeclaring(null);
			if (redAttribute instanceof EExplicit_attribute) {
				int n = v.size();
				for (int i = 0; i < n; i++) {
					if (redAttribute == v.get(i)) {
						v.remove(i);
						break; // There shuld be only one instance of one attribute.
					}
				}
			} else {
				removeDerivedAttribute((EDerived_attribute)redAttribute, v);
			}
		}
	}

/**If name specifies path than links repository else open repository name*/
	public static SdaiRepository linkRepositoryOrName(String name, String path) throws SdaiException {
		SdaiSession session = SdaiSession.getSession();
		if (session.getActiveTransaction() == null) {
			session.startTransactionReadOnlyAccess();
		}
		ASdaiRepository repos = session.getKnownServers();
		SdaiIterator it_repos = repos.createIterator();
		while (it_repos.next()) {
			SdaiRepository repo = repos.getCurrentMember(it_repos);
			if (repo.getName().equalsIgnoreCase(path)) {
				return repo;
			}
		}
		return session.linkRepository(name, path)	;
	}

/**Starts read only access for model*/
	public static void startReadOnlyAccess(SdaiModel model) throws SdaiException {
		enshureReadOnlyTransaction();
		switch (model.getMode()) {
			case SdaiModel.NO_ACCESS :
				model.startReadOnlyAccess();
				break;
			case SdaiModel.READ_ONLY :
				break;
			case SdaiModel.READ_WRITE :
				model.reduceSdaiModelToRO();
				break;
		}
	}

/**Starts read write access for model*/
	public static void startReadWriteAccess(SdaiModel model) throws SdaiException {
		enshureReadWriteTransaction();
		switch (model.getMode()) {
			case SdaiModel.NO_ACCESS :
				model.startReadWriteAccess();
				break;
			case SdaiModel.READ_ONLY :
				model.promoteSdaiModelToRW();
				break;
			case SdaiModel.READ_WRITE :
				break;
		}
	}

/**Ends access for model*/
	public static void endAccess(SdaiModel model) throws SdaiException {
		switch (model.getMode()) {
			case SdaiModel.NO_ACCESS :
				break;
			case SdaiModel.READ_ONLY :
				model.endReadOnlyAccess();
				break;
			case SdaiModel.READ_WRITE :
				model.endReadWriteAccess();
				break;
		}
	}

/**Enshures that model are in read only access*/
	public static void enshureReadOnlyModel(SdaiModel model) throws SdaiException {
		if (model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		}
	}

/**Enshures that model are in read write access*/
	public static void enshureReadWriteModel(SdaiModel model) throws SdaiException {
		if (model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		} else if (model.getMode() == SdaiModel.READ_ONLY) {
			model.promoteSdaiModelToRW();
		}
	}

/**Enshures that transaction are in read only access*/
	public static void enshureReadOnlyTransaction() throws SdaiException {
		SdaiSession session = SdaiSession.getSession();
		if (!session.testActiveTransaction()) {
			session.startTransactionReadOnlyAccess();
		}
	}

/**Enshures that transaction are in read write access*/
	public static void enshureReadWriteTransaction() throws SdaiException {
		SdaiSession session = SdaiSession.getSession();
		if (!session.testActiveTransaction()) {
			session.startTransactionReadWriteAccess();
		} else {
			SdaiTransaction transaction = session.getActiveTransaction();
			switch (transaction.getMode()) {
				case SdaiTransaction.READ_ONLY :
					transaction.endTransactionAccessAbort();
					session.startTransactionReadWriteAccess();
					break;
				case SdaiTransaction.READ_WRITE :
					break;
			}
		}
	}

/**Tests is attribute redeclared for specified definition.*/
	public static boolean testRedeclaring(EAttribute attribute, EEntity_definition definition, ASdaiModel domain) throws SdaiException {
		boolean result = false;
		AEntity_definition supertypes = definition.getSupertypes(null);
		SdaiIterator it_s = supertypes.createIterator();
		while (it_s.next()) {
			EEntity_definition supertype = supertypes.getCurrentMember(it_s);
			result = result || testRedeclaring(attribute, supertype, domain);
		}
		AAttribute attributes = definition.getAttributes(null, domain);
		SdaiIterator it_a = attributes.createIterator();
		while (it_a.next()) {
			EAttribute attr = attributes.getCurrentMember(it_a);
			result = result || (getRedeclaring(attr) == attribute);
			result = result || ((getRedeclaring(attr) == getRedeclaring(attribute))
							&& (getRedeclaring(attr) != null)
							&& (getRedeclaring(attribute) != null)
							&& (!isSubtype(attribute.getParent_entity(null), definition)));
/*			if (attr instanceof EDerived_attribute) {
				EDerived_attribute da = (EDerived_attribute)attr;
				if (da.testRedeclaring(null)) {
					result =  result || (da.getRedeclaring(null) == attribute);
				}
			} else if (attr instanceof EExplicit_attribute) {
				EExplicit_attribute ea = (EExplicit_attribute)attr;
				if (ea.testRedeclaring(null)) {
					result =  result || (ea.getRedeclaring(null) == attribute);
				}
			} else if (attr instanceof EInverse_attribute) {
				EInverse_attribute ia = (EInverse_attribute)attr;
				if (ia.testRedeclaring(null)) {
					result =  result || (ia.getRedeclaring(null) == attribute);
				}
			}*/
		}
		return result;
	}

	public static boolean isDefinitionInside(Vector definitions, EEntity_definition definition) {
		for (int i = 0; i < definitions.size(); i++) {
			if (definitions.elementAt(i) == definition) {
				return true;
			}
		}
		return false;
	}

/**Changes aggregate low bouds to 1*/
	public static int correctAggregateIndex(int index, EAggregation_type type, int offset) throws SdaiException {
		index = (index<offset)?offset:index;
		int in = index;
		if (type instanceof EArray_type) {
			EArray_type at = (EArray_type)type;
			EBound bound = at.getLower_index(null);
			if (bound instanceof EInteger_bound) {
				EInteger_bound ib = (EInteger_bound)bound;
				int i = ib.getBound_value(null);
				in = index+i-offset;
			}
		} else if (type instanceof EVariable_size_aggregation_type) {
			in = index+1-offset;
		}
		return in;
	}

/**Collects attributes which domain is as specified definition*/
	public static EExplicit_attribute[] findAttributesForDomainDefinition(EEntity_definition definition, ASdaiModel domain) throws SdaiException {
		Vector result = findAttributesForDomainDefinitionVector(definition, domain);
		EExplicit_attribute[] ex = new EExplicit_attribute[result.size()];
		result.toArray(ex);
		return ex;
	}

/**Collects attributes which domain is as specified definition*/
	public static Vector findAttributesForDomainDefinitionVector(EEntity_definition definition, ASdaiModel domain) throws SdaiException {
		Vector result = new Vector();
		AEntity_definition supertypes = definition.getSupertypes(null);
		SdaiIterator it_supertypes = supertypes.createIterator();
		while (it_supertypes.next()) {
			EEntity_definition supertype = supertypes.getCurrentMember(it_supertypes);
			result.addAll(findAttributesForDomainDefinitionVector(supertype, domain));
		}
		result.addAll(findAttributesForDomain(definition, domain));
		ASelect_type selects = new ASelect_type();
		CSelect_type.usedinSelections(null, definition, domain, selects);
		SdaiIterator it_selects = selects.createIterator();
		while (it_selects.next()) {
			ESelect_type select = selects.getCurrentMember(it_selects);
			result.addAll(findAttributesForDomainDefine(select, domain));
		}
		result.addAll(findAttributesForDomainAggr(definition, domain));
		return result;
	}

	private static Vector findAttributesForDomain(EEntity entity, ASdaiModel domain) throws SdaiException {
		Vector result = new Vector();
		AExplicit_attribute attributes = new AExplicit_attribute();
		CExplicit_attribute.usedinDomain(null, entity, domain, attributes);
		addArrayToVector(result, LangUtils.aggregateToArray(attributes));
		return result;
	}

	private static Vector findAttributesForDomainDefine(EEntity entity, ASdaiModel domain) throws SdaiException {
		Vector result = new Vector();
		ADefined_type defines = new ADefined_type();
		CDefined_type.usedinDomain(null, entity, domain, defines);
		SdaiIterator it_defines = defines.createIterator();
		while (it_defines.next()) {
			EDefined_type define = defines.getCurrentMember(it_defines);
			result.addAll(findAttributesForDomain(define, domain));
			result.addAll(findAttributesForDomainDefine(define, domain));
			result.addAll(findAttributesForDomainAggr(define, domain));
		}
		return result;
	}

	private static Vector findAttributesForDomainAggr(EEntity entity, ASdaiModel domain) throws SdaiException {
		Vector result = new Vector();
		AAggregation_type aggregats = new AAggregation_type();
		CAggregation_type.usedinElement_type(null, entity, domain, aggregats);
		SdaiIterator it_aggregats = aggregats.createIterator();
		while (it_aggregats.next()) {
			EAggregation_type aggregate = aggregats.getCurrentMember(it_aggregats);
			result.addAll(findAttributesForDomain(aggregate, domain));
			result.addAll(findAttributesForDomainDefine(aggregate, domain));
			result.addAll(findAttributesForDomainAggr(aggregate, domain));
		}
		return result;
	}

/**Adds array to vector*/
	public static Collection addArrayToVector(Collection vec, Object array[]) {
		for (int i = 0; i < array.length; i++) {
			vec.add(array[i]);
		}
		return vec;
	}

/**Collects nodes for specified domain*/
	public static EEntity getNodesDomain(EDefined_type nodes[]) throws SdaiException {
//		printDefs(nodes);
		EEntity result = null;
		if ((nodes != null) && (nodes.length > 1)) {
			EDefined_type dt = (EDefined_type)nodes[nodes.length-2];
			result = getDefined_typeDomain(dt);
		}
		return result;
	}

/**Determines is limited type inside select*/
	public static boolean isLimitedTypesInsideSelect(EEntity type) throws SdaiException {
		if (type instanceof ESelect_type) {
			ESelect_type st = (ESelect_type)type;
			return isLimitedTypesInsideSelect(st);
		}
		return false;
	}

	private static boolean isLimitedTypesInsideSelect(ESelect_type st) throws SdaiException {
		ANamed_type sels = st.getSelections(null);
		SdaiIterator it_sels = sels.createIterator();
		while (it_sels.next()) {
			ENamed_type nt = sels.getCurrentMember(it_sels);
			if (nt instanceof EDefined_type) {
				if (isLimitedTypesInsideSelect((EDefined_type)nt)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isLimitedTypesInsideSelect(EDefined_type dt) throws SdaiException {
		EEntity domain = dt.getDomain(null);
		if (domain instanceof EDefined_type) {
			return isLimitedTypesInsideSelect((EDefined_type)domain);
		} else if ((domain instanceof ELogical_type) || (domain instanceof EBoolean_type) || (domain instanceof EEnumeration_type)) {
			return true;
		} else if (domain instanceof ESelect_type) {
			return isLimitedTypesInsideSelect((ESelect_type)domain);
		}
		return false;
	}

/**Finds attribte with specified name for specified entity deefiniton.*/
	public static EAttribute findAttribute(EEntity_definition ed, String attrName) throws SdaiException {
		AAttribute attrs = ed.getAttributes(null, null);
		SdaiIterator it_attrs = attrs.createIterator();
		while (it_attrs.next()) {
			EAttribute a = attrs.getCurrentMember(it_attrs);
			if (a.getName(null).equalsIgnoreCase(attrName)) {
				return a;
			}
		}
		return null;
	}

/**Constructs AEntity from specified Object[]*/
	public static AEntity arrayToAEntity(Object ao[]) throws SdaiException {
		AEntity ae = new AEntity();
		if (ao != null) {
			for (int i = 0; i < ao.length; i++) {
				Object o = ao[i];
				if (o instanceof EEntity)
					ae.addByIndex(ae.getMemberCount()+1, (EEntity)o);
			}
		}
		return ae;
	}

/**Constructs Entity[] from specified AEntity*/
	public static EEntity[] aEntityToArray(AEntity ae) throws SdaiException {
		EEntity ao[] = new EEntity[ae.getMemberCount()];
		SdaiIterator it_ae = ae.createIterator();
		int i = 0;
		while (it_ae.next()) {
			ao[i] = ae.getCurrentMemberEntity(it_ae);
			i++;
		}
		return ao;
	}

/**Tests is entity_definition1 subtype of entity_definition2*/
	public static boolean isSubtype(EEntity_definition definition1, EEntity_definition definition2) throws SdaiException {
		boolean result = (definition1 == definition2);
		AEntity_definition supertypes = definition1.getSupertypes(null);
		SdaiIterator it_supertypes = supertypes.createIterator();
		while (it_supertypes.next() && !result) {
			EEntity_definition supertype = supertypes.getCurrentMember(it_supertypes);
			result = result || isSubtype(supertype, definition2);
		}
		return result;
	}

/**Returns index of object in aggregate begining from specified position*/
	public static int indexInAggregate(AEntity aggregate, EEntity instance, int beginPos) throws SdaiException {
		for (int i = beginPos; i < aggregate.getMemberCount(); i++) {
			if (aggregate.getByIndexEntity(i) == instance) {
				return i;
			}
		}
		return -1;
	}

/*Gets supertypes of specified entity definition. Self included.*/
	public static ArrayList getSupertypes(EEntity_definition definition, ArrayList supertypes) throws SdaiException {
		AEntity_definition definitions = definition.getSupertypes(null);
		SdaiIterator it_definitions = definitions.createIterator();
		while (it_definitions.next()) {
			EEntity_definition ed = definitions.getCurrentMember(it_definitions);
			getSupertypes(ed, supertypes);
		}
		if (supertypes.indexOf(definition) == -1) {
			supertypes.add(definition);
		}
		return supertypes;
	}

	public static void appendAggregateToAggregate(Aggregate agg, Aggregate toadd) throws SdaiException {
		SdaiIterator it_toadd = toadd.createIterator();
		SdaiIterator it_agg = agg.createIterator();
		while (it_toadd.next()) {
			it_agg.end();
			agg.addBefore(it_agg, toadd.getCurrentMemberObject(it_toadd), null);
		}
	}

	public static boolean canSetForThisDomain(EEntity valueDomain, EEntity domain) throws SdaiException {
		return canSetForThisDomain(null, valueDomain, domain);
	}

	public static boolean canSetForThisDomain(SdaiContext sdaiContext, EEntity valueDomain, EEntity domain) throws SdaiException {
		if (valueDomain == domain) {
			return true;
		} else if ((domain instanceof EEntity_definition) && (valueDomain instanceof EEntity_definition)) {
		   boolean result = false;
			EEntity_definition def = (EEntity_definition)valueDomain;
		   AEntity_definition supertypes = def.getSupertypes(null);
		   SdaiIterator it = supertypes.createIterator();
		   while (it.next()) {
				EEntity_definition d = supertypes.getCurrentMember(it);
				result = result || canSetForThisDomain(d, domain);
		   }
			return result;
		} else if (domain instanceof ESelect_type) {
			boolean result = false;
			ESelect_type select = (ESelect_type)domain;
			ANamed_type selections = sdaiContext != null ? select.getSelections(null, sdaiContext) : select.getSelections(null);
			SdaiIterator it_s = selections.createIterator();
			while (it_s.next() && !result) {
				domain = selections.getCurrentMember(it_s);
				if (domain instanceof EDefined_type) {
					EDefined_type type = (EDefined_type)domain;
					domain = getDefined_typeDomain(type);
				}
				result = result || canSetForThisDomain(valueDomain, domain);
			}
			return result;
		}
		return false;
	}
	
	/**
	 * Returns all root explicit attributes of specified entity definition.
	 * Root attributes are ones that are not redeclaring any attribute. It is guaranteed
	 * that returned attributes are not redeclared as derived in scope of specified
	 * entity definition.
	 * @param eDefinition specified entity definition.
	 * @return all root explicit attributes of specified entity definition.
	 * @throws SdaiException
	 */
	public static AExplicit_attribute getExplicitAttributes(EEntity_definition eDefinition)
		throws SdaiException {

		// get the sets of attributes. each set contain attributes
		// that are linked by redeclaration tree
		List attrGroups = new LinkedList();
		getAttributes(eDefinition, attrGroups);
		
		// if group has derived or inverse attribute, then it is not explicit
		for (Iterator i = attrGroups.iterator(); i.hasNext();) {
			Set attrGroup = (Set) i.next();
			boolean remove = false;
			for (Iterator j = attrGroup.iterator(); j.hasNext();) {
				EAttribute eAttr = (EAttribute) j.next();
				if (eAttr instanceof EDerived_attribute || eAttr instanceof EInverse_attribute) {
					remove = true;
					break;
				}
			}
			
			if (remove)
				i.remove();
		}
		
		// return only attributes that are not redeclaring.
		// there should be one such attribute per group
		AExplicit_attribute aRes = new AExplicit_attribute();
		for (Iterator i = attrGroups.iterator(); i.hasNext();) {
			Set attrGroup = (Set) i.next();
			EExplicit_attribute eRes = null;
			for (Iterator j = attrGroup.iterator(); j.hasNext();) {
				EExplicit_attribute eAttr = (EExplicit_attribute) j.next();
				if (!eAttr.testRedeclaring(null)) {
					if (eRes != null)
						System.err.println("There is more than one non-redeclaring attribute in one set: " + eAttr);
					eRes = eAttr;
				}
			}
			
			aRes.addUnordered(eRes);
		}

		return aRes;
	}
	
	private static void getRedeclared(EAttribute eAttr, Set redeclared)
		throws SdaiException {
		
		EAttribute eRd = null;
		if (eAttr instanceof EDerived_attribute) {
			EDerived_attribute eDer = (EDerived_attribute) eAttr;
			if (eDer.testRedeclaring(null))
				eRd = (EAttribute) eDer.getRedeclaring(null);
		}
		else if (eAttr instanceof EExplicit_attribute) {
			EExplicit_attribute eExp = (EExplicit_attribute) eAttr;
			if (eExp.testRedeclaring(null))
				eRd = eExp.getRedeclaring(null);
		}
		else if (eAttr instanceof EInverse_attribute) {
			EInverse_attribute eInv = (EInverse_attribute) eAttr;
			if (eInv.testRedeclaring(null))
				eRd = eInv.getRedeclaring(null);
		}

		if (eRd != null) {
			redeclared.add(eRd);
			getRedeclared(eRd, redeclared);
		}
	}
	
	private static boolean intersects(Set set1, Set set2) {
		for (Iterator i = set1.iterator(); i.hasNext();) {
			Object e1 = i.next();
			if (set2.contains(e1))
				return true;			
		}
		
		return false;
	}
	
	private static void getAttributes(EEntity_definition eDefinition, List attrGroups)
		throws SdaiException {
		
		AAttribute aAttr = eDefinition.getAttributes(null, null);
		for (SdaiIterator i = aAttr.createIterator(); i.next();) {
			EAttribute eAttr = aAttr.getCurrentMember(i);
			Set red = new HashSet();
			getRedeclared(eAttr, red);
			red.add(eAttr);

			for (Iterator j = attrGroups.iterator(); j.hasNext();) {
				Set attrGroup = (Set) j.next();
				if (intersects(red, attrGroup)) {
					red.addAll(attrGroup);
					j.remove();
				}
			}

			attrGroups.add(red);
		}

		AEntity_definition aSuper = getSupertypes(eDefinition);
		for (SdaiIterator i = aSuper.createIterator(); i.next();) {
			EEntity_definition eSuper = aSuper.getCurrentMember(i);
			getAttributes(eSuper, attrGroups);
		}
	}

	private static AEntity_definition getSupertypes(EEntity_definition eEd)
		throws SdaiException {
		
		AEntity aGenSupertypes = eEd.getGeneric_supertypes(null);
		AEntity_definition aSupertypes = new AEntity_definition();
		for (SdaiIterator i = aGenSupertypes.createIterator(); i.next();) {
			EEntity eGen = aGenSupertypes.getCurrentMemberEntity(i);
			if (eGen instanceof EEntity_definition)
				aSupertypes.addUnordered(eGen);
		}
		
		return aSupertypes;
	}
}


class MyInteger {
	public int integer;
	public MyInteger(int integer) {
		this.integer = integer;
	}
}