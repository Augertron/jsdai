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

package jsdai.express_g.exp2.ui.command;

import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.exp2.*;
import jsdai.express_g.exp2.eg.*;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.lang.*;
import jsdai.SExpress_g_schema.*;


/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public abstract class GenJSDAICommand extends AbstractCommand {
  public GenJSDAICommand(CommandInvoker invoker) {
    super(invoker);
	
  }

  /**
   * updateModel
   *
   * @param model SdaiModel
   */
  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
/*    EGraphics_diagram schema = (EGraphics_diagram)modelEG.createEntityInstance(EGraphics_diagram.class);
    schema.setComment(null, prop.getNameEG());
    if (modelDict != null) {
      ESchema_definition schemaDef = prop.getRepositoryHandler().getModelHandler(prop.getName()) 
      schemaDef.setName(null, prop.getName());
    }
    schema.setDic_schema(null, prop.getSchemaDef());*/
  	
    Iterator iter = prop.handler().drawableIterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof SDAIdicSchema) {
        if ((item instanceof EGPage) || (item instanceof VisualPage)) 
          ((SDAIdicSchema)item).clearDefinitions();
        else
          ((SDAIdicSchema)item).setDefinitionPlacement(null);
      }
    }
/*    for (int i = 0; i <= prop.handler().getMaxPage(); i++) { // PAGES INCLUDED IN DRAWABLE
    	VisualPage vp = prop.handler().getVisualPage(i);
    	vp.clearDefinitions();
    	vp.getDefinition(modelDict, modelEG);
    }*/
    iter = prop.handler().drawableRevIterator();

//    VisualPage vpINV = new VisualPage(Paging.INVISIBLE_PAGE);
    while (iter.hasNext()) {
      Object item = iter.next();
      if ((item instanceof SDAIdicSchema) && !(item instanceof VisualPage)) {
        ((SDAIdicSchema)item).getDefinition(modelDict, modelEG);
        int pgNr = ((Paging)item).getPage();
        if (pgNr >= 0) {
          VisualPage vp = prop.handler().getVisualPage(pgNr);
          EEntity placeDef = ((SDAIdicSchema)item).getDefinitionPlacement(modelDict, modelEG);
          EPage pg = (EPage)vp.getDefinition(modelDict, modelEG);
    	  if (placeDef != null && pgNr > 0) {
    		  SdaiModel mod = null; 
    		  try {
    			  mod = pg.findEntityInstanceSdaiModel();
    		  } catch (SdaiException sex) {
    			  SdaieditPlugin.log(sex);
    		  }
    		  if ((pg == null) || (mod != modelEG)) {
    			  String msg = "NO ENTITY FOR PAGE (" + vp.getPage() + "):" + vp;
    			  SdaieditPlugin.log(msg, IStatus.ERROR);
    			  SdaieditPlugin.console(msg);
    		  } else ((EObject_placement)placeDef).setPresented_on(null, pg);
    	  }
        }
/*        else if (pgNr == Paging.INVISIBLE_PAGE) {
          ( (EObject_placement) ( (SDAIdicSchema) item).getDefinitionPlacement(
              modelDict, modelEG)).setPresented_on(null,
              (EPage) vpINV.getDefinition(modelDict, modelEG));
        }*/
      }
    }
    
    // -----------------------------------------------------------------------------------------------
    // set properties
    // ------------------
    
    EProperty p = (EProperty)modelEG.createEntityInstance(EProperty.class); // Entity font
    p.setName(null, PropertySharing.PROP_FONT1); 
    p.setData(null, prop.getFont1().getFontData()[0].toString());
    p = (EProperty)modelEG.createEntityInstance(EProperty.class); // Attribute font
    p.setName(null, PropertySharing.PROP_FONT2);
    p.setData(null, prop.getFont2().getFontData()[0].toString());
    p = (EProperty)modelEG.createEntityInstance(EProperty.class); // Editing mode
    p.setName(null, PropertySharing.PROP_EDITING_MODE);
    p.setData(null, String.valueOf(prop.getEditMode()));
    if (prop.isPagesSameSize()) {
    	p = (EProperty)modelEG.createEntityInstance(EProperty.class); // pages of same size
    	p.setName(null, PropertySharing.PROP_PAGES_OF_SAME_SIZE);
    	// no data for this property
    }
    int pg_ren = prop.getPageRenumber();
    if (pg_ren > 0) {
        p = (EProperty)modelEG.createEntityInstance(EProperty.class); // Editing mode
        p.setName(null, PropertySharing.PROP_PAGE_RENUMBER);
        p.setData(null, String.valueOf(pg_ren));
    }
    
    iter = prop.properties().keySet().iterator();
    while (iter.hasNext()) {
    	Object key = iter.next();
    	Object val = prop.properties().get(key);
        p = (EProperty)modelEG.createEntityInstance(EProperty.class);
        p.setName(null, key.toString());
        p.setData(null, val.toString());
    }
  }

}
