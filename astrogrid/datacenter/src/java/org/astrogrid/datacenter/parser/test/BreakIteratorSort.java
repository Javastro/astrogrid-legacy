package  org.astrogrid.datacenter.parser.test;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import  org.astrogrid.datacenter.parser.util.*;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class BreakIteratorSort {
  /**
   *
   * @param target String
   * @param wordIterator BreakIterator
   *
   * **THIS IS A TEST CLASS***
   */
  static void extractWords(String target, BreakIterator wordIterator) {

    wordIterator.setText(target);
    int start = wordIterator.first();
    int end = wordIterator.next();

    while (end != BreakIterator.DONE) {
      String word = target.substring(start, end);
      if (Character.isLetterOrDigit(word.charAt(0))) {
        System.out.println(word);
      }
      start = end;
      end = wordIterator.next();
    }
  }

  /**
   *
   * @param target String
   * @param iterator BreakIterator
   */
  static void markBoundaries(String target, BreakIterator iterator) {

    StringBuffer markers = new StringBuffer();
    markers.setLength(target.length() + 1);
    for (int k = 0; k < markers.length(); k++) {
      markers.setCharAt(k, ' ');
    }

    iterator.setText(target);
    int boundary = iterator.first();

    while (boundary != BreakIterator.DONE) {
      markers.setCharAt(boundary, '^');
      boundary = iterator.next();
    }

    System.out.println(target);
    System.out.println(markers);
  }

  /**
   *
   * @param target String
   * @param maxLength int
   * @param currentLocale Locale
   */
  static void formatLines(String target, int maxLength,
                          Locale currentLocale) {

    BreakIterator boundary = BreakIterator.getLineInstance(currentLocale);
    boundary.setText(target);
    int start = boundary.first();
    int end = boundary.next();
    int lineLength = 0;

    while (end != BreakIterator.DONE) {
      String word = target.substring(start, end);
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

  /**
   *
   * @param target String
   * @param iterator BreakIterator
   */
  static void listPositions(String target, BreakIterator iterator) {

    iterator.setText(target);
    int boundary = iterator.first();

    while (boundary != BreakIterator.DONE) {
      System.out.println(boundary);
      boundary = iterator.next();
    }
  }

  /**
   *
   * @throws MalformedURLException
   */
  static void lineExamples() throws MalformedURLException,
      MalformedURLException, IOException {
    Locale currentLocale = new Locale("en", "US");
    BreakIterator lineIterator = BreakIterator.getLineInstance(currentLocale);
    String someText = Transform.LocalFileToString(
        "//home//pedro//public_html//code//sql//select.sql");
    markBoundaries(someText, lineIterator);

  }

  //experiments
  /**
   *
   * @param pos int
   * @param text String
   * @return int
   */
  public static int nextWordStartAfter(int pos, String text) throws
      MalformedURLException, IOException {
    BreakIterator wb = BreakIterator.getWordInstance();
    try {
      wb.setText(Transform.LocalFileToString(
          "//home//pedro//public_html//code//sql//select.sql"));
    }
    catch (MalformedURLException ex) {
    }
    int last = wb.following(pos);
    int current = wb.next();
    while (current != BreakIterator.DONE) {
      for (int p = last; p < current; p++) {
        if (Character.isLetter(text.charAt(p))) {
          return last;
        }
      }
      last = current;
      current = wb.next();
    }
    return BreakIterator.DONE;
  }

  /**
   *
   * @param args String[]
   */
  static public void main(String[] args) throws MalformedURLException,
      IOException {

    try {
      lineExamples();

    }
    catch (MalformedURLException ex) {}
  }

} // class
