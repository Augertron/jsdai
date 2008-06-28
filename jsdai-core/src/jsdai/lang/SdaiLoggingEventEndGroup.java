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
 * This class supports iterating over entity instances for which some changes 
 * (due to their modification, creation, deletion or substitution) were recorded 
 * in the log file for subsequent undo/redo operations. The records are divided 
 * into segments called groups. The class allows iteration over records of 
 * the last group in the log file in the direction from the first record to the last one, 
 * that is, it allows to get information on all the changes to the entity instances, 
 * which were done from the moment of start of the last group until its closing. 
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
class SdaiLoggingEventEndGroup extends SdaiLoggingForwardIterableEvent {

/**
 * Constructs a new object of <code>SdaiLoggingEventEndGroup</code>
 * with methods which allow to iterate over records (in the forward direction) 
 * in the undo/redo log file. 
 * @param source the object on which the <code>SdaiLoggingEventEndGroup</code> occurred.
 * @param argument an information provided to SDAI listeners receiving logging event.
 */
	SdaiLoggingEventEndGroup(SdaiSession source, Object argument) {
		super(source, ENDED_UNDO_GROUP, -1, argument);
	}


/**
 * Returns an iterator (an object of this class) which can be used to run 
 * through entity instances for which some changes (due to their modification, 
 * creation, deletion or substitution) were recorded in the log file for 
 * subsequent application of undo/redo operations. 
 * The iterator is constrained to iterate (in forward direction) over the 
 * records of the group, which has been closed, in the situation when new 
 * group is not yet started. It allows to examine all entity instance changes 
 * whose description is combined to form this group.
 * @return the iterator over instances within one group of records in undo/redo log file.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 */
	public SdaiOperationIterator getOperationIterator() throws SdaiException {
		try {
			// Initialize operation iterator
			SdaiSession session = (SdaiSession) getSource();
			session.undo_redo_file.seek(session.pointer_pos);
			long new_pos = session.undo_redo_file.readLong();
			while (new_pos > 0) {
				session.undo_redo_file.seek(new_pos);
				new_pos = session.undo_redo_file.readLong();
			}
			initIteratorValues();
			finished = false;
			return this;
		} catch (IOException e) {
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR).initCause(e);
		}
	}

}
