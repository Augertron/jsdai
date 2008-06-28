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
public class MappingForAttribute {
	
	public EEntity constraints;
	public LinkedList path;
	public WalkerState state;
	public AttributeAST attribute;
	public EntityAST dataType;
	public EGeneric_attribute_mapping mappingInstance;
	
	protected boolean stateParsed;

	/** Creates new MappingForAttribute */
    public MappingForAttribute(AttributeAST attribute, EntityAST dataType, WalkerState state) {
		this.attribute = attribute;
		this.dataType = dataType;
		this.state = state;
		constraints = null;
		path = null;
		stateParsed = false;
		mappingInstance = null;
    }
	
	private void createConstraintsAndPath(MappingDataWalker mappingDataWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		if(!stateParsed) {
			if(state != null) {
				path = new LinkedList();
				constraints = mappingDataWalker.topPathElement(state.remaining, path, state);
				if(path.size() == 0) path = null;
			}
			stateParsed = true;
		}
	}

	public EEntity getConstraints(MappingDataWalker mappingDataWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		createConstraintsAndPath(mappingDataWalker);
		return constraints;
	}

	public LinkedList getPath(MappingDataWalker mappingDataWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		createConstraintsAndPath(mappingDataWalker);
		return path;
	}
}
