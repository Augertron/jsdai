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

package jsdai.tools;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

/**
 * @author Viktoras Kovaliovas
 */
public class SearchForRedundantUsefroms {
	
	public static SdaiRepository getSystemRepository()
		throws SdaiException {

		// init sdai
		SdaiSession session = SdaiSession.getSession();
		if (session == null)
			session = SdaiSession.openSession();

		session.startTransactionReadWriteAccess();
		return session.getSystemRepository();		
	}

	public static SdaiRepository getRepository(String repoName)
		throws SdaiException {
		
		// init sdai
		SdaiSession session = SdaiSession.openSession();
		session.setSdaiContext(new SdaiContext());
		session.startTransactionReadWriteAccess();
		ASdaiRepository arep = session.getKnownServers();
		SdaiRepository repo = null;
		for (SdaiIterator i = arep.createIterator(); i.next();) {
			repo = arep.getCurrentMember(i);
			if (repo.getName().equals(repoName)) {
				repo.openRepository();
				break;
			}
		}
		
		return repo;
	}
	
	public static ASdaiModel activateModels(SdaiRepository repo)
		throws SdaiException {
		
		ASdaiModel models = repo.getModels();
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			try {
				if (model.getMode() == SdaiModel.NO_ACCESS) {
					model.startReadOnlyAccess();
				}
			}
			catch (SdaiException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		return models;
	}
	
	public static Map enumSchemas(ASdaiModel models)
		throws SdaiException {
		
		AEntity aSchemas = models.getInstances(ESchema_definition.class);
		Map schemas = new HashMap();
		for (SdaiIterator i = aSchemas.createIterator(); i.next();) {
			ESchema_definition schema = (ESchema_definition) aSchemas.getCurrentMemberEntity(i);
			String schemaName = schema.getName(null);
			
			schemas.put(schemaName, schema);
		}

		return schemas;
	}
	
	private static Map findRedundantUsefroms(SdaiRepository repo, String[] roots,
			boolean includeReferenceFroms, boolean includePartialInterfaceSpec)
		throws SdaiException {
		
		Map redUsefroms = new HashMap();
		
		// sctivate all models
		ASdaiModel models = activateModels(repo);
		
		// enumerate all schemas
		Map schemas = enumSchemas(models);

		// find redundant usefroms
		for (int i = 0; i < roots.length; i++) {
			ESchema_definition schema = (ESchema_definition) schemas.get(roots[i]);
			if (schema == null) {
				System.err.println("Schema not found: " + roots[i]);
				continue;
			}

			Map cashe = new HashMap();			
			getUsefroms(models, schema, includeReferenceFroms, includePartialInterfaceSpec, cashe, redUsefroms);
		}
		
		return redUsefroms;
	}
	
	private static Set getUsefroms(
			ASdaiModel domain,
			EGeneric_schema_definition schema,
			boolean includeReferenceFroms,
			boolean includePartialInterfaceSpec,
			Map usefromCashe,
			Map redUsefroms)
		throws SdaiException {
		
		String schemaName = schema.getName(null);
		
		// check cashe. if this schema is present in cashe it means we have a cycle
		Set allUsefroms = (Set) usefromCashe.get(schemaName);
		if (allUsefroms != null)
			return allUsefroms;

		// save usefroms to cashe, so we would not get infinite recursion
		// then cycles appear. this set shall be filled on the exit, because this 
		// solves problems then there is cycle, we get, that all interfaced schemas
		// are redundant
		allUsefroms = new HashSet();
		usefromCashe.put(schema.getName(null), allUsefroms);
		
		// gather usefroms refenrenced directly and indirectly 
		Set myUsefroms = new HashSet();
		Set refUsefroms = new HashSet();

		AInterface_specification aInterfaces = new AInterface_specification();
		CUse_from_specification.usedinCurrent_schema(null, schema, domain, aInterfaces);
		if (includeReferenceFroms) {
			CReference_from_specification.usedinCurrent_schema(null, schema, domain, aInterfaces);
		}
		for (SdaiIterator i = aInterfaces.createIterator(); i.next();) {
			EInterface_specification eInterface = aInterfaces.getCurrentMember(i);
			if (!includePartialInterfaceSpec) {
				if (eInterface instanceof EUse_from_specification) {
					if (((EUse_from_specification) eInterface).testItems(null)) {
						continue;
					}
				} else {
					if (((EReference_from_specification) eInterface).testItems(null)) {
						continue;
					}
				}
			}
			EGeneric_schema_definition foreignSchema = eInterface.getForeign_schema(null);
			String foreignName = foreignSchema.getName(null);
			if (!myUsefroms.contains(foreignName)) {
				myUsefroms.add(foreignName);

				Set usefroms = getUsefroms(domain, foreignSchema, includeReferenceFroms,
					includePartialInterfaceSpec, usefromCashe, redUsefroms);
				refUsefroms.addAll(usefroms);
			}
		}
		
		// mark redundant usefroms
		Set reds = (Set) redUsefroms.get(schemaName);
		for (Iterator i = myUsefroms.iterator(); i.hasNext();) {
			String usefrom = (String) i.next();
			if (refUsefroms.contains(usefrom)) {
				if (reds == null) {
					reds = new HashSet();
					redUsefroms.put(schemaName, reds);
				}
				
				reds.add(usefrom);
			}
		}
		
		// add all usefroms referenced indirectly. allUsefroms should be used here
		// because it is put into cashe
		allUsefroms.addAll(myUsefroms);
		allUsefroms.addAll(refUsefroms);

		return allUsefroms;
	}
	
	private static Set mult(Set s1, Set s2) {
		Set res = new HashSet();
		if (s2.size() < s1.size()) {
			Set s = s1;
			s1 = s2;
			s2 = s;
		}
		
		for (Iterator i = s1.iterator(); i.hasNext();) {
			String s = (String) i.next();
			if (s2.contains(s))
				res.add(s);
		}
		
		return res;
	}

	private static void updateReds(Map redUsefroms, List cycles) {
		// filter red usefroms and mark according to cycles
		Set removeKeys = new HashSet();
		for (Iterator i = redUsefroms.keySet().iterator(); i.hasNext();) {
			String schema = (String) i.next();
			Set usefroms = (Set) redUsefroms.get(schema);

			Set removeRed = new HashSet();
			Set markRed = new HashSet();
			List marks = new LinkedList();
			for (Iterator j = cycles.iterator(); j.hasNext();) {
				List cycle = (List) j.next();

				Set m = mult(usefroms, new HashSet(cycle));
				int n = m.size();
				if (n == 0)
					continue;
				
				if (n == 1 && !markRed.contains(m.iterator().next()))
					removeRed.addAll(m);
				else {
					removeRed.removeAll(m);
					markRed.addAll(m);
					marks.add(m);
				}				
			}
			
			// remove usefroms
			usefroms.removeAll(removeRed);
			if (usefroms.size() == 0)
				removeKeys.add(schema);
			
			// mark usefroms
			for (Iterator j = markRed.iterator(); j.hasNext();) {
				String usefrom = (String) j.next();
				String newString = usefrom;
				for (ListIterator k = marks.listIterator(); k.hasNext();) {
					Set markTargets = (Set) k.next();
					if (markTargets.contains(usefrom))
						newString = "[" + k.nextIndex() + "] " + newString;
				}
				
				if (usefrom.length() != newString.length()) {
					usefroms.remove(usefrom);
					usefroms.add(newString);
				}
			}
			
		}
		
		for (Iterator i = removeKeys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			redUsefroms.remove(key);
		}
	}
	
	/**
	 * Returns redundant interface specifications for schemas reachable
	 * from specified root schemas (case sensitive) in specified repository.
	 * 
	 * @param repo repository containing dictionary data.
	 * @param rootSchemas root schemas' names.
	 * @param includeReferenceFroms if set to <code>true</code>, then
	 *   <code>reference_from_specifications</code> will be used together with
	 *   <code>use_from_specifications</code>. Otherwise only <code>use_from_specifications</code>
	 *   will be used.
	 * @param includePartialInterfaceSpec if set to <code>true</code>, then
	 *   partial interface specifications will be used, otherwise only complete
	 *   interface specifications will be used.
	 *   
	 * @return <code>Map</code> containing as keys schema names (<code>String</code>),
	 * that have redundant interface specifications and as values containing
	 * <code>Collection</code> of redundant interface specifications (<code>String</code>).
	 * 
	 * @throws SdaiException
	 */
	public static Map getRedundantInterfaceSpecifications(SdaiRepository repo,
		String[] rootSchemas, boolean includeReferenceFroms,
			boolean includePartialInterfaceSpec) throws SdaiException {
		
		Map redUsefroms = findRedundantUsefroms(repo, rootSchemas,
			includeReferenceFroms, includePartialInterfaceSpec);
		List cycles = SearchForCycles.getCycles(repo, rootSchemas,
			includeReferenceFroms, includePartialInterfaceSpec);
		updateReds(redUsefroms, cycles);
		return redUsefroms;
	}
	
	public static void main(String[] args)
		throws SdaiException {
		
		boolean includeReferenceFrom = false;
		boolean includePartialInterfaceSpec = true;
		List rootSchemas = new ArrayList();
		String repoName = "ExpressCompilerRepo";
		
		// non-strict arguments parsing
		for (int i = 0, n = args.length; i < n; i++) {
			String arg = args[i];
			if (arg.equals("-r")) {
				includeReferenceFrom = true;
			} else if (arg.equals("-p")) {
				includePartialInterfaceSpec = false;
			} else if (arg.equals("-repo")) {
				i++;
				if (i < n) {
					repoName = args[i];
				} else {
					printUsage();
					return;
				}
			} else {
				rootSchemas.add(arg);
			}
		}
		
		if (rootSchemas.size() == 0) {
			printUsage();
			return;
		}

		System.out.println("Initializing JSDAI...");
		SdaiRepository repo = getRepository(repoName);
		if (repo == null) {
			System.out.println("Repository not found: " + repoName);
			return;
		}
		
		System.out.println("Searching for redundant usefroms...");
		Map redUsefroms = getRedundantInterfaceSpecifications(repo, args, includeReferenceFrom, includePartialInterfaceSpec);		

		// print output
		if (redUsefroms.size() > 0) {
			System.out.println("List of redundant usefroms:");
			for (Iterator i = redUsefroms.keySet().iterator(); i.hasNext();) {
				String schema = (String) i.next();
				Set usefroms = (Set) redUsefroms.get(schema);
				
				System.out.println("=== SCHEMA: " + schema);
				for (Iterator j = usefroms.iterator(); j.hasNext();) {
					String usefrom = (String) j.next();
					System.out.println("USEFROM: " + usefrom);
				}
			}
		}
		else
			System.out.println("No redundant usefroms found.");

		// finish jsdai session
		SdaiSession.getSession().closeSession();
		
		System.out.println("Done.");
	}

	private static void printUsage() {
		System.out.println("Invalid usage. Should be:");
		System.out.println("  java jsdai.tools.SearchForRedundantUsefroms [-r] [-p] [-repo repoName] schema1 [schema2 [schema3] ...]");
		System.out.println("-r use reference_from_specifications.");
		System.out.println("-p skip partial interface specifications.");
		System.out.println("-repo case sensitive name of repository containing dictionary data. Default ExpressCompilerRepo.");
		System.out.println("Note:");
		System.out.println("  Schema names are case sensitive.");
		System.out.println("Example:");
		System.out.println("  java jsdai.tools.SearchForRedundantUsefroms ap210_arm ap212_arm");
	}
}
