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
import java.lang.reflect.Method;
import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.query.SchemaInstanceRef;
import jsdai.query.SerializableRef;
import jsdai.xml.InstanceReader;
import jsdai.xml.SdaiInputSource;

/**
 * The SchemaInstance class represents a logical collection of
 * <code>SdaiModel</code>s.

 * <p>
 * Conceptionally, the <code>SchemaInstance</code> can be represented by
 * the following EXPRESS entity:

 * <P><TT><pre>ENTITY SchemaInstance;
 *  {@link #getName name} : String;
 *  {@link #getAssociatedModels associatedModels} : <A HREF="../../jsdai/lang
/ASdaiModel.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/SdaiModel.html">SdaiModel</A>;
 *  {@link #getNativeSchema nativeSchema} : <A HREF="../../jsdai/dictionary
/ESchema_definition.html">ESchema_definition</A>;
 *  {@link #getRepository repository} : <A HREF="../../jsdai/lang
/SdaiRepository.html">SdaiRepository</A>;
 *  {@link #getChangeDate changeDate} : OPTIONAL String;
 *  {@link #getValidationDate validationDate} : String;
 *  {@link #getValidationResult validationResult} : int;
 *  {@link #getValidationLevel validationLevel} : int;
 *  {@link #getDescription description} : <A HREF="../../jsdai/lang/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getAuthor author}       : <A HREF="../../jsdai/lang/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getOrganization organization}       : <A HREF="../../jsdai/lang
/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getPreprocessorVersion preprocessorVersion} : String;
 *  {@link #getOriginatingSystem originatingSystem} : String;
 *  {@link #getAuthorization authorization} : String;
 *  {@link #getDefaultLanguage defaultLanguage} : OPTIONAL String;
 *  {@link #getContextIdentifiers contextIdentifiers}     : <A HREF="../../jsdai
/lang/A_string.html">LIST</A> [0:?] OF String;
 *UNIQUE
 *  UR1: name, repository;
 *WHERE
 *  WR1: SELF IN {@link SdaiRepository#getSchemas SELF.repository.schemas};
 *END_ENTITY;</TT></pre>
 * <P>

 * All attributes are read-only, and for each of them a corresponding
 * <code>get</code> method (or methods sometimes) is defined.
 * Both the <code>changeDate</code> and <code>validationDate</code>
 * can be retrieved either as <code>String</code> or as <code>long</code>.
 * Similarly, <code>nativeSchema</code> can be retrieved
 * either as <code>ESchema_definition</code> or as <code>String</code>.
 * For <code>preprocessorVersion</code>, <code>originatingSystem</code>, <code>authorization</code>,
 * and <code>defaultLanguage</code> the corresponding <code>set</code> methods are given.

 * <p>
 * A <code>SchemaInstance</code> belongs to an <code>SdaiRepository</code>
 * and is available only when the <code>SdaiRepository</code> is open.
 * Closing an <code>SdaiRepository</code> makes its <code>SchemaInstance</code>s invalid.
 * The associated <code>SdaiModel</code>s can belong to different
 * <code>SdaiRepositories</code>. The underlying EXPRESS schema of each
 * <code>SdaiModel</code> associated with some <code>SchemaInstance</code> needs
 * to be domain equivalent with the native schema of this <code>SchemaInstance</code>.
 * The <code>SchemaInstance</code>s define the domain over which to perform validation
 * operations: for global rules, uniqueness rules, and instance references.
 * @see "ISO 10303-22::8.4.1 schema_instance"
 */
public abstract class SchemaInstance extends SdaiCommon implements SdaiEventSource, QuerySource {

//	private static class Move extends jsdai.util.Move {
//		private static SdaiRepository moveSchemaInstanceNoDelete(SchemaInstance si, SdaiRepository repo) throws SdaiException {
//			return moveSchemaInstanceInternal(si, repo, false);
//		}
//	}

/**
	EXPRESS attributes of SchemaInstance, see ISO 10303-22::8.4.1 schema_instance.
*/
	String name;
	ASdaiModel associated_models;
	CSchema_definition native_schema;
	SdaiRepository repository;
	long change_date;
	long validation_date;
	int validation_result;
	int validation_level;

/**
	EXPRESS attribute of the header entity 'file_description', see ISO 10303-21::8.2.1.
*/
	A_string description;

/**
	EXPRESS attributes of the header entity 'file_name', see ISO 10303-21::8.2.2.
*/
	A_string author;
	A_string organization;
	String preprocessor_version;
	String originating_system;
	String authorization;

/**
	It is renamed attribute 'default_language' of the header entity 'section_language',
	see new edition of ISO 10303-21, clause 8.2.5.
*/
	String language;

/**
	EXPRESS attribute of the header entity 'section_context', see new edition of
	ISO 10303-21, clause 8.2.6.
*/
	A_string context_identifiers;

	protected ASchemaInstance included_schemas;

/**
	Data dictionary model describing the same Express schema whose definition
	is given by the field 'native_schema'. In particular, the value of this
	field can be obtained from this model.
*/
	SdaiModel dictionary;

/**
	Data dictionary model describing the Express schema for which this
	schema instance is created. Each Express schema having a directory in
	JSDAI library has its 'own' schema instance created during
	<code>openSession</code>.
*/
	SdaiModel defining_schema_model;

/**
	An indicator used to set values for attributes of header entities
	when reading an exchange structure (method importClearTextEncoding).
	The value is 1 if schema instance is created during this process
	(in class PhFileReader) and the above mentioned values need to be set.
	The value is 2 if the above values already are set; this happens at
	the end of execution of moveDataAfterImportClearTextEncoding method
	(in this class).
*/
	int moved_data_status;

/**
	Has value 'true' if and only if something in the schema instance
	is modified (renamed, model added or removed, value to some field
	is assigned and so on).
	The value is switched to 'false' during the commit operation.
*/
	boolean modified;

/**
	Has value 'true' if modifications in the schema instance are
	because of its preparation during the import from the exchange
	structure (part 21 file).
	The value is switched to 'false' during first commit operation.
*/
	boolean modified_by_import;

/**
	Has value 'true' if the data of this SchemaInstance have been written
	to binary file (either in the current session using commit operation
	of SdaiTransaction or in previous sessions; in the latter case
	'true' is set within openRepository method).
*/
	boolean committed;

/**
	An auxiliary variable used during loading a repository binary file.
*/
	boolean exists;

/**
	Has value 'true' when adding/removing of models to/from this
	SchemaInstance is allowed. This takes place only for
	"SDAI_DICTIONARY_SCHEMA_INSTANCE" and "SDAI_MAPPING_SCHEMA_INSTANCE"
	in SystemRepository and only restricted time period during 'open session'
	operation.
*/
	boolean allow_model;

/**
	Has value "true" only when this schema instance was created after the
	last invocation of commit or abort operation, whichever of them
	appeared more recently.
*/
	boolean created;

	Class [] param;
	Object [] arg;
	static final String WHERE_RULE_METHOD_NAME_PREFIX = "r";
	int aux;
	SdaiIterator iter = null;
	EAttribute [] attributes = null;
	boolean [] flag = null;
	Value [] values [];
	private final int ATTRIBUTES_ARRAY_SIZE = 8;
	private final int INSTANCES_ARRAY_SIZE = 128;
	ASdaiModel models_dom = null;
	int count_insts;
	int ren_count;

	/**
	 * The aggregate containing instances of SdaiListener class.
	 */
	protected CAggregate listenrList;

//	static final boolean CATCH_EXCEPTIONS = false;
	static final boolean CATCH_EXCEPTIONS = true;
	static final int OUTER_MODELS_ARRAY_SIZE = 32;
	static final int DUPL_MODELS_ARRAY_SIZE = 8;
	static final int RENUMB_INSTS_ARRAY_SIZE = 256;
	static final int SAVED_NAMES_ARRAY_SIZE = 16;


	/**
	 * This method returns null. Remote schema instance has to override this method.
	 * @return null
	 */
	protected SchemaInstanceRemote getSchInstRemote() {
		 return null;
	}

	/**
	 * This mehod does nothing. Remote schema instance has to override this method.
	 */
	protected void setSchInstRemote(SchemaInstanceRemote sRemote) {
	}

/**
	Returns an owner of this SchemaInstance.
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
	Used in the following contexts:
	- in createSchemaInstance method;
	- to create schema instances with the names "SDAI_DICTIONARY_SCHEMA_INSTANCE"
	  and "SDAI_MAPPING_SCHEMA_INSTANCE" in SystemRepository.
*/
	protected SchemaInstance(String given_name, CSchema_definition schema,
							 SdaiRepository given_repository) throws SdaiException {
		repository = given_repository;
		initLocal(given_name, schema);
	}

	protected void initLocal(String given_name, CSchema_definition schema) throws SdaiException {
		name = given_name;
		native_schema = schema;
		initializeSchemaInstance();
	}


/**
	Used to create a SchemaInstance when reading a repository binary file.
*/
	protected SchemaInstance(String given_name, SdaiRepository given_repository,
							 SdaiModel given_dictionary) throws SdaiException {
		repository = given_repository;
		initLocal(given_name, given_dictionary);
	}

	protected void initLocal(String given_name, SdaiModel given_dictionary) throws SdaiException {
		name = given_name;
		dictionary = given_dictionary;
		initializeSchemaInstance();
	}


/**
	Used to create schema instances corresponding to the Express schemas
	in JSDAI library, while reading binary file 'repository' produced by
	Express compiler. More specifically, this constructor is invoked in
	method loadSchemaInstances in class SdaiRepository.
*/
	protected SchemaInstance(String given_name, SdaiRepository given_repository,
							 SdaiModel given_dictionary, SdaiModel given_schema_model
							 ) throws SdaiException {
		repository = given_repository;
		initLocal(given_name, given_dictionary, given_schema_model);
	}

	protected void initLocal(String given_name, SdaiModel given_dictionary,
							 SdaiModel given_schema_model) throws SdaiException {
		name = given_name;
		dictionary = given_dictionary;
		defining_schema_model = given_schema_model;
		initializeSchemaInstance();
	}

