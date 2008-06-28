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
import jsdai.lang.*;
import java.awt.*;
import jsdai.util.*;

public class RepositoryTableModel extends AbstractTableModel {
	SdaiIterator it_active, it_known;
	ASdaiRepository active, known;
	String[] names = {"Active", "Modified", "Name", "Location"};
	Class[] clases = {Boolean.class, Boolean.class, String.class, String.class};

	String location = "";

	public RepositoryTableModel() {
	}

//	SdaiListener sdaiListener = new SdaiListener() {
//		public void actionPerformed(SdaiEvent e) {
//Debug.println("AAAAAAAAA");
//		   try {
//				switch (e.getId()) {
//					case SdaiEvent.INVALID :
//						setSession(null);
//						break;
//					case SdaiEvent.MODIFIED :
//						fireTableDataChanged();
//						break;
//				}
//		   } catch (SdaiException ex) { }
//		}
//	};

	public void setSession(SdaiSession session) throws SdaiException {
		location = session.getRepositoriesPath();
		known = session.getKnownServers();
//		known.addSdaiListener(sdaiListener);
		it_known = known.createIterator();
		active = session.getActiveServers();
//		active.addSdaiListener(sdaiListener);
		it_active = active.createIterator();
		fireTableDataChanged();
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
		int result = 0;
		try {
			if (known != null) result = known.getMemberCount();
		} catch (SdaiException e) {
//			SdaiEdit.processMessage(e);
		}
		return result;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		try {
			int i = 0;
			it_known.beginning();
			while ((i <= rowIndex) && (it_known.next())) {i++;}
			switch (columnIndex) {
				case 0 :
					it_active.beginning();
					result = Boolean.FALSE;
					while (it_active.next()) {
						if (known.getCurrentMember(it_known) == active.getCurrentMember(it_active))
							result = Boolean.TRUE;
					}
					break;
				case 1 :
					result = Boolean.valueOf((known.getCurrentMember(it_known).isActive())?known.getCurrentMember(it_known).isModified():false);
					break;
				case 2 :
					result = known.getCurrentMember(it_known).getName();
					break;
				case 3 :
				    String tmp = known.getCurrentMember(it_known).getLocation();
				    if ((tmp != null) && (tmp.indexOf(location) != -1)) {
						result = "<default>"+tmp.substring(tmp.indexOf(location)+location.length());
				    } else {
						result = tmp;
				    }
					break;
				default : result = known.getCurrentMember(it_known);
			}
		} catch (SdaiException e) {
//			SdaiEdit.processMessage(e);
		}
		return result;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
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

