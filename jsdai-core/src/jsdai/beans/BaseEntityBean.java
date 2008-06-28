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
import java.util.List;
import java.util.*;
import java.text.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import jsdai.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

public class BaseEntityBean extends SdaiPanel {
	ASdaiModel dataDomain = null;
	/** Data domain origin can be object of one of types: SdaiModel, SchemaInstance, ASdaiModel */
	Object dataDomainOrigin;
	String ident = "   ";

	EEntity entity = null;

	Vector textes 		= new Vector();
	Vector attributs 	= new Vector();
	Vector combos 		= new Vector();
	Vector unsets_v 	= new Vector();
	Vector types 		= new Vector();

	JPanel pattr = new JPanel();

	GoList inverses = new GoList();
	AggregateListModel inversesModel = new AggregateListModel();

	JTextField instance = new JTextField();
	GoTextField owning_model = new GoTextField();
	GoTextField entity_definition = new GoTextField();

	JButton bedit, baccept, bcancel;
	boolean editMode = false;
    boolean invalidEntity = false;

	JScrollPane sb;


	boolean isDeveloperMode = false;

	boolean isLockSplits = false;
	int splitLocation = 0;
	ArrayList splits = new ArrayList();
	boolean lockChangeListener = false;

	Vector values = new Vector();

	SdaiListener sdaiListener = new SdaiListener() {
		public void actionPerformed(SdaiEvent e) {
			try {
				switch (e.getId()) {
					case SdaiEvent.INVALID :
						setEntityAndModelInstance(null, null);
						break;
					case SdaiEvent.MODIFIED :
						if (!editMode && !invalidEntity) {
							refreshData();
						}
						break;
				}
			} catch (SdaiException ex) { processMessage(ex); }
		}
	};

	ActionListener limitedTypeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				JComboBox j = (JComboBox)e.getSource();
				Vector v =(Vector)j.getSelectedItem();
				EDefined_type dts[] = (EDefined_type[])v.toArray(new EDefined_type[v.size()]);
				EEntity domain = SimpleOperations.getNodesDomain(dts);
				EAttribute attribute = (EAttribute)attributs.elementAt(indexForCombo(j));
				JPanel panel = (JPanel)textes.elementAt(indexForCombo(j));
				Component comps[] = panel.getComponents();
				JComboBox tik = null;
				JTextField dik = null;
				if (comps[0] instanceof JComboBox) tik = (JComboBox)comps[0];
				if (comps[1] instanceof JTextField) dik = (JTextField)comps[1];
				CardLayout cl = (CardLayout)panel.getLayout();
				if (domain instanceof EEnumeration_type) {
					EEnumeration_type et = (EEnumeration_type)domain;
					tik.setModel(new AggregateListModel(et.getElements(null)));
					if (entity.testAttribute(attribute, new EDefined_type[10]) != 0) {
						tik.setSelectedIndex(SimpleOperations.getAttributeInt(entity, attribute)-1);
					}
					cl.show(panel, "combo");
				} else if (domain instanceof EBoolean_type) {
					String b[] = {"false", "true"};
					tik.setModel(new DefaultComboBoxModel(b));
					if (entity.testAttribute(attribute, new EDefined_type[10]) != 0) {
						tik.setSelectedIndex(SimpleOperations.getAttributeInt(entity, attribute)-1);
					}
					cl.show(panel, "combo");
				} else if (domain instanceof ELogical_type) {
					String b[] = {"false", "true", "unknown"};
					tik.setModel(new DefaultComboBoxModel(b));
					if (entity.testAttribute(attribute, new EDefined_type[10]) != 0) {
						tik.setSelectedIndex(SimpleOperations.getAttributeInt(entity, attribute)-1);
					}
					cl.show(panel, "combo");
				} else {
					if (entity.testAttribute(attribute, new EDefined_type[10]) != 0) {
						dik.setText(SimpleOperations.getAttributeString(entity, attribute));
					}
					cl.show(panel, "field");
				}
			} catch (SdaiException h) {processMessage(h);}
		}
	};

	public BaseEntityBean() {
		setLayout(new BorderLayout());

		JPanel scroll = new JPanel(new BorderLayout());
		JPanel pentity = new JPanel(new BorderLayout());
		((BorderLayout)pentity.getLayout()).setVgap(5);
		BorderLayout bl_t = new BorderLayout();
		bl_t.setVgap(5);
		JPanel title = new JPanel(bl_t);
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel north = new JPanel(bl);
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JLabel label;
		label = (JLabel)labels.add(new JLabel("Model"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label = (JLabel)labels.add(new JLabel("Type"));
		label.setHorizontalAlignment(JLabel.RIGHT);
		north.add(labels, BorderLayout.WEST);
		JPanel texts = new JPanel(gl);
		texts.add(owning_model);
		owning_model.addGoListener(goListener);
		owning_model.setUnderlying(true);
//		owning_model.addMouseListener(mouseListener);
//		owning_model.addFocusListener(focusListener);
//		owning_model.addKeyListener(keyListener);
		owning_model.setEditable(false);
		texts.add(entity_definition);
		entity_definition.addGoListener(goListener);
		entity_definition.setUnderlying(true);
//		entity_definition.addMouseListener(mouseListener);
//		entity_definition.addFocusListener(focusListener);
//		entity_definition.addKeyListener(keyListener);
		entity_definition.setEditable(false);
		north.add(texts, BorderLayout.CENTER);
		title.add(north, BorderLayout.NORTH);

		BoundedRangeModel xbound = instance.getHorizontalVisibility();
		xbound.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				BoundedRangeModel bound = (BoundedRangeModel)e.getSource();
				if (!instance.hasFocus()) {
					bound.setValue(0);
				}
			}
		});

