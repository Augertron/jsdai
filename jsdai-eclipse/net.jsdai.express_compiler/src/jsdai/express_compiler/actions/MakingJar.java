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

package jsdai.express_compiler.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Compresses a file or directory into a Jar archive. Users of the
 * class supply the name of the file or directory as an argument.
 */
public class MakingJar {

   /**
    * Creates a Jar archive. If the name of the file passed in is a
    * directory, the directory's contents will be made into a Jar file.
    * @param fileName the file or directory name to add to jar
    * @param jarName jar file name
    * @param fileFilterRegexp regular expression of file names to include or null
    *        to include all files
    * @throws IOException if an exception creating jar occurs 
    */
public static void makeJar(String fileName, String jarName, String fileFilterRegexp) throws IOException {

//System.out.println("Making jar, jar name: " + jarName + " file name: " + fileName);	   
	   
	  File file = new File(fileName);
      Manifest manifest = new Manifest();
      Attributes attributes = manifest.getMainAttributes();
      attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
      attributes.putValue("Created-By", "Eclipse Express Plugin");
      JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarName), manifest);
      Matcher fileFilterMatcher = fileFilterRegexp != null ? Pattern.compile(fileFilterRegexp).matcher("") : null;
      //Call recursion.
      recurseFiles(file, fileName.length()+1, jos, fileFilterMatcher);
      //We are done adding entries to the Jar archive,
      //so close the Jar output stream.
      jos.close();
   }

   /**
    * Recurses down a directory and its subdirectories to look for
    * files to add to the Jar. If the current file being looked at
    * is not a directory, the method adds it to the Jar file.
    * @param jos jar stream to write to
    * @param fileFilterMatcher matcher of file names to include or null
    *        to include all files
    */
   private static void recurseFiles(File file, int substring_start, JarOutputStream jos, Matcher fileFilterMatcher)
   throws IOException {
      if (file.isDirectory()) {
         //Create an array with all of the files and subdirectories
         //of the current directory.
         String[] fileNames = file.list();
         if (fileNames != null) {
            //Recursively add each array entry to make sure that we get 
            //subdirectories as well as normal files in the directory.
            for (int i=0; i<fileNames.length; i++)  {
               recurseFiles(new File(file, fileNames[i]), substring_start, jos, fileFilterMatcher);
            }
         }
      }
      //Otherwise, a file so add it as an entry to the Jar file.
      else if(fileFilterMatcher == null || fileFilterMatcher.reset(file.getName()).matches()) {
    	  byte[] buf = new byte[8192];
    	  int len;
    	  //Create a new Jar entry with the file's name.
//  	  String zip_entry_string = file.toString();
//  	  String cleaned_entry = zip_entry_string.substring(input_dir.length()+1);
//  	  String cleaned_entry = zip_entry_string.substring(input_dir.length()+1);
//  	  System.out.println("ZipEntry: " + zip_entry_string);
//  	  System.out.println("ZipEntry2: " + cleaned_entry);
    	  
    	  JarEntry jarEntry = new JarEntry(file.toString().substring(substring_start)
    			  .replace(File.separatorChar, '/'));
//  	  ZipEntry zipEntry = new ZipEntry(cleaned_entry);
    	  //Create a buffered input stream out of the file
    	  //we're trying to add into the Jar archive.
    	  FileInputStream fin = new FileInputStream(file);
    	  BufferedInputStream in = new BufferedInputStream(fin);
    	  jos.putNextEntry(jarEntry);
    	  //Read bytes from the file and write into the Jar archive.
    	  while ((len = in.read(buf)) >= 0) {
    		  jos.write(buf, 0, len);
    	  }
    	  //Close the input stream.
    	  in.close();
    	  //Close this entry in the Jar stream.
    	  jos.closeEntry();
      }
   }
}

