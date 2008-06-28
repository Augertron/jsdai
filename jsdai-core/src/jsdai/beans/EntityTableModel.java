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
import java.util.*;
import jsdai.util.*;

public class EntityTableModel extends AbstractTableModel {
	static final int FILTER_NONE = 0;
	static final int FILTER_ZERO = 1;
	static final int FILTER_EXACT = 2;

	int theFilter = FILTER_ZERO;

	ASdaiModel theModels = null;

	Vector elements[] = new Vector[5];
	String[] names = {"Name", "Schema", "# values", "# instances"};
	Class[] clases = {String.class, String.class, Integer.class, Integer.class};

	int 	edtCount = 0,
			cedtCount = 0,
			folderCount = 0;

	public EntityTableModel() {
		elements[0] = new Vector();
		elements[1] = new Vector();
		elements[2] = new Vector();
		elements[3] = new Vector();
		elements[4] = new Vector();
	}

	public EntityTableModel(ASdaiModel models) throws SdaiException {
		this();
		setASdaiModel(models);
	}

	public void setASdaiModel(ASdaiModel models) throws SdaiException {
		theModels = models;
		elements[0].removeAllElements();
		elements[1].removeAllElements();
		elements[2].removeAllElements();
		elements[3].removeAllElements();
		elements[4].removeAllElements();
		edtCount = 0;
		cedtCount = 0;
		folderCount = 0;
		SdaiIterator it_models = models.createIterator();
		while (it_models.next()) {
			SdaiModel model = models.getCurrentMember(it_models);
			SimpleOperations.enshureReadOnlyModel(model);
			ESchema_definition schema = model.getUnderlyingSchema();
			AEntity_declaration declarations = schema.getEntity_declarations(null, null);
			SdaiIterator it_declarations = declarations.createIterator();
			SchemaInstance si = model.getRepository().getSession().getDataDictionary();
			ASchemaInstance asi = new ASchemaInstance();
			asi.addByIndex(1, si);
			while (it_declarations.next()) {
				EDeclaration declaration = declarations.getCurrentMember(it_declarations);
				EEntity_definition definition = (EEntity_definition)declaration.getDefinition(null);

				boolean exact_triger = definition.getInstantiable(null) && (model.getExactInstanceCount(definition) != 0);
				boolean zero_triger = (model.getInstanceCount(definition) != 0);
				if (((theFilter == FILTER_ZERO) && (zero_triger)) || ((theFilter == FILTER_EXACT) && (exact_triger)) || (theFilter == FILTER_NONE)) {
					if (elements[0].indexOf(definition.getName(null)) == -1) {
						elements[0].add(definition.getName(null));
						elements[1].add(SimpleOperations.findSchema_definition(definition, asi).getName(null));
						if (definition.getComplex(null)) {
							elements[2].add(null);
						} else {
							edtCount ++;
							elements[2].add(new Integer(model.getInstanceCount(definition)));
						}
						if (definition.getInstantiable(null)) {
							cedtCount ++;
							elements[3].add(new Integer(model.getExactInstanceCount(definition)));
						} else {
							elements[3].add(null);
						}
						elements[4].add(definition);
					} else {
						int index = elements[0].indexOf(definition.getName(null));
						if (!definition.getComplex(null)) {
							elements[2].set(index, new Integer(model.getInstanceCount(definition)+((Integer)elements[2].get(index)).intValue()));
						}
						if (definition.getInstantiable(null)) {
							elements[3].set(index, new Integer(model.getExactInstanceCount(definition)+((Integer)elements[3].get(index)).intValue()));
						}
					}
				}
			}
			folderCount += model.getInstanceCount();
		}
		fireTableDataChanged();
//		fireTableRowsUpdated(0, getRowCount());
	}

	public int getColumnCount() {
		return 4;
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
		return elements[(columnIndex == -1)?4:columnIndex].elementAt(rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void fireTableDataUpdated() {
		fireTableRowsUpdated(0, getRowCount());
	}

	public int getEdtCount() {return edtCount;}
	public int getCedtCount() {return cedtCount;}
	public int getFolderCount() {return folderCount;}

	public void setFilter(int filter) throws SdaiException {
		theFilter = filter;
		setASdaiModel(theModels);
	}

}

