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

header {
	package jsdai.mappingUtils.paths;

	import java.util.*;

	import jsdai.lang.*;
	import jsdai.SExtended_dictionary_schema.*;
	import jsdai.SMapping_schema.*;
	import antlr.*;
}

class MappingDataWalker extends TreeParser;
options {
	importVocab=MappingPathWalker;
}

{
	private MappingPathWalker pathWalker;

	private LinkedList attributePath;

	public MappingDataWalker(MappingPathWalker pathWalker) {
		this();
		this.pathWalker = pathWalker;
	}

	public void reportError(RecognitionException ex) {
		pathWalker.reportError(ex);
	}
}

topType [WalkerState state] throws SdaiException, MappingSemanticException	:
		(	#(TYPE_CONSTRAINT 
				#(TYPE_CONSTRAINT ( id:ID { state.addFirstToList((EntityAST)id); } )+ )
				{ state.getFirst(pathWalker.parser); }
			)
		|	.
		)?
	;

topPathElement [LinkedList attributePath, WalkerState state] returns [EEntity path = null]
throws SdaiException, MappingSemanticException
{	if(topPathElement_AST_in == null) return null;
	this.attributePath = attributePath;
}	:
		(	(TYPE_CONSTRAINT) => path = typeConstraint[state]
		|	path = pathElement
		)?
	;

