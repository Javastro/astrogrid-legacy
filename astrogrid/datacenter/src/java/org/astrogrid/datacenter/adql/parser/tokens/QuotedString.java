package  org.astrogrid.datacenter.parser.tokens;

import java.util.*;

import  org.astrogrid.datacenter.parser.parse.*;

public class QuotedString
    extends Terminal {
  /**
   *
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    Token t = (Token) o;
    return t.isQuotedString();
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  public Vector randomExpansion(int maxDepth, int depth) {
    int n = (int) (5.0 * Math.random());
    char[] letters = new char[n + 2];
    letters[0] = '"';
    letters[n + 1] = '"';

    for (int i = 0; i < n; i++) {
      int c = (int) (26.0 * Math.random()) + 'a';
      letters[i + 1] = (char) c;
    }
    Vector v = new Vector();
    v.addElement(new String(letters));
    return v;
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  public String unvisitedString(Vector visited) {
    return "QuotedString";
  }
}
