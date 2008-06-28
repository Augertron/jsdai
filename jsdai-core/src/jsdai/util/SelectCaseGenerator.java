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

package jsdai.util;

/*

C.7	Select Case Generator

This example jsdai application defines the algorithm to calculate select cases
It accepts 3 command line parameters: schema name, named type name, optional attribute name
If the named type is an entity, then the third parameter - attribute name - is needed.
If the named type is a defined type, then the third parameter is absent.

*/

import java.util.*;

import jsdai.lang.*;
import jsdai.dictionary.*;

/**
 *	@author: Raimundas Raciunas
 */
public class SelectCaseGenerator
{

	/*
		case 1 is reserved for entity values when additional defined_type parameters are not needed.
		case 1 may be or not be present. Even if it is not present, other cases are still numbered beginning with 2.
		This algorithm determines if case 1 is present, as well as calculates all the other select cases.
	*/
	static void generateSelectCases(EEntity base_type) throws SdaiException {
		Vector cases = new Vector();
		ADefined_type nodes = new ADefined_type(); // non-persistant list to keep the nodes

		EEntity ut = base_type;

		boolean select_present = false;
		while (ut instanceof EDefined_type) {
			EEntity domain = ((EDefined_type)ut).getDomain(null);
			if (domain instanceof ESelect_type) {
				boolean case_1_present = proceed((ESelect_type) domain, nodes, cases);
				removeDuplicateCases(cases);
				printCases(cases, case_1_present);
				select_present = true;
				break;
			}
			ut = domain;
		}
		if (ut instanceof EAggregation_type) {
			EEntity element = ((EAggregation_type)ut).getElement_type(null);
 			for (;;) {
	 			boolean done_something = false;
				if (element instanceof EDefined_type) {
					ut = element;
					element = ((EDefined_type)ut).getDomain(null);
		     	done_something = true;
				} else
				if (element instanceof EAggregation_type) {
					ut = element;
					element = ((EAggregation_type)ut).getElement_type(null);
		     	done_something = true;
				}
			 	if (!done_something) {
					ut = element;
				 	break;
				}
			}	 
			if (ut instanceof ESelect_type) {
				boolean case_1_present = proceed((ESelect_type) ut, nodes, cases);
				removeDuplicateCases(cases);
				printCases(cases, case_1_present);
				select_present = true;
			}
		}
		if (!select_present) {
			System.out.println("\nThe base type of this attribute is not of select or aggregate of select type");
		}
	}

	static boolean proceed(ESelect_type st, ADefined_type nodes, Vector cases) throws SdaiException {
		boolean case_1_present = false;
		ANamed_type ant = st.getSelections(null);
		SdaiIterator iant = ant.createIterator();
		boolean node_added = false;
		while (iant.next()) {
			EEntity entity = (ENamed_type)ant.getCurrentMemberObject(iant);
			while (entity instanceof EDefined_type) {
				EEntity domain = ((EDefined_type)entity).getDomain(null);
				if (!(domain instanceof ESelect_type)) {
					if (!node_added) {
						nodes.addByIndex(nodes.getMemberCount()+1, (EDefined_type)entity);
						node_added = true;
					}
				}
    		entity = domain;
			}
			if (entity instanceof ESelect_type) {
  			boolean case_1_inside = proceed((ESelect_type)entity, nodes, cases);
  			if (!case_1_present) {
  				case_1_present = case_1_inside;
  			} 
			}	else 
			if (entity instanceof EEntity_definition && nodes.getMemberCount() == 0) { 
				case_1_present = true;
			} else {
				SelectCase path = new SelectCase(nodes, entity);	
				cases.addElement(path);
			}
			if (node_added){ 
				nodes.removeByIndex(nodes.getMemberCount());
				node_added = false;
			}
		}
		return case_1_present;
	}

	static void removeDuplicateCases(Vector cases) {
		int case_count = cases.size();
    for (int i = 0; i < case_count - 1; i++) {
      SelectCase master = (SelectCase)cases.elementAt(i);  
      for (int j = i + 1; j < case_count; j++) {
	      SelectCase slave = (SelectCase)cases.elementAt(j);  
				boolean identical = master.compare(slave);
        if (identical) {
	        cases.removeElementAt(j); 
					case_count--;
					j--;
				}  
			}
		}
	}

	static void printCases(Vector cases, boolean print_case_1) throws SdaiException {
		/*
		//System.out.println("");
		if (print_case_1) {
			//System.out.println("1. ENTITY");
			//System.out.println("\tEEntity");
		}
    for (int i = 0; i < cases.size(); i++) {
      SelectCase a_case = (SelectCase)cases.elementAt(i);  
      a_case.printCase(i + 2);
	}
	*/		
	}

	static void fillResult(Vector cases, boolean print_case_1, EDefined_type target_type, ArrayList result) throws SdaiException {
		System.out.println("target_type="+target_type);
		// System.out.println("");
		if (print_case_1) {
			System.out.println("returning empty result set.");
			// at least one SELECT selection is entity, so we use the path nr.1, return empty result
			;//return;
		}
    if (target_type == null) {
		System.out.println("target type is null.");
			// the target_type == null means probably that at least one entity is in SELECT, no need to modify result
    	;//return;		
	  }
    for (int i = 0; i < cases.size(); i++) {
      SelectCase a_case = (SelectCase)cases.elementAt(i);
	  System.out.println("a_case>last="+a_case.last());
      if (a_case.last() == target_type) {
		  System.out.println("last matches target type.");
				// take this case, put into result and skip all the others
      	a_case.fillResult(result);
	      // just for debugging:
	    //  a_case.printCase(i + 2);
      	return;
      }
		}		
	}

