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

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.StaticTools;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.lang.ASdaiModel;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

/**
 * Creates or links to Express Dictionary repository and holds it open
 * all model modifications must go through this class
 * 
 * @author Mantas Balnys 
 *
 * Changes system property: new.repository.format = SDAI
 */
public class RepositoryHandler {
	private IPath path;
	
	private SdaiSession session;
	private SdaiTransaction transaction;
	private SdaiRepository repository;
	
	private String[][] names; 
	private Hashtable models = new Hashtable();
	
	public RepositoryHandler(IPath repository) throws SdaiException {
		path = repository;
		init();
	}
	
	public IPath getRepoPath() {
		return path;
	}
	
	public void init() throws SdaiException {
		SdaiSession.setLogWriter(new PrintWriter(System.out));
//		System.setProperty("repositories", ppath.concat("\\Internal"));
		if (session == null) {
			if (SdaiSession.getSession() == null) {
//System.out.println("session null");				
				Properties prop = new Properties();
				File repoDir = SdaieditPlugin.getDefault().getStateLocation().append("sdairepos").toFile();
				if (!repoDir.exists()) repoDir.mkdirs();
				prop.setProperty("repositories", repoDir.getAbsolutePath());
				SdaiSession.setSessionProperties(prop); 
/*
				File repoDir = SdaieditPlugin.getDefault().getStateLocation().toFile();
				System.setProperty("jsdai.properties", repoDir.getAbsolutePath()); // FIX temporary use prop */
//				if (!repoDir.exists()) repoDir.mkdirs();
//				JSDAIProperties.createJSDAIProperties(ppath);
			}
			session = SdaiSession.openSession();
		}
			
//		session = SdaieditPlugin.getDefault().getSdaiSession();
		String osPath = path.toOSString();
		if (session.testActiveTransaction()) {
			transaction = session.getActiveTransaction();
		} else {
			transaction = session.startTransactionReadWriteAccess();
		}
		
		File file = new File(osPath);

		if (!file.exists()) {
			String emptyPath = RuntimePlugin.getFragmentResourcePath(Platform.getBundle("net.jsdai.ext_dict_lib"), "ExpressCompilerRepo.sdai");
//System.out.println(emptyPath);			
			EGToolKit.copyFile(emptyPath, osPath);
		}
		ASdaiRepository arep = session.getActiveServers();
		SdaiIterator repi = arep.createIterator();
		repository = null;
		while ((repi.next())&&(repository == null)) {
			SdaiRepository rep = arep.getCurrentMember(repi);
			String loc = rep.getLocation();
			if ((loc != null)&&(loc.equals(osPath))) {
				repository = rep;
			}
		}

		if (repository == null)	repository = session.linkRepository("", osPath);

		if (!repository.isActive()) repository.openRepository();
		update();
	}
	
	public String startROChanger(IRepositoryChanger changer) {
		String[] chm = changer.getModels();
		String msg = null;
		for (int i = 0; (i < chm.length)&&(msg == null); i++) {
			ModelHandler mh = (ModelHandler)models.get(chm[i]);
			if (mh == null) {
				msg = "Model not found:" + chm[i];
			} else {
				msg = mh.startROAccess(changer);
			}
		}
		if (msg != null) endROChanger(changer);
		return msg;
	}
	
	public void endROChanger(IRepositoryChanger changer) {
		String[] chm = changer.getModels();
		for (int i = 0; i < chm.length; i++) {
			ModelHandler mh = (ModelHandler)models.get(chm[i]);
			if (mh != null) mh.endROAccess(changer);
		}
	}

	public String startRWChanger(IRepositoryChanger changer) {
		String[] chm = changer.getModels();
		String msg = null;
		for (int i = 0; (i < chm.length)&&(msg == null); i++) {
			ModelHandler mh = (ModelHandler)models.get(chm[i]);
			if (mh == null) {
				msg = "Model not found:" + chm[i];
			} else {
				msg = mh.startRWAccess(changer);
			}
		}
		if (msg != null) endRWChanger(changer);
		return msg;
	}
	
	public void endRWChanger(IRepositoryChanger changer) {
		String[] chm = changer.getModels();
		for (int i = 0; i < chm.length; i++) {
			ModelHandler mh = (ModelHandler)models.get(chm[i]);
			if (mh != null) {
				mh.endRWAccess(changer);
			}
		}
	}

