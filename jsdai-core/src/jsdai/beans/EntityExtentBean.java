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
import jsdai.dictionary.*;
import java.util.List;
import jsdai.util.*;

public class EntityExtentBean extends SdaiPanel {
	EEntity_definition extent;
	private SdaiModel currentModel;
	private SchemaInstance currentSchemaInstance;
    private ASdaiModel domain;

	AggregateListModel comboModelsModel = new AggregateListModel();
	JComboBox comboModels = new JComboBox(comboModelsModel);
	GoTextField definition 	= new GoTextField();

	JButton create = new JButton("Create");
	JButton delete = new JButton("Delete");
	JButton buttonGet = new JButton("Get");

	GoList instances = new GoList();
	AggregateListModel instancesModel = new AggregateListModel();

	public EntityExtentBean() {
		setLayout(new BorderLayout());
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel north = new JPanel(bl);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Model"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Definition"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels, BorderLayout.WEST);
		JPanel texts = new JPanel(gl);
		comboModels.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (comboModels.getSelectedItem() != null) {
					goListener.goPerformed(new GoEvent(this, comboModels.getSelectedItem(), ""));
				}
			}
		});
		comboModels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshButtons();
			}
		});
//		comboModels.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboModels.setPreferredSize(definition.getPreferredSize());
		comboModels.setRenderer(new SdaiCellRenderer());
		comboModels.addMouseListener(mouseListener);
		comboModels.addFocusListener(focusListener);
		comboModels.addKeyListener(keyListener);
		comboModels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instances.setSelectedIndex(-1);
			}
		});
		texts.add(comboModels);
		definition.setEditable(false);
		definition.addGoListener(goListener);
		definition.setUnderlying(true);
		texts.add(definition);
		north.add(texts, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);

		JPanel pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Instances"));
		instances.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				refreshButtons();
			}
		});
		instances.addGoListener(goListener);
		instances.setModel(instancesModel);
