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

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.beans.*;
import java.text.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;

import jsdai.lang.*;
import jsdai.util.*;

public class SdaiSessionBean extends SdaiPanel {
	JFrame frame;
	SdaiSessionBean self = this;
	SdaiSession session;

	JPanel pedit = new JPanel();

	JComboBox garbage = new JComboBox();
	JTextField repoName = new JTextField();
	JTextField location = new JTextField();

	JRadioButton off = new JRadioButton("Off");
	JRadioButton readOnly = new JRadioButton("R/O");
	JRadioButton readWrite = new JRadioButton("R/W");

	JButton	bCommit = new JButton("Commit");
	JButton bAbort = new JButton("Abort");

	JButton		bImport = new JButton("Import..."),
				bExport = new JButton("Export..."),
				bLink = new JButton("Link..."),
//				bServer = new JButton("Server..."),
				bUnlink = new JButton("Unlink"),
				bCreate = new JButton("Create"),
				bDelete = new JButton("Delete");

	JCheckBox isNull = new JCheckBox("Name", false);

	GoTable repositories;
	TableSorter sorter = new TableSorter();
	RepositoryTableModel repositoriesModel = new RepositoryTableModel();
	SdaiServerBean sdaiServerBean;

	String currentDir = "";
	String conString = "";

	private String getRepoName() {
		String result = null;
		switch (garbage.getSelectedIndex()) {
			case 0 :
				result = null;
				break;
			case 1 :
				result  = repoName.getText();
				break;
			case 2 :
				result = "";
				break;
		}
		return result;
	}

	public SdaiSessionBean(JFrame frame) throws SdaiException {
		this.frame = frame;
		
		setLayout(new BorderLayout());

		sdaiServerBean = new SdaiServerBean(frame);
		JPanel repo = new JPanel(new BorderLayout());
		repo.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Known repositories"));

		repositories = new GoTable(sorter);
		repositories.addGoListener(goListener);
		bExport.setEnabled(false);
//		bUnlink.setEnabled(false);
		bDelete.setEnabled(false);
		repositories.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			    refreshButtonsData();
			}
		});
		sorter.addMouseListenerToHeaderInTable(repositories);
		sorter.setModel(repositoriesModel);
		sorter.fireTableStructureChanged();
		sorter.lock(0);
		sorter.sortByColumn(2);
		repositories.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
      			try {
					if (e.getClickCount() == 1) {
	      				JTable ta = (JTable)e.getSource();
	      				if (ta.getSelectedColumn() == 0) {
      						setHourGlass(true);
							SdaiRepository rep = (SdaiRepository)ta.getModel().getValueAt(ta.getSelectedRow(), -1);
	      					if (rep.isActive()) {
	      						rep.closeRepository();
	      					} else {
	      						rep.openRepository();
	      					}
	      				}
					}
	         	} catch (SdaiException h) {
					processMessage(h);
				} finally {
      				setHourGlass(false);
				}
			}
		});
