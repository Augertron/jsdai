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

class MappingPathWalker extends TreeParser;
options {
	importVocab=MappingPathParser;
}
tokens {
	INVERSE_ATTRIBUTE_CONSTRAINT;
	INVERSE_ATTRIBUTE_CONSTRAINT_SELECT; // Auxiliary
	AGGREGATE_MEMBER_CONSTRAINT;
	ATTRIBUTE_INVERSE_ONLY;
	AGGREGATE_MEMBER_INVERSE_ONLY;
	STRING_CONSTRAINT;
	REAL_CONSTRAINT;
	ENUMERATION_CONSTRAINT;
	PATH_CONSTRAINT;
	BOOLEAN_CONSTRAINT;
	INTEGER_CONSTRAINT;
	LOGICAL_CONSTRAINT;
	SELECT_CONSTRAINT;
	SELECT_DATA_TYPE;			// Auxiliary
	INSTANCE_EQUAL;
	TYPE_CONSTRAINT;			// Auxiliary
	ENTITY_CONSTRAINT;
	AND_CONSTRAINT;
	OR_CONSTRAINT;
	INTERSECTION_CONSTRAINT;
	END_OF_PATH_CONSTRAINT;
}

{
	static public final int ENTITY_PATH = 0;
	static public final int ATTRIBUTE_PATH = 1;

	static private final int COMPLETION_NOT_FINISHED = 0;
	static private final int COMPLETION_FINISHED_ALL = 1;
	static private final int COMPLETION_FINISHED_CONSTRAINT = 2;

	static private final String NESTED_EXCEPTION_MESSAGE = "";
	static private final String REFERENCED_ERROR_MESSAGE = "Error/warning in referenced mapping";

	public MappingPathParser parser;
	public int line;
    public SourceLocator sourceLocator;

	public EntityAST armEntity;
	public LinkedList aimEntityList = new LinkedList();
	public boolean dontReport;
	public MappingSemanticException notReported;
	private int constraintLevel;
	private boolean errorOccured;
	private boolean pathDetected;

	/** This set contains all possible mapped values or null if mapped values 
	 *	are not used for this attribute. It is set to null in case of entity mapping also
	 */
	private Set mappedValueSet;
	private Map mappedValues;

	/** This flag is true when mapped value was used in mapping path
	 *	this turns mapped value detection off
	 */
	private boolean mappedValueUsed;

	/** This flag is true when mapped value was really used in mapping tree
	 *	either explicitly in mappin path or through detection in constraints
	 */
	private boolean mappedValueReallyUsed;

	private WalkerOptions defaultOpt = new WalkerOptions();

	public MappingPathWalker(MappingPathParser parser) {
		this();
		this.parser = parser;
		setASTNodeClass("jsdai.mappingUtils.paths.MappingAST");
	}

	public void reportError(RecognitionException ex) {
		System.err.println(antlr.FileLineFormatter.getFormatter()
			.getFormatString(sourceLocator.getFilename(), line) + ex.getMessage());
	}

	public void reportError(AST token, String text) {
		reportWarning(token, text, true);
		errorOccured = true;
	}

	public void reportWarning(AST token, String text) {
		MappingSemanticException nr = notReported;
		notReported = null;
		for( ; nr != null; nr = (MappingSemanticException)nr.getCause()) {
			reportWarning(nr.token, nr.message, false);
		}
		reportWarning(token, text, true);
	}

	private void reportWarning(AST token, String text, boolean useSourceLocator) {
		boolean wasReported = false;
		for(ListIterator i = locators.listIterator(locators.size()); i.hasPrevious(); ) {
			Object locatorObj = i.previous();
			if(locatorObj instanceof SourceLocator) {
				SourceLocator locator = (SourceLocator)locatorObj;
				if(!wasReported) {
					int reportLine = line >= 0 ? line : locator.getLine();
					System.err.println(antlr.FileLineFormatter.getFormatter()
									   .getFormatString(locator.getFilename(), 
														token instanceof ParserAST
														? ((ParserAST)token).getLine()
														: reportLine) + text);
					wasReported = true;
				} else {
					System.err.println(antlr.FileLineFormatter.getFormatter()
									   .getFormatString(locator.getFilename(), locator.getLine()) +
									   REFERENCED_ERROR_MESSAGE);
				}
			}
		}
		if(useSourceLocator) {
			if(!wasReported) {
				int reportLine = line >= 0 ? line : sourceLocator.getLine();
				System.err.println(antlr.FileLineFormatter.getFormatter()
								   .getFormatString(sourceLocator.getFilename(), 
													token instanceof ParserAST
													? ((ParserAST)token).getLine() : reportLine) + text);
			} else {
				System.err.println(antlr.FileLineFormatter.getFormatter()
								   .getFormatString(sourceLocator.getFilename(), sourceLocator.getLine()) +
								   REFERENCED_ERROR_MESSAGE);
			}
		}
	}

	private static class SourceLocatorMark {
		private SourceLocatorMark() { }
	}

	private List locators = new ArrayList();

	private void clearLocators(SourceLocatorMark mark) {
		for(ListIterator i = locators.listIterator(locators.size()); i.hasPrevious(); ) {
			Object locator = i.previous();
			i.remove();
			if(locator == mark) {
				break;
			}
		}
	}

	private void pushLocatorMark(SourceLocatorMark mark) {
		locators.add(mark);
	}

	private void pushLocator(SourceLocator sourceLocator) {
		locators.add(sourceLocator);
	}

	private List dupLocators() {
		List locatorCopy = new ArrayList(locators.size() + 1);
		locatorCopy.add(sourceLocator);
		locatorCopy.addAll(locators);
		return locatorCopy;
	}

	// Utility methods used for mapping path construction
	public AST getRemainingOverPathConstraint(WalkerState state) {
		return getRemainingOverPathConstraint(state.remaining);
	}

	public AST getRemainingOverPathConstraint(AST tree) {
		if(tree != null) {
			return (tree.getType() == PATH_CONSTRAINT ? 
				tree.getFirstChild() : tree);
		} else
		return null;
	}

	public void setRemainingOverPathConstraint(WalkerState state, AST newRemaining) {
		if(state.remaining != null) {
			if(state.remaining.getType() == PATH_CONSTRAINT) {
				AST sibling = state.remaining.getFirstChild().getNextSibling();
				state.remaining = #(auxiliaryAST(newRemaining, PATH_CONSTRAINT), newRemaining, sibling);
			} else {
				state.remaining = newRemaining;
			}
		} else
			state.remaining = newRemaining;
	}

	static public AST dupAttachTree(AST ast) {
		if(ast == null) return null;
		if(ast instanceof ParserAST) {
			return ((ParserAST)ast).dupAttachTree();
		}
		AST newAst = new CommonAST();
		newAst.setType(ast.getType());
		newAst.setText(ast.getText());
		newAst.setFirstChild(ast.getFirstChild());
		return newAst;
	}

	static public AST dupWholeTree(AST ast) {
		if(ast == null) return null;
		AST newAst = dupAttachTree(ast);
		if(ast.getFirstChild() != null) newAst.setFirstChild(dupWholeTree(ast.getFirstChild()));
		if(ast.getNextSibling() != null) newAst.setNextSibling(dupWholeTree(ast.getNextSibling()));
		return newAst;
	}

	public AST auxiliaryAST(int type) {
		return auxiliaryAST(null, type);
	}

	public AST auxiliaryAST(AST parentAST, int type) {
		int line = 0;
		String name = (parentAST != null && parentAST instanceof ParserAST && 
			(line = ((ParserAST)parentAST).getLine()) != 0 ? 
			_tokenNames[type] + " at line " + line :
			_tokenNames[type] + (parentAST != null ? " nearby " + parentAST.getText() : ""));
		AST newAst = (#[type, name]);
		((ParserAST)newAst).setLine(line);
		return newAst;
	}

	private static AST skipOverPathElement(AST node) {
		int nodeType;
		while(node != null && ((nodeType = node.getType()) == MappingPathParser.TEMPLATE_CALL
							   || nodeType == MappingPathParser.ORIGINAL_LOCATION)) {
			node = node.getNextSibling();
		}
		if(node != null) {
			node = node.getNextSibling();
			if(node != null && node.getType() == MappingPathParser.ENUM) {
				node = node.getNextSibling();
			}
		}
		return node;
	}

	// Mapping path construction methods

	private void cleanInnerElement(LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		if(remainingStateList != null && innerElementOpt.reachedAndEnd != null) {
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				remainingState.remaining = null;
//DEBUG 				reportWarning(remainingState.getFirst(parser), 
// 					"reached andEnd at " + remainingState.getFirst(parser));
			}
		}
	}

	public LinkedList stateAtEntity(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			WalkerState remainingState = new WalkerState(id);
			if(id.exactType) {
				addTypeConstraint(id, remainingState, id.dup());
			}
			return WalkerState.createList(remainingState);
		} else {
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				EntityAST remainingFirst = remainingState.getFirst(parser);
				if(!id.isSame(remainingFirst)) {
					if(id.isSelectOf(remainingFirst) || remainingFirst.isSelectOf(id) ||
					   remainingFirst.isExtensionOf(id) || id.isExtensionOf(remainingFirst)) {

						// Ugly hack for select
						remainingState.setFirst(id);
					} else {
						throw new MappingSemanticException(id, "Illegal sequence: " + id.getText() + 
							" " + remainingFirst.getText());
					}
				}
				if(id.exactType) {
					addTypeConstraint(id, remainingState, id.dup());
				}
			}
			return remainingStateList;
		}
	}

	public LinkedList stateAtExact(EntityAST id, LinkedList remainingStateList, WalkerOptions options)
	throws MappingSemanticException, SdaiException {
		if(id.exactType) {
			if(options.postProcess) remainingStateList = postProcessState(remainingStateList, false);
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				addTypeConstraint(id, remainingState, id.dup());
			}
		}
		return remainingStateList;
	}

	private void addTypeConstraint(EntityAST id, WalkerState remainingState, AST typeConstraintAST) {
		AST typeConstraintBody = auxiliaryAST(id, TYPE_CONSTRAINT);
		typeConstraintBody.setFirstChild(typeConstraintAST);
		if(remainingState.remaining != null) {
			if(remainingState.remaining.getType() != TYPE_CONSTRAINT) {
				remainingState.remaining = (
					#(auxiliaryAST(id, TYPE_CONSTRAINT), typeConstraintBody, remainingState.remaining)
				);
			} else {
				// #(T.C #(T.C. dataType etc.) remaining)
				remainingState.remaining.getFirstChild().addChild(typeConstraintAST);
			}
		} else {
			remainingState.remaining = #(auxiliaryAST(id, TYPE_CONSTRAINT), typeConstraintBody);
		}
	}

	public LinkedList stateAtEntityAggreg(EntityAST id, AST index, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(id.isEntityDefinition()) {
			throw new MappingSemanticException(id, "Defined type expected here");
		}
		EEntity domain = id.getRealDomain();
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			if(!id.isSame(remainingState.getFirst(parser))) {
				throw new MappingSemanticException(id, "Illegal sequence: " + id.getText() + 
												   "[] " + remainingState.getFirst(parser).getText());
			}
			AST topRemaining = getRemainingOverPathConstraint(remainingState);
			int topRemainingType = topRemaining.getType();
			if(topRemainingType == INVERSE_ATTRIBUTE_CONSTRAINT
			|| topRemainingType == INVERSE_ATTRIBUTE_CONSTRAINT_SELECT) {
				AST oldInverse = topRemaining.getFirstChild();
				AST oldNextSibling = oldInverse.getNextSibling();
				oldInverse.setNextSibling(null);
				AST aggregateMemberConstraint = #(auxiliaryAST(id, AGGREGATE_MEMBER_CONSTRAINT), 
					oldInverse, astFactory.dup(index));
				aggregateMemberConstraint.setNextSibling(oldNextSibling);
				topRemaining.setFirstChild(aggregateMemberConstraint);
			} else {
				setRemainingOverPathConstraint(remainingState, 
					#(auxiliaryAST(id, INVERSE_ATTRIBUTE_CONSTRAINT), 
						#(auxiliaryAST(id, AGGREGATE_MEMBER_CONSTRAINT), 
							topRemaining, astFactory.dup(index)),
						dupAttachTree(topRemaining)
					)
				);
			}
		}
		return remainingStateList;
	}

	public LinkedList stateAtEqEntity(EntityAST id, LinkedList remainingStateList)
	throws MappingSemanticException {
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Value is missing: " + id.getText());
		}
		if(id.declaration.type != DictionaryDeclaration.TYPE) {
			throw new MappingSemanticException(id, "Defined type expected but entity definition found: " +
											   id.getText());
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			remainingState.setFirst(id);
			remainingState.remaining = #(auxiliaryAST(id, EQ_ENTITY), remainingState.remaining);
		}
		return remainingStateList;
	}
	
	public LinkedList stateAtFromPoint(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " <-");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			if(remainingState.remaining == null) {
				throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " <-");
			}
			AST typeConstraint = null;
			if(remainingState.remaining.getType() == TYPE_CONSTRAINT) {
				// #(T.C #(T.C. dataType etc.) remaining)
				typeConstraint = dupAttachTree(remainingState.remaining.getFirstChild());
				remainingState.remaining = remainingState.remaining.getFirstChild().getNextSibling();
			}
			AST attributeAST = getRemainingOverPathConstraint(remainingState);
			EntityAST remainingFirst = remainingState.getFirst(parser);

			if(remainingFirst.isDomainValid(id)) {

				int remainingType = remainingState.remaining.getType();
				if(remainingType == PATH_CONSTRAINT) {
					remainingType = remainingState.remaining.getFirstChild().getType();
				}
				boolean wrongRemaining = false;
				if(remainingFirst instanceof AttributeAST) {
					if(remainingType != ATTRIBUTE 
					&& remainingType != AGGREGATE_MEMBER_CONSTRAINT 
					&& remainingType != ATTRIBUTE_INVERSE_ONLY 
					&& remainingType != AGGREGATE_MEMBER_INVERSE_ONLY) {
						wrongRemaining = true;
					}
				} else {
					if(remainingType != INVERSE_ATTRIBUTE_CONSTRAINT
					&&  remainingType != INVERSE_ATTRIBUTE_CONSTRAINT_SELECT) {
						wrongRemaining = true;
					}
				}
				if(wrongRemaining) {
					throw new MappingSemanticException(id, "Wrong type follows after " + id.getText() +
													   " <- " + remainingFirst +
													   " : " + _tokenNames[remainingType]);
				}
				if(remainingFirst instanceof AttributeAST) {
					AST iac = auxiliaryAST(id, (id.isSelectType() ? 
							INVERSE_ATTRIBUTE_CONSTRAINT_SELECT :
							INVERSE_ATTRIBUTE_CONSTRAINT));
					AST newAttributeAST = dupAttachTree(attributeAST);
					if(newAttributeAST.getType() == ATTRIBUTE_INVERSE_ONLY) {
						newAttributeAST.setType(ATTRIBUTE);
					} else if(newAttributeAST.getType() == AGGREGATE_MEMBER_INVERSE_ONLY) {
						newAttributeAST.setType(AGGREGATE_MEMBER_CONSTRAINT);
					}
					iac = #(iac, newAttributeAST);
					if(typeConstraint != null) {
						iac =#(auxiliaryAST(id, ENTITY_CONSTRAINT), typeConstraint, iac);
					}
					setRemainingOverPathConstraint(remainingState, iac);
				}
			} else {
				throw new MappingSemanticException(id, "Illegal type: " + id.getText() + " for " +
												   remainingFirst);
			}
			remainingState.setFirst(id);
		}
		if(innerElementOpt.reachedAndEnd != null) {
			LinkedList postAndStateList = remainingStateList;
			remainingStateList = new LinkedList();
			remainingIter = postAndStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				WalkerState newRemainingState = remainingState.dup();
				// INVERSE_ATTRIBUTE_CONSTRAINT goes into intersection and then remaining part 
				if(newRemainingState.remaining.getType() == PATH_CONSTRAINT) {
					newRemainingState.remaining = remainingState.remaining.getFirstChild();
					remainingState.remaining = newRemainingState.remaining.getNextSibling();
					newRemainingState.remaining.setNextSibling(null);
				} else {
					remainingState.remaining = null;
				}
				remainingStateList.add(newRemainingState);
			}
			innerElementOpt.reachedAndEnd.andConstruct.remainingStateList = postAndStateList;
		}

		return remainingStateList;
	}

	private boolean oneStateAtSuper(EntityAST id, WalkerState remainingState, boolean throwException)
	throws MappingSemanticException, SdaiException {
		AST typeConstraintAST = null;
		if(remainingState.getFirstList() == null) {
			EntityAST remainingFirst = remainingState.getFirst(parser);
			if(!((remainingFirst.exactType
			&& id.declaration.definition == remainingFirst.declaration.definition)
			|| id.isSupertypeOf(remainingFirst))) {
				if(!throwException) {
					return false;
				} else {
					throw new MappingSemanticException(id, id.getText() + " is not a supertype of " +
													   remainingFirst.getText());
				}
			}
			typeConstraintAST = remainingState.getFirst(parser).dup();
		} else {
			Iterator firstIter = remainingState.getFirstList().iterator();
			while(firstIter.hasNext()) {
				EntityAST first = (EntityAST)firstIter.next();
				if(!((first.exactType
				&& id.declaration.definition == first.declaration.definition)
				|| id.isSupertypeOf(first))) {
					if(!throwException) {
						return false;
					} else {
						throw new MappingSemanticException(id, id.getText() + 
														   " is not a supertype of " + first.getText());
					}
				}
				AST typeConstraintElement = first.dup();
				typeConstraintElement.setNextSibling(typeConstraintAST);
				typeConstraintAST = typeConstraintElement;
			}
		}
		addTypeConstraint(id, remainingState, typeConstraintAST);
		remainingState.setFirst(id);
		return true;
	}

	public LinkedList stateAtSuper(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " =>");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			oneStateAtSuper(id, remainingState, true);
		}
		return remainingStateList;
	}

	public LinkedList stateAtSub(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " <=");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			if(remainingState.getFirstList() == null) {
				if(!remainingState.getFirst(parser).isSupertypeOf(id)) {
					throw new MappingSemanticException(id, id.getText() + " is not a subtype of " +
													   remainingState.getFirst(parser).getText());
				}
			} else {
				Iterator firstIter = remainingState.getFirstList().iterator();
				while(firstIter.hasNext()) {
					EntityAST first = (EntityAST)firstIter.next();
					if(!first.isSupertypeOf(id)) {
						throw new MappingSemanticException(id, id.getText() + " is not a subtype of " +
														   first.getText());
					}
				}
			}
			remainingState.setFirst(id);
			if(remainingState.remaining != null
			&& remainingState.remaining.getType() == TYPE_CONSTRAINT) {

				// #(T.C #(T.C. dataType etc.) remaining)
				EntityAST otherEntity = (
					(EntityAST)remainingState.remaining.getFirstChild().getFirstChild());
				if(!id.isSupertypeOf(otherEntity)) {
					// Complex entity
					remainingState.remaining.getFirstChild().addChild(id.dup());
				}
			}
		}
		return remainingStateList;
	}

	public boolean oneStateAtSelect(EntityAST id, WalkerState remainingState, boolean throwException)
	throws MappingSemanticException, SdaiException {
		EntityAST selectAST;
		EntityAST selectionAST;
		if(id.isSelectOf(remainingState.getFirst(parser))) {
			selectAST = id;
			selectionAST = remainingState.getFirst(parser);
		} else if(remainingState.getFirst(parser).isSelectOf(id)) {
			selectAST = remainingState.getFirst(parser);
			selectionAST = id;
		} else {
			if(!throwException) {
				return false;
			} else {
				throw new MappingSemanticException(id, "Wrong select: " + id.getText() + " = " +
												   remainingState.getFirst(parser).getText());
			}
		}
		remainingState.setFirst(selectAST);
		if(remainingState.remaining != null) {
			AST topRemaining = getRemainingOverPathConstraint(remainingState);
			AST remainingInverse = topRemaining; // Possible inverse
			boolean doDefault = false;
			switch(topRemaining.getType()) {
			case SELECT_CONSTRAINT: {
                    if(selectionAST.isSimpleType()) {
                        AST selectDataType = topRemaining.getFirstChild();
                        selectDataType.addChild(selectionAST.dup());
                        break;
                    } else {
                        doDefault = true;
                        break;
                    }
                }

			case ENTITY_CONSTRAINT:
				AST entityConstraintChild = topRemaining.getFirstChild().getNextSibling();
				if(entityConstraintChild.getType() == INVERSE_ATTRIBUTE_CONSTRAINT_SELECT) {
					remainingInverse = entityConstraintChild;
					// Continues into INVERSE_ATTRIBUTE_CONSTRAINT_SELECT
				} else {
					doDefault = true;
					break;
				}
			case INVERSE_ATTRIBUTE_CONSTRAINT_SELECT: {
					AST inverseChild = remainingInverse.getFirstChild();
					if(inverseChild.getType() != SELECT_CONSTRAINT) {
						if(!selectionAST.isEntityDefinition()) {
							AST newInverse = (
								#(auxiliaryAST(id, INVERSE_ATTRIBUTE_CONSTRAINT_SELECT),
									#(auxiliaryAST(id, SELECT_CONSTRAINT), 
										#(auxiliaryAST(id, SELECT_DATA_TYPE), selectionAST.dup()),
										inverseChild))
							);
							if(topRemaining.getType() == INVERSE_ATTRIBUTE_CONSTRAINT_SELECT) {
								setRemainingOverPathConstraint(remainingState, newInverse);
							} else {
								topRemaining.getFirstChild().setNextSibling(newInverse);
							}
						} else {
							AST iacs = (topRemaining.getType() == INVERSE_ATTRIBUTE_CONSTRAINT_SELECT ?
								topRemaining :
								topRemaining.getFirstChild().getNextSibling());
							iacs.setType(INVERSE_ATTRIBUTE_CONSTRAINT);
						}
					} else {
						AST selectDataType = inverseChild.getFirstChild();
						if(!selectionAST.isEntityDefinition()) {
							AST selectElement = selectionAST.dup();
							selectElement.setNextSibling(selectDataType.getFirstChild());
							selectDataType.setFirstChild(selectElement);
						} else {
							AST oldInverseChild = inverseChild.getFirstChild().getNextSibling();
							AST newInverse = (
								#(auxiliaryAST(id, INVERSE_ATTRIBUTE_CONSTRAINT), oldInverseChild)
							);
							if(topRemaining.getType() == INVERSE_ATTRIBUTE_CONSTRAINT_SELECT) {
								setRemainingOverPathConstraint(remainingState, newInverse);
							} else {
								topRemaining.getFirstChild().setNextSibling(newInverse);
							}
						}
					}
					remainingState.setFirst(selectionAST);
					break;
				}

			case TYPE_CONSTRAINT: {
					// #(T.C #(T.C. dataType etc.) remaining)
					AST lastAST = topRemaining.getFirstChild().getFirstChild();
					AST next;
					while((next = lastAST.getNextSibling()) != null) {
						lastAST = next;
					}
					int lastASTType = lastAST.getType();
					if(lastASTType == AGGREGATE_MEMBER_CONSTRAINT && selectionAST.isSimpleType()) {
						lastAST.setNextSibling(
							#(auxiliaryAST(id, SELECT_CONSTRAINT), 
								#(auxiliaryAST(id, SELECT_DATA_TYPE), selectionAST.dup())
							)
						);
					} else if(lastASTType == SELECT_CONSTRAINT && selectionAST.isSimpleType()) {
						lastAST.getFirstChild().addChild(selectionAST.dup());
					} else {
						doDefault = true;
					}
					break;
				}

			default: {
					doDefault = true;
					break;
				}
			}
			if(doDefault) {
				if(selectionAST.isSimpleType()) { //!selectionAST.isEntityDefinition()) {
					AST selectConstraint = (
						#(auxiliaryAST(id, SELECT_CONSTRAINT), 
							#(auxiliaryAST(id, SELECT_DATA_TYPE), selectionAST.dup()))
					);
					remainingState.remaining = (
						#(auxiliaryAST(id, PATH_CONSTRAINT), selectConstraint, remainingState.remaining)
					);
				} else if(selectionAST.isEntityDefinition() 
				&& remainingState.remaining.getType() != TYPE_CONSTRAINT) { //???
					remainingState.remaining = (
						#(auxiliaryAST(id, TYPE_CONSTRAINT), 
							#(auxiliaryAST(id, TYPE_CONSTRAINT), selectionAST.dup()),
							remainingState.remaining
						)
					);
				}
			}
		} else {
			if(selectionAST.isSimpleType()) { //!selectionAST.isEntityDefinition()) {
				remainingState.remaining = (
					#(auxiliaryAST(id, SELECT_CONSTRAINT), 
						#(auxiliaryAST(id, SELECT_DATA_TYPE), selectionAST.dup()))
				);
			} else if(selectionAST.isEntityDefinition()) { //???
				remainingState.remaining = (
					#(auxiliaryAST(id, TYPE_CONSTRAINT),
						#(auxiliaryAST(id, TYPE_CONSTRAINT), selectionAST.dup()),
						remainingState.remaining
					)
				);
			}
		}
		return true;
	}

	public LinkedList stateAtSelect(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " =");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			oneStateAtSelect(id, remainingState, true);
		}
		return remainingStateList;
	}

	public LinkedList stateAtExtendedInto(EntityAST id, 
										  LinkedList remainingStateList,
										  WalkerOptions innerElementOpt
	) throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " *>");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
            EntityAST extendedSel = remainingState.getFirst(parser);
            if(!extendedSel.isExtensionOf(id)) {
                throw new MappingSemanticException(id, "Identifier: " + id.getText() +
                                                   " does not extend into " + extendedSel.getText());
            }
            remainingState.setFirst(id);
		}
		return remainingStateList;
    }

	public LinkedList stateAtExtensionOf(EntityAST id, 
										 LinkedList remainingStateList,
										 WalkerOptions innerElementOpt
	) throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " <*");
		}
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
            EntityAST extensibleSel = remainingState.getFirst(parser);
            if(!id.isExtensionOf(extensibleSel)) {
                throw new MappingSemanticException(id, "Identifier: " + id.getText() +
                                                   " is not an extension of " +
												   extensibleSel.getText());
            }
            remainingState.setFirst(id);
		}
		return remainingStateList;
    }

	public LinkedList stateAtAttribute(AttributeAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(remainingStateList == null) {
			AttributeAST remainingAtt = (AttributeAST)id.dup();
			remainingAtt.constraintLevelInRemaining = constraintLevel;
			WalkerState remainingState = new WalkerState(remainingAtt);
			remainingState.remaining = id.dup();
			remainingState.remaining.setType(ATTRIBUTE);
			return WalkerState.createList(remainingState);
		} else {
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				EntityAST remainingFirst = remainingState.getFirst(parser);
				if(!id.isSame(remainingFirst)) {
					throw new MappingSemanticException(id, "Illegal sequence: " + id.getText() + 
						" " + remainingState.getFirst(parser).getText());
				}
				AttributeAST remainingAtt =
					remainingFirst instanceof AttributeAST ? (AttributeAST)remainingFirst : null;
				if(!(remainingAtt != null && id.isSameAttribute(remainingAtt))
				|| (constraintLevel == 0 && remainingAtt.constraintLevelInRemaining <= constraintLevel)) {
					remainingState.setFirst(id);
					AST attributeAST = id.dup();
//DEBUG 					attributeAST.setType(ATTRIBUTE);
					attributeAST.setType(ATTRIBUTE_INVERSE_ONLY);
//DEBUG 					reportWarning(attributeAST, "Attribute was set as inverse only: " + attributeAST);
					AST typeConstraint = null;
					if(remainingState.remaining != null && 
						remainingState.remaining.getType() == TYPE_CONSTRAINT) {

						typeConstraint = dupAttachTree(remainingState.remaining.getFirstChild());
						remainingState.remaining = (remainingState.remaining
							.getFirstChild().getNextSibling());
					}
					remainingState.remaining = (remainingState.remaining != null ?
						#(auxiliaryAST(id, PATH_CONSTRAINT), attributeAST, remainingState.remaining) : 
						attributeAST);
					if(typeConstraint != null) {
						remainingState.remaining = #(auxiliaryAST(id, TYPE_CONSTRAINT), 
							typeConstraint, remainingState.remaining);
					}
				} else if(constraintLevel == 0) {
					WalkerState attRemainingState = new WalkerState(id);
					attRemainingState.remaining = id.dup();
					attRemainingState.remaining.setType(ATTRIBUTE);
					((ParserAST)attRemainingState.remaining).setInPath(ParserAST.IN_PATH);
					pathDetected = true;
					remainingState.remaining = combineToAnd(attRemainingState, remainingState, false);
					remainingState.setFirst(id);
// 					reportWarning(id, "Attribute else case: " + id + " " + remainingState.remaining.toStringTree());//DEBUG
				}
			}
			return remainingStateList;
		}
	}

	public LinkedList stateAtAttributeAggreg(AttributeAST id, AST member, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		AST attributeAST = id.dup();
		attributeAST.setType(ATTRIBUTE);
		AST aggregateMemberConstraint = (
			#(auxiliaryAST(id, AGGREGATE_MEMBER_CONSTRAINT), attributeAST, astFactory.dup(member))
		);
		if(remainingStateList == null) {
			WalkerState remainingState = new WalkerState(id);
			remainingState.remaining = aggregateMemberConstraint;
			return WalkerState.createList(remainingState);
		} else {
			aggregateMemberConstraint.setType(AGGREGATE_MEMBER_INVERSE_ONLY);
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				if(!id.isSame(remainingState.getFirst(parser))) {
					throw new MappingSemanticException(id, "Illegal sequence: " + id.getText() + 
													   " " + remainingState.getFirst(parser).getText());
				}
				remainingState.setFirst(id);
				AST typeConstraint = null;
				if(remainingState.remaining != null && 
					remainingState.remaining.getType() == TYPE_CONSTRAINT) {

					typeConstraint = dupAttachTree(remainingState.remaining.getFirstChild());
					remainingState.remaining = remainingState.remaining.getFirstChild().getNextSibling();
				}
				remainingState.remaining = (remainingState.remaining != null ? 
					#(auxiliaryAST(id, PATH_CONSTRAINT), dupAttachTree(aggregateMemberConstraint), 
						remainingState.remaining) :
					aggregateMemberConstraint);
				if(typeConstraint != null) {
					remainingState.remaining = #(auxiliaryAST(id, TYPE_CONSTRAINT), 
						typeConstraint, remainingState.remaining);
				}
			}
			return remainingStateList;
		}
	}

	public LinkedList stateAtEqAttributeValue(
		LinkedList attributeStateList, LinkedList remainingStateList, WalkerOptions options)
	throws MappingSemanticException, SdaiException {
		if(attributeStateList.size() != 1) {
			throw new MappingSemanticException(null, "FATAL ERROR! Wrong attribute list at value");
		}
		WalkerState attributeState = (WalkerState)attributeStateList.getFirst();
		AttributeAST id = (AttributeAST)attributeState.getFirst(parser);
		EEntity realDomain = id.getRealDomain();
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);

			if(realDomain instanceof EBinary_type) {
				throw new MappingSemanticException(id, "Binary type is unsupported: " + id.getText());
			} else if(realDomain instanceof EBoolean_type) {
				if(remainingState.remaining.getType() == ENUMERATION_CONSTRAINT) {
					remainingState.remaining.setType(BOOLEAN_CONSTRAINT);
				} else {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": boolean expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else if(realDomain instanceof EInteger_type) {
				if(remainingState.remaining.getType() != INTEGER_CONSTRAINT) {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": integer expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else if(realDomain instanceof ELogical_type) {
				if(remainingState.remaining.getType() == ENUMERATION_CONSTRAINT) {
					remainingState.remaining.setType(LOGICAL_CONSTRAINT);
				} else {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": logical expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else if(realDomain instanceof ENumber_type
			 		|| realDomain instanceof EReal_type) {
				if(remainingState.remaining.getType() == INTEGER_CONSTRAINT) {
					remainingState.remaining.setType(REAL_CONSTRAINT);
				} else if(remainingState.remaining.getType() != REAL_CONSTRAINT) {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": real expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else if(realDomain instanceof EString_type) {
				if(remainingState.remaining.getType() != STRING_CONSTRAINT) {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": string expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else if(realDomain instanceof EEnumeration_type) {
				if(remainingState.remaining.getType() != ENUMERATION_CONSTRAINT) {
					throw new MappingSemanticException(id, "Wrong type for " + id.getText() +
													   ": enumeration expected but " + 
													   remainingState.remaining.getType() + " found");
				}
			} else {
				throw new MappingSemanticException(id, "Illegal attribute value: " + id.getText());
			}
			remainingState.remaining.addChild(dupAttachTree(attributeState.remaining));
			remainingState.setFirst(id);
			if(options.negation) {
				remainingState.remaining = #(auxiliaryAST(id, NEGATION), remainingState.remaining);
			}
		}
		return remainingStateList;
	}

	public LinkedList stateAtEqAttributeAttribute(
		LinkedList attributeOneList, LinkedList attributeTwoList, WalkerOptions options)
	throws MappingSemanticException, SdaiException {
		if(attributeOneList.size() != 1 || attributeTwoList.size() != 1) {
			throw new MappingSemanticException(null, "FATAL ERROR! Wrong attribute list at attribute");
		}
		WalkerState attributeOne = (WalkerState)attributeOneList.getFirst();
		WalkerState attributeTwo = (WalkerState)attributeTwoList.getFirst();
		if(!attributeOne.getFirst(parser).isSame(attributeTwo.getFirst(parser))
		&& !attributeOne.getFirst(parser).isSupertypeOf(attributeTwo.getFirst(parser))
		&& !attributeTwo.getFirst(parser).isSupertypeOf(attributeOne.getFirst(parser))) {
			throw new MappingSemanticException(attributeOne.getFirst(parser),
											   "Attributes can not be of different entities: " + 
											   attributeOne.getFirst(parser).getText() + " and " + 
											   attributeTwo.getFirst(parser).getText());
		}
		AST element1 = attributeOne.remaining;
		AST element2 = attributeTwo.remaining;
		attributeOne.remaining = #(auxiliaryAST(element1, INSTANCE_EQUAL), element1, element2);
		if(options.negation) {
			attributeOne.remaining = #(auxiliaryAST(element1, NEGATION), attributeOne.remaining);
		}
		return attributeOneList;
	}

	private void makePathTo(EntityAST attribute, WalkerState remainingState)
	throws MappingSemanticException, SdaiException {

		boolean illegalDomain = true;
		List pathList = attribute.getPathTo(remainingState.getFirst(parser));
		if(pathList != null) {
			Iterator pathListIter = pathList.iterator();
			illegalDomain = false;
			while(pathListIter.hasNext()) {
				EEntity domainElement = (EEntity)pathListIter.next();
				EntityAST domainAST = attribute.dup();
				if(domainElement instanceof EEntity_definition) {
					domainAST.setIdentifier(parser, 
						((EEntity_definition)domainElement).getName(null), true);
					if(!oneStateAtSuper(domainAST, remainingState, false)) {
						illegalDomain = true;
						break;
					}
				} else if(domainElement instanceof EDefined_type) {
					domainAST.setIdentifier(parser, 
						((EDefined_type)domainElement).getName(null), true);
					if(!oneStateAtSelect(domainAST, remainingState, false)) {
						illegalDomain = true;
						break;
					}
				} else {
					illegalDomain = true;
					break;
				}
			}
		}
		if(illegalDomain) {
			throw new MappingSemanticException(attribute, "Illegal type: " + 
											   remainingState.getFirst(parser).getText() +
											   " for " + attribute.getText());
		}
	}

	public LinkedList stateAtToPoint(LinkedList attributeStateList, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(attributeStateList.size() != 1) {
			throw new MappingSemanticException(null, "FATAL ERROR! Wrong attribute list at ->");
		}
		WalkerState attributeState = (WalkerState)attributeStateList.getFirst();

		if(remainingStateList == null) {
			throw new MappingSemanticException(attributeState.getFirst(parser), "Incomplete element: " +
											   attributeState.getFirst(parser).getText() + " ->");
		}
		remainingStateList = postProcessState(remainingStateList, true);
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);

			EntityAST attribute = attributeState.getFirst(parser);
			makePathTo(attribute, remainingState);
			AST attributeRemaining = dupAttachTree(attributeState.remaining);
			if(remainingState.remaining != null
			&& remainingState.remaining.getType() == TYPE_CONSTRAINT) {
				// #(T.C #(T.C. dataType etc.) remaining)
				AST lastAST = remainingState.remaining.getFirstChild().getFirstChild();
				LinkedList backwardList = new LinkedList();
				while(lastAST != null) {
					backwardList.addFirst(lastAST);
					lastAST = lastAST.getNextSibling();
				}
				Iterator backwardIter = backwardList.iterator();
				boolean clearSiblings = false;
				AST previousAST = null;
				while(backwardIter.hasNext()) {
					AST ast = (AST)backwardIter.next();
					if(clearSiblings) {
						ast.setNextSibling(null);
						clearSiblings = false;
					}
					int astType = ast.getType();
					if(astType == SELECT_CONSTRAINT
					|| astType == AGGREGATE_MEMBER_CONSTRAINT) {
						AST entityConstraints = ast.getNextSibling();
						if(entityConstraints != null) {
							AST typeConstraintBody = auxiliaryAST(attribute, TYPE_CONSTRAINT);
							typeConstraintBody.setFirstChild(dupAttachTree(entityConstraints));
							attributeRemaining = (
								#(auxiliaryAST(attribute, ENTITY_CONSTRAINT), 
									typeConstraintBody, 
									attributeRemaining)
							);
						}
						clearSiblings = true;
						if(astType == SELECT_CONSTRAINT) {
							AST selectConstraint = dupAttachTree(ast);
							selectConstraint.getFirstChild().setNextSibling(attributeRemaining);
							attributeRemaining = selectConstraint;
						} else {
							AST index = ast.getFirstChild();
							attributeRemaining = (
								#(auxiliaryAST(attribute, AGGREGATE_MEMBER_CONSTRAINT), 
									attributeRemaining,
									index
								)
							);
						}
					} else if(!backwardIter.hasNext()) {
						attributeRemaining = (
							#(auxiliaryAST(attribute, ENTITY_CONSTRAINT), 
								#(auxiliaryAST(attribute, TYPE_CONSTRAINT), dupWholeTree(ast)),
								attributeRemaining)
						);
					}
				}
				remainingState.remaining = remainingState.remaining.getFirstChild().getNextSibling();
			}
			if(remainingState.remaining != null) {
				AST topRemaining = getRemainingOverPathConstraint(remainingState);
				if(topRemaining.getType() == SELECT_CONSTRAINT && 
					topRemaining.getFirstChild().getNextSibling() == null) {

// 					reportWarning(attributeState.getFirst(parser), 
// 						"!!!!!! SELECT_CONSTRAINT was used!!!!!!");
					topRemaining.getFirstChild().setNextSibling(attributeRemaining);
					if(remainingState.remaining.getType() == PATH_CONSTRAINT) {
						AST pathElement2 = remainingState.remaining.getFirstChild().getNextSibling();
						if(pathElement2.getType() == EQ_ENTITY) {
							AST attributeValueConstraint = getRemainingOverPathConstraint(
								pathElement2.getFirstChild());
							attributeValueConstraint.getFirstChild().setNextSibling(
								dupAttachTree(topRemaining));
							remainingState.remaining = pathElement2.getFirstChild();
						}
					}
				} else if(remainingState.remaining.getType() == EQ_ENTITY) {
					AST attributeValueConstraintPath = remainingState.remaining.getFirstChild();
					AST attributeValueConstraint = getRemainingOverPathConstraint(
						attributeValueConstraintPath);
					attributeValueConstraint.getFirstChild().setNextSibling(attributeRemaining);
					remainingState.remaining = attributeValueConstraintPath;
				} else {
					remainingState.remaining = #(auxiliaryAST(attribute, PATH_CONSTRAINT), 
						attributeRemaining, remainingState.remaining);
				}
			} else {
				remainingState.remaining = attributeRemaining;
			}
			remainingState.setFirst(attributeState.getFirst(parser));
		}
		return remainingStateList;
	}

	public LinkedList stateAtEntityToPoint(EntityAST id, 
		LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " ->");
		}
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(id.isEntityDefinition()) {
			throw new MappingSemanticException(id, "Defined type expected here");
		}
		remainingStateList = postProcessState(remainingStateList, true);
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			makePathTo(id, remainingState);
			remainingState.setFirst(id);
		}
		return remainingStateList;
	}

	public LinkedList stateAtEntityAggregToPoint(EntityAST id, 
		AST index, LinkedList remainingStateList, WalkerOptions innerElementOpt)
	throws MappingSemanticException, SdaiException {
		if(remainingStateList == null) {
			throw new MappingSemanticException(id, "Incomplete element: " + id.getText() + " ->");
		}
		cleanInnerElement(remainingStateList, innerElementOpt);
		if(id.isEntityDefinition()) {
			throw new MappingSemanticException(id, "Defined type expected here");
		}
		remainingStateList = postProcessState(remainingStateList, true);
		Iterator remainingIter = remainingStateList.iterator();
		while(remainingIter.hasNext()) {
			WalkerState remainingState = WalkerState.next(remainingIter);
			makePathTo(id, remainingState);
			AST aggregateMemberConstraint = #(auxiliaryAST(id, AGGREGATE_MEMBER_CONSTRAINT), 
				astFactory.dup(index));
			if(remainingState.remaining != null
			&& remainingState.remaining.getType() == TYPE_CONSTRAINT) {
				// #(T.C #(T.C. dataType etc.) remaining)
				remainingState.remaining.getFirstChild().addChild(aggregateMemberConstraint);
			} else {
				remainingState.remaining = #(auxiliaryAST(id, TYPE_CONSTRAINT),
					#(auxiliaryAST(id, TYPE_CONSTRAINT), aggregateMemberConstraint),
					remainingState.remaining
				);
			}
			remainingState.setFirst(id);
		}
		return remainingStateList;
	}

	private AST combineToAnd(final WalkerState remainingState1, 
		final WalkerState remainingState2, boolean checkConstraintLevel)
	throws MappingSemanticException, SdaiException {
		if(remainingState1.remaining == null) {
			return dupWholeTree(remainingState2.remaining);
		}
		if(remainingState2.remaining == null) {
			return dupWholeTree(remainingState1.remaining);
		}
		AST remaining1 = remainingState1.remaining;
		AST remaining2 = remainingState2.remaining;
		if(remaining1.getType() == TYPE_CONSTRAINT && remaining2.getType() == TYPE_CONSTRAINT) {
			AST anotherType = remaining2.getFirstChild();
			remaining2 = anotherType.getNextSibling();
			// Children are made as siblings
			anotherType = dupWholeTree(anotherType.getFirstChild());
			remaining1 = dupWholeTree(remaining1);
			remaining1.getFirstChild().addChild(anotherType);
			if(remaining2 == null) return remaining1;
		}
		AST typeConstraintAST = null;
		AST otherAST = null;
		boolean switchedAnds = false;
		if(remaining1.getType() == TYPE_CONSTRAINT) {
			typeConstraintAST = dupWholeTree(remaining1);
			otherAST = dupWholeTree(remaining2);
			switchedAnds = true;
		} else if(remaining2.getType() == TYPE_CONSTRAINT) {
			typeConstraintAST = dupWholeTree(remaining2);
			otherAST = dupWholeTree(remaining1);
		}
		if(typeConstraintAST != null) {
			AST typeConstraintChild = typeConstraintAST.getFirstChild();
			AST typeConstraintRemaining = typeConstraintChild.getNextSibling();
			if(checkConstraintLevel && constraintLevel == 0 && typeConstraintRemaining != null) {
				throw new MappingSemanticException(remainingState1.getFirst(parser), 
												   "AND is not supported here (note 1)");
			}
			if(typeConstraintRemaining == null) {
				typeConstraintChild.setNextSibling(otherAST);
			} else {
				ParserAST childAndConstraint = (ParserAST)(
					(switchedAnds ? 
						#(auxiliaryAST(otherAST, AND_CONSTRAINT), typeConstraintRemaining, otherAST) :
						#(auxiliaryAST(otherAST, AND_CONSTRAINT), otherAST, typeConstraintRemaining))
				);
				childAndConstraint.setInPath(((ParserAST)childAndConstraint.getFirstChild()).getInPath());
				typeConstraintChild.setNextSibling(childAndConstraint);
			}
			return typeConstraintAST;
		} else {
			if(checkConstraintLevel && constraintLevel == 0) {
				throw new MappingSemanticException(remainingState1.getFirst(parser), 
												   "AND is not supported here (note 2)");
			}
			ParserAST andConstraint = (ParserAST)(
				#(auxiliaryAST(remaining1, AND_CONSTRAINT), 
					dupWholeTree(remaining1),
					dupWholeTree(remaining2))
			);
			andConstraint.setInPath(((ParserAST)andConstraint.getFirstChild()).getInPath());
			return andConstraint;
		}
	}

	public LinkedList stateAtConstraint(LinkedList constraintStateList, 
		LinkedList remainingStateList, WalkerOptions options)
	throws MappingSemanticException, SdaiException {
		if(remainingStateList == null) {
			if(options.negation) {
				Iterator constraintIter = constraintStateList.iterator();
				while(constraintIter.hasNext()) {
					WalkerState constraintState = WalkerState.next(constraintIter);
					EntityAST constraintStateFirst = constraintState.getFirst(parser);
					if(constraintState.remaining == null) {
						throw new MappingSemanticException(constraintStateFirst, "Negation is " +
														   "illegal here, there is nothing to negate");
					} else {
						constraintState.remaining = #(auxiliaryAST(constraintStateFirst, NEGATION),
							constraintState.remaining);
						((ParserAST)constraintState.remaining).setInPath(ParserAST.NOT_IN_PATH);
					}
				}
			}
			return constraintStateList;
		}
		if(options.partialConstraints
		&& !((WalkerState)constraintStateList.getFirst()).getFirst(parser)
		.isSame(((WalkerState)remainingStateList.getFirst()).getFirst(parser))) {
			options.constraintStateList = constraintStateList;
			return remainingStateList;
		}
		LinkedList newRemainingList = new LinkedList();
		Iterator constraintIter = constraintStateList.iterator();
		while(constraintIter.hasNext()) {
			WalkerState constraintState = WalkerState.next(constraintIter);
			EntityAST constraintStateFirst = constraintState.getFirst(parser);
			if(options.negation) {
				if(constraintState.remaining == null) {
					throw new MappingSemanticException(constraintStateFirst, "Negation is " +
													   "illegal here, there is nothing to negate");
				} else {
					constraintState.remaining = #(auxiliaryAST(constraintStateFirst, NEGATION),
						constraintState.remaining);
					((ParserAST)constraintState.remaining).setInPath(ParserAST.NOT_IN_PATH);
				}
			}
			Iterator remainingIter = remainingStateList.iterator();
			while(remainingIter.hasNext()) {
				WalkerState remainingState = WalkerState.next(remainingIter);
				EntityAST remainingStateFirst = remainingState.getFirst(parser);
				boolean constraintType = false;
				if(!constraintStateFirst.isSame(remainingStateFirst)
				&& !constraintStateFirst.isSupertypeOf(remainingStateFirst)) {
					if(remainingStateFirst.isSupertypeOf(constraintStateFirst)) {
						constraintType = true;
					} else {
						throw new MappingSemanticException(constraintStateFirst, 
														   "Constraint starts with " +
														   constraintStateFirst.getText() + 
														   " but type following constraint is " + 
														   remainingState.getFirst(parser).getText());
					}
				}
				WalkerState newRemainingState = remainingState.dup();
				newRemainingState.synchronizeWith(constraintState);
				newRemainingState.remaining = combineToAnd(remainingState, constraintState, false);
				if(constraintType) {
					newRemainingState.remaining = (
						#(auxiliaryAST(constraintStateFirst, TYPE_CONSTRAINT), 
							#(auxiliaryAST(constraintStateFirst, TYPE_CONSTRAINT), 
								constraintStateFirst.dup()),
							newRemainingState.remaining
						)
					);
				}
				newRemainingList.add(newRemainingState);
			}
		}
		return newRemainingList;
	}

	public LinkedList stateAtAnd(LinkedList remainingStateList1, LinkedList remainingStateList2)
	throws MappingSemanticException, SdaiException {
		LinkedList newRemainingList = new LinkedList();
		Iterator remainingIter1 = remainingStateList1.iterator();
		while(remainingIter1.hasNext()) {
			WalkerState remainingState1 = WalkerState.next(remainingIter1);
			Iterator remainingIter2 = remainingStateList2.iterator();
			while(remainingIter2.hasNext()) {
				WalkerState remainingState2 = WalkerState.next(remainingIter2);
				WalkerState newRemainingState = remainingState2.dup();
				newRemainingState.synchronizeWith(remainingState1);
				LinkedList complexIds;
				LinkedList firstList;
				if(remainingState2.getFirstList() == null) {
					newRemainingState.addFirstToList(remainingState2.getFirst(parser));
				}
				if(remainingState1.getFirstList() == null) {
					newRemainingState.addFirstToList(remainingState1.getFirst(parser));
				} else {
					newRemainingState.addFirstToList(remainingState1.getFirstList());
				}
				newRemainingState.remaining = combineToAnd(remainingState1, remainingState2, true);
				newRemainingList.add(newRemainingState);
			}
		}
		return newRemainingList;
	}

	public void scanAndEnds(AndConstructAST andConstructAst)
	throws RecognitionException, MappingSemanticException, SdaiException {
		andConstructAst.subpaths = null;
		AST remainingAst;
		if(andConstructAst.andLastList != null 
		&& (remainingAst = andConstructAst.andEnd.getFirstChild()) != null) {
			LinkedList remainingStateList = pathInnerElement(remainingAst, new WalkerOptions());
			if(remainingStateList != null && remainingStateList.size() >= 1) {
				EntityAST remainingFirst = null;
				Iterator remainingStateIter = remainingStateList.iterator();
				while(remainingStateIter.hasNext()) {
					WalkerState state = WalkerState.next(remainingStateIter);
					EntityAST stateFirst = state.getFirst(parser);
					if(remainingFirst == null) {
						remainingFirst = stateFirst;
					} else if(remainingFirst != stateFirst) {
						throw new MappingSemanticException(remainingFirst,
														   "Alternatives with different " +
														   "start types can not be used in " + 
														   "this way after AND constraint");
					}
				}
				Iterator andLastIter = andConstructAst.andLastList.iterator();
				boolean intersection = true;
				boolean valid = false;
				int andLastCount = 0;
				while(andLastIter.hasNext()) {
					AST andLast = (AST)andLastIter.next();
//DEBUG 					reportWarning(andConstructAst, "andLast is " + andLast.getText());
					AST andLastChild = andLast.getFirstChild();
					AST nextSibling;
					while((nextSibling = andLastChild.getNextSibling()) != null) {
						andLastChild = nextSibling;
					}
					((AndEndAST)andLastChild).unused = false;
					if(andLast.getType() == ENTITY) {
						if(!((EntityAST)andLast.getFirstChild()).isSame(remainingFirst)) {
							intersection = false;
							((AndEndAST)andLastChild).unused = true;
//DEBUG 							reportWarning(andConstructAst, andLastChild.getText() + " was unused") ;
							continue;
						}
					}
					valid = true;
					andLastCount++;
				}
				if(!valid) {
					throw new MappingSemanticException(remainingFirst, 
						"Path following AND can not be attached");
				}
				if(andLastCount <= 1) intersection = false;
				if(intersection) andConstructAst.subpaths = new LinkedList();
				andConstructAst.remainingStateList = remainingStateList;
			}
		}
	}

	private boolean collectSubpaths(AST andConstructAst, LinkedList andStateList, 
		WalkerState headState, LinkedList subpaths[], int level)
	throws MappingSemanticException, SdaiException {
		if(level == subpaths.length) return false;
//DEBUG 		reportWarning(andConstructAst, "subpaths[level].size() " + subpaths[level].size());
		boolean added = false;
		Iterator oneStateIter = subpaths[level].iterator();
		while(oneStateIter.hasNext()) {
			WalkerState state = WalkerState.next(oneStateIter);
			WalkerState currentState = headState.dup();
// 			reportWarning(andConstructAst, "New current state");
			if(currentState.remaining != null) {
				currentState.remaining = dupWholeTree(currentState.remaining);
				if(state.remaining != null) {
					currentState.remaining.addChild(dupAttachTree(state.remaining));
				}
			} else if(state.remaining != null) {
				currentState.remaining = #(auxiliaryAST(andConstructAst, INTERSECTION_CONSTRAINT), 
					state.remaining);
			}
			LinkedList stateFirstList = state.getFirstList();
			if(stateFirstList != null) {
				currentState.addFirstToList(stateFirstList);
			} else {
				currentState.addFirstToList(state.getFirst(parser));
			}
			if(!collectSubpaths(andConstructAst, andStateList, currentState, subpaths, level + 1)) {
				if(currentState.remaining != null) {
					AST remainingStart = null;
					boolean commonStart = true;
					for(AST remaining = currentState.remaining.getFirstChild(); remaining != null; 
						remaining = remaining.getNextSibling()) {

						if(remaining.getType() != PATH_CONSTRAINT) {
							commonStart = false;
							break;
						}
						if(remainingStart == null) {
							remainingStart = remaining.getFirstChild();
						} else {
							if(!WalkerState.equalsAST(remainingStart,
									remaining.getFirstChild())) {
								commonStart = false;
								break;
							}
						}
					}
					if(commonStart) {
//DEBUG 						System.out.println("commonStart = true");
						AST oldRemaining = dupAttachTree(currentState.remaining);
						AST remainingPart = dupAttachTree(oldRemaining.getFirstChild().getFirstChild());
						remainingPart.setNextSibling(null);
						currentState.remaining.setFirstChild(null);
						for(AST remaining = oldRemaining.getFirstChild(); remaining != null; 
							remaining = remaining.getNextSibling()) {
							currentState.remaining.addChild(
								dupAttachTree(remaining.getFirstChild().getNextSibling()));
						}
						ensureIntersectPath(currentState.remaining);
						((ParserAST)currentState.remaining).setInPath(constraintLevel == 0 ? 
							ParserAST.IN_PATH : ParserAST.NOT_IN_PATH);
						currentState.remaining = #(auxiliaryAST(andConstructAst, PATH_CONSTRAINT), 
							remainingPart, currentState.remaining);
					} else {
						ensureIntersectPath(currentState.remaining);
					}
				}
				andStateList.add(currentState);
			}
			added = true;
		}
		return added ? true : collectSubpaths(andConstructAst, andStateList, 
			headState, subpaths, level + 1);
	}

	public LinkedList stateAtIntersect(AndConstructAST andConstructAst, LinkedList andStateList)
	throws MappingSemanticException, SdaiException {
		if(andConstructAst.subpaths != null) {
			LinkedList subpaths[] = (LinkedList[])andConstructAst.subpaths.toArray(
				new LinkedList[andConstructAst.subpaths.size()]);
			andStateList.clear();
			if(!collectSubpaths(andConstructAst, andStateList, new WalkerState(), subpaths, 0)) {
				return andConstructAst.remainingStateList;
			}
			andStateList = postProcessState(andStateList, true);
			andConstructAst.remainingStateList = (
				postProcessState(andConstructAst.remainingStateList, true));
			LinkedList andFinalStateList = new LinkedList();
			Iterator andStateIter = andStateList.iterator();
			while(andStateIter.hasNext()) {
				WalkerState andState = WalkerState.next(andStateIter);
				if(andState.remaining != null) {
					boolean straightforwardAdd = false;
					Iterator remainingStateIter = andConstructAst.remainingStateList.iterator();
					while(remainingStateIter.hasNext()) {
						WalkerState remainingState = WalkerState.next(remainingStateIter);
						WalkerState andFinalState = andState.dup();
						if(remainingState.remaining != null) {
							if(andState.remaining.getType() == PATH_CONSTRAINT) {
//DEBUG								System.out.println("andState.remaining.getType() == PATH_CONSTRAINT");
								AST commonStartAst = andState.remaining.getFirstChild();
								andFinalState.remaining = (
									#(auxiliaryAST(andConstructAst, PATH_CONSTRAINT),
										dupAttachTree(commonStartAst), 
										#(auxiliaryAST(andConstructAst, PATH_CONSTRAINT),
											dupAttachTree(commonStartAst.getNextSibling()),
											remainingState.remaining)
									)
								);
							} else {
								andFinalState.remaining = #(auxiliaryAST(andConstructAst, PATH_CONSTRAINT),
									dupAttachTree(andState.remaining), remainingState.remaining);
							}
						} else {
							if(!straightforwardAdd) {
								andFinalState.remaining = dupAttachTree(andState.remaining);
								straightforwardAdd = true;
							} else {
								continue;
							}
						}
//DEBUG 						reportWarning(andConstructAst, "INTERSECTION found (case 1)");
						andFinalStateList.add(andFinalState);
					}
				} else {
					Iterator remainingStateIter = andConstructAst.remainingStateList.iterator();
					while(remainingStateIter.hasNext()) {
						WalkerState remainingState = WalkerState.next(remainingStateIter);
						WalkerState andFinalState = andState.dup();
						andFinalState.remaining = remainingState.remaining;
						LinkedList stateFirstList = remainingState.getFirstList();
						if(stateFirstList != null) {
							andFinalState.addFirstToList(stateFirstList);
						} else {
							andFinalState.addFirstToList(remainingState.getFirst(parser));
						}
						andFinalStateList.add(andFinalState);
//DEBUG 						reportWarning(andConstructAst, "INTERSECTION found (case 2)");
					}
				}
			}
			if(andFinalStateList.size() == 0) {
				throw new MappingSemanticException(andConstructAst,
												   "Empty path is invalid in detected intersection");
			}
			return andFinalStateList;
		} else {
			return andStateList;
		}
	}

	public LinkedList stateAtOr(LinkedList remainingStateList1, LinkedList remainingStateList2)
	throws MappingSemanticException {
		if(remainingStateList1 == null) {
			return remainingStateList2;
		} else if(remainingStateList2 == null) {
			return remainingStateList1;
		} else {
			remainingStateList1.addAll(remainingStateList2);
//DEBUG  			reportWarning(remainingState1.getFirst(parser), "!!!!!! OR !!!!!!");
			return remainingStateList1;
		}
	}

	public LinkedList stateAtOrExtended(LinkedList states, LinkedList extStates) throws MappingSemanticException, SdaiException {
		if(states == null) {
			return extStates;
		} else if(extStates == null) {
			return states;
		} else {
            for(Iterator extStIter = extStates.iterator(); extStIter.hasNext(); ) {
                WalkerState extState = WalkerState.next(extStIter);
                EntityAST extStateFirst = extState.getFirst(parser);
                for(Iterator stIter = states.iterator(); stIter.hasNext(); ) {
                    WalkerState state = WalkerState.next(stIter);
                    if(extStateFirst.isSame(state.getFirst(parser))) {
                        if(state.remaining == null) {
                            // Remove generic mapping from base type because of specialization in the extended mapping
                            stIter.remove(); 
                        }
                    }
                }
            }
            states.addAll(extStates);
            return states;
        }
	}

	private int getCompletionState(AST element, boolean extendedElement) {
		AST checkElement = getRemainingOverPathConstraint(element);
		if(checkElement != null) {
			switch(checkElement.getType()) {
			case AGGREGATE_MEMBER_CONSTRAINT: 
			case AND_CONSTRAINT:
			case ATTRIBUTE: 
			case ENTITY_CONSTRAINT:
			case INTERSECTION_CONSTRAINT:
			case INVERSE_ATTRIBUTE_CONSTRAINT:
			case INVERSE_ATTRIBUTE_CONSTRAINT_SELECT:
				return COMPLETION_FINISHED_ALL;


			case SELECT_CONSTRAINT:
				return (checkElement.getFirstChild().getNextSibling() != null ? 
					COMPLETION_FINISHED_ALL : COMPLETION_NOT_FINISHED);

			case TYPE_CONSTRAINT:
				return extendedElement ? COMPLETION_FINISHED_ALL : COMPLETION_NOT_FINISHED;

			case INSTANCE_EQUAL: 
			case NEGATION:
			case OR_CONSTRAINT:
				return COMPLETION_FINISHED_CONSTRAINT;

			case BOOLEAN_CONSTRAINT:
			case ENUMERATION_CONSTRAINT:
			case INTEGER_CONSTRAINT:
			case LOGICAL_CONSTRAINT:
			case REAL_CONSTRAINT:
			case STRING_CONSTRAINT:
				return (checkElement.getFirstChild().getNextSibling() != null ? 
					COMPLETION_FINISHED_CONSTRAINT : COMPLETION_NOT_FINISHED);
			}
		} else {
            return extendedElement ? COMPLETION_FINISHED_ALL : COMPLETION_NOT_FINISHED;
        }
		return COMPLETION_NOT_FINISHED;
	}

	private int getCompletionState(WalkerState state, boolean extendedElement) {
		if(state.mappedValue != null) {
			return COMPLETION_NOT_FINISHED;
		} else {
			return getCompletionState(state.remaining, extendedElement);
		}
	}

	public LinkedList postProcessState(LinkedList remainingStateList, boolean extendedElement)
	throws MappingSemanticException, SdaiException {
		LinkedList newStateList = new LinkedList();
		while(remainingStateList.size() != 0) {
			WalkerState state = (WalkerState)remainingStateList.removeFirst();
			newStateList.add(state);
			int completionState = getCompletionState(state, extendedElement);
			if(completionState != COMPLETION_NOT_FINISHED) {
				if(constraintLevel == 0) {
					ParserAST path = (ParserAST)state.remaining;
                    int pathType;
					if(path != null && (pathType = path.getType()) != AND_CONSTRAINT
                       && path.getInPath() == ParserAST.UNDETERMINED) {
						if(completionState == COMPLETION_FINISHED_CONSTRAINT) {
							int remainingType = getRemainingOverPathConstraint(state).getType();
							throw new MappingSemanticException(state.remaining, "Illegal type " +
															   "of constraint in the path: " +
															   _tokenNames[remainingType]);
						}
						path.setInPath(ParserAST.IN_PATH);
						pathDetected = true;
						if(pathType == PATH_CONSTRAINT || pathType == TYPE_CONSTRAINT) {
							ParserAST element2 = (ParserAST)path.getFirstChild().getNextSibling();
							if(element2 != null && element2.getInPath() != ParserAST.IN_PATH) {
								ParserAST endOfPathConstraint = (ParserAST)#(
									auxiliaryAST(element2, END_OF_PATH_CONSTRAINT), element2);
								endOfPathConstraint.setInPath(ParserAST.IN_PATH);
								path.getFirstChild().setNextSibling(endOfPathConstraint);
							}
						}
					}
				} else {
					ListIterator remainingIter = remainingStateList.listIterator();
					while(remainingIter.hasNext()) {
						WalkerState remainingState = WalkerState.next(remainingIter);
                        EntityAST stateFirst, remainingStateFirst;
						if(state.getFirstList() == null && remainingState.getFirstList() == null &&
							(stateFirst = state.getFirst(parser)).isSame(remainingStateFirst = remainingState.getFirst(parser))) {

							if(getCompletionState(remainingState, extendedElement) != 
								COMPLETION_NOT_FINISHED) {

                                if(state.remaining == null) {
                                    addTypeConstraint(stateFirst, state, stateFirst);
                                    if(remainingState.remaining != null) {
                                        reportWarning(stateFirst,
                                                      "Warning: The alternative takes precedence over other alternatives");
                                    }
                                }
                                if(remainingState.remaining == null) {
                                    addTypeConstraint(remainingStateFirst, remainingState, remainingStateFirst);
                                    if(state.remaining != null) {
                                        reportWarning(remainingStateFirst,
                                                      "Warning: The alternative takes precedence over other alternatives");
                                    }
                                }
								remainingIter.remove();
								state.remaining = #(auxiliaryAST(remainingState.remaining, OR_CONSTRAINT), 
                                                    remainingState.remaining, state.remaining
                                                    );
								((ParserAST)state.remaining).setInPath(ParserAST.NOT_IN_PATH);
							}
						}
					}
                    if(state.remaining != null) {
                        ((ParserAST)state.remaining).setInPath(ParserAST.NOT_IN_PATH);
                    }
				}
			}
		}
		return newStateList;
	}

	private void ensureIntersectPath(AST intersection) throws MappingSemanticException, SdaiException {
		AST prevSubpath = null;
		for(ParserAST subpath = (ParserAST)intersection.getFirstChild(); subpath != null; 
			subpath = (ParserAST)subpath.getNextSibling()) {

			int completionState = getCompletionState(subpath, true);
			if(completionState != COMPLETION_NOT_FINISHED 
			&& subpath.getInPath() == ParserAST.UNDETERMINED) {
				if(completionState == COMPLETION_FINISHED_CONSTRAINT) {
					throw new MappingSemanticException(subpath, 
						"Illegal type of constraint in intersection path: " +
						_tokenNames[getRemainingOverPathConstraint(subpath).getType()]);
				}
				subpath.setInPath(ParserAST.IN_PATH);
				int subpathType = subpath.getType();
				if(subpathType == PATH_CONSTRAINT || subpathType == TYPE_CONSTRAINT) {
					ParserAST element2 = (ParserAST)subpath.getFirstChild().getNextSibling();
					if(element2 != null && element2.getInPath() != ParserAST.IN_PATH) {
						ParserAST endOfPathConstraint = (ParserAST)#(
							auxiliaryAST(element2, END_OF_PATH_CONSTRAINT), element2);
						endOfPathConstraint.setInPath(ParserAST.IN_PATH);
						subpath.getFirstChild().setNextSibling(endOfPathConstraint);
					}
				}
			}
			if(subpath.getInPath() != ParserAST.IN_PATH) {
				ParserAST endOfPathConstraint = (ParserAST)#(
					auxiliaryAST(subpath, END_OF_PATH_CONSTRAINT), dupAttachTree(subpath));
				endOfPathConstraint.setInPath(ParserAST.IN_PATH);
				if(prevSubpath == null) {
					intersection.setFirstChild(endOfPathConstraint);
				} else {
					prevSubpath.setNextSibling(endOfPathConstraint);
				}
				endOfPathConstraint.setNextSibling(subpath.getNextSibling());
			}
			prevSubpath = subpath;
		}
	}

	private void ensurePathExists(LinkedList stateList) 
	throws MappingSemanticException, SdaiException {
		Iterator stateIter = stateList.iterator();
		while(stateIter.hasNext()) {
			WalkerState state = WalkerState.next(stateIter);
			if(state.remaining != null && ((ParserAST)state.remaining).getInPath() != ParserAST.IN_PATH) {
				ParserAST endOfPathConstraint = (ParserAST)#(
					auxiliaryAST(state.remaining, END_OF_PATH_CONSTRAINT), state.remaining);
				endOfPathConstraint.setInPath(ParserAST.IN_PATH);
				state.remaining = endOfPathConstraint;
			}
		}
	}

	public LinkedList combineStatesAndAimList(LinkedList aimList, LinkedList stateList)
	throws MappingSemanticException, SdaiException {
		if(!pathDetected && !(mappedValueSet != null && mappedValueReallyUsed)) {
			LinkedList newStateList = null;
			Iterator aimListIter = aimList.iterator();
			while(aimListIter.hasNext()) {
				Object aimAttrObject = aimListIter.next();
				if(aimAttrObject instanceof AttributeAST) {
					AttributeAST aimAttr = (AttributeAST)aimAttrObject;
					if(newStateList == null) {
						newStateList = new LinkedList();
					}
					boolean attrWasAttached = false;
					Iterator stateIter = stateList.iterator();
					while(stateIter.hasNext()) {
						WalkerState state = WalkerState.next(stateIter);
						if(state.getFirst(parser).isSame(aimAttr)) {
							WalkerState newState = state.dup();
							WalkerState attState = new WalkerState(aimAttr);
							attState.remaining = aimAttr.dup();
							attState.remaining.setType(ATTRIBUTE);
							((ParserAST)attState.remaining).setInPath(ParserAST.IN_PATH);
							newState.remaining = combineToAnd(attState, state, false);
							newState.setFirst(aimAttr);
							newStateList.add(newState);
							attrWasAttached = true;
						}
					}
					if(!attrWasAttached) {
						WalkerState attState = new WalkerState(aimAttr);
						attState.remaining = aimAttr.dup();
						attState.remaining.setType(ATTRIBUTE);
						((ParserAST)attState.remaining).setInPath(ParserAST.IN_PATH);
						newStateList.add(attState);
					}
				}
			}
			if(newStateList != null) {
				return newStateList;
			}
		}
		if(!(mappedValueSet != null && mappedValueReallyUsed)) {
			ensurePathExists(stateList);
		}
		return stateList;
	}
}

