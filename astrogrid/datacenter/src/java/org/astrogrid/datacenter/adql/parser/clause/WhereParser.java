package  org.astrogrid.datacenter.parser.clause;

import  org.astrogrid.datacenter.parser.match.*;

/**
 *
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */
public class WhereParser {
  /**
   * A string parser that looks at a Logical expression (ie this AND that, this OR that)
   * and creates a LogicalExpression to model that
   * @param expression String
   * @return BooleanExpression
   */

  public static BooleanExpression parseBoolean(String expression) {

    int andIndex = expression.toUpperCase().indexOf(" AND "); //surround by spaces so we don't find it in other words

    if (andIndex > -1) { //found an AND
      BooleanExpression lhs = parseBoolean(expression.substring(0, andIndex));
      BooleanExpression rhs = parseBoolean(expression.substring(andIndex + 6));
      return new LogicalExpression(lhs, "AND", rhs);
    }

    int orIndex = expression.toUpperCase().indexOf(" OR "); //surround by spaces so we don't find it in other words

    if (orIndex > -1) { //found an AND
      BooleanExpression lhs = parseBoolean(expression.substring(0, orIndex));
      BooleanExpression rhs = parseBoolean(expression.substring(orIndex + 5));
      return new LogicalExpression(lhs, "OR", rhs);
    }

    int opIndex = expression.toUpperCase().indexOf(" > "); //surround by spaces so we don't find it in other words
    if (opIndex == -1) {
      opIndex = expression.toUpperCase().indexOf(" < ");
    }
    if (opIndex == -1) {
      opIndex = expression.toUpperCase().indexOf(" = ");
    }

    if (opIndex > -1) { //found an operator
      NumericExpression lhs = parseNumeric(expression.substring(0, opIndex));
      NumericExpression rhs = parseNumeric(expression.substring(opIndex + 4));
      return new ComparisonExpression(lhs, expression.substring(opIndex + 1, 1),
                                      rhs);
    }
    throw new IllegalArgumentException("'" + expression +
                                       "' is not a boolean expression (no logical/comparison operator AND, OR, <, > or =)");
  }

  /**
   *
   * @param expression String
   * @return NumericExpression
   */
  public static NumericExpression parseNumeric(String expression) {
    try {
      int value = Integer.parseInt(expression);
      return new Constant("" + value);
    }
    catch (NumberFormatException nfe) {
      //wasn't a number, must be a column reference
      return new ColumnReference(expression);
    }
  }

  public static void main(String[] args) {

    String s = "S.RA > 2 AND S.RA = G.RA AND S.DEC = G.DEC";
    BooleanExpression whereClause = WhereParser.parseBoolean(s);

    if (whereClause instanceof LogicalExpression) {
      BooleanExpression lhs = ( (LogicalExpression) whereClause).getLHS();
    }
  }
}
