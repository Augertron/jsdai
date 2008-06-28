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

import jsdai.dictionary.*;
import jsdai.lang.*;
import jsdai.util.*;

public class GoLabel extends JLabel {
	Object link = null;

	boolean isUnderlineOn = false;
	protected static final Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);

	ArrayList goListeners = new ArrayList();

	public GoLabel() {
		super();
		setBorder(new EtchedBorder(getBackground(), getBackground()));
		border = getBorder();
		addMouseListener(mouseListener);
	}

	public GoLabel(String s) {
		super(s);
		setBorder(new EtchedBorder(getBackground(), getBackground()));
		border = getBorder();
		addMouseListener(mouseListener);
	}

	public void setLink(Object link) {
	    this.link = link;
	}

	boolean isBorderOn = false;
	Border border;
	Border newBorder = new EtchedBorder(Color.blue, Color.darkGray);

	public void paint(Graphics g) {
		if (isBorderOn) {
			setBorder(newBorder);
		} else {
			setBorder(border);
		}
		super.paint(g);
	}

	void turnBorderOn(boolean aFlag) {
		isBorderOn = aFlag;
		revalidate();
		repaint();
	}


	public void setUnderlying(boolean flag) {
		isUnderlineOn = flag;
	}

	MouseListener mouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			if (canUnderly()) {
				turnBorderOn(true);
				setCursor(cursorHand);
			}
		}

		public void mouseExited(MouseEvent e) {
			turnBorderOn(false);
			setCursor(Cursor.getDefaultCursor());
		}

		public void mousePressed(MouseEvent e) {
			if (canUnderly() && ((e.getModifiers() & e.BUTTON3_MASK) == 0)) {
				fireGo(new GoEvent(this, link, null));
			}
		}
	};

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

	private boolean canUnderly() {
		return (link != null) && isUnderlineOn;
	}
}
