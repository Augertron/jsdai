package jsdai.tools;

import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.EEntity;

public class MappingDocParseUtils {
  // NOTE: for now, we are able to parse with full extent only EAttribute_mapping,
  // not EGeneric_attribute_mapping.
  public static EAttribute_mapping globalAtrMapping = null;
  public static EEntity_mapping globalEntMapping = null;
  public static Object globalPreviousEntity = null;
  // I assume this variable stores ref to currently processed path.
  public static EEntity globalThisPath = null;
  public static String globalArrayMember = "";

  public MappingDocParseUtils() {
  }

  /* ***********************************************************************
   * 							Constraints parsing methods
   *  ***********************************************************************
   */
/*
	protected static String parseElement(ESelect_constraint constraint,
								AAttribute_mapping_path_select path,
								boolean simple,
								boolean inverse) throws SdaiException {
		String result = "";
		String t = "";
		ADefined_type ad = constraint.getData_type(null);
		SdaiIterator it = ad.createIterator();
		while (it.next()) {
			t += ad.getCurrentMember(it).getName(null)+", ";
		}
		result += println(t+" = ");
		result += parseElement(constraint.getAttribute(null), path, true, inverse);
		return result;
	}

	protected static String parseElement(EAggregate_member_constraint constraint,
								AAttribute_mapping_path_select path,
								boolean simple,
								boolean inverse) throws SdaiException {
		String result = "";
		if (constraint.testMember(null)) {
			globalArrayMember = String.valueOf(constraint.getMember(null));
		} else {
			globalArrayMember = "i";
		}
		result += parseElement(constraint.getAttribute(null), path, simple, inverse);
		return result;
	}


	protected static String parseElement(EInstance_constraint constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
		String result = "";
		String s1 = "", s2 = "", s3 = "", s4 = "";
		if (constraint instanceof EAnd_constraint_relationship) {
			s1 = "["; s2 = "]"; s3 = "["; s4 = "]";
		} else if (constraint instanceof EOr_constraint_relationship) {
			s1 = "("; s2 = ")"; s3 = "("; s4 = ")";
		}
		if ( !(constraint.testElement1(null) && constraint.testElement2(null)) ) {
			System.out.println("PROBLEM: Instance_constraint element not set!"+constraint);
			return "PATH_CONSTRAINT INCOMPLETE!";
		}
		EEntity element1 = constraint.getElement1(null);
		EEntity element2 = constraint.getElement2(null);
		if (!MappingDocOperations.isElementInPath(element1, path, globalThisPath)) {
			if (!MappingDocOperations.isElementInPath(element2, path, globalThisPath)) {
				result += s1;
				result += parseElement(element1, path, false, inverse);
				result += println(s2);
			} else {
				result += parseElement(element1, path, false, inverse);
			}
		}
		if (!MappingDocOperations.isElementInPath(element2, path, globalThisPath)) {
			if (!MappingDocOperations.isElementInPath(element1, path, globalThisPath)) {
				result += s3;
				result += MappingDocOperations.printSuperTypes(
								MappingDocOperations.getEndType(element1),
								MappingDocOperations.getStartType(element2));
				result += parseElement(element2, path, false, inverse);
				result += s4;
			} else {
				result += MappingDocOperations.printSuperTypes(
								MappingDocOperations.getEndType(element1),
								MappingDocOperations.getStartType(element2));
				result += parseElement(element2, path, false, inverse);
			}
		}
		return result;
	}
	
	protected static String parseElement(EPath_constraint constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
//System.out.println("Starting to parseElement(EPath_constraint"+constraint);
		String result = "";
		if ( !(constraint.testElement1(null) && constraint.testElement2(null)) ) {
			System.out.println("PROBLEM: Path_constraint element not set!"+constraint);
			return "PATH_CONSTRAINT INCOMPLETE!";
		}
		EEntity element1 = constraint.getElement1(null);
		EEntity element2 = constraint.getElement2(null);
		if (!MappingDocOperations.isElementInPath(element1, path, globalThisPath)) {

			if (!MappingDocOperations.isElementInPath(element2, path, globalThisPath)) {
				result += println(parseElement(element1, path, false, inverse));
			} else {
				result += parseElement(element1, path, false, inverse);
			}
		}
		if (!MappingDocOperations.isElementInPath(element2, path, globalThisPath)) {

//System.out.println("starting to parse element2, which is "+ element2);

			if (!MappingDocOperations.isElementInPath(element1, path, globalThisPath)) {
//System.out.println("starting to parse then branch");
				result += "{";
				result += MappingDocOperations.printSuperTypes(
									MappingDocOperations.getEndType(element1),
									MappingDocOperations.getStartType(element2));
				result += parseElement(element2, path, false, inverse);
				result += "}";
//System.out.println("completed parsing then branch");
			} else {
				result += MappingDocOperations.printSuperTypes(
									MappingDocOperations.getEndType(element1),
									MappingDocOperations.getStartType(element2));
				result += parseElement(element2, path, false, inverse);
			}
		}
		return result;
	}
	
	protected static String parseElement(EInverse_attribute_constraint constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
		String result = "";
		result += parseElement(constraint.getInverted_attribute(null), path, true, true);
		return result;
	}
	
	protected static String parseElement(EAttribute constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
		String result = "";
		String a = null;
		String t = null;
		String g = "";
		a = constraint.getParent(null).getName(null) + "." + constraint.getName(null);
		if (constraint instanceof EDerived_attribute) {
			t = "derived";
		} else if (constraint instanceof EInverse_attribute) {
			t = "inverse";
		} else if (constraint instanceof EExplicit_attribute) {
			EExplicit_attribute ee = (EExplicit_attribute)constraint;
			EEntity domain = ee.getDomain(null);
			while (domain instanceof EAggregation_type) {
				if (globalArrayMember.length() == 0)
					System.out.println("PROBLEM: parseElement(EAttribute) - globalArrayMember is empty string!");
				g += "["+globalArrayMember+"]";
				domain = ((EAggregation_type)domain).getElement_type(null);
			}
			if (domain instanceof ESimple_type) {
				result += a + g;
				return result;
			} else if (domain instanceof ENamed_type) {
				if (domain instanceof EEntity_definition) {
					t = ((ENamed_type)domain).getName(null);
				} else {
					result += a + g;
					return result;
				}
			}
//System.out.println("completed source for explicit_attribute branch");
		}
		if (!inverse) {
			result += a + g;
			if (!simple) {
				result += println(" -> ");
				result += t;
			}
		} else {
			result += t;
			result += println(" <- ");
			result += a + g;
		}
		return result;
	}

	protected static String parseElement(EEntity_constraint constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
		String result = "";
		String a = constraint.getDomain(null).getName(null);
		if (MappingDocOperations.isSelectInside((EEntity) constraint)) {
			result += println(a+" = ");
			result += parseElement(constraint.getAttribute(null), path, simple, inverse);
		} else {
			result += parseElement(constraint.getAttribute(null), path, simple, inverse);
			result += println(" => ");
			result += a;
		}
		return result;
	}

	protected static String parseElement(EAttribute_value_constraint constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
		String result = "";
		String constraintCondition = " = ";

		if (constraint instanceof EEnumeration_constraint) {
			EEnumeration_constraint c = (EEnumeration_constraint)constraint;
			if (c.testConstraint_value(null)) {
				constraintCondition += c.getConstraint_value(null);
			}
		} else if (constraint instanceof EString_constraint) {
			EString_constraint c = (EString_constraint)constraint;
			if (c.testConstraint_value(null)) {
				constraintCondition += "'" + c.getConstraint_value(null) + "'";
			}
		} else if (constraint instanceof ELogical_constraint) {
			ELogical_constraint c = (ELogical_constraint)constraint;
			if (c.testConstraint_value(null)) {
				constraintCondition += c.getConstraint_value(null);
			}
		} else if (constraint instanceof EReal_constraint) {
			EReal_constraint c = (EReal_constraint)constraint;
			if (c.testConstraint_value(null)) {
				constraintCondition += c.getConstraint_value(null);
			}
		} else if (constraint instanceof EBoolean_constraint) {
			EBoolean_constraint c = (EBoolean_constraint)constraint;
			if (c.testConstraint_value(null)) {
				if (c.getConstraint_value(null)) {
					constraintCondition += ".TRUE.";
				} else {
					constraintCondition += ".FALSE.";
				}
			}
		} else if (constraint instanceof EInteger_constraint) {
			EInteger_constraint c = (EInteger_constraint)constraint;
			if (c.testConstraint_value(null)) {
				constraintCondition += c.getConstraint_value(null);
			}
		}


		result += parseElement(constraint.getAttribute(null), path, true, inverse);
		result += constraintCondition;
		return result;
	}
*/
  /**
   *	A helper method that allows to hide details of attribute constraints parsing.
   *
   */
/*	public static String parseElement(EEntity constraint,
									AAttribute_mapping_path_select path,
									boolean simple,
									boolean inverse) throws SdaiException {
//System.out.println("Starting parseElement(EEntity) with "+constraint);
//System.out.println("Stack trace was: ");
//Thread.dumpStack();
		if (MappingDocOperations.isElementInPath(constraint, path, globalThisPath)) {
			return "";
		}
		if (constraint instanceof EAttribute) {
			return parseElement((EAttribute) constraint, path, simple, inverse);
		}
		if (constraint instanceof EAttribute_value_constraint) {
			return parseElement((EAttribute_value_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof EInverse_attribute_constraint) {
			return parseElement((EInverse_attribute_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof EPath_constraint) {
			return parseElement((EPath_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof EEntity_constraint) {
			return parseElement((EEntity_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof EInstance_constraint) {
			return parseElement((EInstance_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof EAggregate_member_constraint) {
			return parseElement((EAggregate_member_constraint) constraint, path, simple, inverse);
		}
		if (constraint instanceof ESelect_constraint) {
			return parseElement((ESelect_constraint) constraint, path, simple, inverse);
		}
		System.out.println("PROBLEM: unknown constraint type! parseElement(EEntity)");
		System.out.println("constraint itself: "+ constraint);
		System.out.println("constraint class is "+ constraint.getClass());
		return "UNKNOWN";
	}
*/
}
