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

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * This class contains some utility functions that help to do some common operations with jsdai.
 */
/**
 This class cannot be instantiated. It contains some utility functions to do some common operations with jsdai. They can be groped into three groups:
 <ul>
 <li>Operations to work with aggregates	<ul>
  <li>addToAggregate</li>
  <li>aggregateToArray</li>
  <li>tryAddEntityToAggregate</li>
 </ul></li>
 <li>Operations to work with SDAI dictionary (to find some data in SDAI dictionary).<ul>
  <li>findDictionaryModel</li>
  <li>findEntityDefinition</li>
  <li>findExplicitAttributes</li>
  <li>findMappingModel</li>
  <li>findSchema</li>
  <li>findSessionModel</li>
  <li>getDefinedTypesOfSchema</li>
  <li>getEntitiesOfSchema</li>
  <li>getEnumerationTypeFromAggregateType</li>
  <li>getEnumerationTypeFromDefinedType</li>
  <li>getNamedTypesOfSchema</li>
  <li>isSupertype</li>
 </ul></li>
 <li>Mapping operations. They help to find some information about mapping constraints.<ul>
  <li>getEnumerationTypeFromMappingAttribute</li>
  <li>getExpressAttributeDefinitionFromConstraint</li>
  <li>hasEntityInsideAggregate</li>
  <li>isInverseInside</li>
 </ul></li>
 </ul>
 */
public class LangUtils {
   /**
    * It checks does specified attribute have specified entity as a domain or as a nested domain (in the case of aggregates and selects).
    * @param attribute this attribute is limited to explicit attributes
    * @param type this entity type will be seached in attribute domain.
    */
   public static boolean hasAggregateInsideForEntityType(EAttribute attribute, EEntity_definition type) throws jsdai.
      lang.SdaiException {
      boolean rv = false;
      if (attribute instanceof EExplicit_attribute) {
         EExplicit_attribute eAttribute = (EExplicit_attribute) attribute;
         EEntity domain = eAttribute.getDomain(null);
         rv = hasEntityInsideAggregate(domain, type, true);
      }
      return rv;
   }

   /**
    * It checks does specified domain has specified entity as base_type or as a nested domain (in the case of aggregates and selects).
    * @param domain defines where to search wor entity
    * @param type this entity type will be seached in attribute domain.
    * @param firstLevel defines nesting of search
    */
   public static boolean hasEntityInsideAggregate(EEntity domain, EEntity_definition type, boolean firstLevel) throws
      jsdai.lang.SdaiException {
      boolean rv = false;
      if (domain instanceof EAggregation_type) {
         firstLevel = false;
         rv = hasEntityInsideAggregate( ( (EAggregation_type) domain).getElement_type(null), type, firstLevel);
      }
      else if (domain instanceof EDefined_type) {
         rv = hasEntityInsideAggregate( ( (EDefined_type) domain).getDomain(null), type, firstLevel);
      }
      else if (domain instanceof EEntity_definition) {
         if (domain == type) {
            rv = !firstLevel;
         }
      }
      else if (domain instanceof ESelect_type) {
         ESelect_type st = (ESelect_type) domain;
         ANamed_type selections = st.getSelections(null);
         SdaiIterator iterator = selections.createIterator();
         while (iterator.next()) {
            rv = rv || hasEntityInsideAggregate(selections.getCurrentMember(iterator), type, firstLevel);
            if (rv) {
               break;
            }
         }
      }
      return rv;
   }

   /**
    * This method adds entity to attribute, which is of aggregate type. If attribute is not set it does not set it (or does not create aggregate if entity must be set in nested aggregate).
    */
   public static boolean tryAddToAttributeAggregate(EEntity entity, EAttribute attribute, EEntity value) throws jsdai.
      lang.SdaiException {
      boolean rv = false;
      if (attribute instanceof EExplicit_attribute) {
         EDefined_type[] types = new EDefined_type[20];
         if (entity.testAttribute(attribute, types) == 0) {
            return rv;
         }
         Object currentValue = entity.get_object(attribute);
         if (currentValue instanceof Aggregate) {
            rv = tryAddEntityToAggregate( (Aggregate) currentValue, value);
         }
      }
      return rv;
   }

   /**
    * This method is left for compatibility with previous versions and will be removed in future.
    */
   public static boolean tryAddEntityToAggregate(Aggregate aggregate, EEntity value) throws jsdai.lang.SdaiException {
      boolean rv = false;
      /*		EEntity domain = null; //aggregate.getDomain();
        if (domain instanceof EAggregation_type) {
       SdaiIterator iterator = aggregate.createIterator();
       while (iterator.next()) {
        rv = tryAddEntityToAggregate((Aggregate)aggregate.getGetCurrentMemberObject(iterator), value);
        if (rv) {
       break;
        }
       }
        } else if (domain instanceof EEntity_definition) {
       //if (domain == type) { // variable type is missing.
       //	aggregate.add(value);
       //}
        } else if (domain instanceof ESelect_type) {
       if (hasEntityAtFirstLevel((ESelect_type)domain, value.getEntityInstanceType())) {
        aggregate.add(value);
        rv = true;
        return rv;
       }
       SdaiIterator iterator = aggregate.createIterator();
       while (iterator.next()) {
        EDefined_type types = new EDefined_type[20];
        if (aggregate.testCurrentMember(iterator, types) != 0) {
       Object o = aggregate.getCurrentMember(iterator);
       if (o instanceof Aggregate) {
        rv = tryAddEntityToAggregate((EAggregate)o, value);
        if (rv) {
       return rv;
        }
       }
        } else {
       // there should be handling of others posible domains, maybe defined type and then aggregate ...
        }
       }
        } else if (domain instanceof EDefined_type) {
       // the same as for select type. I hope is should work. But I left it separeted for //Debuging.
       if (hasEntityAtFirstLevel((ESelect_type)domain, value.getEntityInstanceType())) {
        aggregate.add(value);
        rv = true;
        return rv;
       }
       SdaiIterator iterator = aggregate.createIterator();
       while (iterator.next()) {
        EDefined_type types = new EDefined_type[20];
        if (aggregate.testCurrentMember(iterator, types) != 0) {
       Object o = aggregate.getCurrentMember(iterator);
       if (o instanceof Aggregate) {
        rv = tryAddEntityToAggregate((EAggregate)o, value);
        if (rv) {
       return rv;
        }
       }
        }
       }
        }
       */return rv;

   }

   /**
    * This method is left for compatibility with previous versions and will be removed in future.
    */
   public static void addToAttributeAggregate(EEntity entity, EAttribute attribute, EEntity value) {
      boolean rv = false;
      /*		if (attribute instanceof EExplicit_attribute) {
       EDefined_type[] types = new EDefined_type[20];
       if (entity.testAttribute(attribute, types) == 0) {
        EEntity domain = attribute.getDomain();
        addToAttribute(entity, attribute, value, domain, types, 0);
       } else {
        Object currentValue = entity.get_object(attribute, types);
        if (currentValue instanceof Aggregate) { // it could be even for any case, not only for aggregate, but this method needs to set only for aggregate
       rv = tryAddEntityToAggregate((Aggregate)currentValue, value);
        }
       }
        }
       return
       ;
       */
   }

   /**
    Accepts defined_type and returns enumeration type which is named by this defined type.
    @param type. Its domain may be defined type (which domain at the end goes enumeration_type) or enumeration_type or aggregation_type.
    @returns enumeration type, or null if there was some problems.
    */
   public static EEnumeration_type getEnumerationTypeFromDefinedType(EDefined_type type) throws SdaiException {
      EEnumeration_type rv = null;
      EEntity domain = type.getDomain(null);
      if ( (domain instanceof ESet_type) || (domain instanceof EBag_type) || (domain instanceof EList_type) ||
          (domain instanceof EArray_type)) {
         rv = getEnumerationTypeFromAggregateType( (EAggregation_type) domain);
      }
      else if (domain instanceof EEnumeration_type) {
         rv = (EEnumeration_type) domain;
      }
      else {
         //Debug.printAssertion("Bad domain (expected enumeration) " + type);
      }
      return rv;
   }

