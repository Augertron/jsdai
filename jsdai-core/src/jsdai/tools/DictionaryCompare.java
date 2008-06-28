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
import java.awt.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import org.w3c.dom.*;

import jsdai.lang.*;
import jsdai.dictionary.*;

/*
<config-options 	target-output="d:\myExt\log\c410to400.txt">

	<compare-schemas schema1="abc_05_22_version" schema2="abc_04_25_version"/>

</config-options>
	
*/

public class DictionaryCompare
{
	private Document loadedDoc = null;
	public DictionaryCompare() throws Exception {
		loadedDoc = readXML("abc.xml");
		if (loadedDoc == null)
			throw new Exception("Failed to read XML. Check the errors and try again.");
	}

	private void compareDefinedTypes(EDefined_type value1,
											 EDefined_type value2,
											 FileWriter outFile, int tabsCount) throws Exception {
		// defined types can be compared by:
		// domain
		EEntity domain1 = value2.getDomain(null);
		EEntity domain2 = value2.getDomain(null);
		String domDef1 = getDomainName(domain1);
		String domDef2 = getDomainName(domain2);
		if (!domDef1.equalsIgnoreCase(domDef2)) {
			writeTabs(outFile, tabsCount);
			outFile.write("Domains are NOT IDENTICAL (SPECIFIC):"+domDef1 +" "+ domDef2);
		}
	}

	private SortedMap buildEnumMap(AEnumeration_type agg) throws Exception {
		SortedMap map = new TreeMap();
		int count = agg.getMemberCount();
		for (int i=1;i<=count;i++) {
			EEnumeration_type t = agg.getByIndex(i);
			// construct enumeration name:
			A_string e = t.getElements(null);
			String fullStr = "";
			for (int j=1;j<=e.getMemberCount();j++) {
				fullStr += e.getByIndex(j)+",";
			}
			map.put(fullStr, t);
		}
		return map;
	}

	private SortedMap buildSelectMap(ASelect_type agg) throws Exception {
		SortedMap map = new TreeMap();
		int count = agg.getMemberCount();
		for (int i=1;i<=count;i++) {
			ESelect_type t = agg.getByIndex(i);
			ANamed_type e = t.getSelections(null);
			SortedMap typesMap = buildMap(e);
			Set keys = typesMap.keySet();
			Iterator it = keys.iterator();
			String fullStr = "";
			while (it.hasNext()) {
				fullStr += (String) it.next() + ",";
			}
			map.put(fullStr, t);
		}
		return map;
	}

	private SortedMap buildAttributes(EEntity_definition def) throws Exception {
		SortedMap map = new TreeMap();
		AExplicit_attribute attributes = def.getExplicit_attributes(null);
		int count = attributes.getMemberCount();
		for (int i=1;i<=count;i++) {
			EAttribute sDef = attributes.getByIndex(i);
			map.put(sDef.getName(null), sDef);
		}
		return map;
	}

	private SortedMap buildInverseAttributes(EEntity_definition def) throws Exception {
		SortedMap map = new TreeMap();
		AAttribute attributes = def.getAttributes(null, null);
		int count = attributes.getMemberCount();
		for (int i=1;i<=count;i++) {
			EAttribute sDef = attributes.getByIndex(i);
			if (sDef instanceof EInverse_attribute)
				map.put(sDef.getName(null), sDef);
		}
		return map;
	}

	private SortedMap buildSupertypes(EEntity_definition def) throws Exception {
		SortedMap map = new TreeMap();
		AEntity supertypes = def.getGeneric_supertypes(null);
		int count = supertypes.getMemberCount();
		for (int i=1;i<=count;i++) {
			EEntity_definition sDef = (EEntity_definition)supertypes.getByIndexObject(i);
			map.put(sDef.getName(null), sDef);
		}
		return map;
	}
	
	private void compareUniquenessRules(EUniqueness_rule value1,
											 EUniqueness_rule value2,
											 FileWriter outFile, int tabsCount) throws Exception {
		// see later, what happens.
	}

