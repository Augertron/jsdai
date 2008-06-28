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

package jsdai.express_g.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGSchema;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.OpenJSDAICommand;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class OpenReferingSchema extends OpenJSDAICommand {
	private Collection sdaiList = null;
	
	public OpenReferingSchema(CommandInvoker invoker) {
		super(invoker);
	}

	public void setSDAIObjects(Collection list) {
		sdaiList = list;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.command.Command#start()
	 */
	public void start() {
		if (sdaiList != null) {
			// resolving main schema object, needed for interschema relations
			HashSet schemas = new HashSet();
			Iterator dit = prop.handler().drawableIterator();
			while (dit.hasNext()) {
				Object obj = dit.next();
				if (obj instanceof EGSchema) {
					try {
						schemas.add(((EGSchema)obj).getDefinition().findEntityInstanceSdaiModel());
						if (((EGSchema)obj).getName().equalsIgnoreCase(prop.getName())) 
							mainSchema = (EGSchema)obj;
					} catch (SdaiException sex) {
				    	prop.status().log(sex);
					}
				}
			}

		    Iterator iter = sdaiList.iterator();
			while (iter.hasNext()) {
				Object object = iter.next();
				if (object instanceof EEntity) {
					try {
						// FIX
						if (!schemas.contains(((EEntity)object).findEntityInstanceSdaiModel())) {
							EGEntityRef ref = createEntityRef((EEntity)object);
						}
					} catch (SdaiException sex) {
				    	prop.status().log(sex);
					}
				}
			}
		}
	    
	    cleanTemp();
	    prop.handler().commandDone();
	}

  	protected EGEntityRef createEntityRef(EEntity entity) throws SdaiException {
  		AbstractEGBox box = createEG(entity);
  		EGEntityRef ref = null;
   		if (box == null) {
//  	    			System.err.println("EInterfaced_declaration (" + declaration + ") contains not supported entity: " + entity);  	    			
   		} else {
            String schema_name = "unknown_schema";
    		SdaiModel modelEnt = entity.findEntityInstanceSdaiModel();
  	  	    AEntity list = modelEnt.getInstances(ESchema_definition.class);
            SdaiIterator it = list.createIterator();
            EGSchema refSchema = null;
            if (it.next()) {
            	ESchema_definition schDef = (ESchema_definition)list.getCurrentMemberEntity(it);
            	schema_name = schDef.getName(null);
            	refSchema = createEG((ESchema_definition)schDef);
            }
            int type = EGEntityRef.TYPE_USED;
    	    box.setDefinition(entity);
    	    // creates interschema relations
          	createSchemaRelation(refSchema, type);

          	ref = new EGEntityRef(prop, box, schema_name, type);
  	  	            
            prop.handler().drawableRemove(box);
            prop.handler().drawableAddR(ref);
  	    }  	
  	    return ref;
  	}
}
