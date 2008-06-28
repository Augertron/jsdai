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
import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

import java.util.zip.Checksum;
import java.util.zip.CheckedInputStream;
import java.util.zip.CRC32;


public class RepositoryChanges extends JsdaiLangAccessor{

    //static ASdaiModel models_old = new ASdaiModel();
    //static ASchemaInstance schemas_old = new ASchemaInstance();
    static HashMap models_old = new HashMap(1024);
    static HashMap schemas_old = new HashMap(1024);
    static HashMap models_new = new HashMap(1024);
    
    private static final boolean flag_debug = false;


    private static void printDebug(String msg) {
        if (flag_debug) {
            System.out.println("EC DEBUG> RC> " + msg);
        }
    }
    
    
    static public void rememberRepositoryState(SdaiRepository repository) throws SdaiException {
        
        printDebug("repo="+repository.getLocation()+File.separator+"repository");
        ASchemaInstance schemas = repository.getSchemas();
        ASdaiModel models = repository.getModels();
        
        printDebug("Repository M count    : " + models.getMemberCount());
        //-- saving models from repository into model_old 
        SdaiIterator i_mod = models.createIterator();
        while (i_mod.next()) {
            SdaiModel model = models.getCurrentMember( i_mod);
            printDebug("\t"+model.getName().toUpperCase());
            models_old.put(model.getName().toUpperCase(),model);
        }
        //-- adding reserved models
        models_old.put(SdaiSession.DICT_MODEL_NAME,null);
        models_old.put(SdaiSession.MAPP_MODEL_NAME,null);
        models_old.put(SdaiSession.COMP_MODEL_NAME,null);
        
        printDebug("Repository SI count    : " + schemas.getMemberCount());
        //-- saving schema instances from repository into schemas_old
        SdaiIterator i_sch = schemas.createIterator();
        while (i_sch.next()) {
            SchemaInstance schema = schemas.getCurrentMember( i_sch);
            printDebug("\t"+schema.getName().toLowerCase());
            schemas_old.put(schema.getName().toLowerCase(),schema);
        }
        
        schemas_old.put(SdaiSession.DICT_SCHEMA_NAME,null);
        schemas_old.put(SdaiSession.MAPP_SCHEMA_NAME,null);
    }

    
    static public void trackRepositoryChanges(SdaiRepository repository, String indexFileName) throws SdaiException {
        String str, prop_key;
        String model_list;
        Properties index = new Properties();
        
        //-- if index file with the same name already exists, index information is removed from saved repository state
        File f = new File(indexFileName);
        if (f == null) {
			throw new SdaiException(SdaiException.SY_ERR, "Index file name is not correct");
		}
        if (f.getParent() == null) {
            f = new File("jsdai",indexFileName+".properties");
        }
        if (f.exists()) {
            printDebug( "f exists="+f.exists());
            
            InputStream istr;
            try {
                istr = new BufferedInputStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                throw new SdaiException(SdaiException.SY_ERR, e);
            }
            try {
                Properties initial_index = new Properties();
                try {
                    initial_index.load(istr);
                    istr.close();
                } catch (IOException e) {
                    throw new SdaiException(SdaiException.SY_ERR, e);
                }
                try {
                    //-- removes models and schema instances from saved repository state
                    for (Enumeration e = initial_index.propertyNames(); e.hasMoreElements();) {
                        prop_key = e.nextElement().toString();
                        if (prop_key.startsWith("DS.") || prop_key.startsWith("MS.")) {
                            str = prop_key.substring(3);
                            schemas_old.remove(str);
                        }
                        if (prop_key.startsWith("DM.") || prop_key.startsWith("MM.")) {
                            str = prop_key.substring(3);
                            models_old.remove(str);
                        }
                    }
                    
                } finally {
                    istr.close();
                }
            } catch (IOException e) {
                throw new SdaiException(SdaiException.SY_ERR, e);
            }
        }
        
        ASdaiModel models = repository.getModels();
        ASchemaInstance schemas = repository.getSchemas();
        
        //-- saving value of max persistent label -- commented - 030516 --
        //printDebug("max_persistent_label  : " + repository.getPersistentLabel());
        //index.setProperty("max_persistent_label", Long.toString(repository.getPersistentLabel()));
        printDebug("Repository M count    : " + models.getMemberCount());
        
        String schema_name, native_schema_name, model_name, model_version;
        int models_count = 0;
        SdaiIterator i_mod = models.createIterator();
        //-- analysing models
        while (i_mod.next()) {
            SdaiModel model = models.getCurrentMember( i_mod);
            model_name = model.getName().toUpperCase();
            if (!models_old.containsKey(model_name)) {  //-- if model was created recently
                printDebug("\t"+model_name);
                
                if (model.getMode() == SdaiModel.NO_ACCESS) {
                    model.startReadOnlyAccess();
                }
                //-- getting identification from schema definition as model version
                if (model_name.endsWith("_DICTIONARY_DATA") || model_name.endsWith("_MAPPING_DATA")) {
                	model_version = getModelVersionString(model);
                    if (model_name.endsWith("_DICTIONARY_DATA")) {  //--  dictionary model
                        index.setProperty("DM."+model_name, model_version);
                    }
                    if (model_name.endsWith("_MAPPING_DATA")) {     //--  mapping model
                        index.setProperty("MM."+model_name, model_version);
                    }
                    models_count++;
                    models_new.put(model_name,model);
                }
            }
        }
        // index.setProperty("models_count", Integer.toString(models_count)); -- commented -- 030516 -- 
        
        printDebug("Repository SI count    : " + schemas.getMemberCount());
        
        int schemas_count = 0;
        SdaiIterator i_sch = schemas.createIterator();
        //-- analysing schema instances
        while (i_sch.next()) {
            SchemaInstance schema = schemas.getCurrentMember( i_sch);
            schema_name = schema.getName().toLowerCase();
            native_schema_name = schema.getNativeSchemaString();
            if (!schemas_old.containsKey(schema_name)) {    // if schema instance was created recently
                printDebug("\t"+schema_name);
                model_list = "";
                ASdaiModel amodels = schema.getAssociatedModels();
                SdaiIterator i_amod = amodels.createIterator();
                while (i_amod.next()) {
                    SdaiModel amodel = amodels.getCurrentMember( i_amod);
                    model_name = amodel.getName().toUpperCase();
                    printDebug("\t\t"+model_name);
                    if (model_list.length()>0) {
                        model_list += " ";
                    }
                    model_list += model_name;
                    if (models_old.containsKey(model_name)) {   // if associated model is from models_old list
                        model_version = "$";
                        if (model_name.endsWith("_DICTIONARY_DATA") || model_name.endsWith("_MAPPING_DATA")) {  //--  dictionary or mapping model
                        	model_version = getModelVersionString(amodel);
                        }
                        index.setProperty("IM."+model_name, model_version);
                    }
                    else if (!models_new.containsKey(model_name)) { // associated model is not found in models list
                        throw new SdaiException(SdaiException.MO_NEXS);
                    }
                }
                if (native_schema_name.endsWith("_DICTIONARY_SCHEMA")) {
                    index.setProperty("DS."+schema_name, model_list);
                }
                if (native_schema_name.endsWith("_MAPPING_SCHEMA")) {
                    index.setProperty("MS."+schema_name, model_list);
                }
                schemas_count++;
            }
        }
        // index.setProperty("schema_instances_count", String.valueOf(schemas_count)); -- commented -- 030516 --
        
        index.setProperty("JSDAIVersion", Implementation.major_version+"."+Implementation.middle_version+"."+Implementation.minor_version);
        
        
        //-- writing to properties file recent changes
		OutputStream ostr;
		try {
			ostr = new BufferedOutputStream(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
		try {
			index.store(ostr, "Repository index file for JSDAI, LKSoftWare GmbH");
			ostr.close();
		} catch (IOException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
        
    }


	private static String getModelVersionString(SdaiModel model) throws SdaiException {
		try {
		    InputStream is = model.getLocationURL().openStream();
			try {
				CheckedInputStream chis = new CheckedInputStream(is, new CRC32());
				BufferedInputStream buis = new BufferedInputStream(chis);
				while (buis.read() != -1) {
					// Reading all file
				}
			    return String.valueOf(chis.getChecksum().getValue());
			} finally {
				is.close();
			}
		} catch (IOException e) {
		    throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

}
