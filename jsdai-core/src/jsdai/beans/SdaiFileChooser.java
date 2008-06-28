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

package jsdai.beans;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.*;

import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiException;

public class SdaiFileChooser extends JDialog {

	public static final int REPOSITORY_TYPE_AUTO = 1;
	public static final int REPOSITORY_TYPE_SPECIFY = 2;
	public static final int REPOSITORY_TYPE_TEMP = 3;

	private static final String lastUsedDirectory = "lastUsedDir";
	private static final String lastUsedFileFilter = "lastUsedFilesFilter";
	private static final String allFilesFilterUsed = "allFiles";
	private static final String stepFilesFilterUsed = "stepFiles";

	JFileChooser fileChooserDlg = null;
	int dialogResult = JFileChooser.CANCEL_OPTION;

	PreviewPanel 	previewPanel;
	Properties		dialogProperties = new Properties();

	// repository parameter controls
	JPanel pRepositoryImport;
	JLabel repositoryLocationLabel;
	JLabel repositoryNameLabel;
	JTextField tfRepositoryImportName;
	JTextField tfRepositoryImportLocation;

	JRadioButton rbAuto;
	JRadioButton rbSpecify;
	JRadioButton rbTemp;

	JRadioButton rbLocal;
	JRadioButton rbRemote;

	private SdaiFileChooser() {
	}

	public SdaiFileChooser(String applicationName) throws SdaiException {
		this.dialogInit();
		this.setModal(true);
		fileChooserDlg = new JFileChooser(applicationName);
		setTitle(applicationName);
		// Let's read any possible dialog properties that are saved by user on this machine.
		dialogProperties = SdaiSession.getSession().loadApplicationProperties ( SdaiFileChooser.class);

		fileChooserDlg.setMultiSelectionEnabled(false);
//		fileChooserDlg.setPreferredSize(new Dimension(500,600));

		previewPanel = new PreviewPanel();
		fileChooserDlg.setAccessory(previewPanel);

		fileChooserDlg.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
					File f = (File)e.getNewValue();
					if (f != null) {
                        previewFile(f);
                    }
				}
			}
		});

/*		fileChooserDlg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
			}
		});*/
		String filters[] = {"stp", "p21", "pf", "step", "2*"};
		SimpleFileFilter sff = new SimpleFileFilter(filters, "STEP files");