//		repositories.addKeyListener(findKeyListener);
//		repositories.addKeyListener(keyListener);
//		repositories.addMouseListener(mouseListener);
//		repositories.getSelectionModel().addListSelectionListener(listListener);
//		repositories.addFocusListener(focusListener);
		repositories.setBackground(getBackground());

		PropertyChangeListener pcl = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				garbage.setPreferredSize(new Dimension(repositories.getColumnModel().getColumn(0).getWidth()+repositories.getColumnModel().getColumn(1).getWidth(), (int)garbage.getPreferredSize().getHeight()));
				repoName.setPreferredSize(new Dimension(repositories.getColumnModel().getColumn(2).getWidth(), (int)repoName.getPreferredSize().getHeight()));
				location.setPreferredSize(new Dimension(repositories.getColumnModel().getColumn(3).getWidth(), (int)location.getPreferredSize().getHeight()));
				pedit.revalidate();
			}
		};
		repositories.getColumnModel().getColumn(0).addPropertyChangeListener(pcl);
		repositories.getColumnModel().getColumn(1).addPropertyChangeListener(pcl);
		repositories.getColumnModel().getColumn(2).addPropertyChangeListener(pcl);
		repositories.getColumnModel().getColumn(3).addPropertyChangeListener(pcl);

		repositories.getColumnModel().getColumn(0).setCellRenderer(new BoolTableCellRenderer());
		repositories.getColumnModel().getColumn(1).setCellRenderer(new BoolTableCellRenderer());
		repositories.getColumnModel().getColumn(0).setPreferredWidth(100);
		repositories.getColumnModel().getColumn(1).setPreferredWidth(100);
		repositories.getColumnModel().getColumn(2).setPreferredWidth(400);
		repositories.getColumnModel().getColumn(3).setPreferredWidth(400);
		repositories.sizeColumnsToFit(-1);

		repo.add(new JScrollPane(repositories), BorderLayout.CENTER);

		JPanel editFields = new JPanel(new BorderLayout());
		((BorderLayout)editFields.getLayout()).setVgap(5);

		pedit.setLayout(new BoxLayout(pedit, BoxLayout.X_AXIS));
		garbage.addItem("default");
		garbage.addItem("specify");
		garbage.addItem("temporary");
		garbage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repoName.setEditable(garbage.getSelectedIndex() == 1);
				switch (garbage.getSelectedIndex()) {
					case 0 :
						repoName.setText("default");
						break;
					case 1 :
						repoName.setText("");
						break;
					case 2 :
						repoName.setText("temporary");
						break;
				}
			}
		});
		garbage.setSelectedIndex(0);
		repoName.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				refreshButtonsData();
			}
			public void insertUpdate(DocumentEvent e) {
				refreshButtonsData();
			}
			public void removeUpdate(DocumentEvent e) {
				refreshButtonsData();
			}
		});
		repoName.setEditable(garbage.getSelectedIndex() == 1);
		garbage.setPreferredSize(new Dimension((int)garbage.getPreferredSize().getWidth(), repositories.getRowHeight()+2));
		repoName.setPreferredSize(new Dimension((int)repoName.getPreferredSize().getWidth(), repositories.getRowHeight()+2));
		location.setPreferredSize(new Dimension((int)location.getPreferredSize().getWidth(), repositories.getRowHeight()+2));
		pedit.add(garbage);
		pedit.add(repoName);
		pedit.add(location);
		editFields.add(pedit, BorderLayout.NORTH);

/*		JPanel plabels = new JPanel(new GridLayout(0, 1));
		((GridLayout)plabels.getLayout()).setVgap(5);
		isNull.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repoName.setEditable(!isNull.isSelected());
				repoName.setText((isNull.isSelected())?"null":"");
			}
		});
		plabels.add(isNull);
		JLabel lala = (JLabel)plabels.add(new JLabel("Location"));
		isNull.setForeground(lala.getForeground());
		JPanel ptextes = new JPanel(new GridLayout(0, 1));
		((GridLayout)ptextes.getLayout()).setVgap(5);
		ptextes.add(repoName);
		ptextes.add(location);
		editFields.add(plabels, BorderLayout.WEST);
		editFields.add(ptextes, BorderLayout.CENTER);*/

		JPanel box = new JPanel(new GridLayout(1, 0));
		bImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (session.isModified()) {
						processMessage("Transaction will be commited automatically after import.");
					}
					SdaiFileChooser chooser = new SdaiFileChooser("Choose file to import");

					if (chooser.showDialog(SdaiSessionBean.this, "Import") == JFileChooser.APPROVE_OPTION) {
						setHourGlass(true);
//						session.importClearTextEncoding(getRepoName(), chooser.getSelectedFile().toString(), (location.getText().equals(""))?null:location.getText());
						RepositoryImportResult rir = new RepositoryImportResult();
						SdaiRepository rrepo = rir.importRepo(chooser.getRepositoryName(), chooser.getSelectedFile().toString(), chooser.getRepositoryLocation());
						session.getActiveTransaction().commit();
						currentDir = chooser.getCurrentDirectory().getPath();
//						int i = sorter.getIndexAt(repositoriesModel.findValueIndex(rrepo));
//						if (i != -1) {
//							repositories.setRowSelectionInterval(i, i);
//							fireSdaiSelectionProcessed();
//						}
				        fireGo(new GoEvent(thisis, rrepo, "Repository"));
					}
				} catch (SdaiException h) {
					processMessage(h);
				} finally {
					repositoriesModel.fireTableDataChanged();
					setHourGlass(false);
				}
			}
		});
		box.add(bImport);
		bExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(currentDir);
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile().exists()) {
						if (JOptionPane.showConfirmDialog(self, "File: "+chooser.getSelectedFile()+" already exsist. Overwrite it?", "Warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
							return;
						}
					}
					try {
						setHourGlass(true);
						SdaiRepository rep = (SdaiRepository)repositories.getModel().getValueAt(repositories.getSelectedRow(), -1);
						if (!rep.isActive()) {
							rep.openRepository();
						}
						rep.exportClearTextEncoding(chooser.getSelectedFile().toString());
						currentDir = chooser.getCurrentDirectory().getPath();
					} catch (SdaiException h) {
						processMessage(h);
					} finally {
						setHourGlass(false);
					}
				}
			}
		});
		box.add(bExport);
		sdaiServerBean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals(SdaiServerBean.LINK_LINKED)
				|| e.getActionCommand().equals(SdaiServerBean.UNLINK_UNLINKED)) {
					repositoriesModel.fireTableDataChanged();
				}
      		}
		});
