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

package com.lksoft.util;

import java.util.*;

/**
 * @author Viktoras Kovaliovas
 */
public class ArgumentsParser
{
	public static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private ArgumentsParser() {}
	
	public static String centerString(int length, String s) {
		int sn = s.length();
		if (s.length() < length) {
			StringBuffer sb = new StringBuffer(length);
			for (int i = 0, n = (length - sn) / 2; i < n; i++) sb.append(' ');
			sb.append(s);
			for (int i = sb.length(); i < length; i++) sb.append(' ');
			
			s = sb.toString();
		}
		
		return s;
	}
	
	public static String getLogo(String version, String date, String copyright) {
		copyright = "Copyright (c) " + copyright + " LKSoftware GmbH"; 
		int vl = version.length();
		int dl = date.length();
		int cl = copyright.length();
		int lineLength = Math.max(vl, Math.max(dl, cl));
		
		StringBuffer line = new StringBuffer(lineLength);
		for (int i = 0; i < lineLength; i++)
			line.append('-');
		
		version = centerString(lineLength, version);
		date = centerString(lineLength, date);
		copyright = centerString(lineLength, copyright);
		
		StringBuffer sb = new StringBuffer();
		sb.append(line);
		sb.append(LINE_SEPARATOR);
		sb.append(version);
		sb.append(LINE_SEPARATOR);
		sb.append(date);
		sb.append(LINE_SEPARATOR);
		sb.append(copyright);
		sb.append(LINE_SEPARATOR);
		sb.append(line);
		
		return sb.toString();
	}
	
	public static String getUsage(String mainClass, List argList) {
		// create command line
		StringBuffer sb = new StringBuffer("java ");
		sb.append(mainClass);
		
		argList = new LinkedList(argList);
		
		List keyList = new LinkedList();
		for (ListIterator i = argList.listIterator(); i.hasNext();) {
			Key key = (Key) i.next();
			if (!(key instanceof Value)) {
				keyList.add(key);
				i.remove();
			}
		}

		if (keyList.size() > 0) {
			sb.append(" [");
			Collections.sort(keyList);
			for (ListIterator i = keyList.listIterator(); i.hasNext();) {
				Key key = (Key) i.next();
				sb.append(key.key);
				if (i.hasNext())
					sb.append(' ');
			}
			sb.append("]");
		}
		
		Collections.sort(argList);
		for (ListIterator i = argList.listIterator(); i.hasNext();) {
			Value value = (Value) i.next();
			if (!value.isMandatory)
				sb.append(" [");
			else
				sb.append(' ');
			
			sb.append(value.getKey());
			sb.append(' ');
			sb.append(value.getValueName() == null ? "valueName" : value.getValueName());
			if (!value.isMandatory)
				sb.append("]");
		}
		
		// create arg description
		for (ListIterator i = keyList.listIterator(); i.hasNext();) {
			Key key = (Key) i.next();

			sb.append(LINE_SEPARATOR);
			sb.append(key.getKey());
			sb.append(" : ");
			sb.append(key.getDescription() == null ? "description" : key.getDescription());
		}

		for (ListIterator i = argList.listIterator(); i.hasNext();) {
			Value value = (Value) i.next();

			sb.append(LINE_SEPARATOR);
			sb.append(value.getValueName() == null ? "valueName" : value.getValueName());
			sb.append(" : ");
			sb.append(value.getDescription() == null ? "description" : value.getDescription());
		}
		
		return sb.toString();
	}

	public static boolean parse(String args[], List argList) {
		// parse args
		List unusedkeyList = new LinkedList(argList);
		for (int i = 0; i < args.length; i++) {
			for (ListIterator j = unusedkeyList.listIterator(); j.hasNext();) {
				Key key = (Key) j.next();
				int r = key.set(i, args);
				if (r != -1) {
					i += r;
					j.remove();
					break;
				}
			}
		}

		// check if all mandatory args are set
		for (ListIterator i = argList.listIterator(); i.hasNext();) {
			Key key = (Key) i.next();
			if (!key.validate())
				return false;
		}

		return true;
	}

	public static class Key
		implements Comparable
	{
		protected String key;
		protected String description;
		protected boolean isSet;
		protected boolean caseSensitive;

		public Key(String key) {
			this.key = key;
			caseSensitive = true;
		}

		public Key(String key, boolean caseSensitive) {
			this.key = key;
			this.caseSensitive = caseSensitive;
		}

		public String getKey() {
			return key;
		}

		public boolean getIsSet() {
			return isSet;
		}

		public int set(int offset, String args[]) {
			if (caseSensitive){
				if (args[offset].equals(key)) {
					isSet = true;
					return 0;
				}
			}else{
				if (args[offset].equalsIgnoreCase(key)) {
					isSet = true;
					return 0;
				}
			}

			return -1;
		}

		public boolean validate() {
			return true;
		}
		
		public int compareTo(Object o) {
			return key.compareTo(((Key)o).key);
		}
		
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class Value
		extends Key
	{
		protected boolean isMandatory;
		protected String valueName;
		protected String value;

		public Value(String key, boolean isMandatory) {
			super(key);

			this.isMandatory = isMandatory;
		}

		public Value(String key, boolean isMandatory, boolean caseSensitive) {
			super(key, caseSensitive);

			this.isMandatory = isMandatory;
		}

		public String getValue() {
			return value;
		}

		public int set(int offset, String args[]) {
			int i = super.set(offset, args);
			if (i == -1)
				return -1;

			if (offset++ < args.length)
				value = args[offset];

			return 1;
		}

		public boolean validate() {
			return !isMandatory || value != null;
		}
		
		public int compareTo(Object o) {
			if (!(o instanceof Value))
				return super.compareTo(o);
			
			boolean isMand = ((Value)o).isMandatory;
			if (isMand) {
				if (isMandatory)
					return super.compareTo(o);
				else
					return 1;
			}
			else {
				if (isMandatory)
					return -1;
				else
					return super.compareTo(o);
			}
		}

		public String getValueName() {
			return valueName;
		}

		public void setValueName(String valueName) {
			this.valueName = valueName;
		}
	}
}