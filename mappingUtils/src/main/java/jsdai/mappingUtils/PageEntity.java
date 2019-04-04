/*
 * PageEntity.java
 *
 * Created on Pirmadienis, 2001, Baland�io 2, 22.51
 */

package jsdai.mappingUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jsdai.SExtended_dictionary_schema.AExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAnd_constraint_relationship;
import jsdai.SMapping_schema.EAttribute_value_constraint;
import jsdai.SMapping_schema.EBoolean_constraint;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EInstance_constraint;
import jsdai.SMapping_schema.EInstance_equal;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.ENon_optional_constraint;
import jsdai.SMapping_schema.EOr_constraint_relationship;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

/**
 * @author Vaidas Narg�las
 */
public class PageEntity {

  /**
   * Holds value of property entityName.
   */
  private String entityName;

  /**
   * Holds value of property sessionModel.
   */
  private SessionModel sessionModel;

  /**
   * Holds value of ARM entity info.
   */
  private EntityInfo entityInfo;

  /**
   * Holds value of property out.
   */
  private javax.servlet.jsp.JspWriter out;

  /**
   * Creates new PageEntity
   */
  public PageEntity() {
  }

  /**
   * Getter for property modelName.
   *
   * @return Value of property modelName.
   */
  public String getEntityName() {
    return entityName;
  }

  /**
   * Setter for property entityName.
   *
   * @param entityName New value of property entityName.
   */
  public void setEntityName(String entityName) throws SdaiException {
    if (this.entityName == null || !this.entityName.equals(entityName)) {
      this.entityName = entityName;
      entityInfo = sessionModel.getEntityInfo(entityName);
    }
  }

  /**
   * Setter for property sessionModel.
   *
   * @param sessionModel New value of property sessionModel.
   */
  public void setSessionModel(SessionModel sessionModel) {
    this.sessionModel = sessionModel;
  }

  public List getMappings() {
    return entityInfo.mappings;
  }

  public String getMappingTargetString(EEntity_mapping mapping) throws SdaiException {
    EEntity entityOrAttribute = mapping.getTarget(null);
    if (entityOrAttribute instanceof EEntity_definition) {
      EEntity_definition entityDefinition = (EEntity_definition) entityOrAttribute;
      return "<B>" + entityDefinition.getName(null) + "</B> in " + entityDefinition.findEntityInstanceSdaiModel().getName();
    }
    else if (entityOrAttribute instanceof EAttribute) {
      EAttribute attribute = (EAttribute) entityOrAttribute;
      EEntity_definition entityDefinition = attribute.getParent_entity(null);
      return "<B>" + entityDefinition.getName(null) + "." + attribute.getName(null) + "</B> in "
          + entityDefinition.findEntityInstanceSdaiModel().getName();
    }
    else {
      return "";
    }
  }

  public List getAttributeMappingStrings(EEntity_mapping mapping) throws SdaiException {
    LinkedList attributeList = new LinkedList();
    getRecursiveAttributes(attributeList, entityInfo.entity, true);
    return attributeList;
  }

  private void getRecursiveAttributes(LinkedList attributeList, EEntity_definition entity, boolean top) throws SdaiException {
    String entityName = entity.getName(null);
    AEntity supertypes = entity.getGeneric_supertypes(null);
    SdaiIterator supertypeIterator = supertypes.createIterator();
    while (supertypeIterator.next()) {
      EEntity_definition supertype = (EEntity_definition) supertypes.getCurrentMemberObject(supertypeIterator);
      getRecursiveAttributes(attributeList, supertype, false);
    }
    AExplicit_attribute attributes = entity.getExplicit_attributes(null);
    SdaiIterator attributeIterator = attributes.createIterator();
    while (attributeIterator.next()) {
      EExplicit_attribute attribute = attributes.getCurrentMember(attributeIterator);
      attributeList.add(top ? attribute.getName(null) : entityName + "." + attribute.getName(null));
    }
  }

  /**
   * Setter for property out.
   *
   * @param out New value of property out.
   */
  public void setOut(javax.servlet.jsp.JspWriter out) {
    this.out = out;
  }

  void workIndent(EEntity e, int i) throws SdaiException, IOException {
    String s = e.getPersistentLabel();
    out.print(s);
    int length = s.length();

    while (length < 11) {
      out.print(" ");
      length++;
    }

    while (i > 0) {
      out.print("  ");
      i--;
    }
  }

  public void workEntityMapping(EEntity_mapping em, int indent) throws SdaiException, IOException {
    workIndent(em, indent);
    EEntity_definition source = em.getSource(null);
    out.print("EM:");
    out.print(source.getName(null));
    out.print(" -- ");
    if (em.testTarget(null)) {
      EEntity target = em.getTarget(null);
      if (target instanceof EEntity_definition) {

        EEntity_definition etarget = (EEntity_definition) target;
        out.print("E:");
        out.println(etarget.getName(null));
      }
      else if (target instanceof EAttribute) {
        EAttribute atarget = (EAttribute) target;
        out.print("A:");
        out.println(atarget.getName(null));
      }
      else {
        throw new SdaiException(SdaiException.SY_ERR, "entity_or_attribute missing");
      }
      if (em.testConstraints(null)) {
        workConstraint(em.getConstraints(null), indent + 1);
      }
      else {
        workIndent(em, indent + 1);
        out.println("-- no constraints --");
      }
    }
    else {
      out.println("$$$$$$$$");
    }
  }

