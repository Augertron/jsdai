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

 /*
 
 changing validation messages:
 

Instead of
violated where rule "WR2" in entity "entity_with_rules"
write
entity_with_rule.WR2 violated

Instead of
violated SIZEOF in where rule "WR2" in entity "entity_with_rules" for
instance #1
write
entity_with_rules.WR2 #1 violation caused here (outmost SIZEOF)

Instead of
belongs to the instance set violating SIZEOF in where rule "WR4" in
entity "entity_with_rules" for instance #1"
write
entity_with_rules.WR4 #1 violation may be caused here (outmost SIZEOF)"

 
test example - console: ######################################################################################################### 
 

'Validate' program. Copyright 1999-2005, LKSoftWare GmbH
--- Exchange structure: G:\eclipse\runtime-EclipseApplication\test_sizeof\test_sizeof.p21
--- Imported to the repository: 
--- Reading time=0sec
count of instances in model "default": 14
#1=ENTITY_WITH_RULES((#2,#3,#4,#5,#6,#7,#8,#9));
 	   Violation of where rule "WR2" in entity "entity_with_rules" 
 	      Instances set making SIZEOF value nonzero:
 	      #2=ANOTHER_ENTITY(2);
 	   Violation of where rule "WR4" in entity "entity_with_rules" 
 	      Instances set violating SIZEOF equation:
 	      #4=ANOTHER_ENTITY(4);
 	      #5=ANOTHER_ENTITY(4);
#10=ENTITY_WITH_RULES((#11,#12,#13,#14));
 	   Violation of where rule "WR1" in entity "entity_with_rules" 
 	      Instances set making SIZEOF value nonzero:
 	      #11=ANOTHER_ENTITY(1);
 	   Violation of where rule "WR3" in entity "entity_with_rules" 
 	      Instances set violating SIZEOF equation:
 	      #12=ANOTHER_ENTITY(3);
 	      #13=ANOTHER_ENTITY(3);
 	   An outcome from where rule "WR5" in entity "entity_with_rules" is an error: VA_NVLD - Value invalid. Error: division by zero.
count of global rules in schema instance "schema1": 1
For the global rule "number_rules" validation gives FALSE
 	   where rule "WR2" is violated
 	      Instances set making SIZEOF value nonzero:
 	      #6=ANOTHER_ENTITY(6);
 	   where rule "WR4" is violated
 	      Instances set violating SIZEOF equation:
 	      #8=ANOTHER_ENTITY(8);
 	      #9=ANOTHER_ENTITY(8);
 	   An outcome from where rule "WR5" is an error: VA_NVLD - Value invalid. Error: division by zero.
count of erroneous instances: 2
count of violated global rules: 1
count of violated uniqueness rules: 0
--- Validation time=0sec


 
 
test example - Problems view: ##########################################################################################

Error	test_sizeof	test_sizeof.p21	global rule "number_rules":  where rule "WR2" is violated	line 0	May 16, 2006 6:28:58 AM	2635
Error	test_sizeof	test_sizeof.p21	global rule "number_rules":  where rule "WR4" is violated	line 0	May 16, 2006 6:28:58 AM	2637
Error	test_sizeof	test_sizeof.p21	global rule "number_rules":  where rule "WR5" caused an error: VA_NVLD - Value invalid. Error: division by zero	line 0	May 16, 2006 6:28:58 AM	2640
Error	test_sizeof	test_sizeof.p21	violates SIZEOF in where rule "WR1" in entity "entity_with_rules" for instance #10	line 31	May 16, 2006 6:28:58 AM	2630
Error	test_sizeof	test_sizeof.p21	violates SIZEOF in where rule "WR2" in entity "entity_with_rules" for instance #1	line 22	May 16, 2006 6:28:58 AM	2625
Error	test_sizeof	test_sizeof.p21	violates SIZEOF in where rule "WR2" of global rule "number_rules"	line 26	May 16, 2006 6:28:58 AM	2636
Error	test_sizeof	test_sizeof.p21	violates where rule "WR1" in entity "entity_with_rules"	line 30	May 16, 2006 6:28:58 AM	2629
Error	test_sizeof	test_sizeof.p21	violates where rule "WR2" in entity "entity_with_rules"	line 21	May 16, 2006 6:28:58 AM	2624
Error	test_sizeof	test_sizeof.p21	violates where rule "WR3" in entity "entity_with_rules"	line 30	May 16, 2006 6:28:58 AM	2631
Error	test_sizeof	test_sizeof.p21	violates where rule "WR4" in entity "entity_with_rules"	line 21	May 16, 2006 6:28:58 AM	2626
Error	test_sizeof	test_sizeof.p21	 where rule "WR5" in entity "entity_with_rules" caused an error: VA_NVLD - Value invalid. Error: division by zero	line 30	May 16, 2006 6:28:58 AM	2634
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10	line 32	May 16, 2006 6:28:58 AM	2632
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10	line 33	May 16, 2006 6:28:58 AM	2633
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR4" in entity "entity_with_rules" for instance #1	line 24	May 16, 2006 6:28:58 AM	2627
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR4" in entity "entity_with_rules" for instance #1	line 25	May 16, 2006 6:28:58 AM	2628
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR4" of global rule "number_rules"	line 28	May 16, 2006 6:28:58 AM	2638
Warning	test_sizeof	test_sizeof.p21	belongs to the instance set violating SIZEOF in where rule "WR4" of global rule "number_rules"	line 29	May 16, 2006 6:28:58 AM	2639

test example - cleaned messages in Problems view: ##############################################################################

- errors - 

global rule "number_rules":  where rule "WR2" is violated
global rule "number_rules":  where rule "WR4" is violated 
global rule "number_rules":  where rule "WR5" caused an error: VA_NVLD - Value invalid. Error: division by zero 
violates SIZEOF in where rule "WR1" in entity "entity_with_rules" for instance #10 
violates SIZEOF in where rule "WR2" in entity "entity_with_rules" for instance #1
violates SIZEOF in where rule "WR2" of global rule "number_rules"
violates where rule "WR1" in entity "entity_with_rules"
violates where rule "WR2" in entity "entity_with_rules"
violates where rule "WR3" in entity "entity_with_rules"
violates where rule "WR4" in entity "entity_with_rules"
where rule "WR5" in entity "entity_with_rules" caused an error: VA_NVLD - Value invalid. Error: division by zero

- warnings -

belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10 
belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10
belongs to the instance set violating SIZEOF in where rule "WR4" in entity "entity_with_rules" for instance #1
belongs to the instance set violating SIZEOF in where rule "WR4" in entity "entity_with_rules" for instance #1
belongs to the instance set violating SIZEOF in where rule "WR4" of global rule "number_rules"
belongs to the instance set violating SIZEOF in where rule "WR4" of global rule "number_rules"


test example - expected new messages in Problems view: ##########################################################################

- errors -

number_rules.WR2 violated
number_rules.WR4 violated
number_rules.WR5 caused an error: VA_NVLD - Value invalid. Error: division by zero
entity_with_rules.WR1 #10 violation caused here (outmost SIZEOF)
entity_with_rules.WR2 #1  violation caused here (outmost SIZEOF)
number_rules.WR2 violation caused here (outmost SIZEOF)
entity_with_rules.WR1 violated
entity_with_rules.WR2 violated
entity_with_rules.WR3 violated
entity_with_rules.WR4 violated
entity_with_rules.WR5 caused an error: VA_NVLD - Value invalid. Error: division by zero

- warnings -

entity_with_rules.WR3 #10 violation may be caused here (outmost SIZEOF)
entity_with_rules.WR3 #10 violation may be caused here (outmost SIZEOF)
entity_with_rules.WR4 #1 violation may be caused here (outmost SIZEOF)
entity_with_rules.WR4 #1 violation may be caused here (outmost SIZEOF)
number_rules.WR4 violation may be caused here (outmost SIZEOF)
number_rules.WR4 violation may be caused here (outmost SIZEOF)


 
 */

