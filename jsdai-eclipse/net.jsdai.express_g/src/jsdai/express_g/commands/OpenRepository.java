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

import java.util.Iterator;

import org.eclipse.swt.graphics.GC;

import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.OpenJSDAICommand;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 * 
 * Opens a repository
 *
 */
public class OpenRepository extends OpenJSDAICommand {
	/**
	 * @param invoker
	 */
	public OpenRepository(CommandInvoker invoker) {
		super(invoker);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.ui.command.Command#start()
	 */
	public void start() {
	    if (prop.getOpenAllowed() == null) try {
	        prop.status().setStatus(getStatus());
//	        prop.setEditMode(PropertySharing.MODE_LAYOUT_FULL);
	        prop.handler().drawableClear();
	        prop.handler().setPage(1);
	        prop.handler().setMaxPage(0);
	        prop.handler().setMaxPage(1);
	        prop.resetEditModeChanged();
	          
	    	RepositoryHandler handler = prop.getRepositoryHandler();
	    	ModelHandler mhDict = handler.getModelHandler(prop.getName());
	    	ModelHandler mhEG = handler.getModelHandler(prop.getNameEG());
	    	if (mhDict == null) {
	    		prop.setEditMode(PropertySharing.MODE_SCHEMA_MASK | prop.getEditMode());
	    		// open Schema level diagram
    	    	SdaiModel modelEG = mhEG.getModel();
//    	        System.out.println("LAYOUT:     " + modelEG);
	    		String[] schemas = handler.getSchemas();
    	        prop.setMultiUpdate(true);
	    		for (int i = 0; i < schemas.length; i++) {
	    			mhDict = handler.getModelHandler(schemas[i]);
		    		if (mhDict != null) {
		    	    	SdaiModel modelDict = mhDict.getModel();
		    	        createEGPopulationSchema1(modelDict);
		    		}
	    		}
    	        createEGPopulationSchema2(modelEG);
    	        prop.setMultiUpdate(false);
	    	} else {
	    		// open standart Entity level diagram
	        	SdaiModel modelDict = mhDict.getModel();
	        	SdaiModel modelEG = mhEG.getModel();
	        	// looking for express schema model
	        	String nameExp = EGToolKit.resolveExpressSchemaName(modelDict.getName());
        		ModelHandler mhExp = handler.getModelHandler(nameExp);
	        	SdaiModel modelExp = mhExp == null ? null : mhExp.getModel();
	        	
//	            System.out.println("DICTIONARY: " + modelDict);
//	            System.out.println("EXPRESS:    " + modelExp);
//	            System.out.println("LAYOUT:     " + modelEG);
	            prop.setMultiUpdate(true);
	            createEGPopulation(modelDict, modelEG, modelExp);
	            prop.setMultiUpdate(false);
	    	}	    	
//	        System.out.println("data loaded");

//            System.out.print("updating...");
            GC g = prop.getPainting().getLastGraphics();
            Iterator itd = prop.handler().drawableIterator();
            while (itd.hasNext()) {
            	Object item = itd.next();
	            if ((item instanceof AbstractEGBox)&&(!((AbstractEGBox)item).isOnPage(Paging.INVISIBLE_PAGE))) 
	            	((Drawable)item).draw(g);
	          	}
            prop.handler().update();
//            System.out.println("done");
	    } catch (SdaiException sex) {
	    	prop.status().log(sex);
	    }
	    prop.handler().commandDone();
	}
	
	  public String getStatus() {
	    return "Opening schema " + prop.getNameEG();
	  }
}
