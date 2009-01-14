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

package jsdai.express_compiler.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsoleStream;
import org.osgi.framework.Bundle;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.preferences.ExpressCompilerPreferences;
import jsdai.express_compiler.preferences.ExpressPreferences;

public class ExpressCompilerUtils {



   /**
	 * @return Returns the working directory.
	 */
	public static String getWorkingDirectory() {
		String working_directory  = ExpressCompilerPlugin.getDefault().getPreferenceStore().getString(ExpressCompilerPreferences.EXPRESS_WORKING_DIRECTORY);
		
		working_directory = "".equals(working_directory) ? null : working_directory;
		
		if (working_directory == null) {
			working_directory = ExpressCompilerPlugin.getDefault().getWorkingDirectory();
		}
		
		return working_directory;
		
	}


	public static String getSystemTempDirectoryString() {
		String dirRoot = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		long tmpseed = (new Date()).getTime();

		// in Linux, returns '/tmp', we must add '/'
		if (!dirRoot.endsWith(File.separator))
			dirRoot += File.separator;

		// on Unix/Linux, the temp dir is shared by many users, so we need to ensure 
		// that the top working directory is different for each user
		if (!Platform.getOS().equals("win32")) { //$NON-NLS-1$
			String home = System.getProperty("user.home"); //$NON-NLS-1$
			home = Integer.toString(home.hashCode());
			dirRoot += home + File.separator;
		}
//		dirRoot += "JSDAI_EXPRESS_COMPILER" + File.separator + "projects" + File.separator + Long.toString(tmpseed) + File.separator; //$NON-NLS-1$ //$NON-NLS-2$
		dirRoot += "JSDAI_EXPRESS_COMPILER" + File.separator + Long.toString(tmpseed) + File.separator; //$NON-NLS-1$ //$NON-NLS-2$

		return dirRoot;
	}


	public static boolean hasCompilerEnded(String current_directory) {

		String file_name = current_directory + File.separator + "temp_rezult";
		
		try {
			FileInputStream ins = new FileInputStream(file_name);
			// to get rid of never read warning
			ins.available();
		} catch (IOException e) {
		    return false;
		}
		return true;
		}




	public static boolean getCompilerExitCode(String current_directory) {

//		IFile result_file = fProject.getFile("Internal/temp_rezult");
//		String file_name = result_file.getLocation().toOSString();
		String file_name = current_directory + File.separator + "temp_rezult";
		String result = "ERROR";
		
		try {
			FileInputStream ins = new FileInputStream(file_name);
		    InputStreamReader isr = new InputStreamReader(ins);
		    StreamTokenizer st = new StreamTokenizer(isr);
		    st.eolIsSignificant(true);
		    st.wordChars('_', '_');
		    st.wordChars('.', '.');
		    st.wordChars(':', ':');
		    st.wordChars('/', '/');
		    st.wordChars('\\', '\\');
		    st.commentChar('#');

		    while (st.ttype != StreamTokenizer.TT_EOF) {
		    	st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					result = st.sval;
					break;
				} // if		
			} // while
		  } // try
		
