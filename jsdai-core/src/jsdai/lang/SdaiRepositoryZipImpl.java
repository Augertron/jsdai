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
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import jsdai.dictionary.*;

/**
 * Zipped SdaiRepository implementation
 */

class SdaiRepositoryZipImpl extends SdaiRepositoryLocalImpl {
	static final String line_separator = System.getProperty("line.separator");

	File fileSource;
	File fileDestination;

	private ZipOutputStream zipOutputStream;
	private DataOutput dataOutputStream;

	HashMap hmUntouchedEntries;

	boolean modifiedExtData;

	boolean nameWithRecurrenceNumber;

	boolean emptyDataOutputStream;


	SdaiRepositoryZipImpl(SdaiSession session, String name, Object location,
	boolean fCheckNameLocation) throws SdaiException {
		super(session, name, location, fCheckNameLocation);
		modifiedExtData = false;
		nameWithRecurrenceNumber = false;
	}

	SdaiRepositoryZipImpl(SdaiSession session, String name) throws SdaiException {
		super(session, name);
		modifiedExtData = false;
		nameWithRecurrenceNumber = false;
	}


	protected SdaiModel createModel(String model_name, CSchema_definition schema) throws SdaiException {
		return new SdaiModelZipImpl(model_name, schema, this);
	}

	protected SdaiModel createModel(String model_name) {
		return new SdaiModelZipImpl(model_name, this);
	}

	protected SdaiModel createModel(String model_name, SdaiModel dict) throws SdaiException {
		return new SdaiModelZipImpl(model_name, this, dict);
	}


