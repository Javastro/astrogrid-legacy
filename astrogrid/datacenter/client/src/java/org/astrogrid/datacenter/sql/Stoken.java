package  org.astrogrid.datacenter.sql;

import java.io.*;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class Stoken {
  private static StreamTokenizer stoken;
  public void Stoken(File file)  throws Exception {
    try {
      stoken = new StreamTokenizer(new InputStreamReader(new FileInputStream(file)));
    }
    catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
    stoken.resetSyntax();
    stoken.wordChars('a', 'z');
    stoken.wordChars('A', 'Z');
    stoken.parseNumbers();
    stoken.lowerCaseMode(true);
    stoken.whitespaceChars(0, ' ');
    stoken.ordinaryChar('.');
  }

}
