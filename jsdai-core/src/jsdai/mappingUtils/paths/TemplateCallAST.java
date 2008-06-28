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

import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.collections.AST;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.lang.SdaiException;

/**
 *
 * Created: Fri Oct  1 11:00:08 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class TemplateCallAST extends MappingAST {

	private boolean inAimList;
	private boolean resolved;

	public TemplateCallAST() {
		inAimList = false;
		resolved = false;
	}

	public TemplateCallAST(Token token) {
		super(token);
		inAimList = false;
		resolved = false;
	}

	public TemplateCallAST(AST other) {
		super(other);
		if(other instanceof TemplateCallAST) {
			TemplateCallAST otherTemplateCall = (TemplateCallAST)other;
			setText(otherTemplateCall.getName());
			inAimList = otherTemplateCall.inAimList;
		} else {
			inAimList = false;
		}
		resolved = false; // Sibblings do not get attached therefore the new one is not resolved
	}

	public void setText(String text) {
		super.setText(text.toUpperCase());
	}

	public String getName() {
		return getPlainText();
	}

	public AST dupAttachTree() {
		AST newAST = new TemplateCallAST(this);
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}

	public void setInAimList(boolean inAimList) {
		this.inAimList = inAimList;
	}

	public boolean isInAimList() {
		return inAimList;
	}

	public void resolve(AST parent, MappingPathParser parser,
						SourceLocator sourceLocator) throws RecognitionException, SdaiException {
		if(!resolved) {
			resolved = true;
			String name = getName();
			if(name.equals("SUBTYPE") || name.equals("SUPERTYPE")) {
// 				System.err.println(antlr.FileLineFormatter.getFormatter()
// 								   .getFormatString(sourceLocator.getFilename(),
// 													sourceLocator.getLine()) + 
// 								   "TYPE call for " + getFirstChild().toStringList() +
// 								   (inAimList ? " in AIM list" : " in reference path"));
				resolveType(parent, parser, sourceLocator);
			} else if(name.equals("/AIMCOMPLEX/")) {
// 				System.err.println(antlr.FileLineFormatter.getFormatter()
// 								   .getFormatString(sourceLocator.getFilename(), getLine()) + 
// 								   "AIMCOMPLEX call for " + getFirstChild().toStringList() +
// 								   (inAimList ? " in AIM list" : " in reference path"));
				resolveAimComplex(parent, parser, sourceLocator);
			} else {
				throw new SemanticException("Unresolved template call: " + name,
											sourceLocator.getFilename(), getLine());
			}
		}
	}

	private void resolveType(AST parent, MappingPathParser parser,
							 SourceLocator sourceLocator
							 ) throws RecognitionException, SdaiException {
		EntityAST typeEntity = (EntityAST)getFirstChild();
        ENamed_type typeEntDefinition = typeEntity.declaration.definition;
		EntityMappingAST origEntMapAst = (EntityMappingAST)parser.entityMappings.get(typeEntDefinition);
		if(origEntMapAst == null) {
			throw new SemanticException("Entity mapping not found in templace call /" +
										getName() + "(" + typeEntity.getText() + ")/",
										sourceLocator.getFilename(), getLine());
		}
		AST lastInsertedSibling;
		AST realNextSibling = getNextSibling();
		if(inAimList) {
			lastInsertedSibling = resolveTypeAimList(this, origEntMapAst, parser, sourceLocator);
			lastInsertedSibling.setNextSibling(realNextSibling);
			
			realNextSibling = parent.getNextSibling();
			int realNextSiblingType;
			AST firstAst = null;
			if(realNextSibling != null
			   && (realNextSiblingType =
				   realNextSibling.getType()) != MappingPathParser.ATTRIBUTE_MAPPING
			   && realNextSiblingType != MappingPathParser.ID) {
				firstAst = realNextSibling;
				AST firstAstEnd = firstAst;
				while((realNextSibling = firstAstEnd.getNextSibling()) != null
					  && (realNextSiblingType =
						  realNextSibling.getType()) != MappingPathParser.ATTRIBUTE_MAPPING
					  && realNextSiblingType != MappingPathParser.ID) {
					firstAstEnd = realNextSibling;
				}
				firstAstEnd.setNextSibling(null);
			}
			lastInsertedSibling =
				resolveTypePath(parent, origEntMapAst, parser, firstAst, sourceLocator, MappingPathParser.OR, false);
		} else {
			lastInsertedSibling = resolveTypePath(this, origEntMapAst, parser, null,
												  sourceLocator, MappingPathParser.OR, true);
		}
		lastInsertedSibling.setNextSibling(realNextSibling);
	}

	private AST resolveTypeAimList(AST lastInsertedSibling, EntityMappingAST origEntMapAst,
								   MappingPathParser parser, SourceLocator sourceLocator
								   ) throws RecognitionException, SdaiException {
		while(origEntMapAst != null) {
			AST aimAst = origEntMapAst.getFirstChild().getNextSibling();
			AST aimChild = aimAst.getFirstChild();
			while(aimChild != null) {
				if(aimChild.getType() == MappingPathParser.TEMPLATE_CALL) {
					((TemplateCallAST)aimChild).resolve(aimAst, parser, origEntMapAst);
				} else {
					AST aimEntity = MappingPathWalker.dupAttachTree(aimChild);
					//aimEntity.setFirstChild(MappingPathWalker.dupWholeTree(aimEntity.getFirstChild()));
					if(aimEntity.getType() != MappingPathParser.ORIGINAL_LOCATION) {
						AST origLocation = origEntMapAst.dupAttachTree();
						origLocation.setFirstChild(null);
						origLocation.setType(MappingPathParser.ORIGINAL_LOCATION);
						origLocation.setNextSibling(aimEntity);
						lastInsertedSibling.setNextSibling(origLocation);
					} else {
						lastInsertedSibling.setNextSibling(aimEntity);
					}
					lastInsertedSibling = aimEntity;
				}
				aimChild = aimChild.getNextSibling();
			}
			for(EntityMappingAST e = origEntMapAst.extended; e != null; e = e.extended) {
				lastInsertedSibling = resolveTypeAimList(lastInsertedSibling, e, parser, sourceLocator);
			}
			origEntMapAst = origEntMapAst.nextEntityMapping;
		}
		return lastInsertedSibling;
	}

	private AST resolveTypePath(AST insertPoint, EntityMappingAST origEntMapAst, MappingPathParser parser,
								AST firstAst, SourceLocator sourceLocator, int orConstructType, boolean mergeAimList
								) throws RecognitionException, SdaiException {
		AST lastInsertedSibling = insertPoint;
		while(origEntMapAst != null) {
			AST aimAst = origEntMapAst.getFirstChild().getNextSibling();
			for(AST a = aimAst.getFirstChild(); a != null; a = a.getNextSibling()) {
				if(a.getType() == MappingPathParser.TEMPLATE_CALL) {
					((TemplateCallAST)a).resolve(aimAst, parser, origEntMapAst);
				}
			}
			AST path = aimAst.getNextSibling();
			int pathType;
			if(path != null && (pathType = path.getType()) != MappingPathParser.ATTRIBUTE_MAPPING
			   && pathType != MappingPathParser.ID) {
				AST pathDup = MappingPathWalker.dupAttachTree(path);
				AST origLocation = origEntMapAst.dupAttachTree();
				origLocation.setFirstChild(null);
				origLocation.setType(MappingPathParser.ORIGINAL_LOCATION);
				origLocation.setNextSibling(pathDup);
				while((pathType = path.getType()) == MappingPathParser.TEMPLATE_CALL
					  || pathType == MappingPathParser.ORIGINAL_LOCATION) {
					if(pathType == MappingPathParser.TEMPLATE_CALL) {
						((TemplateCallAST)path).resolve(path, parser, origEntMapAst);
					}
					path = path.getNextSibling();
					AST pathDupNext = MappingPathWalker.dupAttachTree(path);
					pathDup.setNextSibling(pathDupNext);
					pathDup = pathDupNext;
				}
				//path.setFirstChild(MappingPathWalker.dupWholeTree(path.getFirstChild()));
				if(firstAst == null) {
					lastInsertedSibling.setNextSibling(origLocation);
					lastInsertedSibling = pathDup;
				} else {
					AST firstAstEnd = firstAst;
					AST firstAstLast;
					while((firstAstLast = firstAstEnd.getNextSibling()) != null) {
						firstAstEnd = firstAstLast;
					}
					firstAstEnd.setNextSibling(origLocation);
					lastInsertedSibling = createOrConstruct(insertPoint, firstAst, orConstructType);
				}
				if(mergeAimList) {
					Collection toIncludeAim =
						mergePathWithAimList(path, aimAst.getFirstChild(), origEntMapAst, parser);
					if(toIncludeAim != null) {
						for(Iterator i = toIncludeAim.iterator(); i.hasNext(); ) {
							AST aimId = (AST)i.next();
							AST entity = createEntityAst(aimId);
							lastInsertedSibling.setNextSibling(entity);
							lastInsertedSibling = createOrConstruct(insertPoint, lastInsertedSibling, orConstructType);
						}
					}
				}
				firstAst = lastInsertedSibling;
			} else if(mergeAimList) {
				for(AST a = aimAst.getFirstChild(); a != null; a = a.getNextSibling()) {
					int aType;
					if((aType = a.getType()) != MappingPathParser.TEMPLATE_CALL
					   && aType != MappingPathParser.ORIGINAL_LOCATION) {
						AST entity = createEntityAst(a);
						if(firstAst == null) {
							lastInsertedSibling.setNextSibling(entity);
							lastInsertedSibling = entity;
						} else {
							AST firstAstEnd = firstAst;
							AST firstAstLast;
							while((firstAstLast = firstAstEnd.getNextSibling()) != null) {
								firstAstEnd = firstAstLast;
							}
							firstAstEnd.setNextSibling(entity);
							lastInsertedSibling = createOrConstruct(insertPoint, firstAst, orConstructType);
						}
						firstAst = lastInsertedSibling;
					}
				}
			}
			for(EntityMappingAST e = origEntMapAst.extended; e != null; e = e.extended) {
				AST newLastInstPoint = resolveTypePath(insertPoint, e, parser, firstAst,
													   sourceLocator, MappingPathParser.OR_EXTENDED, mergeAimList);
				if(newLastInstPoint != lastInsertedSibling) {
					firstAst = newLastInstPoint;
				}
				lastInsertedSibling = newLastInstPoint;
			}
			origEntMapAst = origEntMapAst.nextEntityMapping;
		}
		return lastInsertedSibling;
	}
// 	static private String toStringList(List list) {
// 		StringBuffer buf = new StringBuffer();
// 		buf.append("[");
// 		for(Iterator i = list.iterator(); i.hasNext(); ) {
// 			Object member = i.next();
// 			if(member instanceof AST) {
// 				buf.append(((AST)member).toStringList().trim());
// 			} else {
// 				buf.append(member);
// 			}
// 			if(i.hasNext()) {
// 				buf.append(", ");
// 			}
// 		}
// 		buf.append("]");
// 		return buf.toString();
// 	}

	private static Collection mergePathWithAimList(AST path, AST aim, EntityMappingAST origEntMapAst,
												   MappingPathParser parser
												   ) throws RecognitionException, SdaiException {
		List topList = new MappingPathPreparser(parser, origEntMapAst).preparse(path);
		List toIncludeAim = null;
		for(; aim != null; aim = aim.getNextSibling()) {
			int aimType;
			if((aimType = aim.getType()) != MappingPathParser.TEMPLATE_CALL
			   && aimType != MappingPathParser.ORIGINAL_LOCATION) {
				EntityAST entity = (EntityAST)aim;
				boolean includedInList = false;
				for(Iterator i = topList.iterator(); i.hasNext(); ) {
					AST member = (AST)i.next();
					if(member instanceof EntityAST) {
						if(member.getNextSibling() != null) {
							//Complex entity
							final AST start = member;
							String complexName =
								EntityAST.makeComplexName(new AbstractCollection() {
										public Iterator iterator() {
											return new Iterator() {
													AST current = start;
													public boolean hasNext() {
														return current != null;
													}

													public Object next() {
														AST next = current;
														current = current.getNextSibling();
														return next;
													}

													public void remove() {
														throw new UnsupportedOperationException();
													}
												};
										}

										public int size() {
											return start.getNextSibling() != null ? 2 : 1;
										}
									});
							if(entity.declaration.definition.getName(null).equals(complexName)) {
								includedInList = true;
								break;
							}
						} else {
							EntityAST pathEntity = (EntityAST)member;
							if(entity.isSame(pathEntity)) {
								includedInList = true;
								break;
							}
						}
					}
				}
				if(!includedInList) {
					if(toIncludeAim == null) {
						toIncludeAim = new ArrayList();
					}
					toIncludeAim.add(entity);
				}
			}
		}
		return toIncludeAim;
	}

	private static AST createEntityAst(AST id) {
		AST entity = new MappingAST(id);
		entity.setType(MappingPathParser.ENTITY);
		entity.setText("ENTITY");
		entity.setFirstChild(MappingPathWalker.dupAttachTree(id));
		return entity;
	}

	private AST createOrConstruct(AST insertPoint, AST lastInsertedSibling, int orConstructType) {
		MappingAST orAst = new MappingAST();
		orAst.setType(orConstructType);
		orAst.setText("OR");
		orAst.setLine(getLine());
		orAst.setFirstChild(lastInsertedSibling);
		insertPoint.setNextSibling(orAst);
		return orAst;
	}

	private void resolveAimComplex(AST parent, MappingPathParser parser, SourceLocator sourceLocator
								   ) throws RecognitionException, SdaiException {
		AST realNextSibling = getNextSibling();
		List alts = new ArrayList();
		for(AST child = getFirstChild(); child != null; child = child.getNextSibling()) {
			int childType = child.getType();
			if(childType == MappingPathParser.TEMPLATE_CALL) {
				TemplateCallAST templateCall = (TemplateCallAST)child;
				if(!templateCall.resolved) {
					AST tempCallNext = child.getNextSibling();
					((TemplateCallAST)child).resolve(parent, parser, sourceLocator);
					if(alts.isEmpty()) {
						for(AST newChild = child.getNextSibling();
							newChild != tempCallNext;
							newChild = newChild.getNextSibling()) {

							child = newChild;
							childType = child.getType();
							if(childType == MappingPathParser.TEMPLATE_CALL) {
								((TemplateCallAST)child).resolve(parent, parser, sourceLocator);
							} else if(childType != MappingPathParser.ORIGINAL_LOCATION) {
								List nameIds = new ArrayList();
								nameIds.add(newChild);
								alts.add(nameIds);
							}
						}
					} else {
						AST newChild = child.getNextSibling();
						if(newChild != tempCallNext) {
							List newAlts = null;
							for(; newChild != tempCallNext; newChild = newChild.getNextSibling()) {
								child = newChild;
								childType = child.getType();
								if(childType == MappingPathParser.TEMPLATE_CALL) {
									((TemplateCallAST)child).resolve(parent, parser, sourceLocator);
								} else if(childType != MappingPathParser.ORIGINAL_LOCATION) {
									for(Iterator i = alts.iterator(); i.hasNext(); ) {
										List nameIds = (List)i.next();
										List newNameIds = new ArrayList(nameIds);
										newNameIds.add(newChild);
										if(newAlts == null) {
											newAlts = new ArrayList();
										}
										newAlts.add(newNameIds);
									}
								}
							}
							if(newAlts != null) {
								alts = newAlts;
							}
						}
					}
				}
			} else if(childType != MappingPathParser.ORIGINAL_LOCATION) {
				if(alts.isEmpty()) {
					List nameIds = new ArrayList();
					nameIds.add(child);
					alts.add(nameIds);
				} else {
					for(Iterator i = alts.iterator(); i.hasNext(); ) {
						List nameIds = (List)i.next();
						nameIds.add(child);
					}
				}
			}
		}
		AST lastInsertedSibling = this;
		for(Iterator i = alts.iterator(); i.hasNext(); ) {
			List nameIds = (List)i.next();
            EntityAST entityAST = new EntityAST();
            entityAST.setType(MappingPathParser.ID);
			entityAST.setLine(getLine());
            entityAST.setIdentifier(parser, EntityAST.makeComplexName(nameIds), true);
			if(entityAST.declaration != null) {
				lastInsertedSibling.setNextSibling(entityAST);
				lastInsertedSibling = entityAST;
			}
		}
		lastInsertedSibling.setNextSibling(realNextSibling);
	}

} // TemplateCallAST
