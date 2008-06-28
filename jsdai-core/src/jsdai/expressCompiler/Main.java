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

package jsdai.expressCompiler;

import java.io.*;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;

// import java.io.*;
// import COM.objectspace.jgl.*;


/*
public class Main
{

//  public static void main(String args[]) throws java.lang.ClassNotFoundException, jsdai.lang.SdaiException, java.io.IOException
  public static int main(String args[]) throws java.lang.ClassNotFoundException, jsdai.lang.SdaiException, java.io.IOException
  {
    Vector compiled_schemas = new Vector();
    return Compiler2.main(args, null, true, null, compiled_schemas);
  }
}

*/




public final class Main
{
	private Main() { }

	static PrintStream pout, perr;
	static BufferedOutputStream bout, berr;
	static FileOutputStream fout, ferr;
	static boolean flag_eclipse = false;





//  public static void main(String args[]) throws java.lang.ClassNotFoundException, jsdai.lang.SdaiException, java.io.IOException
	  public static void main(String args[])
	  {
		  main(args, true, null);
	  }

	  private static void main(String args[], boolean exitOnError, ECMonitor monitor)
	  {
		  int exitCode = 0;
    Vector compiled_schemas = new Vector();
    PrintStream prevOut = null;
    PrintStream prevErr = null;
//    public static int main(String args[], Vector par_express_files, boolean first_time_invoking, IProgressMonitor monitor, Vector compiled_schemas2) throws jsdai.lang.SdaiException, java.lang.ClassNotFoundException, java.io.IOException

		String dir1 = null;
		String dir = null;
		boolean flag_write_files = false;
		for (int j = 0; j < args.length-1; j++) {
		if (args[j].equals("-eclipse")) {
			flag_eclipse = true;
		} else 
		if (args[j].equals("-write_files")) {
			flag_write_files = true;
		}
	}

	for (int j = 0; j < args.length-1; j++) {
    if ((args[j].equalsIgnoreCase("-output_dir")) || (args[j].equalsIgnoreCase("-out"))) {
			dir1 = args[j+1];
			if (flag_eclipse) {
				dir = dir1.substring(0, dir1.length()-5);
			} else {
				dir = dir1;
			} 
			break;
		}
	}
	
try {

	for (int ihi = 0; ihi < args.length-1; ihi++) {

			if (args[ihi].equalsIgnoreCase("-stdout")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A file name must follow " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A file name must follow " + args[ihi-1] + " switch");
					return;
				}
//				System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(args[ihi]))));
				fout = new FileOutputStream(args[ihi]);
				bout = new BufferedOutputStream(fout);
				pout = new PrintStream(bout);
				prevOut = System.out;
				System.setOut(pout);
			}
			if (args[ihi].equalsIgnoreCase("-stderr")) {
				ihi++;
				if (ihi < args.length) {
					if (args[ihi].substring(0,1).equals("-")) {
						System.out.println("A file name must follow " + args[ihi-1] + " switch");
						return;
					}
				} else {
					System.out.println("A file name must follow " + args[ihi-1] + " switch");
					return;
				}
//				System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(args[ihi]))));
				ferr = new FileOutputStream(args[ihi]);
				berr = new BufferedOutputStream(ferr);
				perr = new PrintStream(berr);
				prevErr = System.err;
				System.setErr(perr);

			}

	}

  String rezult_file = null;
  String schema_file = null;

		int result = Compiler2.main(args, null, true, monitor, compiled_schemas);

	  if ((dir != null) && (flag_write_files)) {
			rezult_file = dir + File.separator + "temp_rezult";
			schema_file = dir + File.separator + "temp_schemas";
    	
    	FileOutputStream fos1 = new FileOutputStream(rezult_file);
    	OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
    	PrintWriter pw1 = new PrintWriter(osw1);
			String result_str = "OK";
			if (result < 0) result_str = "ERROR";
	
			pw1.println("" + result_str);
	    pw1.flush();
    	pw1.close();
	

    	FileOutputStream fos2 = new FileOutputStream(schema_file);
    	OutputStreamWriter osw2 = new OutputStreamWriter(fos2);
    	PrintWriter pw2 = new PrintWriter(osw2);

			if (compiled_schemas != null) {
				if (compiled_schemas.size() > 0) {
					for (int i = 0; i < compiled_schemas.size(); i++) {
						String compiled_schema = (String)compiled_schemas.elementAt(i);
						pw2.println(compiled_schema);
					}
				}
			}


	    pw2.flush();
    	pw2.close();
		}
} catch (LicensingException e) {
	exitCode = 1;
} catch (Throwable eee) {
//} catch (Exception eee) {

	System.err.println("Exception occurred: " + eee);
	eee.printStackTrace();
	exitCode = 1;
}

		try {
			SdaiSession runningSession = SdaiSession.getSession();
			if (runningSession != null) {
				if (runningSession.testActiveTransaction()) {
					runningSession.getActiveTransaction().abort();
				}
				runningSession.closeSession();
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		}
		if (pout != null) {
			pout.flush();
			pout.close();
		}
		if(prevOut != null) {
			System.setOut(prevOut);
		}
		if (perr != null) {
			perr.flush();
			perr.close();
		}
		if(prevErr != null){
			System.setErr(prevErr);
		}

		if(exitOnError && exitCode != 0) {
			System.exit(exitCode);
		}
		return;
  }

  	public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final ECMonitor monitor)
  	throws SdaiException {
  		Properties jsdaiProperties = new Properties();
  		jsdaiProperties.setProperty("repositories", sdaireposDirectory);
  		SdaiSession.setSessionProperties(jsdaiProperties);
  		return new Runnable() {
  			public void run() {
  				main(args, false, monitor);
  			}
  		};
  	}


/*
  public static int main(String args[]) throws java.lang.ClassNotFoundException, jsdai.lang.SdaiException, java.io.IOException
  {
    Vector compiled_schemas = new Vector();

		int result = Compiler2.main(args, null, true, null, compiled_schemas);
		return result;
  }
*/

}


// original without support for Eclipse
/*
class Main
{
  public static void main(String args[]) throws java.lang.ClassNotFoundException, jsdai.lang.SdaiException, java.io.IOException
  {
    Compiler2.main(args, null, true);
  }
}

*/



