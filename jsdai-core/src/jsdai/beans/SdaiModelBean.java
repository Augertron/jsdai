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

import jsdai.mapping.*;
import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class SdaiModelBean extends SdaiPanel {
	SdaiModel model;
	ESchema_mapping mapping_schema;
	ASdaiModel xmodels = new ASdaiModel();
	int mode = EEntity.NO_RESTRICTIONS;

	Hashtable mappingSchemas = new Hashtable();

//	JRadioButton off 		= new JRadioButton("Off");
	JRadioButton readOnly	= new JRadioButton("R/O");
	JRadioButton readWrite	= new JRadioButton("R/W");

	JRadioButton rbPopulated = new JRadioButton("Populated folders");
	JRadioButton rbAll 		= new JRadioButton("All folders");
	JRadioButton rbExact 	= new JRadioButton("Exact folders");

	JRadioButton rbmPopulated = new JRadioButton("Populated folders");
	JRadioButton rbmAll = new JRadioButton("All folders");
	JRadioButton rbmValidate = new JRadioButton("Debug");

	JCheckBox checkModeified = new JCheckBox("Modified");

	JTextField name 		= new JTextField();
	GoTextField repository 	= new GoTextField();
	JTextField date			= new JTextField();
	JTextField schema	 	= new JTextField();

	JTextField edtCount		= new JTextField();
	JTextField cedtCount	= new JTextField();
	JTextField folderCount	= new JTextField();

	GoTable extents;
	EntityTableModel extentsModel = new EntityTableModel();

	GoList schemas = new GoList();
	AggregateListModel schemasModel = new AggregateListModel();

/*senas mappingas
	AggregateBean mappings = new AggregateBean();
*/
	GoTable mappings;
	JScrollPane mappingScroll;
	JPanel foldm;
	JPanel panelMapping;
	JLabel labelNoMapping = new JLabel("No mapping data");
	SchemaMappingTableModel extentTableModel = new SchemaMappingTableModel();


	JTabbedPane tp = new JTabbedPane();

	public SdaiModelBean() {
		setLayout(new BorderLayout());
//top
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel north = new JPanel(bl);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JLabel label;
		label = new JLabel("Name");
		label.setHorizontalAlignment(JLabel.RIGHT);
		labels.add(label);
		label = new JLabel("Repository");
		label.setHorizontalAlignment(JLabel.RIGHT);
		labels.add(label);
		label = new JLabel("Underlying schema");
		label.setHorizontalAlignment(JLabel.RIGHT);
		labels.add(label);
		label = new JLabel("Change date");
		label.setHorizontalAlignment(JLabel.RIGHT);
		labels.add(label);
		north.add(labels, BorderLayout.WEST);
		JPanel texts = new JPanel(gl);
		name.setEditable(false);
		texts.add(name);
		repository.setEditable(false);
		repository.addGoListener(goListener);
		repository.setUnderlying(true);
//		repository.addKeyListener(keyListener);
//		repository.addMouseListener(mouseListener);
//		repository.addFocusListener(focusListener);
		texts.add(repository);
		schema.setEditable(false);
		texts.add(schema);
//		JPanel panelAccess = new JPanel();
		Box panelAccess = Box.createHorizontalBox();
		date.setEditable(false);
		panelAccess.add(date);

//access
		checkModeified.setHorizontalAlignment(JLabel.RIGHT);
		checkModeified.setEnabled(false);
		checkModeified.setPreferredSize(new Dimension((int)checkModeified.getPreferredSize().getWidth(), 13));
		checkModeified.setMinimumSize(new Dimension((int)checkModeified.getMinimumSize().getWidth(), 13));
		panelAccess.add(checkModeified);

		JPanel access = new JPanel();
//		access.setLayout(new GridLayout(1, 0));
//		access.add(new JLabel("Access"));
		ButtonGroup bgroup = new ButtonGroup();
		readOnly.setPreferredSize(new Dimension((int)readOnly.getPreferredSize().getWidth(), 13));
		readOnly.setMinimumSize(new Dimension((int)readOnly.getMinimumSize().getWidth(), 13));
		readOnly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					switch (model.getMode()) {
						case SdaiModel.NO_ACCESS :
							model.startReadOnlyAccess();
							break;
						case SdaiModel.READ_ONLY :
							break;
						case SdaiModel.READ_WRITE :
							model.reduceSdaiModelToRO();
							break;
					}
				} catch (SdaiException h) {
//					h.printStackTrace();
				}
			}
		});
		bgroup.add(readOnly);
		access.add(readOnly);
		readWrite.setPreferredSize(new Dimension((int)readWrite.getPreferredSize().getWidth(), 13));
		readWrite.setMinimumSize(new Dimension((int)readWrite.getMinimumSize().getWidth(), 13));
		readWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					switch (model.getMode()) {
						case SdaiModel.NO_ACCESS :
							model.startReadWriteAccess();
							break;
						case SdaiModel.READ_ONLY :
							model.promoteSdaiModelToRW();
							break;
						case SdaiModel.READ_WRITE :
							break;
					}
				} catch (SdaiException h) {
//					h.printStackTrace();
				}
			}
		});
		bgroup.add(readWrite);
		access.add(readWrite);
