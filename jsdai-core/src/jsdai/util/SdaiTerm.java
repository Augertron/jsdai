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

package jsdai.util;

import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;

/**
* Command line application which enable you to execute elemental jsdai operations.
* To run it write "java jsdai.util.SdaiTerm"
*/
public class SdaiTerm implements SdaiListener {
	static final int INST_DIM = 1000;
	static final int VAR_DIM = 10;
	Object listing[] = new Object[INST_DIM];

	SdaiSession session;
	SdaiTransaction transaction;
	SdaiRepository repositories[] 		= new SdaiRepository[VAR_DIM];
	SdaiModel models[] 						= new SdaiModel[VAR_DIM];
	EEntity_definition entityDefs[]		= new EEntity_definition[VAR_DIM];
	EDefined_type definedTypes[]			= new EDefined_type[VAR_DIM];
	EntityExtent entityExtents[] 			= new EntityExtent[VAR_DIM];
	EEntity instances[] 						= new EEntity[VAR_DIM];
	EAttribute attributes[] 				= new EAttribute[VAR_DIM];
	Aggregate aggregates[] 					= new Aggregate[VAR_DIM];
	int member = 1;
	SchemaInstance schemaInstances[] 	= new SchemaInstance[VAR_DIM];
	ESchema_definition schemaDefs[]		= new ESchema_definition[VAR_DIM];
	ASchemaInstance aSchemaInstances[]	= new ASchemaInstance[VAR_DIM];

	int ir = 0;  // index into repositories
	int im = 0;  // index into models
	int ix = 0;  // index into entityExtents
	int ie = 0;  // index into entityDefs
	int id = 0;  // index into definedTypes
	int ii = 0;  // index into instances
	int ia = 0;  // index into attributes
	int ig = 0;  // index into aggregate
	int ic = 0;  // index into schemaInstances
	int ih = 0;  // index into schemaDefs
	int in = 0;  // index into aSchemaInstance

	static final int NORMAL_MODE = 1;
	static final int CHECK_MODE = 2;
	static final int CHECK_OUTPUT_MODE = 3;
	static final int SKIP_OUTPUT_MODE = 4;
	int mode = NORMAL_MODE;
	ByteArrayOutputStream capture;
	PrintStream out = System.out;
	String checkString;

	A_string remote_sessions = null;
	String server = "";
	int repo_count;
	boolean echo = false;

	public void actionPerformed(SdaiEvent e) {
		try {
			SdaiRepository repo = (SdaiRepository)e.getSource();
			out.print("Another user commited repository:"+repo.getName()+" !\n:");
		} catch (SdaiException h) {};
	}

/** Method thats run this program.*/
	public static void main(String args[]) throws Exception {
		SdaiTerm mtc = new SdaiTerm();
		mtc.run(args);
	}

	private SdaiTerm() {
	}

	String sHeadLine =
		"Terminal Interface for JSDAI(TM), " + Implementation.copyright;
	String sSepartorLine =
		"-------------------------------------------------------------------------------";
	String sObjectLine =
		" Invoked method - parametres - explanation";

	String[] asHelpMain = {
		sHeadLine,
		sSepartorLine,
		"      SdaiSession s: o(open)  m(import-r) i(implementation) l(list)",
		"                     c(close) r(create-r) f(link-r) s(server) g(log)",
		"  SdaiTransaction t: r(start read only)  c(commit) e(commit+end)   i(info)",
		"                     w(start read write) a(abort)  b(abort+end)",
		"   SdaiRepository r: =(nr) o(open)  d(delete) i(info) s(create-c)",
		"                   :       c(close) e(export) l(list) m(create-m) x(import-r p28)",
		"        SdaiModel m: d(delete) r(startReadOnly) e(endReadOnly) p(promote to RW)",
		"                   : n(rename) w(startReadWrite) f(endReadWrite) i(info)",
		"                   : =(nr) c(create) l(list) o(reduce to RO)",
		"     EntityExtent x: =(nr) i(info) l(list)",
		" EntityDefinition e: =(nr) i(info) l(list)",
		"      DefinedType d: =(nr) i(info",
		"  Entity Instance i: =(nr) i(info) l(list) d(delete)",
		"        Attribute a: =(nr) s(set) u(unset) c(create) i(info)",
		"        Aggregate g: =(nr) a(add) r(remove) c(create) l(list)",
		"           Member b: =(nr)",
		"Schema_definition h: =(nr) i(info) l(list)",
		"   SchemaInstance c: =(nr) d(dictionary) i(info) l(list) n(rename) r, a, e, m",
		"  ASchemaInstance n: =(nr) a(add) c(clear) l(list) - domain",
		" Run command file f: o(file_name)",
		"            Debug .: c(check-mode) n(normal-mode) !(compare) *(skip) i(info)",
		"             help ?: s, t, r, m, x, e, d, i, p, a, g, b, h, c, f, ., ?",
		"             quit q:   ",
		sSepartorLine,
	};