entityMapping throws MappingSemanticException, SdaiException
{	LinkedList states;
	Map extAttrMappings = null;
	EntityMappingAST entityMappingAst = null;
	SourceLocatorMark mark = null;
}	:
		#(mapping:ENTITY_MAPPING
			{   errorOccured = false;
				dontReport = false;
				notReported = null;
				entityMappingAst = (EntityMappingAST)mapping;
				sourceLocator = entityMappingAst;
				line = -1;
			}
			#(	ARM
				arm:ID
				{	if(((EntityAST)arm).declaration.type != DictionaryDeclaration.ENTITY) {
						reportError(aim, "Entity mapping ARM type is not an entity");
					}
					armEntity = (EntityAST)arm;
					mappedValueSet = null;
					mappedValueUsed = false;
				}
			)
			#(	aimAst:AIM
				(
					(   t:TEMPLATE_CALL 
						{   ((TemplateCallAST)t).resolve(aimAst, parser, sourceLocator);
                        	_t = t.getNextSibling();
						}
					|   o:ORIGINAL_LOCATION
						{	if(mark == null) {
								mark = new SourceLocatorMark();
								pushLocatorMark(mark);
							}
							pushLocator((SourceLocator)o);
						}
					)*
					aim:ID
					{	if(((EntityAST)aim).declaration.type != DictionaryDeclaration.ENTITY) {
							reportError(aim, "Entity mapping AIM type is not an entity");
						}
						aimEntityList.add(aim);
						if(mark != null) {
							clearLocators(mark);
							mark = null;
						}
					}
				)+
                {	try {
                        for(EntityMappingAST e = entityMappingAst.extended; e != null; e = e.nextEntityMapping) {
							mark = new SourceLocatorMark();
							pushLocatorMark(mark);
							pushLocator(e);
                            extEntityMappingAim(e);
							clearLocators(mark);
							mark = null;
                        }
                    } finally {
						if(mark != null) {
							clearLocators(mark);
							mark = null;
						}
                    }
					EntityAST.removeAimListDuplicates(aimEntityList);
                }
			)
			(	states = p:path[ENTITY_PATH] 
                {   try {
                        for(EntityMappingAST e = entityMappingAst.extended; e != null; e = e.nextEntityMapping) {
							mark = new SourceLocatorMark();
							pushLocatorMark(mark);
							pushLocator(e);
                            LinkedList extStates = extEntityMappingPath(e);
                            if(states == null) {
                                states = extStates;
                            } else if(extStates != null) {
                                states = stateAtOrExtended(states, extStates);
                            }
							clearLocators(mark);
							mark = null;
                        }
                    } finally {
						if(mark != null) {
							clearLocators(mark);
							mark = null;
						}
                    }
					if(states != null) {
                        states = postProcessState(states, true);
						Iterator stateIter = states.iterator();
						while(stateIter.hasNext()) {
							WalkerState state = (WalkerState)stateIter.next();
							if(state.remaining != null) {
// 								if(state.remaining.getType() == TYPE_CONSTRAINT) {
// 									reportWarning(arm, "!!!!!! TYPE_CONSTRAINT on the top!!!!!!");
// 								}
                                mapping.addChild(state.remaining);
							}
						}
						try {
							if(states != null) {
								WalkerState.removeDuplicates(parser, states);
							}
							EntityAST.attachEntityMappings(states, this);
						} catch(MappingSemanticException e) {
							reportError(e.token, e.message);
						}
					}
					for(EntityMappingAST e = entityMappingAst.extended; e != null; e = e.nextEntityMapping) {
						mapping.addChild(dupAttachTree(e));
					}
				}
			)?
            {   try {
					for(EntityMappingAST e = entityMappingAst.extended; e != null; e = e.nextEntityMapping) {
						mark = new SourceLocatorMark();
						pushLocatorMark(mark);
						pushLocator(e);
						if(extAttrMappings == null) {
							extAttrMappings = new HashMap();
						}
						extEntityMappingAttributes(e, extAttrMappings);
						line = -1;
						clearLocators(mark);
						mark = null;
					}
				} finally {
					if(mark != null) {
						clearLocators(mark);
						mark = null;
					}
					line = -1;
				}
            }
			(attributeMapping[extAttrMappings, false])*
			{   line = -1;
				try {
					if(!errorOccured) EntityAST.createMappings(this);
				} catch(MappingSemanticException e) {
					reportError(e.token, e.message);
				}
			}

		)
    ;

