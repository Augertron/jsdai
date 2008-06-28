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

package jsdai.tools;

import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

/* generates arm_to_aim.exp link file and addon for arm schema */
class SchemaRootDefinitions {
    
    static AEntity_declaration Aen_decl;
    static AEntity_declaration Aen_decl_bak;
    AEntity_definition Avisited_supertypes;
    AEntity_definition Aroot_en_definition;
    
    public static final void main(String argv[]) throws SdaiException {
        if (argv.length > 0 && argv[0].length() > 0 && argv[1].length() > 0){
            new SchemaRootDefinitions(argv);
        } else{
            System.out.println("Syntax: java SchemaRootDefinitions ARM_SCHEMA_NAME AIM_SCHEMA_NAME");
            System.out.println(" Ex: java SchemaRootDefinitions AP214_ARM AUTOMOTIVE_DESIGN");
            System.out.println(" Expected output: ap214_to_automotive_design_link.exp and ap214_arm.exp.add files");
        }
    }
    
    SchemaRootDefinitions(String argv[]) throws SdaiException{
        Avisited_supertypes = new AEntity_definition();
        Aroot_en_definition = new AEntity_definition();
        findRootDefinitionsFromSchema(argv[0], argv[1]);
    }
    
    void findRootDefinitionsFromSchema(String arm_schema, String aim_schema) throws SdaiException{
        //        System.out.println("ARM Schema name: " +arm_schema);
        //        System.out.println("AIM Schema name: " +aim_schema);
        SdaiSession.setLogWriter(new PrintWriter(System.out, true));
        SdaiSession session = SdaiSession.openSession();
        SdaiTransaction transaction = session.startTransactionReadOnlyAccess();
        File fd = null;
        try{
            fd = new File(arm_schema.toLowerCase() +"_to_" +aim_schema.toLowerCase() +"_link" +".exp");
            System.setOut(new PrintStream(new FileOutputStream(fd)));
        }catch (FileNotFoundException nfe){
            return ;
        }
        
        SdaiRepository repo = session.linkRepository("SystemRepository", null);
        EEntity_declaration en_decl = null;
        AEntity_definition Avisited_en_definition = new AEntity_definition();
        
        try {
            SdaiModel model = repo.findSdaiModel(aim_schema+"_DICTIONARY_DATA");
            if (model == null){
                System.out.println("Given AIM schema not found: check the name");
                return ;
            }
            
            model.startReadOnlyAccess();
            
            Aen_decl = (AEntity_declaration) model.getInstances(CEntity_declaration.class);
            Aen_decl_bak = (AEntity_declaration) model.getInstances(CEntity_declaration.class);
            
            //            System.out.println("Total number of declarations: " +Aen_decl.getMemberCount());
            
            AEntity_definition Aen_def = new AEntity_definition();
            EEntity_definition last_def = null;
            
            for(int i=1; i<= Aen_decl.getMemberCount(); i++){
                if(Aen_decl.getByIndex(i).isKindOf(EImplicit_declaration.class)){
                    //                    System.out.println("IMPLICIT ENTITY DECLARATION");
                }else{
                    if((Aen_decl.getByIndex(i) != null)){
                        EEntity_declaration en_decl_inst = (EEntity_declaration) Aen_decl.getByIndex(i);
                        EEntity_definition en_def = (EEntity_definition) en_decl_inst.getDefinition(null);
                        //                        System.out.println("Ciklas: " +i +"   Entity: " +en_def.getName(null));
                        traverse(en_def, Avisited_en_definition, last_def);
                    }
                }
            }
            //            System.out.println("Root definitions list folows: ");
            
            SortByName();
            generateDefinitionsSchemasReferences(arm_schema);
            generateUndefinedObjectStuff();
            // generate undefined_object_select
            System.out.println("TYPE undefined_object_select = SELECT (");
            for(int i=1; i<Aroot_en_definition.getMemberCount(); i++){
                System.out.println("    " +Aroot_en_definition.getByIndex(i).getName(null) +",");
            }
            System.out.println("    " +Aroot_en_definition.getByIndex(Aroot_en_definition.getMemberCount()).getName(null) );
            System.out.println(");");
            System.out.println("END_TYPE;");
            
            // generate aim_entity_select
            System.out.println();
            System.out.println("TYPE aim_entity_select = SELECT (");
            for(int i=1; i<Aroot_en_definition.getMemberCount(); i++){
                System.out.println("    " +Aroot_en_definition.getByIndex(i).getName(null) +",");
            }
            System.out.println("    " +Aroot_en_definition.getByIndex(Aroot_en_definition.getMemberCount()).getName(null) );
            System.out.println(");");
            System.out.println("END_TYPE;");
            
            // generate arm_to aim entity
            System.out.println();
            System.out.println("ENTITY arm_to_aim;");
            System.out.println("  arm_entity : arm_entity_select;");
            System.out.println("  aim_entity : aim_entity_select;");
            System.out.println("END_ENTITY;");
            
            // System.out.println("Done. ");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // deal with arm schema 
        try {
            SdaiModel model = repo.findSdaiModel(arm_schema+"_DICTIONARY_DATA");
            if (model == null){
                System.out.println("Given ARM schema not found: check the name");
                return ;
            }
            
            model.startReadOnlyAccess();
            
            Aen_decl = (AEntity_declaration) model.getInstances(CEntity_declaration.class);
            Aen_decl_bak = (AEntity_declaration) model.getInstances(CEntity_declaration.class);
            
            //            System.out.println("Total number of declarations: " +Aen_decl.getMemberCount());
            
            AEntity_definition Aen_def = new AEntity_definition();
            EEntity_definition last_def = null;
            
            for(int i=1; i<= Aen_decl.getMemberCount(); i++){
                if(Aen_decl.getByIndex(i).isKindOf(EImplicit_declaration.class)){
                    //                    System.out.println("IMPLICIT ENTITY DECLARATION");
                }else{
                    if((Aen_decl.getByIndex(i) != null)){
                        EEntity_declaration en_decl_inst = (EEntity_declaration) Aen_decl.getByIndex(i);
                        EEntity_definition en_def = (EEntity_definition) en_decl_inst.getDefinition(null);
                        //                        System.out.println("Ciklas: " +i +"   Entity: " +en_def.getName(null));
                        traverse(en_def, Avisited_en_definition, last_def);
                    }
                }
            }
            //           System.out.println("Root definitions list folows: ");
            
            SortByName();

//            File fd = null;
            try{
                fd = new File(arm_schema.toLowerCase()+".exp.add");
                System.setOut(new PrintStream(new FileOutputStream(fd)));
            }catch (FileNotFoundException nfe){
                return ;
            }

//            System.setOut(new PrintStream(new FileOutputStream("arm_entity.exp")));            
            generateArmAddon();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        transaction.endTransactionAccessCommit();
        session.closeSession();
        
        return ;
    }

    void generateArmAddon() throws SdaiException{
        System.out.println("");
        System.out.println("TYPE undefined_object_string = STRING;");
        System.out.println("END_TYPE;");
        System.out.println("");
        System.out.println("ENTITY undefined_object_entity;");
        System.out.println("END_ENTITY;");
        System.out.println("");
        System.out.println("TYPE undefined_object = SELECT (");
        System.out.println("  undefined_object_string,");
        System.out.println("  undefined_object_entity");
        System.out.println("END_TYPE;");
        System.out.println("");
        System.out.println("TYPE arm_entity_select = SELECT(");

        int cnt = Aroot_en_definition.getMemberCount();
        for(int i=1; i< cnt; i++){
            System.out.println("  " +Aroot_en_definition.getByIndex(i).getName(null) +",");
        }
        System.out.println("  " +Aroot_en_definition.getByIndex(cnt).getName(null));

        System.out.println(");");
        System.out.println("END_TYPE;");
    }
    
    void generateUndefinedObjectStuff() throws SdaiException{
        System.out.println("TYPE undefined_object_string = STRING;");
        System.out.println("END_TYPE;");
        System.out.println();
        System.out.println("ENTITY undefined_object_entity;");
        System.out.println("END_ENTITY;");
        System.out.println();
        System.out.println("TYPE undefined_object = SELECT (");
        System.out.println("  undefined_object_string,");
        System.out.println("  undefined_object_entity");
        System.out.println(");");
        System.out.println("END_TYPE;");
        System.out.println();
        System.out.println("ENTITY undefined_object_link;");
        System.out.println("  arm_undefined_object : undefined_object;");
        System.out.println("  aim_entity : undefined_object_select;");
        System.out.println("END_ENTITY;");
        System.out.println("  ");
    }
    
    void generateDefinitionsSchemasReferences(String arm_schema) throws SdaiException{
        
        System.out.println("REFERENCE FROM " +arm_schema.toLowerCase() +" (arm_entity_select, undefined_object_entity);");
        Vector en_refs = new Vector();
        
        for(int i=1; i<=Aroot_en_definition.getMemberCount(); i++){
            //            System.out.println("  REFERENCE FROM " +Aroot_en_definition.getByIndex(i).getName(null));
            en_refs.addElement(Aroot_en_definition.getByIndex(i).findEntityInstanceSdaiModel().getName().toLowerCase() +"("+Aroot_en_definition.getByIndex(i).getName(null) +")" );
        }
        
        en_refs = SortBySdaiModel(en_refs);
        String last_sdaimodel = null;
        
        for(int i=0; i<en_refs.size(); i++){
            int sub_ind = en_refs.elementAt(i).toString().indexOf("_dictionary_data");
            if (en_refs.elementAt(i).toString().substring(0, sub_ind).equals(last_sdaimodel)){
                last_sdaimodel = en_refs.elementAt(i).toString().substring(0, sub_ind);
                int ent_sind = en_refs.elementAt(i).toString().indexOf("(");
                int ent_eind = en_refs.elementAt(i).toString().indexOf(")");
                System.out.println(",");
                System.out.print("    " +en_refs.elementAt(i).toString().substring(ent_sind+1,ent_eind) );
            }else{
                if(i>0) System.out.println(");"); // finish previous reference
                System.out.println();
                last_sdaimodel = en_refs.elementAt(i).toString().substring(0, sub_ind);
                int ent_sind = en_refs.elementAt(i).toString().indexOf("(");
                int ent_eind = en_refs.elementAt(i).toString().indexOf(")");
                System.out.println("REFERENCE FROM " +last_sdaimodel +" (" );
                System.out.print("    " +en_refs.elementAt(i).toString().substring(ent_sind+1, ent_eind));
            }
        }
        
        if(en_refs.size() > 0){
            System.out.println(");"); // finish previous reference
            System.out.println();
        }
        return ;
    }
    
    void traverse(EEntity_definition en_def, AEntity_definition Avisited_en_definition, EEntity_definition last_def) throws SdaiException{
        //        System.out.println("...");
        AEntity_definition tmp_Adefs = en_def.getSupertypes(null);
        AEntity_definition Aen_def = new AEntity_definition();
        for(int i=1; i<=tmp_Adefs.getMemberCount();i++) Aen_def.addByIndex(i, tmp_Adefs.getByIndex(i));
        
        boolean ret = false;
        if(Aen_def.getMemberCount() == 0) {
            if(Includes(en_def)){
                if(en_def.getName(null).equals("time_role")) System.out.println("WIRED INSTANCE: please check number from Ciklas: ");
                //                System.out.println("REFERENCE FROM " +en_def.findEntityInstanceSdaiModel().getName().toLowerCase()  +"("+en_def.getName(null) +")" );
                if(!isAddedToRootArray(en_def)) Aroot_en_definition.addUnordered(en_def);
                //                    addVisitedSuperTypes(en_def);
            }else{ // if most root node is not among Aen_desc, then last_def is root
                //                    System.out.println("REFERENCE FROM " +en_def.findEntityInstanceSdaiModel().getName().toLowerCase() +"("+en_def.getName(null) +")" );
                if(!isAddedToRootArray(en_def)) Aroot_en_definition.addUnordered(en_def);
                //                    System.out.println("!!!");
                //                    System.out.println("!!!");
            }
            
            ret = true; // this is the "root" instance - return
        }
        
        if(ret) return ;
        
        if(!ret){
            Aen_def = Includes(Aen_def);
            for(int i=1; i <= Aen_def.getMemberCount(); i++){
                if((Aen_def.getByIndex(i)!=null) && !Aen_def.getByIndex(i).equals((EEntity_definition) en_def)){
                    //                    System.out.println("Start traversing SuperTypes: " +Aen_def.getByIndex(i).getName(null));
                    if(!VisitedSuperTypes(Aen_def.getByIndex(i))){
                        addVisitedSuperTypes(en_def);
                        traverse(Aen_def.getByIndex(i), Avisited_en_definition, en_def);
                        //                        System.out.println("Stoped traversing SuperTypes: " +Aen_def.getByIndex(i).getName(null));
                    }else{
                        //                        System.out.println("Skiping SuperType: " +Aen_def.getByIndex(i).getName(null) +" - already visited: skipping childs");
                        return ;
                    }
                }
            }
        }
        
        Aen_def  =new AEntity_definition();
        CEntity_definition.usedinSupertypes(null, en_def, null, Aen_def);
        Aen_def = Includes(Aen_def);
        Avisited_en_definition.addUnordered(en_def);
        for(int i=1; i <= Aen_def.getMemberCount(); i++){
            if((Aen_def.getByIndex(i) != null) && !Visited(Aen_def.getByIndex(i), Avisited_en_definition)){
                //                System.out.println("  current: " +(en_def.getName(null)) +"   next: " +Aen_def.getByIndex(i).getName(null));
                //                System.out.println("Start traversing child: " +Aen_def.getByIndex(i).getName(null));
                Avisited_en_definition.addUnordered(Aen_def.getByIndex(i));
                traverse(Aen_def.getByIndex(i), Avisited_en_definition, last_def);
                //                System.out.println("Stoped traversing child: " +Aen_def.getByIndex(i).getName(null));
            }
        }
        for(int i=1; i<=Avisited_en_definition.getMemberCount(); i++) Avisited_en_definition.removeByIndex(i);
        
        for(int i=1; i <= Aen_def.getMemberCount(); i++){
            if(Aen_def.getByIndex(i).equals((EEntity_definition) en_def)){
                Aen_decl.removeByIndex(i);
            }
        }
        return ;
    }
    
    boolean Visited(EEntity_definition en_def, AEntity_definition Avisited_en_def) throws SdaiException{
        //        System.out.println("Visited childs:" +Avisited_en_def.getMemberCount());
        for(int i=1; i<= Avisited_en_def.getMemberCount(); i++){
            if(en_def.equals(Avisited_en_def.getByIndex(i))) return true;
        }
        return false;
    }
    
    boolean isAddedToRootArray(EEntity_definition en_def) throws SdaiException{
        for(int i=1; i<=Aroot_en_definition.getMemberCount(); i++){
            if(Aroot_en_definition.getByIndex(i).equals(en_def)){
                return true;
            }
        }
        return false;
    }
    
    void SortByName() throws SdaiException{
        EEntity_definition temp_ed;
        int cnt = Aroot_en_definition.getMemberCount();
        for(int i=1; i<= cnt; i++){
            for(int k=i+1; k<=cnt; k++){
                if(Aroot_en_definition.getByIndex(i).getName(null).compareTo(Aroot_en_definition.getByIndex(k).getName(null)) >0 ){
                    temp_ed = Aroot_en_definition.getByIndex(i);
                    Aroot_en_definition.setByIndex(i, Aroot_en_definition.getByIndex(k));
                    Aroot_en_definition.setByIndex(k, temp_ed);
                }
            }
        }
    }
    
    Vector SortBySdaiModel(Vector en_defs) throws SdaiException{
        String temp_def = new String();
        int cnt = en_defs.size();
        for(int i=0; i< cnt-1; i++){
            for(int k=i+1; k< cnt; k++){
                if(en_defs.elementAt(i).toString().compareTo(en_defs.elementAt(k).toString()) >0 ){
                    temp_def = en_defs.elementAt(i).toString();
                    en_defs.setElementAt(en_defs.elementAt(k), i);
                    en_defs.setElementAt(temp_def, k);
                }
            }
        }
        return en_defs;
    }
    
    /* mark which Aen_def members are not "included" by global Aen_decl */
    AEntity_definition Includes(AEntity_definition Aen_defin) throws SdaiException{
        boolean found;
        for(int k=1; k<=Aen_defin.getMemberCount(); k++){
            found = false;
            if(Aen_defin.getByIndex(k) != null){
                for(int i=1; i<=Aen_decl.getMemberCount(); i++){
                    if((Aen_decl.getByIndex(i) != null) && Aen_decl.getByIndex(i).getDefinition(null).equals(Aen_defin.getByIndex(k))){
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                Aen_defin.removeByIndex(k);
            }
        }
        
        return Aen_defin;
    }
    
    /* mark which Aen_def members are not "included" by global Aen_decl */
    boolean Includes(EEntity_definition en_def) throws SdaiException{
        boolean found;
        found = false;
        for(int i=1; i<=Aen_decl.getMemberCount(); i++){
            if((en_def != null) && en_def.equals(Aen_decl.getByIndex(i).getDefinition(null))){
                found = true;
                break;
            }
        }
        if(!found) return false;
        
        return true;
    }
    
    void addVisitedSuperTypes(EEntity_definition en_def)throws SdaiException{
        Avisited_supertypes.addUnordered(en_def);
    }
    
    boolean VisitedSuperTypes(EEntity_definition en_def) throws SdaiException{
        for(int i=1; i<Avisited_supertypes.getMemberCount(); i++){
            if(en_def.equals((EEntity_definition)Avisited_supertypes.getByIndex(i))) return true;
        }
        
        return false;
    }
    
}
