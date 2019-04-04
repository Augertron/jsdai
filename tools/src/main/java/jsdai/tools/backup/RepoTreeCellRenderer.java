/**
 * Author : Kazimieras Vaina
 * Date: 2003.6.18
 * Time: 16.40.46
 */
package jsdai.tools.backup;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.*;
import java.awt.*;

public class RepoTreeCellRenderer extends DefaultTreeCellRenderer {

  private GenericTreeTable treetable;

  public RepoTreeCellRenderer(GenericTreeTable tree) {
    super();
    this.treetable = tree;
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
      boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    GenericTTModel model = treetable.getTreeModel();
    Object selection = treetable.getSelectedNode();
    if (selection != null) {
      setToolTipText(model.getValueAt(selection, 1).toString());
    }
    else {
      setToolTipText(null);
    }
    setIcon(null);
    return this;
  }
}
