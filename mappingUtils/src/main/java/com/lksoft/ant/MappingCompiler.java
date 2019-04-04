/*
 * Tool for ant
 */

package com.lksoft.ant;

import java.io.File;

import jsdai.mappingUtils.paths.MappingPath;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

/**
 * @author Vaidas Nargelas <a href="mailto:vaidas.nargelas@lksoft.lt">vaidas.nargelas@lksoft.lt</a>
 */
public class MappingCompiler extends MatchingTask {
  private File baseDir = null;
  private boolean fail = true;

  public void setJsdaiProperties(File properties) {
    System.setProperty("jsdai.properties", properties.getAbsolutePath());
  }

  public void setFail(boolean fail) {
    this.fail = fail;
  }

  /**
   * Set the base directory.
   **/
  public void setBasedir(File dir) {
    baseDir = dir;
  } //-- setBaseDir

  /**
   * Performs the copy operation.
   */
  public void execute() throws BuildException {
    if (baseDir == null) {
      baseDir = project.resolveFile(".");
    }

    DirectoryScanner scanner;
    String[] list;
    String baseParent = baseDir.getPath() + File.separatorChar;
    log("baseParent is " + baseParent, Project.MSG_INFO);
    scanner = getDirectoryScanner(baseDir);
    list = scanner.getIncludedFiles();
    for (int i = 0; i < list.length; ++i) {
      try {
        MappingPath.main(new String[] { baseParent + list[i] });
      }
      catch (Exception ex) {
        // If failed to process document, must delete target document,
        // or it will not attempt to process it the second time
        log("Failed to process " + baseParent + list[i], Project.MSG_INFO);
        if (fail) {
          throw new BuildException(ex);
        }
        else {
          log(ex.toString(), Project.MSG_INFO);
        }
      }
    }
  }

}
