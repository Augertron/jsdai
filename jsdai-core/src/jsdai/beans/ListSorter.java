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
import javax.swing.event.*;

import jsdai.lang.*;
import jsdai.util.*;
import jsdai.dictionary.*;


public class ListSorter extends AbstractListModel implements ComboBoxModel, ListDataListener {
	AggregateListModel model;
	int indexes[];
	boolean sorting = false;

	public ListSorter(AggregateListModel model) {
		setModel(model);
	}

	public void setModel(AggregateListModel model) {
		this.model = model;
		model.addListDataListener(this);
		reallocateIndexes();
	}

	public void setAggregate(Aggregate aggregate) throws SdaiException {
		model.setAggregate(aggregate);
		reallocateIndexes();
	}

	public Aggregate getAggregate() {
		return model.getAggregate();
	}

	public Object getElementAt(int index) {
		return (sorting)?model.getElementAt(indexes[index]):model.getElementAt(index);
	}

	public int getSize() {
		return model.getSize();
	}

	public void setSelectedItem(Object anObject) {
		model.setSelectedItem(anObject);
	}

	public Object getSelectedItem() {
		return model.getSelectedItem();
	}

	public void fireContentsChanged() throws SdaiException {
		model.fireContentsChanged();
		reallocateIndexes();
	}

	private void reallocateIndexes() {
		int count = getSize();
		indexes = new int[count];
		for (int row = 0; row < count; row++) {
			indexes[row] = row;
		}
		sort(sorting);
	}

	public void sort(boolean sorting) {
		this.sorting = sorting;
        if (sorting) {
			//sort();
		    shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
		}
	}

	private void sort() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = i+1; j < getSize(); j++) {
				if (compareUsingIndex(i, j) == -1) {
					swap(i, j);
				}
			}
		}
	}

    private int compare(int i, int j) {
		Object o1 = model.getElementAt(i);
		Object o2 = model.getElementAt(j);
		int result = SimpleOperations.getSdaiName(o2).compareToIgnoreCase(SimpleOperations.getSdaiName(o1));
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compareUsingIndex(int i, int j) {
		Object o1 = model.getElementAt(indexes[i]);
		Object o2 = model.getElementAt(indexes[j]);
		int result = SimpleOperations.getSdaiName(o2).compareToIgnoreCase(SimpleOperations.getSdaiName(o1));
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	private void swap(int i, int j) {
		int tmp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = tmp;
	}

	public void shuttlesort(int from[], int to[], int low, int high) {
		if (high - low < 2) {
		   return;
		}
		int middle = (low + high)/2;
		shuttlesort(to, from, low, middle);
		shuttlesort(to, from, middle, high);

		int p = low;
		int q = middle;

		if (high - low >= 4 && compare(from[middle-1], from[middle]) >= 0) {
			for (int i = low; i < high; i++) {
	   	   to[i] = from[i];
		   }
		   return;
		}

		for (int i = low; i < high; i++) {
		   if (q >= high || (p < middle && compare(from[p], from[q]) >= 0)) {
		      to[i] = from[p++];
		   }
		   else {
		      to[i] = from[q++];
		   }
		}
	}

	public void contentsChanged(ListDataEvent e) {
		reallocateIndexes();
		super.fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
	}

	public void intervalAdded(ListDataEvent e) {
		reallocateIndexes();
		super.fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
 	}

	public void intervalRemoved(ListDataEvent e) {
		reallocateIndexes();
		super.fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
	}

}

