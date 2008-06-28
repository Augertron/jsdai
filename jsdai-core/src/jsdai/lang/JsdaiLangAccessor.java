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

package jsdai.lang;


import java.lang.reflect.Field;
import jsdai.dictionary.*;
import jsdai.query.QueryLibProvider;

/**
 * This class is part of JSDAI implementation.
 * It is intended for internal JSDAI use.
 *
 * @author  Vaidas Nargelas
 * @version $Revision$
 */
public class JsdaiLangAccessor {

	protected JsdaiLangAccessor() {
	}

	static protected EExplicit_attribute[] getEntityExplicitAttributes(EEntity_definition definition)
		throws SdaiException {

		CEntityDefinition edef = (CEntityDefinition)definition;
		if(edef.attributes == null) {
            edef.getEntityClass(); 
			//edef.setEarlyBinding(null);
		}
		return edef.attributes;
	}
        
    static protected EExplicit_attribute[] getExplicitAttributesByPartialEntities(EEntity_definition definition)
		throws SdaiException {
            
        CEntityDefinition edef = (CEntityDefinition)definition;
		if(edef.attributes == null) {
            edef.getEntityClass(); 
		}
        
        //System.out.println("--definition.name="+edef.name);
        //System.out.println("--definition.noOfPartialAttributes="+edef.attributes.length);
        
        EExplicit_attribute[] explicitAttributes = new EExplicit_attribute[edef.attributes.length];
        int idx = 0;
        for (int i = 0; i < edef.noOfPartialEntityTypes; i++) {
            CEntityDefinition partial_def = edef.partialEntityTypes[i];
            //System.out.println("--  partial_def name = "+partial_def.name);
            if (partial_def.attributesExpl != null) {
                //System.out.println("--    partial_def.attributesExpl.length = "+partial_def.attributesExpl.length);
                for (int j = 0; j < partial_def.attributesExpl.length; j++) {
                    //System.out.println("--    partial_def.attributesExpl[j].name = "+partial_def.attributesExpl[j].getName(null));
                    explicitAttributes[idx++] = partial_def.attributesExpl[j];
                }
            }
        }
        return explicitAttributes;
    }

	static protected Field[] getEntityAttributeFields(EEntity_definition definition) {
		CEntityDefinition edef = (CEntityDefinition)definition;
		return edef.attributeFields;
	}

	static protected long getInstanceIdentifier(EEntity instance) {
		return ((CEntity)instance).instance_identifier;
	}

	static protected EEntity_definition findDictionaryEntityDefinition(SdaiModel modelDictionary, 
																String entityName)
	throws SdaiException {
		if(modelDictionary.getMode() == SdaiModel.NO_ACCESS) modelDictionary.startReadOnlyAccess();
		entityName = entityName.replace('+', '$');
		SchemaData sch_data = modelDictionary.schemaData;
		int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, entityName.toUpperCase());
		if (index >= 0) {
			return sch_data.entities[index];
		} else {
			throw new SdaiException(SdaiException.EI_NEXS, entityName + " in model " + modelDictionary);
		}
	}

	static protected EEntity_definition findSchemaEntityDefinition(SdaiModel model, String entityName)
	throws SdaiException {
		return model.underlying_schema.getEntityDefinition(entityName);
	}

	static protected EEntity_definition findSchemaEntityDefinitionFast(SdaiModel model, String entityName)
	throws SdaiException {
		return ((SchemaDefinition)model.underlying_schema).getEntityDefinitionFast(entityName);
	}

	static protected EEntity_definition findSchemaEntityDefinition(ESchema_definition schema, String entityName)
	throws SdaiException {
		return schema.getEntityDefinition(entityName);
	}

	static protected EEntity_definition findSchemaEntityDefinitionFast(ESchema_definition schema, String entityName)
	throws SdaiException {
		return ((SchemaDefinition)schema).getEntityDefinitionFast(entityName);
	}

	static protected EDefined_type findSchemaDefinedType(SdaiModel model, String typeName)
	throws SdaiException {
		return model.underlying_schema.getDefinedType(typeName);
	}

	static protected EDefined_type findSchemaDefinedTypeFast(SdaiModel model, String typeName)
	throws SdaiException {
		return ((SchemaDefinition)model.underlying_schema).getDefinedTypeFast(typeName);
	}

	static protected EDefined_type findSchemaDefinedType(ESchema_definition schema, String typeName)
	throws SdaiException {
		return schema.getDefinedType(typeName);
	}

	static protected EDefined_type findSchemaDefinedTypeFast(ESchema_definition schema, String typeName)
	throws SdaiException {
		return ((SchemaDefinition)schema).getDefinedTypeFast(typeName);
	}

	static protected EAttribute findAttributeFast(EEntity_definition entityDef, String attrName)
	throws SdaiException {
		CEntityDefinition edef = (CEntityDefinition)entityDef;
		if(edef.attributes == null) edef.getEntityClass();
		EAttribute attr = edef.extract_attribute(attrName);
		return attr;
	}

	static protected EAttribute findAttribute(EEntity_definition entityDef, String attrName)
	throws SdaiException {
		EAttribute attr = findAttributeFast(entityDef, attrName);
		if (attr != null) {
			return attr;
		} else {
			throw new SdaiException(SdaiException.AT_NDEF, "Attribute " + entityDef.getName(null) +
									"." + attrName);
		}
	}

    static protected ASdaiModel getModels(SdaiRepository repo)
    throws SdaiException {
        return repo.models;
    }
    
    static protected ASchemaInstance getSchemaInstances(SdaiRepository repo)
    throws SdaiException {
        return repo.schemas;
    }

    static protected QueryLibProvider getQueryLibProvider(SdaiSession session) {
		return session.queryLibProvider;
	}

    static protected QueryLibProvider getQueryLibProvider(SdaiTransaction transaction) {
		return transaction.queryLibProvider;
	}

    static protected void setQueryLibProvider(SdaiSession session, QueryLibProvider queryLibProvider) {
		session.queryLibProvider = queryLibProvider;
	}
    
    static protected void setQueryLibProvider(SdaiTransaction transaction, 
											  QueryLibProvider queryLibProvider) {
		transaction.queryLibProvider = queryLibProvider;
	}
    
    static protected long getModelVersion(SdaiModel model) {
		if (model instanceof SdaiModelDictionaryImpl) {
            return ((SdaiModelDictionaryImpl)model).getVersion();
        }
        return -1;
	}

	static protected int getSelectTypeCount(SelectType selectType) {
		return selectType.count;
	}

	static protected CDefined_type[][] getSelectTypePaths(SelectType selectType) {
		return selectType.paths;
	}

	static protected SdaiModel getSchemaModelFromSystemRepository(String schemaName)
	throws SdaiException {
		return SdaiSession.systemRepository.get_schema_model(schemaName);
	}
}