   /**
    Accepts aggregation_type and returns enumeration type which is element type of this aggregation type.
    @param type. Its element type may be defined_type (which domain at the end goes enumeration_type) or enumeration_type or aggregation_type.
    @retruns enumeration type, or null if there was some problems.
    */
   public static EEnumeration_type getEnumerationTypeFromAggregateType(EAggregation_type type) throws SdaiException {
      EEnumeration_type rv = null;
      Object domain = type.getElement_type(null);
      if (domain instanceof EDefined_type) {
         rv = getEnumerationTypeFromDefinedType( (EDefined_type) domain);
      }
      else if (domain instanceof EAggregation_type) {
         rv = getEnumerationTypeFromAggregateType( (EAggregation_type) domain);
      }
      else {
         //Debug.printAssertion("We should not come here.");
      }
      return rv;
   }

   /**
    Set value of attribute. It comes around lang but with set(Integer or Double).
    */
   public static void setAttribute(EEntity targetInstance, EAttribute la, Object value, EDefined_type[] types) throws
      SdaiException {
      if (value instanceof Integer) {
         targetInstance.set(la, ( (Integer) value).intValue(), types);
      }
      else if (value instanceof Double) {
         targetInstance.set(la, ( (Double) value).doubleValue(), types);
      }
      else {
         targetInstance.set(la, value, types);
      }
   }

   /**
    Add value to aggregate. It comes around lang but with add(Integer or Double) and diferences in adding elements to diferent types of aggregates.
    @param aggregate aggregate to which value will be added
    @param value this value will be added to aggregate
    @param type clarification of types for selects only
    @index index in aggregate where to add new value
    @useIndex is there need to add using index, else value will be added at the end of aggregate
    */
   public static void addToAggregate(Aggregate aggregate, Object value, EDefined_type[] types, int index,
                                     boolean useIndex) throws SdaiException {
	   EAggregation_type aggregateType = aggregate.getAggregationType();
	   if(aggregateType instanceof EList_type) {
		   if (!useIndex) {
			   index = aggregate.getMemberCount() + 1;
		   }
		   aggregate.addByIndex(index, value, types);
	   } else if(aggregateType instanceof EArray_type) {
		   aggregate.setByIndex(index, value, types);
	   } else {
            aggregate.addUnordered(value, types);
	   }
   }

   /**
    Add value to aggregate. It comes around lang but with add(Integer or Double) and diferences in adding elements to diferent types of aggregates.
    */
   public static void addToAggregate(Aggregate aggregate, Object value, EDefined_type[] types) throws SdaiException {
      addToAggregate(aggregate, value, types, -1, false);
   }

   /**
    Prints contents of one model to given output. If no output is given then outputs to System.out.
    @param model contents of this model will be printed using toString method.
       @param out output stream where contents of model will be printed. It may be null. If out is null System.out is used.
    */
   public static void dumpModel(SdaiModel model, java.io.PrintStream out) throws SdaiException {
      if (model == null) {
         //Debug.printAssertion(//Debug.ASSERTION_NULL, "Parameter model is null.");
         return;
      }
      if (out == null) {
         out = System.out;
      }
      jsdai.lang.Aggregate a = model.getInstances();
      jsdai.lang.SdaiIterator i = a.createIterator();
      while (i.next()) {
         out.println(a.getCurrentMemberObject(i));
      }
   }

   /**
    Prints contents of one model to standard output (System.out).
    @param model - contents of this model will be printed using toString method.
    */
   public static void dumpModel(SdaiModel model) throws SdaiException {
      dumpModel(model, null);
   }

   /**
    If parameter is aggregate prints contents of it, else simply prints object.
    @param object object to be printed.
    */
   public static void printContentsOf(Object object, String indentation) throws SdaiException {
      if (object instanceof Aggregate) {
         Aggregate a = (Aggregate) object;
         SdaiIterator i = a.createIterator();
         System.out.println(a.getMemberCount());
         indentation += "	";
         while (i.next()) {
            printContentsOf(a.getCurrentMemberObject(i), indentation);
         }
      }
      else {
         System.out.println(indentation + object);
      }
   }

   /**
    * It finds entity_definition in specified schema.
    * @param entityName name of entity which will be searched in schema
    * @param dictionarySchema schema where to search
    * @returns entity wiht the specified name. It returns null if it did not find entity with specified name.
    * Note. This method works only in one model.
    */
   public static EEntity_definition findEntityDefinition(String entityName, ESchema_definition dictionarySchema) throws
      SdaiException {
      EEntity_definition[] entities = getEntitiesOfSchema(dictionarySchema);
      int n = entities.length;
      for (int i = 0; i < n; i++) {
         if (entities[i].getName(null).equalsIgnoreCase(entityName)) {
            return entities[i];
         }
      }
      return null;
   }

   /**
    * Finds entity_definition in specified array of entity definitions.
    //* This method uses binary search, because of this the array of entity
     //* definitions must be sorted in ascending order using names of entity
       //* definitions (the case is ignored).
       * @param entityName name of entity which will be searched in schema
       * @param dictionarySchema schema where to search
       * @returns entity wiht the specified name. It returns null if it did not find entity with specified name.
       * Note. This method works only in one model.
       */
      public static EEntity_definition findEntityDefinition(String s, EEntity_definition entities[]) throws jsdai.lang.
         SdaiException {
         EEntity_definition rv = null;
         int i = Arrays.binarySearch(entities, s, new ComparatorOfEntityName());
         if (i >= 0) {
            rv = entities[i];
         }
         return rv;
      }

   /**
    * It finds all entities that belongs to specified schema.
    * @param schema where to search for entities.
       * @returns all entities visible in this schema. Entities are sorded in ascending order by their names ignoring cases.
    */
   public static EEntity_definition[] getEntitiesOfSchema(ESchema_definition schema) throws SdaiException {
      List rv = new ArrayList();
      ADeclaration declarations = new ADeclaration();
      CDeclaration.usedinParent_schema(null, schema, SdaiSession.getSession().getActiveModels(), declarations);
      SdaiIterator i = declarations.createIterator();
      while (i.next()) {
         EDeclaration declaration = declarations.getCurrentMember(i);
         EEntity namedType = declaration.getDefinition(null);
         if (namedType instanceof EEntity_definition) {
            rv.add(namedType);
         }
      }
      EEntity_definition[] entities = (EEntity_definition[]) rv.toArray(new EEntity_definition[0]);
      Arrays.sort(entities, new ComparatorOfEntities());
      return entities;
   }

   /**
    * It finds all named types that belongs to specified schema.
    * @param schema where to search for named types.
    * @returns all named types visible in this schema.
    */
   public static ENamed_type[] getNamedTypesOfSchema(ESchema_definition schema) throws SdaiException {
      List rv = new ArrayList();
      ADeclaration declarations = new ADeclaration();
      CDeclaration.usedinParent_schema(null, schema, null, declarations);
      SdaiIterator i = declarations.createIterator();
      while (i.next()) {
         EDeclaration declaration = declarations.getCurrentMember(i);
         EEntity definition = declaration.getDefinition(null);
         if (definition instanceof ENamed_type) {
            rv.add(definition);
         }
      }
      return (ENamed_type[]) rv.toArray(new ENamed_type[0]);
   }

   /**
    * It finds all defined types that belongs to specified schema.
    * @param schema where to search for defined types.
    * @returns all defined types visible in this schema.
    */
   public static EDefined_type[] getDefinedTypesOfSchema(ESchema_definition schema) throws SdaiException {
      List rv = new ArrayList();
      ADeclaration declarations = new ADeclaration();
      CDeclaration.usedinParent_schema(null, schema, null, declarations);
      SdaiIterator i = declarations.createIterator();
      while (i.next()) {
         EDeclaration declaration = declarations.getCurrentMember(i);
         EEntity definition = declaration.getDefinition(null);
         if (definition instanceof EDefined_type) {
            rv.add(definition);
         }
      }
      return (EDefined_type[]) rv.toArray(new EDefined_type[0]);
   }

