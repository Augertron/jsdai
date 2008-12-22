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
	import java.awt.event.WindowAdapter;
	import java.awt.event.WindowEvent;
	import javax.swing.JFrame;

	import jsdai.lang.*;
	import jsdai.SExtended_dictionary_schema.*;
	import jsdai.SMapping_schema.*;
	import jsdai.mappingUtils.MappingData;
	import jsdai.tools.RepositoryChanges;
	import antlr.*;

	import java.io.*;
}

class MappingPathParser extends Parser;
options {
	buildAST = true;
	k = 2;
}
tokens {
	SCHEMA_MAPPING;
	UOF_MAPPING;
	ENTITY_MAPPING;
	ATTRIBUTE_MAPPING;
	EQ_ENTITY<AST=MappingAST>;
	EQ_ATTRIBUTE<AST=MappingAST>;
	ENTITY<AST=MappingAST>;
	ENTITY_AGGREG<AST=MappingAST>;
	ATTRIBUTE<AST=MappingAST>;
	ATTRIBUTE_AGGREG<AST=MappingAST>;
    TEMPLATE_CALL<AST=TemplateCallAST>;
	SELECT<AST=MappingAST>;
	CONSTRAINT<AST=MappingAST>;
	OR<AST=MappingAST>;
	OR_EXTENDED<AST=MappingAST>;
	AND<AST=MappingAST>;
	AND_CONSTRUCT<AST=MappingAST>;
	OR_END<AST=MappingAST>;
	AND_END<AST=AndEndAST>;
	INT<AST=MappingAST>;
	FLOAT<AST=MappingAST>;
	ENUM<AST=MappingAST>;
	LAST<AST=MappingAST>;
	ARM;
	ARM_ATTRIBUTE;
	AIM;
	AIM_ATTRIBUTE;
	SUB<AST=MappingAST>;
	SUPER<AST=MappingAST>;
	TOPOINT<AST=MappingAST>;
	FROMPOINT<AST=MappingAST>;
    EXTENDED_INTO<AST=MappingAST>;
    EXTENSION_OF<AST=MappingAST>;
	ATTRIBUTE_NEGATION<AST=MappingAST>;
	NEGATION<AST=MappingAST>;
    STATE_LINK;
    ORIGINAL_LOCATION;

	SCHEMA_MAPPING_INFO;
	STRONG;
	ENTRY_POINT;
	STRONG_USERS;
	DERIVED_VARIANT;
}

