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

package jsdai.mappingCompiler.util;

import jsdai.SMapping_schema.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.ASchemaInstance;
import jsdai.lang.SdaiModel;
import jsdai.lang.ASdaiModel;
import jsdai.lang.Aggregate;
import jsdai.lang.SdaiIterator;
import jsdai.lang.AEntity;
import java.util.List;
import java.util.ArrayList;
import jsdai.util.LangUtils;
import java.util.Arrays;
import jsdai.util.Debug;

public class MappingUtils {
	public static boolean isSelectInside(ENamed_type nt) {
		boolean rv = false;
		try {
			if (nt instanceof EDefined_type) {
				EDefined_type dt = (EDefined_type)nt;
				if (dt.getDomain(null) instanceof ESelect_type) {
					rv = true;
				} else if (dt.getDomain(null) instanceof EAggregation_type) {
					EAggregation_type at = (EAggregation_type)dt.getDomain(null);
					rv = isSelectInside(at);
				}
			}
		} catch(jsdai.lang.SdaiException e) {
	   	e.printStackTrace();
	   }
	   return rv;
	}

	public static boolean isSelectInside(EAggregation_type at) {
		boolean rv = false;
		try {
			EEntity type = at.getElement_type(null);
			if (type instanceof EAggregation_type) {
				rv = isSelectInside((EAggregation_type)type);
			} else if (type instanceof ENamed_type) {
				rv = isSelectInside((ENamed_type)type);
			}
	   } catch(jsdai.lang.SdaiException e) {
	   	e.printStackTrace();
	   }
	   return rv;
	}

	public static ESelect_type getSelect(ENamed_type nt) {
		ESelect_type rv = null;
		try {
			if (nt instanceof EDefined_type) {
				EDefined_type dt = (EDefined_type)nt;
				EEntity domain = dt.getDomain(null);
				if (domain instanceof ESelect_type) {
					rv = (ESelect_type)domain;
				} else if (domain instanceof EAggregation_type) {
					EAggregation_type at = (EAggregation_type)domain;
					rv = getSelect(at);
				}
			}
		} catch(jsdai.lang.SdaiException e) {
	   	e.printStackTrace();
	   }
	   return rv;
	}

	public static ESelect_type getSelect(EAggregation_type at) {
		ESelect_type rv = null;
		try {
			EEntity type = at.getElement_type(null);
			if (type instanceof EAggregation_type) {
				rv = getSelect((EAggregation_type)type);
			} else if (type instanceof ENamed_type) {
				rv = getSelect((ENamed_type)type);
			}
	   } catch(jsdai.lang.SdaiException e) {
	   	e.printStackTrace();
	   }
	   return rv;
	}

	public static boolean isInverseInside(EEntity attribute) throws SdaiException {
		boolean rv = false;
		if (attribute instanceof EAggregate_member_constraint) {
			rv = isInverseInside((EEntity)attribute.get_object(CAggregate_member_constraint.attributeAttribute(null)));
		}
		if (attribute instanceof EInverse_attribute_constraint) {
			rv = true;
		}
		return rv;
	}

//	public static boolean isOfEntityTypeOrSubtypeOf(jsdai.lang.EEntity instance, Entity_definition entity) {
//		return instance.

	  //public static jsdai.lang.EEntity createOrFindInstance(jsdai.lang.

