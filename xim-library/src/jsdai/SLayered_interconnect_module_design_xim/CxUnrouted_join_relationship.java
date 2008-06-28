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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SLayered_interconnect_module_design_mim.CJoin_shape_aspect;
import jsdai.SProduct_property_definition_schema.*;

public class CxUnrouted_join_relationship extends CUnrouted_join_relationship implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Shape_aspect
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a0$;
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


	// ENDOF Taken from Shape_aspect
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CJoin_shape_aspect.definition);

		setMappingConstraints(context, this);
		
		// unrouted_terminals
		setUnrouted_terminals(context, this);
		
		// unrouted_lcp
		setUnrouted_lcp(context, this);
		
      // unrouted_junction
		setUnrouted_junction(context, this);
		
		// unrouted_paa
		setUnrouted_paa(context, this);
		
		// unrouted_via
		setUnrouted_via(context, this);
		
		// Clean ARM
		// unrouted_terminals
		unsetUnrouted_terminals(null);
		
		// unrouted_lcp
		unsetUnrouted_lcp(null);
		
      // unrouted_junction
		unsetUnrouted_junction(null);
		
		// unrouted_paa
		unsetUnrouted_paa(null);
		
		// unrouted_via
		unsetUnrouted_via(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// unrouted_terminals
		unsetUnrouted_terminals(context, this);
		
		// unrouted_lcp
		unsetUnrouted_lcp(context, this);
		
      // unrouted_junction
		unsetUnrouted_junction(context, this);
		
		// unrouted_paa
		unsetUnrouted_paa(context, this);
		
		// unrouted_via
		unsetUnrouted_via(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	join_shape_aspect &lt;=
	*  shape_aspect
	*  {shape_aspect
	*  (shape_aspect.name = 'unrouted join')
	*  (shape_aspect.name = 'constrained intra layer join')
	*  (shape_aspect.name = 'inter stratum join')
	*  (shape_aspect.name = 'stratum embedded component join')
	*  (shape_aspect.name = 'intra stratum join')}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxJoin_relationship.setMappingConstraints(context, armEntity);
		armEntity.setName((EShape_aspect)null, "unrouted join");
	}

	public static void unsetMappingConstraints(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		CxJoin_relationship.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName((EShape_aspect)null);
	}

	//********** "unrouted_join_relationship" attributes

	/**
	* Sets/creates data for unrouted_terminals attribute.
	*
	* <p>
	*  attribute_mapping unrouted_terminals_laminate_component_join_terminal (unrouted_terminals
	* , (*PATH*), laminate_component_join_terminal);
	* 	join_shape_aspect <= 
	* 	shape_aspect <- 
	* 	shape_aspect_relationship.related_shape_aspect 
	* 	shape_aspect_relationship 
	* 	{shape_aspect_relationship 
	* 	shape_aspect_relationship.name = `unrouted terminals'} 
	* 	shape_aspect_relationship.relating_shape_aspect -> 
	* 	shape_aspect => 
	* 	{shape_aspect 
	* 	(shape_aspect.description = `land join terminal') 
	* 	(shape_aspect.description = `non functional land join terminal') 
	* 	(shape_aspect.description = `component termination passage join terminal') 
	* 	(shape_aspect.description = `embedded component terminal') 
	* 	(shape_aspect.description = `printed component join terminal')} 
	* 	component_terminal 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	// JSA <- SAR -> CT
	public static void setUnrouted_terminals(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetUnrouted_terminals(context, armEntity);
		
		if (armEntity.testUnrouted_terminals(null))
		{ 
			ALaminate_component_join_terminal_armx armUnrouted_terminals = armEntity.getUnrouted_terminals(null);
			SdaiIterator iter = armUnrouted_terminals.createIterator();
			while(iter.next()){
				ELaminate_component_join_terminal_armx terminal = armUnrouted_terminals.getCurrentMember(iter);
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelating_shape_aspect(null, terminal);
				sar.setRelated_shape_aspect(null, armEntity);
				sar.setName(null, "unrouted terminals");
			}
		}
	}


	/**
	* Unsets/deletes data for unrouted_terminals attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetUnrouted_terminals(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("unrouted terminals")){
				EShape_aspect esa = esar.getRelating_shape_aspect(null);
				if((esa.testDescription(null))&&(
						(esa.getDescription(null).equals("land join terminal"))||
						(esa.getDescription(null).equals("non functional land join terminal"))||
						(esa.getDescription(null).equals("component termination passage join terminal"))||
						(esa.getDescription(null).equals("embedded component terminal"))||
						(esa.getDescription(null).equals("printed component join terminal"))))
					esar.deleteApplicationInstance();
			}
		}
	}

	/**
	* Sets/creates data for unrouted_lcp attribute.
	*
	* <p>
	*  attribute_mapping unrouted_lcp_layer_connection_point (unrouted_lcp
	* , (*PATH*), layer_connection_point);
	* 	join_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `unrouted lcp'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	layer_connection_point 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setUnrouted_lcp(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetUnrouted_lcp(context, armEntity);
		
		if (armEntity.testUnrouted_lcp(null))
		{ 
			ALayer_connection_point_armx armUnrouted_lcp = armEntity.getUnrouted_lcp(null);
			SdaiIterator iter = armUnrouted_lcp.createIterator();
			while(iter.next()){
				ELayer_connection_point_armx lcp = armUnrouted_lcp.getCurrentMember(iter);
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelating_shape_aspect(null, lcp);
				sar.setRelated_shape_aspect(null, armEntity);
				sar.setName(null, "unrouted lcp");
			}

		}
	}


	/**
	* Unsets/deletes data for unrouted_lcp attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetUnrouted_lcp(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("unrouted lcp")){
				esar.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for unrouted_junction attribute.
	*
	* <p>
	*  attribute_mapping unrouted_junction_layout_junction (unrouted_junction
	* , (*PATH*), layout_junction);
	* 	join_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `unrouted junction'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	layout_junction 
	*  end_attribute_mapping;
	*      
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setUnrouted_junction(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetUnrouted_junction(context, armEntity);
		
		if (armEntity.testUnrouted_junction(null))
		{ 
			ALayout_junction_armx armUnrouted_junction = armEntity.getUnrouted_junction(null);
			SdaiIterator iter = armUnrouted_junction.createIterator();
			while(iter.next()){
				ELayout_junction_armx elj = armUnrouted_junction.getCurrentMember(iter);
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelating_shape_aspect(null, elj);
				sar.setRelated_shape_aspect(null, armEntity);
				sar.setName(null, "unrouted junction");
			}
		}
	}


	/**
	* Unsets/deletes data for unrouted_junction attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetUnrouted_junction(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("unrouted junction")){
				esar.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for unrouted_paa attribute.
	*
	* <p>
	*  attribute_mapping unrouted_paa_probe_access_area (unrouted_paa
	* , (*PATH*), probe_access_area);
	* 	join_shape_aspect &lt;=
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'unrouted paa'}
	*  shape_aspect_relationship.related_shape_aspect -&gt;
	*  shape_aspect
	*  shape_aspect.of_shape -&gt;
	*  product_definition_shape =&gt;
	*  assembly_component =&gt;
	*  probe_access_area 
	*  end_attribute_mapping;
	*      
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setUnrouted_paa(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetUnrouted_paa(context, armEntity);
		
		if (armEntity.testUnrouted_paa(null))
		{ 
			AProbe_access_area_armx armUnrouted_paa = armEntity.getUnrouted_paa(null);
			SdaiIterator iter = armUnrouted_paa.createIterator();
			while(iter.next()){
				EProbe_access_area_armx epaa = armUnrouted_paa.getCurrentMember(iter);
				// SA
				LangUtils.Attribute_and_value_structure[] structure = {new LangUtils.Attribute_and_value_structure(
						CShape_aspect.attributeOf_shape(null), epaa)};
				EShape_aspect esa = (EShape_aspect)
					LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition, structure);
				if(!esa.testName(null))
					esa.setName(null, "");
				if(!esa.testProduct_definitional(null))
					esa.setProduct_definitional(null, ELogical.UNKNOWN);
				
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelated_shape_aspect(null, esa);
				sar.setRelating_shape_aspect(null, armEntity);
				sar.setName(null, "unrouted paa");
			}
		}
	}


	/**
	* Unsets/deletes data for unrouted_paa attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetUnrouted_paa(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("unrouted paa")){
				esar.deleteApplicationInstance();
			}
		}
	}


	/**
	* Sets/creates data for unrouted_via attribute.
	*
	* <p>
	*  attribute_mapping unrouted_via_via_terminal (unrouted_via
	* , (*PATH*), via_terminal);
	* 	join_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `unrouted terminals'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	{shape_aspect
	* 	shape_aspect.description = `via terminal'}
	* 	component_terminal 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void setUnrouted_via(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetUnrouted_via(context, armEntity);
		
		if (armEntity.testUnrouted_via(null))
		{ 
			AVia_terminal armUnrouted_via = armEntity.getUnrouted_via(null);
			SdaiIterator iter = armUnrouted_via.createIterator();
			while(iter.next()){
				EVia_terminal evt = armUnrouted_via.getCurrentMember(iter);
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				sar.setRelating_shape_aspect(null, evt);
				sar.setRelated_shape_aspect(null, armEntity);
				sar.setName(null, "unrouted terminals");
			}
	
		}
	}


	/**
	* Unsets/deletes data for unrouted_via attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetUnrouted_via(SdaiContext context, EUnrouted_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1;i<=asar.getMemberCount();i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if(esar.getName(null).equals("unrouted terminals")){
				EShape_aspect esa = esar.getRelated_shape_aspect(null);
				// it has to be exactly shape_aspect
				if(esa.getInstanceType() != CShape_aspect.definition)
					continue;
				if((esa.testDescription(null))&&(esa.getDescription(null).equals("via terminal")))
					esar.deleteApplicationInstance();
			}
		}
	}
	
	
}