	void loadRepositoryLocal() throws SdaiException {
		//System.out.println("--- SdaiRepositoryZipImpl > loadRepositoryLocal");
		try {
			File fileSource = new FileForZipFile((String)location);
			ZipFile zipFile = new ZipFile(fileSource);
			try {
				ZipEntry zipEntry = zipFile.getEntry("repository");
				InputStream inputStream = zipFile.getInputStream( zipEntry);
				if (inputStream != null) {
					DataInputStream dataInputStream =
						new DataInputStream(new BufferedInputStream(inputStream));
					try {
						loadRepositoryStream(dataInputStream);
					} finally {
						dataInputStream.close();
					}
				}
			} finally {
				zipFile.close();
			}
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		physical_file_name = name;
	}

/**
	Writes the data contained in this repository but not in models within it to the
	binary file when this repository is a local one.
*/
	void saveRepositoryLocal() throws SdaiException {
		//System.out.println("--- SdaiRepositoryZipImpl > saveRepositoryLocal");
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (location == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		ZipOutputStream zipOutputStream = getZipOutputStreanm();
		if (zipOutputStream == null) {
			throw new SdaiException(SdaiException.SY_ERR, "Not opened ZIP output stream to save repository data");
		}
		try {
			String zeName = "repository";
			ZipEntry ze = new ZipEntry(zeName);
			zipOutputStream.putNextEntry(ze);
			DataOutput dataOutputStream = getZipDataOutputStream();
			saveRepositoryStream(dataOutputStream);
			((OutputStream)dataOutputStream).flush();
			zipOutputStream.closeEntry();

			emptyDataOutputStream = false;
			hmUntouchedEntries.remove(zeName);
		} catch (IOException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
	}

	protected void deleteRepositoryInternal() throws SdaiException {
		deleteRepositoryCommon(false);
		File fileSource = new File((String)location);
		if (fileSource.exists()) {
			boolean return_value = fileSource.delete();
			synchronized(FileForZipFile.class) {
				FileForZipFile.fileForZipFileTimeOffset++;
			}
			if (!return_value) {
				String base = SdaiSession.line_separator + AdditionalMessages.BF_DELF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		session = null;
	}


	protected ExternalData createNewEntityExternalData(CEntity entity, boolean existing) throws SdaiException {
		ExternalDataZipImpl newEntityExternalData = null;

		Long instanceIdentifier = new Long(entity.instance_identifier);
		//System.out.println("---createNewEntityExternalData--");//--VV--030704--
		if (existing) {
			if (!getExtDataProps().containsKey(String.valueOf(entity.instance_identifier))){
				throw new SdaiException(SdaiException.VA_NSET, "ExternalData is not set for this instance");
			}
			else {
				if (entityExternalData != null) {
					newEntityExternalData = (ExternalDataZipImpl) entityExternalData.get(instanceIdentifier);
				}
				if (newEntityExternalData == null) {
					newEntityExternalData = new ExternalDataZipImpl(entity, false);
					entityExternalData.put(instanceIdentifier, newEntityExternalData);
					getExtDataProps().setProperty(String.valueOf(instanceIdentifier), "");
					modifiedExtData = true;
				}
			}
		}
		else {
			if (entityExternalData.containsKey(instanceIdentifier)) {
				throw new SdaiException(SdaiException.SI_DUP);
			}
			else {
				newEntityExternalData = new ExternalDataZipImpl(entity, true);
				entityExternalData.put(instanceIdentifier, newEntityExternalData);
				getExtDataProps().setProperty(String.valueOf(instanceIdentifier), "");
				modifiedExtData = true;
			}
		}
		return newEntityExternalData;
	}


	protected boolean testNewEntityExternalData(CEntity entity) throws SdaiException {
		return getExtDataProps().containsKey(String.valueOf(entity.instance_identifier));
	}

	void removeEntityExternalData(CEntity instance, boolean deletingInstance, boolean deletedExternalData) throws SdaiException {
		//System.out.println("removeEntityExternalData");   //--VV--030708--
		Long instanceIdentifier = new Long(instance.instance_identifier);
		Object externalDataObject = (entityExternalData != null) ? entityExternalData.remove(instanceIdentifier) : null;
		if (externalDataObject != null || !deletingInstance || testNewEntityExternalData(instance)) {
			if (entityRemovedExternalData == null) {
				entityRemovedExternalData = new HashMap();
			}
			entityRemovedExternalData.put(instanceIdentifier, externalDataObject != null ? externalDataObject : instance);
			modifiedExtData = true;
			if(externalDataObject != null) {
				((ExternalData)externalDataObject).setDeleted(deletedExternalData);
			}
		}
	}

	boolean testAndRemoveEntityExternalData(long instIdent) throws SdaiException {
		if(super.testAndRemoveEntityExternalData(instIdent)) {
			modifiedExtData = true;
			return true;
		} else {
			return false;
		}
	}

	private void removeEntityExternalDataLocalImpl(long instIdent) throws SdaiException {
		String str_inst_id = String.valueOf(instIdent);

		hmUntouchedEntries.remove("e"+str_inst_id);
		extDataProps.remove(str_inst_id);
	}


	protected void removeEntityExternalDataLocalImpl(ExternalDataLocalImpl externalDataObject) throws SdaiException {
		removeEntityExternalDataLocalImpl(externalDataObject.owningEntity.instance_identifier);
	}


	protected Properties takeExtDataProperties() throws SdaiException {
		extDataProps = new Properties();

		File fileSource = new FileForZipFile((String)location);
		//System.out.println("file = " + fileSource.getName());
		//System.out.println("\texists = " + fileSource.exists());
		//System.out.println("\tlength = " + fileSource.length());
		if (fileSource.exists()) {
			String zeName = "extdata.properties";
			ZipEntry zeExtData = new ZipEntry( zeName);
			try {
				ZipFile zipFile = new ZipFile(fileSource);
				InputStream dataInputStream = zipFile.getInputStream( zeExtData);
				if (dataInputStream != null) {
					BufferedInputStream bufferedStream = new BufferedInputStream(dataInputStream);
					extDataProps.load(bufferedStream);
					bufferedStream.close();
				}
				zipFile.close();
			} catch (IOException e) {
				String base = line_separator + AdditionalMessages.IO_ERRP;
				throw new SdaiException(SdaiException.SY_ERR, base, e);
			}
		}
		return extDataProps;
	}


	protected void updateExtDataProperties() throws SdaiException {
		DataOutput dataOutputStream = getZipDataOutputStream();
		String zeName = "extdata.properties";
		ZipEntry zeExtData = new ZipEntry( zeName);

		try {
			ZipOutputStream zipOutputStream = getZipOutputStreanm();
			zipOutputStream.putNextEntry(zeExtData);
			extDataProps.store((DataOutputStream)dataOutputStream, "External data properties");
			((OutputStream)dataOutputStream).flush();
			zipOutputStream.closeEntry();

			emptyDataOutputStream = false;
			hmUntouchedEntries.remove(zeName);
		} catch (IOException e) {
			String base = line_separator + AdditionalMessages.IO_ERWP;
			throw new SdaiException(SdaiException.SY_ERR, base, e);
		}
	}


	protected void commitExternalData() throws SdaiException {
		Iterator externalDataIter;

		if(entityRemovedExternalData != null) {
			externalDataIter = entityRemovedExternalData.values().iterator();
			while(externalDataIter.hasNext()) {
				Object externalDataObject = externalDataIter.next();
				if(externalDataObject instanceof Long) {
					removeEntityExternalDataLocalImpl(((Long)externalDataObject).longValue());
				} else if(externalDataObject instanceof CEntity) {
					removeEntityExternalDataLocalImpl(((CEntity)externalDataObject).instance_identifier);
				} else {
					ExternalDataLocalImpl externalData =
						(ExternalDataLocalImpl)externalDataObject;
					externalData.removed();
					removeEntityExternalDataLocalImpl(externalData);
				}
			}
			entityRemovedExternalData.clear();
		}

		if(entityExternalData != null) {
			externalDataIter = entityExternalData.values().iterator();
			while(externalDataIter.hasNext()) {
				ExternalData externalData = (ExternalData)externalDataIter.next();
				externalData.commit();
			}
		}
		updateExtDataProperties();
	}


	protected void preCommitting() throws SdaiException {
		//System.out.println("--- SdaiRepositoryZipImpl > preCommitting");

		if (modifiedExtData) {
			getExtDataProps();
		}
		fileSource = new FileForZipFile((String)location);
		if (destinationLocation == null) {
			fileDestination = new File((String)location + ".tmp");
		}
		else {
			fileDestination = new File(destinationLocation);
		}
		hmUntouchedEntries = new HashMap();

		// Removing old temporary file
		if (fileDestination.exists()) {
			fileDestination.delete();
		}

		if (fileSource.exists()) {

			// Retrieving entries form source file
			try {
				ZipFile zipFile = new ZipFile(fileSource);
				Enumeration zipEntries = zipFile.entries();
				while (zipEntries.hasMoreElements()) {
					ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
					hmUntouchedEntries.put(zipEntry.getName(), zipEntry);
				}
				zipFile.close();
			} catch (IOException ex) {
				throw (SdaiException)new SdaiException(SdaiException.SY_ERR, ex.getMessage()).initCause(ex);
			}
		}
		zipOutputStream = null;
		dataOutputStream = null;

		emptyDataOutputStream = true;
	}

	void postCommitting() throws SdaiException {
		//System.out.println("--- SdaiRepositoryZipImpl > postCommitting");

		if (modifiedExtData) {
			String zeName = "extdata.properties";
			commitExternalData();
			hmUntouchedEntries.remove(zeName);
			modifiedExtData = false;
		}

		/*
		try {
			if (fileSource.exists()) {
				FileInputStream fileInputStream = new FileInputStream(fileSource);
				ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));

				ZipEntry ze;

				while ((ze = zipInputStream.getNextEntry()) != null) {
					if (hmUntouchedEntries.containsKey(ze.getName())) {
						byte[] buffer = new byte[8*1024];
						int count = 0;
						zipOutputStream.putNextEntry(ze);
						do {
							zipOutputStream.write(buffer, 0, count);
							count = zipInputStream.read(buffer, 0, buffer.length);
						} while (count != -1);
						zipOutputStream.closeEntry();
						hmUntouchedEntries.remove(ze.getName());
					}
				}
				zipInputStream.close();
			}
		} catch (IOException ex) {
			String msg = "Failed to create " + fileDestination.getAbsolutePath() + " due to " + ex.getMessage();
			throw new SdaiException(SdaiException.SY_ERR, msg);
		}
		*/
	}


	void postCommittingRelease(boolean successful) throws SdaiException {
		boolean successfulRelease = false;
		try {
			if (!emptyDataOutputStream) {
				if (dataOutputStream != null) {
					((OutputStream)dataOutputStream).close();

					int countUE = hmUntouchedEntries.size();
					if (countUE > 0 && fileSource.exists()) {
						RandomAccessFile rafSource = new RandomAccessFile(fileSource,"r");
						RandomAccessFile rafDestination = new RandomAccessFile(fileDestination,"rw");

						//--- DEST --- Reading "End of central directory record" information
						int offsetECDRDest = (int)rafDestination.length() - 22;
						rafDestination.seek(offsetECDRDest);
						rafDestination.skipBytes(10);
						int countEntitiesDest = (rafDestination.readUnsignedByte()+(rafDestination.readUnsignedByte()<<8));
						int sizeCDDest = (rafDestination.readUnsignedByte()+(rafDestination.readUnsignedByte()<<8)+(rafDestination.readUnsignedByte()<<16)+(rafDestination.readUnsignedByte()<<24));
						int offsetCDDest = (rafDestination.readUnsignedByte()+(rafDestination.readUnsignedByte()<<8)+(rafDestination.readUnsignedByte()<<16)+(rafDestination.readUnsignedByte()<<24));
						int sizeBytesCDDest = (int)rafDestination.length() - offsetCDDest;
						byte bytesCDDest[] = new byte[sizeBytesCDDest];
						rafDestination.seek(offsetCDDest);
						// Buffering part of destination file
						rafDestination.read(bytesCDDest);
						rafDestination.seek(offsetCDDest);

						//--- SOURCE --- Reading "End of central directory record" information
						int offsetECDR = (int)rafSource.length() - 22;
						rafSource.seek(offsetECDR);
						rafSource.skipBytes(10);
						int countEntities = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8));
						//int sizeCD = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8)+(rafSource.readUnsignedByte()<<16)+(rafSource.readUnsignedByte()<<24));
						// Read sizeCD
						rafSource.readUnsignedByte();
						rafSource.readUnsignedByte();
						rafSource.readUnsignedByte();
						rafSource.readUnsignedByte();
						int offsetCD = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8)+(rafSource.readUnsignedByte()<<16)+(rafSource.readUnsignedByte()<<24));

						rafSource.seek(offsetCD);

						String nameUE[] = new String[countUE];
						int offsetUEDest[] = new int[countUE];
						int offsetUE[] = new int[countUE];
						int sizeUE[] = new int[countUE];
						int currentUE = 0;

						//--- SOURCE --- Analysing information of entries
						for (int i = 0; i < countEntities; i++) {
							rafSource.skipBytes(20);
							int compressedSize = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8)+(rafSource.readUnsignedByte()<<16)+(rafSource.readUnsignedByte()<<24));
							//int uncompressedSize = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8)+(rafSource.readUnsignedByte()<<16)+(rafSource.readUnsignedByte()<<24));
							rafSource.readUnsignedByte();
							rafSource.readUnsignedByte();
							rafSource.readUnsignedByte();
							rafSource.readUnsignedByte();
							int fileNameLength = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8));
							int extraFieldLength = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8));
							int fileCommentLength = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8));
							rafSource.skipBytes(8);
							int offsetLocalHeader = (rafSource.readUnsignedByte()+(rafSource.readUnsignedByte()<<8)+(rafSource.readUnsignedByte()<<16)+(rafSource.readUnsignedByte()<<24));
							String entryName= "";
							for (int j = 0; j < fileNameLength; j++) {
								entryName += (char)rafSource.readUnsignedByte();
							}
							rafSource.skipBytes(extraFieldLength);
							rafSource.skipBytes(fileCommentLength);
							int offsetNextEntry = (int)rafSource.getFilePointer();

							//--- DEST --- Copying untouched entries data from source to destination file
							if (hmUntouchedEntries.containsKey(entryName)) {
								rafSource.seek(offsetLocalHeader);

								int entryLength = 30 + fileNameLength + extraFieldLength + compressedSize + 16;
								byte entryBytes[] = new byte[entryLength];
								rafSource.read(entryBytes);

								nameUE[currentUE] = entryName;
								offsetUEDest[currentUE] = (int)rafDestination.getFilePointer();
								sizeUE[currentUE] = fileNameLength + extraFieldLength + fileCommentLength;
								offsetUE[currentUE] = offsetNextEntry - sizeUE[currentUE] - 46;
								currentUE++;

								//Writing untouched entry data
								rafDestination.write(entryBytes);
								hmUntouchedEntries.remove(entryName);
							}

							rafSource.seek(offsetNextEntry);
						}
						countUE = currentUE;

						if (countUE > 0) {
							// Finding offset "End of central dir record" in buffered part of destination file
							offsetECDRDest = -1;
							for (int i = 0; i < bytesCDDest.length-4; i++) {
								if (((bytesCDDest[i] == 0x50 && bytesCDDest[i+1] == 0x4b) && bytesCDDest[i+2] == 0x05) && bytesCDDest[i+3] == 0x06) {
									offsetECDRDest = i;
									break;
								}
							}

							sizeCDDest = 0;
							offsetCDDest = (int)rafDestination.getFilePointer();
							if (offsetECDRDest > 0) {
								//--- DEST --- Writing "Central directory" information
								// Copying file headers from buffered part of destination file
								rafDestination.write(bytesCDDest, 0, offsetECDRDest);
								sizeCDDest += offsetECDRDest;

								// Copying file headers of untouched entries from source file
								for (int i = 0; i < countUE ; i++) {
									rafSource.seek(offsetUE[i]);

									byte bytesFH1[] = new byte[42];
									rafSource.read(bytesFH1);
									rafDestination.write(bytesFH1);
									sizeCDDest += 42;

									rafSource.skipBytes(4);
									// Writing new offset of untouched entry
									rafDestination.writeByte(offsetUEDest[i]);
									rafDestination.writeByte(offsetUEDest[i]>>8);
									rafDestination.writeByte(offsetUEDest[i]>>16);
									rafDestination.writeByte(offsetUEDest[i]>>24);
									sizeCDDest += 4;

									byte bytesFH2[] = new byte[sizeUE[i]];
									rafSource.read(bytesFH2);
									rafDestination.write(bytesFH2);
									sizeCDDest += sizeUE[i];
								}

								//--- DEST --- Writing "End of central dir record" information
								rafDestination.write(bytesCDDest, offsetECDRDest, 8);
								// Writing total count of entries on this disk
								int countAllEntitiesDest = countEntitiesDest + countUE;
								rafDestination.writeByte(countAllEntitiesDest);
								rafDestination.writeByte(countAllEntitiesDest>>8);
								// Writing total count of entries
								rafDestination.writeByte(countAllEntitiesDest);
								rafDestination.writeByte(countAllEntitiesDest>>8);
								// Writing size of the central directory
								rafDestination.writeByte(sizeCDDest);
								rafDestination.writeByte(sizeCDDest>>8);
								rafDestination.writeByte(sizeCDDest>>16);
								rafDestination.writeByte(sizeCDDest>>24);
								// Writing offset of the central directory
								rafDestination.writeByte(offsetCDDest);
								rafDestination.writeByte(offsetCDDest>>8);
								rafDestination.writeByte(offsetCDDest>>16);
								rafDestination.writeByte(offsetCDDest>>24);
								// Writing zipfile comment length
								rafDestination.writeByte(0);
								rafDestination.writeByte(0);
							}
						}
						rafSource.close();
						rafDestination.close();
					}
				}
				successfulRelease = true;
			}
			else {
				if (zipOutputStream != null) {
					zipOutputStream.close();
				}
			}
		} catch (IOException ex) {
			String msg = "Failed to create " + fileDestination.getAbsolutePath() + " due to " + ex;
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR, msg).initCause(ex);
		} finally {
			if (!emptyDataOutputStream) {
				if (successful && successfulRelease) {
					if (destinationLocation == null) {
						if (fileSource.exists()) {
							fileSource.delete();
						}
						if (fileDestination.exists()) {
							fileDestination.renameTo(fileSource);
							synchronized(FileForZipFile.class) {
								FileForZipFile.fileForZipFileTimeOffset++;
							}
						}
					} else {
						location = dir_name = destinationLocation;
					}
				}
				else {
					if (fileDestination.exists()) {
						fileDestination.delete();
					}
				}
			}
			else {
				if (fileDestination.exists()) {
					fileDestination.delete();
				}
			}
			destinationLocation = null;
		}


	}

	ZipOutputStream getZipOutputStreanm() throws SdaiException {
		lazyCreateZipStreams();
		return zipOutputStream;
	}

	DataOutput getZipDataOutputStream() throws SdaiException {
		lazyCreateZipStreams();
		return dataOutputStream;
	}

	private void lazyCreateZipStreams() throws SdaiException {
		if(zipOutputStream == null) {
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(fileDestination);
				zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
				zipOutputStream.setLevel(Deflater.BEST_SPEED);
				dataOutputStream = new DataOutputStream(new BufferedOutputStream(zipOutputStream));
			} catch (IOException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
		}
	}

	boolean validateNameFromFile(String nameFromFile) throws SdaiException {
		return true;
	}


	//String removeRecurrenceNumber(String name) throws SdaiException {
	//    if (nameWithRecurrenceNumber) {
	//        int lastIndex = name.lastIndexOf("_");
	//        if (lastIndex > -1) {
	//            name = name.substring(0, lastIndex);
	//        }
	//    }
	//    return name;
	//}
	public String getRealName() throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (nameWithRecurrenceNumber) {
			int lastIndex = name.lastIndexOf("_");
			if (lastIndex > -1) {
				return name.substring(0, lastIndex);
			}
		}
		return name;
	}


	protected boolean committingInternal(boolean commit_bridge, boolean modified) throws SdaiException {
		saveRepositoryLocal();
		return commit_bridge;
	}


	public void setLocation(String location) throws SdaiException {
		if (session == null) {
			throw new SdaiException(SdaiException.RP_NEXS);
		}
		if (location == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (!location.equals((String)this.location)) {
			destinationLocation = location;
			modified = true;
		}
		else {
			destinationLocation = null;
		}
	}

	protected void abortingInternal(boolean modif, boolean contents_modified) throws SdaiException {
		super.abortingInternal(modif, contents_modified);
		destinationLocation = null;
	}

	/**
	 * This is a workaround class for using in ZipFile.
	 * ZipFile has cache and to force file as a new file the last modified
	 * time returns unique value each time file was changed.
	 * This works because ZipFiles are cached by modification time.
	 */
	static class FileForZipFile extends File {
		static int fileForZipFileTimeOffset = 1;

		FileForZipFile(String pathname) {
			super(new File(pathname).getAbsolutePath());
		}

		FileForZipFile(String parent, String child) {
			super(parent, child);
		}

		public long lastModified() {
			synchronized(FileForZipFile.class) {
				return super.lastModified() + fileForZipFileTimeOffset;
			}
		}
	}

}