	/**
		Accepts mapping elements that may be as attribute for mapping constraints and returns enumeration type which is domain (may be nested) of this attribute.
		@param attribute may be one of following:
			EAttribute (one of it subtypes: explicit attribute of derived attribute. This class is abstract.)
			EAggregate_member_constraint (it may be nested)
			ESelect_constraint (it should specify full path till enumeration type)
		@retruns enumeration type, or null if there was some problems.
	*/
	public static jsdai.dictionary.EEnumeration_type getEnumerationTypeFromMappingAttribute(EEntity attribute, ASchemaInstance schemaInstance) throws SdaiException {
		jsdai.dictionary.EEnumeration_type rv = null;
		if (attribute instanceof EExplicit_attribute) {
			Object domain = ((EExplicit_attribute)attribute).get_object(CExplicit_attribute.attributeDomain(null));
			if (domain instanceof EDefined_type) {
				rv = LangUtils.getEnumerationTypeFromDefinedType(convertToLang((EDefined_type)domain, schemaInstance));
			} else if (domain instanceof EAggregation_type) {
				rv = getEnumerationTypeFromAggregateType((EAggregation_type)domain, schemaInstance);
			} else {
				Debug.printAssertion("We should not come here. " + domain);
			}
		} else if (attribute instanceof EDerived_attribute) {
			Debug.reportNotImplemented("EDerived_attribute");
		} else if (attribute instanceof ESelect_constraint) {
			ADefined_type domains = ((ESelect_constraint)attribute).getData_type(null);
			EDefined_type domain = domains.getByIndex(domains.getMemberCount());
			rv = LangUtils.getEnumerationTypeFromDefinedType(convertToLang(domain, schemaInstance));
		} else {
			Debug.printAssertion("Bad parameter " + attribute);
		}
		return rv;
	}

	/**
		Accepts aggregation_type and returns enumeration type which is element type of this aggregation type.
		@param type. Its element type may be defined_type (which domain at the end goes enumeration_type) or enumeration_type or aggregation_type.
		@retruns enumeration type, or null if there was some problems.
	*/
	public static jsdai.dictionary.EEnumeration_type getEnumerationTypeFromAggregateType(EAggregation_type type, ASchemaInstance schemaInstance) throws SdaiException {
		jsdai.dictionary.EEnumeration_type rv = null;
		Object domain = type.get_object(CAggregation_type.attributeElement_type(null));
		if (domain instanceof EDefined_type) {
			rv = LangUtils.getEnumerationTypeFromDefinedType(convertToLang((EDefined_type)domain, schemaInstance));
		} else if (domain instanceof EAggregation_type) {
			rv = getEnumerationTypeFromAggregateType((EAggregation_type)domain, schemaInstance);
		} else {
			Debug.printAssertion("We should not come here.");
		}
		return rv;
	}

	/**
		Checks if attribute (directly or indirectly) is of type logical.
	*/
	public static boolean isLogicalInside(EEntity attribute) throws SdaiException{
		EEntity domain = null;
		boolean rv = false;
		if (attribute instanceof EExplicit_attribute) {
			domain = (EEntity)attribute.get_object(CExplicit_attribute.attributeDomain(null));
		} else if (attribute instanceof EDerived_attribute) {
			domain = (EEntity)attribute.get_object(CDerived_attribute.attributeDomain(null));
		} else if (attribute instanceof ESelect_constraint) {
			ESelect_constraint sc = (ESelect_constraint)attribute;
			if (sc.testData_type(null)) {
				ADefined_type types = sc.getData_type(null);
				int n = types.getMemberCount();
				if (n > 0) {
					domain = types.getByIndex(n);
				}
			}
		}
		if (domain instanceof EDefined_type) {
			rv = isLogicalInsideDefinedType((EDefined_type)domain);
		} else if (domain instanceof EAggregation_type) {
			rv = isLogicalInsideAggregateType((EAggregation_type)domain);
		} else if (domain instanceof ELogical_type) {
			rv = true;
		}
		return rv;
	}

	/**
		Checks is logical type in this domain.
	*/
	public static boolean isLogicalInsideDefinedType(EDefined_type type) throws SdaiException {
		boolean rv = false;
		EEntity domain = type.getDomain(null);
		if (domain instanceof EAggregation_type) {
			rv = isLogicalInsideAggregateType((EAggregation_type)domain);
		} if (domain instanceof EDefined_type) {
			rv = isLogicalInsideDefinedType((EDefined_type)domain);
		} if (domain instanceof ESimple_type) {
			rv = domain instanceof ELogical_type;
		}
		return rv;
	}