package jsdai.express_compiler.p21_editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class ValidationMessageParser {


  protected static final int SEVERITY_INFO    = 1;
  protected static final int SEVERITY_WARNING = 2;
	protected static final int SEVERITY_ERROR   = 3;


	String previous_line = null;


	boolean fImport_messages = true;
	boolean fValidate_messages = false;
	boolean fImport_error = false;
	boolean fImport_error_instance_line = false;
	boolean fImport_error_attribute_line = false;
	boolean fInstance_validation_error = false;
	boolean fGlobal_rule_validation_error = false;
	boolean fUniqueness_rule_validation_error = false;
	boolean fWhere_rule_in_global_rule_validation_error = false;
	boolean fWhere_rule_in_entity_validation_error = false;
	boolean fSizeOfError = false;
	boolean fSizeOfWarning = false;
	boolean fStarts_at_0 = false;

	String fError_description = "";
	String fError_description_gr = "";
	String fError_description_wr = "";
	String fInstance_str = "";
	String fInstance_str_x = "";
	String fDescription_str_entity_wr = "";


	int fLine_number = 0;
	IProject fProject;


		
//	public void parseValidationMessages(IProject project, IFile validated_file, String validation_output, IPath validated_path) throws CoreException {
	public void parseValidationMessages(IProject project, IFile validated_file, String validation_output, IPath validated_path, IProgressMonitor monitor) throws CoreException {

		monitor.beginTask("Parsing validation messages", IProgressMonitor.UNKNOWN);
	
		fProject = project;
//		StringTokenizer tokenizer = new StringTokenizer(validation_output, "\n"); //$NON-NLS-1$
		StringTokenizer tokenizer = new StringTokenizer(validation_output, "\n\r"); //$NON-NLS-1$
  	String current_line = null;
  		while (tokenizer.hasMoreElements()) {
  			previous_line = current_line;
			current_line = (String) tokenizer.nextElement();
			if (current_line != null) {
//				System.out.println("parsing line: "  + current_line);
	monitor.setTaskName(("Parsing validation messages: " + current_line));
				processLine(validated_file, current_line, validated_path);
			} else {
//				System.out.println("parsing line NULL");
			}
  		}
  		monitor.setTaskName(("Parsing validation messages - done: " + current_line));
	}

	
	
	
	//	public boolean processLine(String line, ErrorParserManager eoParser, int inheritedSeverity) {

	void processLine(IFile validated_file, String line, IPath compiled_path) throws CoreException {


		// boolean starts_with_ERROR = line.startsWith("Error:");
		// boolean starts_with_VIOLATION = line.trim().startsWith("Violation");
	
	
		int original_line_length;
		int trimmed_line_length;
		
		original_line_length = line.length();
		line = line.trim();
		trimmed_line_length = line.length();
		if (original_line_length > trimmed_line_length) {
			fStarts_at_0 = false;
		} else {
			fStarts_at_0 = true;
		}
		
		if (line.startsWith("'Validate' program.")) {
			// the first line of the validation messages, not sure what it is good for, perhaps for some initializations
		} else 
		if (line.startsWith("--- Exchange structure: ")) {
			// the 1st line of the import part (inside Validation)
			fImport_messages = true;
			fValidate_messages = false;
			fImport_error = false;
			fImport_error_instance_line = false;
			fImport_error_attribute_line = false;
			fInstance_validation_error = false;
			fGlobal_rule_validation_error = false;
			fUniqueness_rule_validation_error = false;
			fWhere_rule_in_global_rule_validation_error = false;
			fWhere_rule_in_entity_validation_error = false;
			fSizeOfError = false;
			fSizeOfWarning = false;
			fError_description = "";
			fError_description_gr = "";
			fError_description_wr = "";
			fInstance_str = "";
			fInstance_str_x = "";
			fDescription_str_entity_wr = "";
			fLine_number = 0;
		} else
		if (line.startsWith("Error:")) { 
			// the 1st line of import error message, contains also the 2nd line and (possibly) the 3rd line - attribute name
			
			if (fImport_error) {
				// already in the import error, that means the previous error ends, a new one starts here
				setMarker(validated_file, fError_description, fLine_number, SEVERITY_WARNING);
				fError_description = "";
				fLine_number = 0;
			}
			
			if (!fImport_messages) {
				// this line is out of sequence, perhaps an accidental debugging print
				return;
			}
			fImport_error = true;
			fImport_error_instance_line = false;
			// we might get the description at this time, why not (later we might add the attribute name to the description)
			String description = line.substring(7, line.length()-1);
			if (description.endsWith(".")) {
				description = description.substring(0,description.length()-1);
			}
			fError_description = description;
		} else
		if (line.startsWith("Instance: #") || line.startsWith("   Instance: #")) {
			// the 2nd line of import error message
			// here we need to get the actual line number of the instance in the p21 file
			
			if (!fImport_error) {
				// something wrong here, this line is out of sequence, perhaps an accidental debugging print
				return;
			}

			fImport_error_instance_line = true;
			fImport_error_attribute_line = false;

//			int line_number = 0;
      int instance_start = line.indexOf("#");
			String instance_str = null;
			if (instance_start > 0) {
				instance_str = line.substring(instance_start);
				instance_str += "=";
			}

			fLine_number = findLineInFile(validated_file, instance_str);

		} else
		if (line.startsWith("Attribute:")) {
			// the 3rd line of import error message
			
			if (!fImport_error) {
				// something wrong here, this line is out of sequence, perhaps an accidental debugging print
				return;
			}
			if (!fImport_error_instance_line) {
				// something still wrong here, this line is out of sequence, perhaps an accidental debugging print
				return;
			}
			
			fImport_error_instance_line = false;
			fImport_error_attribute_line = true;
			/*
			we might want to add the name of the attribute to the description of the error
				lets convert from:
					Attribute: a2
				to:
					attribute a2:
				and add this string to the beginning of the error description
			
			    attribute te: a2:
			 
			*/
			String attr_name_str = "attribute " + line.substring(11) + ": ";
			fError_description = attr_name_str + fError_description;
		} else 
		if (line.startsWith("--- Imported to the repository:")) {
			// indicates the ending of the import (one more line only)
	
			// mark if there is a pending import error
			if (fImport_error) {
        setMarker(validated_file, fError_description, fLine_number, SEVERITY_WARNING);
				fError_description = "";
				fLine_number = 0;

				fImport_error = false;
				fImport_error_instance_line = false;
				fImport_error_attribute_line = false;
			}

		} else 
		if (line.startsWith("--- Reading time=")) {
			// the last line of the import part, validation will start
			fImport_messages = false;
			fImport_error = false;
			fValidate_messages = true;
		} else 
		if (line.startsWith("count of instances in model")) {
			// the 1st line of actual validation messages? 
			// 
		} else 
		if (line.startsWith("count of global rules in schema instance")) {
			// the 2nd line of actual validation, if present
		} else
		if (line.startsWith("#")) {
			/*
					check if it is #12..3= format, that is, if it starts with the instance number
					if so, it may be: 
					- the 1st line of Validate instance violation 
					- the 3rd and further lines of Validate uniqueness rule 
					NEW:
					- instances violating where rule in a global rule, in entity or in type (our new special SizeOfExt)
			*/
		
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			int inst_number_end = line.indexOf('=');
			if (inst_number_end < 2) {
				// common, at least one digit has to be in the instance number! So this is a garbage line
				return;
			}
			String number_string = line.substring(1,inst_number_end);
	
	
			
			try {
//				int dummy = Integer.parseInt(number_string, 10);
			} catch (NumberFormatException e) {
				// this is garbage, skip the line
				return;
			}

				
			// hmm, it still may be garbage, if the first character was -:  #-23=
			if (number_string.startsWith("-")) {
				return;
			}
			// ok, this string is acceptable, let's try to find the instance line in the p21 file
			String instance_str = "#" + number_string + "=";
			int line_number = findLineInFile(validated_file, instance_str);
			if (fStarts_at_0) {
				fInstance_str = "#" + number_string;
				fLine_number = line_number;
				fWhere_rule_in_global_rule_validation_error = false;
				fWhere_rule_in_entity_validation_error = false;
			}

			if (fUniqueness_rule_validation_error) {
				// this is the 3rd or further line of Validate uniqueness rule violation
				// set a marker for each such line immediately, the description is already formed
	      setMarker(validated_file, fError_description, line_number, SEVERITY_ERROR);
			} else 
			if (fWhere_rule_in_global_rule_validation_error) {
				// violation of where rule, either error or warning, may be multiple instances
				String the_error_description;
				if (fSizeOfError) {

// violates SIZEOF in where rule "WR2" of global rule "number_rules"
// number_rules.WR2 violation caused here (outmost SIZEOF)

// belongs to the instance set violating SIZEOF in where rule "WR4" of global rule "number_rules"
// number_rules.WR4 violation may be caused here (outmost SIZEOF)


//old					the_error_description = "violates SIZEOF in " + fError_description_wr + " of " + fError_description_gr;
					the_error_description = fError_description_gr + "." + fError_description_wr + " violation caused here (outmost SIZEOF)";
					setMarker(validated_file, the_error_description, line_number, SEVERITY_ERROR);
				} else 
				if (fSizeOfWarning) { 
//old					the_error_description = "belongs to the instance set violating SIZEOF in " + fError_description_wr + " of " + fError_description_gr;
					the_error_description = fError_description_gr + "." + fError_description_wr + " violation may be caused here (outmost SIZEOF)";
					setMarker(validated_file, the_error_description, line_number, SEVERITY_WARNING);
				} else {
					the_error_description = "";
					// something wrong here					
					// setMarker(validated_file, "KU-KU", line_number, SEVERITY_ERROR);
				} 
//	      setMarker(validated_file, fError_description, line_number, SEVERITY_ERROR);
			} else 
			if (fWhere_rule_in_entity_validation_error) {

// violates SIZEOF in where rule "WR1" in entity "entity_with_rules" for instance #10 
// entity_with_rules.WR1 #10 violation caused here (outmost SIZEOF)

// belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10 
// entity_with_rules.WR3 #10 violation may be caused here (outmost SIZEOF)

				// violation of where rule, either error or warning, may be multiple instances
				String the_error_description;
				if (fSizeOfError) {
//old					the_error_description = "violates SIZEOF in " + fDescription_str_entity_wr + " for instance " + fInstance_str_x;
					the_error_description = fDescription_str_entity_wr + " " + fInstance_str_x + " violation caused here (outmost SIZEOF)";
					setMarker(validated_file, the_error_description, line_number, SEVERITY_ERROR);
				} else 
				if (fSizeOfWarning) { 
//old					the_error_description = "belongs to the instance set violating SIZEOF in " + fDescription_str_entity_wr + " for instance " + fInstance_str_x;
					the_error_description = fDescription_str_entity_wr + " " + fInstance_str_x + " violation may be caused here (outmost SIZEOF)";
					setMarker(validated_file, the_error_description, line_number, SEVERITY_WARNING);
				} else {
					the_error_description = "";
					// something wrong here					
					// setMarker(validated_file, "KU-KU", line_number, SEVERITY_ERROR);
				} 


			} else {
				// this is the 1st line of Validate instance violation
				// if there was already Validate instance violation before that, nothing is pending to mark, so ok
				fInstance_validation_error = true;
//				fLine_number = line_number;
			}

		
		} else
		if (line.startsWith("Violation")) {
			// the 2nd and further lines of Validate instance violation
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			if (!fInstance_validation_error) {
				// not in the Validate instance violation, out of sequence garbage
				return;
			}
			/*
				ok, let's mark the error
				first, what description
				
				
				Do we need to clarify which are validation error as which are import errors?
				We could mark import errors as warnnings in yellow,
				and validation errors - in red, they probably duplicate import warnings anyway.
				Then we do not need to specify that it is a validation error, lets make this description:
					

        for:
					Violation for attribute "a2": at least one element of array is missing
        make:        					
					attribute a2: at least one element of array is missing
					
				for 
			   Violation of where rule "sizeof_function" in entity "aggr_functions" 
				make
					violates where rule sizeof_function in entity aggr_functions			
	
			*/
			String description_str = "violation detected";
			if (line.startsWith("Violation for attribute")) {
				int attribute_ends = line.indexOf(':');
	      if (attribute_ends > 25) {
	      	description_str = "attribute " +  line.substring(25, attribute_ends-1) + ": " + line.substring(attribute_ends + 2);
				} else {
					// again, it is just a garbage line, although very similar to the real deal
					return;
				}
			} else 
			if (line.startsWith("Violation of where rule")) {
//				description_str = "violates "  + line.substring(13).replace('\"','');
//				char c1 = "";
//				description_str = "violates "  + line.substring(13).replace('\"',c1);
//				description_str = "violates "  + line.substring(13);
				fWhere_rule_in_entity_validation_error = true;
//				fDescription_str_entity_wr = line.substring(13);
				int wr_start = line.indexOf('\"');
				int wr_end   = line.indexOf("\" in entity");
				int e_start  = wr_end + 12;
				int e_end    = line.lastIndexOf('\"');
		
				if (wr_start >= 0 && wr_end > wr_start && e_start > wr_end && e_end > e_start) {
					fDescription_str_entity_wr = line.substring(e_start+1, e_end) + "." + line.substring(wr_start+1, wr_end);
				} else {
					fDescription_str_entity_wr = "INTERNAL ERROR #03";
				}
				description_str = fDescription_str_entity_wr + " violated";
				fInstance_str_x = fInstance_str;

// Violation of where rule "WR1" in entity "entity_with_rules" 


// violates SIZEOF in where rule "WR1" in entity "entity_with_rules" for instance #10 
// entity_with_rules.WR1 #10 violation caused here (outmost SIZEOF)

// belongs to the instance set violating SIZEOF in where rule "WR3" in entity "entity_with_rules" for instance #10 
// entity_with_rules.WR3 #10 violation may be caused here (outmost SIZEOF)

// violates where rule "WR1" in entity "entity_with_rules"
// entity_with_rules.WR1 violated


			}			
			// sets the marker immediately, nothing pending is left here			
      setMarker(validated_file, description_str, fLine_number, SEVERITY_ERROR);


		} else
		if (line.startsWith("For the uniqueness rule")) {
			/*
			 	the 1st line of Validate uniqueness rule violation
			 	extract and form description from it, for example, from:

			  For the uniqueness rule "ur1" defined in entity data type "product_definition_shape" validation gives FALSE
  
        make:
              
			 	violates uniqueness rule ur1 
			
				the rule ur1 is probably in the entity of the instance type (or its supertype) so we might skip the name of the entity, or:

			 	violates uniqueness rule ur1 in product_definition_shape

			*/
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			String description_str = "violates uniqueness rule";
			int middle_section = line.indexOf("defined in entity data type");
			if (middle_section < 25) { // number probably too low
				// garbage
				return;
			}
			int end_section = line.indexOf("validation gives FALSE");
			if (end_section < 50)	{ // number probably way too low
				// garbage
				return;
			}
			description_str += " " + line.substring(25, middle_section - 2) + " in entity " + line.substring(middle_section + 29, end_section - 2);
			fError_description = description_str;
			if (fInstance_validation_error) {
				// it could have been, previous errors on instances, not sure, but nothing pending to mark
				fInstance_validation_error = false;
			}					
			fUniqueness_rule_validation_error = true;
		
		} else
		if (line.equalsIgnoreCase("instances not conforming to the validation:")) {
			// the 2nd line of Validate uniqueness rule message
		} else
		if (line.equalsIgnoreCase("Instances set making SIZEOF value nonzero:")) {
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			// must be either global rule or entity instance true, both true - internal error, both false - garbage, skip
			if (!((fGlobal_rule_validation_error || fInstance_validation_error) && (!(fGlobal_rule_validation_error && fInstance_validation_error)))) {
				return;
			}
			fSizeOfError = true;
			fSizeOfWarning = false; // just as a reminder
		} else
		if (line.equalsIgnoreCase("Instances set violating SIZEOF equation:")) {
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			// must be either global rule or entity instance true, both true - internal error, both false - garbage, skip
			if (!((fGlobal_rule_validation_error || fInstance_validation_error) && (!(fGlobal_rule_validation_error && fInstance_validation_error)))) {
				return;
			}
			fSizeOfWarning = true; 
			fSizeOfError = false; // just as a reminder
		} else
		if (line.startsWith("For the global rule")) {		
			// the 1st line of Validate global rule violation
			// ok, so reset where_rule, error/warning of set of instances in where rule, instances in where_rule
			
		
		  /*
		  	 I don't have precise format at this point that would allow me to extract the names of the global rule and where rules
		  	 for now, just make this (perhaps very long?) description:
		  	 global_rule_text + where_rule_1 text
		  	 global_rule_text + where_rule_2 text
		  	 ... 

 validation gives FALSE

For the global rule "nice_family" validation gives FALSE
 	   where rule "WR1" is violated			


		  */
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			if (fInstance_validation_error) {
				// it could have been, previous errors on instances, not sure, but nothing pending to mark
				fInstance_validation_error = false;
			}					
			if (fUniqueness_rule_validation_error) {
				// it could have been, previous errors, not sure, but nothing pending to mark
				fUniqueness_rule_validation_error = false;
			}					
			//fError_description = "global rule " + line.substring(20,line.length()-23) + ": ";
//			fError_description_gr = "global rule " + line.substring(20,line.length()-23);
  		int gr_start = line.indexOf('\"');
  		int gr_end   = line.lastIndexOf('\"');
  		if (gr_start >= 0 && gr_end > gr_start) {
  			fError_description_gr = line.substring(gr_start+1, gr_end);
      } else {
      	fError_description_gr = "INTERNAL ERROR #02";
      }
			fError_description = fError_description_gr + ".";
			
			fGlobal_rule_validation_error = true;
		
		} else
		if (line.startsWith("where rule")) {
			// the 2nd and further lines of Validate global rule violation
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}
			if (!fGlobal_rule_validation_error) {
				// garbage
				return;
			}
			fWhere_rule_in_global_rule_validation_error = true;
//			fError_description_wr = line.substring(0,line.length()-12);
  		int wr_start = line.indexOf('\"');
  		int wr_end   = line.lastIndexOf('\"');
  		if (wr_start >= 0 && wr_end > wr_start) {
  			fError_description_wr = line.substring(wr_start+1, wr_end);
      } else {
      	fError_description_wr = "INTERNAL ERROR #01";
      }
//			fError_description += fError_description_wr + " violated";
//      setMarker(validated_file, fError_description + " " + line, 0, SEVERITY_ERROR);
      setMarker(validated_file, fError_description + fError_description_wr + " violated", 0, SEVERITY_ERROR);



		} else 
		if (line.startsWith("An outcome from where rule")) {	
			// this indicates an exception while validating a where rule, it should be marked as an error as well
			if (!fValidate_messages) {
				// not in the Validate section, out of sequence garbage
				return;
			}

// global rule "number_rules":  where rule "WR5" caused an error: VA_NVLD - Value invalid. Error: division by zero 
// number_rules.WR5 caused an error: VA_NVLD - Value invalid. Error: division by zero

// where rule "WR5" in entity "entity_with_rules" caused an error: VA_NVLD - Value invalid. Error: division by zero
// entity_with_rules.WR5 caused an error: VA_NVLD - Value invalid. Error: division by zero

			if (fGlobal_rule_validation_error) {
				// fWhere_rule_in_global_rule_validation_error = true;  // not really needed?
//			fError_description_wr = line.substring(0,line.length()-12);
				int wr_start = line.indexOf("rule \"") + 5;
				int wr_end = line.indexOf("\" is an") + 1;
				int wr_description_start = wr_end + 14;
//old				String fError_description_wr2 = fError_description + " where rule " + line.substring(wr_start, wr_end) + " caused an error: " + line.substring(wr_description_start, line.length()-1);
				String fError_description_wr2 = fError_description + line.substring(wr_start+1, wr_end-1) + " caused an error: " + line.substring(wr_description_start, line.length()-1);

//  	    setMarker(validated_file, fError_description + line, 0, SEVERITY_ERROR);
  	    setMarker(validated_file, fError_description_wr2, 0, SEVERITY_ERROR);
			 } else 
			 if (fInstance_validation_error) {


// An outcome from where rule "WR5" in entity "entity_with_rules" is an error: VA_NVLD - Value invalid. Error: division by zero.

				int wr_start = line.indexOf('\"');
				int wr_end   = line.indexOf("\" in entity");
				int e_start  = wr_end + 12;
				int e_end    = line.indexOf("\" is an error");
				int wr_description_start = e_end + 15;
		
				String fError_description_wr2;
				if (wr_start >= 0 && wr_end > wr_start && e_start > wr_end && e_end > e_start) {
					fError_description_wr2 = line.substring(e_start+1, e_end) + "." + line.substring(wr_start+1, wr_end) + " caused an error: " + line.substring(wr_description_start, line.length()-1);
				} else {
					fError_description_wr2 = "INTERNAL ERROR #04";
				}
	
	     

//				int wr_start = line.indexOf("rule \"") + 5;
//				int wr_end = line.indexOf("\" is an") + 1;  // take longer substring, including the name of the entity as well
//old				String fError_description_wr2 = fError_description + " where rule " + line.substring(wr_start, wr_end) + " caused an error: " + line.substring(wr_description_start, line.length()-1);
//				String fError_description_wr2 = fError_description + " where rule " + line.substring(wr_start, wr_end) + " caused an error: " + line.substring(wr_description_start, line.length()-1);
//  		    setMarker(validated_file, fError_description + line, 0, SEVERITY_ERROR);
  		    setMarker(validated_file, fError_description_wr2, fLine_number, SEVERITY_ERROR);
			 }
		} else 
		if (line.startsWith("count of erroneous instances")) {
			// indicates the end of Validate, a couple of similar lines remaining
		} else 
		if (line.startsWith("count of violated global rules")) {
			// indicates the end of Validate, one more similar line  is remaining
		} else 
		if (line.startsWith("count of violated uniqueness rules")) {
			// indicates the end of Validate, the very last line remaining
		} else 
		if (line.startsWith("--- Validation time=")) {
			// it is the last line of Validate
		} else {
			// unrecognized line, may be a debugging print or not yet supported type of error message
		}			

	
	} // end of method
		

	protected void setMarkerOld(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
	
		Map attributes = new HashMap();
    
		switch (severity) {
			case SEVERITY_ERROR:
				attributes.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
				break;
			case SEVERITY_WARNING:
				attributes.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_WARNING));
				break;
			case SEVERITY_INFO:
				attributes.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_INFO));
				break;
			default:
				// not possible, it's not mine, ignore it, but perhaps report an internal problem
				break;
		} // switch
	
	MarkerUtilities.setLineNumber(attributes, line_number);
    MarkerUtilities.setMessage(attributes, message);
    if (file_with_the_problem != null) {
    	MarkerUtilities.createMarker(file_with_the_problem, attributes, IMarker.PROBLEM);
    }
    }


	protected void setMarker(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
	
    if (file_with_the_problem != null) {

			IMarker marker = file_with_the_problem.createMarker("net.jsdai.express_compiler.p21problem");
		
			switch (severity) {
				case SEVERITY_ERROR:
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					break;
				case SEVERITY_WARNING:
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
					break;
				case SEVERITY_INFO:
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
					break;
				default:
					break;
			}
			marker.setAttribute(IMarker.MESSAGE, message);
			if (fProject != null) {
				marker.setAttribute("project", fProject.getName());
			} else {
				marker.setAttribute("project", "P21 validation run not from express project!!!");
			}
			marker.setAttribute(IMarker.LINE_NUMBER, line_number);

  }
	
	}



