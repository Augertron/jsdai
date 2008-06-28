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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jsdai.dictionary.*;
import jsdai.lang.*;
import jsdai.util.*;

public class GoTable extends JTable {
	int returnCount = 1;

	ArrayList goListeners = new ArrayList();

	boolean isUnderlineOn = false;
	int underlyingRow = -1;

	static final int SINGLE_CLICK = 1;
	static final int DOUBLE_CLICK = 2;
	int clickCount = DOUBLE_CLICK;

	protected static final Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);

	public GoTable() {
		super();
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
		addKeyListener(findKeyListener);
		addMouseMotionListener(mouseMotionListener);
		getTableHeader().setReorderingAllowed(false);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public GoTable(TableModel model) {
		super(model);
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
		addKeyListener(findKeyListener);
		addMouseMotionListener(mouseMotionListener);
		getTableHeader().setReorderingAllowed(false);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void setUnderlying(boolean flag) {
		if (flag) {
			clickCount = SINGLE_CLICK;
		    setCursor(cursorHand);
		} else {
			clickCount = DOUBLE_CLICK;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		isUnderlineOn = flag;
	}

	public void addGoListener(GoListener listener) {
		goListeners.add(listener);
	}

	public void removeGoListener(GoListener listener) {
		goListeners.remove(listener);
	}

	public void fireGo(GoEvent e) {
		for (int i = 0; i < goListeners.size(); i++) {
			GoListener listener = (GoListener)goListeners.get(i);
			listener.goPerformed(e);
		}
	}

	void setUnderlyingRow(int i) {
		if ((i == underlyingRow) || !isUnderlineOn) return;
		repaint(
			(int)getCellRect(underlyingRow, 0, false).getX(),
			(int)getCellRect(underlyingRow, 0, false).getY(),
			(int)getCellRect(underlyingRow, getColumnCount()-1, false).getMaxX(),
			(int)getRowHeight()
		);
	    underlyingRow = i;
		if (i != -1) {
			requestFocus();
			Graphics2D g2 = (Graphics2D)getGraphics();
			g2.setStroke(new BasicStroke(1));
			g2.setPaint(Color.blue);
			g2.drawRect(
				(int)getCellRect(underlyingRow, 0, false).getX(),
				(int)getCellRect(underlyingRow, 0, false).getY(),
				(int)getCellRect(underlyingRow, getColumnCount()-1, false).getMaxX()-1,
				(int)getRowHeight()-2
			);
		}
	}

	MouseListener mouseListener = new MouseAdapter() {
		public void mouseExited(MouseEvent e) {
			mouseMotionTimer.stop();
		    setUnderlyingRow(-1);
		}

		int row;
		final javax.swing.Timer timer = new javax.swing.Timer(500, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				if ((clickCount == SINGLE_CLICK) && (getSelectedRow() != -1)) {
				    fireGo(new GoEvent(this, getClickValue(getSelectedRow()), null));
				}
				timer.stop();
			}
		});

		public void mousePressed(MouseEvent e) {
			if (((e.getModifiers() & e.BUTTON3_MASK) == 0) && timer.isRunning() && (row == getSelectedRow()) && (getSelectedRow() != -1)) {
				timer.stop();
				if (clickCount == DOUBLE_CLICK) {
				    fireGo(new GoEvent(this, getClickValue(getSelectedRow()), null));
				}
			} else {
				timer.restart();
				row = getSelectedRow();
			}
	    }
	};

	protected KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (getSelectedRow() != -1)) {
				fireGo(new GoEvent(this, getClickValue(getSelectedRow()), null));
			}
		}
	};

	int mouseMotionTimerIndex = -1;
	javax.swing.Timer mouseMotionTimer = new javax.swing.Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(underlyingRow == mouseMotionTimerIndex) {
				if ((mouseMotionTimerIndex >= 0) && 
				    (mouseMotionTimerIndex < getRowCount())) {
					setRowSelectionInterval(mouseMotionTimerIndex, mouseMotionTimerIndex);
				}
			}
		}
	});
	
	MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {

		public void mouseMoved(MouseEvent e) {
			int newIndex = rowAtPoint(e.getPoint());
			if(newIndex != underlyingRow) {
				if(clickCount == SINGLE_CLICK) {
					mouseMotionTimerIndex = newIndex;
					mouseMotionTimer.setRepeats(false);
					mouseMotionTimer.restart();
				}
				setUnderlyingRow(newIndex);
			}
		}
	};


	public void paint(Graphics g) {
		super.paint(g);
		if ((underlyingRow != -1) && isUnderlineOn) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(1));
			g2.setPaint(Color.blue);
			g2.drawRect(
				(int)getCellRect(underlyingRow, 0, false).getX(),
				(int)getCellRect(underlyingRow, 0, false).getY(),
				(int)getCellRect(underlyingRow, getColumnCount()-1, false).getMaxX()-1,
				(int)getRowHeight()-2
			);
		}
	}

	public void setReturnCount(int count) {
		returnCount = count;
	}

	private Object getClickValue(int row) {
		if (returnCount == 1) {
			return getValueAt(getSelectedRow(), -1);
		} else {
			Object value[] = new Object[returnCount];
			for (int i = 0; i < returnCount; i++) {
				value[i] = getValueAt(getSelectedRow(), -(i+1));
Debug.println(""+value[i]);
			}
			return value;
		}
	}

	String search_value = "";

	protected KeyAdapter findKeyListener = new KeyAdapter() {
		public void keyReleased(KeyEvent e) {
			JComponent c = (JComponent)e.getSource();
			if ((e.getKeyCode() == KeyEvent.VK_F) && e.isControlDown()) {
				search_value = (String)JOptionPane.showInputDialog(c, null, "Find", JOptionPane.PLAIN_MESSAGE, null, null, search_value);
				if ((search_value != null) && !search_value.equals("")) {
					searchTable((JTable)c, search_value);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_F3) {
			    if ((search_value == null) || search_value.equals("")) {
				    search_value = JOptionPane.showInputDialog(c, null, "Find", JOptionPane.PLAIN_MESSAGE);
				    if ((search_value != null) && !search_value.equals("")) {
   					    searchTable((JTable)c, search_value);
	    			}
				} else {
				    searchTable((JTable)c, search_value);
				}
			}
		}
	};

	void searchTable(JTable table, String search) {
		int i = table.getSelectedRow();
		while ((i < table.getRowCount()-1)) {
			i++;
			for (int j = 0; j < table.getColumnCount(); j++) {
//				TableCellRenderer r = table.getCellRenderer(i, j);
				Object value = table.getValueAt(i, j);
				if ((value != null)?value.toString().toLowerCase().indexOf(search.toLowerCase()) != -1:false) {
					table.setRowSelectionInterval(i, i);
					table.scrollRectToVisible(table.getCellRect(i, j, true));
					return;
				}
			}
		}
		JOptionPane.showMessageDialog(table, "No occurrance of \""+search_value+"\" found.");
	}

}