   /**
    * It finds attribute definition for attribute that is constraint by specified constraint.
    * @param constraint defines constraint that constraints attribute.
    * @returns attribute that is constraint.
    */
   public static EAttribute getExpressAttributeDefinitionFromConstraint(EEntity constraint) throws SdaiException {
      EEntity rv = null;
      if (constraint instanceof EAttribute) {
         return (EAttribute) constraint;
      }
      else if (constraint instanceof EAttribute_value_constraint) {
         jsdai.dictionary.EAttribute a = CAttribute_value_constraint.attributeAttribute(null);
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else if (constraint instanceof ESelect_constraint) {
         jsdai.dictionary.EAttribute a = CSelect_constraint.attributeAttribute(null);
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else if (constraint instanceof EEntity_constraint) {
         jsdai.dictionary.EAttribute a = CEntity_constraint.attributeAttribute(null);
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else if (constraint instanceof EAggregate_member_constraint) {
         jsdai.dictionary.EAttribute a = CAggregate_member_constraint.attributeAttribute(null);
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else if (constraint instanceof EConstraint_attribute) {
         jsdai.dictionary.EAttribute a = null;
         if (constraint instanceof EAttribute_value_constraint) {
            a = CAttribute_value_constraint.attributeAttribute(null);
         }
         else if (constraint instanceof ESelect_constraint) {
            a = CSelect_constraint.attributeAttribute(null);
         }
         else if (constraint instanceof EEntity_constraint) {
            a = CEntity_constraint.attributeAttribute(null);
         }
         else if (constraint instanceof EAggregate_member_constraint) {
            a = CAggregate_member_constraint.attributeAttribute(null);
         }
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else if (constraint instanceof EInverse_attribute_constraint) {
         jsdai.dictionary.EAttribute a = CInverse_attribute_constraint.attributeInverted_attribute(null);
         rv = (jsdai.lang.EEntity) constraint.get_object(a);
      }
      else {
         return null;
      }
      return getExpressAttributeDefinitionFromConstraint(rv);
   }

   /**
    * It finds instance of schema in one model.
    * @param model where to search for schema.
    * @returns instance of schema definition. It returns null if it does not find instance of schema definition.
    */
   public static ESchema_definition findSchema(SdaiModel model) throws jsdai.lang.SdaiException {
      jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESchema_definition.class);
      jsdai.lang.SdaiIterator i = a.createIterator();
      while (i.next()) {
         ESchema_definition s = (ESchema_definition) a.getCurrentMemberObject(i);
         //if (s.getPersistentLabel().equals(pl)) {
         return s;
         //}
      }
      return null;
   }

   /**
    * It finds instance of schema that defines ARM data in one model.
    * @param model where to search for schema.
    * @returns instance of schema definition. It returns null if it does not find instance of schema definition.
    */
   public static ESchema_definition findArmSchema(SdaiModel model) throws jsdai.lang.SdaiException {
      return findSchema(model);
   }

   /**
    * It finds instance of schema in one model.
    * @param model where to search for schema.
    * @returns instance of schema definition. It returns null if it does not find instance of schema definition.
    */
   public static ESchema_mapping findMappingSchema(SdaiModel model) throws jsdai.lang.SdaiException {
      jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESchema_mapping.class);
      jsdai.lang.SdaiIterator i = a.createIterator();
      while (i.next()) {
         ESchema_mapping s = (ESchema_mapping) a.getCurrentMemberObject(i);
         return s;
      }
      return null;
   }

   /**
    * It finds model in dictionary repository.
    * @param name the name of the model without sufux
    * @sufix sufinx the sufix for model name
    * @returns with name (specified name + specified sufix). It returns null if it does not find model with such name.
    */
   public static SdaiModel findSessionModel(String name, String sufix) throws jsdai.lang.SdaiException {
      SdaiModel rv = null, model;
      name = name.toUpperCase() + sufix;
      ASdaiModel models = SdaiSession.getSession().getDataDictionary().getRepository().getModels();
      SdaiIterator iterator = models.createIterator();
      while (iterator.next()) {
         model = models.getCurrentMember(iterator);
         if (model.getName().equals(name)) {
            rv = model;
            break;
         }
      }
      return rv;
   }

   /**
    * It finds dictionary model in dictionary repository.
    * @param name the name of the model without sufux
    * @returns with name (specified name + dictionary sufix). It returns null if it does not find model with such name.
    */
   public static SdaiModel findDictionaryModel(String name) throws jsdai.lang.SdaiException {
      return findSessionModel(name, SdaiSession.DICTIONARY_NAME_SUFIX);
   }

   /**
    * It finds mapping model in dictionary repository.
    * @param name the name of the model without sufux
    * @returns with name (specified name + mapping sufix). It returns null if it does not find model with such name.
    */
   public static SdaiModel findMappingModel(String name) throws jsdai.lang.SdaiException {
      return findSessionModel(name, SdaiSession.MAPPING_NAME_SUFIX);
   }

   /**
    Accepts mapping elements that may be as attribute for mapping constraints and returns enumeration type which is domain (may be nested) of this attribute.
    @param attribute may be one of following:
     EAttribute (one of it subtypes: explicit attribute of derived attribute. This class is abstract.),
     EAggregate_member_constraint (it may be nested),
     ESelect_constraint (it should specify full path till enumeration type)
    @retruns enumeration type, or null if there was some problems.
    */
   public static jsdai.dictionary.EEnumeration_type getEnumerationTypeFromMappingAttribute(EEntity attribute,
      ASdaiModel schemaInstance) throws SdaiException {
      jsdai.dictionary.EEnumeration_type rv = null;
      if (attribute instanceof EExplicit_attribute) {
         Object domain = ( (EExplicit_attribute) attribute).get_object(CExplicit_attribute.attributeDomain(null));
         if (domain instanceof EDefined_type) {
            rv = LangUtils.getEnumerationTypeFromDefinedType( (EDefined_type) domain);
         }
         else if (domain instanceof EAggregation_type) {
            rv = getEnumerationTypeFromAggregateType( (EAggregation_type) domain, schemaInstance);
         }
         else {
            //Debug.printAssertion("We should not come here. " + domain);
         }
      }
      else if (attribute instanceof EDerived_attribute) {
         //Debug.reportNotImplemented("EDerived_attribute");
      }
      else if (attribute instanceof ESelect_constraint) {
         ADefined_type domains = ( (ESelect_constraint) attribute).getData_type(null);
         EDefined_type domain = domains.getByIndex(domains.getMemberCount());
         rv = LangUtils.getEnumerationTypeFromDefinedType(domain);
      }
      else {
         //Debug.printAssertion("Bad parameter " + attribute);
      }
      return rv;
   }

   /**
    Accepts aggregation_type and returns enumeration type which is element type of this aggregation type.
    @param type. Its element type may be defined_type (which domain at the end goes enumeration_type) or enumeration_type or aggregation_type.
    @retruns enumeration type, or null if there was some problems.
    */
   public static jsdai.dictionary.EEnumeration_type getEnumerationTypeFromAggregateType(EAggregation_type type,
      ASdaiModel schemaInstance) throws SdaiException {
      jsdai.dictionary.EEnumeration_type rv = null;
      Object domain = type.get_object(CAggregation_type.attributeElement_type(null));
      if (domain instanceof EDefined_type) {
         rv = LangUtils.getEnumerationTypeFromDefinedType( (EDefined_type) domain);
      }
      else if (domain instanceof EAggregation_type) {
         rv = getEnumerationTypeFromAggregateType( (EAggregation_type) domain, schemaInstance);
      }
      else {
         //Debug.printAssertion("We should not come here.");
      }
      return rv;
   }

   /**
    * Cheks does constraint has inverse constraints.
    * @attribute constraint that is checked
    * @returns true if there is inverse constraint, false otherwise.
    */
   public static boolean isInverseInside(EEntity attribute) throws SdaiException {
      boolean rv = false;
      if (attribute instanceof EAggregate_member_constraint) {
         rv = isInverseInside( (EEntity) attribute.get_object(CAggregate_member_constraint.attributeAttribute(null)));
      }
      if (attribute instanceof EInverse_attribute_constraint) {
         rv = true;
      }
      return rv;
   }

   /**
    * This functionality now is in model.
    */
   public static AEntity getInstancesOfEntity(SdaiModel model, jsdai.dictionary.EEntity_definition entity) throws
      SdaiException {
      AEntity rv = new AEntity();
      Aggregate a = model.getInstances();
      SdaiIterator i = a.createIterator();
      while (i.next()) {
         EEntity ins = (EEntity) a.getCurrentMemberObject(i);
         if (ins.getInstanceType() == entity) {
            rv.addByIndex(rv.getMemberCount() + 1, ins);
         }
      }
      return rv;
   }

   /**
    * Checks is base entity supertype of derived entity.
    * @param base entity wich is suposed to be supertype
    * @param derived entity wich is suposed to be subtype
    * @returns true if base is supertype of derived, false otherwise.
    */
   public static boolean isSupertype(EEntity_definition base, EEntity_definition derived) throws SdaiException {
      boolean rv = true;
      if (base == derived) {
         return true;
      }
      AEntity_definition supertypes = derived.getSupertypes(null);
      SdaiIterator i = supertypes.createIterator();
      while (i.next() && rv) {
         rv = !isSupertype(base, supertypes.getCurrentMember(i));
      }
      return!rv;
   }

   /**
    * Converts SDAI aggregate to Java array.
    * @param aggregate to be converted
    * @returns Java array with elements for aggregate.
    */
   public static Object[] aggregateToArray(Aggregate aggregate) throws SdaiException {
      if (aggregate == null) {
         return null;
      }
      int n = aggregate.getMemberCount();
      Object rv[] = new Object[n]; // newInstance(Class elementType)
      SdaiIterator i = aggregate.createIterator();
      int j = 0;
      while (i.next()) {
         Object o = aggregate.getCurrentMemberObject(i);
         rv[j] = o;
         j++;
      }
      return rv;
   }

   /**
    * Converts SDAI aggregate to Java array. It will thrown run time exceptio if not all elements fits to elementType.
    * @param aggregate to be converted
    * @param elementType type of elements
    * @returns Java array with elements for aggregate. Returned array is of specified type.
    */
   public static Object[] aggregateToArray(Aggregate aggregate, Class elementType) throws SdaiException {
      if (aggregate == null) {
         return null;
      }
      int n = aggregate.getMemberCount();
      Object rv[];
      if (elementType == null) {
         rv = new Object[n]; // newInstance(Class elementType)
      }
      else {
         rv = (Object[]) Array.newInstance(elementType, n); // newInstance(Class elementType)
      }
      SdaiIterator i = aggregate.createIterator();
      int j = 0;
      while (i.next()) {
         Object o = aggregate.getCurrentMemberObject(i);
         rv[j] = o;
         j++;
      }
      return rv;
   }

   /**
    * Simplified version of open and find repository.
    * It searches for such repository if there is no such repository,
    * then it tries to open such physical file.
    */
   public static SdaiRepository openRepositoryOrFile(String name) throws SdaiException {
      SdaiRepository rv = null;
      try {
         rv = SdaiSession.getSession().linkRepository(name, null);
      }
      catch (Exception e) {
      }
      if (rv == null) {
         rv = SdaiSession.getSession().importClearTextEncoding("", name, null);
      }
      else {
         rv.openRepository();
      }
      return rv;
   }

   /**
    * Finds explicit attributes of entity including inherited attributes.
    * If attribute is redeclared it is not included.
    */
   public static EExplicit_attribute[] findExplicitAttributes(EEntity_definition entity) throws SdaiException {
      ArrayList rv = new ArrayList();
      findAllExplicitAttributesForOneEntity(entity, rv);
      return (EExplicit_attribute[]) rv.toArray(new EExplicit_attribute[rv.size()]);
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
            removeExplicitRedclaredAttribute( (EExplicit_attribute) attribute, v);
            if (!v.contains(attribute)) { // To avoid dublication of attributes.
               v.add(attributes.getCurrentMember(i));
            }
         }
         else if (attribute instanceof EDerived_attribute) {
            removeDerivedAttribute( (EDerived_attribute) attribute, v);
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
            removeDerivedAttribute( (EDerived_attribute) attribute, v);
         }
         else if (attribute instanceof EExplicit_attribute) {
            removeExplicitRedclaredAttribute( (EExplicit_attribute) attribute, v);
         }
      }
   }

   /**
    * Removes redeclared attributes from suplied List.
    * Suplied list must contain only explicit attributes.
    */
   private static void removeDerivedAttribute(EDerived_attribute attribute, List v) throws SdaiException {
      if (attribute.testRedeclaring(null)) {
         EEntity redAttribute = (EEntity) attribute.getRedeclaring(null);
         if (redAttribute instanceof EExplicit_attribute) {
            int n = v.size();
            for (int i = 0; i < n; i++) {
               if (redAttribute == v.get(i)) {
                  v.remove(i);
                  break; // There shuld be only one instance of one attribute.
               }
            }
         }
         else {
            removeDerivedAttribute( (EDerived_attribute) redAttribute, v);
         }
      }
   }

   /**
    * Removes redeclared attributes from suplied List.
    * Suplied list must contain only explicit attributes.
    */
   private static void removeExplicitRedclaredAttribute(EExplicit_attribute attribute, List v) throws SdaiException {
      if (attribute.testRedeclaring(null)) {
         EEntity redAttribute = (EEntity) attribute.getRedeclaring(null);
         if (redAttribute instanceof EExplicit_attribute) {
            int n = v.size();
            for (int i = 0; i < n; i++) {
               EExplicit_attribute at = (EExplicit_attribute) v.get(i);
               if (redAttribute == at) {
                  v.remove(i);
                  break; // There shuld be only one instance of one attribute.
               }
               else if (at.testRedeclaring(null)) {
                  if (redAttribute == at.getRedeclaring(null)) {
                     v.remove(i);
                     break; // There shuld be only one instance of one attribute.
                  }
               }
            }
         }
         else {
            removeExplicitRedclaredAttribute( (EExplicit_attribute) redAttribute, v);
         }
      }
   }

   /**
    * @deprecated 
    * Use convertEnumerationStringToInt(String, EDefined_type, SdaiContext) instead. 
    */
	public static int convertEnumerationStringToInt(String value, EDefined_type definedType) throws SdaiException {
		return convertEnumerationStringToInt(value, definedType, null);
	}
	
   /**
    * Returns an integer that represents value from enumeration as defined type.
    * @param value string representation of constraint from enumeration.
    * @param type enumeration type where constant is defined
    * @returns integer value that represent specified value. 
	*          0 if there is no element in enumeration with specified name.
    */
	public static int convertEnumerationStringToInt(String value,
													EDefined_type definedType, SdaiContext context) throws SdaiException {
		// FIXME: enhance to work with selects
		return convertEnumerationStringToInt(value, (EEnumeration_type)definedType.getDomain(null), context);
	}

   /**
    * @deprecated 
    * Use convertEnumerationStringToInt(String, EEnumeration_type, SdaiContext) instead. 
    */
	public static int convertEnumerationStringToInt(String value, EEnumeration_type type) throws SdaiException {
		return convertEnumerationStringToInt(value, type, null);
	}
	
   /**
    * Returns an integer that represents value from enumeration.
    * @param value string representation of constraint from enumeration.
    * @param type enumeration where constant is defined
    * @returns integer value that represent specified value.
	*          0 if there is no element in enumeration with specified name.
    */
   public static int convertEnumerationStringToInt(String value, EEnumeration_type type, SdaiContext context) throws jsdai.lang.
      SdaiException {
	   jsdai.lang.A_string values;
	   if(context != null){
		   if(type instanceof EExtensible_enumeration_type){
			   values = type.getElements(null, context);
		   }else{
			   values = type.getElements(null);
		   }
	   }else{
		   values = type.getElements(null);
	   }
      jsdai.lang.SdaiIterator i = values.createIterator();
      //String s = constraint.getConstraint_value(null);
      int n = 0;
      while (i.next()) {
         n++;
         String s1 = values.getCurrentMember(i);
         if (s1.equalsIgnoreCase(value)) {
            return n;
         }
      }
      return 0;
   }

   public static int convertEnumerationStringToInt(SdaiContext context,
		   String value, EEnumeration_type type) throws SdaiException {
	   jsdai.lang.A_string values = type.getElements(null, context);
	   jsdai.lang.SdaiIterator i = values.createIterator();
	   int n = 0;
	   while (i.next()) {
		   n++;
		   String s1 = values.getCurrentMember(i);
		   if (s1.equalsIgnoreCase(value)) {
			   return n;
		   }
	   }
	   return 0;
   }

   /**
    * Returns an integer that represents value from enumeration.
    * @param value string representation of constrant from enumeration.
    * @param type enumeration where constant is defined
       * @returns integer value that represent specified value. 0 if there is no element in enumeration with specified name.
    */
   public static String convertEnumerationIntToString(int value, EEnumeration_type type) throws jsdai.lang.
      SdaiException {
      jsdai.lang.A_string values = type.getElements(null);
      String rv = null;
      if ( (value > 0) || (value <= values.getMemberCount())) {
         rv = values.getByIndex(value);
      }
      return rv;
   }

   /**
    * Removes dictionary sufix from model name.
    * @param name dictionary model name
    * @returns string without standard dictinary sufix. It returns the same string as parameter name if name does not have standart sufix.
    */
   public static String removeDictionarySufix(String name) {
      int i = name.lastIndexOf(SdaiSession.DICTIONARY_NAME_SUFIX);
      if (i > 0) {
         name = name.substring(0, i);
      }
      return name;
   }

   /**
    * It finds model in specified repository.
    * @param name the name of the model
       * @returns model with name (specified name + specified sufix). It returns null if it does not find model with such name.
    */
   public static SdaiModel findModel(String name, SdaiRepository repository) throws jsdai.lang.SdaiException {
      if (repository == null) {
         return null;
      }
      SdaiModel rv = null, model;
      ASdaiModel models = repository.getModels();
      SdaiIterator iterator = models.createIterator();
      while (iterator.next()) {
         model = models.getCurrentMember(iterator);
         if (model.getName().equalsIgnoreCase(name)) {
            rv = model;
            break;
         }
      }
      return rv;
   }

   /**
    * This method need to be enchnced to accept elements like and(c1, c2) and and(c2, c1) as equal.
    * The same is for "or".
    */
   public static jsdai.lang.EEntity findSubstitute(jsdai.lang.EEntity ea) throws jsdai.lang.SdaiException {
      jsdai.dictionary.EDefined_type types[] = new jsdai.dictionary.EDefined_type[20];
      jsdai.lang.EEntity rv = ea;
      jsdai.dictionary.EEntity_definition ed = ea.getInstanceType();
      jsdai.dictionary.AExplicit_attribute attributes = ed.getExplicit_attributes(null);
      boolean f = false;
      jsdai.lang.SdaiIterator i = attributes.createIterator();
      jsdai.dictionary.EExplicit_attribute attribute = null;
      while (i.next()) {
         attribute = attributes.getCurrentMember(i);
         if (attribute.getDomain(null)instanceof EEntity_definition) {
            if (ea.testAttribute(attribute, types) != 0) {
               f = true;
               break;
            }
         }
      }
      jsdai.lang.SdaiModel model = ea.findEntityInstanceSdaiModel();
      jsdai.lang.AEntity entities;
      if (f) {
         entities = new jsdai.lang.AEntity();
         ASdaiModel domain = new ASdaiModel();
         domain.addByIndex(1, model, null);
         EEntity tmp = (EEntity) ea.get_object(attribute);
         tmp.findEntityInstanceUsedin(attribute, domain, entities);
      }
      else {
         entities = (jsdai.lang.AEntity) model.getEntityExtentInstances(ea.getClass());
      }
      i = entities.createIterator();
      while (i.next()) {
         rv = (jsdai.lang.EEntity) entities.getCurrentMemberObject(i);
         if ( ( (jsdai.lang.EEntity) rv).compareValuesBoolean(ea) && (ea != rv)) {
            ea.deleteApplicationInstance();
            return rv;
         }
      }
      return ea;
   }

   /**
    * Finds all subtypes of given entity definition.
    */
   public static void findSubtypes(EEntity_definition aimEntity, ASdaiModel domain, List subtypes) throws SdaiException {
      AEntity_definition directSubtypes = new AEntity_definition();
      CEntity_definition.usedinSupertypes(null, aimEntity, domain, directSubtypes);
      SdaiIterator i = directSubtypes.createIterator();
      while (i.next()) {
         EEntity_definition subtype = directSubtypes.getCurrentMember(i);
         findSubtypes(subtype, domain, subtypes);
         subtypes.add(subtype);
      }
   }

   /**
    * Finds all supertypes of given entity definition.
    */
   public static void findSupertypes(EEntity_definition aimEntity, List supertypes) throws SdaiException {
      AEntity_definition directSupertypes = aimEntity.getSupertypes(null);
      SdaiIterator i = directSupertypes.createIterator();
      while (i.next()) {
         EEntity_definition supertype = directSupertypes.getCurrentMember(i);
         findSupertypes(supertype, supertypes);
         supertypes.add(supertype);
      }
   }

   public static SdaiModel getModelReadOnly(SdaiRepository repository) throws SdaiException {
      SdaiModel rv = null;
      ASdaiModel set = repository.getModels();
      SdaiIterator iter = set.createIterator();
      while (iter.next()) {
         rv = set.getCurrentMember(iter);
         break;
      }
      if (rv.getMode() == SdaiModel.NO_ACCESS) {
         rv.startReadOnlyAccess();
      }
      return rv;
   }

   public static boolean containsEntityDefinition(ArrayList types, EEntity_definition entityDefinition) throws
      SdaiException {
      if (types.contains(entityDefinition)) {
         return true;
      }
      int n = types.size();
      for (int i = 0; i < n; i++) {
         Object o = types.get(i);
         if (o instanceof EEntity_definition) {
            if (isSupertype( (EEntity_definition) o, entityDefinition)) {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * It set all explicit attributes to target to the same values as in source.
    * Target and souce entities must be of the same type.
    */
   public static boolean copyAttributes(EEntity source, EEntity target) throws jsdai.lang.SdaiException {
      if (source == target) {
         return false;
      }
      if (source.getClass() != target.getClass()) {
         return false;
      }
      jsdai.dictionary.EDefined_type types[] = new jsdai.dictionary.EDefined_type[20];
      List attributes = new ArrayList();
      jsdai.dictionary.EEntity_definition ed = source.getInstanceType();
      LangUtils.findAllExplicitAttributesForOneEntity(ed, attributes);
      // jsdai.dictionary.AExplicit_attribute attributes = ed.getExplicit_attributes(null);
      //jsdai.lang.SdaiIterator i = attributes.createIterator();
      jsdai.dictionary.EExplicit_attribute attribute = null;
      for(int index=0,count=attributes.size(); index < count; index++){
         attribute = (jsdai.dictionary.EExplicit_attribute)attributes.get(index);
//         if (attribute.getDomain(null)instanceof EEntity_definition) {
            if (source.testAttribute(attribute, types) != 0) {
               target.set(attribute, source.get_object(attribute), types);
            }
//         }
      }
      return true;
   }

   /**
    * Finds explicit attribute of entity including inherited attributes.
    * If attribute is redeclared it is not included.
    */
   public static EExplicit_attribute findExplicitAttribute(EEntity_definition entity, String attribute) throws
      SdaiException {
      ArrayList list = new ArrayList();
      findExplicitAttributes(entity, list);
      int n = list.size();
      for (int i = 0; i < n; i++) {
         EExplicit_attribute a = (EExplicit_attribute) list.get(i);
         if (a.getName(null).equalsIgnoreCase(attribute)) {
            return a;
         }
      }
      return null;
   }

   public static EEntity createInstanceIfNeeded(SdaiContext context, EEntity_definition instanceType,
           Attribute_and_value_structure[] structure) throws SdaiException {
	   return createInstanceIfNeeded(context.working_model, context.domain, instanceType, structure);
   }
   
   // Utility function
   // This function should create instance of the type passed,
   // only if there are no suitable ones in SdaiModel.
   // If there are good types - reuse them.
   // Method will return either newly created instance of the suitable
   // one which was found in SdaiModel passed
   // --------------------------
   // ASdaiModel domain - domain in which we should search for suitable instances
   // SdaiModel model - model in which we need to create new instances if needed
   // String instanceType - instance type we should work with
   // Attribute_and_value_structure[] structure - pairs of attributes and values we need to base our search
   // EEntity_definition[] aimTypes - ARRAY of types in which we will perform our search
   // or setting of values.
   public static EEntity createInstanceIfNeeded(SdaiModel model, ASdaiModel domain, EEntity_definition instanceType,
                                                Attribute_and_value_structure[] structure) throws SdaiException {
      if (model == null) {
         throw new SdaiException(SdaiException.MO_NVLD);
      }
      if (domain == null) {
         domain = new ASdaiModel();
         domain.addByIndex(1, model, null);
      }

      // Find definition of the type
      if (instanceType == null) {
         throw new SdaiException(SdaiException.VA_NVLD, " type " + instanceType.getName(null) + " not found in schema ");
      }

      ArrayList list = new ArrayList();
      // Supertypes
      LangUtils.findSupertypes(instanceType, list);
      // Add itself
      list.add(instanceType);

      AExplicit_attribute allAttributes = new AExplicit_attribute();
      int index = 1;
      for (int i = 0; i < list.size(); i++) {
         EEntity_definition temp = (EEntity_definition) list.get(i);
         AExplicit_attribute tempAttributes = temp.getExplicit_attributes(null);
         int count = tempAttributes.getMemberCount();
         for (int j = 1; j <= count; j++) {
            allAttributes.addByIndex(1/*index++*/, tempAttributes.getByIndex(j));
         }
      }

      // Find all attributes which we are intrested in
      // and store them in the same order as structure passed.
      // If at least one attribute is not found - throw exception
      AExplicit_attribute attributes = new AExplicit_attribute();
      // this is used for faster processing by usedin operation. Only one, since
      // one usedin is pretty enough to reduce amount of instances significantly
      EExplicit_attribute notSimpleAttribute = null;
      EEntity notSimpleValue = null;

      index = 1;
      SdaiIterator iteratorForAttributes = allAttributes.createIterator();
      l1:for (int i = 0; i < structure.length; i++) {
         iteratorForAttributes.beginning();
         while(iteratorForAttributes.next()) {
            EExplicit_attribute attribute = allAttributes.getCurrentMember(iteratorForAttributes);
            if (attribute == structure[i].attribute) {
               attributes.addByIndex(index++, attribute);
               if (structure[i].value instanceof EEntity) {
                  notSimpleAttribute = attribute;
                  notSimpleValue = (EEntity) structure[i].value;
               }
               continue l1;
            }
         }
         throw new SdaiException(SdaiException.VA_NVLD,
                                 "Attribute " + structure[i].attribute + " not found in " + instanceType);
      }

      EDefined_type[] types = new EDefined_type[30];
      EEntity entity = null;
      AEntity ae = new AEntity();
      // Try to reduce the number of instanes by usedin operations
      if (notSimpleAttribute != null) {
         notSimpleValue.findEntityInstanceUsedin(notSimpleAttribute, domain, ae);
//			if(ae.getMemberCount() == 0){
//				System.out.println(" There's no users for "+notSimpleValue+" through "+notSimpleAttribute);
//			}
      }
      // Usedin hasn't helped - do as usual
      else {
         // Get all instances of the type specified
         ae = domain.getInstances(instanceType);
      }
//		System.err.println(" Instances of that type " + ae.getMemberCount());
      // Check if all attribute constraints are met
      SdaiIterator iterator = ae.createIterator();
      l1:while (iterator.next()) {
         entity = ae.getCurrentMemberEntity(iterator);
//				System.err.println(entity+" is not suitable " + instanceType+" "+entity.isInstanceOf(instanceType));
         if (!isSupertype(instanceType, entity.getInstanceType())) {
            continue; // probably instance of parenty type - ignore, it will not suit anyway
         }

         for (int k = 0; k < structure.length; k++) {
         	// Need to clean here, since it might be left garbage from previous iteration, since test method is not cleaning this aggregate
         	types = new EDefined_type[30];

			// Safety check for derived attributes
			if(((CEntity)entity).testAttributeFast(attributes.getByIndex(k + 1), types) < 0){
				continue l1;
			}
         	
            //attribute.get
            if (entity.testAttribute(attributes.getByIndex(k + 1), types) != 0) {
               if (structure[k].value == null) {
                  continue l1;
               }
               else if (structure[k].value instanceof Boolean) {
                  if (entity.get_boolean(attributes.getByIndex(k + 1)) !=
                      ((Boolean)(structure[k].value)).booleanValue())
                     continue l1;
               }
               else if (structure[k].value instanceof Double) {
                  if (entity.get_double(attributes.getByIndex(k + 1)) !=
                      ((Double)(structure[k].value)).doubleValue())
                     continue l1;
               }
               else if (structure[k].value instanceof Integer) {
                  if (entity.get_int(attributes.getByIndex(k + 1)) !=
                      ((Integer)(structure[k].value)).intValue())
                     continue l1;
               }
               else{
                  Object obj = entity.get_object(attributes.getByIndex(k + 1));
                  // support one instance in aggregate check only
                  if (obj instanceof AEntity) {
                     AEntity agg = (AEntity) obj;
                     if (agg.getMemberCount() == 1) {
                        if (!agg.getByIndexEntity(1).equals(structure[k].value)) {
                           continue l1;
                        }
                        else if ( (structure[k].value instanceof Boolean) ||
                                 (structure[k].value instanceof Double) ||
                                 (structure[k].value instanceof Integer)) {
                           throw new SdaiException(SdaiException.FN_NAVL,
                                                   "Unsupported case - when value is of type other than entity and attribute is aggregate" +
                                                   structure[k].value);
                        }
                     }
                     // need to compare two aggregates
                     else if(structure[k].value instanceof AEntity){
                        AEntity tempAgg = (AEntity) structure[k].value;
                        if(tempAgg.getMemberCount() != agg.getMemberCount())
                           continue l1;
                        for(int i=1;i<=tempAgg.getMemberCount();i++){
                           // Exception might happen if aggregates contain not entities
                           // In such a case this place need to be extended.
                           if(!tempAgg.isMember(agg.getByIndexEntity(i)))
                              continue l1;
                        }
                     }
                     else
                        continue l1;
                  }
                  else if (!obj.equals(structure[k].value))
                     continue l1;
                  if (structure[k].type != types[0])
                     continue l1;
               }
            }
            else {
               if (structure[k].value != null) {
                  continue l1;
               }
            }
         }
	// System.err.println(entity+" is almost suitable " + instanceType+" "+entity.getInstanceType());
         // Suitable instance found
         if (isSupertype(instanceType, entity.getInstanceType())) {
            return entity;
         }
      }
	// System.err.println(" NOTHING IS NOT SUITABLE ");
      // We need to create totally new instance
      types = new EDefined_type[30];
      EEntity instance = model.createEntityInstance(instanceType);
      for (int k = 0; k < structure.length; k++) {
         Object value = structure[k].value;
         if (value == null) {
            continue;
         }

         EExplicit_attribute attr = attributes.getByIndex(k + 1);
         if (structure[k].type == null) {
            EEntity attrDomain = (EEntity) attr.getDomain(null);
            if (attrDomain instanceof EAggregation_type) {
               Aggregate aggr = instance.createAggregate(attr, types);
               boolean addByIndex = (attrDomain instanceof CList_type);
               if (addByIndex) {
                  aggr.addByIndex(1, value, types);
               }
               else {
                  if(value instanceof AEntity){
                     AEntity tempAgg = (AEntity) value;
                     for(int i=1;i<=tempAgg.getMemberCount();i++){
                        // Supporting only entities for now
                        aggr.addUnordered(tempAgg.getByIndexEntity(i), null);
                     }
                  }
                  else
                     aggr.addUnordered(value, types);
               }
            }
            else {
               instance.set(attr, value, types);
            }
         }
         else {
            types[0] = structure[k].type;
            if (value instanceof Boolean) {
               instance.set(attr, ( (Boolean) (value)).booleanValue(), types);
            }
            else if (value instanceof Double) {
               instance.set(attr, ( (Double) (value)).doubleValue(), types);
            }
            else if (value instanceof Integer) {
               instance.set(attr, ( (Integer) (value)).intValue(), types);
            }
            else {
               instance.set(attr, value, types);
            }
         }
      }
      return instance;
   }

   public static class Attribute_and_value_structure {
      public Attribute_and_value_structure(EAttribute attribute, Object value) {
         this.attribute = attribute;
         this.value = value;
      }

      public Attribute_and_value_structure(EAttribute attribute, Object value, EDefined_type type) {
         this.attribute = attribute;
         this.value = value;
         this.type = type;
      }

      public EAttribute attribute = null;
      public Object value = null;
      public EDefined_type type = null;
   }

   /**
    * Finds specified defined types that belongs to specified schema.
    * Deprecated. It's better to use directly ESchema_definition.getDefinedType(defined_typeName)
    * @deprecated
    * @param schema
    * @param defined_typeName name of defined_type. Example "length_measure"
    * @return found defined type or null.
    * @throws SdaiException
    */
   public static EDefined_type getDefinedTypeOfSchema(ESchema_definition schema, String defined_typeName) throws
      SdaiException {
//      EDefined_type dt = null;
//      EDefined_type[] aDefined_type = getDefinedTypesOfSchema(schema);
//      for (int i = 0; i < aDefined_type.length; i++) {
//         dt = (EDefined_type) aDefined_type[i];
//         String name = dt.getName(null).toUpperCase();
//         if (name.equals(defined_typeName.toUpperCase())) {
//            return dt;
//         }
//      }
//      return dt;
       return schema.getDefinedType(defined_typeName);
   }

   public static void setSimpleAttributes(EEntity mainInstance, EEntity_definition exactAIMType, EEntity armInstance,
                                          SdaiContext context, EAttribute[] attributePairs) throws SdaiException {
      setSimpleAttributes( (EMappedARMEntity) mainInstance, exactAIMType, null, (EMappedARMEntity) armInstance, context,
                          attributePairs);
   }

   /** Based on CMappedARMEntity.setSimpleAttributes 2002.06.17 implementation
    * This method sets AIM attributes form ARM, when mapping is simple
    * and contains no path (1:1 mapping).
    * String[] attributePairs should contain even number of elements, like this:
    * AIM attribute No.1, ARM attribute No.1, AIM attribute No.2, ARM attribute No.2 ...
    * String exactAIMType is needed in order to avoid name conflicts
    * It is likely we would need to do the same on ARM later
    */
   public static void setSimpleAttributes(EMappedARMEntity mainInstance, EEntity_definition exactAIMType,
                                          EEntity attributesInstance, EMappedARMEntity armInstance, SdaiContext context,
                                          EAttribute[] attributePairs) throws SdaiException {
      if (attributesInstance == null) {
         attributesInstance = mainInstance.getAimInstance();
      }

      if (attributePairs.length % 2 != 0) {
         throw new SdaiException(SdaiException.VT_NVLD,
                                 "Not valid number of attributes in attributes list - it must be even");
      }
      for (int i = 0; i < attributePairs.length; i += 2) {
         EAttribute aimAttribute = null;
         if (exactAIMType == null) {
            aimAttribute = attributePairs[i];
         }
         else {
            AExplicit_attribute attributes = exactAIMType.getExplicit_attributes(null);
            for (int j = 1; j <= attributes.getMemberCount(); j++) {
               if (attributes.getByIndex(j) == attributePairs[i]) {
                  aimAttribute = attributes.getByIndex(j);
                  break;
               }
            }
            if (aimAttribute == null) {
               throw new SdaiException(SdaiException.VA_NVLD,
                                       " attribute " + exactAIMType.getName(null) + "." + attributePairs[i] +
                                       " not found in schema ");
            }
         }
         EAttribute armAttribute = attributePairs[i + 1];
         setSimpleAttribute(armInstance, attributesInstance, aimAttribute, armAttribute, context);
      }
   }

   protected static boolean isEnumString(EExplicit_attribute attr, ReferencedObject enumTypeRef) throws SdaiException {
      boolean rv = false;
      EEntity attrDomain = attr.getDomain(null);
      if (attrDomain instanceof EDefined_type) {
         EDefined_type defType = (EDefined_type) attrDomain;
         EEntity defTypeDomain = defType.getDomain(null);
         if (defTypeDomain instanceof EEnumeration_type) {
            EEnumeration_type enumType = (EEnumeration_type) defTypeDomain;
            enumTypeRef.value = enumType;
            EExplicit_attribute elementsAttribute = (EExplicit_attribute) enumType.getAttributeDefinition("elements");
            EEntity elementsDomain = elementsAttribute.getDomain(null);
            if (elementsDomain instanceof EAggregation_type) {
               EAggregation_type aggrType = (EAggregation_type) elementsDomain;
               EEntity elementType = aggrType.getElement_type(null);
               if (elementType instanceof EDefined_type) {
                  EDefined_type defElementType = (EDefined_type) elementType;
                  EEntity elementDomain = defElementType.getDomain(null);
                  if (elementDomain instanceof EString_type) {
                     rv = true;
                  }
               }
            }
         }
      }

      return rv;
   }

   public static void setSimpleAttribute(EEntity armInstance, EEntity attributesInstance, EAttribute aimAttribute,
                                         EAttribute armAttribute, SdaiContext context) throws SdaiException {
      jsdai.dictionary.EDefined_type[] types = new jsdai.dictionary.EDefined_type[30];
      setSimpleAttribute(armInstance, attributesInstance, aimAttribute, armAttribute, context, types);
   }

   public static void setSimpleAttribute(EEntity armInstance, EEntity attributesInstance, EAttribute aimAttribute,
                                         EAttribute armAttribute, SdaiContext context, EDefined_type[] types) throws
      SdaiException {
      if (attributesInstance == null) {
         throw new SdaiException(SdaiException.VT_NVLD, "AIM instance is not created yet for " + armInstance.toString());
      }
      int type = armInstance.testAttribute(armAttribute, types);
      EEntity aimAttributeDomain = ( (EExplicit_attribute) aimAttribute).getDomain(null);

      boolean enumString = false;
      ReferencedObject enumType = new ReferencedObject();
      if (type == 2) {
         // check maybe value is enumeration string
         enumString = isEnumString( (EExplicit_attribute) armAttribute, enumType);
         if (enumString) {
            if (aimAttributeDomain instanceof EDefined_type) {
               EEntity aimDomainsDomain = ( (EDefined_type) aimAttributeDomain).getDomain(null);
               if (! (aimDomainsDomain instanceof EEnumeration_type)) {
                  type = 1;
               }
            }
         }
      }

      switch (type) {
         // Unset
         case 0: {
            break;
         }
         // For object (EEntity, Aggregate, String, Binary, BigDecimal, BigInteger);
         case 1: {
            Object value = null;
            if (!enumString) {
               value = armInstance.get_object(armAttribute);
            }
            else {
               int intValue = armInstance.get_int(armAttribute);
               value = LangUtils.convertEnumerationIntToString(intValue, (EEnumeration_type) enumType.value);
            }

            EMappedARMEntity armValue = null;
            if ( (value instanceof AEntity) &&
                ! (aimAttributeDomain instanceof EAggregation_type)) {
               AEntity aggr = (AEntity) value;
               int memberCount = aggr.getMemberCount();
               if (memberCount == 0) {
                  // do not do anything for empty aggregate
                  return;
               }
               else if (memberCount == 1) {
                  armValue = (CMappedARMEntity) aggr.getByIndexEntity(1);
               }
               else if (memberCount > 1) {
                  throw new SdaiException(SdaiException.EI_NVLD,
                                          "Can't map aggregate of few instances to single AIM entity");
               }
            }
            else if (value instanceof EEntity) {
               armValue = (EMappedARMEntity) value;
            }

            if (value instanceof String) {
               ReferencedObject tmp = new ReferencedObject();
               boolean aimEnumString = isEnumString( (EExplicit_attribute) aimAttribute, tmp);
               if (aimEnumString) {
                  int int_val = convertEnumerationStringToInt( (String) value, (EEnumeration_type) tmp.value, context);
                  attributesInstance.set(aimAttribute, int_val, types);
               }
               else {
                  attributesInstance.set(aimAttribute, value, types);
               }
            }
            else if (armValue != null) {
               EEntity aimSubInstance = armValue.getAimInstance();
               if (aimSubInstance == null) {
                  armValue.createAimData(context);
                  aimSubInstance = armValue.getAimInstance();
               }

               if (aimAttributeDomain instanceof EAggregation_type) {
                  ( (CMappedARMEntity) armInstance).addAggregateItemSimple(attributesInstance,
                     (EExplicit_attribute) aimAttribute, aimSubInstance, true, 1);
               }
               else {
                  attributesInstance.set(aimAttribute, aimSubInstance, types);
               }
            }
            else if (value instanceof AEntity) {
               AEntity armValues = (AEntity) armInstance.get_object(armAttribute);
               EEntity aimValue;
               Aggregate aimValues = attributesInstance.createAggregate(aimAttribute, types);
               int index = 1;
               for (int j = 1; j <= armValues.getMemberCount(); j++) {
                  armValue = (EMappedARMEntity) armValues.getByIndexEntity(j);
                  EEntity aimSubInstance = armValue.getAimInstance();
                  if (aimSubInstance == null) {
                     armValue.createAimData(context);
                     aimSubInstance = armValue.getAimInstance();
                  }
                  aimValues.addUnordered(aimSubInstance, null);
               }
            }
            else {
               throw new SdaiException(SdaiException.FN_NAVL,
                                       " type " + value + " is not supported for automatic setting ");
            }
            break;
         }
         // For integer
         case 2: {
            int int_value = armInstance.get_int(armAttribute);
            if (aimAttributeDomain instanceof EDefined_type) {
               EEntity underlyingType = getUnderlyingType( (EDefined_type) aimAttributeDomain);
               if (underlyingType instanceof EString_type) {
                  attributesInstance.set(aimAttribute, String.valueOf(int_value), types);
                  break;
               }
            }
            attributesInstance.set(aimAttribute, armInstance.get_int(armAttribute), types);
            break;
         }
         // For double
         case 3: {
            attributesInstance.set(aimAttribute, armInstance.get_double(armAttribute), types);
            break;
         }
         // For boolean
         case 4: {
            boolean bool_value = armInstance.get_boolean(armAttribute);
            if (aimAttributeDomain instanceof EDefined_type) {
               EEntity underlyingType = getUnderlyingType( (EDefined_type) aimAttributeDomain);
               if (underlyingType instanceof EString_type) {
                  attributesInstance.set(aimAttribute, String.valueOf(bool_value), types);
                  break;
               }
            }
            attributesInstance.set(aimAttribute, bool_value, types);
            break;
         }
         default: {
            throw new SdaiException(SdaiException.FN_NAVL, " type " + type + " is not supported for automatic setting ");
         }
      }
   }

   /**
    * This method checks whether there are instances which uses this instance.
    * If not - delete it.
    * In the future it should be extended to check whether this instance has any remaining
    * AIM-ARM relationships and delete only if there's no such ones.
    * @param domain - domain in which to search users of the instance passed.
    * @param instance - candidate to be deleted.
    * @throws SdaiException
    */
   public static void deleteInstanceIfUnused(ASdaiModel domain, EEntity instance) throws SdaiException {
      deleteInstanceIfUsageCountLessThan(domain, instance, 1);
   }

   public static boolean deleteInstanceIfUsageCountLessThan(ASdaiModel domain, EEntity instance, int count) throws
      SdaiException {
      AEntity result = new AEntity();
      instance.findEntityInstanceUsers(domain, result);
      if (result.getMemberCount() < count) {
         instance.deleteApplicationInstance();
         return true;
      }
      return false;
   }

   /**
    * This method checks whether there are instances which uses this instance.
    * If not - delete it.
    * In the future it should be extended to check whether this instance has any remaining
    * AIM-ARM relationships and delete only if there's no such ones.
    * It also goes for attributes of instance and tries to perform the same task recursivly
    * @param domain - domain in which to search users of the instance passed.
    * @param instance - candidate to be deleted.
    * @throws SdaiException
    */
   public static boolean deleteInstanceIfUnusedRecursive(ASdaiModel domain, EEntity instance) throws
      SdaiException {
      AEntity result = new AEntity();
      instance.findEntityInstanceUsers(domain, result);
      if (result.getMemberCount() < 1) {
	AEntity aggr = new AEntity();
	findRelated(aggr, instance);
	for(int i=1;i<=aggr.getMemberCount();i++){
		EEntity instanceTemp = aggr.getByIndexEntity(i);
		AEntity resultTemp = new AEntity();
		instanceTemp.findEntityInstanceUsers(domain, resultTemp);
		if (result.getMemberCount() < 2) 
			instanceTemp.deleteApplicationInstance();
	}
         instance.deleteApplicationInstance();
         return true;
      }
      return false;
   }


   public static EEntity getUnderlyingType(EDefined_type type) throws SdaiException {
      EEntity domain = type.getDomain(null);
      if (domain instanceof EDefined_type) {
         return getUnderlyingType( (EDefined_type) domain);
      }
      return domain;
   }

	public static Set findAttributes(EEntity_definition def, Set rdAttrSet)
		throws SdaiException
	{
		Set attrSet = new HashSet();
		if (rdAttrSet == null)
			rdAttrSet = new HashSet();

		AEntity_definition aSuper = def.getSupertypes(null);
		for (SdaiIterator i = aSuper.createIterator(); i.next();)
			attrSet.addAll(findAttributes(aSuper.getCurrentMember(i), rdAttrSet));

		AAttribute aAttr = def.getAttributes(null, null);
		for (SdaiIterator i = aAttr.createIterator(); i.next();) {
			EAttribute attr = aAttr.getCurrentMember(i);
			if (rdAttrSet.contains(attr))
				continue;

			if (attr instanceof EInverse_attribute || attr instanceof EView_attribute)
				continue;

			EAttribute rdAttr = null;
			if (attr instanceof EExplicit_attribute) {
				EExplicit_attribute eAttr = (EExplicit_attribute) attr;
				if (eAttr.testRedeclaring(null))
					rdAttr = eAttr.getRedeclaring(null);
			}
			else if (attr instanceof EDerived_attribute) {
				EDerived_attribute dAttr = (EDerived_attribute) attr;
				if (dAttr.testRedeclaring(null))
					rdAttr = (EAttribute) dAttr.getRedeclaring(null);
			}

			if (rdAttr != null)
				rdAttrSet.add(rdAttr);

			attrSet.add(attr);
		}

		for (Iterator i = rdAttrSet.iterator(); i.hasNext();)
			attrSet.remove(i.next());

		return attrSet;
	}

	private static EDefined_type[] DEFINED_TYPE = new EDefined_type[3];
	
	public static void findRelated(AEntity aggr, EEntity ent)
		throws SdaiException
	{
		Set attrSet = findAttributes(ent.getInstanceType(), null);
		for (Iterator i = attrSet.iterator(); i.hasNext();) {
			EAttribute attribute = (EAttribute) i.next();
			if (attribute instanceof EDerived_attribute)
				continue;

			if (1 == ent.testAttribute(attribute, DEFINED_TYPE)) {
				Object attr = ent.get_object(attribute);
				if (attr instanceof AEntity) {
					AEntity aEnt = (AEntity) attr;
					for (SdaiIterator j = aEnt.createIterator(); j.next();) {
						EEntity e = aEnt.getCurrentMemberEntity(j);
						if (!aggr.isMember(e)) {
							aggr.addUnordered(e);
							findRelated(aggr, e);
						}
					}
				}
				else if (attr instanceof EEntity) {
					EEntity e = (EEntity) attr;
					if (!aggr.isMember(e)) {
						aggr.addUnordered(e);
						findRelated(aggr, e);
					}
				}
			}
		}
	}


}
