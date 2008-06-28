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

import java.io.*;
import java.util.*;

/**
 * <p>Processes mapping data file to get mapping data for entity attributes. </p>
 * Note: works bad when one entity attributes has same name as begining of other atribute name.
 * Example: EData_element: attributes unit_of_measure and unit_of_measure_prefix.
 * @author Valdas Zigas
 * @version $Revision$
 */
public class MappingProcessor {
	private static final String START_ENTITY_MAPPING = "entity_mapping";
	private static final String END_ENTITY_MAPPING = "end_entity_mapping;";

	private static final String START_MAPPING_CONSTRAINTS = "mapping_constraints;";
	private static final String END_MAPPING_CONSTRAINTS = "end_mapping_constraints;";

	private static final String START_ATTRIBUTE_MAPPING = "attribute_mapping";
	private static final String END_ATTRIBUTE_MAPPING = "end_attribute_mapping;";

	//flags
	private static final int FLAG_START_ENTITY_MAPPING = 0;
	private static final int FLAG_START_ENTITY_MAPPING_NAME = 1;
	private static final int FLAG_END_ENTITY_MAPPING = 2;

	private static final int FLAG_START_ATTRIBUTE_MAPPING = 3;
	private static final int FLAG_START_ATTRIBUTE_MAPPING_NAME = 4;
	private static final int FLAG_END_ATTRIBUTE_MAPPING = 5;

	private static final int FLAG_START_MAPPING_CONSTRAINTS = 6;
	private static final int FLAG_END_MAPPING_CONSTRAINTS = 7;

	private static final String MAPPING_CONSTRAINTS_KEY = "MAPPING_CONSTRAINTS_KEY";

	private final Hashtable entities = new Hashtable();

    public MappingProcessor() {
    }

	/**
	 * Initailizes processor with new data file. Must be at least once called before data request.
	 * @param reader array of Reader instances which used to read mapping data.
	 * @throws IOException
	 */
	public void init(Reader readers []) throws IOException {
		for (int i = 0; i < readers.length; i++) {
			init(readers[i]);
		}
	}

	/**
	 * Initailizes processor with new data file. Must be at least once called before data request.
	 * @param reader
	 * @throws IOException
	 */
	public void init(Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);

		String line = null;
		StringBuffer attributeMapping = new StringBuffer();

		String currentEntity = null;
		String currentAttribute = null;
		int flag = -1;
		Hashtable attributes = null;
		List attributeData = null;
		List mappingConstraintData = null;
		List mappingCases = null;

		//process file data lines
		while ( (line = in.readLine()) != null) {

			if (line.length() == 0) continue;

			StringTokenizer st = new StringTokenizer(line);

			//process line tokens
			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				//entity mapping
				if (token.equals(START_ENTITY_MAPPING)) {
					flag = FLAG_START_ENTITY_MAPPING;

				} else if (token.equals(END_ENTITY_MAPPING)) {
					flag = FLAG_END_ENTITY_MAPPING;

				//mapping constraints
				} else if (token.equals(START_MAPPING_CONSTRAINTS)) {
					flag = FLAG_START_MAPPING_CONSTRAINTS;
					mappingConstraintData = new LinkedList();

				} else if (token.equals(END_MAPPING_CONSTRAINTS)) {
					flag = FLAG_END_MAPPING_CONSTRAINTS;

				//attribute mapping
				} else if (token.equals(START_ATTRIBUTE_MAPPING)) {
					flag = FLAG_START_ATTRIBUTE_MAPPING;

				} else if (token.equals(END_ATTRIBUTE_MAPPING)) {
					flag = FLAG_END_ATTRIBUTE_MAPPING;
				} else {
					switch (flag) {
						case FLAG_START_ENTITY_MAPPING:
							 flag = FLAG_START_ENTITY_MAPPING_NAME;
							 currentEntity = token;

							 mappingCases = (List) entities.get(currentEntity);
							 if (mappingCases == null) {
								mappingCases = new LinkedList();
								entities.put(currentEntity, mappingCases);
							 }
							 attributes = new Hashtable();
							 mappingCases.add(attributes);
							 //entities.put(currentEntity, attributes);

							 //System.out.println(currentEntity);
						     break;
						case FLAG_START_ATTRIBUTE_MAPPING:
							 flag = FLAG_START_ATTRIBUTE_MAPPING_NAME;
							 currentAttribute = token;
							 attributeData = new LinkedList();
						     break;
					}
				}
			}

			switch (flag) {
				case FLAG_START_ATTRIBUTE_MAPPING_NAME:
					 attributeData.add(line);
					 break;
				case FLAG_END_ATTRIBUTE_MAPPING:
					 attributeData.add(line);
					 attributes.put(currentAttribute, attributeData);
					 break;

				case FLAG_START_MAPPING_CONSTRAINTS:
					 mappingConstraintData.add(line);
					 break;
				case FLAG_END_MAPPING_CONSTRAINTS:
					 mappingConstraintData.add(line);
					 attributes.put(MAPPING_CONSTRAINTS_KEY, mappingConstraintData);
					 break;
			}

		}
	}

	/**
	 * Returns mapping data of entity.
	 * @param entityName entity name.
	 * @return list which contains entity mapppings. Separate entity attribute mappings contained in hashtables.
	 */
	public List getEntityMappingData(String entityName) {
		return (List) entities.get(entityName);
	}

	/**
	 * Returns mapping data of entity attribute.
	 * @param entityName entity name.
	 * @param attributeName attribute name.
	 * @return list of strings which contains attribute mapping data. Performed data merge if are two or more
	 * attribute mapping cases (example: selects).
	 */
	public List getAttributeMappingData(String entityName, String attributeName) {
		List mappingCases = getEntityMappingData(entityName);

		List result = getComponentMappingData(mappingCases, attributeName);
		return result;
	}

	/**
	 * Returns entity mapping constraints data.
	 * @param entityName entity name.
	 * @return list of strings which contains mapping constraints data.
	 */
	public List getEntityMappingConstraintsData(String entityName) {
		List mappingCases = getEntityMappingData(entityName);

		List result = getComponentMappingData(mappingCases, MAPPING_CONSTRAINTS_KEY);
		return result;
	}


	private List getComponentMappingData(List mappingCases, String componentName) {
		List resultAllCases = new LinkedList();
		List result = null;
		if (mappingCases != null) {
			for (int i = 0; i < mappingCases.size(); i++) {
				result = new LinkedList();
				Hashtable attributes = (Hashtable) mappingCases.get(i);

				Enumeration keys = attributes.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();

					if (key.startsWith(componentName)) {
					   List data = (List) attributes.get(key);
					   if (data != null) {
						  result.addAll(data);
					   }
					}
				}

				if (result.size() > 0) {
					resultAllCases.add(result);
				}
			}
		}
		return resultAllCases;
	}

	/**
	 * Clears red entity mapping data.
	 */
	public void clear() {
		entities.clear();
	}
}