	// Noop constructor for subclass creation. If this constructor is used
	// the object has to be initialized by initLocal(...)
	protected SchemaInstance(SdaiRepository given_repository) {
		repository = given_repository;
	}

/**
	Initializes the fields of this class. Invoked only in the constructors.
*/
	private void initializeSchemaInstance() throws SdaiException {
//		associated_models = new ASdaiModel(SdaiSession.setType0toN, this);
		validation_date = change_date = System.currentTimeMillis();
		validation_result = 3;
		validation_level = 0;
		modified = false;
		committed = false;
		allow_model = false;
		moved_data_status = 0;
		preprocessor_version = " ";
		originating_system = " ";
		authorization = " ";
	}


/**
 * Returns the name of the schema instance as a <code>String</code>.
 * <p> The schema instances within a <code>SdaiRepository</code> must have unique names.
 * @return the name of this <code>SchemaInstance</code>.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public String getName() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (name == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return name;
	}


/**
 * Returns an aggregate of strings <code>A_string</code> containing optional
 * descriptions about the <code>SchemaInstance</code>.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this aggregate within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated contains strings taken from the field
 * <code>description</code> of the entity
 * <code>file_description</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.1 file_description).
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * @return aggregate containing informal description of this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.1 file_description."
 */
	public A_string getDescription() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (description == null) {
			description = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return description;
	}


/**
 * Returns an aggregate of strings <code>A_string</code> identifying the person
 * responsible for creating <code>SdaiModel</code>s associated with
 * this schema instance. The aggregate shall have its name at the
 * first position and mail and/or email addresses at the following
 * positions.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this aggregate within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated contains strings taken from the field <code>author</code>
 * of the entity <code>file_name</code> in the header of the exchange
 * structure (see ISO 10303-21::8.2.2 file_name).
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * It is a responsibility of the application to fill this aggregate with
 * useful information.
 * @return aggregate identifying an author of the <code>SdaiModel</code>s
 * associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public A_string getAuthor() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (author == null) {
			author = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return author;
	}


/**
 * Returns an aggregate of strings <code>A_string</code> containing the
 * organizations with whom the author of this schema instance is associated.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this aggregate within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated contains strings taken from the field <code>organization</code>
 * of the entity <code>file_name</code> in the header of the exchange
 * structure (see ISO 10303-21::8.2.2 file_name).
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * It is a responsibility of the application to fill this aggregate with
 * useful information.
 * @return aggregate that lists organizations an author of this schema instance is associated.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public A_string getOrganization() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (organization == null) {
			organization = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return organization;
	}


/**
 * Returns a <code>String</code> characterizing the system used to
 * build <code>SdaiModel</code>s associated with this schema instance.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this string within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated has value taken from the field
 * <code>preprocessor_version</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * @return string describing the system used to build
 * <code>SdaiModel</code>s associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setPreprocessorVersion
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getPreprocessorVersion() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return preprocessor_version;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the system used to build <code>SdaiModel</code>s associated with
 * this schema instance.
 * This assignment is allowed only if the repository containing
 * this schema instance is open.
 * @param value string describing the system used to build <code>SdaiModel</code>s
 * associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getPreprocessorVersion
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setPreprocessorVersion(String value) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		preprocessor_version = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> characterizing the system from
 * which the data contained in <code>SdaiModel</code>s
 * associated with this schema instance are originated.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this string within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated has value taken from the field
 * <code>originating_system</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * @return string describing the system that is a source of the data
 * in <code>SdaiModel</code>s associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setOriginatingSystem
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getOriginatingSystem() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return originating_system;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the system from which the data contained in <code>SdaiModel</code>s
 * associated with this schema instance are originated.
 * This assignment is allowed only if the repository containing
 * this schema instance is open.
 * @param value string describing the system that is a source of the data
 * in <code>SdaiModel</code>s associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getOriginatingSystem
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setOriginatingSystem(String value) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		originating_system = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> containing the name and mailing address
 * of the person who authorizes the data contained in <code>SdaiModel</code>s
 * associated with this schema instance.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this string within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated has value taken from the field
 * <code>authorization</code> of the entity <code>file_name</code> in
 * the header of the exchange structure (see ISO 10303-21::8.2.2 file_name).
 * @return string describing the person who authorizes this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setAuthorization
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getAuthorization() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return authorization;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the person who authorizes the data contained in <code>SdaiModel</code>s
 * associated with this schema instance.
 * This assignment is allowed only if the repository containing
 * this schema instance is open.
 * @param value string describing the person who authorizes this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getAuthorization
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setAuthorization(String value) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		authorization = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> identifying the default language
 * for string values in those <code>SdaiModel</code>s associated
 * with this schema instance, for which their own default language
 * is not specified. More precisely, for a model, the default language
 * can be assigned individually. <code>String</code> returned by this
 * method serves as a default language for those models for which
 * an individual assignment was not made.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this string within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated can take value extracted from the header of the exchange structure
 * (see ISO 10303-21::8.2.4 section_language) provided
 * an appropriate value is given there (otherwise it is set to null).
 * @return <code>String</code> identifying the default language for string
 * values in <code>SdaiModel</code>s associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setDefaultLanguage
 * @see SdaiModel#getDefaultLanguage
 * @see SdaiModel#setDefaultLanguage
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public String getDefaultLanguage() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return language;
	}


/**
 * Assigns a <code>String</code> to the attribute identifying
 * the default language for string values in those <code>SdaiModel</code>s
 * associated with this schema instance,
 * for which their own default language is not specified.
 * This assignment is allowed only if the repository containing
 * this schema instance is open.
 * @param value <code>String</code> identifying the default language for string
 * values in <code>SdaiModel</code>s associated with this schema instance.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #getDefaultLanguage
 * @see SdaiModel#getDefaultLanguage
 * @see SdaiModel#setDefaultLanguage
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public void setDefaultLanguage(String value) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		language = value;  // use value=null for unset!
		modified = true;
	}


/**
 * Returns an aggregate <code>A_string</code> containing information
 * describing the contexts within which the instances in the
 * <code>SdaiModel</code>s associated with this schema instance are applicable.
 * Both the schema instance and each model associated with it have
 * their own aggregates <code>A_string</code> for writing context identifiers.
 * But for those models, for which their own aggregate is empty,
 * contexts are described by the aggregate returned by this method,
 * that is, by the schema instance aggregate.
 * <p> After invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * this aggregate within each <code>SchemaInstance</code> with which
 * at least one <code>SdaiModel</code> in the resulting repository
 * is associated will contain information extracted from the header of the
 * exchange structure (see ISO 10303-21::8.2.5 section_context) provided
 * an appropriate information is given there.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * @return aggregate containing information describing the contexts.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see SdaiModel#getContextIdentifiers
 * @see "ISO 10303-21::8.2.5 section_context."
 */
	public A_string getContextIdentifiers() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return context_identifiers;
	}


	/**
	 * Returns this schema instance effective permission which determines
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
	 * has no read access to this schema instance. Otherwise this method returns
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
	 * has no write access to this schema instance. Otherwise this method returns
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
	Method is used to set values for attributes of header entities
	when reading an exchange structure (method importClearTextEncoding).
	It is invoked only once, in the class PhFileReader, for a schema
	instances for which the indicator 'moved_data_status' has value 1.
*/
	void moveDataAfterImportClearTextEncoding() throws SdaiException {
		int i;
		String str;
		A_string descr = repository.description;
		if (description == null) {
			description = new A_string(SdaiSession.listTypeSpecial, this);
		}
		for (i = 1; i <= descr.myLength; i++) {
			str = descr.getByIndex(i);
			description.addByIndexPrivate(i, str);
		}
		A_string auth = repository.file_name.author;
		if (author == null) {
			author = new A_string(SdaiSession.listTypeSpecial, this);
		}
		for (i = 1; i <= auth.myLength; i++) {
			str = auth.getByIndex(i);
			author.addByIndexPrivate(i, str);
		}
		A_string org = repository.file_name.organization;
		if (organization == null) {
			organization = new A_string(SdaiSession.listTypeSpecial, this);
		}
		for (i = 1; i <= org.myLength; i++) {
			str = org.getByIndex(i);
			organization.addByIndexPrivate(i, str);
		}
		preprocessor_version = repository.file_name.preprocessor_version;
		originating_system = repository.file_name.originating_system;
		authorization = repository.file_name.authorization;
		language = repository.language;
		A_string con_id = repository.context_identifiers;
		if (con_id != null) {
			if (context_identifiers == null) {
				context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
			}
			for (i = 1; i <= con_id.myLength; i++) {
				str = con_id.getByIndex(i);
				context_identifiers.addByIndexPrivate(i, str);
			}
		}
		moved_data_status = 2;
	}


/**
 * Returns the set of <code>SdaiModel</code>s associated with this
 * schema instance.
 * An <code>SdaiModel</code> may be associated with no, one or
 * several schema instances.
 * @return the set of models associated with this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 */
	public abstract ASdaiModel getAssociatedModels() throws SdaiException;


	protected void check_schemaInstance() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
	}


	ASdaiModel getAssociatedModelsPrivate() throws SdaiException {
		return getAssociatedModelsPrivateCommon();
	}


	abstract protected ASdaiModel getAssociatedModelsPrivateCommon() throws SdaiException;


	protected ASdaiModel getAssociatedModelsRemote() {
		return associated_models;
	}

	protected void setAssociatedModelsRemote(ASdaiModel associated_models) {
		this.associated_models = associated_models;;
	}


	protected ASdaiModel createAssociatedModelsRemote() throws SdaiException {
		associated_models = new ASdaiModel(SdaiSession.setType0toN, this);
		return associated_models;
	}


	protected int get_known_servers_length() {
		return repository.session.known_servers.myLength;
	}


	protected SdaiRepository get_repo_in_known_servers(int index) {
		return (SdaiRepository)repository.session.known_servers.myData[index];
	}


	protected int get_models_aggregate_length(ASdaiModel mods) {
		return mods.myLength;
	}

	protected SdaiModel get_model_from_aggregate(ASdaiModel mods, int index) {
		return (SdaiModel)mods.myData[index];
	}


	protected boolean explore_model(SdaiModel mod) throws SdaiException {
		if (mod.underlying_schema == null && mod.dictionary == null) {
			return false;
		} else {
			addSdaiModel(mod);
			return true;
		}
	}


	protected SdaiModel create_virtual_model_in_repo(SdaiRepository repo, String mod_name) throws SdaiException {
		return repo.createVirtualModel(mod_name);
	}

	protected boolean is_modified_outside_contents(SdaiModel mod) {
		return mod.modified_outside_contents;
	}

	protected void set_modified_outside_contents(SdaiModel mod, boolean modif) {
		mod.modified_outside_contents = modif;
	}

	protected boolean get_modified() {
		return modified;
	}

	protected void set_modified(boolean modif) {
		modified = modif;
	}

/**
 * Returns the name of the schema upon which this schema instance is based.
 * @return the name of the native schema for this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getNativeSchema
 * @see #setNativeSchema(ESchema_definition)
 * @see #setNativeSchema(Class)
 */
	public String getNativeSchemaString() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (native_schema == null) {
			if (dictionary == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			return dictionary.name.substring(0, dictionary.name.length() - SdaiSession.ENDING_FOR_DICT);
		}
		return native_schema.getName(null);
//		} // syncObject
	}


/**
 * Returns definition of the schema upon which this schema instance is based.
 * @return native schema definition for this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getNativeSchemaString
 * @see #setNativeSchema(ESchema_definition)
 * @see #setNativeSchema(Class)
 */
	public ESchema_definition getNativeSchema() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (native_schema == null) {
			if (dictionary == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
				dictionary.startReadOnlyAccess();
			}
			native_schema = dictionary.described_schema;
			if (native_schema == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		return (ESchema_definition)native_schema;
//		} // syncObject
	}


/**
 * Puts definition of the schema upon which this schema instance will be based.
 * As a consequence of this operation, new <code>change_date</code> is set.
 * Passing null value to the method's
 * parameter results in SdaiException SD_NDEF.
 * @param schema_def new native schema definition for this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @see #getNativeSchema
 * @see #getNativeSchemaString
 * @see #setNativeSchema(Class)
 */
	public void setNativeSchema(ESchema_definition schema_def) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (schema_def == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		native_schema = (CSchema_definition)schema_def;
		change_date = System.currentTimeMillis();
//		} // syncObject
	}


/**
 * Puts definition of the Express schema upon which this schema instance will be based.
 * As a consequence of this operation, new <code>change_date</code> is set.
 * The value submitted to the parameter during invocation of the method shall be
 * a special java class with the name constructed from the schema name. This class
 * is contained in the package corresponding to the schema of interest.
 * For example, if the schema name is "geometry_schema", then the package name is
 * "jsdai.SGeometry_schema" and the value for the parameter shall
 * be "jsdai.SGeometry_schema.SGeometry_schema.class".
 * Passing null value to the method's
 * parameter results in SdaiException SD_NDEF.
 * @param schema_def Java class for the Express schema which has to be
 * established as a native schema for this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @see #getNativeSchema
 * @see #getNativeSchemaString
 * @see #setNativeSchema(ESchema_definition)
 */
	public void setNativeSchema(Class schema_def) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (schema_def == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		CSchema_definition def = repository.session.getSchemaDefinition(schema_def);
		if (def == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		native_schema = def;
		change_date = System.currentTimeMillis();
//		} // syncObject
	}


/**
 * Returns the <code>SdaiRepository</code> to which this schema instance belongs.
 * @return the owning repository.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 */
	public SdaiRepository getRepository() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return repository;
	}


/**
 * Checks if the creation date or date of the most recent
 * modification of the aggregate "associated_models" was set.
 * @return <code>true</code> if the date was set, and
 * <code>false</code> otherwise.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @see #getChangeDate
 * @see #getChangeDateLong
 */
	public boolean testChangeDate() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (change_date <= 0) {
			return false;
		} else {
			return true;
		}
	}


