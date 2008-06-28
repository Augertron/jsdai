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

package jsdai.express_compiler.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class ExpressCompilerMessageParser {

  protected static final int SEVERITY_INFO    = 1;
  protected static final int SEVERITY_WARNING = 2;
	protected static final int SEVERITY_ERROR   = 3;


	String previous_line = null;

	boolean fError_to_mark = false;
	boolean fSingle_file_error = false;
	boolean fMulti_file_error = false;
	boolean fSyntax_error = false;
	boolean fMark_columns = false;
  String fSub_line;
  String fLine;	
  int fLine_number = 0;
	int fColumn_number = -1;
	int fColumn_start = -1;
	int fColumn_end = -1;
	IFile fFile;
	int fSeverity = SEVERITY_ERROR;
	String fDescription = "";
	String fEncountered;
	String fChoice_value;
	boolean fSingle_choice;
	int fChoice_count;
	Vector fChoices;
	int fDescription_length_limit = 132;
	IProject fProject;
	
		
	public void parseCompilerMessages(IProject project, IFile compiled_file, String express_compiler_output, IPath compiled_path) throws CoreException {                    
	
		fProject = project;
		StringTokenizer tokenizer = new StringTokenizer(express_compiler_output, "\n"); //$NON-NLS-1$
  	String current_line = null;
  		while (tokenizer.hasMoreElements()) {
  			previous_line = current_line;
			current_line = (String) tokenizer.nextElement();
			if (current_line != null) {
//				System.out.println("parsing line: "  + current_line);
//				processLine(compiled_file, current_line, compiled_path);
				processLine(compiled_file, (HashMap)null, current_line, compiled_path);

			} else {
//				System.out.println("parsing line NULL");
			}
  		}

	}

	public void parseCompilerMessages_old(IFile compiled_file, String express_compiler_output, IPath compiled_path) throws CoreException {
	
		StringTokenizer tokenizer = new StringTokenizer(express_compiler_output, "\n"); //$NON-NLS-1$
  	String current_line = null;
  		while (tokenizer.hasMoreElements()) {
  			previous_line = current_line;
			current_line = (String) tokenizer.nextElement();
			if (current_line != null) {
//				System.out.println("parsing line: "  + current_line);
				processLine(compiled_file, current_line, compiled_path);
			} else {
//				System.out.println("parsing line NULL");
			}
  		}
	}

	public void parseCompilerMessages(IProject project, HashMap express_files, String express_compiler_output) throws CoreException {
		
		fProject = project;
		StringTokenizer tokenizer = new StringTokenizer(express_compiler_output, "\n"); //$NON-NLS-1$
  	String current_line = null;
  		while (tokenizer.hasMoreElements()) {
  			previous_line = current_line;
			current_line = (String) tokenizer.nextElement();
			if (current_line != null) {
//				System.out.println("parsing line: "  + current_line);
//				processLine(express_files, current_line);
				processLine((IFile)null, express_files, current_line, (IPath)null);
			} else {
//				System.out.println("parsing line NULL");
			}
  		}

	
	}

	public void parseCompilerMessages_old(HashMap express_files, String express_compiler_output) throws CoreException {
		
		StringTokenizer tokenizer = new StringTokenizer(express_compiler_output, "\n"); //$NON-NLS-1$
  	String current_line = null;
  		while (tokenizer.hasMoreElements()) {
  			previous_line = current_line;
			current_line = (String) tokenizer.nextElement();
			if (current_line != null) {
//				System.out.println("parsing line: "  + current_line);
				processLine(express_files, current_line);
			} else {
//				System.out.println("parsing line NULL");
			}
  		}
	}

	
	
	
	
	//	public boolean processLine(String line, ErrorParserManager eoParser, int inheritedSeverity) {

	void processLine(IFile compiled_file, String line, IPath compiled_path) throws CoreException {

//if (line != null) {
//	System.out.println("<1> parsing line: " + line);
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

		if (starts_with_ERROR) {
// System.out.println("<>ERROR parser, line: " + line);
//			String fileName = "test_schema.exp";

//			String fileName = "E:/eclipse/runtime-workbench-workspace/aaaaa5/express/test_schema.exp";

//			String fileName = "E:/eclipse/runtime-workbench-workspace/ZZZ_333/Express files/cartesian_dot.exp";
			// String fileName = null;
			
			// try to get selected something
			
//			String fileName = ExpressCorePlugin.getCompiledExpressFileName();	
			
//			IFile file = eoParser.findFilePath(fileName);
			IFile file = compiled_file;

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
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
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
//				String fileName = previous.substring(14, previous.length()-1);
//System.out.println("ERROR parser, file name 2: " + fileName);
//			String fileName = "E:/eclipse/runtime-workbench-workspace/ZZZ_333/Express files/cartesian_dot.exp";


				// !!! need to do something here - to find IFile by the name, or to have them all stored somewhere as IFile
				//TODO tempororily, for testing with single files - WRONG for multiple
				IFile file = compiled_file;
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
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
					}

				}
				//eoParser.generateMarker(file, num, desc, severity, varName);
        setMarker(file, desc, num, SEVERITY_ERROR);
			}
			
		
		} // two-line error
		
		} // not single line error
		
	
	
	} // end of method
		




	protected void setMarker_old(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
	
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


	protected void setMarkerAlt2(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
	
		Map attributes = new HashMap();
  /*  
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
*/	
		MarkerUtilities.setLineNumber(attributes, line_number);
//    MarkerUtilities.setMessage(attributes, message);
    if (file_with_the_problem != null) {
//    	MarkerUtilities.createMarker(file_with_the_problem, attributes, "net.jsdai.express_compiler.expressmarker");
    	MarkerUtilities.createMarker(file_with_the_problem, attributes, IMarker.TEXT);
//    	MarkerUtilities.createMarker(file_with_the_problem, attributes, IMarker.PROBLEM);
    }
  }


	protected void setMarker(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
		setMarker(file_with_the_problem, message, line_number, severity, false, -1, -1);
	}

	protected void setMarker(IFile file_with_the_problem, String message, int line_number, int severity, boolean mark_columns, int column_start_number, int column_end_number) throws CoreException {
	
    if (file_with_the_problem != null) {

//System.out.println("<<!!!>> creating marker");
			IMarker marker = file_with_the_problem.createMarker("net.jsdai.express_compiler.expressproblem");
//			IMarker marker = file_with_the_problem.createMarker(IMarker.PROBLEM);
//			IMarker marker = file_with_the_problem.createMarker(IMarker.TEXT);
//System.out.println("<<!!!>> marker created: " + marker);
//System.out.println("<<!!!>> marker type: " + marker.getType());
//System.out.println("<<!!!>> marker - is subtype of PROBLEM: " + marker.isSubtypeOf(IMarker.PROBLEM));
		
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
				marker.setAttribute("project", "non express project!!!");
			}
//System.out.println("line: " + line_number + ", column: " + column_start_number);
			marker.setAttribute(IMarker.LINE_NUMBER, line_number);
//System.out.println("char start recovered: : " +  marker.getAttribute(IMarker.CHAR_START));

/*			
			int line_offset = convertLineNr2FileOffset(file_with_the_problem,line_number);

			if (fMark_columns) {
				marker.setAttribute(IMarker.CHAR_START, fColumn_start);
				marker.setAttribute(IMarker.CHAR_END, fColumn_end);
			}
*/
    }
	
	}




