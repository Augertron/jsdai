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

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.beans.*;
import java.text.*;

import jsdai.dictionary.*;
import jsdai.util.*;
import jsdai.lang.*;

public class SdaiRepositoryBean extends SdaiPanel {
	SdaiRepository repository;

	//top

	JTextField name = new JTextField();
	JTextField location = new JTextField();
	JTextField time = new JTextField();
	JCheckBox modified = new JCheckBox("Modified");

	//models

	GoTable models;
	TableSorter sorter = new TableSorter();
	ModelTableModel modelsModel = new ModelTableModel();

	JComboBox cellEditor = new JComboBox();

	JButton buttonFind = new JButton("Find");
	JTextField textSearch = new JTextField();

	JTextField modelName = new JTextField();
	JComboBox modelSchema = new JComboBox();
	AggregateListModel modelModelSchemas = new AggregateListModel();

	JButton modelCreate = new JButton("Create");
	JButton modelDelete = new JButton("Delete");

	//schemas

	GoTable schemas;
	SchemaTableModel schemasModel = new SchemaTableModel();

	JTextField schemaName = new JTextField();
	JComboBox schemaSchema = new JComboBox();
	AggregateListModel modelSchemaSchemas = new AggregateListModel();

	JButton schemaCreate = new JButton("Create");
	JButton schemaDelete = new JButton("Delete");

	//info

	JTextField authorization = new JTextField();
	JTextField preprocesor = new JTextField();
	JTextField system = new JTextField();


	A_stringBean description = new A_stringBean();
	A_stringBean author = new A_stringBean();
	A_stringBean organization = new A_stringBean();


	JButton infoEdit = new JButton("Edit");
	JButton infoAccept = new JButton("Accept");
	JButton infoCancel = new JButton("Cancel");


	EEntity_definition def;

	JPanel pedit = new JPanel();
	JPanel peditSchema = new JPanel();
	JPanel modelgarbage = new JPanel(new BorderLayout());
	JPanel editFieldsSchema = new JPanel(new BorderLayout());

	public SdaiRepositoryBean() {
		setLayout(new BorderLayout());
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel title = new JPanel(bl);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Name"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Location"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Time stamp"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		title.add(labels, BorderLayout.WEST);
		JPanel texts = new JPanel(gl);
		name.setEditable(false);
		texts.add(name);
		location.setEditable(false);
		texts.add(location);
		time.setEditable(false);
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		modified.setHorizontalAlignment(JLabel.RIGHT);
		modified.setEnabled(false);
		modified.setPreferredSize(new Dimension((int)modified.getPreferredSize().getWidth(), 13));
		modified.setMinimumSize(new Dimension((int)modified.getMinimumSize().getWidth(), 13));
		p1.add(time);
		p1.add(modified);
		texts.add(p1);
		title.add(texts, BorderLayout.CENTER);
		add(title, BorderLayout.NORTH);

		JTabbedPane pane = new JTabbedPane();
//model tab
		JPanel model = new JPanel(new BorderLayout());
	//table
		sorter.setModel(modelsModel);
		models = new GoTable(sorter);
		models.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshModelButtons();
			}
		});
		models.addGoListener(goListener);
		sorter.addMouseListenerToHeaderInTable(models);
		sorter.fireTableStructureChanged();
		sorter.sortByColumn(2);
		models.setBackground(getBackground());
