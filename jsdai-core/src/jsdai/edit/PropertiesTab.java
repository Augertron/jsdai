/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.edit;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import jsdai.beans.*;
import jsdai.lang.*;
import jsdai.edit.*;

public class PropertiesTab extends SdaiPanel {
	PropertiesTab thisis = this;
	SdaiEdit sdaiedit;

	Properties props;

   BoxLayout layoutMain = new BoxLayout(this, BoxLayout.Y_AXIS);

   JPanel pFrame = new JPanel();
   GridLayout layoutFrame = new GridLayout();
   JLabel lLeft = new JLabel();
   JLabel lTop = new JLabel();
   JLabel lWidth = new JLabel();
   JLabel lHeight = new JLabel();
   JTextField tTop = new JTextField();
   JTextField tLeft = new JTextField();
   JTextField tWidth = new JTextField();
   JTextField tHeight = new JTextField();

   JPanel pClipboard = new JPanel();
   BorderLayout layoutClipboard = new BorderLayout();
   JScrollPane scrollClipboard = new JScrollPane();
   JList lClipboard = new JList();
	AggregateListModel clipboardModel = new AggregateListModel();

   JPanel pDirectory = new JPanel();
   GridLayout layoutDirectory = new GridLayout();
   JPanel pSubDirectory = new JPanel();
   BoxLayout layoutSubDirectory = new BoxLayout(pSubDirectory, BoxLayout.X_AXIS);
   JTextField tDirectory = new JTextField();
   JButton bDirectory = new JButton();

   JPanel pRegistration = new JPanel();
   GridLayout layoutRegistration = new GridLayout();
   JLabel lLicense = new JLabel();
   JLabel lSerial = new JLabel();
   JLabel lKey = new JLabel();
   JTextField tLicense = new JTextField();
   JTextField tSerial = new JTextField();
   JTextField tKey = new JTextField();

   JPanel pMapping = new JPanel();
	ButtonGroup gMapping = new ButtonGroup();
   JRadioButton rMapping1 = new JRadioButton();
   JRadioButton rMapping2 = new JRadioButton();
   JRadioButton rMapping3 = new JRadioButton();

   JPanel pSplit = new JPanel();
//   GridLayout layoutSplit = new GridLayout();
   JCheckBox jSplit = new JCheckBox();
//   JTextField tSplit = new JTextField();
//   JLabel lSplit = new JLabel();

   JPanel pDeveloper = new JPanel();
   JCheckBox jDeveloper = new JCheckBox();

   JPanel pToolbar = new JPanel();
   JButton bAccept = new JButton();
   FlowLayout flowLayout1 = new FlowLayout();
   JButton bCancel = new JButton();
   JButton bEdit = new JButton();

   public PropertiesTab(SdaiEdit edit) {
      try {
		   sdaiedit = edit;
         jbInit();
      }
      catch(Exception ex) {
         ex.printStackTrace();
      }
   }

   void jbInit() throws Exception {
      this.setLayout(layoutMain);

      pFrame.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Frame"));
      pFrame.setLayout(layoutFrame);
      layoutFrame.setRows(2);
      layoutFrame.setColumns(4);
      layoutFrame.setHgap(5);
      lLeft.setText("Top");
      lTop.setText("Left");
      lWidth.setText("Width");
      lHeight.setText("Height");

      pClipboard.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Clipboard"));
      pClipboard.setLayout(layoutClipboard);
		AEntity clipboard = SdaiSession.getSession().getClipboard();
		clipboardModel.setAggregate(clipboard);
		lClipboard.setModel(clipboardModel);
		lClipboard.setBackground(getBackground());
		clipboard.addSdaiListener(new SdaiListener() {
		   public void actionPerformed(SdaiEvent e) {
				try {
				   clipboardModel.fireContentsChanged();
				} catch (SdaiException ex) {}
		   }
		});

      pDirectory.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Working directory"));
      pDirectory.setLayout(layoutDirectory);
      pSubDirectory.setLayout(layoutSubDirectory);
      bDirectory.setPreferredSize(new Dimension(43, 21));
		bDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(tDirectory.getText());
				chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
				if (chooser.showDialog(thisis, "Choose") == JFileChooser.APPROVE_OPTION) {
					tDirectory.setText(chooser.getSelectedFile().getPath());
				}
			}
		});

      bDirectory.setText("...");

      pRegistration.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Registration"));
      pRegistration.setLayout(layoutRegistration);
      layoutRegistration.setRows(3);
      layoutRegistration.setColumns(2);
      lLicense.setText("Registered to");
      lSerial.setText("Serial");
      lKey.setText("Key");

      pMapping.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Mapping mode"));
      rMapping1.setText("No restrictions");
      rMapping2.setText("Most specific entity");
      rMapping3.setText("Mandatory attributes set");
		gMapping.add(rMapping1);
		gMapping.add(rMapping2);
		gMapping.add(rMapping3);

      pSplit.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Split"));
//      pSplit.setLayout(layoutSplit);
//      layoutSplit.setColumns(3);
//      jSplit.setPreferredSize(new Dimension(Short.MAX_VALUE, 21));
      jSplit.setText("Is all splits aligned.");
//      lSplit.setHorizontalAlignment(SwingConstants.CENTER);
//      lSplit.setText("Split size");

      pDeveloper.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Developer"));
