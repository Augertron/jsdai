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

/**
 * @author Viktoras Kovaliovas
 */
public final class LongForm {

	private LongForm() { }

	/**
	 * Generates a express file concantiated of all schemas that are
	 * reachable by specified root schema.
	 */
	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Invalid usage. Should be:");
			System.out.println("java modules.LongForm outputFileName propsFileName");
			System.out.println();
			System.out.println(" outputFileName - the name of file that shall store the result.");
			System.out.println(" propsFileName - the name of file with properties.");
			System.out.println();
			System.out.println("Example:");
			System.out.println("  java modules.LongForm arm.exp longform.prop");
			return;
		}

		String outFile = args[0];

		Properties props;
		try {
			props = Utils.loadProperties(args[1]);
		} catch (IOException e) {
			System.err.println("Fatal error has occurred: " + e);
			return;
		}

		// create long form
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(outFile));

			SortedSet modules = ModuleFinder.getExpress(props);
			for (Iterator i = modules.iterator(); i.hasNext();) {
				String module = (String) i.next();
				BufferedReader reader = new BufferedReader(new FileReader(module));
				String s;
				while ((s = reader.readLine()) != null) {
					writer.println(s);
				}
				reader.close();
			}

			System.out.println("Output in file: " + outFile);
		} catch (IOException e) {
			System.err.println("Fatal error has occurred: " + e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