//		bLink.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser chooser = new JFileChooser(currentDir);
//				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//					try {
//						SdaiRepository rrepo = session.linkRepository(getRepoName(), chooser.getCurrentDirectory().toString());
////						int i = sorter.getIndexAt(repositoriesModel.findValueIndex(rrepo));
////						repositories.setRowSelectionInterval(i, i);
////						fireSdaiSelectionProcessed();
////						paintAll(getGraphics());
//						currentDir = chooser.getCurrentDirectory().getPath();
//				        fireGo(new GoEvent(thisis, rrepo, "Repository"));
//					} catch (SdaiException h) {
//						processMessage(h);
//					} finally {
//						repositoriesModel.fireTableDataChanged();
//					}
//      		}
//			}
//		});
		sdaiServerBean.attachLinkButton(bLink);
		box.add(bLink);
//		bServer.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					SdaiServerBean server = new SdaiServerBean(null);
//					server.showDialog();
//				} catch (SdaiException h) {
//					processMessage(h);
//				}
//			}
//		});
//		box.add(bServer);
//		bUnlink.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					SdaiRepository rep = (SdaiRepository)repositories.getModel().getValueAt(repositories.getSelectedRow(), -1);
//					rep.unlinkRepository();
//					repositoriesModel.fireTableDataChanged();
//				} catch (SdaiException h) {
//					processMessage(h);
//				}
//			}
//		});
		sdaiServerBean.attachUnlinkButton(bUnlink);
		box.add(bUnlink);
		bCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiRepository rrepo = session.createRepository(getRepoName(), (location.getText().equals(""))?null:location.getText());
					repositoriesModel.fireTableDataChanged();
