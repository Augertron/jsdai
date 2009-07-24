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


package jsdai.step_editor.editor;

//public interface P21Constants {
public class P21Constants {


  public static final String P21_PARTITIONER = "net.jsdai.step_editor.editor.P21Partitioner";

  public final static String P21_MULTILINE_COMMENT = "__p21_multiline_comment"; //$NON-NLS-1$
  public final static String P21_COMPLEX_INSTANCE = "__p21_complex_instance"; //$NON-NLS-1$

  public final static String[] P21_PARTITION_TYPES = new String[] {P21_MULTILINE_COMMENT, P21_COMPLEX_INSTANCE};


  public final static String[] HEADER_KEYWORDS = { 
    "FILE_DESCRIPTION",
    "FILE_NAME",
    "FILE_SCHEMA",
    "FILE_POPULATION"
  };



  public final static String[] SECTION_KEYWORDS = { 
    "DATA",
    "ENDSEC",
    "HEADER"
  };

  public final static String[] KEYWORDS3 = { 
    "END-ISO-10303-21",
    "ISO-10303-21"
  };

  public final static String[] CONSTANTS = { 
	    "$",
	    "*",
	    ".T.",
	    ".F."
	  };


  /*
  private final static String[] LEGAL_CONTENT_TYPES= new String[] {
    IJavaPartitions.JAVA_DOC,
    IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
    IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
    IJavaPartitions.JAVA_STRING,
    IJavaPartitions.JAVA_CHARACTER
  };
*/

}






// Block Comment On = /* Block Comment Off = */

/*


String - ''

/C1
FILE_DESCRIPTION FILE_NAME FILE_SCHEMA
/C2
DATA
ENDSEC
HEADER
/C3
END-ISO-10303-21
ISO-10303-21




*/