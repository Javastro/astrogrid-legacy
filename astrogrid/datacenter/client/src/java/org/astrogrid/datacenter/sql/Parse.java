package  org.astrogrid.datacenter.sql;

import java.io.*;
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
public class Parse {
 // default constructor
 public Parse() {}

 private static StreamTokenizer stoken;
  /**
  *
  * @param args String[]
  * @throws Exception
  */
 public static void main( String args[] ) throws Exception {
   String filePath = "D://httpd//files//sql";
   String fileUrlPath = "http:\\143.117.59.17/~pedro/code/sql/select.sql";
   String word;
   double number;
   try {
     stoken = new StreamTokenizer(new InputStreamReader(new FileInputStream(filePath)));
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

   int theChar = stoken.nextToken();
   System.out.println(Transform.LocalFileToString(filePath));

   while (theChar != StreamTokenizer.TT_EOF) {
     switch (theChar) {
       case StreamTokenizer.TT_EOL: // If we find an EOL, just print a new-line char
         System.out.println();
         break;
       case StreamTokenizer.TT_WORD: // If we find a word, print it and a space
         word = stoken.sval;
         System.out.println(word);
         Compare.




         break;
       case StreamTokenizer.TT_NUMBER: // If we find a number, print it and a space
         number = stoken.nval;
         System.out.println(number);
         break;
      default: // Otherwise, we must have an ordinary character, which we just print.
        System.out.println((char)theChar);
     }
     theChar = stoken.nextToken();
   }
 }
}
