/*
 * Tool for ant
 */

package com.lksoft.ant;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.io.*;
import java.util.*;

/**
 * Based on copy task. Concatenates source files to one destination
 * file.
 *
 * @author Vaidas Nargelas <a href="mailto:vaidas.nargelas@lksoft.lt">vaidas.nargelas@lksoft.lt</a>
 */
public class Cat extends Task {
  protected File file = null; // the source file
  protected File destFile = null; // the destination file
  protected Vector<FileSet> filesets = new Vector<FileSet>();

  protected boolean filtering = false;
  protected boolean forceOverwrite = false;
  protected boolean append = false;
  protected int verbosity = Project.MSG_VERBOSE;

  protected HashSet<String> fileCopySet = new HashSet<String>();

  /**
   * Sets a single source file to copy.
   */
  public void setFile(File file) {
    this.file = file;
  }

  /**
   * Sets the destination file.
   */
  public void setTofile(File destFile) {
    this.destFile = destFile;
  }

  /**
   * Sets filtering.
   */
  public void setFiltering(boolean filtering) {
    this.filtering = filtering;
  }

  /**
   * Overwrite any existing destination file(s).
   */
  public void setOverwrite(boolean overwrite) {
    this.forceOverwrite = overwrite;
  }

  /**
   * If append is true the existing destination file is
   * now overwritten but source file(s) are appended to the end.
   */
  public void setAppend(boolean append) {
    this.append = append;
  }

  /**
   * Used to force listing of all names of copied files.
   */
  public void setVerbose(boolean verbose) {
    if (verbose) {
      this.verbosity = Project.MSG_INFO;
    }
    else {
      this.verbosity = Project.MSG_VERBOSE;
    }
  }

  /**
   * Adds a set of files (nested fileset attribute).
   */
  public void addFileset(FileSet set) {
    filesets.addElement(set);
  }

  /**
   * Performs the copy operation.
   */
  @Override
  public void execute() throws BuildException {
    // make sure we don't have an illegal set of options
    validateAttributes();

    boolean needCat = false;
    if (forceOverwrite) {
      needCat = true;
    }

    // deal with the single file
    if (file != null) {
      if (forceOverwrite || (file.lastModified() > destFile.lastModified())) {
        fileCopySet.add(file.getAbsolutePath());
        needCat = true;
      }
    }

    // deal with the filesets
    if (!needCat) {
      for (int i = 0; i < filesets.size(); i++) {
        FileSet fs = filesets.elementAt(i);
        DirectoryScanner ds = fs.getDirectoryScanner(project);
        File fromDir = fs.getDir(project);

        String[] srcFiles = ds.getIncludedFiles();

        needCat = scanAndCheck(fromDir, srcFiles);
        if (needCat) {
          break;
        }
      }
    }

    if (needCat) {
      for (int i = 0; i < filesets.size(); i++) {
        FileSet fs = filesets.elementAt(i);
        DirectoryScanner ds = fs.getDirectoryScanner(project);
        File fromDir = fs.getDir(project);

        String[] srcFiles = ds.getIncludedFiles();

        scanAndAdd(fromDir, srcFiles);
      }
      // do all the copy operations now...
      doFileOperations();
    }

  }

//************************************************************************
//  protected and private methods
//************************************************************************

  /**
   * Ensure we have a consistent and legal set of attributes, and set
   * any internal flags necessary based on different combinations
   * of attributes.
   */
  protected void validateAttributes() throws BuildException {
    if (file == null && filesets.size() == 0) {
      throw new BuildException("Specify at least one source - a file or a fileset.");
    }

    if (destFile == null) {
      throw new BuildException("Destination file must be set.");
    }

    if (destFile != null && destFile.exists() && destFile.isDirectory()) {
      throw new BuildException("Destination can not be a directory.");
    }

  }

  /**
   * Compares source files to destination files to see if they should be
   * copied.
   */
  protected boolean scanAndCheck(File fromDir, String[] files) {
    boolean needCat = false;
    for (int i = 0; i < files.length; i++) {
      String filename = files[i];
      File src = new File(fromDir, filename);
      if (src.lastModified() > destFile.lastModified()) {
        needCat = true;
        break;
      }
    }

    return needCat;
  }

  protected void scanAndAdd(File fromDir, String[] files) {
    for (int i = 0; i < files.length; i++) {
      String filename = files[i];
      File src = new File(fromDir, filename);
      fileCopySet.add(src.getAbsolutePath());
    }

  }

  /**
   * Actually does the file (and possibly empty directory) copies.
   * This is a good method for subclasses to override.
   */
  protected void doFileOperations() {
    if (fileCopySet.size() > 0) {
      log("Concatenating " + fileCopySet.size() + " files to " + destFile.getAbsolutePath());

      try {
        // ensure that parent dir of dest file exists!
        // not using getParentFile method to stay 1.1 compat
        File parent = new File(destFile.getParent());
        if (!parent.exists()) {
          parent.mkdirs();
        }
        FileOutputStream destFileStream = new FileOutputStream(destFile.getAbsolutePath(), append);

        Iterator i = fileCopySet.iterator();
        while (i.hasNext()) {
          String fromFile = (String) i.next();

          try {
            log("Concatenating " + fromFile, verbosity);
            File fromFileFile = new File(fromFile);

            FileInputStream in = new FileInputStream(fromFileFile);

            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
              destFileStream.write(buffer, 0, count);
              count = in.read(buffer, 0, buffer.length);
            } while (count != -1);

            in.close();
          }
          catch (IOException ioe) {
            destFileStream.close();
            String msg = "Failed to concatenate " + fromFile + " to " + destFile.getAbsolutePath() + " due to " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
          }
        }
        destFileStream.close();
      }
      catch (IOException ioe) {
        String msg = "Failed to concatenate to " + destFile.getAbsolutePath() + " due to " + ioe.getMessage();
        throw new BuildException(msg, ioe, location);
      }
    }

  }

}