	private void compareGlobalRules(EGlobal_rule value1,
											 EGlobal_rule value2,
											 FileWriter outFile, int tabsCount) throws Exception {
		// see later, what happens.
	}

	private String getDomainName(EEntity domain) throws Exception {
		if (domain instanceof ENamed_type)
			return ((ENamed_type) domain).getName(null);
		if (domain instanceof ESimple_type) 
			return domain.getInstanceType().getName(null);
		if (domain instanceof EEnumeration_type) {
			// it's essential to construct enumeration name from its elements,
			// because it can easily happen, that enumerations have identical
			// name but different content
			EEnumeration_type t = (EEnumeration_type) domain;
			A_string e = t.getElements(null);
			String fullStr = "";
			for (int j=1;j<=e.getMemberCount();j++) {
				fullStr += e.getByIndex(j)+",";
			}
			return fullStr;
		}
		if (domain instanceof ESelect_type) {
			ESelect_type t = (ESelect_type) domain;
			ANamed_type e = t.getSelections(null);
			SortedMap typesMap = buildMap(e);
			Set keys = typesMap.keySet();
			Iterator it = keys.iterator();
			String fullStr = "";
			while (it.hasNext()) {
				fullStr += (String) it.next() + ",";
			}			
			return fullStr;
		}
		if (domain instanceof ESet_type) {
			ESet_type set = (ESet_type) domain;
			EEntity elemType = set.getElement_type(null);
			return getDomainName(elemType);
		}
		if (domain instanceof EList_type) {
			EList_type list = (EList_type) domain;
			EEntity elemType = list.getElement_type(null);
			String elmType = getDomainName(elemType);
			boolean isUnique = list.getUnique_flag(null);
			return (isUnique ? "UNIQUE OF "+elmType : elmType);
		}
		throw new Exception("unknown type"+domain);
	}
	
	private void compareExplicitAttributes(EExplicit_attribute value1,
														EExplicit_attribute value2,
														FileWriter outFile,
														int tabsCount) throws Exception {
		// explicit attributes can be compared by:
		// domain
		EEntity domain1 = value1.getDomain(null);
		EEntity domain2 = value2.getDomain(null);
		String domDef1 = getDomainName(domain1);
		String domDef2 = getDomainName(domain2);
		
		//System.out.println("dom1 is "+domain1);
//		String domDef1 = domain1.getInstanceType().getName(null);
//		String domDef2 = domain2.getInstanceType().getName(null);
		//System.out.println("domain is "+domDef1);
		if (!domDef1.equalsIgnoreCase(domDef2)) {
			writeTabs(outFile, tabsCount);
			outFile.write("Domains are NOT IDENTICAL (SPECIFIC):"+domDef1 + " "+domDef2+"\n");
		}
		// redeclaring flag
		if (value1.testRedeclaring(null) != value2.testRedeclaring(null)) {
			writeTabs(outFile, tabsCount);
			outFile.write("Redeclaring property is NOT IDENTICAL (SPECIFIC):"+"\n");
		} else if (value1.testRedeclaring(null)) {
			writeTabs(outFile, tabsCount);
			outFile.write("Comparing redeclared attributes\n");
			EExplicit_attribute r1 = value1.getRedeclaring(null);
			EExplicit_attribute r2 = value2.getRedeclaring(null);
			compareExplicitAttributes(r1, r2, outFile, tabsCount+1);
		}
		// optional flag
		if (value1.testOptional_flag(null) != value2.testOptional_flag(null)) {
			writeTabs(outFile, tabsCount);
			outFile.write("Optional flag is NOT IDENTICAL (SPECIFIC)");
		}
		
	}