//      jDeveloper.setPreferredSize(new Dimension(Short.MAX_VALUE, 21));
      jDeveloper.setText("Is developer mode on.");


      pToolbar.setLayout(flowLayout1);
      flowLayout1.setAlignment(FlowLayout.RIGHT);
      bAccept.setText("Accept");
      bAccept.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(ActionEvent e) {
				getProperties(props);
				sdaiedit.resetProperties(props);
            setEditable(false);
         }
      });
      bCancel.setText("Cancel");
      bCancel.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(ActionEvent e) {
            setProperties(props);
            setEditable(false);
         }
      });
      bEdit.setText("Edit");
      bEdit.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(ActionEvent e) {
            setEditable(true);
         }
      });

//      this.add(pFrame, null);
      pFrame.add(lLeft, null);
      pFrame.add(tTop, null);
      pFrame.add(lTop, null);
      pFrame.add(tLeft, null);
      pFrame.add(lWidth, null);
      pFrame.add(tWidth, null);
      pFrame.add(lHeight, null);
      pFrame.add(tHeight, null);

      this.add(pDeveloper, null);
      pDeveloper.add(jDeveloper, null);

      this.add(pSplit, null);
      pSplit.add(jSplit, null);
//      pSplit.add(lSplit, null);
//      pSplit.add(tSplit, null);

      this.add(pMapping, null);
      pMapping.add(rMapping1, null);
      pMapping.add(rMapping2, null);
      pMapping.add(rMapping3, null);

//      this.add(pRegistration, null);
      pRegistration.add(lLicense, null);
      pRegistration.add(tLicense, null);
      pRegistration.add(lSerial, null);
      pRegistration.add(tSerial, null);
      pRegistration.add(lKey, null);
      pRegistration.add(tKey, null);

      this.add(pDirectory, null);
      pDirectory.add(pSubDirectory, null);
      pSubDirectory.add(tDirectory, null);
      pSubDirectory.add(bDirectory, null);

      this.add(pClipboard, null);
      pClipboard.add(scrollClipboard, BorderLayout.CENTER);
      scrollClipboard.getViewport().add(lClipboard, null);

      this.add(pToolbar, null);
      pToolbar.add(bEdit, null);
      pToolbar.add(bCancel, null);
      pToolbar.add(bAccept, null);

		setEditable(false);
   }

	public void setProperties(Properties props) {
		this.props = props;
		tTop.setText(props.getProperty("frame.top"));
		tLeft.setText(props.getProperty("frame.left"));
		tWidth.setText(props.getProperty("frame.width"));
		tHeight.setText(props.getProperty("frame.height"));

		tSerial.setText(props.getProperty("main.serial"));
		tLicense.setText(props.getProperty("main.license"));
		tKey.setText(props.getProperty("main.key"));

		jDeveloper.setSelected(Boolean.valueOf(props.getProperty("entityPage.isDeveloperMode")).booleanValue());

		rMapping1.setSelected(Integer.parseInt(props.getProperty("mapping.mode", "0")) == 0);
		rMapping2.setSelected(Integer.parseInt(props.getProperty("mapping.mode", "0")) == 1);
		rMapping3.setSelected(Integer.parseInt(props.getProperty("mapping.mode", "0")) == 2);

		tDirectory.setText(props.getProperty("sessionPage.dir"));

		jSplit.setSelected(Boolean.valueOf(props.getProperty("entityPage.isLockSplits", "false")).booleanValue());
//		tSplit.setText(props.getProperty("entityPage.splitLocation"));
	}

	public void getProperties(Properties props) {
		this.props = props;
		props.setProperty("frame.top", tTop.getText());
		props.setProperty("frame.left", tLeft.getText());
		props.setProperty("frame.width", tWidth.getText());
		props.setProperty("frame.height", tHeight.getText());

		props.setProperty("main.serial", tSerial.getText());
		props.setProperty("main.license", tLicense.getText());
		props.setProperty("main.key", tKey.getText());

		props.setProperty("entityPage.isDeveloperMode", String.valueOf(jDeveloper.isSelected()));

		if (rMapping1.isSelected())
		   props.setProperty("mapping.mode", "0");
		if (rMapping2.isSelected())
		   props.setProperty("mapping.mode", "1");
		if (rMapping3.isSelected())
		   props.setProperty("mapping.mode", "2");

		props.setProperty("sessionPage.dir", tDirectory.getText());

		props.setProperty("entityPage.isLockSplits", String.valueOf(jSplit.isSelected()));
//		props.setProperty("entityPage.splitLocation", tSplit.getText());
	}

	private void setEditable(boolean editable) {
		tTop.setEditable(editable);
		tLeft.setEditable(editable);
		tWidth.setEditable(editable);
		tHeight.setEditable(editable);

		tSerial.setEditable(editable);
		tLicense.setEditable(editable);
		tKey.setEditable(editable);

		jDeveloper.setEnabled(editable);

		rMapping1.setEnabled(editable);
		rMapping2.setEnabled(editable);
		rMapping3.setEnabled(editable);

		tDirectory.setEditable(editable);
		bDirectory.setEnabled(editable);

		jSplit.setEnabled(editable);
//		tSplit.setEditable(editable);

		bAccept.setEnabled(editable);
		bCancel.setEnabled(editable);
		bEdit.setEnabled(!editable);
	}

	public void refreshData() {
		try {
		   sdaiedit.retakeProperties(props);
			setProperties(props);
			clipboardModel.fireContentsChanged();
		} catch (SdaiException ex) {}
	}
}