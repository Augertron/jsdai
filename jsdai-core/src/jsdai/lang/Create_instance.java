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

class Create_instance {

/**
	An instance of either class <code>PhFileReader</code> or class <code>Value</code> 
	to which this class is attached. 
*/
	private Object owner;
//	private PhFileReader myReader;

	private static final int MISSING_DEFS_SIZE = 8;



/**
	The constructor of this class. Used in <code>PhFileReader</code>.
*/
	Create_instance(Object provided_owner) {
		owner = provided_owner;
	}



/**
	Tries to create an instance of the entity whose name is specified by the 
	first two parameters. If such an entity does not exist in schema 
	represented by the model given by the third parameter, then the most similar 
	available entity is taken and its instance is created. The parameter 
	<code>model_app</code> gives the model into which the new instance 
	shall be stored. The parameter <code>items_count</code> means the number 
	of simple entity data types in the complex entity which instance is 
	required to be created.
*/
	CEntity object(byte[] key, int key_length, SdaiModel model, 
			SdaiModel model_app, long instance_identifier, int items_count, int mark)
			throws SdaiException,
			java.lang.IllegalAccessException,
			java.lang.InstantiationException {

		SchemaData schemaData = model.schemaData;
		int result;
		int true_index;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.short_names) {
			result = find_class_short(0, schemaData.noOfShortNames - 1, key, key_length, model);
			if (result >= 0) {
				result = schemaData.fromShort[result];
			} else {
				result = find_class(0, schemaData.noOfEntityDataTypes - 1, key, key_length, model);
				if (result >= 0) {
					result = schemaData.fromLong[result];
					staticFields.short_names = false;
				}
			}
		} else {
			result = find_class(0, schemaData.noOfEntityDataTypes - 1, key, key_length, model);
			if (result >= 0) {
				result = schemaData.fromLong[result];
			} else {
				result = find_class_short(0, schemaData.noOfShortNames - 1, key, key_length, model);
				if (result >= 0) {
					result = schemaData.fromShort[result];
					staticFields.short_names = true;
				}
			}
if (SdaiSession.debug2) {String ss = new String(key, 0, key_length);
System.out.println("   FINDING: " + ss + "   index= " + result +
" entity: " + schemaData.entities[result].getName(null) + 
"   ind = " + schemaData.entities[result].index + "    " + schemaData.entities[result]);
for (int i = 0; i < schemaData.noOfEntityDataTypes; i++) {
System.out.println(" index = " + i + "    *****NORMAL NAME: " + schemaData.sNames[i] + 
"   LONG NAME: " + schemaData.sLongNames[i]);
}System.out.println();}
		}
		if (result < 0) {
			if (mark < -1) {
				return null;
			}
			boolean try_to_convert = false;
			boolean failure = false;
			if (mark > 1) {
				byte[] new_key = ((PhFileReader)owner).remedy_complex_name(items_count);
				if (new_key == null) {
					try_to_convert = true;
				} else {
					CEntity entity_returned = object(new_key, key_length, model, model_app, 
						instance_identifier, items_count, -1);
					if (entity_returned != null) {
						EntityValue.printWarningToLogo(model_app.repository.session, AdditionalMessages.RD_WODE, instance_identifier);
					}
					return entity_returned;
				}
			} else if (mark == -1) {
				try_to_convert = true;
			}
			if (try_to_convert) {
				CEntity inst;
				int new_index = convertToSimpleEntity(staticFields, model, items_count);
				if (new_index < 0) {
					inst = recoverMissingPartialEntities(staticFields, model, model_app, instance_identifier, items_count);
					if (inst != null) {
						EntityValue.printWarningToLogo(model_app.repository.session, 
							AdditionalMessages.RD_MIPE, instance_identifier);
						return inst;
					}
					failure = true;
				} else {
					true_index = schemaData.entities[new_index].index;
					schemaData = schemaData.toInterfaced[new_index];
					Class class_found = schemaData.getEntityClassByIndex(true_index);
					inst = schemaData.super_inst.makeInstance(class_found, model_app, new_index, 0);
					if(model_app.getSubMode() == SdaiModel.MODE_SUBMODE_PARTIAL
						&& (model_app.sim_status[new_index] & SdaiModel.SIM_LOADED_MASK) ==
						SdaiModel.SIM_LOADED_NONE) {

						model_app.sim_status[new_index] =
							(short)((model_app.sim_status[new_index] & ~SdaiModel.SIM_LOADED_MASK) |
									SdaiModel.SIM_LOADED_PARTIAL);
					}
					return inst;
				}
			}
			if (failure) {
				result = takeBestFit(staticFields, model, items_count, instance_identifier, model_app.repository.session);
				PhFileReader rd = (PhFileReader)owner;
				if (result >= 0) {
					int ind_long = schemaData.toLong[result];
					String cor_ent_name;
					if (rd.inst_is_required) {
						cor_ent_name = new String(rd.req_instance_name, 0, rd.req_instance_name_length);
					} else {
						cor_ent_name = null;
					}
					AdditionalMessages.printWarningToLogo(model_app.repository.session, AdditionalMessages.RD_CONF, 
						instance_identifier, new String(key, 0, key_length), cor_ent_name, 
						new String(schemaData.bLongNames[ind_long]));
					rd.inst_is_required = false;
				} else if (result == -2) {
					rd.inst_is_required = false;
					String message = AdditionalMessages.RD_UNET;
					String entity_type_name;
					boolean first = true;
					PartialEntityName [] complex_name_byte = rd.get_complex_name();
					for (int i = 0; i < items_count; i++) {
						if (staticFields.complex_name[i] == null) {
							entity_type_name = 
								new String(complex_name_byte[i].entity_name, 0, complex_name_byte[i].length_of_entity_name);
							if (first) {
								message += entity_type_name;
								first = false;
							} else {
								message += (", " + entity_type_name);
							}
						}
					}
					message += ".";
					AdditionalMessages.printWarningToLogo(model_app.repository.session, message, instance_identifier, 
						new String(key, 0, key_length));
					return null;
				} else {
					rd.inst_is_required = false;
					AdditionalMessages.printWarningToLogo(model_app.repository.session, AdditionalMessages.RD_CONF, 
						instance_identifier, new String(key, 0, key_length));
					return null;
				}
			} else {
//System.out.println("*****MODEL: " + model.name/* + "    SCHEMA: " + schema.getName(null)*/);
//for (int i = 0; i < schemaData.noOfEntityDataTypes; i++) {
//System.out.println("    *****NORMAL NAME: " + schemaData.sNames[i] + 
//"   LONG NAME: " + schemaData.sLongNames[i]);
//}System.out.println();
				AdditionalMessages.printWarningToLogo(model_app.repository.session, AdditionalMessages.RD_ENNF, 
					instance_identifier, new String(key, 0, key_length));
				return null;
			}
		}
		true_index = schemaData.entities[result].index;
		schemaData = schemaData.toInterfaced[result];
//String entity_name111 = new String(key, 0, key_length);
//System.out.println("  Create_instance   entity wanted: " + entity_name111);
		Class class_found = schemaData.getEntityClassByIndex(true_index);
if (SdaiSession.debug2) System.out.println("  CLASS FOUND: " + class_found.getName());
		CEntity inst = schemaData.super_inst.makeInstance(class_found, model_app, result, 0);
		if(model_app.getSubMode() == SdaiModel.MODE_SUBMODE_PARTIAL
		   && (model_app.sim_status[result] & SdaiModel.SIM_LOADED_MASK) ==
		   SdaiModel.SIM_LOADED_NONE) {

			model_app.sim_status[result] =
				(short)((model_app.sim_status[result] & ~SdaiModel.SIM_LOADED_MASK) |
						SdaiModel.SIM_LOADED_PARTIAL);
		}
		return inst;
	}


	private CEntity recoverMissingPartialEntities(StaticFields staticFields, SdaiModel model, SdaiModel model_app, long instance_identifier, 
			int items_count) throws SdaiException,
			java.lang.IllegalAccessException,
			java.lang.InstantiationException {
		int i,j,m;
		CEntityDefinition def;
		PartialEntityName [] complex_name;
		PhFileReader rd;
		if (owner instanceof PhFileReader) {
			rd = (PhFileReader)owner;
			complex_name = rd.get_complex_name();
		} else {
			return null;
		}
		SchemaData schemaData = model.schemaData;
//if (rd.saved_ent_name[2] == rd.saved_ent_name[3]) 
//System.out.println("Create_instance ...................aaaaa  VIOLA on instance: #" + instance_identifier);
		for (m = 0; m < items_count; m++) {
			int result;
			if (staticFields.short_names) {
				result = find_class_short(0, schemaData.noOfShortNames - 1, 
					complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
				if (result >= 0) {
					result = schemaData.fromShort[result];
				} else {
					result = find_class(0, schemaData.noOfEntityDataTypes - 1, 
						complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
					if (result >= 0) {
						result = schemaData.fromLong[result];
						staticFields.short_names = false;
					}
				}
			} else {
				result = find_class(0, schemaData.noOfEntityDataTypes - 1, 
					complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
				if (result >= 0) {
					result = schemaData.fromLong[result];
				} else {
					result = find_class_short(0, schemaData.noOfShortNames - 1, 
						complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
					if (result >= 0) {
						result = schemaData.fromShort[result];
						staticFields.short_names = true;
					}
				}
			}
			if (result < 0) {
				return null;
			}
			def = schemaData.entities[result];
//String stri = new String(complex_name[m].entity_name, 0, complex_name[m].length_of_entity_name);
//System.out.println("Create_instance +++++++ m : " + m + "   stri: " + stri + 
//"   result: " + result + "   def: " + def);
			if (def.partialEntityTypes == null) {
				def.prepareExternalMappingData(staticFields);
			}
			complex_name[m].def = def;
			complex_name[m].index = result;
		}
//if (rd.saved_ent_name[2] == rd.saved_ent_name[3]) 
//System.out.println("Create_instance ...................bbbbb  VIOLA on instance: #" + instance_identifier);
		int miss_count = 0;
		boolean found;
		for (m = 0; m < items_count; m++) {
			def = complex_name[m].def;
			for (i = 0; i < def.noOfPartialEntityTypes; i++) {
				CEntityDefinition partial_def = def.partialEntityTypes[i];
				if (!findPartialEntity(complex_name, items_count, partial_def)) {
					found = false;
					for (j = 0; j < miss_count; j++) {
						if (staticFields.missing_defs[j] == partial_def) {
							found = true;
							break;
						}
					}
					if (found) {
						continue;
					}
					if (staticFields.missing_defs == null) {
						staticFields.missing_defs = new CEntityDefinition[MISSING_DEFS_SIZE];
					} else if (miss_count >= staticFields.missing_defs.length) {
						int new_length = staticFields.missing_defs.length * 2;
						CEntityDefinition [] new_array = new CEntityDefinition[new_length];
						System.arraycopy(staticFields.missing_defs, 0, new_array, 0, staticFields.missing_defs.length);
						staticFields.missing_defs = new_array;
					}
					staticFields.missing_defs[miss_count++] = partial_def;
				}
			}
		}
		if (miss_count > 0) {
			if (staticFields.sep_defs == null) {
				if (miss_count <= MISSING_DEFS_SIZE) {
					staticFields.sep_defs = new CEntityDefinition[MISSING_DEFS_SIZE];
					staticFields.sep_def_inds = new int[MISSING_DEFS_SIZE];
				} else {
					staticFields.sep_defs = new CEntityDefinition[miss_count];
					staticFields.sep_def_inds = new int[miss_count];
				}
			} else if (miss_count > staticFields.sep_defs.length) {
				staticFields.sep_defs = new CEntityDefinition[miss_count];
				staticFields.sep_def_inds = new int[miss_count];
			}
			int sep_count = 0;
			for (i = 0; i < miss_count; i++) {
				CEntityDefinition miss_def = staticFields.missing_defs[i];
				int index = 0;
				CEntityDefinition guardian = null;
				for (j = 0; j < items_count; j++) {
					if (identifyAsPartialEntity(complex_name[j].def, miss_def)) {
						if (guardian == null) {
							guardian = complex_name[j].def;
							index = j;
						} else {
							if (identifyAsPartialEntity(guardian, complex_name[j].def)) {
								guardian = complex_name[j].def;
								index = j;
							} else if (identifyAsPartialEntity(complex_name[j].def, guardian)) {
							} else {
//System.out.println("Create_instance   DOUBLE INCLUSION  def1: " + ((CEntity_definition)guardian).getName(null) + 
//"    def2: " + ((CEntity_definition)complex_name[j].def).getName(null));
								return null;
							}
						}
					}
				}
				found = false;
				for (j = 0; j < sep_count; j++) {
					if (staticFields.sep_defs[j] == guardian) {
						found = true;
						break;
					}
				}
				if (!found) {
					if (checkSupertypes(complex_name, items_count, guardian, miss_def)) {
						return null;
					}
					staticFields.sep_defs[sep_count] = guardian;
					staticFields.sep_def_inds[sep_count++] = index;
				}
			}

			byte [] new_name = rd.extendComplexName(model_app, items_count, staticFields.sep_defs, staticFields.sep_def_inds, 
				sep_count, instance_identifier);
			if (new_name == null) {
				rd.restore_entity_values(items_count);
				return null;
			}
//String str = new String(new_name, 0, rd.enl_instance_name_length);
//System.out.println("Create_instance   new_name: " + str);
			CEntity entity_returned = object(new_name, rd.enl_instance_name_length, model, model_app, 
				instance_identifier, items_count, -2);
			if (entity_returned == null) {
				rd.restoreComplexName(items_count);
				rd.restore_entity_values(items_count);
				return null;
			}
			rd.setNameLength();
			return entity_returned;
		}
		return null;
	}

	boolean findPartialEntity(PartialEntityName [] complex_name, int items_count, CEntityDefinition partial_def)
			throws SdaiException {
		for (int i = 0; i < items_count; i++) {
			if (partial_def == complex_name[i].def) {
				return true;
			}
		}
		return false;
	}
	boolean identifyAsPartialEntity(CEntityDefinition def, CEntityDefinition partial_def)
			throws SdaiException {
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			if (partial_def == def.partialEntityTypes[i]) {
				return true;
			}
		}
		return false;
	}
	boolean checkSupertypes(PartialEntityName [] complex_name, int items_count, 
			CEntityDefinition def, CEntityDefinition partial_def) throws SdaiException {
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			if (def == def.partialEntityTypes[i]) {
				continue;
			}
			if (findPartialEntity(complex_name, items_count, def.partialEntityTypes[i]) && 
					identifyAsPartialEntity(def.partialEntityTypes[i], partial_def)) {
				return true;
			}
		}
		return false;
	}


/**
	Tries to recognize the noncomplex entity (available in the schema 
	represented by the model given by the first parameter) whose instance, 
	encoded in the external mapping mode, is found in the exchange structure. 
	In the case of success, the index of such an entity in the array of all 
	entities available in the specified schema is returned. In the case of 
	failure the return value is -1. This always happens for complex instances. 
	The parameter <code>items_count</code> means the number of simple entity 
	data types found in the external mapping.  
*/
	private int convertToSimpleEntity(StaticFields staticFields, SdaiModel model, int items_count)
			throws SdaiException {
		PartialEntityName [] complex_name;
		if (owner instanceof PhFileReader) {
			complex_name = ((PhFileReader)owner).get_complex_name();
		} else {
			complex_name = ((Value)owner).get_complex_name(staticFields);
		}
		SchemaData schemaData = model.schemaData;
		int count_of_partial_def = -1;
		int index_selected = -1;
		for (int m = 0; m < items_count; m++) {
			int result;
			if (staticFields.short_names) {
				result = find_class_short(0, schemaData.noOfShortNames - 1, 
					complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
				if (result >= 0) {
					result = schemaData.fromShort[result];
				} else {
					result = find_class(0, schemaData.noOfEntityDataTypes - 1, 
						complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
					if (result >= 0) {
						result = schemaData.fromLong[result];
						staticFields.short_names = false;
					}
				}
			} else {
				result = find_class(0, schemaData.noOfEntityDataTypes - 1, 
					complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
				if (result >= 0) {
					result = schemaData.fromLong[result];
				} else {
					result = find_class_short(0, schemaData.noOfShortNames - 1, 
						complex_name[m].entity_name, complex_name[m].length_of_entity_name, model);
					if (result >= 0) {
						result = schemaData.fromShort[result];
						staticFields.short_names = true;
					}
				}
			}
			if (result < 0) {
//String str = new String(complex_name[m].entity_name, 0, complex_name[m].length_of_entity_name);
//System.out.println("Create_instance  111111 entity not found: " + str);
				return -1;
			}
			CEntityDefinition def = schemaData.entities[result];
			if (def.partialEntityTypes == null) {
				def.prepareExternalMappingData(staticFields);
			}
			complex_name[m].def = def;
			complex_name[m].index = result;
			complex_name[m].mark = false;
//System.out.println("Create_instance  ^^^^ entity: " + def.getName(null) +
//"   noOfPartialEntityTypes = " + def.noOfPartialEntityTypes + 
//"  owning_model: " + def.owning_model.name);
			if (def.noOfPartialEntityTypes > count_of_partial_def) {
				count_of_partial_def = def.noOfPartialEntityTypes;
				index_selected = m;
			}
		}
		if (count_of_partial_def != items_count) {
//System.out.println("Create_instance  222222 different sizes: count_of_partial_def = " + 
//count_of_partial_def + "   items_count = " + items_count);
			return -1;
		}
		CEntityDefinition def_selected = complex_name[index_selected].def;
		for (int i = 0; i < count_of_partial_def; i++) {
			boolean found = false;
			for (int m = 0; m < items_count; m++) {
				if (!complex_name[m].mark && 
						def_selected.partialEntityTypes[i] == complex_name[m].def) {
					found = true;
					complex_name[m].mark = true;
					break;
				}
			}
			if (!found) {
//System.out.println("Create_instance 333333 not found definition: " +
//def_selected.partialEntityTypes[i].getName(null));
				return -1;
			}
		}
//System.out.println("Create_instance entity found: " + def_selected.getName(null));
		return complex_name[index_selected].index;
	}


/**
	Selects an entity (from the schema represented by the model given by the 
	first parameter) which is most close to the non-existing in the schema 
	entity whose names of the contained simple entity data types were found 
	in the encoding (in the external mapping mode) of the instance. The index 
	of the selected entity in the array of all entities available in the 
	specified schema is returned.
*/
	private int takeBestFit(StaticFields staticFields, SdaiModel model, int items_count, long instance_identifier, SdaiSession session) throws SdaiException {
		int i, j;
		int res_index;
		int init_value = -items_count;
		SchemaData schemaData = model.schemaData;
		PartialEntityName [] complex_name_byte = ((PhFileReader)owner).get_complex_name();
		if (staticFields.complex_name == null) {
			if (items_count <= PhFileReader.NAMES_ARRAY_SIZE) {
				staticFields.complex_name = new String[PhFileReader.NAMES_ARRAY_SIZE];
			} else {
				staticFields.complex_name = new String[items_count];
			}
		} else if (items_count > staticFields.complex_name.length) {
			enlarge_complex_name(staticFields, items_count);
		}
		for (i = 0; i < items_count; i++) {
//String sss = new String(complex_name_byte[i].entity_name, 0,
//complex_name_byte[i].length_of_entity_name);
//System.out.println("Create_instance   sss: " + sss);
			if (staticFields.short_names) {
				res_index = find_class_short(0, schemaData.noOfEntityDataTypes - 1, 
					complex_name_byte[i].entity_name, complex_name_byte[i].length_of_entity_name, model);
				if (res_index >= 0) {
					res_index = schemaData.fromShort[res_index];
				}
			} else {
				res_index = find_class(0, schemaData.noOfEntityDataTypes - 1, complex_name_byte[i].entity_name,
					complex_name_byte[i].length_of_entity_name, model);
				if (res_index >= 0) {
					res_index = schemaData.fromLong[res_index];
				}
			}
			if (res_index < 0) {
				AdditionalMessages.printWarningToLogo(session, AdditionalMessages.RD_ENNF, instance_identifier, 
					new String(complex_name_byte[i].entity_name, 0, complex_name_byte[i].length_of_entity_name));
				staticFields.complex_name[i] = null;
			} else {
				staticFields.complex_name[i] = schemaData.sNames[res_index];
				CEntityDefinition req_def = schemaData.entities[res_index];
				if (req_def.noOfPartialAttributes < 0) {
					req_def.setAttributeInformation(staticFields, model);
				}
				init_value -= schemaData.entities[res_index].noOfPartialAttributes;
			}
		}
		int best_value = -10000;
		int best_type = -1;
		CEntityDefinition def;
		for (i = 0; i < schemaData.noOfEntityDataTypes; i++) {
			def = schemaData.entities[i];
			int value = init_value;
//			if (def.noOfPartialEntityTypes <= 0) {
			if (def.noOfPartialAttributes < 0) {
				def.setAttributeInformation(staticFields, model);
			}
			for (j = 0; j < def.noOfPartialEntityTypes; j++) {
				value -= (def.partialEntityTypes[j].noOfPartialAttributes + 1);
			}
			for (j = 0; j < items_count; j++) {
				if (staticFields.complex_name[j] == null) {
					return -2;
				}
				res_index = def.find_partial_entity_upper_case(0, def.noOfPartialEntityTypes - 1, 
					staticFields.complex_name[j]);
				if (res_index >= 0) {
					value += (2*(1+def.partialEntityTypes[res_index].noOfPartialAttributes));
				}
			}
			if (value > best_value) {
				best_value = value;
				best_type = i;
			} else if (value == best_value) {
				if (def.noOfPartialEntityTypes < schemaData.entities[best_type].noOfPartialEntityTypes) {
					best_type = i;
				}
			}
		}
		if (best_type < 0) {
			return best_type;
		}
		def = schemaData.entities[best_type];
		if (staticFields.ref2partial_values == null) {
			if (def.noOfPartialEntityTypes <= PhFileReader.NAMES_ARRAY_SIZE) {
				staticFields.ref2partial_values = new int[PhFileReader.NAMES_ARRAY_SIZE];
			} else {
				staticFields.ref2partial_values = new int[def.noOfPartialEntityTypes];
			}
		} else if (def.noOfPartialEntityTypes > staticFields.ref2partial_values.length) {
			enlarge_ref2partial_values(staticFields, def.noOfPartialEntityTypes);
		}
		for (j = 0; j < def.noOfPartialEntityTypes; j++) {
			staticFields.ref2partial_values[j] = -1;
		}
		for (j = 0; j < items_count; j++) {
			if (staticFields.complex_name[j] == null) {
				continue;
			}
			res_index = def.find_partial_entity_upper_case(0, def.noOfPartialEntityTypes - 1, 
				staticFields.complex_name[j]);
			if (res_index >= 0) {
				staticFields.ref2partial_values[res_index] = j;
			}
		}
		((PhFileReader)owner).update_complex_entity_values(def, staticFields.ref2partial_values);
		return best_type;
	}


/**
	Returns the index of the specified entity in the array of all entities 
	available in the schema represented by the (data dictionary) model submitted 
	through the last parameter. The name of the entity is given by parameters 
	<code>key</code> and <code>key_length</code>. The array of entities is 
	sorted alphabetically, so the method performs the binary search. If the 
	required entity is not found, then -1 is returned.
*/
	private int find_class(int left, int right, byte[] key, int key_length, SdaiModel model) {
		int i;
		int key_larger, key_longer;
		int length;
		SchemaData schemaData = model.schemaData;
		while (left <= right) {
			int middle = (left + right)/2;
			byte[] name = schemaData.bLongNames[middle];
			if (key_length > name.length) {
				key_longer = 1;
				length = name.length;
			} else if (key_length < name.length) {
				key_longer = -1;
				length = key_length;
			} else {
				key_longer = 0;
				length = key_length;
			}
			for (i = 0, key_larger = 0; i < length; i++) {
				if (key[i] > name[i]) {
					key_larger = 1;
					break;
				} else if (key[i] < name[i]) {
					key_larger = -1;
					break;
				}
			}
			if (key_larger == 0) {
				key_larger = key_longer;
			}
			if (key_larger > 0) {
				left = middle + 1;
			} else if (key_larger < 0) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


/**
	Returns the index of the specified entity in the array of all entities 
	available in the schema represented by the (data dictionary) model submitted 
	through the last parameter. The entity is specified by the acronym 
	(parameters <code>key</code> and <code>key_length</code>). The array of 
	entities is sorted alphabetically according to acronyms, so the method 
	performs the binary search. If the required entity is not found, then -1 
	is returned. 
*/
	private int find_class_short(int left, int right, byte[] key, int key_length, SdaiModel model) {
		int i;
		int key_larger, key_longer;
		int length;
		SchemaData schemaData = model.schemaData;
		while (left <= right) {
			int middle = (left + right)/2;
			byte[] name = model.schemaData.bShortNames[middle];
			if (name == null) {
				return -1;
			}
			if (key_length > name.length) {
				key_longer = 1;
				length = name.length;
			} else if (key_length < name.length) {
				key_longer = -1;
				length = key_length;
			} else {
				key_longer = 0;
				length = key_length;
			}
			for (i = 0, key_larger = 0; i < length; i++) {
				if (key[i] > name[i]) {
					key_larger = 1;
					break;
				} else if (key[i] < name[i]) {
					key_larger = -1;
					break;
				}
			}
			if (key_larger == 0) {
				key_larger = key_longer;
			}
			if (key_larger > 0) {
				left = middle + 1;
			} else if (key_larger < 0) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	CEntity create_inst_expr(byte[] key, int key_length, SdaiModel model, 
			SdaiModel model_app, long instance_identifier, int items_count, Value val)
			throws SdaiException, java.lang.IllegalAccessException, java.lang.InstantiationException {

		SchemaData schemaData = model.schemaData;
		int result;
		int true_index;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.short_names) {
			result = find_class_short(0, schemaData.noOfShortNames - 1, key, key_length, model);
			if (result >= 0) {
				result = schemaData.fromShort[result];
			} else {
				result = find_class(0, schemaData.noOfEntityDataTypes - 1, key, key_length, model);
				if (result >= 0) {
					result = schemaData.fromLong[result];
					staticFields.short_names = false;
				}
			}
		} else {
			result = find_class(0, schemaData.noOfEntityDataTypes - 1, key, key_length, model);
			if (result >= 0) {
				result = schemaData.fromLong[result];
			} else {
				result = find_class_short(0, schemaData.noOfShortNames - 1, key, key_length, model);
				if (result >= 0) {
					result = schemaData.fromShort[result];
					staticFields.short_names = true;
				}
			}
		}

		if (result < 0) {
			result = convertToSimpleEntity(staticFields, model, items_count);
			if (result < 0) {
				AdditionalMessages.printWarningToLogo(model_app.repository.session, AdditionalMessages.RD_CONF, 
					instance_identifier, new String(key, 0, key_length));
				return null;
			}
		}
		val.d_type = schemaData.entities[result];
		true_index = schemaData.entities[result].index;
		schemaData = schemaData.toInterfaced[result];
		Class class_found = schemaData.getEntityClassByIndex(true_index);
		CEntity inst = schemaData.super_inst.makeInstance(class_found, model_app, result, 0);
		if(model_app.getSubMode() == SdaiModel.MODE_SUBMODE_PARTIAL
		   && (model_app.sim_status[result] & SdaiModel.SIM_LOADED_MASK) ==
		   SdaiModel.SIM_LOADED_NONE) {

			model_app.sim_status[result] =
				(short)((model_app.sim_status[result] & ~SdaiModel.SIM_LOADED_MASK) |
						SdaiModel.SIM_LOADED_PARTIAL);
		}
		return inst;
	}


/**
	Increases the size of the auxiliary array 'complex_name' either twice 
	or to satisfy the required demand, whichever of these two values is larger. 
*/
	private static void enlarge_complex_name(StaticFields staticFields, int demand) {
		int new_length = staticFields.complex_name.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.complex_name = new String[new_length];
	}


/**
	Increases the size of the auxiliary array 'ref2partial_values' either twice 
	or to satisfy the required demand, whichever of these two values is larger. 
*/
	private static void enlarge_ref2partial_values(StaticFields staticFields, int demand) {
		int new_length = staticFields.ref2partial_values.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.ref2partial_values  = new int[new_length];
	}

}
