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

package jsdai.tools;

import java.io.*;
import java.util.*;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.util.*;


public class SearchForCyclicDependencies {

	static PrintStream pout, perr;
	static BufferedOutputStream bout, berr;
	static FileOutputStream fout, ferr;

	static boolean flag_only_cyclic = false;
	static boolean flag_help = false;
	static boolean bottom_level = false;
	static boolean flag_search_for_redundant_use_froms = false;
  static boolean flag_include_reference_froms = false;
	static boolean flag_include_partial = false;

	static public void main(String args[]) {
		main(args, null);
	}


//	public static final void main(String args[], jsdai.util.UtilMonitor monitor) throws SdaiException {
	public static final void main(String args[], jsdai.util.UtilMonitor monitor) {

			String location = "ExpressCompilerRepo";

			System.out.println("");
			System.out.println("JSDAI(TM) SearchForCyclicDependencies,   Copyright (C) 2004 LKSoftWare GmbH");
			System.out.println("---------------------------------------------------------------------------");

		try {
		
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-stdout")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A file name must follow " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A file name must follow " + args[i-1] + " switch");
						return;
					}
					fout = new FileOutputStream(args[i]);
					bout = new BufferedOutputStream(fout);
					pout = new PrintStream(bout);
					System.setOut(pout);
				}
				if (args[i].equalsIgnoreCase("-stderr")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A file name must follow " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A file name must follow " + args[i-1] + " switch");
						return;
					}
					ferr = new FileOutputStream(args[i]);
					berr = new BufferedOutputStream(ferr);
					perr = new PrintStream(berr);
					System.setErr(perr);
				}

				if (args[i].equalsIgnoreCase("-help")) flag_help = true;
				if (args[i].equalsIgnoreCase("-?")) flag_help = true;
				if (args[i].equalsIgnoreCase("-include_reference_froms")) flag_include_reference_froms = true;
				if (args[i].equalsIgnoreCase("-ir")) flag_include_reference_froms = true;
				if (args[i].equalsIgnoreCase("-include_partial")) flag_include_partial = true;
				if (args[i].equalsIgnoreCase("-ip")) flag_include_partial = true;
				if (args[i].equalsIgnoreCase("-oc")) flag_only_cyclic = true;
				if (args[i].equalsIgnoreCase("-only_cyclic")) flag_only_cyclic = true;
				if (args[i].equalsIgnoreCase("-redundant")) flag_search_for_redundant_use_froms = true;
				if (args[i].equalsIgnoreCase("-ruf")) flag_search_for_redundant_use_froms = true;
				if (args[i].equalsIgnoreCase("-location")) {
					i++;
					if (i < args.length) {
						if (args[i].substring(0,1).equals("-")) {
							System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
							return;
						}
					} else {
						System.out.println("A repository path or name must follow the " + args[i-1] + " switch");
						return;
					}
					location = args[i];
				}
			}

			if (flag_help) {
				System.out.println("USAGE:\n");
				System.out.println("java jsdai.tools.SearchForCyclicDependencies [-oc | -only_cyclic] [-location name|path]");
				System.out.println("\ncommand line switches:\n");
				System.out.println("-oc | -only_cyclic"); 
				System.out.println("\tdoes not print schemas without cyclic dependencies");
				System.out.println("-location name|path");
				System.out.println("\tthe name of or the path to the repository to be checked,");
				System.out.println("\t\tdefault: ExpressCompilerRepo, location specified in jsdai.properties");
				return;
			}
	
			SearchForCyclicDependencies sfcd = new SearchForCyclicDependencies();
			SdaiSession session = SdaiSession.openSession();
			SdaiTransaction trans = session.startTransactionReadOnlyAccess();
      sfcd.run(SimpleOperations.linkRepositoryOrName("ExpressCompilerRepo", location));
      session.closeSession();

		} catch (Exception eee) {
			System.err.println("Exception occurred: " + eee);
			eee.printStackTrace();
		} finally {
			if (pout != null) {
				pout.flush();
				pout.close();
			}
			if (perr != null) {
				perr.flush();
				perr.close();
			}
		}
			
	
	}

	void run(SdaiRepository repo) throws SdaiException {
		repo.openRepository();
		//repo.exportClearTextEncoding("_KUKU3.pf");
		System.out.println();


		ASdaiModel models = repo.getModels();
		SdaiIterator iter = models.createIterator();
    int count = 0;
		HashSet interfaced_schemas = new HashSet();
    
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String name = model.getName();
			if ((name.endsWith("_DICTIONARY_DATA")) && (!name.startsWith("SDAI_"))) {
	      if (model.getMode() == SdaiModel.NO_ACCESS) {
        	model.startReadOnlyAccess();
	      }
				ESchema_definition sd = getSchema_definitionFromModel(model);
				if (!flag_only_cyclic) {
//					System.out.print("checking schema: " + sd.getName(null));
					System.out.print("" + sd.getName(null));
				}
				bottom_level = false;
				boolean is_cyclic = CyclicTest(sd, sd.getName(null), true, interfaced_schemas);
				if (is_cyclic) {
					// System.out.println("");
				  count++;
				} else {
					 if (!flag_only_cyclic) {
					 	if (bottom_level) {
//					 		System.out.println(" - TOP");
					 		System.out.println(" - BOTTOM"); // schema that is not interfacing any other shemas
					 	} else {
					 		System.out.println(" - OK");
						}
					}
				}
			}
		} 
	
	
		if (count == 0) {
			System.out.println("\nNo schemas with cyclic dependencies found");
		} else if (count == 1) {
			System.out.println("\n one schema with cyclic dependencies found");
		} else {
			System.out.println("\n" + count + " schemas with cyclic dependencies found");
		}

		// new cycle through the models to print the list of top-level schemas
   	System.out.println("\n=== Top level schemas, not interfaced by any other schemas ===\n");
		iter.beginning();
		int top_count = 0;
		Vector v_top_level_schemas = new Vector();