		    //  catch (FileNotFoundException e) 
		  catch (IOException e) {
//				ExpressCompilerPlugin.log(e); // no need to show this to the users
//System.out.println("<X> exception: " + e);						
		    return false;
		   }
		  if (result.equals("OK")) {
		  	return true;
		  } else {
		  	return false;
		  }
		}

	
	
	public static Vector getCompiledSchemas(String current_directory) {
		Vector result = new Vector();

		String file_name = current_directory + File.separator + "temp_schemas";
	
		
		try {
			FileInputStream ins = new FileInputStream(file_name);
		    InputStreamReader isr = new InputStreamReader(ins);
		    StreamTokenizer st = new StreamTokenizer(isr);
		    st.eolIsSignificant(true);
		    st.wordChars('_', '_');
		    st.wordChars('.', '.');
		    st.wordChars(':', ':');
		    st.wordChars('/', '/');
		    st.wordChars('\\', '\\');
		    st.commentChar('#');

		    while (st.ttype != StreamTokenizer.TT_EOF) {
		    	st.nextToken();
				if (st.ttype == StreamTokenizer.TT_WORD) {
					result.addElement(st.sval.replace('/', File.separatorChar).replace('\\', File.separatorChar));
				}		
			}
		  } // try

		    //  catch (FileNotFoundException e) 
		  catch (IOException e) {
//				ExpressCompilerPlugin.log(e);
//		    System.out.println("the file " + file_name + " with the models to include not found.");
					
		    return result;
		  }
	
		
		
		
		return result;
	}
	
	public static String getClassPath(Bundle bundle, String jarFile) {
//System.out.println("<XX-01>bundle: " + bundle + ", jar: " + jarFile);
		try {
//System.out.println("<XX-02>");
			URL classpath = Platform.asLocalURL(bundle.getEntry(jarFile));
//System.out.println("<XX-03>URL: " + classpath);
			String classpath_string = classpath.getFile().toString();
//System.out.println("<XX-04>string: " + classpath_string);
//		classpath_string = classpath_string.substring(1, classpath_string.length());

		if (Platform.getOS().equals("win32")) {
			classpath_string = classpath_string.substring(1);
		}
//System.out.println("<XX-05>substring: " + classpath_string);
			return classpath_string;
		} catch (IOException e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("<XX-06>exception: " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public static String getStringFromStream(InputStream in) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[2048];
		int read = in.read(buf);

		while (read > 0)
		{
			out.write(buf, 0, read);
			read = in.read(buf);
		}

		return out.toString();
	}


	public static String getStringFromStreamAvailable(InputStream in) throws IOException
	{
		
//		int out_size = 100000000;
//		int buf_size = 2048;


//System.out.println("<SfS-00 input stream: " + in);
//System.out.println("<SfS-00 input stream count: " + in.available());
//in.reset();
//System.out.println("<SfS-00 input stream count again: " + in.available());
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ByteArrayOutputStream out = new ByteArrayOutputStream(out_size);
		byte[] buf = new byte[2048];
//System.out.println("<SfS-01>");
//		int read = in.read(buf);
		int read = 0;
		if (in.available() > 0) {
			read = in.read(buf);
		}
//System.out.println("<SfS-02> read: " + read);

		while (read > 0)
		{
//System.out.println("<SfS-03>");
			out.write(buf, 0, read);
//System.out.println("<SfS-04>");
			read = 0;
			if (in.available() > 0) {
				read = in.read(buf);
			}
//System.out.println("<SfS-05> read: " + read);
		}
//System.out.println("<SfS-06>");

		return out.toString();
	}


	public static String getStringFromStream_alt(InputStream stream) throws IOException {
		int k;
		// seems to be the fastest way - the largest chunks, hopefully, in a single one
//		int aBuffSize = Integer.MAX_VALUE;
		int aBuffSize = 10000000; // ok
//		int aBuffSize = 100000000; // not working
//		int aBuffSize = 50000000; // not working
//		int aBuffSize =   28000000; // OK

//System.out.println("<SfS-01>");
//		int aBuffSize =   1000; // OK
//		int aBuffSize =   10000; // OK
//		int aBuffSize = 2147483647; // max - error
		byte buff[] = new byte[aBuffSize];
//System.out.println("<SfS-02>");
		OutputStream xOutputStream = new ByteArrayOutputStream(aBuffSize); 
//System.out.println("<SfS-03>");
		while ((k = stream.read(buff) ) != -1) { 
//System.out.println("<SfS-04>");
			xOutputStream.write(buff,0,k);
//System.out.println("<SfS-05>");
		}
//System.out.println("<SfS-06>");
		return xOutputStream.toString();
	}

	/**
	 * Prints out the string represented by the string buffer
	 */
/* no longer used	
	public static void printResultInConsoleO(String output, IWorkbenchPage fPage) {
//System.out.println("<TO-Console-01> output: " + output);
		try {
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IWorkbenchPage page = fPage;
//			System.out.println("<TO-Console-02> page: " + page);
			ExpressConsoleView console = (ExpressConsoleView)page.findView("org.eclipse.ui.console.ConsoleView");
//System.out.println("<TO-Console-03> console: " + console);
			
			if (console!=null) {
				console.setOutputText(output);
//System.out.println("<TO-Console-04>");
//RR			} else if (ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferencePage.SHOW_OUTPUT_IN_CONSOLE)==true) {
			} else {
				page.showView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
//			ExpressConsoleView console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
//				console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);			
//System.out.println("<TO-Console-05> console: " + console);
				console.setOutputText(output);
//System.out.println("<TO-Console-06>");
			}
		} catch (PartInitException e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("<TO-Console-07 exception: >" + e);
			ExpressCompilerPlugin.getDefault().getLog().log(
				new Status(
					IStatus.ERROR,
					ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID,
					0,
					ExpressCompilerPlugin.getResourceString("CompilerAction.consoleViewOpeningProblem"),
					e));
		} catch (Throwable thr) {
				ExpressCompilerPlugin.log(thr);
			System.out.println("<TO-Console-08 exception: >" + thr);
		}
		

	}
*/

	public static void printResultInConsole(String message, IWorkbenchPage fPage) {

		MessageConsoleStream stream = ExpressCompilerPlugin.getDefault().getConsole();
		if (stream != null) {
//			stream.println(message);
				stream.println("validation or compilation results are here");
//			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(stream.getConsole());
		}

		
	}

/* no longer used	

	public static void printResultInXConsole(String output, IWorkbenchPage fPage) {
//System.out.println("<TO-Console-01> output: " + output);
		try {
//			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IWorkbenchPage page = fPage;
//			System.out.println("<TO-Console-02> page: " + page);
			ExpressConsoleView console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
//System.out.println("<TO-Console-03> console: " + console);
			
			if (console!=null) {
				console.setOutputText(output);
//System.out.println("<TO-Console-04>");
//RR			} else if (ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferencePage.SHOW_OUTPUT_IN_CONSOLE)==true) {
			} else {
				page.showView(ExpressConsoleView.EXPRESS_CONSOLE_ID);
				console = (ExpressConsoleView)page.findView(ExpressConsoleView.EXPRESS_CONSOLE_ID);			
//System.out.println("<TO-Console-05> console: " + console);
				console.setOutputText(output);
//System.out.println("<TO-Console-06>");
			}
		} catch (PartInitException e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("<TO-Console-07 exception: >" + e);
			ExpressCompilerPlugin.getDefault().getLog().log(
				new Status(
					IStatus.ERROR,
					ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID,
					0,
					ExpressCompilerPlugin.getResourceString("CompilerAction.consoleViewOpeningProblem"),
					e));
		} catch (Throwable thr) {
				ExpressCompilerPlugin.log(thr);
			System.out.println("<TO-Console-08 exception: >" + thr);
		}
		
	}

*/
	public static void verifyPath(File path, boolean isFile) {
		// if we are expecting a file back off 1 path element
		if (isFile) {
			if (path.getAbsolutePath().endsWith(File.separator)) {
				// make sure this is a file
				path = path.getParentFile();
				isFile = false;
			}
		}

		// already exists ... just return
		if (path.exists())
			return;

		// does not exist ... ensure parent exists
		File parent = path.getParentFile();
		verifyPath(parent, false);

		// ensure directories are made. Mark files or directories for deletion
		if (!isFile)
			path.mkdir();
		path.deleteOnExit();
	}




	public static void clearDirectory(String dir_str) {

			File dir = new File(dir_str);
	
	    deleteDir(dir);	
		 
			dir.mkdir();
	}


		// Deletes all files and subdirectories under dir.
		// Returns true if all deletions were successful.
	  // If a deletion fails, the method stops attempting to delete and returns false.
	public static void deleteAllFilesAndDirectories(String dir_str) {

			File dir = new File(dir_str);
	
	    deleteDir(dir);	
		
	}
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
       	return dir.delete();
     }		

	/*
	
		take the directory with complex entities and merge together all the .ce files in it into one .ce file in the temporary directory
		this file in the temporary directory  will be fed to the express compiler

		Let's make it a more generic case, maybe we could use it for other purposes:
		input: 
			- the path where we want the concatenated file	
			- the directory with files that we want concatenated
			- the mask of the type of files we  want concatenated (only .ce in this case)
		output:
			we don't need any output, but perhaps boolean ok/error or something
	*/

	public static boolean makeSingleFile(String output_file, String input_directory, String file_mask) {
		boolean result = true;
	
		FilesInDirectory list_of_files = new FilesInDirectory(input_directory, file_mask);
	
		SequenceInputStream s = new SequenceInputStream(list_of_files);
		int c;

		File output = new File(output_file);
		// some unnecessary code
		if (output.exists()) {
			output.delete();
			output = new File(output_file);
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(output);
			while ((c = s.read()) != -1) {
				out.write(c);
			}
			s.close();	
			out.close();
		} catch (FileNotFoundException e) {
            System.err.println("makeSingleFile (1): Can't open " + output);
			result = false;
		} catch (IOException e) {
            System.err.println("makeSingleFile: IO exception " + e);
		  
		  result = false;		
		}
		return result;
	}



	public static void switchPerspective_alt(IWorkbenchPartSite site) {
		try {
			IPreferenceStore prefs = ExpressCompilerPlugin.getDefault().getPreferenceStore();
			boolean auto_switch = prefs.getBoolean(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE);
			if (!auto_switch) {
//				IWorkbenchPartSite site = getSite();
				IWorkbenchWindow window = site.getWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IWorkbench workbench = window.getWorkbench();
				IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
				IPerspectiveDescriptor descriptor = registry.findPerspectiveWithId("net.jsdai.express_compiler.ExpressPerspective");
				if ((page != null) && (page.getPerspective() != descriptor)) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(site.getShell(), "Confirm Perspective Switch",
							"Express files are associated with the Express Perspective. Do you want to switch to this perspective now?",
							"Remember my decision", auto_switch, prefs, ExpressPreferences.AUTO_SWITCH_PERSPECTIVE);
					if (dialog.getReturnCode() == 2)
						page.setPerspective(descriptor);
					auto_switch = dialog.getToggleState();
					prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, auto_switch);
					ExpressCompilerPlugin.getDefault().savePluginPreferences();
				}
			}
		} catch (Throwable t) { 
				ExpressCompilerPlugin.log(t);
//System.out.println("exception when switching perspective: " + t); 
	t.printStackTrace();
			ExpressCompilerPlugin.log(t);
			ExpressCompilerPlugin.console(t.toString());
		}
	}

	 public static void switchProjectPerspective() {
		try {
			IPreferenceStore prefs = ExpressCompilerPlugin.getDefault().getPreferenceStore();
			String auto_switch = prefs.getString(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE);
			if (auto_switch.equalsIgnoreCase("leave")) {
				// do nothing
			} else {
				IWorkbenchPage page = null;
				IWorkbench workbench = PlatformUI.getWorkbench();
				if (workbench.getActiveWorkbenchWindow() != null) {
					page = workbench.getActiveWorkbenchWindow().getActivePage();
				}

/*
				IWorkbenchPartSite site = getSite();
				IWorkbenchWindow window = site.getWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IWorkbench workbench = window.getWorkbench();
*/	
				IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
				IPerspectiveDescriptor descriptor = registry.findPerspectiveWithId("net.jsdai.express_compiler.ExpressPerspective");
				if ((page != null) && (page.getPerspective() != descriptor)) {
					if (auto_switch.equalsIgnoreCase("ask")) {
						MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(ExpressCompilerPlugin.getDefault().getShell(), "Open Associated Perspective?",
								"Express project is associated with the Express Perspective. Do you want to switch to this perspective now?",
								"Remember my decision", false, null, null);

						boolean switch_yes = true;
						if (dialog.getReturnCode() == 2) {
							page.setPerspective(descriptor);
						} else {
							switch_yes = false;
						}
						boolean remember_auto_switch = dialog.getToggleState();
						if (remember_auto_switch) {
							if (switch_yes) {
								prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "switch");
							} else {
								prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "leave");
							}
						} else {
							prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "ask");
						}

						ExpressCompilerPlugin.getDefault().savePluginPreferences();
					}
				}
			}
		} catch (Throwable t) { 

			ExpressCompilerPlugin.log(t);
//			ExpressCompilerPlugin.console(t.toString());
//System.out.println("exception when switching perspective: " + t); 
	t.printStackTrace();
		}
	}

	 public static void switchPerspective(IWorkbenchPartSite site) {
		try {
			IPreferenceStore prefs = ExpressCompilerPlugin.getDefault().getPreferenceStore();
			String auto_switch = prefs.getString(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE);

// System.out.println("auto-switch perspective: " + auto_switch);
			if (auto_switch.equalsIgnoreCase("leave")) {
				// do nothing
			} else {
				IWorkbenchPage page = null;
				IWorkbench workbench = PlatformUI.getWorkbench();
				if (workbench.getActiveWorkbenchWindow() != null) {
					page = workbench.getActiveWorkbenchWindow().getActivePage();
				}

/*
				IWorkbenchPartSite site = getSite();
				IWorkbenchWindow window = site.getWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IWorkbench workbench = window.getWorkbench();
*/	
				IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
				IPerspectiveDescriptor descriptor = registry.findPerspectiveWithId("net.jsdai.express_compiler.ExpressPerspective");
				if ((page != null) && (page.getPerspective() != descriptor)) {
					if (auto_switch.equalsIgnoreCase("ask")) {
				
				
						MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(ExpressCompilerPlugin.getDefault().getShell(), "Open Associated Perspective?",
								"Express files are associated with the Express Perspective. Do you want to switch to this perspective now?",
								"Remember my decision", false, null, null);

						boolean switch_yes = true;
						if (dialog.getReturnCode() == 2) {
							page.setPerspective(descriptor);
						} else {
							switch_yes = false;
						}
						boolean remember_auto_switch = dialog.getToggleState();
						if (remember_auto_switch) {
							if (switch_yes) {
								prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "switch");
							} else {
								prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "leave");
							}
						} else {
							prefs.setValue(ExpressPreferences.AUTO_SWITCH_PERSPECTIVE, "ask");
						}
						ExpressCompilerPlugin.getDefault().savePluginPreferences();
				
				
				} else {
					// switch without asking
					page.setPerspective(descriptor);
				}
			}
		}		
		} catch (Throwable t) { 

			ExpressCompilerPlugin.log(t);
//			ExpressCompilerPlugin.console(t.toString());
//System.out.println("exception when switching perspective: " + t); 
	t.printStackTrace();
		}
	}

  public static PrintWriter getPrintWriter(String file_name) throws IOException {
    FileOutputStream fos = new FileOutputStream(file_name);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    return pw;
  }


	/*
			puth all the java files with paths into the sourcfile 
			from java directory - recursively
			We can use this approach because we now delete java directory before each compilation,
			so we know that older already compiled packages are not included here

	*/
	public static void makeSourceFile(String source_path, String source_file_path) {
		Vector java_files = new Vector();
		File dir = new File(source_path);
		String [] files;
		if (!(dir.isDirectory())) {
			return;
		}
		JavaFilenameFilterForRecursive ef = new JavaFilenameFilterForRecursive();
		files = dir.list(ef);
		for (int i = 0; i < files.length; i++) {
			String a_file_path = source_path + File.separator + files[i];
			File a_file = new File(a_file_path);
			if (a_file.isDirectory()) {
				// do something recursive
					makeSourceFileRecursive(java_files, a_file_path);
			} else {
				java_files.addElement(a_file_path);
			}
		} // for - loop through the java files
		
		// let's make the file here
	
		PrintWriter pw;
		try {
			pw = getPrintWriter(source_file_path);
			Iterator iter = java_files.iterator();
			while(iter.hasNext()) {
				String java_file = (String)iter.next();
				pw.println(java_file);
			}

			pw.flush();
			pw.close();
		} catch (IOException e) {
			ExpressCompilerPlugin.log(e);
		}

		
	}

	public static void makeSourceFileRecursive(Vector java_files, String source_path) {
		File dir = new File(source_path);
		String [] files;
		if (!(dir.isDirectory())) {
			return;
		}
		JavaFilenameFilterForRecursive ef = new JavaFilenameFilterForRecursive();
		files = dir.list(ef);
		for (int i = 0; i < files.length; i++) {
			String a_file_path = source_path + File.separator + files[i];
			File a_file = new File(a_file_path);
			if (a_file.isDirectory()) {
				// do something recursive
					makeSourceFileRecursive(java_files, a_file_path);
			} else {
				java_files.addElement(a_file_path);
			}
		} // for - loop through the java files
	}

	public static void createJsdaiProperties(String jsdaiPropertiesDirectory, String repositoryDirectory) throws IOException {
		Properties jsdaiProperties = new Properties();
		jsdaiProperties.put("repositories", new File(repositoryDirectory).getAbsolutePath());
		FileOutputStream propertyStream = new FileOutputStream(new File(jsdaiPropertiesDirectory, "jsdai.properties"));
		jsdaiProperties.store(propertyStream, null);
		propertyStream.close();
	}
	
}

