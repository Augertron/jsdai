
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

package jsdai.express_g.util.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.HashMap;


public class IsoDbTools {

  static HashMap hm_iso_numbers = null;


	public static HashMap getIsoNumbers() {
		return hm_iso_numbers;
	}  
	
  public static void readIsoNumbersOfSchemas(String iso_file_name) {

    final int TK_START_LINE = 0;
    final int TK_LONG = 1;
    final int TK_SHORT = 2;
    final int TK_COMMA = 3;

    if (hm_iso_numbers == null) {
    	hm_iso_numbers = new HashMap();
		} else {
			return;
		}
    try {
      FileInputStream ins = new FileInputStream(iso_file_name);
      InputStreamReader isr = new InputStreamReader(ins);
      StreamTokenizer st = new StreamTokenizer(isr);
      st.eolIsSignificant(true);
      st.wordChars('_', '_');
      st.wordChars(' ', ' ');
      st.wordChars('-', '-');
      st.wordChars('/', '/');
      st.ordinaryChar(',');
      st.commentChar('#');

      int status = TK_START_LINE;
      String current_schema_name = null;
      String current_schema_iso_number = null;

      while (st.ttype != StreamTokenizer.TT_EOF) {
        st.nextToken();
//System.out.println("<<>><> status: " + status + ", type: " + st.ttype + ", value: " + st.sval);
        if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_schema_name = st.sval.toLowerCase();
          status = TK_LONG;
        } else if ((status == TK_LONG) && (st.ttype == ',')) {
          status = TK_COMMA;
        } else if ((status == TK_COMMA) && (st.ttype == StreamTokenizer.TT_WORD)) {
          current_schema_iso_number = st.sval;
          status = TK_SHORT;
          hm_iso_numbers.put(current_schema_name, current_schema_iso_number);
//System.out.println("<<>> schema: " + current_schema_name + ", number: " +  current_schema_iso_number);        
        } else if (((status == TK_SHORT) && (st.ttype == StreamTokenizer.TT_EOL)) || 
                       (st.ttype == StreamTokenizer.TT_EOF)) {
          // current reading completed. Now, use it.
          if (st.ttype == StreamTokenizer.TT_EOF) {
            status = TK_START_LINE;

            break;
          } else if (st.ttype == StreamTokenizer.TT_EOL) {
            // ok, next complex entity
            status = TK_START_LINE;
          }
        } else if ((status == TK_START_LINE) && (st.ttype == StreamTokenizer.TT_EOL)) {
        } else {
          System.out.println("ERROR in input file, line: " + st.lineno());

          break;
        }
      }

    } // try
    catch (IOException e) {
      // no schema - no iso numbers
      return;
   }
  }

}