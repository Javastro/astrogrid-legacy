package  org.astrogrid.datacenter.parser.test;

import java.io.*;

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

public class Test3 {
  public static void main(String[] args) throws IOException,
      FileNotFoundException {

    SqlToVector stv = new SqlToVector();
    stv.FromURL("http://astrogrid.cs.qub.ac.uk/~pedro/code/sql/query.sql");
  }

}
