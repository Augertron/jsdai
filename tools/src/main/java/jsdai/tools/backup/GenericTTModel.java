package jsdai.tools.backup;

import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

/**
 * Class, created for being superclass in cases, when
 * guys want to create their own model for usage in
 * GenericTreeTable control.
 */
public abstract class GenericTTModel implements TreeModel, TreeWillExpandListener {

  private MutableTreeNode intlRoot;

  private DefaultTreeModel treePart;

  private Object[] columnNames;
  private Class[] columnTypes;
  private boolean[] columnProps;

  public GenericTTModel() {
    // create our own root object:
    intlRoot = new DefaultMutableTreeNode("");
    treePart = new DefaultTreeModel(intlRoot);
  }

  protected GenericTTModel(MutableTreeNode root) {
    this();
    intlRoot = root;
  }

  // TableModel implementation:

  public Class getColumnClass(int columnIndex) {
    if (columnTypes == null) {
      throw new NullPointerException("No installed columns found!");
    }
    else {
      return columnTypes[columnIndex];
    }
  }

  public int getColumnCount() {
    if (columnTypes == null) {
      throw new NullPointerException("No installed columns found!");
    }
    else {
      return columnTypes.length;
    }
  }

  public String getColumnName(int columnIndex) {
    if (columnNames == null) {
      throw new NullPointerException("No installed columns found!");
    }
    else {
      return String.valueOf(columnNames[columnIndex]);
    }
  }
 /*
 	public int getRowCount() {
 		// notice how we use here not a table part, but tree.
 		return treePart.getRowCount();
 	}
 	*/
 	/*
 	public Object getValueAt(int rowIndex, int columnIndex) {
 		throw new NullPointerException("No installed columns found!");
 	}
 	*/

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnProps == null) {
      throw new NullPointerException("No installed columns found!");
    }
    else {
      return columnProps[columnIndex];
    }
  }
 	
 	/*
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		//nodeForRow(row)
		//tablePart.setValueAt(aValue, rowIndex, columnIndex);
	}
	*/

  // TreeModel implementation

  public abstract Object getValueAt(Object node, int column);

  public abstract boolean isCellEditable(Object node, int column);

  public abstract void setValueAt(Object aValue, Object node, int column);

  public void addTreeModelListener(TreeModelListener l) {
    treePart.addTreeModelListener(l);
  }

  public Object getChild(Object parent, int index) {
    return treePart.getChild(parent, index);
  }

  public int getChildCount(Object parent) {
    return treePart.getChildCount(parent);
  }

  public int getIndexOfChild(Object parent, Object child) {
    return treePart.getIndexOfChild(parent, child);
  }

  public Object getRoot() {
    return intlRoot;
  }

  public boolean isLeaf(Object node) {
    return treePart.isLeaf(node);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    treePart.removeTreeModelListener(l);
  }

  public void valueForPathChanged(TreePath path, Object newValue) {
    treePart.valueForPathChanged(path, newValue);
  }

  // now, custom behavior:

  /**
   * Arrays of objects are duplicated, objects themselves not.
   *
   * @param colProps We support limited editing capability:
   *                 full column is either editable or not,
   *                 contrary to the default Java proposed
   *                 implementation.
   */
  protected void installColumns(Object[] colNames,
      Class[] colTypes,
      boolean[] colProps) {
    columnNames = new Object[colNames.length];
    for (int i = 0; i < colNames.length; i++) {
      columnNames[i] = colNames[i];
    }
    columnTypes = new Class[colTypes.length];
    for (int i = 0; i < colTypes.length; i++) {
      columnTypes[i] = colTypes[i];
    }
    columnProps = new boolean[colProps.length];
    for (int i = 0; i < colProps.length; i++) {
      columnProps[i] = colProps[i];
    }
  }

  protected DefaultTreeModel getIntlTreeModel() {
    return treePart;
  }

  public MutableTreeNode getIntlRoot() {
    return intlRoot;
  }
    
   /*
	private Object nodeForRow(int row) {
		TreePath treePath = treePart.getPathForRow(row);
		return treePath.getLastPathComponent();         
    }
*/

  public void treeWillCollapse(TreeExpansionEvent event) {
  }

  public void treeWillExpand(TreeExpansionEvent event) {
  }

}  