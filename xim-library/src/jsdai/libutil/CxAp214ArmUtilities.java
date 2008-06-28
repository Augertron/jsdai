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
import jsdai.SAction_schema.EAction_request_solution;
import jsdai.SActivity_xim.EActivity;
import jsdai.SApplication_context_schema.*;
import jsdai.SApplication_context_schema.EApplication_context;
import jsdai.SApplication_context_schema.CApplication_context;
import jsdai.SGeneric_product_occurrence_xim.EProduct_occurrence;
import jsdai.SPart_view_definition_xim.EPart_view_definition;
import jsdai.SPerson_organization_assignment_mim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SMeasure_schema.*;
import jsdai.SMulti_linguism_mim.*;
import jsdai.util.*;
import jsdai.SShape_feature_xim.EShape_feature_definition_armx;
import jsdai.SFile_identification_mim.EDocument_file;
import jsdai.SGeometry_schema.*;
import jsdai.SIda_step_schema_xim.*;
import jsdai.SIdentification_assignment_mim.*;
import jsdai.SItem_definition_structure_xim.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;
import jsdai.SSpecification_control_xim.*;
import jsdai.SSpecified_product_mim.*;
import jsdai.SProduct_class_xim.*;
import jsdai.SProduct_breakdown_xim.*;

import java.lang.reflect.*;
import jsdai.SManagement_resources_schema.*;
import jsdai.dictionary.*;
import jsdai.SDate_time_assignment_mim.*;
import jsdai.SDate_time_schema.*;
import jsdai.SDocument_schema.EDocument_representation_type;
import jsdai.SEvent_assignment_mim.*;
import jsdai.SExternal_item_identification_assignment_mim.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;

/**
 * <p>
 * Contains helpful operations fo ARM to AIM implementation.
 * </p>
 * 
 * @author Valdas Zigas
 * @version $Revision$
 */
public class CxAp214ArmUtilities {

	/**
	 * Counts how many users has entity instance.
	 * 
	 * @param context
	 * @param entity
	 *            entity to count users.
	 * @return number of users.
	 * @throws SdaiException
	 */
	public static int countEntityUsers(SdaiContext context, EEntity entity)
			throws SdaiException {
		AEntity aEntity = new AEntity();
		entity.findEntityInstanceUsers(context.domain, aEntity);
		return aEntity.getMemberCount();
	}

	/**
	 * Creates AIM application_context entity instance.
	 * 
	 * @param context
	 * @param apcApplication
	 *            application_context.Application attribute value.
	 * @param apcReuse
	 *            flag: true reuse existing entity, false create new.
	 * @return application_context entity
	 * @throws SdaiException
	 */
	public static EApplication_context createApplication_context(
			SdaiContext context, String apcApplication, boolean apcReuse)
			throws SdaiException {
		EApplication_context application_context = null;

		//reuse
		if (apcReuse) {
			LangUtils.Attribute_and_value_structure[] acsStructure = null;

			if (apcApplication != null) {
				acsStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
						CApplication_context.attributeApplication(null),
						apcApplication),};
			} else {
				acsStructure = new LangUtils.Attribute_and_value_structure[]{};
			}

			application_context = (EApplication_context) LangUtils
					.createInstanceIfNeeded(context,
							CApplication_context.definition, acsStructure);
			if (!application_context.testApplication(null)) {
				application_context.setApplication(null, "");
			}

