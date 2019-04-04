package jsdai.tools.refPath;

class Repeat implements Path {
  protected PathElements path = null;

  public String display() {
    if (path == null) {
      return "( )*";
    }
    else {
      return "(" + path.display() + ")*";
    }
  }
}
