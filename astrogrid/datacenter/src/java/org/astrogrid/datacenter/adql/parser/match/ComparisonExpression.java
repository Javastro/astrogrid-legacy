package  org.astrogrid.datacenter.parser.match;

public class ComparisonExpression
    extends BooleanExpression {
  /**
   * A Comparison Expression is one that consists of a numeric expression and a
   * a comparison operator (eg less than, equals, greater than, etc) that returns
   * a true/false answer when evaluated.
   */
  private NumericExpression lhs = null; //left hand side of expression
  private NumericExpression rhs = null; //left right side of expression

  public ComparisonExpression(NumericExpression givenLHS, String operator,
                              NumericExpression givenRHS) {
    this.lhs = givenLHS;
    this.rhs = givenRHS;
  }
}
