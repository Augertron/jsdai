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

package jsdai.libutil;

import jsdai.lang.*;
import jsdai.util.*;
import jsdai.dictionary.*;
import jsdai.SPerson_organization_assignment_mim.*;
import jsdai.SPerson_organization_schema.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.CMeasure_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.*;
import jsdai.SMeasure_schema.EFrequency_measure_with_unit;
import jsdai.SMeasure_schema.EPower_measure_with_unit;
//import jsdai.SDocument_schema.EDocument;
//import jsdai.SDocument_schema.EDocument_type;
import jsdai.SCharacteristic_xim.ETolerance_characteristic;
import jsdai.SDocument_assignment_mim.AApplied_document_reference;
import jsdai.SDocument_assignment_mim.ADocument_reference_item;
import jsdai.SDocument_assignment_mim.CApplied_document_reference;
import jsdai.SDocument_assignment_mim.EApplied_document_reference;
import jsdai.SDocument_definition_xim.EDocument_definition;
import jsdai.SDocument_schema.ADocument_product_association;
import jsdai.SDocument_schema.CDocument;
import jsdai.SDocument_schema.CDocument_product_association;
import jsdai.SDocument_schema.CDocument_type;
import jsdai.SDocument_schema.EDocument;
import jsdai.SDocument_schema.EDocument_product_association;
import jsdai.SDocument_schema.EDocument_type;
import jsdai.SGeometry_schema.CGeometric_representation_context;
import jsdai.SGeometry_schema.EAxis2_placement_2d;
import jsdai.SGroup_schema.EGroup;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SApplication_context_schema.*;
import jsdai.SApproval_mim.*;
import jsdai.SBasic_attribute_schema.ADescription_attribute;
import jsdai.SBasic_attribute_schema.CDescription_attribute;
import jsdai.SBasic_attribute_schema.EDescription_attribute;
//import java.lang.reflect.*;
import java.util.HashMap;

/**
 * <p>Contains helpful operations fo ARM to AIM implementation.</p>
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */
public class CxAP210ARMUtilities extends JsdaiLangAccessor{
   // This is hasmap used for instances, which are most popular and which consumes most of CPU type.
   // It will help to speed up things and avoid using of LangUtils,
   // which goes through the whole population
   // USERS so far:
   // dri_placement_fixed_true,
   // dri_placement_fixed_false,
   ///
   // dri_seating_plane_intersection_surface_intersection,
   // dri_seating_plane_intersection_through_intersection,
   // dri_seating_plane_intersection_does_not_intersect
   // pcbLabel is persistentLabel of particular pcb instance (it could also be rppii)
   // pcbLabel+"_pcb_pups_design"
   // pcbLabel+"_pcb_pds"
	// dri_padstack_location_top,
	// dri_padstack_location_symmetrical,
	// dri_padstack_location_bottom,
   public static HashMap mostlyUsedInstances = new HashMap();

	/**
	 * Creates AIM entities for attributes which value is person_organization.
	 * (implementation of one of frequent mapping cases)
	 * @param context SdaiContext.
	 * @param armEntityBase  ARM entity which hase attribute with value of EPerson_organization.
	 * @param armEntityAttribute attribute of select type EPerson_organization.
	 * Example: for Ee_product value of this parameter Ee_product.getDesign_owner(null).
	 * @param roleName name of AIM role this assigns entity and attribute.
	 * Example: for Ee_product value of this parameter "design owner".
	 */
	public static void createPerson_organization(SdaiContext context, EMappedARMEntity armEntityBase, EMappedARMEntity armEntityAttribute, String roleName) throws SdaiException {
		EEntity aimEntityBase = armEntityBase.getAimInstance();

		armEntityAttribute.createAimData(context);
		EEntity aimEntityAttribute = armEntityAttribute.getAimInstance();

		//EOrganization
		 if (armEntityAttribute instanceof EOrganization) {

			//applied_organization_assignment
			 EApplied_organization_assignment applied_organization_assignment = null;
			 AApplied_organization_assignment aApplied_organization_assignment = new AApplied_organization_assignment();
			 CApplied_organization_assignment.usedinAssigned_organization(null, (jsdai.SPerson_organization_schema.EOrganization) aimEntityAttribute, context.domain, aApplied_organization_assignment);
			 jsdai.SPerson_organization_schema.EOrganization_role role = null;
	         for (int i = 1; i <= aApplied_organization_assignment.getMemberCount(); i++) {
					applied_organization_assignment = aApplied_organization_assignment.getByIndex(i);
					if (applied_organization_assignment.testRole(null)) {
						role = applied_organization_assignment.getRole(null);
						if (role.testName(null) && role.getName(null).equals(roleName)) {
							break;
						}
					}
					applied_organization_assignment = null;
				}

				//applied_organization_assignment
				if (applied_organization_assignment == null) {
					 applied_organization_assignment = (EApplied_organization_assignment) context.working_model.createEntityInstance(CApplied_organization_assignment.class);
					 applied_organization_assignment.setAssigned_organization(null, (jsdai.SPerson_organization_schema.EOrganization) aimEntityAttribute);
				}

				//organization_role
				if (!applied_organization_assignment.testRole(null)) {

					LangUtils.Attribute_and_value_structure[] structure = {new LangUtils.Attribute_and_value_structure(jsdai.SPerson_organization_schema.COrganization_role.attributeName(null), roleName)};
					role = (jsdai.SPerson_organization_schema.EOrganization_role)
							    LangUtils.createInstanceIfNeeded(context, jsdai.SPerson_organization_schema.COrganization_role.definition, structure);

					applied_organization_assignment.setRole(null, role);
				}

				if (!applied_organization_assignment.testItems(null)) {
				   applied_organization_assignment.createItems(null);
				}

				applied_organization_assignment.getItems(null).addUnordered(aimEntityBase);

		//EPerson_and_organization
		 } else if (armEntityAttribute instanceof EPerson_and_organization) {
			    EApplied_person_and_organization_assignment applied_person_and_organization_assignment = null;
				AApplied_person_and_organization_assignment aApplied_person_and_organization_assignment = new AApplied_person_and_organization_assignment();
				CApplied_person_and_organization_assignment.usedinAssigned_person_and_organization(null, (jsdai.SPerson_organization_schema.EPerson_and_organization) aimEntityAttribute, context.domain, aApplied_person_and_organization_assignment);
				jsdai.SPerson_organization_schema.EPerson_and_organization_role role = null;

				for (int i = 1; i <= aApplied_person_and_organization_assignment.getMemberCount(); i++) {
					applied_person_and_organization_assignment = aApplied_person_and_organization_assignment.getByIndex(i);
					if (applied_person_and_organization_assignment.testRole(null)) {
						role = applied_person_and_organization_assignment.getRole(null);
						if (role.testName(null) && role.getName(null).equals(roleName)) {
							break;
						}
					}
					applied_person_and_organization_assignment = null;
				}

				//applied_organization_assignment
				if (applied_person_and_organization_assignment == null) {
					 applied_person_and_organization_assignment = (EApplied_person_and_organization_assignment) context.working_model.createEntityInstance(CApplied_person_and_organization_assignment.class);
					 applied_person_and_organization_assignment.setAssigned_person_and_organization(null, (jsdai.SPerson_organization_schema.EPerson_and_organization) aimEntityAttribute);
				}

				//organization_role
				if (!applied_person_and_organization_assignment.testRole(null)) {

					LangUtils.Attribute_and_value_structure[] structure = {new LangUtils.Attribute_and_value_structure(jsdai.SPerson_organization_schema.CPerson_and_organization_role.attributeName(null), roleName)};
					role = (jsdai.SPerson_organization_schema.EPerson_and_organization_role) LangUtils.createInstanceIfNeeded(context, jsdai.SPerson_organization_schema.CPerson_and_organization_role.definition, structure);

					applied_person_and_organization_assignment.setRole(null, role);
				}

				if (!applied_person_and_organization_assignment.testItems(null)) {
				   applied_person_and_organization_assignment.createItems(null);
				}

				applied_person_and_organization_assignment.getItems(null).addUnordered(aimEntityBase);
		 }
	}

	/**
	 * Deletes AIM entities for attributes which value is person_organization.
	 * (implementation of one of frequent mapping cases)
	 */
	public static void removePerson_organization(SdaiContext context, EMappedARMEntity armEntityBase, String roleName) throws SdaiException {
		EEntity aimEntity = armEntityBase.getAimInstance();
		Class assignedClass = EApplied_organization_assignment.class;
		clearAssignment(context, assignedClass, "Items", roleName, aimEntity);

		assignedClass = EApplied_person_and_organization_assignment.class;

		clearAssignment(context, assignedClass, "Items", roleName, aimEntity);
	}

	/**
	 * Creates AIM entities for attributes which value is ee_approval.
	 * (implementation of one of frequent mapping cases)
	 * @param context SdaiContext.
	 * @param armEntityBase  ARM entity which hase attribute with value of ee_approval.
	 * @param armEntityAttribute attribute of select type ee_approval.
	 * Example for EE_product_definition MEe_product_definition.class.getMethod("testProduct_definition_approval", new Class[] {ASdaiModel.class, EEntity.class});
	 */
	public static void createApproval(SdaiContext context, EMappedARMEntity armEntityBase, EMappedARMEntity armEntityAttribute) throws SdaiException {
		   EEntity aimEntity = armEntityBase.getAimInstance();

			armEntityAttribute.createAimData(context);
			jsdai.SApproval_schema.EApproval aimApproval = (jsdai.SApproval_schema.EApproval) armEntityAttribute.getAimInstance();

			EApplied_approval_assignment assignment = null;
			AApplied_approval_assignment aaaa = new AApplied_approval_assignment();
			CApplied_approval_assignment.usedinItems(null, aimEntity, context.domain, aaaa);
			//unset old values
			for (int i = 1; i <= aaaa.getMemberCount(); i++) {
				assignment = aaaa.getByIndex(i);
				AEntity items = assignment.getItems(null);

				while (items.isMember(aimEntity)) {
					items.removeUnordered(aimEntity);
				}

				if ((items.getMemberCount() == 0) && (countEntityUsers(context, assignment) == 0)) {
					assignment.deleteApplicationInstance();
				}
			}

			//set new values
			aaaa.clear();
			CApplied_approval_assignment.usedinAssigned_approval(null, aimApproval, context.domain, aaaa);
			if(aaaa.getMemberCount() > 0){
				// Reuse it
				assignment = aaaa.getByIndex(1);
				if(!assignment.testItems(null)) {
					assignment.createItems(null);
				}
			} else {
			  assignment = (EApplied_approval_assignment) context.working_model.createEntityInstance(CApplied_approval_assignment.class);
			  assignment.createItems(null);
			}
			assignment.getItems(null).addUnordered(aimEntity);
			assignment.setAssigned_approval(null, aimApproval);
	}