//					int i = sorter.getIndexAt(repositoriesModel.findValueIndex(rrepo));
//					repositories.setRowSelectionInterval(i, i);
//					fireSdaiSelectionProcessed();
				    fireGo(new GoEvent(thisis, rrepo, "Repository"));
				} catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		box.add(bCreate);
		bDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiRepository rep = (SdaiRepository)repositories.getModel().getValueAt(repositories.getSelectedRow(), -1);
					rep.deleteRepository();
					repositoriesModel.fireTableDataChanged();
				} catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		box.add(bDelete);
		editFields.add(box, BorderLayout.SOUTH);
		repo.add(editFields, BorderLayout.SOUTH);
		add(repo, BorderLayout.CENTER);

		JPanel tran = new JPanel();
		tran.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Transaction"));
		tran.setLayout(new BoxLayout(tran, BoxLayout.X_AXIS));
		ButtonGroup group = new ButtonGroup();
		off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (session.testActiveTransaction()) {
						session.getActiveTransaction().endTransactionAccessCommit();
					}
					refreshTransactionData();
				}
				catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		group.add(off);
		tran.add(off);
		readOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (session.testActiveTransaction()) {
						SdaiTransaction transaction = session.getActiveTransaction();
						if (transaction.getMode() != SdaiTransaction.READ_ONLY) {
							session.getActiveTransaction().endTransactionAccessCommit();
							session.startTransactionReadOnlyAccess();
						}
					}
					else {
						session.startTransactionReadOnlyAccess();
					}
					refreshTransactionData();
				}
				catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		group.add(readOnly);
		tran.add(readOnly);
		readWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (session.testActiveTransaction()) {
						SdaiTransaction transaction = session.getActiveTransaction();
						if (transaction.getMode() != 2) {
							session.getActiveTransaction().endTransactionAccessCommit();
							session.startTransactionReadWriteAccess();
						}
					}
					else {
						session.startTransactionReadWriteAccess();
					}
					refreshTransactionData();
				}
				catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		group.add(readWrite);
		tran.add(readWrite);
		bCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					session.getActiveTransaction().commit();
					refreshTransactionData();
				}
				catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		bCommit.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		tran.add(bCommit);
		bAbort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					session.getActiveTransaction().abort();
					refreshTransactionData();
				}
				catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		bAbort.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		tran.add(bAbort);
		add(tran, BorderLayout.SOUTH);
	}

	public SdaiSessionBean() throws SdaiException {
		this((JFrame)null);
	}
	public SdaiSessionBean(SdaiSession session) throws SdaiException {
		this((JFrame)null);
		setSession(session);
	}

	public void setSession(SdaiSession session) throws SdaiException {
		if (this.session == session) return;
		try {
			this.session = session;
			if (session.getSession() == null) {
				session.openSession();
			}
			if (session.testActiveTransaction()) {
				if (session.getActiveTransaction().getMode() == 1) {
					readOnly.setSelected(true);
				} else {
					readWrite.setSelected(true);
				}
			} else {
				off.setSelected(true);
			}
			refreshData();
		} catch (SdaiException ex) { processMessage(ex); }
	}

	public void refreshData() {
		try {
			repositoriesModel.setSession(session);
			refreshButtonsData();
			refreshTransactionData();
		} catch (SdaiException e) { processMessage(e); }
	}

	private void refreshButtonsData() {
		int index = repositories.getSelectedRow();
		bExport.setEnabled((index >= 0));
//		bUnlink.setEnabled((index >= 1));
		bDelete.setEnabled((index >= 1));

		bImport.setEnabled(!repoName.getText().equals(""));
//		bLink.setEnabled(!repoName.getText().equals(""));
//		bServer.setEnabled(!repoName.getText().equals(""));
		bCreate.setEnabled(!repoName.getText().equals("") && garbage.getSelectedIndex() != 0);
	}

	private void refreshTransactionData() throws SdaiException {
		int mode = (session.testActiveTransaction())?session.getActiveTransaction().getMode():SdaiTransaction.NO_ACCESS;
		switch (mode) {
			case SdaiTransaction.NO_ACCESS :
				off.setSelected(true);
				break;
			case SdaiTransaction.READ_ONLY :
				readOnly.setSelected(true);
				break;
			case SdaiTransaction.READ_WRITE :
				readWrite.setSelected(true);
				break;
		}
		boolean isPending = session.isModified();
		off.setEnabled(!isPending);
		readOnly.setEnabled(!isPending);
		bCommit.setEnabled(isPending);
		bAbort.setEnabled(isPending);
	}

	public SdaiSession getSession() throws SdaiException {
		return session;
	}

	public String getTreeLeave() throws SdaiException {
		return "/";
	}

	public int getMode()  throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection == repositories) {
			mode = mCREATE_DELETE;
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
//need implement lastselection
		result.add(repositories.getModel().getValueAt(repositories.getSelectedRow(), -1));
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (repositories.hasFocus()) {
			result = (repositories.getSelectedRowCount() != 0);
		}
		return result;
	}

/*	private void setTransectionEnabled(boolean enabled) {
		bCommit.setEnabled(enabled);
		bAbort.setEnabled(enabled);
	}*/

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
	};

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
	};

	public void sdaiNew() throws SdaiException {
		session.createRepository((isNull.isSelected())?null:repoName.getText(), (location.getText().equals(""))?null:location.getText());
//		paintAll(getGraphics());
	};

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection == repositories) {
			if (repositories.getSelectedRow() != -1) {
				SdaiRepository rep = (SdaiRepository)repositories.getModel().getValueAt(repositories.getSelectedRow(), -1);
				rep.deleteRepository();
			}
		}
//		paintAll(getGraphics());
	};

	public void setProperties(Properties props) {
		currentDir = props.getProperty("sessionPage.dir", System.getProperty("user.dir"));
		conString = props.getProperty("sessionPage.con");
	}

	public void getProperties(Properties props) {
		props.setProperty("sessionPage.dir", (currentDir == null)?"":currentDir);
		props.setProperty("sessionPage.con", (conString == null)?"":conString);
	}

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - Session - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
			result += "-Repositories-\n";
			for (int j = 0; j < repositories.getColumnCount(); j++) {
				result += repositories.getColumnName(j)+"\t";
			}
			result += "\n";
			for (int i = 0; i < repositories.getRowCount(); i++) {
				for (int j = 0; j < repositories.getColumnCount(); j++) {
					result += repositories.getValueAt(i, j)+"\t";
				}
				result += "\n";
			}
  		} catch (SdaiException ex) { processMessage(ex); }
		return result;
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			Object value = e.getValue();
			if (value instanceof SdaiRepository) {
				fireGo(new GoEvent(thisis, value, "Repository"));
			}
		}
	};
}
