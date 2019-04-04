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

package jsdai.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import jsdai.SExtended_dictionary_schema.ADefined_type;
import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.CEntity_definition;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SMapping_schema.AConstraint_select;
import jsdai.SMapping_schema.AEntity_mapping;
import jsdai.SMapping_schema.AGeneric_attribute_mapping;
import jsdai.SMapping_schema.AUof_mapping;
import jsdai.SMapping_schema.CEntity_mapping;
import jsdai.SMapping_schema.CGeneric_attribute_mapping;
import jsdai.SMapping_schema.CUof_mapping;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAnd_constraint_relationship;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EAttribute_mapping_boolean_value;
import jsdai.SMapping_schema.EAttribute_mapping_enumeration_value;
import jsdai.SMapping_schema.EAttribute_mapping_int_value;
import jsdai.SMapping_schema.EAttribute_mapping_logical_value;
import jsdai.SMapping_schema.EAttribute_mapping_real_value;
import jsdai.SMapping_schema.EAttribute_mapping_string_value;
import jsdai.SMapping_schema.EAttribute_mapping_value;
import jsdai.SMapping_schema.EAttribute_value_constraint;
import jsdai.SMapping_schema.EBoolean_constraint;
import jsdai.SMapping_schema.EConstraint_attribute;
import jsdai.SMapping_schema.EConstraint_relationship;
import jsdai.SMapping_schema.EEnd_of_path_constraint;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EExact_entity_constraint;
import jsdai.SMapping_schema.EExact_type_constraint;
import jsdai.SMapping_schema.EGeneric_attribute_mapping;
import jsdai.SMapping_schema.EInstance_constraint;
import jsdai.SMapping_schema.EInstance_equal;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.EIntersection_constraint;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.ENegation_constraint;
import jsdai.SMapping_schema.ENon_optional_constraint;
import jsdai.SMapping_schema.EOr_constraint_relationship;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.SMapping_schema.EType_constraint;
import jsdai.beans.AggregateListModel;
import jsdai.beans.GoEvent;
import jsdai.beans.GoListener;
import jsdai.beans.SdaiPanel;
import jsdai.edit.MapEditEntMappingsPanel.AttributeInfo;
import jsdai.edit.MapEditEntMappingsPanel.AttributeMappingElement.PathInfo;
import jsdai.edit.MapEditEntMappingsPanel.ConstraintElement;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

/**
 * @author Vaidas Nargelas
 */
public class MapEditEntMappingsPanel extends SdaiPanel {

  private static final long serialVersionUID = 1L;
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel bar;
  private javax.swing.JComboBox superClassField;
  private javax.swing.JLabel legendBlueLabel;
  private jsdai.beans.GoTable mappingTable;
  private javax.swing.JLabel subClassLabel;
  private javax.swing.JScrollPane attributeConstraintScroll;
  private javax.swing.JPanel attributePanel;
  private javax.swing.JLabel aimEntityLabel;
  private javax.swing.JLabel legendBlackLabel;
  private javax.swing.JComboBox subClassField;
  private javax.swing.JComboBox aimEntityField;
  private javax.swing.JPanel mappingPanel;
  private javax.swing.JButton superClassButton;
  private javax.swing.JButton buttonGet;
  private javax.swing.JLabel legendRedLabel;
  private javax.swing.JLabel armEntityLabel;
  private javax.swing.JLabel attributeLabel;
  private javax.swing.JButton subClassButton;
  private javax.swing.JPanel legendPanel;
  private jsdai.beans.GoTextField armEntityField;
  private javax.swing.JComboBox attributeField;
  private javax.swing.JPanel attributeSelection;
  private javax.swing.JSplitPane split;
  private javax.swing.JScrollPane mappingScroll;
  private javax.swing.JPanel north;
  private javax.swing.JLabel uofLabel;
  private javax.swing.JButton delete;
  private javax.swing.JComboBox uofField;
  private jsdai.beans.GoTable attributeConstraintTable;
  private javax.swing.JLabel legendLabel;
  private javax.swing.JLabel superClassLabel;
  private javax.swing.JButton create;
  // End of variables declaration//GEN-END:variables

  private MapEditEntitiesPanel mapEditEntitiesPanel;

  /**
   * Holds value of property entityInfo.
   */
  private MapEditEntityInfo entityInfo;

  private ASdaiModel mappingDomain;

  private MapEditEntityInfo newEntityInfo;

  private final AimListModel aimListModel = new AimListModel();

  private EEntity_mapping selectedMapping;

  private final AggregateListModel uofListModel = new AggregateListModel();

  private final AggregateListModel superClassListModel = new AggregateListModel();

  private final AggregateListModel subClassListModel = new AggregateListModel();

  private final CommonListModel attributeListModel = new CommonListModel();

  private final ArrayList mappingElements = new ArrayList();

  private final MappingsModel mappingsModel = new MappingsModel(mappingElements, MappingsModel.ENTITY);

  private final ArrayList attributeElements = new ArrayList();

  private final MappingsModel attributeModel = new MappingsModel(attributeElements, MappingsModel.ATTRIBUTE);

  private final EntityGoListener entityGoListener = new EntityGoListener();

