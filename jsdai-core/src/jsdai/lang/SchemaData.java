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
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class SchemaData {
	SdaiModel model; // the model to which this SchemaData belongs to

	// set by initializeSchemaData() and init()
	int noOfEntityDataTypes;
	int noOfComplexEntityDataTypes;
	int noOfShortNames;
	byte bShortNames[][];
	byte bLongNames[][];
	byte bNames[][];
	String sShortNames[];  // e.g. "CRTPNT"
	String sLongNames[];   // e.g. "CARTESIAN_POINT" - capital writing
	String sNames[];       // e.g. "CARTESIAN_POINT" - capital writing
	String sClassNames[]; // e.g. "jsdai.SConfig_control_design.CCartesian_point"
	int toShort[];
	int fromShort[];
	int toLong[];
	int fromLong[];
	String e_aliases [];
	SchemaData [] toInterfaced;
	CEntityDefinition entities[];

	String [] bypass_rules [];

	private final int ARRAY_FOR_ENTITIES_SORTING_SIZE = 512;

	int defTypesCount;
	CDefined_type def_types [];
	String dt_aliases [];
	String dtNames [];
	DataType [] data_types;

	int aux[];
	int aux2[];
	int involved = -1;

	// set by linkEarlyBinding
	SSuper super_inst;
	CSchema_definition schema;

	// set only when needed 
	Class bAggregates[];
	private Class bClasses[];
	private Class bInterfaces[];

	AggregationType nested_types [];
	int nesting_depth;


	SchemaData(SdaiModel model) {
		this.model = model;
		noOfEntityDataTypes = -1;
		noOfShortNames = 0;
		defTypesCount = -1;
		nested_types = new AggregationType[10];
	}



	void initializeSchemaData(int noOfEntityDataTypes, int noOfComplexEntityDataTypes) {
		bAggregates = new Class[noOfEntityDataTypes]; // used in EntityExtent
		bClasses = new Class[noOfComplexEntityDataTypes]; // used somewhere
		bInterfaces = new Class[noOfEntityDataTypes];  // seems not used so far
		bNames = new byte[noOfEntityDataTypes][];  // used in several classes
		bShortNames = new byte[noOfEntityDataTypes][]; // used in several classes
		bLongNames = new byte[noOfEntityDataTypes][]; // used somewhere
		sNames = new String[noOfEntityDataTypes]; // seems not used so far
		sClassNames = new String[noOfEntityDataTypes]; // introduced recently; not used so far
		sShortNames = new String[noOfEntityDataTypes]; // seems not used so far 
		sLongNames = new String[noOfEntityDataTypes]; // seems not used so far
		toShort = new int[noOfEntityDataTypes]; // used in several classes
		fromShort = new int[noOfEntityDataTypes]; // used somewhere
		toLong = new int[noOfEntityDataTypes]; // seems not used so far
		fromLong = new int[noOfEntityDataTypes]; // used somewhere
		toInterfaced = new SchemaData[noOfEntityDataTypes]; // used somewhere
		entities = new CEntityDefinition[noOfEntityDataTypes]; // introduced recently
		aux = new int[noOfEntityDataTypes];
		this.noOfEntityDataTypes = noOfEntityDataTypes;
		this.noOfComplexEntityDataTypes = noOfComplexEntityDataTypes;
if (SdaiSession.debug2) System.out.println("  SchemaData   noOfEntityDataTypes = " + 
noOfEntityDataTypes + "    initialized schema for model: " + model.name);
	}

	void initializeDefinedTypes() throws SdaiException {
		defTypesCount = model.lengths[SdaiSession.DEFINED_TYPE] + 
			model.lengths[SdaiSession.REFERENCED_DECL_TYPE_DECL] +
			model.lengths[SdaiSession.TYPE_DECL_USED_DECL] +
			model.lengths[SdaiSession.IMPLICIT_DECL_TYPE_DECL];
		def_types = new CDefined_type[defTypesCount];
		dtNames = new String[defTypesCount];
		StaticFields staticFields = StaticFields.get();
		if (staticFields.def_types_sorting == null) {
			staticFields.def_types_sorting = new CDefined_type[defTypesCount];
		} else if (defTypesCount > staticFields.def_types_sorting.length) {
			int new_length = staticFields.def_types_sorting.length * 2;
			if (defTypesCount > new_length) {
				new_length = defTypesCount;
			}
			staticFields.def_types_sorting = new CDefined_type[new_length];
		}
		int i, k = 0;
		CEntity d_types [];
		d_types = model.instances_sim[SdaiSession.DEFINED_TYPE];
		for (i = 0; i < model.lengths[SdaiSession.DEFINED_TYPE]; i++) {
			def_types[k] = (CDefined_type)d_types[i];
			staticFields.def_types_sorting[k] = def_types[k];
			k++;
		}
		d_types = model.instances_sim[SdaiSession.REFERENCED_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.REFERENCED_DECL_TYPE_DECL]; i++) {
			CReferenced_declaration$type_declaration dec_ref = 
				(CReferenced_declaration$type_declaration)d_types[i];
			def_types[k] = (CDefined_type)dec_ref.getDefinition(null);
			staticFields.def_types_sorting[k] = def_types[k];
			k++;
		}
		d_types = model.instances_sim[SdaiSession.TYPE_DECL_USED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.TYPE_DECL_USED_DECL]; i++) {
			CType_declaration$used_declaration dec_used = 
				(CType_declaration$used_declaration)d_types[i];
			def_types[k] = (CDefined_type)dec_used.getDefinition(null);
			staticFields.def_types_sorting[k] = def_types[k];
			k++;
		}
		d_types = model.instances_sim[SdaiSession.IMPLICIT_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.IMPLICIT_DECL_TYPE_DECL]; i++) {
			CImplicit_declaration$type_declaration dec_imp = 
				(CImplicit_declaration$type_declaration)d_types[i];
			def_types[k] = (CDefined_type)dec_imp.getDefinition(null);
			staticFields.def_types_sorting[k] = def_types[k];
			k++;
		}
		sortTypes(staticFields);
		for (i = 0; i < defTypesCount; i++) {
			dtNames[i] = def_types[i].getNameUpperCase();
		}
//System.out.println("   SchemaData   *****MODEL: " + model.name + 
//"    SCHEMA: " + model.underlying_schema.getName(null));
//for (i = 0; i < defTypesCount; i++) {
//System.out.println("   SchemaData   ###def_type: " + dtNames[i]);
//}System.out.println();
	}

	Class getEntityClassByIndex(int index) throws SdaiException {
		if (index < 0 || index >= bClasses.length) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (bClasses[index] == null) {
//System.out.println("  SchemaData getEntityClassByIndex invoked for  schema : " + schema.getName(null)
//+ "    entity required: " + entities[index].getName(null));
			Class c = entities[index].getEntityClass();
			bClasses[index] = c;
			sClassNames[index] = c.getName();
		}
		return bClasses[index];
	}


	Class getEntityInterfaceByIndex(int index) throws SdaiException {
		if (index < 0 || index >= bInterfaces.length) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (bInterfaces[index] == null) {
			Class c = entities[index].getEntityInterface();
			bInterfaces[index] = c;
		}
		return bInterfaces[index];
	}


	Class getAggregateClassByIndex(int index) throws SdaiException {
		if (index < 0 || index >= bAggregates.length) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (bAggregates[index] == null) {
			bAggregates[index] = entities[index].getEntityAggregate();
		}
		return bAggregates[index];
	}

	CSchema_definition init() throws SdaiException {
long time1, time2, time3, time4, time5;
		int i, j;
		int var;
		int count;
		CSchema_definition schema = (CSchema_definition)model.instances_sim[SdaiSession.SCHEMA_DEFINITION][0];
		if (model == SdaiSession.baseDictionaryModel) {
			count = model.lengths[SdaiSession.ENTITY_DEFINITION];
			setDataType(model, SdaiSession.ENTITY_DEFINITION, DataType.ENTITY);
			var = 1;
		} else {
			count = bClasses.length;
			var = 2;
		}

//System.out.println("  SchemaData    count : " + count + 
//"   noOfEntityDataTypes: " + noOfEntityDataTypes);
		CEntityDefinition edef;
		CEntity entity_instances [];
time1 = System.currentTimeMillis();
		if (var == 1) {
			entity_instances = model.instances_sim[SdaiSession.ENTITY_DEFINITION];
			for (i = 0; i < count; i++) {
				edef = (CEntityDefinition)entity_instances[i];
				edef.ssuper = super_inst;
				entities[i] = edef;
//System.out.println("  SchemaData   entity(" + i + "): " + edef.getName(null) + 
//"     upper: " + edef.getNameUpperCase());
				toInterfaced[i] = this;
			}
		} else {
			int k = 0;
//System.out.println("  SchemaData   ***** MODEL: " + model.name);
			// set entities array for local entity declarations
			entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_LOCAL_DECL];
			for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_LOCAL_DECL]; i++) {
				CEntity_declaration$local_declaration dec_loc = 
					(CEntity_declaration$local_declaration)entity_instances[i];
				entities[k] = (CEntity_definition)dec_loc.getDefinition(null);
				k++;
			}

			if (model != SdaiSession.baseComplexModel) {
				// set entities array for referenced entity declarations
				entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_REFERENCED_DECL];
				for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_REFERENCED_DECL]; i++) {
					CEntity_declaration$referenced_declaration dec_ref = 
						(CEntity_declaration$referenced_declaration)entity_instances[i];
					entities[k] = (CEntity_definition)dec_ref.getDefinition(null);
					k++;
				}
	
				// set entities array for used entity declarations
				entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_USED_DECL];
				for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_USED_DECL]; i++) {
					CEntity_declaration$used_declaration dec_used = 
						(CEntity_declaration$used_declaration)entity_instances[i];
					entities[k] = (CEntity_definition)dec_used.getDefinition(null);
					k++;
				}
	
				// set entities array for implicit entity declarations
				entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_IMPLICIT_DECL];
				for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_IMPLICIT_DECL]; i++) {
					CEntity_declaration$implicit_declaration dec_imp = 
						(CEntity_declaration$implicit_declaration)entity_instances[i];
					entities[k] = (CEntity_definition)dec_imp.getDefinition(null);
					k++;
				}
			}