/**
 * Returns the date and time when this <code>SchemaInstance</code> was
 * created or its aggregate "associated_models" most recently modified.
 * @return a <code>String</code> representing the date and time.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #testChangeDate
 * @see #getChangeDateLong
 */
	public String getChangeDate() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
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
 * this <code>SchemaInstance</code> was created or its aggregate
 * "associated_models" most recently modified.
 * @return a <code>long</code> representing the date and time.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #testChangeDate
 * @see #getChangeDate
 */
	public long getChangeDateLong() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (change_date > 0) {
			return change_date;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Returns the date of the most recent invocation of the
 * <code>validateSchemaInstance</code> method for this schema instance.
 * @return a <code>String</code> representing the date and time.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getValidationDateLong
 * @see #validateSchemaInstance
 */
	public String getValidationDate() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (validation_date > 0) {
			return repository.session.cal.longToTimeStamp(validation_date);
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
//		} // syncObject
	}


/**
 * Returns a <code>long</code> value of the date of the
 * most recent invocation of the <code>validateSchemaInstance</code>
 * method for this schema instance.
 * @return a <code>long</code> representing the date and time.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getValidationDate
 * @see #validateSchemaInstance
 */
	public long getValidationDateLong() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (validation_date > 0) {
			return validation_date;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Gives the result returned by the most recent invocation of the
 * <code>validateSchemaInstance</code> method for this schema instance.
 * @return an <code>int</code> value representing the result of validation.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @see #validateSchemaInstance
 */
	public int getValidationResult() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return validation_result;
	}


/**
 * Returns the level of expression evaluation for validation.
 * @return an <code>int</code> value representing the level of validation.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @see #validateSchemaInstance
 */
	public int getValidationLevel() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		return validation_level;
	}


/**
 * Returns definition of the Express schema represented by this schema instance.
 * Each Express schema having a directory in JSDAI library has its 'own'
 * schema instance created during <code>openSession</code>.
 * The name of the schema instance is the same as that of the related
 * Express schema.
 * The set <code>associated_models</code> of such a schema instance
 * for an Express schema consists of all data dictionary models containing
 * definition of at least one entity (or defined type) either defined,
 * or declared(through USED or REFERENCED) or
 * implicitly interfaced in this Express schema.
 * All such schema instances belong to SystemRepository.
 * If method is applied to schema instance of different kind,
 * then SdaiException FN_NAVL is thrown.
 * @return schema definition for the Express schema represented
 * by this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public ESchema_definition getDefiningSchema() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (defining_schema_model == null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (defining_schema_model.described_schema == null) {
			defining_schema_model.startReadOnlyAccess();
		}
		if (defining_schema_model.described_schema == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return (ESchema_definition)defining_schema_model.described_schema;
//		} // syncObject
	}

	/**
	 * Returns the name of the user that has placed exclusive lock
	 * on remote schema instance. This method always returns
	 * <code>null</code> for local schema instance.
	 * @return the name of the user that has the exclusive lock or
	 *         null if there is no exclusive lock on the schema instance
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
 * Deletes this <code>SchemaInstance</code>.
 * <p> Deleting of a <code>SchemaInstance</code> from "SystemRepository"
 * is forbidden. In this case, SdaiException VT_NVLD is thrown.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see "ISO 10303-22::10.6.1 Delete schema instance"
 */
	public abstract void delete() throws SdaiException;


	protected void deleteCommon() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		session.active_transaction.registerDeletedSchemaInstance(this);

//		repository.schemas.removeUnorderedRO(this);
		repository.schemas.removeSchemaUnorderedKeepSorted(this);
		repository.schema_instance_deleted = true;
		if (associated_models != null) {
			for (int i = 0; i < associated_models.myLength; i++) {
				SdaiModel model = (SdaiModel)associated_models.myData[i];
				model.associated_with.removeUnorderedRO(this);
			}
			associated_models = null;
		}
	}


	protected void unset_repo_for_remote(boolean decrease_unresolved) throws SdaiException {
		if (decrease_unresolved) {
			repository.unresolved_sch_count--;
		}
		repository = null;
	}