/*		off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					switch (model.getMode()) {
						case SdaiModel.NO_ACCESS :
							break;
						case SdaiModel.READ_ONLY :
							model.endReadOnlyAccess();
							break;
						case SdaiModel.READ_WRITE :
							model.endReadWriteAccess();
							break;
					}
				} catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		bgroup.add(off);
		access.add(off);*/
		panelAccess.add(access);
		texts.add(panelAccess);

		north.add(texts, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);
//extents
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
		extents.setUnderlying(true);
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
//schemas
		JPanel pSchemas = new JPanel(new BorderLayout());
		schemas.setModel(schemasModel);
		schemas.setBackground(getBackground());
		schemas.setCellRenderer(new SdaiCellRenderer());
//		schemas.addKeyListener(findKeyListener);
		schemas.addGoListener(goListener);
//		schemas.getList().addKeyListener(keyListener);
//		schemas.addSdaiSelectableListener(selectableListener);
		schemas.setUnderlying(true);
		pSchemas.add(new JScrollPane(schemas), BorderLayout.CENTER);
		tp.add(pSchemas, "SchemaInstances");
//mapping

/*senas mappingas
		JPanel pmapping = new JPanel(new BorderLayout());
		mappings.getList().setBackground(getBackground());
		mappings.getList().setCellRenderer(new SdaiCellRenderer());
		mappings.getList().addKeyListener(keyListener);

		mappings.addSdaiSelectableListener(selectableListener);
		pmapping.add(mappings, BorderLayout.CENTER);
		tp.add(pmapping, "Mapping");
*/
		panelMapping = new JPanel(new BorderLayout());

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
					extentTableModel.setFilter(SchemaMappingTableModel.FILTER_NONE);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		groupm.add(rbmAll);
		foldm.add(rbmAll);
		rbmValidate.setVisible(false);
		rbmValidate.setPreferredSize(new Dimension((int)rbmValidate.getPreferredSize().getWidth(), 13));
		rbmValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					extentTableModel.setFilter(SchemaMappingTableModel.FILTER_VALIDATE);
				}
				catch (SdaiException h) {
					h.printStackTrace();
				}
			}
		});
		groupm.add(rbmValidate);
		foldm.add(rbmValidate);
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
		sorter.setModel(extentTableModel);
		sorter.fireTableStructureChanged();
		sorter.sortByColumn(0);
		mappings.getColumnModel().getColumn(0).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(1).setPreferredWidth(400);
//		mappings.getColumnModel().getColumn(2).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(2).setPreferredWidth(50);
		mappings.sizeColumnsToFit(-1);
		mappings.setUnderlying(true);
		mappingScroll = new JScrollPane(mappings);
		panelMapping.add(mappingScroll, BorderLayout.CENTER);
		panelMapping.add(labelNoMapping, BorderLayout.SOUTH);

