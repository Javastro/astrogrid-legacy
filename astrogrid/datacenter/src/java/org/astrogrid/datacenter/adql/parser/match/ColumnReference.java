package  org.astrogrid.datacenter.parser.match;

public class ColumnReference
    extends NumericExpression {
  /**
   * A Constant is a numeric value such as '3' or '12.0'
   */
  String col = null;
  String tableAlias = null;
  String colName = null;

  public ColumnReference(String givenRef) {
    this.col = givenRef;
  }
}
