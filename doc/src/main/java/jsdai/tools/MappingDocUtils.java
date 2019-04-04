package jsdai.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EArray_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBag_type;
import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EBound;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInteger_bound;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.EList_type;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EPopulation_dependent_bound;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESet_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.SExtended_dictionary_schema.EVariable_size_aggregation_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EAttribute_mapping_value;
import jsdai.SMapping_schema.EAttribute_value_constraint;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.ESchema_mapping;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.tools.refPath.AttributeReferencePath;

public class MappingDocUtils extends MappingDocParseUtils {

  public MappingDocUtils() {
  }

  /* ***********************************************************************
   * Constraints parsing methods
   * ***********************************************************************
   */

  protected static void parseExistingRefPath(AEntity path, AttributeReferencePath arPath, EEntity_mapping eMapping, EAttribute_mapping aMapping,
      EEntity constraint) throws SdaiException, IOException {
    SdaiIterator it = path.createIterator();
    while (it.next()) {
      EEntity element = (EEntity) path.getCurrentMemberObject(it);
      MappingDocOperations.currentPathElement = element;
      if (element instanceof EExplicit_attribute) {
        // now check whether this attribute is specified in constraint:
        //System.out.println("Starting to parse atribute"+aMapping+eMapping+element);
        if (element == constraint) {
          arPath.parse(element, eMapping, aMapping);
        }
        else {
          continue;// do nothing - this attribute should not be parsed or somehow displayed.
        }
      }
      if (element instanceof EInverse_attribute) {
        //System.out.println("Starting to parse inverse atribute"+aMapping+eMapping+element);
        if (element == constraint) {
          arPath.parse(element, eMapping, aMapping);
        }
        else {
          continue;// do nothing - this attribute should not be parsed or somehow displayed.
        }
      }
      if (element instanceof EDerived_attribute) {
        //System.out.println("Starting to parse derived atribute"+aMapping+eMapping+element);
        if (element == constraint) {
          arPath.parse(element, eMapping, aMapping);
        }
        else {
          continue;// do nothing - this attribute should not be parsed or somehow displayed.
        }
      }
      // for the rest of cases.
      arPath.parse(element, eMapping, aMapping);
    }
  }