//		models.addKeyListener(findKeyListener);
//		models.addKeyListener(keyListener);
//		models.addMouseListener(mouseListener);
//		models.getSelectionModel().addListSelectionListener(listListener);
//		models.addFocusListener(focusListener);
		cellEditor.setEditable(false);
		cellEditor.addItem("r/o");
		cellEditor.addItem("r/w");
		cellEditor.addItem(" - ");
		cellEditor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					if (models.getSelectedRow() == -1) return;
					JComboBox cb = (JComboBox)e.getSource();
					SdaiModel mod = (SdaiModel)models.getModel().getValueAt(models.getSelectedRow(), -1);
					switch (cb.getSelectedIndex()) {
						case 0 :
							SimpleOperations.startReadOnlyAccess(mod);
							break;
						case 1 :
							SimpleOperations.startReadWriteAccess(mod);
							break;
						case 2 :
							SimpleOperations.endAccess(mod);
							break;
					}
				} catch (SdaiException ex) { processMessage(ex); }
			}
		});
		models.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cellEditor));
		models.getColumnModel().getColumn(0).setPreferredWidth(100);
		models.getColumnModel().getColumn(1).setPreferredWidth(100);
		models.getColumnModel().getColumn(2).setPreferredWidth(400);
		models.getColumnModel().getColumn(3).setPreferredWidth(400);
		models.getColumnModel().getColumn(1).setCellRenderer(new BoolTableCellRenderer());
		models.sizeColumnsToFit(-1);
		PropertyChangeListener pcl = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				modelgarbage.setPreferredSize(new Dimension(models.getColumnModel().getColumn(0).getWidth()+models.getColumnModel().getColumn(1).getWidth(), (int)modelgarbage.getPreferredSize().getHeight()));
				modelName.setPreferredSize(new Dimension(models.getColumnModel().getColumn(2).getWidth(), (int)modelName.getPreferredSize().getHeight()));
				modelSchema.setPreferredSize(new Dimension(models.getColumnModel().getColumn(3).getWidth(), (int)modelSchema.getPreferredSize().getHeight()));
				pedit.revalidate();
			}
		};
		models.getColumnModel().getColumn(0).addPropertyChangeListener(pcl);
		models.getColumnModel().getColumn(1).addPropertyChangeListener(pcl);
		models.getColumnModel().getColumn(2).addPropertyChangeListener(pcl);
		models.getColumnModel().getColumn(3).addPropertyChangeListener(pcl);

		model.add(new JScrollPane(models), BorderLayout.CENTER);
	//panel edit
		JPanel editFieldsModel = new JPanel(new BorderLayout());
		pedit.setLayout(new BoxLayout(pedit, BoxLayout.X_AXIS));

		ActionListener searchAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EEntity result = null;
				try {
					result = repository.getSessionIdentifier(SimpleOperations.findIdentifier(textSearch.getText()));
                    Object s[] = new Object[2];
                    s[0] = result;
                    s[1] = result.findEntityInstanceSdaiModel();
                    fireGo(new GoEvent(thisis, s, "EntityInstance"));
                } catch (SdaiException ex) {
					processMessage(ex);
				}
//					lastSelection = textSearch;
//					fireSdaiSelectionProcessed();
			}
		};

		textSearch.addActionListener(searchAction);
		textSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				buttonFind.setEnabled(!textSearch.getText().equals(""));
			}
			public void insertUpdate(DocumentEvent e) {
				buttonFind.setEnabled(!textSearch.getText().equals(""));
			}
			public void removeUpdate(DocumentEvent e) {
				buttonFind.setEnabled(!textSearch.getText().equals(""));
			}
		});
		modelgarbage.add(textSearch, BorderLayout.CENTER);
		buttonFind.setEnabled(false);
		modelgarbage.add(buttonFind, BorderLayout.EAST);
		buttonFind.addActionListener(searchAction);


		ListSorter modelSorter = new ListSorter(modelModelSchemas);
		modelSorter.sort(true);
		modelSchema.setModel(modelSorter);

		modelSchema.setRenderer(new SdaiCellRenderer());
//		modelSchema.setKeySelectionManager(new Locator((SdaiCellRenderer)modelSchema.getRenderer()));
//		modelSchema.addKeyListener(keyListener);
//		modelSchema.addItemListener(itemListener);
//		modelSchema.addFocusListener(focusListener);
		modelSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshModelButtons();
			}
		});
		modelgarbage.setPreferredSize(new Dimension((int)modelgarbage.getPreferredSize().getWidth(), models.getRowHeight()+2));
		modelName.setPreferredSize(new Dimension((int)modelName.getPreferredSize().getWidth(), models.getRowHeight()+2));
		modelName.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				refreshModelButtons();
			}
			public void insertUpdate(DocumentEvent e) {
				refreshModelButtons();
			}
			public void removeUpdate(DocumentEvent e) {
				refreshModelButtons();
			}
		});
		modelSchema.setPreferredSize(new Dimension((int)modelSchema.getPreferredSize().getWidth(), models.getRowHeight()+2));
		pedit.add(modelgarbage);
		pedit.add(modelName);
		pedit.add(modelSchema);
		editFieldsModel.add(pedit, BorderLayout.NORTH);
	//bar
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		modelCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiModel m = (SdaiModel)modelSchema.getModel().getSelectedItem();
					SimpleOperations.enshureReadOnlyModel(m);
					SdaiModel mod = repository.createSdaiModel(modelName.getText(), m.getDefinedSchema());
					((TableModelListener)models.getModel()).tableChanged(new TableModelEvent(((TableSorter)models.getModel()).getModel()));
