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

import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.GenJSDAICommand;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class SaveRepository extends GenJSDAICommand {
	
	/**
	 * @param invoker
	 */
	public SaveRepository(CommandInvoker invoker) {
		super(invoker);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.ui.command.Command#start()
	 */
	public void start() {
	    if (prop.getSaveAllowed() == null) try {
	    	prop.status().setStatus(getStatus());
	    	RepositoryHandler handler = prop.getRepositoryHandler();
	    	ModelHandler mhDict = handler.getModelHandler(prop.getName());
	    	ModelHandler mhEG = handler.getModelHandler(prop.getNameEG());
	    	SdaiModel modelDict = null;
	    	SdaiModel modelEG;

   	    	String question = null;
   	    	if ((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0) {
   		    	modelDict = mhDict.getModel();
     		}
	    	modelEG = mhEG.clearModel();
	    	if (mhDict != null)
	    		mhEG.setSchema_definition(mhDict.getSchema_definition());

//System.err.println(modelDict + " - " + modelEG);	    	
		    updateModel(modelDict, modelEG);
	    	
	    } catch (SdaiException sex) {
	    	prop.status().log(sex);
	    }
	      
	    prop.handler().commandDone();
	}

	public String getStatus() {
	    return "Saving schema " + prop.getNameEG();
	}
}