  /*
   * protected static void parseExistingRefPath(AAttribute_mapping_path_select path,
   * AttributeReferencePath arPath,
   * EEntity_mapping eMapping,
   * EAttribute_mapping aMapping)
   * throws SdaiException, IOException {
   * SdaiIterator it = path.createIterator();
   * boolean first = true;
   * while (it.next()) {
   * EEntity element = (EEntity) path.getCurrentMemberObject(it);
   * // if (first) {
   * // if (element instanceof EExplicit_attribute) {
   * // if (!aMapping.testConstraints(null))
   * // System.out.println("FOUND: "+eMapping+aMapping);
   * // else
   * // arPath.parse(element, eMapping, aMapping);
   * // }
   * // first = false;
   * // }
   * // else
   * arPath.parse(element, eMapping, aMapping);
   * }
   * }
   */
  /*
   * protected static String parseExistingRefPath(EAttribute_mapping atrMapping) throws SdaiException {
   * String referencePath = "";
   * EEntity target = globalEntMapping.getTarget(null);
   * if (target instanceof EEntity_definition) {
   * globalPreviousEntity = target;
   * } else if (target instanceof EAttribute) {
   * globalPreviousEntity = (EEntity_definition)((EAttribute)target).getParent(null);
   * }
   * AAttribute_mapping_path_select path = atrMapping.getPath(null);
   * SdaiIterator it_path = path.createIterator();
   * if (path.getMemberCount() > 0) {
   * if (atrMapping.testConstraints(null)) {
   * referencePath += MappingDocOperations.printSuperTypes(globalPreviousEntity,
   * MappingDocOperations.getStartType(atrMapping.getConstraints(null)));
   * globalPreviousEntity = MappingDocOperations.getEndType(atrMapping.getConstraints(null));
   * String constraintsRefPath = parseConstraint(atrMapping.getConstraints(null), path, false, false);
   * referencePath += (constraintsRefPath.equals("")) ? "" : println(constraintsRefPath);
   * }
   *
   * if (!(isMappedDirectly(path.getByIndex(path.getMemberCount())) && (path.getMemberCount() == 1))) {
   * for (int i = 1; i <= path.getMemberCount(); i++) {
   * globalThisPath = path.getByIndex(i);
   * referencePath += MappingDocOperations.printSuperTypes(globalPreviousEntity,
   * MappingDocOperations.getStartType(globalThisPath));
   * globalPreviousEntity = MappingDocOperations.getEndType(globalThisPath);
   * referencePath += println(parseConstraint(globalThisPath, path, false, false));
   * }
   * }
   * } else {
   * if (atrMapping.testConstraints(null)) {
   * referencePath += println(parseConstraint(atrMapping.getConstraints(null), path, false, false));
   * }
   * }
   * return referencePath;
   * }
   */
  public static String parseReferencePath(EGeneric_attribute_mapping atrMapping, EEntity_mapping entMapping) throws SdaiException, IOException {
    // attribute_mapping has been changed to next one.
    // clean trace of previous attribute_mapping:
    MappingDocOperations.lastRefEntityDef = null;
    // What we should do is to create a merged array of Eentities.
    // Both constraints and path should be merged.
//System.out.println("atrMapping"+atrMapping+entMapping);		
    AttributeReferencePath arPath = new AttributeReferencePath();
    EEntity constraint = null;
    AEntity path = null;
    EAttribute_mapping aMapping = null;
    if (atrMapping.testConstraints(null)) {
      constraint = atrMapping.getConstraints(null);
    }
    if (atrMapping instanceof EAttribute_mapping) {
      aMapping = (EAttribute_mapping) atrMapping;
      if (aMapping.testPath(null)) {
        path = aMapping.getPath(null);
      }
    }
    // what we can do right now:
    // check, maybe this constraint exists within path. If yes,
    // forget this constraint and use path only. If not, add constraint
    // before path.
    if ((constraint == null) && (path == null)) {
//			System.out.println("PROBLEM: no constraints, no path is specified!"+atrMapping);
      MappingDocOperations.fullPath = null;
      return arPath.display();
    }
    if (constraint == null) {
      MappingDocOperations.fullPath = path;
      parseExistingRefPath(path, arPath, entMapping, aMapping, constraint);
      return arPath.display();
    }
    if (path == null) {
      AEntity agEntity = new AEntity();
      agEntity.addUnordered(constraint);
      MappingDocOperations.fullPath = agEntity;
      parseExistingRefPath(agEntity, arPath, entMapping, aMapping, constraint);
      return arPath.display();
    }
    if (path.isMember(constraint)) {
      MappingDocOperations.fullPath = path;
      parseExistingRefPath(path, arPath, entMapping, aMapping, constraint);
      return arPath.display();
    }
    //System.out.println("atrMapping is "+aMapping + "constraint is "+constraint);
    AEntity agEntity = new AEntity();
//!!		agEntity.addByIndex(1, constraint);
    int i = 1;
    SdaiIterator it = path.createIterator();
    while (it.next()) {
      EEntity element = (EEntity) path.getCurrentMemberObject(it);
      agEntity.addByIndex(i++, element);
    }
    MappingDocOperations.fullPath = agEntity;
    parseExistingRefPath(agEntity, arPath, entMapping, aMapping, constraint);
    return arPath.display();
    /*
     * EAttribute_mapping attributeMapping = (EAttribute_mapping)entity;
     *
     * boolean included = false;
     * EEntity constraints = null;
     * if (attributeMapping.testConstraints(null) && attributeMapping.testPath(null)) {
     * constraints = attributeMapping.getConstraints(null);
     * AEntity paths = attributeMapping.getPath(null);
     * SdaiIterator pathsIterator = paths.createIterator();
     * while(pathsIterator.next()) {
     * EEntity path = paths.getCurrentMemberEntity(pathsIterator);
     * if(path == constraints) {
     * included = true;
     * break;
     * }
     * }
     * }
     * if (!included && attributeMapping.testConstraints(null)) {
     * ConstraintElement ce =
     * ConstraintSelectElement.create(container, this,
     * attributeMapping.getConstraints(null));
     * ce.setType(CONSTRAINT_ONLY);
     * ce.parse(container);
     * }
     *
     * if (attributeMapping.testPath(null)) {
     * AEntity paths = attributeMapping.getPath(null);
     * SdaiIterator pathsIterator = paths.createIterator();
     * while(pathsIterator.next()) {
     * EEntity path = paths.getCurrentMemberEntity(pathsIterator);
     * ConstraintElement ce =
     * AttributeMappingPathSelectElement.create(container, this, path);
     * if(included && path == constraints)
     * ce.setType(CONSTRAINT_AND_PATH);
     * ce.parse(container);
     * }
     * }
     */

//		System.out.println("Starting to work on attribute mapping "+atrMapping);
    /*
     * AttributeReferencePath arPath = new AttributeReferencePath();
     * if (!(atrMapping instanceof EAttribute_mapping)) {
     * // for now, parse only constraints field:
     * if (!atrMapping.testConstraints(null)) {
     * return arPath.display();
     * }
     * EEntity constraints = atrMapping.getConstraints(null);
     * arPath.parse(constraints, entMapping, null);
     * return arPath.display();
     * }
     *
     * EAttribute_mapping eaMapping = (EAttribute_mapping) atrMapping;
     * if (eaMapping.testPath(null)) {
     * AAttribute_mapping_path_select path = eaMapping.getPath(null);
     * parseExistingRefPath(path, arPath, entMapping, eaMapping);
     * }
     * else {
     * if (eaMapping.testConstraints(null)) {
     * EEntity constraint = eaMapping.getConstraints(null);
     * arPath.parse(constraint, entMapping, eaMapping);
     * }
     * }
     * return arPath.display();
     */
  }

