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

package jsdai.query;

import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.JsdaiLangAccessor;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

/**
 *
 * Created: Fri May  9 10:16:35 2003
 * Imported from JsdaiLangAccessor class
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class JsdaiLangAccessorLocal extends JsdaiLangAccessor {

	static protected EEntity_definition findDictionaryEntityDefinition(SdaiModel modelDictionary, 
																	   String entityName)
		throws SdaiException {
		return JsdaiLangAccessor.findDictionaryEntityDefinition(modelDictionary, entityName);
	}

	static protected EEntity_definition findSchemaEntityDefinition(SdaiModel model, String entityName)
		throws SdaiException {
		return JsdaiLangAccessor.findSchemaEntityDefinition(model, entityName);
	}

	static protected EEntity_definition findSchemaEntityDefinition(ESchema_definition schema, 
																   String entityName)
		throws SdaiException {
		return JsdaiLangAccessor.findSchemaEntityDefinition(schema, entityName);
	}

	static protected EDefined_type findSchemaDefinedType(SdaiModel model, String typeName)
		throws SdaiException {
		return JsdaiLangAccessor.findSchemaDefinedType(model, typeName);
	}


	static protected EDefined_type findSchemaDefinedType(ESchema_definition schema,
														 String typeName) throws SdaiException{
		return JsdaiLangAccessor.findSchemaDefinedType(schema, typeName);
	}

	static protected EAttribute findAttribute(EEntity_definition entityDef, String attrName)
		throws SdaiException {
		return JsdaiLangAccessor.findAttribute(entityDef, attrName);
	}

    static protected QueryLibProvider getQueryLibProvider(SdaiSession session) {
		return JsdaiLangAccessor.getQueryLibProvider(session);
	}

    static protected QueryLibProvider getQueryLibProvider(SdaiTransaction transaction) {
		return JsdaiLangAccessor.getQueryLibProvider(transaction);
	}

    static protected void setQueryLibProvider(SdaiSession session, QueryLibProvider queryLibProvider) {
		JsdaiLangAccessor.setQueryLibProvider(session, queryLibProvider);
	}
    
    static protected void setQueryLibProvider(SdaiTransaction transaction, 
											  QueryLibProvider queryLibProvider) {
		JsdaiLangAccessor.setQueryLibProvider(transaction, queryLibProvider);
	}

} // JsdaiLangAccessorLocal