//					int i = sorter.getIndexAt(modelsModel.findValueIndex(mod));
//					models.setRowSelectionInterval(i, i);
//					fireSdaiSelectionProcessed();
					fireGo(new GoEvent(thisis, mod, "Model"));
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(modelCreate);
		modelDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (models.getSelectedRow() != -1) {
						SimpleOperations.enshureReadWriteTransaction();
						SdaiModel m = (SdaiModel)((TableModel)models.getModel()).getValueAt(models.getSelectedRow(), -1);
						m.deleteSdaiModel();
						((TableModelListener)models.getModel()).tableChanged(new TableModelEvent(((TableSorter)models.getModel()).getModel()));
						refreshModelButtons();
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(modelDelete);
		editFieldsModel.add(bar, BorderLayout.SOUTH);

		model.add(editFieldsModel, BorderLayout.SOUTH);
		pane.add("Models", model);
//schema tab
		JPanel pschema = new JPanel(new BorderLayout());
		TableSorter ssorter = new TableSorter(schemasModel);
		schemas = new GoTable(ssorter);
		schemas.addGoListener(goListener);
		ssorter.addMouseListenerToHeaderInTable(schemas);
		ssorter.fireTableStructureChanged();
		ssorter.sortByColumn(0);
		schemas.setBackground(getBackground());
//		schemas.addKeyListener(findKeyListener);
//		schemas.addKeyListener(keyListener);
//		schemas.addMouseListener(mouseListener);
//		schemas.getSelectionModel().addListSelectionListener(listListener);
//		schemas.addFocusListener(focusListener);
		schemas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshSchemaButtons();
			}
		});
		schemas.sizeColumnsToFit(-1);
		PropertyChangeListener spc = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				schemaName.setPreferredSize(new Dimension(schemas.getColumnModel().getColumn(0).getWidth(), (int)schemaName.getPreferredSize().getHeight()));
				schemaSchema.setPreferredSize(new Dimension(schemas.getColumnModel().getColumn(1).getWidth(), (int)schemaSchema.getPreferredSize().getHeight()));
				peditSchema.revalidate();
			}
		};
		schemas.getColumnModel().getColumn(0).addPropertyChangeListener(spc);
		schemas.getColumnModel().getColumn(1).addPropertyChangeListener(spc);
		pschema.add(new JScrollPane(schemas));

	//panel edit
		JPanel editFieldsSchema = new JPanel(new BorderLayout());
		peditSchema.setLayout(new BoxLayout(peditSchema, BoxLayout.X_AXIS));

		ListSorter schemaSorter = new ListSorter(modelSchemaSchemas);
		schemaSorter.sort(true);
		schemaSchema.setModel(schemaSorter);

//		schemaSchema.setModel(modelSchemaSchemas);
		schemaSchema.setRenderer(new SdaiCellRenderer());
//			schemaSchema.addKeyListener(keyListener);
//			schemaSchema.addItemListener(itemListener);
//			schemaSchema.addFocusListener(focusListener);
		schemaSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshSchemaButtons();
			}
		});
		schemaName.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				refreshSchemaButtons();
			}
			public void insertUpdate(DocumentEvent e) {
				refreshSchemaButtons();
			}
			public void removeUpdate(DocumentEvent e) {
				refreshSchemaButtons();
			}
		});
		schemaName.setPreferredSize(new Dimension((int)schemaName.getPreferredSize().getWidth(), schemas.getRowHeight()+2));
		schemaSchema.setPreferredSize(new Dimension((int)schemaSchema.getPreferredSize().getWidth(), schemas.getRowHeight()+2));
		peditSchema.add(schemaName);
		peditSchema.add(schemaSchema);
		editFieldsSchema.add(peditSchema, BorderLayout.NORTH);
	//bar
		JPanel barShema = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		schemaCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiModel m = (SdaiModel)schemaSchema.getModel().getSelectedItem();
					SimpleOperations.enshureReadOnlyModel(m);
					SchemaInstance sc = repository.createSchemaInstance(schemaName.getText(), m.getDefinedSchema());
					((TableModelListener)schemas.getModel()).tableChanged(new TableModelEvent(((TableSorter)schemas.getModel()).getModel()));
