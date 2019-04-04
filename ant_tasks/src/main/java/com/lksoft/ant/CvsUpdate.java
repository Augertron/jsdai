/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 16, 2005 11:37:18 AM
 */
package com.lksoft.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;

public class CvsUpdate extends Task {

  private File revisions;
  private TopDirContainer topDirs;

  public CvsUpdate() {
    super();
  }

  public void setRevisions(File revisions) {
    this.revisions = revisions;
  }

  public void addConfiguredTopdirs(TopDirContainer topDirs) {
    this.topDirs = topDirs;
  }

  @Override
  public void execute() throws BuildException {

    // read local entries from topdirs
    Map localTopdirs = new HashMap();
    Map localEntries = new HashMap();

    for (Iterator i = topDirs.getDirs().iterator(); i.hasNext(); ) {
      TopDir dir = (TopDir) i.next();
      log("Processing module: " + dir.getModule());

      localTopdirs.put(dir.getModule(), dir);
      CvsEntriesWorker.getEntries(dir, localEntries);
    }

    // read required revisions
    Map reqRevisions;
    try {
      reqRevisions = CvsEntriesWorker.readEntries(revisions);
    }
    catch (IOException e) {
      throw new BuildException(e);
    }

    // update as needed
    log("Updating...");

    Set<String> unknownDirs = new HashSet<String>();
    int updatedFiles = 0;
    int checkedOutFiles = 0;
    for (Iterator i = reqRevisions.keySet().iterator(); i.hasNext(); ) {
      String file = (String) i.next();
      String reqVersion = (String) reqRevisions.get(file);
      String localVersion = (String) localEntries.get(file);

      boolean exists = localVersion != null;
      if (exists && localVersion.equals(reqVersion)) {
        continue;
      }

      String topDir = file.substring(0, file.indexOf(':'));
      TopDir dir = (TopDir) localTopdirs.get(topDir);
      if (dir == null) {
        if (!unknownDirs.contains(topDir)) {
          log("Unknown topdir: " + topDir);
          unknownDirs.add(topDir);
        }
        continue;
      }

      file = file.substring(file.indexOf(':') + 1);

      Cvs cvs = new Cvs();
      cvs.setProject(getProject());
      cvs.setOwningTarget(getOwningTarget());
      cvs.setTaskName("cvs");
      cvs.setQuiet(true);

      if (!exists) {
        cvs.setDest(dir.getBase());
        cvs.setCommand("checkout -r " + reqVersion + " " + dir.getModule() + "/" + file);
        cvs.setCvsRoot(dir.getRoot());

        cvs.execute();
        checkedOutFiles++;

        log("Checked out: " + file + " in " + dir.getModule());
      }
      else {
        cvs.setDest(new File(dir.getBase(), dir.getModule()));
        cvs.setCommand("update -r " + reqVersion + " " + file);

        cvs.execute();
        updatedFiles++;

        log("Updated: " + file + " in " + dir.getModule());
      }
    }

    log("Done updating. Updated " + updatedFiles + " files. Checked out " + checkedOutFiles + " files.");
  }
}
