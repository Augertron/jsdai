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

package jsdai.mappingCompiler.util;

import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.util.*;
import jsdai.SMapping_schema.*;
import jsdai.SExtended_dictionary_schema.*;

public class MappingUtilOperations {
	
	/*
	*	******************************************************************
	*							getStartType methods
	*	******************************************************************
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
			} else if (att instanceof EAggregate_member_constraint) {
				a = getEndType((EAggregate_member_constraint) att);
			} else if (att instanceof EEntity_constraint) {
				a = getEndType((EEntity_constraint) att);
			} else if (att instanceof ESelect_constraint) {
				a = getEndType((ESelect_constraint) att);
			} else {
				System.out.println("PROBLEM: getStartType for inverse_attribute_constraint");
				System.out.println("encountered unknown constraint subtype:"+att);
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
	*	******************************************************************
	*							getEndType methods
	*	******************************************************************
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
		if (pe instanceof EExplicit_attribute)
			result = ((EExplicit_attribute) pe).getDomain(null);
		else if (pe instanceof EInverse_attribute)
			result = ((EInverse_attribute) pe).getDomain(null);
		else if (pe instanceof EDerived_attribute)
			result = ((EDerived_attribute) pe).getDomain(null);
		return result;
	}

	public static Object getEndType(EInverse_attribute_constraint pe) throws SdaiException {
		Object a = null;
		if (pe.testInverted_attribute(null)) {
			EEntity att = pe.getInverted_attribute(null);
			a = getStartType((EEntity)att);
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
	
}
