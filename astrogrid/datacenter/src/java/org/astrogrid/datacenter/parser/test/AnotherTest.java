package org.astrogrid.datacenter.parser.test;

import java.io.*;
import org.astrogrid.datacenter.parser.util.*;

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


public class AnotherTest {

  public static void main(String[] args) throws Exception {

    try {

      System.out.print(Transform.LocalFileToString(
          "//home//pedro//public_html//code//sql//select.sql"));
      // Create the tokenizer to read from a file
      FileReader rd = new FileReader(
          "//home//pedro//public_html//code//sql//select.sql");

      StreamTokenizer st = new StreamTokenizer(rd);

      // Prepare the tokenizer for Java-style tokenizing rules
      st.parseNumbers();
      st.wordChars('_', '_');
      st.eolIsSignificant(true);

      // If whitespace is not to be discarded, make this call

      st.resetSyntax();
      st.parseNumbers();
      //st.eolIsSignificant(false);
      st.wordChars('A', 'Z');
      st.wordChars('a', 'z');
      st.wordChars('\u00A0', '\u00FF');
      st.whitespaceChars('\u0000', '\u0020');
      st.whitespaceChars(0, ' ');
      st.ordinaryChar('.');

      // These calls caused comments to be discarded
      //st.slashSlashComments(true);
      //st.slashStarComments(true);

      // Parse the file
      int token = st.nextToken();
      while (token != StreamTokenizer.TT_EOF) {

        switch (token) {
          case StreamTokenizer.TT_NUMBER:

            // A number was found; the value is in nval
            double num = st.nval;
            System.out.println(num);
            break;
          case StreamTokenizer.TT_WORD:

            // A word was found; the value is in sval
            String word = st.sval;
            System.out.println(word);
            break;
          case '"':

            // A double-quoted string was found; sval contains the contents
            String dquoteVal = st.sval;
            System.out.print(dquoteVal);
            break;
          case '\'':

            // A single-quoted string was found; sval contains the contents
            String squoteVal = st.sval;
            System.out.print(squoteVal);
            break;
          case StreamTokenizer.TT_EOL:

            // End of line character found
            break;
          case StreamTokenizer.TT_EOF:

            // End of file has been reached
            break;
          default:

            // A regular character was found; the value is the token itself
            char ch = (char) st.ttype;
            System.out.print(ch);
            break;
        }
        token = st.nextToken();
      }

    }
    catch (IOException e) {
    }
  }

}