  /*
   * public static String parseReferencePath(EGeneric_attribute_mapping atrMapping,
   * EEntity_mapping entMapping) throws SdaiException {
   * if (!(atrMapping instanceof EAttribute_mapping)) {
   * // for now, parse only constraints field:
   * if (!atrMapping.testConstraints(null)) {
   * return "";
   * }
   * EEntity constraints = atrMapping.getConstraints(null);
   * return parseConstraint(constraints, null, false, false);
   * }
   * globalAtrMapping = (EAttribute_mapping) atrMapping;
   * globalEntMapping = entMapping;
   * if (globalAtrMapping.testPath(null)) {
   * return parseExistingRefPath(globalAtrMapping);
   * }
   * else {
   * if (globalAtrMapping.testConstraints(null)) {
   * EEntity constraints = globalAtrMapping.getConstraints(null);
   * return parseConstraint(constraints, null, false, false);
   * }
   * else
   * return "";
   * }
   * }
   */
  private static boolean isMappedDirectly(EEntity element) throws SdaiException {
    if (element instanceof EExplicit_attribute) {
      EExplicit_attribute ea = (EExplicit_attribute) element;
      EEntity domain = ea.getDomain(null);
      while (domain instanceof EDefined_type)
        domain = ((EDefined_type) domain).getDomain(null);
      if (domain instanceof ESimple_type) {
        return true;
      }
    }
    return false;
  }

  /* ***********************************************************************
   * determineAttrMappingTypeFromConstraint methods
   * ***********************************************************************
   */

  protected static String determineAttrMappingTypeFromConstraint(EPath_constraint epElement, EGeneric_attribute_mapping egaMapping) throws SdaiException,
      IOException {
    if (!epElement.testElement1(null)) {
      System.out.println("PROBLEM: element1 is not set! determineAttrMappingTypeFromConstraint(EEntity)");
      return "PATH";
    }
    EEntity element1 = epElement.getElement1(null);
    if (element1 instanceof EExplicit_attribute) {
      return parseExplicit_attribute((EExplicit_attribute) element1, egaMapping);
    }
    else {
      return determineAttrMappingTypeFromConstraint(element1, egaMapping);
    }
  }