  /**
   * Creates new form MapEditEntMappingsBean
   */
  public MapEditEntMappingsPanel() {
    initComponents();
    setTableColumnParameters(mappingTable, MappingsModel.ENTITY);
    setTableColumnParameters(attributeConstraintTable, MappingsModel.ATTRIBUTE);
    entityInfo = null;
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    north = new javax.swing.JPanel();
    armEntityLabel = new javax.swing.JLabel();
    armEntityField = new jsdai.beans.GoTextField();
    aimEntityLabel = new javax.swing.JLabel();
    aimEntityField = new javax.swing.JComboBox();
    uofLabel = new javax.swing.JLabel();
    uofField = new javax.swing.JComboBox();
    superClassLabel = new javax.swing.JLabel();
    superClassField = new javax.swing.JComboBox();
    superClassButton = new javax.swing.JButton();
    subClassLabel = new javax.swing.JLabel();
    subClassField = new javax.swing.JComboBox();
    subClassButton = new javax.swing.JButton();
    split = new javax.swing.JSplitPane();
    mappingPanel = new javax.swing.JPanel();
    mappingScroll = new javax.swing.JScrollPane();
    mappingTable = new jsdai.beans.GoTable();
    attributePanel = new javax.swing.JPanel();
    attributeSelection = new javax.swing.JPanel();
    attributeLabel = new javax.swing.JLabel();
    attributeField = new javax.swing.JComboBox();
    legendLabel = new javax.swing.JLabel();
    legendPanel = new javax.swing.JPanel();
    legendBlueLabel = new javax.swing.JLabel();
    legendRedLabel = new javax.swing.JLabel();
    legendBlackLabel = new javax.swing.JLabel();
    attributeConstraintScroll = new javax.swing.JScrollPane();
    attributeConstraintTable = new jsdai.beans.GoTable();
    bar = new javax.swing.JPanel();
    create = new javax.swing.JButton();
    delete = new javax.swing.JButton();
    buttonGet = new javax.swing.JButton();

    setLayout(new java.awt.BorderLayout());

    north.setLayout(new java.awt.GridBagLayout());

    armEntityLabel.setText("Source");
    armEntityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    north.add(armEntityLabel, gridBagConstraints);

    armEntityField.setEditable(false);
    armEntityField.setUnderlying(true);
    armEntityField.addGoListener(entityGoListener);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    north.add(armEntityField, gridBagConstraints);

    aimEntityLabel.setText("Target");
    aimEntityLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    north.add(aimEntityLabel, gridBagConstraints);

    aimEntityField.setModel(aimListModel);
    aimEntityField.setFont(armEntityField.getFont());
    aimEntityField.setRenderer(new AimCellRenderer());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    north.add(aimEntityField, gridBagConstraints);

    uofLabel.setText("UOFs");
    uofLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    north.add(uofLabel, gridBagConstraints);

    uofField.setModel(uofListModel);
    uofField.setFont(armEntityField.getFont());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    north.add(uofField, gridBagConstraints);

    superClassLabel.setText("Super");
    superClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    north.add(superClassLabel, gridBagConstraints);

    superClassField.setModel(superClassListModel);
    superClassField.setFont(armEntityField.getFont());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    north.add(superClassField, gridBagConstraints);

    superClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jsdai/edit/images/forward.gif")));
    superClassButton.setMargin(new java.awt.Insets(3, 0, 3, 0));
    superClassButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        superSubClassButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 3;
    north.add(superClassButton, gridBagConstraints);

    subClassLabel.setText("Sub");
    subClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    north.add(subClassLabel, gridBagConstraints);

    subClassField.setModel(subClassListModel);
    subClassField.setFont(armEntityField.getFont());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    north.add(subClassField, gridBagConstraints);

    subClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jsdai/edit/images/forward.gif")));
    subClassButton.setMargin(new java.awt.Insets(3, 0, 3, 0));
    subClassButton.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        superSubClassButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 4;
    north.add(subClassButton, gridBagConstraints);

    add(north, java.awt.BorderLayout.NORTH);

    split.setDividerLocation(300);
    split.setDividerSize(5);
    split.setResizeWeight(0.6);
    split.setBorder(null);
    split.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    mappingPanel.setLayout(new java.awt.BorderLayout());

    mappingPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(1, 0, 0, 0), java.awt.Color.gray),
        "Constraints"));
    mappingScroll.setViewportBorder(new javax.swing.border.CompoundBorder());
    mappingTable.setModel(mappingsModel);
    mappingTable.setBackground(mappingPanel.getBackground());
    mappingTable.setReturnCount(1);
    mappingTable.setUnderlying(true);
    mappingTable.addGoListener(entityGoListener);
    mappingScroll.setViewportView(mappingTable);

    mappingPanel.add(mappingScroll, java.awt.BorderLayout.CENTER);

    split.setTopComponent(mappingPanel);

    attributePanel.setLayout(new java.awt.BorderLayout());

    attributeSelection.setLayout(new java.awt.GridBagLayout());

    attributeLabel.setText("Attribute");
    attributeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    attributeSelection.add(attributeLabel, gridBagConstraints);

    attributeField.setModel(attributeListModel);
    attributeField.setRenderer(new AttributeCellRenderer());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    attributeSelection.add(attributeField, gridBagConstraints);

    legendLabel.setText("Legend");
    legendLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    attributeSelection.add(legendLabel, gridBagConstraints);

    legendPanel.setLayout(new javax.swing.BoxLayout(legendPanel, javax.swing.BoxLayout.X_AXIS));

    legendPanel.setBorder(new javax.swing.border.EtchedBorder());
    legendBlueLabel.setText("Constraint in a path");
    legendBlueLabel.setForeground(java.awt.Color.blue);
    legendBlueLabel.setFont(attributeConstraintTable.getFont());
    legendPanel.add(legendBlueLabel);

    legendRedLabel.setText(" Constraint NOT in a path");
    legendRedLabel.setForeground(java.awt.Color.red);
    legendRedLabel.setFont(attributeConstraintTable.getFont());
    legendPanel.add(legendRedLabel);

    legendBlackLabel.setText(" Path NOT in a constraint");
    legendBlackLabel.setForeground(attributeConstraintTable.getForeground());
    legendBlackLabel.setFont(attributeConstraintTable.getFont());
    legendPanel.add(legendBlackLabel);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    attributeSelection.add(legendPanel, gridBagConstraints);

    attributePanel.add(attributeSelection, java.awt.BorderLayout.NORTH);

    attributeConstraintScroll.setViewportBorder(new javax.swing.border.CompoundBorder());
    attributeConstraintTable.setModel(attributeModel);
    attributeConstraintTable.setBackground(mappingPanel.getBackground());
    attributeConstraintTable.setReturnCount(1);
    attributeConstraintTable.setUnderlying(true);
    attributeConstraintTable.addGoListener(entityGoListener);
    attributeConstraintScroll.setViewportView(attributeConstraintTable);

    attributePanel.add(attributeConstraintScroll, java.awt.BorderLayout.CENTER);

    split.setBottomComponent(attributePanel);

    add(split, java.awt.BorderLayout.CENTER);

    bar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

    create.setText("Create");
    create.setEnabled(false);
    bar.add(create);

    delete.setText("Delete");
    delete.setEnabled(false);
    bar.add(delete);

    buttonGet.setText("Get");
    buttonGet.setEnabled(false);
    bar.add(buttonGet);

    add(bar, java.awt.BorderLayout.SOUTH);

  }//GEN-END:initComponents

  private void superSubClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_superSubClassButtonActionPerformed
    try {
      EEntity_definition entityDefinition = (EEntity_definition) (evt.getSource() == superClassButton ? superClassField.getSelectedItem() : subClassField
          .getSelectedItem());
      if (entityDefinition != null) {
        MapEditEntityInfo entityInfo = new MapEditEntityInfo(entityDefinition);
        AEntity_mapping mapings = new AEntity_mapping();
        CEntity_mapping.usedinSource(null, entityDefinition, mappingDomain, mapings);
        SdaiIterator mappingIterator = mapings.createIterator();
        boolean haveMappings = false;
        while (mappingIterator.next()) {
          EEntity_mapping mapping = mapings.getCurrentMember(mappingIterator);
          entityInfo.mappings.add(mapping);
          haveMappings = true;
        }
        if (haveMappings) {
          fireGo(new GoEvent(mapEditEntitiesPanel, entityInfo, SdaiEdit.MAPPING_EDITOR_ENTITY_MAPPINGS));
        }
      }
    }
    catch (SdaiException e) {
      processMessage(e);
    }
  }//GEN-LAST:event_superSubClassButtonActionPerformed

  @Override
  public void pushChainElementValues(List list) throws SdaiException {
    list.add(getMapEditEntitiesPanel());
    list.add(getEntityInfo());
  }

  @Override
  public void popChainElementValues(List list) throws SdaiException {
    setMapEditEntitiesPanel((MapEditEntitiesPanel) list.get(0));
    setEntityInfo((MapEditEntityInfo) list.get(1));
  }

  /**
   * Getter for property mapEditEntitiesPanel.
   *
   * @return Value of property mapEditEntitiesPanel.
   */
  public MapEditEntitiesPanel getMapEditEntitiesPanel() {
    return mapEditEntitiesPanel;
  }

  /**
   * Setter for property mapEditEntitiesPanel.
   *
   * @param mapEditEntitiesPanel New value of property mapEditEntitiesPanel.
   */
  public void setMapEditEntitiesPanel(MapEditEntitiesPanel mapEditEntitiesPanel) {
    this.mapEditEntitiesPanel = mapEditEntitiesPanel;
    mappingDomain = mapEditEntitiesPanel.getMappingDomain();
  }

  /**
   * Getter for property entityInfo.
   *
   * @return Value of property entityInfo.
   */
  public MapEditEntityInfo getEntityInfo() {
    return entityInfo;
  }

  /**
   * Setter for property entityInfo.
   *
   * @param entityInfo New value of property entityInfo.
   */
  public void setEntityInfo(MapEditEntityInfo entityInfo) throws SdaiException {
    newEntityInfo = entityInfo;
    refreshData();
  }

  @Override
  public synchronized void refreshData() {
    try {
      int oldAimEntityFieldIndex = aimEntityField.getSelectedIndex();
      int oldAttributeFieldIndex = attributeField.getSelectedIndex();
      aimListModel.fireRemoved();
      entityInfo = newEntityInfo;
      aimListModel.fireAdded();
      if (aimEntityField.getItemCount() > 0) {
        aimEntityField.setSelectedIndex(oldAimEntityFieldIndex >= 0 && oldAimEntityFieldIndex < aimEntityField.getItemCount() ? oldAimEntityFieldIndex
            : 0);
      }
      if (attributeField.getItemCount() > 0) {
        attributeField.setSelectedIndex(oldAttributeFieldIndex >= 0 && oldAttributeFieldIndex < attributeField.getItemCount() ? oldAttributeFieldIndex
            : 0);
      }
      armEntityField.setText(entityInfo.entity.toString());
      armEntityField.setLink(entityInfo.entity);

      //Fill in sub super class lists
      superClassListModel.setAggregate(entityInfo.entity.getGeneric_supertypes(null));
      if (superClassField.getItemCount() > 0) {
        superClassField.setSelectedIndex(0);
        superClassLabel.setVisible(true);
        superClassField.setVisible(true);
        superClassButton.setVisible(true);
      }
      else {
        superClassLabel.setVisible(false);
        superClassField.setVisible(false);
        superClassButton.setVisible(false);
      }
      AEntity_definition entityDefinitions = new AEntity_definition();
      CEntity_definition.usedinGeneric_supertypes(null, entityInfo.entity, null, entityDefinitions);
      subClassListModel.setAggregate(entityDefinitions);
      if (subClassField.getItemCount() > 0) {
        subClassField.setSelectedIndex(0);
        subClassLabel.setVisible(true);
        subClassField.setVisible(true);
        subClassButton.setVisible(true);
      }
      else {
        subClassLabel.setVisible(false);
        subClassField.setVisible(false);
        subClassButton.setVisible(false);
      }
    }
    catch (SdaiException e) {
      processMessage(e);
    }
  }

  public void setSelectedMapping(EEntity_mapping selectedMapping) {
    try {
      if (this.selectedMapping != selectedMapping) {
        this.selectedMapping = selectedMapping;
        mappingElements.clear();
        ConstraintElement element = EntityMappingElement.create(mappingElements, null, selectedMapping);
        element.parse(mappingElements);
        mappingsModel.fireTableDataChanged();

        // Collect attribute mappings
        int oldAttributesIndex = attributeField.getSelectedIndex();
        attributeListModel.fireRemoved();
        TreeMap<String, AttributeInfo> attributeMap = new TreeMap<String, AttributeInfo>();
        AGeneric_attribute_mapping attributeMapings = new AGeneric_attribute_mapping();
        CGeneric_attribute_mapping.usedinParent_entity(null, selectedMapping, null, attributeMapings);

        SdaiIterator aMappingIterator = attributeMapings.createIterator();
        while (aMappingIterator.next()) {
          EGeneric_attribute_mapping attributeMapping = (EGeneric_attribute_mapping) attributeMapings.getCurrentMemberEntity(aMappingIterator);
          EAttribute attribute = attributeMapping.getSource(null);
          String attributeName = attribute.getName(null);
          AttributeInfo attributeInfo = attributeMap.get(attributeName);
          if (attributeInfo == null) {
            attributeInfo = new AttributeInfo(attribute);
            attributeInfo.mappings.add(attributeMapping);
            attributeMap.put(attributeName, attributeInfo);
          }
          else {
            attributeInfo.mappings.add(attributeMapping);
          }
        }
        attributeListModel.list = new ArrayList(attributeMap.values());
        attributeListModel.fireAdded();
        attributeField.setSelectedIndex(oldAttributesIndex >= 0 && oldAttributesIndex < attributeField.getModel().getSize() ? oldAttributesIndex
            : (attributeField.getModel().getSize() > 0 ? 0 : -1));
        // Make uof list
        if (selectedMapping != null) {
          AUof_mapping uofs = new AUof_mapping();
          CUof_mapping.usedinMappings(null, selectedMapping, null, uofs);
          uofListModel.setAggregate(uofs);
          if (uofField.getItemCount() > 0) {
            uofField.setSelectedIndex(0);
          }
        }
      }
    }
    catch (SdaiException e) {
      processMessage(e);
    }
  }

  public void setSelectedListItem(CommonListModel commonListModel) {
    if (commonListModel == attributeListModel) {
      setSelectedAttribute((AttributeInfo) commonListModel.selectedItem);
    }
  }

  public void setSelectedAttribute(AttributeInfo selectedAttributeInfo) {
    try {
      attributeElements.clear();
      if (selectedAttributeInfo != null) {
        Iterator i = selectedAttributeInfo.mappings.iterator();
        while (i.hasNext()) {
          EGeneric_attribute_mapping mapping = (EGeneric_attribute_mapping) i.next();
          ConstraintElement element = GenericAttributeMappingElement.create(attributeElements, null, mapping);
          element.parse(attributeElements);
        }
      }
      attributeModel.fireTableDataChanged();
    }
    catch (SdaiException e) {
      processMessage(e);
    }
  }

  @Override
  public String getTreeLeave() throws SdaiException {
    if (mapEditEntitiesPanel == null) {
      return "";
    }
    else {
      return mapEditEntitiesPanel.getTreeLeave() + "/" + entityInfo.entity.getName(null);
    }
  }

  class AimListModel extends AbstractListModel implements ComboBoxModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Object getSelectedItem() {
      return selectedMapping;
    }

    @Override
    public void setSelectedItem(Object obj) {
      setSelectedMapping((EEntity_mapping) obj);
    }

    @Override
    public int getSize() {
      return entityInfo != null ? entityInfo.mappings.size() : 0;
    }

    @Override
    public Object getElementAt(int index) {
      return entityInfo.mappings.get(index);
    }

    public void fireRemoved() {
      if (entityInfo != null) {
        fireIntervalRemoved(this, 0, entityInfo.mappings.size() - 1);
      }
    }

    public void fireAdded() {
      if (entityInfo != null) {
        fireIntervalAdded(this, 0, entityInfo.mappings.size() - 1);
      }
    }

  }

  class CommonListModel extends AbstractListModel implements ComboBoxModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ArrayList list = new ArrayList();

    Object selectedItem;

    @Override
    public Object getSelectedItem() {
      return selectedItem;
    }

    @Override
    public void setSelectedItem(Object selectedItem) {
      if (this.selectedItem != selectedItem) {
        this.selectedItem = selectedItem;
        setSelectedListItem(this);
      }
    }

    @Override
    public int getSize() {
      return list.size();
    }

    @Override
    public Object getElementAt(int index) {
      return list.get(index);
    }

    public void fireRemoved() {
      if (list.size() > 0) {
        fireIntervalRemoved(this, 0, list.size() - 1);
      }
    }

    public void fireAdded() {
      if (list.size() > 0) {
        fireIntervalAdded(this, 0, list.size() - 1);
      }
    }

  }

  static class AttributeInfo {
    public EAttribute attribute;

    public LinkedList<EGeneric_attribute_mapping> mappings;

    /**
     * Creates new AttributeInfo
     */
    public AttributeInfo(EAttribute attribute) {
      this.attribute = attribute;
      mappings = new LinkedList<EGeneric_attribute_mapping>();
    }

  }

  static class AimCellRenderer extends BasicComboBoxRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      String valueText = "...";
      if (value instanceof EEntity_mapping) {
        EEntity_mapping entityMapping = (EEntity_mapping) value;
        try {
          valueText = entityMapping.getTarget(null).toString();
        }
        catch (SdaiException e) {
          e.printStackTrace(System.err);
        }
      }

      return super.getListCellRendererComponent(list, valueText, index, isSelected, cellHasFocus);
    }

  }

  static class AttributeCellRenderer extends BasicComboBoxRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      String valueText = "...";
      if (value instanceof AttributeInfo) {
        AttributeInfo attributeInfo = (AttributeInfo) value;
        try {
          valueText = attributeInfo.attribute.getName(null);
        }
        catch (SdaiException e) {
          e.printStackTrace(System.err);
        }
      }

      return super.getListCellRendererComponent(list, valueText, index, isSelected, cellHasFocus);
    }

  }

  class EntityGoListener implements GoListener {

    @Override
    public void goPerformed(GoEvent e) {
      Object s[] = new Object[2];
      s[0] = e.getValue();
      s[1] = mapEditEntitiesPanel.getSchemaInstanceOrModel();
      fireGo(new GoEvent(MapEditEntMappingsPanel.this, s, "EntityInstance"));
    }

  }

  class MappingsModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List list;
    private final int type;
    public static final int ENTITY = 0;
    public static final int ATTRIBUTE = 1;

    public MappingsModel(List list, int type) {
      this.list = list;
      this.type = type;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      try {
        ConstraintElement constraintElement = (ConstraintElement) list.get(rowIndex);
        EEntity entity = constraintElement.getEntity();
        switch (columnIndex) {
          case 0:
            return entity.getPersistentLabel();
          case 1:
            return constraintElement;
          case 2:
            if (type == ATTRIBUTE) { // else go to case 3
              int pathPosition = constraintElement.getPathPosition();
              return pathPosition >= 0 ? (Object) new Integer(pathPosition) : (Object) "";
            }
            //$FALL-THROUGH$
          case 3:
            Class entityClass = constraintElement.getEntity().getClass();
            return entityClass.getName().substring(entityClass.getPackage().getName().length() + 2);
          case -1:
            return entity;
          default:
            return "...";
        }
      }
      catch (SdaiException e) {
        e.printStackTrace(System.err);
      }
      return null;
    }

    @Override
    public int getRowCount() {
      return list != null ? list.size() : 0;
    }

    @Override
    public int getColumnCount() {
      return type == ENTITY ? 3 : 4;
    }

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0:
          return "Instance #";
        case 1:
          return "Mapping";
        case 2:
          if (type == ATTRIBUTE) // else go to case 3
          {
            return "Path";
          }
          //$FALL-THROUGH$
        case 3:
          return "Entity";
        default:
          return "...";
      }
    }

  }

  static public void setTableColumnParameters(JTable table, int type) {
    TableColumnModel comlumnModel = table.getColumnModel();
    comlumnModel.getColumn(0).setPreferredWidth(100);
    comlumnModel.getColumn(1).setPreferredWidth(680);
    comlumnModel.getColumn(2).setPreferredWidth(220);
    if (type == MappingsModel.ENTITY) {
      comlumnModel.getColumn(2).setPreferredWidth(220);
    }
    else {
      comlumnModel.getColumn(2).setPreferredWidth(50);
      comlumnModel.getColumn(3).setPreferredWidth(220);
    }
    comlumnModel.getColumn(1).setCellRenderer(new ConstraintElementCellRender());
  }

  // Constraint parser classes
  static abstract class ConstraintElement {
    EEntity entity;
    ConstraintElement parent;
    int type;
    int pathPosition;
    boolean isNegation;
    static final int NONE = 0;
    static final int CONSTRAINT_AND_PATH = 1;
    static final int CONSTRAINT_ONLY = 2;

    public ConstraintElement init(List<ConstraintElement> container, ConstraintElement parent, EEntity entity) throws SdaiException {
      this.entity = entity;
      this.parent = parent;
      type = NONE;
      pathPosition = -1;
      isNegation = false;
      container.add(this);
      initialized(this);
      return this;
    }

    protected void initialized(ConstraintElement element) throws SdaiException {
      if (parent != null) {
        parent.initialized(element);
      }
    }

    public abstract void parse(List container) throws SdaiException;

    public abstract String getDisplayValue() throws SdaiException;

    public EEntity getEntity() {
      return entity;
    }

    public void setNegation(boolean value) {
      isNegation = value;
    }

    public int getLevel() {
      int level = 0;
      for (ConstraintElement element = parent; element != null; element = element.parent)
        level++;
      return level;
    }

    public void setType(int type) {
      this.type = type;
    }

    public int getType() {
      if (type != NONE) {
        return type;
      }
      else {
        for (ConstraintElement element = parent; element != null; element = element.parent) {
          if (element.type != NONE) {
            return element.type;
          }
        }
        return NONE;
      }
    }

    public void setPathPosition(int pathPosition) {
      this.pathPosition = pathPosition;
    }

    public int getPathPosition() {
      return pathPosition;
    }
  }

  static class EntityMappingElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new EntityMappingElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      EEntity_mapping entityMapping = (EEntity_mapping) entity;
      if (entityMapping.testConstraints(null)) {
        ConstraintElement ce = ConstraintSelectElement.create(container, this, entityMapping.getConstraints(null));
        ce.parse(container);
      }

    }

    @Override
    public String getDisplayValue() throws SdaiException {
      EEntity_mapping entityMapping = (EEntity_mapping) entity;
      String displayValue;
      EEntity_definition source = entityMapping.getSource(null);
      displayValue = "EM: " + source.getName(null) + " -- ";
      if (entityMapping.testTarget(null)) {
        EEntity target = entityMapping.getTarget(null);
        if (target instanceof EEntity_definition) {
          EEntity_definition etarget = (EEntity_definition) target;
          displayValue += "E: " + etarget.getName(null);
        }
        else if (target instanceof EAttribute) {
          EAttribute atarget = (EAttribute) target;
          displayValue += "A: " + atarget.getName(null);
        }
        else {
          throw new SdaiException(SdaiException.SY_ERR, "entity_or_attribute missing");
        }
      }
      return displayValue;
    }
  }

  static abstract class ConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EConstraint_attribute) {
        return ConstraintAttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EConstraint_relationship) {
        return ConstraintRelationshipElement.create(container, parent, entity);
      }
      else if (entity instanceof EInverse_attribute_constraint) {
        return InverseAttributeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EEnd_of_path_constraint) {
        return EndOfPathConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EIntersection_constraint) {
        return IntersectionConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EType_constraint) {
        return TypeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ENegation_constraint) {
        ConstraintElement rv = ConstraintSelectElement.create(container, parent, ((ENegation_constraint) entity).getConstraints(null));
        rv.setNegation(true);
        return rv;
      }

      return null;
    }
  }

  static abstract class ConstraintStartSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EType_constraint) {
        return TypeConstraintElement.create(container, parent, entity);
      }
      else {
        return ConstraintSelectElement.create(container, parent, entity);
      }
    }
  }

  static abstract class ConstraintAttributeElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute_value_constraint) {
        return AttributeValueConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ESelect_constraint) {
        return SelectConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EEntity_constraint) {
        return EntityConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static class TypeConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EExact_type_constraint) {
        return ExactTypeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EType_constraint) {
        return new TypeConstraintElement().init(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      EType_constraint typeConstraint = (EType_constraint) entity;
      if (typeConstraint.testConstraints(null)) {
        ConstraintElement ce = ConstraintSelectElement.create(container, this, typeConstraint.getConstraints(null));
        ce.parse(container);
      }
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "=> " + ((EType_constraint) entity).getDomain(null).getName(null);
    }
  }

  static class ExactTypeConstraintElement extends TypeConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new ExactTypeConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "=> |" + ((EExact_type_constraint) entity).getDomain(null).getName(null) + "|";
    }
  }

  static class IntersectionConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EIntersection_constraint) {
        return new IntersectionConstraintElement().init(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      EIntersection_constraint intersectionConstraint = (EIntersection_constraint) entity;
      if (intersectionConstraint.testSubpaths(null)) {
        AConstraint_select paths = intersectionConstraint.getSubpaths(null);
        SdaiIterator it = paths.createIterator();
        while (it.next()) {
          ConstraintElement ce = ConstraintSelectElement.create(container, this, (EEntity) paths.getCurrentMemberObject(it));
          ce.parse(container);
        }

      }
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "&";
    }
  }

  static abstract class AttributeValueConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EBoolean_constraint) {
        return BooleanConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EInteger_constraint) {
        return IntegerConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ENon_optional_constraint) {
        return NonOptionalConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EString_constraint) {
        return StringConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EReal_constraint) {
        return RealConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EEnumeration_constraint) {
        return EnumerationConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ELogical_constraint) {
        return LogicalConstraintElement.create(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = AttributeValueConstraintSelectElement.create(container, this, ((EAttribute_value_constraint) entity).getAttribute(null));
      ce.parse(container);
    }
  }

  static class BooleanConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new BooleanConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return (((EBoolean_constraint) entity).getConstraint_value(null) ? ".TRUE." : ".FALSE.") + " ==";
    }
  }

  static class IntegerConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new IntegerConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return ((EInteger_constraint) entity).getConstraint_value(null) + " ==";
    }
  }

  static class NonOptionalConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new NonOptionalConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "non_optional_constraint ";
    }
  }

  static class StringConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new StringConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      String suffix = "==";

