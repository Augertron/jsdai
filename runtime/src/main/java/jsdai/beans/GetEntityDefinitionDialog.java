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

import java.util.Vector;

/**
 * @author gedas
 */
public class GetEntityDefinitionDialog extends javax.swing.JDialog {
  private static final long serialVersionUID = 1L;
  private final Vector availableTypes;
  private int selIndex;

  /**
   * Creates new form GetEntityDefinitionDialog
   *
   * @param parent
   * @param types
   * @param modal
   */
  public GetEntityDefinitionDialog(java.awt.Frame parent, boolean modal, Vector types) {
    super(parent, modal);
    initComponents();
    availableTypes = types;
    selIndex = -1;
  }

  public int getSelIndex() {
    return selIndex;
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    jPanel = new javax.swing.JPanel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jPanel1 = new javax.swing.JPanel();
    entitiesList = new javax.swing.JList();

    setTitle("Select entity definition");
    setModal(true);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowOpened(java.awt.event.WindowEvent evt) {
        formWindowOpened(evt);
      }

      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });

    jPanel.setToolTipText("");
    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        onOK(evt);
      }
    });

    jPanel.add(okButton);

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        onCancel(evt);
      }
    });

    jPanel.add(cancelButton);

    getContentPane().add(jPanel, java.awt.BorderLayout.SOUTH);

    jScrollPane1.setToolTipText("");
    jScrollPane1.setPreferredSize(new java.awt.Dimension(622, 622));
    jScrollPane1.setMinimumSize(new java.awt.Dimension(222, 222));
    jPanel1.setLayout(new java.awt.BorderLayout());

    entitiesList.setBackground(new java.awt.Color(204, 204, 204));
    entitiesList.setVisibleRowCount(20);
    jPanel1.add(entitiesList, java.awt.BorderLayout.CENTER);

    jScrollPane1.setViewportView(jPanel1);

    getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

    pack();
  }//GEN-END:initComponents

  private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
    // Add your handling code here:
    selIndex = -1;
    closeDialog(null);
  }//GEN-LAST:event_onCancel

  private void onOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOK
    // Add your handling code here:
    selIndex = entitiesList.getSelectedIndex();
    closeDialog(null);
  }//GEN-LAST:event_onOK

  private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    entitiesList.setListData(availableTypes);
  }//GEN-LAST:event_formWindowOpened

  /**
   * Closes the dialog
   */
  private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
    setVisible(false);
    dispose();
  }//GEN-LAST:event_closeDialog

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton okButton;
  private javax.swing.JButton cancelButton;
  private javax.swing.JList entitiesList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JPanel jPanel;
  private javax.swing.JPanel jPanel1;
  // End of variables declaration//GEN-END:variables
}
