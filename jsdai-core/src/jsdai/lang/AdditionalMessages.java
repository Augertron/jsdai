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

/** This is for internal JSDAI use only. Applications shall not use this */
class AdditionalMessages {

//----Added by Donatas
	static final String SS_PWD = "Login incorrect";
	static final String IO_USER = "Property \"user\" in file jsdai.properties not found";
	static final String RP_ACD = "User access denied to repository";
	static final String NE_MDUP = "SDAI-model duplicate on server";
	static final String NE_SDUP = "Schema Instance duplicate on server";
	static final String MO_RO = "Can't access this model in Read-Write mode";
	static final String MO_RU = "Another user have access to this model in Read-Write mode";
	static final String MO_NA = "SdaiModel is not available";
	static final String SI_NA = "Schema Instance is not available";
	static final String RP_DUP = "Repository duplicate on remote server";






//----Added by VV
    static final String MO_VDIF = "Version of associated model differs from version defined in repository";

//--------------
//   Mixed usage
	static final String AX_NAME = "The name: "; // in addition to MO_DUP, RP_DUP
	static final String EI_CREF = "Creation of the entity instance failed"; // makeInstance()
	static final String SE_INSI = "Invalid select information for value being tested"; // CAggregate, CEntity
	static final String SE_NUNF = "Select path is invalid"; // CAggregate, CEntity
	static final String SE_INCP = "Incompatible parameters of the function"; // CAggregate, CEntity
	static final String FL_ORZE = "Only one repository can be open in SINGLE runtime environment"; // SdaiSession, SdaiRepository
	static final String FL_OMZE = "Only one model can be started in SINGLE runtime environment"; // SdaiModel, PhFileReader
//   SdaiSession
	static final String SS_LOCF = "First repository location: ";
	static final String SS_LOCS = "Second repository location: ";
	static final String SS_SCMI = "Error: SdaiContext is not provided.";
	static final String SS_BLNO = "Error: rules black list file not found: ";
	static final String SS_BLEX = "Error: exception while reading rules black list file: ";
//   SdaiRepository
	static final String SS_NODR = "A directory for repositories is not specified"; // deleteRepository()
	static final String RP_DICT = "The repository containing data dictionary cannot be deleted"; // deleteRepository()
	static final String EI_LANV = "Label supplied is invalid";  // getSessionIdentifier()
	static final String FL_OSZE = "Only one schema allowed in SINGLE runtime environment"; // creation of SchemaInstance
//   SchemaInstance
	static final String SI_CYCL = "Inclusion of submitted schema instance will create loops in schema instances tree.";
	static final String SI_SYRE = "Inclusion of a schema instance from the System Repository is disallowed.";
	static final String SI_AUTO = "Autoinclusion for a schema instance is disallowed.";
	static final String SI_LOOP = "Warning: autoinclusion for schema instance ";
//   SdaiModel
	static final String EI_NVLI = "Entity instance identifier cannot be set";  // ident reached an upper limit
	static final String EI_EDNV = "Entity definition is not in the context of the schema underlying for this model.";  // creation of entity instances
	static final String EI_EDAB = "Entity definition is abstract and cannot be used to create entity instances.";  // creation of entity instances
//   CEntity
	static final String AT_EINV = "Error in inverse attribute"; // validateInverseAttributes()
	static final String AI_INCB = "Aggregate bound is of incorrect type"; // validateAggregateSize()
	static final String AI_ENNF = "Entity not found in the table"; // getAsString()
	static final String SE_ARTS = "Select array is not provided or is too small";
	static final String SE_ATSM = "Together with a value being set a select path (a sequence of defined types) specifying the actual type of this value shall be provided";
	static final String AT_REDD = "Error: attribute, which is redeclared as derived, cannot be used as a role in usedin";
	static final String AT_NEXS = "Error: attribute was not found in the entity";
	static final String AT_INEX = "Error: inverse attribute was not found in the entity";
	static final String AT_VANX = "Error: attribute value does not exist";
	static final String AT_VAWR = "Error: wrong type of the attribute value";
	static final String EI_ROMD = "Error: entity instance to be deleted is referenced by an instance in a Read-Only model.";
	static final String EI_NCOM = "Error: the value type is not compatible with this method; please, use get_boolean instead of get_int.";
//   CAggregate and other aggregate classes
	static final String AI_BVL  = "Aggregate instance bound is violated";
	static final String AI_CREF = "Creation of the aggregate instance failed"; // also in SdaiModel class
	static final String AI_WVAL = "Values do not correspond to aggregate type"; // setValueInternal()
	static final String IR_IMPR = "Improper iterator";
	static final String RD_ERRF = "Error in resolving forward references"; // changeReferences()
//   SelectType
	static final String AI_IVP  = "Incorrect value of a parameter";
//   Errors while working with data dictionary
	static final String SE_ERDD = "Error in data dictionary"; // CAggregate, CEntity
	static final String DI_NESA = "Nesting of depth greater than three for standard type aggregates is not supported"; // resolveType(), resolveSimpleType() in SchemaData
	static final String DI_WRMS = "Method getSelections(ESelect_type type, SdaiContext context) shall be used"; // SelectType
	static final String DI_WRME = "Method getElements(EEnumeration_type type, SdaiContext context) shall be used"; // EnumerationType
	static final String DI_WRMP = "Method getBound_value(EBound type, SdaiContext context) shall be used"; // PopulationDependentBound
	static final String DI_BINF = "Wrong dictionary data for the schema: ";
	static final String DI_FUN1 = "Error: field ";
	static final String DI_FUN2 = " is unset.";
	static final String DI_WRDT = "Wrong data type: ";
	static final String DI_NOTF = "Data type not found: ";
	static final String DI_NAME  = "   Data type name: ";
	static final String DI_CODE  = "   Data type code: ";
	static final String DI_ATNF  = "Error: entity attribute was not found in data dictionary ";
//   For "exportClearTextEncoding"
	static final String WR_OUT  = "Writer error (output to exchange structure)"; // PhFileWriter, Print_instance, Get_instance
//   Reading/Writing binary files
	static final String BF_REP  = "   Repository: ";
	static final String BF_MOD  = "   Model: ";
	static final String BF_SCH  = "   Schema instance: ";
	static final String BF_ISCH = "   Included schema instance: ";
	static final String BF_REPS = "   Repository containing schema instance: ";
	static final String BF_NEGL = "Negative counts in binary file"; // SdaiRepository
	static final String BF_DAM  = "Binary file is damaged"; // SdaiRepository, SdaiModel
	static final String BF_OLD0 = "Binary file \""; 
	static final String BF_OLD1 = "\" was created with build No.";  
	static final String BF_OLD2 = "It should be compatible with build at least No.";
	static final String BF_FVAL = "Failed to write attribute value to binary file"; // save_instance() in SdaiModel
	static final String BF_WENT = "Wrong entity in binary file"; // SdaiModel
	static final String BF_EINC = "Entity with the name taken from the binary file was not found: "; // SdaiModel, CEntity
	static final String BF_IINC = "Instance with specification taken from the binary file was not found: #"; // CEntity
	static final String BF_IDIF = "Instance specified differs from the current instance"; // CEntity
	static final String BF_WVAL = "Wrong value in binary file"; // SdaiModel
	static final String BF_WISI = "Wrong index to schema instance in binary file"; // SdaiRepository
	static final String BF_ERVC = "Error in values for complex instances"; // save_instance() in SdaiModel
	static final String BF_ERR  = "Error in binary file"; // SdaiRepository, SdaiModel, SdaiSession
	static final String BF_NOD1 = "Directory for binary files cannot be created: check whether a file with the name \"";
	static final String BF_NOD2 = "\" exists";
	static final String BF_DELF = "Deletion of binary file failed"; // SdaiRepository, SdaiTransaction
	static final String BF_MNF1 = "Binary file \"";
	static final String BF_MNF2 = "\" for model not found";
	static final String BF_DDM1 = "Dictionary data file is missing for schema \"";
	static final String BF_DDM2 = "\"";
	static final String BF_DDE1 = "Error while reading dictionary data file for schema \"";
	static final String BF_DDE2 = "\"";
	static final String BF_MIF0 = "Warning: binary file \""; 
	static final String BF_MIF1 = "\" is missing";
	static final String BF_MIMO = "Warning: reference to missing model "; // extractModel() in SdaiModel
	static final String BF_REND = "Different repository name in binary file was found: "; // loadRepositoryStream() in SdaiRepository
	static final String RE_MINC = "Reference model is incorrect"; // resolveInConnectors() in SdaiModel
	static final String BF_RENF = "Warning: repository does not exist, model was not associated with schema instance."; // loadRepositoryStream() in SdaiRepository
	static final String BF_RPNF = "Warning: repository does not exist, schema instance was not included."; // loadRepositoryStream() in SdaiRepository
	static final String BF_RCLO = "Warning: owning repository is closed, schema instance was not included."; // loadRepositoryStream() in SdaiRepository
	static final String SS_RFMI = "Warning: binary file for System Repository is missing."; // loadSchemaInstances() in SdaiSession
	static final String SS_RFNR = "Warning: cannot open binary file for System Repository."; // loadSchemaInstances() in SdaiSession
	static final String SS_UMOD = "Warning: unknown model associated with schema instance "; // loadSchemaInstances() in SdaiRepository
	static final String BF_RSTM = "Input stream from the remote repository is not provided";
	static final String BF_RSTE = "Error while reading the stream from the remote repository";
	static final String BF_DIID = "Entity instance with not unique identifier: #";
	static final String BF_ICED = "Warning: entity definition is incompatible with the current data dictionary."; // SdaiModel
	static final String BF_MISI = "   Missing single entity data type: "; // SdaiModel
	static final String BF_OLDT = "   No longer existing single entity data type was found: "; // SdaiModel
	static final String BF_DIAT = "   Different set of attributes in single entity data type: "; // SdaiModel
	static final String BF_NFOU = "Warning: entity definition not found in the current data dictionary; its instances will be ignored"; // SdaiModel
	static final String BF_UEDT = "Error: unknown simple entity data type was found in the file: "; // SdaiModel
//   Processing of the properties files
	static final String SS_ND1  = "Directory cannot be created: check whether a file with the name \"";
	static final String SS_ND2  = "\" exists";
	static final String IO_PRNF = "File jsdai.properties not found"; // takeProperties() in SdaiSession
	static final String IO_RENF = "Property \"repositories\" in file jsdai.properties not found"; // getRepositoriesPath() in SdaiSession
	static final String IO_ERPR = "Error while reading jsdai.properties file"; // takeProperties() in SdaiSession
	static final String IO_ERRP = "Error while reading sdairepos.properties file"; // takeRepoProperties(), loadApplicationProperties() in SdaiSession
	static final String IO_ERWP = "Error while writing sdairepos.properties file"; // updateSdaireposProperties(), storeApplicationProperties() in SdaiSession
	static final String IO_IOFF = "An input-output function failed"; // SdaiSession
	static final String IO_APNF = "Application properties directory not found"; // SdaiSession
//   Server-client connection
	static final String NE_UNH  = "Host is unknown"; // SdaiSession, SdaiRepository, SdaiModel, SdaiTransaction
	static final String NE_IOEX = "Failure during input-output"; // SdaiSession, SdaiRepository, SdaiModel, SdaiTransaction, SchemaInstance
	static final String NE_FINF = "File with the name provided to server not found";
	static final String NE_NODR = "Directory for binary files cannot be created: check whether a file with this name already exists";
	static final String NE_NEDR = "Directory for binary files does not exist";
//   EntityValue
	static final String DI_ATNS = "Argument for attribute is null"; // When CExplicit_attribute attr == null
//   For "importClearTextEncoding"
			// General
	static final String RD_SCAN = "Exchange structure scanning error: "; // Is common for a group of scanner errors
	static final String RD_PARS = "Exchange structure parsing error: "; // Is common for a group of parser errors
	static final String RD_ERR = "Error: "; // Is common for a group of reader actions errors
//	static final String RD_PHFI = "   Exchange structure: ";
	static final String RD_MODL = "   SDAI model: ";
	static final String RD_INST = "   Instance: #";
	static final String RD_ATTR = "   Attribute: ";
	static final String RD_ENT  = "   Entity: ";
	static final String RD_SENT = "   Single entity data type: ";
	static final String RD_ENTF = "   Entity name found: ";
	static final String RD_ENTC = "   Corrected entity name: ";
	static final String RD_ENTS = "   Substituted by entity: ";
	static final String RD_IREF = "   Referenced instance: #";
	static final String RD_INSM = "   Missing instance: #";
	static final String RD_INSR = "   Referencing instance: #";
	static final String RD_ATHE = "   Attribute in header: ";
	static final String RD_SENA = "   Section name: ";
	static final String RD_FPOP = "   Instance of file_population: ";
	static final String RD_TOKE = "   Token expected: ";
	static final String RD_WTOK = "   Wrong token: ";
	static final String RD_ENTE = "   Entity expected: ";
	static final String RD_WSTR = "   String: ";
	static final String RD_FILE = "   File: ";
	static final String RD_SCHE = "   Schema: ";
	static final String RD_VENT = "   Validated entity: ";
	static final String RD_VINS = "   Validated instance: #";
			// Parser
	static final String RD_CORP = "comma or right parenthesis expected."; // embedded_list() in PhFileReader
	static final String RD_WCHA = "Warning: wrong character (0) was encountered, in an exchange structure only 8-bit bytes with decimal values 32 to 126 are allowed."; 
			// Reading instances
	static final String RD_UDEN = "Warning: user defined entity was encountered, instance was ignored."; // data_entity() in PhFileReader
	static final String RD_WODE = "Warning: wrong ordering of partial entity values in complex instance."; // object() in Create_instance
	static final String RD_MIPE = "Error: missing one or more partial entities, they were recovered."; // object() in Create_instance
	static final String RD_CONF = "Error: complex entity not available, please add it to .ce file and recreate your EXPRESS library."; // object() in Create_instance
	static final String RD_UNET = "Error: unknown entity data types "; // object() in Create_instance
	static final String RD_ENNF = "Error: entity data type in the schema not found."; // object() and takeBestFit() in Create_instance
	static final String RD_ZEID = "Error: instance with zero identifier (id) is not allowed, id was set to #"; // exchange_file() in PhFileReader
			// Reading values of entity instances
	static final String RD_DFEX = "Warning: discrepancies between physical file and express schema."; // parameters() in PhFileReader
	static final String RD_BINT = "Error: value of integer type expected, replaced by unset value."; // Value
	static final String RD_BREA = "Error: value of real type expected, replaced by unset value."; // Value
	static final String RD_BSTR = "Error: value of string type expected, replaced by null."; // Value
	static final String RD_BLOG = "Error: value of logical type expected, replaced by unset value."; // Value
	static final String RD_BBOO = "Error: value of boolean type expected, replaced by unset value."; // Value
	static final String RD_BEN1 = "Error: wrong enumerated value, replaced by unset value."; // Value
	static final String RD_BEN2 = "Error: value of enumeration type expected, replaced by unset value."; // Value
	static final String RD_BBIN = "Error: value of binary type expected, replaced by null."; // Value
	static final String RD_BREF = "Error: reference to entity expected, replaced by null."; // EntityValue
	static final String RD_BAGG = "Error: value of aggregate type expected, replaced by null."; // EntityValue
	static final String RD_BNAG = "Error: aggregate as an element of an outer aggregate expected, replaced by null."; // Value
	static final String RD_BVAT = "Error: wrong type of value, value set to null."; // Value
	static final String RD_INRE = "Error: wrong redefinition for integer, replaced by unset value."; // Value
	static final String RD_DORE = "Error: wrong redefinition for double, replaced by unset value."; // Value
	static final String RD_STRE = "Error: wrong redefinition for string, replaced by null."; // Value
	static final String RD_LORE = "Error: wrong redefinition for logical, replaced by unset value."; // Value
	static final String RD_BORE = "Error: wrong redefinition for boolean, replaced by unset value."; // Value
	static final String RD_ENRE = "Error: wrong redefinition for enumeration, replaced by unset value."; // Value
	static final String RD_BIRE = "Error: wrong redefinition for binary, replaced by null."; // Value
	static final String RD_RIRE = "Error: wrong redefinition for reference to instance, replaced by null."; // EntityValue
	static final String RD_SERE = "Error: wrong redefinition for value of select type, replaced by null."; // Value
	static final String RD_INSV = "Error: insufficient number of values, replaced by unset value."; // EntityValue
	static final String RD_UNSV = "Warning: unset value not allowed in SET, BAG, LIST (only in ARRAY)."; // EntityValue, Value, CAggregate
	static final String RD_EXAR = "Error: excessive value in ARRAY, it is ignored."; // Aggregate classes
	static final String RD_MINS = "Warning: reference to missing instance."; // InverseEntity
	static final String RD_WRRE = "Error: reference to an instance of wrong type, replaced by null."; // EntityValue
	static final String RD_BTYP = "Error: TYPED_PARAMETER expected, value replaced by null."; // Value
	static final String RD_TPRE = "Warning: TYPED_PARAMETER missing, it was recovered."; // Value
	static final String RD_REDV = "Warning: value is assigned to an attribute that is redefined."; // Value
	static final String RD_VADI = "Error: number of values differs from the number of attributes."; // entity_instance_RHS() in PhFileReader
	static final String RD_VLEA = "Error: number of values is less than the number of attributes."; // parameters() in PhFileReader
	static final String RD_VEXA = "Error: number of values exceeds the number of attributes."; // parameters() in PhFileReader
	static final String RD_ELEX = "Error: the number of aggregate elements exceeds its upper limit.";
	static final String RD_IMAG = "Error: illegal member of the aggregate, it is ignored.";
	static final String RD_MEIN = "   Member's index: ";
	static final String RD_MEAT = "   Member's actual type: ";
	static final String RD_MEDT = "   Member's required type: ";
         // Analysing of the strings
	static final String RD_WRST = "Error: incorrect string."; // analyse_string() in PhFileReader
	static final String RD_WRIS = "Error: incorrect string, trying to recover."; // analyse_string() in PhFileReader
	static final String RD_INCH = "Error: invalid character in string."; // analyse_string() in PhFileReader
	static final String RD_INCS = "Error: invalid character in string, trying to recover."; // analyse_string() in PhFileReader
	static final String RD_CDEM = "Warning: control directive for the end of the sequence of characters from ISO 10646 is missing."; // analyse_string() in PhFileReader
			// Header of the exchange structure
	static final String RD_DEHE = 
		"Error: value for 'description' in header section entity FILE_DESCRIPTION is missing, set to default."; // FILE_DESCRIPTION
	static final String RD_DWHE = 
		"Error: string value for 'description' in header section entity FILE_DESCRIPTION shall be enclosed into parentheses."; // FILE_DESCRIPTION
	static final String RD_DNHE = 
		"Error: value for 'description' in header section entity FILE_DESCRIPTION contains non-strings."; // FILE_DESCRIPTION
	static final String RD_DBHE = 
		"Error: wrong value for 'description' in header section entity FILE_DESCRIPTION, set to default."; // FILE_DESCRIPTION
	static final String RD_DLHE = 
		"Error: value for 'description' in header section entity FILE_DESCRIPTION is an empty list, default member is included."; // FILE_DESCRIPTION
	static final String RD_ILHE = 
		"Error: value for 'implementation_level' in header section entity FILE_DESCRIPTION is missing, set to default."; // FILE_DESCRIPTION
	static final String RD_IWHE = 
		"Error: wrong value for 'implementation_level' in header section entity FILE_DESCRIPTION, set to default."; // FILE_DESCRIPTION
	static final String RD_APHE = 
		"Error: string value for 'author' in header section entity FILE_NAME shall be enclosed into parentheses."; // FILE_NAME
	static final String RD_ABHE = 
		"Error: wrong value for 'author' in header section entity FILE_NAME, set to default."; // FILE_NAME
	static final String RD_ANHE = 
		"Error: value for 'author' in header section entity FILE_NAME contains non-strings."; // FILE_NAME
	static final String RD_ALHE = 
		"Error: value for 'author' in header section entity FILE_NAME is an empty list, default member is included."; // FILE_NAME
	static final String RD_OPHE = 
		"Error: string value for 'organization' in header section entity FILE_NAME shall be enclosed into parentheses."; // FILE_NAME
	static final String RD_OBHE = 
		"Error: wrong value for 'organization' in header section entity FILE_NAME, set to default."; // FILE_NAME
	static final String RD_ONHE = 
		"Error: value for 'organization' in header section entity FILE_NAME contains non-strings."; // FILE_NAME
	static final String RD_OLHE = 
		"Error: value for 'organization' in header section entity FILE_NAME is an empty list, default member is included."; // FILE_NAME
	static final String RD_PRHE = 
		"Error: value for 'preprocessor_version' in header section entity FILE_NAME is missing, set to default."; // FILE_NAME
	static final String RD_PWHE = 
		"Error: wrong value for 'preprocessor_version' in header section entity FILE_NAME, set to default."; // FILE_NAME
	static final String RD_OSHE = 
		"Error: value for 'originating_system' in header section entity FILE_NAME is missing, set to default."; // FILE_NAME
	static final String RD_OWHE = 
		"Error: wrong value for 'originating_system' in header section entity FILE_NAME, set to default."; // FILE_NAME
	static final String RD_AUHE = 
		"Error: value for 'authorization' in header section entity FILE_NAME is missing, set to default."; // FILE_NAME
	static final String RD_AWHE = 
		"Error: wrong value for 'authorization' in header section entity FILE_NAME, set to default."; // FILE_NAME
	static final String RD_TSHE = 
		"Error: value for 'time_stamp' in header section entity FILE_NAME is missing, set to the actual date and time."; // FILE_NAME
	static final String RD_WSPO = "Error: wrong section name in an instance of 'file_population'."; // create_schema_instances() in PhFileReader
	static final String RD_WCPO = "Error: wrong comment in an instance of 'file_population'."; // process_comment_file_population(), process_string() in PhFileReader
	static final String RD_WLFP = "Error: wrong or missing value of logical type in an instance of 'file_population', replaced by unset value."; // process_logical() in PhFileReader
	static final String RD_WDFP = "Error: wrong or missing digit in an instance of 'file_population', replaced by zero."; // process_digit() in PhFileReader
	static final String RD_WAFP = "Error: wrong aggregate in an instance of 'file_population'."; // process_aggregate() in PhFileReader
	static final String RD_UNSI = "Error: included schema instance from unknown repository in 'file_population'.";
	static final String RD_WRIL = "Warning: wrong implementation level in header section entity FILE_DESCRIPTION.";
	static final String RD_OSC1 = "Warning: exchange structure "; // PhFileReader
	static final String RD_OSC2 = " does not contain FILE_SCHEMA. "; // PhFileReader
//   Exporting exchange structures
	static final String WR_NOSC = "Error: no FILE_SCHEMA constructed."; // PhFileWriter
	static final String WR_OSC1 = "Warning: repository "; // PhFileWriter
	static final String WR_OSC2 = " contains no model. FILE_SCHEMA is ommitted. "; // PhFileWriter
//   undo_redo
	static final String UR_WRME = "   Wrong member in the aggregate of entity instances: ";
	static final String UR_NUME = "   Null member in the list of entity instances. ";
	static final String UR_MOPO = "   JSDAI data are modified while responding to fired SdaiEvent. ";
         // Methods for Express compiler
	static final String EC_INET = "Incompatible Express types."; // check() in Value
	static final String EC_TOF1 = "Error: built-in function 'typeOf' is applied in the context of the schema '";
	static final String EC_TOF2 = "' which does not contain declaration of the entity '";
	static final String EC_TOF3 = "'.";
	static final String EC_TOF4 = "' which does not contain declaration of the defined type '";
	static final String EC_CONR = "SDAI context to be set is required; use setSdaiContext(SdaiContext context) method in SdaiSession.";
         // Remote connection
	static final String RC_DAMI = "Warning: change date is not provided.";
			// Express expressions
	static final String EE_VADT = "   Declared value type: ";
	static final String EE_VAAT = "   Actual value type: ";
	static final String EE_VATP = "   Value type: ";
	static final String EE_VAJT = "   Value java type: ";
	static final String EE_NASA = "Error: standard aggregate not available, please report to jsdai@lksoft.com.";
	static final String EE_ILOP = "Error: an operand is of illegal type.";
	static final String EE_DIVZ = "Error: division by zero.";
	static final String EE_INTY = "Error: incomparable types of the operands.";
	static final String EE_BNUL = "Error: base is null and power is nonpositive.";
	static final String EE_BNPO = "Error: base is nonpositive and power is not a whole number.";
	static final String EE_WRST = "Error: string not a feasible value of enumeration.";
	static final String EE_WRCH = "Warning: wrong pattern matching character in LIKE operator; false is returned";
	static final String EE_QNRO = "Error: fully qualified attribute name in UsedIn is wrong - ";
	static final String EE_SCEM = "Error: SdaiContext with no schema attached. Use, for example, 'new SdaiContext(null, null, work_model)' thus providing schema that is underlying for 'work_model'."; // typeof method in Value
	static final String EE_WMOD = "SdaiContext with no working model attached. Use, for example, 'new SdaiContext(null, null, work_model)'."; // makeInstance method in Value
	static final String EE_CONC = "SdaiContext requires working model to be submitted."; // constructor in SdaiContext
	static final String EE_NINS = "Error: asked value is not an entity instance.";
	static final String EE_NVAL = "Error: the value is not provided (aggregate is expected).";
	static final String EE_NAGG = "Error: the value is not an aggregate as expected.";
	static final String EE_NATY = "Error: the declared type of the value is not an aggregation type.";
	static final String EE_AIOB = "Error: aggregate member index is out of bounds.";
	static final String EE_NOAT = "Error: attribute is not provided.";
	static final String EE_ATNE = "Error: attribute is not defined.";
	static final String EE_NOAD = "Error: data type cannot have attributes.";
	static final String EE_NOVA = "Error: value for attribute is not provided.";
	static final String EE_NTVA = "Error: the type of value is not indicated.";
	static final String EE_UNVA = "Error: value assigned to the variable of undetermined type.";
	static final String EE_INNF = "Error: integer value does not fit to the type of the variable.";
	static final String EE_DONF = "Error: double value does not fit to the type of the variable.";
	static final String EE_STNF = "Error: string value does not fit to the type of the variable.";
	static final String EE_LONF = "Error: logical value does not fit to the type of the variable.";
	static final String EE_ENNF = "Error: enumeration value does not fit to the type of the variable.";
	static final String EE_BINF = "Error: binary value does not fit to the type of the variable.";
	static final String EE_EINF = "Error: entity instance value does not fit to the type of the variable.";
	static final String EE_AGNF = "Error: aggregate value does not fit to the type of the variable.";
	static final String EE_IANF = "Error: instance aggregate value does not fit to the type of the variable.";
	static final String EE_VANF = "Error: value does not fit to the type.";
	static final String EE_VNLO = "Error: value is not of logical type.";
	static final String EE_INNP = "Error: index is not provided.";
	static final String EE_INNA = "Error: indexing operator is applied to a non-aggregate.";
	static final String EE_DTNA = "Error: data type is not allowed for range qualifier.";
	static final String EE_INZE = "Error: increment value is zero.";
	static final String EE_INTI = "Error: incorrect increment value type.";
	static final String EE_IVTI = "Error: incorrect type of the value to be incremented.";
	static final String EE_DEZE = "Error: decrement value is zero.";
	static final String EE_DETI = "Error: incorrect decrement value type.";
	static final String EE_DVTI = "Error: incorrect type of the value to be decremented.";
	static final String EE_VNAG = "Error: value is not of aggregation type.";
	static final String EE_NAVA = "Error: the value is not provided.";
	static final String EE_ITVA = "Error: incorrect type of the value.";
	static final String EE_ITVR = "Error: incorrect type of the variable for storing result.";
	static final String EE_NAAG = "Error: aggregate is not provided.";
	static final String EE_NBAG = "Error: the argument of Express function bag_to_set shall be of BAG OF GENERIC type.";
	static final String EE_OPUO = "Error: incompatible types of operands of union operator.";
	static final String EE_SODO = "Error: the second operand of difference operator is illegal.";
	static final String EE_ITDO = "Error: incompatible types of operands of difference operator.";
	static final String EE_SOIO = "Error: the second operand of intersection operator is illegal.";
	static final String EE_ITIO = "Error: incompatible types of operands of intersection operator.";
	static final String EE_WOSO = "Error: wrong type of an operand of subset operator.";
	static final String EE_EFNE = "Error: Express function is not defined at this value.";
	static final String EE_INQN = "Error: incorrect qualified name.";
	static final String EE_IDIN = "Error: incomplete data in the construction of an instance.";

