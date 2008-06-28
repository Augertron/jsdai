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
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.util.*;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class SchemaMappingTableModel extends AbstractTableModel {
	static final int FILTER_NONE = 0;
	static final int FILTER_ZERO = 1;
	static final int FILTER_VALIDATE = 2;
	int theFilter = FILTER_ZERO;
	int mode = EEntity.NO_RESTRICTIONS;

	ESchema_mapping theSchema = null;
	ASdaiModel theModels = null;

	String[] names = {"Source (ARM)", "Target (AIM)", "#"}; //"Uof",
	Class[] clases = {String.class, String.class, Integer.class}; //String.class,
	Vector elements[] = new Vector[4];

	public SchemaMappingTableModel() {
//		this.schema = schema;
		elements[0] = new Vector();
		elements[1] = new Vector();
		elements[2] = new Vector();
		elements[3] = new Vector();
//		elements[4] = new Vector();
	}

	public boolean testSchema_mappingAndAModels(ESchema_mapping schema, ASdaiModel models) throws SdaiException {
//		boolean result = schema.equals(theSchema);
/*		SdaiIterator it_models = models.createIterator();
		while (it_models.next()) {
			SdaiModel model = models.get
		}*/
		return (schema != null)?(schema.equals(theSchema) && models.equals(theModels)):false;
	}

	public void setSchema_mappingAndAModels(ESchema_mapping schema, ASdaiModel models, int mode) throws SdaiException {
		Class[] types = new Class[3];
		types[0] = ESchema_mapping.class;
		types[1] = ASdaiModel.class;
		types[2] = Integer.class;

		Object[] params = new Object[3];
		params[0] = schema;
		params[1] = models;
		params[2] = new Integer(mode);

		MappingOperationsThreadManager.invokeMethod(this, "setSchema_mappingAndAModelsSingleThreaded",
													types, params);
	}

	public void setSchema_mappingAndAModelsSingleThreaded(ESchema_mapping schema, ASdaiModel models, Integer modeInt) throws SdaiException {
		int mode = modeInt.intValue();

		this.mode = mode;
		theSchema = schema;
		theModels = models;
		elements[0].removeAllElements();
		elements[1].removeAllElements();
		elements[2].removeAllElements();
		elements[3].removeAllElements();
		
		SdaiSession currSession = SdaiSession.getSession();
		if (currSession.isModified()) {
			jsdai.lang.MethodCallsCacheManager.clear(currSession);
		}
		
//		elements[4].removeAllElements();
		if ((schema == null) || (models == null)) return;
		SdaiIterator it_models = models.createIterator();
		while (it_models.next()) {
			SdaiModel model = models.getCurrentMember(it_models);
			ASdaiModel mappingDomain = MappingOperations.getMappingDomain(currSession.getMappings(model.getUnderlyingSchema()));
			SimpleOperations.enshureReadOnlyModel(model);
			AUof_mapping uofs = schema.getUofs(null);
			SdaiIterator uofs_it = uofs.createIterator();
// 			HashSet mappings = new HashSet();
// 			while (uofs_it.next()) {
// 				EUof_mapping uof = uofs.getCurrentMember(uofs_it);
// 				SimpleOperations.addArrayToVector(mappings, SimpleOperations.aEntityToArray(uof.getMappings(null)));
// 			}
// 			ArrayList amappings = new ArrayList(mappings);
			ArrayList amappings = new ArrayList();
			SimpleOperations.addArrayToVector(amappings,
											  SimpleOperations.aEntityToArray(schema.findEntityInstanceSdaiModel()
																			  .getInstances(EEntity_mapping.class)));
			for (int i = 0; i < amappings.size(); i++) {
				EEntity_mapping mapping = (EEntity_mapping)amappings.get(i);
				Integer l = null;
				try {
				if (theFilter == FILTER_VALIDATE) {
					l = new Integer(MappingOperations.getValidatedMappingInstnces(mapping, model, models, mappingDomain, mode).getMemberCount());
				} else {
						l = new Integer(model.findMappingInstances(mapping, models, mappingDomain, mode).getMemberCount());
					}
				} catch (SdaiException ex) {
                    //System.out.println(ex.toString());
                   ex.printStackTrace();
                }
				if ((theFilter == FILTER_ZERO) && ((l == null) || (l.intValue() == 0))) {
					continue;
				} else {
				    if (elements[3].indexOf(mapping) == -1) {
						elements[0].add(mapping.getSource(null).getName(null));
						EEntity entity = mapping.getTarget(null);
						if (entity instanceof EEntity_definition) {
							elements[1].add(((EEntity_definition)entity).getName(null));
						} else {
							elements[1].add(((EAttribute)entity).getName(null));
						}
//						elements[2].add(uof.getName(null));
						elements[2].add(l);
						elements[3].add(mapping);
					} else {
						int index = elements[3].indexOf(mapping);
						elements[2].set(index, new Integer(l.intValue()+((Integer)elements[2].get(index)).intValue()));
					}
				}
			}
		}
		fireTableDataChanged();
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int columnIndex) {
		return names[columnIndex];
	}

	public Class getColumnClass(int columnIndex) {
		return clases[columnIndex];
	}

	public int getRowCount() {
/*		int result = 0;
		try {
			AUof_mapping uofs = schema.getUofs(null);
			SdaiIterator uofs_it = uofs.createIterator();
			while (uofs_it.next()) {
				EUof_mapping uof = uofs.getCurrentMember(uofs_it);
				result += uof.getMappings(null).getMemberCount();
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		}
		return result;*/
		return elements[0].size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
/*		Object result = null;
		try {
			int i = 0;
			AUof_mapping uofs = schema.getUofs(null);
			SdaiIterator uofs_it = uofs.createIterator();
			while (uofs_it.next()) {
				EUof_mapping uof = uofs.getCurrentMember(uofs_it);
				AEntity_mapping entities = uof.getMappings(null);
				SdaiIterator entities_it = entities.createIterator();
				while (entities_it.next()) {
					EEntity_mapping entity = entities.getCurrentMember(entities_it);
					if (rowIndex == i) {
						switch (columnIndex) {
							case 0 :
								result = uof.getName(null);
								break;
							case 1 :
								result = entity.getSource(null).getName(null);
								break;
							case 2 :
								EEntity target = entity.getTarget(null);
								if (target instanceof EEntity_definition) {
									result = ((EEntity_definition)target).getName(null);
								} else {
									result = ((EAttribute)target).getName(null);
								}
								break;
							default :
								result = entity;
								break;
						}
						i = Integer.MAX_VALUE;
					} else {
						i++;
					}
				}
			}
		} catch (SdaiException e) {
			e.printStackTrace();
		}
		return result;*/
		return elements[(columnIndex == -1)?3:columnIndex].elementAt(rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)   {
		return false;
	}

	public void setFilter(int filter) throws SdaiException {
		theFilter = filter;
		setSchema_mappingAndAModels(theSchema, theModels, mode);
	}
}