//		labelNoMapping.setAlignmentX(JLabel.CENTER);
		labelNoMapping.setHorizontalAlignment(JLabel.CENTER);
		labelNoMapping.setFont(new Font(labelNoMapping.getFont().getName(), Font.BOLD, 22));
		tp.add(panelMapping, "Mapping");

		tp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					if (tp.getTitleAt(tp.getSelectedIndex()).equals("Mapping") && !extentTableModel.testSchema_mappingAndAModels(mapping_schema, xmodels)) {
						refreshData();
					}
				} catch (SdaiException ex) { processMessage(ex); }
			}
		});

		add(tp, BorderLayout.CENTER);
	}

	public SdaiModelBean(SdaiModel model) throws SdaiException {
		this();
		setModel(model);
	}

	public void setModel(SdaiModel model) {
		try {
	//		if (this.model == model) return;
			this.model = model;
			SimpleOperations.enshureReadOnlyTransaction();
			switch (model.getMode()) {
				case SdaiModel.NO_ACCESS :
					model.startReadOnlyAccess();
					readOnly.setSelected(true);
					break;
				case SdaiModel.READ_ONLY :
					readOnly.setSelected(true);
					break;
				case SdaiModel.READ_WRITE :
					readWrite.setSelected(true);
					break;
			}
			xmodels.clear();
			xmodels.addByIndex(1, model, null);
			SdaiSession.getSession().setSdaiContext(new SdaiContext(model.getUnderlyingSchema(), 
																	xmodels, model));
			ASchema_mapping mschemas = SdaiSession.getSession().getMappings(model.getUnderlyingSchema());
			if (mschemas.getMemberCount() > 0) {
				mapping_schema = (ESchema_mapping)mschemas.getByIndexEntity(1);
			} else {
				mapping_schema = null;
			}
			refreshData();
		} catch (SdaiException ex) { processMessage(ex); }
	}

	public void refreshData() {
		try {
			setHourGlass(true);
			name.setText(model.getName());
			repository.setLink(model.getRepository());
			repository.setText(model.getRepository().getName());
			checkModeified.setSelected(model.isModified());
			date.setText(model.getChangeDate());
			schema.setText(model.getUnderlyingSchema().getName(null));

/*senas mappingas
			ASchemaInstance domain = new ASchemaInstance();
			domain.addByIndex(1, SdaiSession.getSession().getDataMapping());
			domain.addByIndex(2, SdaiSession.getSession().getDataDictionary());

			SchemaInstance si = SdaiSession.getSession().getDataMapping();
			ASdaiModel models = si.getAssociatedModels();
			ASdaiModel mapping = new ASdaiModel();
			SdaiIterator models_it = models.createIterator();
			SdaiIterator mapping_it = mapping.createIterator();
			while (models_it.next()) {
				SdaiModel mod = models.getCurrentMember(models_it);
				if (mod.getName().endsWith("MAPPING_DATA")) {
					mapping.addAfter(mapping_it, mod, null);
				}
			}
			mappings.setAggregate(mapping);
*/
/*			String schema_name = model.getUnderlyingSchema().getName(null);
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
			if (tp.getTitleAt(tp.getSelectedIndex()).equals("Mapping")) {
				if (mapping_schema != null) {
					panelMapping.removeAll();
					panelMapping.add(foldm, BorderLayout.NORTH);
					panelMapping.add(mappingScroll, BorderLayout.CENTER);
					extentTableModel.setSchema_mappingAndAModels(mapping_schema, xmodels, mode);
				} else {
					panelMapping.removeAll();
					panelMapping.add(labelNoMapping, BorderLayout.CENTER);
				}
			}

			//extents.setAggregate(model.getPopulatedFolders());

			extentsModel.setASdaiModel(xmodels);
			extentsModel.fireTableDataUpdated();

			edtCount.setText(String.valueOf(extentsModel.getEdtCount()));
			cedtCount.setText(String.valueOf(extentsModel.getCedtCount()));
			folderCount.setText(String.valueOf(extentsModel.getFolderCount()));

			schemasModel.setAggregate(model.getAssociatedWith());
	//		count.setText(String.valueOf(model.getInstances().getMemberCount()));
		    refreshAccessButtons();
		} catch (SdaiException ex) {
			setHourGlass(false);
			processMessage(ex);
		} finally {
			setHourGlass(false);
		}
	}

	private void refreshAccessButtons() {
		try {
			switch (model.getMode()) {
				case SdaiModel.NO_ACCESS :
					model.startReadOnlyAccess();
					readOnly.setSelected(true);
					break;
				case SdaiModel.READ_ONLY :
					readOnly.setSelected(true);
					break;
				case SdaiModel.READ_WRITE :
					readWrite.setSelected(true);
					break;
			}
			readOnly.setEnabled(!model.isModified());
			readWrite.setEnabled(!model.isModified());
		} catch (SdaiException ex) {
			processMessage(ex);
		}
	}

	public SdaiModel getModel() throws SdaiException {
		return model;
	}

	public ASdaiModel getModels() throws SdaiException {
		return xmodels;
	}

	public String getTreeLeave() throws SdaiException {
		if (model == null) {
			return "";
		} else {
			return "/"+model.getRepository().getName()+"/"+model.getName();
		}
	}

	public int getMode() throws SdaiException {
		return mNOT_USE;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == schemas) {
			result.add(schemas.getSelectedValue());
		} else if (lastSelection == extents) {
//			extents.getSelected(result);
			result.add((EEntity_definition)extents.getModel().getValueAt(extents.getSelectedRow(), -1));
		} else if (lastSelection == repository) {
			result.add(model.getRepository());
/*senas mappingas
		} else if (lastSelection == mappings) {
			if (mappings.getList().getSelectedIndex() != -1) {
				SdaiModel mod = (SdaiModel)mappings.getList().getSelectedValue();
				SimpleOperations.startReadOnlyAccess(mod);
				AEntity schemas = mod.getInstances(ESchema_mapping.class);
				result.add((ESchema_mapping)schemas.getByIndexEntity(1));
			}
*/
/*			SchemaInstance si = SdaiSession.getSession().getDataMapping();
			ASdaiModel models = si.getAssociatedModels();
			SdaiIterator models_it = models.createIterator();
			while (models_it.next()) {
				SdaiModel mod = models.getCurrentMember(models_it);
				if (mod.getName().equals(c_mapping.getSelectedItem().toString())) {
					if (mod.getMode() == SdaiModel.NO_ACCESS) {
						mod.startReadOnlyAccess();
					}
					AEntity schemas = mod.getInstances(ESchema_mapping.class);
					result.add((ESchema_mapping)schemas.getByIndexEntity(1));
					return;
				}
			}*/
		} else if (lastSelection == mappings) {
			result.add((EEntity_mapping)mappings.getModel().getValueAt(mappings.getSelectedRow(), -1));
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (schemas.hasFocus()) {
			result = true;
		} else if (extents.hasFocus()) {
			result = (extents.getSelectedRowCount() != 0);
		} else if (repository.hasFocus()) {
			result = true;
/*senas mappingas
		} else if (mappings.getList().hasFocus()) {
			result = mappings.isSelected();
*/
		} else if (mappings.hasFocus()) {
			result = (mappings.getSelectedRowCount() != 0);
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
//		model.renameSdaiModel(name.getText());
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
		name.setText(model.getName());
	};

	public void sdaiNew() throws SdaiException {
	};

	public void sdaiDestroy() throws SdaiException {
	};

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - Model - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Name:\t"+name.getText()+"\n";
		result += "Underlying schema:\t"+schema.getText()+"\n";
		result += "Changed date:\t"+date.getText()+"\n";
		result += "-Extents-"+"\n";
		for (int j = 0; j < extents.getColumnCount(); j++) {
			result += extents.getColumnName(j)+"\t";
		}
		result += "\n";
		for (int i = 0; i < extents.getRowCount(); i++) {
			for (int j = 0; j < extents.getColumnCount(); j++) {
				result += extents.getValueAt(i, j)+"\t";
			}
			result += "\n";
		}
		result += "# of EDT's:\t"+edtCount.getText()+"\n";
		result += "# of CEDT's:\t"+cedtCount.getText()+"\n";
		result += "# of instaces:\t"+folderCount.getText()+"\n";
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
//Debug.println("SC "+schema_name);
//Debug.println("MA "+mapping_name);
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
			Object value = e.getValue();
			if (value instanceof SchemaInstance) {
				fireGo(new GoEvent(thisis, value, "SchemaInstance"));
			} else if (value instanceof SdaiRepository) {
				fireGo(new GoEvent(thisis, value, "Repository"));
			} else if (value instanceof EEntity_definition) {
				Object s[] = new Object[2];
				s[0] = value;
				s[1] = model;
				fireGo(new GoEvent(thisis, s, "EntityExtent"));
			} else if (value instanceof EEntity_mapping) {
				Object s[] = new Object[2];
				s[0] = value;
				s[1] = xmodels;
				fireGo(new GoEvent(thisis, s, "ExtentMapping"));
			}
		}
	};
}