	// used for debugging only - do not include into part 27
	static void printList(ADefined_type nodes, String header) throws SdaiException {
		for (int i = 1; i <= nodes.getMemberCount(); i++) {
			EDefined_type node = (EDefined_type)nodes.getByIndex(i);
			//System.out.print(node.getName(null) + ", ");
		}
		//System.out.println(header);
	}

	public static void fillSelectArray(EEntity start_type, EDefined_type target_type, ArrayList result) throws SdaiException {
 		Vector cases = new Vector();
		ADefined_type nodes = new ADefined_type(); // non-persistant list to keep the nodes

		EEntity ut = start_type;

		boolean select_present = false;
		while (ut instanceof EDefined_type) {
			EEntity domain = ((EDefined_type)ut).getDomain(null);
			if (domain instanceof ESelect_type) {
				boolean case_1_present = proceed((ESelect_type) domain, nodes, cases);
				removeDuplicateCases(cases);
				fillResult(cases, case_1_present, target_type, result);
				select_present = true;
				break;
			}
			ut = domain;
		}
		if (ut instanceof EAggregation_type) {
			EEntity element = ((EAggregation_type)ut).getElement_type(null);
 			for (;;) {
	 			boolean done_something = false;
				if (element instanceof EDefined_type) {
					ut = element;
					element = ((EDefined_type)ut).getDomain(null);
		     	done_something = true;
				} else
				if (element instanceof EAggregation_type) {
					ut = element;
					element = ((EAggregation_type)ut).getElement_type(null);
		     	done_something = true;
				}
			 	if (!done_something) {
					ut = element;
				 	break;
				}
			}	 
			if (ut instanceof ESelect_type) {
				boolean case_1_present = proceed((ESelect_type) ut, nodes, cases);
				removeDuplicateCases(cases);
				fillResult(cases, case_1_present, target_type, result);
				select_present = true;
			}
		}
		if (!select_present) {
			System.out.println("\nThe base type of this attribute is not of select or aggregate of select type");
		}
	}
}

class SelectCase {
  EDefined_type path[];
  EEntity value_type;

	SelectCase(ADefined_type nodes, EEntity entity) throws SdaiException  {
		int count = nodes.getMemberCount();
		path = new EDefined_type[count];
		for (int i = 1; i <= count; i++) {
			EDefined_type node = (EDefined_type)nodes.getByIndex(i);
			path[i-1] = node;
		}
		value_type = entity;
	}

	int size() {
		return path.length;
	}

	EDefined_type at(int i) {
		return path[i];
	}

	EDefined_type last() {
		return path[path.length-1];
	}

	/*
		methods size() and at() are not really necessary, 
		attributes path and value_type can be accessed directly, is it a better style for p27?
	*/
	boolean compare(SelectCase other) {
  	boolean identical;
  	if (path.length != other.size()) {
	  	identical = false;
		} else {
	  	identical = true;
  	  for (int k = 0; k < path.length; k++) {
				EDefined_type dt1, dt2;
  	    if (path[k] != other.at(k)) {
		      identical = false;
  		    break;  
    		}        
	  	}
		}
		return identical;
	}

	void fillResult(ArrayList result) throws SdaiException {
		for (int i = 0; i < path.length; i++) {
			result.add(path[i]);
		}
	}
	
	/*	
	void printCase(int nr) throws SdaiException {
		String value_type_name = null;
		String java_type_name = null;
		if (value_type instanceof EEntity_definition) {
			value_type_name = "ENTITY";
			java_type_name = "EEntity";
		}	else 
		if (value_type instanceof EInteger_type) {
			value_type_name = "INTEGER";
			java_type_name = "int";
		}	else
		if (value_type instanceof ENumber_type) {
			value_type_name = "NUMBER";
			java_type_name = "double";
		}	else
		if (value_type instanceof EReal_type) {
			value_type_name = "REAL";
			java_type_name = "double";
		}	else
		if (value_type instanceof EString_type) {
			value_type_name = "STRING";
			java_type_name = "String";
		}	else
		if (value_type instanceof ELogical_type) {
			value_type_name = "LOGICAL";
			java_type_name = "int";
		}	else
		if (value_type instanceof EBoolean_type) {
			value_type_name = "BOOLEAN";
			java_type_name = "boolean";
		}	else
		if (value_type instanceof EBinary_type) {
			value_type_name = "BINARY";
			java_type_name = "Binary";
		} else	
		if (value_type instanceof EEnumeration_type) {
			value_type_name = "ENUMERATION";
			java_type_name = "int";
		}	else
		if (value_type instanceof EAggregation_type) {
			value_type_name = "AGGREGATE";
			java_type_name = "Aggregate";
		}
		String coma = "";
		System.out.print(nr + ". " + value_type_name + ": ");
		for (int i = 0; i < path.length; i++) {
			System.out.print(coma + path[i].getName(null));
			coma = ", ";
		}
		System.out.println();
		coma = "";
		System.out.print("\t" + java_type_name + ": ");
		for (int i = 0; i < path.length; i++) {
			String node_name = path[i].getName(null);
			String normalized_node_name = "E" + node_name.substring(0,1).toUpperCase() + node_name.substring(1).toLowerCase();
			System.out.print(coma + normalized_node_name);
			coma = ", ";
		}	
		System.out.println();
	}
	*/

}