{
//private static int andNum = 0;
	private static final int PATH_ALL = 0;
	private static final int PATH_ATTRIBUTE_ONLY = 1;

    static public SdaiSession sdaiSession;
    static private final String repositoryName = "ExpressCompilerRepo";
    private SdaiRepository expressRepository;
    public SdaiModel mappingModel;
    public SdaiModel armModel;
    public SdaiModel aimModel;
	public HashMap armDeclarations;
	public HashMap aimDeclarations;
	public HashMap attributeMappedValues;
    public Map entityMappings;
	public ConstraintFactory constraintFactory;

//     private SchemaInstance aimSchemaInstance;

	private ESchema_mapping schemaMappingInstance;
	private AUof_mapping uofInstances;
	private AEntity_mapping entityMappingInstances;
    private Map entityExtendedMappings;

	private boolean mappingError;
	private boolean mappedValueUsed;

	protected TokenStreamSelector selector;
	protected LinkedList fileNames;

	protected String infoFileName;
	protected AST infoParserTree;
	protected boolean reportMissing;
    protected Collection missingSchemaMatchers;
	protected String trackChangesFile;
	
	static private TokenStreamSelector createSelector(String fileName) throws FileNotFoundException {
		TokenStreamSelector selector = new TokenStreamSelector();
		MappingPathLexer lexer = new MappingPathLexer(new FileReader(fileName));
		lexer.setFilename(fileName);
		selector.addInputStream(lexer, fileName);
		selector.select(fileName);
		return selector;
	}

	public MappingPathParser(String fileName, String infoFileName, boolean reportMissing, Collection missingSchemaMatchers,
                             String trackChangesFile) throws FileNotFoundException {
		this(createSelector(fileName), fileName);
		this.infoFileName = infoFileName;
		this.infoParserTree = null;
		this.reportMissing = reportMissing;
		this.missingSchemaMatchers = missingSchemaMatchers;
		this.trackChangesFile = trackChangesFile;
		setASTNodeClass("jsdai.mappingUtils.paths.MappingAST");
	}

	private MappingPathParser(TokenStreamSelector selector, String fileName) {
		this(selector);
		setFilename(fileName);
		fileNames = new LinkedList();
		fileNames.add(fileName);
		this.selector = selector;
	}

	public void pushFile(String fileName, boolean relative) throws FileNotFoundException, IOException {
		if(relative) {
			String fileParent = new File(getFilename()).getParent();
			if(fileParent != null) {
				fileName =  fileParent + File.separator + fileName;
			}
		}
		MappingPathLexer lexer = new MappingPathLexer(new FileReader(fileName));
		lexer.setFilename(fileName);
		selector.push(lexer);
		setFilename(fileName);
		fileNames.add(fileName);
	}

	public void popFile() {
		selector.pop();
		fileNames.removeLast();
		setFilename((String)fileNames.getLast());
	}

    static protected ESchema_definition getSchemaDefinition(SdaiModel model) throws SdaiException {
		ASchema_definition schemaDefinitions = 
			(ASchema_definition)model.getInstances(ESchema_definition.class);
		return schemaDefinitions.getByIndex(1);
    }

    static protected void promoteSdaiModelToRO(SdaiModel model) throws SdaiException {
		if(model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		}
    }

    static protected void addToAEntity(AEntity aggregate, EEntity value) throws SdaiException {
		aggregate.addByIndex(aggregate.getMemberCount() + 1, value);
    }

    static protected void addEntityMappingToMap(EntityMappingAST entMapAst,
                                                EntityAST armEntity, Map map) {
        ENamed_type armDefinition = armEntity.declaration.definition;
        EntityMappingAST currEntMapAst =
            (EntityMappingAST)map.get(armDefinition);
        if(currEntMapAst != null) {
            while(currEntMapAst.nextEntityMapping != null) {
                currEntMapAst = currEntMapAst.nextEntityMapping;
            }
            currEntMapAst.nextEntityMapping = entMapAst;
        } else {
            map.put(armDefinition, entMapAst);
        }
    }

    /** Finds model with given name
     * @param repository Repository to find model in.
     * @param findModelName model name to find
     * @throws SdaiException Exception MO_NVLD is thrown if model with this name is not found.
     * Other exceptions can be thrown as well.
     * @return Model which was found
     */    
    static public SdaiModel findModel(SdaiRepository repository, String findModelName)
	throws SdaiException {
        SdaiModel model = repository.findSdaiModel(findModelName);
        if(model != null) return model;
        throw new SdaiException(SdaiException.MO_NVLD, repository, 
                                "Model not found: " + findModelName);
    }

    static public SchemaInstance findSchemaInstance(SdaiRepository repository, String findInstanceName)
    throws SdaiException {
        SchemaInstance schemaInstance = repository.findSchemaInstance(findInstanceName);
        if(schemaInstance != null) return schemaInstance;
        throw new SdaiException(SdaiException.SI_NEXS, repository, 
                                "Schema instance not found: " + findInstanceName);
    }

    public void reportError(RecognitionException ex) {
		System.err.println(ex);
		mappingError = true;
    }

	public final void schemaMapping()
	throws RecognitionException, TokenStreamException, SdaiException,FileNotFoundException,IOException {
		sdaiSession = SdaiSession.openSession();
		try {
			schemaMappingInternal();
		} finally {
			if(sdaiSession.testActiveTransaction()) {
				sdaiSession.getActiveTransaction().endTransactionAccessAbort();
			}
			sdaiSession.closeSession();
		}
	}

}

