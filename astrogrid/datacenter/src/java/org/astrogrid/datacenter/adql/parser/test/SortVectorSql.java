package  org.astrogrid.datacenter.parser.test;

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

public class SortVectorSql {
  public SortVectorSql() {
  }

  /**
   * This is matrix definition which contains parser keyWords
   */
  private final static String[] keyWord = {
      "select",
      "from",
      "where",
  };

  /**
   *
   * @param v Vector
   * @return Vector[]
   */
  public static Vector Expresion(Vector v) {
    Vector vSql = new Vector();
    Vector vSelect = new Vector();
    Vector vFrom = new Vector();
    Vector vWhere = new Vector();

    for (int i = 0; i < v.size(); i++) {
      while (v.elementAt(i) != keyWord[2]) {
        vSelect.add(v.elementAt(i));
      }
    }
    //v.elementAt(i);
    for (int i = 0; i < vSelect.size(); i++) {
      System.out.println("    > i :" + i + " " + vSelect.elementAt(i));
    }

    return (vSelect);
  }

}
