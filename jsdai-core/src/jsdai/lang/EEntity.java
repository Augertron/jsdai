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

package jsdai.lang;

import jsdai.dictionary.*;
import jsdai.mapping.*;

/** Implicit supertype for all dictionary and application entity types.
 * @see AEntity
 * @see <a href="../dictionary/entity_definition.html">jsdai.dictionary.EEntity_definition</a>
 * @see "ISO 10303-22::9.4.2 entity_instance"
 * @see "ISO 10303-22::9.4.3 application_instance"
 * @see "ISO 10303-22::9.4.5 dictionary_instance"
 */
public interface EEntity extends SdaiEventSource {
/**
 * The mode to apply no additional restrictions on the data returned by
 * {@link #findEntityMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping mappings, int mode) findEntityMappings},
 * {@link #getMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute},
 * {@link #getMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute},
 */
	int NO_RESTRICTIONS = 0;
/**
 * The mode to return only mapping of the most specific and not abstract entities.
 * It is used in method
 * {@link #findEntityMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping mappings, int mode) findEntityMappings}.
 */
	int MOST_SPECIFC_ENTITY = 1;
/**
 *  Tn addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 */
	int MANDATORY_ATTRIBUTES_SET = 2;

/**
 * Returns the value (of type Object) of the specified attribute.
 * This method implements a specialized late-binding version of the "Get Attribute" operation
 * for the case when the attribute is of some object type like EEntity, Aggregate,
 * String, Binary, BigDecimal, or BigInteger (BigDecimal and BigInteger are not
 * supported in this version of JSDAI).
 * <P>
 * All the early-binding and late-binding methods implementing "Get Attribute" operation behave
 * as follows. In the case when the value of the attribute is unset, SdaiException
 * VA_NSET is thrown. If in the express specification the attribute is defined as
 * OPTIONAL, the user should first invoke the testAttribute method to check whether
 * the attribute is set (has value) and only after this invoke appropriate method
 * implementing "Get Attribute" operation. In the case when in the express the attribute is
 * not defined to be OPTIONAL, the user can directly apply one of such methods.
 * Thus, SdaiException VA_NSET is thrown only in the case when population of
 * instances is invalid. This leads to application programs with a clear structure.
 * <P>
 * In the case when the attribute is of EEntity type, the behaviour of the
 * methods implementing "Get Attribute" depends on which SdaiModel and
 * SdaiRepository the referenced instance belongs to. If the SdaiModel of the
 * instance is already in read-only or read-write access mode, e.g. because
 * this instance and the referencing one are in the same SdaiModel, then the
 * instance is returned immediately. If the SdaiModel has access mode unset,
 * but belongs to an open SdaiRepository, then first the read-only access
 * for this model is started and only then the instance is returned. If, however,
 * the SdaiRepository is closed, SdaiException RP_NOPN is thrown.

 * <p> The method throws SdaiException FN_NAVL if attribute whose value is asked
 * is inverse or derived. In the former case, {@link #get_inverse get_inverse}
 * method shall be used.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EAttribute attr = vect.getAttributeDefinition("orientation");
 *    // access the attribute as a general entity, that is, EEntity
 *    EEntity inst = (EEntity)vect.get_object(attr);
 *    // access the attribute as a specific entity
 *    EDirection dir = (EDirection)vect.get_object(attr);
 *    attr = vect.getAttributeDefinition("name");
 *    // access a String attribute
 *    String name = (String)vect.get_object(attr);</pre></TT>


 * @param attribute the attribute which value is needed.
 * @return value (of type Object) of the attribute.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_int get_int
 * @see #get_double get_double
 * @see #get_boolean get_boolean
 * @see #get_inverse get_inverse
 * @see #getAttributeDefinition getAttributeDefinition
 * @see #testAttribute testAttribute
 * @see "10303-22::10.10.1 Get attribute"
 */
	Object get_object(EAttribute attribute) throws SdaiException;

/**
 * Returns the value (of type int) of the specified attribute.
 * This method is a specialization of the general "Get Attribute" operation
 * for the case when the attribute is of an integer type.
 * <p> The method throws SdaiException AT_NVLD if the passed
 * attribute is not from this entity, SdaiException VT_NVLD if the attribute
 * type is different than integer, and SdaiException FN_NAVL if attribute
 * whose value is asked is a derived one.
 * For more details see method {@link #get_object(EAttribute) get_object}.

 * <P><B>Example:</B>
 * <P><TT><pre>    ACalendar_date date_list = ...;
 *    ECalendar_date date = date_list.getByIndex(1);
 *    EAttribute attr = date.getAttributeDefinition("year_component");
 *    int year = date.get_int(attr);</TT></pre>

 * @param attribute the attribute which value is needed.
 * @return value (of type int) of the attribute.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_double get_double
 * @see #get_boolean get_boolean
 * @see #getAttributeDefinition getAttributeDefinition
 * @see #testAttribute testAttribute
 * @see "ISO 10303-22::10.10.1 Get attribute"
 */
	int get_int(EAttribute attribute) throws SdaiException;

/**
 * Returns the value (of type boolean) of the specified attribute.
 * This method is a specialization of the general "Get Attribute" operation
 * for the case when the attribute is of the boolean type.
 * <p> The method throws SdaiException AT_NVLD if the passed
 * attribute is not from this entity, SdaiException VT_NVLD if the attribute
 * type is different than boolean, and SdaiException FN_NAVL if attribute
 * whose value is asked is a derived one.
 * For more details see method {@link #get_object(EAttribute) get_object}.

 * <P><B>Example:</B>
 * <P><TT><pre>    ETrimmed_curve curve = ...;
 *    EAttribute attr = curve.getAttributeDefinition("sense_agreement");
 *    boolean agreement = curve.get_boolean(attr);</TT></pre>

 * @param attribute the attribute which value is needed.
 * @return value (of type boolean) of the attribute.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_int get_int
 * @see #get_double get_double
 * @see #getAttributeDefinition getAttributeDefinition
 * @see #testAttribute testAttribute
 * @see "ISO 10303-22::10.10.1 Get attribute"
 */
	boolean get_boolean(EAttribute attribute) throws SdaiException;

/**
 * Returns the value (of type double) of the specified attribute.
 * This method is a specialization of the general "Get Attribute" operation
 * for the case when the attribute is of the double type.
 * <p> The method throws SdaiException AT_NVLD if the passed
 * attribute is not from this entity, SdaiException VT_NVLD if the attribute
 * type is different than double, and SdaiException FN_NAVL if attribute
 * whose value is asked is a derived one.
 * For more details see method {@link #get_object(EAttribute) get_object}.

 * <P><B>Example:</B>
 * <P><TT><pre>    Vector vector_list = ...;
 *    EVector vect = vector_list.getByIndex(1);
 *    EAttribute attr = vect.getAttributeDefinition("magnitude");
 *    double magn = vect.get_double(attr);</TT></pre>

 * @param attribute the attribute which value is needed.
 * @return value (of type double) of the attribute.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_int get_int
 * @see #get_boolean get_boolean
 * @see #getAttributeDefinition getAttributeDefinition
 * @see #testAttribute testAttribute
 * @see "ISO 10303-22::10.10.1 Get attribute"
 */
	double get_double(EAttribute attribute) throws SdaiException;

/**
 * Returns the value (of type aggregate of entities) of the specified inverse attribute.
 * This method implements a specialized late-binding version of the "Get Attribute" operation
 * for the inverse attribute case.
 * The resulting aggregate consists of entity instances which are of type
 * specified in the definition of the inverse attribute and reference the current instance
 * by the attribute (called inverted attribute) also specified there.
 * The search area for such entity instances is restricted to those <code>SdaiModel</code>s
 * which belong to the set given by the second parameter.
 * In the case when the second parameter is null, the search is restricted
 * to the model which is the owner of the current instance (can be found
 * using {@link #findEntityInstanceSdaiModel findEntityInstanceSdaiModel}).
 * <p> The method throws SdaiException AT_NVLD if the passed
 * attribute is either not inverse one or not from this entity.

 * <P><B>Example:</B>
 * <P><TT><pre>    EComposite_curve_segment segment = ...;
 *    ASdaiModel domain = ...;
 *    EInverse_attribute inv_attr = (EInverse_attribute)segment.getAttributeDefinition("using_curves");
 *    AEntity inv_aggr = segment.get_inverse(inv_attr, domain);</pre></TT>

 * @param attribute the inverse attribute which value is needed.
 * @param domain the list of <code>SdaiModel</code>s that defines the
 * area where to search for entity instances referencing the current
 * entity instance in the way specified by the given inverse attribute.
 * @return value (of type non-persistent list of entities) of the attribute.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_int get_int
 * @see #get_double get_double
 * @see #get_boolean get_boolean
 * @see #getAttributeDefinition getAttributeDefinition
 * @see "10303-22::10.10.1 Get attribute"
 */
	AEntity get_inverse(EInverse_attribute attribute, ASdaiModel domain)
			throws SdaiException;

/**
 * Checks if the specified attribute has a value and if so, then
 * returns an indicator showing the type of the value.
 * More precisely, this method fulfills the following three purposes:
 * <OL>
 * <LI>checks whether the attribute has a value or is unset.
 * If unset, the return value is 0.</LI>
 * <LI>retrieves the type of the value (if set). The numbering of the types is as follows:
 * <ul><li> 1 for object (EEntity, Aggregate, String, Binary,
 * BigDecimal, BigInteger);
 * <li> 2 for integer;
 * <li> 3 for double;
 * <li> 4 for boolean.</ul>
 * The case of return value 2 encompasses the cases when the attribute value is of
 * EXPRESS type INTEGER, ENUMERATION and LOGICAL.
 * For the same value each of the methods <code>testAttribute</code>, 
 * {@link Aggregate#testByIndex Aggregate.testByIndex} and 
 * {@link Aggregate#testCurrentMember Aggregate.testCurrentMember} 
 * returns the same type indicator.
 * </LI>
 * <LI>retrieves the select path corresponding to the actual value of the
 * attribute in the case when this attribute is of the select type.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type
 * (as defined in Technical corrigendum of ISO 10303-21:11.1.8).
 * This sequence, in fact, defines some path in some rooted acyclic graph with
 * vertices corresponding to select data types, defined data types, and
 * specific data types (integer, double, String, enumeration, aggregation etc.).
 * The path in this graph is from the root to some its leaf. The leaves of the
 * graph describe specific data types. This graph, in general, is not a tree because select data types
 * can be nested, and two such types may have the same selection item.
 * Fortunately, in most cases we have a simple(non-nested) select type,
 * and in these cases the graph reduces to a tree.
 * The array of defined types needed to store the select path must be
 * allocated before invocation of the method. If this array is too small,
 * then SdaiException SY_ERR with some adjoined explanatory text is thrown.</LI></OL>
 * The attribute to be tested is given by the first method's parameter,
 * so this is a late-binding method. There exist for attribute testing also early-binding
 * methods in which the attribute name constitutes a part of the method name.
 * All these methods are various implementations of the general Test Attribute
 * operation (ISO 10303-22:10.10.2).

 * <P><B>Example 1</B> showing how to use testAttribute as a late binding operation:
 * <P><TT><pre>    EDefined_type [] selection = new EDefined_type[3];
 *    EAttribute attr = ...;
 *    EEntity instance= ...;
 *    int type = instance.testAttribute(attr, selection);
 *    switch (type) {
 *    case 0: // unset
 *        // skip
 *        break;
 *    case 1: // object
 *        Object oValue = instance.get_object(attr);
 *        if (oValue instanceof EEntity) {
 *            ...
 *        } else if (oValue instanceof Aggregate) {
 *            ...
 *        } else if (oValue instanceof String) {
 *            ...
 *        }
 *        break;
 *    case 2: // integer
 *        int iValue = instance.get_int(attr);
 *        break;
 *    case 3: // double
 *        double rValue = instance.get_double(attr);
 *        EDefined_type areaMeasureType = ...;
 *        EDefined_type lengthMeasureType = ...;
 *        if (selection(0) == areaMeasureType) {
 *            double area = rValue;
 *        } else if (selection(0) == lengthMeasureType) {
 *            double length = rValue;
 *        break;
 *    case 4: // boolean
 *        boolean bValue = instance.get_boolean(attr);
 *        break;
 *    }</TT></pre>

 * <P><B>Example 2</B> illustrating early binding usage of the "Test Attribute" operation:
 * <P><TT><pre>    EMeasure_with_unit instance = ...;
 *    int iSelection = instance.testValue_component(null);
 *    switch (iSelection) {
 *    case EMeasure_with_unit.sValue_componentArea_measure:
 *        double area = instance.getValue_component((EArea_measure) null);
 *        break;
 *    case EMeasure_with_unit.sValue_componentLength_measure:
 *        double length = instance.getValue_component((ELength_measure) null);
 *        break;
 *    ...;
 *    }</TT></pre>

 * @param attribute the attribute which value is tested.
 * @param select the array of defined types used to store the select path
 * corresponding to the actual value of the attribute.
 * @return type of the value (0 if the attribute has no value).
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_int get_int
 * @see #get_double get_double
 * @see #get_boolean get_boolean
 * @see #getAttributeDefinition getAttributeDefinition
 * @see "ISO 10303-22::10.10.2 Test attribute"
 */
	int testAttribute(EAttribute attribute, EDefined_type[] select) throws SdaiException;

/**
 * Returns a specific instance of the entity "attribute" defined in "SDAI_dictionary_schema",
 * describing EXPRESS schemas on the meta level.
 * While submitting the parameter to the method the following rule shall be considered. 
 * If the required attribute is unique within the entity data type of the current instance, 
 * then the parameter is the name of that attribute.
 * Otherwise, the parameter is of the form "en.an" where 'an' and 'en' denote 
 * attribute name and, respectively, the name of the simple entity 
 * that is the owner of the required attribute.
 * For example, complex entity definition of the instance 
 * #43296=(CHARACTERIZED_OBJECT('spotting point','design feature')FEATURE_DEFINITION()GENERAL_FEATURE()INSTANCED_FEATURE()SHAPE_ASPECT('RPPunkt_H_1480_L_9841K0_813_111','H_1480_L_984',#43307,.U.))
 * has two attributes with the name 'description': 
 * the owner of one of them is simple entity data type 'characterized_object' and 
 * the owner of another is 'shape_aspect'.
 * To get definition of the first attribute, method shall be 
 * invoked with the string "characterized_object.description".
 * To get the second attribute, "shape_aspect.description" shall be supplied.

 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EAttribute attr = vect.getAttributeDefinition("magnitude");</TT></pre>
 * <P>

 * @param attributeName the name of the entity attribute, possibly, with the simple entity name as a prefix.
 * @return instance of the entity describing entity attribute.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #get_object get_object
 * @see #get_int get_int
 * @see #get_double get_double
 * @see #get_boolean get_boolean
 * @see #get_inverse get_inverse
 * @see #findEntityInstanceUsedin findEntityInstanceUsedin
 * @see #getAttributeValueBound getAttributeValueBound
 * @see #set set
 * @see #unsetAttributeValue unsetAttributeValue
 * @see #createAggregate createAggregate
 */
	EAttribute getAttributeDefinition(String attributeName) throws SdaiException;

/**
 * Returns <code>SdaiModel</code> this entity instance belongs to. Every entity instance
 * does belong to exactly one <code>SdaiModel</code>. The entity instances of an <code>SdaiModel</code>
 * are only available when this model is either in read-only or read-write access mode.
 * If the access mode is unset, all the entity instances of this <code>SdaiModel</code>
 * become forbidden for the applications. In fact, invocation of most methods
 * for such an instance causes SdaiException EI_NEXS to be thrown.
 * <p>
 * Don't be irritated by the word "find" in the name of the method. This method
 * is fast because JSDAI has this information available explicitly.

 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity inst = ...;
 *    SdaiModel owner = inst.findEntityInstanceSdaiModel();</TT></pre>
 * <P>

 * @return <code>SdaiModel</code> that contains this entity instance.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @see "ISO 10303-22::10.10.3 Find entity instance SDAI-model"
 */
	SdaiModel findEntityInstanceSdaiModel() throws SdaiException;

/**
 * Returns the instance of <code>EEntity_definition</code> which represents
 * the type of this entity instance in "SDAI_dictionary_schema".

 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity inst = ...;
 *    EEntity_definition def = inst.getInstanceType();</TT></pre>
 * <P>

 * @return entity definition of this entity instance.
 * @see "ISO 10303-22::10.10.4 Get instance type"
 */
	EEntity_definition getInstanceType() throws SdaiException;

/**
 * Checks if the type (described by EEntity_definition) of this entity
 * instance coincides with the type given by method's parameter.
 * The answer is obtained, contrary to
 * {@link #isInstanceOf(Class) isInstanceOf(Class)}, solely on the
 * basis of "SDAI_dictionary_schema" information.
 * If null is passed to the method's parameter,
 * then SdaiException ED_NDEF is thrown.

 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity inst = ...;
 *    EEntity another_inst = ...;
 *    EEntity_definition def = another_inst.getInstanceType();
 *    boolean is_of_def = inst.isInstanceOf(def);</TT></pre>
 * <P>

 * @param type the given type (instance of EEntity_definition).
 * @return true if the type (EEntity_definition) of the instance coincides
 * with the given type; false, otherwise.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isInstanceOf(Class) isInstanceOf(Class)
 * @see #isKindOf(EEntity_definition) isKindOf(EEntity_definition)
 * @see #isKindOf(Class) isKindOf(Class)
 * @see "ISO 10303-22::10.10.5 Is instance of"
 */
	boolean isInstanceOf(EEntity_definition type) throws SdaiException;

/**
 * Checks if <code>java.lang.Class</code> object of this entity instance
 * coincides with the given <code>java.lang.Class</code> object which describes java class
 * representing some other entity instance. Contrary to
 * {@link #isInstanceOf(EEntity_definition) isInstanceOf(EEntity_definition)} method,
 * no "SDAI_dictionary_schema" information is used.

 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity inst = ...;
 *    EEntity another_inst = ...;
 *    Class cl = another_inst.getClass();
 *    boolean is_of_cl = inst.isInstanceOf(cl);</TT></pre>
 * <P>

 * @param type <code>java.lang.Class</code> object which describes java class
 * representing some entity instance.
 * @return true if <code>java.lang.Class</code> object of this entity instance coincides
 * with the given <code>java.lang.Class</code> object; false, otherwise.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @see #isInstanceOf(EEntity_definition) isInstanceOf(EEntity_definition)
 * @see #isKindOf(EEntity_definition) isKindOf(EEntity_definition)
 * @see #isKindOf(Class) isKindOf(Class)
 * @see "ISO 10303-22::10.10.5 Is instance of"
 */
	boolean isInstanceOf(Class type) throws SdaiException;

/**
 * Checks if the type (described by EEntity_definition) of this entity
 * instance coincides with the given type (instance of EEntity_definition)
 * or is a subtype of it.
 * The answer is obtained, contrary to
 * {@link #isKindOf(Class) isKindOf(Class)} method, solely on the
 * basis of "SDAI_dictionary_schema" information.
 * If null is passed to the method's parameter,
 * then SdaiException ED_NDEF is thrown.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    ERepresentation_item item = ...;
 *    EEntity_definition def = item.getInstanceType();
 *    boolean is_vect_an_item = vect.isKindOf(def);</TT></pre>
 * <P>

 * @param type the given type (instance of EEntity_definition).
 * @return true if the type (EEntity_definition) of this instance coincides
 * with the given type or is a subtype of it; false, otherwise.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @see #isInstanceOf(EEntity_definition) isInstanceOf(EEntity_definition)
 * @see #isInstanceOf(Class) isInstanceOf(Class)
 * @see #isKindOf(Class) isKindOf(Class)
 * @see "ISO 10303-22::10.10.6 Is kind of"
 */
	boolean isKindOf(EEntity_definition type) throws SdaiException;

/**
 * Checks if <code>java.lang.Class</code> object of this entity instance
 * coincides with the given <code>java.lang.Class</code> object, which describes java class
 * representing some other entity instance, or is a <code>java.lang.Class</code> object for
 * the java class which is a subclass of the given class. Contrary to
 * {@link #isKindOf(EEntity_definition) isKindOf(EEntity_definition)} method,
 * no "SDAI_dictionary_schema" information is used.
 * If null value is passed to the method's
 * parameter, then SdaiException VA_NSET is thrown.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    boolean is_vect_an_item = vect.isKindOf(ERepresentation_item.class);</TT></pre>
 * <P>

 * @param type <code>java.lang.Class</code> object which describes java class
 * representing some entity instance.
 * @return true if <code>java.lang.Class</code> object of this entity instance coincides
 * with the given <code>java.lang.Class</code> object or is a <code>java.lang.Class</code> object for
 * the java class which is a subclass of the given class; false, otherwise.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isInstanceOf(EEntity_definition) isInstanceOf(EEntity_definition)
 * @see #isInstanceOf(Class) isInstanceOf(Class)
 * @see #isKindOf(EEntity_definition) isKindOf(EEntity_definition)
 * @see "ISO 10303-22::10.10.6 Is kind of"
 */
	boolean isKindOf(Class type) throws SdaiException;

/**
 * Searches for entity instances which reference the current instance.
 * The search area for such entity instances is restricted to those <code>SdaiModel</code>s
 * which belong to the set given by the first method's parameter.
 * In the case when the first parameter is null, the search is restricted
 * to the model which is the owner of the current instance (can be found
 * using {@link #findEntityInstanceSdaiModel findEntityInstanceSdaiModel}).
 * If the value of the first parameter is an empty array of type <code>ASdaiModel</code>, 
 * then no restriction is imposed on the search area, that is, referencing 
 * instances from all <code>SdaiModel</code>s, which have either read-only or read-write 
 * access, are considered as being relevant.
 * Referencing instances are stored into a non-persistent list passed
 * to the method through the second parameter. Usually this
 * list is created with operator <TT>new</TT>. A referencing instance is put into
 * this list as many times as it references the current instance.
 * If null value is passed to the method's second
 * parameter, then SdaiException AI_NEXS is thrown.

 * <P><B>Example:</B>
 * <P><TT><pre>    EProduct pro = ...;
 *    EDirection dir = ...;
 *    AEntity general_list = new AEntity();
 *    pro.findEntityInstanceUsers(null, general_list);
 *    ASdaiModel domain = ...;
 *    ADirection dir_list = new ADirection();
 *    dir.findEntityInstanceUsers(domain, dir_list);</TT></pre>
 * <P>

 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area where to search for entity instances referencing the current
 * entity instance.
 * @param result the submitted non-persistent list to which all entity
 * instances referencing this instance are appended.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #findEntityInstanceUsedin findEntityInstanceUsedin
 * @see #findInstanceRoles findInstanceRoles
 * @see "ISO 10303-22::10.10.8 Find entity instance users"
 */
	void findEntityInstanceUsers(ASdaiModel domain, AEntity result)
		throws SdaiException;

/**
 * Gives the count of entity instances which reference the current instance.
 * The search area for such entity instances is restricted to those <code>SdaiModel</code>s
 * which belong to the set given by the method's parameter.
 * In the case where this parameter is null, the search is restricted
 * to the model which is the owner of the current instance (can be found
 * using {@link #findEntityInstanceSdaiModel findEntityInstanceSdaiModel}).
 * If the value of the parameter is an empty array of type <code>ASdaiModel</code>, 
 * then no restriction is imposed on the search area, that is, referencing 
 * instances from all <code>SdaiModel</code>s, which have either read-only 
 * or read-write access, are considered as being relevant.
 * A referencing instance is counted as many times as it references 
 * the current instance.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area where to search for entity instances referencing the current
 * entity instance.
 * @return the count of entity instances which reference the current instance.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #findEntityInstanceUsers findEntityInstanceUsers
 */
	int findEntityInstanceUserCount(ASdaiModel domain) throws SdaiException;

/**
 * Searches for entity instances which reference the current instance
 * in the role specified by an instance of EAttribute (first parameter).
 * The search area for such entity instances is restricted to those <code>SdaiModel</code>s
 * which belong to the set given by the second method's parameter.
 * In the case when the second parameter is null, the search is restricted
 * to the model which is the owner of the current instance (can be found
 * using {@link #findEntityInstanceSdaiModel findEntityInstanceSdaiModel} method).
 * If the value of the second parameter is an empty array of type <code>ASdaiModel</code>, 
 * then no restriction is imposed on the search area, that is, referencing 
 * instances from all <code>SdaiModel</code>s, which have either read-only or read-write 
 * access, are considered as being relevant. 
 * Referencing instances are stored into a non-persistent list submitted
 * to the method through the third parameter. Usually this
 * list is created with operator <TT>new</TT>. A referencing instance is put into
 * this list as many times as it references the current instance
 * in the specified role.
 * The list of cases when <code>SdaiException</code> is thrown contains the following items:
 * <ul><li> AT_NDEF if role is not submitted;
 * <li> AI_NEXS if non-persistent list is not submitted;
 * <li> AI_NVLD if aggregate passed to the method's third parameter is not
 * a non-persistent list.</ul>

 * <p>
 * In EXPRESS, a relation between two entities is defined and stored
 * in only one entity of the pair, but it always exists in both directions.
 * For some attributes of one entity, an inverse attribute on the other
 * entity is defined. This is usually done to restrict the cardinality
 * of the relation. But independently of the explicit specification of an
 * inverse attribute there is an implicit inverse relation for every
 * explicit one. The current JSDAI implementation of findEntityInstanceUsedin
 * allows fast access to every inverse attribute.
 * This method is fast because JSDAI explicitly stores all inverse pointers.

 * <P><B>Example 1</B> illustrating late binding alternative:
 * <P><TT><pre>    EProduct product = ...;
 *    EAttribute attrib = CProduct_definition_formation.attributeOf_product(null);
 *    AProduct_definition_formation formations = new AProduct_definition_formation();
 *    product.findEntityInstanceUsedin(attrib, null, formations);</TT></pre>
 * <P><B>Example 2</B> illustrating early binding alternative:
 * <P><TT><pre>    EProduct product = ...;
 *    AProduct_definition_formation formations = new AProduct_definition_formation();
 *    CProduct_definition_formation.usedinOf_product(null, product, null, formations);</TT></pre>
 * <P>
 
 * @param role the attribute by which this entity instance is accessed.
 * Note that any attribute always belongs to exactly one entity definition.
 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area where to search for entity instances referencing the current
 * entity instance in the specified role.
 * @param result the submitted non-persistent list to which all entity
 * instances referencing this instance in the specified role (first parameter)
 * are appended.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #findEntityInstanceUsers findEntityInstanceUsers
 * @see #findInstanceRoles findInstanceRoles
 * @see "ISO 10303-22::10.10.9 Find entity instance usedin"
 */
	void findEntityInstanceUsedin(EAttribute role, ASdaiModel domain, AEntity result)
		throws SdaiException;

/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.

