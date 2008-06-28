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
import jsdai.lang.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import jsdai.mapping.*;

public class ModelMappingBean extends SdaiPanel {
	ESchema_mapping schema;
	SdaiModel model;

	JTextField t_source	= new JTextField();
	JTextField t_target	= new JTextField();
	JTextField t_id		= new JTextField();

	JTable mappings;
	SchemaMappingTableModel extentTableModel = new SchemaMappingTableModel();

	public ModelMappingBean() {
		setLayout(new BorderLayout());

		JPanel north = new JPanel(new BorderLayout());
		((BorderLayout)north.getLayout()).setHgap(5);

		JPanel labels = new JPanel(new GridLayout(0, 1));
		((GridLayout)labels.getLayout()).setVgap(5);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Source schema"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Target schema"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Schema mapping id"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels, BorderLayout.WEST);

		JPanel texts = new JPanel(new GridLayout(0, 1));
		((GridLayout)texts.getLayout()).setVgap(5);
		t_source.addMouseListener(mouseListener);
		t_source.addFocusListener(focusListener);
		t_source.addKeyListener(keyListener);
		t_source.setEditable(false);
		texts.add(t_source);
		t_target.addMouseListener(mouseListener);
		t_target.addFocusListener(focusListener);
		t_target.addKeyListener(keyListener);
		t_target.setEditable(false);
		texts.add(t_target);
		t_id.setEditable(false);
		texts.add(t_id);
		north.add(texts, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);

		JPanel pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Entity mappings"));
		TableSorter sorter = new TableSorter();
		mappings = new JTable(sorter);
		sorter.addMouseListenerToHeaderInTable(mappings);
		mappings.addMouseListener(mouseListener);
		mappings.setBackground(getBackground());
		mappings.getSelectionModel().addListSelectionListener(listListener);
		mappings.addFocusListener(focusListener);
		mappings.addKeyListener(keyListener);
		mappings.getTableHeader().setReorderingAllowed(false);
		mappings.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sorter.setModel(extentTableModel);
		sorter.fireTableStructureChanged();
		sorter.sortByColumn(0);
		mappings.getColumnModel().getColumn(0).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(1).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(2).setPreferredWidth(400);
		mappings.getColumnModel().getColumn(3).setPreferredWidth(50);
		mappings.sizeColumnsToFit(-1);
		pa.add(new JScrollPane(mappings), BorderLayout.CENTER);

		add(pa, BorderLayout.CENTER);
	}

	public ModelMappingBean(ESchema_mapping schema, SdaiModel model) throws SdaiException {
		this();
		setSchema_and_model(schema, model);
	}

	public void setSchema_and_model(ESchema_mapping schema, SdaiModel model) throws SdaiException {
		try {
			if ((this.schema == schema) && (this.model == model)) return;
			if (schema.getTarget(null) != model.getUnderlyingSchema()) {
				processMessage("Mapping schema \""+schema.getId(null)+"\" cann't be used with model \""+model.getName()+"\"!");
				return;
			}
			this.schema = schema;
			this.model = model;
			t_source.setText(schema.getSource(null).getName(null));
			t_target.setText(schema.getTarget(null).getName(null));
			t_id.setText(schema.getId(null));
//			extentTableModel.setSchema_mappingAndModel(schema, model);
			paintAll(getGraphics());
		} catch (SdaiException ex) { processMessage(ex); }
	}

	public ESchema_mapping getSchema() throws SdaiException {
		return schema;
	}

	public SdaiModel getModel() throws SdaiException {
		return model;
	}

	public String getTreeLeave() throws SdaiException {
		if ((model == null) || (schema == null)) {
			return "";
		} else {
			return model.getRepository().getName()+"/"+model.getName()+"/"+schema.getId(null);
		}
	}

	public int getMode()  throws SdaiException {
		return mNOT_USE;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == mappings) {
			result.add((EEntity_mapping)mappings.getModel().getValueAt(mappings.getSelectedRow(), -1));
		} else if (lastSelection == t_source) {
			result.add(schema.getSource(null));
		} else if (lastSelection == t_target) {
			result.add(schema.getTarget(null));
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (mappings.hasFocus()) {
			result = (mappings.getSelectedRowCount() != 0);
		}
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
	}

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
	}

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
	}

	public void sdaiNew() throws SdaiException {
	}

	public void sdaiDestroy() throws SdaiException {
	}

	public String copyContentsAsText() {
		String result = "";
		result += "Source schema:\t"+t_source.getText()+"\n";
		result += "Target schema:\t"+t_target.getText()+"\n";
		result += "Schema mapping id:\t"+t_id.getText()+"\n";
		result += "-Entity mappings-";
		for (int j = 0; j < mappings.getColumnCount(); j++) {
			result += mappings.getColumnName(j)+"\t";
		}
		result += "\n";
		for (int i = 0; i < mappings.getRowCount(); i++) {
			for (int j = 0; j < mappings.getColumnCount(); j++) {
				result += mappings.getValueAt(i, j)+"\t";
			}
			result += "\n";
		}
		return result;
	}

}