//####################################
	
	
	//	public boolean processLine(String line, ErrorParserManager eoParser, int inheritedSeverity) {

	void processLine(HashMap express_files, String line) throws CoreException {

//if (line != null) {
//	System.out.println("<> parsing line: " + line);
//} else {
//	System.out.println("<> parsing line: NULL");
//}
		
		
/*		
		Known patterns.


single-line messages:

ERROR: ...

NOTE: in the case of a single line error message, the file name is not available, it should be supplied internally,
because internally it must be known to run the compiler on it.


NOTE: there are arrors without line numbers, at least some of them are duplicating error messages with line numbers,
but perhaps not all of them. To do something abaut it in the compiler, some formatting of the messages is needed, perhaps.

multi-line messages:

ERROR in file E:\eclipse\runtime-workbench-workspace\ZZZ_333\Express files\cartesian_dot.exp: 
referenced (line 12, column 10) named type "huhu" not found


to ignore:

1 error found in pass 3.

		
		
		
		
*/		

		boolean starts_with_ERROR = line.startsWith("ERROR:");

//System.out.println("<>ERROR parser, line: " + line);
//System.out.println("<>ERROR parser, previous line: " + eoParser.getPreviousLine());

		if (starts_with_ERROR) { // will not happen
// System.out.println("<>ERROR parser, line: " + line);
//			String fileName = "test_schema.exp";

//			String fileName = "E:/eclipse/runtime-workbench-workspace/aaaaa5/express/test_schema.exp";

//			String fileName = "E:/eclipse/runtime-workbench-workspace/ZZZ_333/Express files/cartesian_dot.exp";
			// String fileName = null;
			
			// try to get selected something
			
//			String fileName = ExpressCorePlugin.getCompiledExpressFileName();	
			
//			IFile file = eoParser.findFilePath(fileName);

// IFile file = compiled_file;
IFile file = null; // will not happen

// seems that fileName not needed here			
/*
			String fileName = null;
			if (file != null) {
				fileName = file.getName();
			} else {
				fileName = compiled_path.lastSegment(); 
			}
*/			
			// System.out.println("PARSING ERRORS - IFile: " + file);			
			int num = -1;
			int num2 = line.indexOf("line");
			int num3 = line.indexOf(",");
			if (num2 > 0) {
				String line_number = line.substring(num2+6,num3);
				num = Integer.parseInt(line_number);
//				int severity = inheritedSeverity;
			
				int not_found_nr = line.indexOf("not found");
			    String not_found_str = null;
				int encountered_nr = line.indexOf("Encountered");
				int at_line = line.indexOf("at line");
				String desc = "problem";
				String varName = null;
                int var_num1 = line.indexOf(". ");
				if (var_num1 > 0) {
					int var_num2 = line.indexOf(" - ", var_num1+1);
					if (var_num2 > 0) {
						varName = line.substring(var_num1+2, var_num2);
					} else if ((encountered_nr > 0) && (at_line > 0)) {
						varName = line.substring(encountered_nr+13, at_line-2); 
					} else {
						varName = "unknown name";
					}
					if (not_found_nr > 0) {
						if (var_num2 > 0) {
							   not_found_str = line.substring(var_num2, not_found_nr + 9);
							}
							if (not_found_str != null) {
								desc = varName + not_found_str;
							} else {
								desc = varName + " definition not found";
							}
//						desc = varName + " definition not found";
					} else if (encountered_nr >= 0) {
						desc = "encountered " + varName + ", if the error is not obvious, look for details in the console";
						//						desc = "encountered " + varName + ", the error may be before it";
					} else {
						desc = desc += " with " + varName;
					}

				}
//				eoParser.generateMarker(file, num, desc, severity, varName);
				setMarker(file, desc, num, SEVERITY_ERROR);
			}
		} else {
			// see if it is a two-line error
//			String previous = eoParser.getPreviousLine();
			String previous = previous_line;
			// protection from NullPointerException
			if (previous == null) {
				previous = line;
			}

			boolean previous_starts_with_ERROR = previous.startsWith("ERROR in file");
			if (previous_starts_with_ERROR) {
//System.out.println("<>ERROR parser, previous line: " + previous);
				// XXXXXXX here:   ERROR in file XXXXXXXX:
				String fileName = previous.substring(14, previous.length()-2);
//System.out.println("ERROR parser, file name 2: " + fileName);
//			String fileName = "E:/eclipse/runtime-workbench-workspace/ZZZ_333/Express files/cartesian_dot.exp";


				// !!! need to do something here - to find IFile by the name, or to have them all stored somewhere as IFile
				//TODO tempororily, for testing with single files - WRONG for multiple

//if (express_files.containsKey(fileName)) {
//	System.out.println("Contains key: " + fileName);				
//} else {
//	System.out.println("DOES NOT Contain key: " + fileName);				

//}

				String key = fileName.toLowerCase().replace('\\','$').replace('/','$');
				IFile file = (IFile)express_files.get(key);
//System.out.println("file from HM - key: (" + key + ")");				
//System.out.println("file from HM: " + file);				
//System.out.println("files from HM: " + express_files);				
//				IFile file = eoParser.findFilePath(fileName);
				int num = -1;
				int num2 = line.indexOf("line");
				int num3 = line.indexOf(",");
				if (num2 > 0) {
				String line_number = line.substring(num2+6,num3);
				num = Integer.parseInt(line_number);
//				int severity = inheritedSeverity;

				int not_found_nr = line.indexOf("not found");
			    String not_found_str = null;
				int encountered_nr = line.indexOf("Encountered");
				int at_line = line.indexOf("at line");
				String desc = "problem";
				String varName = null;
                int var_num1 = line.indexOf(". ");
				if (var_num1 > 0) {
					int var_num2 = line.indexOf(" - ", var_num1+1);
					if (var_num2 > 0) {
						varName = line.substring(var_num1+2, var_num2);
					} else if ((encountered_nr > 0) && (at_line > 0)) {
						varName = line.substring(encountered_nr+13, at_line-2); 
					} else {
						varName = "unknown name";
					}
//					varName = line.substring(var_num1+2, var_num2);
					if (not_found_nr > 0) {
						if (var_num2 > 0) {
						   not_found_str = line.substring(var_num2, not_found_nr + 9);
						}
						if (not_found_str != null) {
							desc = varName + not_found_str;
						} else {
							desc = varName + " definition not found";
						}
					} else if (encountered_nr >= 0) {
						desc = "encountered " + varName + ", if the error is not obvious, look for details in the console";
					} else {
						desc = desc += " with " + varName;
					}

				}
				//eoParser.generateMarker(file, num, desc, severity, varName);
        setMarker(file, desc, num, SEVERITY_ERROR);
			}
			
		
		} // two-line error
		
		} // not single line error
		
	
	
	} // end of method
	
	
	int findLineInFile(IFile validated_file, String instance_str) throws CoreException {

		int line_number = 0;
		if (instance_str != null) {
			//FIXME: Replace by regexp, this is just temporary
			if(instance_str.endsWith("=")) {
				instance_str = instance_str.substring(0, instance_str.length() - 1);
			}
			// ok, we need to find this string in the file and get the number of the line there
			try {
				InputStream stream = validated_file.getContents();
	      InputStreamReader fr = new InputStreamReader(stream);
				BufferedReader br =  new BufferedReader(fr);
				for (int count = 1; ;count++) {
				  String current_line = br.readLine();
					if (current_line == null) break;
					if (current_line.startsWith(instance_str)) {
						line_number = count;
						break;
					}
				}
   
				stream.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return line_number;
		}
    return 0;
	}
	
	
	
	
} // end of class



