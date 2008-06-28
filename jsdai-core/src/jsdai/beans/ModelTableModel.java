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

package jsdai.beans;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import jsdai.lang.*;
import java.awt.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;

public class ModelTableModel extends AbstractTableModel {
	SdaiRepository repo = null;
	String[] names = {"Mode", "Modified", "Name", "Underlying schema"};

	public ModelTableModel() {
	}

	public void setRepository(SdaiRepository repo) {
		this.repo = repo;
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return 4;
	}

	public String getColumnName(int columnIndex) {
		return names[columnIndex];
	}

	public int getRowCount() {
		int result = 0;
		try {
			result = ((repo == null)?0:repo.getModels().getMemberCount());
		} catch (SdaiException e) {
//			e.printStackTrace();
		}
		return result;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		try {
			ASdaiModel models = repo.getModels();
			SdaiModel model = models.getByIndex(rowIndex+1);
			switch (columnIndex) {
				case 0 :
					switch (model.getMode()) {
						case 0 :
							result = " - ";
							break;
						case 1 :
							result = "r/o ";
							break;
						case 2 :
							result = "r/w ";
							break;
						default :
							result = " - ";
							break;
					}
					break;
				case 1 :
					result = (Boolean.valueOf(model.isModified())).toString();
					break;
				case 2 :
					result = model.getName();
					break;
				case 3 :
					result = model.getUnderlyingSchemaString();
					break;
				default :
					result = model;
					break;
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean modified = false;
		try {
			modified = ((SdaiModel)getValueAt(rowIndex, -1)).isModified();
		} catch (SdaiException ex) {
			ex.printStackTrace();
		}
		return (columnIndex == 0) && !modified;
	}

	public int findValueIndex(Object value) {
		int i = 0;
		while (i < getRowCount()) {
			if (value == getValueAt(i, -1)) {
				return i;
			}
			i++;
		}
		return -1;
	}
}

