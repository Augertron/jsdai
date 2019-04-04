package jsdai.tools;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EAttribute_value_constraint;
import jsdai.SMapping_schema.EConstraint_attribute;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EInstance_constraint;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

public class MappingDocOperations {
  public static AEntity fullPath = null;
  public static EEntity currentPathElement = null;

  public static EEntity_definition lastRefEntityDef = null;

  public MappingDocOperations() {
  }

  public static boolean isElementInPath(EEntity element) throws SdaiException {
    if (fullPath == null) {
      return false;
    }
    if (currentPathElement == element) {
      return false;
    }

    SdaiIterator it_path = fullPath.createIterator();
    while (it_path.next()) {
      if (element == fullPath.getCurrentMemberObject(it_path)) {
        return true;
      }
    }

    if (element instanceof EPath_constraint) {
      EPath_constraint pc = (EPath_constraint) element;
      if ((pc.testElement1(null)) && (pc.testElement2(null))) {
        return isElementInPath(pc.getElement1(null)) && isElementInPath(pc.getElement2(null));
      }
      else {
        System.out.println("PROBLEM: Path_constraint element not set!" + pc);
        return false;
      }
    }
    else if (element instanceof EInstance_constraint) {
      EInstance_constraint ic = (EInstance_constraint) element;
      if ((ic.testElement1(null)) && (ic.testElement2(null))) {
        return isElementInPath(ic.getElement1(null)) && isElementInPath(ic.getElement2(null));
      }
      else {
        System.out.println("PROBLEM: Instance_constraint element not set!" + ic);
        return false;
      }
    }
    return false;
  }

  public static String printSuperTypes(Object base, Object derived) throws SdaiException {
    String result = "";
    String s = "";
    if ((base instanceof EEntity_definition) && (derived instanceof EEntity_definition)) {
      EEntity_definition entities[] = null;
      if (isSuperType((EEntity_definition) base, (EEntity_definition) derived)) {
        entities = getInheritancePath(base, derived);
        s = "=>";
      }
      else if (isSuperType((EEntity_definition) derived, (EEntity_definition) base)) {
        entities = getInheritancePath(derived, base);
        s = "<=";
      }
      if (entities != null) {
        boolean first = true;
        int i = 0;
        while (i < entities.length) {
          if (!first) {
            result += MappingDocUtils.println(s);
            result += ((ENamed_type) entities[i]).getName(null);
          }
          else {
            result += ((ENamed_type) entities[i]).getName(null);
            first = false;
          }
          i++;
        }
        result += MappingDocUtils.println("");
      }
    }
    else {
//			System.out.println("PROBLEM: don't know how to respond to request to print supertypes!");
      //		System.out.println("base = "+base+" derived = "+derived);
    }
    return result;
  }

  public static EEntity getBaseType(Object type) throws SdaiException {
    EEntity result = null;
    if ((type instanceof ENamed_type) || (type instanceof ESimple_type)) {
      result = (EEntity) type;
    }
    else if (type instanceof EAggregation_type) {
      EAggregation_type eat = (EAggregation_type) type;
      boolean fa = true;
      while (fa) {
        fa = false;
        type = eat.getElement_type(null);
        if ((type instanceof ENamed_type) || (type instanceof ESimple_type)) {
          result = (EEntity) type;
        }
        else if (type instanceof EAggregation_type) {
          eat = ((EAggregation_type) type);
          fa = true;
        }
      }
    }
    return result;
  }

  public static EEntity_definition[] getInheritancePath(Object base, Object derived) throws SdaiException {
    EEntity_definition inheritancePath[] = null;
    boolean a = false;
    AEntity entities = ((EEntity_definition) derived).getGeneric_supertypes(null);
    SdaiIterator it_entities = entities.createIterator();
    while ((it_entities.next()) && !(a)) {
      EEntity_definition entity = (EEntity_definition) entities.getCurrentMemberObject(it_entities);
      if (entity == base) {
        a = true;
        inheritancePath = new EEntity_definition[2];
        inheritancePath[0] = (EEntity_definition) derived;
        inheritancePath[1] = entity;
      }
      else {
        inheritancePath = getInheritancePath(base, entity);
        if (inheritancePath != null) {
          EEntity_definition temp[] = new EEntity_definition[inheritancePath.length + 1];
          System.arraycopy(inheritancePath, 0, temp, 1, inheritancePath.length);
          temp[0] = (EEntity_definition) derived;
          inheritancePath = temp;
        }
      }
    }
    return inheritancePath;
  }

