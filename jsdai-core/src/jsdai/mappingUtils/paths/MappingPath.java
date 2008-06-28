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

package jsdai.mappingUtils.paths;

import antlr.*;
import antlr.collections.AST;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.swing.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;
import jsdai.lang.*;

/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class MappingPath {

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws FileNotFoundException, IOException, SdaiException {
		try {
			boolean showHelp = false;
			boolean reportMissing = false;
			Collection missingSchemaMatchers = null;
			boolean trackChanges = false;
			String trackChangesFile = null;
			String pathFile = null;
			String mappingInfoFile = null;
			if(args.length == 0) {
				showHelp = true;
			} else {
				for (int i = 0; i < args.length; i++) {
					if(trackChanges) {
						trackChangesFile = args[i];
						trackChanges = false;
					} else if(args[i].charAt(0) == '-') {
						if(args[i].equals("-missing")) {
							reportMissing = true;
						} else if(args[i].equals("-missing-for-schemas")) {
							if(++i == args.length) {
								showHelp = true;
								break;
							}
							if(missingSchemaMatchers == null) {
								missingSchemaMatchers = new ArrayList();
							}
							missingSchemaMatchers.add(Pattern.compile(args[i], Pattern.CASE_INSENSITIVE).matcher(""));
							reportMissing = true;
						} else if(args[i].equals("-track")) {
							trackChanges = true;
						} else {
							showHelp = true;
							break;
						}
					} else if(pathFile == null) {
						pathFile = args[i];
					} else if(mappingInfoFile == null) {
						mappingInfoFile = args[i];
					} else {
						showHelp = true;
                        break;
					}
				}
			}
			if(!showHelp && !trackChanges) {
				MappingPathParser parser = 
					new MappingPathParser(pathFile, mappingInfoFile, reportMissing, missingSchemaMatchers, trackChangesFile);
				// Parse the input expression
				parser.schemaMapping();
// 				// Print the resulting tree out in LISP notation
// 				System.out.println(t.toStringTree());
				//MappingPathWalker walker = new MappingPathWalker();
				//walker.allPaths(tree);
			} else {
				System.out.println("Usage:\n" +
								   "  MappingPath [-missing [-missing-for-schemas schemas_regexp [...]]] " +
								   "[-track track_changes_property_file] path_file [mapping_info_file]\n");
			}
		}
		catch(TokenStreamRecognitionException e) {
			System.err.println(e.recog);
		}
		catch(TokenStreamException e) {
			System.err.println("exception: "+e);
		}
		catch(RecognitionException e) {
			System.err.println(e);
		}
	}

}
