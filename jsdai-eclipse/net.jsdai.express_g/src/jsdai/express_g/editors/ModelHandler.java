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

package jsdai.express_g.editors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.util.ListenerList;

import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExpress_g_schema.EProperty;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.express_g.SdaieditPlugin;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class ModelHandler {
	public static final String DICTIONARY_MODEL_NAME = "SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA";
	
	private RepositoryHandler repositoryHandler;
	
	private String name;
	private SdaiModel model;
//	private SdaiModel schema_model;
	private Properties properties;
	private ESchema_definition schema_definition = null;
	private EGraphics_diagram diagram_definition = null;
	
	private ListenerList roChangers = new ListenerList();
	private ListenerList rwChangers = new ListenerList();

//	public int NO_ACCESS = SdaiModel.NO_ACCESS;
//	public int READ_ONLY = SdaiModel.READ_ONLY;
//	public int READ_WRITE = SdaiModel.READ_WRITE;
	
//	private int access = NO_ACCESS;
	private boolean rwModel;
	
	/**
	 * 
	 */
	public ModelHandler(RepositoryHandler repositoryHandler, SdaiModel model) throws SdaiException {
		this.model = model;
		this.repositoryHandler = repositoryHandler;
//System.out.println("<<A>>Creating new ModelHandler - inside - going to init - model: " + model);
		init();
	}
	
//	public int getAccessType() {
//		return access;
//	}

	/**
	 * tryies to start RO access of this model
	 * @param changer
	 * @return
	 */
	public String startROAccess(IRepositoryChanger changer) {
		roChangers.add(changer);
		return null;
	}
	
	/**
	 * tryies to end RO access of this model
	 * @param changer
	 * @return
	 */
	public String endROAccess(IRepositoryChanger changer) {
		String ok = changer.askToEnd();
		if (ok == null) {
			roChangers.remove(changer);
		}
		return ok;
	}
	
	/**
	 * tryies to start RW access of this model
	 * @param changer
	 * @return
	 */
	public String startRWAccess(IRepositoryChanger changer) {
		String ok = null;
		if (!rwModel) try {
			openModel(model);
		} catch (SdaiException sex) { 
			SdaieditPlugin.log(sex);
			SdaieditPlugin.console(sex.toString());
		}
		if (rwModel) { 
			Object[] rwCh = rwChangers.getListeners();
			for (int i = 0; (i < rwCh.length)&&(ok == null); i++) {
				ok = endRWAccess((IRepositoryChanger)rwCh[i]);
			}
			if (ok == null) {
				rwChangers.add(changer);
			}
		} else {
			ok = "RW access on SdaiModel failed";
		}
		return ok;
	}
	
	/**
	 * tryies to end RW access of this model
	 * @param changer
	 * @return
	 */
	public String endRWAccess(IRepositoryChanger changer) {
		String ok = changer.askToEnd();
		if (ok == null) {
			rwChangers.remove(changer);
		}
		return ok;
	}
	
	public String endRWAccess(IRepositoryChanger changer, boolean force) {
		String ok = endRWAccess(changer);
		if ((force)&&(ok != null)) {
			changer.forceToEnd();
			ok = null;
			rwChangers.remove(changer);
		}
		return ok;
	}
	
	public void close() {
		Object[] list = roChangers.getListeners();
		for (int i = 0; i < list.length; i++) {
			IRepositoryChanger ch = (IRepositoryChanger)list[i];
			ch.forceToEnd();
		}
		list = rwChangers.getListeners();
		for (int i = 0; i < list.length; i++) {
			IRepositoryChanger ch = (IRepositoryChanger)list[i];
			ch.forceToEnd();
		}
		try {
			switch (model.getMode()) {
				case SdaiModel.READ_ONLY :
// DEBUG					
//System.out.print("END RO ACCESS FOR " + model);
					model.endReadOnlyAccess();
//System.out.println(" DONE.");					
					break;
				case SdaiModel.READ_WRITE :
// DEBUG					
//System.out.print("END RW ACCESS FOR " + model);
					model.endReadWriteAccess();
//System.out.println(" DONE.");					
					break;
			}
		} catch (SdaiException sex) {
			if (sex.getErrorId() == SdaiException.MO_NEXS) {
				// model already closed
			} else {
				SdaieditPlugin.log(sex);
				SdaieditPlugin.console(sex.toString());
			}
		}	
	}
	
	private void openModel(SdaiModel  model) throws SdaiException {
		rwModel = false;
		if (model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		}
		switch (model.getMode()) {
			case SdaiModel.NO_ACCESS :
//				System.err.println("NO ACCESS model:" + model);
				break;
			case SdaiModel.READ_ONLY :
				try {
					model.promoteSdaiModelToRW();
					rwModel = true;
				} catch (SdaiException sex) {
					SdaieditPlugin.log("RO ACCESS model:" + model, IStatus.WARNING);
					SdaieditPlugin.console("RO ACCESS model:" + model);
					rwModel = false;
				}
				break;
			case SdaiModel.READ_WRITE :
				rwModel = true;
				break;
		}
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	private void init() throws SdaiException {
		openModel(model);
		properties = new Properties();
	    if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXTENDED_DICTIONARY_SCHEMA")) {
	    	String modelName = model.getName();
        	if (!modelName.equalsIgnoreCase("SDAI_DICTIONARY_SCHEMA_DICTIONARY_DATA") &&
        		!modelName.equalsIgnoreCase("SDAI_MAPPING_SCHEMA_DICTIONARY_DATA") &&
				!modelName.equalsIgnoreCase("MAPPING_SCHEMA_DICTIONARY_DATA") &&
				!modelName.equalsIgnoreCase("EXTENDED_DICTIONARY_SCHEMA_DICTIONARY_DATA")) 
        	{ // start filtered
        		AEntity schemas = model.getInstances(ESchema_definition.class);
        		SdaiIterator schemas_it = schemas.createIterator();
        		if (schemas_it.next()) {
        			schema_definition = (ESchema_definition)schemas.getCurrentMemberEntity(schemas_it);
        		}
        	} // end filtering
		} else
    	if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
			AEntity diagrams = model.getInstances(EGraphics_diagram.class);
			SdaiIterator schemas_it = diagrams.createIterator();
			if (schemas_it.next()) {
				diagram_definition = (EGraphics_diagram)diagrams.getCurrentMemberEntity(schemas_it);
				if (diagram_definition.testDic_schema(null)) {
					schema_definition = diagram_definition.getDic_schema(null);
//System.out.println("<<>> getting dic_schema: " + schema_definition + ", diagram: " + diagram_definition);
				} else {
					schema_definition = null;
				}
	    	}
			// load properties
			AEntity props = model.getInstances(EProperty.class);
			SdaiIterator prop_it = props.createIterator();
			while (prop_it.next()) {
				EProperty prop = (EProperty)props.getCurrentMemberEntity(prop_it);
				if (prop.testName(null))
					properties.put(prop.getName(null), prop.testData(null) ? prop.getData(null) : "");
				else {
					String message = "EProperty " + prop + " not optional attribute missing in " + model;
					SdaieditPlugin.log(message, IStatus.ERROR);
				}
			}
		}
		updateName();
	}

	/**
	 * returns displayable name of model 
	 * @return name of diagram definition, schema definition or model
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns displayable name of model 
	 * @return name of diagram definition, schema definition or model
	 */
	private void updateName() {
		name = "undefined";
		try {
			if (diagram_definition != null) {
				name = diagram_definition.getComment(null);
			} else
			if (schema_definition != null) {
				name = schema_definition.getName(null);
			} else {
				name = model.getName();
			}
//System.out.println("<<A>> updating name: " + name);
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @throws SdaiException
	 */
	public boolean setName(String name) {
		boolean rez = false;
		try {
			if (diagram_definition != null) {
				diagram_definition.setComment(null, name);
				rez = true;
			} else
			if (schema_definition != null) {
				schema_definition.setName(null, name);
				rez = true;
			} else { 
//				System.err.println("Cannot set name of SdaiModel");
				rez = false;
			}
	 	} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			rez = false;
	 	}
	 	updateName();
		return rez;
	}
	
	/**
	 * don't use before getting access
	 * @return sdai model
	 */
	public SdaiModel getModel() {
		return model;
	}
	
	/**
	 * don't use before getting access
	 * @return schema definition
	 */
	public ESchema_definition getSchema_definition() {
		return schema_definition;
	}
	
	/**
	 * don't use before getting access
	 * @return diagram definition
	 */
	public EGraphics_diagram getDiagram_definition() {
		return diagram_definition;
	}
	
	/**
	 * recreates this model
	 * also creates schema definition or graphics definition
	 *
	 */
	public SdaiModel clearModel() throws SdaiException {
		AEntity entities = model.getInstances();
		SdaiIterator iter = entities.createIterator();
		HashSet toDelete = new HashSet(entities.getMemberCount());
		while (iter.next()) {
			EEntity entity = entities.getCurrentMemberEntity(iter);
			if ((entity != diagram_definition)&&(entity != schema_definition)) {
				toDelete.add(entity);
			} else {
			}
		}

		Iterator it = toDelete.iterator();
		while (it.hasNext()) {
			((EEntity)it.next()).deleteApplicationInstance();
		}
		return model;
	}
	
	public void setSchema_definition(ESchema_definition definition) throws SdaiException {
		schema_definition = definition;
		diagram_definition.setDic_schema(null, schema_definition);
	}
	
	public void setDiagram_definition(EGraphics_diagram definition) throws SdaiException {
		diagram_definition = definition;
		diagram_definition.setDic_schema(null, schema_definition);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " " + super.toString();
/*		String text = getName() + " (RO:"; 
		Object[] listeners = roChangers.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			text += " " + listeners[i];
		}
		text += "; RW:";
		listeners = rwChangers.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			text += " " + listeners[i];
		}
		text += ")";
		return text;*/
	}
	
	/**
	 * @return Returns the repositoryHandler.
	 */
	public RepositoryHandler getRepositoryHandler() {
		return repositoryHandler;
	}
}
