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

// Java class implementing entity uniqueness_rule

package jsdai.SExtended_dictionary_schema;

import jsdai.lang.*;

public class CUniqueness_rule extends CEntity implements EUniqueness_rule {
  public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CUniqueness_rule.class, SExtended_dictionary_schema.ss);

  /*----------------------------- Attributes -----------*/

/*
	protected String a0; // label - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected AAttribute a1; // attributes - current entity - LIST OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected Object a2; // parent_entity - current entity - ENTITY entity_definition
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
*/

  /*----------------------------- Attributes (new version) -----------*/

  // label - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
  protected String a0;
  // attributes - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
  protected AAttribute a1;
  // parent_entity - explicit - current entity
  protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
  protected Object a2;

  public jsdai.dictionary.EEntity_definition getInstanceType() {
    return definition;
  }

/* *** old implementation ***

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a1, old, newer);
		if (a2 == old) {
			a2 = newer;
		}
	}
*/

  protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
    changeReferencesAggregate(a1, old, newer);
    if (a2 == old) {
      a2 = newer;
    }
  }

  /*----------- Methods for attribute access -----------*/


  /*----------- Methods for attribute access (new)-----------*/

  //going through all the attributes: #1850=EXPLICIT_ATTRIBUTE('label',#1848,0,#1522,$,.T.);
  //<01> generating methods for consolidated attribute:  label
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  /// methods for attribute: label, base type: STRING
  public boolean testLabel(EUniqueness_rule type) throws SdaiException {
    return test_string(a0);
  }

  public String getLabel(EUniqueness_rule type) throws SdaiException {
    return get_string(a0);
  }

  public void setLabel(EUniqueness_rule type, String value) throws SdaiException {
    a0 = set_string(value);
  }

  public void unsetLabel(EUniqueness_rule type) throws SdaiException {
    a0 = unset_string();
  }

  public static jsdai.dictionary.EAttribute attributeLabel(EUniqueness_rule type) throws SdaiException {
    return a0$;
  }

  //going through all the attributes: #1851=EXPLICIT_ATTRIBUTE('attributes',#1848,1,#2082,$,.F.);
  //<01> generating methods for consolidated attribute:  attributes
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // methods for attribute: attributes, base type: LIST OF ENTITY
  public static int usedinAttributes(EUniqueness_rule type, EAttribute instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a1$, domain, result);
  }

  public boolean testAttributes(EUniqueness_rule type) throws SdaiException {
    return test_aggregate(a1);
  }

  public AAttribute getAttributes(EUniqueness_rule type) throws SdaiException {
    return (AAttribute) get_aggregate(a1);
  }

  public AAttribute createAttributes(EUniqueness_rule type) throws SdaiException {
    a1 = (AAttribute) create_aggregate_class(a1, a1$, AAttribute.class, 0);
    return a1;
  }

  public void unsetAttributes(EUniqueness_rule type) throws SdaiException {
    unset_aggregate(a1);
    a1 = null;
  }

  public static jsdai.dictionary.EAttribute attributeAttributes(EUniqueness_rule type) throws SdaiException {
    return a1$;
  }

  //going through all the attributes: #1852=EXPLICIT_ATTRIBUTE('parent_entity',#1848,2,#1633,$,.F.);
  //<01> generating methods for consolidated attribute:  parent_entity
  //<01-0> current entity
  //<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
  // attribute (current explicit or supertype explicit) : parent_entity, base type: entity entity_definition
  public static int usedinParent_entity(EUniqueness_rule type, EEntity_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
    return ((CEntity) instance).makeUsedin(definition, a2$, domain, result);
  }

  public boolean testParent_entity(EUniqueness_rule type) throws SdaiException {
    return test_instance(a2);
  }

  public EEntity_definition getParent_entity(EUniqueness_rule type) throws SdaiException {
    return (EEntity_definition) get_instance(a2);
  }

  public void setParent_entity(EUniqueness_rule type, EEntity_definition value) throws SdaiException {
    a2 = set_instance(a2, value);
  }

  public void unsetParent_entity(EUniqueness_rule type) throws SdaiException {
    a2 = unset_instance(a2);
  }

  public static jsdai.dictionary.EAttribute attributeParent_entity(EUniqueness_rule type) throws SdaiException {
    return a2$;
  }


  /*---------------------- setAll() --------------------*/

/* *** old implementation ***
	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = (AAttribute)av.entityValues[0].getInstanceAggregate(1, a1$, this);
		a2 = av.entityValues[0].getInstance(2, this, a2$);
	}
*/

  protected void setAll(ComplexEntityValue av) throws SdaiException {
    if (av == null) {
      a0 = null;
      if (a1 instanceof CAggregate) {
        a1.unsetAll();
      }
      a1 = null;
      a2 = unset_instance(a2);
      return;
    }
    a0 = av.entityValues[0].getString(0);
    a1 = (AAttribute) av.entityValues[0].getInstanceAggregate(1, a1$, this);
    a2 = av.entityValues[0].getInstance(2, this, a2$);
  }

  /*---------------------- getAll() --------------------*/

/* *** old implementation ***
	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: uniqueness_rule
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
		av.entityValues[0].setInstance(2, a2);
	}
*/

  protected void getAll(ComplexEntityValue av) throws SdaiException {
    // partial entity: uniqueness_rule
    av.entityValues[0].setString(0, a0);
    av.entityValues[0].setInstanceAggregate(1, a1);
    av.entityValues[0].setInstance(2, a2);
  }
}