	private void compareEntityDefinitions(EEntity_definition value1,
											 EEntity_definition value2,
											 FileWriter outFile, int tabsCount) throws Exception {
		// entity definitions can be compared by:
		// list of supertypes
		SortedMap map1 = buildSupertypes(value1);
		SortedMap map2 = buildSupertypes(value2);
		doMapsComparison(map1, map2, "supertypes", outFile, tabsCount);
		// list of explicit attributes
		map1 = buildAttributes(value1);
		map2 = buildAttributes(value2);
		Vector identical = doMapsComparison(map1, map2, "attributes", outFile, tabsCount);
		for (int i=0;i<identical.size();i++) {
			writeTabs(outFile, tabsCount);
			String name = (String) identical.get(i);
			outFile.write("Comparing identical attributes "+name+"\n");
			EExplicit_attribute atr1 = (EExplicit_attribute) map1.get(name);
			EExplicit_attribute atr2 = (EExplicit_attribute) map2.get(name);
			compareExplicitAttributes(atr1, atr2, outFile, tabsCount);
		}
		// list of inverse attributes:
		map1 = buildInverseAttributes(value1);
		map2 = buildInverseAttributes(value2);
		identical = doMapsComparison(map1, map2, "inverse_attributes", outFile, tabsCount);
		// do nothing with identical for now.
		
		// list of uniqueness and global rules											 	
		AGlobal_rule rule1 = value1.getGlobal_rules(null, null);
		AGlobal_rule rule2 = value2.getGlobal_rules(null, null);
		map1 = buildMap(rule1);
		map2 = buildMap(rule2);
		identical = doMapsComparison(map1, map2, "global_rules", outFile, tabsCount);
		for (int i=0;i<identical.size();i++) {
			String name = (String) identical.get(i);
			EGlobal_rule type1 = (EGlobal_rule) map1.get(name);
			EGlobal_rule type2 = (EGlobal_rule) map2.get(name);
			compareGlobalRules(type1, type2, outFile, tabsCount);
		}

		AUniqueness_rule rule1u = value1.getUniqueness_rules(null, null);
		AUniqueness_rule rule2u = value2.getUniqueness_rules(null, null);
		map1 = buildMap(rule1u);
		map2 = buildMap(rule2u);
		identical = doMapsComparison(map1, map2, "uniqueness_rules", outFile, tabsCount);
		for (int i=0;i<identical.size();i++) {
			String name = (String) identical.get(i);
			EUniqueness_rule type1 = (EUniqueness_rule) map1.get(name);
			EUniqueness_rule type2 = (EUniqueness_rule) map2.get(name);
			compareUniquenessRules(type1, type2, outFile, tabsCount);
		}


	}
	
	private void writeTabs(FileWriter file, int tabsCount) throws Exception {
		for (int i=0;i<tabsCount;i++) {
			file.write("\t");
		}
	}

	private String writeTabs(int tabsCount) {
		String tabs = "";
		for (int i=0;i<tabsCount;i++) {
			tabs += "\t";
		}
		return tabs;
	}


	private Vector doMapsComparison(SortedMap map1, SortedMap map2,
						String type, FileWriter outFile, int tabsCount) throws Exception {
		Vector identical = new Vector();
		String finalStr = "";
		Set keys1 = map1.keySet();
		Iterator it = keys1.iterator();
		while (it.hasNext()) {
			String keyValue = (String) it.next();
			//System.out.println("keyValue is "+keyValue);
			if (map2.containsKey(keyValue)) {
				//finalStr += "\t\t"+keyValue+"\t"+keyValue+"\n";
				//System.out.println("keyValue is specific to both");
				finalStr += writeTabs(tabsCount+1)+keyValue+"\n";
				identical.add(keyValue);
			}
		}
		if (finalStr != "") {
			writeTabs(outFile, tabsCount);
			outFile.write("Printing out "+type+", which are IDENTICAL in both:\n");	
			//outFile.write(finalStr);
		}	
		finalStr = "";
		keys1 = map1.keySet();
		it = keys1.iterator();
		while (it.hasNext()) {
			String keyValue = (String) it.next();
			//System.out.println("keyValue2 is "+keyValue);
			if (!map2.containsKey(keyValue)) {
				//System.out.println("keyValue2 is specific to first");
				//System.out.println("-------------------------------");
				Object value = map1.get(keyValue);
				//System.out.println("value is "+value);
				if (value instanceof EData_type) {
					//System.out.println("it's data type.");
					//System.out.println("name is "+((EData_type) value).getName(null));
				}
				
				//System.out.println("-------------------------------");
				finalStr += writeTabs(tabsCount+1)+keyValue+"\n";
			}
		}
		if (finalStr != "") {
			writeTabs(outFile, tabsCount);
			outFile.write("Printing out "+type+", which are SPECIFIC for first:\n");
			outFile.write(finalStr);
		}	
		finalStr = "";
		Set keys2 = map2.keySet();
		it = keys2.iterator();
		while (it.hasNext()) {
			String keyValue = (String) it.next();
			//System.out.println("keyValue3 is "+keyValue);
			if (!map1.containsKey(keyValue)) {
				//System.out.println("keyValue3 is specific to second");
				//System.out.println("-------------------------------");
				Object value = map1.get(keyValue);
				//System.out.println("value is "+value);
				if (value instanceof EData_type) {
					//System.out.println("it's data type.");
					//System.out.println("name is "+((EData_type) value).getName(null));
				}
				//System.out.println("-------------------------------");

				finalStr += writeTabs(tabsCount+1)+keyValue+"\n";
			}
		}				
		if (finalStr != "") {
			writeTabs(outFile, tabsCount);
			outFile.write("Printing out "+type+", which are SPECIFIC for second:\n");
			outFile.write(finalStr);
		}	
		return identical;
	}

