/*
 * Created on Jan 27, 2006
 * Author: Kazimieras Vaina
 */
package com.lksoft.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.util.FileNameMapper;

public class RelativePathMapper implements FileNameMapper {
  private List basePathElements;

  @Override
  public void setFrom(String arg0) {
    // ignore it
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.tools.ant.util.FileNameMapper#setTo(java.lang.String)
   */
  @Override
  public void setTo(String to) {
    basePathElements = getPathStructure(new File(to));
  }

  /**
   * @param dir
   */
  private List getPathStructure(File dir) {
    LinkedList<String> pathStructure = new LinkedList<String>();
    do {
      pathStructure.addFirst(dir.getName());
      dir = dir.getParentFile();
    } while (dir != null);
    return pathStructure;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.tools.ant.util.FileNameMapper#mapFileName(java.lang.String)
   */
  @Override
  public String[] mapFileName(String fileName) {
    List pathElements = getPathStructure(new File(fileName));
    int matchedElements = 0;
    while ((pathElements.size() > matchedElements) && (basePathElements.size() > matchedElements)
        && basePathElements.get(matchedElements).equals(pathElements.get(matchedElements))) {
      matchedElements++;
    }
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < (basePathElements.size() - matchedElements); i++) {
      buf.append("..");
      buf.append(File.separatorChar);
    }
    for (int i = matchedElements; i < pathElements.size(); i++) {
      buf.append(pathElements.get(i));
      if ((i + 1) < pathElements.size()) {
        buf.append(File.separatorChar);
      }
    }

    String[] retValue = { buf.toString() };
    return retValue;
  }
}
