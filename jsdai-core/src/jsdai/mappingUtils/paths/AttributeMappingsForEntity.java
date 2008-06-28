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

import antlr.*;
import antlr.collections.AST;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;

import java.util.*;

/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class AttributeMappingsForEntity {
	
	public EntityAST entity;
	public EntityAST startEntity;
	public LinkedList mappingsForAttributes;

	/** Creates new AttributeMappingsForEntity */
    public AttributeMappingsForEntity(EntityAST entity, EntityAST startEntity) {
		this.entity = entity;
		this.startEntity = startEntity;
		mappingsForAttributes = new LinkedList();
    }

    public AttributeMappingsForEntity(EntityAST entity, MappingPathParser parser
									  ) throws SdaiException, MappingSemanticException {
		this(entity, extractStartEntity(entity, parser));
	}
	
	public void addMapping(MappingForAttribute mapping) {
		mappingsForAttributes.add(mapping);
	}
	
	public MappingForAttribute getFirstMapping() {
		return (MappingForAttribute)mappingsForAttributes.getFirst();
	}

	public static EntityAST extractStartEntity(EntityAST entity, MappingPathParser parser
											   ) throws SdaiException, MappingSemanticException {
		return entity.pathState != null ? entity.pathState.getFirst(parser) : entity;
	}
}
