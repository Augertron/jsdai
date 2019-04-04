/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 16, 2005 3:29:12 PM
 */
package com.lksoft.ant;

import java.util.*;

import org.apache.tools.ant.types.*;

public class TopDirContainer extends DataType {

  private List<TopDir> dirs;

  public TopDirContainer() {
    super();

    dirs = new LinkedList<TopDir>();
  }

  public void addConfiguredTopdir(TopDir dir) {
    dirs.add(dir);
  }

  public List getDirs() {
    return dirs;
  }
}
