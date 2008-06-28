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

import javax.swing.table.AbstractTableModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import jsdai.lang.*;
import jsdai.dictionary.*;

/**The AggregateListModel provides a List with its contents.
 	Contens are jsdai.lang.Aggregate and supports all
*/
public class AggregateTableModel extends AbstractTableModel {
	SdaiIterator iterator, it_attributes;
	Aggregate aggregate;
	AAttribute attributes;
	String[] names = null;

	public AggregateTableModel() throws SdaiException{
	}

	public AggregateTableModel(Aggregate aggregate, EEntity_definition entity) throws SdaiException{
		setAggregate(aggregate, entity);
	}

	public void setAggregate(Aggregate aggregate, EEntity_definition entity) throws SdaiException {
		try {
			this.aggregate = aggregate;
			iterator = aggregate.createIterator();
			attributes = entity.getAttributes(null, null);
			it_attributes = attributes.createIterator();
			names = new String[attributes.getMemberCount()];
			for (int i = 0; i < attributes.getMemberCount(); i++) {
				names[i] = attributes.getByIndex(i).getName(null);
			}
			fireTableDataChanged();
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

/*	public Class getColumnClass(int columnIndex) {
//		return aggregate.getByIndexObject(columnIndex).class;
		return null;
	}*/

	public int getColumnCount() {
		int result = 0;
		if (names != null) {
			result = names.length;
		}
		return result;
	}

	public String getColumnName(int columnIndex) {
		return names[columnIndex];
	}

	public int getRowCount() {
		int result = 0;
		try {
			if (aggregate != null)
				result = aggregate.getMemberCount();
			else
				result = 0;
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}
		return result;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		try {
			int i = 0;
			iterator.beginning();
			while ((i <= rowIndex) && (iterator.next())) {i++;}
			EEntity entity = (EEntity)aggregate.getCurrentMemberObject(iterator);
			if (entity.testAttribute(attributes.getByIndex(columnIndex), new jsdai.dictionary.EDefined_type[20]) != 0) {
				result = entity.get_object(attributes.getByIndex(columnIndex));
			}
			else {
				result = "";
			}
//			result = aggregate.getByIndexObject(rowIndex).get_object(attributes.getByIndexObject(columnIndex));
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}
		return result;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		aggregate.setByIndexObject(rowIndex)
	}

	public EAttribute getColumnAttribute(int columnIndex) throws SdaiException {
		int i = 0;
		it_attributes.beginning();
		while ((i <= columnIndex) && (it_attributes.next())) {
			i++;
		}
		return attributes.getCurrentMember(it_attributes);
//		return attributes.getByIndexObject(columnIndex);
	}
}
