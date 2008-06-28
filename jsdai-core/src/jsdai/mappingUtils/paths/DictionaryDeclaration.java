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

package jsdai.mappingUtils.paths;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jsdai.SExtended_dictionary_schema.AAttribute;
import jsdai.SExtended_dictionary_schema.AEntity_declaration;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.AType_declaration;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_declaration;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExtended_select_type;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.EType_declaration;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;


/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class DictionaryDeclaration {

	static public final int TYPE = 1;
	static public final int ENTITY = 2;

	//FIXME: this attribue point to containing map in case of type. Used for extensible selects.
	public Map parentMap = null;
	public int type;
	public ENamed_type definition;
	/* 	For extensible selects it contains based_on selects. */
	public HashMap attributes;
	/** Only used for ARM entities, stores instances of EEntity_mapping, 
	 *  keys are AIM entity names (String)
	 */
	public HashMap entityMappings = null;
	/** Only used for ARM entities, stores instances of {@link MappingForAttribute}
	 */
	public LinkedList attributeMappings = null;
	
	static public HashMap create(SdaiModel model) throws SdaiException {
		HashMap map = new HashMap();
		AType_declaration typeDeclarations =
			(AType_declaration)model.getInstances(EType_declaration.class);
		SdaiIterator declarationIterator = typeDeclarations.createIterator();
		while(declarationIterator.next()) {
			EType_declaration typeDeclaration = 
				typeDeclarations.getCurrentMember(declarationIterator);
			EDefined_type type = (EDefined_type)typeDeclaration.getDefinition(null);
			DictionaryDeclaration declaration = new DictionaryDeclaration();
			declaration.type = TYPE;
			declaration.definition = type;
			declaration.attributes = null;
			declaration.parentMap = map; // FIXME: dirty hack for extensible selects
			map.put(type.getName(null), declaration);
		}
		Iterator definedTypeIter = map.values().iterator();
		while(definedTypeIter.hasNext()) {
			DictionaryDeclaration definedTypeDeclaration =
				(DictionaryDeclaration)definedTypeIter.next();
			EDefined_type type = (EDefined_type)definedTypeDeclaration.definition;
			EEntity domain = type.getDomain(null);
			if(domain instanceof EExtended_select_type) {
				EDefined_type extensibleSelectDefType = 
					((EExtended_select_type)domain).getIs_based_on(null);
				String definedTypeName = extensibleSelectDefType.getName(null);
				DictionaryDeclaration extensibleDeclaration = 
					(DictionaryDeclaration)map.get(definedTypeName);
				if(extensibleDeclaration.attributes == null) {
					extensibleDeclaration.attributes = new HashMap();
				}
				extensibleDeclaration.attributes.put(definedTypeDeclaration, definedTypeDeclaration);
			}
		}
		AEntity_declaration entityDeclarations =
			(AEntity_declaration)model.getInstances(EEntity_declaration.class);
		declarationIterator = entityDeclarations.createIterator();
		while(declarationIterator.next()) {
			EEntity_declaration entityDeclaration = 
				entityDeclarations.getCurrentMember(declarationIterator);
			EEntity_definition entity = (EEntity_definition)entityDeclaration.getDefinition(null);
			DictionaryDeclaration declaration = new DictionaryDeclaration();
			declaration.type = ENTITY;
			declaration.definition = entity;
			declaration.attributes = new HashMap();
			declaration.parentMap = map; // FIXME: dirty hack for extensible selects
			collectAttributes(declaration.attributes, entity);
			map.put(entity.getName(null), declaration);
		}
		return map;
	}

	static private void collectAttributes(HashMap attributeMap, EEntity_definition entity)
	throws SdaiException {
		if(entity.testGeneric_supertypes(null)) {
			AEntity supertypes = entity.getGeneric_supertypes(null);
			SdaiIterator supertypeIterator = supertypes.createIterator();
			while(supertypeIterator.next()) {
				EEntity_definition supertype =
					(EEntity_definition)supertypes.getCurrentMemberObject(supertypeIterator);
				collectAttributes(attributeMap, supertype);
			}
		}
		AAttribute atributes = entity.getAttributes(null, null);
		SdaiIterator attributeIterator = atributes.createIterator();
		while(attributeIterator.next()) {
			EAttribute attribute = atributes.getCurrentMember(attributeIterator);
			attributeMap.put(attribute.getName(null), attribute);
		}
	}
	
	public boolean isContained(List list) throws SdaiException {
		Iterator listIterator = list.iterator();
		while(listIterator.hasNext()) {
			ENamed_type listDefinition = (ENamed_type)listIterator.next();
			if(listDefinition == definition || isContainedInSupertype(listDefinition))
				return true;
		}
		return false;
	}
	
	private boolean isContainedInSupertype(ENamed_type listDefinition) throws SdaiException {
		if(listDefinition instanceof EEntity_definition) {
			EEntity_definition entity = (EEntity_definition)listDefinition;
			if(entity.testGeneric_supertypes(null)) {
				AEntity supertypes = entity.getGeneric_supertypes(null);
				SdaiIterator supertypeIterator = supertypes.createIterator();
				while(supertypeIterator.next()) {
					EEntity_definition supertype =
						(EEntity_definition)supertypes.getCurrentMemberObject(supertypeIterator);
					if(supertype == definition) return true;
					if(isContainedInSupertype(supertype)) return true;
				}
			}
		}
		return false;
	}

	public ANamed_type getSelections(EDefined_type defindedType) throws SdaiException {
		Map map = parentMap;
		EEntity domain = defindedType.getDomain(null);
		if(!(domain instanceof ESelect_type)) return null;
		ESelect_type select = (ESelect_type)domain;
		ANamed_type selections = select.getSelections(null);
		DictionaryDeclaration typeDeclaration = (DictionaryDeclaration)map.get(defindedType.getName(null));
		if(typeDeclaration.attributes == null) {
			// Is not extensible
			return selections;
		} else {
			ANamed_type allSelections = new ANamed_type();
			addSelections(allSelections, selections, typeDeclaration);
			return allSelections;
		}
	}
	
	static private void addSelections(ANamed_type allSelections, 
								 ANamed_type selections, 
								 DictionaryDeclaration typeDeclaration) throws SdaiException {
		SdaiIterator selectionIterator = selections.createIterator();
		while(selectionIterator.next()) {
			ENamed_type selection = selections.getCurrentMember(selectionIterator);
			allSelections.addByIndex(1, selection);
		}
		if(typeDeclaration.attributes != null) {
			Iterator extendedTypeIter = typeDeclaration.attributes.values().iterator();
			while(extendedTypeIter.hasNext()) {
				DictionaryDeclaration extendedTypeDeclaration =
					(DictionaryDeclaration)extendedTypeIter.next();
				EDefined_type extendedType = (EDefined_type)extendedTypeDeclaration.definition;
				addSelections(allSelections, 
							  ((ESelect_type)extendedType.getDomain(null)).getSelections(null),
							  extendedTypeDeclaration);
			}
		}
	}

}
