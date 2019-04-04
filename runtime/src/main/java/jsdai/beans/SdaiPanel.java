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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jsdai.lang.Aggregate;
import jsdai.lang.MethodCallsCacheManager;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;

public abstract class SdaiPanel extends JPanel implements SdaiSelectable, SdaiEditable {
  private static final long serialVersionUID = 1L;

  private SdaiSession session = SdaiSession.getSession();

  Object thisis = this;
  protected static final String newLine = "\r\n";
  private final Vector selectableListeners = new Vector();
  private final Vector editableListeners = new Vector();

  ArrayList goListeners = new ArrayList();

  //global listeners
  protected MouseAdapter mouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        lastSelection = e.getSource();
        fireSdaiSelectionProcessed();
      }
    }
  };

  protected KeyAdapter keyListener = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        fireSdaiSelectionProcessed();
      }
    }
  };

  public SdaiSession getSession() {
    return session;
  }

  public void setSession(SdaiSession session) {
    this.session = session;
  }

  protected class Locator implements JComboBox.KeySelectionManager {
    String locator = "";

    SdaiCellRenderer r = null;

    public Locator(SdaiCellRenderer r) {
      this.r = r;
    }

    @Override
    public int selectionForKey(char aKey, ComboBoxModel aModel) {
      if (aKey == KeyEvent.VK_ESCAPE) {
        locator = "";
      }
      else {
        locator += aKey;
        for (int i = 0; i < aModel.getSize(); i++) {
          String el = r.getText(aModel.getElementAt(i)).toLowerCase();
          if (el.startsWith(locator.toLowerCase())) {
            return i;
          }
        }
      }
      return -1;
    }
  }

  protected FocusListener focusListener = new FocusListener() {
    Border border = null;

    @Override
    public void focusGained(FocusEvent e) {
      lastSelection = e.getSource();
      if (lastSelection instanceof JTextField) {
        JTextField tf = (JTextField) lastSelection;
        border = tf.getBorder();
        tf.setBorder(new EtchedBorder(Color.blue, Color.darkGray));
      }
      fireSdaiSelectionChanged();
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (lastSelection instanceof JTextField) {
        JTextField tf = (JTextField) lastSelection;
        tf.setBorder(border);
      }
      fireSdaiSelectionChanged();
    }
  };

  protected SdaiSelectableAdapter selectableListener = new SdaiSelectableAdapter() {
    @Override
    public void sdaiSelectionChanged(SdaiSelectable selectable) {
      lastSelection = selectable;
      fireSdaiSelectionChanged();
    }

    @Override
    public void sdaiSelectionProcessed(SdaiSelectable selectable) {
      fireSdaiSelectionProcessed();
    }
  };

  protected ListSelectionListener listListener = new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent e) {
      fireSdaiSelectionChanged();
    }
  };

  protected ItemListener itemListener = new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
      lastSelection = e.getSource();
      fireSdaiSelectionChanged();
    }
  };

  protected ActionListener actionListener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      lastSelection = e.getSource();
      fireSdaiSelectionProcessed();
    }
  };

  @Override
  public String getTreeLeave() throws SdaiException {
    return "/";
  }

  @Override
  public int getMode() throws SdaiException {
    return -1;
  }

  @Override
  public void getSelected(Vector result) throws SdaiException {
  }

  @Override
  public void setSelected(Vector agg) throws SdaiException {
  }

  @Override
  public boolean isSelected() throws SdaiException {
    return false;
  }

  @Override
  public void addSdaiSelectableListener(SdaiSelectableListener l) {
    selectableListeners.add(l);
  }

  @Override
  public void removeSdaiSelectableListener(SdaiSelectableListener l) {
    selectableListeners.remove(l);
  }

  @Override
  public void fireSdaiSelectionChanged() {
    for (int i = 0; i < selectableListeners.size(); i++) {
      ((SdaiSelectableListener) selectableListeners.elementAt(i)).sdaiSelectionChanged(this);
    }
  }

  @Override
  public void fireSdaiSelectionProcessed() {
    if (this.isEdit()) {
//			JOptionPane.showMessageDialog(null, "First you must go out from edit mode!", "Error", JOptionPane.ERROR_MESSAGE);
      showJOptionPaneMessage(null, "First you must go out from edit mode!", "Error", new Integer(JOptionPane.ERROR_MESSAGE));
      return;
    }
    for (int i = 0; i < selectableListeners.size(); i++) {
      ((SdaiSelectableListener) selectableListeners.elementAt(i)).sdaiSelectionProcessed(this);
    }
  }

  public void showJOptionPaneMessage(Component parent, String msg, String head, Integer type) {
    Component parent2 = (Component) MethodCallsCacheManager.getParent();
    if (parent2 != null) {
      Class[] types = new Class[4];
      types[0] = Component.class;
      types[1] = Object.class;
      types[2] = String.class;
      types[3] = Integer.TYPE;

      Object[] values = new Object[4];
      values[0] = parent2;
      values[1] = msg;
      values[2] = head;
      values[3] = type;

      MappingOperationsThreadManager.invokeMethod(null, JOptionPane.class, "showMessageDialog", types, values, false);
    }
    else {
      JOptionPane.showMessageDialog(parent, msg, head, type.intValue());
    }
  }

  protected void processMessage(Exception e) {
    e.printStackTrace();
    showJOptionPaneMessage(this, e.getMessage(), "Error", new Integer(JOptionPane.ERROR_MESSAGE));
  }

  protected void processMessage(String s) {
    showJOptionPaneMessage(null, s, "Attention!", new Integer(JOptionPane.WARNING_MESSAGE));
  }

  protected boolean sdaiEdit = false;
  protected Object lastSelection;

  @Override
  public void addSdaiEditableListener(SdaiEditableListener l) {
    editableListeners.add(l);
  }

  @Override
  public void removeSdaiEditableListener(SdaiEditableListener l) {
    editableListeners.remove(l);
  }

  @Override
  public void fireSdaiEditableChanged() {
    for (int i = 0; i < editableListeners.size(); i++) {
      ((SdaiEditableListener) editableListeners.elementAt(i)).sdaiEditableChanged(this);
    }
  }

  @Override
  public boolean isEdit() {
    return sdaiEdit;
  }

  @Override
  public void sdaiEdit() throws SdaiException {
    sdaiEdit = true;
    fireSdaiEditableChanged();
  }

  @Override
  public void sdaiAccept() throws SdaiException {
    sdaiEdit = false;
    fireSdaiEditableChanged();
  }

  @Override
  public void sdaiCancel() throws SdaiException {
    sdaiEdit = false;
    fireSdaiEditableChanged();
  }

  @Override
  public void sdaiNew() throws SdaiException {
  }

  @Override
  public void sdaiDestroy() throws SdaiException {
  }

  protected Object getClipboardElement() throws SdaiException {
    return getClipboardElement(0);
  }

  protected Object getClipboardElement(int index) throws SdaiException {
    Aggregate agg = SdaiSession.getSession().getClipboard();
    if (agg.getMemberCount() - index > 0) {
      return agg.getByIndexObject(agg.getMemberCount() - index);
    }
    else {
      return null;
    }
  }

  synchronized protected void setHourGlass(boolean visability) {
//		getRootPane().getContentPane().setEnabled(!visability);
    getRootPane().getGlassPane().setVisible(visability);
  }

  synchronized public void refreshData() {
    processMessage("Refresh data not implemented on this page.");
  }

  public void setProperties(Properties props) {
  }

  public void getProperties(Properties props) {
  }

  public String copyContentsAsText() {
    processMessage("Copy all not implemented on this page.");
    return "";
  }

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

  /**
   * This method is called by ChainElement.getChain
   * It should be abstract but just few panel implement it
   *
   * @param list
   */
  public void pushChainElementValues(java.util.List list) throws SdaiException {
  }

  /**
   * This method is called by ChainElement.setChain
   * It should be abstract but just few panel implement it
   *
   * @param list
   */
  public void popChainElementValues(java.util.List list) throws SdaiException {
  }

}
