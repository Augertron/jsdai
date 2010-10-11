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
public class EntityAST extends BaseAST implements ParserAST {
	int ttype = Token.INVALID_TYPE;
	DictionaryDeclaration declaration;
	boolean aim;
	int line;
	int inPath = UNDETERMINED;
	WalkerState pathState = null;
	boolean exactType = false;
	/** Only used for AIM entities, stores instances of {@link MappingForAttribute}
	 */
	public LinkedList attachedAttributeMappings = null;

	public EntityAST() {
	}

	public EntityAST(Token tok) {
		initialize(tok);
	}

	public static EntityAST construct(MappingPathParser parser, List nameList, boolean aim)
	throws MappingSemanticException, SdaiException {
		EntityAST newEntity = ((EntityAST)nameList.get(0)).dup();
		String newEntityName = makeComplexName(nameList);
		if(!newEntity.setIdentifierInternal(parser, newEntityName, aim)) {
			throw new MappingSemanticException(newEntity,
											   "Complex entity " + newEntityName + " not found");
		} else {
			return newEntity;
		}
	}

	public EntityAST dup() {
		return new EntityAST(this);
	}

	public AST dupAttachTree() {
		AST newAST = dup();
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}

	/** Get the token text for this node */
	public String getText() {
		if(declaration != null) {
			try {
				String tokenText = declaration.definition.getName(null);
				if(inPath == IN_PATH) tokenText = "(" + tokenText + ")";
				return tokenText;
			} catch (SdaiException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "";
		}
	}

	public void setText(String text) { }

	public void setIdentifier(MappingPathParser parser, Token token, boolean aim)
	throws SemanticException {
		setIdentifier(parser, token.getText(), aim);
	}

	public void setIdentifier(MappingPathParser parser, String name, boolean aim) {
		if(!setIdentifierInternal(parser, name, aim)) {
			parser.reportError(new SemanticException("Identifier " + name + " not found",
								parser.getFilename(), line));
		}
	}

	private boolean setIdentifierInternal(MappingPathParser parser, String name, boolean aim) {
		declaration = (DictionaryDeclaration)
			(aim ? parser.aimDeclarations.get(name.toLowerCase()) :
			parser.armDeclarations.get(name.toLowerCase()));
		return declaration != null;
	}

	/** Get the token type for this node */
	public int getType() { return ttype; }

	public int getLine() { return line; }

	public void setLine(int line) { this.line = line; }

	public void initialize(int t, String txt) {
		setType(t);
		setText(txt);
	}

	public void initialize(AST t) {
		setText(t.getText());
		setType(t.getType());
	}

	public void initialize(Token tok) {
		setText(tok.getText());
		setType(tok.getType());
		setLine(tok.getLine());
	}

	/** Set the token type for this node */
	public void setType(int ttype_) {
		ttype = ttype_;
	}

	public boolean isSame(EntityAST other) {
		return declaration.definition == other.declaration.definition;
	}

	public boolean isEqualTo(EntityAST other) {
		return isSame(other);
	}

	public boolean isSupertypeOf(EntityAST other) throws SdaiException {
		if(declaration.type != DictionaryDeclaration.ENTITY
				|| other.declaration.type != DictionaryDeclaration.ENTITY) return false;
		EEntity_definition thisEntity = (EEntity_definition)declaration.definition;
		EEntity_definition otherEntity = (EEntity_definition)other.declaration.definition;
		return isSupertypeOf(thisEntity, otherEntity);
	}

	protected static boolean isSupertypeOf(EEntity_definition thisEntity, EEntity_definition otherEntity)
	throws SdaiException {
		if(thisEntity.getComplex(null)) {
			AEntity leaves = thisEntity.getGeneric_supertypes(null);
			SdaiIterator leafIterator = leaves.createIterator();
			while(leafIterator.next()) {
				EEntity_definition leaf = (EEntity_definition)leaves.getCurrentMemberObject(leafIterator);
				if(!isSupertypeOfEntity(leaf, otherEntity)) {
					return false;
				}
			}
			return true;
		} else {
			return isSupertypeOfEntity(thisEntity, otherEntity);
		}
	}

	private static boolean isSupertypeOfEntity(EEntity_definition thisEntity, EEntity_definition otherEntity)
	throws SdaiException {
		if(otherEntity.testGeneric_supertypes(null)) {
			AEntity supertypes = otherEntity.getGeneric_supertypes(null);
			SdaiIterator supertypeIterator = supertypes.createIterator();
			while(supertypeIterator.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(supertypeIterator);
				if(supertype == thisEntity) {
					return true;
				}
				if(isSupertypeOfEntity(thisEntity, supertype)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean areCompatible(EEntity_definition entityOne, EEntity_definition entityTwo)
	throws SdaiException {
		if(entityOne == entityTwo) return true;
		AEntity entityOneDefinitions;
		if(entityOne.getComplex(null)) {
			entityOneDefinitions = entityOne.getGeneric_supertypes(null);
		} else {
			entityOneDefinitions = new AEntity_definition();
			entityOneDefinitions.addByIndex(1, entityOne);
		}
		AEntity entityTwoDefinitions;
		if(entityTwo.getComplex(null)) {
			entityTwoDefinitions = entityTwo.getGeneric_supertypes(null);
		} else {
			entityTwoDefinitions = new AEntity_definition();
			entityTwoDefinitions.addByIndex(1, entityTwo);
		}
		SdaiIterator entityOneIter = entityOneDefinitions.createIterator();
		while(entityOneIter.next()) {
			entityOne = (EEntity_definition)entityOneDefinitions.getCurrentMemberObject(entityOneIter);
			SdaiIterator entityTwoIter = entityTwoDefinitions.createIterator();
			while(entityTwoIter.next()) {
				entityTwo = (EEntity_definition)entityTwoDefinitions.getCurrentMemberObject(entityTwoIter);
				if(entityOne == entityTwo
				|| isSupertypeOf(entityOne, entityTwo)
				|| isSupertypeOf(entityTwo, entityOne)) {
					return true;
				}
			}
		}
		return false;
	}

	// --- FIXME ---
	// This method needs more testing and also doesn't cover cases when
	// entityOne and entityTwo are also complex entities.
	private static boolean isCloserThan(EEntity_definition testEntity, EEntity_definition entityOne,
	EEntity_definition entityTwo) throws SdaiException {
		if(entityOne == entityTwo) return false;
		if(testEntity == entityOne) return true;
		AEntity testEntityDefinitions;
		if(testEntity.getComplex(null)) {
			testEntityDefinitions = testEntity.getGeneric_supertypes(null);
		} else {
			testEntityDefinitions = new AEntity_definition();
			testEntityDefinitions.addByIndex(1, testEntity);
		}
		SdaiIterator testEntityIter = testEntityDefinitions.createIterator();
		while(testEntityIter.next()) {
			testEntity = (EEntity_definition)testEntityDefinitions.getCurrentMemberObject(testEntityIter);
			if(testEntity != entityTwo) {
				if(isSupertypeOf(testEntity, entityTwo)) {
					if(isSupertypeOf(entityOne, entityTwo)) return true;
				} else {
					if(isSupertypeOf(entityTwo, entityOne)) return true;
				}
			}
		}
		return false;
	}

	public boolean isSelectOf(EntityAST other) throws SdaiException {
		if(declaration.type != DictionaryDeclaration.TYPE) return false;
		EDefined_type thisType = (EDefined_type)declaration.definition;
		ENamed_type otherType = other.declaration.definition;
		return isSelectOf(thisType, otherType);
	}

	private boolean isSelectOf(EDefined_type thisType, ENamed_type otherType) throws SdaiException {
		EEntity domain = thisType.getDomain(null);
		if(!(domain instanceof ESelect_type)) return false;
		//ESelect_type select = (ESelect_type)domain;
		ANamed_type selections = declaration.getSelections(thisType); //select.getSelections(null);
		SdaiIterator selectionIterator = selections.createIterator();
		while(selectionIterator.next()) {
			ENamed_type selection = selections.getCurrentMember(selectionIterator);
			if(selection == otherType) return true;
			if(selection instanceof EDefined_type
			&& isSelectOf((EDefined_type)selection, otherType)) {
				return true;
			}
		}
		return false;
	}

	public boolean isExtensionOf(EntityAST other) throws SdaiException {
		if(declaration.type != DictionaryDeclaration.TYPE
		   || other.declaration.type != DictionaryDeclaration.TYPE) {
			return false;
		}
		EDefined_type thisType = (EDefined_type)declaration.definition;
		EDefined_type otherType = (EDefined_type)other.declaration.definition;
		if(!(otherType.getDomain(null) instanceof EExtensible_select_type)) {
			return false;
		} else {
			return isExtensionOf(thisType, otherType);
		}
	}

	private static boolean isExtensionOf(EDefined_type thisType, EDefined_type otherType
										 ) throws SdaiException {
		if(thisType == otherType) {
			return true;
		} else {
			EEntity domain = thisType.getDomain(null);
			if(domain instanceof EExtended_select_type) {
				return isExtensionOf(((EExtended_select_type)domain).getIs_based_on(null), otherType);
			} else {
				return false;
			}
		}
	}

	public boolean isEntityDefinition() {
		return declaration.type == DictionaryDeclaration.ENTITY;
	}

	public boolean isSimpleType() throws SdaiException {
		if(declaration.type != DictionaryDeclaration.TYPE) return false;
		EDefined_type type = (EDefined_type)declaration.definition;
		return isSimpleType(type);
	}

	private static boolean isSimpleType(EDefined_type type) throws SdaiException {
		EEntity domain = type.getDomain(null);
		if(domain instanceof ESimple_type || domain instanceof EEnumeration_type
				|| domain instanceof EAggregation_type) {
			return true;
		}
		if(domain instanceof EDefined_type) {
			return isSimpleType((EDefined_type)domain);
		} else {
			return false;
		}
	}

	public boolean isSelectType() throws SdaiException {
		if(declaration.type != DictionaryDeclaration.TYPE) return false;
		EDefined_type type = (EDefined_type)declaration.definition;
		return isSelectType(type);
	}

	private static boolean isSelectType(EDefined_type type) throws SdaiException {
		EEntity domain = type.getDomain(null);
		if(domain instanceof ESelect_type) return true;
		if(domain instanceof EDefined_type) return isSelectType((EDefined_type)domain);
		return false;
	}

	protected EntityAST(EntityAST other) {
		ttype = other.ttype;
		declaration = other.declaration;
		aim = other.aim;
		line = other.line;
		inPath = other.inPath;
		exactType = other.exactType;
	}

	public void setInPath(int inPath) {
		this.inPath = inPath;
	}

	public int getInPath() {
		return inPath;
	}

	static public void removeAimListDuplicates(List aimEntityList) {
		for(ListIterator i = aimEntityList.listIterator(); i.hasNext(); ) {
			EntityAST entity = (EntityAST)i.next();
			if(entity != null) {
				for(ListIterator j = aimEntityList.listIterator(i.nextIndex()); j.hasNext(); ) {
					EntityAST otherEntity = (EntityAST)j.next();
					if(otherEntity != null && entity.isSame(otherEntity)) {
						j.set(null);
					}
				}
			}
		}
		for(ListIterator i = aimEntityList.listIterator(); i.hasNext(); ) {
			if(i.next() == null) {
				i.remove();
			}
		}
	}

	static public void processExtAttrMappings(AST path, AST last,
											  Collection extAttrList, MappingPathWalker pathWalker
											  ) throws RecognitionException, MappingSemanticException,
													   SdaiException {
// 		System.err.println(antlr.FileLineFormatter.getFormatter()
// 						   .getFormatString(pathWalker.sourceLocator
// 											.getFilename(), pathWalker.line) +
// 						   "processExtAttrMappings entered: " + last);
		if(last == null) {
			return;
		}
		AST lastChild = last.getFirstChild();
		for(AST n = lastChild; n != null; n = n.getNextSibling()) {
			lastChild = n;
		}
		pathWalker.dontReport = true;
		try {
			for(Iterator e = extAttrList.iterator(); e.hasNext(); ) {
				ExtAttribMapping extAttribMapping = (ExtAttribMapping)e.next();
				if(extAttribMapping.states != null) {
					for(Iterator s = extAttribMapping.states.iterator(); s.hasNext(); ) {
						WalkerState state = (WalkerState)s.next();
// 						System.err.println(antlr.FileLineFormatter.getFormatter()
// 										   .getFormatString(extAttribMapping.sourceLocator
// 															.getFilename(),
// 															extAttribMapping.arm.getLine()) +
// 										   "states: " + extAttribMapping.states);
						int pathWalkerLine =
							pathWalker.line >= 0 ? pathWalker.line : pathWalker.sourceLocator.getLine();
						StateLink stateLink = new StateLink(state.dup(), last instanceof ParserAST
															? ((ParserAST)last).getLine()
															: pathWalkerLine);
						if(lastChild != null) {
							lastChild.setNextSibling(stateLink);
						} else {
							last.setFirstChild(stateLink);
						}
						pathWalker.notReported = null;
						LinkedList extStates = pathWalker.path(path, MappingPathWalker.ATTRIBUTE_PATH);
						if(stateLink.wasVisited && extStates != null) {
							if(extAttribMapping.extStates == null) {
								extAttribMapping.extStates = new LinkedList();
								for(Iterator sp = extAttribMapping.states.iterator(); sp.hasNext(); ) {
									WalkerState prevState = (WalkerState)sp.next();
									if(prevState == state) {
										break;
									}
									extAttribMapping.extStates.add(prevState);
								}
							}
							for(Iterator exs = extStates.iterator(); exs.hasNext(); ) {
								WalkerState extState = (WalkerState)exs.next();
								extState.notReported = pathWalker.notReported;
							}
							extAttribMapping.extStates.addAll(extStates);
						} else {
							if(extAttribMapping.extStates != null) {
								extAttribMapping.extStates.add(state);
							}
							state.notReported = pathWalker.notReported;
						}
					}
					if(extAttribMapping.extStates != null) {
						WalkerState.removeDuplicates(pathWalker.parser, extAttribMapping.extStates);
					}
				}
			}
		} finally {
			pathWalker.dontReport = false;
			pathWalker.notReported = null;
			if(lastChild != null) {
				lastChild.setNextSibling(null);
			} else {
				last.setFirstChild(null);
			}
		}
	}

	static public void attachEntityMappings(LinkedList pathStates, MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		if(pathStates.size() == 0) return;
		MappingDataWalker mappingDataWalker = new MappingDataWalker(pathWalker);
		ListIterator aimEntityIter = pathWalker.aimEntityList.listIterator();
		while(aimEntityIter.hasNext()) {
			EntityAST aimEntity = (EntityAST)aimEntityIter.next();
			Iterator stateIter = pathStates.iterator();
			while(stateIter.hasNext()) {
				WalkerState state = WalkerState.next(stateIter);
				if(state.getFirst(pathWalker.parser).isSame(aimEntity)) {
					if(aimEntity.pathState != null) {
						if(aimEntity.isSame(aimEntity.pathState.getFirst(pathWalker.parser))) {
							pathWalker.reportError(null, "Duplicated entity paths at " +
												   aimEntity.getText());
						} else {
							aimEntity = aimEntity.dup();
							aimEntityIter.add(aimEntity);
						}
					}
					mappingDataWalker.topType(state.remaining, state);
					aimEntity.pathState = state;
					stateIter.remove();
				}
			}
		}
		if(pathStates.size() != 0) {
			// Apply looser matching
			aimEntityIter = pathWalker.aimEntityList.listIterator();
			while(aimEntityIter.hasNext()) {
				EntityAST aimEntity = (EntityAST)aimEntityIter.next();
				Iterator stateIter = pathStates.iterator();
				while(stateIter.hasNext()) {
					WalkerState state = WalkerState.next(stateIter);
					if(state.getFirst(pathWalker.parser).isSupertypeOf(aimEntity)) {
						if(aimEntity.pathState != null) {
							if(aimEntity.isSame(aimEntity.pathState.getFirst(pathWalker.parser))) {
								pathWalker.reportError(null, "Duplicated entity paths at " +
													   aimEntity.getText());
							} else {
								aimEntity = aimEntity.dup();
								aimEntityIter.add(aimEntity);
							}
						}
						mappingDataWalker.topType(state.remaining, state);
						state.setFirst(aimEntity);
						aimEntity.pathState = state;
						stateIter.remove();
					}
				}
			}
		}
		if(pathStates.size() != 0) {
			StringBuffer error =
				new StringBuffer("Some entity paths were not attached to entity mappings: ");
			for(Iterator i = pathStates.iterator(); i.hasNext(); ) {
				WalkerState state = WalkerState.next(i);
				error.append(state.getFirst(pathWalker.parser));
				if(i.hasNext()) {
					error.append(", ");
				}
			}
			error.append(" AIM entities: ").append(pathWalker.aimEntityList);
			pathWalker.reportError(null, error.toString());
// 			pathWalker.reportError(null, "Some entity paths were not attached to entity mappings");
		}
		aimEntityIter = pathWalker.aimEntityList.listIterator();
		while(aimEntityIter.hasNext()) {
			EntityAST aimEntity = (EntityAST)aimEntityIter.next();
			if(aimEntity.pathState == null) {
				pathWalker.reportWarning(null, "Warning: Mapping to " +
										 aimEntity.getText() + " has no entity path");
			}
		}
	}

	static public void attachAttributeMappings(AttributeAST arm, EntityAST dataType, LinkedList aimList,
											   LinkedList pathStates, MappingPathWalker pathWalker
											   ) throws MappingSemanticException,
														RecognitionException, SdaiException {
		if(pathStates == null) {
			attachAimListAttributeMappings(arm, dataType, aimList, pathWalker);
		} else {
			attachStatesAttributeMappings(arm, dataType, aimList, pathStates, pathWalker);
		}
	}

	private static void attachAimListAttributeMappings(AttributeAST arm, EntityAST dataType,
													   LinkedList aimList, MappingPathWalker pathWalker
													   ) throws MappingSemanticException,
																RecognitionException, SdaiException {
		if(aimList == null) {
			throw new MappingSemanticException(arm,
			"AIM list can not be empty when mapping path is omitted");
		}
		if(aimList.size() > 0) {
			Iterator aimListIter = aimList.iterator();
			while(aimListIter.hasNext()) {
				Object aimObject = aimListIter.next();
				if(!(aimObject instanceof AttributeAST)) {
					throw new MappingSemanticException((AST)aimObject,
					"Attribute expected but entity found");
				}
				AttributeAST aimAttribute = (AttributeAST)aimObject;
				Iterator aimEntityIter = pathWalker.aimEntityList.iterator();
				while(aimEntityIter.hasNext()) {
					EntityAST aimEntity = (EntityAST)aimEntityIter.next();
					EntityAST startEntity =
						AttributeMappingsForEntity.extractStartEntity(aimEntity, pathWalker.parser);
					if(startEntity.declaration.definition == aimAttribute.declaration.definition
					   || aimAttribute.isSupertypeOf(startEntity)) {

						WalkerState state = new WalkerState(aimAttribute);
						EntityAST pathAttribute = aimAttribute.dup();
						pathAttribute.setType(MappingPathWalker.ATTRIBUTE);
						pathAttribute.setInPath(ParserAST.IN_PATH);
						state.remaining = pathAttribute;
						MappingForAttribute mappingForAttribute =
							new MappingForAttribute(arm, dataType, state);
						if(aimEntity.attachedAttributeMappings == null) {
							aimEntity.attachedAttributeMappings = new LinkedList();
						}
						aimEntity.attachedAttributeMappings.add(mappingForAttribute);
					}
				}
			}
		} else {
			Iterator aimEntityIter = pathWalker.aimEntityList.iterator();
			while(aimEntityIter.hasNext()) {
				EntityAST aimEntity = (EntityAST)aimEntityIter.next();
				MappingForAttribute mappingForAttribute =
					new MappingForAttribute(arm, dataType, null);
				if(aimEntity.attachedAttributeMappings == null) {
					aimEntity.attachedAttributeMappings = new LinkedList();
				}
				aimEntity.attachedAttributeMappings.add(mappingForAttribute);
			}
		}
	}

	private static boolean attachAttributeOneState(AttributeAST arm, EntityAST dataType,
		LinkedList attributeMappingsForEntities, EntityAST attributeStartEntity,
		WalkerState state, MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		Iterator attributeMappingsIter = attributeMappingsForEntities.iterator();
		boolean stateAttached = false;
		while(attributeMappingsIter.hasNext()) {
			AttributeMappingsForEntity attributeMappings =
				(AttributeMappingsForEntity)attributeMappingsIter.next();
			if(attributeMappings.startEntity.declaration.definition ==
			attributeStartEntity.declaration.definition
			|| attributeStartEntity.isSupertypeOf(attributeMappings.startEntity)) {
				if(attributeMappings.mappingsForAttributes.size() == 0) {
					attributeMappings.addMapping(new MappingForAttribute(arm, dataType, state));
					stateAttached = true;
				} else {
					MappingForAttribute mapping = attributeMappings.getFirstMapping();
					EntityAST entityState = mapping.state.getFirst(pathWalker.parser);
					if(attributeStartEntity.declaration.definition ==
					entityState.declaration.definition) {
						attributeMappings.addMapping(new MappingForAttribute(arm, dataType, state));
						stateAttached = true;
					} else if(entityState.isSupertypeOf(attributeStartEntity)) {
						attributeMappings.mappingsForAttributes.clear();
						attributeMappings.addMapping(new MappingForAttribute(arm, dataType, state));
						stateAttached = true;
					}
				}
			}
		}
		return stateAttached;
	}

	private static void attachStatesAttributeMappings(AttributeAST arm, EntityAST dataType,
													  LinkedList aimList, LinkedList pathStates,
													  MappingPathWalker pathWalker
													  ) throws MappingSemanticException,
															   RecognitionException, SdaiException {
		MappingDataWalker mappingDataWalker = new MappingDataWalker(pathWalker);
		LinkedList attributeMappingsForEntities = new LinkedList();
		Iterator aimEntityIter = pathWalker.aimEntityList.iterator();
		while(aimEntityIter.hasNext()) {
			EntityAST aimEntity = (EntityAST)aimEntityIter.next();
			attributeMappingsForEntities.add(new AttributeMappingsForEntity(aimEntity, pathWalker.parser));
		}
		try {
			ListIterator stateIter = pathStates.listIterator();
			while(stateIter.hasNext()) {
				WalkerState state = WalkerState.next(stateIter);
				pathWalker.notReported = state.notReported;
				WalkerState realState = state.dup();
				mappingDataWalker.topType(realState.remaining, realState);
				EntityAST startEntity = state.getFirst(pathWalker.parser);
				EntityAST realStartEntity = realState.getFirst(pathWalker.parser);
				boolean stateAttached =
					attachAttributeOneState(arm, dataType, attributeMappingsForEntities,
											realStartEntity, realState, pathWalker);
				if(!stateAttached
				   && startEntity.declaration.definition != realStartEntity.declaration.definition) {
					stateAttached =
						attachAttributeOneState(arm, dataType, attributeMappingsForEntities,
												startEntity, state, pathWalker);
					if(!stateAttached) {
						pathWalker.reportWarning(arm, "This kind of attribute path is unsupported. " +
												 "Attribute mapping requires " +
												 realStartEntity.getText() + " as AIM entity");
					}
				}
				if(stateAttached) {
					stateIter.remove();
				} else {
					state.notReported = pathWalker.notReported;
				}
			}
			stateIter = pathStates.listIterator();
			while(stateIter.hasNext()) {
				WalkerState state = WalkerState.next(stateIter);
				pathWalker.notReported = state.notReported;
				EntityAST startEntity = state.getFirst(pathWalker.parser);
				pathWalker.reportError(arm, "Attribute mapping was not attached to entity mappings: " +
									   startEntity.declaration.definition.getName(null));
			}
		} finally {
			pathWalker.notReported = null;
		}
		Iterator attributeMappingsIter = attributeMappingsForEntities.iterator();
		while(attributeMappingsIter.hasNext()) {
			AttributeMappingsForEntity attributeMappings =
				(AttributeMappingsForEntity)attributeMappingsIter.next();
			if(attributeMappings.entity.attachedAttributeMappings == null) {
				attributeMappings.entity.attachedAttributeMappings =
					attributeMappings.mappingsForAttributes;
			} else {
				attributeMappings.entity.attachedAttributeMappings
					.addAll(attributeMappings.mappingsForAttributes);
			}
		}
	}

	static public void createMappings(MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		EEntity_definition armDefinition = (EEntity_definition)pathWalker.armEntity.declaration.definition;
		MappingDataWalker mappingDataWalker = new MappingDataWalker(pathWalker);
		ListIterator aimEntityIter = pathWalker.aimEntityList.listIterator();
		while(aimEntityIter.hasNext()) {
			EntityAST aimEntity = (EntityAST)aimEntityIter.next();
			EEntity_definition aimDefinition = (EEntity_definition)
				(aimEntity.pathState != null ? aimEntity.pathState.getFirst(pathWalker.parser) : aimEntity)
				.declaration.definition;
			EEntity_mapping entityMappingInstance = (EEntity_mapping)(
				pathWalker.armEntity.declaration.entityMappings != null ?
				pathWalker.armEntity.declaration.entityMappings.get(aimDefinition.getName(null)) : null);
			if(entityMappingInstance == null) {
				entityMappingInstance = (EEntity_mapping)pathWalker.parser.mappingModel
					.createEntityInstance(EEntity_mapping.class);
				entityMappingInstance.setSource(null, armDefinition);
				entityMappingInstance.setTarget(null, aimDefinition);
				EEntity constraints = null;
				if(aimEntity.pathState != null) {
					constraints =
						mappingDataWalker.topPathElement(aimEntity.pathState.remaining,
														 null, aimEntity.pathState);
				}
				if(aimEntity.exactType) {
					EType_constraint newTypeConstraint = pathWalker.parser.constraintFactory
						.createExactTypeConstraint((EEntity_definition)
												   aimEntity.declaration.definition, constraints);
					entityMappingInstance.setConstraints(null, newTypeConstraint);
				} else if(constraints != null) {
					entityMappingInstance.setConstraints(null, constraints);
				}
				entityMappingInstance.setEntry_point(null, false);
				entityMappingInstance.setStrong_users(null, false);
				if(aimEntity.attachedAttributeMappings != null) {
					Iterator attributeMappingIter = aimEntity.attachedAttributeMappings.iterator();
					while(attributeMappingIter.hasNext()) {
						MappingForAttribute mappingForAttribute =
							(MappingForAttribute)attributeMappingIter.next();
						pathWalker.line = ((ParserAST)mappingForAttribute.attribute).getLine();
						try {
							if(mappingForAttribute.state == null
							   || mappingForAttribute.state.mappedValue == null) {

								mappingForAttribute.mappingInstance =
									createAttributeMapping(entityMappingInstance, mappingForAttribute,
														   mappingDataWalker, pathWalker);
							} else {
								mappingForAttribute.mappingInstance =
									createAttributeValueMapping(entityMappingInstance,
																mappingForAttribute, mappingDataWalker,
																pathWalker);
							}
						} catch(MappingSemanticException e) {
							pathWalker.reportError(e.token, e.message);
						}
					}
					if(pathWalker.armEntity.declaration.attributeMappings == null) {
						pathWalker.armEntity.declaration.attributeMappings =
							aimEntity.attachedAttributeMappings;
					} else {
						pathWalker.armEntity.declaration.attributeMappings
							.addAll(aimEntity.attachedAttributeMappings);
					}
				}
				if(pathWalker.armEntity.declaration.entityMappings == null)
					pathWalker.armEntity.declaration.entityMappings = new HashMap();
				pathWalker.armEntity.declaration.entityMappings
					.put(aimDefinition.getName(null), entityMappingInstance);
			}
			if(pathWalker.sourceLocator instanceof EntityMappingAST) {
				AEntity_mapping uofInstances =
					((EntityMappingAST)pathWalker.sourceLocator).getUofInstances();
				if(uofInstances != null) {
					uofInstances.addUnordered(entityMappingInstance);
				}
			}
		}
	}

	private static EGeneric_attribute_mapping createAttributeMapping(
		EEntity_mapping  entityMappingInstance, MappingForAttribute mappingForAttribute,
		MappingDataWalker mappingDataWalker, MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		EAttribute_mapping attributeMappingInstance =
			(EAttribute_mapping)pathWalker.parser.mappingModel
			.createEntityInstance(EAttribute_mapping.class);
		boolean attributeMappingInstanceComplete = false;
		try {
			attributeMappingInstance.setParent_entity(null, entityMappingInstance);
			attributeMappingInstance.setSource(null, mappingForAttribute.attribute.attribute);
			if(mappingForAttribute.dataType != null) {
				ANamed_type data_type = attributeMappingInstance.createData_type(null);
				data_type.addByIndex(1, mappingForAttribute.dataType.declaration.definition);
			}
			EEntity constraints = mappingForAttribute.getConstraints(mappingDataWalker);
			if(constraints != null) {
				attributeMappingInstance.setConstraints(null, constraints);
			}
			LinkedList path = mappingForAttribute.getPath(mappingDataWalker);
			if(path != null) {
				AAttribute_mapping_path_select pathAggregate =
					attributeMappingInstance.createPath(null);
				Iterator pathIter = path.iterator();
				while(pathIter.hasNext()) {
					EEntity pathElement = (EEntity)pathIter.next();
					// Because path is considered being obsolete
					// type constraint was never added as a valid type for path attribute.
					if(!(pathElement instanceof EType_constraint)) {
						try {
							pathAggregate.addByIndex(1, pathElement);
						} catch(SdaiException e) {
							if(e.getErrorId() == SdaiException.VT_NVLD) {
								pathWalker.reportWarning(mappingForAttribute.attribute,
														 "!!!! SdaiException was thrown setting " +
														 "the path using " + pathElement + " !!!!");
								//e.printStackTrace(System.err);
							} else {
								throw e;
							}
						}
					}
				}
			}
			attributeMappingInstance.setStrong(null, false);
			attributeMappingInstanceComplete = true;
			return attributeMappingInstance;
		} finally {
			if(!attributeMappingInstanceComplete) {
				attributeMappingInstance.deleteApplicationInstance();
			}
		}
	}

	private static EGeneric_attribute_mapping createAttributeValueMapping(
		EEntity_mapping  entityMappingInstance, MappingForAttribute mappingForAttribute,
		MappingDataWalker mappingDataWalker, MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		EEntity realDomain =
			mappingForAttribute.attribute.getMappedValueDomain(mappingForAttribute.dataType);
		EAttribute_mapping_value attributeMappingInstance = null;
		boolean attributeMappingInstanceComplete = false;
		try {
			if(realDomain instanceof EEnumeration_type) {
				attributeMappingInstance =
					(EAttribute_mapping_value)pathWalker.parser.mappingModel
					.createEntityInstance(EAttribute_mapping_enumeration_value.class);
				((EAttribute_mapping_enumeration_value)attributeMappingInstance)
					.setMapped_value(null, mappingForAttribute.state.mappedValue.getText().toLowerCase());
			} else if(realDomain instanceof ELogical_type) {
				String logicalString = mappingForAttribute.state.mappedValue.getText().toUpperCase();
				attributeMappingInstance =
					(EAttribute_mapping_value)pathWalker.parser.mappingModel
					.createEntityInstance(EAttribute_mapping_logical_value.class);
				((EAttribute_mapping_logical_value)attributeMappingInstance)
					.setMapped_value(null, (logicalString.equals("UNKNOWN") ?
											3 : (logicalString.equals("TRUE") ? 2 : 1)));
			} else if(realDomain instanceof EBoolean_type) {
				String boolString = mappingForAttribute.state.mappedValue.getText().toUpperCase();
				attributeMappingInstance =
					(EAttribute_mapping_value)pathWalker.parser.mappingModel
					.createEntityInstance(EAttribute_mapping_boolean_value.class);
				((EAttribute_mapping_boolean_value)attributeMappingInstance)
					.setMapped_value(null, boolString.equals("TRUE") ? true : false);
			} else {
				throw new MappingSemanticException(mappingForAttribute.attribute,
												   "Attribute value mapping is unsupported " +
												   "for this attribute");
			}
			attributeMappingInstance.setParent_entity(null, entityMappingInstance);
			attributeMappingInstance.setSource(null, mappingForAttribute.attribute.attribute);
			if(mappingForAttribute.dataType != null) {
				ANamed_type data_type = attributeMappingInstance.createData_type(null);
				data_type.addByIndex(1, mappingForAttribute.dataType.declaration.definition);
			}
			EEntity constraints = mappingForAttribute.getConstraints(mappingDataWalker);
			if(constraints != null)
				attributeMappingInstance.setConstraints(null, constraints);
			attributeMappingInstance.setStrong(null, false);
			attributeMappingInstanceComplete = true;
			return attributeMappingInstance;
		} finally {
			if(!attributeMappingInstanceComplete && attributeMappingInstance != null) {
				attributeMappingInstance.deleteApplicationInstance();
			}
		}
	}

	static public String makeComplexName(Collection complexEntities) throws SdaiException {
		TreeSet names = new TreeSet();
		Iterator complexEntityIter = complexEntities.iterator();
		while(complexEntityIter.hasNext()) {
			EntityAST entity = (EntityAST)complexEntityIter.next();
			if(entity.declaration != null) {
				EEntity_definition entityDefinition = (EEntity_definition)entity.declaration.definition;
				AEntity leaves = null;
				SdaiIterator leaveIterator = null;
				if(entityDefinition.getComplex(null)) {
					leaves = entityDefinition.getGeneric_supertypes(null);
					leaveIterator = leaves.createIterator();
				}
				while(leaveIterator == null || leaveIterator.next()) {
					EEntity_definition leave = leaveIterator != null
						? (EEntity_definition)leaves.getCurrentMemberObject(leaveIterator)
						: entityDefinition;
					Iterator otherEntityIter = complexEntities.iterator();
					boolean include = true;
					while(otherEntityIter.hasNext()) {
						EntityAST otherEntity = (EntityAST)otherEntityIter.next();
						if(entity != otherEntity && otherEntity.declaration != null) {
							EEntity_definition otherDefinition =
								(EEntity_definition)otherEntity.declaration.definition;
							AEntity otherLeaves = null;
							SdaiIterator otherLeaveIterator = null;
							if(otherDefinition.getComplex(null)) {
								otherLeaves = otherDefinition.getGeneric_supertypes(null);
								otherLeaveIterator = otherLeaves.createIterator();
							}
							while(otherLeaveIterator == null || otherLeaveIterator.next()) {
								EEntity_definition otherLeave = otherLeaveIterator != null
									? ((EEntity_definition)
									   otherLeaves.getCurrentMemberObject(otherLeaveIterator))
									: otherDefinition;
								if(isSupertypeOf(leave, otherLeave)) {
									include = false;
									break;
								}
								if(otherLeaveIterator == null) break;
							}
							if(!include) break;
						}
					}
					if(include) names.add(leave.getName(null));
					if(leaveIterator == null) break;
				}
			}
		}
		Iterator nameIter = names.iterator();
		String complexName = null;
		while(nameIter.hasNext()) {
			String name = (String)nameIter.next();
			complexName = complexName == null ? name : complexName + "+" + name;
		}
		return complexName;
	}

	private static EEntity getInvertedMappingTarget(EEntity path) throws SdaiException {
		if(path instanceof EInverse_attribute_constraint) {
			EEntity invertedAttribute = ((EInverse_attribute_constraint)path).getInverted_attribute(null);
			return getInvertedMappingTarget(invertedAttribute);
		} else if(path instanceof ESelect_constraint) {
			EEntity attribute = ((ESelect_constraint)path).getAttribute(null);
			return getInvertedMappingTarget(attribute);
		} else if(path instanceof EEntity_constraint) {
			EEntity attribute = ((EEntity_constraint)path).getAttribute(null);
			return getInvertedMappingTarget(attribute);
		} else if(path instanceof EAggregate_member_constraint) {
			EEntity attribute = ((EAggregate_member_constraint)path).getAttribute(null);
			return getInvertedMappingTarget(attribute);
		} else { //EAttribute
			EAttribute attribute = (EAttribute)path;
			return attribute.getParent(null);
		}
	}

	private static EEntity getMappingTargetBaseType(EEntity baseType) throws SdaiException {
		if(baseType instanceof EAggregation_type) {
			return getMappingTargetBaseType(((EAggregation_type)baseType).getElement_type(null));
		} else {
			return baseType;
		}
	}
	private static EEntity getMappingTarget(EEntity path) throws SdaiException {
		if(path instanceof EAggregate_member_constraint) {
			EEntity attribute = ((EAggregate_member_constraint)path).getAttribute(null);
			return getMappingTarget(attribute);
		} else if(path instanceof EAnd_constraint_relationship) {
			EEntity element1 = ((EAnd_constraint_relationship)path).getElement1(null);
			return getMappingTarget(element1);
		} else if(path instanceof EEnd_of_path_constraint) {
			EEnd_of_path_constraint eopConstraint = (EEnd_of_path_constraint)path;
			EEntity constraint = eopConstraint.getConstraints(null);
			return constraint instanceof EType_constraint ?
				((EType_constraint)constraint).getDomain(null) : null;
		} else if(path instanceof EEntity_constraint) {
			return ((EEntity_constraint)path).getDomain(null);
		} else if(path instanceof EIntersection_constraint) {
			EEntity subpath = ((EIntersection_constraint)path).getSubpaths(null).getByIndexEntity(1);
			return getMappingTarget(subpath);
		} else if(path instanceof EInverse_attribute_constraint) {
			EEntity invertedAttribute = ((EInverse_attribute_constraint)path).getInverted_attribute(null);
			return getInvertedMappingTarget(invertedAttribute);
		} else if(path instanceof EPath_constraint) {
			EPath_constraint pathConstraint = (EPath_constraint)path;
			EEntity mappingTarget = getMappingTarget(pathConstraint.getElement2(null));
			return mappingTarget != null ?
				mappingTarget : getMappingTarget(pathConstraint.getElement1(null));
		} else if(path instanceof ESelect_constraint) {
			ADefined_type definedTypes = ((ESelect_constraint)path).getData_type(null);
			EDefined_type definedType = definedTypes.getByIndex(definedTypes.getMemberCount());
			EEntity definedTypeBaseType = getMappingTargetBaseType(definedType.getDomain(null));
			return definedTypeBaseType instanceof EEntity_definition ? definedTypeBaseType : definedType;
		} else if(path instanceof EType_constraint) {
			EType_constraint typeConstraint = (EType_constraint)path;
			EEntity mappingTarget = null;
			if(typeConstraint.testConstraints(null)) {
				mappingTarget = getMappingTarget(typeConstraint.getConstraints(null));
			}
			return mappingTarget != null ?
				mappingTarget : typeConstraint.getDomain(null);
		} else if(path instanceof EAttribute) {
			EAttribute attribute = (EAttribute)path;
			EEntity baseType = (EEntity)attribute.get_object(attribute.getAttributeDefinition("domain"));
			return getMappingTargetBaseType(baseType);
		} else if(path instanceof EAttribute_mapping) {
			EAttribute_mapping attributeMapping = (EAttribute_mapping)path;
			EEntity mappingTarget = null;
			if(attributeMapping.testConstraints(null)) {
				try {
				mappingTarget = getMappingTarget(attributeMapping.getConstraints(null));
				} catch(SdaiException e) {
					System.out.println("attributeMapping: " + attributeMapping);
					e.printStackTrace(System.out);
					mappingTarget = null;
				}
			}
			if(mappingTarget == null) {
				mappingTarget = attributeMapping.getParent_entity(null).getTarget(null);
			}
			return mappingTarget;
		} else {
			throw new SdaiException(SdaiException.SY_ERR, "Unsupported constraint in a path: " + path);
		}
	}

	static public void setAttributeMappingDomains(MappingPathParser parser) throws SdaiException {
		AAttribute_mapping attributeMappings =
			(AAttribute_mapping)parser.mappingModel.getInstances(EAttribute_mapping.class);
		SdaiIterator attributeIterator = attributeMappings.createIterator();
		while(attributeIterator.next()) {
			EAttribute_mapping attributeMapping = attributeMappings.getCurrentMember(attributeIterator);
			EEntity aimEntity = getMappingTarget(attributeMapping);
			if(aimEntity instanceof ENamed_type) {
				ENamed_type aimDataType = (ENamed_type)aimEntity;
				ENamed_type dataType = null;
				if(attributeMapping.testData_type(null)) {
					ANamed_type dataTypes = attributeMapping.getData_type(null);
					dataType = dataTypes.getByIndex(dataTypes.getMemberCount());
				} else {
					EAttribute armAttribute = attributeMapping.getSource(null);
					Object attributeDomain =
						armAttribute.get_object(armAttribute.getAttributeDefinition("domain"));
					if(attributeDomain instanceof ENamed_type)
						dataType = (ENamed_type)attributeDomain;
					else if(attributeDomain instanceof EAggregation_type) {
						EAggregation_type aggregationDomain = (EAggregation_type)attributeDomain;
						EEntity aggregationElementType = aggregationDomain.getElement_type(null);
						if(aggregationElementType instanceof ENamed_type)
							dataType = (ENamed_type)aggregationElementType;
					}
				}
				if(dataType != null) {
					DictionaryDeclaration dataTypeDeclaration = (DictionaryDeclaration)
						parser.armDeclarations.get(dataType.getName(null));
					if(dataTypeDeclaration != null
					   && dataTypeDeclaration.type == DictionaryDeclaration.ENTITY
					   && dataTypeDeclaration.entityMappings != null) {
						EEntity_mapping targetMapping = null;
						Iterator entityMappingIter =
							dataTypeDeclaration.entityMappings.values().iterator();
						while(entityMappingIter.hasNext()) {
							EEntity_mapping entityMapping =
								(EEntity_mapping)entityMappingIter.next();
							EEntity mappingAimType = entityMapping.getTarget(null);
							if(mappingAimType == aimDataType
							|| (mappingAimType instanceof EEntity_definition
							&& aimDataType instanceof EEntity_definition
							&& areCompatible((EEntity_definition)aimDataType,
							(EEntity_definition)mappingAimType))) {
								if(targetMapping != null) {
									EEntity anotherMappingAimType = targetMapping.getTarget(null);
									if(aimDataType instanceof EEntity_definition
									&& mappingAimType instanceof EEntity_definition
									&& anotherMappingAimType instanceof EEntity_definition
									&& isCloserThan((EEntity_definition)aimDataType,
									(EEntity_definition)mappingAimType,
									(EEntity_definition)anotherMappingAimType)) {
										targetMapping = entityMapping;
									}
								} else {
									targetMapping = entityMapping;
								}
							}
						}
						if(targetMapping != null) {
							attributeMapping.setDomain(null, targetMapping);
							continue;
						}
					}
				}
				attributeMapping.setDomain(null, aimDataType);
			}
		}
	}

	public void createDerivedVariant(LinkedList pathStates, MappingPathWalker pathWalker)
	throws MappingSemanticException, RecognitionException, SdaiException {
		MappingDataWalker mappingDataWalker = new MappingDataWalker(pathWalker);
		Iterator stateIter = pathStates.iterator();
		while(stateIter.hasNext()) {
			WalkerState state = WalkerState.next(stateIter);
			if(state.remaining == null) {
				throw new MappingSemanticException(state.getFirst(pathWalker.parser),
					"Path needs to be specified");
			}
			EntityAST relatingAimEntity = state.getFirst(pathWalker.parser);
			EEntity_mapping relatingMapping = (EEntity_mapping)
				(declaration.entityMappings != null ?
				declaration.entityMappings.get(relatingAimEntity.declaration.definition.getName(null)) :
				null);
			if(relatingMapping == null) {
				throw new MappingSemanticException(state.getFirst(pathWalker.parser),
					declaration.definition.getName(null) + " has no mapping to " +
					relatingAimEntity.declaration.definition.getName(null));
			}
			mappingDataWalker.topType(state.remaining, state);
			EEntity relatedAim = state.getFirst(pathWalker.parser).declaration.definition;
			LinkedList path = new LinkedList();
			EEntity constraints =
				mappingDataWalker.topPathElement(state.remaining, path, state);
// 			if(path.size() == 0) {
// 				throw new MappingSemanticException(state.getFirst(pathWalker.parser),
// 					"Path can not be empty");
// 			}
			if(path.size() != 0) {
				relatedAim = getMappingTarget((EEntity)path.getFirst());
			}
			if(!(relatedAim instanceof ENamed_type)) {
				throw new MappingSemanticException(state.getFirst(pathWalker.parser),
					"Path can not point to " + relatedAim);
			}
			ENamed_type relatedAimEntity = (ENamed_type)relatedAim;
			EEntity_mapping relatedMapping = (EEntity_mapping)
				declaration.entityMappings.get(relatedAimEntity.getName(null));
			if(relatedMapping == null) {
				throw new MappingSemanticException(state.getFirst(pathWalker.parser),
					declaration.definition.getName(null) + " has no mapping to " +
					relatedAimEntity.getName(null));
			}
			EDerived_variant_entity_mapping derivedVariantInstance =
				(EDerived_variant_entity_mapping)pathWalker.parser.mappingModel
				.createEntityInstance(EDerived_variant_entity_mapping.class);
			derivedVariantInstance.setRelating(null, relatingMapping);
			derivedVariantInstance.setRelated(null, relatedMapping);
			if(constraints != null)
				derivedVariantInstance.setConstraints(null, constraints);
			if(path.size() != 0) {
				AAttribute_mapping_path_select pathAggregate = derivedVariantInstance.createPath(null);
				Iterator pathIter = path.iterator();
				while(pathIter.hasNext()) {
					EEntity pathElement = (EEntity)pathIter.next();
					pathAggregate.addByIndex(1, pathElement);
				}
			}
		}
	}

	public EEntity getDomain() throws SdaiException {
		if(!(declaration.definition instanceof EDefined_type)) return null;
		EDefined_type definedType = (EDefined_type)declaration.definition;
		return definedType.getDomain(null);
	}

	public EEntity getRealDomain() throws SdaiException {
		return getRealDomain(getDomain());
	}

	public boolean isDomainValid(EntityAST type) throws SdaiException {
		return isDomainValid(getDomain(), type.declaration.definition);
	}

	protected static EEntity getRealDomain(EEntity domain) throws SdaiException {
		if(domain instanceof EAggregation_type)
			return getRealDomain(((EAggregation_type)domain).getElement_type(null));
		if(domain instanceof EDefined_type)
			return getRealDomain(((EDefined_type)domain).getDomain(null));
		return domain;
	}

	private boolean isDomainValid(EEntity domain, ENamed_type type) throws SdaiException {
		if(domain == type) return true;
		if(domain instanceof EEntity_definition && type instanceof EEntity_definition) {
			if(isSupertypeOf((EEntity_definition)domain, (EEntity_definition)type)) {
				return true;
			}
		} else if(domain instanceof EDefined_type) {
			EEntity definedTypeDomain = ((EDefined_type)domain).getDomain(null);
			if(definedTypeDomain instanceof ESelect_type) {
				//ESelect_type select = (ESelect_type)definedTypeDomain;
				ANamed_type selections = declaration.getSelections((EDefined_type)domain);
				//select.getSelections(null);
				SdaiIterator selectionIterator = selections.createIterator();
				while(selectionIterator.next()) {
					ENamed_type selection = selections.getCurrentMember(selectionIterator);
					if(isDomainValid(selection, type)) return true;
				}
			} else {
				return isDomainValid(definedTypeDomain, type);
			}
		} else if(domain instanceof EAggregation_type) {
			return isDomainValid(((EAggregation_type)domain).getElement_type(null), type);
		}
		return false;
	}

	public List getPathTo(EntityAST entity) throws SdaiException {
		ENamed_type targetType = entity.declaration.definition;
		EEntity domain = getDomain();
		List selectPath = new ArrayList();
		if(getPathToElement(domain, targetType, selectPath)) {
			return selectPath;
		} else {
			return null;
		}
	}

	private boolean getPathToElement(EEntity domain, ENamed_type targetType, List selectPath)
	throws SdaiException {
		if(domain == targetType) {
			return true;
		} else if(domain instanceof EEntity_definition && targetType instanceof EEntity_definition) {
			if(isSupertypeOf((EEntity_definition)domain, (EEntity_definition)targetType)) {
				selectPath.add(domain);
				return true;
			}
		} else if(domain instanceof EDefined_type) {
			EEntity definedTypeDomain = ((EDefined_type)domain).getDomain(null);
			if(definedTypeDomain instanceof ESelect_type) {
				//ESelect_type select = (ESelect_type)definedTypeDomain;
				ANamed_type selections = declaration.getSelections((EDefined_type)domain);
				//select.getSelections(null);
				SdaiIterator selectionIterator = selections.createIterator();
				while(selectionIterator.next()) {
					ENamed_type selection = selections.getCurrentMember(selectionIterator);
					if(getPathToElement(selection, targetType, selectPath)) {
						selectPath.add(domain);
						return true;
					}
				}
			} else {
				return getPathToElement(definedTypeDomain, targetType, selectPath);
			}
		} else if(domain instanceof EAggregation_type) {
			return getPathToElement(((EAggregation_type)domain).getElement_type(null),
				targetType, selectPath);
		}
		return false;
	}

}
