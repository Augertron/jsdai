package jsdai.tools.backup;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class GenericTreeTable extends JTable {

  //protected GenericTTCellRenderer tree;
  protected JTree tree;
  protected JPopupMenu rclickMenu = null;

  public GenericTreeTable() {
    super();
    ToolTipManager.sharedInstance().registerComponent(this);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // create internal, dumb model:
    GenericTTModel m = new DefaultGenericTTModel();
    init(m, null);
  }

  public void addRClickMenu(JPopupMenu menu) {
    this.rclickMenu = menu;
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
          // check, if any particular row is selected. If yes,
          // then display a menu
          if (getSelectedNode() != null) {
            rclickMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
          }
        }
      }
    });
  }

  private void init(GenericTTModel model, JTree treeComponent) {
    // set highly customized renderer.
    if (treeComponent == null) {
      tree = new GenericTTCellRenderer(model);
      tree.setRootVisible(false);
      tree.setCellRenderer(new TTCellRenderer());
    }
    else {
      // we presume, all care was taken outside.
      tree = treeComponent;
      // except for the root object
      tree.setRootVisible(false);
    }
    //this.addMouseListener(tree);
    // Install a tableModel representing the visible rows in the tree.
    GenericTTModelAdapter adp = new GenericTTModelAdapter(model, tree, getSelectionModel(), this);
    super.setModel(adp);

    // Force the JTable and JTree to share their row selection models.
    tree.setSelectionModel(new DefaultTreeSelectionModel() {
      // Extend the implementation of the constructor, as if:
      /* public this() */ {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      }
    });
    tree.getSelectionModel().addTreeSelectionListener(adp);
    getSelectionModel().addListSelectionListener(adp);
    //setSelectionModel(
    // Make the tree and table row heights the same.
    tree.setRowHeight(getRowHeight());

    // Install the tree editor renderer and editor.
    if (tree instanceof TableCellRenderer) {
      setDefaultRenderer(GenericTTModel.class, (TableCellRenderer) tree);
    }
    else {
      throw new IllegalStateException("Given tree instance does not implement TableCellRenderer!");
    }

    setDefaultEditor(GenericTTModel.class, new TreeTableCellEditor());

    setShowHorizontalLines(false);
    setShowVerticalLines(true);

    //setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    //setIntercellSpacing(new Dimension(0, 0));

    if (tree.getRowHeight() < 1) {
      super.setRowHeight(18);
    }
  }

  public JTree getTree() {
    return tree;
  }

  public void setRowHeight(int h) {
    if (tree != null) {
      tree.setRowHeight(h);
    }
    super.setRowHeight(h);
  }

  public void expandRoot(MutableTreeNode root) {
    tree.expandPath(new TreePath(root));
  }

  public TreeCellRenderer getTreeCellRenderer() {
    return tree.getCellRenderer();
  }

  public void setTreeCellRenderer(TreeCellRenderer renderer) {
    tree.setCellRenderer(renderer);
  }

  public Object getSelectedNode() {
    // go to the tree component, and find out, what's the last selection:
    return tree.getLastSelectedPathComponent();
  }

  public void setSelectionPath(TreePath newPath) {
    tree.setSelectionPath(newPath);
  }

  public TreePath getSelectionPath() {
    return tree.getSelectionPath();
  }

  public GenericTreeTable(GenericTTModel model) {
    super();
    init(model, null);
  }

  public GenericTreeTable(GenericTTModel model, JTree treeComponent) {
    super();
    init(model, treeComponent);
  }

  public void expandRow(int rowNo) {
    tree.expandRow(rowNo);
  }

  public void setModel(GenericTTModel model, GenericTTModel oldModel) {
    tree.removeTreeWillExpandListener(oldModel);
    tree.setModel(model);
    super.setModel(new GenericTTModelAdapter(model, tree, getSelectionModel(), this));
    //fireTableStructureChanged();
  }

  public void setModel(GenericTTModel model) {
    tree.setModel(model);
    super.setModel(new GenericTTModelAdapter(model, tree, getSelectionModel(), this));
    //fireTableStructureChanged();
  }

  public GenericTTModel getTreeModel() {
    return (GenericTTModel) tree.getModel();
  }

  public JToolTip createToolTip() {
    MultiLineToolTip tip = new MultiLineToolTip();
    tip.setComponent(this);
    return tip;
  }

  public class GenericTTCellRenderer extends JTree implements
      TableCellRenderer {
    protected int visibleRow;

    public GenericTTCellRenderer(TreeModel model) {
      super(model);
      this.putClientProperty("JTree.lineStyle", "Angled");
      setShowsRootHandles(true);
    }

    public void updateUI() {
      super.updateUI();
      TreeCellRenderer tcr = getCellRenderer();
      if (tcr instanceof DefaultTreeCellRenderer) {
        DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);

        dtcr.setTextSelectionColor(UIManager.getColor
            ("Table.selectionForeground"));

        dtcr.setBackgroundSelectionColor(UIManager.getColor
            ("Table.selectionBackground"));
      }
    }

    public void setRowHeight(int rowHeight) {
      if (rowHeight > 0) {
        super.setRowHeight(rowHeight);

        if (GenericTreeTable.this != null &&
            GenericTreeTable.this.getRowHeight() != rowHeight) {
          GenericTreeTable.this.setRowHeight(GenericTreeTable.this.getRowHeight());
        }
      }
    }

		/*
        public void setBounds(int x, int y, int w, int h)
		{
			super.setBounds(x, 0, w, GenericTreeTable.this.getHeight());
		}
        */

    public void paint(Graphics g) {
      g.translate(0, -visibleRow * GenericTreeTable.this.getRowHeight());
      super.paint(g);
    }

    public Component getTableCellRendererComponent(JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row, int column) {
      if (isSelected) {
        this.setBackground(table.getSelectionBackground());
      }
      else {
        this.setBackground(table.getBackground());
      }

      visibleRow = row;

      return this;
    }
  }

  public class TreeTableCellEditor extends DefaultCellEditor {
    public TreeTableCellEditor() {
      super(new JTextField());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int r, int c) {
      return tree;
    }

    public boolean isCellEditable(EventObject e) {
      if (e instanceof MouseEvent) {
        for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
          if (getColumnClass(counter) == GenericTTModel.class) {
            MouseEvent me = (MouseEvent) e;
            MouseEvent newME = new MouseEvent(tree, me.getID(),
                me.getWhen(), me.getModifiers(),
                me.getX() - getCellRect(0, counter, true).x,
                me.getY(), me.getClickCount(),
                me.isPopupTrigger());
            tree.dispatchEvent(newME);
            break;
          }
        }
      }
      return false;
    }

  }

  class TTCellRenderer extends DefaultTreeCellRenderer {
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {
      super.getTreeCellRendererComponent(
          tree, value, sel,
          expanded, leaf, row,
          hasFocus);

      setIcon(null);
      this.setToolTipText(String.valueOf(value));
      return this;
    }
  }

  class MyTableCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      this.setToolTipText(String.valueOf(value));
      return this;
    }

  }

  class MultiLineToolTip extends JToolTip {
    MultiLineToolTip() {
      //setUI(new MultiLineToolTipUI());
    }
  }

  class TreeTableSelectionModel extends DefaultTreeSelectionModel {
    public TreeTableSelectionModel(ListSelectionModel listSelectionModel) {
      this.listSelectionModel = (DefaultListSelectionModel) listSelectionModel;
    }
  }
}