/**
 * Assigns a new name to this <code>SchemaInstance</code>.
 * This new name must differ from the names of other schema instances in the
 * same <code>SdaiRepository</code>. If this condition is violated, then
 * SdaiException SI_DUP is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <p> Renaming of a <code>SchemaInstance</code> from "SystemRepository"
 * is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param provided_name new name of this schema instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException SI_DUP, schema instance duplicate.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see "ISO 10303-22::10.6.2 Rename schema instance"
 */
	public void rename(String provided_name) throws SdaiException {
		String name_in_repository;

//		synchronized (syncObject) {
		if (provided_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (provided_name.length() == 0) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		SdaiSession session = repository.session;
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (repository == repository.session.systemRepository) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if((repository.location!=null)&& (((String)repository.location).startsWith(session.LOCATION_PREFIX))) {
			try {  //Checking for access rigths to repository
				repository.ostream.writeByte('W');
				if(repository.istream.readByte()==0) {
					throw new SdaiException(SdaiException.RP_RO);
				}
				repository.ostream.writeUTF(name);
				int result = repository.istream.readByte();
				if(result==0) {
					throw new SdaiException(SdaiException.RP_RO);
				}
				if(result==-3) {
					throw new SdaiException(SdaiException.SI_NEXS);
				}
				if(result == -5) {
					throw new SdaiException(SdaiException.RP_LOCK);
				}
				repository.ostream.writeUTF(provided_name);
			}  catch (IOException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}

		repository.internal_usage = true;
		SchemaInstance sch_inst = repository.findSchemaInstance(provided_name);
		repository.internal_usage = false;
		if (sch_inst != null) {
			throw new SdaiException(SdaiException.SI_DUP, sch_inst);
		}
		repository.correctSchemaInstancePosition(name, provided_name);
		name = provided_name;
		modified = true;
/*		if(repository.location!=null) {
			if(((String)repository.location).startsWith(session.LOCATION_PREFIX)) {
				repository.saveContentsStream(repository.ostream);
			}
		}	*/
//		} // syncObject
	}


/**
 * Adds a specified <code>SdaiModel</code> to the set of models
 * that are associated with this schema instance.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * <p> Adding a model to a <code>SchemaInstance</code> in "SystemRepository"
 * is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param model the <code>SdaiModel</code> submitted to associate with
 * this schema instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see "ISO 10303-22::10.6.3 Add SDAI-model"
 */
	public abstract void addSdaiModel(SdaiModel model) throws SdaiException;


	protected void addSdaiModelCommon(SdaiModel model) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (model == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if (associated_models == null) {
			getAssociatedModels();
		}
		associated_models.addUnorderedRO(model);
		model.addAssociatedWith(this);
		modified = true;
	}

	protected void addSdaiModelFast(SdaiModel model) throws SdaiException {
		associated_models.addUnorderedRO(model);
	}


/**
 * Removes a specified <code>SdaiModel</code> from the set of models
 * that are associated with this schema instance.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * <p> Removing a model from a <code>SchemaInstance</code> in "SystemRepository"
 * is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param model the <code>SdaiModel</code> that is to be removed
 * from the set "associated_models" of this schema instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see "ISO 10303-22::10.6.4 Remove SDAI-model"
 */
	public void removeSdaiModel(SdaiModel model) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (model == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (repository == repository.session.systemRepository && !allow_model) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}

		if (associated_models == null) {
			getAssociatedModels();
		}
		associated_models.removeUnorderedRO(model);
		model.removeAssociatedWith(this, true);
		modified = true;
//		} // syncObject
	}

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
	public int query(String where, EEntity entity, AEntity result) throws SdaiException 	{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		return getAssociatedModels();
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		return null;
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		SchemaInstanceRemote schInstRemote = getSchInstRemote();
        if(schInstRemote != null) {
			return new SchemaInstanceRef(schInstRemote.getRemoteRepository().getRemoteId(),
										 schInstRemote.getRemoteId());
        } else {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		return getQuerySourceInstanceRef();
	}

/**
 * Determines if the submitted global rule is satisfied by the population
 * associated with this <code>SchemaInstance</code>. Validation is performed
 * for all entity instances of the entity types to which the specified global
 * rule applies in all <code>SdaiModel</code>s associated with the schema instance.
 * @param rule the global rule to validate
 * @param nonConf the non-persistent list to which those instances of the
 * entity <code>where_rule</code> within the validated global rule to which they did not
 * conform are appended (if the return value is 1)
 * @return number 2 if global rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #validateGlobalRule(String rule, AWhere_rule nonConf)
 * @see "ISO 10303-22::10.6.5 Validate global rule"
 */
	public int validateGlobalRule(EGlobal_rule rule, AWhere_rule nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (rule == null) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (repository == session.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}

		SdaiModel rule_owner = ((CEntity)rule).owning_model;
		if (rule_owner.described_schema != native_schema) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		int ret_val = ELogical.TRUE;
		GlobalRule grule = (GlobalRule)rule;
		String [] bp_rules_names = null;                     // added for Bug #2739
		int br_count = grule.getBypassedRulesCount(session); // added for Bug #2739
		if (br_count > 0) {                                  // added for Bug #2739
			bp_rules_names = grule.getBypassedRules();         // added for Bug #2739
		}                                                    // added for Bug #2739

		Class rule_class = grule.getRuleClass();
		Object rule_object = grule.getRuleObject();

		AWhere_rule where_rules = rule.getWhere_rules(null, null);
		CWhere_rule [] w_rules_ord = ((GlobalRule)rule).getWhereRules(where_rules);
//System.out.println("SchemaInstance    where_rules aggregation type: " + ((AEntity)where_rules).myType +
//"    count: " + where_rules.getMemberCount() + "   global rule: " + rule.getName(null));
		if (param == null) {
			param = new Class[1];
			param[0] = SdaiContext.class;
			arg = new Object [1];
		}
		if (repository.session.sdai_context == null) {
			throw new SdaiException(SdaiException.SY_ERR, "SdaiContext shall be provided");
		}
		ASdaiModel domain = repository.session.sdai_context.domain;
		Method meth;
//		ListElement element = (ListElement)((AEntity)where_rules).myData[0];
//		while (element != null) {
		for (int i = 0; i < ((AEntity)where_rules).myLength; i++) {
//			CWhere_rule w_rule = (CWhere_rule)element.object;
			CWhere_rule w_rule = w_rules_ord[i];
			if (br_count > 0 && ((WhereRule)w_rule).isRuleBypassed(bp_rules_names, br_count)) {  // added for Bug #2739
				continue;                                                                          // added for Bug #2739
			}                                                                                    // added for Bug #2739
			String m_name = WHERE_RULE_METHOD_NAME_PREFIX + CEntity.normalise(w_rule.getLabel(null));
			try {
				meth = rule_class.getDeclaredMethod(m_name, param);
			} catch (java.lang.NoSuchMethodException ex) {
				throw new SdaiException(SdaiException.SY_ERR, "Method not found: " + m_name);
			}
			if(associated_models == null) {
				getAssociatedModels();
			}
			repository.session.sdai_context.domain = associated_models;
			SdaiContext contxt = repository.session.getSdaiContext();
			arg[0] = contxt;
//System.out.println("SchemaInstance   global rule: " + rule.getName(null) + "   where rule: " + w_rule.getLabel(null));
			Object res;
			int check_result;
			if (contxt != null && contxt.aggr_size != 0) {
				contxt.aggr_size = 0;
			}
//long time1=0, time2=0, time3, time4, time5;
			try {
//time1 = System.currentTimeMillis();
				res = meth.invoke(rule_object, arg);
				check_result = ((Integer)res).intValue();
//time2 = System.currentTimeMillis();
//time3=time2-time1;
//System.out.println("+++ where rule: " + w_rule.getLabel(null) + "time =" + time3);
			} catch (Exception ex) {
				if (CATCH_EXCEPTIONS && ex instanceof java.lang.reflect.InvocationTargetException) {
//					Exception tex = (Exception)((java.lang.reflect.InvocationTargetException)ex).getTargetException();
					Object tex = ((java.lang.reflect.InvocationTargetException)ex).getTargetException();
					if (tex instanceof SdaiException) {
//						((WhereRule)w_rule).exc = tex;
						((WhereRule)w_rule).exc = (SdaiException)tex;
					} else if (tex instanceof StackOverflowError) {
						((WhereRule)w_rule).exc = new StackOverflowError("stack overflow");
					} else {
						throw new SdaiException(SdaiException.SY_ERR, tex);
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				check_result = ELogical.FALSE;
//				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			if (check_result <= 1) {
				ret_val = ELogical.FALSE;
				WhereRule wr = (WhereRule)w_rule;
				if (contxt != null && contxt.aggr_size != 0) {
					wr.store_to_array(contxt.ent_instances, contxt.aggr_size, contxt.empty_aggr);
					contxt.aggr_size = 0;
				} else {
					wr.aggr_size = 0;
				}
				if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
					((AEntity)nonConf).addAtTheEnd(w_rule, null);
				} else {
					((AEntity)nonConf).setForNonList(w_rule, ((AEntity)nonConf).myLength, null, null);
				}
			} else if (check_result == 3) {
				if (ret_val == ELogical.TRUE) {
					ret_val = ELogical.UNKNOWN;
				}
			}
//			element = element.next;
		}
		repository.session.sdai_context.domain = domain;
		return ret_val;
//		} // syncObject
	}


/**
 * Determines if specified global rule is satisfied by the population
 * associated with this <code>SchemaInstance</code>. Validation is performed
 * for all entity instances of the entity types to which the specified global
 * rule applies in all <code>SdaiModel</code>s associated with the schema instance.
 * @param rule the name of the global rule to validate
 * @param nonConf the non-persistent list to which those instances of the
 * entity <code>where_rule</code> within the validated global rule to which they did not
 * conform are appended (if the return value is 1)
 * @return number 2 if global rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #validateGlobalRule(EGlobal_rule rule, AWhere_rule nonConf)
 * @see "ISO 10303-22::10.6.5 Validate global rule"
 */
	public int validateGlobalRule(String rule, AWhere_rule nonConf) throws SdaiException {
		int i;
		EGlobal_rule rule_found = null;
		EGlobal_rule gl_rule;
		SdaiModel model_dict = native_schema.modelDictionary;
		CEntity [] instances = model_dict.instances_sim[SdaiSession.IMPLICIT_DECL_RULE_DECL];
		for (i = 0; i < model_dict.lengths[SdaiSession.IMPLICIT_DECL_RULE_DECL]; i++) {
			CImplicit_declaration$rule_declaration dec_impl =
				(CImplicit_declaration$rule_declaration)instances[i];
			gl_rule = (EGlobal_rule)dec_impl.getDefinition(null);
			if (rule.equals(gl_rule.getName(null))) {
				rule_found = gl_rule;
				break;
			}
		}
		if (rule_found == null) {
			instances = model_dict.instances_sim[SdaiSession.LOCAL_DECL_RULE_DECL];
			for (i = 0; i < model_dict.lengths[SdaiSession.LOCAL_DECL_RULE_DECL]; i++) {
				CLocal_declaration$rule_declaration dec_loc =
					(CLocal_declaration$rule_declaration)instances[i];
				gl_rule = (EGlobal_rule)dec_loc.getDefinition(null);
				if (rule.equals(gl_rule.getName(null))) {
					rule_found = gl_rule;
					break;
				}
			}
		}
		if (rule_found == null) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		return validateGlobalRule(rule_found, nonConf);
	}


/**
 * Determines whether the submitted uniqueness rule is satisfied by the population
 * associated with this <code>SchemaInstance</code>. Validation is performed
 * for all entity instances of the entity type in which the specified uniqueness
 * rule was defined in all <code>SdaiModel</code>s associated with the schema instance.
 * @param rule the uniqueness rule to validate
 * @param nonConf the non-persistent list to which instances not conforming to
 * validation are appended (if the return value is 1)
 * @return number 2 if uniqueness rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #validateUniquenessRule(String rule, AEntity nonConf)
 * @see "ISO 10303-22::10.6.6 Validate uniqueness rule"
 */
	public int validateUniquenessRule(EUniqueness_rule rule, AEntity nonConf) throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (rule == null) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (repository == session.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		int i = 0;
		int mod_ind = -1;
		int tp_ind;
		int subtp_ind;
		int inst_ind;

		CEntityDefinition def = (CEntityDefinition)rule.getParent_entity(null);
		if(associated_models == null) {
			getAssociatedModels();
		}
		SdaiModel mod = getNextModel(mod_ind, def);
		if (mod == null) {
			return ELogical.TRUE;
		}
		AAttribute attrs = rule.getAttributes(null);
		AEntity attrs2 = (AEntity)attrs;
		if (iter == null) {
			iter = attrs.createIterator();
		} else {
			attrs.attachIterator(iter);
		}
		if (attributes == null) {
			if (attrs2.myLength > ATTRIBUTES_ARRAY_SIZE) {
				attributes = new EAttribute[attrs2.myLength];
				flag = new boolean[attrs2.myLength];
				values = new Value[attrs2.myLength][];
			} else {
				attributes = new EAttribute[ATTRIBUTES_ARRAY_SIZE];
				flag = new boolean[ATTRIBUTES_ARRAY_SIZE];
				values = new Value[ATTRIBUTES_ARRAY_SIZE][];
			}
		} else if (attrs2.myLength > attributes.length) {
			Value [] n_values [] = new Value[attrs2.myLength][];
			for (i = 0; i < attributes.length; i++) {
				n_values[i] = values[i];
			}
			values = n_values;
			attributes = new EAttribute[attrs2.myLength];
			flag = new boolean[attrs2.myLength];
		}
		i = 0;
		while(iter.next()) {
			attributes[i] = attrs.getCurrentMember(iter);
			if (((AttributeDefinition)attributes[i]).attr_tp == AttributeDefinition.EXPLICIT) {
				flag[i] = ((CExplicit_attribute)attributes[i]).getOptional_flag(null);
			}
			i++;
		}

		int count = 0;
		mod_ind = aux;
		tp_ind = mod.e_type_ind;
		subtp_ind = mod.subtype_ind;
		inst_ind = 0;
		while (true) {
			inst_ind = getNextInstanceIndex(mod, tp_ind, subtp_ind, inst_ind);
			if (inst_ind < 0) {
				mod = getNextModel(mod_ind, def);
				if (mod == null) {
					break;
				}
				mod_ind = aux;
				tp_ind = mod.e_type_ind;
				subtp_ind = mod.subtype_ind;
				inst_ind = getNextInstanceIndex(mod, tp_ind, subtp_ind, 0);
			} else {
				subtp_ind = mod.subtype_ind;
			}
			CEntity inst = mod.instances_sim[mod.pop_ent_count][inst_ind];
			for (i = 0; i < attrs2.myLength; i++) {
				if (values[i] == null) {
					values[i] = new Value[INSTANCES_ARRAY_SIZE];
				} else if (count >= values[i].length) {
					int new_ln = 2 * values[i].length;
					Value new_values [] = new Value[new_ln];
					System.arraycopy(values[i], 0, new_values, 0, values[i].length);
					values[i] = new_values;
				}
				Value val = inst.get(attributes[i]);
				val.agg_owner = inst;
				val.types_count = 0;
				switch (val.aux) {
					case 1:
						if (val.tag == Value.INDETERMINATE && flag[i]) {
							val.types_count = 1;
						}
						break;
					case 2:
						if (val.tag == Value.INDETERMINATE || (val.tag == PhFileReader.LOGICAL && val.integer == 2)) {
							val.types_count = 1;
						}
						break;
					case 3:
						if (val.tag == Value.INDETERMINATE) {
							val.types_count = 1;
						}
						break;
				}
				val.aux = 0;
				values[i][count] = val;
			}
			count++;
			inst_ind++;
		}
		return enumerate_violations(count, attrs2.myLength, nonConf);
	}

	private int enumerate_violations(int count, int attr_count, AEntity nonConf) throws SdaiException {
		int valid_res = ELogical.TRUE;
		for (int i = 0; i < count - 1; i++) {
			boolean inst_violated;
			if (values[0][i].types_count >1) {
				inst_violated = true;
				values[0][i].types_count -= 2;
			} else {
				inst_violated = false;
			}
			for (int j = i + 1; j < count; j++) {
				int comp_res = 2;
				for (int k = 0; k < attr_count; k++) {
					if (values[k][i].types_count == 1 || values[k][j].types_count == 1 || values[k][j].types_count == 3) {
						if (comp_res == 2) {
							comp_res = 1;
						}
					} else {
						int res = Value.instanceEqualInt(null, values[k][i], values[k][j]);
						if (res == ELogical.UNKNOWN) {
							if (comp_res == 2) {
								comp_res = 1;
							}
						} else if (res == ELogical.FALSE) {
							comp_res = 0;
						}
					}
				}
				if (comp_res == 1) {
					if (valid_res == ELogical.TRUE) {
						valid_res = ELogical.UNKNOWN;
					}
				} else if (comp_res == 2) {
					valid_res = ELogical.FALSE;
					if (!inst_violated) {
						addToNonConf((CEntity)values[0][i].agg_owner, nonConf);
						inst_violated = true;
					}
					addToNonConf((CEntity)values[0][j].agg_owner, nonConf);
					values[0][j].types_count += 2;
				}
			}
		}
		return valid_res;
	}


/*	public int validateUniquenessRule(EUniqueness_rule rule, AEntity nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (rule == null) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (repository == session.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		int i = 0;
		int mod_ind1 = -1, mod_ind2;
		int tp_ind1, tp_ind2;
		int subtp_ind1, subtp_ind2;
		int inst_ind1, inst_ind2;
		CEntityDefinition def = (CEntityDefinition)rule.getParent_entity(null);
		if(associated_models == null) {
			getAssociatedModels();
		}
		SdaiModel mod1 = getNextModel(mod_ind1, def);
		if (mod1 == null) {
			return ELogical.TRUE;
		}
		AAttribute attrs = rule.getAttributes(null);
		AEntity attrs2 = (AEntity)attrs;
		if (iter == null) {
			iter = attrs.createIterator();
		} else {
			attrs.attachIterator(iter);
		}
		if (attributes == null) {
			if (attrs2.myLength > ATTRIBUTES_ARRAY_SIZE) {
				attributes = new EAttribute[attrs2.myLength];
				values = new Value[attrs2.myLength];
				unknown = new boolean[attrs2.myLength];
			} else {
				attributes = new EAttribute[ATTRIBUTES_ARRAY_SIZE];
				values = new Value[ATTRIBUTES_ARRAY_SIZE];
				unknown = new boolean[ATTRIBUTES_ARRAY_SIZE];
			}
		} else if (attrs2.myLength > attributes.length) {
			attributes = new EAttribute[attrs2.myLength];
			values = new Value[attrs2.myLength];
			unknown = new boolean[attrs2.myLength];
		}
		while(iter.next()) {
			attributes[i++] = attrs.getCurrentMember(iter);
		}

		int valid_res = ELogical.TRUE;
		mod_ind1 = aux;
		tp_ind1 = mod1.e_type_ind;
		subtp_ind1 = mod1.subtype_ind;
		inst_ind1 = 0;
		while (true) {
			inst_ind1 = getNextInstanceIndex(mod1, tp_ind1, subtp_ind1, inst_ind1);
			if (inst_ind1 < 0) {
				mod1 = getNextModel(mod_ind1, def);
				if (mod1 == null) {
					break;
				}
				mod_ind1 = aux;
				tp_ind1 = mod1.e_type_ind;
				subtp_ind1 = mod1.subtype_ind;
				inst_ind1 = getNextInstanceIndex(mod1, tp_ind1, subtp_ind1, 0);
			} else {
				subtp_ind1 = mod1.subtype_ind;
			}
//			CEntity inst1 = mod1.instances_sim[tp_ind1][inst_ind1];
			CEntity inst1 = mod1.instances_sim[mod1.pop_ent_count][inst_ind1];
			boolean inst1_violated = false;
			for (i = 0; i < attrs2.myLength; i++) {
				values[i] = inst1.get(attributes[i]);
				unknown[i] = false;
				switch (values[i].aux) {
					case 1:
						if (values[i].tag == Value.INDETERMINATE && ((CExplicit_attribute)attributes[i]).getOptional_flag(null)) {
							unknown[i] = true;
						}
						break;
					case 2:
						if (values[i].tag == Value.INDETERMINATE) {
							unknown[i] = true;
						} else if (values[i].tag == PhFileReader.LOGICAL && values[i].integer == 2) {
							unknown[i] = true;
						}
						break;
					case 3:
						if (values[i].tag == Value.INDETERMINATE) {
							unknown[i] = true;
						}
						break;
				}
				values[i].aux = 0;
			}
			inst_ind2 = inst_ind1;
			SdaiModel mod2 = mod1;
			mod_ind2 = mod_ind1;
			tp_ind2 = tp_ind1;
			subtp_ind2 = subtp_ind1;
			while (true) {
				inst_ind2++;
				inst_ind2 = getNextInstanceIndex(mod2, tp_ind2, subtp_ind2, inst_ind2);
				if (inst_ind2 < 0) {
					mod2 = getNextModel(mod_ind2, def);
					if (mod2 == null) {
						break;
					}
					mod_ind2 = aux;
					tp_ind2 = mod2.e_type_ind;
					subtp_ind2 = mod2.subtype_ind;
					inst_ind2 = getNextInstanceIndex(mod2, tp_ind2, subtp_ind2, 0);
				} else {
					subtp_ind2 = mod2.subtype_ind;
				}
//				CEntity inst2 = mod2.instances_sim[tp_ind2][inst_ind2];
				CEntity inst2 = mod2.instances_sim[mod2.pop_ent_count][inst_ind2];
				int comp_res = 2;
				for (int j = 0; j < attrs2.myLength; j++) {
					Value val2 = inst2.get(attributes[j]);
					boolean unknown2 = false;
					switch (val2.aux) {
						case 1:
							if (val2.tag == Value.INDETERMINATE && ((CExplicit_attribute)attributes[j]).getOptional_flag(null)) {
								unknown2 = true;
							}
							break;
						case 2:
							if (val2.tag == Value.INDETERMINATE) {
								unknown2 = true;
							} else if (val2.tag == PhFileReader.LOGICAL && val2.integer == 2) {
								unknown2 = true;
							}
							break;
						case 3:
							if (val2.tag == Value.INDETERMINATE) {
								unknown2 = true;
							}
							break;
					}
					val2.aux = 0;
					if (unknown[j] || unknown2) {
						if (comp_res == 2) {
							comp_res = 1;
						}
					} else {
						int res = Value.instanceEqualInt(null, values[j], val2);
						if (res == ELogical.UNKNOWN) {
							if (comp_res == 2) {
								comp_res = 1;
							}
						} else if (res == ELogical.FALSE) {
							comp_res = 0;
						}
					}
				}
				if (comp_res == 1) {
					if (valid_res == ELogical.TRUE) {
						valid_res = ELogical.UNKNOWN;
					}
				} else if (comp_res == 2) {
					valid_res = ELogical.FALSE;
					if (!inst1_violated) {
						addToNonConf(inst1, nonConf);
						inst1_violated = true;
					}
					addToNonConf(inst2, nonConf);
				}
			}
			inst_ind1++;
		}
		return valid_res;
//		} // syncObject
//		throw new SdaiException(SdaiException.FN_NAVL);
	}*/

	private SdaiModel getNextModel(int ind, CEntityDefinition def) throws SdaiException {
		ind++;
		while (ind < associated_models.myLength) {
			SdaiModel mod = (SdaiModel)associated_models.myData[ind];
			SchemaData sch_data = mod.underlying_schema.modelDictionary.schemaData;
			int ind_type = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, def);
			if (ind_type >= 0) {
				if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					mod.startReadOnlyAccess();
				}
				int ind_type_true;
				if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
						mod.repository != SdaiSession.systemRepository) {
					ind_type_true = mod.find_entityRO(mod.dictionary.schemaData.entities[ind_type]);
				} else {
					ind_type_true = ind_type;
				}
				if (ind_type_true >= 0 && mod.lengths[ind_type_true] > 0) {
					mod.e_type_ind = ind_type;
					mod.subtype_ind = -1;
					aux = ind;
					return mod;
				}
				int subtypes[] = mod.underlying_schema.getSubtypes(ind_type);
				if (subtypes != null) {
					for (int i = 0; i < subtypes.length; i++) {
						if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
								mod.repository != SdaiSession.systemRepository) {
							ind_type_true = mod.find_entityRO(mod.dictionary.schemaData.entities[subtypes[i]]);
						} else {
							ind_type_true = subtypes[i];
						}
						if (ind_type_true >= 0 && mod.lengths[ind_type_true] > 0) {
							mod.e_type_ind = ind_type;
							mod.subtype_ind = i;
							aux = ind;
							return mod;
						}
					}
				}
			}
			ind++;
		}
		return null;
	}

	private int getNextInstanceIndex(SdaiModel mod, int tp_ind, int subtp_ind, int ind) throws SdaiException {
		int m, r;
		boolean subtypes_read = false;
		int subtypes[] = null;
		if (subtp_ind < 0) {
			m = tp_ind;
		} else {
			subtypes = mod.underlying_schema.getSubtypes(tp_ind);
			subtypes_read = true;
			m = subtypes[subtp_ind];
		}
		if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && mod.repository != SdaiSession.systemRepository) {
			m = mod.find_entityRO(mod.dictionary.schemaData.entities[m]);
		}
		if (m >= 0 && ind < mod.lengths[m]) {
			mod.subtype_ind = subtp_ind;
			mod.pop_ent_count = m;
			return ind;
		}
		if (subtp_ind < 0) {
			r = 0;
		} else {
			r = subtp_ind + 1;
		}
		if (!subtypes_read) {
			subtypes = mod.underlying_schema.getSubtypes(tp_ind);
		}
		if (subtypes == null) {
			return -1;
		}
		while (r < subtypes.length) {
			int true_index;
			if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && mod.repository != SdaiSession.systemRepository) {
				true_index = mod.find_entityRO(mod.dictionary.schemaData.entities[subtypes[r]]);
			} else {
				true_index = subtypes[r];
			}
			if (true_index >= 0 && mod.lengths[true_index] > 0) {
				mod.subtype_ind = r;
				mod.pop_ent_count = true_index;
				return 0;
			}
			r++;
		}
		return -1;
	}

	private void addToNonConf(CEntity inst, AEntity nonConf) throws SdaiException {
		boolean found = false;
		if (nonConf.myLength > 0) {
			int i;
			Object [] myDataA;
			if (nonConf.myType == null || nonConf.myType.express_type == DataType.LIST) {
				ListElement element;
				if (nonConf.myLength == 1) {
					if (inst == nonConf.myData) {
						found = true;
					}
				} else if (nonConf.myLength == 2) {
					myDataA = (Object [])nonConf.myData;
					for (i = 0; i < 2; i++) {
						if (inst == myDataA[i]) {
							found = true;
							break;
						}
					}
				} else {
					if (nonConf.myLength <= CAggregate.SHORT_AGGR) {
						element = (ListElement)nonConf.myData;
					} else {
						myDataA = (Object [])nonConf.myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (inst == element.object) {
							found = true;
							break;
						}
						element = element.next;
					}
				}
			} else {
				if (nonConf.myLength == 1) {
					if (inst == nonConf.myData) {
						found = true;
					}
				} else {
					myDataA = (Object [])nonConf.myData;
					for (i = 0; i < nonConf.myLength; i++) {
						if (inst == myDataA[i]) {
							found = true;
							break;
						}
					}
				}
			}
		}
		if (!found) {
			if (nonConf.myType == null || nonConf.myType.express_type == DataType.LIST) {
				nonConf.addAtTheEnd(inst, null);
			} else {
				nonConf.setForNonList(inst, nonConf.myLength, null, null);
			}
		}
	}


