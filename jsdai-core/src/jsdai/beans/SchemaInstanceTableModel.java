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

import java.awt.*;
import java.util.*;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class SchemaInstanceTableModel extends AbstractTableModel {
	SchemaInstance schemaInstance = null;

	String[] names = {"Repository", "Model", "Schema"};
	Class[] clases = {String.class, String.class, String.class};
	Vector elements[] = new Vector[4];

	public SchemaInstanceTableModel() {
		elements[0] = new Vector();
		elements[1] = new Vector();
		elements[2] = new Vector();
		elements[3] = new Vector();
	}

	public void setSchemaInstance(SchemaInstance schemaInstance) throws SdaiException {
		this.schemaInstance = schemaInstance;
		ASdaiModel models = schemaInstance.getAssociatedModels();
		elements[0].removeAllElements();
		elements[1].removeAllElements();
		elements[2].removeAllElements();
		elements[3].removeAllElements();
		if ((schemaInstance == null) || (models == null)) return;
		SdaiIterator it_models = models.createIterator();
		while (it_models.next()) {
			SdaiModel model = models.getCurrentMember(it_models);
			elements[0].add(model.getRepository().getName());
			elements[1].add(model.getName());
			elements[2].add(model.getUnderlyingSchema().getName(null));
			elements[3].add(model);
		}
		super.fireTableDataChanged();
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int columnIndex) {
		return names[columnIndex];
	}

	public Class getColumnClass(int columnIndex) {
		return clases[columnIndex];
	}

	public int getRowCount() {
		return elements[0].size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return elements[(columnIndex == -1)?3:columnIndex].elementAt(rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void fireTableDataChanged() {
		try {
			setSchemaInstance(schemaInstance);
		} catch (SdaiException ex) { ; }
	}
}

