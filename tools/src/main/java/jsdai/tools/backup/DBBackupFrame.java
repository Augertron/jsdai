package jsdai.tools.backup;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.io.File;
//import java.sql.Connection;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.SwingUtilities;
//import com.borland.jbcl.layout.*;
import jsdai.lang.*;
import jsdai.beans.SdaiServerBean;
import jsdai.beans.SimpleFileFilter;

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

public class DBBackupFrame extends JFrame {
  JPanel contentPane;

  SdaiServerBean sdaiServerBean;
  SdaiSession session;

  CardLayout cardLayout = new CardLayout();
  JPanel panelCards = new JPanel();
  JPanel panelChoise = new JPanel(new GridLayout(4, 1));
  PanelRepositoryList panelRepositoryList;
  JPanel panelControls = new JPanel();
  JPanel panelButtons = new JPanel();

  JButton buttonLink = new JButton();
  JButton buttonUnlink = new JButton();
  JButton buttonBack = new JButton();
  JButton buttonAction = new JButton();
  JButton buttonCancel = new JButton();
  JProgressBar progressBar = new JProgressBar();
  JLabel labelMessage = new JLabel(" ");
  JLabel labelChoose = new JLabel("Choose one of the following actions:");

  ButtonGroup bgroupBackupRestore = new ButtonGroup();
  JRadioButton rbuttonBackup = new JRadioButton("Backup");
  JRadioButton rbuttonRestore = new JRadioButton("Restore");

  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenu1 = new JMenu();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JMenuItem jMenuItem2 = new JMenuItem();

  private int iCurrCard;
  int processed;
  String backupFilePath;

  static final int BUTTON_LINK = 0;
  static final int BUTTON_UNLINK = 1;
  static final int BUTTON_BACK = 4;
  static final int BUTTON_ACTION = 2;
  static final int BUTTON_CANCEL = 3;

  static final String BUTTON_TEXT_NEXT = "   Next   ";
  static final String BUTTON_TEXT_BACKUP = " Backup ";
  static final String BUTTON_TEXT_RESTORE = "Restore";

