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

// import org.apache.commons.collections.SequencedHashMap;
import java.util.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.util.Debug;
import jsdai.util.LangUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class ObjectMapping {
//     public final boolean debugState = true;

    jsdai.dictionary.EDefined_type definedTypes[] = new jsdai.dictionary.EDefined_type[20];
    ASdaiModel targetDomain = null;
    ASdaiModel mappingDomain = null;
    SdaiModel dataModel = null;

    // only for ARM attributes
    jsdai.lang.EEntity temp;

    java.util.Set touchedInstances;

	static final int BLOCK_MAPPED_USERS_RECURSION = 128;
	static final int DONT_CHECK_STRONG_ATTRIBUTES = 256;
	static final int DONT_CHECK_STRONG_USERS = 512;
	static final int DONT_CHECK_ATTRIBUTE_DOMAIN_MAPPING = 1024;
	static final int DONT_USE_DERIVED_VARIANTS = 2048;

	//SdaiSession session = SdaiSession.getSession();
	MethodCallsCache getMappedAttributeCache = null;
	MethodCallsCache testMappedAttributeCache = null;
	MethodCallsCache testMappedEntityCache = null;
//     MethodCallsCache goThroughPathCache = null;
    MethodCallsCache findMappedUsersCache = null;
    MethodCallsCache testMappingPathCache = null;

	// Attributes used for attribute mappings
	jsdai.mapping.AAttribute_mapping_path_select attributePath = null;
	EEntity currentPathElementEntityMapping = null;
	boolean firstConstraintEntityMapping = true;
	SdaiIterator iteratorPosition = null;
	int leftElements = 0;
	Object value;
    static boolean oldVersion = false;


	//New version support fields
	int members[] = new int[30];
	ADefined_type dataTypes[] = new ADefined_type[30];
	int attributePathTop = -1;
	EDefined_type selectPath[] = new EDefined_type[20];
	int defaultMode;

    ObjectMapping() { }

//     ObjectMapping(ASdaiModel data, ASdaiModel mapping, 
// 				  java.util.Set touchedInstances) throws SdaiException {
// 		initialize(data, mapping, touchedInstances);
// //         targetDomain = data;
// //         mappingDomain = mapping;
// //         this.touchedInstances = touchedInstances;
//     }

	void initialize(SdaiSession session, ASdaiModel data, ASdaiModel mapping, 
					java.util.Set touchedInstances) throws SdaiException {
		definedTypes = new jsdai.dictionary.EDefined_type[20];
		targetDomain = null;
		mappingDomain = null;
		dataModel = null;

		targetDomain = data;
		mappingDomain = mapping;
		this.touchedInstances = touchedInstances;

		getMappedAttributeCache = 
			MethodCallsCacheManager.methodCallsCache(session, 
													 MethodCallsCacheManager.CACHE_GET_MAPPED_ATTRIBUTE);
		testMappedAttributeCache = 
			MethodCallsCacheManager.methodCallsCache(session, 
													 MethodCallsCacheManager.CACHE_TEST_MAPPED_ATTRIBUTE);
		testMappedEntityCache = 
			MethodCallsCacheManager.methodCallsCache(session, 
													 MethodCallsCacheManager.CACHE_TEST_MAPPED_ENTITY);
// 		goThroughPathCache = 
// 			MethodCallsCacheManager.methodCallsCache(session, 
// 													 MethodCallsCacheManager.CACHE_GO_THROUGH_PATH);
		findMappedUsersCache = 
			MethodCallsCacheManager.methodCallsCache(session, 
													 MethodCallsCacheManager.CACHE_FIND_MAPPED_USERS);
		testMappingPathCache = 
			MethodCallsCacheManager.methodCallsCache(session, 
													 MethodCallsCacheManager.CACHE_TEST_MAPPING_PATH);

		// Attributes used for attribute mappings
		attributePath = null;
		currentPathElementEntityMapping = null;
		firstConstraintEntityMapping = true;
		iteratorPosition = null;
		leftElements = 0;
		oldVersion = false;
	}

	boolean isEqualDefinedTypes(EDefined_type[] type1, ADefined_type type2) throws SdaiException {
		boolean rv = false;

		SdaiIterator it2 = type2.createIterator();
		boolean tmp = true;
		int index = 0;

		while (type1[index] != null) {
			tmp = it2.next();
			if (!tmp) {
				return false;
			}

			EDefined_type dt2 = (EDefined_type) type2.getCurrentMemberObject(it2);
			if (!type1[index].equals(dt2)) {
				return false;
			}
			index++;
		}

		if (tmp) {
			tmp = it2.next();
			if (tmp) {
				return false;
			}
		}

		return true;
	}

    /**
     * Tests one element in mapping path. If this element is some relation (path_constraint, ...) then it
     */
    boolean testMappingPath(jsdai.lang.EEntity targetInstance, jsdai.lang.EEntity constraint) throws jsdai.lang.SdaiException {

        if(touchedInstances != null) touchedInstances.add(targetInstance);
        boolean rv = false;
        jsdai.dictionary.EDefined_type[] types = new jsdai.dictionary.EDefined_type[20];
        if (targetInstance == null) {
            rv = false;
        }
        if (constraint == null) {
            rv = true;
        }
        else {
            if (constraint instanceof ENegation_constraint) {
                return !testMappingPath(targetInstance, ((ENegation_constraint) constraint).getConstraints(null));
            } else if (constraint instanceof EEnd_of_path_constraint) {
                return testMappingPath(targetInstance, ((EEnd_of_path_constraint) constraint).getConstraints(null));
            }
        }

        jsdai.lang.SdaiModel dataModel = targetInstance.findEntityInstanceSdaiModel();
        if (constraint instanceof EAttribute) {
            jsdai.dictionary.EAttribute l = (EAttribute)constraint;
            rv = ((CEntity)targetInstance).testAttributeFast(l, types) > 0;
        } else if (constraint instanceof EInverse_attribute_constraint) {
            if (((EInverse_attribute_constraint)constraint).testInverted_attribute(null)) {
                jsdai.lang.Aggregate a = getFollowingInvertedAttribute(targetInstance, (EInverse_attribute_constraint)constraint);
                rv = 0 != a.getMemberCount();
            } else { // this should never happend.
                rv = false;
            }
        } else if (constraint instanceof EPath_constraint) {
			Object ids[] = new Object[] { constraint, targetInstance };
			Object value = testMappingPathCache.getValueByIds(ids);
			if (value != null) {
				return value != MethodCallsCache.nullValue;
			}
			jsdai.lang.Aggregate a = getFollowing(targetInstance, getElement1((EPath_constraint)constraint));
			if ((a == null) || (0 == a.getMemberCount())) {
				rv = false;
			} else {
				jsdai.lang.SdaiIterator it = a.createIterator();
				while(it.next()) {
					jsdai.lang.EEntity ae = (jsdai.lang.EEntity)a.getCurrentMemberObject(it);
					rv = rv || testMappingPath(ae, getElement2((EConstraint_relationship)constraint));
					if (rv) {
						break;
					}
				}
			}
			testMappingPathCache.setValueByIds(ids, rv ? constraint : null);
        } else if (constraint instanceof EOr_constraint_relationship) {
            rv = testMappingPath(targetInstance, getElement1((EOr_constraint_relationship)constraint)) || testMappingPath(targetInstance, getElement2((EConstraint_relationship)constraint));
        } else if (constraint instanceof EAnd_constraint_relationship) {
            rv = testMappingPath(targetInstance, getElement1((EAnd_constraint_relationship)constraint)) && testMappingPath(targetInstance, getElement2((EConstraint_relationship)constraint));
        } else if (constraint instanceof EIntersection_constraint) {
			rv = true;
			EIntersection_constraint ic = (EIntersection_constraint) constraint;
			List intersectList = intersectConstraints(Collections.singletonList(targetInstance), ic);
			rv = !intersectList.isEmpty();
        } else if (constraint instanceof EConstraint_attribute) {
            EConstraint_attribute ca = (EConstraint_attribute)constraint;
            //			if (!testMappingPath(targetInstance, getAttribute(ca))) {
            //				return false;
            //			}
            if (constraint instanceof ESelect_constraint) {
                ESelect_constraint sc = (ESelect_constraint)constraint;
                jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
                EEntity cattribute = getAttribute(sc);
                if (cattribute instanceof EAttribute) {
                    jsdai.dictionary.EAttribute l = (EAttribute)cattribute;
                    int n = ((CEntity)targetInstance).testAttributeFast(l, adt);
/*                    for (int j = 0; j < n; j++) {
                        jsdai.dictionary.EDefined_type dt = adt[j];
                        ADefined_type cadt = sc.getData_type(null);
                        jsdai.lang.SdaiIterator ci = cadt.createIterator();
                        if (cadt.getMemberCount() == 0) {
                            rv = false;
                        }
                        boolean f = true;
                        while (ci.next()) {
                            EDefined_type cdt = cadt.getCurrentMember(ci);
                            f = f && (dt == cdt);
                        }
                        rv = f;
                    }*/
					if (n > 0) {
						rv = isEqualDefinedTypes(adt, sc.getData_type(null));
					}
                } else {
                    rv = false;
                }
            } else if (constraint instanceof EEntity_constraint) {
                rv = getFollowingEntity(targetInstance, (EEntity_constraint)constraint) != null;
            } else if (constraint instanceof EAggregate_member_constraint) {
                rv = getFollowingAggregateMember(targetInstance, (EAggregate_member_constraint)constraint) != null;
            } else {
				Object o;try{
                o = getFollowingObject(targetInstance, getAttribute(ca));
				} catch(SdaiException e) {
					System.out.print("ca: ");
					System.out.println(ca);
					System.out.print("targetInstance: ");
					System.out.println(targetInstance);
					throw e;
				}
                if(o != null) {
					int intCv = 0;
					double doubleCv = 0;
					String stringCv = null;
					int cvType = 0;
                    if (constraint instanceof EEnumeration_constraint) {
                        jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
                        jsdai.dictionary.EExplicit_attribute l = (EExplicit_attribute)getAttribute((EConstraint_attribute)constraint); // there can be not only explicit attributess
                        jsdai.dictionary.EDefined_type dt = (EDefined_type)l.getDomain(null);
                        EEnumeration_type enumDomain = (EEnumeration_type)dt.getDomain(null); // bug. This will not work for selects.
                        intCv = LangUtils.convertEnumerationStringToInt(((EEnumeration_constraint)constraint).getConstraint_value(null), enumDomain);
						cvType = 1;
                    } else if (constraint instanceof EInteger_constraint) {
                        intCv = ((EInteger_constraint)constraint).getConstraint_value(null);
						cvType = 1;
                    } else if (constraint instanceof EBoolean_constraint) {
                        intCv = ((EBoolean_constraint)constraint).getConstraint_value(null) ? 2 : 1;
						cvType = 1;
                    } else if (constraint instanceof EReal_constraint) {
                        doubleCv = ((EReal_constraint)constraint).getConstraint_value(null);
						cvType = 2;
                    } else if (constraint instanceof ELogical_constraint) {
                        intCv = ((ELogical_constraint)constraint).getConstraint_value(null);
						cvType = 1;
                    } else if (constraint instanceof EString_constraint) {
                        stringCv = ((EString_constraint)constraint).getConstraint_value(null);
						cvType = 3;
                    }
					switch(cvType) {
					case 1:
						if(o instanceof Aggregate) {
							Aggregate oAgg = (Aggregate)o;
							SdaiIterator oAggIter = oAgg.createIterator();
							while(oAggIter.next()) {
								Object aggObj = oAgg.getCurrentMemberObject(oAggIter);
								int va = ((Integer)o).intValue();
								rv = va == intCv;
								if(rv) {
									break;
								}
							}
						} else {
							int va = ((Integer)o).intValue();
							rv = va == intCv;
						}
						break;
					case 2:
						if(o instanceof Aggregate) {
							Aggregate oAgg = (Aggregate)o;
							SdaiIterator oAggIter = oAgg.createIterator();
							while(oAggIter.next()) {
								Object aggObj = oAgg.getCurrentMemberObject(oAggIter);
								double va = ((Double)aggObj).doubleValue();
								rv = va == doubleCv;
								if(rv) {
									break;
								}
							}
						} else {
							double va = ((Double)o).doubleValue();
							rv = va == doubleCv;
						}
						break;
					case 3:
						if(o instanceof Aggregate) {
							Aggregate oAgg = (Aggregate)o;
							SdaiIterator oAggIter = oAgg.createIterator();
							while(oAggIter.next()) {
								Object aggObj = oAgg.getCurrentMemberObject(oAggIter);
								String va = (String)aggObj;
								rv = va.equals(stringCv);
								if(rv) {
									break;
								}
							}
						} else {
							String va = (String)o;
							rv = va.equals(stringCv);
						}
						break;
					}
                }
            }
        } else if (constraint instanceof EType_constraint) {
            rv = testFollowingType(targetInstance, (EType_constraint)constraint);
            //   				rv = true;
        } else {
            rv = false;
        }

        return rv;
    }

    jsdai.lang.EEntity getElement1(EPath_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CPath_constraint.attributeElement1(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getElement1(EInstance_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CInstance_constraint.attributeElement1(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getElement2(EConstraint_relationship c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CConstraint_relationship.attributeElement2(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getAttribute(EAttribute_value_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CAttribute_value_constraint.attributeAttribute(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getAttribute(ESelect_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CSelect_constraint.attributeAttribute(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getAttribute(EEntity_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CEntity_constraint.attributeAttribute(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getAttribute(EAggregate_member_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CAggregate_member_constraint.attributeAttribute(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

	jsdai.lang.Aggregate list2Aggregate(List list) throws jsdai.lang.SdaiException {
		jsdai.lang.Aggregate rv = new jsdai.lang.AEntity();
		for (int i = 0; i < list.size(); i++) {
			rv.addByIndex(i + 1, list.get(i), null);
		}

		return rv;
	}

    jsdai.lang.EEntity getAttribute(EConstraint_attribute c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = null;
        if (c instanceof EAttribute_value_constraint) {
            a = CAttribute_value_constraint.attributeAttribute(null);
        } else if (c instanceof ESelect_constraint) {
            a = CSelect_constraint.attributeAttribute(null);
        } else if (c instanceof EEntity_constraint) {
            a = CEntity_constraint.attributeAttribute(null);
        } else if (c instanceof EAggregate_member_constraint) {
            a = CAggregate_member_constraint.attributeAttribute(null);
        }
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.EEntity getAttribute(EInverse_attribute_constraint c) throws jsdai.lang.SdaiException {
        jsdai.dictionary.EAttribute a = CInverse_attribute_constraint.attributeInverted_attribute(null);
        return (jsdai.lang.EEntity)c.get_object(a);
    }

    jsdai.lang.Aggregate getFollowing(jsdai.lang.EEntity targetInstance, jsdai.lang.EEntity path) throws jsdai.lang.SdaiException {
        jsdai.lang.Aggregate rv = new jsdai.lang.AEntity();
        jsdai.lang.EEntity t = null;
        if (path == null) {
            return rv;
        } else if (path instanceof EAttribute) {
            rv = getFollowingAttribute(targetInstance, (EAttribute)path);
        } else if (path instanceof EInverse_attribute_constraint) {
            rv = getFollowingInvertedAttribute(targetInstance, (EInverse_attribute_constraint)path);
        } else if (path instanceof EAggregate_member_constraint) {
            rv = getFollowingAggregateMember(targetInstance, (EAggregate_member_constraint)path);
		} else if (path instanceof EEntity_constraint) {
            rv = getFollowingEntity(targetInstance, (EEntity_constraint)path);
		} else if (path instanceof EIntersection_constraint) {
			EIntersection_constraint ic = (EIntersection_constraint) path;
            List aggrList = intersectConstraints(Collections.singletonList(targetInstance), ic);
			rv = list2Aggregate(aggrList);
        }

        return rv;
    }

    jsdai.lang.Aggregate getFollowingAttribute(jsdai.lang.EEntity targetInstance, EAttribute path) throws jsdai.lang.SdaiException {
        jsdai.lang.Aggregate rv = null;
        jsdai.dictionary.EAttribute at = path;
        jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
		if (((CEntity)targetInstance).testAttributeFast(at, adt) <= 0) {
			return null;
		}
        Object t = targetInstance.get_object(at);
        if (t instanceof jsdai.lang.EEntity) {
            rv = new jsdai.lang.AEntity();
            rv.addByIndex(1, (jsdai.lang.EEntity)t, null);
        } else {
            if (t instanceof jsdai.lang.Aggregate) {
                rv = (jsdai.lang.Aggregate)t;
            }
        }
        return rv;
    }

    jsdai.lang.Aggregate getFollowingInvertedAttribute(jsdai.lang.EEntity targetInstance, EInverse_attribute_constraint c) throws jsdai.lang.SdaiException {
        EEntity a = getAttribute(c);
        EAttribute attribute = LangUtils.getExpressAttributeDefinitionFromConstraint(a);
        jsdai.lang.AEntity rv = new AEntity();
        targetInstance.findEntityInstanceUsedin(attribute, targetDomain, rv);
        SdaiIterator i = rv.createIterator();
        while (i.next()) {
            if (!testMappingPath((EEntity)rv.getCurrentMemberObject(i), a)) {
                i.remove();
            }
        }
        return rv;
    }

    Object getFollowingObject(jsdai.lang.EEntity targetInstance, jsdai.lang.EEntity path) throws jsdai.lang.SdaiException {
        Object rv = null;
        if (path instanceof EAttribute) {
            rv = getFollowingAttributeObject(targetInstance, (EAttribute)path);
        } else if (path instanceof EInverse_attribute_constraint) {
            rv = getFollowingInvertedAttribute(targetInstance, (EInverse_attribute_constraint)path);
        } else if(path instanceof EAggregate_member_constraint) {
            rv = getFollowingAggregateMember(targetInstance, (EAggregate_member_constraint)path);
        } else if (path instanceof EEntity_constraint) {
            rv = getFollowingEntity(targetInstance, (EEntity_constraint)path);
        }
        return rv;
    }

    Object getFollowingAttributeObject(jsdai.lang.EEntity targetInstance, EAttribute path) throws jsdai.lang.SdaiException {
        Object rv = null;
        jsdai.dictionary.EAttribute at = path;
        //Debug.println(" target=" + targetInstance + " attribute=" + at);
		//Workaround for derived attributes throwing an exception in testAttribute
		if (((CEntity)targetInstance).testAttributeFast(at, definedTypes) > 0) {
			rv = targetInstance.get_object(at);
		}
        return rv;
    }

    /**
     * This method returns Object that is attribute of targetInstance and its definition is refered by path.attribute. If aggregate constraints are connected reqursevly it call other method to make reqursion.
     * @param targetInstance - an instance to which attribute belongs. It is required and can not be null.
     * @param constraint - aggregate_member_constraint that defined which attribute of target is aggregate and which element of this aggregate is required. It is required and can not be null.
     * @param targetDomain - domain where to seach for target dictionary definitions. It may be null.
     * @return element of aggregate restricted by constraint and belongs to targetInstance. It returns null in nore aggregate elements are found.
     */
    jsdai.lang.Aggregate getFollowingAggregateMember(jsdai.lang.EEntity targetInstance, EAggregate_member_constraint constraint) throws jsdai.lang.SdaiException {
        jsdai.lang.Aggregate rv = null, tmp;
        EEntity cattribute = constraint.getAttribute(null);
		if(cattribute instanceof ESelect_constraint) {
			ESelect_constraint sc = (ESelect_constraint)cattribute;
			cattribute = getAttribute(sc);
			jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
			int n = ((CEntity)targetInstance).testAttributeFast((EAttribute)cattribute, adt);
			if(n <= 0 || !isEqualDefinedTypes(adt, sc.getData_type(null))) {
				return rv;
			}
		}
        if (cattribute instanceof EAttribute) {
            jsdai.dictionary.EAttribute attribute = (EAttribute)cattribute;
            //		Debug.println("instance=" + targetInstance + " attribute=" + attribute);
            if (attribute instanceof EInverse_attribute ||
            ((CEntity)targetInstance).testAttributeFast(attribute, definedTypes) > 0) {
                rv = attribute instanceof EInverse_attribute ?
                (Aggregate)targetInstance.get_inverse((EInverse_attribute)attribute, targetDomain)
                : (Aggregate)targetInstance.get_object(attribute);
                if (constraint.testMember(null)) {
                    tmp = rv;
                    int index = constraint.getMember(null);
                    rv = new CAggregate();
                    //				if(tmp.getLowerBound() <= index && index <= tmp.getUpperBound())
                    if(index <= tmp.getMemberCount()) // This is terrible but jsdai has no support for above yet!!!!! (V.N.)
                        LangUtils.addToAggregate(rv, tmp.getByIndexObject(index), null);
                } else {
                    Aggregate originalAggregate = rv;
                    rv = new CAggregate();
                    SdaiIterator aggIter = originalAggregate.createIterator();
                    int newIndex = 1;
                    while (aggIter.next()) {
                        Object instance = originalAggregate.getCurrentMemberObject(aggIter);
                        rv.addByIndex(newIndex++, instance, null);
                    }
                }
            }
        } else if (cattribute instanceof EInverse_attribute_constraint) {
            rv = getFollowingInvertedAttribute(targetInstance, (EInverse_attribute_constraint)cattribute);
        } else if (cattribute instanceof EAggregate_member_constraint) {
            rv = new jsdai.lang.AEntity();
            tmp = getFollowingAggregateMember(targetInstance, (EAggregate_member_constraint)cattribute);
            SdaiIterator i = tmp.createIterator();
            while (i.next()) {
                Object o = tmp.getCurrentMemberObject(i);
                if (o instanceof jsdai.lang.Aggregate) {
                    if (constraint.testMember(null)) {
                        o = ((Aggregate)o).getByIndexObject(constraint.getMember(null));
                    }
                    LangUtils.addToAggregate(rv, o, null);
                }
            }
        } else {
            //Debug.reportNotImplemented("Get folowing select of aggregates");
        }
        return rv;
    }

    /**
     * I assume that entity constraint can not go to insverse attribute (nor directly, nor inderectly).
     * This method will not work for inverse attributes.
     */
    jsdai.lang.Aggregate getFollowingEntity(jsdai.lang.EEntity targetInstance, EEntity_constraint path) throws jsdai.lang.SdaiException {
        jsdai.lang.Aggregate rv = null;
        jsdai.lang.Aggregate aggregate = getFollowing(targetInstance, getAttribute(path));
        if (aggregate == null) {
            return rv;
        }
        jsdai.lang.SdaiIterator iterator = aggregate.createIterator();
        jsdai.dictionary.EEntity_definition lang_entity = null;
        EEntity_definition mapping_entity = null;

        if (!path.testDomain(null)) {
            Debug.println("Wrong=" + path);
        }
        mapping_entity = path.getDomain(null);
		boolean exact = path instanceof EExact_entity_constraint;
        while (iterator.next()) {
            EEntity ae = (jsdai.lang.EEntity)aggregate.getCurrentMemberObject(iterator);
            lang_entity = mapping_entity;
            if ((exact && ae.isInstanceOf(lang_entity)) || (!exact && ae.isKindOf(lang_entity))) {
                if (rv == null) {
                    rv = new AEntity();
                }
                rv.addByIndex(1, ae, definedTypes);
            }
        }
        return rv;
    }

    boolean testFollowingType(jsdai.lang.EEntity targetInstance, EType_constraint path) throws jsdai.lang.SdaiException {
        boolean rv = false;
        EEntity_definition mapping_entity = null;
        EEntity constraints = null;

        if (path.testDomain(null)) {
            mapping_entity = path.getDomain(null);
        }


        if (path.testConstraints(null)) {
            constraints = path.getConstraints(null);
        }


        // check targetInstance to be subtype of domain attribute
        if (mapping_entity != null) {
			boolean exact = path instanceof EExact_type_constraint;
            rv = (exact && targetInstance.isInstanceOf(mapping_entity)) ||
				(!exact && targetInstance.isKindOf(mapping_entity));

            if (rv) {
                if (constraints != null) {
                    rv = rv && testMappingPath(targetInstance, path.getConstraints(null));
                }
            }
        }
        else {
            Debug.println("Wrong=" + path);
        }

        return rv;
    }

    // This method
    Object getAttributeFolowing(jsdai.lang.EEntity targetInstances, jsdai.lang.EEntity constraint) throws jsdai.lang.SdaiException {
        if (targetInstances == null) {
            return null;
        }
        if (constraint == null) {
            return targetInstances;
        }
        Object rv;
        /* This part is no longer used, because the input was restricted to EEntity, but it may be relased to object soon.
        if (targetInstances instanceof jsdai.lang.Aggregate) {
            ArrayList arr = new ArrayList();
            rv = arr;
            jsdai.lang.Aggregate agg = (jsdai.lang.Aggregate)targetInstances;
            jsdai.lang.Aggregate agg1;
            jsdai.lang.SdaiIterator i = agg.createIterator();
            while (i.next()) {
                Object o = getAttributeFolowingOneInstance((jsdai.lang.EEntity)agg.getCurrentMemberObject(i), constraint);
                if (o instanceof jsdai.lang.Aggregate) {
                    agg1 = (jsdai.lang.Aggregate)o;
                    jsdai.lang.SdaiIterator i1 = agg1.createIterator();
                    while (i1.next()) {
                        Object o1 = agg1.getCurrentMemberObject(i1);
                        arr.add(o1);
                    }
                } else {
                    rv = o;
                }
            }
        } else {
         */
        rv = getAttributeFolowingOneInstance(targetInstances, constraint);
        //}
        return rv;
    }

    // This method returns non empty Aggregate or Object (entity instance or simple value: String, Integer, Double, Boolean).
    Object getAttributeFolowingOneInstance(jsdai.lang.EEntity targetInstance, jsdai.lang.EEntity constraint) throws jsdai.lang.SdaiException {
        if (targetInstance == null) {
            return null;
        }
        if (constraint == null) {
            return targetInstance;
        }
        Object rv = null;
        jsdai.lang.SdaiModel dataModel = targetInstance.findEntityInstanceSdaiModel();
        if (constraint instanceof EAttribute) {
            jsdai.dictionary.EAttribute l = (EAttribute)constraint;
            if (((CEntity)targetInstance).testAttributeFast(l, definedTypes) > 0) {
                rv = targetInstance.get_object(l);
            }
        } else if (constraint instanceof EInverse_attribute_constraint) {
            if (((EInverse_attribute_constraint)constraint).testInverted_attribute(null)) {
                jsdai.lang.Aggregate a = getFollowingInvertedAttribute(targetInstance, (EInverse_attribute_constraint)constraint);
                rv = a;
            }
        } else if (constraint instanceof EPath_constraint) {
            jsdai.lang.Aggregate a = getFollowing(targetInstance, getElement1((EPath_constraint)constraint));
            if (a != null) {
                jsdai.lang.AEntity a1 = new jsdai.lang.AEntity();
                rv = a1;
                if (0 != a.getMemberCount()) {
                    jsdai.lang.SdaiIterator it = a.createIterator();
                    while(it.next()) {
                        jsdai.lang.EEntity ae = (jsdai.lang.EEntity)a.getCurrentMemberObject(it);
                        if (testMappingPath(ae, getElement2((EConstraint_relationship)constraint))) {
                            a1.addByIndex(1, ae);
                        }
                    }
                }
            }
        } else if (constraint instanceof EOr_constraint_relationship) {
            if (testMappingPath(targetInstance, getElement1((EOr_constraint_relationship)constraint)) || testMappingPath(targetInstance, getElement2((EOr_constraint_relationship)constraint))) {
                rv = targetInstance;
            }
        } else if (constraint instanceof EAnd_constraint_relationship) {
            if (testMappingPath(targetInstance, getElement1((EAnd_constraint_relationship)constraint)) && testMappingPath(targetInstance, getElement2((EAnd_constraint_relationship)constraint))) {
                rv = targetInstance;
            }
        } else if (constraint instanceof EIntersection_constraint) {
            rv = targetInstance;
//             System.out.println("That's an intersection constraint.");
        } else if (constraint instanceof EConstraint_attribute) {
            if (constraint instanceof EEntity_constraint) {
                Aggregate t;
                if ((t = getFollowingEntity(targetInstance, (EEntity_constraint)constraint)) != null) {
                    rv = t;
                }
            } else if (constraint instanceof EAggregate_member_constraint) {
                rv = getFollowingAggregateMember(targetInstance, (EAggregate_member_constraint)constraint);
            } else {
                EConstraint_attribute ca = (EConstraint_attribute)constraint;
                Object o = getFollowingObject(targetInstance, getAttribute(ca));
                if (constraint instanceof ESelect_constraint) {
                    ESelect_constraint sc = (ESelect_constraint)constraint;
                    jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
                    jsdai.dictionary.EAttribute l = (EAttribute)getAttribute(sc);
                    int n = ((CEntity)targetInstance).testAttributeFast(l, adt);
/*                    // Incorect
                    for (int ii = 0; ii < n; ii++) {
                        jsdai.dictionary.EDefined_type dt = adt[ii];
                        ADefined_type cadt = sc.getData_type(null);
                        jsdai.lang.SdaiIterator ci = cadt.createIterator();
                        boolean f = true;
                        while (ci.next()) {
                            EDefined_type cdt = cadt.getCurrentMember(ci);
                            f = f && (dt == cdt);
                        }
                        //
                        if (f) {
                            rv = o;
                        }
                    } */
					if (n > 0) {
						if (isEqualDefinedTypes(adt, sc.getData_type(null))) {
							rv = o;
						}
					}
                } else if (constraint instanceof EEnumeration_constraint) {
                    jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
                    jsdai.dictionary.EAttribute l = (EAttribute)getAttribute((EConstraint_attribute)constraint);
                    int n = ((CEntity)targetInstance).testAttributeFast(l, adt);
					if(n > 0) {
						int index = 0;
						while (adt[index] != null) {
							jsdai.dictionary.EDefined_type dt = adt[index];
							int cv = LangUtils.convertEnumerationStringToInt(((EEnumeration_constraint)constraint).getConstraint_value(null), (jsdai.dictionary.EEnumeration_type)dt);
							int va = ((Integer)o).intValue();
							if (va == cv) {
								rv = o;
							}
							index++;
						}
					}
                } else if (constraint instanceof EInteger_constraint) {
                    int cv = ((EInteger_constraint)constraint).getConstraint_value(null);
                    int va = ((Integer)o).intValue();
                    if (va == cv) {
                        rv = o;
                    }
                } else if (constraint instanceof EBoolean_constraint) {
                    boolean cv = ((EBoolean_constraint)constraint).getConstraint_value(null);
                    boolean va = ((Boolean)o).booleanValue();
                    if (va == cv) {
                        rv = o;
                    }
                } else if (constraint instanceof EReal_constraint) {
                    double cv = ((EReal_constraint)constraint).getConstraint_value(null);
                    double va = ((Double)o).doubleValue();
                    if (va == cv) {
                        rv = o;
                    }
                } else if (constraint instanceof ELogical_constraint) {
                    int cv = ((ELogical_constraint)constraint).getConstraint_value(null);
                    int va = ((Integer)o).intValue();
                    if (va == cv) {
                        rv = o;
                    }
                } else if (constraint instanceof EString_constraint) {
                    String cv = ((EString_constraint)constraint).getConstraint_value(null);
                    String va = (String)o;
                    if (va.equals(cv)) {
                        rv = o;
                    }
                }
            }
        }
        if (rv instanceof Aggregate) {
            if (((Aggregate)rv).getMemberCount() == 0) {
                rv = null;
            }
        }
        return rv;
    }

/*
    Object findAndSetValueForAttribute(jsdai.dictionary.EAttribute attribute, boolean needsToBeCreated, jsdai.lang.EEntity targetInstance) {
        Object rv = null;
        jsdai.lang.EEntity attributeDomain = null;
        if (attribute instanceof jsdai.lang.EExplicit_attribute) {
            jsdai.lang.EExplicit_attribute explicitAttribute = (jsdai.lang.EExplicit_attribute)attribute;
            attributeDomain = explicitAttribute.getDomain();
        }
        if (attributeDomain instanceof jsdai.dictionary.EEntity_definition) {
            jsdai.lang.Aggregate targetInstances = model.getEntityExtent((jsdai.dictionary.EEntity_definition)attributeDomain);
            jsdai.lang.SdaiIterator i = targetInstances.createIterator();
            while (i.next()) {
                rv = targetInstances.getCurrentMember(i);
                targetInstance.set(l, rv, new jsdai.dictionary.EDefined_type[1]);
                return rv;
            }
            needsToBeCreated = true;
        }
        if (needsToBeCreated) {
            if (attributeDomain instanceof jsdai.dictionary.EEntity_definition) {
                rv = model.createEntityInstance((jsdai.dictionary.EEntity_definition)attributeDomain);
            }
            else {
                // list all posible cases
            }
        }
        targetInstance.set(l, rv, new jsdai.dictionary.EDefined_type[1]);
        return rv;
    }
 */

	static long testMappedEntityMillis = 0;//FIXME:time
	/**
	 * Test whether a given targetInstance does fit to the mapping of entity_mapping.
	 * @param targetInstance an instance, which is tested to be mapping of
	 *        sourceEntity.
	 * @param sourceEntity an entity_mapping which mapping is tested
	 * @return true if this target instance is mapping of specified entity_mapping, false if is
	 *         otherwise.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	*/
	public boolean testMappedEntity(jsdai.lang.EEntity targetInstance,
	EEntity_mapping sourceEntity, int mode) throws jsdai.lang.SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		boolean rv = false;
		if ((targetInstance != null) && (sourceEntity != null)) {
			Object ids[] = null;
			boolean useCacheData = true;
			if (useCacheData) {
				ids = new Object[] { targetInstance, sourceEntity/*, new Integer(mode)*/ };
				if(testMappedEntityCache != null) {
					Object value = testMappedEntityCache.getValueByIds(ids);
					if (value != null) {
						testMappedEntityMillis += System.currentTimeMillis() - startMillis;//FIXME:time
						return value != MethodCallsCache.nullValue;
					}
				}
			}

			EEntity target = sourceEntity.getTarget(null);
			if (target instanceof EEntity_definition) {
				jsdai.dictionary.EEntity_definition aimen = (EEntity_definition)target;
				if (!targetInstance.isKindOf(aimen)) {
					return addTestMappedEntityCache(rv, useCacheData, ids, startMillis);
				}
			} else {
				jsdai.dictionary.EAttribute aimatt = (EAttribute)target;
				jsdai.dictionary.EEntity_definition aimen = aimatt.getParent_entity(null);

                if (!targetInstance.isKindOf(aimen)) {
					return addTestMappedEntityCache(rv, useCacheData, ids, startMillis);
				}
				if (targetInstance.testAttribute(aimatt, definedTypes) != 0) {
					return addTestMappedEntityCache(rv, useCacheData, ids, startMillis);
				}
			}
			if (!sourceEntity.testConstraints(null)) {
				rv = true;
			} else {
				rv = testMappingPath(targetInstance, (jsdai.lang.EEntity)sourceEntity.get_object(CEntity_mapping.attributeConstraints(null)));
			}
			if (rv) {
				CEntityMappingBase entityMappingBase = (CEntity_mapping)sourceEntity;
				entityMappingBase.collectStrongAttributes();
				if((mode & DONT_CHECK_STRONG_ATTRIBUTES) == 0
				&& entityMappingBase.strongAttributes != null) {
					Iterator strongAttributeIter =
						entityMappingBase.strongAttributes.values().iterator();
					while(strongAttributeIter.hasNext()) {
						LinkedList oneAttributeList = (LinkedList)strongAttributeIter.next();
						Iterator oneAttributeIter = oneAttributeList.iterator();
						rv = false;
						while(oneAttributeIter.hasNext()) {
							EGeneric_attribute_mapping strongAttribute =
								(EGeneric_attribute_mapping)oneAttributeIter.next();
							TestAttributeCallsCacheEntry entry =
								new TestAttributeCallsCacheEntry(targetInstance, strongAttribute, mode);
                            if (!testAttributeCallsCacheExists(entry)) {
                                try {
									testAttributeCallsCachePushCall(entry);
									if(testMappedAttribute(targetInstance, strongAttribute, mode)) {
										rv = true;
										break;
									}
                                } finally {
									testAttributeCallsCachePopCall();
                                }
                            } else {
                                rv = true;
                                break;
                            }
						}
						if(!rv) break;
					}
				}
//				if (rv && (mode & (BLOCK_MAPPED_USERS_RECURSION | DONT_CHECK_STRONG_USERS)) == 0 &&
				if (rv && (mode & DONT_CHECK_STRONG_USERS) == 0 &&
                    sourceEntity.getStrong_users(null)) {
					TestMappedUsersCallsCacheEntry entry = 
						new TestMappedUsersCallsCacheEntry(targetInstance, sourceEntity, mode);
                    if (!testMappedUsersCallsCacheExists(entry)) {
						
						try {
							testMappedUsersCallsCachePushCall(entry);
							rv = testMappedUsers(targetInstance, sourceEntity, null, mode);
						} finally {
							testMappedUsersCallsCachePopCall();
						}
//						mode | BLOCK_MAPPED_USERS_RECURSION);
                    }
				}

//				if (mode == EEntity.MANDATORY_ATTRIBUTES_SET) {
//					rv = testMandatoryAttributes(targetInstance, sourceEntity);
//				}
			}
			return addTestMappedEntityCache(rv, useCacheData, ids, startMillis);

		}
		testMappedEntityMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return rv;
	}

	private boolean addTestMappedEntityCache(boolean rv, boolean useCacheData, 
											 Object ids[], long startMillis) {
		if(useCacheData && testMappedEntityCache != null) {
			testMappedEntityCache.setValueByIds(ids, rv ? ids[0] : null);
		}
		testMappedEntityMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return rv;
	}

	boolean testMandatoryAttributes(EEntity targetInstance, EEntity_mapping sourceEntity) throws SdaiException {
		boolean rv = true;
		ArrayList attributes = new ArrayList();
		LangUtils.findExplicitAttributes(sourceEntity.getSource(null), attributes);
		// Iterate through all explicit attributes of ARM entity.
		for (int i = 0; i < attributes.size() && rv; i++) {
			EAttribute armAttribute = (EAttribute)attributes.get(i);
			if (armAttribute instanceof EExplicit_attribute) {
				if (!((EExplicit_attribute)armAttribute).getOptional_flag(null)) {
					continue;
				}
				rv = getSourceAttribute(targetInstance, armAttribute, 0) != null;
			}
		}
		return rv;
	}

	/**
	@returns null - NO MAPPING FOR SPECIFIED ALTERNATIVES
		Object[].length == 0 - there are mappings but attribute not set
		Object[] - values of attribute
	*/
	Object[] getSourceAttribute(EAttribute sourceAttribute, EEntity targetInstance, EEntity_mapping entityMapping) throws SdaiException {
		Object rv[];
		boolean noMapping = true;
		if ((targetInstance == null) || (sourceAttribute == null)) {
			return null;
		}
		AAttribute_mapping aema = new AAttribute_mapping();
		jsdai.mapping.CAttribute_mapping.usedinSource(null, sourceAttribute, mappingDomain, aema);
		jsdai.lang.SdaiIterator i = aema.createIterator();
		ArrayList v = new ArrayList();
		while(i.next()) {
			EAttribute_mapping attributeMapping = (EAttribute_mapping)aema.getCurrentMember(i);
			if (attributeMapping.getParent_entity(null) != entityMapping) {
				continue;
			}
			noMapping = false;
			Object o = targetInstance.getMappedAttribute(attributeMapping, targetDomain, mappingDomain, 0);
			if (o != null) {
				if (o instanceof jsdai.lang.Aggregate) {
					jsdai.lang.Aggregate ag = (jsdai.lang.Aggregate)o;
					jsdai.lang.SdaiIterator j = ag.createIterator();
					while (j.next()) {
     					Object value = ag.getCurrentMemberObject(j);
     					if (!v.contains(value)) {
							v.add(value);
       				}
					}
				} else if (o instanceof ArrayList) {
    				ArrayList ag = (ArrayList)o;
        			int n = ag.size();
        			for (int j = 0; j < n; j++) {
     					Object value = ag.get(j);
     					if (!v.contains(value)) {
							v.add(value);
       				}
           		}
				} else if (o instanceof Object[]) {
    				Object[] ag = (Object[])o;
        			int n = ag.length;
        			for (int j = 0; j < n; j++) {
     					Object value = ag[j];
     					if (!v.contains(value)) {
							v.add(value);
       				}
           		}
    			} else {
  					if (!v.contains(o)) {
						v.add(o);
					}
				}
			}
		}
		if (noMapping) {
			rv = null;
		} else {
			rv = v.toArray();
		}
		return rv;
	}

	// mapped users support methods
// 	private void constructUsedAttributeMappings(EEntity_definition entity, AAttribute_mapping attributeMappings) throws SdaiException {
// 		CAttribute_mapping.usedinData_type(null, entity, mappingDomain, attributeMappings);
// 		if(entity.testSupertypes(null)) {
// 			AEntity_definition supertypes = entity.getSupertypes(null);
// 			SdaiIterator supertypeIterator = supertypes.createIterator();
// 			while(supertypeIterator.next()) {
// 				EEntity_definition supertype = supertypes.getCurrentMember(supertypeIterator);
// 				constructUsedAttributeMappings(supertype, attributeMappings);
// 			}
// 		}
// 	}

	static long findMappedUsersMillis = 0;//FIXME:time
// 	AEntity findMappedUsersOld(EEntity instance, EEntity_mapping sourceType, AAttribute_mapping attributeMappings, AAttribute_mapping users, int mode) throws SdaiException {
// 		long startMillis = System.currentTimeMillis();//FIXME:time
// 		if(attributeMappings == null) {
// 			attributeMappings = new AAttribute_mapping();
// 			constructUsedAttributeMappings(sourceType.getSource(null), attributeMappings);
// 		}
// 		Object ids[] = null;
// 		boolean useCacheData = users == null;
// 		if (useCacheData) { //FIXME: Cache is used only if users is null. Best it would support both
// 			ids = new Object[1/*3*/ + attributeMappings.getMemberCount()];
// 			ids[0] = instance;
// 			//ids[1] = sourceType;
// 			//ids[2] = new Integer(mode);
// 			int i = 1/*3*/;
// 			SdaiIterator attributeMappingIter = attributeMappings.createIterator();
// 			while(attributeMappingIter.next()) {
// 				ids[i++] = attributeMappings.getCurrentMember(attributeMappingIter);
// 			}
// // 			ids = new Object[] { instance, sourceType, attributeMappings, new Integer(mode) };
// 			if(findMappedUsersCache != null) {
// 				Object value = findMappedUsersCache.getValueByIds(ids);
// 				if (value != null) {
// 					findMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
// 					return (AEntity)(value == MethodCallsCache.nullValue ? null : value);
// 				}
// 			}
// 		} else {
// 			findMappedUsersCache.incCallsCount();
// 		}

// 		AEntity usersFound = new AEntity();
// 		HashMap usersCollected = new HashMap();
// 		if(users != null) users.clear();
// 		AEntity startInstances = new AEntity();
// 		startInstances.addByIndex(1, instance);
// 		AEntity singleMappingUsers = new AEntity();
// 		SdaiIterator attrMappingIterator = attributeMappings.createIterator();
// 		while(attrMappingIterator.next()) {
// 			EAttribute_mapping attributeMapping =
// 				attributeMappings.getCurrentMember(attrMappingIterator);

// 			Tree pathTree = collectPathFromConstraint(attributeMapping);

// 			if (pathTree != null) {
// 				singleMappingUsers = getSingleMappingUsers(instance, attributeMapping, pathTree);
// 				SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();
// 				while(singleMapUserIterator.next()) {
// 					EEntity mappingUser =
// 						singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 					if(containsInstance(instance, mappingUser, attributeMapping, mode)) {
// 						String mappingUserKey = mappingUser.getPersistentLabel();
// 						Object existingMappingUser = usersCollected.get(mappingUserKey);
// 						if(existingMappingUser == null) {
// 							LinkedList mappingUserList = new LinkedList();
// 							mappingUserList.add(mappingUser);
// 							mappingUserList.add(attributeMapping);
// 							usersCollected.put(mappingUserKey, mappingUserList);
// 						} else {
// 							LinkedList mappingUserList = (LinkedList)existingMappingUser;
// 							EEntity_definition newDefinition =
// 							attributeMapping.getParent_entity(null).getSource(null);
// 							boolean mappingResolved = false;
// 							ListIterator userIter = mappingUserList.listIterator();
// 							userIter.next(); // First element is instance
// 							while(userIter.hasNext()) {
// 								EAttribute_mapping mapping = (EAttribute_mapping)userIter.next();
// 								EEntity_definition oldDefinition =
// 								mapping.getParent_entity(null).getSource(null);
// 								if(getSupertypeOfLevel(newDefinition, oldDefinition, 0) >= 0) {
// 									mappingResolved = true;
// 									break;
// 								} else if(getSupertypeOfLevel(oldDefinition, newDefinition, 0) > 0) {
// 									userIter.set(attributeMapping);
// 									mappingResolved = true;
// 									break;
// 								}
// 							}
// 							if(!mappingResolved) {
// 								mappingUserList.add(attributeMapping);
// 							}
// 						}
// 					}
// 				}
// 			}
// 		}
// 		Iterator usersCollectedIter = usersCollected.values().iterator();
// 		while(usersCollectedIter.hasNext()) {
// 			LinkedList userCollected = (LinkedList)usersCollectedIter.next();
// 			Iterator userIter = userCollected.iterator();
// 			EEntity mappingUser = (EEntity)userIter.next();
// 			if(users != null) {
// 				while(userIter.hasNext()) {
// 					EAttribute_mapping mapping = (EAttribute_mapping)userIter.next();
// 					usersFound.addByIndex(1, mappingUser);
// 					users.addByIndex(1, mapping);
// 				}
// 			} else {
// 				usersFound.addByIndex(1, mappingUser);
// 			}
// 		}

// 		if (useCacheData && findMappedUsersCache != null) { //FIXME: see above about users == null
// 			findMappedUsersCache.setValueByIds(ids, usersFound);
// 		}

// 		findMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
// 		return usersFound;
// 	}

	static long testMappedUsersMillis = 0;//FIXME:time
// 	boolean testMappedUsersOld(EEntity instance, EEntity_mapping sourceType,
// 	AAttribute_mapping attributeMappings, int mode) throws SdaiException {
// 		long startMillis = System.currentTimeMillis();//FIXME:time
// 		if(attributeMappings == null) {
// 			attributeMappings = new AAttribute_mapping();
// 			constructUsedAttributeMappings(sourceType.getSource(null), attributeMappings);
// 		}
// 		Object ids[] = null;
// 		boolean useCacheData = true;
// // 		if (useCacheData) {
// // 			ids = new Object[1/*3*/ + attributeMappings.getMemberCount()];
// // 			ids[0] = instance;
// // 			//ids[1] = sourceType;
// // 			//ids[2] = new Integer(mode);
// // 			int i = 1/*3*/;
// // 			SdaiIterator attributeMappingIter = attributeMappings.createIterator();
// // 			while(attributeMappingIter.next()) {
// // 				ids[i++] = attributeMappings.getCurrentMember(attributeMappingIter);
// // 			}
// // // 			ids = new Object[] { instance, sourceType, attributeMappings, new Integer(mode) };
// // 			if(testMappedUsersCache != null) {
// // 				Object value = testMappedUsersCache.getValueByIds(ids);
// // 				if (value != null) {
// // 					testMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
// // 					return value != MethodCallsCache.nullValue;
// // 				}
// // 			}
// // 		}

// 		AEntity startInstances = new AEntity();
// 		startInstances.addByIndex(1, instance);
// 		AEntity singleMappingUsers = new AEntity();
// 		SdaiIterator attrMappingIterator = attributeMappings.createIterator();
// 		while(attrMappingIterator.next()) {
// 			EAttribute_mapping attributeMapping =
// 				attributeMappings.getCurrentMember(attrMappingIterator);
// 			if(attributeMapping.testPath(null)) {
// 				Tree pathTree = collectPathFromConstraint(attributeMapping);

// 				if (pathTree != null) {
// 					singleMappingUsers = getSingleMappingUsers(instance, attributeMapping, pathTree);

// 					SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();
// 					while(singleMapUserIterator.next()) {
// 						EEntity mappingUser =
// 							singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 						if(containsInstance(instance, mappingUser, attributeMapping, mode)) {
// // 							if (useCacheData && testMappedUsersCache != null) {
// // 								testMappedUsersCache.setValueByIds(ids, instance);
// // 							}
// // 							testMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
// 							return true;
// 						}
// 					}
// 				}
// 			}
// 		}

// // 		if (useCacheData && testMappedUsersCache != null) {
// // 			testMappedUsersCache.setValueByIds(ids, null);
// // 		}
// // 		testMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
//         return false;
// 	}

// 	static long containsInstanceEntityMillis = 0;//FIXME:time
// 	static long containsInstanceAttributeMillis = 0;//FIXME:time
// 	private boolean containsInstance(EEntity instance, EEntity mappingUser,
// 	EAttribute_mapping attributeMapping, int mode) throws SdaiException {
// //		return testMappedAttribute(mappingUser, attributeMapping, mode)
// //			&& testMappedEntity(mappingUser, attributeMapping.getParent_entity(null),mode);
// // 		long startMillis1 = System.currentTimeMillis();//FIXME:time
// 		if(!testMappedEntity(mappingUser, attributeMapping.getParent_entity(null), mode)) {
// // 			containsInstanceEntityMillis += System.currentTimeMillis() - startMillis1;//FIXME:time
// 			return false;
// 		}
// // 		containsInstanceEntityMillis += System.currentTimeMillis() - startMillis1;//FIXME:time
// // 		long startMillis2 = System.currentTimeMillis();//FIXME:time
// 		Object attributeValue = getMappedAttribute(mappingUser, attributeMapping, mode);
// // 		containsInstanceAttributeMillis += System.currentTimeMillis() - startMillis2;//FIXME:time
// 		if(attributeValue instanceof EEntity) {
// 			return attributeValue == instance;
// 		} else if(attributeValue instanceof AEntity) {
// 			SdaiIterator attributeValueIter = ((AEntity)attributeValue).createIterator();
// 			while(attributeValueIter.next()) {
// 				Object oneValue = ((AEntity)attributeValue).getCurrentMemberObject(attributeValueIter);
// 				if(oneValue == instance) return true;
// 			}
// 		}
// 		return false;
// 	}

// 	private void collectDerivedInstancesForUsers(AEntity instances, EAttribute_mapping attributeMapping,
// 	AEntity instanceAggreg)	throws SdaiException {
// 		if(attributeMapping.testDomain(null)) {
// 			EEntity domain = attributeMapping.getDomain(null);
// 			if(domain instanceof EEntity_mapping) {
// 				ADerived_variant_entity_mapping derivedVariants =
// 					new ADerived_variant_entity_mapping();
// 				CDerived_variant_entity_mapping.usedinRelating(null, (EEntity_mapping)domain,
// 					mappingDomain, derivedVariants);
// 				SdaiIterator derivedVariantIter = derivedVariants.createIterator();
// 				while(derivedVariantIter.next()) {
// 					EDerived_variant_entity_mapping derivedVariant =
// 						derivedVariants.getCurrentMember(derivedVariantIter);
// 					AEntity derivedInstanceAggreg = instances;
// 					if(derivedVariant.testPath(null)) {
// 						AAttribute_mapping_path_select pathElements = derivedVariant.getPath(null);
// 						SdaiIterator pathElementIterator = pathElements.createIterator();
// 						pathElementIterator.end();
// 						while(pathElementIterator.previous()
// 							  && derivedInstanceAggreg.getMemberCount() > 0) {
// 							EEntity pathElement =
// 								pathElements.getCurrentMember(pathElementIterator);
// 							derivedInstanceAggreg =
// 								getPathElementUsers(pathElement, derivedInstanceAggreg);
// 						}
// 					}
// 					EEntity derivedConstraints = derivedVariant.testConstraints(null) ? 
// 						derivedVariant.getConstraints(null) : null;
// 					SdaiIterator instanceIter = derivedInstanceAggreg.createIterator();
// 					while(instanceIter.next()) {
// 						EEntity derivedInstance =
// 							derivedInstanceAggreg.getCurrentMemberEntity(instanceIter);
// 						if(derivedConstraints == null 
// 						   || testMappingPath(derivedInstance, derivedConstraints)) {
// 							instanceAggreg.addByIndex(1, derivedInstance);
// 						}
// 					}
// 				}
// 			}
// 		}
// 	}

// 	AEntity getPathElementInverseUsers(EEntity pathElement, AEntity singleMappingUsers)
// 	throws SdaiException {
// 		if(pathElement instanceof EInverse_attribute_constraint) {
// 			return getPathElementInverseUsers(
// 			((EInverse_attribute_constraint) pathElement).getInverted_attribute(null),
// 			singleMappingUsers);
// 		} else if(pathElement instanceof ESelect_constraint) {
// 			return getPathElementInverseUsers(
// 			((ESelect_constraint) pathElement).getAttribute(null),
// 			singleMappingUsers);
// 		} else if(pathElement instanceof EEntity_constraint) {
// 			return getPathElementInverseUsers(
// 			((EEntity_constraint) pathElement).getAttribute(null),
// 			singleMappingUsers);
// 		} else if(pathElement instanceof EAggregate_member_constraint) {
// 			return getPathElementInverseUsers(
// 			((EAggregate_member_constraint) pathElement).getAttribute(null),
// 			singleMappingUsers);
//         } else if(pathElement instanceof EAttribute_value_constraint) {
//             return getPathElementInverseUsers(
// 			((EAttribute_value_constraint) pathElement).getAttribute(null),
// 			singleMappingUsers);
// 		} else if(pathElement instanceof EAttribute) {
// 			AEntity nextMappingUsers = new AEntity();
// 			SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();
// 			while(singleMapUserIterator.next()) {
// 				EAttribute attribute = (EAttribute)pathElement;
// 				EEntity mappingUser =
// 					singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 				if(mappingUser.isKindOf(attribute.getParent_entity(null))) {
// 					EDefined_type[] types = new EDefined_type[30];
//                     if(mappingUser.testAttribute(attribute, types) != 0) {
// 						Object userObject = mappingUser.get_object(attribute);
// 						if(userObject instanceof AEntity) {
// 							AEntity userAggregate = (AEntity)userObject;
// 							SdaiIterator userIterator = userAggregate.createIterator();
// 							while(userIterator.next()) {
// 								EEntity userEntity =
// 								userAggregate.getCurrentMemberEntity(userIterator);
// 								nextMappingUsers.addByIndex(1, userEntity);
// 							}
// 						} else if(userObject instanceof EEntity) {
// 							EEntity userEntity = (EEntity)userObject;
// 							nextMappingUsers.addByIndex(1, userEntity);
// 						}
// 					} else if(!(attribute instanceof EExplicit_attribute)
// 					|| !((EExplicit_attribute)attribute).getOptional_flag(null)) {
//                         String errText = "Mandatory attribute should be set in mapping operation." +
// 							"\nInstance: " + mappingUser + "\nAttribute: " + attribute + "\n";
// //                        throw new SdaiException(SdaiException.VA_NSET, mappingUser, "\n" + errText);
//                         System.out.println(errText);
// 					}
// 				}
// 			}
// 			return nextMappingUsers;
// 		} else {
// 			throw new SdaiException(SdaiException.SY_ERR, "Path element of unsupported type");
// 		}
// 	}

// 	AEntity getPathElementUsers(EEntity pathElement, AEntity singleMappingUsers) throws SdaiException {
// 		if(pathElement instanceof EPath_constraint) {
// 			return getPathElementUsers(
// 				((EPath_constraint)pathElement).getElement1(null),
// 				singleMappingUsers);
// 		} else if(pathElement instanceof EInverse_attribute_constraint) {
// 			return getPathElementInverseUsers(
// 				((EInverse_attribute_constraint)pathElement).getInverted_attribute(null),
// 				singleMappingUsers);
// 		} else if(pathElement instanceof ESelect_constraint) {
// 			return getPathElementUsers(
// 				((ESelect_constraint)pathElement).getAttribute(null),
// 				singleMappingUsers);
// 		} else if(pathElement instanceof EEntity_constraint) {
// 			EEntity_definition domain = ((EEntity_constraint)pathElement).getDomain(null);
// 			SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();
// 			AEntity validInstances = new AEntity();
// 			boolean exact = pathElement instanceof EExact_entity_constraint;
// 			while(singleMapUserIterator.next()) {
// 				EEntity mappingUser =
// 					singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 				if((exact && mappingUser.isInstanceOf(domain))
// 				|| (!exact && mappingUser.isKindOf(domain))) {
// 					validInstances.addByIndex(1, mappingUser);
// 				}
// 			}
// 			return getPathElementUsers(
// 					((EEntity_constraint)pathElement).getAttribute(null),
// 					validInstances);
// 		} else if(pathElement instanceof EAggregate_member_constraint) {
// 			return getPathElementUsers(
// 					((EAggregate_member_constraint)pathElement).getAttribute(null),
// 					singleMappingUsers);
// 		} else if(pathElement instanceof EAttribute) {
// 			AEntity nextMappingUsers = new AEntity();
// 			SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();
// 			while(singleMapUserIterator.next()) {
// 				EEntity mappingUser =
// 					singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 				mappingUser.findEntityInstanceUsedin((EAttribute)pathElement,
// 					targetDomain, nextMappingUsers);
// 			}
// 			return nextMappingUsers;
// 		} else if(pathElement instanceof EIntersection_constraint) {
// 			EIntersection_constraint constraint = (EIntersection_constraint) pathElement;
// 			AEntity nextMappingUsers = singleMappingUsers;
// 			if (constraint.testSubpaths(null)) {
// 				AConstraint_select subpaths = constraint.getSubpaths(null);
// 				SdaiIterator it = subpaths.createIterator();

// 				AEntity intersectedUsers = null;
// 				while (it.next()) {
// 					EEntity subConstraint = (EEntity) subpaths.getCurrentMemberObject(it);
// 					nextMappingUsers = getPathElementUsers(subConstraint, singleMappingUsers);

// 					if (intersectedUsers == null) {
// 						intersectedUsers = nextMappingUsers;
// 					} else { // intersect entities aggregates
// 						SdaiIterator intersectIt = intersectedUsers.createIterator();
// 						AEntity tmp = new AEntity();
// 						while (intersectIt.next()) {
// 							EEntity user = intersectedUsers.getCurrentMemberEntity(intersectIt);
// 							if (nextMappingUsers.isMember(user)) {
// 								tmp.addUnordered(user);
// 							}
// 						}
// 						intersectedUsers = tmp;
// 					}
// 				}
// 				nextMappingUsers = intersectedUsers;
// 			}

// 			return nextMappingUsers;
//         } else if(pathElement instanceof EAnd_constraint_relationship) {
//             EAnd_constraint_relationship acr = (EAnd_constraint_relationship) pathElement;
//             EEntity element1 = acr.getElement1(null);
// 			EEntity element2 = acr.getElement2(null);
// 			AEntity nextUsers1 = getPathElementUsers(element1, singleMappingUsers);
// 			AEntity nextUsers2 = getPathElementUsers(element2, singleMappingUsers);
//             AEntity tmp = new AEntity();

//             // intersect user aggregates
//             SdaiIterator it = nextUsers1.createIterator();
//             while (it.next()) {
//                 EEntity user1 = nextUsers1.getCurrentMemberEntity(it);
//                 if (nextUsers2.isMember(user1)) {
//                     tmp.addUnordered(user1);
//                 }
//             }
//             return tmp;
//         } else if(pathElement instanceof EOr_constraint_relationship) {
// 			EOr_constraint_relationship ocr = (EOr_constraint_relationship) pathElement;
//             EEntity element1 = ocr.getElement1(null);
// 			EEntity element2 = ocr.getElement2(null);
// 			AEntity nextUsers1 = getPathElementUsers(element1, singleMappingUsers);
// 			AEntity nextUsers2 = getPathElementUsers(element2, singleMappingUsers);

// 			// merge users aggregates
// 			SdaiIterator it = nextUsers2.createIterator();
// 			while (it.next()) {
// 				EEntity user2 = nextUsers2.getCurrentMemberEntity(it);
// 				if (!nextUsers1.isMember(user2)) {
// 					nextUsers1.addUnordered(user2);
// 				}
// 			}
//             return nextUsers1;
// 		} else if(pathElement instanceof EType_constraint) {
// 			EType_constraint constraint = (EType_constraint) pathElement;

// 			EEntity_definition mapping_entity = null;

// 			AEntity validInstances = singleMappingUsers;
// 			if (constraint.testDomain(null)) {
// 				mapping_entity = constraint.getDomain(null);
// 				SdaiIterator singleMapUserIterator = singleMappingUsers.createIterator();

// 				validInstances = new AEntity();
// 				boolean exact = pathElement instanceof EExact_type_constraint;
// 				while(singleMapUserIterator.next()) {
// 					EEntity mappingUser =
// 						singleMappingUsers.getCurrentMemberEntity(singleMapUserIterator);
// 					if((exact && mappingUser.isInstanceOf(mapping_entity))
// 					|| (!exact && mappingUser.isKindOf(mapping_entity))) {
// 						validInstances.addByIndex(1, mappingUser);
// 					}
// 				}
// 			}

// 			if (constraint.testConstraints(null)) {
// 				EEntity subConstraint = constraint.getConstraints(null);
// 				AEntity nextMappingUsers = getPathElementUsers(subConstraint, validInstances);
// 				return nextMappingUsers;
// 			}

// 			return validInstances;
// 		} else if (pathElement instanceof EAttribute_value_constraint) {
// 			EAttribute_value_constraint avc = (EAttribute_value_constraint) pathElement;
//             EEntity attrObj = avc.getAttribute(null);
//             EDefined_type[] selTypes = null;
//             if (attrObj instanceof EAttribute) {
//                 attrObj = avc.getAttribute(null);
//             } else if (attrObj instanceof ESelect_constraint) {
//                 ESelect_constraint sc = (ESelect_constraint) attrObj;
//                 ADefined_type stypes = sc.getData_type(null);
//                 selTypes = new EDefined_type[stypes.getMemberCount()];
//                 SdaiIterator it = stypes.createIterator();
//                 int i = 0;
//                 while (it.next()) {
//                     selTypes[i] = stypes.getCurrentMember(it);
//                     i++;
//                 }
//                 attrObj = sc.getAttribute(null);
//             }

//             if (!(attrObj instanceof EAttribute)) {
// 				throw new SdaiException(SdaiException.SY_ERR, "Path element of unsupported type: " + pathElement);
// 			}
// 			EAttribute attribute = (EAttribute) attrObj;

//             boolean isString = pathElement instanceof EString_constraint;
//             boolean isReal = false;

//             if (!isString) {
//                 isReal = pathElement instanceof EReal_constraint;
//             }

//             String strConstraintValue = null;
//             double realConstraintValue = 0;
//             if (isString) {
//                 strConstraintValue = ((EString_constraint) avc).getConstraint_value(null);
//             } else if (isReal) {
//                 realConstraintValue = ((EReal_constraint) avc).getConstraint_value(null);
//             } else {
//                 throw new SdaiException(SdaiException.ED_NVLD, "This type of constraint isn't implemented in getPathElementUsers(): " + avc);
//             }

// 			AEntity nextMappingUsers = new AEntity();
// 			SdaiIterator it = singleMappingUsers.createIterator();
// 			while (it.next()) {
// 				EEntity user = singleMappingUsers.getCurrentMemberEntity(it);
//                 if (isString) {
//                     String value = (String) user.get_object(attribute);
//                     if (value.equals(strConstraintValue)) {
//                         nextMappingUsers.addUnordered(user);
//                     }
//                 } else if (isReal) {
//                     double value = (double) user.get_double(attribute);
//                     if (value == realConstraintValue) {
//                         nextMappingUsers.addUnordered(user);
//                     }
//                 }
// 			}
// 			return nextMappingUsers;
// 		} else {
// 			throw new SdaiException(SdaiException.SY_ERR, "Path element of unsupported type: " + pathElement);
// 		}
// 	}

	static int getSupertypeOfLevel(EEntity_definition thisEntity, 
								   EEntity_definition otherEntity, int level) throws SdaiException {
		if(thisEntity == otherEntity) {
			return level;
		}
		if(otherEntity.testSupertypes(null)) {
			AEntity_definition supertypes = otherEntity.getSupertypes(null);
			SdaiIterator supertypeIterator = supertypes.createIterator();
			while(supertypeIterator.next()) {
				EEntity_definition supertype = supertypes.getCurrentMember(supertypeIterator);
				int supertypeLevel = getSupertypeOfLevel(thisEntity, supertype, level + 1);
				if(supertypeLevel >= 0) {
					return supertypeLevel;
				}
			}
		}
		return -1;
	}

	// Attribute mapping support

	/**
	 * Returns value of attribute_mapping. The target_instance must be mapping of
	 * entity_mapping of which attribute_mapping is get. It returns value of element to which
	 * attribute_mapping is mapped. If mapping of attribute_mapping is not met it returns
	 * null.
	 * @param targetInstance target instance to which attribute parent
	 * entity_mapping is mapped
	 * @param sourceAttribute source attribute which mapping are tested
	 * @param targetDomain a target instances domain where to search instances to
	 * satisfy mapping constraints.  It may be null. Then owning model of
	 * targetInstance will be used as domain.
	 * @param mappingDomain a domain for mapping constraints, target and source
	 * schemas. It may be null. Then owning model of sourceAttribute will be used as
	 * domain.
	 * @return value of source attribute, null if attribute_mapping reference path is not met.
	 */
	Object getMappedAttribute(jsdai.lang.EEntity targetInstance, EGeneric_attribute_mapping sourceAttribute, int mode, boolean useCacheData) throws jsdai.lang.SdaiException {
		Object rv = null;
		if ((targetInstance != null) && (sourceAttribute != null)) {
			EEntity entityMapping = sourceAttribute.getParent_entity(null).getTarget(null);
			EEntity_definition startType;
			if (entityMapping instanceof EAttribute) {
				startType = ((EExplicit_attribute)entityMapping).getParent_entity(null);
			} else {
				startType = (EEntity_definition)entityMapping;
			}
			if (!targetInstance.isKindOf(startType)) {// isInstanceOf replace with isKindOf
				return rv;
			}
			//			if (testMappedAttribute(targetInstance, sourceAttribute, targetDomain, mappingDomain)) {
			if (sourceAttribute instanceof EAttribute_mapping) {
				rv = getMappedAttribute(targetInstance, (EAttribute_mapping)sourceAttribute, mode, useCacheData);
			} else if (sourceAttribute instanceof EAttribute_mapping_value) {
				if (testMappedAttribute(targetInstance, sourceAttribute, mode)) {
					if(sourceAttribute instanceof EAttribute_mapping_enumeration_value)
						rv = ((EAttribute_mapping_enumeration_value)sourceAttribute).getMapped_value(null);
					else if(sourceAttribute instanceof EAttribute_mapping_boolean_value)
						rv = Boolean.valueOf(
						((EAttribute_mapping_boolean_value)sourceAttribute).getMapped_value(null));
					else if(sourceAttribute instanceof EAttribute_mapping_logical_value)
						rv = new Integer(
						((EAttribute_mapping_logical_value)sourceAttribute).getMapped_value(null));
				}
			}
		}
		return rv;
	}

	Object getMappedAttribute(jsdai.lang.EEntity targetInstance, EAttribute_mapping sourceAttribute, int mode) throws SdaiException {
		return getMappedAttribute(targetInstance,  sourceAttribute, mode, true);
	}

	Object getMappedAttribute(jsdai.lang.EEntity targetInstance, EGeneric_attribute_mapping sourceAttribute, int mode) throws SdaiException {
		//FIXME: AM 	return getMappedAttribute(targetInstance,  sourceAttribute, mode, false);
		return getMappedAttribute(targetInstance,  sourceAttribute, mode, true);
	}

	static long getMappedAttributeMillis = 0;//FIXME:time
	static long getMappedAttributeEntityMillis = 0;//FIXME:time
	Object getMappedAttribute(jsdai.lang.EEntity targetInstance, EAttribute_mapping sourceAttribute, int mode, boolean useCacheData) throws SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		Object ids[] = null;
		if (useCacheData) {
			ids = new Object[] { targetInstance, sourceAttribute/*, new Integer(mode)*/ };
			if(getMappedAttributeCache != null) {
				Object value = getMappedAttributeCache.getValueByIds(ids);
				if (value != null) {
					getMappedAttributeMillis += System.currentTimeMillis() - startMillis;//FIXME:time
					return value == MethodCallsCache.nullValue ? null : value;
				}
			}
		}

		Object rv = null;
		List attributeValues = Collections.singletonList(targetInstance);

//        System.out.println(targetInstance.toString() + " " + sourceAttribute.toString());

		if (sourceAttribute.testConstraints(null)) {
//            long start = System.currentTimeMillis();
			EEntity constraints = sourceAttribute.getConstraints(null);
			attributeValues = recurseAttributeMapping(attributeValues, constraints, false, true);
			//goThroughPath2(attributeValues, constraints, useCacheData);
/*            long finish = System.currentTimeMillis();
            long estimatedTime = Go_ThroughPathCache.getEstimatedTime();
            go_ThroughPathCache.addEstimatedTime(finish - start);
*/
		}

		Iterator attributeValueIter = attributeValues.iterator();
		List finalAttributeValues = null;
		List nextAttributeValues = null;
		int attributeValueIndex = 0;
		EEntity_mapping domainMapping = null;
		ADerived_variant_entity_mapping derivedVariants = null;
		if((mode & DONT_USE_DERIVED_VARIANTS) == 0 && sourceAttribute.testDomain(null)) {
			EEntity domain = sourceAttribute.getDomain(null);
			if(domain instanceof EEntity_mapping) domainMapping = (EEntity_mapping)domain;
		}
		while(attributeValueIter.hasNext()) {
			Object attributeValue = attributeValueIter.next();

                if(testAttributeValueByMode(sourceAttribute, attributeValue, mode)) {
                    if(finalAttributeValues != null) {
                        finalAttributeValues.add(attributeValue);
                    }
                } else {
                    if(finalAttributeValues == null) {
                        finalAttributeValues = new LinkedList();
                        finalAttributeValues.addAll(attributeValues.subList(0, attributeValueIndex));
                    }
                    if(domainMapping != null) {
                        if(nextAttributeValues == null) {
                            derivedVariants = new ADerived_variant_entity_mapping();
                            CDerived_variant_entity_mapping.usedinRelating(null, domainMapping, 
																		   mappingDomain, derivedVariants);
                            if(derivedVariants.getMemberCount() > 0) {
                                // There should be only one derived variant
                                nextAttributeValues = new LinkedList();
                                nextAttributeValues.add(attributeValue);
                            } else {
                                domainMapping = null;
                            }
                        } else {
                            nextAttributeValues.add(attributeValue);
                        }
                    }
                }

			attributeValueIndex++;
		}
		if(finalAttributeValues == null) finalAttributeValues = attributeValues;
		if(nextAttributeValues != null) {
			SdaiIterator derivedVariantIter = derivedVariants.createIterator();
			while(derivedVariantIter.next()) {
				EDerived_variant_entity_mapping derivedVariant =
					derivedVariants.getCurrentMember(derivedVariantIter);
				attributeValues.clear();
				if(derivedVariant.testConstraints(null)) {
					EEntity derivedConstraints = derivedVariant.getConstraints(null);
					Iterator nextAttributeValueIter = nextAttributeValues.iterator();
					while(nextAttributeValueIter.hasNext()) {
						Object nextAttributeValue = nextAttributeValueIter.next();
						if(nextAttributeValue instanceof EEntity
						   && testMappingPath((EEntity)nextAttributeValue, derivedConstraints)) {
							attributeValues.add(nextAttributeValue);
						}
					}
					attributeValues = recurseAttributeMapping(nextAttributeValues, derivedConstraints,
															  false, true);
					//goThroughPath2(nextAttributeValues, derivedConstraints, 
					//								 useCacheData);
				} else {
					attributeValues.addAll(nextAttributeValues);
				}
				EEntity_mapping relatedMapping = derivedVariant.getRelated(null);
				attributeValueIter = attributeValues.iterator();
				while(attributeValueIter.hasNext()) {
					Object attributeValue = attributeValueIter.next();
					if(!(attributeValue instanceof EEntity)
					|| getMappedAttributeTestMappedEntity((EEntity)attributeValue, relatedMapping, mode)) {
						finalAttributeValues.add(attributeValue);
					}
				}
			}
		}
		switch(finalAttributeValues.size()) {
			case 0:
				rv = null;
				break;
			case 1:
				rv = finalAttributeValues.get(0);
				break;
			default:
				AEntity valueAggregate = new AEntity();
				Iterator finalAttributeValueIter = finalAttributeValues.iterator();
				rv = null;
				while(finalAttributeValueIter.hasNext()) {
					Object finalAttributeValue = finalAttributeValueIter.next();
					if(finalAttributeValue instanceof EEntity) {
						valueAggregate.addByIndex(1, (EEntity)finalAttributeValue, null);
					} else {
						rv = finalAttributeValues.toArray();
						break;
					}
				}
				if(rv == null) {
					rv = valueAggregate;
				}
				break;
		}

		if (useCacheData && getMappedAttributeCache != null) {
			getMappedAttributeCache.setValueByIds(ids, rv);
		}

		getMappedAttributeMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return rv;
	}

	private boolean getMappedAttributeTestMappedEntity(EEntity instance, 
													   EEntity_mapping targetType, 
													   int mode) throws SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		boolean result = testMappedEntity(instance, targetType, mode);
		getMappedAttributeEntityMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return result;
	}

	private List goThroughPath(List pathValues, Aggregate path) throws SdaiException {
		SdaiIterator i = path.createIterator();
		while (i.next() && pathValues.size() != 0) {
			EEntity pathElement = (EEntity)path.getCurrentMemberObject(i);
			List nextPathValues = new LinkedList();
			Iterator pathValueIter = pathValues.iterator();
			while(pathValueIter.hasNext()) {
				Object pathValue = pathValueIter.next();
				if(pathValue instanceof EEntity) {
					Object nextPathValue =
						getAttributeFolowing((EEntity)pathValue, pathElement);
					if(nextPathValue instanceof Aggregate) {
						Aggregate nextPathValueAggregate = (Aggregate)nextPathValue;
						SdaiIterator aggregIter = nextPathValueAggregate.createIterator();
						while (aggregIter.next()) {
							nextPathValues.add(nextPathValueAggregate.getCurrentMemberObject(aggregIter));
						}
					} else if(nextPathValue instanceof ArrayList) {
						nextPathValues.addAll((ArrayList)nextPathValue);
					} else if(nextPathValue instanceof Object[]) {
						nextPathValues.addAll(Arrays.asList((Object[])nextPathValue));
					} else {
						nextPathValues.add(nextPathValue);
					}
				}
			}
			pathValues = nextPathValues;
		}
		return pathValues;
	}

    private List getFollowingAttributes(List pathValues, jsdai.lang.EEntity constraints, boolean isNegation) throws SdaiException {
        int intConstraintValue = 0;
        boolean booleanConstraintValue = false;
        double doubleConstraintValue = 0;
        String stringConstraintValue = "";
        int typeOfConstraint = 0;

        if (constraints instanceof EInteger_constraint) {
            intConstraintValue = ((EInteger_constraint)constraints).getConstraint_value(null);
            typeOfConstraint = 1;
        } else if (constraints instanceof EBoolean_constraint) {
            booleanConstraintValue = ((EBoolean_constraint)constraints).getConstraint_value(null);
            typeOfConstraint = 2;
        } else if (constraints instanceof EReal_constraint) {
            doubleConstraintValue = ((EReal_constraint)constraints).getConstraint_value(null);
            typeOfConstraint = 3;
        } else if (constraints instanceof ELogical_constraint) {
            intConstraintValue = ((ELogical_constraint)constraints).getConstraint_value(null);
            typeOfConstraint = 4;
        } else if (constraints instanceof EString_constraint) {
            stringConstraintValue = ((EString_constraint)constraints).getConstraint_value(null);
            typeOfConstraint = 5;
        }

        List tmpList = new LinkedList();

        Iterator pathValueIter = pathValues.iterator();
		while(pathValueIter.hasNext()) {
    		Object targetInstance = pathValueIter.next();
            EConstraint_attribute ca = (EConstraint_attribute)constraints;
            Object o = getFollowingObject((EEntity) targetInstance, getAttribute(ca));

            if (o != null) {
                boolean equals = false;

                if (typeOfConstraint == 1) {
                    int va = ((Integer)o).intValue();
                    if ((va == intConstraintValue) != isNegation) {
                       equals = true;
                    }
                } else if (typeOfConstraint == 2) {
                    boolean va = ((Boolean)o).booleanValue();
                    if ((va == booleanConstraintValue) != isNegation) {
                        equals = true;
                    }
                } else if (typeOfConstraint == 3) {
                    double va = ((Double)o).doubleValue();
                    if ((va == doubleConstraintValue) != isNegation) {
                        equals = true;
                    }
                } else if (typeOfConstraint == 4) {
                    int va = ((Integer)o).intValue();
                    if ((va == intConstraintValue) != isNegation) {
                        equals = true;
                    }
                } else if (typeOfConstraint == 5) {
                    String va = (String)o;
                    if (va.equals(stringConstraintValue) != isNegation) {
                        equals = true;
                    }
                } else {
                    throw new SdaiException(0, null, "Constraint type not supported: " +
                                            constraints.toString());
                }

                if (equals) {
                    tmpList.add(targetInstance);
                }
            }
        }

        return tmpList;
    }

    private List getAttributes(List pathValues, jsdai.lang.EEntity constraints) throws SdaiException {
        List tmpList = new LinkedList();

        Iterator pathValueIter = pathValues.iterator();
		while(pathValueIter.hasNext()) {
    		EEntity targetInstance = (EEntity) pathValueIter.next();
            jsdai.dictionary.EAttribute l = (EAttribute) constraints;

            if (((CEntity)targetInstance).testAttributeFast(l, definedTypes) > 0) {
                tmpList.add(targetInstance.get_object(l));
            }
        }

        return tmpList;
    }

    private List makeConstraintStep(List pathValues, jsdai.lang.EEntity constraints) throws SdaiException {
        List tmpList = new LinkedList();

        Iterator pathValueIter = pathValues.iterator();
		while(pathValueIter.hasNext()) {
    		Object targetInstance = pathValueIter.next();
            Aggregate t;

            if (constraints instanceof EInverse_attribute_constraint) {
                t = getFollowingInvertedAttribute((EEntity) targetInstance, (EInverse_attribute_constraint)constraints);
            }
            else if (constraints instanceof EEntity_constraint) {
                t = getFollowingEntity((EEntity) targetInstance, (EEntity_constraint)constraints);
            }
            else if (constraints instanceof EAggregate_member_constraint) {
                t = getFollowingAggregateMember((EEntity) targetInstance, (EAggregate_member_constraint)constraints);
            }
            else {
                t = null;
            }

            if (t != null) {
    			SdaiIterator aggregIter = t.createIterator();
				while (aggregIter.next()) {
					tmpList.add(t.getCurrentMemberObject(aggregIter));
				}
            }
        }

        return tmpList;
    }

    private List filterByConstraint(List pathValues, jsdai.lang.EEntity constraints, jsdai.lang.EEntity orConstraints) throws SdaiException {
        List tmpList = new LinkedList();

        Iterator pathValueIter = pathValues.iterator();
		while(pathValueIter.hasNext()) {
    		Object targetInstance = pathValueIter.next();

            if (testMappingPath((EEntity) targetInstance, constraints)) {
                tmpList.add(targetInstance);
            } else {
                if (orConstraints != null) {
                    if (testMappingPath((EEntity) targetInstance, orConstraints)) {
                        tmpList.add(targetInstance);
                    }
                }
            }
        }

        return tmpList;
    }

    private List intersectLists(List list1, List list2) {
        List retVal = new LinkedList();

        Iterator list1Iter = list1.iterator();
        while (list1Iter.hasNext()) {
            Object pathValue1 = list1Iter.next();

            if (list2.contains(pathValue1)) {
                retVal.add(pathValue1);
            }
        }

        return retVal;
    }

    private List mergeLists(List list1, List list2) {
        List retVal = list2;

        Iterator list1Iter = list1.iterator();
        while (list1Iter.hasNext()) {
            Object pathValue1 = list1Iter.next();

            if (!list2.contains(pathValue1)) {
                list2.add(pathValue1);
            }
        }

        return retVal;
    }

    private List intersectConstraints(List pathValues, EIntersection_constraint constraints) throws SdaiException {
        AConstraint_select subpaths = constraints.getSubpaths(null);

        SdaiIterator pathsIterator = subpaths.createIterator();

        List retVal = null;
        boolean start = true;

        while (pathsIterator.next()) {
    		 EEntity subconstraint = (EEntity) subpaths.getCurrentMemberObject(pathsIterator);
             List tmpList = recurseAttributeMapping(pathValues, subconstraint, false, true);

             if (start) {
                retVal = tmpList;
				start = false;
             } else {
				retVal = intersectLists(tmpList, retVal);
             }
        }

        return retVal;
    }

    private List filterBySelectConstraint(List pathValues, ESelect_constraint constraints) throws SdaiException {
        List tmpList = new LinkedList();

        Iterator pathValueIter = pathValues.iterator();
		while(pathValueIter.hasNext()) {
    		EAttribute targetInstance = (EAttribute) pathValueIter.next();
            EConstraint_attribute ca = (EConstraint_attribute)constraints;
            Object o = getFollowingObject((EEntity) targetInstance, getAttribute(ca));

            if (o != null) {
                jsdai.dictionary.EDefined_type adt[] = new jsdai.dictionary.EDefined_type[20];
                jsdai.dictionary.EAttribute l = (EAttribute)getAttribute(constraints);
				int n = ((CEntity)targetInstance).testAttributeFast(l, adt);
                if (n > 0) {
/*                    jsdai.dictionary.EDefined_type dt = adt[0];
                    ADefined_type cadt = constraints.getData_type(null);
                    jsdai.lang.SdaiIterator ci = cadt.createIterator();
                    boolean f = true;
                    while (ci.next()) {
                        EDefined_type cdt = cadt.getCurrentMember(ci);
                        f = f && (dt == cdt);
                    }
                    //
                    if (f) {
                        tmpList.add(o);
                    }*/
					if (isEqualDefinedTypes(adt, constraints.getData_type(null))) {
						tmpList.add(o);
					}
                }
            }
        }

        return tmpList;
    }

    private List recurseAttributeMapping(List pathValues, jsdai.lang.EEntity constraints, boolean isNegation, boolean mainPath) throws SdaiException {
        List rv = pathValues;

        if (constraints instanceof EAttribute) {
//            System.out.println("That's an EAttribute");
            rv = getAttributes(pathValues, constraints);
        }
        else if (constraints instanceof EPath_constraint) {
            pathValues = recurseAttributeMapping(pathValues, getElement1((EPath_constraint)constraints), isNegation, false);
            rv = recurseAttributeMapping(pathValues, getElement2((EPath_constraint)constraints), isNegation, mainPath);
        } else if (constraints instanceof EAnd_constraint_relationship) {
            pathValues = filterByConstraint(pathValues, getElement2((EAnd_constraint_relationship)constraints), null);
            rv = recurseAttributeMapping(pathValues, getElement1((EAnd_constraint_relationship)constraints), isNegation, mainPath);
        } else if (constraints instanceof EOr_constraint_relationship) {
            if (!mainPath) {
				rv = filterByConstraint(pathValues, getElement1((EOr_constraint_relationship)constraints),
					                    getElement2((EOr_constraint_relationship)constraints));
			} else {
				// merge lists
	             List tmpList1 = recurseAttributeMapping(pathValues, getElement1((EOr_constraint_relationship)constraints), false, mainPath);
		         List tmpList2 = recurseAttributeMapping(pathValues, getElement2((EOr_constraint_relationship)constraints), false, mainPath);
				 tmpList1 = mergeLists(tmpList1, tmpList2);
			}
        } else if (constraints instanceof EInverse_attribute_constraint) {
            rv = makeConstraintStep(pathValues, constraints);
        } else if (constraints instanceof EConstraint_attribute) {
            if (constraints instanceof EAttribute_value_constraint) {
                rv = getFollowingAttributes(pathValues, constraints, isNegation);
            } else if ((constraints instanceof EAggregate_member_constraint) ||
                      (constraints instanceof EEntity_constraint)) {
                rv = makeConstraintStep(pathValues, constraints);
            } else if (constraints instanceof ESelect_constraint) {
                rv = filterBySelectConstraint(pathValues, (ESelect_constraint) constraints);
            }
        } else if (constraints instanceof EEnd_of_path_constraint) {
			EEnd_of_path_constraint eopc = (EEnd_of_path_constraint) constraints;
			if (eopc.testConstraints(null)) {
				rv = filterByConstraint(pathValues, eopc.getConstraints(null), null);
			}
        } else if (constraints instanceof ENegation_constraint) {
			ENegation_constraint nc = (ENegation_constraint)constraints;
			if (nc.testConstraints(null)) {
				rv = recurseAttributeMapping(pathValues, nc.getConstraints(null), !isNegation, mainPath);
			}
        } else if (constraints instanceof EIntersection_constraint) {
            rv = intersectConstraints(pathValues, (EIntersection_constraint) constraints);
        } else if (constraints instanceof EType_constraint) {
			EType_constraint tc = (EType_constraint) constraints;
			EEntity_definition domain = tc.getDomain(null);
			LinkedList tmp = new LinkedList();
			boolean exact = constraints instanceof EExact_type_constraint;
			for (int i = 0; i < rv.size(); i++) {
				EEntity entity = (EEntity) rv.get(i);
				if((exact && entity.isInstanceOf(domain))
				|| (!exact && entity.isKindOf(domain))) {
					tmp.add(entity);
				}
			}
			if (tc.testConstraints(null)) {
				rv = recurseAttributeMapping(tmp, tc.getConstraints(null), isNegation, mainPath);
			} else {
				rv = tmp;
			}

        } else {
            throw new SdaiException(SdaiException.SY_ERR, "Unrecognized constraint: " + constraints);
        }

        return rv;
    }

//     private List goThroughPath2(List pathValues, EEntity constraints, boolean useCacheData) throws SdaiException {
// 		Object ids[] = null;
// 		if (useCacheData) {
// 			ids = new Object[] { pathValues, constraints };
// 			if(goThroughPathCache != null) {
// 				Object value = goThroughPathCache.getValueByIds(ids);
// 				if (value != null) {
// 					return (List)(value == MethodCallsCache.nullValue ? null : value);
// 				}
// 			}
// 		}

// 		List attributeValues = recurseAttributeMapping(pathValues, constraints, false, true);

// 		if (useCacheData && goThroughPathCache != null) {
// 			goThroughPathCache.setValueByIds(ids, attributeValues);
// 		}

// 		return attributeValues;
//     }

	/**
	 * Returns aggregate of values for source attribute. If mapping of attribute has several
	 * alternatives and these alternatives are satisfied by targetInstance, then this aggregate
	 * may contain several values.
	 * @param targetInstance an instance to which parent entity of attribute is
	 * mapped
	 * @param sourceAttribute source attribute which mappings are returned
	 * @param targetDomain a target instances domain where to search instances to
	 * satisfy mapping constraints.  It may be null. Then owning model of
	 * targetInstance will be used as domain.
	 * @param mappingDomain a domain for mapping constraints, target and source
	 * schemas. It may be null. Then owning model of sourceAttribute will be used as
	 * domain.
	 * @return aggregate of sourceAttribute values. They are target instances or simple
	 * values (like string, integer) that are mappings of sourceAttribute.
	 */
	Object[] getSourceAttribute(jsdai.lang.EEntity targetInstance, EAttribute sourceAttribute, int mode) throws jsdai.lang.SdaiException {
		Object rv[];
		if ((targetInstance == null) || (sourceAttribute == null)) {
			return new Object[0];
		}
		AGeneric_attribute_mapping aema = new AGeneric_attribute_mapping();
		jsdai.mapping.CGeneric_attribute_mapping.usedinSource(null, sourceAttribute, mappingDomain, aema);
		jsdai.lang.SdaiIterator i = aema.createIterator();
		ArrayList v = new ArrayList();
		while(i.next()) {
			EGeneric_attribute_mapping attributeMapping = aema.getCurrentMember(i);
			//			This is being tested in getMappedAttribute anyway
			//			if(attributeMapping.getParent_entity(null).getTarget(null) !=
			//			targetInstance.getInstanceType())
			//				continue;
			Object o = getMappedAttribute(targetInstance, attributeMapping, mode);
			if (o != null) {
				if (o instanceof jsdai.lang.Aggregate) {
					jsdai.lang.Aggregate ag = (jsdai.lang.Aggregate)o;
					jsdai.lang.SdaiIterator j = ag.createIterator();
					while (j.next()) {
						Object value = ag.getCurrentMemberObject(j);
						if (!v.contains(value)) {
							v.add(value);
						}
					}
				} else if (o instanceof ArrayList) {
					ArrayList ag = (ArrayList)o;
					int n = ag.size();
					for (int j = 0; j < n; j++) {
						Object value = ag.get(j);
						if (!v.contains(value)) {
							v.add(value);
						}
					}
				} else if (o instanceof Object[]) {
					Object[] ag = (Object[])o;
					int n = ag.length;
					for (int j = 0; j < n; j++) {
						Object value = ag[j];
						if (!v.contains(value)) {
							v.add(value);
						}
					}
				} else {
					if (!v.contains(o)) {
						v.add(o);
					}
				}
			}
		}
		return v.toArray();
	}

	boolean testAttributeValueByMode(EAttribute_mapping sourceAttribute, Object value, int mode) throws SdaiException {
        boolean rv = true;
		if((mode & DONT_CHECK_ATTRIBUTE_DOMAIN_MAPPING) == 0
		&& value instanceof EEntity && sourceAttribute.testDomain(null)) {
			EEntity sourceDomain = sourceAttribute.getDomain(null);
			if(sourceDomain instanceof EEntity_mapping) {
				EEntity_mapping sourceDomainMapping = (EEntity_mapping)sourceDomain;
				rv = getMappedAttributeTestMappedEntity((EEntity)value, sourceDomainMapping, mode);
			}
		}
		//		if ((mode == EEntity.MOST_SPECIFC_ENTITY) || (mode == EEntity.MANDATORY_ATTRIBUTES_SET)) {
		//			if (value instanceof EEntity) {
		//				if (sourceAttribute.testDomain(null)) {
		//					EEntity domain = sourceAttribute.getDomain(null);
		//					if (domain instanceof EEntity_mapping) {
		//						rv = testMappedEntity((EEntity)value, (EEntity_mapping)domain, mode);
		//					}
		//				}
		//			}
		//		}
		return rv;
	}

	static long testMappedAttributeMillis = 0;//FIXME:time
	/**
	 * Test whether a given targetInstance does fit to the contraints of attribute_mapping.
	 * @param targetInstance an instance to which parent entity_mapping of
	 * attribute_mapping is mapped
	 * @param sourceAttribute source attribute which mapping is tested
	 * @return true if this target instance has mapping of specified attribute mapping, false
	 * otherwise.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public boolean testMappedAttribute(jsdai.lang.EEntity targetInstance, EGeneric_attribute_mapping sourceAttribute, int mode) throws jsdai.lang.SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		Object ids[] = null;
		boolean useCacheData = true;
		if (useCacheData) {
			ids = new Object[] { targetInstance, sourceAttribute/*, new Integer(mode)*/ };
			if(testMappedAttributeCache != null) {
				Object value = testMappedAttributeCache.getValueByIds(ids);
				if (value != null) {
					testMappedAttributeMillis += System.currentTimeMillis() - startMillis;//FIXME:time
					return value != MethodCallsCache.nullValue;
				}
			}
		}

		boolean rv = false;
		if ((targetInstance != null) && (sourceAttribute != null)) {
			if (sourceAttribute.testConstraints(null)) {
				EEntity entityMapping = sourceAttribute.getParent_entity(null).getTarget(null);
				EEntity_definition startType;
				if (entityMapping instanceof EAttribute) {
					startType = ((EExplicit_attribute)entityMapping).getParent_entity(null);
				} else {
					startType = (EEntity_definition)entityMapping;
				}
                EEntity constraint = sourceAttribute.getConstraints(null);

                if (targetInstance.isKindOf(startType)) { // replace with isKindOf
					rv = testMappingPath(targetInstance, constraint);
				} else {
					rv = false;
				}
				if (rv && sourceAttribute instanceof EAttribute_mapping) {
					rv = testMappedAttribute(targetInstance, (EAttribute_mapping)sourceAttribute, mode);
				}
			} else {
				if (sourceAttribute instanceof EAttribute_mapping) {
					rv = testMappedAttribute(targetInstance, (EAttribute_mapping)sourceAttribute, mode);
				}
			}
		}

		if (useCacheData && testMappedAttributeCache != null) {
			testMappedAttributeCache.setValueByIds(ids, rv ? targetInstance : null);
		}

		testMappedAttributeMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return rv;
	}

	boolean testMappedAttribute(jsdai.lang.EEntity targetInstance, EAttribute_mapping sourceAttribute, int mode) throws jsdai.lang.SdaiException {
		Object o = getMappedAttribute(targetInstance, sourceAttribute, mode);
        return o != null;
		//		boolean rv;
		//		EEntity entityMapping = sourceAttribute.getParent_entity(null).getTarget(null);
		//		EEntity_definition startType;
		//      Object tmp = null;
		//		if (entityMapping instanceof EAttribute) {
		//			startType = ((EExplicit_attribute)entityMapping).getParent_entity(null);
		//		} else {
		//			startType = (EEntity_definition)entityMapping;
		//		}
		//		if (!targetInstance.isKindOf(startType)) {// isInstanceOf replace with isKindOf
		//			return false;
		//		}
		//		if (!sourceAttribute.testPath(null)) {
		//			tmp = targetInstance;
		//		} else {
		//			tmp = targetInstance;
		//			jsdai.lang.Aggregate path = sourceAttribute.getPath(null);
		//			jsdai.lang.SdaiIterator i = path.createIterator();
		//			while (i.next()) {
		//				if (tmp instanceof jsdai.lang.Aggregate) {
		//					jsdai.lang.Aggregate aggregate = (jsdai.lang.Aggregate)tmp;
		//					if (aggregate.getMemberCount() == 0) {
		//						return false;
		//					}
		//					tmp = ((jsdai.lang.Aggregate)tmp).getByIndexObject(1); // there should be iteration throght the aggregate.
		//					tmp = getAttributeFolowing((jsdai.lang.EEntity)tmp, (jsdai.lang.EEntity)path.getCurrentMemberObject(i));
		//				} else {
		//					tmp = getAttributeFolowing((jsdai.lang.EEntity)tmp, (jsdai.lang.EEntity)path.getCurrentMemberObject(i));
		//				}
		//			}
		//      }
		//		rv = tmp != null;
		//		if (tmp != null) {
		//			rv = testAttributeValueByMode(sourceAttribute, tmp, mode);
		//		}
		//		return rv;
	}

	void collectPathFromConstraintRecursion(EEntity constraint, Tree tree) throws SdaiException {
		if (constraint instanceof EPath_constraint) {
			tree = tree.addChild(constraint);
			collectPathFromConstraintRecursion(getElement2((EPath_constraint)constraint), tree);
		} else if (constraint instanceof EAnd_constraint_relationship) {
			collectPathFromConstraintRecursion(getElement1((EAnd_constraint_relationship)constraint), tree);
		} else if (constraint instanceof EOr_constraint_relationship) {
			EEntity constraintTmp = getElement1((EOr_constraint_relationship)constraint);
			Tree child = tree.addChild(constraintTmp);
			collectPathFromConstraintRecursion(constraintTmp, child);

			constraintTmp = getElement2((EOr_constraint_relationship)constraint);
			child = tree.addChild(constraintTmp);
			collectPathFromConstraintRecursion(constraintTmp, child);
		} else if (constraint instanceof EIntersection_constraint) {
	        AConstraint_select subpaths = ((EIntersection_constraint) constraint).getSubpaths(null);
	        SdaiIterator pathsIterator = subpaths.createIterator();
			while (pathsIterator.next()) {
				EEntity subconstraint = (EEntity) subpaths.getCurrentMemberObject(pathsIterator);
				Tree child = tree.addChild(subconstraint);
				collectPathFromConstraintRecursion(subconstraint, child);
			}
		} else if (constraint instanceof ENegation_constraint) {
			collectPathFromConstraintRecursion(((ENegation_constraint)constraint).getConstraints(null), tree);
		} else if (constraint instanceof EAggregate_member_constraint) {
			tree = tree.addChild(constraint);
			EEntity subConstraint = ((EAggregate_member_constraint)constraint).getAttribute(null);

			if (!((subConstraint instanceof EAttribute) ||
			    (subConstraint instanceof EInverse_attribute_constraint))) {
				collectPathFromConstraintRecursion(subConstraint, tree);
			}
		} else if ((constraint instanceof EEntity_constraint) ||
				  (constraint instanceof EAttribute) ||
				  (constraint instanceof EInverse_attribute_constraint)) {
			tree.addChild(constraint);
		} else if (constraint instanceof EType_constraint) {
			tree.addChild(constraint);
		} else if ((constraint instanceof EEnd_of_path_constraint) ||
				  (constraint instanceof ESelect_constraint) ||
				  (constraint instanceof EAttribute_value_constraint)) {
			return;
		} else {
			System.out.println("This type of constraint isn't involved in path construction:");
			System.out.println(constraint.toString());
		}
	}

	Tree collectPathFromConstraint(EAttribute_mapping attributeMapping) throws SdaiException {
		Tree rv = new Tree();
		if (attributeMapping.testConstraints(null)) {
			collectPathFromConstraintRecursion(attributeMapping.getConstraints(null), rv);
		}
		return rv;
	}

// 	AEntity getNextSingleMappingUsers(EEntity instance, EAttribute_mapping attributeMapping, Tree treeTop) throws SdaiException {
// 		AEntity rv = null;

// 		if (treeTop.containsChildren()) {
// 			AEntity nextMappingUsers = new AEntity();
// 			int i, j;
// 			for (i = 0; i < treeTop.childrenCount(); i++) {
// 				Tree child = treeTop.child(i);
// 				AEntity tmp = getNextSingleMappingUsers(instance, attributeMapping, child);
// 				EEntity pathElement = (EEntity) child.getNode();
// 				tmp = getPathElementUsers(pathElement, tmp);

// 				SdaiIterator it = new SdaiIterator(tmp);

// 				while (it.next()) {
// 					EEntity elem = (EEntity) tmp.getCurrentMemberObject(it);
// 					if (!nextMappingUsers.isMember(elem, null)) {
// 						nextMappingUsers.addUnordered(elem, null);
// 					}
// 				}
// 			}
// 			rv = nextMappingUsers;
// 		} else {
// 			rv = new AEntity();
// 			rv.clear();
// 			rv.addByIndex(1, instance);
// 			AEntity startInstances = new AEntity();
// 			startInstances.clear();
// 			startInstances.addByIndex(1, instance);
// 			collectDerivedInstancesForUsers(startInstances, attributeMapping, rv);
// 		}

// 		return rv;
// 	}

// 	AEntity getSingleMappingUsers(EEntity instance, EAttribute_mapping attributeMapping, Tree treeTop) throws SdaiException {
// 		AEntity rv = getNextSingleMappingUsers(instance, attributeMapping, treeTop);
// 		return rv;
// 	}

	//New version support methods

	void attributePathClear() {
		attributePathTop = -1;
	}

	void attributePathPush(int member) {
		members[++attributePathTop] = member;
		dataTypes[attributePathTop] = null;
	}

	void attributePathPush(ADefined_type dataType) {
		dataTypes[++attributePathTop] = dataType;
	}

	void attributePathPop() {
		if(attributePathTop >= 0) {
			dataTypes[attributePathTop--] = null;
		}
	}

// 	int getAttributePathTop() {
// 		return attributePathTop;
// 	}

	boolean isAttributeMemberAt(int position) {
		return dataTypes[position] == null;
	}

	int getAttributeMemberAt(int position) {
		return members[position];
	}

	ADefined_type getAttributeDataTypeAt(int position) {
		return dataTypes[position];
	}

	/**
	 * Extra requirement calling this method:
	 * selectPath field has to be filled in making a call like this:<br/>
	 * <code>instance.testAttribute(attribute, selectPath);</code>
	 * @param pathValue original attribute value obtained by <code>instance.get_object(attribute);</code>
	 * @param pathPosition path walking starting position
	 * which should always be 0 except when called recursively
	 * @return resulting attribute value as <code>Object</code>
	 * @exception SdaiException if an error occurs
	 */
	private Object followAttributePath(Object pathValue, int pathPosition) throws SdaiException {
		for(; pathPosition <= attributePathTop; pathPosition++) {
			if(isAttributeMemberAt(pathPosition)) {
				int member = getAttributeMemberAt(pathPosition);
				if(!(pathValue instanceof Aggregate)) {
					pathValue = null;
					break;
				}
				Aggregate pathAggregate = (Aggregate)pathValue;
				if(member < 0) {
					Set pathValueSet = new HashSet();
					SdaiIterator pathAggIter = pathAggregate.createIterator();
					while(pathAggIter.next()) {
						pathAggregate.testCurrentMember(pathAggIter, selectPath);
						pathValue = 
							followAttributePath(pathAggregate.getCurrentMemberObject(pathAggIter), 
												pathPosition + 1);
						if(pathValue instanceof EEntity) {
							pathValueSet.add(pathValue);
						}
					}
					pathValue = pathValueSet;
					break;
				} else {
					if(pathAggregate.getMemberCount() < member) {
						pathValue = null;
						break;
					}
					pathAggregate.testByIndex(member, selectPath);
					pathValue = pathAggregate.getByIndexObject(member);
				}
			} else {
				ADefined_type dataType = getAttributeDataTypeAt(pathPosition);
				SdaiIterator dataTypeIter = dataType.createIterator();
				int selPathIdx = 0;
				for(; selectPath[selPathIdx] != null && dataTypeIter.next(); 
					selPathIdx++) {
					EDefined_type dataTypeElem = dataType.getCurrentMember(dataTypeIter);
					if(dataTypeElem != selectPath[selPathIdx]) {
						break;
					}
				}
				if(selectPath[selPathIdx] != null || selPathIdx != dataType.getMemberCount()) {
					pathValue = null;
					break;
				}
			}
		}
		return pathValue;
	}

	void attributeMapUsersForward(EAttribute attribute, Set instances) throws SdaiException {
		Set inputInstances = (Set)((HashSet)instances).clone();
		instances.clear();
		boolean dontTestPath = 
			attributePathTop < 0 
			|| (attributePathTop == 0 && isAttributeMemberAt(0) && getAttributeMemberAt(0) < 0);
		Iterator instanceIter = inputInstances.iterator();
		while(instanceIter.hasNext()) {
			EEntity instance = (EEntity)instanceIter.next();
			AEntity aggValues = new AEntity();
			instance.findEntityInstanceUsedin(attribute, targetDomain, aggValues);
			SdaiIterator aggValueIter = aggValues.createIterator();
			while(aggValueIter.next()) {
				EEntity aggValue = aggValues.getCurrentMemberEntity(aggValueIter);
				if(!dontTestPath) {
					aggValue.testAttribute(attribute, selectPath);
					Object pathValue = followAttributePath(aggValue.get_object(attribute), 0);
					if(pathValue instanceof Collection) {
						if(((Collection)pathValue).contains(instance)) {
							instances.add(aggValue);
						}
					} else {
						if(pathValue == instance) {
							instances.add(aggValue);
						}
					}
				} else {
					instances.add(aggValue);
				}
			}
		}
	}

	void attributeMapUsersBackward(EAttribute attribute, Set instances) throws SdaiException {
		Set inputInstances = (Set)((HashSet)instances).clone();
		instances.clear();
		Iterator instanceIter = inputInstances.iterator();
		while(instanceIter.hasNext()) {
			EEntity instance = (EEntity)instanceIter.next();
			if(((CEntity)instance).testAttributeFast(attribute, selectPath) > 0) {
				Object value = instance.get_object(attribute);
				if(attributePathTop >= 0) {
					value = followAttributePath(value, 0);
					if(value == null) {
						continue;
					}
					if(value instanceof Collection) {
						instances.addAll((Collection)value);
						continue;
					}
				}
				if(value instanceof EEntity) {
					instances.add(value);
				} else if(value instanceof AEntity) { // FIXME: Should aggregate support be enhanced?
					AEntity aggValues = (AEntity)value;
					SdaiIterator aggValueIter = aggValues.createIterator();
					while(aggValueIter.next()) {
						EEntity aggValue = aggValues.getCurrentMemberEntity(aggValueIter);
						instances.add(aggValue);
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR, "Unexpected value: " + value);
				}
			}
		}
	}

	AEntity findMappedUsers(EEntity instance, EEntity_mapping sourceType, 
							AAttribute_mapping attributeMappings, 
							AAttribute_mapping users, int mode) throws SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		Set instances = new HashSet();
		Map attributeInstances = new HashMap();
		defaultMode = mode;
		AEntity usersFound = new AEntity();
		int usersFoundIdx = 1;
		if(users != null) {
			users.clear();
		}
		Set attributeMappingSet = null;
		if(attributeMappings != null) {
			if(sourceType != null) {
				attributeMappingSet = new HashSet(attributeMappings.getMemberCount());
				SdaiIterator attributeMappingIter = attributeMappings.createIterator();
				while(attributeMappingIter.next()) {
					attributeMappingSet.add(attributeMappings.getCurrentMember(attributeMappingIter));
				}
			} else {
				// Fast implementation triggered by sourceType being null
				Set oneMappingInstances = new HashSet();
				SdaiIterator attributeMappingIter = attributeMappings.createIterator();
				if(users != null) {
					while(attributeMappingIter.next()) {
						EAttribute_mapping attributeMapping = 
							attributeMappings.getCurrentMember(attributeMappingIter);
						instances.clear();
						instances.add(instance);
						((MappingConstraintPath)attributeMapping).mapUsersForward(this, instances);
						instances.removeAll(oneMappingInstances);
						oneMappingInstances.addAll(instances);
						Iterator instanceIter = instances.iterator();
						while(instanceIter.hasNext()) {
							EEntity userInstance = (EEntity)instanceIter.next();
							usersFound.addByIndex(usersFoundIdx, userInstance);
							users.addByIndex(usersFoundIdx, attributeMapping);
							usersFoundIdx++;
						}
					}
				} else {
					while(attributeMappingIter.next()) {
						EAttribute_mapping attributeMapping = 
							attributeMappings.getCurrentMember(attributeMappingIter);
						instances.clear();
						instances.add(instance);
						((MappingConstraintPath)attributeMapping).mapUsersForward(this, instances);
						oneMappingInstances.addAll(instances);
					}
					Iterator instanceIter = oneMappingInstances.iterator();
					while(instanceIter.hasNext()) {
						EEntity userInstance = (EEntity)instanceIter.next();
						usersFound.addByIndex(usersFoundIdx++, userInstance);
					}
				}
				findMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
				return usersFound;
			}
		}
		Map attMappingsByAtt = getAttMappingsByAtt(sourceType);
		Iterator attMappingByAttIter = attMappingsByAtt.values().iterator();
		while(attMappingByAttIter.hasNext()) {
			List singleMappings = (List)attMappingByAttIter.next();
// 			System.out.print(instance + " " + sourceType + "(");
			attributeInstances.clear();
			ListIterator singleMappingIter = singleMappings.listIterator(singleMappings.size());
			while(singleMappingIter.hasPrevious()) {
				Set levelMappings = (Set)singleMappingIter.previous();
				if(levelMappings != null) {
// 					System.out.print(" (");
					Iterator levelMappingIter = levelMappings.iterator();
					while(levelMappingIter.hasNext()) {
						AttributeMappingAndChildren mappingAndChildren = 
							(AttributeMappingAndChildren)levelMappingIter.next();
						EAttribute_mapping attributeMapping = mappingAndChildren.mapping;
						if(attributeMappingSet == null || attributeMappingSet.contains(attributeMapping)) {
							instances.clear();
							instances.add(instance);
							((MappingConstraintPath)attributeMapping).mapUsersForward(this, instances);
							if(!instances.isEmpty()) {
// 								System.out.print(attributeMapping.getPersistentLabel() + "*" + 
// 												 instances.size() + "*");
								Set oneMappingInstances = new HashSet();
								Set children = mappingAndChildren.children;
								if(children != null) {
									Iterator childrenIter = children.iterator();
// 									System.out.print("{");
									while(childrenIter.hasNext()) {
										AttributeMappingAndChildren childAndChildren = 
											(AttributeMappingAndChildren)childrenIter.next();
// 										System.out.print("{" + childAndChildren + "}");
										Set childInstances = (Set)attributeInstances.get(childAndChildren);
										if(childInstances != null) {
											instances.removeAll(childInstances);
											oneMappingInstances.addAll(childInstances);
										}
									}
// 									System.out.print("}");
								}
								oneMappingInstances.addAll(instances);
								attributeInstances.put(mappingAndChildren, oneMappingInstances);
// 								System.out.print(instances.size()+"_"+oneMappingInstances.size()+" ");

								Iterator instanceIter = instances.iterator();
								while(instanceIter.hasNext()) {
									EEntity userInstance = (EEntity)instanceIter.next();
									usersFound.addByIndex(usersFoundIdx, userInstance);
									if(users != null) {
										users.addByIndex(usersFoundIdx, attributeMapping);
									}
									usersFoundIdx++;
								}
							}
						}
					}
// 					System.out.print(") ");
				}
			}
// 			System.out.println(")");
		}
		findMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return usersFound;
	}

	private boolean testMappedUsers(EEntity instance, EEntity_mapping sourceType,
									AAttribute_mapping attributeMappings, int mode) throws SdaiException {
		long startMillis = System.currentTimeMillis();//FIXME:time
		Set instances = new HashSet();
		defaultMode = mode;
		Map attMappingsByAtt = getAttMappingsByAtt(sourceType);
		Set attributeMappingSet = null;
		if(attributeMappings != null) {
			attributeMappingSet = new HashSet(attributeMappings.getMemberCount());
			SdaiIterator attributeMappingIter = attributeMappings.createIterator();
			while(attributeMappingIter.next()) {
				attributeMappingSet.add(attributeMappings.getCurrentMember(attributeMappingIter));
			}
		}
		Iterator attMappingByAttIter = attMappingsByAtt.values().iterator();
		while(attMappingByAttIter.hasNext()) {
			List singleMappings = (List)attMappingByAttIter.next();
			ListIterator singleMappingIter = singleMappings.listIterator(singleMappings.size());
			while(singleMappingIter.hasPrevious()) {
				Set levelMappings = (Set)singleMappingIter.previous();
				if(levelMappings != null) {
					Iterator levelMappingIter = levelMappings.iterator();
					while(levelMappingIter.hasNext()) {
						AttributeMappingAndChildren mappingAndChildren = 
							(AttributeMappingAndChildren)levelMappingIter.next();
						EAttribute_mapping attributeMapping = mappingAndChildren.mapping;
						if(attributeMappingSet == null || attributeMappingSet.contains(attributeMapping)) {
							instances.clear();
							instances.add(instance);
							((MappingConstraintPath)attributeMapping).mapUsersForward(this, instances);

							if(!instances.isEmpty()) {
								testMappedUsersMillis += 
									System.currentTimeMillis() - startMillis;//FIXME:time
								return true;
							}
						}
					}
				}
			}
		}
		testMappedUsersMillis += System.currentTimeMillis() - startMillis;//FIXME:time
		return false;
	}

	/**
	 * Returns a map of user attribute mapping list sorted by attributes 
	 * pointing to this entity mapping. The map has EAttributes as keys and
	 * Lists of levels as values. Each level consists of Set of 
	 * AttributeMappingAndChildren objects in the same level. 
	 * Level is a distance in a subtype tree (graph?) from the entity attribute is defined in to
	 * the entity attribute mapping is defined for.
	 *
	 * @param entMapping an <code>EEntity_mapping</code> value
	 * @return a <code>Map</code> value
	 * @exception SdaiException if an error occurs
	 */
	private Map getAttMappingsByAtt(EEntity_mapping entMapping) throws SdaiException {
		CEntityMappingBase entMappingBase = (CEntityMappingBase)entMapping;
		if(entMappingBase.attMappingsByAtt == null) {
			entMappingBase.attMappingsByAtt = new HashMap();
			collectUsedAttributeMappings(entMapping, entMappingBase.attMappingsByAtt, 
										 new AAttribute_mapping(), new ADerived_variant_entity_mapping());
			// Fill in AttributeMappingAndChildren.children sets
			Iterator attMappingByAttIter = entMappingBase.attMappingsByAtt.values().iterator();
			while(attMappingByAttIter.hasNext()) {
				List singleMappings = (List)attMappingByAttIter.next();
				ListIterator singleMappingIter = singleMappings.listIterator(singleMappings.size());
				while(singleMappingIter.hasPrevious()) {
					int levelIdx = singleMappingIter.previousIndex();
					Set levelMappings = (Set)singleMappingIter.previous();
					if(singleMappingIter.hasPrevious() && levelMappings != null) {
						Iterator levelMappingIter = levelMappings.iterator();
						while(levelMappingIter.hasNext()) {
							AttributeMappingAndChildren mappingAndChildren = 
								(AttributeMappingAndChildren)levelMappingIter.next();
							EEntity_definition childEntDef = 
								mappingAndChildren.mapping.getParent_entity(null).getSource(null);
							ListIterator parentLevelIter = singleMappings.listIterator(levelIdx);
							while(parentLevelIter.hasPrevious()) {
								Set parentLevelMappings = (Set)parentLevelIter.previous();
								if(parentLevelMappings != null) {
									Iterator parentIter = parentLevelMappings.iterator();
									while(parentIter.hasNext()) {
										AttributeMappingAndChildren parentAndChildren = 
											(AttributeMappingAndChildren)parentIter.next();
										EEntity_definition parentEntDef = 
											parentAndChildren.mapping
											.getParent_entity(null).getSource(null);
										if(getSupertypeOfLevel(parentEntDef, childEntDef, 0) > 0) {
											if(parentAndChildren.children == null) {
												parentAndChildren.children = new HashSet();
											}
											parentAndChildren.children.add(mappingAndChildren);
										}
									}
								}
							}
						}
					}
				}
				singleMappingIter = singleMappings.listIterator();
				while(singleMappingIter.hasNext()) {
					Set levelMappings = (Set)singleMappingIter.next();
					if(levelMappings != null) {
						Iterator levelMappingIter = levelMappings.iterator();
						while(levelMappingIter.hasNext()) {
							AttributeMappingAndChildren mappingAndChildren = 
								(AttributeMappingAndChildren)levelMappingIter.next();
							Set children = mappingAndChildren.children;
							if(children != null) {
								Iterator childrenIter = ((Set)((HashSet)children).clone()).iterator();
								while(childrenIter.hasNext()) {
									AttributeMappingAndChildren childAndChildren = 
										(AttributeMappingAndChildren)childrenIter.next();
									if(childAndChildren.children != null) {
										children.removeAll(childAndChildren.children);
									}
								}
							}
						}
					}
				}
			}
// 			System.out.println("getAttMappingsByAtt: ");
// 			System.out.println(entMappingBase.attMappingsByAtt);
		}
		return entMappingBase.attMappingsByAtt;
	}
	
	private void collectUsedAttributeMappings(EEntity_mapping entMapping,
											  Map attMappingsByAtt, 
											  AAttribute_mapping attMappingAggr,
											  ADerived_variant_entity_mapping derivedVariantAgg)
		throws SdaiException {

		attMappingAggr.clear();
		CAttribute_mapping.usedinDomain(null, entMapping, mappingDomain, attMappingAggr);
		SdaiIterator attributeMappingIter = attMappingAggr.createIterator();
		while(attributeMappingIter.next()) {
			EAttribute_mapping attributeMapping = attMappingAggr.getCurrentMember(attributeMappingIter);

			EAttribute attribute = attributeMapping.getSource(null);
			if(attribute instanceof EExplicit_attribute) { 
				EExplicit_attribute explicitAttribute = (EExplicit_attribute)attribute;
				while(explicitAttribute.testRedeclaring(null)) {
					explicitAttribute = explicitAttribute.getRedeclaring(null);
				}
				attribute = explicitAttribute;
			}
			List attMappingList = (List)attMappingsByAtt.get(attribute);
			if(attMappingList == null) {
				attMappingList = new ArrayList();
				attMappingsByAtt.put(attribute, attMappingList);
			}
			int level = calculateAttMappingLevel(attributeMapping, attribute);
			int neededSizeIncrease = level - attMappingList.size() + 1;
			if(neededSizeIncrease > 0) {
				attMappingList.addAll(Collections.nCopies(neededSizeIncrease, null));
			}
			Set levelMappings = (Set)attMappingList.get(level);
			if(levelMappings == null) {
				levelMappings = new HashSet();
				attMappingList.set(level, levelMappings);
			}
			levelMappings.add(new AttributeMappingAndChildren(attributeMapping));
		}
		derivedVariantAgg.clear();
		CDerived_variant_entity_mapping.usedinRelated(null, entMapping, mappingDomain, derivedVariantAgg);
		if(derivedVariantAgg.getMemberCount() > 0) {
			ADerived_variant_entity_mapping derivedVariantInnerAgg = 
				new ADerived_variant_entity_mapping();
			attributeMappingIter = derivedVariantAgg.createIterator();
			while(attributeMappingIter.next()) {
				EDerived_variant_entity_mapping derivedVariant = 
					derivedVariantAgg.getCurrentMember(attributeMappingIter);
				EEntity_mapping derivedMapping = derivedVariant.getRelating(null);
				collectUsedAttributeMappings(derivedMapping, attMappingsByAtt, 
											 attMappingAggr, derivedVariantInnerAgg);
			}
		}
		EEntity_definition srcEntity = entMapping.getSource(null);
		EEntity targEntity = entMapping.getTarget(null);
		if(srcEntity.testSupertypes(null)) {
			AEntity_definition supertypes = srcEntity.getSupertypes(null);
			SdaiIterator supertypeIterator = supertypes.createIterator();
			while(supertypeIterator.next()) {
				EEntity_definition supertype = supertypes.getCurrentMember(supertypeIterator);
				AEntity_mapping oneSupertypeMappings = new AEntity_mapping();
				CEntity_mapping.usedinSource(null, supertype, mappingDomain, oneSupertypeMappings);
				SdaiIterator oneSupertypeMappingIter = oneSupertypeMappings.createIterator();
				while(oneSupertypeMappingIter.next()) {
					EEntity_mapping oneSupertypeMapping =
						oneSupertypeMappings.getCurrentMember(oneSupertypeMappingIter);
					if(CEntityMappingBase.isMappingEqual(targEntity,
														 oneSupertypeMapping.getTarget(null))) {
						collectUsedAttributeMappings(oneSupertypeMapping, attMappingsByAtt, 
													 attMappingAggr, derivedVariantAgg);
					}
				}
			}
		}
	}

	static private int calculateAttMappingLevel(EAttribute_mapping attributeMapping, 
												EAttribute attribute) throws SdaiException {
		EEntity_definition testEntDefinition = attributeMapping.getParent_entity(null).getSource(null);
		EEntity_definition targetEntDefintion = attribute.getParent_entity(null);
		return getSupertypeOfLevel(targetEntDefintion, testEntDefinition, 0);
// 		int level = getSupertypeOfLevel(targetEntDefintion, testEntDefinition, 0);
// 		if(attribute.getPersistentLabel().equals("#2251799813687268")) {
// 			System.out.println("calculateAttMappingLevel(testEntDefinition: " + testEntDefinition +
// 							   " targetEntDefintion: " + targetEntDefintion + " level: " + level + ")");
// 		}
// 		return level;
	}

	static private class AttributeMappingAndChildren {
		private EAttribute_mapping mapping;
		Set children;
		private AttributeMappingAndChildren(EAttribute_mapping mapping) {
			this.mapping = mapping;
		}

		public boolean equals(Object obj) {
			if(obj instanceof AttributeMappingAndChildren) {
				AttributeMappingAndChildren other = (AttributeMappingAndChildren)obj;
				return mapping == other.mapping;
			}
			return false;
		}

		public int hashCode() {
			return mapping.hashCode();
		}

		public String toString() {
			try {
				return "AM&C{" + mapping.getPersistentLabel() + " " + children + "}";
			} catch(SdaiException e) {
				return e.toString();
			}
		}
	}

    /* Structure indicating one single call to TestMappedUsers() method that will
     * be stored on stack until returning from TestMappedUsers().
     */
    static class TestMappedUsersCallsCacheEntry {
        EEntity instance;
        EEntity_mapping sourceType;
        //AAttribute_mapping attributeMappings;
        int mode;

        TestMappedUsersCallsCacheEntry(EEntity _instance, EEntity_mapping _sourceType, int _mode) {
            instance = _instance;
            sourceType = _sourceType;
            mode = _mode;
        }

//         public boolean equals(Object obj) {
//             if (obj instanceof TestMappedUsersCallsCacheEntry) {
//                 TestMappedUsersCallsCacheEntry rh = (TestMappedUsersCallsCacheEntry) obj;
//                 if ((instance == rh.instance) &&
//                     (sourceType == rh.sourceType) &&
//                     (mode == rh.mode)) {
//                     return true;
//                 }
//             }

//             return false;
//         }

		public boolean equals(Object obj) {
			if(obj instanceof TestMappedUsersCallsCacheEntry) {
				TestMappedUsersCallsCacheEntry other = (TestMappedUsersCallsCacheEntry)obj;
				return new EqualsBuilder()
					.append(instance, other.instance)
					.append(sourceType, other.sourceType)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(5659, 6619)
				.append(instance)
				.append(sourceType)
				.toHashCode();
		}
    }

	Stack testMappedUsersCallsCacheStack = new Stack();
// 	SequencedHashMap testMappedUsersCallsCacheStack = new SequencedHashMap();

	private void testMappedUsersCallsCachePushCall(TestMappedUsersCallsCacheEntry entry) {
		testMappedUsersCallsCacheStack.push(entry);
// 		testMappedUsersCallsCacheStack.put(entry, entry);
	}

	private void testMappedUsersCallsCachePopCall() {
		testMappedUsersCallsCacheStack.pop();
// 		testMappedUsersCallsCacheStack.remove(testMappedUsersCallsCacheStack.size() - 1);
	}

	private boolean testMappedUsersCallsCacheExists(TestMappedUsersCallsCacheEntry entry) {
		return testMappedUsersCallsCacheStack.contains(entry);
// 		return testMappedUsersCallsCacheStack.containsKey(entry);
	}

//     /* class used for collecting all calls to TestMappedUsers() method and not get into
//      * loop recursion.
//      */
//     static class TestMappedUsersCallsCache {
//         static Stack stack = new Stack();

//         static void pushCall(TestMappedUsersCallsCacheEntry entry) {
//             stack.push(entry);
//         }

//         static void popCall() {
//             stack.pop();
//         }

//         static boolean exists(TestMappedUsersCallsCacheEntry entry) {
//             return stack.contains(entry);
//         }
//     }

/* stuff for testAttributeValueByMode calls cache
 */
    static class TestAttributeCallsCacheEntry {
        EEntity targetInstance;
        EGeneric_attribute_mapping sourceAttribute;
        int mode;

        TestAttributeCallsCacheEntry(EEntity _targetInstance, EGeneric_attribute_mapping _sourceAttribute, int _mode) {
            targetInstance = _targetInstance;
            sourceAttribute = _sourceAttribute;
            mode = _mode;
        }

//         public boolean equals(Object obj) {
//             if (obj instanceof TestAttributeCallsCacheEntry) {
//                 TestAttributeCallsCacheEntry rh = (TestAttributeCallsCacheEntry) obj;
//                 if ((targetInstance == rh.targetInstance) &&
//                     (sourceAttribute == rh.sourceAttribute) &&
//                     (mode == rh.mode)) {
//                     return true;
//                 }
//             }

//             return false;
//         }

		public boolean equals(Object obj) {
			if(obj instanceof TestAttributeCallsCacheEntry) {
				TestAttributeCallsCacheEntry other = (TestAttributeCallsCacheEntry)obj;
				return new EqualsBuilder()
					.append(targetInstance, other.targetInstance)
					.append(sourceAttribute, other.sourceAttribute)
					.append(mode, other.mode)
					.isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(2711, 1223)
				.append(targetInstance)
				.append(sourceAttribute)
				.append(mode)
				.toHashCode();
		}
    }

	Stack testAttributeCallsCacheStack = new Stack();
// 	SequencedHashMap testAttributeCallsCacheStack = new SequencedHashMap();

	private void testAttributeCallsCachePushCall(TestAttributeCallsCacheEntry entry) {
		testAttributeCallsCacheStack.push(entry);
// 		testAttributeCallsCacheStack.put(entry, entry);
	}

	private void testAttributeCallsCachePopCall() {
		testAttributeCallsCacheStack.pop();
// 		testAttributeCallsCacheStack.remove(testAttributeCallsCacheStack.size() - 1);
	}

	private boolean testAttributeCallsCacheExists(TestAttributeCallsCacheEntry entry) {
		return testAttributeCallsCacheStack.contains(entry);
// 		return testAttributeCallsCacheStack.containsKey(entry);
	}

//     /* class used for collecting all calls to TestMappedUsers() method and not get into
//      * loop recursion.
//      */
//     static class TestAttributeCallsCache {
//         static Stack stack = new Stack();

//         static void pushCall(TestAttributeCallsCacheEntry entry) {
//             stack.push(entry);
//         }

//         static void popCall() {
//             stack.pop();
//         }

//         static boolean exists(TestAttributeCallsCacheEntry entry) {
//             return stack.contains(entry);
//         }
//     }
}

