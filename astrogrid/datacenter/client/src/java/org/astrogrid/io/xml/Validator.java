/**
 * Validator.java
 *
 * @author Created by Omnicore CodeGuide
 */

package org.astrogrid.io.xml;

/** Just checks to see if the given XML file is valid, throwing exceptions as usual if not */
import java.io.*;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Validator implements DocumentHandler, ErrorHandler {
   
   PrintWriter out = null;
   
   public Validator(Writer givenOut) {
      this.out = new PrintWriter(givenOut);
   }
   
   /**
    * Receive notification of the beginning of an element.
    */
   public void startElement(String name, AttributeList atts) throws SAXException {
      out.print("Element: <"+name);
      for (int i = 0; i < atts.getLength(); i++) {
         out.print(" "+atts.getName(i)+"='"+atts.getValue(i)+"' ");
      }
      out.println(">");
   }
   
   /**
    * Receive notification of the beginning of a document.
    */
   public void startDocument() throws SAXException {
   }
   
   /**
    * Receive notification of a processing instruction.
    */
   public void processingInstruction(String target, String data) throws SAXException {
   }
   
   /**
    * Receive notification of character data.
    */
   public void characters(char[] ch, int start, int length) throws SAXException {
      if (length>0) {
         out.println("String: '"+new String(ch, start, length)+"'");
      }
   }
   
   /**
    * Receive notification of the end of a document.
    */
   public void endDocument() throws SAXException {
      out.println("--- COMPLETE! ----");
   }
   
   /**
    * Receive notification of a recoverable error.
    */
   public void error(SAXParseException exception) throws SAXException {
      out.println("ERROR: "+exception);
   }
   
   /**
    * Receive notification of the end of an element.
    */
   public void endElement(String name) throws SAXException {
      // TODO
   }
   
   /**
    * Receive notification of a warning.
    */
   public void warning(SAXParseException exception) throws SAXException {
      out.println("WARNING: "+exception);
   }
   
   /**
    * Receive notification of a non-recoverable error.
    */
   public void fatalError(SAXParseException exception) throws SAXException {
      out.println("FATAL: "+exception);
   }
   
   /**
    * Receive notification of ignorable whitespace in element content.
    */
   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
   }
   
   /**
    * Receive an object for locating the origin of SAX document events.
    */
   public void setDocumentLocator(Locator locator) {
   }

   /** A convenience routine for validating any given filename, output going to the given Writer */
   public static void validate(InputStream source, Writer out) throws ParserConfigurationException, SAXException, IOException {
      Validator v = new Validator(out);

      Parser parser;
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);
      SAXParser sp = spf.newSAXParser();
      parser = sp.getParser();

      parser.setDocumentHandler (v);
      parser.setErrorHandler(v);
      parser.parse(new InputSource(source));

      out.flush();
   }

   /**
    *
    */
   public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//    String filename = args[0];
      validate(new FileInputStream("/home/mch/tmp/pal-ssa.metadata.xml"), new OutputStreamWriter(System.out));
      
   }
}