	/**
	 * Method removes assigned entity to base entity and related entities with it.
	 * @param context SdaiContext.
	 * @param assignedEntity assigned entity class.
	 * @param linkAtribute attribute which links to base AIM entity.
	 * @param attributesToCheck array of attribute names which must be checked. If attribute value (Entity) used only by assigned entity it will be deleted.
	 * @param roleName role value.
	 * @param baseAimEntity entity which linked by assignedEntity.
	 * @throws SdaiException
	 */
	public static void clearAssignment(SdaiContext context, Class assignedEntity, String linkAtribute, String roleName, EEntity baseAimEntity) throws SdaiException {
		AEntity aAssignment = new AEntity();

		try {
			//create assignment classes
			String entityName = assignedEntity.getName();
			int lastDotIndex = entityName.lastIndexOf('.');

			String packageName = entityName.substring(0, lastDotIndex);
//			String packageName = assignedEntity.getPackage().getName();

			if (lastDotIndex != -1) {
				entityName = entityName.substring(lastDotIndex + 1);
			}
			entityName = entityName.substring(1);

			Class cEntityClass = Class.forName(packageName + "." + "C" + entityName);

			java.lang.reflect.Method methods [] = cEntityClass.getMethods();
			java.lang.reflect.Method usedinMethod = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("usedin" + linkAtribute)) {
					usedinMethod = methods[i];
				}
			}
			Object parameters [] = new Object[] {null, baseAimEntity, context.domain, aAssignment};
			usedinMethod.invoke(null, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}


