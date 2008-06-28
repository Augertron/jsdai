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

import java.io.*;
import java.util.*;
import java.util.regex.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

/**
 * @author Viktoras Kovaliovas
 */
public final class CompareAttributes {

	private CompareAttributes() { }

	public static final Object SCHEMA_KEY = new Object();

	public static Map enumEntities(Set schemaNames, Map schemas)
		throws SdaiException {

		Map entities = new HashMap();
		for (Iterator i = schemaNames.iterator(); i.hasNext();) {
			String schemaName = (String) i.next();
			ESchema_definition schema = (ESchema_definition) schemas.get(schemaName);
			entities.putAll(enumEntities(schema));
		}

		return entities;
	}

	private static Map enumEntities(ESchema_definition schema)
		throws SdaiException {

		Map entities = new HashMap();

		SdaiModel model = schema.findEntityInstanceSdaiModel();
		AEntity instances = model.getInstances(CEntity_declaration.definition);
		for (SdaiIterator i = instances.createIterator(); i.next();) {
			EEntity_declaration eEd = (EEntity_declaration) instances.getCurrentMemberEntity(i);
			if (!(eEd instanceof ELocal_declaration)) {
				continue;
			}

			EEntity_definition eDef = (EEntity_definition) eEd.getDefinition(null);
			eDef.setTemp(SCHEMA_KEY, schema);
			entities.put(eDef.getName(null), eDef);
		}

		return entities;
	}

	private static void enumReferencedSchemas(ASdaiModel domain, String rootSchema, Map schemas, Set result)
		throws SdaiException {

		ESchema_definition eSchema = (ESchema_definition) schemas.get(rootSchema);
		Set usefroms = CompareUsefroms.enumAllUsefroms(domain, eSchema);
		for (Iterator i = usefroms.iterator(); i.hasNext();) {
			String usefrom = (String) i.next();
			if (result.contains(usefrom)) {
				continue;
			}

			result.add(usefrom);
			enumReferencedSchemas(domain, usefrom, schemas, result);
		}
	}

	private static EAttribute getRedeclaring(EAttribute eAttr)
		throws SdaiException {

		EAttribute eRd = null;
		if (eAttr instanceof EDerived_attribute) {
			EDerived_attribute eDer = (EDerived_attribute) eAttr;
			if (eDer.testRedeclaring(null)) {
				eRd = (EAttribute) eDer.getRedeclaring(null);
			}
		} else if (eAttr instanceof EExplicit_attribute) {
			EExplicit_attribute eExp = (EExplicit_attribute) eAttr;
			if (eExp.testRedeclaring(null)) {
				eRd = eExp.getRedeclaring(null);
			}
		} else if (eAttr instanceof EInverse_attribute) {
			EInverse_attribute eInv = (EInverse_attribute) eAttr;
			if (eInv.testRedeclaring(null)) {
				eRd = eInv.getRedeclaring(null);
			}
		}

		return eRd;
	}

	static Map getAttributes(ASdaiModel domain, EEntity_definition eEd, Set redeclaredAttrs)
		throws SdaiException {

		// get redeclared attributes of entry entity
		AAttribute aAttr = eEd.getAttributes(null, domain);
		for (SdaiIterator i = aAttr.createIterator(); i.next();) {
			EAttribute eAttr = aAttr.getCurrentMember(i);
			EAttribute eRd = getRedeclaring(eAttr);
			if (eRd != null) {
				redeclaredAttrs.add(eRd);
			}
		}

		// get explicit attributes of entry entity
		Map explAttr = new HashMap();
		for (SdaiIterator i = aAttr.createIterator(); i.next();) {
			EAttribute eAttr = aAttr.getCurrentMember(i);
			if (!redeclaredAttrs.contains(eAttr)) {
				explAttr.put(eAttr.getName(null), eAttr);
			}
		}

		// get explicit attributes of supertypes
		AEntity_definition aSuper = getSupertypes(eEd);
		for (SdaiIterator i = aSuper.createIterator(); i.next();) {
			EEntity_definition eSuper = aSuper.getCurrentMember(i);
			explAttr.putAll(getAttributes(domain, eSuper, redeclaredAttrs));
		}

		return explAttr;
	}