	String[] asHelpSession = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"so : openSession - () - Open this session.",
		"sc : closeSession - () - Close this session.",
		"si : getSdai_implementation - () - Show information about this session.",
		"sl : getKnown_servers - () - List repositories in this session.",
		"sr : createRepository - (name, [location]) - Create repository.",
		"sm : importClearTextEncoding - (path, [name, [location]]) - Import phisical file.",
		"sf : linkRepository - (name, [path]) - Link repository from other path.",
		"ss : remoteRepositories - (server) - Attach server repositories to repository list.",
		"sg : setLogWriter - (name) - Sets log writer to file \"name\" or to \"System.out\"",
		"sb : linkDataBaseBridge(String bridgeURL, String user, char password[])",
		"su : unlinkDataBaseBridge()",
		sSepartorLine,
	};

	String[] asHelpTransaction = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ti : getMode - () - Show information about this transection.",
		"tr : startTransactionReadOnlyAccess - () - Start R/O access.",
		"tw : startTransactionReadWriteAccess - () - Start R/W access.",
		"tc : commit - () - Commit this transection. All changes are saved.",
		"ta : abort - ()- Abort this transection. All changes will disapear.",
		"te : endTransactionAccessCommit - () - Commint and end transeciton.",
		"tb : endTransactionAccessAbort - () - Abort and end transeciton.",
		sSepartorLine,
	};

	String[] asHelpRepository = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ro : openRepository - () - Open this repository.",
		"rc : closeRepository - () - Close this repository.",
		"rd : deleteRepository - () - Imediatly delete this repository.",
		"rx : importXml p28 - (path, [name, [location]]) - Import part28 file.",		
		"re : exportClearTextEncoding - (path) - Export to phisical file",
		"ri : getName.. - () - Show information about this repository.",
		"rl : getModels, getSchemas - () - List all models and schemas of repository.",
		"rm : createSdaiModel - (name, @h | package_name) - Create model.",
		"rs : createSchemaInstance - (name, @h | package_name) - Create schema.",
		"ra : import p21 to existing repository util.Move Class",
		sSepartorLine,
	};

	String[] asHelpModel = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"mn : renameSdaiModel - (name) - Change name of this model.",
		"mr : startReadOnlyAccess - () - Start R/O acces. No changes will be available",
		"mw : startReadWriteAccess - () - Start R/W acces. Changes need to commit with transection",
		"me : endReadOnlyAccess - () - End R/O access.",
		"mf : endReadWriteAccess - ()- End R/W acces. Changes are discarded.",
		"mp : promoteSdaiModelToRW - () - Change access from R/O to R/W.",
		"mo : reduceSdaiModelToRO - () - Change access from R/W to R/O.",
		"md : deleteSdaiModel - () - Delete this model.",
		"mc : createEntityInstance - (@e | class_name) - Create entity instance.",
		"mi : getName.. - () - Show information about this model.",
		"ml : getPopulated_folders - () List populated entity extnends",
		sSepartorLine,
	};

	String[] asHelpExtent = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"xi : getDefinition - () - Show information about this Extend.",
		"xl : getInstances - () - List all entity instance in this extend",
		sSepartorLine,
	};

	String[] asHelpEntityDef = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ei : getName - () - Print information about this entity definition.",
		"el : getExplicit_attributes -() - List attribute of this entity definition.",
		sSepartorLine,
	};

	String[] asHelpDefinedType = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"di : getName - () - Print information about this defined type.",
		sSepartorLine,
	};

	String[] asHelpEntity = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ii : toString - () - Shows instance representation in phisical file.",
		"il : getExplicit_attributes -() - List attribute with value of this entity definition.",
		"id : deleteApplicationInstance -() - Delete this instance. All attributes which was referenced to it are unseted.",
		"iu : prints all instance users. (Instance which have attribute set with this instance)",
		sSepartorLine,
	};

	String[] asHelpAttribute = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ai : getName - () - Shows attribute information.",
		"as : set - (@i, [@d ..], String | double | int | Binary | @i) - Set value of attribute for instance.",
		"au : unsetAttribute - (@i) - Unset value of atribute for instance",
		"ac : createAggregate - (@i) - Create aggregate for attribute. Aplicable if attribute type is aggregate.",
		sSepartorLine,
	};

	String[] asHelpMapping = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"pa : findEntityMappings - (@n, @n) - Finds entity_mappings which fit with this instance.",
		"pb : testSourceEntity - (@e, @n, @n) - Tests whether this instance of a target entity type does fit to a specified source entity.",
		"pc : testMappedEntity - (@i, @n, @n) - Test whether a given targetInstance does fit to the mapping of entity_mapping.",
		"pd : testSourceAttribute - (@a, @n, @n) - Tests mapping of source attribute.",
		"pe : testMappedAttribute - (@i, @n, @n) - Test whether this target instance does fit to the contraints of the given attribute_mapping.",
		"pf : getMappedAttribute - (@a, @n, @n) - Returns aggregate of values for source attribute.",
		"pg : getMappedAttribute - (@i, @n) - Returns value of attribute_mapping.",
		"pi : findMappingInstances - (@e, @n, @n) - Returns instances on which can be used this mapping type.",
		"pk : findMappingInstances - (@i, @n, @n) - Returns instances on which can be used this entity mapping.",
		sSepartorLine,
	};

	String[] asHelpAggregate = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"gl : get... - () - List aggregate members.",
		"ga : add... - (@i, [@d ..], String | double | int | Binary | @i) - Add value to aggregate. Index must be specified, see b.",
		"gr : remove... - () - Remove value from aggregate. Index must be specified, see b.",
		"gu : unsetAttribute - () - Unset element of aggregate. Index must be specified, see b.",
		"ac : createAggregate - () - Create aggregate for element. Aplicable if element type is aggregate. Index must be specified, see b.",
		sSepartorLine,
	};

	String[] asHelpMember = {
		sHeadLine,
		sSepartorLine,
		"b= : Chosing aggregate member by index. It will be used in aggregate operations.",
		sSepartorLine,
	};

	String[] asHelpSchemaDefinition = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"hi : getName - () - Shows information for this schema definition.",
		"hl : getDeclarations - () - List all entity_definitions and defined_types belonging to this schema.",
		sSepartorLine,
	};

	String[] asHelpSchemaInstance = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"ci : getName - () - Show information for this schema instance.",
		"cl : getAssociatedModels - () - List models belonging to this schema.",
		"cd : getDataDictionary - () - Chosing standart dictionary schema instance.",
		"cm : getDataMapping - () - Chosing standart mapping schema instance.",
		"cn : rename - (name) - Rename schema instance.",
		"ce : delete - () - Delete schema instance.",
		"ca : addSdaiModel - (@m) - Add model to schema.",
		"cr : removeSdaiModel - (@m) - Remove model from schema.",
		sSepartorLine,
	};

	String[] asHelpASchemaInstance = {
		sHeadLine,
		sSepartorLine,
		sObjectLine,
		sSepartorLine,
		"na : add - (@c) - Add SchemaInstance to this aggregate.",
		"nc : clear - () - Clear this aggregate.",
		"nl : get - () - List member schema instances of this aggregate.",
		sSepartorLine,
	};

	String[] asHelpCheck = {
		sHeadLine,
		sSepartorLine,
		".c : Going to check mode where command .!, .* will be available.",
		".n : Going to normal working mode.",
		".! : Checking, will next command ganerate the same output, as it is.",
		".* : Any output for next command will be correct.",
		".i : Show current mode.",
		sSepartorLine,
	};

	String[] asHelpFile = {
		sHeadLine,
		sSepartorLine,
		"fo : Open given file by parameter and execute it interpreting by one line.",
		sSepartorLine,
	};

	String[] asHelp1 = {
		sHeadLine,
		sSepartorLine,
		"  There are stored some remarks which are important to understand base",
		"functionality of SdaiTerm.",
		sSepartorLine,
		"  If any command generate indexed list, this meen that following commands can",
		"use this indexes setting value to self. Need to remember that each another,",
		"listing command will force indexes which was before discard. So there is",
		"possibility to use one index at time. For every object you can assign with",
		"simbol = from listing.",
		sSepartorLine,
		"  In the help you will see symbol @ sometimes passed as parameter. This meen",
		"that here you are using another commands from SdaiTerm following letter means",
		"which instance you must put there.",
		sSepartorLine,
		"  For every type of object you have 10 reserved objects, from 0 to 9. Usage of",
		"it by specifying number on which you are operating. This number always are",
		"following after command leter, like  r2o, m4i, g4l. If you not specify number",
		"by default it operate on 0. Some creating commands directly sore created object",
		"to 0, like rm, mc.",
		sSepartorLine,
		"  If you are specifying String which have space char you must place this String",
		"double quates \"string\". Also you cann't use double quates in any ather cases.",
		sSepartorLine,
		"  Instead of repository name and location can be used to standart keywords:",
		"-\"temp\" can be used just for repository name and meens temporary reposiotry",
		"-\"def\" meens default name or location. This parameter are used if nothing is",
		"specified.",
		sSepartorLine
	};

	void help(String[] as) {
		for (int i=0; i < as.length; i++) {
			out.println(as[i]);
		}
	}

