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

import java.util.Iterator;
import java.util.Set;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.CEntityDefinition;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 10:22:31 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class SchemaType extends Type {
	protected ESchema_definition schema;
	protected EEntity_definition entity;

	SchemaType(ESchema_definition schema) {
		this.schema = schema;
	}

	protected void setParameters(String entityName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		entity = JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, entityName);
		Iterator ctsIter = localContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Iterator currTypeIter = cType.iterator();
			boolean typeFound = false;
			while(currTypeIter.hasNext()) {
				Object currTypeObject = currTypeIter.next();
				EEntity_definition currType;
				if(currTypeObject == LocalSdaiQuery.allTypesToken
				   || (currType = LocalContext.getSchemaEntityType(currTypeObject)) == entity
				   || ((CEntityDefinition)entity).isSubtypeOf(currType)) {
					cType.clear();
					cType.add(LocalContext.entityType(entity));
					typeFound = true;
					break;
				}
			}
			if(!typeFound) {
				cType.clear(); //FIXME: Maybe throw an exception if no types left
			}
		}
	}

	protected void execute(Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		Iterator cisIter = localContext.currentTypes.iterator();
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				if(!(!exact && currInst.isKindOf(entity))
				   && !(exact && currInst.isInstanceOf(entity))) {
					currInstIter.remove();
				}
			}
		}
		executeChildren(context, true);
	}

} // SchemaType
