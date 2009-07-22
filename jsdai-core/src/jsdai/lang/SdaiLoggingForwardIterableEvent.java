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

/**
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
abstract class SdaiLoggingForwardIterableEvent extends SdaiLoggingIterableEvent {

	protected boolean finished;

/**
 * Constructs a new object of <code>SdaiLoggingForwardIterableEvent</code>
 * with methods which allow to iterate over records in the undo/redo log file.
 * @param source the object on which the <code>SdaiLoggingForwardIterableEvent</code> occurred.
 * @param id the type of the logging event.
 * @param item an indicator describing a group of records in the undo/redo log file.
 * @param argument an information provided to SDAI listeners receiving logging event.
 */
	SdaiLoggingForwardIterableEvent(SdaiSession source, int id,
			int item, Object argument) {
		super(source, id, item, argument);
		finished = true;
	}


/**
 * Returns an iterator which can be used to run through entity
 * instances for which some changes (due to their modification, creation,
 * deletion or substitution) were recorded in the log file for
 * subsequent application of undo/redo operations.
 * The iterator is constrained to iterate over one group of records.
 * @return the iterator over instances within one group of records in undo/redo log file.
 */
	public abstract SdaiOperationIterator getOperationIterator() throws SdaiException;

	// SdaiOperationIterator implementation

	/* (non-Javadoc)
	 * @see jsdai.lang.SdaiOperationIterator#next()
	 */
	public boolean next() throws SdaiException {
		try {
			initIteratorValues();

			if(!finished) {
				SdaiSession session = (SdaiSession) getSource();
				byte token = session.undo_redo_file.readByte();
				switch (token) {
				case 'c':
				case 'o': {
					operationType = CREATE_OPERATION;
					if(!readInstanceLog(session, false, false)) {
						token = 'Q';
					}
					break;
				}
				case 'd': {
					operationType = DELETE_OPERATION;
					if(!readInstanceLog(session, true, false)) {
						token = 'Q';
					}
					break;
				}
				case 'm': {
					operationType = MODIFY_OPERATION;
					if(!readInstanceLog(session, false, true)) {
						token = 'Q';
					}
					break;
				}
				case 's': {
					operationType = SUBSTITUTE_OPERATION;
					boolean success;
					try {
						success = readInstanceLog(session, true, false);
						operationPrevInstanceId = operationInstanceId;
						operationPrevInstanceType = operationInstanceType;
						operationPrevModel = operationModel;
					} finally {
						operationInstanceId = -1;
						operationInstanceType = null;
						operationModel = null;
					}
					if(success) {
						if(!readInstanceLog(session, false, false)) {
							token = 'Q';
						}
					} else {
						token = 'Q';
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
				session.undo_redo_file.readLong();
				finished = token == 'Q';
			}
			return !finished;
		} catch (IOException e) {
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR).initCause(e);
		}
	}

}
