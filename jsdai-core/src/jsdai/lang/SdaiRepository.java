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
import java.util.*;

import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.query.SdaiRepositoryRef;
import jsdai.query.SerializableRef;
import jsdai.xml.InstanceReader;
import jsdai.xml.InstanceWriterSelector;
import jsdai.xml.SdaiInputSource;

/**
An <code>SdaiRepository</code> is the <b>physical container</b> to store persistently:
<br>- <code>SdaiModel</code>s with entity instances within them;
<br>- <code>SchemaInstance</code>s which logically group <code>SdaiModel</code>s.
<br>Throughout this documentation repository is used as a synonym for <code>SdaiRepository</code>.
<p>

 * Conceptionally, the <code>SdaiRepository</code> can be represented by
 * the following EXPRESS entity:

 * <P><TT><pre>ENTITY SdaiRepository;
 *  {@link #getName name} : String;
 *  {@link #getLocation location} : OPTIONAL Object;
 *  {@link #getDescription description} : <A HREF="../../jsdai/lang/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getModels models}       : <A HREF="../../jsdai/lang
/ASdaiModel.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/SdaiModel.html">SdaiModel</A>;
 *  {@link #getSchemas schemas}      : <A HREF="../../jsdai/lang
/ASchemaInstance.html">SET</A> [0:?] OF <A HREF="../../jsdai/lang/SchemaInstance.html">SchemaInstance</A>;
 *  {@link #getChangeDate changeDate} : String;
 *  {@link #getAuthor author}       : <A HREF="../../jsdai/lang/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getOrganization organization}       : <A HREF="../../jsdai/lang
/A_string.html">LIST</A> [1:?] OF String;
 *  {@link #getPreprocessorVersion preprocessorVersion} : String;
 *  {@link #getOriginatingSystem originatingSystem} : String;
 *  {@link #getAuthorization authorization} : String;
 *  {@link #getDefaultLanguage defaultLanguage} : OPTIONAL String;
 *  {@link #getContextIdentifiers contextIdentifiers}     : <A HREF="../../jsdai
/lang/A_string.html">LIST</A> [0:?] OF String;
 *INVERSE
 *  {@link #getSession session}  : <A HREF="../../jsdai/lang
/SdaiSession.html">SdaiSession</A> FOR {@link SdaiSession#getKnownServers knownServers};
 *UNIQUE
 *  UR1: name, session;
 *END_ENTITY;</TT></pre>
 * <P>

 * The attributes <code>description</code>, <code>author</code>, <code>organization</code>,
 * <code>defaultLanguage</code> and <code>contextIdentifiers</code>
 * are read-write, whereas all other attributes are read-only.
 * For each attribute a corresponding get method (or methods in the case of <code>changeDate</code>)
 * is defined. The <code>changeDate</code> can be retrieved either as <code>String</code> or
 * as <code>long</code>. The attributes <code>preprocessorVersion</code>, <code>originatingSystem</code>,
 * <code>authorization</code> and <code>defaultLanguage</code> also have corresponding set methods.

<p>
The <strong>persistent storage</strong> is realized through the special
directories containing binary data files.
For every <code>SdaiRepository</code> a corresponding directory with the
<code>SdaiRepository</code> name exists. By default these directories are placed
into a special repositories-directory in the local file system.
This is specified by the property "repositories" in the configuration file
jsdai.properties.
<P><B>Example:</B>
<P><TT><pre>    repositories=D:\\repos</pre></TT>
Other locations are also possible.
A user should never manipulate these directories and binary data files.
An access to them is allowed only through the JSDAI methods.
<p>
In general, the repositories can be accessed from either the local file system
or remotely from a JSDAI-Server.
After opening a session all repositories are closed, except a special one
named "SystemRepository" which contains dictionary and mapping data.
Each model in this repository can be at most read-only, that is, an attempt
to set read-write mode to such models will lead to an <code>SdaiException</code>.
Repositories can be opened with openRepository()
and closed with closeRepository() method.
<p>
Repositories are mainly selected by SdaiSession.getKnownServers() to get
all available repositories or by SdaiSession.getActiveServers() to get
only the currently open ones. Initially, after openSession(), the set returned by
getKnownServers() contains all <code>SdaiRepositories</code> located in the
special repositories-directory.
The remote repositories or repositories located at other places on the file system
can be added to the known servers with linkRepository() method. New repositories are created with the
<code>SdaiSession</code> methods createRepository() and importClearTextEncoding().
No longer needed repositories are removed with deleteRepository() method.
<p>
Each repository has a unique non-empty <strong>name</strong> for identification. The
name is assigned by createRepository() or importClearTextEncoding() methods
and cannot be changed afterwards. During creation of a repository an empty
string can be passed to the name parameter to signal JSDAI to create a temporary repository.
The name of the local repository can consist only of the following characters:
upper case letters from 'A' to 'Z', lower case letters from 'a' to 'z',
digits '0', '1',..., '9' and underscore '_'. Moreover, the name must start from
a letter. If in importClearTextEncoding() method the repository name is extracted from
the header of the imported file, this name is corrected to conform the
above rules. In the case of the remote repository, its name, in addition, is
allowed to contain the following characters: "file://", "/" and ".".
<p>
The repositories need to be <strong>located</strong> somewhere. The location is either
<br>- local in the default repositories-directory;
<br>- local in the specified directory as <a href="../../../guides/SDAIFile.html">SDAI file</a>;
<br>- remote on a JSDAI-Server;
<br>- unknown in the case of a degenerated repository (see below).
<p>
The actual location can be accessed with getLocation() method.
By default the repositories are created locally in the specific
repositories-directory. But it is possible to put repositories in other
places, either locally or remotely. Locally, on an available file system
by specifying a path during creation, or remotely by specifying the domain
or INET of the JSDAI-Server. However, only repositories located in the default
repositories-directory are by default available after openSession().
The location of a repository cannot be changed once
it is created except for that the location gots invalid.
<p>
It is not possible to create a repository on an unknown location.
But a repository may contain instances which reference other instances in
other repositories which do not belong to <code>knownServers</code>.
<br>In this case only the name of the referenced repositories is known, but not
their location. We call them degenerated repositories. Trying to open a
degenerated repository results in the
<code>SdaiException</code> "RP_NAVL - Repository not available".
If getLocation() is applied to such a repository, then the
<code>SdaiException</code> "VA_NEXS - Value does not exist" is thrown.
The degenerated repositories can be promoted to ordinary repositories
with importClearTextEncoding() or linkRepository() methods by using
the same repository name.
<p>
With linkRepository() method it is possible to
link local or remote repositories to the current session.
With unlinkRepository() closed and for the user not needed repositories can be
made unknown to the current session.
<p>
The methods createRepository(), importClearTextEncoding() and linkRepository()
have parameters for the repository name and the repository location.
The following combinations of values for these parameters are allowed:
<p>1) null, xxx : This is possible only for importClearTextEncoding() method.
If this case happens, the repository name is taken from the entity
<code>file_name</code> in the header of an exchange structure from
which the encoding is imported.
<p>2)  String1, xxx: String1 is used as the repository name.
<p>3) "", null : A temporary repository is created with an artificial name.
Temporary repositories are automatically removed during closeSession() and
are not available after the next openSession().
<p>4) xxx, String2: The repository is created at the location indicated by String2.
If String2 ends with ".sdai" then repository stored in
<a href="../../../guides/SDAIFile.html">SDAI file</a> is created.
If String2 ends with ".sdai" and the first parameter is null or empty string
then repository name is derived from <a href="../../../guides/SDAIFile.html">SDAI file</a> name.
<p>5) String1, String2: String1 is the name of a remote repository.
String2 is the username and password with the prefix "//" and the last character "@". Example:
"//guest:passwd@".
A server for remote access should be named by prefixing it with "//" and giving
the domain name.
Example:
Repository "test1" on the domain "lksoft.de"
has to be named "//test1.lksoft.de".
<p>
Repositories have two sets of attributes: header properties and contents.
<br>The header properties are the following:
<br>- description
<br>- time_stamp
<br>- author
<br>- organization
<br>- preprocessor_version
<br>- originating_system
<br>- authorization
<br>- default_language
<br>- context_identifiers.
<br>The contents consists of:
<br>- models
<br>- schemas.
<p>
To access the header properties for reading, the repository must be
opened. To modify the header attributes, a read-write transaction has to
be started additionally.
<p>
To access the contents data, the repository must be opened and
either a read-only or a read-write transaction has to be started.
When transaction access is ended, the contents of a
repository is no longer available. The repository contents cannot be
manipulated directly by an application. Manipulations are only possible
with the following methods:
<br>- createSdaiModel()
<br>- deleteSdaiModel()
<br>- renameSdaiModel()
<br>- createSchemaInstance()
<br>- delete() (in class <code>SchemaInstance</code>).
<p>
 * @see "ISO 10303-22::7.4.3 sdai_repository and 7.4.4 sdai_repository_contents"
 */


//------------------- Repository header binary file ----------------
//   filename: header
	/* Format of file RepositoryHeader:
	file:=
		'H'   // file indicator
		INTEGER //build number
		'B'    // Beginnig
		INTEGER //count of descriptions
		{STRING}		//description
		STRING //implementation level
		'B'	//new section
		STRING // repo.name
		STRING // repo.changedDate : timeStamp
		INTEGER //count of repo.authors
		{ STRING }		// repo.author[]
		INTEGER //count of repo.organizations
		{ STRING }		// repo.organization[]
		STRING // repo.preprocesor verion
		STRING // repo.orginatingSystem
		STRING // repo.authorization
		'B'	// new section
		INTEGER // count of used schema names
		{ STRING }		// used schema name[]
		'B' // new section
		INTEGER // count of languages
		[ 'M'
			STRING  // default/general language
		]
		{ 'P'
			STRING //section name
			STRING //language
		}
		'B' // new section
		{'P' | 'M' if 'P' {
				STRING //section name
				INTEGER //count of context names
				{STRING} //context name

			} else {
				INTEGER //count of context names
				{STRING} //context name
			}
		}
		'E' //end of model


//------------------- repository contents binary file ----------------
   filename: contents
	/* Format of file RepositoryContents:
	file :=
		'C'   // file indicator
		INTEGER 	  // build number
		[ 'I'
			LONG // largest instance_identifier
		]
		'S'   // section flag for schema instances
		INTEGER // no of schema definitions
		{ SCHEMA_DEFINITION }  // the schema definitions
		'S'   // section flag for SchemaInstances
		INTEGER  // no of schema instances
		{ SCHEMA_INSTANCE }  // the schema instances
		;

SCHEMA_DEFINITION :=
		STRING   // name of schema definition/
SCHEMA_INSTANCE :=
		'B'    // indicate beginning of schema_instance
		STRING // name of schema instance
		STRING // native_schema : schema_definition
		STRING // change_date
		STRING // validation_date
		BYTE   // validation_result: true, flase
		INTEGER // validation_level
		INTEGER // no. of assciated_models
		{ ASSCOCIATED_MODEL }  // the associated_models

		'S'     // model section
		INTEGER  // no. of models
		{ SDAI_MODEL }

ASSCOCIATED_MODEL :=
		STRING  // name of model
		STRING  // name of repository
		;

SDAI_MODEL :=
		'B'    // Beginnig
		STRING // name of model
		( ( 'P' STRING ) // further underlying_schemas (not used today)
		| ( 'M' )
		)
		STRING  // underlying_schema
		STRING // change_date
		INTEGER // count of asociated schema instances
		{STRING} //asociated schema instance name

	*/


public abstract class SdaiRepository extends SdaiCommon implements SdaiEventSource, QuerySource {
/**
	EXPRESS attributes of SdaiRepository, see ISO 10303-22::7.4.3 sdai_repository.
*/
	String name;
	A_string description;
	SdaiSession session;

/**
	EXPRESS attributes from SdaiRepositoryContents, see ISO 10303-22::7.4.4 sdai_repository_contents.
*/
	ASdaiModel models;
	ASchemaInstance schemas;

/**
	The location of this repository (it may be either local or a remote one).
*/
	Object location;
	String destinationLocation;

/**
	The default language for string values in entity instances of the SdaiModels
	in this repository which do not have specified their own default languages.
*/
	String language;

/**
	An aggregate containing information describing the contexts
	within which the instances of the SdaiModels in this repository are applicable.
	This field is used only by those SdaiModels which do not have their own
	context_identifiers assigned.
*/
	A_string context_identifiers;



// A new code due to ejbclient

/**
	An instance of the class SdaiRepositoryHeader which contains information
	about the repository. In a particular case this information is extracted
	from the instances of the following entities in the header section of an
	exchange structure:
	 - file_description;
	 - file_name;
	 - section_language;
	 - section_context.
*/
//	SdaiRepositoryHeader repoHeader;

	String implementation_level;
	FILE_NAME file_name;
	FILE_SCHEMA file_schema;
	SECTION_LANGUAGE [] languages;
	SECTION_CONTEXT [] contexts;
	$HEADER_USER_DEFINED_ENTITY [] user_defined_instances;
	int languages_count;
	int contexts_count;
	int user_defined_instances_count;
	final int LANGUAGES_ARRAY_SIZE = 16;
	final int CONTEXTS_ARRAY_SIZE = 16;
	final int USER_DEFINED_INSTANCES_ARRAY_SIZE = 16;
	final String ASTERISK = "*";

	boolean virtual;

// End of a new code due to ejbclient


/**
	The name of part-21 file related with this repository.
	The field takes a value in the following way:
	1) If repository is constructed using importClearTextEncoding method,
	   then the value of the parameter 'source_location' is adopted;
	2) If repository is constructed using createRepository method,
	   then the value is the repository name submitted to this method;
	3) If repository is linked by loading 'repository' binary file,
	   then the value is the repository name extracted from this file.
	The value is used:
	1) As a name for the part-21 file produced by exportClearTextEncoding method;
	2) For construction of warning and error messages written to logo file
	   during reading of a part-21 file;
	3) In method getDescription in class CEntity.
*/
	String physical_file_name;

/**
	The aggregate containing instances of SdaiListener class.
*/
	protected CAggregate listenrList;

/**
	The count of models in this repository. This value is used to set
	the field 'identifier' in the class SdaiModel.
*/
	int model_count;

/**
	The count of schema instances in this repository.
*/
	int schema_count;

	int unresolved_mod_count;

	int unresolved_sch_count;

/**
	Highest number used in persistent labels for instances in this repository.
*/
	protected long largest_identifier;

    protected long current_identifier;

    protected long next_used_identifier;

/**
	JSDAI build number at the moment when this repository was created.
*/
	int build_number;

/**
	Some thread indicator used by server.
*/
	int serverThread;

/**
	Has value 'true' if and only if this repository is open.
*/
	boolean active;

/**
	Has value 'true' if and only if something within the repository but not within
	its contents is modified (for example, preprocessor_version, authorization,
	default_language and so on).
	Modification of repository contents is reflected by analogous flags in
	models and schema instances contained in the repository.
	The value is switched to 'false' during a commit operation.
*/
	boolean modified;

/**
	Has value 'true' if the data of this repository (but not its contents) were
	written to binary file 'repository' (using commit operation in SdaiTransaction).
	This flag is used to load the binary file in openRepository and abort methods.
*/
	boolean committed;

/**
	Has value 'true' if this repository was constructed using either method
	createRepository or method importClearTextEncoding.
*/
	boolean created_or_imported;

/**
	Has value 'true' if some model was removed from this repository.
	The value is set back to 'false' during transaction commit or abort operations.
*/
	boolean model_deleted;

/**
	Has value 'true' if this repository is remote created using
	createRepository method.
*/
	boolean remote_created;

/**
	Has value 'true' if some schema instance was removed from this repository.
	The value is set back to 'false' during transaction commit or abort operations.
*/
	boolean schema_instance_deleted;

/**
	Has value 'true' if ordinary names of entities for instances in models
	of this repository are used (to produce part-21 files).
	If value is 'false', then acronyms of entity names are preferred.
*/
	boolean suppress_short_names;

/**
	Has value 'true' if this repository is temporary, that is, its
	existence is ceased during execution of closeSession method.
*/
	boolean temporary;

	boolean sch_inst_created_during_simulation;

	boolean model_created_during_simulation;

	boolean internal_usage;

	boolean extracting_model;

	boolean was_opened;

	boolean importing;

	boolean currently_aborting;

/**
	The name of the directory in which the binary files attributed to this
	repository are stored. For example, "r15".
	The value of this field is returned by public method getId().
*/
	String dir_name;

/**
	The build number starting from which the format of the binary file
	'repository' is extended by including some additional information
	on schema instances.
*/
	final int BUILD_WITH_SCHEMA_INSTANCE_EXTENSION = 234;

/**
	The (initial) size of the internal array 'schema_names' used for
	saving/loading data to/from the 'repository' binary file.
*/
	final int SCHEMAS_ARRAY_SIZE = 32;

/** The following fields are needed only for remote repository connection.
	The connection between client(=SdaiRepository) and server exists as long
	as repository is open.
*/
	Socket sock, sockListen;
	InputStream is;
	OutputStream os;
	DataInput istream;
	DataOutput ostream;
	int port = 16384; // SdaiServer default port
	String user; //user name and password user@passwd
	String addr; //internet address
	String conThread; // connection to server number


	/** Holds this model entity data which was queried by application */
	protected HashMap entityExternalData;
	/** Holds this model removed data before commit */
	protected HashMap entityRemovedExternalData;


/**
	Returns an owner of this repository.
*/
	SdaiCommon getOwner() {
		return session;
	}


/**
	Sets field 'modified' with value 'true'.
*/
	void modified() throws SdaiException {
		modified = true;
	}


/**
	Assigns the name to this repository.
	Checks if a repository with the submitted name already exists. If so,
	RP_DUP is thrown.
	Also assigns the location to the repository (either provided or some
	specific directory).
*/
	void setNameLocation(String name, Object location) throws SdaiException {
		this.name = name;
		if (name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		for (int i = 0; i < session.known_servers.myLength; i++) {
			SdaiRepository rep = (SdaiRepository)session.known_servers.myData[i];
			if (rep != this && rep.name.equals(name)) {
				throw new SdaiException(SdaiException.RP_DUP, rep,
					AdditionalMessages.AX_NAME + name);
			}
		}
		if (location != null) {
			if (!(location instanceof String)) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			this.location = (String) location;
		} else {
//			session.repo_count++;
//			dir_name = "r" + session.repo_count;
			dir_name = "r" + SdaiSession.incRepoCount();
			this.location = SdaiSession.repos_path + java.io.File.separator + dir_name;
		}
	}


/**
	Used to create a new ordinary repository in methods linkRepository, createRepository,
	importClearTextEncoding and also to create systemRepository in openSession method.
*/
	protected SdaiRepository(SdaiSession session, String name, Object location, boolean fCheckNameLocation)
			throws SdaiException {
		initLocal(session, name, location, fCheckNameLocation);
	}

/**
	Used to create a new virtual repository.
*/
	protected SdaiRepository(SdaiSession session, String name) throws SdaiException {
		initLocal(session, name);
	}

	// Noop constructor for subclass creation. If this constructor is used
	// the object has to be initialized by initLocal(...)
	protected SdaiRepository() { }

	protected void initLocal(SdaiSession session, String name, Object location,
							 boolean fCheckNameLocation) throws SdaiException {
		if (fCheckNameLocation) {
			if(name != null && name.equals(this.name)) {
				throw new SdaiException(SdaiException.RP_DUP, this, AdditionalMessages.AX_NAME + name);
			}
			this.session = session;
			setNameLocation(name, location);
		} else {
			this.session = session;
			this.name = name;
			this.location = (String) location;
		}
	// from contents
		models = new ASdaiModel(SdaiSession.setType0toN, this);
		schemas = new ASchemaInstance(SdaiSession.setType0toN, this);
	// other
		if(!name.startsWith(SdaiSession.LOCATION_PREFIX)&&location !=null && ((String)location).startsWith(SdaiSession.LOCATION_PREFIX)) {
			takeServerProp();
		}
		build_number = Implementation.build;
		initializeRepository();
	}

	protected void initLocal(SdaiSession session, String name) throws SdaiException {
		virtual = true;
		this.session = session;
		this.name = name;
		models = new ASdaiModel(SdaiSession.setType0toN, this);
		temporary = false;
		build_number = Implementation.build;
//		System.out.println("Virtual REPO constructor called!");
	}

/**
	Sets some repository fields with values.
	Used either in the constructor for ordinary repository or to promote
	a virtual repository to an ordinary one.
*/
	private void initializeRepository() throws SdaiException {
		virtual = false;
		active = false;
		modified = false;
		committed = false;
		created_or_imported = false;
		model_deleted = false;
		schema_instance_deleted = false;
		remote_created = false;
		suppress_short_names = true;
		temporary = false;
		internal_usage = false;
		serverThread = 0;
		language = null;
		unresolved_mod_count = 0;
		unresolved_sch_count = 0;
		entityExternalData = null;
		entityRemovedExternalData = null;
        next_used_identifier = -1;
	}


/**
	Promotes a virtual repository to an ordinary one.
*/
	void promoteVirtualToOrdinary(Object location, boolean fCheckNameLocation) throws SdaiException {
//		synchronized (syncObject) {
		if (fCheckNameLocation) {
			setNameLocation(name, location);
		} else {
			this.location = (String) location;
		}
		schemas = new ASchemaInstance(SdaiSession.setType0toN, this);
		initializeRepository();
//		} // syncObject
	}


/**
 * Returns the name of the repository as a <code>String</code>.
 * <p> The repositories known to an <code>SdaiSession</code> must have unique names.
 * An exception will be thrown if the repository is deleted or if the
 * <code>SdaiSession</code> is closed.
 * @return the name of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 */
	public String getName() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return name;
	}


/**
 * Returns repository identifier as a <code>String</code>.
 * <p> The repositories within a <code>SdaiSession</code> have unique identifiers,
 * which are not reused when some repositories are deleted and some new created
 * or imported.
 * @return the identifier of this <code>SdaiRepository</code>.
 * @throws SdaiException RP_NEXS, repository does not exist.
 */
   public String getId() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return dir_name;
	}