extEntityMappingAim throws MappingSemanticException, SdaiException
{
	SourceLocatorMark mark = null;
}	:
		#(mapping:ENTITY_MAPPING
			#(ARM .)
			#(	aimAst:AIM
				(	(   t:TEMPLATE_CALL
						{   ((TemplateCallAST)t).resolve(aimAst, parser, sourceLocator);
                        	_t = t.getNextSibling();
						}
					|   o:ORIGINAL_LOCATION
						{	if(mark == null) {
								mark = new SourceLocatorMark();
								pushLocatorMark(mark);
							}
							pushLocator((SourceLocator)o);
						}
					)*
					aim:ID
					{	if(((EntityAST)aim).declaration.type != DictionaryDeclaration.ENTITY) {
                            reportError(aim, "Entity mapping AIM type is not an entity");
						}
						aimEntityList.add(aim);
						if(mark != null) {
							clearLocators(mark);
							mark = null;
						}
					}
                )+
			)
			skipEntityMappingPath
			( ATTRIBUTE_MAPPING )*
            ID //flag
        )
    ;

extEntityMappingPath returns [LinkedList states = null] throws MappingSemanticException, SdaiException
	:
		#(mapping:ENTITY_MAPPING
			#(ARM .)
			#(AIM .)
			(	states = path[ENTITY_PATH] 
			)?
			( ATTRIBUTE_MAPPING )*
            ID //flag
        )
    ;