  protected static String parseExplicit_attribute(EExplicit_attribute expAttr, EGeneric_attribute_mapping egaMapping) throws SdaiException, IOException {
    EEntity domain = expAttr.getDomain(null);
    if (domain instanceof EEntity_definition) {
      EEntity_definition eDef = (EEntity_definition) domain;
      return eDef.getName(null);
    }
    else if (domain instanceof EAggregation_type) {
      String[] proposedType = new String[1];
      EAggregation_type eaType = (EAggregation_type) domain;
      EEntity elType = eaType.getElement_type(null);
      // ignoring return value!
      parseSelectBaseType(elType, proposedType);
      return proposedType[0];
    }
    else {
      return expAttr.getParent(null).getName(null) + "." + expAttr.getName(null);
    }
  }

  protected static String parseDerived_attribute(EDerived_attribute derAttr, EGeneric_attribute_mapping egaMapping) throws SdaiException, IOException {
    EEntity domain = derAttr.getDomain(null);
    if (domain instanceof EEntity_definition) {
      EEntity_definition eDef = (EEntity_definition) domain;
      return eDef.getName(null);
    }
    else if (domain instanceof EAggregation_type) {
      String[] proposedType = new String[1];
      EAggregation_type eaType = (EAggregation_type) domain;
      EEntity elType = eaType.getElement_type(null);
      // ignoring return value!
      parseSelectBaseType(elType, proposedType);
      return proposedType[0];
    }
    else {
      return derAttr.getParent(null).getName(null) + "." + derAttr.getName(null);
    }
  }

  protected static String determineAttrMappingTypeFromConstraint(EEntity_constraint eeElement, EGeneric_attribute_mapping egaMapping) throws SdaiException,
      IOException {
    // from entity_constraints let's take only domain value
    EEntity_definition eDef = eeElement.getDomain(null);
    return eDef.getName(null);
  }

  protected static String determineAttrMappingTypeFromConstraint(EInverse_attribute_constraint eiElement, EGeneric_attribute_mapping egaMapping)
      throws SdaiException, IOException {
    EEntity invAttribute = eiElement.getInverted_attribute(null);
    return determineAttrMappingTypeFromConstraint(invAttribute, egaMapping);
  }

  protected static String determineAttrMappingTypeFromConstraint(EAggregate_member_constraint eaElement, EGeneric_attribute_mapping egaMapping)
      throws SdaiException, IOException {
    EEntity attribute = eaElement.getAttribute(null);
    return determineAttrMappingTypeFromConstraint(attribute, egaMapping);
  }

  protected static String determineAttrMappingTypeFromConstraint(ESelect_constraint esElement, EGeneric_attribute_mapping egaMapping) throws SdaiException,
      IOException {
    EEntity attribute = esElement.getAttribute(null);
    return determineAttrMappingTypeFromConstraint(attribute, egaMapping);
  }

  protected static String determineAttrMappingTypeFromConstraint(EAttribute_value_constraint eavElement, EGeneric_attribute_mapping egaMapping)
      throws SdaiException, IOException {
    EEntity attribute = eavElement.getAttribute(null);
    return determineAttrMappingTypeFromConstraint(attribute, egaMapping);
  }

