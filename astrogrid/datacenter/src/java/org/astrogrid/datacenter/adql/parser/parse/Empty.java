package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

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

public class Empty
    extends Parser {
  /**
   *
   * @param pv ParserVisitorHierarchy
   * @param visited Vector
   */
  public void accept(ParserVisitorHierarchy pvh, Vector visited) {
    pvh.visitEmpty(this, visited);
  }

  /**
   *
   * @param in Vector
   * @return Vector
   */
  public Vector match(Vector in) {
    return elementClone(in);
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  protected Vector randomExpansion(int maxDepth, int depth) {
    return new Vector();
  }

  /**
   * return parser description
   * @param visited Vector
   * @return String
   */
  protected String unvisitedString(Vector visited) {
    return " empty ";
  }
}
