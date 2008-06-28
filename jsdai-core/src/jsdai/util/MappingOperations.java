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

import java.util.*;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class MappingOperations {

/**Find EAttribute_mapping's for coresponding EEntity_mapping.*/
	public static EAttribute_mapping[] findAttributeMappingsForEntityMapping(AAttribute_mapping attributes, EEntity_mapping entity) throws SdaiException {
		Vector result = new Vector();
		SdaiIterator it = attributes.createIterator();
		while (it.next()) {
			EAttribute_mapping attribute = attributes.getCurrentMember(it);
			if (attribute.getParent_entity(null) == entity) {
				result.addElement(attribute);
			}
		}
		return (EAttribute_mapping[])result.toArray();
	}

	public static EEntity getTarget(EAttribute_mapping attribute) throws SdaiException {
		EEntity result = null;
		if(attribute.testDomain(null)) {
			EEntity domain = attribute.getDomain(null);
			if(domain instanceof ENamed_type) {
				result = domain;
			} else if(domain instanceof EEntity_mapping) {
				EEntity_mapping domainEntMapping = (EEntity_mapping)domain;
				if(domainEntMapping.testTarget(null)) {
					result = domainEntMapping.getTarget(null);
				} else
					throw new SdaiException(SdaiException.SY_ERR, "missing entity maping target");
			} else
				throw new SdaiException(SdaiException.SY_ERR, "missing domain");
		} else if (attribute.testPath(null)) {
			AAttribute_mapping_path_select path = attribute.getPath(null);
			SdaiIterator it_path = path.createIterator();
			it_path.end();
			it_path.previous();
			result = getTarget(path.getCurrentMember(it_path));
		} else {
			result = getEntity_mappingTarget(attribute.getParent_entity(null));
		}
		return result;
	}

	public static EEntity getTarget(EEntity select) throws SdaiException {
		EEntity result = null;
		if (select instanceof EAttribute) {
			result = SimpleOperations.getAttributeDomain((EAttribute)select);
		} else if (select instanceof EEntity_constraint) {
			EEntity_constraint ec = (EEntity_constraint)select;
			result = ec.getDomain(null);
		} else if (select instanceof EInverse_attribute_constraint) {
			EInverse_attribute_constraint ia = (EInverse_attribute_constraint)select;
			EEntity invert = ia.getInverted_attribute(null);
			if (invert instanceof EAttribute) {
				result = ((EAttribute)invert).getParent_entity(null);
			} else {
				result = getTargetForInverse(invert);
			}
		} else if (select instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint)select;
			result = getTarget(pc.getElement1(null));
		} else if (select instanceof ESelect_constraint) {
			ESelect_constraint sc = (ESelect_constraint)select;
			ADefined_type types = sc.getData_type(null);
			result = types.getByIndex(types.getMemberCount());
		} else if (select instanceof EAttribute_value_constraint) {
			EAttribute_value_constraint avc = (EAttribute_value_constraint)select;
			result = getTarget(avc.getAttribute(null));
		} else if (select instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint)select;
			result = getTarget(amc.getAttribute(null));
		}
		return result;
	}

	public static EEntity getTargetForInverse(EEntity select) throws SdaiException {
		EEntity result = null;
		if (select instanceof EAttribute) {
			result = ((EAttribute)select).getParent_entity(null);
		} else if (select instanceof EEntity_constraint) {
			EEntity_constraint ec = (EEntity_constraint)select;
			result = getTargetForInverse(ec.getAttribute(null));
/*		} else if (select instanceof EInverse_attribute_constraint) {
			EInverse_attribute_constraint ia = (EInverse_attribute_constraint)select;
			EEntity invert = ia.getInverted_attribute(null);
			if (invert instanceof EAttribute) {
				result = getTarget(invert);
			} else {
				result = ((EAttribute)invert).getParent_entity(null);
			}*/
/*		} else if (select instanceof EPath_constraint) {
			EPath_constraint pc = (EPath_constraint)select;
			result = getTarget(pc.getElement2(null));*/
		} else if (select instanceof ESelect_constraint) {
			ESelect_constraint sc = (ESelect_constraint)select;
			result = getTargetForInverse(sc.getAttribute(null));
/*		} else if (select instanceof EAttribute_value_constraint) {
			EAttribute_value_constraint avc = (EAttribute_value_constraint)select;
			result = getTarget(avc.getAttribute(null));*/
		} else if (select instanceof EAggregate_member_constraint) {
			EAggregate_member_constraint amc = (EAggregate_member_constraint)select;
			result = getTargetForInverse(amc.getAttribute(null));
		}
		return result;
	}

