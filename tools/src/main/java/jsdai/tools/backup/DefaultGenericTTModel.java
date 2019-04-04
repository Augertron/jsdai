package jsdai.tools.backup;

import javax.swing.table.*;
import javax.swing.tree.*;
import java.util.*;

public class DefaultGenericTTModel extends GenericTTModel {

  // Let's have one column
  protected static String[] colNames = { "ABISIN" };

  // Types of the columns.
  protected static Class[] colTypes = { GenericTTModel.class };

  public DefaultGenericTTModel() {
    super();
    // install my own column classes
    installColumns(colNames, colTypes, new boolean[] { true });
  }

  public Object getValueAt(Object node, int column) {
    return node;
  }

  public boolean isCellEditable(Object node, int column) {
    return true;
  }

  public void setValueAt(Object aValue, Object node, int column) {
  }

}
