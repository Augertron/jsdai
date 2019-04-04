package jsdai.tools.backup;

public class RepositoryInfo {
  String name;
  Boolean backup;
  SchemaInstanceInfo[] siInfo;

  static final int COLUMN_BACKUP = 1;

  public RepositoryInfo(String name) {
    this.name = name;
    this.backup = new Boolean(false);
  }

  public void setSIInfo(SchemaInstanceInfo[] siInfo) {
    this.siInfo = siInfo;
  }

  public SchemaInstanceInfo[] getSIInfo() {
    return this.siInfo;
  }
}