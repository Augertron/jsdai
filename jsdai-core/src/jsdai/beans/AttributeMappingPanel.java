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
import jsdai.mapping.*;

public class AttributeMappingPanel extends JPanel {
	final static int NO_MAPPING = 1;
	final static int ONE_MAPPING = 2;
	final static int MORE_MAPPING = 3;
	final static int INVERSE = 4;

	EEntity instance;
	ASdaiModel dataDomain;
	ASdaiModel mappingDomain;
	int mode = EEntity.NO_RESTRICTIONS;

	EAttribute attribute = null;
	Vector alternatives = null;
	EEntity_mapping mapping = null;
	int mappingsCount = NO_MAPPING;
	boolean noMappingFound;

	GoTextField textValue = null;
	JComboBox comboAlternatives = null;
	JTextField textUnset = null;

	public AttributeMappingPanel(EAttribute attribute, EEntity_mapping mapping, int mod) throws SdaiException {
		this.attribute = attribute;
		this.mapping = mapping;
		this.mode = mod;

		setLayout(new BorderLayout());
		setBorder(null);

		textValue = new GoTextField();
		textValue.setEditable(false);
		textValue.setUnderlying(true);

		noMappingFound = false;
		if (attribute instanceof EInverse_attribute) {
		   mappingsCount = INVERSE;
		} else {
			alternatives = MappingOperations.getAttributeMappingAlternatives(attribute, mapping);
			if (alternatives.size() > 1) {
				mappingsCount = MORE_MAPPING;
			} else if (alternatives.size() < 1) {
				mappingsCount = NO_MAPPING;
				AGeneric_attribute_mapping mappings = new AGeneric_attribute_mapping();
				ASdaiModel mappingDomainWorkaround = new ASdaiModel();
				mappingDomainWorkaround.addByIndex(1, mapping.findEntityInstanceSdaiModel(), null);
				if(CGeneric_attribute_mapping
				.usedinSource(null, attribute, mappingDomainWorkaround, mappings) == 0
				&& attribute instanceof EExplicit_attribute 
				&& !((EExplicit_attribute)attribute).getOptional_flag(null)) {
					noMappingFound = true;
				}
			} else if (alternatives.size() == 1) {
				mappingsCount = ONE_MAPPING;
			}
		}

		switch (mappingsCount) {
		    case NO_MAPPING : {
				textValue.setText(noMappingFound ? "NO MAPPING FOR THIS ATTRIBUTE" : 
					(attribute instanceof EDerived_attribute ? 
					"* (Derived attributes not suported yet.)" : ""));
				textValue.setEnabled(false);
				add(textValue, BorderLayout.CENTER);
				break;
			}
			case ONE_MAPPING : {
				EGeneric_attribute_mapping am = (EGeneric_attribute_mapping)alternatives.elementAt(0);
				textValue.setToolTipText(SimpleOperations.getDomainString(MappingOperations.getTarget(am)));
			   add(textValue, BorderLayout.CENTER);
				break;
			}
			case MORE_MAPPING : {
				JPanel p = new JPanel(new GridLayout(1, 2));
				comboAlternatives = new JComboBox(alternatives);
				comboAlternatives.setRenderer(new SdaiCellRenderer());
				comboAlternatives.setMinimumSize(new Dimension(0, 0));
				comboAlternatives.setPreferredSize(textValue.getPreferredSize());
				comboAlternatives.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Object selected = comboAlternatives.getSelectedItem();
							if(!(selected instanceof EGeneric_attribute_mapping)) return;
							EGeneric_attribute_mapping am = (EGeneric_attribute_mapping)selected;
							textValue.setToolTipText(SimpleOperations.getDomainString(MappingOperations.getTarget(am)));
							if (instance.testMappedAttribute(am, dataDomain, mappingDomain, mode)) {
								Object value = MappingOperations.getMappedAttributeObject(instance, am, dataDomain, mappingDomain, mode);
								if (am instanceof EAttribute_mapping) {
									EAttribute_mapping m = (EAttribute_mapping)am;
									if (m.testDomain(null) 
									&& m.getDomain(null) instanceof EEntity_mapping) {
										Object s[] = new Object[2];
										s[0] = value;
										s[1] = m.getDomain(null);
										textValue.setLink(s);
									} else {
										textValue.setLink(null);
									}
								}
								textValue.setText(MappingOperations.getMappedAttributeString(value));
								textUnset.setText("");
							} else {
								textValue.setText("");
								textUnset.setText("$");
							}
							//resets prefferdsizes
							if (textValue != null) {
								textValue.setPreferredSize(new Dimension(0, (int)textValue.getPreferredSize().getHeight()));
							}
							if (comboAlternatives != null) {
								comboAlternatives.setPreferredSize(new Dimension(0, (int)comboAlternatives.getPreferredSize().getHeight()));
							}
						} catch (SdaiException h) {}
					}
				});
				if(alternatives.get(0) instanceof EAttribute_mapping) {
					p.add(textValue);
				} else {
					comboAlternatives.setEnabled(false);
				}
				comboAlternatives.setSelectedIndex(-1);
				p.add(comboAlternatives);
			    add(p, BorderLayout.CENTER);
				break;
			}
		   case INVERSE : {
			   add(textValue, BorderLayout.CENTER);
				break;
		   }
		}

		JPanel p2 = new JPanel(new BorderLayout());
		JPanel pc = new JPanel(new GridLayout(1, 2));
		p2.add(pc, BorderLayout.WEST);
		textUnset = new JTextField("$");
		textUnset.setPreferredSize(textUnset.getPreferredSize());
		textUnset.setBackground(getBackground());
		textUnset.setBorder(null);
		textUnset.setEditable(false);
		p2.add(textUnset, BorderLayout.CENTER);
		add(p2, BorderLayout.EAST);
	}

	public void setInstance(EEntity instance, ASdaiModel dataDomain, ASdaiModel mappingDomain) throws SdaiException {
		this.instance = instance;
		this.dataDomain = dataDomain;
		this.mappingDomain = mappingDomain;

		switch (mappingsCount) {
		    case NO_MAPPING :
				break;
			case ONE_MAPPING : {
			   EGeneric_attribute_mapping am = (EGeneric_attribute_mapping)alternatives.get(0);

				if (MappingOperationsThreadManager.testMappedAttribute(instance, am, dataDomain, mappingDomain, mode)) {
					Object value = MappingOperations.getMappedAttributeObject(instance, am, dataDomain, mappingDomain, mode);
					textValue.setLink(null);
					if (am instanceof EAttribute_mapping) {
						EAttribute_mapping m = (EAttribute_mapping)am;
						if (m.testDomain(null) && m.getDomain(null) instanceof EEntity_mapping) {
							Object s[] = new Object[2];
							s[0] = value;
							s[1] = m.getDomain(null);
							textValue.setLink(s);
						}
					}
					textValue.setText(MappingOperations.getMappedAttributeString(value));
					textUnset.setText("");
				} else {
					textValue.setText("");
					textUnset.setText("$");
				}
				break;
			}
			case MORE_MAPPING : {
				boolean setted = false;
				int i = 0;
				while (!setted && (i < alternatives.size())) {
					Object alternative = alternatives.get(i);
					if(alternative instanceof EGeneric_attribute_mapping) {
						EGeneric_attribute_mapping gam = (EGeneric_attribute_mapping)alternative;
						if (instance.testMappedAttribute(gam, dataDomain, mappingDomain, mode)) {
							setted = true;
							comboAlternatives.setSelectedItem(gam);
						}
					}
					i++;
				}
				if(!setted)	{
					comboAlternatives.setSelectedIndex(-1);
					comboAlternatives.setEnabled(false);
				} else if(alternatives.get(0) instanceof EAttribute_mapping) {
					comboAlternatives.setEnabled(true);
				}
				break;
			}
		   case INVERSE : {
				EInverse_attribute ia = (EInverse_attribute)attribute;
				AAttribute_mapping aam = new AAttribute_mapping();
				CAttribute_mapping.usedinSource(null, ia.getInverted_attr(null), mappingDomain, aam);
//				AEntity inverses = instance.findMappedUsers(mapping, aam, dataDomain, mappingDomain, new AAttribute_mapping(), mode);
				AAttribute_mapping result2 = new AAttribute_mapping();

//changed with Alfas method
//				AEntity inverses = MappingOperations.findMappingUsedin(instance, mapping, aam, dataDomain, mappingDomain, mode, result2);
				AEntity inverses = MappingOperationsThreadManager.findMappedUsers(instance, mapping, aam, dataDomain, mappingDomain, result2, mode);

				Object s[] = new Object[2];
				s[0] = inverses;
				s[1] = ((aam.getMemberCount() > 0)?aam.getByIndex(1).getParent_entity(null):null);
				textValue.setLink(s);
				textValue.setText(String.valueOf(inverses));
				textUnset.setText("");//(result2.getMemberCount() > 0)?"":
				break;
		   }
		}

		//resets prefferdsizes
		if (textValue != null) {
			textValue.setPreferredSize(new Dimension(0, (int)textValue.getPreferredSize().getHeight()));
		}
		if (comboAlternatives != null) {
			comboAlternatives.setPreferredSize(new Dimension(0, (int)comboAlternatives.getPreferredSize().getHeight()));
		}
	}

	public void setEditable(boolean editable) {
	    if (textValue != null) textValue.setEditable(editable);
	}

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

	ActionListener pliusListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				Aggregate agg = SdaiSession.getSession().getClipboard();
				if (agg.getMemberCount() > 0) {
					EEntity value = (EEntity)agg.getByIndexObject(agg.getMemberCount());
					if (textValue != null) {
						textValue.setEEntity(value);
					}
				}
			} catch (SdaiException ex) { ; }
		}
	};

	protected class Attribute_and_value_structure {
		public Attribute_and_value_structure(EAttribute attribute, Object value){
			this.attribute = attribute;
			this.value = value;
		}

		public EAttribute attribute = null;
		public Object value = null;
	}
}