			//create new
		} else {
			application_context = (EApplication_context) context.working_model
					.createEntityInstance(EApplication_context.class);
			if (apcApplication != null) {
				application_context.setApplication(null, apcApplication);
			} else {
				application_context.setApplication(null, "");
			}
		}
		return application_context;
	}

	/**
	 * Creates product_definition_context entity instance with reused
	 * application_context.
	 * 
	 * @param context
	 * @param pdcName
	 *            product_definition_context.name attribute value.
	 * @param pdcLifeCycleStage
	 *            product_definition_context.life_cycle_stage attribute value.
	 * @param pdcReuse
	 *            flag: true reuse existing product_definition_context entity,
	 *            false create new.
	 * @return product_definition_context entity instance.
	 * @throws SdaiException
	 */
	public static EProduct_definition_context createProduct_definition_context(
			SdaiContext context, String pdcName, String pdcLifeCycleStage,
			boolean pdcReuse) throws SdaiException {
		return createProduct_definition_context(context, pdcName,
				pdcLifeCycleStage, pdcReuse, "", true);
	}

	/**
	 * Creates product_definition_context entity instance.
	 * 
	 * @param context
	 * @param pdcName
	 *            product_definition_context.name attribute value.
	 * @param pdcLifeCycleStage
	 *            product_definition_context.life_cycle_stage attribute value.
	 * @param pdcReuse
	 *            flag: true reuse existing product_definition_context entity,
	 *            false create new.
	 * @param apcApplication
	 *            application_context.Application attribute value.
	 * @param apcReuse
	 *            flag: true reuse existing application_context entity, false
	 *            create new.
	 * @return product_definition_context entity instance.
	 * @throws SdaiException
	 */
	public static EProduct_definition_context createProduct_definition_context(
			SdaiContext context, String pdcName, String pdcLifeCycleStage,
			boolean pdcReuse, String apcApplication, boolean apcReuse)
			throws SdaiException {
		EProduct_definition_context product_definition_context = null;

		//reuse
		if (pdcReuse) {
			//product_definition_context
			LangUtils.Attribute_and_value_structure[] pdcStructure = null;

			if (pdcName != null) {
				if (pdcLifeCycleStage != null) {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{
							new LangUtils.Attribute_and_value_structure(
									CProduct_definition_context
											.attributeName(null), pdcName),
							new LangUtils.Attribute_and_value_structure(
									CProduct_definition_context
											.attributeLife_cycle_stage(null),
									pdcLifeCycleStage)};

				} else {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
							CProduct_definition_context.attributeName(null),
							pdcName),};

				}
			} else {
				if (pdcLifeCycleStage != null) {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
							CProduct_definition_context
									.attributeLife_cycle_stage(null),
							pdcLifeCycleStage)};
				} else {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{};
				}
			}
			product_definition_context = (EProduct_definition_context) LangUtils
					.createInstanceIfNeeded(context,
							CProduct_definition_context.definition,
							pdcStructure);
			//create new
		} else {
			product_definition_context = (EProduct_definition_context) context.working_model
					.createEntityInstance(EProduct_definition_context.class);
			if (pdcName != null) {
				product_definition_context.setName(null, pdcName);
			}

			if (pdcLifeCycleStage != null) {
				product_definition_context.setLife_cycle_stage(null,
						pdcLifeCycleStage);
			}
		}

		//name
		if (!product_definition_context.testName(null)) {
			product_definition_context.setName(null, "");
		}

		//life_cycle_stage
		if (!product_definition_context.testLife_cycle_stage(null)) {
			product_definition_context.setLife_cycle_stage(null, "");
		}

		//application_context
		if (!product_definition_context.testFrame_of_reference(null)) {
			EApplication_context application_context = createApplication_context(
					context, apcApplication, apcReuse);
			product_definition_context.setFrame_of_reference(null,
					application_context);
		}
		return product_definition_context;
	}

	/**
	 * Removes product_definition_context entity instance and referenced
	 * application_context if not used.
	 * 
	 * @param context
	 * @param product_definition_context
	 *            product_definition_context entity instance
	 * @throws SdaiException
	 */
	public static void removeProduct_definition_context(SdaiContext context,
			EProduct_definition_context product_definition_context)
			throws SdaiException {
		if (product_definition_context == null)
			return;

		if (CxAp214ArmUtilities.countEntityUsers(context,
				product_definition_context) == 0) {
			if (product_definition_context.testFrame_of_reference(null)) {
				EApplication_context application_context = product_definition_context
						.getFrame_of_reference(null);
				product_definition_context.unsetFrame_of_reference(null);

				if (CxAp214ArmUtilities.countEntityUsers(context,
						application_context) == 0) {
					application_context.deleteApplicationInstance();
				}
			}
		}
	}

	/**
	 * Creates product_context entity instance.
	 * 
	 * @param context
	 * @param pcName
	 * @param pcDiscipline_type
	 * @param pcReuse
	 * @param apcApplication
	 * @param apcReuse
	 * @return product_context entity instance.
	 * @throws SdaiException
	 */
	public static EProduct_context createProduct_context(SdaiContext context,
			String pcName, String pcDiscipline_type, boolean pcReuse,
			String apcApplication, boolean apcReuse) throws SdaiException {
		EProduct_context product_context = null;

		//reuse
		if (pcReuse) {
			//product_context
			LangUtils.Attribute_and_value_structure[] pdcStructure = null;
			if (pcName != null) {
				if (pcDiscipline_type != null) {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{
							new LangUtils.Attribute_and_value_structure(
									CProduct_context.attributeName(null),
									pcName),
							new LangUtils.Attribute_and_value_structure(
									CProduct_context
											.attributeDiscipline_type(null),
									pcDiscipline_type)};
				} else {
					pdcStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
							CProduct_context.attributeName(null), pcName),};
				}
			} else if (pcDiscipline_type != null) {
				pdcStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
						CProduct_context.attributeDiscipline_type(null),
						pcDiscipline_type)};
			} else {
				pdcStructure = new LangUtils.Attribute_and_value_structure[]{};
			}

			product_context = (EProduct_context) LangUtils
					.createInstanceIfNeeded(context,
							CProduct_context.definition, pdcStructure);

			//name
			if (!product_context.testName(null)) {
				product_context.setName(null, "");
			}

			//discipline_type
			if (!product_context.testDiscipline_type(null)) {
				product_context.setDiscipline_type(null, "");
			}

			//create new
		} else {
			product_context = (EProduct_context) context.working_model
					.createEntityInstance(EProduct_context.class);

			//name
			if (pcName != null) {
				product_context.setName(null, pcName);
			} else {
				product_context.setName(null, "");
			}

			//discipline_type
			if (pcDiscipline_type != null) {
				product_context.setDiscipline_type(null, pcDiscipline_type);
			} else {
				product_context.setDiscipline_type(null, "");
			}
		}

		//application_context
		if (!product_context.testFrame_of_reference(null)) {
			EApplication_context application_context = createApplication_context(
					context, apcApplication, apcReuse);
			product_context.setFrame_of_reference(null, application_context);
		}
		return product_context;
	}

	/**
	 * Sets derived attribute description.
	 * 
	 * @param context
	 * @param instance
	 * @param description
	 * @throws SdaiException
	 */
	public static void setDerivedDescription(SdaiContext context,
			EEntity instance, String description) throws SdaiException {
		jsdai.SBasic_attribute_schema.EDescription_attribute description_attribute = null;
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute
				.usedinDescribed_item(null, instance, context.domain,
						aDescription_attribute);
		if (aDescription_attribute.getMemberCount() > 0) {
			description_attribute = aDescription_attribute.getByIndex(1);
		} else {
			description_attribute = (jsdai.SBasic_attribute_schema.EDescription_attribute) context.working_model
					.createEntityInstance(jsdai.SBasic_attribute_schema.EDescription_attribute.class);
			description_attribute.setDescribed_item(null, instance);
		}
		description_attribute.setAttribute_value(null, description);
	}

	/**
	 * Unsets derived attribute description.
	 * 
	 * @param context
	 * @param instance
	 * @throws SdaiException
	 */
	public static void unsetDerivedDescription(SdaiContext context,
			EEntity instance) throws SdaiException {
		jsdai.SBasic_attribute_schema.ADescription_attribute aDescription_attribute = new jsdai.SBasic_attribute_schema.ADescription_attribute();
		jsdai.SBasic_attribute_schema.CDescription_attribute
				.usedinDescribed_item(null, instance, context.domain,
						aDescription_attribute);
		for (int i = 1; i <= aDescription_attribute.getMemberCount(); i++) {
			aDescription_attribute.getByIndex(i).deleteApplicationInstance();
		}
	}

	/**
	 * Sets derived name attribute.
	 * 
	 * @param context
	 * @param instance
	 * @param name
	 * @throws SdaiException
	 */
	public static void setDerivedName(SdaiContext context, EEntity instance,
			String name) throws SdaiException {
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null,
				instance, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
		} else {
			name_attribute = (jsdai.SBasic_attribute_schema.EName_attribute) context.working_model
					.createEntityInstance(jsdai.SBasic_attribute_schema.EName_attribute.class);
			name_attribute.setNamed_item(null, instance);
		}
		name_attribute.setAttribute_value(null, name);
	}

	/**
	 * Unsets derived name attribute.
	 * 
	 * @param context
	 * @param instance
	 * @throws SdaiException
	 */
	public static void unsetDerivedName(SdaiContext context, EEntity instance)
			throws SdaiException {
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null,
				instance, context.domain, aName_attribute);
		for (int i = 1; i <= aName_attribute.getMemberCount(); i++) {
			name_attribute = aName_attribute.getByIndex(i);
			name_attribute.deleteApplicationInstance();
		}
	}

	/**
	 * Gets derived attribute name.
	 * 
	 * @param context
	 * @param instance
	 * @return derived name
	 * @throws SdaiException
	 */
	public static String getDerivedName(SdaiContext context, EEntity instance)
			throws SdaiException {
		String name = null;
		jsdai.SBasic_attribute_schema.EName_attribute name_attribute = null;
		jsdai.SBasic_attribute_schema.AName_attribute aName_attribute = new jsdai.SBasic_attribute_schema.AName_attribute();
		jsdai.SBasic_attribute_schema.CName_attribute.usedinNamed_item(null,
				instance, context.domain, aName_attribute);
		if (aName_attribute.getMemberCount() > 0) {
			name_attribute = aName_attribute.getByIndex(1);
			if (name_attribute.testAttribute_value(null)) {
				name = name_attribute.getAttribute_value(null);
			}
		}
		return name;
	}

	/**
	 * Tests derived attribute name.
	 * 
	 * @param context
	 * @param instance
	 * @return true if derived attribute "name" is set, else - otherwise.
	 * @throws SdaiException
	 */
	public static boolean testDerivedName(SdaiContext context, EEntity instance)
			throws SdaiException {
		if (getDerivedName(context, instance) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Sets derived attribute id.
	 * 
	 * @param context
	 * @param instance
	 * @param id
	 * @throws SdaiException
	 */
	public static void setDerivedId(SdaiContext context, EEntity instance,
			String id) throws SdaiException {
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null,
				instance, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
		} else {
			id_attribute = (jsdai.SBasic_attribute_schema.EId_attribute) context.working_model
					.createEntityInstance(jsdai.SBasic_attribute_schema.EId_attribute.class);
			id_attribute.setIdentified_item(null, instance);
		}
		id_attribute.setAttribute_value(null, id);
	}

	/**
	 * Gets derived attribute id.
	 * 
	 * @param context
	 * @param instance
	 * @throws SdaiException
	 */
	public static String getDerivedId(SdaiContext context, EEntity instance)
			throws SdaiException {
		String derivedId = null;
		jsdai.SBasic_attribute_schema.EId_attribute id_attribute = null;
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null,
				instance, context.domain, aId_attribute);
		if (aId_attribute.getMemberCount() > 0) {
			id_attribute = aId_attribute.getByIndex(1);
			if (id_attribute.testAttribute_value(null)) {
				derivedId = id_attribute.getAttribute_value(null);
			}
		}
		return derivedId;
	}

	/**
	 * Unsets derived attribute id.
	 * 
	 * @param context
	 * @param instance
	 * @throws SdaiException
	 */
	public static void unsetDerivedId(SdaiContext context, EEntity instance)
			throws SdaiException {
		jsdai.SBasic_attribute_schema.AId_attribute aId_attribute = new jsdai.SBasic_attribute_schema.AId_attribute();
		jsdai.SBasic_attribute_schema.CId_attribute.usedinIdentified_item(null,
				instance, context.domain, aId_attribute);
		for (int i = 1; i <= aId_attribute.getMemberCount(); i++) {
			aId_attribute.getByIndex(i).deleteApplicationInstance();
		}
	}

	/**
	 * Sets measure value to EValue_representation_item.
	 * 
	 * @param target
	 *            EValue_representation_item instance to set value.
	 * @param value
	 *            value to set.
	 * @param type
	 *            type of set.
	 * @throws SdaiException
	 */
	public static void setMeasureValue(EValue_representation_item target,
			String value, int type) throws SdaiException {
		if (type == EMeasure_with_unit.sValue_componentLength_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ELength_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentMass_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EMass_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentTime_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ETime_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentElectric_current_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EElectric_current_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentThermodynamic_temperature_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EThermodynamic_temperature_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentCelsius_temperature_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ECelsius_temperature_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentAmount_of_substance_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EAmount_of_substance_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentLuminous_intensity_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ELuminous_intensity_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPlane_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPlane_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentSolid_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ESolid_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentArea_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EArea_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentVolume_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EVolume_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentRatio_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ERatio_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentParameter_value)
			target.setValue_component(null, Double.parseDouble(value),
					(EParameter_value) null);

		else if (type == EMeasure_with_unit.sValue_componentNumeric_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ENumeric_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentContext_dependent_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EContext_dependent_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentNumeric_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ENumeric_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentDescriptive_measure)
			target.setValue_component(null, value, (EDescriptive_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_length_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_length_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_plane_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_plane_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_ratio_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_ratio_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentCount_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ECount_measure) null);
		//default
		else {
			target.setValue_component(null, Double.parseDouble(value),
					(ELength_measure) null);
		}
	}

	/**
	 * Sets measure value to EMeasure_with_unit.
	 * 
	 * @param emwu
	 *            EMeasure_with_unit instance to set value.
	 * @param value
	 *            value to set.
	 * @param type
	 *            type of set.
	 * @throws SdaiException
	 */
	public static void setMeasureValue(EMeasure_with_unit target, String value,
			int type) throws SdaiException {
		if (value.trim().length() == 0) {
			value = "0";
		}

		if (type == EMeasure_with_unit.sValue_componentLength_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ELength_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentMass_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EMass_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentTime_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ETime_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentElectric_current_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EElectric_current_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentThermodynamic_temperature_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EThermodynamic_temperature_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentCelsius_temperature_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ECelsius_temperature_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentAmount_of_substance_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EAmount_of_substance_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentLuminous_intensity_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ELuminous_intensity_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPlane_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPlane_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentSolid_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ESolid_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentArea_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EArea_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentVolume_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EVolume_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentRatio_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ERatio_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentParameter_value)
			target.setValue_component(null, Double.parseDouble(value),
					(EParameter_value) null);

		else if (type == EMeasure_with_unit.sValue_componentNumeric_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ENumeric_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentContext_dependent_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EContext_dependent_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentNumeric_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ENumeric_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentDescriptive_measure)
			target.setValue_component(null, value, (EDescriptive_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_length_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_length_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_plane_angle_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_plane_angle_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentPositive_ratio_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(EPositive_ratio_measure) null);

		else if (type == EMeasure_with_unit.sValue_componentCount_measure)
			target.setValue_component(null, Double.parseDouble(value),
					(ECount_measure) null);
		//default
		else {
			target.setValue_component(null, Double.parseDouble(value),
					(ELength_measure) null);
		}
	}

	/**
	 * Sets Arm version_id attribute (common case)
	 * 
	 * @param context
	 * @param aimEntity
	 *            mapping target.
	 * @param version_id
	 *            version id value.
	 * @throws SdaiException
	 */
	public static void setArmVersion_idA(SdaiContext context,
			EEntity aimEntity, String version_id) throws SdaiException {
		//applied_identification_assignment
		EApplied_identification_assignment applied_identification_assignment = (EApplied_identification_assignment) context.working_model
				.createEntityInstance(EApplied_identification_assignment.class);
		applied_identification_assignment.createItems(null).addUnordered(
				aimEntity);
		applied_identification_assignment.setAssigned_id(null, version_id);

		//identification_role
		LangUtils.Attribute_and_value_structure[] irStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
				jsdai.SManagement_resources_schema.CIdentification_role
						.attributeName(null), "version"),};

		jsdai.SManagement_resources_schema.EIdentification_role identification_role = (jsdai.SManagement_resources_schema.EIdentification_role) LangUtils
				.createInstanceIfNeeded(
						context,
						jsdai.SManagement_resources_schema.CIdentification_role.definition,
						irStructure);
		applied_identification_assignment.setRole(null, identification_role);
	}

	/**
	 * Unsets Arm version_id attribute (common case)
	 * 
	 * @param context
	 * @param aimEntity
	 * @throws SdaiException
	 */
	public static void unsetArmVersion_idA(SdaiContext context,
			EEntity aimEntity) throws SdaiException {
		//applied_identification_assignment
		EApplied_identification_assignment applied_identification_assignment = null;
		AApplied_identification_assignment aApplied_identification_assignment = new AApplied_identification_assignment();
		CApplied_identification_assignment
				.usedinItems(null, aimEntity, context.domain,
						aApplied_identification_assignment);
		for (int i = 1; i <= aApplied_identification_assignment
				.getMemberCount(); i++) {
			applied_identification_assignment = aApplied_identification_assignment
					.getByIndex(i);

			//role
			if (applied_identification_assignment.testRole(null)) {
				jsdai.SManagement_resources_schema.EIdentification_role identification_role = applied_identification_assignment
						.getRole(null);
				if (identification_role.testName(null)
						&& identification_role.getName(null).equals("version")) {
					AIdentification_item aItems = applied_identification_assignment
							.getItems(null);
					while (aItems.isMember(aimEntity)) {
						aItems.removeUnordered(aimEntity);
					}

					if (aItems.getMemberCount() == 0
							&& CxAp214ArmUtilities.countEntityUsers(context,
									applied_identification_assignment) == 0) {
						applied_identification_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								identification_role) == 0) {
							identification_role.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Sets Arm version_id attribute (common case)
	 * 
	 * @param context
	 * @param aimEntity
	 *            mapping target.
	 * @param version_id
	 *            version id value.
	 * @throws SdaiException
	 */
	public static void setArmVersion_idE(SdaiContext context,
			EEntity aimEntity, String version_id) throws SdaiException {
		//applied_identification_assignment
		EApplied_identification_assignment applied_identification_assignment = (EApplied_identification_assignment) context.working_model
				.createEntityInstance(EApplied_identification_assignment.class);
		applied_identification_assignment.createItems(null).addUnordered(
				aimEntity);
		applied_identification_assignment.setAssigned_id(null, version_id);

		//identification_role
		LangUtils.Attribute_and_value_structure[] irStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
				jsdai.SManagement_resources_schema.CIdentification_role
						.attributeName(null), "version"),};

		jsdai.SManagement_resources_schema.EIdentification_role identification_role = (jsdai.SManagement_resources_schema.EIdentification_role) LangUtils
				.createInstanceIfNeeded(
						context,
						jsdai.SManagement_resources_schema.CIdentification_role.definition,
						irStructure);
		applied_identification_assignment.setRole(null, identification_role);
	}

	/**
	 * Unsets Arm version_id attribute (common case)
	 * 
	 * @param context
	 * @param aimEntity
	 * @throws SdaiException
	 */
	public static void unsetArmVersion_idE(SdaiContext context,
			EEntity aimEntity) throws SdaiException {
		//applied_identification_assignment
		EApplied_identification_assignment applied_identification_assignment = null;
		AApplied_identification_assignment aApplied_identification_assignment = new AApplied_identification_assignment();
		CApplied_identification_assignment
				.usedinItems(null, aimEntity, context.domain,
						aApplied_identification_assignment);
		for (int i = 1; i <= aApplied_identification_assignment
				.getMemberCount(); i++) {
			applied_identification_assignment = aApplied_identification_assignment
					.getByIndex(i);

			//role
			if (applied_identification_assignment.testRole(null)) {
				jsdai.SManagement_resources_schema.EIdentification_role identification_role = applied_identification_assignment
						.getRole(null);
				if (identification_role.testName(null)
						&& identification_role.getName(null).equals("version")) {
					AIdentification_item aItems = applied_identification_assignment
							.getItems(null);
					while (aItems.isMember(aimEntity)) {
						aItems.removeUnordered(aimEntity);
					}

					if (aItems.getMemberCount() == 0
							&& CxAp214ArmUtilities.countEntityUsers(context,
									applied_identification_assignment) == 0) {
						applied_identification_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								identification_role) == 0) {
							identification_role.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Sets deriver role attribute.
	 * 
	 * @param context
	 * @param instance
	 * @param object_role
	 * @throws SdaiException
	 */
	public static void setDerivedRole(SdaiContext context, EEntity instance,
			jsdai.SBasic_attribute_schema.EObject_role object_role)
			throws SdaiException {
		jsdai.SBasic_attribute_schema.ERole_association role_association = null;
		jsdai.SBasic_attribute_schema.ARole_association aRole_association = new jsdai.SBasic_attribute_schema.ARole_association();
		jsdai.SBasic_attribute_schema.CRole_association.usedinItem_with_role(
				null, instance, context.domain, aRole_association);
		if (aRole_association.getMemberCount() > 0) {
			role_association = aRole_association.getByIndex(1);
		} else {
			role_association = (jsdai.SBasic_attribute_schema.ERole_association) context.working_model
					.createEntityInstance(jsdai.SBasic_attribute_schema.ERole_association.class);
			role_association.setItem_with_role(null, instance);
		}
		role_association.setRole(null, object_role);
	}

	/**
	 * Gets deriver role attribute.
	 * 
	 * @param context
	 * @param instance
	 * @return derived role.
	 * @throws SdaiException
	 */
	public static jsdai.SBasic_attribute_schema.EObject_role getDerivedRole(
			SdaiContext context, EEntity instance) throws SdaiException {
		jsdai.SBasic_attribute_schema.EObject_role object_role = null;
		jsdai.SBasic_attribute_schema.ERole_association role_association = null;
		jsdai.SBasic_attribute_schema.ARole_association aRole_association = new jsdai.SBasic_attribute_schema.ARole_association();
		jsdai.SBasic_attribute_schema.CRole_association.usedinItem_with_role(
				null, instance, context.domain, aRole_association);
		if (aRole_association.getMemberCount() > 0) {
			role_association = aRole_association.getByIndex(1);

			if (role_association.testRole(null)) {
				object_role = role_association.getRole(null);
			}
		}
		return object_role;
	}

	/**
	 * Unsets deriver role attribute
	 * 
	 * @param context
	 * @param instance
	 * @throws SdaiException
	 */
	public static void unsetDerivedRole(SdaiContext context, EEntity instance)
			throws SdaiException {
		jsdai.SBasic_attribute_schema.EObject_role object_role = null;

		jsdai.SBasic_attribute_schema.ERole_association role_association = null;
		jsdai.SBasic_attribute_schema.ARole_association aRole_association = new jsdai.SBasic_attribute_schema.ARole_association();
		jsdai.SBasic_attribute_schema.CRole_association.usedinItem_with_role(
				null, instance, context.domain, aRole_association);
		for (int i = 1; i <= aRole_association.getMemberCount(); i++) {
			role_association = aRole_association.getByIndex(i);
			if (role_association.testRole(null)) {
				object_role = role_association.getRole(null);

				if (countEntityUsers(context, object_role) == 1) {
					object_role.deleteApplicationInstance();
				}
			}
			role_association.deleteApplicationInstance();
		}
	}

	/**
	 * Creates jsdai.SGeometry_schema.ECartesian_point with specified name and
	 * default coordinates by specified dimension.
	 * 
	 * @param context
	 * @param name
	 * @param dimension
	 * @return created ECartesian_point instance.
	 * @throws SdaiException
	 */
	public static jsdai.SGeometry_schema.ECartesian_point createCartesian_point(
			SdaiContext context, String name, int dimension)
			throws SdaiException {
		return createCartesian_point(context.working_model, name, dimension);
	}

	/**
	 * Creates jsdai.SGeometry_schema.ECartesian_point with specified name and
	 * default coordinates by specified dimension.
	 * 
	 * @param model
	 * @param name
	 * @param dimension
	 * @return created ECartesian_point instance.
	 * @throws SdaiException
	 */
	public static jsdai.SGeometry_schema.ECartesian_point createCartesian_point(
			SdaiModel model, String name, int dimension) throws SdaiException {
		jsdai.SGeometry_schema.ECartesian_point cartesian_point = (jsdai.SGeometry_schema.ECartesian_point) model
				.createEntityInstance(jsdai.SGeometry_schema.ECartesian_point.class);

		if (name != null) {
			cartesian_point.setName(null, name);
		} else {
			cartesian_point.setName(null, "");
		}

		A_double aDouble = cartesian_point.createCoordinates(null);
		for (int i = 1; i <= dimension; i++) {
			aDouble.addByIndex(i, 0);
		}

		return cartesian_point;
	}

	/**
	 * Sets mandatory default attributes for ERepresentation_item entities
	 * related with ARM Transformation.
	 * 
	 * @param context
	 * @param transformation
	 * @param spaceDimension
	 * @throws SdaiException
	 */
	public static void setAimTransformationAttributes(SdaiContext context,
			jsdai.SRepresentation_schema.ERepresentation_item transformation,
			int spaceDimension) throws SdaiException {
		setAimTransformationAttributes(context.working_model, transformation,
				spaceDimension);
	}

	/**
	 * Sets mandatory default attributes for ERepresentation_item entities
	 * related with ARM Transformation.
	 * 
	 * @param model
	 * @param transformation
	 * @param spaceDimension
	 * @throws SdaiException
	 */
	public static void setAimTransformationAttributes(SdaiModel model,
			jsdai.SRepresentation_schema.ERepresentation_item transformation,
			int spaceDimension) throws SdaiException {
		if (!transformation.testName(null)) {
			transformation.setName(null, "");
		}

		//placement
		if (transformation instanceof EPlacement) {
			if (!((EPlacement) transformation).testLocation(null)) {
				jsdai.SGeometry_schema.ECartesian_point cartesian_point = CxAp214ArmUtilities
						.createCartesian_point(model, "", spaceDimension);
				((EPlacement) transformation)
						.setLocation(null, cartesian_point);
			}

			//cartesian_transformation_operator
		} else if (transformation instanceof ECartesian_transformation_operator) {
			((ECartesian_transformation_operator) transformation).setName(
					(EFunctionally_defined_transformation) null, "");
			if (!((ECartesian_transformation_operator) transformation)
					.testLocal_origin(null)) {
				jsdai.SGeometry_schema.ECartesian_point cartesian_point = CxAp214ArmUtilities
						.createCartesian_point(model, "", spaceDimension);
				((ECartesian_transformation_operator) transformation)
						.setLocal_origin(null, cartesian_point);
			}
		}
	}

	/**
	 * Unsets attributes for ERepresentation_item entities related with ARM
	 * Transformation.
	 * 
	 * @param context
	 * @param transformation
	 * @param spaceDimension
	 * @throws SdaiException
	 */
	public static void unsetAimTransformationAttributes(SdaiContext context,
			jsdai.SRepresentation_schema.ERepresentation_item transformation)
			throws SdaiException {

		//placement
		if (transformation instanceof EPlacement) {
			if (((EPlacement) transformation).testLocation(null)) {
				jsdai.SGeometry_schema.ECartesian_point cartesian_point = ((EPlacement) transformation)
						.getLocation(null);
				((EPlacement) transformation).unsetLocation(null);
				if (CxAp214ArmUtilities.countEntityUsers(context,
						cartesian_point) == 0) {
					cartesian_point.deleteApplicationInstance();
				}
			}

			//axis1_placement
			if (transformation instanceof EAxis1_placement) {
				if (((EAxis1_placement) transformation).testAxis(null)) {
					EDirection direction = ((EAxis1_placement) transformation)
							.getAxis(null);
					((EAxis1_placement) transformation).unsetAxis(null);
					if (CxAp214ArmUtilities
							.countEntityUsers(context, direction) == 0) {
						direction.deleteApplicationInstance();
					}
				}

				//axis2_placement_2d
			} else if (transformation instanceof EAxis2_placement_2d) {
				if (((EAxis2_placement_2d) transformation)
						.testRef_direction(null)) {
					EDirection direction = ((EAxis2_placement_2d) transformation)
							.getRef_direction(null);
					((EAxis2_placement_2d) transformation)
							.unsetRef_direction(null);
					if (CxAp214ArmUtilities
							.countEntityUsers(context, direction) == 0) {
						direction.deleteApplicationInstance();
					}
				}

				//axis2_placement_3d
			} else if (transformation instanceof EAxis2_placement_3d) {
				if (((EAxis2_placement_3d) transformation).testAxis(null)) {
					EDirection direction = ((EAxis2_placement_3d) transformation)
							.getAxis(null);
					((EAxis2_placement_3d) transformation).unsetAxis(null);
					if (CxAp214ArmUtilities
							.countEntityUsers(context, direction) == 0) {
						direction.deleteApplicationInstance();
					}
				}

				if (((EAxis2_placement_3d) transformation)
						.testRef_direction(null)) {
					EDirection direction = ((EAxis2_placement_3d) transformation)
							.getRef_direction(null);
					((EAxis2_placement_3d) transformation)
							.unsetRef_direction(null);
					if (CxAp214ArmUtilities
							.countEntityUsers(context, direction) == 0) {
						direction.deleteApplicationInstance();
					}
				}
			}

			//cartesian_transformation_operator
		} else if (transformation instanceof ECartesian_transformation_operator) {
			//cartesian_transformation_operator.local_origin
			if (((ECartesian_transformation_operator) transformation)
					.testLocal_origin(null)) {
				jsdai.SGeometry_schema.ECartesian_point cartesian_point = ((ECartesian_transformation_operator) transformation)
						.getLocal_origin(null);
				((ECartesian_transformation_operator) transformation)
						.unsetLocal_origin(null);

				if (CxAp214ArmUtilities.countEntityUsers(context,
						cartesian_point) == 0) {
					cartesian_point.deleteApplicationInstance();
				}
			}

			//cartesian_transformation_operator.axis1
			if (((ECartesian_transformation_operator) transformation)
					.testAxis1(null)) {
				EDirection direction = ((ECartesian_transformation_operator) transformation)
						.getAxis1(null);
				((ECartesian_transformation_operator) transformation)
						.unsetAxis1(null);
				if (CxAp214ArmUtilities.countEntityUsers(context, direction) == 0) {
					direction.deleteApplicationInstance();
				}
			}

			//cartesian_transformation_operator.axis2
			if (((ECartesian_transformation_operator) transformation)
					.testAxis2(null)) {
				EDirection direction = ((ECartesian_transformation_operator) transformation)
						.getAxis2(null);
				((ECartesian_transformation_operator) transformation)
						.unsetAxis2(null);
				if (CxAp214ArmUtilities.countEntityUsers(context, direction) == 0) {
					direction.deleteApplicationInstance();
				}
			}
		}
	}

	/**
	 * Invokes setModified method on specified Cx object.
	 * 
	 * @param armEntity
	 * @param bModified
	 */
	public static void setModified(EMappedARMEntity armEntity, boolean bModified)
			throws SdaiException {
		if (armEntity == null)
			return;

		Method method = null;
		try {
			Class instanceClass = armEntity.getClass();
			//System.out.println("Instance class: " + instanceClass);
			method = instanceClass.getMethod("setModified",
					new Class[]{Boolean.TYPE});
			method.invoke(armEntity, new Object[]{new Boolean(bModified)});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates AIM data for ARM multi_language_string.
	 * 
	 * @param context
	 *            SDAI context
	 * @param armEntity
	 *            ARM entity instance to create multi language attribute.
	 * @param multi_language_string
	 *            multi language attribute value.
	 * @param attributeName
	 *            ARM entity instance attribute name.
	 * @throws SdaiException
	 */
	// TODO - has to be rewritten as style going to stepmod is different now.
/*	public static void createMultiLanguageAttribute(SdaiContext context,
			EMappedARMEntity armEntity,
			EMulti_language_string multi_language_string, String attributeName)
			throws SdaiException {
		createMultiLanguageAttribute(context, armEntity, multi_language_string,
				attributeName, attributeName);
	}*/

	/**
	 * Creates AIM data for ARM multi_language_string.
	 * 
	 * @param context
	 *            SDAI context
	 * @param armEntity
	 *            ARM entity instance to create multi language attribute.
	 * @param multi_language_string
	 *            multi language attribute value.
	 * @param armAttributeName
	 *            ARM entity instance attribute name.
	 * @param aimAttributeName
	 *            AIM entity instance attribute name.
	 * @throws SdaiException
	 */
	// TODO - has to be rewritten as style going to stepmod is different now.
/*	public static void createMultiLanguageAttribute(SdaiContext context,
			EMappedARMEntity armEntity,
			EMulti_language_string multi_language_string,
			String armAttributeName, String aimAttributeName)
			throws SdaiException {
		EEntity aimEntity = armEntity.getAimInstance();
		if (aimEntity == null)
			return;

		multi_language_string.createAimData(context);

		if (multi_language_string.testPrimary_language_dependent_string(null)) {
			EString_with_language string_with_language = multi_language_string
					.getPrimary_language_dependent_string(null);

			EAttribute_language_assignment attribute_language_assignment = (EAttribute_language_assignment) string_with_language
					.getAimInstance();
			if (!attribute_language_assignment.testItems(null)) {
				attribute_language_assignment.createItems(null);
			}
			attribute_language_assignment.getItems(null)
					.addUnordered(aimEntity);
			attribute_language_assignment.setAttribute_name(null,
					armAttributeName);

			if (string_with_language.testContents(null)) {
				String contents = string_with_language.getContents(null);
				EAttribute attribute = LangUtils.findExplicitAttribute(
						aimEntity.getInstanceType(), aimAttributeName);
				aimEntity.set(attribute, contents, new EDefined_type[20]);
			}
		}

		if (multi_language_string
				.testAdditional_language_dependent_string(null)) {
			AString_with_language aString_with_language = multi_language_string
					.getAdditional_language_dependent_string(null);
			EString_with_language string_with_language = null;
			jsdai.SAutomotive_design.EMulti_language_attribute_assignment multi_language_attribute_assignment = null;
			for (int i = 1; i <= aString_with_language.getMemberCount(); i++) {
				string_with_language = aString_with_language.getByIndex(i);

				multi_language_attribute_assignment = (jsdai.SAutomotive_design.EMulti_language_attribute_assignment) string_with_language
						.getAimInstance();
				if (!multi_language_attribute_assignment.testItems(null)) {
					multi_language_attribute_assignment.createItems(null);
				}
				multi_language_attribute_assignment.getItems(null)
						.addUnordered(aimEntity);
				multi_language_attribute_assignment.setAttribute_name(null,
						armAttributeName);
			}
		}
	}*/

	/**
	 * Removes AIM data for ARM multi_language_string.
	 * 
	 * @param context
	 *            SDAI context
	 * @param armEntity
	 *            ARM entity instance to remove multi language attribute.
	 * @param attributeName
	 *            ARM entity instance attribute name.
	 * @throws SdaiException
	 */
	public static void removeMultiLanguageAttribute(SdaiContext context,
			EMappedARMEntity armEntity, String attributeName)
			throws SdaiException {
		removeMultiLanguageAttribute(context, armEntity, null, attributeName);
	}

	/**
	 * Removes AIM data for ARM multi_language_string.
	 * 
	 * @param context
	 *            SDAI context
	 * @param armEntity
	 *            ARM entity instance to remove multi language attribute.
	 * @param aimEntity
	 *            AIM entity instance to remove multi language attribute (for
	 *            cases when attribute is not applied to mapping target of ARM
	 *            entity). if null is taken ARM mapping target.
	 * @param attributeName
	 *            ARM entity instance attribute name.
	 * @throws SdaiException
	 */
	public static void removeMultiLanguageAttribute(SdaiContext context,
			EMappedARMEntity armEntity, EEntity aimEntity, String attributeName)
			throws SdaiException {

		if (aimEntity == null && armEntity != null) {
			aimEntity = armEntity.getAimInstance();
		}

		if (aimEntity == null)
			return;

		//attribute_language_assignment
		EAttribute_language_assignment attribute_language_assignment = null;
		AAttribute_language_assignment aAttribute_language_assignment = new AAttribute_language_assignment();
		CAttribute_language_assignment
				.usedinItems(null, aimEntity, context.domain,
						aAttribute_language_assignment);

		for (int i = 1; i <= aAttribute_language_assignment.getMemberCount(); i++) {
			attribute_language_assignment = aAttribute_language_assignment
					.getByIndex(i);
			if (attribute_language_assignment.testAttribute_name(null)
					&& attribute_language_assignment.getAttribute_name(null)
							.equals(attributeName)) {
				while (attribute_language_assignment.getItems(null).isMember(
						aimEntity)) {
					attribute_language_assignment.getItems(null)
							.removeUnordered(aimEntity);
				}
			}
		}

		//multi_language_attribute_assignment
		EMulti_language_attribute_assignment multi_language_attribute_assignment = null;
		AMulti_language_attribute_assignment aMulti_language_attribute_assignment = new AMulti_language_attribute_assignment();
		CMulti_language_attribute_assignment
				.usedinItems(null, aimEntity, context.domain,
						aMulti_language_attribute_assignment);

		for (int i = 1; i <= aMulti_language_attribute_assignment
				.getMemberCount(); i++) {
			multi_language_attribute_assignment = aMulti_language_attribute_assignment
					.getByIndex(i);
			if (multi_language_attribute_assignment.testAttribute_name(null)
					&& multi_language_attribute_assignment.getAttribute_name(
							null).equals(attributeName)) {
				while (multi_language_attribute_assignment.getItems(null)
						.isMember(aimEntity)) {
					multi_language_attribute_assignment.getItems(null)
							.removeUnordered(aimEntity);
				}
			}
		}
	}

	/**
	 * Creates part definition type for EProduct_definition entity instance
	 * (Used for ARM EProcess_state, EMating_definition to specify
	 * design_discipline_item_definition subtype type).
	 * 
	 * @param context
	 *            SdaiContext
	 * @param product_definition
	 *            EProduct_definition entity instance
	 * @param type
	 *            part definition type
	 * @param resuePdc
	 *            flag to specify reuse or not for product_definition_context
	 * @exception SdaiException
	 *                SdaiException
	 */
	public static void createPartDefinitionType(SdaiContext context,
			EProduct_definition product_definition, String type,
			boolean reusePdc) throws SdaiException {
		//product_definition_context_association
		EProduct_definition_context_association product_definition_context_association = (EProduct_definition_context_association) context.working_model
				.createEntityInstance(EProduct_definition_context_association.class);
		product_definition_context_association.setDefinition(null,
				product_definition);

		//product_definition_context_role
		LangUtils.Attribute_and_value_structure[] pdcrStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
				CProduct_definition_context_role.attributeName(null),
				"part definition type"),};

		EProduct_definition_context_role product_definition_context_role = (EProduct_definition_context_role) LangUtils
				.createInstanceIfNeeded(context,
						CProduct_definition_context_role.definition,
						pdcrStructure);
		product_definition_context_association.setRole(null,
				product_definition_context_role);

		if (!product_definition_context_association
				.testFrame_of_reference(null)) {
			jsdai.SApplication_context_schema.EProduct_definition_context product_definition_context = CxAp214ArmUtilities
					.createProduct_definition_context(context, type, "",
							reusePdc, null, true);
			product_definition_context_association.setFrame_of_reference(null,
					product_definition_context);
		}
	}

	/**
	 * Removes part definition type for EProduct_definition entity instance
	 * (Used for ARM EProcess_state, EMating_definition to specify
	 * design_discipline_item_definition subtype type).
	 * 
	 * @param context
	 *            SdaiContext
	 * @param product_definition
	 *            EProduct_definition entity instance
	 * @param type
	 *            part definition type
	 * @exception SdaiException
	 *                SdaiException
	 */
	public static void removePartDefinitionType(SdaiContext context,
			EProduct_definition product_definition, String type)
			throws SdaiException {
		EProduct_definition_context_association product_definition_context_association = null;
		AProduct_definition_context_association aProduct_definition_context_association = new AProduct_definition_context_association();
		CProduct_definition_context_association.usedinDefinition(null,
				product_definition, context.domain,
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
								.equals("part definition type")) {
					//product_definition_context
					if (product_definition_context_association
							.testFrame_of_reference(null)) {
						jsdai.SApplication_context_schema.EProduct_definition_context product_definition_context = product_definition_context_association
								.getFrame_of_reference(null);

						if (product_definition_context.testName(null)
								&& product_definition_context.getName(null)
										.equals(type)) {
							if (CxAp214ArmUtilities.countEntityUsers(context,
									product_definition_context_role) == 1) {
								product_definition_context_role
										.deleteApplicationInstance();
							}

							if (CxAp214ArmUtilities.countEntityUsers(context,
									product_definition_context) == 1) {
								product_definition_context
										.deleteApplicationInstance();
							}
							product_definition_context_association
									.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Removes repetitive aggregate members to leave unique components.
	 * 
	 * @param aggregate
	 *            aggregate to process.
	 * @throws SdaiException
	 */
	public static void removeSameAggregateObjects(Aggregate aggregate)
			throws SdaiException {
		int i = 1;
		int j = 1;
		Object instance = null;
		while (i <= aggregate.getMemberCount()) {
			instance = aggregate.getByIndexObject(i);
			j = i + 1;
			while (j <= aggregate.getMemberCount()) {
				if (aggregate.getByIndexObject(j) == instance) {
					aggregate.removeByIndex(j);
				} else {
					j++;
				}
			}
			i++;
		}
	}

	/**
	 * Creates AIM EApplied_date_assignment to associate aim entity instance
	 * with date with specified role name.
	 */
	public static void createApplied_date_assignmentA(SdaiContext context,
			EEntity aimEntity, EDate date, String roleName)
			throws SdaiException {
		EApplied_date_assignment applied_date_assignment = null;
		AApplied_date_assignment aApplied_date_assignment = new AApplied_date_assignment();
		CApplied_date_assignment.usedinAssigned_date(
				null, date, context.domain, aApplied_date_assignment);
		for (int i = 1; i <= aApplied_date_assignment.getMemberCount(); i++) {
			applied_date_assignment = aApplied_date_assignment.getByIndex(i);
			if (applied_date_assignment.testRole(null)) {
				EDate_role date_role = applied_date_assignment.getRole(null);
				if (date_role.testName(null)
						&& date_role.getName(null).equals(roleName)) {
					break;
				}
			}
			applied_date_assignment = null;
		}

		if (applied_date_assignment == null) {
			applied_date_assignment = (EApplied_date_assignment) context.working_model
					.createEntityInstance(EApplied_date_assignment.class);
			applied_date_assignment.setAssigned_date(null, date);

			//date_role
			LangUtils.Attribute_and_value_structure[] drStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CDate_role.attributeName(null), roleName),};

			EDate_role date_role = (EDate_role) LangUtils
					.createInstanceIfNeeded(context, CDate_role.definition,
							drStructure);
			applied_date_assignment.setRole(null, date_role);
		}

		if (!applied_date_assignment.testItems(null)) {
			applied_date_assignment.createItems(null);
		}

		applied_date_assignment.getItems(null).addUnordered(aimEntity);

	}

	/**
	 * Removes AIM EApplied_date_assignment with specified role name from aim
	 * entity instance.
	 */
	public static void removeApplied_date_assignmentA(SdaiContext context,
			EEntity aimEntity, String roleName) throws SdaiException {
		//#3: If no duration is specified as planned_end.
		//#1: If only a certain day is known.
		EApplied_date_assignment applied_date_assignment = null;
		AApplied_date_assignment aApplied_date_assignment = new AApplied_date_assignment();
		CApplied_date_assignment.usedinItems(null,
				aimEntity, context.domain, aApplied_date_assignment);
		for (int i = 1; i <= aApplied_date_assignment.getMemberCount(); i++) {
			applied_date_assignment = aApplied_date_assignment.getByIndex(i);

			//date_role
			if (applied_date_assignment.testRole(null)) {
				EDate_role date_role = applied_date_assignment.getRole(null);
				if (date_role.testName(null)
						&& date_role.getName(null).equals(roleName)) {

					while (applied_date_assignment.getItems(null).isMember(
							aimEntity)) {
						applied_date_assignment.getItems(null).removeUnordered(
								aimEntity);
					}

					if (applied_date_assignment.getItems(null).getMemberCount() == 0) {
						applied_date_assignment.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								date_role) == 0) {
							date_role.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Creates AIM EApplied_date_and_time_assignment to associate aim entity
	 * instance with date with specified role name.
	 */
	public static void createApplied_date_and_time_assignmentA(
			SdaiContext context, EEntity aimEntity,
			EDate_and_time date_and_time, String roleName) throws SdaiException {
		EApplied_date_and_time_assignment applied_date_and_time_assignment = null;
		AApplied_date_and_time_assignment aApplied_date_and_time_assignment = new AApplied_date_and_time_assignment();
		CApplied_date_and_time_assignment.usedinAssigned_date_and_time(null, date_and_time,
						context.domain, aApplied_date_and_time_assignment);
		for (int i = 1; i <= aApplied_date_and_time_assignment.getMemberCount(); i++) {
			applied_date_and_time_assignment = aApplied_date_and_time_assignment
					.getByIndex(i);
			if (applied_date_and_time_assignment.testRole(null)) {
				EDate_time_role date_time_role = applied_date_and_time_assignment
						.getRole(null);
				if (date_time_role.testName(null)
						&& date_time_role.getName(null).equals(roleName)) {
					break;
				}
			}
			applied_date_and_time_assignment = null;
		}

		if (applied_date_and_time_assignment == null) {
			applied_date_and_time_assignment = (EApplied_date_and_time_assignment) context.working_model
					.createEntityInstance(EApplied_date_and_time_assignment.class);
			applied_date_and_time_assignment.setAssigned_date_and_time(null,
					date_and_time);

			//date_time_role
			LangUtils.Attribute_and_value_structure[] drStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CDate_time_role.attributeName(null), roleName),};

			EDate_time_role date_time_role = (EDate_time_role) LangUtils
					.createInstanceIfNeeded(context,
							CDate_time_role.definition, drStructure);
			applied_date_and_time_assignment.setRole(null, date_time_role);
		}

		if (!applied_date_and_time_assignment.testItems(null)) {
			applied_date_and_time_assignment.createItems(null);
		}

		applied_date_and_time_assignment.getItems(null).addUnordered(aimEntity);

	}

	/**
	 * Removes AIM EApplied_date_and_time_assignment with specified role name
	 * from aim entity instance.
	 */
	public static void removeApplied_date_and_time_assignmentA(
			SdaiContext context, EEntity aimEntity, String roleName)
			throws SdaiException {
		//#3: If no duration is specified as planned_end.
		//#2: If a certain day and the time of day is known.
		EApplied_date_and_time_assignment applied_date_and_time_assignment = null;
		AApplied_date_and_time_assignment aApplied_date_and_time_assignment = new AApplied_date_and_time_assignment();
		CApplied_date_and_time_assignment.usedinItems(
				null, aimEntity, context.domain,
				aApplied_date_and_time_assignment);
		for (int i = 1; i <= aApplied_date_and_time_assignment.getMemberCount(); i++) {
			applied_date_and_time_assignment = aApplied_date_and_time_assignment
					.getByIndex(i);

			//date_time_role
			if (applied_date_and_time_assignment.testRole(null)) {
				EDate_time_role date_time_role = applied_date_and_time_assignment
						.getRole(null);
				if (date_time_role.testName(null)
						&& date_time_role.getName(null).equals(roleName)) {
					while (applied_date_and_time_assignment.getItems(null)
							.isMember(aimEntity)) {
						applied_date_and_time_assignment.getItems(null)
								.removeUnordered(aimEntity);
					}

					if (applied_date_and_time_assignment.getItems(null)
							.getMemberCount() == 0) {
						applied_date_and_time_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								date_time_role) == 0) {
							date_time_role.deleteApplicationInstance();
						}
					}

				}
			}
		}
	}

	/**
	 * Creates AIM EApplied_event_occurrence_assignment to associate aim entity
	 * instance with date with specified role name.
	 */
	public static void createApplied_event_occurrence_assignmentA(
			SdaiContext context, EEntity aimEntity,
			EEvent_occurrence event_occurrence, String roleName)
			throws SdaiException {
		EApplied_event_occurrence_assignment applied_event_occurrence_assignment = null;
		AApplied_event_occurrence_assignment aApplied_event_occurrence_assignment = new AApplied_event_occurrence_assignment();
		CApplied_event_occurrence_assignment
				.usedinAssigned_event_occurrence(null, event_occurrence,
						context.domain, aApplied_event_occurrence_assignment);
		for (int i = 1; i <= aApplied_event_occurrence_assignment
				.getMemberCount(); i++) {
			applied_event_occurrence_assignment = aApplied_event_occurrence_assignment
					.getByIndex(i);
			if (applied_event_occurrence_assignment.testRole(null)) {
				EEvent_occurrence_role event_occurrence_role = applied_event_occurrence_assignment
						.getRole(null);
				if (event_occurrence_role.testName(null)
						&& event_occurrence_role.getName(null).equals(roleName)) {
					break;
				}
			}
			applied_event_occurrence_assignment = null;
		}

		if (applied_event_occurrence_assignment == null) {
			applied_event_occurrence_assignment = (EApplied_event_occurrence_assignment) context.working_model
					.createEntityInstance(EApplied_event_occurrence_assignment.class);
			applied_event_occurrence_assignment.setAssigned_event_occurrence(
					null, event_occurrence);

			//event_occurrence_role
			LangUtils.Attribute_and_value_structure[] drStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CEvent_occurrence_role.attributeName(null), roleName),};

			EEvent_occurrence_role event_occurrence_role = (EEvent_occurrence_role) LangUtils
					.createInstanceIfNeeded(context,
							CEvent_occurrence_role.definition, drStructure);
			applied_event_occurrence_assignment.setRole(null,
					event_occurrence_role);
		}

		if (!applied_event_occurrence_assignment.testItems(null)) {
			applied_event_occurrence_assignment.createItems(null);
		}

		applied_event_occurrence_assignment.getItems(null).addUnordered(
				aimEntity);

	}

	/**
	 * Removes AIM EApplied_event_occurrence_assignment with specified role name
	 * from aim entity instance.
	 */
	public static void removeApplied_event_occurrence_assignmentA(
			SdaiContext context, EEntity aimEntity, String roleName)
			throws SdaiException {
		//#3: If no duration is specified as planned_end.
		//#1: If only a certain day is known.
		EApplied_event_occurrence_assignment applied_event_occurrence_assignment = null;
		AApplied_event_occurrence_assignment aApplied_event_occurrence_assignment = new AApplied_event_occurrence_assignment();
		CApplied_event_occurrence_assignment
				.usedinItems(null, aimEntity, context.domain,
						aApplied_event_occurrence_assignment);
		for (int i = 1; i <= aApplied_event_occurrence_assignment
				.getMemberCount(); i++) {
			applied_event_occurrence_assignment = aApplied_event_occurrence_assignment
					.getByIndex(i);

			//event_occurrence_role
			if (applied_event_occurrence_assignment.testRole(null)) {
				EEvent_occurrence_role event_occurrence_role = applied_event_occurrence_assignment
						.getRole(null);
				if (event_occurrence_role.testName(null)
						&& event_occurrence_role.getName(null).equals(roleName)) {

					while (applied_event_occurrence_assignment.getItems(null)
							.isMember(aimEntity)) {
						applied_event_occurrence_assignment.getItems(null)
								.removeUnordered(aimEntity);
					}

					if (applied_event_occurrence_assignment.getItems(null)
							.getMemberCount() == 0) {
						applied_event_occurrence_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								event_occurrence_role) == 0) {
							event_occurrence_role.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Creates AIM EApplied_organization_assignment to associate aim entity
	 * instance with date with specified role name.
	 */
	public static void createApplied_organization_assignment(
			SdaiContext context, EEntity aimEntity,
			jsdai.SPerson_organization_schema.EOrganization organization,
			String roleName) throws SdaiException {
		//applied_organization_assignment
		//try to find existing assignment to organization.
		EApplied_organization_assignment applied_organization_assignment = null;
		AApplied_organization_assignment aApplied_organization_assignment = new AApplied_organization_assignment();
		CApplied_organization_assignment
				.usedinAssigned_organization(null, organization,
						context.domain, aApplied_organization_assignment);
		for (int i = 1; i <= aApplied_organization_assignment.getMemberCount(); i++) {
			applied_organization_assignment = aApplied_organization_assignment
					.getByIndex(i);

			//organization_role
			if (applied_organization_assignment.testRole(null)) {
				jsdai.SPerson_organization_schema.EOrganization_role organization_role = applied_organization_assignment
						.getRole(null);
				if (organization_role.testName(null)
						&& organization_role.getName(null).equals(roleName)) {
					break;
				}
			}
			applied_organization_assignment = null;
		}

		if (applied_organization_assignment == null) {
			applied_organization_assignment = (EApplied_organization_assignment) context.working_model
					.createEntityInstance(EApplied_organization_assignment.class);
			applied_organization_assignment.setAssigned_organization(null,
					organization);

			//organization_role
			LangUtils.Attribute_and_value_structure[] orStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					jsdai.SPerson_organization_schema.COrganization_role
							.attributeName(null), roleName),};

			jsdai.SPerson_organization_schema.EOrganization_role organization_role = (jsdai.SPerson_organization_schema.EOrganization_role) LangUtils
					.createInstanceIfNeeded(
							context,
							jsdai.SPerson_organization_schema.COrganization_role.definition,
							orStructure);
			applied_organization_assignment.setRole(null, organization_role);

		}

		if (!applied_organization_assignment.testItems(null)) {
			applied_organization_assignment.createItems(null);
		}
		applied_organization_assignment.getItems(null).addUnordered(aimEntity);

	}

	/**
	 * Removes AIM EApplied_organization_assignment with specified role name
	 * from aim entity instance.
	 */
	public static void removeApplied_organization_assignment(
			SdaiContext context, EEntity aimEntity, String roleName)
			throws SdaiException {
		EApplied_organization_assignment applied_organization_assignment = null;
		AApplied_organization_assignment aApplied_organization_assignment = new AApplied_organization_assignment();
		CApplied_organization_assignment.usedinItems(
				null, aimEntity, context.domain,
				aApplied_organization_assignment);
		CxAp214ArmUtilities
				.removeSameAggregateObjects(aApplied_organization_assignment);

		for (int i = 1; i <= aApplied_organization_assignment.getMemberCount(); i++) {
			applied_organization_assignment = aApplied_organization_assignment
					.getByIndex(i);

			//organization_role
			if (applied_organization_assignment.testRole(null)) {
				jsdai.SPerson_organization_schema.EOrganization_role organization_role = applied_organization_assignment
						.getRole(null);
				if (organization_role.testName(null)
						&& organization_role.getName(null).equals(roleName)) {
					while (applied_organization_assignment.getItems(null)
							.isMember(aimEntity)) {
						applied_organization_assignment.getItems(null)
								.removeUnordered(aimEntity);
					}

					if (applied_organization_assignment.getItems(null)
							.getMemberCount() == 0) {
						applied_organization_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								organization_role) == 0) {
							organization_role.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Creates AIM EApplied_person_and_organization_assignment to associate aim
	 * entity instance with date with specified role name.
	 */
	public static void createApplied_person_and_organization_assignment(
			SdaiContext context,
			EEntity aimEntity,
			jsdai.SPerson_organization_schema.EPerson_and_organization person_and_organization,
			String roleName) throws SdaiException {
		//applied_person_and_organization_assignment
		//try to find existing assignment to person_and_organization.
		EApplied_person_and_organization_assignment applied_person_and_organization_assignment = null;
		AApplied_person_and_organization_assignment aApplied_person_and_organization_assignment = new AApplied_person_and_organization_assignment();
		CApplied_person_and_organization_assignment
				.usedinAssigned_person_and_organization(null,
						person_and_organization, context.domain,
						aApplied_person_and_organization_assignment);
		for (int i = 1; i <= aApplied_person_and_organization_assignment
				.getMemberCount(); i++) {
			applied_person_and_organization_assignment = aApplied_person_and_organization_assignment
					.getByIndex(i);

			//person_and_organization_role
			if (applied_person_and_organization_assignment.testRole(null)) {
				jsdai.SPerson_organization_schema.EPerson_and_organization_role person_and_organization_role = applied_person_and_organization_assignment
						.getRole(null);
				if (person_and_organization_role.testName(null)
						&& person_and_organization_role.getName(null).equals(
								roleName)) {
					break;
				}
			}
			applied_person_and_organization_assignment = null;
		}

		if (applied_person_and_organization_assignment == null) {
			applied_person_and_organization_assignment = (EApplied_person_and_organization_assignment) context.working_model
					.createEntityInstance(EApplied_person_and_organization_assignment.class);
			applied_person_and_organization_assignment
					.setAssigned_person_and_organization(null,
							person_and_organization);

			//person_and_organization_role
			LangUtils.Attribute_and_value_structure[] orStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					jsdai.SPerson_organization_schema.CPerson_and_organization_role
							.attributeName(null), roleName),};

			jsdai.SPerson_organization_schema.EPerson_and_organization_role person_and_organization_role = (jsdai.SPerson_organization_schema.EPerson_and_organization_role) LangUtils
					.createInstanceIfNeeded(
							context,
							jsdai.SPerson_organization_schema.CPerson_and_organization_role.definition,
							orStructure);
			applied_person_and_organization_assignment.setRole(null,
					person_and_organization_role);

		}

		if (!applied_person_and_organization_assignment.testItems(null)) {
			applied_person_and_organization_assignment.createItems(null);
		}
		applied_person_and_organization_assignment.getItems(null).addUnordered(
				aimEntity);

	}

	/**
	 * Removes AIM EApplied_person_and_organization_assignment with specified
	 * role name from aim entity instance.
	 */
	public static void removeApplied_person_and_organization_assignment(
			SdaiContext context, EEntity aimEntity, String roleName)
			throws SdaiException {
		EApplied_person_and_organization_assignment applied_person_and_organization_assignment = null;
		AApplied_person_and_organization_assignment aApplied_person_and_organization_assignment = new AApplied_person_and_organization_assignment();
		CApplied_person_and_organization_assignment
				.usedinItems(null, aimEntity, context.domain,
						aApplied_person_and_organization_assignment);
		CxAp214ArmUtilities
				.removeSameAggregateObjects(aApplied_person_and_organization_assignment);

		for (int i = 1; i <= aApplied_person_and_organization_assignment
				.getMemberCount(); i++) {
			applied_person_and_organization_assignment = aApplied_person_and_organization_assignment
					.getByIndex(i);

			//person_and_organization_role
			if (applied_person_and_organization_assignment.testRole(null)) {
				jsdai.SPerson_organization_schema.EPerson_and_organization_role person_and_organization_role = applied_person_and_organization_assignment
						.getRole(null);
				if (person_and_organization_role.testName(null)
						&& person_and_organization_role.getName(null).equals(
								roleName)) {
					while (applied_person_and_organization_assignment.getItems(
							null).isMember(aimEntity)) {
						applied_person_and_organization_assignment.getItems(
								null).removeUnordered(aimEntity);
					}

					if (applied_person_and_organization_assignment.getItems(
							null).getMemberCount() == 0) {
						applied_person_and_organization_assignment
								.deleteApplicationInstance();

						if (CxAp214ArmUtilities.countEntityUsers(context,
								person_and_organization_role) == 0) {
							person_and_organization_role
									.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	/**
	 * Creates descriptive_representation_item with specified name and
	 * description. Adds it to items of representation if not null.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param representation
	 *            representation to set created descriptive_representation_item.
	 * @param name
	 *            name of descriptive_representation_item.
	 * @param description
	 *            description of descriptive_representation_item.
	 * @throws SdaiException
	 */
	public static EDescriptive_representation_item createDescriptive_representation_item(
			SdaiContext context, ERepresentation representation, String name,
			String description) throws SdaiException {
		//descriptive_representation_item
		EDescriptive_representation_item descriptive_representation_item = (EDescriptive_representation_item) context.working_model
				.createEntityInstance(EDescriptive_representation_item.class);
		descriptive_representation_item.setName(null, name);
		descriptive_representation_item.setDescription(null, description);

		if (representation != null) {
			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
			representation.getItems(null).addUnordered(
					descriptive_representation_item);
		}
		return descriptive_representation_item;
	}

	/**
	 * Removes descriptive_representation_item with specified name from
	 * representation items.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param representation
	 *            representation to set remove descriptive_representation_items.
	 * @param name
	 *            name of descriptive_representation_item.
	 * @throws SdaiException
	 */
	public static void removeDescriptive_representation_item(
			SdaiContext context, ERepresentation representation,
			String attribute) throws SdaiException {
		if (representation != null && representation.testItems(null)) {
			ARepresentation_item aItems = representation.getItems(null);
			EDescriptive_representation_item descriptive_representation_item = null;
			CxAp214ArmUtilities.removeSameAggregateObjects(aItems);
			int i = 1;
			while (i <= aItems.getMemberCount()) {
				if (aItems.getByIndex(i) instanceof EDescriptive_representation_item) {
					descriptive_representation_item = (EDescriptive_representation_item) aItems
							.getByIndex(i);
					if (descriptive_representation_item.testName(null)
							&& descriptive_representation_item.getName(null)
									.equals(attribute)) {
						aItems.removeUnordered(descriptive_representation_item);
						if (CxAp214ArmUtilities.countEntityUsers(context,
								descriptive_representation_item) == 0) {
							descriptive_representation_item
									.deleteApplicationInstance();
						}
						i--;
					}
				}
				i++;
			}
		}
	}

	/**
	 * Creates AIM EApplied_external_identification_assignment to associate AIM
	 * entity instance with external_source with specified role name.
	 */
	public static void createApplied_external_identification_assignmentA(
			SdaiContext context, EEntity aimEntity,
			jsdai.SExternal_reference_schema.EExternal_source external_source,
			String assigned_id, String roleName) throws SdaiException {
		//applied_external_identification_assignment
		//try to find existing assignment to external_source.
		EApplied_external_identification_assignment applied_external_identification_assignment = null;
		AApplied_external_identification_assignment aApplied_external_identification_assignment = new AApplied_external_identification_assignment();
		CApplied_external_identification_assignment
				.usedinSource(null, external_source, context.domain,
						aApplied_external_identification_assignment);
		for (int i = 1; i <= aApplied_external_identification_assignment
				.getMemberCount(); i++) {
			applied_external_identification_assignment = aApplied_external_identification_assignment
					.getByIndex(i);

			if (assigned_id == null
					|| (assigned_id != null
							&& applied_external_identification_assignment
									.testAssigned_id(null) && applied_external_identification_assignment
							.getAssigned_id(null).equals(assigned_id))) {

				//identification_role
				if (applied_external_identification_assignment.testRole(null)) {
					EIdentification_role identification_role = applied_external_identification_assignment
							.getRole(null);
					if (identification_role.testName(null)
							&& identification_role.getName(null).equals(
									roleName)) {
						break;
					}
				}
			}
			applied_external_identification_assignment = null;
		}

		if (applied_external_identification_assignment == null) {
			applied_external_identification_assignment = (EApplied_external_identification_assignment) context.working_model
					.createEntityInstance(EApplied_external_identification_assignment.class);
			applied_external_identification_assignment.setSource(null,
					external_source);
			if (assigned_id != null) {
				applied_external_identification_assignment.setAssigned_id(null,
						assigned_id);
			} else {
				applied_external_identification_assignment.setAssigned_id(null,
						"");
			}

			//identification_role
			LangUtils.Attribute_and_value_structure[] irStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CIdentification_role.attributeName(null), roleName),};

			EIdentification_role identification_role = (EIdentification_role) LangUtils
					.createInstanceIfNeeded(context,
							CIdentification_role.definition, irStructure);
			applied_external_identification_assignment.setRole(null,
					identification_role);

		}

		if (!applied_external_identification_assignment.testItems(null)) {
			applied_external_identification_assignment.createItems(null);
		}
		applied_external_identification_assignment.getItems(null).addUnordered(
				aimEntity);

	}

	/**
	 * Creates AIM EApplied_external_identification_assignment to associate AIM
	 * entity instance with external_source with specified role name.
	 */
	public static void createApplied_external_identification_assignmentE(
			SdaiContext context, EEntity aimEntity,
			jsdai.SExternal_reference_schema.EExternal_source external_source,
			String assigned_id, String roleName) throws SdaiException {
		//applied_external_identification_assignment
		//try to find existing assignment to external_source.
		EApplied_external_identification_assignment applied_external_identification_assignment = null;
		AApplied_external_identification_assignment aApplied_external_identification_assignment = new AApplied_external_identification_assignment();
		CApplied_external_identification_assignment
				.usedinSource(null, external_source, context.domain,
						aApplied_external_identification_assignment);
		for (int i = 1; i <= aApplied_external_identification_assignment
				.getMemberCount(); i++) {
			applied_external_identification_assignment = aApplied_external_identification_assignment
					.getByIndex(i);

			if (assigned_id == null
					|| (assigned_id != null
							&& applied_external_identification_assignment
									.testAssigned_id(null) && applied_external_identification_assignment
							.getAssigned_id(null).equals(assigned_id))) {

				//identification_role
				if (applied_external_identification_assignment.testRole(null)) {
					EIdentification_role identification_role = applied_external_identification_assignment
							.getRole(null);
					if (identification_role.testName(null)
							&& identification_role.getName(null).equals(
									roleName)) {
						break;
					}
				}
			}
			applied_external_identification_assignment = null;
		}

		if (applied_external_identification_assignment == null) {
			applied_external_identification_assignment = (EApplied_external_identification_assignment) context.working_model
					.createEntityInstance(EApplied_external_identification_assignment.class);
			applied_external_identification_assignment.setSource(null,
					external_source);
			if (assigned_id != null) {
				applied_external_identification_assignment.setAssigned_id(null,
						assigned_id);
			} else {
				applied_external_identification_assignment.setAssigned_id(null,
						"");
			}

			//identification_role
			LangUtils.Attribute_and_value_structure[] irStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CIdentification_role.attributeName(null), roleName),};

			EIdentification_role identification_role = (EIdentification_role) LangUtils
					.createInstanceIfNeeded(context,
							CIdentification_role.definition, irStructure);
			applied_external_identification_assignment.setRole(null,
					identification_role);

		}

		if (!applied_external_identification_assignment.testItems(null)) {
			applied_external_identification_assignment.createItems(null);
		}
		applied_external_identification_assignment.getItems(null).addUnordered(
				aimEntity);

	}

	/**
	 * Removes AIM EApplied_external_identification_assignment with specified
	 * role name from aim entity instance.
	 */
	public static void removeApplied_external_identification_assignment(
			SdaiContext context, EEntity aimEntity, String assigned_id,
			String roleName) throws SdaiException {
		EApplied_external_identification_assignment applied_external_identification_assignment = null;
		AApplied_external_identification_assignment aApplied_external_identification_assignment = new AApplied_external_identification_assignment();
		CApplied_external_identification_assignment
				.usedinItems(null, aimEntity, context.domain,
						aApplied_external_identification_assignment);
		CxAp214ArmUtilities
				.removeSameAggregateObjects(aApplied_external_identification_assignment);

		for (int i = 1; i <= aApplied_external_identification_assignment
				.getMemberCount(); i++) {
			applied_external_identification_assignment = aApplied_external_identification_assignment
					.getByIndex(i);

			if (assigned_id == null
					|| (assigned_id != null
							&& applied_external_identification_assignment
									.testAssigned_id(null) && applied_external_identification_assignment
							.getAssigned_id(null).equals(assigned_id))) {

				//identification_role
				if (applied_external_identification_assignment.testRole(null)) {
					EIdentification_role identification_role = applied_external_identification_assignment
							.getRole(null);
					if (identification_role.testName(null)
							&& identification_role.getName(null).equals(
									roleName)) {
						while (applied_external_identification_assignment
								.getItems(null).isMember(aimEntity)) {
							applied_external_identification_assignment
									.getItems(null).removeUnordered(aimEntity);
						}

						if (applied_external_identification_assignment
								.getItems(null).getMemberCount() == 0) {
							applied_external_identification_assignment
									.deleteApplicationInstance();

							if (CxAp214ArmUtilities.countEntityUsers(context,
									identification_role) == 0) {
								identification_role.deleteApplicationInstance();
							}
						}
					}
				}
			}
		}
	}

	public static void setDocumentProperty(SdaiContext context,
			EEntity aimEntity, ERepresentation aimDocument_property,
			String property_definitionName) throws SdaiException {
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain,
				aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null)
					&& property_definition.getName(null).equals(
							property_definitionName)) {
				break;
			}
			property_definition = null;
		}

		if (property_definition == null) {
			property_definition = (EProperty_definition) context.working_model
					.createEntityInstance(EProperty_definition.class);
			property_definition.setName(null, property_definitionName);
			property_definition.setDefinition(null, aimEntity);
		}

		//property_definition_representation
		EProperty_definition_representation property_definition_representation = (EProperty_definition_representation) context.working_model
				.createEntityInstance(EProperty_definition_representation.class);
		property_definition_representation.setDefinition(null,
				property_definition);
		property_definition_representation.setUsed_representation(null,
				aimDocument_property);
	}

	public static void unsetDocumentProperty(SdaiContext context,
			EEntity aimEntity, String representationName,
			String property_definitionName) throws SdaiException {
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain,
				aProperty_definition);
		EProperty_definition_representation property_definition_representation = null;
		ERepresentation representation = null;
		AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null)
					&& property_definition.getName(null).equals(
							property_definitionName)) {

				//property_definition_representation
				aProperty_definition_representation.clear();
				CProperty_definition_representation.usedinDefinition(null,
						property_definition, context.domain,
						aProperty_definition_representation);
				for (int j = 1; j <= aProperty_definition_representation
						.getMemberCount(); j++) {
					property_definition_representation = aProperty_definition_representation
							.getByIndex(j);
					if (property_definition_representation
							.testUsed_representation(null)) {
						representation = property_definition_representation
								.getUsed_representation(null);
						if (representation.testName(null)
								&& representation.getName(null).equals(
										representationName)) {
							property_definition_representation
									.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}

	public static final Object KEY_AIM_INSTANCE = new Object();
	public static EEntity getAimInstance(EEntity instance, SdaiContext context)
			throws SdaiException {
		EEntity aimInstance = null;
		if (instance instanceof EMappedARMEntity) {
			aimInstance = ((EMappedARMEntity) instance).getAimInstance();

			if (aimInstance == null) {
				try {
					((EMappedARMEntity) instance).createAimData(context);
					aimInstance = ((EMappedARMEntity) instance)
							.getAimInstance();
				} catch (SdaiException e) {
				}
			}
		} else {
			aimInstance = (EEntity) instance.getTemp(KEY_AIM_INSTANCE);
			if (aimInstance == null) {
				throw new SdaiException(SdaiException.VA_NSET,
						"AIM instance not set for " + instance);
			}
		}
		return aimInstance;
	}

	public static void setAimInstance(EEntity instance, EEntity aimInstance) {
		if (instance instanceof EMappedARMEntity) {
			((EMappedARMEntity) instance).setAimInstance(aimInstance);
		} else {
			instance.setTemp(KEY_AIM_INSTANCE, aimInstance);
		}
	}

	//*************************************** ELECTROTECHNICAL_DESIGN
	// ***************************************

	/**
	 * Creates EProperty_definition instance.
	 * 
	 * @param context
	 * @param definition
	 *            EProperty_definition subtype definition
	 * @param pdName
	 *            EProperty_definition name.
	 * @param pdDescription
	 *            EProperty_definition description.
	 * @param pdDefinition
	 *            EProperty_definition definition.
	 * @param pdReuse
	 *            reuse flag.
	 * @return EProperty_definition instance.
	 * @throws SdaiException
	 */
	public static EProperty_definition createProperty_definition(
			SdaiContext context, EEntity_definition definition, String pdName,
			String pdDescription, EEntity pdDefinition, boolean pdReuse)
			throws SdaiException {
		EProperty_definition property_definition = null;
		if (definition == null) {
			definition = CProperty_definition.definition;
		}

		if (pdReuse) {
			LangUtils.Attribute_and_value_structure[] pdStructure = {
					new LangUtils.Attribute_and_value_structure(
							CProperty_definition.attributeName(null), pdName),
					new LangUtils.Attribute_and_value_structure(
							CProperty_definition.attributeDescription(null),
							pdDescription),
					new LangUtils.Attribute_and_value_structure(
							CProperty_definition.attributeDefinition(null),
							pdDefinition),};

			property_definition = (EProperty_definition) LangUtils
					.createInstanceIfNeeded(context, definition, pdStructure);

		} else {
			property_definition = (EProperty_definition) context.working_model
					.createEntityInstance(definition);
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

	public static EEntity getAimInstanceE(EEntity instance, SdaiContext context)
			throws SdaiException {
		EEntity aimInstance = null;
		if (instance instanceof EMappedARMEntity) {
			aimInstance = ((EMappedARMEntity) instance).getAimInstance();

			if (aimInstance == null) {
				try {
					((EMappedARMEntity) instance).createAimData(context);
					aimInstance = ((EMappedARMEntity) instance)
							.getAimInstance();
				} catch (SdaiException e) {
				}
			}
		} else {
			aimInstance = (EEntity) instance.getTemp(KEY_AIM_INSTANCE);
		}

		return aimInstance;
	}

	/**
	 * Creates representation_context if it is needed
	 * 
	 * @param context
	 * @throws SdaiException
	 */
	public static ERepresentation_context createDefaultRepresentationContext(
			SdaiContext context) throws SdaiException {
		LangUtils.Attribute_and_value_structure attributeAndValueStructure[] = {
				new LangUtils.Attribute_and_value_structure(
						CRepresentation_context
								.attributeContext_identifier(null), ""),
				new LangUtils.Attribute_and_value_structure(
						CRepresentation_context.attributeContext_type(null), "")};
		ERepresentation_context reprContext = (ERepresentation_context) LangUtils
				.createInstanceIfNeeded(context,
						CRepresentation_context.definition,
						attributeAndValueStructure);
		return reprContext;
	}

	/**
	 * Deletes representation_context if it is not used
	 * 
	 * @param context
	 * @throws SdaiException
	 */
	public static void deleteDefaultRepresentationContext(SdaiContext context,
			CRepresentation representation) throws SdaiException {
		if (representation.testContext_of_items(null)) {
			ERepresentation_context reprContext = representation
					.getContext_of_items(null);
			representation.unsetContext_of_items(null);
			if (countEntityUsers(context, reprContext) == 0)
				reprContext.deleteApplicationInstance();
		}
	}

	public static boolean belongsToPropertyAssociationSelectAP214(
			EEntity instance) {
		return instance instanceof EProduct_class_armx
				|| instance instanceof EDesign_constraint_definition
//				|| instance instanceof ECatalogue_thread
				|| instance instanceof EProduct_occurrence //Item_instance
				|| instance instanceof EPart_view_definition
				|| instance instanceof EBreakdown_element_usage_armx
				|| instance instanceof EPart_definition_relationship
				|| instance instanceof EItem_definition_instance_relationship
				|| instance instanceof EItem_instance_relationship
				|| instance instanceof EItem_shape
				|| instance instanceof EShape_element
				|| instance instanceof EShape_element_relationship
				|| instance instanceof EBreakdown_element_definition
				|| instance instanceof EDocument_file
				|| instance instanceof EDocument_representation_type
				|| instance instanceof EShape_feature_definition_armx
				|| instance instanceof EGeneral_feature_armx
//				|| instance instanceof EThread_feature
				|| instance instanceof EProduct_identification;
//				|| instance instanceof EProduct_as_individual;
	}

	public static boolean belongsToProcessPropertyAssociationSelectAP214(
			EEntity instance) {
		return instance instanceof EProcess_plan_armx
				|| instance instanceof EProcess_operation_occurrence
				|| instance instanceof EProcess_operation_resource_assignment
				|| instance instanceof EActivity
				|| instance instanceof EAction_request_solution
				|| instance instanceof EProcess_operation_definition;
	}
}