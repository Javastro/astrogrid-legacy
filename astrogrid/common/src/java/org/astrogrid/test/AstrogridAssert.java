/*$Id: AstrogridAssert.java,v 1.2 2004/08/27 12:48:08 nw Exp $
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
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
        URL votable = AstrogridAssert.class.getResource("VOTable.dtd");
        VOTABLE_SYSTEM_ID = votable.toString();
    }
    /** system id for votable dtd - i.e. absolute location on classpath of votable dtd */
    public static final String VOTABLE_SYSTEM_ID;
    /** doctype for votable */
    public static final String VOTABLE_DOCTYPE = "VOTABLE";
    
    // assertions for votable.
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(String s) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        assertXpathExists("/" + VOTABLE_DOCTYPE,s);
        assertXMLValid(s,VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE);
    }
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(InputStream is) throws SAXException, IOException, TransformerException, ParserConfigurationException{
        Document d = DomHelper.newDocument(is);
        assertVotable(d);
    }

    /** assert is a votable document 
     * @asserts doctype, and dtd validates */
    public static void assertVotable(Document d) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        assertXpathExists("/" + VOTABLE_DOCTYPE,d);
        assertXMLValid(new Validator(d,VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE));
    }
    /** assert is a votable document 
     * @asserts doctype, and dtd validates */    
    public static void assertVotable(Element e) throws SAXException, ParserConfigurationException {
        assertEquals(VOTABLE_DOCTYPE,e.getLocalName());
        assertXMLValid(DomHelper.ElementToString(e),VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE);
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
Revision 1.2  2004/08/27 12:48:08  nw
started class of static assertions relevant to astrogird - assertVotable() for starters.
Also provides methods to check format of xml documents.

Revision 1.1  2004/08/27 12:42:58  nw
started class of static assertions relevant to astrogird - assertVotable() for starters.
Also provides methods to check format of xml documents.
 
*/