  /*
   * ******************************************************************
   * isSuperType methods
   * ******************************************************************
   */
  public static boolean isSuperType(EAttribute base, EAttribute derived) throws SdaiException {
    boolean a = false;
    if ((base instanceof EExplicit_attribute) && (derived instanceof EExplicit_attribute)) {
      a = isSuperType((EExplicit_attribute) base, (EExplicit_attribute) derived);
    }
    else if ((base instanceof EConstraint_attribute) && (derived instanceof EConstraint_attribute)) {
      System.out.println("PROBLEM: 1st case. Don't know how to respond: isSuperType(EAttribute, EAttribute)");
    }
    else {
      System.out.println("PROBLEM: 2nd case. Don't know how to respond: isSuperType(EAttribute, EAttribute)");
    }
    return a;
  }

  public static boolean isSuperType(EExplicit_attribute base, EExplicit_attribute derived) throws SdaiException {
    boolean a = false;
    if ((getBaseType(base.getDomain(null)) instanceof ENamed_type) && (getBaseType(derived.getDomain(null)) instanceof ENamed_type)) {
      a = isSuperType((ENamed_type) base.getDomain(null), (ENamed_type) derived.getDomain(null));
    }
    else if ((getBaseType(base.getDomain(null)) instanceof ESimple_type) && (getBaseType(derived.getDomain(null)) instanceof ESimple_type)) {
      System.out.println("PROBLEM: 1st case. isSupertype(EExplicit_attribute, EExplicit_attribute)");
    }
    else {
      System.out.println("PROBLEM: 2nd case. isSupertype(EExplicit_attribute, EExplicit_attribute)");
    }
    return a;
  }

  public static boolean isSuperType(ENamed_type base, ENamed_type derived) throws SdaiException {
    boolean a = false;
    if ((base instanceof EEntity_definition) && (derived instanceof EEntity_definition)) {
      a = isSuperType((EEntity_definition) base, (EEntity_definition) derived);
    }
    else {
      System.out.println("PROBLEM: isSupertype(ENamed_type, ENamed_type)");
    }
    return a;
  }

  public static boolean isSuperType(EEntity_definition base, EEntity_definition derived) throws SdaiException {
    boolean a = false;
    AEntity entities = derived.getGeneric_supertypes(null);
    SdaiIterator it_entities = entities.createIterator();
    while ((it_entities.next()) && !(a)) {
      if (entities.getCurrentMemberObject(it_entities) == base) {
        a = true;
      }
      else {
        a = isSuperType(base, (EEntity_definition) entities.getCurrentMemberObject(it_entities));
      }
    }
    return a;
  }

  /*
   * ******************************************************************
   * isSelectInside methods
   * ******************************************************************
   */

  public static boolean isSelectInside(EEntity type) throws SdaiException {
    if (type instanceof EEntity_constraint) {
      return isSelectInside((EEntity_constraint) type);
    }
    if (type instanceof EPath_constraint) {
      return isSelectInside((EPath_constraint) type);
    }
    if (type instanceof EInstance_constraint) {
      return isSelectInside((EInstance_constraint) type);
    }
    if (type instanceof EAttribute_value_constraint) {
      return isSelectInside((EAttribute_value_constraint) type);
    }
    if (type instanceof ESelect_constraint) {
      return isSelectInside((ESelect_constraint) type);
    }
    if (type instanceof EAggregate_member_constraint) {
      return isSelectInside((EAggregate_member_constraint) type);
    }
    if (type instanceof EInverse_attribute_constraint) {
      return isSelectInside((EInverse_attribute_constraint) type);
    }
    if (type instanceof EAttribute) {
      return isSelectInside((EAttribute) type);
    }
    if (type instanceof EExplicit_attribute) {
      return isSelectInside((EExplicit_attribute) type);
    }
    if (type instanceof ESimple_type) {
      return false;
    }
    if (type instanceof ESelect_type) {
      return true;
    }
    if (type instanceof EEnumeration_type) {
      return false;
    }
    if (type instanceof EEntity_definition) {
      return false;
    }
    if (type instanceof ENamed_type) {
      return isSelectInside((ENamed_type) type);
    }
    if (type instanceof EAggregation_type) {
      return isSelectInside((EAggregation_type) type);
    }

    System.out.println("PROBLEM: unknown type passed to isSelectInside(EEntity)");
    System.out.println("type is " + type);
    System.out.println("its class is " + type.getClass());
    return false;
  }