//					int i = schemasModel.findValueIndex(sc);
//					schemas.setRowSelectionInterval(i, i);
//					fireSdaiSelectionProcessed();
					fireGo(new GoEvent(thisis, sc, "SchemaInstance"));
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		barShema.add(schemaCreate);
		schemaDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (schemas.getSelectedRow() != -1) {
						SimpleOperations.enshureReadWriteTransaction();
						SchemaInstance schema = (SchemaInstance)((TableModel)schemas.getModel()).getValueAt(schemas.getSelectedRow(), -1);
						schema.delete();
						refreshSchemaButtons();
					}
					((TableModelListener)schemas.getModel()).tableChanged(new TableModelEvent(((TableSorter)schemas.getModel()).getModel()));
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		barShema.add(schemaDelete);
		editFieldsSchema.add(barShema, BorderLayout.SOUTH);
		pschema.add(editFieldsSchema, BorderLayout.SOUTH);

/*		((BorderLayout)editFieldsSchema.getLayout()).setHgap(5);
		JPanel plabels = new JPanel(new GridLayout(0, 1));
		((GridLayout)plabels.getLayout()).setVgap(5);
		plabels.add(new JLabel("Name"));
		plabels.add(new JLabel("Schema definition"));
		editFieldsSchema.add(plabels, BorderLayout.WEST);
		JPanel ptextes = new JPanel(new GridLayout(0, 1));
		((GridLayout)ptextes.getLayout()).setVgap(5);
		ptextes.add(schemaName);
		ptextes.add(schemaSchema);
		schemaSchema.setRenderer(new SdaiCellRenderer());
		schemaSchema.addFocusListener(focusListener);
		editFieldsSchema.add(ptextes, BorderLayout.CENTER);
		pschema.add(editFieldsSchema, BorderLayout.SOUTH);*/

		pane.add("SchemaInstances", pschema);
