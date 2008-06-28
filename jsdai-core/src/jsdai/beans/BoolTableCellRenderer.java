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
import java.awt.*;

public class BoolTableCellRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JCheckBox cb = new JCheckBox("", value != null ?
									 Boolean.valueOf(value.toString()).booleanValue() : false);
		cb.setHorizontalAlignment(JCheckBox.CENTER);
//		cb.setBorder((hasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
		if (isSelected) {
			cb.setBackground(table.getSelectionBackground());
			cb.setForeground(table.getSelectionForeground());
		}
		else {
			cb.setBackground(table.getBackground());
			cb.setForeground(table.getForeground());
		}
		return cb;
	}
}
