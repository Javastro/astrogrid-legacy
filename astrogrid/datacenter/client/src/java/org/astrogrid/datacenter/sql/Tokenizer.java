package parser;package  org.astrogrid.datacenter.sql;

import java.io.*;
import java.util.*;
import java.net.*;
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

class  Tokenizer{
  public Tokenizer(){}
  //private static StreamTokenizer stoken;

  public static int[] wordCounter(String sqlFile) throws IOException {
    int tokenType[] = new int[3];
    int stringCounter = 0;
    int charCounter = 0;
    int numberCounter = 0;
    //StringTokenizer st = new StringTokenizer(sqlFile);
    InputStream in = new StringBufferInputStream( sqlFile );
    StreamTokenizer parser = new StreamTokenizer( in );

    ///test
    parser.resetSyntax();
    parser.parseNumbers();
    parser.eolIsSignificant(false);
    parser.wordChars('A', 'Z');
    parser.wordChars('a', 'z');
    parser.wordChars('\u00A0', '\u00FF');
    parser.whitespaceChars('\u0000', '\u0020');
    parser.whitespaceChars(0, ' ');
    parser.ordinaryChar('.');
    ///test

    while ( parser.nextToken() != StreamTokenizer.TT_EOF )
    {
       if ( parser.ttype == StreamTokenizer.TT_WORD)
         stringCounter++;
       else if  ( parser.ttype == StreamTokenizer.TT_NUMBER )
         numberCounter++;
       else if  ( parser.ttype == StreamTokenizer.TT_EOL)
         stringCounter++;
       else
         charCounter++;
    }
    tokenType[0] = (stringCounter);
    tokenType[1] = (charCounter);
    tokenType[2] = (numberCounter);

    return (tokenType);
 }

 public static void main( String args[] ) throws Exception {
   //File which is converted to a String
   String file = "D://httpd//files//sql";
   String sqlFile = Transform.LocalFileToString(file);

   int s = 0;
   int c = 0;
   int n = 0;
   int correlative = 0;
   int arrayTypeLenght[] = wordCounter(sqlFile);
   int stringCounter = arrayTypeLenght[0];
   int charCounter = arrayTypeLenght[1];
   int numberCounter = arrayTypeLenght[2];
   int totalToken = stringCounter + charCounter + numberCounter;

   InputStream in = new StringBufferInputStream(sqlFile);
   StreamTokenizer parser = new StreamTokenizer(in);

   //print out some info this could be deleted in final version
   System.out.println(sqlFile);
   System.out.println("StringT : " + stringCounter);
   System.out.println("CharT : " + charCounter);
   System.out.println("numberT : " + numberCounter);
   System.out.println("totalT : " + totalToken);
   //end of print out

   //Matrix declaration
   String[] stringType = new String[stringCounter];
   char[] charType = new char[charCounter];
   double[] numberType = new double[numberCounter];
   //field 1 = correlativeIndex; field 2 = type; field 3 = positionTypeMatrix
   //type : s = String ; c =char ; n = number
   String[][] realPosition = new String[totalToken][2];

   //tokens
   parser.resetSyntax();
   parser.parseNumbers();
   parser.eolIsSignificant(false);
   parser.wordChars('A', 'Z');
   parser.wordChars('a', 'z');
   parser.wordChars('\u00A0', '\u00FF');
   parser.whitespaceChars('\u0000', '\u0020');
   parser.whitespaceChars(0, ' ');
   parser.ordinaryChar('.');
   //endTokens

   ///matching tokens in the matrix
   int i = 0;
   int j = 0;
   while (parser.nextToken() != StreamTokenizer.TT_EOF) {
     if (parser.ttype == StreamTokenizer.TT_WORD) {
       correlative++;
       stringType[s] = (parser.sval);
       s++;
       //realPosition[i,2] = {"s",s};
       j++;
     }
     else if (parser.ttype == StreamTokenizer.TT_NUMBER) {
       correlative++;
       numberType[n] = (parser.nval);
       n++;
     }
     else if (parser.ttype == StreamTokenizer.TT_EOL) {
       correlative++;
       stringType[s] = "EOL";
       s++;
     }
     else {
       charType[c] = (char)(parser.ttype);
       c++;
       correlative++;
     }
   }

   for (int k = 0; k < stringType.length; k++) {
     System.out.println(stringType[k]);
   }
   for (int k = 0; k < charType.length; k++) {
     System.out.println(charType[k]);
   }
   for (int k = 0; k < numberType.length; k++) {
     System.out.println(numberType[k]);
   }
 }
}
