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

package jsdai.SSpecification_document_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SDocument_assignment_mim.*;
import jsdai.SDocument_assignment_xim.CxDocument_assignment;
import jsdai.SDocument_assignment_xim.EDocument_assignment;
import jsdai.SManagement_resources_schema.EDocument_reference;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxSpecification_allocation extends CSpecification_allocation implements EMappedXIMEntity
{

	
	// Taken from Document_reference
	// attribute (current explicit or supertype explicit) : assigned_document, base type: entity document
/*	public static int usedinAssigned_document(EDocument_reference type, jsdai.SDocument_schema.EDocument instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testAssigned_document(EDocument_reference type) throws SdaiException {
		return test_instance(a0);
	}
	public jsdai.SDocument_schema.EDocument getAssigned_document(EDocument_reference type) throws SdaiException {
		a0 = get_instance(a0);
		return (jsdai.SDocument_schema.EDocument)a0;
	}*/
	public void setAssigned_document(EDocument_reference type, jsdai.SDocument_schema.EDocument value) throws SdaiException {
		a0 = set_instanceX(a0, value);
	}
	public void unsetAssigned_document(EDocument_reference type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeAssigned_document(EDocument_reference type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: source, base type: STRING
/*	public boolean testSource(EDocument_reference type) throws SdaiException {
		return test_string(a1);
	}
	public String getSource(EDocument_reference type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setSource(EDocument_reference type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetSource(EDocument_reference type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeSource(EDocument_reference type) throws SdaiException {
		return a1$;
	}
	
	// END OF taken from Document_reference
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CApplied_document_reference.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			// assigned_document_armx
			setAssigned_document_x(context, this);

			// role_x
			setRole_x(context, this);

			// purpose
			// setPurpose(context, this);
			
	      // Clean ARM
			// assigned_document_armx
			unsetAssigned_document_x(null);

			// role_x
			unsetRole_x(null);
			
			// purpose
			// unsetPurpose(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// assigned_document_armx
		unsetAssigned_document_x(context, this);

		// role_x
		unsetRole_x(context, this);
		
		// purpose
		// unsetPurpose(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	* mapping_constraints;
	* 	(applied_document_reference &lt;=
	*  document_reference)
	*  (applied_document_usage_constraint_assignment &lt;=
	*  document_usage_constraint_assignment)
	* end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Only first alternative is really supported
	public static void setMappingConstraints(SdaiContext context, ESpecification_allocation armEntity) throws SdaiException
	{
		CxDocument_assignment.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ESpecification_allocation armEntity) throws SdaiException
	{
		CxDocument_assignment.unsetMappingConstraints(context, armEntity);
	}
	
	/**
	* Sets/creates data for Assigned_document_armx attribute.
	*
	* <p>
   <aa attribute="assigned_document" assertion_to="Document">
     <alt_map alt_map.inc="1">
       <aimelt>PATH</aimelt>
       <refpath>applied_document_reference &lt;=
document_reference
document_reference.assigned_document -&gt;
document &lt;-
{document.kind -&gt; document_type
document_type.product_data_type = 'configuration controlled document'}
document_product_association.relating_document
document_product_association
{document_product_association =&gt; document_product_equivalence}
document_product_association.related_product -&gt;
product_or_formation_or_definition
product_or_formation_or_definition = product
{product &lt;- product_related_product_category.products
product_related_product_category &lt;= product_category
product_category.name='document'}
       </refpath>
     </alt_map>
     <aa attribute="assigned_document" assertion_to="Document_version">
       <alt_map alt_map.inc="1">
       <aimelt>PATH</aimelt>
       <refpath>applied_document_reference &lt;=
document_reference
document_reference.assigned_document -&gt;
document &lt;-
{document.kind -&gt; document_type
document_type.product_data_type = 'configuration controlled document version'}
document_product_association.relating_document
document_product_association
{document_product_association =&gt; document_product_equivalence}
document_product_association.related_product -&gt;
product_or_formation_or_definition
product_or_formation_or_definition = product_definition_formation
{product_definition_formation.of_product -&gt; product
product &lt;- product_related_product_category.products
product_related_product_category &lt;= product_category
product_category.name='document'}
       </refpath>
     </alt_map>
  <aa attribute="assigned_document" assertion_to="Document_definition">
    <alt_map alt_map.inc="1">
       <aimelt>PATH</aimelt>
       <refpath>applied_document_reference &lt;=
document_reference
document_reference.assigned_document -&gt;
document &lt;-
{document.kind -&gt; document_type
document_type.product_data_type = 'configuration controlled document definition'}
document_product_association.relating_document
document_product_association
{document_product_association =&gt; document_product_equivalence}
document_product_association.related_product -&gt;
product_or_formation_or_definition
product_or_formation_or_definition = product_definition
{product_definition.frame_of_reference -&gt;
product_definition_context &lt;=
application_context_element
(application_context_element.name = 'physical document definition')
(application_context_element.name = 'digital document definition')}
{product_definition.formation -&gt; product_definition_formation
product_definition_formation.of_product -&gt; product
product &lt;- product_related_product_category.products
product_related_product_category &lt;= product_category
product_category.name='document'}
       </refpath>
     </alt_map>
     <aa attribute="assigned_document" assertion_to="File">
       <alt_map alt_map.inc="1">
         <aimelt>PATH</aimelt>
         <refpath>(applied_document_reference &lt;=
document_reference
document_reference.assigned_document -&gt;)
document
{document &lt;- document_representation_type.represented_document
(document_representation_type.name = 'digital')
(document_representation_type.name = 'physical')}
document =&gt; document_file 
       </refpath>
     </alt_map>
     
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 1) ADR -> D <- DPE.ing DPE DPE.ed -> P/PD/PDF
	// 4) ADR -> F
	public static void setAssigned_document_x(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		CxDocument_assignment.setAssigned_document_x(context, armEntity);
	}


	/**
	* Unsets/deletes data for basis attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx, delete AGAs
	public static void unsetAssigned_document_x(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		CxDocument_assignment.unsetAssigned_document_x(context, armEntity);		
	}

	/**
	* Sets/creates data for role_x attribute.
	*
	* <p>
   <aa attribute="role">
     <alt_map alt_map.inc="1">
       <aimelt>object_role.name</aimelt>
       <source>ISO 10303-41</source>
       <refpath>(applied_document_reference &lt;=
       document_reference
       document_reference.role -&gt;
       object_role
       object_role.name)
       </refpath>
     </alt_map>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 1) ADR -> D <- DPE.ing DPE DPE.ed -> P/PD/PDF
	// 4) ADR -> F
	public static void setRole_x(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		CxDocument_assignment.setRole_x(context, armEntity);		
	}


	/**
	* Unsets/deletes data for role_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx, delete AGAs
	public static void unsetRole_x(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		CxDocument_assignment.unsetRole_x(context, armEntity);
	}

	/**
	* Sets/creates data for role_x attribute.
	*
	* <p>
	attribute_mapping purpose(purpose, $PATH);
		applied_document_reference <=
		document_reference
		({document_reference.source = 'predefined purpose'}
		document_reference.role ->
		object_role
		object_role.name
		{object_role.name = 'identifier in target context'})
		(document_reference.role ->
		object_role
		object_role.name
		{!{
		(object_role.name = 'reference document')
		(object_role.name = 'base type dictionary')
		(object_role.name = 'product category reference document')
		(object_role.name = 'text identifier in document')}})
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// 1) ADR -> D <- DPE.ing DPE DPE.ed -> P/PD/PDF
/*	
	public static void setPurpose(SdaiContext context, ESpecification_allocation armEntity) throws SdaiException
	{
		//unset old values
		unsetPurpose(context, armEntity);

		if (armEntity.testPurpose(null))
		{

			String name = armEntity.getPurpose(null);
			LangUtils.Attribute_and_value_structure[] orStructure =
				{new LangUtils.Attribute_and_value_structure(
					CObject_role.attributeName(null),
					"purpose"),
				new LangUtils.Attribute_and_value_structure(
					CObject_role.attributeDescription(null),
					name)					
				};
			EObject_role eor = (EObject_role)
				LangUtils.createInstanceIfNeeded(context, CObject_role.definition, orStructure);
			// role_association
			ERole_association era = (ERole_association)
				context.working_model.createEntityInstance(CRole_association.definition);
			era.setRole(null, eor);
			era.setItem_with_role(null, armEntity);
		}
	}
*/

	/**
	* Unsets/deletes data for role_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> xxx, delete AGAs
/*	
	public static void unsetPurpose(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		ARole_association ara = new ARole_association();
		CRole_association.usedinItem_with_role(null, armEntity, context.domain, ara);
		SdaiIterator iter = ara.createIterator();
		while(iter.next()){
			ERole_association era = ara.getCurrentMember(iter);
			EObject_role role = era.getRole(null);
			if((role.testName(null))&&(role.getName(null).equals("purpose"))){
				era.deleteApplicationInstance();
			}
		}
	}
*/	
	

}