/** Return express name representation at jsdai.*/
	private String sdaiName(String s) {
		String s1 = s.substring(0,1).toUpperCase();
		String s2 = s.substring(1).toLowerCase();
		return s1.concat(s2);
	}

	private void listStrings(String s1, String s2, A_string as) throws Exception {
		if (as != null) {
			SdaiIterator iter = as.createIterator();
			if (as.getMemberCount() > 0) {
				while (iter.next()) {
					String s = as.getCurrentMember(iter);
					out.println(s1 + s);
					s1 = s2;
				}
			} else {
				out.println(s1 + "-");
			}
		} else {
			out.println(s1 + "null");
		}
	}

	String accessType[] = { "NO_ACCESS", "READ_ONLY", "READ_WRITE" };

	private void log(String s) {
//		out.println(s);
	}

	private void run(String[] args) throws Exception {
		CEntity inv;
		boolean fRunning = true;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		SdaiSession.setLogWriter(new PrintWriter(out, true));
		System.out.println(sHeadLine);
		System.out.println(sSepartorLine);
		System.out.println("Press ? for command list.");
		int i = 0;
		while (i < args.length) {
			fRunning = doCommand("fo "+args[i]);
			i++;
		}
		while (fRunning) {
			System.out.print(":");
			String s = "";
			boolean going = true;
			while (going) {
				char c = (char)in.read();
				s += c;
				if (c == '\n') {
					going = false;
					fRunning = doCommand(s);
					s = "";
				}
			}
		};
	}

	private boolean isCommand(String s) {
		if (s.charAt(0) == '@') {
			return true;
		} else {
			return false;
		}
	}

	private Object encodeToken(String s) {
		Object result = null;
		int index = Integer.parseInt(s.substring(2));
		switch (s.charAt(1)) {
		case 'r' :
			result = repositories[index];
			break;
		case 'm' :
			result = models[index];
			break;
		case 'e' :
			result = entityDefs[index];
			break;
		case 'd' :
			result = definedTypes[index];
			break;
		case 'x' :
			result = entityExtents[index];
			break;
		case 'i' :
			result = instances[index];
			break;
		case 'a' :
			result = attributes[index];
			break;
		case 'g' :
			result = aggregates[index];
			break;
		case 'c' :
			result = schemaInstances[index];
			break;
		case 'h' :
			result = schemaDefs[index];
			break;
		case 'n' :
			result = aSchemaInstances[index];
			break;
		default :
			result = null;
			break;
		}
		return result;
	}

	private boolean doCommand(String s) throws Exception {
		boolean fRunning = true;
		try {
		if (echo) {SdaiSession.println(s);}
		boolean fError = false;
		int i1 = 0, i2 = 0, index = 0, array_index1 = 0, array_index2 = -1;
		char c1 = ' ';
		char c2 = ' ';
		if (s.length() >= 1) {
			c1 = s.charAt(0);
		}
		if (s.trim().length() >= 2) {
			int index_pos = 2;
			c2 = s.charAt(1);
			if (c2 >= '0' && c2 <= '9') {
				array_index1 = Integer.parseInt(""+c2);
				c2 = s.charAt(2);
				index_pos++;
			}
			if (c2 == '=') {
				char c3 = s.charAt(index_pos);
				if (!(c3 >= '0' && c3 <= '9')) {
					index_pos++;
					array_index2 = Integer.parseInt(s.substring(index_pos).trim());
				} else {
					index = Integer.parseInt(s.substring(index_pos).trim());
				}
			}
		}
		StringTokenizer st = new StringTokenizer(s);
		Vector vtokens = new Vector();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.charAt(0) == '\"') {
				token = token.substring(1);
				boolean _end = false;
				while (st.hasMoreTokens() && !_end) {
					token += ' '+st.nextToken();
					if (token.charAt(token.length()-1) == '\"') {
						token = token.substring(0, token.length()-2);
						_end = true;
					}
				}
				vtokens.addElement(token);
			} else {
				vtokens.addElement(token);
			}
		}
		String tokens[] = new String[vtokens.size()];
		int i = 0;
		while (i < vtokens.size()) {
			tokens[i] = (String)vtokens.elementAt(i);
			i++;
		}
		SdaiIterator it1, it2;
		ASdaiRepository ar;
		ASdaiModel am;
		ASchemaInstance asi;
		Class clas;
		try {
			switch (c1) {
//Session
			case 's':
				switch (c2) {
				case 'o':
					log("Open Session(out s)");
					session = SdaiSession.openSession();
/*					if (SdaiSession.getLogWriter() == null) {
						SdaiSession.setLogWriter(new PrintWriter((OutputStream)out, true));
					}*/
//					SdaiSession.setLogWriter(null);
					break;
				case 'c':
//					log("Close session(in s)");
					session.closeSession();
					session = null;
					break;
				case 'i':
					Implementation imp = session.getSdaiImplementation();
					out.println("----------- Implementation -------------");
					out.println("                      name: " + imp.getName());
					out.println("                     level: " + imp.getLevel());
					out.println("              sdai_version: " + imp.getSdaiVersion());
					out.println("           binding_version: " + imp.getBindingVersion());
					out.println("      implementation_class: " + imp.getImplementationClass());
					out.println("         transaction_level: " + imp.getTransactionLevel());
					out.println("          expression_level: " + imp.getExpressionLevel());
					out.println("  domain_equivalence_level: " + imp.getDomainEquivalenceLevel());
					break;
				case 'l':
					ar = session.getKnownServers();
					it1 = ar.createIterator();
					i1 = 0;
					while (it1.next()) {
						i1++;
						SdaiRepository r = ar.getCurrentMember(it1);
						String sh; // =	(r==repository)? ": " : "= ";
						if (r == repositories[0]) {
							sh = r.isActive() ? "#" : "=";
						} else {
							sh = r.isActive() ? "+" : ":";
						}
						out.print(i1 + sh + r.getName() + " : ");
						out.println();
						listing[i1] = r;
					}
/*					if (remote_sessions != null) {
						repo_count = i1;
						out.println(server.substring(server.lastIndexOf("@")+1)+" repositories:");
						for (int j = 0; j < remote_sessions.size(); j++) {
							i1++;
							out.println(i1+"~"+remote_sessions.get(j));
						}
					}*/
					break;
				case 'r':
//					log("Create repository(\"" + tokens[1] + "\", \"" + tokens[2] + "\")");
					repositories[0] = session.createRepository(getRepoName(((tokens.length > 1)?tokens[1]:"def")), getRepoLocation(((tokens.length > 2)?tokens[2]:"def")));
					break;
				case 'm':
//					log("Import clear text encoding(\"" + tokens[2] + "\", \"" + tokens[1] + "\", \"" + tokens[3] + "\")");
					repositories[0] = session.importClearTextEncoding(getRepoName(((tokens.length > 2)?tokens[2]:"def")), tokens[1], getRepoLocation(((tokens.length > 3)?tokens[3]:"def")));
					break;
				case 'f':
					if (isNumber(tokens[1]) && (server != "")) {
						repositories[0] = session.linkRepository(remote_sessions.getByIndex(Integer.parseInt(tokens[1])), server);
					} else {
						repositories[0] = session.linkRepository(getRepoName(((tokens.length > 1)?tokens[1]:"def")), getRepoLocation(((tokens.length > 2)?tokens[2]:"def")));
					}
					break;
				case 'b':
					session.linkDataBaseBridge(tokens[1], tokens[2], tokens[3].toCharArray());
				break;
				case 'u':
					session.unlinkDataBaseBridge();
				break;
				case 's':
					if (tokens.length > 1) {
						server = tokens[1];
					}
					remote_sessions = session.remoteRepositories(tokens[1]);
					for (int z = 1; z <= remote_sessions.getMemberCount(); z++) {
						out.println(z+"~"+remote_sessions.getByIndex(z));
					}
/*					int z = 1;
					SdaiIterator it = remote_sessions.createIterator();
					while (it.next()) {
						out.println(z+"~"+remote_sessions.getCurrentMemberObject(it));
					}*/
					break;
				case 'g' :
					if (tokens.length > 0) {
						if (tokens[1].equals("System.out")) {
							SdaiSession.setLogWriter(new PrintWriter(System.out, true));
						} else if (tokens[1].equals("System.err")) {
							SdaiSession.setLogWriter(new PrintWriter(System.err, true));
						} else {
							SdaiSession.setLogWriter(new PrintWriter(new FileOutputStream(new File(tokens[1])), true));
						}
					} else {
						out.println("log writer not specified");
					}
					break;
				case 'w' :
					SdaiSession.println(tokens.toString());
					break;
				default:
					fError = true;
				}
				break;
//Transaction
			case 't':
				switch (c2) {
				case 'i':
					if (transaction != null) {
						int iMode = transaction.getMode();
						out.println("transaction mode = " + accessType[iMode]);
					} else {
						out.println("transaction = null");
					}
					break;
				case 'r':
					log("Start transaction read-only access(in s, out t)");
					transaction = session.startTransactionReadOnlyAccess();
					break;
				case 'w':
					log("Start transaction read-write access(in s, out t)");
					transaction = session.startTransactionReadWriteAccess();
					break;
				case 'c':
					log("Commit(in t)");
					transaction.commit();
					break;
				case 'a':
					log("Abort(in t)");
					transaction.abort();
					break;
				case 'e':
					log("End transaction access and commit(in t)");
					transaction.endTransactionAccessCommit();
					break;
				case 'b':
					log("End transaction access and abort(in t)");
					transaction.endTransactionAccessAbort();
					break;
				default:
					fError = true;
				}
				if (!fError) {
				}
				break;
//Repository
			case 'r':
				ir = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						repositories[ir] = repositories[array_index2];
					} else {
						repositories[ir] = (SdaiRepository)listing[index];
					}
					break;
				case 'o':
					log("Open repository(in s, in r" + ir + ")");
					repositories[ir].openRepository();
					repositories[ir].addSdaiListener(this);
					break;
				case 'c':
					repositories[ir].closeRepository();
					repositories[ir].removeSdaiListener(this);
					break;
				case 'd':
					repositories[ir].deleteRepository();
					break;
				case 'e':
					repositories[ir].exportClearTextEncoding(tokens[1]);
					break;
				case 'x':
					InputStream fromStream = new BufferedInputStream(new FileInputStream(tokens[1]));
					try {
						repositories[ir].importXml(fromStream);
					}
					finally {
						fromStream.close();
					}
					break;
				case 'i':
					out.println("--------------- Repository -------------");
					out.println("                      name: " + repositories[ir].getName());
					out.println("                  location: " + repositories[ir].getLocation());
					listStrings("               description: ",
								"                          : ", repositories[ir].getDescription());
					out.println("                time_stamp: " + repositories[ir].getChangeDate());
					listStrings("                    author: ",
								"                          : ", repositories[ir].getAuthor());
					listStrings("              organization: ",
								"                          : ", repositories[ir].getOrganization());
					out.println("             authorization: " + repositories[ir].getAuthorization());
					out.println("      preprocessor_version: " + repositories[ir].getPreprocessorVersion());
					out.println("        originating_system: " + repositories[ir].getOriginatingSystem());
					out.println("                    status: " + ((repositories[ir].isActive())?"OPEN":"CLOSE"));
					break;
				case 'l':
					am = repositories[ir].getModels();
					it1 = am.createIterator();
					i1 = 0;
					out.println("---models");
					while (it1.next()) {
						i1++;
						SdaiModel m = am.getCurrentMember(it1);
						out.println(i1 + ": " + m.getName());
						listing[i1] = m;
					}
					asi = repositories[ir].getSchemas();
					it1 = asi.createIterator();
					out.println("---schemas");
					while (it1.next()) {
						i1++;
						SchemaInstance si = asi.getCurrentMember(it1);
						out.println(i1 + ": " + si.getName());
						listing[i1] = si;
					}
					break;
				case 'm':
					if (tokens.length >= 2) {
						if (!isCommand(tokens[2])) {
							s = "jsdai.S" + sdaiName(tokens[2]) + ".S" + sdaiName(tokens[2]);
							clas = Class.forName(s);
							models[0] = repositories[ir].createSdaiModel(tokens[1], clas);
						} else {
							models[0] = repositories[ir].createSdaiModel(tokens[1], (ESchema_definition)encodeToken(tokens[2]));
						}
					} else {
						out.println("error: name and schema not given");
					}
					break;
				case 'a':
					SdaiRepository repoSource = session.importClearTextEncoding(null, tokens[1], null);
					Move.moveRepoContents(repoSource, repositories[ir]);
					transaction.endTransactionAccessCommit();
					repoSource.deleteRepository();
					transaction = session.startTransactionReadWriteAccess();
				break;
				case 's':
					if (tokens[2] != null /*&& !s2. length()*/) {
						if (!isCommand(tokens[2])) {
							s = "jsdai.S" + sdaiName(tokens[2]) + ".S" + sdaiName(tokens[2]);
							clas = Class.forName(s);
							schemaInstances[0] = repositories[ir].createSchemaInstance(tokens[1], clas);
						} else {
							schemaInstances[0] = repositories[ir].createSchemaInstance(tokens[1], (ESchema_definition)encodeToken(tokens[2]));
						}
					} else {
						out.println("error: name and schema not given");
					}
					break;
				default:
					fError = true;
				}
				break;
