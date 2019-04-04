package jsdai.mappingUtils;

import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EArray_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBag_type;
import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.EList_type;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESet_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * This class contains some usefull methods to support some simple operations on attributes of entity.
 * It can not be instantiated.
 */
public class SimpleOperationsOnExtended {

  /**
   * Method returns final type of attribute.
   *
   * @param attribute This can be any type of attribute: explicit, or inverse or derived.
   */
  public static EEntity getAttributeDomain(EAttribute attribute) throws SdaiException {
    EEntity result = null;
    if (attribute instanceof EExplicit_attribute) {
      EExplicit_attribute ea = (EExplicit_attribute) attribute;
      result = getExplicit_attributeDomain(ea);
    }
    else if (attribute instanceof EInverse_attribute) {
      EInverse_attribute ia = (EInverse_attribute) attribute;
      result = ia.getDomain(null);
    }
    else if (attribute instanceof EDerived_attribute) {
      EDerived_attribute da = (EDerived_attribute) attribute;
      result = getBase_typeDomain(da.getDomain(null));
    }
    return result;
  }

  /**
   * Method returns final type of explicit_attribute.
   */
  public static EEntity getExplicit_attributeDomain(EExplicit_attribute e_attribute) throws SdaiException {
    return getBase_typeDomain(e_attribute.getDomain(null));
  }

  /**
   * Method returns final type of base_type.
   */
  public static EEntity getBase_typeDomain(EEntity domain) throws SdaiException {
    EEntity result = null;
    if (domain instanceof EAggregation_type) {
      result = getAggregation_typeDomain((EAggregation_type) domain);
    }
    else if (domain instanceof ESimple_type) {
      result = getSimple_typeDomain((ESimple_type) domain);
    }
    else if (domain instanceof ENamed_type) {
      result = getNamed_typeDomain((ENamed_type) domain);
    }
    return result;
  }

  /**
   * Method returns final type of aggregation_type.
   */
  public static EEntity getAggregation_typeDomain(EAggregation_type domain) throws SdaiException {
    EEntity result = null;
    if (domain instanceof ESet_type) {
      result = domain;
    }
    else if (domain instanceof EBag_type) {
      result = domain;
    }
    else if (domain instanceof EList_type) {
      result = domain;
    }
    else if (domain instanceof EArray_type) {
      result = domain;
    }
    return result;
  }

  /**
   * Method returns final type of simple_type.
   */
  public static EEntity getSimple_typeDomain(ESimple_type domain) throws SdaiException {
    EEntity result = null;
    if (domain instanceof ENumber_type) {
      result = domain;
    }
    else if (domain instanceof EInteger_type) {
      result = domain;
    }
    else if (domain instanceof EReal_type) {
      result = domain;
    }
    else if (domain instanceof EBoolean_type) {
      result = domain;
    }
    else if (domain instanceof ELogical_type) {
      result = domain;
    }
    else if (domain instanceof EBinary_type) {
      result = domain;
    }
    else if (domain instanceof EString_type) {
      result = domain;
    }
    return result;
  }

  /**
   * Method returns final type of named_type.
   */
  public static EEntity getNamed_typeDomain(ENamed_type domain) throws SdaiException {
    EEntity result = null;
    if (domain instanceof EDefined_type) {
      result = getDefined_typeDomain((EDefined_type) domain);
    }
    else if (domain instanceof EEntity_definition) {
      result = domain;
    }
    return result;
  }

  /**
   * Method returns final type of defined_type.
   */
  public static EEntity getDefined_typeDomain(EDefined_type domain) throws SdaiException {
    ENamed_type ntDomain = domain;
    // This checking reflects a common problem in creating application protocols:
    // developers of AP stops further defining entity property and declares it
    // as undefined_object. This means type of property is the same as in AIM: as ARM
    // property is mapped to AIM in any case. For handling this, we must not parse
    // undefined_object here (because this parsing would be wrong), and we must
    // allow this parsing be done somewhere else. For example, take a look at
    // ClassesGeneratorUtilityFunctions.generateGetXXAttributeMethods method.
    if (ntDomain.getName(null).equalsIgnoreCase("undefined_object")) {
      return domain;
    }
    return getUnderlying_typeDomain(domain.getDomain(null));
  }

  /**
   * Method returns final type of underlying_type.
   */
  public static EEntity getUnderlying_typeDomain(EEntity domain) throws SdaiException {
    EEntity result = null;
    if (domain instanceof EAggregation_type) {
      result = getAggregation_typeDomain((EAggregation_type) domain);
    }
    else if (domain instanceof ESimple_type) {
      result = getSimple_typeDomain((ESimple_type) domain);
    }
    else if (domain instanceof EDefined_type) {
      result = getDefined_typeDomain((EDefined_type) domain);
    }
    else if (domain instanceof ESelect_type) {
      result = domain;
    }
    else if (domain instanceof EEnumeration_type) {
      result = domain;
    }
    return result;
  }

  public static void enshureReadOnlyModel(SdaiModel model) throws SdaiException {
    if (model.getMode() == SdaiModel.NO_ACCESS) {
      model.startReadOnlyAccess();
    }
  }

}