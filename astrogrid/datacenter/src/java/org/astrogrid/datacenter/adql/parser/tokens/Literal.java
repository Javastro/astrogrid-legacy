package  org.astrogrid.datacenter.parser.tokens;

import java.util.*;

import  org.astrogrid.datacenter.parser.parse.*;

public class Literal
    extends Terminal {
  protected Token literal;

  /**
   *
   * @param s String
   */
  public Literal(String s) {
    literal = new Token(s);
  }

  /**
   *
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    return literal.equals( (Token) o);
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  public String unvisitedString(Vector visited) {
    return literal.toString();
  }
}