	private void compareSimpleTypes(ESimple_type value1, 
										ESimple_type value2,
										FileWriter outFile) throws Exception {
		if (value1.getInstanceType().getName(null) != value2.getInstanceType().getName(null))
			outFile.write("Simple_type "+value1 + " and simple_type "+
				value2 + " are of different types!");
	}

	private void compareWhere_rules(EWhere_rule rule1, EWhere_rule rule2,
										FileWriter outFile, int tabsCount) throws Exception {
		// well, it's not possible to do further comparison :(
	}

	private void compareWhere_rules(AWhere_rule rule1, AWhere_rule rule2,
										FileWriter outFile, int tabsCount) throws Exception {
		SortedMap map1 = buildMap(rule1);
		SortedMap map2 = buildMap(rule2);
		
		Vector identical = doMapsComparison(map1, map2, "where rules", outFile, tabsCount);
		for (int i=0;i<identical.size();i++) {
			String name = (String) identical.get(i);
			EWhere_rule type1 = (EWhere_rule) map1.get(name);
			EWhere_rule type2 = (EWhere_rule) map2.get(name);
			compareWhere_rules(type1, type2, outFile, tabsCount);
		}		
	}

	private void compareNamedTypes(ENamed_type value1, 
										ENamed_type value2,
										FileWriter outFile, int tabsCount) throws Exception {
		writeTabs(outFile, tabsCount);
		// the names are identical. Check if types are identical as well:
		if ((value1 instanceof EDefined_type) && 
		    (value2 instanceof EEntity_definition)) {
		   writeTabs(outFile, tabsCount);
		   outFile.write("Named_type "+value1 + " and named_type "+
		   	value2 + " are of different types! Further comparison "+
		   	" impossible.");
		   return;
		}
		else if ((value2 instanceof EDefined_type) && 
		    (value1 instanceof EEntity_definition)) {
		   writeTabs(outFile, tabsCount);
		   outFile.write("Named_type "+value1 + " and named_type "+
		   	value2 + " are of different types! Further comparison "+
		   	" impossible.");
		   return;
		}
		else if (value2 instanceof EEntity_definition) {
			// think about comparing where rules
			AWhere_rule rule1 = value1.getWhere_rules(null, null);
			AWhere_rule rule2 = value2.getWhere_rules(null, null);
			compareWhere_rules(rule1, rule2, outFile, tabsCount+1);
			// both values are entity-definitions.
			compareEntityDefinitions((EEntity_definition) value1,
											 (EEntity_definition) value2,
											 outFile, tabsCount+1);
		}
		else {
			// think about comparing where rules
			AWhere_rule rule1 = value1.getWhere_rules(null, null);
			AWhere_rule rule2 = value2.getWhere_rules(null, null);
			compareWhere_rules(rule1, rule2, outFile, tabsCount+1);
			// both values are edefined_types
			compareDefinedTypes((EDefined_type) value1,
									  (EDefined_type) value2,
									  outFile, tabsCount+1);
		}
	}
	
