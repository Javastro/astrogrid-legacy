package  org.astrogrid.datacenter.parser.test;

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

import java.io.*;

public class Token {
  private static StreamTokenizer stoken;
  public Token() { //tokens definition
    stoken.resetSyntax();
    stoken.wordChars('a', 'z');
    stoken.wordChars('A', 'Z');
    stoken.parseNumbers();
    stoken.lowerCaseMode(true);
    stoken.whitespaceChars(0, ' ');
    stoken.ordinaryChar('.');
  }
}
