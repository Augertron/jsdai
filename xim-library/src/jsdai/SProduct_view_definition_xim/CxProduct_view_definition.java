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

package jsdai.SProduct_view_definition_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.*;
import jsdai.SApplication_context_schema.*;
import jsdai.SProduct_definition_schema.*;

public class CxProduct_view_definition
		extends
			CProduct_view_definition implements EMappedXIMEntity{

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}

	// Product_view_definition
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", jsdai.SProduct_definition_schema.CProduct_definition.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes

			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// Clean ARM specific attributes
			unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes

			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			unsetAdditional_characterization(context, this);
			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
	}

	//********** "design_discipline_item_definition" attributes

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * <p>
	 * attribute_mapping name (name , product_definition.description);
	 * end_attribute_mapping;
	 * </p>
	 * <p>
	 * attribute_mapping name_multi_language_string (name ,
	 * ([product_definition.description] [$PATH]) ($PATH),
	 * multi_language_string); product_definition (attribute_language_item =
	 * product_definition attribute_language_item <-
	 * attribute_language_assignment.items[i] attribute_language_assignment
	 * {attribute_language_assignment <= attribute_classification_assignment
	 * attribute_classification_assignment.attribute_name = 'description'})
	 * (multi_language_attribute_item = product_definition
	 * multi_language_attribute_item <-
	 * multi_language_attribute_assignment.items[i]
	 * multi_language_attribute_assignment {multi_language_attribute_assignment <=
	 * attribute_value_assignment attribute_value_assignment.attribute_name =
	 * 'description'}) end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	/* It is coming from AIM now
	public static void setName_x(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		if(armEntity.testName_x(null)){
			//unset old values
			unsetName_x(context, armEntity);
	
			armEntity.setDescription(null, armEntity.getName_x(null));
		}
	}
*/
	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void unsetName_x(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		// System.err.println(" unset NAME type "+armEntity.getClass());
		armEntity.unsetDescription(null);
	}
*/

	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * <p>
	 * attribute_mapping additional_context_application_context
	 * (additional_context , $PATH, application_context); product_definition <-
	 * product_definition_context_association.definition
	 * product_definition_context_association
	 * {product_definition_context_association.role -> (* Modified by Audronis
	 * Gustas *) product_definition_context_role
	 * product_definition_context_role.name = 'additional context'}
	 * product_definition_context_association.frame_of_reference ->
	 * product_definition_context end_attribute_mapping;
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		//unset old values
		unsetAdditional_contexts(context, armEntity);

		if (armEntity.testAdditional_contexts(null)) {
			jsdai.SProduct_definition_schema.EProduct_definition aimEntity = armEntity;
			AProduct_definition_context aArmAdditional_context = armEntity.getAdditional_contexts(null);
			EProduct_definition_context armAdditional_context = null;

			EProduct_definition_context_association product_definition_context_association = null;
			for (int i = 1; i <= aArmAdditional_context.getMemberCount(); i++) {
				armAdditional_context = aArmAdditional_context.getByIndex(i);

				//product_definition_context_association
				product_definition_context_association = (EProduct_definition_context_association) context.working_model
						.createEntityInstance(EProduct_definition_context_association.class);
				product_definition_context_association.setDefinition(null,
						aimEntity);
				product_definition_context_association.setFrame_of_reference(
						null, armAdditional_context);

				//product_definition_context_role
				LangUtils.Attribute_and_value_structure[] pdcrStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
						CProduct_definition_context_role.attributeName(null),
						"additional context"),};

				EProduct_definition_context_role product_definition_context_role = (EProduct_definition_context_role) LangUtils
						.createInstanceIfNeeded(context,
								CProduct_definition_context_role.definition,
								pdcrStructure);
				product_definition_context_association.setRole(null,
						product_definition_context_role);
			}
		}
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		jsdai.SProduct_definition_schema.EProduct_definition aimEntity = armEntity;
		EProduct_definition_context_association product_definition_context_association = null;
		AProduct_definition_context_association aProduct_definition_context_association = new AProduct_definition_context_association();
		CProduct_definition_context_association.usedinDefinition(null,
				aimEntity, context.domain,
				aProduct_definition_context_association);
		//product_definition_context_association
		for (int i = 1; i <= aProduct_definition_context_association
				.getMemberCount(); i++) {
			product_definition_context_association = aProduct_definition_context_association
					.getByIndex(i);

			//product_definition_context_role
			if (product_definition_context_association.testRole(null)) {
				EProduct_definition_context_role product_definition_context_role = product_definition_context_association
						.getRole(null);
				if (product_definition_context_role.testName(null)
						&& product_definition_context_role.getName(null)
								.equals("additional context")) {
					if (CxAp214ArmUtilities.countEntityUsers(context,
							product_definition_context_role) == 1) {
						product_definition_context_role
								.deleteApplicationInstance();
					}
					product_definition_context_association
							.deleteApplicationInstance();
				}
			}
		}
	}

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * <p>
	 * attribute_mapping name (name , product_definition.description);
	 * end_attribute_mapping;
	 * </p>
	 * <p>
	 * attribute_mapping name_multi_language_string (name ,
	 * ([product_definition.description] [$PATH]) ($PATH),
	 * multi_language_string); product_definition (attribute_language_item =
	 * product_definition attribute_language_item <-
	 * attribute_language_assignment.items[i] attribute_language_assignment
	 * {attribute_language_assignment <= attribute_classification_assignment
	 * attribute_classification_assignment.attribute_name = 'description'})
	 * (multi_language_attribute_item = product_definition
	 * multi_language_attribute_item <-
	 * multi_language_attribute_assignment.items[i]
	 * multi_language_attribute_assignment {multi_language_attribute_assignment <=
	 * attribute_value_assignment attribute_value_assignment.attribute_name =
	 * 'description'}) end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		//unset old values
		unsetAdditional_characterization(context, armEntity);
		if(armEntity.testAdditional_characterization(null)){
			jsdai.SProduct_definition_schema.EProduct_definition aimEntity = armEntity;
			CxAP210ARMUtilities.setProduct_definitionName(context, aimEntity, armEntity.getAdditional_characterization(null));
		}
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxAP210ARMUtilities.unsetDerivedName(context, armEntity);
	}

}