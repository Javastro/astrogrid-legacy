package  org.astrogrid.datacenter.parser.test;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Data Centre Group
 *
 */

public class Expression {

  public class BooleanOperation
      extends Expression {
    Expression left;
    Expression right;
    //BooleanOperator op;
  }

//(BooleanOperator might be an enumeration of AND, OR, XOR, >, <, = etc)

  public class MathExpression
      extends Expression {
    Expression left;
    Expression right;
    //MathOperator op;
  }

//(MathOperator might be an enumeration of '+', '-', '*', etc)

  public class Column
      extends Expression {
    String tableAlias;
    String columnName;
  }

  public class Value
      extends Expression {
    String value;
  }
}
