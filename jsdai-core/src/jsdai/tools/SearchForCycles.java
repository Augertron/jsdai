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
public class SearchForCycles {

	private static List enumCycles(Map graph, String n, String m, Set marked) {
		List res = new LinkedList();
		marked.add(n);

		// goto adjacent verteces
		Set adjacentVer = (Set) graph.get(n);
		for (Iterator i = adjacentVer.iterator(); i.hasNext();) {
			String adj = (String) i.next();
			if (marked.contains(adj))
				continue;
			
			List r = enumCycles(graph, adj, m, marked);
			if (r != null) {
				for (Iterator j = r.iterator(); j.hasNext();) {
					List cycle = (List) j.next();
					cycle.add(n);
					
					res.add(cycle);
				}
			}
		}

		// check if this vertex has target as adjacent vertex
		if (adjacentVer.contains(m)) {
			List cycle = new LinkedList();
			cycle.add(n);
			
			res.add(cycle);
		}
		
		return res.size() > 0 ? res : null;
	}

	static Set enumAllUsefroms(ASdaiModel domain, ESchema_definition schema,
		boolean includeReferenceFroms, boolean includePartialInterfaceSpec) throws SdaiException {
		
		Set result = new HashSet();

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
			EInterface_specification eUsefrom = aInterfaces.getCurrentMember(i);
			EGeneric_schema_definition foreignSchema = eUsefrom.getForeign_schema(null);
			String foreignName = foreignSchema.getName(null);
			result.add(foreignName);
		}
		
		return result;
	}
	
	private static Map enumUsefroms(SdaiRepository repo, String[] roots,
		boolean includeReferenceFroms, boolean includePartialInterfaceSpec) throws SdaiException {
		
		// activate all models
		ASdaiModel models = SearchForRedundantUsefroms.activateModels(repo);
		
		// enumerate all schemas
		Map schemas = SearchForRedundantUsefroms.enumSchemas(models);

		// enum usefroms
		Map usefroms = new HashMap();
		for (int i = 0; i < roots.length; i++)
			enumUsefroms(models, schemas, usefroms, roots[i], includeReferenceFroms, includePartialInterfaceSpec);
		
		return usefroms;
	}
	
	private static void enumUsefroms(ASdaiModel domain, Map schemas, Map usefroms,
		String schemaName, boolean includeReferenceFroms, boolean includePartialInterfaceSpec) throws SdaiException {

		if (usefroms.containsKey(schemaName))
			return;
		
		ESchema_definition schema = (ESchema_definition) schemas.get(schemaName);
		if (schema == null) {
			System.err.println("Schema not found: " + schemaName);
			return;
		}

		Set myUsefroms = enumAllUsefroms(domain, schema, includeReferenceFroms, includePartialInterfaceSpec);
		usefroms.put(schemaName, myUsefroms);
		
		for (Iterator i = myUsefroms.iterator(); i.hasNext();) {
			String s = (String) i.next();
			enumUsefroms(domain, schemas, usefroms, s, includeReferenceFroms, includePartialInterfaceSpec);
		}
	}

	public static List getCycles(SdaiRepository repo, String[] roots, boolean includeReferenceFroms,
			boolean includePartialInterfaceSpec) throws SdaiException {
		
		List allCycles = new LinkedList();
		Map graph = enumUsefroms(repo, roots, includeReferenceFroms, includePartialInterfaceSpec);
		
		// look for cycles
		Set marked = new HashSet();
		for (Iterator i = graph.keySet().iterator(); i.hasNext();) {
			String schema = (String) i.next();

			Set m = new HashSet(marked);
			List cycles = enumCycles(graph, schema, schema, m);
			if (cycles != null)
				allCycles.addAll(cycles);
			
			marked.add(schema);
		}
		
		return allCycles;
	}
	
	
	public static void main(String[] args)
		throws SdaiException {

		if (args.length == 0) {
			System.out.println("Invalid usage. Should be:");
			System.out.println("  java jsdai.tools.SearchForCycles schema1 [schema2 [schema3] ...]");
			System.out.println("Note:");
			System.out.println("  Schema names are case sensitive.");
			System.out.println("Example:");
			System.out.println("  java jsdai.tools.SearchForCycles ap210_arm ap212_arm");
			return;
		}
		
		System.out.println("Initializing JSDAI...");
		SdaiRepository repo = SearchForRedundantUsefroms.getRepository("ExpressCompilerRepo");

		List cycles = getCycles(repo, args, false, true);
		
		int cycleNr = 1;
		for (Iterator j = cycles.iterator(); j.hasNext();) {
			List cycle = (List) j.next();
			System.out.println("Cycle number: " + cycleNr++);
			for (Iterator k = cycle.iterator(); k.hasNext();) {
				String s = (String) k.next();
				System.out.println(s);
			}
		}
		
		// finish jsdai session
		SdaiSession.getSession().closeSession();
		
		System.out.println("Done.");		
	}
}
