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

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class GoList extends JList {
	ArrayList goListeners = new ArrayList();

	boolean isUnderlineOn = false;
	int underlyingRow = -1;

	static final int SINGLE_CLICK = 1;
	static final int DOUBLE_CLICK = 2;
	int clickCount = DOUBLE_CLICK;

	protected static final Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);

	public GoList() {
		super();
		addKeyListener(findKeyListener);
		setFixedCellHeight(17);
		setFixedCellWidth(170);
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
		addMouseMotionListener(mouseMotionListener);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public GoList(ListModel model) {
		super(model);
		addKeyListener(findKeyListener);
		setFixedCellHeight(17);
		setFixedCellWidth(170);
		addMouseListener(mouseListener);
		addKeyListener(keyListener);
		addMouseMotionListener(mouseMotionListener);
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
		if (underlyingRow != -1) {
			requestFocus();
			Rectangle r = getCellBounds(underlyingRow, underlyingRow);
		    if (r != null) {
				repaint(
					(int)r.getX(),
					(int)r.getY(),
					(int)r.getMaxX(),
					(int)r.getMaxY()
				);
			}
		}
	    underlyingRow = i;
		if (i != -1) {
			Graphics2D g2 = (Graphics2D)getGraphics();
			g2.setStroke(new BasicStroke(1));
			g2.setPaint(Color.blue);
		    Rectangle r = getCellBounds(i, i);
			g2.drawRect(
				(int)r.getX(),
				(int)r.getY(),
				(int)r.getMaxX()-1,
				(int)r.getMaxY()-(int)r.getY()-2
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
				if ((clickCount == SINGLE_CLICK) && (underlyingRow != -1)) {
				    fireGo(new GoEvent(this, getModel().getElementAt(underlyingRow), null));
				}
				timer.stop();
			}
		});

		public void mousePressed(MouseEvent e) {
			if (((e.getModifiers() & e.BUTTON3_MASK) == 0) && timer.isRunning() && (row == getSelectedIndex())) {
				timer.stop();
				if (clickCount == DOUBLE_CLICK) {
				    fireGo(new GoEvent(this, getSelectedValue(), null));
				}
			} else {
				timer.restart();
				row = getSelectedIndex();
			}
	    }
	};

	protected KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_ENTER) && !isSelectionEmpty()) {
				fireGo(new GoEvent(this, getSelectedValue(), null));
			}
		}
	};

	int mouseMotionTimerIndex = -1;
	javax.swing.Timer mouseMotionTimer = new javax.swing.Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(underlyingRow == mouseMotionTimerIndex)
				setSelectedIndex(mouseMotionTimerIndex);
		}
	});
	
	MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {

		public void mouseMoved(MouseEvent e) {
			int newIndex = locationToIndex(e.getPoint());
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
		    Rectangle r = getCellBounds(underlyingRow, underlyingRow);
			if (r != null) {
			    g2.drawRect(
					(int)r.getX(),
					(int)r.getY(),
					(int)r.getMaxX()-1,
					(int)r.getMaxY()-(int)r.getY()-2
				);
			}
		}
	}

	String search_value = "";

	protected KeyAdapter findKeyListener = new KeyAdapter() {
		public void keyReleased(KeyEvent e) {
			JComponent c = (JComponent)e.getSource();
			if ((e.getKeyCode() == KeyEvent.VK_F) && e.isControlDown()) {
				search_value = (String)JOptionPane.showInputDialog(c, null, "Find", JOptionPane.PLAIN_MESSAGE, null, null, search_value);
				if ((search_value != null) && !search_value.equals("")) {
					searchList((JList)c, search_value);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_F3) {
			    if ((search_value == null) || search_value.equals("")) {
				    search_value = JOptionPane.showInputDialog(c, null, "Find", JOptionPane.PLAIN_MESSAGE);
				    if ((search_value != null) && !search_value.equals("")) {
    					searchList((JList)c, search_value);
	    			}
				} else {
					searchList((JList)c, search_value);
				}
			}
		}
	};

	void searchList(JList list, String search) {
		int i = list.getSelectedIndex();
		while ((i < list.getModel().getSize()-1)) {
			i++;

			String value = ((JLabel)list.getCellRenderer().getListCellRendererComponent(list, list.getModel().getElementAt(i), i, false, false)).getText();
			if ((value != null)?value.toLowerCase().indexOf(search.toLowerCase()) != -1:false) {
				list.setSelectedIndex(i);
				list.ensureIndexIsVisible(i);
				return;
			}
		}
		JOptionPane.showMessageDialog(list, "No occurrance of \""+search_value+"\" found.");
	}

}