  private static boolean isSelectInside(EAttribute type) throws SdaiException {
    boolean rv = false;
    EEntity domain = null;
    if (type instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) type;
      if (eAttr.testDomain(null)) {
        domain = eAttr.getDomain(null);
        rv = isSelectInside(domain);
      }
      else {
        System.out.println("PROBLEM: 1st case:domain not set. isSelectInside(EAttribute)");
      }
    }
    else if (type instanceof EDerived_attribute) {
      EDerived_attribute dAttr = (EDerived_attribute) type;
      if (dAttr.testDomain(null)) {
        domain = dAttr.getDomain(null);
        rv = isSelectInside(domain);
      }
      else {
        System.out.println("PROBLEM: 2nd case:domain not set. isSelectInside(EAttribute)");
      }
    }
    else if (type instanceof EInverse_attribute) {
      EInverse_attribute iAttr = (EInverse_attribute) type;
      if (iAttr.testInverted_attr(null)) {
        EExplicit_attribute expAttr = iAttr.getInverted_attr(null);
        rv = isSelectInside(expAttr);
      }
      else {
        System.out.println("PROBLEM: 3rd case: inverse not set. isSelectInside(EAttribute)");
      }
    }
    else {
      System.out.println("PROBLEM: unknown case. isSelectInside(EAttribute)");
    }