//		String [] top_level_schemas = new String [1];
		
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			String name = model.getName();
			if ((name.endsWith("_DICTIONARY_DATA")) && (!name.startsWith("SDAI_"))) {
				ESchema_definition sd = getSchema_definitionFromModel(model);
	      if (!interfaced_schemas.contains(sd) && !sd.getName(null).equalsIgnoreCase("mixed_complex_types")) {
	      	// top level schema, not interfaced by any other schemas
	      	System.out.println(sd.getName(null));
//	      	if (sd.getName(null).equalsIgnoreCase("IDA_STEP_SCHEMA_XIM")) {
	      		v_top_level_schemas.add(sd.getName(null));
//	      		top_level_schemas[top_count] = sd.getName(null);
//	      		top_level_schemas[0] = sd.getName(null);
//	      	}
	      	top_count++;
	      }
			}
		}
   	if (top_count == 0) {
	   	System.out.println("\nNo top level schemas found\n");
		} else
   	if (top_count == 1) {
	   	System.out.println("\nOne top level schema found\n");
		} else {
	   	System.out.println("\n" + top_count + " top level schemas found\n");
		}

		if (flag_search_for_redundant_use_froms) {
   		System.out.println("\n\nChecking for redundant USE FROMs\n");
//		top_level_schemas[0] = "IDA_STEP_SCHEMA_XIM";
			String [] top_level_schemas = new String[top_count];
			Iterator tls_iter = v_top_level_schemas.iterator();
			int itls = 0;
			while (tls_iter.hasNext()) {
				String a_tls = (String)tls_iter.next();
				top_level_schemas[itls++] = a_tls;
			}
		
		  Map redUsefroms = jsdai.tools.SearchForRedundantUsefroms.getRedundantInterfaceSpecifications(repo, top_level_schemas, false, true);

			if (redUsefroms.size() > 0) {
				System.out.println("List of redundant usefroms:\n");
				for (Iterator i = redUsefroms.keySet().iterator(); i.hasNext();) {
					String schema = (String) i.next();
					Set usefroms = (Set) redUsefroms.get(schema);
				
					System.out.println("=== SCHEMA: " + schema);
					for (Iterator j = usefroms.iterator(); j.hasNext();) {
						String usefrom = (String) j.next();
						System.out.println("USEFROM: " + usefrom);
					}
				}
			}
			else
				System.out.println("No redundant usefroms found.");
    } // end- - invoke checking for redundant USE FROMs
	}


	boolean CyclicTest(ESchema_definition asd, String noas, boolean first, HashSet interfaced_schemas) throws SdaiException {
		if (!first && asd.getName(null).equals(noas)) {
			// a cyclic case found
			if (flag_only_cyclic) {
			 	System.out.println("" + noas + " - cyclic, interfaced");
			} else {
				System.out.println(" - cyclic, interfaced" );
			}
			return true;
		} else
		if (noas.equals((String)asd.getTemp())) {
    	// we already were here, skip this branch
		} else {
			// first time visiting
			asd.setTemp(noas); 
			if (!first) {
				interfaced_schemas.add(asd);
			}
	    // loop over all interfaced schemas
		  AInterface_specification specifications = new AInterface_specification();
//			CInterface_specification.usedinCurrent_schema(null, asd, null, specifications);
			CUse_from_specification.usedinCurrent_schema(null, asd, null, specifications);
			if (flag_include_reference_froms) {
				CReference_from_specification.usedinCurrent_schema(null, asd, null, specifications);
			}
			if (first && specifications.getMemberCount() < 1) {
				bottom_level = true;
			}
			SdaiIterator iter = specifications.createIterator();
			while (iter.next()) {
				EInterface_specification specification = (EInterface_specification)specifications.getCurrentMemberObject(iter);
       
       
				if (!flag_include_partial) {
					if (specification instanceof EUse_from_specification) {
						if (((EUse_from_specification) specification).testItems(null)) {
							continue;
						}
					} else {
						if (((EReference_from_specification) specification).testItems(null)) {
							continue;
						}
					}
				}
       
        ESchema_definition sd = (ESchema_definition)specification.getForeign_schema(null);
        if (CyclicTest(sd, noas, false, interfaced_schemas)) {
        	System.out.println("  into " + asd.getName(null));
        	return true;
        }
			}
		}
		return false;
	}




	boolean CyclicTest_old(ESchema_definition asd, String noas, boolean first, HashSet interfaced_schemas) throws SdaiException {
		if (!first && asd.getName(null).equals(noas)) {
			// a cyclic case found
			if (flag_only_cyclic) {
			 	System.out.println("" + noas + " - cyclic, interfaced");
			} else {
				System.out.println(" - cyclic, interfaced" );
			}
			return true;
		} else
		if (noas.equals((String)asd.getTemp())) {
    	// we already were here, skip this branch
		} else {
			// first time visiting
			asd.setTemp(noas); 
			if (!first) {
				interfaced_schemas.add(asd);
			}
	    // loop over all interfaced schemas
		  AInterface_specification specifications = new AInterface_specification();
			CInterface_specification.usedinCurrent_schema(null, asd, null, specifications);
			if (first && specifications.getMemberCount() < 1) {
				bottom_level = true;
			}
			SdaiIterator iter = specifications.createIterator();
			while (iter.next()) {
				EInterface_specification specification = (EInterface_specification)specifications.getCurrentMemberObject(iter);
        ESchema_definition sd = (ESchema_definition)specification.getForeign_schema(null);
        if (CyclicTest_old(sd, noas, false, interfaced_schemas)) {
        	System.out.println("  into " + asd.getName(null));
        	return true;
        }
			}
		}
		return false;
	}
	

  ESchema_definition getSchema_definitionFromModel(SdaiModel sm)
                                                   throws SdaiException {
    if (sm == null) return null;
    Aggregate ia = sm.getEntityExtentInstances(ESchema_definition.class);
    SdaiIterator iter_inst = ia.createIterator();

    if (iter_inst.next()) {
      ESchema_definition inst = (ESchema_definition) ia.getCurrentMemberObject(iter_inst);

      return inst;
    }

    return null;
  }

  	public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final jsdai.util.UtilMonitor monitor)
  	throws SdaiException {
  		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);
  		SdaiSession.setSessionProperties(jsdaiProperties);
  		return new Runnable() {
  			public void run() {
  				main(args, monitor);
  			}
  		};
  	}

	
}