  //Construct the frame
  public DBBackupFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(DBBackupFrame.class.getResource("[Your Icon]")));

    session = SdaiSession.openSession();

    contentPane = (JPanel) this.getContentPane();
    this.setSize(new Dimension(600, 480));
    this.setTitle("JSDAI-DB Backup tool");

    labelChoose.setEnabled(false);
    rbuttonBackup.setSelected(true);
    rbuttonBackup.setEnabled(false);
    rbuttonRestore.setEnabled(false);
    bgroupBackupRestore.add(rbuttonBackup);
    bgroupBackupRestore.add(rbuttonRestore);

    panelChoise.add(new JLabel(" "));
    panelChoise.add(labelChoose);
    panelChoise.add(rbuttonBackup);
    panelChoise.add(rbuttonRestore);
    JPanel panelChoiseP = new JPanel(new FlowLayout());
    panelChoiseP.add(panelChoise);

    panelCards.setLayout(cardLayout);
    panelCards.add(panelChoiseP, "First");
    iCurrCard = 0;

    sdaiServerBean = new SdaiServerBean(this);
    sdaiServerBean.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(SdaiServerBean.LINK_LINKED)) {
          try {
            if (SdaiSession.getSession().getDataBaseBridge() != null) {
              enableButton(BUTTON_ACTION, true);
              labelChoose.setEnabled(true);
              rbuttonBackup.setEnabled(true);
              rbuttonRestore.setEnabled(true);
            }
          }
          catch (SdaiException ex) {
            ex.printStackTrace();
          }
        }
        else if (e.getActionCommand().equals(SdaiServerBean.UNLINK_UNLINKING)) {
          try {
            SdaiSession session = SdaiSession.getSession();

            ASdaiRepository aRepo = session.getKnownServers();
            SdaiIterator itRepo = aRepo.createIterator();

            while (itRepo.next()) {
              SdaiRepository repo = aRepo.getCurrentMember(itRepo);
              if (repo.getName().startsWith("//")) {
                RepositoryInfo repoInfo = new RepositoryInfo(repo.getName());
                if (repo.isActive()) {
                  repo.closeRepository();
                }
              }
            }
          }
          catch (SdaiException ex) {
            ex.printStackTrace();
          }
        }
        else if (e.getActionCommand().equals(SdaiServerBean.UNLINK_UNLINKED)) {
          enableButton(BUTTON_ACTION, false);
          labelChoose.setEnabled(false);
          rbuttonBackup.setEnabled(false);
          rbuttonRestore.setEnabled(false);
        }
      }
    });
    sdaiServerBean.attachLinkButton(buttonLink);
    sdaiServerBean.attachUnlinkButton(buttonUnlink);

    buttonLink.setText("Link...");
    buttonUnlink.setText("Unlink");
    buttonBack.setText("Back");
    buttonBack.setEnabled(false);
    buttonBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonBack_actionPerformed(e);
      }
    });
    buttonAction.setText(BUTTON_TEXT_NEXT);
    buttonAction.setEnabled(false);
    buttonAction.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonAction_actionPerformed(e);
      }
    });
    buttonCancel.setText("Exit");
    buttonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonCancel_actionPerformed(e);
      }
    });

    panelButtons.add(buttonLink, null);
    panelButtons.add(buttonUnlink, null);
    panelButtons.add(new JLabel(" "), null);
    panelButtons.add(buttonBack, null);
    panelButtons.add(buttonAction, null);
    panelButtons.add(buttonCancel, null);

    panelControls.setLayout(new BorderLayout());
    panelControls.add(labelMessage, BorderLayout.NORTH);
    panelControls.add(progressBar, BorderLayout.CENTER);
    panelControls.add(panelButtons, BorderLayout.SOUTH);

    JPanel panelControlsP = new JPanel(new FlowLayout());
    panelControlsP.add(panelControls);

    contentPane.setLayout(new BorderLayout());
    contentPane.add(panelCards, BorderLayout.CENTER);
    contentPane.add(panelControlsP, BorderLayout.SOUTH);

    jMenu1.setText("File");
    jMenuItem1.setText("Exit");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem1_actionPerformed(e);
      }
    });
    jMenu2.setText("Help");
    jMenuItem2.setText("About");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem2_actionPerformed(e);
      }
    });

    jMenu1.add(jMenuItem1);
    jMenu2.add(jMenuItem2);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(jMenu2);
    this.setJMenuBar(jMenuBar1);

  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void buttonAction_actionPerformed(ActionEvent e) {
    try {
      if (iCurrCard == 0) {
        if (rbuttonBackup.isSelected()) {
          buttonAction.setText(BUTTON_TEXT_BACKUP);
          if (panelRepositoryList == null) {
            panelRepositoryList = new PanelRepositoryList(this, true);
            panelCards.add(panelRepositoryList, "Second");
          }
          else {
            panelRepositoryList.reloadRepositoryInfo(true);
          }
          panelRepositoryList.setVisibleNameControls(false);
          cardLayout.next(panelCards);
          enableButton(BUTTON_UNLINK, false);
          enableButton(BUTTON_BACK, true);
          printMessage(" ");
          showProgress(0);
          iCurrCard++;
        }
        else {
          //--Selection of sdai file from which repository will be restored
          JFileChooser fileChooser = new JFileChooser();
          SimpleFileFilter fileFilter = new SimpleFileFilter("sdai", "SDAI files");
          fileChooser.addChoosableFileFilter(fileFilter);
          fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setCurrentDirectory(new File("."));

          int returnVal = fileChooser.showDialog(this, "Restore");
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            backupFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            buttonAction.setText(BUTTON_TEXT_RESTORE);
            if (panelRepositoryList == null) {
              panelRepositoryList = new PanelRepositoryList(this, false);
              panelCards.add(panelRepositoryList, "Second");
            }
            else {
              panelRepositoryList.reloadRepositoryInfo(false);
            }
            panelRepositoryList.setVisibleNameControls(true);
            cardLayout.next(panelCards);
            enableButton(BUTTON_UNLINK, false);
            enableButton(BUTTON_BACK, true);
            printMessage(" ");
            showProgress(0);
            iCurrCard++;
          }
        }

      }
      else {
        if (panelRepositoryList.isSelected()) {
          if (rbuttonBackup.isSelected()) {
            JFileChooser fileChooser = new JFileChooser();
            SimpleFileFilter fileFilter = new SimpleFileFilter("sdai", "SDAI files");
            fileChooser.addChoosableFileFilter(fileFilter);
            fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
            fileChooser.setCurrentDirectory(new File("."));

            int returnVal = fileChooser.showDialog(this, "Backup");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
              //System.out.println("--backuping selected repository--");
              boolean bCanCreate = true;
              File fileBackup = fileChooser.getSelectedFile();
              if (fileBackup.exists()) {
                if (JOptionPane.showConfirmDialog(this, "Specified file already exists.\nOverwrite ?", "Confirm", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                  fileBackup.delete();
                }
                else {
                  bCanCreate = false;
                }
              }
              if (bCanCreate) {
                backupFilePath = fileBackup.getAbsolutePath();
                DBBackupThread dbBackupThread = new DBBackupThread(this);
                dbBackupThread.setAction(DBBackupThread.ACTION_BACKUP);
                Thread thread = new Thread(dbBackupThread);
                thread.start();
              }
            }
          }
          else {
            if ((panelRepositoryList.rbNameSpecify.isSelected() && panelRepositoryList.tfName.getText().length() > 0) ||
                panelRepositoryList.rbNameDefault.isSelected()) {
              //System.out.println("--restoring selected repository--");
              DBBackupThread dbBackupThread = new DBBackupThread(this);
              dbBackupThread.setAction(DBBackupThread.ACTION_RESTORE);
              Thread thread = new Thread(dbBackupThread);
              thread.start();
            }
            else {
              JOptionPane.showMessageDialog(this, "Not specified repository name.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
          }
        }
        else {
          JOptionPane
              .showMessageDialog(this, "There are no selected items in the list.\nAt least one item must be selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void buttonBack_actionPerformed(ActionEvent e) {
    try {
      if (iCurrCard > 0) {
        buttonAction.setText(BUTTON_TEXT_NEXT);
        cardLayout.previous(panelCards);
        enableButton(BUTTON_UNLINK, true);
        enableButton(BUTTON_BACK, false);
        printMessage(" ");
        showProgress(0);
        iCurrCard--;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void buttonCancel_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  void printMessage(final String message) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        labelMessage.setText(message);
      }
    });
  }

  void showProgress(final int value) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setValue(value);
      }
    });

  }

  void prevPage() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        buttonAction.setText(BUTTON_TEXT_NEXT);
        cardLayout.previous(panelCards);
        enableButton(BUTTON_UNLINK, true);
        enableButton(BUTTON_BACK, false);
        printMessage(" ");
        showProgress(0);
        iCurrCard--;
      }
    });
  }

  void enableButton(final int button, final boolean enabled) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        switch (button) {
          case BUTTON_UNLINK:
            buttonUnlink.setEnabled(enabled);
            break;
          case BUTTON_BACK:
            buttonBack.setEnabled(enabled);
            break;
          case BUTTON_ACTION:
            buttonAction.setEnabled(enabled);
            break;
          case BUTTON_CANCEL:
            buttonCancel.setEnabled(enabled);
            break;
        }
      }
    });

  }

  void helpAbout() {
    DBBackupFrame_AboutBox dlg = new DBBackupFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }

  void jMenuItem2_actionPerformed(ActionEvent e) {
    helpAbout();
  }

  void jMenuItem2_ancestorAdded(AncestorEvent e) {

  }

  void jMenuItem2_mouseClicked(MouseEvent e) {
  }

  void jMenuItem1_actionPerformed(ActionEvent e) {
    System.exit(0);
  }
    
    /*
    class MyFilenameFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.endsWith(".sdai");
        }
    }
    */
}