schemaMappingInternal throws SdaiException, FileNotFoundException, IOException
{
	SdaiTransaction transaction = null;
	String mappingModelName = null;
}	:
		"schema_mapping"! mappingId:ID!<AST=CommonLowerCaseAST> { #mappingId.setType(SCHEMA_MAPPING); }
		OPAREN! armId:ID<AST=CommonLowerCaseAST> COMMA! aimId:ID<AST=CommonLowerCaseAST> CPAREN! SEP!

		{	transaction = sdaiSession.startTransactionReadWriteAccess();
			expressRepository = sdaiSession.linkRepository(repositoryName, null);
			expressRepository.openRepository();
			if(trackChangesFile != null) {
				RepositoryChanges.rememberRepositoryState(expressRepository);
			}

			mappingModelName = #mappingId.getText().toUpperCase() + "_MAPPING_DATA";
			mappingModel = expressRepository.findSdaiModel(mappingModelName);
			if(mappingModel != null) mappingModel.deleteSdaiModel();
			mappingModel = expressRepository.createSdaiModel(mappingModelName, SMapping_schema.class);
			mappingModel.startReadWriteAccess();
			armModel = findModel(expressRepository, 
				#armId.getText().toUpperCase() + "_DICTIONARY_DATA");
			promoteSdaiModelToRO(armModel);
			aimModel = findModel(expressRepository, 
				#aimId.getText().toUpperCase() + "_DICTIONARY_DATA");
			promoteSdaiModelToRO(aimModel);
			constraintFactory = new ConstraintFactory(this);
			armDeclarations = DictionaryDeclaration.create(armModel);
			aimDeclarations = DictionaryDeclaration.create(aimModel);
// 			aimSchemaInstance = findSchemaInstance(expressRepository, #aimId.getText());
				
			schemaMappingInstance = 
				(ESchema_mapping)mappingModel.createEntityInstance(ESchema_mapping.class);
			schemaMappingInstance.setId(null, #mappingId.getText());
			schemaMappingInstance.setSource(null, getSchemaDefinition(armModel));
			schemaMappingInstance.setTarget(null, getSchemaDefinition(aimModel));
			uofInstances = schemaMappingInstance.createUofs(null);
			attributeMappedValues = new HashMap();
            entityMappings = new LinkedHashMap();
            entityExtendedMappings = new HashMap();
			entityMappingInstances = null;
			if(infoFileName != null) {
				AST saveReturnAST = returnAST;
				returnAST = null;
				pushFile(infoFileName, false);
				schemaMappingInfo();
				infoParserTree = getAST();
				popFile();
				returnAST = saveReturnAST;

// 				antlr.debug.misc.ASTFrame frame = 
// 					new antlr.debug.misc.ASTFrame("AST JTree", infoParserTree);
// 				frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
// 				frame.setVisible(true);
			}
		}

		mappings
		"end_schema_mapping"! SEP!

		{	#schemaMappingInternal = #(#mappingId, #schemaMappingInternal);
            for(Iterator i = entityExtendedMappings.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry)i.next();
                Object armEntity = (Object)entry.getKey();
                EntityMappingAST extEntMapAst = (EntityMappingAST)entry.getValue();
                EntityMappingAST entMapAst = (EntityMappingAST)entityMappings.get(armEntity);
                if(entMapAst == null) {
                    reportError(new SemanticException("Base mapping not found for extended " +
													  "entity mapping " + extEntMapAst.getFirstChild()
													  .getFirstChild().getText(),
													  extEntMapAst.getFilename(), extEntMapAst.getLine()));
                } else {
					entMapAst = entMapAst.matchAlternative(this, extEntMapAst);
					entMapAst.extended = extEntMapAst;
				}
            }
            for(Iterator i = entityMappings.values().iterator(); i.hasNext(); ) {
                EntityMappingAST entMapAst = (EntityMappingAST)i.next();
                do {
                    try {
                        new MappingPathWalker(this).entityMapping(entMapAst);
                    } catch(MappingSemanticException e) {
                        System.err.println("FATAL ERROR! " +
                                           "MappingSemanticException should be caught before");
                        e.printStackTrace();
                        System.exit(1);
                    }
                    entMapAst = entMapAst.nextEntityMapping;
                } while(entMapAst != null);
            }
			EntityAST.setAttributeMappingDomains(this);
			if(infoParserTree != null) {
				pushFile(infoFileName, false);
				new MappingInfoWalker(this).schemaMappingInfo(infoParserTree);
				popFile();
			}
			transaction.commit();
			if(false) { // Debug output
				//AST tree = getAST();
				antlr.debug.misc.ASTFrame frame = (
					new antlr.debug.misc.ASTFrame("AST JTree", #schemaMappingInternal));
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				final WindowAdapter windowClosed = new WindowAdapter() {
					public synchronized void windowClosed(WindowEvent e) {
						notify();
					}
				};
				frame.addWindowListener(windowClosed);
				frame.setVisible(true);
				try {
					synchronized(windowClosed) { windowClosed.wait(); }
				} catch (InterruptedException e) { }
			}
			mappingModel.endReadWriteAccess();
			armModel.endReadOnlyAccess();
			aimModel.endReadOnlyAccess();
			if(trackChangesFile != null) {
				RepositoryChanges.trackRepositoryChanges(expressRepository, trackChangesFile);
			}
			expressRepository.closeRepository();
			transaction.endTransactionAccessCommit();
			if(reportMissing) {
				new MappingData(sdaiSession).findMissingMappingsOperation(mappingModelName, missingSchemaMatchers);
			}
		}
	;

includeMapping! throws SdaiException, FileNotFoundException, IOException :
		"include" fileName:STRING SEP
		{   pushFile(#fileName.getText(), true);
        }
        mappings
        EOF //Consuming EOF is a safeguard ensuring no mess is left over for the including stream
        {   #includeMapping = getAST();
			popFile();
		}
	;

mappings throws SdaiException, FileNotFoundException, IOException :
        (includeMapping|uofMapping|entityMapping)*
    ;

uofMapping throws SdaiException	:
		"uof_mapping"! id:ID!<AST=CommonLowerCaseAST>  { #id.setType(UOF_MAPPING); }
		OPAREN! uofId:ID<AST=CommonLowerCaseAST> CPAREN! SEP!

		{
			EUof_mapping uofMappingInstance = 
				(EUof_mapping)mappingModel.createEntityInstance(EUof_mapping.class);
			uofMappingInstance.setName(null, #uofId.getText());
			entityMappingInstances = uofMappingInstance.createMappings(null);
			uofInstances.addUnordered(uofMappingInstance);
		}

		( entityMapping	)*
		"end_uof_mapping"! SEP!
 		{	#uofMapping = #(#id, #uofMapping);
			entityMappingInstances = null;
		}
	;

entityMapping throws SdaiException
{
	int entityMappingLine = LT(1).getLine();
    EntityAST armEntity = null;
	mappingError = false;
}	:
		"entity_mapping"! id:ID!<AST=EntityMappingAST>
		OPAREN!
        e:armId COMMA! { armEntity = (EntityAST)#e.getFirstChild(); } aimEntity
        CPAREN! (flag:ID!)? SEP!
		(entityMappingConstraints)?
		(attributeMapping[armEntity])*
		"end_entity_mapping"! SEP!
		{	#entityMapping = #(#id, #entityMapping, #flag);
            EntityMappingAST entMapAst = (EntityMappingAST)(#entityMapping/*#id*/);
            entMapAst.setType(ENTITY_MAPPING);
            entMapAst.setFilename(getFilename());
            entMapAst.setLine(entityMappingLine);
            entMapAst.setUofInstances(entityMappingInstances);
			if(!mappingError) {
                if(#flag != null) {
                    if(#flag.getText().equals("extended")) {
                        addEntityMappingToMap(entMapAst, armEntity, entityExtendedMappings);
                    } else {
                        throw new SemanticException("Unsupported entity mapping flag: " + #flag.getText(), 
                                                    getFilename(), id.getLine());
                    }
                } else {
                    addEntityMappingToMap(entMapAst, armEntity, entityMappings);
                }
			}
		}
	;

entityMappingConstraints
{
	AST last;
}	:
		"mapping_constraints"! SEP!
		( EXTENDED_INTO! )? ( last = pathElement[PATH_ALL] )?
		"end_mapping_constraints"! SEP!
	;

attributeMapping[EntityAST entity]
{
	AST last = null;
}	:
		"attribute_mapping"! id:ID!<AST=CommonLowerCaseAST>  { #id.setType(ATTRIBUTE_MAPPING); }
		OPAREN! armAttribute[entity] 
		(COMMA! (aimAttribute)? (COMMA! armId)?)? CPAREN! SEP!
		{ mappedValueUsed = false; }
		( EXTENDED_INTO! )? ( last = pathElement[PATH_ALL] )?
		"end_attribute_mapping"! SEP!
		{	#attributeMapping = (mappedValueUsed ? 
				#(#id, #[ENUM, "mappedValueUsed"], #attributeMapping) :
				#(#id, #attributeMapping)
			);
            if(last != null) {
                #attributeMapping.addChild(#(#[LAST, "LAST"], last));
            }
		}
	;

entity
{	boolean aggregate = false;
}	:
		(	VBAR! id1:ID<AST=EntityAST> VBAR!
	  		{	#id1.setIdentifier(this, id1, true);
				#id1.exactType = true;
	  		}
		|	id2:ID<AST=EntityAST> 
	  		{	#id2.setIdentifier(this, id2, true);
	  		}
	  		(	{ #id2 == null || ((EntityAST)#id2).declaration.type == DictionaryDeclaration.TYPE }?
				(OSQBRAC ("i" | INT) CSQBRAC) => 
				OSQBRAC! ("i" | INT) CSQBRAC! { aggregate = true; }
	  		)?
		)
		{	#entity = (aggregate ? 
				#([ENTITY_AGGREG, "ENTITY_AGGREG"], #entity) : 
				#([ENTITY, "ENTITY"], #entity));
		}
	;

attribute	:
		id1:ID<AST=AttributeAST> DOT! id2:ID! { #id1.setIdentifiers(this, id1, id2, true); }
		(	(OSQBRAC ("i" | INT) CSQBRAC) => 
			OSQBRAC! ("i" | INT) CSQBRAC! 
			{ #attribute = #([ATTRIBUTE_AGGREG, "ATTRIBUTE_AGGREG"], #attribute); }
		|	{ #attribute = #([ATTRIBUTE, "ATTRIBUTE"], #attribute); }
		)
	;

value	:
		STRING | INT | FLOAT | ENUM
	;

templateCall  :
        SLASH!
        name:ID^<AST=TemplateCallAST> { #name.setType(TEMPLATE_CALL); }
        (   { #name.getName().equals("SUBTYPE") || #name.getName().equals("SUPERTYPE") }?
            OPAREN! id:ID<AST=EntityAST> { #id.setIdentifier(this, id, false); } CPAREN!
        |   // AIM template
            (OPAREN! templateParam ( COMMA! templateParam )* CPAREN!)?
        )
        SLASH!
    ;

templateParam   :
        entity | attribute | value
    ;

pathElement [int nextType] returns [AST last = null]	:
		(	{nextType == PATH_ALL}? 
			last = constraintConstruct
		|	( last = entityConstruct
			| last = attributeConstruct
			| last = orConstruct[nextType]
			| last = andConstruct
            | t:templateCall { last = #t; }
			)
		)
	;

entityConstruct! returns [AST last = null]	:
		entity:entity { last = #entity; }
		(	( ( constraintSingle )? (FROMPOINT|TOPOINT|SUPER|SUB|EQ|EXTENDED_INTO|EXTENSION_OF) ) =>
			last = end:entityConstructEnd[#entity]
			{ #entityConstruct = #end; }
		|	last = next:pathElement[PATH_ALL] 
			{	if(#entity != null) {
					#entityConstruct = #entity;
					#entityConstruct.addChild(#next);
				}
			}
		|	{ #entityConstruct = #entity; }
		)
	;

entityConstructEnd [AST entity] returns [AST last = null]	:
		{ #entityConstructEnd = last = entity; }
//		( ( constraintSingle )? (FROMPOINT|TOPOINT|SUPER|SUB|EQ|EXTENDED_INTO|EXTENSION_OF) ) =>
		(! constraint:constraintSingle )?
		(	{ entity == null || entity.getType() == ENTITY }?
			(	(EQ (value|OPAREN value CPAREN)) =>
				eq:EQ^ { #eq.setType(EQ_ENTITY); }
				( value |! or:orValue[#eq, entity] { #entityConstructEnd = #or; } ) { last = null; }
			|	FROMPOINT^
				(!	{ #constraint == null }?
					constraint2:constraintSingle 
					{ #constraint = #constraint2; }
				)?
				( { last = #entityConstructEnd; }
				| last = pathElement[PATH_ATTRIBUTE_ONLY]
				)
			|	( SUPER^
				| SUB^
				| EXTENDED_INTO^
				| EXTENSION_OF^
				| EQ^ { #EQ.setType(SELECT); #EQ.setText("SELECT"); }
				)
				( { last = #entityConstructEnd; }
				| last = pathElement[PATH_ALL]
				)
			)
		|	TOPOINT^
			( { last = #entityConstructEnd; }
			| last = pathElement[PATH_ALL]
			)
		)
		{	if(#constraint != null) {
				#constraint.addChild(#entityConstructEnd);
				#entityConstructEnd = #constraint;
			}
		}
	;

orConstruct [int nextType] returns [AST last = null]
{
	LinkedList orLastList;
	Boolean realOr[] = new Boolean[] { Boolean.FALSE };
}	:
		orLastList = or:or[nextType, realOr]
		(!	{orLastList != null}?
			last = next:orAndNext[(AST)orLastList.getFirst(), nextType]
			{	if(realOr[0].booleanValue()) {
					Iterator orLastIter = orLastList.iterator();
					while(orLastIter.hasNext()) {
						AST orLast = (AST)orLastIter.next();
						orLast.addChild(#([OR_END, "OR_END"], #next));
					}
				} else {
					((AST)orLastList.getFirst()).addChild(#next);
				}
			}
		|	{	if(orLastList != null) {
					if(realOr[0].booleanValue()) {
						last = #([OR_END, "OR_END"]);
						Iterator orLastIter = orLastList.iterator();
						while(orLastIter.hasNext()) {
							AST orLast = (AST)orLastIter.next();
							orLast.addChild(last);
						}
					} else {
						last = (AST)orLastList.getFirst();
					}
				}
			}
		)
	;

orAndNext [AST orAndLast, int nextType] returns [AST last = null]
{
	AST entity = orAndLast;
}	:
		(	( ( constraintSingle )? (FROMPOINT|TOPOINT|SUPER|SUB|EQ|EXTENDED_INTO|EXTENSION_OF) ) =>
			{	orAndLast == null ||
				orAndLast.getType() == ENTITY || orAndLast.getType() == ENTITY_AGGREG
			}?
			{ entity = MappingPathWalker.dupWholeTree(orAndLast); }
			last = entityConstructEnd[entity]
		|	last = pathElement[nextType]
		)
	;

or [int nextType, Boolean realOr[]] returns [LinkedList lastList = null]
{
	AST last = null;
}	:
		(	OPAREN!
			( ( EXTENDED_INTO! )? last = pathElement[nextType] (ENUM { mappedValueUsed = true; } )? )?
			CPAREN!
		|	OANBRAC!
			( last = pathElement[nextType] (ENUM { mappedValueUsed = true; } )? )?
			CANBRAC!
		)
		(!	(OPAREN | OANBRAC) => lastList = next:or[nextType, null]
			{	if(last != null && lastList != null) lastList.add(last);
				if(#or == null) {
					#or = #next;
				} else if(#next != null) {
					#or = #([OR, "OR"], #or, #next); 
				}
				if(realOr != null) realOr[0] = Boolean.TRUE;
			}
		| { if(realOr != null) realOr[0] = Boolean.FALSE; }
		)
		{	if(last != null && lastList == null) {
				lastList = new LinkedList();
				lastList.add(last);
			}
		}
	;

andConstruct returns [AST last = null]
{
	LinkedList andLastList;
	Token andToken = LT(1);
	Boolean realAnd[] = new Boolean[] { Boolean.FALSE };
}	:
		andLastList = and[realAnd]
		(!	{andLastList != null}? 
// 			{	System.out.println(antlr.FileLineFormatter.getFormatter()
// 					.getFormatString(getFilename(), LT(1).getLine()) + 
// 					"and continues");
// 			}
			last = next:orAndNext[(AST)andLastList.getFirst(), PATH_ALL]
			{	if(realAnd[0].booleanValue()) {
					AndConstructAST andConstructAst = new AndConstructAST(andToken, AND_CONSTRUCT, 
						"AND_CONSTRUCT", andLastList);
					Iterator andLastIter = andLastList.iterator();
					while(andLastIter.hasNext()) {
						AST andLast = (AST)andLastIter.next();
						AndEndAST andEnd = new AndEndAST(AND_END, "AND_END"/* + andNum++*/, andConstructAst);
						andConstructAst.andEnd = andEnd;
						andLast.addChild(#(andEnd, #next));
					}
					#andConstruct = #(andConstructAst, #andConstruct);
				} else {
					((AST)andLastList.getFirst()).addChild(#next);
				}
			}
		|	{	if(realAnd[0].booleanValue()) {
					AndConstructAST andConstructAst = new AndConstructAST(andToken, AND_CONSTRUCT, 
						"AND_CONSTRUCT", andLastList);
					if(andLastList != null) {
						last = new AndEndAST(AND_END, "AND_END"/* + andNum++*/, andConstructAst);
						andConstructAst.andEnd = (AndEndAST)last;
						Iterator andLastIter = andLastList.iterator();
						while(andLastIter.hasNext()) {
							AST andLast = (AST)andLastIter.next();
							AndEndAST andEnd = new AndEndAST(AND_END, "AND_END"/* + andNum++*/, andConstructAst);
							andLast.addChild(#(andEnd, last));
						}
					}
					#andConstruct = #(andConstructAst, #andConstruct);
				} else {
					if(andLastList != null) last = (AST)andLastList.getFirst();
				}
			}
		)
	;

and [Boolean realAnd[]] returns [LinkedList lastList = null]
{
	AST last;
}	:
		OSQBRAC! last = pathElement[PATH_ALL] CSQBRAC!
		(!	(OSQBRAC) => lastList = next:and[null]
			{	if(last != null && lastList != null) lastList.add(last);
				#and = #([AND, "AND"], #and, #next);
				if(realAnd != null) realAnd[0] = Boolean.TRUE;
			}
		| { if(realAnd != null) realAnd[0] = Boolean.FALSE; }
		)
		{	if(last != null && lastList == null) {
				lastList = new LinkedList();
				lastList.add(last);
			}
		}
	;

attributeConstruct returns [AST last = null]	:
		attrib:attribute { last = #attributeConstruct; }
		(	( (EQ | NEQ) ) => //( EQ ) => 
			( eq:EQ^ { #eq.setType(EQ_ATTRIBUTE); } | neq:NEQ^ { #neq.setType(EQ_ATTRIBUTE); } )
			( value
			|! or:orValue[#eq, #attrib] { #attributeConstruct = #or; }
			| attribute
			) 
			{	last = null;
				if(#neq != null) {
					MappingAST negationAst = new MappingAST(neq);
					negationAst.setType(ATTRIBUTE_NEGATION);
					#attributeConstruct = #(negationAst, #attributeConstruct);
				}
			}
		|	TOPOINT^
			( { last = #attributeConstruct; }
			| last = pathElement[PATH_ALL]
			)
		|!	last = next:pathElement[PATH_ALL] 
			{ if(#attributeConstruct != null) #attributeConstruct.addChild(#next); }
		)?
	;

orValue! [AST rel, AST var]	:
		OPAREN! value:value CPAREN!
		{ AST relAST = new MappingAST(rel); #orValue = #(relAST, var, value); }
		( (OPAREN) => nextOr:orValue[rel, var]
			{ #orValue = #([OR, "OR"], #orValue, #nextOr); } )?
	;

// orValue	:
// 		OPAREN! value CPAREN!
// 		(! (OPAREN) => nextOr:orValue
// 			{ #orValue = #([OR, "OR"], #orValue, nextOr); } )?
// 	;

constraintConstruct returns [AST last = null]
{
	AST inner;
}	:
		(exclam:EXCLAM!)?
		OBRACE! inner = pathElement[PATH_ALL] CBRACE!
		{ #constraintConstruct = #([CONSTRAINT, "CONSTRAINT"], #constraintConstruct); }
		(!	last = next:pathElement[PATH_ALL]
			{ if(#constraintConstruct != null) #constraintConstruct.addChild(#next); }
		|	{ last = #constraintConstruct; }
		)
		{	if(exclam != null) {
				#exclam.setType(NEGATION);
				#constraintConstruct = #(#exclam, #constraintConstruct);
			}
		}
	;

constraintSingle
{
	AST inner;
}	:
		(exclam:EXCLAM!)? OBRACE !
		( inner = pathElement[PATH_ALL] )
		CBRACE ! { #constraintSingle = #([CONSTRAINT, "CONSTRAINT"], #constraintSingle); }
		{	if(exclam != null) {
				#exclam.setType(NEGATION);
				#constraintSingle = #(#exclam, #constraintSingle);
			}
		}
	;

armId	:
		id:ID<AST=EntityAST> { #id.setIdentifier(this, id, false); }
		{ #armId = #([ARM, "ARM"], #armId); }
	;

armAttribute[EntityAST entity]	:
		id:ID<AST=AttributeAST> { #id.setIdentifiers(this, entity, id, false); }
		{ #armAttribute = #([ARM_ATTRIBUTE, "ARM_ATTRIBUTE"], #armAttribute); }
	;

aimEntitySingleElem throws SdaiException	:
		(	id1:ID<AST=EntityAST> { #id1.setIdentifier(this, id1, true); }
		|	aimComplex
        |   t:templateCall { ((TemplateCallAST)#t).setInAimList(true); }
		)
	;

aimComplex throws SdaiException
{
	List nameIds = new ArrayList();
	Token nextToken = LT(1);
    boolean templated = false;
}	:
		(	OSQBRAC!
			(   id:ID<AST=EntityAST> { #id.setIdentifier(this, id, true); nameIds.add(#id); }
            |   t:templateCall { ((TemplateCallAST)#t).setInAimList(true); templated = true; }
            )
			CSQBRAC!
		)+
	{	if(templated) {
            TemplateCallAST templateCallAst = new TemplateCallAST(nextToken);
            templateCallAst.setType(TEMPLATE_CALL);
            templateCallAst.setText("/AIMCOMPLEX/");
            templateCallAst.setInAimList(true);
            templateCallAst.setFirstChild(#aimComplex);
            #aimComplex = templateCallAst;
        } else{
            EntityAST entityAST = new EntityAST(nextToken);
            entityAST.setType(ID);
            entityAST.setIdentifier(this, EntityAST.makeComplexName(nameIds), true);
            #aimComplex = entityAST;
        }
	}
	;

aimEntityElem throws SdaiException	:
		(	aimEntitySingleElem
		|	VBAR! aimEntitySingleElem VBAR!
			{ ((EntityAST)#aimEntityElem).exactType = true; }
		|	(OPAREN! aimEntityElem CPAREN!)+
		)
	;

aimEntity throws SdaiException	:
		aimEntityElem
		{ #aimEntity = #([AIM, "AIM"], #aimEntity); }
	;

aimAttributeSingleElem	:
		(	(ID DOT ID) =>
			id1:ID<AST=AttributeAST> DOT! id2:ID! { #id1.setIdentifiers(this, id1, id2, true); }
		|	id3:ID<AST=EntityAST> { #id3.setIdentifier(this, id3, true); }
		)
	;

aimAttributeComplexElem	:
		(	aimAttributeSingleElem
		|	VBAR! aimAttributeSingleElem VBAR!
		|	MACRO
		|	(	OSQBRAC!
				( aimAttributeSingleElem | MACRO )?
				CSQBRAC!
			)+
		)
	;

aimAttributeElem	:
		(	aimAttributeComplexElem
		|	(	OPAREN!
				( aimAttributeElem )?
				CPAREN!
			)+
		|	(	OANBRAC! // Limitation. Angle bracket is treated as parentheses
				( aimAttributeElem )?
				CANBRAC!
			)+
        |   templateCall
		)
	;

aimAttribute	:
		aimAttributeElem
		{ #aimAttribute = #([AIM_ATTRIBUTE, "AIM_ATTRIBUTE"], #aimAttribute); }
	;


// schema mapping info parsing
schemaMappingInfo throws SdaiException
{	AST top = null;
}	:
		smi:"schema_mapping_info"!
		(	id:ID!
			{	#id.setType(SCHEMA_MAPPING_INFO);
				if(!mappingModel.getName().equals(#id.getText().toUpperCase() + "_MAPPING_DATA")) {
					throw new SemanticException("Mapping info schema name doesn't match", 
						getFilename(), LT(1).getLine());
				}
				top = #id;
			}
		|	{	#smi.setType(SCHEMA_MAPPING_INFO);
				top = #smi;
			}
		)
		SEP!
		(schemaMappingInfoEntity)+
		"end_schema_mapping_info"! SEP!
		{ #schemaMappingInfo = #(top, #schemaMappingInfo); }
	;

schemaMappingInfoEntity	:
		"entity"! id:ID!<AST=EntityAST> SEP!
		{	#id.setIdentifier(this, id, false);
			#id.setType(ENTITY);
			if(#id.declaration == null || #id.declaration.type != DictionaryDeclaration.ENTITY) {
				throw new SemanticException("Identifier is not an entity", getFilename(), id.getLine());
			}
		}
		(	schemaMappingInfoAttribute[#id]
		|	schemaMappingInfoEntryPoint
		|	schemaMappingInfoEntryStrongUsers
		|	schemaMappingInfoEntryDerivedVariant
		)+
		"end_entity"! SEP!
		{ #schemaMappingInfoEntity = #(#id, #schemaMappingInfoEntity); }
	;

schemaMappingInfoAttribute[EntityAST entity]	:
		"attribute"! id:ID!<AST=AttributeAST> SEP!
		{	#id.setIdentifiers(this, entity, id, false);
			#id.setType(ATTRIBUTE);
		}
		(	schemaMappingInfoStrong
		|	schemaMappingInfoMappedValues[#id]
		)+
		"end_attribute"! SEP!
		{ #schemaMappingInfoAttribute = #(#id, #schemaMappingInfoAttribute); }
	;

schemaMappingInfoStrong	:
		type:"strong"<AST=MappingAST>
		{ #type.setType(STRONG); }
		(	"for"! id:ID!<AST=EntityAST>
			{	#id.setIdentifier(this, id, true);
				#schemaMappingInfoStrong = #(#schemaMappingInfoStrong, #id);
			}
		)?
		SEP!
	;

schemaMappingInfoOneMappedValue! [Map mappedValues] :
		value:ENUM COLON key:value
		{ mappedValues.put(#key.getText(), #value); }
	;

schemaMappingInfoMappedValues ! [AttributeAST attribute]
{	Map mappedValues = null;
}	:
		"mapped_values"
		(	"as" dataType:ID<AST=EntityAST>
			{	#dataType.setIdentifier(this, dataType, false);
				if(#dataType.declaration == null) {
					throw new SemanticException("Identifier not found", 
						getFilename(), LT(1).getLine());
				}
			}
		)?
		{	Map alternativeMappedValues = (Map)attributeMappedValues.get(attribute.attribute);
			if(alternativeMappedValues == null) {
				alternativeMappedValues = new HashMap();
				attributeMappedValues.put(attribute.attribute, alternativeMappedValues);
			}
			ENamed_type dataTypeDefintion = (#dataType != null ? 
				#dataType.declaration.definition : null);
			mappedValues = (Map)alternativeMappedValues.get(dataTypeDefintion);
			if(mappedValues == null) {
				mappedValues = new HashMap();
				alternativeMappedValues.put(dataTypeDefintion, mappedValues);
			}
		}
		schemaMappingInfoOneMappedValue[mappedValues]
		(COMMA schemaMappingInfoOneMappedValue[mappedValues])*
		SEP
	;

schemaMappingInfoEntryPoint :
		type:"entry_point"
		{ #type.setType(ENTRY_POINT); }
		(	"for" id:ID!<AST=EntityAST>
			{	#id.setIdentifier(this, id, true);
				#schemaMappingInfoEntryPoint = #(#schemaMappingInfoEntryPoint, #id);
			}
		)?
		SEP!
	;

schemaMappingInfoEntryStrongUsers :
		type:"strong_users"
		{ #type.setType(STRONG_USERS); }
		(	"for" id:ID!<AST=EntityAST>
			{	#id.setIdentifier(this, id, true);
				#schemaMappingInfoEntryStrongUsers = #(#schemaMappingInfoEntryStrongUsers, #id);
			}
		)?
		SEP!
	;

schemaMappingInfoEntryDerivedVariant
{
	AST last;
}	:
		type:"derived_variant"! SEP!
		{ #type.setType(DERIVED_VARIANT); }
		last = pathElement[PATH_ALL]
		"end_derived_variant"! SEP!
		{	#schemaMappingInfoEntryDerivedVariant = #(#type, #schemaMappingInfoEntryDerivedVariant);
		}
	;


class MappingPathLexer extends Lexer;
options {
	charVocabulary = '\3'..'\177';
	testLiterals=false;
	k = 2;
}
WS	:
		(' '
		| '\t'
		|   ( '\\' )? // STEPMOD style line continuation symbol is simply ignored
            ("\r\n"
			| '\r'
			| '\n'
			) { newline(); }
		)
		{ $setType(Token.SKIP); }
	;

MULTI_LINE_COMMENT
	:	"(*"
		(	options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2) != ')' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*)"
		{ $setType(Token.SKIP); }
	;

SINGLE_LINE_COMMENT
    :   "--"
		( ~('\n'|'\r') )*
		("\r\n"
        | '\r'
        | '\n'
        )
		{   newline();
            $setType(Token.SKIP);
        }
	;

protected
LETTER	:
		'a'..'z' | 'A'..'Z' | '_'
	;

protected
DIGIT	:
		'0'..'9'
	;

ID
options { testLiterals=true; }
	:
		LETTER (LETTER | DIGIT)*
	;

MACRO	:
		'$'! ID
	;

DOT	:	'.'
		((ID '.') => enum:ID '.' { $setText(enum.getText()); $setType(ENUM); })?
	;

STRING	:
		('\''! | '`'!) (~('`' | '\'' | '\r' | '\n'))* ('\''! | '`'!) |
		'"' (~('"' | '\r' | '\n'))* '"'!
    ;

INT	:
		( '-' )? (DIGIT)+
		( DOT (DIGIT)* { $setType(FLOAT); }	)?
	;

EXCLAM	:	'!' ;

OBRACE	:	'{' ;

CBRACE	:	'}' ;

OSQBRAC	:	'['	;

CSQBRAC	:	']'	;

OPAREN	:	'('	;

CPAREN	:	')'	;

EQ	:	'='	;

NEQ	:	"!=" ;

SUB	:	"<=" ;

SUPER	:	"=>" ;

TOPOINT	:	"->" ;

FROMPOINT	:	"<-" ;

EXTENDED_INTO   :   "*>" ;

EXTENSION_OF    :   "<*" ;

SEP	:	';'	;

COMMA	: ',' ;

VBAR	:	'|' ;

SLASH   :   '/' ;

OANBRAC	:	'<' ;

CANBRAC	:	'>' ;

COLON	:	':' ;

ASTERISK :	'*' { $setType(Token.SKIP); } ; // No support for * (it is simply ignored)
