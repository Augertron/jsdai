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

	import java.io.*;
}


class MappingInfoWalker extends TreeParser;
options {
	importVocab=MappingPathParser;
}

{
	protected MappingPathParser parser;

	public MappingInfoWalker(MappingPathParser parser) {
		this();
		this.parser = parser;
	}

	public void reportWarning(AST token, String text) {
		System.err.println(antlr.FileLineFormatter.getFormatter()
			.getFormatString(parser.getFilename(), 
				token instanceof ParserAST ? ((ParserAST)token).getLine() : 0) + 
			text);
	}
}

schemaMappingInfo throws SdaiException	:
		#(SCHEMA_MAPPING_INFO (entity)+)
	;

entity throws SdaiException	:
		#(entity:ENTITY 
			(	attribute
			|	entryPoint[(EntityAST)entity]
			|	strongUsers[(EntityAST)entity]
			|	derivedVariant[(EntityAST)entity]
			)+
		)
	;

attribute throws SdaiException	:
		#(attribute:ATTRIBUTE 
			( strong[(AttributeAST)attribute] )*
		)
	;

strong[AttributeAST attribute] throws SdaiException	:
		#(STRONG
			(aimId:ID)?
			{	if(attribute.declaration.attributeMappings == null) {
					reportWarning(attribute, attribute + " can not be set as strong: no mappings");
				} else {
					Iterator attributeMappingIter = attribute.declaration.attributeMappings.iterator();
					while(attributeMappingIter.hasNext()) {
						MappingForAttribute mappingForAttribute = (
							(MappingForAttribute)attributeMappingIter.next());
						if(mappingForAttribute.attribute.attribute == attribute.attribute &&
							(aimId == null || 
								mappingForAttribute.mappingInstance
								.getParent_entity(null).getTarget(null) == aimId)) {
							mappingForAttribute.mappingInstance.setStrong(null, true);
						}
					}
				}
			}
		)
	;

entryPoint[EntityAST entity] throws SdaiException	:
		#(ENTRY_POINT 
			(aimId:ID)?
			{	if(entity.declaration.entityMappings == null) {
					reportWarning(entity, entity + " can not be set as entry point: no mappings");
				} else {
					Iterator entityMappingIter = entity.declaration.entityMappings.values().iterator();
					while(entityMappingIter.hasNext()) {
						EEntity_mapping entityMapping = (EEntity_mapping)entityMappingIter.next();
						if(aimId == null || entityMapping.getTarget(null) == aimId) {
							entityMapping.setEntry_point(null, true);
						}
					}
				}
			}
		)
	;

strongUsers[EntityAST entity] throws SdaiException	:
		#(STRONG_USERS
			(aimId:ID)?
			{	if(entity.declaration.entityMappings == null) {
					reportWarning(entity, entity + " can not be set for strong users: no mappings");
				} else {
					Iterator entityMappingIter = entity.declaration.entityMappings.values().iterator();
					while(entityMappingIter.hasNext()) {
						EEntity_mapping entityMapping = (EEntity_mapping)entityMappingIter.next();
						if(aimId == null || entityMapping.getTarget(null) == aimId) {
							entityMapping.setStrong_users(null, true);
						}
					}
				}
			}
		)
	;

derivedVariant[final EntityAST entity] throws SdaiException	:
		#(DERIVED_VARIANT
			path:.
			{	try {
					MappingPathWalker mappingPathWalker = new MappingPathWalker(parser);
					mappingPathWalker.sourceLocator = new SourceLocator() {
							public String getFilename() {
								return parser.getFilename();
							}
							public int getLine() {
								return entity.getLine();
							}
						};
					mappingPathWalker.line = -1;
					LinkedList pathStates = mappingPathWalker.path(path, MappingPathWalker.ATTRIBUTE_PATH);
					if(pathStates != null) {
						entity.createDerivedVariant(pathStates, mappingPathWalker);
					}
				} catch(MappingSemanticException e) {
					reportWarning(e.token, e.message);
				}
			}
		)
	;
