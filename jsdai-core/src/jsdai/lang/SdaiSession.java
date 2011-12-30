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
import java.net.*;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.lang.MappingContext.ImmutableArrayMap;
import jsdai.mapping.*;
import jsdai.query.LocalSdaiQuery;
import jsdai.query.QueryLibProvider;
import jsdai.query.SerializableRef;
import jsdai.util.JarFileURLStreamHandler;
import jsdai.util.Move;
import org.w3c.dom.Element;

/** It is the main class to begin and end any usage of JSDAI.
 * Only one <code>SdaiSession</code> object can exist at a time.
 * An <code>SdaiSession</code> is started with
 * {@link #openSession openSession} and ended with {@link #closeSession closeSession}
 * methods.
 * The active session can be statically accessed with {@link #getSession getSession}
 * method.
 * <p>
 * Conceptionally, the <code>SdaiSession</code> can be represented by
 * the following EXPRESS entity:

 * <P><pre>ENTITY SdaiSession;
 *  {@link #getSdaiImplementation sdaiImplementation} : <A HREF="../../jsdai/lang
/Implementation.html">Implementation</A>;
 *  {@link #getKnownServers knownServers}       : <A HREF="../../jsdai/lang
/ASdaiRepository.html">SET</A> [1:?] OF <A HREF="../../jsdai/lang
/SdaiRepository.html">SdaiRepository</A>;
 *  {@link #getActiveServers activeServers}      : <A HREF="../../jsdai/lang
/ASdaiRepository.html">SET</A> [1:?] OF <A HREF="../../jsdai/lang
/SdaiRepository.html">SdaiRepository</A>;
 *  {@link #getActiveModels activeModels}       : <A HREF="../../jsdai/lang
/ASdaiModel.html">SET</A> [1:?] OF <A HREF="../../jsdai/lang
/SdaiModel.html">SdaiModel</A>;
 *  {@link #getDataDictionary dataDictionary}     : OPTIONAL <A HREF="../../jsdai/lang
/SchemaInstance.html">SchemaInstance</A>;
 *  {@link #getDataMapping dataMapping}        : OPTIONAL <A HREF="../../jsdai/lang
/SchemaInstance.html">SchemaInstance</A>;
 *INVERSE
 *  {@link #getActiveTransaction activeTransaction}  : <A HREF="../../jsdai/lang
/SdaiTransaction.html">SdaiTransaction</A> FOR owningSession;
 *END_ENTITY;</pre>
 * <P>

 * All attributes are read-only, and for each of them a corresponding get
 * method is defined.

 * <p>
 * As it is documented in the description of class <code>SdaiRepository</code>,
 * there exists a special repositories-directory which location is specified
 * by the property "repositories" in the configuration file jsdai.properties.
 * During execution of <code>openSession</code> method all the repositories
 * found in this directory are made available for the user by including them
 * into the set <code>knownServers</code> defined in the session.
 * Some members of <code>knownServers</code> can be opened and, thus, can also be found in
 * the set <code>activeServers</code>.
 * <p>
 * After <code>openSession</code> only the special repository with the
 * name "SystemRepository" is opened. The "SystemRepository" is always
 * the first member in <code>activeServers</code> and <code>knownServers</code>.
 * The contents of this repository (<code>SdaiModel</code>s and
 * <code>SchemaInstance</code>s) are read-only; an application is not
 * allowed to modify them. The <code>SdaiModel</code>s in "SystemRepository"
 * contain either meta-descriptions of the supported
 * EXPRESS schemas or mapping information between such schemas.
 * The "SystemRepository" contains 2 <code>SchemaInstance</code>s:
 * with one all dictionary <code>SdaiModel</code>s (data_dictionary)
 * and with another all dictionary and mapping <code>SdaiModel</code>s (data_mapping)
 * are associated.

 * @see "ISO 10303-22::7.4.1 sdai_session"
 */

public final class SdaiSession extends SdaiCommon implements QuerySource {

/**
	EXPRESS attributes of SdaiSession, see ISO 10303-22::7.4.1 sdai_session.
*/
	Implementation sdai_implementation;
	ASdaiRepository known_servers;
	ASdaiRepository active_servers;
	ASdaiModel active_models;
	SchemaInstance data_dictionary;
	SdaiTransaction active_transaction;
//	static int test = 0;
/**
	A schema instance logically connecting all data mapping SdaiModels.
*/
	SchemaInstance data_mapping;


/** This field is for internal JSDAI use only. Applications shall not use it. */
	public static final String asterisk = "*";

/**
	An instance of the special class in directory 'dictionary'.
	It is used to create base data dictionary entity instances.
*/
	private static SSuper sDictionary; // = new SDictionary();

/**
	If nonnull, then currently active SDAI session.
*/
	private static SdaiSession session = null;

	boolean opened;

	private boolean isMainSession() {
		return this == session;
	}

	static boolean isSessionAvailable() {
		//FIXME: should it be synchronized?
// 		synchronized(syncObject) {
			return session != null;
// 		}
	}

/**
	A globally available aggregate for all JSDAI applications
	running on the same JVM.
*/
	AEntity clipboard;

/**
	This array is used within method extractSchemas
	when reading (from jsdai.properties) the list of the names of EXPRESS schemas
	that are represented by a library package.
*/
	private static int [] commas;

/**
	The build number when formats of binary files were changed.
	The binary files created using build 54 or less are not supported.
	The corresponding message accompanying SdaiException.SY_ERR is displayed.
*/
	static final int valid_from = 55;

/**
	The build number starting from which strings in binary file are stored
	in a different way, namely, using writeUTF() method.
	In earlier builds each string was stored as an array of bytes.
*/
	static final int change_1 = 196;

/**
	The location of the special repositories directory. This string is taken
	from jsdai.properties file.
*/
	static String repos_path = null;

	static private FileLock repos_path_lock = null;

/**
	An instance of PrintWriter that can be used during SdaiSession
	for logging/tracing purposes.
*/
	static private PrintWriter logWriter;
	static private boolean closeLogWriter;
	static private Object logWriterSync = new Object();

/**
	An instance of PrintWriter that can be used during SdaiSession
	for logging/tracing purposes.
*/

	PrintWriter logWriterSession;

/**
	Properties loaded from jsdai.properties file.
*/
	static Properties props = null;

/**
	An instance of class Print_instance several methods of which are used
	in classes CAggregate and CEntity.
*/
	static Print_instance printer;
//	Print_instance printer;

/**
	It is System property "line.separator".
*/
	static String line_separator;

/**
	An instance of class SdaiCalendar methods of which are used in this and
	several other classes.
*/
	SdaiCalendar cal;

/**
	Default value for the attribute 'description' of the entity 'file_description'.
	This value is used when exporting the repository to the exchange structure.
*/
	static final String [] description = {" "};

/**
	Default value for the attribute 'author' of the entity 'file_name'.
	This value is used when exporting the repository to the exchange structure.
*/
	static final String [] author = {" "};

/**
	Default value for the attribute 'organization' of the entity 'file_name'.
	This value is used when exporting the repository to the exchange structure.
*/
	static final String [] organization = {" "};

/**
	An auxiliary array into which properties to be deleted from 'repoProps'
	are stored. These properties correspond to repositories which were not
	found in 'known_servers'.
*/
	private static String [] del_props;

/**
	Properties loaded from sdairepos.properties file.
*/
	private static Properties repoProps = null;

/**
	The number used to construct directory names for repositories in the form "r9",
	for example. This number is also stored in sdairepos.properties file (under
	the name "r_max").
*/
	private static int repo_count;
	private static Object repoPropsSync = new Object();

/**
	The number used to construct the names of temporary repositories in the
	form "&temp2"; here count_for_repositories=2.
*/
	private static int count_for_repositories = 0;
	private static Object countForRepsSync = new Object();

/**
	The number used to construct a name for a repository for which during
	import of a part-21 file JSDAI failed to recover such a name from the
	header of this file. The example of the constructed name:
	"REPO5"; here "REPO" is string constant DEFAULT_REPOSITORY_NAME in the
	class FILE_NAME and count_for_abnormal_repo_names=5.
*/
	private int count_for_abnormal_repo_names = 0;

/**
	If 'true', then special conditions for deleteSdaiModelWork method are ensured.
	More specifically, first, models are not removed from SdaiRepository.models
	aggregate, and second, references to deleted models are made unset (not replaced by
	the connectors).
	This field is set to 'true' in closeSession method, and used in closeRepository
	method.
*/
	boolean session_closing;

/**
	If 'true', then sdairepos.properties file shall be updated.
*/
	private boolean repo_properties_changed;

/**
	Repository directory name in the new style, that is, "r" + repo_count.
	The field is used when moving from the old directory names to the new names.
*/
	private String updated_dir_name;

/**
	Temporary fields used for debugging and run time evaluation.
*/
static final boolean debug = false;
static final boolean debug2 = false;
static final boolean debug3 = false;
static long tm0=0, tm1=0, tm2=0, tm3=0, tm4=0;
static long tmbind0=0, tmbind1=0, tmbind2=0, tmbind3=0, tmbind4=0, tmbind5=0, tmbind6=0;

/**
 * If <code>true</code>, then strings as values of entity attributes
 * are returned by <code>toString</code> methods in unicode;
 * if <code>false</code>, then in ASCII.
 */
	private static boolean toStringUnicode = false;

/** integer value for LOGICAL value TRUE.*/
	public static final int TRUE = 2;

/** integer value for LOGICAL value FALSE.*/
	public static final int FALSE = 1;

/** integer value for LOGICAL value UNKNOWN.*/
	public static final int UNKNOWN = 3;

/** Suffix of dictionary SdaiModel "_DICTIONARY_DATA"*/
	public static final String DICTIONARY_NAME_SUFIX = "_DICTIONARY_DATA";

/** Suffix of dictionary SdaiModel with mapping data "_MAPPING_DATA"*/
	public static final String MAPPING_NAME_SUFIX = "_MAPPING_DATA";

/**
 * The name of the default <code>SchemaInstance</code> "SDAI_DICTIONARY_SCHEMA_INSTANCE"
 * holding all dictionary <code>SdaiModel</code>s.
 */
	public static final String DICTIONARY_SCHEMA_INSTANCE_NAME = "SDAI_DICTIONARY_SCHEMA_INSTANCE";

/**
 * The name of the default <code>SchemaInstance</code> "SDAI_MAPPING_SCHEMA_INSTANCE"
 * holding all mapping and related dictionary <code>SdaiModel</code>s
 */
	public static final String MAPPING_SCHEMA_INSTANCE_NAME = "SDAI_MAPPING_SCHEMA_INSTANCE";


/**
	Prefix for the location of the java classes for an EXPRESS schema.
*/
	static final String SCHEMA_PREFIX = "jsdai.S";

/**
	Prefix for the location of the data for an AP-mapping.
*/
	static final String MAPPING_PREFIX = "jsdai.M";

/**
	Prefix used (in startReadOnlyAccess method) to construct a path
	when reading binary file corresponding to an EXPRESS schema.
*/
	static final String SCHEMA_PREFIX_SPEC = "jsdai/S";

/**
	Prefix used (in startReadOnlyAccess method) to construct a path
	when reading binary file corresponding to an AP-mapping.
*/
	static final String MAPPING_PREFIX_SPEC = "jsdai/M";

/**
	Prefix used to recognize or constuct a location for remote repository.
*/
	static final String LOCATION_PREFIX = "//";

/**
	Extension used to construct a location for SDAI file.
*/
	static final String SDAI_FILE_EXTENSION = ".sdai";

/**
	Suffix used to separate directory from SDAI file.
*/
	static final String DIRECTORY_SUFFIX = "/";


/**
	Mapping package name.
*/
	static final String MAPPING_PACKAGE = "jsdai.mapping";

/**
	Prefix used (in startReadOnlyAccess method) to construct a path
	when reading binary file for mapping.
*/
	static final String MAPPING_PACKAGE_SPEC = "jsdai/mapping/";

/**
	The name of a special class in mapping package.
*/
	static final String MAPPING_FILE = ".SMapping";

/**
	A string used in the construction of temporary repository name.
	Such repositories (if any) are deleted while executing openSession method.
*/
	static final String DEF_REP_NAME = "&temp";

/**
	The name of the subdirectory in the repositories directory.
	This subdirectory is introduced for applications
	(contains, for example, applications properties files).
*/
	private static final String APPLICATIONS_DIR_NAME = "application";

	private static final String PROPERTY_FOR_SYSTEM_REPOSITORY = "SystemRepository";

/**
	The default user name.
*/
	static final String DEF_USER_NAME = "guest";

/**
	The default passward.
*/
	static final String DEF_PASSWORD = "passwd";

/**
	Suffix to recognize being rejected schema instances loaded from jsdai/repository
	binary file.
*/
	static final String SCHEMA_INSTANCE_SUFIX = "_INSTANCE";

/**
	The flag used by server.
*/
	static final boolean onServer = false;

/**
	The type indicator for the SET used as a private aggregate in lang.
*/
	static final int PRIVATE_AGGR = -5;

	static final int EXPRESSIONS_INST_AGGR     = 20;
	static final int EXPRESSIONS_INTEGER_AGGR  = 21;
	static final int EXPRESSIONS_DOUBLE_AGGR   = 22;
	static final int EXPRESSIONS_STRING_AGGR   = 23;
	static final int EXPRESSIONS_LOGICAL_AGGR  = 24;
	static final int EXPRESSIONS_BOOLEAN_AGGR  = 25;
	static final int EXPRESSIONS_ENUM_AGGR     = 26;
	static final int EXPRESSIONS_BINARY_AGGR   = 27;
	static final int EXPRESSIONS_MIXED_AGGR    = 28;
	static final int EXPRESSIONS_INTEGER2_AGGR = 29;
	static final int EXPRESSIONS_DOUBLE2_AGGR  = 30;
	static final int EXPRESSIONS_STRING2_AGGR  = 31;
	static final int EXPRESSIONS_LOGICAL2_AGGR = 32;
	static final int EXPRESSIONS_BOOLEAN2_AGGR = 33;
	static final int EXPRESSIONS_ENUM2_AGGR    = 34;
	static final int EXPRESSIONS_DOUBLE3_AGGR  = 35;

/**
	The type indicator for the SET of instances of an entity data type
	and all its subtypes.
*/
	static final int INST_AGGR = -7;

/**
	The type indicator for the SET of instances of an entity data type
	but not its subtypes.
*/
	static final int INST_EXACT_AGGR = -8;

/**
	The type indicator for the SET of all instances of those entities
	which contain a specified collection of simple entity data types.
*/
	static final int INST_COMPL_AGGR = -11;

/**
	The type indicator for the SET of all instances of all entity data types
	in the EXPRESS schema considered.
*/
	static final int ALL_INST_AGGR = -9;

	static final int ASDAIMODEL_INST_ALL = -14;

/**
	The type indicator for the SET of all instances of the specified entity
	data type and all instances of all its subtypes collected going through
	all started SdaiModels in a given ASdaiModel.
*/
	static final int ASDAIMODEL_INST = -15;

/**
	The type indicator for the SET of all instances of the specified entity
	data type (but not instances of its subtypes) collected going through
	all started SdaiModels in a given ASdaiModel.
*/
	static final int ASDAIMODEL_INST_EXACT = -16;

			/* ______________________  lengths and sizes  _______________________   */
/**
	Value used to set an initial size for some arrays.
	Its meaning: count of simple entity data types in a complex entity.
	Appears in classes ComplexEntityValue (for array 'entityValues') and
	PhFileReader (for array 'complex_name').
*/
	static final int NUMBER_OF_ITEMS_IN_COMPLEX_ENTITY = 16;

/**
	Value used to set an initial size for some arrays.
	Its meaning: count of attributes in a simple entity data type.
	Appears in classes EntityValue (for array 'values'),
	Value (for array 'nested_values'),
	and PhFileReader (for array 'array_of_inverse').
*/
	static final int NUMBER_OF_VALUES = 16;

/**
	Value used to set an initial size for the following arrays:
	'def_type_chain' in SdaiModel, 'route' in SelectType, and
	'route' in Value.
	All these arrays are of type 'CDefined_type'.
*/
	static final int NUMBER_OF_DEFINED_TYPES = 16;

/**
	Value used to set an initial size for the array 'numbers'.
*/
	private static final int COUNT_OF_INTEGER_NUMBERS = 32;

/**
	Value used to set an initial size for the internal array 'repo_name'.
*/
	private static final int NUMBER_OF_CHARACTERS = 64;

/**
	Value used to set an initial size for the internal array 'del_props'.
*/
	private static final int NUMBER_OF_DEL_PROPERTIES = 32;

/**
	Value used to set an initial size for the internal array 'commas'.
*/
	private static final int NUMBER_OF_COMMAS = 8;

/**
	Count of characters in SCHEMA_PREFIX.
*/
	static final int SCHEMA_PREFIX_LENGTH = SCHEMA_PREFIX.length();

/**
	Count of characters in LOCATION_PREFIX.
*/
	static final int LOCATION_PREFIX_LENGTH = LOCATION_PREFIX.length();

/**
	Count of characters in DICTIONARY_NAME_SUFIX.
	Used in PhFileReader, SchemaInstance, SdaiModel,and SdaiRepository.
*/
	static final int ENDING_FOR_DICT = DICTIONARY_NAME_SUFIX.length();

/**
	Count of characters in MAPPING_NAME_SUFIX.
	Used in SdaiModel and SdaiRepository.
*/
	static final int ENDING_FOR_MAPPING = MAPPING_NAME_SUFIX.length();

/**
	Count of characters in SCHEMA_INSTANCE_SUFIX.
*/
	static final int ENDING_FOR_INST = SCHEMA_INSTANCE_SUFIX.length();

/**
	Count of characters in DEF_REP_NAME.
*/
	static final int DEF_REP_NAME_LENGTH = DEF_REP_NAME.length();

/**
	The system repository containing all dictionary and mapping data.
*/
	static SdaiRepository systemRepository;

/**
	A model whose contents consists of instances of entities in
	SDAI dictionary schema (see clause 6 of Part 21).
	This model belongs to systemRepository.
*/
//	static SdaiModel baseDictionaryModel;
	static SdaiModelDictionaryImpl baseDictionaryModel;

/**
	A model containing instances of entities in Mapping schema.
	This model belongs to systemRepository.
*/
	static SdaiModel baseMappingModel;

/**
	A model containing instances of complex entities whose simple
	entity data types belong to two or more Express schemas realized in
	JSDAI library.
*/
	static SdaiModel baseComplexModel;

/**
	An auxiliary model used to create entity instances during bootstrapping
	(within constructor of this class).
	Later this model becomes redundant.
*/
	private static SdaiModel sessionModel;

/**
	The (only one) instance of the entity 'schema_definition' in
	SDAI dictionary schema (see clause 6 of Part 21).
	Used in SdaiModel class.
*/
	static CSchema_definition baseDictionarySchemaDef;

/**
	The (only one) instance of the entity 'schema_definition' in
	Mapping schema.
	Used in SdaiRepository and SdaiModel.
*/
	static CSchema_definition mappingSchemaDefinition;

/**
	An object of the class used for reading an exchange structure.
*/
	private PhFileReader reader;

/**
	Instance of the entity 'integer_bound' (defined in SDAI dictionary schema) with bound value 0.
	Used as a value of attributes in aggregate instances.
*/
	private CInteger_bound bound0;

/**
	Instance of the entity 'integer_bound' (defined in SDAI dictionary schema) with bound value 1.
	Used as a value of attributes in aggregate instances.
*/
	private CInteger_bound bound1;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema).
	Lower bound in this instance is 0, upper bound is not set.
	Used as an aggregation type for several session and auxiliary sets,
	for example, 'models' and 'schemas' in SdaiRepository.
*/
	static CSet_type setType0toN;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema).
	Lower bound in this instance is 1, upper bound is not set.
	Used as an aggregation type for sets 'known_servers',
	'active_servers', and 'active_models'.
*/
	static private CSet_type setType1toN;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set returned by 'getInstances'
	method in SdaiModel.
*/
	static CSet_type setTypeForInstances;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set returned by 'getExactInstances'
	method in SdaiModel.
*/
	static CSet_type setTypeForInstancesExact;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set returned by 'getInstances(Class types[])'
	method in SdaiModel.
*/
	static CSet_type setTypeForInstancesCompl;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set 'instances' in SdaiModel.
*/
	static CSet_type setTypeForInstancesAll;

	static CSet_type setTypeForInstancesListOfModelAll;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set returned by 'getInstances'
	method in ASdaiModel.
*/
	static CSet_type setTypeForInstancesListOfModel;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the set returned by 'getExactInstances'
	method in ASdaiModel.
*/
	static CSet_type setTypeForInstancesListOfModelExact;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for several sets which are considered
	as private in 'jsdai.lang'.
*/
	static CSet_type setTypeSpecial;

/**
	Instance of the entity 'set_type' (defined in SDAI dictionary schema)
	used as an aggregation type for the nonempty set 'governed_sections' in
	the entity 'file_population'.
	In future, may be used also for other sets.
*/
	static CSet_type setTypeSpecialNonEmpty;

/**
	Instance of the entity 'list_type' (defined in SDAI dictionary schema).
	Lower bound in this instance is 0, upper bound is not set.
	Used as an aggregation type for the aggregate of explicit attributes
	in CEntityDefinition.
*/
	static CList_type listType0toN;

/**
	Instance of the entity 'list_type' (defined in SDAI dictionary schema)
	used as an aggregation type for several lists which are considered
	as private in 'jsdai.lang'.
*/
	static CList_type listTypeSpecial;

	static CList_type listTypeSpecialU;

/**
	An aggregate to which definitions of entities from SDAI dictionary schema
	are stored. The aggregate is constructed during bootstrapping
	(within constructor of this class).
	Besides this class used also in SdaiModel.
*/
	static AEntity_definition bootEntityAggregate;

/**
	An integer used as an index of entity added to bootEntityAggregate.
	Indexing starts from 0 and must reach NUMBER_OF_ENTITIES - 1.
*/
	private static int counter;

//	private static boolean check_data_types = true;
	private static boolean check_data_types = false;

	SdaiContext sdai_context;

	String [] bypassed_rules [];

	int byp_rules_count;

	private final int BLACK_LIST_SIZE = 32;

// Undo-Redo
	File UR_file;

	RandomAccessFile undo_redo_file;

	RandomAccessFile undo_redo_file_saved;

	CEntity undoRedoInstance = null;

	ComplexEntityValue undoRedoOldValue;

	int undoRedoOperation;

	boolean forbid_undo_on_SdaiEvent = false;

	SdaiModel [] mods_undo;

	int n_mods_undo;

	static final int NUMBER_OF_MODS_UNDO = 16;

	String [] ent_names_undo;

	int n_ent_names_undo;

	static final int NUMBER_OF_ENT_NAMES_UNDO = 32;

	long pointer_pos;

	static int SIZEOF_LONG = 8;

	long [] substituted;

	SchemaData saved_sch_data;

	SdaiModel saved_model;

	int saved_inst_index;

	boolean empty_group;

	boolean group_start_event;

	boolean undo_performed;

	boolean modif_state;

	boolean del_state;

	boolean modif_state_old;

	boolean sdai_context_missing;

	boolean a_double3_overflow;

	/**
	 * An aggregate <code>SdaiListeners</code> for logging events.
	 */
	private CAggregate loggingListenrList;

	static final int CREATE_OPERATION = 1;

	static final int DELETE_OPERATION = 2;

	static final int MODIFY_OPERATION = 3;

	static final int SUBSTITUTE_OPERATION = 4;

	static final int COPY_OPERATION = 5;

/**
	Count of entities in SDAI dictionary schema.
*/
	static final int NUMBER_OF_ENTITIES = 143;

/**
	Below the data dictionary constants are listed.
	They are intended to be *final*, but as long as the dictionary is changing
	keep them as variable and init them during bootstrapping.
*/
	static int AGGREGATION_TYPE = -1; // entity 'aggregation_type'
	static int ARRAY_TYPE = -1; // entity 'array_type'
	static int BAG_TYPE = -1; // entity 'bag_type'
	static int BINARY_TYPE = -1; // entity 'binary_type'
	static int BOOLEAN_TYPE = -1; // entity 'boolean_type'
	static int DATA_TYPE = -1; // entity 'data_type'
	static int DATA_TYPE_DECL_IMPLICIT_DECL = -1; // entity 'data_type_declaration$implicit_declaration'
	static int DATA_TYPE_DECL_LOCAL_DECL = -1; // entity 'data_type_declaration$local_declaration'
	static int DEFINED_TYPE = -1; // entity 'defined_type'
	static int DERIVED_ATTRIBUTE = -1; // entity 'derived_attribute'
	static int ENTITY_DECL_IMPLICIT_DECL = -1; // entity 'entity_declaration$implicit_declaration'
	static int ENTITY_DECL_LOCAL_DECL = -1; // entity 'entity_declaration$local_declaration'
	static int ENTITY_DECL_REFERENCED_DECL = -1; // entity 'entity_declaration$referenced_declaration'
	static int ENTITY_DECL_USED_DECL = -1; // entity 'entity_declaration$used_declaration'
	static int ENTITY_DEFINITION = -1; // entity 'entity_definition'
	static int ENTITY_SELECT_TYPE = -1; // entity 'entity_select_type'
	static int ENT_EXT_EXT_SELECT_TYPE = -1; // entity 'entity_select_type$extended_select_type$extensible_select_type'
	static int ENT_EXT_NON_EXT_SELECT_TYPE = -1; // entity 'entity_select_type$extended_select_type$non_extensible_select_type'
	static int ENT_EXT_SELECT_TYPE = -1; // entity 'entity_select_type$extensible_select_type'
	static int ENT_NON_EXT_SELECT_TYPE = -1; // entity 'entity_select_type$non_extensible_select_type'
	static int ENUMERATION_TYPE = -1; // entity 'enumeration_type'
	static int EXPLICIT_ATTRIBUTE = -1; // entity 'explicit_attribute'
	static int EXTENDED_ENUM_TYPE = -1; // entity 'extended_enumeration_type'
	static int EXTENDED_EXTENSIBLE_ENUM_TYPE = -1; // entity 'extended_enumeration_type$extensible_enumeration_type'
	static int EXTENDED_SELECT_TYPE = -1; // entity 'extended_select_type'
	static int EXT_EXT_SELECT_TYPE = -1; // entity 'extended_select_type$extensible_select_type'
	static int EXT_NON_EXT_SELECT_TYPE = -1; // entity 'extended_select_type$non_extensible_select_type'
	static int EXTENSIBLE_ENUM_TYPE = -1; // entity 'extensible_enumeration_type'
	static int EXTENSIBLE_SELECT_TYPE = -1; // entity 'extensible_select_type'
	static int IMPLICIT_DECL_RULE_DECL = -1; // entity 'implicit_declaration$rule_declaration'
	static int IMPLICIT_DECL_TYPE_DECL = -1; // entity 'implicit_declaration$type_declaration'
	static int INTEGER_TYPE = -1; // entity 'integer_type'
	static int INVERSE_ATTRIBUTE = -1; // entity 'inverse_attribute'
	static int LIST_TYPE = -1; // entity 'list_type'
	static int LOCAL_DECL_RULE_DECL = -1; // entity 'local_declaration$rule_declaration'
	static int LOCAL_DECL_TYPE_DECL = -1; // entity 'local_declaration$type_declaration'
	static int LOGICAL_TYPE = -1; // entity 'logical_type'
	static int NON_EXT_SELECT_TYPE = -1; // entity 'non_extensible_select_type'
	static int NUMBER_TYPE = -1; // entity 'number_type'
	static int REAL_TYPE = -1; // entity 'real_type'
	static int REFERENCED_DECL_TYPE_DECL = -1; // entity 'referenced_declaration$type_declaration'
	static int STRING_TYPE = -1; // entity 'string_type'
	static int SCHEMA_DEFINITION = -1; // entity 'schema_definition'
	static int SELECT_TYPE = -1; // entity 'select_type'
	static int SET_TYPE = -1; // entity 'set_type'
	static int TYPE_DECL_USED_DECL = -1; // entity 'type_declaration$used_declaration'
	static int UNIQUENESS_RULE = -1; // entity 'uniqueness_rule'

/**
	Here are constants related with serial number.
*/
	private static final String STR_SERIAL_CHAR = "123456789ABCDEFGHJKMNPQRSTUVWXYZ";
	private static final int STR_SERIAL_LEN = STR_SERIAL_CHAR.length();
	private String STR_SERIAL;

/**
 *  Stuff related with EJB sqlBridge
 */
	private static final Object sdaiBridgeRemoteFactorySync = new Object();
	private static SdaiBridgeRemoteFactory sdaiBridgeRemoteFactory = null;
	SessionRemote bridgeSession;
	static final boolean oldConnectionProtocol = false;
// 	static final String CONTEXT_NAME = "ejb/ejbclient/bridge";
	String bridgeURL = null;

	/* V.N. */
	Map methodCallsCacheMap = null;
    boolean methodCallsCacheInUse = false;
	QueryLibProvider queryLibProvider = null;
	Map jarFileURLStreamHandlers = new HashMap();

/**
	HashMap to store models from index files.
*/
    HashMap models_from_index_files = new HashMap(1024);

/** The name of the reserved SdaiModel "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA". */
    public static final String DICT_MODEL_NAME = "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA";
/** The name of the reserved SdaiModel "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA". */
    public static final String MAPP_MODEL_NAME = "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA";
/** The name of the reserved SdaiModel "MIXED_COMPLEX_TYPES_DICTIONARY_DATA". */
    public static final String COMP_MODEL_NAME = "MIXED_COMPLEX_TYPES_DICTIONARY_DATA";
/** The name of the reserved SchemaInstance "SDAI_DICTIONARY_SCHEMA". */
    public static final String DICT_SCHEMA_NAME = "SDAI_DICTIONARY_SCHEMA";
/** The name of the reserved SchemaInstance "SDAI_MAPPING_SCHEMA". */
    public static final String MAPP_SCHEMA_NAME = "SDAI_MAPPING_SCHEMA";


/**
    Loading from index file modes.
*/
    private static final String LOAD_SI = "SI"; // Loads schema instances
    private static final String LOAD_MO = "MO"; // Loads models (also loads and sets next persistent label)

/**
	Returns null since a session has no owner.
*/
	SdaiCommon getOwner() {
		return null;
	}


/**
	The method 'modified' for session is empty.
*/
	void modified() throws SdaiException {
	}


/**
 * Returns a clipboard - a globally available aggregate for all JSDAI applications
 * running on the same JVM. Applications can access the clipboard aggregate,
 * clear its contents, and add or remove instances of EEntity or other objects to or from it.
 * @return the pre-existing general purpose clipboard aggregate.
 */
	public AEntity getClipboard() {
//		synchronized (syncObject) {
		if (clipboard == null) {
			clipboard = new AEntity();
		}
		return clipboard;
//		} // syncObject
	}


	/**
	 * Returns the default context of this session.
	 * The default context is used expression evaluation during legacy
	 * JSDAI method calls that have no <code>SdaiContext</code> parameter.
	 *
	 * @return Current default context
	 */
	public SdaiContext getSdaiContext() {
//		synchronized (syncObject) {
			return sdai_context;
//		} // syncObject
	}


	/**
	 * Sets the default context of this session.
	 * The default context is used expression evaluation during legacy
	 * JSDAI method calls that have no <code>SdaiContext</code> parameter.
	 *
	 * @param context The new default context
	 */
	public void setSdaiContext(SdaiContext context) {
//		synchronized (syncObject) {
			sdai_context = context;
//		} // syncObject
	}


/**
 * Returns the session.
 * Only one <code>SdaiSession</code> object can exist at the same time,
 * and this object can be accessed by this method.
 * @return the only one <code>SdaiSession</code> or null if no session is open.
 */
	public static SdaiSession getSession() {
		return session;
	}


/**
 * Returns the instance of class <code>Implementation</code> containing
 * the general characteristics of JSDAI.
 * The <code>SdaiSession</code> has only one associated <code>Implementation</code>.
 * @return the associated <code>Implementation</code>.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException VA_NSET, value not set.
 */
	public Implementation getSdaiImplementation() throws SdaiException {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (sdai_implementation != null) {
			return sdai_implementation;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Returns the aggregate containing all repositories currently available
 * in this <code>SdaiSession</code>. This aggregate is called known servers.
 * @return the aggregate of available repositories.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getActiveServers
 */
	public ASdaiRepository getKnownServers() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		assertTrue(known_servers != null, "getKnownServers");
		return known_servers;
//		} // syncObject
	}


/**
 * Returns the aggregate containing all repositories currently open
 * in this <code>SdaiSession</code>. This aggregate is called active servers.
 * @return the aggregate of open repositories.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getKnownServers
 */
	public ASdaiRepository getActiveServers() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		assertTrue(active_servers != null, "getActiveServers");
		return active_servers;
//		} // syncObject
	}


/**
 * Checks whether this SDAI implementation supports data dictionary.
 * For JSDAI 2.0 and later versions this is always the case.
 * @return <code>true</code>.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public boolean testDataDictionary() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return (data_dictionary != null);
//		} // syncObject
	}


/**
 * Checks whether this SDAI implementation supports operations with mapping data.
 * For JSDAI 2.1 and later versions this is always the case.
 * @return <code>true</code>.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public boolean testDataMapping() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return (data_mapping != null);
//		} // syncObject
	}


/**
 * Returns the <code>SchemaInstance</code> with which all data dictionary
 * <code>SdaiModel</code>s are associated.
 * This schema instance has name "SDAI_DICTIONARY_SCHEMA_INSTANCE" and
 * belongs to the special repository with the name "SystemRepository".
 * In JSDAI 2.0 and later versions this schema instance is always available.
 * @return the <code>SchemaInstance</code> for the data dictionary.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getDataMapping
 */
	public SchemaInstance getDataDictionary() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (data_dictionary == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		} else {
			return data_dictionary;
		}
//		} // syncObject
	}


/**
 * Returns the <code>SchemaInstance</code> with which all <code>SdaiModel</code>s
 * with dictionary and mapping data are associated.
 * This schema instance has name "SDAI_MAPPING_SCHEMA_INSTANCE" and
 * belongs to the special repository with the name "SystemRepository".
 * In JSDAI 2.0 and later versions this schema instance is always available.
 * @return the <code>SchemaInstance</code> for both dictionary and mapping data.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getDataDictionary
 */
	public SchemaInstance getDataMapping() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (data_mapping == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		} else {
			return data_mapping;
		}
//		} // syncObject
	}


