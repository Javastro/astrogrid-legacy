package  org.astrogrid.datacenter.parser.parse;

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
import java.util.*;

public abstract class Assembler {
  /**
   *
   * @param ass Assembly
   * @param fe Object
   * @return Vector
   */
  public static Vector elementsAbove(Assembly ass, Object fe) {
    Vector ve = new Vector();

    while (!ass.stackIsEmpty()) {
      Object top = ass.pop();
      if (top.equals(fe)) {
        break;
      }
      ve.addElement(top);
    }
    return ve;
  }

  /**
   *
   * @param ass Assembly
   */
  public abstract void workOn(Assembly ass);
}
