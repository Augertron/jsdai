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

import java.awt.event.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.text.*;

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class AggregateBean extends SdaiPanel {
	EEntity instance;
	Aggregate aggregate;

	/** Data domain origin can be object of one of types: SdaiModel, SchemaInstance, ASdaiModel */
	Object dataDomainOrigin;

	GoList list = new GoList();

	JTextField editField = new JTextField();
	JComboBox comboField = new JComboBox();
	JLabel ltype = new JLabel();

	JPanel two = new JPanel(new CardLayout());
	JPanel editPanel = new JPanel(new BorderLayout());
	JPanel ptype = new JPanel(new BorderLayout());

	boolean select_aggregate = false;
	boolean entityAggregate = false;
	private JButton bcreate = null;
	JComboBox cb = new JComboBox();

	SdaiListener sdaiListener = new SdaiListener() {
		public void actionPerformed(SdaiEvent e) {
			switch (e.getId()) {
				case SdaiEvent.INVALID :
					setAggregateAndDomain(null, null);
					break;
				case SdaiEvent.MODIFIED :
					refreshData();
					break;
			}
		}
	};

	public AggregateBean() {
		setLayout(new BorderLayout());

		ltype.addFocusListener(focusListener);
		ltype.addMouseListener(mouseListener);
		ltype.addKeyListener(keyListener);
		ptype.add(ltype, BorderLayout.CENTER);
		ptype.setVisible(false);
		add(ptype, BorderLayout.NORTH);

//		setViewportView(list);
		list.setFixedCellHeight(17);
		list.setFixedCellWidth(170);
		list.setUnderlying(true);
//		JLabel la = new JLabel("A");
//		la.setPreferredSize(new Dimension(1, (int)la.getPreferredSize().getHeight()));
//		list.setPrototypeCellValue(la);

//		list.addListSelectionListener(listListener);
//		list.addFocusListener(focusListener);
//		list.addMouseListener(mouseListener);
		list.addGoListener(goListener);
//		list.addKeyListener(findKeyListener);
//		list.addKeyListener(keyListener);
//		list.setCellRenderer(new SdaiCellRenderer());
		add(new JScrollPane(list), BorderLayout.CENTER);

		two.add(editField, "edit");
		two.add(comboField, "combo");
		comboField.setPreferredSize(two.getPreferredSize());
		editPanel.add(two, BorderLayout.CENTER);
		editPanel.setVisible(false);
		editField.addFocusListener(focusListener);
		editPanel.add(cb, BorderLayout.EAST);
		cb.setVisible(false);

		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		// I don't know what button Set should do. V.N.
//		JButton bset = new JButton("Set");
//		bset.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					editField.setText(getClipboardElement().toString());
//				} catch (SdaiException h) {processMessage(h);}
//			}
//		});
//		bar.add(bset);
		JButton bget = new JButton("Get");
		bget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Vector selection = new Vector();
					//getSelected(selection);
					Object selected = list.getSelectedValue();
					if (selected != null) {
						SdaiSession.getSession().getClipboard().addByIndex(SdaiSession.getSession().getClipboard().getMemberCount()+1, selected, null);
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(bget);
		JButton badd = new JButton("Add");
		badd.addActionListener(new ActionListener() {
			synchronized public void actionPerformed(ActionEvent e) {
				try {
					SimpleOperations.enshureReadWriteModel(instance.findEntityInstanceSdaiModel());
					EEntity domain = SimpleOperations.getBase_typeDomain(getAggregate().getAggregationType().getElement_type(null));
					if (domain instanceof EAggregation_type) {
						Object agg = SimpleOperations.createElementAggregate(getAggregate(), SimpleOperations.correctAggregateIndex(list.getSelectedIndex(), getAggregate().getAggregationType(), 0), null);
						fireAggregateChanged();
						list.setSelectedValue(instance, true);
						fireSdaiSelectionProcessed();
					} else {
						EDefined_type defs[] = null;
						if (select_aggregate) {
							Vector element = (Vector)cb.getSelectedItem();
							defs = new EDefined_type[element.size()];
							defs = (EDefined_type[])element.toArray(defs);
						}
						String value = "";
						if ((domain instanceof EEnumeration_type) || (domain instanceof EBoolean_type) || (domain instanceof ELogical_type)) {
							int k = comboField.getSelectedIndex();
							value = ((k == -1)?"":String.valueOf(k+1));
						} else {
							value = editField.getText();
						}
						if(!entityAggregate) {
							SimpleOperations.addElementString(instance, getAggregate(), SimpleOperations.correctAggregateIndex(list.getSelectedIndex(), getAggregate().getAggregationType(), 0), value, defs);
						} else {
							SimpleOperations.addElementObject(getAggregate(), SimpleOperations.correctAggregateIndex(list.getSelectedIndex(), getAggregate().getAggregationType(), 0), getClipboardElement(), null);
						}
					}
					fireAggregateChanged();
					list.setSelectedValue(instance, true);
					fireSdaiSelectionProcessed();
				} catch (SdaiException h) { processMessage(h); }
			}
		});
		bar.add(badd);
		bcreate = new JButton("Create");
		bcreate.addActionListener(new ActionListener() {
			synchronized public void actionPerformed(ActionEvent e) {
				try {
                	SdaiModel owning_model = instance.findEntityInstanceSdaiModel();
					SimpleOperations.enshureReadWriteModel(owning_model);
					EEntity domain = SimpleOperations.getBase_typeDomain(getAggregate().getAggregationType().getElement_type(null));
                	ASdaiModel dataDomain = AttributePanel.getDataDomain(owning_model);
                    Collection entDefinitions =
                    	AttributePanel.getDomainEntityDefinitions(SdaiSession.getSession().getSdaiContext(), domain, null);
            		Collection edList = AttributePanel.getEntity_definition_list(entDefinitions, dataDomain, null);
            		if(edList != null) {
	                    Vector availableTypes = new Vector(edList);
	                    EEntity_definition selectedEntity =
	                    	AttributePanel.getEntityDefinitionDialog(AggregateBean.this, availableTypes);
						if (selectedEntity != null) {
							EEntity newInstance = owning_model.createEntityInstance(selectedEntity);
							SimpleOperations.addElementObject(getAggregate(), SimpleOperations.correctAggregateIndex(list.getSelectedIndex(), getAggregate().getAggregationType(), 0), newInstance, null);
						}					
						fireAggregateChanged();
						list.setSelectedValue(instance, true);
						fireSdaiSelectionProcessed();
            		}
				} catch (SdaiException h) { processMessage(h); }
			}
		});
		bcreate.setVisible(false);
		bar.add(bcreate);
		JButton bremove = new JButton("Remove");
		bremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SimpleOperations.enshureReadWriteModel(instance.findEntityInstanceSdaiModel());
					if (!list.isSelectionEmpty() && (list.getSelectedIndex() != -1)) {
						SimpleOperations.removeElement(getAggregate(), SimpleOperations.correctAggregateIndex(list.getSelectedIndex(), getAggregate().getAggregationType(), 0));
						fireAggregateChanged();
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(bremove);
		editPanel.add(bar, BorderLayout.SOUTH);

		add(editPanel, BorderLayout.SOUTH);
	}

	public AggregateBean(Aggregate aggregate, Object modelInstanceDomain) throws SdaiException {
		this();
		setAggregateAndDomain(aggregate, modelInstanceDomain);
	}

	public void setAggregateAndDomain(Aggregate aggregate, Object modelInstanceDomain) {
		this.aggregate = aggregate;
		aggregate.addSdaiListener(sdaiListener);
		dataDomainOrigin = modelInstanceDomain;
		refreshData();
	}

    public void pushChainElementValues(List list) throws SdaiException {
        list.add(instance);
        list.add(aggregate);
        list.add(dataDomainOrigin);
    }
    
    public void popChainElementValues(List list) throws SdaiException {
		setInstance((EEntity)list.get(0));
		setAggregateAndDomain((Aggregate)list.get(1), list.get(2));
    }

	public void refreshData() {
		try {
			ListModel lm = list.getModel();
	/*		if (lm instanceof AggregateListModel) {
				if (((AggregateListModel)lm).getAggregate() == aggregate) return;
			}*/
			list.setModel(new AggregateListModel(aggregate));

	//for selects
			if (aggregate.getAggregationType() != null) {
				entityAggregate = false;
				if (aggregate.getAggregationType().testElement_type(null)) {
					ltype.setText(SimpleOperations.getDomainString(aggregate.getAggregationType()));
					EEntity domain = SimpleOperations.getBase_typeDomain(aggregate.getAggregationType().getElement_type(null));
					JComponent c;
					if ((domain instanceof EEnumeration_type) || (domain instanceof EBoolean_type) || (domain instanceof ELogical_type)) {
						((CardLayout)two.getLayout()).show(two, "combo");
						if (domain instanceof EEnumeration_type) {
							EEnumeration_type et = (EEnumeration_type)domain;
							comboField.setModel(new AggregateListModel(et.getElements(null)));
						} else if (domain instanceof EBoolean_type) {
							String b[] = {"false", "true"};
							comboField.setModel(new DefaultComboBoxModel(b));
						} else if (domain instanceof ELogical_type) {
							String b[] = {"false", "true", "unknown"};
							comboField.setModel(new DefaultComboBoxModel(b));
						}
					} else {
						((CardLayout)two.getLayout()).show(two, "edit");
						if(domain instanceof EEntity_definition) entityAggregate = true;
					}
					ESelect_type st = SimpleOperations.isSelectInside(aggregate.getAggregationType().getElement_type(null));
					if (st != null) {
						select_aggregate = true;
						Vector nodes = new Vector();
						if (SimpleOperations.getNodes(SdaiSession.getSession().getSdaiContext(), st, nodes, new Vector())) {
							cb.setModel(new NodesListModel(nodes));
							cb.setRenderer(new SdaiCellRenderer());
							cb.setVisible(true);
						} else {
							cb.setVisible(false);
							select_aggregate = false;
							entityAggregate = true;
						}
					} else {
						cb.setVisible(false);
						select_aggregate = false;
					}
				}
				two.setVisible(!entityAggregate);
				bcreate.setVisible(entityAggregate);
			}
		} catch (SdaiException ex) { processMessage(ex); }
	}

	public Aggregate getAggregate() {
		ListModel model = list.getModel();
		if (model instanceof AggregateListModel) {
			return ((AggregateListModel)model).getAggregate();
		} else {
			return null;
		}
	}

	public JList getList() {
		return list;
	}

	public void setEditable(boolean editable) {
		editPanel.setVisible(editable);
	}

	public boolean getEditable() {
		return editPanel.isVisible();
	}

	public void setType(boolean istype) {
		ptype.setVisible(istype);
	}

	public String getTreeLeave() throws SdaiException {
		if (instance == null) {
			return "";
		} else {
			return BaseEntityBean.getLocationString(instance, dataDomainOrigin) + 
				   "/Aggregate";
		}
	}

	public int getMode() throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection == list) {
			mode = mADD_REMOVE;
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == list) {
			if (!list.isSelectionEmpty()) {
				result.add(list.getSelectedValue());
			}
		} else if (lastSelection == ltype) {
			result.add(getAggregate().getAggregationType());
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (list.hasFocus()) {
			result = !list.isSelectionEmpty();
		}
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
		editPanel.setVisible(true);
//		paintAll(getGraphics());
	};

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
		editPanel.setVisible(false);
//		paintAll(getGraphics());
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
		editPanel.setVisible(false);
//		paintAll(getGraphics());
	};

	public void sdaiNew() throws SdaiException {
		if (lastSelection instanceof JList) {
			if (SimpleOperations.getBase_typeDomain(getAggregate().getAggregationType().getElement_type(null)) instanceof EAggregation_type) {
				SimpleOperations.createElementAggregate(getAggregate(), (list.getSelectedIndex()==-1)?1:list.getSelectedIndex()+1, null);
			} else {
				EDefined_type defs[] = null;
				if (select_aggregate) {
					Vector element = (Vector)cb.getSelectedItem();
					defs = new EDefined_type[element.size()];
					defs = (EDefined_type[])element.toArray(defs);
				}
				SimpleOperations.addElementString(null, getAggregate(), list.getSelectedIndex()+1, editField.getText(), defs);
			}
		} else if (lastSelection instanceof JTextField) {
			editField.setText(getClipboardElement().toString());
		}
//		paintAll(getGraphics());
	};

	public void sdaiDestroy() throws SdaiException {
		if (!list.isSelectionEmpty() && (list.getSelectedIndex() != -1)) {
			SimpleOperations.removeElement(getAggregate(), list.getSelectedIndex()+1);
			fireAggregateChanged();
//			paintAll(getGraphics());
		}
	};

/**This method must be executed if aggregate or it contents changed. In future aggregate by self will suport this operation.*/
	public void fireAggregateChanged() throws SdaiException {
		AggregateListModel listModel = (AggregateListModel)list.getModel();
		listModel.fireContentsChanged();
	}

	public void setInstance(EEntity instance) {
		this.instance = instance;
	}

	public EEntity getInstance() {
		return instance;
	}

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - Aggregate - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Type:\t"+ltype.getText()+"\n";
		result += "-Elements-"+"\n";
		for (int i = 0; i < list.getModel().getSize(); i++) {
			result += list.getModel().getElementAt(i)+"\n";
		}
		return result;
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			Object value = e.getValue();
			if (value instanceof EEntity) {
				Object s[] = new Object[2];
				s[0] = value;
				s[1] = dataDomainOrigin;
				fireGo(new GoEvent(thisis, s, "EntityInstance"));
			} else if (value instanceof Aggregate) {
				Object s[] = new Object[3];
				s[0] = instance;
				s[1] = value;
				s[2] = dataDomainOrigin;
				fireGo(new GoEvent(thisis, s, "Aggregate"));
			}
		}
	};


}