	ANamed_type loadNamedTypes(String schemaName) throws Exception {
		String fullName = schemaName.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel model = MappingUtil.findSystemModel(fullName);
		if (model == null)
			throw new Exception("SdaiModel not found:"+fullName);

		ESchema_definition schemaDef = findSchema(model);
		if (schemaDef == null) {
			throw new Exception("No schema_definition found for "+
				"schema "+ fullName);
		}
		ASdaiModel metaDomain = MappingUtil.findMetaDomain(model);

		ANamed_type types = new ANamed_type();
		ADeclaration declarations = new ADeclaration();
		CDeclaration.usedinParent_schema(null, schemaDef, SdaiSession.getSession().getActiveModels(), declarations);
		SdaiIterator i = declarations.createIterator();
		while (i.next()) {
			EDeclaration declaration = declarations.getCurrentMember(i);
			// commented out these two lines to enable comparison of two big schemas
			// with multiple imports in one rush.
			//if (!(declaration instanceof ELocal_declaration))
			//	continue;
			EEntity e = declaration.getDefinition(null);
			if (e instanceof ENamed_type) {
				types.addUnordered((ENamed_type) e, null);
			}
		}
/*		
		// now filter out this list of named types: leave only those, which are declared
		// locally:
		ANamed_type filtered = new ANamed_type();
		SdaiIterator it = types.createIterator();
		while (it.next()) {
			ENamed_type type = types.getCurrentMember(it);
			if (type instanceof EEntity_definition) {
				if (isLocalDecl((EEntity_definition) type))
					filtered.addUnordered(type);
				continue;
			}
			if (type instanceof EDefined_type) {
				if (isLocalDecl((EDefined_type) type))
					filtered.addUnordered(type);
				continue;
			}
		}
*/		
		return types;
	}