pathElement returns [EEntity path = null] throws SdaiException, MappingSemanticException	:
		(	(	path = pathConstraint
			|	path = inverseAttributeConstraint
			|	path = aggregateMemberConstraint
			|	path = selectConstraint
			|	path = instanceEqual
			|	path = entityConstraint
			|	path = attribute
			|	path = typeConstraint[null]
			|	path = intersectionConstraint
			|	path = negationConstraint
			)
			{	if(attributePath != null && 
					((ParserAST)pathElement_AST_in).getInPath() == ParserAST.IN_PATH) {

					attributePath.addLast(path);
				}
			}
		|	path = endOfPathConstraint
		|	path = attributeValueConstraint
		|	path = and
		|	path = or
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

pathConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EEntity remainingPath = null;
}	:
		#(PATH_CONSTRAINT 
			(	path = aggregateMemberConstraint
			|	path = inverseAttributeConstraint
			|	path = entityConstraint
			|	path = attribute
			|	path = intersectionConstraint
			|	aio:ATTRIBUTE_INVERSE_ONLY
				{	if(aio != null) {
						throw new MappingSemanticException(aio, "Attribute " + aio.getText() + 
							" can be used in this way only in the context of inverse constraint");
					}
				}
			)
			remainingPath = pathElement
		)
		{	EPath_constraint newPathConstraint = (
				pathWalker.parser.constraintFactory.createPathConstraint(path, remainingPath));
			path = newPathConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

inverseAttributeConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException	:
		(	#(INVERSE_ATTRIBUTE_CONSTRAINT 
				(	path = attribute
				|	path = entityConstraint
				|	path = aggregateMemberConstraint
				|	path = selectConstraint
				)
			)
		|
			#(INVERSE_ATTRIBUTE_CONSTRAINT_SELECT
				(	path = attribute
				|	path = entityConstraint
				|	path = aggregateMemberConstraint
				|	path = selectConstraint
				)
			)
		)
		{	EInverse_attribute_constraint newInvAttConstraint = (
				pathWalker.parser.constraintFactory.createInverseAttributeConstraint(path));
			path = newInvAttConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

aggregateMemberConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException	:
		#(AGGREGATE_MEMBER_CONSTRAINT
			(	path = attribute
			|	path = entityConstraint
			|	path = inverseAttributeConstraint
			|	path = aggregateMemberConstraint
			|	path = selectConstraint
			)
			(member:INT)?
		)
		{	EAggregate_member_constraint newAggMembConstraint = (pathWalker.parser
				.constraintFactory.createAggregateMemberConstraint(path, 
					member != null ? Integer.parseInt(member.getText()) : -1));
			path = newAggMembConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

attributeValueConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EEntity valuePath = null;
	double realValue = 0;
}	:
		(	#(STRING_CONSTRAINT stringValue:STRING valuePath = attributeValueConstraintSelect)
			{	EString_constraint newStringConstraint = (pathWalker.parser.constraintFactory
					.createStringConstraint(valuePath, stringValue.getText()));
				path = newStringConstraint;
			}
		|	#(REAL_CONSTRAINT realValue = intOrFloat valuePath = attributeValueConstraintSelect)
			{	EReal_constraint newRealConstraint = (pathWalker.parser.constraintFactory
					.createRealConstraint(valuePath, realValue));
				path = newRealConstraint;
			}
		|	#(ENUMERATION_CONSTRAINT enumValue:ENUM valuePath = attributeValueConstraintSelect)
			{	EEnumeration_constraint newEnumConstraint = (pathWalker.parser.constraintFactory
					.createEnumerationConstraint(valuePath, enumValue.getText().toLowerCase()));
				path = newEnumConstraint;
			}
		|	#(BOOLEAN_CONSTRAINT boolValue:ENUM valuePath = attributeValueConstraintSelect)
			{	String boolString = boolValue.getText().toUpperCase();
				EBoolean_constraint newBoolConstraint = (pathWalker.parser.constraintFactory
					.createBooleanConstraint(valuePath, boolString.equals("TRUE") ? true : false));
				path = newBoolConstraint;
			}
		|	#(INTEGER_CONSTRAINT intValue:INT valuePath = attributeValueConstraintSelect)
			{	EInteger_constraint newIntConstraint = (pathWalker.parser.constraintFactory
					.createIntegerConstraint(valuePath, Integer.parseInt(intValue.getText())));
				path = newIntConstraint;
			}
		|	#(LOGICAL_CONSTRAINT logicalValue:ENUM valuePath = attributeValueConstraintSelect)
			{	String logicalString = logicalValue.getText().toUpperCase();
				ELogical_constraint newLogicalConstraint = (pathWalker.parser.constraintFactory
					.createLogicalConstraint(valuePath, 
						logicalString.equals("UNKNOWN") ? 3 : (logicalString.equals("TRUE") ? 2 : 1)));
				path = newLogicalConstraint;
			}
		)
		{	if(attributePath != null && 
				((ParserAST)attributeValueConstraint_AST_in).getInPath() == ParserAST.IN_PATH) {
				
				attributePath.addLast(valuePath);
			}
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

intOrFloat returns [double value = 0] :
		(	i:INT
			{ value = Integer.parseInt(i.getText()); }
		|	f:FLOAT
			{ value = new Double(f.getText()).doubleValue(); }
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

attributeValueConstraintSelect returns [EEntity path = null]
throws SdaiException, MappingSemanticException	:
		(	path = attribute
		|	path = aggregateMemberConstraint
		|	path = selectConstraint
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

attribute returns [EEntity path = null]	:
		attribute:ATTRIBUTE
		{ path = ((AttributeAST)attribute).attribute; }
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

selectConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	LinkedList dataTypeList = new LinkedList();
}	:
		#(SELECT_CONSTRAINT 
			#(SELECT_DATA_TYPE 
				(id:ID { dataTypeList.addFirst(((EntityAST)id).declaration.definition); } )+
			)
			( path = aggregateMemberConstraint | path = attribute)
		)
		{	ESelect_constraint newSelectConstraint = (pathWalker.parser.constraintFactory
					.createSelectConstraint(path, dataTypeList));
			path = newSelectConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

instanceEqual returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EEntity path2 = null;
}	:
		#(INSTANCE_EQUAL path = pathElement path2 = pathElement)
		{	EInstance_equal newInstanceEqual = (pathWalker.parser.constraintFactory
					.createInstanceEqual(path, path2));
			path = newInstanceEqual;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

entityConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	LinkedList domainList = new LinkedList();
}	:
		#(ENTITY_CONSTRAINT
			#(TYPE_CONSTRAINT ( id:ID { domainList.add(id); } )+ )
			( path = attribute
			| path = inverseAttributeConstraint
			| path = aggregateMemberConstraint
			)
		)
		{	EntityAST domainEntity = EntityAST.construct(pathWalker.parser, domainList, true);
			EEntity_constraint newEntityConstraint;
			if(domainEntity.exactType) {
				newEntityConstraint = (pathWalker.parser
					.constraintFactory.createExactEntityConstraint(path, 
						(EEntity_definition)domainEntity.declaration.definition));
			} else {
				newEntityConstraint = (pathWalker.parser
					.constraintFactory.createEntityConstraint(path, 
						(EEntity_definition)domainEntity.declaration.definition));
			}
			path = newEntityConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

typeConstraint[WalkerState state] returns [EEntity path = null]
throws SdaiException, MappingSemanticException
{	LinkedList typeList = new LinkedList();
}	:
		#(TYPE_CONSTRAINT 
			#(TYPE_CONSTRAINT ( id:ID { typeList.add(id); } )+ )
			(path = pathElement)?
			{	EntityAST typeEntity = EntityAST.construct(pathWalker.parser, typeList, true);
				boolean create = true;
				if(state != null) {
					EntityAST firstState = state.getFirst(pathWalker.parser);
// 					create = (typeEntity.exactType == firstState.exactType || 
					create = (typeEntity.exactType || 
						!firstState.isEqualTo(typeEntity));
				}
// 				if(state == null || !state.getFirst(pathWalker.parser).isEqualTo(typeEntity)) {
				if(create) {
					EType_constraint newTypeConstraint;
					if(typeEntity.exactType) {
						newTypeConstraint = (pathWalker.parser.constraintFactory
							.createExactTypeConstraint(
								(EEntity_definition)typeEntity.declaration.definition, path));
					} else {
						newTypeConstraint = (pathWalker.parser.constraintFactory
							.createTypeConstraint((EEntity_definition)typeEntity.declaration.definition,
								path));
					}
					path = newTypeConstraint;
				}
			}
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

intersectionConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EIntersection_constraint newIntersectionConstraint = null;
	Set subpaths = new HashSet();
} :
		#(INTERSECTION_CONSTRAINT 
			(path = pathElement { subpaths.add(path); } )+
			{	newIntersectionConstraint = (pathWalker.parser.constraintFactory
					.createIntersectionConstraint(subpaths));
				path = newIntersectionConstraint;
			}
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

negationConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException :
		#(NEGATION path = pathElement
			{	ENegation_constraint newNegationConstraint = (pathWalker.parser.constraintFactory
					.createNegationConstraint(path));
				path = newNegationConstraint;
			}
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

endOfPathConstraint returns [EEntity path = null] throws SdaiException, MappingSemanticException :
		#(END_OF_PATH_CONSTRAINT path = pathElement
			{	EEnd_of_path_constraint newEndOfPathConstraint = (pathWalker.parser.constraintFactory
					.createEndOfPathConstraint(path));
				path = newEndOfPathConstraint;
			}
		)
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

and returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EEntity path2 = null;
}	:
		#(AND_CONSTRAINT path = pathElement path2 = pathElement)
		{	EAnd_constraint_relationship newAndConstraint = (pathWalker.parser.constraintFactory
					.createAndConstraintRelationship(path, path2));
			path = newAndConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}

or returns [EEntity path = null] throws SdaiException, MappingSemanticException
{	EEntity path2 = null;
}	:
		#(or:OR_CONSTRAINT path = pathElement path2 = pathElement)
		{	EOr_constraint_relationship newOrConstraint = (pathWalker.parser.constraintFactory
					.createOrConstraintRelationship(path, path2));
			path = newOrConstraint;
		}
	;
	exception
	catch [RecognitionException e] {
		throw e;
	}
