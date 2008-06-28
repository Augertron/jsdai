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
import jsdai.util.*;
import jsdai.mapping.*;

public class AggregateMappingPage extends SdaiPanel {
	ASdaiModel dataDomain;
	ASdaiModel mappingDomain;

	JTextField tMapping = new JTextField();
	AggregateListModel lInstancesModel = new AggregateListModel();
	AggregateListModel lAlternativesModel = new AggregateListModel();
	GoList lInstances = new GoList(lInstancesModel);
	GoList lAlternatives = new GoList(lAlternativesModel);
	int mode = EEntity.NO_RESTRICTIONS;

	EEntity_mapping mapping;
	AEntity instances;

	public AggregateMappingPage() {
		setLayout(new BorderLayout());

		JPanel pMapping = new JPanel(new BorderLayout());
		pMapping.add(new JLabel("Processed mapping"), BorderLayout.WEST);
/*		tMapping.addFocusListener(focusListener);
		tMapping.addMouseListener(mouseListener);
		tMapping.addKeyListener(keyListener);*/
		tMapping.setBackground(this.getBackground());
		pMapping.add(tMapping, BorderLayout.CENTER);
		add(pMapping, BorderLayout.NORTH);

		JPanel center = new JPanel(new BorderLayout());
		lInstances.setFixedCellHeight(17);
		lInstances.setFixedCellWidth(170);
//		lInstances.addListSelectionListener(listListener);
//		lInstances.addFocusListener(focusListener);
//		lInstances.addMouseListener(mouseListener);
//		lInstances.addKeyListener(findKeyListener);
//		lInstances.addKeyListener(keyListener);
		lInstances.addGoListener(goListener);
		lInstances.setUnderlying(true);
		lInstances.setBackground(this.getBackground());
		lInstances.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				try {
					JList list = (JList)e.getSource();
					EEntity instance = (EEntity)list.getSelectedValue();
				   AEntity_mapping aem = new AEntity_mapping();
				   instance.findEntityMappings(dataDomain, mappingDomain, aem, mode);
					lAlternativesModel.setAggregate(aem);
					lAlternativesModel.fireContentsChanged();
				} catch (SdaiException ex) { processMessage(ex); }
			}
		});
		center.add(new JScrollPane(lInstances), BorderLayout.CENTER);
		center.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Instances"));
		add(center, BorderLayout.CENTER);

//		JPanel south = new JPanel(new BorderLayout());
		lAlternatives.setFixedCellHeight(17);
		lAlternatives.setFixedCellWidth(170);
//		lAlternatives.addListSelectionListener(listListener);
//		lAlternatives.addFocusListener(focusListener);
//		lAlternatives.addMouseListener(mouseListener);
//		lAlternatives.addKeyListener(keyListener);
		lAlternatives.addGoListener(goListener);
		lAlternatives.setUnderlying(true);
		lAlternatives.setCellRenderer(new SdaiCellRenderer());
		lAlternatives.setBackground(this.getBackground());
//		south.add(new JScrollPane(lAlternatives), BorderLayout.CENTER);
//		south.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Alternatives"));
//		add(south, BorderLayout.SOUTH);
	}

	public AggregateMappingPage(EEntity_mapping map, AEntity insts, ASdaiModel domain) throws SdaiException {
		this();
		setMappingAndInstancesAndDomain(map, insts, domain);
	}

	public void setMappingAndInstancesAndDomain(EEntity_mapping map, AEntity insts, ASdaiModel domain) throws SdaiException {
		dataDomain = domain;
		SdaiModel model = dataDomain.getByIndex(1);
		mappingDomain = MappingOperations.getMappingDomain(SdaiSession.getSession().getMappings(model.getUnderlyingSchema()));
		mapping = map;
		instances = insts;
		refreshData();
	}

	public void refreshData() {
		try {
			tMapping.setText((mapping != null)?mapping.getSource(null).getName(null):"none");
			lInstancesModel.setAggregate(instances);
			lInstancesModel.fireContentsChanged();
//			paintAll(getGraphics());
		} catch (SdaiException ex) { processMessage(ex); }
	}

	public AEntity getInstances() {
		return instances;
	}

	public EEntity_mapping getMapping() {
		return mapping;
	}

	public String getTreeLeave() throws SdaiException {
		return "MappingAggregate";
	}

	public int getMode() throws SdaiException {
		return 0;
	}

	public void getSelected(Vector result) throws SdaiException {
		if ((lastSelection == lInstances) || (lastSelection == lAlternatives)) {
			if (!lInstances.isSelectionEmpty() && (!lAlternatives.isSelectionEmpty() || (mapping != null))) {
				result.add(lInstances.getSelectedValue());
				if (!lAlternatives.isSelectionEmpty()) {
					mapping = (EEntity_mapping)lAlternatives.getSelectedValue();
				}
			}
		} else if (lastSelection == tMapping) {
			result.add(mapping);
		}
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		return true;
	}

	public void sdaiEdit() throws SdaiException {
	}

	public void sdaiAccept() throws SdaiException {
	}

	public void sdaiCancel() throws SdaiException {
	}

	public void sdaiNew() throws SdaiException {
	}

	public void sdaiDestroy() throws SdaiException {
	}

	public String copyContentsAsText() {
		String result = "";
		try {
			GregorianCalendar cal = new GregorianCalendar();
			Date date = cal.getTime();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
			result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
				+"} - EntityExtent - "+dateFormatter.format(date)+"\n";
			result += "Location: "+getTreeLeave();
		} catch (SdaiException ex) { processMessage(ex); }
		result += "Processed mapping:\t"+tMapping.getText()+"\n";
		result += "-Instances-"+"\n";
		for (int i = 0; i < lInstances.getModel().getSize(); i++) {
			result += lInstances.getModel().getElementAt(i)+"\n";
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
				if (value instanceof EEntity_mapping) {
					Object s[] = new Object[3];
					s[0] = lInstances.getSelectedValue();
					s[1] = value;
					s[2] = dataDomain;
					fireGo(new GoEvent(thisis, s, "InstanceMapping"));
				} else if (value instanceof EEntity) {
					Object s[] = new Object[3];
					s[0] = value;
					AEntity_mapping aem = new AEntity_mapping();
					MappingOperations.getMostSpecificMappings((EEntity)value, ((EEntity_mapping)mapping).getSource(null), dataDomain, mappingDomain, aem);
					s[1] = (aem.getMemberCount() > 0)?aem.getByIndex(1):null;
					s[2] = dataDomain;
					fireGo(new GoEvent(thisis, s, "InstanceMapping"));
				} else if (value instanceof Aggregate) {
					Object s[] = new Object[3];
					s[0] = value;
					s[1] = mapping;
					s[2] = dataDomain;
					fireGo(new GoEvent(thisis, s, "AggregateMapping"));
				} else {
Debug.println("Not supported type: "+value.getClass().getName()+". Value = "+value);
				}
		   } catch (SdaiException ex) { processMessage(ex); }
		}
	};

	public ASdaiModel getDataDomain() {
		return dataDomain;
	}
}