    return rv;
  }

  private static boolean isSelectInside(EExplicit_attribute attr) throws SdaiException {
    boolean a = false;
    EEntity domain = attr.getDomain(null);
    if (domain instanceof ESimple_type) {
      a = false;
    }
    else {
      a = isSelectInside(domain);
    }
    return a;
  }

  private static boolean isSelectInside(EAggregation_type at) throws SdaiException {
    boolean rv = false;
    if (at.testElement_type(null)) {
      EEntity type = at.getElement_type(null);
      rv = isSelectInside(type);
    }
    else {
      System.out.println("PROBLEM: element_type not set. isSelectInside(EAggregation_type)");
    }
    return rv;
  }

  private static boolean isSelectInside(ENamed_type nt) throws SdaiException {
    boolean rv = false;
    if (nt instanceof EDefined_type) {
      EDefined_type dt = (EDefined_type) nt;
      if (dt.testDomain(null)) {
        EEntity type = dt.getDomain(null);
        rv = isSelectInside(type);
      }
      else {
        System.out.println("PROBLEM:  1st case: domain not set. isSelectInside(ENamed_type)");
      }
    }
    else if (nt instanceof EEntity_definition) {
      rv = false;
    }
    else {
      System.out.println("PROBLEM: unknown type. isSelectInside(ENamed_type)");
    }

    return rv;
  }

  private static boolean isSelectInside(EInverse_attribute_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testInverted_attribute(null)) {
      System.out.println("EIAC: inverted attribute not set!");
      return false;
    }
    EEntity domain = constraint.getInverted_attribute(null);
    a = isSelectInside(domain);
    return a;
  }

  private static boolean isSelectInside(EEntity_constraint constraint) throws SdaiException {
    boolean a = false;
    EEntity attribute = constraint.getAttribute(null);
    if (attribute instanceof EAttribute) {
      a = isSelectInside((EAttribute) attribute);
    }
    else if (attribute instanceof EInverse_attribute_constraint) {
      a = isSelectInside((EInverse_attribute_constraint) attribute);
    }
    else if (attribute instanceof EAggregate_member_constraint) {
      a = isSelectInside((EAggregate_member_constraint) attribute);
    }
    else {
      System.out.println("EEC: unknown attribute type!");
    }
    return a;
  }

  private static boolean isSelectInside(EAggregate_member_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testAttribute(null)) {
      System.out.println("EAMC: Attribute not set!");
      return false;
    }
    EEntity domain = constraint.getAttribute(null);
    a = isSelectInside(domain);

    return a;
  }

  private static boolean isSelectInside(ESelect_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testAttribute(null)) {
      System.out.println("ESC: Attribute not set!");
    }
    EEntity attribute = constraint.getAttribute(null);
    a = isSelectInside(attribute);
    return a;
  }

  private static boolean isSelectInside(EAttribute_value_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testAttribute(null)) {
      System.out.println("EAV: Attribute not set!");
    }
    EEntity attribute = constraint.getAttribute(null);
    a = isSelectInside(attribute);
    return a;
  }

  private static boolean isSelectInside(EInstance_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testElement1(null)) {
      System.out.println("EIC: Element1 not set!");
    }
    EEntity attribute = constraint.getElement1(null);
    a = isSelectInside(attribute);
    return a;
  }

  private static boolean isSelectInside(EPath_constraint constraint) throws SdaiException {
    boolean a = false;
    if (!constraint.testElement1(null)) {
      System.out.println("EPC: Element1 not set!");
    }
    EEntity attribute = constraint.getElement1(null);
    a = isSelectInside(attribute);
    return a;
  }

  /*
   * ******************************************************************
   * getStartType methods
   * ******************************************************************
   */
  public static Object getStartType(EEntity ae) throws SdaiException {
    Object a = null;
    if (ae instanceof EAttribute) {
      a = getStartType((EAttribute) ae);
    }
    else if (ae instanceof EInverse_attribute_constraint) {
      a = getStartType((EInverse_attribute_constraint) ae);
    }
    else if (ae instanceof EInstance_constraint) {
      a = getStartType((EInstance_constraint) ae);
    }
    else if (ae instanceof EPath_constraint) {
      a = getStartType((EPath_constraint) ae);
    }
    else if (ae instanceof EAttribute_value_constraint) {
      a = getStartType((EAttribute_value_constraint) ae);
    }
    else if (ae instanceof ESelect_constraint) {
      a = getStartType((ESelect_constraint) ae);
    }
    else if (ae instanceof EEntity_constraint) {
      a = getStartType((EEntity_constraint) ae);
    }
    else if (ae instanceof EAggregate_member_constraint) {
      a = getStartType((EAggregate_member_constraint) ae);
    }
    else {
      System.out.println("PROBLEM: unknown entity type. getStartType(EEntity)");
    }
    return a;
  }

  public static Object getStartType(EAttribute pe) throws SdaiException {
    return pe.getParent(null);
  }

  public static Object getStartType(EInverse_attribute_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testInverted_attribute(null)) {
      EEntity att = pe.getInverted_attribute(null);
      if (att instanceof EAttribute) {
        a = getEndType((EAttribute) att);
      }
      else if (att instanceof EAggregate_member_constraint) {
        a = getEndType((EAggregate_member_constraint) att);
      }
      else if (att instanceof EEntity_constraint) {
        a = getEndType((EEntity_constraint) att);
      }
      else if (att instanceof ESelect_constraint) {
        a = getEndType((ESelect_constraint) att);
      }
      else {
        System.out.println("PROBLEM: getStartType for inverse_attribute_constraint");
        System.out.println("encountered unknown constraint subtype:" + att);
      }
    }
    return a;
  }

  public static Object getStartType(EPath_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testElement1(null)) {
      a = getStartType(pe.getElement1(null));
    }
    return a;
  }

  public static Object getStartType(EInstance_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testElement1(null)) {
      a = getStartType(pe.getElement1(null));
    }
    return a;
  }

  public static Object getStartType(EAttribute_value_constraint pe) throws SdaiException {
    return getStartType(pe.getAttribute(null));
  }

  public static Object getStartType(ESelect_constraint pe) throws SdaiException {
    return getStartType(pe.getAttribute(null));
  }

  public static Object getStartType(EEntity_constraint pe) throws SdaiException {
    Object a = null;
    a = getStartType(pe.getAttribute(null));
    return a;
  }

  public static Object getStartType(EAggregate_member_constraint pe) throws SdaiException {
    return getStartType(pe.getAttribute(null));
  }

  /*
   * ******************************************************************
   * getEndType methods
   * ******************************************************************
   */
  public static Object getEndType(EEntity ae) throws SdaiException {
    Object a = null;
    if (ae instanceof EAttribute) {
      a = getEndType((EAttribute) ae);
    }
    else if (ae instanceof EInverse_attribute_constraint) {
      a = getEndType((EInverse_attribute_constraint) ae);
    }
    else if (ae instanceof EPath_constraint) {
      a = getEndType((EPath_constraint) ae);
    }
    else if (ae instanceof EInstance_constraint) {
      a = getEndType((EInstance_constraint) ae);
    }
    else if (ae instanceof EAttribute_value_constraint) {
      a = getEndType((EAttribute_value_constraint) ae);
    }
    else if (ae instanceof ESelect_constraint) {
      a = getEndType((ESelect_constraint) ae);
    }
    else if (ae instanceof EEntity_constraint) {
      a = getEndType((EEntity_constraint) ae);
    }
    else if (ae instanceof EAggregate_member_constraint) {
      a = getEndType((EAggregate_member_constraint) ae);
    }
    return a;
  }

  public static Object getEndType(EAttribute pe) throws SdaiException {
    Object result = null;
    if (pe instanceof EExplicit_attribute) {
      result = ((EExplicit_attribute) pe).getDomain(null);
    }
    else if (pe instanceof EInverse_attribute) {
      result = ((EInverse_attribute) pe).getDomain(null);
    }
    else if (pe instanceof EDerived_attribute) {
      result = ((EDerived_attribute) pe).getDomain(null);
    }
    return result;
  }

  public static Object getEndType(EInverse_attribute_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testInverted_attribute(null)) {
      EEntity att = pe.getInverted_attribute(null);
      a = getStartType(att);
    }
    return a;
  }

  public static Object getEndType(EPath_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testElement1(null)) {
      a = getEndType(pe.getElement1(null));
    }
    return a;
  }

  public static Object getEndType(EInstance_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testElement1(null)) {
      a = getEndType(pe.getElement1(null));
    }
    return a;
  }

  public static Object getEndType(EAttribute_value_constraint pe) throws SdaiException {
    return null;
  }

  public static Object getEndType(EEntity_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testDomain(null)) {
      a = pe.getDomain(null);
    }
    return a;
  }

  public static Object getEndType(EAggregate_member_constraint pe) throws SdaiException {
    Object a = getEndType(pe.getAttribute(null));
    // Aggregate constraint is to get only one element of the aggregate, but not whole aggregate.
    if (a instanceof EAggregation_type) {
      EAggregation_type agg = (EAggregation_type) a;
      a = agg.getElement_type(null);
    }
    return a;
  }

  public static Object getEndType(ESelect_constraint pe) throws SdaiException {
    Object a = null;
    if (pe.testData_type(null)) {
      a = pe.getData_type(null);
    }
    return a;
  }

  public static EEntity_definition getAssumedDomain(EAttribute_mapping aMapping, EEntity_mapping eMapping) throws SdaiException {
    EEntity target = eMapping.getTarget(null);
    if (lastRefEntityDef == null) {
      return (EEntity_definition) target;
    }
    EEntity_definition retValue = lastRefEntityDef;
    //!!	lastRefEntityDef = null;// clean it after first usage..
    return retValue;

    /*
     * // get entity_mapping target:
     * EEntity target = ((EEntity_definition)aMapping.getParent(null)).getTarget(null);
     * // if data_type field is empty, return entity_mapping target
     * if (aMapping.testData_type(null)) {
     * ANamed_type types = aMapping.getData_type(null);
     * if (types.getMemberCount() != 1) {
     * System.out.println("PROBLEM: requested data_type list contains not 1 entry!"+
     * types+aMapping);
     * return (EEntity_definition) target;
     * }
     * else
     * return (EEntity_definition) types.getByIndexEntity(1);
     * }
     * return (EEntity_definition) target;
     */
  }

  public static EDefined_type getDefinedOrNull(EEntity type, EAttribute_mapping aMapping) throws SdaiException {
    if (type == null) {
      return null;
    }
    if (type instanceof EDefined_type) {
      return (EDefined_type) type;
    }

    if (type instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) type;
      return getDefinedOrNull(eAttr.getDomain(null), aMapping);
    }
    if (type instanceof EDerived_attribute) {
      EDerived_attribute dAttr = (EDerived_attribute) type;
      return getDefinedOrNull(dAttr.getDomain(null), aMapping);
    }
    if (type instanceof EInverse_attribute) {
      // use domain of inverse attribute itself!
      EInverse_attribute iAttr = (EInverse_attribute) type;
      return getDefinedOrNull(iAttr.getDomain(null), aMapping);
    }
    if (type instanceof ESimple_type) {
      return null;
    }
    if (type instanceof EEntity_definition) {
      return null;
    }
    if (type instanceof EAggregation_type) {
      EAggregation_type agType = (EAggregation_type) type;
      return getDefinedOrNull(agType.getElement_type(null), aMapping);
    }
    System.out.println("PROBLEM: this null should not be reached ever!" + type + aMapping);
    return null;
  }

  public static EEntity getSelectOrDefinition(EEntity type, EAttribute_mapping aMapping) throws SdaiException {
    if (type instanceof ESelect_type) {
      return type;
    }
    if (type instanceof EEntity_definition) {
      return type;
    }
    if (type instanceof EAggregation_type) {
      EAggregation_type agType = (EAggregation_type) type;
      EEntity agElement = agType.getElement_type(null);
      return getSelectOrDefinition(agElement, aMapping);
    }
    if (type instanceof EDefined_type) {
      EDefined_type deType = (EDefined_type) type;
      EEntity deDomain = deType.getDomain(null);
      return getSelectOrDefinition(deDomain, aMapping);
    }
    if (type instanceof ESimple_type) {
//!!			System.out.println("PROBLEM: Simple type detected: this should not happen!"+type+aMapping);
      return null;
    }
    if (type instanceof EEnumeration_type) {
//!!			System.out.println("PROBLEM: Enumeration type detected: this should not happen!"+type+aMapping);
      return null;
    }
    return null;
  }

  public static void buildFlatArray(ANamed_type types, EAttribute_mapping aMapping, Vector result) throws SdaiException {

    SdaiIterator it = types.createIterator();
    while (it.next()) {
      EEntity type = (EEntity) types.getCurrentMemberObject(it);
      EEntity insideDomain = getSelectOrDefinition(type, aMapping);
      if (insideDomain instanceof EEntity_definition) {
        result.add(insideDomain);
        continue;
      }
      /*
       * EEntity insideDomain = ((EDefined_type) type).getDomain(null);
       * if (insideDomain instanceof EAggregation_type) {
       * // access its element_type
       * }
       */
      if (!(insideDomain instanceof ESelect_type)) {
//!!				System.out.println("PROBLEM: MappingDocOperations.buildFlatArray"+
//!!					" insideDomain is not select type!"+insideDomain+aMapping);
        return;
      }
      ESelect_type insideType = (ESelect_type) insideDomain;
      ANamed_type insideTypes = insideType.getSelections(null);
      // do recursion
      buildFlatArray(insideTypes, aMapping, result);
    }
  }

  public static boolean buildWalkChain(Vector walkChain, EEntity_definition bottomType, EEntity_definition startType) throws SdaiException {
    if (bottomType == startType) {
      // do not add anything to chain- because this element will probably
      // be listed in select forward list.
      return true;
    }
    // otherwise get list of current supertypes
    AEntity supertypes = bottomType.getGeneric_supertypes(null);
    // visit all supertypes
    SdaiIterator it = supertypes.createIterator();
    while (it.next()) {
      EEntity_definition superType = (EEntity_definition) supertypes.getCurrentMemberObject(it);
      if (buildWalkChain(walkChain, superType, startType)) {
        walkChain.add(bottomType);
        return true;
      }
    }
    return false;
  }
}
