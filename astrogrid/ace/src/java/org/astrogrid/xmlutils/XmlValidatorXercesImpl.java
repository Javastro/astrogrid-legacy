//-------------------------------------------------------------------------
// FILE: XmlValidatorXercesImpl.java
// PACKAGE: org.astrogrid.xmlutils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 16/10/02   KEA       Initial prototype
// 02/12/02   KEA       Update for supporting feature-setting, and
//   reporting of Schema validation errors (rather than just well-
//   formedness errors) using Exceptions.
//-------------------------------------------------------------------------

package org.astrogrid.xmlutils;

// FOR XERCES PARSER
import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.Reader;

import java.util.Map;
import java.util.Iterator;

import org.astrogrid.xmlutils.XmlValidatorIfc;

import org.astrogrid.log.Log;

/**
 * <p>Implementation class for syntactic and semantic validation of XML
 * input documents, based on the Xerces Java XML parser.
 * <p>See Validate.java for a command-line harness.
 *
 * <p> See XmlValidatorIfc documentation for usage example with a
 * namespace map.
 *
 *
 * <p>TO DO:  <ul>
 * <li>In schema-validation mode, the Xerces parser can produce pretty
 * cryptic parse-error messages - is there an alternative parser we
 * can use?  XML4j seems not much better (v. similar to Xerces).
 * <li>Nicer / more informative exception-throwing?
 * </ul>
 *
 *
 * @see org.astrogrid.xmlutils.XmlValidatorIfc
 * @see org.astrogrid.xmlutils.Validate
 * @see org.apache.xerces.parsers.SAXParser
 * @see org.xml.sax.helpers.DefaultHandler
 *
 * @author Kona Andrews,
 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>
 * @version 1.0
 *
 *
 * (c) Copyright Astrogrid 2002; all rights reserved.
 * See http://www.astrogrid.org/code_licence.html for terms of usage.
 */
public class XmlValidatorXercesImpl extends DefaultHandler
{

   /**
    * Configuration flag: If true, deeply validate referenced schema,
    * as well as referencing XML document.
    * Defaults to false.
    */
   protected boolean validateSchema = false;

   /**
    * Configuration flag: If true, don't print parse warnings to stderr -
    * silently pass over them.
    * Defaults to false.
    */
   protected boolean suppressWarnings = false;

   /**
    * Configuration flag: If true, use a namespace-aware parser.
    * Defaults to true.
    */
   protected boolean useNamespaces = true;

   /**
    * Internal flag: Validate using supplied namespace map.
    */
   private boolean useNamespaceMap = false;

   /**
    * Namespace map (namespaces -> schema locations) supplied by
    * the user when validate() method is called.  If present,
    * validation of XML document will use schema at location
    * specified for that document's namespace.  If the namespace
    * in the document is not found in the map, a validation error
    * will be generated.
    */
   private Map currNamespaceMap;

   /**
    * Internal flag used for checking document namespace during SAX parsing.
    */
   private boolean isRoot;


   /**
    * Dummy constructor - does nothing.
    */
   public XmlValidatorXercesImpl()
   {
   }


   /**
    * Syntactically and semantically validates an input XML file
    * against its schema(s), the location of which must be specified
    * in the <tt>xsi:schemalocation</tt> attribute of the toplevel
    * document element.
    *
    * @param reader  A (pre-initialised) reader for the input XML.
    *
    * @throws org.xml.sax.SAXException if input document is invalid.
    */
   public void validate(Reader reader) throws SAXException, IOException
   {
      useNamespaceMap = false;
      currNamespaceMap = null;
      parse(new InputSource(reader));
   }

   /**
    * Syntactically and semantically validates an input XML file
    * against its schema(s), the location of which must be specified
    * in the supplied Map matching namespaces to schema locations.
    *
    * @param reader  A (pre-initialised) reader for the input XML.
    * @param namespaceMap  A Map type containing string:string pairs,
    * the key being a namespace URI, the value being the location of
    * the schema for that namespace.
    *
    * @throws org.xml.sax.SAXException if input document is invalid.
    */
   public void validate(Reader reader, Map namespaceMap) throws SAXException,IOException
   {
      useNamespaceMap = true;
      currNamespaceMap = namespaceMap;
      parse(new InputSource(reader));
   }