/**
 * Returns <code>SdaiModel</code>s currently being accessed in the session.
 * Such <code>SdaiModel</code>s must belong to opened repositories
 * and must have either read-only or read-write access mode started.
 * @return an aggregate containing all accessible <code>SdaiModel</code>s.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public ASdaiModel getActiveModels() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		assertTrue(active_models != null, "getActiveModels");
		return active_models;
//		} // syncObject
	}


/**
 * Checks if the transaction is currently available in the session.
 * The transaction, when started, makes access to all <code>SdaiModel</code>s and
 * <code>SchemaInstance</code>s contained in the repositories of this session.
 * @return <code>true</code> if an <code>SdaiTransaction</code> is available,
 * <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 * @see #getActiveTransaction
 */
	public boolean testActiveTransaction() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return (active_transaction != null);
//		} // syncObject
	}


/**
 * Returns the transaction currently available in the session.
 * For an <code>SdaiSession</code> there is either no or one
 * <code>SdaiTransaction</code> which can be accessed by this method.
 * If transaction is not available, then <code>SdaiException</code> VA_NSET is thrown.
 * @return the <code>SdaiTransaction</code> of this session.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #testActiveTransaction
 */
	public SdaiTransaction getActiveTransaction() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (active_transaction != null) {
			return active_transaction;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
//		} // syncObject
	}


/**
 * Informs if at least one repository available in this session is modified.
 * A repository is called modified if either at least one model or
 * schema instance has been created, deleted or modified or some data
 * outside repository contents were modified since the most recent
 * commit or abort operation was performed.
 * @return <code>true</code> if some repository of this session is modified, and
 * <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public boolean isModified() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		for (int k = 0; k < active_servers.myLength; k++) {
			SdaiRepository rep = (SdaiRepository)active_servers.myData[k];
			if (rep.isModified()) {
				return true;
			}
		}
		return false;
//		} // syncObject
	}


/**
 * Returns a special repository with the name "SystemRepository". This
 * repository is opened automatically during {@link SdaiSession#openSession openSession}.
 * However, its contents (<code>SdaiModel</code>s and
 * <code>SchemaInstance</code>s) are read-only; an application is not
 * allowed to modify them. The <code>SdaiModel</code>s in "SystemRepository"
 * contain either meta-descriptions of the supported
 * EXPRESS schemas or mapping information between such schemas.
 * The "SystemRepository" contains 2 <code>SchemaInstance</code>s:
 * with one all dictionary <code>SdaiModel</code>s (data_dictionary)
 * and with another all dictionary and mapping <code>SdaiModel</code>s (data_mapping)
 * are associated.
 * @return special repository named "SystemRepository".
 * @throws SdaiException SS_NOPN, session is not open.
 */
	public SdaiRepository getSystemRepository() throws SdaiException {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return systemRepository;
	}


/**
 * Returns a description of this session as a <code>String</code>.
 * It may be either "SdaiSession status: closed" or "SdaiSession status: open".
 * @return a description of the session.
 */
	public String toString() {
		if (!opened) {
			return "SdaiSession status: closed";
		} else {
			return "SdaiSession status: open";
		}
	}


/**
 * Sets the static logging/tracing <code>PrintWriter</code> that can be used during
 * any <code>SdaiSession</code>. To disable it, this method should be
 * applied with null passed to method's parameter.
 * @param out the new logging/tracing <code>PrintWriter</code>.
 * @see #getLogWriter
 * @see #println
 */
	public static void setLogWriter(PrintWriter out) {
		synchronized (logWriterSync) {
			if (logWriter != null && closeLogWriter) {
				logWriter.close();
			}
			logWriter = out;
			closeLogWriter = false;
		} // logWriterSync
	}

	static void printStackTraceToLogWriter(Throwable e) {
		synchronized(logWriterSync) {
			if(logWriter != null) {
				e.printStackTrace(logWriter);
			}
		}
	}

/**
 * Sets the logging/tracing <code>PrintWriter</code> that can be used during
 * the current <code>Object</code> of <code>SdaiSession</code>. To disable it, this method should be
 * applied with null passed to method's parameter.
 * @param out the new logging/tracing <code>PrintWriter</code>.
 * @see #getLogWriter
 * @see #println
 */
	public void setLogWriterSession(PrintWriter out) {
//		synchronized (syncObject) {
			logWriterSession = out;
//		} // syncObject
	}


/**
 * Returns the static logging/tracing <code>PrintWriter</code> that can be used during
 * any <code>SdaiSession</code>.
 * @return the logging/tracing <code>PrintWriter</code> or null if it is disabled.
 * @see #setLogWriter
 * @see #println
 */
	public static PrintWriter getLogWriter() {
		return logWriter;
	}


/**
 * Returns the logging/tracing <code>PrintWriter</code> that is associated with
 * the current <code>Object</code> of <code>SdaiSession</code>.
 * @return the logging/tracing <code>PrintWriter</code> or null if it is disabled.
 * @see #setLogWriter
 * @see #println
 */
	public PrintWriter getLogWriterSession() {
		return logWriterSession;
	}


