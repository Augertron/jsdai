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

package jsdai.lang; import jsdai.dictionary.*;

class Get_instance {
	private int [] entity_types;
	private int [] indices;
	int length;
	int index_user_defined = -1;
	SdaiModel model;
	Writer_error_table error_table;


	Get_instance(Writer_error_table err_table) {
		error_table = err_table;
		if (entity_types == null) {
			entity_types = new int[PhFileWriter.INITIAL_SIZE_OF_FOLDERS];
			indices = new int[PhFileWriter.INITIAL_SIZE_OF_FOLDERS];
		}
	}


	void put_entity_types(SdaiModel given_model)	{
		model = given_model;
		if (entity_types.length < model.lengths.length) {
			entity_types = ensureCapacity(entity_types, model.lengths.length);
			indices = ensureCapacity(indices, model.lengths.length);
		}
		for (int l = 0; l < model.lengths.length; l++) {
			if (model.lengths[l] > 0) {
				entity_types[length] = l;
				indices[length] = 0;
				length++;
			} 
		}
	}

	CEntity get_next() throws SdaiException {
		if (length <= 0) {
			return null;
		}
		int i;
		CEntity instance = null;
		long min_id = Long.MAX_VALUE;
		int index_selected = -1;

		for (i = 0; i < length; i++) {
			CEntity inst =
				model.instances_sim[entity_types[i]][indices[i]];
			if (inst.instance_identifier <= min_id) {
				min_id = inst.instance_identifier;
				instance = inst;
				index_selected = i;
			}
		}
		if (index_selected >= 0) {
			if (indices[index_selected] < model.lengths[entity_types[index_selected]]-1) {
				indices[index_selected]++;
				return instance;
			}
			for (i = index_selected; i < length-1; i++) {
				entity_types[i] = entity_types[i+1];
				indices[i] = indices[i+1];
			}
			length--;
			return instance;
		}
		String base = SdaiSession.line_separator + AdditionalMessages.WR_OUT + 
			SdaiSession.line_separator + "     " +
			(String)error_table.messages.get(new Integer(PhFileWriter.INTERNAL_ERROR_WR));
		throw new SdaiException(SdaiException.SY_ERR, base);
	}

	CEntity get_next_user_defined() {
		if (model.repository == SdaiSession.systemRepository) {
			return null;
		}
		int ind = model.lengths.length - 1;
		index_user_defined++;
		if (index_user_defined < model.lengths[ind]) {
			CEntity inst = model.instances_sim[ind][index_user_defined];
			return inst;
		} else {
			return null;
		}
	}


	private int [] ensureCapacity(int [] arr, int demand) {
		int new_length = arr.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		int [] new_array = new int[new_length];
		return new_array;
	}

}
