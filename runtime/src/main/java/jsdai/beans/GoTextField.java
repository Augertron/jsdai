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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jsdai.lang.EEntity;

public class GoTextField extends JTextField {
  private static final long serialVersionUID = 1L;
  EEntity entity = null;
  Object link = null;

  boolean isUnderlineOn = false;
  protected static final Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);

  ArrayList goListeners = new ArrayList();

  public GoTextField() {
    super();
    border = getBorder();

    addMouseListener(mouseListener);
    addKeyListener(keyListener);

    BoundedRangeModel xbound = getHorizontalVisibility();
    xbound.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        BoundedRangeModel bound = (BoundedRangeModel) e.getSource();
        if (!hasFocus()) {
          bound.setValue(0);
        }
      }
    });

  }

  public EEntity getEEntity() {
    return entity;
  }

  public void setEEntity(EEntity entity) {
    this.entity = entity;
    setText((entity == null) ? "" : entity.toString());
    link = entity;
  }

  public void setLink(Object link) {
    entity = null;
    this.link = link;
  }

  boolean isBorderOn = false;
  Border border;
  Border newBorder = new EtchedBorder(Color.blue, Color.darkGray);

  public void paint(Graphics g) {
    if (isBorderOn) {
      setBorder(newBorder);
    }
    else {
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
      if (canUnderly() && ((e.getModifiers() & InputEvent.BUTTON3_MASK) == 0)) {
        fireGo(new GoEvent(this, link, null));
      }
    }
  };

  protected KeyAdapter keyListener = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      if (canUnderly() && (e.getKeyCode() == KeyEvent.VK_ENTER)) {
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
      GoListener listener = (GoListener) goListeners.get(i);
      listener.goPerformed(e);
    }
  }

  private boolean canUnderly() {
    return ((entity != null) || (link != null)) && isUnderlineOn && !isEditable();
  }
}
