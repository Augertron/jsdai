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
import jsdai.lang.*;
import jsdai.dictionary.*;
import java.io.*;
import java.util.*;
import jsdai.mapping.*;

/**
 * <p>Class to create CxXXX template classes from CXXX class. </p>
 * Can be used from commandline: first parameter mapping file name (example: clause5-1.path)
 * , following parameters entity names (example: package ee_document)
 * @author Valdas Zigas
 * @version $Revision$
 */

public class CxTemplateCreator {
	private static String entityNameString = "model_image";
	private static String filePath = "clause5-1.path";
	private static String author = "";
	private static String defaultAimSchemaString = "jsdai.SElectronic_assembly_interconnect_and_packaging_design.SElectronic_assembly_interconnect_and_packaging_design";
	private static String defaultArmSchemaString = "jsdai.SAp210_arm.SAp210_arm";

	private static String packageName = null;
	private static MappingProcessor mp = new MappingProcessor();
	private static SdaiContext context = null;
	private static SdaiModel modelARM = null;
	private static HashSet uniqueAttributes = new HashSet();

    public static void main(String[] args) throws IOException, SdaiException, ClassNotFoundException {

		if((args == null) || (args.length < 1)) {
			System.out.println("Program usage: ");
			System.out.println("\tFirst parameter mapping file name or reference to folder which contains files with mapping data (example1: clause5-1.path example2: c:/mappingDir)");
			System.out.println("\tFollowing parameters entity names to generate Cx files (example: package ee_document)");
			System.out.println();
			System.out.println("Java VM property examples: ");
			System.out.println("\t-Dauthor=\"Valdas Zigas\"");
			System.out.println("\t-DaimSchema=\""+ defaultAimSchemaString +"\" ");
			System.out.println("\t-DarmSchema=\""+ defaultArmSchemaString +"\" ");
			System.out.println();
		}

		System.out.println("  Initializing JSDAI...");
		//init jsdai
		SdaiSession session = SdaiSession.openSession();
		SdaiRepository repo = session.createRepository("", null);
		SdaiTransaction transaction = session.startTransactionReadWriteAccess();
		repo.openRepository();


		//load aim/arm schema classes
		String aimSchemaString = System.getProperty("aimSchema", defaultAimSchemaString);
		String armSchemaString = System.getProperty("armSchema", defaultArmSchemaString);
		Class aimSchemaClass = Class.forName(aimSchemaString);
		Class armSchemaClass = Class.forName(armSchemaString);
		packageName = armSchemaString.substring(armSchemaString.lastIndexOf(".") + 1);
		packageName = "jsdai." + packageName;

	    SdaiModel model = repo.createSdaiModel("model", aimSchemaClass);
		model.startReadWriteAccess();
		//asi.addSdaiModel(model);

		modelARM = repo.createSdaiModel("modelARM", armSchemaClass);
		modelARM.startReadWriteAccess();
		//asi.addSdaiModel(modelARM);

		ASdaiModel domain = new ASdaiModel();
		domain.addByIndex(1, model, null);
		context = new SdaiContext(model.getUnderlyingSchema(), domain, model);

		String entities [] = null;

		//file path
		if (args != null && args.length > 0) {
			filePath = args[0];
		}

		if (args != null && args.length > 1) {
			entities = new String [args.length - 1];
			System.arraycopy(args, 1, entities, 0, args.length - 1);
		}


		if (entities == null) {
			entities = new String[] {entityNameString.trim()};
		}

		try {
			File path = new File(filePath);
			if (path.isDirectory()) {
				File files[] = path.listFiles();
				File file = null;
				for (int i = 0; i < files.length; i++) {
					file = files[i];
					if (file.isFile()) {
					   mp.init(new FileReader(file));
					}
				}
			} else {
				mp.init(new FileReader(path));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}

		for (int i = 0; i < entities.length; i++) {
			try {
				System.out.println("  Creating a template for entity \""+entities[i]+"\"...");
				ESchema_definition dictionarySchema = modelARM.getUnderlyingSchema();//session.findSchemaDefinition(schemaDefinitionString);
				createTemplate(entities[i], dictionarySchema);
			} catch (SdaiException e) {
				e.printStackTrace();
			}
		}

		session.closeSession();
		System.out.println("  Finished.");
    }

	/**
	 * Creates Cx template file for specified entity.
	 * @param entityNameString entity name.
	 * @param dictionarySchema dictionary schema
	 * @throws SdaiException
	 * @throws IOException
	 */
	protected static void createTemplate(String entityNameString, ESchema_definition dictionarySchema) throws SdaiException, IOException {
		//dictionarySchema.set
		EEntity_definition definition = jsdai.util.LangUtils.findEntityDefinition(entityNameString, dictionarySchema);
		if (definition == null) throw new SdaiException(SdaiException.VA_NVLD, entityNameString + " definition not found");

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Cx" + getNameWithFirstUpper(definition.getName(null)) + ".java")));

		//create class header
		createClassHeader(definition, out);

		//create "create" method
		createCreateMethod(definition, out);
		out.println();
		//create "remove" method
		createRemoveMethod(definition, out);

		//setters for attributes
		uniqueAttributes.clear();
		createMethodStubs(definition, definition, out);
		out.println("}");

		out.close();
	}

	/**
	 * Creates class header.
	 * @param definition
	 * @param out
	 * @throws SdaiException
	 */
	protected static void createClassHeader(EEntity_definition definition, PrintWriter out) throws SdaiException {
		//header
		out.println("package " + packageName + ";");
		out.println();
		out.println("/**");
		out.println("* <p>Copyright: Copyright (c) 2002-2003</p>");
		out.println("* <p>Company: LKSoft</p>");
		author = System.getProperty("author", "");
		out.println("* @author " + author);
		out.println("* @version " + "$" + "Revision:" + "$");
		out.println("* $" + "Id" + "$");
		out.println("*/");
		out.println();
		out.println("import jsdai.lang.*;");
		out.println("import jsdai.util.*;");
		out.println();
		out.println("public class Cx" + getNameWithFirstUpper(definition.getName(null)) + " extends C" + getNameWithFirstUpper(definition.getName(null)));
		out.println("{");
		out.println();
	}

	/**
	 * Creates "createAimData" method.
	 * @param definition
	 * @param out
	 * @throws SdaiException
	 */
	protected static void createCreateMethod(EEntity_definition definition, PrintWriter out) throws SdaiException {
		out.println("	public void createAimData(SdaiContext context) throws SdaiException");
		out.println("\t{");
		out.println("		if (getModified()) {");
		out.println("			setModified(false);");
		out.println("		} else {");
		out.println("			return;");
		out.println("		}");
		out.println("		commonForAIMInstanceCreation(context);");
		out.println();
		out.println("		setMappingConstraints(context, this);");

		uniqueAttributes.clear();
		createSetMethodReferences(definition, definition, out);
		out.println("	}");
	}

	/**
	 * Creates "removeAimData" method.
	 * @param definition
	 * @param out
	 * @throws SdaiException
	 */
	protected static void createRemoveMethod(EEntity_definition definition, PrintWriter out) throws SdaiException {
		out.println("	public void removeAimData(SdaiContext context) throws SdaiException");
		out.println("\t{");

		uniqueAttributes.clear();

		out.println("\t" + "\t" + "unsetMappingConstraints(context, this);");
		//write unset method references
		createUnsetMethodReferences(definition, definition, out);

		//mapping target detection
		Object targetData [] = findMappingTarget(definition);
		String target = (String) targetData[0];
		String targetPackageName = (String) targetData[1];
		if (targetPackageName.length() > 0) {
		   targetPackageName = targetPackageName.concat(".");
		}

		//mapping target and attribute processing
		out.println("\t" + "\t" + targetPackageName + "E" + getNameWithFirstUpper(target) + " aimEntity = (" + targetPackageName + "E" + getNameWithFirstUpper(target) + ") getAimInstance();");
		out.println("\t" + "\t" + "aimEntity.deleteApplicationInstance();");
		out.println("\t" + "\t" + "setAimInstance(null);");
		out.println("	}");
	}

	/**
	 * Creates references to set methods.
	 * @param entityDefinition
	 * @param topEntityDefinition
	 * @param out
	 * @throws SdaiException
	 */
	protected static void createSetMethodReferences(EEntity_definition entityDefinition, EEntity_definition topEntityDefinition,PrintWriter out) throws SdaiException {
		AEntity_definition directSupertypes = entityDefinition.getSupertypes(null);
		String tabs = "\t\t";

		for (int i = 1; i <= directSupertypes.getMemberCount(); i++) {
			createSetMethodReferences(directSupertypes.getByIndex(i), topEntityDefinition, out);
		}

		out.println();
		out.println(tabs + "//********** \"" + entityDefinition.getName(null) + "\" attributes");
		AExplicit_attribute aAttribute = entityDefinition.getExplicit_attributes(null);
		EExplicit_attribute attribute = null;
		for (int i = 1; i <= aAttribute.getMemberCount(); i++) {
			attribute = aAttribute.getByIndex(i);
			if (!uniqueAttributes.contains(attribute.getName(null))) {
				out.println(tabs + "//" + attribute.getName(null));
				String attrName = getNameWithFirstUpper(attribute.getName(null));
				System.out.println("\tWriting a reference to method set"+attrName+"()");
				out.println(tabs + "set" + attrName + "(context, this);");
				out.println();
		        //System.out.println(entityDefinition.getName(null) + "." + );
				uniqueAttributes.add(attribute.getName(null));
			}
		}
	}

	/**
	 * Creates references to unset methods.
	 * @param entityDefinition
	 * @param topEntityDefinition
	 * @param out
	 * @throws SdaiException
	 */
	protected static void createUnsetMethodReferences(EEntity_definition entityDefinition, EEntity_definition topEntityDefinition,PrintWriter out) throws SdaiException {
		AEntity_definition directSupertypes = entityDefinition.getSupertypes(null);
		String tabs = "\t\t";

		for (int i = 1; i <= directSupertypes.getMemberCount(); i++) {
			createUnsetMethodReferences(directSupertypes.getByIndex(i), topEntityDefinition, out);
		}

		out.println();
		out.println(tabs + "//********** \"" + entityDefinition.getName(null) + "\" attributes");
		AExplicit_attribute aAttribute = entityDefinition.getExplicit_attributes(null);
		EExplicit_attribute attribute = null;
		for (int i = 1; i <= aAttribute.getMemberCount(); i++) {
			attribute = aAttribute.getByIndex(i);
			if (!uniqueAttributes.contains(attribute.getName(null))) {
				out.println(tabs + "//" + attribute.getName(null));
				String attrName = getNameWithFirstUpper(attribute.getName(null));
				System.out.println("\tWriting a reference to method unset"+attrName+"()");
				out.println(tabs + "unset" + attrName + "(context, this);");
				out.println();
		        //System.out.println(entityDefinition.getName(null) + "." + );
				uniqueAttributes.add(attribute.getName(null));
			}
		}
	}

	/**
	 * Creates method stubs.
	 * @param entityDefinition entity definition (can be definition of entity supertype).
	 * @param topEntityDefinition entity defintion.
	 * @param out output.
	 * @throws SdaiException
	 */
	protected static void createMethodStubs(EEntity_definition entityDefinition, EEntity_definition topEntityDefinition, PrintWriter out) throws SdaiException {
		AEntity_definition directSupertypes = entityDefinition.getSupertypes(null);
		String tabs = "\t";

		//mapping constraints
		if (topEntityDefinition == entityDefinition) {
		   createSetMappingConstraintsStub(entityDefinition, out, tabs);
		   createUnsetMappingConstraintsStub(entityDefinition, out, tabs);
		}

		//supertypes
		for (int i = 1; i <= directSupertypes.getMemberCount(); i++) {
			createMethodStubs(directSupertypes.getByIndex(i), topEntityDefinition, out);
		}

		out.println();
		out.println(tabs + "//********** \"" + entityDefinition.getName(null) + "\" attributes");



		AExplicit_attribute aAttribute = entityDefinition.getExplicit_attributes(null);
		EExplicit_attribute attribute = null;

		for (int i = 1; i <= aAttribute.getMemberCount(); i++) {
			attribute = aAttribute.getByIndex(i);

			//test redeclared. Don't works !!!
			if (attribute.testRedeclaring(null)) {
				attribute.getRedeclaring(null);
				System.out.println("redeclared: " + entityDefinition.getName(null) + " " + attribute.getName(null));
		    }

			if (!uniqueAttributes.contains(attribute.getName(null))) {
				//System.out.println(attribute.getName(null) +  " " + attribute.getParent_entity(null));
				createAttributeSetterStub(entityDefinition, topEntityDefinition, attribute, out, tabs);
				createAttributeUnsetterStub(entityDefinition, topEntityDefinition, attribute, out, tabs);
				uniqueAttributes.add(attribute.getName(null));
			}
		}
	}

	/**
	 * Creates stub for mapping constraints setter.
	 * @param entityDefinition
	 * @param out
	 * @param tabs
	 * @throws SdaiException
	 */
	public static void createSetMappingConstraintsStub(EEntity_definition entityDefinition, PrintWriter out, String tabs) throws SdaiException {
		//comment
		out.println();
		out.println(tabs + "/**");
		out.println(tabs + "* Sets/creates data for mapping constraints.");
		out.println(tabs + "*");

		List data = mp.getEntityMappingConstraintsData(entityDefinition.getName(null));
		printMappingData(out, data, tabs);

		out.println(tabs + "* @param context SdaiContext.");
		out.println(tabs + "* @param armEntity arm entity.");
		out.println(tabs + "* @throws SdaiException");
		out.println(tabs + "*/");

		out.print(tabs + "public static void setMappingConstraints(SdaiContext context, ");
		out.println("E" + getNameWithFirstUpper(entityDefinition.getName(null)) + " armEntity) throws SdaiException");
		out.println(tabs + "{");

		//unset
		out.println(tabs + "\t//unset old data");
		out.println(tabs + "\t" + "unsetMappingConstraints(context, armEntity);");
		out.println(tabs + "\t");

		//call mapping constrains from direct supertypes
		AEntity_definition directSupertypes = entityDefinition.getSupertypes(null);
		for (int i = 1; i <= directSupertypes.getMemberCount(); i++) {
			out.println(tabs + "\t" + "Cx" + getNameWithFirstUpper(directSupertypes.getByIndex(i).getName(null)) + ".setMappingConstraints(context, armEntity);");
		}
		//mapping target detection
		Object targetData [] = findMappingTarget(entityDefinition);
		String target = (String) targetData[0];
		String targetPackageName = (String) targetData[1];
		if (targetPackageName.length() > 0) {
		   targetPackageName = targetPackageName.concat(".");
		}

		//mapping target and attribute processing
		out.println(tabs + "\t" + targetPackageName + "E" + getNameWithFirstUpper(target) + " aimEntity = (" + targetPackageName + "E" + getNameWithFirstUpper(target) + ") armEntity.getAimInstance();");

		out.println(tabs + "}");
	}

	/**
	 * Creates stub for mapping constraints setter.
	 * @param entityDefinition
	 * @param out
	 * @param tabs
	 * @throws SdaiException
	 */
	public static void createUnsetMappingConstraintsStub(EEntity_definition entityDefinition, PrintWriter out, String tabs) throws SdaiException {
		//comment
		out.println();
		out.println(tabs + "/**");
		out.println(tabs + "* Unsets/deletes mapping constraint data.");
		out.println(tabs + "*");

		out.println(tabs + "* @param context SdaiContext.");
		out.println(tabs + "* @param armEntity arm entity.");
		out.println(tabs + "* @throws SdaiException");
		out.println(tabs + "*/");

		out.print(tabs + "public static void unsetMappingConstraints(SdaiContext context, ");
		out.println("E" + getNameWithFirstUpper(entityDefinition.getName(null)) + " armEntity) throws SdaiException");
		out.println(tabs + "{");

		//call mapping constrains from direct supertypes
		AEntity_definition directSupertypes = entityDefinition.getSupertypes(null);
		for (int i = 1; i <= directSupertypes.getMemberCount(); i++) {
			out.println(tabs + "\t" + "Cx" + getNameWithFirstUpper(directSupertypes.getByIndex(i).getName(null)) + ".unsetMappingConstraints(context, armEntity);");
		}
		out.println(tabs + "\t");

		//mapping target detection
		Object targetData [] = findMappingTarget(entityDefinition);
		String target = (String) targetData[0];
		String targetPackageName = (String) targetData[1];
		if (targetPackageName.length() > 0) {
		   targetPackageName = targetPackageName.concat(".");
		}

		//mapping target and attribute processing
		out.println(tabs + "\t" + targetPackageName + "E" + getNameWithFirstUpper(target) + " aimEntity = (" + targetPackageName + "E" + getNameWithFirstUpper(target) + ") armEntity.getAimInstance();");

		out.println(tabs + "}");
	}


	/**
	 * Creates stub for attribute data setter.
	 * @param entityDefinition
	 * @param topEntityDefinition
	 * @param attribute
	 * @param out
	 * @param tabs
	 * @throws SdaiException
	 */
	public static void createAttributeSetterStub(EEntity_definition entityDefinition, EEntity_definition topEntityDefinition, EAttribute attribute, PrintWriter out, String tabs) throws SdaiException {
		String aNameWithFirstUpper = getNameWithFirstUpper(attribute.getName(null));
		//System.out.print("\tCreating a method stub for set"+aNameWithFirstUpper+"()...");

		//comment
		out.println();
		out.println(tabs + "/**");
		out.println(tabs + "* Sets/creates data for " + attribute.getName(null) +" attribute.");
		out.println(tabs + "*");

		//entity attribute mapping data
		if (topEntityDefinition == entityDefinition) {
			List data = mp.getAttributeMappingData(entityDefinition.getName(null), attribute.getName(null));
			printMappingData(out, data, tabs);
		}
		out.println(tabs + "* @param context SdaiContext.");
		out.println(tabs + "* @param armEntity arm entity.");
		out.println(tabs + "* @throws SdaiException");

		out.println(tabs + "*/\t");
		out.print(tabs + "public static void set");
		out.print(aNameWithFirstUpper + "(SdaiContext context, ");
		out.println("E" + getNameWithFirstUpper(entityDefinition.getName(null)) + " armEntity) throws SdaiException");
		out.println("\t{");
		//method body
		if (topEntityDefinition != entityDefinition) {
		   out.println(tabs + "\tCx" + getNameWithFirstUpper(entityDefinition.getName(null)) + ".set" + aNameWithFirstUpper + "(context, armEntity);");

		} else {
			//uset before test because optional attributes after change can have no value.
			out.println(tabs + "\t//unset old values");
		    out.println(tabs + "\tunset" + aNameWithFirstUpper + "(context, armEntity);");
			out.println(tabs + "\t");

			//mapping target detection
			Object targetData [] = findMappingTarget(entityDefinition);
			String target = (String) targetData[0];
			String targetPackageName = (String) targetData[1];
			EMappedARMEntity armEntity = (EMappedARMEntity) targetData[2];
			Class cClass = (Class) targetData[3];
			if (targetPackageName.length() > 0) {
			   targetPackageName = targetPackageName.concat(".");
			}

			//generate test if atribute is set
			Class testReturn = null;
			if (cClass != null) {
			   testReturn = getReturnType(cClass, "test" + aNameWithFirstUpper);
			}

			boolean isSelect = false;
			if (testReturn != null && testReturn.getName().equals("int")) {
			   isSelect = true;
			   out.println(tabs + "\t" + "int result = 0;");
			   out.println(tabs + "\t"+ "if ((result = armEntity.test" + aNameWithFirstUpper + "(null)) != 0)");
			   out.println(tabs+"\t" + "{ ");

			} else {
				out.println(tabs + "\t" + "if (armEntity.test" + aNameWithFirstUpper + "(null))");
				out.println(tabs+"\t" + "{ ");
			}

			//mapping target and attribute processing
			out.println(tabs + "\t\t" + targetPackageName + "E" + getNameWithFirstUpper(target) + " aimEntity = (" + targetPackageName + "E" + getNameWithFirstUpper(target) + ") armEntity.getAimInstance();");

			//generate get arm attribute
			Class getReturn = null;
			if (cClass != null) {
				getReturn = getReturnType(cClass, "get" + aNameWithFirstUpper);
			}

			if (getReturn != null) {
				if (isSelect) {
					out.print(tabs + "\t\t" + "//");
				} else {
					out.print(tabs + "\t\t");
				}
			   out.print(getReturn.getName() + " arm" + aNameWithFirstUpper + " = (" + getReturn.getName() + ") ");
			   out.println("armEntity.get" + aNameWithFirstUpper + "(null);");
			} else {
			  out.println(tabs + "\t\tarmEntity.get" + aNameWithFirstUpper + "(null);");
			}
			out.println(tabs);
			out.println(tabs + "\t}");
		}

		out.println(tabs + "}");
		out.println();
	}


	/**
	 * Creates stub for attribute data unsetter.
	 * @param entityDefinition
	 * @param topEntityDefinition
	 * @param attribute
	 * @param out
	 * @param tabs
	 * @throws SdaiException
	 */
	public static void createAttributeUnsetterStub(EEntity_definition entityDefinition, EEntity_definition topEntityDefinition, EAttribute attribute, PrintWriter out, String tabs) throws SdaiException {
		String aNameWithFirstUpper = getNameWithFirstUpper(attribute.getName(null));

		//comment
		out.println();
		out.println(tabs + "/**");
		out.println(tabs + "* Unsets/deletes data for " + attribute.getName(null) +" attribute.");
		out.println(tabs + "*");

		out.println(tabs + "* @param context SdaiContext.");
		out.println(tabs + "* @param armEntity arm entity.");
		out.println(tabs + "* @throws SdaiException");

		out.println(tabs + "*/\t");
		out.print(tabs + "public static void unset");
		out.print(aNameWithFirstUpper + "(SdaiContext context, ");
		out.println("E" + getNameWithFirstUpper(entityDefinition.getName(null)) + " armEntity) throws SdaiException");
		out.println("\t{");

		//method body
		if (topEntityDefinition != entityDefinition) {
		   out.println(tabs + "\tCx" + getNameWithFirstUpper(entityDefinition.getName(null)) + ".unset" + aNameWithFirstUpper + "(context, armEntity);");
		} else {
			//mapping target detection
			Object targetData [] = findMappingTarget(entityDefinition);
			String target = (String) targetData[0];
			String targetPackageName = (String) targetData[1];
			EMappedARMEntity armEntity = (EMappedARMEntity) targetData[2];

			if (targetPackageName.length() > 0) {
			   targetPackageName = targetPackageName.concat(".");
			}

			//mapping target and attribute processing
			out.println(tabs + "\t" + targetPackageName + "E" + getNameWithFirstUpper(target) + " aimEntity = (" + targetPackageName + "E" + getNameWithFirstUpper(target) + ") armEntity.getAimInstance();");
		}
		out.println(tabs + "}");
		out.println();
	}


	/**
	 * Prints mapping data from list.
	 * @param out
	 * @param data data mapping data.
	 * @param tabs
	 */
	public static void printMappingData(PrintWriter out, List data, String tabs) {
		if (data != null) {
			List caseData = null;
			for (int i = 0; i < data.size(); i++) {
				out.println(tabs + "* <p>");
				caseData = (List) data.get(i);
				for (int j = 0; j < caseData.size(); j++) {
				    out.println(tabs + "* " + (String) caseData.get(j) );
					//out.println(tabs + "* <p>" + (String) caseData.get(j) + "</p>");
				}
				out.println(tabs + "* </p> ");
			}
		} else {
		  //System.out.println(entityDefinition.getName(null) + "." + attribute.getName(null) + " mapping not found in data file");
		}
	}

	/**
	 * Creates new string with first upper case letter.
	 * @param string initial string.
	 * @return new string with first upper case letter.
	 */
	public static String getNameWithFirstUpper(String string) {
		StringBuffer stringBuffer = new StringBuffer(string.toLowerCase());
		stringBuffer.setCharAt(0, Character.toUpperCase(stringBuffer.charAt(0)));
		return stringBuffer.toString();
	}


	/**
	 * Finds mapping target from entity_definition
	 * @param entityDefinition entity definition.
	 * @return String array where values:
	 * 1. entity name (default "entity");
	 * 2. target package name (default "")
	 * 3. enity instance
	 * 4. entity class
	 */
	public static Object [] findMappingTarget(EEntity_definition entityDefinition) {
		String target = "entity";
		String targetPackageName = "";
		EMappedARMEntity armEntity = null;
		Class cClass = null;
		try {
			//System.out.print("\n  Now will try to create source entity...");
			cClass = Class.forName(packageName + ".C" + getNameWithFirstUpper(entityDefinition.getName(null)));

			if (entityDefinition.getInstantiable(null)){
				armEntity = (EMappedARMEntity) modelARM.createEntityInstance(entityDefinition);
				EEntity_definition mappingTarget = armEntity.getMappingTarget(context);
				target = mappingTarget.getName(null);
				//System.out.println("success!");

				//System.out.print("  Now will try to create target entity...");
				if (mappingTarget.getInstantiable(null)){
					EEntity mtEntity = context.working_model.createEntityInstance(mappingTarget);
					//System.out.println("success!");
					targetPackageName = mtEntity.getClass().getPackage().getName();
					/*if (targetPackageName.length() > 0) {
						targetPackageName = targetPackageName.concat(".");
					}
					*/
				} else {
					//System.out.println("not fully complete!");
				}
				//System.out.println("success.");
			} else {
					//System.out.println("not fully complete!");
			}

		} catch (SdaiException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Object [] {target, targetPackageName, armEntity, cClass};
	}


	public static Class getReturnType(Class baseClass, String methodName) {
		Class returnType = null;

		java.lang.reflect.Method methods [] = baseClass.getMethods();
		java.lang.reflect.Method method = null;
		for (int m = 0; m < methods.length; m++) {
			method = methods[m];
			if (method.getName().equals(methodName)) {
				break;
			}
		}
		if (method != null) {
			returnType = method.getReturnType();
		}
		return returnType;
	}
}
