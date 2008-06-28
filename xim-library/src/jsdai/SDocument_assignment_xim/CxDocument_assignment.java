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

package jsdai.SDocument_assignment_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SBasic_attribute_schema.*;
import jsdai.SDocument_and_version_identification_xim.*;
import jsdai.SDocument_assignment_mim.*;
import jsdai.SDocument_definition_xim.*;
import jsdai.SDocument_schema.*;
import jsdai.SFile_identification_xim.EFile;
import jsdai.SManagement_resources_schema.EDocument_reference;
import jsdai.SProduct_definition_schema.EProduct;
import jsdai.SProduct_definition_schema.EProduct_definition_formation;

/**
* @author Giedrius Liutkus
* @version $Revision$
*/

public class CxDocument_assignment extends CDocument_assignment implements EMappedXIMEntity
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
		a0 = set_instance(a0, value);
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

	      // Clean ARM
			// assigned_document_armx
			unsetAssigned_document_x(null);

			// role_x
			unsetRole_x(null);
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
	public static void setMappingConstraints(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
//	      if(!armEntity.testSource(null))
	    	  armEntity.setSource(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
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
		//unset old values
		unsetAssigned_document_x(context, armEntity);

		if (armEntity.testAssigned_document_x(null))
		{

			EEntity ee = armEntity.getAssigned_document_x(null);
			String name;
			String docID;
			if(ee instanceof EFile){
				armEntity.setAssigned_document(null, (EFile)ee);
				return;
			}
			else if(ee instanceof EDocument_definition){
				name = "configuration controlled document definition";
				EDocument_definition edd = (EDocument_definition)ee;
				EProduct_definition_formation version = edd.getFormation(null);
				EProduct product = version.getOf_product(null);
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
				if(version.testId(null)){
					docID += "-"+version.getId(null);
				}
				if(edd.testId(null)){
					docID += "-"+edd.getId(null);
				}
			}
			else if(ee instanceof EDocument_version){
				name = "configuration controlled document version";
				EProduct_definition_formation version = (EProduct_definition_formation)ee;
				EProduct product = version.getOf_product(null);
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
				if(version.testId(null)){
					docID += "-"+version.getId(null);
				}
			}
			else if(ee instanceof EDocument_armx){
				name = "configuration controlled document";
				EDocument_armx product = (EDocument_armx)ee;
				docID = new String();
				if(product.testId(null)){
					docID += product.getId(null);
				}
			}
			else
				throw new SdaiException(SdaiException.EI_NVLD, " This kind of target for assignment is not supported "+ee);
			// Strategy - find any suitable kind of document_type, than create document
			// document_type
			LangUtils.Attribute_and_value_structure[] dtStructure =
				{new LangUtils.Attribute_and_value_structure(
					CDocument_type.attributeProduct_data_type(null),
					name)
				};
			EDocument_type type = (EDocument_type)
				LangUtils.createInstanceIfNeeded(context, CDocument_type.definition, dtStructure);
			// Document - it must be unique as definition of it - equivalence
			LangUtils.Attribute_and_value_structure[] dStructure =
			{new LangUtils.Attribute_and_value_structure(
				CDocument.attributeKind(null),
				type),
			new LangUtils.Attribute_and_value_structure(
				CDocument.attributeId(null),
				docID)				
			};
			EDocument document = (EDocument)
				LangUtils.createInstanceIfNeeded(context, CDocument.definition, dStructure);
			if(!document.testName(null))
				document.setName(null, "");
			// DPE
			LangUtils.Attribute_and_value_structure[] dpeStructure =
			{new LangUtils.Attribute_and_value_structure(
				CDocument_product_equivalence.attributeRelated_product(null),
				ee),
			new LangUtils.Attribute_and_value_structure(
				CDocument_product_equivalence.attributeRelating_document(null),
				document)				
			};
			EDocument_product_equivalence edpe = (EDocument_product_equivalence)
				LangUtils.createInstanceIfNeeded(context, CDocument_product_equivalence.definition, dpeStructure);
			if(!edpe.testName(null)){
				// According WR1
				edpe.setName(null, "equivalence");
			}
			armEntity.setAssigned_document(null, document);
		}
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
		// xxx
		armEntity.unsetAssigned_document(null);
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
		//unset old values
		unsetRole_x(context, armEntity);

		if (armEntity.testRole_x(null))
		{

			String name = armEntity.getRole_x(null);
			LangUtils.Attribute_and_value_structure[] orStructure =
				{new LangUtils.Attribute_and_value_structure(
					CObject_role.attributeName(null),
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


	/**
	* Unsets/deletes data for role_x attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRole_x(SdaiContext context, EDocument_assignment armEntity) throws SdaiException
	{
		ARole_association ara = new ARole_association();
		CRole_association.usedinItem_with_role(null, armEntity, context.domain, ara);
		SdaiIterator iter = ara.createIterator();
		while(iter.next()){
			ERole_association era = ara.getCurrentMember(iter);
			era.deleteApplicationInstance();
		}
	}
	

}