//		instance.addMouseListener(mouseListener);
//		instance.addFocusListener(focusListener);
		instance.setEditable(false);
		title.add(instance, BorderLayout.SOUTH);
		pentity.add(title, BorderLayout.NORTH);

		pattr.setLayout(new BoxLayout(pattr, BoxLayout.Y_AXIS));
		pattr.setBorder(new MatteBorder(1, 0, 1, 0, Color.darkGray));
		pentity.add(pattr, BorderLayout.CENTER);
		scroll.add(pentity, BorderLayout.NORTH);

		JPanel pa = new JPanel(new BorderLayout());
		pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Users"));
//		inverses.addSdaiSelectableListener(selectableListener);
		inverses.addGoListener(goListener);
//		inverses.addKeyListener(findKeyListener);
		inverses.setModel(inversesModel);
		inverses.setBackground(getBackground());
		inverses.setUnderlying(true);

		pa.add(new JScrollPane(inverses), BorderLayout.CENTER);
		scroll.add(pa, BorderLayout.CENTER);
		sb = new JScrollPane(scroll, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sb.getVerticalScrollBar().setUnitIncrement(10);
		sb.getHorizontalScrollBar().setUnitIncrement(10);
		add(sb, BorderLayout.CENTER);

		JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton bget = new JButton("Get");
		bget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (entity != null) {
						SdaiSession.getSession().getClipboard().addByIndex(SdaiSession.getSession().getClipboard().getMemberCount()+1, entity, null);
					}
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(bget);
		bedit = new JButton("Edit");
		bedit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SimpleOperations.enshureReadWriteModel(entity.findEntityInstanceSdaiModel());
//					sdaiEdit();
				    setEditEnable(true);
					editMode = true;
				} catch (SdaiException h) {processMessage(h);}
			}
		});
		bar.add(bedit);
		baccept = new JButton("Accept");
		baccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
//					sdaiAccept();
					for (int i = 0; i < values.size(); i++) {
						AttributePanel value = (AttributePanel)values.get(i);
						value.setAttributeOnInstance(entity);
					}
					setEditEnable(false);
					editMode = false;
					refreshData();
				} catch (SdaiException h) { processMessage(h); }
			}
		});
		bar.add(baccept);
		bcancel = new JButton("Cancel");
		bcancel.setMnemonic('C');
		bcancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEditEnable(false);
				editMode = false;
				refreshData();
			}
		});
		bar.add(bcancel);
