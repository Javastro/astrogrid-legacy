/*$Id: TestInstallation.java,v 1.1 2003/09/10 13:05:05 nw Exp $
 * Created on 08-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.integration.axisdataserver;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*; 
import java.util.*;
import java.net.*;
/** junit test for validating an installation
 * - deliberately not called *Test - as we dont want it automatically run via maven
 *  - just that junit is a nice way to present these installation tests 
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2003
 *@todo just extends hsqlTestCase to get access to unitily functions. Could move these to a static helper class.
 */
public class TestInstallation extends HsqlTestCase { 

    /**
     * Constructor for TestInstallation.
     * @param arg0
     */
    public TestInstallation(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(TestInstallation.class);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestInstallation.class);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
       baseURL = new URL (System.getProperty(BASE_URL_KEY,BASE_URL_DEFAULT)); // trailing / is important here.
       serviceName = System.getProperty(SERVICE_NAME_KEY,SERVICE_NAME_DEFAULT);
       queryFile = new File(System.getProperty(QUERY_FILE_KEY,QUERY_FILE_DEFAULT));
    }
    protected URL baseURL;
    protected String serviceName;
    protected File queryFile;
    public final String BASE_URL_KEY = "datacenter.test.installation.base.url";
    public final String BASE_URL_DEFAULT="http://localhost:8080/axis/"; // trailing / is important
    public final String SERVICE_NAME_KEY = "datacenter.test.installation.service.name";
    public final String SERVICE_NAME_DEFAULT="AxisDataServer";
    public final String QUERY_FILE_KEY = "datacenter.test.installation.query.file";
    public final String QUERY_FILE_DEFAULT = "query.xml";
    
    

    /*
     * @see TestCase#tearDown() 
     */
    protected void tearDown() throws Exception {
    }
    
    public void testAxisInstallation() throws Exception {
        String frontPage = streamToString( baseURL.openConnection().getInputStream() );
        assertTrue(frontPage.trim().length() > 0);
        assertNotNull(frontPage);
        assertEquals(-1,frontPage.toLowerCase().indexOf("error"));
        String checkPage = streamToString( (new URL(baseURL,"happyaxis.jsp")).openConnection().getInputStream() );
        assertNotNull(checkPage);
        assertTrue(checkPage.trim().length() > 0);
        assertEquals(-1,checkPage.toLowerCase().indexOf("error"));       
    }
    
    public void testDatacenterInstallation() throws Exception {
        // nothing here for now - later will add a self-test / reporting web service method to datacenter.
    }
    
    public void testDoQuery() throws Exception {
        String serviceEndpoint = (new URL(baseURL,"services/" + serviceName)).toString();
        System.out.println("Accessing service at " + serviceEndpoint);
        DatacenterDelegate del = DatacenterDelegate.makeDelegate( serviceEndpoint) ;// pity it can't take a URL
        assertNotNull(del);
        assertTrue("query file does not exist: " +queryFile.getAbsolutePath(),queryFile.exists());
        if (queryFile.isFile()) {
            doQuery(del,queryFile);
        } else if (queryFile.isDirectory()) {
            File[] inputs = queryFile.listFiles();
            for (int i  = 0; i < inputs.length; i++) {
                doQuery(del,inputs[i]);
            }
        }
    }
    
    protected void doQuery(DatacenterDelegate del,File f) throws Exception {
        assertTrue(f.exists() && f.isFile());
        Document doc = XMLUtils.newDocument(new FileInputStream(f));
        assertNotNull(doc);
        Element input = doc.getDocumentElement();
        assertNotNull(input);
        Element result =  del.adqlQuery(input);
        assertNotNull(result); 
        assertEquals("VOTABLE",result.getLocalName());
        assertEquals(1,result.getElementsByTagName("TR").getLength()); // should return a single row.
        System.out.println(XMLUtils.ElementToString(result)); 
    }
    

}


/* 
$Log: TestInstallation.java,v $
Revision 1.1  2003/09/10 13:05:05  nw
added integration -testing hierarchy
 
*/