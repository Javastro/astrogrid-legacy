package  org.astrogrid.datacenter.parser.match;

public class Constant
    extends NumericExpression {
  /**
   * A Constant is a numeric value such as '3' or '12.0'
   */
  String value = null;

  public Constant(String givenValue) {
    this.value = givenValue;
  }
}
