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

import  org.astrogrid.datacenter.parser.util.*;

public class test {

  public static void main(String[] args) throws Exception {

    String filePath = "//home//pedro//public_html//code//sql//select.sql";
    String fileURL = "http://astrogrid.cs.qub.ac.uk/~pedro/code/sql/select.sql";

    StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(new
        FileInputStream(
        filePath)));

    System.out.print(Transform.LocalFileToString(filePath));

    tokenizer.resetSyntax();
    tokenizer.parseNumbers();
    tokenizer.eolIsSignificant(false);
    tokenizer.wordChars('A', 'Z');
    tokenizer.wordChars('a', 'z');
    tokenizer.wordChars('\u00A0', '\u00FF');
    tokenizer.whitespaceChars('\u0000', '\u0020');
    tokenizer.whitespaceChars(0, ' ');
    tokenizer.ordinaryChar('.');
    tokenizer.ordinaryChar('\'');
    tokenizer.ordinaryChar('\"');

    while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {

      switch (tokenizer.ttype) {
        case StreamTokenizer.TT_EOF:
          System.out.println("EOF");
          break;
        case StreamTokenizer.TT_EOL:
          System.out.println("EOL");
          break;
        case StreamTokenizer.TT_WORD:
          System.out.println(tokenizer.sval);
          break;
        case StreamTokenizer.TT_NUMBER:
          System.out.println(tokenizer.nval);
          break;
        default:
          System.out.println( (char) tokenizer.ttype);
      }
    }

  }

}