/**
 * Prints a message to the static log Writer accessable for any <code>SdaiSession</code>.
 * If null value is passed to the method's parameter,
 * then <code>SdaiException</code> VA_NSET is thrown.
 * @param message a log or tracing message.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getLogWriter
 * @see #setLogWriter
 */
	public static void println(String message) throws SdaiException {
		synchronized (logWriterSync) {
			if (message == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (logWriter != null) {
				logWriter.println(message);
			}
		} // logWriterSync
	}


/**
 * Prints a message to the log Writer accessable only for the current
 * <code>Object</code> of <code>SdaiSession</code>.
 * If null value is passed to the method's parameter,
 * then <code>SdaiException</code> VA_NSET is thrown.
 * @param message a log or tracing message.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getLogWriter
 * @see #setLogWriter
 */
	public void printlnSession(String message) throws SdaiException {
//		synchronized (syncObject) {
		if (message == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (logWriterSession != null) {
			logWriterSession.println(message);
		} else  {
			println(message);
		}
//		} // syncObject
	}


/**
	Returns an object of java class File representing
	the file 'jsdai.properties'.
	Method is invoked in 'takeProperties'.
*/
	private static File takePropertiesFile() {
		File f;
		String dir_name = System.getProperty("jsdai.properties");
        if (dir_name != null) {
			f = new File(dir_name + File.separator + "jsdai.properties");
			if (f.exists()) {
				return f;
			}
		}
		f = new File("jsdai.properties");
		if (f.exists()) {
			return f;
		}
		f = new File(System.getProperty("user.home") +
				File.separator + "jsdai.properties");
		if (f.exists()) {
			return f;
		}
		String paths = System.getProperty("java.ext.dirs");
		String path;
		int first_sym = 0;
		int semicolon;
		boolean cont = true;
		while (cont) {
			if (first_sym >= paths.length()) {
				break;
			}
			semicolon = paths.indexOf(File.pathSeparatorChar, first_sym);
			if (semicolon == first_sym) {
				first_sym++;
				continue;
			}
			if (semicolon > 0) {
				path = paths.substring(first_sym, semicolon);
				first_sym = semicolon + 1;
			} else {
				path = paths.substring(first_sym);
				cont = false;
			}
			f = new File(path + File.separator + "jsdai.properties");
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}


/**
	Returns an object of java class Properties obtained
	from the file 'jsdai.properties'.
*/
	private static Properties takeProperties() throws SdaiException {
		File f = takePropertiesFile();
		if (f == null) {
			String base = line_separator + AdditionalMessages.IO_PRNF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		InputStream istr;
		try {
			istr = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			String base = line_separator + AdditionalMessages.IO_IOFF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Properties props = new Properties();
		try {
			props.load(istr);
			istr.close();
		} catch (IOException e) {
			String base = line_separator + AdditionalMessages.IO_ERPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return props;
	}


/**
	Returns an object of java class Properties obtained
	from the file 'sdairepos.properties'.
	This file belongs to the special repositories directory.
*/
	private static Properties takeRepoProperties() throws SdaiException {
		repoProps = new Properties();
		File f = new File(repos_path + File.separator + "sdairepos.properties");
		if (f.exists()) {
			try {
				InputStream istr = new BufferedInputStream(new FileInputStream(f));
				repoProps.load(istr);
				istr.close();
			} catch (FileNotFoundException e) {
				String base = line_separator + AdditionalMessages.IO_IOFF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} catch (IOException e) {
				String base = line_separator + AdditionalMessages.IO_ERRP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		return repoProps;
	}


/**
	Extracts pathname string for the server from properties.
*/
	static String getServerPath() throws SdaiException {
		String server = props.getProperty("server");
		if (server == null) {
			String base = line_separator + AdditionalMessages.IO_RENF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return server;
	}


/**
 * Returns the pathname string of the local directory containing subdirectories
 * for different repositories and some additional information.
 * After opening the session these repositories become members of
 * the set <code>knownServers</code> defined in the session.
 * @return the pathname string of the repositories directory.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public static String getRepositoriesPath() throws SdaiException {
		return getRepositoriesPath(props);
	}

	private static String getRepositoriesPath(Properties properties) throws SdaiException {
//		synchronized (syncObject) {
		String repos_path = properties.getProperty("repositories");
		if (repos_path == null) {
			String base = line_separator + AdditionalMessages.IO_RENF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return repos_path;
//		} // syncObject
	}


/**
	Assigns a value taken from properties to field 'toStringUnicode'.
*/
	private static void getUnicodeOrAscii() {
		String unicode = props.getProperty("toStringUnicode");
		if (unicode != null && (unicode.equals("TRUE") || unicode.equals("true"))) {
			toStringUnicode = true;
		}
	}

	static boolean isToStringUnicode() {
		synchronized (syncObject) {
			return toStringUnicode;
		}
	}


	String [][] getBlackList() throws SdaiException {
		if (bypassed_rules != null) {
			return bypassed_rules;
		}
		String bl_file_name = props.getProperty("black");
		if (bl_file_name == null) {
			byp_rules_count = -1;
			return null;
		}
		File black_list_file = new File(bl_file_name);
		if (!black_list_file.exists()) {
			if (logWriterSession != null) {
				printlnSession(AdditionalMessages.SS_BLNO + bl_file_name);
			} else {
				SdaiSession.println(AdditionalMessages.SS_BLNO + bl_file_name);
			}
			byp_rules_count = -1;
			return null;
		}
		BufferedReader brd;
		try {
			brd = new BufferedReader(new FileReader(black_list_file));
		} catch (FileNotFoundException ex) {
			if (logWriterSession != null) {
				printlnSession(AdditionalMessages.SS_BLNO + bl_file_name);
			} else {
				SdaiSession.println(AdditionalMessages.SS_BLNO + bl_file_name);
			}
			byp_rules_count = -1;
			return null;
		}

		String ent_name;
		String rule_name;
		bypassed_rules = new String[2][];
		bypassed_rules[0] = new String[BLACK_LIST_SIZE];
		bypassed_rules[1] = new String[BLACK_LIST_SIZE];
		byp_rules_count = 0;
		while (true) {
			String line;
			try {
				line = brd.readLine();
			} catch (IOException ex) {
				if (logWriterSession != null) {
					printlnSession(AdditionalMessages.SS_BLEX + bl_file_name);
				} else {
					SdaiSession.println(AdditionalMessages.SS_BLEX + bl_file_name);
				}
				break;
			}
			if (line == null) {
				break;
			}
			StringTokenizer st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				String str = st.nextToken();
				int dot = str.indexOf(PhFileReader.DOT);
				if (byp_rules_count >= bypassed_rules[0].length) {
					enlarge_bypassed_rules();
				}
				if (dot >= 0) {
					bypassed_rules[0][byp_rules_count] = str.substring(0, dot).toUpperCase();
					bypassed_rules[1][byp_rules_count] = str.substring(dot + 1);
				} else {
					bypassed_rules[0][byp_rules_count] = null;
					bypassed_rules[1][byp_rules_count] = str;
				}
				byp_rules_count++;
			}
		}
		if (byp_rules_count == 0) byp_rules_count--;
		return bypassed_rules;
	}


	private void enlarge_bypassed_rules() throws SdaiException {
		int new_length = bypassed_rules[0].length * 2;
		String [] new_ent_file = new String[new_length];
		System.arraycopy(bypassed_rules[0], 0, new_ent_file, 0, bypassed_rules[0].length);
		bypassed_rules[0] = new_ent_file;
		String [] new_rules_file = new String[new_length];
		System.arraycopy(bypassed_rules[1], 0, new_rules_file, 0, bypassed_rules[1].length);
		bypassed_rules[1] = new_rules_file;
	}


/**
	Returns port number taken from the properties.
*/
	static String getPortNumber() throws SdaiException {
		return props.getProperty("port");
	}


/**
	Creates a default instance of PrintWriter that can be used
	during SdaiSession for logging/tracing purposes.
	The method is invoked in the constructor of this class.
	The default instance can be replaced by another one using 'setLogWriter' method.
*/
	private void createWriterForLog() throws SdaiException {
		String str = props.getProperty("log");
		try {
			if (str != null) {
				synchronized(logWriterSync) {
					if (logWriter != null && closeLogWriter) {
						logWriter.close();
					}
					if (str.equals("System.out")) {
						closeLogWriter = false;
						logWriter = new PrintWriter(System.out, true);
					} else if (str.equals("System.err")) {
						closeLogWriter = false;
						logWriter = new PrintWriter(System.err, true);
					} else {
						closeLogWriter = true;
						logWriter = new PrintWriter(new FileWriter(str), true);
					}
				}
			}
		} catch (java.io.IOException ex){
		}
	}


/**
	Adds definition of an entity of SDAI dictionary schema to bootEntityAggregate.
*/
	private CEntity_definition addNewEntityDefinition(CSchema_definition dicsd, String name) throws SdaiException {
		CEntity_definition entity = (CEntity_definition) sDictionary.makeInstance(CEntity_definition.class, sessionModel, -1, 0);
		entity.setName(null, name);
		entity.setComplex(null, false);
		entity.owning_model = dicsd.owning_model;
		((AEntity)bootEntityAggregate).addUnorderedSY(entity);
		counter ++;
		return entity;
	}


/**
	Creates some temporary dictionary data for the bootstrapping proccess.
	This data is thrown away later.
*/
	private CSchema_definition initBootstrapDictionary(SdaiModel model) throws SdaiException {
		CSchema_definition dicsd = (CSchema_definition) sDictionary.makeInstance(CSchema_definition.class, sessionModel, -1, 0);
		baseDictionarySchemaDef = dicsd;
		dicsd.owning_model = model;
		dicsd.modelDictionary = model; // model is not necessary so far, it seems
		dicsd.setName(null, "SDAI_DICTIONARY_SCHEMA");
		bootEntityAggregate = new AEntity_definition();
		((AEntity)bootEntityAggregate).myType = setType0toN;
		((AEntity)bootEntityAggregate).owner = dicsd;

		CEntity_definition e;
		counter = -1;

/*   0 */ addNewEntityDefinition(dicsd, "aggregate_target_parameter");
/*   1 */ addNewEntityDefinition(dicsd, "aggregation_type"); AGGREGATION_TYPE = counter;
/*   2 */ addNewEntityDefinition(dicsd, "algorithm_declaration");
		CEntity_definition eAlgorithm_definition =
/*   3 */ addNewEntityDefinition(dicsd, "algorithm_definition");
		CEntity_definition eAndor_subtype_expression =
/*   4 */ addNewEntityDefinition(dicsd, "andor_subtype_expression");
		CEntity_definition eAnd_subtype_expression =
/*   5 */ addNewEntityDefinition(dicsd, "and_subtype_expression");
		CEntity_definition eAnnotation =
/*   6 */ addNewEntityDefinition(dicsd, "annotation");
/*   7 */ addNewEntityDefinition(dicsd, "array_type"); ARRAY_TYPE = counter;
		CEntity_definition eAttribute =
/*   8 */ addNewEntityDefinition(dicsd, "attribute");
/*   9 */ addNewEntityDefinition(dicsd, "bag_type"); BAG_TYPE = counter;
/*  10 */ addNewEntityDefinition(dicsd, "binary_type"); BINARY_TYPE = counter;
/*  11 */ addNewEntityDefinition(dicsd, "boolean_type"); BOOLEAN_TYPE = counter;
/*  12 */ addNewEntityDefinition(dicsd, "bound");
/*  13 */ addNewEntityDefinition(dicsd, "constant_declaration");
/*  14 */ addNewEntityDefinition(dicsd, "constant_declaration$implicit_declaration");
/*  15 */ addNewEntityDefinition(dicsd, "constant_declaration$inner_declaration");
/*  16 */ addNewEntityDefinition(dicsd, "constant_declaration$local_declaration");
/*  17 */ addNewEntityDefinition(dicsd, "constant_declaration$referenced_declaration");
/*  18 */ addNewEntityDefinition(dicsd, "constant_definition");
/*  19 */ addNewEntityDefinition(dicsd, "data_type"); DATA_TYPE = counter;
/*  20 */ addNewEntityDefinition(dicsd, "data_type_declaration");
/*  21 */ addNewEntityDefinition(dicsd, "data_type_declaration$implicit_declaration"); DATA_TYPE_DECL_IMPLICIT_DECL = counter;
/*  22 */ addNewEntityDefinition(dicsd, "data_type_declaration$local_declaration"); DATA_TYPE_DECL_LOCAL_DECL = counter;
/*  23 */ addNewEntityDefinition(dicsd, "declaration");
		CEntity_definition eDefined_type =
/*  24 */ addNewEntityDefinition(dicsd, "defined_type"); DEFINED_TYPE = counter;
/*  25 */ addNewEntityDefinition(dicsd, "dependent_map_definition");
/*  26 */ addNewEntityDefinition(dicsd, "dependent_map_partition");
/*  27 */ addNewEntityDefinition(dicsd, "dependent_source_parameter");
		CEntity_definition eDependent_view_definition =
/*  28 */ addNewEntityDefinition(dicsd, "dependent_view_definition");
/*  29 */ addNewEntityDefinition(dicsd, "derived_attribute"); DERIVED_ATTRIBUTE = counter;
		CEntity_definition eDocumentation =
/*  30 */ addNewEntityDefinition(dicsd, "documentation");
/*  31 */ addNewEntityDefinition(dicsd, "documentation_with_element");
/*  32 */ addNewEntityDefinition(dicsd, "domain_equivalent_type");
/*  33 */ addNewEntityDefinition(dicsd, "entity_declaration");
/*  34 */ addNewEntityDefinition(dicsd, "entity_declaration$implicit_declaration"); ENTITY_DECL_IMPLICIT_DECL = counter;
/*  35 */ addNewEntityDefinition(dicsd, "entity_declaration$inner_declaration");
/*  36 */ addNewEntityDefinition(dicsd, "entity_declaration$local_declaration"); ENTITY_DECL_LOCAL_DECL = counter;
/*  37 */ addNewEntityDefinition(dicsd, "entity_declaration$referenced_declaration"); ENTITY_DECL_REFERENCED_DECL = counter;
/*  38 */ addNewEntityDefinition(dicsd, "entity_declaration$used_declaration"); ENTITY_DECL_USED_DECL = counter;
		CEntity_definition eEntity_definition =
/*  39 */ addNewEntityDefinition(dicsd, "entity_definition"); ENTITY_DEFINITION = counter;
		CEntity_definition eEntity_or_view_definition =
/*  40 */ addNewEntityDefinition(dicsd, "entity_or_view_definition");
		CEntity_definition eEntity_select_type =
/*  41 */ addNewEntityDefinition(dicsd, "entity_select_type"); ENTITY_SELECT_TYPE = counter;
		CEntity_definition eEntity_extended_extensible_select_type =
/*  42 */ addNewEntityDefinition(dicsd, "entity_select_type$extended_select_type$extensible_select_type");
			ENT_EXT_EXT_SELECT_TYPE = counter;
		CEntity_definition eEntity_extended_non_extensible_select_type =
/*  43 */ addNewEntityDefinition(dicsd, "entity_select_type$extended_select_type$non_extensible_select_type");
			ENT_EXT_NON_EXT_SELECT_TYPE = counter;
		CEntity_definition eEntity_extensible_select_type =
/*  44 */ addNewEntityDefinition(dicsd, "entity_select_type$extensible_select_type");
			ENT_EXT_SELECT_TYPE = counter;
		CEntity_definition eEntity_non_extensible_select_type =
/*  45 */ addNewEntityDefinition(dicsd, "entity_select_type$non_extensible_select_type");
			ENT_NON_EXT_SELECT_TYPE = counter;
		CEntity_definition eEnumeration_type =
/*  46 */ addNewEntityDefinition(dicsd, "enumeration_type"); ENUMERATION_TYPE = counter;
/*  47 */ addNewEntityDefinition(dicsd, "explicit_attribute"); EXPLICIT_ATTRIBUTE = counter;
		CEntity_definition eExpress_code =
/*  48 */ addNewEntityDefinition(dicsd, "express_code");
		CEntity_definition eExtended_enumeration_type =
/*  49 */ addNewEntityDefinition(dicsd, "extended_enumeration_type"); EXTENDED_ENUM_TYPE = counter;
		CEntity_definition eExtended_extensible_enumeration_type =
/*  50 */ addNewEntityDefinition(dicsd, "extended_enumeration_type$extensible_enumeration_type");
			 EXTENDED_EXTENSIBLE_ENUM_TYPE = counter;
		CEntity_definition eExtended_select_type =
/*  51 */ addNewEntityDefinition(dicsd, "extended_select_type"); EXTENDED_SELECT_TYPE = counter;
		CEntity_definition eExtended_extensible_select_type =
/*  52 */ addNewEntityDefinition(dicsd, "extended_select_type$extensible_select_type"); EXT_EXT_SELECT_TYPE = counter;
		CEntity_definition eExtended_non_extensible_select_type =
/*  53 */ addNewEntityDefinition(dicsd, "extended_select_type$non_extensible_select_type"); EXT_NON_EXT_SELECT_TYPE = counter;
		CEntity_definition eExtensible_enumeration_type =
/*  54 */ addNewEntityDefinition(dicsd, "extensible_enumeration_type"); EXTENSIBLE_ENUM_TYPE = counter;
		CEntity_definition eExtensible_select_type =
/*  55 */ addNewEntityDefinition(dicsd, "extensible_select_type"); EXTENSIBLE_SELECT_TYPE = counter;
/*  56 */ addNewEntityDefinition(dicsd, "external_schema");
/*  57 */ addNewEntityDefinition(dicsd, "function_declaration");
/*  58 */ addNewEntityDefinition(dicsd, "function_declaration$implicit_declaration");
/*  59 */ addNewEntityDefinition(dicsd, "function_declaration$inner_declaration");
/*  60 */ addNewEntityDefinition(dicsd, "function_declaration$local_declaration");
/*  61 */ addNewEntityDefinition(dicsd, "function_declaration$referenced_declaration");
		CEntity_definition eFunction_definition =
/*  62 */ addNewEntityDefinition(dicsd, "function_definition");
/*  63 */ addNewEntityDefinition(dicsd, "generic_schema_definition");
		CEntity_definition eGlobal_rule =
/*  64 */ addNewEntityDefinition(dicsd, "global_rule");
/*  65 */ addNewEntityDefinition(dicsd, "identified_by_parameter");
/*  66 */ addNewEntityDefinition(dicsd, "implicit_declaration");
/*  67 */ addNewEntityDefinition(dicsd, "implicit_declaration$map_declaration");
/*  68 */ addNewEntityDefinition(dicsd, "implicit_declaration$procedure_declaration");
/*  69 */ addNewEntityDefinition(dicsd, "implicit_declaration$rule_declaration"); IMPLICIT_DECL_RULE_DECL = counter;
/*  70 */ addNewEntityDefinition(dicsd, "implicit_declaration$subtype_constraint_declaration");
/*  71 */ addNewEntityDefinition(dicsd, "implicit_declaration$type_declaration"); IMPLICIT_DECL_TYPE_DECL = counter;
/*  72 */ addNewEntityDefinition(dicsd, "implicit_declaration$view_declaration");
		CEntity_definition eIndependent_view_definition =
/*  73 */ addNewEntityDefinition(dicsd, "independent_view_definition");
/*  74 */ addNewEntityDefinition(dicsd, "inner_declaration");
/*  75 */ addNewEntityDefinition(dicsd, "inner_declaration$procedure_declaration");
/*  76 */ addNewEntityDefinition(dicsd, "inner_declaration$rule_declaration");
/*  77 */ addNewEntityDefinition(dicsd, "inner_declaration$subtype_constraint_declaration");
/*  78 */ addNewEntityDefinition(dicsd, "inner_declaration$type_declaration");
/*  79 */ addNewEntityDefinition(dicsd, "integer_bound");
/*  80 */ addNewEntityDefinition(dicsd, "integer_type"); INTEGER_TYPE = counter;
		CEntity_definition eInterfaced_declaration =
/*  81 */ addNewEntityDefinition(dicsd, "interfaced_declaration");
		CEntity_definition eInterface_specification =
/*  82 */ addNewEntityDefinition(dicsd, "interface_specification");
/*  83 */ addNewEntityDefinition(dicsd, "inverse_attribute"); INVERSE_ATTRIBUTE = counter;
/*  84 */ addNewEntityDefinition(dicsd, "list_type"); LIST_TYPE = counter;
/*  85 */ addNewEntityDefinition(dicsd, "local_declaration");
/*  86 */ addNewEntityDefinition(dicsd, "local_declaration$map_declaration");
/*  87 */ addNewEntityDefinition(dicsd, "local_declaration$procedure_declaration");
/*  88 */ addNewEntityDefinition(dicsd, "local_declaration$rule_declaration"); LOCAL_DECL_RULE_DECL = counter;
/*  89 */ addNewEntityDefinition(dicsd, "local_declaration$subtype_constraint_declaration");
/*  90 */ addNewEntityDefinition(dicsd, "local_declaration$type_declaration"); LOCAL_DECL_TYPE_DECL = counter;
/*  91 */ addNewEntityDefinition(dicsd, "local_declaration$view_declaration");
/*  92 */ addNewEntityDefinition(dicsd, "logical_type"); LOGICAL_TYPE = counter;
/*  93 */ addNewEntityDefinition(dicsd, "map_declaration");
/*  94 */ addNewEntityDefinition(dicsd, "map_declaration$referenced_declaration");
/*  95 */ addNewEntityDefinition(dicsd, "map_declaration$used_declaration");
/*  96 */ addNewEntityDefinition(dicsd, "map_definition");
/*  97 */ addNewEntityDefinition(dicsd, "map_or_view_input_parameter");
/*  98 */ addNewEntityDefinition(dicsd, "map_or_view_partition");
/*  99 */ addNewEntityDefinition(dicsd, "map_partition");
		CEntity_definition eNamed_type =
/* 100 */ addNewEntityDefinition(dicsd, "named_type");
		CEntity_definition eNon_extensible_select_type =
/* 101 */ addNewEntityDefinition(dicsd, "non_extensible_select_type"); NON_EXT_SELECT_TYPE = counter;
/* 102 */ addNewEntityDefinition(dicsd, "number_type"); NUMBER_TYPE = counter;
		CEntity_definition eOneof_subtype_expression =
/* 103 */ addNewEntityDefinition(dicsd, "oneof_subtype_expression");
		CEntity_definition eParameter =
/* 104 */ addNewEntityDefinition(dicsd, "parameter");
/* 105 */ addNewEntityDefinition(dicsd, "population_dependent_bound");
/* 106 */ addNewEntityDefinition(dicsd, "procedure_declaration");
/* 107 */ addNewEntityDefinition(dicsd, "procedure_declaration$referenced_declaration");
		CEntity_definition eProcedure_definition =
/* 108 */ addNewEntityDefinition(dicsd, "procedure_definition");
/* 109 */ addNewEntityDefinition(dicsd, "real_type"); REAL_TYPE = counter;
/* 110 */ addNewEntityDefinition(dicsd, "referenced_declaration");
/* 111 */ addNewEntityDefinition(dicsd, "referenced_declaration$type_declaration"); REFERENCED_DECL_TYPE_DECL = counter;
/* 112 */ addNewEntityDefinition(dicsd, "referenced_declaration$view_declaration");
		CEntity_definition eReference_from_specification =
/* 113 */ addNewEntityDefinition(dicsd, "reference_from_specification");
		CEntity_definition eReference_from_specification_as =
/* 114 */ addNewEntityDefinition(dicsd, "reference_from_specification_as");
		CEntity_definition eReference_from_specification_as_source =
/* 115 */ addNewEntityDefinition(dicsd, "reference_from_specification_as_source");
		CEntity_definition eReference_from_specification_as_target =
/* 116 */ addNewEntityDefinition(dicsd, "reference_from_specification_as_target");
/* 117 */ addNewEntityDefinition(dicsd, "rule_declaration");
/* 118 */ addNewEntityDefinition(dicsd, "schema_definition"); SCHEMA_DEFINITION = counter;
/* 119 */ addNewEntityDefinition(dicsd, "schema_map_definition");
/* 120 */ addNewEntityDefinition(dicsd, "schema_view_definition");
		CEntity_definition eSelect_type =
/* 121 */ addNewEntityDefinition(dicsd, "select_type"); SELECT_TYPE = counter;
/* 122 */ addNewEntityDefinition(dicsd, "set_type"); SET_TYPE = counter;
/* 123 */ addNewEntityDefinition(dicsd, "simple_type");
/* 124 */ addNewEntityDefinition(dicsd, "source_parameter");
/* 125 */ addNewEntityDefinition(dicsd, "string_type"); STRING_TYPE = counter;
/* 126 */ addNewEntityDefinition(dicsd, "subtype_constraint_declaration");
		CEntity_definition eSubtype_expression =
/* 127 */ addNewEntityDefinition(dicsd, "subtype_expression");
		CEntity_definition eSub_supertype_constraint =
/* 128 */ addNewEntityDefinition(dicsd, "sub_supertype_constraint");
/* 129 */ addNewEntityDefinition(dicsd, "target_parameter");
/* 130 */ addNewEntityDefinition(dicsd, "type_declaration");
/* 131 */ addNewEntityDefinition(dicsd, "type_declaration$used_declaration"); TYPE_DECL_USED_DECL = counter;
		CEntity_definition eUniqueness_rule =
/* 132 */ addNewEntityDefinition(dicsd, "uniqueness_rule");UNIQUENESS_RULE = counter;
/* 133 */ addNewEntityDefinition(dicsd, "used_declaration");
/* 134 */ addNewEntityDefinition(dicsd, "used_declaration$view_declaration");
		CEntity_definition eUse_from_specification =
/* 135 */ addNewEntityDefinition(dicsd, "use_from_specification");
/* 136 */ addNewEntityDefinition(dicsd, "variable_size_aggregation_type");
/* 137 */ addNewEntityDefinition(dicsd, "view_attribute");
/* 138 */ addNewEntityDefinition(dicsd, "view_declaration");
		CEntity_definition eView_definition =
/* 139 */ addNewEntityDefinition(dicsd, "view_definition");
/* 140 */ addNewEntityDefinition(dicsd, "view_partition");
/* 141 */ addNewEntityDefinition(dicsd, "view_partition_attribute");
/* 142 */ addNewEntityDefinition(dicsd, "where_rule");
		counter++;
		assertTrue(NUMBER_OF_ENTITIES == counter, "NUMBER_OF_ENTITIES == counter");

/*		CExplicit_attribute attrib_ent_sup =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_ent_sup = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_ent_sup.express_type = DataType.LIST;
		t_ent_sup.setLower_bound(null, bound0);
		t_ent_sup.aggregateClass = AEntity_definition.class;
		t_ent_sup.setElement_type(null, eEntity_definition);
		attrib_ent_sup.setDomain(null, t_ent_sup);
		attrib_ent_sup.setParent_entity(null, eEntity_definition);
		try {
			sDictionary.setDataField(CEntity_definition.class, "a6$", attrib_ent_sup);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
*/

		CExplicit_attribute attrib_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CSet_type t_sel = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
		t_sel.express_type = DataType.SET;
		t_sel.setLower_bound(null, bound1);
		t_sel.aggregateClass = ANamed_type.class;
		t_sel.setElement_type(null, eNamed_type);
		attrib_sel.setDomain(null, t_sel);
//		attrib_sel.setParent_entity(null, eSelect_type);
		attrib_sel.setParent(null, eSelect_type);
		CExplicit_attribute attrib_ent_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_sel.setDomain(null, t_sel);
//		attrib_ent_sel.setParent_entity(null, eEntity_select_type);
		attrib_ent_sel.setParent(null, eEntity_select_type);
		CExplicit_attribute attrib_extended_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extended_sel.setDomain(null, t_sel);
//		attrib_extended_sel.setParent_entity(null, eExtended_select_type);
		attrib_extended_sel.setParent(null, eExtended_select_type);
		CExplicit_attribute attrib_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extensible_sel.setDomain(null, t_sel);
//		attrib_extensible_sel.setParent_entity(null, eExtensible_select_type);
		attrib_extensible_sel.setParent(null, eExtensible_select_type);
		CExplicit_attribute attrib_non_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_non_extensible_sel.setDomain(null, t_sel);
//		attrib_non_extensible_sel.setParent_entity(null, eNon_extensible_select_type);
		attrib_non_extensible_sel.setParent(null, eNon_extensible_select_type);
		CExplicit_attribute attrib_ent_extended_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_extended_extensible_sel.setDomain(null, t_sel);
//		attrib_ent_extended_extensible_sel.setParent_entity(null, eEntity_extended_extensible_select_type);
		attrib_ent_extended_extensible_sel.setParent(null, eEntity_extended_extensible_select_type);
		CExplicit_attribute attrib_ent_extended_non_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_extended_non_extensible_sel.setDomain(null, t_sel);
//		attrib_ent_extended_non_extensible_sel.setParent_entity(null, eEntity_extended_non_extensible_select_type);
		attrib_ent_extended_non_extensible_sel.setParent(null, eEntity_extended_non_extensible_select_type);
		CExplicit_attribute attrib_ent_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_extensible_sel.setDomain(null, t_sel);
//		attrib_ent_extensible_sel.setParent_entity(null, eEntity_extensible_select_type);
		attrib_ent_extensible_sel.setParent(null, eEntity_extensible_select_type);
		CExplicit_attribute attrib_ent_non_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_non_extensible_sel.setDomain(null, t_sel);
//		attrib_ent_non_extensible_sel.setParent_entity(null, eEntity_non_extensible_select_type);
		attrib_ent_non_extensible_sel.setParent(null, eEntity_non_extensible_select_type);
		CExplicit_attribute attrib_extended_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extended_extensible_sel.setDomain(null, t_sel);
//		attrib_extended_extensible_sel.setParent_entity(null, eExtended_extensible_select_type);
		attrib_extended_extensible_sel.setParent(null, eExtended_extensible_select_type);
		CExplicit_attribute attrib_extended_non_extensible_sel =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extended_non_extensible_sel.setDomain(null, t_sel);
//		attrib_extended_non_extensible_sel.setParent_entity(null, eExtended_non_extensible_select_type);
		attrib_extended_non_extensible_sel.setParent(null, eExtended_non_extensible_select_type);
		try {
			sDictionary.setDataField(CSelect_type.class, "a1$", attrib_sel);
			sDictionary.setDataField(CEntity_select_type.class, "a1$", attrib_ent_sel);
			sDictionary.setDataField(CExtended_select_type.class, "a1$", attrib_extended_sel);
			sDictionary.setDataField(CExtensible_select_type.class, "a1$", attrib_extensible_sel);
			sDictionary.setDataField(CNon_extensible_select_type.class, "a1$", attrib_non_extensible_sel);
			sDictionary.setDataField(CEntity_select_type$extended_select_type$extensible_select_type.class,
				"a1$", attrib_ent_extended_extensible_sel);
			sDictionary.setDataField(CEntity_select_type$extended_select_type$non_extensible_select_type.class,
				"a1$", attrib_ent_extended_non_extensible_sel);
			sDictionary.setDataField(CEntity_select_type$extensible_select_type.class,
				"a1$", attrib_ent_extensible_sel);
			sDictionary.setDataField(CEntity_select_type$non_extensible_select_type.class,
				"a1$", attrib_ent_non_extensible_sel);
			sDictionary.setDataField(CExtended_select_type$extensible_select_type.class,
				"a1$", attrib_extended_extensible_sel);
			sDictionary.setDataField(CExtended_select_type$non_extensible_select_type.class,
				"a1$", attrib_extended_non_extensible_sel);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_enum =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_enum = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_enum.express_type = DataType.LIST;
		CString_type s_enum = (CString_type) sDictionary.makeInstance(CString_type.class, sessionModel, -1, 0);
		t_enum.setLower_bound(null, bound0);
		t_enum.aggregateClass = A_string.class;
		t_enum.setElement_type(null, s_enum);
		attrib_enum.setDomain(null, t_enum);
//		attrib_enum.setParent_entity(null, eEnumeration_type);
		attrib_enum.setParent(null, eEnumeration_type);
		CExplicit_attribute attrib_extended_enum =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extended_enum.setDomain(null, t_enum);
//		attrib_extended_enum.setParent_entity(null, eExtended_enumeration_type);
		attrib_extended_enum.setParent(null, eExtended_enumeration_type);
		CExplicit_attribute attrib_extensible_enum =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extensible_enum.setDomain(null, t_enum);
//		attrib_extensible_enum.setParent_entity(null, eExtensible_enumeration_type);
		attrib_extensible_enum.setParent(null, eExtensible_enumeration_type);
		CExplicit_attribute attrib_extended_extensible_enum =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_extended_extensible_enum.setDomain(null, t_enum);
//		attrib_extended_extensible_enum.setParent_entity(null, eExtended_extensible_enumeration_type);
		attrib_extended_extensible_enum.setParent(null, eExtended_extensible_enumeration_type);
		try {
			sDictionary.setDataField(CEnumeration_type.class, "a1$", attrib_enum);
			sDictionary.setDataField(CExtended_enumeration_type.class, "a1$", attrib_extended_enum);
			sDictionary.setDataField(CExtensible_enumeration_type.class, "a1$", attrib_extensible_enum);
			sDictionary.setDataField(CExtended_enumeration_type$extensible_enumeration_type.class,
				"a1$", attrib_extended_extensible_enum);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_gl_ent =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_gl_ent = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_gl_ent.express_type = DataType.LIST;
		t_gl_ent.setLower_bound(null, bound1);
		t_gl_ent.aggregateClass = AEntity_definition.class;
		t_gl_ent.setElement_type(null, eEntity_definition);
		attrib_gl_ent.setDomain(null, t_gl_ent);
//		attrib_gl_ent.setParent_entity(null, eGlobal_rule);
		attrib_gl_ent.setParent(null, eGlobal_rule);
		try {
			sDictionary.setDataField(CGlobal_rule.class, "a1$", attrib_gl_ent);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_un =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_un = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_un.express_type = DataType.LIST;
		t_un.setLower_bound(null, bound1);
		t_un.aggregateClass = AAttribute.class;
		t_un.setElement_type(null, eAttribute);
		attrib_un.setDomain(null, t_un);
//		attrib_un.setParent_entity(null, eUniqueness_rule);
		attrib_un.setParent(null, eUniqueness_rule);
		try {
			sDictionary.setDataField(CUniqueness_rule.class, "a1$", attrib_un);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}


/*		CExplicit_attribute attrib_subexpr =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CSet_type t_subexpr = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
		t_subexpr.express_type = DataType.SET;
		t_subexpr.setLower_bound(null, bound1);
		t_subexpr.aggregateClass = AEntity_or_subtype_expression.class;
		t_subexpr.setElement_type(null, eDefined_type);
		attrib_subexpr.setDomain(null, t_subexpr);
		attrib_subexpr.setParent_entity(null, eSubtype_expression);
		CExplicit_attribute attrib_asubexpr =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_asubexpr.setDomain(null, t_subexpr);
		attrib_asubexpr.setParent_entity(null, eAnd_subtype_expression);
		CExplicit_attribute attrib_aosubexpr =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_aosubexpr.setDomain(null, t_subexpr);
		attrib_aosubexpr.setParent_entity(null, eAndor_subtype_expression);
		CExplicit_attribute attrib_osubexpr =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_osubexpr.setDomain(null, t_subexpr);
		attrib_osubexpr.setParent_entity(null, eOneof_subtype_expression);
*/

		CExplicit_attribute attrib_subexpr_gen =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CSet_type t_subexpr_gen = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
		t_subexpr_gen.express_type = DataType.SET;
		t_subexpr_gen.setLower_bound(null, bound1);
		t_subexpr_gen.aggregateClass = AEntity_or_view_or_subtype_expression.class;
		t_subexpr_gen.setElement_type(null, eDefined_type);
		attrib_subexpr_gen.setDomain(null, t_subexpr_gen);
//		attrib_subexpr_gen.setParent_entity(null, eSubtype_expression);
		attrib_subexpr_gen.setParent(null, eSubtype_expression);
		CExplicit_attribute attrib_asubexpr_gen =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_asubexpr_gen.setDomain(null, t_subexpr_gen);
//		attrib_asubexpr_gen.setParent_entity(null, eAnd_subtype_expression);
		attrib_asubexpr_gen.setParent(null, eAnd_subtype_expression);
		CExplicit_attribute attrib_aosubexpr_gen =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_aosubexpr_gen.setDomain(null, t_subexpr_gen);
//		attrib_aosubexpr_gen.setParent_entity(null, eAndor_subtype_expression);
		attrib_aosubexpr_gen.setParent(null, eAndor_subtype_expression);
		CExplicit_attribute attrib_osubexpr_gen =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_osubexpr_gen.setDomain(null, t_subexpr_gen);
//		attrib_osubexpr_gen.setParent_entity(null, eOneof_subtype_expression);
		attrib_osubexpr_gen.setParent(null, eOneof_subtype_expression);
		try {
//			sDictionary.setDataField(CSubtype_expression.class, "a0$", attrib_subexpr);
//			sDictionary.setDataField(CAnd_subtype_expression.class, "a0$", attrib_asubexpr);
//			sDictionary.setDataField(CAndor_subtype_expression.class, "a0$", attrib_aosubexpr);
//			sDictionary.setDataField(COneof_subtype_expression.class, "a0$", attrib_osubexpr);
			sDictionary.setDataField(CSubtype_expression.class, "a0$", attrib_subexpr_gen);
			sDictionary.setDataField(CAnd_subtype_expression.class, "a0$", attrib_asubexpr_gen);
			sDictionary.setDataField(CAndor_subtype_expression.class, "a0$", attrib_aosubexpr_gen);
			sDictionary.setDataField(COneof_subtype_expression.class, "a0$", attrib_osubexpr_gen);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_alg =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_alg = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_alg.express_type = DataType.LIST;
		t_alg.setLower_bound(null, bound0);
		t_alg.aggregateClass = AParameter.class;
		t_alg.setElement_type(null, eParameter);
		attrib_alg.setDomain(null, t_alg);
//		attrib_alg.setParent_entity(null, eAlgorithm_definition);
		attrib_alg.setParent(null, eAlgorithm_definition);
		CExplicit_attribute attrib_fun =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_fun.setDomain(null, t_alg);
//		attrib_fun.setParent_entity(null, eFunction_definition);
		attrib_fun.setParent(null, eFunction_definition);
		CExplicit_attribute attrib_proc =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_proc.setDomain(null, t_alg);
//		attrib_proc.setParent_entity(null, eProcedure_definition);
		attrib_proc.setParent(null, eProcedure_definition);
		try {
			sDictionary.setDataField(CAlgorithm_definition.class, "a1$", attrib_alg);
			sDictionary.setDataField(CFunction_definition.class, "a1$", attrib_fun);
			sDictionary.setDataField(CProcedure_definition.class, "a1$", attrib_proc);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_annot =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_annot = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_annot.express_type = DataType.LIST;
		CString_type s_annot = (CString_type) sDictionary.makeInstance(CString_type.class, sessionModel, -1, 0);
		t_annot.setLower_bound(null, bound1);
		t_annot.aggregateClass = A_string.class;
		t_annot.setElement_type(null, s_annot);
		attrib_annot.setDomain(null, t_annot);
//		attrib_annot.setParent_entity(null, eAnnotation);
		attrib_annot.setParent(null, eAnnotation);
		CExplicit_attribute attrib_doc =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_doc.setDomain(null, t_annot);
//		attrib_doc.setParent_entity(null, eDocumentation);
		attrib_doc.setParent(null, eDocumentation);
		CExplicit_attribute attrib_exprcode =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_exprcode.setDomain(null, t_annot);
//		attrib_exprcode.setParent_entity(null, eExpress_code);
		attrib_exprcode.setParent(null, eExpress_code);
		try {
			sDictionary.setDataField(CAnnotation.class, "a1$", attrib_annot);
			sDictionary.setDataField(CDocumentation.class, "a1$", attrib_doc);
			sDictionary.setDataField(CExpress_code.class, "a1$", attrib_exprcode);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_par =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_par = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_par.express_type = DataType.LIST;
		CString_type s_par = (CString_type) sDictionary.makeInstance(CString_type.class, sessionModel, -1, 0);
		t_par.setLower_bound(null, bound1);
		t_par.aggregateClass = A_string.class;
		t_par.setElement_type(null, s_par);
		attrib_par.setDomain(null, t_par);
//		attrib_par.setParent_entity(null, eParameter);
		attrib_par.setParent(null, eParameter);

		CExplicit_attribute attrib_func_def =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_func_def.setDomain(null, t_par);
		attrib_func_def.setParent(null, eFunction_definition);

		try {
			sDictionary.setDataField(CParameter.class, "a3$", attrib_par);
			sDictionary.setDataField(CFunction_definition.class, "a4$", attrib_func_def); // added line
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_ent_view_def =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CList_type t_view_def = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
		t_view_def.express_type = DataType.LIST;
		t_view_def.setLower_bound(null, bound0);
		t_view_def.aggregateClass = AEntity_or_view_definition.class;
		t_view_def.setElement_type(null, eEntity_or_view_definition);
		attrib_ent_view_def.setDomain(null, t_view_def);
//		attrib_ent_view_def.setParent_entity(null, eEntity_or_view_definition);
		attrib_ent_view_def.setParent(null, eEntity_or_view_definition);
		CExplicit_attribute attrib_view_def =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_view_def.setDomain(null, t_view_def);
//		attrib_view_def.setParent_entity(null, eView_definition);
		attrib_view_def.setParent(null, eView_definition);
		CExplicit_attribute attrib_indep_view_def =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_indep_view_def.setDomain(null, t_view_def);
//		attrib_indep_view_def.setParent_entity(null, eIndependent_view_definition);
		attrib_indep_view_def.setParent(null, eIndependent_view_definition);
		CExplicit_attribute attrib_dep_view_def =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_dep_view_def.setDomain(null, t_view_def);
//		attrib_dep_view_def.setParent_entity(null, eDependent_view_definition);
		attrib_dep_view_def.setParent(null, eDependent_view_definition);
		CExplicit_attribute attrib_ent_sup_gen =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_ent_sup_gen.setDomain(null, t_view_def);
//		attrib_ent_sup_gen.setParent_entity(null, eEntity_definition);
		attrib_ent_sup_gen.setParent(null, eEntity_definition);
		try {
			sDictionary.setDataField(CEntity_or_view_definition.class, "a2$", attrib_ent_view_def);
			sDictionary.setDataField(CView_definition.class, "a2$", attrib_view_def);
			sDictionary.setDataField(CIndependent_view_definition.class, "a2$", attrib_indep_view_def);
			sDictionary.setDataField(CDependent_view_definition.class, "a2$", attrib_dep_view_def);
			sDictionary.setDataField(CEntity_definition.class, "a2$", attrib_ent_sup_gen);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_interf_spec =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CSet_type t_interf_spec = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
		t_interf_spec.express_type = DataType.SET;
		t_interf_spec.setLower_bound(null, bound1);
		t_interf_spec.aggregateClass = AInterfaced_declaration.class;
		t_interf_spec.setElement_type(null, eInterfaced_declaration);
		attrib_interf_spec.setDomain(null, t_interf_spec);
//		attrib_interf_spec.setParent_entity(null, eInterface_specification);
		attrib_interf_spec.setParent(null, eInterface_specification);
		CExplicit_attribute attrib_refer_spec =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_refer_spec.setDomain(null, t_interf_spec);
//		attrib_refer_spec.setParent_entity(null, eReference_from_specification);
		attrib_refer_spec.setParent(null, eReference_from_specification);
		CExplicit_attribute attrib_refer_spec_as =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_refer_spec_as.setDomain(null, t_interf_spec);
//		attrib_refer_spec_as.setParent_entity(null, eReference_from_specification_as);
		attrib_refer_spec_as.setParent(null, eReference_from_specification_as);
		CExplicit_attribute attrib_refer_spec_as_source =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_refer_spec_as_source.setDomain(null, t_interf_spec);
//		attrib_refer_spec_as_source.setParent_entity(null, eReference_from_specification_as_source);
		attrib_refer_spec_as_source.setParent(null, eReference_from_specification_as_source);
		CExplicit_attribute attrib_refer_spec_as_target =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_refer_spec_as_target.setDomain(null, t_interf_spec);
//		attrib_refer_spec_as_target.setParent_entity(null, eReference_from_specification_as_target);
		attrib_refer_spec_as_target.setParent(null, eReference_from_specification_as_target);
		CExplicit_attribute attrib_use_spec =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		attrib_use_spec.setDomain(null, t_interf_spec);
//		attrib_use_spec.setParent_entity(null, eUse_from_specification);
		attrib_use_spec.setParent(null, eUse_from_specification);
		try {
			sDictionary.setDataField(CInterface_specification.class, "a2$", attrib_interf_spec);
			sDictionary.setDataField(CReference_from_specification.class, "a2$", attrib_refer_spec);
			sDictionary.setDataField(CReference_from_specification_as.class, "a2$", attrib_refer_spec_as);
			sDictionary.setDataField(CReference_from_specification_as_source.class,
				"a2$", attrib_refer_spec_as_source);
			sDictionary.setDataField(CReference_from_specification_as_target.class,
				"a2$", attrib_refer_spec_as_target);
			sDictionary.setDataField(CUse_from_specification.class, "a2$", attrib_use_spec);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		CExplicit_attribute attrib_tot_cov =
			(CExplicit_attribute) sDictionary.makeInstance(CExplicit_attribute.class, sessionModel, -1, 0);
		CSet_type t_tot_cov = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
		t_tot_cov.express_type = DataType.SET;
		t_tot_cov.setLower_bound(null, bound0);
		t_tot_cov.aggregateClass = AEntity_definition.class;
		t_tot_cov.setElement_type(null, eEntity_definition);
		attrib_tot_cov.setDomain(null, t_tot_cov);
//		attrib_tot_cov.setParent_entity(null, eSub_supertype_constraint);
		attrib_tot_cov.setParent(null, eSub_supertype_constraint);
		try {
			sDictionary.setDataField(CSub_supertype_constraint.class, "a2$", attrib_tot_cov);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.NoSuchFieldException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}

		return dicsd;
	}


/**
	Used to create and open SdaiSession.
*/
	private SdaiSession() throws SdaiException {
//		synchronize(syncObject) {

		if (session == null) {
			line_separator = System.getProperty("line.separator");
			if(props == null) {
				Properties new_properties = takeProperties();
				String new_repos_path = getRepositoriesPath(new_properties);
				repos_path_lock = lockRepositoriesPath(new_repos_path);
				props = new_properties;
				repos_path = new_repos_path;
			} else {
				String new_repos_path = getRepositoriesPath(props);
				repos_path_lock = lockRepositoriesPath(new_repos_path);
				repos_path = new_repos_path;
			}
			session = this;
		}
		undo_redo_file_saved = undo_redo_file;
		undo_redo_file = null;
		cal = new SdaiCalendar();
		opened = true;
		CEntity_definition dicEntities[];
		sdai_implementation = new Implementation();
		sdai_implementation.session = this;
		boolean sysrepo_exists;

		if (systemRepository == null) {
			sysrepo_exists = false;
			systemRepository = new SdaiRepositoryDictionaryImpl(this, "SystemRepository", null, false);
			systemRepository.build_number = 195;
			sessionModel = systemRepository.createModel("SESSION_DATA", (CSchema_definition)null);
			sessionModel.mode = SdaiModel.READ_WRITE;
		} else {
			sysrepo_exists = true;
		}

		if (isMainSession()) {
			sDictionary = new SDictionary(); // added recently
			setTypeSpecial = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeSpecial.shift = PRIVATE_AGGR;
			setTypeSpecial.express_type = DataType.SET;

		// make boun0
			bound0 = (CInteger_bound) sDictionary.makeInstance(CInteger_bound.class, sessionModel, -1, 0);
			bound0.setBound_value(null, 0);

		// make boun1
			bound1 = (CInteger_bound) sDictionary.makeInstance(CInteger_bound.class, sessionModel, -1, 0);
			bound1.setBound_value(null, 1);

		// make setType0toN : SET[0:?]
			setType0toN = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
			setType0toN.shift = 0;
			setType0toN.express_type = DataType.SET;
			setType0toN.setLower_bound(null, bound0);

		// make setType1toN : SET[1:?]
			setType1toN = (CSet_type) sDictionary.makeInstance(CSet_type.class, sessionModel, -1, 0);
			setType1toN.shift = 0;
			setType1toN.express_type = DataType.SET;
			setType1toN.setLower_bound(null, bound1);

			setTypeSpecialNonEmpty = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeSpecialNonEmpty.shift = PRIVATE_AGGR;
			setTypeSpecialNonEmpty.express_type = DataType.SET;
			setTypeSpecialNonEmpty.setLower_bound(null, bound1);

			setTypeForInstances = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstances.shift = INST_AGGR;
			setTypeForInstances.express_type = DataType.SET;

			setTypeForInstancesExact = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesExact.shift = INST_EXACT_AGGR;
			setTypeForInstancesExact.express_type = DataType.SET;

			setTypeForInstancesCompl = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesCompl.shift = INST_COMPL_AGGR;
			setTypeForInstancesCompl.express_type = DataType.SET;
			setTypeForInstancesCompl.setLower_bound(null, bound0);

			setTypeForInstancesAll = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesAll.shift = ALL_INST_AGGR;
			setTypeForInstancesAll.express_type = DataType.SET;

			setTypeForInstancesListOfModelAll = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesListOfModelAll.shift = ASDAIMODEL_INST_ALL;
			setTypeForInstancesListOfModelAll.express_type = DataType.SET;

			setTypeForInstancesListOfModel = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesListOfModel.shift = ASDAIMODEL_INST;
			setTypeForInstancesListOfModel.express_type = DataType.SET;

			setTypeForInstancesListOfModelExact = (CSet_type) sDictionary.makeInstance(CSet_type.class,
				sessionModel, -1, 0);
			setTypeForInstancesListOfModelExact.shift = ASDAIMODEL_INST_EXACT;
			setTypeForInstancesListOfModelExact.express_type = DataType.SET;

			listType0toN = (CList_type) sDictionary.makeInstance(CList_type.class, sessionModel, -1, 0);
			listType0toN.shift = 0;
			listType0toN.express_type = DataType.LIST;
			listType0toN.setUnique_flag(null, true);
			listType0toN.setLower_bound(null, bound0);

			listTypeSpecial = (CList_type) sDictionary.makeInstance(CList_type.class,
				sessionModel, -1, 0);
			listTypeSpecial.shift = PRIVATE_AGGR;
			listTypeSpecial.express_type = DataType.LIST;
			listTypeSpecial.setUnique_flag(null, false);
			listTypeSpecial.setLower_bound(null, bound1);

			listTypeSpecialU = (CList_type) sDictionary.makeInstance(CList_type.class,
				sessionModel, -1, 0);
			listTypeSpecialU.shift = PRIVATE_AGGR;
			listTypeSpecialU.express_type = DataType.LIST;
			listTypeSpecialU.setUnique_flag(null, true);
			listTypeSpecialU.setLower_bound(null, bound1);
		}

        Implementation implementation = session.getSdaiImplementation();
        String model_version =  implementation.getSdaiVersion();

		if (!sysrepo_exists) {
			systemRepository.description = new A_string(listTypeSpecial, systemRepository);
			systemRepository.description.addByIndexPrivate(1,
				"A REPOSITORY CONTAINING DICTIONARY AND MAPPING DATA");
			systemRepository.models.myType = setType0toN;
			systemRepository.schemas.myType = setType0toN;
			baseDictionaryModel =
				(SdaiModelDictionaryImpl)systemRepository.createModel("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA", (CSchema_definition)null);
            baseDictionaryModel.setVersion(-1);
			systemRepository.models.addUnorderedRO(baseDictionaryModel);
			systemRepository.insertModel();
			baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			create_header(systemRepository);
		}

		known_servers = new ASdaiRepository(setType1toN, this);
		active_servers = new ASdaiRepository(setType1toN, this);
		active_models = new ASdaiModel(setType1toN, this);

		known_servers.addUnorderedRO(systemRepository);
		active_servers.addUnorderedRO(systemRepository);
		systemRepository.active = true;

		CSchema_definition dicsd;
		if (!sysrepo_exists) {
			baseDictionaryModel.schemaData = new SchemaData(baseDictionaryModel);
			baseDictionaryModel.schemaData.initializeSchemaData(NUMBER_OF_ENTITIES, NUMBER_OF_ENTITIES);
			baseDictionaryModel.schemaData.super_inst = sDictionary;
			dicsd = initBootstrapDictionary(baseDictionaryModel);
			baseDictionaryModel.described_schema = dicsd;
			baseDictionaryModel.underlying_schema = dicsd;
			baseDictionaryModel.identifier = -1;
			baseDictionaryModel.initializeContents();
			baseDictionaryModel.loadResource("jsdai/dictionary/");

		// from now on the bootstrap dictionary data is no longer needed
			dicsd = baseDictionaryModel.schemaData.init();
			baseDictionaryModel.described_schema = dicsd;
			baseDictionaryModel.underlying_schema = dicsd;
			baseDictionaryModel.schemaData.linkEarlyBinding(null);
			baseDictionaryModel.formFoldersForBaseDictModel();
//System.out.println("   BEGINNING of print");
//for (int i = 0; i < baseDictionaryModel.lengths[ENTITY_DEFINITION]; i++) {
//CEntity_definition eee =
//(CEntity_definition)baseDictionaryModel.instances_sim[ENTITY_DEFINITION][i];
//String nnn = eee.getName(null);
//System.out.println("  name: " + nnn);
//}

			baseDictionaryModel.mode = SdaiModel.READ_ONLY;
		} else {
			dicsd = baseDictionaryModel.described_schema;
			updateSystemRepository();
		}

		// --------------------------------------------------
		int i, j;

//System.out.println("SdaiSession    isMainSession: " + isMainSession() + "   this: " + this);
		if (isMainSession()) {
			active_transaction = new SdaiTransaction(SdaiTransaction.READ_WRITE, this);
			getUnicodeOrAscii();
			repoProps = takeRepoProperties();
/*String max_repo = repoProps.getProperty("r_max", "0");
int r_count = Integer.parseInt(max_repo);
System.out.println("  SdaiSession  max_repo: " + max_repo + "   r_count = " + r_count);
repoProps.setProperty("r2", "test");
Enumeration enum = repoProps.propertyNames();
while (enum.hasMoreElements()) {
String str = (String)enum.nextElement();
System.out.println("  SdaiSession key: " + str);
}
repoProps.remove("r_max");
repoProps.setProperty("r_max", Integer.toString(r_count+1));
max_repo = repoProps.getProperty("r_max", "0");
System.out.println("  SdaiSession  max_repo again: " + max_repo);
File fff = new File(repos_path + File.separator + "sdairepos.properties");
try {
OutputStream ostr = new BufferedOutputStream(new FileOutputStream(fff));
repoProps.store(ostr, "Repository directory names (as keys) and repository names (as values) are listed");
ostr.close();
} catch (FileNotFoundException e) {
String base = line_separator + AdditionalMessages.IO_IOFF;
throw new SdaiException(SdaiException.SY_ERR, base);
} catch (IOException e) {
String base = line_separator + AdditionalMessages.IO_ERRP;
throw new SdaiException(SdaiException.SY_ERR, base);
}*/
			createWriterForLog();
			File dir = new File(repos_path);
			if (dir.exists()) {
				repo_properties_changed = false;
				String [] subdirs = dir.list();
				if (subdirs == null) {
					throw new SdaiException(SdaiException.SY_ERR,
						"Provided pathname does not denote a directory");
				}
				int init_repo_count = Integer.parseInt(repoProps.getProperty("r_max", "0"));
				repo_count = init_repo_count;
				for (i = 0; i < subdirs.length; i++) {
					if (subdirs[i].equals(APPLICATIONS_DIR_NAME)) {
						continue;
					}
					String repository_name = null;
					String repository_location = null;
					boolean new_formats = false;
					boolean is_rep_contents = false;
					boolean is_rep_header = false;
					File subdir = new SdaiRepositoryZipImpl.FileForZipFile(repos_path, subdirs[i]);
					if (!subdir.isDirectory()) {
						try {
							ZipFile zipFile = new ZipFile(subdir);
							try {
								ZipEntry zipEntry = zipFile.getEntry("repository");
								if (zipEntry == null) continue;
							} finally {
								zipFile.close();
							}
							repository_name = "";
							repository_location = subdir.getAbsolutePath();
							updated_dir_name = subdirs[i];
							new_formats = true;
						}
						catch (IOException ex) {
							continue;
						}
					}
					else {
						File repos_dir = new File((String)subdir.getAbsolutePath());
						String [] files = subdir.list();
						String prop_value = repoProps.getProperty(subdirs[i]);
						if (prop_value != null && prop_value.length() > DEF_REP_NAME_LENGTH) {
							if ((prop_value.substring(0, DEF_REP_NAME_LENGTH)).equals(DEF_REP_NAME)) {
								boolean return_value = true;
								for (j = 0; j < files.length; j++) {
									File f = new File(subdir, files[j]);
									return_value = f.delete();
									if (!return_value) {
										break;
									}
								}
								if (return_value) {
									return_value = subdir.delete();
								}
								continue;
							}
						}
						if (prop_value == null) {
							updated_dir_name = null;
							prop_value = extractRepositoryName(subdirs[i], subdirs, files, repos_dir);
							if (prop_value == null) {
								continue;
							}
							if (updated_dir_name != null) {
								subdir = new File(repos_path, updated_dir_name);
							}
						} else {
							updated_dir_name = subdirs[i];
						}

						for (j = 0; j < files.length; j++) {
							if (files[j].equals("contents")) {
								is_rep_contents = true;
							} else if (files[j].equals("header")) {
								is_rep_header = true;
							} else if (files[j].equals("repository")) {
								new_formats = true;
							}
						}
						if (!new_formats && !is_rep_header) {
							continue;
						}
						repository_name = prop_value;
						repository_location = subdir.getAbsolutePath();
					}

					SdaiRepository repo = (SdaiRepository)linkRepository(repository_name, repository_location);
					repo.dir_name = updated_dir_name;
					if (!new_formats && !is_rep_contents) {
						repo.created_or_imported = true;
					}
				}
				checkProperties(init_repo_count);
				if (repo_properties_changed) {
					updateSdaireposProperties();
				}
			} else {
				throw new SdaiException(SdaiException.SY_ERR, "Directory for SdaiRepositories <" + repos_path + "> does not exist");
			}
		}

	//------------------------------------------------------------------------------------------

		if (!sysrepo_exists) {
			data_dictionary =
				((SdaiRepositoryDictionaryImpl)systemRepository).createDictionarySchemaInstance(DICTIONARY_SCHEMA_INSTANCE_NAME, dicsd);
			data_dictionary.allow_model = true;
			data_dictionary.committed = true;
			data_dictionary.addSdaiModel(baseDictionaryModel);
	// --------------------------------------------------

			baseComplexModel =
				systemRepository.createModel("MIXED_COMPLEX_TYPES_DICTIONARY_DATA", dicsd);
            ((SdaiModelDictionaryImpl)baseComplexModel).setVersion(-1);
			systemRepository.models.addUnorderedRO(baseComplexModel);
			systemRepository.insertModel();
			data_dictionary.addSdaiModel(baseComplexModel);
			baseMappingModel =
				systemRepository.createModel("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA", dicsd);
            ((SdaiModelDictionaryImpl)baseMappingModel).setVersion(-1);
			systemRepository.models.addUnorderedRO(baseMappingModel);
			systemRepository.insertModel();
			data_dictionary.addSdaiModel(baseMappingModel);
			baseMappingModel.startReadOnlyAccess();
			mappingSchemaDefinition = (CSchema_definition)baseMappingModel.described_schema;
			data_mapping =
				((SdaiRepositoryDictionaryImpl)systemRepository).createDictionarySchemaInstance(MAPPING_SCHEMA_INSTANCE_NAME, mappingSchemaDefinition);
			data_mapping.committed = true;
			data_mapping.allow_model = true;
			data_mapping.addSdaiModel(baseDictionaryModel);
			data_mapping.addSdaiModel(baseComplexModel);
			data_mapping.addSdaiModel(baseMappingModel);


            //-- adding reserved models to models_from_index_files to use them as ordinary models
            models_from_index_files.put(baseDictionaryModel.getName(), baseDictionaryModel);
            models_from_index_files.put(baseMappingModel.getName(), baseMappingModel);
            models_from_index_files.put(baseComplexModel.getName(), baseComplexModel);
			//loadSchemaInstances();
            loadSchemaInstancesModelsFromProperties(LOAD_MO);
            loadSchemaInstancesModelsFromProperties(LOAD_SI);


			Enumeration keys = props.propertyNames();
			while (keys.hasMoreElements()) {
				String sch = (String)keys.nextElement();
				if (sch.length() < SCHEMA_PREFIX_LENGTH) {
					continue;
				}
				boolean fSchema = sch.startsWith(SCHEMA_PREFIX);
				boolean fMapping = sch.startsWith(MAPPING_PREFIX);
				SdaiModel dictionary = null;
				SdaiModel mod_found;
				String dict_name;
				if (fSchema || fMapping) {
					String sch_adj = (sch.substring(SCHEMA_PREFIX_LENGTH)).toUpperCase();
					if (fSchema) {
						dict_name = sch_adj + DICTIONARY_NAME_SUFIX;
						mod_found = systemRepository.findSdaiModel(dict_name);
						if (mod_found != null) {
							extractSchemas(props, sch, mod_found);
							continue;
						}
						dictionary =
							systemRepository.createModel(dict_name, dicsd);
//System.out.println("   SdaiSession  model created = " + dict_name);
						data_dictionary.addSdaiModel(dictionary);
						data_mapping.addSdaiModel(dictionary);
					} else {
						dict_name = sch_adj + MAPPING_NAME_SUFIX;
						mod_found = systemRepository.findSdaiModel(dict_name);
						if (mod_found != null) {
							continue;
						}
						dictionary =
							systemRepository.createModel(dict_name, mappingSchemaDefinition);
						data_mapping.addSdaiModel(dictionary);
					}
					systemRepository.models.addUnorderedRO(dictionary);
					systemRepository.insertModel();
				}
				if (dictionary != null) {
					extractSchemas(props, sch, dictionary);
/*if (dictionary.property_schemas != null)
for (i = 0; i < dictionary.property_schemas.length; i++) {
System.out.println("   SdaiSession  ******* i = " + i);
String [] line = dictionary.property_schemas[i];
for (j = 0; j < line.length; j++) {
System.out.println("   SdaiSession  str = " + line[j]);
}
}*/
				}
			}
			data_dictionary.allow_model = false;
			data_mapping.allow_model = false;
		} else {
			SchemaInstance schema_inst = systemRepository.findSchemaInstance(DICTIONARY_SCHEMA_INSTANCE_NAME);
			if (schema_inst != null) {
				data_dictionary = schema_inst;
			}
			schema_inst = systemRepository.findSchemaInstance(MAPPING_SCHEMA_INSTANCE_NAME);
			if (schema_inst != null) {
				data_mapping = schema_inst;
			}

/*			for (i = 0; i < systemRepository.schemas.myLength; i++) {
				SchemaInstance schema_inst = (SchemaInstance)systemRepository.schemas.myData[i];
				if (schema_inst.name.equals(DICTIONARY_SCHEMA_INSTANCE_NAME)) {
					data_dictionary = schema_inst;
				} else if (schema_inst.name.equals(MAPPING_SCHEMA_INSTANCE_NAME)) {
					data_mapping = schema_inst;
				}
			} */
		}

		if (isMainSession()) {
			active_transaction.owning_session = null;
			active_transaction = null;
		}
		if (!sysrepo_exists) {
			systemRepository.schemas.addUnorderedRO(data_dictionary);
			systemRepository.insertSchemaInstance();
			systemRepository.schemas.addUnorderedRO(data_mapping);
			systemRepository.insertSchemaInstance();
		}
		undo_redo_file = undo_redo_file_saved;

//		} // syncObject
	}


/**
	Extracts schema instances or models from the index file produced by Express compiler.
	This file should be located in parallel with 'lang' directory.
	Its absence does not stop JSDAI from the correct behaviour.
	But in this case special schema instances or models are not created.
	If created, schema instances or models belong to SystemRepository.
*/
	private void loadSchemaInstancesModelsFromProperties(String mode) throws SdaiException {
        Set allIndexes = new HashSet();
		try {
			ClassLoader classLoader = SdaiClassLoaderProvider.getDefault().getClassLoader();
			Enumeration repositoryPropertyUrls = classLoader.getResources("jsdai/repository.properties");
			while(repositoryPropertyUrls.hasMoreElements()) {
				URL repositoryPropertyUrl = (URL)repositoryPropertyUrls.nextElement();
				try {
					Properties index = loadSchemaInstModelsFromPropURL(repositoryPropertyUrl, mode, allIndexes);
					if(index != null) {
						allIndexes.add(index);
					}
				} catch(SdaiException e) {
					if(e.getErrorId() == SdaiException.MO_DUP) {
						SdaiException wrapper =
							new SdaiException(SdaiException.SY_ERR, "Duplicates in property file: " +
											  repositoryPropertyUrl);
						wrapper.initCause(e);
						throw wrapper;
					} else {
						throw e;
					}
				}
			}

			for(int index_file_idx = 0; ;index_file_idx++) {
				//System.out.println("-------------------------------------------------------------");
				String l_side = PROPERTY_FOR_SYSTEM_REPOSITORY + "." + index_file_idx;
				String r_side = props.getProperty(l_side, "");
				if (!r_side.equals("")) {
					URL repositoryPropertyUrl =
						classLoader.getResource("jsdai/" + r_side + ".properties");
					if (repositoryPropertyUrl == null) {
						if (logWriterSession != null) {
							printlnSession(AdditionalMessages.SS_RFMI);
						} else {
							SdaiSession.println(AdditionalMessages.SS_RFMI);
						}
						break;
					}
					try {
						Properties index = loadSchemaInstModelsFromPropURL(repositoryPropertyUrl, mode, allIndexes);
						if(index != null) {
							allIndexes.add(index);
						}
					} catch(SdaiException e) {
						if(e.getErrorId() == SdaiException.MO_DUP) {
							SdaiException wrapper =
								new SdaiException(SdaiException.SY_ERR, "Duplicates in property file: " +
												  repositoryPropertyUrl);
							wrapper.initCause(e);
							throw wrapper;
						} else {
							throw e;
						}
					}
				} else {
					break;
				}
			}
		} catch (IOException ex) {
			if (logWriterSession != null) {
				printlnSession(AdditionalMessages.SS_RFNR);
			} else {
				SdaiSession.println(AdditionalMessages.SS_RFNR);
			}
		}
	}

	private Properties loadSchemaInstModelsFromPropURL(URL repositoryPropertyUrl, String mode,
													   Set allIndexes) throws SdaiException, IOException {
		InputStream istream = null;
		try {
			istream = repositoryPropertyUrl.openStream();
		} catch (IOException ex) {
			if (logWriterSession != null) {
				printlnSession(AdditionalMessages.SS_RFNR + ": " + repositoryPropertyUrl);
			} else {
				SdaiSession.println(AdditionalMessages.SS_RFNR + ": " + repositoryPropertyUrl);
			}
			//return;
		}
        Properties index = new Properties();
		try {
			index.load(istream);
			istream.close();
		} catch (IOException e) {
			String base = line_separator + AdditionalMessages.IO_ERPR;
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR, base).initCause(e);
		}
		if(allIndexes.contains(index)) {
			return null;
		}
		try {
			if (Implementation.major_version != 0 && !index.getProperty("JSDAIVersion", "$")
				.equalsIgnoreCase(Implementation.major_version + "." +
								  Implementation.middle_version + "." +
								  Implementation.minor_version)) {
				String base =
					line_separator + "JSDAI versions in repository index file " +
					repositoryPropertyUrl + " and jsdai.lang do not match";
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (mode == LOAD_MO) {
				((SdaiRepositoryDictionaryImpl)systemRepository)
					.loadModels(index, models_from_index_files);
			}
			if (mode == LOAD_SI) {
				((SdaiRepositoryDictionaryImpl)systemRepository)
					.loadSchemaInstances(index, models_from_index_files);
			}
		} finally {
			istream.close();
		}
		return index;
	}

/**
	Extracts (from properties file) the names of other EXPRESS schemas which are represented by
	a (data dictionary) SdaiModel submitted to the method. Some of these names
	may be aliases of the EXPRESS schema described by the model.
	Also, the model may represent a combination of two or more EXPRESS schemas.
	To select an appropriate statement in the properties file, 'key' (second parameter)
	is provided.
	Each name or a combination of names is put in a separate row
	of the matrix 'property_schemas' declared in SdaiModel.
*/
	private static void extractSchemas(Properties props, String key, SdaiModel mod) throws SdaiException {
		int i;
		char sym;
		int first_ind = 0;
		String value = props.getProperty(key);
		if (value == null || value.length() < 1) {
			return;
		}
		int ln = value.length();
		int sch_count = 0;
		for (i = 0; i < ln; i++) {
			sym = (char)value.charAt(i);
			if (sym == PhFileReader.SEMICOLON_b) {
				if (i > first_ind) {
					sch_count++;
				}
				first_ind = i + 1;
			}
		}
		if (sch_count <= 0) {
			return;
		}
		((SdaiModelDictionaryImpl)mod).property_schemas = new String[sch_count][];
		int count = 0;
		int comp_count = 1;
		first_ind = 0;
		if (commas == null) {
			commas = new int[NUMBER_OF_COMMAS];
		}
		for (i = 0; i < ln; i++) {
			sym = (char)value.charAt(i);
			if (sym == PhFileReader.SEMICOLON_b) {
				if (first_ind >= i) {
					first_ind = i + 1;
					continue;
				}
				((SdaiModelDictionaryImpl)mod).property_schemas[count] = new String[comp_count];
				String [] line = ((SdaiModelDictionaryImpl)mod).property_schemas[count];
				if (comp_count == 1) {
					line[0] = value.substring(first_ind, i);
				} else {
					int start_sym_ind = first_ind;
					int end_sym_ind;
					for (int j = 0; j < comp_count; j++) {
						if (j == comp_count - 1) {
							end_sym_ind = i;
						} else {
							end_sym_ind = commas[j];
						}
						line[j] = value.substring(start_sym_ind, end_sym_ind);
						start_sym_ind = end_sym_ind + 1;
					}
				}
				count++;
				first_ind = i + 1;
				comp_count = 1;
			} else if (sym == PhFileReader.COMMA_b) {
				if (comp_count >= commas.length) {
					commas = ensureIntsCapacity(commas);
				}
				commas[comp_count - 1] = i;
				comp_count++;
			}
		}
	}


/**
	Returns the name of the repository specified by the name (parameter 'dir') of
	its subdirectory in the special repositories directory.
	The subdirectory name may be in the new style (for example, "r37") or in the old
	one (for example, "my_repo"). In the former case the repository name is read from
	the corresponding binary file.
	In the latter case, if found, the subdirectory is renamed
	to conform the rules of the new style. The new name is also fixed in the repositories
	properties file.
	In the case of failure (for example, needed binary file does not exist) method
	returns 'null'.
	This method is invoked in the constructor of this class.
*/
	private String extractRepositoryName(String dir, String [] subdirs, String [] files,
			File repos_dir) throws SdaiException {
		int i;
		String rep_name;
		boolean number = true;
		if (dir.startsWith("r")) {
			char [] dir_char = dir.toCharArray();
			if (dir_char.length < 2 || dir_char[1] < '1' || dir_char[1] > '9') {
				number = false;
			} else {
				for (i = 2; i < dir_char.length; i++) {
					if (dir_char[i] < '0' || dir_char[i] > '9') {
						number = false;
						break;
					}
				}
			}
		} else {
			number = false;
		}
		String key_found = null;
		if (number) {
			String rep_file = null;
			int r_count = Integer.parseInt(dir.substring(1));
			for (i = 0; i < files.length; i++) {
				if (files[i].equals("header")) {
					rep_file = "header";
				} else if (files[i].equals("repository")) {
					rep_file = "repository";
				}
			}
			if (rep_file == null) {
				return null;
			}
			File handle = new File(repos_dir, rep_file);
			byte bt;
			int integ;
			String str;
			try {
				FileInputStream file = new FileInputStream(handle);
				DataInput stream = new DataInputStream(file);
				if (rep_file.equals("repository")) {
					try {
						bt = stream.readByte();
						integ = stream.readInt();
						integ = stream.readShort();
						integ = stream.readShort();
						integ = stream.readShort();
						bt = stream.readByte();
						integ = stream.readShort();
						for (i = 0; i < integ; i++) {
							str = stream.readUTF();
						}
						bt = stream.readByte();
						rep_name = stream.readUTF();
					} finally {
						((DataInputStream)stream).close();
						file.close();
					}
				} else {
					try {
						bt = stream.readByte();
						integ = stream.readInt();
						bt = stream.readByte();
						integ = stream.readInt();
						for (i = 0; i < integ; i++) {
							str = stream.readUTF();
						}
						str = stream.readUTF();
						bt = stream.readByte();
						rep_name = stream.readUTF();
					} finally {
						((DataInputStream)stream).close();
						file.close();
					}
				}
			} catch (IOException ex) {
				String base = line_separator + AdditionalMessages.BF_ERR;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (repo_count < r_count) {
				repo_count = r_count;
			}
		} else {
			if (repoProps.containsValue(dir)) {
				for (Enumeration enumer = repoProps.propertyNames(); enumer.hasMoreElements();) {
					String key = (String)enumer.nextElement();
					String value = repoProps.getProperty(key);
					if (value.equals(dir)) {
						boolean key_is_subdir = false;
						for (i = 0; i < subdirs.length; i++) {
							if (subdirs[i].equals(APPLICATIONS_DIR_NAME)) {
								continue;
							}
							if (subdirs[i].equals(key)) {
								key_is_subdir = true;
								break;
							}
						}
						if (!key_is_subdir) {
							key_found = key;
							break;
						} else {
							throw new SdaiException(SdaiException.RP_DUP);
						}
					}
				}
			}
			rep_name = dir;
			if (key_found != null) {
				dir = key_found;
			} else {
				repo_count++;
				dir = "r" + repo_count;
			}
			File new_repos_dir = new File(repos_path, dir);
			if (!repos_dir.renameTo(new_repos_dir)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			updated_dir_name = dir;
		}
		if (key_found == null) {
			repoProps.setProperty(dir, rep_name);
			repo_properties_changed = true;
		}
		return rep_name;
	}


/**
	Checks all repository descriptions in the repositories properties list.
	All statements describing repositories which do not belong longer to the set
	'known_servers' are deleted.
	The value of the key "r_max" in the properties list is updated.
*/
	private void checkProperties(int init_repo_count) throws SdaiException {
		int i;
		int del_props_count = 0;
		int new_repo_count = 0;
		int max_repo_index = -1;
		if (repo_count > init_repo_count) {
			new_repo_count = repo_count;
		}
		for (Enumeration enumer = repoProps.propertyNames(); enumer.hasMoreElements();) {
			String key = (String)enumer.nextElement();
			if (key.equals("r_max")) {
				continue;
			}
			String value = repoProps.getProperty(key);
			boolean found = false;
			for (i = 0; i < known_servers.myLength; i++) {
				SdaiRepository repo = (SdaiRepository)known_servers.myData[i];
				if (repo.name.equals(value)) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (del_props == null) {
					del_props = new String[NUMBER_OF_DEL_PROPERTIES];
				} else if (del_props_count >= del_props.length) {
					del_props = ensureStringsCapacity(del_props);
				}
				del_props[del_props_count] = key;
				del_props_count++;
			} else if (new_repo_count == 0) {
				int index = Integer.parseInt(key.substring(1));
				if (index > max_repo_index) {
					max_repo_index = index;
				}
			}
		}
		for (i = 0; i < del_props_count; i++) {
			repo_properties_changed = true;
			repoProps.remove(del_props[i]);
		}
		if (new_repo_count == 0 && max_repo_index > 0) {
			new_repo_count = max_repo_index;
		}
		if (new_repo_count != init_repo_count) {
			repoProps.setProperty("r_max", Integer.toString(new_repo_count));
			repo_count = new_repo_count;
		}
	}


/**
	Saves updated properties to "sdairepos.properties" file in the special
	repositories directory.
	Method is used in 'addToProperties' method and the constructor of this class
	and 'removeFromProperties' method in SdaiRepository.
*/
	static private void updateSdaireposProperties() throws SdaiException {
		File f = new File(repos_path + File.separator + "sdairepos.properties");
		try {
			OutputStream ostr = new BufferedOutputStream(new FileOutputStream(f));
			repoProps.store(ostr, "JSDAI repository directory");
			ostr.close();
		} catch (FileNotFoundException e) {
			String base = line_separator + AdditionalMessages.IO_IOFF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (IOException e) {
			String base = line_separator + AdditionalMessages.IO_ERWP;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
 * Method is invoked when session is reopened. In this case SystemRepository from
 * the previous call of openSession method is reused.
 */
	private void updateSystemRepository() throws SdaiException {
		systemRepository.session = session;
	}


/**
 * Returns the serial number of JSDAI.
 * This method is deprecated.
 * @return the serial number.
 */
	public static String getSerialNumber() {
		return "xxx";
	}


/**
 * Returns the name of the person or organization to whom a licence is granted.
 * This method is deprecated.
 * @return the name of the licensee.
 */
	public static String getLicensee() {
		return "xxx";
	}


	/**
	 * Supplies <code>SdaiSession</code> properties. This is an alternative
	 * way to providing properties through <code>jsdai.properties</code> file.
	 * This method should be called before the first <code>SdaiSession</code>
	 * is opened. If non null properties value is provided then
	 * <code>jsdai.properties</code> file is not loaded on session open.
	 * <p>This method is an extension of JSDAI, which is
	 * not a part of the standard.</p>
	 *
	 * @param properties session properties. Refer to <code>jsdai.properties</code>
	 *                   for used properties. At least one property with the name
	 *                   <code>repositories</code> has to be available, which
	 *                   specifies the directory where <code>SdaiRepositories</code>
	 *                   are stored. This parameter can be null, which switched
	 *                   to default behavior of loading <code>jsdai.properties</code> file.
	 * @throws SdaiException SS_OPN is thrown in session was already opened.
	 * @since 4.0.0
	 */
	public static void setSessionProperties(Properties properties) throws SdaiException {
		synchronized (syncObject) {
			if(session != null) {
				throw new SdaiException(SdaiException.SS_OPN);
			} else {
				props = properties;
			}
		}
	}

/**
 * Commences a new <code>SdaiSession</code>.
 * This is the first operation before any other SDAI activity can be started.
 * More than one <code>SdaiSession</code> can be active at the same time.
 * However only the first opened <code>SdaiSession</code> has the access to
 * local <code>SdaiRepositories</code> in the set <code>knownServers</code>.
 * JSDAI 2.2 and later versions allow within the same run of an application
 * to re-open an <code>SdaiSession</code> as many times as it is needed.
 * <p> This method initiates the set <code>knownServers</code> by putting into it
 * "SystemRepository" and all repositories located in the special
 * repositories-directory in the local file system.
 * After execution of the method, "SystemRepository" always contains
 * the following <code>SdaiModel</code>s:
 * <ul><li>"SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA" that contains information
 *    about the basic EXPRESS schema describing the structure of the data dictionary (this
 *    schema is underlying for each model containing dictionary data);
 * <li>"SDAI_MAPPING_SCHEMA_DICTIONARY_DATA" that is created to store the mapping data;
 * <li>"MIXED_COMPLEX_TYPES_DICTIONARY_DATA" that is created to store instances of
 * complex entity data types whose partial entity types are taken from
 * different EXPRESS schemas;</ul>
 * and the following <code>SchemaInstance</code>s:
 * <ul><li>"SDAI_DICTIONARY_SCHEMA_INSTANCE" with which all data dictionary
 * models are associated;
 * <li>"SDAI_MAPPING_SCHEMA_INSTANCE" with which all models
 * with dictionary and mapping data are associated.</ul>
 * Also, an insertion of specific data dictionary <code>SdaiModel</code>s into
 * "SystemRepository" can be managed.
 * The needed models are specified by the property "jsdai.SXxx=some_string" in the
 * configuration file jsdai.properties; here "xxx" is the package name in JSDAI library
 * for which the corresponding data dictionary model will be created, and "some_string"
 * is a two-level list of the names of EXPRESS schemas represented by the package "xxx".
 * The names are written in upper case letters, exactly as in the instance of entity
 * "file_schema" in the header of a Part-21 file. The list looks like
 * AAA;BBB,CCC,DDD;EEE;FFF,GGG;
 * <br> The top level of this list defines groups of schemas. Each group is covered by
 * classes and interfaces of the package "xxx". A group may contain more than one schema.
 * Their names in "some_string" are separated by commas. In the example above, the first
 * group is AAA, the second is BBB,CCC,DDD, and so on. This strategy of alternative
 * definition and grouping is useful in several situations including the following:
 * <ul><li>it is possible to use non-standard extensions to specify extended AP schemas;
 * in this case more than one schema may be given;
 * <li>it is possible to indicate subsets of some existing schema, usually by appending
 * some suffix to its name;
 * <li>one and the same schema may be known by different names.</ul>
 * Also, it is permitted to drop the right-hand string (then it is assumed that
 * some_string="") or to write any text not conforming the rules just defined. In this case
 * a string at the right-hand side of the property statement is not used, and the
 * schema represented by the data dictionary model created has the name xxx (which is
 * taken from the left-hand side of the statement). The desribed mechanism allows to
 * relate Part-21 files with libraries in JSDAI. The names representing some group
 * in "some_string", for example, BBB,CCC,DDD can be listed in the header of a Part-21 file.
 * This will mean that instances contained in this file are from the package "xxx".
 * <p> Thus, for each JSDAI package specified using "jsdai.SXxx" property a dictionary
 * model in "SystemRepository" is created. However, an explicit specification of
 * dictionary models relevant for an application through jsdai.properties file
 * is not mandatory. When needed, such models are created automatically.
 * For example, when applying importClearTextEncoding method the exchange structure
 * being imported may contain the name of the EXPRESS schema (in the header entity
 * "file_schema", see "ISO 10303-21::9.2.3") for which no model in "SystemRepository"
 * currently exists. In this case the needed model is created during execution of
 * this method.
 * <br><pre>  Examples of property statements:
 *     jsdai.SAp203val=CCDCLGWIS;CONFIG_CONTROL_DESIGN,GEOMETRIC_VALIDATION_PROPERTIES_MIM;
 *     jsdai.SAutomotive_design=AUTOMOTIVE_DESIGN;AUTOMOTIVE_DESIGN_CC1;AUTOMOTIVE_DESIGN_CC2;
 *     jsdai.STopology_schema=anything</pre>
 * <br><pre>  Examples of FILE_SCHEMA instances:
 *     FILE_SCHEMA=(('CCDCLGWIS'));
 *     FILE_SCHEMA=(('CONFIG_CONTROL_DESIGN {099}','GEOMETRIC_VALIDATION_PROPERTIES_MIM{111}"));
 *     FILE_SCHEMA=(('AUTOMOTIVE_DESIGN_CC1 {1 2 10303 214-1 1 5 3}'));
 *     FILE_SCHEMA=(('TOPOLOGY_SCHEMA'));</pre>
 * <p> Only two models within "SystemRepository"
 * are automatically made accessible: "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA" and
 * "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA". To start (read-only) access of any other
 * data dictionary model, a transaction has to be started.
 * <p> "SystemRepository" is opened automatically and only close session operation
 * can close that repository.
 * <p>Before opening <code>SdaiSession</code> JSDAI configuration information is searched.
 * Configuration can be provided by invoking <code>setSessionProperties</code> method
 * prior to first call to <code>openSession</code> or otherwise the property file
 * <code>jsdai.properties</code> is searched in the following order:</p>
 * <ol>
 * <li>If there is a system property <code>jsdai.properties</code> it is used as a
 * directory where file <code>jsdai.properties</code> is searched. If system
 * property is not given or file <code>jsdai.properties</code> does not exist in this
 * directory then the next step is considered</li>
 * <li>User's current working directory (as returned by system property
 * <code>user.dir</code>) is searched for file <code>jsdai.properties</code>.
 * If file <code>jsdai.properties</code> does not exist in this directory then
 * the next step is considered</li>
 * <li>User's home directory (as returned by system property <code>user.home</code>)
 * is searched for file <code>jsdai.properties</code>. If file
 * <code>jsdai.properties</code> does not exist in this directory then the next
 * step is considered</li>
 * <li>Path of extension directory or directories (as returned by system property
 * <code>java.ext.dirs</code>) is searched for file <code>jsdai.properties</code>.
 * If <code>jsdai.properties</code> does not exist in this path then
 * <code>SdaiException.SY_ERR</code> is thrown.</li>
 * </ol>
 * @return the <code>SdaiSession</code> opened.
 * @throws SdaiException SS_OPN, session open.
 * @throws SdaiException SS_NAVL, session not available.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #closeSession
 * @see "ISO 10303-22::10.3.1 OpenSession"
 * @see #setSessionProperties
 */
	public static SdaiSession openSession() throws SdaiException {
//  several sessions
//		if (session != null) {
//			throw new SdaiException(SdaiException.SS_OPN, session);
//		}
		synchronized (syncObject) {
		SdaiSession ss = new SdaiSession();
/*String time = "2000-06-25T06:04:02";
long lo = cal.timeStampToLong(time);*/
/*System.out.println("  from string to long: " + time + "   lo: " + lo);
String new_time = cal.longToTimeStamp(lo);
System.out.println("  from long to string: " + lo + "   string: " + new_time);*/
/*byte [] bt1 = new byte[4];bt1[0]=(byte)'2';bt1[1]=(byte)'1';bt1[2]=(byte)'2';bt1[3]=(byte)'B';
Binary bin1 = new Binary(bt1, 4);
int vvv;
for (int i = 0; i < bin1.value.length; i++) {
vvv = bin1.value[i];System.out.println("  i = " + i + "   byte value = " + vvv);
}
String str1 = bin1.toString();
System.out.println("  string for binary: " + bin1);
byte [] bt1new = new byte [5];
int cccc = bin1.toByteArray(bt1new);
for (int i = 0; i < cccc; i++) {
System.out.println("  i = " + i + "   byte value of bt1new = " + (char)bt1new[i]);
}
System.out.println("  size: " + bin1.getSize());
Binary bin1new = new Binary(bt1new, 3);
String str1new = bin1new.toString();
System.out.println(" NEW string for binary: " + str1new);
byte [] bt2 = new byte[4];bt2[0]=(byte)'0';bt2[1]=(byte)'9';bt2[2]=(byte)'2';bt2[3]=(byte)'A';
Binary bin2 = new Binary(bt2, 4);
for (int i = 0; i < bin2.value.length; i++) {
vvv = bin2.value[i];System.out.println("  i = " + i + "   byte value = " + vvv);
}
String str2 = bin2.toString();
System.out.println("  string for binary: " + str2);
byte [] bt2new = new byte [5];
bin2.toByteArray(bt2new);
Binary bin2new = new Binary(bt2new, 4);
String str2new = bin2new.toString();
System.out.println(" NEW string for binary: " + str2new);*/
/*long time1 = System.currentTimeMillis();
System.out.println("  SdaiSession   time1 = " + time1);
SdaiCalendar cal = new SdaiCalendar();
cal.setTimeLong(time1);
long time2 = cal.getTimeLong();
String sss = ss.get_time_stamp(cal);
System.out.println("  SdaiSession   time2 = " + time2 + "    sss: " + sss);
time2 += 120000;cal.setTimeLong(time2);
sss = ss.get_time_stamp(cal);
System.out.println("  SdaiSession Again  time2 = " + time2 + "    sss: " + sss);*/
/*String s1 = ss.convertRepoName("9KO*&()g");
System.out.println("  SdaiSession   s1: " + s1);
String s2 = ss.convertRepoName("@#$%^&*()");
System.out.println("  SdaiSession   s2: " + s2);
String s3 = ss.convertRepoName("***a***cc");
System.out.println("  SdaiSession   s3: " + s3);
String s4 = ss.convertRepoName("77777s");
System.out.println("  SdaiSession   s4: " + s4);
String s5 = ss.convertRepoName("s77777");
System.out.println("  SdaiSession   s5: " + s5);*/

/*String test = "\u041D\u0435\u0442";
System.out.println("  SdaiSession   test: " + test);
String tt = "\u0041\u0042\u0043";
System.out.println("  SdaiSession   tt: " + tt);*/
/*char [] ch = new char[3];
ch[0] = (char)1034;
ch[1] = (char)1077;
ch[2] = (char)1090;
ss.cyrillic = new String(ch);*/
//ss.storeApplicationProperties(CEntity.class, props, "Application SdaiEdit");
/*Properties propo = ss.loadApplicationProperties(CEntity.class);
String lgg = propo.getProperty("log");
System.out.println("  SdaiSession   log property: " + lgg);*/
/*for (int i = 0; i < ss.known_servers.myLength; i++) {
SdaiRepository rep = (SdaiRepository)ss.known_servers.myData[i];
System.out.println("   SdaiSession   +++++  repository: " + rep.name +
"  dir_name: " + rep.dir_name);
}*/
/*String str = "\AAAA";
System.out.println("   SdaiSession   1  str: " + str);
str = "\";
System.out.println("   SdaiSession   2  str: " + str);
str = "\\";
System.out.println("   SdaiSession   3  str: " + str);
str = "\@";
System.out.println("   SdaiSession   4  str: " + str);
str = "\^";
System.out.println("   SdaiSession   5  str: " + str);
str = "\?";
System.out.println("   SdaiSession   6  str: " + str);
str = "\&";
System.out.println("   SdaiSession   7  str: " + str);
str = "\#";
System.out.println("   SdaiSession   8  str: " + str);
str = "\$";
System.out.println("   SdaiSession   9  str: " + str);
str = "\*";
System.out.println("   SdaiSession   10  str: " + str);
str = "\!";
System.out.println("   SdaiSession   11  str: " + str);*/
/*System.out.println("logWriter: " + logWriter +
"   message written: " + "in openSession()");
logWriter.println("in openSession()");
boolean bool = logWriter.checkError();
System.out.println("----------- bool: " + bool);*/
/* Binary bin_inst = Binary.BitsToBinary("0100101011");
System.out.println("SdaiSession   ----------- bin_inst: " + bin_inst);
String str = bin_inst.toString();
System.out.println("SdaiSession   ----------- bin_inst as string: " + str);*/

		if (ss.printer == null) {
			ss.printer = new Print_instance(null);
		}

//		ss.systemRepository.exportClearTextEncoding("sys_rep.pf");

		return ss;
		} // syncObject
	}


/**
 * Terminates this <code>SdaiSession</code>.
 * After execution of this method all JSDAI objects become not available.
 * Particularly, all open repositories are closed.
 * This method also deletes all temporary repositories.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #openSession
 * @see "ISO 10303-22::10.4.4 Close session"
 */
	public void closeSession() throws SdaiException {
		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
//reader.entity_values.check_references_ComplexEntityValue();
//System.out.println(" SdaiSession    cnt: " + cnt);
		if (active_transaction != null &&
				active_transaction.mode != SdaiTransaction.NO_ACCESS) {
//			active_transaction.endTransactionAccessAbort();
			active_transaction.owning_session = null;
			active_transaction = null;
		}
		int number_of_active_rep = active_servers.myLength;
		session_closing = true;
		SdaiRepository rep;
		while (number_of_active_rep > 0) {
			rep = (SdaiRepository)active_servers.myData[0];
			if (rep == systemRepository) {
				active_servers.removeUnorderedRO(rep);
				if (isMainSession()) {
					rep.active = false;
				}
			} else if (rep != null) {
				rep.closeRepository();
			}
			number_of_active_rep--;
		}
		if (bridgeSession != null) {
			bridgeSession.abort();
			bridgeSession.close();
			bridgeSession = null;
		}
		int number_of_servers = known_servers.myLength;
		int deleted = 0;
		for (int i = 0; i < number_of_servers; i++) {
			rep = (SdaiRepository)known_servers.myData[i - deleted];
			if (rep.temporary) {
				rep.deleteRepository();
				deleted++;
				continue;
			}
			if (rep == systemRepository) {
				continue;
			}
			for (int j = 0; j < rep.models.myLength; j++) {
				SdaiModel model = (SdaiModel)rep.models.myData[j];
				model.repository = null;
			}
			rep.models = null;
			rep.schemas = null;
			rep.session = null;
		}
		synchronized(logWriterSync) {
			if (logWriter != null && isMainSession() && closeLogWriter) {
				logWriter.close();
				logWriter = null;
				closeLogWriter = false;
			}
		}
		logWriterSession = null;
		sdai_implementation.session = null;
		if (isMainSession()) {
			releaseRepositoriesPath(repos_path_lock);
			repos_path_lock = null;
			StaticFields staticFields = StaticFields.get();
			staticFields.for_instances_sorting = null;
			staticFields.for_entities_sorting = null;
			session = null;
			SdaiClassLoaderProvider.dispose();
//			systemRepository = null;
		}
		enableLogging(false);
		if (UR_file != null) {
			UR_file.delete();
		}
		for (Iterator i = jarFileURLStreamHandlers.values().iterator(); i.hasNext();) {
			JarFileURLStreamHandler handler = (JarFileURLStreamHandler) i.next();
			try {
				handler.getJarFile().close();
			} catch (IOException e) {
				throw (SdaiException)new SdaiException(SdaiException.SY_ERR, e.getMessage()).initCause(e);
			}
		}
		opened = false;
		} // syncObject
	}


/**
 * Specifies the beginning of a sequence of read-write operations
 * in this <code>SdaiSession</code>.
 * @return the read-write <code>SdaiTransaction</code>.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_EXS, transaction exists.
 * @see #startTransactionReadOnlyAccess
 * @see SdaiTransaction#endTransactionAccessCommit
 * @see SdaiTransaction#endTransactionAccessAbort
 * @see "ISO 10303-22::10.4.6 Start transaction read-write access,
 * 7.4.5 sdai_transaction, and 13.1.1 Levels of transaction"
 */
	public SdaiTransaction startTransactionReadWriteAccess() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (active_transaction != null) {
			throw new SdaiException(SdaiException.TR_EXS, active_transaction);
		}
		active_transaction = new SdaiTransaction(SdaiTransaction.READ_WRITE, this);
/*		if (bridge == null) {
			Context initial = new InitialContext();
			   Object objref = initial.lookup(CONTEXT_NAME);
			}
			String op_res = bridge.startReadWriteTransaction();
			if (op_res != null) {
				throw new SdaiException(SdaiException.SY_ERR, op_res);
			}*/
		return active_transaction;
//		} // syncObject
	}


/**
 * Specifies the beginning of a sequence of read-only operations
 * in this <code>SdaiSession</code>.
 * @return the read-only <code>SdaiTransaction</code>.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_EXS, transaction exists.
 * @see #startTransactionReadWriteAccess
 * @see SdaiTransaction#endTransactionAccessCommit
 * @see SdaiTransaction#endTransactionAccessAbort
 * @see "ISO 10303-22::10.4.7 Start transaction read-only access,
 * 7.4.5 sdai_transaction, and 13.1.1 Levels of transaction"
 */
	public SdaiTransaction startTransactionReadOnlyAccess() throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (active_transaction != null) {
			throw new SdaiException(SdaiException.TR_EXS, active_transaction);
		}
		active_transaction = new SdaiTransaction(SdaiTransaction.READ_ONLY, this);
/*		if (bridge == null) {
			Context initial = new InitialContext();
			   Object objref = initial.lookup(CONTEXT_NAME);
			}
			String op_res = bridge.startReadOnlyTransaction();
			if (op_res != null) {
				throw new SdaiException(SdaiException.SY_ERR, op_res);
			}*/
		return active_transaction;
//		} // syncObject
	}


/**
 * Finds the entity definition for a complex entity data type with two or more
 * leaf entity data types.
 * For such entities at least one "+" in their name can be observed.
 * <p> The aggregate passed to the first method's parameter shall include
 * all leaf entity data types required to uniquely identify the complex
 * entity and may optionally include some supertypes of those
 * leaf entity data types.
 * <p> This method is not implemented in JSDAI 2.0.
 * <code>SdaiException</code> FN_NAVL will be thrown if this method is invoked.
 * @param types an aggregate containing leaf entity data types and
 * possibly some of their supertypes.
 * @param schema the EXPRESS schema where entity data types are defined or declared.
 * @return the entity definition for a complex entity data type.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #getComplexEntityClass
 * @see "ISO 10303-22::10.9.1 Get complex entity definition"
 */
	public EEntity_definition getComplexEntityDefinition(EEntity_definition types[], ESchema_definition schema)
			throws SdaiException {
		if (types == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
/*		ASdaiModel mods = data_dictionary.associated_models;
		SdaiModel model = null;
		for (int i = 0; i < mods.myLength; i++) {
			SdaiModel m = (SdaiModel)mods.myData[i];
			if (m.described_schema == schema) {
				model = m;
				break;
			}
		}
		if (model == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		if (entity_defs == null) {
			entity_defs = new CEntity_definition[DEFS_ARRAY_SIZE];
		}
		int n_defs = 0;
		boolean null_found = false;
		for (int i = 0; i < type.length; i++) {
			if (type[i] == null) {
				n_defs = i;
				null_found = true;
				break;
			}
			if (i >= defs.length) {
				ensureDefsCapacity();
			}
			defs[i] = (CEntity_definition)type[i];
		}
		if (!null_found) {
			n_defs = type.length;
		}
		return getInstancesInternal2(n_defs);*/
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * Finds the entity class for a complex entity data type with two or more
 * leaf entity data types.
 * In the names of such classes at least one "$" can be observed.
 * <p> The aggregate passed to the first method's parameter shall include
 * entity interfaces for all leaf entity data types required to uniquely identify
 * the complex entity and may optionally include entity interfaces for some
 * supertypes of those leaf entity data types.
 * <p> This method is not implemented in JSDAI 2.0.
 * <code>SdaiException</code> FN_NAVL will be thrown if this method is invoked.
 * @param types an aggregate containing entity interfaces for leaf entity data types
 * and possibly some of their supertypes.
 * @param schema the EXPRESS schema where entity data types are defined or declared.
 * @return the entity class for a complex entity data type.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #getComplexEntityDefinition
 * @see "ISO 10303-22::10.9.1 Get complex entity definition"
 */
	public Class getComplexEntityClass(Class[] types, Class schema)
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * Given schema name, returns definition of the Express schema with this name.
 * If schema with submitted name does not exist,
 * then <code>null</code> is returned.
 * @param name name of the schema.
 * @return definition of the schema with the specified name.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SchemaInstance#getDefiningSchema
 */
	public ESchema_definition findSchemaDefinition(String name) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		SchemaInstance sch_inst = systemRepository.findSchemaInstance(name);
		if (sch_inst == null) {
			return null;
		}
		ESchema_definition schema = null;
		try {
			schema = sch_inst.getDefiningSchema();
		} catch (SdaiException ex) {
			if (ex.getErrorId() == SdaiException.FN_NAVL) {
				schema = null;
			} else {
				throw ex;
			}
		}
		return schema;
//		} // syncObject
	}


/**
 * <P>Links an already existing <code>SdaiRepository</code> outside
 * of this <code>SdaiSession</code> (external) to <code>knownServers</code>
 * (but not to <code>activeServers</code> what means that the repository
 * returned is closed).
 * Such a repository exists in the special repositories-directory or
 * in the specified directory as <a href="../../../guides/SDAIFile.html">SDAI file</a>
 * or it is a remote repository on a JSDAI-Server.
 * This method also can be used to promote a degenerated repository
 * (see class <code>SdaiRepository</code>) to the normal one.
 * If method is applied by passing to its parameters the name and, possibly,
 * the location of some repository which already exists in the current
 * session, then it simply finds this repository in <code>knownServers</code>
 * and returns it.
 * <P>If null value or empty string is passed to the method's first
 * parameter for the local repository, then <code>SdaiException</code> VA_NSET is thrown.
 * If null value or empty string is passed to the method's first
 * parameter for repository stored in <a href="../../../guides/SDAIFile.html">SDAI file</a>, then repository name is derived from location.
 * If repository with the specified or derived name already exists and name parameter
 * was not null, then repository with modified name (repository name plus sequence number)
 * is linked. If name parameter was null, then <code>SdaiException</code> LC_NVLD is thrown.
 * <P> Remote location string format is as follows:<br>
 * //username:password@hostname:port or //username:password
 * <br> Here the port number is optional.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiSession s = ...;
 *    SdaiRepository rep;
 *    rep = s.linkRepository("MyRepo0", null);
 *    rep = s.linkRepository("MyRepo1", "c:\\myrepos\\MyRepo.sdai");
 *    rep = s.linkRepository("", "c:\\MyRepo2.sdai");
 *    rep = s.linkRepository("MyRepo2", "//guest:passwd@192.123.22.11:1050");
 *    rep = s.linkRepository("MyRepo3", "//username:password@server.mydomain.com");
 *    rep = s.linkRepository("//testrep.server.lksoft.de", "//username:password@"); ?
 *    rep = s.linkRepository("//server.lksoft.de/MyRepo5", "//username:password@"); ?
 *    rep = s.linkRepository("//username:password@server.lksoft.de/MyRepo5", null);
 *    rep = s.linkRepository("//username:password@testrep.server.lksoft.de", null);</pre></TT>
 * @param name the name of the external repository to be linked.
 * @param location a place where to find the external repository
 * and, if needed, an access information like user-name and password.
 * @return the <code>SdaiRepository</code> with the given name at the given location,
 * either local or remote.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_DUP, repository duplicate.
 * @throws SdaiException LC_NVLD, location is invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @see SdaiRepository#unlinkRepository
 */
    public SdaiRepository linkRepository(String name, String location) throws SdaiException {
        SdaiRepository repository = linkRepositoryFast( name, location);
        if (repository == null) {
//for (int i = 0; i < known_servers.myLength; i++) {
//SdaiRepository existingRepository = (SdaiRepository)known_servers.myData[i];
//System.out.println("SdaiSession   repo: " + existingRepository.name);}
            throw new SdaiException(SdaiException.RP_NEXS);
        }
        else {
            return repository;
		}
	}


	SdaiRepository linkRepositoryFast(String name, String location) throws SdaiException {
//System.out.println("Begining name="+name);
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (active_transaction == null
			|| active_transaction.mode == SdaiTransaction.NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}


		SdaiRepository repository;
		File fileSource = null;
		if (location != null) {
			fileSource = new SdaiRepositoryZipImpl.FileForZipFile(location);
		}
		if (fileSource != null && fileSource.isFile()) {
			// Deriving repository name from location
			boolean bNullName = false;
			if (name == null || name.length() == 0) {
				if (name == null) {
					bNullName = true;
				}
				try {
					ZipFile zipFile = new ZipFile(fileSource);
					try {
						ZipEntry zipEntry = zipFile.getEntry("repository");
						if (zipEntry != null) {
							InputStream inputStream =
								new BufferedInputStream(zipFile.getInputStream( zipEntry));
							DataInputStream dataInputStream = new DataInputStream(inputStream);
							try {
								name = SdaiRepositoryLocalImpl.getRepositoryNameFromStream(dataInputStream);
							} catch (SdaiException ex) {
								String base = SdaiSession.line_separator + "Could not retrieve repository name from specified file '"+location+"'.";
								throw (SdaiException)new SdaiException(SdaiException.SY_ERR, base).initCause(ex);
							} finally {
								dataInputStream.close();
							}
						}
						else {
							throw new SdaiException(SdaiException.RP_NEXS, "Specified file '"+location+"' does not contain repository");
						}
					} finally {
						zipFile.close();
					}
				} catch (IOException ex) {
					String base = SdaiSession.line_separator + "Could not read from specified file '"+location+"'.";
					throw (SdaiException)new SdaiException(SdaiException.SY_ERR, base).initCause(ex);
				}

				if (name == null || name.length() == 0) {
					int lastIndex = location.lastIndexOf(File.separatorChar);
					if (lastIndex > -1) {
						name = location.substring(lastIndex + 1);
					}
					else {
						name = location;
					}

					lastIndex = name.lastIndexOf('.');
					if (lastIndex > 0) {
						name = name.substring(0, lastIndex);
					}
				}
			}
			// Adding sequence numbers for repeating repository names
			boolean bFreeNameFound = false;
			boolean bNameExists;
			int current = -1;
			String current_name;
			while (!bFreeNameFound) {
				current++;

				if (current > 0) {
					current_name = name + "_" +String.valueOf(current);
				}
				else {
					current_name = name;
				}
				bNameExists = false;
				for (int i = 0; i < known_servers.myLength; i++) {
					SdaiRepository existingRepository = (SdaiRepository)known_servers.myData[i];
					String existingLocation = (String)existingRepository.location;
					if (current == 0 && location.equals(existingLocation)) {
						throw new SdaiException(SdaiException.RP_DUP, existingRepository, AdditionalMessages.AX_NAME + name + " location: " + location);
					}
					if (existingRepository.name.equals(current_name)) {
						bNameExists = true;
						break;
					}
				}
				if (!bNameExists) {
					bFreeNameFound = true;
					name = current_name;
				}
				else {
					if (bNullName) {
						throw new SdaiException(SdaiException.RP_DUP, AdditionalMessages.AX_NAME + name);
					}
				}
			}

			repository = new SdaiRepositoryZipImpl(this, name, location, false);
			repository.modified = false;
			repository.dir_name = location;
			known_servers.addUnorderedRO(repository);
			if (current > 0) {
				((SdaiRepositoryZipImpl)repository).nameWithRecurrenceNumber = true;
			}
		}
		else {
			if (name == null || name.length() == 0) {
				throw new SdaiException(SdaiException.VA_NSET);
			}

			String modifiedName = null;
			if (!onServer&&name.startsWith(LOCATION_PREFIX)) {
				modifiedName = getRepoName(name);
	//			System.out.println("Modified location="+modifiedName);
				if(!modifiedName.startsWith(LOCATION_PREFIX)) {
					if(!name.startsWith(LOCATION_PREFIX)) {
						name = LOCATION_PREFIX + name.substring(name.indexOf("@")+1);
					} else {
						name = name.substring(name.indexOf("@")+1);
					}
				} else {
					name = modifiedName;
				}
	//			System.out.println("Taken repo name="+name);
			} else {
				modifiedName = name;
			}

			for (int i = 0; i < known_servers.myLength; i++) {
				repository = (SdaiRepository)known_servers.myData[i];
				if (location == null) {
					if (repository.name.equals(name)) {
						if (repository.virtual) {
							repository.promoteVirtualToOrdinary(null, true);
						}
						return repository;
					}
				} else {
					if (repository.name.equals(name)) {
						if (repository.virtual) {
							repository.promoteVirtualToOrdinary(location, false);
							return repository;
						}
						if (!(repository.location instanceof String)) {
							throw new SdaiException(SdaiException.RP_DUP, repository,
								AdditionalMessages.AX_NAME + name);
						}
						if (((String)repository.location).equals(location)) {
							return repository;
						}
						if(repository.getRepoRemote() != null) {
							return repository;
						}
	//System.out.println("  SdaiSession   name: " + name  +
	//"   location: " + location +
	//"   repository.location: " + repository.location);
						throw new SdaiException(SdaiException.RP_DUP, repository, SdaiSession.line_separator +
							AdditionalMessages.AX_NAME + name + SdaiSession.line_separator +
							AdditionalMessages.SS_LOCF + repository.location + SdaiSession.line_separator +
							AdditionalMessages.SS_LOCS + location);
					}
				}
			}
			if (location == null) {
				return null;
			}
	//System.out.println("!Location="+location);
			repository = createSdaiRepositoryImpl(name, location, false);
			if (location.startsWith(LOCATION_PREFIX) || name.startsWith(LOCATION_PREFIX)) {
	//-----------Donatas changes
			   if (!oldConnectionProtocol) {
				try {
				   if(bridgeSession == null) {
	//				   bridge = (SdaiBridgeRemote)Class.forName("jsdai.rmiclient.RMIBridgeClient").newInstance();
	//				   SdaiBridgeHome home = (SdaiBridgeHome)PortableRemoteObject.narrow(objref, SdaiBridgeHome.class);
	//				   bridge = home.create();

	//				 ((jsdai.rmiclient.RMIBridgeClient)bridge).initUser(getUser(location), getPassword(location));

					}
	//System.out.println("Modified name="+modifiedName);
					repository.setRepoRemote(bridgeSession.linkRepository(modifiedName));
					} catch (Exception e) {
						throw new SdaiException(SdaiException.SY_ERR, e);
					}
			   } else {
					repository.loadHeaderRemote();
			   }
			}
			repository.modified = false;
			known_servers.addUnorderedRO(repository);
		}
		return repository;
//		} // syncObject
	}



/**
 * <P>Creates a new repository and adds it to <code>knownServers</code> (but not
 * to <code>activeServers</code> what means that the repository returned
 * is closed).
 * Such a repository is created either in the special repositories-directory
 * or in the specified directory as <a href="../../../guides/SDAIFile.html">SDAI file</a>
 * or as a remote repository on a JSDAI-Server.<br>
 * <UL><LI>The first case happens if null is passed to the method's second
 * parameter ("location").
 * Passing null value to the method's first parameter results in
 * <code>SdaiException</code> VA_NSET. If the value for this parameter is
 * an empty string "", then JSDAI creates a temporary repository with some
 * default name. Each temporary repository is automatically deleted
 * during execution of {@link #closeSession closeSession} or
 * {@link #openSession openSession} methods. The latter is possible if
 * the last close session operation has ended abnormally or an application
 * did not perform this operation.
 * The value passed to the first parameter, provided nonempty, must obey
 * the general rules imposed on the construction of valid repository names (see
 * class <code>SdaiRepository</code>). If these rules are violated, then
 * <code>SdaiException</code> VA_NVLD is thrown.</LI>
 * <LI>The second case happens if path of <a href="../../../guides/SDAIFile.html">SDAI file</a> is passed to the method's second
 * parameter ("location").
 * If the value for the first parameter is null or an empty string "", then JSDAI
 * creates a repository with name derived from <a href="../../../guides/SDAIFile.html">SDAI file</a> name.
 * If repository with the specified or derived name already exists, then repository
 * with modified name (repository name plus sequence number) is created.
 * <a href="../../../guides/SDAIFile.html">SDAI file</a> is used to store all repository data (repository, models and external data)
 * in one file.</LI>
 * <LI>The third case happens if remote location string is passed to the method's second
 * parameter ("location"). Remote location string format is as follows:<br>
 * //username:password@hostname:port or //username:password<br>
 * Here the port number is optional.</LI></UL>
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiSession s = ...;
 *    SdaiRepository repo;
 *    // create local repositories
 *    repo = s.createRepository("MyRepo1", null);
 *    repo = s.createRepository("", null);
 *
 *    // create repositories stored in SDAI files
 *    repo = s.createRepository("MyRepo2", "c:\\myrepos\\MyRepo.sdai");
 *    repo = s.createRepository("", "c:\\MyRepo2.sdai");
 *
 *    // create remote repositories on JSDAI-Server
 *    repo = s.createRepository("MyRepo3", "//guest:passwd@192.123.22.11:1050");
 *    repo = s.createRepository("MyRepo4", "//username:password@server.mydomain.com");
 *    repo = s.createRepository("//testrep.server.lksoft.de", "//username:password@");
 *    repo = s.createRepository("//server.lksoft.de/MyRepo6", "//username:password@");</pre></TT>
 * @param repository_name the name of the newly created repository.
 * @param location a location where to place the new repository.
 * @return the <code>SdaiRepository</code> with the given name at the given location,
 * either local or remote.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException RP_DUP, repository duplicate.
 * @throws SdaiException LC_NVLD, location is invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #convertRepoName
 */
	public SdaiRepository createRepository(String repository_name, Object location)
			throws SdaiException {
//		synchronized (syncObject) {
		int i;
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		startProcessTransactionReadWrite();

		if (repository_name!=null&&!onServer&&repository_name.startsWith(LOCATION_PREFIX)) {
			location = takeLocation(repository_name, (String)location);
		}
		boolean temp = false;

		String loc = (String)location;
		SdaiRepository repository;
		String newRepositoryFormat = props.getProperty("new.repository.format", "DIR");


		if ( ((loc == null && newRepositoryFormat.equalsIgnoreCase("SDAI")) && (repository_name == null || repository_name.length() != 0))
			|| (loc != null && (!loc.endsWith(DIRECTORY_SUFFIX) && !loc.endsWith(File.separator))) ) {
			// Deriving repository name from location
			if (repository_name == null || repository_name.length() == 0) {
				if (loc != null) {
					int lastIndex = loc.lastIndexOf(File.separatorChar);
					if (lastIndex > -1) {
						repository_name = loc.substring(lastIndex + 1);
					}
					else {
						repository_name = loc;
					}

					lastIndex = repository_name.lastIndexOf('.');
					if (lastIndex > 0) {
						repository_name = repository_name.substring(0, lastIndex);
					}
				}
				else {
					throw new SdaiException(SdaiException.LC_NVLD, "The location: " + loc);
				}
			}
			if (loc == null) {
				loc = repository_name+SDAI_FILE_EXTENSION;
			}
			// Adding repos_path to the start of location if file name without path is specified
			if (loc.lastIndexOf(File.separator) < 0) {
				loc = repos_path + File.separator + loc;
			}
			// Adding sequence numbers for repeating repository names
			boolean bFreeNameFound = false;
			boolean bNameExists;
			int current = -1;
			String current_name;
			while (!bFreeNameFound) {
				current++;

				if (current > 0) {
					current_name = repository_name + "_" +String.valueOf(current);
				}
				else {
					current_name = repository_name;
				}
				bNameExists = false;
				for (i = 0; i < known_servers.myLength; i++) {
					SdaiRepository existingRepository = (SdaiRepository)known_servers.myData[i];
					String existingLocation = (String)existingRepository.location;
					if (current == 0 && loc.equals(existingLocation)) {
						throw new SdaiException(SdaiException.RP_DUP, existingRepository, AdditionalMessages.AX_NAME + repository_name + " location: " + loc);
					}
					if (existingRepository.name.equals(current_name)) {
						bNameExists = true;
						break;
					}
				}
				if (!bNameExists) {
					bFreeNameFound = true;
					repository_name = current_name;
				}
			}

			repository = new SdaiRepositoryZipImpl(this, repository_name, loc, false);
			repository.temporary = false;
			known_servers.addUnorderedRO(repository);
			repository.created_or_imported = true;
			repository.physical_file_name = loc;
			repository.description = new A_string(listTypeSpecial, repository);
			repository.implementation_level = "1";
			repository.dir_name = loc;

			FILE_NAME file_name = new FILE_NAME(repository);
			file_name.name = repository_name;
			long time = System.currentTimeMillis();
			file_name.time_stamp = cal.longToTimeStamp(time);
			file_name.preprocessor_version = " ";
			file_name.originating_system = " ";
			file_name.authorization = " ";
			repository.file_name = file_name;
			if (current > 0) {
				((SdaiRepositoryZipImpl)repository).nameWithRecurrenceNumber = true;
			}

			repository.file_schema = new FILE_SCHEMA(repository);
			boolean successful = false;
			try {
				((SdaiRepositoryZipImpl)repository).preCommitting();
				((SdaiRepositoryZipImpl)repository).saveRepositoryLocal();
				((SdaiRepositoryZipImpl)repository).postCommitting();
				successful = true;
			}
			catch (Exception e) {
				throw new SdaiException(SdaiException.SY_ERR, e);
			}
			finally {
				((SdaiRepositoryZipImpl)repository).postCommittingRelease(successful);
			}
		}
		else {
			if (loc != null && loc.length() == 0) {
				loc = null;
			}
			if (repository_name == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
				if (repository_name.length() == 0) {
					temp = true;
//					boolean found = true;
//					while (found) {
					synchronized(countForRepsSync) {
						count_for_repositories++;
						repository_name = DEF_REP_NAME + count_for_repositories;
					}
//						found = false;
//						for (i = 0; i < session.known_servers.myLength; i++) {
//							SdaiRepository rep = (SdaiRepository)session.known_servers.myData[i];
//							if (rep.name.equals(repository_name)) {
//								found = true;
//								break;
//							}
//						}
//					}
			} else if ((!repository_name.startsWith(LOCATION_PREFIX)) && (!checkRepoName(repository_name))) {
				throw new SdaiException(SdaiException.VA_NVLD);
			}

			repository = createSdaiRepositoryImpl(repository_name, loc, true);
			repository.temporary = temp;
			known_servers.addUnorderedRO(repository);
			repository.created_or_imported = true;
			repository.physical_file_name = repository_name;

			repository.description = new A_string(listTypeSpecial, repository);
			repository.implementation_level = "1";

			FILE_NAME file_name = new FILE_NAME(repository);
			file_name.name = repository_name;
			long time = System.currentTimeMillis();
			file_name.time_stamp = cal.longToTimeStamp(time);
			file_name.preprocessor_version = " ";
			file_name.originating_system = " ";
			file_name.authorization = " ";
			repository.file_name = file_name;

			repository.file_schema = new FILE_SCHEMA(repository);

			if (repository_name.startsWith(SdaiSession.LOCATION_PREFIX) ||
			(loc != null && loc.startsWith(LOCATION_PREFIX))) {
				if (oldConnectionProtocol) {
					repository.remote_created = true;
					Socket sock_temp;
					DataInput istr;
					DataOutput ostr;
					try {
						repository.sock = new Socket(repository.addr, repository.port);
						repository.is = repository.sock.getInputStream();
						repository.os = repository.sock.getOutputStream();
						istr = new DataInputStream(repository.is);
						ostr = new DataOutputStream(repository.os);
						ostr.writeUTF(repository.user);
						if(istr.readByte()==0) {
							String base = line_separator + AdditionalMessages.SS_PWD;
							throw new SdaiException(SdaiException.LC_NVLD, base);
						}
						String v_Repo_Name = Implementation.build+":"+repository.name; // Sending build version together
						ostr.writeUTF(v_Repo_Name);             	      					// with repo name, for backward
						ostr.writeByte('n');															// compatibility
						int result = istr.readByte();
						if(result == 0) {
							throw new SdaiException(SdaiException.RP_DUP, AdditionalMessages.RP_DUP);
						}
						if(result == -1) {
							throw new SdaiException(SdaiException.SS_RO);
						}
					} catch (java.net.UnknownHostException ex) {
						String base = line_separator + AdditionalMessages.NE_UNH;
						throw new SdaiException(SdaiException.LC_NVLD, base);
					} catch (IOException ex) {
						String base = line_separator + AdditionalMessages.NE_IOEX;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					repository.saveRepositoryRemote(istr, ostr);
					try {
						ostr.writeByte('c');
						repository.sock.close();
						((DataInputStream)istr).close();
						((DataOutputStream)ostr).close();
					} catch (IOException ex) {
						String base = line_separator + AdditionalMessages.NE_IOEX;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				} else {
					//FIXME: Location part in the name handling to ensure uniform
					//       remote repository naming. Location part is removed. To enable
					//       multibridge functionality this handling has to be enhanced. V.N.
					if(repository_name.charAt(2) != '/') {
						repository_name = "///" +
							repository_name.substring(repository_name.indexOf('/', 2) + 1);
					}
					try {
	//				if (bridge == null) {
	//						bridge = (SdaiBridgeRemote)Class.forName("jsdai.rmiclient.RMIBridgeClient").newInstance();
	//					}
						bridgeSession.registerCreatedRepository(repository);
						//Moved to SessionRemote.commitStart V.N.
//                         repository.setRepoRemote(bridgeSession.createRepository(repository_name));
//                         repository.commitCreatedRemote();
					} catch (Exception e) {
						throw new SdaiException(SdaiException.SY_ERR, e);
					}
				}
			} else {
				((SdaiRepositoryLocalImpl)repository).saveRepositoryLocal();
	//			if (!temp) {
				addToProperties(repository, false);
	//			}
			}
		}

		endProcessTransactionReadWrite();

		return repository;
//		} // syncObject
	}


/**
	Adds an information about a repository (its name and directory name)
	to "sdairepos.properties" file in the special repositories directory.
	Method is used in createRepository and importClearTextEncoding methods in
	this class and commit in SdaiTransaction. This method is not thread safe
	and has to be synchronized from outside througth syncObject.
*/

	static void addToProperties(SdaiRepository repository, boolean checkExistance) throws SdaiException {
		synchronized(repoPropsSync) {
			if (!checkExistance || !repoProps.containsKey(repository.dir_name)) {
				if (repository.dir_name == null || repository.name == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				repoProps.setProperty(repository.dir_name, repository.name);
//System.out.println("  SdaiSession !!!!!!!!!!!!!!!!!!!!!!   repo_count: " + repo_count +
//"   repository.dir_name: " + repository.dir_name +
//"   repository.name " + repository.name);
				repoProps.setProperty("r_max", Integer.toString(repo_count));
//		 		repoProps.setProperty("r_max", Integer.toString(session.repo_count));
				updateSdaireposProperties();
			}
		}
	}

	static void removeFromProperties(SdaiRepository repository) throws SdaiException {
		synchronized(repoPropsSync) {
			repoProps.remove(repository.dir_name);
			updateSdaireposProperties();
		}
	}

	static int incRepoCount() {
		synchronized(repoPropsSync) {
			repo_count++;
			return repo_count;
		}
	}

/** Creates a new repository and imports an exchange structure
 * (part21 file) into it.
 * Such a repository is created either in the special repositories-directory
 * or in the specified directory as <a href="../../../guides/SDAIFile.html">SDAI file</a>
 * or as a remote repository on a JSDAI-Server.<br>
 * <UL><LI>The first case happens if null is passed to the method's third
 * parameter ("destination_location").</LI>
 * <LI>The second case happens if path of <a href="../../../guides/SDAIFile.html">SDAI file</a>
 * is passed to the method's third parameter ("destination_location").</LI>
 * <LI>The third case happens if remote location string is passed to the method's third
 * parameter ("destination_location"). Remote location string format is as follows:<br>
 * //username:password@hostname:port or //username:password<br>
 * Here the port number is optional.</LI>
 * </UL>
 * The repository is added to both <code>knownServers</code>
 * and <code>activeServers</code>, thus it is open after execution of this method.
 * <p> There are the following possibilities to choose the name of the repository:
 * <OL>
 * <LI>The name is some nonempty string (passed to the first method's parameter).
 * This string must obey the general rules imposed on the construction of valid
 * repository names (see class <code>SdaiRepository</code>).
 * If these rules are violated, then
 * <code>SdaiException</code> VA_NVLD is thrown.</LI>
 * <LI>An empty string is passed for the name. In this case JSDAI creates a temporary repository
 * with some default name. Each temporary repository is automatically deleted
 * during execution of {@link #closeSession closeSession} or
 * {@link #openSession openSession} methods. The latter is possible if
 * the last close session operation has ended abnormally or an application
 * did not perform this operation.</LI>
 * <LI>The name parameter is null. In this case the name of the
 * repository is taken from the entity <code>file_name</code> in the header
 * of an exchange structure from which the encoding is imported. The string
 * extracted from this entity, if needed, is converted to a string allowed
 * to be a valid name of the repository (see class <code>SdaiRepository</code>).
 * All characters before the first letter are deleted, and all illegal characters
 * are replaced by underscores. If the resulting string is empty, then
 * some default string as a repository name is used.</LI>
 * </OL>
 * <p>Before creating a new repository this method tries to find a degenerated repository
 * with the same name (see class <code>SdaiRepository</code>) and, if an attempt is
 * successfull, promotes it to the normal one.
 * <p> For each data section in the exchange structure a separate <code>SdaiModel</code>
 * in the repository is created. If the exchange structure contains only one data section,
 * then the corresponding model gets name "default". Otherwise, the name of each
 * model is taken from the parameters of the data section from which this model
 * is constructed. Upon termination of the method each model contains all entity
 * instances whose encoding is found in the corresponding data section.
 * <p> This method automatically creates all needed data dictionary models, if any.
 * Read-only access to these models is automatically started.
 * <p> It should be noted that the method imports exchange structures in which
 * all or some names of entities are presented in a short form.
 * For example, any of the following cases for entity <code>cartesian_point</code> is apt:
 * <ul><li> #1016=CARTESIAN_POINT('cp5',(0.0,0.0,-0.25));
 * <li> #1016=CRTPNT('cp5',(0.0,0.0,-0.25));
 * </ul>
 * To identify an entity instance correctly from its short name, the latter
 * must be defined in the data dictionary (provided by an Express compiler).
 * <p> When importing part21 file it may happen that some complex instance in it is of
 * entity type that is not defined or declared in the underlying schema. In this case
 * method itself tries to replace this instance with an instance of known entity type
 * that most fits to the missing type. To attributes of the substitute, which are common
 * to those of the missing entity type, values taken from the part21 file are assigned.
 * Attributes (if any) of the created instance, which are not defined in the missing entity type,
 * are left unset. A user about this replacement operation is informed by the
 * warning message issued.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiSession s = ...;
 *    SdaiRepository repo;
 *    repo = s.importClearTextEncoding(null,"myfile1.stp", null);
 *    repo = s.importClearTextEncoding("","myfile2.stp", null);
 *    repo = s.importClearTextEncoding("MyRepo3","myfile3.stp", null);
 *    repo = s.importClearTextEncoding("MyRepo4","myfile4.stp", "c:\\myrepos");
 *
 *    // Stored in SDAI files
 *    repo = s.createRepository(null, "myfile5.pf", "MyRepo.sdai");
 *    repo = s.createRepository("MyRepo6", "myfile6.pf", "c:\\myrepos\\MyRepo.sdai");
 *
 *    // On remote server side:
 *    repo = s.importClearTextEncoding("MyRepo7", "myfile7.pf", "//guest:passwd@192.123.22.11:1050");
 *    repo = s.importClearTextEncoding("MyRepo8", "myfile8.pf", "//user:secret@server.mydomain.com");
 *    repo = s.importClearTextEncoding("//MyRepo9.server.mydomain.com", "myfile9.pf", "//username:password@");
 *    repo = s.importClearTextEncoding("//server.mydomain.com/MyRepo10", "myfile10.pf", "//username:password@");</pre></TT>
 * <p> During import of an exchange structure (a part21 file) some checking and validation of the
 * data contained in it is performed. This activity can be roughly classified as follows:
 * <OL>
 * <LI> Detecting and reporting scanner errors. The error messages are:
 * <ul><li> "Only sign is specified."
 * <li> "Integer number is too large to fit into long."
 * <li> "Real literal without cyphers in an exponent."
 * <li> "Unterminated string."
 * <li> "Unterminated binary."
 * <li> "Unterminated enumeration item."
 * <li> "Incorrect enumeration item."
 * <li> "Invalid user-defined entity name."
 * <li> "Zero instance identifier."
 * <li> "Incorrect instance identifier."
 * <li> "Unmatched close of a comment."
 * <li> "Comment not terminated."
 * <li> "Lowercase is not allowed."
 * <li> "Unexpected character."
 * <li> "Unmatched input."
 * <li> "Bad input stream initializer."
 * </ul>
 * </LI>
 * <LI> Detecting and reporting parser errors. The error messages are:
 * <ul><li> "Unexpected header entity.". It is thrown when some entity instance not allowed to appear
 * in the header of the exchange structure is found.
 * <li> "Value is incompatible with attribute type.". It is thrown when submitted values for attributes
 * (which are all standard) of entity instances in the header are wrong. Also, in the case when
 * at least one of the values after "DATA" keyword is incorrect (these values must be present
 * when an exchange structure contains more than one data section).
 * <li> "Insufficient number of values." This message appears for the header entities and
 * "DATA" keyword similarly as "Value is incompatible with attribute type.".
 * <li> "List contains improper item.". This message appears for the header entities and
 * "DATA" keyword similarly as "Value is incompatible with attribute type.".
 * <li> "List cannot be empty.". This message appears for the header entities similarly
 * as "Value is incompatible with attribute type.".
 * <li> "No express schema is provided.". It is thrown when an instance of the entity "file_schema"
 * in the header does not provide the name of the express schema underlying for an exchange structure.
 * <li> "Value in header is redefined or is entity instance identifier."
 * <li> "Schema name in data section is inconsistent with the header.". The message appears when
 * schema name specified for data section is missing in the list given by
 * "file_schema" entity instance in the header.
 * <li> "Parameter list in data section contains too many values.". This means that the
 * number of values after "DATA" keyword is greater than two.
 * <li> "Invalid token.". This message appears when a token incompatible with the grammar of
 * the exchange structure is found. For example, "ENDSEC" is expected but the token considered is
 * different.
 * <li> "Entity name or right parenthesis is expected.". This means that in a data section after
 * instance identifier and equality sign it should be either entity name (internal mapping case)
 * or right parenthesis (external mapping case).
 * <li> "Error: comma or right parenthesis expected.". The message may appear while processing
 * an embedded list.
 * </ul>
 * </LI>
 * <LI> Checking the contents of the exchange structure.
 * When reporting about errors, in addition to a message also some other information is
 * printed: exchange structure, instance, entity, attribute, section name.
 * The messages can be divided into the following groups:
 * <OL>
 * <LI> Reading header:
 * <ul><li> "Invalid section name in header's entities.". The message informs that section name
 * in an instance of either entity 'section_language' or 'section_context' is wrong.
 * <li> "Error: value for 'preprocessor_version' in FILE_NAME is missing, set to default."
 * <li> "Error: value for 'originating_system' in FILE_NAME is missing, set to default."
 * <li> "Error: value for 'authorization' in FILE_NAME is missing, set to default."
 * <li> "Error: wrong section name in an instance of 'file_population'."
 * <li> "Error: wrong comment in an instance of 'file_population'."
 * <li> "Error: wrong or missing value of logical type in an instance of 'file_population'."
 * <li> "Error: wrong or missing digit in an instance of 'file_population'."
 * <li> "Error: wrong aggregate in an instance of 'file_population'."
 * </ul>
 * </LI>
 * <LI> Reading instances:
 * <ul><li> "Duplicate instance identifier."
 * <li> "Warning: user defined entity was encountered, instance was ignored."
 * <li> "Warning: wrong ordering of partial entity values in complex instance."
 * <li> "Error: Error: complex entity not available, please add it to .ce file and recreate your EXPRESS library"
 * <li> "Error: unknown entity data types ". After the message the names of entity types
 * of interest are listed.
 * <li> "Error: entity data type in the schema not found."
 * </ul>
 * </LI>
 * <LI> Reading values of entity instances:
 * <ul><li> "Error: value of integer type expected, replaced by unset value."
 * <li> "Error: value of real type expected, replaced by unset value."
 * <li> "Error: value of string type expected, replaced by null."
 * <li> "Error: value of logical type expected, replaced by unset value."
 * <li> "Error: value of boolean type expected, replaced by unset value."
 * <li> "Error: wrong enumerated value, replaced by unset value."
 * <li> "Error: value of enumeration type expected, replaced by unset value."
 * <li> "Error: value of binary type expected, replaced by null."
 * <li> "Error: reference to entity expected, replaced by null."
 * <li> "Error: value of aggregate type expected, replaced by null."
 * <li> "Error: aggregate as an element of an outer aggregate expected, replaced by null."
 * <li> "Error: wrong type of value, value set to null."
 * <li> "Error: wrong redefinition for integer, replaced by unset value."
 * <li> "Error: wrong redefinition for double, replaced by unset value."
 * <li> "Error: wrong redefinition for string, replaced by null."
 * <li> "Error: wrong redefinition for logical, replaced by unset value."
 * <li> "Error: wrong redefinition for boolean, replaced by unset value."
 * <li> "Error: wrong redefinition for enumeration, replaced by unset value."
 * <li> "Error: wrong redefinition for binary, replaced by null."
 * <li> "Error: wrong redefinition for reference to instance, replaced by null."
 * <li> "Error: wrong redefinition for value of select type, replaced by null."
 * <li> "Error: insufficient number of values, replaced by unset value."
 * <li> "Warning: discrepancies between physical file and express schema."
 * <li> "Warning: unset value not allowed in SET, BAG, LIST (only in ARRAY)."
 * <li> "Error: excessive value in ARRAY, it is ignored."
 * <li> "Warning: reference to missing instance."
 * <li> "Error: reference to an instance of wrong type, replaced by null."
 * <li> "Error: TYPED_PARAMETER expected, value replaced by null."
 * <li> "Warning: TYPED_PARAMETER missing, it was recovered."
 * <li> "Error: number of values differs from the number of attributes."
 * <li> "Error: number of values is less than the number of attributes."
 * <li> "Error: number of values exceeds the number of attributes."
 * </ul>
 * </LI>
 * <LI> Analysing of the strings:
 * <ul><li> "Incorrect string: "
 * <li> "Invalid character in string: "
 * <li> "Warning: control directive for the end of the sequence of characters
 * from ISO 10646 is missing."
 * </ul>
 * </LI>
 * </OL>
 * </LI>
 * </OL>

 * @param name the name of the repository to which the data should be imported.
 * @param source_location the location of the exchange structure, e.g. "c:\a_path\the_file.stp".
 * @param destination_location the location where to place the new repository.
 * @return the <code>SdaiRepository</code> with the data imported from the exchange structure.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException RP_DUP, repository duplicate.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_DUP, SDAI-model duplicate.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #convertRepoName
 * @see "ISO 10303-21: Implementation methods: Clear text encoding of the exchange structure"
 * @see "Technical corrigendum 1 to ISO 10303-21"
 * @see "Ammendment to ISO 10303-21"
 */
	public SdaiRepository importClearTextEncoding(String name,
			Object source_location, Object destination_location) throws SdaiException {
//long time1, time2, time_import = 0, time3, time4;
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		startProcessTransactionReadWrite();

		if (name!=null) {
			if (name.startsWith(LOCATION_PREFIX)) {
				destination_location = takeLocation(name, (String)destination_location);
			} else {
				if (name.length() > 0 && !checkRepoName(name)) {
					throw new SdaiException(SdaiException.VA_NVLD);
				}
			}
		}

        if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < active_servers.myLength; i++) {
				if (active_servers.myData[i] != systemRepository) {
					count++;
				}
			}
			if (count >= 1) {
				String base = line_separator + AdditionalMessages.FL_ORZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, active_transaction);
		}

		SdaiRepository repository;
		try {
			if (reader == null) {
				reader = new PhFileReader();
			}
//			if (!(source_location instanceof String)) {
//				throw new SdaiException(SdaiException.VT_NVLD);
//			}
			repository = reader.exchange_file(this, name, source_location,
				destination_location, data_dictionary);
		} catch (java.io.IOException ex){
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if(destination_location instanceof SdaiRepository) {
			endProcessTransactionReadWrite();
			return repository;
		}
//		repository.description = repository.header.description.description;
        destination_location = repository.location;

		boolean temp = false;
		String for_name = null;
		if (name == null) {
			for_name = repository.file_name.repository_name;
			if (for_name.equals(FILE_NAME.DEFAULT_REPOSITORY_NAME)) {
				count_for_abnormal_repo_names++;
				for_name += count_for_abnormal_repo_names;
			}
		} else if (name.length() == 0) {
			temp = true;
//			boolean found = true;
//			while (found) {
			synchronized(countForRepsSync) {
				count_for_repositories++;
				for_name = DEF_REP_NAME + count_for_repositories;
			}
//				found = false;
//				for (int i = 0; i < session.known_servers.myLength; i++) {
//					SdaiRepository rep = (SdaiRepository)session.known_servers.myData[i];
//					if (rep.name.equals(for_name)) {
//						found = true;
//						break;
//					}
//				}
//			}
		} else {
			for_name = name;
		}
		repository.setNameLocation(for_name, destination_location);
		known_servers.addUnorderedRO(repository);
		active_servers.addUnorderedRO(repository);
		repository.temporary = temp;

        String loc = "SdaiRepository/";
        if (destination_location instanceof String) {
            loc = (String)destination_location;
        }
        String newRepositoryFormat = props.getProperty("new.repository.format", "DIR");

        if ((loc == null && newRepositoryFormat.equalsIgnoreCase("SDAI")) || (loc != null && (!loc.endsWith(DIRECTORY_SUFFIX) && !loc.endsWith(File.separator)))) {
            boolean successful = false;
            try {
                ((SdaiRepositoryZipImpl)repository).preCommitting();
                ((SdaiRepositoryZipImpl)repository).saveRepositoryLocal();
                ((SdaiRepositoryZipImpl)repository).postCommitting();
                successful = true;
            }
            catch (Exception e) {
                throw new SdaiException(SdaiException.SY_ERR, e);
            }
            finally {
                ((SdaiRepositoryZipImpl)repository).postCommittingRelease(successful);
            }
        }
        else {
            if (loc != null && loc.startsWith(LOCATION_PREFIX)) {
                if (oldConnectionProtocol) {
                    try {
                        boolean repeat = true;
                        byte result = 0;
                        repository.sock = new Socket(repository.addr, repository.port);
                        repository.is = repository.sock.getInputStream();
                        repository.os = repository.sock.getOutputStream();
                        repository.istream = new DataInputStream((InputStream)repository.is);
                        repository.ostream = new DataOutputStream((OutputStream)repository.os);
                        repository.ostream.writeUTF("~" + Implementation.build);
                        repository.conThread = repository.istream.readUTF();
                        repository.initServerListener();
                        repository.ostream.writeUTF(repository.user);
                        if(repository.istream.readByte()==0) {
                            String base = line_separator + AdditionalMessages.SS_PWD;
                            throw new SdaiException(SdaiException.LC_NVLD, base);
                        }
                        String v_Repo_Name = Implementation.build+":"+repository.name; // Sending build version together
                        repository.ostream.writeUTF(v_Repo_Name);             	      // with repo name, for backward
                        repository.ostream.writeByte('m');                             // compatibility
                        result = repository.istream.readByte();
                        if(result == 0) {
                            String base = AdditionalMessages.AX_NAME + " "+ repository.name +" "+AdditionalMessages.RP_DUP;
                            throw new SdaiException(SdaiException.RP_DUP, base);
                        }
                        if(result == -1) {
                            throw new SdaiException(SdaiException.SS_RO);
                        }
                    } catch (java.net.UnknownHostException ex) {
                        String base = line_separator + AdditionalMessages.NE_UNH;
                        throw new SdaiException(SdaiException.LC_NVLD, base);
                    } catch (IOException ex) {
                        String base = line_separator + AdditionalMessages.NE_IOEX;
                        throw new SdaiException(SdaiException.LC_NVLD, base);
                    }
                    repository.saveRepositoryRemote();
                } else {
                    try {
                        if (bridgeSession == null) {
                            throw new
                                SdaiException(SdaiException.SY_ERR,
                                              "Can not import into remote repository. Bridge is not linked.");
                        }
                        //System.out.println(" SdaiSession remote repo: " + for_name);
						bridgeSession.registerCreatedRepository(repository);
						//Moved to SessionRemote.commitStart V.N.
//                         repository.setRepoRemote(bridgeSession.createRepository(for_name));
                        // 					repository.commitCreatedRemote();
                        for (int i = 0; i < repository.models.myLength; i++) {
                            SdaiModel mod = (SdaiModel)repository.models.myData[i];
                            mod.modified = true;
                        }
                        for (int i = 0; i < repository.schemas.myLength; i++) {
                            SchemaInstance sch = (SchemaInstance)repository.schemas.myData[i];
                            sch.modified = true;
                        }
                    } catch (Exception e) {
                        throw new SdaiException(SdaiException.SY_ERR, e);
                    }
                }
            } else {
                ((SdaiRepositoryLocalImpl)repository).saveRepositoryLocal();
				addToProperties(repository, false);
            }
        }
//System.out.println("    =====SdaiSession    time_import = " + time_import);
//long diff = time4 - time3;
//System.out.println("    =====SdaiSession    exchange_file = " + diff);
		endProcessTransactionReadWrite();

		return repository;
//		} // syncObject
	}


/**
 * Imports an exchange structure (part21 file) into an existing repository
 * submitted through the second method's parameter.
 * It is not required for it to be empty: the new <code>SdaiModel</code>s and
 * <code>SchemaInstance</code>s are simply added to the existing ones.
 * The new schema instances constitute an aggregate that is returned by the method.
 * In the case when a data section in the exchange structure being imported has the name
 * which is already used by a model in the repository, the name of the new model
 * corresponding to this data section is constructed by appending to it the letter "x",
 * for example, data section "polytope" becomes a <code>SdaiModel</code> with
 * the name "polytopex". An analogous operation is applied
 * to the schema instances found in the exchange structure file.
 * Other functionality of the method is the same as of its main variant
 * {@link #importClearTextEncoding(String, Object, Object) importClearTextEncoding}.
 * @param sourceLocation the location of the exchange structure, e.g. "c:\a_path\the_file.stp".
 * @param targetRepo the repository where to import the data from the exchange structure.
 * @return the <code>ASchemaInstance</code> with the schema instances created or imported
 * from the exchange structure.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-21: Implementation methods: Clear text encoding of the exchange structure"
 * @see "Technical corrigendum 1 to ISO 10303-21"
 * @see "Ammendment to ISO 10303-21"
 * @see #importClearTextEncoding(String, Object, Object)
 */
/*	public void
	importClearTextEncoding(String sourceLocation, SdaiRepository destinationLocation)
	throws SdaiException {
		importClearTextEncoding(null, sourceLocation, destinationLocation);
	}*/

	public ASchemaInstance importClearTextEncoding(String sourceLocation, SdaiRepository targetRepo)
			throws SdaiException {
//		synchronized (syncObject) {
		int i;
		if (targetRepo.models.myLength <= 0) {
			return importClearTextEncoding(null, sourceLocation, targetRepo).schemas;
		} else {
			SdaiRepository temp_repo = importClearTextEncoding("", sourceLocation, null);
			SchemaInstance sch;
			for (i = 0; i < targetRepo.schemas.myLength; i++) {
				sch = (SchemaInstance)targetRepo.schemas.myData[i];
				sch.exists = true;
			}
			Move.moveRepoContents(temp_repo, targetRepo);
			ASchemaInstance res_schemas = new ASchemaInstance(setType0toN, targetRepo);
			for (i = 0; i < targetRepo.schemas.myLength; i++) {
				sch = (SchemaInstance)targetRepo.schemas.myData[i];
				if (sch.exists) {
					sch.exists = false;
				} else {
					res_schemas.addUnorderedRO(sch);
				}
			}
//			temp_repo.deleteRepository();
			return res_schemas;
		}
//		} // syncObject
	}


/**
 * Imports an exchange structure (part21 file) into an existing repository
 * submitted through the second method's parameter.
 * The repository is required to be empty.
 * This method is applicable when the location of the exchange structure file is
 * remote and the data from it are supplied through the <code>InputStream</code>
 * (the first parameter of the method).
 * Other functionality of the method is the same as of its main variant
 * {@link #importClearTextEncoding(String, Object, Object) importClearTextEncoding}.
 * @param sourceLocation the location of the exchange structure.
 * @param destinationLocation the repository where to import the data from the exchange structure.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_DUP, SDAI-model duplicate.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-21: Implementation methods: Clear text encoding of the exchange structure"
 * @see "Technical corrigendum 1 to ISO 10303-21"
 * @see "Ammendment to ISO 10303-21"
 * @see #importClearTextEncoding(String, Object, Object)
 */
	public void
	importClearTextEncoding(InputStream sourceLocation, SdaiRepository destinationLocation)
	throws SdaiException {
		importClearTextEncoding(null, sourceLocation, destinationLocation);
	}


/**
	Prepares the header information for SystemRepository. More precisely,
   some default values are set to attributes of entities 'file_description',
   'file_name' and 'file_schema'. Instances of 'file_population' are
   prepared automatically when SystemRepository is exported to an exchange
   structure. The method is invoked in the constructor of this class.
*/
	void create_header(SdaiRepository rep) throws SdaiException {
		rep.description = new A_string(listTypeSpecial, rep);
		rep.description.myData = "Repository for data dictionary";
		rep.description.myLength = 1;
		rep.implementation_level = "1";
		FILE_NAME file_name = new FILE_NAME(rep);
		file_name.name = rep.name;
		long time = System.currentTimeMillis();
		file_name.time_stamp = cal.longToTimeStamp(time);
		A_string str_auth = file_name.author;
		str_auth.myData = "GP";
		str_auth.myLength = 1;
		A_string str_org = file_name.organization;
		str_org.myData = "LKSoftWare";
		str_org.myLength = 1;
		file_name.preprocessor_version = "no version";
		file_name.originating_system = "created while opening a session";
		file_name.authorization = "missing";
		rep.file_name = file_name;
		rep.file_schema = new FILE_SCHEMA(rep);
	}


// Moved to SdaiRepository and mabe later it will be moved to SdaiModel (V.N.)
///**
//	Creates a repository called 'virtual'. In this repository only
//	the following fields from those defined in Part 22 are set with values:
//	- name;
//	- session;
//	- models.
//	This method is used to create a repository for the repository name
//	found when reading model's binary file provided there is no repository
//	with this name in the set 'known_servers'.
//	The method is invoked in one of variations of extractModel in class
//	SdaiModel.
//*/
//	SdaiRepository createVirtualRepository(String name) throws SdaiException {
//		SdaiRepository repo = new SdaiRepositoryLocalImpl(this, name);
//		known_servers.addUnorderedRO(repo);
//		return repo;
//	}


	SdaiRepository createSdaiRepositoryImpl(String name, String location, boolean fCheckNameLocation)
	throws SdaiException {
		boolean isRepositoryRemote = name.startsWith(SdaiSession.LOCATION_PREFIX) ||
			(location != null && location.startsWith(SdaiSession.LOCATION_PREFIX));
		if(isRepositoryRemote) {
			return bridgeSession.createSdaiRepositoryImpl(this, name, location, fCheckNameLocation);
		} else {
			return new SdaiRepositoryLocalImpl(this, name, location, fCheckNameLocation);
		}
	}


/**
	Returns definition for the Express schema specified by parameter
	of type class. For example, SGeometry_schema.class.
	If data dictionary model corresponding to the required schema has no
	access, then this model is started in read-only mode.
	If needed model is not found in the set 'associated_models' of the
	data dictionary, then a new dictionary model with the standardized
	name including schema's name is created. Again, this model is put in
	the read-only mode.
	If by some reason method fails to create a required data dictionary model,
	then null is returned.
	This method is invoked in class SchemaInstance, public method setNativeSchema
	and in class SdaiRepository, public methods createSdaiModel and
	createSchemaInstance.
*/
	CSchema_definition getSchemaDefinition(Class schema) throws SdaiException {
		ASdaiModel assoc_mods = data_dictionary.associated_models;
if (debug2) System.out.println("  BEFORE getSchemaDefinition()    schema: " + schema.getName());
		for (int i = 0; i < assoc_mods.myLength; i++) {
			SdaiModel model = (SdaiModel)assoc_mods.myData[i];
if (debug2) System.out.println("   model in data_dictionary: " + model.name +
"  mode: " + model.mode);
			if (model.schemaData == null) {
				continue;
			}
if (debug2) System.out.println("   model: " + model.name +
"   super class name: " + model.schemaData.super_inst.getClass().getName());
if (debug2) System.out.println("   schema: " + schema.getName());

			if (model.schemaData.super_inst == null) {
				String sch_name = model.schemaData.schema.getName(null);
				String normalized_sch_name = sch_name.substring(0,1).toUpperCase() + sch_name.substring(1).toLowerCase();
				String f = SCHEMA_PREFIX + normalized_sch_name + ".S" + normalized_sch_name + ".ss";
				printWarningToLogoSdaiContext(line_separator + AdditionalMessages.DI_FUN1 +
					f + AdditionalMessages.DI_FUN2);
				continue;
			}
			if (schema == model.schemaData.super_inst.getClass()) {
//System.out.println("   model for schema returned: " + model.name +
//"   super class name: " + model.schemaData.super_inst.getClass().getName());
				if (model.getMode() == model.NO_ACCESS) {
					model.startReadOnlyAccess();
				}
				return model.described_schema;
			}
		}
		String name = schema.getName();
		String dict_name =
			name.substring(name.lastIndexOf(".") + 2, name.length()).toUpperCase() +
			DICTIONARY_NAME_SUFIX;
if (debug2) System.out.println("   SESSION new model in data_dictionary: " + dict_name);
		SdaiModel new_dict = systemRepository.findDictionarySdaiModel(dict_name);
		new_dict.startReadOnlyAccess();
		if (schema == new_dict.schemaData.super_inst.getClass()) {
			return new_dict.described_schema;
		}
		return null;
	}


	String getIdentification(String schema_name) throws SdaiException {
		ASdaiModel assoc_mods = data_dictionary.associated_models;
		for (int i = 0; i < assoc_mods.myLength; i++) {
			SdaiModel model = (SdaiModel)assoc_mods.myData[i];
			CSchema_definition schema = model.described_schema;
			if (schema == null) {
				continue;
			}
			String sch_name = schema.getName(null).toUpperCase();
			if (sch_name.equals(schema_name)) {
				if (schema.testIdentification(null)) {
					return schema.getIdentification(null);
				} else {
					return null;
				}
			}
		}
		return null;
	}


/*	private SchemaData getSchemaData(Class schema) throws SdaiException {
		ASdaiModel assoc_mods = data_dictionary.associated_models;
		for (int i = 0; i < assoc_mods.myLength; i++) {
			SdaiModel model = (SdaiModel)assoc_mods.myData[i];
			if (model.schemaData == null) {
				continue;
			}
System.out.println("SdaiSession **** model: " + model.name);
if (model.schemaData.super_inst == null) System.out.println("SdaiSession --------- super_inst is NULL");
System.out.println("SdaiSession +++++++++ super_inst is POS");
			if (schema == model.schemaData.super_inst.getClass()) {
				if (model.mode == model.NO_ACCESS) {
					model.startReadOnlyAccess();
				}
				return model.schemaData;
			}
		}
		String name = schema.getName();
		String dict_name =
			name.substring(name.lastIndexOf(".") + 2, name.length()).toUpperCase() +
			DICTIONARY_NAME_SUFIX;
		SdaiModel new_dict = systemRepository.findDictionarySdaiModel(dict_name);
		new_dict.startReadOnlyAccess();
		if (schema == new_dict.schemaData.super_inst.getClass()) {
			return new_dict.schemaData;
		}
		return null;
	}*/
	private SchemaData getSchemaData(Class schema) throws SdaiException {
		String name = schema.getName();
		String dict_name =
			name.substring(name.lastIndexOf(".") + 2, name.length()).toUpperCase();
		if (dict_name.equals("DICTIONARY")) {
			dict_name = "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA";
		} else {
			dict_name += DICTIONARY_NAME_SUFIX;
		}
		ASdaiModel assoc_mods = data_dictionary.associated_models;
		for (int i = 0; i < assoc_mods.myLength; i++) {
			SdaiModel model = (SdaiModel)assoc_mods.myData[i];
			if (model.schemaData == null) {
				continue;
			}
			if (dict_name.equals(model.name)) {
//System.out.println("SdaiSession ***** 'equals' model: " + model.name);
				if (model.schemaData.model.lengths == null) {
					if (model.getMode() == model.NO_ACCESS) {
						model.startReadOnlyAccess();
					} else {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
				return model.schemaData;
			}
		}
		SdaiModel new_dict = systemRepository.findDictionarySdaiModel(dict_name);
		new_dict.startReadOnlyAccess();
		if (new_dict.schemaData.super_inst == null) {
			String sch_name = new_dict.schemaData.schema.getName(null);
			String normalized_sch_name = sch_name.substring(0,1).toUpperCase() + sch_name.substring(1).toLowerCase();
			String f = SCHEMA_PREFIX + normalized_sch_name + ".S" + normalized_sch_name + ".ss";
			String base = line_separator + AdditionalMessages.DI_FUN1 +
				f + AdditionalMessages.DI_FUN2;
			printWarningToLogoSdaiContext(base);
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (schema == new_dict.schemaData.super_inst.getClass()) {
			return new_dict.schemaData;
		}
		return null;
	}


/** Converts a given <code>String</code> to a string that may be
 * used as a repository name.
 * All characters before the first letter are deleted, and all illegal characters
 * are replaced by underscores.
 * This resulting string obeys the general rules imposed on the construction of valid
 * repository names (see class <code>SdaiRepository</code>).
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param name the <code>String</code> asked to convert to valid repository name.
 * @return the <code>String</code> converted to valid repository name.
 * @see #createRepository
 * @see #importClearTextEncoding
 */
	public String convertRepoName(String name) throws SdaiException {
//		synchronized (syncObject) {
		int count = 0;
		int ln = name.length();
		boolean started = false;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.repo_name == null) {
			int sym_count;
			if (ln >= NUMBER_OF_CHARACTERS) {
				sym_count = ln;
			} else {
				sym_count = NUMBER_OF_CHARACTERS;
			}
			staticFields.repo_name = new byte[sym_count];
		} else {
			if (ln > staticFields.repo_name.length) {
				enlarge_repo_name(staticFields, ln);
			}
		}
		for (int i = 0; i < ln; i++) {
			byte s = (byte)name.charAt(i);
			if ('A' <= s && s <= 'Z' || 'a' <= s && s <= 'z') {
				staticFields.repo_name[count] = s;
				count++;
				started = true;
				continue;
			}
			if ('0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE) {
				if (count > 0) {
					staticFields.repo_name[count] = s;
					count++;
				}
			} else if (count > 0) {
				staticFields.repo_name[count] = PhFileReader.UNDERSCORE;
				count++;
			}
		}
		return new String(staticFields.repo_name, 0, count);
//		} // syncObject
	}


/**
	Checks if the String value provided as a repository name obeys
	the general rules imposed on the construction of valid repository names (see
	class <code>SdaiRepository</code>). If so, then value 'true' is returned.
	This method is used in createRepository and importClearTextEncoding.
*/
	private boolean checkRepoName(String name) throws SdaiException {
		byte s = (byte)name.charAt(0);
		if (!('A' <= s && s <= 'Z' || 'a' <= s && s <= 'z')) {
			return false;
		}
		for (int i = 1; i < name.length(); i++) {
			s = (byte)name.charAt(i);
			if (!('A' <= s && s <= 'Z' || 'a' <= s && s <= 'z' ||
					'0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE)) {
				return false;
			}
		}
		return true;
	}


/**
	Returns the index of the string in an alphabetically sorted array
	of strings. A searched string and the array are submitted as parameters.
	It is a general method used in SdaiRepository and SdaiModel classes.
*/
	int find_string(int left, int right, String key, String [] strs)
			throws SdaiException {
		while (left <= right) {
			int middle = (left + right) / 2;
			int comp_res = strs[middle].compareTo(key);
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
	Increases the size of the auxiliary array 'repo_name' twice.
	This method is invoked in convertRepoName.
*/
	private void enlarge_repo_name(StaticFields staticFields, int demand) {
		int new_length = staticFields.repo_name.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_repo_name[] = new byte[new_length];
		staticFields.repo_name = new_repo_name;
	}


/**
	Increases the size of the array of strings submitted as a parameter twice.
	It is a general method used in SdaiSession, SdaiRepository and SdaiModel classes.
*/
	String [] ensureStringsCapacity(String [] strings) {
		int new_length = strings.length * 2;
		String [] new_array = new String[new_length];
		System.arraycopy(strings, 0, new_array, 0, strings.length);
		return new_array;
	}


/**
	Increases the size of the array of integer values submitted as a parameter twice.
	It is a general method used in SdaiSession and SdaiModel classes.
*/
	static int [] ensureIntsCapacity(int [] ints) {
		int new_length = ints.length * 2;
		int [] new_array = new int[new_length];
		System.arraycopy(ints, 0, new_array, 0, ints.length);
		return new_array;
	}


/**
	Creates an instance of SdaiException.
	It is an auxiliary method used in different situations.
*/
	void assertTrue(boolean fAssert, Object base) throws SdaiException {
		if (!fAssert) {
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	This method performs the following operations:
	- finds (or creates new if not found) data dictionary model corresponding
	to an instance of the class SSuper; if the instance of SSuper is
	SMapping, then the model processed further is "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA";
	- creates and relates with the selected model an instance of SchemaData
	provided a selected model does not have such an instance;
	- relates the instance of SSuper to the instance of SchemaData.
	The method is invoked in initSuper in class SSuper.
	The latter method, in turn, is used to assign value to a static final
	field of type SSuper in each class representing an Express schema (such
	classes have the name of the corresponding schema with the prefix 'S',
	for example, "SGeometry_schema").
*/
	static protected SSuper initSuper(SSuper ss) {
		String name = ss.getClass().getName();
		String dict_name;
		if (name.equals("jsdai.mapping.SMapping")) {
			dict_name = "SDAI_MAPPING_SCHEMA_DICTIONARY_DATA";
		} else {
			dict_name =
				name.substring(name.lastIndexOf(".") + 2).toUpperCase() + DICTIONARY_NAME_SUFIX;
		}
		SdaiModel model = null;
		if (systemRepository == null) {
			// init repo
		}
		ASdaiModel mods = systemRepository.models;
		for (int i = 0; i < mods.myLength; i++) {
			SdaiModel m = (SdaiModel)mods.myData[i];
			if (m.name.equals(dict_name)) {
//System.out.println("  model was found in SdaiSession" + "   its mode: " + m.mode);
				model = m;
				break;
			}
		}
		try {
			if (model == null) {
if (debug2) System.out.println("  SdaiSession findDictionarySdaiModel   name: " + dict_name);
				model = systemRepository.findDictionarySdaiModel(dict_name);
				model.schemaData = new SchemaData(model);
			} else if (model.schemaData == null) {
				model.schemaData = new SchemaData(model);
			}
//System.out.println("  SdaiSession initSchema invoked for  model: " + model.name);
//			model.initSchemaShort();
			model.schemaData.super_inst = ss;
//System.out.println("  SdaiSession ss assigned to super_inst for  model: " + model.name);
			ss.model = model;
		} catch (SdaiException e) {
			printStackTraceToLogWriter(e);
		}
//System.out.println("  In: SdaiSession, initSuper()  ss = " + ss);
		return ss;
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	static public EData_type findDataType(String name, Class schema) {
//		synchronized (syncObject) {
/*		if (name.equals("_INTEGER")) {
			CInteger_type int_tp = new CInteger_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				int_tp.setName(null, "_INTEGER");
			} catch (SdaiException ex) {
			}
			int_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return int_tp;
		}
		if (name.equals("_NUMBER")) {
			CNumber_type number_tp = new CNumber_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				number_tp.setName(null, "_NUMBER");
			} catch (SdaiException ex) {
			}
			number_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return number_tp;
		}
		if (name.equals("_REAL")) {
			CReal_type real_tp = new CReal_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				real_tp.setName(null, "_REAL");
			} catch (SdaiException ex) {
			}
			real_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return real_tp;
		}
		if (name.equals("_LOGICAL")) {
			CLogical_type log_tp = new CLogical_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				log_tp.setName(null, "_LOGICAL");
			} catch (SdaiException ex) {
			}
			log_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return log_tp;
		}
		if (name.equals("_BOOLEAN")) {
			CBoolean_type bool_tp = new CBoolean_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				bool_tp.setName(null, "_BOOLEAN");
			} catch (SdaiException ex) {
			}
			bool_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return bool_tp;
		}
		if (name.equals("_STRING")) {
			CString_type str_tp = new CString_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				str_tp.setName(null, "_STRING");
			} catch (SdaiException ex) {
			}
			str_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return str_tp;
		}
		if (name.equals("_BINARY")) {
			CBinary_type bin_tp = new CBinary_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				bin_tp.setName(null, "_BINARY");
			} catch (SdaiException ex) {
			}
			bin_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return bin_tp;
		}
		if (name.equals("_ENTITY")) {
			CData_type entity_tp = new CData_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				entity_tp.setName(null, "_ENTITY");
			} catch (SdaiException ex) {
			}
			entity_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return entity_tp;
		}
		if (name.equals("_GENERIC")) {
			CData_type gen_tp = new CData_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			try {
				gen_tp.setName(null, "_GENERIC");
			} catch (SdaiException ex) {
			}
			gen_tp.owning_model = baseDictionaryModel;
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return gen_tp;
		}
		if (name.equals("_LIST_GENERIC")) {
			CList_type listgen_tp = new CList_type();
baseDictionaryModel.mode = SdaiModel.READ_WRITE;
			listgen_tp.owning_model = baseDictionaryModel;
			try {
System.out.println("  SdaiSession, BEFORE   listgen_tp.setName");
				listgen_tp.setName(null, "_LIST_GENERIC");
System.out.println("  SdaiSession, AFTER   listgen_tp.setName");
			} catch (SdaiException ex) {
ex.printStackTrace();
			}
CEntity ennn = (CEntity)listgen_tp;
System.out.println("  SdaiSession, listgen_tp: " + ennn);
baseDictionaryModel.mode = SdaiModel.READ_ONLY;
			return listgen_tp;
		}*/



		SchemaData sch_data;
		try {
			sch_data = session.getSchemaData(schema);
			if (sch_data == null) {
if (check_data_types) {
System.out.println("SdaiSession   sch_data is null    name: " + name +
"   schema: " + schema.getName());
}
				return null;
			}

if (check_data_types && name.equals("_GENERALARRAY_1PDB_2PDB_GENERIC"))
for (int i = 0; i < sch_data.data_types.length; i++) {
System.out.println(" SchemaData    i = " + i +
"   data_type: " + sch_data.data_types[i].getName(null));
}

			EData_type dt = sch_data.find_data_type(name.toUpperCase());
if (check_data_types && dt == null) {
System.out.println("SdaiSession   dt is null    name: " + name +
"   schema: " + schema.getName());
}
			return dt;
		} catch (SdaiException e) {
			printStackTraceToLogWriter(e);
if (check_data_types) {
System.out.println("SdaiSession   SdaiException is thrown    name: " + name +
"   schema: " + schema.getName());
}
			return null;
		}
//		} // syncObject
	}


/**
	Constructs a string representing the location of a repository in the case
	when binary files of this repository do not belong to the special
	repositories-directory (which is locally in the computer).
	The method is invoked in createRepository and importClearTextEncoding methods.
*/
	String takeLocation(String name, String location) throws SdaiException{
		if(name.startsWith(LOCATION_PREFIX) && (location == null || !location.endsWith("@"))) {
			return name.substring(0, name.indexOf("/", 2));
		} else {
			if(name.indexOf("/",LOCATION_PREFIX_LENGTH) != -1) {
				if(location.endsWith("@")){
					location = location+name.substring(LOCATION_PREFIX_LENGTH, name.indexOf("/",LOCATION_PREFIX_LENGTH));
				} else {
					location = "//"+location+"@"+name.substring(LOCATION_PREFIX_LENGTH, name.indexOf("/",LOCATION_PREFIX_LENGTH));
				}
			} else {
				if(location.endsWith("@")){
					location = location+name.substring(LOCATION_PREFIX_LENGTH);
				} else {
					location = "//"+location+"@"+name.substring(LOCATION_PREFIX_LENGTH);
				}
			}
		}
		return location;
	}


/**
	Extracts the user name and password from the string representing
	the location of this repository.
*/
	String getUserAndPassword(Object location) throws SdaiException {
//		synchronized (syncObject) {
		String locationFull=(String)location;
		if(locationFull.indexOf("@")!=-1) {
			return locationFull.substring(locationFull.indexOf("/")+2, locationFull.indexOf("@"));
		} else {
			return "";
		}
//		} // syncObject
	}

	String getUser(Object location) throws SdaiException {
//		synchronized (syncObject) {
		String locationFull=(String)location;
		if(locationFull.indexOf("@")!=-1) {
			String userAndPass = locationFull.substring(locationFull.indexOf("/")+2, locationFull.indexOf("@"));
			if(userAndPass.indexOf(":")!=-1) {
				return userAndPass.substring(0, userAndPass.indexOf(":"));
			} else return "";
		} else {
			return "";
		}
//		} // syncObject
	}

	String getRepoName(String rName) {
//		synchronized (syncObject) {
		String retName;
		if(rName.indexOf("//") != -1) {
			retName = rName.substring(rName.indexOf("/", 3)+1);
			return retName;
		} else {
			return rName;
		}
//		} // syncObject
	}

	String getPassword(Object location) throws SdaiException {
//		synchronized (syncObject) {
		String locationFull=(String)location;
		if(locationFull.indexOf("@")!=-1) {
			String userAndPass = locationFull.substring(locationFull.indexOf("/")+2, locationFull.indexOf("@"));
			if(userAndPass.indexOf(":")!=-1) {
				return userAndPass.substring(userAndPass.indexOf(":")+1);
			} else return "";
		} else {
			return "";
		}
//		} // syncObject
	}

	String getLocation(String location) throws SdaiException {
		if(location.startsWith(LOCATION_PREFIX)) {
			int locationPrefix = location.indexOf("@", LOCATION_PREFIX_LENGTH);
			if(locationPrefix != -1) {
				int locationPostfix = location.indexOf("/", locationPrefix + 1);
				if(locationPostfix != -1) {
					return location.substring(locationPrefix + 1, locationPostfix);
				}
			}
		}
		return "";
	}


/**
 * Finds a list of repositories on remote server.
 * <p> Returns an A_string of the given name at the given location on remote server.
 * <p>Example:
 * <br>rep = session.remoteRepositories("//guest:passwd@server.lksoft.de")
 * <br>rep = session.remoteRepositories("//guest:passwd@192.123.22.11:1050")
 * <br>rep = session.remoteRepositories("//username:password@server.mydomain.com")
 *
 * @return the A_string
 * @param location where to find the external repository
 *  and if needed access information like user-name and password.
 * <p>Remote location format: //username:password@hostname:port
 * <br>Port number is optional
 * @throws SdaiException SS_NOPN, the session is closed
 * @throws SdaiException The location is invalid
*/
	public A_string remoteRepositories(String location)  throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (!location.startsWith(LOCATION_PREFIX)) {
			throw new SdaiException(SdaiException.LC_NVLD);
		}
		A_string repos = new A_string(setTypeSpecial, null);
		SdaiRepositoryRemote[] retVal = null;
		if(!oldConnectionProtocol) {
			try {
				if(bridgeSession == null) {
					synchronized(sdaiBridgeRemoteFactorySync) {
						if(sdaiBridgeRemoteFactory == null) {
							sdaiBridgeRemoteFactory = SdaiBridgeRemoteFactory.newInstance();
						}
					}
					SdaiBridgeRemote bridge = sdaiBridgeRemoteFactory.newSdaiBridgeRemote();
					bridge.initBridge(LOCATION_PREFIX + getLocation(location));
					bridgeSession = bridge.initUser(getUser(location), getPassword(location));
				}
				retVal = bridgeSession.repositoriesList();
			} catch (Exception e) {
				throw new SdaiException(SdaiException.SY_ERR, e);
			}
			for(int i = 0; i < retVal.length; i++) {
				repos.addUnordered(retVal[i].getRemoteName());
                SdaiRepository repository = createSdaiRepositoryImpl(retVal[i].getRemoteName(), getRepositoriesPath(), false);
                repository.location = retVal[i].getRemoteLocation();
                repository.setRepoRemote(retVal[i]);
		        repository.modified = false;
		        known_servers.addUnorderedRO(repository);
			}
			return repos;
		}
		Socket sock = null;
		DataInput istr = null;
		DataOutput ostr = null;
		String addr = null;
		int port = 16384;
		try {
			if(location.indexOf("@") ==-1) {
				location = LOCATION_PREFIX + DEF_USER_NAME + ":" + DEF_PASSWORD + "@" + location.substring(LOCATION_PREFIX_LENGTH);
			}
			addr = location.substring(location.indexOf("@")+1);
			if(addr.indexOf(":") !=-1) {
				port = Integer.parseInt(addr.substring(addr.indexOf(":")+1));
				addr = addr.substring(0, addr.indexOf(":"));
			}
			boolean repeat = true;
			byte result = 0;
			while(repeat) { // In mutithreading mode if server waiting for listener connection can be refused
				sock = new Socket(addr,port);
				istr = new DataInputStream(sock.getInputStream());
				ostr = new DataOutputStream(sock.getOutputStream());
				ostr.writeUTF(getUserAndPassword(location));
				result = istr.readByte();
				if(result == 100) {
					try {
						Thread.sleep(200); //make some delay to yield time for other connection
					} catch (java.lang.InterruptedException e) {};
					repeat = true;
				} else {
					repeat = false;
				}
			}
			if(result == 0) {
				String base = line_separator + AdditionalMessages.SS_PWD;
				throw new SdaiException(SdaiException.LC_NVLD, base);
			}
			ostr.writeUTF("");
			ostr.writeByte('F');
			int count=istr.readByte();
			for(int i=0; i<count; i++) {
				repos.addUnordered(istr.readUTF());
			}
			ostr.writeByte('c');
		} catch (java.net.UnknownHostException ex) {
			String base = line_separator + AdditionalMessages.NE_UNH;
			throw new SdaiException(SdaiException.LC_NVLD, base);
		} catch (IOException ex) {
			String base = line_separator + AdditionalMessages.NE_IOEX;
			throw new SdaiException(SdaiException.LC_NVLD, base);
		}
		return repos;
//		} // syncObject
	}


	/**
	 * Returns the schema_mappings of AIM schema.
	 *
	 * @param schema The AIM schema to return schema_mappings for
	 * @return <code>ASchema_mapping</code> containing schema_mappings
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public ASchema_mapping getMappings(ESchema_definition schema) throws SdaiException {
//		synchronized (syncObject) {
			ASchema_mapping mapping_schemas = new ASchema_mapping();
			Vector model_names = new Vector();
			String schema_name = schema.getName(null);
			for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
				String name = (String)e.nextElement();
				if (name.length() > 15) {
					if (name.substring(0, 15).equalsIgnoreCase("mapping.schema.")) {
						String tmp_schema_name = name.substring(15);
						String tmp_mapping_name = props.getProperty(name);
						if (schema_name.equals(tmp_schema_name)) {
							model_names.add(tmp_mapping_name);
						}
					}
				}
			}
			SchemaInstance si = getDataMapping();
			ASdaiModel models = si.getAssociatedModels();
			SdaiIterator models_it = models.createIterator();
			while (models_it.next()) {
				SdaiModel mod = models.getCurrentMember(models_it);
				for (int i = 0; i < model_names.size(); i++) {
					String tmp_mapping_name = (String)model_names.elementAt(i);
					if (mod.getName().equals(tmp_mapping_name)) {
						if (mod.getMode() == SdaiModel.NO_ACCESS) {
							mod.startReadOnlyAccess();
						}
						AEntity schemas = mod.getInstances(ESchema_mapping.class);
						mapping_schemas.addByIndex(mapping_schemas.getMemberCount()+1,
												   (ESchema_mapping)schemas.getByIndexEntity(1));
					}
				}
			}
			return mapping_schemas;
//		} // syncObject
	}


/**
 * Returns <code>Properties</code> table for an application specified
 * by a parameter of type <code>Class</code>.
 * If the required file containing a list of application properties does not exist,
 * then an empty <code>Properties</code> table is returned.<br/>
 * Classes of package <code>jsdai.lang</code> are reserved by JSDAI to provide
 * special JSDAI properties. The following special classes as a parameter
 * can be specified:
 * <dl>
 * <dt><code>jsdai.lang.SdaiSession</code> (since JSDAI 4.0.1)</dt>
 * <dd>JSDAI-DB session properties. If the session is not linked to JSDAI-DB (by invoking
 * method <code>linkDataBaseBridge</code>) the empty <code>Properties</code> is returned.
 * Otherwise properties include at minimum properties named
 * <code>group-&lt;name of the group&gt;</code> for each group the JSDAI-DB user which
 * was specified invoking method <code>linkDataBaseBridge</code> belongs. The values of
 * above mentioned properties correspond to Group Properties entered using
 * Web Administration Tool.
 * </dd>
 * </dl>
 * <p>This method is an extension of JSDAI, which is not a part of the standard.</p>
 * @param identification the class specifying an application.
 * @return a table containing application properties.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #storeApplicationProperties
 */
	public Properties loadApplicationProperties(Class identification) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if(identification.getName().startsWith("jsdai.lang.")) {
			if(identification == SdaiSession.class) {
				return bridgeSession != null ? bridgeSession.getProperties() : new Properties();
			} else {
				throw new SdaiException(SdaiException.SY_ERR,
						"Application properties for " + identification.getName() + "are reserved");
			}
		} else {
			File applSubdir = new File(repos_path, APPLICATIONS_DIR_NAME);
			if (!applSubdir.isDirectory()) {
				String base = line_separator + AdditionalMessages.IO_APNF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			Properties applProps = new Properties();
			File f = new File(applSubdir, identification.getName().replace('.', '_') + ".properties");
			if (f.exists()) {
				try {
					InputStream istr = new BufferedInputStream(new FileInputStream(f));
					applProps.load(istr);
					istr.close();
				} catch (FileNotFoundException e) {
					String base = line_separator + AdditionalMessages.IO_IOFF;
					throw new SdaiException(SdaiException.SY_ERR, base);
				} catch (IOException e) {
					String base = line_separator + AdditionalMessages.IO_ERRP;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
			return applProps;
		}
//		} // syncObject
	}


/**
 * Writes the property list of the submitted <code>Properties</code> table
 * to the properties file created for an application specified by a parameter
 * of type <code>Class</code>.<br/>
 * Classes of package <code>jsdai.lang</code> are reserved by JSDAI to store
 * special JSDAI properties.
 * <p> This method is an extension of JSDAI, which is not a part of the standard.
 * @param identification the class specifying an application.
 * @param properties a table containing application properties.
 * @param header a description of the property list.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #loadApplicationProperties
 */
	public void storeApplicationProperties(Class identification, Properties properties, String header)
			throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if(identification.getName().startsWith("jsdai.lang.")) {
			throw new SdaiException(SdaiException.SY_ERR,
					"Application properties for " + identification.getName() + "are reserved");
		} else {
			File applSubdir = new File(repos_path, APPLICATIONS_DIR_NAME);
			boolean return_value = true;
			if (!applSubdir.isDirectory()) {
				return_value = applSubdir.mkdir();
			}
			if (!return_value) {
				String base = line_separator + AdditionalMessages.SS_ND1 +
				applSubdir.getName() + AdditionalMessages.SS_ND2;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			File f = new File(applSubdir, identification.getName().replace('.', '_') + ".properties");
			try {
				OutputStream ostr = new BufferedOutputStream(new FileOutputStream(f));
				properties.store(ostr, header);
				ostr.close();
			} catch (FileNotFoundException e) {
				String base = line_separator + AdditionalMessages.IO_IOFF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} catch (IOException e) {
				String base = line_separator + AdditionalMessages.IO_ERWP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
//		} // syncObject
	}

/* -------------- With serial number related methods -------------------*/

	private int checkSerialNumber(String sNumber) {
		if(sNumber.length() != 12) {
			return 0;
		}
		final int orderLen = STR_SERIAL_LEN * STR_SERIAL_LEN;
		final int dataLen = orderLen * orderLen * STR_SERIAL_LEN;
		final long seed = 23;
		final long DELTA_TIME = 956861860000L;
		final long DATE_PRECISION = 10000; //mili seconds
		int build;
		int valid_time = 0; // 0 means unlimited
		final int VALID_STEP = 2;
		final boolean checkSums = false;
		Random rnd = new Random(seed);
		char carray[] = STR_SERIAL_CHAR.toCharArray();
		for(int i = 0; i < STR_SERIAL_LEN; i++) {
			char c = carray[i % STR_SERIAL_LEN];
			int rndVal = rnd.nextInt(STR_SERIAL_LEN);
			carray[i % STR_SERIAL_LEN] = carray [rndVal];
			carray [rndVal] = c;
		}
		STR_SERIAL = new String(carray);
		sNumber = decTo36((int)swap(myTodec(sNumber.substring(sNumber.length()-2)), myTodec(sNumber.substring(0,2))), 2) + sNumber.substring(2);
		int str_start = 0;
		if(checkSums) {
			str_start = 2;
		}
		String tod = sNumber.substring(str_start, sNumber.length()-2);
		Random dRandom = new Random (myTodec(tod));
		long check = myTodec(sNumber.substring(sNumber.length()-2, sNumber.length()));
		int rndC = dRandom.nextInt(orderLen);
		if(rndC != check) {
			return 0;
		}
		dRandom.setSeed(seed);
		String serial;
		long subOrdr = (long)myTodec(sNumber.substring(0,2)) - STR_SERIAL_LEN;
		String mSubDate = sNumber.substring(2,7);
		mSubDate = decTo36((int)swap(myTodec(mSubDate.substring(mSubDate.length()-2)), myTodec(mSubDate.substring(0,2))), 2) + mSubDate.substring(2);
		long subDate = (long)myTodec(mSubDate);
		int mbuild = (int)myTodec(sNumber.substring(7,9));
		int minfo = (int)myTodec(sNumber.substring(9,10));
		long date;
		dRandom.setSeed(subOrdr);
		date = swap(dRandom.nextInt(dataLen), subDate);
		date =  date * DATE_PRECISION + DELTA_TIME;
		dRandom.setSeed(subDate);
		int rbuild = swap(dRandom.nextInt(orderLen), mbuild);
		long rvalid_days = swap (dRandom.nextInt(STR_SERIAL_LEN), minfo) * VALID_STEP;
		long nextDate =  date + rvalid_days * 1000 * 60 * 60 * 24;
		if((nextDate < (new Date()).getTime())&& rvalid_days != 0) {
			return 2;
		}
		return 1;
  }
	private String decTo36 (long num, int len) {
		Random rnd = new Random(num);
		String nn36 = "";
		int checkSum = 0;
		while(num >= STR_SERIAL_LEN) {
			int modNum = (int)(num % STR_SERIAL_LEN);
			nn36 = STR_SERIAL.charAt(modNum) + nn36;
			num /= STR_SERIAL_LEN;
		}
		int modNum = (int)(num % STR_SERIAL_LEN);
		nn36 = STR_SERIAL.charAt(modNum) + nn36;
		for(int i = nn36.length(); i < len; i++) {
			nn36 = STR_SERIAL.charAt(0) + nn36;
		}
		/*    if(checkSums) {
		  nn36 += str.charAt(rnd.nextInt(STR_SERIAL_LEN));
		} */
		return nn36;
  }

  private int swap(long first, long last) {
	  return  (int)(last ^ first);
  }

  private int unswap(long first, long last) {
	  return  (int) (first < last ? last - first : first - last);
  }


  private long myTodec (String s36) {
	long nn = 0;
	for(int i = 0 ; i < s36.length(); i++){
	  nn += Math.pow((double)STR_SERIAL_LEN, (double)(s36.length() - i - 1)) * (STR_SERIAL.indexOf(s36.charAt(i)));
	}
	return nn;
  }

/* ------------------------------- End with serial number related methods ---------------*/


/**
 * Starts recording changes made to the entity instances in the log for
 * subsequent undo/redo operations.
 * Modifications done on higher levels (<code>SdaiModel</code>, <code>SdaiRepository</code>,
 * <code>SchemaInstance</code>) are not logged.
 * The flow of records is divided into segments called groups.
 * To close a group, method {@link #endUndoGroup()} shall be used.
 * The log file is automatically emptied during execution of
 * {@link SdaiTransaction#commit commit} and {@link SdaiTransaction#abort abort} operations.
 * Recording is stopped by submitting value <code>false</code> to
 * the method. In this case the data are released, and undo/redo operations become
 * disabled.
 * @param enable <code>true</code> to start recording changes to the instances and
 * <code>false</code> to stop recording.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @since 4.0.0
 */
	public void enableLogging(boolean enable) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (enable) {
			if (undo_redo_file != null) {
				return;
			}
			forbid_undo_on_SdaiEvent = true;
			fireSdaiLoggingEvent(SdaiLoggingEvent.ENABLED_LOGGING, null);
			forbid_undo_on_SdaiEvent = false;
			try {
				if (UR_file == null) {
					File applSubdir = new File(repos_path, APPLICATIONS_DIR_NAME);
					if (!applSubdir.isDirectory()) {
						String base = line_separator + AdditionalMessages.IO_APNF;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					String UR_name = "undo_redo_" + System.identityHashCode(this) + ".dat";
					UR_file = new File(applSubdir, UR_name);
					UR_file.deleteOnExit();
				}
//				undo_redo_file = new RandomAccessFile ("undo_redo.dat", "rw" );
				undo_redo_file = new RandomAccessFile (UR_file, "rw" );
				undo_redo_file.writeByte('B');
				pointer_pos = undo_redo_file.getFilePointer();
				undo_redo_file.writeLong(-1);
//				SIZEOF_LONG = (int)(undo_redo_file.getFilePointer() - pointer_pos);
				undo_redo_file.seek(pointer_pos);
				if (undoRedoOldValue == null) {
					undoRedoOldValue = new ComplexEntityValue();
					substituted = new long[4];
				}
				empty_group = true;
				group_start_event = false;
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.EMPTIED_HISTORY, null);
				forbid_undo_on_SdaiEvent = false;
			} catch (IOException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			if (mods_undo == null) {
				mods_undo = new SdaiModel[NUMBER_OF_MODS_UNDO];
			}
			n_mods_undo = 0;
			n_ent_names_undo = 0;
		} else {
			if (undoRedoOldValue != null) {
				undoRedoOldValue.unset_ComplexEntityValue();
			}
			if (undo_redo_file == null) {
				return;
			}
			try {
				undo_redo_file.close();
			} catch (IOException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			undo_redo_file = null;
			undoRedoInstance = null;
			saved_sch_data = null;
			forbid_undo_on_SdaiEvent = true;
			fireSdaiLoggingEvent(SdaiLoggingEvent.DISABLED_LOGGING, null);
			forbid_undo_on_SdaiEvent = false;
		}
//		} // syncObject
	}


	void empty_undo_file() throws SdaiException {
		if (undo_redo_file != null) {
			try {
				undo_redo_file.seek(0);
				undo_redo_file.writeByte('B');
				pointer_pos = undo_redo_file.getFilePointer();
				undo_redo_file.writeLong(-1);
				undo_redo_file.seek(pointer_pos);
				empty_group = true;
				group_start_event = false;
			} catch (IOException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			n_mods_undo = 0;
			n_ent_names_undo = 0;
			undoRedoInstance = null;
			forbid_undo_on_SdaiEvent = true;
			fireSdaiLoggingEvent(SdaiLoggingEvent.EMPTIED_HISTORY, null);
			forbid_undo_on_SdaiEvent = false;
		}
	}


/**
 * Ends recording changes (made to the entity instances) in the current group.
 * The next record automatically will be directed to a new group.
 * The method also generates an object of SDAI logging event notifying
 * the SDAI listeners about closing the current group of records.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #endUndoGroup(Object listenerArgument)
 * @since 4.0.0
 */
	public void endUndoGroup() throws SdaiException {
		endUndoGroup(null);
	}


/**
 * Ends recording changes (made to the entity instances) in the current group.
 * The next record automatically will be directed to a new group.
 * The method also generates an object of SDAI logging event notifying
 * the SDAI listeners about closing the current group of records and
 * supplies this event with an argument carrying user's information,
 * which can be accessed by objects of classes implementing <code>SdaiListener</code> interface.
 * @param listenerArgument an information provided to SDAI listeners receiving SDAI logging event.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #endUndoGroup()
 * @since 4.0.0
 */
	public void endUndoGroup(Object listenerArgument) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (undo_redo_file == null) {
			return;
		}
		try {
			if (undoRedoInstance != null) {
				writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
			}
			undoRedoInstance = null;
			undo_redo_file.writeLong(empty_group ? 0L : pointer_pos);
			undo_redo_file.writeByte('Q');
			long pos = undo_redo_file.getFilePointer();
			undo_redo_file.writeLong(-1);
			undo_redo_file.seek(pos);
//System.out.println(">>>>>>>>>> SdaiSession  pointer_pos: " + pointer_pos + "   pos: " + pos);
			empty_group = true;
			group_start_event = false;
			if (loggingListenrList != null && loggingListenrList.myLength > 0) {
				try {
					forbid_undo_on_SdaiEvent = true;
					fireSdaiLoggingEvent(0, new SdaiLoggingEventEndGroup(this, listenerArgument));
//System.out.println("SdaiSession  Inside endUndoGroup 1  current pos: " + undo_redo_file.getFilePointer()); 
				} finally {
					undo_redo_file.seek(pos);
					forbid_undo_on_SdaiEvent = false;
				}
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
//		} // syncObject
	}


/**
 * Informs whether the undo operation is available for at least one group of recorded changes.
 * That is, the answer is affirmative when the following two conditions are met:
 * <ul><li> method {@link #enableLogging} was applied with parameter set at value <code>true</code>;
 * <li> at least one entity instance has been modified (created, deleted, substituted).
 * </ul>
 * @return <code>true</code> if undo operation is available; <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @since 4.1.0
 */
	public boolean isUndoGroupAvailable() throws SdaiException {
		try {
			if (!opened) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
			if (undo_redo_file == null) {
				return false;
			}
			if (!empty_group) {
				return true;
			}
			long pos = undo_redo_file.getFilePointer();
			undo_redo_file.seek(pos - 1);
			return undo_redo_file.readByte() != 'B';
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}


/**
 * Performs undo operation for a specified number of groups.
 * If this number, submitted through the method's parameter, is negative,
 * then only one (the last) group undergoes undo operation.
 * For each group, the method also generates an object of SDAI logging event notifying
 * the SDAI listeners about completion of undo operation for that group of records.
 * @param groupCount the number of groups (taken from the end of the list)
 * for which the undo operation is asked to be performed.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #undoGroup(int groupCount, Object listenerArgument)
 * @since 4.0.0
 */
	public void undoGroup(int groupCount) throws SdaiException {
		undoGroup(groupCount, null);
	}


/**
 * Performs undo operation for a specified number of groups.
 * If this number, submitted through the method's parameter, is negative,
 * then only one (the last) group undergoes undo operation.
 * For each group, the method also generates an object of SDAI logging event notifying
 * the SDAI listeners about completion of undo operation for that group of records and
 * supplies this event with an argument carrying user's information,
 * which can be accessed by objects of classes implementing <code>SdaiListener</code> interface.
 * @param groupCount the number of groups (taken from the end of the list)
 * for which the undo operation is asked to be performed.
 * @param listenerArgument an information provided to SDAI listeners receiving SDAI logging event.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #undoGroup(int groupCount)
 * @since 4.0.0
 */
	public void undoGroup(int groupCount, Object listenerArgument) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (undo_redo_file == null) {
			return;
		}
		if (groupCount == 0) {
			return;
		}
//System.out.println("  SdaiSession  groupCount: " + groupCount + "   empty_group: " + empty_group);
 		if (groupCount < 0) {
			groupCount = 1;
		}
//		if (!empty_group) {
		if (!empty_group || undoRedoInstance != null) {
			endUndoGroup();
		}
		try {
			for (int i = 0; i < groupCount; i++) {
				long prePos = undo_redo_file.getFilePointer();
//System.out.println("  SdaiSession  Before restore_group_back   i: " + i);
				boolean res = restore_group_back();
//System.out.println("  SdaiSession  After restore_group_back   i: " + i);
				if (!res) {
					return;
				}
				undo_performed = true;
				if (loggingListenrList != null && loggingListenrList.myLength > 0) {
					long postPos = undo_redo_file.getFilePointer();
					try {
						forbid_undo_on_SdaiEvent = true;
						fireSdaiLoggingEvent(0, new SdaiLoggingEventUndo(this, listenerArgument, prePos));
					} finally {
						undo_redo_file.seek(postPos);
						forbid_undo_on_SdaiEvent = false;
					}
				}
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
//		} // syncObject
	}


/**
 * Informs whether the redo operation is available for at least one group of recorded changes.
 * That is, the answer is affirmative when undo operation was executed for
 * at least one group and, after that, no instance modification (deletion, creation)
 * operation was performed.
 * @return <code>true</code> if redo operation is available; <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @since 4.1.0
 */
    public boolean isRedoGroupAvailable() throws SdaiException {
		try {
			if (!opened) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
			if (undo_redo_file == null || !undo_performed) {
				return false;
			}
			long pos = undo_redo_file.getFilePointer();
			long numFlag = undo_redo_file.readLong();
			undo_redo_file.seek(pos);
			return numFlag >= 0;
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}


/**
 * Performs redo operation for a specified number of groups.
 * If this number, submitted through the method's parameter, is negative,
 * then only one (the first) group undergoes redo operation.
 * For each group, the method also generates an object of SDAI logging event notifying
 * the SDAI listeners about completion of redo operation for that group of records.
 * @param groupCount the number of groups (taken from the beginning of the list)
 * for which the redo operation is asked to be performed.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #redoGroup(int groupCount, Object listenerArgument)
 * @since 4.0.0
 */
	public void redoGroup(int groupCount) throws SdaiException {
		redoGroup(groupCount, null);
	}


/**
 * Performs redo operation for a specified number of groups.
 * If this number, submitted through the method's parameter, is negative,
 * then only one (the first) group undergoes redo operation.
 * For each group, the method also generates an object of SDAI logging event notifying
 * the SDAI listeners about completion of redo operation for that group of records and
 * supplies this event with an argument carrying user's information,
 * which can be accessed by objects of classes implementing <code>SdaiListener</code> interface.
 * @param groupCount the number of groups (taken from the beginning of the list)
 * for which the redo operation is asked to be performed.
 * @param listenerArgument an information provided to SDAI listeners receiving SDAI logging event.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see #redoGroup(int groupCount)
 * @since 4.0.0
 */
	public void redoGroup(int groupCount, Object listenerArgument) throws SdaiException {
//		synchronized (syncObject) {
		if (!opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (undo_redo_file == null) {
			return;
		}
		if (groupCount == 0) {
			return;
		}
 		if (groupCount < 0) {
			groupCount = 1;
		}
		try {
			for (int i = 0; i < groupCount; i++) {
				long prePos = undo_redo_file.getFilePointer();
				boolean res = restore_group_forward();
				if (!res) {
					return;
				}
				if (loggingListenrList != null && loggingListenrList.myLength > 0) {
					long postPos = undo_redo_file.getFilePointer();
					try {
						forbid_undo_on_SdaiEvent = true;
						fireSdaiLoggingEvent(0, new SdaiLoggingEventRedo(this, listenerArgument, prePos));
					} finally {
						undo_redo_file.seek(postPos);
						forbid_undo_on_SdaiEvent = false;
					}
				}
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
//		} // syncObject
	}


    boolean restore_group_back() throws java.io.IOException, SdaiException {
		long cur_pos = undo_redo_file.getFilePointer() - 1;
		undo_redo_file.seek(cur_pos);
		byte bt = undo_redo_file.readByte();
		if (bt == 'B') {
			return false;
		}
		cur_pos -= SIZEOF_LONG;
		undo_redo_file.seek(cur_pos);
		long new_pos = undo_redo_file.readLong();
		while (new_pos > 0) {
			cur_pos = new_pos;
			long inst_id, new_inst_id;
			int index2mod, index2new_mod;
			SdaiModel ref_mod, new_ref_mod;
			int pop_index, new_pop_index;
			int inst_index, new_inst_index;
			CEntity inst;
			boolean modif, inst_del;
			undo_redo_file.seek(new_pos);
			new_pos = undo_redo_file.readLong();
			bt = undo_redo_file.readByte();
			switch (bt) {
				case 'c':
				case 'o':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					inst = ref_mod.instances_sim[pop_index][inst_index];
					modif = undo_redo_file.readBoolean();
/*System.out.println("SdaiSession ~~~~~~~~~~~~~ cur_pos: " + cur_pos + "  new_pos: " + new_pos); 
CEntity [] row_of_instances = ref_mod.instances_sim[pop_index];
System.out.println(" ???????????????????? SdaiSession  pop_index: " + pop_index + 
"   ref_mod.lengths[pop_index]: " + ref_mod.lengths[pop_index]);
for (int j = 0; j < ref_mod.lengths[pop_index]; j++) {
System.out.println("    ------- SdaiSession  row_of_instances[j]: " + row_of_instances[j]);
}*/
//if (inst == null) {
/*System.out.println("SdaiSession ++++++ ref_mod: " + ref_mod + "  inst_id: " + inst_id + 
"   index2mod: " + index2mod + "   pop_index: " + pop_index + "   inst_index: " + inst_index + 
"   lengths[pop_index]: " + ref_mod.lengths[pop_index]);*/
//}
					bypass_values(undo_redo_file, (CEntity_definition)inst.getInstanceType());
					ref_mod.bypass_setAll = true;
					try {
						inst.setAll(null);
					} finally {
						ref_mod.bypass_setAll = false;
					}
//					ref_mod.delete_created(inst_id, pop_index, inst_index, undo_redo_file.readBoolean());
					ref_mod.delete_created(inst_id, pop_index, inst_index, modif);
					inst.owning_model = null;
					break;
				case 'd':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					ref_mod.create_deleted(undo_redo_file, inst_id, pop_index, inst_index, modif, undo_redo_file.readBoolean());
					break;
				case 'm':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					CEntity_definition edef = ref_mod.restore_modified(undo_redo_file, inst_id, pop_index, inst_index, modif);
					bypass_values(undo_redo_file, edef);
					break;
				case 's':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					inst_del = undo_redo_file.readBoolean();
					CEntity older = ref_mod.create_substituted(undo_redo_file, inst_id, pop_index);

					new_inst_id = undo_redo_file.readLong();
					index2new_mod = undo_redo_file.readShort();
					if (index2new_mod < 0 || index2new_mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					new_ref_mod = mods_undo[index2new_mod];
					new_pop_index = undo_redo_file.readShort();
					new_inst_index = undo_redo_file.readInt();
					inst = new_ref_mod.instances_sim[new_pop_index][new_inst_index];
					new_ref_mod.delete_substitute(undo_redo_file, new_pos, older, new_inst_id, new_pop_index, new_inst_index,
						undo_redo_file.readBoolean());
					inst.owning_model = null;
					ref_mod.include_substituted(undo_redo_file, older, pop_index, inst_index, modif, inst_del);
					break;
				case 'e': // External data created
					inst_id = undo_redo_file.readLong();
					SdaiRepository repository = findRepositoryByIdentity(undo_redo_file.readInt());
					if(repository != null) {
						repository.deleteUndoRedoExternalData(inst_id);
					}
					break;
				case 'r': // External data removed
					inst_id = undo_redo_file.readLong();
					repository = findRepositoryByIdentity(undo_redo_file.readInt());
					if(repository != null) {
						repository.restoreUndoRedoExternalData(inst_id);
					}
					break;
				default:
					String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
					throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		undo_redo_file.seek(cur_pos);
		return true;
	}


	void bypass_values(RandomAccessFile ur_f, CEntity_definition def) throws java.io.IOException, SdaiException {
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			for (int j = 0; j < def.partialEntityTypes[i].noOfPartialAttributes; j++) {
				bypass_value_for_undo(ur_f, true, (byte)' ');
			}
		}
	}


	private void bypass_value_for_undo(RandomAccessFile ur_f, boolean byte_needed, byte sym)
			throws java.io.IOException, SdaiException {
		byte token;

		if (byte_needed) {
			token = ur_f.readByte();
		} else {
			token = sym;
		}
		switch (token) {
			case '$':
			case '*':
			case 'f':
			case 't':
			case 'u':
				break;
			case 'i':
				ur_f.readInt();
				break;
			case 'r':
				ur_f.readDouble();
				break;
			case 'e':
			case 's':
				ur_f.readUTF().intern();
				break;
			case 'b':
//				ur_f.readUTF();
				int bt_count = (int)ur_f.readLong();
				ur_f.skipBytes(bt_count);
				break;
			case 'p':
				ur_f.readShort();
				bypass_value_for_undo(ur_f, true, (byte)' ');
				break;
			case '1':
				ur_f.readLong();
				ur_f.readShort();
				ur_f.readInt();
				break;
			case '2':
				ur_f.readLong();
				ur_f.readShort();
				ur_f.readShort();
				ur_f.readInt();
				break;
			case '3':
			case '4':
				ur_f.readLong();
				ur_f.readShort();
				ur_f.readShort();
				break;
			case '(':
				byte bt = ur_f.readByte();
				while (bt != ')') {
					bypass_value_for_undo(ur_f, false, bt);
					bt = ur_f.readByte();
				}
				break;
			default:
				String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
				throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


	boolean restore_group_forward() throws java.io.IOException, SdaiException {
		if (!undo_performed) {
			return false;
		}
		long inst_id, new_inst_id;
		int index2mod, index2new_mod;
		SdaiModel ref_mod, new_ref_mod;
		int pop_index, new_pop_index;
		int inst_index, new_inst_index;
		CEntity_definition edef;
		boolean inst_del, modif;

		long numb = undo_redo_file.readLong();
		if (numb < 0) {
			return false;
		}
		byte bt = undo_redo_file.readByte();
		long f_pointer = undo_redo_file.getFilePointer();
		while (bt != 'Q') {
			switch (bt) {
				case 'c':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					ref_mod.create_again(undo_redo_file, f_pointer, inst_id, pop_index, inst_index);
					break;
				case 'o':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					ref_mod.copy_again(undo_redo_file, f_pointer, inst_id, pop_index, inst_index);
					break;
				case 'd':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					inst_del = undo_redo_file.readBoolean();
					edef = ref_mod.delete_again(undo_redo_file, f_pointer, inst_id, pop_index, inst_index);
					bypass_values(undo_redo_file, edef);
					break;
				case 'm':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					edef = (CEntity_definition)ref_mod.instances_sim[pop_index][inst_index].getInstanceType();
					bypass_values(undo_redo_file, edef);
					ref_mod.modified_again(undo_redo_file, f_pointer, inst_id, pop_index, inst_index);
					break;
				case 's':
					inst_id = undo_redo_file.readLong();
					index2mod = undo_redo_file.readShort();
					if (index2mod < 0 || index2mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					ref_mod = mods_undo[index2mod];
					pop_index = undo_redo_file.readShort();
					inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					inst_del = undo_redo_file.readBoolean();
					edef = (CEntity_definition)ref_mod.instances_sim[pop_index][inst_index].getInstanceType();
					bypass_values(undo_redo_file, edef);

					new_inst_id = undo_redo_file.readLong();
					index2new_mod = undo_redo_file.readShort();
					if (index2new_mod < 0 || index2new_mod >= n_mods_undo) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					new_ref_mod = mods_undo[index2new_mod];
					new_pop_index = undo_redo_file.readShort();
					new_inst_index = undo_redo_file.readInt();
					modif = undo_redo_file.readBoolean();
					new_ref_mod.substitute_again(undo_redo_file, f_pointer, ref_mod, inst_id, pop_index, inst_index,
						new_inst_id, new_pop_index, new_inst_index, modif);
					break;
				case 'e': // External data created
					inst_id = undo_redo_file.readLong();
					SdaiRepository repository = findRepositoryByIdentity(undo_redo_file.readInt());
					if(repository != null) {
						repository.restoreUndoRedoExternalData(inst_id);
					}
					break;
				case 'r': // External data removed
					inst_id = undo_redo_file.readLong();
					repository = findRepositoryByIdentity(undo_redo_file.readInt());
					if(repository != null) {
						repository.deleteUndoRedoExternalData(inst_id);
					}
					break;
				default:
					String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
					throw new SdaiException(SdaiException.SY_ERR, base);
			}
			numb = undo_redo_file.readLong();
			bt = undo_redo_file.readByte();
			f_pointer = undo_redo_file.getFilePointer();
		}
		return true;
	}


    SdaiRepository findRepositoryByIdentity(int repIdentity) {
		for (int i = 0; i < known_servers.myLength; i++) {
			SdaiRepository existingRepository = (SdaiRepository)known_servers.myData[i];
			if(System.identityHashCode(existingRepository) == repIdentity) {
				return existingRepository;
			}
		}
		return null;
    }

	final void undoRedoCreatePrepare(CEntity instance, boolean mod_modified) throws SdaiException {
		if (undo_redo_file != null) {
			if (!group_start_event) {
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
				forbid_undo_on_SdaiEvent = false;
				group_start_event = true;
			}
			if (undoRedoInstance != null) {//
				// write out previous instance
				writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
			}
			// prepare for next instance
			undoRedoOperation = CREATE_OPERATION;
  			undoRedoInstance = instance;
			modif_state = mod_modified;
		}
	}


	final void undoRedoCopyPrepare(CEntity instance, boolean mod_modified) throws SdaiException {
		if (undo_redo_file != null) {
			if (!group_start_event) {
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
				forbid_undo_on_SdaiEvent = false;
				group_start_event = true;
			}
			if (undoRedoInstance != null) {//
				// write out previous instance
				writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
			}
			// prepare for next instance
			undoRedoOperation = COPY_OPERATION;
  			undoRedoInstance = instance;
			modif_state = mod_modified;
		}
	}


	final void undoRedoDeletePrepare(CEntity instance) throws SdaiException {
		if (undo_redo_file != null) {
			if (!group_start_event) {
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
				forbid_undo_on_SdaiEvent = false;
				group_start_event = true;
			}
			if (undoRedoInstance != null) {//
				// write out previous instance
				writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
			}
			// prepare for next instance
			undoRedoOperation = DELETE_OPERATION;
			instance.owning_model.prepareAll(undoRedoOldValue, (CEntity_definition)instance.getInstanceType());
			instance.getAll(undoRedoOldValue);
  			undoRedoInstance = instance;
			saved_model = instance.owning_model;
			saved_sch_data = saved_model.underlying_schema.modelDictionary.schemaData;
			modif_state = saved_model.modified;
			del_state = saved_model.inst_deleted;
			String entity_name = ((CEntityDefinition)instance.getInstanceType()).getNameUpperCase();
			int pop_index = saved_sch_data.find_entity(0, saved_sch_data.sNames.length - 1, entity_name);
			if (pop_index < 0) {
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
			saved_inst_index = instance.find_instance(pop_index);
		}
	}


	final void undoRedoModifyPrepare(CEntity instance) throws SdaiException {
		if (undo_redo_file != null) {
			if (!group_start_event) {
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
				forbid_undo_on_SdaiEvent = false;
				group_start_event = true;
			}
			if (undoRedoInstance != instance) { // instance user is changing
				if (undoRedoInstance != null) {
					// write out previous instance
					writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
				}
				// prepare for next instance
				undoRedoOperation = MODIFY_OPERATION;
				instance.owning_model.prepareAll(undoRedoOldValue, (CEntity_definition)instance.getInstanceType());
				instance.getAll(undoRedoOldValue);
  				undoRedoInstance = instance;
				modif_state = instance.owning_model.modified;
			}
		}
	}


	final void undoRedoSubstitutePrepare(CEntity old_instance, CEntity new_instance, boolean mod_modified)
			throws SdaiException {
		if (undo_redo_file != null) {
			if (!group_start_event) {
				forbid_undo_on_SdaiEvent = true;
				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
				forbid_undo_on_SdaiEvent = false;
				group_start_event = true;
			}
			if (undoRedoInstance != null) {//
				// write out previous instance
				writeToUndoRedoFile(undoRedoOperation, undoRedoInstance, undoRedoOldValue, modif_state);
			}
			// prepare for next instance
			undoRedoOperation = SUBSTITUTE_OPERATION;
			CEntity_definition def = (CEntity_definition)old_instance.getInstanceType();
			old_instance.owning_model.prepareAll(undoRedoOldValue, def);
			old_instance.getAll(undoRedoOldValue);
  			undoRedoInstance = new_instance;
			modif_state = mod_modified;
			substituted[0] = old_instance.instance_identifier;
			substituted[1] = find_model_undo(old_instance.owning_model);
			saved_model = old_instance.owning_model;
			saved_sch_data = saved_model.underlying_schema.modelDictionary.schemaData;
			String entity_name = ((CEntityDefinition)def).getNameUpperCase();
			int pop_index = saved_sch_data.find_entity(0, saved_sch_data.sNames.length - 1, entity_name);
			if (pop_index < 0) {
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
			substituted[2] = pop_index;
			if ((saved_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
				pop_index = saved_model.find_entityRO(saved_sch_data.entities[pop_index]);
				if (pop_index < 0) {
					throw new SdaiException(SdaiException.SY_ERR/*, base*/);
				}
			}
			int inst_index = old_instance.find_instance(pop_index);
			if (inst_index < 0) {
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
			substituted[3] = inst_index;
			modif_state_old = saved_model.modified;
			del_state = saved_model.inst_deleted;
		}
	}

	final void undoRedoCreateExternalData(SdaiRepository repository, ExternalData extData) throws SdaiException {
		try {
			if (undo_redo_file != null) {
				undo_redo_file.writeByte('e');
				undo_redo_file.writeLong(extData.owningEntity.instance_identifier);
				undo_redo_file.writeInt(System.identityHashCode(repository));
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}

	final void undoRedoRemoveExternalData(SdaiRepository repository, ExternalData extData) throws SdaiException {
		try {
			if (undo_redo_file != null) {
				undo_redo_file.writeByte('r');
				undo_redo_file.writeLong(extData.owningEntity.instance_identifier);
				undo_redo_file.writeInt(System.identityHashCode(repository));
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}

	private void writeToUndoRedoFile(int operation, CEntity inst, ComplexEntityValue undoRedoOldValue,
			boolean modif_state) throws SdaiException {
//System.out.println("  SdaiSession  operation: " + operation + "   inst: #" + inst.instance_identifier);
		CEntity_definition def = (CEntity_definition)inst.getInstanceType();
		try {
			if (empty_group) {
				pointer_pos = undo_redo_file.getFilePointer();
//System.out.println("SdaiSession ***empty group*** pointer_pos: " + pointer_pos); 
				undo_redo_file.writeLong(0);
				empty_group = false;
//				forbid_undo_on_SdaiEvent = true;
//				fireSdaiLoggingEvent(SdaiLoggingEvent.STARTING_UNDO_GROUP, null);
//				forbid_undo_on_SdaiEvent = false;
			} else {
				long new_pos = undo_redo_file.getFilePointer();
				undo_redo_file.writeLong(pointer_pos);
//System.out.println("SdaiSession ~~~~~~~~~~~~~ old pointer_pos: " + pointer_pos + "  new pointer_pos: " + new_pos); 
				pointer_pos = new_pos;
			}
			undo_performed = false;
//System.out.println("SdaiSession  **********  operation = " + operation + "  inst: " + inst);
			switch (operation) {
				case CREATE_OPERATION:
					undo_redo_file.writeByte('c');
					break;
				case COPY_OPERATION:
					undo_redo_file.writeByte('o');
					break;
				case DELETE_OPERATION:
					undo_redo_file.writeByte('d');
					break;
				case MODIFY_OPERATION:
					undo_redo_file.writeByte('m');
					break;
				case SUBSTITUTE_OPERATION:
					undo_redo_file.writeByte('s');
					undo_redo_file.writeLong(substituted[0]);
					undo_redo_file.writeShort((short)substituted[1]);
					undo_redo_file.writeShort((short)substituted[2]);
					undo_redo_file.writeInt((int)substituted[3]);
					undo_redo_file.writeBoolean(modif_state_old);
					undo_redo_file.writeBoolean(del_state);
					save_undo_values(saved_sch_data, undoRedoOldValue, saved_model);
					break;
			}
			undo_redo_file.writeLong(inst.instance_identifier);
			SchemaData sch_data;
			SdaiModel model;
			if (operation == DELETE_OPERATION) {
				model = saved_model;
				undo_redo_file.writeShort(find_model_undo(saved_model));
				sch_data = saved_sch_data;
			} else {
				model = inst.owning_model;
				undo_redo_file.writeShort(find_model_undo(model));
				sch_data = model.underlying_schema.modelDictionary.schemaData;
			}
			String entity_name = ((CEntityDefinition)def).getNameUpperCase();
			int pop_index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
			if (pop_index < 0) {
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
			undo_redo_file.writeShort(pop_index);
			if (operation == DELETE_OPERATION) {
				undo_redo_file.writeInt(saved_inst_index);
			} else {
				int inst_index = inst.find_instance(pop_index);
				if (inst_index < 0) {
					throw new SdaiException(SdaiException.SY_ERR/*, base*/);
				}
/*CEntity [] row_of_instances = model.instances_sim[pop_index];
for (int j = 0; j < model.lengths[pop_index]; j++) {
System.out.println("    ^^^^^^^^^^^^^^^^^^ SdaiSession  row_of_instances[j]: " + row_of_instances[j]);
}*/
				undo_redo_file.writeInt(inst_index);
			}
			undo_redo_file.writeBoolean(modif_state);
			if (operation == DELETE_OPERATION) {
				undo_redo_file.writeBoolean(del_state);
			}
			if (operation == MODIFY_OPERATION || operation == DELETE_OPERATION) {
				save_undo_values(sch_data, undoRedoOldValue, model);
			}
			if (operation != DELETE_OPERATION) {
				model.prepareAll(undoRedoOldValue, def);
				inst.getAll(undoRedoOldValue);
				save_undo_values(sch_data, undoRedoOldValue, model);
			}
//System.out.println("SdaiSession  ###################  current pos: " + undo_redo_file.getFilePointer()); 
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}


	void save_undo_values(SchemaData sch_data, ComplexEntityValue e_values, SdaiModel owners_mod)
			throws java.io.IOException, SdaiException {
		for (int i = 0; i < e_values.def.noOfPartialEntityTypes; i++) {
			EntityValue pval = e_values.entityValues[i];
			if (pval == null) {
				String base = line_separator + AdditionalMessages.BF_ERVC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (int j = 0; j < pval.count; j++) {
				Value val = pval.values[j];
				boolean res = val.explore_value_for_undo(this, undo_redo_file, owners_mod, sch_data);
				if (!res) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_FVAL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
	}


	short find_model_undo(SdaiModel mod) throws SdaiException {
		short found_mod_ind = -1;
		for (short i = 0; i < n_mods_undo; i++) {
			if (mods_undo[i] == mod) {
				found_mod_ind = i;
				break;
			}
		}
		if (found_mod_ind < 0) {
			if (n_mods_undo >= mods_undo.length) {
				ensureModsUndoCapacity();
			}
			found_mod_ind = (short)n_mods_undo;
			mods_undo[n_mods_undo++] = mod;
		}
		return found_mod_ind;
	}


	short find_entity_undo(String ent_name) throws SdaiException {
		if (ent_names_undo == null) {
			ent_names_undo = new String[NUMBER_OF_ENT_NAMES_UNDO];
			ent_names_undo[0] = ent_name;
			n_ent_names_undo = 1;
			return 0;
		}
		short found_ent_ind = -1;
		for (short i = 0; i < n_ent_names_undo; i++) {
			if (ent_names_undo[i].equals(ent_name)) {
				found_ent_ind = i;
				break;
			}
		}
		if (found_ent_ind < 0) {
			if (n_ent_names_undo >= ent_names_undo.length) {
				ensureEntNamesUndoCapacity();
			}
			found_ent_ind = (short)n_ent_names_undo;
			ent_names_undo[n_ent_names_undo++] = ent_name;
		}
		return found_ent_ind;
	}


/**
 * Adds <code>SdaiListener</code> extending <code>java.util.EventListener</code>
 * to a special aggregate of SDAI listeners receiving SDAI logging events.
 * @param listener a <code>SdaiListener</code> to be added.
 * @see #removeLoggingListener
 * @since 4.1.0
 */
	public void addLoggingListener(SdaiListener listener) throws SdaiException {
		if (loggingListenrList == null) {
			loggingListenrList = new CAggregate(setTypeSpecial);
		}
		loggingListenrList.addUnordered(listener, null);
	}


/**
 * Removes <code>SdaiListener</code> extending <code>java.util.EventListener</code>
 * from the special aggregate of SDAI listeners receiving SDAI logging events.
 * @param listener <code>SdaiListener</code> to be removed.
 * @see #addLoggingListener
 * @since 4.1.0
 */
	public void removeLoggingListener(SdaiListener listener) throws SdaiException {
		if (loggingListenrList != null) {
			loggingListenrList.removeUnordered(listener, null);
		}
	}


/**
 * Notifies all SDAI listeners, which have registered interest for notification,
 * on logging events. If not submitted through the second parameter,
 * the event instance is lazily created using the first parameter
 * passed into the fire method.
*/
	private void fireSdaiLoggingEvent(int id, SdaiEvent sdaiEvent) {
		if (loggingListenrList != null && loggingListenrList.myLength > 0) {
			if (loggingListenrList.myLength == 1) {
				if (sdaiEvent == null) {
					sdaiEvent = new SdaiLoggingEvent(this, id, -1, null);
				}
				((SdaiListener)loggingListenrList.myData).actionPerformed(sdaiEvent);
			} else {
				Object [] myDataA = new Object[loggingListenrList.myLength];
				System.arraycopy(loggingListenrList.myData, 0, myDataA, 0, myDataA.length);
				if (sdaiEvent == null) {
					sdaiEvent = new SdaiLoggingEvent(this, id, -1, null);
				}
				for (int i = 0; i < myDataA.length; i++) {
					((SdaiListener)myDataA[i]).actionPerformed(sdaiEvent);
				}
			}
		}
	}

    PhFileReader getReader() throws SdaiException{
		return reader;
	}


	final void unset_entity_values(ComplexEntityValue cev) throws SdaiException {
		if (cev == null) {
			return;
		}
		cev.def = null;
		EntityValue [] evalues = cev.entityValues;
		if (evalues == null) {
			return;
		}
		for (int i = 0; i < evalues.length; i++) {
			EntityValue ev = evalues[i];
			if (ev == null) {
				continue;
			}
			ev.unset_EntityValue();
		}
	}


	void ensureModsUndoCapacity() {
		int new_length = mods_undo.length * 2;
		SdaiModel [] new_array = new SdaiModel[new_length];
		System.arraycopy(mods_undo, 0, new_array, 0, mods_undo.length);
		mods_undo = new_array;
	}


	void ensureEntNamesUndoCapacity() {
		int new_length = ent_names_undo.length * 2;
		String [] new_array = new String[new_length];
		System.arraycopy(ent_names_undo, 0, new_array, 0, ent_names_undo.length);
		ent_names_undo = new_array;
	}


	void printWarningToLogoSdaiContext(String text) throws SdaiException {
		if (logWriterSession != null) {
			logWriterSession.println(text);
		} else {
			println(text);
		}
	}




  /**
   * Links SQL Bridge and makes remote repositories available in aggregate of
   * <code>known_servers</code>, which is accessible through
   * {@link #getKnownServers getKnownServers} method.
   *
   * <p> This method is an extension of JSDAI, which is
   * not a part of the standard.
   * <P><B>Example:</B>
   * <P><pre>
   *  SdaiSession s = ...;
   *  s.linkDataBaseBridge("//my.bridgeUrl.com", "user", "password".toCharArray());
   *  ...
   *  ASdaiRepository agRepositories = s.getKnownServers();
   *  </pre>
   *
   * @param bridgeURL a location where SQL Bridge is running prefixed with "//".
   * @param user a user of SQL Bridge to use.
   * @param password password stored as a character array.
   * @throws SdaiException LC_NVLD, the location is invalid.
   * @throws SdaiException SY_ERR, underlying system error.
   * @see #getKnownServers
   * @see <a href="package-summary.html#DBExtension">JSDAI DB Extension</a>
   */

  public void linkDataBaseBridge(String bridgeURL, String user, char password[]) throws SdaiException {
	  if(Implementation.remoteSupport) {
//		  synchronized (syncObject) {
			  if(Implementation.userConcurrencyCompatibility == 1 && active_transaction != null) {
				  throw new SdaiException(SdaiException.TR_NEXS, "Transaction can not be running while linking");
			  }
			  if(bridgeSession != null) {
				  throw new SdaiException(SdaiException.SY_ERR, "Already connected to sqlBridge");
			  }
			  if(bridgeURL.indexOf(LOCATION_PREFIX) < 0) {
				  throw new SdaiException(SdaiException.LC_NVLD);
			  }
// 			  String bLocation = LOCATION_PREFIX + user + ":" + new String(password) + "@" +  bridgeURL.substring(2)+"/";
			  this.bridgeURL = bridgeURL;
			  try {
				  synchronized(sdaiBridgeRemoteFactorySync) {
					  if(sdaiBridgeRemoteFactory == null) {
						  sdaiBridgeRemoteFactory = SdaiBridgeRemoteFactory.newInstance();
					  }
				  }
				  SdaiBridgeRemote bridge = sdaiBridgeRemoteFactory.newSdaiBridgeRemote();
				  bridge.initBridge(bridgeURL);
				  bridgeSession = bridge.initUser(user, new String(password));
// 				  if(Implementation.userConcurrencyCompatibility == 0 && active_transaction != null) {
// 					  active_transaction.initRemoteTransaction();
// 				  }
				  linkRemoteRepositories();
			  } catch(Exception ex) {
				  unlinkBridgeInternal();
				  throw new SdaiException(SdaiException.SY_ERR, ex);
			  }
			  this.bridgeURL = bridgeURL;
//		  } // syncObject
	  } else {
		  throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.RC_NAVL);
	  }
  }

	private void linkRemoteRepositories() throws SdaiException {
		SdaiRepositoryRemote[] retVal = bridgeSession.repositoriesList();
		  for(int i = 0; i < retVal.length; i++) {
			  SdaiRepository repository = createSdaiRepositoryImpl(retVal[i].getRemoteName(), getRepositoriesPath(), false);
			  repository.location = retVal[i].getRemoteLocation();
			  repository.setRepoRemote(retVal[i]);
			  repository.modified = false;
			  known_servers.addUnorderedRO(repository);
		  }
	}

	private void unlinkRemoteRepositories() throws SdaiException {
		boolean found = true;
		while(found) {
			found = false;
			ASdaiRepository repositories = getKnownServers();
			SdaiIterator iter = repositories.createIterator();
			while(iter.next()) {
				SdaiRepository repo = repositories.getCurrentMember(iter);
				if(repo.getRepoRemote() != null) {
					repo.unlinkRepository();
					repo.setRepoRemote(null);
					found = true;
					break;
				}
			}
		}
	}

	private void unlinkBridgeInternal() throws SdaiException {
		unlinkRemoteRepositories();
		this.bridgeURL = null;
		if(bridgeSession != null) {
			SessionRemote prevBridgeSession = bridgeSession;
			bridgeSession = null;
			prevBridgeSession.close();
		}
	}

/**
 * Removal of all remote repositories from the aggregate "known_servers".
 */
	public void unlinkDataBaseBridge() throws SdaiException {
		if(Implementation.remoteSupport) {
//			synchronized (syncObject) {
				if(bridgeSession == null) {
					throw new SdaiException(SdaiException.SY_ERR, "Not connected to sqlBridge");
				}
				try {
					switch(Implementation.userConcurrencyCompatibility) {
					case 0:
						if(active_transaction != null) {
							active_transaction.stopRemoteTransaction();
						}
						break;
					case 1:
						if(active_transaction != null) {
							throw new SdaiException(SdaiException.TR_EXS,
													"Transaction can not be running while unlinking");
						}
						break;
					}
				} finally {
					unlinkBridgeInternal();
				}
//			} // syncObject
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.RC_NAVL);
		}
	}

/**
 * Returns SQL bridge connection URL
 */

	public String getDataBaseBridge() throws SdaiException {
//		synchronized (syncObject) {
			if (!opened) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
			return bridgeURL;
//		} // syncObject
	}

	/**
	 * Switches remote repositories to specified commit point
	 * in history or to current state if null is passed as the parameter.
	 * This is an extension of SDAI. Commit reference is obtained from
	 * JSDAI-DB using remote queries in namespace <code>jsdai:database:</code>
	 * by querying entity <code>commits</code> which corresponds to
	 * commits table. Entity type  <code>commits</code> is defined by
	 * this EXPRESS code:<br/>
	 * <pre>
	 * ENTITY commits;
	 *   user_name : OPTIONAL STRING;
	 *   time_stamp : STRING;
	 *   app_comment : OPTIONAL STRING;
	 * END_ENTITY;
	 * </pre>
	 * Attribute <code>time_stamp</code> conforms to this
	 * <code>SimpleDateFormat</code> string:<br/>
	 * <code>yyyy-MM-dd'T'HH:mm:ss.S</code><br/>
	 * For querying also two more alternative <code>time_stamp</code>
	 * formats are allowed:<br/>
	 * <code>yyyy-MM-dd'T'HH:mm:ss</code><br/>
	 * <code>yyyy-MM-dd</code><br/>
	 * <p>This method is an extension of JSDAI, which is
	 * not a part of the standard.</p>
	 *
	 * @param commitRef commit reference obtained using remote SDAI query
	 *                  or null which denotes current state
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @since 4.1.0
	 */
	public void switchToCommit(SerializableRef commitRef) throws SdaiException {
		if(Implementation.remoteSupport) {
			if(bridgeSession == null) {
				throw new SdaiException(SdaiException.SY_ERR, "Not connected to sqlBridge");
			}
			unlinkRemoteRepositories();
			bridgeSession.switchToCommit(commitRef);
			linkRemoteRepositories();
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.RC_NAVL);
		}
	}

	/**
	 * Locks specified remote <code>SdaiRepositories</code>, <code>SchemaInstances</code>,
	 * and <code>SdaiModels</code> in exclusive mode on JSDAI-DB.
	 * <p>JSDAI-DB can place shared or exclusive locks on remote
	 * <code>SdaiRepositories</code>, <code>SchemaInstances</code>, and
	 * <code>SdaiModels</code>. JSDAI-DB locks objects in shared mode for certain
	 * JSDAI operations. Shared mode locks can be placed on the same object by more
	 * than one <code>SdaiSession</code>, however exclusive lock can be placed by at
	 * most one <code>SdaiSession</code> on the same object. In addition the lock can
	 * not be placed on an object if the lock of the opposite type already exists.</p>
	 * <p>Exclusive locks are removed either by invoking method <code>unlockAll</code> or
	 * by ending remote session as a result of invoking <code>unlinkRemoteRepositories</code>
	 * or <code>closeSession</code> methods.</p>
	 * <p>Shared locks are automatically placed by these operations on:
	 * <dl>
	 * <dt><code>SdaiRepository</code></dt>
	 * <dd>During run of <code>deleteRepository</code>,
	 * <code>createModel</code>, <code>createSchemaInstance</code>, and methods
	 * setting the header of <code>SdaiRepository</code>, or during run of
	 * <code>delete</code>, and <code>deleteSdaiModel</code> of contained
	 * <code>SchemaInstances</code> and <code>SdaModels</code> respectively
	 * on JSDAI-DB.</dd>
	 * <dt><code>SchemaInstance</code></dt>
	 * <dd>During run of <code>addSdaiModel</code>, <code>removeSdaiModel</code>,
	 * <code>rename</code>, <code>delete</code>, and methods setting the header of
	 * <code>SchemaInstance</code> on JSDAI-DB.</dd>
	 * <dt><code>SdaiModel</code></dt>
	 * <dd>From <code>startReadWriteAccess</code> or <code>promoteSdaiModelToRW</code>
	 * till <code>endReadWriteAccess</code> or <code>reduceSdaiModelToRO</code>
	 * operations.</br>
	 * During run of <code>renameSdaiModel</code> and <code>deleteSdaiModel</code>
	 * on JSDAI-DB.</dd>
	 * </dl></p>
	 * <p>This method is an extension of JSDAI, which is
	 * not a part of the standard.</p>
	 * @param repositories an aggregate of <code>SdaiRepositories</code> to be locked
	 *                     or null if none of repositories needs to be locked.
	 * @param schInstances an aggregate of <code>SchemaInstances</code> to be locked
	 *                     or null if none of schema instances needs to be locked.
	 * @param models an aggregate of <code>SdaiModels</code> to be locked
	 *               or null if none of models needs to be locked.
	 * @param maxwait maximum time in seconds to retry getting lock. The value
	 *                of <code>Integer.MAX_VALUE</code> means that locking attempts
	 *                should be retried indeterminately.
	 * @exception SdaiException SY_LOC, if requested lock can not be obtained
	 *                                  within specified maximum time period.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #unlockAll
	 * @see SdaiRepository#getLockingUser
	 * @see SchemaInstance#getLockingUser
	 * @see SdaiModel#getLockingUser
	 * @since 4.1.0
	 */
	public void lock(ASdaiRepository repositories, ASchemaInstance schInstances,
			ASdaiModel models, int maxwait) throws SdaiException {
		if(Implementation.remoteSupport) {
			if(bridgeSession == null) {
				throw new SdaiException(SdaiException.SY_ERR, "Not connected to sqlBridge");
			}
			bridgeSession.lock(repositories, schInstances, models, maxwait);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.RC_NAVL);
		}
	}

	/**
	 * Removes all exclusive locks placed by this <code>SdaiSession</code> on
	 * remote <code>SdaiRepositories</code>, <code>SchemaInstances</code>, and
	 * <code>SdaiModels</code>.
	 * <p>This method is an extension of JSDAI, which is
	 * not a part of the standard.</p>
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #lock
	 * @since 4.1.0
	 */
	public void unlockAll() throws SdaiException {
		if(Implementation.remoteSupport) {
			if(bridgeSession == null) {
				throw new SdaiException(SdaiException.SY_ERR, "Not connected to sqlBridge");
			}
			bridgeSession.unlockAll();
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.RC_NAVL);
		}
	}

	void commitClosing() throws SdaiException {
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

	/**
	 * Creates and returns a new SdaiQuery.
	 *
	 * @param querySpec is a XML document, which defines SdaiQuery
	 * @return The newly created <code>SdaiQuery</code>
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public SdaiQuery newQuery(org.w3c.dom.Document querySpec) throws SdaiException {
		Element el = querySpec.getDocumentElement();
		return newQuery(el);
	}


	/**
	 * Creates and returns a new SdaiQuery
	 * @param el is a XML element, which defines SdaiQuery
	 * @return The newly created <code>SdaiQuery</code>
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public SdaiQuery newQuery(org.w3c.dom.Element el) throws SdaiException {
		// preparse
		String cx = el.getAttribute("context");
		if(cx != null && cx.toLowerCase().equals("remote")) {
			if(bridgeSession == null) {
				throw new SdaiException(SdaiException.SY_ERR, "Not connected to sqlBridge");
			}
			return bridgeSession.newQuery(this, el);
		} else {
			return LocalSdaiQuery.create(this, el);
		}
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		return getActiveModels();
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		return null;
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		return null;
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		return null;
	}

/**
 * Returns remote <code>SdaiRepository</code> using specified repository reference.
 * @param repositoryRef <code>SerializableRef</code> used to refer to remote repository.
 * @return <code>SdaiRepository</code> refered by specified repository reference.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiRepository#getSdaiModelByRef(SerializableRef)
 * @see SdaiRepository#getSchemaInstanceByRef(SerializableRef)
 * @see SdaiRepository#getInstanceByRef(SerializableRef)
 */
    public SdaiRepository getRepositoryByRef(SerializableRef repositoryRef) throws SdaiException{
        SdaiRepository repository;
        long repositoryId = repositoryRef.getRepositoryId();

        for (int i = 0; i < known_servers.myLength; i++) {
			repository = (SdaiRepository)known_servers.myData[i];
            if (repository.hasId(repositoryId)) {
				return repository;
 			}
        }
		try {
            SdaiRepositoryRemote repoRemote = bridgeSession.linkRepository(repositoryRef);
            if (repoRemote != null) {
                repository = bridgeSession.createSdaiRepositoryImpl(this, repoRemote.getRemoteName(), getRepositoriesPath(), false);
                repository.setRepoRemote(repoRemote);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new SdaiException(SdaiException.SY_ERR, e);
        }

		repository.modified = false;
		known_servers.addUnorderedRO(repository);
		return repository;
    }

	SdaiRepository findRepositoryInKnownServers(long repositoryId) throws SdaiException {
        SdaiRepository repository;

        for (int i = 0; i < known_servers.myLength; i++) {
			repository = (SdaiRepository)known_servers.myData[i];
            if (repository.hasId(repositoryId)) {
				return repository;
 			}
        }
		return null;
	}

	/**
	 * Converts target (AIM) population by invoking underlying AIM2ARM algorithm.
	 *
	 * @param mappingContext the mapping context containing conversion related information
	 * @exception SdaiException if an underlying JSDAI error occurs
	 * @see MappingContext
	 * @see MappingPopulationCreator
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
     * @since 3.6.0
	 */
	static public void convertMapping(MappingContext mappingContext) throws SdaiException {
		if(Implementation.mappingSupport) {
			mappingContext.clearCaches();
			AEntity_mapping entityMappings = (AEntity_mapping)
				mappingContext.context.mappingDomain.getInstances(EEntity_mapping.class);
			MappingConstraintMatcher.MatcherInstances allInstances =
				mappingContext.newAllInstances(MappingConstraintMatcher.MatcherInstances.STATUS_FORWARD);
			SdaiIterator entityMappingIter = entityMappings.createIterator();
			while(entityMappingIter.next()) {
				((MappingConstraintMatcher)entityMappings.getCurrentMemberObject(entityMappingIter))
					.findForward(mappingContext, allInstances, true);
			}
			mappingContext.matcherInstancesCache.clear();
			final List mappingList = mappingContext.mappingList;
			final int mappingListSize = mappingList.size();
			if(mappingContext.interleavedCreation) {
				for(int mappingIdx = 0; mappingIdx < mappingListSize; ) {
					EEntity_mapping mapping = (EEntity_mapping)mappingList.get(mappingIdx);
					mappingList.set(mappingIdx++, null);
					Set targetInstances = (Set)mappingList.get(mappingIdx);
					mappingList.set(mappingIdx++, null);
					mappingContext.creator.createSourceInstance(mapping, targetInstances);

					if(mappingIdx < mappingListSize && !(mappingList.get(mappingIdx) instanceof EEntity_mapping)) {
						int attributeIdx = -1;
						for(Iterator i = targetInstances.iterator(); i.hasNext(); ) {

							EEntity aimInstance = (EEntity)i.next();
							List sourceKeys = new ArrayList();
							List sourceValues = new ArrayList();
							Object attributeMapping;
							attributeIdx = mappingIdx;
							for( ; attributeIdx < mappingListSize &&
									 !((attributeMapping = mappingList.get(attributeIdx)) instanceof EEntity_mapping); attributeIdx++) {
								Map attributeValueMap = (Map)mappingList.get(++attributeIdx);
								Object attributeValue = attributeValueMap.get(aimInstance);
								if(attributeValue != null) {
									sourceKeys.add(attributeMapping);
									sourceValues.add(attributeValue);
								}
							}
							if(!sourceKeys.isEmpty()) {
								Map sourceValueMap = new ImmutableArrayMap(sourceKeys, sourceValues);
								mappingContext.creator.setSourceAttributeValues(mapping, aimInstance, sourceValueMap);
							}
						}
						for( ; mappingIdx < attributeIdx; mappingIdx++) {
							mappingList.set(mappingIdx, null);
						}
					}
				}
			} else {
				for(int mappingIdx = 0; mappingIdx < mappingListSize; ) {
					EEntity_mapping mapping = (EEntity_mapping)mappingList.get(mappingIdx++);
					Set targetInstances = (Set)mappingList.get(mappingIdx++);
					mappingContext.creator.createSourceInstance(mapping, targetInstances);

					while(mappingIdx < mappingListSize && !(mappingList.get(mappingIdx) instanceof EEntity_mapping)) {
						mappingIdx++;
					}
				}
				for(int mappingIdx = 0; mappingIdx < mappingListSize; ) {
					EEntity_mapping mapping = (EEntity_mapping)mappingList.get(mappingIdx);
					mappingList.set(mappingIdx++, null);
					Set targetInstances = (Set)mappingList.get(mappingIdx);
					mappingList.set(mappingIdx++, null);

					if(mappingIdx < mappingListSize && !(mappingList.get(mappingIdx) instanceof EEntity_mapping)) {
						int attributeIdx = -1;
						for(Iterator i = targetInstances.iterator(); i.hasNext(); ) {

							EEntity aimInstance = (EEntity)i.next();
							List sourceKeys = new ArrayList();
							List sourceValues = new ArrayList();
							Object attributeMapping;
							attributeIdx = mappingIdx;
							for( ; attributeIdx < mappingListSize &&
									!((attributeMapping = mappingList.get(attributeIdx)) instanceof EEntity_mapping); attributeIdx++) {
								Map attributeValueMap = (Map)mappingList.get(++attributeIdx);
								Object attributeValue = attributeValueMap.get(aimInstance);
								if(attributeValue != null) {
									sourceKeys.add(attributeMapping);
									sourceValues.add(attributeValue);
								}
							}
							if(!sourceKeys.isEmpty()) {
								Map sourceValueMap = new ImmutableArrayMap(sourceKeys, sourceValues);
								mappingContext.creator.setSourceAttributeValues(mapping, aimInstance, sourceValueMap);
							}
						}
						for( ; mappingIdx < attributeIdx; mappingIdx++) {
							mappingList.set(mappingIdx, null);
						}
					}
				}
			}
			mappingContext.clearCaches();
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	final void startProcessTransactionReadOnly() throws SdaiException {
		switch(Implementation.userConcurrencyCompatibility) {
		case 0:
			if(bridgeSession != null && active_transaction == null) {
				startTransactionReadOnlyAccess();
				active_transaction.oldMode = SdaiTransaction.NO_ACCESS;
			}
			break;
		case 1:
			if(active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}
			break;
		}
	}

	final void endProcessTransactionReadOnly() throws SdaiException {
		if(Implementation.userConcurrencyCompatibility == 0 && bridgeSession != null
		   && active_transaction.oldMode == SdaiTransaction.NO_ACCESS) {
			active_transaction.endTransactionAccessCommit();
		}
	}

	final void startProcessTransactionReadWrite() throws SdaiException {
		switch(Implementation.userConcurrencyCompatibility) {
		case 0:
			if(bridgeSession != null) {
				int oldMode = SdaiTransaction.READ_WRITE;
				if(active_transaction == null) {
					oldMode = SdaiTransaction.NO_ACCESS;
				} else if(active_transaction.mode == SdaiTransaction.READ_ONLY) {
					active_transaction.endTransactionAccessCommit();
					oldMode = SdaiTransaction.READ_ONLY;
				}
				if(oldMode != SdaiTransaction.READ_WRITE) {
					startTransactionReadWriteAccess();
					active_transaction.oldMode = oldMode;
				}
			}
			break;
		case 1:
			if(active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}
			break;
		}
	}

	final void endProcessTransactionReadWrite() throws SdaiException {
		if(Implementation.userConcurrencyCompatibility == 0 && bridgeSession != null
		   && active_transaction.oldMode >= 0) {
			SdaiTransaction oldTransaction = active_transaction;
			oldTransaction.endTransactionAccessCommit();
			if(oldTransaction.oldMode == SdaiTransaction.READ_ONLY) {
				startTransactionReadOnlyAccess();
			}
		}
	}

	void print_entity_values(ComplexEntityValue compl, long instance_identifier) throws SdaiException {
		int count = compl.def.noOfPartialEntityTypes;
		System.out.println("INSTANCE: #" + instance_identifier);
		for (int i = 0; i < count; i++) {
			EntityValue partval = compl.entityValues[i];
			String def_name = compl.def.partialEntityTypes[i].name;
			System.out.println("****** partial entity no. " + i + "   entity: " + def_name);
			for (int j = 0; j < partval.count; j++) {
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
		String str;
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
				if (val.integer == 0) {
					System.out.print(".F.  ");
				} else if (val.integer == 1) {
					System.out.print(".T.  ");
				} else {
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
				String ref_class = ref.getClass().getName();
				CEntity inst = (CEntity)ref;
				System.out.print(ref_class + "  " + "   id: " + inst.instance_identifier);
//				System.out.print("REF  ");
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

	private static FileLock lockRepositoriesPath(String path) throws SdaiException {
		try {
			File parentFile = new File(path, "application");
			if (!parentFile.exists()) {
				if(!parentFile.mkdirs()) {
					throw new SdaiException(SdaiException.SY_ERR,
											"\nCould not create directory \"" + parentFile + "\"");
				}
			}
			FileOutputStream fileStream = new FileOutputStream(new File(parentFile, ".lock"), true);
			boolean wasLocked = false;
			try {
				FileLock fileLock = fileStream.getChannel().tryLock();
				if(fileLock != null) {
					wasLocked = true;
					return fileLock;
				} else {
					throw new SdaiException(SdaiException.SY_ERR,
							"\nRepository path \"" + path + "\"\nspecified in " +
							"jsdai.properties is locked by another running JSDAI instance");
				}
			} finally {
				if(!wasLocked) {
					fileStream.close();
				}
			}
		} catch (IOException e) {
			if(System.getProperty("jsdai.allow.repository.path.lock.failure","")
					.toLowerCase().equals("true")) {
				return null;
			} else {
				throw new SdaiException(SdaiException.SY_ERR, e);
			}
		}
	}

	private static void releaseRepositoriesPath(FileLock fileLock) throws SdaiException {
		if (fileLock != null) {
			try {
				fileLock.release();
				fileLock.channel().close();
			} catch (IOException e) {
				throw new SdaiException(SdaiException.SY_ERR, e);
			}
		}
	}

}