//		JButton get = new JButton("Get");
//		get.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					Vector selection = new Vector();
//					getSelected(selection);
//					if (selection.size() > 0) {
//						SdaiSession.getSession().getClipboard().addByIndex(SdaiSession.getSession().getClipboard().getMemberCount()+1, selection.elementAt(selection.size()-1), null);
//					}
//				} catch (SdaiException h) {processMessage(h);}
//			}
//		});
//		bar.add(get);
//		ButtonWithMenu set = new ButtonWithMenu("Set|Create");
//		set.setToolTipText("set attribute or create aggregate");
//		set.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					SimpleOperations.enshureReadWriteModel(entity.findEntityInstanceSdaiModel());
//					sdaiNew(0);
//				} catch (SdaiException h) {processMessage(h);}
//			}
//		});
//		set.addMenuListener(new MouseAdapter() {
//			public void mouseReleased(MouseEvent e) {
//				try {
//					JMenuItem mi = (JMenuItem)e.getSource();
//					JPopupMenu pm = (JPopupMenu)mi.getParent();
//					sdaiNew(pm.getComponentIndex(mi));
//				} catch (SdaiException ex) { processMessage(ex); }
//			}
//		});
//		bar.add(set);
//		JButton unset = new JButton("Unset|Delete");
//		set.setToolTipText("unset attribute or delete aggregate");
//		unset.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					SimpleOperations.enshureReadWriteModel(entity.findEntityInstanceSdaiModel());
//					sdaiDestroy();
//				} catch (SdaiException h) {processMessage(h);}
//			}
//		});
//		bar.add(unset);
		add(bar, BorderLayout.SOUTH);
	}

	public BaseEntityBean(EEntity entity, Object modelInstanceDomain) throws SdaiException {
		this();
		setEntityAndModelInstance(entity, modelInstanceDomain);
	}

	//public void setEntityAndDomain(EEntity entity, ASdaiModel domain)
	public void setEntityAndModelInstance(EEntity entity, Object modelInstanceDomain) throws SdaiException {
        if (entity == null) {
			unsetEntity();
		   return;
		}
//		if (this.entity == entity) return;
		try {
			this.entity = entity;
			dataDomainOrigin = modelInstanceDomain;
			if(dataDomainOrigin instanceof SchemaInstance) {
				dataDomain = ((SchemaInstance)dataDomainOrigin).getAssociatedModels();
			} else if(dataDomainOrigin instanceof SdaiModel) {
				dataDomain = new ASdaiModel();
				dataDomain.addByIndex(1, (SdaiModel)dataDomainOrigin, null);
			} else {
				dataDomain = (ASdaiModel)dataDomainOrigin;
			}
			invalidEntity = false;
            entity.addSdaiListener(sdaiListener);
            pattr.removeAll();
			textes.removeAllElements();
			attributs.removeAllElements();
			combos.removeAllElements();
			types.removeAllElements();
			unsets_v.removeAllElements();
			values.removeAllElements();
			splits.clear();
			buildAttributes(entity.getInstanceType(), new Vector());
			if (isLockSplits) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JSplitPane sp = (JSplitPane)splits.get(0);
						sp.setDividerLocation(sp.getWidth()-getMaxSizeSplit());
					}
				});
			}
			setEditEnable(false);
			sb.getVerticalScrollBar().setValue(0);
			refreshData();
		} catch (SdaiException ex) { processMessage(ex); }
	}

    public void pushChainElementValues(List list) throws SdaiException {
        list.add(entity);
        list.add(dataDomainOrigin);
    }
    
    public void popChainElementValues(List list) throws SdaiException {
		setEntityAndModelInstance((EEntity)list.get(0), list.get(1));
    }
    
	public void unsetEntity() {
//		try {
//		    entity.removeSdaiListener(sdaiListener);
//			pattr.removeAll();
//			textes.removeAllElements();
//			attributs.removeAllElements();
//			combos.removeAllElements();
//			types.removeAllElements();
//			unsets_v.removeAllElements();
//			values.removeAllElements();
//		} catch (SdaiException ex) {
//			processMessage(ex);
//		}
        invalidEntity = true;
		fireGo(new GoEvent(thisis, null, "Hell"));
	}

	private void buildAttributes(EEntity_definition definition, Vector definitions) throws SdaiException {
//printing attributes of supertypes
		AEntity_definition supertypes = definition.getSupertypes(null);
		SdaiIterator it_s = supertypes.createIterator();
		while (it_s.next()) {
			EEntity_definition supertype = supertypes.getCurrentMember(it_s);
			if (definitions.indexOf(supertype) == -1) {
				buildAttributes(supertype, definitions);
				definitions.add(supertype);
			}
		}
		if(definition.getComplex(null)) return; // complexes don't have to be displayed as themsevles
//printing attributes of this definition
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel panel = new JPanel(bl);
		panel.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), definition.getName(null)));
		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel plabels = new JPanel(gl);
		JPanel ptexts = new JPanel(gl);
		JPanel punsets = new JPanel(gl);
		JPanel ptypes = new JPanel(gl);
		AExplicit_attribute attributes = definition.getExplicit_attributes(null);
		SdaiIterator it_attributes = attributes.createIterator();
		while (it_attributes.next()) {
			EExplicit_attribute ea = attributes.getCurrentMember(it_attributes);
//			if (!SimpleOperations.testRedeclaring(ea, entity.getInstanceType(), null)) {
				buildAttribute(plabels, ptexts, punsets, ptypes, ea);
//			}
		}
		AAttribute iattributes = definition.getAttributes(null, null);
		SdaiIterator it_iattributes = iattributes.createIterator();
		while (it_iattributes.next()) {
			EAttribute attribute = iattributes.getCurrentMember(it_iattributes);
// 			if ((attribute instanceof EExplicit_attribute) && (SimpleOperations.getRedeclaring(attribute) != null)) {
// //				if (!SimpleOperations.testRedeclaring(attribute, entity.getInstanceType(), null)) {
// 					buildAttribute(plabels, ptexts, punsets, ptypes, attribute);
// //				}
// 			} else
			if (attribute instanceof EInverse_attribute) {
//				if (!SimpleOperations.testRedeclaring(attribute, entity.getInstanceType(), null)) {
					buildAttribute(plabels, ptexts, punsets, ptypes, attribute);
//				}
			} else if (attribute instanceof EDerived_attribute) {
//				if (!SimpleOperations.testRedeclaring(attribute, entity.getInstanceType(), null)) {
					buildAttribute(plabels, ptexts, punsets, ptypes, attribute);
//				}
			}
		}
		panel.add(plabels, BorderLayout.WEST);
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splits.add(sp);
		if (isLockSplits) {
//not	   	sp.setDividerLocation(splitLocation);
			sp.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if (!lockChangeListener && e.getPropertyName().equals("dividerLocation")) {
						lockChangeListener = true;
						for (int i = 0; i < splits.size(); i++) {
							JSplitPane split = (JSplitPane)splits.get(i);
							if (split != e.getSource()) {
								JSplitPane thisPane = (JSplitPane)e.getSource();
								splitLocation = split.getWidth()-(thisPane.getWidth()-thisPane.getDividerLocation());
								split.setDividerLocation(((splitLocation > 0)&&(splitLocation < split.getWidth()))?splitLocation:0);
							}
						}
						lockChangeListener = false;
					}
				}
		   });
		}
		sp.setDividerSize(5);
		sp.setBorder(null);
		sp.setLeftComponent(ptypes);
		sp.setRightComponent(ptexts);
		panel.add(sp, BorderLayout.CENTER);
		panel.add(punsets, BorderLayout.EAST);
		pattr.add(panel);
	}

	private void buildAttribute(JPanel plabels, JPanel ptexts, JPanel punsets, JPanel ptypes, EAttribute attribute) throws SdaiException {
//labels
//		JLabel label = new JLabel(ident+SimpleOperations.getAttributeName(attribute));
//		label.setHorizontalAlignment(JLabel.RIGHT);
//		label.setToolTipText(SimpleOperations.getAttributeDomainString(attribute));
//		plabels.add(label);
//types
		GoLabel ltype = new GoLabel(ident+SimpleOperations.getAttributePrefix(attribute)+" "
		   +SimpleOperations.getAttributeName(attribute)+": "+SimpleOperations.getAttributeDomainString(attribute));
		if (isDeveloperMode) {
		    ltype.setUnderlying(true);
		    ltype.setLink(SimpleOperations.getAttributeDomain(attribute));
		}
		ltype.addGoListener(goListener);
//		ltype.addMouseListener(mouseListener);
//		ltype.addKeyListener(keyListener);
		ltype.setMinimumSize(new Dimension(0, 0));
		types.add(ltype);
		ptypes.add(ltype);
//texts
/*		EEntity domain = SimpleOperations.getAttributeDomain(attribute);
		JComponent c;
		if ((domain instanceof EEnumeration_type) || (domain instanceof EBoolean_type) || (domain instanceof ELogical_type)) {
			JComboBox comboField = new JComboBox();
			comboField.setEnabled(true);
			comboField.setPreferredSize(instance.getPreferredSize());
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
			c = comboField;
		} else {
			JTextField textField = new GoTextField();
			textField.addFocusListener(focusListener);
			if ((domain instanceof EAggregation_type) || (domain instanceof EEntity_definition) || (domain instanceof ESelect_type)) {
				textField.addMouseListener(mouseListener);
//				textField.addFocusListener(focusListener);
				textField.addKeyListener(keyListener);
			}
			c = textField;
		}
		attributs.add((SimpleOperations.testRedeclaring(attribute))?SimpleOperations.getRedeclaring(attribute):attribute);
	//for selects
		if (SimpleOperations.isMixedSelectInside(SimpleOperations.getAttributeDomain(attribute))) {
			JPanel p = new JPanel(new GridLayout(0, 2));
			JComboBox cb = new JComboBox();
			cb.setMinimumSize(new Dimension(0, 0));
			cb.setPreferredSize(c.getPreferredSize());
			cb.setRenderer(new SdaiCellRenderer());
			if (SimpleOperations.isLimitedTypesInsideSelect(domain)) {
				JPanel lim = new JPanel(new CardLayout());
				JComboBox comboField = new JComboBox();
				comboField.setEnabled(true);
				comboField.setPreferredSize(instance.getPreferredSize());
				lim.add(comboField, "combo");
				lim.add(c, "field");
				c = lim;
				textes.add(c);
				cb.addActionListener(limitedTypeListener);
			} else {
				textes.add(c);
			}
			p.add(c);
			p.add(cb);
			ptexts.add(p);
			combos.add(cb);
			Vector nodes = new Vector();
			ESelect_type st = SimpleOperations.isSelectInside(SimpleOperations.getAttributeDomain(attribute));
			SimpleOperations.getNodes(st, nodes, new Vector());
			cb.setModel(new NodesListModel(nodes));
		} else {
			textes.add(c);
			ptexts.add(c);
			combos.add(null);
		}*/
		AttributePanel value = new AttributePanel(attribute);
		value.addGoListener(goListener);
        ptexts.add(value);
		values.add(value);

//unsets
		JTextField unset = new JTextField();
		Dimension d = new Dimension(8, 21);
		unset.setPreferredSize(d);
		unset.setMaximumSize(d);
		unset.setMinimumSize(d);
		unset.setEditable(false);
		unset.setBorder(null);
		punsets.add(unset);
		unsets_v.addElement(unset);
	}

