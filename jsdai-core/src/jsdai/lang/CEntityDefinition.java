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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** It is a supertype of the class <code>CEntity_definition</code> contained in the
 * <code>jsdai.dictionary</code> package. This class is designed primarily
 * to hold some non-public data fields and a number of non-public methods
 * mainly used for processing of values of these fields and for creation of the
 * data dictionary during opening of the session. Also, the public methods
 * {@link EntityDefinition#isSubtypeOf isSubtypeOf} and
 * {@link EntityDefinition#isDomainEquivalentWith isDomainEquivalentWith} defined in
 * "ISO 10303-22: Product data representation and exchange: Standard data access interface"
 * and additional public methods
 * {@link EntityDefinition#getExplicit_attributes getExplicit_attributes} and
 * {@link EntityDefinition#testExplicit_attributes testExplicit_attributes},
 * which are not a part of the standard, are implemented here.
 */
public abstract class CEntityDefinition extends DataType implements EEntity_definition {

	// duplicate the fields from CEntity_definition
	protected String name; // equals  "a0"
	protected int complex; // equals  "a4"

	protected int noOfAllAttributes;    // do not understand for what this field
//	protected int noOfPartialAttributes; // number of attributes in exchange structure
	protected int noOfPartialAttributes = -1; // number of attributes in exchange structure
	protected int noOfPartialEntityTypes; // number of partial entity types
	protected int [] internalMappingIndexing; // external to internal
	protected int [] externalMappingIndexing; // internal to external
	protected CEntity_definition [] partialEntityTypes; // ordered alphabetically
	String goodName;
	String nameUpperCase;
	String nameExpress;

	static final int NUMBER_OF_PARTIAL_TYPES = 16;
	private static final int NUMBER_OF_ATTRIBUTES = 16;
	private static final int NUMBER_OF_DEFINED_TYPES = 16;
	private static final int MAX_LEVEL_OF_NESTING = 10;
	static final int RULES_ARRAY_SIZE = 64;

	static final String GET_METHOD_PREFIX = "get";

	Class entityClass; // entity class, e.g. CXxx
	private Class entityInterface; // entity interface, e.g. EXxx
	private Class entityAggregate; // entity aggregate, e.g. AXxx
	private Class [] entityNestedAggregate;  // e.g. [AXxx, AaXxx, AaaXxx, null, null...]

	private SdaiIterator iter;

	AEntity_definition singleEntityTypes;
//	static EAttribute [] attrs;
	CEntity_definition [] leaves;
	int [] indices2supertypes;
	CEntity_definition [] supertypes;
	AExplicit_attribute expl_attribs;
	CExplicit_attribute [] attributesExpl;
	CDerived_attribute [] attributesDeriv;
	CInverse_attribute [] attributesInv;
	CInverse_attribute [] attributesInvAll;
	CDefined_type [] def_types;
	EAttribute [] def_types_attr;
	int [] def_types_branch;
	CWhere_rule w_rules_array [];
	AAttribute not_expl_attribs;
//	AExplicit_attribute attribs;
	int index; // index for the SdaiModel-extent to which this belongs. set in SchemaData.init
	int toComplex;
	int attr_start_index;
	int inv_count;
	int leaf_count;
	int supertypes_count;
	boolean used;
	boolean der_attr_exist;
	boolean inv_attr_exist;
	boolean used_for_roles;
//	boolean del_supertype;

	// listing of all explicit attributes 0..x as they where generate in the
	// early binding entity classes.
	// the dimension of these aggregates is the no. of explicit attributs.
	// needs to be set during setEarlyBinding()
	CExplicit_attribute attributes[]; // a0$, a1$, ...
	CInverse_attribute attributesInverse[]; // inverse attributes
	CDerived_attribute attributesDerived[]; // derived attributes
	EAttribute attributesRedecl[];
	Class attributesDerivedClass[];
	Method attributesDerivedMethod[];
	Object attributesDerivedMethodValue[];
	String attributesDerivedMethodName[];
	Field attributeFields[];  // a0, a1, ...
	Field attributeFieldSelects[]; // only set for selects: a0$$, a1$$, ...
	CEntity_definition fieldOwners[];  // for each attribute the entity owning it
	protected SSuper ssuper;


	SchemaData typeOf_schema;
	CDefined_type [] typeOf_types = null;


	protected CEntityDefinition() {
		super();
	}

	protected String getNameUpperCase() {
//		synchronized (syncObject) {
		if (nameUpperCase == null) {
			try {
				nameUpperCase = getCorrectName().toUpperCase();
			} catch (Exception ex) {
				return null;
			}
		}
		return nameUpperCase;
//		} // syncObject
	}


	protected String getCorrectName() {
//		synchronized (syncObject) {
		if (goodName == null) {
			goodName = name.replace('+', '$');
		}
		return goodName;
//		} // syncObject
	}


	protected String getNameExpress() {
//		synchronized (syncObject) {
		if (nameExpress == null) {
			try {
				nameExpress = name.toUpperCase();
			} catch (Exception ex) {
				return null;
			}
		}
		return nameExpress;
//		} // syncObject
	}


	public boolean isSubtypeOf(EEntity_definition compType) throws SdaiException {
//		synchronized (syncObject) {
		int i;
		if (compType == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (complex == 2 /*true*/) {
			if (this == compType) {
				return true;
			}
			StaticFields staticFields = null;
			if (partialEntityTypes == null) {
				if(staticFields == null) {
					staticFields = StaticFields.get();
				}
				prepareExternalMappingData(staticFields);
			}
			CEntityDefinition def_super = (CEntityDefinition)compType;
			if (def_super.complex == 2) {
				if (def_super.partialEntityTypes == null) {
					if(staticFields == null) {
						staticFields = StaticFields.get();
					}
					def_super.prepareExternalMappingData(staticFields);
				}
				for (i = 0; i < def_super.partialEntityTypes.length; i++) {
					boolean found = false;
					for (int j = 0; j < partialEntityTypes.length; j++) {
						if (def_super.partialEntityTypes[i] == partialEntityTypes[j]) {
							found = true;
							break;
						}
					}
					if (!found) {
						return false;
					}
				}
				return true;
			} else {
				for (i = 0; i < partialEntityTypes.length; i++) {
					if (partialEntityTypes[i] == compType) {
						return true;
					}
				}
			}
			return false;
		} else {
			return IsInSupertypeList((CEntity_definition)this, (CEntity_definition)compType);
//			return IsInSubtypesList(this, (CEntityDefinition)compType); // sometimes this implementation is faster
//if (b1 != b2) {
//System.out.println("CEntityDefinition *** differ for this: " + ((CEntity_definition)this).getName(null) +
//"   and compType: " + ((CEntity_definition)compType).getName(null));
//}
//return b1;
		}
//		} // syncObject
	}
	private boolean IsInSubtypesList(CEntityDefinition def, CEntityDefinition compType)
			throws SdaiException {
		SchemaData sch_data = owning_model.schemaData;
		int super_index;
		if (sch_data == compType.owning_model.schemaData) {
			super_index = compType.index;
		} else {
			super_index = sch_data.find_entity(0, sch_data.sNames.length - 1, compType);
		}
		if (super_index < 0) {
			return false;
		}
		int sub_index = def.index;
		if (sub_index < 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
				((CEntity_definition)def).getName(null) + " (used in model: " + owning_model.name + ")");
		}
		if (sub_index == super_index) {
			return true;
		}
		int subtypes [] = sch_data.schema.getSubtypes(super_index);
		if (subtypes != null) {
			int left = 0;
			int right = subtypes.length - 1;
			if (right >= 0) {
				while (left <= right) {
					int middle = (left + right)/2;
					int comp_res = subtypes[middle] - sub_index;
					if (comp_res < 0) {
						left = middle + 1;
					} else if (comp_res > 0) {
						right = middle - 1;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}
	private boolean IsInSupertypeList(CEntity_definition def, CEntity_definition compType)
			throws SdaiException {
		if (def == compType) {
			return true;
		}
//		AEntity_definition list_of_definitions = def.getSupertypes(null);
		Object [] myDataA;
		AEntity list_of_defs = def.getSupertypes(null);
		if (list_of_defs.myLength < 0) {
			list_of_defs.resolveAllConnectors();
		}
		if (list_of_defs.myLength == 1) {
			return IsInSupertypeList((CEntity_definition)list_of_defs.myData, compType);
		} else if (list_of_defs.myLength == 2) {
			myDataA = (Object [])list_of_defs.myData;
			if (IsInSupertypeList((CEntity_definition)myDataA[0], compType)) {
				return true;
			}
			if (IsInSupertypeList((CEntity_definition)myDataA[1], compType)) {
				return true;
			}
		} else {
			ListElement element;
			if (list_of_defs.myLength <= CAggregate.SHORT_AGGR) {
				element = (ListElement)list_of_defs.myData;
			} else {
				myDataA = (Object [])list_of_defs.myData;
				element = (ListElement)myDataA[0];
			}
			while (element != null) {
				if (IsInSupertypeList((CEntity_definition)element.object, compType)) {
					return true;
				}
				element = element.next;
			}
		}
		return false;
//		for (int i = 1; i <= list_of_definitions.getMemberCount(); i++) {
//			boolean found =
//				IsInSupertypeList((CEntity_definition)list_of_definitions.getByIndex(i), compType);
//			if (found) {
//				return true;
//			}
//		}
//		return false;
	}


	public boolean isDomainEquivalentWith(EEntity_definition compType)
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

//	protected int getEntityExtentIndex() {
//		return  SdaiSession.ENTITY_DEFINITION;
//	}

	AEntity_definition getTypes() throws SdaiException {
		if (singleEntityTypes == null) {
			singleEntityTypes = new AEntity_definition();
			for (int i = 0; i < noOfPartialEntityTypes; i++) {
				((AEntity)singleEntityTypes).addAtTheEnd(partialEntityTypes[i], null);
//				singleEntityTypes.addByIndex(i + 1, partialEntityTypes[i]);
			}
		}
		return singleEntityTypes;
	}

	static Class getEntityInterface(SchemaDefinition schemaDef, String entity) throws SdaiException {
		SSuper sup = schemaDef.owning_model.schemaData.super_inst;
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String s = str.substring(0, index) + ".E" + normalise(entity);
		try {
			return SdaiClassLoaderProvider.getDefault().getClassLoader().loadClass(s);
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR,
				"Early binding entity-class class not found: " + s);
		}
	}

	static Class getEntityClass(SchemaDefinition schemaDef, String entity) throws SdaiException {
		SSuper sup = schemaDef.owning_model.schemaData.super_inst;
//if (sup == null) System.out.println("  CEntityDefinition sup is NULL for  model: " + model.name);
//else System.out.println("  CEntityDefinition sup is POS for  model: " + model.name);
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String s = str.substring(0, index) + ".C" + normalise(entity);
		try {
			return Class.forName(s, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR,
				"Early binding entity-class class not found: " + s);
		}
	}

	static Class getEntityAggregate(SchemaDefinition schemaDef, String entity) throws SdaiException {
		SdaiModel model = schemaDef.owning_model;
		SSuper sup = model.schemaData.super_inst;
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String s = str.substring(0, index) + ".A" + normalise(entity);
		try {
			return Class.forName(s, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR,
				"Early binding entity-aggregate class not found: " + s);
		}
	}

	static Class getEntityNestedAggregate(SchemaDefinition schemaDef, String entity, int level)
			throws SdaiException {
		SdaiModel model = schemaDef.owning_model;
		SSuper sup = model.schemaData.super_inst;
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String many_a = ".A";
		for (int i = 1; i < level; i++) {
			many_a += "a";
		}
		String s = str.substring(0, index) + many_a + normalise(entity);
if (SdaiSession.debug2) System.out.println(" In EntityDefinition s = " + s);
		try {
			return SdaiClassLoaderProvider.getDefault().getClassLoader().loadClass(s);
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR,
				"Early binding entity-aggregate class not found: " + s);
		}
	}

	Class getEntityInterface() throws SdaiException {
		if (entityInterface == null) {
			entityInterface = getEntityInterface(owning_model.described_schema, this.getCorrectName());
		}
		return entityInterface;
	}

	Class getEntityClass() throws SdaiException {
		if (entityClass == null) {
			entityClass = getEntityClass(owning_model.described_schema, this.getCorrectName());
		}
		return entityClass;
	}

	Class getEntityAggregate() throws SdaiException {
		if (entityAggregate == null) {
			if (complex == 2 /*true*/) {
				entityAggregate = AEntity.class;
			} else {
				entityAggregate = getEntityAggregate(owning_model.described_schema, this.getCorrectName());
			}
		}
		return entityAggregate;
	}

	Class getEntityNestedAggregate(int level) throws SdaiException {
		if (entityNestedAggregate == null) {
			entityNestedAggregate = new Class[MAX_LEVEL_OF_NESTING];
		}
		if (level > entityNestedAggregate.length) {
			throw new SdaiException(SdaiException.SY_ERR,
				"Aggregates of nesting level " + level + " are not supported");
		}
		int ind = level - 1;
		if (entityNestedAggregate[ind] == null) {
			entityNestedAggregate[ind] = getEntityNestedAggregate(owning_model.described_schema, this.getCorrectName(), level);
		}
		return entityNestedAggregate[ind];
	}

	private void setEarlyBinding1(EEntity_definition entity, AEntity_definition result)
			throws SdaiException {
// process all supertypes recursively
		if (!(result.isMember(entity))) {
//			AEntity_definition supertypes = entity.getSupertypes(null);
			AEntity supertypes = entity.getSupertypes(null);
			if (supertypes.myLength < 0) {
				supertypes.resolveAllConnectors();
			}
			Object [] myDataA;
			if (supertypes.myLength == 1) {
				setEarlyBinding1((EEntity_definition)supertypes.myData, result);
			} else if (supertypes.myLength == 2) {
				myDataA = (Object [])supertypes.myData;
				setEarlyBinding1((EEntity_definition)myDataA[0], result);
				setEarlyBinding1((EEntity_definition)myDataA[1], result);
			} else {
				ListElement element;
				if (supertypes.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)supertypes.myData;
				} else {
					myDataA = (Object [])supertypes.myData;
					element = (ListElement)myDataA[0];
				}
				while (element != null) {
					setEarlyBinding1((EEntity_definition)element.object, result);
					element = element.next;
				}
			}
//			for (int i = 1; i <= ((AEntity)supertypes).myLength; i++) {
//				setEarlyBinding1(supertypes.getByIndex(i), result);
//			}
			if (((CEntity_definition)entity).complex != 2 /*!true*/) {
				((AEntity)result).addAtTheEnd(entity, null);
//				result.addByIndex(((AEntity)result).myLength + 1, entity);
			}
		}
	}

	final AEntity_definition prepareExternalMappingData(StaticFields staticFields) throws SdaiException {
		AEntity_definition result = new AEntity_definition();
		setEarlyBinding1(this, result);
/*int lnn = result.getMemberCount();
System.out.println("  &&&&& entity: " + getName(null) + "   count = " + lnn +
"  complex?: " + getComplex(null));
boolean rep = false;
for (int j = 1; j <= lnn; j++) {
CEntity_definition endef = (CEntity_definition)result.getByIndex(j);
for (int k = 1; k < j; k++) {
if (endef == result.getByIndex(k)) rep = true;
}
System.out.print("  E: " + endef.getName(null));
}
System.out.println();
if (rep) System.out.println("  ????? REPETITION");*/

		int res_length = ((AEntity)result).myLength;
		if (res_length > staticFields.names.length) {
//			enlargeNames(res_length, staticFields);
			enlargeNamesExt(res_length, staticFields);
		}
		externalMappingIndexing = new int[res_length];
		internalMappingIndexing = new int[res_length];
		partialEntityTypes = new CEntity_definition[res_length];

		int i = 0;
		if (res_length < 1) {
		} else if (res_length == 1) {
			internalMappingIndexing[0] = 0;
			staticFields.part_types[0] = (CEntity_definition)((AEntity)result).myData;
			staticFields.names[0] = staticFields.part_types[0].getNameUpperCase();
//			staticFields.names[0] = ((CEntity_definition)((AEntity)result).myData).getNameUpperCase();
		} else if (res_length == 2) {
			Object [] myDataA = (Object [])((AEntity)result).myData;
			for (i = 0; i < 2; i++) {
				internalMappingIndexing[i] = i;
				staticFields.part_types[i] = (CEntity_definition)myDataA[i];
				staticFields.names[i] = ((CEntity_definition)myDataA[i]).getNameUpperCase();
			}
		} else {
			if (staticFields.it == null) {
				staticFields.it = result.createIterator();
			} else {
				result.attachIterator(staticFields.it);
			}
			while (staticFields.it.next()) {
//				names[i - 1] = ((CEntity_definition)result.getCurrentMemberObject(it)).getNameUpperCase();
//			for (i = 1; i <= res_length; i++) {
//				internalMappingIndexing[i - 1] = i - 1;
				internalMappingIndexing[i] = i;
//				names[i - 1] = ((CEntity_definition)result.getByIndex(i)).getNameUpperCase();
				staticFields.part_types[i] = (CEntity_definition)((ListElement)staticFields.it.myElement).object;
				staticFields.names[i] = staticFields.part_types[i].getNameUpperCase();
//				staticFields.names[i] = ((CEntity_definition)((ListElement)staticFields.it.myElement).object).getNameUpperCase();
				i++;
			}
		}

		boolean mark = true;
		String str;
		int index, new_index;
		int k = res_length - 2;
		while (mark) {
			mark = false;
			for (i = 0; i <= k; i++) {
				if (staticFields.names[i].compareTo(staticFields.names[i + 1]) > 0) {
					str = staticFields.names[i];
					staticFields.names[i] = staticFields.names[i + 1];
					staticFields.names[i + 1] = str;
					index = internalMappingIndexing[i];
					internalMappingIndexing[i] = internalMappingIndexing[i + 1];
					internalMappingIndexing[i + 1] = index;
					mark = true;
				}
			}
			k--;
		}
		for (i = 0; i < res_length; i++) {
			externalMappingIndexing[internalMappingIndexing[i]] = i;
			partialEntityTypes[i] = staticFields.part_types[internalMappingIndexing[i]];
//			partialEntityTypes[i] = (CEntity_definition)result.getByIndex(internalMappingIndexing[i] + 1);
		}
		noOfPartialEntityTypes = res_length;
/*System.out.println("  CEntityDefinition partial entities for entity: " + getName(null) +
"   count = " + result.myLength);
for (int j = 0; j < partialEntityTypes.length; j++) {
System.out.print("  P: " + partialEntityTypes[j].getName(null));
}
System.out.println();*/
		return result;
	}


/** The method prepares information of the explicit attributes of this entity in a convenient form.
  This information is used, in particular, in attribute access methods.
  This method is invoked during its initialization (static) by
  every entity class CXxx/definition = initEntityDefinition(...)  -> setEarlyBinding(...)
*/
	final void setEarlyBinding(SdaiModel model) throws SdaiException {
		int i, j;
		int index, index_for_this = -1;
		int alternative;
		boolean found;
		Class cl, cl_next, cl_this = null, cl_current;
		Class if_cl = null;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.param_ed == null) {
			staticFields.param_ed = new Class[1];
		}
		AEntity_definition result = null;
// Preparing aggregate with partial entity types
		if (partialEntityTypes == null || complex != 2 /*!true*/) {
			result = prepareExternalMappingData(staticFields);
		}
		CExplicit_attribute [] partial_attrs;
		CDerived_attribute [] der_attrs;
		CInverse_attribute [] inv_attrs;
		CEntity_definition entity;
		CEntity_definition owner = null;
		String attr_name;
		int position = 0;
		int deriv_count = 0;
		int inv_count = 0;
		SchemaData sch_data = owning_model.schemaData;

// Counting the explicit non-redeclared attributes
		if (complex != 2 /*true*/) {
			iter = result.createIterator();
		}
		j = -1;
		while (j < noOfPartialEntityTypes - 1) {
			j++;
			if (complex == 2 /*true*/) {
				entity = partialEntityTypes[j];
			} else {
				iter.next();
				if (((AEntity)result).myLength <= 2) {
					entity = (CEntity_definition)iter.myElement;
				} else {
					entity = (CEntity_definition)((ListElement)iter.myElement).object;
				}
			}
			partial_attrs = ((CEntityDefinition)entity).takeExplicit_attributes();
			for (i = 0; i < partial_attrs.length; i++) {
				if (!partial_attrs[i].testRedeclaring(null)) {
					position++;
				}
			}
			if (((CEntityDefinition)entity).der_attr_exist) {
				der_attrs = ((CEntityDefinition)entity).takeDerived_attributes();
				for (i = 0; i < der_attrs.length; i++) {
					if (!der_attrs[i].testRedeclaring(null)) {
						deriv_count++;
					} else {
						EEntity redecl_attr = (der_attrs[i]).getRedeclaring(null);
						if (((AttributeDefinition)redecl_attr).attr_tp == AttributeDefinition.EXPLICIT) {
//						if (redecl_attr instanceof CExplicit_attribute) {
							deriv_count++;
						}
					}
				}
			}
			if (((CEntityDefinition)entity).inv_attr_exist) {
				inv_attrs = ((CEntityDefinition)entity).takeInverse_attributes();
				inv_count += inv_attrs.length;
			}
		}
//System.out.println(" CEntityDefinition  attributes CREATED  for  name: " + getName(null) +
//"  owning_model: " + ((CEntity)this).owning_model.name);

// Allocating arrays for attributes

		attributes = new CExplicit_attribute[position];
		attributeFields = new Field[position];
		attributeFieldSelects = new Field[position];
		fieldOwners = new CEntity_definition[position];
		if (deriv_count > 0) {
			attributesDerived = new CDerived_attribute[deriv_count];
			attributesDerivedClass = new Class[deriv_count];
			attributesDerivedMethod = new Method[deriv_count];
			attributesDerivedMethodValue = new Object[deriv_count];
			attributesDerivedMethodName = new String[deriv_count];
		}
		if (inv_count > 0) {
			attributesInverse = new CInverse_attribute[inv_count];
		}
		if (complex == 2 /*true*/) {
			owner = (CEntity_definition)this;
		}
		position = 0;
		deriv_count = 0;
		inv_count = 0;
		if (complex != 2 /*true*/) {
			result.attachIterator(iter);
		}
		j = -1;
		while (j < noOfPartialEntityTypes - 1) {
			j++;
			if (complex == 2 /*true*/) {
				entity = partialEntityTypes[j];
				alternative = 0;
			} else {
				iter.next();
/*if (iter.myElement == null) {
System.out.println(" CEntityDefinition iter.myAggregate: " +
iter.myAggregate.getClass().getName() +
"  result.myLength: " + result.getMemberCount() +
"   result: " + result.getClass().getName());
System.out.println(" CEntityDefinition this entity: " + ((CEntity_definition)this).getName(null));
CEntity_definition eee = (CEntity_definition)result.getByIndex(j + 1);
System.out.println(" CEntityDefinition eee: " + eee.getName(null) + "  j = " + j);
}*/
				if (((AEntity)result).myLength <= 2) {
					owner = entity = (CEntity_definition)iter.myElement;
				} else {
					owner = entity = (CEntity_definition)((ListElement)iter.myElement).object;
				}
				if (entity == this) {
					alternative = 0;
				} else {
					alternative = 1;
				}
			}
			partial_attrs = ((CEntityDefinition)entity).takeExplicit_attributes();
			SdaiModel error_mod;
			if (partial_attrs.length <= 0) {
				entity.noOfPartialAttributes = 0;
				if (!((CEntityDefinition)entity).der_attr_exist && !((CEntityDefinition)entity).inv_attr_exist) {
					continue;
				}
			}
			if (((CEntityDefinition)entity).der_attr_exist) {
				if (complex == 2 /*true*/) {
					SchemaData sch_data_compl = entity.owning_model.schemaData;
					index = sch_data_compl.findEntityExtentIndex(entity);
					if (index < 0) {
						error_mod = sch_data_compl.model;
						throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
							entity.getName(null) + " (used in model: " + error_mod.name + ")");
					}
					if_cl = sch_data_compl.getEntityInterfaceByIndex(index);
					index = sch_data.findEntityExtentIndex(this);
					if (index < 0) {
						error_mod = sch_data.model;
						throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
							((CEntity_definition)this).getName(null) + " (used in model: " + error_mod.name + ")");
					}
				} else {
					index = sch_data.findEntityExtentIndex(entity);
					if (index < 0) {
						error_mod = sch_data.model;
						throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
							entity.getName(null) + " (used in model: " + error_mod.name + ")");
					}
					if_cl = sch_data.getEntityInterfaceByIndex(index);
				}
//System.out.println("CEntityDefinition  this entity: " + getName(null) +
//"   partial entity: " + entity.getName(null));
			} else {
				if (complex == 2 /*true*/) {
					index = sch_data.findEntityExtentIndex(this);
					if (index < 0) {
						error_mod = sch_data.model;
						throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
							((CEntity_definition)this).getName(null) + " (used in model: " + error_mod.name + ")");
					}
				} else {
					index = sch_data.findEntityExtentIndex(entity);
					if (index < 0) {
						error_mod = sch_data.model;
						throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
							entity.getName(null) + " (used in model: " + error_mod.name + ")");
					}
				}
			}
			cl = sch_data.getEntityClassByIndex(index);
			if (partial_attrs.length > 0 || ((CEntityDefinition)entity).der_attr_exist ||
					((CEntityDefinition)entity).inv_attr_exist) {
				if (partial_attrs.length > 0 && alternative == 0) {
					entity.noOfPartialAttributes = 0;
				}

// Case when partial entity is different from the entity itself
				if (alternative > 0) {
					found = false;
					if (index_for_this < 0) {
						index_for_this = sch_data.findEntityExtentIndex(this);
						if (index_for_this < 0) {
							throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
						}
						cl_this = sch_data.getEntityClassByIndex(index_for_this);
					}
					cl_next = cl_this;
					while (cl_next != CEntity.class) {
						if (cl_next == cl) {
							found = true;
							break;
						}
						cl_next = cl_next.getSuperclass();
					}
					if (!found) {
						cl_current = cl_this;
						cl_next = getEntitySupertypeClass(cl_this);
						CEntity_definition current_entity = (CEntity_definition)this;
						while (cl_next != CEntity.class) {
							CEntity_definition super_entity = sch_data.findEntity(cl_next);
							boolean res = findPartialEntity(current_entity, super_entity, entity);
							if (res) {
								cl = cl_current;
//								if (cl == cl_this) {
								alternative = -1;
								owner = current_entity;
//								} else {
//									alternative = 1;
//								}
//System.out.println("  HOST class: " + cl.getName() +
//"  this entity: " + ((CEntity_definition)this).getName(null));
								found = true;
//								break;
							}
							cl_current = cl_next;
							cl_next = getEntitySupertypeClass(cl_current);
							current_entity = super_entity;
						}
					}
					if (!found) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
// Going through all partial attributes
				for (i = 0; i < partial_attrs.length; i++) {
					if (!partial_attrs[i].testRedeclaring(null)) {
						if (alternative == 0) {
							entity.noOfPartialAttributes++;
						}
//if (((CEntity_definition)this).getName(null).equals("si_unit"))
//System.out.println("CEntityDefinition +++++++++++ i: " + i +
//"   attrib: " + attrib.getName(null));
						attributes[position] = partial_attrs[i];
						if (checkIfDerived(partial_attrs[i])) {
							position++;
							continue;
						}
						attr_name = "a" + position;
						switch (alternative) {
							case -1:
							case 0:
								try {
									attributeFields[position] = cl.getDeclaredField(attr_name);
									fieldOwners[position] = owner;
								} catch (java.lang.NoSuchFieldException ex) {
									throw new SdaiException(SdaiException.SY_ERR, ex);
								}
								try {
									attributeFieldSelects[position] = cl.getDeclaredField(attr_name + "$$");
								} catch (java.lang.NoSuchFieldException ex) {
								}
								break;
							case 1:
								found = false;
								for (int k = 0; k <= ((CEntityDefinition)entity).attributes.length; k++) {
									if (partial_attrs[i] == ((CEntityDefinition)entity).attributes[k]) {
										attributeFields[position] = ((CEntityDefinition)entity).attributeFields[k];
										attributeFieldSelects[position] = ((CEntityDefinition)entity).attributeFieldSelects[k];
										fieldOwners[position] = ((CEntityDefinition)entity).fieldOwners[k];
										found = true;
										break;
									}
								}
								if (!found) {
									throw new SdaiException(SdaiException.SY_ERR);
								}
								break;
						}
						position++;
					}
				}
			}

			if (((CEntityDefinition)entity).der_attr_exist) {
				der_attrs = ((CEntityDefinition)entity).takeDerived_attributes();
				for (i = 0; i < der_attrs.length; i++) {
					Class true_if_cl;
					if (der_attrs[i].testRedeclaring(null)) {
						EEntity redecl = der_attrs[i].getRedeclaring(null);
						if (((AttributeDefinition)redecl).attr_tp == AttributeDefinition.DERIVED) {
							continue;
						} else {
							if (checkRepetition(deriv_count, redecl)) {
								continue;
							}
							EExplicit_attribute red = (EExplicit_attribute)redecl;
							String red_name = red.getName(null);
							while (red != null) {
								if (red.testRedeclaring(null)) {
									EExplicit_attribute red_next = red.getRedeclaring(null);
									String red_name_next = red_next.getName(null);
									if (red_name_next.equals(red_name)) {
										red = red_next;
										red_name = red_name_next;
									} else {
										redecl = red;
										break;
									}
//									red = red.getRedeclaring(null);
								} else {
									redecl = red;
									break;
								}
							}
							CEntity_definition super_type = (CEntity_definition)((CExplicit_attribute)redecl).getParent_entity(null);
							SchemaData type_sch_data = super_type.owning_model.schemaData;
							int indx = type_sch_data.findEntityExtentIndex(super_type);
							if (indx < 0) {
								throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
							}
							true_if_cl = type_sch_data.getEntityInterfaceByIndex(indx);
						}
					} else {
						true_if_cl = if_cl;
					}
					attributesDerived[deriv_count] = der_attrs[i];
					attributesDerivedClass[deriv_count] = cl;
//if (((CEntity_definition)this).getName(null).equals("generic_footprint_definition_armx"))
//System.out.println("CEntityDefinition +++++++++++ deriv_count: " + deriv_count +
//"   attrib: " + der_attrs[i].getName(null) + "   cl: " + cl.getName() +
//"  entity: " + ((CEntityDefinition)entity).getName(null));
					String m_name = GET_METHOD_PREFIX + normalise(der_attrs[i].getName(null));
					staticFields.param_ed[0] = true_if_cl;
					try {
						attributesDerivedMethod[deriv_count] = cl.getDeclaredMethod(m_name, staticFields.param_ed);
					} catch (java.lang.NoSuchMethodException ex) {
					}
					attributesDerivedMethodValue[deriv_count] = true_if_cl;
					attributesDerivedMethodName[deriv_count] = m_name;
					deriv_count++;
				}
			}

			if (((CEntityDefinition)entity).inv_attr_exist) {
				inv_attrs = ((CEntityDefinition)entity).takeInverse_attributes();
				System.arraycopy(inv_attrs, 0, attributesInverse, inv_count, inv_attrs.length);
				inv_count += inv_attrs.length;
			}
		}
		noOfAllAttributes = position;
		if (attributesDerived != null && deriv_count < attributesDerived.length) {
			CDerived_attribute [] attributesDerived_shr = new CDerived_attribute[deriv_count];
			System.arraycopy(attributesDerived, 0, attributesDerived_shr, 0, deriv_count);
			attributesDerived = attributesDerived_shr;
			Class [] attributesDerivedClass_shr = new Class[deriv_count];
			System.arraycopy(attributesDerivedClass, 0, attributesDerivedClass_shr, 0, deriv_count);
			attributesDerivedClass = attributesDerivedClass_shr;
			Method [] attributesDerivedMethod_shr = new Method[deriv_count];
			System.arraycopy(attributesDerivedMethod, 0, attributesDerivedMethod_shr, 0, deriv_count);
			attributesDerivedMethod = attributesDerivedMethod_shr;
			Object [] attributesDerivedMethodValue_shr = new Object[deriv_count];
			System.arraycopy(attributesDerivedMethodValue, 0, attributesDerivedMethodValue_shr, 0, deriv_count);
			attributesDerivedMethodValue = attributesDerivedMethodValue_shr;
			String [] attributesDerivedMethodName_shr = new String[deriv_count];
			System.arraycopy(attributesDerivedMethodName, 0, attributesDerivedMethodName_shr, 0, deriv_count);
			attributesDerivedMethodName = attributesDerivedMethodName_shr;
		}
	}

	/**
	 * Returns a JSDAI entity (CXxxx class) super-class of the specified class, or <code>null</code> if no such class is available.
	 * @param clazz specified class. Should be JSDAI C class itself.
	 * @return a JSDAI C super-class of the specified class, or <code>null</code> if no such class is available.
	 */
	private static Class getEntitySupertypeClass(Class clazz) {
		if (clazz == null) {
			return null;
		}

		Class superclass = clazz.getSuperclass();
		while (superclass != null) {
			String superclassName = superclass.getName();

			// get rid of all package names
			int pos = superclassName.lastIndexOf(".");
			if (pos > 0) {
				superclassName = superclassName.substring(pos + 1);
				// determine if class is a JSDAI C class by. this code ignores Cg, Cx and similar class names.
				if (superclassName.length() > 1 && Character.isUpperCase(superclassName.charAt(1))) {
					break;
				}
			}

			superclass = superclass.getSuperclass();
		}
		return superclass;
	}
	
	private boolean findPartialEntity(CEntity_definition base_entity, CEntity_definition super_entity,
			CEntity_definition target_entity) throws SdaiException {
		AEntity_definition supertypes = base_entity.getSupertypes(null);
		for (int j = 1; j <= ((AEntity)supertypes).myLength; j++) {
			CEntity_definition entity = (CEntity_definition)supertypes.getByIndex(j);
			if (entity == super_entity) {
				continue;
			}
			if (entity == target_entity) {
				return true;
			}
			boolean res = findPartialEntity(entity, null, target_entity);
			if (res) {
				return true;
			}
		}
		return false;
	}
	private boolean checkRepetition(int der_count, EEntity red_attr) throws SdaiException {
		EExplicit_attribute expl_red_attr = (EExplicit_attribute)red_attr;
		while (expl_red_attr.testRedeclaring(null)) {
			expl_red_attr = expl_red_attr.getRedeclaring(null);
		}
		for (int i = 0; i < der_count; i++) {
			if (!attributesDerived[i].testRedeclaring(null)) {
				continue;
			}
			EExplicit_attribute redecl = (EExplicit_attribute)attributesDerived[i].getRedeclaring(null);
			while (redecl.testRedeclaring(null)) {
				redecl = redecl.getRedeclaring(null);
			}

/*			EExplicit_attribute red = redecl;
			while (red != null) {
				if (red.testRedeclaring(null)) {
					red = red.getRedeclaring(null);
				} else {
					redecl = red;
					break;
				}
			}*/
//			if (redecl == red_attr) {
			if (redecl == expl_red_attr) {
				return true;
			}
		}
		return false;
	}

	final void setAttributeInformation(StaticFields staticFields, SdaiModel model) throws SdaiException {
		AEntity_definition result = null;
		if (partialEntityTypes == null || complex != 2) {
			result = prepareExternalMappingData(staticFields);
		}
		CEntity_definition entity;

		if (complex != 2 /*true*/) {
			if (staticFields.it == null) {
				staticFields.it = result.createIterator();
			} else {
				result.attachIterator(staticFields.it);
			}
		}
		int j = -1;
		while (j < noOfPartialEntityTypes - 1) {
			j++;
			if (complex == 2) {
				entity = partialEntityTypes[j];
			} else {
				staticFields.it.next();
				if (((AEntity)result).myLength <= 2) {
					entity = (CEntity_definition)staticFields.it.myElement;
				} else {
					entity = (CEntity_definition)((ListElement)staticFields.it.myElement).object;
				}
			}
/*		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			if (complex == 2) {
				entity = partialEntityTypes[j];
			} else {
				entity = (CEntity_definition)result.getByIndex(j + 1);
			}*/
			CExplicit_attribute [] partial_attrs = ((CEntityDefinition)entity).takeExplicit_attributes();
//			AExplicit_attribute partial_attrs = ((CEntityDefinition)entity).getExplicit_attributes(null);
//			if (partial_attrs == null || ((AEntity)partial_attrs).myLength <= 0) {
			if (partial_attrs.length <= 0) {
				entity.noOfPartialAttributes = 0;
				continue;
			}
			entity.noOfPartialAttributes = 0;
//			for (int i = 1; i <= ((AEntity)partial_attrs).myLength; i++) {
//				EAttribute attrib = partial_attrs.getByIndex(i);
			for (int i = 0; i < partial_attrs.length; i++) {
				EAttribute attrib = partial_attrs[i];
				if ((((AttributeDefinition)attrib).attr_tp == AttributeDefinition.EXPLICIT)  &&
						!((CExplicit_attribute)attrib).testRedeclaring(null)) {
					entity.noOfPartialAttributes++;
				}
			}
		}
	}


	final void setEarlyBindingBaseDictionary(StaticFields staticFields, SdaiModel model) throws SdaiException {
		int i;
		AEntity_definition result = null;
		if (partialEntityTypes == null || complex != 2 /*!true*/) {
			result = prepareExternalMappingData(staticFields);
		}
		Class cl = getEntityClass();
		try {
			owning_model.schemaData.super_inst.setDataField(cl, "definition", this);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		CExplicit_attribute [] partial_attrs;
		CDerived_attribute [] der_attrs;
		CInverse_attribute [] inv_attrs;
		CEntity_definition entity;
		String name, attr_name;
		int position = 0;
		int deriv_count = 0;
		int inv_count = 0;
		SchemaData sch_data = owning_model.schemaData;

		if (complex != 2 /*true*/) {
			if (staticFields.it == null) {
				staticFields.it = result.createIterator();
			} else {
				result.attachIterator(staticFields.it);
			}
		}
		int j = -1;
//String nnn = ((CEntity_definition)this).getName(null);
//boolean bbb = false;
//if (nnn.equals("entity_definition") || nnn.equals("population_dependent_bound")
//|| nnn.equals("dependent_map_partition")) bbb = true;
//if (bbb) System.out.println("  CEntityDefinition  entity: " + nnn);
		while (j < noOfPartialEntityTypes - 1) {
			j++;
			if (complex == 2) {
				entity = partialEntityTypes[j];
			} else {
				staticFields.it.next();
				if (((AEntity)result).myLength <= 2) {
					entity = (CEntity_definition)staticFields.it.myElement;
				} else {
					entity = (CEntity_definition)((ListElement)staticFields.it.myElement).object;
				}
			}
//if (bbb) System.out.println("  CEntityDefinition  partial entity: " + entity.getName(null));
			partial_attrs = ((CEntityDefinition)entity).takeExplicit_attributes();
			for (i = 0; i < partial_attrs.length; i++) {
				if (!partial_attrs[i].testRedeclaring(null)) {
					position++;
				}
			}
			if (((CEntityDefinition)entity).der_attr_exist) {
				der_attrs = ((CEntityDefinition)entity).takeDerived_attributes();
				for (i = 0; i < der_attrs.length; i++) {
					if (!der_attrs[i].testRedeclaring(null)) {
						deriv_count++;
					} else {
						EEntity redecl_attr = (der_attrs[i]).getRedeclaring(null);
						if (redecl_attr instanceof CExplicit_attribute) {
							deriv_count++;
						}
					}
				}
			}
			if (((CEntityDefinition)entity).inv_attr_exist) {
				inv_attrs = ((CEntityDefinition)entity).takeInverse_attributes();
				inv_count += inv_attrs.length;
			}

//CDerived_attribute[] der_attrss = ((CEntityDefinition)entity).takeDerived_attributes();
//for (i = 0; i < der_attrss.length; i++) {
//EAttribute attrib = der_attrss[i];
//if (bbb) System.out.println("  CEntityDefinition   derived  attrib: " + attrib.getName(null));}

		}
		attributes = new CExplicit_attribute[position];
		attributeFields = new Field[position];
		if (deriv_count > 0) {
			attributesDerived = new CDerived_attribute[deriv_count];
		}
		if (inv_count > 0) {
			attributesInverse = new CInverse_attribute[inv_count];
		}

		position = 0;
		deriv_count = 0;
		inv_count = 0;
		if (complex != 2 /*true*/) {
			result.attachIterator(staticFields.it);
		}
		j = -1;
		while (j < noOfPartialEntityTypes - 1) {
			j++;
			if (complex == 2) {
				entity = partialEntityTypes[j];
			} else {
				staticFields.it.next();
				if (((AEntity)result).myLength <= 2) {
					entity = (CEntity_definition)staticFields.it.myElement;
				} else {
					entity = (CEntity_definition)((ListElement)staticFields.it.myElement).object;
				}
			}
			entity.noOfPartialAttributes = 0;
			partial_attrs = ((CEntityDefinition)entity).takeExplicit_attributes();
			for (i = 0; i < partial_attrs.length; i++) {
				if (partial_attrs[i].testRedeclaring(null)) {
					continue;
				}
				entity.noOfPartialAttributes++;
				attributes[position] = partial_attrs[i];
				if (checkIfDerivedBasicDictionary(partial_attrs[i])) {
					position++;
					continue;
				}
				attr_name = "a" + position;
				name = attr_name + "$";
				try {
					sch_data.super_inst.setDataField(cl, name, partial_attrs[i]);
				} catch (java.lang.NoSuchFieldException ex) {
String base = SdaiSession.line_separator + "Class: " + cl.getName() +
"   schema: " + sch_data.schema.getName(null) + "   name: " + name + "   " + ex;
					throw new SdaiException(SdaiException.SY_ERR, base);
//					throw new SdaiException(SdaiException.SY_ERR, ex);
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				try {
					attributeFields[position] = cl.getDeclaredField(attr_name);
				} catch (java.lang.NoSuchFieldException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				position++;
			}
			if (((CEntityDefinition)entity).der_attr_exist) {
				der_attrs = ((CEntityDefinition)entity).takeDerived_attributes();
				for (i = 0; i < der_attrs.length; i++) {
					if (!der_attrs[i].testRedeclaring(null) || der_attrs[i].getRedeclaring(null) instanceof CExplicit_attribute) {
						attributesDerived[deriv_count++] = der_attrs[i];
					}
				}
			}
			if (((CEntityDefinition)entity).inv_attr_exist) {
				inv_attrs = ((CEntityDefinition)entity).takeInverse_attributes();
				System.arraycopy(inv_attrs, 0, attributesInverse, inv_count, inv_attrs.length);
				inv_count += inv_attrs.length;
			}
		}
		noOfAllAttributes = position;
	}


	boolean checkIfDerived(CExplicit_attribute attr) throws SdaiException {
		if (getIfDerived(attr) != null) {
			return true;
		}
		return false;
/*		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
			if (entity.not_expl_attribs == null) {
				continue;
			}
			AAttribute partial_not_expl_attrs = entity.not_expl_attribs;
			if (it3 == null) {
				it3 = partial_not_expl_attrs.createIterator();
			} else {
				partial_not_expl_attrs.attachIterator(it3);
			}
			while (it3.next()) {
				EAttribute attrib = (EAttribute)((ListElement)it3.myElement).object;
				if (attrib instanceof EDerived_attribute) {
					CDerived_attribute der_attr = (CDerived_attribute)attrib;
					if (der_attr.testRedeclaring(null)) {
						if (attr == der_attr.getRedeclaring(null)) {
							return true;
						}
					}
				}
			}
		}
		return false;*/
	}


	EAttribute getIfDerived(CExplicit_attribute attr) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
			if (entity.not_expl_attribs == null) {
				continue;
			}
			AAttribute partial_not_expl_attrs = entity.not_expl_attribs;
			if (staticFields.it3 == null) {
				staticFields.it3 = partial_not_expl_attrs.createIterator();
			} else {
				partial_not_expl_attrs.attachIterator(staticFields.it3);
			}
			while (staticFields.it3.next()) {
				EAttribute attrib;
				if (((AEntity)partial_not_expl_attrs).myLength <= 2) {
					attrib = (EAttribute)staticFields.it3.myElement;
				} else {
					attrib = (EAttribute)((ListElement)staticFields.it3.myElement).object;
				}
				if (((AttributeDefinition)attrib).attr_tp == AttributeDefinition.DERIVED) {
					CDerived_attribute der_attr = (CDerived_attribute)attrib;
					if (der_attr.testRedeclaring(null)) {
						EAttribute r_attr = (EAttribute)der_attr.getRedeclaring(null);
						while (r_attr != null) {
							if (attr == r_attr) {
								return der_attr;
							}
							if (((AttributeDefinition)r_attr).attr_tp == AttributeDefinition.EXPLICIT) {
								EExplicit_attribute e_attr = (EExplicit_attribute)r_attr;
								if (e_attr.testRedeclaring(null)) {
									r_attr = e_attr.getRedeclaring(null);
								} else {
									break;
								}
							} else {
								CDerived_attribute d_attr = (CDerived_attribute)r_attr;
								if (d_attr.testRedeclaring(null)) {
									r_attr = (EAttribute)d_attr.getRedeclaring(null);
								} else {
									break;
								}
							}
						}
//						if (attr == der_attr.getRedeclaring(null)) {
//							return der_attr;
//						}
					}
				}
			}
		}
		return null;
	}


	EAttribute findLastDerived(CExplicit_attribute attr, StaticFields staticFields) throws SdaiException {
		CDerived_attribute ret_der_attr = null;
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
			if (entity.not_expl_attribs == null) {
				continue;
			}
			AAttribute partial_not_expl_attrs = entity.not_expl_attribs;
			if (staticFields.it3 == null) {
				staticFields.it3 = partial_not_expl_attrs.createIterator();
			} else {
				partial_not_expl_attrs.attachIterator(staticFields.it3);
			}
			while (staticFields.it3.next()) {
				EAttribute attrib;
				if (((AEntity)partial_not_expl_attrs).myLength <= 2) {
					attrib = (EAttribute)staticFields.it3.myElement;
				} else {
					attrib = (EAttribute)((ListElement)staticFields.it3.myElement).object;
				}
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.DERIVED) {
					continue;
				}
				CDerived_attribute der_attr = (CDerived_attribute)attrib;
				if (!der_attr.testRedeclaring(null)) {
					continue;
				}
				EAttribute r_attr = (EAttribute)der_attr.getRedeclaring(null);
				while (r_attr != null) {
					if (attr == r_attr) {
						if (ret_der_attr == null) {
							ret_der_attr = der_attr;
						} else {
							if (checkIfDeriving(ret_der_attr, der_attr)) {
								ret_der_attr = der_attr;
							}
						}
						break;
					}
					if (((AttributeDefinition)r_attr).attr_tp == AttributeDefinition.EXPLICIT) {
						EExplicit_attribute e_attr = (EExplicit_attribute)r_attr;
						if (e_attr.testRedeclaring(null)) {
							r_attr = e_attr.getRedeclaring(null);
						} else {
							break;
						}
					} else {
						CDerived_attribute d_attr = (CDerived_attribute)r_attr;
						if (d_attr.testRedeclaring(null)) {
							r_attr = (EAttribute)d_attr.getRedeclaring(null);
						} else {
							break;
						}
					}
				}
			}
		}
		return ret_der_attr;
	}


	boolean checkIfDeriving(CDerived_attribute current_attr, CDerived_attribute cand_attr) throws SdaiException {
		EAttribute r_attr = (EAttribute)cand_attr.getRedeclaring(null);
		while (r_attr != null) {
			if (current_attr == r_attr) {
				return true;
			}
			if (((AttributeDefinition)r_attr).attr_tp == AttributeDefinition.EXPLICIT) {
				return false;
			}
			CDerived_attribute d_attr = (CDerived_attribute)r_attr;
			if (d_attr.testRedeclaring(null)) {
				r_attr = (EAttribute)d_attr.getRedeclaring(null);
			} else {
				return false;
			}
		}
		return false;
	}


	boolean checkIfDerivedBasicDictionary(CExplicit_attribute attr) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
			if (entity.not_expl_attribs == null) {
				continue;
			}
			AAttribute partial_not_expl_attrs = entity.not_expl_attribs;
			if (staticFields.it3 == null) {
				staticFields.it3 = partial_not_expl_attrs.createIterator();
			} else {
				partial_not_expl_attrs.attachIterator(staticFields.it3);
			}
			while (staticFields.it3.next()) {
				EAttribute attrib;
				if (((AEntity)partial_not_expl_attrs).myLength <= 2) {
					attrib = (EAttribute)staticFields.it3.myElement;
				} else {
					attrib = (EAttribute)((ListElement)staticFields.it3.myElement).object;
				}
				if (attrib instanceof EDerived_attribute) {
					CDerived_attribute der_attr = (CDerived_attribute)attrib;
					if (der_attr.testRedeclaring(null)) {
						EAttribute r_attr = (EAttribute)der_attr.getRedeclaring(null);
						while (r_attr != null) {
							if (attr == r_attr) {
								return true;
							}
							if (r_attr instanceof EExplicit_attribute) {
								EExplicit_attribute e_attr = (EExplicit_attribute)r_attr;
								if (e_attr.testRedeclaring(null)) {
									r_attr = e_attr.getRedeclaring(null);
								} else {
									break;
								}
							} else {
								CDerived_attribute d_attr = (CDerived_attribute)r_attr;
								if (d_attr.testRedeclaring(null)) {
									r_attr = (EAttribute)d_attr.getRedeclaring(null);
								} else {
									break;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}


	CDerived_attribute getDerivedForExplicit(CExplicit_attribute attr) throws SdaiException {
		CDerived_attribute found_der = null;
		StaticFields staticFields = StaticFields.get();
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
			if (entity.not_expl_attribs == null) {
				continue;
			}
			AAttribute partial_not_expl_attrs = entity.not_expl_attribs;
			if (staticFields.it3 == null) {
				staticFields.it3 = partial_not_expl_attrs.createIterator();
			} else {
				partial_not_expl_attrs.attachIterator(staticFields.it3);
			}
			while (staticFields.it3.next()) {
				EAttribute attrib;
				if (((AEntity)partial_not_expl_attrs).myLength <= 2) {
					attrib = (EAttribute)staticFields.it3.myElement;
				} else {
					attrib = (EAttribute)((ListElement)staticFields.it3.myElement).object;
				}
				if (((AttributeDefinition)attrib).attr_tp == AttributeDefinition.DERIVED) {
					CDerived_attribute der_attr = (CDerived_attribute)attrib;
					if (der_attr.testRedeclaring(null)) {
						if (found_der == null) {
							if (attr == der_attr.getRedeclaring(null)) {
								found_der = der_attr;
							}
						} else {
							if (found_der == der_attr.getRedeclaring(null)) {
								found_der = der_attr;
							}
						}
					}
				}
			}
		}
		return found_der;
	}

/*	final void extractEarlyBindingInfo() throws SdaiException {
		AEntity_definition result = null;
		if (partialEntityTypes == null || complex!=2 ) {
			result = prepareExternalMappingData();
		}

		AExplicit_attribute partial_attrs;
		CEntity_definition entity;
		EAttribute attrib;
		int count;
		for (int j = 0;	j < noOfPartialEntityTypes; j++) {
			if (complex) {
				entity = partialEntityTypes[j];
			} else {
				entity = (CEntity_definition)result.getByIndex(j + 1);
			}
			count = 0;
			partial_attrs =	entity.getExplicit_attributes(null);
			if (partial_attrs != null) {
				for (int i = 1; i <= partial_attrs.myLength; i++) {
					attrib = partial_attrs.getByIndex(i);
					if (attrib instanceof CExplicit_attribute &&
						!((CExplicit_attribute)attrib).testRedeclaring(null)) {
//						!checkIfDerived((CExplicit_attribute)attrib)) {
						count++;
					}
				}
			}
			entity.noOfPartialAttributes = count;
		}
		for (int j = 0;	j < noOfPartialEntityTypes; j++) {
			if (complex) {
				entity = partialEntityTypes[j];
			} else {
				entity = (CEntity_definition)result.getByIndex(j + 1);
			}
			entity.noOfPartialAttributes = countAttributes(entity);
		}
	}*/

/*	private	int countAttributes(CEntity_definition	entity) throws SdaiException {
		int count = 0;
		Object inv = entity.inverseList;
		if (inv	== null) {
			return 0;
		}
		if (!(inv instanceof Inverse)) {
			count =	checkInverse(inv, entity, count);
		} else {
			Inverse	inv1 = (Inverse)inv;
			while (true) {
				count =	checkInverse(inv1.value, entity, count);
				if (!(inv1.next	instanceof Inverse)) {
					count =	checkInverse(inv1.next,	entity,	count);
					break;
				}
				inv1 = (Inverse)inv1.next;
			}
		}
		return count;
	} */


	private void addPartialTypes(AEntity_definition result)
			throws SdaiException {
		if (!(result.isMember(this))) {
			for (int i = 0; i < supertypes_count; i++) {
				((CEntityDefinition)supertypes[i]).addPartialTypes(result);
			}
			((AEntity)result).addAtTheEnd(this, null);
//			result.addByIndex(((AEntity)result).myLength + 1, this);
		}
	}

	final void preparePartialTypes() throws SdaiException {
		AEntity_definition result = new AEntity_definition();
		for (int i = 0; i < leaf_count; i++) {
			((CEntityDefinition)leaves[i]).addPartialTypes(result);
		}
		sortPartialTypes(result);
	}

	private final void sortPartialTypes(AEntity_definition result) throws SdaiException {
		int res_length = ((AEntity)result).myLength;
		StaticFields staticFields = StaticFields.get();
		if (res_length > staticFields.names.length) {
			enlargeNames(res_length, staticFields);
		}
		externalMappingIndexing = new int[res_length];
		internalMappingIndexing = new int[res_length];
		partialEntityTypes = new CEntity_definition[res_length];
		int i = 0;
		if (res_length < 1) {
		} else if (res_length == 1) {
			internalMappingIndexing[0] = 0;
			staticFields.names[0] = ((CEntity_definition)((AEntity)result).myData).getNameUpperCase();
		} else if (res_length == 2) {
			Object [] myDataA = (Object [])((AEntity)result).myData;
			for (i = 0; i < 2; i++) {
				internalMappingIndexing[i] = i;
				staticFields.names[i] = ((CEntity_definition)myDataA[i]).getNameUpperCase();
			}
		} else {
			if (staticFields.it == null) {
				staticFields.it = result.createIterator();
			} else {
				result.attachIterator(staticFields.it);
			}
			while (staticFields.it.next()) {
				internalMappingIndexing[i] = i;
				staticFields.names[i] = ((CEntity_definition)((ListElement)staticFields.it.myElement).object).getNameUpperCase();
				i++;
			}
		}

		boolean mark = true;
		String str;
		int index, new_index;
		int k = res_length - 2;
		while (mark) {
			mark = false;
			for (i = 0; i <= k; i++) {
				if (staticFields.names[i].compareTo(staticFields.names[i + 1]) > 0) {
					str = staticFields.names[i];
					staticFields.names[i] = staticFields.names[i + 1];
					staticFields.names[i + 1] = str;
					index = internalMappingIndexing[i];
					internalMappingIndexing[i] = internalMappingIndexing[i + 1];
					internalMappingIndexing[i + 1] = index;
					mark = true;
				}
			}
			k--;
		}
		for (i = 0; i < res_length; i++) {
			externalMappingIndexing[internalMappingIndexing[i]] = i;
			partialEntityTypes[i] = (CEntity_definition)result.getByIndex(internalMappingIndexing[i] + 1);
		}
		noOfPartialEntityTypes = res_length;
	}


	int find_partial_entity(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			String name = partialEntityTypes[middle].getCorrectName();
			int comp_res = name.compareTo(key);
			if (comp_res < 0) {
				left = middle + 1;
			} else if (comp_res > 0) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	int find_partial_entity_upper_case(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			String name = partialEntityTypes[middle].getNameUpperCase();
			int comp_res = name.compareTo(key);
			if (comp_res < 0) {
				left = middle + 1;
			} else if (comp_res > 0) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	boolean check_entity(CEntity_definition type[], int count) throws SdaiException {
		if (count != noOfPartialEntityTypes) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			String ent_name = type[i].getCorrectName();
			if (!partialEntityTypes[i].getCorrectName().equals(ent_name)) {
				return false;
			}
		}
		return true;
	}


	public final AExplicit_attribute getExplicit_attributes(EEntity_definition type)
			throws SdaiException {
//		synchronized (syncObject) {
		int i;
		if (expl_attribs != null) {
			return expl_attribs;
		}
		AExplicit_attribute expl_attribs = new AExplicit_attribute();
		((AEntity)expl_attribs).attach(SdaiSession.listType0toN, this);
		if (attributesExpl == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		for (i = 0; i < attributesExpl.length; i++) {
			((AEntity)expl_attribs).addByIndexAttribute(i + 1, attributesExpl[i]);
		}
		int count = attributesExpl.length;
		for (i = 0; i < attributesRedecl.length; i++) {
			if (((AttributeDefinition)attributesRedecl[i]).attr_tp == AttributeDefinition.EXPLICIT) {
				((AEntity)expl_attribs).addByIndexAttribute(++count, attributesRedecl[i]);
			}
		}
		return expl_attribs;
//		} // syncObject
	}


	public final boolean testExplicit_attributes(EEntity_definition type) throws SdaiException {
		return true;
	}


	/**
     * @since 4.0.0
     */
	public boolean isValidFor(ESchema_definition schema) throws SdaiException {
		SchemaDefinition sch_def = (SchemaDefinition)schema;
		if (schema == null || sch_def.owning_model == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		SchemaData sch_data = sch_def.owning_model.schemaData;
		if (sch_data == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (sch_data.findEntityExtentIndex(this) < 0) {
			return false;
		}
		return true;
	}


	/**
     * @since 4.0.0
     */
	public boolean isValidFor(Class schema) throws SdaiException {
		if (schema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		SdaiSession session = owning_model.repository.session;
		CSchema_definition def = session.getSchemaDefinition(schema);
		if (def == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		return isValidFor(def);
	}


	final CExplicit_attribute[] takeExplicit_attributes() throws SdaiException {
		if (attributesExpl == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		return attributesExpl;
	}


	private final void take_attributes(SdaiModel mod) throws SdaiException {
		int i;
		EAttribute attr;
		CEntity_definition edef;
		int count = 0;
		int redecl_attr_count = 0;
		AAttribute all_attr = null;
		int inv_attr_all_count = 0;

		StaticFields staticFields = StaticFields.get();
		if (mod == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < mod.lengths[SdaiSession.EXPLICIT_ATTRIBUTE]; i++) {
				CExplicit_attribute ex_attr =
					(CExplicit_attribute)mod.instances_sim[SdaiSession.EXPLICIT_ATTRIBUTE][i];
				edef = (CEntity_definition)ex_attr.getParent(null);
				if (edef != this || !ex_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			der_attr_exist = false;
		} else {
			all_attr = getAttributes(null, null);
//for (i = 0; i < der_attrs.length; i++)
//System.out.println("CEntityDefinition:  i: " + i + "  der_attrs[i]: " + der_attrs[i]);
			if (staticFields.it2 == null) {
				staticFields.it2 = all_attr.createIterator();
			} else {
				all_attr.attachIterator(staticFields.it2);
			}
			i = 0;
			while (staticFields.it2.next()) {
				if (((AEntity)all_attr).myLength <= 2) {
					attr = (EAttribute)staticFields.it2.myElement;
				} else {
					attr = (EAttribute)((ListElement)staticFields.it2.myElement).object;
				}
				boolean bool;
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE) {
					inv_attr_all_count++;
					bool = true;
				} else {
					bool = false;
				}
				if (!attr.testOrder(null)) {
					redecl_attr_count++;
					i++;
					continue;
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
					der_attr_exist = true;
				} else if (bool) {
					inv_attr_exist = true;
				} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					if (staticFields.indices == null) {
						staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
					} else if (count >= staticFields.indices.length) {
						enlargeIndices();
					}
					staticFields.indices[count++] = i;
				}
				i++;
			}
		}

		attributesExpl = new CExplicit_attribute[count];
		if (mod == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < count; i++) {
				attributesExpl[i] =
					(CExplicit_attribute)mod.instances_sim[SdaiSession.EXPLICIT_ATTRIBUTE][staticFields.indices[i]];
				staticFields.indices[i] = attributesExpl[i].getOrder(null);
			}
		} else {
			for (i = 0; i < count; i++) {
				attributesExpl[i] = (CExplicit_attribute)((AEntity)all_attr).getByIndexEntity(staticFields.indices[i] + 1);
				staticFields.indices[i] = attributesExpl[i].getOrder(null);
			}
		}
		int j = count - 2;
		int ind;
		CExplicit_attribute at;
		boolean mark = true;
		while (mark) {
			mark = false;
			for (i = 0; i <= j; i++) {
				if (staticFields.indices[i] > staticFields.indices[i + 1]) {
					ind = staticFields.indices[i];
					staticFields.indices[i] = staticFields.indices[i + 1];
					staticFields.indices[i + 1] = ind;
					at = attributesExpl[i];
					attributesExpl[i] = attributesExpl[i + 1];
					attributesExpl[i + 1] = at;
					mark = true;
				}
			}
			j--;
		}

		count = 0;
		if (mod == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < mod.lengths[SdaiSession.DERIVED_ATTRIBUTE]; i++) {
				CDerived_attribute der_attr =
					(CDerived_attribute)mod.instances_sim[SdaiSession.DERIVED_ATTRIBUTE][i];
				edef = (CEntity_definition)der_attr.getParent(null);
				if (edef != this || !der_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			attributesDeriv = new CDerived_attribute[count];
			if (count > 0) {
				der_attr_exist = true;
				for (i = 0; i < count; i++) {
					attributesDeriv[i] =
						(CDerived_attribute)mod.instances_sim[SdaiSession.DERIVED_ATTRIBUTE][staticFields.indices[i]];
					staticFields.indices[i] = attributesDeriv[i].getOrder(null);
				}
			}
		} else if (!der_attr_exist) {
			attributesDeriv = new CDerived_attribute[0];
		} else {
			staticFields.it2.beginning();
			i = 0;
			while (staticFields.it2.next()) {
				if (((AEntity)all_attr).myLength <= 2) {
					attr = (EAttribute)staticFields.it2.myElement;
				} else {
					attr = (EAttribute)((ListElement)staticFields.it2.myElement).object;
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED && attr.testOrder(null)) {
					if (staticFields.indices == null) {
						staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
					} else if (count >= staticFields.indices.length) {
						enlargeIndices();
					}
					staticFields.indices[count++] = i;
				}
				i++;
			}
			attributesDeriv = new CDerived_attribute[count];
			for (i = 0; i < count; i++) {
				attributesDeriv[i] = (CDerived_attribute)((AEntity)all_attr).getByIndexEntity(staticFields.indices[i] + 1);
				staticFields.indices[i] = attributesDeriv[i].getOrder(null);
			}
		}
		if (count > 1) {
			j = count - 2;
			CDerived_attribute at_der;
			mark = true;
			while (mark) {
				mark = false;
				for (i = 0; i <= j; i++) {
					if (staticFields.indices[i] > staticFields.indices[i + 1]) {
						ind = staticFields.indices[i];
						staticFields.indices[i] = staticFields.indices[i + 1];
						staticFields.indices[i + 1] = ind;
						at_der = attributesDeriv[i];
						attributesDeriv[i] = attributesDeriv[i + 1];
						attributesDeriv[i + 1] = at_der;
						mark = true;
					}
				}
				j--;
			}
		}

		count = 0;
		if (mod == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < mod.lengths[SdaiSession.INVERSE_ATTRIBUTE]; i++) {
				CInverse_attribute inv_attr =
					(CInverse_attribute)mod.instances_sim[SdaiSession.INVERSE_ATTRIBUTE][i];
				edef = (CEntity_definition)inv_attr.getParent(null);
				if (edef != this || !inv_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			attributesInv = new CInverse_attribute[count];
			if (count > 0) {
				inv_attr_exist = true;
				for (i = 0; i < count; i++) {
					attributesInv[i] =
						(CInverse_attribute)mod.instances_sim[SdaiSession.INVERSE_ATTRIBUTE][staticFields.indices[i]];
					staticFields.indices[i] = attributesInv[i].getOrder(null);
				}
			}
		} else if (inv_attr_all_count == 0) {
			attributesInv = new CInverse_attribute[0];
		} else {
			attributesInvAll = new CInverse_attribute[inv_attr_all_count];
			int c = 0;
			staticFields.it2.beginning();
			i = 0;
			while (staticFields.it2.next()) {
				if (((AEntity)all_attr).myLength <= 2) {
					attr = (EAttribute)staticFields.it2.myElement;
				} else {
					attr = (EAttribute)((ListElement)staticFields.it2.myElement).object;
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE) {
					attributesInvAll[c++] = (CInverse_attribute)attr;
					if (attr.testOrder(null)) {
						if (staticFields.indices == null) {
							staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
						} else if (count >= staticFields.indices.length) {
							enlargeIndices();
						}
						staticFields.indices[count++] = i;
					}
				}
				i++;
			}
			attributesInv = new CInverse_attribute[count];
			for (i = 0; i < count; i++) {
				attributesInv[i] = (CInverse_attribute)((AEntity)all_attr).getByIndexEntity(staticFields.indices[i] + 1);
				staticFields.indices[i] = attributesInv[i].getOrder(null);
			}
		}
		if (count > 1) {
			j = count - 2;
			CInverse_attribute at_inv;
			mark = true;
			while (mark) {
				mark = false;
				for (i = 0; i <= j; i++) {
					if (staticFields.indices[i] > staticFields.indices[i + 1]) {
						ind = staticFields.indices[i];
						staticFields.indices[i] = staticFields.indices[i + 1];
						staticFields.indices[i + 1] = ind;
						at_inv = attributesInv[i];
						attributesInv[i] = attributesInv[i + 1];
						attributesInv[i + 1] = at_inv;
						mark = true;
					}
				}
				j--;
			}
		}

		count = 0;
		if (mod == SdaiSession.baseDictionaryModel) {
			int count1 = 0, count2 = 0, count3 = 0;
			for (i = 0; i < mod.lengths[SdaiSession.EXPLICIT_ATTRIBUTE]; i++) {
				CExplicit_attribute ex_attr =
					(CExplicit_attribute)mod.instances_sim[SdaiSession.EXPLICIT_ATTRIBUTE][i];
				edef = (CEntity_definition)ex_attr.getParent(null);
				if (edef != this || ex_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			count1 = count;
			for (i = 0; i < mod.lengths[SdaiSession.DERIVED_ATTRIBUTE]; i++) {
				CDerived_attribute der_attr =
					(CDerived_attribute)mod.instances_sim[SdaiSession.DERIVED_ATTRIBUTE][i];
				edef = (CEntity_definition)der_attr.getParent(null);
				if (edef != this || der_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			count2 = count - count1;
			for (i = 0; i < mod.lengths[SdaiSession.INVERSE_ATTRIBUTE]; i++) {
				CInverse_attribute inv_attr =
					(CInverse_attribute)mod.instances_sim[SdaiSession.INVERSE_ATTRIBUTE][i];
				edef = (CEntity_definition)inv_attr.getParent(null);
				if (edef != this || inv_attr.testOrder(null)) {
					continue;
				}
				if (staticFields.indices == null) {
					staticFields.indices = new int[NUMBER_OF_ATTRIBUTES];
				} else if (count >= staticFields.indices.length) {
					enlargeIndices();
				}
				staticFields.indices[count++] = i;
			}
			count3 = count - count2;
			attributesRedecl = new EAttribute[count];
			if (count1 > 0) {
				for (i = 0; i < count1; i++) {
					attributesRedecl[i] =
						(EAttribute)mod.instances_sim[SdaiSession.EXPLICIT_ATTRIBUTE][staticFields.indices[i]];
				}
			}
			if (count2 > 0) {
				for (i = count1; i < count1 + count2; i++) {
					attributesRedecl[i] =
						(EAttribute)mod.instances_sim[SdaiSession.DERIVED_ATTRIBUTE][staticFields.indices[i]];
				}
			}
			if (count3 > 0) {
				for (i = count1 + count2; i < count; i++) {
					attributesRedecl[i] =
						(EAttribute)mod.instances_sim[SdaiSession.INVERSE_ATTRIBUTE][staticFields.indices[i]];
				}
			}
		} else if (redecl_attr_count == 0) {
			attributesRedecl = new EAttribute[0];
		} else {
			attributesRedecl = new EAttribute[redecl_attr_count];
			staticFields.it2.beginning();
			while (staticFields.it2.next()) {
				if (((AEntity)all_attr).myLength <= 2) {
					attr = (EAttribute)staticFields.it2.myElement;
				} else {
					attr = (EAttribute)((ListElement)staticFields.it2.myElement).object;
				}
				if (!attr.testOrder(null)) {
					attributesRedecl[count++] = attr;
				}
			}
		}
	}


	private final CDerived_attribute[] takeDerived_attributes() throws SdaiException {
		if (attributesDeriv == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		return attributesDeriv;
	}


	final CInverse_attribute[] takeInverse_attributes() throws SdaiException {
		if (attributesInv == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		return attributesInv;
	}


	final CInverse_attribute[] takeInverse_attributesAll() throws SdaiException {
		if (attributesInv == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		return attributesInvAll;
	}


	final EAttribute[] takeRedecl_attributes() throws SdaiException {
		if (attributesRedecl == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		return attributesRedecl;
	}


/**
	Returns entity attribute of this entity definition (including all supertypes)
   for a given attribute name. If an attribute with the submitted name is not
   found, then <code>null</code> value is returned.
	This method is invoked in <code>getAttributeDefinition</code> in
   <code>CEntity</code> class.
*/
	EAttribute extract_attribute(String attributeName) throws SdaiException {
		int i, j;
		int count_of_found = 0;
		EAttribute at_found = null;
		EAttribute at;

		int dot = attributeName.lastIndexOf(PhFileReader.DOT);
		if (dot >= 0) {
			String ent_name = attributeName.substring(0, dot).toUpperCase();
			attributeName = attributeName.substring(dot + 1);
			CEntityDefinition entity;
			for (j = 0; j < noOfPartialEntityTypes; j++) {
				if (complex == 2) {
					entity = partialEntityTypes[j];
				} else {
					entity = partialEntityTypes[externalMappingIndexing[j]];
				}
				if (!((CEntity_definition)entity).getName(null).toUpperCase().equals(ent_name)) {
					continue;
				}
				return entity.extract_attribute_partial(attributeName);
			}
			return null;
		}

		for (i = 0; i < attributes.length; i++) {
			at = attributes[i];
			if (at.getName(null).equals(attributeName)) {
				at_found = at;
				count_of_found++;
			}
		}
		if (attributesDerived != null) {
			for (i = 0; i < attributesDerived.length; i++) {
				at = attributesDerived[i];
				if (at.getName(null).equals(attributeName)) {
					at_found = at;
					count_of_found++;
				}
			}
		}
		if (attributesInverse != null) {
			for (i = 0; i < attributesInverse.length; i++) {
				at = attributesInverse[i];
				if (at.getName(null).equals(attributeName)) {
					at_found = at;
					count_of_found++;
				}
			}
		}
		CEntityDefinition partial_edef;
		for (j = 0; j < noOfPartialEntityTypes; j++) {
			partial_edef = partialEntityTypes[j];
			for (i = 0; i < partial_edef.attributesRedecl.length; i++) {
				at = partial_edef.attributesRedecl[i];
				if (at.getName(null).equals(attributeName)) {
					if (count_of_found > 0) {
						boolean found = false;
						EExplicit_attribute expl_attr;
						if (((AttributeDefinition)at).attr_tp == AttributeDefinition.EXPLICIT &&
							((CExplicit_attribute)at).testRedeclaring(null)) {
							expl_attr = (EExplicit_attribute)at;
							while (expl_attr.testRedeclaring(null)) {
								expl_attr = expl_attr.getRedeclaring(null);
								if (expl_attr==at_found) {
									found = true;
									break;
								}
							}
						}
						if (found) {
							at_found = at;
						} else {
							if (((AttributeDefinition)at_found).attr_tp == AttributeDefinition.EXPLICIT &&
									((CExplicit_attribute)at_found).testRedeclaring(null)) {
								expl_attr = (EExplicit_attribute)at_found;
								while (expl_attr.testRedeclaring(null)) {
									expl_attr = expl_attr.getRedeclaring(null);
									if (expl_attr==at) {
										found = true;
										break;
									}
								}
							}
							if (!found) {
								at_found = at;
								count_of_found++;
							}
						}
					} else {
						at_found = at;
						count_of_found++;
					}
				}
			}
		}
		for (j = 0; j < noOfPartialEntityTypes; j++) {
			partial_edef = partialEntityTypes[j];
			if (!partial_edef.der_attr_exist) {
				continue;
			}
			CDerived_attribute [] der_attrs = partial_edef.takeDerived_attributes();
			for (i = 0; i < der_attrs.length; i++) {
				if (!der_attrs[i].testOrder(null)) {
					continue;
				}
				if (!der_attrs[i].getName(null).equals(attributeName)) {
					continue;
				}
				boolean found = false;
				if (attributesDerived != null) {
					for (int k = 0; k < attributesDerived.length; k++) {
						if (der_attrs[i] == attributesDerived[k]) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					at_found = der_attrs[i];
					count_of_found++;
				}
			}
		}
		if (count_of_found == 1) {
			return at_found;
		} else if (count_of_found == 0) {
			return null;
		} else {
			throw new SdaiException(SdaiException.AT_NVLD, "Name is " + attributeName + " for entity definition " + this);
		}
	}


	EAttribute find_attribute(String attributeName) throws SdaiException {
		if (attributeName == null) {
			throw new SdaiException(SdaiException.AT_NDEF, "Name is " + attributeName + " for entity definition " + this);
		}
		int i;
		EAttribute at_found = null;
		EAttribute at;
		if (attributes == null) {
			SdaiSession session = owning_model.repository.session;
			String ent_name = ((CEntity_definition)this).getName(null);
			if (session != null && session.logWriterSession != null) {
				session.printlnSession(AdditionalMessages.DI_ATNF + SdaiSession.line_separator +
					AdditionalMessages.RD_ENT + ent_name + SdaiSession.line_separator +
					AdditionalMessages.RD_ATTR + attributeName);
			} else {
				SdaiSession.println(AdditionalMessages.DI_ATNF + SdaiSession.line_separator +
					AdditionalMessages.RD_ENT + ent_name + SdaiSession.line_separator +
					AdditionalMessages.RD_ATTR + attributeName);
			}
			SchemaData sch_data = owning_model.schemaData;
			int indx = sch_data.findEntityExtentIndex(this);
			if (indx < 0) {
				SdaiModel error_mod = sch_data.model;
				throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table: " +
					ent_name + " (used in model: " + error_mod.name + ")");
			}
			Class cl_x = sch_data.getEntityClassByIndex(indx);
		}
		for (i = 0; i < attributes.length; i++) {
			at = attributes[i];
			if (at.getName(null).equals(attributeName)) {
				at_found = at;
			}
		}
		if (attributesDerived != null) {
			for (i = 0; i < attributesDerived.length; i++) {
				at = attributesDerived[i];
				if (at.getName(null).equals(attributeName)) {
					if (at_found == null) {
						at_found = at;
					} else {
//System.out.println("CEntityDefinition  DERIVED  def: " + ((CEntity_definition)this).getName(null) +
//"   attributeName: " + attributeName + "     at_found: " + at_found.getName(null) +
//"     at: " + at.getName(null));
						if (checkRedeclaredAttribute(at, at_found)) {
							at_found = at;
						}
					}
				}
			}
		}
		if (attributesInverse != null) {
			for (i = 0; i < attributesInverse.length; i++) {
				at = attributesInverse[i];
				if (at.getName(null).equals(attributeName)) {
					at_found = at;
				}
			}
		}
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition partial_edef = partialEntityTypes[j];
			for (i = 0; i < partial_edef.attributesRedecl.length; i++) {
				at = partial_edef.attributesRedecl[i];
				if (at.getName(null).equals(attributeName)) {
					if (at_found == null) {
						at_found = at;
					} else {
						if (checkRedeclaredAttribute(at, at_found)) {
							at_found = at;
						}
					}
				}
			}
		}
		if (at_found != null) {
			return at_found;
		} else {
			return null;
//System.out.println("CEntityDefinition  ^^^  def: " + ((CEntity_definition)this).getName(null) +
//"   attributeName: " + attributeName);
//			throw new SdaiException(SdaiException.AT_NVLD);
		}
	}


	void extract_def_types(StaticFields staticFields) throws SdaiException {
		if (staticFields.aux_arr_tp == null) {
			staticFields.aux_arr_tp = new CDefined_type[NUMBER_OF_DEFINED_TYPES];
			staticFields.aux_arr_at = new EAttribute[NUMBER_OF_DEFINED_TYPES];
			staticFields.aux_arr_br = new int[NUMBER_OF_DEFINED_TYPES];
		}
		int def_tp_count = 0;
		DataType tp;
		int i;
		for (i = 0; i < attributes.length; i++) {
			tp = (DataType)attributes[i].getDomain(null);
			if (tp.express_type == DataType.DEFINED_TYPE) {
				def_tp_count = check_type(tp, attributes[i], true, def_tp_count);
			}
		}
		if (attributesDerived != null) {
			for (i = 0; i < attributesDerived.length; i++) {
				tp = (DataType)attributesDerived[i].getDomain(null);
				if (tp.express_type == DataType.DEFINED_TYPE) {
					def_tp_count = check_type(tp, attributesDerived[i], true, def_tp_count);
				}
			}
		}
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition partial_edef = partialEntityTypes[j];
			if (partial_edef.attributesRedecl == null ) {
				continue;
			}
			for (i = 0; i < partial_edef.attributesRedecl.length; i++) {
				EAttribute at = partial_edef.attributesRedecl[i];
				if (((AttributeDefinition)at).attr_tp == AttributeDefinition.EXPLICIT) {
					tp = (DataType)((CExplicit_attribute)at).getDomain(null);
				} else if (((AttributeDefinition)at).attr_tp == AttributeDefinition.DERIVED) {
					tp = (DataType)((CDerived_attribute)at).getDomain(null);
				} else {
					continue;
				}
				if (tp.express_type == DataType.DEFINED_TYPE) {
					def_tp_count = check_type(tp, at, true, def_tp_count);
				}
			}
		}
		def_types = new CDefined_type[def_tp_count];
		def_types_attr = new EAttribute[def_tp_count];
		def_types_branch = new int[def_tp_count];
		if (def_tp_count > 0) {
			System.arraycopy(staticFields.aux_arr_tp, 0, def_types, 0, def_tp_count);
			System.arraycopy(staticFields.aux_arr_at, 0, def_types_attr, 0, def_tp_count);
			System.arraycopy(staticFields.aux_arr_br, 0, def_types_branch, 0, def_tp_count);
		}
	}


	private int check_type(DataType type, EAttribute attr, boolean first, int def_tp_count) throws SdaiException {
		DataType tp = type;
		boolean is_rule = false;
		if (tp.express_type == DataType.DEFINED_TYPE) {
			while (tp.express_type == DataType.DEFINED_TYPE) {
				if (!is_rule) {
					AWhere_rule w_rules = ((CDefined_type)tp).getWhere_rules(null, null);
					if (((AEntity)w_rules).myLength > 0) {
						is_rule = true;
					}
				}
				tp = (DataType)((CDefined_type)tp).getDomain(null);
			}
			boolean is_select = tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT;
			if (is_rule) {
				StaticFields staticFields = StaticFields.get();
				if (def_tp_count >= staticFields.aux_arr_tp.length) {
					int new_length = def_tp_count * 2;
					CDefined_type new_aux_arr_tp[] = new CDefined_type[new_length];
					System.arraycopy(staticFields.aux_arr_tp, 0, new_aux_arr_tp, 0, def_tp_count);
					staticFields.aux_arr_tp = new_aux_arr_tp;
					EAttribute new_aux_arr_at[] = new EAttribute[new_length];
					System.arraycopy(staticFields.aux_arr_at, 0, new_aux_arr_at, 0, def_tp_count);
					staticFields.aux_arr_at = new_aux_arr_at;
					int new_aux_arr_br[] = new int[new_length];
					System.arraycopy(staticFields.aux_arr_br, 0, new_aux_arr_br, 0, def_tp_count);
					staticFields.aux_arr_br = new_aux_arr_br;
				}
				staticFields.aux_arr_tp[def_tp_count] = (CDefined_type)type;
				if (first) {
					if (is_select) {
						staticFields.aux_arr_br[def_tp_count] = 1;
					} else {
						staticFields.aux_arr_br[def_tp_count] = 0;
					}
				} else {
					staticFields.aux_arr_br[def_tp_count] = 2;
				}
				staticFields.aux_arr_at[def_tp_count++] = attr;
			}
			if (is_select) {
				ANamed_type	selections;
				if (tp.express_type >= DataType.EXTENSIBLE_SELECT) {
					SdaiSession ss = ((CEntity)tp).owning_model.repository.session;
					if (ss.sdai_context == null) {
						if (!ss.sdai_context_missing) {
							ss.sdai_context_missing = true;
							ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
						}
					}
					selections = ((EExtensible_select_type)tp).getSelections(null, ss.sdai_context);
				} else {
					selections = ((ESelect_type)tp).getSelections(null);
				}
				AEntity sels = (AEntity)selections;
				if (sels.myLength < 0) {
					sels.resolveAllConnectors();
				}
				Object [] myDataA = null;
				if (sels.myLength > 1) {
					myDataA = (Object [])sels.myData;
				}
				for (int i = 0; i < sels.myLength; i++) {
					DataType element;
					if (sels.myLength == 1) {
						element = (DataType)sels.myData;
					} else {
						element = (DataType)myDataA[i];
					}
					if (element.express_type == DataType.ENTITY) {
						continue;
					}
					if (element.express_type != DataType.DEFINED_TYPE) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					def_tp_count = check_type(element, attr, false, def_tp_count);
				}
			} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
// currently do nothing
			}
		} else if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
// currently do nothing
		}
		return def_tp_count;
	}


	private boolean checkRedeclaredAttribute(EAttribute red_attr, EAttribute checked) throws SdaiException {
		EAttribute attr = red_attr;
		boolean cont = true;
		while (cont) {
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
				attr = ((EExplicit_attribute)attr).getRedeclaring(null);
				if (attr == checked) {
					return true;
				}
				cont = ((EExplicit_attribute)attr).testRedeclaring(null);
			} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
				attr = (EAttribute)((EDerived_attribute)attr).getRedeclaring(null);
				if (attr == checked) {
					return true;
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					cont = ((EExplicit_attribute)attr).testRedeclaring(null);
				} else {
					cont = ((EDerived_attribute)attr).testRedeclaring(null);
				}
			} else {
				attr = ((EInverse_attribute)attr).getRedeclaring(null);
				if (attr == checked) {
					return true;
				}
				cont = ((EInverse_attribute)attr).testRedeclaring(null);
			}
		}
		return false;
	}


	EAttribute extract_attribute_partial(String attributeName) throws SdaiException {
		int i;
		EAttribute at;
		if (attributesExpl == null) {
			SdaiModel mod = ((CEntity)this).owning_model;
			take_attributes(mod);
		}
		for (i = 0; i < attributesExpl.length; i++) {
			at = attributesExpl[i];
			if (at.getName(null).equals(attributeName)) {
				return at;
			}
		}
		for (i = 0; i < attributesDeriv.length; i++) {
			at = attributesDeriv[i];
			if (at.getName(null).equals(attributeName)) {
				return at;
			}
		}
		for (i = 0; i < attributesInv.length; i++) {
			at = attributesInv[i];
			if (at.getName(null).equals(attributeName)) {
				return at;
			}
		}
		for (i = 0; i < attributesRedecl.length; i++) {
			at = attributesRedecl[i];
			if (at.getName(null).equals(attributeName)) {
				return at;
			}
		}
		return null;
	}


	final protected AEntity_definition getSupertypesInternal(AEntity_or_view_definition aggr) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (aggr == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		AEntity_definition supertypes = new AEntity_definition();
		AEntity agg = (AEntity)aggr;
		SdaiIterator iter_supt;
		if (agg.myLength < 0) {
//			iter_supt = agg.createIterator();
			agg.resolveAllConnectors();
			iter_supt = new SdaiIterator(agg);
		} else {
			StaticFields staticFields = StaticFields.get();
			iter_supt = staticFields.iter_sup;
			if (iter_supt == null) {
				iter_supt = agg.createIterator();
			} else {
				agg.attachIterator(iter_supt);
			}
		}
//		while (staticFields.iter_sup.next()) {
		while (iter_supt.next()) {
			Object obj;
			if (agg.myLength <= 2) {
//				obj = staticFields.iter_sup.myElement;
				obj = iter_supt.myElement;
			} else {
//				obj = ((ListElement)staticFields.iter_sup.myElement).object;
				obj = ((ListElement)iter_supt.myElement).object;
			}
			DataType inst;
			if (obj instanceof SdaiModel.Connector) {
				inst = (DataType)((SdaiModel.Connector)obj).resolveConnector(false, true, false);
			} else {
				inst = (DataType)obj;
			}
			if (inst.express_type == DataType.ENTITY) {
				((CAggregate)supertypes).addAtTheEnd(inst, null);
			}
		}
		return supertypes;
//		} // syncObject
	}


	CWhere_rule [] get_where_rules(AWhere_rule w_rules) throws SdaiException {
		if (w_rules_array != null) {
			return w_rules_array;
		}
		int ln = ((AEntity)w_rules).myLength;
		w_rules_array = new CWhere_rule[ln];
		StaticFields staticFields = StaticFields.get();
		if (staticFields.w_rules_sorting == null) {
			if (ln <= RULES_ARRAY_SIZE) {
				staticFields.w_rules_sorting = new CWhere_rule[RULES_ARRAY_SIZE];
			} else {
				staticFields.w_rules_sorting = new CWhere_rule[ln];
			}
		} else if (ln > staticFields.w_rules_sorting.length) {
			staticFields.w_rules_sorting = new CWhere_rule[ln];
		}
		int count = 0;
		if (iter == null) {
			iter = w_rules.createIterator();
		} else {
			w_rules.attachIterator(iter);
		}
		while(iter.next()) {
			CWhere_rule wrule = (CWhere_rule)w_rules.getCurrentMember(iter);
			((CEntity)wrule).instance_position = wrule.getOrder(null);
			staticFields.w_rules_sorting[count] = wrule;
			w_rules_array[count++] = wrule;
		}
		sortWhereRules(staticFields.w_rules_sorting, w_rules_array, 0, ln);
		return w_rules_array;
	}


	/*private*/static void sortWhereRules(CWhere_rule [] s_rules, CWhere_rule [] rules, int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index &&
						((CEntity)rules[j-1]).instance_position > ((CEntity)rules[j]).instance_position; j--) {
					CWhere_rule r = rules[j-1];
					rules[j-1] = rules[j];
					rules[j] = r;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortWhereRules(rules, s_rules, start_index, middle);
		sortWhereRules(rules, s_rules, middle, end_index);
		if (((CEntity)s_rules[middle-1]).instance_position <= ((CEntity)s_rules[middle]).instance_position) {
			System.arraycopy(s_rules, start_index, rules, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					((CEntity)s_rules[m]).instance_position <= ((CEntity)s_rules[n]).instance_position) {
				rules[i] = s_rules[m++];
			} else {
				rules[i] = s_rules[n++];
			}
		}
	}




	private void enlargeNames(int demand, StaticFields staticFields) {
		int new_length = staticFields.names.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.names = new String[new_length];
	}


	private void enlargeNamesExt(int demand, StaticFields staticFields) {
		int new_length = staticFields.names.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.names = new String[new_length];
		staticFields.part_types = new CEntity_definition[new_length];
	}


/*	void enlargeAttrs() {
		int new_length = attrs.length * 2;
		EAttribute new_attrs[] = new EAttribute[new_length];
		System.arraycopy(attrs, 0, new_attrs, 0, attrs.length);
		attrs = new_attrs;
	}*/


	private static void enlargeIndices() {
		StaticFields staticFields = StaticFields.get();
		int new_length = staticFields.indices.length * 2;
		int new_indices[] = new int[new_length];
		System.arraycopy(staticFields.indices, 0, new_indices, 0, staticFields.indices.length);
		staticFields.indices = new_indices;
	}


	void list_attributes() throws SdaiException {
		EAttribute attr;
		StaticFields staticFields = StaticFields.get();
		for (int j = 0; j < noOfPartialEntityTypes; j++) {
			CEntityDefinition entity = partialEntityTypes[j];
System.out.println("partial_entity: " + entity);
			AEntity_definition supertypes = entity.getSupertypes(null);
			for (int k = 1; k <= ((AEntity)supertypes).myLength; k++) {
				CEntity_definition supertp = (CEntity_definition)supertypes.getByIndex(k);
System.out.println("     supertype: " + supertp);
			}
System.out.println("------------------------------------");
			AAttribute all_attr = entity.getAttributes(null, null);
			if (staticFields.it2 == null) {
				staticFields.it2 = all_attr.createIterator();
			} else {
				all_attr.attachIterator(staticFields.it2);
			}
			int i = 0;
			while (staticFields.it2.next()) {
				if (((AEntity)all_attr).myLength <= 2) {
					attr = (EAttribute)staticFields.it2.myElement;
				} else {
					attr = (EAttribute)((ListElement)staticFields.it2.myElement).object;
				}
System.out.println("     i = " + i + "   attr: " + attr);
				i++;
			}
		}
	}

}
