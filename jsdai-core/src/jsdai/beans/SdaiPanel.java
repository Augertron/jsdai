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
import java.awt.event.*;
import java.awt.*;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

import jsdai.lang.*;
import jsdai.util.*;

public abstract class SdaiPanel extends JPanel implements SdaiSelectable, SdaiEditable {
	Object thisis = this;
	protected static final String newLine = "\r\n";
	private Vector selectableListeners = new Vector();
	private Vector editableListeners = new Vector();

	ArrayList goListeners = new ArrayList();

//global listeners
	protected MouseAdapter mouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				lastSelection = e.getSource();
				fireSdaiSelectionProcessed();
			}
		}
	};

	protected KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				fireSdaiSelectionProcessed();
			}
		}
	};

/*	protected class LocatorKeyListener extends KeyAdapter {
		String locator = "";
		public void keyPressed(KeyEvent e) {
			if (e.getSource() instanceof JComboBox) {
			    if (e.getKeyCode() == KeyEvent.VK_CANCEL) {
				    locator = "";
			    } else {
					JComboBox combo = (JComboBox)e.getSource();
	    			locator += e.getKeyChar();
					int old = combo.getSelectedIndex();
					for (int i = 0; i < combo.getModel().getSize(); i++) {
						String el = combo.getModel().getElementAt(i).toString();
						if (el.startsWith(locator)) {
							combo.setSelectedIndex(i);
							return;
						}
					}
				}
			}
		}
	}*/

	protected class Locator implements JComboBox.KeySelectionManager {
		final static int mode = 0;
		String locator = "";

		SdaiCellRenderer r = null;

		public Locator(SdaiCellRenderer r) {
			this.r = r;
		}

		public int selectionForKey(char aKey, ComboBoxModel aModel) {
		    try {
				if (mode == 0) {
					if (aKey == KeyEvent.VK_ESCAPE) {
						locator = "";
					} else {
						locator += aKey;
						for (int i = 0; i < aModel.getSize(); i++) {
							String el = r.getText(aModel.getElementAt(i)).toLowerCase();
							if (el.startsWith(locator.toLowerCase())) {
								return i;
							}
						}
					}
				}
				if (mode == 1) {
					for (int i = indexof(aModel, aModel.getSelectedItem())+1; i < aModel.getSize(); i++) {
						String el = r.getText(aModel.getElementAt(i)).toLowerCase();
						if (el.startsWith(locator.toLowerCase())) {
							return i;
						}
					}
					for (int i = 0; i < indexof(aModel, aModel.getSelectedItem()); i++) {
						String el = r.getText(aModel.getElementAt(i)).toLowerCase();
						if (el.startsWith(locator.toLowerCase())) {
							return i;
						}
					}
				}
			} catch (SdaiException ex) {}
			return -1;
		}

		private int indexof(ComboBoxModel model, Object value) {
			for (int i = 0; i < model.getSize(); i++) {
				if (model.getElementAt(i) == value) {
					return i;
				}
			}
			return 0;
		}
	}

	protected FocusListener focusListener = new FocusListener() {
		Border border = null;
		public void focusGained(FocusEvent e) {
			lastSelection = e.getSource();
			if (lastSelection instanceof JTextField) {
				JTextField tf = (JTextField)lastSelection;
				border = tf.getBorder();
				tf.setBorder(new EtchedBorder(Color.blue, Color.darkGray));
			}
			fireSdaiSelectionChanged();
		}
		public void focusLost(FocusEvent e) {
			if (lastSelection instanceof JTextField) {
				JTextField tf = (JTextField)lastSelection;
				tf.setBorder(border);
			}
			fireSdaiSelectionChanged();
		}
	};

	protected SdaiSelectableAdapter selectableListener = new SdaiSelectableAdapter() {
		public void sdaiSelectionChanged(SdaiSelectable selectable) {
			lastSelection = selectable;
			fireSdaiSelectionChanged();
		}
		public void sdaiSelectionProcessed(SdaiSelectable selectable) {
			fireSdaiSelectionProcessed();
		}
	};

	protected ListSelectionListener listListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			fireSdaiSelectionChanged();
		}
	};

	protected ItemListener itemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			lastSelection = e.getSource();
			fireSdaiSelectionChanged();
		}
	};

	protected ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lastSelection = e.getSource();
			fireSdaiSelectionProcessed();
		}
	};

	public String getTreeLeave() throws SdaiException {
		return "/";
	}

	public int getMode() throws SdaiException {
		return -1;
	}
	public void getSelected(Vector result) throws SdaiException {
	}
	public void setSelected(Vector agg) throws SdaiException {
	}
	public boolean isSelected() throws SdaiException {
		return false;
	}

	public void addSdaiSelectableListener(SdaiSelectableListener l) {
		selectableListeners.add(l);
	}

	public void removeSdaiSelectableListener(SdaiSelectableListener l) {
		selectableListeners.remove(l);
	}

	public void fireSdaiSelectionChanged() {
		for (int i = 0; i < selectableListeners.size(); i++) {
			((SdaiSelectableListener)selectableListeners.elementAt(i)).sdaiSelectionChanged(this);
		}
	}

	public void fireSdaiSelectionProcessed() {
		if (this.isEdit()) {
//			JOptionPane.showMessageDialog(null, "First you must go out from edit mode!", "Error", JOptionPane.ERROR_MESSAGE);
            showJOptionPaneMessage(null, "First you must go out from edit mode!", "Error", new Integer(JOptionPane.ERROR_MESSAGE));
			return;
		}
		for (int i = 0; i < selectableListeners.size(); i++) {
			((SdaiSelectableListener)selectableListeners.elementAt(i)).sdaiSelectionProcessed(this);
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
        } else {
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

	public void addSdaiEditableListener(SdaiEditableListener l) {
		editableListeners.add(l);
	}

	public void removeSdaiEditableListener(SdaiEditableListener l) {
		editableListeners.remove(l);
	}

 	public void fireSdaiEditableChanged() {
		for (int i = 0; i < editableListeners.size(); i++) {
			((SdaiEditableListener)editableListeners.elementAt(i)).sdaiEditableChanged(this);
		}
 	}

	public boolean isEdit() {
		return sdaiEdit;
	}

	public void sdaiEdit() throws SdaiException {
		sdaiEdit = true;
		fireSdaiEditableChanged();
	}

	public void sdaiAccept() throws SdaiException {
		sdaiEdit = false;
		fireSdaiEditableChanged();
	}

	public void sdaiCancel() throws SdaiException {
		sdaiEdit = false;
		fireSdaiEditableChanged();
	}

	public void sdaiNew() throws SdaiException {
	}
	public void sdaiDestroy() throws SdaiException {
	}

	protected Object getClipboardElement() throws SdaiException {
		return getClipboardElement(0);
	}

	protected Object getClipboardElement(int index) throws SdaiException {
		Aggregate agg = SdaiSession.getSession().getClipboard();
		if (agg.getMemberCount()-index > 0) {
			return agg.getByIndexObject(agg.getMemberCount()-index);
		} else {
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
			GoListener listener = (GoListener)goListeners.get(i);
			listener.goPerformed(e);
		}
	}

    /**
     * This method is called by ChainElement.getChain
     * It should be abstract but just few panel implement it
     */
    public void pushChainElementValues(java.util.List list) throws SdaiException { }
    
    /**
     * This method is called by ChainElement.setChain
     * It should be abstract but just few panel implement it
     */
    public void popChainElementValues(java.util.List list) throws SdaiException { }
    
}