/*	private void refreshAttributes(EEntity_definition definition) throws SdaiException {
//printing attributes of supertypes
		AEntity_definition definitions = definition.getSupertypes(null);
		SdaiIterator it_definitions = definitions.createIterator();
		while (it_definitions.next()) {
			refreshAttributes(definitions.getCurrentMember(it_definitions));
		}
//printing attributes of this definition
		AExplicit_attribute attributes = definition.getExplicit_attributes(null);
		SdaiIterator it_attributes = attributes.createIterator();
		while (it_attributes.next()) {
			if (attributes.getCurrentMember(it_attributes) instanceof EExplicit_attribute) {
				EExplicit_attribute ea = (EExplicit_attribute)attributes.getCurrentMember(it_attributes);
				refreshAttribute(ea);
			}
		}
	}

	private void refreshAttribute(EAttribute attribute) throws SdaiException {
		int i = indexForAttribute(attribute);
		if (entity.testAttribute(attribute, null) != 0) {
			((JTextField)textes.elementAt(i)).setText(SimpleOperations.getAttributeString(entity, attribute));
			((JTextField)unsets_v.elementAt(i)).setText("X");
		} else {
			((JTextField)textes.elementAt(i)).setText("");
			((JTextField)unsets_v.elementAt(i)).setText("$");
		}
	}*/

	public void refreshData() {
		try {
		    owning_model.setLink(entity.findEntityInstanceSdaiModel());
			owning_model.setText(entity.findEntityInstanceSdaiModel().getName());
	   	entity_definition.setLink(entity.getInstanceType());
			entity_definition.setText(entity.getInstanceType().getName(null));
			instance.setText(entity.toString());
			instance.setPreferredSize(new Dimension(0, (int)instance.getPreferredSize().getHeight()));
			AEntity result = new AEntity();
//Debug.println("Models"+String.valueOf(dataDomain.getMemberCount()));
//		   ASdaiModel mols = new ASdaiModel();
//		   mols.addByIndex(1, entity.findEntityInstanceSdaiModel(), null);
			entity.findEntityInstanceUsers(dataDomain, result);
//Debug.println("Result1 "+String.valueOf(result.getMemberCount()));
//			entity.findEntityInstanceUsers(null, result);
//Debug.println("Result2 "+String.valueOf(result.getMemberCount()));
			inversesModel.setAggregate(result);
			inverses.setVisibleRowCount((inverses.getModel().getSize()<20)?inverses.getModel().getSize()+1:20);
			refreshAttributes();
			revalidate();
			repaint();
		} catch (SdaiException ex) { processMessage(ex); }
//} catch (SdaiException ex) { 
//int kkk = ex.getErrorId();
//System.out.println("  BaseEntityBean    kkk = " + kkk);
//ex.printStackTrace();
//processMessage(ex); }
	}

	private void refreshAttributes() throws SdaiException {
		for (int i = 0; i < values.size(); i++) {
/*			EAttribute attribute = (EAttribute)attributs.elementAt(i);
			if (attribute instanceof EExplicit_attribute) {
				EDefined_type dts[] = new EDefined_type[10];
				Object o = textes.elementAt(i);
				if (entity.testAttribute(attribute, dts) != 0) {
					if (dts != null) {
						int k = 0;
						while ((k < dts.length) && (dts[k] != null)) {
							k++;
						}
						if (k > 0) {
							EDefined_type ndts[] = new EDefined_type[k+1];
							System.arraycopy(dts, 0, ndts, 0, k+1);
							Vector x = new Vector();
							SimpleOperations.addArrayToVector(x, ndts);
							JComboBox c = (JComboBox)combos.elementAt(i);
							c.setSelectedItem(x);
						}
					}
					if (o instanceof JTextField) {
						((JTextField)o).setText(SimpleOperations.getAttributeString(entity, attribute));
						((JTextField)o).scrollRectToVisible(new Rectangle(1, 1));
					} else if (o instanceof JComboBox) {
						((JComboBox)o).setSelectedIndex(SimpleOperations.getAttributeInt(entity, attribute)-1);
					} else if (o instanceof JPanel) {
// this will refresh automaticaly
					}
					((JTextField)unsets_v.elementAt(i)).setText("");
				} else {
					if (o instanceof JTextField) {
						((JTextField)o).setText("");
					} else if (o instanceof JComboBox) {
						((JComboBox)o).setSelectedItem(null);
					} else if (o instanceof JPanel) {
// this will refresh automaticaly
					}
					((JTextField)unsets_v.elementAt(i)).setText("$");
				}
			} else if (attribute instanceof EInverse_attribute) {
				EInverse_attribute ie = (EInverse_attribute)attribute;
				((JTextField)textes.elementAt(i)).setText(entity.get_inverse(ie, null).toString());
//				((JTextField)textes.elementAt(i)).setText("Inverses not implemented yet");
				((JTextField)unsets_v.elementAt(i)).setText("");
			} else if (attribute instanceof EDerived_attribute) {
				EDerived_attribute da = (EDerived_attribute)attribute;
				((JTextField)textes.elementAt(i)).setText("* (Derived attributes not suported yet.)");
				((JTextField)unsets_v.elementAt(i)).setText("$");
			}
			JComponent t = (JComponent)textes.elementAt(i);
			t.setPreferredSize(new Dimension(0, (int)t.getPreferredSize().getHeight()));*/
			AttributePanel value = (AttributePanel)values.get(i);
		   value.setInstance(entity);

		   JTextField textUnset = (JTextField)unsets_v.get(i);
			EAttribute attribute = value.getAttribute();
			if (attribute instanceof EExplicit_attribute) {
			   EDefined_type dts[] = new EDefined_type[10];
				try {
					if (value.isDerived() || entity.testAttribute(attribute, dts) != 0) {
						textUnset.setText("");
					} else {
					   textUnset.setText("$");
					}
				} catch (SdaiException ex) { 
					int errorId = ex.getErrorId();
					if (errorId != SdaiException.FN_NAVL && errorId != SdaiException.AT_NVLD) {
						throw ex;
					}
				}
			} else if (attribute instanceof EInverse_attribute) {
				textUnset.setText("");
			} else if (attribute instanceof EDerived_attribute) {
				textUnset.setText("");
			}
		}
	}

