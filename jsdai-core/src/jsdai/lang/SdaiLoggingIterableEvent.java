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

import jsdai.dictionary.CEntity_definition;
import jsdai.dictionary.EEntity_definition;

/**
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
abstract class SdaiLoggingIterableEvent extends SdaiLoggingEvent implements SdaiOperationIterator {

	protected int operationType;
	protected long operationInstanceId;
	protected EEntity_definition operationInstanceType;
	protected SdaiModel operationModel;
	protected SdaiRepository operationRepository;
	protected long operationPrevInstanceId;
	protected EEntity_definition operationPrevInstanceType;
	protected SdaiModel operationPrevModel;

/**
 * Constructs a new object of <code>SdaiLoggingIterableEvent</code>
 * with methods which allow to iterate over records in the undo/redo log file.
 * @param source the object on which the <code>SdaiLoggingIterableEvent</code> occurred.
 * @param id the type of the logging event.
 * @param item an indicator describing a group of records in the undo/redo log file.
 * @param argument an information provided to SDAI listeners receiving logging event.
 */
	public SdaiLoggingIterableEvent(SdaiSession source, int id, int item,
			Object argument) {
		super(source, id, item, argument);
	}

	public int getOperationType() throws SdaiException {
		return operationType;
	}

	public String getOperationInstanceLabel() throws SdaiException {
		return operationInstanceId >= 0 ? "#" + operationInstanceId : "";
	}

	public long getOperationInstanceId() throws SdaiException {
		return operationInstanceId;
	}

	public EEntity_definition getOperationInstanceType() throws SdaiException {
		return operationInstanceType;
	}

	public SdaiModel getOperationModel() throws SdaiException {
		return operationModel;
	}

	public SdaiRepository getOperationRepository() throws SdaiException {
		return operationRepository;
	}

	public String getOperationPrevInstanceLabel() throws SdaiException {
		return operationPrevInstanceId >= 0 ? "#" + operationPrevInstanceId : null;
	}

	public long getOperationPrevInstanceId() throws SdaiException {
		return operationPrevInstanceId;
	}

	public EEntity_definition getOperationPrevInstanceType() throws SdaiException {
		return operationPrevInstanceType;
	}

	public SdaiModel getOperationPrevModel() throws SdaiException {
		return operationPrevModel;
	}

	protected void initIteratorValues() {
		operationType = -1;
		operationInstanceId = -1;
		operationInstanceType = null;
		operationModel = null;
		operationRepository = null;
		operationPrevInstanceId = -1;
		operationPrevInstanceType = null;
		operationPrevModel = null;
	}

	protected boolean readInstanceLog(SdaiSession session, boolean readInstDel,
			boolean doubleBypass) throws IOException, SdaiException {
		operationInstanceId = session.undo_redo_file.readLong();
		short index2mod = session.undo_redo_file.readShort();
		short pop_index = session.undo_redo_file.readShort();
		/*int inst_index =*/ session.undo_redo_file.readInt();
		/*boolean modif =*/ session.undo_redo_file.readBoolean();
		if(readInstDel) {
			/*boolean inst_del =*/ session.undo_redo_file.readBoolean();
		}
		if (index2mod >= 0 && index2mod < session.n_mods_undo) {
			operationModel = session.mods_undo[index2mod];
			SchemaData sch_data = operationModel.underlying_schema.modelDictionary.schemaData;
			operationInstanceType = sch_data.entities[pop_index];
			CEntity_definition edef = (CEntity_definition)operationInstanceType;
			session.bypass_values(session.undo_redo_file, edef);
			if(doubleBypass) {
				session.bypass_values(session.undo_redo_file, edef);
			}
		} else {
			return false;
		}
		return true;
	}
}