/**
 * Determines whether the submitted uniqueness rule is satisfied by the population
 * associated with this <code>SchemaInstance</code>. Validation is performed
 * for all entity instances of the entity type in which the uniqueness
 * rule was defined in all <code>SdaiModel</code>s associated with the schema instance.
 * The rule is specified by the string of the form "entity_name.rule_name", where
 * 'entity_name' is the name of the entity data type within which the rule is declared
 * and 'rule_name' is the name (if present) of the rule. If the rule has no name, then
 * the supplied string shall be of the form "entity_name." or just "entity_name".
 * @param rule the name of the uniqueness rule to validate qualified by the name
 * of its owning entity data type
 * @param nonConf the non-persistent list to which instances not conforming to
 * validation are appended (if the return value is 1)
 * @return number 2 if uniqueness rule is satisfied, number 1 if it is violated,
 * and number 3 if evaluation value is indeterminate.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RU_NDEF, rule not defined.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @see #validateUniquenessRule(EUniqueness_rule rule, AEntity nonConf)
 * @see "ISO 10303-22::10.6.6 Validate uniqueness rule"
 */
	public int validateUniquenessRule(String rule, AEntity nonConf) throws SdaiException {
		return validateUniquenessRule(native_schema.modelDictionary.schemaData.findUniquenessRule(rule), nonConf);
	}


/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.6.7 Validate instance reference domain"
 */
	public int validateInstanceReferenceDomain(AEntity nonConf) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.6.8 Validate schema instance"
 */
	public int validateSchemaInstance() throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (repository == session.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		int valid_result = ELogical.TRUE;
		int res = validationInstancesInModels();
		if (res == ELogical.FALSE) {
			recordValidation(ELogical.FALSE);
			return ELogical.FALSE;
		} else if (res == ELogical.UNKNOWN) {
			valid_result = ELogical.UNKNOWN;
		}
		res = validationRules();
		if (res == ELogical.FALSE) {
			recordValidation(ELogical.FALSE);
			return ELogical.FALSE;
		} else if (res == ELogical.UNKNOWN) {
			valid_result = ELogical.UNKNOWN;
		}
		recordValidation(valid_result);
		return valid_result;
//		} // syncObject
//		throw new SdaiException(SdaiException.FN_NAVL);
	}


	private void recordValidation(int result) throws SdaiException {
		validation_date = System.currentTimeMillis();
		validation_result = result;
		validation_level = repository.session.sdai_implementation.getExpressionLevel();
	}


	private int validationInstancesInModels() throws SdaiException {
		if (associated_models == null) {
			return ELogical.TRUE;
		}
		int valid_result = ELogical.TRUE;
		AAttribute attrs_aggr = new AAttribute();
		SdaiIterator it_insts = null;
		SdaiIterator it_rules = null;
		for (int i = 0; i < associated_models.myLength; i++) {
			SdaiModel model = (SdaiModel)associated_models.myData[i];
			if (models_dom == null) {
				models_dom = new ASdaiModel();
			} else {
				models_dom.clear();
			}
			models_dom.addByIndex(1, model, null);
			AEntity instances = model.getInstances();
			if (it_insts == null) {
				it_insts = instances.createIterator();
			} else {
				instances.attachIterator(it_insts);
			}
			int res = validateInstances(instances, attrs_aggr, it_insts, it_rules, models_dom);
			if (res == ELogical.FALSE) {
				return ELogical.FALSE;
			} else if (res == ELogical.UNKNOWN) {
				valid_result = ELogical.UNKNOWN;
			}
		}
		return valid_result;
	}


	private int validateInstances(AEntity instances, AAttribute attrs_aggr,
			SdaiIterator it_insts, SdaiIterator it_rules, ASdaiModel models_dom) throws SdaiException {
		int valid_result = ELogical.TRUE;
		while (it_insts.next()){
			attrs_aggr.clear();
			CEntity instance = (CEntity)instances.getCurrentMemberEntity(it_insts);
			int res = instance.validateInstance(attrs_aggr, it_rules, models_dom);
			if (res == ELogical.FALSE) {
				return ELogical.FALSE;
			} else if (res == ELogical.UNKNOWN) {
				valid_result = ELogical.UNKNOWN;
			}
		}
		return valid_result;
	}


	private int validationRules() throws SdaiException {
		SdaiContext session_sdai_context = repository.session.sdai_context;
		SdaiModel working_model = null;
		if (session_sdai_context == null || session_sdai_context.schema != native_schema) {
			working_model = repository.createSdaiModel("working&&&", native_schema);
			working_model.startReadWriteAccess();
			repository.session.sdai_context = new SdaiContext(native_schema, null, working_model);
		}

		int res;
		int valid_result = ELogical.TRUE;
		AWhere_rule nonConf = new AWhere_rule();
		ARule_declaration decls = native_schema.getRule_declarations(null, null);
		SdaiIterator it_decls = decls.createIterator();
		while (it_decls.next()) {
			ERule_declaration decl = decls.getCurrentMember(it_decls);
			EGlobal_rule gl_rule = (EGlobal_rule)decl.getDefinition(null);
			res = validateGlobalRule(gl_rule, nonConf);
			if (res == ELogical.FALSE) {
				if (working_model != null) {
					working_model.deleteSdaiModel();
					if (session_sdai_context != null) {
						repository.session.sdai_context = session_sdai_context;
					}
				}
				return ELogical.FALSE;
			} else if (res == ELogical.UNKNOWN) {
				valid_result = ELogical.UNKNOWN;
			}
			nonConf.clear();
		}

		AEntity_declaration e_decls = native_schema.getEntity_declarations(null, null);
		e_decls.attachIterator(it_decls);
		AEntity nonConf_ent = new AEntity();
		SdaiIterator it_rules = null;
		while (it_decls.next()) {
			EEntity_declaration e_decl = e_decls.getCurrentMember(it_decls);
			EEntity_definition e_def = (EEntity_definition)e_decl.getDefinition(null);
			AUniqueness_rule u_rules = e_def.getUniqueness_rules(null, null);
			if (((AEntity)u_rules).myLength == 0) {
				continue;
			}
			if (it_rules == null) {
				it_rules = u_rules.createIterator();
			} else {
				u_rules.attachIterator(it_rules);
			}
			while(it_rules.next()) {
				EUniqueness_rule u_rule = u_rules.getCurrentMember(it_rules);
				res = validateUniquenessRule(u_rule, nonConf_ent);
				if (res == ELogical.FALSE) {
					if (working_model != null) {
						working_model.deleteSdaiModel();
						if (session_sdai_context != null) {
							repository.session.sdai_context = session_sdai_context;
						}
					}
					return ELogical.FALSE;
				} else if (res == ELogical.UNKNOWN) {
					valid_result = ELogical.UNKNOWN;
				}
				nonConf_ent.clear();
			}
		}

		if (working_model != null) {
			working_model.deleteSdaiModel();
			if (session_sdai_context != null) {
				repository.session.sdai_context = session_sdai_context;
			}
		}
		return valid_result;
	}


