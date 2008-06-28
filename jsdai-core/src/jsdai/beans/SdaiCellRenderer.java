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

import java.util.*;

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.util.*;
import jsdai.mapping.*;

import java.awt.Component;
import java.awt.Color;

public class SdaiCellRenderer extends JLabel implements ListCellRenderer {
	protected static Border noFocusBorder;

	public SdaiCellRenderer() {
		super();
		noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		setOpaque(true);
		setBorder(noFocusBorder);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		try {
			setText(getText(value));
		}
		catch (SdaiException e) {
//			e.printStackTrace();
		}

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

		return this;
	}

	public String getText(Object value) throws SdaiException {
//SdaiRepository
		if (value instanceof SdaiRepository) {
			return ((SdaiRepository)value).getName();
		}
//SdaiModel
		else if (value instanceof SdaiModel) {
			SdaiModel model = (SdaiModel)value;
/*			String sufix = "";
			switch (model.getMode()) {
				case 0 :
					sufix = " - ";
					break;
				case 1 :
					sufix = "r/o ";
					break;
				case 2 :
					sufix = "r/w ";
					break;
			}*/
			String model_name = model.getName();
			int li = model_name.lastIndexOf("_DICTIONARY_DATA");
			if (li != -1) {
				return model_name.substring(0, li);
			} else {
				return model.getName();
			}
		}
//SchemaInstance
		else if (value instanceof SchemaInstance) {
			return ((SchemaInstance)value).getName();
		}
//EntityExtent
		else if (value instanceof EntityExtent) {
			EntityExtent extent = (EntityExtent)value;
			return extent.getDefinition().getName(null)+" ("+String.valueOf(extent.getInstances().getMemberCount())+")";
		}
//EAttribute_mapping
		else if (value instanceof EAttribute_mapping) {
			EAttribute_mapping am = (EAttribute_mapping)value;
			String s = "";
			if (am.testData_type(null)) {
				ANamed_type ant = am.getData_type(null);
				SdaiIterator it_ant = ant.createIterator();
				boolean first = true;
				while (it_ant.next()) {
					ENamed_type nt = ant.getCurrentMember(it_ant);
					s += nt.getName(null);
					if (first) {
						first = false;
					} else {
						s += ",";
					}
				}
				s += " ";
			}
			s += "(";
			EEntity target = MappingOperations.getTarget(am);
			if (target instanceof EEntity_definition) {
				EEntity_definition ed = (EEntity_definition)target;
				s += ed.getName(null);
			} else if (target instanceof EAttribute) {
				EAttribute attribute = (EAttribute)target;
				s += attribute.getName(null);
			} else if (target instanceof ESimple_type) {
				if (target instanceof ENumber_type) {
					s += "Number";
				} else if (target instanceof EInteger_type) {
					s += "Integer";
				} else if (target instanceof EReal_type) {
					s += "Real";
				} else if (target instanceof EBoolean_type) {
					s += "Boolean";
				} else if (target instanceof ELogical_type) {
					s += "Logical";
				} else if (target instanceof EBinary_type) {
					s += "Binary";
				} else if (target instanceof EString_type) {
					s += "String";
				}
			} else {
				s += ((target == null)?value.toString():target.toString());
			}
			s += ")";
			return s;
		} else if (value instanceof EAttribute_mapping_value) {
			EAttribute_mapping_value attributeMapping = (EAttribute_mapping_value)value;
			EAttribute mappedValueAttribute = attributeMapping.getAttributeDefinition("mapped_value");
			switch(attributeMapping.testAttribute(mappedValueAttribute, null)) {
				case 1:
					return attributeMapping.get_object(mappedValueAttribute).toString();
				case 2:
					return Integer.toString(attributeMapping.get_int(mappedValueAttribute));
				case 3:
					return Double.toString(attributeMapping.get_double(mappedValueAttribute));
				case 4:
					return attributeMapping.get_boolean(mappedValueAttribute) ? ".TRUE." : ".FALSE.";
			}
			return value.toString();
		}
//EEntity_mapping
		else if (value instanceof EEntity_mapping) {
			EEntity_definition source = ((EEntity_mapping)value).getSource(null);
			return source.getName(null);
		}
//EEntity
		else if (value instanceof EEntity) {
			if (value instanceof EDeclaration) {
				EDeclaration dec = (EDeclaration)value;
				return dec.getDefinition(null).toString();
			} else {
				return ((EEntity)value).toString();
			}
		}
//Aggregate
		else if (value instanceof Aggregate) {
			return "("+SimpleOperations.getAggregateString((Aggregate)value, false, ", ")+")";
		}
//Vector of defined_types
		else if (value instanceof Vector) {
			Vector nodes = (Vector)value;
			String s = "";
			boolean first = true;
			for (int i = 0; i < nodes.size(); i++) {
				Object element = nodes.elementAt(i);
				if (first) {
					first = false;
				} else {
					s += ", ";
				}
//					if (element instanceof EDefined_type) {
				if (element != null) {
					s += ((EDefined_type)element).getName(null);
				} else {
					s += (nodes.size()==1)?"Entity":"";
				}
			}
			return s;
		}
//default
		else {
			return (value == null) ? "" : value.toString();
		}
	}
}
