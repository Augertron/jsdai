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

package jsdai.mappingUtils.paths;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jsdai.SExtended_dictionary_schema.ADefined_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SMapping_schema.AConstraint_select;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAnd_constraint_relationship;
import jsdai.SMapping_schema.EBoolean_constraint;
import jsdai.SMapping_schema.EEnd_of_path_constraint;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EExact_entity_constraint;
import jsdai.SMapping_schema.EExact_type_constraint;
import jsdai.SMapping_schema.EInstance_equal;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.EIntersection_constraint;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.ENegation_constraint;
import jsdai.SMapping_schema.EOr_constraint_relationship;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.SMapping_schema.EType_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created: Tue Apr 22 20:00:29 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class ConstraintFactory {
	private MappingPathParser parser;

	private Map pathConstraintMap;
	private Map inverseAttributeConstraintMap;
	private Map aggregateMemberConstraintMap;
	private Map stringConstraintMap;
	private Map realConstraintMap;
	private Map enumerationConstraintMap;
	private Map booleanConstraintMap;
	private Map integerConstraintMap;
	private Map logicalConstraintMap;
	private Map selectConstraintMap;
	private Map instanceEqualMap;
	private Map entityConstraintMap;
	private Map exactEntityConstraintMap;
	private Map typeConstraintMap;
	private Map exactTypeConstraintMap;
	private Map intersectionConstraintMap;
	private Map negationConstraintMap;
	private Map endOfPathConstraintMap;
	private Map andConstraintRelationshipMap;
	private Map orConstraintRelationshipMap;

	public ConstraintFactory(MappingPathParser parser) {
		this.parser = parser;
		pathConstraintMap = new HashMap();
		inverseAttributeConstraintMap = new HashMap();
		aggregateMemberConstraintMap = new HashMap();
		stringConstraintMap = new HashMap();
		realConstraintMap = new HashMap();
		enumerationConstraintMap = new HashMap();
		booleanConstraintMap = new HashMap();
		integerConstraintMap = new HashMap();
		logicalConstraintMap = new HashMap();
		selectConstraintMap = new HashMap();
		instanceEqualMap = new HashMap();
		entityConstraintMap = new HashMap();
		exactEntityConstraintMap = new HashMap();
		typeConstraintMap = new HashMap();
		exactTypeConstraintMap = new HashMap();
		intersectionConstraintMap = new HashMap();
		negationConstraintMap = new HashMap();
		endOfPathConstraintMap = new HashMap();
		andConstraintRelationshipMap = new HashMap();
		orConstraintRelationshipMap = new HashMap();
	}

	public EPath_constraint createPathConstraint(EEntity element1, EEntity element2) 
		throws SdaiException {
		ObjectObjectKey pathConstraintKey = new ObjectObjectKey(element1, element2);
		EPath_constraint newPathConstraint = (EPath_constraint)pathConstraintMap.get(pathConstraintKey);
		if(newPathConstraint == null) {
			newPathConstraint = (EPath_constraint)
				parser.mappingModel.createEntityInstance(EPath_constraint.class);
			newPathConstraint.setElement1(null, element1);
			newPathConstraint.setElement2(null, element2);
			pathConstraintMap.put(pathConstraintKey, newPathConstraint);
		}
		return newPathConstraint;
	}

	public EInverse_attribute_constraint createInverseAttributeConstraint(EEntity invertedAttribute) 
		throws SdaiException {
		EInverse_attribute_constraint newInvAttConstraint = (EInverse_attribute_constraint)
			inverseAttributeConstraintMap.get(invertedAttribute);
		if(newInvAttConstraint == null) {
			newInvAttConstraint = (EInverse_attribute_constraint)
				parser.mappingModel.createEntityInstance(EInverse_attribute_constraint.class);
			newInvAttConstraint.setInverted_attribute(null, invertedAttribute);
			inverseAttributeConstraintMap.put(invertedAttribute, newInvAttConstraint);
		}
		return newInvAttConstraint;
	}

	public EAggregate_member_constraint createAggregateMemberConstraint(EEntity attribute, int member)
		throws SdaiException {
		ObjectIntKey aggregateMemberConstraintKey = new ObjectIntKey(attribute, member);
		EAggregate_member_constraint newAggMembConstraint = (EAggregate_member_constraint)
			aggregateMemberConstraintMap.get(aggregateMemberConstraintKey);
		if(newAggMembConstraint == null) {
			newAggMembConstraint = (EAggregate_member_constraint)
				parser.mappingModel.createEntityInstance(EAggregate_member_constraint.class);
			newAggMembConstraint.setAttribute(null, attribute);
			if(member >= 0) {
				newAggMembConstraint.setMember(null, member);
			}
			aggregateMemberConstraintMap.put(aggregateMemberConstraintKey, newAggMembConstraint);
		}
		return newAggMembConstraint;
	}

	public EString_constraint createStringConstraint(EEntity attribute, String value)
		throws SdaiException {
		ObjectObjectKey stringConstraintKey = new ObjectObjectKey(attribute, value);
		EString_constraint newStringConstraint = (EString_constraint)
			stringConstraintMap.get(stringConstraintKey);
		if(newStringConstraint == null) {
			newStringConstraint = (EString_constraint)
				parser.mappingModel.createEntityInstance(EString_constraint.class);
			newStringConstraint.setAttribute(null, attribute);
			newStringConstraint.setConstraint_value(null, value);
			stringConstraintMap.put(stringConstraintKey, newStringConstraint);
		}
		return newStringConstraint;
	}

	public EReal_constraint createRealConstraint(EEntity attribute, double value)
		throws SdaiException {
		ObjectDoubleKey realConstraintKey = new ObjectDoubleKey(attribute, value);
		EReal_constraint newRealConstraint = (EReal_constraint)
			realConstraintMap.get(realConstraintKey);
		if(newRealConstraint == null) {
			newRealConstraint = (EReal_constraint)
				parser.mappingModel.createEntityInstance(EReal_constraint.class);
			newRealConstraint.setAttribute(null, attribute);
			newRealConstraint.setConstraint_value(null, value);
			realConstraintMap.put(realConstraintKey, newRealConstraint);
		}
		return newRealConstraint;
	}

	public EEnumeration_constraint createEnumerationConstraint(EEntity attribute, String value)
		throws SdaiException {
		ObjectObjectKey enumerationConstraintKey = new ObjectObjectKey(attribute, value);
		EEnumeration_constraint newEnumConstraint = (EEnumeration_constraint)
			enumerationConstraintMap.get(enumerationConstraintKey);
		if(newEnumConstraint == null) {
			newEnumConstraint = (EEnumeration_constraint)
				parser.mappingModel.createEntityInstance(EEnumeration_constraint.class);
			newEnumConstraint.setAttribute(null, attribute);
			newEnumConstraint.setConstraint_value(null, value);
			enumerationConstraintMap.put(enumerationConstraintKey, newEnumConstraint);
		}
		return newEnumConstraint;
	}

	public EBoolean_constraint createBooleanConstraint(EEntity attribute, boolean value)
		throws SdaiException {
		ObjectBooleanKey booleanConstraintKey = new ObjectBooleanKey(attribute, value);
		EBoolean_constraint newBoolConstraint = (EBoolean_constraint)
			booleanConstraintMap.get(booleanConstraintKey);
		if(newBoolConstraint == null) {
			newBoolConstraint = (EBoolean_constraint)
				parser.mappingModel.createEntityInstance(EBoolean_constraint.class);
			newBoolConstraint.setAttribute(null, attribute);
			newBoolConstraint.setConstraint_value(null, value);
			booleanConstraintMap.put(booleanConstraintKey, newBoolConstraint);
		}
		return newBoolConstraint;
	}

	public EInteger_constraint createIntegerConstraint(EEntity attribute, int value)
		throws SdaiException {
		ObjectIntKey integerConstraintKey = new ObjectIntKey(attribute, value);
		EInteger_constraint newIntConstraint = (EInteger_constraint)
			integerConstraintMap.get(integerConstraintKey);
		if(newIntConstraint == null) {
			newIntConstraint = (EInteger_constraint)
				parser.mappingModel.createEntityInstance(EInteger_constraint.class);
			newIntConstraint.setAttribute(null, attribute);
			newIntConstraint.setConstraint_value(null, value);
			integerConstraintMap.put(integerConstraintKey, newIntConstraint);
		}
		return newIntConstraint;
	}

	public ELogical_constraint createLogicalConstraint(EEntity attribute, int value)
		throws SdaiException {
		ObjectIntKey logicalConstraintKey = new ObjectIntKey(attribute, value);
		ELogical_constraint newLogicalConstraint = (ELogical_constraint)
			logicalConstraintMap.get(logicalConstraintKey);
		if(newLogicalConstraint == null) {
			newLogicalConstraint = (ELogical_constraint)
					parser.mappingModel.createEntityInstance(ELogical_constraint.class);
			newLogicalConstraint.setAttribute(null, attribute);
			newLogicalConstraint.setConstraint_value(null, value);
			logicalConstraintMap.put(logicalConstraintKey, newLogicalConstraint);
		}
		return newLogicalConstraint;
	}

	public ESelect_constraint createSelectConstraint(EEntity attribute, List dataTypeList)
		throws SdaiException {
		ObjectObjectKey selectConstraintKey = new ObjectObjectKey(attribute, dataTypeList);
		ESelect_constraint newSelectConstraint = (ESelect_constraint)
			selectConstraintMap.get(selectConstraintKey);
		if(newSelectConstraint == null) {
			newSelectConstraint = (ESelect_constraint)
				parser.mappingModel.createEntityInstance(ESelect_constraint.class);
			newSelectConstraint.setAttribute(null, attribute);
			ADefined_type dataTypes = newSelectConstraint.createData_type(null);
			Iterator dataTypesIter = dataTypeList.iterator();
			while(dataTypesIter.hasNext()) {
				EDefined_type entity = (EDefined_type)dataTypesIter.next();
				dataTypes.addByIndex(1, entity);
			}
			selectConstraintMap.put(selectConstraintKey, newSelectConstraint);
		}
		return newSelectConstraint;
	}

	public EInstance_equal createInstanceEqual(EEntity element1, EEntity element2)
		throws SdaiException {
		ObjectObjectKey instanceEqualKey = new ObjectObjectKey(element1, element1);
		EInstance_equal newInstanceEqual = (EInstance_equal)
			instanceEqualMap.get(instanceEqualKey);
		if(newInstanceEqual == null) {
			newInstanceEqual = (EInstance_equal)
				parser.mappingModel.createEntityInstance(EInstance_equal.class);
			newInstanceEqual.setElement1(null, element1);
			newInstanceEqual.setElement2(null, element2);
			instanceEqualMap.put(instanceEqualKey, newInstanceEqual);
		}
		return newInstanceEqual;
	}

	public EEntity_constraint createEntityConstraint(EEntity attribute, EEntity_definition domain)
		throws SdaiException {
		ObjectObjectKey entityConstraintKey = new ObjectObjectKey(attribute, domain);
		EEntity_constraint newEntityConstraint = (EEntity_constraint)
			entityConstraintMap.get(entityConstraintKey);
		if(newEntityConstraint == null) {
			newEntityConstraint = (EEntity_constraint)
				parser.mappingModel.createEntityInstance(EEntity_constraint.class);
			newEntityConstraint.setDomain(null, domain);
			newEntityConstraint.setAttribute(null, attribute);
			entityConstraintMap.put(entityConstraintKey, newEntityConstraint);
		}
		return newEntityConstraint;
	}

	public EExact_entity_constraint createExactEntityConstraint(EEntity attribute, 
																EEntity_definition domain)
		throws SdaiException {
		ObjectObjectKey entityConstraintKey = new ObjectObjectKey(attribute, domain);
		EExact_entity_constraint newEntityConstraint = (EExact_entity_constraint)
			exactEntityConstraintMap.get(entityConstraintKey);
		if(newEntityConstraint == null) {
			newEntityConstraint = (EExact_entity_constraint)
				parser.mappingModel.createEntityInstance(EExact_entity_constraint.class);
			newEntityConstraint.setDomain(null, domain);
			newEntityConstraint.setAttribute(null, attribute);
			exactEntityConstraintMap.put(entityConstraintKey, newEntityConstraint);
		}
		return newEntityConstraint;
	}

	public EType_constraint createTypeConstraint(EEntity_definition domain, EEntity constraints)
		throws SdaiException {
		ObjectObjectKey typeConstraintKey = new ObjectObjectKey(domain, constraints);
		EType_constraint newTypeConstraint = (EType_constraint)
			typeConstraintMap.get(typeConstraintKey);
		if(newTypeConstraint == null) {
			newTypeConstraint = (EType_constraint)
				parser.mappingModel.createEntityInstance(EType_constraint.class);
			newTypeConstraint.setDomain(null, domain);
			if(constraints != null) {
				newTypeConstraint.setConstraints(null, constraints);
			}
			typeConstraintMap.put(typeConstraintKey, newTypeConstraint);
		}
		return newTypeConstraint;
	}

	public EExact_type_constraint createExactTypeConstraint(EEntity_definition domain,
															EEntity constraints)
		throws SdaiException {
		ObjectObjectKey typeConstraintKey = new ObjectObjectKey(domain, constraints);
		EExact_type_constraint newTypeConstraint = (EExact_type_constraint)
			exactTypeConstraintMap.get(typeConstraintKey);
		if(newTypeConstraint == null) {
			newTypeConstraint = (EExact_type_constraint)
				parser.mappingModel.createEntityInstance(EExact_type_constraint.class);
			newTypeConstraint.setDomain(null, domain);
			if(constraints != null) {
				newTypeConstraint.setConstraints(null, constraints);
			}
			exactTypeConstraintMap.put(typeConstraintKey, newTypeConstraint);
		}
		return newTypeConstraint;
	}

	public EIntersection_constraint createIntersectionConstraint(Set subpaths)
		throws SdaiException {
		EIntersection_constraint newIntersectionConstraint = (EIntersection_constraint)
			intersectionConstraintMap.get(subpaths);
		if(newIntersectionConstraint == null) {
			newIntersectionConstraint = (EIntersection_constraint)
				parser.mappingModel.createEntityInstance(EIntersection_constraint.class);
			AConstraint_select subpathAgg = newIntersectionConstraint.createSubpaths(null);
			Iterator subpathIter = subpaths.iterator();
			while(subpathIter.hasNext()) {
				EEntity subpath = (EEntity)subpathIter.next();
				subpathAgg.addUnordered(subpath);
			}
			intersectionConstraintMap.put(subpaths, newIntersectionConstraint);
		}
		return newIntersectionConstraint;
	}

	public ENegation_constraint createNegationConstraint(EEntity constraints)
		throws SdaiException {
		ENegation_constraint newNegationConstraint = (ENegation_constraint)
			negationConstraintMap.get(constraints);
		if(newNegationConstraint == null) {
			newNegationConstraint = (ENegation_constraint)
				parser.mappingModel.createEntityInstance(ENegation_constraint.class);
			newNegationConstraint.setConstraints(null, constraints);
			negationConstraintMap.put(constraints, newNegationConstraint);
		}
		return newNegationConstraint;
	}

	public EEnd_of_path_constraint createEndOfPathConstraint(EEntity constraints)
		throws SdaiException {
		EEnd_of_path_constraint newEndOfPathConstraint = (EEnd_of_path_constraint)
			endOfPathConstraintMap.get(constraints);
		if(newEndOfPathConstraint == null) {
			newEndOfPathConstraint = (EEnd_of_path_constraint)
				parser.mappingModel.createEntityInstance(EEnd_of_path_constraint.class);
			newEndOfPathConstraint.setConstraints(null, constraints);
			endOfPathConstraintMap.put(constraints, newEndOfPathConstraint);
		}
		return newEndOfPathConstraint;
	}

	public EAnd_constraint_relationship createAndConstraintRelationship(EEntity element1,
																		EEntity element2)
		throws SdaiException {
		ObjectObjectKey andConstraintRelationshipKey = new ObjectObjectKey(element1, element2);
		EAnd_constraint_relationship newAndConstraint = (EAnd_constraint_relationship)
			andConstraintRelationshipMap.get(andConstraintRelationshipKey);
		if(newAndConstraint == null) {
			newAndConstraint = (EAnd_constraint_relationship)
				parser.mappingModel.createEntityInstance(EAnd_constraint_relationship.class);
			newAndConstraint.setElement1(null, element1);
			newAndConstraint.setElement2(null, element2);
			andConstraintRelationshipMap.put(andConstraintRelationshipKey, newAndConstraint);
		}
		return newAndConstraint;
	}

	public EOr_constraint_relationship createOrConstraintRelationship(EEntity element1,
																	  EEntity element2)
		throws SdaiException {
		ObjectObjectKey orConstraintRelationshipKey = new ObjectObjectKey(element1, element2);
		EOr_constraint_relationship newOrConstraint = (EOr_constraint_relationship)
			orConstraintRelationshipMap.get(orConstraintRelationshipKey);
		if(newOrConstraint == null) {
			newOrConstraint = (EOr_constraint_relationship)
				parser.mappingModel.createEntityInstance(EOr_constraint_relationship.class);
			newOrConstraint.setElement1(null, element1);
			newOrConstraint.setElement2(null, element2);
			orConstraintRelationshipMap.put(orConstraintRelationshipKey, newOrConstraint);
		}
		return newOrConstraint;
	}

	static private class ObjectObjectKey {
		private Object object1;
		private Object object2;

		ObjectObjectKey(Object object1, Object object2) {
			this.object1 = object1;
			this.object2 = object2;
		}

		public boolean equals(Object obj) {
			if(obj instanceof ObjectObjectKey) {
				ObjectObjectKey other = (ObjectObjectKey)obj;
				return new EqualsBuilder()
					.append(object1, other.object1)
					.append(object2, other.object2)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(9803, 10463)
				.append(object1)
				.append(object2)
				.toHashCode();
		}
	}

	static private class ObjectIntKey {
		private Object object;
		private int intVal;

		ObjectIntKey(Object object, int intVal) {
			this.object = object;
			this.intVal = intVal;
		}

		public boolean equals(Object obj) {
			if(obj instanceof ObjectIntKey) {
				ObjectIntKey other = (ObjectIntKey)obj;
				return new EqualsBuilder()
					.append(object, other.object)
					.append(intVal, other.intVal)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(12527, 12907)
				.append(object)
				.append(intVal)
				.toHashCode();
		}
	}

	static private class ObjectDoubleKey {
		private Object object;
		private double doubleValue;

		ObjectDoubleKey(Object object, double doubleValue) {
			this.object = object;
			this.doubleValue = doubleValue;
		}

		public boolean equals(Object obj) {
			if(obj instanceof ObjectDoubleKey) {
				ObjectDoubleKey other = (ObjectDoubleKey)obj;
				return new EqualsBuilder()
					.append(object, other.object)
					.append(doubleValue, other.doubleValue)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(13711, 14591)
				.append(object)
				.append(doubleValue)
				.toHashCode();
		}
	}

	static private class ObjectBooleanKey {
		private Object object;
		private boolean booleanValue;

		ObjectBooleanKey(Object object, boolean booleanValue) {
			this.object = object;
			this.booleanValue = booleanValue;
		}

		public boolean equals(Object obj) {
			if(obj instanceof ObjectBooleanKey) {
				ObjectBooleanKey other = (ObjectBooleanKey)obj;
				return new EqualsBuilder()
					.append(object, other.object)
					.append(booleanValue, other.booleanValue)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(15443, 16267)
				.append(object)
				.append(booleanValue)
				.toHashCode();
		}
	}

} // ConstraintFactory