	/**
		Checks is logical type in this domain.
	*/
	public static boolean isLogicalInsideAggregateType(EAggregation_type type) throws SdaiException {
		boolean rv = false;
		Object domain = type.get_object(CAggregation_type.attributeElement_type(null));
		if (domain instanceof EDefined_type) {
			rv = isLogicalInsideDefinedType((EDefined_type)domain);
		} else if (domain instanceof EAggregation_type) {
			rv = isLogicalInsideAggregateType((EAggregation_type)domain);
		} else if (domain instanceof ELogical_type) {
			rv = true;
		}
		return rv;
	}

	public static ESchema_definition findSchema(String pl, SdaiModel model) throws SdaiException {
		Aggregate a = model.getEntityExtentInstances(ESchema_definition.class);
		SdaiIterator i = a.createIterator();
		while (i.next()) {
			ESchema_definition s = (ESchema_definition)a.getCurrentMemberObject(i);
			if (s.getPersistentLabel().equals(pl)) {
				return s;
			}
		}
		return null;
	}

	public static ESchema_definition findArmSchema(SdaiModel model) throws SdaiException {
		return findSchema("#20000", model);
	}

	public static ESchema_definition findAimSchema(SdaiModel model) throws SdaiException {
		return findSchema("#10000", model);
	}

	public static EEntity_definition findArmEntity(SdaiModel mappingModel, String entityName) throws SdaiException {
		ESchema_definition armSchema = findArmSchema(mappingModel);
		EEntity_definition ins[] = getEntitiesOfSchema(armSchema);
		EEntity_definition armEntity = null;
		boolean foundEntity = false;
		int n = ins.length;
		entityName = entityName.toUpperCase();
		for(int i = 0; i < n; i++) {
			armEntity = ins[i];
			if (armEntity.getName(null).toUpperCase().equals(entityName)) {
				foundEntity = true;
				break;
			}
		}
		if (foundEntity) {
			return armEntity;
		} else {
			return null;
		}
	}