//Model
			case 'm':
				im = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						models[im] = models[array_index2];
					} else {
						models[im] = (SdaiModel)listing[index];
					}
					break;
				case 'n':
//					models[im].renameSdaiModel(tokens[1]);
					break;
				case 'd':
					models[im].deleteSdaiModel();
					break;
				case 'r':
					models[im].startReadOnlyAccess();
					break;
				case 'w':
					models[im].startReadWriteAccess();
					break;
				case 'e':
					models[im].endReadOnlyAccess();
					break;
				case 'f':
					models[im].endReadWriteAccess();
					break;
				case 'p':
					models[im].promoteSdaiModelToRW();
					break;
				case 'o':
					models[im].reduceSdaiModelToRO();
					break;
				case 'c':
					if (tokens[1] != null/* && !s1.isEmpty()*/) {
						if (!isCommand(tokens[1])) {
/*							s = "jsdai.S" + sdaiName(models[im].getUnderlyingSchema().getName(null)) + ".C" + sdaiName(tokens[1]);
							clas = Class.forName(s);
							instances[0] = models[im].createEntityInstance(clas);*/
							instances[0] = models[im].createEntityInstance(SimpleOperations.findEntity_definition(tokens[1], models[im].getUnderlyingSchema()));
						} else {
							instances[0] = models[im].createEntityInstance((EEntity_definition)encodeToken(tokens[1]));
						}
					} else {
						out.println("error: no entity-type given");
					}
					break;
