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
import javax.swing.event.ListDataListener;
import jsdai.lang.*;
import jsdai.util.*;
import jsdai.dictionary.*;

/**The AggregateListModel provides a JList and JComboBox contents. Contens are jsdai.lang.Aggregate and supports all functionality from it.*/
public class AggregateListModel extends AbstractListModel implements ComboBoxModel {
	Aggregate aggregate = null;

	public AggregateListModel() {
	}

	public AggregateListModel(Aggregate aggregate) throws SdaiException {
		setAggregate(aggregate);
	}

	SdaiListener sdaiListener = new SdaiListener() {
		public void actionPerformed(SdaiEvent e) {
		   try {
				switch (e.getId()) {
					case SdaiEvent.INVALID :
						setAggregate(null);
						break;
					case SdaiEvent.MODIFIED :
						fireContentsChanged();
						break;
				}
		   } catch (SdaiException ex) { }
		}
	};

	public void setAggregate(Aggregate aggregate) throws SdaiException {
		if (this.aggregate != null) this.aggregate.removeSdaiListener(sdaiListener);
		aggregate.addSdaiListener(sdaiListener);
		this.aggregate = aggregate;
		if (aggregate != null) {
			selectedItem = aggregate.createIterator();
			selectedItem.next();
			fireContentsChanged(this, 0, aggregate.getMemberCount()-1);
		} else {
			fireContentsChanged(this, -1, -1);
		}
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

	public Object getElementAt(int index) {
		Object result = null;
		try {
			if (aggregate == null) {
				result = "";
			} else {
				EAggregation_type at = aggregate.getAggregationType();
				if (at == null) {
					int i;
					SdaiIterator it = aggregate.createIterator();
					for (i = 0; i <= index; i++) {
						if (!it.next()) return null;
					}
					result = aggregate.getCurrentMemberObject(it);
				} else {
					if (at.testElement_type(null)) {
						result = SimpleOperations.getElementObject(aggregate, SimpleOperations.correctAggregateIndex(index, at, 0));
					} else {//instanceof SessionAggregate
						if (aggregate instanceof A_string) {
							result = ((A_string)aggregate).getByIndex(index+1);
						} else {
							int i;
							SdaiIterator it = aggregate.createIterator();
							for (i = 0; i <= index; i++) {
								it.next();
							}
							result = aggregate.getCurrentMemberObject(it);
						}
					}
				}
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getSize() {
		int result = 0;
		try {
			if (aggregate != null) {
				result = aggregate.getMemberCount();
			}
		} catch (SdaiException e) {
//			e.printStackTrace();
		}
		return result;
	}

	public void fireContentsChanged() throws SdaiException {
		selectedItem = aggregate.createIterator();
		fireContentsChanged(this, 0, getSize());
	}
	
	public void fireRemoved(int index) {
		fireIntervalRemoved(this, index, index);
	}

//ComboBoxModel
	SdaiIterator selectedItem;

	public void setSelectedItem(Object anObject) {
		try {
			selectedItem.beginning();
			while (selectedItem.next()) {
				if (aggregate.getCurrentMemberObject(selectedItem) == anObject) {
					return;
				}
			}
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}
	}

	public Object getSelectedItem() {
		Object result = null;
		try {
			if (selectedItem != null) {
				result = aggregate.getCurrentMemberObject(selectedItem);
			} else {
				result = null;
			}
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}
		return result;
	}

//	public void addListDataListener(ListDataListener l) {
//Debug.println("adding");
//		super.addListDataListener(l);
//	}
//
//	public void removeListDataListener(ListDataListener l) {
//Debug.println("removing");
//		super.removeListDataListener(l);
//	}
//
//	private void printListeners() {
//Debug.println("Listeners");
//		Object listeners[] = listenerList.getListenerList();
//		for (int i = 0; i < listeners.length; i++) {
//Debug.println("listn "+i+"  "+listeners[i]);
//		}
//	}
}
