/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 16, 2005 3:23:59 PM
 */
package com.lksoft.ant;

import java.io.*;

import org.apache.tools.ant.types.*;

public class TopDir extends DataType {

  private String module;
  private File base;
  private String root;

  public TopDir() {
    super();
  }

  public void setModule(String name) {
    this.module = name;
  }

  public String getModule() {
    return module;
  }

  public void setBase(File dir) {
    this.base = dir;
  }

  public File getBase() {
    return base;
  }

  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }
}
