/**
 * $Id: Validator.java,v 1.3 2005/11/14 15:04:47 kea Exp $
 *
 */

package org.astrogrid.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.XMLCatalogResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;

/** Parsers a document to see if it is well formed and/or valid.  Throws exceptions
 * when not.  This could eb written as unit test helper, but there are issues then
 * about including unit test code in the main source code. */

public class Validator  {
   
   
   /** A convenience routine for checking that the given stream contains a
    * well-formed XML document - ie it is parsable - but does not check against schema.
    * Throws exceptions if there is a problem. */
   public static void isWellFormed(InputStream xml) throws SAXException, IOException {

      try {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setValidating(false);
         XMLReader parser = new ParserAdapter(spf.newSAXParser().getParser());
   
         parser.parse(new InputSource(xml));
      }
      catch (ParserConfigurationException pce) {
         throw new RuntimeException("JRE not configured correctly; "+pce, pce);
      }
   }

   /** A convenience routine for checking that the given stream contains a
    * valid xml document - ie it conforms to its schema.  Returns null if it
    * is fine, otherwise the error recorder that contains the list of errors/warnings
    */
   public static ErrorRecorder isValid(InputStream xml) throws SAXException, IOException {
      ErrorRecorder recorder = isValidUsingXerces(xml);

      if (recorder.hasErrors() || recorder.hasWarnings()) {
         return recorder;
      }

      return null;
   }
   
   /** This validates the given stream using the JAXP version
    * - ie it uses the parserfactory, however this just seems to give a
    * DTD validator */
   private static ErrorRecorder isValidUsingFactory(InputStream xml) throws SAXException, IOException {

      try {
         // set up catalog so that local schema copies are used instead of remote ones
         // if possible
         String[] catalogs = new String[] { ""+Validator.class.getResource("catalog.xml") };
         XMLCatalogResolver resolver = new XMLCatalogResolver();
         resolver.setPreferPublic(true);
         resolver.setCatalogList(catalogs);
         
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setValidating(true);
         
         XMLReader parser = new ParserAdapter(spf.newSAXParser().getParser());

         parser.setEntityResolver(resolver);
         
         ErrorRecorder recorder = new ErrorRecorder();
         parser.setErrorHandler(recorder);
         parser.parse(new InputSource(xml));
         
         return recorder;
      }
      catch (ParserConfigurationException pce) {
         throw new RuntimeException("JRE not configured correctly; "+pce, pce);
      }

   }

   /** This validates the given stream using the Xerces library.
    * Note that schema validation (rather than DTD validation) is used. 
    * @TOFIX: Work out why the CatalogResolver isn't working.
    */
   private static ErrorRecorder isValidUsingXerces(InputStream xml) throws SAXException, IOException {
      // set up catalog so that local schema copies are used instead of 
      // remote ones if possible (NOTE - actual use disabled below)
      String[] catalogs = new String[] { ""+Validator.class.getResource("catalog.xml") };
      XMLCatalogResolver resolver = new XMLCatalogResolver();
      resolver.setPreferPublic(true);
      resolver.setCatalogList(catalogs);
      
      SAXParser parser = new SAXParser();
      parser.setFeature("http://xml.org/sax/features/validation", true);
      // Use schema (rather than DTD) validation
      parser.setFeature ("http://apache.org/xml/features/validation/schema", 
          true);
      parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true); 

      // Disabled by KEA because it was causing parse failures (couldn't find
      // the schema, I think)
      // parser.setProperty("http://apache.org/xml/properties/internal/entity-resolver", resolver);

      ErrorRecorder recorder = new ErrorRecorder();
      parser.setErrorHandler(recorder);
      parser.parse(new InputSource(xml));

      return recorder;
   }

}
