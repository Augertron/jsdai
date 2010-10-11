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

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class AttributePanel extends JPanel {
	final static int SIMPLE = 1;
	final static int ENTITY = 2;
	final static int LIMITED = 3;
	final static int AGGREGATE = 4;
	final static int MIXED = 5;
	final static int INVERSE = 11;
	final static int DERIVED = 12;

	final static ComboBoxModel DERIVED_LIMITED_MODEL = new DefaultComboBoxModel(new String[] {"* (Derived)"});

	EEntity instance;
	EAttribute attribute = null;
	EEntity domain = null;
	int type = 0;
	JPanel panelLimited = null;
	CardLayout card = null;

	GoTextField textValue = null;
	JComboBox comboValue = null;
	JComboBox comboNode = null;

	JButton bMinus = new JButton(new ImageIcon(getClass().getResource("images/minus_t.gif")));
	JButton bPlius = new JButton(new ImageIcon(getClass().getResource("images/plius_t.gif")));
	JButton bCreate = new JButton(new ImageIcon(getClass().getResource("images/create_t.gif")));

	private boolean redeclaring;

	/** Holds value of property derived. */
	private boolean derived;


	public AttributePanel(EAttribute attribute) throws SdaiException {
		setLayout(new BorderLayout());
		setBorder(null);

		if (SimpleOperations.testRedeclaring(attribute) && (attribute instanceof EExplicit_attribute)) {
		   this.attribute = SimpleOperations.getRedeclaring(attribute);
		   redeclaring = true;
		} else {
		   this.attribute = attribute;
		   redeclaring = false;
		}
		derived = false;
		domain = SimpleOperations.getAttributeDomain(this.attribute);

		if (this.attribute instanceof EExplicit_attribute) {
			if (SimpleOperations.isMixedSelectInside(SdaiSession.getSession().getSdaiContext(), domain)) {
				type = MIXED;
			} else if ((domain instanceof EEnumeration_type) || (domain instanceof EBoolean_type) || (domain instanceof ELogical_type)) {
				type = LIMITED;
			} else if (domain instanceof EAggregation_type) {
				type = AGGREGATE;
			} else if ((domain instanceof EEntity_definition) || (domain instanceof ESelect_type)) {
				type = ENTITY;
			} else {
				type = SIMPLE;
			}
		} else if (this.attribute instanceof EInverse_attribute) {
			type = INVERSE;
		} else if (this.attribute instanceof EDerived_attribute) {
			type = DERIVED;
			derived = true;
		}

		switch (type) {
			case SIMPLE : {
				textValue = new GoTextField();
				bPlius.setVisible(false);
				bCreate.setVisible(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
			case LIMITED : {
				comboValue = new JComboBox();
				comboValue.setPreferredSize((new JTextField()).getPreferredSize());
				if (domain instanceof EEnumeration_type) {
					EEnumeration_type et = (EEnumeration_type)domain;
					comboValue.setModel(new AggregateListModel(et.getElements(null,
							SdaiSession.getSession().getSdaiContext())));
				} else if (domain instanceof EBoolean_type) {
					String b[] = {"false", "true"};
					comboValue.setModel(new DefaultComboBoxModel(b));
				} else if (domain instanceof ELogical_type) {
					String b[] = {"false", "true", "unknown"};
					comboValue.setModel(new DefaultComboBoxModel(b));
				}
				bPlius.setVisible(false);
				bCreate.setVisible(false);
				add(comboValue, BorderLayout.CENTER);
			    break;
			}
			case AGGREGATE : {
		      textValue = new GoTextField();
				textValue.setUnderlying(true);
				textValue.setEditable(false);
				bCreate.setVisible(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
			case ENTITY : {
				textValue = new GoTextField();
				textValue.setUnderlying(true);
				textValue.setEditable(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
			case MIXED : {
				JPanel panelMixed = new JPanel(new GridLayout(0, 2));

				//text + combo
				card = new CardLayout();
				panelLimited = new JPanel(card);
				textValue = new GoTextField();
				textValue.setUnderlying(true);
				panelLimited.add(textValue, "field");
				comboValue = new JComboBox();
				comboValue.setPreferredSize((new JTextField()).getPreferredSize());
				comboValue.setMaximumSize(new Dimension(Short.MAX_VALUE, (int)comboValue.getPreferredSize().getHeight()));
				panelLimited.add(comboValue, "combo");
				card.show(panelLimited, "field");
				panelMixed.add(panelLimited);

				//nodes
				comboNode = new JComboBox();
				comboNode.setMinimumSize(new Dimension(0, 0));
				comboNode.setPreferredSize((new JTextField()).getPreferredSize());
				comboNode.setRenderer(new SdaiCellRenderer());
				comboNode.addActionListener(limitedTypeListener);
				Vector nodes = new Vector();
				ESelect_type st = SimpleOperations.isSelectInside(domain);
				SimpleOperations.getNodes(st, nodes, new Vector());
				comboNode.setModel(new NodesListModel(nodes));

				panelMixed.add(comboNode);
				bPlius.setVisible(false);
				bCreate.setVisible(false);
				add(panelMixed, BorderLayout.CENTER);
				break;
			}
			case INVERSE : {
				textValue = new GoTextField();
				textValue.setUnderlying(true);
				textValue.setEditable(false);
				bPlius.setVisible(false);
				bMinus.setVisible(false);
				bCreate.setVisible(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
			case DERIVED : {
				textValue = new GoTextField();
				textValue.setText("* (Derived attributes not supported yet.)");
				textValue.setEditable(false);
				bPlius.setVisible(false);
				bMinus.setVisible(false);
				bCreate.setVisible(false);
				textValue.setEnabled(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
		}

		JPanel pc = new JPanel(new GridLayout(1, 2));
	    bPlius.setDisabledIcon(new ImageIcon(getClass().getResource("images/plius.gif")));
		bPlius.setBounds(2, 0, 21, 21);
		bPlius.setPreferredSize(new Dimension (21, 21));
		bPlius.addActionListener(pliusListener);
		pc.add(bPlius);
	    bMinus.setDisabledIcon(new ImageIcon(getClass().getResource("images/minus.gif")));
		bMinus.setBounds(2, 0, 21, 21);
		bMinus.setPreferredSize(new Dimension (21, 21));
		bMinus.addActionListener(minusListener);
		pc.add(bMinus);
	    bCreate.setDisabledIcon(new ImageIcon(getClass().getResource("images/create.gif")));
		bCreate.setBounds(2, 0, 21, 21);
		bCreate.setPreferredSize(new Dimension (21, 21));
		bCreate.addActionListener(createListener);
		pc.add(bCreate);
		add(pc, BorderLayout.EAST);
	}

	public EAttribute getAttribute() {
		return attribute;
	}

	public void setInstance(EEntity instance) throws SdaiException {
		this.instance = instance;
//        SdaiModel owning_model = instance.findEntityInstanceSdaiModel();

//        dataDomain = new ASdaiModel();
//        dataDomain.addByIndex(1, owning_model, null);

		EDefined_type dts[] = new EDefined_type[10];
		int test_res = -1;
		try {
			if (type <= MIXED) {
				test_res = instance.testAttribute(attribute, dts);
			}
		} catch (SdaiException ex) {
			int errorId = ex.getErrorId();
			if (errorId == SdaiException.FN_NAVL || errorId == SdaiException.AT_NVLD) {
				test_res = -1;
			} else {
				throw ex;
			}
		}
		switch (type) {
			case SIMPLE : {
				derived = false;
			    if (test_res > 0) {
				    textValue.setText(SimpleOperations.getAttributeString(instance, attribute));
				} else if (test_res == 0) {
					textValue.setText("");
				} else {
					textValue.setEnabled(false);
					textValue.setText("* (Derived)");
					derived = true;
				}
				break;
			}
			case LIMITED : {
			    if (test_res > 0) {
			    	int selIndex = SimpleOperations.getAttributeInt(instance, attribute)-1;
		    		comboValue.setSelectedIndex(selIndex > 0 && selIndex < comboValue.getModel().getSize() ? selIndex : -1);
	    		} else if (test_res == 0) {
					comboValue.setSelectedIndex(-1);
				} else {
					comboValue.setModel(DERIVED_LIMITED_MODEL);
					comboValue.setSelectedIndex(0);
					derived = true;
				}
			    break;
			}
			case AGGREGATE : {
				derived = false;
			    if (test_res > 0) {
					textValue.setLink(SimpleOperations.getAttributeObject(instance, attribute));
					textValue.setText(SimpleOperations.getAttributeString(instance, attribute));
	    		} else if (test_res == 0) {
					textValue.setLink(null);
					textValue.setText("");
				} else {
					textValue.setLink(null);
					textValue.setEnabled(false);
					textValue.setText("* (Derived)");
					derived = true;
				}
				break;
			}
			case ENTITY : {
				derived = false;
			    if (test_res > 0) {
					textValue.setEEntity((EEntity)SimpleOperations.getAttributeObject(instance, attribute));
	    		} else if (test_res == 0) {
				   textValue.setEEntity(null);
				} else {
					textValue.setEEntity(null);
					textValue.setEnabled(false);
					textValue.setText("* (Derived)");
					derived = true;
				}
				break;
			}
			case MIXED : {
				derived = false;
				textValue.setEEntity(null);
				textValue.setLink(null);
				textValue.setText("");
				comboValue.setSelectedIndex(-1);
			   if (test_res > 0) {
					int k = 0;
					while ((k < dts.length) && (dts[k] != null)) {
						k++;
					}
					EDefined_type ndts[] = new EDefined_type[k+1];
					System.arraycopy(dts, 0, ndts, 0, k+1);
					Vector x = new Vector();
					SimpleOperations.addArrayToVector(x, ndts);
					comboNode.setSelectedItem(x);

					textValue.setEEntity(null);
					textValue.setLink(null);
					if (x.size() < 2) {
						textValue.setEEntity((EEntity)SimpleOperations.getAttributeObject(instance, attribute));
					} else {
						EEntity node = SimpleOperations.getDefined_typeDomain((EDefined_type)x.get(x.size()-2));
						if ((node instanceof EEnumeration_type) || (node instanceof EBoolean_type) || (node instanceof ELogical_type)) {
							comboValue.setSelectedIndex(SimpleOperations.getAttributeInt(instance, attribute)-1);
						} else if (node instanceof EAggregation_type) {
							textValue.setLink(SimpleOperations.getAttributeObject(instance, attribute));
							textValue.setText(SimpleOperations.getAttributeString(instance, attribute));
						} else {
							textValue.setText(SimpleOperations.getAttributeString(instance, attribute));
						}
					}
				} else if (test_res < 0) {
					textValue.setEnabled(false);
					textValue.setText("* (Derived)");
					derived = true;
				}
				break;
			}
			case INVERSE : {
				EInverse_attribute ie = (EInverse_attribute)attribute;
				textValue.setLink(instance.get_inverse(ie, null));
				textValue.setText(instance.get_inverse(ie, null).toString());
				break;
			}
			case DERIVED : {
				EDerived_attribute da = (EDerived_attribute)attribute;
				textValue.setText("* (Derived attributes not supported yet.)");
				break;
			}
		}

		//resets preferred sizes
		if (textValue != null) {
			textValue.setPreferredSize(new Dimension(0, (int)textValue.getPreferredSize().getHeight()));
		}
		if (comboValue != null) {
			comboValue.setPreferredSize(new Dimension(0, (int)comboValue.getPreferredSize().getHeight()));
		}
		if (comboNode != null) {
			comboNode.setPreferredSize(new Dimension(0, (int)comboNode.getPreferredSize().getHeight()));
		}
	}

	public void setEditable(boolean editable) {
	    switch (type) {
			case SIMPLE :
				editable = !redeclaring && !derived && editable;
				textValue.setEditable(editable);
				break;
			case AGGREGATE :
			case ENTITY :
				editable = !derived && editable;
				break;
			case LIMITED :
				editable = !derived && editable;
				comboValue.setEnabled(editable);
				break;
			case MIXED :
				editable = !derived && editable;
				comboNode.setEnabled(editable);
				comboValue.setEnabled(editable);
				textValue.setEditable(editable);
				break;
			case INVERSE :
			case DERIVED :
				editable = false;
		}
		bMinus.setEnabled(editable);
		bPlius.setEnabled(editable);
		bCreate.setEnabled(editable);
	}

	public void setAttributeOnInstance(EEntity instance) throws SdaiException {
		if(!derived && !redeclaring) {
			switch (type) {
				case SIMPLE : {
					SimpleOperations.setAttributeString(instance, attribute, textValue.getText(), null);
					break;
				}
				case ENTITY : {
					if (textValue.getEEntity() != null) {
						SimpleOperations.setAttributeObject(instance, attribute, textValue.getEEntity(), null);
					}
					break;
				}
				case LIMITED : {
					if (comboValue.getSelectedIndex() != -1) {
						SimpleOperations.setAttributeInt(instance, attribute, comboValue.getSelectedIndex()+1, null);
					}
					break;
				}
				case AGGREGATE : {
					//for aggregates no set operations is performed
					break;
				}
				case MIXED : {
					Vector v =(Vector)comboNode.getSelectedItem();
					EDefined_type dts[] = (EDefined_type[])v.toArray(new EDefined_type[v.size()]);
					EEntity nodeDomain = SimpleOperations.getNodesDomain(dts);
					if ((nodeDomain instanceof EEnumeration_type) || (nodeDomain instanceof EBoolean_type) || (nodeDomain instanceof ELogical_type)) {
						if (comboValue.getSelectedIndex() != -1) {
							SimpleOperations.setAttributeInt(instance, attribute, comboValue.getSelectedIndex()+1, dts);
						}
					} else if (nodeDomain instanceof EAggregation_type) {
						//for aggregates no set operations is performed
					} else if ((nodeDomain instanceof EEntity_definition) || (nodeDomain == null)) {
						if (textValue.getEEntity() != null) {
							SimpleOperations.setAttributeObject(instance, attribute, textValue.getEEntity(), dts);
						}
					} else {
						SimpleOperations.setAttributeString(instance, attribute, textValue.getText(), dts);
					}
					break;
				}
				case INVERSE : {
					break;
				}
				case DERIVED : {
					break;
				}
			}
		}
	}

	ActionListener limitedTypeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				JComboBox j = (JComboBox)e.getSource();
				Vector v =(Vector)j.getSelectedItem();
				if (v == null) return;
				EDefined_type dts[] = (EDefined_type[])v.toArray(new EDefined_type[v.size()]);
				EEntity nodeDomain = SimpleOperations.getNodesDomain(dts);
				if (nodeDomain instanceof EEnumeration_type) {
					EEnumeration_type et = (EEnumeration_type)nodeDomain;
					comboValue.setModel(new AggregateListModel(et.getElements(null)));
					card.show(panelLimited, "combo");
				} else if (nodeDomain instanceof EBoolean_type) {
					String b[] = {"false", "true"};
					comboValue.setModel(new DefaultComboBoxModel(b));
					card.show(panelLimited, "combo");
				} else if (nodeDomain instanceof ELogical_type) {
					String b[] = {"false", "true", "unknown"};
					comboValue.setModel(new DefaultComboBoxModel(b));
					card.show(panelLimited, "combo");
				} else if ((nodeDomain instanceof EAggregation_type) || (nodeDomain == null) /*EEntity_definition*/) {
					textValue.setUnderlying(true);
					bPlius.setVisible(true);
					card.show(panelLimited, "field");
				} else {
					card.show(panelLimited, "field");
				}
			} catch (SdaiException h) {
				h.printStackTrace();
			}
		}
	};

	public void addGoListener(GoListener listener) {
		if (textValue != null) {
		    textValue.addGoListener(listener);
		}
	}

	public void removeGoListener(GoListener listener) {
		if (textValue != null) {
		    textValue.removeGoListener(listener);
		}
	}

	/** Getter for property redeclaring.
	 * @return Value of property redeclaring.
	 */
	public boolean isRedeclaring() {
		return redeclaring;
	}

	/** Setter for property redeclaring.
	 * @param redeclaring New value of property redeclaring.
	 */
	public void setRedeclared(boolean redeclaring) {
		this.redeclaring = redeclaring;
	}

	/** Getter for property derived.
	 * @return Value of property derived.
	 */
	public boolean isDerived() {
		return derived;
	}

	ActionListener pliusListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				if (domain instanceof EAggregation_type) {
					Aggregate agg = instance.createAggregate(attribute, null);
					textValue.setLink(agg);
					textValue.setText(agg.toString());
				} else {
					Aggregate agg = SdaiSession.getSession().getClipboard();
					if (agg.getMemberCount() > 0) {
						EEntity value = (EEntity)agg.getByIndexObject(agg.getMemberCount());
						if (SimpleOperations.canSetForThisDomain(SdaiSession.getSession().getSdaiContext(),
																 value.getInstanceType(), domain) && (textValue != null)) {
							textValue.setEEntity(value);
						}
					}
				}
			} catch (SdaiException ex) { ; }
		}
	};

	ActionListener minusListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				SimpleOperations.unsetAttribute(instance, attribute);
				setInstance(instance);
			} catch (SdaiException ex) { ; }
		}
	};

	private static class EEntity_definition_list_item {
		EEntity_definition ed;
		public boolean equals(Object obj) {
			EEntity_definition_list_item rh = (EEntity_definition_list_item) obj;
			return ed.equals(rh.ed);
		}

		public String toString() {
			try {
				return ed.getName(null).toUpperCase();
			} catch (SdaiException ex) {
				ex.printStackTrace();
			}

			return null;
		}

		public EEntity_definition getDefinition() {
			return ed;
		}
	}

	private static class EEntity_definition_list_item_comparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}

	/*
	 * Method is used by this class and AggregateBean
	 */
	static Collection/*<EEntity_definition>*/ getDomainEntityDefinitions(SdaiContext sdaiContext, EEntity domain,
			Collection/*<EEntity_definition>*/ entDefinitions) throws SdaiException {
		if (domain instanceof EDefined_type) {
			domain = ((EDefined_type)domain).getDomain(null);
		}
		if(domain instanceof ESelect_type) {
			ESelect_type selectDomain = (ESelect_type)domain;
			ANamed_type sels =
				sdaiContext != null ? selectDomain.getSelections(null, sdaiContext) : selectDomain.getSelections(null);
			SdaiIterator it_sels = sels.createIterator();
			while (it_sels.next()) {
				ENamed_type nt = sels.getCurrentMember(it_sels);
				entDefinitions = getDomainEntityDefinitions(sdaiContext, nt, entDefinitions);
			}
		} else if (domain instanceof EEntity_definition) {
			if(entDefinitions == null) {
				entDefinitions = new HashSet();
			}
			entDefinitions.add(domain);
		}
		return entDefinitions;
	}

	/*
	 * Method is used by this class and AggregateBean
	 */
    static Collection getEntity_definition_list(Collection/*<EEntity_definition>*/ entDefinitions, ASdaiModel dataDomain,
    		Collection/*<EEntity_definition_list_item>*/ edList) throws SdaiException {
    	if(entDefinitions != null) {
    		for (Iterator i = entDefinitions.iterator(); i.hasNext();) {
				EEntity_definition entDefinition = (EEntity_definition) i.next();
				if(entDefinition.getInstantiable(null)) {
					if(edList == null) {
						edList = new TreeSet(new EEntity_definition_list_item_comparator());
					}
					EEntity_definition_list_item newItem = new EEntity_definition_list_item();
					newItem.ed = entDefinition;
					edList.add(newItem);
				}
				AEntity_definition result = new AEntity_definition();
		        int childCount = CEntity_definition.usedinSupertypes(null, entDefinition, dataDomain, result);

		        for (int j = 1; j <= childCount; j++) {
		            EEntity_definition def = result.getByIndex(j);
					edList = getEntity_definition_list(Collections.singletonList(def), dataDomain, edList);
		        }
			}
    	}
        return edList;
    }

	/*
	 * Method is used by this class and AggregateBean
	 */
    static EEntity_definition getEntityDefinitionDialog(Component parent, java.util.Vector possibleTypes) {
		Component topParent = WindowCenterer.getTopParent(parent);
		Frame frame = topParent instanceof Frame ? (Frame)topParent : null;
    	GetEntityDefinitionDialog selectEntityDefDlg = new GetEntityDefinitionDialog(frame, true, possibleTypes);
		WindowCenterer.showCentered(topParent, selectEntityDefDlg);

		int selIndex = selectEntityDefDlg.getSelIndex();
		if (selIndex >= 0) {
			return ((EEntity_definition_list_item) possibleTypes.get(selIndex)).getDefinition();
		}

		return null;
	}

	/*
	 * Method is used by this class and AggregateBean
	 */
    static ASdaiModel getDataDomain(SdaiModel owning_model) throws SdaiException {
        ASdaiModel rv = new ASdaiModel();

        SdaiSession session = SdaiSession.getSession();
        ESchema_definition schemaDef = owning_model.getUnderlyingSchema();
        SdaiRepository systemRepo = session.getSystemRepository();
        String schemaDefString = schemaDef.getName(null);
        schemaDefString = schemaDefString.toLowerCase();
        SchemaInstance schemaInstance = systemRepo.findSchemaInstance(schemaDefString);
        rv = schemaInstance.getAssociatedModels();
        return rv;
    }

	ActionListener createListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
                if (attribute instanceof EExplicit_attribute) {
                    EEntity entity = ((EExplicit_attribute) attribute).getDomain(null);
                	SdaiModel owning_model = instance.findEntityInstanceSdaiModel();
                	ASdaiModel dataDomain = getDataDomain(owning_model);
                    Collection entDefinitions =
                    	getDomainEntityDefinitions(SdaiSession.getSession().getSdaiContext(), entity, null);
            		Collection edList = getEntity_definition_list(entDefinitions, dataDomain, null);
            		if(edList != null) {
	                    Vector availableTypes = new Vector(edList);
	                    EEntity_definition selectedEntity = getEntityDefinitionDialog(AttributePanel.this, availableTypes);
						if (selectedEntity != null) {
							EEntity instance = owning_model.createEntityInstance(selectedEntity);

							if (SimpleOperations.canSetForThisDomain(SdaiSession.getSession().getSdaiContext(),
																	 instance.getInstanceType(), domain) && (textValue != null)) {
								textValue.setEEntity(instance);
							}
						}
            		}
                }
			} catch (SdaiException ex) {
                ex.printStackTrace();
            }
		}
	};
}
