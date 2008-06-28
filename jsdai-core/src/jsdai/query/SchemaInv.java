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

import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 10:28:47 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class SchemaInv extends Inv {
	protected ESchema_definition schema;
	protected EAttribute attribute;

	SchemaInv(ESchema_definition schema) {
		this.schema = schema;
	}

	protected void setParameters(String entityName, String attributeName,
								 Node containerNode, Context context) throws SdaiException {
		LocalContext localContext = (LocalContext)context;

		EEntity_definition entity = JsdaiLangAccessorLocal.findSchemaEntityDefinition(schema, entityName);
		attribute = JsdaiLangAccessorLocal.findAttribute(entity, attributeName);
		Iterator ctsIter = localContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			if(cType.isEmpty()) continue;
			cType.clear();
			cType.add(LocalContext.entityType(entity));
		}
	}

	protected void execute(Context context) throws SdaiException {
		SchemaInv.execute(context, attribute);
		executeChildren(context, true);
	}

	protected void executeInv(Context context) throws SdaiException {
		executeChildren(context, true);
		SchemaFwd.execute(context, attribute);
	}

	static protected void execute(Context context, EAttribute attribute) throws SdaiException {
		LocalContext localContext = (LocalContext)context;
		ListIterator cisIter = localContext.currentTypes.listIterator();
		ASdaiModel invDomain = localContext.getLocalQuery().domain;
		while(cisIter.hasNext()) {
			Set cis = (Set)cisIter.next();
			Iterator currInstIter = cis.iterator();
			Set invSet = new HashSet();
			while(currInstIter.hasNext()) {
				EEntity currInst = LocalContext.getEntityInstance(currInstIter.next());
				AEntity invVal = new AEntity();
				currInst.findEntityInstanceUsedin(attribute, invDomain, invVal);
				SdaiIterator invValIter = invVal.createIterator();
				while(invValIter.next()) {
					Object invInst = invVal.getCurrentMemberObject(invValIter);
					invSet.add(LocalContext.entityInstance((EEntity)invInst));
				}
			}
			cisIter.set(invSet);
		}
	}

} // SchemaInv
