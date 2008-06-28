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

package jsdai.util.arm_template;

/**
 * <p> Checks subtypes of specified entity for Cx implementation. Generates simple report. </p>
 * @author Valdas Zigas
 * @version $Revision$
 */

import jsdai.lang.*;
import jsdai.dictionary.*;
import java.io.*;
import java.util.*;
import jsdai.mapping.*;


public class CxBranchTester {
	private static String entityNameString = "part_template";
	private static String defaultArmSchemaString = "jsdai.SAp210_arm.SAp210_arm";
	private static String packageName = "jsdai.SAp210_arm";

    public static void main(String[] args) throws SdaiException, ClassNotFoundException {

		//entity name
		if (args != null && args.length > 0) {
			entityNameString = args[0];
		} else {
			System.out.println("Program usage: ");
			System.out.println("        First parameter entity name to which subtypes to check");
			System.out.println();
		}

		System.out.println("  Initializing JSDAI...");
		//init jsdai
		SdaiSession session = SdaiSession.openSession();
		SdaiRepository repo = session.createRepository("", null);
		SdaiTransaction transaction = session.startTransactionReadWriteAccess();
		repo.openRepository();

		//load arm schema classes
		String armSchemaString = System.getProperty("armSchema", defaultArmSchemaString);
		Class armSchemaClass = Class.forName(armSchemaString);

		SdaiModel modelARM = repo.createSdaiModel("modelARM", armSchemaClass);
		modelARM.startReadWriteAccess();
		//asi.addSdaiModel(modelARM);

		//process definition
		ESchema_definition dictionarySchema = modelARM.getUnderlyingSchema();

		EEntity_definition definition = jsdai.util.LangUtils.findEntityDefinition(entityNameString, dictionarySchema);
		if (definition == null) throw new SdaiException(SdaiException.VA_NVLD, entityNameString + " definition not found");

		List subtypes = new LinkedList();
		jsdai.util.LangUtils.findSubtypes(definition, session.getSystemRepository().getModels(), subtypes);

		EEntity_definition subtypeDefinition = null;
		boolean flag = false;
		int countImplemented = 0;
		int countNotImplemented = 0;
		System.out.println("**** MISSING CX CLASSES for " + entityNameString + " BRANCH ******");
		for (int i = 0; i < subtypes.size(); i++) {
			subtypeDefinition = (EEntity_definition) subtypes.get(i);
			String className = packageName + "." + "Cx" + getNameWithFirstUpper(subtypeDefinition.getName(null));
			flag = false;
			try {
				Class.forName(className);
				flag = true;
			} catch (Exception e) {
			}

			if (!flag) {
			   System.out.println(subtypeDefinition.getName(null));
			   countNotImplemented++;
			} else {
				countImplemented++;
			}
		}
		System.out.println("******************************");

		System.out.println();
		System.out.println("Number of implemented Cx classes: " + countImplemented);
		System.out.println("Number of missing Cx classes: " + countNotImplemented);

		session.closeSession();
		System.out.println("  Finished.");
    }

	/**
	 * Creates new string with first upper case letter.
	 * @param name initial string.
	 * @return new string with first upper case letter.
	 */
	public static String getNameWithFirstUpper(String string) {
		StringBuffer stringBuffer = new StringBuffer(string.toLowerCase());
		stringBuffer.setCharAt(0, Character.toUpperCase(stringBuffer.charAt(0)));
		return stringBuffer.toString();
	}
}