	public static jsdai.dictionary.EAttribute convertToLang(EAttribute a, jsdai.lang.ASchemaInstance schemaInstances) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute rv = null;
		jsdai.lang.SdaiIterator isi = schemaInstances.createIterator();
		jsdai.dictionary.ESchema_definition schema = null;
		while (isi.next()) {
			jsdai.lang.SchemaInstance sii = schemaInstances.getCurrentMember(isi);
			schema = sii.getNativeSchema();
		}
		jsdai.dictionary.EEntity_definition entities[] = LangUtils.getEntitiesOfSchema(schema);
		String s = a.getParent_entity(null).getName(null).toUpperCase();
		jsdai.dictionary.EEntity_definition entity = null;
		boolean found = false;
		int n = entities.length;
		for (int i = 0; i < n; i++) {
			entity = entities[i];
			if (entity.getName(null).toUpperCase().equals(s)) {
				found = true;
				break;
			}
		}
		s = a.getName(null).toUpperCase();
		if (found) {
			jsdai.dictionary.AAttribute attributes = entity.getAttributes(null, null);
			SdaiIterator targetInstance = attributes.createIterator();
			found = false;
			jsdai.dictionary.EAttribute attribute = null;
			while(targetInstance.next()) {
				attribute = attributes.getCurrentMember(targetInstance);
				if (attribute.getName(null).toUpperCase().equals(s)) {
					found = true;
					break;
				}
			}
			if (found) {
				rv = attribute;
			}
		}
		return rv;
	}

	public static jsdai.dictionary.EDefined_type convertToLang(EDefined_type a, jsdai.lang.ASchemaInstance schemaInstances) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EDefined_type rv = null;
		jsdai.lang.SdaiIterator isi = schemaInstances.createIterator();
		jsdai.dictionary.ESchema_definition schema = null;
		while (isi.next()) {
			jsdai.lang.SchemaInstance sii = schemaInstances.getCurrentMember(isi);
			schema = sii.getNativeSchema();
		}
		jsdai.dictionary.EDefined_type[] types = LangUtils.getDefinedTypesOfSchema(schema);
		String s = a.getName(null).toUpperCase();
		jsdai.dictionary.EDefined_type type = null;
		boolean found = false;
		int n = types.length;
		for (int i = 0; i < n; i++) {
			type = types[i];
			if (type.getName(null).toUpperCase().equals(s)) {
				rv = type;
				break;
			}
		}
		return rv;
	}

	public static jsdai.dictionary.EEntity_definition convertToLang(EEntity_definition a, jsdai.lang.ASchemaInstance schemaInstances) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EEntity_definition rv = null;
		jsdai.lang.SdaiIterator isi = schemaInstances.createIterator();
		jsdai.dictionary.ESchema_definition schema = null;
		String s = a.getName(null).toUpperCase();
		while (isi.next()) {
			jsdai.lang.SchemaInstance sii = schemaInstances.getCurrentMember(isi);
			schema = sii.getNativeSchema();
			jsdai.dictionary.EEntity_definition[] entities = LangUtils.getEntitiesOfSchema(schema);
			jsdai.dictionary.EEntity_definition entity = null;
			boolean found = false;
			int n = entities.length;
			for (int i = 0; i < n; i++) {
				entity = entities[i];
				if (entity.getName(null).toUpperCase().equals(s)) {
					return entity;
				}
			}
		}
		return rv;
	}

	public static EEntity_definition[] getEntitiesOfSchema(ESchema_definition schema) throws SdaiException {
		return getEntitiesOfSchema(schema, schema.findEntityInstanceSdaiModel().getRepository().getModels());
	}

	public static ENamed_type[] getNamedTypesOfSchema(ESchema_definition schema) throws SdaiException {
		return getNamedTypesOfSchema(schema, schema.findEntityInstanceSdaiModel().getRepository().getModels());
	}

	public static EEntity_definition[] getEntitiesOfSchema(ESchema_definition schema, ASdaiModel dictionaryDomain) throws SdaiException {
		List rv = new ArrayList();
		ADeclaration declarations = new ADeclaration();
		CDeclaration.usedinParent(null, schema, dictionaryDomain, declarations);
		SdaiIterator i = declarations.createIterator();
		while (i.next()) {
			EDeclaration declaration = declarations.getCurrentMember(i);
			EEntity definition = declaration.getDefinition(null);
			if (definition instanceof EEntity_definition) {
				rv.add(definition);
			}
		}
  		EEntity_definition[] entities = (EEntity_definition[])rv.toArray(new EEntity_definition[0]);
    	Arrays.sort(entities, new ComparatorOfEntities());
		return entities;
	}

	public static ENamed_type[] getNamedTypesOfSchema(ESchema_definition schema, ASdaiModel dictionaryDomain) throws SdaiException {
		List rv = new ArrayList();
		ADeclaration declarations = new ADeclaration();
		CDeclaration.usedinParent(null, schema, dictionaryDomain, declarations);
		SdaiIterator i = declarations.createIterator();
		while (i.next()) {
			EDeclaration declaration = declarations.getCurrentMember(i);
			EEntity definition = declaration.getDefinition(null);
			if (definition instanceof ENamed_type) {
				rv.add(definition);
			}
		}
		return (ENamed_type[])rv.toArray(new ENamed_type[0]);
	}

	public static EAttribute getExpressAttributeDefinitionFromConstraint(EEntity constraint) throws SdaiException {
		EEntity rv = null;
		if (constraint instanceof EAttribute) {
			return (EAttribute)constraint;
		} else if (constraint instanceof EAttribute_value_constraint) {
			jsdai.dictionary.EAttribute a = CAttribute_value_constraint.attributeAttribute(null);
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else if (constraint instanceof ESelect_constraint) {
			jsdai.dictionary.EAttribute a = CSelect_constraint.attributeAttribute(null);
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else if (constraint instanceof EEntity_constraint) {
			jsdai.dictionary.EAttribute a = CEntity_constraint.attributeAttribute(null);
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else if (constraint instanceof EAggregate_member_constraint) {
			jsdai.dictionary.EAttribute a = CAggregate_member_constraint.attributeAttribute(null);
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else if (constraint instanceof EConstraint_attribute) {
			jsdai.dictionary.EAttribute a = null;
			if (constraint instanceof EAttribute_value_constraint) {
				a = CAttribute_value_constraint.attributeAttribute(null);
			} else if (constraint instanceof ESelect_constraint) {
				a = CSelect_constraint.attributeAttribute(null);
			} else if (constraint instanceof EEntity_constraint) {
				a = CEntity_constraint.attributeAttribute(null);
			} else if (constraint instanceof EAggregate_member_constraint) {
				a = CAggregate_member_constraint.attributeAttribute(null);
			}
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else if (constraint instanceof EInverse_attribute_constraint) {
			jsdai.dictionary.EAttribute a = CInverse_attribute_constraint.attributeInverted_attribute(null);
			rv = (jsdai.lang.EEntity)constraint.get_object(a);
		} else {
			return null;
		}
	return getExpressAttributeDefinitionFromConstraint(rv);
	}

	///%*
	//% Checks is base entity supertype of derived entity.
	//% @param base entity wich is suposed to be supertype
	//% @param derived entity wich is suposed to be subtype
	//% @returns true if base is supertype of derived, false otherwise.
	//%/
	public static boolean isSupertype(EEntity_definition base, EEntity_definition derived) throws SdaiException {
		boolean rv = false;
		if (base == derived) {
			rv = true;
		} else {
			AEntity_definition supertypes = derived.getSupertypes(null);
			SdaiIterator i = supertypes.createIterator();
			while (i.next() && !rv) {
				rv = isSupertype(base, supertypes.getCurrentMember(i));
			}
   	}
		return rv;
	}

   ///%*
   //% Goes into any level to find type. All aggregation types are ignored.
   //% @param typeToFind this type will be seacrhed.
   //% @param defType this type where to search.
   //% @returns path of defined types, how to come to specified type. Top level type is not in returned array.
   //% If type is not found it will return null.
   //%/
   public static EDefined_type[] findTypeInDefinedTypeOfSelect(EEntity typeToFind, EDefined_type defType) {
		/*		while (nt instanceof EDefined_type) {
					nt = nt.getDomain(null);
				}
				if (nt instanceof ESelect_type) {
		*/
		return null;
   }

	///%*
	//% Finds explicit attributes of entity including inherited attributes.
	//%/
	public static EAttribute[] findAllAttributes(EEntity_definition entity) throws SdaiException {
		ArrayList rv = new ArrayList();
		findAllAttributes(entity, rv);
		return (EAttribute[])rv.toArray(new EAttribute[rv.size()]);
	}

	///%*
	//% Finds explicit attributes of entity including inherited attributes. They are in specified vector.
	//% It may not work for redeclared attributes and multiple inheritance.
	//%/
	public static void findAllAttributes(EEntity_definition entity, List v) throws SdaiException {
		AEntity attributes = new AEntity();
		CAttribute.usedinParent(null, entity, null, attributes);
		jsdai.lang.SdaiIterator i = attributes.createIterator();
		while (i.next()) {
			EAttribute e = (EAttribute)attributes.getCurrentMemberObject(i);
			int n = v.size();
			boolean f = true;
			for (int j = 0; j < n; j++) {
				EAttribute a = (EAttribute)v.get(j);
				if (e.getName(null).toUpperCase().equals(a.getName(null).toUpperCase())) {
					f = false;
				}
			}
			if (f) {
				v.add(e);
			}
		}
		AEntity_definition supertypes = entity.getSupertypes(null);
		i = supertypes.createIterator();
		while (i.next()) {
			findAllAttributes(supertypes.getCurrentMember(i), v);
		}
	}


	public static jsdai.lang.EEntity getElement1(EPath_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CPath_constraint.attributeElement1(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getElement1(EInstance_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CInstance_constraint.attributeElement1(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getElement2(EConstraint_relationship c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CConstraint_relationship.attributeElement2(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(EAttribute_value_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CAttribute_value_constraint.attributeAttribute(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(ESelect_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CSelect_constraint.attributeAttribute(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(EEntity_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CEntity_constraint.attributeAttribute(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(EAggregate_member_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CAggregate_member_constraint.attributeAttribute(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(EConstraint_attribute c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = null;
		if (c instanceof EAttribute_value_constraint) {
			a = CAttribute_value_constraint.attributeAttribute(null);
		} else if (c instanceof ESelect_constraint) {
			a = CSelect_constraint.attributeAttribute(null);
		} else if (c instanceof EEntity_constraint) {
			a = CEntity_constraint.attributeAttribute(null);
		} else if (c instanceof EAggregate_member_constraint) {
			a = CAggregate_member_constraint.attributeAttribute(null);
		}
		return (jsdai.lang.EEntity)c.get_object(a);
	}

	public static jsdai.lang.EEntity getAttribute(EInverse_attribute_constraint c) throws jsdai.lang.SdaiException {
		jsdai.dictionary.EAttribute a = CInverse_attribute_constraint.attributeInverted_attribute(null);
		return (jsdai.lang.EEntity)c.get_object(a);
	}



	public static jsdai.lang.EEntity getAttribute(EEntity c) throws jsdai.lang.SdaiException {
		if (c instanceof EPath_constraint) {
			jsdai.dictionary.EAttribute a = CPath_constraint.attributeElement1(null);
			return (EEntity)(c.testAttribute(a, null) != 0 ? c.get_object(a) : null);
		} else if (c instanceof EInstance_constraint) {
			jsdai.dictionary.EAttribute a = CInstance_constraint.attributeElement1(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof EAttribute_value_constraint) {
			jsdai.dictionary.EAttribute a = CAttribute_value_constraint.attributeAttribute(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof ESelect_constraint) {
			jsdai.dictionary.EAttribute a = CSelect_constraint.attributeAttribute(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof EEntity_constraint) {
			jsdai.dictionary.EAttribute a = CEntity_constraint.attributeAttribute(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof EAggregate_member_constraint) {
			jsdai.dictionary.EAttribute a = CAggregate_member_constraint.attributeAttribute(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof EConstraint_attribute) {
			jsdai.dictionary.EAttribute a = null;
			if (c instanceof EAttribute_value_constraint) {
				a = CAttribute_value_constraint.attributeAttribute(null);
			} else if (c instanceof ESelect_constraint) {
				a = CSelect_constraint.attributeAttribute(null);
			} else if (c instanceof EEntity_constraint) {
				a = CEntity_constraint.attributeAttribute(null);
			} else if (c instanceof EAggregate_member_constraint) {
				a = CAggregate_member_constraint.attributeAttribute(null);
			}
			return (jsdai.lang.EEntity)c.get_object(a);
		} else if (c instanceof EInverse_attribute_constraint) {
			jsdai.dictionary.EAttribute a = CInverse_attribute_constraint.attributeInverted_attribute(null);
			return (jsdai.lang.EEntity)c.get_object(a);
		}
		return null;
	}

	public static EAttribute findAttributeDefinition(EEntity_definition entity, String attributeName) throws jsdai.lang.SdaiException {
		EAttribute a[] = findAllAttributes(entity);
		int n = a.length;
		for (int i = 0; i < n; i++) {
			EAttribute e = a[i];
			if (e.getName(null).toUpperCase().equals(attributeName.toUpperCase())) {
				return e;
			}
		}
		return null;
	}

	public static EEntity_definition findEntityDefinition(String s, EEntity_definition entities[]) throws jsdai.lang.SdaiException {
		/*int n = entities.length;
		for (int i = 0; i < n; i++) {
			EEntity_definition e = entities[i];
			if (e.getName(null).toUpperCase().equals(s.toUpperCase())) {
				return e;
			}
		}
  		*/
    	EEntity_definition rv = null;
		int i = Arrays.binarySearch(entities, s, new ComparatorOfEntityName());
		if (i >= 0) {
			rv = entities[i];
   	}
		return rv;
	}

	///%*
	//% It finds instance of schema in one model.
	//% @param model where to search for schema.
	//% @returns instance of schema definition. It returns null if it does not find instance of schema definition.
	//%/
	public static ESchema_mapping findMappingSchema(SdaiModel model) throws jsdai.lang.SdaiException {
		jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESchema_mapping.class);
		jsdai.lang.SdaiIterator i = a.createIterator();
		while (i.next()) {
			ESchema_mapping s = (ESchema_mapping)a.getCurrentMemberObject(i);
			return s;
		}
		return null;
	}


	/**
	* Finds explicit attributes of entity including inherited attributes.
	* If attribute is redeclared it is not included.
	*/
	public static EExplicit_attribute[] findExplicitAttributes(EEntity_definition entity) throws SdaiException {
		ArrayList rv = new ArrayList();
		findAllExplicitAttributesForOneEntity(entity, rv);
		return (EExplicit_attribute[])rv.toArray(new EExplicit_attribute[rv.size()]);
	}

	/**
	* Finds explicit attributes of entity including inherited attributes. They are in specified vector.
	* If attribute is redeclared it is not included.
	*/
	public static void findExplicitAttributes(EEntity_definition entity, List v) throws SdaiException {
		findAllExplicitAttributesForOneEntity(entity, v);
		//removeAllRedeclaredAttributes(entity, v);
	}

	/**
	* Finds explicit attributes in recursion.
	* Attributes are imediatily tested for redeclaring.
	*/
	private static void findAllExplicitAttributesForOneEntity(EEntity_definition entity, List v) throws SdaiException {
		SdaiIterator i;
		// First I need to find explicit attributes from supertypes.
		// Latter these attributes will be removed if they are redeclared.
		// If they are redeclared by explicit attributes, new attributes will be added.
		AEntity_definition supertypes = entity.getSupertypes(null);
		i = supertypes.createIterator();
		while (i.next()) {
			findAllExplicitAttributesForOneEntity(supertypes.getCurrentMember(i), v);
		}
		// Add explicit attribute defined in this entity.
		AAttribute attributes = entity.getAttributes(null, null);
		i = attributes.createIterator();
		while (i.next()) {
			EAttribute attribute = attributes.getCurrentMember(i);
			if (attribute instanceof EExplicit_attribute) {
				removeExplicitRedclaredAttribute((EExplicit_attribute)attribute, v);
				if (!v.contains(attribute)) { // To avoid dublication of attributes.
					v.add(attributes.getCurrentMember(i));
				}
			} else if (attribute instanceof EDerived_attribute) {
				removeDerivedAttribute((EDerived_attribute)attribute, v);
			}
		}
	}

	// It does not remove redeclered attributes.
	private static void findAllExplicitAttributes(EEntity_definition entity, List v) throws SdaiException {
		SdaiIterator i;
		//AExplicit_attribute attributes = entity.getExplicit_attributes(null);
		AAttribute attributes = entity.getAttributes(null, null);
		i = attributes.createIterator();
		while (i.next()) {
			EAttribute attribute = attributes.getCurrentMember(i);
			if (attribute instanceof EExplicit_attribute) {
				if (!v.contains(attribute)) { // To avoid dublication of attributes for multiple inheritance with the same root.
					v.add(attributes.getCurrentMember(i));
				}
			}
		}
		AEntity_definition supertypes = entity.getSupertypes(null);
		i = supertypes.createIterator();
		while (i.next()) {
			findExplicitAttributes(supertypes.getCurrentMember(i), v);
		}
	}

	private static void removeAllRedeclaredAttributes(EEntity_definition entity, List v) throws SdaiException {
		AEntity_definition supertypes = entity.getSupertypes(null);
		SdaiIterator i = supertypes.createIterator();
		while (i.next()) {
			removeAllRedeclaredAttributes(supertypes.getCurrentMember(i), v);
		}
      // Remove derived-redeclared and explicit-redeclared attributes.
		AAttribute allAttributes = entity.getAttributes(null, null);
		i = allAttributes.createIterator();
		while (i.next()) {
			EAttribute attribute = allAttributes.getCurrentMember(i);
			if (attribute instanceof EDerived_attribute) {
				removeDerivedAttribute((EDerived_attribute)attribute, v);
			} else if (attribute instanceof EExplicit_attribute) {
				removeExplicitRedclaredAttribute((EExplicit_attribute)attribute, v);
			}
		}
	}

	/**
	* Removes redeclared attributes from suplied List.
	* Suplied list must contain only explicit attributes.
	*/
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

	/**
	* Removes redeclared attributes from suplied List.
	* Suplied list must contain only explicit attributes.
	*/
	private static void removeExplicitRedclaredAttribute(EExplicit_attribute attribute, List v) throws SdaiException {
		if (attribute.testRedeclaring(null)) {
			EEntity redAttribute = (EEntity)attribute.getRedeclaring(null);
			if (redAttribute instanceof EExplicit_attribute) {
				int n = v.size();
				for (int i = 0; i < n; i++) {
					EExplicit_attribute at = (EExplicit_attribute)v.get(i);
					if (redAttribute == at) {
						v.remove(i);
						break; // There shuld be only one instance of one attribute.
					} else if (at.testRedeclaring(null)) {
						if (redAttribute == at.getRedeclaring(null)) {
							v.remove(i);
							break; // There shuld be only one instance of one attribute.
						}
					}
				}
			} else {
				removeExplicitRedclaredAttribute((EExplicit_attribute)redAttribute, v);
			}
		}
	}

	public static boolean redeclares(EAttribute redeclaring, EAttribute redeclared) throws SdaiException {
		boolean rv = false;
		if (redeclaring instanceof EExplicit_attribute) {
			EExplicit_attribute exp = (EExplicit_attribute)redeclaring;
			if (exp.testRedeclaring(null)) {
				EAttribute red = exp.getRedeclaring(null);
				if (red == redeclared) {
					rv = true;
				} else {
					rv = redeclares(red, redeclared);
				}
			}
		} else if (redeclaring instanceof EDerived_attribute) {
			EDerived_attribute exp = (EDerived_attribute)redeclaring;
			if (exp.testRedeclaring(null)) {
				EAttribute red = (EAttribute)exp.getRedeclaring(null);
				if (red == redeclared) {
					rv = true;
				} else {
					rv = redeclares(red, redeclared);
				}
			}
		}
		return rv;
	}

	public static Object getEndType(EAttribute_mapping attributeMapping) throws SdaiException {
		Object rv = null;
		if (attributeMapping != null) {
			if (attributeMapping.testPath(null)) {
				AAttribute_mapping_path_select path = attributeMapping.getPath(null);
				int n = path.getMemberCount();
				if (n > 0) {
					rv = MappingUtilOperations.getEndType((EEntity)path.getByIndex(n));
				}
			} else {
				rv = getEntityMappingTarget(attributeMapping.getParent_entity(null));
			}
		}
		return rv;
	}

	public static EEntity_definition getEntityMappingTarget(EEntity_mapping mapping) throws SdaiException {
		EEntity target = mapping.getTarget(null);
		if (target instanceof EAttribute) {
			target = ((EAttribute)target).getParent_entity(null);
		}
		return (EEntity_definition)target;
	}

	public static boolean containsEntityDefinition(ArrayList types, EEntity_definition entityDefinition) throws SdaiException {
		if (types.contains(entityDefinition)) {
			return true;
		}
		int n = types.size();
		for (int i = 0; i < n; i++) {
			Object o = types.get(i);
			if (o instanceof EEntity_definition) {
				if (isSupertype((EEntity_definition)o, entityDefinition)) {
					return true;
				}
			}
		}
		return false;
	}
}