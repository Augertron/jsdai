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

package jsdai.util;

import java.io.*;
import java.net.*;
import java.util.*;

/**
	Small and simple class that helps to print information about bugs, assertions and warnings.
*/

public class Debug {

	/**
		Assertion number to indicate that assertion for null failed. It means that expected value can not be null.
	*/
	static public final int ASSERTION_NULL = 1;

	/** Output stream where to write output messages. Default value is System.out. */
	public static java.io.PrintStream out = System.out;

	/** Output stram where to write error messages. Default value is System.err. Note. Changing this value will not change output of stack trace. */
	public static java.io.PrintStream err = System.out;

	/** Does warnings needs to be printed. */
	static boolean printWarnings = true;

	/** Does assertions needs to be printed. */
	static boolean printAssertions = true;

    static boolean debugOutput = true;


    static {
	try
	    {
		InputStream debugPropertiesStream = Debug.class.getResourceAsStream("debug.properties");
		Properties debugProperties = new Properties();
		if (debugPropertiesStream != null)
		    {
			debugProperties.load(debugPropertiesStream);
			String output = debugProperties.getProperty("output");
			if (output == null ||
			    (!output.equalsIgnoreCase("yes") && !output.equalsIgnoreCase("true")))
			    {
				debugOutput = false;
			    }
		    }
	    }
	catch (IOException e)
	    {
	    }
    }

	public static void reportBug() {
		err.println("Bug.");
		Thread.currentThread().dumpStack();
	}

	public static void reportBug(String s) {
		err.println("Bug. " + s);
		Thread.currentThread().dumpStack();
	}

	public static void reportBug(int i) {
		err.println("Bug. " + i);
		Thread.currentThread().dumpStack();
	}

	public static void println(String s) {
		if (debugOutput) {
			out.println(s);
		}
	}

	public static void println(Object s) {
		if (debugOutput) {
			out.println(s);
		}
	}

	public static void reportNotImplemented(String s) {
		err.println("NotImplemented. " + s);
		Thread.currentThread().dumpStack();
	}

	public static void printlnWarning(String s) {
		if (printWarnings) {
			err.println("Warning: " + s);
		}
	}

	public static void printWarning(String s) {
		if (printWarnings) {
			err.print("Warning: " + s);
		}
	}

	public static void printAssertion(String s) {
		if (printAssertions) {
			err.println("Assertion: " + s);
			Thread.currentThread().dumpStack();
		}
	}

	public static void printAssertion(int type, String s) {
		printAssertion(s);
	}

	public static void printDump() {
		Thread.currentThread().dumpStack();
	}

	public static void setPrintWarnings(boolean f) {
		printWarnings = f;
	}

	public static void setPrintAssertions(boolean f) {
		printAssertions = f;
	}

	// The last used bug number is 1.
	// If you want to include your bug report please increase this bug number and use this new number.
	public static void reportPotencialBug(int bugNumber) {
		err.println("You encountered situation where the bug is suspected. Please report this situation No. " + bugNumber + " to info@lksoft.lt.");
	}
}
