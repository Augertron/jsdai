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
import java.io.*;

/**
 *
 * Created: Thu Nov 25 15:42:57 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class StaticFields {

	private static final ThreadLocal local = new ThreadLocal() {
		protected Object initialValue() {
			return new StaticFields();
		}
	};

	static StaticFields get() {
		return (StaticFields)local.get();
	}

	private StaticFields() {
		// CEntityDefinition
		names = new String[CEntityDefinition.NUMBER_OF_PARTIAL_TYPES];
		part_types = new CEntity_definition[CEntityDefinition.NUMBER_OF_PARTIAL_TYPES];
		// Create_instance
		short_names = false;
		// SdaiRepository
		string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_STRING];
		// PhFileReader
		current_instance_identifier = -1;
	}

// SdaiSession
/**
	Used in the public method convertRepoName in SdaiSession class.
*/
	byte [] repo_name;

// SdaiRepository
/**
	An auxiliary array used to store the names of schemas underlying for
	SdaiModels within this repository. The names are sorted alphabetically.
	The array is used for saving/loading data to/from the 'repository' binary file.
*/
	String [] schema_names;
/**
	An auxiliary array used to sort elements of the array 'schema_names'.
*/
	String [] schema_names_sorting;


// SchemaInstance
	CEntity [] renumb_insts;
	long [] saved_ids;
	SdaiModel [] other_repo_mods;
	SdaiModel [] dup_names_mods;
	SdaiModel [] ren_models;
	String [] old_names;


//  SdaiModel
/**
	An auxiliary array used to sort instances of an entity data type.
*/
	CEntity [] for_instances_sorting;
/**
	An auxiliary array used in method copyInstances to store sorted instances.
	In this method also the above defined array 'for_instances_sorting' is employed.
*/
	CEntity [] sorted_instances;

/**
	An auxiliary variable used to store the number of instances in an array 
	submitted to the method copyInstances.
*/
	int sorted_instances_count;

/**
	An array to which sorted instances of all entity data types are stored.
	This array is prepared by the method sortAllInstances which is invoked 
	when saving model's data to a binary file.
*/
	CEntity [] instances_all;
/**
	An auxiliary array used (together with 'instances_all') to sort instances 
	of all entity data types.
*/
	CEntity [] instances_all_aux;
/**
	An auxiliary array used to store and afterwards process given entity 
	definitions in getInstances methods having parameter an array of some type
	(either EEntity_definition or Class).
*/
	CEntity_definition [] defs;
/**
	An auxiliary array used for sorting definitions in array 'defs'.
*/
	CEntity_definition [] aux_defs;
	long [] ids;


// CEntity
/**
	An auxiliary array used to prepare String representation of this
	entity instance (see method <code>toString</code>).
*/
	byte [] ent_instance_as_string;
/**
	Description of the values of the entity instance. This field is
	used to exchange the data between JSDAI lang package and JSDAI
	library <code>getAll</code> and <code>setAll</code> methods.
*/
	ComplexEntityValue entity_values;
/**
	Description of the values of the entity instance. This field is
	needed in <code>compareValuesBoolean</code> method to take values,
	using <code>getAll</code>, of one of the two instances compared
	(for the values of another one the field 'entity_values' is employed).
*/
	ComplexEntityValue entity_values2;
	Class [] paramValue;
	Object[] argsValue;
	Object[] args;
	Class [] param;
	Object [] arg;
	Class [] paramw;
	Object [] argw;
	Class [] paramwa;
	Object [] argwa;
// 	boolean isConnector;
	EAttribute [] roles;
	EDefined_type[] df_types;
	Value [] attr_values;
	CDefined_type [] defined_types;
	boolean [] marks;
	CWhere_rule [] wrules;
	SdaiIterator it_refs;
	ASdaiModel _domain;
	CSchema_definition context_schema;
	CEntity inst_under_valid;


// InverseEntity
	SdaiIterator it;
	SdaiIterator it2;
	SdaiIterator it3;


// CEntityDefinition
	SdaiIterator iter_sup;
	String [] names;
	int [] indices;
	CEntity_definition [] part_types;
	Class [] param_ed;
	CDefined_type [] aux_arr_tp;
	EAttribute [] aux_arr_at;
	int [] aux_arr_br;
	CWhere_rule [] w_rules_sorting;


