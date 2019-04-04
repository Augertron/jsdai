package jsdai.tools.backup;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class GenericTTModelAdapter extends AbstractTableModel
    implements ListSelectionListener, TreeSelectionListener {
  protected JTree tree;
  protected JTable table;
  protected GenericTTModel treeTableModel;
  protected ListSelectionModel listModel;

  public GenericTTModelAdapter(GenericTTModel treeTableModel, JTree tree,
      ListSelectionModel listModel,
      JTable table) {
    this.tree = tree;
    this.table = table;
    this.treeTableModel = treeTableModel;
    this.listModel = listModel;

    tree.addTreeExpansionListener(new TreeExpansionListener() {
      // Don't use fireTableRowsInserted() here;
      // the selection model would get  updated twice.
      public void treeExpanded(TreeExpansionEvent event) {
        fireTableDataChanged();
      }

      public void treeCollapsed(TreeExpansionEvent event) {
        fireTableDataChanged();
      }
    });
    tree.addTreeWillExpandListener(treeTableModel);
  }

  // implementation of ListSelectionListener
  public void valueChanged(ListSelectionEvent e) {
    TreePath thePath = tree.getPathForRow(table.getSelectedRow());
    tree.getSelectionModel().setSelectionPath(thePath);
  }

  // implementation of TreeSelectionListener
  public void valueChanged(TreeSelectionEvent e) {
  }

  // Wrappers, implementing TableModel interface. 

  public int getColumnCount() {
    return treeTableModel.getColumnCount();
  }

  public String getColumnName(int column) {
    return treeTableModel.getColumnName(column);
  }

  public Class getColumnClass(int column) {
    return treeTableModel.getColumnClass(column);
  }

  public int getRowCount() {
    return tree.getRowCount();
  }

  protected Object nodeForRow(int row) {
    TreePath treePath = tree.getPathForRow(row);
    if (treePath == null) {
      return null;
    }
    return treePath.getLastPathComponent();
  }

  public Object getValueAt(int row, int column) {
    return treeTableModel.getValueAt(nodeForRow(row), column);
  }

  public boolean isCellEditable(int row, int column) {
    return treeTableModel.isCellEditable(nodeForRow(row), column);
  }

  public void setValueAt(Object value, int row, int column) {
    treeTableModel.setValueAt(value, nodeForRow(row), column);
  }
}

