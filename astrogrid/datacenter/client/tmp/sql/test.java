package  org.astrogrid.datacenter.sql;
/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */


import java.io.StreamTokenizer;
import java.io.InputStreamReader;
import java.io.FileInputStream;


public class test {

    public static void main(String[] args) throws Exception {

      String filePath = "D://httpd//files//sql";

      StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(new FileInputStream(
          filePath)));

      System.out.print(Transform.LocalFileToString("filePath"));


      tokenizer.resetSyntax();
      tokenizer.parseNumbers();
      tokenizer.eolIsSignificant(false);
      tokenizer.wordChars('A', 'Z');
      tokenizer.wordChars('a', 'z');
      tokenizer.wordChars('\u00A0', '\u00FF');
      tokenizer.whitespaceChars('\u0000', '\u0020');
      tokenizer.whitespaceChars(0, ' ');
      tokenizer.ordinaryChar('.');

      while (tokenizer.nextToken()  != StreamTokenizer.TT_EOF) {

          switch (tokenizer.ttype) {
          case StreamTokenizer.TT_EOF:
              System.out.println("EOF");
              break;
          case StreamTokenizer.TT_EOL:
              System.out.println("EOL");
              break;
          case StreamTokenizer.TT_NUMBER:
              System.out.println(tokenizer.nval);
              break;
          case StreamTokenizer.TT_WORD:
              System.out.println(tokenizer.sval);
              break;
          default:
              System.out.println((char)tokenizer.ttype);
          }
      }


    }

}