//for (i = 0; i < count; i++) {
//System.out.println("    *****NORMAL NAME: " + sNames[i] + "   LONG NAME: " + sLongNames[i]);
//}System.out.println();
		}

time2 = System.currentTimeMillis();
		sortEntities();
time3 = System.currentTimeMillis();
		for (i = 0; i < count; i++) {
			if (entities[i].owning_model == model) {
				entities[i].index = i;
			}
			edef = entities[i];
			sNames[i] = edef.getNameUpperCase();
			bNames[i] = fromString(sNames[i]);
			if (edef.testShort_name(null)) {
				sShortNames[i] = edef.getShort_name(null);
			} else {
				sShortNames[i] = null;
			}
if (SdaiSession.debug2) System.out.println("  entity : " + edef.getCorrectName() + "  complex= " + edef.complex + 
"   sNames: " + sNames[i] + "  entity ident: " + ((CEntity)edef).instance_identifier);
			if (edef.complex == 2 && (model != SdaiSession.baseComplexModel) ) {
				boolean short_name_started = false;
				String name_from_super = null;
				String short_name_from_super = null;
				String sh_n;

//				if (edef.partialEntityTypes == null || edef.del_supertype) {
				if (edef.partialEntityTypes == null) {
					StaticFields staticFields = StaticFields.get();
					edef.prepareExternalMappingData(staticFields);
//edef.del_supertype = false;
				}
				CEntity_definition [] types = edef.partialEntityTypes;
				for (j = 0; j < types.length; j++) {
					CEntityDefinition edef_in_super = (CEntityDefinition)types[j];
					String partial_name = edef_in_super.getNameUpperCase();
					if (edef_in_super.testShort_name(null)) {
						sh_n = edef_in_super.getShort_name(null);
						if (short_name_started) {
							short_name_from_super += ("$" + sh_n);
						} else {
							if (j == 0) {
								short_name_from_super = sh_n;
							} else {
								short_name_from_super = name_from_super + "$" + sh_n;
							}
						}
						short_name_started = true;
					} else if (short_name_started) {
						short_name_from_super += ("$" + partial_name);
					}
					if (j == 0) {
						name_from_super = partial_name;
					} else {
						name_from_super += ("$" + partial_name);
					}
				}
				sLongNames[i] = name_from_super;
				if (short_name_started) {
					sShortNames[i] = short_name_from_super;
				}
//System.out.println("  SchemaData    i = " + i + "  ****long: " + name_from_super + 
//"  ****short: " + short_name_from_super);
			} else {
				sLongNames[i] = edef.getNameUpperCase();
			}
			fromLong[i] = i;
if (SdaiSession.debug2) System.out.println("  parent schema = " + edef.owning_model.described_schema.getName(null));
if (SdaiSession.debug2) System.out.println(" class name = " + sClassNames[i] + "  i: " + i);
		}
time4 = System.currentTimeMillis();
		sortEntitiesByLongNames();
		sortEntitiesByShortNames();
		for (i = 0; i < count; i++) {
			toLong[fromLong[i]] = i;
			bLongNames[i] = fromString(sLongNames[i]);
			toShort[i] = -1;
			entities[i].inv_count = 0;
if (SdaiSession.debug2) System.out.println(" LONG NAME: " + sLongNames[i] + "   NORMAL NAME: " + sNames[i]);
		}
		for (i = 0; i < noOfShortNames; i++) {
			toShort[fromShort[i]] = i;
			bShortNames[i] = fromString(sShortNames[i]);
		}
time5 = System.currentTimeMillis();
if (SdaiSession.debug2 && model == SdaiSession.baseMappingModel) {
System.out.println("*****MODEL: " + model.name + "    SCHEMA: " + schema.getName(null));
for (i = 0; i < count; i++) {
System.out.println("    *****NORMAL NAME: " + sNames[i] + "   LONG NAME: " + sLongNames[i]);
}System.out.println();
}
		if (schema != null) {
			schema.modelDictionary = model;
		}
		model.described_schema = schema;
if (SdaiSession.debug2) System.out.println("MODEL: " + model.name + "    SCHEMA: " + 
schema.getName(null) + "   noOfShortNames = " + noOfShortNames);
if (SdaiSession.debug2) for (i = 0; i < count; i++) {
System.out.println(" ind = " + i + "     NORMAL NAME: " + sNames[i] + //"   LONG NAME: " + sLongNames[i] +
//"   from long: " + fromLong[i]);
"   entity name: " + entities[i].getNameUpperCase());
//"   SHORT NAME: " + sShortNames[i]);
}
if (SdaiSession.debug2) System.out.println();
		this.schema = schema;