/**
 * Returns the location of the repository as a <code>String</code>.
 * This is either
 * <br>- null for unknown location or
 * <br>- a path within a locally available file system or
 * <br>- when starting with the prefix "//", the INET or domain of a remote JSDAI-Server.
 * If the repository is degenerated, then SdaiException VA_NEXS is thrown.
 * @return the location of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException VA_NEXS, value does not exist.
 */
	public String getLocation() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (schemas == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
//		synchronized (syncObject) {
		if (location instanceof String) {
			if(((String)location).startsWith(SdaiSession.LOCATION_PREFIX)) {
				String loc = (String)location;
				if(loc.indexOf(":") == -1) return loc;
				loc = loc.substring(0, loc.indexOf(":")+1) + "******" + loc.substring(loc.indexOf("@"));
				return loc;
			}
			return (String)location;
		}
		return null;
//		} // syncObject
	}


/**
 * Removes the repository from the current session, but does not delete it
 * physically. As a result, the Java object, representing the repository becomes invalid and
 * this object is no longer included in SdaiSession.knownServers.
 * The physical repository however remains intact and can later accessed again with linkRepository.
 * This method can be applied only to closed repositories.
 * If the repository is degenerated, then SdaiException FN_NAVL is thrown.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_OPN, repository open.
 * @throws SdaiException RP_NAVL, repository not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @see SdaiSession#linkRepository
 */
	public void unlinkRepository() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (schemas == null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (active) {
			throw new SdaiException(SdaiException.RP_OPN, this);
		}
//		synchronized (syncObject) {
		session.known_servers.removeUnorderedRO(this);
		session = null;
//		} // syncObject
	}


/**
 * Returns an aggregate of strings <code>A_string</code> containing optional
 * descriptions about the <code>SdaiRepository</code>.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * this aggregate is always empty, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} it contains
 * strings taken from the field <code>description</code> of the entity
 * <code>file_description</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.1 file_description).
 * That is, this aggregate always exists, but may be empty.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the content of this aggregate will be written to the exchange structure
 * created by the method being applied.
 * The method is available only if the repository is open.
 * @return aggregate containing informal description of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.1 file_description."
 */
	public A_string getDescription() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return description;
	}


/**
 * Returns the only one <code>SdaiSession</code> object. If session is closed,
 * SdaiException RP_NEXS is thrown.
 * @return the current session.
 * @throws SdaiException RP_NEXS, repository does not exist.
 */
	public SdaiSession getSession() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return session;
	}


/**
 * Returns a read-only aggregate of all the <code>SdaiModels</code> of this repository.
 * The aggregate is automatically updated when new models are created with
 * {@link #createSdaiModel createSdaiModel} or removed by
 * {@link SdaiModel#deleteSdaiModel deleteSdaiModel}.
 * The models of a repository are only accessible if the repository is open.
 * @return aggregate containing all models of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #openRepository
 */
	public ASdaiModel getModels() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (models != null) {
			return models;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Returns a read-only aggregate of all <code>SchemaInstances</code> of this repository.
 * The aggregate is automatically updated when new schema instances are
 * created with {@link #createSchemaInstance createSchemaInstance} or removed by
 * {@link SchemaInstance#delete delete}.
 * The schema instances of a repository are only accessible if the repository is open.
 * @return aggregate containing all schema instances of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #openRepository
 */
	public ASchemaInstance getSchemas() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (schemas != null) {
			return schemas;
		} else {
			throw new SdaiException(SdaiException.VA_NSET);
		}
	}


/**
 * Returns the date and time specifying when this repository was
 * created or most recently modified. The format of this description
 * is specified in ISO 8601 (for details, see ISO 10303-21::9.2.2 file_name).
 * <p> The time stamp is updated each time when
 * {@link SdaiTransaction#commit commit} method is applied,
 * provided either the repository has been modified since the most recent commit
 * or abort operation was performed,
 * or no such operation was performed thus far but the repository was
 * established by {@link SdaiSession#createRepository createRepository}
 * method. If, however, for the repository no such case has appeared,
 * then the time stamp is set as follows.
 * If the repository was introduced by
 * {@link SdaiSession#createRepository createRepository} method, then
 * the time of its creation is taken. The same rule applies also to
 * the "SystemRepository". If the repository was created using
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} method,
 * then the time stamp is borrowed from the entity
 * <code>file_name</code> in the header of an exchange structure from
 * which the encoding was imported.
 * Finally, if the repository was established using
 * {@link SdaiSession#linkRepository linkRepository} method, then
 * the time stamp is extracted from the data of the repository linked to
 * the session.
 * The method is available only if the repository is open.
 * <P><B>Example of the time stamp:</B>
 * <P><TT><pre>    Calendar date: 28 June 2000
 *    Time of the day: 15 minutes 12 seconds past 17 hours
 *    Above date and time encoded according to ISO 8601:
 *        2000-06-28T17:15:12</pre></TT>
 * @return time stamp of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #getChangeDateLong
 * @see SdaiTransaction#commit
 * @see SdaiSession#createRepository
 * @see SdaiSession#importClearTextEncoding
 * @see SdaiSession#linkRepository
 * @see "ISO 10303-21::9.2.2 file_name."
 */
	public String getChangeDate() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.time_stamp;
	}


/**
 * Returns <code>long</code> value of the date and time specifying when this
 * repository was created or most recently modified.
 * This value is updated each time when
 * {@link SdaiTransaction#commit commit} method is applied,
 * provided either the repository has been modified since the most recent commit
 * or abort operation was performed,
 * or no such operation was performed thus far but the repository was
 * established by {@link SdaiSession#createRepository createRepository}
 * method. If, however, for the repository no such case has appeared,
 * then this value is set as follows.
 * If the repository was introduced by
 * {@link SdaiSession#createRepository createRepository} method, then
 * the time of its creation is taken. The same rule applies also to
 * the "SystemRepository". If the repository was created using
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} method,
 * then the date is borrowed from the entity
 * <code>file_name</code> in the header of an exchange structure from
 * which the encoding was imported.
 * Finally, if the repository was established using
 * {@link SdaiSession#linkRepository linkRepository} method, then
 * the date is extracted from the data of the repository linked to
 * the session.
 * <p> If time value is obtained as a string from an exchange structure
 * processed by {@link SdaiSession#importClearTextEncoding importClearTextEncoding}
 * method and this string violates the rules for time stamps stated in
 * "ISO 10303-21::8.2.2", then SdaiException VA_NVLD is thrown.
 * The method is available only if the repository is open.
 * @return a <code>long</code> representing the date and time.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NVLD, value invalid.
 * @see #getChangeDate
 * @see SdaiTransaction#commit
 * @see SdaiSession#createRepository
 * @see SdaiSession#importClearTextEncoding
 * @see SdaiSession#linkRepository
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public long getChangeDateLong() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		try {
//			synchronized (syncObject) {
				return session.cal.timeStampToLong(file_name.time_stamp);
//			} // syncObject
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
	}


/**
 * Returns an aggregate of strings <code>A_string</code> identifying the person
 * responsible for creating this repository. The aggregate shall have
 * its name at the first position and mail and/or email addresses at the following
 * positions.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * this aggregate is always empty, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} it contains
 * strings taken from the field <code>author</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * That is, this aggregate always exists, but may be empty.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * It is a responsibility of the application to fill this aggregate with
 * useful information.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the content of this aggregate will be written to the exchange structure
 * created by the method being applied.
 * The method is available only if the repository is open.
 * @return aggregate identifying an author of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public A_string getAuthor() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.author;
	}


/**
 * Returns an aggregate of strings <code>A_string</code> containing the organizations with
 * whom the author of this repository is associated.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * this aggregate is always empty, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} it contains
 * strings taken from the field <code>organization</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * That is, this aggregate always exists, but may be empty.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * It is a responsibility of the application to fill this aggregate with
 * useful information.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the content of this aggregate will be written to the exchange structure
 * created by the method being applied.
 * The method is available only if the repository is open.
 * @return aggregate that lists organizations an author of this repository is associated.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public A_string getOrganization() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.organization;
	}


/**
 * Returns a <code>String</code> characterizing the system used to
 * create this repository.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * some default value to this string is assigned, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} this
 * string has value taken from the field <code>preprocessor_version</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * The method is available only if the repository is open.
 * @return string describing the system used to create this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setPreprocessorVersion
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getPreprocessorVersion() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.preprocessor_version;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the system used to create this repository.
 * This assignment is allowed only if the repository is open.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the value assigned will be written to the exchange structure
 * created by the method being applied.
 * @param value string describing the system used to create this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getPreprocessorVersion
 * @see #openRepository
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setPreprocessorVersion(String value) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		checkServerRWAccess();
		file_name.preprocessor_version = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> characterizing the system from
 * which the data contained in this repository are originated.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * some default value to this string is assigned, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} this
 * string has value taken from the field <code>originating_system</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * The method is available only if the repository is open.
 * @return string describing the system that is a source of the data in this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setOriginatingSystem
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getOriginatingSystem() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.originating_system;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the system from which the data contained in this repository are originated.
 * This assignment is allowed only if the repository is open.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the value assigned will be written to the exchange structure
 * created by the method being applied.
 * @param value string describing the system that is a source of the data in this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getOriginatingSystem
 * @see #openRepository
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setOriginatingSystem(String value) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		checkServerRWAccess();
		file_name.originating_system = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> containing the name and mailing address
 * of the person who authorizes sending of the exchange structure
 * exported from this repository.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * some default value to this string is assigned, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} this
 * string has value taken from the field <code>authorization</code> of the entity
 * <code>file_name</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.2 file_name).
 * The method is available only if the repository is open.
 * @return string describing the person who authorizes sending of the exchange structure.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setAuthorization
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public String getAuthorization() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return file_name.authorization;
	}


/**
 * Assigns a <code>String</code> value to the attribute characterizing
 * the person who authorizes sending of the exchange structure
 * exported from this repository.
 * This assignment is allowed only if the repository is open.
 * When applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the value assigned will be written to the exchange structure
 * created by the method being applied.
 * @param value string describing the person who authorizes sending of the exchange structure.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getAuthorization
 * @see #openRepository
 * @see "ISO 10303-21::8.2.2 file_name."
 */
	public void setAuthorization(String value) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		checkServerRWAccess();
		file_name.authorization = value;
		modified = true;
	}


/**
 * Returns a <code>String</code> identifying the default language
 * for string values in those models of this repository, for which their own
 * default language is not specified. More precisely, for a model, the default language
 * can be assigned individually. <code>String</code> returned by this
 * method serves as a default language for those models for which
 * an individual assignment was not made.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * this string is set to null, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} this
 * string can take value extracted from the header of the exchange structure
 * (see ISO 10303-21::8.2.4 section_language) provided
 * an appropriate value is given there (otherwise it is set to null).
 * The method is available only if the repository is open.
 * @return <code>String</code> identifying the default language for string
 * values in models of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #setDefaultLanguage
 * @see SdaiModel#getDefaultLanguage
 * @see SdaiModel#setDefaultLanguage
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public String getDefaultLanguage() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		return language;
	}



/**
 * Assigns a <code>String</code> to the attribute identifying
 * the default language for string values in those models of this repository,
 * for which their own default language is not specified.
 * This assignment is allowed only if the repository is open.
 * If at least one model in this repository does not have its own
 * default language and the default language of the repository is set
 * with some value, then when applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * this value will be written to the exchange structure
 * created by the method being applied.
 * @param value <code>String</code> identifying the default language for string
 * values in models of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see #getDefaultLanguage
 * @see SdaiModel#getDefaultLanguage
 * @see SdaiModel#setDefaultLanguage
 * @see #openRepository
 * @see "ISO 10303-21::8.2.4 section_language."
 */
	public void setDefaultLanguage(String value) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		language = value;  // use value=null for unset!
		modified = true;
	}


/**
 * Returns an aggregate <code>A_string</code> containing information
 * describing the contexts within which the instances of the models in
 * this repository are applicable. Both the repository and each model
 * in it have their own aggregates <code>A_string</code> for writing context identifiers.
 * But for those models, for which their own aggregate is empty,
 * contexts are described by the aggregate returned by this method,
 * that is, by the repository aggregate.
 * <p> After invocation of <code>SdaiSession</code> method
 * {@link SdaiSession#createRepository createRepository}
 * this aggregate is empty, whereas after invocation of
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding} this
 * aggregate will contain information extracted from the header of the exchange structure
 * (see ISO 10303-21::8.2.5 section_context) provided
 * an appropriate information is given there.
 * That is, this aggregate always exists, but may be empty.
 * During a read-write transaction the aggregate can be
 * modified by adding/modifying/removing its members.
 * If for at least one model in this repository its own aggregate
 * describing contexts is empty and the repository aggregate contains at least
 * one string, then when applying {@link #exportClearTextEncoding exportClearTextEncoding},
 * the content of this aggregate will be written to the exchange structure
 * created by the method being applied.
 * The method is available only if the repository is open.
 * @return aggregate containing information describing the contexts.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @see SdaiModel#getContextIdentifiers
 * @see "ISO 10303-21::8.2.5 section_context."
 */
	public A_string getContextIdentifiers() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		return context_identifiers;
	}