 * @param attribute the attribute for which value bound is to be computed.
 * @return value of population dependent bound for double, string or binary
 * type for the value of the specified attribute.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.10.10 Get attribute value bound"
 */
	int getAttributeValueBound(EAttribute attribute) throws SdaiException;

/**
 * Searches for attributes by which other entity instances reference the current instance.
 * The search area for such attributes is restricted to
 * entity instances in those <code>SdaiModel</code>s
 * which belong to the set given by the first method's parameter.
 * In the case when the first parameter is null, the search is restricted
 * to the model which is the owner of the current instance (can be found
 * using {@link #findEntityInstanceSdaiModel findEntityInstanceSdaiModel} method).
 * Referencing attributes are stored into a non-persistent list submitted
 * to the method through the second parameter. Usually this
 * list is created with operator <TT>new</TT>.
 * If null value is passed to the method's second
 * parameter, then SdaiException AI_NEXS is thrown.
 * <p> This method bears some similarity to <code>findEntityInstanceUsers</code>
 * method. However, the result is not the referencing instances but
 * the list containing entity attributes by which the current instance
 * from other instances is referenced. Each attribute is represented
 * by an instance of the class <code>CAttribute</code> of the SDAI dictionary.
 * Having an instance of <code>CAttribute</code> it is possible to get
 * entity definition containing it by simply applying
 * <code>getParent_entity</code> method.

 * <P><B>Example:</B>
 * <P><TT><pre>    EProduct pro = ...;
 *    EDirection dir = ...;
 *    AEntity general_list = new AEntity();
 *    pro.findInstanceRoles(null, general_list);
 *    ASdaiModel domain = ...;
 *    ADirection dir_list = new ADirection();
 *    dir.findInstanceRoles(domain, dir_list);</TT></pre>
 * <P>

 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area where to search for attributes by which other entity instances
 * reference the current instance.
 * @param result the submitted non-persistent list to which all
 * attributes that reference this entity instance are appended.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #findEntityInstanceUsers findEntityInstanceUsers
 * @see #findEntityInstanceUsedin findEntityInstanceUsedin
 * @see "ISO 10303-22::10.10.11 Find instance roles"
 */
	void findInstanceRoles(ASdaiModel domain, AAttribute result)
		throws SdaiException;

/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.

