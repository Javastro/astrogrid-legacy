/*$Id: TestInstallation.java,v 1.2 2003/09/11 11:05:33 nw Exp $
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
/** integration test for validating an installation
 * <p>
 * First checks that axis is running, then that the datacenter web application is present and installed.
 * Then will run predefined queries through the datacenter.
 * <p>
 * - deliberately not called *Test - as we dont want it automatically run via maven
 *  - just that junit is a nice way to present these installation tests 
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2003

 */
public class TestInstallation extends TestCase { 

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
     /** set up connection properties. will use defaults defined as constants in this class,
      * unless system properties are defined (under the corresponding keys).
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
    
    

    /** check that tomcat is running and the axis server is running */
    public void testAxisInstallation() throws Exception {
        String frontPage = HsqlTestCase.streamToString( baseURL.openConnection().getInputStream() );
        assertTrue(frontPage.trim().length() > 0);
        assertNotNull(frontPage);
        assertEquals(-1,frontPage.toLowerCase().indexOf("error"));
        String checkPage = HsqlTestCase.streamToString( (new URL(baseURL,"happyaxis.jsp")).openConnection().getInputStream() );
        assertNotNull(checkPage);
        assertTrue(checkPage.trim().length() > 0);
        assertEquals(-1,checkPage.toLowerCase().indexOf("error"));       
    }
    
    /** check the datacenter web service is installed, by attempting to get the wsdl for it. */
    public void testDatacenterInstallation() throws Exception {
        InputStream wsdlStream =  (new URL(baseURL,"services/" + serviceName + "?wsdl")).openConnection().getInputStream();
        assertNotNull(wsdlStream);
        Document wsdlDoc = XMLUtils.newDocument(wsdlStream);
        assertNotNull(wsdlDoc);
        assertEquals("definitions",wsdlDoc.getDocumentElement().getLocalName());
        
    }
    /** run a series of sample queries through the service
     *  <p>
     *  examines value of {@link #QUERY_FILE_KEY}, searches for input files in following order
     * <ol>
     * <li>a directory named ${QUERY_FILE_KEY} - uses each *.xml file within it as an input
     * <li>a file named ${QUERY_FILE_KEY} - uses this as the single input
     * <li>a resource on classpath named ${QUERY_FILE_KEY} - uses this as the single input.
     * @throws Exception
     */
    public void testDoQuery() throws Exception {
        String serviceEndpoint = (new URL(baseURL,"services/" + serviceName)).toString();
        System.out.println("Accessing service at " + serviceEndpoint);
        DatacenterDelegate del = DatacenterDelegate.makeDelegate( serviceEndpoint) ;// pity it can't take a URL
        assertNotNull(del);
        if (queryFile.exists()) { // on local file system.
            if (queryFile.isFile()) {
                System.out.println ("Local file");
                doQuery(del,new FileInputStream(queryFile));
            } else if (queryFile.isDirectory()) {
                System.out.println("Local directory");
                File[] inputs = queryFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {                        
                        return pathname.getName().toLowerCase().endsWith(".xml");
                    }
                });
                for (int i  = 0; i < inputs.length; i++) {
                    doQuery(del,new FileInputStream(inputs[i]));
                }
            }
        } else { // try loading from classpath.
            System.out.println("Loading Resource");
            InputStream is = this.getClass().getResourceAsStream(queryFile.getPath()); 
            assertNotNull("input file :" + queryFile.getPath() + " not found on filesystem, or as resource",is);
            doQuery(del,is);
        }
    }
    
    protected void doQuery(DatacenterDelegate del,InputStream is) throws Exception {
        Document doc = XMLUtils.newDocument(is);
        assertNotNull(doc);
        Element input = doc.getDocumentElement();
        assertNotNull(input);
        Element result =  del.adqlQuery(input);
        assertNotNull(result); 
        assertEquals("ResultsResponse",result.getLocalName());
        assertEquals(1,result.getElementsByTagName("TR").getLength()); // should return a single row.
        System.out.println(XMLUtils.ElementToString(result));

    }
    

}


/* 
$Log: TestInstallation.java,v $
Revision 1.2  2003/09/11 11:05:33  nw
fixed to work with changes to query input format

Revision 1.1  2003/09/10 13:05:05  nw
added integration -testing hierarchy
 
*/