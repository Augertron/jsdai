package jsdai.tools.refPath;

class NestedAggregateBackward implements IdBackwardElement {
  protected String aggregateTypeName = null;
  protected RPAggregate aggregate = null;

  public String display() {
    if ((aggregateTypeName == null) || (aggregate == null)) {
      return "=";
    }
    return "=" + aggregateTypeName + aggregate.display();
  }
}