/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.6.9 Is validation current"
 */
	public boolean isValidationCurrent() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * Returns a description of this schema instance as a <code>String</code>.
 * It includes constant string "SchemaInstance: " and the name of the schema instance.
 * @return a description of the schema instance.
 */
	public String toString() {
		return "SchemaInstance: " + name;
	}



	protected SchemaInstanceHeader toSchemaInstanceHeader() throws SdaiException {
		int i;
		int count;
		SchemaInstanceHeader schemaHeader = new SchemaInstanceHeader();
		schemaHeader.name = name;
		schemaHeader.nativeSchema = getNativeSchemaString();
		schemaHeader.validation_date = getValidationDate();
		schemaHeader.validation_result = validation_result;
		schemaHeader.validation_level = validation_level;
		if (description == null) {
			count = 0;
		} else {
			count = description.myLength;
		}
		schemaHeader.description = new String[count];
		for (i = 0; i < count; i++) {
			schemaHeader.description[i] = description.getByIndex(i + 1);
		}
		schemaHeader.changeDate = getChangeDate();
		if (author == null) {
			count = 0;
		} else {
			count = author.myLength;
		}
		schemaHeader.author = new String[count];
		for (i = 0; i < count; i++) {
			schemaHeader.author[i] = author.getByIndex(i + 1);
		}
		if (organization == null) {
			count = 0;
		} else {
			count = organization.myLength;
		}
		schemaHeader.organization = new String[count];
		for (i = 0; i < count; i++) {
			schemaHeader.organization[i] = organization.getByIndex(i + 1);
		}
		schemaHeader.preprocessorVersion = preprocessor_version;
		schemaHeader.originatingSystem = originating_system;
		schemaHeader.authorization = authorization;
		schemaHeader.defaultLanguage = language;
		if (context_identifiers == null) {
			count = 0;
		} else {
			count = context_identifiers.myLength;
		}
		schemaHeader.contextIdentifiers = new String[count];
		for (i = 0; i < count; i++) {
			schemaHeader.contextIdentifiers[i] = context_identifiers.getByIndex(i + 1);
		}
		return schemaHeader;
	}


/**
*/
	protected void fromSchemaInstanceHeader(SchemaInstanceHeader schemaHeader) throws SdaiException {
		int i;
		name = schemaHeader.name;
		if (schemaHeader.nativeSchema != null) {
			String dict_name = schemaHeader.nativeSchema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
			dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
			if (native_schema != null && !native_schema.getName(null).equals(schemaHeader.nativeSchema)) {
				native_schema = null;
			}
		} else {
			native_schema = null;
		}
		if (schemaHeader.validation_date != null) {
			validation_date = repository.session.cal.timeStampToLong(schemaHeader.validation_date);
		}
		validation_result = schemaHeader.validation_result;
		validation_level = schemaHeader.validation_level;
		if (description == null) {
			description = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (schemaHeader.description != null) {
			for (i = 0; i < schemaHeader.description.length; i++) {
				description.addByIndexPrivate(i + 1, schemaHeader.description[i]);
			}
		}
		if (schemaHeader.changeDate != null) {
			change_date = repository.session.cal.timeStampToLong(schemaHeader.changeDate);
		}
		if (author == null) {
			author = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (schemaHeader.author != null) {
			for (i = 0; i < schemaHeader.author.length; i++) {
				author.addByIndexPrivate(i + 1, schemaHeader.author[i]);
			}
		}
		if (organization == null) {
			organization = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (schemaHeader.organization != null) {
			for (i = 0; i < schemaHeader.organization.length; i++) {
				organization.addByIndexPrivate(i + 1, schemaHeader.organization[i]);
			}
		}
		preprocessor_version = schemaHeader.preprocessorVersion;
		originating_system = schemaHeader.originatingSystem;
		authorization = schemaHeader.authorization;
		language = schemaHeader.defaultLanguage;
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (schemaHeader.contextIdentifiers != null) {
			for (i = 0; i < schemaHeader.contextIdentifiers.length; i++) {
				context_identifiers.addByIndexPrivate(i + 1, schemaHeader.contextIdentifiers[i]);
			}
		}
	}


	boolean committing(boolean commit_bridge) throws SdaiException {
		if (modified || modified_by_import) {
			if (modified) {
				change_date = System.currentTimeMillis();
			} else if (modified_by_import) {
				try {
					if (change_date <= 0) {
						change_date = repository.session.cal.timeStampToLong(repository.file_name.time_stamp);
					}
				} catch (Exception ex) {
					change_date = System.currentTimeMillis();
				}
				try {
					if (validation_date <= 0) {
						validation_date = repository.session.cal.timeStampToLong(repository.file_name.time_stamp);
					}
				} catch (Exception ex) {
					validation_date = System.currentTimeMillis();
				}
			}
			commit_bridge = committingInternal(commit_bridge);
		}
		committed = true;
		return commit_bridge;
	}


	abstract protected boolean committingInternal(boolean commit_bridge) throws SdaiException;


//	void committing() throws SdaiException {
//		committingInternal();
//		modified = false;
//	}


	/**
	 * The name of this method might by misleading.
	 * The implementation is responsible for committing
	 * BOTH associated models and included schema instances.
	 */
	abstract protected void committingAssocModels() throws SdaiException;


	protected SessionRemote get_bridgeSession() {
		return repository.session.bridgeSession;
	}

	protected SessionRemote get_bridgeSession(SdaiRepository rep) {
		return rep.session.bridgeSession;
	}

	protected boolean get_created() {
		return created;
	}


	protected void set_created(boolean cr) {
		created = cr;
	}


	boolean deleting(boolean commit_bridge, SdaiRepository repo) throws SdaiException {
		return deletingInternal(commit_bridge, repo);
	}


	abstract protected boolean deletingInternal(boolean commit_bridge, SdaiRepository repo) throws SdaiException;


	boolean aborting() throws SdaiException {
		boolean deleted = abortingInternal(modified);
		modified = false;
		return deleted;
	}


	protected abstract boolean abortingInternal(boolean modif) throws SdaiException;


	protected void abortingCreated() throws SdaiException {
		delete();
	}


	boolean isRemote() throws SdaiException {
		return isRemoteInternal();
	}


	protected abstract boolean isRemoteInternal() throws SdaiException;


	boolean checkSchInstance(Object current_member) throws SdaiException {
		return checkSchInstanceInternal(current_member);
	}


	protected abstract boolean checkSchInstanceInternal(Object current_member) throws SdaiException;


	protected abstract void attachRemoteSchInstance(Object current_member) throws SdaiException;


/*	protected void runThroughAssociatedModels() throws SdaiException {
		ASdaiModel assoc_mods = getAssociatedModels();
		for (int i = 0; i < assoc_mods.myLength; i++) {
			SdaiModel model = (SdaiModel)assoc_mods.myData[i];
			if (model.mode == SdaiModel.READ_ONLY) {
				throw new SdaiException(SdaiException.MX_RO);
			} else if (model.mode == SdaiModel.READ_WRITE) {
				throw new SdaiException(SdaiException.MX_RW);
			}
			if (!model.repository.active) {
				model.repository.openRepository();
			}
			model.startReadOnlyAccess();
		}
	}*/


	void startAssociatedModels(Object domain) throws SdaiException {
		startAssociatedModelsInternal(domain);
	}


	protected abstract void startAssociatedModelsInternal(Object domain) throws SdaiException;


	void closeDomain() throws SdaiException {
		closeDomainInternal();
	}


	protected abstract void closeDomainInternal() throws SdaiException;


	void refreshDomain(boolean read_write, ASdaiModel rwModels) throws SdaiException {
		refreshDomainInternal(read_write, rwModels);
	}


	protected abstract void refreshDomainInternal(boolean read_write, ASdaiModel rwModels) throws SdaiException;

    protected final String getNameFast() {
        return name;
    }

	protected final int getAssociatedModelsCount() {
		return associated_models != null ? associated_models.myLength : 0;
	}

	protected final SdaiModel getAssociatedModel(int index) throws SdaiException {
		return (SdaiModel)associated_models.myData[index];
	}

	protected final void removeModelAssociatedWith(SdaiModel model,
												   boolean initialize) throws SdaiException {
		model.removeAssociatedWith(this, initialize);
	}

	protected void attachDomain(SdaiModel model, Object dom) throws SdaiException {
		model.domain = dom;
	}


	protected void releaseAssociatedModel(int index) throws SdaiException {
		((SdaiModel)associated_models.myData[index]).domain = null;
	}


/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this schema instance, that is, the header information
 * and all its associated models. This operation is allowed only if this
 * schema instance was not modified and no associated model was created, deleted
 * or modified since the most recent commit or abort operation was performed.
 * If this condition is not satisfied, then SdaiException TR_RW is thrown.
 * At the moment of invocation of the method the repository owning this schema
 * instance must be opened.
 * If parameter <code>location</code> gets some <code>String</code> value,
 * then this value is used as a name for the resulting file.
 * If, however, <code>location</code> is null, then the file name is constructed by
 * taking the name of this schema instance and combining it with the extension "stp".
 * The contents of each associated model is written down to
 * the file as a separate data section. The header of the file includes
 * time-stamp which specifies the date and time when the schema instance was
 * created or most recently modified.
 * The attribute <code>name</code> of the entity <code>file_name</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.2 file_name) is set
 * to the name of this schema instance.
 * The value of the attribute <code>implementation_level</code> of the
 * entity <code>file_description</code> (see ISO 10303-21::8.2.1 file_description)
 * can take one of the following values:
 * <ul><li>"2;1" if the aggregate <code>associated_models</code> contains at most one model;
 * <li>"3;1" otherwise; in this case for the schema instance an instance of
 * the entity <code>file_population</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.4 file_population) is created;
 * </ul>
 * In this <code>file_population</code> instance the value standing for
 * <code>associated_models</code> is missing, that is, dollar sign is used.
 * This happens because all the data sections in the
 * resulting exchange structure file correspond to models in the
 * <code>associated_models</code> set (see ISO 10303-21::8.2.4 file_population).
 * <p> After execution of this method the schema instance itself remains unchanged.
 * The method is disabled for the special schema instance <code>data_dictionary</code>
 * defined in <code>SdaiSession</code> class.
 * In this case SdaiException FN_NAVL is thrown.
 * @param location the name of the file created by the method.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiRepository#exportClearTextEncoding
 * @see #exportClearTextEncoding(String location, String file_name)
 * @see #exportClearTextEncoding(OutputStream location)
 * @see #exportClearTextEncoding(OutputStream location, String file_name)
 * see "ISO 10303-21"
 * @since 4.0.0
 */
	public void exportClearTextEncoding(String location) throws SdaiException {
		exportClearTextEncodingGeneric(location, null, null);
	}

/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this schema instance, that is, the header information
 * and all its associated models. This operation is allowed only if this
 * schema instance was not modified and no associated model was created, deleted
 * or modified since the most recent commit or abort operation was performed.
 * If this condition is not satisfied, then SdaiException TR_RW is thrown.
 * At the moment of invocation of the method the repository owning this schema
 * instance must be opened.
 * If parameter <code>location</code> gets some <code>String</code> value,
 * then this value is used as a name for the resulting file.
 * If, however, <code>location</code> is null, then the file name is constructed by
 * taking the name of this schema instance and combining it with the extension "stp".
 * The contents of each associated model is written down to
 * the file as a separate data section. The header of the file includes
 * time-stamp which specifies the date and time when the schema instance was
 * created or most recently modified.
 * The attribute <code>name</code> of the entity <code>file_name</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.2 file_name) is set
 * to the string value submitted through the second method's parameter provided
 * this value is nonnull. If this value is <code>null</code>, then the attribute
 * <code>name</code> is set to the name of this schema instance.
 * The value of the attribute <code>implementation_level</code> of the
 * entity <code>file_description</code> (see ISO 10303-21::8.2.1 file_description)
 * can take one of the following values:
 * <ul><li>"2;1" if the aggregate <code>associated_models</code> contains at most one model;
 * <li>"3;1" otherwise; in this case for the schema instance an instance of
 * the entity <code>file_population</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.4 file_population) is created;
 * </ul>
 * In this <code>file_population</code> instance the value standing for
 * <code>associated_models</code> is missing, that is, dollar sign is used.
 * This happens because all the data sections in the
 * resulting exchange structure file correspond to models in the
 * <code>associated_models</code> set (see ISO 10303-21::8.2.4 file_population).
 * <p> After execution of this method the schema instance itself remains unchanged.
 * The method is disabled for the special schema instance <code>data_dictionary</code>
 * defined in <code>SdaiSession</code> class.
 * In this case SdaiException FN_NAVL is thrown.
 * @param location the name of the file created by the method.
 * @param file_name the name of the exchange structure to which this schema instance is exported.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiRepository#exportClearTextEncoding
 * @see #exportClearTextEncoding(String location)
 * @see #exportClearTextEncoding(OutputStream location)
 * @see #exportClearTextEncoding(OutputStream location, String file_name)
 * see "ISO 10303-21"
 * @since 4.0.0
 */
	public void exportClearTextEncoding(String location, String file_name) throws SdaiException {
		exportClearTextEncodingGeneric(location, null, file_name);
	}