/*				case 'q':
					models[im].query();
					break;*/
				case 'i':
					out.println("                      name: " + models[im].getName());
					out.println("         underlying_schema: "
					  + models[im].getUnderlyingSchemaString());
					out.println("                repository: "
					  + models[im].getRepository().getName());
					out.println("               change_date: "
					  + models[im].getChangeDate());
					out.println("                      mode: " + accessType[models[im].getMode()]);
//						out.println("           associated_with: xxx");
// ASchemaInstance asi = models[im].getAssociated_schemas()
					break;
				case 'l': {
					AEntityExtent aee = models[im].getPopulatedFolders();
					it1 = aee.createIterator();
					i1 = 0;
					while (it1.next()) {
						i1++;
						EntityExtent ee = aee.getCurrentMember(it1);
						EEntity_definition ed = ee.getDefinition();
						out.println(i1 + " folder: " +  ed.getName(null));
						listing[i1] = ee;
					}
					break;
				}
				case 'x': {
					AEntityExtent aee = models[im].getFolders();
					it1 = aee.createIterator();
					i1 = 0;
					while (it1.next()) {
						i1++;
						EntityExtent ee = aee.getCurrentMember(it1);
						EEntity_definition ed = ee.getDefinition();
						out.println(i1 + " folder: " +  ed.getName(null));
						listing[i1] = ed;
					}
					break;
				}
				default:
					fError = true;
				}
				break;
//Entity_extent
			case 'x':
				ix = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						entityExtents[ix] = entityExtents[array_index2];
					} else {
						entityExtents[ix] = (EntityExtent) listing[index];
					}
					break;
				case 'i':
					EEntity_definition ed = entityExtents[ix].getDefinition();
					out.println(" name: "+ed.getName(null));
					out.println("count: "+String.valueOf(entityExtents[ix].getOwnedBy().getInstanceCount(ed)));
					break;
				case 'l':
					Aggregate agg = entityExtents[ix].getInstances();
					it1 = agg.createIterator();
					i1 = 0;
					while (it1.next()) {
						i1++;
						CEntity be = (CEntity) agg.getCurrentMemberObject(it1);
						out.println(be.toString());
					}
					break;
				default:
					fError = true;
				}
				break;
//Entity_definition
			case 'e':
				ie = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						entityDefs[ie] = entityDefs[array_index2];
					} else {
						entityDefs[ie] = (EEntity_definition)listing[index];
					}
					break;
				case 'i':
					out.println(" name: "+entityDefs[ie].getName(null));
					break;
				case 'l':
					attributes_value(entityDefs[ie], null, 1, false);
					break;
				default:
					fError = true;
				}
				break;
//Defined_type
			case 'd':
				id = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						definedTypes[id] = definedTypes[array_index2];
					} else {
						definedTypes[id] = (EDefined_type)listing[index];
					}
					break;
				case 'i':
					out.println(" name: "+definedTypes[id].getName(null));
					break;
				default:
					fError = true;
				}
				break;
//Entity_instance
			case 'i':
				ii = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						instances[ii] = instances[array_index2];
					} else {
						instances[ii] = repositories[ir].getSessionIdentifier("#" + index);
					}
					break;
				case 'i':
					out.println(instances[ii].toString());
					break;
				case 'l':
					attributes_value(instances[ii].getInstanceType(), instances[ii], 1, true);
					break;
				case 'd':
					instances[ii].deleteApplicationInstance();
					break;
				case 'u':
				   AEntity result = new AEntity();
					instances[ii].findEntityInstanceUsers(null, result);
				   out.println(SimpleOperations.getAggregateString(result, false, "/n"));
					break;
				default:
					fError = true;
				}
				break;