 * @param result the submitted non-persistent list to which all
 * named types of which this entity instance is a member are appended.
 * @param schema defines the scope where to search for the named types 
 * for which this instance is a member of. If the value is <code>null</code>, then 
 * the underlying schema of the model to which this instance belongs is taken.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.10.12 Find instance data types"
 */
	void findInstanceDataTypes(ANamed_type result, ESchema_definition schema) throws SdaiException;

/**
 * Creates a copy of this instance in the specified <code>SdaiModel</code>.
 * All attribute values from this instance are copied to the new one.
 * If some attribute is of aggregation type, then in the new instance
 * for this attribute a new aggregate is created and either filled
 * with values taken from this instance (if the aggregation type
 * describes non-nested aggregate) or this rule is applied recursively
 * (if the aggregation type describes nested aggregate).
 * If this and new instances belong to the same repository,
 * then persistent labels for these instances will be different.
 * <p> A target <code>SdaiModel</code> to which the current instance is copied
 * must be in read-write access mode. If, however, no target model is submitted
 * to the method (through its parameter), then <code>SdaiException</code> MO_NEXS is thrown.
 * This method is applicable to instances of entities which are encountered
 * in the EXPRESS schema whose definition is underlying for the target model.
 * Otherwise, that is, if entity is not known for this schema,
 * <code>SdaiException</code> ED_NVLD is thrown.

 * @param target_model the <code>SdaiModel</code> this application instance to be copied to.
 * @return the new copy of this application instance.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.11.1 Copy application instance"
 */
	EEntity copyApplicationInstance(SdaiModel target_model) throws SdaiException;

/**
 * Deletes this entity instance.
 * All references from other entity instances to this instance are unset.
 * After invoking of this method the Java object representing this
 * instance becomes invalid and is no longer referenced within JSDAI.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.11.2 Delete application instance"
 */
	void deleteApplicationInstance() throws SdaiException;

/*
 * Assigns a value of type <code>Object</code> to the specified explicit attribute.
 * This method is applicable for values of type
 * EEntity, String, and Binary.
 * If the base type of the attribute is select type and the
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type
 * (as defined in Technical corrigendum of ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select"
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity;
 * <li> VA_NSET if null is passed for the value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type
 * is wrong for the attribute whose value is being set or its type is <code>Aggregate</code>;
 * in the latter case <code>createAggregate</code> method shall be applied;
 * <li> VA_NVLD if select path is invalid.</ul>
 * <p> This method is a late binding implementation of the
 * "put attribute" operation.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EAttribute attr = vect.getAttributeDefinition("orientation");
 *    EDirection dir = ...;
 *    vect.set(attr, dir, null);</pre></TT>

 * @param attribute the explicit attribute to be assigned a value.
 * @param value the value to be assigned to the attribute.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(EAttribute, int, EDefined_type[])
 * @see #set(EAttribute, double, EDefined_type[])
 * @see #set(EAttribute, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.11.3 put attribute"
 */
/**
Assigns a value of type <code>Object</code> to the specified explicit attribute.
This method is applicable for values of type EEntity, String, and Binary.
If the base type of the attribute is select type and the set of possible 
leaf types is not a subset of the set of entity data types, 
then the select path corresponding to the value to be assigned needs to be 
submitted (through the third method's parameter).
Otherwise, null to the third parameter can be passed.

<p> Select path is a sequence of defined data types needed to specify a
value of a select data type (as defined in ISO 10303-21:11.1.8).
In fact, for a select data type a value of which is asked to be set, 
a certain oriented acyclic graph G = (V,A) can be constructed. 
The vertices of this graph correspond to defined data types and select data types.
An arc goes from the vertex representing defined data type to a vertex representing 
select data type if the latter is the value of the attribute "domain" of the former.
Further, an arc connects vertex of the select data type to a vertex of 
the defined data type if this latter type belongs to the SET assigned to the 
attribute "selections" of the select data type.

<P><B>Example.</B>
Suppose the following data types are given (here Express language notation is used):

<P><TT><pre>
TYPE chi = LIST [1:3] OF REAL;
END_TYPE;

TYPE xi = INTEGER;
END_TYPE;

TYPE tau = ENUMERATION OF
	(stigma,
	 digamma,
	 koppa,
	 sampi);
END_TYPE;

TYPE rho = SELECT
	(nu,
	 omicron,
	 upsilon);
END_TYPE;

TYPE nu = SELECT
	(chi,
	 xi,
	 tau);
END_TYPE;

TYPE omicron = lambda;
END_TYPE;

TYPE lambda = nu;
END_TYPE;

TYPE upsilon = SET [0:3] OF nu;
END_TYPE;</pre></TT>

For these Express definitions the following data dictionary instances are created by JSDAI:

<P><TT><pre>
#2=DEFINED_TYPE('chi',(),$,#94);
#94=LIST_TYPE(#93,#91,#92,.F.);
#91=INTEGER_BOUND(1);
#92=INTEGER_BOUND(3);
#93=REAL_TYPE($);
#10=DEFINED_TYPE('xi',(),$,#99);
#99=INTEGER_TYPE();
#16=DEFINED_TYPE('tau',(),$,#103);
#103=ENUMERATION_TYPE(('stigma','digamma','koppa','sampi'));
#20=DEFINED_TYPE('rho',(),$,#105);
#105=SELECT_TYPE((#4,#14,#18));
#4=DEFINED_TYPE('nu',(),$,#104);
#104=SELECT_TYPE((#2,#10,#16));
#14=DEFINED_TYPE('omicron',(),$,#15);
#15=DEFINED_TYPE('lambda',(),$,#4);
#18=DEFINED_TYPE('upsilon',(),$,#102);
#102=SET_TYPE(#4,#100,#92);
#100=INTEGER_BOUND(0);</pre></TT>

Then, for example, for the select data type "rho" the oriented graph G can be constructed 
whose vertex set V = {#20,#105,#4,#14,#18,#104,#15,#2,#10,#16} 
and the arc set A = {(#20,#105), (#105,#4), (#105,#14), (#105,#18), (#4,#104), 
(#14,#15), (#15,#4), (#104,#2), (#104,#10), (#104,#16)}.

<p> The root of the graph corresponds to the select data type inducing the graph, 
whereas the types of the values which can be assigned to an attribute of this 
select data type are represented by the vertices of this graph having no 
outcoming arcs (leaves of the graph). These vertices are called value vertices.
In the example given above, the root #20 is for "rho" and the vertices #2,#10,#16,#18 
represent 'LIST [1:3] OF REAL', INTEGER, 'ENUMERATION OF (stigma,digamma,koppa,sampi)', 
and 'SET [0:3] OF nu', respectively. When setting a value to an attribute, 
the needed path from the root of the graph to one of its value vertices shall be identified. 
To unambiguously describe such a path, it is sufficient to list only vertices 
of G corresponding to defined data types.
Even more, not all such vertices following from the root to a value vertex are taken.
The following two rules shall be applied:
<OL>
<LI> Each defined type whose "domain" is a select data type is excluded from the path.</LI>
<LI> If in the path there is a subpath of length at least 4 whose both endvertices represent 
select data types and the internal vertices represent defined data types, 
then only the first of the internal vertices is left, while the others are removed.</LI>
</OL>
Such a sequence of vertices obtained by taking only defined data types and applying 
the two rules stated above is called a shrinked select path or just a select path.

<P><B>Example (continued).</B>
For value of type 'SET [0:3] OF nu' the full path is #20,#105,#18, whereas the corresponding 
shrinked select path is #18, that is, consists of a single vertex. For value of type 
INTEGER there is full path #20,#105,#4,#104,#10 and its shrinked select path is #10.
However, for INTEGER there exists also another path #20,#105,#14,#15,#4,#104,#10. 
Its shrinked select path is #14,#10. This means that the same integer value can 
be set to the same attribute in two slightly different contexts.


<p> When trying to set a value for a given attribute whose base type is select 
data type and in the graph describing it there is the corresponding value vertex, 
then it is required to provide the shrinked select path to this value vertex by 
submitting a nonempty array of EDefined_type as the third parameter of this method. 
To prepare this array, {@link ESchema_definition#getDefinedType getDefinedType} 
method can be used.

<P><B>Example (continued).</B>
To set an integer to an attribute of type "rho" the following statements can be adopted.
<P><TT><pre>
	EEpsilon inst = ...
	EAttribute attr = inst.getAttributeDefinition("e5");
	ESchema_definition schema = inst.findEntityInstanceSdaiModel().getUnderlyingSchema();
	EDefined_type[] sel = new EDefined_type[5];
	sel[0] = schema.getDefinedType("xi"); // the shrinked select path consists of the defined type with the name 'xi'
	inst.set(attr, 99, sel);</pre></TT>
After exporting the repository containing 'inst' to an exchange structure, 
this value will be written as XI(99).
If the above excerpt from an Express schema was modified slightly by taking 
'TYPE upsilon = INTEGER;' instead of 'TYPE upsilon = SET [0:3] OF nu;', 
then the following assignment of the same value to 'attr' is classified as 
being different from that given before:
<P><TT><pre>
	sel[0] = schema.getDefinedType("upsilon"); // the shrinked select path consists of the defined type with the name 'upsilon'
	sel[1] = null;
	inst.set(attr, 99, sel);</pre></TT>
In an exchange structure the value will be given as UPSILON(99).

<p> The number of defined data types in the array "select" giving the select path 
is either specified by the position of the first null value in this array or 
is equal to the length of this array if it contains no null value.
<p> The list of cases when <code>SdaiException</code> is thrown
contains the following items:
<ul><li> AT_NDEF if no attribute is submitted;
<li> AT_NVLD if attribute is either not explicit one or not from this entity;
<li> VA_NSET if null is passed for the value to be assigned;
<li> VT_NVLD if value cannot be assigned because either its type is wrong for 
the attribute whose value is being set or its type is <code>aggregation_type</code>;
in the latter case {@link #createAggregate createAggregate} method shall be applied;
<li> VA_NVLD if select path is invalid.</ul>
<p> This method is a late binding implementation of the "put attribute" operation.

@param attribute the explicit attribute to be assigned a value.
@param value the value to be assigned to the attribute.
@param select select path corresponding to the value submitted.
@throws SdaiException TR_NAVL, transaction currently not available.
@throws SdaiException TR_NRW, transaction not read-write.
@throws SdaiException TR_EAB, transaction ended abnormally.
@throws SdaiException MX_NRW, SDAI-model access not read-write.
@throws SdaiException EI_NEXS, entity instance does not exist.
@throws SdaiException AT_NDEF, attribute not defined.
@throws SdaiException AT_NVLD, attribute invalid.
@throws SdaiException VA_NSET, value not set.
@throws SdaiException VT_NVLD, value type invalid.
@throws SdaiException VA_NVLD, value invalid.
@throws SdaiException SY_ERR, underlying system error.
@see #set(EAttribute, int, EDefined_type[])
@see #set(EAttribute, double, EDefined_type[])
@see #set(EAttribute, boolean, EDefined_type[])
@see "ISO 10303-22::10.11.3 put attribute"
 */
	void set(EAttribute attribute, Object value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a value of type <code>int</code> to the specified explicit attribute.
 * This method is applicable for values of EXPRESS data types
 * INTEGER, BOOLEAN, LOGICAL, ENUMERATION and REAL.
 * If the base type of the attribute is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select"
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link #set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity;
 * <li> VA_NSET if unset value is passed for the attribute;
 * <li> VT_NVLD if value cannot be assigned because either Java <code>int</code> type
 * is disallowed for the attribute whose value is being set or
 * select path represents EXPRESS data type that cannot be mapped to
 * Java <code>int</code>.
 * <li> VA_NVLD if select path is invalid.</ul>
 * <p> This method is a late binding implementation of the
 * "put attribute" operation.

 * <P><B>Example:</B>
 * <P><TT><pre>    ETrimmed_curve curve = ...;
 *    EAttribute attr = curve.getAttributeDefinition("master_representation");
 *    curve.set(attr, 15, null);</pre></TT>

 * @param attribute the explicit attribute to be assigned a value.
 * @param value the value to be assigned to the attribute.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(EAttribute, Object, EDefined_type[])
 * @see #set(EAttribute, double, EDefined_type[])
 * @see #set(EAttribute, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.11.3 put attribute"
 */
	void set(EAttribute attribute, int value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a value of type <code>double</code> to the specified explicit attribute.
 * This method is applicable for values of EXPRESS data types
 * REAL, and NUMBER.
 * If the base type of the attribute is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select"
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link #set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity;
 * <li> VA_NSET if unset value is passed for the attribute;
 * <li> VT_NVLD if value cannot be assigned because either Java <code>double</code> type
 * is disallowed for the attribute whose value is being set or
 * select path represents EXPRESS data type that cannot be mapped to
 * Java <code>double</code>.
 * <li> VA_NVLD if select path is invalid.</ul>
 * <p> This method is a late binding implementation of the
 * "put attribute" operation.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EAttribute attr = vect.getAttributeDefinition("magnitude");
 *    vect.set(attr, 9.99, null);</pre></TT>

 * @param attribute the explicit attribute to be assigned a value.
 * @param value the value to be assigned to the attribute.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(EAttribute, Object, EDefined_type[])
 * @see #set(EAttribute, int, EDefined_type[])
 * @see #set(EAttribute, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.11.3 put attribute"
 */
	void set(EAttribute attribute, double value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a value of type <code>boolean</code> to the specified explicit attribute.
 * This method is applicable for values of EXPRESS data type BOOLEAN.
 * If the base type of the attribute is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select"
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link #set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity;
 * <li> VT_NVLD if value cannot be assigned because either Java <code>boolean</code> type
 * is disallowed for the attribute whose value is being set or
 * select path represents EXPRESS data type that cannot be mapped to
 * Java <code>boolean</code>.
 * <li> VA_NVLD if select path is invalid.</ul>
 * <p> This method is a late binding implementation of the
 * "put attribute" operation.

 * <P><B>Example:</B>
 * <P><TT><pre>    EOriented_edge edge = ...;
 *    EAttribute attr = edge.getAttributeDefinition("orientation");
 *    edge.set(attr, true, null);</pre></TT>

 * @param attribute the explicit attribute to be assigned a value.
 * @param value the value to be assigned to the attribute.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(EAttribute, Object, EDefined_type[])
 * @see #set(EAttribute, int, EDefined_type[])
 * @see #set(EAttribute, double, EDefined_type[])
 * @see "ISO 10303-22::10.11.3 put attribute"
 */
	void set(EAttribute attribute, boolean value, EDefined_type select[])
		throws SdaiException;

/**
 * Changes the state of the specified explicit attribute, so that this
 * attribute has no value longer.
 * If no attribute is submitted or, respectively,
 * the attribute submitted is either not explicit one or not from this entity,
 * then <code>SdaiException</code> AT_NDEF and, respectively, AT_NVLD
 * is thrown.
 * <p> This method is a late binding implementation of the
 * "unset attribute value" operation.

 * @param attribute the explicit attribute whose value is to be unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.11.4 Unset attribute value"
 */
	void unsetAttributeValue(EAttribute attribute) throws SdaiException;

/**
 * Creates a new empty aggregate instance replacing the existing value (if any)
 * of the specified explicit attribute.
 * If the base type of the attribute is select type,
 * then the select path specifying the type of the aggregate
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select"
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link #set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity
 * or no aggregate can be created for this attribute;
 * <li> VA_NVLD if select path is invalid.</ul>
 * <p> This method is a late binding implementation of the
 * "create aggregate instance" operation.

 * <P><B>Example:</B>
 * <P><TT><pre>    ETrimmed_curve curve = ...;
 *    EAttribute attr = curve.getAttributeDefinition("trim_1");
 *    ATrimming_select aggr = curve.createAggregate(attr, null);</pre></TT>

 * @param attribute the explicit attribute whose value is to be set
 * with a new aggregate instance.
 * @param select select path specifying the type of the aggregate
 * to be assigned.
 * @return the aggregate instance that has been created.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.11.5 Create aggregate instance"
 */
	Aggregate createAggregate(EAttribute attribute, EDefined_type select[])
		throws SdaiException;

/**
 * Returns a <code>String</code>, which is a persistent label of this entity instance.
 * This label is assigned to the instance by JSDAI at the time when this instance is
 * created, and does not change later - it is persistent.
 * The label is unique within the repository containing
 * the instance. The label is defined both for application and dictionary instances.
 * <p>
 * JSDAI chooses the persistent label in such a way that it satisfies the requirements
 * imposed on the instance names in the clear text encoding of the
 * exchange structure (ISO 10303-21::7.3.4 Entity instance names).

 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity inst = ...;
 *    String ident = inst.getPersistentLabel();</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The value of <TT>ident</TT> will be like this:
 * <pre>    #2618</pre>

 * @return persistent label.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @see SdaiRepository#getSessionIdentifier
 * @see "ISO 10303-22::10.11.6 Get persistent label"
 */
	String getPersistentLabel() throws SdaiException;

/**
 * Returns a human readable description of the entity instance.
 * This description is in the form of two strings separated by one space.
 * The first string is entity instance name
 * (see ISO 10303-21::7.3.4 Entity instance names),
 * while the second is the name of the file containing this instance.
 * @return the description for the entity instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @see "ISO 10303-22::10.11.8 Get description"
 */
	String getDescription() throws SdaiException;

/**
 * Checks if a where rule is satisfied for this instance. 
 * If the first parameter delivering a rule is <code>null</code>, 
 * then all where rules found in the entity definition of this 
 * instance are checked. If the second parameter is <code>null</code>, 
 * then the domain is only the actual <code>SdaiModel</code> of the 
 * current instance. 
 * <p>
 * @param rule the where rule.
 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area in which to perform the validation of where rules. 
 * @return number 2 if rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.11.9 Validate where rule"
 * @see #validateWhereRule(AWhere_rule, ASdaiModel)
 */
	int validateWhereRule(EWhere_rule rule, ASdaiModel domain) throws SdaiException;

/**
 * Checks if where rules associated with this instance are satisfied. 
 * The rules validated include both those which are declared directly as part 
 * of the entity definition upon which this instance is based and those 
 * which are declared in defined types that serve as domains for the 
 * attributes of this entity definition.
 * If the second parameter is <code>null</code>, then the domain is only 
 * the actual <code>SdaiModel</code> of the current instance. 
 * <p>
 * @param viol_rules the non-persistent list to which violated 
 * where rules are appended.
 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area in which to perform the validation of where rules. 
 * @return number 2 if rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.11.9 Validate where rule"
 * @see #validateWhereRule(EWhere_rule, ASdaiModel)
 */
	int validateWhereRule(AWhere_rule viol_rules, ASdaiModel domain) throws SdaiException;

/**
 * Checks if all non-optional explicit attributes of this instance have values.
 * Redeclaring explicit attributes are considered as well.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which non-optional attributes
 * without values are appended.
 * @return <code>true</code> if all non-optional attributes have values or all attributes are optional,
 * <code>false</code> otherwise.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.10 Validate required explicit attributes assigned"
 */
	boolean validateRequiredExplicitAttributesAssigned(AAttribute nonConf)
		throws SdaiException;

/**
 * Checks if cardinality constraints specified in the INVERSE clauses
 * are satisfied for this entity instance.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the inverse attributes
 * violating the constraints are appended.
 * @return <code>true</code> if all inverse attribute constraints are satisfied or if
 * this entity does not have inverse attributes, <code>false</code> otherwise.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.11 Validate inverse attributes"
 */
	boolean validateInverseAttributes(AAttribute nonConf) throws SdaiException;

/**
 * Checks if cardinality constraints specified in the INVERSE clauses
 * are satisfied for this entity instance.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the inverse attributes
 * violating the constraints are appended.
 * @param domain the list of <code>SdaiModel</code>s specifying the
 * area inverse attributes in which shall be validated.
 * @return <code>true</code> if all inverse attribute constraints are satisfied or if
 * this entity does not have inverse attributes, <code>false</code> otherwise.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.11 Validate inverse attributes"
 */
	boolean validateInverseAttributes(AAttribute nonConf, ASdaiModel domain) throws SdaiException;

/**
 * Checks if all the entity instances referenced by explicit attributes
 * of this entity instance are of valid type for those attributes.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the attributes referencing
 * entity instances of wrong type are appended.
 * @return number 2 if all attributes referencing entity instances have
 * correct values or this instance has no such attributes,
 * number 1 if any attribute has value that is an instance of incorrect type,
 * and number 3 if any non-optional explicit attribute, which could reference
 * an entity instance, is unset.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.12 Validate explicit attributes references"
 */
	int validateExplicitAttributesReferences(AAttribute nonConf)
		throws SdaiException;

/**
 * Checks if the number of members in aggregates, or valid index values
 * for array instances, of any attributes of this entity instance
 * meet the constraints specified in the types of these attributes.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the attributes
 * having as values aggregates of sizes violating the constraints are appended.
 * @return number 2 if all aggregate size constraints for this entity instance
 * are satisfied,
 * number 1 if at least one size constraint is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.13 Validate aggregates size"
 */
	int validateAggregatesSize(AAttribute nonConf) throws SdaiException;

/**
 * Checks if all members are unique in any aggregate instance
 * being a value of any attribute whose type requires uniqueness.
 * This validation is performed for all attributes of this instance.
 * Entity instances in aggregates are checked for uniqueness by
 * comparing their names (see ISO 10303-21::7.3.4 Entity instance names).
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the attributes
 * with value violating the aggregate uniqueness requirement are appended.
 * @return <code>true</code> if all uniqueness constraints are satisfied or
 * this entity instance has no aggregates as attribute values,
 * <code>false</code> if at least one aggregate uniqueness constraint is violated.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.14 Validate aggregates uniqueness"
 */
	boolean validateAggregatesUniqueness(AAttribute nonConf) throws SdaiException;

/**
 * Checks if arrays have values at all positions for the attributes
 * of this entity instance where optional elements are not allowed.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistent list to which the attributes with
 * value of type array violating the requirement to have no unset elements
 * are appended.
 * @return <code>true</code> if all arrays, in which optional elements
 * are not allowed, have values at all index positions, or all arrays
 * are declared to contain optional elements, or this entity instance
 * has not attributes with array values,
 * <code>false</code> if at least one array, that is not declared to
 * have elements at all positions, is missing at least one element,
 * or if the array bounds conflict with array type declaration.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.15 Validate array not optional"
 */
	boolean validateArrayNotOptional(AAttribute nonConf) throws SdaiException;

/**
 * Checks if strings assigned, either directly or as members of aggregates, 
 * to attributes of this entity instance are of the required width.
 * This validation is performed for all attributes of the instance.
 * <p> If null is passed to the method's
 * parameter, then SdaiException AI_NEXS is thrown.
 * @param nonConf the non-persistant list to which the attributes
 * whose value is string having invalid width are appended.
 * @return number 2 if all string values assigned to attributes
 * are of the correct width,
 * number 1 if for at least one string value the declared width is violated,
 * and number 3 if derived attribute value expression does not
 * evaluate to a determinate value.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.11.16 Validate string width"
 */
	int validateStringWidth(AAttribute nonConf) throws SdaiException;

/**
 * Checks if <code>Binary</code> values assigned to attributes of this entity instance
 * are of the required width.
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @param nonConf the non-persistant list to which the attributes
 * whose value is <code>Binary</code> having invalid width are appended.
 * @return number 2 if all <code>Binary</code> values assigned to attributes
 * are of the correct width,
 * number 1 if for at least one <code>Binary</code> value the declared width is violated,
 * and number 3 if derived attribute value expression does not
 * evaluate to a determinate value.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.11.17 Validate binary width"
 */
	int validateBinaryWidth(AAttribute nonConf) throws SdaiException;

/**
 * Checks if <code>real</code> values assigned to attributes of this entity instance
 * are of the required minimum precision.
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @param nonConf the non-persistant list to which the attributes
 * with <code>real</code> value violating the declared precision are appended.
 * @return number 2 if all <code>real</code> values assigned to attributes
 * are of the declared precision,
 * number 1 if at least one <code>real</code> value violates the declared precision,
 * and number 3 if derived attribute value expression does not
 * evaluate to a determinate value.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.11.18 Validate real precision"
 */
	int validateRealPrecision(AAttribute nonConf) throws SdaiException;

/**
 * Returns a <code>String</code> representing this entity instance
 * including values of all its attributes.
 * This representation adheres all mapping rules defined
 * in "ISO 10303-21: Clear text encoding of the exchange structure".
 * The entity name may appear either in long form or in short form.
 * For example, for entity <code>cartesian_point</code> any of the following cases can be chosen: 
 * <ul><li> #1016=CARTESIAN_POINT('cp5',(0.0,0.0,-0.25));
 * <li> #1016=CRTPNT('cp5',(0.0,0.0,-0.25));
 * </ul>
 * To select one of these alternatives, repository method
 * {@link SdaiRepository#shortNameSupport shortNameSupport} should be used.
 * Selecting the short form can give the desired result only if the short name of the entity 
 * is defined in the data dictionary (provided by an Express compiler).
 * If method fails to identify this name, then the full entity 
 * name is used instead of.
 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    System.out.println("instance: " + vect);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    instance: #55=VECTOR('vec5',#1008,7.0);</pre>

 * @return the <code>String</code> representing this entity instance.
 * @see #getPersistentLabel getPersistentLabel
 * @see #getDescription getDescription
 * @see SdaiRepository#shortNameSupport
 * @see "ISO 10303-21: Implementation methods: Clear text encoding
 * of the exchange structure."
 */
	String toString();

/**
 * Returns an aggregate containing definitions of all single entity 
 * data types from which the entity type of this instance consists.
 * The entity types in the aggregate are given in the alphabetical order 
 * of their names.

 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiRepository repo = ...;
 *    EEntity inst2265 = repo.getSessionIdentifier("#2265");
 *    AEntity_definition types2265 = inst2265.typeOf();
 *    for (int i = 1; i <= types2265.getMemberCount(); i++) {
 *       System.out.println("type: " + types2265.getByIndex(i).getName(null));
 *    }</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;For an instance of the entity "dimension_text_associativity"
 * in the schema "aic_associative_draughting_elements" the output will be as follows:
 * <pre>    type: dimension_text_associativity
 *    type: geometric_representation_item
 *    type: mapped_item
 *    type: representation_item
 *    type: text_literal</pre>

 * @return the aggregate consisting of single entity data types for this
 * entity instance.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @see "ISO 10303-11::15.25 TypeOf - general function"
 */
	AEntity_definition typeOf() throws SdaiException;

/**
 * Returns an <code>Object</code> where an application is allowed
 * to store some user's data related to this entity instance.
 * This <code>Object</code> field is not persistent.
 * Its value is lost when the read-only or read-write access to the owning
 * <code>SdaiModel</code> is ended or when the transaction is aborted.
 * @return the current value of the special field of type<code>Object</code>.
 * @see #getTemp(Object key)
 * @see #setTemp(Object key)
 * @see #setTemp(Object key, Object value)
  */
	Object getTemp();

/**
 * Assigns a value to special field of type <code>Object</code>
 * in which an application is allowed to store some user's data related
 * to this entity instance.
 * This <code>Object</code> field is not persistent.
 * Its value is lost when the read-only or read-write access to the owning
 * <code>SdaiModel</code> is ended or when the transaction is aborted.
 * @param value the value for the special <code>Object</code> field
 * to be set.
 * @see #getTemp()
 * @see #getTemp(Object key)
 * @see #setTemp(Object key, Object value)
  */
	void setTemp(Object value);

	/**
	 * An extension of {@link #getTemp()}. Allows to retrieve more than one user data
	 * object previously stored with this entity using {@link #setTemp(Object key, Object value)}.
	 * Object is identified by a <code>key</code> which has to be the same as
	 * it was provided for method {@link #setTemp(Object key, Object value)}. The rest of the
     * functionality is identical to {@link #getTemp()}.
	 *
	 * @param key a key which identifies the user data object.
	 * @return the value of user defined data.
	 *
	 * @see #getTemp()
	 * @see #setTemp(Object key)
	 * @see #setTemp(Object key, Object value)
	 */
	Object getTemp(Object key);

	/**
	 * An extension of {@link #setTemp(Object value)}. Allows to store more than one user data
	 * object with this entity. Object is identified by a <code>key</code>. Later the same
	 * <code>key</code> can be used to retrieve the stored data object using
	 * {@link #getTemp(Object key)}.  The rest of the
     * functionality is identical to {@link #setTemp(Object value)}.
	 *
	 * @param key a key which identifies the user data object.
	 * @param value the used data value to store.
	 *
	 * @see #getTemp()
	 * @see #getTemp(Object key)
	 * @see #setTemp(Object key)
	 */
	void setTemp(Object key, Object value);


/**
 * Tests whether this instance of a target entity type does fit to mapping constraints of specified source entity.
 * It returns an aggregate containing all the mating
 *      entity_mappings for the specified source entity.
 * If there is no mappings for which constraints are met it returns null.
 * <P><B>Example:</B>
 * <P><TT><pre>
 *    EEntity instance = ...;  // instance that we want to check
 *    SdaiModel sourceModel = LangUtils.findDictionaryModel("my_mapping_schema"); // usualy name of mapping model is the same as it is for source schema
 *    sourceModel.startReadOnlyAccess();
 *    armSchema = LangUtils.findSchema(sourceModel);
 *    EEntity_definition sourceEntity = LangUtils.findEntityDefinition("my_source_entity", schema);
 *    AEntity_mapping mappings = m.testSourceEntity(sourceEntity, session.getDataDictionary(), session.getDataMapping());
 *    SdaiIterator i = mappings.createIterator();
 *    System.out.println("The instance " + instance + " is mapping of:");
 *    while (i.next()) {
 *        System.out.print(" " + mappings.getCurrentMember(i).getSource(null).getName(null));
 *    }
 *</pre></TT>
 * @param sourceEntity an entity which mapping is tested
 * @param dataDomain a target instances domain where to search instances to
 *     satisfy mapping constraints.  It may be null. Then owning model of
 *     target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source schemas.
 * @param mode sets level of cheking for mapped entity.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified entity mapping are tested
 * {@link #MANDATORY_ATTRIBUTES_SET} - all explicit mandatory ARM attributes of every entity mapping are tested.
 * If at least one these attributes is unset, then this entity mapping is not included in the returned set of entity mappings.
 * @return an aggregate of mapping_entities that may be mapping to target entity, null if
 *    there is no mappings of this target instance for specified target.
 * This returned aggregate contains at least one matting entity_mapping.
 * If there are several mapping alternatives the returned aggregate may contain several instances.
 * @see #testMappedEntity(EEntity_mapping sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedEntity
 * @see #findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see #testMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #getMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	AEntity_mapping testMappedEntity(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Tests whether this instance of a target entity type does fit to mapping constraints of specified source entity.
 * It returns an aggregate containing all the mating
 *      entity_mappings for the specified source entity.
 * If there is no mappings for which constraints are met it returns null.
 * <P><B>Example:</B>
 * <P><TT><pre>
 *    EEntity instance = ...;  // instance that we want to check
 *    SdaiModel sourceModel = LangUtils.findDictionaryModel("my_mapping_schema"); // usualy name of mapping model is the same as it is for source schema
 *    sourceModel.startReadOnlyAccess();
 *    armSchema = LangUtils.findSchema(sourceModel);
 *    EEntity_definition sourceEntity = LangUtils.findEntityDefinition("my_source_entity", schema);
 *    AEntity_mapping mappings = m.testSourceEntity(sourceEntity, session.getDataDictionary(), session.getDataMapping());
 *    SdaiIterator i = mappings.createIterator();
 *    System.out.println("The instance " + instance + " is mapping of:");
 *    while (i.next()) {
 *        System.out.print(" " + mappings.getCurrentMember(i).getSource(null).getName(null));
 *    }
 *</pre></TT>
 * @param sourceEntity an entity which mapping is tested
 * @param dataDomain a target instances domain where to search instances to
 *     satisfy mapping constraints.  It may be null. Then owning model of
 *     target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source schemas.
 * @param mode sets level of cheking for mapped entity.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified entity mapping are tested
 * {@link #MANDATORY_ATTRIBUTES_SET} - all explicit mandatory ARM attributes of every entity mapping are tested.
 * If at least one these attributes is unset, then this function returns false.
 * @return an aggregate of mapping_entities that may be mapping to target entity, null if
 *    there is no mappings of this target instance for specified target.
 * This returned aggregate contains at least one matting entity_mapping.
 * If there are several mapping alternatives the returned aggregate may contain several instances.
 * @see #testMappedEntity(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedEntity
 * @see #findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see #testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #getMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	boolean testMappedEntity(EEntity_mapping sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Tests mapping of source attribute.
 * Specified target instance should be mapping of sourceAttribute parent entity.
 * It returns attribute_mappings for which reference path is satisfied.
 * If there is no attribute_mappings which reference path is satisfied, then method returns null.
 * @param sourceAttribute source attribute which mapping is tested.
 * @param dataDomain a domain where to search instances to satisfy mapping constraints.
 * It may be null.
 * Then owning model of target instance will be used as domain.
 * @param mode sets level of cheking for mapped attribute.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified attribute are tested
 * {@link #MOST_SPECIFC_ENTITY} - if ARM type of attribute is enity data type, then the mapping constraints for this entity data type are checked,
 * {@link #MANDATORY_ATTRIBUTES_SET} - in addition all explicit mandatory ARM attributes of every entity mapping are tested.
 * @param mappingDomain a domain for mapping constraints, target and source schemas.
 * @return aggregate of attribute mappings that are mappings of source attribute and has values set.
 * It returns null if no attribute mappings found.
 * @see #testMappedEntity(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedEntity
 * @see #testMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #getMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	AGeneric_attribute_mapping testMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Test whether this target instance does fit to the constraints of the given attribute_mapping.
 * @param sourceAttribute source attribute which mapping is tested.
 * @param dataDomain a target instances domain where to search instances to
 *   satisfy mapping constraints.  It may be null. Then owning model of
 *   target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source
 *   schemas. It may be null. Then owning model of sourceAttribute will be used as
 *   domain.
 * @param mode sets level of cheking for mapped attribute.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified attribute are tested
 * {@link #MOST_SPECIFC_ENTITY} - if ARM type of attribute is enity data type, then the mapping constraints for this entity data type are checked,
 * {@link #MANDATORY_ATTRIBUTES_SET} - in addition all explicit mandatory ARM attributes of every entity mapping are tested.
 * @return true if this target instance has mapping of specified attribute mapping, false
 *   otherwise.
 * @see #testMappedEntity(EEntity_mapping sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testSourceEntity
 * @see #testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #getMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) getMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	boolean testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Returns value for source attribute.
 * If there is only one value then value is returned as object.
 * If there is more than one value then Java array Object[] is returned.
 * If there is no value set, then this method returns null.
 * There can be several reasons why there is more than one return value.
 * <ul>
 * <li> There are several attribute_mapping alternatives.
 *      Then this method will include values for all alternatives, even if the values will be the same.
 * <li> Mapping path for attribute includes constraints (like aggregate or inverse attribute relationship) for witch there ant be multiple values.
 * </ul>
 * The type of source attribute has no direct affect on the cardinality of values returned by this method.
 * Simple types like int, float, double and boolean are converted to corresponding Java objects.
 * <P><B>Example:</B>
 * <P><TT><pre>    EEntity_definition armEntity = ....
 *    ArrayList attributes = new ArrayList();
 *    LangUtils.findExplicitAttributes(armEntity, attributes);
 *    // Iterate through all explicit attributes of ARM entity.
 *    for (int i = 0; i < attributes.size(); i++) {
 *    armAttribute = (EAttribute)attributes.get(i);
 *    try {
 *        // Test if ARM attribute is set.
 *        System.out.print(armAttribute.getName(null));
 *        boolean f = instance.testSourceAttribute(armAttribute, dataSi, mappingSi) != null;
 *        // If ARM attribute is set, then take and print a value of it.
 *        if (f) {
 *            Object o[] = instance.getSourceAttribute(armAttribute, dataSi, mappingSi);
 *            if (o.length > 0) {
 *                if (o[0] instanceof String) {
 *                    System.out.print(" = '" + o[0] + "'");
 *                } else {
 *                    System.out.print(" = " + o[0]);
 *                }
 *                for (int k = 1; k < o.length; k++) {
 *                    if (o[k] instanceof String) {
 *                        System.out.print(" | '" + o[k] + "'");
 *                    } else {
 *                        System.out.print(" | " + o[k]);
 *                    }
 *                }
 *            } else {
 *                System.out.print(" = ?");
 *            }
 *        }
 *        System.out.println();
 *    } catch (Exception e) {
 *    }
 *</pre></TT>
 * @param sourceAttribute source attribute which mappings are returned.
 * @param dataDomain a target instances domain where to search instances to satisfy mapping constraints.
 * It may be null.
 * Then owning model of target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source
 *   schemas. It may be null. Then owning model of sourceAttribute will be used as
 *   domain.
 * @param mode sets level of cheking for mapped attribute.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified attribute are tested
 * {@link #MOST_SPECIFC_ENTITY} - if ARM type of attribute is enity data type, then the mapping constraints for this entity data type are checked,
 * {@link #MANDATORY_ATTRIBUTES_SET} - in addition all explicit mandatory ARM attributes of every entity mapping are tested.
 * @return aggregate of sourceAttribute values.
 * @see #testMappedEntity(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedEntity
 * @see #testMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #testMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	Object[] getMappedAttribute(EAttribute sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Returns value of attribute following constraints defined by attribute_mapping.
 * The target_instance must be mapping of entity_mapping of which attribute_mapping is get.
 * If mapping of attribute_mapping is not met it returns null.
 * @param sourceAttribute attribute which mapping is tested.
 * @param dataDomain a domain where to search instances to satisfy mapping constraints.
 * It may be null.
 * Then owning model of target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source schemas.
 * It may be null.
 * Then owning model of sourceAttribute will be used as domain.
 * @param mode sets level of cheking for mapped attribute.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified attribute are tested
 * {@link #MOST_SPECIFC_ENTITY} - if ARM type of attribute is enity data type, then the mapping constraints for this entity data type are checked,
 * {@link #MANDATORY_ATTRIBUTES_SET} - in addition all explicit mandatory ARM attributes of every entity mapping are tested.
 * @return value of source attribute, null if attribute_mapping reference path is not met.
 * If there are several values it returns jsdai.Aggregate.
 * @see #testMappedEntity(EEntity_mapping sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testSourceEntity
 * @see #testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see #testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) testMappedAttribute
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	Object getMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;

/**
 * Finds entity_mappings that fit with this instance.
 * If sourceEntity is specified then the search is restricted to entity_mappings for the
 * specified entity_definition and its subtypes.
 * Resulting mappings may be restricted to not include mappings for abstract types and mappings where there are mappings of subtypes.
 * Having entity_mapping an application can directly access the corresponding source entities.
 * @param sourceEntity only mappings of this entity or its subtypes can be included in returned set.
 * If it is null, then this function works the same as {@link #findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)}.
 * @param dataDomain a domain of the target instance and instances related to it.
 * It may be null.
 * Then owning model of target instance will be used as domain.
 * @param mappingDomain a domain for mapping constraints, target and source schemas.
 * @param mappings the mappings that was found.
 * This parameter is for read/write access.
 * The mappings that will be found will be added to this aggregate.
 * @param mode {@link #NO_RESTRICTIONS NO_RESTRICTIONS} - no restrictions,
 * {@link #MOST_SPECIFC_ENTITY MOST_SPECIFC_ENTITY} - retured mappings are restricted to most specific.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instanciated are also not included.
 * @return number of entity mappings that meet constraints.
 * @see #testMappedEntity testMappedEntity
 * @see #findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	int findEntityMappings(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain,
		AEntity_mapping mappings, int mode) throws SdaiException;

/**
 * Finds entity_mappings that fit with this instance.
 * @see #findEntityMappings(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping mappings, int mode) findEntityMappings}.
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	int findEntityMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain,
		AEntity_mapping mappings, int mode) throws SdaiException;

	/** Finds most specific ARM mappings for the instance.
	 * This is ARM (mapping) operations support method.
	 * Most specific mapping can be viewed as means to define
	 * of which ARM type the AIM entity really is.
	 * @return it returns an aggregate of entity_mappings that represent
	 * most specific mappings. Usualy resulting aggregate contains only one member.
	 * But in some cases instance can have more than one most specific mapping and
	 * then all mappings are returned.
	 * @param mappingDomain is domain for mapping constraints.
	 * @param dataDomain is domain which defines where to search instances that satisfy mapping constraints.
	 * 					In the case it is null owning model of target instance will be used as data domain.
	 * @param baseMappings is an aggregate of mapings which define where to start looking for most specific mappings.
	 * The mappings returned will be mappings for subtypes of source enitity of base mappings.
	 * @param mode is mode for mapping operations. It can be one of:
	 * EEntity.NO_RESTRICTIONS
	 * EEntity.MOST_SPECIFC_ENTITY
	 * EEntity.MANDATORY_ATTRIBUTES_SET
	 * @throws SdaiException All variety of SdaiExceptions can be thrown.
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public AEntity_mapping findMostSpecificMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain,
		AEntity_mapping baseMappings, int mode) throws SdaiException;

	/**
	 * Creates or returns already created mapped entity 
	 * instance in context's mappedWorkingModel model.
	 * If mapped entity is created it is in ATTRIBUTES_UNKNOWN state.
	 *
	 * @param context context where the mapped instance should be created.
	 * @param mappedInstanceType mapped entity instance expected type or 
	 *        null if application is unable to specify expected type.
	 * @return mapped entity instance possibly created in context.mappedWorkingModel.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #findLinkedMappedInstance(EEntity_definition mappedInstanceType)
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public EMappedARMEntity buildMappedInstance(SdaiContext context, EEntity_definition mappedInstanceType)
		throws SdaiException;

	/**
	 * Finds already created mapped entity instance.
	 * @param mappedInstanceType mapped entity instance expected type or 
	 *        null if application is unable to specify expected type
	 * @return mapped entity instance possibly created in context.mappedWorkingModel
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #buildMappedInstance(SdaiContext context, EEntity_definition mappedInstanceType)
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public EMappedARMEntity findLinkedMappedInstance(EEntity_definition mappedInstanceType) 
		throws SdaiException;

/**
 * Compares this entity instance to the submitted entity instance.
 * Both instances must be based on the same entity definition.
 * If not, then <code>SdaiException</code> ED_NDEQ is thrown.
 * When executing this method, values of all attributes
 * in these two instances are compared.
 * <p> Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect1 = ...;
 *    EVector vect2 = ...;
 *    boolean equal = vect1.compareValuesBoolean(vect2);</pre></TT>
 * @param inst the entity instance submitted to compare against this
 * entity instance.
 * @return <code>true</code> if values of the same attributes
 * of the instances are equal, <code>false</code> otherwise.
 * @throws SdaiException ED_NDEQ, entity definition not domain equivalent.
 * @throws SdaiException VA_NSET, value not set.
 * @see #compareValuesLogical
 */
	boolean compareValuesBoolean(EEntity inst) throws SdaiException;

/**
 * Compares this entity instance to the submitted entity instance.
 * This method is not implemented in current JSDAI version.
 * <p> SdaiException FN_NAVL will be thrown if this method is invoked.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param instance the entity instance submitted to compare against this
 * entity instance.
 * @return number 2 if values of the same attributes of the instances are equal,
 * number 1 if values for at least one attribute are different,
 * and number 3 if a comparison result for at least one attribute is unknown.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-11::12.2.1.7 Entity value comparison"
 */
	int compareValuesLogical(EEntity instance) throws SdaiException;

/*
 * This method is not available now.
 * Mapping operations that creates application data may create some instances with mandatory attributes not set.
 * This method fixes it by seting all mandatory attributes to some dumb values.
 * It checks only the instances that are used by specified mapping.
	void finalyzeMapping(EEntity_mapping usedMapping, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException;
*/

/**
 * It finds application data instances that coresponds to mapped instaces, that references to this instance.
 * @param source_type mapping of entity data type, which mapping constraints this entity satisfies.
 * @param attribute mappings of attributes, for which inverses will be searched.
 * The specified attributes must reference to source_type.
 * If it is not specified, then all attributes that references source_type will be in result set.
 * @param data_domain a domain where to search instances to satisfy mapping constraints.
 * It may be null.
 * Then owning model of target instance will be used as domain.
 * @param mapping_domain a domain for mapping constraints, target and source schemas.
 * @param users for attribute mappings, that uses this instance.
 * It can be null.
 * It is synchronized with return value.
 * @param mode sets level of cheking for mapped attribute.
 * {@link #NO_RESTRICTIONS} - only the mapping constraints for specified attribute are tested
 * {@link #MOST_SPECIFC_ENTITY} - if ARM type of attribute is enity data type, then the mapping constraints for this entity data type are checked,
 * {@link #MANDATORY_ATTRIBUTES_SET} - in addition all explicit mandatory ARM attributes of every entity mapping are tested.
 * @return set of instances that satisfies requirements to reference to this instance throgh the mapping.
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
*/
	AEntity findMappedUsers(EEntity_mapping source_type, AAttribute_mapping attribute,
		   ASdaiModel data_domain, ASdaiModel mapping_domain, AAttribute_mapping users,
			int mode) throws SdaiException;

/**
 * Checks is attribute mapped for given entity mapping.
 * If entity mapping is not specified then check is attribute mapped at all.
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	boolean hasMappedAttribute(EEntity_mapping entityMapping, EAttribute attribute, ASdaiModel mappingDomain) throws SdaiException;

	/**
	 * Creates external for this entity.
	 * External data is the data outside express
	 * which can be stored within the entity.
	 * The entity can have zero or one ExternalData object.
	 * This method works in the context of jsdai transactions.
	 * @return {@link ExternalData} object which holds newly created entity external data
	 * @see #getExternalData
	 * @see #testExternalData
	 * @see #removeExternalData
	 * @throws SdaiException if this entity already has an external data
	 * @throws SdaiException there can be other cases when exception it thrown
	 */
	ExternalData createExternalData() throws SdaiException;
	
	/**
	 * Gets external data object.
	 * This method works in the context of jsdai transactions.
	 * @return {@link ExternalData} object which holds this entity external data
	 * @see #createExternalData
	 * @see #testExternalData
	 * @see #removeExternalData
	 * @throws SdaiException if this entity has no external data
	 * @throws SdaiException there can be other cases when exception it thrown
	 */
	ExternalData getExternalData() throws SdaiException;

	/** 
	 * Removes external data which was attached to this entity.
	 * If application has obtained {@link ExternalData} object it becomes invalid.
	 * If the entity has no external data this method does nothing.
	 * @see #createExternalData
	 * @see #getExternalData
	 * @see #testExternalData
	 * @throws SdaiException
	 */
	void removeExternalData() throws SdaiException;

	/** 
	 * Tests if external data is attached to this entity.
	 * @see #createExternalData
	 * @see #getExternalData
	 * @see #removeExternalData
	 * @return true if the entity has external data attached
	 * @throws SdaiException
	 */
	boolean testExternalData() throws SdaiException;

/**
 * Assigns the value wrapped in a given object of type <code>Value</code>
 * to the specified explicit attribute. Particularly, it is allowed 
 * for the value to be an aggregate. 
 * Also this method can be used to unset the attribute.
 * <p> The list of cases when <code>SdaiException</code> is thrown
 * contains the following items:
 * <ul><li> AT_NDEF if no attribute is submitted;
 * <li> AT_NVLD if attribute is either not explicit one or not from this entity;
 * <li> VA_NSET if null is passed for the value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because its type
 * is wrong for the attribute whose value is being set.</ul>

 * <P><B>Example:</B>
 * <P><TT><pre>
 *    EData_type dt = SdaiSession.findDataType("myStringType", jsdai.SMy_schema.SMy_schema.class);
 *    Value val = Value.alloc(dt).set("LKSoft");
 *    EAttribute attr = ...;
 *    EEntity inst = ...;
 *    inst.set(attr, val);</pre></TT>

 * @param attribute the explicit attribute to be assigned a value.
 * @param val the value to be assigned to the attribute.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException AT_NVLD, attribute invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	void set(EExplicit_attribute attribute, Value val) throws SdaiException;

/**
 * Returns the value (wrapped in an object of type <code>Value</code>) of the 
 * specified attribute, which can be either explicit or derived but not inverse. 
 * This method returns meaningfull information even in the case when the 
 * attribute is unset. 
 * <P>
 * In the case when the attribute is of EEntity type and references an instance 
 * whose owning SdaiModel has access mode unset, then <code>Value</code> contains 
 * some data describing this reference but not the referenced instance itself.

 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EAttribute attr = vect.getAttributeDefinition("orientation");
 *    // access the attribute getting an instance of <code>Value</code>
 *    Value val = vect.get(attr);
 *    // retrieve an entity instance from <code>Value</code>
 *    EDirection dir = (EDirection)val.getInstance();</pre></TT>


 * @param attribute the attribute which value is needed.
 * @return value (of type <code>Value</code>) of the attribute.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	Value get(EAttribute attribute) throws SdaiException;

/**
 * Returns an aggregate (of Express type LIST) whose elements are 
 * entity instances referenced by the current entity instance.
 * In the case when an instance is referenced multiple times, this instance 
 * will appear in the aggregate once for each reference.
 * @return the list of referenced entity instances.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	AEntity getAllReferences() throws SdaiException;

/**
 * Checks if java <code>Object</code> of type <code>EEntity</code> 
 * represents a valid instance of a population within {@link SdaiSession}.
 * @return <code>true</code> if the current instance is valid, 
 * <code>false</code> otherwise.
 * @since 3.6.0
 */
	boolean isValid();

/**
 * Changes all the pointers referencing an entity instance submitted 
 * through the parameter to the pointers referencing the current entity instance.
 * Only references from instances in <code>SdaiModel</code>s with started 
 * read-write access mode are considered. 
 * In the case of a reference from an instance in an <code>SdaiModel</code> 
 * existing in the read-only mode SdaiException MX_NRW is thrown.
 * If the submitted instance is referenced by an instance in an 
 * <code>SdaiModel</code> whose access is ended, then such a reference is left 
 * intact.
 * <p>
 * This method is available only when the entity data type of the current 
 * instance is the same as that of the submitted instance or a subtype of it 
 * (in other words, this.isKindOf(src.getInstanceType()) returns <code>true</code>).

 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiRepository repo = ...;
 *    EEntity inst40 = repo.getSessionIdentifier("#40");
 *    EEntity inst4055 = repo.getSessionIdentifier("#4055");
 *    EEntity move = inst4055.moveUsersFrom(inst40);
 *    // move and inst4055 point to the same entity instance </TT></pre>
 * <P>

 * @param src an entity instance the references to which are moved 
 * to this entity instance
 * @return this entity instance
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	EEntity moveUsersFrom(EEntity src) throws SdaiException;

/**
 * Copies values of all attributes of the entity instance submitted 
 * through the parameter to the corresponding attributes of the current entity instance.
 * The <code>SdaiModel</code> owning the current instance shall be in  
 * read-write access mode. The source instance retain the values of the attributes.
 * <p>
 * This method is available only when the entity data types of the current 
 * instance and the submitted instance are the same.

 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiRepository repo = ...;
 *    EEntity inst2241 = repo.getSessionIdentifier("#2241");
 *    EEntity inst2243 = repo.getSessionIdentifier("#2243");
 *    EEntity copy = inst2241.copyValuesFrom(inst2243);
 *    // copy and inst2241 point to the same entity instance </TT></pre>
 * <P>

 * @param src an entity instance the attribute values of which are 
 * copied to this entity instance
 * @return this entity instance
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	EEntity copyValuesFrom(EEntity src) throws SdaiException;

}
