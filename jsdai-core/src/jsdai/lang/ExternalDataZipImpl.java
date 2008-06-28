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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author  Vytautas Vaitkevicius
 */
class ExternalDataZipImpl extends ExternalDataLocalImpl {
    
    /** Creates a new instance of ExternalDataZipImpl */
    ExternalDataZipImpl(CEntity owningEntity, boolean isNew) throws SdaiException {
        super(owningEntity, isNew);
    }
    
    /* write down all storeStream content to File ("e" + instance_identifier);    */
    protected void commitInternal() throws SdaiException {

        //System.out.println("---Writing Ext Data to file"); //--VV--030704--
        if (storeStream != null) {
            try {
                String zeName = "e" + owningEntity.instance_identifier;
                ZipEntry zeExtData = new ZipEntry( zeName);
                SdaiRepositoryZipImpl repositoryZipImpl = (SdaiRepositoryZipImpl)repo;
				ZipOutputStream zipOutputStream = repositoryZipImpl.getZipOutputStreanm();
                DataOutput dataOutputStream = repositoryZipImpl.getZipDataOutputStream();
                zipOutputStream.putNextEntry(zeExtData);
                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    dataOutputStream.write(buffer, 0, count);
                    count = storeStream.read(buffer, 0, buffer.length);
                } while (count != -1);
				((OutputStream)dataOutputStream).flush();
                zipOutputStream.closeEntry();
                
                (repositoryZipImpl).emptyDataOutputStream = false;
                (repositoryZipImpl).hmUntouchedEntries.remove(zeName);
				storeStream.close();
                storeStream = null; // protect from repetitive call from commit
            }
            catch(IOException e) {
                throw new SdaiException(SdaiException.SY_ERR, e);
            }
        }
		if(name != null) {
			repo.extDataProps.setProperty(String.valueOf(owningEntity.instance_identifier), name);
		}
    }
    
    protected void loadToStreamInternal(OutputStream stream) throws SdaiException {
        //System.out.println("--- ExternalDataZipImpl > loadToStreamInternal");
        File fileSource = new SdaiRepositoryZipImpl.FileForZipFile(repo.getLocation());
        
        if (fileSource.exists()) {
            Long instanceIdentifier = new Long(owningEntity.instance_identifier);
            String strInstanceIdentifier = String.valueOf(owningEntity.instance_identifier);
            if (repo.getEntityRemovedExternalData() != null && repo.getEntityRemovedExternalData().containsKey(instanceIdentifier)) { 
            // external data was removed, but not commited - thus stream is emty 
                return;
            }
            
            // check for not committed external data
            if (repo.getExtDataProps().containsKey(strInstanceIdentifier)) {
                // external data was created, but not commited - thus stream is empty
                try {
                    ZipFile zipFile = new ZipFile(fileSource);
                    boolean zeExtDataNotExists = (zipFile.getEntry("e"+strInstanceIdentifier) == null);
                    zipFile.close();
                    if (zeExtDataNotExists) {
                        return;
                    }
                } catch(IOException e) {
                    throw new SdaiException(SdaiException.SY_ERR, e);
                }
            }
            
            
            String zeName = "e" + owningEntity.instance_identifier;
            ZipEntry zeExtData = new ZipEntry( zeName);
            try {
                ZipFile zipFile = new ZipFile(fileSource);
                try {
                    InputStream dataInputStream = zipFile.getInputStream( zeExtData);
                    if (dataInputStream != null) {
						BufferedInputStream bufferedStream = new BufferedInputStream(dataInputStream);
                        byte[] buffer = new byte[8 * 1024];
                        int count = 0;
                        do {
                            stream.write(buffer, 0, count);
                            count = bufferedStream.read(buffer, 0, buffer.length);
                        } while (count != -1);
                        
                        bufferedStream.close();
                    }
                    else {
                        throw new SdaiException(SdaiException.VA_NEXS, "External data for this entity does not exist");
                    }
                } finally {
                    zipFile.close();
                }
            } catch(IOException e) {
                throw new SdaiException(SdaiException.SY_ERR, e);
            }
        }
    }


    public void setName(String name) throws SdaiException {
		super.setName(name);
        ((SdaiRepositoryZipImpl)repo).modifiedExtData = true;
	}

    public void storeFromStream(InputStream stream) throws SdaiException {
    	super.storeFromStream(stream);
        ((SdaiRepositoryZipImpl)repo).modifiedExtData = true;
    }
}
