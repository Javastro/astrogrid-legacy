package  org.astrogrid.datacenter.parser.test;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

import java.io.*;
import java.util.*;

import  org.astrogrid.datacenter.parser.tokens.*;
import  org.astrogrid.datacenter.parser.util.*;

public class Test1 {
  private static Vector v = new Vector();
  public static void main(String args[]) throws IOException {

    Tokenizer t = new Tokenizer(Transform.URLFileToString(
        "http://astrogrid.cs.qub.ac.uk/~pedro/code/sql/query.sql"));

    t.setCharacterState('#', '#', t.quoteState());
    /**
        while (true) {
          Token tok = t.nextToken();
          if (tok.equals(Token.EOF)) {
            break;
          }
          System.out.println(tok);
          v.add(tok);
        }

        for (int i = 0; i < v.size(); i++) {
          System.out.print("    > i:" + i);
          System.out.println("          " + v.elementAt(i));

        }
     */
  }

}
