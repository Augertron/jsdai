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
public final class CompareUsefroms {

	private CompareUsefroms() { }

	private static final Set SCHEMA_TYPES = new HashSet(Arrays.asList(new String[] {
			"_MIM", "_ARM", "_XIM"}));

	static Set enumAllUsefroms(ASdaiModel domain, ESchema_definition schema)
		throws SdaiException {

		Set result = new HashSet();

		AUse_from_specification aUsefroms = new AUse_from_specification();
		CUse_from_specification.usedinCurrent_schema(null, schema, domain, aUsefroms);
		for (SdaiIterator i = aUsefroms.createIterator(); i.next();) {
			EUse_from_specification eUsefrom = aUsefroms.getCurrentMember(i);
			EGeneric_schema_definition foreignSchema = eUsefrom.getForeign_schema(null);
			String foreignName = foreignSchema.getName(null);
			result.add(foreignName);
		}

		return result;
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

	public static void main(String[] args)
		throws SdaiException {

		if (args.length != 2) {
			System.out.println("Invalid usage. Should be:");
			System.out.println("  java jsdai.tools.CompareUsefroms schemasType1 schemasType2");
			System.out.println("    schemasType1: the first type of schemas to compare (_MIM, _ARM, _XIM)");
			System.out.println("    schemasType2: the second type of schemas to compare (_MIM, _ARM, _XIM)");
			System.out.println("Note:");
			System.out.println("  Schemas types are case sensitive.");
			System.out.println("Example:");
			System.out.println("  java jsdai.tools.CompareUsefroms _ARM _XIM");
			return;
		}

		final String schemasType1 = args[0];
		if (!SCHEMA_TYPES.contains(schemasType1)) {
			System.out.println("The first schemas type is invalid: " + schemasType1);
			return;
		}

		final String schemasType2 = args[1];
		if (!SCHEMA_TYPES.contains(schemasType2)) {
			System.out.println("The second schemas type is invalid: " + schemasType2);
			return;
		}

		System.out.println("Initializing JSDAI...");
		SdaiRepository repo = getRepository("ExpressCompilerRepo");
		if (repo == null) {
			System.out.println("Unable to init Jsdai.");
			return;
		}

		System.out.println("Activating models...");
		ASdaiModel models = SearchForRedundantUsefroms.activateModels(repo);
		System.out.println("Enumerating schemas...");
		Map schemas = enumSchemas(models);
		System.out.println("Type1: " + schemasType1);
		System.out.println("Type2: " + schemasType2);

		for (Iterator i = schemas.keySet().iterator(); i.hasNext();) {
			String type1SchemaName = (String) i.next();
			if (!type1SchemaName.endsWith(schemasType1)) {
				continue;
			}

			String type2SchemaName = type1SchemaName.replaceAll(schemasType1, schemasType2);
			ESchema_definition type2Schema = (ESchema_definition) schemas.get(type2SchemaName);
			if (type2Schema == null) {
				System.err.println("Unable to find " + schemasType2 + " schema " + type2SchemaName);
				continue;
			}

			ESchema_definition type1Schema = (ESchema_definition) schemas.get(type1SchemaName);
			Set type1Usefroms = enumAllUsefroms(models, type1Schema);
			Set type2Usefroms = enumAllUsefroms(models, type2Schema);

			// check type1 -> type2
			for (Iterator j = type1Usefroms.iterator(); j.hasNext();) {
				String type1Usefrom = (String) j.next();
				if (!type1Usefrom.endsWith(schemasType1)) {
					continue;
				}

				String type2Usefrom = type1Usefrom.replaceAll(schemasType1, schemasType2);
				if (!type2Usefroms.contains(type2Usefrom)) {
					System.err.println(type2SchemaName + ": the usefrom " + type2Usefrom + " was not found.");
				}
			}

			// check type2 -> type1
			for (Iterator j = type2Usefroms.iterator(); j.hasNext();) {
				String type2Usefrom = (String) j.next();
				if (!type2Usefrom.endsWith(schemasType2)) {
					continue;
				}

				String type1Usefrom = type2Usefrom.replaceAll(schemasType2, schemasType1);
				if (!type1Usefroms.contains(type1Usefrom)) {
					System.err.println(type1SchemaName + ": the usefrom " + type1Usefrom + " was not found.");
				}
			}
		}

		// finish jsdai session
		SdaiSession.getSession().closeSession();
	}
}
