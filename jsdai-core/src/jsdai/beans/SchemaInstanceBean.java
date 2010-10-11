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
import java.text.*;
import java.beans.*;

import jsdai.lang.*;
import jsdai.util.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;

public class SchemaInstanceBean extends SdaiPanel {
	SchemaInstance schema;
	int mode = EEntity.NO_RESTRICTIONS;

	ESchema_mapping mapping_schema;
	Hashtable mappingSchemas = new Hashtable();

	JTextField name = new JTextField();
	GoTextField repository 	= new GoTextField();
	JTextField schemaDef 	= new JTextField();
	JTextField v_date		= new JTextField();
	JTextField v_level		= new JTextField();
	JTextField v_result		= new JTextField();

	JRadioButton 	rbPopulated = new JRadioButton("Populated folders");
	JRadioButton 	rbAll 		= new JRadioButton("All folders");
	JRadioButton 	rbExact 	= new JRadioButton("Exact folders");

	JRadioButton 	rbmPopulated 	= new JRadioButton("Populated folders");
	JRadioButton 	rbmAll 			= new JRadioButton("All folders");

	JTextField edtCount		= new JTextField();
	JTextField cedtCount	= new JTextField();
	JTextField folderCount	= new JTextField();

	GoTable models;
	SchemaInstanceTableModel modelsModel = new SchemaInstanceTableModel();

	JButton badd = new JButton("Add model");
	JButton bremove = new JButton("Remove model");

	GoTable extents;
	EntityTableModel extentsModel = new EntityTableModel();

	JScrollPane mappingScroll;
	JPanel foldm;
	JPanel panelMapping;
	JLabel labelNoMapping = new JLabel("No mapping data");
	GoTable mappings;
	SchemaMappingTableModel extentTableModel = new SchemaMappingTableModel();

	JPanel pedit = new JPanel();

	SchemaModelRenderer siRenderer;
	JComboBox c_models = new JComboBox();
	AggregateListModel modelModels = new AggregateListModel();
	AggregateListModel modelRepos = new AggregateListModel();

	JComboBox cRepos = new JComboBox();

	JPanel garbage = new JPanel();

	public SchemaInstanceBean() {
		setLayout(new BorderLayout());
//info
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel north = new JPanel(bl);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Name"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Repository"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Native schema"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Validation date"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Validation level"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Validation result"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels, BorderLayout.WEST);
		name.setEditable(false);
		JPanel texts = new JPanel(gl);
		texts.add(name);
		repository.setEditable(false);
		repository.setUnderlying(true);
		repository.addGoListener(goListener);
//		repository.addKeyListener(keyListener);
//		repository.addMouseListener(mouseListener);
//		repository.addFocusListener(focusListener);
		texts.add(repository);
		schemaDef.setEditable(false);
		texts.add(schemaDef);
		v_date.setEditable(false);
		texts.add(v_date);
		v_level.setEditable(false);
		texts.add(v_level);
		v_result.setEditable(false);
		texts.add(v_result);
		north.add(texts, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);

//extents
		JTabbedPane tp = new JTabbedPane();
		JPanel panel = new JPanel(new BorderLayout());

		JPanel fold = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rbPopulated.setPreferredSize(new Dimension((int)rbPopulated.getPreferredSize().getWidth(), 13));
		ButtonGroup group = new ButtonGroup();
		rbPopulated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentsModel.setFilter(EntityTableModel.FILTER_ZERO);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		group.add(rbPopulated);
		fold.add(rbPopulated);
		rbPopulated.setSelected(true);
		rbAll.setPreferredSize(new Dimension((int)rbAll.getPreferredSize().getWidth(), 13));
		rbAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentsModel.setFilter(EntityTableModel.FILTER_NONE);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		group.add(rbAll);
		fold.add(rbAll);
		rbExact.setPreferredSize(new Dimension((int)rbExact.getPreferredSize().getWidth(), 13));
		rbExact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentsModel.setFilter(EntityTableModel.FILTER_EXACT);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		group.add(rbExact);
		fold.add(rbExact);
		panel.add(fold, BorderLayout.NORTH);

