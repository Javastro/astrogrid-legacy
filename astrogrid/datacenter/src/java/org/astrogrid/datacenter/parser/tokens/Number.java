package  org.astrogrid.datacenter.parser.tokens;

import java.util.*;

import  org.astrogrid.datacenter.parser.parse.*;

public class Number
    extends Terminal {
  /**
   *
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    Token t = (Token) o;
    return t.isNumber();
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  public Vector randomExpansion(int maxDepth, int depth) {
    double dou = Math.floor(1000.0 * Math.random()) / 10;
    Vector ve = new Vector();
    ve.addElement(Double.toString(dou));
    return ve;
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  public String unvisitedString(Vector visited) {
    return "Number";
  }
}