class JavaFilenameFilterForRecursive implements FilenameFilter {
	JavaFilenameFilterForRecursive() {
	}
	public boolean accept(File dir, String name) {
	  File file = new File(dir,name);
  	if (file.isDirectory()) {
  		return true;
  	} else 
  	if((file.isFile()) && (name.toLowerCase().endsWith(".java"))) {
  		return true;
  	} else {
  		return false;
  	}
	} 
}


class FilesInDirectory implements Enumeration {
	private String input_directory;
	private String file_mask;
	private ArrayList directory_elements;
	private int current = 0;

  public FilesInDirectory(String input_directory, String file_mask) {
    this.input_directory = input_directory;
    this.file_mask = file_mask;
	  directory_elements = getDirectoryElements();
}

	ArrayList getDirectoryElements() {
		ArrayList the_directory_elements = new ArrayList();
		File dir_file = new File(input_directory);
  if (dir_file.isDirectory()) {
      String[] children = dir_file.list();
    for (int i = 0; i < children.length; i++) {
    	if (children[i].endsWith(file_mask)) {
    		File child = new File(dir_file, children[i]);
    		if (!child.isDirectory()) {
    			// ok, take it
    			the_directory_elements.add(child);
    		}
    	}
    }
		}
		return the_directory_elements;
		
	}
	
public boolean hasMoreElements() {
    if (current <  directory_elements.size())
        return true;
    else
        return false;
}	


//public FileInputStream nextElement() {
public Object nextElement() {
    FileInputStream in = null;

    if (!hasMoreElements())
        throw new NoSuchElementException("No more files in directory");
    else {
        File next_file = (File)directory_elements.get(current);
        current++;
        try {
            in = new FileInputStream(next_file);
        } catch (FileNotFoundException e) {
            System.err.println("makeSingleFile: Can't open " + next_file);
        }
    }
    return in;
}


} // class
