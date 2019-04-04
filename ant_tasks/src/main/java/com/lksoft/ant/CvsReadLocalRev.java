/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 15, 2005 2:08:14 PM
 */
package com.lksoft.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;

public class CvsReadLocalRev extends Task {

  private TopDirContainer topDirs;
  private File output;

  public CvsReadLocalRev() {
    super();
  }

  public void addConfiguredTopdirs(TopDirContainer topDirs) {
    this.topDirs = topDirs;
  }

  public void setOutput(File output) {
    this.output = output;
  }

  @Override
  public void execute() throws BuildException {

    // read entries from topdirs
    Map<String, String> entries = new HashMap<String, String>();

    for (Iterator i = topDirs.getDirs().iterator(); i.hasNext(); ) {
      TopDir dir = (TopDir) i.next();
      log("Processing module: " + dir.getModule());
      CvsEntriesWorker.getEntries(dir, entries);
    }

    // write entries to output
    try {
      CvsEntriesWorker.writeEntries(output, entries);
    }
    catch (IOException e) {
      throw new BuildException(e);
    }

    log("Revisions recorded to file: " + output);
  }
}