		TableSorter sorter = new TableSorter();
		extents = new GoTable(sorter);
		extents.addGoListener(goListener);
		sorter.setModel(extentsModel);
		sorter.fireTableStructureChanged();
		sorter.sortByColumn(0);
		sorter.addMouseListenerToHeaderInTable(extents);
		extents.setBackground(getBackground());
//		extents.addMouseListener(mouseListener);
//		extents.getSelectionModel().addListSelectionListener(listListener);
//		extents.addFocusListener(focusListener);
//		extents.addKeyListener(findKeyListener);
//		extents.addKeyListener(keyListener);
		extents.getColumnModel().getColumn(0).setPreferredWidth(400);
		extents.getColumnModel().getColumn(1).setPreferredWidth(400);
		extents.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableCellRenderer headerRenderer = extents.getColumnModel().getColumn(2).getHeaderRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer)headerRenderer).setToolTipText("# of Entity Data Type(EDT) values");
		}
		extents.getColumnModel().getColumn(3).setPreferredWidth(100);
		headerRenderer = extents.getColumnModel().getColumn(3).getHeaderRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer)headerRenderer).setToolTipText("# of Complex Entity Data Type(CEDT) instances");
		}
		extents.sizeColumnsToFit(-1);
		extents.setUnderlying(true);

		panel.add(new JScrollPane(extents), BorderLayout.CENTER);

		JPanel counts = new JPanel(new GridLayout(1, 3));
		((GridLayout)counts.getLayout()).setHgap(5);
		JPanel edt = new JPanel(new BorderLayout());
		((BorderLayout)edt.getLayout()).setHgap(5);
		edt.add(new JLabel("# of EDT's"), BorderLayout.WEST);
		edtCount.setEditable(false);
		edt.add(edtCount);
		counts.add(edt);
		JPanel cedt = new JPanel(new BorderLayout());
		((BorderLayout)cedt.getLayout()).setHgap(5);
		cedt.add(new JLabel("# of CEDT's"), BorderLayout.WEST);
		cedtCount.setEditable(false);
		cedt.add(cedtCount);
		counts.add(cedt);
		JPanel folder = new JPanel(new BorderLayout());
		((BorderLayout)folder.getLayout()).setHgap(5);
		folder.add(new JLabel("# of instaces"), BorderLayout.WEST);
		folderCount.setEditable(false);
		folder.add(folderCount);
		counts.add(folder);
		panel.add(counts, BorderLayout.SOUTH);
		tp.add(panel, "Extents");
//models
		JPanel panelModels = new JPanel(new BorderLayout());
		TableSorter msorter = new TableSorter();
		models = new GoTable(msorter);
		models.addGoListener(goListener);
		msorter.addMouseListenerToHeaderInTable(models);
		msorter.setModel(modelsModel);
		msorter.fireTableStructureChanged();
		msorter.sortByColumn(1);
		models.setBackground(getBackground());
//		models.addMouseListener(mouseListener);
//		models.getSelectionModel().addListSelectionListener(listListener);
//		models.addFocusListener(focusListener);
//		models.addKeyListener(findKeyListener);
//		models.addKeyListener(keyListener);
		models.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshModelButtons();
			}
		});
		models.getColumnModel().getColumn(0).setPreferredWidth(400);
		models.getColumnModel().getColumn(1).setPreferredWidth(400);
		models.getColumnModel().getColumn(2).setPreferredWidth(400);
		models.sizeColumnsToFit(-1);
		PropertyChangeListener spc = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				cRepos.setPreferredSize(new Dimension(models.getColumnModel().getColumn(0).getWidth(), (int)cRepos.getPreferredSize().getHeight()));
				c_models.setPreferredSize(new Dimension(models.getColumnModel().getColumn(1).getWidth(), (int)c_models.getPreferredSize().getHeight()));
				garbage.setPreferredSize(new Dimension(models.getColumnModel().getColumn(2).getWidth(), (int)garbage.getPreferredSize().getHeight()));
				pedit.revalidate();
			}
		};
		models.getColumnModel().getColumn(0).addPropertyChangeListener(spc);
		models.getColumnModel().getColumn(1).addPropertyChangeListener(spc);
		models.setUnderlying(true);
		panelModels.add(new JScrollPane(models), BorderLayout.CENTER);
//pedit
		JPanel panelEdit = new JPanel(new BorderLayout());
		pedit.setLayout(new BoxLayout(pedit, BoxLayout.X_AXIS));
		cRepos.setRenderer(new SdaiCellRenderer());
		cRepos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshModelButtons();
			}
		});