	public static AEntity_definition getSupertypes(EEntity_definition eEd)
		throws SdaiException {

		AEntity aGenSupertypes = eEd.getGeneric_supertypes(null);
		AEntity_definition aSupertypes = new AEntity_definition();
		for (SdaiIterator i = aGenSupertypes.createIterator(); i.next();) {
			EEntity eGen = aGenSupertypes.getCurrentMemberEntity(i);
			if (eGen instanceof EEntity_definition) {
				aSupertypes.addUnordered(eGen);
			}
		}

		return aSupertypes;
	}

	private static boolean skip(EEntity_definition ximEd)
		throws SdaiException {

		AEntity_definition ximSupertypes = getSupertypes(ximEd);
		if (ximSupertypes.getMemberCount() == 0) {
			System.out.println("XIM entity has no supertypes: " + ximEd.getName(null));
			return true;
		}

		boolean hasDirectXim = false;
		for (SdaiIterator j = ximSupertypes.createIterator(); j.next();) {
			EEntity_definition eSuper = ximSupertypes.getCurrentMember(j);
			ESchema_definition eSchema = (ESchema_definition) eSuper.getTemp(SCHEMA_KEY);
			if (eSchema.getName(null).endsWith("_XIM")) {
				hasDirectXim = true;
				break;
			}
		}

		return hasDirectXim;
	}

	private static EAttribute getRootAttribute(EAttribute eAttr)
		throws SdaiException {

		EAttribute eRd = getRedeclaring(eAttr);
		if (eRd != null) {
			return getRootAttribute(eRd);
		}

		return eAttr;
	}

