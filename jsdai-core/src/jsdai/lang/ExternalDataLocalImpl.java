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

import java.io.*;
import java.util.*;
import java.util.HashMap;
import jsdai.dictionary.*;

/**
 *
 * @author  Antanas Masevicius
 */
class ExternalDataLocalImpl extends ExternalData {
    File fileD;
    boolean fileDIsSet=false;
    SdaiRepositoryLocalImpl repo = null;
    
    /** Creates a new instance of ExternalDataLocalImpl */
    ExternalDataLocalImpl(CEntity owningEntity, boolean isNew) throws SdaiException {
        super(owningEntity);
		storeStream = null;
        unset = false;
		name = null;
		repo = (SdaiRepositoryLocalImpl) owningEntity.owning_model.getRepository();
		if(isNew){
			name = "";
		}else{
			name = repo.getExtDataProps().getProperty(String.valueOf(owningEntity.instance_identifier));
		};
    }
    
    /* write down all storeStream content to File ("e" + instance_identifier);    */
    protected void commitInternal() throws SdaiException {
        /* update extdata.profile if setName() have been called */
        if(newName){
            repo.extDataProps.setProperty(String.valueOf(owningEntity.instance_identifier), name);
            repo.updateExtDataProperties();
        }
		// else{
		// 						if(repo.extDataProps.getProperty(String.valueOf(owningEntity.instance_identifier)) != null){
		// 								repo.extDataProps.setProperty(String.valueOf(owningEntity.instance_identifier), "");
		// 								//								repo.extDataProps.remove(String.valueOf(owningEntity.instance_identifier));
		// 								repo.updateExtDataProperties();
		// 						};
		// 				}

        if(storeStream != null){
            if(fileDIsSet){
                if( (name != null) && (newName == false) ){
                    repo.extDataProps.setProperty(String.valueOf(owningEntity.instance_identifier), name);                    
                    repo.updateExtDataProperties();
                }
                try {
                    FileOutputStream stream = new FileOutputStream(fileD);
					BufferedOutputStream bstream = new BufferedOutputStream(stream);
                    //System.out.println("file name: " +fileD.toString());
					//Thread.dumpStack();

                    byte[] buffer = new byte[8 * 1024];
                    int count = 0;
                    do {
						//System.err.println("writing to file... count: " +count);
						//System.out.println("write: " +new String(buffer));
											
                        bstream.write(buffer, 0, count);
						bstream.flush();
                        count = storeStream.read(buffer, 0, buffer.length);
                    } while (count != -1);
					bstream.close();
					// 										FileInputStream fi = new FileInputStream(fileD);
					// 										byte[] buf = new byte[1000];
					// 										fi.read(buf);
					//										System.out.println("buffer: " + new String(buf));										
					storeStream.close();
					storeStream = null; // protect from repetative call from commit
                }
                catch(IOException e) {
                    throw new SdaiException(SdaiException.SY_ERR, e);
                }
            }else{
                throw new SdaiException(SdaiException.VA_NSET,
										"External data repository is not created for this instance - do getExternalData() first: " + owningEntity);
            }
        }else if (name != null) {
			repo.extDataProps.setProperty(String.valueOf(owningEntity.instance_identifier), name);  
			repo.updateExtDataProperties();
		} 
    }
    
    protected void loadToStreamInternal(OutputStream stream) throws SdaiException {
        try{
			Long instanceIdentifier = new Long(owningEntity.instance_identifier);
			if (repo.getEntityRemovedExternalData() != null && repo.getEntityRemovedExternalData().containsKey(instanceIdentifier)) {
				// external data was removed, but not commited, thus it is emty
				return;
			}

			File fd = new File(repo.getLocation(), "e" + owningEntity.instance_identifier);
						
			// check for not commited external data
			if (fileDIsSet && !fileD.exists()) {
				// external data was created, but not commited - thus stream is empty
				return;
			} 
			
            FileInputStream streamFromRepo = new FileInputStream(fd);
            if(streamFromRepo == null) {
                throw new SdaiException(SdaiException.VA_NEXS, "External data for this entity does not exist");
            }
            
            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
                stream.write(buffer, 0, count);
                count = streamFromRepo.read(buffer, 0, buffer.length);
            } while (count != -1);
            streamFromRepo.close();
            
        } catch(IOException e) {
            throw new SdaiException(SdaiException.SY_ERR, e);
        }
    }
    
    protected void setFD(File fd) {
        fileD = fd;
        fileDIsSet = true;
    }
    
    protected void unsetFD() {
        fileD = null;
        fileDIsSet = false;
    }

    protected boolean isSetFD() {
		return fileDIsSet;
    }
    
    protected File getFD() {
        return fileD;
    }
}