/**
 * Selects to write entity names either in usual form or in short form.
 * For the latter alternative the parameter value shall be set to
 * <code>true</code>. This information is used
 * by {@link #exportClearTextEncoding exportClearTextEncoding}
 * and {@link CEntity#toString CEntity.toString} methods.
 * Invocation of the method is allowed only if the repository is open.
 * <p> This method is an extension of JSDAI, which is not a part of the standard.
 * @param enable shows whether entity names must be short (if <code>true</code>)
 * or usual (if <code>false</code>).
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 */
	public void shortNameSupport(boolean enable) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		suppress_short_names = !enable;
	}


	/**
	 * Returns this repository effective permission which determines
	 * the repository's access rights.
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
	 * has no read access to this repository. Otherwise this method returns
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
	 * has no write access to this repository. Otherwise this method returns
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
	 * Returns <code>false</code> if the owning session is not permitted to
	 * create a new schema instance in this repository for specified native
	 * schema. Otherwise, if schema instance could be created, it returns
	 * <code>true</code>.
	 * @param nativeSchema the native EXPRESS schema
	 * @return <code>false</code> if the owning session is permitted to create
	 *         a new schema instance; <code>true</code> otherwise.
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkCreateSchemaInstance(Class)
	 * @since 4.0.1
	 */
	public boolean checkCreateSchemaInstance(ESchema_definition nativeSchema) throws SdaiException {
		if (nativeSchema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return doCheckCreateSchemaInstance(nativeSchema);
	}

	/**
	 * Returns <code>false</code> if the owning session is not permitted to
	 * create a new schema instance in this repository for specified native
	 * schema. Otherwise, if schema instance could be created, it returns
	 * <code>true</code>.
	 * @param nativeSchema the native EXPRESS schema class
	 * @return <code>false</code> if the owning session is permitted to create
	 *         a new schema instance; <code>true</code> otherwise.
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkCreateSchemaInstance(ESchema_definition)
	 * @since 4.0.1
	 */
	public boolean checkCreateSchemaInstance(Class nativeSchema) throws SdaiException {
		if (nativeSchema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		CSchema_definition nativeSchemaDef = session.getSchemaDefinition(nativeSchema);
		if (nativeSchemaDef == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		return doCheckCreateSchemaInstance(nativeSchemaDef);
	}

	/**
	 * Returns <code>false</code> if the owning session is not permitted to
	 * create a new model in this repository with specified underlying schema.
	 * Otherwise, if model could be created, it returns <code>true</code>.
	 * @param underlyingSchema the underlying EXPRESS schema
	 * @return <code>false</code> if the owning session is permitted to create
	 *         a new model; <code>true</code> otherwise.
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkCreateSdaiModel(Class)
	 * @since 4.0.1
	 */
	public boolean checkCreateSdaiModel(ESchema_definition underlyingSchema) throws SdaiException {
		if (underlyingSchema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return doCheckCreateSdaiModel(underlyingSchema);
	}

	/**
	 * Returns <code>false</code> if the owning session is not permitted to
	 * create a new model in this repository with specified underlying schema.
	 * Otherwise, if model could be created, it returns <code>true</code>.
	 * @param underlyingSchema the underlying EXPRESS schema class
	 * @return <code>false</code> if the owning session is permitted to create
	 *         a new model; <code>true</code> otherwise.
	 * @throws SdaiException if an error occurs performing underlying JSDAI operations
	 * @see #checkCreateSdaiModel(ESchema_definition)
	 * @since 4.0.1
	 */
	public boolean checkCreateSdaiModel(Class underlyingSchema) throws SdaiException {
		if (underlyingSchema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		CSchema_definition underlyingSchemaDef = session.getSchemaDefinition(underlyingSchema);
		if (underlyingSchemaDef == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		return doCheckCreateSdaiModel(underlyingSchemaDef);
	}

	protected abstract boolean doCheckCreateSchemaInstance(ESchema_definition nativeSchema) throws SdaiException;

	protected abstract boolean doCheckCreateSdaiModel(ESchema_definition underlyingSchema) throws SdaiException;

	/**
	 * Returns the name of the user that has placed exclusive lock
	 * on remote repository. This method always returns
	 * <code>null</code> for local repository.
	 * @return the name of the user that has the exclusive lock or
	 *         null if there is no exclusive lock on the repository
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see SdaiSession#lock
	 * @since 4.1.0
	 */
	public String getLockingUser() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return doGetLockingUser();
	}

	/**
	 * This method should be always overridden by remote repository
	 */
	protected String doGetLockingUser() throws SdaiException {
		return null;
	}

/** Makes the <code>SdaiModels</code> and <code>SchemaInstances</code>
 * contained in this repository available for subsequent access.
 * If the repository is deleted or if the <code>SdaiSession</code> is closed,
 * SdaiException RP_NEXS will be thrown.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NAVL, repository not available.
 * @throws SdaiException RP_OPN, repository open.
 * @throws SdaiException LC_NVLD, location invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.4.5 Open repository"
 */
	public abstract void openRepository() throws SdaiException;


	protected boolean openRepositoryRemote() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}

		session.startProcessTransactionReadOnly();

		if (feature_level == 0) {
			int count = 0;
			for (int i = 0; i < session.active_servers.myLength; i++) {
				if (session.active_servers.myData[i] != SdaiSession.systemRepository) {
					count++;
				}
			}
			if (count >= 1) {
				String base = SdaiSession.line_separator + AdditionalMessages.FL_ORZE;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (virtual) {
			throw new SdaiException(SdaiException.RP_NAVL, this);
		}
		if (active) {
			throw new SdaiException(SdaiException.RP_OPN, this);
		}
		if (committed || !created_or_imported) {
			return true;
		}
		return false;
	}


	protected abstract boolean updateRemoteModel(SdaiModel model, String name, SdaiModelRemote modRem) throws SdaiException;


	protected void openRepositoryRemoteContinued() throws SdaiException {
		active = true;
		modified = false;
		session.active_servers.addUnorderedRO(this);
		int i;
		SdaiModel model;
		if (was_opened) {
			int k = 0;
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				boolean upd = updateRemoteModel(model, model.name, model.getModRemote());
				if (upd) {
					model.loadInstanceIdentifiersRemoteModel();
					model.committed = true;
					if (k < i) {
						models.myData[k] = models.myData[i];
					}
					k++;
				}
			}
			if (k < models.myLength) {
				for (i = k; i < models.myLength; i++) {
					models.myData[i] = null;
				}
				models.myLength = k;
			}
		} else {
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				model.loadInstanceIdentifiersRemoteModel();
				model.committed = true;
			}
		}
		for (i = 0; i < schemas.myLength; i++) {
			SchemaInstance sch = (SchemaInstance)schemas.myData[i];
			sch.committed = true;
		}
		was_opened = true;

		session.endProcessTransactionReadOnly();
	}

	protected void reopenRepository() throws SdaiException { }

/**
 * Creates a new <code>SdaiModel</code> in this repository.
 * This model is automatically added to the aggregate of type <code>ASdaiModel</code>,
 * containing all models of the repository. This aggregate can
 * be obtained by applying {@link #getModels getModels} method.
 * The name of the new model must be unique within this repository.
 * If the name is not supplied or is an empty string, then SdaiException
 * VA_NSET or, respectively, VA_NVLD is thrown.
 * The second parameter specifies some EXPRESS schema. The
 * new model can contain instances only those entities which are
 * defined or used in this schema.
 * If schema is not supplied, then SdaiException SD_NDEF is thrown.
 * @param model_name name of the model.
 * @param schema underlying EXPRESS schema.
 * @return the new created <code>SdaiModel</code>.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_DUP, SDAI-model duplicate.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.5.1 Create SDAI-model"
 */
	public abstract SdaiModel createSdaiModel(String model_name, ESchema_definition schema)
		throws  SdaiException;


	protected boolean createSdaiModelCommonChecking(String model_name, ESchema_definition schema,
													boolean remote, int transactionMode)
			throws  SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		SdaiTransaction trans = session.active_transaction;
		if (trans == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (trans.mode < transactionMode) {
			throw new SdaiException(SdaiException.TR_NRW, trans);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (model_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (model_name.length() == 0) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		if (schema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}

		internal_usage = true;
		SdaiModel model = findSdaiModel(model_name);
		internal_usage = false;
		if (model != null) {
			throw new SdaiException(SdaiException.MO_DUP, model);
		}
		if (model_created_during_simulation || !remote) {
			return model_created_during_simulation;
		}
		for (int i = 0; i < trans.stack_length; i++) {
			if (trans.stack_del_mods_rep[i] == this && model_name.equals(trans.stack_del_mods[i].name)) {
				return true;
			}
		}
		return false;
	}


	protected SdaiModel createSdaiModelCommonCreating(String model_name, ESchema_definition schema, boolean remote)
			throws  SdaiException {
		SdaiModel model = createModel(model_name, (CSchema_definition)schema);
		model_count++;
		model.identifier = model_count;
		model.created = true;
		models.addUnorderedRO(model);
		insertModel();
		model.modified_outside_contents = true;
		if (remote && !model_created_during_simulation) {
			unresolved_mod_count++;
		}

/*		SdaiModel dict = ((CSchema_definition)schema).owning_model;
		if (dict.mode == SdaiModel.NO_ACCESS) {
			dict.startReadOnlyAccess();
		}
		SchemaData sch_data = dict.schemaData;
System.out.println("  SdaiRepository   dictionary: " + dict.name);
		model.startReadOnlyAccess();
		EEntity_definition edeff;
		for (int i = 0; i < sch_data.noOfEntityDataTypes; i++) {
			edeff = (EEntity_definition)sch_data.entities[i];
CEntity eee = (CEntity)edeff;
System.out.println("  SdaiRepository  i = " + i + "   ====== entity: " + edeff.getName(null) +
" ident: #" + eee.instance_identifier);
		}

		for (int i = 0; i < sch_data.noOfEntityDataTypes; i++) {
			edeff = (EEntity_definition)sch_data.entities[i];
System.out.println("  SdaiRepository  ********************************* entity: " + edeff.getName(null));
			int cc = model.getInstanceCount(edeff);
System.out.println("  SdaiRepository   its count: " + cc);
		}
		model.endReadOnlyAccess();*/

		return model;
	}


	private boolean restoreDeletedModelRemote(SdaiModel model, String name, SdaiModel dict) throws SdaiException {
		if (!model.name.equals(name)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		internal_usage = true;
		SdaiModel mod = findSdaiModel(name);
		internal_usage = false;
		if (mod != null) {
			return false;
		}
		model.repository = this;
		model.dictionary = dict;
		model.underlying_schema = dict.described_schema;
		model.initializeSdaiModel();
		models.addUnorderedRO(model);
		insertModel();
		return true;
	}


/**
 * Creates a new <code>SdaiModel</code> in this repository.
 * It is a variation of {@link #createSdaiModel createSdaiModel(String model_name,
 * ESchema_definition schema)} method.
 * The EXPRESS schema is specified by the second parameter whose type is <code>Class</code>.
 * The value submitted to this parameter during invocation of the method shall be
 * a special java class with the name constructed from the schema name. This class
 * is contained in the package corresponding to the schema of interest.
 * For example, if the schema is "geometry_schema", then the package name is
 * "jsdai.SGeometry_schema" and the value for the second parameter shall
 * be "jsdai.SGeometry_schema.SGeometry_schema.class".
 * @param model_name name of the model.
 * @param schema underlying EXPRESS schema.
 * @return the new created <code>SdaiModel</code>.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MO_DUP, SDAI-model duplicate.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.5.1 Create SDAI-model"
 */
	public abstract SdaiModel createSdaiModel(String model_name, Class schema)
		throws  SdaiException;


	protected CSchema_definition getSchemaDefinitionForClass(Class schema) throws SdaiException {
		if (schema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}
		return session.getSchemaDefinition(schema);
	}


/**
 * Given a name, finds the <code>SdaiModel</code> with this name in this repository.
 * If such a model does not exist, then <code>null</code> is returned.
 * @param name name of the model.
 * @return <code>SdaiModel</code> with the specified name.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public abstract SdaiModel findSdaiModel(String name) throws SdaiException;


	protected SdaiModel findSdaiModelCommon(String name) throws SdaiException {
// Internally, the method is invoked in SdaiModel class, methods renameSdaiModel and
// extractModel (the latter is used when references to other models
// during loading of binary files are found).
		if (!active && !extracting_model && !internal_usage) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		int res = findModel(0, models.myLength - 1, name);
		if (res >= 0) {
			return (SdaiModel)models.myData[res];
		}
		return null;
	}


	protected int getModelTreatmentFlag(SdaiModel model) {
		if (model != null && !(model.underlying_schema == null && model.dictionary == null)) {
			return 1;
		}
		if (internal_usage) {
			return 0;
		}
		if (extracting_model) {
			return -2;
		}
		return -1;
	}


	protected SdaiModel createOrPromoteModelRemote(SdaiModel model, SdaiModelHeader modelHeader) throws SdaiException {
		String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
			dictionary.startReadOnlyAccess();
		}
		if (model != null) {
			model.promoteVirtualToOrdinary(dictionary);
		} else {
			model_created_during_simulation = true;
			try {
				createSdaiModelCommonChecking(modelHeader.name, dictionary.described_schema,
											  true, SdaiTransaction.READ_ONLY);
				model = createSdaiModelCommonCreating(modelHeader.name, dictionary.described_schema, true);
// 				model = createSdaiModelCommon(modelHeader.name, dictionary.described_schema, true);
			} finally {
				model_created_during_simulation = false;
			}
		}
		model.created = false;
		model.modified_outside_contents = false;
		model.fromSdaiModelHeader(modelHeader);
		model.committed = true;
		return model;
	}


	protected void setModRemote(SdaiModel model, SdaiModelRemote mRemote) throws SdaiException {
		model.setModRemote(mRemote);
	}


/**
 * Creates a new <code>SchemaInstance</code> in this repository.
 * This schema instance is automatically added to the aggregate of type <code>ASchemaInstance</code>,
 * containing all schema instances of the repository. This aggregate can
 * be obtained by applying {@link #getSchemas getSchemas} method.
 * The name of the new schema instance must be unique within this repository.
 * If name is not supplied, then SdaiException VA_NSET is thrown.
 * The second parameter specifies some EXPRESS schema this schema instance is based on.
 * If schema is not supplied, then SdaiException SD_NDEF is thrown.
 * @param name name of the new <code>SchemaInstance</code>.
 * @param schema the native schema for the <code>SchemaInstance</code> created.
 * @return the new created <code>SchemaInstance</code>.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SI_DUP, schema instance duplicate.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.5.2 Create schema instance"
 */
	public abstract SchemaInstance createSchemaInstance(String name, ESchema_definition schema)
		throws  SdaiException;

	protected boolean createSchemaInstanceCommon(String name, ESchema_definition schema,
												 boolean remote, int transactionMode)
			throws  SdaiException {
		if (this != SdaiSession.systemRepository && feature_level == 0 && schemas.myLength >= 1) {
			String base = SdaiSession.line_separator + AdditionalMessages.FL_OSZE;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		SdaiTransaction trans = session.active_transaction;
		if (trans == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (trans.mode < transactionMode) {
			throw new SdaiException(SdaiException.TR_NRW, trans);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (schema == null) {
			throw new SdaiException(SdaiException.SD_NDEF);
		}

		internal_usage = true;
		SchemaInstance instance = findSchemaInstance(name);
		internal_usage = false;
		if (instance != null) {
			throw new SdaiException(SdaiException.SI_DUP, instance);
		}
		if (sch_inst_created_during_simulation || !remote) {
			return sch_inst_created_during_simulation;
		}
		for (int i = 0; i < trans.stack_length_sch_insts; i++) {
			if (trans.stack_del_sch_insts_rep[i] == this && name.equals(trans.stack_del_sch_insts[i].name)) {
				return true;
			}
		}
		return false;
	}

	protected void postCreateSchemaInstance(SchemaInstance instance, boolean remote) throws SdaiException {
		schema_count++;
		instance.created = true;
		schemas.addUnorderedRO(instance);
		insertSchemaInstance();
		instance.modified = true;
		if (remote && !sch_inst_created_during_simulation) {
			unresolved_sch_count++;
		}
	}


/**
 * Creates a new <code>SchemaInstance</code> in this repository.
 * It is a variation of {@link #createSchemaInstance createSchemaInstance(String name,
 * ESchema_definition schema)} method.
 * The EXPRESS schema is specified by the second parameter whose type is <code>Class</code>.
 * The value submitted to this parameter during invocation of the method shall be
 * a special java class with the name constructed from the schema name. This class
 * is contained in the package corresponding to the schema of interest.
 * For example, if the schema name is "geometry_schema", then the package name is
 * "jsdai.SGeometry_schema" and the value for the second parameter shall
 * be "jsdai.SGeometry_schema.SGeometry_schema.class".
 * @param name name of the new <code>SchemaInstance</code>.
 * @param schema the native schema for the <code>SchemaInstance</code> created.
 * @return the new created <code>SchemaInstance</code>.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SI_DUP, schema instance duplicate.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException RP_RO, read-only access to repository.
 * @throws SdaiException RP_LOCK, repository locked by another user.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.5.2 Create schema instance"
 */
	public abstract SchemaInstance createSchemaInstance(String name, Class schema)
		throws  SdaiException;


/**
 * Given a name, finds the <code>SchemaInstance</code> with this name in this repository.
 * If such a schema instance does not exist, then <code>null</code> is returned.
 * @param name name of the schema instance.
 * @return <code>SchemaInstance</code> with the specified name.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public abstract SchemaInstance findSchemaInstance(String name) throws SdaiException;


	protected SchemaInstance findSchemaInstanceCommon(String name) throws SdaiException {
// Internally, the method is invoked in SchemaInstance class, method 'rename',
// SdaiRepository class, methods 'createSchemaInstance' and 'loadContentsStream', and
// in the constructor of SdaiSession.
		int res = findSchemaInstance(0, schemas.myLength - 1, name);
		if (res >= 0) {
			return (SchemaInstance)schemas.myData[res];
		}
		return null;
	}


	protected boolean getInternalUsageFlag() {
		if (internal_usage) {
			return true;
		}
		return false;
	}


	protected SchemaInstance createSchemaInstanceRemote(SchemaInstanceHeader schInstHeader) throws SdaiException {
		String dict_name = schInstHeader.nativeSchema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
			dictionary.startReadOnlyAccess();
		}
		sch_inst_created_during_simulation = true;
		SchemaInstance sch_inst;
		try {
			sch_inst = createSchemaInstance(schInstHeader.name, dictionary.described_schema);
		} finally {
			sch_inst_created_during_simulation = false;
		}
		sch_inst.created = false;
		sch_inst.modified = false;
		sch_inst.fromSchemaInstanceHeader(schInstHeader);
		return sch_inst;
	}


	protected void setSchInstRemote(SchemaInstance sch_inst, SchemaInstanceRemote schInstRemote) {
		sch_inst.setSchInstRemote(schInstRemote);
	}


/**
 * Closes this repository.
 * In the case when a read-write transaction is active, this operation
 * is allowed only if no model and no schema instance was created, deleted
 * or modified since the most recent commit or abort operation was performed.
 * If this condition is not satisfied, then SdaiException TR_RW is thrown.
 * After closing a repository, <code>SdaiModels</code> and
 * <code>SchemaInstances</code> contained in it become inaccessible.
 * In turn, entity instances of any <code>SdaiModel</code> in this
 * repository also become inaccessible. For most methods in class
 * <code>EEntity</code>, if applied to such instances,
 * SdaiException EI_NEXS will be thrown.
 * The largest number appearing in the name of an instance within
 * the repository is stored by {@link SdaiTransaction#commit commit}
 * in 'contents' binary file of the repository and will be
 * extracted and used when this repository will be opened again.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException RP_NAVL, repository not available.
 * @throws SdaiException TR_RW, transaction read-write.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiTransaction#commit
 * @see SdaiTransaction#abort
 * @see "ISO 10303-22::10.5.3 Close repository"
 */
	public void closeRepository() throws  SdaiException {
//		synchronized (syncObject) {
			if (session == null) {
				throw new SdaiException(SdaiException.RP_NEXS);
			}
			if (!active) {
				throw new SdaiException(SdaiException.RP_NOPN, this);
			}
			session.startProcessTransactionReadWrite();

			closeRepositoryInternal(false);

			session.endProcessTransactionReadWrite();
			StaticFields staticFields = StaticFields.get();
			if (staticFields.entity_values != null) {
				staticFields.entity_values.unset_ComplexEntityValue();
			}
			if (staticFields.entity_values2 != null) {
				staticFields.entity_values2.unset_ComplexEntityValue();
			}
//		} // syncObject
	}

	protected abstract void closeRepositoryInternal(boolean delete_repo) throws SdaiException;

	protected final void closeRepositoryCommon(boolean delete_repo) throws SdaiException {
//long start, end, cumulat = 0;
		int i;
		SdaiModel model;
		SchemaInstance sch;
		if (session.active_transaction != null &&
				session.active_transaction.mode == SdaiTransaction.READ_WRITE && !delete_repo) {
			if (model_deleted || schema_instance_deleted) {
				throw new SdaiException(SdaiException.TR_RW, session.active_transaction);
			}
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				if (((model.modified || model.modified_outside_contents) && (model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) ||
						(model.created && !model.committed)) {
					throw new SdaiException(SdaiException.TR_RW, session.active_transaction);
				}
			}
			for (i = 0; i < schemas.myLength; i++) {
				sch = (SchemaInstance)schemas.myData[i];
				if (sch.modified || (sch.created && !sch.committed)) {
					throw new SdaiException(SdaiException.TR_RW, session.active_transaction);
				}
			}
		}
		deleteInversesAll(delete_repo);
		for (i = 0; i < models.myLength; i++) {
			model = (SdaiModel)models.myData[i];
			if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
				model.endingAccess();
			}
			model.exists = true;
			if (session.session_closing) {
				model.deleteSdaiModelWork(false, false, true, false);
			} else {
				boolean saved_modified = model.modified;
//System.out.println("SdaiRepository !!!!! model: " + model.name);
//start = System.currentTimeMillis();
				model.deleteSdaiModelWork(true, true, true, false);
//end = System.currentTimeMillis();
//cumulat += (end-start);
//System.out.println("Cumulative time +++: "+cumulat/1000.0 +" sec");
				model.modified = saved_modified;
			}
			model.setModRemote(null);
		}
//System.out.println("Cumulative time of deleteSdaiModelWork: "+cumulat/1000.0 +" sec");
		for (i = 0; i < schemas.myLength; i++) {
			sch = (SchemaInstance)schemas.myData[i];
			sch.setSchInstRemote(null);
			sch.repository = null;
			if (sch.associated_models != null) {
				for (int j = 0; j < sch.associated_models.myLength; j++) {
					model = (SdaiModel)sch.associated_models.myData[j];
					if (model.repository != this) {
						model.associated_with.removeUnorderedRO(sch);
					}
				}
				sch.associated_models = null;
			}
			sch.native_schema = null;
		}
		schemas.myLength = 0;
		session.active_servers.removeUnorderedRO(this);
		if(entityExternalData != null) {
			for (Iterator j = entityExternalData.values().iterator(); j.hasNext();) {
				ExternalData exData = (ExternalData) j.next();
				exData.owningEntity = null;
			}
			entityExternalData.clear();
		}
		active = false;
	}


	private final void deleteInversesAll(boolean delete_repo) throws SdaiException {
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
				model.endingAccess();
				model.deleteInverses(delete_repo);
			}
		}
	}


/**
 * Returns entity instance for the given persistent label.
 * Every entity instance within a repository can be uniquely identified by its persistent label.
 * In the role of the persistent label the entity instance name defined
 * in "ISO 10303-21::6.3.4" is used.
 * This method searches for the entity instance with the specified persistent label in all,
 * even without read-only or read-write access, models of this repository.
 * If an attempt is unsuccessful, SdaiException EI_NEXS is thrown.
 * @param label the persistent label of the entity instance to look for.
 * @return entity instance with the given persistent label.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see EEntity#getPersistentLabel
 * @see "ISO 10303-22::10.11.7 Get session identifier"
 */
	public abstract EEntity getSessionIdentifier(String label) throws SdaiException;


	protected long getSessionIdentifierInit(String label) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (session.active_transaction.mode == SdaiTransaction.NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, session.active_transaction);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		if (label == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		long identifier;
		if (label.charAt(0) != '#') {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_LANV;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		try {
			identifier = Long.parseLong(label.substring(1));
		} catch (NumberFormatException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_LANV;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return identifier;
	}


/*	protected EEntity getSessionIdentifierRemote(long identifier) throws SdaiException {
		if (iter == null) {
			iter = models.createIterator();
		} else {
			models.attachIterator(iter);
		}
		while (iter.next()) {
			SdaiModel model = null;
			boolean virtual = false;
			if (iter.myIndex >= 0) {
				model = (SdaiModel)models.myData[iter.myIndex];
			} else {
				boolean found = false;
				for (int i = 0; i < models.myLength; i++) {
					model = (SdaiModel)models.myData[i];
					if (model.checkModel(iter.myElement)) {
						if (model.underlying_schema == null && model.dictionary == null) {
							virtual = true;
						} else {
							found = true;
						}
						break;
					}
				}
				if (!found) {
					SdaiModelHeader modelHeader = takeModelHeader(iter.myElement);
					String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
					SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
					if (dictionary.mode == SdaiModel.NO_ACCESS) {
						dictionary.startReadOnlyAccess();
					}
					if (virtual) {
						model.promoteVirtualToOrdinary(dictionary);
						if (model.inst_idents == null) {
							model.loadInstanceIdentifiersRemoteModel();
						}
					} else {
						model = createNewModel(modelHeader.name, dictionary.described_schema, iter.myElement);
					}
					model.created = false;
					model.modified_outside_contents = false;
					model.fromSdaiModelHeader(modelHeader);
					model.committed = true;
				}
			}
			int ind = -1;
			if (model.mode == SdaiModel.NO_ACCESS) {
				if (model.inst_idents != null) {
					ind = model.find_instance_id(identifier);
					if (ind < 0) {
						continue;
					}
					model.startReadOnlyAccess();
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			CEntity inst = null;
			for (int j = 0; j < model.lengths.length; j++) {
				if (model.sorted[j]) {
					ind = model.find_instance(0, model.lengths[j] - 1, j, identifier);
					if (ind >= 0) {
						inst = model.instances_sim[j][ind];
					}
				} else {
					ind = -1;
					for (int k = 0; k <  model.lengths[j]; k++) {
						inst = model.instances_sim[j][k];
						if (inst.instance_identifier == identifier) {
							ind = k;
							break;
						}
					}
				}
				if (ind >= 0) {
					return (EEntity)inst;
				}
			}
		}
		return null;
	}*/

    protected EEntity getSessionIdentifierCommon(long identifier) throws SdaiException {
//    	synchronized (syncObject) {
			int i;
			SdaiModel model;
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				if (model.getMode() == SdaiModel.NO_ACCESS) {
					continue;
				}
				CEntity inst = model.quick_find_instance(identifier);
				if (inst != null) {
					return (EEntity)inst;
				}
			}
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				if (model.getMode() != SdaiModel.NO_ACCESS) {
					continue;
				}
				if (model.inst_idents == null) {
					model.loadInstanceIdentifiers();
				}
				int ind = model.find_instance_id(identifier);
				if (ind < 0) {
					continue;
				}
				model.startReadOnlyAccess();
				CEntity inst = model.quick_find_instance(identifier);
				if (inst != null) {
					return (EEntity)inst;
				}
				model.endReadOnlyAccess();
			}
			return null;
//		} // syncObject
	}


	protected EEntity getSessionIdentifierRemote(long identifier, int typeIndex,
												 long modelId) throws SdaiException {
		int i;
		EEntity inst;
		SdaiModel model = null;
		if(modelId >= 0) {
			model = findSdaiModelById(modelId);
			if(model != null && model.repository == null) {
				throw new SdaiException(SdaiException.EI_NEXS,
										"Instance identifier " + identifier +
										" is in deleted model " + model);
			}
		}

		if(model == null) {
			boolean virtual = false;
			SdaiModelRemote mod_remote = findSdaiModelBySessionIdentifier(identifier);
			if (mod_remote == null) {
				throw new SdaiException(SdaiException.EI_NEXS,
										"Instance identifier " + identifier +
										" does not have owning model");
			}
			boolean found = false;
			long modRemoteId = mod_remote.getRemoteId();
			for (i = 0; i < models.myLength; i++) {
				model = (SdaiModel)models.myData[i];
				if (model.hasId(modRemoteId)) {
					if (model.underlying_schema == null && model.dictionary == null) {
						virtual = true;
					} else {
						found = true;
					}
					break;
				}
			}
			if (!found) {
				SdaiModelHeader modelHeader = takeModelHeader(mod_remote);
				String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
					dictionary.startReadOnlyAccess();
				}
				if (virtual) {
					model.promoteVirtualToOrdinary(dictionary);
					/*
					  if (model.inst_idents == null) {
					  model.loadInstanceIdentifiersRemoteModel();
					  }
                    */
				} else {
					model = createNewModel(modelHeader.name, dictionary.described_schema, mod_remote);
				}
				model.created = false;
				model.modified_outside_contents = false;
				model.fromSdaiModelHeader(modelHeader);
				model.committed = true;
			}
		}
		if((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
			model.startPartialReadOnlyAccess();
		}
		if(typeIndex >= 0) {
			model.provideInstancesForExactTypeIfNeeded(typeIndex);
			int ind = model.find_instance(0, model.lengths[typeIndex] - 1, typeIndex, identifier);
			inst = ind >= 0 ? model.instances_sim[typeIndex][ind] : null;
		} else {
			model.provideInstancesForType(identifier);
			inst = model.quick_find_instance(identifier);
		}
		if (inst == null) {
			throw new SdaiException(SdaiException.EI_NEXS,
									"Instance identifier " + identifier + " not found in model " + model);
		}
		return inst;
	}


	boolean checkInstanceIdentifier(long identifier, SdaiModel owner) throws SdaiException {
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			if (model == owner) {
				continue;
			}
			if (model.inst_idents == null) {
				model.loadInstanceIdentifiers();
			}
			if (model.find_instance_id(identifier) > 0) {
				return true;
			}
		}
		return false;
	}


	void closeIdentifiers() throws SdaiException {
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			model.inst_idents = null;
			model.all_inst_count = 0;
		}
	}


/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.4.14 SDAI query"
 */
	public int query(String where, EEntity entity, AEntity result)
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		return getModels();
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		return null;
	}

	/**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		SdaiRepositoryRemote repoRemote = getRepoRemote();
        if(repoRemote != null) {
            return new SdaiRepositoryRef(repoRemote.getRemoteId());
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
 * Deletes this repository.
 * Upon termination of this method the repository does not
 * exist both in the session (aggregate <code>knownServers</code>) and physically.
 * It is not possible to get back it using
 * {@link SdaiSession#linkRepository linkRepository} method.
 * If the repository at the moment of invocation of this method is open,
 * it is first closed and only then deleted.
 * Deleting of special repository "SystemRepository", containing dictionary
 * and mapping data, is forbidden.
 * If the repository is degenerated, then SdaiException FN_NAVL is thrown.
 * <p> This method is an extension of JSDAI, which is not a part of the standard.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NAVL, repository not available.
 * @throws SdaiException TR_RW, transaction read-write.
 * @throws SdaiException SY_ERR, underlying system error.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #closeRepository
 */
	public void deleteRepository() throws SdaiException {
//		synchronized (syncObject) {
			if (session == null) {
				throw new SdaiException(SdaiException.RP_NEXS);
			}
			if (schemas == null) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			SdaiSession session = this.session;
			session.startProcessTransactionReadWrite();

			deleteRepositoryInternal();

			session.endProcessTransactionReadWrite();
			StaticFields staticFields = StaticFields.get();
			if (staticFields.entity_values != null) {
				staticFields.entity_values.unset_ComplexEntityValue();
			}
			if (staticFields.entity_values2 != null) {
				staticFields.entity_values2.unset_ComplexEntityValue();
			}
//		} // syncObject
	}

	protected abstract void deleteRepositoryInternal() throws SdaiException;

	protected void deleteRepositoryCommon(boolean remote) throws SdaiException {
		if (active) {
			closeRepositoryInternal(true);
		}
		int count = models.myLength;
		for (int i = 0; i < count; i++) {
			SdaiModel model = (SdaiModel)models.myData[0];
			models.removeUnorderedRO(model);
			model.repository = null;
			if (session.session_closing) {
				model.removeInConnectors();
			} else {
				model.resolveInConnectors(true);
			}
		}
		session.known_servers.removeUnorderedRO(this);
		models = null;
		schemas = null;
		System.gc();
		if (remote) {
			session = null;
		}
	}


/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this repository.
 * At the moment of invocation of the method the repository must be opened.
 * If parameter <code>location</code> gets some <code>String</code> value,
 * then this value is used as a name for the resulting file.
 * If, however, <code>location</code> is null, then the name is chosen as follows.
 * If the repository was created during this session by applying
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding},
 * then the name of the file is the same as that of the input file for
 * importClearTextEncoding method. In the remaining cases, that is,
 * when the repository was either created by
 * {@link SdaiSession#createRepository createRepository} or linked by
 * {@link SdaiSession#linkRepository linkRepository}, the name of the
 * resulting file coincides with the name of the repository.
 * The contents of each model of the repository is written down to
 * the file as a separate data section. The header of the file includes
 * time-stamp which specifies the date and time when this repository was
 * created or most recently modified.
 * The value of the attribute <code>implementation_level</code> of the
 * entity <code>file_description</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.1 file_description) can take one of the following
 * values:
 * <ul><li>"2;1" if the repository contains at most one model;
 * <li>"3;1" otherwise; in this case for each schema instance in the
 * repository an instance of the entity <code>file_population</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.4 file_population)
 * is created;
 * </ul>
 * During creation of an instance of <code>file_population</code> for a schema instance
 * only those models in the set <code>associated_models</code> which belong
 * to this repository are listed. Models from the other repositories simply
 * are ignored. However, if all models of this repository belong to
 * <code>associated_models</code> of the schema instance considered, then
 * the value standing for <code>associated_models</code> in the
 * corresponding instance of <code>file_population</code> is missing,
 * that is, dollar sign is used (see ISO 10303-21::8.2.4 file_population).
 * <p> The repository may contain instances which reference instances in other
 * repositories. In this case the strategy is as follows:
 * <ul><li>if such a reference is a non-aggregate value of an attribute or belongs
 * to an aggregate of Express type ARRAY, then this reference in an exchange
 * structure being formed is replaced by an unset value (dollar sign "$");
 * <li>if such a reference belongs to an aggregate of Express type either LIST or
 * SET or BAG, then when writing data to an exchange structure this reference
 * is simply dropped;
 * </ul>
 * <p> After execution of this method the repository itself remains unchanged.
 * In particular, all references (if any) from the current repository to other
 * repositories are retained.
 * The method is disabled for special repository "SystemRepository",
 * containing dictionary and mapping data.
 * In this case SdaiException FN_NAVL is thrown.
 * @param location the name of the file created by the method.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #exportClearTextEncoding(String location, String file_name)
 * @see SchemaInstance#exportClearTextEncoding
 * see "ISO 10303-21"
 */
	public void exportClearTextEncoding(String location) throws SdaiException {
		exportClearTextEncoding(location, null);
	}


/**
 * Creates a STEP file in a form of the exchange structure
 * containing the data of this repository.
 * At the moment of invocation of the method the repository must be opened.
 * If parameter <code>location</code> gets some <code>String</code> value,
 * then this value is used as a name for the resulting file.
 * If, however, <code>location</code> is null, then the name is chosen as follows.
 * If the repository was created during this session by applying
 * {@link SdaiSession#importClearTextEncoding importClearTextEncoding},
 * then the name of the file is the same as that of the input file for
 * importClearTextEncoding method. In the remaining cases, that is,
 * when the repository was either created by
 * {@link SdaiSession#createRepository createRepository} or linked by
 * {@link SdaiSession#linkRepository linkRepository}, the name of the
 * resulting file coincides with the name of the repository.
 * The second parameter of the method can be used to set the value of the attribute
 * <code>name</code> of the header entity <code>file_name</code>
 * (see ISO 10303-21::8.2.2 file_name). If this parameter is <code>null</code>,
 * then the value of <code>name</code> depends on the origin of the
 * repository. If it has been imported from an exchange structure, then
 * the existing value of <code>name</code> is taken. If, however, the repository
 * was created, then its name as the value of the attribute
 * <code>name</code> is used.
 *
 * <p> The contents of each model of the repository is written down to
 * the file as a separate data section. The header of the file includes
 * time-stamp which specifies the date and time when this repository was
 * created or most recently modified.
 * The value of the attribute <code>implementation_level</code> of the
 * entity <code>file_description</code> in the header of the exchange structure
 * (see ISO 10303-21::8.2.1 file_description) can take one of the following
 * values:
 * <ul><li>"2;1" if the repository contains at most one model;
 * <li>"3;1" otherwise; in this case for each schema instance in the
 * repository an instance of the entity <code>file_population</code> in the
 * header of the exchange structure (see ISO 10303-21::8.2.4 file_population)
 * is created;
 * </ul>
 * During creation of an instance of <code>file_population</code> for a schema instance
 * only those models in the set <code>associated_models</code> which belong
 * to this repository are listed. Models from the other repositories simply
 * are ignored. However, if all models of this repository belong to
 * <code>associated_models</code> of the schema instance considered, then
 * the value standing for <code>associated_models</code> in the
 * corresponding instance of <code>file_population</code> is missing,
 * that is, dollar sign is used (see ISO 10303-21::8.2.4 file_population).
 * <p> The repository may contain instances which reference instances in other
 * repositories. In this case the strategy is as follows:
 * <ul><li>if such a reference is a non-aggregate value of an attribute or belongs
 * to an aggregate of Express type ARRAY, then this reference in an exchange
 * structure being formed is replaced by an unset value (dollar sign "$");
 * <li>if such a reference belongs to an aggregate of Express type either LIST or
 * SET or BAG, then when writing data to an exchange structure this reference
 * is simply dropped;
 * </ul>
 * <p> Applying this method, it is possible to write entity names either in usual form or in short form.
 * For example, for entity <code>cartesian_point</code> any of the following cases can be chosen:
 * <ul><li> #1016=CARTESIAN_POINT('cp5',(0.0,0.0,-0.25));
 * <li> #1016=CRTPNT('cp5',(0.0,0.0,-0.25));
 * </ul>
 * To change the current alternative, {@link #shortNameSupport shortNameSupport}
 * method should be used.
 * Selecting the short form can give the desired result only if the short names of entities
 * are defined in the data dictionary (provided by an Express compiler).
 * If, for an entity, the method fails to identify its short name, then the full entity
 * name is used instead of.
 * <p> After execution of this method the repository itself remains unchanged.
 * In particular, all references (if any) from the current repository to other
 * repositories are retained.
 * The method is disabled for special repository "SystemRepository",
 * containing dictionary and mapping data.
 * In this case SdaiException FN_NAVL is thrown.
 * @param location the name of the file created by the method.
 * @param file_name the name of the exchange structure to which this repository is exported.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #exportClearTextEncoding(String location)
 * @see SchemaInstance#exportClearTextEncoding
 * @see "ISO 10303-21"
 */
	public void exportClearTextEncoding(String location, String file_name) throws SdaiException {
if (SdaiSession.debug2) for (int j = 0; j < models.myLength; j++) {
SdaiModel model = (SdaiModel)models.myData[j];
System.out.println("   SdaiRepository   *****  model: " + model.name +
"   its schema_name: " + model.schema_name);
}
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
//		synchronized (syncObject) {
		if (this == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		} else {
			if (session.active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}
			if (session.active_transaction.mode == SdaiTransaction.NO_ACCESS) {
				throw new SdaiException(SdaiException.TR_NAVL, session.active_transaction);
			}
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		updateHeaderData();
		try {
			PhFileWriter writer = new PhFileWriter();
			File handle;
			File handle_back;
			String output_file_name;
			String file_name_back;
			if (location != null) {
				output_file_name = location;
				file_name_back = output_file_name + ".BAK";
				handle = new File(output_file_name);
				handle_back = new File(file_name_back);
			} else if (physical_file_name != null) {
				int point_position = physical_file_name.lastIndexOf((byte)'.');
				if (point_position > 0) {
					String prefix = physical_file_name.substring(0, point_position + 1);
					file_name_back = prefix + "BAK";
				} else {
					file_name_back = physical_file_name + ".BAK";
				}
				handle = new File(physical_file_name);
				handle_back = new File(file_name_back);
				output_file_name = physical_file_name;
			} else {
				handle = new File("ph_file");
				handle_back = new File("ph_file.BAK");
				output_file_name = "ph_file";
			}
			if (handle.exists()) {
				if (handle_back.exists()) {
					handle_back.delete();
				}
				if (!handle.renameTo(handle_back)) {
				}
			}
			writer.output_to_physical_file(output_file_name, this, file_name);
			if (handle_back.exists()) {
				handle_back.delete();
			}
		} catch (java.io.IOException ex){
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
//		} // syncObject
	}


	/**
	 * Creates a STEP file in a form of the exchange structure
	 * containing the data of this repository.
	 * At the moment of invocation of the method the repository must be opened.
	 * The exchange structure is written to the supplied stream.
	 * For more details about the exchange format see
	 * {@link #exportClearTextEncoding(String location, String file_name)}.
	 * Invoking this method is equivalent to making this call:
	 * <code>exportClearTextEncoding(location, null);</code>
	 *
	 * @param location an <code>OutputStream</code> value
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void exportClearTextEncoding(OutputStream location) throws SdaiException {
		exportClearTextEncoding(location, null);
	}


	/**
	 * Creates a STEP file in a form of the exchange structure
	 * containing the data of this repository.
	 * At the moment of invocation of the method the repository must be opened.
	 * The exchange structure is written to the supplied stream.
	 * For more details about the exchange format see
	 * {@link #exportClearTextEncoding(String location, String file_name)}
	 *
	 * @param location an <code>OutputStream</code> value
	 * @param file_name the name of the exchange structure to which this repository is exported.
	 *                  It sets the value of the attribute <code>name</code> of the header entity
	 *                  <code>file_name</code> (see ISO 10303-21::8.2.2 file_name).
	 *                  If this parameter is <code>null</code>, then the value of
	 *                  <code>name</code> depends on the origin of the repository. If it has been
	 *                  imported from an exchange structure, then the existing value of
	 *                  <code>name</code> is taken. If, however, the repository was created,
	 *                  then its name as the value of the attribute <code>name</code> is used.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void exportClearTextEncoding(OutputStream location, String file_name) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
//		synchronized (syncObject) {
		if (this == SdaiSession.systemRepository) {
			throw new SdaiException(SdaiException.FN_NAVL);
		} else {
			if (session.active_transaction == null) {
				throw new SdaiException(SdaiException.TR_NEXS);
			}
			if (session.active_transaction.mode == SdaiTransaction.NO_ACCESS) {
				throw new SdaiException(SdaiException.TR_NAVL, session.active_transaction);
			}
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
		updateHeaderData();
		try {
			PhFileWriter writer = new PhFileWriter();
			writer.output_to_physical_file(location, this, file_name);
		} catch (java.io.IOException ex){
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
//		} // syncObject
	}

	/**
	 * Writes this repository in XML representation to specified stream.
	 * Output format is controlled by <code>instanceReader</code> parameter.
	 *
	 * @param location an <code>OutputStream</code> to write XML to
	 * @param instanceReader an {@link jsdai.xml.InstanceReader} describing output format
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see jsdai.xml.InstanceReader jsdai.xml.InstanceReader
	 * @see jsdai.xml.LateBindingReader jsdai.xml.LateBindingReader
	 */
	public void exportXml(OutputStream location, InstanceReader instanceReader)
	throws SdaiException {
		try {
			instanceReader.serialize(location, new SdaiInputSource(this));
		} catch(SdaiException e) {
			throw e;
		} catch(Exception e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

	/**
	 * Reads repository from specified stream as XML representation.
	 *
	 * @param location an <code>InputStream</code> to read XML from.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see jsdai.xml.InstanceWriterSelector
	 */
	public void importXml(InputStream location) throws SdaiException {
		try {
			InstanceWriterSelector.parse(location, new InstanceWriterSelector(this));
		} catch(Exception e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

/**
 * Informs if this repository is open.
 * An exception will be thrown if the repository is deleted
 * or if the <code>SdaiSession</code> is closed.
 * @return <code>true</code> if this repository is open, and
 * <code>false</code> otherwise.
 * @throws SdaiException RP_NEXS, repository does not exist.
 */
	public boolean isActive() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		return active;
	}


/**
 * Informs if this repository is modified.
 * A repository is called modified if either at least one model or
 * schema instance has been created, deleted or modified or some data
 * outside repository contents were modified since the most recent
 * commit or abort operation was performed.
 * The method is available only if the repository is open.
 * @return <code>true</code> if this repository is modified, and
 * <code>false</code> otherwise.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @throws SdaiException RP_NOPN, repository is not open.
 */
	public boolean isModified() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (!active) {
			throw new SdaiException(SdaiException.RP_NOPN, this);
		}
//		synchronized (syncObject) {
		if (this == SdaiSession.systemRepository) {
			return false;
		}
		if (model_deleted || schema_instance_deleted || modified) {
			return true;
		}
		int i;
		SdaiModel model;
		SchemaInstance sch;
		for (i = 0; i < models.myLength; i++) {
			model = (SdaiModel)models.myData[i];
			if (model.modified || model.modified_outside_contents) {
//System.out.println("SdaiRepository  model: " + model.name +
//"   modified = " + model.modified +
//"   modified_outside_contents = " + model.modified_outside_contents);
				return true;
			}
		}
		for (i = 0; i < schemas.myLength; i++) {
			sch = (SchemaInstance)schemas.myData[i];
			if (sch.modified) {
				return true;
			}
		}
		return false;
//		} // syncObject
	}


/**
 * Returns a description of this repository as a <code>String</code>.
 * It includes constant string "SdaiRepository: " and repository name.
 * @return a description of the repository.
 */
	public String toString() {
		if (session == null) {
			return super.toString();
		}
		return "SdaiRepository: " + name;
	}


/**
	Writes the data contained in this repository but not in models within it to the
	binary file when this repository is a remote one.
*/
	void saveRepositoryRemote() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
//		if (!active) {
//			throw new SdaiException(SdaiException.RP_NOPN, this);
//		}
		try {
			ostream.writeByte('t');
		} catch (java.net.UnknownHostException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_UNH;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		ByteArrayOutputStream baostream = new ByteArrayOutputStream();
		DataOutputStream daostream = new DataOutputStream(baostream);
		saveRepositoryStream(daostream);
		try {
			ostream.writeInt(baostream.size());
			baostream.writeTo(os);
			daostream.close();
			baostream.close();
			String res = istream.readUTF();
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	Writes the data contained in this repository but not in models within it to the
	binary file when this repository is a remote one.
*/
	void saveRepositoryRemote(DataInput istr, DataOutput ostr) throws SdaiException {
		istream = istr;
		ostream = ostr;
		saveRepositoryRemote();
	}


/**
	Writes the data contained in this repository (but not in models within it) to the
	binary file "repository".
*/
	void saveRepositoryStream(DataOutput stream) throws SdaiException {
		int i, j;
		int index2name;
		String sch_name;
		String some_date;
		CSchema_definition def;
		A_string strings;

		updateHeaderData();
		StaticFields staticFields = StaticFields.get();
		try {
			stream.writeByte('R');
			stream.writeInt(Implementation.build);
			stream.writeShort(Implementation.major_version);
			stream.writeShort(Implementation.middle_version);
			stream.writeShort(Implementation.minor_version);
			stream.writeByte('B');
			stream.writeShort((short)description.myLength);
			for (i = 1; i <= description.myLength; i++) {
				stream.writeUTF((String)description.getByIndex(i));
			}
			stream.writeByte('B');
			//stream.writeUTF(removeRecurrenceNumber(name));
            stream.writeUTF(getRealName());
			stream.writeUTF(file_name.time_stamp);
			strings = file_name.author;
			stream.writeShort(strings.myLength);
			for (i = 1; i <= strings.myLength; i++) {
				stream.writeUTF((String)strings.getByIndex(i));
			}
			strings = file_name.organization;
			stream.writeShort(strings.myLength);
			for (i = 1; i <= strings.myLength; i++) {
				stream.writeUTF((String)strings.getByIndex(i));
			}
			stream.writeUTF(file_name.preprocessor_version);
			stream.writeUTF(file_name.originating_system);
			stream.writeUTF(file_name.authorization);
			stream.writeByte('B');
			strings = file_schema.schema_identifiers;
			stream.writeShort(strings.myLength);
			for (i = 1; i <= strings.myLength; i++) {
				stream.writeUTF((String)strings.getByIndex(i));
			}
			stream.writeByte('B');
			for (i = 0; i < languages_count; i++) {
				if (languages[i].section == null) {
					stream.writeByte('L');
					stream.writeUTF(languages[i].default_language);
					break;
				}
			}
			for (i = 0; i < contexts_count; i++) {
				if (contexts[i].section == null) {
					stream.writeByte('C');
					strings = contexts[i].context_identifiers;
					stream.writeShort(strings.myLength);
					for (j = 1; j <= strings.myLength; j++) {
						stream.writeUTF((String)strings.getByIndex(j));
					}
					break;
				}
			}
			stream.writeByte('I');
			stream.writeLong(largest_identifier);
			stream.writeByte('S');
			int schemas_count = takeSchemas(staticFields);
			stream.writeShort((short)schemas_count);
			for (i = 0; i < schemas_count; i++) {
				stream.writeUTF(staticFields.schema_names[i]);
			}

			stream.writeByte('S');
			stream.writeInt(model_count);
			stream.writeShort((short)models.myLength);
			SdaiModel mod;
			for (i = 0; i < models.myLength; i++) {
				mod = (SdaiModel)models.myData[i];
				stream.writeByte('B');
				stream.writeUTF(mod.name);
				stream.writeInt(mod.identifier);
				def = mod.underlying_schema;
				if (def != null) {
					sch_name = def.getName(null);
				} else {
					sch_name = mod.dictionary.name.substring(0, mod.dictionary.name.length() -
						SdaiSession.ENDING_FOR_DICT);
				}
				index2name = session.find_string(0, schemas_count - 1, sch_name, staticFields.schema_names);
				if (index2name < 0) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				stream.writeShort((short)index2name);
				stream.writeByte('D');
				if (mod.change_date > 0) {
					some_date = session.cal.longToTimeStamp(mod.change_date);
					stream.writeUTF(some_date);
				} else {
					stream.writeUTF("");
				}
				for (j = 0; j < languages_count; j++) {
					SECTION_LANGUAGE sec_lang = languages[j];
					if (sec_lang.section != null && sec_lang.section.equals(mod.name)) {
						stream.writeByte('L');
						stream.writeUTF(sec_lang.default_language);
						break;
					}
				}
				for (j = 0; j < contexts_count; j++) {
					SECTION_CONTEXT sec_con = contexts[j];
					if (sec_con.section != null && sec_con.section.equals(mod.name)) {
						stream.writeByte('C');
						strings = sec_con.context_identifiers;
						stream.writeShort(strings.myLength);
						for (int k = 1; k <= strings.myLength; k++) {
							stream.writeUTF((String)strings.getByIndex(k));
						}
						break;
					}
				}
			}

			stream.writeByte('S');
			stream.writeShort((short)schemas.myLength);
			for (i = 0; i < schemas.myLength; i++) {
				SchemaInstance sch = (SchemaInstance)schemas.myData[i];
				stream.writeByte('B');
				stream.writeUTF(sch.name);
				def = sch.native_schema;
				if (def != null) {
					sch_name = def.getName(null);
				} else {
					sch_name = sch.dictionary.name.substring(0, sch.dictionary.name.length() -
						SdaiSession.ENDING_FOR_DICT);
				}
				index2name = session.find_string(0, schemas_count - 1, sch_name, staticFields.schema_names);
				if (index2name < 0) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				stream.writeShort((short)index2name);
				stream.writeByte('D');
				if (sch.change_date > 0) {
					some_date = session.cal.longToTimeStamp(sch.change_date);
					stream.writeUTF(some_date);
				} else {
					stream.writeUTF("");
				}
				some_date = session.cal.longToTimeStamp(sch.validation_date);
				stream.writeUTF(some_date);
				stream.writeByte((byte)sch.validation_result);
				stream.writeShort((short)sch.validation_level);
				if (sch.description == null) {   // new code added from here
					stream.writeShort(0);
				} else {
					stream.writeShort((short)sch.description.myLength);
					for (j = 1; j <= sch.description.myLength; j++) {
						stream.writeUTF((String)sch.description.getByIndex(j));
					}
				}
				if (sch.author == null) {
					stream.writeShort(0);
				} else {
					stream.writeShort((short)sch.author.myLength);
					for (j = 1; j <= sch.author.myLength; j++) {
						stream.writeUTF((String)sch.author.getByIndex(j));
					}
				}
				if (sch.organization == null) {
					stream.writeShort(0);
				} else {
					stream.writeShort((short)sch.organization.myLength);
					for (j = 1; j <= sch.organization.myLength; j++) {
						stream.writeUTF((String)sch.organization.getByIndex(j));
					}
				}
				if (sch.preprocessor_version != null) {
					stream.writeByte('P');
					stream.writeUTF(sch.preprocessor_version);
				}
				if (sch.originating_system != null) {
					stream.writeByte('O');
					stream.writeUTF(sch.originating_system);
				}
				if (sch.authorization != null) {
					stream.writeByte('A');
					stream.writeUTF(sch.authorization);
				}
				if (sch.language != null) {
					stream.writeByte('L');
					stream.writeUTF(sch.language);
				}
				stream.writeByte('C');
				if (sch.context_identifiers == null) {
					stream.writeShort(0);
				} else {
					stream.writeShort((short)sch.context_identifiers.myLength);
					for (j = 1; j <= sch.context_identifiers.myLength; j++) {
						stream.writeUTF((String)sch.context_identifiers.getByIndex(j));
					}
				}     // new code added until here

				ASdaiModel assoc_mod = sch.associated_models;
				int ln;
				if (assoc_mod == null) {
					ln = 0;
				} else {
					ln = assoc_mod.myLength;
				}
				stream.writeShort(ln);
				int res;
				for (j = 0; j < ln; j++) {
					mod = (SdaiModel)assoc_mod.myData[j];
					if (mod.repository == this) {
						res = findModel(0, models.myLength - 1, mod.name);
						if (res < 0) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						stream.writeByte('L');
						stream.writeInt(res);
					} else {
						stream.writeByte('E');
						stream.writeUTF(mod.name);
						stream.writeUTF(mod.repository.name);
					}
				}
				if (sch.included_schemas != null && sch.included_schemas.myLength > 0) {
					stream.writeByte('I');
					ln = sch.included_schemas.myLength;
					stream.writeShort(ln);
					for (j = 0; j < ln; j++) {
						SchemaInstance incl_sch = (SchemaInstance)sch.included_schemas.myData[j];
						if (incl_sch.repository == this) {
							res = findSchemaInstance(0, schemas.myLength - 1, incl_sch.name);
							if (res < 0) {
								throw new SdaiException(SdaiException.SY_ERR);
							}
							stream.writeByte('L');
							stream.writeInt(res);
						} else {
							stream.writeByte('E');
							stream.writeUTF(incl_sch.name);
							stream.writeUTF(incl_sch.repository.name);
						}
					}
				}
			}

			stream.writeByte('E');
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}


/**
	Collects into one array, namely array 'schema_names', the names of schemas underlying for
	SdaiModels within this repository. The names in the array are sorted alphabetically.
	Method is used when saving the repository data to the binary file.
*/
	private int takeSchemas(StaticFields staticFields) throws SdaiException {
		int i, j;
		int schemas_count = 0;
		CSchema_definition def;
		if (staticFields.schema_names == null) {
			staticFields.schema_names = new String[SCHEMAS_ARRAY_SIZE];
		}
		if (staticFields.schema_names_sorting == null) {
			staticFields.schema_names_sorting = new String[SCHEMAS_ARRAY_SIZE];
		}
		String sch_name;
		boolean found;
		for (i = 0; i < models.myLength; i++) {
			SdaiModel mod = (SdaiModel)models.myData[i];
			def = mod.underlying_schema;
			if (def != null) {
				sch_name = def.getName(null);
			} else {
				sch_name = mod.dictionary.name.substring(0, mod.dictionary.name.length() -
					SdaiSession.ENDING_FOR_DICT);
			}
			found = false;
			for (j = 0; j < schemas_count; j++) {
				if (sch_name.equals(staticFields.schema_names[j])) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (schemas_count >= staticFields.schema_names.length) {
					staticFields.schema_names = session.ensureStringsCapacity(staticFields.schema_names);
					staticFields.schema_names_sorting = session.ensureStringsCapacity(staticFields.schema_names_sorting);
				}
				staticFields.schema_names[schemas_count] = sch_name;
				staticFields.schema_names_sorting[schemas_count] = sch_name;
				schemas_count++;
			}
		}
		for (i = 0; i < schemas.myLength; i++) {
			SchemaInstance sch = (SchemaInstance)schemas.myData[i];
			def = sch.native_schema;
			if (def != null) {
				sch_name = def.getName(null);
			} else {
				sch_name = sch.dictionary.name.substring(0, sch.dictionary.name.length() -
					SdaiSession.ENDING_FOR_DICT);
			}
			found = false;
			for (j = 0; j < schemas_count; j++) {
				if (sch_name.equals(staticFields.schema_names[j])) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (schemas_count >= staticFields.schema_names.length) {
					staticFields.schema_names = session.ensureStringsCapacity(staticFields.schema_names);
					staticFields.schema_names_sorting = session.ensureStringsCapacity(staticFields.schema_names_sorting);
				}
				staticFields.schema_names[schemas_count] = sch_name;
				staticFields.schema_names_sorting[schemas_count] = sch_name;
				schemas_count++;
			}
		}
		sortStrings(staticFields.schema_names_sorting, staticFields.schema_names, 0, schemas_count);
		return schemas_count;
	}


/**
	Puts in an alphabetical order the names of schemas underlying for SdaiModels
	within this repository. Method is used when saving the repository data to the
	binary file.
*/
	private void sortStrings(String [] s_strs, String [] strs, int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index && strs[j-1].compareTo(strs[j]) > 0; j--) {
					String str = strs[j-1];
					strs[j-1] = strs[j];
					strs[j] = str;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortStrings(strs, s_strs, start_index, middle);
		sortStrings(strs, s_strs, middle, end_index);
		if (s_strs[middle-1].compareTo(s_strs[middle]) < 0) {
			System.arraycopy(s_strs, start_index, strs, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					s_strs[m].compareTo(s_strs[n]) <= 0) {
				strs[i] = s_strs[m++];
			} else {
				strs[i] = s_strs[n++];
			}
		}
	}


/**
	Extracts the user name and password from the string representing
	the location of this repository.
*/
	String getUserAndPassword() throws SdaiException {
		String locationFull=(String)location;
		if(locationFull.indexOf("@")!=-1) {
			return locationFull.substring(locationFull.indexOf("/")+2, locationFull.indexOf("@"));
		} else {
			return "";
		}
	}


/**
	Sets several fields needed for server-client connection with values.
*/
	void takeServerProp() throws SdaiException {
	try {
			if(((String)location).indexOf("@") == -1) {
				location = SdaiSession.LOCATION_PREFIX + SdaiSession.DEF_USER_NAME + ":" + SdaiSession.DEF_PASSWORD + "@" + ((String)location).substring(SdaiSession.LOCATION_PREFIX_LENGTH);
			}
			user=getUserAndPassword();
			String address = ((String)location).substring(((String)location).indexOf("@")+1);
			if(address.indexOf("//")!=-1) {
				address = address.substring(address.indexOf(SdaiSession.LOCATION_PREFIX)+2);
			}
			if (SdaiSession.getPortNumber() != null) {
				port=Integer.parseInt(SdaiSession.getPortNumber());
			}
			if(address.indexOf(":") !=-1) {
				port = Integer.parseInt(address.substring(address.indexOf(":")+1));
				address = address.substring(0, address.indexOf(":"));
			}
			addr = address;
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.LC_NVLD);
		}

	}


/**
	Loads the data contained in the binary file 'header' to this repository
	when this repository is a remote one.
	The method is invoked while linking repositories created with an old build of JSDAI
	(220 or earlier).
*/
	void loadHeaderRemote() throws SdaiException {
		takeServerProp();
		Socket sock_temp = null;
		DataInput istr = null;
		DataOutput ostr = null;
		try {
			int result = 0;
			boolean repeat = true;
			while(repeat) { // In mutithreading mode if server waiting for listener connection can be refused
				sock_temp = new Socket(addr, port);
				InputStream	is_temp = sock_temp.getInputStream();
				OutputStream	os_temp = sock_temp.getOutputStream();
				istr = new DataInputStream((InputStream)is_temp);
				ostr = new DataOutputStream((OutputStream)os_temp);
				ostr.writeUTF(user);
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
				String base = SdaiSession.line_separator + AdditionalMessages.SS_PWD;
				throw new SdaiException(SdaiException.LC_NVLD, base);
			}
			ostr.writeUTF(name);
			ostr.writeByte('i');
			result = istr.readByte();
			if(result==0) {
				throw new SdaiException(SdaiException.RP_NEXS, this);
			}
			if(result==-1) {
				throw new SdaiException(SdaiException.RP_NAVL, this);
			}
		} catch (java.net.UnknownHostException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_UNH;
			throw new SdaiException(SdaiException.LC_NVLD, base);
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		loadHeaderStream(istr);
		try {
			ostr.writeByte('c');
			sock_temp.close();
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	Loads the data contained in the binary file 'header' to this repository.
	The method is invoked while linking repositories created with an old build of JSDAI
	(220 or earlier).
*/
	void loadHeaderStream(DataInput stream) throws SdaiException {
		int i;

		try {
			byte bt;
			int length;
			bt = stream.readByte();
			if (bt != 'H') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			i = stream.readInt();
			if (i < SdaiSession.valid_from) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_OLD0 + location + ".header" +
					AdditionalMessages.BF_OLD1 + i + ". " + SdaiSession.line_separator +
					AdditionalMessages.BF_OLD2 + SdaiSession.valid_from;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (description == null) {
				description = new A_string(SdaiSession.listTypeSpecial, this);
			} else {
				description.myLength = 0;
			}
			for (i = 1; i <= length; i++) {
				description.addByIndexPrivate(i, stream.readUTF());
			}
			implementation_level = stream.readUTF();
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			name = stream.readUTF();
			if (file_name == null) {
				file_name = new FILE_NAME(this);
			} else {
				file_name.author.myLength = 0;
				file_name.organization.myLength = 0;
			}
			file_name.name = name;
			String tst = stream.readUTF();
			if (tst.length() > 10 && tst.substring(10,11).equals(" ")) {
				file_name.time_stamp = tst.substring(0, 10) + tst.substring(11);
			} else {
				file_name.time_stamp = tst;
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= length; i++) {
				file_name.author.addByIndexPrivate(i, stream.readUTF());
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= length; i++) {
				file_name.organization.addByIndexPrivate(i, stream.readUTF());
			}
			file_name.preprocessor_version = stream.readUTF();
			file_name.originating_system = stream.readUTF();
			file_name.authorization = stream.readUTF();
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (file_schema == null) {
				file_schema = new FILE_SCHEMA(this);
			} else {
				file_schema.schema_identifiers.myLength = 0;
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			for (i = 1; i <= length; i++) {
				file_schema.schema_identifiers.addByIndexPrivate(i, stream.readUTF());
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			int new_length;
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (languages == null) {
				if (LANGUAGES_ARRAY_SIZE >= length) {
					languages = new SECTION_LANGUAGE[LANGUAGES_ARRAY_SIZE];
				} else {
					languages = new SECTION_LANGUAGE[length];
				}
			} else if (length > languages.length) {
				new_length = languages.length * 2;
				if (length > new_length) {
					new_length = length;
				}
				languages = new SECTION_LANGUAGE[new_length];
			}
			languages_count = 0;
			for (i = 0; i < length; i++) {
				SECTION_LANGUAGE sec_lang;
				bt = stream.readByte();
				if (bt == 'M') {
					sec_lang = new SECTION_LANGUAGE(null, stream.readUTF());
					language = sec_lang.default_language;
				} else if (bt == 'P') {
					sec_lang = new SECTION_LANGUAGE(stream.readUTF(), stream.readUTF());
				} else {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				languages[languages_count++] = sec_lang;
			}
			bt = stream.readByte();
			if (bt != 'B') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			length = stream.readInt();
			if (length < 0) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (contexts == null) {
				if (CONTEXTS_ARRAY_SIZE >= length) {
					contexts = new SECTION_CONTEXT[CONTEXTS_ARRAY_SIZE];
				} else {
					contexts = new SECTION_CONTEXT[length];
				}
			} else if (length > contexts.length) {
				new_length = contexts.length * 2;
				if (length > new_length) {
					new_length = length;
				}
				contexts = new SECTION_CONTEXT[new_length];
			}
			contexts_count = 0;
			for (i = 0; i < length; i++) {
				SECTION_CONTEXT sec_con;
				String sect = null;
				A_string strings;
				bt = stream.readByte();
				if (bt == 'P') {
					sect = stream.readUTF();
				} else if (bt != 'M') {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				strings = new A_string(SdaiSession.listTypeSpecial, this);
				int inner_length = stream.readInt();
				if (inner_length < 0) {
					String base = SdaiSession.line_separator + AdditionalMessages.BF_NEGL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				for (int j = 1; j <= inner_length; j++) {
					strings.addByIndexPrivate(j, stream.readUTF());
				}
				sec_con = new SECTION_CONTEXT(sect, strings);
				contexts[contexts_count++] = sec_con;
				if (sect == null) {
					context_identifiers = strings;
				}
			}
			bt = stream.readByte();
			if (bt != 'E') {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DAM;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			committed = true;
		} catch (IOException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	Creates a virtual SdaiModel.
	The method is invoked while loading repository or model binary files.
	More specifically, in the cases when a reference to an entity instance
	belonging to a model currently unknown for this repository is found.
*/
	SdaiModel createVirtualModel(String name) throws SdaiException {
		internal_usage = true;
		SdaiModel model = findSdaiModel(name);
		internal_usage = false;
		if (model != null) {
			model.mode = SdaiModel.NO_ACCESS;
			model.change_date = -1;
			if (model.associated_with == null) {
				model.associated_with = new ASchemaInstance(SdaiSession.setType0toN, model);
			} else {
				model.associated_with.shrink(this);
			}
			return model;
		}
		model = createModel(name);
		model_count++;
		model.identifier = model_count;
		models.addUnorderedRO(model);
		insertModel();
		return model;
	}


/**
	Given a name, searches for the data dictionary model with this name.
	If failure, then new model with the submitted name is created.
	This method is invoked in different contexts in several classes:
	- in PhFileReader, to get data dictionary model for the Express schema name
	   extracted from a part-21 file being read;
	- in SdaiModel, extractModel method, to find the referenced model in the
	   case when the repository is systemRepository;
	- in this class while creating dictionary models during loading of
	   'repository' binary file;
	- in SdaiSession, to get schema definition for the given package name,
	   and also in the method initSuper.
*/
	SdaiModel findDictionarySdaiModel(String model_name) throws SdaiException {
if (SdaiSession.debug2) System.out.println("   REPOSIT new model in data_dictionary: " + model_name);
		if (model_name == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		SdaiModel model = findSdaiModel(model_name);
		if (model == null) {
			model =
				createModel(model_name, SdaiSession.baseDictionaryModel.described_schema);
			models.addUnorderedRO(model);
			insertModel();
			session.data_dictionary.associated_models.addUnorderedRO(model);
			model.associated_with.addUnorderedRO(session.data_dictionary);
		} else if (model.underlying_schema == null) {
			model.underlying_schema = SdaiSession.baseDictionaryModel.described_schema;
		}
		return model;
	}


	SdaiModel findExistingDictionarySdaiModel(String model_name) throws SdaiException {
		if (model_name == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		SdaiModel model = findSdaiModel(model_name);
		if (model != null && model.underlying_schema == null) {
			model.underlying_schema = SdaiSession.baseDictionaryModel.described_schema;
		}
		return model;
	}


/**
	Finds the index of the SdaiModel with the given name in this repository.
	The method is invoked in findSdaiModel, insertModel and saveRepositoryStream
	(in the latter when the elements of 'associated_models' set are saved).
*/
	int findModel(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = ((SdaiModel)models.myData[middle]).name.compareTo(key);
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
	Puts the last model in the array 'models' in correct position to keep
	SdaiModels in this array ordered alphabetically according to their names.
	The method is invoked:
	- in SdaiModel, methods renameSdaiModel and extractModel; in the latter case
	   when a new (virtual) model is created;
	- in SdaiRepository, methods createSdaiModel, loadContentsStream,
	   loadRepositoryStream, createVirtualModel and findDictionarySdaiModel (in all
	   these cases a new model is created);
	- in SdaiSession to insert the following models in systemRepository:
	   baseDictionaryModel, baseComplexModel, baseMappingModel, and other specific
	   dictionary and mapping models.
*/
	boolean insertModel() throws SdaiException {
		Object mod = models.myData[models.myLength - 1];
		int pos = findModelPosition(0, models.myLength - 2, ((SdaiModel)mod).name);
		if (pos < 0) {
			return false;
		}
		if (pos < models.myLength - 1) {
			for (int i = models.myLength - 1; i > pos; i--) {
				models.myData[i] = models.myData[i - 1];
			}
		}
		models.myData[pos] = mod;
		return true;
	}


/**
	Given a name, finds the position where the <code>SdaiModel</code> with this
	name should be inserted to keep the ordering of <code>SdaiModel</code>s
	within the array 'models' inviolated. This method is always applied when
	there is no <code>SdaiModel</code> with the specified name in 'models'
	array. If this, however, happens, then value -1 is returned.
*/
	private int findModelPosition(int left, int right, String key) throws SdaiException {
		if (right < left) {
			return 0;
		}
		int comp_res = ((SdaiModel)models.myData[left]).name.compareTo(key);
		if (comp_res > 0) {
			return 0;
		} else if (comp_res == 0) {
			return -1;
		}
		comp_res = ((SdaiModel)models.myData[right]).name.compareTo(key);
		if (comp_res < 0) {
			return right + 1;
		} else if (comp_res == 0) {
			return -1;
		}
		while (left <= right) {
			if (right-left <= 1) {
				return right;
			}
			int middle = (left + right)/2;
			comp_res = ((SdaiModel)models.myData[middle]).name.compareTo(key);
			if (comp_res < 0) {
				left = middle;
			} else if (comp_res > 0) {
				right = middle;
			} else {
				return -1;
			}
		}
		return -1;
	}


/**
	Finds the index of the SchemaInstance with the given name in this repository.
	The method is invoked in public findSchemaInstance method and also in
	insertSchemaInstance.
*/
	private int findSchemaInstance(int left, int right, String key) throws SdaiException {
		while (left <= right) {
			int middle = (left + right)/2;
			int comp_res = ((SchemaInstance)schemas.myData[middle]).name.compareTo(key);
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
	Puts the last SchemaInstance in the array 'schemas' in correct position to keep
	SchemaInstances in this array ordered alphabetically according to their names.
	The method is invoked:
	- in constructor of SdaiSession;
	- in SdaiRepository, methods createSchemaInstance, loadContentsStream,
	   loadRepositoryStream, and loadSchemaInstances.
*/
	boolean insertSchemaInstance() throws SdaiException {
		Object schema_inst = schemas.myData[schemas.myLength - 1];
		int pos = findSchemaInstancePosition(0, schemas.myLength - 2, ((SchemaInstance)schema_inst).name);
		if (pos < 0) {
			return false;
		}
		if (pos < schemas.myLength - 1) {
			for (int i = schemas.myLength - 1; i > pos; i--) {
				schemas.myData[i] = schemas.myData[i - 1];
			}
		}
		schemas.myData[pos] = schema_inst;
		return true;
	}


/**
	Given a name, finds the position where the <code>SchemaInstance</code> with this
	name should be inserted to keep the ordering of <code>SchemaInstance</code>s
	within the array 'schemas' inviolated. This method is always applied when
	there is no <code>SchemaInstance</code> with the specified name in 'schemas'
	array. If this, however, happens, then value -1 is returned.
*/
	private int findSchemaInstancePosition(int left, int right, String key) throws SdaiException {
		if (right < left) {
			return 0;
		}
		int comp_res = ((SchemaInstance)schemas.myData[left]).name.compareTo(key);
		if (comp_res > 0) {
			return 0;
		} else if (comp_res == 0) {
			return -1;
		}
		comp_res = ((SchemaInstance)schemas.myData[right]).name.compareTo(key);
		if (comp_res < 0) {
			return right + 1;
		} else if (comp_res == 0) {
			return -1;
		}
		while (left <= right) {
			if (right-left <= 1) {
				return right;
			}
			int middle = (left + right)/2;
			comp_res = ((SchemaInstance)schemas.myData[middle]).name.compareTo(key);
			if (comp_res < 0) {
				left = middle;
			} else if (comp_res > 0) {
				right = middle;
			} else {
				return -1;
			}
		}
		return -1;
	}


	boolean correctSchemaInstancePosition(String o_name, String n_name) throws SdaiException {
		int o_pos = findSchemaInstance(0, schemas.myLength - 1, o_name);
		if (o_pos < 0) {
			return false;
		}
		int n_pos = findSchemaInstancePosition(0, schemas.myLength - 1, n_name);
		if (n_pos < 0) {
			return false;
		}
		if (n_pos == o_pos) {
			return true;
		}
		Object schema_inst = schemas.myData[o_pos];
		if (n_pos < o_pos) {
			for (int i = o_pos; i > n_pos; i--) {
				schemas.myData[i] = schemas.myData[i - 1];
			}
			schemas.myData[n_pos] = schema_inst;
		} else {
			n_pos--;
			for (int i = o_pos; i < n_pos; i++) {
				schemas.myData[i] = schemas.myData[i + 1];
			}
			schemas.myData[n_pos] = schema_inst;
		}
		return true;
	}


/**
	Prepares in a suitable form an information on default languages and
	context identifiers to be exported to a part-21 file.
*/
	private void updateHeaderData() throws SdaiException {
		boolean default_language_needed = false, default_context_needed = false;
		int l_count = 0, c_count = 0;
		if (models.myLength == 0) {
			default_language_needed = true;
			default_context_needed = true;
		}
		for (int k = 0; k < models.myLength; k++) {
			SdaiModel mod = (SdaiModel)models.myData[k];
			if (mod.language != null) {
				if (l_count < languages_count) {
					languages[l_count].section = mod.name;
					languages[l_count].default_language = mod.language;
				} else {
					SECTION_LANGUAGE sect_language = new SECTION_LANGUAGE(mod.name, mod.language);
					if (languages == null) {
						languages = new SECTION_LANGUAGE[LANGUAGES_ARRAY_SIZE];
					} else if (l_count >= languages.length) {
						enlarge_languages();
					}
					languages[l_count] = sect_language;
				}
				l_count++;
			} else {
				default_language_needed = true;
			}
			if (mod.context_identifiers != null && mod.context_identifiers.myLength > 0) {
				if (c_count < contexts_count) {
					contexts[c_count].section = mod.name;
					contexts[c_count].context_identifiers = mod.context_identifiers;
				} else {
					SECTION_CONTEXT sect_context = new SECTION_CONTEXT(mod.name, mod.context_identifiers);
					if (contexts == null) {
						contexts = new SECTION_CONTEXT[CONTEXTS_ARRAY_SIZE];
					} else if (c_count >= contexts.length) {
						enlarge_contexts();
					}
					contexts[c_count] = sect_context;
				}
				c_count++;
			} else {
				default_context_needed = true;
			}
		}
		if (default_language_needed && language != null) {
			if (l_count < languages_count) {
				languages[l_count].section = null;
				languages[l_count].default_language = language;
			} else {
				SECTION_LANGUAGE sect_language = new SECTION_LANGUAGE(null, language);
				if (languages == null) {
					languages = new SECTION_LANGUAGE[LANGUAGES_ARRAY_SIZE];
				} else if (l_count >= languages.length) {
					enlarge_languages();
				}
				languages[l_count] = sect_language;
			}
			l_count++;
		}
		languages_count = l_count;
		if (default_context_needed && context_identifiers != null && context_identifiers.myLength > 0) {
			if (c_count < contexts_count) {
				contexts[c_count].section = null;
				contexts[c_count].context_identifiers = context_identifiers;
			} else {
				SECTION_CONTEXT sect_context = new SECTION_CONTEXT(null, context_identifiers);
				if (contexts == null) {
					contexts = new SECTION_CONTEXT[CONTEXTS_ARRAY_SIZE];
				} else if (c_count >= contexts.length) {
					enlarge_contexts();
				}
				contexts[c_count] = sect_context;
			}
			c_count++;
		}
		contexts_count = c_count;
	}


/**
	Compares the string (or strings) written in FILE_SCHEMA instance
	in the header of an exchange structure with strings contained
	in 'property_schemas' array for models in System Repository.
	If the result of some such comparison is positive, then the model
   on which this occurs is returned. Otherwise the return value is null.
	Additionally, in the case of success, the 'true' schema name is
	constructed (from the name of the model found) and stored in
	this SdaiRepository.
*/
	SdaiModel get_schema_model() throws SdaiException {
		A_string string_list = file_schema.schema_identifiers_short;
		int ln = string_list.myLength;
		if (ln <= 0) {
			string_list = file_schema.schema_identifiers;
			ln = string_list.myLength;
		}
		SdaiRepository repo = SdaiSession.systemRepository;
		for (int i = 0; i < repo.models.myLength; i++) {
//			SdaiModel mod = (SdaiModel)repo.models.myData[i];
			SdaiModelDictionaryImpl mod = (SdaiModelDictionaryImpl)repo.models.myData[i];
			if (mod.property_schemas == null) {
				continue;
			}
//System.out.println(" ++++++++++  SdaiModel = " + mod.name);
//if (mod.property_schemas != null)
//for (int k = 0; k < mod.property_schemas.length; k++) {
//System.out.println("   SdaiRepository  ******* k = " + k);
//String [] line = mod.property_schemas[k];
//for (int j = 0; j < line.length; j++) {
//System.out.println("   SdaiRepository  str = " + line[j]);
//}
//}

// Below is the old code (valid till 2008-02-25)
/*			for (int j = 0; j < mod.property_schemas.length; j++) {
				String [] line = mod.property_schemas[j];
				boolean match = true;
				if (line.length == 1 && line[0].equals(ASTERISK)) {
				} else {
					if (line.length != ln) {
						continue;
					}
					for (int k = 1; k <= ln; k++) {
						String item = string_list.getByIndex(k);
						boolean found = false;
						for (int l = 0; l < line.length; l++) {
							if (compare_against_pattern(item, line[l])) {
								found = true;
								break;
							}
						}
						if (!found) {
							match = false;
							break;
						}
					}
				}
				if (match) {
					int sch_length = mod.name.length() - SdaiSession.ENDING_FOR_DICT;
					String sch = mod.name.substring(0, sch_length);
					file_schema.schema = sch;
					return mod;
				}
			}
*/

			boolean match = true;
			for (int k = 1; k <= ln; k++) {
				String item = string_list.getByIndex(k);
				boolean found = false;
				for (int j = 0; j < mod.property_schemas.length; j++) {
					String [] line = mod.property_schemas[j];
					if (line.length >1) {
						continue;
					}
					if (line[0].equals(ASTERISK)) {
						found = true;
					} else if (compare_against_pattern(item, line[0])) {
						found = true;
					}
					if (found) {
						break;
					}
				}
				if (!found) {
					match = false;
					break;
				}
			}
			if (match) {
				int sch_length = mod.name.length() - SdaiSession.ENDING_FOR_DICT;
				String sch = mod.name.substring(0, sch_length);
				file_schema.schema = sch;
				return mod;
			}

		}
		return null;
	}


	private boolean compare_against_pattern(String source, String pattern) throws SdaiException {
// Example:
// if source = "A_D_CCRR000_ABHHFG121212XYZ1212", pattern = "A_D_CC***AB**12*XYZ**", then
// the result is 'true';
// if pattern = "A_D_CC***AB**12*XYZ", then for the above source the result is 'false';
// if source = "A_D_CCRR000_ABHHFG121212XYZ", pattern = "A_D_CC***AB**12*XYZ", then again
// the result is 'true'.
//		String s = source;

		String s_init = source;
		String s = "";
		int prev_ind=0,index=0;
		int s_ln=s_init.length();
		while (index < s_ln) {
			if (s_init.charAt(index) == '*') {
				s = s + s_init.substring(prev_ind, index);
				prev_ind = index + 1;
			}
			index++;
		}
		s = s + s_init.substring(prev_ind, s_ln);

		String str = pattern;
		int ast_ind = str.indexOf('*');
		if (ast_ind < 0) {
			return source.equals(pattern);
		}
		int next_ind;
		boolean repeat = false;
		while (ast_ind > 0) {
			if (repeat) {
				String substr = str.substring(0, ast_ind);
				int i = s.indexOf(substr);
				if (i < 0) {
					return false;
				}
				next_ind = i + ast_ind;
				if (ast_ind == str.length()) {
					if (next_ind == str.length()) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				String prefix = str.substring(0, ast_ind);
				if (!s.startsWith(prefix)) {
					return false;
				}
				next_ind = ast_ind;
				repeat = true;
			}
			int ind = ast_ind + 1;
			int ln = str.length();
			while (ind < ln) {
				if (str.charAt(ind) != '*') {
					break;
				}
				ind++;
			}
			if (ind == ln) {
				return true;
			}
			str = str.substring(ind);
			s = s.substring(next_ind);
			ast_ind = str.indexOf('*');
			if (ast_ind < 0) {
				ast_ind = str.length();
			}
		}
		return true;
	}


/**
	Returns the data dictionary model describing the governing schema of an
	instance of the entity 'file_population' submitted through the method's
	parameter.
	Additionally, if the governing schema is specified by a non-standard
	name (alias, for example),then the 'true' schema name is constructed
	(from the name of the corresponding data dictionary model) and substituted
	for the old governing schema name.
*/
	SdaiModel get_schema_model(FILE_POPULATION pop) throws SdaiException {
		String sch_name = pop.governing_schema;
		SdaiModel mod = get_schema_model(sch_name);
		if (mod == null) {
			String dict_name = sch_name.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
			mod = findDictionarySdaiModel(dict_name);
		} else {
			int sch_length = mod.name.length() - SdaiSession.ENDING_FOR_DICT;
			String sch = mod.name.substring(0, sch_length);
			pop.governing_schema = sch;
		}
		return mod;
	}


/**
	Compares the string submitted through the parameter with strings contained
	in 'property_schemas' array for models in System Repository.
	If the result of some such comparison is positive, then the model
   on which this occurs is returned. Otherwise the return value is null.
*/
	SdaiModel get_schema_model(String sch_name) throws SdaiException {
		for (int i = 0; i < models.myLength; i++) {
//			SdaiModel mod = (SdaiModel)models.myData[i];
			SdaiModelDictionaryImpl mod = (SdaiModelDictionaryImpl)models.myData[i];
			if (mod.property_schemas == null) {
				continue;
			}
			for (int j = 0; j < mod.property_schemas.length; j++) {
				String [] line = mod.property_schemas[j];
//				if (line.length == 1 && sch_name.equals(line[0])) {
				if (line.length == 1 && compare_against_pattern(sch_name, line[0])) {
					return mod;
				}
			}
		}
		return null;
	}


	SdaiModel verify_schema_name(String pattern_schema, String sch_name) throws SdaiException {
		String search_mod_name = pattern_schema + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModelDictionaryImpl mod =
			(SdaiModelDictionaryImpl)SdaiSession.systemRepository.findSdaiModel(search_mod_name);
		if (mod == null || mod.property_schemas == null) {
			return null;
		}
		for (int j = 0; j < mod.property_schemas.length; j++) {
			String [] line = mod.property_schemas[j];
			if (line.length == 1 && compare_against_pattern(sch_name, line[0])) {
				return mod;
			}
		}
		return null;
	}


/**
	Finds entity definition for a given entity java class.
	The method is used in isKindOf(Class type) in CEntity.
*/
	CEntity_definition getEntityDefinition(Class type) throws SdaiException {
		if (this != SdaiSession.systemRepository) {
			return null;
		}
		CEntity_definition def;
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			if (model.underlying_schema != SdaiSession.baseDictionaryModel.described_schema) {
				continue;
			}
			if (model.getMode() == SdaiModel.NO_ACCESS) {
				model.startReadOnlyAccess();
			}
			def = model.schemaData.findEntity(type);
			if (def != null) {
				return def;
			}
		}
		return null;
	}


/**
	Returns 'true' iff the aggregate submitted as a parameter
	contains exactly the same models as aggregate 'models' in this class.
*/
	boolean isComplete(ASdaiModel aggr) throws SdaiException {
		for (int i = 0; i < models.myLength; i++) {
			SdaiModel model = (SdaiModel)models.myData[i];
			boolean found = false;
			for (int j = 0; j < aggr.myLength; j++) {
				SdaiModel mod = (SdaiModel)aggr.myData[j];
				if (mod == model) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}


	protected SdaiRepositoryHeader toSdaiRepositoryHeader() throws SdaiException {
		int i;
		SdaiRepositoryHeader repoHeader = new SdaiRepositoryHeader();
		repoHeader.name = name;
		repoHeader.description = new String[description.myLength];
		for (i = 0; i < description.myLength; i++) {
			repoHeader.description[i] = description.getByIndex(i + 1);
		}
		repoHeader.changeDate = file_name.time_stamp;
		int count = file_name.author.myLength;
		repoHeader.author = new String[count];
		for (i = 0; i < count; i++) {
			repoHeader.author[i] = file_name.author.getByIndex(i + 1);
		}
		count = file_name.organization.myLength;
		repoHeader.organization = new String[count];
		for (i = 0; i < count; i++) {
			repoHeader.organization[i] = file_name.organization.getByIndex(i + 1);
		}
		repoHeader.preprocessorVersion = file_name.preprocessor_version;
		repoHeader.originatingSystem = file_name.originating_system;
		repoHeader.authorization = file_name.authorization;
		repoHeader.defaultLanguage = language;
		if (context_identifiers == null) {
			count = 0;
		} else {
			count = context_identifiers.myLength;
		}
		repoHeader.contextIdentifiers = new String[count];
		for (i = 0; i < count; i++) {
			repoHeader.contextIdentifiers[i] = context_identifiers.getByIndex(i + 1);
		}
		return repoHeader;
	}


	protected void fromSdaiRepositoryHeader(SdaiRepositoryHeader repoHeader) throws SdaiException {
		int i;
//		name = repoHeader.name;
		if (description == null) {
			description = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (repoHeader.description != null) {
			for (i = 0; i < repoHeader.description.length; i++) {
				description.addByIndexPrivate(i + 1, repoHeader.description[i]);
			}
		}
		if (file_name == null) {
			file_name = new FILE_NAME(this);
		}
		file_name.name = name;
		if (repoHeader.changeDate == null) {
			printWarningOnHeaderToLogo(session, AdditionalMessages.RC_DAMI, name);
			long time = System.currentTimeMillis();
			file_name.time_stamp = session.cal.longToTimeStamp(time);
		} else {
			file_name.time_stamp = repoHeader.changeDate;
		}
		if (repoHeader.author != null) {
			for (i = 0; i < repoHeader.author.length; i++) {
				file_name.author.addByIndexPrivate(i + 1, repoHeader.author[i]);
			}
		}
		if (repoHeader.organization != null) {
			for (i = 0; i < repoHeader.organization.length; i++) {
				file_name.organization.addByIndexPrivate(i + 1, repoHeader.organization[i]);
			}
		}
		file_name.preprocessor_version = repoHeader.preprocessorVersion;
		file_name.originating_system = repoHeader.originatingSystem;
		file_name.authorization = repoHeader.authorization;
		language = repoHeader.defaultLanguage;
		if (context_identifiers == null) {
			context_identifiers = new A_string(SdaiSession.listTypeSpecial, this);
		}
		if (repoHeader.contextIdentifiers != null) {
			for (i = 0; i < repoHeader.contextIdentifiers.length; i++) {
				context_identifiers.addByIndexPrivate(i + 1, repoHeader.contextIdentifiers[i]);
			}
		}
		if (file_schema == null) {
			file_schema = new FILE_SCHEMA(this);
		}
	}


	static void printWarningOnHeaderToLogo(SdaiSession ss, String text, String repo_name) throws SdaiException {
		if (ss.logWriterSession != null) {
			ss.printlnSession(text + SdaiSession.line_separator + AdditionalMessages.BF_REP + repo_name);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator + AdditionalMessages.BF_REP + repo_name);
		}
	}





	void enlarge_languages() {
		int new_length = languages.length * 2;
		SECTION_LANGUAGE [] new_languages = new SECTION_LANGUAGE[new_length];
		System.arraycopy(languages, 0, new_languages, 0, languages.length);
		languages = new_languages;
	}


	void enlarge_contexts() {
		int new_length = contexts.length * 2;
		SECTION_CONTEXT [] new_contexts = new SECTION_CONTEXT[new_length];
		System.arraycopy(contexts, 0, new_contexts, 0, contexts.length);
		contexts = new_contexts;
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, String repo_name, String mod_name,
			SchemaInstance schema) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
				AdditionalMessages.BF_REP + repo_name + SdaiSession.line_separator +
				AdditionalMessages.BF_MOD + mod_name + SdaiSession.line_separator +
				AdditionalMessages.BF_SCH + schema.name + SdaiSession.line_separator +
				AdditionalMessages.BF_REPS + schema.repository.name);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
				AdditionalMessages.BF_REP + repo_name + SdaiSession.line_separator +
				AdditionalMessages.BF_MOD + mod_name + SdaiSession.line_separator +
				AdditionalMessages.BF_SCH + schema.name + SdaiSession.line_separator +
				AdditionalMessages.BF_REPS + schema.repository.name);
		}
	}


	static void printWarningToLogo2(SdaiSession session, String text, String repo_name,
			SchemaInstance schema, String included_schema) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
				AdditionalMessages.BF_REP + repo_name + SdaiSession.line_separator +
				AdditionalMessages.BF_SCH + schema.name + SdaiSession.line_separator +
				AdditionalMessages.BF_REPS + schema.repository.name + SdaiSession.line_separator +
				AdditionalMessages.BF_ISCH + included_schema);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
				AdditionalMessages.BF_REP + repo_name + SdaiSession.line_separator +
				AdditionalMessages.BF_SCH + schema.name + SdaiSession.line_separator +
				AdditionalMessages.BF_REPS + schema.repository.name + SdaiSession.line_separator +
				AdditionalMessages.BF_ISCH + included_schema);
		}
	}


/* Creates a new target SdaiModel in this repository
 * and invoke copyApplicationInstance for all instances in source model for the target model
 * and finally update references between target instances to become local.
 * The target model inherits the underlying schema from the source model.
 * @param source, the SdaiModel to be copied
 * @param name, the name of the new created target SdaiModel.
 *   If this value is null, then the source name is taken (different repository!)
 * @return the entity instance with the persistent label
 * @throws SdaiException TR_NEXS, a transaction has not been started.
 * @throws SdaiException RP_NOPN, the repository is not open.
 * @throws SdaiException RP_NEXS, the repository does not exist.
 * @throws SdaiException FN_NAVL, this function is not supported by implementation.
 * @throws SdaiException MO_DUP, SDAI-model duplicate (same name)
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 * @see SdaiRepository#createSdaiModel
 * @see CEntity#copyApplicationInstance
 */
/*	public void addCopy(ASdaiModel models, ASchemaInstance schemas) throws SdaiException {
	}
	public SdaiModel addCopy(SdaiModel source, String name) throws SdaiException {
		return null;
	}
*/


/**
 * Adds <code>SdaiListener</code> extending <code>java.util.EventListener</code>
 * to a special aggregate in this repository.
 * @param listener a <code>SdaiListener</code> to be added.
 * @see #removeSdaiListener
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
	Check remote repository RW access
	@throws SdaiException RP_RO, read-only access to repository.
	@throws SdaiException RP_LOCK, Repository locked by another user.
*/
	void checkServerRWAccess() throws SdaiException{
		if(location!=null&&ostream!=null&&((String)location).startsWith(SdaiSession.LOCATION_PREFIX)) {
			try {  //Checking for update repository
				ostream.writeByte('U');
				int result = istream.readByte();
				if(result == 0) {
					throw new SdaiException(SdaiException.RP_RO);
				}
				if(result == -5) {
					throw new SdaiException(SdaiException.RP_LOCK);
				}
			}  catch (IOException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.NE_IOEX;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
	}


/**
	Some stuff for JSDAI-Server.
*/
	void initServerListener() throws java.io.IOException, java.net.UnknownHostException, java.io.IOException{
		sockListen = new Socket(addr, port);   					   // client support listener
		DataInput plb = new DataInputStream (sockListen.getInputStream());
		DataOutput plo = new DataOutputStream (sockListen.getOutputStream());
		plo.writeUTF(conThread);
		ServerListener rl = new ServerListener(sockListen, this);// Listener socket
		Thread thread = new Thread(rl);
		thread.setDaemon(true);
		thread.start();
	}


	// Start of new redesign: SdaiRepostitory becomes an abstract class
	// and gets couple creation methods (V.N.)

	protected abstract SdaiModel createModel(String model_name, CSchema_definition schema
											 ) throws SdaiException;

	protected abstract SdaiModel createModel(String model_name, SdaiModel dict) throws SdaiException;

	protected abstract SdaiModel createModel(String model_name) throws SdaiException;

	protected void addVirtualRepository(SdaiRepository repo) throws SdaiException {
		session.known_servers.addUnorderedRO(repo);
	}

/**
	Creates a repository called 'virtual'. In this repository only
	the following fields from those defined in Part 22 are set with values:
	- name;
	- session;
	- models.
	This method is used to create a repository for the repository name
	found when reading model's binary file provided there is no repository
	with this name in the set 'known_servers'.
	The method is invoked in one of variations of extractModel in class
	SdaiModel.
*/
	protected abstract SdaiRepository createVirtualRepository(String name) throws SdaiException;

	/** This is a callback method.
	 * It is called during commit from SdaiTransaction.commit
	 * when repository state needs to be committed.
	 */
	boolean committing(boolean commit_bridge) throws SdaiException {
		commit_bridge = committingInternal(commit_bridge, modified);
		modified = false;
		return commit_bridge;
	}

	protected SessionRemote get_bridgeSession() {
		return session.bridgeSession;
	}

	void commitSetFields(boolean repo_stored_not_after_import) throws SdaiException {
		model_deleted = false;
		schema_instance_deleted = false;
		committed = true;
		unresolved_mod_count = unresolved_sch_count = 0;
		if (repo_stored_not_after_import) {
			long time = System.currentTimeMillis();
			file_name.time_stamp = session.cal.longToTimeStamp(time);
		}
	}

	void aborting(boolean contents_modified) throws SdaiException {
		//Abort external data
		if(entityExternalData != null) {
			for (Iterator i = entityExternalData.values().iterator(); i.hasNext();) {
				ExternalData exData = (ExternalData) i.next();
				exData.abort();
			}
		}

		abortingInternal(modified, contents_modified);
		modified = false;
	}

	protected abstract void abortingInternal(boolean modif, boolean contents_modified) throws SdaiException;

	void restoring(SdaiTransaction trans) throws SdaiException {
		if (!isRemote()) {
			return;
		}
		for (int j = 0; j < trans.stack_length; j++) {
			SdaiModel model = trans.stack_del_mods[j];
			if (trans.stack_del_mods_rep[j] != this || !model.isRemote() || (model.mode_before_deletion & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				continue;
			}
			restoringInternal(model, model.mode_before_deletion);
		}
		unresolved_mod_count = 0;
		unresolved_sch_count = 0;
	}

	protected abstract boolean isRemote() throws SdaiException;

	protected abstract void restoringInternal(SdaiModel model, int mode) throws SdaiException;

	protected SdaiModel restoringDeletedModelRemote(SdaiModel model, SdaiModelHeader modelHeader) throws SdaiException {
		String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
			dictionary.startReadOnlyAccess();
		}
		SdaiModel restored_mod = model;
		if (restoreDeletedModelRemote(restored_mod, modelHeader.name, dictionary)) {
			restored_mod.fromSdaiModelHeader(modelHeader);
			restored_mod.committed = true;
			return null;
		}
		restored_mod = createSdaiModel(modelHeader.name, dictionary.described_schema);
		restored_mod.identifier = model.identifier;
		restored_mod.created = false;
		restored_mod.fromSdaiModelHeader(modelHeader);
		restored_mod.committed = true;
		return restored_mod;
	}

	void postCommitting() throws SdaiException {
		commitExternalData();
	}

	Object getNextModel(Object current_member) throws SdaiException {
		return getNextModelInternal(current_member);
	}

	protected abstract Object getNextModelInternal(Object current_member) throws SdaiException;

	Object getNextSchInstance(Object current_member) throws SdaiException {
		return getNextSchInstanceInternal(current_member);
	}

	protected abstract Object getNextSchInstanceInternal(Object current_member) throws SdaiException;

	int getModCount() throws SdaiException {
		return getModCountInternal();
	}

	protected abstract int getModCountInternal() throws SdaiException;

	SdaiModelHeader takeModelHeader(Object current_member) throws SdaiException {
		return takeModelHeaderInternal(current_member);
	}

	protected abstract SdaiModelHeader takeModelHeaderInternal(Object current_member) throws SdaiException;

	SdaiModel createNewModel(String mod_name, CSchema_definition schema, Object current_member) throws SdaiException {
		model_created_during_simulation = true;
		SdaiModel model;
		try {
			model = createSdaiModel(mod_name, schema);
		} finally {
			model_created_during_simulation = false;
		}
		model.attachRemoteModel(current_member);
		model.loadInstanceIdentifiersRemoteModel();
		return model;
	}

	int getSchInstCount() throws SdaiException {
		return getSchInstCountInternal();
	}

	protected abstract int getSchInstCountInternal() throws SdaiException;

	SchemaInstanceHeader takeSchInstHeader(Object current_member) throws SdaiException {
		return takeSchInstHeaderInternal(current_member);
	}

	protected abstract SchemaInstanceHeader takeSchInstHeaderInternal(Object current_member) throws SdaiException;

	SdaiModelRemote findSdaiModelBySessionIdentifier(long identif) throws SdaiException {
		return findSdaiModelBySessionIdentif(identif);
	}

	protected abstract SdaiModelRemote findSdaiModelBySessionIdentif(long identif) throws SdaiException;

	SchemaInstance createNewSchemaInstance(SchemaInstanceHeader schInstHeader, CSchema_definition schema, Object current_member) throws SdaiException {
		sch_inst_created_during_simulation = true;
		SchemaInstance sch_inst;
		try {
			sch_inst = createSchemaInstance(schInstHeader.name, schema);
		} finally {
			sch_inst_created_during_simulation = false;
		}
		sch_inst.created = false;
		sch_inst.modified = false;
		sch_inst.fromSchemaInstanceHeader(schInstHeader);
		sch_inst.attachRemoteSchInstance(current_member);
		sch_inst.getAssociatedModelsPrivate();
		return sch_inst;
	}

	ExternalData createEntityExternalData(CEntity instance) throws SdaiException {
		if(entityExternalData == null) entityExternalData = new HashMap();
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = entityExternalData.get(instanceIdentifier);
		if(externalDataObject != null) {
			throw new SdaiException(SdaiException.EI_NVLD, "External data already exists");
		} else {
			return createNewEntityExternalData(instance, false);
		}
	}

	ExternalData getEntityExternalData(CEntity instance) throws SdaiException {
		if(entityExternalData == null) entityExternalData = new HashMap();
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = entityExternalData.get(instanceIdentifier);
		if(externalDataObject != null) {
			return (ExternalData)externalDataObject;
		} else {
			if(entityRemovedExternalData != null
			&& entityRemovedExternalData.containsKey(instanceIdentifier)) {
				throw new SdaiException(SdaiException.EI_NEXS, "External data was removed");
			} else {
				return createNewEntityExternalData(instance, true);
			}
		}
	}

	boolean testEntityExternalData(CEntity instance) throws SdaiException {
		if(entityExternalData == null) entityExternalData = new HashMap();
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = entityExternalData.get(instanceIdentifier);
		if(externalDataObject != null) {
			return true;
		} else {
			if(entityRemovedExternalData != null
			&& entityRemovedExternalData.containsKey(instanceIdentifier)) {
				return false;
			} else {
				return testNewEntityExternalData(instance);
			}
		}
	}

	void removeEntityExternalData(CEntity instance, boolean deletingInstance, boolean deletedExternalData) throws SdaiException {
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = (entityExternalData != null) ?
			entityExternalData.remove(instanceIdentifier) : null;
		if(externalDataObject == null) {
			if(deletingInstance && !testNewEntityExternalData(instance)) {
				return;
			}
			//entityRemovedExternalData = null;
			// FIXME: some one should make this statement more clear before uncommenting,
			// because it brakes code in SdaiRepositoryRMIIml class commitExternalData method.
			if(entityRemovedExternalData == null) {
				entityRemovedExternalData = new HashMap();
			}
			entityRemovedExternalData.put(instanceIdentifier, instance);
			//System.out.println("sheduled for remove");
		} else {
			if(entityRemovedExternalData == null) {
				entityRemovedExternalData = new HashMap();
			}
			entityRemovedExternalData.put(instanceIdentifier, externalDataObject);
			((ExternalData)externalDataObject).setDeleted(deletedExternalData);
			//System.out.println("sheduled for remove2");
		}
	}

	void removeLoadedEntityExternalData(CEntity instance, boolean deletedExternalData) throws SdaiException {
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = (entityExternalData != null) ?
			entityExternalData.remove(instanceIdentifier) : null;
		if(externalDataObject != null) {
			if(entityRemovedExternalData == null) {
				entityRemovedExternalData = new HashMap();
			}
			entityRemovedExternalData.put(instanceIdentifier, externalDataObject);
			((ExternalData)externalDataObject).setDeleted(deletedExternalData);
		}
	}

	void restoreExternalDataForInstance(CEntity instance) {
		Long instanceIdentifier = new Long(instance.instance_identifier);
		ExternalData externalData =
			(ExternalData) ((entityRemovedExternalData != null) ?
				entityRemovedExternalData.get(instanceIdentifier) : null);
		if(externalData != null && !externalData.isDeleted()) {
			entityRemovedExternalData.remove(instanceIdentifier);
			if(entityExternalData == null) {
				entityExternalData = new HashMap();
			}
			entityExternalData.put(instanceIdentifier, externalData);
			externalData.owningEntity = instance;
		}
	}

	void deleteUndoRedoExternalData(long inst_id) {
		Long instanceIdentifier = new Long(inst_id);
		Object externalDataObject = (entityExternalData != null) ?
				entityExternalData.remove(instanceIdentifier) : null;
		if(externalDataObject != null) {
			if(entityRemovedExternalData == null) {
				entityRemovedExternalData = new HashMap();
			}
			entityRemovedExternalData.put(instanceIdentifier, externalDataObject);
			((ExternalData)externalDataObject).setDeleted(true);
		} else if(entityRemovedExternalData != null) {
			externalDataObject = entityRemovedExternalData.get(instanceIdentifier);
			if(externalDataObject != null) {
				((ExternalData)externalDataObject).setDeleted(true);
			}
		}
	}

	void restoreUndoRedoExternalData(long inst_id) {
		Long instanceIdentifier = new Long(inst_id);
		Object externalDataObject = (entityRemovedExternalData != null) ?
				entityRemovedExternalData.remove(instanceIdentifier) : null;
		if(externalDataObject != null) {
			if(entityExternalData == null) {
				entityExternalData = new HashMap();
			}
			entityExternalData.put(instanceIdentifier, externalDataObject);
			((ExternalData)externalDataObject).setDeleted(false);
		} else if(entityExternalData != null) {
			externalDataObject = entityExternalData.get(instanceIdentifier);
			if(externalDataObject != null) {
				((ExternalData)externalDataObject).setDeleted(false);
			}
		}
	}

	protected static long getInstanceIdentifier(CEntity instance) {
		return instance.instance_identifier;
	}

	/** This is a callback method.
	 * It is called internally during commit committing
	 * when repository state needs to be committed.
	 */
	protected abstract boolean committingInternal(boolean commit_bridge, boolean modified) throws SdaiException;

	/**
	 * This method returns null. Remote repository has to override this method.
	 * @return null
	 */
	protected SdaiRepositoryRemote getRepoRemote() {
		 return null;
	}

	/**
	 * This mehod does nothing. Remote repository has to override this method.
	 */
	protected void setRepoRemote(SdaiRepositoryRemote rRemote) {
	}

	protected abstract void commitExternalData() throws SdaiException;
//	{
//		Iterator externalDataIter;
//		if(entityExternalData != null) {
//			externalDataIter = entityExternalData.values().iterator();
//			while(externalDataIter.hasNext()) {
//				ExternalData externalData = (ExternalData)externalDataIter.next();
//				externalData.commit();
//			}
//		}
//		if(entityRemovedExternalData != null) {
//			externalDataIter = entityRemovedExternalData.values().iterator();
//			while(externalDataIter.hasNext()) {
//				Object externalDataObject = externalDataIter.next();
//				if(externalDataObject instanceof CEntity) {
//					removeEntityExternalData...((CEntity)externalDataObject);
//				} else {
//					removeEntityExternalData...((ExternalData)externalDataObject);
//				}
//			}
//			entityRemovedExternalData.clear();
//		}
//	}

	protected abstract ExternalData createNewEntityExternalData(CEntity entity, boolean existing)
		throws SdaiException;
//	{
//		ExternalData newEntityExternalData = new ExternalData...(...);
//		entityExternalData.put(instanceIdentifier, newEntityExternalData);
//		if(entityRemovedExternalData != null) entityRemovedExternalData.remove(instanceIdentifier);
//		return newEntityExternalData;
//	}

	protected abstract boolean testNewEntityExternalData(CEntity entity) throws SdaiException;
//	{
//		ExternalData newEntityExternalData = ....;
//		...;
//		if(newEntityExternalData != null) {
//			entityExternalData.put(instanceIdentifier, newEntityExternalData);
//			if(entityRemovedExternalData != null) entityRemovedExternalData.remove(instanceIdentifier);
//			return true;
//		} else {
//			return false;
//		}
//	}


//    public long getMaxPersistentLabel() {
//        return largest_identifier;
//    }

//    public void setMaxPersistentLabel( long newMaxPersistentLabel) throws SdaiException {
//        if (newMaxPersistentLabel >= largest_identifier) {
//            largest_identifier = newMaxPersistentLabel;
//            current_identifier = newMaxPersistentLabel;
//        }
//        else {
//            throw new SdaiException(SdaiException.VA_NVLD, "New maximal persistent label is lesser than current maximal persistent label");
//        }
//    }

/**
 * Returns current persistent label.
 * @return persistent label.
 * @see #setNextPersistentLabel(long)
 */
    public long getPersistentLabel() {
        return current_identifier;
    }

//    public long getNextUsedPersistentLabel() {
//        return next_used_identifier;
//    }

/**
 * Assigns a <code>long</code> value to the next persistent label, which will be used for the next instance.
 * Value of the next persistent label must be unused.
 * @param newPersistentLabel <code>long</code> value of next persistent label.
 * @throws SdaiException VA_NVLD, value invalid.
 * @see #getPersistentLabel()
 */
    public abstract void setNextPersistentLabel( long newPersistentLabel) throws SdaiException;
//    {
//        newPersistentLabel--;
//        synchronized (syncObject) {
//            if (this instanceof SdaiRepositoryLocalImpl) {
//                //System.out.println("setPL > SdaiRepositoryLocalImpl");
//                //System.out.println("setPL > MODEL: largest="+largest_identifier);
//                if (newPersistentLabel < largest_identifier) {
//                    //System.out.println("setPL > newPersistentLabel="+newPersistentLabel);
//                    int i,m;
//                    SdaiModel model;
//                    long next_used_pl=-1;
//                    for (i = 0; i < models.myLength; i++) {
//                        model = (SdaiModel)models.myData[i];
//                        //System.out.println("setPL > MODEL: name="+model.getName());
//                        //if (model.mode == SdaiModel.NO_ACCESS) {
//                            if (model.inst_idents != null) {
//                                /*System.out.print("setPL > MODEL: PLs=");
//                                for(int l=0; l<model.inst_idents.length; l++) {
//                                    System.out.print( model.inst_idents[l]+" ");
//                                }
//                                System.out.println();*/
//                                int idx = Arrays.binarySearch( model.inst_idents, newPersistentLabel+1);
//                                //System.out.println("setPL > MODEL: idx="+idx);
//                                if (idx < 0) {      // pl is not found in model
//                                    idx = -idx -1;  // index of closest bigger pl
//                                    //System.out.println("setPL > MODEL: inst_idents.length="+model.inst_idents.length);
//                                    if (idx < model.inst_idents.length) {   // if index is equal to array length, then there is no bigger pl
//                                        if (model.inst_idents[idx] < next_used_pl || next_used_pl == -1) {    // choosing minimal of used bigger pl
//                                            next_used_pl = model.inst_idents[idx];
//                                            //System.out.println("setPL > MODEL: next_used_pl="+next_used_pl);
//                                        }
//                                    }
//                                }
//                                else {              // pl is found in model
//                                    throw new SdaiException(SdaiException.VA_NVLD);
//                                }
//                            }
//                        //}
//                    }
//
//                    next_used_identifier = next_used_pl;
//                }
//                else {
//                    largest_identifier = newPersistentLabel;
//                    next_used_identifier = -1;
//                }
//                current_identifier = newPersistentLabel;
//                //System.out.println("setPL > MODEL: current="+current_identifier);
//                //System.out.println("setPL > MODEL: next_used="+next_used_identifier);
//                //System.out.println("setPL > MODEL: largest="+largest_identifier);
//            }
//            else {
//                throw new SdaiException(SdaiException.FN_NAVL);
//            }
//
//		} // syncObject
//    }

/**
 * Returns a <code>long</code> value of persistent label to use and increments it for the next persistent label.
 * @param model <code>SdaiModel</code> from which function is called.
 * @return value of persistent label to use.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getPersistentLabel()
 * @see #setPersistentLabel(long)
 */
    protected long incPersistentLabel( SdaiModel model) throws SdaiException {
        if (largest_identifier < Long.MAX_VALUE) {
            //System.out.println("incPL > next_used="+next_used_identifier);
            if (next_used_identifier < 0) {
                largest_identifier++;
                current_identifier = largest_identifier;
            }
            else {
                current_identifier++;
                if (model.inst_idents != null) {
                    //System.out.println("incPL > current_identifier="+current_identifier);
                    //System.out.println("incPL > model_max="+model.inst_idents[model.inst_idents.length-1]);
                    if (current_identifier <= model.inst_idents[model.inst_idents.length-1]){
                        largest_identifier++;
                        current_identifier = largest_identifier;
                        next_used_identifier = -1;
                    }
                }
                if (current_identifier >= next_used_identifier) {
                    //System.out.println("incPL --> current="+current_identifier);
                    //System.out.println("incPL --> next_used="+next_used_identifier);
                    largest_identifier++;
                    current_identifier = largest_identifier;
                    next_used_identifier = -1;
                }
            }
            //System.out.println("incPL > current="+current_identifier);
            return current_identifier;
		}
        else {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_NVLI;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
    }

    /**
     * This method is safe to call by remote repositories only and the ones that
     * are <code>getRepoRemot() != null</code>.
     * @param persLabel
     * @throws SdaiException
     */
    protected void checkPersistentLabelRange(long persLabel) throws SdaiException { }

/**
 * Informs if remote repository data base id is equal to specified one.
 * @param repositoryId <code>long</code> value of the remote repository data base id.
 * @throws SdaiException
 * @return <code>true</code> if repository is remote and its id is equal to specified one, <code>false</code> otherwise
 */
    protected boolean hasId(long repositoryId) throws SdaiException {
		SdaiRepositoryRemote repoRemote = getRepoRemote();
        if (repoRemote != null) {
            return (repositoryId == repoRemote.getRemoteId());
        }
        else return false;
    }



/**
 * Returns remote <code>SdaiModel</code> using specified model reference.
 * @param modelRef <code>SerializableRef</code> used to refer to remote model.
 * @return <code>SdaiModel</code> refered by specified model reference.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getSchemaInstanceByRef(SerializableRef)
 * @see #getInstanceByRef(SerializableRef)
 */
    public SdaiModel getSdaiModelByRef(SerializableRef modelRef) throws SdaiException {
	    throw new SdaiException(SdaiException.FN_NAVL);
	}


  	protected SdaiModel findSdaiModelById(long modelId) throws SdaiException {
        SdaiModel model;
		for (int i = 0; i < models.myLength; i++) {
            model = (SdaiModel)models.myData[i];
            if (model.hasId(modelId)) {
                return model;
            }
        }
		SdaiTransaction trans = session.active_transaction;
		for (int i = 0; i < trans.stack_length; i++) {
			if(trans.stack_del_mods_rep[i] == this && trans.stack_del_mods[i].hasId(modelId)) {
				return trans.stack_del_mods[i];
			}
		}
		return null;
	}


/**
 * Returns remote <code>SchemaInstance</code> using specified schema instance reference.
 * @param schemaRef <code>SerializableRef</code> used to refer to remote schema instance.
 * @return <code>SchemaInstance</code> refered by specified schema instance reference.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getSdaiModelByRef(SerializableRef)
 * @see #getInstanceByRef(SerializableRef)
 */
    public SchemaInstance getSchemaInstanceByRef(SerializableRef schemaRef) throws SdaiException {
	    throw new SdaiException(SdaiException.FN_NAVL);
	}

    protected SchemaInstance findSchemaInstanceById(long schemaId) throws SdaiException {
        SchemaInstance schema;
		for (int i = 0; i < schemas.myLength; i++) {
            schema = (SchemaInstance)schemas.myData[i];
            if (schema.hasId(schemaId)) {
                return schema;
            }
        }
		SdaiTransaction trans = session.active_transaction;
		for (int i = 0; i < trans.stack_length; i++) {
			if(trans.stack_del_sch_insts_rep[i] == this && trans.stack_del_sch_insts[i].hasId(schemaId)) {
				return trans.stack_del_sch_insts[i];
			}
		}
		return null;
	}


/**
 * Returns remote <code>EEntity</code> using specified entity reference.
 * @param entityRef <code>SerializableRef</code> used to refer to remote entity instance.
 * @return <code>EEntity</code> refered by specified entity instance reference.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getSdaiModelByRef(SerializableRef)
 * @see #getSchemaInstanceByRef(SerializableRef)
 */
    public EEntity getInstanceByRef(SerializableRef entityRef) throws SdaiException {
	    throw new SdaiException(SdaiException.FN_NAVL);
	}

    protected void preCommitting() throws SdaiException {
	}

    //String removeRecurrenceNumber(String name) throws SdaiException {
    //    return name;
    //}
    //
/**
 * Returns the real (without recurrence number) name of the repository as a <code>String</code>.
 * <p> The repositories known to an <code>SdaiSession</code> must have unique names.
 * An exception will be thrown if the repository is deleted or if the
 * <code>SdaiSession</code> is closed.
 * @return the real name of this repository.
 * @throws SdaiException RP_NEXS, repository does not exist.
 * @since 3.6.0
 */
    public String getRealName() throws SdaiException {
        if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
        return name;
    }

    void postCommittingRelease(boolean restore) throws SdaiException {
    }


/**
 * Makes a copy of all models together with instances and schema instances
 * from the specified repository to this repository.
 * References between instances of the given repository are mapped
 * to references between their copies. Other values for attributes of the newly created
 * copies are taken from the original instances similarly as in
 * {@link SdaiModel#copyInstances copyInstances} method.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param sourceRepository the source repository.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #copyFrom(SchemaInstance)
 * @see #copyFrom(ASchemaInstance)
 * @since 3.6.0
 */
    public void copyFrom(SdaiRepository sourceRepository) throws SdaiException {
        ASdaiModel aSourceModel = sourceRepository.getModels();
        SdaiIterator itSourceModel = aSourceModel.createIterator();
        SdaiModel sourceModel;
        SdaiModel targetModel;
        AEntity allSourceInstances;
        AEntity allTargetInstances;
        SdaiIterator itTargetInstances;

        while (itSourceModel.next()) {
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            if (sourceModel.getMode() == SdaiModel.NO_ACCESS) {
                sourceModel.startReadOnlyAccess();
            }
        }

        itSourceModel.beginning();

        if (itSourceModel.next()) {
            //--Copying all instances into one model to retain references between them
            allSourceInstances = aSourceModel.getInstances();
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
            targetModel.startReadWriteAccess();
				targetModel.bypass = true;
            allTargetInstances = targetModel.copyInstances(allSourceInstances);
				targetModel.bypass = false;
            itTargetInstances = allTargetInstances.createIterator();

            //--Passing instances of first model
            for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                itTargetInstances.next();
            }

            //--Moving instances to appropriate models
            while (itSourceModel.next()) {
                sourceModel = aSourceModel.getCurrentMember(itSourceModel);
                targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
                targetModel.startReadWriteAccess();
                for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                    if (itTargetInstances.next()) {
                        EEntity targetInstance = allTargetInstances.getCurrentMemberEntity(itTargetInstances);
								targetModel.bypass = true;
                        targetModel.substituteInstance(targetInstance);
								targetModel.bypass = false;
                    }
                    else {
                        throw new SdaiException(SdaiException.VA_NVLD, "Wrong count of instances in source model, named "+sourceModel.getName());
                    }
                }
            }
        }

        ASchemaInstance aSourceSI = sourceRepository.getSchemas();
        SdaiIterator itSourceSI = aSourceSI.createIterator();
        while (itSourceSI.next()) {
            SchemaInstance sourceSI = aSourceSI.getCurrentMember(itSourceSI);
            SchemaInstance targetSI = this.createSchemaInstance(sourceSI.getName(), sourceSI.getNativeSchema());
            ASdaiModel aSourceAssociatedModel = sourceSI.getAssociatedModels();
            SdaiIterator itSourceAssociatedModel = aSourceAssociatedModel.createIterator();
            while (itSourceAssociatedModel.next()) {
                SdaiModel sourceAssociatedModel =  aSourceAssociatedModel.getCurrentMember(itSourceAssociatedModel);
                SdaiModel targetAssociatedModel = this.findSdaiModel(sourceAssociatedModel.getName());
                targetSI.addSdaiModel(targetAssociatedModel);
            }
        }
    }


/**
 * Makes a copy of all instances from the specified schema instance to this repository.
 * References between instances of the given repository are mapped
 * to references between their copies. Other values for attributes of the newly created
 * copies are taken from the original instances similarly as in
 * {@link SdaiModel#copyInstances copyInstances} method.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param sourceSchemaInstance the source repository.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #copyFrom(SdaiRepository)
 * @see #copyFrom(ASchemaInstance)
 * @since 3.6.0
 */
    public void copyFrom(SchemaInstance sourceSchemaInstance) throws SdaiException {
        ASdaiModel aSourceModel = sourceSchemaInstance.getAssociatedModels() ;
        SdaiIterator itSourceModel = aSourceModel.createIterator();
        SdaiModel sourceModel;
        SdaiModel targetModel;
        AEntity allSourceInstances;
        AEntity allTargetInstances;
        SdaiIterator itTargetInstances;

        while (itSourceModel.next()) {
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            if (sourceModel.getMode() == SdaiModel.NO_ACCESS) {
                sourceModel.startReadOnlyAccess();
            }
        }

        itSourceModel.beginning();

        if (itSourceModel.next()) {
            //--Copying all instances into one model to retain references between them
            allSourceInstances = aSourceModel.getInstances();
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
            targetModel.startReadWriteAccess();
				targetModel.bypass = true;
            allTargetInstances = targetModel.copyInstances(allSourceInstances);
				targetModel.bypass = false;
            itTargetInstances = allTargetInstances.createIterator();

            //--Passing instances of first model
            for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                itTargetInstances.next();
            }

            //--Moving instances to appropriate models
            while (itSourceModel.next()) {
                sourceModel = aSourceModel.getCurrentMember(itSourceModel);
                targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
                targetModel.startReadWriteAccess();
                for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                    if (itTargetInstances.next()) {
                        EEntity targetInstance = allTargetInstances.getCurrentMemberEntity(itTargetInstances);
								targetModel.bypass = true;
                        targetModel.substituteInstance(targetInstance);
								targetModel.bypass = false;
                    }
                    else {
                        throw new SdaiException(SdaiException.VA_NVLD, "Wrong count of instances in source model, named "+sourceModel.getName());
                    }
                }
            }
        }

        SchemaInstance targetSI = this.createSchemaInstance(sourceSchemaInstance.getName(), sourceSchemaInstance.getNativeSchema());
        ASdaiModel aSourceAssociatedModel = sourceSchemaInstance.getAssociatedModels();
        SdaiIterator itSourceAssociatedModel = aSourceAssociatedModel.createIterator();
        while (itSourceAssociatedModel.next()) {
            SdaiModel sourceAssociatedModel =  aSourceAssociatedModel.getCurrentMember(itSourceAssociatedModel);
            SdaiModel targetAssociatedModel = this.findSdaiModel(sourceAssociatedModel.getName());
            targetSI.addSdaiModel(targetAssociatedModel);
        }

    }


/**
 * Makes a copy of all instances from the specified schema instance aggregate to this repository.
 * References between instances of the given repository are mapped
 * to references between their copies. Other values for attributes of the newly created
 * copies are taken from the original instances similarly as in
 * {@link SdaiModel#copyInstances copyInstances} method.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param aSourceSchemaInstance the source repository.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NRW, transaction not read-write.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #copyFrom(SdaiRepository)
 * @see #copyFrom(SchemaInstance)
 * @since 3.6.0
 */
    public void copyFrom(ASchemaInstance aSourceSchemaInstance) throws SdaiException {
        ASdaiModel aSourceModel = aSourceSchemaInstance.getAssociatedModels();
        SdaiIterator itSourceModel = aSourceModel.createIterator();
        SdaiModel sourceModel;
        SdaiModel targetModel;
        AEntity allSourceInstances;
        AEntity allTargetInstances;
        SdaiIterator itTargetInstances;

        while (itSourceModel.next()) {
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            if (sourceModel.getMode() == SdaiModel.NO_ACCESS) {
                sourceModel.startReadOnlyAccess();
            }
        }

        itSourceModel.beginning();

        if (itSourceModel.next()) {
            //--Copying all instances into one model to retain references between them
            allSourceInstances = aSourceModel.getInstances();
            //System.out.println("-- S instances = "+allSourceInstances);
            sourceModel = aSourceModel.getCurrentMember(itSourceModel);
            targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
            targetModel.startReadWriteAccess();
				targetModel.bypass = true;
            allTargetInstances = targetModel.copyInstances(allSourceInstances);
				targetModel.bypass = false;
            itTargetInstances = allTargetInstances.createIterator();
            //System.out.println("-- T instances = "+allTargetInstances);

            //--Passing instances of first model
            //System.out.println("-- S model = "+sourceModel.getName());
            //System.out.println("-- S count = "+sourceModel.getInstanceCount());
            for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                itTargetInstances.next();
            }

            //--Moving instances to appropriate models
            while (itSourceModel.next()) {
                sourceModel = aSourceModel.getCurrentMember(itSourceModel);
                targetModel = this.createSdaiModel(sourceModel.getName(), sourceModel.getUnderlyingSchema());
                targetModel.startReadWriteAccess();
                //System.out.println("-- S model = "+sourceModel.getName());
                //System.out.println("-- S count = "+sourceModel.getInstanceCount());
                for (long i = 0; i < sourceModel.getInstanceCount(); i++) {
                    if (itTargetInstances.next()) {
                        EEntity targetInstance = allTargetInstances.getCurrentMemberEntity(itTargetInstances);
                        //System.out.println("--"+targetInstance);
								targetModel.bypass = true;
                        targetModel.substituteInstance(targetInstance);
								targetModel.bypass = false;
                    }
                    else {
                        throw new SdaiException(SdaiException.VA_NVLD, "Wrong count of instances in source model, named "+sourceModel.getName());
                    }
                }
            }
        }

        SdaiIterator itSourceSI = aSourceSchemaInstance.createIterator();
        while (itSourceSI.next()) {
            SchemaInstance sourceSI = aSourceSchemaInstance.getCurrentMember(itSourceSI);
            SchemaInstance targetSI = this.createSchemaInstance(sourceSI.getName(), sourceSI.getNativeSchema());
            ASdaiModel aSourceAssociatedModel = sourceSI.getAssociatedModels();
            SdaiIterator itSourceAssociatedModel = aSourceAssociatedModel.createIterator();
            while (itSourceAssociatedModel.next()) {
                SdaiModel sourceAssociatedModel =  aSourceAssociatedModel.getCurrentMember(itSourceAssociatedModel);
                SdaiModel targetAssociatedModel = this.findSdaiModel(sourceAssociatedModel.getName());
                targetSI.addSdaiModel(targetAssociatedModel);
            }
        }

    }

    protected final String getNameFast() /*throws SdaiException*/ {
        return name;
    }

    protected final int getModelsMyLength() {
		return models.myLength;
	}

    protected final void setModelsMyLength(int myLength) {
		models.myLength = myLength;
	}

    protected final Object[] getModelsMyData() {
		return models.myData;
	}

    protected final int getSchemasMyLength() {
		return schemas.myLength;
	}

    protected final void setSchemasMyLength(int myLength) {
		schemas.myLength = myLength;
	}

    protected final Object[] getSchemasMyData() {
		return schemas.myData;
	}

	protected void deleteSdaiModelInReopen(SdaiModel model) throws SdaiException {
		model.deleteSdaiModelWork(false, true, false, false);
	}

	protected void deleteSchemaInstanceInReopen(SchemaInstance sch) throws SdaiException {
		sch.repository = null;
		if (sch.associated_models != null) {
			for (int j = 0; j < sch.associated_models.myLength; j++) {
				SdaiModel model = (SdaiModel)sch.associated_models.myData[j];
				if (model.repository != this) {
					model.associated_with.removeUnorderedRO(sch);
				}
			}
			sch.associated_models = null;
		}
		sch.native_schema = null;
	}

	protected static final SdaiRepository getRepositoryFastForModel(SdaiModel model) {
		return model.repository;
	}

	protected static final SdaiRepository getRepositoryFastForSchInst(SchemaInstance schInst) {
		return schInst.repository;
	}

	protected static final void forwardExternalDataCommit(ExternalData externalData) throws SdaiException {
		externalData.commit();
	}

	protected static final void forwardExternalDataSetOwningEntity(ExternalData externalData,
			CEntity owningEntity) throws SdaiException {
		externalData.owningEntity = owningEntity;
	}

	protected static final void forwardExternalDataSetStoreStream(ExternalData externalData,
			InputStream storeStream) throws SdaiException {
		externalData.storeStream = storeStream;
	}

	protected static void forwardExternalDataRemoved(ExternalData extData) throws SdaiException {
		extData.removed();
	}


	void endAccessExternalData(SdaiModel mod_under_closing) throws SdaiException {
		Iterator it;
		ExternalData extData;
		CEntity ownr;
//		Long instIdentObject;
		if (entityExternalData != null) {
			for (it = entityExternalData.values().iterator(); it.hasNext();) {
				extData = (ExternalData)it.next();
				ownr = extData.owningEntity;
				if (ownr == null) {
					continue;
				}
				if (ownr.owning_model != mod_under_closing) {
					continue;
				}
//				instIdentObject = new Long(ownr.instance_identifier);
				extData.owningEntity = null;
//				entityExternalData.remove(instIdentObject);
				it.remove();
				extData.removed();
			}
		}
		if (entityRemovedExternalData != null) {
			for (it = entityRemovedExternalData.values().iterator(); it.hasNext();) {
				extData = (ExternalData)it.next();
				ownr = extData.owningEntity;
				if (ownr == null) {
					continue;
				}
				if (ownr.owning_model != mod_under_closing) {
					continue;
				}
//				instIdentObject = new Long(ownr.instance_identifier);
				extData.owningEntity = null;
//				entityRemovedExternalData.remove(instIdentObject);
				it.remove();
				extData.removed();
			}
		}
	}


 /**
 * Assigns a <code>String</code> value to the location attribute of the repository.
 * This assignment is allowed only if the repository is stored in
 * <a href="../../../guides/SDAIFile.html">SDAI file</a> else
 * SdaiException FN_NAVL is thrown.
 * If the new location has been assigned successfully, then new <a href="../../../guides/SDAIFile.html">SDAI file</a>
 * will be created for the current repository while performing commit operation.
 * Old <a href="../../../guides/SDAIFile.html">SDAI file</a> will remain untouched
 * in the old location after commit operation.
 * <p> This method is an extension of JSDAI, which is
 * not a part of the standard.
 * @param location the new location of the repository.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #getLocation
 * @since 4.0.0
 */
	public void setLocation(String location) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

}