//             if (isNegation) {
//                 suffix = "!=";
//             }

      return "'" + ((EString_constraint) entity).getConstraint_value(null) + "' " + suffix;
    }
  }

  static class RealConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new RealConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return ((EReal_constraint) entity).getConstraint_value(null) + " ==";
    }
  }

  static class EnumerationConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new EnumerationConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "." + ((EEnumeration_constraint) entity).getConstraint_value(null) + ". ==";
    }
  }

  static class LogicalConstraintElement extends AttributeValueConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new LogicalConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "." + ELogical.toString(((ELogical_constraint) entity).getConstraint_value(null)) + ". ==";
    }
  }

  static class SelectConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new SelectConstraintElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = SelectConstraintSelectElement.create(container, this, ((ESelect_constraint) entity).getAttribute(null));
      ce.parse(container);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      String definedTypeString = "";
      ADefined_type definedTypes = ((ESelect_constraint) entity).getData_type(null);
      SdaiIterator definedTypeIterator = definedTypes.createIterator();
      boolean first = true;
      while (definedTypeIterator.next()) {
        EDefined_type definedType = definedTypes.getCurrentMember(definedTypeIterator);
        definedTypeString += first ? definedType.getName(null) : definedType.getName(null) + ", ";
        first = false;
      }
      return definedTypeString + " =";
    }
  }

  static class AggregateMemberConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AggregateMemberConstraintElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = AggregateMemberConstraintSelectElement.create(container, this, ((EAggregate_member_constraint) entity).getAttribute(null));
      ce.parse(container);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      EAggregate_member_constraint aggregateMemberConstraint = (EAggregate_member_constraint) entity;
      return aggregateMemberConstraint.testMember(null) ? "[" + aggregateMemberConstraint.getMember(null) + "]" : "[i]";
    }
  }

  static class EntityConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EExact_entity_constraint) {
        return ExactEntityConstraintElement.create(container, parent, entity);
      }
      else {
        return new EntityConstraintElement().init(container, parent, entity);
      }
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = AttributeSelectElement.create(container, this, ((EEntity_constraint) entity).getAttribute(null));
      ce.parse(container);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return ((EEntity_constraint) entity).getDomain(null).getName(null) + " <=";
    }
  }

  static class ExactEntityConstraintElement extends EntityConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new ExactEntityConstraintElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "|" + ((EEntity_constraint) entity).getDomain(null).getName(null) + "| <=";
    }
  }

  static abstract class SelectConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static class AttributeElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      EAttribute attribute = (EAttribute) entity;
      return "A: " + attribute.getParent(null).getName(null) + "." + attribute.getName(null);
    }
  }

  static class EndOfPathConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new EndOfPathConstraintElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      EEnd_of_path_constraint constraint = (EEnd_of_path_constraint) entity;
      if (constraint.testConstraints(null)) {
        ConstraintElement ce = ConstraintSelectElement.create(container, this, constraint.getConstraints(null));
        ce.parse(container);
      }
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "EOPC";
    }
  }

  static class EntityDefinitionElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new EntityDefinitionElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "E: " + ((EEntity_definition) entity).getName(null);
    }
  }

  static abstract class AggregateMemberConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EInverse_attribute_constraint) {
        return InverseAttributeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ESelect_constraint) {
        return SelectConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EEntity_constraint) {
        return EntityConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static abstract class AttributeValueConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ESelect_constraint) {
        return SelectConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static class InverseAttributeConstraintElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new InverseAttributeConstraintElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = InverseAttributeConstraintSelectElement.create(container, this,
          ((EInverse_attribute_constraint) entity).getInverted_attribute(null));
      ce.parse(container);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "<-";
    }
  }

  static abstract class InverseAttributeConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EEntity_constraint) {
        return EntityConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ESelect_constraint) {
        return SelectConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static abstract class AttributeSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EInverse_attribute_constraint) {
        return InverseAttributeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static abstract class ConstraintRelationshipElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EPath_constraint) {
        return PathConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EInstance_constraint) {
        return InstanceConstraintElement.create(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = ConstraintSelectElement.create(container, this, ((EConstraint_relationship) entity).getElement2(null));

      ce.parse(container);
    }

  }

  static class PathConstraintElement extends ConstraintRelationshipElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new PathConstraintElement().init(container, parent, entity);
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = PathConstraintSelectElement.create(container, this, ((EPath_constraint) entity).getElement1(null));
      ce.parse(container);
      super.parse(container);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "path_constraint";
    }
  }

  static abstract class InstanceConstraintElement extends ConstraintRelationshipElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAnd_constraint_relationship) {
        return AndConstraintRelationshipElement.create(container, parent, entity);
      }
      else if (entity instanceof EOr_constraint_relationship) {
        return OrConstraintRelationshipElement.create(container, parent, entity);
      }
      else if (entity instanceof EInstance_equal) {
        return InstanceEqualElement.create(container, parent, entity);
//            } else if(entity instanceof EInstances_not_equal) {
//                return InstancesNotEqualElement.create(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      ConstraintElement ce = ConstraintSelectElement.create(container, this, ((EInstance_constraint) entity).getElement1(null));
      ce.parse(container);
      super.parse(container);
    }

  }

  static class AndConstraintRelationshipElement extends InstanceConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AndConstraintRelationshipElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "AND";
    }
  }

  static class OrConstraintRelationshipElement extends InstanceConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new OrConstraintRelationshipElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "OR";
    }
  }

  static class InstanceEqualElement extends InstanceConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new InstanceEqualElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "EQ";
    }
  }

  //    static class InstancesNotEqualElement extends InstanceConstraintElement {