extEntityMappingAttributes[Map extAttrMappings] throws MappingSemanticException, SdaiException
	:
		#(mapping:ENTITY_MAPPING
			#(ARM .)
			#(AIM .)
			skipEntityMappingPath
			(attributeMapping[extAttrMappings, true])*
            ID //flag
        )
    ;

skipEntityMappingPath	:
		(TEMPLATE_CALL | ORIGINAL_LOCATION)*
		(
			ENTITY|ENTITY_AGGREG|EQ_ENTITY|FROMPOINT|SUPER|SUB|SELECT|EXTENDED_INTO|EXTENSION_OF|
			ATTRIBUTE_NEGATION|EQ_ATTRIBUTE|TOPOINT|ATTRIBUTE|ATTRIBUTE_AGGREG|
			AND_CONSTRUCT|OR|OR_EXTENDED|NEGATION|CONSTRAINT|STATE_LINK
		)?
	;

attributeMapping[Map extAttrMappings, boolean extended] throws MappingSemanticException, SdaiException
{	LinkedList states = null;
	LinkedList aimList = new LinkedList();
	List extAttrList = null;
	mappedValueUsed = mappedValueReallyUsed = false;
}	:
		#(mapping:ATTRIBUTE_MAPPING
			(ENUM { mappedValueUsed = mappedValueReallyUsed = true; } )?
			#(ARM_ATTRIBUTE arm:ID) 
			{	line = ((ParserAST)arm).getLine();
				if(!extended && extAttrMappings != null) {
					AttributeAST armAttrAst = (AttributeAST)arm;
					extAttrList = (List)extAttrMappings.get(armAttrAst.attribute);
				}
			}
			(	#(AIM_ATTRIBUTE 
					(	aim:ID { aimList.add(aim); } 
					|	MACRO 
					)+
				)
			)?
			(#(ARM dataType:ID))?
			{   mappedValueSet = ((AttributeAST)arm).getMappedValueSet((EntityAST)dataType);
                mappedValues = null;
				Map alternativeMappedValues = (Map)parser.attributeMappedValues.get(
					((AttributeAST)arm).attribute);
				if(alternativeMappedValues != null) {
					ENamed_type dataTypeDefintion = (dataType != null ? 
						((EntityAST)dataType).declaration.definition : null);
					mappedValues = (Map)alternativeMappedValues.get(dataTypeDefintion);
				}
			}
			(	(	{extAttrList == null}?
					states = path[ATTRIBUTE_PATH]
					(LAST)?
					{	if(mappedValueSet != null) {
							if(!mappedValueReallyUsed) {
								reportWarning(arm, "Mapped value expected but not found");
							}
						} else {
							if(mappedValueReallyUsed) {
								reportError(arm, "Mapped value should not be used for this attribute");
							}
						}
						if(states != null) {
							states = combineStatesAndAimList(aimList, postProcessState(states, true));
							Iterator stateIter = states.iterator();
							while(stateIter.hasNext()) {
								WalkerState state = (WalkerState)stateIter.next();
								if(state.mappedValue != null) {
									if(mappedValueSet == null) {
										reportError(state.mappedValue, "Unexpected mapped value: " +
													state.mappedValue.getText());
									}
								} else if(mappedValueSet != null && mappedValueReallyUsed) {
									reportError(state.getFirst(parser),
												"Mapped value needed but is missing for some constraints");
								}
								if(state.remaining != null) {
									line = state.getFirst(parser).getLine();
									mapping.addChild(state.remaining);
								}
							}
							WalkerState.removeDuplicates(parser, states);
						}
					}
				|	states = p:path[ATTRIBUTE_PATH]
					(#(LAST last:.))?
					{	states = null;
						EntityAST.processExtAttrMappings(p, last, extAttrList, this);
						for(Iterator e = extAttrList.iterator(); e.hasNext(); ) {
							ExtAttribMapping extAttribMapping = (ExtAttribMapping)e.next();
							LinkedList extStates = extAttribMapping.extStates != null
								? extAttribMapping.extStates
								: extAttribMapping.states;
							if(extStates != null) {
								for(Iterator s = extStates.iterator(); s.hasNext(); ) {
									WalkerState state = WalkerState.next(s);
									if(state.remaining != null) {
										mapping.addChild(dupAttachTree(state.remaining));
									}
								}
							}
						}
					}
				)
			)?
			{	if(!errorOccured) {
                    if(extended) {
                        AttributeAST armAttrAst = (AttributeAST)arm;
                        EAttribute armAttr = armAttrAst.attribute;
                        extAttrList = (List)extAttrMappings.get(armAttr);
                        if(extAttrList == null) {
                            extAttrList = new ArrayList();
                            extAttrMappings.put(armAttr, extAttrList);
                        }
                        extAttrList.add(new ExtAttribMapping(sourceLocator, armAttrAst,
                                                             (EntityAST)dataType, aimList, states));
                    } else {
                        if(extAttrList != null) {
                            SourceLocatorMark mark = null;
                            try {
                                for(Iterator eam = extAttrList.iterator(); eam.hasNext(); ) {
                                    ExtAttribMapping extAttribMapping = (ExtAttribMapping)eam.next();
                                    LinkedList extStates = extAttribMapping.extStates != null
                                        ? extAttribMapping.extStates
                                        : extAttribMapping.states;
                                    mark = new SourceLocatorMark();
                                    pushLocatorMark(mark);
                                    pushLocator(extAttribMapping.sourceLocator);
                                    EntityAST.attachAttributeMappings(extAttribMapping.arm,
                                                                      extAttribMapping.dataType, 
                                                                      extAttribMapping.aimList,
                                                                      extStates, this);
                                    clearLocators(mark);
                                    mark = null;
                                }
                            } finally {
                                if(mark != null) {
                                    clearLocators(mark);
                                    mark = null;
                                }
                                for(Iterator e = extAttrList.iterator(); e.hasNext(); ) {
                                    ExtAttribMapping extAttribMapping = (ExtAttribMapping)e.next();
                                    extAttribMapping.extStates = null;
                                }
                            }
                        } else {
                            EntityAST.attachAttributeMappings((AttributeAST)arm, (EntityAST)dataType, 
                                                              aimList, states, this);
                        }
                    }
                }
			}
		)
	;
	exception
	catch [MappingSemanticException e] {
        reportError(e.token, e.message);
    }

path[int pathType] returns [LinkedList states]
throws MappingSemanticException, SdaiException
{
	constraintLevel = (pathType == ENTITY_PATH ? 1 : 0);
	pathDetected = false;
	SourceLocatorMark mark = new SourceLocatorMark();
	pushLocatorMark(mark);
}	:
		states = pathElement[defaultOpt]
		{ clearLocators(mark); }
	;
	exception
	catch [MappingSemanticException e] {
		if(e.message != NESTED_EXCEPTION_MESSAGE) {
			if(dontReport) {
				e.setLocators(dupLocators());
				e.initCause(notReported);
				notReported = e;
			} else {
				reportError(e.token, e.message);
				clearLocators(mark);
			}
		}
		states = null;
		// Skip over pathElement that caused exception
		_t = skipOverPathElement(_t); // Go ahead taken from ANTLR generated code
	}

pathElement [WalkerOptions options] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{
	SourceLocatorMark mark = null;
}	:
		(   t:TEMPLATE_CALL
			{	((TemplateCallAST)t).resolve(t, parser, sourceLocator);
				_t = t.getNextSibling();
			}
		|   o:ORIGINAL_LOCATION
			{	if(mark == null) {
					mark = new SourceLocatorMark();
					pushLocatorMark(mark);
				}
				pushLocator((SourceLocator)o);
			}
		)*
		( states = entityConstruct[options]
		| states = attributeConstruct[options]
		| states = andConstruct[options]
		| states = orConstruct[true]
		| states = constraintConstruct[options]
		| stateLink:STATE_LINK 
			{	states = WalkerState.createList(((StateLink)stateLink).state);
				((StateLink)stateLink).wasVisited = true;
			}
		)
		{	if(mark != null) {
				clearLocators(mark);
			}
			if(options.postProcess) states = postProcessState(states, false);
		}
		(	enum:ENUM
			{	if(states.size() == 1 && mappedValueSet != null) {
					if(mappedValueSet.contains(enum.getText().toLowerCase())) {
						((WalkerState)states.getFirst()).mappedValue = enum;
						constraintLevel++;
					} else {
						throw new MappingSemanticException(enum, "Mapped value invalid");
					}
				} else {
					throw new MappingSemanticException(enum, "Mapped value can not be used here");
				}
			}
		)?
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("1" + e);
		throw e;
	}

