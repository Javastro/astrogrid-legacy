/*$Id: AstrogridAssert.java,v 1.4 2004/09/02 09:55:53 nw Exp $
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

import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import junit.framework.Assert;

/** class of static JUnit assertion methods, for asserting things relevant to astrogrid.
 * extends XMLAssert - which makes assertions about xml documents - 
 * this class builds upon this to check properties of votables, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Aug-2004
 *
 */
public class AstrogridAssert extends XMLAssert{

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
    public static final URL VOTABLE_SYSTEM_ID;
    /** doctype for votable */
    public static final String VOTABLE_DOCTYPE = "VOTABLE";
    
    // assertions for votable.
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(String s)  {
        assertDTDValid(s,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(InputStream is) {
        assertDTDValid(is,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }

    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(Document d) {
        assertDTDValid(d,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */    
    public static void assertVotable(Element e) {
        assertDTDValid(e,VOTABLE_DOCTYPE,VOTABLE_SYSTEM_ID);
    }
    
    // the xml unit validation methods are a bit loosly typed - and poorly named if there's schema validation available too. 
    // these are some wrapper methods with better typing.
    public static void assertDTDValid(String xml,String doctype, URL dtdResource)  {
        try {
            assertXMLValid(xml,dtdResource.toString(),doctype);
        } catch (SAXException e) {
            fail("assertDTDValid: parse failure " + e.getMessage());
        } catch (ParserConfigurationException e) {
            fail("assertDTDValid: parser configuration failure " + e.getMessage());
        }                
    }
    
    public static void assertDTDValid(Document d,String doctype, URL dtdResource) {
        try {
            assertXMLValid(new Validator(d,dtdResource.toString(),doctype));
        } catch (SAXException e) {
            fail("assertDTDValid: parse failure " + e.getMessage());
        } catch (ParserConfigurationException e) {
            fail("assertDTDValid: parser configuration failure " + e.getMessage());
        }         
    }
    
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
    
    public static void assertDTDValid(Element e,String doctype,URL dtdResource)  {
        try {
            assertXMLValid(DomHelper.ElementToString(e),dtdResource.toString(),doctype);
        } catch (SAXException e1) {
            fail("assertDTDValid: parse failure " + e1.getMessage());
        } catch (ParserConfigurationException e1) {
            fail("assertDTDValid: parser configuration failure " + e1.getMessage());
        }         
    }
    
    // assert a document is schema-valid. need to pass in a map defining namespace - schema location mappings.
    public static void assertSchemaValid(String xml,String rootElementName,Map schemaLocations) {
        try {
            assertXpathEvaluatesTo(rootElementName,"local-name(/node())",xml);
        } catch (Exception e) {
            fail("Failed to extract root element name " + e.getMessage());
        }
        XMLReader  parser = createParser(schemaLocations);
        InputSource source = new InputSource(new StringReader(xml));
        try {
            parser.parse(source);
        } catch (Exception e) {
            fail("failed to validate against schema: " + e.getMessage());
        }
    }
    
    public static void assertSchemaValid(Document d,String rootElementName,Map schemaLocations) {
        assertSchemaValid(DomHelper.DocumentToString(d),rootElementName,schemaLocations);       
    }
    
    public static void assertSchemaValid(InputStream is,String rootElementName,Map schemaLocations){ 
       
        try {
            assertSchemaValid(getStreamContents(is),rootElementName,schemaLocations);
        } catch (IOException e) {
            fail("Failed to read from stream " + e.getMessage());
        }
    }
    

    public static void assertSchemaValid(Element e,String rootElementName,Map schemaLocations) {
        assertSchemaValid(DomHelper.ElementToString(e),rootElementName,schemaLocations);
    }
    
    private static XMLReader createParser(Map schemaLocations)  {
        
        String locationString = mkSchemaLocationString(schemaLocations);
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();            
            reader.setFeature("http://xml.org/sax/features/validation",true);
            reader.setFeature("http://apache.org/xml/features/validation/schema",true);
            reader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",locationString);                
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
     * 
     * @param is stream of content to check.
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

}


/* 
$Log: AstrogridAssert.java,v $
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