//		setFileFilter(sff);
		fileChooserDlg.addChoosableFileFilter(sff);

		// now, try to determine what filter should be pre-set before displaying dialog:
		String usedFilter = dialogProperties.getProperty(lastUsedFileFilter);
		if (usedFilter != null && usedFilter.equals (allFilesFilterUsed)) {
			fileChooserDlg.setFileFilter(fileChooserDlg.getAcceptAllFileFilter());
		}
		else {
			fileChooserDlg.setFileFilter(sff);
		}

		// now let's set the default directory for display. Note that in case properties file was not found,
		// it's possible to have NULL as current directory, thus we must workaround possible exception.
		String dirPath = dialogProperties.getProperty (lastUsedDirectory);
		dirPath = (dirPath == null) ? "" : dirPath;
		fileChooserDlg.setCurrentDirectory (new File (dirPath));

		// and add action listener to listen for 'accept' click event to make sure we save the latest
		// used directory.
		fileChooserDlg.addActionListener ( new AbstractAction () {
			public void actionPerformed (ActionEvent e) {
				try {
					if ( e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
						// determine currently selected directory:
						String newDefDirectory = fileChooserDlg.getCurrentDirectory().getPath();
						dialogProperties.setProperty (lastUsedDirectory, newDefDirectory);
						// determine currently selected file filter:
						if (fileChooserDlg.getFileFilter() == fileChooserDlg.getAcceptAllFileFilter()) {
							dialogProperties.setProperty(lastUsedFileFilter, allFilesFilterUsed);
						}
						else {
							dialogProperties.setProperty(lastUsedFileFilter, stepFilesFilterUsed);
						}
						SdaiSession.getSession().storeApplicationProperties ( SdaiFileChooser.class, dialogProperties,"");
						dialogResult = JFileChooser.APPROVE_OPTION;
						dispose();
					} else if ( e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
						dialogResult = JFileChooser.CANCEL_OPTION;
						dispose();
					}
				}
				catch ( SdaiException error) {
					System.out.println (error);
				}
			}
		});

		// Repository parameter panel
		pRepositoryImport = new JPanel();
		pRepositoryImport.setLayout(new BoxLayout(pRepositoryImport, BoxLayout.Y_AXIS));
		pRepositoryImport.add(Box.createVerticalStrut(5));

		rbAuto    = new JRadioButton("Auto");
		rbSpecify = new JRadioButton("Specify");
		rbTemp    = new JRadioButton("Temp");

		rbAuto.setActionCommand("AutoImport");
		rbSpecify.setActionCommand("SpecifyImport");
		rbTemp.setActionCommand("TempImport");

		RepositoryImportActionListener aActionListener =
			new RepositoryImportActionListener();

		rbAuto.addActionListener(aActionListener);
		rbSpecify.addActionListener(aActionListener);
		rbTemp.addActionListener(aActionListener);

		rbTemp.setSelected(true);

		ButtonGroup aGroup = new ButtonGroup();
		aGroup.add(rbAuto);
		aGroup.add(rbSpecify);
		aGroup.add(rbTemp);

		Box aBox2 = new Box(BoxLayout.X_AXIS);
		aBox2.add(Box.createHorizontalGlue());
		aBox2.add(rbAuto);
		aBox2.add(rbSpecify);
		aBox2.add(rbTemp);
		aBox2.add(Box.createHorizontalGlue());

		pRepositoryImport.add(aBox2);
		pRepositoryImport.setMaximumSize(new Dimension(pRepositoryImport.getMaximumSize().width, pRepositoryImport.getPreferredSize().height));
		pRepositoryImport.add(Box.createVerticalStrut(5));

		JPanel labelsFields = new JPanel();
		labelsFields.setLayout(new GridBagLayout());

		repositoryNameLabel = new JLabel("Repository name:");
		tfRepositoryImportName = new JTextField();
		tfRepositoryImportName.setEditable(false);


		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 0, 5);
      c.anchor = GridBagConstraints.WEST;
      labelsFields.add(repositoryNameLabel, c);

      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(0, 0, 0, 5);
      c.weightx = 1.0;
      labelsFields.add(tfRepositoryImportName, c);

      ////////////////////////////////////////////////////////////////////////////////
//		JPanel repoLocationButtonPanel = new JPanel();
//		repoLocationButtonPanel.setLayout(new BoxLayout(repoLocationButtonPanel, BoxLayout.X_AXIS));

		rbLocal   = new JRadioButton("Local");
		rbRemote  = new JRadioButton("Remote");

		rbLocal.setActionCommand("Local");
		rbRemote.setActionCommand("Remote");

		rbLocal.addActionListener(aActionListener);
		rbRemote.addActionListener(aActionListener);

		rbLocal.setSelected(true);
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rbLocal);
		bGroup.add(rbRemote);

//		repoLocationButtonPanel.add(Box.createHorizontalGlue());
//		repoLocationButtonPanel.add(rbLocal);
//		repoLocationButtonPanel.add(rbRemote);
//		repoLocationButtonPanel.add(Box.createHorizontalGlue());

		///////////////////////////////////////////////////////////////////////////////

		// add new buttons:
//		c.anchor = GridBagConstraints.CENTER;
//		c.gridwidth = GridBagConstraints.REMAINDER;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.insets = new Insets(0, 0, 0, 0);
//		c.weightx = 0.0;
//		labelsFields.add(repoLocationButtonPanel, c);

//      repositoryLocationLabel = new JLabel("Repository location:");
      tfRepositoryImportLocation = new JTextField();
