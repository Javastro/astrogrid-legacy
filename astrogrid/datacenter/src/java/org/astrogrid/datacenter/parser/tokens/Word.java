package  org.astrogrid.datacenter.parser.tokens;

import java.util.*;
import  org.astrogrid.datacenter.parser.parse.*;

public class Word
    extends Terminal {

  /**
   * Returns true if an assembly's next element is a word
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    Token t = (Token) o;
    return t.isWord();
  }

  /**
   * Create a set with one random word (with 3 to 7 characters)
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  public Vector randomExpansion(int maxDepth, int depth) {
    int n = (int) (5.0 * Math.random()) + 3;

    char[] letters = new char[n];
    for (int i = 0; i < n; i++) {
      int c = (int) (26.0 * Math.random()) + 'a';
      letters[i] = (char) c;
    }

    Vector v = new Vector();
    v.addElement(new String(letters));
    return v;
  }

  /**
   * Returns a textual description of this parser
   * @param visited Vector a list of parsers already printed in this description
   * @return String a textual description of this parser
   */
  public String unvisitedString(Vector visited) {
    return "Word";
  }
}