//		instances.getList().setCellRenderer(new SdaiCellRenderer());
//		instances.addSdaiSelectableListener(selectableListener);
//		instances.getList().addKeyListener(findKeyListener);
		instances.setBackground(getBackground());
		instances.setUnderlying(true);
		instances.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				try {
					EEntity entity = (EEntity)instances.getSelectedValue();
	    			comboModels.setSelectedItem((entity != null)?entity.findEntityInstanceSdaiModel():null);
				} catch (SdaiException ex) { processMessage(ex); }
			}
		});

		pa.add(new JScrollPane(instances), BorderLayout.CENTER);
		add(pa, BorderLayout.CENTER);

		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    create.setEnabled(false);
	    delete.setEnabled(false);
	    buttonGet.setEnabled(false);
		buttonGet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Vector selection = new Vector();
				    if (!instances.isSelectionEmpty()) {
						AEntity clipboard = SdaiSession.getSession().getClipboard();
						clipboard.addByIndex(clipboard.getMemberCount()+1, instances.getSelectedValue(), null);
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(buttonGet);
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SdaiModel model = (SdaiModel)comboModels.getSelectedItem();
				    SimpleOperations.enshureReadWriteModel(model);
					EEntity instance = model.createEntityInstance(extent);
					refreshData();
//					instances.setSelectedValue(instance, true);
//					lastSelection = instances;
//					fireSdaiSelectionProcessed();
				    Object s[] = new Object[2];
					s[0] = instance;
					if(currentSchemaInstance != null) {
						s[1] = currentSchemaInstance;
					} else if(currentModel != null) {
						s[1] = currentModel;
					} else {
						s[1] = domain;
					}
				    fireGo(new GoEvent(thisis, s, "EntityInstance"));
				} catch (SdaiException h) { processMessage(h); }
			}
		});
		bar.add(create);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!instances.isSelectionEmpty()) {
						EEntity instance = (EEntity)instances.getSelectedValue();
						SdaiModel model = instance.findEntityInstanceSdaiModel();
						SimpleOperations.enshureReadWriteModel(model);
						instance.deleteApplicationInstance();
						setInstancesAggregate();
						instancesModel.fireRemoved(instances.getSelectedIndex());
//						instancesModel.fireContentsChanged();
					}
				} catch (SdaiException h) { processMessage(h); }
			}
		});
		bar.add(delete);
		add(bar, BorderLayout.SOUTH);
	}

	public EntityExtentBean(EEntity_definition extent, Object modelOrInstance) throws SdaiException {
		this();
		setExtentModelInstance(extent, modelOrInstance);
	}

	public void setExtentModelInstance(EEntity_definition extent, Object modelOrInstance)
	throws SdaiException {
		ASdaiModel domain;
		SdaiModel model = null;
		SchemaInstance schemaInstance = null;
		if(modelOrInstance instanceof SchemaInstance) {
			schemaInstance = (SchemaInstance)modelOrInstance;
			domain = schemaInstance.getAssociatedModels();
		} else {
			model = (SdaiModel)modelOrInstance;
	        domain =  new ASdaiModel();
		    domain.addByIndex(1, model, null);
		}
		if (this.extent == extent && 
			this.currentModel == model &&
			this.currentSchemaInstance == schemaInstance) return;
		this.extent = extent;
		this.currentModel = model;
		this.currentSchemaInstance = schemaInstance;
		this.domain = domain;
		refreshData();
	}

    public void pushChainElementValues(List list) throws SdaiException {
        list.add(extent);
        list.add(currentSchemaInstance != null ? (Object)currentSchemaInstance : (Object)currentModel);
    }
    
    public void popChainElementValues(List list) throws SdaiException {
		setExtentModelInstance((EEntity_definition)list.get(0), list.get(1));
    }
    
	public void refreshData() {
		try {
			comboModelsModel.setAggregate(domain);
			if (domain.getMemberCount() == 1) {
				comboModels.setSelectedIndex(0);
				comboModels.setEnabled(false);
			} else {
				comboModels.setEnabled(true);
			}
			create.setEnabled(extent.getInstanceType().getInstantiable(null));
//			model.setText(extent.getOwned_by().getName());
//			definition.setText(extent.getStringDefinition());
		    definition.setLink(extent);
			definition.setText(extent.getName(null));
			setInstancesAggregate();
			refreshButtons();
		} catch (SdaiException ex) { processMessage(ex); }
	}
	
	private void setInstancesAggregate() {
		try {
		    AEntity aggr = new AEntity();
			SdaiIterator it_models = domain.createIterator();
			while (it_models.next()) {
				SdaiModel model = domain.getCurrentMember(it_models);
				try {
					AEntity insts = model.getInstances(extent);
					SimpleOperations.appendAggregateToAggregate(aggr, insts);
				} catch (SdaiException ex) {
					if (ex.getErrorId() == SdaiException.ED_NVLD) {
// do nothing
					} else {
						throw ex;
					}
				}
//			    SimpleOperations.appendAggregateToAggregate(aggr, model.getInstances(extent));
			}
		    instancesModel.setAggregate(aggr);
		} catch (SdaiException ex) {
			processMessage(ex);
		}
	}

	public void refreshButtons() {
	    create.setEnabled(comboModels.getSelectedIndex() != -1);
	    delete.setEnabled(instances.getSelectedIndex() != -1);
	    buttonGet.setEnabled(instances.getSelectedIndex() != -1);
	}

	public EEntity_definition getExtent() throws SdaiException {
		return extent;
	}

	public ASdaiModel getModels() throws SdaiException {
		return domain;
	}

	public String getTreeLeave() throws SdaiException {
		if (extent == null) {
			return "";
		} else {
//			SdaiModel model = (SdaiModel)comboModels.getSelectedItem();
//			return ((model != null)?"/"+model.getRepository().getName()+"/"+model.getName():"")+"/"+extent.getName(null);
			if(currentSchemaInstance != null) {
				return "/" + currentSchemaInstance.getRepository().getName() + 
					   "/" + currentSchemaInstance.getName() + "/" + extent.getName(null);
			} else if(currentModel != null) {
				return "/" + currentModel.getRepository().getName() + 
					   "/" + currentModel.getName() + "/" + extent.getName(null);
			} else {
				SdaiModel model = (SdaiModel)comboModels.getSelectedItem();
				return ((model != null)?"/"+model.getRepository().getName()+"/"+model.getName():"")+"/"+extent.getName(null);
			}
		}
	}

	public int getMode() throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection == instances) {
			mode = mCREATE_DELETE;
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == instances) {
//			instances.getSelected(result);
		} else if (lastSelection == comboModels) {
			result.add(comboModels.getSelectedItem());
		} else {
			result.add(null);
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (instances.hasFocus()) {
			result = true;
		}
		else if (comboModels.getSelectedIndex() != -1) {
			result = true;
		}
		return result;
	}

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
//		model.createEntityInstance(extent);
//		instances.fireAggregateChanged();
	};

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection == instances) {
			if (!instances.isSelectionEmpty()) {
				((EEntity)instances.getSelectedValue()).deleteApplicationInstance();
//				instances.fireAggregateChanged();
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
				+"} - EntityExtent - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
	    	SdaiModel model = (SdaiModel)comboModels.getSelectedItem();
			result += "Model:\t"+((model != null)?model.getName():"")+"\n";
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Definition:\t"+definition.getText()+"\n";
		result += "-Instances-"+"\n";
		for (int i = 0; i < instances.getModel().getSize(); i++) {
			result += instances.getModel().getElementAt(i)+"\n";
		}
		return result;
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			Object value = e.getValue();
			if (value instanceof SdaiModel) {
				fireGo(new GoEvent(thisis, value, "Model"));
			} else if (value instanceof EEntity) {
				Object s[] = new Object[2];
				s[0] = value;
				if(currentSchemaInstance != null) {
					s[1] = currentSchemaInstance;
				} else if(currentModel != null) {
					s[1] = currentModel;
				} else {
					s[1] = domain;
				}
				fireGo(new GoEvent(thisis, s, "EntityInstance"));
			}
		}
	};
}