//repo info
		refreshInfoButtons(false);
		JPanel info = new JPanel(new BorderLayout());
		BorderLayout bl2 = new BorderLayout();
		bl2.setHgap(5);
		JPanel north = new JPanel(bl2);
		GridLayout gl2 = new GridLayout(0, 1);
		gl2.setVgap(5);
		JPanel labels2 = new JPanel(gl2);
		label = (JLabel)labels2.add(new JLabel("Authorization"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels2.add(new JLabel("Preprocessor_version"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels2.add(new JLabel("Originating_system"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels2, BorderLayout.WEST);
		JPanel texts2 = new JPanel(gl2);
		authorization.setEditable(false);
		texts2.add(authorization);
		preprocesor.setEditable(false);
		texts2.add(preprocesor);
		system.setEditable(false);
		texts2.add(system);
		north.add(texts2, BorderLayout.CENTER);
		info.add(north, BorderLayout.NORTH);
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JPanel pa;
		pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Description"));
		description.setEditable(false);
//		description.addSdaiSelectableListener(selectableListener);
		pa.add(description, BorderLayout.CENTER);
		center.add(pa);
		pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Author"));
		author.setEditable(false);
//		author.addSdaiSelectableListener(selectableListener);
		pa.add(author);
		center.add(pa);
		pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Organization"));
		organization.setEditable(false);
//		organization.addSdaiSelectableListener(selectableListener);
		pa.add(organization);
		center.add(pa);
		info.add(center, BorderLayout.CENTER);
	//bar
		JPanel infobar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		infoEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setByEdit(true);
					description.sdaiEdit();
					author.sdaiEdit();
					organization.sdaiEdit();
					refreshInfoButtons(true);
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		infobar.add(infoEdit);
		infoAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setByEdit(false);
					repository.setAuthorization(authorization.getText());
					repository.setOriginatingSystem(system.getText());
					repository.setPreprocessorVersion(preprocesor.getText());
					description.sdaiAccept();
					author.sdaiAccept();
					organization.sdaiAccept();
					refreshInfoButtons(false);
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		infobar.add(infoAccept);
		infoCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setByEdit(false);
					refreshInfoButtons(false);
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		infobar.add(infoCancel);
		info.add(infobar, BorderLayout.SOUTH);
		pane.add("Info", new JScrollPane(info));

		add(pane, BorderLayout.CENTER);
	}

	public SdaiRepositoryBean(SdaiRepository repository) throws SdaiException {
		this();
		setRepository(repository);
	}

//property repository
	public void setRepository(SdaiRepository repository) {
		try {
			if (!repository.isActive()) {
				repository.openRepository();
			}
//			if (this.repository == repository) return;
			this.repository = repository;
/*			ListSorter modelSorter = new ListSorter(new AggregateListModel(SdaiSession.getSession().getDataDictionary().getAssociatedModels()));
			modelSorter.sort(true);
			modelSchema.setModel(modelSorter);
			schemaSchema.setModel(new AggregateListModel());*/

//			refreshData();
		} catch (SdaiException ex) { processMessage(ex); }

//		paintAll(getGraphics());
	}

	public void refreshData() {
		try {
			modelModelSchemas.setAggregate(SdaiSession.getSession().getDataDictionary().getAssociatedModels());
			modelSchemaSchemas.setAggregate(SdaiSession.getSession().getDataDictionary().getAssociatedModels());
			if(repository != null) {
				name.setText(repository.getName());
				location.setText(repository.getLocation());

				modelsModel.setRepository(repository);

				schemasModel.setRepository(repository);

				modified.setSelected(repository.isModified());
				authorization.setText(repository.getAuthorization());
				preprocesor.setText(repository.getPreprocessorVersion());
				time.setText(repository.getChangeDate());
				system.setText(repository.getOriginatingSystem());
				description.setA_string(repository.getDescription());
				author.setA_string(repository.getAuthor());
				organization.setA_string(repository.getOrganization());
				refreshModelButtons();
				refreshSchemaButtons();
			}
		} catch (SdaiException e) { processMessage(e); }
	}

	private void refreshModelButtons() {
		modelCreate.setEnabled(!modelName.getText().equals("") && (modelSchema.getSelectedIndex() != -1));
		modelDelete.setEnabled(models.getSelectedRow() != -1);
	}

	private void refreshSchemaButtons() {
		schemaCreate.setEnabled(!schemaName.getText().equals("") && (schemaSchema.getSelectedIndex() != -1));
		schemaDelete.setEnabled(schemas.getSelectedRow() != -1);
	}

	private void refreshInfoButtons(boolean edit) {
	    infoEdit.setEnabled(!edit);
	    infoAccept.setEnabled(edit);
	    infoCancel.setEnabled(edit);
	}

//	private void refreshModes() {
//		for (int i = 0; i < models.getRowCount(); i++) {
//			SdaiModel m = (SdaiModel)models.getValueAt(-1, i);
//			if (models.setEdi
//		}
//	}
//
	public SdaiRepository getRepository() throws SdaiException {
		return repository;
	}

	public String getTreeLeave() throws SdaiException {
		if (repository == null) {
			return "";
		} else {
			return "/"+repository.getName();
		}
	}

	public int getMode() throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection == schemas) {
			mode = mCREATE_DELETE;
		} else if (lastSelection == models) {
			mode = mCREATE_DELETE;
		} else if (lastSelection == modelSchema) {
			mode = mSET_UNSET;
		} else if (lastSelection == schemaSchema) {
			mode = mSET_UNSET;
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == schemas) {
			result.add((SchemaInstance)schemas.getModel().getValueAt(schemas.getSelectedRow(), -1));
		} else if (lastSelection == models) {
			result.add((SdaiModel)models.getModel().getValueAt(models.getSelectedRow(), -1));
		} else if (lastSelection == modelSchema) {
			result.add(modelSchema.getModel().getSelectedItem());
		} else if (lastSelection == schemaSchema) {
			result.add(schemaSchema.getModel().getSelectedItem());
		} else if (lastSelection == textSearch) {
			try {
				result.add(repository.getSessionIdentifier(SimpleOperations.findIdentifier(textSearch.getText())));
			} catch (SdaiException ex) {
				processMessage(ex);
			}
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (schemas.hasFocus()) {
			result = (schemas.getSelectedRowCount() != 0);
		} else if (models.hasFocus()) {
			result = (models.getSelectedRowCount() != 0);
		}
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
		setByEdit(true);
		description.sdaiEdit();
		author.sdaiEdit();
		organization.sdaiEdit();
	};

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
		setByEdit(false);
		repository.setAuthorization(authorization.getText());
//		repository.setLocation(location.getText());
		repository.setOriginatingSystem(system.getText());
		repository.setPreprocessorVersion(preprocesor.getText());
		description.sdaiAccept();
		author.sdaiAccept();
		organization.sdaiAccept();
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
		setByEdit(false);
	};

	private void setByEdit(boolean isEdit) throws SdaiException {
		authorization.setEditable(isEdit);
		preprocesor.setEditable(isEdit);
		system.setEditable(isEdit);
		description.setEditable(isEdit);
		author.setEditable(isEdit);
		organization.setEditable(isEdit);
	}

	public void sdaiNew() throws SdaiException {
		if (lastSelection == models) {
			SdaiModel m = (SdaiModel)modelSchema.getModel().getSelectedItem();
			repository.createSdaiModel(modelName.getText(), m.getDefinedSchema());
			((TableModelListener)models.getModel()).tableChanged(new TableModelEvent(((TableSorter)models.getModel()).getModel()));
		} else if (lastSelection == schemas) {
			SdaiModel m = (SdaiModel)schemaSchema.getModel().getSelectedItem();
			repository.createSchemaInstance(schemaName.getText(), m.getDefinedSchema());
			((AbstractTableModel)schemas.getModel()).fireTableDataChanged();
		} else if (lastSelection == description) {
			description.sdaiNew();
		} else if (lastSelection == author) {
			author.sdaiNew();
		} else if (lastSelection == organization) {
			organization.sdaiNew();
		}
//		paintAll(getGraphics());
	};

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection == models) {
			if (models.getSelectedRow() != -1) {
				SdaiModel model = (SdaiModel)((TableModel)models.getModel()).getValueAt(models.getSelectedRow(), -1);
				model.deleteSdaiModel();
			}
		} else if (lastSelection == schemas) {
			if (schemas.getSelectedRow() != -1) {
				SchemaInstance schema = (SchemaInstance)((TableModel)schemas.getModel()).getValueAt(schemas.getSelectedRow(), -1);
				schema.delete();
			}
		} else if (lastSelection == description) {
			description.sdaiDestroy();
		} else if (lastSelection == author) {
			author.sdaiDestroy();
		} else if (lastSelection == organization) {
			organization.sdaiDestroy();
		}
//		paintAll(getGraphics());
	};

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - Repository - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Name:\t"+name.getText()+"\n";
		result += "Location:\t"+location.getText()+"\n";
		result += "Authorization:\t"+authorization.getText()+"\n";
		result += "Preprocessor version:\t"+preprocesor.getText()+"\n";
		result += "Originating system:\t"+system.getText()+"\n";
		result += "Time stamp:\t"+time.getText()+"\n";
		result += "Modified:\t"+String.valueOf(modified.isSelected())+"\n";
		result += "-Models-"+"\n";
		for (int j = 0; j < models.getColumnCount(); j++) {
			result += models.getColumnName(j)+"\t";
		}
		result += "\n";
		for (int i = 0; i < models.getRowCount(); i++) {
			for (int j = 0; j < models.getColumnCount(); j++) {
				result += models.getValueAt(i, j)+"\t";
			}
			result += "\n";
		}
		result += "-Schemas-"+"\n";
		for (int j = 0; j < schemas.getColumnCount(); j++) {
			result += schemas.getColumnName(j)+"\t";
		}
		result += "\n";
		for (int i = 0; i < schemas.getRowCount(); i++) {
			for (int j = 0; j < schemas.getColumnCount(); j++) {
				result += schemas.getValueAt(i, j)+"\t";
			}
			result += "\n";
		}
		return result;
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			Object value = e.getValue();
			if (value instanceof SchemaInstance) {
				fireGo(new GoEvent(thisis, value, "SchemaInstance"));
			} else if (value instanceof SdaiModel) {
				fireGo(new GoEvent(thisis, value, "Model"));
			}
		}
	};
}
