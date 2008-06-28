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

package jsdai.SInterconnect_module_connection_routing_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SInterconnect_module_usage_view_mim.EInterconnect_module_terminal;
import jsdai.SInterconnect_module_with_macros_mim.EInterconnect_module_macro_component_join_terminal;
import jsdai.SLayered_interconnect_module_design_mim.CLayer_connection_point;
import jsdai.SLayered_interconnect_module_design_mim.ELaminate_component_join_terminal;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SPhysical_unit_design_view_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxDependently_located_layer_connection_point extends CDependently_located_layer_connection_point implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF From CShape_aspect.java
	

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLayer_connection_point.definition);

		setMappingConstraints(context, this);
		
		// location_2d
		setLocation_2d(context, this);
		
      // location_3d
		setLocation_3d(context, this);
		
		// associated_design_object
		setAssociated_design_object(context, this);
		
      // reference_zone
		setReference_zone(context, this);
		
		// Clean ARM

		// location_2d
		unsetLocation_2d(null);
		
      // location_3d
		unsetLocation_3d(null);

		// associated_design_object
		unsetAssociated_design_object(null);
		
      // reference_zone
		unsetReference_zone(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// location_2d
			unsetLocation_2d(context, this);
			
	      // location_3d
			unsetLocation_3d(context, this);

			// associated_design_object
			unsetAssociated_design_object(context, this);
			
	      // reference_zone
			unsetReference_zone(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; layer_connection_point &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'dependently located'}
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
			EDependently_located_layer_connection_point armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxLayer_connection_point_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "dependently located");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EDependently_located_layer_connection_point armEntity) throws SdaiException {
		CxLayer_connection_point_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}


	//********** "layer_connection_point" attributes

	/**
	* Sets/creates data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// LCP <- PD <- PDR -> R -> CP
	public static void setLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.setLocation_2d(context, armEntity);		
	}


	/**
	* Unsets/deletes data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.unsetLocation_2d(context, armEntity);
	}

	/**
	* Sets/creates data for location_3d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.setLocation_3d(context, armEntity);		
	}


	/**
	* Unsets/deletes data for location_3d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.unsetLocation_3d(context, armEntity);		
	}



	//********** "dependently_located_layer_connection_point" attributes

	/**
	* Sets/creates data for associated_design_object attribute.
	*
            <aa attribute="associated_design_object" assertion_to="terminal_or_inter_stratum_feature">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">layer_connection_point &lt;=
					shape_aspect &lt;-
					shape_aspect_relationship.related_shape_aspect
					shape_aspect_relationship
					{shape_aspect_relationship
					shape_aspect_relationship.name = 'associated design object'}
					shape_aspect_relationship.relating_shape_aspect -&gt;
					shape_aspect
					(shape_aspect =&gt;
					component_feature =&gt;
					component_terminal =&gt;
					laminate_component_join_terminal)
					(shape_aspect =&gt;
					interconnect_module_terminal)
					(shape_aspect =&gt;
					component_feature =&gt;
					component_terminal =&gt;
					interconnect_module_macro_component_join_terminal)
					(shape_aspect.of_shape -&gt;
					product_definition_shape &lt;=
					property_definition
					property_definition.definition -&gt;
					product_definition =&gt;
					component_definition =&gt;
					assembly_component =&gt;
					inter_stratum_feature)
					</refpath>
            </aa>
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAssociated_design_object(SdaiContext context, EDependently_located_layer_connection_point armEntity) throws SdaiException
	{
		//unset old values
		unsetAssociated_design_object(context, armEntity);

		if (armEntity.testAssociated_design_object(null))
		{

				EEntity armAssociated_design_object = armEntity.getAssociated_design_object(null);
				EShape_aspect esa = null;
				if(armAssociated_design_object instanceof EShape_aspect){
					esa = (EShape_aspect)armAssociated_design_object;
				}else if(armAssociated_design_object instanceof EAssembly_component_armx){

					AShape_aspect asa = new AShape_aspect(); 
					CShape_aspect.usedinOf_shape(null, (EAssembly_component_armx)armAssociated_design_object, 
							context.domain, asa);
					SdaiIterator iterator = asa.createIterator();
					while(iterator.next()){
						EShape_aspect esaTemp = asa.getCurrentMember(iterator);
			            if((esaTemp instanceof ELaminate_component_join_terminal)||
				            	(esaTemp instanceof EInterconnect_module_macro_component_join_terminal)||	
				            	(esaTemp instanceof EInterconnect_module_terminal)){
							continue;
			            }
						esa = esaTemp;
						break;	
					}
					if(esa == null){
						// If it is one of the terminals - we can't use them - need other type of Shape_aspect
		            	esa = (EShape_aspect)context.working_model.createEntityInstance(CShape_aspect.definition);
		            	esa.setOf_shape(null, (EAssembly_component_armx)armAssociated_design_object);
		            }
		            
		            if (!esa.testName(null)) 
		               esa.setName(null, "");
		            if (!esa.testProduct_definitional(null)) 
		               esa.setProduct_definitional(null, ELogical.UNKNOWN);
				}
				else
					throw new SdaiException(SdaiException.ED_NVLD," this type of associated object is not supported "+armAssociated_design_object);

				EShape_aspect_relationship
						shape_aspect_relationship = (EShape_aspect_relationship)
								(context.working_model.createEntityInstance(CShape_aspect_relationship.class));

				shape_aspect_relationship.setName(null, "associated design object");
				shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
				shape_aspect_relationship.setRelating_shape_aspect(null, esa);

		}
	}


	/**
	* Unsets/deletes data for associated_design_object attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_design_object(SdaiContext context, EDependently_located_layer_connection_point armEntity) throws SdaiException
	{
		AShape_aspect_relationship results = new AShape_aspect_relationship();

		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, results);
		EShape_aspect_relationship sar = null;

		final String KEYWORD = "associated design object";

		for (int i = 1; i<= results.getMemberCount(); i++)
		{
			sar = (EShape_aspect_relationship)results.getByIndexEntity(i);

			if (sar.testName(null)){
				if (!KEYWORD.equals(sar.getName(null))){
					continue;
				}
			}
			System.err.println(" DELETING "+sar);
			sar.deleteApplicationInstance();

		}//for

	}	

	/**
	* Sets/creates data for associated_design_object attribute.
	*
            <aa attribute="reference_zone" assertion_to="Connection_zone_in_design_view">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">layer_connection_point &lt;=
						shape_aspect &lt;-
						shape_aspect_relationship.related_shape_aspect
						shape_aspect_relationship
						{shape_aspect_relationship
						shape_aspect_relationship.name = 'reference zone'}
						shape_aspect_relationship.relating_shape_aspect -&gt;
						shape_aspect
						{shape_aspect.description = 'connection zone'}
						</refpath>
            </aa>
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setReference_zone(SdaiContext context, EDependently_located_layer_connection_point armEntity) throws SdaiException
	{
		//unset old values
		unsetReference_zone(context, armEntity);

		if (armEntity.testReference_zone(null))
		{
			EConnection_zone_in_design_view armAssociated_design_object = armEntity.getReference_zone(null);
			EShape_aspect_relationship
					shape_aspect_relationship = (EShape_aspect_relationship)
							(context.working_model.createEntityInstance(CShape_aspect_relationship.class));
			shape_aspect_relationship.setName(null, "reference zone");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armAssociated_design_object);

		}
	}


	/**
	* Unsets/deletes data for associated_design_object attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetReference_zone(SdaiContext context, EDependently_located_layer_connection_point armEntity) throws SdaiException
	{
		AShape_aspect_relationship results = new AShape_aspect_relationship();

		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, results);
		EShape_aspect_relationship sar = null;

		final String KEYWORD = "reference zone";

		for (int i = 1; i<= results.getMemberCount(); i++)
		{
			sar = (EShape_aspect_relationship)results.getByIndexEntity(i);

			if (sar.testName(null)){
				if (!KEYWORD.equals(sar.getName(null))){
					continue;
				}
			}
			sar.deleteApplicationInstance();

		}//for

	}	
	
	
}