package jsdai.tools.backup;

//import jsdaix.util.swing.TreeTable.GenericTTModel;
//import jsdaix.wizard.client.WizardsHandler;
//import jsdaix.wizard.exchange.MetaDataRequest;

import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

public class RepositoriesTreeModel extends GenericTTModel {

  protected String[] colNames = { "Name", "Backup" };
  protected static Class[] colTypes = { GenericTTModel.class, Boolean.class };

  protected HashMap repositoriesInfo;
  protected HashMap schemasInfo;

  protected boolean includeSchemas;

  GenericTreeTable treeTable;

  RepositoryInfo[] reposInfo;

  String selectedRepoName;
  int selectedSICount;

  public RepositoriesTreeModel(RepositoryInfo[] reposInfo, boolean includeSchemas) {
    super();
    this.includeSchemas = includeSchemas;
    getIntlRoot().setUserObject(new String("Repositories"));

    installColumns(colNames, colTypes, new boolean[] { true });
    repositoriesInfo = new HashMap();
    schemasInfo = new HashMap();

    setRepositoryInfo(reposInfo, includeSchemas);
  }

  public void setRepositoryInfo(RepositoryInfo[] reposInfo, boolean includeSchemas) {
    this.reposInfo = reposInfo;
    selectedRepoName = "";
    selectedSICount = 0;
    for (int i = 0; i < reposInfo.length; i++) {
      DefaultMutableTreeNode theNewNode = new DefaultMutableTreeNode(reposInfo[i].name);
      repositoriesInfo.put(theNewNode, reposInfo[i]);
      getIntlTreeModel().insertNodeInto(theNewNode, getIntlRoot(), getChildCount(getIntlRoot()));
      if (includeSchemas) {
        SchemaInstanceInfo[] sisInfo = reposInfo[i].getSIInfo();
        if (sisInfo != null) {
          for (int j = 0; j < sisInfo.length; j++) {
            DefaultMutableTreeNode theNewChildNode = new DefaultMutableTreeNode(sisInfo[j].name);
            schemasInfo.put(theNewChildNode, sisInfo[j]);
            getIntlTreeModel().insertNodeInto(theNewChildNode, theNewNode, theNewNode.getChildCount());
          }
        }
      }
    }
  }

  public void setTreeTable(GenericTreeTable treeTable) {
    this.treeTable = treeTable;
  }

  public void setColumnNames(String[] colNames) {
    this.colNames = colNames;
    installColumns(colNames, colTypes, new boolean[] { true });
  }

  public void treeWillExpand(TreeExpansionEvent event) {
  }

  public Object getValueAt(Object node, int column) {
    DefaultMutableTreeNode theNode = (DefaultMutableTreeNode) node;
    if (column == 0) {
      if (theNode.getParent() == getIntlRoot()) {
        RepositoryInfo repoInfo = (RepositoryInfo) repositoriesInfo.get(theNode);
        return repoInfo.name;
      }
      else {
        SchemaInstanceInfo siInfo = (SchemaInstanceInfo) schemasInfo.get(theNode);
        return siInfo.name;
      }
    }
    if (column == RepositoryInfo.COLUMN_BACKUP) {
      if (theNode.getParent() == getIntlRoot()) {
        RepositoryInfo repoInfo = (RepositoryInfo) repositoriesInfo.get(theNode);
        return repoInfo.backup;
      }
      else {
        SchemaInstanceInfo siInfo = (SchemaInstanceInfo) schemasInfo.get(theNode);
        return siInfo.backup;
      }
    }
    return "Unforseen case!";
  }

  public boolean isCellEditable(Object node, int column) {
    return true;
  }

  public void setValueAt(Object aValue, Object node, int column) {
    DefaultMutableTreeNode theNode = (DefaultMutableTreeNode) node;
    if (column == RepositoryInfo.COLUMN_BACKUP) {
      if (theNode.getParent() == getIntlRoot()) {
        RepositoryInfo repoInfo = (RepositoryInfo) repositoriesInfo.get(theNode);
        if (selectedRepoName.equals("") || repoInfo.name.equals(selectedRepoName)) {
          repoInfo.backup = (Boolean) aValue;
          selectedSICount = 0;
          if (aValue.equals(Boolean.TRUE)) {
            selectedRepoName = repoInfo.name;
          }
          else {
            selectedRepoName = "";
          }
          //--Changing backup value for repository will affect schema instance backup values
          for (int i = 0; i < theNode.getChildCount(); i++) {
            DefaultMutableTreeNode theChildNode = (DefaultMutableTreeNode) theNode.getChildAt(i);
            if (!theChildNode.toString().equals("")) {
              SchemaInstanceInfo siInfo = (SchemaInstanceInfo) schemasInfo.get(theChildNode);
              siInfo.backup = (Boolean) aValue;
              if (aValue.equals(Boolean.TRUE)) {
                selectedSICount++;
              }
            }
          }
        }
      }
      else {
        DefaultMutableTreeNode theParentNode = (DefaultMutableTreeNode) theNode.getParent();
        RepositoryInfo repoInfo = (RepositoryInfo) repositoriesInfo.get(theParentNode);
        if (selectedRepoName.equals("") || repoInfo.name.equals(selectedRepoName)) {
          if (aValue.equals(Boolean.TRUE)) {
            selectedRepoName = repoInfo.name;
            selectedSICount++;
          }
          else {
            selectedSICount--;
            if (selectedSICount == 0) {
              selectedRepoName = "";
            }
          }
          SchemaInstanceInfo siInfo = (SchemaInstanceInfo) schemasInfo.get(theNode);
          siInfo.backup = (Boolean) aValue;
          if (aValue.equals(Boolean.FALSE)) {
            repoInfo.backup = Boolean.FALSE;
          }
        }
      }
    }
    treeTable.repaint();
  }

  public TreePath selectRepo(RepositoryInfo repoInfo) {
    if (repoInfo == null) {
      return null;
    }
    LinkedList fullPath = new LinkedList();
    Object parent = getIntlRoot();
    TreeModel model = getIntlTreeModel();
    return findSelectionPath(model, parent, fullPath, repoInfo);
  }

  private TreePath findSelectionPath(TreeModel model,
      Object parent,
      LinkedList fullPath, RepositoryInfo repoInfo) {
    fullPath.addLast(parent);
    TreePath result = null;
    if (String.valueOf(parent).equals(repoInfo)) {
      // construct TreePath and return it.
      result = new TreePath(fullPath.toArray());
      return result;
    }
    int count = model.getChildCount(parent);
    for (int i = 0; i < count; i++) {
      Object child = model.getChild(parent, i);
      result = findSelectionPath(model, child, fullPath, repoInfo);
      if (result != null) {
        return result;
      }
    }
    fullPath.removeLast();
    return null;
  }

}
