package jsdai.tools.refPath;

import java.util.Vector;

class PathElements {

  protected Vector pathElements = null;

  public PathElement getLast() {
    if (pathElements == null) {
      return null;
    }
    return (PathElement) pathElements.lastElement();
  }

  public void removeLast() {
    if (pathElements == null) {
      return;
    }
    pathElements.remove(pathElements.size() - 1);
    if (pathElements.size() == 0) {
      pathElements = null;
    }
  }

  public boolean isEmpty() {
    if (pathElements == null) {
      return true;
    }
    else {
      return false;
    }
  }

  public void add(Object o) {
    if (pathElements == null) {
      pathElements = new Vector();
    }
    pathElements.add(o);
  }

  public String display() {
    String result = "";
    if (pathElements != null) {
      int count = pathElements.size();
      for (int i = 0; i < count; i++) {
        result = result + ((PathElement) pathElements.get(i)).display() + ";<BR>\n";
      }
      // remove last ";<BR>\n":
      if (result.endsWith(";<BR>\n")) {
        int endIndex = result.length() - 6;
        result = result.substring(0, endIndex);
      }
    }
    return result;
  }
}