/*$Id: AstrogridAssert.java,v 1.10 2008/09/03 11:42:46 gtr Exp $
 * Created on 27-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.test;

import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/** class of static JUnit assertion methods, for asserting things relevant to astrogrid.
 * extends XMLAssert - which makes assertions about xml documents - 
 * this class builds upon this to check properties of votables, and assertions to validate against DTD and schema.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Aug-2004
 *
 */
public class AstrogridAssert extends XMLAssert{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AstrogridAssert.class);
    /** Construct a new XMLAssertions
     * 
     */
    private AstrogridAssert() {
        super();
    }
    
    /** static initializer  - finds path to votable dtd.*/
    static {
        VOTABLE_SYSTEM_ID = AstrogridAssert.class.getResource("VOTable.dtd");
    }
    /** system id for votable dtd - i.e. absolute location on classpath of votable dtd */
    public static URL VOTABLE_SYSTEM_ID;
    /** doctype for votable */
    public static final String VOTABLE_DOCTYPE = "VOTABLE";
    
    // assertions for votable.
    /** assert is a votable document 
     * @param s string containing xml.
     * 
     */
    public static void assertVotable(String s)  {
        assertDTDValid(s,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    /** assert is a votable document 
     * @param is input stream containing xml. */
    public static void assertVotable(InputStream is) {
        assertDTDValid(is,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }

    /** assert is a votable document 
     * @param d suspected votable.
     * @asserts doctype, and dtd validates */
    public static void assertVotable(Document d) {
        assertDTDValid(d,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    /** assert is a votable document 
     * @param e suspected votable.
     * @asserts doctype, and dtd validates */    
    public static void assertVotable(Element e) {
        assertDTDValid(e,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    
    // the xml unit validation methods are a bit loosly typed - and poorly named if there's schema validation available too. 
    // these are some wrapper methods with better typing.
    /** assert xml is DTD-valid.
     * @param xml string containing xml
     * @param doctype the DOCTYPE the xml should have (i.e. root element).
     * @param dtdResource url of dtd file.
     * 
     */
    public static void assertDTDValid(String xml,String doctype, URL dtdResource)  {
        try {
            assertXMLValid(xml,dtdResource.toString(),doctype);
        } catch (SAXException e) {
            fail("assertDTDValid: parse failure " + e.getMessage());
        } catch (ParserConfigurationException e) {
            fail("assertDTDValid: parser configuration failure " + e.getMessage());
        }                
    }
    
    /** assert xml is DTD-valid
     * @param d document to validate
     * @param doctype the DOCTYPE that the xml should have (i.e. the root element).
     * @param dtdResource url of dtd file.
     */
    public static void assertDTDValid(Document d,String doctype, URL dtdResource) {
        try {
            assertXMLValid(new Validator(d,dtdResource.toString(),doctype));
        } catch (SAXException e) {
            fail("assertDTDValid: parse failure " + e.getMessage());
        } catch (ParserConfigurationException e) {
            fail("assertDTDValid: parser configuration failure " + e.getMessage());
        }         
    }
    
    /** assert xml is DTD valid.
     * @param xmlStream stream of xml to validate
     * @param doctype the DOCTYPE that the xml should have (ie. the expected root element)
     * @param dtdResource url to dtd file.
     */
    public static void assertDTDValid(InputStream xmlStream, String doctype, URL dtdResource)  {
        try {
            assertXMLValid(new Validator(DomHelper.newDocument(xmlStream),dtdResource.toString(),doctype));
        } catch (SAXException e) {
            fail("assertDTDValid: parse failure " + e.getMessage());
        } catch (ParserConfigurationException e) {
            fail("assertDTDValid: parser configuration failure " + e.getMessage());       
        } catch (IOException e) {
            fail("assertDTDValid: failed to read from stream " + e.getMessage());
        }
    }
    
    /** assert xml is DTD valid
     * @param e xml to validate
     * @param doctype the DOCTYPE that the xml should have (ie. the expected root element)
     * @param dtdResource url to dtd file.
     */
    public static void assertDTDValid(Element e,String doctype,URL dtdResource)  {
        try {
            assertXMLValid(DomHelper.ElementToString(e),dtdResource.toString(),doctype);
        } catch (SAXException e1) {
            fail("assertDTDValid: parse failure " + e1.getMessage());
        } catch (ParserConfigurationException e1) {
            fail("assertDTDValid: parser configuration failure " + e1.getMessage());
        }         
    }
    
    /** assert xml is schema valid.
     * @param xml string containing xml to validate.
     * @param rootElementName expected localname of the root element of the document 
     * @param schemaLocations map of namespace - schema location associations.<br>
     *  keys of map expected to be namespace (will be converted to java.lang.String via <tt>toString()</tt>), <br>
     * values of map expected to be url locations of schema document (will be converted to java.lang.String via <tt>toString()</tt>)
     * 
     * @see org.astrogrid.test.schema.SchemaMap  in workflow-objects project
     */
    public static void assertSchemaValid(String xml,String rootElementName,Map schemaLocations) {
        try {
            assertXpathEvaluatesTo(rootElementName,"local-name(/*)",xml);
        } catch (Exception e) {
            fail("Failed to extract root element name " + e.getMessage());
        }
        AstrogridAssertDefaultHandler handler = new AstrogridAssertDefaultHandler();
        XMLReader  parser = createParser(handler,schemaLocations);
        InputSource source = new InputSource(new StringReader(xml));
        try {
            parser.parse(source);
        } catch (Exception e) {
            fail("failed to validate against schema: " + e.getMessage() + handler.getMessages());
        }

    }
    
    /**assert xml is schema valid.
     * @param d document to validate
     * @param rootElementName expected localname of the root element of the document
     * @param schemaLocations map of namespace - schema location associations.<br>
     *  keys of map expected to be namespace (will be converted to java.lang.String via <tt>toString()</tt>), <br>
     * values of map expected to be url locations of schema document (will be converted to java.lang.String via <tt>toString()</tt>)
     * @see org.astrogrid.test.schema.SchemaMap  in workflow-objects project     * 
     */
    public static void assertSchemaValid(Document d,String rootElementName,Map schemaLocations) {
        assertSchemaValid(DomHelper.DocumentToString(d),rootElementName,schemaLocations);       
    }
    
    /**assert xml is schema valid.
     * @param is stream of xml to validate
     * @param rootElementName expected localname of the root element of the document
     * @param schemaLocations map of namespace - schema location associations.<br>
     *  keys of map expected to be namespace (will be converted to java.lang.String via <tt>toString()</tt>), <br>
     * values of map expected to be url locations of schema document (will be converted to java.lang.String via <tt>toString()</tt>)
     *     * @see org.astrogrid.test.schema.SchemaMap  in workflow-objects project 
     */
    public static void assertSchemaValid(InputStream is,String rootElementName,Map schemaLocations){ 
       
        try {
            assertSchemaValid(getStreamContents(is),rootElementName,schemaLocations);
        } catch (IOException e) {
            fail("Failed to read from stream " + e.getMessage());
        }
    }
    

    /**assert xml is schema valid.
     * @param e element to validate
     * @param rootElementName expected localname of the root element of the document
     * @param schemaLocations map of namespace - schema location associations.<br>
     *  keys of map expected to be namespace (will be converted to java.lang.String via <tt>toString()</tt>), <br>
     * values of map expected to be url locations of schema document (will be converted to java.lang.String via <tt>toString()</tt>)
     * @see org.astrogrid.test.schema.SchemaMap  in workflow-objects project     * 
     */
    public static void assertSchemaValid(Element e,String rootElementName,Map schemaLocations) {
        assertSchemaValid(DomHelper.ElementToString(e),rootElementName,schemaLocations);
    }


    
    
    /** create an xml parser, setup to validate using schema, register 
     * appropriate schema locations, and an error handler 
     * @param handler - handler to use
     * @param schemaLocations
     * @return
     */
    private static XMLReader createParser(DefaultHandler handler,Map schemaLocations)  {
        
        String locationString = mkSchemaLocationString(schemaLocations);
        try {

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature("http://xml.org/sax/features/validation",true);
            reader.setFeature("http://apache.org/xml/features/validation/schema",true);
            reader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",locationString);
            reader.setErrorHandler(handler);
            reader.setContentHandler(handler);
            return reader;
        } catch (SAXNotRecognizedException e) {
            fail("required features not rcognized by this xml parser " + e.getMessage());
        } catch (SAXNotSupportedException e) {
            fail("required features not supported by this xml parser " + e.getMessage());
        } catch (SAXException e) {
            fail("cannot create xml reader " + e.getMessage());
        }
        throw new IllegalStateException("Cannot be reached");

        
    }
    /** collapse the map into a space-separated string, suitable for passing to the parser */
    private static String mkSchemaLocationString(Map schemaLocations) {
        StringBuffer result = new StringBuffer();
        for (Iterator i = schemaLocations.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            result.append(e.getKey().toString());
            result.append(' ');
            result.append(e.getValue().toString());
            result.append(' ');
        }
        return result.toString();
    }
    
    // the basics -assert something is xml.
    /** assert that input is well formed xml
     * @param xmlDocument document to check for wellformedness.
     * 
     */
    public static void assertWellFormedXML(String xmlDocument) {
        try {
            assertNotNull("null document returned",DomHelper.newDocument(xmlDocument));
        } catch (ParserConfigurationException e) {
            Assert.fail("Problem with parser:" + e.getMessage());
        } catch (SAXException e) {
            Assert.fail("xmlDocument is not well formed:" + e.getMessage());            
        } catch (IOException e) {
            Assert.fail("Problem reading document:" + e.getMessage());
        }
    }
    /** assert that input is well formed xml
     * @param xmlStream stream of content to check.
     */
    public static void assertWellFormedXML(InputStream xmlStream) {
        try {
            assertNotNull("null document returned",DomHelper.newDocument(xmlStream));
        } catch (ParserConfigurationException e) {
            Assert.fail("Problem with parser:" + e.getMessage());
        } catch (SAXException e) {
            Assert.fail("xmlDocument is not well formed:" + e.getMessage());            
        } catch (IOException e) {
            Assert.fail("Problem reading document:" + e.getMessage());
        }        
    }
    
    
    // helper methods.
    /**
     * load a resource into a string.
     * @asserts resource can be found, and is not null
     * @param c class that resorce path is relative to
     * @param resource path to resurce
     * @return comtents of this resource.
     * @throws IOException
     */
    public static String getResourceAsString(Class c, String resource) throws IOException {
       InputStream is = c.getResourceAsStream(resource);
       String script = AstrogridAssert.getStreamContents(is);
       assertNotNull(script);
       return script;
    }

    /** read stream contents into a string */
    public static String getStreamContents(InputStream is) throws IOException {
       assertNotNull(is);
       StringWriter sw = new StringWriter();
       Piper.bufferedPipe(new InputStreamReader(is), sw);
       return sw.toString();
    }    
    /** handler passed to schema-validation parses - logs all errors, throws assertion failed if errors seen by end */
    static class AstrogridAssertDefaultHandler extends DefaultHandler {
      private StringBuffer buff = new StringBuffer();
      
      public String getMessages() {
        return buff.toString();
      }
            
      boolean sawError = false;
      
      public void endDocument() throws SAXException {
        if (sawError) {
          throw new AssertionFailedError("The document is invalid with respect to its schema" 
                                         + this.getMessages());
        }
      }

      public void error(SAXParseException e) throws SAXException {
        sawError = true;
        System.err.println("Error:" + this.getMessage(e));
        this.buff.append("\n").append(this.getMessage(e));
      }
      
      public void fatalError(SAXParseException e) throws SAXException {
        sawError = true;
        System.err.println("Fatal: " + this.getMessage(e));
        this.buff.append("\n").append(this.getMessage(e));            
      }
      
      public void warning(SAXParseException e) throws SAXException {
        System.err.println("Warn: " + this.getMessage(e));
        this.buff.append("\n").append(this.getMessage(e));           
      }
      
      private String getMessage(SAXParseException e) {
        return e.getMessage() +
               " Found at line " +
               e.getLineNumber() +
               ", column " +
               e.getColumnNumber() +
               " in " +
               e.getSystemId();
      }
    }
    
}


/* 
$Log: AstrogridAssert.java,v $
Revision 1.10  2008/09/03 11:42:46  gtr
I removed an explicit reference to xerces.

Revision 1.9  2006/10/16 14:38:37  clq2
common-gtr-1933

Revision 1.8.116.1  2006/10/13 17:48:29  gtr
Error reports in schema validation now report the line and column numbers where the problems are found.

Revision 1.8  2004/11/19 09:36:27  clq2
common_nww_712

Revision 1.7.42.1  2004/11/18 15:25:12  nw
schema assertions that fail to validate report parse errors.

Revision 1.7  2004/09/03 09:18:03  nw
got schema validation asserts working.

Revision 1.6  2004/09/02 11:25:25  nw
got schema validation working.

Revision 1.5  2004/09/02 10:14:00  nw
improved documentation

Revision 1.4  2004/09/02 09:55:53  nw
fixed bug.

Revision 1.3  2004/09/02 01:32:17  nw
added in assertDTDValid() and assertSchemaValid() methods.

Revision 1.2  2004/08/27 12:48:08  nw
started class of static assertions relevant to astrogird - assertVotable() for starters.
Also provides methods to check format of xml documents.

Revision 1.1  2004/08/27 12:42:58  nw
started class of static assertions relevant to astrogird - assertVotable() for starters.
Also provides methods to check format of xml documents.
 
*/