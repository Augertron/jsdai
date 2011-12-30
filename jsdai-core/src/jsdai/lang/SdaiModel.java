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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.query.SdaiRepositoryRef;
import jsdai.query.SerializableRef;
import jsdai.xml.InstanceReader;
import jsdai.xml.SdaiInputSource;

/**
 * <code>SdaiModel</code> is a Java class used for grouping entity instances.
 * Each entity instance within <code>SdaiSession</code> must belong to
 * some <code>SdaiModel</code>.
 * Each <code>SdaiModel</code> is based upon one EXPRESS schema called underlying schema.
 * The EXPRESS types of all entities whose instances can appear in an
 * <code>SdaiModel</code> shall be defined or declared in the EXPRESS schema
 * underlying for this <code>SdaiModel</code>.

 * <p>
 * Conceptionally, the <code>SdaiModel</code> can be represented by
 * the following EXPRESS entity:

 * <P><TT><pre>ENTITY SdaiModel;
 *  {@link #getName name} : String;
 *  {@link #getUnderlyingSchema underlyingSchema} : <A HREF="../../jsdai/dictionary
/ESchema_definition.html">ESchema_definition</A>;
 *  {@link #getDefinedSchema describedSchema} : OPTIONAL <A HREF="../../jsdai/dictionary
/ESchema_definition.html">ESchema_definition</A>;
 *  {@link #getRepository repository} : <A HREF="../../jsdai/lang
/SdaiRepository.html">SdaiRepository</A>;
 *  {@link #getChangeDate changeDate} : OPTIONAL String;
 *  {@link #getMode mode} : OPTIONAL int;
 *  {@link #getInstances instances} : <A HREF="../../jsdai/lang
/AEntity.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/EEntity.html">EEntity</A>;
 *  {@link #getFolders folders} : <A HREF="../../jsdai/lang
/AEntityExtent.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/EntityExtent.html">EntityExtent</A>;
 *  {@link #getPopulatedFolders populatedFolders} : <A HREF="../../jsdai/lang
/AEntityExtent.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/EntityExtent.html">EntityExtent</A>;
 *  {@link #getDefaultLanguage defaultLanguage} : OPTIONAL String;
 *  {@link #getContextIdentifiers contextIdentifiers}     : <A HREF="../../jsdai
/lang/A_string.html">LIST</A> [0:?] OF String;
 *INVERSE
 *  {@link #getAssociatedWith associatedWith}  : <A HREF="../../jsdai/lang
/ASchemaInstance.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang
/SchemaInstance.html">SchemaInstance</A> FOR {@link SchemaInstance#getAssociatedModels associatedModels};
 *UNIQUE
 *  UR1: name, repository;
 *WHERE
 *  WR1: SELF IN {@link SdaiRepository#getModels SELF.repository.models};
 *END_ENTITY;</TT></pre>
 * <P>

 * The attributes <code>defaultLanguage</code> and <code>contextIdentifiers</code>
 * are read-write, whereas all other attributes are read-only.
 * For each attribute a corresponding get method (or methods sometimes)
 * is defined. The <code>changeDate</code> can be retrieved either as <code>String</code> or
 * as <code>long</code>. Similarly, <code>underlyingSchema</code> can be retrieved
 * either as <code>ESchema_definition</code> or as <code>String</code>.
 * The attribute <code>defaultLanguage</code> also has the corresponding set method.

 * <p>
 * The entity instances together with their grouping into <code>EntityExtent</code>s
 * comprise the contents of <code>SdaiModel</code>.
 * To make the instances within the contents of an <code>SdaiModel</code> available,
 * the <code>SdaiModel</code> needs to be started in either read-only or read-write
 * access mode.
 * Upon ending read-only or read-write access all entity instances belonging to
 * this <code>SdaiModel</code> become not available.
 * If some entity instance within a <code>SdaiModel</code> has been
 * created, deleted or modified since the most recent
 * commit, abort or start transaction read-write access
 * operation was performed, then before ending read-write access of this
 * <code>SdaiModel</code> either commit or abort operation has to be
 * applied once again.
 * <p>
 * Each <code>SdaiModel</code> belongs to exactly one <code>SdaiRepository</code>.
 * The <code>SdaiRepository</code> needs to be opened to
 * access the <code>SdaiModel</code>. Upon closing the <code>SdaiRepository</code>
 * all <code>SdaiModel</code>s contained in it become not available.
 * <code>SdaiModel</code>s containing dictionary and mapping information
 * belong to special <code>SdaiRepository</code> named "SystemRepository".
 * For these <code>SdaiModel</code>s read-write access mode is forbidden.
 * <p>
 * A <code>SdaiRepository</code> may contain one or more virtual models.
 * Virtual model is an instance of <code>SdaiModel</code> with only 'name' and
 * 'repository' fields set with values. A virtual model has no underlying schema
 * attached to it. Such models are created automatically when unresolved
 * references are detected while starting read-only or read-write access of
 * normal (non-virtual) <code>SdaiModel</code>s. When needed, virtual models
 * are (automatically) promoted to normal ones.
 *
 * @see "ISO 10303-22::8.4.2 sdai_model and 8.4.3 sdai_model_contents"
 */


/*
//---------------  Format of binary model contents files:

File for model-contents:
For the examples assume a schema with the entities "a", "b" and "c" where
"a" is the supertype for "b" and "c".
Instances
#11=A(...);
#22=B(......);
#55=(A(...)B(...)C(...));

model_file :=
	'D'        // model-start
	INTEGER 	  // build number
	LONG		  // count of instances in this model
	{ LONG }   // (sorted in increasing order) identifiers of instances of this model, ONLY FOR LOCAL, not REMOTE (Not true at least since beginning of 2003 V.N.)

	'S'
	SHORT      // no of defined_types which do not have an immediate
              // underlying select_data_type.
	{ STRING } // names of the defined_data_types

	'S'
	SHORT      // no of single entity_data_types
	{ single_entity_type_definition } // sorted alphabetically (upper case)

	'S'
	SHORT      // no of populated complex entity_data_types, even those with only one entity_data_type
	{ complex_entity_instances } // sorted lexicographically (Gintaras !)
	{ entity_instance_values };  // sorted by instance_identifier
-------- This part became obsolete, see below
	[ 'L'
	  arm_2_aim_link
    ]
-------- End of section
	'E'      // model-end

single_entity_type_definition := // used to allocate memory for instances_sim arrays (in SdaiModel)
	STRING   // from dictionary: entity_definition.name, e.g. "a", "b" or "c"
	SHORT    // no of all non-redeclaring explicit attributes
				// of this entity_data_type, but not of it's supertypes
 	         // the order is as defined in express. It can be used as index 0, 1, 2...
	{ STRING }; // attribute names, see see explicit_attribute.name
	SHORT     // number of supertpyes
	{ SHORT }; // index of supertypes

complex_entity_instances := // used to allocate memory for instances_sim arrays (in SdaiModel)
	SHORT       // count of leave entity_data_types
	{ SHORT }   // indexes/ref to all leave entity_data_types,
            	// sorted alphabetically (upper case)
	LONG 			// no_of_instances, > 0  , was INTEGER till version xxxx
	{ LONG } ;    // instance ids, ordered


entity_instance_values :=
	"c"        // start-indication
	SHORT      // index/ref to complex entity type of this instance
//	LONG       // instance_identifier; e.g. 11 for instance "#11", no longer needed since it comes before
	{ value }; // values for all attributes in single_entity_data_typs
                               // sorted in given order above (composing)
    [{aim_2_arm_link}]  // AIM to ARM links

value :=  // value of a single attribute
	( '$' // Missing - unset
	| '*' // Redefined
	| 'u' // UNKNOWN
	| 't' // TRUE
	| 'f' // FALSE
	| 'r' DOUBLE       // Real value
	| 'i' INTEGER      // Integer value
	| 's' STRING       // String value
	| 'b' STRING       // Binary value
	| 'e' STRING       // Enumeration value definition
	| 'p' SHORT value // index to typed parameters needed for vales of
                     // select_data_types, recursive definition
	| ( '1' // instance-ref inside this model
		 SHORT   // index of populated_entity_type
		 INTEGER    // instance index
	  )
	| ( '2' // instance-ref inside this repository with model-name
		 STRING // model-name
		 entity_type // description of the entity type
		 LONG   // instance identifier
	  )
	| ( '3' // instance-ref inside this repository with model-index
		 SHORT  // model-index
		 entity_type // description of the entity type
		 LONG    // instance identifier
	  )
	| ( '4' // instance-ref with repository-name and model-name
		 STRING // repository-name
		 STRING // model-name
		 entity_type // description of the entity type
		 LONG   // instance identifier
	  )
	| ( '5' // instance-ref with repository-index and model-name
		 SHORT  // repository-index
		 STRING // model-name
		 entity_type // description of the entity type
		 LONG  // instance identifier
	  )
	| ( '6' // instance-ref with repository-index and model-index
		 SHORT    // repository-index
		 SHORT    // model-index
		 entity_type // description of the entity type
		 LONG        // instance identifier
	  )
   | '(' { value } ')' // for all aggregates, starting with first member or valid index position
-------- Proposal for ARM type information storing in AIM model
   | ( 'a'
       STRING // ARM schema name
	   STRING // ARM complex entity type name
       // This info is converted into link (specific temp value) to dictionary data
       // (entity_defintion) when the model is read
       // It can be handled by database too by having additional table on the DB which links
       // instance table with complex entity table (two columns)
     )
   | ( 'b'
       SHORT  // ARM schema index (flat)
	   STRING // ARM complex entity type name
     )
   | 'c' SHORT // ARM complex entity type index (flat for all ARM schemas)
-------- End of proposal
	) ;

entity_type :=
	(
	  ( '1' // types from the current data dictionary are used
       SHORT // index to populated_entity_types
	  )
	| ( '2' // type is not found in the current data dictionary
       STRING // entity-name
	  )
	| ( '3' // type is not found in the current data dictionary
		 SHORT // entity-index
	  )
	)

-------- AIM2ARM link information
aim_2_arm_link :=
   ( 'l'
       STRING // ARM repository name (if "" then current repository)
       STRING // ARM model name
	   LONG   // ARM instance identifier
     )
   | ( 'k'
       SHORT  // ARM model index (flat)
	   LONG   // ARM instance identifier
   )
--------

-------- This section became obsolete and will no longer be created (2003-06-12)
arm_2_aim_link :=
    INTEGER     //Model count
    { STRING    //Model name
	  INTEGER   //Link count
      { LONG    //ARM instance PL
	    LONG    //AIM instance PL
	  }
	}
--------
*/


public abstract class SdaiModel extends SdaiCommon
implements SdaiEventSource, QuerySource, SdaiModelConnector {

/**
 * When {@link #getMode() getMode} returns this value, the contents of this <code>SdaiModel</code>
 * is not accessible.
 */
	public static final int NO_ACCESS = 0;

/**
 * When {@link #getMode() getMode} returns this value, the contents of this <code>SdaiModel</code>
 * is available for read-only access.
 */
	public static final int READ_ONLY = 1;

/**
 * When {@link #getMode() getMode} returns this value, the contents of this <code>SdaiModel</code>
 * is available for read-write access.
 */
	public static final int READ_WRITE = 2;

	static final boolean CHECK_ID = false;


    protected static final int MODE_MODE_MASK = 0x000F;
    protected static final int MODE_SUBMODE_MASK = 0x00F0;

	protected static final int MODE_SUBMODE_PARTIAL = 0x0010;

/**
	The initial length of an array to store instances of one entity data type,
	or more specifically, the initial length of the row in the matrix 'instances_sim'
	used to store all the instances of this SdaiModel.
*/
	static final int INSTANCE_ARRAY_INITIAL_SIZE = 8;

/**
	The number by which the length of the row in the matrix 'instances_sim'
	is increased (when space in the row is insufficient to accomodate a new
	instance of the specified entity data type).
*/
	static final int INSTANCE_ARRAY_SIZE_STEP = 128;

/**
	The initial length of the internal arrays 'for_instances_sorting' and 'sorted_instances'
	used for sorting of the instances (for each row of 'instances_sim',
	and also inside public method 'copyInstances').
*/
	static final int ARRAY_FOR_INSTANCES_SORTING_SIZE = 1024;

/**
	The initial length of the internal array 'type_names' used to store the
	names of the defined types.
*/
	private static final int TYPES_ARRAY_SIZE = 128;

/**
	The initial length of the internal array 'simple_entity_names' used to store the
	names of simple entity data types.
*/
	private static final int ENTITIES_ARRAY_SIZE = 1024;

/**
	The initial length of the internal array 'to_leaves'.
*/
	private static final int LEAF_ARRAY_SIZE = 24;

/**
	The initial length of the internal arrays 'ex_models' and 'ex_model_names'.
*/
	static final int NUMBER_OF_EXTERNAL_MODS = 4;

/**
	The initial length of the internal array 'ex_repositories'.
*/
	static final int NUMBER_OF_EXTERNAL_REPS = 4;

/**
	The initial length of the internal array 'ex_edefs'.
*/
	static final int NUMBER_OF_EXTERNAL_EDEFS = 16;

/**
	The initial length of the internal arrays 'defs' and 'aux_defs'.
*/
	private static final int DEFS_ARRAY_SIZE = 16;

/**
	The initial length of the internal arrays 'instances_all' and 'instances_all_aux'.
*/
	private static final int INSTANCES_ALL_INIT_SIZE = 50000;

/**
	The initial length of the internal array 'current_lengths'.
*/
	private static final int INIT_COUNT_FOR_ENTITIES = 1024;

	private static final int ATTRS_ARRAY_SIZE = 16;

	private static final int INCOMPATIBLES_SIZE = 8;

	private static final int PART_DEFS_SIZE = 32;

	private static final int COMPLEX_INCOMPATIBLES_SIZE = 16;

	private static final int REFS_ARRAY_SIZE = 16;

	private static final String SUFIX_XIM = "_XIM";

/**
	EXPRESS attributes of SdaiModel, see ISO 10303-22::8.4.2 sdai_model.
*/
	String name;
	CSchema_definition underlying_schema;
	SdaiRepository repository;
	long change_date;		/* OPTIONAL */
	protected int mode;     /* OPTIONAL */
	protected ASchemaInstance associated_with;

/**
	EXPRESS Attributes from SdaiModelContents, see ISO 10303-22::8.4.3 sdai_model_contents.
*/
	AEntity instances;    /*    simulated			*/
	AEntityExtent folders;
	AEntityExtent populated_folders;

/**
	If this SdaiModel is a data dictionary one, then definition of the
	EXPRESS schema described by it.
	Otherwise the value of the field is null.
*/
	CSchema_definition described_schema;

/**
	The default language for string values in entity instances of this SdaiModel.
*/
	String language;

/**
	An aggregate containing information describing the contexts
	within which the instances of this SdaiModel are applicable.
*/
	A_string context_identifiers;

/**
	An array being assigned to lines of 'instances_sim' which
	correspond to entity data types with no instantiation in this SdaiModel.
*/
//	private static Object [] emptyArray = new Object[0];
	protected static final CEntity [] emptyArray = new CEntity[0];


/**
	An array consisting of identifiers of all instances contained in this
	model. The array is prepared by reading the beginning of the model's
   binary file. This can happen in the following methods:
   openRepository, endReadOnlyAccess, endReadWriteAccess.
   The identifiers in the array are sorted in increasing order.
*/
	long [] inst_idents;


	private static final int INST_PORTION_SIZE = 32768;
	SimIdx[] instances_sim_start;
	SimIdxEnd[] instances_sim_end;
	int instances_sim_start_len;

// Array to store deleted instances identifiers
	private static final long[] EMPTY_DEL_INST_IDS = new long[0];
	long [] del_inst_ids;
	int n_del_inst_ids;

// Threshold to use recreation of all instances in DB
    long del_inst_threshold;

/**
	The internal array used to store the names of the defined types
	encountered in the definitions of populated entity data types within
	the schema which is underlying for this SdaiModel.
	Only the names of those defined types are included into this array which
	do not have an immediate underlying select data type.
	These names are saved to and loaded from the binary file containing
	the data of the model.
*/
    private String [] type_names;

/**
	The internal array used to store the names of simple entity data types
	encountered in the definitions of populated entity data types within
	the schema which is underlying for this SdaiModel.
	These names are saved to and loaded from the binary file containing
	the data of the model.
*/
    private String [] simple_entity_names;

/**
	Indices(references) to all leaf entity data types sorted alphabetically (upper case).
	The elements of this array after its construction are stored to
	binary file containing the data of the model.
*/
    private int [] to_leaves;

	private String [] attr_names;

	private int [] attr_counts_file;

	private int [] incompatibles;

	private int [] value2attr [];

	private int inc_count;

	private int [] complex_incompat;

	private int [] type2type [];

	private int [] dummy_attrs [];

	private int complex_inc_count;

	private Value dummy_val;

	private int [] supertypes_from_file [];

	private int [] part_defs_from_file;

	private int [] not_existing_part_defs_from_file;

/**
	Array(matrix) of instances of entities encountered in the schema
	which is underlying for this SdaiModel.
	Each row of this array contains instances of some specific entity data type.
	If the population represented by the model does not include instances
	of this type, then the corresponding row is empty (that is, 'emptyArray'
	defined above is assigned to it).
	The rows are ordered alphabetically according to the names of the entities
	these rows represent. Furthermore, instances inside each row usually are
	stored in the increasing order of their identifiers.
	The order is not kept during reading a Part-21 file in which
	instances are not sorted.
*/
//	Object [] instances_sim [];
	CEntity [] instances_sim [];

/**
	An array each element of which gives the count of instances of some
	specific entity data type. Entities for which these counts are provided
	appear in the same (alphabetical) order as in 'instances_sim'.
*/
	int lengths [];

	CEntityDefinition entities [];

/**
	An array each element of which is a flag showing whether instances
	in the corresponding row of 'instances_sim' are sorted (value 'true')
	or not (value 'false').
	I suspect that it is possible to remove this array but it is
	not easy to verify and be absolutely sure that it is indeed a case.
*/
	//boolean sorted [];
    protected static final short SIM_SORTED = 1;
    protected static final short SIM_LOADED_MASK = 6;
    protected static final short SIM_LOADED_COMPLETE = 0;
    protected static final short SIM_LOADED_PARTIAL = 2;
    protected static final short SIM_LOADED_NONE = 4;
    protected static final short SIM_PROCESS_MASK = 24;
    protected static final short SIM_DELETE = 8;
    protected static final short SIM_KEEP = 16;

    protected short sim_status [];

/**
	Indices(references) either:
	1) from all entity data types encountered in the underlying schema
	   to populated entity data types (in this case the array is used to prepare
	   information for writing down to the binary file for this SdaiModel);
	2) from populated entity data types to all entity data types encountered
	   in the underlying schema (the array is used to prepare information
	   when reading the binary file for this SdaiModel);
*/
	int to_entities [];

/**
	An array each element of which gives the current (increasing) count of
	instances of some specific entity data type during reading entity instances
   from the binary file. The order of entity types for which these counts are
	managed is the same as for array 'lengths', that is, the order is 'standard'.
*/
    private int [] current_lengths;

/**
	An auxiliary array used to detect all defined types
	encountered in the definitions of populated entity data types within
	the schema which is underlying for this SdaiModel.
*/
	private CDefined_type [] def_type_chain;

/**
	The length of the array 'def_type_chain' declared above.
*/
	private int def_type_count;

/**
	Data dictionary model describing EXPRESS schema that is underlying
	for this SdaiModel.
*/
	SdaiModel dictionary;

/**
	The name of the schema this SdaiModel is based on. This name is used
	when reading-writing part-21 and binary files.
*/
	String schema_name;

	String original_name = null;

/**
	A positive number (identifier) representing this SdaiModel. All models within a
	repository have unique identifiers. The numbering starts from 1.
*/
	int identifier;

	int mode_before_deletion;

/**
	The number of instances contained in this model. It serves as a count of
   elements in the array inst_idents.
*/
	long all_inst_count;

/**
	An auxiliary variable used to store the number of populated entity data types.
	Needed for method sortAllInstances.
*/
	int pop_ent_count;

/**
	The identifying number of the build using which the binary file containing
	the data of this SdaiModel was created. This number is settled after starting
	(read-only or read-write) access of the model.
*/
	private int underlying_build;

/**
	Has value 'true' if and only if something in the contents of the model
	is modified (created, deleted, value assigned or added, value unset, and so on).
	The value is switched to 'false' during a commit operation.
*/
	boolean modified;

	boolean modified_sleeping;

/**
	Has value 'true' in the case when this SdaiModel was imported from an exchange structure
	(part 21 file). The value is switched to 'false' during first commit operation.
*/
	boolean modified_by_import;

/**
	Has value 'true' if and only if something outside of the contents of the model
	is modified (for example, model renamed or added to/removed from the
	schema instance).
	The value is switched to 'false' during the commit operation.
*/
	boolean modified_outside_contents;

/**
	Has value 'true' if modifications outside of the contents of the model are
	because this SdaiModel was imported from the exchange structure (part 21 file).
	The value is switched to 'false' during first commit operation.
*/
	boolean modified_outside_contents_by_import;

/**
	Has value 'true' if the data of this SdaiModel were written to binary file
	(using commit operation in SdaiTransaction).
*/
	boolean committed;

/**
	Has value 'true' if either some instance of this model was deleted or some
   instance was lost by substituting it with another instance which goes to
   another repository. The value is set to 'false' in commit, abort,
   endReadOnlyAccess and endReadWriteAccess methods.
*/
	boolean inst_deleted;

/**
	Has value 'true' if either some instance of this model was deleted or some
   instance was lost by substituting it with another instance which goes to
   another repository and, in addition, this change was made persistent by
   executing commit operation. The value is set to 'false' in endReadOnlyAccess
   and endReadWriteAccess methods.
*/
	boolean inst_deleted_permanently;

/**
	Some auxiliary variable used in two contexts:
	1) before methods deleteContents and deleteSdaiModelWork to know the
	   status of the model (whether its access is ended or it is deleted completely);
	2) during loading a repository binary file.
*/
	boolean exists;

/**
	An auxiliary variable used in exportClearTextEncoding to register models
	whose access should be temporary started (and then immediately ended again).
*/
	boolean export;

	boolean bypass;
	boolean bypass_setAll;
	boolean closingAll;
	boolean refresh_in_abort;

/**
	Has value 'true' if and only if this SdaiModel already is included into
	array 'aInterfacedModel'.
*/
	boolean initialized;

/**
	Has value 'true' if and only if for this SdaiModel method linkEarlyBinding
	(from the class SchemaData) was already applied.
*/
	boolean early_binding_linked;
   boolean early_binding_linking;

/**
	Has value 'true' if and only if for this SdaiModel method linkEarlyBindingInit
	(from the class SchemaData) was already applied.
*/
	boolean early_binding_linked_init;

/**
	Has value 'true' if and only if access for this SdaiModel is started.
	The value 'true' is assigned and used in SdaiRepository method getSessionIdentifier.
*/
	boolean started;

	boolean deleting;

	boolean file_saved;

/**
	Has value 'true' if and only if the binary file for this SdaiModel does exist.
	The value is assigned in loadResource method and used in SdaiRepository method
	getSessionIdentifier.
*/
	boolean bin_file_missing;

	boolean substitute_operation;

/**
	The argument of getInstances and getExactInstances methods in the class
	ASdaiModel. It is used in aggregate methods isMember, getByIndexObject,
	getCurrentMemberObject.
*/
	Object extent_type;

/**
	The index/reference to the alphabetically ordered list of entity data types
	for the entity assigned to 'extent_type' declared above.
*/
	int extent_index;

	int ext_index;

	int e_type_ind;

	int subtype_ind;

	int undo_delete = 0;

/**
	The maximal number of instances of the same entity data type, that is,
	the largest element in array 'lengths'.
*/
	private int max_inst_count;

/**
	The array containing the names of models entity
	instances of which are referenced by entity instances within
	this SdaiModel. This information is prepared and used during
	saving the data to/loading from the binary file.
	This array cannot be eliminated and array 'ex_models' used instead it
	because frequently the names of models are encountered but models
	themselves are unknown (in such cases model names are provided by
	instances of the class Connector).
*/
	String [] ex_model_names;

/**
	The array containing the models entity instances of which are
	referenced by entity instances within this SdaiModel.
	This information is prepared and used during loading the data
	from the binary file.
*/
	SdaiModel [] ex_models;

/**
	The array consisting of the names of repositories containing models
	entity instances of which are referenced by entity instances within
	this SdaiModel. This information is prepared and used during
	saving the data to/loading from the binary file.
*/
	String [] ex_repositories;

/**
	The array containing definitions of the entities whose instances are
	referenced by entity instances within this SdaiModel.
	This information is prepared and used during saving the data to/loading from
	the binary file.
*/
	String [] ex_edefs;

/**
	The count of elements in arrays 'ex_models', 'ex_repositories' and
	'ex_edefs', respectively.
*/
	int n_ex_models, n_ex_repositories, n_ex_edefs;

/**
	The internal array used to store definitions of the simple entity data types
	encountered in the definitions of populated entity data types within
	the schema which is underlying for this SdaiModel.
	These data are prepared and used (in loading of the binary file) only for the
	baseDictionaryModel.
*/
	private CEntity_definition [] single_defs;

/**
	Used to store values of an entity instance.
	This field is applied in the following two contexts:
	  1) To save/load an instance when saving the data to/loading from the
	     binary file;
	  2) To process instances in substituteInstance methods.
*/
	ComplexEntityValue entity_values;

/**
	A pointer to double linked list of outgoing connectors.
*/
	private Connector outConnectors;

/**
	A pointer to double linked list of incoming connectors.
*/
	private Connector inConnectors;

/**
	An instance of the class SchemaData for this SdaiModel.
	This field is nonnull only for data dictionary models.
	It is set with value while executing startReadOnlyAccess method.
*/
	SchemaData schemaData;

	/**
	 * The aggregate containing instances of SdaiListener class.
	 */
	protected CAggregate listenrList;

/**
	Has value "true" only when this model was created after the
	last invocation of commit or abort operation, whichever of them
	appeared more recently.
*/
	boolean created;

	boolean optimized = false;

	Object domain;

	int queryMode;

	static final int QUERY_MODE_NO_ACCESS_ON = 0;

	static final int QUERY_MODE_NO_ACCESS_OFF = 1;

	private final int ONE_TYPE_INSTANCES_ARRAY_SIZE = 1024;


	/**
	 * This method returns null. Remote model has to override this method.
	 * @return null
	 */
	protected SdaiModelRemote getModRemote() {
		return null;
	}

	/**
	 * This mehod does nothing. Remote model has to override this method.
	 */
	protected void setModRemote(SdaiModelRemote mRemote) throws SdaiException {
	}

	final protected void setMode(int mode_to_set) {
		mode = (mode & ~MODE_MODE_MASK) | mode_to_set;
	}


    final protected void setSubMode(int submode_to_set) {
		mode = (mode & ~MODE_SUBMODE_MASK) | submode_to_set;
	}


/**
	Returns an owner of this SdaiModel.
*/
	SdaiCommon getOwner() {
		return repository;
	}


/**
	Sets field 'modified' with value 'true'.
*/
	void modified() throws SdaiException {
		modified = true;
	}


/**
	Used to create application (through the method createSdaiModel) and dictionary
	SdaiModels.
	Also using this constructor some special SdaiModel called sessionModel is created.
*/
	protected SdaiModel(String model_name, CSchema_definition schema,
						SdaiRepository rep) throws SdaiException {
		repository = rep;
		initLocal(model_name, schema);
	}

	protected void initLocal(String model_name, CSchema_definition schema) throws SdaiException {
		name = model_name;
		underlying_schema = schema;
		if (repository != SdaiSession.systemRepository) {
			dictionary = ((SchemaDefinition)schema).modelDictionary;
		}
		initializeSdaiModel();
	}

/**
	Used to create an SdaiModel when reading a repository binary file.
*/
	protected SdaiModel(String model_name, SdaiRepository rep, SdaiModel dict) throws SdaiException {
		repository = rep;
		initLocal(model_name, dict);
	}

	protected void initLocal(String model_name, SdaiModel dict) throws SdaiException {
		name = model_name;
		dictionary = dict;
		initializeSdaiModel();
	}

/**
	Used to create an SdaiModel when in the binary file being loaded the name of
	a model and its repository (or only its name) are found but this repository
	misses a model with this given name.
	Also, used to create virtual models.
*/
	protected SdaiModel(String model_name, SdaiRepository rep) {
		repository = rep;
		initLocal(model_name);
	}

	protected void initLocal(String model_name) {
		name = model_name;
		mode = NO_ACCESS;
		change_date = -1;
		initializeAssociatedWith();
	}

	// Noop constructor for subclass creation. If this constructor is used
	// the object has to be initialized by initLocal(...)
	protected SdaiModel(SdaiRepository rep) {
		repository = rep;
	}

/**
	Initializes the fields of this class.
	Invoked in constructors and promoteVirtualToOrdinary method.
*/
	void initializeSdaiModel() throws SdaiException {
		mode = NO_ACCESS;
		initializeAssociatedWith();
		instances = new AEntity();
		Object [] myDataA = new Object[2];
		myDataA[0] = this;
		instances.myData = myDataA;
		instances.myType = SdaiSession.setTypeForInstancesAll;
		populated_folders = new AEntityExtent(SdaiSession.setType0toN, this);
		change_date = System.currentTimeMillis();
		modified = false;
		modified_outside_contents = false;
		committed = false;
		file_saved = false;
		inst_deleted = false;
		inst_deleted_permanently = false;
		initialized = false;
		early_binding_linked = false;
		early_binding_linked_init = false;
		entity_values = new ComplexEntityValue();
		language = null;
		queryMode = QUERY_MODE_NO_ACCESS_ON;
	}

	protected void initializeAssociatedWith() {
		if (associated_with == null) {
			associated_with = new ASchemaInstance(SdaiSession.setType0toN, this);
		}
	}

	protected static ASchemaInstance newASchemaInstance(SdaiCommon owner) {
		return new ASchemaInstance(SdaiSession.setType0toN, owner);
	}

/**
	Initializes the contents of this SdaiModel: entity extents,
	array 'instances_sim' containing instances and other information.
*/
	protected abstract void initializeContents() throws SdaiException;

	void initializeDictionaryModelContents() throws SdaiException {
		int size;
		if (this == SdaiSession.baseDictionaryModel) {
			size = ((AEntity)SdaiSession.bootEntityAggregate).myLength;
		} else {
			SchemaData sch_data = underlying_schema.owning_model.schemaData;
			size = sch_data.noOfEntityDataTypes;
		}
		lengths = new int[size];
		//sorted = new boolean[size];
		sim_status = new short[size];
		instances_sim = new CEntity[size][];
		instances_sim_start = new SimIdx[size];
		instances_sim_end = new SimIdxEnd[size];
		for (int j = 0; j < size; j++) {
			instances_sim[j] = emptyArray;
            sim_status[j] |= SIM_SORTED;
            sim_status[j] = (short)((sim_status[j] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
		}
	}

	protected void initializeDataModelContents() throws SdaiException {
		if (dictionary.getMode() == NO_ACCESS) {
			dictionary.startReadOnlyAccess();
		}
		if (underlying_schema == null) {
			underlying_schema = dictionary.described_schema;
		}
        //<--VV--Instance state tracking--
		del_inst_ids = EMPTY_DEL_INST_IDS; //new long[INSTANCE_ARRAY_INITIAL_SIZE];
		n_del_inst_ids = 0;
		del_inst_threshold = -1;
        //--VV-->Instance state tracking--
	}

	protected void prepareInitialContens(int mode) throws SdaiException {
		if(mode == READ_ONLY) {
			entities = new CEntityDefinition[0];
			if (instances_sim == null || instances_sim.length != 0) {
				prepareInstancesSim(1, false);
				instances_sim[0] = emptyArray;
				lengths[0] = 0;
			}
		} else if(mode == READ_WRITE) {
			entities = null;
			prepareInstancesSim(dictionary.schemaData.noOfEntityDataTypes + 1, true);
		}
	}

	void prepareInstancesSim(int size, boolean initialize) throws SdaiException {
		lengths = new int[size];
		instances_sim = new CEntity[size][];
		instances_sim_start = new SimIdx[size];
		instances_sim_end = new SimIdxEnd[size];
		sim_status = new short[size];
		for (int j = 0; j < size; j++) {
			sim_status[j] |= SIM_SORTED;
			sim_status[j] = (short)((sim_status[j] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
		}
		if (initialize) {
			for (int j = 0; j < size; j++) {
				instances_sim[j] = emptyArray;
			}
		}
	}


	int getQueryMode() {
		return queryMode;
	}


	void setQueryMode(int query_mode) {
		queryMode = query_mode;
	}


/**
 * Returns the name of the model as a <code>String</code>.
 * <p> The models within a <code>SdaiRepository</code> must have unique names.
 * @return the name of this <code>SdaiModel</code>.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
*/
	public String getName() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (name == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return name;
	}


/**
 * Returns model identifier as a <code>String</code>.
 * <p> The models within a <code>SdaiRepository</code> have unique identifiers,
 * which are not reused when some models are deleted and some new created.
 * @return the identifier of this <code>SdaiModel</code>.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
*/
   public String getId() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		return "m" + identifier;
	}


/**
 * Returns definition of the EXPRESS schema on which the contents
 * of this SdaiModel is based on. This method is valid only for
 * models containing application data. When applied to models from
 * "System Repository", SdaiException VA_NSET will be thrown.
 * @return the underlying EXPRESS schema.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getUnderlyingSchemaString
*/
	public ESchema_definition getUnderlyingSchema() throws SdaiException {
// start read-only access to underlying schema if needed
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (underlying_schema == null) {
			if (repository == SdaiSession.systemRepository || dictionary == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (dictionary.getMode() == NO_ACCESS) {
				dictionary.startReadOnlyAccess();
			}
			underlying_schema = dictionary.described_schema;
			if (underlying_schema == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		return (ESchema_definition)underlying_schema;
//		} // syncObject
	}


/**
 * Returns the name of the EXPRESS schema on which the contents
 * of this SdaiModel is based on. This method is valid only for
 * models containing application data. When applied to models from
 * "System Repository", SdaiException VA_NSET will be thrown.
 * @return the name of the underlying EXPRESS schema.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getUnderlyingSchema
 * @see #getOriginalSchemaName
*/
	public String getUnderlyingSchemaString() throws SdaiException {
// shall work even if no access is started to underlying model/schema
// schema name is take from underlying model name
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (underlying_schema == null) {
			if (repository == SdaiSession.systemRepository || dictionary == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return dictionary.name.substring(0, dictionary.name.length() - SdaiSession.ENDING_FOR_DICT);
		}
		return underlying_schema.getName(null);
//		} // syncObject
	}


/**
 * Returns the name of the EXPRESS schema which was assigned to this
 * model in Part 21 file the repository containing this model was imported from.
 * If the model belongs to a repository that entered the session by applying
 * an operation different than import from a Part 21 file, then the method
 * returns the name of the schema on which the contents
 * of this SdaiModel is based on. This method is valid only for
 * models containing application data. When applied to models from
 * "System Repository", SdaiException VA_NSET will be thrown.
 * @return the name of the schema taken from Part 21 file.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getUnderlyingSchemaString
 * @see #setOriginalSchemaName
 * @since 4.0.0
*/
	public String getOriginalSchemaName() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (original_name != null) {
			return original_name;
		} else {
			return getUnderlyingSchemaString();
		}
	}


/**
 * Provides the name of an EXPRESS schema which will be used to relate this
 * model with that schema when exporting the repository containing this model
 * to a Part 21 file. This supplied name will appear in the Part 21 header
 * entity FILE_SCHEMA and, if repository contains more than one model,
 * additionally in DATA entity opening the data section for the model of interest.
 * This method is valid only for models containing application data.
 * When applied to models from "System Repository", SdaiException VA_NSET will be thrown.
 * @param name the name of an EXPRESS schema.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getOriginalSchemaName
 * @since 4.1.0
*/
	public void setOriginalSchemaName(String name) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		original_name = name;
		schema_name = name;
	}


/**
 * Returns the <code>SdaiRepository</code> to which this <code>SdaiModel</code> belongs.
 * @return the owning repository.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 */
	public SdaiRepository getRepository() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		return repository;
	}


/**
 * Checks if the creation date or date of the most recent
 * modification of this model was set.
 * @return <code>true</code> if the date was set, and
 * <code>false</code> otherwise.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @see #getChangeDate
 * @see #getChangeDateLong
 */
	public boolean testChangeDate() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (change_date < 0) {
			return false;
		} else {
			return true;
		}
	}


/**
 * Returns the date and time when this <code>SdaiModel</code> was
 * created or most recently modified.
 * @return a <code>String</code> representing the date and time.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #testChangeDate
 * @see #getChangeDateLong
 */
	public String getChangeDate() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (change_date > 0) {
			return repository.session.cal.longToTimeStamp(change_date);
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
//		} // syncObject
	}


/**
 * Returns a <code>long</code> value of the date and time when
 * this <code>SdaiModel</code> was created or most recently modified.
 * @return a <code>long</code> representing the date and time.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #testChangeDate
 * @see #getChangeDate
 */
	public long getChangeDateLong() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (change_date > 0) {
			return change_date;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Returns the current access mode for this <code>SdaiModel</code>.
 * The contents of an <code>SdaiModel</code> is a collection of
 * entity instances grouped into <code>EntityExtent</code>s.
 * For accessing this contents, 3 modes are defined:
 * <p>NO_ACCESS: contents cannot be accessed;
 * <p>READ_ONLY: only read operations on the contents are allowed;
 * <p>READ_WRITE: read-write operations are allowed.
 * @return the current access mode.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 */
	public int getMode() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		return (mode & MODE_MODE_MASK);
	}


    final protected int getSubMode() throws SdaiException {
		return (mode & MODE_SUBMODE_MASK);
	}


/**
 * Returns the set of schema instances with which this <code>SdaiModel</code>
 * is currently associated.
 * An <code>SdaiModel</code> may be associated with no, one or
 * several <code>SchemaInstance</code>s.
 * @return the set of schema instances, with which this model is associated.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public ASchemaInstance getAssociatedWith() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (associated_with == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return associated_with;
	}

    protected final ASchemaInstance getAssociatedWithFast() {
		return associated_with;
	}

    protected final void setAssociatedWith(ASchemaInstance schInstAgg) {
		associated_with = schInstAgg;
	}

    protected void addAssociatedWith(SchemaInstance schInst) throws SdaiException {
		associated_with.addUnorderedRO(schInst);
		modified_outside_contents = true;
	}

    protected void removeAssociatedWith(SchemaInstance schInst, boolean initialize) throws SdaiException {
//		associated_with.removeUnorderedRO(this);
		associated_with.removeUnorderedRO(schInst);
		modified_outside_contents = true;
	}

	/**
	 * Returns the name of the user that has placed exclusive lock
	 * on remote model. This method always returns
	 * <code>null</code> for local schema instance.
	 * @return the name of the user that has the exclusive lock or
	 *         null if there is no exclusive lock on the model
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see SdaiSession#lock
	 * @since 4.1.0
	 */
	public String getLockingUser() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return doGetLockingUser();
	}

	/**
	 * This method should be always overridden by remote repository
	 */
	protected String doGetLockingUser() throws SdaiException {
		return null;
	}


/**
 * Returns the set of <code>EntityExtent</code>s for all entity types
 * available in the EXPRESS schema corresponding to this model.
 * The entity data types in an EXPRESS schema can be defined
 * either directly or indirectly.
 * An <code>SdaiModel</code> contains an <code>EntityExtent</code>
 * for each entity data type.
 * The method is valid only when the access mode for the model is set.
 * @return the set of all <code>EntityExtent</code>s of this model.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @see #getPopulatedFolders
*/
	public AEntityExtent getFolders() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS  || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if (folders == null) {
			createFolders();
		}
		provideAllInstancesIfNeeded();
		return folders;
	}


	protected AEntityExtent createFolders() throws SdaiException {
		folders = new AEntityExtent(SdaiSession.setType0toN, this);
		SchemaData sch_data = null;
		if (repository == SdaiSession.systemRepository) {
			if (this == SdaiSession.baseDictionaryModel) {
				return folders;
			} else {
				sch_data = underlying_schema.owning_model.schemaData;
			}
		} else {
			if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
				dictionary.startReadOnlyAccess();
			}
			if (underlying_schema == null) {
				underlying_schema = dictionary.described_schema;
			}
			sch_data = dictionary.schemaData;
		}
		CEntityDefinition entities[] = sch_data.entities;
		for (int j = 0; j < sch_data.noOfEntityDataTypes; j++) {
			EntityExtent extent = new EntityExtent((CEntity_definition)entities[j], this, j);
			folders.addUnorderedRO(extent);
		}
		return folders;
	}


/**
	Gives a count of instances of an entity data type, specified by the index
	pointing to the (ordered) list of all entity types known in the
	underlying schema, and of its subtypes.
*/
	int countEntityInstances(int index) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return 0;
		}
		int iSum = lengths[index];
		if (underlying_schema == null) {
			getUnderlyingSchema();
		}
		int subtypes[] = underlying_schema.getSubtypes(index);
		if (subtypes != null) {
			for (int i = 0; i < subtypes.length; i++) {
//System.out.println("countEntityInstances " + index + " " + i + " " + lengths[subtypes[i]]);
				iSum += lengths[subtypes[i]];
			}
		}
		return iSum;
	}


	int countEntityInstancesRO(int index) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return 0;
		}
		int iSum = 0;
		if (underlying_schema == null) {
			getUnderlyingSchema();
		}
		SchemaData sch_data = dictionary.schemaData;
		int indexRO = find_entityRO(sch_data.entities[index]);
		if (indexRO >= 0) {
			iSum += lengths[indexRO];
		}
		int subtypes[] = underlying_schema.getSubtypes(index);
		if (subtypes != null) {
			for (int i = 0; i < subtypes.length; i++) {
//System.out.println("countEntityInstances " + index + " " + i + " " + lengths[subtypes[i]]);
				indexRO = find_entityRO(sch_data.entities[subtypes[i]]);
				if (indexRO >= 0) {
					iSum += lengths[indexRO];
				}
			}
		}
		return iSum;
	}


/**
 * Returns the set of <code>EntityExtent</code>s for those entity types
 * available in the EXPRESS schema corresponding to this model for which
 * instances currently exist in the model.
 * The entity data types in an EXPRESS schema can be defined
 * either directly or indirectly.
 * An <code>SdaiModel</code> contains an <code>EntityExtent</code>
 * for each entity data type. This method finds those
 * <code>EntityExtent</code>s which have at least one entity instance.
 * The method is valid only when the access mode for the model is set.
 * @return the set of nonempty <code>EntityExtent</code>s of this model.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getFolders
 */
	public AEntityExtent getPopulatedFolders() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if (folders == null) {
			createFolders();
		}
		provideAllInstancesIfNeeded();
		populated_folders.myLength = 0;
		for (int i = 0; i < folders.myLength; i++) {
			if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
				if (countEntityInstancesRO(i) > 0) {
					populated_folders.addUnorderedRO((EntityExtent)folders.myData[i]);
				}
			} else {
				EntityExtent ext = (EntityExtent)folders.myData[i];
				if (countEntityInstances(ext.index_to_entity) > 0) {
					populated_folders.addUnorderedRO(ext);
				}
			}
		}
		return populated_folders;
//		} // syncObject
	}


/**
 * Returns a <code>String</code> identifying the default language
 * for string values in this model, or <code>null</code> if the
 * default language for this model is not specified.
 * <p> After creation of the model using
 * {@link SdaiRepository#createSdaiModel createSdaiModel} method
 * the default language is unset.
 * In the case when a model was created during execution of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * method, this model may contain some value for the default language
 * extracted from the header of the exchange structure
 * (see ISO 10303-21::8.2.4 section_language).
 * @return <code>String</code> identifying the default language for string
 * values in the model.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @see #setDefaultLanguage
 * @see SdaiRepository#getDefaultLanguage
 * @see SdaiRepository#setDefaultLanguage
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public String getDefaultLanguage() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		return language;
	}


/**
 * Assigns a <code>String</code> to the attribute identifying
 * the default language for string values in this model.
 * To unset the default language, <code>null</code> to the
 * parameter of the method should be passed.
 * <p> When applying
 * {@link SdaiRepository#exportClearTextEncoding exportClearTextEncoding},
 * the default language, if non-null, will be written to the exchange structure
 * created by the above method.
 * @param value the default language for string
 * values in this model.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @see #getDefaultLanguage
 * @see SdaiRepository#getDefaultLanguage
 * @see SdaiRepository#setDefaultLanguage
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public void setDefaultLanguage(String value) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		language = value;
		modified_outside_contents = true;
	}


/**
 * Returns an aggregate <code>A_string</code> containing information
 * describing the contexts within which the instances of this model
 * are applicable.
 * <p> After creation of the model using
 * {@link SdaiRepository#createSdaiModel createSdaiModel} method
 * this aggregate is empty.
 * In the case when the model was created during execution of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * method, this aggregate may contain some context identifiers
 * extracted from the header of the exchange structure
 * (see ISO 10303-21::8.2.5 section_context).
 * That is, this aggregate always exists, but may be empty.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * <p> When applying
 * {@link SdaiRepository#exportClearTextEncoding exportClearTextEncoding} method,
 * the content of this aggregate, if non-empty, will be written to the exchange structure
 * created by the above method.
 * @return aggregate containing information describing the contexts.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @see SdaiRepository#getContextIdentifiers
 * @see "ISO 10303-21::8.2.5 section_context."
 */
	public A_string getContextIdentifiers() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return context_identifiers;
	}


/**
 * Informs if this model is modified.
 * A model is called modified if at least one instance within
 * it has been created, deleted or modified since the most recent
 * commit or abort operation was performed.
 * @return <code>true</code> if this model is modified, and
 * <code>false</code> otherwise.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 */
	public boolean isModified() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		return modified;
	}

	/**
	 * Returns this model effective permission which determines
	 * the model's access rights.
	 * @return the effective permission
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkRead
	 * @see #checkWrite
	 * @see SdaiPermission
	 * @since 4.0.1
	 */
	public abstract SdaiPermission checkPermission() throws SdaiException;

	/**
	 * Throws <code>SdaiException.SY_SEC</code> if the owning session
	 * has no read access to this model. Otherwise this method returns
	 * with no action.
	 * @throws SdaiException <code>SY_SEC</code> if the owning session is
	 *         not granted the read access
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkPermission
	 * @see #checkWrite
	 * @see SdaiPermission
	 * @since 4.0.1
	 */
	public abstract void checkRead() throws SdaiException;

	/**
	 * Throws <code>SdaiException.SY_SEC</code> if the owning session
	 * has no write access to this model. Otherwise this method returns
	 * with no action.
	 * @throws SdaiException <code>SY_SEC</code> if the owning session is
	 *         not granted the write access
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkPermission
	 * @see #checkRead
	 * @see SdaiPermission
	 * @since 4.0.1
	 */
	public abstract void checkWrite() throws SdaiException;

/**
 * Deletes this <code>SdaiModel</code> together with all entity instances within it.
 * The <code>SdaiModel</code> is also removed from the <code>SdaiRepository</code>
 * this model belongs to.
 * <p> Deleting of an <code>SdaiModel</code> from "SystemRepository"
 * is forbidden. In this case, SdaiException VT_NVLD is thrown.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException MO_LOCK, SDAI-model locked by another user.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see "ISO 10303-22::10.7.1 Delete SDAI-model"
 */
	public abstract void deleteSdaiModel() throws SdaiException;


	protected void deleteSdaiModelCommon(boolean remote) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		SdaiTransaction trans = session.active_transaction;
		trans.registerDeletedModel(this);
		scheduledForDeletion();
		repository.model_deleted = true;
		if ((mode & MODE_MODE_MASK) != NO_ACCESS) {
			exists = false;
		}
		if (remote && !committed) {
			repository.unresolved_mod_count--;
		}
		deleteSdaiModelWork(false, true, false, false);
		resolveInConnectors(true);
	}


	void deleteInverses(boolean delete_repo) throws SdaiException {
		Connector con = outConnectors;
		while (con != null) {
			con.disconnect();
			con = outConnectors;
		}
		if (instances_sim == null) {
			return;
		}
		for (int i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] == 0) {
				continue;
			}
			CEntity [] row_of_instances = instances_sim[i];
			for (int j = 0; j < lengths[i]; j++) {
				if (optimized) {
					row_of_instances[j].unsetInverseReferencesNoInverse(false, false, delete_repo);
				} else {
					row_of_instances[j].deleteInverseReferences(delete_repo);
				}
			}
		}
	}


/**
	Delets all the data within this SdaiModel.
*/
	void deleteSdaiModelWork(boolean make_virtual, boolean remove, boolean delete_or_close_repo, 
			boolean aborting) throws SdaiException {
if (SdaiSession.debug2) System.out.println("  SdaiModel   MODEL being DELETED: " + name);
		if ((mode & MODE_MODE_MASK) != NO_ACCESS) {
			if (delete_or_close_repo) {
				deleteContentsUnset();
			} else {
				deleteContents(make_virtual, aborting);
			}
		}

		instances_sim = null;
		lengths = null;
		//sorted = null;
		sim_status = null;
		to_entities = null;
		ex_model_names = null;
		ex_models = null;
		ex_repositories = null;
		ex_edefs = null;
//		if (folders != null) {
//			for (int i = 0; i < folders.myLength; i++) {
//				EntityExtent extent = (EntityExtent)folders.myData[i];
//				extent.instances = null;
//			}
//			folders = null;
//		}
		if (instances != null) {
			instances.myData = null;
			instances = null;
		}
		populated_folders = null;
		repository.session.active_models.removeUnorderedRO(this);
//System.out.println("  SdaiModel !!!!!!  model: " + name +
//"   its repo: " + repository.name);
		if (!make_virtual && remove) {
			repository.models.removeUnorderedKeepSorted(this);
			repository = null;
		}
		if (associated_with != null) {
			for (int i = 0; i < associated_with.myLength; i++) {
				SchemaInstance schema = (SchemaInstance)associated_with.myData[i];
				ASdaiModel models = schema.associated_models;
				if(models != null) {
					models.removeUnorderedRO(this);
				}
			}
			associated_with = null;
			initializeAssociatedWith();
		}
		underlying_schema = null;
		dictionary = null;
		if (make_virtual) {
			schema_name = null;
//			identifier = 0;
			entity_values = null;
		}
	}


/**
 * Assigns a new name to this <code>SdaiModel</code>.
 * This new name must differ from the names of other models in the
 * same <code>SdaiRepository</code>. If this condition is violated, then
 * SdaiException MO_DUP is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <p> This method realizes the SDAI operation "Rename SDAI-model",
 * specified in ISO 10303-22::10.7.2. Currently, the use of the method
 * is limited: if the model being renamed is referenced by instances in other
 * models (in the same or other repositories), and at the moment of
 * invocation of the method, access of at least one such model
 * is not started (or already ended), then the results of the renaming
 * operation are unpredictable.
 * <p> Renaming of an <code>SdaiModel</code> from "SystemRepository"
 * is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param provided_name new name of this model.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException MO_DUP, SDAI-model duplicate.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.7.2 Rename SDAI-model"
 */
	public void renameSdaiModel(String provided_name) throws SdaiException {
		if (provided_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (provided_name.length() == 0) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (repository.session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (repository.session.active_transaction.mode != READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, repository.session.active_transaction);
		}

//		synchronized (syncObject) {
		if (name.equals(provided_name)) {
			return;
		}
		repository.internal_usage = true;
		SdaiModel model = repository.findSdaiModel(provided_name);
		repository.internal_usage = false;
		if (model != null) {
			throw new SdaiException(SdaiException.MO_DUP, model);
		}
		int ind = 0;
		for (int i = 0; i < repository.models.myLength; i++) {
			Object mod = repository.models.myData[i];
			if (mod != this) {
				repository.models.myData[ind] = mod;
				ind++;
			}
		}
		repository.models.myData[ind] = this;
		name = provided_name;
		modified_outside_contents = true;
		repository.insertModel();
//		throw new SdaiException(SdaiException.FN_NAVL);
//		} // syncObject
	}


/**
 * Creates a new entity instance within this <code>SdaiModel</code>.
 * This method is applicable to only instantiable entities
 * (represented by their definitions), which are encountered in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if provided entity is not known for this schema,
 * or is not instantiable, then SdaiException ED_NVLD is thrown.
 * The integer used to construct the name of the created entity instance
 * (see ISO 10303-21::7.3.4) is larger than the number in the name
 * of any already existing instance in the same repository.
 * The attributes of the created entity instance are unset, so the
 * {@link EEntity#testAttribute testAttribute} method applied to any of them
 * will return value 0.
 * <p> If null value is passed to the method's
 * parameter, then SdaiException ED_NDEF is thrown.
 * @param entity_definition definition of the entity of which an instance has to be created.
 * @return the created entity instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #createEntityInstance(Class provided_class)
 * @see "ISO 10303-22::10.7.9 Create entity instance"
 * @see "ISO 10303-21::7.3.4 Entity instance names"
 */
	public EEntity createEntityInstance(EEntity_definition entity_definition)
			throws  SdaiException {
		if (entity_definition == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository.session.undo_redo_file != null && repository.session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
//		synchronized (syncObject) {
		CEntity instance = createEntityInstanceInternal((CEntity_definition)entity_definition, 0);
		bypass_setAll = true;
		try {
			instance.setAll(null);
		} finally {
			bypass_setAll = false;
		}
        //<--VV--030310--
        /*if (repository.largest_identifier < Long.MAX_VALUE) {
			repository.largest_identifier++;
			instance.instance_identifier = repository.largest_identifier;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_NVLI;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}*/
        instance.instance_identifier = repository.incPersistentLabel( this);
        instance.instance_position = CEntity.INS_MASK | instance.instance_position; //--VV-- Instance state tracking --
        //--VV--030310-->
		if (repository.session.undo_redo_file != null && !bypass) {
			repository.session.undoRedoCreatePrepare(instance, modified);
		}
		modified = true;
		return instance;
//		} // syncObject
	}


/**
	Creates a new entity instance within this <code>SdaiModel</code>.
	The method is invoked in 'createEntityInstance' and 'substituteInstance'
	(two variations) methods.
*/
	private CEntity createEntityInstanceInternal(CEntity_definition edef, long id)
			throws  SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			SdaiSession session = repository.session;
			if (session.active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}
			if (session.active_transaction.mode != READ_WRITE) {
				throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
			}
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
		if (!edef.getInstantiable(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_EDAB +
				SdaiSession.line_separator + AdditionalMessages.RD_ENT + edef.getName(null) +
				SdaiSession.line_separator + AdditionalMessages.RD_SCHE + underlying_schema.getName(null);
			throw new SdaiException(SdaiException.ED_NVLD, edef, base);
		}

		SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		int index_to_entity = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1,
//			edef.getNameUpperCase());
			(CEntityDefinition)edef);
		if (index_to_entity < 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_EDNV +
				SdaiSession.line_separator + AdditionalMessages.RD_ENT + edef.getName(null) +
				SdaiSession.line_separator + AdditionalMessages.RD_SCHE + underlying_schema.getName(null);
			throw new SdaiException(SdaiException.ED_NVLD, edef, base);
		}
		e_type_ind = index_to_entity;
//System.out.println("  SdaiModel  super: " + sch_data.super_inst.getClass().getName());
		if (id < 0) {
			index_to_entity = -1;
		}
		CEntity instance =
			m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
												 this, index_to_entity, id);
		if (id >= 0 && getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[index_to_entity] & SIM_LOADED_MASK) == SIM_LOADED_NONE) {

			sim_status[index_to_entity] =
				(short)((sim_status[index_to_entity] & ~SIM_LOADED_MASK) | SIM_LOADED_PARTIAL);
		}
		return instance;
	}


	private void insertEntityInstance(CEntity inst, int index_to_entity, boolean  at_end) throws  SdaiException {
		if (lengths[index_to_entity] >= instances_sim[index_to_entity].length) {
			ensureCapacity(index_to_entity);
		}
		if (at_end) {
			instances_sim[index_to_entity][lengths[index_to_entity]] = inst;
			invalidate_quick_find();
		} else {
			if (!insertInstance(index_to_entity, inst, inst.instance_identifier)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[index_to_entity] & SIM_LOADED_MASK) == SIM_LOADED_NONE) {

			sim_status[index_to_entity] =
				(short)((sim_status[index_to_entity] & ~SIM_LOADED_MASK) | SIM_LOADED_PARTIAL);
		}
		lengths[index_to_entity]++;
	}


/**
 * Creates a new entity instance within this <code>SdaiModel</code>.
 * This method is applicable to only instantiable entities
 * (represented by the corresponding class EXxx.class where "xxx" is
 * entity name), which are encountered in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * or an entity was identified but is not instantiable,
 * then SdaiException ED_NDEF and, respectively, ED_NVLD is thrown.
 * The integer used to construct the name of the created entity instance
 * (see ISO 10303-21::7.3.4) is larger than the number in the name
 * of any already existing instance in the same repository.
 * The attributes of the created entity instance are unset, so the
 * {@link EEntity#testAttribute testAttribute} method applied to any of them
 * will return value 0.
 * <p> If null value is passed to the method's
 * parameter, then SdaiException VA_NSET is thrown.
 * @param provided_class Java class for the entity of which an instance has to be created.
 * @return the created entity instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #createEntityInstance(EEntity_definition entity_definition)
 * @see "ISO 10303-22::10.7.9 Create entity instance"
 * @see "ISO 10303-21::7.3.4 Entity instance names"
 */
	public EEntity createEntityInstance(Class provided_class) throws SdaiException {
//System.out.println("SdaiModel!!!!!!!!!!!!!!!! provided_class: " + provided_class.getName());
		if (provided_class == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
//		synchronized (syncObject) {
		CEntity_definition edef = (CEntity_definition)checkEntity(provided_class.getName());
		if (edef == null) {
			String f_name = provided_class.getName();
			String entity_name = f_name.substring(f_name.lastIndexOf(".") + 2, f_name.length());
			String base = SdaiSession.line_separator + AdditionalMessages.EI_EDNV +
				SdaiSession.line_separator + AdditionalMessages.RD_ENT + entity_name +
				SdaiSession.line_separator + AdditionalMessages.RD_SCHE + underlying_schema.getName(null);
			throw new SdaiException(SdaiException.ED_NDEF, base);
		}
		return createEntityInstance(edef);
//		} // syncObject
	}


/**
	Creates a new entity instance within this <code>SdaiModel</code>.
	The method is invoked in 'copyApplicationInstance' method in class CEntity.
*/
	CEntity makeEntityInstance(CEntity_definition edef, int index_to_entity) throws  SdaiException {
		SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
		CEntity instance = m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
			this, index_to_entity, 0);
		//<--VV--030310--
        /*if (repository.largest_identifier < Long.MAX_VALUE) {
			repository.largest_identifier++;
			instance.instance_identifier = repository.largest_identifier;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_NVLI;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
        */
		if (getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[index_to_entity] & SIM_LOADED_MASK) == SIM_LOADED_NONE) {

			sim_status[index_to_entity] =
				(short)((sim_status[index_to_entity] & ~SIM_LOADED_MASK) | SIM_LOADED_PARTIAL);
		}
        instance.instance_identifier = repository.incPersistentLabel( this);
        instance.instance_position = CEntity.INS_MASK | instance.instance_position; //--VV-- Instance state tracking --
        //--VV--030310-->
		modified = true;
		return instance;
	}


/**
 * Makes the contents of this <code>SdaiModel</code> available for read-only access.
 * This method is available both for models with the application data and for
 * models in "SystemRepository". In the latter case there is no need for the
 * transaction to be started. If the model contains application data and the
 * dictionary model with the data of the schema that is underlying to the
 * former model is without read-only access, then the access for this
 * dictionary model (contained in "SystemRepository") is started automatically.
 * <p> The model, if its access was started successfully, is included
 * into the set "active_models" of <code>SdaiSession</code>.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException MX_RW, SDAI-model access read-write.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #startReadWriteAccess
 * @see #promoteSdaiModelToRW
 * @see #endReadOnlyAccess
 * @see SdaiSession#getActiveModels
 * @see "ISO 10303-22::10.7.3 Start read-only access"
 */
	public abstract void startReadOnlyAccess() throws SdaiException;


	protected void startAccessRemote() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < repository.models.myLength; i++) {
				SdaiModel mod = (SdaiModel)repository.models.myData[i];
				if ((mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					count++;
				}
			}
			if (count >= 1) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OMZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (repository.session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
	}


	protected boolean startReadOnlyAccessRemote() throws SdaiException {
		startAccessRemote();
		if (repository.session.active_transaction.mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, repository.session.active_transaction);
		}
		if (underlying_schema == null && dictionary == null) {
			return true;
		}
		return false;
	}


	protected boolean startReadWriteAccessRemote() throws SdaiException {
		startAccessRemote();
		if (repository.session.active_transaction.mode != READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, repository.session.active_transaction);
		}
		if (underlying_schema == null && dictionary == null) {
			return true;
		}
		return false;
	}


	protected boolean initializeRemote(int mode_to_start) throws SdaiException {
		initializeContents();
		if (committed) {
			return true;
		}
		return false;
	}


/**
 * Makes the contents of this <code>SdaiModel</code> available for read-write access.
 * For models in "SystemRepository" this method is forbidden
 * (SdaiException FN_NAVL is thrown).
 * If the dictionary model with the data of the schema that is underlying to this
 * model is without read-only access, then the access for this
 * dictionary model (contained in "SystemRepository") is started automatically.
 * <p> The model, if its access was started successfully, is included
 * into the set "active_models" of <code>SdaiSession</code>.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException MX_RW, SDAI-model access read-write.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException MO_LOCK, SDAI-model locked by another user.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #startReadOnlyAccess
 * @see #reduceSdaiModelToRO
 * @see #endReadWriteAccess
 * @see SdaiSession#getActiveModels
 * @see "ISO 10303-22::10.7.6 Start read-write access"
 */
	public abstract void startReadWriteAccess() throws SdaiException;


	/**
	 * Makes the contents of this <code>SdaiModel</code> available for read-only access
	 * in partial dynamic mode. No actual instances are loaded to memory when this method
	 * is invoked but needed data is provided lazily on demand.
	 * This method is available only for remote <code>SdaiModels</code>
	 *
	 * @exception SdaiException if an error occurs during the operation
	 * @exception SdaiException FN_NAVL, if the method is invoked for non remote <code>SdaiModel</code>
	 */
	public abstract void startPartialReadOnlyAccess() throws SdaiException;

	/**
	 * Makes the contents of this <code>SdaiModel</code> available for read-write access
	 * in partial dynamic mode. No actual instances are loaded to memory when this method
	 * is invoked but needed data is provided lazily on demand.
	 * This method is available only for remote <code>SdaiModels</code>
	 *
	 * @exception SdaiException if an error occurs during the operation
	 * @exception SdaiException FN_NAVL, if the method is invoked for non remote <code>SdaiModel</code>
	 */
    public abstract void startPartialReadWriteAccess() throws SdaiException;

/**
 * Changes the access mode for this <code>SdaiModel</code> from read-only to read-write.
 * For models in "SystemRepository" this method is forbidden
 * (SdaiException FN_NAVL is thrown).
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException MX_RW, SDAI-model access read-write.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #endReadOnlyAccess
 * @see "ISO 10303-22::10.7.4 Promote SDAI-model to read-write"
 */
	public abstract void promoteSdaiModelToRW() throws SdaiException;


	protected void promoteSdaiModelToRWRemote() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < repository.models.myLength; i++) {
				SdaiModel mod = (SdaiModel)repository.models.myData[i];
				if ((mod.mode & MODE_MODE_MASK) != NO_ACCESS && mod != this) {
					count++;
				}
			}
			if (count >= 1) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_OMZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
	}


	protected void promoteSdaiModelFromSmall() throws SdaiException {
		int i;
		int size = dictionary.schemaData.noOfEntityDataTypes + 1;
		CEntity [] new_array [] = new CEntity[size][];
		int new_lengths [] = new int[size];
		SimIdx new_start [] = new SimIdx[size];
		SimIdxEnd new_end [] = new SimIdxEnd[size];
		short new_status [] = new short[size];

		for (i = 0; i < size; i++) {
			new_lengths[i] = 0;
			new_status[i] |= SIM_SORTED;
			new_status[i] = (short)((new_status[i] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
		}
		if (entities != null) {
			for (i = 0; i < entities.length; i++) {
				int index_to_ent = dictionary.schemaData.findEntityExtentIndex((CEntity_definition)entities[i]);
				new_array[index_to_ent] = instances_sim[i];
				new_lengths[index_to_ent] = lengths[i];
				new_start[index_to_ent] = instances_sim_start[i];
				new_end[index_to_ent] = instances_sim_end[i];
				new_status[index_to_ent] = sim_status[i];
			}
		}
		for (i = 0; i < size; i++) {
			if (new_lengths[i] == 0) {
				new_array[i] = emptyArray;
			}
		}
		instances_sim = new_array;
		lengths = new_lengths;
		instances_sim_start = new_start;
		instances_sim_end = new_end;
		sim_status = new_status;
		entities = null;
		invalidate_quick_find();
	}


/**
 * Changes the access mode for this <code>SdaiModel</code> from read-write to read-only.
 * If some entity instance within this <code>SdaiModel</code> has been
 * created, deleted or modified since the most recent
 * commit, abort or start transaction read-write access
 * operation was performed, then SdaiException TR_RW is thrown.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @throws SdaiException TR_RW, transaction read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #endReadWriteAccess
 * @see SdaiTransaction#commit
 * @see SdaiTransaction#abort
 * @see SdaiSession#startTransactionReadWriteAccess
 */
	public abstract void reduceSdaiModelToRO() throws SdaiException;


	protected void reduceSdaiModelToRORemote() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if (modified) {
			throw new SdaiException(SdaiException.TR_RW, repository.session.active_transaction);
		}
	}


	protected void reduceSdaiModelToSmall() throws SdaiException {
		int i;
		int pop_count = 0;
		int write_i = 0;
		CEntityDefinition ents [] = dictionary.schemaData.entities;
		for (i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] != null && lengths[i] > 0) {
				pop_count++;
			}
		}
		entities = new CEntityDefinition[pop_count];
		CEntity [] new_array [] = new CEntity[pop_count + 1][];
		new_array[pop_count] = emptyArray;
		int new_lengths [] = new int[pop_count + 1];
		SimIdx new_start [] = new SimIdx[pop_count + 1];
		SimIdxEnd new_end [] = new SimIdxEnd[pop_count + 1];
		short new_status [] = new short[pop_count + 1];
		for (i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] == 0) {
				continue;
			}
			entities[write_i] = ents[i];
			new_array[write_i] = instances_sim[i];
			new_lengths[write_i] = lengths[i];
			new_start[write_i] = instances_sim_start[i];
			new_end[write_i] = instances_sim_end[i];
			new_status[write_i] = sim_status[i];
			instances_sim[i] = null;
			instances_sim_start[i] = null;
			instances_sim_end[i] = null;
			write_i++;
		}
		instances_sim = new_array;
		lengths = new_lengths;
		instances_sim_start = new_start;
		instances_sim_end = new_end;
		sim_status = new_status;
		invalidate_quick_find();
	}


/**
 * Terminates the read-only access for this <code>SdaiModel</code>.
 * As a result, the contents of the model becomes inaccessable.
 * The model is removed from the set "active_models" of <code>SdaiSession</code>.
 * When applied to a model in "SystemRepository", this method
 * throws <code>SdaiException</code> FN_NAVL.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException MX_RW, SDAI-model access read-write.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #startReadOnlyAccess
 * @see #promoteSdaiModelToRW
 * @see SdaiSession#getActiveModels
 * @see "ISO 10303-22::10.7.5 End read-only access"
 */
	public abstract void endReadOnlyAccess() throws SdaiException;


	void loadInstanceIdentifiersRemoteModel() throws SdaiException {
		loadInstanceIdentifiersRemote();
	}


	protected void endReadOnlyAccessCommon() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		exists = true;
		boolean saved_modified = modified;
		deleteContents(true, false);
		modified = saved_modified;
		repository.session.active_models.removeUnorderedRO(this);
		if (Implementation.build >= 249 && inst_deleted_permanently) {
			inst_deleted = false;
			inst_deleted_permanently = false;
		}
		if (entity_values != null) {
			entity_values.unset_ComplexEntityValue();
		}
	}


/**
 * Terminates the read-write access for this <code>SdaiModel</code>.
 * As a result, the contents of the model becomes inaccessable.
 * The model is removed from the set "active_models" of <code>SdaiSession</code>.
 * If some entity instance within this <code>SdaiModel</code> has been
 * created, deleted or modified since the most recent
 * commit, abort or start transaction read-write access
 * operation was performed, then SdaiException TR_RW is thrown.
 * @throws SdaiException TR_RW, transaction read-write.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #startReadWriteAccess
 * @see #reduceSdaiModelToRO
 * @see SdaiSession#getActiveModels
 * @see "ISO 10303-22::10.7.7 End read-write access"
 */
	public abstract void endReadWriteAccess() throws SdaiException;


	protected void endReadWriteAccessCommon() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if (modified || modified_by_import) {
			throw new SdaiException(SdaiException.TR_RW, repository.session.active_transaction);
		}
		repository.endAccessExternalData(this);
		exists = true;
		boolean saved_modified = modified;
//closingAll=true;
		deleteContents(true, false);
//closingAll=false;
		modified = saved_modified;
		repository.session.active_models.removeUnorderedRO(this);
		if (Implementation.build >= 249 && inst_deleted_permanently) {
			inst_deleted = false;
			inst_deleted_permanently = false;
		}
		if (entity_values != null) {
			entity_values.unset_ComplexEntityValue();
		}
	}


	protected void endAccessAll() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
//			throw new SdaiException(SdaiException.MX_NDEF, this);
			return;
		}
		if (modified || modified_by_import) {
			throw new SdaiException(SdaiException.TR_RW, repository.session.active_transaction);
		}
		exists = true;
		boolean saved_modified = modified;
		deleteContents(true, false);
		modified = saved_modified;
		repository.session.active_models.removeUnorderedRO(this);
		if (Implementation.build >= 249 && inst_deleted_permanently) {
			inst_deleted = false;
			inst_deleted_permanently = false;
		}
		if (entity_values != null) {
			entity_values.unset_ComplexEntityValue();
		}
	}


	protected void restartReopenedAccess(boolean partial) throws SdaiException { }

/*
 * This method is for transaction level 2, but not for transaction level 3 which is used in JSDAI.
 * Exception FN_NAVL will be thrown if this method is invoked.
 * <BR>For details please look at ISO/DIS 10303-22::10.7.10
 * @throws SdaiException SS_NOPN, an SDAI session is not open.
 * @throws SdaiException MO_NEXS, the SDAI-model does not exist.
 * @throws SdaiException MX_NRW, the SDAI-model access is not read-write.
 * @throws SdaiException FN_NAVL, this function is not supported by implementation.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.7.10 Undo changes"
 *
	public void undoChanges() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
*/

/*
 * This method is for transaction level 2, but not for transaction level 3 which is used in JSDAI.
 * Exception FN_NAVL will be thrown if this method is invoked.
 * <BR>For details please look at ISO/DIS 10303-22::10.7.11
 * @throws SdaiException SS_NOPN, an SDAI session is not open.
 * @throws SdaiException MO_NEXS, the SDAI-model does not exist.
 * @throws SdaiException MX_NRW, the SDAI-model access is not read-write.
 * @throws SdaiException FN_NAVL, this function is not supported by implementation.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see "ISO 10303-22::10.7.11 Save changes"
 *
	public void saveChanges() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
 */

	/**
	 * Adds <code>SdaiListener</code> extending <code>java.util.EventListener</code>
	 * to a special aggregate in this repository.
	 * @param listener a <code>SdaiListener</code> to be added.
	 * @see #removeSdaiListener
	 * @since 4.1.0
	 */
	public void addSdaiListener(SdaiListener listener) {
		try {
			if (listenrList == null) {
				listenrList = new CAggregate(SdaiSession.setTypeSpecial);
			}
			listenrList.addUnordered(listener, null);
		} catch (SdaiException ex) {
			throw (IllegalStateException)
				new IllegalStateException(ex.getMessage()).initCause(ex);
		}
	}


	/**
	 * Removes <code>SdaiListener</code> extending <code>java.util.EventListener</code>
	 * from the special aggregate in this repository.
	 * @param listener <code>SdaiListener</code> to be removed.
	 * @see #addSdaiListener
	 * @since 4.1.0
	 */
	public void removeSdaiListener(SdaiListener listener) {
		try {
			if (listenrList != null) {
				listenrList.removeUnordered(listener, null);
			}
		} catch (SdaiException ex) {
			throw (IllegalStateException)
				new IllegalStateException(ex.getMessage()).initCause(ex);
		}
	}

/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.4.14 SDAI query"
 */
	public int query(String where, EEntity entity, AEntity result) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		ASdaiModel domain = new ASdaiModel();
		domain.addByIndex(1, this, null);
		return domain;
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		return null;
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		return getQuerySourceInstanceRef();
	}


/**
 * Returns entity definition for the entity with the specified name.
 * This method searches the set of entities that are encountered in the
 * EXPRESS schema whose definition is underlying for this model.
 * Method's parameter is some string giving the name of an entity.
 * If this entity is complex, then its constituents (simple entity data
 * types) within this string are separated by '+' character.
 * For example, "length_unit+si_unit". Both lower case letters and
 * upper case letters are acceptable. If this parameter does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param entity_name the name of the entity whose definition is requested.
 * @return definition of the specified entity.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SchemaDefinition#getEntityDefinition
 * @see "ISO 10303-22::10.7.8 Get entity definition"
 */
	public EEntity_definition getEntityDefinition(String entity_name) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (underlying_schema == null) {
			getUnderlyingSchema();
		}
		return underlying_schema.getEntityDefinition(entity_name);
//		} // syncObject
	}


/**
	Finds the index of the entity specified by its name given as parameter 'key'
	in the (ordered) list of entities specified by the parameter 'set'.
	This operation is based on the binary search.
*/
	private int find_entity(int left, int right, String key, AEntity_definition set)
			throws SdaiException {
		Object [] myDataA = (Object [])((AEntity)set).myData;
		while (left <= right) {
			int middle = (left + right)/2;
			CEntity_definition entity = (CEntity_definition)myDataA[middle];
			int comp_res = entity.getNameUpperCase().compareTo(key);
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


/**
 * Returns schema definition if this <code>SdaiModel</code>
 * manages dictionary data, and null otherwise.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @return definition of the schema which is described
 * by this <code>SdaiModel</code>.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 */
	public ESchema_definition getDefinedSchema() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		return described_schema;
	}


/**
	Promotes this SdaiModel from the state 'virtual' to the state 'ordinary'.
*/
	void promoteVirtualToOrdinary(SdaiModel dict_model) throws SdaiException {
		dictionary = dict_model;
		if (dict_model.described_schema != null) {
			underlying_schema = dict_model.described_schema;
		}
		initializeSdaiModel();
	}


	protected void promoteVirtualToOrdinaryRemoteBasic(SdaiModelHeader modelHeader) throws SdaiException {
		String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dictionary.getMode() == NO_ACCESS) {
			dictionary.startReadOnlyAccess();
			underlying_schema = dictionary.described_schema;
		}
		initializeSdaiModel();
		fromSdaiModelHeader(modelHeader);
		committed = true;
	}


/**
	Deletes all instances contained in this SdaiModel.
	Also removes all outcoming connectors (but not incoming ones).
*/
	void deleteContents(boolean replace_by_connector, boolean aborting) throws SdaiException {
		int i, j;
		Connector con = outConnectors;
		while (con != null) {
			con.disconnect();
			con = outConnectors;
		}
		CEntity [] row_of_instances;
		deleting = true;
		if (instances_sim != null) {
			for (i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null || lengths[i] == 0) {
					continue;
				}
				row_of_instances = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					if (optimized) {
						row_of_instances[j].unsetInverseReferencesNoInverse(true, replace_by_connector, false);
					} else {
						row_of_instances[j].unsetInverseReferences(replace_by_connector, aborting);
					}
				}
			}

			refresh_in_abort = aborting;
			for (i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null || lengths[i] == 0) {
					continue;
				}
				row_of_instances = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					CEntity instance = row_of_instances[j];
					bypass_setAll = true;
					try {
						instance.setAll(null);
					} finally {
						bypass_setAll = false;
					}
					instance.deletedObject();
					instance.owning_model = null;
					instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
					instance.fireSdaiEvent(SdaiEvent.INVALID, -1, null);
//if (instance instanceof CEntityDefinition) {
//((CEntityDefinition)instance).partialEntityTypes = null;
//}
				}
				instances_sim[i] = emptyArray;
                sim_status[i] &= ~SIM_SORTED;
                //sim_status[i] = (short)((sim_status[number_of_entities] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
				lengths[i] = 0;
			}
			refresh_in_abort = false;
			invalidate_quick_find();
		}
		if (folders != null) {
			for (i = 0; i < folders.myLength; i++) {
				EntityExtent extent = (EntityExtent)folders.myData[i];
				extent.owned_by = null;
				extent.definition = null;
			}
			folders = null;
		}
		deleting = false;
		setMode(NO_ACCESS);
	}


	private final void deleteContentsUnset() throws SdaiException {
		int i;
		deleting = true;
		if (instances_sim != null) {
			for (i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null || lengths[i] == 0) {
					continue;
				}
				CEntity [] row_of_instances = instances_sim[i];
				for (int j = 0; j < lengths[i]; j++) {
					CEntity instance = row_of_instances[j];
					bypass_setAll = true;
					try {
						instance.setAll(null);
					} finally {
						bypass_setAll = false;
					}
					instance.deletedObject();
					instance.owning_model = null;
					instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
					instance.fireSdaiEvent(SdaiEvent.INVALID, -1, null);
				}
				instances_sim[i] = emptyArray;
                sim_status[i] &= ~SIM_SORTED;
                //sim_status[i] = (short)((sim_status[number_of_entities] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
				lengths[i] = 0;
			}
			invalidate_quick_find();
		}
		if (folders != null) {
			for (i = 0; i < folders.myLength; i++) {
				EntityExtent extent = (EntityExtent)folders.myData[i];
				extent.owned_by = null;
				extent.definition = null;
			}
			folders = null;
		}
		deleting = false;
		setMode(NO_ACCESS);
	}


	void deleteContentsRemote() throws SdaiException {
		Connector con = outConnectors;
		while (con != null) {
			con.disconnect();
			con = outConnectors;
		}
		if (folders != null) {
			for (int i = 0; i < folders.myLength; i++) {
				EntityExtent extent = (EntityExtent)folders.myData[i];
				extent.owned_by = null;
				extent.definition = null;
			}
			folders = null;
		}
		setMode(NO_ACCESS);
	}


/**
	Checks whether the string submitted as a parameter is the name
	of some entity data type known in the schema underlying for this SdaiModel.
	If so, definition of such an entity is returned.
	Method is used in createEntityInstance and substituteInstance methods.
*/
	private CEntityDefinition checkEntity(String full_name) throws SdaiException {
		String entity_name =
			full_name.substring(full_name.lastIndexOf(".") + 2,
													full_name.length()).toUpperCase();
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
//System.out.println(" SdaiModel  model: " + underlying_schema.modelDictionary.name);
		int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, entity_name);
		CEntityDefinition entity;
		if (index >= 0) {
			entity = sch_data.entities[index];
		} else {
			entity = null;
		}
		return entity;
	}


/**
 * Substitutes a given entity instance with a newly created one which is
 * written to the contents of this <code>SdaiModel</code>.
 * The given entity instance can belong to other model and even
 * to other repository. Moreover, the entity type of the given instance
 * and that of the new one may be different.
 * This new entity must be instantiable and  must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if this entity is not known for this schema,
 * or is not instantiable, then SdaiException ED_NVLD is thrown.
 * At the end of the operation the given instance is deleted
 * from its owning model.
 * <p> If entity type supplied for creation of new instance is the same as
 * entity type of the given one, then values of all attributes
 * are passed from the old instance to its substitute.
 * If entity types are different, then values are passed only for
 * common parts of these entities. The remaining attributes are left unset,
 * so the {@link EEntity#testAttribute testAttribute} method applied
 * to any of them will return value 0.
 * If the given instance belongs to the same repository as this model does,
 * then its substitute gets the persistent label (or name as specified in
 * ISO 10303-21::7.3.4) of it. Otherwise, new persistent label for
 * the substitute is chosen. The integer used to construct it
 * is taken larger than the number in the persistent label of any already
 * existing instance in the same repository.
 * All the references from other instances within models in at least
 * read-only mode to the given instance are replaced by the references
 * to its substitute.
 * <p> If null value is passed to one of the method's parameters specifying
 * the given instance and entity type for its substitute, then
 * SdaiException VA_NSET and, respectively, ED_NDEF is thrown.
 * Submitting an invalid instance leads to throwing SdaiException EI_NEXS.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param old given entity instance.
 * @param entity_definition definition of the entity of which an instance
 * has to substitute the given one.
 * @return the created entity instance which substitutes the old one.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #substituteInstance(EEntity old, Class type)
 * @see #substituteInstance(EEntity old)
 * @see "ISO 10303-21::7.3.4 Entity instance names"
 */
	public EEntity substituteInstance(EEntity old,
			EEntity_definition entity_definition) throws SdaiException {
		if (old == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (((CEntity)old).owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (entity_definition == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository.session.undo_redo_file != null && repository.session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int i, j;
//		synchronized (syncObject) {
		CEntity_definition old_def = (CEntity_definition)old.getInstanceType();
		CEntity_definition new_def = (CEntity_definition)entity_definition;
		CEntity older = (CEntity)old;
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, this);
		}

		SdaiModel older_owner = older.owning_model;
		if ((older_owner.mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, older_owner);
		}
		boolean xim = false;
		String u_sch_name = older_owner.underlying_schema.getName(null);
		int u_sch_ind = u_sch_name.length() - 4;
		String u_sch_sufix = u_sch_name.substring(u_sch_ind).toUpperCase();
		if (u_sch_sufix.equals(SUFIX_XIM)) {
			xim = true;
		}
		if (older_owner == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		StaticFields staticFields;
		if (old_def == new_def) {
			if (older_owner != this) {
				SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
				int index_to_entity = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1,
					(CEntityDefinition)old_def);
				if (index_to_entity < 0) {
					throw new SdaiException(SdaiException.ED_NVLD, old_def);
				}
				if (repository.session.undo_redo_file != null && !bypass) {
					repository.session.undoRedoSubstitutePrepare(older, older, modified);
				}
				older.deleteInstanceWeak();
				older.owning_model = this;
				staticFields = StaticFields.get();
				if (staticFields.entity_values == null) {
					staticFields.entity_values = new ComplexEntityValue();
				}
				if (older_owner.outConnectors != null) {
					moveConnectors(staticFields.entity_values, old_def, older, older_owner);
				}
				if (repository == older_owner.repository) {
					insertEntityInstance(older, index_to_entity, false);
				} else {
					older.instance_identifier = repository.incPersistentLabel(this);
					insertEntityInstance(older, index_to_entity, true);
					older_owner.inst_deleted = true;
				}
				older.instance_position |= CEntity.INS_MASK;
				modified = true;
				if (older_owner.optimized) {
					older.setModifiedFlagNoInverse();
				} else {
					older.setModifiedFlag();
				}
			}
			return older;
		}

		staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		Value val_message;
		if (older_owner.optimized) {
			val_message = older.checkInverseReferencesNoInverse(new_def);
		} else {
			val_message = older.checkInverseReferences(new_def);
		}
//boolean is_subtp = new_def.isSubtypeOf(old_def);
//System.out.println("SdaiModel    is_subtp = " + is_subtp + "   val_message: " + val_message);
		if (!new_def.isSubtypeOf(old_def) && val_message != null) {
//System.out.println("SdaiModel ?????   val_message.reference = " + val_message.reference);
			CExplicit_attribute users_attr = (CExplicit_attribute)val_message.reference;
			CEntity user = (CEntity)val_message.agg_owner;
			throw new SdaiException(SdaiException.ED_NVLD, new_def,
"Entity definition of the substitute is not a subtype of the entity the instance of which is being replaced " +
"and there exists a reference to this instance by an attribute whose type is such that the type of the substitute " +
"is not compatible with it." +
SdaiSession.line_separator + "   Old instance: " + older +
SdaiSession.line_separator + "   Its user: " + user +
SdaiSession.line_separator + "   User's attribute: " + users_attr +
SdaiSession.line_separator + "   New instance definition: " + new_def);
		}
		long ident;
		long key;
		if (repository == older_owner.repository) {
			key = ident = older.instance_identifier;
		} else {
			//<--VV--030310--
            /*if (repository.largest_identifier < Long.MAX_VALUE) {
				repository.largest_identifier++;
				ident = repository.largest_identifier;
				older_owner.inst_deleted = true;
			} else {
				String base = SdaiSession.line_separator + AdditionalMessages.EI_NVLI;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}*/
			ident = repository.incPersistentLabel( this);
            //--VV--030310-->
			key = 0;
		}
		CEntity substitute = createEntityInstanceInternal(new_def, -1);
		if (xim) {
			staticFields.entity_values.xim_special_substitute_instance = true;
		}
		prepareAll(staticFields.entity_values, old_def);
		older.getAll(staticFields.entity_values);
//print_entity_values(staticFields.entity_values, 161);
		if (xim) {
			staticFields.entity_values.xim_special_substitute_instance = false;
		}
		int start_index = 0;
		if (new_def.noOfPartialEntityTypes > entity_values.entityValues.length) {
			entity_values.enlarge(new_def.noOfPartialEntityTypes);
		} else {
			for (i = 0; i < new_def.noOfPartialEntityTypes; i++) {
				if (entity_values.entityValues[i] == null) {
					entity_values.entityValues[i] = new EntityValue(repository.session);
				}
			}
		}
		entity_values.def = new_def;
// The values of the attributes of the instance being replaced are wrapped into an object of Value class.
		for (i = 0; i < new_def.noOfPartialEntityTypes; i++) {
			CEntity_definition new_partial_def = new_def.partialEntityTypes[i];
			int res_index = ((CEntityDefinition)old_def).find_partial_entity_upper_case(start_index,
				old_def.noOfPartialEntityTypes - 1, new_partial_def.getNameUpperCase());
			int count = new_partial_def.noOfPartialAttributes;
			EntityValue partial_entity_values = entity_values.entityValues[i];
			if (partial_entity_values.values == null) {
				if (SdaiSession.NUMBER_OF_VALUES >=count) {
					partial_entity_values.values = new Value[SdaiSession.NUMBER_OF_VALUES];
				} else {
					partial_entity_values.values = new Value[count];
				}
			} else if (partial_entity_values.values.length < count) {
				partial_entity_values.enlarge(count);
			}
			partial_entity_values.def = new_partial_def;
			partial_entity_values.count = count;
			if (res_index < 0) {
				for (j = 0; j < count; j++) {
					if (partial_entity_values.values[j] == null) {
						partial_entity_values.values[j] = new Value();
					}
					partial_entity_values.values[j].tag = PhFileReader.MISSING;
				}
			} else {
				for (j = 0; j < count; j++) {
					if (partial_entity_values.values[j] == null) {
						partial_entity_values.values[j] = new Value();
					}
					partial_entity_values.values[j].copyValue(
						staticFields.entity_values.entityValues[res_index].values[j], substitute);
				}
				start_index = res_index + 1;
			}
		}
		if (repository.session.undo_redo_file != null && !bypass) {
			repository.session.undoRedoSubstitutePrepare(older, substitute, modified);
		}
		store_instance(substitute, key);
		if (xim) {
			entity_values.xim_special_substitute_instance = true;
		}
//if (older.instance_identifier == 349)
//print_entity_values(entity_values, 349);
//if (older.instance_identifier == 349) {
//System.out.println("SdaiModel *********** BEFORE older inverse: #" + older.instance_identifier +
//"  its type: " + older.getInstanceType().getName(null));
//older.printInverses();
//CEntity inst314 = (CEntity)repository.getSessionIdentifier("#314");
//System.out.println("SdaiModel *********** BEFORE  #314");inst314.printInverses();
//CEntity inst346 = (CEntity)repository.getSessionIdentifier("#346");
//System.out.println("SdaiModel *********** BEFORE  #346");inst346.printInverses();
//System.out.println("");System.out.println("");
//}
		substitute.setAll(entity_values);
		if (xim) {
			entity_values.xim_special_substitute_instance = false;
		}
		if (repository != older_owner.repository) {
			older_owner.inst_deleted = true;
		}
		substitute.instance_identifier = ident;
		substitute.instance_position = CEntity.INS_MASK | substitute.instance_position; //--VV-- Instance state tracking --
//if (older.instance_identifier == 349) {
//System.out.println("SdaiModel *********** AAAAA older inverse: #" + older.instance_identifier +
//"  its type: " + older.getInstanceType().getName(null));
//older.printInverses();
//CEntity inst314a = (CEntity)repository.getSessionIdentifier("#314");
//System.out.println("SdaiModel *********** AAAAA  #314");inst314a.printInverses();}
		older_owner.substitute_operation = true;
		if (older_owner.optimized) {
			older.changeInverseReferencesNoInverse(older, substitute, false);
		} else {
			older.changeInverseReferences(older, substitute, false, false);
		}
//if (older.instance_identifier == 349) {
//System.out.println("SdaiModel *********** BBBBB older inverse: #" + older.instance_identifier +
//"  its type: " + older.getInstanceType().getName(null));
//older.printInverses();
//CEntity inst314b = (CEntity)repository.getSessionIdentifier("#314");
//System.out.println("SdaiModel *********** CCCCC  #314");inst314b.printInverses();}
		boolean saved_value = older_owner.inst_deleted;
		older_owner.undo_delete = -1;
		older.deleteApplicationInstance();
		older_owner.substitute_operation = false;
//if (older.instance_identifier == 349) {
//System.out.println("SdaiModel *********** CCCCC older inverse: #" + older.instance_identifier +
//"  its type: " + older.getInstanceType().getName(null));older.printInverses();
//CEntity inst314c = (CEntity)repository.getSessionIdentifier("#314");
//System.out.println("SdaiModel *********** CCCCC  #314");inst314c.printInverses();
//System.out.println("SdaiModel *********** CCCCC substitute inverse: #" + substitute.instance_identifier +
//"  its type: " + substitute.getInstanceType().getName(null));substitute.printInverses();}
		older_owner.undo_delete = 0;
		older_owner.inst_deleted = saved_value;
		modified = true;
		substitute.inverseList = older.inverseList;
//if (older.instance_identifier == 349) {
//System.out.println("SdaiModel *********** substitute inverse 349");substitute.printInverses();}
//if (older.instance_identifier == 349 || ident == 349)
//System.out.println("SdaiModel !!! Place 2   older: #" + older.instance_identifier + "  substitute: " + substitute);
//System.out.println("");System.out.println("");
		return substitute;
//		} // syncObject
	}


	private void store_instance(CEntity inst, long id) throws SdaiException {
		if (e_type_ind < underlying_schema.owning_model.schemaData.entities.length) {
			if (lengths[e_type_ind] >= instances_sim[e_type_ind].length) {
				ensureCapacity(e_type_ind);
			}
			if (id > 0) {
				if (!insertInstance(e_type_ind, inst, id)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			} else {
				instances_sim[e_type_ind][lengths[e_type_ind]] = inst;
				invalidate_quick_find();
			}
			lengths[e_type_ind]++;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[e_type_ind] & SIM_LOADED_MASK) == SIM_LOADED_NONE) {

			sim_status[e_type_ind] =
				(short)((sim_status[e_type_ind] & ~SIM_LOADED_MASK) | SIM_LOADED_PARTIAL);
		}
	}


/**
 * Substitutes a given entity instance with a newly created one which is
 * written to the contents of this <code>SdaiModel</code>.
 * The given entity instance can belong to other model and even
 * to other repository. Moreover, the entity type of the given instance
 * and that of the new one may be different.
 * This new entity (represented by the corresponding java class EXxx.class
 * where "xxx" is entity name) must be
 * instantiable and  must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the second parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * or an entity was identified but is not instantiable,
 * then SdaiException ED_NDEF and, respectively, ED_NVLD is thrown.
 * At the end of the operation the given instance is deleted
 * from its owning model.
 * <p> If entity type supplied (through the second parameter)
 * for creation of new instance is the same as
 * entity type of the given one, then values of all attributes
 * are passed from the old instance to its substitute.
 * If entity types are different, then values are passed only for
 * common parts of these entities. The remaining attributes are left unset,
 * so the {@link EEntity#testAttribute testAttribute} method applied
 * to any of them will return value 0.
 * If the given instance belongs to the same repository as this model does,
 * then its substitute gets the persistent label (or name as specified in
 * ISO 10303-21::7.3.4) of it. Otherwise, new persistent label for
 * the substitute is chosen. The integer used to construct it
 * is taken larger than the number in the persistent label of any already
 * existing instance in the same repository.
 * All the references from other instances within models in at least
 * read-only mode to the given instance are replaced by the references
 * to its substitute.
 * <p> If null value is passed to either of the method's parameters,
 * then SdaiException VA_NSET is thrown.
 * Submitting an invalid instance leads to throwing SdaiException EI_NEXS.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param old given entity instance.
 * @param type Java class for the entity of which an instance
 * has to substitute the given one.
 * @return the created entity instance which substitutes the old one.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #substituteInstance(EEntity old, EEntity_definition entity_definition)
 * @see #substituteInstance(EEntity old)
 * @see "ISO 10303-21::7.3.4 Entity instance names"
 */
	public EEntity substituteInstance(EEntity old, Class type) throws SdaiException	{
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
//		synchronized (syncObject) {
		CEntity_definition edef = (CEntity_definition)checkEntity(type.getName());
		return substituteInstance(old, edef);
//		} // syncObject
	}


/**
 * Substitutes a given entity instance with a newly created one which is
 * written to the contents of this <code>SdaiModel</code>.
 * The given entity instance can belong to other model and even
 * to other repository. The entity type of the new instance is the same
 * as that of the given one.
 * This entity type must be instantiable and  must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if this entity is not known for this schema,
 * or is not instantiable, then SdaiException ED_NVLD is thrown.
 * At the end of the operation the given instance is deleted
 * from its owning model.
 * <p> The values of all attributes of the old instance
 * are passed to its substitute.
 * If the given instance belongs to the same repository as this model does,
 * then its substitute gets the persistent label (or name as specified in
 * ISO 10303-21::7.3.4) of it. Otherwise, new persistent label for
 * the substitute is chosen. The integer used to construct it
 * is taken larger than the number in the persistent label of any already
 * existing instance in the same repository.
 * All the references from other instances within models in at least
 * read-only mode to the given instance are replaced by the references
 * to its substitute.
 * <p> If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * Submitting an invalid instance leads to throwing SdaiException EI_NEXS.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param old given entity instance.
 * @return the created entity instance which substitutes the old one.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #substituteInstance(EEntity old, EEntity_definition entity_definition)
 * @see #substituteInstance(EEntity old, Class type)
 * @see "ISO 10303-21::7.3.4 Entity instance names"
 */
	public EEntity substituteInstance(EEntity old) throws SdaiException {
		if (old == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (((CEntity)old).owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (repository.session.undo_redo_file != null && repository.session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
//		synchronized (syncObject) {
		CEntity_definition def = (CEntity_definition)old.getInstanceType();
		CEntity older = (CEntity) old;
		SdaiModel older_owner = older.owning_model;
		if (older_owner == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
		if ((older_owner.mode & MODE_MODE_MASK) != READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, older_owner);
		}
		if (older_owner != this) {
			SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
			int index_to_entity = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, (CEntityDefinition)def);
			if (index_to_entity < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, def);
			}
			if (repository.session.undo_redo_file != null && !bypass) {
				repository.session.undoRedoSubstitutePrepare(older, older, modified);
			}
			older.deleteInstanceWeak();
			older.owning_model = this;
			StaticFields staticFields = StaticFields.get();
			if (staticFields.entity_values == null) {
				staticFields.entity_values = new ComplexEntityValue();
			}
			if (older_owner.outConnectors != null) {
				moveConnectors(staticFields.entity_values, def, older, older_owner);
			}
			if (repository == older_owner.repository) {
				insertEntityInstance(older, index_to_entity, false);
			} else {
				older.instance_identifier = repository.incPersistentLabel(this);
				insertEntityInstance(older, index_to_entity, true);
				older_owner.inst_deleted = true;
			}
			older.instance_position |= CEntity.INS_MASK;
			modified = true;
			if (older_owner.optimized) {
				older.setModifiedFlagNoInverse();
			} else {
				older.setModifiedFlag();
			}
		}
		return older;
//		} // syncObject
	}


	private void moveConnectors(ComplexEntityValue entity_values, CEntity_definition def,
			CEntity inst, SdaiModel old_model) throws SdaiException {
		prepareAll(entity_values, def);
		inst.getAll(entity_values);
		boolean moved = false;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			for (int j = 0; j < pval.count; j++) {
				boolean res = pval.values[j].move_connectors(inst, old_model);
				if (res) {
					moved = true;
				}
			}
		}
		if (moved) {
			inst.setAll(entity_values);
		}
	}

//This method is not used
///**
//*/
//	private void copyConnectors(ComplexEntityValue entity_values, CEntity_definition def,
//			CEntity inst) throws SdaiException {
//		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
//			EntityValue pval = entity_values.entityValues[i];
//			for (int j = 0; j < pval.count; j++) {
//				pval.values[j].look4connectors(inst);
//			}
//		}
//	}

/**
*/
	protected void saveRemoteModel(DataOutput stream, int version) throws SdaiException {
		if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
//		try {
			saveStreamRemote(stream, version);  //saveStream(stream);
//		} catch (IOException ex) {
//			throw new SdaiException(SdaiException.SY_ERR, ex);
//		}
	}

	protected void postSaveRemoteModel() {
		// Clean up deleted instance list

        //int inst_count = sortAllInstances();
        int inst_count = 0;
        for (int i = 0; i < lengths.length; i++) {
			if (lengths[i] > 0) {
				inst_count += lengths[i];
			}
		}
		if (inst_count > 0) {
			del_inst_threshold = (long)(0.5 * inst_count+1);
		} else {
			del_inst_threshold = -1;
		}
		del_inst_ids = EMPTY_DEL_INST_IDS; //new long[INSTANCE_ARRAY_INITIAL_SIZE];
		n_del_inst_ids = 0;

		// Clean inserted and updated instances flags

		for (int type_id = 0; type_id < lengths.length; type_id++) {
			for (int j = 0; j < lengths[type_id]; j++) {
				CEntity instance = instances_sim[type_id][j];
				instance.instance_position &= CEntity.POS_MASK;
			}
		}
	}

    protected void saveStreamCommon(DataOutput stream) throws SdaiException {
		int i, j;
		short count = 0;
		CEntity instance;

//int inst_count = sortAllInstances();
//for (i = 0; i < inst_count; i++) {
//System.out.println("    *****instance: #" + instances_all[i].instance_identifier +
//"   its type: " + instances_all[i].getInstanceType().getName(null));
//if (i == 0) continue;
//if (instances_all[i].instance_identifier <= instances_all[i - 1].instance_identifier)
//System.out.println("  SdaiModel  !!!!!!!  Error in instance ordering.  Instance: #" +
//instances_all[i].instance_identifier + "   its index: " + i);
//}System.out.println();
		if (to_entities == null) {
			to_entities = new int[instances_sim.length];
		}
        //<--VV-- Instance state tracking
        del_inst_ids = EMPTY_DEL_INST_IDS; //new long[INSTANCE_ARRAY_INITIAL_SIZE];
        n_del_inst_ids = 0;
        //--VV--> Instance state tracking
		int inst_count = sortAllInstances();
		try {
			stream.writeByte('D');
			stream.writeInt(Implementation.build);
			stream.writeLong(inst_count);
			StaticFields staticFields = StaticFields.get();
			for (i = 0; i < inst_count; i++) {
				stream.writeLong((long)staticFields.instances_all[i].instance_identifier);
                //<--VV-- Instance state tracking
                staticFields.instances_all[i].instance_position = staticFields.instances_all[i].instance_position & CEntity.POS_MASK;
                //--VV--> Instance state tracking
			}
			SdaiModel dict_mod = underlying_schema.modelDictionary;
			initializeInfoFromDict(dict_mod);
			prepareInfoFromDict(dict_mod);
			int def_types_count = takeDefTypes(dict_mod);
			stream.writeByte('S');
			stream.writeShort((short)def_types_count);
			for (i = 0; i < def_types_count; i++) {
				stream.writeUTF(type_names[i]);
			}
if (SdaiSession.debug2) {
System.out.println("  _____________ count of def types: " + def_types_count);
for (i = 0; i < def_types_count; i++) {
System.out.println("    *****index: " + i + "   DEFINED TYPE: " + type_names[i]);
}System.out.println();
}

			stream.writeByte('S');
			int count_of_types = takeSingleEntityTypes(dict_mod, stream);

			stream.writeByte('S');
			CEntityDefinition def;
			SchemaData schd = dict_mod.schemaData;
			count = 0;
			for (i = 0; i < instances_sim.length - 1; i++) {
				if (lengths[i] > 0) {
					count++;
				}
			}
			stream.writeShort(count);
if (SdaiSession.debug2) System.out.println("  _____________ count of populated entity data types: " + count);
			count = 0;
			String leaf;
			int index2name;
			if (to_leaves == null) {
				to_leaves = new int[LEAF_ARRAY_SIZE];
			}
			for (i = 0; i < instances_sim.length - 1; i++) {
				if (lengths[i] > 0) {
					def = schd.entities[i];
if (SdaiSession.debug2) System.out.println("  !!!!! entity: " + def.name +
"   noOfPartialEntityTypes: " + def.noOfPartialEntityTypes +
"   count of instances: " + lengths[i]);
					if (def.complex == 2) {
						String leaves = def.getNameUpperCase();
						int leaf_count = 0;
						int index = 0;
						int pos = 0;
						while (index >= 0) {
							index = leaves.indexOf('$', pos);
							if (index >= 0) {
								leaf = leaves.substring(pos, index);
								pos = index + 1;
							} else {
								leaf = leaves.substring(pos);
							}
							if (leaf_count >= to_leaves.length) {
								to_leaves = SdaiSession.ensureIntsCapacity(to_leaves);
							}
							index2name = repository.session.find_string(0, count_of_types - 1, leaf, simple_entity_names);
							if (index2name < 0) {
								throw new SdaiException(SdaiException.SY_ERR);
							}
if (SdaiSession.debug2) System.out.println(" Complex case   index: " + i + "  leaf: " + leaf +
"   index2name: " + index2name);
							to_leaves[leaf_count] = index2name;
							leaf_count++;
						}
						stream.writeShort((short)leaf_count);
						for (j = 0; j < leaf_count; j++) {
							stream.writeShort((short)to_leaves[j]);
						}
					} else {
						leaf = def.getNameUpperCase();
						index2name = repository.session.find_string(0, count_of_types - 1, leaf, simple_entity_names);
						if (index2name < 0) {
//for (int r = 0; r < count_of_types; r++) {
//System.out.println("   SdaiModel   simple_type: " + simple_entity_names[r]);
//}System.out.println();
							throw new SdaiException(SdaiException.SY_ERR);
						}
if (SdaiSession.debug2) System.out.println(" Simple case   index: " + i + "  leaf: " + leaf +
"   index2name: " + index2name);
						stream.writeShort(1);
						stream.writeShort((short)index2name);
					}
//					stream.writeInt(lengths[i]);
					stream.writeLong(lengths[i]);
					CEntity [] instances = instances_sim[i];
					for (j = 0; j < lengths[i]; j++) {
						instance = instances[j];
						stream.writeLong(instance.instance_identifier);
						//instance.instance_position = j;
                        instance.instance_position = (instance.instance_position & CEntity.FLG_MASK) | (j & CEntity.POS_MASK); //--VV--
					}
					to_entities[i] = count;
					count++;
				} else {
					to_entities[i] = -1;
				}
			}
			n_ex_models = 0;
			n_ex_repositories = 0;
			n_ex_edefs = 0;

            //<--VV--030616-- Writing aim_2_arm link data to stream
            String[] sModelNames = new String[NUMBER_OF_EXTERNAL_MODS];
            String[] sRepoNames = new String[NUMBER_OF_EXTERNAL_MODS];
			int iModelNamesCnt = 0;
            //--VV--030616--> Writing aim_2_arm link data to stream

			for (i = 0; i < inst_count; i++) {
				instance = staticFields.instances_all[i];
				def = (CEntityDefinition)instance.getInstanceType();
				int to_type = schd.findEntityExtentIndex(def);
				if (to_type < 0) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
//				stream.writeShort(to_entities[to_type]);
//String md_name = instance.owning_model.name;
//boolean bool = false;
//if (md_name.equals("bin_mod")) bool = true;
//if (bool) System.out.println("SdaiModel:  Before prepareAll  def: " + def);System.out.flush();
				prepareAll(entity_values, (CEntity_definition)def);
				instance.getAll(entity_values);
				save_instance(instance, entity_values, stream, def_types_count, to_entities[to_type]);

                //<--VV--030613-- Writing aim_2_arm link data to stream
                if (!repository.temporary) {
                    CMappedARMEntity armInstance = CMappedARMEntity.getFirstArmInstance((EEntity)instance);
                    while (armInstance != null) {

                        SdaiModel model = (SdaiModel)armInstance.getOwner();
                        if (model != null) {
                            String sModelName = model.getName();
                            SdaiRepository repo = model.getRepository();
                            String sRepoName = repo.getName();

                            int iModelNamesIdx = -1;
                            for (int k = 0; k < iModelNamesCnt; k++) {
                                if (sModelNames[k].equals(sModelName) && sRepoNames[k].equals(sRepoName)) {
                                    iModelNamesIdx = k;
                                    break;
                                }
                            }
                            if (iModelNamesIdx == -1) {
                                if (sRepoName.equalsIgnoreCase(repository.getName())) {
                                    sRepoName = "";
                                }

                                if (iModelNamesCnt >= sModelNames.length) {
                                    sModelNames = repository.session.ensureStringsCapacity(sModelNames);
                                    sRepoNames = repository.session.ensureStringsCapacity(sRepoNames);
                                }
                                sModelNames[iModelNamesCnt] = sModelName;
                                sRepoNames[iModelNamesCnt] = sRepoName;
                                iModelNamesCnt++;
                            }

                            if (iModelNamesIdx == -1) {
                                stream.writeByte('l');
                                stream.writeUTF( sRepoName);
                                stream.writeUTF( sModelName);
                            }
                            else {
                                stream.writeByte('k');
                                stream.writeShort( iModelNamesIdx);
                            }
                            stream.writeLong( Long.parseLong( armInstance.getPersistentLabel().substring(1)));
                        }
                        armInstance = armInstance.nextArm;
                    }
                }
                //--VV--030613--> Writing aim_2_arm link data to stream
			}
			stream.writeByte('E');
			file_saved = true;
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		} finally {
            type_names = null;
            simple_entity_names = null;
            to_leaves = null;
        }
	}


/**
	Writes the data contained in this SdaiModel to the binary stream.
*/
    protected void saveStreamRemote(DataOutput stream, int version) throws SdaiException {
		CEntity instance;


        try {

            //System.out.println("del_inst_threshold="+del_inst_threshold);
            //System.out.println("n_del_inst_ids="+n_del_inst_ids);

            CEntity_definition def;
            SdaiModel dict_mod = underlying_schema.modelDictionary;
            initializeInfoFromDict(dict_mod);
			prepareInfoFromDict(dict_mod);
            //int def_types_count = takeDefTypes(dict_mod);

            // DELETE part of stream
            if (del_inst_threshold != 0) {
                if (del_inst_threshold > 0) {
                    for (int i = 0; i < n_del_inst_ids; i++) {
                        stream.writeLong(del_inst_ids[i]);
                        //System.out.println(del_inst_ids[i]);
                    }
                }
                stream.writeLong(-1);
                //System.out.println("-1");
            }
            else {
                for (int type_id = 0; type_id < lengths.length; type_id++) {
                    for (int j = 0; j < lengths[type_id]; j++) {
                        instance = instances_sim[type_id][j];
                        if ((instance.instance_position & CEntity.FLG_MASK) != CEntity.INS_MASK) {
                            stream.writeLong(instance.instance_identifier);
                            //System.out.println(instance);
                        }
                    }
                }
                stream.writeLong(-2);
                //System.out.println("-2");
            }

            // INSERT part of stream
			for (int type_id = 0; type_id < lengths.length; type_id++) {
                for (int j = 0; j < lengths[type_id]; j++) {
                    instance = instances_sim[type_id][j];
                    if ((instance.instance_position & CEntity.INS_MASK) != 0) {
                        def = (CEntity_definition)instance.getInstanceType();

                        stream.writeLong(instance.instance_identifier);
                        stream.writeInt(type_id);

                        prepareAll(entity_values, (CEntity_definition)def);
                        instance.getAll(entity_values);
                        save_instance_remote(instance, entity_values, stream, version);

                        //System.out.println(instance);
                    }
                }
            }
            stream.writeLong(-1);
            //System.out.println("-1");

            // UPDATE part of stream
            for (int type_id = 0; type_id < lengths.length; type_id++) {
                for (int j = 0; j < lengths[type_id]; j++) {
                    instance = instances_sim[type_id][j];
                    if ((instance.instance_position & CEntity.FLG_MASK) == CEntity.UPD_MASK) {
                        def = (CEntity_definition)instance.getInstanceType();

                        stream.writeLong(instance.instance_identifier);
                        stream.writeInt(type_id);

                        prepareAll(entity_values, (CEntity_definition)def);
                        instance.getAll(entity_values);
                        save_instance_remote(instance, entity_values, stream, version);
                        //System.out.println(instance);
                    }
                }
            }

            stream.writeLong(-1);
            //System.out.println("-1");

		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}

        //System.out.println("sSR t="+(float)(lCT-lST)/1000);
	}

/**
	Initializes some fields for entities and defined types to know which
	of these objects are used to construct population contained in
	this SdaiModel.
*/
	private void initializeInfoFromDict(SdaiModel dict_mod) throws SdaiException {
		int i;
		SchemaData schd = dict_mod.schemaData;
		for (i = 0; i < schd.noOfEntityDataTypes; i++) {
			schd.entities[i].used = false;
		}
		for (i = 0; i < schd.defTypesCount; i++) {
			schd.def_types[i].used = false;
		}
	}


/**
	Prepares an information on entity data types and defined types to be
	written into binary file.
*/
	private void prepareInfoFromDict(SdaiModel dict_mod) throws SdaiException {
		int j;
		SchemaData schd = dict_mod.schemaData;
		for (int i = 0; i < instances_sim.length - 1; i++) {
			if (lengths[i] <= 0) {
				continue;
			}
			CEntityDefinition def = schd.entities[i];
			CExplicit_attribute attr;
			if (def.complex == 2) {
if (SdaiSession.debug2) System.out.println(" In prepareInfoFromDict  entity: " + def.name);
				int count = 0;
				int new_count;
				for (j = 0; j < def.noOfPartialEntityTypes; j++) {
					CEntityDefinition partial_def = def.partialEntityTypes[j];
					new_count = count + partial_def.noOfPartialAttributes;
					if (!partial_def.used) {
						partial_def.used = true;
						partial_def.toComplex = i;
						partial_def.attr_start_index = count;
					}
if (SdaiSession.debug2) System.out.println(" In prepareInfoFromDict   partial_def.noOfPartialAttributes: " +
partial_def.noOfPartialAttributes);
					for (int k = count; k < new_count; k++) {
						attr = def.attributes[k];
if (SdaiSession.debug2) System.out.println(" In prepareInfoFromDict  attr: " + attr.getName(null));
						if (def_type_chain == null) {
							def_type_chain = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
						}
						def_type_count = 0;
						exploreBaseType((DataType)attr.getDomain(null));
					}
					count = new_count;
				}
			} else {
if (SdaiSession.debug2) System.out.println(" In prepareInfoFromDict  ^^^^^^^ entity: " +
def.name + "   def.noOfPartialEntityTypes: " + def.noOfPartialEntityTypes +
"   def: " + def +
"  owning_model: " + def.owning_model.name);
				for (j = 0; j < def.noOfPartialEntityTypes; j++) {
					CEntityDefinition partial_def = def.partialEntityTypes[j];
					partial_def.used = true;
//partial_def.toComplex = i;
					if (partial_def.attributes != null) {
						int attr_ln = partial_def.attributes.length;
						for (int k = attr_ln - partial_def.noOfPartialAttributes; k < attr_ln; k++) {
//if (k < 0) {
//System.out.println(" In prepareInfoFromDict  ^^^^^^^ entity: " + def.getName(null));
//}
							attr = partial_def.attributes[k];
if (SdaiSession.debug2) System.out.println(" In prepareInfoFromDict  **********  attr: " + attr.getName(null));
							if (def_type_chain == null) {
								def_type_chain = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
							}
							def_type_count = 0;
							exploreBaseType((DataType)attr.getDomain(null));
						}
					}
				}
			}
		}
	}


/**
	Prepares a list of defined types encountered in the definitions
	of entity data types which are populated in this SdaiModel.
*/
	private int takeDefTypes(SdaiModel dict_mod) throws SdaiException {
		int i;
		int count = 0;

		SchemaData schd = dict_mod.schemaData;
		if (schd.defTypesCount < 0) {
			schd.initializeDefinedTypes();
		}
		for (i = 0; i < schd.defTypesCount; i++) {
			if (schd.def_types[i].used) {
				count++;
			}
		}
		if (type_names == null) {
			if (count > TYPES_ARRAY_SIZE) {
				type_names = new String[count];
			} else {
				type_names = new String[TYPES_ARRAY_SIZE];
			}
		} else if (count > type_names.length) {
			int new_length = type_names.length * 2;
			if (count > new_length) {
				new_length = count;
			}
			type_names = new String[new_length];
		}
		for (i = 0, count = 0; i < schd.defTypesCount; i++) {
			if (schd.def_types[i].used) {
				type_names[count++] = schd.dtNames[i];
			}
		}
		return count;
	}


/**
	Prepares a list of single entity data types encountered in entities
	populated in this SdaiModel. For each member in this list its definition
	information (attributes, supertypes) is written to the binary file.
*/
	private int takeSingleEntityTypes(SdaiModel dict_mod, DataOutput stream) throws SdaiException {
		int i, j;
		int count = 0;
		int single_entity_count;
		int attr_ln;
		CEntityDefinition def, supertype;
		CExplicit_attribute attr;
		SchemaData schd = dict_mod.schemaData;

        if (simple_entity_names == null) {
            simple_entity_names = new String[ENTITIES_ARRAY_SIZE];
        }
		for (i = 0; i < schd.noOfEntityDataTypes; i++) {
			def = schd.entities[i];
			if (def.used) {
				if (count >= simple_entity_names.length) {
					simple_entity_names = repository.session.ensureStringsCapacity(simple_entity_names);
				}
				simple_entity_names[count] = schd.sNames[i];
if (SdaiSession.debug2) System.out.println("*******  index: " + count + "   single entity: " + schd.sNames[i]);
				count++;
			}
		}
		single_entity_count = count;
		try {
			stream.writeShort((short)count);
			count = 0;
			for (i = 0; i < schd.noOfEntityDataTypes; i++) {
				def = schd.entities[i];
				if (!def.used) {
					continue;
				}
//String nm = def.getName(null);
//String st=null, st2=null, st3=null;
				stream.writeUTF(schd.sNames[i]);
				count++;
//if (name.equals("mod_99") && nm.equals("conical_surface")) stream.writeShort((short)def.noOfPartialAttributes);
//else
				stream.writeShort((short)def.noOfPartialAttributes);
				if (def.attributes == null) {
if (SdaiSession.debug2) System.out.println("   NULL CASE  single entity data type: " + schd.sNames[i] +
"      noOfPartialAttributes: " + def.noOfPartialAttributes);
					supertype = schd.entities[def.toComplex];
					attr_ln = def.attr_start_index + def.noOfPartialAttributes;
					for (j = def.attr_start_index; j < attr_ln; j++) {
						attr = supertype.attributes[j];
//if (name.equals("mod_99") && nm.equals("conical_surface") && j == def.attr_start_index) {st = attr.getName(null);continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve") && j == def.attr_start_index + 1) {stream.writeUTF("replaced");continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve") && j == def.attr_start_index + 3) {st2 = attr.getName(null);continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve_with_knots") && j == def.attr_start_index) {st3 = attr.getName(null);continue;}
						stream.writeUTF(attr.getName(null));
//if (name.equals("mod_99") && nm.equals("b_spline_curve_with_knots") && j == def.attr_start_index + 1) stream.writeUTF(st3);
					}
//if (name.equals("mod_99") && nm.equals("conical_surface")) stream.writeUTF(st);
//if (name.equals("mod_99") && nm.equals("b_spline_curve")) stream.writeUTF(st2);
				} else {
if (SdaiSession.debug2) System.out.println("   POS CASE  single entity data type: " + schd.sNames[i] +
"      noOfPartialAttributes: " + def.noOfPartialAttributes +
 "   def.toComplex: " + schd.entities[def.toComplex].name);
					attr_ln = def.attributes.length;
					for (j = attr_ln - def.noOfPartialAttributes; j < attr_ln; j++) {
						attr = def.attributes[j];
//if (name.equals("mod_99") && nm.equals("conical_surface") && j == attr_ln - def.noOfPartialAttributes) {st = attr.getName(null);continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve") && j == attr_ln - def.noOfPartialAttributes + 1) {stream.writeUTF("replaced");continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve") && j == attr_ln - def.noOfPartialAttributes + 3) {st2 = attr.getName(null);continue;}
//if (name.equals("mod_99") && nm.equals("b_spline_curve_with_knots") && j == attr_ln - def.noOfPartialAttributes) {st3 = attr.getName(null);continue;}
//if (name.equals("mod_99")) System.out.println("SdaiModel  simple entity: " + nm + "  stored attr: " + attr.getName(null));
						stream.writeUTF(attr.getName(null));
//if (name.equals("mod_99") && nm.equals("b_spline_curve_with_knots") && j == attr_ln - def.noOfPartialAttributes + 1) stream.writeUTF(st3);
if (SdaiSession.debug2) System.out.println("        attribute: " + attr.getName(null));
					}
//if (name.equals("mod_99") && nm.equals("conical_surface")) stream.writeUTF(st);
////if (name.equals("mod_99") && nm.equals("conical_surface")) stream.writeUTF("dummy");
//if (name.equals("mod_99") && nm.equals("b_spline_curve")) stream.writeUTF(st2);
				}
				AEntity_definition supertypes = ((CEntity_definition)def).getSupertypes(null);
				stream.writeShort((short)((AEntity)supertypes).myLength);
				for (j = 1; j <= ((AEntity)supertypes).myLength; j++) {
					supertype = (CEntityDefinition)supertypes.getByIndex(j);
					String name = supertype.getNameUpperCase();
					int index2name = repository.session.find_string(0, single_entity_count - 1, name, simple_entity_names);
if (SdaiSession.debug2) System.out.println("    index: " + j + "  supertype: " + name +
"   index2name: " + index2name);
					if (index2name < 0) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					stream.writeShort((short)index2name);
				}
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return single_entity_count;
	}


/**
	Analyses the base type of attributes of populated entity data types
	in order to find involved defined types.
*/
	private void exploreBaseType(DataType base) throws SdaiException {
		if (base.express_type == DataType.DEFINED_TYPE) {
			CDefined_type def_type = (CDefined_type)base;
			if (!checkDefinedType(def_type)) {
				exploreDefinedType(null, def_type);
				def_type_count--;
			}
		} else if (base.express_type >= DataType.LIST && base.express_type <= DataType.AGGREGATE) {
			exploreBaseType((DataType)((EAggregation_type)base).getElement_type(null));
		}
		return;
	}


/**
	Checks whether the submitted defined type is already included
	into an array of defined types.
*/
	private boolean checkDefinedType(CDefined_type def_type) throws SdaiException {
		boolean explored = false;
		for (int i = 0; i < def_type_count; i++) {
			if (def_type_chain[i] == def_type) {
				explored = true;
//System.out.println("  SdaiModel &&&&&  in checkDefinedType  explored found  def_type: " + def_type.getName(null));
				break;
			}
		}
		if (explored) {
			return true;
		}
		if (def_type_count >= def_type_chain.length) {
			enlargeChain();
		}
		def_type_chain[def_type_count] = def_type;
		def_type_count++;
		return false;
	}


/**
	Analyses the specified defined type having a purpose to register it as used
	in the definitions of entity data types which are populated in this SdaiModel.
*/
	private void exploreDefinedType(CDefined_type head, CDefined_type tail) throws SdaiException {
		DataType domain = (DataType)tail.getDomain(null);
		CDefined_type def_type;
		if ( (domain.express_type >= DataType.NUMBER && domain.express_type <= DataType.BINARY) ||
				(domain.express_type >= DataType.ENUMERATION && domain.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM)) {
			if (head != null) {
				head.used = true;
			}
		} else if (domain.express_type >= DataType.LIST && domain.express_type <= DataType.AGGREGATE) {
			if (head != null) {
				head.used = true;
			}
			exploreBaseType((DataType)((EAggregation_type)domain).getElement_type(null));
		} else if (domain.express_type == DataType.DEFINED_TYPE) {
			def_type = (CDefined_type)domain;
			if (!checkDefinedType(def_type)) {
				exploreDefinedType(head, def_type);
				def_type_count--;
			}
		} else if (domain.express_type >= DataType.SELECT && domain.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (head != null && head != tail) {
				head.used = true;
			}
			AEntity sels;
			if (domain.express_type >= DataType.EXTENSIBLE_SELECT) {
				StaticFields staticFields = null;
				SdaiSession ss = repository.session;
				if (ss.sdai_context == null) {
					staticFields = StaticFields.get();
					staticFields.context_schema = underlying_schema;
					if (!ss.sdai_context_missing) {
						ss.sdai_context_missing = true;
						ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
					}
				}
				sels = ((EExtensible_select_type)domain).getSelections(null, ss.sdai_context);
				if (ss.sdai_context == null) {
					staticFields.context_schema = null;
				}
			} else {
				sels = ((ESelect_type)domain).getSelections(null);
			}
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			Object [] myDataA = null;
			if (sels.myLength > 1) {
				myDataA = (Object [])sels.myData;
			}
			for (int i = 0; i < sels.myLength; i++) {
				DataType alternative;
				if (sels.myLength == 1) {
					alternative = (DataType)sels.myData;
				} else {
					alternative = (DataType)myDataA[i];
				}
				if (alternative.express_type == DataType.DEFINED_TYPE) {
					def_type = (CDefined_type)alternative;
					if (!checkDefinedType(def_type)) {
						exploreDefinedType(def_type, def_type);
						def_type_count--;
					}
				}
			}
		}
		return;
	}

	protected long loadRemoteModel(DataInput stream, int mode_to_start, boolean add_to_active) throws SdaiException {
		CEntity[][] inst_ces = loadRemoteInstanceHeader(stream, mode_to_start, add_to_active);
		long max_ident = loadRemoteInstanceBody(stream, inst_ces);
		inst_ces = null;
		return max_ident;
	}

    /**
    */
	protected CEntity[][] loadRemoteInstanceHeader(DataInput stream, int mode_to_start, boolean add_to_active) throws SdaiException {
		if (stream == null) {
			prepareInitialContens(mode_to_start);
			setMode(mode_to_start);
			repository.session.active_models.addUnorderedRO(this);
			modified = false;
			return null;
		}
        if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			ex_models = new SdaiModel[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}

        CEntity [][] inst_ces = null;
		try {
			setMode(READ_WRITE);

			int complex_entity_count;
			long inst_count;

		    AEntity_definition set;

		    if (underlying_schema == null) {
		        underlying_schema = dictionary.described_schema;
		    }
		    set = underlying_schema.getEntities();
			complex_entity_count = ((AEntity)set).myLength;

		    inst_count = stream.readLong();
		    if (inst_count > 0) {
		        del_inst_threshold = (long)(0.5 * inst_count+1);
		    }
		    else {
		        del_inst_threshold = -1;
		    }
		    del_inst_ids = EMPTY_DEL_INST_IDS; //new long[INSTANCE_ARRAY_INITIAL_SIZE];
		    n_del_inst_ids = 0;

		    if(mode_to_start != READ_ONLY) {
		    	entities = null;
				if (instances_sim == null) {
					prepareInstancesSim(complex_entity_count + 1, true);
				}
		    }

		    // Auxiliary arrays to have references to instances using instance identifiers
			inst_ces =
				new CEntity[((int)inst_count + INST_PORTION_SIZE - 1) / INST_PORTION_SIZE][];
		    int [] inst_tps = new int[(int)inst_count];
		    int [] type_len = new int[complex_entity_count];
		    int populated_types = 0;

		    long inst_id = -1;
		    int cnt = 0;

		    // Reads instance identifier and type identifier for instances from stream
			Object [] myDataA = null;
			if (((AEntity)set).myLength > 1) {
				myDataA = (Object [])((AEntity)set).myData;
			}
			boolean tryToFindInstance = mode_to_start != READ_ONLY || entities != null;
			for(int i = 0; i < inst_ces.length; i++) {
				inst_ces[i] = new CEntity[(int)inst_count - cnt >= INST_PORTION_SIZE ?
										  (INST_PORTION_SIZE) : (int)inst_count - cnt];
				for (int j = 0; j < INST_PORTION_SIZE; j++) {
					inst_id = stream.readLong();
					int type_id = stream.readInt();
					//ps.println("#"+inst_id+"\t"+type_id);
		            CEntity instance;
					int index;
					int typeIndex;
					if(tryToFindInstance) {
						typeIndex = typeIdToIndex(type_id, mode_to_start);
						index = typeIndex >= 0 ? find_instance(0, lengths[typeIndex] - 1, typeIndex, inst_id) : -1;
					} else {
						index = -1;
						typeIndex = 0;
					}
					if (index >= 0) {
						instance = instances_sim[typeIndex][index];
						instance.instance_identifier = -instance.instance_identifier;
					} else {
						CEntityDefinition edef;
						if (myDataA == null) {
							edef = (CEntityDefinition)((AEntity)set).myData;
						} else {
							edef = (CEntityDefinition)myDataA[type_id];
						}
						SdaiModel m;
						if (this == SdaiSession.baseDictionaryModel) {
							m = this;
						} else {
							m = ((CSchema_definition) edef.owning_model.described_schema).modelDictionary;
						}
						SSuper sup = m.schemaData.super_inst;
						Class entityClass = edef.getEntityClass();

						// Creates instance using complex entity type identifier and instance identifier
						instance = sup.makeInstance(entityClass, this, -1, 0);

						instance.instance_identifier = inst_id;
					}

					// Saves instance reference for access using instance identifier
					inst_tps[cnt] = type_id;
					inst_ces[i][j] = instance;
					if(type_len[type_id] == 0) {
						populated_types++;
					}
					type_len[type_id]++;
					repository.checkPersistentLabelRange(inst_id);
					if(++cnt == inst_count) {
						break;
					}
				}
			}

		    if(mode_to_start == READ_ONLY && entities == null) {
		    	entities = new CEntity_definition[populated_types];
				prepareInstancesSim(populated_types + 1, false);
				instances_sim[populated_types] = emptyArray;
				lengths[populated_types] = 0;
		    }

			deleteInstances(true);

		    if(mode_to_start == READ_ONLY) {
			    if(entities.length < populated_types) {
			    	entities = new CEntity_definition[populated_types];
					prepareInstancesSim(populated_types + 1, false);
					instances_sim[populated_types] = emptyArray;
					lengths[populated_types] = 0;
			    }
		        cnt = 0;
		    	for (int i = 0; i < complex_entity_count; i++) {
		    		if(type_len[i] > 0) {
		    			CEntityDefinition edef;
						if (myDataA == null) {
							edef = (CEntityDefinition)((AEntity)set).myData;
						} else {
							edef = (CEntityDefinition)myDataA[i];
						}
						entities[cnt] = edef;
		    			instances_sim[cnt] = new CEntity[type_len[i]];
		    			sim_status[cnt] |= SIM_SORTED;
		    			type_len[i] = cnt;
		    			cnt++;
		    		}
		    	}
		        cnt = 0;
				for(int i = 0; i < inst_ces.length; i++) {
					for (int j = 0; j < INST_PORTION_SIZE; j++) {
						int idx = type_len[inst_tps[cnt]];
						instances_sim[idx][lengths[idx]] = inst_ces[i][j];
						lengths[idx]++;
						if(++cnt == inst_count) {
							break;
						}
					}
				}
		    } else {
		    	for (int i = 0; i < complex_entity_count; i++) {
		    		if(type_len[i] > 0) {
		    			instances_sim[i] = new CEntity[type_len[i]];
		    			//sorted[i] = true;
		    			sim_status[i] |= SIM_SORTED;
		    		}
		    	}
		        cnt = 0;
				for(int i = 0; i < inst_ces.length; i++) {
					for (int j = 0; j < INST_PORTION_SIZE; j++) {
						instances_sim[inst_tps[cnt]][lengths[inst_tps[cnt]]] = inst_ces[i][j];
						lengths[inst_tps[cnt]]++;
						if(++cnt == inst_count) {
							break;
						}
					}
				}
				type_len = null;
		    }
			inst_tps = null;
			invalidate_quick_find();

		    n_ex_models = 0;
			n_ex_repositories = 0;
			n_ex_edefs = 0;

		} catch (IOException ex) {
			throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, ex)).initCause(ex);
		} finally {
            setMode(mode_to_start);
		}

		resolveInConnectors(false);
		if (add_to_active) {
			repository.session.active_models.addUnorderedRO(this);
		}
		return inst_ces;
	}

	protected long loadRemoteInstanceBody(DataInput stream, CEntity [][] inst_ces) throws SdaiException {
		try {
			long largest_identifier = -1;
			if(inst_ces != null) {
			    // Reads attribute values for instances from stream
				StreamUTF streamUtf = new StreamUTF();
			    for(int i = 0; i < inst_ces.length; i++) {
					for (int j = 0; j < inst_ces[i].length; j++) {
						stream.readInt();

						CEntity instance = inst_ces[i][j];
						instance.owning_model = this;
						load_instance_remote(entity_values, stream, instance,
											 (CEntity_definition)instance.getInstanceType(), streamUtf, inst_ces, false);
						instance.setAll(entity_values);
						if(instance.instance_identifier > largest_identifier) {
							largest_identifier = instance.instance_identifier;
						}
					}
//					inst_ces[i] = null;
				}
			}
			modified = false;
			return largest_identifier;
		} catch (IOException ex) {
			throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, ex)).initCause(ex);
		}
	}

    protected long loadStreamCommon(DataInput stream, int mode_to_start) throws SdaiException {
		long res;
		try {
			byte begin = stream.readByte();
			if (begin != 'D') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			underlying_build = stream.readInt();
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (underlying_build >= 270) {
			res = loadStream3(stream, mode_to_start);
		} else if (underlying_build >= 220) {
			res = loadStream2(stream);
		} else {
			res = loadStream1(stream);
		}
		return res;
	}

    protected void provideInstancesForTypes(int[] ceTypes, boolean delete) throws SdaiException {
    }

    protected void provideInstancesForType(long instPl) throws SdaiException {
	}

    final protected int getComplexEntitiesCount() throws SdaiException {
        return ((AEntity)underlying_schema.getEntities()).myLength;
    }

	final void provideAllInstancesIfNeeded() throws SdaiException {
		 if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			int typeLen = sim_status.length - 1;
			int ceTypesMaxLength = typeLen >> 2;
			int [] ceTypes = new int[ceTypesMaxLength];
			int ceTypesRealLength = 0;
			int simIdx = 0;
			for(int i = 0; i < getComplexEntitiesCount(); i++) {
				if(getSimType(simIdx) == getSchemaType(i)) {
					if((sim_status[simIdx] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
						if(ceTypesRealLength == ceTypesMaxLength) {
							ceTypesMaxLength = 0;
							break;
						}
						ceTypes[ceTypesRealLength++] = i;
					}
					simIdx++;
					if(simIdx >= typeLen) {
						break;
					}
				}
			}
			if(ceTypesRealLength > 0) {
				if(ceTypesMaxLength == 0) {
					ceTypes = null;
				} else if(ceTypesRealLength < ceTypes.length) {
					int [] ceTypesCopy = new int[ceTypesRealLength];
					System.arraycopy(ceTypes, 0, ceTypesCopy, 0, ceTypesRealLength);
					ceTypes = ceTypesCopy;
				}
				provideInstancesForTypes(ceTypes, false);
			}
		}
	}

	final void provideInstancesForTypeIfNeeded(int index) throws SdaiException {
		if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			int ceTypesMaxLength = 1;
			int [] subtypes = underlying_schema.getSubtypes(index);
			if (subtypes != null) {
				ceTypesMaxLength += subtypes.length;
			}
			int [] ceTypes = new int[ceTypesMaxLength];
			int ceTypesRealLength = 0;
			int index_true;
			if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
 				index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
		 	} else {
 				index_true = index;
		 	}
			if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
				ceTypes[ceTypesRealLength++] = index;
			}
			if (subtypes != null) {
				for (int i = 0; i < subtypes.length; i++) {
					if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
						index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[i]]);
 					} else {
						index_true = subtypes[i];
 					}
					if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
						ceTypes[ceTypesRealLength++] = subtypes[i];
					}
				}
			}
			if (ceTypesRealLength > 0) {
				if(ceTypesRealLength < ceTypes.length) {
					int [] ceTypesCopy = new int[ceTypesRealLength];
					System.arraycopy(ceTypes, 0, ceTypesCopy, 0, ceTypesRealLength);
					ceTypes = ceTypesCopy;
				}
				provideInstancesForTypes(ceTypes, false);
			}
		}
	}

	final void provideInstancesForExactTypeIfNeeded(int index) throws SdaiException {
		if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			int index_true;
			if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
 				index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
		 	} else {
 				index_true = index;
		 	}
			if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
				provideInstancesForTypes(new int[] { index }, false);
			}
		}
	}

    final Map provideInstancesForUsersIfNeeded(EEntity_definition defintion, Map userAttribMap,
											   boolean putToMap) throws SdaiException {
        if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			Set userTypeSet = null;
			if(userAttribMap == null
			   || (userTypeSet = (Set)userAttribMap.get(underlying_schema)) == null) {

				String schemaSchemaInstName = underlying_schema.modelDictionary.name
					.substring(0, underlying_schema.modelDictionary.name.length() -
							   SdaiSession.ENDING_FOR_DICT).toLowerCase();
				SchemaInstance schemaSchemaInst = underlying_schema.modelDictionary.repository
					.findSchemaInstance(schemaSchemaInstName);
				ASdaiModel schemaModels = schemaSchemaInst.getAssociatedModels();
				AExplicit_attribute userAttributes = new AExplicit_attribute();
				CExplicit_attribute.usedinDomain(null, defintion, schemaModels, userAttributes);
				AEntity miscUsers = new AEntity();
				CDefined_type.usedinDomain(null, defintion, schemaModels, miscUsers);
				collectMiscUsers(miscUsers, schemaModels, userAttributes, true);
				miscUsers.clear();
				CAggregation_type.usedinElement_type(null, defintion, schemaModels, miscUsers);
				collectMiscUsers(miscUsers, schemaModels, userAttributes, true);
				miscUsers.clear();
				CSelect_type.usedinLocal_selections(null, defintion, schemaModels, miscUsers);
				collectMiscUsers(miscUsers, schemaModels, userAttributes, false);
				collectSupertypeUsers(defintion, schemaModels, userAttributes);
				userTypeSet = new HashSet();
				SchemaData schemaData = underlying_schema.owning_model.schemaData;
				for(SdaiIterator i = userAttributes.createIterator(); i.next(); ) {
					EExplicit_attribute userAttribute = userAttributes.getCurrentMember(i);
					CEntityDefinition userEntity =
						(CEntityDefinition)userAttribute.getParent_entity(null);
					int userEntityIdx = schemaData.findEntityExtentIndex(userEntity);
					if(userEntityIdx >= 0) {
						userTypeSet.add(new Integer(userEntityIdx));
						int [] subtypeIdxs = underlying_schema.getSubtypes(userEntityIdx);
						for (int j = 0; j < subtypeIdxs.length; j++) {
							Integer subTypeIdx = new Integer(subtypeIdxs[j]);
							userTypeSet.add(subTypeIdx);
						}
					}
				}
				if(putToMap) {
					if(userAttribMap == null) {
						userAttribMap = new HashMap();
					}
					userAttribMap.put(underlying_schema, userTypeSet);
				}
			}

            if(!userTypeSet.isEmpty()) {
				int ceLength = 0;
				int index_true;
				for(Iterator i = userTypeSet.iterator(); i.hasNext(); ) {
					Integer ceIndex = (Integer)i.next();
					if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
		 				index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[ceIndex.intValue()]);
				 	} else {
 						index_true = ceIndex.intValue();
				 	}
					if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
						ceLength++;
					}
				}

				if(ceLength > 0) {
					int [] ceTypes = new int[ceLength];
					ceLength = 0;
					for(Iterator i = userTypeSet.iterator(); i.hasNext(); ) {
						Integer ceIndex = (Integer)i.next();
						if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
		 					index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[ceIndex.intValue()]);
					 	} else {
 							index_true = ceIndex.intValue();
					 	}
						if(index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK)
						   != SIM_LOADED_COMPLETE) {

							ceTypes[ceLength++] = ceIndex.intValue();
						}
					}
					userTypeSet = null;
					provideInstancesForTypes(ceTypes, false);
				}
			}
        }
		return userAttribMap;
	}

	private static void collectSupertypeUsers(EEntity_definition defintion, ASdaiModel schemaModels,
											  AExplicit_attribute userAttributes) throws SdaiException {
		AEntity_definition supertypes = defintion.getSupertypes(null);
		if(supertypes.getMemberCount() > 0) {
			collectMiscUsers(supertypes, schemaModels, userAttributes, true);
			for(SdaiIterator i = supertypes.createIterator(); i.next(); ) {
				EEntity_definition supertype = supertypes.getCurrentMember(i);
				collectSupertypeUsers(supertype, schemaModels, userAttributes);
			}
		}
	}

	private static void collectMiscUsers(AEntity miscUsers, ASdaiModel schemaModels,
										 AExplicit_attribute userAttributes,
										 boolean testAggrsAndSelects) throws SdaiException {
		if(miscUsers.getMemberCount() > 0) {
			AEntity miscChildUsers = new AEntity();
			for(SdaiIterator i = miscUsers.createIterator(); i.next(); ) {
				EData_type miscUser = (EData_type)miscUsers.getCurrentMemberEntity(i);
				CExplicit_attribute.usedinDomain(null, miscUser, schemaModels, userAttributes);
				miscChildUsers.clear();
				CDefined_type.usedinDomain(null, miscUser, schemaModels, miscChildUsers);
				collectMiscUsers(miscChildUsers, schemaModels, userAttributes, true);
				if(testAggrsAndSelects) {
					miscChildUsers.clear();
					CAggregation_type.usedinElement_type(null, miscUser, schemaModels, miscChildUsers);
					collectMiscUsers(miscChildUsers, schemaModels, userAttributes, true);
					if(miscUser instanceof ENamed_type) {
						miscChildUsers.clear();
						CSelect_type.usedinLocal_selections(null, (ENamed_type)miscUser,
															schemaModels, miscChildUsers);
						collectMiscUsers(miscChildUsers, schemaModels, userAttributes, false);
					}
				}
			}
		}
	}

/**
	Loads the data contained in the binary stream to specified instances_sim rows.
*/
    protected void loadRemoteInstancesForTypes(DataInput stream) throws SdaiException {
		int complex_entity_count;
		long inst_count;
		boolean oldModified = modified;
		StreamUTF streamUtf = new StreamUTF();

        try {

            if (underlying_schema == null) {
                underlying_schema = dictionary.described_schema;
            }
            AEntity_definition set = underlying_schema.getEntities();
			complex_entity_count = ((AEntity)set).myLength;


            inst_count = stream.readLong();
            if (inst_count > 0) {
                if (del_inst_threshold < 0) del_inst_threshold = 0;
                del_inst_threshold += inst_count;
// 				del_inst_ids = new long[INSTANCE_ARRAY_INITIAL_SIZE];???
// 				n_del_inst_ids = 0;???

				// Auxiliary arrays to have references to instances using instance identifiers
				CEntity [][] inst_ces =
					new CEntity[((int)inst_count + INST_PORTION_SIZE - 1) / INST_PORTION_SIZE][];
				int [] inst_tps = new int[(int)inst_count];
				int [] type_len = new int[complex_entity_count];

				long inst_id = -1;
				int cnt = 0;
				// Reads instance identifier and type identifier for instances from stream
				Object [] myDataA = null;
				if (((AEntity)set).myLength > 1) {
					myDataA = (Object [])((AEntity)set).myData;
				}
				int completeCeTypeIdx = -1;
				try {
					for(int i = 0; i < inst_ces.length; i++) {
						inst_ces[i] = new CEntity[(int)inst_count - cnt >= INST_PORTION_SIZE ?
												  (INST_PORTION_SIZE) : (int)inst_count - cnt];
						for (int j = 0; j < INST_PORTION_SIZE; j++) {
							inst_id = stream.readLong();
							int type_id = stream.readInt();
							int type_idx = typeIdToIndex(type_id);
							CEntity instance = null;
							if(type_idx >= 0) {
								if(i == 0 && j == 0
								   && (sim_status[type_idx] & SIM_PROCESS_MASK) == 0) {

									completeCeTypeIdx = type_idx;
									sim_status[type_idx] |= SIM_KEEP;
								}
								if((sim_status[type_idx] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {

									//ps.println("#"+inst_id+"\t"+type_id);
									int index = find_instance(0, lengths[type_idx] - 1, type_idx, inst_id);
									if (index >= 0) {
										instance = instances_sim[type_idx][index];
										instance.instance_identifier = -instance.instance_identifier;
									} else {
										CEntityDefinition edef;
										if (myDataA == null) {
											edef = (CEntityDefinition)((AEntity)set).myData;
										} else {
											edef = (CEntityDefinition)myDataA[type_id];
										}
										SdaiModel m;
										if (this == SdaiSession.baseDictionaryModel) {
											m = this;
										} else {
											m = ((CSchema_definition)edef.owning_model.described_schema)
												.modelDictionary;
										}
										SSuper sup = m.schemaData.super_inst;
										Class entityClass = edef.getEntityClass();

										// Creates instance using complex entity type identifier
										// and instance identifier
										instance = sup.makeInstance(entityClass, this, -1, 0);
										instance.owning_model = this;
										instance.instance_identifier = inst_id;
									}
									type_len[type_idx]++;
								}
							}
							// Saves instance reference for access using instance identifier
							inst_tps[cnt] = type_idx;
							inst_ces[i][j] = instance;
							repository.checkPersistentLabelRange(inst_id);
							if(++cnt == inst_count) {
								break;
							}
						}
					}
					//deleteInstances(true);
					prepareSimsForTypes(type_len);

					cnt = 0;
					for(int i = 0; i < inst_ces.length; i++) {
						for (int j = 0; j < INST_PORTION_SIZE; j++) {
							CEntity instance = inst_ces[i][j];
							if(instance != null) {
								int type_idx = inst_tps[cnt];
								int inst_idx = lengths[type_idx] - (instances_sim[type_idx].length -
																   type_len[type_idx]);
								instances_sim[type_idx][inst_idx] = instance;
								if((sim_status[type_idx] & SIM_DELETE) != 0
								   && instance.instance_identifier < 0) {

									instance.instance_identifier = -instance.instance_identifier;
								}
								lengths[type_idx]++;
							}
							if(++cnt == inst_count) {
								break;
							}
						}
					}
					inst_tps = null;
					type_len = null;
					resolveInConnectorsPartial(false);
					//invalidate_quick_find();

					//lCT = System.currentTimeMillis();
					//System.out.println("lSR - create instances - t="+(float)(lCT-lST)/1000);
					//lST = lCT;

					n_ex_models = 0;
					n_ex_repositories = 0;
					n_ex_edefs = 0;

					// Reads attribute values for instances from stream
					cnt = 0;
					for(int i = 0; i < inst_ces.length; i++) {
						for (int j = 0; j < INST_PORTION_SIZE; j++) {
							int bin_length = stream.readInt();

							CEntity instance = inst_ces[i][j];
							if(instance == null) {
								stream.skipBytes(bin_length);
							} else if(instance.instance_identifier < 0) {
								instance.instance_identifier = -instance.instance_identifier;
								stream.skipBytes(bin_length);
							} else {
								instance.owning_model = this;
								load_instance_remote(entity_values, stream, instance,
													 (CEntity_definition)instance.getInstanceType(),
													 streamUtf, inst_ces, true);
								instance.setAll(entity_values);
							}
							//ps.println("\t"+instance.toString());
							if(++cnt == inst_count) {
								break;
							}
						}
//						inst_ces[i] = null;
					}
					if(completeCeTypeIdx >= 0) {
						sim_status[completeCeTypeIdx] = (short)
							((sim_status[completeCeTypeIdx] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
					}
				} finally {
					if(completeCeTypeIdx >= 0) {
						sim_status[completeCeTypeIdx] &= ~SIM_KEEP;
					}
				}

            } else {
            	prepareSimsForTypes(null);
            }

            //lCT = System.currentTimeMillis();
            //System.out.println("lSR - set attr values - t="+(float)(lCT-lST)/1000);

		} catch (IOException ex) {
			throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, ex)).initCause(ex);
		}
		modified = oldModified;
        //lCT = System.currentTimeMillis();
        //System.out.println("lSR t="+(float)(lCT-lST)/1000);
	}

	protected EEntity loadInstanceRemote(long identifier, int instTypeId,
										 DataInput instData) throws SdaiException {
		int instTypeIndex = typeIdToIndex(instTypeId);
		if((sim_status[instTypeIndex] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
			StreamUTF streamUtf = new StreamUTF();

			try {

				if (underlying_schema == null) {
					underlying_schema = dictionary.described_schema;
				}
				AEntity_definition set = underlying_schema.getEntities();

				if (del_inst_threshold < 0) {
					del_inst_threshold = 0;
				}
				del_inst_threshold++;
// 				del_inst_ids = new long[INSTANCE_ARRAY_INITIAL_SIZE];???
// 				n_del_inst_ids = 0;???

				sim_status[instTypeIndex] |= SIM_SORTED;
				sim_status[instTypeIndex] = (short)
					((sim_status[instTypeIndex] & ~SIM_LOADED_MASK) | SIM_LOADED_PARTIAL);

				CEntityDefinition edef;
				if(((AEntity)set).myLength <= 1) {
					edef = (CEntityDefinition)((AEntity)set).myData;
				} else {
					edef = (CEntityDefinition)((Object[])((AEntity)set).myData)[instTypeId];
				}
				SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
				SSuper sup = m.schemaData.super_inst;
				Class entityClass = edef.getEntityClass();

				// Creates instance using complex entity type identifier and instance identifier
				CEntity instance = sup.makeInstance(entityClass, this, instTypeIndex, identifier);
				instance.instance_identifier = identifier;
				repository.checkPersistentLabelRange(identifier);

				//deleteInstances(true);

				instance.owning_model = this;
				load_instance_remote(entity_values, instData, instance,
									 (CEntity_definition)instance.getInstanceType(), streamUtf, null, true);
				instance.setAll(entity_values);
				return instance;
			} catch (IOException ex) {
				throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, ex)).initCause(ex);
			}
		} else {
			return null;
		}
	}

	private void deleteInstances(boolean replace_by_connector) throws SdaiException {
		int i, j;
		deleting = true;
		if (instances_sim != null) {
			CEntity [] row_of_instances;
			CEntity instance;
			for (i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null || lengths[i] <= 0) {
					continue;
				}
				row_of_instances = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					instance = row_of_instances[j];
					if (instance.instance_identifier > 0) {
						if (optimized) {
							instance.unsetInverseReferencesNoInverse(true, replace_by_connector, false);
						} else {
							instance.unsetInverseReferences(replace_by_connector, false);
						}
					} else {
						instance.instance_identifier = -instance.instance_identifier;
						if (optimized) {
							instance.unsetInverseReferencesNoInverse(true, true, false);
						} else {
							instance.unsetInverseReferences(true/*replace_by_connector*/, false);
							instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
						}
						instance.instance_identifier = -instance.instance_identifier;
					}
				}
			}

			for (i = 0; i < instances_sim.length; i++) {
				if (instances_sim[i] == null || lengths[i] <= 0) {
					continue;
				}
				row_of_instances = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					instance = row_of_instances[j];
					if (instance.instance_identifier > 0) {
						bypass_setAll = true;
						try {
							instance.setAll(null);
						} finally {
							bypass_setAll = false;
						}
						instance.deletedObject();
						instance.owning_model = null;
						instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
						instance.fireSdaiEvent(SdaiEvent.INVALID, -1, null);
					} else {
						instance.instance_identifier = -instance.instance_identifier;
					}
				}
				instances_sim[i] = emptyArray;
                sim_status[i] &= ~SIM_SORTED;
                //sim_status[i] = (short)((sim_status[number_of_entities] & ~SIM_LOADED_MASK) | SIM_LOADED_COMPLETE);
				lengths[i] = 0;
			}
			invalidate_quick_find();
		}
		deleting = false;
	}

	private void prepareSimsForTypes(int[] newLengthsPerType) throws SdaiException {
		deleting = true;
		if (instances_sim != null) {
			int typeLen = sim_status.length - 1;
			for (int i = 0; i < typeLen; i++) {
				CEntity [] row_of_instances = instances_sim[i];
				int newLen = newLengthsPerType != null ? newLengthsPerType[i] : 0;
				if (row_of_instances == null) {
					if(newLen > 0) {
						instances_sim[i] = new CEntity[newLen];
					}
				} else {
					if((sim_status[i] & SIM_DELETE) != 0) {
						for (int j = 0; j < lengths[i]; j++) {
							CEntity instance = row_of_instances[j];
							if (instance.instance_identifier > 0) {
								if (optimized) {
									instance.unsetInverseReferencesNoInverse(true, true, false);
								} else {
									instance.unsetInverseReferences(true/*replace_by_connector*/, false);
								}
								bypass_setAll = true;
								try {
									instance.setAll(null);
								} finally {
									bypass_setAll = false;
								}
								instance.deletedObject();
								instance.owning_model = null;
								instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
								instance.fireSdaiEvent(SdaiEvent.INVALID, -1, null);
							} else {
								instance.instance_identifier = -instance.instance_identifier;
								if (optimized) {
									instance.unsetInverseReferencesNoInverse(true, true, false);
								} else {
									instance.unsetInverseReferences(true/*replace_by_connector*/, false);
									instance.inverseList = null; // LK?: what is about connectors, maybe we require that this is already null
								}
							}
						}
						instances_sim[i] = new CEntity[newLen];
						lengths[i] = 0;
					} else if((sim_status[i] & SIM_KEEP) != 0) {
						int existingCount = 0;
						for (int j = 0; j < lengths[i]; j++) {
							CEntity instance = row_of_instances[j];
							if (instance.instance_identifier > 0) {
								row_of_instances[existingCount++] = instance;
							}
						}
						instances_sim[i] = new CEntity[newLen + existingCount];
						System.arraycopy(row_of_instances, 0, instances_sim[i], newLen, existingCount);
						lengths[i] = existingCount;
					} else if(newLen > 0) {
						throw new SdaiException(SdaiException.SY_ERR, "Unexpected type index " + i + " while loading remote stream");
					}
				}
				if(instances_sim[i] != null) {
					sim_status[i] |= SIM_SORTED;
				}
			}
			invalidate_quick_find();
		}
		deleting = false;
	}


/**
	Loads the data contained in the binary file to this SdaiModel (new version,
	valid from the build 220).
	Starting from the build 240 all instances within the binary file are sorted
	in one sequence (not separate sortings for each entity type are used as it
	was before).
*/
	private long loadStream2(DataInput stream) throws SdaiException {
		long largest_identifier = -1;
		int index;
		int i, j;
		int def_types_count;
		int single_entity_count;
		int ext_count;
		long inst_count;
		byte bt;
		CEntity_definition edef = null;
		CEntityDefinition edef_;
		StreamUTF streamUtf = new StreamUTF();

		try {
			if (underlying_build >= 249) {
				inst_count = stream.readLong();
				stream.skipBytes((int)inst_count * 8);
//				((DataInputStream)stream).skip(inst_count * 8);
				bt = stream.readByte();
				if (bt != 'S') {
					bin_file_exception(AdditionalMessages.BF_DAM);
				}
			}
//System.out.println("   SdaiModel *******  loadStream2 for model: " + name);
			// read defined_types
			def_types_count = stream.readShort();
			if (type_names == null) {
				if (def_types_count > TYPES_ARRAY_SIZE) {
					type_names = new String[def_types_count];
				} else {
					type_names = new String[TYPES_ARRAY_SIZE];
				}
			} else if (def_types_count > type_names.length) {
				type_names = new String[def_types_count];
			}
			for (i = 0; i < def_types_count; i++) {
				type_names[i] = streamUtf.readUTF(stream);
			}
			bt = stream.readByte();
			if (bt != 'S') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}

			AEntity_definition set;
			if (underlying_schema == SdaiSession.baseDictionarySchemaDef) {
				set = SdaiSession.bootEntityAggregate;
				initializeContents();
			} else {
				if (underlying_schema == null) {
					underlying_schema = dictionary.described_schema;
				}
				set = underlying_schema.getEntities();
			}
			int number_of_entities = ((AEntity)set).myLength;

			// read single entity type definitions
			single_entity_count = stream.readShort();
			if (simple_entity_names == null) {
				if (single_entity_count > ENTITIES_ARRAY_SIZE) {
					simple_entity_names = new String[single_entity_count];
				} else {
					simple_entity_names = new String[ENTITIES_ARRAY_SIZE];
				}
			} else if (single_entity_count > simple_entity_names.length) {
				simple_entity_names = new String[single_entity_count];
			}
			if (this == SdaiSession.baseDictionaryModel) {
				single_defs = new CEntity_definition[single_entity_count];
			}
			for (i = 0; i < single_entity_count; i++) {
				simple_entity_names[i] = streamUtf.readUTF(stream);
				int attr_count = stream.readShort();
				for (j = 0; j < attr_count; j++) {
					streamUtf.readUTF(stream);
				}
				int super_count = stream.readShort();
				if (this == SdaiSession.baseDictionaryModel) {
					index = find_entity(0, number_of_entities - 1, simple_entity_names[i], set);
					if (index < 0) {
						bin_file_exception(AdditionalMessages.BF_WENT);
					}
					edef = (CEntity_definition)set.getByIndex(index + 1);
					single_defs[i] = edef;
					edef_ = (CEntityDefinition)edef;
					edef_.indices2supertypes = new int[super_count];
					edef_.supertypes = new CEntity_definition[super_count];
					edef_.supertypes_count = super_count;
					edef_.noOfPartialAttributes = attr_count;
				}
				for (j = 0; j < super_count; j++) {
					int super_index = stream.readShort();
					if (this == SdaiSession.baseDictionaryModel) {
						((CEntityDefinition)edef).indices2supertypes[j] = super_index;
					}
				}
			}
			if (this == SdaiSession.baseDictionaryModel) {
				for (i = 0; i < single_entity_count; i++) {
					edef_ = single_defs[i];
					for (j = 0; j < edef_.indices2supertypes.length; j++) {
						edef_.supertypes[j] = single_defs[edef_.indices2supertypes[j]];
					}
				}
			}

			bt = stream.readByte();
			if (bt != 'S') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}

			// read complex types
			int leaf_count;
			int n_entities_from_file = stream.readShort();
if (SdaiSession.debug2) System.out.println(" number_of_entities = " + number_of_entities +
"    n_entities_from_file = " + n_entities_from_file);
			if (to_entities == null) {
				to_entities = new int[number_of_entities + 1];
			}
			for (i = 0; i < n_entities_from_file; i++) {
				leaf_count = stream.readShort();
				CEntity_definition [] leaves = null;
				if (this == SdaiSession.baseDictionaryModel) {
					leaves = new CEntity_definition[leaf_count];
				}
				String type;
				if (leaf_count == 1) {
					type = simple_entity_names[stream.readShort()];
					if (this == SdaiSession.baseDictionaryModel) {
						index = find_entity(0, number_of_entities - 1, type, set);
						if (index < 0) {
							bin_file_exception(AdditionalMessages.BF_WENT);
						}
						leaves[0] = (CEntity_definition)set.getByIndex(index + 1);
					}
				} else {
					int leaf_index = 0;
					type = "";
					for (j = 0; j < leaf_count; j++) {
						if (j > 0) {
							type += "$";
						}
						String leaf_name = simple_entity_names[stream.readShort()];
						type += leaf_name;
						if (this == SdaiSession.baseDictionaryModel) {
							index = find_entity(0, number_of_entities - 1, leaf_name, set);
							if (index < 0) {
								bin_file_exception(AdditionalMessages.BF_WENT);
							}
							leaves[leaf_index] = (CEntity_definition)set.getByIndex(index + 1);
							leaf_index++;
						}
					}
				}
				int n_instances = stream.readInt();
				index = find_entity(0, number_of_entities - 1, type, set);
				if (index < 0) {
if (SdaiSession.debug2) {System.out.println("  ENTITY: " + type + "  number of instances = " + n_instances +
"   number_of_entities = " + number_of_entities +
"   underlying_schema: " + underlying_schema.getName(null) +
"  model: " + name);
for (int r = 0; r < ((AEntity)set).myLength; r++) {
//CEntity_definition eee = (CEntity_definition)((AEntity)set).myData[r];
//System.out.println("  **** entity in underlying schema: " + eee.name);
}}
					bin_file_exception(AdditionalMessages.BF_WENT);
				}
				if (this == SdaiSession.baseDictionaryModel) {
					edef_ = (CEntityDefinition)set.getByIndex(index + 1);
					edef_.leaf_count = leaf_count;
					edef_.leaves = leaves;
				}
				to_entities[i] = index;
				lengths[index] = n_instances;
				instances_sim[index] = new CEntity[n_instances];
                sim_status[index] &= ~SIM_SORTED;
//if (repository != SdaiSession.systemRepository)
//System.out.println("   SdaiModel   for index = " + index +
//"    type: " + type + "  count of instances = " + n_instances);
			}


			// create all empty instances of all types
			CEntity [] instances;
			CEntity instance;
			inst_count = 0;
			if (repository == SdaiSession.systemRepository) {
				ext_count = instances_sim.length;
			} else {
				ext_count = instances_sim.length - 1;
			}
			Object [] myDataA = null;
			if (((AEntity)set).myLength > 1) {
				myDataA = (Object [])((AEntity)set).myData;
			}
			for (i = 0; i < ext_count; i++) {
				if (lengths[i] == 0) continue;
				if (myDataA == null) {
					edef_ = (CEntity_definition)((AEntity)set).myData;
				} else {
					edef_ = (CEntity_definition)myDataA[i];
				}
				// locate parent schema of this type
				SdaiModel m;
				if (this == SdaiSession.baseDictionaryModel) {
					m = this;
				} else {
					m = ((CSchema_definition) edef_.owning_model.described_schema).modelDictionary;
				}
				SSuper sup = m.schemaData.super_inst;
if (SdaiSession.debug2) System.out.println(" In: SdaiModel ******* edef: " + edef_.getCorrectName());
				Class entityClass = edef_.getEntityClass();
				instances = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					instance = sup.makeInstance(entityClass, this, -1, 0);
					instances[j] = instance;
					instance.owning_model = this;
				}
				inst_count += lengths[i];
			}
			invalidate_quick_find();

			// load and set instance attributes
			n_ex_models = 0;
			n_ex_repositories = 0;
			n_ex_edefs = 0;

			if (underlying_build >= 240) {
				if (current_lengths == null) {
					if (INIT_COUNT_FOR_ENTITIES < ext_count) {
						current_lengths = new int[ext_count];
					} else {
						current_lengths = new int[INIT_COUNT_FOR_ENTITIES];
					}
				} else if (current_lengths.length < ext_count) {
					int new_length = current_lengths.length * 2;
					if (new_length < ext_count) {
						new_length = ext_count;
					}
					current_lengths = new int[new_length];
				}
				for (i = 0; i < ext_count; i++) {
					current_lengths[i] = 0;
				}
				for (i = 0; i < inst_count; i++) {
					bt = stream.readByte();
					if (bt != 'c') {
						bin_file_exception(AdditionalMessages.BF_DAM);
					}
					index = to_entities[stream.readShort()];
					if (myDataA == null) {
						edef = (CEntity_definition)((AEntity)set).myData;
					} else {
						edef = (CEntity_definition)myDataA[index];
					}
					CEntity app_inst = instances_sim[index][current_lengths[index]];
					(current_lengths[index])++;
					load_instance(entity_values, stream, app_inst, edef, n_entities_from_file, def_types_count, 1, -1, false, streamUtf);
					app_inst.setAll(entity_values);
//System.out.println("SdaiModel ++++++   instance: " + edef.getName(null) +
//"   ident = " + app_inst.instance_identifier +
//"   index: " + index +
//"   current_lengths[index]: " + current_lengths[index] +
//"    model: " + name);
					if (app_inst.instance_identifier > largest_identifier) {
						largest_identifier = app_inst.instance_identifier;
					}
				}
			} else {
				for (i = 0; i < n_entities_from_file; i++) {
					index = to_entities[i];
					instances = instances_sim[index];
					if (myDataA == null) {
						edef = (CEntity_definition)((AEntity)set).myData;
					} else {
						edef = (CEntity_definition)myDataA[index];
					}
					for (j = 0; j < lengths[index]; j++) {
						bt = stream.readByte();
						if (bt != 'c') {
							bin_file_exception(AdditionalMessages.BF_DAM);
						}
						CEntity app_inst = instances[j];
if (SdaiSession.debug2)
if (app_inst == null) System.out.println(" app_inst is NULL  " + " index = " + index + "   i = "
+ i + "   j = " + j);
else System.out.println(" app_inst is POS");
						load_instance(entity_values, stream, app_inst, edef, n_entities_from_file, def_types_count, 1, -1, false, streamUtf);
if (SdaiSession.debug2) System.out.println("  instance ident = " + app_inst.instance_identifier);
						app_inst.setAll(entity_values);
						if (app_inst.instance_identifier > largest_identifier) {
							largest_identifier = app_inst.instance_identifier;
						}
					}
				}
			}
			bt = stream.readByte();
			if (bt != 'E') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		} finally {
            type_names = null;
            simple_entity_names = null;
            current_lengths = null;
        }
		if (this == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < ext_count; i++) {
				if (lengths[i] == 0) continue;
				Object [] myDataA = (Object [])((AEntity)SdaiSession.bootEntityAggregate).myData;
				edef_ = (CEntity_definition)myDataA[i];
				edef_.partialEntityTypes = null;
				edef_.noOfPartialEntityTypes = 0;
				edef_.leaves = null;
			}
		}
		modified = false;
		sort_inside_extents();
		return largest_identifier;
	}


	final private void bin_file_exception(String error_message) throws SdaiException {
		String base = SdaiSession.line_separator + error_message +
			SdaiSession.line_separator + "   Repository: " + repository.name +
			SdaiSession.line_separator + "   SdaiModel: " + name;
		throw new SdaiException(SdaiException.SY_ERR, base);
	}


	private void create_incompatible(int inc_count, int attr_count) throws SdaiException {
		if (incompatibles == null) {
			incompatibles = new int[INCOMPATIBLES_SIZE];
			value2attr = new int[INCOMPATIBLES_SIZE][];
		} else if (inc_count >= incompatibles.length) {
			int new_length = incompatibles.length * 2;
			int [] new_incompatibles = new int[new_length];
			System.arraycopy(incompatibles, 0, new_incompatibles, 0, incompatibles.length);
			int [][] new_value2attr = new int[new_length][];
			System.arraycopy(value2attr, 0, new_value2attr, 0, incompatibles.length);
			incompatibles = new_incompatibles;
			value2attr = new_value2attr;
		}
		value2attr[inc_count] = new int[attr_count];
	}


	private void enlarge_supertypes() throws SdaiException {
		int new_length = part_defs_from_file.length * 2;
		int [] new_part_defs_from_file = new int[new_length];
		System.arraycopy(part_defs_from_file, 0, new_part_defs_from_file, 0, part_defs_from_file.length);
		part_defs_from_file = new_part_defs_from_file;
		not_existing_part_defs_from_file = new int[new_length];
	}


	private int findTypePosition(int left, int right, int key) throws SdaiException {
		if (right < left) {
			return 0;
		}
		int index = part_defs_from_file[left];
		if (index > key) {
			return 0;
		} else if (index == key) {
			return -1;
		}
		index = part_defs_from_file[right];
		if (index < key) {
			return right + 1;
		} else if (index == key) {
			return -1;
		}
		while (left <= right) {
			if (right-left <= 1) {
				return right;
			}
			int middle = (left + right)/2;
			index = part_defs_from_file[middle];
			if (index < key) {
				left = middle;
			} else if (index > key) {
				right = middle;
			} else {
				return -1;
			}
		}
		return -1;
	}


	private void insertType(int pos, int part_defs_count, int type) throws SdaiException {
		for (int i = part_defs_count - 1; i >= pos; i--) {
			part_defs_from_file[i+1] = part_defs_from_file[i];
		}
		part_defs_from_file[pos] = type;
	}


	private void check_compatibility(int ent_index, String simple_ent_name, int attr_count,
			String [] attr_names, SchemaData sch_data) throws SdaiException {
		int j;

		int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, simple_ent_name);
		CEntityDefinition ent_def;
		if (index < 0) {
			SdaiSession session = repository.session;
			if (session.logWriterSession != null) {
				session.printlnSession(AdditionalMessages.BF_UEDT + simple_ent_name);
			} else {
				SdaiSession.println(AdditionalMessages.BF_UEDT + simple_ent_name);
			}
			return;
//			bin_file_exception(AdditionalMessages.BF_WENT);
		}
		ent_def = sch_data.entities[index];
		if (ent_def.attributes == null) {
			Class entityClass = ent_def.getEntityClass();
		}
		int attr_ln = ent_def.attributes.length;
		int start_pos = attr_ln - ent_def.noOfPartialAttributes;
		boolean incomp = false;
		if (attr_count < ent_def.noOfPartialAttributes) {
			incomp = true;
			create_incompatible(inc_count, attr_count);
			incompatibles[inc_count] = ent_index;
		}
		for (int i = 0; i < attr_count; i++) {
			index = -1;
			for (j = start_pos; j < attr_ln; j++) {
				CExplicit_attribute attr = ent_def.attributes[j];
				String name = attr.getName(null);
				if (name.equals(attr_names[i])) {
					index = j - start_pos;
					break;
				}
			}
			if (index != i) {
				if (!incomp) {
					incomp = true;
					create_incompatible(inc_count, attr_count);
					incompatibles[inc_count] = ent_index;
					for (j = 0; j < i; j++) {
						value2attr[inc_count][j] = j;
					}
				}
				value2attr[inc_count][i] = index;
			} else if (incomp) {
				value2attr[inc_count][i] = i;
			}
		}
		if (incomp) {
			inc_count++;
//System.out.println("SdaiModel ***** inc_count = " + inc_count);
//for (j = 0; j < attr_count; j++) System.out.print("  " + attr_names[j]);System.out.println("");
//System.out.println("SdaiModel ========== from data dictionary  count: " + ent_def.noOfPartialAttributes);
//for (j = attr_ln - ent_def.noOfPartialAttributes; j < attr_ln; j++)
//System.out.print("  " + ent_def.attributes[j].getName(null));System.out.println("");
//for (j = 0; j < attr_count; j++) System.out.print("  " + value2attr[inc_count-1][j]);
//System.out.println("");System.out.println("");
		}
	}


	private void create_complex_incompatible(int complex_inc_count, int part_def_count) throws SdaiException {
		if (complex_incompat == null) {
			complex_incompat = new int[COMPLEX_INCOMPATIBLES_SIZE];
			type2type = new int[COMPLEX_INCOMPATIBLES_SIZE][];
			dummy_attrs = new int[COMPLEX_INCOMPATIBLES_SIZE][];
		} else if (complex_inc_count >= complex_incompat.length) {
			int new_length = complex_incompat.length * 2;
			int [] new_complex_incompat = new int[new_length];
			System.arraycopy(complex_incompat, 0, new_complex_incompat, 0, complex_incompat.length);
			int [][] new_type2type = new int[new_length][];
			System.arraycopy(type2type, 0, new_type2type, 0, complex_incompat.length);
			int [][] new_dummy_attrs = new int[new_length][];
			System.arraycopy(dummy_attrs, 0, new_dummy_attrs, 0, complex_incompat.length);
			complex_incompat = new_complex_incompat;
			type2type = new_type2type;
			dummy_attrs = new_dummy_attrs;
		}
		type2type[complex_inc_count] = new int[part_def_count];
		dummy_attrs[complex_inc_count] = new int[part_def_count + 1];
	}


	int find_incompat_simple(int left, int right, int key_ind)
			throws SdaiException {
		while (left <= right) {
			int middle = (left + right) / 2;
			if (incompatibles[middle] < key_ind) {
				left = middle + 1;
			} else if (incompatibles[middle] > key_ind) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	private int collect_supertypes(int type_index, int init_count) throws SdaiException {
		int part_defs_count = init_count;
		if (init_count == 0) {
			part_defs_from_file[0] = type_index;
			part_defs_count++;
		} else {
			int pos = findTypePosition(0, part_defs_count - 1, type_index);
			if (pos >= 0) {
				if (part_defs_count >= part_defs_from_file.length) {
					enlarge_supertypes();
				}
				insertType(pos, part_defs_count, type_index);
				part_defs_count++;
			}
		}
		for (int i = 0; i < supertypes_from_file[type_index].length; i++) {
			part_defs_count = collect_supertypes(supertypes_from_file[type_index][i], part_defs_count);
		}
		return part_defs_count;
	}


	private void verify_complex_compatibility(CEntityDefinition edef, int defs_count_file, int single_entity_count,
			int compl_ent_tp_index) throws SdaiException {
		int i;
		boolean compatible = true;
		String key_name;
		int index2name;
		int ind2simple;
		if (defs_count_file != edef.noOfPartialEntityTypes) {
			compatible = false;
		} else {
			for (i = 0; i < edef.noOfPartialEntityTypes; i++) {
				key_name = edef.partialEntityTypes[i].getNameUpperCase();
				index2name = repository.session.find_string(0, single_entity_count - 1, key_name, simple_entity_names);
				if (index2name != part_defs_from_file[i]) {
					compatible = false;
					break;
				} else {
					ind2simple = find_incompat_simple(0, inc_count - 1, index2name);
					if (ind2simple >= 0) {
						compatible = false;
						break;
					}
				}
			}
		}
		if (compatible) {
			return;
		}
		create_complex_incompatible(complex_inc_count, edef.noOfPartialEntityTypes);
		complex_incompat[complex_inc_count] = compl_ent_tp_index;
		int search_ind = 0;
		int dummy_attr_count = 0;
		for (i = 0; i < defs_count_file; i++) {
			not_existing_part_defs_from_file[i] = 0;
		}
		for (i = 0; i < edef.noOfPartialEntityTypes; i++) {
			key_name = edef.partialEntityTypes[i].getNameUpperCase();
			index2name = repository.session.find_string(0, single_entity_count - 1, key_name, simple_entity_names);
			if (index2name < 0) {
				type2type[complex_inc_count][i] = -2;
				dummy_attrs[complex_inc_count][i] = 0;
				continue;
			}
			while (part_defs_from_file[search_ind] < index2name && search_ind < defs_count_file) {
				dummy_attr_count += attr_counts_file[part_defs_from_file[search_ind]];
				not_existing_part_defs_from_file[search_ind] = 1;
				search_ind++;
			}
			if (search_ind < defs_count_file && part_defs_from_file[search_ind] == index2name) {
				ind2simple = find_incompat_simple(0, inc_count - 1, index2name);
				if (ind2simple >= 0) {
					type2type[complex_inc_count][i] = ind2simple;
				} else {
					type2type[complex_inc_count][i] = -1;
				}
				dummy_attrs[complex_inc_count][i] = dummy_attr_count;
				dummy_attr_count = 0;
				search_ind++;
			} else {
				type2type[complex_inc_count][i] = -2;
				dummy_attrs[complex_inc_count][i] = 0;
			}
		}
		if (search_ind < defs_count_file) {
			while (search_ind < defs_count_file) {
				dummy_attr_count += attr_counts_file[part_defs_from_file[search_ind]];
				not_existing_part_defs_from_file[search_ind] = 1;
				search_ind++;
			}
		}
		dummy_attrs[complex_inc_count][edef.noOfPartialEntityTypes] = dummy_attr_count;
		printWarningToLogo(edef, complex_inc_count, defs_count_file, part_defs_from_file, not_existing_part_defs_from_file);
//String str = ((CEntity_definition)edef).getName(null);
//System.out.println("SdaiModel <<<<< entity: " + str);
//for (int j = 0; j < defs_count_file; j++) System.out.print("  " + part_defs_from_file[j]);
//System.out.println("");
//for (int j = 0; j < defs_count_file; j++) System.out.print("  " + attr_counts_file[part_defs_from_file[j]]);
//System.out.println("");
//for (int j = 0; j < edef.noOfPartialEntityTypes; j++) System.out.print("  " + type2type[complex_inc_count][j]);
//System.out.println("");
//for (int j = 0; j <= edef.noOfPartialEntityTypes; j++) System.out.print("  " + dummy_attrs[complex_inc_count][j]);
//System.out.println("");System.out.println("");System.out.println("");
		complex_inc_count++;
	}


	void printWarningToLogo(CEntityDefinition edef, int complex_inc_count, int defs_count_file,
				int [] part_defs_from_file, int [] not_existing_part_defs_from_file) throws SdaiException {
		SdaiSession session = repository.session;
		String ent_name = ((CEntity_definition)edef).getName(null);
		if (session.logWriterSession != null) {
			session.printlnSession(AdditionalMessages.BF_ICED + SdaiSession.line_separator +
				AdditionalMessages.RD_ENT + ent_name);
		} else {
			SdaiSession.println(AdditionalMessages.BF_ICED + SdaiSession.line_separator +
				AdditionalMessages.RD_ENT + ent_name);
		}
		int i;
		String part_name;
		String str;
		for (i = 0; i < edef.noOfPartialEntityTypes; i++) {
			if (type2type[complex_inc_count][i] == -1) {
				continue;
			}
			part_name = edef.partialEntityTypes[i].getName(null);
			str = null;
			if (type2type[complex_inc_count][i] == -2) {
				str = AdditionalMessages.BF_MISI + part_name;
			} else if (type2type[complex_inc_count][i] >= 0) {
				str = AdditionalMessages.BF_DIAT + part_name;
			}
			if (str == null) {
				continue;
			}
			if (session.logWriterSession != null) {
				session.printlnSession(str);
			} else {
				SdaiSession.println(str);
			}
		}
		for (i = 0; i < defs_count_file; i++) {
			if (not_existing_part_defs_from_file[i] == 0) {
				continue;
			}
			part_name = simple_entity_names[part_defs_from_file[i]].toLowerCase();
			str = AdditionalMessages.BF_OLDT + part_name;
			if (session.logWriterSession != null) {
				session.printlnSession(str);
			} else {
				SdaiSession.println(str);
			}
		}
	}


	int find_incompat_complex(int left, int right, int key_ind)
			throws SdaiException {
		while (left <= right) {
			int middle = (left + right) / 2;
			if (complex_incompat[middle] < key_ind) {
				left = middle + 1;
			} else if (complex_incompat[middle] > key_ind) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	private long loadStream3(DataInput stream, int mode_to_start) throws SdaiException {
		long largest_identifier = -1;
		int index;
		int i, j;
		int def_types_count;
		int single_entity_count;
		int ext_count;
		long inst_count;
		byte bt;
		boolean mod_small;
		CEntity_definition edef = null;
		CEntityDefinition edef_;
		StreamUTF streamUtf = new StreamUTF();

		if (mode_to_start == READ_ONLY && repository != SdaiSession.systemRepository) {
			mod_small = true;
		} else {
			mod_small = false;
		}
		try {
			inst_count = stream.readLong();
			stream.skipBytes((int)inst_count * 8);
			bt = stream.readByte();
			if (bt != 'S') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}
			// read defined_types
			def_types_count = stream.readShort();
			if (type_names == null) {
				if (def_types_count > TYPES_ARRAY_SIZE) {
					type_names = new String[def_types_count];
				} else {
					type_names = new String[TYPES_ARRAY_SIZE];
				}
			} else if (def_types_count > type_names.length) {
				type_names = new String[def_types_count];
			}
			for (i = 0; i < def_types_count; i++) {
				type_names[i] = streamUtf.readUTF(stream);
			}
			bt = stream.readByte();
			if (bt != 'S') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}

			AEntity_definition set;
			if (underlying_schema == SdaiSession.baseDictionarySchemaDef) {
				set = SdaiSession.bootEntityAggregate;
				initializeContents();
			} else {
				if (underlying_schema == null) {
					underlying_schema = dictionary.described_schema;
				}
				set = underlying_schema.getEntities();
			}
			int number_of_entities = ((AEntity)set).myLength;

			// read single entity type definitions
			inc_count = 0;
			SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
			single_entity_count = stream.readShort();
			if (simple_entity_names == null) {
				if (single_entity_count > ENTITIES_ARRAY_SIZE) {
					simple_entity_names = new String[single_entity_count];
					if (repository != SdaiSession.systemRepository) {
						attr_counts_file = new int[single_entity_count];
						supertypes_from_file = new int[single_entity_count][];
					}
				} else {
					simple_entity_names = new String[ENTITIES_ARRAY_SIZE];
					if (repository != SdaiSession.systemRepository) {
						attr_counts_file = new int[ENTITIES_ARRAY_SIZE];
						supertypes_from_file = new int[ENTITIES_ARRAY_SIZE][];
					}
				}
			} else if (single_entity_count > simple_entity_names.length) {
				simple_entity_names = new String[single_entity_count];
				if (repository != SdaiSession.systemRepository) {
					attr_counts_file = new int[single_entity_count];
					supertypes_from_file = new int[single_entity_count][];
				}
			}
			if (this == SdaiSession.baseDictionaryModel) {
				single_defs = new CEntity_definition[single_entity_count];
			}
			for (i = 0; i < single_entity_count; i++) {
				simple_entity_names[i] = streamUtf.readUTF(stream);
				int attr_count = stream.readShort();
				if (repository != SdaiSession.systemRepository) {
					attr_counts_file[i] = attr_count;
				}
				for (j = 0; j < attr_count; j++) {
					String attr_name = streamUtf.readUTF(stream);
					if (repository == SdaiSession.systemRepository) {
						continue;
					}
					if (attr_names == null) {
						if (attr_count > ATTRS_ARRAY_SIZE) {
							attr_names = new String[attr_count];
						} else {
							attr_names = new String[ATTRS_ARRAY_SIZE];
						}
					} else if (attr_count > attr_names.length) {
						attr_names = new String[attr_count];
					}
					attr_names[j] = attr_name;
				}
				if (repository != SdaiSession.systemRepository) {
//System.out.println("SdaiModel ***** i: " + i + "   simple entity: " + simple_entity_names[i] +
//"   attr_count = " + attr_count);
//for (j = 0; j < attr_count; j++) System.out.print("  " + attr_names[j]);
//System.out.println("");System.out.println("");
					check_compatibility(i, simple_entity_names[i], attr_count, attr_names, sch_data);
				}
				int super_count = stream.readShort();
				if (repository != SdaiSession.systemRepository) {
					supertypes_from_file[i] = new int[super_count];
				}
				if (this == SdaiSession.baseDictionaryModel) {
					index = find_entity(0, number_of_entities - 1, simple_entity_names[i], set);
					if (index < 0) {
						bin_file_exception(AdditionalMessages.BF_WENT);
					}
					edef = (CEntity_definition)set.getByIndex(index + 1);
					single_defs[i] = edef;
					edef_ = (CEntityDefinition)edef;
					edef_.indices2supertypes = new int[super_count];
					edef_.supertypes = new CEntity_definition[super_count];
					edef_.supertypes_count = super_count;
					edef_.noOfPartialAttributes = attr_count;
				}
				for (j = 0; j < super_count; j++) {
					int super_index = stream.readShort();
					if (repository != SdaiSession.systemRepository) {
						supertypes_from_file[i][j] = super_index;
					}
					if (this == SdaiSession.baseDictionaryModel) {
						((CEntityDefinition)edef).indices2supertypes[j] = super_index;
					}
				}
//if (repository != SdaiSession.systemRepository) {
//for (j = 0; j < super_count; j++) System.out.print("  " + supertypes_from_file[i][j]);
//System.out.println("");System.out.println("");}
			}
			if (this == SdaiSession.baseDictionaryModel) {
				for (i = 0; i < single_entity_count; i++) {
					edef_ = single_defs[i];
					for (j = 0; j < edef_.indices2supertypes.length; j++) {
						edef_.supertypes[j] = single_defs[edef_.indices2supertypes[j]];
					}
				}
			}

			bt = stream.readByte();
			if (bt != 'S') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}

			// read complex types
			complex_inc_count = 0;
			CEntity instance;
			inst_count = 0;

			if (repository != SdaiSession.systemRepository && part_defs_from_file == null) {
				part_defs_from_file = new int[PART_DEFS_SIZE];
				not_existing_part_defs_from_file = new int[PART_DEFS_SIZE];
			}
			CEntity [] instances;
			int leaf_count;
			int n_entities_from_file = stream.readShort();
			if (mod_small) {
				entities = new CEntityDefinition[n_entities_from_file];
				prepareInstancesSim(n_entities_from_file + 1, false);
				instances_sim[n_entities_from_file] = emptyArray;
				lengths[n_entities_from_file] = 0;
			} else {
				entities = null;
				if (instances_sim == null) {
					prepareInstancesSim(number_of_entities + 1, true /*false*/);
				}
			}
			if (to_entities == null) {
				to_entities = new int[n_entities_from_file + 1];
			}
			StaticFields staticFields = StaticFields.get();
			if (staticFields.ids == null) {
				staticFields.ids = new long[ONE_TYPE_INSTANCES_ARRAY_SIZE];
			}
			Object [] myDataA = null;
			if (((AEntity)set).myLength > 1) {
				myDataA = (Object [])((AEntity)set).myData;
			}
			int miss_small = 0;
			for (i = 0; i < n_entities_from_file; i++) {
				leaf_count = stream.readShort();
				CEntity_definition [] leaves = null;
				if (this == SdaiSession.baseDictionaryModel) {
					leaves = new CEntity_definition[leaf_count];
				}
//if (repository != SdaiSession.systemRepository)
//System.out.println("SdaiModel ++++++ leaf_count: " + leaf_count);
				String type;
				int defs_count_file = 0;
				int type_index;
				if (leaf_count == 1) {
					type_index = stream.readShort();
					if (repository != SdaiSession.systemRepository) {
						defs_count_file = collect_supertypes(type_index, 0);
					}
					type = simple_entity_names[type_index];
//if (repository != SdaiSession.systemRepository) {
//System.out.println("SdaiModel ---------------------- type: " + type + "   defs_count_file: " + defs_count_file);
//for (j = 0; j < defs_count_file; j++) System.out.print("  " + part_defs_from_file[j]);
//System.out.println("");System.out.println("");}
					if (this == SdaiSession.baseDictionaryModel) {
						index = find_entity(0, number_of_entities - 1, type, set);
						if (index < 0) {
							bin_file_exception(AdditionalMessages.BF_WENT);
						}
						leaves[0] = (CEntity_definition)set.getByIndex(index + 1);
					}
				} else {
					int leaf_index = 0;
					type = "";
					for (j = 0; j < leaf_count; j++) {
						if (j > 0) {
							type += "$";
						}
						type_index = stream.readShort();
						if (repository != SdaiSession.systemRepository) {
							defs_count_file = collect_supertypes(type_index, defs_count_file);
						}
						String leaf_name = simple_entity_names[type_index];
						type += leaf_name;
						if (this == SdaiSession.baseDictionaryModel) {
							index = find_entity(0, number_of_entities - 1, leaf_name, set);
							if (index < 0) {
								bin_file_exception(AdditionalMessages.BF_WENT);
							}
							leaves[leaf_index] = (CEntity_definition)set.getByIndex(index + 1);
							leaf_index++;
						}
					}
				}
				long n_instances = stream.readLong();
				index = find_entity(0, number_of_entities - 1, type, set);
//if (repository != SdaiSession.systemRepository)
//System.out.println("SdaiModel ++++++ type: " + type + "   index: " + index);
				if (index < 0) {
					inst_count += n_instances;
					printWarningToLogo(type);
					process_not_found(i, stream, n_instances, mod_small);
					if (mod_small) {
						miss_small++;
					}
				} else {
					if (myDataA == null) {
						edef_ = (CEntity_definition)((AEntity)set).myData;
					} else {
						edef_ = (CEntity_definition)myDataA[index];
					}
					if (this == SdaiSession.baseDictionaryModel) {
						edef_.leaf_count = leaf_count;
						edef_.leaves = leaves;
					}
//if (repository != SdaiSession.systemRepository)
//System.out.println("SdaiModel :::::: edef_: " + ((CEntity_definition)edef_).getName(null) +
//"   single_entity_count: " + single_entity_count + "   inc_count: " + inc_count);
					if (repository != SdaiSession.systemRepository) {
						verify_complex_compatibility(edef_, defs_count_file, single_entity_count, i);
					}

					SdaiModel m;
					if (this == SdaiSession.baseDictionaryModel) {
						m = this;
					} else {
						m = ((CSchema_definition) edef_.owning_model.described_schema).modelDictionary;
					}
					SSuper sup = m.schemaData.super_inst;
					Class entityClass = edef_.getEntityClass();
					long inst_id;
					to_entities[i] = index;
					if (mod_small) {
						entities[i] = underlying_schema.modelDictionary.schemaData.entities[index];
						lengths[i] = (int)n_instances;
						sim_status[i] |= SIM_SORTED;
						CEntity [] new_array = new CEntity[(int)n_instances];
						if (n_instances > 0) {
							for (j = 0; j < n_instances; j++) {
								instance = sup.makeInstance(entityClass, this, -1, 0);
								new_array[j] = instance;
								instance.owning_model = this;
								inst_id = stream.readLong();
								if (CHECK_ID && repository.checkInstanceIdentifier(inst_id, this)) {
									String base = SdaiSession.line_separator + AdditionalMessages.BF_DIID + inst_id;
									throw new SdaiException(SdaiException.SY_ERR, base);
								}
								instance.instance_identifier = inst_id;
							}
							inst_count += n_instances;
						}
						instances_sim[i] = new_array;
					} else {
						instances = instances_sim[index];
						int created_count = 0;
						int search_ind = 0;
						int write_ind = 0;
						long current_id;
						int old_length = lengths[index];
						if (lengths[index] > 0) {
							current_id = instances[search_ind].instance_identifier;
						} else {
							current_id = -1;
						}
						for (j = 0; j < n_instances; j++) {
							inst_id = stream.readLong();
							if (CHECK_ID && repository.checkInstanceIdentifier(inst_id, this)) {
								String base = SdaiSession.line_separator + AdditionalMessages.BF_DIID + inst_id;
								throw new SdaiException(SdaiException.SY_ERR, base);
							}
							if (search_ind < lengths[index]) {
								while (current_id < inst_id) {
									instances[search_ind].fireSdaiEvent(SdaiEvent.INVALID, -1, null);
									search_ind++;
									if (search_ind >= lengths[index]) {
										break;
									}
									current_id = instances[search_ind].instance_identifier;
								}
							}
							if (current_id == inst_id) {
								instances[write_ind++] = instances[search_ind];
								search_ind++;
								if (search_ind < lengths[index]) {
									current_id = instances[search_ind].instance_identifier;
								}
							} else {
								if (staticFields.ids.length <= created_count) {
									ensureIdsCapacity(staticFields);
								}
								staticFields.ids[created_count++] = inst_id;
							}
						}
						lengths[index] = (int)n_instances;
						sim_status[index] |= SIM_SORTED;
						if (n_instances == 0) {
							continue;
						}
						int set_ind;
						if (instances.length >= n_instances) {
							set_ind = (int)n_instances - 1;
							search_ind = created_count - 1;
							if (search_ind >= 0) {
								inst_id = staticFields.ids[search_ind];
							} else {
								inst_id = -1;
							}
							for (j = write_ind - 1; j >= 0; j--) {
								if (search_ind >= 0) {
									current_id = instances[j].instance_identifier;
									while (current_id < inst_id) {
										instance = sup.makeInstance(entityClass, this, -1, 0);
										instances[set_ind--] = instance;
										instance.owning_model = this;
										instance.instance_identifier = inst_id;
										search_ind--;
										if (search_ind < 0) {
											break;
										}
										inst_id = staticFields.ids[search_ind];
									}
								}
								instances[set_ind--] = instances[j];
							}
							for (j = 0; j <= set_ind; j++) {
								instance = sup.makeInstance(entityClass, this, -1, 0);
								instances[j] = instance;
								instance.owning_model = this;
								instance.instance_identifier = staticFields.ids[j];
							}
							if (old_length > n_instances) {
								for (int q = (int)n_instances; q < old_length; q++) {
									instances[q] = null;
								}
							}
						} else {
							CEntity [] new_array = new CEntity[(int)n_instances];
							set_ind = 0;
							search_ind = 0;
							inst_id = staticFields.ids[0];
							for (j = 0; j < write_ind; j++) {
								if (search_ind < created_count) {
									current_id = instances[j].instance_identifier;
									while (current_id > inst_id) {
										instance = sup.makeInstance(entityClass, this, -1, 0);
										new_array[set_ind++] = instance;
										instance.owning_model = this;
										instance.instance_identifier = inst_id;
										search_ind++;
										if (search_ind >= created_count) {
											break;
										}
										inst_id = staticFields.ids[search_ind];
									}
								}
								new_array[set_ind++] = instances[j];
							}
							for (j = set_ind; j < n_instances; j++) {
								instance = sup.makeInstance(entityClass, this, -1, 0);
								new_array[j] = instance;
								instance.owning_model = this;
								instance.instance_identifier = staticFields.ids[search_ind++];
							}
							instances_sim[index] = new_array;
							instances = null;
						}
						inst_count += n_instances;
					}
				}
			}
			invalidate_quick_find();

			// create all empty instances of all types

			if (mod_small) {
				ext_count = n_entities_from_file;
			} else if (repository == SdaiSession.systemRepository) {
				ext_count = instances_sim.length;
			} else {
				ext_count = instances_sim.length - 1;
			}

			// load and set instance attributes
			n_ex_models = 0;
			n_ex_repositories = 0;
			n_ex_edefs = 0;
			if (current_lengths == null) {
				if (INIT_COUNT_FOR_ENTITIES < ext_count) {
					current_lengths = new int[ext_count];
				} else {
					current_lengths = new int[INIT_COUNT_FOR_ENTITIES];
				}
			} else if (current_lengths.length < ext_count) {
				int new_length = current_lengths.length * 2;
				if (new_length < ext_count) {
					new_length = ext_count;
				}
				current_lengths = new int[new_length];
			}
			for (i = 0; i < ext_count; i++) {
				current_lengths[i] = 0;
			}

            //<--VV--030616-- Reading aim_2_arm link data from stream
            String[] sRepoNames = new String[NUMBER_OF_EXTERNAL_MODS];
            String[] sModelNames = new String[NUMBER_OF_EXTERNAL_MODS];
			int iModelNamesCnt = 0;
            //--VV--030616--> Reading aim_2_arm link data from stream

			for (i = 0; i < inst_count; i++) {
                if (bt != 'c') {        //--VV--030613--Added for AIM to ARM link implementation
				    bt = stream.readByte();
                }
                //if (name.equals("test_aim")) System.out.print("["+(char)bt+"]");
				if (bt != 'c') {
//System.out.println("SdaiModel ++++++ bt: " + (char)bt + "  *** i: " + i);
					bin_file_exception(AdditionalMessages.BF_DAM);
				}
				int short_index = stream.readShort();
				if (to_entities[short_index] < 0) {
					load_unrecognized_instance(stream, this, 2, streamUtf);
					continue;
				}
				if (mod_small) {
					index = short_index;
					edef = (CEntity_definition)entities[index];
				} else {
					index = to_entities[short_index];
					if (myDataA == null) {
						edef = (CEntity_definition)((AEntity)set).myData;
					} else {
						edef = (CEntity_definition)myDataA[index];
					}
				}
				int spec_type_index = -1;
				if (repository != SdaiSession.systemRepository && complex_inc_count > 0) {
					spec_type_index = find_incompat_complex(0, complex_inc_count - 1, short_index);
				}
				CEntity app_inst = instances_sim[index][current_lengths[index]];
				(current_lengths[index])++;
//if (repository != SdaiSession.systemRepository)
//System.out.println("SdaiModel *** i: " + i + "  entity: " + edef.getName(null));
				staticFields.current_instance_identifier = app_inst.instance_identifier;
//long time1 = System.currentTimeMillis();
				load_instance(entity_values, stream, app_inst, edef, n_entities_from_file, def_types_count, 2,
					spec_type_index, mod_small, streamUtf);
				app_inst.setAll(entity_values);
//long time2 = System.currentTimeMillis();
//long time_diff = time2-time1;
//if (SdaiModelLocalImpl.sum_time) time_load_inst+=time_diff;
				staticFields.current_instance_identifier = -1;
//System.out.println("SdaiModel ++++++   instance: " + edef.getName(null) +
//"   ident = " + app_inst.instance_identifier +
//"   index: " + index +
//"   current_lengths[index]: " + current_lengths[index] +
//"    model: " + name);
				if (app_inst.instance_identifier > largest_identifier) {
					largest_identifier = app_inst.instance_identifier;
				}
                //<--VV--030613-- Reading aim_2_arm link data from stream
                bt = stream.readByte();
                //if (name.equals("test_aim")) System.out.print("["+(char)bt+"]");
                while (bt == 'l' || bt == 'k') {
                    String sArmRepoName = "";
                    String sArmModelName = "";
                    long lArmPL = -1;

	                switch (bt) {
                        case 'l':
                            sArmRepoName = streamUtf.readUTF(stream);    // ARM repository name
                            sArmModelName = streamUtf.readUTF(stream);   // ARM model name
                            lArmPL = stream.readLong();         // ARM instance identifier
                            //System.out.println("repo="+sArmRepoName+" model="+sArmModelName+" lArmPL="+lArmPL);
                            if (iModelNamesCnt >= sModelNames.length) {
                                sModelNames = repository.session.ensureStringsCapacity(sModelNames);
                                sRepoNames  = repository.session.ensureStringsCapacity(sRepoNames);
                            }
                            sRepoNames[iModelNamesCnt] = sArmRepoName;
                            sModelNames[iModelNamesCnt] = sArmModelName;
                            iModelNamesCnt++;
                            break;
                        case 'k':
                            int iArmModelNamesIdx = stream.readShort();     // ARM model index
                            lArmPL = stream.readLong();                     // ARM instance identifier
                            if (iArmModelNamesIdx < iModelNamesCnt) {
                                sArmRepoName  = sRepoNames[iArmModelNamesIdx];  // ARM repository name
                                sArmModelName = sModelNames[iArmModelNamesIdx]; // ARM model name
                            }
                            else {
											bin_file_exception(AdditionalMessages.BF_WVAL);
                             }
                            //System.out.println("repo="+sArmRepoName+" model="+sArmModelName+" lArmPL="+lArmPL);
                            break;
                    }

                    SdaiRepository repo = null;
                    SdaiModel model = null;

                    if (sArmRepoName.equalsIgnoreCase("") || sArmRepoName.equalsIgnoreCase(repository.getName())) {
                        repo = repository;
                    }
                    else {
                        repo = repository.session.linkRepositoryFast( sArmRepoName, null);
                    }

                    if (repo != null) {
                        if (!repo.isActive()) {
                            repo.openRepository();
                        }
                        model = repo.findSdaiModel(sArmModelName);
                        if (model != null) {
                            if (model.getMode() == SdaiModel.NO_ACCESS) {
                                model.startReadOnlyAccess();
                            }
							CEntity instInModel = model.quick_find_instance(lArmPL);
							if(instInModel != null) {
								EMappedARMEntity instanceArm = (EMappedARMEntity)instInModel;
								instanceArm.setAimInstance(app_inst);
							}
                        }
                        else {
                            SdaiSession.println("Can not create AIM to ARM link for instance " + app_inst.instance_identifier + ". Model " + sArmModelName + " in repository " + repo.getName() + " does not exist");
                            //System.out.println("Model " + sArmModelName + " does not exist");
                        }
                    }
                    else {
                        if (sArmRepoName.equalsIgnoreCase("")) {
                            SdaiSession.println("Can not create AIM to ARM link for instance " + app_inst.instance_identifier + ". Repository " + repository.getName() + " does not exist");
                            //System.out.println("Repository " + repository.getName() + " does not exist");
                        }
                        else {
                            SdaiSession.println("Can not create AIM to ARM link for instance " + app_inst.instance_identifier + ". Repository " + sArmRepoName + " does not exist");
                            //System.out.println("Repository " + sArmRepoName + " does not exist");
                        }
                    }
                    bt = stream.readByte();
                }
                //--VV--030613--> Reading aim_2_arm link data from stream
			}
			if (!mod_small) {
				for (i = 0; i < ext_count; i++) {
					current_lengths[i] = -1;
				}
				for (i = 0; i < n_entities_from_file; i++) {
					current_lengths[to_entities[i]] = 1;
				}
				for (i = 0; i < ext_count; i++) {
					if (current_lengths[i] == -1) {
						lengths[i] = 0;
						instances_sim[i] = emptyArray;
					}
				}
			} else if (miss_small > 0) {
				shrink_types(miss_small);
			}
			to_entities = null;
			if (CHECK_ID) {
				repository.closeIdentifiers();
			}
            if (bt != 'E') {        //--VV--030613--Added for AIM to ARM link implementation
			    bt = stream.readByte();
            }
            //if (name.equals("test_aim")) System.out.print("["+(char)bt+"]");

			if (bt != 'E') {
				bin_file_exception(AdditionalMessages.BF_DAM);
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		} finally {
            type_names = null;
            simple_entity_names = null;
            current_lengths = null;
        }
		if (this == SdaiSession.baseDictionaryModel) {
			for (i = 0; i < ext_count; i++) {
				if (lengths[i] == 0) continue;
				Object [] myDataA = (Object [])((AEntity)SdaiSession.bootEntityAggregate).myData;
				edef_ = (CEntity_definition)myDataA[i];
				edef_.partialEntityTypes = null;
				edef_.noOfPartialEntityTypes = 0;
				edef_.leaves = null;
			}
		}
		if (modified_sleeping) {
			modified_sleeping = false;
		}
		modified = false;
		return largest_identifier;
	}


	void printWarningToLogo(String type_name) throws SdaiException {
		SdaiSession session = repository.session;
		if (session.logWriterSession != null) {
			session.printlnSession(AdditionalMessages.BF_NFOU + SdaiSession.line_separator +
				AdditionalMessages.RD_ENT + type_name.toLowerCase());
		} else {
			SdaiSession.println(AdditionalMessages.BF_NFOU + SdaiSession.line_separator +
				AdditionalMessages.RD_ENT + type_name.toLowerCase());
		}
	}


	private void process_not_found(int ind, DataInput stream, long n_instances, boolean small)
			throws java.io.IOException, SdaiException {
		to_entities[ind] = -1;
		if (small) {
			entities[ind] = null;
		}
		for (int j = 0; j < n_instances; j++) {
			long inst_id = stream.readLong();
		}
	}


	private void shrink_types(int miss_small) throws SdaiException {
		int i;
		int size = entities.length;
		int new_size = size - miss_small;
		CEntityDefinition [] new_entities = new CEntityDefinition[new_size];
		int count = 0;
		for (i = 0; i < size; i++) {
			if (entities[i] == null) {
				continue;
			}
			new_entities[count] = entities[i];
			lengths[count] = lengths[i];
			instances_sim[count] = instances_sim[i];
			instances_sim_start[count] = instances_sim_start[i];
			instances_sim_end[count] = instances_sim_end[i];
			sim_status[count] = sim_status[i];
			count++;
		}
		instances_sim[count] = instances_sim[size];
		lengths[count] = lengths[size];
		for (i = count + 1; i <= size; i++) {
			instances_sim[i] = null;
			lengths[i] = 0;
		}
		entities = new_entities;
	}


	private void load_unrecognized_instance(DataInput stream, SdaiModel mod,
				int version, StreamUTF streamUtf)
			throws java.io.IOException, SdaiException {
		if (version <= 1) {
			long ident = stream.readLong();
		}
		byte sym = (byte)' ';
		while (sym != 'c' && sym != 'E') {
			sym = bypass_value_for_binary(stream, mod, true, (byte)' ', streamUtf);
		}
	}


	private byte bypass_value_for_binary(DataInput stream, SdaiModel mod,
				boolean byte_needed, byte sym, StreamUTF streamUtf)
				throws java.io.IOException, SdaiException, ArrayIndexOutOfBoundsException {
		byte bt;
		int index;
		int sec_index;
		long ident;
		String rep_name;
		String mod_name;
		SdaiRepository repo = mod.repository;
		SdaiSession ses = repo.session;

		byte token;
		if (byte_needed) {
			token = stream.readByte();
			if (token == 'c' || token == 'E') {
				return token;
			}
		} else {
			token = sym;
		}
		switch (token) {
			case '$':
			case '*':
			case 'f':
			case 't':
			case 'u':
				return '0';
			case 'i':
				int whole = stream.readInt();
				return '0';
			case 'r':
				double real_numb = stream.readDouble();
				return '0';
			case 'e':
			case 's':
			case 'b':
				String str = streamUtf.readUTF(stream)/*.intern()*/;
				return '0';
			case 'B':
				int bt_count = (int)stream.readLong();
				stream.skipBytes(bt_count);
				return '0';
			case 'p':
				index = stream.readShort();
				bt = stream.readByte();
				bypass_value_for_binary(stream, mod, false, bt, streamUtf);
				return '0';
			case '1':
				index = stream.readShort();
				sec_index = stream.readInt();
				return '0';
			case '2':
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1' || bt == '3') {
					index = stream.readShort();
				} else if (bt == '2') {
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = streamUtf.readUTF(stream);
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				mod.ex_models[mod.n_ex_models] = extractModel(repo, mod_name);
				mod.n_ex_models++;
				ident = stream.readLong();
				return '0';
			case '3':
				index = stream.readShort();
				bt = stream.readByte();
				if (bt == '1' || bt == '3') {
					sec_index = stream.readShort();
				} else if (bt == '2') {
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = streamUtf.readUTF(stream);
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				return '0';
			case '4':
				rep_name = streamUtf.readUTF(stream);
				if (mod.n_ex_repositories >= mod.ex_repositories.length) {
					mod.ex_repositories = ses.ensureStringsCapacity(mod.ex_repositories);
				}
				mod.ex_repositories[mod.n_ex_repositories] = rep_name;
				mod.n_ex_repositories++;
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1' || bt == '3') {
					index = stream.readShort();
				} else if (bt == '2') {
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = streamUtf.readUTF(stream);
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				if (!file_saved && rep_name.length() > SdaiSession.DEF_REP_NAME_LENGTH &&
						rep_name.substring(0, SdaiSession.DEF_REP_NAME_LENGTH).equals(SdaiSession.DEF_REP_NAME)) {
					mod.ex_repositories[mod.n_ex_repositories - 1] = SdaiSession.DEF_REP_NAME;
				} else {
					mod.ex_models[mod.n_ex_models] = extractModel(rep_name, mod_name);
					mod.n_ex_models++;
				}
				return '0';
			case '5':
				index = stream.readShort();
				rep_name = mod.ex_repositories[index];
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1' || bt == '3') {
					index = stream.readShort();
				} else if (bt == '2') {
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = streamUtf.readUTF(stream);
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				mod.ex_models[mod.n_ex_models] = extractModel(rep_name, mod_name);
				mod.n_ex_models++;
				return '0';
			case '6':
				index = stream.readShort();
				sec_index = stream.readShort();
				bt = stream.readByte();
				if (bt == '1' || bt == '3') {
					index = stream.readShort();
				} else if (bt == '2') {
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = streamUtf.readUTF(stream);
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				return '0';
			case '(':
				int index_in_list = 0;
				bt = stream.readByte();
				boolean con_created = false;
				while (bt != ')') {
					bypass_value_for_binary(stream, mod, false, bt, streamUtf);
					bt = stream.readByte();
				}
				return '0';
			}
			String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
	}


/**
	Loads the data contained in the binary file to this SdaiModel (old version,
	valid up to the build 119 inclusively).
*/
	private long loadStream1(DataInput stream) throws SdaiException {
		long largest_identifier = -1;
		int index;
		int i, j;
		StreamUTF streamUtf = new StreamUTF();
		try {
//			byte begin = stream.readByte();
//			if (begin != 'D') {
//				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
//				throw new SdaiException(SdaiException.SY_ERR, base);
//			}
//			i = stream.readInt();
//			if (i < repository.session.valid_from) {
//				String base = SdaiSession.line_separator + AdditionalMessages.BF_OLD0 + repository.location + "." + name +
//					AdditionalMessages.BF_OLD1 + i + ". " + SdaiSession.line_separator +
//					AdditionalMessages.BF_OLD2 + repository.session.valid_from;
//				throw new SdaiException(SdaiException.SY_ERR, base);
//			}
//			underlying_build = i;
			if (underlying_build < SdaiSession.valid_from) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_OLD0 + repository.location + "." + name +
					AdditionalMessages.BF_OLD1 + underlying_build + ". " + SdaiSession.line_separator +
					AdditionalMessages.BF_OLD2 + SdaiSession.valid_from;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			CEntity_definition edef;
			AEntity_definition set;
			if (underlying_schema == SdaiSession.baseDictionarySchemaDef) {
				set = SdaiSession.bootEntityAggregate;
				initializeContents();
			} else {
				if (underlying_schema == null) {
					underlying_schema = dictionary.described_schema;
				}
				set = underlying_schema.getEntities();
			}
			int number_of_entities = ((AEntity)set).myLength;
			int n_entities_from_file = stream.readInt();
if (SdaiSession.debug2) System.out.println(" number_of_entities = " + number_of_entities +
"    n_entities_from_file = " + n_entities_from_file);
			if (to_entities == null) {
				to_entities = new int[number_of_entities + 1];
			}
			for (i = 0; i < n_entities_from_file; i++) {
				String type = streamUtf.readUTF(stream).toUpperCase();
				int n_instances = stream.readInt();
				index = find_entity(0, number_of_entities - 1, type, set);
				if (index < 0) {
if (SdaiSession.debug2) System.out.println("  ENTITY: " + type + "  number of instances = " + n_instances +
"   number_of_entities = " + number_of_entities +
"   underlying_schema: " + underlying_schema.getName(null) +
"  model: " + name);
					String base = SdaiSession.line_separator + AdditionalMessages.BF_WENT;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				to_entities[i] = index;
				lengths[index] = n_instances;
				instances_sim[index] = new CEntity[n_instances];
                sim_status[index] &= ~SIM_SORTED;
//System.out.println("   SdaiModel   for index = " + index + "  count of instances = " + n_instances);
			}


			// create all empty instances of all types
			CEntity instance;
if (SdaiSession.debug2) System.out.println(" NAME of this model: " + name);
			int ext_count;
			if (repository == SdaiSession.systemRepository) {
				ext_count = instances_sim.length;
			} else {
				ext_count = instances_sim.length - 1;
			}
			Object [] myDataA = null;
			if (((AEntity)set).myLength > 1) {
				myDataA = (Object [])((AEntity)set).myData;
			}
			for (i = 0; i < ext_count; i++) {
				if (lengths[i] == 0) continue;
				if (myDataA == null) {
					edef = (CEntity_definition)((AEntity)set).myData;
				} else {
					edef = (CEntity_definition)myDataA[i];
				}
				// locate parent schema of this type
				SdaiModel m;
				if (this == SdaiSession.baseDictionaryModel) {
					m = this;
				} else {
					m = ((CSchema_definition) edef.owning_model.described_schema).modelDictionary;
				}
				SSuper sup = m.schemaData.super_inst;
if (SdaiSession.debug2) System.out.println(" In: SdaiModel edef: " + edef.getCorrectName());
				Class entityClass = ((CEntityDefinition)edef).getEntityClass();
				CEntity instances_sim_i[] = instances_sim[i];
				for (j = 0; j < lengths[i]; j++) {
					instance = sup.makeInstance(entityClass, this, -1, 0);
					instances_sim_i[j] = instance;
					instance.owning_model = this;
				}
			}
			invalidate_quick_find();

			// load and set instance attributes
			n_ex_models = 0;
			n_ex_repositories = 0;
			n_ex_edefs = 0;
			CEntity [] instances;
			byte bt;
			StaticFields staticFields = StaticFields.get();
			for (i = 0; i < n_entities_from_file; i++) {
				index = to_entities[i];
				instances = instances_sim[index];
				for (j = 0; j < lengths[index]; j++) {
					bt = stream.readByte();
					if (bt != 'c' && bt != 's') {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					CEntity app_inst = instances[j];
if (SdaiSession.debug2)
if (app_inst == null) System.out.println(" app_inst is NULL  " + " index = " + index + "   i = "
+ i + "   j = " + j);
else System.out.println(" app_inst is POS");
					load_instance(staticFields, entity_values, stream, app_inst, n_entities_from_file, streamUtf);
if (SdaiSession.debug2) {System.out.println("  instance ident = " + app_inst.instance_identifier);
if (app_inst.getInstanceType() == null)
System.out.println(" SdaiModel   app_inst.getInstanceType() is NULL" + "   model: " + name);
else System.out.println(" SdaiModel   app_inst.getInstanceType() is POSITIVE" + "   model: " + name);}
					app_inst.setAll(entity_values);
					if (app_inst.instance_identifier > largest_identifier) {
						largest_identifier = app_inst.instance_identifier;
					}
					modified = false;
				}
			}
			bt = stream.readByte();
if (SdaiSession.debug) System.out.println(" ^^^^^ last byte = " + (char)bt);
			if (bt != 'E') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		sort_inside_extents();
		return largest_identifier;
	}


/**
	Prepares the array inst_idents consisting of identifiers of all instances
   contained in this model. The array is created by reading the beginning of
   the model's binary file. In the case of exceptions a warning message is
   printed and the array is made empty. This method is nontrivially executed
   only for binary files formed with build 249 or later. Otherwise, again the
   array is left empty.	The method is invoked in the following methods:
   openRepository, endReadOnlyAccess, endReadWriteAccess.
*/
	void loadInstanceIdentifiers() throws SdaiException {
		File handle = new File((String)repository.location, "m" + identifier);
		FileInputStream file;

		try {
			file = new FileInputStream(handle);
		} catch (FileNotFoundException ex) {
			String f_name = "m" + identifier;
			if (repository.session.logWriterSession != null) {
				repository.session.printlnSession(AdditionalMessages.BF_MIF0 + f_name + AdditionalMessages.BF_MIF1);
			} else {
				SdaiSession.println(AdditionalMessages.BF_MIF0 + f_name + AdditionalMessages.BF_MIF1);
			}
			all_inst_count = 0;
			return;
		}
		try {
			DataInput stream = new DataInputStream( new BufferedInputStream(file));
			try {
				loadInstanceIdentifiers(stream);
			} finally {
				((DataInputStream)stream).close();
				file.close();
			}
		} catch (IOException ex) {
			if (repository.session.logWriterSession != null) {
				repository.session.printlnSession(AdditionalMessages.BF_MNF1 + handle.getAbsolutePath() +
					AdditionalMessages.BF_MNF2);
			} else {
				SdaiSession.println(AdditionalMessages.BF_MNF1 + handle.getAbsolutePath() +
					AdditionalMessages.BF_MNF2);
			}
			all_inst_count = 0;
		}
	}


/**
*/
	protected void loadInstanceIdentifiersRemote() throws SdaiException {
        all_inst_count = 0;
        inst_idents = new long[(int)all_inst_count];
        /*
		if (stream == null) {
			if (repository.session.logWriterSession != null) {
				repository.session.printlnSession(AdditionalMessages.BF_RSTM);
			} else {
				SdaiSession.println(AdditionalMessages.BF_RSTM);
			}
			all_inst_count = 0;
			return;
		}
		try {
			try {
				//loadInstanceIdentifiers(stream);
                byte bt = stream.readByte();
                all_inst_count = stream.readLong();
                if (inst_idents == null || all_inst_count > inst_idents.length) {
                    inst_idents = new long[(int)all_inst_count];
                }
                for (int i = 0; i < all_inst_count; i++) {
                    inst_idents[i] = stream.readLong();
                    stream.skipBytes(4);
                }
			} finally {
				((DataInputStream)stream).close();
			}
		} catch (IOException ex) {
			if (repository.session.logWriterSession != null) {
				repository.session.printlnSession(AdditionalMessages.BF_RSTE);
			} else {
				SdaiSession.println(AdditionalMessages.BF_RSTE);
			}
			all_inst_count = 0;
		}
        */
	}


/**
	Prepares the array inst_idents consisting of identifiers of all instances
   contained in this model. The array is created by reading the beginning of
   the model's binary file. The method is invoked in loadInstanceIdentifiers()
   and loadInstanceIdentifiersSystemRepo.
*/
	void loadInstanceIdentifiers(DataInput stream) throws SdaiException, IOException {
		byte bt = stream.readByte();
		if (bt != 'D') {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		underlying_build = stream.readInt();
		if (underlying_build < 249) {
			all_inst_count = 0;
			return;
		}
		all_inst_count = stream.readLong();
		if (inst_idents == null || all_inst_count > inst_idents.length) {
			inst_idents = new long[(int)all_inst_count];
		}
		for (int i = 0; i < all_inst_count; i++) {
			inst_idents[i] = stream.readLong();
		}
		bt = stream.readByte();
		if (bt != 'S') {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	Prepares an instance of the class ComplexEntityValue to store
	the values of an entity instance.
	Used in methods substituteInstance and saveStream.
*/
	void prepareAll(ComplexEntityValue entity_values, CEntity_definition def) throws SdaiException {
		int i;
		entity_values.def = def;
		if (entity_values.entityValues.length < def.noOfPartialEntityTypes) {
			entity_values.enlarge(def.noOfPartialEntityTypes);
		} else {
			for (i = 0; i < def.noOfPartialEntityTypes; i++) {
				if (entity_values.entityValues[i] == null) {
					entity_values.entityValues[i] = new EntityValue(repository.session);
				}
			}
		}
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			CEntity_definition partial_def = def.partialEntityTypes[i];
			if (pval.values == null) {
				if (SdaiSession.NUMBER_OF_VALUES >= partial_def.noOfPartialAttributes) {
					pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
				} else {
					pval.values = new Value[partial_def.noOfPartialAttributes];
				}
			} else if (pval.values.length < partial_def.noOfPartialAttributes) {
				pval.enlarge(partial_def.noOfPartialAttributes);
			}
			for (int j = 0; j < partial_def.noOfPartialAttributes; j++) {
				if (pval.values[j] == null) {
					pval.values[j] = new Value();
				}
			}
			pval.count = partial_def.noOfPartialAttributes;
			pval.def = partial_def;
		}
	}


/**
	Replaces the incoming connectors by references to instances or by unset value.
*/
	void resolveInConnectors(boolean unset) throws SdaiException {
		boolean aborting = !unset &&
			(repository.session.active_transaction != null ?
			repository.session.active_transaction
			.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING : false);
		Connector con = inConnectors;
/*System.out.println(" SdaiModel  for resolveInConnectors: " + name);*/
		while (con != null) {
/*if (repository != SdaiSession.systemRepository)*/
/*con.print_connector();*/
			if (con.resolveModelIn() != this) {
				String base = SdaiSession.line_separator + AdditionalMessages.RE_MINC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			Connector nextConnector = con.nextIn;
			if(nextConnector == inConnectors) {
				nextConnector = null;
			}
			replaceByReference(con, unset, aborting);
			con = nextConnector;
		}
	}


	void removeInConnectors() throws SdaiException {
		Connector con = inConnectors;
		while (con != null) {
			Connector nextConnector = con.nextIn;
			if(nextConnector == inConnectors) {
				nextConnector = null;
			}
			con.disconnect();
			con = nextConnector;
		}
	}


/*	private void replaceByReference(Connector con, boolean unset) throws SdaiException {
		CEntity owner = con.owning_instance;
		SdaiModel host_model = owner.owning_model;
		if (!unset) {
			host_model.reference = owner.resolveConnector(con, true);
		} else {
			con.disconnect();
		}
		owner.changeReferences(con, host_model.reference);  // LK?: is this really correct??? GP: Yes!
		host_model.reference = null;
	} */
/**
	Replaces the specified incoming connector by a reference to an instance or by unset value.
*/
	private void replaceByReference(Connector con, boolean unset, boolean aborting) throws SdaiException {
		CEntity owner = con.owning_instance;
		CEntity reference = null;
		if (unset || (aborting && owner.owning_model.modified)) {
			con.disconnect();
		} else {
//System.out.println(" SdaiModel   owner: #" + owner.instance_identifier +
//"  owning_model: " + owner.owning_model.name);
			reference = con.resolveConnector(true, false, aborting);
			if (reference == null) return;
		}
/*if (repository != SdaiSession.systemRepository)
System.out.println(" SdaiModel  changeReferences invoked for: #" + owner.instance_identifier);
if (repository != SdaiSession.systemRepository)
System.out.println(" SdaiModel  ~~~~~~ model: " + owner.owning_model.name +
"     modified: " + owner.owning_model.modified);*/
		owner.changeReferences(con, reference);
//		if (repository != SdaiSession.systemRepository) {
//			owner.owning_model.modified = true;
//		}
/*if (repository != SdaiSession.systemRepository)
System.out.println(" SdaiModel  ~~~~~~ after model: " + owner.owning_model.name +
"     modified: " + owner.owning_model.modified);*/
	}

/**
	Replaces the incoming connectors by references to instances or by unset value
	for partial access model.
*/
	private void resolveInConnectorsPartial(boolean unset) throws SdaiException {
		boolean aborting = !unset &&
			(repository.session.active_transaction != null ?
			repository.session.active_transaction
			.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING : false);
		Connector con = inConnectors;
		while (con != null) {
			if (con.resolveModelIn() != this) {
				String base = SdaiSession.line_separator + AdditionalMessages.RE_MINC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			Connector nextConnector = con.nextIn;
			if(nextConnector == inConnectors) {
				nextConnector = null;
			}
			CEntity owner = con.owning_instance;
			if(unset || (aborting && owner.owning_model.modified)) {
				con.disconnect();
				owner.changeReferences(con, null);
			} else {
				CEntity reference = con.resolveConnector(true, false, aborting);
				if(reference != null) {
					owner.changeReferences(con, reference);
				}
			}
			con = nextConnector;
		}
	}


/**
	Sorts instances (separately of each entity data type) in increasing order
	of their identifiers.
*/
	void sort_inside_extents() {
		prepareForSorting();
		for (int k = 0; k < lengths.length; k++) {
			if (lengths[k] >1) {
				sortInstances(k);
			}
			//sorted[k] = true;
            sim_status[k] |= SIM_SORTED;
		}
		closeSorting();
	}


/**
	Enlarges an array (a row of 'instances_sim') allocated for
	storing instances of the specified (by parameter 'index') entity data type.
	This method is invoked in makeInstance method in class SSuper.
*/
	void ensureCapacity(int index) {
		int new_length;
		if (instances_sim[index].length == 0) {
			new_length = INSTANCE_ARRAY_INITIAL_SIZE;
		} else {
			if (lengths[index] < INSTANCE_ARRAY_SIZE_STEP) {
				new_length = lengths[index] * 2;
			} else {
				new_length = lengths[index] + INSTANCE_ARRAY_SIZE_STEP;
			}
		}
		CEntity[] new_array = new CEntity[new_length];
		System.arraycopy(instances_sim[index], 0, new_array, 0, lengths[index]);
		instances_sim[index] = new_array;
	}


/**
	Writes identifier and attribute values of an entity instance to the binary file.
*/
	void save_instance(CEntity owning_instance, ComplexEntityValue entity_values, DataOutput stream,
			int def_types_count, int to_type) throws java.io.IOException, SdaiException {
		CEntity_definition def = entity_values.def;
		stream.writeByte('c');
		stream.writeShort((short)to_type);
//		stream.writeLong(owning_instance.instance_identifier);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			if (pval == null) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_ERVC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (int j = 0; j < pval.count; j++) {
				Value val = pval.values[j];
				boolean res = explore_value_for_binary(owning_instance, val, stream, def_types_count);
				if (!res) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_FVAL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
	}


/**
	Writes identifier and attribute values of an entity instance to the binary stream.
*/
    private void save_instance_remote(CEntity owning_instance, ComplexEntityValue entity_values, DataOutput stream, int version) throws java.io.IOException, SdaiException {
		CEntity_definition def = entity_values.def;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			if (pval == null) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_ERVC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (int j = 0; j < pval.count; j++) {
                Value val = pval.values[j];
                boolean res = explore_value_for_binary_remote(owning_instance, val, dos, version);
				if (!res) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_FVAL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}

			}
		}
        stream.writeInt(baos.size());
        //stream.println(baos.size());
        //stream.write(baos);
        baos.writeTo((OutputStream)stream);
	}



/**
	Loads identifier and attribute values of an entity instance from the
	binary file (new version of the method).
*/
	private void load_instance(ComplexEntityValue inst, DataInput stream, CEntity owning_instance,
							   CEntity_definition def, int n_entities_from_file, int def_types_count,
							   int version, int spec_type_index, boolean mod_small, StreamUTF streamUtf)
			throws java.io.IOException, SdaiException {
		int i, j;
		CEntityDefinition def_ = def;
//if (repository != SdaiSession.systemRepository)
//System.out.println(" SdaiModel   def: " + def.getName(null) + "  inst: #" + owning_instance.instance_identifier);
		inst.def = def;
		if (version <= 1) {
			long ident = stream.readLong();
			if (ident <= 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (CHECK_ID && repository.checkInstanceIdentifier(ident, this)) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DIID + ident;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			owning_instance.instance_identifier = ident;
		}
		if (this == SdaiSession.baseDictionaryModel) {
			def_.preparePartialTypes();
		}
		if (def.noOfPartialEntityTypes > inst.entityValues.length) {
			inst.enlarge(def.noOfPartialEntityTypes);
		} else {
			for (i = 0; i < def.noOfPartialEntityTypes; i++) {
				if (inst.entityValues[i] == null) {
					inst.entityValues[i] = new EntityValue(repository.session);
				}
			}
		}
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			CEntityDefinition partial_def = def.partialEntityTypes[i];
			inst.entityValues[i].def = (CEntity_definition)partial_def;
			int count_of_values = partial_def.noOfPartialAttributes;
			if (spec_type_index >= 0 && dummy_attrs[spec_type_index][i] > 0) {
				read_dummies(dummy_attrs[spec_type_index][i], stream, owning_instance,
					n_entities_from_file, def_types_count, mod_small, streamUtf);
			}
//if (repository != SdaiSession.systemRepository)
//System.out.println(" SdaiModel ??? def.noOfPartialEntityTypes: " + def.noOfPartialEntityTypes +
//"  prt_def: " + ((CEntity_definition)partial_def).getName(null) + "   count_of_values: " + count_of_values +
//"  dummy_attrs[spec_type_index][i]: " + dummy_attrs[spec_type_index][i]);
			if (count_of_values <= 0) {
				if (spec_type_index >= 0 && type2type[spec_type_index][i] >= 0) {
					load_dummy_values(type2type[spec_type_index][i], stream, owning_instance,
						n_entities_from_file, def_types_count, mod_small, streamUtf);
				}
				inst.entityValues[i].count = 0;
				continue;
			}
			EntityValue pval = inst.entityValues[i];
			if (pval.values == null) {
				if (SdaiSession.NUMBER_OF_VALUES >= count_of_values) {
					pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
				} else {
					pval.values = new Value[count_of_values];
				}
			} else if (count_of_values > pval.values.length) {
				pval.enlarge(count_of_values);
			}
//if (spec_type_index >= 0 && repository != SdaiSession.systemRepository) {
//System.out.println(" SdaiModel +++++   partial_def: " + ((CEntity_definition)partial_def).getName(null) +
//"  i=" + i + "   type2type[i]: " + type2type[spec_type_index][i]);}
			if (spec_type_index < 0 || type2type[spec_type_index][i] == -1) {
				int k = 0;
				for (j = 0; j < count_of_values; j++) {
					if (pval.values[k] == null) {
						pval.values[k] = new Value();
					}
					Value val = pval.values[k];
					try {
						extract_value_for_binary(val, stream, owning_instance,
							n_entities_from_file, def_types_count, true, (byte)' ', mod_small, streamUtf);
					} catch (ArrayIndexOutOfBoundsException ex) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					k++;
				}
				pval.count = k;
			} else if (type2type[spec_type_index][i] == -2) {
				for (j = 0; j < count_of_values; j++) {
					if (pval.values[j] == null) {
						pval.values[j] = new Value();
					}
					pval.values[j].tag = PhFileReader.MISSING;
				}
				pval.count = count_of_values;
			} else {
				load_incomp_partial(pval, count_of_values, type2type[spec_type_index][i], stream,
					owning_instance, n_entities_from_file, def_types_count, mod_small, streamUtf);
			}
		}
		if (spec_type_index >= 0 && dummy_attrs[spec_type_index][def.noOfPartialEntityTypes] > 0) {
			read_dummies(dummy_attrs[spec_type_index][def.noOfPartialEntityTypes], stream,
				owning_instance, n_entities_from_file, def_types_count, mod_small, streamUtf);
		}
	}


	private void read_dummies(int dummy_count, DataInput stream, CEntity owning_instance,
			int n_entities_from_file, int def_types_count, boolean mod_small, StreamUTF streamUtf)
			throws java.io.IOException, SdaiException {
		if (dummy_val == null) {
			dummy_val = new Value();
		}
		for (int i = 0; i < dummy_count; i++) {
			try {
				extract_value_for_binary(dummy_val, stream, owning_instance,
					n_entities_from_file, def_types_count, true, (byte)' ', mod_small, streamUtf);
			} catch (ArrayIndexOutOfBoundsException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
	}


	private void load_incomp_partial(EntityValue pval, int count_of_values, int simple_ind, DataInput stream,
					CEntity owning_instance, int n_entities_from_file,
					int def_types_count, boolean mod_small, StreamUTF streamUtf)
			throws java.io.IOException, SdaiException {
		for (int j = 0; j < count_of_values; j++) {
			if (pval.values[j] == null) {
				pval.values[j] = new Value();
			}
			pval.values[j].tag = PhFileReader.MISSING;
		}
		for (int i = 0; i < value2attr[simple_ind].length; i++) {
			int index = value2attr[simple_ind][i];
			Value val;
			if (index < 0) {
				if (dummy_val == null) {
					dummy_val = new Value();
				}
				val = dummy_val;
			} else {
				val = pval.values[index];
			}
			try {
				extract_value_for_binary(val, stream, owning_instance,
					n_entities_from_file, def_types_count, true, (byte)' ', mod_small, streamUtf);
			} catch (ArrayIndexOutOfBoundsException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		pval.count = count_of_values;
	}


	private void load_dummy_values(int simple_ind, DataInput stream, CEntity owning_instance,
					int n_entities_from_file, int def_types_count, boolean mod_small, StreamUTF streamUtf)
				throws java.io.IOException, SdaiException {
		if (dummy_val == null) {
			dummy_val = new Value();
		}
		for (int i = 0; i < value2attr[simple_ind].length; i++) {
			try {
				extract_value_for_binary(dummy_val, stream, owning_instance,
					n_entities_from_file, def_types_count, true, (byte)' ', mod_small, streamUtf);
			} catch (ArrayIndexOutOfBoundsException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
	}


/**
	Loads identifier and attribute values of an entity instance from the
	binary file (old version of the method).
*/
	private void load_instance(StaticFields staticFields, ComplexEntityValue inst, DataInput stream,
							   CEntity owning_instance, int n_entities_from_file, StreamUTF streamUtf)
		throws java.io.IOException, SdaiException {
		int i;
		inst.def = (CEntity_definition)owning_instance.getInstanceType();
		int ident = stream.readInt();
		if (ident <= 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
/*if (SdaiSession.debug3)*/
//if (name.equals("ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN_DICTIONARY_DATA"))
//System.out.println(" SdaiModel   model: " + name + "   inst: #" + ident );
		owning_instance.instance_identifier = ident;
		int count_of_types = stream.readInt();
		if (count_of_types > inst.entityValues.length) {
			inst.enlarge(count_of_types);
		} else {
			for (i = 0; i < count_of_types; i++) {
				if (inst.entityValues[i] == null) {
					inst.entityValues[i] = new EntityValue(repository.session);
				}
			}
		}
		for (i = 0; i < count_of_types; i++) {
			int count_of_values = stream.readInt();
			if (count_of_values > 0) {
				EntityValue pval = inst.entityValues[i];
				if (pval.values == null) {
					if (SdaiSession.NUMBER_OF_VALUES >= count_of_values) {
						pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
					} else {
						pval.values = new Value[count_of_values];
					}
				} else if (count_of_values > pval.values.length) {
					pval.enlarge(count_of_values);
				}
				int k = 0;
				for (int j = 0; j < count_of_values; j++) {
					if (pval.values[k] == null) {
						pval.values[k] = new Value();
					}
					Value val = pval.values[k];
					try {
						extract_value_for_binary(staticFields, val, stream, owning_instance,
							n_entities_from_file, true, (byte)' ', streamUtf);
					} catch (ArrayIndexOutOfBoundsException ex) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					k++;
				}
				pval.count = k;
			} else {
				inst.entityValues[i].count = 0;
			}
		}
	}



/**
	Loads identifier and attribute values of an entity instance from the binary stream.
*/
    private void load_instance_remote(ComplexEntityValue inst, DataInput stream, CEntity owning_instance,
    		CEntity_definition def, StreamUTF streamUtf, CEntity[][] inst_ces, boolean connectMissingRef)
			throws java.io.IOException, SdaiException {
//		CEntity_definition def = (CEntity_definition)owning_instance.getInstanceType();
		CEntityDefinition def_ = def;
		inst.def = def;

        //ps.println("#"+owning_instance.instance_identifier+"=");

		if (this == SdaiSession.baseDictionaryModel) {
			def_.preparePartialTypes();
		}
		if (def.noOfPartialEntityTypes > inst.entityValues.length) {
			inst.enlarge(def.noOfPartialEntityTypes);
		} else {
			for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
				if (inst.entityValues[i] == null) {
					inst.entityValues[i] = new EntityValue(repository.session);
				}
			}
		}
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			CEntityDefinition partial_def = def.partialEntityTypes[i];
			inst.entityValues[i].def = (CEntity_definition)partial_def;
			int count_of_values = partial_def.noOfPartialAttributes;
			if (count_of_values > 0) {
				EntityValue pval = inst.entityValues[i];
				if (pval.values == null) {
					if (SdaiSession.NUMBER_OF_VALUES >= count_of_values) {
						pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
					} else {
						pval.values = new Value[count_of_values];
					}
				} else if (count_of_values > pval.values.length) {
					pval.enlarge(count_of_values);
				}
				int k = 0;
				for (int j = 0; j < count_of_values; j++) {
					if (pval.values[k] == null) {
						pval.values[k] = new Value();
					}
					Value val = pval.values[k];
					try {
						extract_value_for_binary_remote(val, stream, owning_instance,
														true, (byte)' ', streamUtf, inst_ces, connectMissingRef);
                        //ps.println();
					} catch (ArrayIndexOutOfBoundsException ex) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
						throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, base)).initCause(ex);
					}
					k++;
				}
				pval.count = k;
			} else {
				inst.entityValues[i].count = 0;
			}
		}
	}


/**
	Writes value of an attribute of the specified entity instance to the binary file.
*/
	private boolean explore_value_for_binary(CEntity owning_instance, Value val, DataOutput stream,
			int def_types_count) throws java.io.IOException, SdaiException {
		Value value_next;
		SdaiModel model;

		switch (val.tag) {
			case PhFileReader.MISSING:
				stream.writeByte('$');
				return true;
			case PhFileReader.REDEFINE:
				stream.writeByte('*');
				return true;
			case PhFileReader.INTEGER:
				stream.writeByte('i');
				stream.writeInt(val.integer);
				return true;
			case PhFileReader.REAL:
				stream.writeByte('r');
				stream.writeDouble(val.real);
				return true;
			case PhFileReader.BOOLEAN:
				if (val.integer == 0) {
					stream.writeByte('f');
				} else if (val.integer == 1) {
					stream.writeByte('t');
				}
				return true;
			case PhFileReader.LOGICAL:
				if (val.integer == 0) {
					stream.writeByte('f');
				} else if (val.integer == 1) {
					stream.writeByte('t');
				} else {
					stream.writeByte('u');
				}
				return true;
			case PhFileReader.ENUM:
				stream.writeByte('e');
				stream.writeUTF(val.string);
				return true;
			case PhFileReader.STRING:
				stream.writeByte('s');
				stream.writeUTF(val.string);
				return true;
			case PhFileReader.BINARY:
//				stream.writeByte('b');
				stream.writeByte('B');
				Binary bnr = (Binary)val.reference;
				long bin_count = (long)bnr.value.length;
				stream.writeLong(bin_count + 1);
				stream.writeByte((byte)bnr.unused);
				stream.write(bnr.value);
//if (owning_instance.instance_identifier==4001) System.out.println("SdaiModel:  Before BINARY ");System.out.flush();
//String bstr = ((Binary)val.reference).toString();
//if (owning_instance.instance_identifier==4001) System.out.println("SdaiModel:  After toString ");System.out.flush();
//				stream.writeUTF(((Binary)val.reference).toString());
//if (owning_instance.instance_identifier==4001) System.out.println("SdaiModel:  After BINARY ");System.out.flush();
				return true;
			case PhFileReader.TYPED_PARAMETER:
				stream.writeByte('p');
				int index2type = repository.session.find_string(0, def_types_count - 1, val.string, type_names);
				if (index2type < 0) {
//System.out.println("SdaiModel   def_types_count = " + def_types_count +
//"   val.string: " + val.string);
//for (int i = 0; i < def_types_count; i++) {
//System.out.println("SdaiModel   i = " + i + "   type_names: " + type_names[i]);
//}
					throw new SdaiException(SdaiException.SY_ERR);
				}
				stream.writeShort((short)index2type);
				value_next = val.nested_values[0];
				return explore_value_for_binary(owning_instance, value_next, stream, def_types_count);
			case PhFileReader.ENTITY_REFERENCE:
				model = owning_instance.owning_model;
				boolean first_entity = false;
				int ex_edef_index = -1;
				if (val.reference instanceof Connector) {
					Connector con = (Connector)val.reference;
					SdaiRepository repos = model.repository;

					boolean first_model = true;
					int ex_model_index = -1;
					for (int i = 0; i < model.n_ex_models; i++) {
						if (model.ex_model_names[i].equals(getNameForModel(con.resolveModelIn()))) {
							first_model = false;
							ex_model_index = i;
							break;
						}
					}
					if (first_model) {
						if (model.n_ex_models >= model.ex_model_names.length) {
							model.ex_model_names = repos.session.ensureStringsCapacity(model.ex_model_names);
						}
						model.ex_model_names[model.n_ex_models] = getNameForModel(con.resolveModelIn());
						ex_model_index = model.n_ex_models;
						model.n_ex_models++;
					}
					String entity_name = con.getEntityNameUpperCase();
					SchemaData sch_data = model.underlying_schema.modelDictionary.schemaData;
					int res = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
					if (res >= 0) {
						res = model.to_entities[res];
					}
					first_entity = true;
					ex_edef_index = -1;
					if (res < 0) {
						for (int i = 0; i < model.n_ex_edefs; i++) {
							if (model.ex_edefs[i].equals(entity_name)) {
								first_entity = false;
								ex_edef_index = i;
								break;
							}
						}
						if (first_entity) {
							if (model.n_ex_edefs >= model.ex_edefs.length) {
								model.ex_edefs = repos.session.ensureStringsCapacity(model.ex_edefs);
							}
							model.ex_edefs[model.n_ex_edefs] = entity_name;
							ex_edef_index = model.n_ex_edefs;
							model.n_ex_edefs++;
						}
					}

					if (getRepositoryForModel(con.resolveModelIn()) == repos
						|| getRepositoryNameForModel(con.resolveModelIn()) == null) {

						if (first_model) {
							stream.writeByte('2');
							stream.writeUTF(getNameForModel(con.resolveModelIn()));

						} else {
							stream.writeByte('3');
							stream.writeShort((short)ex_model_index);
						}
					} else {
						boolean first_repos = true;
						int ex_repos_index = -1;
						for (int i = 0; i < model.n_ex_repositories; i++) {
							if (model.ex_repositories[i].equals(getNameForModel(con.resolveModelIn()))) {
								first_repos = false;
								ex_repos_index = i;
								break;
							}
						}
						if (first_repos) {
							if (model.n_ex_repositories >= model.ex_repositories.length) {
								model.ex_repositories = repos.session.ensureStringsCapacity(model.ex_repositories);
							}
							model.ex_repositories[model.n_ex_repositories] =
								getRepositoryNameForModel(con.resolveModelIn());
							stream.writeByte('4');
							stream.writeUTF(getRepositoryNameForModel(con.resolveModelIn()));
							stream.writeUTF(getNameForModel(con.resolveModelIn()));
							model.n_ex_repositories++;
						} else {
							if (first_model) {
								stream.writeByte('5');
								stream.writeShort((short)ex_repos_index);
								stream.writeUTF(getNameForModel(con.resolveModelIn()));
							} else {
								stream.writeByte('6');
								stream.writeShort((short)ex_repos_index);
								stream.writeShort((short)ex_model_index);
							}
						}
					}
					if (res >= 0) {
						stream.writeByte('1');
						stream.writeShort((short)res);
					} else {
						if (first_entity) {
							stream.writeByte('2');
							stream.writeUTF(entity_name);
						} else {
							stream.writeByte('3');
							stream.writeShort((short)ex_edef_index);
						}
					}
					stream.writeLong((long)con.instance_identifier);
					return true;
				}

				CEntity inst = (CEntity)val.reference;
				String entity_name = ((CEntityDefinition)inst.getInstanceType()).getNameUpperCase();
//if ( model.schemaData == null) System.out.println("  schemaData is NULL  model: " + name);
//else System.out.println("  schemaData is POS  model: " + name);
				SchemaData sch_data = model.underlying_schema.modelDictionary.schemaData;
				int res = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
				if (res >= 0) {
					res = model.to_entities[res];
				}
//if (res < 0) {
//System.out.println("SdaiModel: " + name + "     res_init = " + res_init +
//"   underlying schema: " + model.underlying_schema.getName(null));
//System.out.println("  ENTITY in SdaiModel: " + entity_name);
//for (int d = 0; d < sch_data.entities.length; d++) {
//CEntity_definition enttt = (CEntity_definition)sch_data.entities[d];
//String nnn = enttt.getNameUpperCase();
//System.out.println("  index = " + d + "    NAME: " + nnn +
//"  inst: " + enttt + "   to_entities: " + to_entities[d]);}
//}
				if (model == inst.owning_model) {
					stream.writeByte('1');
					if (res < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR +
							". Enitity type " + entity_name + " not found";
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					stream.writeShort((short)res);
					//stream.writeInt(inst.instance_position);
                    stream.writeInt(inst.instance_position & CEntity.POS_MASK);    //--VV--
				} else {
					SdaiRepository rep_instance = model.repository;
					if (inst.owning_model == null) {
						throw new SdaiException(SdaiException.EI_NVLD, inst,
												"Instance #" + inst.instance_identifier +
												" referenced from instance " + owning_instance);
					}
					SdaiRepository rep_attribute = inst.owning_model.repository;
					boolean first_model = true;
					int ex_model_index = -1;
					for (int i = 0; i < model.n_ex_models; i++) {
						if (model.ex_model_names[i].equals(inst.owning_model.name)) {
							first_model = false;
							ex_model_index = i;
							break;
						}
					}
					if (first_model) {
						if (model.n_ex_models >= model.ex_model_names.length) {
							model.ex_model_names = rep_instance.session.ensureStringsCapacity(model.ex_model_names);
						}
						model.ex_model_names[model.n_ex_models] = inst.owning_model.name;
						ex_model_index = model.n_ex_models;
						model.n_ex_models++;
					}

					first_entity = true;
					ex_edef_index = -1;
					if (res < 0) {
						for (int i = 0; i < model.n_ex_edefs; i++) {
							if (model.ex_edefs[i].equals(entity_name)) {
								first_entity = false;
								ex_edef_index = i;
								break;
							}
						}
						if (first_entity) {
							if (model.n_ex_edefs >= model.ex_edefs.length) {
								model.ex_edefs = rep_instance.session.ensureStringsCapacity(model.ex_edefs);
							}
							model.ex_edefs[model.n_ex_edefs] = entity_name;
							ex_edef_index = model.n_ex_edefs;
							model.n_ex_edefs++;
						}
					}

					if (rep_instance == rep_attribute) {
						if (first_model) {
							stream.writeByte('2');
							stream.writeUTF(inst.owning_model.name);
						} else {
							stream.writeByte('3');
							stream.writeShort((short)ex_model_index);
						}
					} else {
						boolean first_repos = true;
						int ex_repos_index = -1;
						for (int i = 0; i < model.n_ex_repositories; i++) {
							if (model.ex_repositories[i].equals(rep_attribute.name)) {
								first_repos = false;
								ex_repos_index = i;
								break;
							}
						}
						if (first_repos) {
							if (model.n_ex_repositories >= model.ex_repositories.length) {
								model.ex_repositories =
									rep_instance.session.ensureStringsCapacity(model.ex_repositories);
							}
							model.ex_repositories[model.n_ex_repositories] = rep_attribute.name;
							stream.writeByte('4');
							stream.writeUTF(rep_attribute.name);
							stream.writeUTF(inst.owning_model.name);
							model.n_ex_repositories++;
						} else {
							if (first_model) {
								stream.writeByte('5');
								stream.writeShort((short)ex_repos_index);
								stream.writeUTF(inst.owning_model.name);
							} else {
								stream.writeByte('6');
								stream.writeShort((short)ex_repos_index);
								stream.writeShort((short)ex_model_index);
							}
						}
					}
				}
				if (model != inst.owning_model) {
					if (res >= 0) {
						stream.writeByte('1');
						stream.writeShort((short)res);
					} else {
						if (first_entity) {
							stream.writeByte('2');
							stream.writeUTF(entity_name);
						} else {
							stream.writeByte('3');
							stream.writeShort((short)ex_edef_index);
						}
					}
					stream.writeLong((long)inst.instance_identifier);
				}
				return true;
			case PhFileReader.EMBEDDED_LIST:
				stream.writeByte('(');
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					boolean result = explore_value_for_binary(owning_instance, value_next, stream, def_types_count);
					if (!result) {
						return false;
					}
				}
				stream.writeByte(')');
				return true;
			}
		return false;
	}


/**
	Writes value of an attribute of the specified entity instance to the binary stream.
*/
    private boolean explore_value_for_binary_remote(CEntity owning_instance, Value val, DataOutput stream, int version) throws java.io.IOException, SdaiException {
		Value value_next;
		SdaiModel model;

		switch (val.tag) {
			case PhFileReader.MISSING:
				stream.writeByte('$');
				return true;
			case PhFileReader.REDEFINE:
				stream.writeByte('*');
				return true;
			case PhFileReader.INTEGER:
				stream.writeByte('i');
				stream.writeInt(val.integer);
				return true;
			case PhFileReader.REAL:
				stream.writeByte('r');
				stream.writeDouble(val.real);
				return true;
			case PhFileReader.BOOLEAN:
				if (val.integer == 0) {
					stream.writeByte('f');
				} else if (val.integer == 1) {
					stream.writeByte('t');
				}
				return true;
			case PhFileReader.LOGICAL:
				if (val.integer == 0) {
					stream.writeByte('f');
				} else if (val.integer == 1) {
					stream.writeByte('t');
				} else {
					stream.writeByte('u');
				}
				return true;
			case PhFileReader.ENUM:
				stream.writeByte('e');
				stream.writeUTF(val.string);
				return true;
			case PhFileReader.STRING:
				stream.writeByte('s');
				stream.writeUTF(val.string);
				return true;
			case PhFileReader.BINARY:
				if(version > 1) {
					stream.writeByte('B');
					Binary bnr = (Binary)val.reference;
					long bin_count = (long)bnr.value.length;
					stream.writeLong(bin_count + 1);
					stream.writeByte((byte)bnr.unused);
					stream.write(bnr.value);
				} else {
					stream.writeByte('b');
					stream.writeUTF(((Binary)val.reference).toString());
				}
				return true;
			case PhFileReader.TYPED_PARAMETER:
				stream.writeByte('p');
                stream.writeUTF(val.string);
				value_next = val.nested_values[0];
				return explore_value_for_binary_remote(owning_instance, value_next, stream, version);
			case PhFileReader.ENTITY_REFERENCE:
                model = owning_instance.owning_model;
                boolean bMissing = false;
				if (val.reference instanceof Connector) {
					Connector con = (Connector)val.reference;
					SdaiRepository repos = model.repository;
					//String entity_name = con.entity_name.toUpperCase();

					SerializableRef conModelInRef = con.getModelInRef();
					if (conModelInRef.getRepositoryId() == repos.getRepoRemote().getRemoteId()) {

                        stream.writeByte('2');
                        //System.out.println("Reference type = 2 Connector");
                        stream.writeLong(conModelInRef.getModelId());
                        //System.out.println("\t model id = " + con.modelIn.modRemote.getId());
					} else {
                        stream.writeByte('3');
                        //System.out.println("Reference type = 3 Connector");
                        stream.writeLong(conModelInRef.getRepositoryId());
                        stream.writeLong(conModelInRef.getModelId());
                        //System.out.println("\t repository id = " + con.modelIn.repository.repoRemote.getId());
                        //System.out.println("\t model id = " + con.modelIn.modRemote.getId());
					}
					stream.writeLong((long)con.instance_identifier);
					return true;
				}

				CEntity inst = (CEntity)val.reference;
				String entity_name = ((CEntityDefinition)inst.getInstanceType()).getNameUpperCase();
				SchemaData sch_data = model.underlying_schema.modelDictionary.schemaData;
				int res = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
				if (model == inst.owning_model) {
					stream.writeByte('1');
					if (res < 0) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				} else {
					SdaiRepository rep_instance = model.repository;
					if (inst.owning_model == null) {
						throw new SdaiException(SdaiException.EI_NVLD, inst);
					}
					SdaiRepository rep_attribute = inst.owning_model.repository;

					if (rep_instance == rep_attribute) {
                        stream.writeByte('2');
                        //System.out.println("Reference type = 2");
                        if (inst.owning_model.getModRemote() == null) {
                            inst.owning_model.committing(false);
                        }
                        stream.writeLong(inst.owning_model.getModRemote().getRemoteId());
                        //System.out.println("\t model id = " + inst.owning_model.modRemote.getId());
					} else {
						SdaiRepositoryRemote attrRepoRemote = rep_attribute.getRepoRemote();
                        if (attrRepoRemote != null) {
                            stream.writeByte('3');
                            //System.out.println("Reference type = 3");
                            stream.writeLong(attrRepoRemote.getRemoteId());
                            if (inst.owning_model.getModRemote() == null) {
                                inst.owning_model.committing(false);
                            }
                            stream.writeLong(inst.owning_model.getModRemote().getRemoteId());
                            //System.out.println("\t repository id = " + rep_attribute.repoRemote.getId());
                            //System.out.println("\t model id = " + inst.owning_model.modRemote.getId());
                        }
                        else {
                            stream.writeByte('$');
                            bMissing = true;
                        }
					}
				}
                if (!bMissing) {
					stream.writeLong((long)inst.instance_identifier);
				}
				return true;
			case PhFileReader.EMBEDDED_LIST:
				stream.writeByte('(');
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					boolean result = explore_value_for_binary_remote(owning_instance, value_next, stream, version);
					if (!result) {
						return false;
					}
				}
				stream.writeByte(')');
				return true;
			}
		return false;
	}


/**
	Loads the value of an attribute of the specified entity instance from the
	binary file (new version of the method).
*/
	private boolean extract_value_for_binary(Value val, DataInput stream,
		CEntity owning_instance, int n_entities_from_file, int def_types_count,
		boolean byte_needed, byte sym, boolean mod_small, StreamUTF streamUtf)
				throws java.io.IOException, SdaiException, ArrayIndexOutOfBoundsException {
		byte bt;
		int index;
		int id_index;
		long ident;
		AEntity_definition set;
		String rep_name;
		String mod_name;
		String ent_name;
		Connector con;
		SdaiModel mod = owning_instance.owning_model;
		SdaiModel ref_mod;
		SdaiRepository repo = mod.repository;
		SdaiSession ses = repo.session;
		byte token;
		boolean mod_aborting;
		SdaiTransaction trans;

		if (byte_needed) {
			token = stream.readByte();
		} else {
			token = sym;
		}
//if (owning_instance.instance_identifier == 67)
//System.out.println(" SdaiModel *****  owning_instance :" + owning_instance.instance_identifier + "   token: " + (char)token);
		Object [] myDataA = null;
		switch (token) {
			case '$':
				val.tag = PhFileReader.MISSING;
				return false;
			case '*':
				val.tag = PhFileReader.REDEFINE;
				return false;
			case 'i':
				val.tag = PhFileReader.INTEGER;
				val.integer = stream.readInt();
				return false;
			case 'r':
				val.tag = PhFileReader.REAL;
				val.real = stream.readDouble();
				return false;
			case 'f':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 0;
				return false;
			case 't':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 1;
				return false;
			case 'u':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 2;
				return false;
			case 'e':
				val.tag = PhFileReader.ENUM;
				val.string = streamUtf.readUTF(stream)/*.intern()*/;
				return false;
			case 's':
				val.tag = PhFileReader.STRING;
				val.string = streamUtf.readUTF(stream)/*.intern()*/;
				return false;
			case 'b':
				val.tag = PhFileReader.BINARY;
				val.string = streamUtf.readUTF(stream);
				val.reference = new Binary(val.string);
				return false;
			case 'B':
				val.tag = PhFileReader.BINARY;
				Binary bnr = new Binary();
				long bt_count = stream.readLong();
				bnr.unused = stream.readByte();
				bnr.value = new byte[(int)bt_count - 1];
				stream.readFully(bnr.value);
				val.reference = bnr;
				return false;
			case 'p':
				val.tag = PhFileReader.TYPED_PARAMETER;
				int index2type = stream.readShort();
				if (index2type < 0 || index2type >= def_types_count) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				val.string = type_names[index2type];
				val.integer = 1;
				bt = stream.readByte();
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (val.nested_values[0] == null) {
					val.nested_values[0] = new Value();
				}
				extract_value_for_binary(val.nested_values[0], stream, owning_instance,
						n_entities_from_file, def_types_count, false, bt, mod_small, streamUtf);
				return false;
			case '1':
				CEntity [][] instances_sim = mod.instances_sim;
				val.tag = PhFileReader.ENTITY_REFERENCE;
				int first_index = stream.readShort();
				if (mod_small) {
					int i_index = stream.readInt();
					if (instances_sim[first_index] == null) {
						val.tag = PhFileReader.MISSING;
						val.reference = null;
					} else {
						val.reference = instances_sim[first_index][i_index];
					}
					//val.reference = instances_sim[first_index][stream.readInt()];
				} else {
					val.reference = instances_sim[mod.to_entities[first_index]][stream.readInt()];
				}
				return false;
			case '2':
				mod_name = streamUtf.readUTF(stream);
if (SdaiSession.debug3)
if (mod == null) System.out.println(" BINFILE mod is NULL  " + mod_name);
else System.out.println(" BINFILE mod is POS  mod_name from stream  " + mod_name);
if (SdaiSession.debug3)
if (mod.ex_model_names == null) System.out.println(" BINFILE mod.ex_models is NULL");
else System.out.println(" BINFILE mod.ex_models is POS   ");
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readShort();
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readShort();
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
if (SdaiSession.debug3)  System.out.println(" BINFILE mod_name: " + mod_name + "    repo: " + repo.name + "  ent_name: " + ent_name);
				ref_mod = extractModel(repo, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				ident = stream.readLong();
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					val.reference = resolveReference(ref_mod, ent_name, ident, owning_instance);
					if (val.reference==null) {
						trans = ref_mod.repository.session.active_transaction;
						if (trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING) {
							mod_aborting = true;
						} else {
							mod_aborting = false;
						}
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, ent_name, ident, owning_instance);
							val.reference = con;
							val.string = ent_name;
							return true;
						}
					}
					return false;
				} else {
//int tt2 = stream.readInt();
//System.out.println(" Case 2 referenced model: " + ref_mod.name + "   entity: " + ent_name);
//"   instance: #" + tt2 + "   owning instance: #" + owning_instance.instance_identifier);
					if (ref_mod.inst_idents != null && underlying_build >= 249 && ref_mod.repository != SdaiSession.systemRepository) {
						id_index = ref_mod.find_instance_id(ident);
						if (id_index < 0) {
							val.tag = PhFileReader.MISSING;
							return false;
						}
					}
					con = newConnector(ref_mod, ent_name, ident, owning_instance);
//con = new Connector(ref_mod, ent_name, tt2, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '3':
				index = stream.readShort();
				ref_mod = mod.ex_models[index];
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readShort();
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readShort();
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
//System.out.println(" SdaiModel ^^^^^3  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					val.reference = resolveReference(ref_mod, ent_name, ident, owning_instance);
					if (val.reference==null) {
						trans = ref_mod.repository.session.active_transaction;
						if (trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING) {
							mod_aborting = true;
						} else {
							mod_aborting = false;
						}
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, ent_name, ident, owning_instance);
							val.reference = con;
							val.string = ent_name;
							return true;
						}
					}
					return false;
				} else {
//int tt3 = stream.readInt();
//System.out.println(" Case 3 referenced model: " + ref_mod.name + "   entity: " + ent_name);
//"   instance: #" + tt3 + "   owning instance: #" + owning_instance.instance_identifier);
					if (ref_mod.inst_idents != null && underlying_build >= 249 && ref_mod.repository != SdaiSession.systemRepository) {
						id_index = ref_mod.find_instance_id(ident);
						if (id_index < 0) {
							val.tag = PhFileReader.MISSING;
							return false;
						}
					}
					con = newConnector(ref_mod, ent_name, ident, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '4':
				rep_name = streamUtf.readUTF(stream);
				if (mod.n_ex_repositories >= mod.ex_repositories.length) {
					mod.ex_repositories = ses.ensureStringsCapacity(mod.ex_repositories);
				}
				mod.ex_repositories[mod.n_ex_repositories] = rep_name;
				mod.n_ex_repositories++;
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readShort();
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readShort();
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				if (!file_saved && rep_name.length() > SdaiSession.DEF_REP_NAME_LENGTH &&
						rep_name.substring(0, SdaiSession.DEF_REP_NAME_LENGTH).equals(SdaiSession.DEF_REP_NAME)) {
					val.tag = PhFileReader.MISSING;
					mod.ex_repositories[mod.n_ex_repositories - 1] = SdaiSession.DEF_REP_NAME;
					return false;
				}
				ref_mod = extractModel(rep_name, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
//System.out.println(" SdaiModel ^^^^^4  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					val.reference = resolveReference(ref_mod, ent_name, ident, owning_instance);
					if (val.reference==null) {
						trans = ref_mod.repository.session.active_transaction;
						if (trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING) {
							mod_aborting = true;
						} else {
							mod_aborting = false;
						}
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, ent_name, ident, owning_instance);
							val.reference = con;
							val.string = ent_name;
							return true;
						}
					}
					return false;
				} else {
					if (ref_mod.inst_idents != null && underlying_build >= 249 && ref_mod.repository.active
							&& ref_mod.repository != SdaiSession.systemRepository) {
						id_index = ref_mod.find_instance_id(ident);
						if (id_index < 0) {
							val.tag = PhFileReader.MISSING;
							return false;
						}
					}
					con = newConnector(ref_mod, ent_name, ident, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '5':
				index = stream.readShort();
				rep_name = mod.ex_repositories[index];
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readShort();
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readShort();
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				if (rep_name == SdaiSession.DEF_REP_NAME) {
					val.tag = PhFileReader.MISSING;
					return false;
				}
				ref_mod = extractModel(rep_name, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
//System.out.println(" SdaiModel ^^^^^5  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					val.reference = resolveReference(ref_mod, ent_name, ident, owning_instance);
					if (val.reference==null) {
						trans = ref_mod.repository.session.active_transaction;
						if (trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING) {
							mod_aborting = true;
						} else {
							mod_aborting = false;
						}
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, ent_name, ident, owning_instance);
							val.reference = con;
							val.string = ent_name;
							return true;
						}
					}
					return false;
				} else {
//int tt5 = stream.readInt();
//System.out.println(" Case 5 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt5 + "   owning instance: #" + owning_instance.instance_identifier);
					if (ref_mod.inst_idents != null && underlying_build >= 249 && ref_mod.repository.active
							&& ref_mod.repository != SdaiSession.systemRepository) {
						id_index = ref_mod.find_instance_id(ident);
						if (id_index < 0) {
							val.tag = PhFileReader.MISSING;
							return false;
						}
					}
					con = newConnector(ref_mod, ent_name, ident, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '6':
				index = stream.readShort();
				rep_name = mod.ex_repositories[index];
				int index_mod = stream.readShort();
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readShort();
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readShort();
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ident = stream.readLong();
				if (rep_name == SdaiSession.DEF_REP_NAME) {
					val.tag = PhFileReader.MISSING;
					return false;
				}
				ref_mod = mod.ex_models[index_mod];
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
//System.out.println(" SdaiModel ^^^^^6  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
					val.reference = resolveReference(ref_mod, ent_name, ident, owning_instance);
					if (val.reference==null) {
						trans = ref_mod.repository.session.active_transaction;
						if (trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING) {
							mod_aborting = true;
						} else {
							mod_aborting = false;
						}
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, ent_name, ident, owning_instance);
							val.reference = con;
							val.string = ent_name;
							return true;
						}
					}
					return false;
				} else {
//int tt6 = stream.readInt();
//System.out.println(" Case 6 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt6 + "   owning instance: #" + owning_instance.instance_identifier);
					if (ref_mod.inst_idents != null && underlying_build >= 249 && ref_mod.repository.active
							&& ref_mod.repository != SdaiSession.systemRepository) {
						id_index = ref_mod.find_instance_id(ident);
						if (id_index < 0) {
							val.tag = PhFileReader.MISSING;
							return false;
						}
					}
					con = newConnector(ref_mod, ent_name, ident, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '(':
				val.tag = PhFileReader.EMBEDDED_LIST;
				int index_in_list = 0;
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				bt = stream.readByte();
				boolean con_created = false;
				while (bt != ')') {
					if (index_in_list >= val.nested_values.length) {
						val.enlarge();
					}
					if (val.nested_values[index_in_list] == null) {
						val.nested_values[index_in_list] = new Value();
					}
					boolean res = extract_value_for_binary(val.nested_values[index_in_list], stream,
						owning_instance, n_entities_from_file, def_types_count, false, bt, mod_small, streamUtf);
					if (res) {
						con_created = true;
					}
					if (val.nested_values[index_in_list].tag != PhFileReader.REDEFINE) {
						index_in_list++;
					}
					bt = stream.readByte();
				}
				val.integer = index_in_list;
				val.length = index_in_list;
				if (con_created) {
					val.real = Double.NaN;
				} else {
					val.real = 0;
				}
				return false;
			}
//System.out.println(" SdaiModel  name: " + name +
//"   token: " + (char)token + "   owning_instance: #" + owning_instance.instance_identifier);
			String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
	}


/**
	Loads the value of an attribute of the specified entity instance from the
	binary file (old version of the method).
*/
	private boolean extract_value_for_binary(StaticFields staticFields, Value val, DataInput stream,
		CEntity owning_instance, int n_entities_from_file,
		boolean byte_needed, byte sym, StreamUTF streamUtf)
				throws java.io.IOException, SdaiException, ArrayIndexOutOfBoundsException {
		byte bt;
		int index;
		AEntity_definition set;
		String rep_name;
		String mod_name;
		String ent_name;
		Connector con;
//System.out.println(" SdaiModel !!!!!  owning_instance :" + owning_instance.instance_identifier);
		SdaiModel mod = owning_instance.owning_model;
		SdaiModel ref_mod;
		SdaiRepository repo = mod.repository;
		SdaiSession ses = repo.session;
		byte token;
		if (byte_needed) {
			token = stream.readByte();
		} else {
			token = sym;
		}
		Object [] myDataA = null;
		switch (token) {
			case '$':
				val.tag = PhFileReader.MISSING;
				return false;
			case '*':
				val.tag = PhFileReader.REDEFINE;
				return false;
			case 'i':
				val.tag = PhFileReader.INTEGER;
				val.integer = stream.readInt();
				return false;
			case 'r':
				val.tag = PhFileReader.REAL;
				val.real = stream.readDouble();
				return false;
			case 'f':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 0;
				return false;
			case 't':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 1;
				return false;
			case 'u':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 2;
				return false;
			case 'e':
				val.tag = PhFileReader.ENUM;
//				if (repository.build_number < SdaiSession.change_1) {
				if (underlying_build < SdaiSession.change_1) {
					val.length = stream.readInt();
					if (staticFields.string.length < val.length) {
						enlarge_string(staticFields, val.length);
					}
					stream.readFully(staticFields.string, 0, val.length);
					val.string = (new String(staticFields.string, 0, val.length))/*.intern()*/;
				} else {
					val.string = streamUtf.readUTF(stream)/*.intern()*/;
				}
				return false;
			case 's':
				val.tag = PhFileReader.STRING;
//				if (repository.build_number < SdaiSession.change_1) {
				if (underlying_build < SdaiSession.change_1) {
					val.length = stream.readInt();
					if (staticFields.string.length < val.length) {
						enlarge_string(staticFields, val.length);
					}
					stream.readFully(staticFields.string, 0, val.length);
					val.string = (new String(staticFields.string, 0, val.length))/*.intern()*/;
				} else {
					val.string = streamUtf.readUTF(stream)/*.intern()*/;
				}
				return false;
			case 'b':
				val.tag = PhFileReader.BINARY;
//				if (repository.build_number < SdaiSession.change_1) {
				if (underlying_build < SdaiSession.change_1) {
					val.length = stream.readInt();
					if (staticFields.string.length < val.length) {
						enlarge_string(staticFields, val.length);
					}
					stream.readFully(staticFields.string, 0, val.length);
					val.reference = new Binary(staticFields.string, val.length);
				} else {
					val.string = streamUtf.readUTF(stream);
					val.reference = new Binary(val.string);
				}
				return false;
			case 'p':
				val.tag = PhFileReader.TYPED_PARAMETER;
//				if (repository.build_number < SdaiSession.change_1) {
				if (underlying_build < SdaiSession.change_1) {
					val.length = stream.readInt();
					if (staticFields.string.length < val.length) {
						enlarge_string(staticFields, val.length);
					}
					stream.readFully(staticFields.string, 0, val.length);
					val.string = new String(staticFields.string, 0, val.length);
				} else {
					val.string = streamUtf.readUTF(stream);
				}
				val.integer = 1;
				bt = stream.readByte();
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (val.nested_values[0] == null) {
					val.nested_values[0] = new Value();
				}
				extract_value_for_binary(staticFields, val.nested_values[0], stream, owning_instance,
						n_entities_from_file, false, bt, streamUtf);
				return false;
			case '1':
				CEntity [][] instances_sim = mod.instances_sim;
				val.tag = PhFileReader.ENTITY_REFERENCE;
				int first_index = mod.to_entities[stream.readInt()];
				val.reference = instances_sim[first_index][stream.readInt()];
				return false;
			case '2':
				mod_name = streamUtf.readUTF(stream);
if (SdaiSession.debug3)
if (mod == null) System.out.println(" BINFILE mod is NULL  " + mod_name);
else System.out.println(" BINFILE mod is POS  mod_name from stream  " + mod_name);
if (SdaiSession.debug3)
if (mod.ex_model_names == null) System.out.println(" BINFILE mod.ex_models is NULL");
else System.out.println(" BINFILE mod.ex_models is POS   ");
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readInt();
//					if (index < 0 || index >= n_entities_from_file) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
//					if (index < 0 || index >= set.myLength) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readInt();
//					if (index < 0 || index >= mod.ex_edefs.length) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
if (SdaiSession.debug3)  System.out.println(" BINFILE mod_name: " + mod_name + "    repo: " + repo.name + "  ent_name: " + ent_name);
				ref_mod = extractModel(repo, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					index = stream.readInt();
//System.out.println(" SdaiModel ^^^^^2  owning_instance :" + owning_instance.instance_identifier +
//"    " + owning_instance);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
//					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = resolveReference(ref_mod, ent_name, stream.readInt(), owning_instance);
					return false;
				} else {
//					val.tag = PhFileReader.ENTITY_REFERENCE_SPECIAL;
//					val.tag = PhFileReader.ENTITY_REFERENCE;
//int tt2 = stream.readInt();
//System.out.println(" Case 2 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt2 + "   owning instance: #" + owning_instance.instance_identifier);
					con = newConnector(ref_mod, ent_name, stream.readInt(), owning_instance);
//con = new Connector(ref_mod, ent_name, tt2, owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '3':
				index = stream.readInt();
//				if (index < 0 || index >= mod.ex_models.length) {
//					String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//					throw new SdaiException(SdaiException.SY_ERR, base);
//				}
//				mod_name = mod.ex_model_names[index];
				ref_mod = mod.ex_models[index];
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readInt();
//					if (index < 0 || index >= n_entities_from_file) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
//					if (index < 0 || index >= set.myLength) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readInt();
//					if (index < 0 || index >= mod.ex_edefs.length) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
//				ref_mod = extractModel(repo, mod_name);
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					index = stream.readInt();
//System.out.println(" SdaiModel ^^^^^3  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
//					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = resolveReference(ref_mod, ent_name, stream.readInt(), owning_instance);
					return false;
				} else {
//					val.tag = PhFileReader.ENTITY_REFERENCE_SPECIAL;
//					val.tag = PhFileReader.ENTITY_REFERENCE;
//int tt3 = stream.readInt();
//System.out.println(" Case 3 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt3 + "   owning instance: #" + owning_instance.instance_identifier);
					con = newConnector(ref_mod, ent_name, stream.readInt(), owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '4':
				rep_name = streamUtf.readUTF(stream);
				if (mod.n_ex_repositories >= mod.ex_repositories.length) {
					mod.ex_repositories = ses.ensureStringsCapacity(mod.ex_repositories);
				}
				mod.ex_repositories[mod.n_ex_repositories] = rep_name;
				mod.n_ex_repositories++;
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
//				mod.n_ex_models++;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readInt();
//					if (index < 0 || index >= n_entities_from_file) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
//					if (index < 0 || index >= set.myLength) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readInt();
//					if (index < 0 || index >= mod.ex_edefs.length) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ref_mod = extractModel(rep_name, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					index = stream.readInt();
//System.out.println(" SdaiModel ^^^^^4  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
//					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = resolveReference(ref_mod, ent_name, stream.readInt(), owning_instance);
					return false;
				} else {
//					val.tag = PhFileReader.ENTITY_REFERENCE_SPECIAL;
//					val.tag = PhFileReader.ENTITY_REFERENCE;
//int tt4 = stream.readInt();
//System.out.println(" Case 4 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt4 + "   owning instance: #" + owning_instance.instance_identifier);
					con = newConnector(ref_mod, ent_name, stream.readInt(), owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '5':
				index = stream.readInt();
//				if (index < 0 || index >= mod.ex_repositories.length) {
//					String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//					throw new SdaiException(SdaiException.SY_ERR, base);
//				}
				rep_name = mod.ex_repositories[index];
				mod_name = streamUtf.readUTF(stream);
				if (mod.n_ex_models >= mod.ex_model_names.length) {
					mod.ex_model_names = ses.ensureStringsCapacity(mod.ex_model_names);
					mod.ensureModsCapacity();
				}
				mod.ex_model_names[mod.n_ex_models] = mod_name;
//				mod.n_ex_models++;
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readInt();
//					if (index < 0 || index >= n_entities_from_file) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
//					if (index < 0 || index >= set.myLength) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readInt();
//					if (index < 0 || index >= mod.ex_edefs.length) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				ref_mod = extractModel(rep_name, mod_name);
				mod.ex_models[mod.n_ex_models] = ref_mod;
				mod.n_ex_models++;
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					index = stream.readInt();
//System.out.println(" SdaiModel ^^^^^5  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
//					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = resolveReference(ref_mod, ent_name, stream.readInt(), owning_instance);
					return false;
				} else {
//					val.tag = PhFileReader.ENTITY_REFERENCE_SPECIAL;
//					val.tag = PhFileReader.ENTITY_REFERENCE;
//int tt5 = stream.readInt();
//System.out.println(" Case 5 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt5 + "   owning instance: #" + owning_instance.instance_identifier);
					con = newConnector(ref_mod, ent_name, stream.readInt(), owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '6':
				index = stream.readInt();
//				if (index < 0 || index >= mod.ex_repositories.length) {
//					String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//					throw new SdaiException(SdaiException.SY_ERR, base);
//				}
				rep_name = mod.ex_repositories[index];
				index = stream.readInt();
//				if (index < 0 || index >= mod.ex_models.length) {
//					String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//					throw new SdaiException(SdaiException.SY_ERR, base);
//				}
//				mod_name = mod.ex_model_names[index];
				ref_mod = mod.ex_models[index];
				bt = stream.readByte();
				if (bt == '1') {
					index = stream.readInt();
//					if (index < 0 || index >= n_entities_from_file) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					index = mod.to_entities[index];
					set = mod.underlying_schema.getEntities();
//					if (index < 0 || index >= set.myLength) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					if (((AEntity)set).myLength > 1) {
						myDataA = (Object [])((AEntity)set).myData;
					}
					if (myDataA == null) {
						ent_name = ((CEntity_definition)((AEntity)set).myData).getCorrectName();
					} else {
						ent_name = ((CEntity_definition)myDataA[index]).getCorrectName();
					}
				} else if (bt == '3') {
					index = stream.readInt();
//					if (index < 0 || index >= mod.ex_edefs.length) {
//						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
//						throw new SdaiException(SdaiException.SY_ERR, base);
//					}
					ent_name = mod.ex_edefs[index];
				} else if (bt == '2') {
					ent_name = streamUtf.readUTF(stream);
					if (mod.n_ex_edefs >= mod.ex_edefs.length) {
						mod.ex_edefs = ses.ensureStringsCapacity(mod.ex_edefs);
					}
					mod.ex_edefs[mod.n_ex_edefs] = ent_name;
					mod.n_ex_edefs++;
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
//				ref_mod = extractModel(rep_name, mod_name);
				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					index = stream.readInt();
//System.out.println(" SdaiModel ^^^^^6  owning_instance :" + owning_instance.instance_identifier);
					return false;
				}
				val.tag = PhFileReader.ENTITY_REFERENCE;
				if (/*ref_mod.underlying_schema != null &&*/ (ref_mod.mode & MODE_MODE_MASK) != NO_ACCESS) {
//					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = resolveReference(ref_mod, ent_name, stream.readInt(), owning_instance);
					return false;
				} else {
//					val.tag = PhFileReader.ENTITY_REFERENCE_SPECIAL;
//					val.tag = PhFileReader.ENTITY_REFERENCE;
//int tt6 = stream.readInt();
//System.out.println(" Case 6 referenced model: " + ref_mod.name + "   entity: " + ent_name +
//"   instance: #" + tt6 + "   owning instance: #" + owning_instance.instance_identifier);
					con = newConnector(ref_mod, ent_name, stream.readInt(), owning_instance);
					val.reference = con;
					val.string = ent_name;
					return true;
				}
			case '(':
				val.tag = PhFileReader.EMBEDDED_LIST;
				int index_in_list = 0;
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				bt = stream.readByte();
				boolean con_created = false;
				while (bt != ')') {
					if (index_in_list >= val.nested_values.length) {
						val.enlarge();
					}
					if (val.nested_values[index_in_list] == null) {
						val.nested_values[index_in_list] = new Value();
					}
					boolean res = extract_value_for_binary(staticFields, val.nested_values[index_in_list], stream,
						owning_instance, n_entities_from_file, false, bt, streamUtf);
					if (res) {
						con_created = true;
					}
					if (val.nested_values[index_in_list].tag != PhFileReader.REDEFINE) {
						index_in_list++;
					}
					bt = stream.readByte();
				}
				val.integer = index_in_list;
				val.length = index_in_list;
				if (con_created) {
					val.real = Double.NaN;
				} else {
					val.real = 0;
				}
				return false;
			}
//System.out.println(" SdaiModel  name: " + name +
//"   token: " + (char)token + "   owning_instance: #" + owning_instance.instance_identifier);
			String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
	}


/**
	Loads the value of an attribute of the specified entity instance from the binary stream.
*/
    private boolean extract_value_for_binary_remote(Value val, DataInput stream, CEntity owning_instance,
    		boolean byte_needed, byte sym, StreamUTF streamUtf, CEntity[][] inst_ces,
    		boolean connectMissingRef) throws IOException, SdaiException, ArrayIndexOutOfBoundsException {
		byte bt;
		long inst_id;
		Connector con;
		SdaiModel mod = owning_instance.owning_model;
		SdaiModel ref_mod;
		SdaiRepository repo = mod.repository;
		SdaiSession ses = repo.session;
		byte token;
        long repId;
        long modId;
        SdaiRepository repository;



		if (byte_needed) {
			token = stream.readByte();
		} else {
			token = sym;
		}
        //ps.print("\t|"+(char)token+"| ");
		switch (token) {
			case '$':
				val.tag = PhFileReader.MISSING;
				return false;
			case '*':
				val.tag = PhFileReader.REDEFINE;
				return false;
			case 'i':
				val.tag = PhFileReader.INTEGER;
				val.integer = stream.readInt();
				return false;
			case 'r':
				val.tag = PhFileReader.REAL;
				val.real = stream.readDouble();
				return false;
			case 'f':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 0;
				return false;
			case 't':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 1;
				return false;
			case 'u':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 2;
				return false;
			case 'e':
				val.tag = PhFileReader.ENUM;
				val.string = streamUtf.readUTF(stream)/*.intern()*/;
				return false;
			case 's':
				val.tag = PhFileReader.STRING;
				val.string = streamUtf.readUTF(stream)/*.intern()*/;
				return false;
			case 'b':
				val.tag = PhFileReader.BINARY;
				val.string = streamUtf.readUTF(stream);
				val.reference = new Binary(val.string);
				return false;
			case 'B':
				val.tag = PhFileReader.BINARY;
				Binary bnr = new Binary();
				long bt_count = stream.readLong();
				bnr.unused = stream.readByte();
				bnr.value = new byte[(int)bt_count - 1];
				stream.readFully(bnr.value);
				val.reference = bnr;
				return false;
			case 'p':
				val.tag = PhFileReader.TYPED_PARAMETER;
				val.string = streamUtf.readUTF(stream);
                //ps.println("p "+val.string);
				val.integer = 1;
				bt = stream.readByte();
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (val.nested_values[0] == null) {
					val.nested_values[0] = new Value();
				}
				extract_value_for_binary_remote(val.nested_values[0], stream, owning_instance,
												false, bt, streamUtf, inst_ces, connectMissingRef);
				return false;
			case '1':
                inst_id = stream.readLong();
                //ps.print("#"+inst_id);
                val.tag = PhFileReader.ENTITY_REFERENCE;
            	val.reference = null;
                if(inst_ces != null) {
                	int inst_ces_left = 0;
                	int inst_ces_right =
                		(inst_ces.length - 1) * INST_PORTION_SIZE + inst_ces[inst_ces.length - 1].length - 1;
            		while (inst_ces_left <= inst_ces_right) {
            			int inst_ces_middle = (inst_ces_left + inst_ces_right) >> 1;
            			CEntity found_inst =
            				inst_ces[inst_ces_middle / INST_PORTION_SIZE][inst_ces_middle % INST_PORTION_SIZE];
            			if (found_inst.instance_identifier < inst_id) {
            				inst_ces_left = inst_ces_middle + 1;
            			} else if (found_inst.instance_identifier > inst_id) {
            				inst_ces_right = inst_ces_middle - 1;
            			} else {
            				val.reference = found_inst;
            				break;
            			}
            		}
                }
                if(val.reference == null) {
                	val.reference = quick_find_instance(inst_id);
                }
				if(val.reference == null) {
					if(connectMissingRef) {
						con = newConnector(this, null, inst_id, owning_instance);
						val.reference = con;
						return true;
					} else {
						val.tag = PhFileReader.MISSING;
						return false;
					}
                }
				return false;
			case '2':
                repId = repo.getRepoRemote().getRemoteId();
                modId = stream.readLong();
                inst_id = stream.readLong();
                ref_mod = repo.findSdaiModelById(modId);
//                 ref_mod = repo.getSdaiModelByRef( new SdaiModelRef(repId, modId));
				if(ref_mod == null) {
					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = newConnector(repId, modId, inst_id, owning_instance);
					return true;
				} else {
					if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
						val.tag = PhFileReader.MISSING;
						return false;
					}

					val.tag = PhFileReader.ENTITY_REFERENCE;
					if ((ref_mod.mode & MODE_MODE_MASK) == READ_ONLY || (ref_mod.mode & MODE_MODE_MASK) == READ_WRITE) {
						val.reference = resolveReference2(ref_mod, inst_id, owning_instance);
						if((ref_mod.mode & MODE_SUBMODE_MASK) == MODE_SUBMODE_PARTIAL
						   && val.reference == null) {

							val.reference = newConnector(ref_mod, null, inst_id, owning_instance);
							return true;
						}
					} else {
						con = newConnector(ref_mod, null, inst_id, owning_instance);
						val.reference = con;
						return true;
					}
					if (val.reference==null) {
						SdaiTransaction trans = ref_mod.repository.session.active_transaction;
						boolean mod_aborting =
							trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING;
						if (ref_mod.committed && ref_mod.modified && mod_aborting) {
							con = newConnector(ref_mod, null, inst_id, owning_instance);
							val.reference = con;
							return true;
						}
					}
					return false;
				}
			case '3':
                repId = stream.readLong();
                modId = stream.readLong();
                inst_id = stream.readLong();
                ref_mod = null;
//                 repository = ses.getRepositoryByRef( new SdaiRepositoryRef(repId));
				repository = ses.findRepositoryInKnownServers(repId);
                if (repository != null) {
//                     ref_mod = repository.getSdaiModelByRef( new SdaiModelRef(repId, modId));
					ref_mod = repository.findSdaiModelById(modId);
					if(ref_mod == null) {
						val.tag = PhFileReader.ENTITY_REFERENCE;
						val.reference = newConnector(repository, modId, inst_id, owning_instance);
						return true;
					}
                } else {
					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = newConnector(repId, modId, inst_id, owning_instance);
					return true;
				}

				if (ref_mod == null || (ref_mod.mode & MODE_MODE_MASK) < 0) {
					val.tag = PhFileReader.MISSING;
					return false;
				}

				val.tag = PhFileReader.ENTITY_REFERENCE;
				if ((ref_mod.mode & MODE_MODE_MASK) == READ_ONLY || (ref_mod.mode & MODE_MODE_MASK) == READ_WRITE) {
					val.reference = resolveReference2(ref_mod, inst_id, owning_instance);
					if((ref_mod.mode & MODE_SUBMODE_MASK) == MODE_SUBMODE_PARTIAL
					   && val.reference == null) {

						val.reference = newConnector(ref_mod, null, inst_id, owning_instance);
						return true;
					}
				} else {
					con = newConnector(ref_mod, null, inst_id, owning_instance);
					val.reference = con;
					return true;
				}

				if (val.reference==null) {
					SdaiTransaction trans = ref_mod.repository.session.active_transaction;
					boolean mod_aborting =
						trans != null && trans.transactionStatus == SdaiTransaction.TRANSACTION_STATUS_ABORTING;
					if (ref_mod.committed && ref_mod.modified && mod_aborting) {
						con = newConnector(ref_mod, null, inst_id, owning_instance);
						val.reference = con;
						return true;
					}
				}
				return false;
			case '(':
				val.tag = PhFileReader.EMBEDDED_LIST;
				int index_in_list = 0;
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				bt = stream.readByte();
				boolean con_created = false;
                while (bt != ')') {
					if (index_in_list >= val.nested_values.length) {
						val.enlarge();
					}
					if (val.nested_values[index_in_list] == null) {
						val.nested_values[index_in_list] = new Value();
					}
					boolean res = extract_value_for_binary_remote(val.nested_values[index_in_list],
																  stream, owning_instance, false, bt,
																  streamUtf, inst_ces, connectMissingRef);
					if (res) {
						con_created = true;
					}
					if (val.nested_values[index_in_list].tag != PhFileReader.REDEFINE) {
						index_in_list++;
					}
					bt = stream.readByte();
				}
				val.integer = index_in_list;
				val.length = index_in_list;
				if (con_created) {
					val.real = Double.NaN;
				} else {
					val.real = 0;
				}
				return false;
			}
			String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
	}



/**
	Tries to find a model in the repository with names given
	through the method's parameters. If a repository with the specified name
	does not exist, then virtual repository is created.
	If the required model is not found, then either a new ordinary model
	is created (if the repository is open) or a new virtual model
	is produced (if the repository is closed).
*/
	private SdaiModel extractModel(String rep_name, String model_name) throws SdaiException {
		SdaiRepository rep = null;
		for (int i = 0; i < repository.session.known_servers.myLength; i++) {
			SdaiRepository known_rep = (SdaiRepository)repository.session.known_servers.myData[i];
			if (known_rep.name.equals(rep_name)) {
				rep = known_rep;
				break;
			}
		}
		if (rep == null ) {
			rep = repository.createVirtualRepository(rep_name);
		}
//		rep.internal_usage = true;
		rep.extracting_model = true;
		SdaiModel mod = rep.findSdaiModel(model_name);
		rep.extracting_model = false;
//		rep.internal_usage = false;
		if (mod != null) {
			return mod;
		}
		if (rep.active) {
/*			SdaiSession.println(AdditionalMessages.BF_MIMO + model_name);
			mod = new SdaiModel(model_name, rep);
			rep.model_count++;
			mod.identifier = rep.model_count;
			rep.models.addUnorderedRO(mod);
			rep.insertModel();
			mod.mode = -1;
			return mod;*/
			return null;
		} else {
			return rep.createVirtualModel(model_name);
		}
	}


/**
	Tries to find a model with the specified name in the repository submitted
	through the first method's parameter.
	If the required model is not found, then either a new ordinary model
	is created (if the repository is open) or a new virtual model
	is produced (if the repository is closed).
*/
	private SdaiModel extractModel(SdaiRepository rep, String model_name) throws SdaiException {
//		rep.internal_usage = true;
		rep.extracting_model = true;
		SdaiModel mod = rep.findSdaiModel(model_name);
		rep.extracting_model = false;
//		rep.internal_usage = false;
		if (mod != null) {
			return mod;
		}
		if (rep == SdaiSession.systemRepository) {
if (SdaiSession.debug2) System.out.println("  SdaiModel   dict model created   name: " +
model_name + "  rep: " + rep.name);
			return rep.findDictionarySdaiModel(model_name);
		} else if (rep.active) {
//for (int i = 0; i < rep.models.myLength; i++) {
//String m_name = ((SdaiModel)rep.models.myData[i]).name;
//System.out.println("     SdaiModel   model: " + m_name);}
//System.out.println("  SdaiModel   model name: " + model_name);
/*			SdaiSession.println(AdditionalMessages.BF_MIMO + model_name);
			mod = new SdaiModel(model_name, rep);
			rep.model_count++;
			mod.identifier = rep.model_count;
			rep.models.addUnorderedRO(mod);
			rep.insertModel();
			mod.mode = -1;
			return mod;*/
			return null;
		} else {
			return rep.createVirtualModel(model_name);
		}
	}


/**
	Given instance identifier, finds entity instance itself within this SdaiModel.
	This method is used when reading binary files.
*/
	private CEntity resolveReference(SdaiModel mod, String entity_name,
			long identif, CEntity owning_instance) throws SdaiException {
		if (mod.underlying_schema == null) {
			mod.getUnderlyingSchema();
		}
		SchemaData sch_data = mod.underlying_schema.modelDictionary.schemaData;

		int index;
		if (mod.repository != SdaiSession.systemRepository && ((mod.mode & MODE_MODE_MASK) == READ_ONLY)) {
			index = mod.find_entityRO(entity_name.toUpperCase());
		} else {
			index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name.toUpperCase());
		}
		if (index < 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_EINC + entity_name;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntity inst;
		//if (mod.sorted[index]) {
        if ((mod.sim_status[index] & SIM_SORTED) != 0) {
			int ind = mod.find_instance(0, mod.lengths[index] - 1, index, identif);
//System.out.println(" SdaiModel !!!!! mod: " + mod.name +
//"   entity_name: " + entity_name + "   ind = " + ind);
			if (ind >= 0) {
				return mod.instances_sim[index][ind];
			}
		} else {
			CEntity [] row_of_instances = mod.instances_sim[index];
			for (int j = 0; j < mod.lengths[index]; j++) {
				inst = row_of_instances[j];
				if (inst.instance_identifier == identif) {
					return inst;
				}
			}
		}
if (SdaiSession.debug2) System.out.println("  owning MODEL: " + name);
if (SdaiSession.debug2) System.out.println("  MODEL to reference: " + mod.name);
if (SdaiSession.debug2) System.out.println("  ENTITY: " + entity_name);
if (SdaiSession.debug2) System.out.println("  index: " + index + "  mod.lengths[index] = " + mod.lengths[index]);
if (SdaiSession.debug2)
for (int j = 0; j < mod.lengths[index]; j++) {
//inst = row_of_instances[j];
//System.out.println("  instance_identifier: #" + inst.instance_identifier);
}
/*for (int j = 0; j < sch_data.noOfEntityDataTypes; j++) {
String en_nam = sch_data.entities[j].getName(null);
System.out.println(" ENTITY: " + en_nam + "   count = " + mod.lengths[j]);
}*/
//		printWarningToLogo(AdditionalMessages.RD_NFRE, owning_instance.instance_identifier,
//			mod.name, identif);
		return null;
//		throw new SdaiException(SdaiException.VA_NSET);
	}



    private CEntity resolveReference2(SdaiModel mod, long inst_id, CEntity owning_instance) throws SdaiException {
		return mod.quick_find_instance(inst_id);
// 		CEntity inst;
//         for (int i = 0; i < mod.instances_sim.length; i++) {
//             if (mod.lengths[i] > 0) {
//                 int idx = mod.find_instance(0, mod.lengths[i] - 1, i, inst_id);
//                 if (idx >= 0) {
//                     return mod.instances_sim[i][idx];
//                 }
//             }
//         }
// 		return null;
	}


/**
	Finds an instance of the specified (by index/reference to the ordered list of entities)
	entity data type having the identifier provided through the parameter 'key'.
*/
	int find_instance(int left, int right, int index, long key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			long id = instances_sim[index][middle].instance_identifier;
			if(id < 0) {
				id = -id;
			}
			if (id < key) {
				left = middle + 1;
			} else if (id > key) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}

	final void prepare_quick_find() throws SdaiException {
		instances_sim_start_len = 0;
		for(int i = 0; i < lengths.length; i++) {
			if(lengths[i] != 0) {
				if(instances_sim_start[instances_sim_start_len] == null) {
					instances_sim_start[instances_sim_start_len] = new SimIdx();
				}
				instances_sim_start[instances_sim_start_len++].index = i;
			}
		}
		for(int i = 0; i < instances_sim_start_len; i++) {
			if(instances_sim_end[i] == null) {
				instances_sim_end[i] = new SimIdxEnd();
			}
			instances_sim_end[i].index = i;
		}
		Arrays.sort(instances_sim_start, 0, instances_sim_start_len, new QuickFindInstanceLeftSorter());
		Arrays.sort(instances_sim_end, 0, instances_sim_start_len, new QuickFindInstanceRightSorter());
		for(int i = 0; i < instances_sim_start_len; i++) {
			int minIndex = instances_sim_start_len;
			for(int j = i; j < instances_sim_start_len; j++) {
				if(instances_sim_end[j].index < minIndex) {
					minIndex = instances_sim_end[j].index;
				}
			}
			instances_sim_end[i].minIndex = minIndex;
		}
	}

	final void invalidate_quick_find() throws SdaiException {
		instances_sim_start_len = -1;
	}

	final CEntity quick_find_instance(long key) throws SdaiException {
		if(instances_sim_start_len < 0) {
			prepare_quick_find();
		}
		int leftSim = 0;
		int rightSim = instances_sim_start_len - 1;
		int middleSim = 0;
		while(leftSim <= rightSim) {
			middleSim = (leftSim + rightSim) >> 1;
			int simIdx = instances_sim_start[instances_sim_end[middleSim].index].index;
			if (lengths[simIdx] <= 0) return null;
			CEntity inst = instances_sim[simIdx][lengths[simIdx] - 1];
			long id = inst.instance_identifier;
			if (id < key) {
				leftSim = middleSim + 1;
			} else if (id > key) {
				rightSim = middleSim - 1;
			} else {
				return inst;
			}
		}
		if(leftSim < instances_sim_start_len) {
			leftSim = instances_sim_end[leftSim].minIndex;
			for(; leftSim < instances_sim_start_len; leftSim++) {
				int simIdx = instances_sim_start[leftSim].index;
				CEntity firstInst = instances_sim[simIdx][0];
				if(firstInst.instance_identifier == key) {
					return firstInst;
				} else if(firstInst.instance_identifier > key) {
					break;
				}
				int right = lengths[simIdx] - 1;
				//if(sorted[simIdx] && instances_sim[simIdx][right].instance_identifier > key) {
				if (((sim_status[simIdx] & SIM_SORTED) != 0) && instances_sim[simIdx][right].instance_identifier > key) {
					int idx = find_instance(0, right, simIdx, key);
					if(idx >= 0) {
						return instances_sim[simIdx][idx];
					}
				}
			}
		}
		return null;
	}

/**
 * Finds instances that are mappings of given source entity.
 * Instances that satisfies mapping constraints must be in this model, but other instances required by mapping constraints may be in other models within given target domain.
 * This method first finds all possible mappings.
 * These mappings should be defined in specified mapping domain.
 * Then it finds instances for every mapping.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_definition sourceEntity = ...;  // definition of source entity
 *    AEntity instances = m.findMappingInstances(sourceEntity, targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param sourceEntity this method searches instances that are mappings of this entity
 * @param targetDomain the domain where to search instances to satisfy mapping constraints.
 * It can be null.
 * @param mappingDomain the domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY} - retured mappings are restricted to most specific.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instanciated are also not included.
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 * @return list of instances that satisfy requirements. Every instance is mentioned only once in this list (elements of list are unique).
 * It will return null if there are no instances that are valid mappings of given entity.
 * @exception SdaiException if an error occurs during the operation
 *                          or in underlying JSDAI operations
 * @see #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see EEntity#testMappedEntity(EEntity_mapping, ASdaiModel, ASdaiModel, int) EEntity.testMappedEntity
 * @see EEntity#findEntityMappings(EEntity_definition, ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see EEntity#findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see ASdaiModel#findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, sourceEntity, targetDomain,
												mappingDomain, null, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
	 * Finds instances that are mappings of given source entity.
	 * This method provides the similar functionality as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * and adds extra possibility to get most specific mappings of the instances.
	 * <p>This method is part of the mapping extensions of JSDAI.
	 * @param sourceEntity this method searches instances that are mappings of this entity
	 * @param targetDomain the domain where to search instances to satisfy mapping constraints.
	 * It can be null.
	 * @param mappingDomain the domain for mapping constraints and dictionary data.
	 * @param instanceMappings if not null should be an empty aggregate. The most specific entity
	 * mappings are returned in this aggregate. The member indexes are synchronized with instance
	 * aggregate which is returned from this method. If null then the method works as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * @param mode Should always be {@link EEntity#NO_RESTRICTIONS} - no restrictions.
	 * @return list of instances that satisfy requirements. If <code>instanceMappings</code> is
	 * not null then member indexes in both aggregates make pairs. Every pair
	 * (instance, instanceMapping) is mentioned in both aggregates only once. If
	 * <code>instanceMappings</code> is null then every instance is mentioned
	 * only once in this aggregate.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
	 * @see EEntity#findMostSpecificMappings
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, sourceEntity, targetDomain, mappingDomain,
												instanceMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}


/**
 * Finds instances that are mappings of given entity mapping.
 * This method is more specific than {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}, because it searches instances that meets requirements only for specified mapping.
 * Instances that satisfy mapping constraints must be in this model, but other instances required by mapping constraints may be in other models within target domain.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_mapping mapping = ...;  // mapping of source entity
 *    AEntity instances = m.findMappingInstances(sourceEntity, targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param entityMapping this method searches instances that satisfies constraints for this entity mapping.
 * @param targetDomain targetInstances domain where to search instances to satisfy mapping constraints.
 * It can be null.
 * @param mappingDomain the domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY MOST_SPECIFC_ENTITY} - retured mappings are restricted to most specific.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instanciated are also not included.
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 * @return list of intstances that satisfy requirements.
 * One instance is mentined only one in this list (elements of list are unique).
 * It will return null if there are no instances that mets constraints of given entity mapping.
 * @see #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
 * @see EEntity#testMappedEntity(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see EEntity#findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see ASdaiModel#findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, targetDomain, mappingDomain,
												null, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
	 * Finds instances that are mappings of given source entity.
	 * This method provides the similar functionality as
	 * {@link #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)}
	 * and adds extra possibility to get most specific mappings of the instances.
	 * <p>This method is part of the mapping extensions of JSDAI.
	 * @param entityMapping this method searches instances that satisfies constraints
	 *                      for this entity mapping.
	 * @param targetDomain the domain where to search instances to satisfy mapping constraints.
	 * It can be null.
	 * @param mappingDomain the domain for mapping constraints and dictionary data.
	 * @param instanceMappings if not null should be an empty aggregate. The most specific entity
	 * mappings are returned in this aggregate. The member indexes are synchronized with instance
	 * aggregate which is returned from this method. If null then the method works as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * @param mode Should always be {@link EEntity#NO_RESTRICTIONS} - no restrictions.
	 * @return list of instances that satisfy requirements. If <code>instanceMappings</code> is
	 * not null then member indexes in both aggregates make pairs. Every pair
	 * (instance, instanceMapping) is mentioned in both aggregates only once. If
	 * <code>instanceMappings</code> is null then every instance is mentioned
	 * only once in this aggregate.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
	 * @see EEntity#findMostSpecificMappings
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, targetDomain, mappingDomain,
												instanceMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}


/**
 * Returns the set of all entity instances contained in this
 * model. This set, in fact, represents the contents of the model.
 * The method is valid only when the access mode for the model is set.
 * @return the set containing all entity instances of the <code>SdaiModel</code>.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 */
	public AEntity getInstances() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		provideAllInstancesIfNeeded();
		return instances;
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type and all instances of all its subtypes.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * This entity type must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_definition cp_type = ...;  // cartesian point
 *    ACartesian_point aggr = (ACartesian_point)m.getInstances(cp_type);</pre></TT>
 * @param type definition of the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(Class type)
 * @see #getInstances(EEntity_definition types[])
 * @see #getInstances(Class types[])
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 * @see #getInstanceCount(EEntity_definition type)
 */
	public AEntity getInstances(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index_to_entity = underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
		if (index_to_entity < 0) {
			throw new SdaiException(SdaiException.ED_NVLD, type);
		}
        return getInstancesInternal(underlying_schema.owning_model.schemaData, index_to_entity);
//		} // syncObject
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type and all instances of all its subtypes.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * This entity type (represented by the method's parameter) must be defined
 * or declared in the EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown.
 * A value passed to the parameter should be in the form EXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    ACartesian_point aggr = (ACartesian_point)m.getInstances(ECartesian_point.class);</pre></TT>
 * @param type Java class for the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(EEntity_definition types[])
 * @see #getInstances(Class types[])
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 * @see #getInstanceCount(Class type)
 */
	public AEntity getInstances(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		SchemaData sch_data = underlying_schema.owning_model.schemaData;
		int index_to_entity = sch_data.findEntityExtentIndex(type);
		if (index_to_entity == -1) {
			throw new SdaiException(SdaiException.ED_NDEF);
		} else if (index_to_entity == -2) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
        return getInstancesInternal(sch_data, index_to_entity);
//		} // syncObject
	}


/**
	Creates an aggregate representing all instances of the specified entity data type.
	The aggregate really is empty (contains only some auxiliary information),
	access operations on it are, in fact, simulated by taking instances from
	the array 'instances_sim'.
	The method is invoked within getInstances(EEntity_definition type) and
	getInstances(Class type).
*/
	private AEntity getInstancesInternal(SchemaData sch_data, int index) throws SdaiException {
		provideInstancesForTypeIfNeeded(index);

		AEntity instancesInExtent;
		try {
			instancesInExtent = (AEntity)sch_data.getAggregateClassByIndex(index).newInstance();
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Object [] myDataA = new Object[2];
		myDataA[0] = this;
		myDataA[1] = underlying_schema;
		instancesInExtent.myData = myDataA;
		instancesInExtent.myLength = index;
		instancesInExtent.myType = SdaiSession.setTypeForInstances;
		return instancesInExtent;
	}


/**
 * Returns a read-only aggregate consisting of all instances
 * of those entities which contain specified simple entity data types.
 * An array of definitions of simple entity data types is passed
 * through the parameter of this method.
 * The number of entity data types is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * If null value is passed to the method's parameter,
 * then SdaiException ED_NDEF is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_definition [] types = new EEntity_definition[5];
 *    types[0] = m.getEntityDefinition("C"); // definition for entity C
 *    types[1] = m.getEntityDefinition("E"); // definition for entity E
 *    types[2] = null;
      // If, for example, entity A is a subtype of entities C, D and E,
      // and entity B is a subtype of entities C, E and F,
      // then the aggregate returned by the following application
      // of getInstances will contain all instances of both A and B.
 *    AEntity aggr = m.getInstances(types);</pre></TT>
 * @param types an array of definitions of simple entity data types.
 * @return aggregate consisting of all instances of those entities
 * which contain specified simple entity data types.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see #getInstances(Class types[])
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 */
	public AEntity getInstances(EEntity_definition types[]) throws SdaiException {
//		synchronized (syncObject) {
		if (types == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.defs == null) {
			staticFields.defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= staticFields.defs.length) {
				ensureDefsCapacity();
			}
			staticFields.defs[i] = (CEntity_definition)types[i];
		}
		if (!null_found) {
			n_defs = types.length;
		}
		return getInstancesInternal2(staticFields, n_defs);
//		} // syncObject
	}


/**
 * Returns a read-only aggregate consisting of all instances
 * of those entities which contain specified simple entity data types.
 * An array of classes representing simple entity data types is passed
 * through the parameter of this method.
 * Each element in this array shall be in the form CXxx.class,
 * where xxx is the name of the entity.
 * The number of entity data types is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    Class [] types = new Class[5];
 *    types[0] = CC.class; // class for entity C
 *    types[1] = CE.class; // class for entity E
 *    types[2] = null;
      // If, for example, entity A is a subtype of entities C, D and E,
      // and entity B is a subtype of entities C, E and F,
      // then the aggregate returned by the following application
      // of getInstances will contain all instances of both A and B.
 *    AEntity aggr = m.getInstances(types);</pre></TT>
 * @param types an array of classes representing simple entity data types.
 * @return aggregate consisting of all instances of those entities
 * which contain specified simple entity data types.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see #getInstances(EEntity_definition types[])
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 * @see #getInstanceCount(Class types[])
 */
	public AEntity getInstances(Class types[]) throws SdaiException {
//		synchronized (syncObject) {
		if (types == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.defs == null) {
			staticFields.defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= staticFields.defs.length) {
				ensureDefsCapacity();
			}
			staticFields.defs[i] = underlying_schema.owning_model.schemaData.findEntity(types[i]);
		}
		if (!null_found) {
			n_defs = types.length;
		}
		return getInstancesInternal2(staticFields, n_defs);
//		} // syncObject
	}


/**
	Creates an aggregate consisting of all instances
	of those entities which contain specified simple entity data types.
	The latter are collected into array 'defs'.
	The method is invoked within getInstances(EEntity_definition types[]) and
	getInstances(Class types[]).
*/
	private AEntity getInstancesInternal2(StaticFields staticFields, int n_defs) throws SdaiException {
		int i, j;
		if (n_defs > 1) {
			sortDefs(staticFields, n_defs);
		}
		SchemaData sch_data = underlying_schema.owning_model.schemaData;
		int count = 0;
		int types_count = 0;
		int entity_array_ln;
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			entity_array_ln = entities.length;
		} else {
			entity_array_ln = sch_data.noOfEntityDataTypes;
		}
		for (i = 0; i < entity_array_ln; i++) {
			CEntityDefinition def;
			if ((mode & MODE_MODE_MASK) == READ_ONLY) {
				def = entities[i];
			} else {
				def = sch_data.entities[i];
			}
            def.getEntityClass();
//System.out.println("  SdaiModel  def CONSIDERED: " + def.getName(null));
//for (j = 0; j < def.noOfPartialEntityTypes; j++) {
//System.out.println("  SdaiModel  partial_def: " + def.partialEntityTypes[j].getName(null));}
			int start_index = 0;
			int res_index;
			boolean found = true;
			for (j = 0; j < n_defs; j++) {
				res_index = def.find_partial_entity(start_index, def.noOfPartialEntityTypes - 1,
					staticFields.defs[j].getCorrectName());
//System.out.println("  SdaiModel  ****** res_index = " + res_index);
				if (res_index < 0) {
					found = false;
					break;
				}
				start_index = res_index + 1;
			}
			if (!found) {
				continue;
			}
			sch_data.aux[types_count] = i;
			types_count++;
//System.out.println("  SdaiModel !!!!STORED with index = " + types_count + "    count = " + count);
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			HashSet ceTypesSet = new HashSet();
			for (i = 0; i < types_count; i++) {
				int index = sch_data.aux[i];
				int sd_index;
				if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 					sd_index = underlying_schema.owning_model.schemaData.findEntityExtentIndex(entities[index]);
		 		} else {
 					sd_index = index;
		 		}
				if ((sim_status[index] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
					 ceTypesSet.add(new Integer(sd_index));
				}

				int [] subtypes = underlying_schema.getSubtypes(sd_index);
				if (subtypes != null) {
					for (j = 0; j < subtypes.length; j++) {
						int index_true;
						if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
		 					index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[j]]);
				 		} else {
 							index_true = subtypes[j];
				 		}
						if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
							ceTypesSet.add(new Integer(subtypes[j]));
						}
					}
				}
			}

			if (ceTypesSet.size() > 0) {
				int [] ceTypes = new int[ceTypesSet.size()];
				Iterator ceTypesIterator = ceTypesSet.iterator();
				j = 0;
				while (ceTypesIterator.hasNext()) {
					ceTypes[j++] = ((Integer)ceTypesIterator.next()).intValue();
				}
				provideInstancesForTypes(ceTypes, false);
			}
		}
		for (i = 0; i < types_count; i++) {
			int index = sch_data.aux[i];
			count += lengths[index];
		}

		AEntity agg = new AEntity();
		if (count > 0) {
			agg.initializeInstancesAggregate(count);
			Object [] myDataA = (Object [])agg.myData;
			int counter = 0;
			for (i = 0; i < types_count; i++) {
				int index = sch_data.aux[i];
				for (j = 0; j < lengths[index]; j++) {
					myDataA[counter] = instances_sim[index][j];
					counter++;
				}
			}
			myDataA[count] = this;
		} else {
			agg.myData = this;
		}
		return agg;
	}


/**
	Sorts the array 'defs' consisting of the specified simple entity data types.
	Invoked in methods: getInstancesInternal2, getInstanceCountInternal2,
	getExactInstanceCount(EEntity_definition types[]).
*/
	private void sortDefs(StaticFields staticFields, int n_defs) {
		if (staticFields.aux_defs == null) {
			staticFields.aux_defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		} else if (n_defs > staticFields.aux_defs.length) {
			staticFields.aux_defs = new CEntity_definition[staticFields.defs.length*2];
		}
//		for (int i = 0; i < n_defs; i++) {
//			aux_defs[i] = defs[i];
//		}
		System.arraycopy(staticFields.defs, 0, staticFields.aux_defs, 0, n_defs);
		sortDefs(staticFields.aux_defs, staticFields.defs, 0, n_defs);
		for (int i = 0; i < n_defs; i++) {
			staticFields.aux_defs[i] = null;
		}
	}


/**
	Method directly performing the sorting of the specified simple entity data types.
*/
	private void sortDefs(CEntityDefinition [] aux_defs, CEntityDefinition [] defs,
			int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index &&
						defs[j-1].getNameUpperCase().compareTo(defs[j].getNameUpperCase()) > 0; j--) {
					CEntityDefinition def = defs[j-1];
					defs[j-1] = defs[j];
					defs[j] = def;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortDefs(defs, aux_defs, start_index, middle);
		sortDefs(defs, aux_defs, middle, end_index);
		if (aux_defs[middle-1].getNameUpperCase().compareTo(aux_defs[middle].getNameUpperCase()) <= 0) {
			System.arraycopy(aux_defs, start_index, defs, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					aux_defs[m].getNameUpperCase().compareTo(aux_defs[n].getNameUpperCase()) <= 0) {
				defs[i] = aux_defs[m++];
			} else {
				defs[i] = aux_defs[n++];
			}
		}
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type.
 * The instances of any of its subtypes are not included into
 * this aggregate. The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * This entity type must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_definition cp_type = ...;  // cartesian point
 *    ACartesian_point aggr = (ACartesian_point)m.getExactInstances(cp_type);</pre></TT>
 * @param type definition of the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see #getInstances(EEntity_definition types[])
 * @see #getInstances(Class types[])
 * @see #getExactInstances(Class type)
 * @see #getExactInstanceCount(EEntity_definition type)
 */
	public AEntity getExactInstances(EEntity_definition type)
			throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index = underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
		if (index < 0) {
			throw new SdaiException(SdaiException.ED_NVLD, type);
		}
		return getExactInstancesInternal(underlying_schema.owning_model.schemaData, index);
//		} // syncObject
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type.
 * The instances of any of its subtypes are not included into
 * this aggregate. The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * This entity type (represented by the method's parameter) must be defined
 * or declared in the EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown.
 * A value passed to the parameter should be in the form CXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    ACartesian_point aggr = (ACartesian_point)m.getExactInstances(CCartesian_point.class);</pre></TT>
 * @param type Java class for the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see #getInstances(EEntity_definition types[])
 * @see #getInstances(Class types[])
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public AEntity getExactInstances(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index = underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
		if (index == -1) {
			throw new SdaiException(SdaiException.ED_NDEF);
		} else if (index == -2) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return getExactInstancesInternal(underlying_schema.owning_model.schemaData, index);
//		} // syncObject
	}


/**
	Creates an aggregate representing all instances of the specified entity data type.
	The instances of any of its subtypes are not assumed to be represented by this aggregate.
	The aggregate really is empty (contains only some auxiliary information),
	access operations on it are, in fact, simulated by taking instances from
	the array 'instances_sim'.
	The method is invoked within getExactInstances(EEntity_definition type) and
	getExactInstances(Class type).
*/
	private AEntity getExactInstancesInternal(SchemaData sch_data, int index) throws SdaiException {
		provideInstancesForExactTypeIfNeeded(index);

        AEntity instancesInExtent;
		try {
			instancesInExtent = (AEntity)sch_data.getAggregateClassByIndex(index).newInstance();
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Object [] myDataA = new Object[2];
		myDataA[0] = this;
		myDataA[1] = underlying_schema;
		instancesInExtent.myData = myDataA;
		instancesInExtent.myLength = index;
		instancesInExtent.myType = SdaiSession.setTypeForInstancesExact;
		return instancesInExtent;
	}


/**
 * Returns the number of instances contained in this model.
 * @return the number of instances.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 */
	public int getInstanceCount() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		provideAllInstancesIfNeeded();
		int count = 0;
		for (int i = 0; i < lengths.length; i++) {
			count += lengths[i];
		}
		return count;
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * and of all its subtypes. Only instances contained in this model are counted.
 * The given entity data type must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * @param type definition of the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @see #getInstanceCount()
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getInstances(EEntity_definition type)
 */
	public int getInstanceCount(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index_to_entity = underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
		if (index_to_entity < 0) {
//System.out.println("  SdaiModel !!!!!!   underlying_schema: " + underlying_schema.getName(null) +
//"    underlying_schema.owning_model: " + underlying_schema.owning_model.name);
//return -1;
			throw new SdaiException(SdaiException.ED_NVLD, type);
		}
		return getInstanceCountInternal(index_to_entity);
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * and of all its subtypes. Only instances contained in this model are counted.
 * The given entity data type (represented by the method's parameter) must be defined
 * or declared in the EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown.
 * A value passed to the parameter should be in the form EXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param type Java class for the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getInstances(Class type)
 */
	public int getInstanceCount(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index_to_entity = underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
		if (index_to_entity == -1) {
			throw new SdaiException(SdaiException.ED_NDEF, type);
		} else if (index_to_entity == -2) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return getInstanceCountInternal(index_to_entity);
//		} // syncObject
	}


/**
	Computes the number of instances of the specified entity data type
	and of all its subtypes.
	The method is invoked within getInstanceCount(EEntity_definition type) and
	getInstanceCount(Class type).
*/
	int getInstanceCountInternal(int index) throws SdaiException {
		int index_true;
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
		} else {
			index_true = index;
		}
		int [] subtypes = underlying_schema != null ? underlying_schema.getSubtypes(index) : null;
		if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			int ceTypesMaxLength = 1;
			if (subtypes != null) {
				ceTypesMaxLength += subtypes.length;
			}
			int [] ceTypes = new int[ceTypesMaxLength];
			int ceTypesRealLength = 0;
			if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
				ceTypes[ceTypesRealLength++] = index;
			}
			if (subtypes != null) {
				for (int i = 0; i < subtypes.length; i++) {
					int ind;
					if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
						ind = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[i]]);
					} else {
						ind = subtypes[i];
					}
					if (ind >= 0 && (sim_status[ind] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
						ceTypes[ceTypesRealLength++] = subtypes[i];
					}
				}
			}
			if (ceTypesRealLength > 0) {
				if(ceTypesRealLength < ceTypes.length) {
					int [] ceTypesCopy = new int[ceTypesRealLength];
					System.arraycopy(ceTypes, 0, ceTypesCopy, 0, ceTypesRealLength);
					ceTypes = ceTypesCopy;
				}
				provideInstancesForTypes(ceTypes, false);
			}
		}
		int count;
		if (index_true >= 0) {
			count = lengths[index_true];
		} else {
			count = 0;
		}
		if (underlying_schema != null) {
			if (subtypes != null) {
				for (int i = 0; i < subtypes.length; i++) {
					if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
						index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[i]]);
			 		} else {
						index_true = subtypes[i];
					}
					if (index_true >= 0) {
						count += lengths[index_true];
					}
				}
			}
		}
		return count;
	}


/**
 * Returns the total number of instances of those entities which
 * contain specified simple entity data types.
 * Only instances contained in this model are counted.
 * An array of definitions of simple entity data types is passed
 * through the parameter of this method.
 * The number of entity data types is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * If null value is passed to the method's parameter,
 * then SdaiException ED_NDEF is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    EEntity_definition [] types = new EEntity_definition[5];
 *    types[0] = m.getEntityDefinition("C"); // definition for entity C
 *    types[1] = m.getEntityDefinition("E"); // definition for entity E
 *    types[2] = null;
      // If, for example, entity A is a subtype of entities C, D and E,
      // and entity B is a subtype of entities C, E and F,
      // then the count returned by the following application
      // of getInstanceCount will include all instances of both A and B.
 *    int A_and_B_and_other = m.getInstanceCount(types);</pre></TT>
 * @param types an array of definitions of simple entity data types.
 * @return the number of instances of those entities
 * which contain specified simple entity data types.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getInstances(EEntity_definition types[])
 */
	public int getInstanceCount(EEntity_definition types[]) throws SdaiException {
//		synchronized (syncObject) {
		if (types == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.defs == null) {
			staticFields.defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= staticFields.defs.length) {
				ensureDefsCapacity();
			}
			staticFields.defs[i] = (CEntity_definition)types[i];
		}
		if (!null_found) {
			n_defs = types.length;
		}
		return getInstanceCountInternal2(staticFields, n_defs);
//		} // syncObject
	}


/**
 * Returns the total number of instances of those entities which
 * contain specified simple entity data types.
 * Only instances contained in this model are counted.
 * An array of classes representing simple entity data types is passed
 * through the parameter of this method.
 * Each element in this array shall be in the form CXxx.class,
 * where xxx is the name of the entity.
 * The number of entity data types is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    Class [] types = new Class[5];
 *    types[0] = CC.class; // class for entity C
 *    types[1] = CE.class; // class for entity E
 *    types[2] = null;
      // If, for example, entity A is a subtype of entities C, D and E,
      // and entity B is a subtype of entities C, E and F,
      // then the count returned by the following application
      // of getInstanceCount will include all instances of both A and B.
 *    int A_and_B_and_other = m.getInstanceCount(types);</pre></TT>
 * @param types an array of classes representing simple entity data types.
 * @return the number of instances of those entities
 * which contain specified simple entity data types.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getInstances(Class types[])
 */
	public int getInstanceCount(Class types[]) throws SdaiException {
//		synchronized (syncObject) {
		if (types == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.defs == null) {
			staticFields.defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= staticFields.defs.length) {
				ensureDefsCapacity();
			}
			staticFields.defs[i] = underlying_schema.owning_model.schemaData.findEntity(types[i]);
		}
		if (!null_found) {
			n_defs = types.length;
		}
		return getInstanceCountInternal2(staticFields, n_defs);
//		} // syncObject
	}


/**
	Computes the total number of instances of those entities which
	contain specified simple entity data types.
	The latter are collected into array 'defs'.
	The method is invoked within getInstanceCount(EEntity_definition types[]) and
	getInstanceCount(Class types[]).
*/
	private int getInstanceCountInternal2(StaticFields staticFields, int n_defs) throws SdaiException {
		if (n_defs > 1) {
			sortDefs(staticFields, n_defs);
		}
		SchemaData sch_data = underlying_schema.owning_model.schemaData;
		int count = 0;
		int types_count = 0;
		int entity_array_ln;
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			entity_array_ln = entities.length;
		} else {
			entity_array_ln = sch_data.noOfEntityDataTypes;
		}
		for (int i = 0; i < entity_array_ln; i++) {
			CEntityDefinition def;
			if ((mode & MODE_MODE_MASK) == READ_ONLY) {
				def = entities[i];
			} else {
				def = sch_data.entities[i];
			}
			def.getEntityClass();
			int start_index = 0;
			int res_index;
			boolean found = true;
			for (int j = 0; j < n_defs; j++) {
				res_index = def.find_partial_entity(start_index, def.noOfPartialEntityTypes - 1,
					staticFields.defs[j].getCorrectName());
				if (res_index < 0) {
					found = false;
					break;
				}
				start_index = res_index + 1;
			}
			if (found) {
				sch_data.aux[types_count++] = i;
// 				count += lengths[i];
			}
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL) {
			HashSet ceTypesSet = new HashSet();
			for (int i = 0; i < types_count; i++) {
				int index = sch_data.aux[i];
				int sd_index;
				if ((mode & MODE_MODE_MASK) == READ_ONLY) {
					sd_index = underlying_schema.owning_model.schemaData.findEntityExtentIndex(entities[index]);
				} else {
					sd_index = index;
				}
				if ((sim_status[index] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
					ceTypesSet.add(new Integer(sd_index));
				}

				int [] subtypes = underlying_schema.getSubtypes(index);
				if (subtypes != null) {
					for (int j = 0; j < subtypes.length; j++) {
						int index_true;
						if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
		 					index_true = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[j]]);
				 		} else {
 							index_true = subtypes[j];
				 		}
						if (index_true >= 0 && (sim_status[index_true] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
							ceTypesSet.add(new Integer(subtypes[j]));
						}
					}
				}
			}

			if (ceTypesSet.size() > 0) {
				int [] ceTypes = new int[ceTypesSet.size()];
				Iterator ceTypesIterator = ceTypesSet.iterator();
				int j = 0;
				while (ceTypesIterator.hasNext()) {
					ceTypes[j++] = ((Integer)ceTypesIterator.next()).intValue();
				}
				provideInstancesForTypes(ceTypes, false);
			}
		}
		for (int i = 0; i < types_count; i++) {
			int index = sch_data.aux[i];
			count += lengths[index];
		}
		return count;
	}

/**
 * Returns the number of instances of the specified entity data type
 * within this model.
 * The instances of any of its subtypes are not counted.
 * The given entity data type must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * @param type definition of the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getExactInstances(EEntity_definition type)
 */
	public int getExactInstanceCount(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index;
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			index = find_entityRO((CEntityDefinition)type);
			if (index < 0) {
				return 0;
			}
		} else {
			index = underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (index < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, type);
			}
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[index] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
			int dict_index;
			if ((mode & MODE_MODE_MASK) == READ_ONLY) {
				dict_index = underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			} else {
				dict_index = index;
			}
			provideInstancesForTypes(new int[] { dict_index }, false);
		}
		return lengths[index];
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * within this model.
 * The instances of any of its subtypes are not counted.
 * The given entity data type (represented by the method's parameter) must be defined
 * or declared in the EXPRESS schema whose definition is underlying for this model.
 * Otherwise, that is, if the parameter passed to this method does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown.
 * A value passed to the parameter should be in the form CXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param type Java class for the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(EEntity_definition types[])
 * @see #getExactInstances(Class type)
 */
	public int getExactInstanceCount(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		int index = underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
		if (index == -1) {
			throw new SdaiException(SdaiException.ED_NDEF, type);
		} else if (index == -2) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		int true_index;
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			true_index = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
			if (true_index < 0) {
				return 0;
			}
		} else {
			true_index = index;
			if (true_index == -1) {
				throw new SdaiException(SdaiException.ED_NDEF, type);
			}
		}
		if (getSubMode() == MODE_SUBMODE_PARTIAL
			&& (sim_status[true_index] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {

			provideInstancesForTypes(new int[] { index }, false);
        }
		return lengths[true_index];
//		} // syncObject
	}


/**
 * Returns the number of instances of the entity consisting
 * of the specified simple entity data types.
 * Only instances contained in this model are counted.
 * An array of definitions of simple entity data types is passed
 * through the parameter of this method.
 * The number of entity data types is either specified by the
 * position of the first null value in this array or is equal
 * to the length of this array if it contains no null value.
 * If null value is passed to the method's parameter,
 * then SdaiException ED_NDEF is thrown.
 * @param types an array of definitions of simple entity data types.
 * @return the number of instances of the entity consisting
 * of the specified simple entity data types.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getInstanceCount(EEntity_definition types[])
 * @see #getInstanceCount(Class types[])
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public int getExactInstanceCount(EEntity_definition types[]) throws SdaiException {
//		synchronized (syncObject) {
		if (types == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS || deleting) {
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.defs == null) {
			staticFields.defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= staticFields.defs.length) {
				ensureDefsCapacity();
			}
			staticFields.defs[i] = (CEntity_definition)types[i];
		}
		if (!null_found) {
			n_defs = types.length;
		}
		if (n_defs > 1) {
			sortDefs(staticFields, n_defs);
		}
		SchemaData sch_data = underlying_schema.owning_model.schemaData;
		int entity_array_ln;
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			entity_array_ln = entities.length;
		} else {
			entity_array_ln = sch_data.noOfEntityDataTypes;
		}
		for (int i = 0; i < entity_array_ln; i++) {
			CEntityDefinition def;
			if ((mode & MODE_MODE_MASK) == READ_ONLY) {
				def = entities[i];
			} else {
				def = sch_data.entities[i];
			}
			def.getEntityClass();
			if (def.check_entity(staticFields.defs, n_defs)) {
				if (getSubMode() == MODE_SUBMODE_PARTIAL
					&& (sim_status[i] & SIM_LOADED_MASK) != SIM_LOADED_COMPLETE) {
					int dict_index;
					if ((mode & MODE_MODE_MASK) == READ_ONLY) {
		 				dict_index = sch_data.findEntityExtentIndex(def);
				 	} else {
 						dict_index = i;
				 	}
					provideInstancesForTypes(new int[] { dict_index }, false);
				}
				return lengths[i];
			}
		}
		return 0;
//		} // syncObject
	}


/**
 * Makes a copy of the specified instance and puts this copy into
 * the contents of this model. The copy is, in addition, returned
 * by this method.
 * The values for all attributes of the copy are taken from
 * the original instance similarly as in
 * {@link CEntity#copyApplicationInstance copyApplicationInstance} method.
 * <p> The entity data type of the original instance must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * The method is not applicable to models from "System Repository".
 * Moreover, no instance from the models of "System Repository" can be copied to
 * any model from any repository.
 * In both these cases SdaiException FN_NAVL is thrown.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param source the given entity instance.
 * @return a copy of the given entity instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #copyInstances
 */
	public EEntity copyInstance(EEntity source) throws SdaiException {
//		synchronized (syncObject) {
		if (source == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository.session.undo_redo_file != null && repository.session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiModel source_owner = ((CEntity)source).owning_model;
		if (source_owner.repository == SdaiSession.systemRepository || repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity_definition def = (CEntity_definition)source.getInstanceType();
		boolean mod_state = modified;
		bypass = true;
		CEntity copy = (CEntity)createEntityInstance(def);
		bypass = false;
		copy.copy_values(StaticFields.get(), (CEntity)source, def, null, this);
		repository.session.undoRedoCreatePrepare(copy, mod_state);
		return copy;
//		} // syncObject
	}


/**
 * Makes a copy of all instances from the specified aggregate
 * to this model. The copies of the instances are also collected into a new
 * aggregate which is returned by this method.
 * References between instances of the given aggregate are mapped
 * to references between their copies. Other values for attributes of the newly created
 * copies are taken from the original instances similarly as in
 * {@link CEntity#copyApplicationInstance copyApplicationInstance} method.
 * <p> The entity data type of each original instance must be defined or declared in the
 * EXPRESS schema whose definition is underlying for this model.
 * Otherwise, SdaiException ED_NVLD is thrown.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * The method is not applicable to models from "System Repository".
 * Moreover, no instance from the models of "System Repository" can be copied to
 * any model from any repository.
 * In both these cases SdaiException FN_NAVL is thrown.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    SdaiModel m_source = ...;
 *    AEntity source = m_source.getInstances(ECartesian_point.class);
 *    AEntity res_aggr = m.copyInstances(source);</pre></TT>
 * @param source the aggregate of entity instances.
 * @return the aggregate of copies of the specified entity instances.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #copyInstance
 */
	public AEntity copyInstances(AEntity source) throws SdaiException {
		if (source == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (repository.session.undo_redo_file != null && repository.session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
//		synchronized (syncObject) {
		if (source.myType == SdaiSession.setTypeForInstancesAll) {
			SdaiIterator iter = source.createIterator();
			AEntity source_new = new AEntity();
			SdaiIterator iter_new = source_new.createIterator();
			while (iter.next()) {
				EEntity inst = source.getCurrentMemberEntity(iter);
				source_new.addAfter(iter_new, inst);
				iter_new.next();
			}
			return copyInstancesInternal(source_new);
		}
		return copyInstancesInternal(source);
//		} // syncObject
	}


	private AEntity copyInstancesInternal(AEntity source) throws SdaiException {
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity source_inst;
		int ln = source.getMemberCount();
		StaticFields staticFields = StaticFields.get();
		if (staticFields.for_instances_sorting == null) {
			if (ARRAY_FOR_INSTANCES_SORTING_SIZE >= ln) {
				staticFields.for_instances_sorting = new CEntity[ARRAY_FOR_INSTANCES_SORTING_SIZE];
			} else {
				staticFields.for_instances_sorting = new CEntity[ln];
			}
		} else if (ln > staticFields.for_instances_sorting.length) {
			staticFields.for_instances_sorting = new CEntity[ln];
		}
		if (staticFields.sorted_instances == null) {
			if (ARRAY_FOR_INSTANCES_SORTING_SIZE >= ln) {
				staticFields.sorted_instances = new CEntity[ARRAY_FOR_INSTANCES_SORTING_SIZE];
			} else {
				staticFields.sorted_instances = new CEntity[ln];
			}
		} else if (ln > staticFields.sorted_instances.length) {
			staticFields.sorted_instances = new CEntity[ln];
		}
		boolean mod_state = modified;
		SdaiIterator it_source = source.createIterator();
		int i = 0;
		while (it_source.next()) {
			source_inst = (CEntity)source.getCurrentMemberEntity(it_source);
			if (source_inst.owning_model == null) {
				throw new SdaiException(SdaiException.EI_NVLD, source_inst);
			}
			if (source_inst.owning_model.repository == SdaiSession.systemRepository) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			//source_inst.instance_position = i;
            source_inst.instance_position = (source_inst.instance_position & CEntity.FLG_MASK) | (i & CEntity.POS_MASK);  //--V--
			staticFields.for_instances_sorting[i] = source_inst;
			staticFields.sorted_instances[i] = source_inst;
			i++;
		}
		sortInstances(staticFields.for_instances_sorting, staticFields.sorted_instances, 0, ln);
		it_source.beginning();
		AEntity aggr = new AEntity();
		SdaiIterator it_result = aggr.createIterator();
		i = 0;
		while (it_source.next()) {
			source_inst = (CEntity)source.getCurrentMemberEntity(it_source);
			bypass = true;
			CEntity copy = (CEntity)createEntityInstance(source_inst.getInstanceType());
			bypass = false;
			aggr.addAfter(it_result, copy);
			staticFields.for_instances_sorting[i] = copy;
			it_result.next();
			i++;
		}
		staticFields.sorted_instances_count = ln;
		it_source.beginning();
		i = 0;
		while (it_source.next()) {
			source_inst = (CEntity)source.getCurrentMemberEntity(it_source);
			staticFields.for_instances_sorting[i].copy_values(staticFields, source_inst,
				(CEntity_definition)source_inst.getInstanceType(), aggr, this);
			if (repository.session.undo_redo_file != null && !bypass) {
//				repository.session.undoRedoCreatePrepare(staticFields.for_instances_sorting[i], mod_state);
				repository.session.undoRedoCopyPrepare(staticFields.for_instances_sorting[i], mod_state);
			}
			i++;
		}
		return aggr;
	}
/*	public AEntity copyInstances(AEntity source) throws SdaiException {
		if (source == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		int i;
		AEntity aggr = new AEntity();
		CEntity source_inst;
		CEntity_definition def;
		int ln = source.getMemberCount();
		if (for_instances_sorting == null) {
			if (ARRAY_FOR_INSTANCES_SORTING_SIZE >= ln) {
				for_instances_sorting = new Object[ARRAY_FOR_INSTANCES_SORTING_SIZE];
			} else {
				for_instances_sorting = new Object[ln];
			}
		} else if (ln > for_instances_sorting.length) {
			for_instances_sorting = new Object[ln];
		}
		if (sorted_instances == null) {
			if (ARRAY_FOR_INSTANCES_SORTING_SIZE >= ln) {
				sorted_instances = new Object[ARRAY_FOR_INSTANCES_SORTING_SIZE];
			} else {
				sorted_instances = new Object[ln];
			}
		} else if (ln > sorted_instances.length) {
			sorted_instances = new Object[ln];
		}
		for (i = 0; i < ln; i++) {
			source_inst = (CEntity)source.getByIndexEntity(i + 1);
			for_instances_sorting[i] = source_inst;
			sorted_instances[i] = source_inst;
		}
		sortInstances(for_instances_sorting, sorted_instances, 0, ln);
		for (i = 0; i < ln; i++) {
			source_inst = sorted_instances[i];
			def = (CEntity_definition)source_inst.getInstanceType();
			aggr.addByIndex(i + 1, createEntityInstance(def));
		}
		sorted_instances_count = ln;
		for (i = 0; i < ln; i++) {
			CEntity copy = (CEntity)aggr.getByIndexEntity(i + 1);
			source_inst = sorted_instances[i];
			def = (CEntity_definition)source_inst.getInstanceType();
			copy.copy_values(source_inst, def, aggr, this);
		}
		return aggr;
	}*/


/**
 * Returns a description of this model as a <code>String</code>.
 * It includes constant string "SdaiModel: " and the name of the model.
 * @return a description of the model.
 */
	public String toString() {
		return "SdaiModel: " + name;
	}


/**
	Given an instance identifier, finds the entity instance in an
	auxiliary array 'sorted_instances' with this identifier.
	This method was introduced for the needs of copyInstance and
	copyInstances methods.
	It is invoked in only one place in class Value.
*/
	int find_instance(StaticFields staticFields, long key) throws SdaiException {
		int left = 0;
		int right = staticFields.sorted_instances_count - 1;
		while (left <= right) {
			int middle = (left + right)/2;
			long id = staticFields.sorted_instances[middle].instance_identifier;
			if (id < key) {
				left = middle + 1;
			} else if (id > key) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


/**
	Given an instance identifier, finds its position in the sorted
   array inst_idents. If instance with this identifier is not found,
   then value -1 is returned. This method was introduced to fast check
   if instance belongs to a specified model.
*/
	int find_instance_id(long key) throws SdaiException {
		return find_instance_id(0, (int)all_inst_count - 1, key);
	}


/**
	Given an instance identifier, finds its position in the sorted array
	inst_idents. If instance with this identifier is not found, then value -1
	is returned. This method is invoked in find_instance_id(long key).
*/
	private int find_instance_id(int left, int right, long key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			if (inst_idents[middle] < key) {
				left = middle + 1;
			} else if (inst_idents[middle] > key) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return -1;
	}


	boolean insertInstance(int index, CEntity inst, long key) throws SdaiException {
		int length = lengths[index];
		CEntity[] single_instances_sim = instances_sim[index];
		if(length > 0 && single_instances_sim[length - 1].instance_identifier < key) {
			single_instances_sim[length] = inst;
			//instances_sim_start_len = -1;
			invalidate_quick_find();
		} else {
			int pos = findInstancePosition(0, length - 1, index, key);
			if (pos < 0) {
				return false;
			}
			if (pos == 0) {
				System.arraycopy(single_instances_sim, 0, single_instances_sim, 1, length);
				//instances_sim_start_len = -1;
				invalidate_quick_find();
			} else {
				System.arraycopy(single_instances_sim, pos, single_instances_sim, pos + 1, length - pos);
			}
			single_instances_sim[pos] = inst;
		}
		return true;
	}


	private int findInstancePosition(int left, int right, int index, long key) throws SdaiException {
		if (right < left) {
			return 0;
		}
		CEntity inst = instances_sim[index][left];
		if (inst.instance_identifier > key) {
			return 0;
		} else if (inst.instance_identifier == key) {
			return -1;
		}
		inst = instances_sim[index][right];
		if (inst.instance_identifier < key) {
			return right + 1;
		} else if (inst.instance_identifier == key) {
			return -1;
		}
		while (left <= right) {
			if (right-left <= 1) {
				return right;
			}
			int middle = (left + right)/2;
			inst = instances_sim[index][middle];
			if (inst.instance_identifier < key) {
				left = middle;
			} else if (inst.instance_identifier > key) {
				right = middle;
			} else {
				return -1;
			}
		}
		return -1;
	}


	int findInstancePositionRedo(int left, int right, int index, long key) throws SdaiException {
		if (right < left) {
			return 0;
		}
		CEntity inst = instances_sim[index][left];
		if (inst.instance_identifier > key) {
			return 0;
		} else if (inst.instance_identifier == key) {
			if (left == 0) {
				return -right-1;
			}
			return -left;
		}
		inst = instances_sim[index][right];
		if (inst.instance_identifier < key) {
			return right + 1;
		} else if (inst.instance_identifier == key) {
			return -right;
		}
		while (left <= right) {
			if (right-left <= 1) {
				return right;
			}
			int middle = (left + right)/2;
			inst = instances_sim[index][middle];
			if (inst.instance_identifier < key) {
				left = middle;
			} else if (inst.instance_identifier > key) {
				right = middle;
			} else {
				return -middle;
			}
		}
		return -1;
	}


	boolean isMemberListOfModelsAll(CEntity value) throws SdaiException {
		CEntity_definition def = (CEntity_definition)value.getInstanceType();
		int index;
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			index = find_entityRO(def);
		} else {
			index = underlying_schema.owning_model.schemaData.findEntityExtentIndex(def);
		}
		if (index < 0) {
			return false;
		}
		//if (sorted[index]) {
        if ((sim_status[index] & SIM_SORTED) != 0) {
			int ind = find_instance(0, lengths[index] - 1, index, value.instance_identifier);
			if (ind >= 0 && instances_sim[index][ind] == value) {
				return true;
			}
		} else {
			for (int j = 0; j < lengths[index]; j++) {
				if (instances_sim[index][j] == value) {
					return true;
				}
			}
		}
		return false;
	}


/**
	Checks if the specified entity instance belongs to this SdaiModel.
	This method is invoked in isMemberListOfModels in CAggregate, which
	in turn is invoked within isMember method there.
	It works when isMember is applied to an aggregate returned by
	getInstances method in ASdaiModel class.
*/
	boolean isMemberListOfModels(CEntity value, Object type) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return false;
		}
		int true_extent_index;
		if (type == extent_type) {
			true_extent_index = extent_index;
		} else if (type instanceof CEntity_definition) {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (true_extent_index < 0) {
				return false;
//				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (true_extent_index < 0) {
				return false;
//				throw new SdaiException(SdaiException.ED_NDEF);
			}
		}
		int index = true_extent_index;
		boolean first_time = true;
		int i = -1;
		int subtypes[] = null;
		while (index >= 0) {
			//if (sorted[index]) {
			if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
				index = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
			}
			if (index >= 0) {
				if ((sim_status[index] & SIM_SORTED) != 0) {
					int ind = find_instance(0, lengths[index] - 1, index,
						value.instance_identifier);
					if (ind >= 0 && instances_sim[index][ind] == value) {
						return true;
					}
				} else {
					for (int j = 0; j < lengths[index]; j++) {
						if (instances_sim[index][j] == value) {
							return true;
						}
					}
				}
			}
			i++;
			if (first_time) {
				if (underlying_schema == null) {
					getUnderlyingSchema();
				}
				subtypes = underlying_schema.getSubtypes(true_extent_index);
				first_time = false;
			}
			if (i < subtypes.length) {
				index = subtypes[i];
			} else {
				index = -1;
			}
		}
		return false;
	}


/**
	Checks if the specified entity instance belongs to this SdaiModel.
	This method is invoked in isMemberListOfModelsExact in CAggregate, which
	in turn is invoked within isMember method there.
	It works when isMember is applied to an aggregate returned by
	getExactInstances method in ASdaiModel class.
*/
	boolean isMemberListOfModelsExact(CEntity value, Object type) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return false;
		}
		int index;
		if (type == extent_type) {
			index = extent_index;
		} else if (type instanceof CEntity_definition) {
			index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (index < 0) {
				return false;
//				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (index < 0) {
				return false;
//				throw new SdaiException(SdaiException.ED_NDEF);
			}
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			index = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
		}
		if (index < 0) {
			return false;
		}
		//if (sorted[index]) {
        if ((sim_status[index] & SIM_SORTED) != 0) {
			int ind = find_instance(0, lengths[index] - 1, index, value.instance_identifier);
			if (ind >= 0 && instances_sim[index][ind] == value) {
				return true;
			}
		} else {
			for (int j = 0; j < lengths[index]; j++) {
				if (instances_sim[index][j] == value) {
					return true;
				}
			}
		}
		return false;
	}


/**
	Given an index, finds the entity instance in this SdaiModel at the position
	specified by this index.
	This method is invoked in getByIndexListOfModels in CAggregate, which
	in turn is invoked within getByIndexObject method there.
	It works when getByIndexObject is applied to an aggregate returned by
	getInstances method in ASdaiModel class.
*/
	Object getByIndexListOfModels(int index, Object type) throws SdaiException {
		int index_to_type = -1;
		int index_inside = -1;
		index--;
		int true_extent_index;
		if (type == extent_type) {
			true_extent_index = extent_index;
		} else if (type instanceof CEntity_definition) {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (true_extent_index < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (true_extent_index == -1) {
				throw new SdaiException(SdaiException.ED_NDEF);
			} else if (true_extent_index == -2) {
				throw new SdaiException(SdaiException.ED_NVLD);
			}
		}
		int true_extent_indexRO;
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			true_extent_indexRO = find_entityRO(underlying_schema.owning_model.schemaData.entities[true_extent_index]);
		} else {
			true_extent_indexRO = true_extent_index;
		}
		int sum;
		if (true_extent_indexRO >= 0) {
			sum = lengths[true_extent_indexRO];
		} else {
			sum = 0;
		}
		if (index < sum) {
			index_to_type = true_extent_indexRO;
			index_inside = index;
		} else {
			if (underlying_schema == null) {
				getUnderlyingSchema();
			}
			int subtypes[] = underlying_schema.getSubtypes(true_extent_index);
			if (subtypes != null) {
				for (int i = 0; i < subtypes.length; i++) {
					int old_sum = sum;
					int subtype_index;
					if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
						subtype_index = find_entityRO(underlying_schema.owning_model.schemaData.entities[subtypes[i]]);
					} else {
						subtype_index = subtypes[i];
					}
					if (subtype_index >= 0) {
						sum += lengths[subtype_index];
					}
					if (index < sum) {
						index_to_type = subtype_index;
						index_inside = index - old_sum;
						break;
					}
				}
			}
		}
		if (index_to_type < 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		return instances_sim[index_to_type][index_inside];
	}


/**
	Given an index within a row of 'instances_sim', finds the entity instance in
	this SdaiModel at the position specified by this index.
	This method is invoked in getByIndexListOfModelsExact in CAggregate, which
	in turn is invoked within getByIndexObject method there.
	It works when getByIndexObject is applied to an aggregate returned by
	getExactInstances method in ASdaiModel class.
*/
	Object getByIndexListOfModelsExact(int index, Object type) throws SdaiException {
		index--;
		int true_extent_index;
		if (type == extent_type) {
			true_extent_index = extent_index;
		} else if (type instanceof CEntity_definition) {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (true_extent_index < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (true_extent_index == -1) {
				throw new SdaiException(SdaiException.ED_NDEF);
			} else if (true_extent_index == -2) {
				throw new SdaiException(SdaiException.ED_NVLD);
			}
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			true_extent_index = find_entityRO(underlying_schema.owning_model.schemaData.entities[true_extent_index]);
		}
		if (true_extent_index >= 0 && index < lengths[true_extent_index] && index >= 0) {
			return instances_sim[true_extent_index][index];
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


	int toStringListOfModelsAll(StaticFields staticFields, int str_index, CAggregate aggr, boolean first) throws SdaiException {
		for (int i = 0; i < lengths.length; i++) {
			if (lengths[i] <= 0) continue;
			for (int j = 0; j < lengths[i]; j++) {
				CEntity inst = instances_sim[i][j];
				if (str_index + 2 >= staticFields.instance_as_string.length) {
					CAggregate.enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				first = false;
				staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
				str_index = aggr.long_to_byte_array(staticFields, inst.instance_identifier, str_index);
			}
		}
		return str_index;
	}


/**
	Writes entity instances of the specified entity data type and its subtypes
	to a string.
	This method is invoked in toStringListOfModels in CAggregate, which
	in turn is invoked within toString method there.
	It works when toString is applied to an aggregate returned by
	getInstances method in ASdaiModel class.
*/
	int toStringListOfModels(StaticFields staticFields, int str_index, CAggregate aggr, boolean first, Object type) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return str_index;
		}
		int true_extent_index;
		if (type == extent_type) {
			true_extent_index = extent_index;
		} else if (type instanceof CEntity_definition) {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (true_extent_index < 0) {
				return str_index;
//				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			true_extent_index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (true_extent_index < 0) {
				return str_index;
//				throw new SdaiException(SdaiException.ED_NDEF);
			}
		}
		int index = true_extent_index;
		boolean first_time = true;
		int i = -1;
		int subtypes[] = null;
		while (index >= 0) {
			if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
				index = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
			}
			if (index >= 0) {
				for (int j = 0; j < lengths[index]; j++) {
					CEntity inst = instances_sim[index][j];
					if (str_index + 2 >= staticFields.instance_as_string.length) {
						CAggregate.enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
					}
					if (!first) {
						staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
					}
					first = false;
					staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
					str_index = aggr.long_to_byte_array(staticFields, inst.instance_identifier, str_index);
				}
			}
			i++;
			if (first_time) {
				if (underlying_schema == null) {
					getUnderlyingSchema();
				}
				subtypes = underlying_schema.getSubtypes(true_extent_index);
				first_time = false;
			}
			if (i < subtypes.length) {
				index = subtypes[i];
			} else {
				index = -1;
			}
		}
		return str_index;
	}


/**
	Writes entity instances of the specified entity data type (but not its subtypes)
	to a string.
	This method is invoked in toStringListOfModelsExact in CAggregate, which
	in turn is invoked within toString method there.
	It works when toString is applied to an aggregate returned by
	getExactInstances method in ASdaiModel class.
*/
	int toStringListOfModelsExact(StaticFields staticFields, int str_index, CAggregate aggr, boolean first, Object type) throws SdaiException {
		if ((mode & MODE_MODE_MASK) <= 0) {
			return str_index;
		}
		int index;
		if (type == extent_type) {
			index = extent_index;
		} else if (type instanceof CEntity_definition) {
			index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
			if (index < 0) {
				return str_index;
//				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)type);
			}
		} else {
			index =
				underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)type);
			if (index < 0) {
				return str_index;
//				throw new SdaiException(SdaiException.ED_NDEF);
			}
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY && repository != SdaiSession.systemRepository) {
			index = find_entityRO(underlying_schema.owning_model.schemaData.entities[index]);
		}
		if (index < 0) {
			return str_index;
		}
		for (int j = 0; j < lengths[index]; j++) {
			CEntity inst = instances_sim[index][j];
			if (str_index + 2 >= staticFields.instance_as_string.length) {
				CAggregate.enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
			}
			if (!first) {
				staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
			}
			first = false;
			staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
			str_index = aggr.long_to_byte_array(staticFields, inst.instance_identifier, str_index);
		}
		return str_index;
	}


/**
 * Deprecated, use
 * {@link #getInstances(EEntity_definition type) getInstances(EEntity_definition type)}
 * instead.
 */
	public AEntity getEntityExtentInstances(EEntity_definition entity_definition)
			throws SdaiException {
		return getInstances(entity_definition);
	}


/**
 * Deprecated, use
 * {@link #getInstances(Class type) getInstances(Class type)}
 * instead.
 */
	public AEntity getEntityExtentInstances(Class provided_class)
			throws SdaiException {
		return getInstances(provided_class);
	}


/**
	Preparing auxiliary array 'for_instances_sorting' to sort instances
	(separately of each entity data type) in increasing order
	of their identifiers.
*/
	void prepareForSorting() {
		int max_inst_count = -1;
		for (int i = 0; i < instances_sim.length; i++) {
			if (lengths[i] > max_inst_count) {
				max_inst_count = lengths[i];
			}
		}
//System.out.println(" SdaiModel   in prepareForSorting   max = " + max +
//"   model: " + name);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.for_instances_sorting == null) {
			if (ARRAY_FOR_INSTANCES_SORTING_SIZE >= max_inst_count) {
				staticFields.for_instances_sorting = new CEntity[ARRAY_FOR_INSTANCES_SORTING_SIZE];
			} else {
				staticFields.for_instances_sorting = new CEntity[max_inst_count];
			}
		} else if (max_inst_count > staticFields.for_instances_sorting.length) {
//System.out.println(" SdaiModel   inside if in  prepareForSorting");
			staticFields.for_instances_sorting = new CEntity[max_inst_count];
		}
	}


/**
	Sorting instances of an entity data type (specified by an index/reference
	to an ordered list of entity types) in increasing order
	of their identifiers.
*/
	void sortInstances(int entity_index) {
//		for (int i = 0; i < lengths[entity_index]; i++) {
//			for_instances_sorting[i] = instances_sim[entity_index][i];
//		}
		StaticFields staticFields = StaticFields.get();
		System.arraycopy(instances_sim[entity_index], 0, staticFields.for_instances_sorting, 0, lengths[entity_index]);
		sortInstances(staticFields.for_instances_sorting, instances_sim[entity_index], 0, lengths[entity_index]);
	}


/**
	Sorting submitted array of entity instances in increasing order
	of their identifiers.
	It is a recursive procedure invoked outside in sortInstances(int entity_index).
*/
	private void sortInstances(CEntity [] s_instances, CEntity [] instances,
			int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index &&
						instances[j-1].instance_identifier > instances[j].instance_identifier; j--) {
					CEntity obj = instances[j-1];
					instances[j-1] = instances[j];
					instances[j] = obj;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortInstances(instances, s_instances, start_index, middle);
		sortInstances(instances, s_instances, middle, end_index);
		if (s_instances[middle-1].instance_identifier <= s_instances[middle].instance_identifier) {
			System.arraycopy(s_instances, start_index, instances, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					s_instances[m].instance_identifier <= s_instances[n].instance_identifier) {
				instances[i] = s_instances[m++];
			} else {
				instances[i] = s_instances[n++];
			}
		}
	}


/**
	Removing auxiliary array 'for_instances_sorting' used to sort instances.
*/
	void closeSorting() {
		StaticFields staticFields = StaticFields.get();
		for (int i = 0; i < max_inst_count; i++) {
			staticFields.for_instances_sorting[i] = null;
		}
	}


/**
	Sorting all instances of this model in increasing order
	of their identifiers. The count of instances is returned,
	while the instances themselves appear in java array 'instances_all'.
*/
	int sortAllInstances() {
		int i;
		int count = 0;
		int inst_count = 0;
		pop_ent_count = 0;
		if (to_entities == null) {
			to_entities = new int[instances_sim.length];
		}
		for (i = 0; i < lengths.length; i++) {
			if (lengths[i] > 0) {
				inst_count += lengths[i];
				to_entities[pop_ent_count++] = i;
			}
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instances_all == null) {
			if (INSTANCES_ALL_INIT_SIZE < inst_count) {
				staticFields.instances_all = new CEntity[inst_count];
				staticFields.instances_all_aux = new CEntity[inst_count];
			} else {
				staticFields.instances_all = new CEntity[INSTANCES_ALL_INIT_SIZE];
				staticFields.instances_all_aux = new CEntity[INSTANCES_ALL_INIT_SIZE];
			}
		} else if (staticFields.instances_all.length < inst_count) {
			int new_length = staticFields.instances_all.length * 2;
			if (new_length < inst_count) {
				new_length = inst_count;
			}
			staticFields.instances_all = new CEntity[new_length];
			staticFields.instances_all_aux = new CEntity[new_length];
		}

		for (i = 0; i < lengths.length; i++) {
			for (int k = 0; k < lengths[i]; k++) {
				staticFields.instances_all[count] = instances_sim[i][k];
				staticFields.instances_all_aux[count] = staticFields.instances_all[count];
				count++;
			}
		}
		if (pop_ent_count > 1) {
			sortAllInstances(staticFields.instances_all_aux, staticFields.instances_all, 0, pop_ent_count - 1, 0, count);
		}
		return inst_count;
	}


/**
	Sorting of entity instances in increasing order of their identifiers.
	It is a recursive procedure invoked outside in sortAllInstances().
	The instances of each entity type are already sorted.
	The task of this method is to merge all these sortings into one big array.
*/
	private void sortAllInstances(CEntity [] instances_all_aux, CEntity [] instances_all,
			int start_type, int end_type, int start_index, int end_index) {
		int i;
		int middle;
		int middle_type = -1;
		int gap = end_index - start_index;
		int type_count = end_type - start_type + 1;
		if (type_count == 2) {
			middle = start_index + lengths[to_entities[start_type]];
		} else if (type_count == 3) {
			if (lengths[to_entities[start_type]] < lengths[to_entities[end_type]]) {
				middle = start_index + lengths[to_entities[start_type]] + lengths[to_entities[start_type + 1]];
				sortAllInstances(instances_all, instances_all_aux, start_type, start_type + 1, start_index, middle);
			} else {
				middle = start_index + lengths[to_entities[start_type]];
				sortAllInstances(instances_all, instances_all_aux, end_type - 1, end_type, middle, end_index);
			}
		} else {
			int half = gap/2;
			int count = 0;
			for (i = start_type; i <= end_type; i++) {
				count += lengths[to_entities[i]];
				if (count >= half) {
					if (i == start_type) {
						middle_type = i + 1;
						count += lengths[to_entities[i + 1]];
					} else if (i >= end_type - 1) {
						middle_type = end_type - 2;
						count -= lengths[to_entities[end_type - 1]];
						if (i == end_type) {
							count -= lengths[to_entities[end_type]];
						}
					} else {
						middle_type = i;
					}
					break;
				}
			}
			middle = start_index + count;
//System.out.println("  SdaiModel !!!!!!  start_index: " + start_index +
//"   middle: " + middle +
//"   start_type: " + start_type + "    middle_type: " + middle_type);
			sortAllInstances(instances_all, instances_all_aux, start_type, middle_type,
				start_index, middle);
//System.out.println("  SdaiModel &&&&&  middle: " + middle +
//"   end_index: " + end_index +
//"   middle_type: " + middle_type + "    end_type: " + end_type);
			sortAllInstances(instances_all, instances_all_aux, middle_type + 1, end_type,
				middle, end_index);
		}
		if (instances_all_aux[middle-1].instance_identifier <=
				instances_all_aux[middle].instance_identifier) {
			System.arraycopy(instances_all_aux, start_index, instances_all, start_index, gap);
			return;
		}
		int m, n;
		for (i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					instances_all_aux[m].instance_identifier <=
					instances_all_aux[n].instance_identifier) {
				instances_all[i] = instances_all_aux[m++];
			} else {
				instances_all[i] = instances_all_aux[n++];
			}
		}
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public EEntity create(ComplexEntityValue values) throws SdaiException {
//		synchronized (syncObject) {
		CEntity_definition def = values.def;
		if (def == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		CEntity instance = createEntityInstanceInternal(def, 0);
		instance.setAll(values);
		//<--VV--030310--
        /*if (repository.largest_identifier < Long.MAX_VALUE) {
			repository.largest_identifier++;
			instance.instance_identifier = repository.largest_identifier;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_NVLI;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}*/
        instance.instance_identifier = repository.incPersistentLabel(this);
        instance.instance_position = CEntity.INS_MASK | instance.instance_position; //--VV-- Instance state tracking --
        //--VV--030310-->
		modified = true;
		return instance;
//		} // syncObject
	}


	protected SdaiModelHeader toSdaiModelHeader() throws SdaiException {
		SdaiModelHeader modelHeader = new SdaiModelHeader();
		modelHeader.name = name;
		modelHeader.schema = getUnderlyingSchemaString();
		modelHeader.changeDate = getChangeDate();
		modelHeader.defaultLanguage = language;
		int count;
		if (context_identifiers == null) {
			count = 0;
		} else {
			count = context_identifiers.myLength;
		}
		modelHeader.contextIdentifiers = new String[count];
		for (int i = 0; i < count; i++) {
			modelHeader.contextIdentifiers[i] = context_identifiers.getByIndex(i + 1);
		}
		return modelHeader;
	}


/**
	Loads the data contained in the header of the specified remote model to this SdaiModel.
*/
	protected void fromSdaiModelHeader(SdaiModelHeader modelHeader) throws SdaiException {
//		name = modelHeader.name;
//		identifier = modelHeader.id;
//		String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
//		dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
//		if (underlying_schema != null && !underlying_schema.getName(null).equals(modelHeader.schema)) {
//			underlying_schema = null;
//		}
		if (modelHeader.changeDate != null) {
			change_date = repository.session.cal.timeStampToLong(modelHeader.changeDate);
		}
		language = modelHeader.defaultLanguage;
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (modelHeader.contextIdentifiers != null) {
			for (int i = 0; i < modelHeader.contextIdentifiers.length; i++) {
				context_identifiers.addByIndexPrivate(i + 1, modelHeader.contextIdentifiers[i]);
			}
		}
	}


/**
	Increases the size of the auxiliary array 'ex_models' twice.
*/
	void ensureModsCapacity() {
		int new_length = ex_models.length * 2;
		SdaiModel [] new_array = new SdaiModel[new_length];
		System.arraycopy(ex_models, 0, new_array, 0, ex_models.length);
		ex_models = new_array;
	}


/**
	Increases the size of the auxiliary array 'defs' used to store and
	afterwards process given entity definitions in getInstances methods
	having parameter an array of some type (either EEntity_definition or Class).
*/
	private void ensureDefsCapacity() {
		StaticFields staticFields = StaticFields.get();
		int new_length = staticFields.defs.length * 2;
		CEntity_definition [] new_array = new CEntity_definition[new_length];
		System.arraycopy(staticFields.defs, 0, new_array, 0, staticFields.defs.length);
		staticFields.defs = new_array;
	}


/**
	Increases the size of byte array used to store strings (as values of
	attributes) when reading binary files.
*/
	private static void enlarge_string(StaticFields staticFields, int demand) {
		int new_length = staticFields.string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_name[] = new byte[new_length];
		staticFields.string = new_instance_name;
	}


	private static void ensureIdsCapacity(StaticFields staticFields) {
		int new_length = staticFields.ids.length * 2;
		long [] new_ids = new long[new_length];
		System.arraycopy(staticFields.ids, 0, new_ids, 0, staticFields.ids.length);
		staticFields.ids = new_ids;
	}


/**
	Increases the size of an auxiliary array used to detect all defined types
	encountered in the definitions of populated entity data types within
	the schema which is underlying for this SdaiModel.
*/
	private void enlargeChain() {
		int new_length = def_type_chain.length * 2;
		CDefined_type [] new_def_type_chain = new CDefined_type[new_length];
		System.arraycopy(def_type_chain, 0, new_def_type_chain, 0, def_type_chain.length);
		def_type_chain = new_def_type_chain;
	}

//This method is not used
///**
//	A method for debugging purposes.
//*/
//	private void print_entity_values(long instance_identifier) throws SdaiException {
//		int count = entity_values.def.noOfPartialEntityTypes;
//		System.out.println("INSTANCE: #" + instance_identifier);
//		for (int i = 0; i < count; i++) {
//			EntityValue partval = entity_values.entityValues[i];
//			String def_name = entity_values.def.partialEntityTypes[i].name;
//			System.out.println("****** partial entity no. " + i + "   entity: " + def_name);
//			for (int j = 0; j < partval.count; j++) {
//				print_value(partval.values[j]);
//				System.out.println("");
//			}
//		}
//	}


	void print_entity_values(ComplexEntityValue entity_vals, long instance_identifier) throws SdaiException {
		int count = entity_vals.def.noOfPartialEntityTypes;
System.out.println("****** count = " + count + "   entity_vals.def: " + entity_vals.def.getName(null));
		System.out.println("INSTANCE: #" + instance_identifier);
		for (int i = 0; i < count; i++) {
			EntityValue partval = entity_vals.entityValues[i];
			String def_name = entity_vals.def.partialEntityTypes[i].name;
			System.out.println("****** partial entity no. " + i + "   entity: " + def_name);
			for (int j = 0; j < partval.count; j++) {
//System.out.println("?????   partval.values[j]: " + partval.values[j]);
				print_value(partval.values[j]);
				System.out.println("");
			}
		}
	}


/**
	A method for debugging purposes.
*/
	private void print_value(Value val) {
		Value value_next;
		switch (val.tag) {
			case PhFileReader.MISSING:
				System.out.print("$  ");
				break;
			case PhFileReader.REDEFINE:
				System.out.print("*  ");
				break;
			case PhFileReader.INTEGER:
				System.out.print(val.integer + "  ");
				break;
			case PhFileReader.REAL:
				System.out.print(val.real + "  ");
				break;
			case PhFileReader.LOGICAL:
			case PhFileReader.BOOLEAN:
				if (val.integer == 0) {
					System.out.print(".F.  ");
				} else if (val.integer == 1) {
					System.out.print(".T.  ");
				} else if (val.integer == 2) {
					System.out.print(".U.  ");
				}
				break;
			case PhFileReader.ENUM:
				System.out.print("." + val.string + ".  ");
				break;
			case PhFileReader.STRING:
				System.out.print("'" + val.string + "'  ");
				break;
			case PhFileReader.BINARY:
				System.out.print("\"" + PhFileReader.BINARY + "\"  ");
				break;
			case PhFileReader.TYPED_PARAMETER:
				System.out.print(val.string + "(");
				value_next = val.nested_values[0];
				print_value(value_next);
				System.out.print(")  ");
				break;
			case PhFileReader.ENTITY_REFERENCE:
				Object ref = val.reference;
				if (ref != null) {
					if (ref instanceof CEntity) {
						CEntity instanc = (CEntity)ref;
						long id = instanc.instance_identifier;
						System.out.print("#" + id + "  ");
					} else {
						String ref_class = ref.getClass().getName();
						System.out.print(ref_class + "  ");
					}
				} else {
					System.out.print("REF  is NULL");
				}
				break;
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
				System.out.print("REF FORWARD  ");
				break;
			case PhFileReader.EMBEDDED_LIST:
				System.out.print("(");
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					print_value(value_next);
				}
				System.out.print(")  ");
				break;
			}
	}

	/** This is a callback method.
	 * It is called during commit from SdaiTransaction.commit
	 * when model header needs to be committed.
	 * Normally should be overridden in remote models.
	 */
	boolean committingHeader(boolean commit_bridge) throws SdaiException {
		boolean modif_remote =
			modified_outside_contents
			|| ((modified || modified_by_import) && (mode & MODE_MODE_MASK) != NO_ACCESS);
		return committingHeaderInternal(commit_bridge, modif_remote);
	}

	/** This is a callback method.
	 * It is called during commit from SdaiTransaction.commit
	 * when model state needs to be committed.
	 */
	boolean committing(boolean commit_bridge) throws SdaiException {
		boolean modif_remote = modified_outside_contents || ((modified || modified_by_import) && (mode & MODE_MODE_MASK) != NO_ACCESS);
		boolean modif_all = (modified || modified_by_import) && (mode & MODE_MODE_MASK) != NO_ACCESS;
		return committingInternal(commit_bridge, modif_remote, modif_all);
	}

	protected boolean get_created() {
		return created;
	}

	protected void set_created(boolean cr) {
		created = cr;
	}

	protected SessionRemote get_bridgeSession() {
		return repository.session.bridgeSession;
	}

	protected SessionRemote get_bridgeSession(SdaiRepository rep) {
		return rep.session.bridgeSession;
	}
	protected void set_fields(boolean modif_all) {
		if (modif_all) {
			if (inst_deleted) {
				inst_deleted_permanently = true;
			}
			inst_deleted = false;
			modified = false;
			modified_by_import = false;
		}
		modified_outside_contents = false;
		modified_outside_contents_by_import = false;
		committed = true;
	}

	/**
	 * Writes this model in XML representation to specified stream.
	 * Output format is controlled by <code>instanceReader</code> parameter.
	 *
	 * @param location an <code>OutputStream</code> to write XML to
	 * @param instanceReader an {@link jsdai.xml.InstanceReader} describing output format
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see jsdai.xml.InstanceReader jsdai.xml.InstanceReader
	 * @see jsdai.xml.LateBindingReader jsdai.xml.LateBindingReader
	 */
	public void exportXml(OutputStream location, InstanceReader instanceReader) throws SdaiException {
		try {
			instanceReader.serialize(location, new SdaiInputSource(this));
		} catch(SdaiException e) {
			throw e;
		} catch(Exception e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}


	/** This is a callback method.
	 * It is called during commit from SdaiTransaction.commit
	 * when model is to be deleted.
	 */
	boolean deleting(boolean commit_bridge, SdaiRepository repo) throws SdaiException {
		return deletingInternal(commit_bridge, repo);
	}

	/**
	 * Callback method which is called when the entity is deleted
	 */
	void entityDeleted(CEntity instance) throws SdaiException {
		entityDeletedInternal(instance);
	}

	/** This is a callback method.
	 * It is called when model is deleted before it's internal state is
	 * removed and before possible commit is performed (if transaction is aborted
	 * this never happens)
	 */
	protected void scheduledForDeletion() throws SdaiException { }

	protected static long getInstanceIdentifier(CEntity instance) {
		return instance.instance_identifier;
	}

	protected final void assignNewInstanceIdentifier(CEntity instance) throws SdaiException {
		instance.instance_identifier = repository.incPersistentLabel(this);
	}

	abstract protected boolean committingHeaderInternal(boolean commit_bridge,
														boolean modif_remote) throws SdaiException;
	/** This is a callback method.
	 * It is internally called during commit from SdaiTransaction.commit
	 * when model state needs to be committed.
	 */
	abstract protected boolean committingInternal(boolean commit_bridge, boolean modif_remote, boolean modif_all) throws SdaiException;

	/** This is a callback method.
	 * It is internally called during commit from SdaiTransaction.commit
	 * when model is to be deleted.
	 */
	abstract protected boolean deletingInternal(boolean commit_bridge, SdaiRepository repo) throws SdaiException;

	/**
	 * Callback method which is internally called when the entity is deleted
	 */
	protected void entityDeletedInternal(CEntity instance) throws SdaiException {
		repository.removeEntityExternalData(instance, true, false);
	}

	boolean aborting(SdaiTransaction trans, boolean endTransaction) throws SdaiException {
		inst_deleted = false;
		boolean result = abortingInternal(trans, modified, modified_outside_contents, endTransaction);
		modified = false;
		return result;
	}

	protected abstract boolean abortingInternal(SdaiTransaction trans, boolean modif,
		boolean modif_outside_contents, boolean endTransaction) throws SdaiException;

	protected void abortingCreated() throws SdaiException {
		setMode(READ_ONLY);
		exists = false;
		deleteSdaiModelWork(false, true, false, false);
		resolveInConnectors(true);
	}

	protected void unsetModified_outside_contents() throws SdaiException {
		modified_outside_contents = false;
	}

	protected void deletingContents() throws SdaiException {
		exists = true;
		deleteContentsRemote();
	}

	protected void refreshingContents() throws SdaiException {
		exists = true;
		refreshContents();
	}

	void refreshContents() throws SdaiException {
		int i, j;
		Connector con = outConnectors;
		while (con != null) {
			con.disconnect();
			con = outConnectors;
		}
//System.out.println("SdaiModel::: after con.disconnect() for model: " + name);System.out.flush();
		if (instances_sim == null) {
			return;
		}
		deleting = true;
		CEntity [] row_of_instances;
		CEntity instance;
		for (i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] <= 0) {
				continue;
			}
			row_of_instances = instances_sim[i];
			for (j = 0; j < lengths[i]; j++) {
				instance = row_of_instances[j];
				if (optimized) {
					instance.unsetInverseReferencesNoInverse(true, true, false);
				} else {
//System.out.println("SdaiModel  ::: unset for instance: #" + instance.instance_identifier);System.out.flush();
					instance.unsetInverseReferences(true, true);
//System.out.println("SdaiModel  ::: unset finished for instance: #" + instance.instance_identifier);System.out.flush();
				}
			}
		}
//System.out.println("SdaiModel::: after unsetInverseReferences for model: " + name);System.out.flush();
		refresh_in_abort = true;
		for (i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] <= 0) {
				continue;
			}
			row_of_instances = instances_sim[i];
			for (j = 0; j < lengths[i]; j++) {
				instance = row_of_instances[j];
				bypass_setAll = true;
				try {
					instance.setAll(null);
				} finally {
					bypass_setAll = false;
				}
				instance.inverseList = null;
			}
		}
		refresh_in_abort = false;
//System.out.println("SdaiModel::: after setAll() for model: " + name);System.out.flush();
		deleting = false;
	}


	boolean isRemote() throws SdaiException {
		return isRemoteInternal();
	}

	protected abstract boolean isRemoteInternal() throws SdaiException;

	boolean checkModel(Object current_member) throws SdaiException {
		return checkModelInternal(current_member);
	}

	protected abstract boolean checkModelInternal(Object current_member) throws SdaiException;

	protected abstract void attachRemoteModel(Object current_member) throws SdaiException;

	protected abstract void endingAccess() throws SdaiException;


	void delete_created(long inst_id, int pop_index, int inst_index, boolean modif) throws SdaiException {
		if (instances_sim[pop_index][inst_index].instance_identifier != inst_id) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		repository.removeEntityExternalData(instances_sim[pop_index][inst_index], true, false);
		instances_sim[pop_index][inst_index].deletedObject();
		System.arraycopy(instances_sim[pop_index], inst_index+1, instances_sim[pop_index],
			inst_index, instances_sim[pop_index].length-inst_index-1);
		lengths[pop_index]--;
		modified = modif;
		invalidate_quick_find();
	}


	CEntity create_deleted(RandomAccessFile ur_f, long inst_id, int pop_index, int inst_index, boolean modif, boolean inst_del)
			throws  java.io.IOException, SdaiException {
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntityDefinition edef = sch_data.entities[pop_index];
		SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
		CEntity instance = m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
			this, -1, 0);
		instance.instance_identifier = inst_id;
		instance.load_values(ur_f, (CEntity_definition)edef, false);
		instance.modified();

		CEntity[] pop_instances_sim = instances_sim[pop_index];
		System.arraycopy(pop_instances_sim, inst_index, pop_instances_sim, inst_index + 1, lengths[pop_index] - inst_index);
		pop_instances_sim[inst_index] = instance;
		lengths[pop_index]++;
		modified = modif;
 		inst_deleted = inst_del;
		invalidate_quick_find();
		repository.restoreExternalDataForInstance(instance);
		return instance;
	}


	CEntity_definition restore_modified(RandomAccessFile ur_f, long inst_id, int pop_index, int inst_index, boolean modif)
			throws  java.io.IOException, SdaiException {
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntity_definition edef = (CEntity_definition)sch_data.entities[pop_index];
		CEntity instance = instances_sim[pop_index][inst_index];
		if (instance.instance_identifier != inst_id) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		bypass_setAll = true;
		try {
			instance.setAll(null);
		} finally {
			bypass_setAll = false;
		}
		instance.load_values(ur_f, edef, false);
		modified = modif;
		return edef;
	}


	CEntity create_substituted(RandomAccessFile ur_f, long inst_id, int pop_index)
			throws  java.io.IOException, SdaiException {
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntityDefinition edef = sch_data.entities[pop_index];
		SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
		CEntity instance = m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
			this, -1, 0);
		instance.load_values(ur_f, (CEntity_definition)edef, false);
		instance.instance_identifier = inst_id;
		instance.modified();
		return instance;
	}


	void delete_substitute(RandomAccessFile ur_f, long f_pointer, CEntity older,
			long inst_id, int pop_index, int inst_index, boolean modif) throws  java.io.IOException, SdaiException {
		verify_model_RW(ur_f, f_pointer, false);
		CEntity substitute = instances_sim[pop_index][inst_index];
		if (substitute.owning_model == null) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (substitute.instance_identifier != inst_id) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (optimized) {
			substitute.changeInverseReferencesNoInverse(substitute, older, false);
		} else {
			substitute.changeInverseReferences(substitute, older, false, false);
		}
		older.inverseList = substitute.inverseList;
		System.arraycopy(instances_sim[pop_index], inst_index+1, instances_sim[pop_index],
			inst_index, instances_sim[pop_index].length-inst_index-1);
		lengths[pop_index]--;
		bypass_setAll = true;
		try {
			substitute.setAll(null);
		} finally {
			bypass_setAll = false;
		}
		substitute.deleted();
		substitute.deletedObject();
		substitute.owning_model = null;
		modified = modif;
	}


	void include_substituted(RandomAccessFile ur_f, CEntity instance, int pop_index, int inst_index, boolean modif, boolean inst_del)
			throws  java.io.IOException, SdaiException {
		int true_index;
		SdaiModel m = instance.owning_model;
		if ((m.mode & MODE_MODE_MASK) == READ_ONLY) {
			true_index = m.find_entityRO(m.dictionary.schemaData.entities[pop_index]);
		} else {
			true_index = pop_index;
		}
//		CEntity[] pop_instances_sim = instances_sim[pop_index];
		CEntity[] pop_instances_sim = instances_sim[true_index];
//		System.arraycopy(pop_instances_sim, inst_index, pop_instances_sim, inst_index + 1, lengths[pop_index] - inst_index);
		System.arraycopy(pop_instances_sim, inst_index, pop_instances_sim, inst_index + 1, lengths[true_index] - inst_index);
		pop_instances_sim[inst_index] = instance;
//		lengths[pop_index]++;
		lengths[true_index]++;
		modified = modif;
 		inst_deleted = inst_del;
		invalidate_quick_find();
	}


	CEntity create_again(RandomAccessFile ur_f, long f_pointer, long inst_id, int pop_index, int inst_index)
			throws  java.io.IOException, SdaiException {
		verify_model_RW(ur_f, f_pointer, true);
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntityDefinition edef = sch_data.entities[pop_index];
		SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
		CEntity instance = m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
			this, -1, 0);
		instance.load_values(ur_f, (CEntity_definition)instance.getInstanceType(), false);
		instance.instance_identifier = inst_id;
		instance.modified();
		CEntity[] pop_instances_sim = instances_sim[pop_index];
		System.arraycopy(pop_instances_sim, inst_index, pop_instances_sim, inst_index + 1, lengths[pop_index] - inst_index);
		pop_instances_sim[inst_index] = instance;
		lengths[pop_index]++;
		modified = true;
		invalidate_quick_find();
		repository.restoreExternalDataForInstance(instance);
		return instance;
	}


	CEntity copy_again(RandomAccessFile ur_f, long f_pointer, long inst_id, int pop_index, int inst_index)
			throws  java.io.IOException, SdaiException {
		verify_model_RW(ur_f, f_pointer, true);
		CEntity instance;
		CEntity[] pop_instances_sim = instances_sim[pop_index];
		if (inst_index < lengths[pop_index] && pop_instances_sim[inst_index] != null &&
				pop_instances_sim[inst_index].instance_identifier == inst_id) {
			instance = pop_instances_sim[inst_index];
		} else {
			SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
			CEntityDefinition edef = sch_data.entities[pop_index];
			SdaiModel m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
			instance = m.schemaData.super_inst.makeInstance(((CEntityDefinition)edef).getEntityClass(),
				this, -1, 0);
			instance.instance_identifier = inst_id;
			System.arraycopy(pop_instances_sim, inst_index, pop_instances_sim, inst_index + 1, lengths[pop_index] - inst_index);
			pop_instances_sim[inst_index] = instance;
			lengths[pop_index]++;
		}
		instance.load_values(ur_f, (CEntity_definition)instance.getInstanceType(), true);
		instance.modified();
		modified = true;
		invalidate_quick_find();
		repository.restoreExternalDataForInstance(instance);
		return instance;
	}


	CEntity_definition delete_again(RandomAccessFile ur_f, long f_pointer, long inst_id, int pop_index, int inst_index)
			throws  java.io.IOException, SdaiException {
		verify_model_RW(ur_f, f_pointer, true);
		CEntity inst = instances_sim[pop_index][inst_index];
		CEntity_definition def = (CEntity_definition)inst.getInstanceType();
		if (inst.owning_model == null) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (inst.instance_identifier != inst_id) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		repository.removeEntityExternalData(inst, true, false);
		if (optimized) {
			inst.changeInverseReferencesNoInverse(inst, null, false);
		} else {
			inst.changeInverseReferences(inst, null, false, false);
		}
		System.arraycopy(instances_sim[pop_index], inst_index+1, instances_sim[pop_index],
			inst_index, instances_sim[pop_index].length-inst_index-1);
		lengths[pop_index]--;
		bypass_setAll = true;
		try {
			inst.setAll(null);
		} finally {
			bypass_setAll = false;
		}
		inst.deleted();
		inst.deletedObject();
		inst_deleted = true;
		inst.owning_model = null;
		return def;
	}


	void modified_again(RandomAccessFile ur_f, long f_pointer, long inst_id, int pop_index, int inst_index)
			throws  java.io.IOException, SdaiException {
		verify_model_RW(ur_f, f_pointer, true);
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntity_definition edef = (CEntity_definition)sch_data.entities[pop_index];
		CEntity instance = instances_sim[pop_index][inst_index];
		if (instance.owning_model == null) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (instance.instance_identifier != inst_id) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		bypass_setAll = true;
		try {
			instance.setAll(null);
		} finally {
			bypass_setAll = false;
		}
		instance.load_values(ur_f, edef, false);
		instance.modified();
	}


	CEntity substitute_again(RandomAccessFile ur_f, long f_pointer, SdaiModel old_mod, long inst_id,
			int pop_index, int inst_index, long new_inst_id, int new_pop_index, int new_inst_index,
			boolean modif) throws java.io.IOException, SdaiException {
		if (old_mod == null) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		old_mod.verify_model_RO(ur_f, f_pointer);
		verify_model_RW(ur_f, f_pointer, true);
		CEntity older = old_mod.instances_sim[pop_index][inst_index];
		if (older.instance_identifier != inst_id) {
			ur_f.seek(f_pointer);
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CEntity_definition old_def = (CEntity_definition)older.getInstanceType();
		SchemaData sch_data = underlying_schema.modelDictionary.schemaData;
		CEntity_definition new_def = (CEntity_definition)sch_data.entities[new_pop_index];
		if (old_def == new_def) {
			old_mod.bypass_setAll = true;
			try {
				older.setAll(null);
			} finally {
				old_mod.bypass_setAll = false;
			}
			if (old_mod != this) {
				older.deleteInstanceWeak();
				older.owning_model = this;
				older.instance_identifier = new_inst_id;
				if (repository == old_mod.repository) {
					insertEntityInstance_undo(older, new_pop_index, new_inst_index);
				} else {
					insertEntityInstance_undo(older, new_pop_index, new_inst_index);
					old_mod.inst_deleted = true;
				}
				modified = true;
			} else {
				modified = modif;
			}
//			repository.session.bypass_values(ur_f, new_def);
			older.load_values(ur_f, new_def, false);
			return older;
		}

		SdaiModel m = ((CSchema_definition)new_def.owning_model.described_schema).modelDictionary;
		CEntity substitute = m.schemaData.super_inst.makeInstance(((CEntityDefinition)new_def).getEntityClass(),
			this, -1, 0);
		substitute.instance_identifier = new_inst_id;
		substitute.modified();
//		substitute.load_values(ur_f, new_def);
		if (repository != old_mod.repository) {
			old_mod.inst_deleted = true;
		}
		if (old_mod.optimized) {
			older.changeInverseReferencesNoInverse(older, substitute, false);
		} else {
			older.changeInverseReferences(older, substitute, false, false);
		}
		substitute.inverseList = older.inverseList;
		insertEntityInstance_undo(substitute, new_pop_index, new_inst_index);

		boolean saved_value = old_mod.inst_deleted;
		old_mod.delete_again(ur_f, f_pointer, inst_id, pop_index, inst_index);
		old_mod.inst_deleted = saved_value;
		substitute.load_values(ur_f, new_def, false);
		modified = true;
		return substitute;
	}


	private void insertEntityInstance_undo(CEntity inst, int new_pop_index, int  new_inst_index) throws  SdaiException {
		int length = lengths[new_pop_index];
		CEntity[] single_instances_sim = instances_sim[new_pop_index];
		System.arraycopy(single_instances_sim, new_inst_index, single_instances_sim, new_inst_index + 1, length - new_inst_index);
		single_instances_sim[new_inst_index] = inst;
		lengths[new_pop_index]++;
		invalidate_quick_find();
	}


    void verify_model_RO(RandomAccessFile ur_f, long f_pointer) throws java.io.IOException, SdaiException {
		SdaiSession se = repository.session;
		if (repository == null) {
			ur_f.seek(f_pointer);
			se.restore_group_back();
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			ur_f.seek(f_pointer);
			se.restore_group_back();
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == NO_ACCESS) {
			ur_f.seek(f_pointer);
			se.restore_group_back();
			throw new SdaiException(SdaiException.MX_NDEF, this);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			ur_f.seek(f_pointer);
			se.restore_group_back();
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
	}


    void verify_model_RW(RandomAccessFile ur_f, long f_pointer, boolean forward) throws java.io.IOException, SdaiException {
		SdaiSession se = repository.session;
		if (repository == null) {
			ur_f.seek(f_pointer);
			if (forward) {
				se.restore_group_back();
			} else {
				se.restore_group_forward();
			}
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (!repository.active) {
			ur_f.seek(f_pointer);
			if (forward) {
				se.restore_group_back();
			} else {
				se.restore_group_forward();
			}
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) != READ_WRITE) {
			ur_f.seek(f_pointer);
			if (forward) {
				se.restore_group_back();
			} else {
				se.restore_group_forward();
			}
			throw new SdaiException(SdaiException.MX_NRW, this);
		}
	}


/**
 * Enables a mode in which each instance of the current model is
 * constructed as CEntity object without an inverse list, that is,
 * without the list containing all entity instances referencing the
 * instance of interest. This way, less computer memory resources
 * are required. In this mode, however, the run times of some methods, like
 * {@link CEntity#findEntityInstanceUsers findEntityInstanceUsers},
 * may become longer.
 * The mode is enabled if the value of the method's parameter is "true".
 * By default, the mode is disabled.
 * Once enabled, the model stays in this special mode during
 * all its lifetime within the session. 
 * The optimization flag represented by the method's parameter is not
 * persistent in the sense that it is not stored neither in an exchange 
 * structure (part21 file) nor in a binary file in the special repositories-directory. 
 * This method is applicable only to models whose access is not started.
 * If this is not the case, then either SdaiException MX_RO or
 * SdaiException MX_RW is thrown.
 * Applying the method to a model from "System Repository" results
 * in SdaiException FN_NAVL.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param enable_mode the flag to enable a special mode of the model.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_RO, SDAI-model access read-only.
 * @throws SdaiException MX_RW, SDAI-model access read-write.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #isOptimized
 */
	public void setOptimized(boolean enable_mode) throws SdaiException {
		if (!enable_mode || optimized) {
			return;
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
			throw new SdaiException(SdaiException.MX_RO, this);
		}
		if ((mode & MODE_MODE_MASK) == READ_WRITE) {
			throw new SdaiException(SdaiException.MX_RW, this);
		}
		optimized = true;
	}


/**
 * Informs if this model is in the mode in which each of its instances
 * is constructed as CEntity object without an inverse
 * list, that is, without the list containing all entity instances
 * referencing the instance of interest.
 * The method is not applicable to models from "System Repository".
 * An attempt to apply it to such a model results in SdaiException FN_NAVL.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @return <code>true</code> if this model is in the special mode, and
 * <code>false</code> otherwise.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #setOptimized
 */
	public boolean isOptimized() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return optimized;
	}


	void scanEntityInstances(CEntity used, CEntityDefinition entityDef, int ent_index,
			int attr_index, AEntity result) throws SdaiException {
		if (entityDef.fieldOwners[attr_index] == null) {
			return;
		}
		SSuper ssuper = entityDef.fieldOwners[attr_index].ssuper;
		Object o;
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			try {
				o = ssuper.getObject(inst, entityDef.attributeFields[attr_index]);
			} catch (java.lang.IllegalAccessException ex) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (o == used) {
				if (result.myType == null || result.myType.express_type == DataType.LIST) {
					result.addAtTheEnd(inst, null);
				} else {
					result.setForNonList(inst, result.myLength, null, null);
				}
			} else if (o instanceof CAggregate) {
				((CAggregate)o).usedin(used, inst, result);
			}
		}
	}


	void scanForUsers(StaticFields staticFields, CEntity used, CEntityDefinition entityDef, int ent_index,
			AEntity result) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			inst.search_for_user(staticFields, used, entityDef, result);
		}
	}


	int scanForUserCount(StaticFields staticFields, CEntity used, CEntityDefinition entityDef, 
			int ent_index, int count) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return count;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			count = inst.search_for_user_count(staticFields, used, entityDef, count);
		}
		return count;
	}


	void scanForRefChange(StaticFields staticFields, CEntity old, CEntity newer, int ent_index,
			CEntityDefinition entityDef, boolean save4undo) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			if (inst == old && substitute_operation) {
				continue;
			}
			inst.search_for_ref_change(staticFields, old, newer, entityDef, save4undo);
		}
	}


	void scanToSetModifiedFlag(StaticFields staticFields, CEntity target, int ent_index,
			CEntityDefinition entityDef) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			inst.if_referencing_mark_as_modified(staticFields, target, entityDef);
		}
	}


	void scanForUnset(StaticFields staticFields, CEntity target, int ent_index,
			CEntityDefinition entityDef, boolean replace_by_connector) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			inst.search_for_unset(staticFields, target, entityDef, replace_by_connector);
		}
	}


	Value scanForCheck(StaticFields staticFields, CEntity target, int ent_index,
			CEntityDefinition entityDef, CEntity_definition new_def) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return null;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			Value val = inst.search_for_check(staticFields, target, entityDef, new_def);
			if (val != null) {
				return val;
			}
		}
		return null;
	}


	void scanForRefMove(StaticFields staticFields, CEntity old, CEntity newer, int ent_index,
			CEntityDefinition entityDef, boolean save4undo) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
				return;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			inst.search_for_ref_move(staticFields, old, newer, entityDef, save4undo);
		}
	}


	CEntity [] scanForRoles(StaticFields staticFields, CEntity role_value, int ent_index,
			CEntityDefinition entityDef, AAttribute result, ASdaiModel modelDomain,
			ListElement l_el, int f_index, CEntity [] ref_instances, int [] res, int ref_inst_count) throws SdaiException {
		if ((mode & MODE_MODE_MASK) == READ_ONLY) {
 			ent_index = find_entityRO(entityDef);
 			if (ent_index < 0) {
 				res[0] = f_index;
				res[1] = ref_inst_count;
				return ref_instances;
			}
		}
		for (int j = 0; j < lengths[ent_index]; j++) {
			CEntity inst = instances_sim[ent_index][j];
			if (!inst.search_if_referenced(staticFields, role_value, entityDef)) {
				continue;
			}
			if ((inst.instance_position & CEntity.POS_MASK) != CEntity.POS_MASK) {
				f_index = inst.findInstanceRoles2(role_value, entityDef, result, modelDomain, l_el, f_index);
				inst.instance_position = (inst.instance_position & CEntity.FLG_MASK) | CEntity.POS_MASK;
				if (ref_instances == null) {
					ref_instances = new CEntity[REFS_ARRAY_SIZE];
				} else if (ref_inst_count >= ref_instances.length) {
					enlarge_refs_array(ref_instances);
				}
				ref_instances[ref_inst_count] = inst;
				ref_inst_count++;
			}
		}
		res[0] = f_index;
		res[1] = ref_inst_count;
		return ref_instances;
	}


	private void enlarge_refs_array(CEntity [] ref_instances) {
		int old_length = ref_instances.length;
		int new_length = old_length * 2;
		CEntity new_refs [] = new CEntity[new_length];
		System.arraycopy(ref_instances, 0, new_refs, 0, old_length);
		ref_instances = new_refs;
	}



/**
 * Informs if remote model data base id is equal to specified one.
 * @param modelId <code>long</code> value of the remote model data base id.
 * @throws SdaiException
 * @return <code>true</code> if model is remote and its id is equal to specified one, <code>false</code> otherwise
 */
    protected boolean hasId(long modelId) throws SdaiException {
		SdaiModelRemote modRemote = getModRemote();
        if (modRemote != null) {
            return (modelId == modRemote.getRemoteId());
        }
        else return false;
    }

    protected final String getNameFast() {
        return name;
    }

    protected boolean checkModelByName(SdaiModelRemote modelRemote) throws SdaiException {
		return false;
	}

/**
 * Returns model location as a <code>URL</code>.
 * <p> The models can be stored in repository directory or in SDAI file.
 * @return the location as URL of this <code>SdaiModel</code>.
 * @throws SdaiException MO_NEXS, SdaiModel does not exist.
*/
    public URL getLocationURL() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
        String sURL = "";
        try {
            File fRepo = new File((String)repository.location);
            sURL = fRepo.toURI().toURL() + "m" + identifier;
		    return new URL(sURL);
        } catch (MalformedURLException ex) {
            throw new SdaiException(SdaiException.LC_NVLD, ex, SdaiSession.line_separator + sURL);
        }
	}

/**
 * Returns entity definition for the entity referenced by specified
 * <code>SerializableRef</code>. This method searches the set of entities that
 * are encountered in the EXPRESS schema whose definition is underlying for this model.
 * If this parameter does not allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param entityRef <code>SerializableRef</code> used to refer to remote entity instance.
 *        <code>entityRef.getTypeIndex()</code> should return positive value otherwise
 *        SdaiException ED_NDEF is thrown.
 * @return definition of the specified entity.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getEntityDefinition
 */
	public EEntity_definition getEntityDefinitionByRef(SerializableRef entityRef) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (entityRef == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (underlying_schema == null) {
			getUnderlyingSchema();
		}
		EEntity_definition definition =
			((SchemaDefinition)underlying_schema).getEntityDefinition(entityRef.getTypeIndex());
		if(definition != null) {
			return definition;
		} else {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
//		} // syncObject
	}

	private static String getNameForModel(SdaiModel model) {
		return model != null ? model.name : null;
	}

	private static SdaiRepository getRepositoryForModel(SdaiModel model) {
		return model != null ? model.repository : null;
	}

	private static String getRepositoryNameForModel(SdaiModel model) {
		return model != null ? model.repository.name : null;
	}

	// Assigns new persistent labels to instances in supplied models.
	// This method is overloaded for remote repositories
	// and makes the real job only there.
	protected void renumberPersistentLabels() throws SdaiException { }

	protected final CEntity[][] getInstances_sim() {
		return instances_sim;
	}

	protected final int[] getLengths() {
		return lengths;
	}

	protected final int typeIdToIndex(int typeId) throws SdaiException {
		return typeIdToIndex(typeId, getMode());
	}

	protected final int typeIdToIndex(int typeId, int mode) throws SdaiException {
		if(mode == READ_ONLY) {
			if (underlying_schema == null) {
                underlying_schema = dictionary.described_schema;
            }
            AEntity_definition set = underlying_schema.getEntities();
			Object [] myDataA = null;
			if (((AEntity)set).myLength > 1) {
				myDataA = (Object [])((AEntity)set).myData;
			}
			CEntityDefinition edef = (CEntityDefinition)(
					myDataA == null ? ((AEntity)set).myData : myDataA[typeId]);
			return find_entityRO(edef);
		} else {
			return typeId;
		}
	}

	protected final CEntityDefinition getSimType(int typeIdx) {
		return (mode & MODE_MODE_MASK) == READ_ONLY ? entities[typeIdx] : dictionary.schemaData.entities[typeIdx];
	}

	protected final CEntityDefinition getSchemaType(int typeIdx) {
		return dictionary.schemaData.entities[typeIdx];
	}

	int find_entityRO(CEntityDefinition def) throws SdaiException {
		String key = def.getNameUpperCase();
		int left = 0;
		int right = entities.length - 1;
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = entities[middle].getNameUpperCase().compareTo(key);
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

	int find_entityRO(String keyy) throws SdaiException {
		int left = 0;
		int right = entities.length - 1;
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = entities[middle].getNameUpperCase().compareTo(keyy);
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

	/**
	 * This method can be called only when model is in partial read only mode
	 * @param typeIds
	 * @throws SdaiException
	 */
	protected void prepareEntityTypesFromIdsRO(int[] typeIds) throws SdaiException {

		CEntityDefinition[] newEntities = new CEntityDefinition[typeIds.length];
		for (int i = 0; i < typeIds.length; i++) {
			newEntities[i] = dictionary.schemaData.entities[typeIds[i]];
		}
		int[] newLengths = new int[typeIds.length + 1];
		CEntity[][] newInstances_sim = new CEntity[typeIds.length + 1][];
		short[] newSim_status = new short[typeIds.length + 1];

		int insFromStartIdx = -1;
		int insToStartIdx = -1;
		int toIdx;
		int fromIdx;
		for (toIdx = 0, fromIdx= 0; toIdx < newEntities.length && fromIdx < entities.length; ) {
			if(newEntities[toIdx] == entities[fromIdx]) {
				if(insToStartIdx < 0) {
					insToStartIdx = toIdx;
					insFromStartIdx = fromIdx;
				}
				toIdx++;
				fromIdx++;
			} else {
				if(insToStartIdx >= 0) {
					copyInstancesSim(newLengths, newInstances_sim, newSim_status, insFromStartIdx, fromIdx, insToStartIdx);
					insToStartIdx = -1;
				}
				if(newEntities[toIdx].getNameUpperCase()
						.compareTo(entities[fromIdx].getNameUpperCase()) > 0) {
					fromIdx++;
				} else {
					fillInstancesSim(newInstances_sim, newSim_status, toIdx);
					toIdx++;
				}
			}
		}
		if(insToStartIdx >= 0) {
			copyInstancesSim(newLengths, newInstances_sim, newSim_status, insFromStartIdx, fromIdx, insToStartIdx);
		}
		for( ; toIdx < newSim_status.length; toIdx++) {
			fillInstancesSim(newInstances_sim, newSim_status, toIdx);
		}
		entities = newEntities;
		lengths = newLengths;
		instances_sim = newInstances_sim;
		sim_status = newSim_status;
		instances_sim_start = new SimIdx[instances_sim.length];
		instances_sim_end = new SimIdxEnd[instances_sim.length];
		invalidate_quick_find();
	}

	private void copyInstancesSim(int[] newLengths, CEntity[][] newInstances_sim,
			short[] newSim_status, int insFromStartIdx, int insFromEndIdx, int insToStartIdx) {
		System.arraycopy(lengths, insFromStartIdx, newLengths, insToStartIdx, insFromEndIdx - insFromStartIdx);
		System.arraycopy(instances_sim, insFromStartIdx, newInstances_sim, insToStartIdx, insFromEndIdx - insFromStartIdx);
		System.arraycopy(sim_status, insFromStartIdx, newSim_status, insToStartIdx, insFromEndIdx - insFromStartIdx);
	}

	private static void fillInstancesSim(CEntity[][] newInstances_sim, short[] newSim_status, int idx) {
		newInstances_sim[idx] =  emptyArray;
		newSim_status[idx] = (short)(SIM_SORTED | SIM_LOADED_NONE);
	}


	void checkContents(String text) throws SdaiException {
		if (instances_sim == null) {
			return;
		}
		for (int i = 0; i < instances_sim.length; i++) {
			if (instances_sim[i] == null || lengths[i] == 0) {
				continue;
			}
			CEntity [] row_of_instances = instances_sim[i];
			for (int j = 0; j < lengths[i]; j++) {
				if (row_of_instances[j].owning_model == null) {
					String base = SdaiSession.line_separator + "Invalid entity instance was found!" +
					SdaiSession.line_separator + "   Model: " + name +
					SdaiSession.line_separator + "   Instance id: #" + row_of_instances[j].instance_identifier +
					SdaiSession.line_separator + text;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
	}


//	public int getFoldersCount() {
//		return dictionary.schemaData.noOfEntityDataTypes;
//	}

//	public int getPopFoldersCount() throws SdaiException {
//		int tot_inst = 0;
//		for (int i = 0; i < entities.length; i++) {
//			CEntity_definition def = (CEntity_definition)entities[i];
//			String ent_name = def.getName(null);
//			int count = lengths[i];
//			if (count <= 0) {
//				throw new SdaiException(SdaiException.SY_ERR);
//			}
//		}
//		int instances_sim_count = instances_sim.length;
//		return entities.length;
//	}

	protected final SdaiRepository getRepositoryFast() {
		return repository;
	}

	protected final SdaiRepositoryRemote getRepositoryRepoRemote() {
		return repository.getRepoRemote();
	}

	static protected final SdaiRepositoryRemote getRepoRemoteForRepository(SdaiRepository repository) {
		return repository.getRepoRemote();
	}

    static protected long getModelVersion(SdaiModel model) {
		if (model instanceof SdaiModelDictionaryImpl) {
            return ((SdaiModelDictionaryImpl)model).getVersion();
        }
        return -1;
	}

    static protected boolean hasSchemaInstanceId(SchemaInstance schInst,
												 long schemaId) throws SdaiException {
		return schInst.hasId(schemaId);
	}

	protected final EEntity findEntityInstanceByType(long identifier,
													 int typeId) throws SdaiException {
		int typeIndex = typeIdToIndex(typeId);
		int ind = find_instance(0, lengths[typeIndex] - 1, typeIndex, identifier);
		return ind >= 0 ? instances_sim[typeIndex][ind] : null;
	}

	protected final void forwardRepositoryRemoveLoadedEntityExternalData(CEntity instance,
			boolean deletedExternalData) throws SdaiException {
		repository.removeLoadedEntityExternalData(instance, deletedExternalData);
	}

	// SdaiModelConnector implementation

	/**
	 * @since 4.0.0
	 */
	public void connectInConnector(Connector connector) {
		if (inConnectors == null) {
			/* create ring with this one element */
			connector.nextIn = connector;
			connector.prevIn = connector;
			inConnectors = connector;
		} else {
			/* link this into the exisiting ring */
			Connector h = inConnectors.prevIn;
			inConnectors.prevIn = connector;
			connector.prevIn = h;
			h.nextIn = connector;
			connector.nextIn = inConnectors;
		}
	}

	/**
	 * @since 4.0.0
	 */
	public void disconnectInConnector(Connector connector) {
        /* remove from incomming ring */
		if (connector.nextIn == connector) {
			/* last remaining element */
			inConnectors = null;
		} else {
			if(connector == inConnectors) {
				inConnectors = connector.nextIn;
			}
			connector.prevIn.nextIn = connector.nextIn;
			connector.nextIn.prevIn = connector.prevIn;
			connector.nextIn = null;
			connector.prevIn = null;
		}
	}

	/**
	 * @since 4.0.0
	 */
	public SdaiModel resolveConnectedModel() {
		return this;
	}

    abstract protected Connector newConnector(SdaiModel model, String entityName,
											  long instanceIdentifier,
											  CEntity owningInstance) throws SdaiException;

    abstract protected Connector newConnector(SdaiRepository repository, long modelId,
											  long instanceIdentifier,
											  CEntity owningInstance) throws SdaiException;

    abstract protected Connector newConnector(long repositoryId, long modelId,
											  long instanceIdentifier,
											  CEntity owningInstance) throws SdaiException;

	private class QuickFindInstanceLeftSorter implements Comparator {
		public int compare(Object o1, Object o2) {
			SimIdx sim1 = (SimIdx)o1;
			SimIdx sim2 = (SimIdx)o2;
			return (int)(instances_sim[sim1.index][0].instance_identifier -
						 instances_sim[sim2.index][0].instance_identifier);
		}
	}

	private class QuickFindInstanceRightSorter implements Comparator {
		public int compare(Object o1, Object o2) {
			SimIdxEnd sim1 = (SimIdxEnd)o1;
			SimIdxEnd sim2 = (SimIdxEnd)o2;
			int idx1 = instances_sim_start[sim1.index].index;
			int idx2 = instances_sim_start[sim2.index].index;
			return (int)(instances_sim[idx1][lengths[idx1] - 1].instance_identifier -
						 instances_sim[idx2][lengths[idx2] - 1].instance_identifier);
		}
	}

	private static class SimIdx {
		private int index;
		private SimIdx() { }
	}

	private static class SimIdxEnd {
		private int index;
		private int minIndex;
		private SimIdxEnd() { }
	}

    protected static abstract class Connector extends InverseEntity  {
		private static final SdaiModelConnector MISSING_CONNECTOR = new SdaiModelConnector() {
				public void connectInConnector(Connector connector) { }

				public void disconnectInConnector(Connector connector) { }

				public SdaiModel resolveConnectedModel() {
					return null;
				}

				public String toString() {
					return "[Missing connector]";
				}

				public SerializableRef getQuerySourceInstanceRef() {
					return new SdaiRepositoryRef(-1);
				}
			};

        protected CEntity owning_instance; // will be replaced by functionality of InverseEntity
        protected SdaiModelConnector modelInConnector;
        protected long instance_identifier;
        protected Connector nextOut;
        protected Connector prevOut;
        protected Connector nextIn;
        protected Connector prevIn;

        SdaiCommon getOwner() {
            // dummy, skip
            return null;
        }

        void modified() throws SdaiException {
            // dummy, skip
        }

        protected Connector(SdaiModelConnector modelInConnector, long instance_identifier,
							CEntity owning_instance) throws SdaiException {
            this.instance_identifier = instance_identifier;
            this.owning_instance = owning_instance;

        /* add into incommingConnector ring */
            SdaiModel modelOut = owning_instance.owning_model;
            if (modelOut.outConnectors == null) {
        /* create ring with this one element */
                nextOut = this;
                prevOut = this;
				modelOut.outConnectors = this;
            } else {
        /* link this into the exisiting ring */
                Connector h = modelOut.outConnectors.prevOut;
                modelOut.outConnectors.prevOut = this;
                prevOut = h;
                h.nextOut = this;
                nextOut = modelOut.outConnectors;
            }

            this.modelInConnector = modelInConnector;
			modelInConnector.connectInConnector(this);
		}

        protected final void disconnect() throws SdaiException {
            if (owning_instance == null) {
                return;
            }
        /* remove from outgoing ring */
            SdaiModel modelOut = owning_instance.owning_model;
            if (nextOut == this) {
        /* last remaining element */
                modelOut.outConnectors = null;
            } else {
				if(this == modelOut.outConnectors) {
					modelOut.outConnectors = nextOut;
				}
                prevOut.nextOut = nextOut;
                nextOut.prevOut = prevOut;
                nextOut = null;
                prevOut = null;
            }

			modelInConnector.disconnectInConnector(this);

            owning_instance = null;
            modelInConnector = null;
        /* now this connector is ready for garbage collection */
        }

		protected SdaiModel resolveModelIn() throws SdaiException {
			SdaiModel modelIn = modelInConnector.resolveConnectedModel();
			if(modelIn != null && modelInConnector != modelIn) {
				modelInConnector.disconnectInConnector(this);
				modelIn.connectInConnector(this);
				modelInConnector = modelIn;
			} else if(modelIn == null) {
				modelInConnector = MISSING_CONNECTOR;
			}
			return modelIn;
		}

		private SerializableRef getModelInRef() throws SdaiException {
			return modelInConnector.getQuerySourceInstanceRef();
		}

        void print_connector() throws SdaiException {
            System.out.println();
            System.out.println("   Owning instance: ");
            System.out.println("       instance identifier = #" + owning_instance.instance_identifier);
            EEntity_definition def = owning_instance.getInstanceType();
            System.out.println("       instance type name  = " + ((CEntityDefinition)def).getCorrectName());
            if (owning_instance.owning_model != null) {
                System.out.println("       instance model name = " + owning_instance.owning_model.name);
            }
            System.out.println("   Referenced model: " + modelInConnector);
            System.out.println("   Referenced instance identifier: #" + instance_identifier);
        }

        protected final void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
            // nothing to do!
            // Connector stands for a usage of an entity instance and this
            // has no populated "entity attribute" in this state.
        }

        /**
            Returns entity instance obtained as a result of resolving a reference
            represented by a connector. If the first parameter is <code>true</code>,
            then the connector is eliminated from the out- and in-rings of connectors.
            The repository the connector points to should be open;
            otherwise, SdaiException RP_NOPN is thrown. The referenced model, however, if
            without access, is started automatically in read-only mode.
            This method is invoked in <code>replaceByReference</code> method in
            <code>SdaiModel</code> class and <code>resolveConnector</code> method in
            <code>CAggregate</code> class.
        */
        protected abstract CEntity resolveConnector(boolean remove_con, boolean providePartial,
													boolean aborting) throws SdaiException;

        protected abstract Connector copyConnector(CEntity owning_instance) throws SdaiException;

		protected abstract String getEntityNameUpperCase();

        protected static CEntity quickFindInstance(SdaiModel model, long instance_identifier) throws SdaiException {
            return model.quick_find_instance(instance_identifier);
        }

        protected static void inverseAdd(CEntity instance, InverseEntity owning_instance) throws SdaiException {
            instance.inverseAdd(owning_instance);
        }

        protected static String additionalMessageWithSeparator(String message) {
            String text = SdaiSession.line_separator;
            if (message.equalsIgnoreCase("BF_IINC")) {
                 text += AdditionalMessages.BF_IINC;
            }
            return text;
        }

    }

}
