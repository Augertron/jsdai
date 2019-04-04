/*
 * EditorData.java
 *
 * Created on �e�tadienis, 2001, Kovo 31, 14.06
 */

package jsdai.mappingUtils;

import java.util.HashMap;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

/**
 * @author Vaidas Narg�las
 */
public class EditorData {

  /**
   * Holds value of property session.
   */
  private SdaiSession session;
  private boolean initialized = false;
  private final HashMap openRepCounts = new HashMap();

  private SdaiTransaction transaction = null;

  /**
   * Creates new EditorData
   */
  public EditorData() throws SdaiException {
  }

  /**
   * Getter for property session.
   *
   * @return Value of property session.
   */
  public SdaiSession getSession() throws SdaiException {
    return session;
  }

  public void startReadOnlyTransaction() throws SdaiException {
    if (transaction != null) {
      commitTransaction();
    }
    transaction = session.startTransactionReadOnlyAccess();
  }

  public void startReadWriteTransaction() throws SdaiException {
    if (transaction != null) {
      commitTransaction();
    }
    transaction = session.startTransactionReadWriteAccess();
  }

  public void abortTransaction() throws SdaiException {
    transaction.endTransactionAccessAbort();
    transaction = null;
  }

  public void commitTransaction() throws SdaiException {
    transaction.endTransactionAccessCommit();
    transaction = null;
  }

  public void setJsdaiProperties(String directory) throws SdaiException {
    if (!initialized) {
      System.setProperty("jsdai.properties", directory);
      session = SdaiSession.openSession();
      initialized = true;
    }
  }

  public void openRepository(SdaiRepository repository) throws SdaiException {
    String repositoryName = repository.getName();
    OpenRepCount openCount = (OpenRepCount) openRepCounts.get(repositoryName);
    if (openCount == null) {
      repository.openRepository();
      openRepCounts.put(repositoryName, new OpenRepCount(1));
    }
    else {
      openCount.openCount++;
    }
  }

  public void closeRepository(SdaiRepository repository) throws SdaiException {
    String repositoryName = repository.getName();
    OpenRepCount openCount = (OpenRepCount) openRepCounts.get(repositoryName);
    if (openCount != null) {
      if (--openCount.openCount == 0) {
        repository.closeRepository();
        openRepCounts.remove(repositoryName);
      }
    }
  }

  static private class OpenRepCount {
    public int openCount;

    OpenRepCount(int initialCount) {
      openCount = initialCount;
    }
  }

}
