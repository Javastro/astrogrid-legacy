package  org.astrogrid.datacenter.parser.util;

import java.io.*;
import java.net.*;
import java.util.*;

import parser.tokens.*;

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

public class SqlToVector
    extends Vector {
  /**
   *
   * This clase chunk a URL File into tokens and return each token into a  Vector
   * @param fileName String
   * @throws IOException
   * @return Vector - contains tokens
   */

  public static Vector FromURL(String fileName) throws IOException {
    Vector v = new Vector();
    Tokenizer tk = new Tokenizer();
    try {
      tk = new Tokenizer(Transform.URLFileToString(fileName));
    }
    catch (MalformedURLException ex) {
    }
    tk.setCharacterState('#', '#', tk.quoteState());

    while (true) {
      Token tok = tk.nextToken();
      if (tok.equals(Token.EOF)) {
        break;
      }
      v.add(tok);
    }
//  to see vector content
    for (int i = 0; i < v.size(); i++) {
      System.out.println("    > i :" + i + " " + v.elementAt(i));
    }
    return (v);
  }

  /**
   * This clase chunk a Local File into tokens and return a Vector with them
   * @param fileName String with a local file name
   * @throws IOException
   * @return Vector - contains tokens
   */
  public static Object FromLocalFile(String fileName) throws IOException {
    Vector v = new Vector();
    Vector vs = new Vector();
    Tokenizer tk = new Tokenizer();

    //
    try {
      tk = new Tokenizer(Transform.LocalFileToString(fileName));
    }
    catch (MalformedURLException ex) {
    }
    tk.setCharacterState('#', '#', tk.quoteState());

    //add the tokens to a Vector
    while (true) {
      Token tok = tk.nextToken();
      if (tok.equals(Token.EOF)) {
        break;
      }
      v.add(tok);
    }
    //to see vector content
    for (int i = 0; i < v.size(); i++) {
      System.out.println("  i[" + i + "] : " + v.elementAt(i));
    }
    return (v);
  }
}