//		cRepos.addKeyListener(keyListener);
//		cRepos.addItemListener(itemListener);
//		cRepos.addFocusListener(focusListener);
		cRepos.setPreferredSize(new Dimension((int)cRepos.getPreferredSize().getWidth(), models.getRowHeight()+2));
		ListSorter reposSorter = new ListSorter(modelRepos);
		reposSorter.sort(true);
		cRepos.setModel(reposSorter);
		cRepos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiRepository repo = (SdaiRepository)cRepos.getSelectedItem();
					if (!repo.isActive()) repo.openRepository();
					c_models.setModel(new AggregateListModel(repo.getModels()));
    			} catch (SdaiException ex) { processMessage(ex); }
			}
		});
		pedit.add(cRepos);
		siRenderer = new SchemaModelRenderer(new SdaiCellRenderer());
		c_models.setRenderer(siRenderer);
		c_models.addKeyListener(keyListener);
		c_models.addItemListener(itemListener);
		c_models.addFocusListener(focusListener);
		c_models.setPreferredSize(new Dimension((int)c_models.getPreferredSize().getWidth(), models.getRowHeight()+2));
		c_models.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshModelButtons();
			}
		});
		ListSorter modelSorter = new ListSorter(modelModels);
		modelSorter.sort(true);
		c_models.setModel(modelSorter);
		pedit.add(c_models);
		garbage.setPreferredSize(new Dimension((int)c_models.getPreferredSize().getWidth(), models.getRowHeight()+2));
		pedit.add(garbage);
		panelEdit.add(pedit, BorderLayout.NORTH);

		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		badd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					schema.addSdaiModel((SdaiModel)c_models.getSelectedItem());
//					((TableModelListener)models.getModel()).tableChanged(new TableModelEvent(((TableSorter)models.getModel()).getModel()));
					modelsModel.fireTableDataChanged();
				    refreshModelButtons();
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(badd);
		bremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (models.getSelectedRowCount() != 0) {
						schema.removeSdaiModel((SdaiModel)((TableModel)models.getModel()).getValueAt(models.getSelectedRow(), -1));
						modelsModel.fireTableDataChanged();
//						((TableModelListener)models.getModel()).tableChanged(new TableModelEvent(((TableSorter)models.getModel()).getModel()));
					}
				    refreshModelButtons();
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(bremove);
		panelEdit.add(bar, BorderLayout.SOUTH);
		panelModels.add(panelEdit, BorderLayout.SOUTH);
		tp.add(panelModels, "Associated models");
//mapping
		panelMapping = new JPanel(new BorderLayout());
//		JPanel pa = new JPanel(new BorderLayout());

		foldm = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup groupm = new ButtonGroup();
		rbmPopulated.setPreferredSize(new Dimension((int)rbmPopulated.getPreferredSize().getWidth(), 13));
		rbmPopulated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentTableModel.setFilter(EntityTableModel.FILTER_ZERO);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		groupm.add(rbmPopulated);
		foldm.add(rbmPopulated);
		rbmPopulated.setSelected(true);
		rbmAll.setPreferredSize(new Dimension((int)rbmAll.getPreferredSize().getWidth(), 13));
		rbmAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentTableModel.setFilter(EntityTableModel.FILTER_NONE);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		groupm.add(rbmAll);
		foldm.add(rbmAll);
//		pa.add(foldm, BorderLayout.NORTH);
		panelMapping.add(foldm, BorderLayout.NORTH);

		sorter = new TableSorter();
		mappings = new GoTable(sorter);
		mappings.addGoListener(goListener);
		sorter.addMouseListenerToHeaderInTable(mappings);
//		mappings.addMouseListener(mouseListener);
		mappings.setBackground(getBackground());
//		mappings.getSelectionModel().addListSelectionListener(listListener);
//		mappings.addFocusListener(focusListener);
//		mappings.addKeyListener(findKeyListener);
//		mappings.addKeyListener(keyListener);
		mappings.getTableHeader().setReorderingAllowed(false);
		mappings.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sorter.setModel(extentTableModel);
		sorter.fireTableStructureChanged();
		sorter.sortByColumn(0);
		mappings.getColumnModel().getColumn(0).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(1).setPreferredWidth(400);
//		mappings.getColumnModel().getColumn(2).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(2).setPreferredWidth(50);
		mappings.sizeColumnsToFit(-1);
		mappings.setUnderlying(true);
