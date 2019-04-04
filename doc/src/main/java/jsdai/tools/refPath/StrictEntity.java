package jsdai.tools.refPath;

class StrictEntity implements Path {
  protected String entityName = null;

  public void setEntityName(String newName) {
    entityName = newName;
  }

  public String display() {
    if (entityName != null) {
      return "|" + entityName + "|";
    }
    else {
      return "| |";
    }
  }
}