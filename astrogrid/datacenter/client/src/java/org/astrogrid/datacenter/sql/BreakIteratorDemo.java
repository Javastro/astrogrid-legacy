package  org.astrogrid.datacenter.sql;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

import java.util.*;
import java.text.*;
import java.net.*;

public class BreakIteratorDemo  {

   static void extractWords(String target, BreakIterator wordIterator) {

      wordIterator.setText(target);
      int start = wordIterator.first();
      int end = wordIterator.next();

      while (end != BreakIterator.DONE) {
         String word = target.substring(start,end);
         if (Character.isLetterOrDigit(word.charAt(0))) {
            System.out.println(word);
         }
         start = end;
         end = wordIterator.next();
      }
   }

   static void markBoundaries(String target, BreakIterator iterator) {

      StringBuffer markers = new StringBuffer();
      markers.setLength(target.length() + 1);
      for (int k = 0; k < markers.length(); k++) {
         markers.setCharAt(k,' ');
      }

      iterator.setText(target);
      int boundary = iterator.first();

      while (boundary != BreakIterator.DONE) {
         markers.setCharAt(boundary,'^');
         boundary = iterator.next();
      }

      System.out.println(target);
      System.out.println(markers);
   }


   static void formatLines(String target, int maxLength,
                           Locale currentLocale) {

      BreakIterator boundary = BreakIterator.getLineInstance(currentLocale);
      boundary.setText(target);
      int start = boundary.first();
      int end = boundary.next();
      int lineLength = 0;

      while (end != BreakIterator.DONE) {
         String word = target.substring(start,end);
         lineLength = lineLength + word.length();
         if (lineLength >= maxLength) {
            System.out.println();
            lineLength = word.length();
         }
         System.out.print(word);
         start = end;
         end = boundary.next();
      }
   }

   static void listPositions(String target, BreakIterator iterator) {

      iterator.setText(target);
      int boundary = iterator.first();

      while (boundary != BreakIterator.DONE) {
         System.out.println (boundary);
         boundary = iterator.next();
      }
   }

   static void lineExamples() throws MalformedURLException  {
      Locale currentLocale = new Locale ("en","US");
      BreakIterator lineIterator = BreakIterator.getLineInstance(currentLocale);
      String someText = Transform.LocalFileToString("D://httpd//files//sql");
      markBoundaries(someText, lineIterator);

   }

   static public void main(String[] args) {

    try {
      lineExamples();
    }
    catch (MalformedURLException ex) {}
    }

}
