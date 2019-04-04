package jsdai.tools.backup;

//import java.sql.*;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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

public class DBBackupThread implements Runnable {

  DBBackupFrame mainFrame;
  int action;

  static final int ACTION_BACKUP = 0;
  static final int ACTION_RESTORE = 1;

  public DBBackupThread(DBBackupFrame mainFrame) throws Exception {
    this.mainFrame = mainFrame;
  }

  void setAction(int action) {
    this.action = action;
  }

  public void run() {
    try {
      switch (action) {
        case ACTION_BACKUP:
          mainFrame.enableButton(DBBackupFrame.BUTTON_BACK, false);
          mainFrame.panelRepositoryList.reposTree.setEnabled(false);
          mainFrame.panelRepositoryList.backupRepository(mainFrame.backupFilePath);
          mainFrame.panelRepositoryList.reposTree.setEnabled(true);
          mainFrame.prevPage();
          break;
        case ACTION_RESTORE:
          mainFrame.enableButton(DBBackupFrame.BUTTON_BACK, false);
          mainFrame.panelRepositoryList.reposTree.setEnabled(false);
          mainFrame.panelRepositoryList.restoreRepository(mainFrame.backupFilePath);
          mainFrame.panelRepositoryList.reposTree.setEnabled(true);
          mainFrame.prevPage();
          break;
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

}