	/**
	 * reloads data from repository
	 * call after adding or removing model
	 *
	 */
	public void update() throws SdaiException {
        ASdaiModel mods = repository.getModels();
        SdaiIterator it_models = mods.createIterator();
        HashMap schemas = new HashMap();
        Hashtable models2 = new Hashtable(mods.getMemberCount());
        while (it_models.next()) {
        	SdaiModel model = mods.getCurrentMember(it_models);
        	ModelHandler mh = new ModelHandler(this, model);
        	String name = mh.getName();
        	ModelHandler mh1 = (ModelHandler)models.get(name);
        	if (mh1 != null) mh = mh1;
        	models2.put(name, mh);
        	
        	EGraphics_diagram diagram = mh.getDiagram_definition();
        	ESchema_definition schema = mh.getSchema_definition();
        	if (diagram != null) { // Diagram model
        		String schemaName = null;
        		if (schema != null) 
            		schemaName = schema.getName(null);
           		Vector dv;
           		if (schemas.containsKey(schemaName)) {
           			dv = (Vector)schemas.get(schemaName);
           		} else {
           			dv = new Vector();
           			schemas.put(schemaName, dv);
           		}
           		dv.add(name);
        	} else
        	if (schema != null) { // Schema model
        		if (!schemas.containsKey(name))
        			schemas.put(name, new Vector());
        	} else { // Unknown model
//        		System.err.println("Unknown model format: " + model);
        	}
        }
        models = models2;
        names = new String[schemas.size()][];
    	Iterator iter = schemas.entrySet().iterator();
    	int i = 0;
    	while ((iter.hasNext())&&(i < names.length)) {
    		Map.Entry entry = (Map.Entry)iter.next();
    		String key = (String)entry.getKey();
    		Vector diagrams = (Vector)entry.getValue();// schemas.get(key);
    		names[i] = new String[diagrams.size() + 1];
        	names[i][0] = key;
        	for (int j = 0; j < diagrams.size(); j++) 
        		names[i][j + 1] = (String)diagrams.get(j);
        	i++;
    	}
	}
	
	public void saveAll() throws SdaiException {
		transaction.commit();
	}

	/**
	 * Asks to end all Changers
	 * Forces to end all Changers
	 * Closing repository 
	 *
	 */
	public void close() throws SdaiException {
		if (repository.isActive() && repository.isModified())
			transaction.abort();
		Iterator iter = models.values().iterator();
		while (iter.hasNext()) {
			ModelHandler model = (ModelHandler)iter.next();
			model.close();
		}
		models.clear();
		if (repository.isActive()) {
			repository.closeRepository();
			repository.unlinkRepository();
		}
		session.closeSession();
		session = null;
	}
	
	/**
	 * fully restarts repository handler. Can be called after ExpressCompilerRepo.sdai file replacing 
	 * or other external access (no file also allowed)
	 * @throws SdaiException
	 */
	public void restart() throws SdaiException {
		close();
		init();
	}

	/**
	 * returns names of schema models (not model names, but names of schema definition)
	 * @return
	 */
	public String[] getSchemas() {
		String[] schemas = new String[names.length];
		for (int i = 0; i < names.length; i++)
			schemas[i] = names[i][0];
		return schemas;
	}

	/**
	 * returns names of layout models for specific schema (not model names, but names of schema definition)
	 * @return
	 */
	public String[] getDiagrams(String schemaName) {
		int i = 0;
		while (i < names.length && !StaticTools.equalStrings(names[i][0], schemaName)) i++;
		String[] diagrams = new String[names[i].length - 1];
		System.arraycopy(names[i], 1, diagrams, 0, diagrams.length);
		return diagrams;
	}

	/**
	 * returns names of all models (not model names, but names of schema definition)
	 * @return
	 */
	public String[] getModels() {
		String[] list = new String[models.size()];
		list = (String[])models.keySet().toArray(list);
		return list;
	}
	
	/**
	 * resolves model, by model name
	 * @param modelName
	 * @return
	 */
	public ModelHandler getModelHandler(String modelName) {
		if (modelName == null) return null;
		return (ModelHandler)models.get(modelName);
	}
	
	public SdaiRepository getRepository() {
		return repository;
	}
	
	/**
	 * special output for debuging purposes
	 */
	public String toString() {
		String text = super.toString() + "\n";
		text += path + "\n";
/*		text += session + "\n";
		text += transaction + "\n";
		text += repository + "\n";
		text += "names (schema diagram diagram ...) " + names;
		if (names != null) {
			text += "[" + names.length + "]:\n";
			for (int i = 0; i < names.length; i++) {
				text += getModelHandler(names[i][0]) + "[" + names[i].length + "]: ";
				for (int j = 1; j < names[i].length; j++) 
					text += getModelHandler(names[i][j]) + " ";
				text += "\n";
			}
		} else text += "\n";
		text += "models map:";
		text += models + "\n";*/
		return text;
	}
	
	
	/**
	 * returns array of schema plus all diagrams of it
	 * @param schema
	 * @return
	 */
	public String[] getSchemaDiagrams(String schema) {
		int i = 0;
		while (i < names.length && !StaticTools.equalStrings(names[i][0], schema)) i++;
		return (i < names.length)?names[i]:new String[]{schema};
	}
	
	/**
	 * deletes model defined by name, also diagram models if this model is a schema
	 * @param name
	 */
	public void deleteModel(String name) {
		String[] models = getSchemaDiagrams(name);
		for (int i = 0; i < models.length; i++) {
			ModelHandler mh = getModelHandler(models[i]);
			if (mh != null) {
				SdaiModel model = mh.getModel();
				try {
					model.deleteSdaiModel();
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex.toString());
				}
			} else {
//				System.err.println("trying to delete model \"" + name + "\" but not found");
			}
		}
	}

	public String getName() {
		String name = "";
		try {
			name = repository.getName();
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			SdaieditPlugin.console(sex.toString());
		}
		return name;
	}
	
	/**
	 * changes associated repository
	 * @param newPath
	 * @throws SdaiException
	 */
	public void changePath(IPath newPath) throws SdaiException {
		close();
		path = newPath;
		init();
	}
}
