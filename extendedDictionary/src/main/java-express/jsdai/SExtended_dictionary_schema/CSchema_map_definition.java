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

// Java class implementing entity schema_map_definition

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CSchema_map_definition extends CGeneric_schema_definition implements ESchema_map_definition {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSchema_map_definition.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	// name: protected String a0;   name - java inheritance - STRING
	// identification: protected String a1;   identification - java inheritance - STRING
	// view_declarations: protected Object  - inverse - current -  ENTITY view_declaration
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
	// map_declarations: protected Object  - inverse - current -  ENTITY map_declaration
	protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
	// source_schema_specifications: protected Object  - inverse - current -  ENTITY reference_from_specification_as_source
	protected static final jsdai.dictionary.CInverse_attribute i2$ = CEntity.initInverseAttribute(definition, 2);
	// target_schema_specifications: protected Object  - inverse - current -  ENTITY reference_from_specification_as_target
	protected static final jsdai.dictionary.CInverse_attribute i3$ = CEntity.initInverseAttribute(definition, 3);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // name - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  // protected String a0;
  // identification - explicit - java inheritance
  // protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  // protected String a1;
  // view_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);
  // map_declarations - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i1$ = CEntity.initInverseAttribute(definition, 1);
  // source_schema_specifications - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i2$ = CEntity.initInverseAttribute(definition, 2);
  // target_schema_specifications - inverse - current entity
  protected static final jsdai.dictionary.CInverse_attribute i3$ = CEntity.initInverseAttribute(definition, 3);

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    super.changeReferences(old, newer);
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1684=EXPLICIT_ATTRIBUTE('name',#1682,0,#1522,$,.F.);
  //<01> generating methods for consolidated attribute:  name
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1685=EXPLICIT_ATTRIBUTE('identification',#1682,1,#1524,$,.T.);
  //<01> generating methods for consolidated attribute:  identification
  //<01-1> supertype, java inheritance
  //<01-1-0> explicit - generateExplicitSupertypeJavaInheritedMethodsX()
  //going through all the attributes: #1802=INVERSE_ATTRIBUTE('view_declarations',#1800,0,#1867,$,#1869,#2237,$,.F.);
  //<01> generating methods for consolidated attribute:  view_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - view_declarations : SET[0:-2147483648] OF view_declaration FOR parent
  public AView_declaration getView_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
    AView_declaration result = (AView_declaration) get_inverse_aggregate(i0$);
    CView_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeView_declarations(ESchema_map_definition type) throws SdaiException {
    return i0$;
  }

  //going through all the attributes: #1803=INVERSE_ATTRIBUTE('map_declarations',#1800,1,#1724,$,#1726,#2239,$,.F.);
  //<01> generating methods for consolidated attribute:  map_declarations
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - map_declarations : SET[0:-2147483648] OF map_declaration FOR parent
  public AMap_declaration getMap_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
    AMap_declaration result = (AMap_declaration) get_inverse_aggregate(i1$);
    CMap_declaration.usedinParent(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeMap_declarations(ESchema_map_definition type) throws SdaiException {
    return i1$;
  }

  //going through all the attributes: #1804=INVERSE_ATTRIBUTE('source_schema_specifications',#1800,2,#1787,$,#1785,#2241,$,.F.);
  //<01> generating methods for consolidated attribute:  source_schema_specifications
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - source_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_source FOR current_schema
  public AReference_from_specification_as_source getSource_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
    AReference_from_specification_as_source result = (AReference_from_specification_as_source) get_inverse_aggregate(i2$);
    CReference_from_specification_as_source.usedinCurrent_schema(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeSource_schema_specifications(ESchema_map_definition type) throws SdaiException {
    return i2$;
  }

  //going through all the attributes: #1805=INVERSE_ATTRIBUTE('target_schema_specifications',#1800,3,#1789,$,#1785,#2243,$,.F.);
  //<01> generating methods for consolidated attribute:  target_schema_specifications
  //<01-0> current entity
  //<01-0-2> inverse attribute - generateInverseCurrentEntityMethodsX()
  // Inverse attribute - target_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_target FOR current_schema
  public AReference_from_specification_as_target getTarget_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException {
    AReference_from_specification_as_target result = (AReference_from_specification_as_target) get_inverse_aggregate(i3$);
    CReference_from_specification_as_target.usedinCurrent_schema(null, this, domain, result);
    return result;
  }

  public static jsdai.dictionary.EAttribute attributeTarget_schema_specifications(ESchema_map_definition type) throws SdaiException {
    return i3$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getString(1);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      a1 = null;
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = av.entityValues[0].getString(1);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: generic_schema_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setString(1, a1);
		// partial entity: schema_map_definition
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: generic_schema_definition
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setString(1, a1);
    // partial entity: schema_map_definition
  }
}
