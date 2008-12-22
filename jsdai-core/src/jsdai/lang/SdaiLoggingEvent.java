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

import jsdai.dictionary.EEntity_definition;

/**
 * This class informs SDAI listeners about changes done in the undo/redo log file.
 * The subclasses of this class support iterating over entity instances for which some
 * changes (due to their modification, creation, deletion or substitution) were recorded
 * in the log file for subsequent undo/redo operations. The records are divided
 * into segments called groups. The subclasses allow iteration over records of
 * one group in one of the two directions: from the first record to the last one,
 * or vice versa.
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
public class SdaiLoggingEvent extends SdaiEvent {

/**
 * Logging of data for subsequent undo/redo operations is enabled.
 */
	public static final int ENABLED_LOGGING = 101;

/**
 * Logging of data for subsequent undo/redo operations is disabled.
 */
	public static final int DISABLED_LOGGING = 102;

/**
 * The undo/redo log file exists but is empty.
 */
	public static final int EMPTIED_HISTORY = 103;

/**
 * A group of records in undo/redo log file is started.
 */
	public static final int STARTING_UNDO_GROUP = 104;

/**
 * A group of records in undo/redo log file is ended.
 */
	public static final int ENDED_UNDO_GROUP = 105;

/**
 * The undo operation is applied to one group of records in the undo/redo log file.
 */
	public static final int UNDONE_GROUP = 106;

/**
 * The redo operation is applied to one group of records in the undo/redo log file.
 */
	public static final int REDONE_GROUP = 107;

	private static final SdaiOperationIterator EMTPY_ITERATOR = new SdaiOperationIterator() {

		public boolean next() {
			return false;
		}

		public int getOperationType() {
			return -1;
		}

		public SdaiModel getOperationModel() {
			return null;
		}

		public SdaiRepository getOperationRepository() throws SdaiException {
			return null;
		}

		public EEntity_definition getOperationInstanceType() {
			return null;
		}

		public String getOperationInstanceLabel() {
			return "";
		}

		public long getOperationInstanceId() throws SdaiException {
			return -1;
		}

		public SdaiModel getOperationPrevModel() {
			return null;
		}

		public EEntity_definition getOperationPrevInstanceType() {
			return null;
		}

		public String getOperationPrevInstanceLabel() {
			return null;
		}

		public long getOperationPrevInstanceId() throws SdaiException {
			return -1;
		}

	};

/**
 * Constructs a new object of <code>SdaiLoggingEvent</code>
 * with methods which allow to iterate over records in the undo/redo log file.
 * @param source the object on which the <code>SdaiLoggingEvent</code> occurred.
 * @param id the type of the logging event.
 * @param item an indicator describing a group of records in the undo/redo log file.
 * @param argument an information provided to SDAI listeners receiving logging event.
 */
	SdaiLoggingEvent(SdaiSession source, int id, int item, Object argument) {
		super(source, id, item, argument);
	}

/**
 * Returns an object of <code>SdaiOperationIterator</code> with fields
 * assigned to default values.
 * @return the iterator over instances within one group of records in undo/redo log file.
 */
	public SdaiOperationIterator getOperationIterator() throws SdaiException {
		return EMTPY_ITERATOR;
	}
}