SdaiSession.tm1+=(time2-time1);SdaiSession.tm2+=(time3-time2);
SdaiSession.tm3+=(time4-time3);SdaiSession.tm4+=(time5-time4);
SdaiSession.tm0+=(time5-time1);
if (SdaiSession.debug2) SdaiSession.println("init() =" + model.name + ": " +
SdaiSession.tm1 + "+" +
SdaiSession.tm2 + "+" +
SdaiSession.tm3 + "+" +
SdaiSession.tm4 + "=" +
SdaiSession.tm0 + " milisec");
		for (i = 0; i < model.lengths[SdaiSession.DERIVED_ATTRIBUTE]; i++) {
			CDerived_attribute der_attr = (CDerived_attribute)model.instances_sim[SdaiSession.DERIVED_ATTRIBUTE][i];
			edef = (CEntity_definition)der_attr.getParent(null);
			if (edef.not_expl_attribs == null) {
				edef.not_expl_attribs = new AAttribute();
			}
			((AEntity)edef.not_expl_attribs).addAtTheEnd(der_attr, null);
		}
		for (i = 0; i < model.lengths[SdaiSession.INVERSE_ATTRIBUTE]; i++) {
			CInverse_attribute inv_attr = (CInverse_attribute)model.instances_sim[SdaiSession.INVERSE_ATTRIBUTE][i];
			edef = (CEntity_definition)inv_attr.getParent(null);
			if (edef.not_expl_attribs == null) {
				edef.not_expl_attribs = new AAttribute();
			}
			((AEntity)edef.not_expl_attribs).addAtTheEnd(inv_attr, null);
			edef.inv_count++;
		}
		return schema;
	}

	void prepareData_types() throws SdaiException {
		int i, k = 0;
		int [] type_refs = new int[29];
		type_refs[0] = SdaiSession.AGGREGATION_TYPE;
		type_refs[1] = SdaiSession.ARRAY_TYPE;
		type_refs[2] = SdaiSession.BAG_TYPE;
		type_refs[3] = SdaiSession.BINARY_TYPE;
		type_refs[4] = SdaiSession.BOOLEAN_TYPE;
		type_refs[5] = SdaiSession.DATA_TYPE;
		type_refs[6] = SdaiSession.DEFINED_TYPE;
		type_refs[7] = SdaiSession.ENTITY_DEFINITION;
		type_refs[8] = SdaiSession.ENTITY_SELECT_TYPE;
		type_refs[9] = SdaiSession.ENT_EXT_EXT_SELECT_TYPE;
		type_refs[10] = SdaiSession.ENT_EXT_NON_EXT_SELECT_TYPE;
		type_refs[11] = SdaiSession.ENT_EXT_SELECT_TYPE;
		type_refs[12] = SdaiSession.ENT_NON_EXT_SELECT_TYPE;
		type_refs[13] = SdaiSession.ENUMERATION_TYPE;
		type_refs[14] = SdaiSession.EXTENDED_ENUM_TYPE;
		type_refs[15] = SdaiSession.EXTENDED_EXTENSIBLE_ENUM_TYPE;
		type_refs[16] = SdaiSession.EXTENDED_SELECT_TYPE;
		type_refs[17] = SdaiSession.EXT_EXT_SELECT_TYPE;
		type_refs[18] = SdaiSession.EXT_NON_EXT_SELECT_TYPE;
		type_refs[19] = SdaiSession.EXTENSIBLE_ENUM_TYPE;
		type_refs[20] = SdaiSession.EXTENSIBLE_SELECT_TYPE;
		type_refs[21] = SdaiSession.INTEGER_TYPE;
		type_refs[22] = SdaiSession.LIST_TYPE;
		type_refs[23] = SdaiSession.LOGICAL_TYPE;
		type_refs[24] = SdaiSession.NON_EXT_SELECT_TYPE;
		type_refs[25] = SdaiSession.NUMBER_TYPE;
		type_refs[26] = SdaiSession.REAL_TYPE;
		type_refs[27] = SdaiSession.SET_TYPE;
		type_refs[28] = SdaiSession.STRING_TYPE;
		int count = 0;
		for (i = 0; i < 29; i++) {
			count += model.lengths[type_refs[i]];
		}
		data_types = new DataType[count];
		StaticFields staticFields = StaticFields.get();
		if (staticFields.data_types_sorting == null || count > staticFields.data_types_sorting.length) {
			staticFields.data_types_sorting = new DataType[count];
		}
		for (int j = 0; j < 29; j++) {
			CEntity [] dt_types = model.instances_sim[type_refs[j]];
//System.out.println(" SchemaData   model.lengths[" + type_refs[j] + 
//"] = " + model.lengths[type_refs[j]] +  "  j = " + j);
			for (i = 0; i < model.lengths[type_refs[j]]; i++) {
				data_types[k] = (DataType)dt_types[i];
				if (data_types[k] == null) {
					String base = SdaiSession.line_separator + AdditionalMessages.DI_BINF + model.name;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
/*CEntity eee = (CEntity)data_types[k];
System.out.println(" SchemaData    type: #" + eee.instance_identifier + "   eee: " + eee.getClass().getName());
String sss = null;
try {
sss = data_types[k].getName(null);
} catch (SdaiException ex) {
System.out.println(" SchemaData    !!!!!!!!!!!!!!!!!!!!  Catched");
}
System.out.println(" SchemaData    k = " + k + 
"   data_type: " + sss);*/
				staticFields.data_types_sorting[k] = data_types[k];
				k++;
			}
		}
		sortData_types(staticFields, count);
	}

/*	void prepareData_types() throws SdaiException {
		int i, k = 0;
		int [] type_refs = new int[16];
		type_refs[0] = SdaiSession.ARRAY_TYPE;
		type_refs[1] = SdaiSession.BAG_TYPE;
		type_refs[2] = SdaiSession.BINARY_TYPE;
		type_refs[3] = SdaiSession.BOOLEAN_TYPE;
		type_refs[4] = SdaiSession.DATA_TYPE;
		type_refs[5] = SdaiSession.DEFINED_TYPE;
		type_refs[6] = SdaiSession.ENTITY_DEFINITION;
		type_refs[7] = SdaiSession.ENUMERATION_TYPE;
		type_refs[8] = SdaiSession.INTEGER_TYPE;
		type_refs[9] = SdaiSession.LIST_TYPE;
		type_refs[10] = SdaiSession.LOGICAL_TYPE;
		type_refs[11] = SdaiSession.NUMBER_TYPE;
		type_refs[12] = SdaiSession.REAL_TYPE;
		type_refs[13] = SdaiSession.SELECT_TYPE;
		type_refs[14] = SdaiSession.SET_TYPE;
		type_refs[15] = SdaiSession.STRING_TYPE;
		int count = 0;
		Object [] dt_types;
		for (i = 0; i < 16; i++) {
if (i == 13) continue;
			dt_types = model.instances_sim[type_refs[i]];
			for (int m = 0; m < model.lengths[type_refs[i]]; m++) {
				if (((DataType)dt_types[m]).testName(null)) {
					count++;
				}
			}
		}
		data_types = new DataType[count];
		if (data_types_sorting == null || count > data_types_sorting.length) {
			data_types_sorting = new DataType[count];
		}

		for (int j = 0; j < 16; j++) {
if (j == 13) continue;
			dt_types = model.instances_sim[type_refs[j]];
//System.out.println(" SchemaData   model.lengths[" + type_refs[j] + 
//"] = " + model.lengths[type_refs[j]]);
			for (i = 0; i < model.lengths[type_refs[j]]; i++) {
				if (((DataType)dt_types[i]).testName(null)) {
					data_types[k] = (DataType)dt_types[i];
//CEntity eee = (CEntity)data_types[k];
//System.out.println(" SchemaData    type: #" + eee.instance_identifier);
//System.out.println(" SchemaData    k = " + k + 
//"   data_type: " + data_types[k].getName(null));
					data_types_sorting[k] = data_types[k];
					k++;
				}
			}
		}
		sortData_types(count);
	}*/


	EData_type find_data_type(String key) throws SdaiException {
		if (data_types == null) {
			prepareData_types();
//if (model.name.equals("SUPPORT_RESOURCE_SCHEMA_DICTIONARY_DATA"))
//if (model.name.equals("AP210_ARM_EXTENDED_DICTIONARY_DATA"))
//for (int i = 0; i < data_types.length; i++) {
//System.out.println(" SchemaData  i = " + i + 
//"   data_type: " + data_types[i].getName(null) + "  its code: " + data_types[i].express_type);
//if (i>0 && data_types[i].getName(null).equals(data_types[i-1].getName(null)))
//System.out.println(" SchemaData !!!!!!!!!!!!!!!!!!!!   DUPLICATE data type ");
//}
		}
		int left = 0, right = data_types.length - 1;
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = data_types[middle].getNameUpperCase().compareTo(key);
			if (comp_res < 0) {
				left = middle + 1;
			} else if (comp_res > 0) {
				right = middle - 1;
			} else {
				return (EData_type)data_types[middle];
			}
		}
		return null;
	}

	EUniqueness_rule findUniquenessRule(String rule) throws SdaiException {
		String ent_name, rule_name;
		int dot = rule.lastIndexOf(PhFileReader.DOT);
		if (dot < 0) {
			ent_name = rule.toUpperCase();
			rule_name = "";
		} else {
			ent_name = rule.substring(0, dot).toUpperCase();
			rule_name = rule.substring(dot + 1);
		}
		int ind = find_entity(0, noOfEntityDataTypes - 1, ent_name);
		if (ind < 0) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		AUniqueness_rule u_rules = ((CEntity_definition)entities[ind]).getUniqueness_rules(null, null);
		if (((CAggregate)u_rules).myLength == 0) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it_sch_dt == null) {
			staticFields.it_sch_dt = u_rules.createIterator();
		} else {
			u_rules.attachIterator(staticFields.it_sch_dt);
		}
		while(staticFields.it_sch_dt.next()) {
			EUniqueness_rule u_rule = u_rules.getCurrentMember(staticFields.it_sch_dt);
			if (rule_name.equals("")) {
				if (!u_rule.testLabel(null)) {
					return u_rule;
				}
			} else {
				if (!u_rule.testLabel(null)) {
					continue;
				}
				if (u_rule.getLabel(null).equals(rule_name)) {
					return u_rule;
				}
			}
		}
		throw new SdaiException(SdaiException.RU_NDEF);
	}

	private void setAggregationTypes(SdaiModel model, int extent, int e_type) throws SdaiException {
		for (int i = 0; i < model.lengths[extent]; i++) {
			EAggregation_type aggr_type = (EAggregation_type)model.instances_sim[extent][i];
			nested_types[0] = (AggregationType)aggr_type;
			((DataType)aggr_type).express_type = e_type;
			nesting_depth = 1;
if (SdaiSession.debug2) System.out.println("  index i = " + i + "    extent = " + extent + 
"   model.name = " + model.name + 
"   name: " + aggr_type.getName(null));
//if (aggr_type.testElement_type(null)) 
			resolveType((CEntity)aggr_type.getElement_type(null));
		}
	}


	private void setDataType(SdaiModel model, int extent, int e_type) throws SdaiException {
		for (int i = 0; i < model.lengths[extent]; i++) {
			DataType data_type = (DataType)model.instances_sim[extent][i];
			data_type.express_type = e_type;
		}
	}


	private void setAttributeType(SdaiModel model, int extent, int attr_type) throws SdaiException {
		for (int i = 0; i < model.lengths[extent]; i++) {
			AttributeDefinition attr_def = (AttributeDefinition)model.instances_sim[extent][i];
			attr_def.attr_tp = attr_type;
		}
	}


	private void resolveType(CEntity el_type) throws SdaiException {
//System.out.println(" in resolveType " + ((CEntity)el_type).instance_identifier 
//+ " name: " + el_type.getClass().getName());
		if (el_type instanceof ESimple_type) {
			resolveSimpleType(el_type);
		} else if (el_type instanceof EAggregation_type) {
			EAggregation_type type_next = (EAggregation_type)el_type;
			nested_types[nesting_depth] = (AggregationType)type_next;
			nesting_depth++;
if (SdaiSession.debug2) System.out.println("  Before resolveType again   nesting_depth = " + nesting_depth); 
			resolveType((CEntity)type_next.getElement_type(null));
		} else if (el_type instanceof CEntity_definition) {
if (SdaiSession.debug2) System.out.println(" case of CEntity_definition = " + ((CEntity)el_type).instance_identifier);
			resolveEntityType((CEntity_definition)el_type);
		} else if (el_type instanceof CDefined_type) {
			CEntity domain = (CEntity)((CDefined_type)el_type).getDomain(null);
			if (domain instanceof ESimple_type) {
				resolveSimpleType(domain);
			} else if (domain instanceof EEnumeration_type) {
				if (nesting_depth == 1) {
					nested_types[0].aggregateClass = A_enumeration.class;
				} else if (nesting_depth == 2) {
					nested_types[0].aggregateClass = Aa_enumeration.class;
					nested_types[0].aggMemberImplClass = A_enumeration.class;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.DI_NESA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			} if (domain instanceof ESelect_type) {
				resolveSelectType(((CDefined_type)el_type).getName(null), (ESelect_type)domain);
			} else {
				resolveType(domain);
			}
		}
	}

	private void resolveSimpleType(CEntity el_type) throws SdaiException {
		if (nesting_depth > 3) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_NESA;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (el_type instanceof CInteger_type) {
			if (nesting_depth == 1) {
				nested_types[0].aggregateClass = A_integer.class;
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_integer.class;
				nested_types[0].aggMemberImplClass = A_integer.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_integer.class;
				nested_types[0].aggMemberImplClass = Aa_integer.class;
			}
		} else if (el_type instanceof CReal_type || el_type instanceof CNumber_type) {
			if (nesting_depth == 1) {
				if (nested_types[0].check_aggregation_double()) {
					nested_types[0].aggregateClass = A_double3.class;
				} else {
					nested_types[0].aggregateClass = A_double.class;
				}
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_double.class;
				nested_types[0].aggMemberImplClass = A_double.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_double.class;
				nested_types[0].aggMemberImplClass = Aa_double.class;
			}
		} else if (el_type instanceof CString_type) {
			if (nesting_depth == 1) {
				nested_types[0].aggregateClass = A_string.class;
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_string.class;
				nested_types[0].aggMemberImplClass = A_string.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_string.class;
				nested_types[0].aggMemberImplClass = Aa_string.class;
			}
		} else if (el_type instanceof CBinary_type) {
			if (nesting_depth == 1) {
				nested_types[0].aggregateClass = A_binary.class;
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_binary.class;
				nested_types[0].aggMemberImplClass = A_binary.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_binary.class;
				nested_types[0].aggMemberImplClass = Aa_binary.class;
			}
		} else if (el_type instanceof CLogical_type) {
			if (nesting_depth == 1) {
				nested_types[0].aggregateClass = A_enumeration.class;
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_enumeration.class;
				nested_types[0].aggMemberImplClass = A_enumeration.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_enumeration.class;
				nested_types[0].aggMemberImplClass = Aa_enumeration.class;
			}
		} else if (el_type instanceof CBoolean_type) {
			if (nesting_depth == 1) {
				nested_types[0].aggregateClass = A_boolean.class;
			} else if (nesting_depth == 2) {
				nested_types[0].aggregateClass = Aa_boolean.class;
				nested_types[0].aggMemberImplClass = A_boolean.class;
			} else if (nesting_depth == 3) {
				nested_types[0].aggregateClass = Aaa_boolean.class;
				nested_types[0].aggMemberImplClass = Aa_boolean.class;
			}
		}
	}

	private void resolveEntityType(CEntity_definition def) throws SdaiException {
		for (int i = 0; i < nesting_depth; i++) {
			nested_types[i].base = def;
			nested_types[i].order = nesting_depth - i;
		}
	}

	private void resolveSelectType(String name, ESelect_type sel) throws SdaiException {
		for (int i = 0; i < nesting_depth; i++) {
			nested_types[i].base = (SelectType)sel;
			nested_types[i].order = nesting_depth - i;
			nested_types[i].name = name;
		}
		nested_types[nesting_depth - 1].select = (SelectType)sel;
	}

	void linkEarlyBindingInit(SdaiModel origin) throws SdaiException {
		if (model != SdaiSession.baseDictionaryModel && super_inst == null) {
			String str;
			if (model == SdaiSession.baseMappingModel) {
				str = SdaiSession.MAPPING_PACKAGE + SdaiSession.MAPPING_FILE;
			} else {
//System.out.println("  +++++ SchemaData  model: " + model.name);
				String name = model.described_schema.getName(null);
				int name_ln = name.length();// - constants.ENDING_FOR_DICT;
				str = name.substring(0, name_ln);
				str =  SdaiSession.SCHEMA_PREFIX + CEntity.normalise(str) + ".S" + CEntity.normalise(str);
			}
			try {
				Class cl =
					Class.forName(str, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
//System.out.println("  Class for field 'ss' = " + cl.getName());
				Field field = cl.getField("ss");
				super_inst =  (SSuper)field.get(null);
//System.out.println(" SchemaData ***** MODEL: " + model.name);
//System.out.println(" SchemaData ***** schemaData: " + this);
//System.out.println(" In schemaData ***** super_inst: " + super_inst);
			} catch (ClassNotFoundException ex) {
				throw new SdaiException(SdaiException.SY_ERR, 
					"Special class for schema definition not found: " + str);
			} catch (java.lang.NoSuchFieldException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			} catch (java.lang.IllegalAccessException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
		}
		int i;
		SchemaData sch_data;
		if (origin != null) {
			sch_data = origin.schemaData;
		} else {
			sch_data = null;
		}
//time1 = System.currentTimeMillis();
if (SdaiSession.debug) System.out.println(" SchemaData    model: " + model.name);
		for (i = 0; i < entities.length; i++) {
if (SdaiSession.debug) System.out.println(" SchemaData BEGIN entity: " + entities[i].getCorrectName() +
"   its owning_model: " + entities[i].owning_model.name);
//if (entities[i]==null) System.out.println("+++++ SchemaData  entities[i] is NULL  model: " + model.name);
			if (entities[i].owning_model == model) {
				entities[i].ssuper = super_inst;
				toInterfaced[i] = this;
			} else {
				toInterfaced[i] = entities[i].owning_model.schemaData;
			}
		}
	}

	void linkEarlyBinding(SdaiModel origin) throws SdaiException {
long time1, time2, time3, time4, time5, time6;
time1 = System.currentTimeMillis();
		int i;
time2 = System.currentTimeMillis();

		setDataType(model, SdaiSession.AGGREGATION_TYPE, DataType.AGGREGATE);
		setDataType(model, SdaiSession.BINARY_TYPE, DataType.BINARY);
		setDataType(model, SdaiSession.BOOLEAN_TYPE, DataType.BOOLEAN);
		setDataType(model, SdaiSession.DATA_TYPE, DataType.DATA_TYPE);
		setDataType(model, SdaiSession.DEFINED_TYPE, DataType.DEFINED_TYPE);
//		setDataType(model, SdaiSession.ENTITY_DEFINITION, DataType.ENTITY);
		setDataType(model, SdaiSession.ENTITY_SELECT_TYPE, DataType.ENTITY_SELECT);
		setDataType(model, SdaiSession.ENT_EXT_EXT_SELECT_TYPE, DataType.ENT_EXT_EXT_SELECT);
		setDataType(model, SdaiSession.ENT_EXT_NON_EXT_SELECT_TYPE, DataType.ENT_EXT_NON_EXT_SELECT);
		setDataType(model, SdaiSession.ENT_EXT_SELECT_TYPE, DataType.ENT_EXT_SELECT);
		setDataType(model, SdaiSession.ENT_NON_EXT_SELECT_TYPE, DataType.ENT_NON_EXT_SELECT);
		setDataType(model, SdaiSession.ENUMERATION_TYPE, DataType.ENUMERATION);
		setDataType(model, SdaiSession.EXTENDED_ENUM_TYPE, DataType.EXTENDED_ENUM);
		setDataType(model, SdaiSession.EXTENDED_EXTENSIBLE_ENUM_TYPE, DataType.EXTENDED_EXTENSIBLE_ENUM);
		setDataType(model, SdaiSession.EXTENDED_SELECT_TYPE, DataType.EXTENDED_SELECT);
		setDataType(model, SdaiSession.EXT_EXT_SELECT_TYPE, DataType.EXT_EXT_SELECT);
		setDataType(model, SdaiSession.EXT_NON_EXT_SELECT_TYPE, DataType.EXT_NON_EXT_SELECT);
		setDataType(model, SdaiSession.EXTENSIBLE_ENUM_TYPE, DataType.EXTENSIBLE_ENUM);
		setDataType(model, SdaiSession.EXTENSIBLE_SELECT_TYPE, DataType.EXTENSIBLE_SELECT);
		setDataType(model, SdaiSession.INTEGER_TYPE, DataType.INTEGER);
		setDataType(model, SdaiSession.LOGICAL_TYPE, DataType.LOGICAL);
		setDataType(model, SdaiSession.NON_EXT_SELECT_TYPE, DataType.NON_EXT_SELECT);
		setDataType(model, SdaiSession.NUMBER_TYPE, DataType.NUMBER);
		setDataType(model, SdaiSession.REAL_TYPE, DataType.REAL);
		setDataType(model, SdaiSession.SELECT_TYPE, DataType.SELECT);
		setDataType(model, SdaiSession.STRING_TYPE, DataType.STRING);

		setAggregationTypes(model, SdaiSession.ARRAY_TYPE, DataType.ARRAY);
		setAggregationTypes(model, SdaiSession.BAG_TYPE, DataType.BAG);
		setAggregationTypes(model, SdaiSession.LIST_TYPE, DataType.LIST);
		setAggregationTypes(model, SdaiSession.SET_TYPE, DataType.SET);

		setAttributeType(model, SdaiSession.EXPLICIT_ATTRIBUTE, AttributeDefinition.EXPLICIT);
		setAttributeType(model, SdaiSession.DERIVED_ATTRIBUTE, AttributeDefinition.DERIVED);
		setAttributeType(model, SdaiSession.INVERSE_ATTRIBUTE, AttributeDefinition.INVERSE);

time3 = System.currentTimeMillis();
//		model.described_schema.prepareSubtypes(origin);

		CEntityDefinition edef;
time4 = System.currentTimeMillis();
		StaticFields staticFields;
		if (model == SdaiSession.baseDictionaryModel) {
			staticFields = StaticFields.get();
			for (i = 0; i < model.lengths[SdaiSession.ENTITY_DEFINITION]; i++) {
				edef = (CEntityDefinition)model.instances_sim[SdaiSession.ENTITY_DEFINITION][i];
if (SdaiSession.debug2) System.out.println(" ENTITY CONSIDERED: " + edef.instance_identifier +
"  name: " + edef.getCorrectName());
				edef.setEarlyBindingBaseDictionary(staticFields, model);
			}
		}
time5 = System.currentTimeMillis();
if (SdaiSession.debug2)
for (i = 0; i < model.lengths[SdaiSession.ENTITY_DEFINITION]; i++) {
System.out.println("  entity__: " + entities[i].getCorrectName());}


		int [] select_refs = new int[10];
		select_refs[0] = SdaiSession.ENTITY_SELECT_TYPE;
		select_refs[1] = SdaiSession.ENT_NON_EXT_SELECT_TYPE;
		select_refs[2] = SdaiSession.NON_EXT_SELECT_TYPE;

		select_refs[3] = SdaiSession.ENT_EXT_NON_EXT_SELECT_TYPE;
		select_refs[4] = SdaiSession.EXTENDED_SELECT_TYPE;
		select_refs[5] = SdaiSession.EXT_NON_EXT_SELECT_TYPE;

		select_refs[6] = SdaiSession.ENT_EXT_EXT_SELECT_TYPE;
		select_refs[7] = SdaiSession.ENT_EXT_SELECT_TYPE;
		select_refs[8] = SdaiSession.EXT_EXT_SELECT_TYPE;
		select_refs[9] = SdaiSession.EXTENSIBLE_SELECT_TYPE;

		int ext_count = model.lengths[select_refs[1]] + model.lengths[select_refs[2]] +
			model.lengths[select_refs[3]] + model.lengths[select_refs[5]] + model.lengths[select_refs[6]] +
			model.lengths[select_refs[7]] + model.lengths[select_refs[8]];
		SdaiContext saved_context = null;
		if (ext_count > 0) {
			saved_context = model.repository.session.sdai_context;
			model.repository.session.setSdaiContext(new SdaiContext(schema, null, null));
//System.out.println("SchemaData    SdaiContext created for schema: " + schema.getName(null));
		}

//		staticFields = StaticFields.get();
//		staticFields.context_schema = model.underlying_schema;
      model.early_binding_linking = true;
		for (int j = 0; j < 10; j++) {
			CEntity [] sel_types = model.instances_sim[select_refs[j]];
			for (i = 0; i < model.lengths[select_refs[j]]; i++) {
				ESelect_type select = (ESelect_type)sel_types[i];
				((SelectType)select).enumerateSelectPaths(model.repository.session.sdai_context);
			}
		}
//		staticFields.context_schema = null;
		if (ext_count > 0) {
			model.repository.session.sdai_context = saved_context;
		}


//		for (i = 0; i < model.lengths[SdaiSession.NON_EXT_SELECT_TYPE]; i++) {
//			ESelect_type select = (ESelect_type)model.instances_sim[SdaiSession.NON_EXT_SELECT_TYPE][i];
//			((SelectType)select).enumerateSelectPaths();
//		}
time6 = System.currentTimeMillis();
SdaiSession.tmbind1+=(time2-time1);SdaiSession.tmbind2+=(time3-time2);
SdaiSession.tmbind3+=(time4-time3);SdaiSession.tmbind4+=(time5-time4);SdaiSession.tmbind5+=(time6-time5);
SdaiSession.tmbind0+=(time6-time1);
if (SdaiSession.debug2) SdaiSession.println("linkEarlyBinding() =" + model.name + ": " +
SdaiSession.tmbind1 + "+" +
SdaiSession.tmbind2 + "+" +
SdaiSession.tmbind3 + "+" +
SdaiSession.tmbind4 + "+" +
SdaiSession.tmbind5 + "=" +
SdaiSession.tmbind0 + " milisec");
	}

	byte [] fromString(String str) {
		byte bytes[] = new byte[str.length()];

		for (int l = 0; l < str.length(); l++) {
			bytes[l] = (byte)str.charAt(l);
		}
		return bytes;
	}

	void sortEntities() {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.for_entities_sorting == null) {
			if (ARRAY_FOR_ENTITIES_SORTING_SIZE > noOfEntityDataTypes) {
				staticFields.for_entities_sorting = new CEntityDefinition[ARRAY_FOR_ENTITIES_SORTING_SIZE];
			} else {
				staticFields.for_entities_sorting = new CEntityDefinition[noOfEntityDataTypes];
			}
		} else if (noOfEntityDataTypes > staticFields.for_entities_sorting.length) {
			staticFields.for_entities_sorting = new CEntityDefinition[noOfEntityDataTypes];
		}
/*if (noOfEntityDataTypes > entities.length || noOfEntityDataTypes > for_entities_sorting.length) {
try {
System.out.println("SchemaData*****MODEL: " + model.name + "    SCHEMA: " + schema.getName(null));
System.out.println("SchemaData    <<<>>> noOfEntityDataTypes: " + noOfEntityDataTypes + 
"   entities.length: " + entities.length + "   for_entities_sorting.length: " + for_entities_sorting.length);
for (int i = 0; i < entities.length; i++) {
System.out.println("    ***** NAME: " + entities[i].getNameUpperCase() + "   LONG NAME: " + sLongNames[i]);
}System.out.println();
} catch (SdaiException ex) {
}
}*/
//System.out.println("SchemaData    <<<>>> noOfEntityDataTypes: " + noOfEntityDataTypes);
//		for (int i = 0; i < noOfEntityDataTypes; i++) {
//			for_entities_sorting[i] = entities[i];
//		}
		System.arraycopy(entities, 0, staticFields.for_entities_sorting, 0, noOfEntityDataTypes);
		sortEntities(staticFields.for_entities_sorting, entities, 0, noOfEntityDataTypes);
		for (int i = 0; i < noOfEntityDataTypes; i++) {
			staticFields.for_entities_sorting[i] = null;
		}
	}
	private void sortEntities(CEntityDefinition [] s_entities, CEntityDefinition [] entities, 
			int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index && 
						entities[j-1].getNameUpperCase().compareTo(entities[j].getNameUpperCase()) > 0; j--) {
					CEntityDefinition def = entities[j-1];
					entities[j-1] = entities[j];
					entities[j] = def;
				}
			}
	    return;
		}
		int middle = (start_index + end_index)/2;
		sortEntities(entities, s_entities, start_index, middle);
		sortEntities(entities, s_entities, middle, end_index);
		if (s_entities[middle-1].getNameUpperCase().compareTo(s_entities[middle].getNameUpperCase()) <= 0) {
			System.arraycopy(s_entities, start_index, entities, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && 
					s_entities[m].getNameUpperCase().compareTo(s_entities[n].getNameUpperCase()) <= 0) {
				entities[i] = s_entities[m++];
			} else {
				entities[i] = s_entities[n++];
			}
		}
	}

	void sortEntitiesByLongNames() {
		for (int i = 0; i < noOfEntityDataTypes; i++) {
			sClassNames[i] = sLongNames[i];
			toLong[i] = fromLong[i];
		}
		sortEntitiesByNames(sClassNames, sLongNames, toLong, fromLong, 0, noOfEntityDataTypes);
	}
	void sortEntitiesByShortNames() {
		int count = 0;
		for (int i = 0; i < noOfEntityDataTypes; i++) {
			if (sShortNames[i] != null) {
				sClassNames[count] = sShortNames[count] = sShortNames[i];
				toLong[count] = fromShort[count] = i;
				count++;
			}
		}
		noOfShortNames = count;
		sortEntitiesByNames(sClassNames, sShortNames, toLong, fromShort, 0, count);
	}
	private void sortEntitiesByNames(String [] s_names, String [] names, 
			int [] s_indices, int [] indices, int start_index, int end_index) {
		int i;
		int m, n;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j = i; j > start_index && names[j-1].compareTo(names[j]) > 0; j--) {
					String str = names[j-1];
					names[j-1] = names[j];
					names[j] = str;
					m = indices[j-1];
					indices[j-1] = indices[j];
					indices[j] = m;
				}
			}
	    return;
		}
		int middle = (start_index + end_index)/2;
		sortEntitiesByNames(names, s_names, indices, s_indices, start_index, middle);
		sortEntitiesByNames(names, s_names, indices, s_indices, middle, end_index);
		if (s_names[middle-1].compareTo(s_names[middle]) <= 0) {
			System.arraycopy(s_names, start_index, names, start_index, gap);
			System.arraycopy(s_indices, start_index, indices, start_index, gap);
			return;
		}
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && s_names[m].compareTo(s_names[n]) <= 0) {
				names[i] = s_names[m];
				indices[i] = s_indices[m++];
			} else {
				names[i] = s_names[n];
				indices[i] = s_indices[n++];
			}
		}
	}

	private void sortTypes(StaticFields staticFields) {
		sortTypes(staticFields.def_types_sorting, def_types, 0, defTypesCount);
		for (int i = 0; i < defTypesCount; i++) {
			staticFields.def_types_sorting[i] = null;
		}
	}
	private static void sortTypes(CDefined_type [] def_types_sorting, CDefined_type [] def_types, 
			int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index && 
						def_types[j-1].getNameUpperCase().compareTo(def_types[j].getNameUpperCase()) > 0; j--) {
					CDefined_type def = def_types[j-1];
					def_types[j-1] = def_types[j];
					def_types[j] = def;
				}
			}
	    return;
		}
		int middle = (start_index + end_index)/2;
		sortTypes(def_types, def_types_sorting, start_index, middle);
		sortTypes(def_types, def_types_sorting, middle, end_index);
		if (def_types_sorting[middle-1].getNameUpperCase().compareTo(def_types_sorting[middle].getNameUpperCase()) <= 0) {
			System.arraycopy(def_types_sorting, start_index, def_types, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && 
					def_types_sorting[m].getNameUpperCase().compareTo(def_types_sorting[n].getNameUpperCase()) <= 0) {
				def_types[i] = def_types_sorting[m++];
			} else {
				def_types[i] = def_types_sorting[n++];
			}
		}
	}


	private void sortData_types(StaticFields staticFields, int count) {
		sortData_types(staticFields.data_types_sorting, data_types, 0, count);
		for (int i = 0; i < count; i++) {
			staticFields.data_types_sorting[i] = null;
		}
/*try {
System.out.println(" SchemaData   ********************* schema: " + model.name);
for (int i = 0; i < data_types.length; i++) {
System.out.println(" SchemaData    i = " + i + 
"   data_type: " + data_types[i].getName(null));
}
} catch (SdaiException ex) {
}*/
	}
	private static void sortData_types(DataType [] data_types_sorting, DataType [] data_types, 
			int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index && 
						data_types[j-1].getNameUpperCase().compareTo(data_types[j].getNameUpperCase()) > 0; j--) {
					DataType dt = data_types[j-1];
					data_types[j-1] = data_types[j];
					data_types[j] = dt;
				}
			}
	    return;
		}
		int middle = (start_index + end_index)/2;
		sortData_types(data_types, data_types_sorting, start_index, middle);
		sortData_types(data_types, data_types_sorting, middle, end_index);
		if (data_types_sorting[middle-1].getNameUpperCase().compareTo(data_types_sorting[middle].getNameUpperCase()) <= 0) {
			System.arraycopy(data_types_sorting, start_index, data_types, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle && 
					data_types_sorting[m].getNameUpperCase().compareTo(data_types_sorting[n].getNameUpperCase()) <= 0) {
				data_types[i] = data_types_sorting[m++];
			} else {
				data_types[i] = data_types_sorting[n++];
			}
		}
	}


	int findEntityExtentIndex(CEntityDefinition edef) throws SdaiException {
		// binary search in entities; e.g. if (entities[i] == edef)
//		int index = find_entity(0, noOfEntityDataTypes - 1, edef.getNameUpperCase());
		int index = find_entity(0, noOfEntityDataTypes - 1, edef);
if (SdaiSession.debug2) System.out.println(" index = " + index  +  "   for: " + edef.getNameUpperCase());
if (SdaiSession.debug2) System.out.println("  entities[index] ident = " + entities[index].instance_identifier + 
"  owning model: " + entities[index].owning_model.name);
		if (index < 0 || entities[index] != edef) {
//System.out.println("SchemaData !!!!! index = " + index + "   edef: " + edef.getName(null) +
//"   schData model: " + model.name);
//for (int i = 0; i < noOfEntityDataTypes; i++) {
//CEntityDefinition edd = entities[i];
//System.out.println("SchemaData ****** entity : " + edd.getCorrectName() + 
//"  entity ident: " + ((CEntity)edd).instance_identifier +
//"  its model: " + edd.owning_model.name);
//}
			return -1;
		} else {
			return index;
		}
	}

	int findEntityExtentIndex(Class provided_class) throws SdaiException {
		String full_name = provided_class.getName();
		String class_name =
			full_name.substring(full_name.lastIndexOf(".") + 2, full_name.length()).toUpperCase();
		int index = find_entity(0, noOfEntityDataTypes - 1, class_name);
		if (index >= 0) {
			if (getEntityClassByIndex(index) != provided_class && getEntityInterfaceByIndex(index) != provided_class) {
				index = -2;
			}
		}
		return index;
	}

	CEntity_definition findEntity(Class provided_class) throws SdaiException {
		int index = findEntityExtentIndex(provided_class);
		if (index < 0) {
			return null;
		} else {
			return (CEntity_definition)entities[index];
		}
	}


	int find_entity(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = sNames[middle].compareTo(key);
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


	int find_entity(int left, int right, CEntityDefinition def) throws SdaiException {
		String key = def.getNameUpperCase();
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = sNames[middle].compareTo(key);
			if (comp_res < 0) {
				left = middle + 1;
			} else if (comp_res > 0) {
				right = middle - 1;
			} else {
				if (def.instance_identifier == entities[middle].instance_identifier) {
					return middle;
				} else {
					return -1;
				}
			}
		}
		return -1;
	}


	int find_type(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = dtNames[middle].compareTo(key);
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


//	void check_if_typeOf(Object type, Value val, SdaiContext context) throws SdaiException {
/*	void check_if_typeOf(DataType type, Value val, SdaiContext context) throws SdaiException {
		if (defTypesCount < 0) {
			initializeDefinedTypes();
		}
		for (int i = 0; i < defTypesCount; i++) {
			DataType underlying_type = (DataType)def_types[i].getDomain(null);
			if (underlying_type.express_type < DataType.SELECT || underlying_type.express_type > DataType.ENT_EXT_EXT_SELECT) {
				continue;
			}
			ESelect_type sel_tp = (ESelect_type)underlying_type;
			AEntity sels = (AEntity)sel_tp.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			if (sels.myLength <= 0) {
				return;
			}
			if (sels.myLength == 1) {
				if (sels.myData == type) {
					val.if_new_then_add(def_types[i], -1);
					val.if_new_then_add(sel_tp, -1); // maybe, comment this line?
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int j = 0; j < sels.myLength; j++) {
					if (myDataA[j] == type) {
						val.if_new_then_add(def_types[i], -1);
						val.if_new_then_add(sel_tp, -1); // maybe, comment this line?
						break;
					}
				}
			}
		}
	}*/


	void check_entity_if_typeOf(CEntityDefinition type, Value val, SdaiContext context) throws SdaiException {
		if (type.typeOf_schema != this) {
//if (type.typeOf_schema != null)
//System.out.println("SchemaData !!!! CHANGE(entity)   model: " + model.name +
//"   entity: " + ((CEntity_definition)type).getName(null));
			collect_typeOF_types_for_entity(type, context);
		}
		for (int i = 0; i < type.typeOf_types.length; i++) {
			val.if_new_then_add(type.typeOf_types[i], -1);
			ESelect_type sel_tp = (ESelect_type)type.typeOf_types[i].getDomain(null);
			val.if_new_then_add(sel_tp, -1); // maybe, comment this line?
		}
	}


	void check_def_type_if_typeOf(DefinedType type, Value val, SdaiContext context) throws SdaiException {
		if (type.typeOf_schema != this) {
//if (type.typeOf_schema != null)
//System.out.println("SchemaData !!!! CHANGE(defined type)   model: " + model.name +
//"   defined type: " + ((CDefined_type)type).getName(null));
			collect_typeOF_types_for_def_type(type, context);
		}
		for (int i = 0; i < type.typeOf_types.length; i++) {
			val.if_new_then_add(type.typeOf_types[i], -1);
			ESelect_type sel_tp = (ESelect_type)type.typeOf_types[i].getDomain(null);
			val.if_new_then_add(sel_tp, -1); // maybe, comment this line?
		}
	}


	void collect_typeOF_types_for_entity(CEntityDefinition type, SdaiContext context) throws SdaiException {
		if (defTypesCount < 0) {
			initializeDefinedTypes();
		}
		int i, j;
		int count = 0;
		DataType underlying_type;
		AEntity sels;
		for (i = 0; i < defTypesCount; i++) {
			underlying_type = (DataType)def_types[i].getDomain(null);
			if (underlying_type.express_type < DataType.SELECT || underlying_type.express_type > DataType.ENT_EXT_EXT_SELECT) {
				continue;
			}
			sels = (AEntity)((ESelect_type)underlying_type).getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			if (sels.myLength <= 0) {
//				return;
				continue;
			}
			if (sels.myLength == 1) {
				if (sels.myData == type) {
					count++;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (j = 0; j < sels.myLength; j++) {
					if (myDataA[j] == type) {
						count++;
						break;
					}
				}
			}
		}
		type.typeOf_types = new CDefined_type[count];
		type.typeOf_schema = this;
		count = 0;
		for (i = 0; i < defTypesCount; i++) {
			underlying_type = (DataType)def_types[i].getDomain(null);
			if (underlying_type.express_type < DataType.SELECT || underlying_type.express_type > DataType.ENT_EXT_EXT_SELECT) {
				continue;
			}
			sels = (AEntity)((ESelect_type)underlying_type).getSelections(null, context);
			if (sels.myLength <= 0) {
//				return;
				continue;
			}
			if (sels.myLength == 1) {
				if (sels.myData == type) {
					type.typeOf_types[count] = def_types[i];
					count++;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (j = 0; j < sels.myLength; j++) {
					if (myDataA[j] == type) {
						type.typeOf_types[count] = def_types[i];
						count++;
						break;
					}
				}
			}
		}
	}


	void collect_typeOF_types_for_def_type(DefinedType type, SdaiContext context) throws SdaiException {
		if (defTypesCount < 0) {
			initializeDefinedTypes();
		}
		int i, j;
		int count = 0;
		DataType underlying_type;
		AEntity sels;
		for (i = 0; i < defTypesCount; i++) {
			underlying_type = (DataType)def_types[i].getDomain(null);
			if (underlying_type.express_type < DataType.SELECT || underlying_type.express_type > DataType.ENT_EXT_EXT_SELECT) {
				continue;
			}
			sels = (AEntity)((ESelect_type)underlying_type).getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			if (sels.myLength <= 0) {
//				return;
				continue;
			}
			if (sels.myLength == 1) {
				if (sels.myData == type) {
					count++;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (j = 0; j < sels.myLength; j++) {
					if (myDataA[j] == type) {
						count++;
						break;
					}
				}
			}
		}
		type.typeOf_types = new CDefined_type[count];
		type.typeOf_schema = this;
		count = 0;
		for (i = 0; i < defTypesCount; i++) {
			underlying_type = (DataType)def_types[i].getDomain(null);
			if (underlying_type.express_type < DataType.SELECT || underlying_type.express_type > DataType.ENT_EXT_EXT_SELECT) {
				continue;
			}
			sels = (AEntity)((ESelect_type)underlying_type).getSelections(null, context);
			if (sels.myLength <= 0) {
//				return;
				continue;
			}
			if (sels.myLength == 1) {
				if (sels.myData == type) {
					type.typeOf_types[count] = def_types[i];
					count++;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (j = 0; j < sels.myLength; j++) {
					if (myDataA[j] == type) {
						type.typeOf_types[count] = def_types[i];
						count++;
						break;
					}
				}
			}
		}
	}


	void take_aliases_of_entity_defs() throws SdaiException {
		if (e_aliases != null) {
			return;
		}
		int i;
		int index;
		String nm, alias;
		e_aliases = new String[noOfEntityDataTypes];
		if (model == SdaiSession.baseComplexModel) {
			return;
		}
		CEntity [] entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_REFERENCED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_REFERENCED_DECL]; i++) {
			CEntity_declaration$referenced_declaration dec_ref = 
				(CEntity_declaration$referenced_declaration)entity_instances[i];
			if (dec_ref.testAlias_name(null)) {
				alias = dec_ref.getAlias_name(null); 
				nm = ((CEntity_definition)dec_ref.getDefinition(null)).getNameUpperCase();
				index = find_entity(0, sNames.length - 1, nm);
				if (index >= 0) {
					e_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
		entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_USED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_USED_DECL]; i++) {
			CEntity_declaration$used_declaration dec_used = 
				(CEntity_declaration$used_declaration)entity_instances[i];
			if (dec_used.testAlias_name(null)) {
				alias = dec_used.getAlias_name(null); 
				nm = ((CEntity_definition)dec_used.getDefinition(null)).getNameUpperCase();
				index = find_entity(0, sNames.length - 1, nm);
				if (index >= 0) {
					e_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
		entity_instances = model.instances_sim[SdaiSession.ENTITY_DECL_IMPLICIT_DECL];
		for (i = 0; i < model.lengths[SdaiSession.ENTITY_DECL_IMPLICIT_DECL]; i++) {
			CEntity_declaration$implicit_declaration dec_imp = 
				(CEntity_declaration$implicit_declaration)entity_instances[i];
			if (dec_imp.testAlias_name(null)) {
				alias = dec_imp.getAlias_name(null); 
				nm = ((CEntity_definition)dec_imp.getDefinition(null)).getNameUpperCase();
				index = find_entity(0, sNames.length - 1, nm);
				if (index >= 0) {
					e_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
	}


	void take_aliases_of_defined_types() throws SdaiException {
		if (dt_aliases != null) {
			return;
		}
		int i;
		int index;
		String nm, alias;
		dt_aliases = new String[defTypesCount];
		CEntity [] d_types = model.instances_sim[SdaiSession.REFERENCED_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.REFERENCED_DECL_TYPE_DECL]; i++) {
			CReferenced_declaration$type_declaration dec_ref = 
				(CReferenced_declaration$type_declaration)d_types[i];
			if (dec_ref.testAlias_name(null)) {
				alias = dec_ref.getAlias_name(null); 
				nm = ((CDefined_type)dec_ref.getDefinition(null)).getNameUpperCase();
				index = find_type(0, defTypesCount - 1, nm);
				if (index >= 0) {
					dt_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
		d_types = model.instances_sim[SdaiSession.TYPE_DECL_USED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.TYPE_DECL_USED_DECL]; i++) {
			CType_declaration$used_declaration dec_used = 
				(CType_declaration$used_declaration)d_types[i];
			if (dec_used.testAlias_name(null)) {
				alias = dec_used.getAlias_name(null); 
				nm = ((CDefined_type)dec_used.getDefinition(null)).getNameUpperCase();
				index = find_type(0, defTypesCount - 1, nm);
				if (index >= 0) {
					dt_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
		d_types = model.instances_sim[SdaiSession.IMPLICIT_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.IMPLICIT_DECL_TYPE_DECL]; i++) {
			CImplicit_declaration$type_declaration dec_imp = 
				(CImplicit_declaration$type_declaration)d_types[i];
			if (dec_imp.testAlias_name(null)) {
				alias = dec_imp.getAlias_name(null); 
				nm = ((CDefined_type)dec_imp.getDefinition(null)).getNameUpperCase();
				index = find_type(0, defTypesCount - 1, nm);
				if (index >= 0) {
					dt_aliases[index] = alias.toUpperCase();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}
	}


	int getSubtypesComplex(int ind) throws SdaiException {
		int types_count = 0;
		StaticFields staticFields = null;
		CEntityDefinition sup = entities[ind];
		if (sup.partialEntityTypes == null) {
			staticFields = StaticFields.get();
			sup.prepareExternalMappingData(staticFields);
		}
		int n_defs = sup.noOfPartialEntityTypes;
//System.out.println("Subtypes of the entity = " + ((CEntity_definition)sup).getName(null) + "  sc: " + schema.getName(null)); 
		for (int i = 0; i < noOfEntityDataTypes; i++) {
			CEntityDefinition def = entities[i];
			if (def.complex != 2 || def == sup) {
				continue;
			}
			if (def.partialEntityTypes == null) {
				if (staticFields == null) {
					staticFields = StaticFields.get();
				}
//System.out.println("SchemaData !!!+++++++++++++++++++++ExternalMapping   def = " + ((CEntity_definition)def).getName(null)); 
				def.prepareExternalMappingData(staticFields);
			}
			int start_index = 0;
			int res_index;
			boolean found = true;
			for (int j = 0; j < n_defs; j++) {
				res_index = def.find_partial_entity(start_index, def.noOfPartialEntityTypes - 1,
					sup.partialEntityTypes[j].getCorrectName());
				if (res_index < 0) {
					found = false;
					break;
				}
				start_index = res_index + 1;
			}
			if (!found) {
				continue;
			}
			aux[types_count] = i;
			types_count++;
//System.out.println("   Subtype = " + ((CEntity_definition)def).getName(null));
		}
		return types_count;
	}


	int [] getAuxiliaryArray() {
		if (aux2 == null) {
			aux2 = new int[noOfEntityDataTypes];
		}
		return aux2;
	}


// accessed by entities[i].index
	int getBypassedRulesCount(int index, SdaiSession ss) throws SdaiException {
		if (ss.byp_rules_count < 0) {
			return 0;
		}
		String [][] byp_rules;
		if (ss.byp_rules_count == 0) {
			byp_rules = ss.getBlackList();
			if (ss.byp_rules_count < 0) {
				return 0;
			}
			getBypassedRulesAll(byp_rules, ss.byp_rules_count);
		}
		if (bypass_rules[index] == null) {
			return 0;
		}
		return bypass_rules[index].length;
	}


// It is assumed, for the sake of efficiency, that at least one bypassed rule for the 
// submitted entity index exists.
// Thus the method shall be invoked only when getBypassedRulesCount(...) 
// returns positive integer.
	String [] getBypassedRules(int index) throws SdaiException {
		return bypass_rules[index];
	}


	private void getBypassedRulesAll(String [][] byp_rules, int byp_rules_count) throws SdaiException {
		bypass_rules = new String[entities.length][];
		for (int i = 0; i < entities.length; i++) {
			String ent_name = sNames[i];
			int count = 0;
			for (int j = 0; j < byp_rules_count; j++) {
				if (byp_rules[0][j] == null) {
					if (!(byp_rules[1][j].toUpperCase().equals(ent_name))) {
						continue;
					}
					if (bypass_rules[i] == null) {
						bypass_rules[i] = new String[1];
					} else {
						enlarge_bypass_rules(i);
					}
					bypass_rules[i][count] = "";
					count++;
				} else {
					if (!(byp_rules[0][j].equals(ent_name))) {
						continue;
					}
					if (bypass_rules[i] == null) {
						bypass_rules[i] = new String[1];
					} else {
						enlarge_bypass_rules(i);
					}
					bypass_rules[i][count] = byp_rules[1][j];
					count++;
				}
			}
		}
	}


	private void enlarge_bypass_rules(int index) throws SdaiException {
		int new_length = bypass_rules[index].length + 1;
		String [] new_rules_file = new String[new_length];
		System.arraycopy(bypass_rules[index], 0, new_rules_file, 0, bypass_rules[index].length);
		bypass_rules[index] = new_rules_file;
	}


}
