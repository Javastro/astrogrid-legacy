package  org.astrogrid.datacenter.parser.match;

public class LogicalExpression
    extends BooleanExpression {
  /**
   * A Logical Expression is one that consists of a logical operator
   * (ie this AND that, this OR that).
   * For the purposes of this model, it takes only two arguments (a left hand
   * side and a right hand side) rather than being a Union/Intersection approach
   */

  private BooleanExpression lhs = null; //left hand side of expression
  private BooleanExpression rhs = null; //right hand side of expression
  private String operator = null; //AND or OR

  /**
   *
   * @param givenLHS BooleanExpression
   * @param givenOperator String
   * @param givenRHS BooleanExpression
   */

  public LogicalExpression(BooleanExpression givenLHS, String givenOperator,
                           BooleanExpression givenRHS) {

    this.lhs = givenLHS;
    this.rhs = givenRHS;
    this.operator = givenOperator;
  }

  /**
   *
   * @return BooleanExpression
   */
  public BooleanExpression getLHS() {
    return lhs;
  }

  /**
   *
   * @return BooleanExpression
   */
  public BooleanExpression getRHS() {
    return rhs;
  }

  /**
   *
   * @return String
   */
  public String getOperator() {
    return operator;
  }

}
