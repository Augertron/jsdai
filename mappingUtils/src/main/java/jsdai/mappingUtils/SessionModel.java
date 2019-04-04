/*
 * SessionModel.java
 *
 * Created on Pirmadienis, 2001, Baland�io 2, 22.51
 */

package jsdai.mappingUtils;

import java.util.Map;
import java.util.TreeMap;

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.ESchema_mapping;
import jsdai.SMapping_schema.EUof_mapping;
import jsdai.lang.AEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

/**
 * @author Vaidas Narg�las
 */
public class SessionModel {

  /**
   * Holds value of property modelName.
   */
  private String modelName;

  /**
   * Holds value of property model.
   */
  private SdaiModel model;

  /**
   * ARM (source) model of DICTIONARY schema.
   */
  private SdaiModel armModel;

  /**
   * ARM (source) schema name.
   */
  private String armName;

  /**
   * Holds value of property sessionRepository.
   */
  private SessionRepository sessionRepository;

  private TreeMap uofs;
  private boolean isNewModel = true;
  private TreeMap armEntities;

  /**
   * Holds value of property selectedUof.
   */
  private String selectedUof = null;

  /**
   * Creates new SessionRepository
   */
  public SessionModel() {
  }

  /**
   * Getter for property modelName.
   *
   * @return Value of property modelName.
   */
  public String getModelName() {
    return modelName;
  }

  /**
   * Setter for property modelName.
   *
   * @param modelName New value of property modelName.
   */
  public void setModelName(String modelName) throws SdaiException {
    if (this.modelName == null || !this.modelName.equals(modelName)) {
      this.modelName = modelName;
      setModel(sessionRepository.getModelByName(modelName));
    }
  }

  /**
   * Getter for property model.
   *
   * @return Value of property model.
   */
  public SdaiModel getModel() {
    return model;
  }

  /**
   * Setter for property repository.
   *
   * @param repository New value of property repository.
   */
  public void setModel(SdaiModel model) throws SdaiException {
    if (this.model != null) {
      closeModel();
    }
    this.model = model;
    openModel();
    isNewModel = true;
    newModel();
  }

  private void openModel() throws SdaiException {
    if (model.getMode() == SdaiModel.NO_ACCESS) {
      if (sessionRepository.getRepository().getLocation() != null) {
        model.startReadWriteAccess();
      }
      else {
        model.startReadOnlyAccess();
      }
    }
  }

  private void closeModel() throws SdaiException {
    if (sessionRepository.getRepository().getLocation() != null) {
      switch (model.getMode()) {
        case SdaiModel.READ_ONLY:
          model.endReadOnlyAccess();
          break;

        case SdaiModel.READ_WRITE:
          model.endReadWriteAccess();
          break;
      }

    }
    model = null;
  }

  /**
   * Setter for property sessionRepository.
   *
   * @return Value of property sessionRepository.
   */
  public void setSessionRepository(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  public void newModel() throws SdaiException {
    // Get source model and this model entity definitions
    ESchema_mapping schemaMapping = (ESchema_mapping) model.getInstances(ESchema_mapping.class).getByIndexEntity(1);
    ESchema_definition armSchemaDefinition = schemaMapping.getSource(null);
    armModel = armSchemaDefinition.findEntityInstanceSdaiModel();
    armName = armSchemaDefinition.getName(null);

    // Get UOFs
    uofs = new TreeMap();
    AEntity uofMappings = model.getInstances(EUof_mapping.class);
    SdaiIterator uofIterator = uofMappings.createIterator();
    while (uofIterator.next()) {
      EUof_mapping uofMapping = (EUof_mapping) uofMappings.getCurrentMemberEntity(uofIterator);
      uofs.put(uofMapping.getName(null), uofMapping);
    }

    selectedUof = null;
    setSelectedUof(" ");

    //System.out.println("uofs is = " + uofs);
    isNewModel = false;
  }

  public Map getUofs() throws SdaiException {
    if (isNewModel) {
      newModel();
    }
    return uofs;
  }

  /**
   * Getter for property selectedUof.
   *
   * @return Value of property selectedUof.
   */
  public String getSelectedUof() {
    return selectedUof;
  }

  /**
   * Setter for property selectedUof.
   *
   * @param selectedUof Selected UOF name. Give " " if you want to select all UOFs
   */
  public void setSelectedUof(String selectedUof) throws SdaiException {
    if (this.selectedUof == null || !this.selectedUof.equals(selectedUof)) {
      armEntities = new TreeMap();
      this.selectedUof = selectedUof;
      AEntity entityMapings = selectedUof.equals(" ") ? model.getInstances(EEntity_mapping.class) : ((EUof_mapping) uofs.get(selectedUof))
          .getMappings(null);

      SdaiIterator eMapppingIterator = entityMapings.createIterator();
      while (eMapppingIterator.next()) {
        EEntity_mapping entityMapping = (EEntity_mapping) entityMapings.getCurrentMemberEntity(eMapppingIterator);
        EEntity_definition armEntity = entityMapping.getSource(null);
        String armEntityName = armEntity.getName(null);
        EntityInfo entityInfo = (EntityInfo) armEntities.get(armEntityName);
        if (entityInfo == null) {
          entityInfo = new EntityInfo(armEntity);
          entityInfo.mappings.add(entityMapping);
          armEntities.put(armEntityName, entityInfo);
        }
        else {
          entityInfo.mappings.add(entityMapping);
        }
      }
    }
  }

  public Map getArmEntities() throws SdaiException {
    return armEntities;
  }

  public EntityInfo getEntityInfo(String entityName) {
    if (armEntities != null) {
      return (EntityInfo) armEntities.get(entityName);
    }
    else {
      return null;
    }
  }

}