		EEntity assignment = null;
		AEntity users = new AEntity();
		for (int i = 1; i <= aAssignment.getMemberCount(); i++) {
			assignment = aAssignment.getByIndexEntity(i);
			EAttribute assignmentAttribute = assignment.getAttributeDefinition(linkAtribute.toLowerCase());
			AEntity items = (AEntity) assignment.get_object(assignmentAttribute);

			EAttribute aimAttribute = assignment.getAttributeDefinition("role");
			EEntity role = (EEntity) assignment.get_object(aimAttribute);

			EAttribute roleNameAttribute = role.getAttributeDefinition("name");
			if (role.testAttribute(roleNameAttribute, null) == 0) {
				continue;
			}
			String roleNameValue = (String) role.get_object(roleNameAttribute);

			//test role name
			if (roleNameValue.equals(roleName)) {
				while (items.isMember(baseAimEntity)) {
					items.removeUnordered(baseAimEntity);
				}
			   if (items.getMemberCount() == 0) {
					users.clear();
					role.findEntityInstanceUsers(context.domain, users);
					if (users.getMemberCount() == 1) {
						role.deleteApplicationInstance();
					}
					assignment.deleteApplicationInstance();
			   }
			}
		}
	}

	public static jsdai.SRepresentation_schema.ERepresentation findRepresentation(SdaiContext context, EEntity aimEntity, String repName) throws SdaiException {
		return findRepresentation(context, aimEntity, null, repName);
	}


	/**
	 * Searches for representation assigned in following path:
	 * product_defintition <- property_definition <- property_definition_representation -> representation
	 * shape_aspect <- property_definition <- property_definition_representation -> representation
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param repName representation name.
	 * @param pdName property_definition name
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ERepresentation findRepresentation(SdaiContext context, EEntity aimEntity, String pdName, String repName) throws SdaiException {
		return findRepresentation(context, aimEntity, null, pdName, null, repName);
	}

	/**
	 * Searches for representation assigned in following path:
	 * product_defintition <- property_definition <- property_definition_representation -> representation
	 * shape_aspect <- property_definition <- property_definition_representation -> representation
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param repName representation name.
	 * @param pdName property_definition name
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ERepresentation findRepresentation(SdaiContext context, EEntity aimEntity, EEntity_definition pdDefinition, String pdName, EEntity_definition repDefinition, String repName) throws SdaiException {

		if (pdDefinition == null) {
			pdDefinition = CProperty_definition.definition;
		}

		if (repDefinition == null) {
			repDefinition = jsdai.SRepresentation_schema.CRepresentation.definition;
		}

		//property_definition
	   LangUtils.Attribute_and_value_structure[] propertyStructure = null;

	   if (pdName == null) {
	      propertyStructure = new LangUtils.Attribute_and_value_structure[]
												{
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), aimEntity),
												};
	   } else {
	      propertyStructure = new LangUtils.Attribute_and_value_structure[]
												{
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeName(null), pdName),
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), aimEntity),
												};
	   }

	   EProperty_definition property_definition  = (EProperty_definition) LangUtils.createInstanceIfNeeded(context, pdDefinition, propertyStructure);
	   if (!property_definition.testName(null)) {
			property_definition.setName(null, "");
	   }

		//property_definition_representation
		AProperty_definition_representation aPdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aPdr);

		EProperty_definition_representation property_definition_representation = null;
		jsdai.SRepresentation_schema.ERepresentation representation = null;
		boolean found = false;
		for (int i = 1; i <= aPdr.getMemberCount(); i++) {
			property_definition_representation = aPdr.getByIndex(i);

			//representation
			if (property_definition_representation.testUsed_representation(null)) {
				 representation = property_definition_representation.getUsed_representation(null);
             // Require exact type, since putting various items (mainly DRI)
             // into geometrical representations (e.g. CSG) is not acceptable
				 if (representation.getInstanceType() == repDefinition) {
					 if (repName == null) {
						found = true;
						break;
					 } else if (representation.testName(null)) {
						String name = representation.getName(null);
						if (name.equals(repName)) {
							found = true;
							break;
						}
					 }
				 }
			}
		}

		if (!found) {
			if (repName == null) {
				representation = createRepresentation(context, repDefinition, "", false);
			} else {
				representation = createRepresentation(context, repDefinition, repName, false);
			}

		   //EProperty_definition_representation
			property_definition_representation = (EProperty_definition_representation) context.working_model.createEntityInstance(EProperty_definition_representation.class);
			property_definition_representation.setDefinition(null, property_definition);
			property_definition_representation.setUsed_representation(null, representation);
		}

		if (!representation.testItems(null)) {
			representation.createItems(null);
		}

		if (!representation.testName(null)) {
			representation.setName(null, "");
		}
		return representation;
	}

	/**
	 * Creates the following path:
	 * xxx <- property_definition <- property_definition_representation -> representation
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param repName representation name.
	 * @param pdName property_definition name
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static void setProperty_definitionToRepresentationPath(SdaiContext context, EEntity aimEntity, EEntity_definition pdDefinition, String pdName, ERepresentation representation) throws SdaiException {

		if (pdDefinition == null) {
			pdDefinition = CProperty_definition.definition;
		}

		//property_definition
	   LangUtils.Attribute_and_value_structure[] propertyStructure = null;

	   if (pdName == null) {
	      propertyStructure = new LangUtils.Attribute_and_value_structure[]
												{
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), aimEntity),
												};
	   } else {
	      propertyStructure = new LangUtils.Attribute_and_value_structure[]
												{
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeName(null), pdName),
													new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), aimEntity),
												};
	   }

	   EProperty_definition property_definition  = (EProperty_definition) LangUtils.createInstanceIfNeeded(context, pdDefinition, propertyStructure);
	   if (!property_definition.testName(null)) {
			property_definition.setName(null, "");
	   }

		//property_definition_representation
	   EProperty_definition_representation property_definition_representation  = (EProperty_definition_representation)
			context.working_model.createEntityInstance(CProperty_definition_representation.definition);
	   
	   property_definition_representation.setDefinition(null, property_definition);
	   property_definition_representation.setUsed_representation(null, representation);
	}

	/**
	 * Deletes the following path:
	 * xxx <- property_definition <- property_definition_representation -> representation
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param repName representation name.
	 * @param pdName property_definition name
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static void unsetProperty_definitionToRepresentationPath(SdaiContext context, EEntity aimEntity, String pdName) throws SdaiException {

		//property_definition
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			// Safety check for derived attributes
/*			if(((CEntity)epd).testAttributeFast(CProperty_definition.attributeName(null), null) < 0){
				continue;
			}*/
			
			// PD to delete
			if((epd.testName(null))&&(epd.getName(null).equals(pdName))){
				AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
				SdaiIterator iterPDR = apdr.createIterator();
				while(iterPDR.next()){
					apdr.getCurrentMember(iterPDR).deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
		
	}

	/**
	 * Searches for representation assigned in following path:
	 * property_definition <- property_definition_representation -> representation
	 * @param context
	 * @param epd property_definition
	 * @param repName representation name.
	 * @param pdName property_definition name
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ERepresentation findRepresentationShort(SdaiContext context, EProperty_definition epd, String repName, String repDescription) throws SdaiException {

		//property_definition_representation
		AProperty_definition_representation aPdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, epd, context.domain, aPdr);

		EProperty_definition_representation property_definition_representation = null;
		jsdai.SRepresentation_schema.ERepresentation representation = null;
		boolean found = false;
		for (int i = 1; i <= aPdr.getMemberCount(); i++) {
			property_definition_representation = aPdr.getByIndex(i);

			//representation
			if (property_definition_representation.testUsed_representation(null)) {
				 representation = property_definition_representation.getUsed_representation(null);
             // Require exact type, since putting various items (mainly DRI)
             // into geometrical representations (e.g. CSG) is not acceptable
				 boolean matchName = (repName==null);
				 boolean matchDescription = (repDescription==null);
				 if ((repName != null)&&(representation.testName(null))) {
					String name = representation.getName(null);
					if (name.equals(repName)) {
						matchName = true;
					}
				 }
				 if ((repDescription != null)&&(representation.testDescription(null))) {
					String description = representation.getDescription(null);
					if (description.equals(repDescription)) {
						matchDescription = true;
					}
				 }
				 if(matchName && matchDescription){
				 	found = true;
				 	break;
				 }
			}
		}

		if (!found) {
			if (repName == null) {
				representation = createRepresentation(context, "", false);
			} else {
				representation = createRepresentation(context, repName, false);
			}
			if(repDescription != null){
				setRepresentationDescription(context, representation, repDescription);
			}
		   //EProperty_definition_representation
			property_definition_representation = (EProperty_definition_representation) context.working_model.createEntityInstance(EProperty_definition_representation.class);
			property_definition_representation.setDefinition(null, epd);
			property_definition_representation.setUsed_representation(null, representation);
		}

		if (!representation.testItems(null)) {
			representation.createItems(null);
		}

		if (!representation.testName(null)) {
			representation.setName(null, "");
		}
		return representation;
	}
	
	
	/**
	 * Searches for representation assigned in following path:
	 * product_defintition <- property_definition <- property_definition_representation -> representation
	 * shape_aspect <- property_definition <- property_definition_representation -> representation
	 * and removes representation items with specified name.
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param pdName property_definition name
	 * @param repName representation name
	 * @param rItemName representation_item name
	 * @throws SdaiException
	 */
	public static void clearRepresentationItems(SdaiContext context, EEntity aimEntity, String pdName, String repName, String rItemName) throws SdaiException {

		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			if ((pdName == null)
			   || (property_definition.testName(null) && property_definition.getName(null).equals(pdName))
			  ) {

				//property_definition_representation
				EProperty_definition_representation property_definition_representation = null;
				AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
				for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
					property_definition_representation = aProperty_definition_representation.getByIndex(j);

					//representation
					if (property_definition_representation.testUsed_representation(null)) {
						ERepresentation representation = property_definition_representation.getUsed_representation(null);

						if ((repName == null) || (representation.testName(null) && representation.getName(null).equals(repName))) {
							if (representation.testItems(null)) {
								ARepresentation_item aItem = representation.getItems(null);
								ERepresentation_item item = null;

								int k = 1;
								while (k <= aItem.getMemberCount()) {
									item = aItem.getByIndex(k);

									if (item.testName(null)
										&& item.getName(null).equals(rItemName)) {
										aItem.removeUnordered(item);
									} else {
										k++;
									}
								}

								if (aItem.getMemberCount() == 0 && CxAP210ARMUtilities.countEntityUsers(context, representation) <= 1) {
                           ERepresentation_context erc = representation.getContext_of_items(null);
                           // this means context is used only by representation which we are going to remove
                           if(CxAP210ARMUtilities.countEntityUsers(context, erc) <= 1)
                              erc.deleteApplicationInstance();
									representation.deleteApplicationInstance();
									property_definition_representation.deleteApplicationInstance();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Searches for representation assigned in following path:
	 * property_definition <- property_definition_representation -> representation
	 * and removes representation items with specified name.
	 * @param context
	 * @param aimEntity characterized_definition
	 * @param pdName property_definition name
	 * @param repName representation name
	 * @param rItemName representation_item name
	 * @throws SdaiException
	 */
	public static void clearRepresentationItemsShort(SdaiContext context, EProperty_definition aimEntity, String repName, String rItemName) throws SdaiException {

			//property_definition_representation
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, aimEntity, context.domain, aProperty_definition_representation);
			for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
				property_definition_representation = aProperty_definition_representation.getByIndex(j);

				//representation
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation representation = property_definition_representation.getUsed_representation(null);

					if ((repName == null) || (representation.testName(null) && representation.getName(null).equals(repName))) {
						if (representation.testItems(null)) {
							ARepresentation_item aItem = representation.getItems(null);
							ERepresentation_item item = null;

							int k = 1;
							while (k <= aItem.getMemberCount()) {
								item = aItem.getByIndex(k);

								if (item.testName(null)
									&& item.getName(null).equals(rItemName)) {
									aItem.removeUnordered(item);
								} else {
									k++;
								}
							}

							if (aItem.getMemberCount() == 0 && CxAP210ARMUtilities.countEntityUsers(context, representation) <= 1) {
                       ERepresentation_context erc = representation.getContext_of_items(null);
                       // this means context is used only by representation which we are going to remove
                       if(CxAP210ARMUtilities.countEntityUsers(context, erc) <= 1)
                          erc.deleteApplicationInstance();
								representation.deleteApplicationInstance();
								property_definition_representation.deleteApplicationInstance();
							}
						}
					}
				}
			}
	}
	
	/**
	 * Relates product_definition with shape_asspect in following path:
	 * product_definition <- property_definition_shape <- shape_aspect <- shape_aspect_relationship -> shape_aspect
	 * @param context
	 * @param product_definition product_definition entity to realate with shape_aspect.
	 * @param aimShape_aspect shape_aspect entity to realate with product_definition.
	 * @param shapeAspectRelationshipName shape aspect relationship name.
	 * @throws SdaiException
	 */
	public static void relateProduct_definitionWithShape_aspect(SdaiContext context, jsdai.SProduct_definition_schema.EProduct_definition product_definition, EShape_aspect aimShape_aspect, String shapeAspectRelationshipName) throws SdaiException {
		relateProduct_definitionWithShape_aspect(context, product_definition, null, aimShape_aspect, shapeAspectRelationshipName);
	}


	/**
	 * Relates product_definition with shape_asspect in following path:
	 * product_definition <- property_definition_shape <- shape_aspect <- shape_aspect_relationship -> shape_aspect
	 * @param context
	 * @param product_definition product_definition entity to realate with shape_aspect.
	 * @param shape_aspectDefinition intermediate shape_aspect definition
	 * @param aimShape_aspect shape_aspect entity to realate with product_definition.
	 * @param shapeAspectRelationshipName shape aspect relationship name.
	 * @throws SdaiException
	 */
	public static void relateProduct_definitionWithShape_aspect(SdaiContext context, jsdai.SProduct_definition_schema.EProduct_definition product_definition, EEntity_definition shape_aspectDefinition, EShape_aspect aimShape_aspect, String shapeAspectRelationshipName) throws SdaiException {
		if (shape_aspectDefinition == null) {
			shape_aspectDefinition = CShape_aspect.definition;
		}

		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, product_definition, context.domain, aProperty_definition);
		EProperty_definition property_definition = null;
		EProduct_definition_shape product_definition_shape = null;


		if (aProperty_definition.getMemberCount() > 0) {
			//searches for property_definition which is product_definition_shape
			for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
				property_definition = aProperty_definition.getByIndex(i);
				if (property_definition instanceof EProduct_definition_shape) {
				   product_definition_shape = (EProduct_definition_shape) property_definition;
				   break;
				}
			}
			property_definition = aProperty_definition.getByIndex(1);
		} else {
			//new property_definition_shape
		   product_definition_shape  = (jsdai.SProduct_property_definition_schema.EProduct_definition_shape) context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.EProduct_definition_shape.class);
		   product_definition_shape.setDefinition(null, product_definition);
		   product_definition_shape.setName(null, "");
		}

		//change entity type
		if (product_definition_shape == null) {
			product_definition_shape = (EProduct_definition_shape) context.working_model.substituteInstance(property_definition, EProduct_definition_shape.class);
		}

		//shape_aspect
	   LangUtils.Attribute_and_value_structure[] shapeAspectStructure =
												 {new LangUtils.Attribute_and_value_structure(
													  CShape_aspect.attributeOf_shape(null), product_definition_shape),
												};
	   EShape_aspect shape_aspect = (EShape_aspect) LangUtils.createInstanceIfNeeded(context, shape_aspectDefinition, shapeAspectStructure);

	   if (!shape_aspect.testName(null)) {
		  shape_aspect.setName(null, "");
	   }

	   if (!shape_aspect.testProduct_definitional(null)) {
		  shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
	   }

		//shape_aspect_relationship
		EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
		shape_aspect_relationship.setRelated_shape_aspect(null, shape_aspect);
		shape_aspect_relationship.setRelating_shape_aspect(null, aimShape_aspect);
		shape_aspect_relationship.setName(null, shapeAspectRelationshipName);
	}

/**
	 * Unrelates product_definition with shape_asspects in following path:
	 * product_definition <- property_definition_shape <- shape_aspect <- shape_aspect_relationship -> shape_aspect
	 * @param context
	 * @param product_definition product_definition entity to realate with shape_aspect.
	 * @param shapeAspectRelationshipName shape aspect relationship name.
	 * @throws SdaiException
	 */
	public static void unrelateProduct_definitionWithShape_aspect(SdaiContext context, jsdai.SProduct_definition_schema.EProduct_definition product_definition, String shapeAspectRelationshipName) throws SdaiException {
		unrelateProduct_definitionWithShape_aspect(context, product_definition, null, shapeAspectRelationshipName);
	}

	/**
	 * Unrelates product_definition with shape_asspects in following path:
	 * product_definition <- property_definition_shape <- shape_aspect <- shape_aspect_relationship -> shape_aspect
	 * @param context
	 * @param product_definition product_definition entity to realate with shape_aspect.
	 * @param shapeAspectRelationshipName shape aspect relationship name.
	 * @throws SdaiException
	 */
	public static void unrelateProduct_definitionWithShape_aspect(SdaiContext context, jsdai.SProduct_definition_schema.EProduct_definition product_definition, EEntity_definition shape_aspectDefinition, String shapeAspectRelationshipName) throws SdaiException {
		if (shape_aspectDefinition == null) {
			shape_aspectDefinition = CShape_aspect.definition;
		}

		//property_definition
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, product_definition, context.domain, aProperty_definition);
		EProperty_definition property_definition = null;
		EProduct_definition_shape product_definition_shape = null;
		EShape_aspect shape_aspect = null;
		AShape_aspect aShape_aspect = new AShape_aspect();
		EShape_aspect_relationship relationship = null;
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();

		//searches for property_definition which is product_definition_shape
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition instanceof EProduct_definition_shape) {
			   product_definition_shape = (EProduct_definition_shape) property_definition;
			   aShape_aspect.clear();
			   CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);

			   for (int j = 1; j <= aShape_aspect.getMemberCount(); j++) {
			       shape_aspect = aShape_aspect.getByIndex(j);

				   if (shape_aspect.getInstanceType().isSubtypeOf(shape_aspectDefinition)) {
					   aRelationship.clear();
					   CShape_aspect_relationship.usedinRelated_shape_aspect(null, shape_aspect, context.domain, aRelationship);
					   for (int k = 1; k <= aRelationship.getMemberCount(); k++) {
						   relationship = aRelationship.getByIndex(k);
						   if (relationship.testName(null) && relationship.getName(null).equals(shapeAspectRelationshipName) ) {
							  relationship.deleteApplicationInstance();
						   }
					   }
				   }
			   }
			}
		}
	}

	/**
	 * Creates product_definition_context entity instance with reused application_context.
	 * @param context
	 * @param pdcName product_definition_context.name attribute value.
	 * @param pdcLifeCycleStage product_definition_context.life_cycle_stage attribute value.
	 * @param pdcReuse flag: true reuse existing product_definition_context entity, false create new.
	 * @return product_definition_context entity instance.
	 * @throws SdaiException
	 */
	public static EProduct_definition_context createProduct_definition_context(SdaiContext context, String pdcName, String pdcLifeCycleStage, boolean pdcReuse) throws SdaiException {
		return createProduct_definition_context(context, pdcName, pdcLifeCycleStage, pdcReuse, "electronic_assembly_interconnect_and_packaging_design", true);
	}

	/**
	 * Creates product_definition_context as part definition type pattern captured in SEDS-1491.
	 * @param context
	 * @param armEntity - entity which PDC will be assigned to.
	 * @param pdcName product_definition_context.name attribute value.
	 * @throws SdaiException
	 */
	public static void assignPart_definition_type(SdaiContext context, EProduct_definition armEntity, String pdcName) throws SdaiException {
		// PDC
	   LangUtils.Attribute_and_value_structure[] pdcStructure = {
	      new LangUtils.Attribute_and_value_structure(CProduct_definition_context.attributeName(null), pdcName)
	   };
	   EProduct_definition_context epdc = (EProduct_definition_context)
	      LangUtils.createInstanceIfNeeded(context, CProduct_definition_context.definition, pdcStructure);
	   // Fill AIM gaps
	   if(!epdc.testFrame_of_reference(null)){
		   EApplication_context ac = createApplication_context(context, "", true);
		   epdc.setFrame_of_reference(null, ac);
	   }
	   if(!epdc.testLife_cycle_stage(null)){
		   epdc.setLife_cycle_stage(null, "");
	   }
	   
		// Role
	   LangUtils.Attribute_and_value_structure[] roleStructure = {
	      new LangUtils.Attribute_and_value_structure(CProduct_definition_context_role.attributeName(null), "part definition type")
	   };
	   EProduct_definition_context_role role = (EProduct_definition_context_role)
	      LangUtils.createInstanceIfNeeded(context, CProduct_definition_context_role.definition, roleStructure);
		
		// PDCA
		EProduct_definition_context_association epdca = (EProduct_definition_context_association)
			context.working_model.createEntityInstance(CProduct_definition_context_association.definition);
		epdca.setFrame_of_reference(null, epdc);
		epdca.setRole(null, role);
		epdca.setDefinition(null, armEntity);
	}

	/**
	 * Creates product_definition_context as part definition type pattern captured in SEDS-1491.
	 * @param context
	 * @param armEntity - entity which PDC will be assigned to.
	 * @param pdcName product_definition_context.name attribute value.
	 * @throws SdaiException
	 */
	public static void deassignPart_definition_type(SdaiContext context, EProduct_definition armEntity, String pdcName) throws SdaiException {
		// PDCA
		AProduct_definition_context_association apdca = new AProduct_definition_context_association();
		CProduct_definition_context_association.usedinDefinition(null, armEntity, context.domain, apdca);
		for(int i=1, count = apdca.getMemberCount();i<=count;i++){
			EProduct_definition_context_association epdca = apdca.getByIndex(i);
			if(!epdca.testFrame_of_reference(null))
				continue;
			EProduct_definition_context epdc = epdca.getFrame_of_reference(null);
			if((epdc.testName(null))&&(epdc.getName(null).equals(pdcName)))
				epdca.deleteApplicationInstance();
		}
	}
	
	/**
	 * Creates product_definition_context entity instance.
	 * @param context
	 * @param pdcName product_definition_context.name attribute value.
	 * @param pdcLifeCycleStage product_definition_context.life_cycle_stage attribute value.
	 * @param pdcReuse flag: true reuse existing product_definition_context entity, false create new.
	 * @param apcApplication application_context.Application attribute value.
	 * @param apcReuse flag: true reuse existing application_context entity, false create new.
	 * @return product_definition_context entity instance.
	 * @throws SdaiException
	 */
	public static EProduct_definition_context createProduct_definition_context(SdaiContext context, String pdcName, String pdcLifeCycleStage, boolean pdcReuse, String apcApplication, boolean apcReuse) throws SdaiException {
		EProduct_definition_context product_definition_context = null;

		if (pdcReuse) {
			//product_definition_context
			LangUtils.Attribute_and_value_structure[] pdcStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProduct_definition_context.attributeName(null), pdcName),
														new LangUtils.Attribute_and_value_structure(CProduct_definition_context.attributeLife_cycle_stage(null), pdcLifeCycleStage)
													};
			product_definition_context = (EProduct_definition_context)
													LangUtils.createInstanceIfNeeded(
																			context,
																			CProduct_definition_context.definition,
																			pdcStructure);
		} else {
			product_definition_context = (EProduct_definition_context) context.working_model.createEntityInstance(EProduct_definition_context.class);
			product_definition_context.setName(null, pdcName);
			product_definition_context.setLife_cycle_stage(null, pdcLifeCycleStage);
		}

		//application_context
		if (!product_definition_context.testFrame_of_reference(null)) {
			EApplication_context application_context  = createApplication_context(context, apcApplication, apcReuse);
			product_definition_context.setFrame_of_reference(null, application_context);
		}
		return product_definition_context;
	}


	/**
	 * Creates aim application_context entity instance.
	 * @param context
	 * @param apcApplication application_context.Application attribute value.
	 * @param apcReuse flag: true reuse existing entity, false create new.
	 * @return application_context entity
	 * @throws SdaiException
	 */
	public static EApplication_context createApplication_context(SdaiContext context, String apcApplication, boolean apcReuse) throws SdaiException {
		   EApplication_context application_context = null;
		   if (apcReuse) {
			LangUtils.Attribute_and_value_structure[] acsStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CApplication_context.attributeApplication(null), apcApplication),
													};
			application_context = (EApplication_context) LangUtils.createInstanceIfNeeded(
																					context,
																					CApplication_context.definition,
																					acsStructure);
		   } else {
			 application_context = (EApplication_context) context.working_model.createEntityInstance(EApplication_context.class);
			 application_context.setApplication(null, apcApplication);
		   }
		   return application_context;
	}


	/**
	 * Creates aim product_definition_relationship entity instance.
	 * @param context
	 * @param pdrName
	 * @param pdrRelating
	 * @param pdrRelated
	 * @param pdrReuse
	 * @return
	 * @throws SdaiException
	 */
	public static EProduct_definition_relationship createProduct_definition_relationship(SdaiContext context, String pdrName, EProduct_definition pdrRelating, EProduct_definition pdrRelated, boolean pdrReuse) throws SdaiException {
		EProduct_definition_relationship relationship = null;

		if (pdrReuse) {
			LangUtils.Attribute_and_value_structure[] pdrStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProduct_definition_relationship.attributeName(null), pdrName),
														new LangUtils.Attribute_and_value_structure(CProduct_definition_relationship.attributeRelating_product_definition(null), pdrRelating),
														new LangUtils.Attribute_and_value_structure(CProduct_definition_relationship.attributeRelated_product_definition(null), pdrRelated),
													};

			relationship = (EProduct_definition_relationship) LangUtils.createInstanceIfNeeded(
																					context,
																					CProduct_definition_relationship.definition,
																					pdrStructure);
			if (relationship.testId(null)) {
				relationship.setId(null, "");
			}
		} else {
			relationship = (EProduct_definition_relationship) context.working_model.createEntityInstance(EProduct_definition_relationship.class);
			relationship.setId(null, "");
			if (pdrName != null) {
			   relationship.setName(null, pdrName);
			}

			if (pdrRelated != null) {
			   relationship.setRelated_product_definition(null, pdrRelated);
			}

			if (pdrRelating != null) {
			   relationship.setRelating_product_definition(null, pdrRelating);
			}
		}
		return relationship;
	}

	/**
	 * Creates aim ERepresentation entity instance.
	 * @param context
	 * @param rName representation name.
	 * @param rReuse reuse flag.
	 * @return ERepresentation entity instance.
	 * @throws SdaiException
	 */
	public static ERepresentation createRepresentation(SdaiContext context, String rName, boolean rReuse) throws SdaiException {
		return createRepresentation(context, null, rName, rReuse);
	}

	/**
	 * Creates aim ERepresentation entity instance.
	 * @param context
	 * @param rName representation name.
	 * @param rReuse reuse flag.
	 * @return ERepresentation entity instance.
	 * @throws SdaiException
	 */
	public static ERepresentation createRepresentation(SdaiContext context, EEntity_definition definition, String rName, boolean rReuse) throws SdaiException {
		   ERepresentation representation = null;

		   if (definition == null) {
			  definition = jsdai.SRepresentation_schema.CRepresentation.definition;
		   }

		   if (rReuse) {
			LangUtils.Attribute_and_value_structure[] rStructure =
													 {
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation.attributeName(null), rName),
													};

			representation = (ERepresentation) LangUtils.createInstanceIfNeeded(
																					context,
																					definition,
																					rStructure);
		   } else {
				//ERepresentation
				representation = (ERepresentation) context.working_model.createEntityInstance(definition);
				representation.setName(null, rName);
		   }

		   if (!representation.testItems(null)) {
		      representation.createItems(null);
		   }

		   //ERepresentation_context
		   if (!representation.testContext_of_items(null)) {
			  LangUtils.Attribute_and_value_structure[] rcStructure =
													 {
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "")
													};

				ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
																					context,
																					jsdai.SRepresentation_schema.CRepresentation_context.definition,
																					rcStructure);

				/*jsdai.SGeometry_schema.EGeometric_representation_context representation_context = (jsdai.SGeometry_schema.EGeometric_representation_context) LangUtils.createInstanceIfNeeded(
																					context,
																					jsdai.SGeometry_schema.CGeometric_representation_context.definition,
																					rcStructure);

				//Geometric_representation_context.coordinate_space_dimension
				if (!representation_context.testCoordinate_space_dimension(null)) {
					representation_context.setCoordinate_space_dimension(null, 2);
				}*/

				representation.setContext_of_items(null, representation_context);
		   }
		   return representation;
	}



	/**
	 * Creates or reuses geometric_representation_context
	 * @param context
	 * @param identifier
	 * @param type
	 * @param dimension
	 * @param reuse
	 * @return
	 * @throws SdaiException
	 */
	public static jsdai.SGeometry_schema.EGeometric_representation_context createGeometric_representation_context(SdaiContext context, String identifier, String type, int dimension, boolean reuse) throws SdaiException {
		jsdai.SGeometry_schema.EGeometric_representation_context geometric_representation_context = null;

		if (reuse) {
		   LangUtils.Attribute_and_value_structure[] rcStructure =
													 {
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), identifier),
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), type),
														new LangUtils.Attribute_and_value_structure(jsdai.SGeometry_schema.CGeometric_representation_context.attributeCoordinate_space_dimension(null), new Integer(dimension))

													};


		   geometric_representation_context = (jsdai.SGeometry_schema.EGeometric_representation_context) LangUtils.createInstanceIfNeeded(
															context,
															jsdai.SGeometry_schema.CGeometric_representation_context.definition,
															rcStructure);

		} else {
			   geometric_representation_context = (jsdai.SGeometry_schema.EGeometric_representation_context) context.working_model.createEntityInstance(CGeometric_representation_context.definition);
			   geometric_representation_context.setContext_identifier(null, identifier);
			   geometric_representation_context.setContext_type(null, type);
			   geometric_representation_context.setCoordinate_space_dimension(null, dimension);
		}

		return geometric_representation_context;
	}
	
	/**
	 * Creates or reuses representation_context
	 * @param context
	 * @param identifier
	 * @param type
	 * @param dimension
	 * @param reuse
	 * @return
	 * @throws SdaiException
	 */
	public static ERepresentation_context createRepresentation_context(SdaiContext context, String identifier, String type, boolean reuse) throws SdaiException {
		ERepresentation_context representation_context = null;

		if (reuse) {
		   LangUtils.Attribute_and_value_structure[] rcStructure =
													 {
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), identifier),
														new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), type)
													};


		   representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
															context,
															CRepresentation_context.definition,
															rcStructure);

		} else {
			   representation_context = (ERepresentation_context) context.working_model.createEntityInstance(CRepresentation_context.definition);
			   representation_context.setContext_identifier(null, identifier);
			   representation_context.setContext_type(null, type);
		}

		return representation_context;
	}
	
	
	/**
	 * Creates EProperty_definition instance.
	 * @param context
	 * @param definition EProperty_definition subtype definition
	 * @param pdName EProperty_definition name.
	 * @param pdDescription EProperty_definition description.
	 * @param pdDefinition EProperty_definition definition.
	 * @param pdReuse reuse flag.
	 * @return EProperty_definition instance.
	 * @throws SdaiException
	 */
	public static EProperty_definition createProperty_definition(SdaiContext context, EEntity_definition definition, String pdName, String pdDescription, EEntity pdDefinition, boolean pdReuse) throws SdaiException {
		EProperty_definition property_definition = null;
		if (definition == null) {
			definition = CProperty_definition.definition;
		}

		if (pdReuse) {
			LangUtils.Attribute_and_value_structure[] pdStructure =
													 {
														new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeName(null), pdName),
														new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDescription(null), pdDescription),
														new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), pdDefinition),
													};

			property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
																					context,
																					definition,
																					pdStructure);

		} else {
			property_definition = (EProperty_definition) context.working_model.createEntityInstance(definition);
			if (pdName != null) {
			   property_definition.setName(null, pdName);
			}

			if (pdDescription != null) {
				property_definition.setDescription(null, pdDescription);
			}

			if (pdDefinition != null) {
			   property_definition.setDefinition(null, pdDefinition);
			}
		}
		return property_definition;
	}

	/**
	 * Creates empty product_definition_formation.
	 * @param context
	 * @return
	 * @throws SdaiException
	 */
	public static EProduct_definition_formation createEmptyProduct_definition_fromation(SdaiContext context) throws SdaiException {
		EApplication_context productApplication_context = CxAP210ARMUtilities.createApplication_context(context, "", true);

		//product_context
		EProduct_context product_context = (EProduct_context) context.working_model.createEntityInstance(EProduct_context.class);
		product_context.setName(null, "");
		product_context.setDiscipline_type(null, "");
		product_context.setFrame_of_reference(null, productApplication_context);

		//product
		EProduct product = (EProduct) context.working_model.createEntityInstance(EProduct.class);
		product.setId(null, "");
		product.setName(null, "");
		product.createFrame_of_reference(null).addUnordered(product_context);

		//product_definition_formation
		EProduct_definition_formation product_definition_formation = (EProduct_definition_formation) context.working_model.createEntityInstance(EProduct_definition_formation.class);
		product_definition_formation.setId(null, "");
		product_definition_formation.setOf_product(null, product);
		return product_definition_formation;
	}

	/**
	 * Counts how many users has entity instance.
	 * @param context
	 * @param entity entity to count users.
	 * @return number of users.
	 * @throws SdaiException
	 */
	public static int countEntityUsers(SdaiContext context, EEntity entity) throws SdaiException {
		AEntity aEntity = new AEntity();
		entity.findEntityInstanceUsers(context.domain, aEntity);
		return aEntity.getMemberCount();
	}

	/**
	 *
	 * @param unit - string passed as a parameter - it represents the unit
	 * @return - conversion factor according milli metres.
	 *    If string is not in the supported list - 1 will be returned
	 */
	public static double getLengthConversionFactorAccordingMilliMetre(String unit){
		double conversionFactor=1; //factor unit -> metre
		double factor = 1;
		// This comes from Mentor
		if((unit.equalsIgnoreCase("inches"))||(unit.equalsIgnoreCase("IN"))||
			(unit.equalsIgnoreCase("inch")))
			conversionFactor = 25.4*factor;
		if((unit.equalsIgnoreCase("tmils"))||(unit.equalsIgnoreCase("TM")))
			conversionFactor = 25.4*0.0001*factor;
		if((unit.equalsIgnoreCase("mils"))||(unit.equalsIgnoreCase("ML"))||
			(unit.equalsIgnoreCase("mil")))
			conversionFactor = 25.4*0.001*factor;
		if(unit.equalsIgnoreCase("tmil"))
			conversionFactor = 25.4*0.01*factor;
		if(unit.equalsIgnoreCase("TN")) // 10 nanometer
			conversionFactor = 0.00001*factor;
		// A place for other known strings/units
		// ...
//		System.out.println(unit+" conversion factor " +conversionFactor);
		return conversionFactor;
	}

	/**
	 * return all Representations related to the shape_aspect passed which
	 * names are the same as the String passed OR really all if null is passed
	 * @param aimEntity - shape_aspect we will search representation for
	 * @param context - SdaiContext, containing domain information for usedin operation.
	 * @param name - the name of representation which user is intrested in.
	 *  if NULL - all related representations will be returned.
	 * @return
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ARepresentation getAllRepresentationsOfShapeAspect(EShape_aspect aimEntity,
		SdaiContext context, String name)throws SdaiException{

		//property_definition
		AProperty_definition propDefinitions = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain, propDefinitions);
		jsdai.SRepresentation_schema.ARepresentation result = new jsdai.SRepresentation_schema.ARepresentation();
		int index = 1;
		for(int i = 1; i <= propDefinitions.getMemberCount(); i++){
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation propDefRepresentaions = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, propDefinitions.getByIndex(i), context.domain, propDefRepresentaions);

			//property_definition_representation
			for(int j = 1; j <= propDefRepresentaions.getMemberCount(); j++) {
				property_definition_representation = propDefRepresentaions.getByIndex(j);
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation temp = property_definition_representation.getUsed_representation(null);
					if(
						(name == null) ||
						(temp.testName(null) && temp.getName(null).equals(name))
					) {
						result.addByIndex(index++, temp);
					}
				}
		   }
		}
		return result;
	}

	/**
	 * return all Representations related to the shape_aspect passed which
	 * names are the same as the String passed OR really all if null is passed
	 * @param aimEntity - characterized_object we will search representation for
	 * @param context - SdaiContext, containing domain information for usedin operation.
	 * @param name - the name of representation which user is intrested in.
	 *  if NULL - all related representations will be returned.
	 * @return
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ARepresentation getAllRepresentationsOfCharacterizedDefinition(EEntity aimEntity,
		SdaiContext context, String name)throws SdaiException{

		//property_definition
		AProperty_definition propDefinitions = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain, propDefinitions);
		jsdai.SRepresentation_schema.ARepresentation result = new jsdai.SRepresentation_schema.ARepresentation();
		int index = 1;
		for(int i = 1; i <= propDefinitions.getMemberCount(); i++){
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation propDefRepresentaions = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, propDefinitions.getByIndex(i), context.domain, propDefRepresentaions);

			//property_definition_representation
			for(int j = 1; j <= propDefRepresentaions.getMemberCount(); j++) {
				property_definition_representation = propDefRepresentaions.getByIndex(j);
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation temp = property_definition_representation.getUsed_representation(null);
					EAttribute attribute = findAttributeAlongSupertypeTree(temp.getInstanceType(), "name");
					if(
						(name == null) || (attribute instanceof EDerived_attribute)||
						(temp.testName(null) && temp.getName(null).equals(name))
					) {
						result.addByIndex(index++, temp);
					}
				}
		   }
		}
		return result;
	}

	private static EAttribute findAttributeAlongSupertypeTree(EEntity_definition type, String name)throws SdaiException{
		EExplicit_attribute[] attributes = getEntityExplicitAttributes(type);
		for(int i=0,count=attributes.length; i<count;i++){
			EExplicit_attribute attribute = attributes[i];
			if(attribute.getPersistentLabel().equals(name)){
				return attribute;
			}
		}
		// Go recursive in supertypes
		AEntity_definition supertypes = type.getSupertypes(null);
		for(int i=1,count=supertypes.getMemberCount(); i<=count;i++){
			EEntity_definition supertype = supertypes.getByIndex(i);
			EAttribute attribute = findAttributeAlongSupertypeTree(supertype, name);
			if(attribute != null){
				return attribute;
			}
		}	
		return null;
	}
	

	/**
	 * return all Representations related to the property_definition passed which
	 * names are the same as the String passed OR really all if null is passed
	 * @param aimEntity - property_definition we will search representation for
	 * @param context - SdaiContext, containing domain information for usedin operation.
	 * @param name - the name of representation which user is intrested in.
	 *  if NULL - all related representations will be returned.
	 * @return
	 * @throws SdaiException
	 */
	public static jsdai.SRepresentation_schema.ARepresentation getAllRepresentationsOfPropertyDefinition(EProperty_definition aimEntity,
		SdaiContext context, String name)throws SdaiException{

		jsdai.SRepresentation_schema.ARepresentation result = new jsdai.SRepresentation_schema.ARepresentation();
		int index=1;

		AProperty_definition_representation propDefRepresentaions = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, aimEntity, context.domain, propDefRepresentaions);
		for(int j=1;j<=propDefRepresentaions.getMemberCount();j++){
			ERepresentation temp = propDefRepresentaions.getByIndex(j).getUsed_representation(null);
			// Need a way to avoid calling derived attributes
			if(temp instanceof ETolerance_characteristic){
				continue;
			}
			if((name == null)||(name.equals(temp.getName(null))))
				result.addByIndex(index++, temp);
		}
		return result;

	}


	/**
	 * This method deletes all descriptive_representation_items, which are in the representation
	 * with the specified name and if this representation is related to shape_aspect, which is passed as a parameter.
	 * @param aimEntity - Shape_aspect for which we will do the job.
	 * @param context - where to search instances, get working model etc.
	 * @param representationName - this is the name passed to getAllRepresentationsOfShapeAspect method.
	 * @param keyword - only descritpive_representation_items, which name is equal to keyword will be deleted
	 * @throws SdaiException
	 */
	public static void unsetRepresentationItemFromShapeAspect(EShape_aspect aimEntity,
		SdaiContext context, String representationName, String keyword)throws SdaiException{

		jsdai.SRepresentation_schema.ARepresentation reps = getAllRepresentationsOfShapeAspect(aimEntity, context, representationName);

		top: for(int i=1;i<=reps.getMemberCount();i++){
			jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
			// Exact types only - since all subtypes are intended for geometry - do not "touch" them
			if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
				if(temp.testItems(null)){
					jsdai.SRepresentation_schema.ARepresentation_item items = temp.getItems(null);
					for(int j=1;j<=items.getMemberCount();j++){
						jsdai.SRepresentation_schema.ERepresentation_item item = items.getByIndex(j);
						if((item instanceof jsdai.SQualified_measure_schema.EDescriptive_representation_item)&&
							(item.getName(null).equals(keyword))){
							item.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * This method deletes all descriptive_representation_items, which are in the representation
	 * with the specified name and if this representation is related to shape_aspect, which is passed as a parameter.
	 * @param aimEntity - characterized_object for which we will do the job.
	 * @param context - where to search instances, get working model etc.
	 * @param representationName - this is the name passed to getAllRepresentationsOfShapeAspect method.
	 * @param keyword - only descritpive_representation_items, which name is equal to keyword will be deleted
	 * @throws SdaiException
	 */
	public static void unsetRepresentationItemFromCharacterizedObject(ECharacterized_object aimEntity,
		SdaiContext context, String representationName, String keyword)throws SdaiException{

		jsdai.SRepresentation_schema.ARepresentation reps = getAllRepresentationsOfCharacterizedDefinition(aimEntity, context, representationName);

		top: for(int i=1;i<=reps.getMemberCount();i++){
			jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
			// Exact types only - since all subtypes are intended for geometry - do not "touch" them
			if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
				if(temp.testItems(null)){
					jsdai.SRepresentation_schema.ARepresentation_item items = temp.getItems(null);
					for(int j=1;j<=items.getMemberCount();j++){
						jsdai.SRepresentation_schema.ERepresentation_item item = items.getByIndex(j);
						if((item instanceof jsdai.SQualified_measure_schema.EDescriptive_representation_item)&&
							(item.getName(null).equals(keyword))){
							item.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}


	/**
	 * This method deletes all descriptive_representation_items, which are in the representation
	 * with the specified name and if this representation is related to property_definition,
	 * which is passed as a parameter.
	 * @param aimEntity - property_definition for which we will do the job.
	 * @param context - where to search instances, get working model etc.
	 * @param representationName - this is the name passed to getAllRepresentationsOfShapeAspect method.
	 * @param keyword - only descritpive_representation_items, which name is equal to keyword will be deleted
	 * @throws SdaiException
	 */
	public static void unsetRepresentationItemFromPropertyDefinition(EProperty_definition aimEntity,
		SdaiContext context, String representationName, String keyword)throws SdaiException{

		jsdai.SRepresentation_schema.ARepresentation reps = getAllRepresentationsOfPropertyDefinition(aimEntity, context, representationName);

		top: for(int i=1;i<=reps.getMemberCount();i++){
			jsdai.SRepresentation_schema.ERepresentation temp = reps.getByIndex(i);
			// Exact types only - since all subtypes are intended for geometry - do not "touch" them
			if(temp.getInstanceType() == jsdai.SRepresentation_schema.CRepresentation.definition){
				if(temp.testItems(null)){
					jsdai.SRepresentation_schema.ARepresentation_item items = temp.getItems(null);
					for(int j=1;j<=items.getMemberCount();j++){
						jsdai.SRepresentation_schema.ERepresentation_item item = items.getByIndex(j);
						if((item instanceof jsdai.SQualified_measure_schema.EDescriptive_representation_item)&&
							(item.testName(null))&&	
							(item.getName(null).equals(keyword))){
							item.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Check whether passed value is in the list of Strings.
	 * @param list - list of Strings, where checking will be performed
	 * @param value - value, which we want to test.
	 * @return - true if value is in list, false - otherwise.
	 */
	public static boolean isStringInList(String[] list, String value){
		for(int i=0;i<list.length;i++){
			if(list[i].equals(value))
				return true;
		}
		return false;
	}


	/**
	 * Returns measure value form EMeasure_with_unit.
	 * @param emwu EMeasure_with_unit instance.
	 * @return measure value as String.
	 * @throws SdaiException
	 */
	public static String getMeasureValue(EMeasure_with_unit emwu) throws SdaiException {
		int type = emwu.testValue_component(null);
		String defValue = "0";
		if(type == 0)
			return defValue;
		switch (type){
			case 2:{
				return String.valueOf(emwu.getValue_component(null,(ELength_measure)null));
			}
			case 3:{
				return String.valueOf(emwu.getValue_component(null,(EMass_measure)null));
			}
			case 4:{
				return String.valueOf(emwu.getValue_component(null,(ETime_measure)null));
			}
			case 5:{
				return String.valueOf(emwu.getValue_component(null,(EElectric_current_measure)null));
			}
			case 6:{
				return String.valueOf(emwu.getValue_component(null,(EThermodynamic_temperature_measure)null));
			}
			case 7:{
				return String.valueOf(emwu.getValue_component(null,(ECelsius_temperature_measure)null));
			}
			case 8:{
				return String.valueOf(emwu.getValue_component(null,(EAmount_of_substance_measure)null));
			}
			case 9:{
				return String.valueOf(emwu.getValue_component(null,(ELuminous_intensity_measure)null));
			}
			case 10:{
				return String.valueOf(emwu.getValue_component(null,(EPlane_angle_measure)null));
			}
			case 11:{
				return String.valueOf(emwu.getValue_component(null,(ESolid_angle_measure)null));
			}
			case 12:{
				return String.valueOf(emwu.getValue_component(null,(EArea_measure)null));
			}
			case 13:{
				return String.valueOf(emwu.getValue_component(null,(EVolume_measure)null));
			}
			case 14:{
				return String.valueOf(emwu.getValue_component(null,(ERatio_measure)null));
			}
			case 15:{
				return String.valueOf(emwu.getValue_component(null,(EParameter_value)null));
			}
			case 16:{
				return String.valueOf(emwu.getValue_component(null,(ENumeric_measure)null));
			}
			case 17:{
				return String.valueOf(emwu.getValue_component(null,(EContext_dependent_measure)null));
			}
			case 18:{
				return String.valueOf(emwu.getValue_component(null,(ENumeric_measure)null));
			}
			case 19:{
				return String.valueOf(emwu.getValue_component(null,(EDescriptive_measure)null));
			}
			case 20:{
				return String.valueOf(emwu.getValue_component(null,(EPositive_length_measure)null));
			}
			case 21:{
				return String.valueOf(emwu.getValue_component(null,(EPositive_plane_angle_measure)null));
			}
			case 22:{
				//return String.valueOf(emwu.getValue_component(null,(EPositive_ratio_measure)null));
				return defValue;
			}
			case 23:{
				return String.valueOf(emwu.getValue_component(null,(ECount_measure)null));
			}
		}
		return defValue;
	}


	/**
	 * Sets measure value to EMeasure_with_unit.
	 * @param emwu EMeasure_with_unit instance to set value.
	 * @param value value to set.
	 * @param type type of set.
	 * @throws SdaiException
	 */
	public static void setMeasureValue(EMeasure_with_unit emwu, String value, int type) throws SdaiException {
		if(type == 0)
			emwu.setValue_component(null, Double.parseDouble(value), (ELength_measure) null);
		switch (type){
			case 2:{
				emwu.setValue_component(null, Double.parseDouble(value),(ELength_measure)null);
			}
			case 3:{
				emwu.setValue_component(null, Double.parseDouble(value),(EMass_measure)null);
			}
			case 4:{
				emwu.setValue_component(null, Double.parseDouble(value),(ETime_measure)null);
			}
			case 5:{
				emwu.setValue_component(null, Double.parseDouble(value),(EElectric_current_measure)null);
			}
			case 6:{
				emwu.setValue_component(null, Double.parseDouble(value),(EThermodynamic_temperature_measure)null);
			}
			case 7:{
				emwu.setValue_component(null, Double.parseDouble(value),(ECelsius_temperature_measure)null);
			}
			case 8:{
				emwu.setValue_component(null, Double.parseDouble(value),(EAmount_of_substance_measure)null);
			}
			case 9:{
				emwu.setValue_component(null, Double.parseDouble(value),(ELuminous_intensity_measure)null);
			}
			case 10:{
				emwu.setValue_component(null, Double.parseDouble(value),(EPlane_angle_measure)null);
			}
			case 11:{
				emwu.setValue_component(null, Double.parseDouble(value),(ESolid_angle_measure)null);
			}
			case 12:{
				emwu.setValue_component(null, Double.parseDouble(value),(EArea_measure)null);
			}
			case 13:{
				emwu.setValue_component(null, Double.parseDouble(value),(EVolume_measure)null);
			}
			case 14:{
				emwu.setValue_component(null, Double.parseDouble(value),(ERatio_measure)null);
			}
			case 15:{
				emwu.setValue_component(null, Double.parseDouble(value),(EParameter_value)null);
			}
			case 16:{
				emwu.setValue_component(null, Double.parseDouble(value),(ENumeric_measure)null);
			}
			case 17:{
				emwu.setValue_component(null, Double.parseDouble(value),(EContext_dependent_measure)null);
			}
			case 18:{
				emwu.setValue_component(null, Double.parseDouble(value),(ENumeric_measure)null);
			}
			case 19:{
				emwu.setValue_component(null, value,(EDescriptive_measure)null);
			}
			case 20:{
				emwu.setValue_component(null, Double.parseDouble(value),(EPositive_length_measure)null);
			}
			case 21:{
				emwu.setValue_component(null, Double.parseDouble(value),(EPositive_plane_angle_measure)null);
			}
			case 22:{
				//emwu.setValue_component(null, Double.parseDouble(value),(EPositive_ratio_measure)null);
				emwu.setValue_component(null, Double.parseDouble(value), (ELength_measure) null);
			}
			case 23:{
				emwu.setValue_component(null, Double.parseDouble(value),(ECount_measure)null);
			}
		}
	}


	/**
	 * Sets product_definition derived attribute name.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setProduct_definitionName(SdaiContext context, EProduct_definition product_definition, String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, product_definition, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
		} else {
			name_attribute = (jsdai.SBasic_attribute_schema.EName_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EName_attribute.class);
			name_attribute.setNamed_item(null, product_definition);
		}
		name_attribute.setAttribute_value(null, name);
	}

	/**
	 * Gets product_definition derived attribute name.
	 * @param context
	 * @param product_definition
	 * @return
	 * @throws SdaiException
	 */
	public static String getProduct_definitionName(SdaiContext context, EProduct_definition product_definition) throws SdaiException {
		String name = null;
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, product_definition, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
			if (name_attribute.testAttribute_value(null)) {
				name = name_attribute.getAttribute_value(null);
			}
		}
		return name;
	}

	/**
	 * Sets property_definition_representation derived attribute name.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setProperty_definition_representationName(SdaiContext context, EProperty_definition_representation property_definition_representation, String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, property_definition_representation, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
		} else {
			name_attribute = (jsdai.SBasic_attribute_schema.EName_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EName_attribute.class);
			name_attribute.setNamed_item(null, property_definition_representation);
		}
		name_attribute.setAttribute_value(null, name);
	}

	/**
	 * Unsets property_definition_representation derived attribute name.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void unsetProperty_definition_representationName(SdaiContext context, EProperty_definition_representation property_definition_representation) throws SdaiException {
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, property_definition_representation, context.domain, aName_attribute);
		int count = aName_attribute.getMemberCount();
		for(int i=1;i<=count;i++){
			name_attribute = aName_attribute.getByIndex(i);
			name_attribute.deleteApplicationInstance();
		}
	}

	/**
	 * Gets property_definition_representation derived attribute name.
	 * @param context
	 * @param product_definition
	 * @return
	 * @throws SdaiException
	 */
	public static String getProperty_definition_representationName(SdaiContext context, EProperty_definition_representation property_definition_representation) throws SdaiException {
		String name = null;
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, property_definition_representation, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
			if (name_attribute.testAttribute_value(null)) {
				name = name_attribute.getAttribute_value(null);
			}
		}
		return name;
	}

	/**
	 * Gets property_definition_representation derived attribute name.
	 * @param context
	 * @param product_definition
	 * @return
	 * @throws SdaiException
	 */
	public static String getProperty_definition_representationDescription(SdaiContext context, EProperty_definition_representation property_definition_representation) throws SdaiException {
		String name = null;
		EDescription_attribute name_attribute = null;
		ADescription_attribute aName_attribute = new ADescription_attribute();
		CDescription_attribute.usedinDescribed_item(null, property_definition_representation, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
			if (name_attribute.testAttribute_value(null)) {
				name = name_attribute.getAttribute_value(null);
			}
		}
		return name;
	}
	

	/**
	 * Sets representation derived attribute description.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setRepresentationDescription(SdaiContext context, jsdai.SRepresentation_schema.ERepresentation representation, String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EDescription_attribute description_attribute = null;
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute.usedinDescribed_item(null, representation, context.domain, aDescription_attribute);
		if (aDescription_attribute.getMemberCount() > 0) {
			description_attribute = aDescription_attribute.getByIndex(1);
		} else {
			description_attribute = (jsdai.SBasic_attribute_schema.EDescription_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EDescription_attribute.class);
			description_attribute.setDescribed_item(null, representation);
		}
		description_attribute.setAttribute_value(null, name);
		// System.err.println(" DESC "+description_attribute);
	}

	/**
	 * Sets derived attribute description for any entity passed as a parameter.
	 * @param context
	 * @param item
	 * @param name
	 * @throws SdaiException
	 */
	public static void setDerviedDescription(SdaiContext context, EEntity item, String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EDescription_attribute description_attribute = null;
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute.usedinDescribed_item(null, item, context.domain, aDescription_attribute);
		if (aDescription_attribute.getMemberCount() > 0) {
			description_attribute = aDescription_attribute.getByIndex(1);
		} else {
			description_attribute = (jsdai.SBasic_attribute_schema.EDescription_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EDescription_attribute.class);
			description_attribute.setDescribed_item(null, item);
		}
		description_attribute.setAttribute_value(null, name);
	}

	/**
	 * Unsets derived attribute description for any entity passed as a parameter.
	 * @param context
	 * @param item
	 * @param name
	 * @throws SdaiException
	 */
	public static void unsetDerviedDescription(SdaiContext context, EEntity item) throws SdaiException {
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute.usedinDescribed_item(null, item, context.domain, aDescription_attribute);
		for(int i=1, count=aDescription_attribute.getMemberCount();i<=count;i++) {
			aDescription_attribute.getByIndex(i).deleteApplicationInstance();
		} 
	}

	/**
	 * Unsets derived attribute description for any entity passed as a parameter.
	 * @param context
	 * @param item
	 * @param name
	 * @throws SdaiException
	 */
	public static void unsetDerivedName(SdaiContext context, EEntity item) throws SdaiException {
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null, item, context.domain, aName_attribute);
		for(int i=1, count=aName_attribute.getMemberCount();i<=count;i++) {
			aName_attribute.getByIndex(i).deleteApplicationInstance();
		} 
	}
	
	
	/**
	 * Sets representation derived attribute id.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setRepresentationId(SdaiContext context, jsdai.SRepresentation_schema.ERepresentation representation, String id) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, representation, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
		} else {
			id_attribute = (jsdai.SBasic_attribute_schema.EId_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EId_attribute.class);
			id_attribute.setIdentified_item(null, representation);
		}
		id_attribute.setAttribute_value(null, id);
	}

	/**
	 * Sets property_definition derived attribute id.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setProperty_definitionId(SdaiContext context, EProperty_definition epd, String id) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, epd, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
		} else {
			id_attribute = (jsdai.SBasic_attribute_schema.EId_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EId_attribute.class);
			id_attribute.setIdentified_item(null, epd);
		}
		id_attribute.setAttribute_value(null, id);
	}

	/**
	 * Sets group derived attribute id.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setGroupId(SdaiContext context, EGroup group, String id) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, group, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
		} else {
			id_attribute = (jsdai.SBasic_attribute_schema.EId_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EId_attribute.class);
			id_attribute.setIdentified_item(null, group);
		}
		id_attribute.setAttribute_value(null, id);
	}
	
	/**
	 * Unsets group derived attribute id.
	 * @param context
	 * @param group
	 * @param name
	 * @throws SdaiException
	 */
	public static void unsetGroupId(SdaiContext context, EGroup group) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, group, context.domain, aName_attribute);
		int count = aName_attribute.getMemberCount();
		for(int i=1;i<=count;i++){
			name_attribute = aName_attribute.getByIndex(i);
			name_attribute.deleteApplicationInstance();
		}
	}

	/**
	 * Sets any derived attribute id.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setId(SdaiContext context, EEntity instance, String id) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, instance, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
		} else {
			id_attribute = (jsdai.SBasic_attribute_schema.EId_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EId_attribute.class);
			id_attribute.setIdentified_item(null, instance);
		}
		id_attribute.setAttribute_value(null, id);
	}
	
	/**
	 * Unsets any derived attribute id.
	 * @param context
	 * @param group
	 * @param name
	 * @throws SdaiException
	 */
	public static void unsetId(SdaiContext context, EEntity instance) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null, instance, context.domain, aName_attribute);
		int count = aName_attribute.getMemberCount();
		for(int i=1;i<=count;i++){
			name_attribute = aName_attribute.getByIndex(i);
			name_attribute.deleteApplicationInstance();
		}
	}
	

	/**
	 * Gets product_definition derived attribute name.
	 * @param context
	 * @param product_definition
	 * @return
	 * @throws SdaiException
	 */
	public static String getRepresentationDescription(SdaiContext context, jsdai.SRepresentation_schema.ERepresentation representation) throws SdaiException {
		String name = null;
		jsdai.SBasic_attribute_schema.EDescription_attribute description_attribute = null;
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute.usedinDescribed_item(null, representation, context.domain, aDescription_attribute);
		if (aDescription_attribute.getMemberCount() > 0) {
			description_attribute = aDescription_attribute.getByIndex(1);
			if (description_attribute.testAttribute_value(null)) {
				name = description_attribute.getAttribute_value(null);
			}
		}
		return name;
	}


	/**
	 * Tests product_definition derived attribute name.
	 * @param context
	 * @param product_definition
	 * @return
	 * @throws SdaiException
	 */
	public static boolean testProduct_definitionName(SdaiContext context, EProduct_definition product_definition) throws SdaiException {
		if (getProduct_definitionName(context, product_definition) == null) {
			return false;
		}
		return true;
	}


	/**
	 * Removes entity instance and entity instances referenced by attributes if they are not used by other entities.
	 * @param context
	 * @param entity entity to remove.
	 * @throws SdaiException
	 */
	public static void removeInstanceIfNeeded(SdaiContext context, EEntity entity) throws SdaiException {

		if (countEntityUsers(context, entity) <= 1) {

			EDefined_type[] types = new EDefined_type[20];
			AAttribute attrs = CMappedARMEntity.getAllAttributes(entity.getInstanceType());
			SdaiIterator attrIt = attrs.createIterator();
			while (attrIt.next()) {
				EAttribute attrObj = attrs.getCurrentMember(attrIt);
				if (attrObj instanceof EExplicit_attribute) {
					EExplicit_attribute attr = (EExplicit_attribute) attrObj;

					if (!attr.testRedeclaring(null)) {

						if (entity.testAttribute(attr, types) == 1) {
							Object value = entity.get_object(attr);

							//value entity
							if (value instanceof EEntity) {
							   removeInstanceIfNeeded(context, (EEntity) value);
							}

							//value aggregate
							if (value instanceof Aggregate) {
								Aggregate valueAggregate = (Aggregate) value;
								EEntity domain = valueAggregate.getAggregationType().getElement_type(null);
								if (domain instanceof EEntity_definition) {
									for (int i = 1; i <= valueAggregate.getMemberCount(); i++) {
										removeInstanceIfNeeded(context, (EEntity) valueAggregate.getByIndexObject(i));
									}
								}
							}
						}
					}
				}
			}
			entity.deleteApplicationInstance();
		}
	}

	public static EShape_aspect createPDS_SA_structureIfNeeded(SdaiContext context, EEntity target)throws SdaiException{
		// Target <- PDS
      LangUtils.Attribute_and_value_structure[] pdsS = {new LangUtils.Attribute_and_value_structure(
            jsdai.SProduct_property_definition_schema.CProduct_definition_shape.attributeDefinition(null),target)
      };
      jsdai.SProduct_property_definition_schema.EProduct_definition_shape pds =
        (jsdai.SProduct_property_definition_schema.EProduct_definition_shape)
        LangUtils.createInstanceIfNeeded(context,jsdai.SProduct_property_definition_schema.CProduct_definition_shape.definition,pdsS);
      if(!pds.testName(null))
        pds.setName(null, "");
    //SA->PDS
    LangUtils.Attribute_and_value_structure[] saS = {new LangUtils.Attribute_and_value_structure(
            jsdai.SProduct_property_definition_schema.CShape_aspect.attributeOf_shape(null),pds)
    };
    jsdai.SProduct_property_definition_schema.EShape_aspect sa =
                    (jsdai.SProduct_property_definition_schema.EShape_aspect)
                    LangUtils.createInstanceIfNeeded(context,jsdai.SProduct_property_definition_schema.CShape_aspect.definition,saS);
    if (!sa.testName(null))
       sa.setName(null, "");
    if (!sa.testProduct_definitional(null))
       sa.setProduct_definitional(null, ELogical.UNKNOWN);
    return sa;
	}

	/**
	 * Sets Property_definition_representation derived attribute description.
	 * @param context
	 * @param product_definition
	 * @param name
	 * @throws SdaiException
	 */
	public static void setProperty_definition_representationDescription(SdaiContext context, EProperty_definition_representation pdr, String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EDescription_attribute description_attribute = null;
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute.usedinDescribed_item(null, pdr, context.domain, aDescription_attribute);
		if (aDescription_attribute.getMemberCount() > 0) {
			description_attribute = aDescription_attribute.getByIndex(1);
		} else {
			description_attribute = (jsdai.SBasic_attribute_schema.EDescription_attribute) context.working_model.createEntityInstance(jsdai.SBasic_attribute_schema.EDescription_attribute.class);
			description_attribute.setDescribed_item(null, pdr);
		}
		description_attribute.setAttribute_value(null, name);
	}

	public static EAxis2_placement_2d getOriginFromShape(EShape_representation shape)throws SdaiException{
		if(shape.testItems(null)){
			ARepresentation_item items = shape.getItems(null);
			SdaiIterator iter = items.createIterator();
			while(iter.next()){
				ERepresentation_item item = items.getCurrentMember(iter);
				if(item instanceof EAxis2_placement_2d){
					if((item.testName(null))&&(item.getName(null).equals("origin"))){
						return (EAxis2_placement_2d)item;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Populates AIM data needed by mappings of this pattern:
	 * xxx
		document_reference_item = xxx
		document_reference_item &lt;-
		applied_document_reference.items[i]
		applied_document_reference
		applied_document_reference &lt;=
		document_reference
		document_reference.assigned_document -&gt;
		document &lt;-
		document_product_association.relating_document
		{document_product_association.name = 'equivalence'}
		document_product_association.related_product -&gt;
		product_or_formation_or_definition = product_definition
		product_definition
		{product_definition.name = 'xxx'}	.
	 * @param context
	 * @param edd - document_definition to be assigned to particular user
	 * @param armEntity - user to which document_definition will be assigned
	 * @throws SdaiException
	 */
	
	public static void assignDocument_definition(SdaiContext context, 
			EDocument_definition edd, EEntity armEntity)throws SdaiException{
		EEntity_definition type = edd.getInstanceType();
		String name = type.getName(null).toLowerCase().replace('_',' ');
      //ADR->ST
      LangUtils.Attribute_and_value_structure[] dpaS = {
      		new LangUtils.Attribute_and_value_structure(
              CDocument_product_association.attributeRelated_product(null),
              edd),
      		new LangUtils.Attribute_and_value_structure(
              CDocument_product_association.attributeName(null),
              "equivalence")
      };
      EDocument_product_association edpa = (EDocument_product_association)
          LangUtils.createInstanceIfNeeded(context, CDocument_product_association.definition, dpaS);
      EDocument document;
      if(edpa.testRelating_document(null)){
      	document = edpa.getRelating_document(null);
      }
      // create new Document
      else{
      	document = (EDocument)context.working_model.createEntityInstance(CDocument.definition);
      	// AIM gaps
      	document.setId(null, edd.getId(null));
      	if(edd.testName_x(null))
      		document.setName(null, edd.getName_x(null));
      	else
      		document.setName(null, edd.getId(null));
      	// Kind
         LangUtils.Attribute_and_value_structure[] dtS = {
         		new LangUtils.Attribute_and_value_structure(
                 CDocument_type.attributeProduct_data_type(null),
                 name)
         };
         EDocument_type edt = (EDocument_type)
             LangUtils.createInstanceIfNeeded(context, CDocument_type.definition, dtS);
         document.setKind(null, edt);
         // DPA -> Document
         edpa.setRelating_document(null, document);
      }
   	// ADR
      LangUtils.Attribute_and_value_structure[] adrS = {
      		new LangUtils.Attribute_and_value_structure(
              CApplied_document_reference.attributeAssigned_document(null),
              document)
      };
      EApplied_document_reference eadr = (EApplied_document_reference)
          LangUtils.createInstanceIfNeeded(context, CApplied_document_reference.definition, adrS);
      ADocument_reference_item adri;
      if(eadr.testItems(null))
      	adri = eadr.getItems(null);
      else
      	adri = eadr.createItems(null);
      if(!eadr.testSource(null))
        eadr.setSource(null, "");
      
      adri.addUnordered(armEntity);
	}

	/**
	 * Removed AIM data needed by mappings of this pattern:
	 * xxx
		document_reference_item = xxx
		document_reference_item &lt;-
		applied_document_reference.items[i]
		applied_document_reference
		applied_document_reference &lt;=
		document_reference
		document_reference.assigned_document -&gt;
		document &lt;-
		document_product_association.relating_document
		{document_product_association.name = 'equivalence'}
		document_product_association.related_product -&gt;
		product_or_formation_or_definition = product_definition
		product_definition
		{product_definition.name = 'xxx'}	.
	 * @param context
	 * @param armEntity - user to which document_definition will be assigned
	 * @param name - magic string used to constrain document_definition
	 * @throws SdaiException
	 */
	
	public static void unAssignDocument_definition(SdaiContext context, 
			EEntity armEntity, String name)throws SdaiException{
	
	   AApplied_document_reference aAdr = new AApplied_document_reference();
	   EApplied_document_reference adr = null;
	
	   CApplied_document_reference.usedinItems(null,armEntity,context.domain,aAdr);
	   if (aAdr.getMemberCount()==0)
	      return;
	
	   SdaiIterator iter = aAdr.createIterator();
	   while(iter.next()){
	   	adr = aAdr.getCurrentMember(iter);
	   	EDocument document = adr.getAssigned_document(null);
	   	ADocument_product_association adpa = new ADocument_product_association(); 
	   	CDocument_product_association.usedinRelating_document(null, document, context.domain, adpa);
	
	   	SdaiIterator iterDPA = adpa.createIterator();
	   	EDocument_product_association edpa; 
	      while(iterDPA.next()){
	      	edpa = adpa.getCurrentMember(iterDPA);
	      	EEntity ee = edpa.getRelated_product(null);
	      	if(ee instanceof EProduct_definition){
	      		EProduct_definition epd = (EProduct_definition)ee;
	         	if(edpa.getName(null).equals("equivalence"))
	         		if((name == null)||
	         		(epd.getName(null).equals(name))){
		         		ADocument_reference_item items = adr.getItems(null);
		         		items.removeUnordered(armEntity);
		         		if(items.getMemberCount() == 0)
		         			adr.deleteApplicationInstance();
	         	}
	      	}
	      }
	   	
	   }
	}

	public static EMeasure_representation_item upgradeToMRI(SdaiModel working_model, EMeasure_with_unit emwu)throws SdaiException{
		EMeasure_representation_item item = null;
		if(emwu instanceof EMeasure_representation_item){
			return (EMeasure_representation_item)emwu;
		}
		if(emwu.getInstanceType() == CMeasure_with_unit.definition){
			// If it is more specific subtype than measure_with_unit - substitute most like
			item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CMeasure_representation_item.definition);
			item.setName(null, "");
			return item;
		}
		if(emwu instanceof EMeasure_with_unit){
			 // TODO - add more on demand
			if(emwu instanceof EVolume_measure_with_unit){
				item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CMeasure_representation_item$volume_measure_with_unit.definition);
			}
			if(emwu instanceof EFrequency_measure_with_unit){
				item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CFrequency_measure_with_unit$measure_representation_item.definition);
			}
			if(emwu instanceof EPower_measure_with_unit){
				item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CMeasure_representation_item$power_measure_with_unit.definition);
			}
			if(emwu instanceof EMass_measure_with_unit){
				item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CMass_measure_with_unit$measure_representation_item.definition);
			}
			if(emwu instanceof ERatio_measure_with_unit){
				item = (EMeasure_representation_item)working_model.substituteInstance(emwu, CMeasure_representation_item$ratio_measure_with_unit.definition);
			}
			if(item != null){
				item.setName(null, "");
				return item;
			}
			
		}
		throw new SdaiException(SdaiException.FN_NAVL, "Case when quantity is of this type is not supported "+emwu);			
	}
	
}