//####################################
	
	
	//	public boolean processLine(String line, ErrorParserManager eoParser, int inheritedSeverity) {

	void processLine(HashMap express_files, String line) throws CoreException {

//if (line != null) {
//	System.out.println("<2> parsing line: " + line);
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

		if (starts_with_ERROR) {
			 // will not happen
			IFile file = null; // will not happen

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
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
					}

				}
//				eoParser.generateMarker(file, num, desc, severity, varName);
				setMarker(file, desc, num, SEVERITY_ERROR);
			}
		} else { // two line message - this will always occur here
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
				int ref_object = line.indexOf("reference to unknown object in an expression");
				int at_line = line.indexOf("at line");
				String desc = "problem";
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
					} else if (ref_object > 0) {
						desc = varName + ": reference to unknown object in an expression";
					} else {
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
					}

				}
				//eoParser.generateMarker(file, num, desc, severity, varName);
        setMarker(file, desc, num, SEVERITY_ERROR);
			}
			
		
		} // two-line error
		
		} // not single line error
		
	
	
	} // end of method
	


	void processLine(IFile compiled_file ,HashMap express_files, String line, IPath compiled_path) throws CoreException {

// IPath compiled_path is not currently used (or not used any longer?)

//if (line != null) {
//	System.out.println("<3> parsing line: " + line);
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



		fLine = line.trim();

		if (fLine.startsWith("ERROR:")) {
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
			fError_to_mark = true;
			fSingle_file_error = true;
			fFile = compiled_file;
			// get the line number
			int index_line_start = fLine.indexOf("line: ");
			int index_line_end = fLine.indexOf(", column:");
			int index_column_end = -1;
			
			if ((index_line_start > 0) && (index_line_end > index_line_start)) {
				fLine_number = Integer.parseInt(fLine.substring(index_line_start+6, index_line_end));
				index_column_end = fLine.indexOf(". ", index_line_end);
				fColumn_number = Integer.parseInt(fLine.substring(index_line_end+10, index_column_end));
// System.out.println("column: " + fColumn_number);
				fSub_line = fLine.substring(index_column_end + 2);
				processMessage();
			} else {
				// something wrong here, probably we need just to return
				resetAll();
				return;
			}  
		} else
		if (fLine.startsWith("ERROR in file")) {
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
			fError_to_mark = true;
			fMulti_file_error = true;
			String fileName = fLine.substring(14, fLine.length()-1);
//System.out.println("multi-file file name: " + fileName);
			String key = fileName.toLowerCase().replace('\\','$').replace('/','$');
//System.out.println("multi-file key: " + key);
			fFile = (IFile)express_files.get(key);
//System.out.println("multi-file: " + fFile);
		} else 
		if (fLine.startsWith("line:")) {
			if (fMulti_file_error) {
				// get the line number
				int index_line_start = fLine.indexOf("line: ");
				int index_line_end = fLine.indexOf(", column:");
				int index_column_end = -1;
			
				if ((index_line_start >= 0) && (index_line_end > index_line_start)) {
					fLine_number = Integer.parseInt(fLine.substring(index_line_start+6, index_line_end));
					index_column_end = fLine.indexOf(". ", index_line_end);
					fSub_line = fLine.substring(index_column_end + 2);
					processMessage();
//System.out.println("we are here: " + fLine_number + ", " + fSub_line);
				} else {
//System.out.println("but not here");
					// something wrong here, probably we need just to return
					resetAll();
					return;
				}  
			} else {
				// should not happen
				resetAll();
				return;
			}
		} else 	
		if (fLine.startsWith("Was expecting:")) {
			if (fSyntax_error) {
				// the only choice
				fSingle_choice = true;
			} else {
				resetAll();
				return;
			}
		} else
		if (fLine.startsWith("Was expecting one of:")) {
			if (fSyntax_error) {
				fSingle_choice = false;
				fChoice_count = 0;
				if (fChoices == null) {
					fChoices = new Vector();
				} else {
					fChoices.clear();
				}
			} else {
				resetAll();
				return;
			}
		} else
		if (fLine.endsWith(" ...") && (fLine.startsWith("\"") || fLine.startsWith("<"))) {
			// a syntax alternative
			if (fSyntax_error) {
				String choice_value = fLine.substring(0, fLine.length()-4); 
				if (fSingle_choice) {
					if (choice_value.equals("\";\"")) {
						fDescription = "\";\" is missing";
					} else
					if (choice_value.equals("<SIMPLE_ID>")) {
						fDescription = "an identifier is missing before " + fEncountered;
					} else {
						fDescription = choice_value + " is missing, " + fEncountered + " encountered";
					}
					setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
				} else {
					// one of multiple choices
					fChoices.addElement(choice_value);
					
				}
			} else {
				resetAll();
				return;
			}
		} else
		if (fLine.startsWith("ERROR RECOVERY:")) {
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
		} else
		if (fLine.equals("")) {
		} else
		if (fLine.startsWith("--- End of Express Compiler Output ---")) {
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
		} else {
		}


/*
		boolean starts_with_ERROR = line.startsWith("ERROR:");

		if (starts_with_ERROR) {
			 // will not happen
			IFile file = null; // will not happen

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
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
					}

				}
//				eoParser.generateMarker(file, num, desc, severity, varName);
				setMarker(file, desc, num, SEVERITY_ERROR);
			}
		} else { // two line message - this will always occur here
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
				int ref_object = line.indexOf("reference to unknown object in an expression");
				int at_line = line.indexOf("at line");
				String desc = "problem";
				String problem_description = "";
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
					problem_description = line.substring(var_num2 + 2, line.length()-1);
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
					} else if (ref_object > 0) {
						desc = varName + ": reference to unknown object in an expression";
					} else {
//						desc = desc += " with " + varName;
						desc = varName + ": " + problem_description;
					}

				}
				//eoParser.generateMarker(file, num, desc, severity, varName);
        setMarker(file, desc, num, SEVERITY_ERROR);
			}
			
		
		} // two-line error
		
		} // not single line error
		
	*/
	
	} // end of method
	
	
	/*
			here we have sub_line with the erorr message
	*/
	void processMessage()  throws CoreException {
System.out.println("description: " + fSub_line);
		if (fSub_line.startsWith("Encountered ")) {
			fSyntax_error = true;
			int index_encountered_start = 12;
			int index_encountered_end   = fSub_line.indexOf(" at line ");
			if (index_encountered_end > index_encountered_start) {
				fEncountered = fSub_line.substring(index_encountered_start, index_encountered_end);
			} else {
				fEncountered = "INTERNAL ERROR #01";
			}
		} else 
		if (fSub_line.endsWith("referenced named type not found")) {
			fMark_columns = true;
			int file_offset = convertLineNr2FileOffset(fFile, fLine_number);
			fColumn_end = file_offset + fColumn_number;
			fColumn_start = fColumn_end - 1;
//			fColumn_start = fColumn_end - (fSub_line.length() - 34);
			//fColumn_start = file_offset + fSub_line.length() - fColumn_number;
			//fColumn_end = fColumn_start + fColumn_number-1;
			fDescription = fSub_line;
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
			fMark_columns = false;
		} else {
			fDescription = fSub_line;
			setMarkerIfNeededAndReset(fFile, fDescription, fLine_number, fSeverity);
		}
	
	}
	
	protected void setMarkerIfNeededAndReset(IFile file_with_the_problem, String message, int line_number, int severity) throws CoreException {
		if (fError_to_mark) {
//System.out.println("are we here: " + fSyntax_error + ", " + fSingle_choice);
			if (fSyntax_error && !fSingle_choice) {
//System.out.println("we are here");
				// we need to make the error description first
				fDescription = "encountered " + fEncountered + ", was expecting: ";
				boolean first_time = true;
				Iterator iter = fChoices.iterator();
				while (iter.hasNext()) {
					String a_choice = (String)iter.next();
					if (first_time) {
						first_time = false;
					} else {
						fDescription += ", ";
					}
					fDescription += a_choice.toUpperCase();
					if (fDescription.length() > fDescription_length_limit) {
						fDescription += ", ...";
						break;
					}
				}
				message = fDescription;
			}
//			setMarker(file_with_the_problem, message, line_number, severity);
//			InputStream is = file_with_the_problem.getContents();
			setMarker(file_with_the_problem, message, line_number, severity);
		}
		resetAll();
	}

	protected void resetAll() {
		fError_to_mark = false;
		fSingle_file_error = false;
		fMulti_file_error = false;
  	// fSub_line = null;
  	// fLine = null;	
  	int fLine_nr = 0;
		// fFile = null;
		fSeverity = SEVERITY_ERROR;
		fDescription = null;
		fSyntax_error = false;
	}
	
	
	int convertLineNr2FileOffset(IFile ifile, int line_number) throws CoreException {
		InputStream is = ifile.getContents();
		int found_lines = 0;
		try {
		for (int i = 0;;i++) {
			int current;
				current = is.read();
			if (current == '\n') {
				found_lines++;
				if (found_lines >= line_number-1) {
					is.close();
					return i;
				} // if enough
			} // if  line end
		} // for
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 

		return -1;
	} // method end
	
	
	
} // end of class





