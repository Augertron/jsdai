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

import java.util.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;

import jsdai.lang.*;
import jsdai.util.*;
import jsdai.dictionary.*;

/**The NodesListModel provides a JList and JComboBox contents. Contens are jsdai.lang.Aggregate and supports all functionality from it.*/
public class NodesListModel extends AbstractListModel implements ComboBoxModel {
	Vector nodes = null;
	int selectedItem = 0;

	public NodesListModel() {
	}

	public NodesListModel(Vector nodes) {
		setNodes(nodes);
	}

	public void setNodes(Vector nodes) {
		this.nodes = nodes;
		if (nodes != null) {
			fireContentsChanged(this, 0, getSize()-1);
		} else {
			fireContentsChanged(this, -1, -1);
		}
	}

	public Vector getNodes() {
		return nodes;
	}

	public Object getElementAt(int index) {
		return ((nodes != null)?nodes.elementAt(index):null);
	}

	public int getSize() {
		return ((nodes != null)?nodes.size():0);
	}

	public void fireContentsChanged() {
		fireContentsChanged(this, 0, getSize()-1);
	}

//ComboBoxModel

	public void setSelectedItem(Object anObject) {
		if (nodes == null) return;
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.elementAt(i).equals(anObject)) {
				selectedItem = i;
			}
		}
	}

	public Object getSelectedItem() {
		return ((nodes != null)?nodes.elementAt(selectedItem):null);
	}


}