pathInnerElement[WalkerOptions options] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	AndEndAST andEnd = null;
	int savedConstraintLevel = 0;
	boolean savedPathDetected = false;
}	:
		(	states = pathElement[options]
		|	#(andEndAst:AND_END 
				{	andEnd = (AndEndAST)andEndAst;
					if(andEnd.andConstruct.subpaths != null) options.reachedAndEnd = andEnd;
				}
				(	{andEnd != null && andEnd.unused}?
					.
				|	{	savedConstraintLevel = constraintLevel;
						savedPathDetected = pathDetected;
                        if(andEnd.andConstruct.oldConstraintLevel >= 0) {
                            constraintLevel = andEnd.andConstruct.oldConstraintLevel;
                            pathDetected = andEnd.andConstruct.oldPathDetected;
                        }
					}
					(states = pathInnerElement[options])?
					{	constraintLevel = savedConstraintLevel;
						pathDetected = savedPathDetected;
					}
				)
			)
			// Do we still need this?
// 			{	if(states != null && options.previousId != null &&
// 					!options.previousId.isSame(((WalkerState)states.getFirst()).getFirst(parser))) {
// 					states = null;
// 				}
// 			}
		|	#(OR_END (states = pathInnerElement[options])?)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("2" + e);
		throw e;
	}