//		tfRepositoryImportLocation.setEditable(false);
//
//		c.insets = new Insets(0, 5, 0, 5);
//      c.anchor = GridBagConstraints.WEST;
//      c.gridwidth = 1;
//
//		labelsFields.add(repositoryLocationLabel, c);

//		c.gridwidth = GridBagConstraints.REMAINDER;
//      c.anchor = GridBagConstraints.WEST;
//      c.fill = GridBagConstraints.HORIZONTAL;
//      c.insets = new Insets(0, 0, 0, 5);
//      c.weightx = 1.0;
//		labelsFields.add(tfRepositoryImportLocation, c);

		pRepositoryImport.add(labelsFields);
		pRepositoryImport.add(Box.createVerticalStrut(5));

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(fileChooserDlg);
		getContentPane().add(pRepositoryImport);
		setSize(new Dimension(500,600));

		//getContentPane().add(pRepositoryImport);
		// This part is L&F dependant therefore it should not be used here
/*		int componentCount = getComponentCount();
		LayoutManager lm = getLayout();
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Component component = getComponent(componentCount - 1);

		if (component instanceof JPanel) {
			JPanel panel = (JPanel) component;
			panel.add(pRepositoryImport);
		} else if (component instanceof Box.Filler) {
			add(pRepositoryImport, componentCount - 1);
		} else {
			add(pRepositoryImport, componentCount);
		}*/
	}

	public int showDialog(Component parent, String approveButtonText) {
		//return fileChooserDlg.showDialog(parent, approveButtonText);
		fileChooserDlg.setApproveButtonText(approveButtonText);
		WindowCenterer.showCentered(parent, this);

		return dialogResult;
	}

	public File getSelectedFile() {
		return fileChooserDlg.getSelectedFile();
	}

	public File getCurrentDirectory() {
		return fileChooserDlg.getCurrentDirectory();
	}

	void previewFile(File f) {
		previewPanel.tfName.setText(f.getName());
		previewPanel.tfSize.setText(String.valueOf(f.length())+" bytes");
		GregorianCalendar cl = new GregorianCalendar();
		cl.setTime(new Date(f.lastModified()));

		//previewPanel.tfModificationDate.setText((new Date(f.lastModified())).toString());
		previewPanel.tfModificationDate.setText(Date2String.convertDate(cl));

		previewPanel.header.initTextArea(f);
	}

	public String getRepositoryName() {
		switch(getRepositoryType()) {
			case REPOSITORY_TYPE_AUTO:
				return null;
			case REPOSITORY_TYPE_SPECIFY:
				return tfRepositoryImportName.getText();
			default:
				return "";
		}
	}

	public int getRepositoryType() {
		if(rbAuto.isSelected())
			return REPOSITORY_TYPE_AUTO;
		else if(rbSpecify.isSelected())
			return REPOSITORY_TYPE_SPECIFY;
		else
			return REPOSITORY_TYPE_TEMP;
	}

	public String getRepositoryLocation() {
		String locationString = tfRepositoryImportLocation.getText();
		return locationString.equals("") ? null : locationString;
	}

	public void setRepositoryLocationVisible(boolean visible) {
		Thread.dumpStack();
		// ignore this feature: it's obsolete
		//repositoryLocationLabel.setVisible(visible);
		//tfRepositoryImportLocation.setVisible(visible);
	}

	class PreviewPanel extends JPanel {
//		JTextField tfCretionDate;
		JTextField tfModificationDate;
		JTextField tfSize;
		JTextField tfName;

		TextPreviewer header;

		PreviewPanel() {
		   setLayout(new BorderLayout());
		   setBorder(new TitledBorder("Preview"));

		   JPanel pFile = new JPanel(new BorderLayout());
		   JPanel pLabels = new JPanel(new GridLayout(3, 1));
//		   pLabels.add(new JLabel("Creation date"));
	      pLabels.add(new JLabel("Name"));
		   pLabels.add(new JLabel("Size"));
		   pLabels.add(new JLabel("Modification date"));
			pFile.add(pLabels, BorderLayout.WEST);

		   JPanel pTexts = new JPanel(new GridLayout(3, 1));
//		   tfCretionDate = new JTextField();
//		   pTexts.add(tfCretionDate);
		   tfName = new JTextField();
		   pTexts.add(tfName);
		   tfName.setEditable(false);
		   tfSize = new JTextField();
		   pTexts.add(tfSize);
		   tfSize.setEditable(false);
		   tfModificationDate = new JTextField();
		   pTexts.add(tfModificationDate);
		   tfModificationDate.setEditable(false);
			pFile.add(pTexts, BorderLayout.CENTER);

		   add(pFile, BorderLayout.NORTH);
		   header = new TextPreviewer();
		   add(header, BorderLayout.CENTER);
		}
	}

	class TextPreviewer extends JComponent {
		private JTextArea textArea = new JTextArea();
		private JScrollPane scroller = new JScrollPane(textArea);
//		private char[] buff = new char[1000];
//		private Color bg;

		TextPreviewer() {
			try {
			   setPreferredSize(new Dimension(250,250));
				textArea.setEditable(false);
//				if ((bg = UIManager.getColor("TextArea.background")) != null)
//					textArea.setBackground(bg);
//				else
//					textArea.setBackground(Color.white);
				setBorder(BorderFactory.createTitledBorder("Header"));
				setLayout(new BorderLayout());
				add(scroller, BorderLayout.CENTER);
			}
			catch (NullPointerException np) {
				// layout can throw exceptions sometimes: ignore
			}
		}

		public void initTextArea(File file) {
			textArea.setText(contentsOfFile(file));
			textArea.setCaretPosition(0);
		}

		public void clear() {
			textArea.setText("");
		}

		private String contentsOfFile(File file) {
			if (file == null) {
				return "";
			}
			if (file.getName().equals("")) {
				return "";
			}

			String s = new String();
			try {
				boolean justPartLoaded = false;
				char buf[] = null;
				long size = file.length();
				if (size > Integer.MAX_VALUE)	{
					// file to big to be loaded into memory.
					// try to read first strokes from it-
					buf = new char[Integer.MAX_VALUE];
					justPartLoaded = true;
					// if file starts from prefix of part21 file,
					// then recognized it as part21 file.
				}
				else {
					buf = new char[(int) size];
				}
				FileReader reader = new FileReader(file);
				int nch = reader.read(buf, 0, buf.length);
				if (nch != -1) {
					s = new String(buf, 0, nch);
					// try to find endsec;
					int endSec = s.indexOf("ENDSEC;");
					if ((endSec == -1) && (justPartLoaded)) {
						// look for starting string:
						if (s.indexOf("ISO-10303-21;") != -1)
							return "STEP file header too big for preview.";
						else
							return "Not a STEP file.";
					}
					else
				   	s = s.substring(0, (s.indexOf("ENDSEC;") == -1)?0:s.indexOf("ENDSEC;")+7);
				}
				reader.close();
			}
			catch (IOException iox) {
				s = "";
		   }
			return (s.length()==0)?"Not a STEP file.":s;
		}
	}

	private class RepositoryImportActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton source = (JRadioButton)e.getSource();
			if (source.getActionCommand().equals("AutoImport")) {
				tfRepositoryImportName.setEditable(false);
				tfRepositoryImportName.setText("");
				return;
			}

			if (source.getActionCommand().equals("SpecifyImport")) {
				tfRepositoryImportName.setEditable(true);
				tfRepositoryImportName.setText("");
				return;
			}

			if (source.getActionCommand().equals("TempImport")) {
				tfRepositoryImportName.setEditable(false);
				tfRepositoryImportName.setText("");
				return;
			}

			if (source.getActionCommand().equals("Local")) {
				tfRepositoryImportLocation.setEditable(false);
				tfRepositoryImportLocation.setText("");
				return;
			}

			if (source.getActionCommand().equals("Remote")) {
				tfRepositoryImportLocation.setEditable(true);
				tfRepositoryImportLocation.setText("//guest:passwd@localhost");
				return;
			}

		}
	}

}