/*oldest solution
	private void buildAttributes(EEntity_definition definition, boolean complex) throws SdaiException {
//printing attributes of supertypes
		if (!definition.getComplex(null)) {
			AEntity_definition definitions = definition.getSupertypes(null);
			SdaiIterator it_definitions = definitions.createIterator();
			while (it_definitions.next()) {
				buildAttributes(definitions.getCurrentMember(it_definitions), complex);
			}
		}
//printing attributes of this definition
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		JPanel panel = new JPanel(bl);
		panel.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), definition.getName(null)));

		GridLayout gl = new GridLayout(0, 1);
		gl.setVgap(5);
		JPanel labels = new JPanel(gl);
		JPanel texts = new JPanel(gl);
		JPanel unsets = new JPanel(gl);

		if (!definition.getComplex(null)) {
			AExplicit_attribute attributes = definition.getExplicit_attributes(null);//getAttributes(null, null);
			SdaiIterator it_attributes = attributes.createIterator();
			while (it_attributes.next()) {
	//			System.out.println(attributes.getCurrentMember(it_attributes).getName(null));
				if (attributes.getCurrentMember(it_attributes) instanceof EExplicit_attribute) {
					EExplicit_attribute ea = (EExplicit_attribute)attributes.getCurrentMember(it_attributes);
					buildAttribute(labels, texts, unsets, ea, definition, ea.getDomain(null));
				}
				else if (attributes.getCurrentMember(it_attributes) instanceof EDerived_attribute) {
	//				buildAttributes(attributes.getCurrentMember(it_attributes), definition, ((EDerived_attribute)attributes.getCurrentMember(it_attributes)).testDomain());
				}
				else if (attributes.getCurrentMember(it_attributes) instanceof EInverse_attribute){
	//				buildAttributes(attributes.getCurrentMember(it_attributes), definition, ((EInverse_attribute)attributes.getCurrentMember(it_attributes)).getInverted_attr().testDomain());
				}
			}
		} else {
			Vector attrs = new Vector();
			LangUtils.findExplicitAttributes(definition, attrs);
			for (int i = 0; i < attrs.size(); i++) {
				EExplicit_attribute ea = (EExplicit_attribute)attrs.elementAt(i);
				buildAttribute(labels, texts, unsets, ea, definition, ea.getDomain(null));
			}
		}
		panel.add(labels, BorderLayout.WEST);
		panel.add(texts, BorderLayout.CENTER);
		panel.add(unsets, BorderLayout.EAST);
		pattr.add(panel);
	}

	private void buildAttribute(JPanel labels, JPanel texts, JPanel unsets, EAttribute attribute, EEntity_definition definition, EEntity type) throws SdaiException {
		JLabel label = new JLabel(attribute.getName(null));
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setToolTipText(SimpleOperations.getAttributeDomainString(attribute));
		labels.add(label);

		JTextField textField = new JTextField();
		JTextField unset = new JTextField();
		unset.setEditable(false);
		EEntity domain = SimpleOperations.getAttributeDomain(attribute);
		if ((domain instanceof EAggregation_type) || (domain instanceof EEntity_definition)) {
			textField.addMouseListener(mouseListener);
			textField.addFocusListener(focusListener);
		}
		if (entity.testAttribute(attribute, null) != 0) {
			textField.setText(SimpleOperations.getAttributeString(entity, attribute));
			unset.setText("X");
		} else {
//			textField.setVisible(false);
//			textField.setText("");
			textField.setText(SimpleOperations.getAttributeString(entity, attribute));
			unset.setText("$");
		}
		unsets.add(unset);
		textes.add(textField);
		attributs.add(attribute);
//for selects
		ESelect_type st = SimpleOperations.isSelectInside(SimpleOperations.getAttributeDomain(attribute));
		if (st != null) {
			System.out.println(st);
			Vector nodes = new Vector();
			if (SimpleOperations.getNodes(st, nodes, new Vector())) {
				JPanel p = new JPanel(new BorderLayout());
				JComboBox cb = new JComboBox(nodes);
				cb.setPreferredSize(textField.getPreferredSize());
				cb.setRenderer(new SdaiCellRenderer());
				p.add(textField, BorderLayout.CENTER);
				p.add(cb, BorderLayout.EAST);
				texts.add(p);
				combos.add(cb);
			} else {
				texts.add(textField);
				combos.add(null);
			}
		} else {
			texts.add(textField);
			combos.add(null);
		}
	}*/

	private EAttribute attributeForText(JComponent text) {
		for (int i = 0; i < textes.size(); i++) {
			if (textes.elementAt(i) == text) {
				return (EAttribute)attributs.elementAt(i);
			}
		}
		return null;
	}

	private JTextField textForAttribute(EAttribute attribute) {
		for (int i = 0; i < attributs.size(); i++) {
			if (attributs.elementAt(i) == attribute) {
				return (JTextField)textes.elementAt(i);
			}
		}
		return null;
	}

	private int indexForAttribute(EAttribute attribute) {
		for (int i = 0; i < attributs.size(); i++) {
			if (attributs.elementAt(i) == attribute) {
				return i;
			}
		}
		return 0;
	}

	private int indexForCombo(JComboBox combo) {
		for (int i = 0; i < combos.size(); i++) {
			if (combos.elementAt(i) == combo) {
				return i;
			}
		}
		return 0;
	}

	public EEntity getEntity() throws SdaiException {
		return entity;
	}

	public String getTreeLeave() throws SdaiException {
		if (entity == null) {
			return "";
		} else {
			return BaseEntityBean.getLocationString(entity, dataDomainOrigin);
		}
	}

	static public String getLocationString(EEntity entity, Object dataDomainOrigin)
	throws SdaiException {
		if(dataDomainOrigin instanceof SchemaInstance) {
			SchemaInstance currentSchemaInstance = (SchemaInstance)dataDomainOrigin;
			return "/" + currentSchemaInstance.getRepository().getName() + 
				   "/" + currentSchemaInstance.getName() + "/" + 
				   entity.getInstanceType().getName(null) + "/" + entity.getPersistentLabel();
		} else if(dataDomainOrigin instanceof SdaiModel) {
			SdaiModel currentModel = (SdaiModel)dataDomainOrigin;
			return "/" + currentModel.getRepository().getName() + 
				   "/" + currentModel.getName() + "/" + 
				   entity.getInstanceType().getName(null) + "/" + entity.getPersistentLabel();
		} else {
			return "/"+entity.findEntityInstanceSdaiModel().getRepository().getName()+"/"+entity.findEntityInstanceSdaiModel().getName()+"/"+entity.getInstanceType().getName(null)+"/"+entity.getPersistentLabel();
		}
	}

	public int getMode() throws SdaiException {
		int mode = mNOT_USE;
		if (lastSelection instanceof JTextField) {
			for (int i = 0; i < textes.size(); i++) {
				if (lastSelection == textes.elementAt(i)) {
					EAttribute attribute = (EAttribute)attributs.elementAt(i);
					if (SimpleOperations.getAttributeDomain(attribute) instanceof EAggregation_type) {
						return mCREATE_DELETE;
					} else {
						return mSET_UNSET;
					}
				}
			}
		}
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
		if (lastSelection == inverses) {
			result.add(inverses.getSelectedValue());
		} else if (lastSelection == owning_model) {
			result.add(entity.findEntityInstanceSdaiModel());
		} else if (lastSelection == entity_definition) {
			result.add(entity.getInstanceType());
		} else if (lastSelection == instance) {
			result.add(entity);
		} else if (lastSelection instanceof JLabel) {
			for (int i = 0; i < types.size(); i++) {
				if (lastSelection == types.elementAt(i)) {
					result.add(SimpleOperations.getAttributeDomain((EAttribute)attributs.elementAt(i)));
				}
			}
		} else {
			for (int i = 0; i < textes.size(); i++) {
				if (lastSelection == textes.elementAt(i)) {
					if (attributs.elementAt(i) instanceof EExplicit_attribute) {
						if (entity.testAttribute((EAttribute)attributs.elementAt(i), getDefs(i)) == 1) {
							result.add(entity.get_object((EAttribute)attributs.elementAt(i)));
						}
					} else if (attributs.elementAt(i) instanceof EInverse_attribute) {
						EInverse_attribute ie = (EInverse_attribute)attributs.elementAt(i);
						result.add(entity.get_inverse(ie, null));
					}
				}
			}
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		if (inverses.hasFocus()) {
			result = true;
		} else if (entity_definition.hasFocus()) {
			result = true;
		} else if (owning_model.hasFocus()) {
			result = true;
		} else {
			for (int i = 0; i < textes.size(); i++) {
				if (((JComponent)textes.elementAt(i)).hasFocus()) {
					result = true;
				}
			}
		}
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
		setEditEnable(true);
	}

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
		for (int i = 0; i < textes.size(); i++) {
			EAttribute attr = (EAttribute)attributs.elementAt(i);
			if (!(attr instanceof EExplicit_attribute)) continue;
			Object o = textes.elementAt(i);
			String value = "";
			if (o instanceof JTextField) {
				value = ((JTextField)o).getText();
			} else if (o instanceof JComboBox) {
				int k = ((JComboBox)o).getSelectedIndex();
				value = ((k == -1)?"":String.valueOf(k+1));
			} else {
				JComboBox j = (JComboBox)combos.elementAt(i);
				Vector v =(Vector)j.getSelectedItem();
				EDefined_type dts[] = (EDefined_type[])v.toArray(new EDefined_type[v.size()]);
				EEntity domain = SimpleOperations.getNodesDomain(dts);
				JPanel panel = (JPanel)textes.elementAt(indexForCombo(j));
				Component comps[] = panel.getComponents();
				JComboBox tik = null;
				JTextField dik = null;
				if (comps[0] instanceof JComboBox) tik = (JComboBox)comps[0];
				if (comps[1] instanceof JTextField) dik = (JTextField)comps[1];
				if ((domain instanceof EEnumeration_type) || (domain instanceof EBoolean_type) || (domain instanceof ELogical_type)) {
					int k = tik.getSelectedIndex();
					value = ((k == -1)?"":String.valueOf(k+1));
				} else {
					value = dik.getText();
				}

			}
			SimpleOperations.setAttributeString(entity, attr, value, getDefs(i));
		}
		setEditEnable(false);
	}

	private EDefined_type[] getDefs(int i) throws SdaiException {
		JComboBox jc = (JComboBox)combos.elementAt(i);
		EDefined_type defs[] = null;
		if (jc == null) {
			defs = null;
		} else {
			Vector element = (Vector)jc.getSelectedItem();
			defs = new EDefined_type[element.size()];
			defs = (EDefined_type[])element.toArray(defs);
		}
		return defs;
	}

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
//		setEntity(entity);
//		setEditEnable(false);
	};

	public void setEditEnable(boolean editable) {
/*		for (int i = 0; i < textes.size(); i++) {
			Object o = textes.elementAt(i);
			if (o instanceof JTextField) {
				((JTextField)o).setEditable(editable);
			} else if (o instanceof JComboBox) {
				((JComboBox)o).setEnabled(editable);
			} else {
				JPanel panel = (JPanel)o;
				Component comps[] = panel.getComponents();
				if (comps[0] instanceof JComboBox) ((JComboBox)comps[0]).setEnabled(editable);
				if (comps[1] instanceof JTextField) ((JTextField)comps[1]).setEditable(editable);
			}
			JComboBox jc = (JComboBox)combos.elementAt(i);
			if (jc != null) {
				jc.setEnabled(editable);
			}
		}*/
		for (int i = 0; i < values.size(); i++) {
			AttributePanel value = (AttributePanel)values.get(i);
			value.setEditable(editable);
		}
		bedit.setEnabled(!editable);
		baccept.setEnabled(editable);
		bcancel.setEnabled(editable);
	}

	public void sdaiNew() throws SdaiException {
		sdaiNew(0);
	}

	public void sdaiNew(int index) throws SdaiException {
		if (lastSelection instanceof JTextField) {
			EAttribute attr = attributeForText((JTextField)lastSelection);
			if (attr != null) {
				EEntity domain = SimpleOperations.getAttributeDomain(attr);
				if (domain instanceof EAggregation_type) {
					entity.createAggregate(attr, null);
					fireSdaiSelectionProcessed();
				} else if (domain instanceof EEntity_definition) {
					textForAttribute(attr).setText(getClipboardElement(index).toString());
				} else if (domain instanceof ESelect_type) {
					EDefined_type defs[] = getDefs(indexForAttribute(attr));
					EEntity sdomain = SimpleOperations.getNodesDomain(defs);
					if (sdomain instanceof EAggregation_type) {
						entity.createAggregate(attr, defs);
					} else if (sdomain instanceof EEntity_definition) {
						textForAttribute(attr).setText(getClipboardElement(index).toString());
					}
				}
			}
		}
//		paintAll(getGraphics());
	};

	public void sdaiDestroy() throws SdaiException {
		if (lastSelection instanceof JTextField) {
			EAttribute attr = attributeForText((JTextField)lastSelection);
			if (attr != null) {
				SimpleOperations.unsetAttribute(entity, attr);
			}
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
				+"} - EntityInstance - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
			result += "Model:\t"+owning_model.getText()+"\n";
			result += "Definition:\t"+entity_definition.getText()+"\n";
			result += "Instance:\t"+instance.getText()+"\n";
			EEntity_definition ed = null;
			for (int i = 0; i < attributs.size(); i++) {
				EAttribute attribute = (EAttribute)attributs.elementAt(i);
				JLabel label = (JLabel)types.elementAt(i);
				String value = "";
				if (((JTextField)unsets_v.get(i)).getText().equals("$")) {
					value = "$";
				} else {
					JComponent c = (JComponent)textes.elementAt(i);
					if (c instanceof JTextField) {
						value = ((JTextField)textes.elementAt(i)).getText();
					} else if (c instanceof JComboBox) {
						value = String.valueOf(((JComboBox)textes.elementAt(i)).getSelectedIndex());
					}
				}
				if (ed != attribute.getParent_entity(null)) {
					ed = attribute.getParent_entity(null);
					result += "-"+ed.getName(null)+"-\n";
				}
				result += attribute.getName(null)+"\t"+label.getText()+"=\t"+value+"\n";
			}
			result += "-Users-\n";
			for (int i = 0; i < inverses.getModel().getSize(); i++) {
				result += inverses.getModel().getElementAt(i)+"\n";
			}
		} catch (SdaiException ex) { processMessage(ex); }
		return result;
	}

	class ButtonWithMenu extends JPanel {

		JButton buttonMain;
		JButton buttonMenu = new JButton(new ImageIcon(getClass().getResource("images/submenu.gif")));

		JPopupMenu popup = new JPopupMenu();
		final int menuItemCount = 10;
		MouseListener menuListener;

		ButtonWithMenu(String s) {
			setLayout(new BorderLayout());
			buttonMain = new JButton(s);
			add(buttonMain, BorderLayout.CENTER);
			add(buttonMenu, BorderLayout.EAST);
			buttonMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					showPopup();
				}
			});
			buttonMenu.setMargin(new Insets(0, 0, 0, 0));
		}

		public void setEnabled(boolean enable) {
			buttonMain.setEnabled(enable);
			buttonMenu.setEnabled(enable);
		}

		void showPopup() {
			try {
				popup.removeAll();
				JMenuItem menuItem;
				int i = 0;
				Aggregate agg = SdaiSession.getSession().getClipboard();
				SdaiIterator it_agg = agg.createIterator();
				int count = agg.getMemberCount();
				while ((i < menuItemCount) && (i < count)) {
					menuItem = new JMenuItem(agg.getByIndexObject(agg.getMemberCount()-i).toString());
					menuItem.addMouseListener(menuListener);
					menuItem.setFont(new Font(menuItem.getFont().getName(), Font.BOLD, 11));
					popup.add(menuItem);
					i++;
				}
				popup.show(buttonMenu, 0, buttonMenu.getHeight());
			} catch (SdaiException ex) { processMessage(ex); }
		}

		public void addActionListener(ActionListener al) {
			buttonMain.addActionListener(al);
		}

		public void addMenuListener(MouseListener ml) {
			menuListener = ml;
		}
	}

	public void setProperties(Properties props) {
		isLockSplits = Boolean.valueOf(props.getProperty("entityPage.isLockSplits", String.valueOf(isLockSplits))).booleanValue();
		splitLocation = Integer.parseInt(props.getProperty("entityPage.splitLocation", String.valueOf(splitLocation)));
		isDeveloperMode = Boolean.valueOf(props.getProperty("entityPage.isDeveloperMode", String.valueOf(isDeveloperMode))).booleanValue();
	}

	public void getProperties(Properties props) {
		props.setProperty("entityPage.isLockSplits", String.valueOf(isLockSplits));
		props.setProperty("entityPage.splitLocation", String.valueOf(splitLocation));
		props.setProperty("entityPage.isDeveloperMode", String.valueOf(isDeveloperMode));
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			Object value = e.getValue();
			if (value instanceof EEntity) {
				Object s[] = new Object[2];
				s[0] = value;
				if (e.getSource() == entity_definition) {
				   s[1] = null;
				} else {
					s[1] = dataDomainOrigin;
				}
				fireGo(new GoEvent(thisis, s, "EntityInstance"));
			} else if (value instanceof Aggregate) {
				Object s[] = new Object[3];
				s[0] = entity;
				s[1] = value;
				s[2] = dataDomainOrigin;
				fireGo(new GoEvent(thisis, s, "Aggregate"));
			} else if (value instanceof SdaiModel) {
				fireGo(new GoEvent(thisis, value, "Model"));
			}
		}
	};

	public ASdaiModel getDataDomain() {
		return dataDomain;
	}

	private int getMaxSizeSplit() {
	   int max = Short.MAX_VALUE;
		for (int i = 0; i < splits.size(); i++) {
		   JSplitPane split = (JSplitPane)splits.get(i);
			int c = (int)(split.getWidth() - split.getLeftComponent().getPreferredSize().getWidth());
		   if (c < max) {
				max = c;
			}
		}
		return max;
	}
}