//		pa.add(new JScrollPane(mappings), BorderLayout.CENTER);

		mappingScroll = new JScrollPane(mappings);
		panelMapping.add(mappingScroll, BorderLayout.CENTER);
		panelMapping.add(labelNoMapping, BorderLayout.SOUTH);

		labelNoMapping.setHorizontalAlignment(JLabel.CENTER);
		labelNoMapping.setFont(new Font(labelNoMapping.getFont().getName(), Font.BOLD, 22));
		tp.add(panelMapping, "Mapping");
//		tp.add(pa, "Mapping");

		add(tp, BorderLayout.CENTER);
	}

	public SchemaInstanceBean(SchemaInstance schema) throws SdaiException {
		this();
		setSchema(schema);
	}

	public void setSchema(SchemaInstance schema) {
//		if (this.schema == schema) return;
		this.schema = schema;
		refreshData();
	}

	public void refreshData() {
		try {
			name.setText(schema.getName());
			repository.setLink(schema.getRepository());
			repository.setText(schema.getRepository().getName());
			schemaDef.setText(schema.getNativeSchemaString());
			v_date.setText(schema.getValidationDate());
			v_level.setText(String.valueOf(schema.getValidationLevel()));
			v_result.setText(String.valueOf(schema.getValidationResult()));
			modelsModel.setSchemaInstance(schema);
		    modelsModel.fireTableDataChanged();
//			cRepos.setModel(new AggregateListModel(SdaiSession.getSession().getKnownServers()));
		    modelRepos.setAggregate(SdaiSession.getSession().getKnownServers());
			siRenderer.setSchemaInstance(schema);
		    modelRepos.setSelectedItem(schema.getRepository());
			SdaiRepository repo = (SdaiRepository)cRepos.getSelectedItem();
			if (!repo.isActive()) repo.openRepository();
		    modelModels.setAggregate(repo.getModels());
//			c_models.setModel(new AggregateListModel(repo.getModels()));

			ASdaiModel xmodels = schema.getAssociatedModels();

			mapping_schema = null;

			ASchema_mapping mschemas = SdaiSession.getSession().getMappings(schema.getNativeSchema());
			if (mschemas.getMemberCount() > 0) {
				mapping_schema = (ESchema_mapping)mschemas.getByIndexEntity(1);
			} else {
				mapping_schema = null;
			}

/*			String schema_name = schema.getNativeSchema().getName(null);
			String mapping_name = (String)mappingSchemas.get(schema_name);
			if (mapping_name != null) {
				SchemaInstance si = SdaiSession.getSession().getDataMapping();
				ASdaiModel models = si.getAssociatedModels();
				SdaiIterator models_it = models.createIterator();
				while (models_it.next()) {
					SdaiModel mod = models.getCurrentMember(models_it);
					if (mod.getName().equals(mapping_name)) {
						if (mod.getMode() == SdaiModel.NO_ACCESS) {
							mod.startReadOnlyAccess();
						}
						AEntity schemas = mod.getInstances(ESchema_mapping.class);
						mapping_schema = (ESchema_mapping)schemas.getByIndexEntity(1);
					}
				}
			}*/
			if (mapping_schema != null) {
				panelMapping.removeAll();
				panelMapping.add(foldm, BorderLayout.NORTH);
				panelMapping.add(mappingScroll, BorderLayout.CENTER);
				extentTableModel.setSchema_mappingAndAModels(mapping_schema, xmodels, mode);
			} else {
				panelMapping.removeAll();
				panelMapping.add(labelNoMapping, BorderLayout.CENTER);
			}

/*			if (mapping_schema != null) {
				extentTableModel.setSchema_mappingAndAModels(mapping_schema, xmodels);
			}*/

			rbPopulated.setSelected(true);
			//extents.setAggregate(model.getPopulatedFolders());

			extentsModel.setASdaiModel(xmodels);
			extentsModel.fireTableDataChanged();

			edtCount.setText(String.valueOf(extentsModel.getEdtCount()));
			cedtCount.setText(String.valueOf(extentsModel.getCedtCount()));
			folderCount.setText(String.valueOf(extentsModel.getFolderCount()));
			refreshModelButtons();

			// is there 'first' model?
			if (xmodels.getMemberCount() > 0) {
				SdaiModel firstModel = xmodels.getByIndex(1);
				SdaiSession.getSession().setSdaiContext(
						new SdaiContext(firstModel.getUnderlyingSchema(), 
						xmodels, firstModel));
			}
			
		} catch (SdaiException ex) { processMessage(ex); }
	}

	private void refreshModelButtons() {
		badd.setEnabled((cRepos.getSelectedIndex() != -1) && (c_models.getSelectedIndex() != -1));
		bremove.setEnabled(models.getSelectedRow() != -1);
	}

	public SdaiModel getModel() throws SdaiException {
		if (models.getSelectedRowCount() != 0) {
			return (SdaiModel)models.getModel().getValueAt(models.getSelectedRow(), -1);
		} else {
			ASdaiModel xmodels = schema.getAssociatedModels();
			SdaiIterator it_xmodels = xmodels.createIterator();
			if (it_xmodels.next()) {
				return xmodels.getCurrentMember(it_xmodels);
			}
		}
		return null;
	}

	public ASdaiModel getModels() throws SdaiException {
		return schema.getAssociatedModels();
	}

	public SchemaInstance getSchema() throws SdaiException {
		return schema;
	}

	public String getTreeLeave() throws SdaiException {
		if (schema == null) {
			return "";
		} else {
			return schema.getRepository().getName()+"/"+schema.getName();
		}
	}

	public int getMode() throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection == models) {
			mode = mADD_REMOVE;
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == models) {
			result.add((SdaiModel)models.getModel().getValueAt(models.getSelectedRow(), -1));
		} else if (lastSelection == repository) {
			result.add(schema.getRepository());
		} else if (lastSelection == extents) {
			result.add((EEntity_definition)extents.getModel().getValueAt(extents.getSelectedRow(), -1));
		} else if (lastSelection == mappings) {
			result.add((EEntity_mapping)mappings.getModel().getValueAt(mappings.getSelectedRow(), -1));
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (models.hasFocus()) {
			result = (models.getSelectedRowCount() != 0);
		} else if (extents.hasFocus()) {
			result = (extents.getSelectedRowCount() != 0);
		} else if (mappings.hasFocus()) {
			result = (mappings.getSelectedRowCount() != 0);
		} else if (repository.hasFocus()) {
			result = true;
		}
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
		name.setEditable(true);
	};

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
		name.setEditable(false);
		if (!name.getText().equals(schema.getName())) {
			schema.rename(name.getText());
		}
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
		name.setEditable(false);
	};


	public void sdaiNew() throws SdaiException {
		if (lastSelection == models) {
			schema.addSdaiModel((SdaiModel)getClipboardElement());
		}
	};

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection == models) {
			if (models.getSelectedRowCount() != 0) {
				schema.removeSdaiModel((SdaiModel)models.getModel().getValueAt(extents.getSelectedRow(), -1));
			}
		}
	};

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - SchemaInstance - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Name:\t"+name.getText()+"\n";
		result += "Native schema:\t"+schemaDef.getText()+"\n";
		result += "Validation date:\t"+v_date.getText()+"\n";
		result += "Validation level:\t"+v_level.getText()+"\n";
		result += "Validation result:\t"+v_result.getText()+"\n";
		result += "-Associated models-"+"\n";
		return result;
	}

	public void setProperties(Properties props) {
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
			String name = (String)e.nextElement();
			if (name.length() > 13) {
				if (name.substring(0, 13).equalsIgnoreCase("model.shemas.")) {
					String schema_name = name.substring(13);
					String mapping_name = props.getProperty(name);
					mappingSchemas.put(schema_name, mapping_name);
				}
			}
		}
		mode = Integer.parseInt(props.getProperty("mapping.mode", String.valueOf(mode)));
	}

	public void getProperties(Properties props) {
		for (Enumeration e = mappingSchemas.keys(); e.hasMoreElements();) {
			String name = (String)e.nextElement();
			props.setProperty("model.shemas."+name, (String)mappingSchemas.get(name));
		}
		props.setProperty("mapping.mode", String.valueOf(mode));
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
		    try {
				Object value = e.getValue();
				if (value instanceof SdaiModel) {
					fireGo(new GoEvent(thisis, value, "Model"));
				} else if (value instanceof SdaiRepository) {
					fireGo(new GoEvent(thisis, value, "Repository"));
				} else if (value instanceof EEntity_definition) {
					Object s[] = new Object[2];
					s[0] = value;
					s[1] = schema;
					fireGo(new GoEvent(thisis, s, "EntityExtent"));
				} else if (value instanceof EEntity_mapping) {
					Object s[] = new Object[2];
					s[0] = value;
					s[1] = schema.getAssociatedModels();
					fireGo(new GoEvent(thisis, s, "ExtentMapping"));
				}
			} catch (SdaiException ex) { processMessage(ex); }
		}
	};

}