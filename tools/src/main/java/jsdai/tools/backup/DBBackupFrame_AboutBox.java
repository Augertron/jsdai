package jsdai.tools.backup;

import java.awt.Color;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class DBBackupFrame_AboutBox extends JDialog implements ActionListener {

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JPanel insetsPanel2 = new JPanel();
  JPanel insetsPanel3 = new JPanel();
  JButton button1 = new JButton();
  JLabel imageControl1 = new JLabel();
  ImageIcon imageIcon;
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  JLabel label3 = new JLabel();
  GridLayout gridLayout1 = new GridLayout();
  String product = "JSDAI-DB Backup tool for JSDAI-SQL Bridge";
  String version = "Version 1.0";
  String copyright = "Copyright \u00A9 LKSoftWare GmbH, 2003";

  public DBBackupFrame_AboutBox(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //imageControl1.setIcon(imageIcon);
    pack();
  }

  private void jbInit() throws Exception {
    insetsPanel2.setLayout(new FlowLayout());
    insetsPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
    insetsPanel2.add(imageControl1, null);

    label1.setText(product);
    label1.setHorizontalAlignment(SwingUtilities.CENTER);
    label2.setText(version);
    label2.setHorizontalAlignment(SwingUtilities.CENTER);
    label3.setText(copyright);
    label3.setHorizontalAlignment(SwingUtilities.CENTER);
    insetsPanel3.setLayout(new GridLayout(3, 1));
    insetsPanel3.setBorder(new EmptyBorder(10, 10, 10, 10));
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);

    panel2.setLayout(new BorderLayout());
    panel2.add(insetsPanel2, BorderLayout.WEST);
    panel2.add(insetsPanel3, BorderLayout.CENTER);

    button1.setText("Ok");
    button1.addActionListener(this);

    insetsPanel1.setLayout(new FlowLayout());
    insetsPanel1.add(button1, null);

    panel1.setLayout(new BorderLayout());
    panel1.add(panel2, BorderLayout.NORTH);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);

    this.setTitle("About");
    this.setResizable(false);
    this.getContentPane().add(panel1, null);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  void cancel() {
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
  }
}