entityConstruct[WalkerOptions options] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates = null;
	WalkerOptions innerElementOpt = new WalkerOptions();
}	:
		(	#(ENTITY
				idEntity:ID 
				{ innerElementOpt.previousId = (EntityAST)idEntity; }
				( remainingStates = pathInnerElement[innerElementOpt] )?
				{ states = stateAtEntity((EntityAST)idEntity, remainingStates, innerElementOpt); }
			)
		|	#(ENTITY_AGGREG
				idEntityAggreg:ID 
				( "i" | index:INT )
				remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtEntityAggreg((EntityAST)idEntityAggreg, index, 
						remainingStates, innerElementOpt);
				}
			)
		|	#(EQ_ENTITY
				#(ENTITY idEq:ID)
				remainingStates = value
				{	if(((EntityAST)idEq).exactType) {
						reportWarning(idEq, "Warning: Exact type was ignored");
					}
					states = stateAtEqEntity((EntityAST)idEq, remainingStates); }
			)
		|	#(FROMPOINT 
				#(ENTITY idFrom:ID) 
				// Limited to attributeConstruct by MappinPathParser
				remainingStates = pathInnerElement[innerElementOpt] 
// 				(	remainingStates = attributeConstruct
// 				|	#(OR_END remainingStates = attributeConstruct)
// 				|	#(AND_END remainingStates = attributeConstruct)
// 				)
				{	states = stateAtFromPoint((EntityAST)idFrom, remainingStates, innerElementOpt);
					states = stateAtExact((EntityAST)idFrom, states, options);
				}
			)
		|	#(SUPER 
				#(ENTITY idSuper:ID)
				{	if(((EntityAST)idSuper).exactType) {
						reportWarning(idSuper, "Warning: Exact type was ignored");
					}
					innerElementOpt.partialConstraints = true;
				}
				remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtSuper((EntityAST)idSuper, remainingStates, innerElementOpt); 
					if(innerElementOpt.constraintStateList != null) {
						innerElementOpt.partialConstraints = false;
						states = stateAtConstraint(innerElementOpt.constraintStateList,
							states, innerElementOpt);
					}
				}
			)
		|	#(SUB 
				#(ENTITY idSub:ID)
				{ innerElementOpt.partialConstraints = true; }
				remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtSub((EntityAST)idSub, remainingStates, innerElementOpt);
					if(innerElementOpt.constraintStateList != null) {
						innerElementOpt.partialConstraints = false;
						states = stateAtConstraint(innerElementOpt.constraintStateList,
							states, innerElementOpt);
					}
					states = stateAtExact((EntityAST)idSub, states, options);
				}
			)
		|	#(SELECT 
				{ innerElementOpt.postProcess = false; }
				#(ENTITY idSelect:ID) remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtSelect((EntityAST)idSelect, remainingStates, innerElementOpt);
					states = stateAtExact((EntityAST)idSelect, states, options);
				}
			)
		|	#(EXTENDED_INTO
				{ innerElementOpt.postProcess = false; }
				#(ENTITY idExtInto:ID) remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtExtendedInto((EntityAST)idExtInto, remainingStates, innerElementOpt);
				}
			)
		|	#(EXTENSION_OF
				{ innerElementOpt.postProcess = false; }
				#(ENTITY idExtOf:ID) remainingStates = pathInnerElement[innerElementOpt]
				{	states = stateAtExtensionOf((EntityAST)idExtOf, remainingStates, innerElementOpt);
				}
			)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("3" + e);
		throw e;
	}

