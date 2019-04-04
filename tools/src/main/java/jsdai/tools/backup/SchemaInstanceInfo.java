package jsdai.tools.backup;

public class SchemaInstanceInfo {
  String name;
  Boolean backup;

  public SchemaInstanceInfo(String name) {
    this.name = name;
    this.backup = new Boolean(false);
  }
}