/*$Id: ServerTestCase.java,v 1.6 2004/03/12 04:54:06 mch Exp $
 * Created on 20-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter;

import java.io.*;

import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/** Base test case class - contains a collection of useful assertion methods, etc.
 *Extend from this, rather than the standard testcase class.
 *<p />
 *Provides a bunch of methods for checking xml document and snippet formats. These methods are implemented
 *so that they do not reference the generating code - if a snippet format changes, the assertion in this class should be updated
 *too... its a double check that we are generating the correct documents.
 *<p />
 *in turn, this class extends XMLTestCase, which provides lots of nice xml unit testing methods
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Nov-2003
 *
 */
public class ServerTestCase extends XMLTestCase {

    /**
     * Constructor for ServerTestCase.
     * @param arg0
     */
    public ServerTestCase(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ServerTestCase.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        URL u  = ServerTestCase.class.getResource("VOTable.dtd");
        assertNotNull(u);
        VOTABLE_SYSTEM_ID = u.toString();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public static String VOTABLE_SYSTEM_ID;
    public static final String VOTABLE_DOCTYPE = "VOTABLE";
    
    /** assert is a votable document */
    public void assertIsVotable(String s) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        assertXpathExists("/" + VOTABLE_DOCTYPE,s);
        assertXMLValid(s,VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE);
    }
    /** assert is a votable document */
    public void assertIsVotable(InputStream is) throws SAXException, IOException, TransformerException, ParserConfigurationException{
        Document d = DomHelper.newDocument(is);
        assertIsVotable(d);
    }

    /** assert is a votable document */
    public void assertIsVotable(Document d) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        assertXpathExists("/" + VOTABLE_DOCTYPE,d);
        assertXMLValid(new Validator(d,VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE));
    }
    
    public void assertIsVotable(Element e) throws SAXException, ParserConfigurationException {
        assertEquals(VOTABLE_DOCTYPE,e.getLocalName());
        assertXMLValid(DomHelper.ElementToString(e),VOTABLE_SYSTEM_ID,VOTABLE_DOCTYPE);
    }

    /**
     * load a resource file into a string.
     */
    public static String getResourceAsString(Class c, String resource) throws IOException {
       InputStream is = c.getResourceAsStream(resource);
       assertNotNull(is);
       String script = ServerTestCase.streamToString(is);
       assertNotNull(script);
       return script;
    }

    /** load stream into a string */
    public static String streamToString(InputStream is) throws IOException {
       assertNotNull(is);
       StringWriter sw = new StringWriter();
       Piper.bufferedPipe(new InputStreamReader(is), sw);
       return sw.toString();
    }
    
    /** parse string to Document  - use DomHelper.newDocument(String)
    public static Document stringToDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
        assertNotNull(xml);
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        assertNotNull(is);
        Document doc =  DomHelper.newDocument(is);
        assertNotNull(doc);
        return doc;
    }

    /**
     * @param d
     */
    public void assertIsVotableResultsResponse(Document d) throws Exception {
//        assertXpathExists("//VOTABLE",d);
       /*
        assertXpathExists("/DatacenterResults/@queryid",d);
        assertXpathExists("/DatacenterResults/TIME",d);
        assertXpathEvaluatesTo("votable","/DatacenterResults/Results/@type",d );
        assertXpathExists("/DatacenterResults/Results/VOTABLE",d);
        // would be nice to validate the votable too..
        */
         
    }
    
    public void assertIsVotableResultsResponse(String s) throws Exception{
        assertIsVotableResultsResponse(DomHelper.newDocument(s));
    }
    
    
    
    
    /**
     * @param d
     */
    public void assertIsUnknownIdResponse(Document d) throws Exception {
        assertXpathExists("/Error",d);
    }

    /**
     * @param d
     * @todo - why has this snippet got a queryId attributed, when all rest have queryId elements?
     */
    public void assertIsStatusResponse(Document d) throws Exception{
        assertXpathExists("/Status",d);
        assertXpathExists("/Status/@queryid",d);
        
    }
    

    /**
     * @param d
     */
    public void assertIsQueryStartedResponse(Document d) throws Exception {
        assertXpathExists("/QueryStarted",d);
        assertHasQueryId(d);
        assertHasStatus(d);
    }

    /**
     * @param d
     */
    public void assertIsQueryCreatedResponse(Document d) throws Exception{
        assertXpathExists("/QueryCreated",d);
        assertHasQueryId(d);
        assertHasStatus(d);
    }
    
    /** @todo implement this once, we have a schema */
    public void assertIsMetadata(Document d) {
        //does nothing for now.
    }
    /** check the document contains a 'QueryId' element */
    public void assertHasQueryId(Document d) throws Exception {
        assertXpathExists("//QueryId",d);
    }
    /** check the document has a 'Status' lement */
    public void assertHasStatus(Document d) throws Exception{
        assertXpathExists("//Status",d);
    }

}


/*
$Log: ServerTestCase.java,v $
Revision 1.6  2004/03/12 04:54:06  mch
It05 MCH Refactor

Revision 1.5  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.4  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.3  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.2  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.1  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code
 
*/