attributeConstruct[WalkerOptions options] returns [LinkedList states = null] 
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates = null;
	LinkedList attributeStates = null;
	AST member = null;
	WalkerOptions innerElementOpt = new WalkerOptions();
}	:
		(	#(n:ATTRIBUTE_NEGATION (EQ_ATTRIBUTE) => 
//DEBUG 				{ reportWarning(n, "!!!ATTRIBUTE_NEGATION!!!"); } 
				{	innerElementOpt = new WalkerOptions(options);
					innerElementOpt.negation = true;
				}
				states = attributeConstruct[innerElementOpt]
			)
		|	#(EQ_ATTRIBUTE attributeStates = attribute
				(	remainingStates = value
					{ states = stateAtEqAttributeValue(attributeStates, remainingStates, options); }
				|	remainingStates = attribute
					{ states = stateAtEqAttributeAttribute(attributeStates, remainingStates, options); }
				)
			)
		|	#(TOPOINT 
				(	attributeStates = attribute remainingStates = pathInnerElement[innerElementOpt]
					{ states = stateAtToPoint(attributeStates, remainingStates, innerElementOpt); }
				|	#(ENTITY_AGGREG idEntityAggreg:ID ( "i" | entityIndex:INT ))
					remainingStates = pathInnerElement[innerElementOpt]
					{	states = stateAtEntityAggregToPoint((EntityAST)idEntityAggreg, 
							entityIndex, remainingStates, innerElementOpt);
					}
				|	#(ENTITY idEntity:ID)
					{	if(((EntityAST)idEntity).exactType) {
							reportWarning(idEntity, "Warning: Exact type was ignored");
						}
					}
					remainingStates = pathInnerElement[innerElementOpt]
					{	states = stateAtEntityToPoint((EntityAST)idEntity,
							remainingStates, innerElementOpt);
					}
				)
			)
		|	#(ATTRIBUTE attId:ID
				(remainingStates = pathInnerElement[innerElementOpt])?
				{ states = stateAtAttribute((AttributeAST)attId, remainingStates, innerElementOpt); }
			)
		|	#(ATTRIBUTE_AGGREG attAggId:ID
				(	"i" 
				|	index:INT { member = index; }
				)
				(remainingStates = pathInnerElement[innerElementOpt])?
				{	states = stateAtAttributeAggreg((AttributeAST)attAggId, member, 
						remainingStates, innerElementOpt);
				}
			)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("4" + e);
		throw e;
	}

