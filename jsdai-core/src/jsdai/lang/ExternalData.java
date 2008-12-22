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

/**
 * An ExternalData object encapsulates data for an entity instance which is
 * not covered by the attribute values of the entity instance. Because of this
 * the external data is not covered by the underlying Express schema. It is up to
 * JSDAI application to take advantage of this and define the meaning of this data.<br>
 *
 * The data is loaded and stored using InputStream and OutputStream thus allowing
 * to handle big chunks of data available in a file.<br>
 *
 * An ExternalData object is always combined with an entity instance ant thus only
 * available when corresponding entity instance is available. An ExternalData object
 * belongs to the SdaiModel to which the entity instance belongs to. If access to
 * the SdaiModel is ended the ExternalData object becomes invalid.<br>
 *
 * This object can be obtained from {@link EEntity#getExternalData}.
 * The state of the object can become invalid if external data
 * is deleted by calling {@link EEntity#removeExternalData}.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public abstract class ExternalData {

	protected CEntity owningEntity;
	protected InputStream storeStream;
	protected String name;
	protected boolean newName;
	protected boolean unset;
	protected boolean removed;
	private boolean deleted;

	/** Creates a new instance of ExternalData */
    protected ExternalData(CEntity owningEntity) {
		this.owningEntity = owningEntity;
		this.storeStream = null;
		this.name = null;
		this.newName = false;
		this.unset = true;
		this.removed = false;
    }

	/**
	 * Gets the instance this external data belongs to.
	 * @return The owning instance
	 */
	public EEntity getInstance() {
		return owningEntity;
	}

	/**
	 * Loads data in this object to specified stream.
	 * The valid transaction has to be running
	 * for this method to succeed. The stream is
	 * never closed. This is responsibility of the application.
	 * @param stream the stream to load data to.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void loadToStream(OutputStream stream) throws SdaiException {
		if (owningEntity == null || owningEntity.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if(storeStream != null) {
			if(!storeStream.markSupported()) {
				throw new SdaiException(SdaiException.SY_ERR,
						"Assigned store stream does not support marking," +
						" therefore loading to stream can not be handled");
			}
			try {
				storeStream.mark(0);
				try {
					byte[] buffer = new byte[8 * 1024];
					int count = 0;
					do {
						stream.write(buffer, 0, count);
						count = storeStream.read(buffer, 0, buffer.length);
					} while (count != -1);
				} finally {
					storeStream.reset();
				}
			}
			catch(IOException e) {
				throw new SdaiException(SdaiException.SY_ERR, e);
			}
		} else {
			loadToStreamInternal(stream);
			if(unset) {
				throw new SdaiException(SdaiException.VA_NSET,
					"External data does not exist for this instance: " + owningEntity);
			}
		}
	}

	/**
	 * Stores data from specified stream to this object.
	 * The valid transaction has to be running
	 * for this method to succeed.
	 * This methods doesn't perform actual operation
	 * but keeps the stream for later use. Actual storing
	 * occurs during transaction committing. The stream is
	 * closed after transaction commit or abort within calls
	 * <code>SdaiTransaction#abort()</code>,
	 * <code>SdaiTransaction#endTransactionAccessAbort()</code>,
	 * <code>SdaiTransaction#commit()</code>,
	 * <code>SdaiTransaction#endTransactionAccessCommit()</code>, or
	 * <code>SdaiTransaction#endTransactionAccessCommit(String)</code>.
	 * If <code>loadToStream</code> is invoked after call to
	 * <code>storeFromStream</code> then provided stream has to
	 * support mark/reset, ie. <code>stream.markSupported()</code>
	 * has to return <code>true</code> and calls to
	 * <code>stream.mark(0)</code> and <code>stream.reset()</code>
	 * have to be functional.
	 * Use <code>jsdai.io.ResettableFileInputStream</code> for this purpose as file input.
	 * @param stream the stream to get data from.
	 *        This stream has to be valid until the end of transaction.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see SdaiTransaction#abort()
	 * @see SdaiTransaction#endTransactionAccessAbort()
	 * @see SdaiTransaction#commit()
	 * @see SdaiTransaction#endTransactionAccessCommit()
	 * @see SdaiTransaction#endTransactionAccessCommit(String)
	 * @see jsdai.io.ResettableFileInputStream
	 */
	public void storeFromStream(InputStream stream) throws SdaiException {
		if (owningEntity == null || owningEntity.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owningEntity.owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owningEntity.owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owningEntity.owning_model);
		}
		if(removed) {
			throw new SdaiException(SdaiException.VA_NEXS, "External data was removed");
		}
		storeStream = stream;
		unset = false;
	}

	/**
	 * Gets the name of this external data.
	 * The valid transaction has to be running
	 * for this method to succeed.
	 * @return The name of this external data
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public String getName() throws SdaiException {
		if (owningEntity == null || owningEntity.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if(unset) {
			throw new SdaiException(SdaiException.VA_NSET,
				"External data does not exist for this instance: " + owningEntity);
		}
		if(name == null) {
			throw new SdaiException(SdaiException.VA_NSET,
				"Name not set: " + owningEntity);
		}
		return name;
	}

	/**
	 * Sets the name of this external data.
	 * ExternalData object name can be used as a file name
	 * when data is exported from JSDAI.
	 * The valid transaction has to be running
	 * for this method to succeed.
	 * @param name The new name of this external data
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void setName(String name) throws SdaiException {
		if(name == null) {
			throw new SdaiException(SdaiException.VA_NSET, "New external data name is null");
		}
		if (owningEntity == null || owningEntity.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owningEntity.owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owningEntity.owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owningEntity.owning_model);
		}
		if(removed) {
			throw new SdaiException(SdaiException.VA_NEXS, "External data was removed");
		}
		this.name = name;
		newName = true;
		unset = false;
	}

	void commit() throws SdaiException {
		commitInternal();
	}

	void abort() throws SdaiException {
		try {
			if(storeStream != null) {
				storeStream.close();
				storeStream = null;
			}
		} catch(IOException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

	protected void removed() throws SdaiException {
		unset = true;
		abort();
	}

	protected void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	protected boolean isDeleted() {
		return this.deleted;
	}

	protected SdaiRepository getOwningRepository() {
		return owningEntity.owning_model.repository;
	}

	protected abstract void loadToStreamInternal(OutputStream stream) throws SdaiException;
	protected abstract void commitInternal() throws SdaiException;
}
