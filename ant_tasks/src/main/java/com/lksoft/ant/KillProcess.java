/*
 * $Id$
 *
 * Copyright (C) LKSoftWare GmbH, 2002. All Rights Reserved.
 *
 */
package com.lksoft.ant;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Reference;

/**
 * Kills the running process.
 *
 * @author Vaidas Nargelas <a href="mailto:vaidas.nargelas@lksoft.lt">vaidas.nargelas@lksoft.lt</a>
 * @version $Revision$
 */
public class KillProcess extends Task {
  private Reference ref;

  /**
   * Sets a reference id to the running process.
   */
  public void setRefid(Reference ref) {
    this.ref = ref;
  }

  /**
   * Kills the process.
   */
  public void execute() throws BuildException {
    try {
      Object javaProcessObject = ref.getReferencedObject(getProject());

      Class javaProcessClass = javaProcessObject.getClass();
      Method getProcessMethod = javaProcessClass.getMethod("getProcess", null);
      Process process = (Process) getProcessMethod.invoke(javaProcessObject, null);
      if (process == null) {
        log("Process " + ref.getRefId() + " was not running", Project.MSG_VERBOSE);
      }
      else {
        process.destroy();
        log("Process " + ref.getRefId() + " was killed", Project.MSG_INFO);
      }
    }
    catch (NoSuchMethodException e) {
      log("Id " + ref.getRefId() + " doesn't reference a process", Project.MSG_ERR);
      throw new BuildException("Id " + ref.getRefId() + " doesn't reference a process");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
