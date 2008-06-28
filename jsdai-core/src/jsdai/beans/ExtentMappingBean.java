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

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class ExtentMappingBean extends SdaiPanel {
	boolean validate = false;

	ASdaiModel mappingDomain;
	int mode = EEntity.NO_RESTRICTIONS;

	ASdaiModel models;
	EEntity_mapping entity;

	AggregateListModel comboModelsModel = new AggregateListModel();
	JComboBox comboModels = new JComboBox(comboModelsModel);

	GoTextField t_source = new GoTextField();
	GoTextField t_target = new GoTextField();

	//GoList instances = new GoList();
	GoTable instances;
	// AggregateListModel instancesModel = new AggregateListModel();
	MappingInversesTableModel instancesModel;

	JButton buttonGet = new JButton("Get");

	public ExtentMappingBean() {
		setLayout(new BorderLayout());

		JPanel north = new JPanel(new BorderLayout());
		((BorderLayout)north.getLayout()).setHgap(5);

		JPanel labels = new JPanel(new GridLayout(0, 1));
		((GridLayout)labels.getLayout()).setVgap(5);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Model"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Source (ARM)"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Target (AIM)"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels, BorderLayout.WEST);

		JPanel texts = new JPanel(new GridLayout(0, 1));
		((GridLayout)texts.getLayout()).setVgap(5);
		comboModels.setPreferredSize(t_source.getPreferredSize());
		comboModels.setRenderer(new SdaiCellRenderer());
//		comboModels.addMouseListener(mouseListener);
//		comboModels.addFocusListener(focusListener);
//		comboModels.addKeyListener(keyListener);
		comboModels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshButtons();
			}
		});
		comboModels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//instances.setSelectedIndex(-1);
				instances.clearSelection();
			}
		});
		texts.add(comboModels);
		t_source.setUnderlying(true);
		t_source.addGoListener(goListener);
//		t_source.addMouseListener(mouseListener);
//		t_source.addFocusListener(focusListener);
//		t_source.addKeyListener(keyListener);
		t_source.setEditable(false);
		texts.add(t_source);
		t_target.setUnderlying(true);
		t_target.addGoListener(goListener);
//		t_target.addMouseListener(mouseListener);
//		t_target.addFocusListener(focusListener);
//		t_target.addKeyListener(keyListener);
		t_target.setEditable(false);
		texts.add(t_target);
		north.add(texts, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);

		JPanel pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Instances"));
		instancesModel = new MappingInversesTableModel();
        TableSorter sorter = new TableSorter(instancesModel);
		instances = new GoTable(sorter);
		sorter.addMouseListenerToHeaderInTable(instances);
		//instances.setModel(instancesModel);
        instances.setUnderlying(true);
        instances.setReturnCount(2);
        instances.setBackground(getBackground());
		instances.addGoListener(goListener);
		//instances.setCellRenderer(new SdaiCellRenderer());
//		instances.addSdaiSelectableListener(selectableListener);
//		instances.addKeyListener(findKeyListener);
		instances.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshButtons();
				try {
					int selectedRow = instances.getSelectedRow();
					if(selectedRow >= 0) {
						EEntity entity = (EEntity)instancesModel.getValueAt(selectedRow, -1);
						SdaiModel selectedModel = (entity != null)?entity.findEntityInstanceSdaiModel():null;
						if(selectedModel != comboModels.getSelectedItem()) {
							comboModels.setSelectedItem(selectedModel);
						}
					}
				} catch (SdaiException ex) { processMessage(ex); }
			}
		});
//		instances.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				try {
//					EEntity entity = (EEntity)instances.getSelectedValue();
//	    			comboModels.setSelectedItem((entity != null)?entity.findEntityInstanceSdaiModel():null);
//				} catch (SdaiException ex) { processMessage(ex); }
//			}
//		});
		instancesModel.fireTableStructureChanged();
		pa.add(new JScrollPane(instances), BorderLayout.CENTER);
		add(pa, BorderLayout.CENTER);

		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    buttonGet.setEnabled(false);
		buttonGet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Vector selection = new Vector();