	static final String MP_NAVL = "JSDAI Mapping Extension is not available";
	static final String RC_NAVL = "JSDAI DB Extension is not available";

//--------------



/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, String ent_name) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_ENT + ent_name);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_ENT + ent_name);
		}
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, String ent_name, 
		String ent_required, String substitute) 
			throws SdaiException {
		String str;
		if (ent_required == null) {
			str = AdditionalMessages.RD_ENTS + substitute;
		} else {
			str = AdditionalMessages.RD_ENTC + ent_required + SdaiSession.line_separator +
				AdditionalMessages.RD_ENTS + substitute;
		}
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_ENTF + ent_name + SdaiSession.line_separator + str);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_ENTF + ent_name + SdaiSession.line_separator + str);
		}
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, String attr_name) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_ATHE + attr_name);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_ATHE + attr_name);
		}
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text);
		} else {
			SdaiSession.println(text);
		}
	}










//----compiler generated exceptions:
	static final String EB_ENT = "Wrong attribute value type while reading physical file: entity instance expected";
	static final String EB_ENU = "Wrong attribute value type while reading physical file: enumeration expected";
	static final String EB_INT = "Wrong attribute value type while reading physical file: integer expected";
	static final String EB_NUM = "Wrong attribute value type while reading physical file: number (double) expected";
	static final String EB_REA = "Wrong attribute value type while reading physical file: real (double) expected";
	static final String EB_STR = "Wrong attribute value type while reading physical file: string expected";
	static final String EB_LOG = "Wrong attribute value type while reading physical file: logical (int) expected";
	static final String EB_BOO = "Wrong attribute value type while reading physical file: boolean (int) expected";
	static final String EB_BIN = "Wrong attribute value type while reading physical file: binary (string) expected";
	static final String EBB_ENT = "Wrong attribute value type while reading binary file: entity instance expected";
	static final String EBB_ENU = "Wrong attribute value type while reading binary file: enumeration expected";
	static final String EBB_INT = "Wrong attribute value type while reading binary file: integer expected";
	static final String EBB_NUM = "Wrong attribute value type while reading binary file: number (double) expected";
	static final String EBB_REA = "Wrong attribute value type while reading binary file: real (double) expected";
	static final String EBB_STR = "Wrong attribute value type while reading binary file: string expected";
	static final String EBB_LOG = "Wrong attribute value type while reading binary file: logical (int) expected";
	static final String EBB_BOO = "Wrong attribute value type while reading binary file: boolean (int) expected";
	static final String EBB_BIN = "Wrong attribute value type while reading binary file: binary (string) expected";
	static final String EBB_BIN_V = "Wrong data byte while reading binary file: binary attribute";
	static final String EBB_IO = "IO exception while reading/writing a binary file";
	static final String EB_SE_NC = "Select error: cannot get, not currently selected";
	static final String EB_NOS = "Do not remember what it is and where used";
	static final String EB_BADS = "Do not remember what it is and where used";


}