//	public static String getMappedAttributeString(EEntity instance, EGeneric_attribute_mapping attribute,
//					ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
	public static String getMappedAttributeString(Object value) throws SdaiException {
		String result = "";
		if (value == null) {
			result = "null";
		} else if (value instanceof Object[]) {
			result = getArrayAsString((Object[])value);
		} else if (value instanceof ArrayList) {
			result = getArrayAsString(((ArrayList)value).toArray());
		} else {
			result = value.toString();
		}
		return result;
	}

	public static String getArrayAsString(Object[] arr) throws SdaiException {
		String result = "";
		result += "(";
		boolean first = true;
		for (int i = 0; i < arr.length; i++) {
			if (first) {
				first = false;
			} else {
				result += ", ";
			}
			if (arr[i] instanceof EEntity) {
				EEntity e = (EEntity)arr[i];
				result += e.getPersistentLabel();
			} else {
				if (arr[i] instanceof String) {
					result += "'"+arr[i].toString()+"'";
				} else {
					result += String.valueOf(arr[i]);
				}
			}
		}
		result += ")";
		return result;
	}

	public static Object getMappedAttributeObject(EEntity instance, EGeneric_attribute_mapping attribute,
					ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		Object result = null;
		result = instance.getMappedAttribute(attribute, targetDomain, mappingDomain, mode);
		if ((SimpleOperations.getAttributeDomain(attribute.getSource(null)) instanceof EAggregation_type) &&
			!(result instanceof ArrayList || result instanceof Aggregate)) {
			ArrayList a = new ArrayList();
			a.add(result);
			result = a;
		}
		return result;
	}

	public static EEntity_definition getEntity_mappingTarget(EEntity_mapping entity) throws SdaiException {
		EEntity_definition result = null;
		if (entity.testTarget(null)) {
			EEntity target = entity.getTarget(null);
			if (target instanceof EEntity_definition) {
				result = (EEntity_definition)target;
			} else if (target instanceof EAttribute) {
				EAttribute attribute = (EAttribute)target;
				result = attribute.getParent_entity(null);
			}
		}
		return result;
	}

	public static EEntity_mapping findMappingForARM(AEntity_mapping mappings, EEntity_definition arm) throws SdaiException {
		SdaiIterator it_mappings = mappings.createIterator();
		while (it_mappings.next()) {
			EEntity_mapping mapping = mappings.getCurrentMember(it_mappings);
			if (mapping.getSource(null) == arm) {
				return mapping;
			}
		}
		return null;
	}

	public static EAttribute_mapping[] findInversesForMapping(EEntity_definition definition, ASdaiModel domain) throws SdaiException {
		HashSet result = new HashSet();
		EExplicit_attribute attrs[] = SimpleOperations.findAttributesForDomainDefinition(definition, domain);
		ArrayList a = new ArrayList();
		SimpleOperations.addArrayToVector(a, attrs);
//		Debug.println("attributai= "+a);
		for (int i = 0; i < attrs.length; i++) {
			AAttribute_mapping mappings = new AAttribute_mapping();
			CAttribute_mapping.usedinSource(null, attrs[i], domain, mappings);
			SimpleOperations.addArrayToVector(result, LangUtils.aggregateToArray(mappings));
		}
//		Iterator it = result.iterator();
//		while (it.hasNext()) {
//			EAttribute_mapping am = (EAttribute_mapping)it.next();
//Debug.println("aaaa "+am.getSource(null).getParent_entity(null).getName(null)+"   "+am.getParent_entity(null).getSource(null).getName(null));
//Debug.println("ammmmm "+am.getSource(null).getParent_entity(null)+" "+am.getSource(null)+" "+am.getPersistentLabel());
//		}
//Debug.println("attribute_mappingai="+result);
		return (EAttribute_mapping[])result.toArray(new EAttribute_mapping[result.size()]);
	}

	public static AEntity findMappingInversesForInstance(EEntity instance, EEntity_mapping mentity,
			ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode, AAttribute_mapping result2) throws SdaiException
	{
		EEntity_definition source = mentity.getSource(null);
		return  findMappingInversesForInstance(instance, source, targetDomain, mappingDomain,
			findInversesForMapping(source, mappingDomain), mode, result2);
	}

	public static AEntity findMappingInversesForInstance(EEntity instance, EEntity_definition arm, ASdaiModel targetDomain,
		ASdaiModel mappingDomain, EAttribute_mapping mattrs[], int mode, AAttribute_mapping result2) throws SdaiException
	{
		AEntity result = new AEntity();
		SdaiModel model = instance.findEntityInstanceSdaiModel();
		for (int i = 0; i < mattrs.length; i++) {
			AEntity entities = model.findMappingInstances(mattrs[i].getParent_entity(null), targetDomain, mappingDomain, mode);
			SdaiIterator it_entities = entities.createIterator();
			while (it_entities.next()) {
				EEntity entity = entities.getCurrentMemberEntity(it_entities);
				Object value = getMappedAttributeObject(entity, mattrs[i], targetDomain, mappingDomain, mode);
//Debug.println("ennnn "+entity+mattrs[i].getSource(null).getParent_entity(null)+" value="+value);
				if ((value == instance)) {
					result2.addByIndex(1, mattrs[i], null);
					result.addByIndex(1, entity);
				} else if (value instanceof AEntity) {
					AEntity aggr = (AEntity)value;
					SdaiIterator it_aggr = aggr.createIterator();
					while(it_aggr.next()) {
						EEntity member = aggr.getCurrentMemberEntity(it_aggr);
						if ((member == instance)) {
							result2.addByIndex(1, mattrs[i], null);
							result.addByIndex(1, entity);
						}
					}
				} else if (value instanceof ArrayList) {
					ArrayList arr = (ArrayList)value;
					for (int ii = 0; ii < arr.size(); ii++) {
						Object member = arr.get(ii);
						if ((member == instance)) {
							result2.addByIndex(1, mattrs[i], null);
							result.addByIndex(1, entity);
						}
					}
				} else if (value instanceof Object[]) {
					Object arr[] = (Object[])value;
					for (int ii = 0; ii < arr.length; ii++) {
						Object member = arr[ii];
						if ((member == instance)) {
							result2.addByIndex(1, mattrs[i], null);
							result.addByIndex(1, entity);
						}
					}
				}
			}
		}
//		Debug.println("Inverses");
//		SdaiIterator it = result.createIterator();
//		while (it.next()) {
//			Debug.println(""+result.getCurrentMemberEntity(it));
//		}
//		Debug.println("Attributeas");
//		SdaiIterator it2 = result2.createIterator();
//		while (it2.next()) {
//			Debug.println(""+result2.getCurrentMemberEntity(it2));
//		}
//		System.out.println("Main "+SimpleOperations.getAggregateString(result, false, "\n"));
//		System.out.println("Main "+SimpleOperations.getAggregateString(result2, false, "\n"));
		return result;
	}

	public static AEntity findMappingInversesForInstance(EEntity instance, EEntity_definition arm,
			ASdaiModel targetDomain, ASdaiModel mappingDomain, EAttribute attribute, int mode) throws SdaiException
	{
		EEntity_definition definition = attribute.getParent_entity(null);
		AAttribute_mapping aam = new AAttribute_mapping();
		CAttribute_mapping.usedinSource(null, attribute, null, aam);
		AEntity result = new AEntity();
		AAttribute_mapping aem = new AAttribute_mapping();
		AEntity inverses = findMappingInversesForInstance(instance, arm, targetDomain, mappingDomain, (EAttribute_mapping[])LangUtils.aggregateToArray(aam, AAttribute_mapping.class), mode, aem);
		SdaiIterator it_inverses = inverses.createIterator();
		while (it_inverses.next()) {
			EEntity e = inverses.getCurrentMemberEntity(it_inverses);
			if (e.testMappedEntity(definition, targetDomain, mappingDomain, mode) != null) {
				result.addByIndex(result.getMemberCount()+1, e);
			}
		}
		return result;
	}

	public static void removeSubtypeInverses(AEntity instances, AAttribute_mapping attributes) throws SdaiException {
//		Debug.println("Inverses");
//		SdaiIterator it = instances.createIterator();
//		while (it.next()) {
//			Debug.println(""+instances.getCurrentMemberEntity(it));
//		}
//		Debug.println("Attributeas");
//		SdaiIterator it2 = attributes.createIterator();
//		while (it2.next()) {
//			Debug.println(""+attributes.getCurrentMemberEntity(it2));
//		}

		HashSet remove  = new HashSet();
		Vector indexes = new Vector();
		for (int i = 1; i <= instances.getMemberCount(); i++) {
			EEntity instance = (EEntity)instances.getByIndexEntity(i);
			indexes.clear();
			int k = i;
			while (k != -1) {
				indexes.add(new Integer(k));
				k = SimpleOperations.indexInAggregate(instances, instance, k+1);
			}
			for (int j = 0; j < indexes.size(); j++) {
				for (int l = j+1; l < indexes.size(); l++) {
					int ij = ((Integer)indexes.get(j)).intValue();
					int il = ((Integer)indexes.get(l)).intValue();
					EEntity_definition dj = ((EAttribute_mapping)attributes.getByIndex(ij)).getParent_entity(null).getSource(null);
					EEntity_definition dl = ((EAttribute_mapping)attributes.getByIndex(il)).getParent_entity(null).getSource(null);
					if (dj.isSubtypeOf(dl)) {
						remove.add(new Integer(il));
					}
					if (dl.isSubtypeOf(dj)) {
						remove.add(new Integer(ij));
					}
//					if (SimpleOperations.isSubtype(dj, dl)) {
//						remove.add(new Integer(il));
//					}
//					if (SimpleOperations.isSubtype(dl, dj)) {
//						remove.add(new Integer(ij));
//					}
				}
			}
		}
		Vector rr = new Vector(remove);
		Collections.sort(rr);
		Integer r[] = (Integer[])rr.toArray(new Integer[rr.size()]);
		for (int i = r.length-1; i > -1; i--) {
			int k = r[i].intValue();
			instances.removeByIndex(k);
			attributes.removeByIndex(k);
		}
//		Debug.println("Inverses");
//		it = instances.createIterator();
//		while (it.next()) {
//			Debug.println(""+instances.getCurrentMemberEntity(it));
//		}
//		Debug.println("Attributeas");
//		it2 = attributes.createIterator();
//		while (it2.next()) {
//			Debug.println(""+attributes.getCurrentMemberEntity(it2));
//		}
	}

	public static Vector getAttributeMappingAlternatives(EAttribute attribute, EEntity_mapping ent_mapping) throws SdaiException {
		Vector result = new Vector();
		AGeneric_attribute_mapping ams =  new AGeneric_attribute_mapping();
		CGeneric_attribute_mapping.usedinParent_entity(null, ent_mapping, null, ams);
		SdaiIterator ams_it = ams.createIterator();
		while (ams_it.next()) {
			EGeneric_attribute_mapping am = ams.getCurrentMember(ams_it);
			if (am.getSource(null) == attribute) {
				result.add(am);
			}
		}
		return result;
	}

	public static AEntity findMappingUsedin(EEntity instance, EEntity_mapping mentity, AAttribute_mapping aam,
			ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode, AAttribute_mapping result2) throws SdaiException
	{
		AEntity result;
		if (aam == null) {
		   result = findMappingInversesForInstance(instance, mentity, dataDomain, mappingDomain, mode, result2);
	      removeSubtypeInverses(result, result2);
		} else {
		   result = findMappingInversesForInstance(instance, null/*mentity.getSource(null)*//*Not used*/, dataDomain,
		   		mappingDomain, (EAttribute_mapping[])LangUtils.aggregateToArray(aam, EAttribute_mapping.class), mode, result2);
	      removeSubtypeInverses(result, result2);
		}
/*		if (aam != null) {
//System.out.println("Defs "+aam.toString());
//System.out.println("Attributes "+result2.toString());
//System.out.println("Instances "+result.toString());
			SdaiIterator it1 = result.createIterator();
			SdaiIterator it2 = result2.createIterator();
			while (it1.next() && it2.next()) {
				EAttribute_mapping am = (EAttribute_mapping)result2.getCurrentMember(it2);
				while (!aam.isMember(am)) {
				   it1.remove();
					it2.remove();
				   try {
						am = (EAttribute_mapping)result2.getCurrentMember(it2);
				   } catch (SdaiException ex) {
						break;
				   }
//System.out.println("removing attribute "+am);
//System.out.println("removing instance "+result.getCurrentMemberEntity(it1));
				}
//System.out.println("Attributes "+result2.toString());
//System.out.println("Instances "+result.toString());
			}
		}*/
		return result;
	}


	public static ESchema_mapping findSchema_mappingFor(EEntity_mapping ent_mapping) throws SdaiException {
		AUof_mapping uof_mappings = new AUof_mapping();
		CUof_mapping.usedinMappings(null, ent_mapping, null, uof_mappings);
		ASchema_mapping sch_mappings = null;
		if(uof_mappings.getMemberCount() > 0) {
			EUof_mapping uof_mapping = uof_mappings.getByIndex(1);
			sch_mappings = new ASchema_mapping();
			CSchema_mapping.usedinUofs(null, uof_mapping, null, sch_mappings);
			if(sch_mappings.getMemberCount() == 0) {
				sch_mappings = null;
			}
		}
		if(sch_mappings == null) {
			sch_mappings = (ASchema_mapping)ent_mapping.findEntityInstanceSdaiModel().getInstances(ESchema_mapping.class);
		}
		return sch_mappings.getByIndex(1);
	}

	public static AEntity_mapping getMappingForARM_And_AIM(EEntity_definition target, EEntity_definition source, ASdaiModel mappingDomain, AEntity_mapping result) throws SdaiException {
		AEntity_definition t_super = target.getSupertypes(null);
		AEntity_definition s_super = source.getSupertypes(null);
		SdaiIterator it_t = t_super.createIterator();
		SdaiIterator it_s = s_super.createIterator();
		while (it_t.next()) {
//			getMappingForARM_And_AIM(t_super.getCurrentMember(it_t), source, mappingDomain, result);
		}
		while (it_s.next()) {
//			getMappingForARM_And_AIM(target, s_super.getCurrentMember(it_s), mappingDomain, result);
		}
		AEntity_mapping tmaps = new AEntity_mapping();
		CEntity_mapping.usedinTarget(null, target, mappingDomain, tmaps);

		AEntity_mapping smaps = new AEntity_mapping();
		CEntity_mapping.usedinSource(null, source, mappingDomain, smaps);

		SdaiIterator tmaps_it = tmaps.createIterator();
		SdaiIterator smaps_it = smaps.createIterator();
		while (tmaps_it.next()) {
			smaps_it.beginning();
			while (smaps_it.next()) {
				if (tmaps.getCurrentMember(tmaps_it) == smaps.getCurrentMember(smaps_it)) {
					result.addByIndex(1, tmaps.getCurrentMember(tmaps_it));
				}
			}
		}
		return result;
	}


	public static ASdaiModel getMappingDomain(ASchema_mapping asm) throws SdaiException {
		ASdaiModel result = new ASdaiModel();
		if(asm.getMemberCount() > 0) {
			SdaiRepository systemRepository = null;
			SdaiIterator it_asm = asm.createIterator();
			while (it_asm.next()) {
				ESchema_mapping sm = asm.getCurrentMember(it_asm);
				sm.getSource(null); // Load all source (ARM) dictionary models
				sm.getTarget(null); // Load all target (AIM,MIM) dictionary models
				if(systemRepository == null) {
					systemRepository = sm.findEntityInstanceSdaiModel().getRepository();
				}
				SchemaInstance mappingSchInst = systemRepository.findSchemaInstance(sm.getId(null) + "_mapping_data");
				if(mappingSchInst != null) {
					ASdaiModel mappingDomainModels = mappingSchInst.getAssociatedModels();
					for(SdaiIterator mappingDomainIter = mappingDomainModels.createIterator(); mappingDomainIter.next(); ) {
						SdaiModel mappingDomainModel = mappingDomainModels.getCurrentMember(mappingDomainIter);
						result.addByIndex(1, mappingDomainModel);
					}
				} else {
					result.addByIndex(1, sm.findEntityInstanceSdaiModel(), null);
					result.addByIndex(1, sm.getSource(null).findEntityInstanceSdaiModel(), null);
					result.addByIndex(1, sm.getTarget(null).findEntityInstanceSdaiModel(), null);
				}
			}
		}
		return result;
	}

	public static AEntity getValidatedMappingInstnces(EEntity_mapping mapping, SdaiModel model, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		AEntity result = new AEntity();
		AEntity instances = model.findMappingInstances(mapping, dataDomain, mappingDomain, mode);
		SdaiIterator it_instances = instances.createIterator();
		while (it_instances.next()) {
			EEntity instance = instances.getCurrentMemberEntity(it_instances);
			if (validateMappingInstance(mapping, instance, dataDomain, mappingDomain, mode)) {
				result.addByIndex(result.getMemberCount()+1, instance);
			}
		}
		return result;
	}

	public static boolean validateMappingInstance(EEntity_mapping mapping, EEntity instance, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		boolean result = true;
		EEntity_definition source = mapping.getSource(null);
		ArrayList attributes = new ArrayList();
		LangUtils.findExplicitAttributes(source, attributes);
		for (int i = 0; i < attributes.size(); i++) {
			EAttribute attribute = (EAttribute)attributes.get(i);
			boolean opt = false;
			if (attribute instanceof EExplicit_attribute) {
				opt = ((EExplicit_attribute)attribute).getOptional_flag(null);
			}
			Object o[] = instance.getMappedAttribute(attribute, dataDomain, mappingDomain, mode);
			boolean set = (o != null) && (o.length > 0);
			result = result && (opt || set);
		}
		return result;
	}

