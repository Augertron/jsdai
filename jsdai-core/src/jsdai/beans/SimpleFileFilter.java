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

package jsdai.beans;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

// *     JFileChooser chooser = new JFileChooser();
// *     ExampleFileFilter filter = new ExampleFileFilter(
// *                   new String{"gif", "jpg"}, "JPEG & GIF Images")
// *     chooser.addChoosableFileFilter(filter);
// *     chooser.showOpenDialog(this);
// *

public class SimpleFileFilter extends FileFilter {

	private static String TYPE_UNKNOWN = "Type Unknown";
	private static String HIDDEN_FILE = "Hidden File";

	private Hashtable filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	/**
	* Creates a file filter. If no filters are added, then all
	* files are accepted.
	*
	* @see #addExtension
	*/
	public SimpleFileFilter() {
	   this.filters = new Hashtable();
	}

	/**
	* Creates a file filter that accepts files with the given extension.
	* Example: new ExampleFileFilter("jpg");
	*
	* @see #addExtension
	*/
	public SimpleFileFilter(String extension) {
	   this(extension, null);
	}

	/**
	* Creates a file filter that accepts the given file type.
	* Example: new ExampleFileFilter("jpg", "JPEG Image Images");
	*
	* Note that the "." before the extension is not needed. If
	* provided, it will be ignored.
	*
	* @see #addExtension
	*/
	public SimpleFileFilter(String extension, String description) {
		this();
	   if (extension != null) addExtension(extension);
	   if (description != null) setDescription(description);
	}

	/**
	* Creates a file filter from the given string array.
	* Example: new ExampleFileFilter(String {"gif", "jpg"});
	*
	* Note that the "." before the extension is not needed adn
	* will be ignored.
	*
	* @see #addExtension
	*/
	public SimpleFileFilter(String[] filters) {
	   this(filters, null);
	}

	/**
	* Creates a file filter from the given string array and description.
	* Example: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
	*
	* Note that the "." before the extension is not needed and will be ignored.
	*
	* @see #addExtension
	*/
	public SimpleFileFilter(String[] filters, String description) {
	   this();
	   for (int i = 0; i < filters.length; i++) {
	   // add filters one by one
	      addExtension(filters[i]);
	   }
	   if (description != null) setDescription(description);
	}

	/**
	* Return true if this file should be shown in the directory pane,
	* false if it shouldn't.
	*
	* Files that begin with "." are ignored.
	*
	* @see #getExtension
	* @see FileFilter#accepts
	*/
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) {
				return true;
			}
			// one more try: allow to have * symbol in filter. Restrictions apply:
			// only one symbol '*' should appear, and it should be the last in extension.
			Enumeration allExt = filters.keys();
			String	ext = "";
			while (allExt.hasMoreElements()) {
				ext = (String) allExt.nextElement();
				if (ext.indexOf("*") != -1) {
					int sIndex = ext.indexOf("*");
					String fixedPart = ext.substring(0,sIndex);
					if (extension != null) {
						int useSymbolsForTestCount = 
										(extension.length() < fixedPart.length())
												 ? extension.length() : fixedPart.length();
						String fixedTestPart = extension.substring(0,useSymbolsForTestCount);
						if (fixedPart.equalsIgnoreCase(fixedTestPart)) {
							return true;
						}
					}
				}
			} 
 		}
		return false;
	}

	/**
	* Return the extension portion of the file's name .
	*
	* @see #getExtension
	* @see FileFilter#accept
	*/
	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i>0 && i<filename.length()-1) {
				return filename.substring(i+1).toLowerCase();
			}
		}
		return null;
	}

	/**
	* Adds a filetype "dot" extension to filter against.
	*
	* For example: the following code will create a filter that filters
	* out all files except those that end in ".jpg" and ".tif":
	*
	*   ExampleFileFilter filter = new ExampleFileFilter();
	*   filter.addExtension("jpg");
	*   filter.addExtension("tif");
	*
	* Note that the "." before the extension is not needed and will be ignored.
	*/
	public void addExtension(String extension) {
		if(filters == null) {
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}


	/**
	* Returns the human readable description of this filter. For
	* example: "JPEG and GIF Image Files (*.jpg, *.gif)"
	*
	* @see setDescription
	* @see setExtensionListInDescription
	* @see isExtensionListInDescription
	* @see FileFilter#getDescription
	*/
	public String getDescription() {
		if(fullDescription == null) {
			if(description == null || isExtensionListInDescription()) {
				fullDescription = description==null ? "(" : description + " (";
				// build the description from the extension list
				Enumeration extensions = filters.keys();
				if(extensions != null) {
					fullDescription += "." + (String) extensions.nextElement();
					while (extensions.hasMoreElements()) {
						fullDescription += ", " + (String) extensions.nextElement();
				}
			}
			fullDescription += ")";
			} else {
				fullDescription = description;
			}
		}
		return fullDescription;
	}

	/**
	* Sets the human readable description of this filter. For
	* example: filter.setDescription("Gif and JPG Images");
	*
	* @see setDescription
	* @see setExtensionListInDescription
	* @see isExtensionListInDescription
	*/
	public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
	}

	/**
	* Determines whether the extension list (.jpg, .gif, etc) should
	* show up in the human readable description.
	*
	* Only relevent if a description was provided in the constructor
	* or using setDescription();
	*
	* @see getDescription
	* @see setDescription
	* @see isExtensionListInDescription
	*/
	public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	/**
	* Returns whether the extension list (.jpg, .gif, etc) should
	* show up in the human readable description.
	*
	* Only relevent if a description was provided in the constructor
	* or using setDescription();
	*
	* @see getDescription
	* @see setDescription
	* @see setExtensionListInDescription
	*/
	public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
	}
}