   /**
    * Syntactically and semantically validates an input XML file
    * against its schema(s), the location of which must be specified
    * in the <tt>xsi:schemalocation</tt> attribute of the toplevel
    * document element.
    *
    * @param systemResource  The specified system resource from which to
    * read the input XML (e.g. a local filename).
    *
    * @throws org.xml.sax.SAXException if input document is invalid.
    */
   public void validate(String systemResource) throws SAXException, IOException
   {
      useNamespaceMap = false;
      currNamespaceMap = null;
      parse(new InputSource(systemResource));
   }

   /**
    * Syntactically and semantically validates an input XML file
    * against its schema, the location of which must be specified
    * in the supplied Map matching namespaces to schema locations.
    *
    * @param systemResource  The specified system resource from which to
    * read the input XML (e.g. a local filename).
    *
    * @param namespaceMap  A Map type containing string:string pairs,
    * the key being a namespace URI, the value being the location of
    * the schema for that namespace.
    *
    * @throws org.xml.sax.SAXException if input document is invalid.
    */
   public void validate(String systemResource, Map namespaceMap)
         throws SAXException, IOException
   {
      useNamespaceMap = true;
      currNamespaceMap = namespaceMap;
      parse(new InputSource(systemResource));
   }


   /**
    * Allows named features to be switched on or off.
    *
    * @param feature  A string identifying a feature by name.
    * @param value  The boolean value to which the named feature
    * should be set.
    *
    * @throws  org.xml.sax.SAXNotRecognizedException if the named
    * feature is not recognised.
    */
   public void setFeature(String feature, boolean val)
         throws org.xml.sax.SAXNotRecognizedException
   {
      if (feature.equals("ValidateSchema"))
      {
         validateSchema = val;
      }
      else if (feature.equals("SuppressWarnings"))
      {
         suppressWarnings = val;
      }
      else if (feature.equals("UseNamespaces"))
      {
         useNamespaces = val;
      }
      else
      {
         throw new SAXNotRecognizedException(
               "Unrecognised validator feature " + feature);
      }
   }


   /**
    * Allows named features to be interrogated for their current value.
    *
    * @param feature  A string identifying a feature by name.
    *
    * @return  The current boolean value of the named feature.
    *
    * @throws  org.xml.sax.SAXNotRecognizedException if the named
    * feature is not recognised.
    */
   public boolean getFeature(String feature)
         throws org.xml.sax.SAXNotRecognizedException
   {
      if (feature.equals("ValidateSchema"))
      {
         return validateSchema;
      }
      else if (feature.equals("SuppressWarnings"))
      {
         return suppressWarnings;
      }
      else if (feature.equals("UseNamespaces"))
      {
         return useNamespaces;
      }
      else
      {
         throw new SAXNotRecognizedException(
               "Unrecognised validator feature " + feature);
      }
   }


   /**
    * Element-handling function supplied to parser.
    * Only invoked when a namespace map is in use.
    * Checks the root document element to ensure that the
    * document namespace URI is a known namespace according
    * to the current map.
    *
    * @param e   An exception containing the warning.
    *
    * @throws  org.xml.sax.SAXException when the document namespace is not
    *   found in the current namespace map.
    */
   public void startElement(
               String namespaceURI,
               String localName,
               String qName,
               Attributes atts)
               throws SAXException
   {
      // Only need to check root element - want document namespace
      //
      if (isRoot)
      {
         isRoot = false;   // No more checks after this one
         boolean goodNamespace = false;
         Iterator iterator;
         for (iterator = currNamespaceMap.entrySet().iterator();
               iterator.hasNext(); )
         {
            Map.Entry me = (Map.Entry)iterator.next();
            String namespace = (String)me.getKey();
            if (namespaceURI.equals(namespace))
            {
               goodNamespace = true;
            }
         }
         if (!goodNamespace)
         {
            throw new SAXException(
            "\nThe document does not validate successfully: \n"
            + "- Unrecognised document namespace " + namespaceURI);
         }
      }
   }