//Mapping operation
			case 'p':
				ii = array_index1;
				switch (c2) {
					case 'a': { //findEntityMappings
						AEntity_mapping mappings = new AEntity_mapping();
						instances[ii].findEntityMappings(((ASchemaInstance)encodeToken(tokens[1])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), mappings, EEntity.NO_RESTRICTIONS);
						SdaiIterator mappings_it = mappings.createIterator();
						while (mappings_it.next()) {
							EEntity_mapping mapping = mappings.getCurrentMember(mappings_it);
							out.println(mapping);
						}
						break;
					}
					case 'b': { //testSourceEntity
						AEntity_mapping mappings = instances[ii].testMappedEntity((EEntity_definition)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						if (mappings == null) {
							out.println("No mappings found.");
						} else {
							SdaiIterator mappings_it = mappings.createIterator();
							while (mappings_it.next()) {
								EEntity_mapping mapping = mappings.getCurrentMember(mappings_it);
								out.println(mapping);
							}
						}
						break;
					}
					case 'c': { //testMappedEntity
						boolean result = instances[ii].testMappedEntity((EEntity_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						out.println(String.valueOf(result));
						break;
					}
					case 'd': { //testSourceAttribute
						AGeneric_attribute_mapping mappings = instances[ii].testMappedAttribute((EAttribute)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						SdaiIterator mappings_it = mappings.createIterator();
						while (mappings_it.next()) {
							EGeneric_attribute_mapping mapping = mappings.getCurrentMember(mappings_it);
							out.println(mapping);
						}
						break;
					}
					case 'e': { //testMappedAttribute
						boolean result = instances[ii].testMappedAttribute((EAttribute_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						out.println(String.valueOf(result));
						break;
					}
					case 'f': { //getMappedAttribute
						Object result[] = instances[ii].getMappedAttribute((EAttribute)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						for (int j = 0; j < result.length; j++) {
							out.println(result[j]);
						}
						break;
					}
					case 'g': { //getMappedAttribute
						Object result = instances[ii].getMappedAttribute((EGeneric_attribute_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						out.println(result);
						break;
					}
					case 'i': { //findMappingInstances
						AEntity mappings = models[im].findMappingInstances((EEntity_definition)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						SdaiIterator mappings_it = mappings.createIterator();
						while (mappings_it.next()) {
							EEntity mapping = (EEntity)mappings.getCurrentMemberObject(mappings_it);
							out.println(mapping);
						}
						break;
					}
					case 'k': { //findMappingInstances
						AEntity mappings = models[im].findMappingInstances((EEntity_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), EEntity.NO_RESTRICTIONS);
						SdaiIterator mappings_it = mappings.createIterator();
						while (mappings_it.next()) {
							EEntity mapping = (EEntity)mappings.getCurrentMemberObject(mappings_it);
							out.println(mapping);
						}
						break;
					}
				}
				break;
//Attribute
			case 'a':
				ia = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						attributes[ia] = attributes[array_index2];
					} else {
						attributes[ia] = (EAttribute)listing[index];
					}
					break;
				case 's':
					EDefined_type defs[] = null;
					if (tokens.length > 3) {
						defs = new EDefined_type[tokens.length-3+1];
						for (int j = 2; j < tokens.length-1; j++) {
							defs[j-2] = (EDefined_type)encodeToken(tokens[j]);
						}
						defs[defs.length-1] = null;
					}
					if (isCommand(tokens[tokens.length-1])) {
						tokens[tokens.length-1] = ((EEntity)encodeToken(tokens[tokens.length-1])).getPersistentLabel();
					}
					value_attr(attributes[ia], (EEntity)encodeToken(tokens[1]), "", 2, tokens[tokens.length-1], defs);
					break;
				case 'u':
					value_attr(attributes[ia], (EEntity)encodeToken(tokens[1]), "", 1, "", null);
					break;
				case 'c':
					defs = null;
					if (tokens.length > 2) {
						defs = new EDefined_type[tokens.length-2+1];
						for (int j = 2; j < tokens.length; j++) {
							defs[j-2] = (EDefined_type)encodeToken(tokens[j]);
						}
						defs[defs.length-1] = null;
					}
					value_attr(attributes[ia], (EEntity)encodeToken(tokens[1]), "", 3, "", defs);
					break;
				case 'i':
					out.println(attributes[ia].getName(null)+((tokens.length > 1)?(" = "+value_attr(attributes[ia], (EEntity)encodeToken(tokens[1]), "", 0, "", null)):""));
					break;
				default:
					fError = true;
				} //a: =(nr), s(set), u(unset), c(create), i(info)"
				break;
//Aggregate
			case 'g':
				int old_ig = ig;
				ig = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						aggregates[ig] = aggregates[array_index2];
					} else {
						aggregates[ig] = (Aggregate)SimpleOperations.getAttributeObject(instances[ii], (EAttribute)listing[index]);
					}
					break;
				case '#':
					aggregates[ig] = (Aggregate)aggregates[old_ig].getByIndexObject(member);
					break;
				case 'a':
					EDefined_type defs[] = null;
					if (tokens.length > 3) {
						defs = new EDefined_type[tokens.length-3+1];
						for (int j = 2; j < tokens.length-1; j++) {
							defs[j-2] = (EDefined_type)encodeToken(tokens[j]);
						}
						defs[defs.length-1] = null;
					}
					if (isCommand(tokens[tokens.length-1])) {
						tokens[tokens.length-1] = ((EEntity)encodeToken(tokens[tokens.length-1])).getPersistentLabel();
					}
					SimpleOperations.addElementString((EEntity)encodeToken(tokens[1]), aggregates[ig], member, tokens[tokens.length-1], defs);
					break;
				case 'r':
					SimpleOperations.removeElement(aggregates[ig], member);
					break;
				case 'c':
					defs = null;
					if (tokens.length > 1) {
						defs = new EDefined_type[tokens.length-1+1];
						for (int j = 1; j < tokens.length; j++) {
							defs[j-1] = (EDefined_type)encodeToken(tokens[j]);
						}
						defs[defs.length-1] = null;
					}
					SimpleOperations.createElementAggregate(aggregates[ig], member, defs);
					break;
				case 'l':
//					out.println(SimpleOperations.getAggregateString(aggregates[ig], false, "\n"));
					out.println(aggregates[ig].toString());
					break;
				case 'i':
					out.println(SimpleOperations.getAggregateString(aggregates[ig], false, "\n"));
					break;
				default:
					fError = true;
				}
				break;
//Aggregate_member
			case 'b':
				switch (c2) {
				case '=':
					member = index;
					break;
				default:
					fError = true;
				}
				break;
//Schema_definition
			case 'h':
				ih = array_index1;
				switch (c2) {
				case '=':
					if (array_index2 >= 0) {
						schemaDefs[ih] = schemaDefs[array_index2];
					} else {
						schemaDefs[ih] = ((SdaiModel)listing[index]).getDefinedSchema();
//						schemaDefs[ih] = SimpleOperations.findDefinedSchema((SdaiModel)listing[index]);
						out.println(schemaDefs[ih].getName(null));
					}
					break;
				case '#':
					ASdaiModel tempModels = session.getDataDictionary().getAssociatedModels();
					SdaiIterator temp_it = tempModels.createIterator();
					boolean finish = false;
					while (!finish && temp_it.next()) {
						SdaiModel tempModel = tempModels.getCurrentMember(temp_it);
						if (tempModel.getMode() == SdaiModel.NO_ACCESS) {
							tempModel.startReadOnlyAccess();
						}
						ESchema_definition tempSchema = tempModel.getDefinedSchema();
						if (tempSchema.getName(null).equalsIgnoreCase(tokens[1])) {
							schemaDefs[ih] = tempSchema;
							finish = true;
						}
					}
					break;
				case 'i':
					out.println("--------------- SchemaDefinition   -------------");
					out.println("                        name: " + schemaDefs[ih].getName(null));
					if (schemaDefs[ih].testIdentification(null)) {
						out.println("              identification: " + schemaDefs[ih].getIdentification(null));
					} else {
						out.println("              identification: " + "null");
					}
					break;
				case 'l' :
					SdaiIterator agg_it;
					i = 1;
					AEntity_declaration agg_e = new AEntity_declaration();
					CEntity_declaration.usedinParent_schema(null, schemaDefs[ih], null, agg_e);
					agg_it = agg_e.createIterator();
					out.println("---Entity definitions");
					while (agg_it.next()) {
						ENamed_type named = (ENamed_type)agg_e.getCurrentMember(agg_it).getDefinition(null);
						out.println(String.valueOf(i)+":"+named.getName(null));
						listing[i] = named;
						i++;
					}
					AType_declaration agg_t = new AType_declaration();
					CType_declaration.usedinParent_schema(null, schemaDefs[ih], null, agg_t);
					agg_it = agg_t.createIterator();
					out.println("---Defined types");
					while (agg_it.next()) {
						ENamed_type named = (ENamed_type)agg_t.getCurrentMember(agg_it).getDefinition(null);
						out.println(String.valueOf(i)+":"+named.getName(null));
						listing[i] = named;
						i++;
					}
					break;
				default:
					fError = true;
				}
				break;
//SchemaInstance
			case 'c':
				ic = array_index1;
				switch (c2) {
					case '=':
						if (array_index2 >= 0) {
							schemaInstances[ic] = schemaInstances[array_index2];
						} else {
							schemaInstances[ic] = (SchemaInstance)listing[index];
						}
						break;
					case 'i':
						out.println("--------------- SchemaInstance   -------------");
						out.println("                      name: " + schemaInstances[ic].getName());
						out.println("             native_schema: " + schemaInstances[ic].getNativeSchemaString());
						out.println("                repository: " + schemaInstances[ic].getRepository().getName());
						out.println("               change_date: " + schemaInstances[ic].getChangeDate());
						out.println("           validation_date: " + schemaInstances[ic].getValidationDate());
						out.println("         validation_result: " + schemaInstances[ic].getValidationResult());
						out.println("          validation_level: " + schemaInstances[ic].getValidationLevel());
						break;
					case 'l':
						am = schemaInstances[ic].getAssociatedModels();
						it1 = am.createIterator();
						i1 = 0;
						while (it1.next()) {
							i1++;
							SdaiModel m = am.getCurrentMember(it1);
							out.println(i1 + " model: " +  m.getName());
							listing[i1] = m;
						}
						break;
					case 'd':
						schemaInstances[ic] = session.getDataDictionary();
						break;
					case 'm':
						schemaInstances[ic] = session.getDataMapping();
						break;
					case 'e':
						schemaInstances[ic].delete();
						break;
					case 'a':
//						System.out.println("!!!"+schemaInstances[ic]);
//						System.out.println("!!!"+(SdaiModel)encodeToken(tokens[1]));
						schemaInstances[ic].addSdaiModel((SdaiModel)encodeToken(tokens[1]));
						break;
					case 'r':
						schemaInstances[ic].removeSdaiModel((SdaiModel)encodeToken(tokens[1]));
						break;
					case 'n':
						schemaInstances[ic].rename(tokens[1]);
						break;
					default:
						fError = true;
					}
				break;
//ASchemaInstance
			case 'n':
				in = array_index1;
				switch (c2) {
					case '=':
						if (array_index2 >= 0) {
							aSchemaInstances[in] = aSchemaInstances[array_index2];
						}
						else {
							aSchemaInstances[in] = new ASchemaInstance();
						}
						break;
					case 'a':
						aSchemaInstances[in].addByIndex(aSchemaInstances[in].getMemberCount()+1, (SchemaInstance)encodeToken(tokens[1]));
						break;
					case 'c':
						aSchemaInstances[in].clear();
						break;
					case 'l':
						SdaiIterator iterator = aSchemaInstances[in].createIterator();
						while (iterator.next()) {
							out.println(aSchemaInstances[in].getCurrentMember(iterator).getNativeSchemaString());
						}
						break;
					default:
						fError = true;
					}
				break;
//Command File
			case 'f' :
				switch (c2) {
					case 'o' :
						out.println("Executing "+tokens[1]);
						File file = new File(tokens[1]);
						FileReader reader = new FileReader(file);
						String ss = "";
						int icc;
						while ((icc = reader.read()) != -1) {
							char c = (char)icc;
							ss += c;
							if (c == '\n') {
								System.out.print(":"+ss);
								fRunning = doCommand(ss);
								ss = "";
							}
						}
						reader.close();
						break;
					case 'c' :
						echo = !echo;
						break;
					default :
						fError = true;
						break;
				}
				break;
//Debug
			case '.' :
				switch (c2) {
					case 'c' : //check-mode
						mode = CHECK_MODE;
						capture = new ByteArrayOutputStream();
						out = new PrintStream(capture);
						checkString = "";
						break;
					case 'n' : //normal-mode
						mode = NORMAL_MODE;
						out = System.out;
						break;
					case '!' : //output check
						if ((mode == CHECK_MODE) || (mode == CHECK_OUTPUT_MODE)) {
							mode = CHECK_OUTPUT_MODE;
							checkString += s.substring(3); //.trim()+"\n"
						}
						break;
					case '*' : //output skip
						if (mode == CHECK_MODE) {
							mode = SKIP_OUTPUT_MODE;
							checkString = "";
						}
						break;
					case 'i' : //info
						out.print("Mode = ");
						switch (mode) {
							case NORMAL_MODE :
								out.println("\'NORMAL_MODE\'");
								break;
							case CHECK_MODE :
								out.println("\'CHECK_MODE\'");
								break;
							case CHECK_OUTPUT_MODE :
								out.println("\'CHECK_OUTPUT_MODE\'");
								break;
							case SKIP_OUTPUT_MODE :
								out.println("\'SKIP_OUTPUT_MODE\'");
								break;
						}
						break;
					default :
						fError = true;
						break;
				}
				break;
//Help
			case '?':
				switch (c2) {
				case ' ':
				case '\n':
					help(asHelpMain);
					break;
				case 's':
					help(asHelpSession);
					break;
				case 't':
					help(asHelpTransaction);
					break;
				case 'r':
					help(asHelpRepository);
					break;
				case 'm':
					help(asHelpModel);
					break;
				case 'x':
					help(asHelpExtent);
					break;
				case 'e':
					help(asHelpEntityDef);
					break;
				case 'd':
					help(asHelpDefinedType);
					break;
				case 'i':
					help(asHelpEntity);
					break;
				case 'a':
					help(asHelpAttribute);
					break;
				case 'g':
					help(asHelpAggregate);
					break;
				case 'b':
					help(asHelpMember);
					break;
				case 'h':
					help(asHelpSchemaDefinition);
					break;
				case 'n':
					help(asHelpASchemaInstance);
					break;
				case 'c':
					help(asHelpSchemaInstance);
					break;
				case '.':
					help(asHelpCheck);
					break;
				case 'f':
					help(asHelpFile);
					break;
				case 'p':
					help(asHelpMapping);
					break;
				case '?':
					help(asHelp1);
					break;
				default:
					fError = true;
				}
				break;
//Comments
			case '-': // test only
				break;
//Quit
			case 'q':
/*				if (session != null) {
					session.closeSession();
				}*/
				fRunning = false;
				break;
			default:
				fError = true;
			}
			if (fError) {
				out.println("error: unknown command");
			}
		} catch (Exception e) {
			out.println("error:" + e);
			if (mode == NORMAL_MODE) {
				e.printStackTrace();
			}
		}
		if (c1 != '.') {
			if (mode == CHECK_MODE) {
				if (capture.toString().trim().length() != 0) {
					capture.writeTo(System.out);
					capture.reset();
	/*				System.out.println("Error encountered. Do you want to continue? (y/n)");
					if (System.in.read() != 'y') {
						fRunning = false;
					}*/
				}
			} else if (mode == CHECK_OUTPUT_MODE) {
				if (!capture.toString().equals(checkString)) {
					capture.writeTo(System.out);
				}
				capture.reset();
				mode = CHECK_MODE;
				checkString = "";
			} else if (mode == SKIP_OUTPUT_MODE) {
//				capture.writeTo(System.out);
				capture.reset();
				mode = CHECK_MODE;
			}
		} else {
			if (mode == CHECK_MODE) {
				capture.writeTo(System.out);
				capture.reset();
			}
		}
		} catch (Exception ex) {
			out.println("error: unknown command");
			ex.printStackTrace();
		}
		return fRunning;
	}

/**Prints to System.out attribute(s) value representation in express*/
	private int attributes_value(EEntity_definition def, EEntity inst, int attr_index, boolean isValue) throws SdaiException {
//working on supertypes
		if (def.getComplex(null)) {
			Vector attrs = new Vector();
			LangUtils.findExplicitAttributes(def, attrs);
			for (int i = 0; i < attrs.size(); i++) {
				EAttribute attr = (EAttribute)attrs.elementAt(i);
				out.println("  "+attr_index+" "+attr.getName(null)+((isValue)?" = "+value_attr(attr, inst, "", 0, "", null):""));
				listing[attr_index] = attr;
				attr_index++;
			}
		} else {
			AEntity_definition supertypes = def.getSupertypes(null);
			SdaiIterator it_supertypes = supertypes.createIterator();
			while (it_supertypes.next()) {
				EEntity_definition supertype = supertypes.getCurrentMember(it_supertypes);
				attr_index = attributes_value(supertype, inst, attr_index, isValue);
			}
	//working  on current definition
			AExplicit_attribute attributes = def.getExplicit_attributes(null);
			SdaiIterator it_attributes = attributes.createIterator();
			out.println(def.getName(null));
			while (it_attributes.next()) {
				EAttribute attr = (EAttribute)attributes.getCurrentMember(it_attributes);
				out.println("  "+attr_index+" "+attr.getName(null)+((isValue)?" = "+value_attr(attr, inst, "", 0, "", null):""));
				listing[attr_index] = attr;
				attr_index++;
			}
		}
		return attr_index;
	}

/**Return attribute value representation in express*/
	private String value_attr(EAttribute attr, EEntity inst, String str_item, int uns, String sset, EDefined_type[] defs) throws SdaiException {
		if (attr instanceof EExplicit_attribute) {
			switch (uns) {
				case 0 : //get
					str_item += SimpleOperations.getAttributeString(inst, attr);
					break;
				case 1 : //unset
					SimpleOperations.unsetAttribute(inst, attr);
					break;
				case 2 : //set
					SimpleOperations.setAttributeString(inst, attr, sset, defs);
					str_item += SimpleOperations.getAttributeString(inst, attr);
					break;
				case 3 : //create
					inst.createAggregate(attr, defs);
					str_item += SimpleOperations.getAttributeString(inst, attr);
					break;
			}
		}
		return str_item;
	}

	private boolean isNumber(String s) {
		boolean result = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= '0') && (c <= '9')) {
			} else {
				result = false;
			}
		}
		return result;
	}

//	getRepoName(((tokens.length > 0)?token[1]:"temp"));
	private String getRepoName(String name) {
		String result = "";
		if (name.equalsIgnoreCase("temp")) {
			result = "";
		} else if (name.equals("") || name.equalsIgnoreCase("def")) {
			result = null;
		} else {
			result = name;
		}
		return result;
	}

// getRepoLocation(((tokens.length > 1)?tokens[2]:"def"));
	private String getRepoLocation(String location) {
		String result = "";
		if (location.equalsIgnoreCase("def") || location.equalsIgnoreCase("")) {
			result = null;
		} else {
			result = location;
		}
		return result;
	}

}