//        static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity)
//		throws SdaiException {
//            return new InstancesNotEqualElement().init(container, parent, entity);
//        }
//
//        public String getDisplayValue() throws SdaiException {
//            return "NEQ";
//        }
//    }
//
  static abstract class PathConstraintSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EInverse_attribute_constraint) {
        return InverseAttributeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EEntity_constraint) {
        return EntityConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      else if (entity instanceof EIntersection_constraint) {
        return IntersectionConstraintElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static abstract class AttributeMappingPathSelectElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EPath_constraint) {
        return PathConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EInverse_attribute_constraint) {
        return InverseAttributeConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof ESelect_constraint) {
        return SelectConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EEntity_constraint) {
        return EntityConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAggregate_member_constraint) {
        return AggregateMemberConstraintElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute) {
        return AttributeElement.create(container, parent, entity);
      }
      return null;
    }
  }

  static abstract class GenericAttributeMappingElement extends ConstraintElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute_mapping) {
        return AttributeMappingElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_value) {
        return AttributeMappingValueElement.create(container, parent, entity);
      }
      return null;
    }

    @Override
    public void parse(List container) throws SdaiException {
      EGeneric_attribute_mapping attributeMapping = (EGeneric_attribute_mapping) entity;
      if (attributeMapping.testConstraints(null)) {
        ConstraintElement ce = ConstraintSelectElement.create(container, this, attributeMapping.getConstraints(null));
        ce.parse(container);
      }

    }

    public String getDataTypeValue() throws SdaiException {
      String dataTypeString = "";
      if (((EGeneric_attribute_mapping) entity).testData_type(null)) {
        ANamed_type dataTypes = ((EGeneric_attribute_mapping) entity).getData_type(null);
        SdaiIterator dataTypeIterator = dataTypes.createIterator();
        boolean first = true;
        while (dataTypeIterator.next()) {
          ENamed_type dataType = dataTypes.getCurrentMember(dataTypeIterator);
          dataTypeString += first ? dataType.getName(null) : dataType.getName(null) + ", ";
          first = false;
        }
      }
      return dataTypeString;
    }
  }

  static class AttributeMappingElement extends GenericAttributeMappingElement {
    static class PathInfo {
      EEntity path;
      public boolean isInConstraint = false;
      public int index;

      public PathInfo(EEntity path, int index) {
        this.path = path;
        this.index = index;
      }
    }

    private final HashMap<String, PathInfo> pathMap = new HashMap<String, PathInfo>();
    private final ArrayList<PathInfo> pathList = new ArrayList<PathInfo>();

    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingElement().init(container, parent, entity);
    }

    @Override
    protected void initialized(ConstraintElement element) throws SdaiException {
      PathInfo pathInfo = pathMap.get(element.entity.getPersistentLabel());
      if (pathInfo != null) {
        pathInfo.isInConstraint = true;
        element.setPathPosition(pathInfo.index);
        element.setType(CONSTRAINT_AND_PATH);
      }
    }

    @Override
    public void parse(List container) throws SdaiException {
      EAttribute_mapping attributeMapping = (EAttribute_mapping) entity;

      if (attributeMapping.testPath(null)) {
        AEntity paths = attributeMapping.getPath(null);
        SdaiIterator pathsIterator = paths.createIterator();
        int index = 1;
        while (pathsIterator.next()) {
          EEntity path = paths.getCurrentMemberEntity(pathsIterator);
          PathInfo pathInfo = new PathInfo(path, index);
          pathMap.put(path.getPersistentLabel(), pathInfo);
          pathList.add(pathInfo);
          index++;
        }
      }

      if (attributeMapping.testConstraints(null)) {
        ConstraintElement ce = ConstraintStartSelectElement.create(container, this, attributeMapping.getConstraints(null));
        if (ce.getType() == NONE) {
          ce.setType(CONSTRAINT_ONLY);
        }
        ce.parse(container);
      }

      pathMap.clear();
      Iterator i = pathList.iterator();
      while (i.hasNext()) {
        PathInfo pathInfo = (PathInfo) i.next();
        if (!pathInfo.isInConstraint) {
          ConstraintElement ce = AttributeMappingPathSelectElement.create(container, this, pathInfo.path);
          ce.setPathPosition(pathInfo.index);
          ce.parse(container);
        }
      }
      pathList.clear();
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      String domainString = "";
      if (((EAttribute_mapping) entity).testDomain(null)) {
        EEntity domain = ((EAttribute_mapping) entity).getDomain(null);
        if (domain instanceof ENamed_type) {
          domainString = " -- " + ((ENamed_type) domain).getName(null);
        }
        else if (domain instanceof EEntity_mapping) {
          EEntity_mapping domainEntMapping = (EEntity_mapping) domain;
          if (domainEntMapping.testTarget(null)) {
            EEntity target = domainEntMapping.getTarget(null);
            if (target instanceof EEntity_definition) {
              EEntity_definition etarget = (EEntity_definition) target;
              domainString += " -- E: " + etarget.getName(null);
            }
            else if (target instanceof EAttribute) {
              EAttribute atarget = (EAttribute) target;
              domainString += " -- A: " + atarget.getName(null);
            }
            else {
              throw new SdaiException(SdaiException.SY_ERR, "entity_or_attribute missing");
            }
          }
        }
        else {
          throw new SdaiException(SdaiException.SY_ERR, "domain missing");
        }
      }
      return "AM: " + getDataTypeValue() + domainString;
    }
  }

  static abstract class AttributeMappingValueElement extends GenericAttributeMappingElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      if (entity instanceof EAttribute_mapping_real_value) {
        return AttributeMappingRealValueElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_enumeration_value) {
        return AttributeMappingEnumerationValueElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_logical_value) {
        return AttributeMappingLogicalValueElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_int_value) {
        return AttributeMappingIntValueElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_boolean_value) {
        return AttributeMappingBooleanValueElement.create(container, parent, entity);
      }
      else if (entity instanceof EAttribute_mapping_string_value) {
        return AttributeMappingStringValueElement.create(container, parent, entity);
      }
      return null;
    }

  }

  static class AttributeMappingRealValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingRealValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "AMV: " + getDataTypeValue() + " == " + ((EAttribute_mapping_real_value) entity).getMapped_value(null);
    }
  }

  static class AttributeMappingEnumerationValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingEnumerationValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "AMV: " + getDataTypeValue() + " == .'" + ((EAttribute_mapping_enumeration_value) entity).getMapped_value(null) + "'.";
    }
  }

  static class AttributeMappingLogicalValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingLogicalValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      String logicalValue = "";
      switch (((EAttribute_mapping_logical_value) entity).getMapped_value(null)) {
        case 1:
          logicalValue = "FALSE";
          break;
        case 2:
          logicalValue = "TRUE";
          break;
        case 3:
          logicalValue = "UNKNOWN";
          break;
      }
      return "AMV: " + getDataTypeValue() + " == ." + logicalValue + ".";
    }
  }

  static class AttributeMappingIntValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingIntValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "AMV: " + getDataTypeValue() + " == " + ((EAttribute_mapping_int_value) entity).getMapped_value(null);
    }
  }

  static class AttributeMappingBooleanValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingBooleanValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      String boolValue = ((EAttribute_mapping_boolean_value) entity).getMapped_value(null) ? "TRUE" : "FALSE";
      return "AMV: " + getDataTypeValue() + " == ." + boolValue + ".";
    }
  }

  static class AttributeMappingStringValueElement extends AttributeMappingValueElement {
    static public ConstraintElement create(List container, ConstraintElement parent, EEntity entity) throws SdaiException {
      return new AttributeMappingStringValueElement().init(container, parent, entity);
    }

    @Override
    public String getDisplayValue() throws SdaiException {
      return "AMV: " + getDataTypeValue() + " == '" + ((EAttribute_mapping_enumeration_value) entity).getMapped_value(null) + "'";
    }
  }

  static class ConstraintElementCellRender extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      try {
        ConstraintElement constraintElement = (ConstraintElement) value;
        String stringValue = constraintElement.getDisplayValue();
        if (constraintElement.isNegation) {
          stringValue = "! " + stringValue;
        }
        super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
        switch (constraintElement.getType()) {
          case ConstraintElement.CONSTRAINT_AND_PATH:
            setForeground(Color.blue);
            break;
          case ConstraintElement.CONSTRAINT_ONLY:
            setForeground(Color.red);
            break;
          default:
            setForeground(Color.black);
            break;
        }
        setBorder(new MatteBorder(new Insets(0, 20 * constraintElement.getLevel(), 0, 0), (Icon) null));
      }
      catch (SdaiException e) {
        e.printStackTrace(System.err);
      }
      return this;
    }

  }

}
