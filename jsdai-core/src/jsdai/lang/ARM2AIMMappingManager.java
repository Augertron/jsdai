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

import java.util.*;
import java.io.*;

import jsdai.dictionary.*;
import jsdai.mapping.*;

class ARM2AIMMappingManager {
    protected static HashMap arm2aimMap = new HashMap();
	protected static HashMap aimReuseFlagsMap = new HashMap();
	protected static SdaiContext context;

	public final static int REUSE_GLOBALLY = 0;
	public final static int REUSE_LOCALLY = 1;
	public final static int REUSE_OVERWRITE = 2;

	public final static String REUSE_GLOBALLY_STR = "REUSE_GLOBALLY";
	public final static String REUSE_LOCALLY_STR = "REUSE_LOCALLY";
	public final static String REUSE_OVERWRITE_STR = "REUSE_OVERWRITE";

	private ARM2AIMMappingManager() { }

	protected static HashMap getPackageMap(EMappedARMEntity armEntity) throws SdaiException {
		Class armClass = armEntity.getClass();
		String className = armClass.getName();
		int dotPos = className.lastIndexOf('.');

		String packageName = className.substring(0, dotPos);

		HashMap pckgMap = (HashMap) arm2aimMap.get(packageName);
		if (pckgMap == null) {
			pckgMap = new HashMap();
			// read schema/arm2aim_mapping.properties file
			try {
				Properties props = new Properties();
				InputStream istream = armClass.getResourceAsStream("arm2aim_mapping.properties");
				if (istream != null) {
					props.load(istream);
					Enumeration prop_elems = props.propertyNames();
					while (prop_elems.hasMoreElements()) {
						String prop_name = (String) prop_elems.nextElement();
						String aimClassStr = props.getProperty(prop_name);
						String armClassStr = packageName + "." + prop_name;

						EMappedARMEntity armEntityEntry =
							(EMappedARMEntity) Class.forName(armClassStr, true,
									SdaiClassLoaderProvider.getDefault().getClassLoader()).newInstance();
						Object armDefinition = armEntityEntry.getInstanceType();
						EEntity aimEntityEntry =
							(EEntity) Class.forName(aimClassStr, true,
									SdaiClassLoaderProvider.getDefault().getClassLoader()).newInstance();
						Object aimDefinition = aimEntityEntry.getInstanceType();

						AEntity_mapping mappings = new AEntity_mapping();
						int mappingsCount = armEntityEntry.findARMEntityMappings(context, mappings, EEntity.MOST_SPECIFC_ENTITY);

                        EEntity_definition armDef = (EEntity_definition) armDefinition;

						// check the DIRECT flag for mapping ant get one - by now it takes only most generic entity mapping
						if (mappingsCount < 1) {
                            throw new SdaiException(SdaiException.VA_NVLD,"Entity [" + armEntityEntry.getClass() + "] has no DIRECT mapping.");
						}

						boolean found = false;
						for (int i = 1; i <= mappingsCount; i++) {
							EEntity_mapping em = mappings.getByIndex(i);
							EEntity aimMappingDefinition = em.getTarget(null);

							if (aimMappingDefinition == aimDefinition) {
								pckgMap.put(armDefinition, em);
								found = true;
								break;
							}
						}

						if (!found) {
							throw new SdaiException(SdaiException.ED_NVLD, "The mapping wasn't found for these definitions:" +
													armDefinition + "->" + aimDefinition);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			arm2aimMap.put(packageName, pckgMap);
		}

		return pckgMap;
	}

	protected static AEntity_mapping mergeComplexEntitiesMappings(AEntity_mapping list1, AEntity_mapping list2) throws SdaiException {
		SdaiIterator list1It = list1.createIterator();
		AEntity_mapping tmp = new AEntity_mapping();
		while (list1It.next()) {
			EEntity_mapping em = list1.getCurrentMember(list1It);
			EEntity_definition domain1 = (EEntity_definition) em.getTarget(null);

            SdaiIterator list2It = list2.createIterator();
			while (list2It.next()) {
				EEntity_mapping em2 = list2.getCurrentMember(list2It);
				EEntity_definition domain2 = (EEntity_definition) em2.getTarget(null);
				if (domain1.isSubtypeOf(domain2)) {
					if (!tmp.isMember(em)) {
						tmp.addUnordered(em);
					}
					break;
				} else if (domain2.isSubtypeOf(domain1)) {
					if (!tmp.isMember(em2)) {
						tmp.addUnordered(em2);
					}
				}
			}
		}
		return tmp;
	}

	static EEntity_mapping getMatchingARMEntityMapping(SdaiContext context, EMappedARMEntity armEntity) throws SdaiException {
		EEntity_definition entityDefinition = armEntity.getInstanceType();
		AEntity_mapping mappings = new AEntity_mapping();
		int mappingsCount = armEntity.findARMEntityMappings(context, mappings, EEntity.MOST_SPECIFC_ENTITY);

		// check the DIRECT flag for mapping ant get one - by now it takes only most generic entity mapping
		if (mappingsCount < 1) {
			// perhaps this is complex entity
			AEntity_definition edefs = ((CMappedARMEntity)armEntity).parseComplexType();
			AEntity_mapping res = null;

			SdaiIterator it = edefs.createIterator();
			while (it.next()) {
				AEntity_mapping tmp = new AEntity_mapping();
				EEntity_definition edef = edefs.getCurrentMember(it);
				CEntity_mapping.usedinSource(null, edef, context.mappingDomain, tmp);
				if (res == null) {
					res = tmp;
				} else {
					res = mergeComplexEntitiesMappings(res, tmp);
				}
			}

			if (res == null) {
				res = new AEntity_mapping();
			} else {
                if (res.getMemberCount() < 1) {
                    throw new SdaiException(SdaiException.OP_NVLD, "No mappings found for entity definition: " + armEntity.getInstanceType());
                }

				return findMostSpecificEntityMapping(res, false);
			}

			if (res.getMemberCount() != 1) {
				throw new SdaiException(SdaiException.VA_NVLD,"Entity [" + armEntity + "] has no DIRECT mapping.");
			} else {
				return res.getByIndex(1);
			}
		}

		if (mappings.getMemberCount() < 1) {
            throw new SdaiException(SdaiException.OP_NVLD, "No mappings found for entity definition: " + armEntity.getInstanceType());
        }

        EEntity_mapping em = findMostSpecificEntityMapping(mappings, true);
		return em;
	}

	protected static EEntity_mapping findMostSpecificEntityMapping(AEntity_mapping mappings, boolean mostAbstract) throws SdaiException {
		int mappingsCount = mappings.getMemberCount();

        EEntity_mapping rv = mappings.getByIndex(1);
		EEntity targetEntity = rv.getTarget(null);

		if (targetEntity instanceof EAttribute) {
			throw new SdaiException(SdaiException.VA_NVLD,"Entity to attribute mapping not implemented yet: " + rv);
		}

		EEntity_definition target = (EEntity_definition) targetEntity;

		// find the most generic entity mapping
		for (int i = 1; i < mappingsCount; i++) {
			EEntity_mapping otherMapping = mappings.getByIndex(i + 1);

			EEntity otherTargetEntity = otherMapping.getTarget(null);

			if (otherTargetEntity instanceof EAttribute) {
				throw new SdaiException(SdaiException.VA_NVLD,"Entity to attribute mapping not implemented yet: " + otherMapping);
			}

			EEntity_definition otherTarget = (EEntity_definition) otherTargetEntity;
			if ((target.isSubtypeOf(otherTarget) && (mostAbstract)) ||
				(otherTarget.isSubtypeOf(target) && (!mostAbstract))){
				rv = otherMapping;
				target = otherTarget;
			}
		}

		// check this to be parent of every mapping
		for (int i = 0; i < mappingsCount; i++) {
			EEntity_mapping otherMapping = mappings.getByIndex(i + 1);
			EEntity otherTargetEntity = otherMapping.getTarget(null);
			EEntity_definition otherTarget = (EEntity_definition) otherTargetEntity;
			if ((!otherTarget.isSubtypeOf(target)) &&
			    (!otherTarget.isKindOf(target))) {
				throw new SdaiException(SdaiException.ED_NVLD, "Entity mappings targets aren't subtypes of each other: " +
										rv + ", " + otherMapping);
			}
		}

		return rv;
	}

	static EEntity_mapping getAIMEntityMapping(SdaiContext _context, EMappedARMEntity armEntity) throws SdaiException {
		EEntity_definition armDefinition = armEntity.getInstanceType();

		if (context != _context) {
			arm2aimMap.clear();
			context = _context;
		}

		HashMap pckgMap = getPackageMap(armEntity);

		Object value = pckgMap.get(armDefinition);
		if (value == null) {
			value = getMatchingARMEntityMapping(context, armEntity);
			if (value != null) {
				pckgMap.put(armDefinition, value);
			}
		}

		return (EEntity_mapping) value;
	}

	protected static HashMap getAimEntityReuseFlags(EMappedARMEntity armEntity) {
		Class armClass = armEntity.getClass();
		String className = armClass.getName();
		int dotPos = className.lastIndexOf('.');

		String packageName = className.substring(0, dotPos);

		HashMap pckgMap = (HashMap) aimReuseFlagsMap.get(packageName);
		if (pckgMap == null) {
			pckgMap = new HashMap();
			// read schema/creation_flags.properties file
			try {
				Properties props = new Properties();
				InputStream istream = armClass.getResourceAsStream("creation_flags.properties");
				if (istream != null) {
					props.load(istream);
					Enumeration prop_elems = props.propertyNames();
					while (prop_elems.hasMoreElements()) {
						String aimClassStr = (String) prop_elems.nextElement();
						String reuseFlag = props.getProperty(aimClassStr);

						EEntity aimEntity =
							(EEntity) Class.forName(aimClassStr, true,
									SdaiClassLoaderProvider.getDefault().getClassLoader()).newInstance();

						int value;
						if (reuseFlag.compareTo(REUSE_GLOBALLY_STR) == 0) {
							value = REUSE_GLOBALLY;
						} else if (reuseFlag.compareTo(REUSE_LOCALLY_STR) == 0) {
							value = REUSE_LOCALLY;
						} else if (reuseFlag.compareTo(REUSE_OVERWRITE_STR) == 0) {
							value = REUSE_OVERWRITE;
						} else {
							throw new SdaiException(SdaiException.VA_NVLD, "This type of reuse flag is not supported: \"" +
													reuseFlag + "\", Class: " + aimEntity.getClass());
						}

						EEntity_definition aimDefinition = aimEntity.getInstanceType();
						pckgMap.put(aimDefinition, new Integer(value));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			aimReuseFlagsMap.put(packageName, pckgMap);
		}

		return pckgMap;
	}

	static int getAimEntityReuseFlag(EMappedARMEntity armEntity, EEntity_definition aimDefinition) {
		HashMap pckgMap = getAimEntityReuseFlags(armEntity);

		Integer value = (Integer) pckgMap.get(aimDefinition);
		if (value == null) {
			value = new Integer(REUSE_LOCALLY);
			pckgMap.put(aimDefinition, value);
		}

		return value.intValue();
	}
}