andConstruct [WalkerOptions options] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates = null;
}	:
		#(andConstructAst:AND_CONSTRUCT
			(	{ scanAndEnds((AndConstructAST)andConstructAst); }
				states = and[(AndConstructAST)andConstructAst]
				{ states = stateAtIntersect((AndConstructAST)andConstructAst, states); } 
			|	states = pathElement[options]
			)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("5" + e);
		throw e;
	}

and [AndConstructAST andConstructAst] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates1 = null;
	LinkedList remainingStates2 = null;
	boolean intersection = andConstructAst.subpaths != null;
}	:
		#(AND 
			remainingStates1 = pathAndElement[andConstructAst, intersection]
			remainingStates2 = pathAndElement[andConstructAst, intersection]
			{	if(!intersection) {
					states = stateAtAnd(remainingStates1, remainingStates2);
				} else {
					states = remainingStates1;
				}
			}
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("6" + e);
		throw e;
	}

pathAndElement [AndConstructAST andConstructAst, boolean intersection] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException	:
		( (AND) => states = and[andConstructAst]
			{ states = postProcessState(states, false); }
		|	{ intersection }?
			{	andConstructAst.oldConstraintLevel = constraintLevel;
				andConstructAst.oldPathDetected = pathDetected;
				constraintLevel = 0;
				pathDetected = false;
			}
			states = pathElement[defaultOpt] 
			{	pathDetected = andConstructAst.oldPathDetected;
				constraintLevel = andConstructAst.oldConstraintLevel;
				LinkedList subpathStates = new LinkedList(states);
				Iterator subpathStateIter = subpathStates.iterator();
				while(subpathStateIter.hasNext()) {
					WalkerState state = WalkerState.next(subpathStateIter);
					if(state.remaining != null) {
						state.remaining = dupWholeTree(state.remaining);
					}
				}
				andConstructAst.subpaths.add(subpathStates);
			}
		|	states = pathElement[defaultOpt]
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("7" + e);
		throw e;
	}

orConstruct [boolean checkEmpty] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates1 = null;
	LinkedList remainingStates2 = null;
}	:
		(   #(OR
                remainingStates1 = pathOrElement
                remainingStates2 = pathOrElement
                {	states = stateAtOr(remainingStates1, remainingStates2);
                    if(checkEmpty && states == null) {
                        throw new MappingSemanticException(null, NESTED_EXCEPTION_MESSAGE);
                    }
                }
            )
        |   #(OR_EXTENDED
                /* remainingStates1 is base mapping states possibly combined with some extended mapping states
                 * remainingStates2 is extended mapping states
                 */
                remainingStates1 = pathOrElement
                remainingStates2 = pathOrElement
                {	states = stateAtOrExtended(remainingStates1, remainingStates2);
                    if(checkEmpty && states == null) {
                        throw new MappingSemanticException(null, NESTED_EXCEPTION_MESSAGE);
                    }
                }
            )
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("8" + e);
		throw e;
	}

pathOrElement returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{
	SourceLocatorMark mark = null;
}	:
		(	(OR) =>
			states = orConstruct[false]
		|	{	mark = new SourceLocatorMark();
				pushLocatorMark(mark);
			}
			states = pathElement[defaultOpt]
			{	clearLocators(mark);
			}
			exception
			catch [MappingSemanticException e] {
				if(e.message != NESTED_EXCEPTION_MESSAGE) {
					if(dontReport) {
						e.setLocators(dupLocators());
						e.initCause(notReported);
						notReported = e;
					} else {
						reportError(e.token, e.message);
						clearLocators(mark);
					}
				}
				states = null;
				// Skip over pathElement that caused exception
				_t = skipOverPathElement(_t); // Go ahead taken from ANTLR generated code
			}
		)
	;

constraintConstruct [WalkerOptions options] returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{	LinkedList remainingStates = null;
	WalkerOptions innerElementOpt = null;
}	:
		(	#(n:NEGATION (CONSTRAINT) =>
//DEBUG 				{ reportWarning(n, "!!!NEGATION!!!"); }
				{	innerElementOpt = new WalkerOptions(options);
					innerElementOpt.negation = true;
				}
				states = constraintConstruct[innerElementOpt]
			)
		|	#(CONSTRAINT
				{ constraintLevel++; } states = pathElement[defaultOpt] { constraintLevel--; }
				(remainingStates = pathInnerElement[new WalkerOptions()])?
				{ states = stateAtConstraint(states, remainingStates, options); }
			)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("9" + e);
		throw e;
	}

attribute returns [LinkedList states = null]
throws MappingSemanticException, SdaiException
{
	AST member = null;
}	:
		(	#(ATTRIBUTE 
				attId:ID
				{ states = stateAtAttribute((AttributeAST)attId, null, defaultOpt); }
			)
		|	#(ATTRIBUTE_AGGREG attAggId:ID
				(	"i" 
				|	index:INT { member = index; }
				)
				{ states = stateAtAttributeAggreg((AttributeAST)attAggId, member, null, defaultOpt); }
			)
		)
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("10" + e);
		throw e;
	}

value returns [LinkedList states = null]
{	AST remaining = null;
	AST possibleMappedValue = null;
}	:
		( s:STRING 
			{	remaining = #(auxiliaryAST(s, STRING_CONSTRAINT), astFactory.dup(s));
				possibleMappedValue = s;
			}
		| i:INT 
			{	remaining = #(auxiliaryAST(i, INTEGER_CONSTRAINT), astFactory.dup(i));
				possibleMappedValue = i;
			}
		| f:FLOAT 
			{ remaining = #(auxiliaryAST(f, REAL_CONSTRAINT), astFactory.dup(f)); possibleMappedValue = f; }
		| enum:ENUM 
			{	remaining = #(auxiliaryAST(enum, ENUMERATION_CONSTRAINT), astFactory.dup(enum));
				possibleMappedValue = enum;
			}
		)
		{	states = WalkerState.createList(remaining);
			if(possibleMappedValue != null && mappedValueSet != null && !mappedValueUsed) {
				// getText() is a bad way here!!!!!!!!!! Should be changed!!!!!!!!!!!!! V.N.
				AST mappedValue = (mappedValues != null ? 
					(AST)mappedValues.get(possibleMappedValue.getText()) : null);
				if(mappedValue != null) {
					((WalkerState)states.getFirst()).mappedValue = dupAttachTree(mappedValue);
					mappedValueReallyUsed = true;
					constraintLevel++;
				} else if(mappedValueSet.contains(possibleMappedValue.getText().toLowerCase())) {
					((WalkerState)states.getFirst()).mappedValue = possibleMappedValue;
					mappedValueReallyUsed = true;
					constraintLevel++;
				} else {
					String otherPossibleMappedValue = possibleMappedValue.getText().replace(' ', '_');
					if(mappedValueSet.contains(otherPossibleMappedValue.toLowerCase())) {
						AST newMappedValue = dupAttachTree(possibleMappedValue);
						newMappedValue.setText(otherPossibleMappedValue);
						((WalkerState)states.getFirst()).mappedValue = newMappedValue;
						mappedValueReallyUsed = true;
						constraintLevel++;
					}
				}
			}
		}
	;
	exception
	catch [RecognitionException e] {
//DEBUG 		reportWarning("11" + e);
		throw e;
	}