/*



--- Express Compiler Output ---

file G:\eclipse\runtime-EclipseApplication\test_sizeof\Express files\_exclude not found.

JSDAI(TM) Express Compiler,   Copyright (C) 1998-2005 LKSoftWare GmbH
                  version 4.0.0, build 424, 2006-04-11
----------------------------------------------------------------------
ERROR:  line: 18, column: 1. Encountered "-" at line 18, column 6.
Was expecting:
    <SIMPLE_ID> ...
    
ERROR RECOVERY: skipping to END_RULE
1 error found in pass 1.

--- End of Express Compiler Output ---
express compilation time: 1 seconds



JSDAI(TM) Express Compiler,   Copyright (C) 1998-2005 LKSoftWare GmbH
                  version 4.0.0, build 424, 2006-04-11
----------------------------------------------------------------------
ERROR:  line: 5, column: 26. ano3ther_entity - referenced named type not found
1 error found in pass 3.

--- End of Express Compiler Output ---
express compilation time: 1 seconds


file G:\eclipse\runtime-EclipseApplication\test_sizeof\Express files\_exclude not found.

JSDAI(TM) Express Compiler,   Copyright (C) 1998-2005 LKSoftWare GmbH
                  version 4.0.0, build 424, 2006-04-11
----------------------------------------------------------------------
ERROR:  line: 5, column: 26. Encountered "WHERE" at line 6, column 1.
Was expecting:
    ";" ...
    
ERROR RECOVERY: skipping to END_ENTITY
1 error found in pass 1.

--- End of Express Compiler Output ---
express compilation time: 1 seconds



ERROR in file c:\eclipseD\W1\stepmod\data\modules\annotated_presentation\mim.exp:
 line: 82, column: 93. Encountered "/ ;" at line 82, column 95.
Was expecting one of:
    ";" ...
    "<" ...
    ">" ...
    "<=" ...
    ">=" ...
    "<>" ...
    "=" ...
    ":<>:" ...
    ":=:" ...
    "in" ...
    "like" ...
    "**" ...
    "+" ...
    "-" ...
    "or" ...
    "xor" ...
    "*" ...
    "/" "[" ...
    "/" "{" ...
    "/" "query" ...
    "/" "case" ...
    "/" "for" ...
    "/" "if" ...
    "/" "+" ...
    "/" "-" ...
    "/" "not" ...
    "/" "(" ...
    "/" <BINARY_LITERAL> ...
    "/" <INTEGER_LITERAL> ...
    "/" "false" ...
    "/" "true" ...
    "/" "unknown" ...
    "/" <REAL_LITERAL> ...
    "/" <SIMPLE_STRING_LITERAL> ...
    "/" <ENCODED_STRING_LITERAL> ...
    "/" "const_e" ...
    "/" "pi" ...
    "/" "self" ...
    "/" "?" ...
    "/" <SIMPLE_ID> ...
    
ERROR RECOVERY: skipping to END_ENTITY
1 error found in pass 1.



*/