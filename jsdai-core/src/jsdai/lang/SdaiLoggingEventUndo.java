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

import java.io.IOException;

import jsdai.dictionary.EEntity_definition;

/**
 * This class supports iterating over entity instances for which some changes
 * (due to their modification, creation, deletion or substitution) were recorded
 * in the log file for subsequent undo/redo operations. The records are divided
 * into segments called groups. The class allows iteration over records of
 * one group in the direction from the last record to the first one, that is,
 * it allows to get information on all the changes to the entity instances the
 * undo operation for one group will perform.
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
class SdaiLoggingEventUndo extends SdaiLoggingIterableEvent {

	private final long startPos;
	private long nextPos;

/**
 * Constructs a new object of <code>SdaiLoggingEventUndo</code>
 * with methods which allow to iterate over records (in the backward direction)
 * in the undo/redo log file.
 * @param source the object on which the <code>SdaiLoggingEventUndo</code> occurred.
 * @param argument an information provided to SDAI listeners receiving logging event.
 * @param startPos the offset in the undo/redo log file before performing undo operation.
 */
	public SdaiLoggingEventUndo(SdaiSession source, Object argument, long startPos) {
		super(source, UNDONE_GROUP, 0, argument);
		this.startPos = startPos;
		nextPos = -1;
	}


/**
 * Returns an iterator (an object of this class) which can be used to run
 * through entity instances for which some changes (due to their modification,
 * creation, deletion or substitution) were recorded in the log file for
 * subsequent application of undo/redo operations.
 * The iterator is constrained to iterate over one group of records in
 * the backward direction, that is, it allows to examine undo
 * operations for reverting entity instance changes whose description is
 * combined to form one group of records.
 * @return the iterator over instances within one group of records in undo/redo log file.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 */
	public SdaiOperationIterator getOperationIterator() throws SdaiException {
		try {
			// Initialize operation iterator
			SdaiSession session = (SdaiSession) getSource();
			long cur_pos = startPos - 1;
			session.undo_redo_file.seek(cur_pos);
			byte bt = session.undo_redo_file.readByte();
			if (bt == 'B') {
				nextPos = 0;
			} else {
				cur_pos -= SdaiSession.SIZEOF_LONG;
				session.undo_redo_file.seek(cur_pos);
				nextPos = session.undo_redo_file.readLong();
			}
			initIteratorValues();
			return this;
		} catch (IOException e) {
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR).initCause(e);
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.lang.SdaiOperationIterator#next()
	 */
	public boolean next() throws SdaiException {
		try {
			initIteratorValues();

			if(nextPos > 0) {
				boolean more = true;
				SdaiSession session = (SdaiSession) getSource();
				session.undo_redo_file.seek(nextPos);
				nextPos = session.undo_redo_file.readLong();
				byte token = session.undo_redo_file.readByte();
				switch (token) {
				case 'c':
				case 'o': {
					operationType = DELETE_OPERATION;
					if(!readInstanceLog(session, false, false)) {
						more = false;
						nextPos = 0;
					}
					break;
				}
				case 'd': {
					operationType = CREATE_OPERATION;
					if(!readInstanceLog(session, true, false)) {
						more = false;
						nextPos = 0;
					}
					break;
				}
				case 'm': {
					operationType = MODIFY_OPERATION;
					if(!readInstanceLog(session, false, true)) {
						more = false;
						nextPos = 0;
					}
					break;
				}
				case 's': {
					operationType = SUBSTITUTE_OPERATION;
					if(readInstanceLog(session, true, false)) {
						long tempOperationInstanceId = operationInstanceId;
						EEntity_definition tempOperationInstanceType = operationInstanceType;
						SdaiModel tempOperationModel = operationModel;
						try {
							if(readInstanceLog(session, false, false)) {
								operationPrevInstanceId = operationInstanceId;
								operationPrevInstanceType = operationInstanceType;
								operationPrevModel = operationModel;
							} else {
								more = false;
								nextPos = 0;
							}
						} finally {
							operationInstanceId = tempOperationInstanceId;
							operationInstanceType = tempOperationInstanceType;
							operationModel = tempOperationModel;
						}
					} else {
						more = false;
						nextPos = 0;
					}
					break;
				}
				case 'e': {
					operationType = CREATE_EXTERNAL_DATA_OPERATION;
					operationInstanceId = session.undo_redo_file.readLong();
					operationRepository =
						session.findRepositoryByIdentity(session.undo_redo_file.readInt());
					operationInstanceType = null;
					operationModel = null;
					break;
				}
				case 'r': {
					operationType = REMOVE_EXTERNAL_DATA_OPERATION;
					operationInstanceId = session.undo_redo_file.readLong();
					operationRepository =
						session.findRepositoryByIdentity(session.undo_redo_file.readInt());
					operationInstanceType = null;
					operationModel = null;
					break;
				}
				}
				return more;
			} else {
				return false;
			}
		} catch (IOException e) {
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR).initCause(e);
		}
	}

}
