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

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import jsdai.lang.AEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.mapping.AAttribute_mapping;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.EAttribute_mapping;
import jsdai.mapping.EEntity_mapping;

public class MappingInversesTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  String[] names = { "Source definition", "Target instance" };
  Class[] clases = { String.class, String.class };
  Vector elements[] = new Vector[2];

  public MappingInversesTableModel() {
    elements[0] = new Vector();
    elements[1] = new Vector();
  }

  public void setInverses(AEntity instances, AAttribute_mapping mappings) throws SdaiException {
    setInverses(instances, (AEntity) mappings);
  }

  public void setInverses(AEntity instances, AEntity_mapping mappings) throws SdaiException {
    setInverses(instances, (AEntity) mappings);
  }

  private void setInverses(AEntity instances, AEntity mappings) throws SdaiException {
    elements[0].removeAllElements();
    elements[1].removeAllElements();
    /*
     * for (int i = 0; i < instances.getMemberCount(); i++) {
     * elements[0].add(mappings.getByIndex(i));
     * elements[1].add(instances.getByIndexEntity(i));
     * }
     */
    SdaiIterator it1 = instances.createIterator();
    SdaiIterator it2 = mappings.createIterator();
    while (it1.next() && it2.next()) {
      elements[0].add(mappings.getCurrentMemberObject(it2));
      elements[1].add(instances.getCurrentMemberEntity(it1));
    }
    fireTableDataChanged();
  }

  public int getColumnCount() {
    return 2;
  }

  public String getColumnName(int columnIndex) {
    return names[columnIndex];
  }

  public Class getColumnClass(int columnIndex) {
    return clases[columnIndex];
  }

  public int getRowCount() {
    return elements[0].size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    try {
      Object mapping = elements[0].elementAt(rowIndex);
      EEntity_mapping entityMapping = mapping instanceof EAttribute_mapping ? ((EAttribute_mapping) mapping).getParent_entity(null)
          : (EEntity_mapping) mapping;
      switch (columnIndex) {
        case -2:
          return entityMapping;
        case -1:
          return elements[1].elementAt(rowIndex);
        case 0: {
          String mappingName = entityMapping.getSource(null).getName(null);
          if (mapping instanceof EAttribute_mapping) {
            mappingName += "." + ((EAttribute_mapping) mapping).getSource(null).getName(null);
          }
          return mappingName;
        }
        case 1:
          return elements[1].elementAt(rowIndex).toString();
      }
    }
    catch (SdaiException ex) {
      ex.printStackTrace();
    }
    return "Errror";
//		return elements[(columnIndex < 0)?(columnIndex*(-1)-1):columnIndex].elementAt(rowIndex);
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }
}
