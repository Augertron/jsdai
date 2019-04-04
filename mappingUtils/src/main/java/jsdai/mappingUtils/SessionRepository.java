/*
 * SessionRepository.java
 *
 * Created on Pirmadienis, 2001, Baland�io 2, 22.51
 */

package jsdai.mappingUtils;

import java.util.HashMap;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

/**
 * @author Vaidas Narg�las
 */
public class SessionRepository {

  /**
   * Holds value of property repositoryName.
   */
  private String repositoryName;

  /**
   * Holds value of property repository.
   */
  private SdaiRepository repository;

  /**
   * Holds value of property sessionData.
   */
  private EditorData sessionData;

  private HashMap modelMap;

  /**
   * Creates new SessionRepository
   */
  public SessionRepository() {
  }

  /**
   * Getter for property repositoryName.
   *
   * @return Value of property repositoryName.
   */
  public String getRepositoryName() {
    return repositoryName;
  }

  /**
   * Setter for property repositoryName.
   *
   * @param repositoryName New value of property repositoryName.
   */
  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  /**
   * Getter for property repository.
   *
   * @return Value of property repository.
   */
  public SdaiRepository getRepository() {
    return repository;
  }

  /**
   * Setter for property repository.
   *
   * @param repository New value of property repository.
   */
  public void setRepository(SdaiRepository repository) throws SdaiException {
    if (this.repository != repository) {
      if (this.repository != null) {
        closeRepository();
      }
      this.repository = repository;
      openRepository();
    }
  }

  private void openRepository() throws SdaiException {
    if (repository.getId() != null) {
      sessionData.openRepository(repository);
    }
    sessionData.startReadWriteTransaction();
    modelMap = new HashMap();
  }

  private void closeRepository() throws SdaiException {
    if (repository.getId() != null) {
      sessionData.closeRepository(repository);
    }
    repository = null;
    modelMap = null;
  }

  /**
   * Setter for property sessionData.
   *
   * @return Value of property sessionData.
   */
  public void setSessionData(EditorData sessionData) {
    this.sessionData = sessionData;
  }

  public void addModel(String modelName, SdaiModel model) {
    modelMap.put(modelName, model);
  }

  public SdaiModel getModelByName(String modelName) {
    return (SdaiModel) modelMap.get(modelName);
  }

}
