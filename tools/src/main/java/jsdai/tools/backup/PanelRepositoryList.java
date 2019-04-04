package jsdai.tools.backup;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

import jsdai.lang.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001-2003</p>
 * <p>Company: </p>
 * <p>$Id$</p>
 *
 * @author unascribed
 * @version $Revision$
 */

//public class SchemasInfo extends JDialog{
public class PanelRepositoryList extends JPanel {
  private JPanel jPanel1 = new JPanel();
  private JPanel panelControls = new JPanel();
  private JScrollPane jScrollPane1 = new JScrollPane();
  JTable jTable = new JTable();
  private RepositoryInfo[] reposInfo;
  private ButtonGroup bgName = new ButtonGroup();
  protected JRadioButton rbNameDefault = new JRadioButton("Default");
  protected JRadioButton rbNameSpecify = new JRadioButton("Specify");
  protected JTextField tfName = new JTextField("///repository_name", 20);
  DBBackupFrame mainFrame;
  //private Connection connection;

  protected String[] colNames = { "Name", "Backup" };
  protected GenericTreeTable reposTree;
  protected RepositoriesTreeModel reposTreeModel;

  public PanelRepositoryList(DBBackupFrame owner, boolean backup) throws Exception {
    this.mainFrame = owner;

    reposInfo = getReposInfo(backup);
    reposTreeModel = new RepositoriesTreeModel(reposInfo, true);
    reposTree = new GenericTreeTable();
    reposTreeModel.setTreeTable(reposTree);
    if (backup) {
      colNames[RepositoryInfo.COLUMN_BACKUP] = "Backup";
    }
    else {
      colNames[RepositoryInfo.COLUMN_BACKUP] = "Restore";
    }
    reposTreeModel.setColumnNames(colNames);
    reposTree.setModel(reposTreeModel);
    reposTree.setTreeCellRenderer(new RepoTreeCellRenderer(reposTree));
        
        /*
        reposTree.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				doAction(e);
			}
			private void doAction(MouseEvent e){
				if((e.getClickCount() == 2) && (e.getButton() == e.BUTTON1)){
                    reposTree.getSelectionModel().setSelectionInterval( 2, 2);
				} else
					if((e.getClickCount() == 1) && (e.getButton() == e.BUTTON1)){
                        reposTree.getSelectionModel().setSelectionInterval( 2, 2);
					}
			}
		});
        */

    jScrollPane1.setViewportView(reposTree);

    bgName.add(rbNameDefault);
    bgName.add(rbNameSpecify);
    rbNameDefault.setSelected(true);
    tfName.setEnabled(false);

    rbNameDefault.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tfName.setEnabled(false);
      }
    });
    rbNameSpecify.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tfName.setEnabled(true);
      }
    });

    panelControls.add(new JLabel("Repository name: "));
    panelControls.add(rbNameDefault);
    panelControls.add(rbNameSpecify);
    panelControls.add(tfName);
    //JPanel panelChoiseP = new JPanel(new FlowLayout());
    //panelName.add(panelChoise);
    panelControls.setVisible(false);

    this.setLayout(new BorderLayout());
    this.add(jScrollPane1, BorderLayout.CENTER);
    this.add(panelControls, BorderLayout.SOUTH);
  }

  void jbInit() throws Exception {

  }

  RepositoryInfo[] getReposInfo(boolean backup) throws Exception {
    ArrayList alRepoInfo = new ArrayList();
    ArrayList alSIInfo = new ArrayList();

    SdaiSession session = SdaiSession.getSession();
    if (!session.testActiveTransaction()) {
      session.startTransactionReadWriteAccess();
    }
    SdaiTransaction transaction = session.getActiveTransaction();

    if (backup) {
      ASdaiRepository aRepo = session.getKnownServers();
      SdaiIterator itRepo = aRepo.createIterator();
      while (itRepo.next()) {
        SdaiRepository repo = aRepo.getCurrentMember(itRepo);
        if (repo.getName().startsWith("//")) {
          RepositoryInfo repoInfo = new RepositoryInfo(repo.getName());
          if (!repo.isActive()) {
            repo.openRepository();
          }
          alSIInfo.clear();

          ASchemaInstance aSI = repo.getSchemas();
          SdaiIterator itSI = aSI.createIterator();
          while (itSI.next()) {
            SchemaInstance si = aSI.getCurrentMember(itSI);
            SchemaInstanceInfo siInfo = new SchemaInstanceInfo(si.getName());
            alSIInfo.add(siInfo);
          }
          repoInfo.setSIInfo((SchemaInstanceInfo[]) alSIInfo.toArray(new SchemaInstanceInfo[0]));
          alRepoInfo.add(repoInfo);
        }
      }
    }
    else {
      SdaiRepository repo = session.linkRepository("", mainFrame.backupFilePath);
      RepositoryInfo repoInfo = new RepositoryInfo(repo.getRealName());
      if (!repo.isActive()) {
        repo.openRepository();
      }
      alSIInfo.clear();

      ASchemaInstance aSI = repo.getSchemas();
      SdaiIterator itSI = aSI.createIterator();
      while (itSI.next()) {
        SchemaInstance si = aSI.getCurrentMember(itSI);
        SchemaInstanceInfo siInfo = new SchemaInstanceInfo(si.getName());
        alSIInfo.add(siInfo);
      }
      repoInfo.setSIInfo((SchemaInstanceInfo[]) alSIInfo.toArray(new SchemaInstanceInfo[0]));
      alRepoInfo.add(repoInfo);
      repo.closeRepository();
      repo.unlinkRepository();
    }
    transaction.endTransactionAccessAbort();

    return (RepositoryInfo[]) alRepoInfo.toArray(new RepositoryInfo[0]);
  }

  void reloadRepositoryInfo(boolean backup) {
    try {
      reposInfo = getReposInfo(backup);
      reposTree.getSelectionModel().clearSelection();
      reposTreeModel = new RepositoriesTreeModel(reposInfo, true);
      reposTreeModel.setTreeTable(reposTree);
      if (backup) {
        colNames[RepositoryInfo.COLUMN_BACKUP] = "Backup";
      }
      else {
        colNames[RepositoryInfo.COLUMN_BACKUP] = "Restore";
      }
      reposTreeModel.setColumnNames(colNames);
      reposTree.setModel(reposTreeModel);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void backupRepository(String backupFilePath) {
    try {
      mainFrame.enableButton(DBBackupFrame.BUTTON_ACTION, false);
      mainFrame.printMessage("Backuping ...");
      mainFrame.showProgress(0);

      //System.out.println(" backupFilePath="+backupFilePath);
      SdaiSession session = SdaiSession.getSession();
      if (!session.testActiveTransaction()) {
        session.startTransactionReadWriteAccess();
      }
      SdaiTransaction transaction = session.getActiveTransaction();
      SdaiRepository repoSource;
      SdaiRepository repoBackup;
      mainFrame.showProgress(10);

      for (int i = 0; i < reposInfo.length; i++) {
        RepositoryInfo repoInfo = reposInfo[i];
        //System.out.println("--repoInfo name= "+repoInfo.name+" backup= "+repoInfo.backup);
        int indexSlash = repoInfo.name.lastIndexOf("/");
        //System.out.println("--repoInfo local name= "+repoInfo.name.substring(indexSlash+1));
        if (repoInfo.backup == Boolean.TRUE) {
          repoSource = session.linkRepository(repoInfo.name, "");
          if (!repoSource.isActive()) {
            repoSource.openRepository();
          }
          mainFrame.showProgress(20);
          //long lST, lCT;
          //lST = System.currentTimeMillis();
          repoBackup = session.createRepository(repoInfo.name.substring(indexSlash + 1), backupFilePath);
          repoBackup.openRepository();
          repoBackup.copyFrom(repoSource);
          mainFrame.showProgress(50);
          transaction.commit();
          mainFrame.showProgress(90);
          repoBackup.closeRepository();
          repoSource.closeRepository();
          repoBackup.unlinkRepository();
          //lCT = System.currentTimeMillis();
          //System.out.println("----------------------------------------------------------------------------------------------");
          //System.out.println("  SdaiRepository.backupRepository t= "+(float)(lCT-lST)/1000+"s");
          //System.out.println("----------------------------------------------------------------------------------------------");
        }
        else {
          repoSource = session.linkRepository(repoInfo.name, "");
          if (!repoSource.isActive()) {
            repoSource.openRepository();
          }
          SchemaInstanceInfo[] sisInfo = repoInfo.getSIInfo();
          ASchemaInstance aSelectedSI = new ASchemaInstance();
          for (int j = 0; j < sisInfo.length; j++) {
            SchemaInstanceInfo siInfo = sisInfo[j];
            //System.out.println("----siInfo = "+siInfo.name+" backup= "+siInfo.backup);
            if (siInfo.backup == Boolean.TRUE) {
              ASchemaInstance aSI = repoSource.getSchemas();
              SdaiIterator itSI = aSI.createIterator();
              while (itSI.next()) {
                SchemaInstance si = aSI.getCurrentMember(itSI);
                if (siInfo.name.equals(si.getName())) {
                  aSelectedSI.addUnordered(si, null);
                }
              }
            }
          }
          mainFrame.showProgress(20);
          if (aSelectedSI.getMemberCount() > 0) {
            repoBackup = session.createRepository(repoInfo.name.substring(indexSlash + 1), backupFilePath);
            repoBackup.openRepository();
            repoBackup.copyFrom(aSelectedSI);
            mainFrame.showProgress(50);
            transaction.commit();
            mainFrame.showProgress(90);
            repoBackup.closeRepository();
            repoBackup.unlinkRepository();
          }
          repoSource.closeRepository();
        }
      }
      transaction.endTransactionAccessAbort();
      mainFrame.enableButton(DBBackupFrame.BUTTON_ACTION, true);
      mainFrame.printMessage("Backuped");
      mainFrame.showProgress(100);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void restoreRepository(String backupFilePath) {
    try {
      mainFrame.enableButton(DBBackupFrame.BUTTON_ACTION, false);
      mainFrame.printMessage("Restoring ...");
      mainFrame.showProgress(0);

      //System.out.println("--restoreFilePath="+backupFilePath);
      SdaiSession session = SdaiSession.getSession();
      if (!session.testActiveTransaction()) {
        session.startTransactionReadWriteAccess();
      }
      SdaiTransaction transaction = session.getActiveTransaction();
      SdaiRepository repoSource;
      SdaiRepository repoTarget;
      mainFrame.showProgress(10);
      String repoName = null;
      for (int i = 0; i < reposInfo.length; i++) {
        RepositoryInfo repoInfo = reposInfo[i];
        //System.out.println("--repoInfo name= "+repoInfo.name+" backup= "+repoInfo.backup);
        boolean bCanCreate = true;
        repoSource = session.linkRepository("", backupFilePath);
        if (!repoSource.isActive()) {
          repoSource.openRepository();
        }
        if (repoInfo.backup == Boolean.TRUE) {
          if (rbNameSpecify.isSelected()) {
            repoName = tfName.getText();
          }
          else {
            repoName = "///" + repoInfo.name;
          }
          ASdaiRepository aRepo = session.getKnownServers();
          SdaiIterator itRepo = aRepo.createIterator();
          while (itRepo.next()) {
            SdaiRepository repo = aRepo.getCurrentMember(itRepo);
            if (repo.getName().equals(repoName)) {
              int returnVal = JOptionPane
                  .showConfirmDialog(mainFrame, "Repository with specified name already exists.\nOverwrite ?", "Confirm", JOptionPane.YES_NO_OPTION);
              if (returnVal == JOptionPane.YES_OPTION) {
                repo.deleteRepository();
                transaction.commit();
              }
              else {
                bCanCreate = false;
              }
              break;
            }
          }
          mainFrame.showProgress(20);

          if (bCanCreate) {
            //long lST, lCT;
            //lST = System.currentTimeMillis();
            repoTarget = session.createRepository(repoName, "");
            repoTarget.openRepository();
            repoTarget.copyFrom(repoSource);
            mainFrame.showProgress(50);
            transaction.commit();
            mainFrame.showProgress(90);
            repoTarget.closeRepository();
            //lCT = System.currentTimeMillis();
            //System.out.println("----------------------------------------------------------------------------------------------");
            //System.out.println("  SdaiRepository.restoreRepository t= "+(float)(lCT-lST)/1000+"s");
            //System.out.println("----------------------------------------------------------------------------------------------");
          }
        }
        else {
          SchemaInstanceInfo[] sisInfo = repoInfo.getSIInfo();
          ASchemaInstance aSelectedSI = new ASchemaInstance();
          for (int j = 0; j < sisInfo.length; j++) {
            SchemaInstanceInfo siInfo = sisInfo[j];
            //System.out.println("----siInfo = "+siInfo.name+" backup= "+siInfo.backup);
            if (siInfo.backup == Boolean.TRUE) {
              ASchemaInstance aSI = repoSource.getSchemas();
              SdaiIterator itSI = aSI.createIterator();
              while (itSI.next()) {
                SchemaInstance si = aSI.getCurrentMember(itSI);
                if (siInfo.name.equals(si.getName())) {
                  aSelectedSI.addUnordered(si, null);
                }
              }
            }
          }
          if (aSelectedSI.getMemberCount() > 0) {
            if (rbNameSpecify.isSelected()) {
              repoName = tfName.getText();
            }
            else {
              repoName = "///" + repoInfo.name;
            }
            ASdaiRepository aRepo = session.getKnownServers();
            SdaiIterator itRepo = aRepo.createIterator();
            while (itRepo.next()) {
              SdaiRepository repo = aRepo.getCurrentMember(itRepo);
              if (repo.getName().equals("///" + repoInfo.name)) {
                int returnVal = JOptionPane
                    .showConfirmDialog(mainFrame, "Repository with specified name already exists.\nOverwrite ?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (returnVal == JOptionPane.YES_OPTION) {
                  repo.deleteRepository();
                  transaction.commit();
                }
                else {
                  bCanCreate = false;
                }
                break;
              }
            }
            mainFrame.showProgress(20);
            if (bCanCreate) {
              repoTarget = session.createRepository(repoName, "");
              repoTarget.openRepository();
              repoTarget.copyFrom(aSelectedSI);
              mainFrame.showProgress(50);
              transaction.commit();
              mainFrame.showProgress(90);
              repoTarget.closeRepository();
            }
          }
        }
        repoSource.closeRepository();
        repoSource.unlinkRepository();
      }
      transaction.endTransactionAccessAbort();
      mainFrame.enableButton(DBBackupFrame.BUTTON_ACTION, true);
      mainFrame.printMessage("Restored");
      mainFrame.showProgress(100);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  boolean isSelected() {
    for (int i = 0; i < reposInfo.length; i++) {
      RepositoryInfo repoInfo = reposInfo[i];
      if (repoInfo.backup == Boolean.TRUE) {
        return true;
      }
      else {
        SchemaInstanceInfo[] sisInfo = repoInfo.getSIInfo();
        for (int j = 0; j < sisInfo.length; j++) {
          SchemaInstanceInfo siInfo = sisInfo[j];
          if (siInfo.backup == Boolean.TRUE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  void setVisibleNameControls(final boolean visible) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        rbNameDefault.setSelected(true);
        tfName.setEnabled(false);
        panelControls.setVisible(visible);
      }
    });
  }

}