//				    if (!instances.isSelectionEmpty()) {
//						AEntity clipboard = SdaiSession.getSession().getClipboard();
//						clipboard.addByIndex(clipboard.getMemberCount()+1, instances.getSelectedValue(), null);
//					}
					int instanceSelection = instances.getSelectedRow();
				    if (instanceSelection >= 0) {
						AEntity clipboard = SdaiSession.getSession().getClipboard();
						clipboard.addByIndex(clipboard.getMemberCount()+1, instancesModel.getValueAt(instanceSelection, -1), null);
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(buttonGet);
		add(bar, BorderLayout.SOUTH);
	}

	public ExtentMappingBean(ASdaiModel models, EEntity_mapping entity) throws SdaiException {
		this();
		setModels_and_definition(models, entity);
	}

	public void setModels_and_definition(ASdaiModel models, EEntity_mapping entity) throws SdaiException {
		if ((this.models == models) && (this.entity == entity)) return;
		this.models = models;
		this.entity = entity;
		SdaiModel model = models.getByIndex(1);
		mappingDomain = MappingOperations.getMappingDomain(SdaiSession.getSession().getMappings(model.getUnderlyingSchema()));
		refreshData();
	}

	public void refreshData() {
		try {
			comboModelsModel.setAggregate(models);
			if (models.getMemberCount() == 1) {
				comboModels.setSelectedIndex(0);
				comboModels.setEnabled(false);
			} else {
				comboModels.setEnabled(true);
			}
			EEntity_definition definition = entity.getSource(null);
			t_source.setLink(definition);
			t_source.setText(definition.getName(null));
			EEntity_definition target = MappingOperations.getEntity_mappingTarget(entity);
			t_target.setLink(target);
			t_target.setText((target == null)?"":target.getName(null));

		    AEntity aggr = new AEntity();
			AEntity_mapping instanceMappings = null;
			SdaiIterator it_models = models.createIterator();
			while (it_models.next()) {
				SdaiModel model = models.getCurrentMember(it_models);
				AEntity_mapping modelInstanceMappings = new AEntity_mapping();
			    SimpleOperations.appendAggregateToAggregate(aggr, model.findMappingInstances(entity, models, mappingDomain, modelInstanceMappings, mode));
				if(instanceMappings == null) {
					instanceMappings = modelInstanceMappings;
				} else {
					SimpleOperations.appendAggregateToAggregate(instanceMappings, modelInstanceMappings);
				}
			}
			//instancesModel.setAggregate(aggr);
// Needed for both examples below			
//			AEntity_mapping baseMappings = new AEntity_mapping();
//			baseMappings.addByIndex(1, entity);
// Use of AEntity.findMostSpecificMappings			
//			instanceMappings = aggr.findMostSpecificMappings(models, mappingDomain, baseMappings, mode);
// Use of EEntity.findMostSpecificMappings			
//			SdaiIterator aggrIter = aggr.createIterator();
//			instanceMappings = new AEntity_mapping();
//			while(aggrIter.next()) {
//				EEntity instance = (EEntity)aggr.getCurrentMemberObject(aggrIter);
//				AEntity_mapping mappings = instance.findMostSpecificMappings(models, mappingDomain, baseMappings, mode);
//				instanceMappings.addByIndex(instanceMappings.getMemberCount() + 1, 
//					(EEntity)mappings.getByIndexObject(1));
//			}
			instancesModel.setInverses(aggr, instanceMappings);
			refreshButtons();

//			instances.setAggregate((validate)?MappingOperations.getValidatedMappingInstnces(entity, model, models, mappingDomain):model.findMappingInstances(entity, models, mappingDomain));
		} catch (SdaiException e) { processMessage(e); }
	}

	public void refreshButtons() {
	    buttonGet.setEnabled(instances.getSelectedRow() != -1);
	}

//	public SdaiModel getModel() throws SdaiException {
//		return model;
//	}

	public ASdaiModel getModels() throws SdaiException {
		return models;
	}

	public EEntity_mapping getEntityMapping() throws SdaiException {
		return entity;
	}

	public String getTreeLeave() throws SdaiException {
		if (entity == null) {
			return "";
		} else {
			return /*model.getRepository().getName()+"/"+model.getName()+"/"+entity.findEntityInstanceSdaiModel().getName()+*/"/"+MappingOperations.findSchema_mappingFor(entity).getId(null)+"/"+entity.getSource(null).getName(null);
		}
	}

	public int getMode()  throws SdaiException {
		if (lastSelection == instances) {
			return mCREATE_DELETE;
		}
		return mNOT_USE;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == instances) {
//			instances.getSelected(result);
		} else if (lastSelection == t_source) {
			result.add(entity.getSource(null));
		} else if (lastSelection == t_target) {
			result.add(MappingOperations.getEntity_mappingTarget(entity));
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (instances.hasFocus()) {
			result = true;
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
//		model.createTargetInstance(entity, models, mappingDomain);
//		instances.fireAggregateChanged();
	}

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection == instances) {
//			if (!instances.isSelectionEmpty()) {
//				((EEntity)instances.getSelectedValue()).deleteApplicationInstance();
////				instances.fireAggregateChanged();
//			}
			int instanceSelection = instances.getSelectedRow();
		    if (instanceSelection >= 0) {
				((EEntity)instancesModel.getValueAt(instanceSelection, -1)).deleteApplicationInstance();
			}
		}
	}

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - ExtentMapping - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
	    	SdaiModel model = (SdaiModel)comboModels.getSelectedItem();
			result += "Model:\t"+((model != null)?model.getName():"")+"\n";
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Source (ARM):\t"+t_source.getText()+"\n";
		result += "Target (AIM):\t"+t_target.getText()+"\n";
		result += "-Instances-"+"\n";
		for (int i = 0; i < instances.getModel().getRowCount(); i++) {
			result += instances.getModel().getValueAt(i, -1)+"\n";
		}
		return result;
	}

	public void setProperties(Properties props) {
		mode = Integer.parseInt(props.getProperty("mapping.mode", String.valueOf(mode)));
	}

	public void getProperties(Properties props) {
		props.setProperty("mapping.mode", String.valueOf(mode));
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			try {
				Object value = e.getValue();
				if (value instanceof EEntity_definition) {
					Object s[] = new Object[2];
					s[0] = value;
					s[1] = ((EEntity)value).findEntityInstanceSdaiModel();
					fireGo(new GoEvent(thisis, s, "EntityInstance"));
				} else if (value instanceof EEntity) {
					Object s[] = new Object[3];
					s[0] = value;
					s[1] = entity;
					s[2] = models;
					fireGo(new GoEvent(thisis, s, "InstanceMapping"));
				} else if (value instanceof Object[] && ((Object[])value)[0] instanceof EEntity) {
					Object values[] = (Object[])value;
					Object s[] = new Object[3];
					s[0] = values[0];
					s[1] = values[1];
					s[2] = models;
					fireGo(new GoEvent(thisis, s, "InstanceMapping"));
				}
			} catch (SdaiException ex) {
				processMessage(ex);
			}
		}
	};

}