  protected static String determineAttrMappingTypeFromConstraint(EEntity lastPathElement, EGeneric_attribute_mapping egaMapping) throws SdaiException,
      IOException {
    if (lastPathElement instanceof EExplicit_attribute) {
      return parseExplicit_attribute((EExplicit_attribute) lastPathElement, egaMapping);
    }
    if (lastPathElement instanceof EPath_constraint) {
      EPath_constraint epElement = (EPath_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(epElement, egaMapping);
    }
    if (lastPathElement instanceof EEntity_constraint) {
      EEntity_constraint eeElement = (EEntity_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(eeElement, egaMapping);
    }
    if (lastPathElement instanceof EInverse_attribute_constraint) {
      EInverse_attribute_constraint eiElement = (EInverse_attribute_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(eiElement, egaMapping);
    }
    if (lastPathElement instanceof EAggregate_member_constraint) {
      EAggregate_member_constraint eaElement = (EAggregate_member_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(eaElement, egaMapping);
    }
    if (lastPathElement instanceof ESelect_constraint) {
      ESelect_constraint esElement = (ESelect_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(esElement, egaMapping);
    }
    if (lastPathElement instanceof EDerived_attribute) {
      EDerived_attribute derAttr = (EDerived_attribute) lastPathElement;
      return parseDerived_attribute(derAttr, egaMapping);
    }
    if (lastPathElement instanceof EAttribute_value_constraint) {
      EAttribute_value_constraint eavElement = (EAttribute_value_constraint) lastPathElement;
      return determineAttrMappingTypeFromConstraint(eavElement, egaMapping);
    }
    System.out.println("don't know how to parse:" + lastPathElement + "  " + egaMapping);
    return "PATH";
  }

  protected static String determineAttrMappingType(EAttribute_mapping_value eamMapping) throws SdaiException, IOException {
    //the only thing we can read from attribute_mapping_value is
    // constraints: they should allow us to catch to what attribute
    // it's mapped on AIM side.
    if (!eamMapping.testConstraints(null)) {
      return "IDENTICAL_MAPPING";
    }
    EEntity constraints = eamMapping.getConstraints(null);
    return determineAttrMappingTypeFromConstraint(constraints, eamMapping);
  }

  protected static String determineAttrMappingType(EAttribute_mapping eaMapping) throws SdaiException, IOException {
    String result = "UNKNOWN";
    if (!eaMapping.testPath(null)) {
      // mapping path is empty. This happens only in case of identical mapping:
      return "IDENTICAL_MAPPING";
    }
    if (eaMapping.getPath(null).getMemberCount() == 0) {
      return "IDENTICAL_MAPPING";
    }
    // test for other cases: PATH, Single AIM, Multiple AIM
    int pathLength = eaMapping.getPath(null).getMemberCount();
    EEntity lastPathElement = eaMapping.getPath(null).getByIndexEntity(pathLength);
    if (isMappedDirectly(lastPathElement)) {
      EAttribute aimAttribute = (EAttribute) lastPathElement;
      String aimAttrParentEntityName = aimAttribute.getParent(null).getName(null);
      result = aimAttrParentEntityName + "." + aimAttribute.getName(null);
      return result; // recognized as Single AIM mapping.
    }
    else {
      return determineAttrMappingTypeFromConstraint(lastPathElement, eaMapping);
    }
//		return "PATH"; // no other knowledge about mapping path?
  }

  protected static String determineAttrMappingType(EGeneric_attribute_mapping atrMapping) throws SdaiException, IOException {
    if (atrMapping instanceof EAttribute_mapping) {
      return determineAttrMappingType((EAttribute_mapping) atrMapping);
    }
    if (atrMapping instanceof EAttribute_mapping_value) {
      return determineAttrMappingType((EAttribute_mapping_value) atrMapping);
    }
    throw new IOException("Unknown attribute mapping type detected!");
  }

  /* ***********************************************************************
   * various helper methods
   * ***********************************************************************
   */

  public static String toSentenceCase(String value) {
    StringBuffer nameBuffer = new StringBuffer(value);
    nameBuffer.setCharAt(0, nameBuffer.substring(0, 1).toUpperCase().charAt(0));
    String retName = nameBuffer.toString();
    return retName;
  }

  public static ESchema_mapping findSchemaMapping(SdaiModel model) throws SdaiException {
    if (model.getMode() == SdaiModel.NO_ACCESS) {
      model.startReadOnlyAccess();
    }
    AEntity e = model.getInstances(ESchema_mapping.class);
    if (e.getMemberCount() > 0) {
      return (ESchema_mapping) e.getByIndexEntity(1);
    }
    else {
      return null;
    }
  }

  public static String getDataTypeName(EGeneric_attribute_mapping atrMapping) throws SdaiException, IOException {
    String dataTypesName = "";
    if (atrMapping.testData_type(null)) {
      ANamed_type namedTypes = atrMapping.getData_type(null);
      SdaiIterator it = namedTypes.createIterator();
      if (it.next()) {
        ENamed_type namedType = (ENamed_type) namedTypes.getCurrentMemberObject(it);
        dataTypesName = namedType.getName(null);
        // check if there are several named_types set:
        if (namedTypes.getMemberCount() > 1) {
          throw new IOException("Several named data types in data_types list detected!");
        }
      }
    }
    return dataTypesName;
  }

  public static String getDomainTypeName(EAttribute attribute, String[] proposedType) throws SdaiException, IOException {
    String result = "";
    if (attribute instanceof EExplicit_attribute) {
      EExplicit_attribute expAttr = (EExplicit_attribute) attribute;
      if (!expAttr.testDomain(null)) {
        return result;
      }
      EEntity baseType = expAttr.getDomain(null);
      result = parseSelectBaseType(baseType, proposedType);
    }
    if (attribute instanceof EDerived_attribute) {
      EDerived_attribute derAttr = (EDerived_attribute) attribute;
      if (!derAttr.testDomain(null)) {
        return result;
      }
      EEntity baseType = derAttr.getDomain(null);
      result = parseSelectBaseType(baseType, proposedType);
    }
    if (attribute instanceof EInverse_attribute) {
      EInverse_attribute invAttr = (EInverse_attribute) attribute;
      if (!invAttr.testInverted_attr(null)) {
        return result;
      }
      EAttribute atr = invAttr.getInverted_attr(null);
      return getDomainTypeName(atr, proposedType);
    }
    return result;
  }

  /* ***********************************************************************
   * determine exact attribute type methods
   * ***********************************************************************
   */
  private static String parseAggregationInfo(EArray_type eaType) throws SdaiException, IOException {
    String result = "ARRAY [";
    if (!eaType.testLower_index(null)) {
      result += "?:";
    }
    else {
      EBound lowerBound = eaType.getLower_index(null);
      result = result + parseEBound(lowerBound) + ":";
    }
    if (!eaType.testUpper_index(null)) {
      result += "?]";
    }
    else {
      EBound higherBound = eaType.getUpper_index(null);
      result = result + parseEBound(higherBound) + "]";
    }
    // now check for unique flag:
    if (eaType.getUnique_flag(null)) {
      return result + " UNIQUE OF ";
    }
    else {
      return result + " OF ";
    }
  }

  private static String parseEBound(EBound bound) throws SdaiException, IOException {
    if (bound instanceof EInteger_bound) {
      int index = ((EInteger_bound) bound).getBound_value(null);
      return String.valueOf(index);
    }
    else if (bound instanceof EPopulation_dependent_bound) {
      // there is no knowledge how to read this instance, so leave
      // a question mark for now.
      return "?";
    }
    else {
      throw new IOException("Unknown EBound subtype found!");
    }
  }

  private static String parseVSAggregationBounds(EVariable_size_aggregation_type vsType) throws SdaiException, IOException {
    String result = "";
    if (!vsType.testLower_bound(null)) {
      result = "?";
    }
    else {
      EBound lowerBound = vsType.getLower_bound(null);
      result = result + parseEBound(lowerBound) + ":";
    }
    if (!vsType.testUpper_bound(null)) {
      result += "?";
    }
    else {
      EBound higherBound = vsType.getUpper_bound(null);
      result = result + parseEBound(higherBound);
    }
    return "[" + result + "]";
  }

  private static String parseAggregationInfo(EVariable_size_aggregation_type vsType) throws SdaiException, IOException {
    String agType = "";
    String agBounds = parseVSAggregationBounds(vsType);
    String agTail = "";
    if (vsType instanceof EBag_type) {
      agType = "BAG ";
      agTail = " OF ";
    }
    if (vsType instanceof EList_type) {
      agType = "LIST ";
      EList_type elType = (EList_type) vsType;
      if (elType.getUnique_flag(null)) {
        agTail = " UNIQUE OF ";
      }
      else {
        agTail = " OF ";
      }
    }
    if (vsType instanceof ESet_type) {
      agType = "SET ";
      agTail = " OF ";
    }
    return agType + agBounds + agTail;
  }

  private static String parseAggregationInfo(EAggregation_type agType) throws SdaiException, IOException {
    if (agType instanceof EArray_type) {
      EArray_type eaType = (EArray_type) agType;
      return parseAggregationInfo(eaType);
    }
    if (agType instanceof EVariable_size_aggregation_type) {
      EVariable_size_aggregation_type vsType = (EVariable_size_aggregation_type) agType;
      return parseAggregationInfo(vsType);
    }
    throw new IOException("Unknown aggregation type encountered!");
  }

  private static String parseSelectBaseType(EEntity baseType, String[] proposedType) throws SdaiException, IOException {
    String result = "";
    if (baseType instanceof ENamed_type) {
      ENamed_type namedType = (ENamed_type) baseType;
      result = namedType.getName(null);
      proposedType[0] = result;
      return "";
    }
    if (baseType instanceof EAggregation_type) {
      // parse the type of elements, although another possibility is
      // to determine exact type of aggregation itself.
      EAggregation_type agType = (EAggregation_type) baseType;
      if (!agType.testElement_type(null)) {
        System.out.println("PROBLEM: element type in aggregation not set!" + agType);
        return "NOTSET!";
      }
      // I must write down what kind of aggregation is this and what boundaries
      // apply.
      result = parseAggregationInfo(agType);
      EEntity elementType = agType.getElement_type(null);
      result = result + parseSelectBaseType(elementType, proposedType);
      return result;
    }
    if (baseType instanceof ESimple_type) {
      if (baseType instanceof EInteger_type) {
        proposedType[0] = "integer";
        return "";
      }
      if (baseType instanceof ENumber_type) {
        proposedType[0] = "number";
        return "";
      }
      if (baseType instanceof EReal_type) {
        proposedType[0] = "real";
        return "";
      }
      if (baseType instanceof EBoolean_type) {
        proposedType[0] = "boolean";
        return "";
      }
      if (baseType instanceof ELogical_type) {
        proposedType[0] = "logical";
        return "";
      }
      if (baseType instanceof EBinary_type) {
        proposedType[0] = "binary";
        return "";
      }
      if (baseType instanceof EString_type) {
        proposedType[0] = "string";
        return "";
      }
      throw new IOException("Although I have ESimple_type," + " I was unable to recognize exact type. Please check.");
    }
    return result;
  }

  /* ***********************************************************************
   * Results output methods
   * ***********************************************************************
   */

  /**
   * A helper method that makes sure that Vector with AttrInfo is linked with given
   * armAttributeName and recored in attrInfoTable structure.
   */
  public static Vector getAttrInfoVector(Hashtable attrInfoTable, String armAttributeName) {
    Vector result = null;
    if (attrInfoTable.containsKey(armAttributeName)) {
      result = (Vector) attrInfoTable.get(armAttributeName);
    }
    else {
      result = new Vector();
      attrInfoTable.put(armAttributeName, result);
    }
    return result;
  }

  private static void printEntityMappingRefPath(FileWriter outFile, Object refPath) throws IOException {
    //printBlockquoteStart(outFile, 1, refPath.toString());
    //printBlockquoteEnd(outFile, 1);
    print(outFile, refPath.toString());
    println(outFile);
    println(outFile);
  }

  private static void printMappedAttributeInfo(FileWriter outFile, Hashtable attrInfo) throws IOException {
    Enumeration keys = attrInfo.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      Vector entries = (Vector) attrInfo.get(key);
      int count = entries.size();
      int blockQuoteLevel = 0;
      for (int i = 0; i < count; i++) {
        AttrInfo info = (AttrInfo) entries.get(i);
        if (info.isSelectTypeInside()) {
          blockQuoteLevel = 1;
          print(outFile, info.getArmColumn());
          println(outFile);
          continue;
        }
        printBlockquoteStart(outFile, blockQuoteLevel, info.getArmColumn());
        printBlockquoteEnd(outFile, blockQuoteLevel);
//				println(outFile);
        printBlockquoteStart(outFile, blockQuoteLevel + 1, info.getRefPath());
        printBlockquoteEnd(outFile, blockQuoteLevel + 1);
      }
    }
  }

  /**
   *
   */
  public static void printTableForEntityMapping(FileWriter outFile, String armEntityFileName, Hashtable entMappingOutput, String startLabel, String endLabel)
      throws IOException {
    print(outFile, startLabel);
    printH3(outFile, "Mapping");
    println(outFile);
    // from given file name parse entity name:
    int index = armEntityFileName.lastIndexOf(File.separator);
    String armEntityName = armEntityFileName.substring(index + 1, armEntityFileName.length());
    armEntityName = armEntityName.toLowerCase();
    Enumeration keys = entMappingOutput.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      // start new table:
      printBlockquoteStart(outFile, 1, printBold(armEntityName) + " (to " + key + ")");
      printBlockquoteEnd(outFile, 1);
      printBlockquoteStart(outFile, 2, "");
      // key == entity_mapping target name (aim entity name for now)
      Vector entries = (Vector) entMappingOutput.get(key);
      int count = entries.size();
      for (int i = 0; i < count; i++) {
        if (i == 0) {
          Object o = entries.get(i);
          printEntityMappingRefPath(outFile, o);
        }
        else {
          Hashtable table = (Hashtable) entries.get(i);
          printMappedAttributeInfo(outFile, table);
        }
      }
      printBlockquoteEnd(outFile, 2);
      printBreak(outFile);
      println(outFile);
    }
    print(outFile, endLabel);
    printHtmlTail(outFile);
  }

  /**
   * Open requested file, load it, remove previous entries from MappingDoc, write back the rest
   * of file and return reference to opened FileWriter object.
   */
  public static FileWriter removePreviousEntries(String fileNameWithoutExtension, String rootLocation, String startLabel, String endLabel) throws IOException {
    String fullFileName = rootLocation + File.separator + fileNameWithoutExtension + ".html";
    FileReader readTargetFile = new FileReader(fullFileName);
    StringBuffer buffer = new StringBuffer();
    char fileChar = '0';
    int retValue = 0;
    while (retValue != -1) {
      retValue = readTargetFile.read();
      if (retValue != -1) {
        fileChar = (char) retValue;
        buffer.append(fileChar);
      }
    }
    readTargetFile.close();

    String fullOriginalFile = new String(buffer);
    String fileStart = fullOriginalFile;
    int htmlLabelIndex = fullOriginalFile.lastIndexOf(startLabel);
    if (htmlLabelIndex != -1) {
      fileStart = fullOriginalFile.substring(0, htmlLabelIndex);
    }
    String fileEnd = "";
    htmlLabelIndex = fullOriginalFile.lastIndexOf(endLabel);
    if (htmlLabelIndex != -1) {
      int value = htmlLabelIndex + endLabel.length();
      fileEnd = fullOriginalFile.substring(value, fullOriginalFile.length());
    }
    // now remove /body and /html tags from the file text
    String fullFileText = fileStart + fileEnd;
    int htmlEndIndex = fullFileText.lastIndexOf("</HTML>");
    if (htmlEndIndex == -1) {
      throw new IOException("Closing tag </HTML> was not found in given file!");
    }
    fullFileText = fullFileText.substring(0, htmlEndIndex);
    int htmlBodyEndIndex = fullFileText.lastIndexOf("</BODY>");
    if (htmlBodyEndIndex == -1) {
      throw new IOException("Closing tag </BODY> was not found in given file!");
    }
    fullFileText = fullFileText.substring(0, htmlBodyEndIndex);

    FileWriter writer = new FileWriter(fullFileName);
    writer.write(fullFileText);

    return writer;
  }

  private static void printBlockquoteStart(FileWriter f, int noOfQuotes, String s) throws IOException {
    for (int i = 0; i < noOfQuotes; i++) {
      f.write("<BLOCKQUOTE>");
    }
    f.write(s);
  }

  private static void printBlockquoteEnd(FileWriter f, int noOfQuotes) throws IOException {
    for (int i = 0; i < noOfQuotes; i++) {
      f.write("</BLOCKQUOTE>");
    }
  }

  public static String println(String s) {
    return s + "<BR>\n";
  }

  private static void println(FileWriter f) throws IOException {
    f.write("<BR>\n");
  }

  private static String printBold(String s) throws IOException {
    return "<B>" + s + "</B>";
  }

  private static void print(FileWriter f, String s) throws IOException {
    f.write(s);
  }

  private static void printBreak(FileWriter f) throws IOException {
    f.write("<HR>\n");
  }

  private static void printH3(FileWriter f, String s) throws IOException {
    f.write("<H4>\n" + s + "</H4>\n");
  }

  private static void printHtmlTail(FileWriter f) throws IOException {
    f.write("</BODY>\n</HTML>\n");
  }

}