   /**
    * Warning-handling function supplied to parser.
    * Either prints warning to stderr or silently ignores exception,
    * depending on value of suppressWarnings flag.
    * Never actually raises an exception.
    *
    * @param e   An exception containing the warning.
    *
    * @throws  org.xml.sax.SAXException
    */
   public void warning(SAXParseException e) throws SAXException
   {
      if (!suppressWarnings)
      {
         org.astrogrid.log.Log.logWarning(null, "Parse warning", e);
      }
   }


   /**
    * Error-handling function supplied to parser.
    * Always raises a SAXException describing the error.
    *
    * @param e   An exception containing the error.
    *
    * @throws  org.xml.sax.SAXException (always)
    */
   public void error(SAXParseException e) throws SAXException
   {
      throw new SAXException(
         "Document contains invalid XML: \n"
               + "SAXParseException: "+ e.getMessage());
   }


   /**
    * Fatal error-handling function supplied to parser.
    * Always raises a SAXException describing the fatal error.
    *
    * @param e   An exception containing the fatal error.
    *
    * @throws  org.xml.sax.SAXException (always)
    */
   public void fatalError(SAXParseException e) throws SAXException
   {
      throw new SAXException(
         "Document contains an error that stops validation: \n"
               + "SAXParseException: "+ e.getMessage());
   }


   /**
    * Provides the actual functionality to syntactically and semantically
    * validate an input XML file against its schema(s), using the
    * Xerces Java XML parser.
    *
    * @param inputSource A (pre-initialised) SAX input source for the
    *    input XML.
    *
    * @throws org.xml.sax.SAXException if input document is invalid.
    */
   protected void parse(InputSource inputSource) throws SAXException, IOException
   {
      Log.trace("Parsing "+inputSource.getPublicId()+" ("+inputSource.getSystemId()+")...");

      // Create a Xerces SAX Parser
      //
      SAXParser parser = new SAXParser();
      parser.setErrorHandler(this);

      // Do we need to validate the document namespace?
      // If so, we need our custom element handler
      //
      if (useNamespaceMap)
      {
         parser.setContentHandler(this);
      }

      // Turn requested features on
      //
      try
      {
         // Switch validation on
         //
         parser.setFeature(
            "http://xml.org/sax/features/validation", true);

         // Support full Schema validation (not just DTD)
         //
         parser.setFeature(
            "http://apache.org/xml/features/validation/schema", true);

         if (validateSchema)
         {
            // Validate the schema itself in detail
            //
            parser.setFeature(
               "http://apache.org/xml/features/validation/schema-full-checking",
               true);
         }

         // Switch on namespaces if required
         //
         if (useNamespaces)
         {
            parser.setFeature("http://xml.org/sax/features/namespaces", true);

            // Use known namespace and schema if this has been requested
            //
            if (useNamespaceMap)
            {
               String canonicalSchemas = "";
               Iterator iterator;
               for (iterator = currNamespaceMap.entrySet().iterator();
                     iterator.hasNext(); )
               {
                  Map.Entry me = (Map.Entry)iterator.next();
                  canonicalSchemas = canonicalSchemas
                        + (String)me.getKey() + " "
                        + (String)me.getValue() + " ";
               }
               parser.setProperty(
                     "http://apache.org/xml/properties/schema/external-schemaLocation",
                     canonicalSchemas);
            }
         }
         else
         {
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
         }

         // This one only reports schema errors if a schema is given
         // (if "false" (default), if no schema is referenced in the XML
         // document then an error is raised.)  Do we want this?
         //
         /*
         parser.setFeature(
               "http://apache.org/xml/features/validation/dynamic", true);
         */

      }
      catch (SAXNotRecognizedException e)
      {
         throw new SAXException(
               "\nThis parser does not recognise a requested feature: \n"
                  + "SAXNotRecognizedException: "+ e.getMessage());
      }
      catch (SAXNotSupportedException e)
      {
         throw new SAXException(
            "\nThis parser does not support a requested feature: \n"
                  + "SAXNotSupportedException: "+ e.getMessage());
      }

      // Parse the document (thereby validating it)
      //
//    try let exceptions propogate up
//    {
      isRoot = true;
      parser.parse(inputSource);
//    }
//    catch (IOException e)
//    {
//       throw new SAXException(
//          "\nThe document does not validate successfully: \n"
//                + "IOException: "+ e.getMessage());
//    }
   }
}
//-------------------------------------------------------------------------