  void workConstraint(EEntity ec, int indent) throws SdaiException, IOException {
    if (indent > 15) {
      out.println("!!! Endless loop !!!");
      return;
    }
    if (ec instanceof EInverse_attribute_constraint) {
      EInverse_attribute_constraint e = (EInverse_attribute_constraint) ec;
      workIndent(ec, indent);
      out.println("inverse_attribute_constraint <- ");
      workConstraint(e.getInverted_attribute(null), indent + 1);
    }
    else if (ec instanceof EAttribute) {
      EAttribute a = (EAttribute) ec;
      EEntity_definition parent = a.getParent_entity(null);
      workIndent(ec, indent);
      out.print("A:");
      out.print(parent.getName(null));
      out.print(".");
      out.println(a.getName(null));
    }
    else if (ec instanceof ESelect_constraint) {
      out.print("select_constraint ");
    }
    else if (ec instanceof EAttribute_value_constraint) {
      workIndent(ec, indent);
      if (ec instanceof EBoolean_constraint) {
        EBoolean_constraint e = (EBoolean_constraint) ec;
        out.print("boolean_constraint == ");
        out.println(e.getConstraint_value(null) ? ".TRUE." : ".FALSE.");
      }
      else if (ec instanceof EInteger_constraint) {
        EInteger_constraint e = (EInteger_constraint) ec;
        out.print("integer_constraint == ");
        out.println(e.getConstraint_value(null));
      }
      else if (ec instanceof ENon_optional_constraint) {
        out.println("non_optional_constraint ");
      }
      else if (ec instanceof EString_constraint) {
        EString_constraint e = (EString_constraint) ec;
        out.print("string_constraint == '");
        out.print(e.getConstraint_value(null));
        out.println("'");
      }
      else if (ec instanceof EReal_constraint) {
        EReal_constraint e = (EReal_constraint) ec;
        out.print("real_constraint == ");
        out.println(e.getConstraint_value(null));
      }
      else if (ec instanceof EEnumeration_constraint) {
        EEnumeration_constraint e = (EEnumeration_constraint) ec;
        out.print("enumeration_constraint == ");
        out.println(e.getConstraint_value(null));
      }
      else if (ec instanceof ELogical_constraint) {
        ELogical_constraint e = (ELogical_constraint) ec;
        out.print("logical_constraint == ");
        out.println(ELogical.toString(e.getConstraint_value(null)));
      }
      else {
        throw new SdaiException(SdaiException.SY_ERR, "attribute_valid_constraint should be abstract");
      }
      workConstraint(((EAttribute_value_constraint) ec).getAttribute(null), indent + 1);
    }
    else if (ec instanceof EAggregate_member_constraint) {
      EAggregate_member_constraint e = (EAggregate_member_constraint) ec;
      workIndent(ec, indent);
      out.print("aggregate_member_constraint == ");
      if (e.testMember(null)) {
        out.println(e.getMember(null));
      }
      else {
        out.println("$");
      }
      workConstraint(e.getAttribute(null), indent + 1);
    }
    else if (ec instanceof EEntity_constraint) {
      EEntity_constraint e = (EEntity_constraint) ec;
      workIndent(ec, indent);
      out.print("entity_constraint == ");
      out.println(e.getDomain(null).getName(null));
      workConstraint(e.getAttribute(null), indent + 1);
    }
    else if (ec instanceof EPath_constraint) {
      EPath_constraint e = (EPath_constraint) ec;
      workIndent(ec, indent);
      out.println("path_constraint ->");
      workConstraint(e.getElement1(null), indent + 1);
      workConstraint(e.getElement2(null), indent + 1);
    }
    else if (ec instanceof EInstance_constraint) {
      EInstance_constraint e = (EInstance_constraint) ec;
      workIndent(ec, indent);
      if (ec instanceof EAnd_constraint_relationship) {
        out.println("and_constraint_relationship");
      }
      else if (ec instanceof EOr_constraint_relationship) {
        out.println("or_constraint_relationship");
      }
      else if (ec instanceof EInstance_equal) {
        out.println("instance_equal");
      }
      else {
        throw new SdaiException(SdaiException.SY_ERR, "unknown type in instance_constraint");
      }
      workConstraint(e.getElement1(null), indent + 1);
      workConstraint(e.getElement2(null), indent + 1);
    }
    else {
      out.println("Other - " + ec);
      //throw new SdaiException(SdaiException.SY_ERR, "unknown type");
    }
  }

}