// EntityValue
/**
	The attribute whose value is currently prepared to be returned by a 
	'get' method of this class. This field is used when printing a warning 
	message to log file.
*/
	CExplicit_attribute current_attribute;
	byte[] ent_value_as_string;

// ComplexEntityValue
	byte[] compl_value_as_string;

// Value
// 	Value val_rec;
/**
	An array used in entity value comparison operation. 
*/
	CEntity [] plist1;
/**
	An array used in entity value comparison operation. 
*/
	CEntity [] plist2;
/**
	The count of elements in <code>plist1</code> and <code>plist2</code>. 
*/
	int plist_count;
/**
	An array used in methods implementing Format function of Express language. 
*/
	char [] preceding_zeros;
/**
	An array used in methods implementing Format function of Express language. 
*/
	char [] preceding_spaces;
/**
	The field used in methods implementing Format function of Express language. 
*/
	int power;
/**
	The field used in methods implementing Format function of Express language. 
	Its value is TRUE iff the considered small number after rounding becomes 
   equal to zero.
*/
	boolean number_vanishes;
/**
	An internal array introduced to (temporary) store the names of simple entity types 
	within a constructed entity data type. This array is used when creating an 
   instance of this constructed type.
*/
	PartialEntityName [] entity_name_components;
/**
	The name of an instance of a constructed entity data type in the form 
	of the byte array. If the instance is complex, then to concatenate the names 
	of simple entity types the symbol '$" is used.
*/
	byte [] constr_entity_name;
/**
	An object of the class <code>Create_instance</code> used to create 
	entity instances wrapped in this object of Value using EntityValue mechanism. 
	Instances are created within makeInstance() method.
*/
	Create_instance create;
/**
	An array of defined data types used while processing a value of select 
	data type. 
*/
	CDefined_type [] route;
/**
	The count of elements in <code>route</code>.
*/
	int def_count;
	byte[] value_as_string;
	byte[] value_as_string_complex;


// CAggregate
/**
	An auxiliary array used to prepare String representation of this 
	aggregate (see method <code>toString</code>).
*/
	byte[] instance_as_string;
	CDefined_type [] sel_array;
	SdaiIterator agg_it1;
	Value aggr_value;


// SessionAggregate
	SdaiIterator it_sa;


// Create_instance
/**
	Has value <code>true</code> if acronyms are used for the names of instances 
	in the exchange structure, and <code>false</code> if the usual names are used. 
*/
	boolean short_names;
/**
	The name of the complex entity type consisting of simple entity data types.
*/
	String [] complex_name;
	CEntityDefinition [] missing_defs;
	CEntityDefinition [] sep_defs;
	int [] sep_def_inds;
/**
	For each simple entity data type within a specified complex entity type the 
	corresponding element of <code>ref2partial_values</code> points (by index) 
	to the same simple entity data type in the array <code>complex_name</code>. 
	If a simple entity data type is not found in <code>complex_name</code>, 
	then in the corresponding position of <code>ref2partial_values</code> the 
	number -1 is stored.
*/
	int [] ref2partial_values;


// EntityExtent
	byte[] instance_as_string_extent;


// PhFileReader
/**
	The identifier of the instance that is currently under processing in the 
	exchange structure. This number is used when writing warning and error 
	messages to log file.
*/
	long current_instance_identifier;
/**
	Has value 'true' during importing of an exchange structure. This field 
	informs some methods where they are invoked.
*/
	boolean importing;

	CEntity c_instance;


// Print_instance
	byte [] string;
	int string_length;


// SchemaData
	CEntityDefinition [] for_entities_sorting;
	CDefined_type [] def_types_sorting;
	DataType [] data_types_sorting;
	SdaiIterator it_sch_dt;


// SelectType
	ESelect_type [] aux_for_ext_sel;


// EnumerationType
	EEnumeration_type [] aux_for_ext_enum;
	SdaiIterator it_aggr;
	SdaiIterator it_el;
	SdaiIterator it_checking;


// GlobalRule
	SdaiIterator iter_gr;
	CWhere_rule [] w_rules_sorting_gr;


// Binary
	byte [] operand;
	byte [] result;
	byte [] bit_array;
	byte [] bit_array2;


} // StaticFields
