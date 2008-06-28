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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jsdai.lang.SdaiException;
}

class MappingPathPreparser extends TreeParser;
options {
	importVocab=MappingPathParser;
}

{
	private MappingPathParser parser;
	private EntityMappingAST entityMappingAst;

	public MappingPathPreparser(MappingPathParser parser,
			 EntityMappingAST entityMappingAst) {
		this.parser = parser;
		this.entityMappingAst = entityMappingAst;
	}

	public List preparse(AST topAst) throws RecognitionException, SdaiException {
		List topList = new ArrayList();
		pathPreparse(topAst, topList);
		return topList;
	}
}

pathPreparse [List topList] throws SdaiException
	:
		(   t:TEMPLATE_CALL
			{	((TemplateCallAST)t).resolve(t, parser, entityMappingAst);
				_t = t.getNextSibling();
			}
		|   ORIGINAL_LOCATION
		)*
		( entityConstructPreparse[topList]
		| attributeConstructPreparse[topList]
		| andConstructPreparse[topList]
		| orConstructPreparse[topList]
		| constraintConstructPreparse[topList]
		| #(AND_END (pathPreparse[topList])? )
		| #(OR_END (pathPreparse[topList])? )
		)
	;

entityConstructPreparse [List topList]
{   AST entity = null;
}   :
		(	#(ENTITY
				idEntity:ID { entity = idEntity; }
			)
		|	#(ENTITY_AGGREG
				idEntityAggeg:ID { entity = idEntityAggeg; }
			)
		|	#(EQ_ENTITY
				#(ENTITY idEq:ID { entity = idEq; } )
			)
		|	#(FROMPOINT 
				#(ENTITY idFrom:ID { entity = idFrom; } )
			)
		|	#(SUPER 
				#(ENTITY idSuper:ID { entity = idSuper; } )
			)
		|	#(SUB 
				#(ENTITY idSub:ID { entity = idSub; } )
			)
		|	#(SELECT 
				#(ENTITY idSelect:ID { entity = idSelect; } )
			)
		|	#(EXTENDED_INTO
				#(ENTITY idExtInto:ID { entity = idExtInto; } )
			)
		|	#(EXTENSION_OF
				#(ENTITY idExtOf:ID { entity = idExtOf; } )
			)
		)
        { topList.add(MappingPathWalker.dupAttachTree(entity)); }
	;

attributeConstructPreparse [List topList]
{   AST attrib = null;
}  :
		(	#(ATTRIBUTE_NEGATION (EQ_ATTRIBUTE) => 
				attributeConstructPreparse[topList]
			)
		|	#(EQ_ATTRIBUTE attrib = attributePreparse
			)
		|	#(TOPOINT 
				(	attrib = attributePreparse
				|	#(ENTITY_AGGREG idEntityAggeg:ID { attrib = idEntityAggeg; } )
				|	#(ENTITY idEntity:ID { attrib = idEntity; } )
				)
			)
		|	attrib = attributePreparse
		)
        { topList.add(MappingPathWalker.dupAttachTree(attrib)); }
	;

attributePreparse returns [AST attrib = null] :
		(	#(ATTRIBUTE attId:ID { attrib = attId; } )
		|	#(ATTRIBUTE_AGGREG attAggId:ID { attrib = attAggId; } )
		)
	;

andConstructPreparse [List topList] throws SdaiException
{   List andList = null;
}   :
		#(AND_CONSTRUCT
			(	{ andList = new ArrayList(); } andPreparse[andList] { topList.addAll(andList); }
			|	pathPreparse[topList]
			)
		)
	;

andPreparse [List andList] throws SdaiException
{   List andList1 = new ArrayList();
    List andList2 = new ArrayList();
}   :
		#(AND 
			pathAndElementPreparse[andList1]
			pathAndElementPreparse[andList2]
		)
        {   for(Iterator l1 = andList1.iterator(); l1.hasNext(); ) {
                AST ast1 = (AST)l1.next();
                for(Iterator l2 = andList2.iterator(); l2.hasNext(); ) {
                    AST ast2 = (AST)l2.next();
                    AST newAst2 = MappingPathWalker.dupAttachTree(ast2);
                    AST s2 = newAst2;
                    for(AST s1 = ast2.getNextSibling(); s1 != null; s1 = s1.getNextSibling()) {
                        AST s2next = MappingPathWalker.dupAttachTree(s1);
                        s2.setNextSibling(s2next);
                        s2 = s2next;
                    }
                    AST sibbling = ast1.getNextSibling();
                    AST newAst1 = l2.hasNext() ? MappingPathWalker.dupAttachTree(ast1) : ast1;
                    newAst1.setNextSibling(newAst2);
                    ast2.setNextSibling(sibbling);
                    andList.add(newAst1);
                }
            }
        }
	;

pathAndElementPreparse [List andList] throws SdaiException
	:
		(   (AND) => andPreparse[andList]
		|	pathPreparse[andList]
		)
	;

orConstructPreparse [List topList] throws SdaiException
	:
		#(OR
			pathPreparse[topList]
			pathPreparse[topList]
		)
	;

constraintConstructPreparse [List topList] throws SdaiException
	:
		(	#(NEGATION (CONSTRAINT) =>
				constraintConstructPreparse[topList]
			)
		|	#(CONSTRAINT pathPreparse[topList] )
		)
	;
