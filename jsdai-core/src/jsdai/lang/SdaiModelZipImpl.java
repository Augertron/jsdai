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

package jsdai.lang;

import jsdai.dictionary.*;
import jsdai.util.JarFileURLStreamHandler;

import java.io.*;
import java.util.jar.JarFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.net.URL;
import java.net.MalformedURLException;


/**
 *
 */
class SdaiModelZipImpl extends SdaiModelLocalImpl {
	/** Used to create application (through the method createSdaiModel) and dictionary
	 *  SdaiModels.
	 *  Also using this constructor some special SdaiModel called sessionModel is created.
	 */
	SdaiModelZipImpl(String model_name, CSchema_definition schema, SdaiRepository rep) throws SdaiException {
		super(model_name, schema, rep);
	}
	
	/** Used to create an SdaiModel when reading a repository binary file.
	 */
	SdaiModelZipImpl(String model_name, SdaiRepository rep, SdaiModel dict) throws SdaiException {
		super(model_name, rep, dict);
	}
	
	/** Used to create an SdaiModel when in the binary file being loaded the name of
	 *  a model and its repository (or only its name) are found but this repository
	 *  misses a model with this given name.
	 *  Also, used to create virtual models.
	 */
	SdaiModelZipImpl(String model_name, SdaiRepository rep) {
		super(model_name, rep);
	}



/**
	Loads the data contained in the binary SDAI file to this SdaiModel when this SdaiModel 
	belongs to a local repository.
*/
	long loadLocal(int mode_to_start) throws SdaiException {
		setMode(READ_WRITE);
		if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			ex_models = new SdaiModel[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
		
        //System.out.println("--- SdaiModelZipImpl > loadLocal");
        File fileSource = null;
		long res = -1;
		try {
            String zeName = "m" + identifier;
            fileSource = new SdaiRepositoryZipImpl.FileForZipFile((String)repository.location);
			ZipFile zipFile = new ZipFile(fileSource);
            try {
                ZipEntry zipEntry = new ZipEntry(zeName);
                InputStream inputStream = zipFile.getInputStream( zipEntry);
                if (inputStream != null) {
                    DataInputStream dataInputStream = 
						new DataInputStream(new BufferedInputStream(inputStream));
                    try {
                        res = loadStream(dataInputStream, mode_to_start);
                    } finally {
                        setMode(mode_to_start);
                        dataInputStream.close();
                    }
                } else {
                	prepareInitialContens(mode_to_start);
					setMode(mode_to_start);
					modified = false;
					deleteAllInstances();
				}
			} finally {
                zipFile.close();
			}
		} catch (IOException ex) {
            String base = SdaiSession.line_separator + AdditionalMessages.BF_MNF1 +
            fileSource.getAbsolutePath() + AdditionalMessages.BF_MNF2;
            throw new SdaiException(SdaiException.SY_ERR, base);
		}
		resolveInConnectors(false);
		return res;
	}


/**
	Writes the data contained in this SdaiModel to the binary SDAI file when this 
	SdaiModel belongs to a local repository.
*/
    void saveLocal() throws SdaiException {
        
        //System.out.println("--- SdaiModelZipImpl > saveLocal");
		if (!repository.active) {
			throw new SdaiException(SdaiException.RP_NOPN, repository);
		}
        if (ex_models == null) {
			ex_model_names = new String[NUMBER_OF_EXTERNAL_MODS];
			n_ex_models = 0;
			ex_repositories = new String[NUMBER_OF_EXTERNAL_REPS];
			n_ex_repositories = 0;
			ex_edefs = new String[NUMBER_OF_EXTERNAL_EDEFS];
			n_ex_edefs = 0;
		}
		try {
            String zeName = "m" + identifier;
            ZipEntry ze = new ZipEntry(zeName);
            SdaiRepositoryZipImpl repositoryZipImpl = (SdaiRepositoryZipImpl)repository;
            ZipOutputStream zipOutputStream = repositoryZipImpl.getZipOutputStreanm();
            DataOutput dataOutputStream = repositoryZipImpl.getZipDataOutputStream();
			zipOutputStream.putNextEntry(ze);
            saveStream(dataOutputStream);
			((OutputStream)dataOutputStream).flush();
            zipOutputStream.closeEntry();
            
            repositoryZipImpl.emptyDataOutputStream = false;
            repositoryZipImpl.hmUntouchedEntries.remove(zeName);
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}
    
    protected boolean deletingInternal(boolean commit_bridge, SdaiRepository rep_for_del_mod) throws SdaiException {
        String zeName = "m" + identifier;
        ((SdaiRepositoryZipImpl)rep_for_del_mod).hmUntouchedEntries.remove(zeName);
		return commit_bridge;
	}


    void loadInstanceIdentifiers() throws SdaiException {
        File fileSource = null;
        all_inst_count = 0;
		try {
            String zeName = "m" + identifier;
            fileSource = new SdaiRepositoryZipImpl.FileForZipFile((String)repository.location);
			ZipFile zipFile = new ZipFile(fileSource);
            try {
                ZipEntry zipEntry = new ZipEntry(zeName);
                InputStream inputStream = zipFile.getInputStream( zipEntry);
                if (inputStream != null) {
                    DataInputStream dataInputStream = 
						new DataInputStream(new BufferedInputStream(inputStream));
                    try {
                        loadInstanceIdentifiers(dataInputStream);
                    } finally {
                        dataInputStream.close();
                    }
                }
			} finally {
                zipFile.close();
			}
		} catch (IOException ex) {
            String base = SdaiSession.line_separator + AdditionalMessages.BF_MNF1 +
            fileSource.getAbsolutePath() + AdditionalMessages.BF_MNF2;
            throw new SdaiException(SdaiException.SY_ERR, base);
			
		}
	}


    public URL getLocationURL() throws SdaiException {
		if (repository == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
        try {
            File fRepo = new SdaiRepositoryZipImpl.FileForZipFile((String)repository.location);
            String repositoryUrl = fRepo.toURI().toURL().toString();
            JarFileURLStreamHandler handler = 
            	(JarFileURLStreamHandler)repository.session.jarFileURLStreamHandlers.get(repositoryUrl);
            if (handler == null) {
            	handler = new JarFileURLStreamHandler(new JarFile(fRepo));
            	repository.session.jarFileURLStreamHandlers.put(repositoryUrl, handler);
			}
		    return new URL("jar", "", -1, repositoryUrl + "!/m" + identifier, handler);
        } catch (MalformedURLException ex) {
            throw new SdaiException(SdaiException.LC_NVLD, ex, ex.getMessage());
        } catch (IOException ex) {
            throw new SdaiException(SdaiException.LC_NVLD, ex, ex.getMessage());
		}
	}
}
