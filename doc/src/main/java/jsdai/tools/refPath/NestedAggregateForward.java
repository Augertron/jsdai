package jsdai.tools.refPath;

class NestedAggregateForward implements IdForwardElement {
  protected String aggregateTypeName = null;
  protected RPAggregate aggregate = null;

  public String display() {
    if ((aggregateTypeName == null) || (aggregate == null)) {
      return "=";
    }
    return aggregateTypeName + aggregate.display() + "=";
  }
}
