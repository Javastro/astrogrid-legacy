package  org.astrogrid.datacenter.sql;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class Expression {

  public class BooleanOperation  extends Expression {
    Expression left;
    Expression right;
    //BooleanOperator op;
  }

//(BooleanOperator might be an enumeration of AND, OR, XOR, >, <, = etc)
  public class MathExpression extends Expression {
    Expression left;
    Expression right;
    //MathOperator op;
  }
//PushbackReader
//(MathOperator might be an enumeration of '+', '-', '*', etc)
  public class Column extends Expression {
    String tableAlias;
    String columnName;
  }

  public class Value extends Expression {
    String value;
  }
}