	private static Map getIgnoreMap(String ignoreFile) {
		Map ignore = new HashMap();

		Pattern p = Pattern.compile("(\\w+)[.](\\w+)");

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(ignoreFile));
			String s;
			while ((s = reader.readLine()) != null) {
				s = s.trim();
				if (s.length() == 0) {
					continue;
				}

				Matcher m = p.matcher(s);
				if (m.find()) {
					String entity = m.group(1);
					String attr = m.group(2);

					Set set = (Set) ignore.get(entity);
					if (set == null) {
						set = new HashSet();
						ignore.put(entity, set);
					}

					set.add(attr);
				} else {
					System.err.println("Wrong format in ignore file: " + s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ignore;
	}

	private static void printUsage() {
		System.out.println("Invalid usage. Should be:");
		System.out.println("  java jsdai.tools.CompareAttributes ignoreFile armSchema ximSchema [-s]");
		System.out.println("    ignoreFile - file containing list of ARM attributes to ignore.");
		System.out.println("    armSchema - the arm root schema to take entities for comparison from.");
		System.out.println("    ximSchema - the xim root schema to take entities for comparison from.");
		System.out.println("    -s - flag used to specify that derive messages should be skipped.");
		System.out.println("Note:");
		System.out.println("  Schema names are case sensitive.");
		System.out.println("Example:");
		System.out.println("  java jsdai.tools.CompareAttributes ignore.txt ap210_arm ap210_xim");
	}

	public static void main(String[] args)
		throws SdaiException {

		if (args.length != 3 && args.length != 4) {
			printUsage();
			return;
		}

		String ignoreFile = args[0];
		String armSchemaName = args[1];
		String ximSchemaName = args[2];
		boolean skipDerived = false;
		if (args.length == 4) {
			skipDerived = true;
		}

		String repoName = "ExpressCompilerRepo";

		System.out.println("Initializing JSDAI...");
		SdaiRepository repo = CompareUsefroms.getRepository(repoName);
		if (repo == null) {
			System.err.println("Unable to open repo: " + repoName);
			return;
		}

		// activate all models
		ASdaiModel models = SearchForRedundantUsefroms.activateModels(repo);

		// enumerate all schemas
		Map schemas = CompareUsefroms.enumSchemas(models);

		if (!schemas.containsKey(armSchemaName)) {
			System.err.println("Specified ARM schema was not found: " + armSchemaName);
			return;
		}

		if (!schemas.containsKey(ximSchemaName)) {
			System.err.println("Specified XIM schema was not found: " + ximSchemaName);
			return;
		}

		// get arm schemas and entities
		System.out.println("Retrieving ARM schemas...");
		Set armSchemas = new HashSet();
		enumReferencedSchemas(models, armSchemaName, schemas, armSchemas);

		System.out.println("Retrieving ARM entities...");
		Map armEntities = enumEntities(armSchemas, schemas);

		// get xim schemas
		System.out.println("Retrieving XIM schemas...");
		Set ximSchemas = new HashSet();
		enumReferencedSchemas(models, ximSchemaName, schemas, ximSchemas);

		System.out.println("Retrieving XIM entities...");
		Map ximEntities = enumEntities(ximSchemas, schemas);

		// work
		Map ignoreMap = getIgnoreMap(ignoreFile);

		Pattern armxPattern = Pattern.compile("(\\w+)_armx");
		Pattern doubleUndescorePattern = Pattern.compile("(\\w+)__\\w+");
		for (Iterator i = ximEntities.keySet().iterator(); i.hasNext();) {
			String ximEntity = (String) i.next();
			if (ximEntity.indexOf("__") >= 0) {
				continue;
			}

			EEntity_definition ximEd = (EEntity_definition) ximEntities.get(ximEntity);

			// skip non xim entities
			ESchema_definition ximSchema = (ESchema_definition) ximEd.getTemp(SCHEMA_KEY);
			if (!ximSchema.getName(null).endsWith("_XIM")) {
				continue;
			}

			// get corresponding arm entity
			Matcher m = armxPattern.matcher(ximEntity);
			String armEntity;
			if (m.find()) {
				armEntity = m.group(1);
			} else {
				m = doubleUndescorePattern.matcher(ximEntity);
				if (m.find()) {
					armEntity = m.group(1);
				} else {
					armEntity = ximEntity;
				}
			}

			EEntity_definition armEd = (EEntity_definition) armEntities.get(armEntity);
			if (armEd == null) {
				System.err.println("Unable to find ARM entity for XIM entity: " + ximEntity);
				continue;
			}

			// enum xim and arm attributes
			Map ximAttrs = getAttributes(models, ximEd, new HashSet());
			Map armAttrs = getAttributes(models, armEd, new HashSet());

			// check if xim has all attributes available in arm
			for (Iterator j = armAttrs.keySet().iterator(); j.hasNext();) {
				String armAttr = (String) j.next();
				EAttribute eAttr = (EAttribute) armAttrs.get(armAttr);
				eAttr = getRootAttribute(eAttr);
				String parent = eAttr.getParent(null).getName(null);
				Set ignore = (Set) ignoreMap.get(parent);
				if (ignore != null && ignore.contains(eAttr.getName(null))) {
					continue;
				}

				if (!ximAttrs.containsKey(armAttr) && !ximAttrs.containsKey(armAttr + "_x")) {
					System.out.println("XIM entity " + ximEntity + " has no attribute " + armAttr);
				}
			}

			// we need to hide attributes only in entities
			// that have no direct xim supertypes
			if (skip(ximEd)) {
				continue;
			}

			// print XIM attributes that do not come from ARM
			boolean displayHead = true;
			for (Iterator j = ximAttrs.keySet().iterator(); j.hasNext();) {
				String ximAttr = (String) j.next();
				String name = ximAttr;
				if (name.endsWith("_x")) {
					name = name.substring(0, name.length() - 2);
				}

				if (!armAttrs.containsKey(name)) {
					EAttribute eXimAttr = (EAttribute) ximAttrs.get(ximAttr);
					EEntity_or_view_definition eParent = eXimAttr.getParent(null);

					if (eXimAttr instanceof EDerived_attribute) {
						continue;
					}

					String out = "";
					if (eParent != ximEd) {
						if (skipDerived) {
							continue;
						}

						if (displayHead) {
							System.out.println("============ Entity " + ximSchema.getName(null) + "." + ximEntity);
							displayHead = false;
						}

						out = "SELF\\" + eParent.getName(null) + "." + ximAttr + " : ";

						if (eXimAttr instanceof EExplicit_attribute) {
							EEntity eDomain = ((EExplicit_attribute) eXimAttr).getDomain(null);
							if (eDomain instanceof ENamed_type) {
								out += ((ENamed_type) eDomain).getName(null);
							}
						}

						out += " := ?;";
					} else {
						out = "Unexpected attribute " + ximEntity + "." + ximAttr;
					}

					System.out.println(out);
				}
			}
		}

		System.out.println("Done.");
	}
}