/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this schema instance, that is, the header information
 * and all its associated models. At the moment of invocation of the method the
 * repository owning this schema instance must be opened.
 * The exchange structure is written to the supplied stream.
 * The attribute <code>name</code> of the entity <code>file_name</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.2 file_name) is set
 * to the name of this schema instance.
 * For more details about the exchange format see
 * {@link #exportClearTextEncoding(String location)}.
 * Invoking this method is equivalent to making the following call:
 * <code>exportClearTextEncoding(location, null);</code>
 *
 * @param location an <code>OutputStream</code> the exchange structure is directed to.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #exportClearTextEncoding(String location)
 * @see #exportClearTextEncoding(String location, String file_name)
 * @see #exportClearTextEncoding(OutputStream location, String file_name)
 * @since 4.0.0
 */
	public void exportClearTextEncoding(OutputStream location) throws SdaiException {
		exportClearTextEncodingGeneric(null, location, null);
	}

/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this schema instance, that is, the header information
 * and all its associated models. At the moment of invocation of the method the
 * repository owning this schema instance must be opened.
 * The exchange structure is written to the supplied stream.
 * The attribute <code>name</code> of the entity <code>file_name</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.2 file_name) is set
 * to the string value submitted through the second method's parameter provided
 * this value is nonnull. If this value is <code>null</code>, then the attribute
 * <code>name</code> is set to the name of this schema instance.
 * For more details about the exchange format see
 * {@link #exportClearTextEncoding(String location, String file_name)}.
 *
 * @param location an <code>OutputStream</code> the exchange structure is directed to.
 * @param file_name the name of the exchange structure to which this schema instance is exported.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #exportClearTextEncoding(String location)
 * @see #exportClearTextEncoding(String location, String file_name)
 * @see #exportClearTextEncoding(OutputStream location)
 * @since 4.0.0
 */
	public void exportClearTextEncoding(OutputStream location, String file_name) throws SdaiException {
		exportClearTextEncodingGeneric(null, location, file_name);
	}

	private void exportClearTextEncodingGeneric(String location, OutputStream location_stream, String file_name)
			throws SdaiException {
//		synchronized (syncObject) {
		if (repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode == SdaiTransaction.NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, session.active_transaction);
		}
		if (this == session.data_dictionary) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (location_stream == null && location == null) {
			location = name + ".stp";
		}
//		if (modified || modified_by_import) {
//			throw new SdaiException(SdaiException.TR_RW, session.active_transaction);
//		}
		if(associated_models == null) {
			getAssociatedModels();
		}
/*		for (int i = 0; i < associated_models.myLength; i++) {
			SdaiModel model = (SdaiModel)associated_models.myData[i];
			if (model.modified || model.modified_by_import || model.modified_outside_contents ||
					model.modified_outside_contents_by_import) {
				throw new SdaiException(SdaiException.TR_RW, session.active_transaction);
			}
		}
*/
// Exporting.
/*
		SdaiRepository rTemp = session.createRepository("", null);
		Move.moveSchemaInstanceNoDelete(this, rTemp);
		String saved_name = rTemp.file_name.name;
		rTemp.file_name.name = name;
		if (location_stream != null) {
			rTemp.exportClearTextEncoding(location_stream, file_name);
		} else {
			rTemp.exportClearTextEncoding(location, file_name);
		}
		rTemp.file_name.name = saved_name;
		rTemp.deleteRepository();
*/
		SdaiRepository rTemp = session.createRepository("", null);
		String saved_name = rTemp.file_name.name;
		rTemp.file_name.name = name;
		StaticFields staticFields = StaticFields.get();
		try {
			removeDuplicateInstIds(staticFields);
			changeModsOwner(rTemp);
			moveDataToRepo(rTemp);
			if (location_stream != null) {
				rTemp.exportClearTextEncoding(location_stream, file_name);
			} else {
				rTemp.exportClearTextEncoding(location, file_name);
			}
			rTemp.models.myData = null;
			rTemp.models.myLength = 0;
		} finally {
			restoreInstIds(staticFields);
		}
		rTemp.file_name.name = saved_name;
		rTemp.deleteRepository();
//		} // syncObject
	}


	private void removeDuplicateInstIds(StaticFields staticFields) throws SdaiException {
		int i, j;
		SdaiModel model, mod;
		boolean many_repos = false;
		SdaiRepository repo = null;
		count_insts = 0;
		ren_count = 0;
		long largest_id = -1;
		for (i = 0; i < associated_models.myLength; i++) {
			model = (SdaiModel)associated_models.myData[i];
			if (repo == null) {
				repo = model.repository;
			} else {
				many_repos = true;
			}
			if (repo.largest_identifier > largest_id) {
				largest_id = repo.largest_identifier;
			}
		}
		if (!many_repos) {
			return;
		}
		int mods_count = 0;
		SdaiRepository prev_repo = null;
		for (i = 0; i < associated_models.myLength; i++) {
			model = (SdaiModel)associated_models.myData[i];
			if (model.instances_sim == null) {
				continue;
			}
			if (model.repository != prev_repo) {
				mods_count = 0;
				for (j = i + 1; j < associated_models.myLength; j++) {
					mod = (SdaiModel)associated_models.myData[j];
					if (mod.repository == model.repository) {
						continue;
					}
					if (staticFields.other_repo_mods == null) {
						staticFields.other_repo_mods = new SdaiModel[OUTER_MODELS_ARRAY_SIZE];
					} else if (staticFields.other_repo_mods.length <= mods_count) {
						ensureModsCapacity(staticFields);
					}
					staticFields.other_repo_mods[mods_count++] = mod;
				}
				if (mods_count == 0) {
					return;
				}
				prev_repo = model.repository;
			}
			boolean mod_renumb = false;
			CEntity [] row_of_instances;
			for (j = 0; j < model.instances_sim.length; j++) {
				if (model.instances_sim[j] == null) {
					continue;
				}
				boolean  ent_type_renumb = false;
				row_of_instances = model.instances_sim[j];
				for (int k = 0; k < model.lengths[j]; k++) {
					CEntity instance = row_of_instances[k];
					boolean found = false;
					for (int m = 0; m < mods_count; m++) {
						mod = staticFields.other_repo_mods[m];
						if ((mod.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
							if (mod.quick_find_instance(instance.instance_identifier) != null) {
								found = true;
								break;
							}
						} else {
							if (mod.inst_idents == null) {
								mod.loadInstanceIdentifiers();
							}
//System.out.println("SchemaInstance   !!!!! instance.instance_identifier " + instance.instance_identifier +
//"   ind: " + ind + "  mod: " + mod.name + "   all_inst_count: " + mod.all_inst_count);
							if (mod.find_instance_id(instance.instance_identifier) >= 0) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						continue;
					}
					if (staticFields.renumb_insts == null) {
						staticFields.renumb_insts = new CEntity[RENUMB_INSTS_ARRAY_SIZE];
						staticFields.saved_ids = new long[RENUMB_INSTS_ARRAY_SIZE];
					} else if (staticFields.renumb_insts.length <= count_insts) {
						ensureRenumbInstsCapacity(staticFields);
					}
					staticFields.renumb_insts[count_insts] = instance;
					staticFields.saved_ids[count_insts++] = instance.instance_identifier;
					largest_id++;
					instance.instance_identifier = largest_id;
					ent_type_renumb = true;
				}
				if (ent_type_renumb) {
					if (model.lengths[j] > 1) {
						model.prepareForSorting();
						model.sortInstances(j);
					}
					mod_renumb = true;
				}
			}
			removeDuplicateModNames(staticFields, model, staticFields.other_repo_mods, mods_count);
			if (mod_renumb) {
				model.extent_index = -1;
			} else {
				model.extent_index = 0;
			}
		}
	}


	private void removeDuplicateModNames(StaticFields staticFields, SdaiModel model, SdaiModel [] other_repo_mods, int mods_count)
			throws SdaiException {
		int i;
		int count = 0;
		boolean first = true;
		for (i = 0; i < mods_count; i++) {
			if (model.name.equals(other_repo_mods[i].name)) {
				if (staticFields.dup_names_mods == null) {
					if (mods_count <= DUPL_MODELS_ARRAY_SIZE) {
						staticFields.dup_names_mods = new SdaiModel[DUPL_MODELS_ARRAY_SIZE];
					} else {
						staticFields.dup_names_mods = new SdaiModel[mods_count];
					}
				} else if (staticFields.dup_names_mods.length < mods_count) {
					staticFields.dup_names_mods = new SdaiModel[mods_count];
				}
				if (first) {
					staticFields.dup_names_mods[count++] = model;
					first = false;
				}
				staticFields.dup_names_mods[count++] = other_repo_mods[i];
			}
		}
		if (count == 0) {
			return;
		}
		int numb = 0;
		String new_name = null;
		for (i = 0; i < count; i++) {
			SdaiModel mod = staticFields.dup_names_mods[i];
			boolean ident = true;
			while (ident) {
				numb++;
				new_name = mod.name + numb;
				ident = false;
				for (int j = 0; j < associated_models.myLength; j++) {
					SdaiModel m = (SdaiModel)associated_models.myData[j];
					if (m.name.equals(new_name)) {
						ident = true;
					}
				}
			}
			if (staticFields.ren_models == null) {
				staticFields.ren_models = new SdaiModel[SAVED_NAMES_ARRAY_SIZE];
				staticFields.old_names = new String[SAVED_NAMES_ARRAY_SIZE];
			} else if (staticFields.ren_models.length <= ren_count) {
				ensureNamesCapacity(staticFields);
			}
			staticFields.ren_models[ren_count] = mod;
			staticFields.old_names[ren_count++] = mod.name;
			mod.name = new_name;
		}
	}


	private void changeModsOwner(SdaiRepository rTemp) throws SdaiException {
		for (int k = 0; k < associated_models.myLength; k++) {
			SdaiModel model = (SdaiModel)associated_models.myData[k];
			model.extent_type = model.repository;
			model.repository = rTemp;
		}
	}


	private void restoreInstIds(StaticFields staticFields) throws SdaiException {
		int i;
		for (i = 0; i < count_insts; i++) {
			staticFields.renumb_insts[i].instance_identifier = staticFields.saved_ids[i];
		}
		count_insts = 0;
		for (i = 0; i < associated_models.myLength; i++) {
			SdaiModel model = (SdaiModel)associated_models.myData[i];
			model.repository = (SdaiRepository)model.extent_type;
			if (model.extent_index < 0) {
				for (int j = 0; j < model.instances_sim.length; j++) {
					if (model.instances_sim[j] == null ||  model.lengths[j] < 2) {
						continue;
					}
					model.prepareForSorting();
					model.sortInstances(j);
				}
			}
		}
		for (i = 0; i < ren_count; i++) {
			staticFields.ren_models[i].name = staticFields.old_names[i];
		}
	}


	private void moveDataToRepo(SdaiRepository repo) throws SdaiException {
		int i;
		String str;
		if (!repo.active) {
			repo.openRepository();
		}
		SchemaInstance t_schInst = repo.createSchemaInstance(name, getNativeSchema());
		A_string si_descr = getDescription();
		A_string repo_descr = repo.description;
		A_string t_schInst_descr = t_schInst.getDescription();
		for (i = 1; i <= si_descr.myLength; i++) {
			str = si_descr.getByIndex(i);
			repo_descr.addByIndex(i, str);
			t_schInst_descr.addByIndex(i, str);
		}
		A_string si_auth = getAuthor();
		A_string repo_auth = repo.file_name.author;
		A_string t_schInst_auth = t_schInst.getAuthor();
		for (i = 1; i <= si_auth.myLength; i++) {
			str = si_auth.getByIndex(i);
			repo_auth.addByIndex(i, str);
			t_schInst_auth.addByIndex(i, str);
		}
		A_string si_org = getOrganization();
		A_string repo_org = repo.file_name.organization;
		A_string t_schInst_org = t_schInst.getOrganization();
		for (i = 1; i <= si_org.myLength; i++) {
			str = si_org.getByIndex(i);
			repo_org.addByIndex(i, str);
			t_schInst_org.addByIndex(i, str);
		}
		A_string si_con = getContextIdentifiers();
		A_string repo_con = repo.getContextIdentifiers();
		A_string t_schInst_con = t_schInst.getContextIdentifiers();
		for (i = 1; i <= si_con.myLength; i++) {
			str = si_con.getByIndex(i);
			repo_con.addByIndex(i, str);
			t_schInst_con.addByIndex(i, str);
		}
		if (preprocessor_version != null) {
			repo.setPreprocessorVersion(preprocessor_version);
			t_schInst.setPreprocessorVersion(preprocessor_version);
		}
		if (originating_system != null) {
			repo.setOriginatingSystem(originating_system);
			t_schInst.setOriginatingSystem(originating_system);
		}
		if (authorization != null) {
			repo.setAuthorization(authorization);
			t_schInst.setAuthorization(authorization);
		}
		repo.setDefaultLanguage(language);
		t_schInst.setDefaultLanguage(language);
		ASdaiModel targetModelAggreg = repo.models;
		for (i = 0; i < associated_models.myLength; i++) {
			SdaiModel mod = (SdaiModel)associated_models.myData[i];
			targetModelAggreg.addUnorderedRO(mod);
			t_schInst.addSdaiModel(mod);
		}
	}


	private void ensureModsCapacity(StaticFields staticFields) {
		int new_length = staticFields.other_repo_mods.length * 2;
		SdaiModel [] new_mods = new SdaiModel[new_length];
		System.arraycopy(staticFields.other_repo_mods, 0, new_mods, 0, staticFields.other_repo_mods.length);
		staticFields.other_repo_mods = new_mods;
	}


	private void ensureRenumbInstsCapacity(StaticFields staticFields) {
		int new_length = staticFields.renumb_insts.length * 2;
		CEntity [] new_insts = new CEntity[new_length];
		System.arraycopy(staticFields.renumb_insts, 0, new_insts, 0, staticFields.renumb_insts.length);
		staticFields.renumb_insts = new_insts;
		long [] new_ids = new long[new_length];
		System.arraycopy(staticFields.saved_ids, 0, new_ids, 0, staticFields.saved_ids.length);
		staticFields.saved_ids = new_ids;
	}


	private void ensureNamesCapacity(StaticFields staticFields) {
		int new_length = staticFields.ren_models.length * 2;
		SdaiModel [] new_ren_models = new SdaiModel[new_length];
		System.arraycopy(staticFields.ren_models, 0, new_ren_models, 0, staticFields.ren_models.length);
		staticFields.ren_models = new_ren_models;
		String [] new_old_names = new String[new_length];
		System.arraycopy(staticFields.old_names, 0, new_old_names, 0, staticFields.old_names.length);
		staticFields.old_names = new_old_names;
	}


	/**
	 * Writes this schema instance in XML representation to specified stream.
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


/**
 * Informs if remote schema instance data base id is equal to specified one.
 * @param schemaId <code>long</code> value of the remote schema instance data base id.
 * @throws SdaiException
 * @return <code>true</code> if schema instance is remote and its id is equal to specified one, <code>false</code> otherwise
 */
    protected boolean hasId(long schemaId) throws SdaiException {
		SchemaInstanceRemote schInstRemote = getSchInstRemote();
        if (schInstRemote != null) {
            return (schemaId == schInstRemote.getRemoteId());
        }
        else return false;
    }

	protected final SdaiRepository getRepositoryFast() {
		return repository;
	}

	protected final SdaiRepositoryRemote getRepositoryRepoRemote() {
		return repository.getRepoRemote();
	}

	protected static SdaiModelRemote getModRemote(SdaiModel model) {
		return model.getModRemote();
	}

/** DRAFT.
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
	/*public*/private void startReadOnlyAccess() throws SdaiException { }

/** DRAFT.
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
	/*public*/private void startReadWriteAccess() throws SdaiException { }

/** DRAFT.
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
	/*public*/private void promoteToReadWrite() throws SdaiException { }

/** DRAFT.
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
	/*public*/private void reduceToReadOnly() throws SdaiException { }

/**
 * Returns the set of <code>SchemaInstance</code>s included by the
 * current schema instance.
 * The inclusion relation defined on the set of schema instances forms
 * a graph, which must be acyclic.
 * @return the set of schema instances included by this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @since 4.2.0
 */
	public abstract ASchemaInstance getIncludedSchemaInstances() throws SdaiException;


/**
 * Adds a new member to the set of schema instances
 * that are included by this schema instance.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * The inclusion relation defined on the set of schema instances forms
 * a graph, which must be acyclic. Therefore, the submitted schema instance
 * is checked against this condition. If inclusion is found to violate this condition,
 * then SdaiException VA_NVLD is thrown. The exception of the same type is
 * also thrown in the case of autoinclusion - when a schema instance is
 * submitted to itself - and also in the case where a schema instance
 * provided as a parameter belongs to "SystemRepository".
 * <p> Applying inclusion operation to a <code>SchemaInstance</code> in
 * "SystemRepository" is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param schemaInstance the <code>SchemaInstance</code> submitted to include into
 * disposition of this schema instance.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @since 4.2.0
 */
	public void addSchemaInstance(SchemaInstance schemaInstance) throws SdaiException {
		addSchemaInstanceCommon(schemaInstance);
	}

	protected void addSchemaInstanceCommon(SchemaInstance schemaInstance) throws SdaiException {
		if (repository == null || schemaInstance.repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (schemaInstance == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if (schemaInstance.repository == SdaiSession.systemRepository) {
			String base = SdaiSession.line_separator + AdditionalMessages.SI_SYRE;
			throw new SdaiException(SdaiException.VA_NVLD, base);
		}
		if (schemaInstance == this) {
			String base = SdaiSession.line_separator + AdditionalMessages.SI_AUTO;
			throw new SdaiException(SdaiException.VA_NVLD, base);
		}
		if (included_schemas == null) {
			getIncludedSchemaInstances();
		}
		if (schemaInstance.isIncluded(this)) {
			String base = SdaiSession.line_separator + AdditionalMessages.SI_CYCL;
			throw new SdaiException(SdaiException.VA_NVLD, base);
		}
		included_schemas.addUnorderedRO(schemaInstance);
		modified = true;
	}

	boolean isIncluded(SchemaInstance sch_inst) throws SdaiException {
		ASchemaInstance included = getIncludedSchemaInstances();
		for (int i = 0; i < included.myLength; i++) {
			SchemaInstance si = (SchemaInstance)included.myData[i];
			if (si == sch_inst) {
				return true;
			}
			boolean res = si.isIncluded(sch_inst);
			if (res) {
				return true;
			}
		}
		return false;
	}


/**
 * Removes a specified member from the set of schema instances
 * that are included by this schema instance.
 * If null value is passed to the method's parameter,
 * then SdaiException VA_NSET is thrown.
 * <p> Applying removal operation to a <code>SchemaInstance</code> in "SystemRepository"
 * is forbidden.
 * In the case of such an attempt, SdaiException VT_NVLD is thrown.
 * @param schemaInstance the <code>SchemaInstance</code> that is to be removed
 * from the set of included schema instances.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR,  underlying system error.
 * @since 4.2.0
 */
		public void removeSchemaInstance(SchemaInstance schemaInstance) throws SdaiException {
		if (repository == null || schemaInstance.repository == null) {
			throw new SdaiException(SdaiException.SI_NEXS);
		}
		if (schemaInstance == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
		if (repository == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		SdaiSession session = repository.session;
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode != SdaiTransaction.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}

		if (included_schemas == null) {
			getIncludedSchemaInstances();
		}
		included_schemas.removeUnorderedRO(schemaInstance);
		modified = true;
	}

/**
 * Returns the set of <code>SdaiModel</code>s associated either with this
 * schema instance or a schema instance reached from this schema instance
 * following the inclusion relation (directly or indirectly).
 * Duplicate appearances of a model are not taken into account.
 * @return the set of models referenced by this schema instance.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException SI_NEXS, schema instance does not exist.
 * @since 4.2.0
 */
	public ASdaiModel getReferencedModels() throws SdaiException {
		return getReferencedModelsCommon();
	}


	protected ASdaiModel getReferencedModelsCommon() throws SdaiException {
		check_schemaInstance();
		ASdaiModel ref_mods = new ASdaiModel(SdaiSession.setType0toN, this);
		ref_mods.myData = new Object[SessionAggregate.INIT_SIZE_SET_SESSION];
		ref_mods.myLength = 0;
		return collectReferencedModels(ref_mods);
	}


	protected ASdaiModel collectReferencedModels(ASdaiModel ref_mods) throws SdaiException {
		int i;
		ASdaiModel assoc_mods = getAssociatedModels();
		if (assoc_mods.myData != null && assoc_mods.myLength > 0) {
			int new_ln = ref_mods.myLength + assoc_mods.myLength;
			if (new_ln > ref_mods.myData.length) {
				ref_mods.ensureCapacity(new_ln);
			}
			for (i = 0; i < assoc_mods.myLength; i++) {
				ref_mods.try_to_addUnordered((SdaiModel)assoc_mods.myData[i]);
			}
		}
		ASchemaInstance included = getIncludedSchemaInstances();
		for (i = 0; i < included.myLength; i++) {
			SchemaInstance si = (SchemaInstance)included.myData[i];
			if (si == this) {
				SdaiSession ss = repository.session;
				if (ss != null && ss.logWriterSession != null) {
					ss.printlnSession(AdditionalMessages.SI_LOOP + si.name + ".");
				} else {
					SdaiSession.println(AdditionalMessages.SI_LOOP + si.name + ".");
				}
				continue;
			}
			ref_mods = si.collectReferencedModels(ref_mods);
		}
		return ref_mods;
	}

	protected void newIncludedSchemas() {
		included_schemas = new ASchemaInstance(SdaiSession.setType0toN, this);
	}

	protected void addSchemaInstanceInternal(SchemaInstance schemaInstance) throws SdaiException {
		included_schemas.addUnorderedRO(schemaInstance);
	}

}