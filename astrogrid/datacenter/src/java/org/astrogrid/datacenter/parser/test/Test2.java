package  org.astrogrid.datacenter.parser.test;

import java.io.*;
import java.util.*;

import  org.astrogrid.datacenter.parser.util.*;

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

public class Test2 {

  public static void main(String[] args) throws IOException,
      NullPointerException {
    SqlToVector stv = new SqlToVector();
    Vector vs = new Vector();
    stv.FromURL("http://astrogrid.cs.qub.ac.uk/~pedro/code/sql/query.sql");
    //stv.FromLocalFile("D:\\temp\\select.sql");

    int i = 0;
    if (stv.elementAt(0) != "") {
      if (stv.elementAt(0) == "Select") {
        while (stv.elementAt(i) != "From") {
          vs.add(stv.elementAt(i));
        }
      }
      //else if () {

      //}
      //else {

      //}

    }

    for (int j = 0; j < vs.size(); j++) {
      System.out.println("    > i :" + j + " " + vs.elementAt(j));
    }

    for (int j = 0; j < stv.size(); j++) {
      // System.out.println("    > j :" + j + " " + stv.size(j));
    }

  }
}
