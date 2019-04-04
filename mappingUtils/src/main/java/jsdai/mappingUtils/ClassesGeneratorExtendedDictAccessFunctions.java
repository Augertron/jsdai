package jsdai.mappingUtils;

import java.util.Hashtable;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.lang.AEntity;
import jsdai.lang.AEntityExtent;
import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.ASdaiRepository;
import jsdai.lang.EEntity;
import jsdai.lang.EntityExtent;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.util.SimpleOperations;

abstract class ClassesGeneratorExtendedDictAccessFunctions {
  /**
   * This method accesses system repository and moves its schemas and entity definitions
   * from schemas to hashtable. It is used later to determine where given entity definition
   * name belongs very fast.
   * Important note: both schema names and entity definition names are stored in upper case
   * only, so when using built hashtable be attentive.
   */
  public static void buildSchemaContentsTable(Hashtable systemRepository, String mainSchemaName) throws SdaiException, Exception {

    systemRepository.clear();
    SdaiRepository systRep = null;
    ASdaiRepository agRepositories = SdaiSession.getSession().getKnownServers();
    SdaiIterator repIt = agRepositories.createIterator();
    while (repIt.next()) {
      SdaiRepository rep = (SdaiRepository) agRepositories.getCurrentMemberObject(repIt);
      if (rep.getName().equalsIgnoreCase("ExpressCompilerRepo")) {
        systRep = rep;
        break;
      }

    }
    if (systRep == null) {
      throw new Exception("ExpressCompilerRepo was not found!");
    }

    ASdaiModel allSysModels = null;
    if (!systRep.isActive()) {
      systRep.openRepository();
    }
    ASchemaInstance allSchemaInstances = systRep.getSchemas();
    // find schema that is most important for us:
    SdaiIterator schIt = allSchemaInstances.createIterator();
    while (schIt.next()) {
      SchemaInstance schInstance = (SchemaInstance) allSchemaInstances.getCurrentMemberObject(schIt);
      if (schInstance.getName().equalsIgnoreCase(mainSchemaName)) {
        allSysModels = schInstance.getAssociatedModels();
        break;
      }
    }
    if (allSysModels == null) {
      throw new Exception("No associated models found in main schema!");
    }

    Hashtable onlyAllowedSchemas = new Hashtable();
    // iterate thru all:
    SdaiIterator mIt = allSysModels.createIterator();
    ESchema_definition sch_def = null;
    ESchema_definition schema = null;
    // NOTE: now we are accessing not schema instances, but models, defined in repository.
    // iterate thru all:
    mIt = allSysModels.createIterator();
    EEntity_definition ent_def = null;
    schema = null;
    while (mIt.next()) {
      SdaiModel model = (SdaiModel) allSysModels.getCurrentMemberObject(mIt);
      try {
        SimpleOperations.enshureReadOnlyModel(model);
        String modelName = model.getName();
        if (modelName.endsWith(SdaiSession.DICTIONARY_NAME_SUFIX)) {
          int endIndex = modelName.length() - SdaiSession.DICTIONARY_NAME_SUFIX.length();
          modelName = modelName.substring(0, endIndex);
        }
        modelName = modelName.toUpperCase();

        Hashtable schemaContents = new Hashtable();

        AEntityExtent modelExtents = model.getFolders();
        SdaiIterator eeIt = modelExtents.createIterator();
        while (eeIt.next()) {
          EntityExtent entExtent = (EntityExtent) modelExtents.getCurrentMemberObject(eeIt);
          String defString = entExtent.getDefinitionString();
          if (!defString.equalsIgnoreCase("entity_definition")) {
            continue;
          }
          AEntity entDefInstances = entExtent.getInstances();
          SdaiIterator edIt = entDefInstances.createIterator();
          while (edIt.next()) {
            EEntity smth = (EEntity) entDefInstances.getCurrentMemberObject(edIt);
            if (smth instanceof EEntity_definition) {
              ent_def = (EEntity_definition) smth;
              String entDefName = ent_def.getName(null);
              entDefName = entDefName.toUpperCase();
              schemaContents.put(entDefName, entDefName);
            }
            else {
              throw new Exception("NOT the entity_definition was found!");
            }
          }
          break;
        }
        systemRepository.put(modelName, schemaContents);
      }
      catch (SdaiException e) {
        continue;
      }
    }
  }

  public static ESchema_definition findSchema(SdaiModel model) throws SdaiException {
    jsdai.lang.Aggregate a = model.getEntityExtentInstances(ESchema_definition.class);
    jsdai.lang.SdaiIterator i = a.createIterator();
    while (i.next()) {
      ESchema_definition s = (ESchema_definition) a.getCurrentMemberObject(i);
      return s;
    }
    return null;
  }

  public static EEntity_definition[] getEntitiesOfSchema(SdaiModel dictModel) throws SdaiException, Exception {
    Vector results = new Vector();
    AEntityExtent modelExtents = dictModel.getFolders();
    SdaiIterator eeIt = modelExtents.createIterator();
    while (eeIt.next()) {
      EntityExtent entExtent = (EntityExtent) modelExtents.getCurrentMemberObject(eeIt);
      String defString = entExtent.getDefinitionString();
      if (!defString.equalsIgnoreCase("entity_definition")) {
        continue;
      }
      AEntity entDefInstances = entExtent.getInstances();
      SdaiIterator edIt = entDefInstances.createIterator();
      while (edIt.next()) {
        EEntity smth = (EEntity) entDefInstances.getCurrentMemberObject(edIt);
        if (smth instanceof EEntity_definition) {
          EEntity_definition ent_def = (EEntity_definition) smth;
          //String entDefName = ent_def.getName(null);
          results.add(ent_def);
        }
        else {
          throw new Exception("NOT the entity_definition was found!");
        }
      }
      break;
    }
    EEntity_definition[] retValue = new EEntity_definition[results.size()];
    for (int i = 0; i < retValue.length; i++) {
      retValue[i] = (EEntity_definition) results.get(i);
    }
    return retValue;
  }

}