	ASimple_type loadSimpleTypes(String schemaName) throws Exception {
		String fullName = schemaName.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel model = MappingUtil.findSystemModel(fullName);
		if (model == null)
			throw new Exception("SdaiModel not found:"+fullName);

		ESchema_definition schemaDef = findSchema(model);
		if (schemaDef == null) {
			throw new Exception("No schema_definition found for "+
				"schema "+ fullName);
		}
		ASdaiModel metaDomain = MappingUtil.findMetaDomain(model);
		jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESimple_type.class);
		ASimple_type types = new ASimple_type();
		SdaiIterator i = a.createIterator();
		while (i.next()) {
			EEntity e = (EEntity) a.getCurrentMemberObject(i);
			if (e instanceof ESimple_type) {
				types.addUnordered((ESimple_type) e, null);
			}
		}
		return types;
	}

	AEnumeration_type loadEnumTypes(String schemaName) throws Exception {
		String fullName = schemaName.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel model = MappingUtil.findSystemModel(fullName);
		//System.out.println("Model to be used:"+model);
		if (model == null)
			throw new Exception("SdaiModel not found:"+fullName);

		ESchema_definition schemaDef = findSchema(model);
		if (schemaDef == null) {
			throw new Exception("No schema_definition found for "+
				"schema "+ fullName);
		}
		//System.out.println("loadEnumTypes: schemaname="+schemaName);
		ASdaiModel metaDomain = MappingUtil.findMetaDomain(model);
		jsdai.lang.Aggregate a = model.getEntityExtentInstances(EEnumeration_type.class);
		//System.out.println("a="+a);
		AEnumeration_type types = new AEnumeration_type();
		SdaiIterator i = a.createIterator();
		while (i.next()) {
			EEntity e = (EEntity) a.getCurrentMemberObject(i);
			if (e instanceof EEnumeration_type) {
				types.addUnordered((EEnumeration_type) e, null);
			}
		}
		return types;
	}

	ASelect_type loadSelectTypes(String schemaName) throws Exception {
		String fullName = schemaName.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel model = MappingUtil.findSystemModel(fullName);
		if (model == null)
			throw new Exception("SdaiModel not found:"+fullName);

		ESchema_definition schemaDef = findSchema(model);
		if (schemaDef == null) {
			throw new Exception("No schema_definition found for "+
				"schema "+ fullName);
		}
		ASdaiModel metaDomain = MappingUtil.findMetaDomain(model);
		jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESelect_type.class);
		ASelect_type types = new ASelect_type();
		SdaiIterator i = a.createIterator();
		while (i.next()) {
			EEntity e = (EEntity) a.getCurrentMemberObject(i);
			if (e instanceof ESelect_type) {
				types.addUnordered((ESelect_type) e, null);
			}
		}
		return types;
	}


	private SortedMap buildMap(Aggregate agg) throws Exception {
		SortedMap map = new TreeMap();
		int size = agg.getMemberCount();
		for (int i=1;i<=size;i++) {
			EEntity ent = (EEntity) agg.getByIndexObject(i);
			if (ent instanceof ENamed_type) {
				map.put( ((ENamed_type) ent).getName(null), ent);	
				continue;
			}
			if (ent instanceof ESimple_type) {
				map.put( ent.getInstanceType().getName(null), ent);
				continue;
			}
			if (ent instanceof EWhere_rule) {
				map.put( ((EWhere_rule) ent).getLabel(null), ent);	
				continue;
			}
			if (ent instanceof EGlobal_rule) {
				map.put( ((EGlobal_rule) ent).getName(null), ent);	
				continue;
			}
			if (ent instanceof EUniqueness_rule) {
				map.put( ((EUniqueness_rule) ent).getLabel(null), ent);	
				continue;
			}
			throw new Exception ("Unknown type"+ent);
		}
		return map;
	}

	private void compareSchemas(String schema1, String schema2,
						 FileWriter outFile) throws Exception {
		outFile.write("-----------------------------------------------\n");
		outFile.write("Schemas being compared: "+schema1 + " and "+schema2+"\n");
		// compare by number of entities: list entities that are
		// in both, then which are in first, then in second.
		//System.out.println("schema1 is "+schema1);
		//System.out.println("schema2 is "+schema2);
		ANamed_type sch1Types = loadNamedTypes(schema1);
		//System.out.println("sch1 types are"+sch1Types);
		ANamed_type sch2Types = loadNamedTypes(schema2);
		//System.out.println("sch2 types are"+sch2Types);

		SortedMap map1 = buildMap(sch1Types);
		//System.out.println("map1 is"+map1);
		SortedMap map2 = buildMap(sch2Types);
		
		Vector identical = doMapsComparison(map1, map2, "named_types", outFile, 1);
		for (int i=0;i<identical.size();i++) {
			String name = (String) identical.get(i);
			outFile.write("Identical by name named_types are scanned for differences:"+name+"\n");
			ENamed_type type1 = (ENamed_type) map1.get(name);
			ENamed_type type2 = (ENamed_type) map2.get(name);
			compareNamedTypes(type1, type2, outFile, 1);
		}
		
		ASimple_type sTypes1 = loadSimpleTypes(schema1);
		ASimple_type sTypes2 = loadSimpleTypes(schema2);
		
		map1 = buildMap(sTypes1);
		map2 = buildMap(sTypes2);
		identical = doMapsComparison(map1, map2, "simple types", outFile, 1);
		for (int i=0;i<identical.size();i++) {
			String name = (String) identical.get(i);
			ESimple_type type1 = (ESimple_type) map1.get(name);
			ESimple_type type2 = (ESimple_type) map2.get(name);
			compareSimpleTypes(type1, type2, outFile);
		}

		AEnumeration_type eTypes1 = loadEnumTypes(schema1);
		//System.out.println("eTypes 1 is"+eTypes1);
		AEnumeration_type eTypes2 = loadEnumTypes(schema2);
		map1 = buildEnumMap(eTypes1);
		//System.out.println("enumerations map1 is "+map1);
		map2 = buildEnumMap(eTypes2);
		//System.out.println("enumerations map2 is "+map2);
		//System.out.println("==================================================");
		doMapsComparison(map1, map2, "enumeration types", outFile, 1);
		//System.out.println("==================================================");
		//select types?
		ASelect_type selTypes1 = loadSelectTypes(schema1);
		ASelect_type selTypes2 = loadSelectTypes(schema2);
		map1 = buildSelectMap(selTypes1);
		map2 = buildSelectMap(selTypes2);
		//System.out.println("//////////////////////////////////////////////////");
		doMapsComparison(map1, map2, "select types", outFile, 1);
		//System.out.println("//////////////////////////////////////////////////");
		
		outFile.write("Comparison finished.\n");
	}
	
	private static ESchema_definition findSchema(SdaiModel model) throws SdaiException {
		jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESchema_definition.class);
		jsdai.lang.SdaiIterator i = a.createIterator();
		while (i.next()) {
			ESchema_definition s = (ESchema_definition)a.getCurrentMemberObject(i);
				return s;
		}
		return null;
	}
		
	public void doComparison() throws Exception {
		// get output file name and create it there:
		Element root = loadedDoc.getDocumentElement();
		String output = root.getAttribute("target-output");
		//File f = new File(output);
		//f.mkdirs();
		FileWriter outFile = new FileWriter (output);
		NodeList children = root.getChildNodes();
		for (int i=0;i<children.getLength();i++) {
				Node item = children.item(i);
				if (!(item instanceof Element)) 
					continue;
				Element e = (Element) item;
				if (!e.getTagName().equalsIgnoreCase("compare-schemas"))
					throw new Exception ("Unknown XML element encountered!"+e);
				String schema1 = e.getAttribute("schema1");
				String schema2 = e.getAttribute("schema2");
				//System.out.println("before starting schema comparison");
				compareSchemas(schema1, schema2, outFile);
		}
		outFile.flush();
		outFile.close();		
	}

	private Document readXML(String fileName) {
      try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();      	
			f.setValidating(true);
			f.setCoalescing(true);
			DocumentBuilder builder = f.newDocumentBuilder();
			builder.setErrorHandler(new MyErrorHandler());
			InputSource src = new InputSource(DictionaryCompare.class
						  .getResource(fileName).toString()) ;
			loadedDoc = builder.parse(src);
       }
       catch(ParserConfigurationException e) {
       	System.out.println("PROBLEM: "+e.getMessage());
       	e.printStackTrace();
       }
       catch(SAXException sxe) {
	    	Exception  x = sxe;
	    	if (sxe.getException() != null)
			x = sxe.getException();
	    	x.printStackTrace();
       }
       catch (IOException ioe) {
	    	// I/O error
	    	ioe.printStackTrace();
       }
       return loadedDoc;
	}

   static class MyErrorHandler implements ErrorHandler {
		public void error(SAXParseException exception) throws SAXParseException {
			System.out.println ("\r\n** ERROR:"
					+ "\r\n line " + exception.getLineNumber ()
					+ "\r\n url " + exception.getSystemId ());		
	    	throw exception;
		}

		public void fatalError(SAXParseException exception) throws SAXParseException
		{
			System.out.println ("\r\n** FATAL ERROR:"
					+ "\r\n line " + exception.getLineNumber ()
					+ "\r\n url " + exception.getSystemId ());
	    	throw exception;
		}

		public void warning(SAXParseException exception) throws SAXParseException
		{
	   	 System.out.println ("** Warning"
					+ ", line " + exception.getLineNumber ()
					+ ", uri " + exception.getSystemId ());
		    System.out.println("   " + exception.getMessage ());
		}
    }

    public static final void main(String args[]) throws SdaiException, Exception {
    	// start sdai session:
    	SdaiSession session = SdaiSession.openSession();
    	SdaiTransaction trx = session.startTransactionReadOnlyAccess();

    	System.out.println("Comparison tool for XXX schema against IR is starting...");
    	DictionaryCompare cmp = null;
		cmp = new DictionaryCompare();
		cmp.doComparison();
    	
    	System.out.println("Comparison tool finished.");
	}
    
}
