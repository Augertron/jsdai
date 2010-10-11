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
public class AttributeAST extends EntityAST {

	EAttribute attribute;
	// This field is not propagated with dup()
	int constraintLevelInRemaining;

	/** Creates new AttributeAST */
    public AttributeAST() {
    	constraintLevelInRemaining = -1;
    }

	public AttributeAST(Token tok) {
		initialize(tok);
		constraintLevelInRemaining = -1;
	}

	public String getText() {
		try {
			String tokenText = declaration != null && attribute != null ?
				declaration.definition.getName(null) + "." + attribute.getName(null) : "";
			if(inPath == IN_PATH) tokenText = "(" + tokenText + ")";
			return tokenText;
		} catch (SdaiException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setIdentifiers(MappingPathParser parser, EntityAST entity, Token id, boolean aim)
	throws SemanticException {
		String entityText;
		declaration = entity.declaration;
		if(declaration != null && declaration.type == DictionaryDeclaration.ENTITY) {
			attribute = (EAttribute)declaration.attributes.get(id.getText().toLowerCase());
			if(attribute == null)
				parser.reportError(new SemanticException("Attribute " + entity.getText() +
									"." + id.getText() + " not found",
									parser.getFilename(), id.getLine()));
		} else if((entityText = entity.getText()).length() > 0) {
			parser.reportError(new SemanticException(entityText + " should be an entity for attribute " +
													 id.getText(), parser.getFilename(), id.getLine()));
		} else {
			parser.reportError(new SemanticException("Attribute " + id.getText() +
													 " not found because of unknown entity",
													 parser.getFilename(), id.getLine()));
		}
	}

	public void setIdentifiers(MappingPathParser parser, Token id1, Token id2, boolean aim)
	throws SemanticException {
		setIdentifier(parser, id1, aim);
		if(declaration != null) setIdentifiers(parser, this, id2, aim);
	}

	public EEntity getDomain() throws SdaiException {
		return (EEntity)attribute.get_object(attribute.getAttributeDefinition("domain"));
	}

	public Set getMappedValueSet(EntityAST dataType) throws SdaiException {
		EEntity realDomain = getMappedValueDomain(dataType);
		if(realDomain instanceof EEnumeration_type) {
			HashSet mappedValueSet = new HashSet();
			A_string elements = ((EEnumeration_type)realDomain).getElements(null);
			SdaiIterator elementIter = elements.createIterator();
			while(elementIter.next()) {
				String element = elements.getCurrentMember(elementIter);
				mappedValueSet.add(element);
			}
			return mappedValueSet;
		} else if(realDomain instanceof ELogical_type) {
			HashSet mappedValueSet = new HashSet();
			mappedValueSet.add("true");
			mappedValueSet.add("false");
			mappedValueSet.add("unknown");
			return mappedValueSet;
		} else if(realDomain instanceof EBoolean_type) {
			HashSet mappedValueSet = new HashSet();
			mappedValueSet.add("true");
			mappedValueSet.add("false");
			return mappedValueSet;
		} else {
			return null;
		}
	}

	public EEntity getMappedValueDomain(EntityAST dataType) throws SdaiException {
		return getMappedValueDomain(getDomain(), dataType);
	}

	private EEntity getMappedValueDomain(EEntity domain, EntityAST dataType) throws SdaiException {
		if(domain instanceof EAggregation_type) {
			return getMappedValueDomain(((EAggregation_type)domain).getElement_type(null), dataType);
		} else if(domain instanceof EDefined_type) {
			EDefined_type definedType = (EDefined_type)domain;
			EEntity typeDomain =
				getMappedValueDomain(definedType.getDomain(null), dataType);
			return typeDomain instanceof ESelect_type && dataType != null ?
				getSelectionMappedValueDomain(declaration.getSelections(definedType), dataType) :
				typeDomain;
		} else {
			return domain;
		}
	}

	private EEntity getSelectionMappedValueDomain(ANamed_type selections,
												  EntityAST dataType) throws SdaiException {
		SdaiIterator selectionIter = selections.createIterator();
		while(selectionIter.next()) {
			ENamed_type selection = selections.getCurrentMember(selectionIter);
			if(selection instanceof EDefined_type) {
				EDefined_type selectionType = (EDefined_type)selection;
				ANamed_type selSelections = declaration.getSelections(selectionType);
				if(selSelections != null) {
					EEntity selDomain = getSelectionMappedValueDomain(selSelections, dataType);
					if(selDomain != null) {
						return selDomain;
					} else {
						continue;
					}
				}
			}
			if(dataType.declaration.definition == selection) {
				return getRealDomain(selection);
			}
		}
		return null;
	}


	protected AttributeAST(AttributeAST other) {
		super(other);
		attribute = other.attribute;
		constraintLevelInRemaining = -1;
	}

	public EntityAST dup() {
		return new AttributeAST(this);
	}

	public boolean isSameAttribute(AttributeAST other) {
		return attribute == other.attribute;
	}

	public boolean isEqualTo(EntityAST other) {
		return other instanceof AttributeAST ? isSameAttribute((AttributeAST)other) :  false;
	}


}