/* This is a method getting inverses with attribute_mnapping.domain support.
	//geting instance users with attribute_mapping domain usage.
	public static AEntity getInverses(EEntity instance, EEntity_mapping entity_mapping,
		    ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode, AAttribute_mapping result2) throws SdaiException {
		AEntity result1 = new AEntity();
		//finding attribute mappings wich is referencing entity_mapping
//Debug.println("Instance "+instance);
//Debug.println("Entity_mapping"+entity_mapping);
		AAttribute_mapping aam = new AAttribute_mapping();
		CAttribute_mapping.usedinDomain(null, entity_mapping, null, aam);
		//finding instances which
		SdaiIterator it_aam = aam.createIterator();
		SdaiModel model = instance.findEntityInstanceSdaiModel();
		while (it_aam.next()) {
			EAttribute_mapping am = aam.getCurrentMember(it_aam);
//Debug.println("am "+am);
			EEntity_mapping em = am.getParent_entity(null);
			AEntity ae = model.findMappingInstances(em, dataDomain, mappingDomain, mode);
			SdaiIterator it_ae = ae.createIterator();
			while (it_ae.next()) {
				EEntity e = ae.getCurrentMemberEntity(it_ae);
//Debug.println("e "+e);
				Object value = e.getMappedAttribute(am, dataDomain, mappingDomain, mode);
				if (value == instance) {
					result1.addByIndex(1, e);
					result2.addByIndex(1, am, null);
				} else if (value instanceof AEntity) {
					AEntity aggr = (AEntity)value;
					SdaiIterator it_aggr = aggr.createIterator();
					while(it_aggr.next()) {
						EEntity member = aggr.getCurrentMemberEntity(it_aggr);
						if (member == instance) {
							result1.addByIndex(1, e);
							result2.addByIndex(1, am, null);
						}
					}
				} else if (value instanceof ArrayList) {
					ArrayList arr = (ArrayList)value;
					for (int ii = 0; ii < arr.size(); ii++) {
						Object member = arr.get(ii);
						if (member == instance) {
							result1.addByIndex(1, e);
							result2.addByIndex(1, am, null);
						}
					}
				} else if (value instanceof Object[]) {
					Object arr[] = (Object[])value;
					for (int ii = 0; ii < arr.length; ii++) {
						Object member = arr[ii];
						if (member == instance) {
							result1.addByIndex(1, e);
							result2.addByIndex(1, am, null);
						}
					}
				}
			}
		}
		return result1;
	}
*/

	static public boolean getMostSpecificMappings(EEntity instance, EEntity_definition type,
		   ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping result) throws SdaiException {
		if (type == null) return false;
		boolean kuku = false;
		AEntity_definition subtypes = new AEntity_definition();
		type.findEntityInstanceUsedin(CEntity_definition.attributeGeneric_supertypes(null), mappingDomain, subtypes);
		if (subtypes.getMemberCount() == 0) {
		   AEntity_mapping aem = instance.testMappedEntity(type, dataDomain, mappingDomain, EEntity.NO_RESTRICTIONS);
		   if (aem != null) {
				SimpleOperations.appendAggregateToAggregate(result, aem);
				kuku = true;
		   }
		} else {
		   SdaiIterator it = subtypes.createIterator();
		   while (it.next()) {
				EEntity_definition subtype = subtypes.getCurrentMember(it);
				kuku = kuku || getMostSpecificMappings(instance, subtype, dataDomain, mappingDomain, result);
		   }
		}
		if (!kuku) {
		   AEntity_mapping aem = instance.testMappedEntity(type, dataDomain, mappingDomain, EEntity.NO_RESTRICTIONS);
		   if (aem != null) {
				SimpleOperations.appendAggregateToAggregate(result, aem);
				kuku = true;
		   }
		}
		return kuku;
	}

}

