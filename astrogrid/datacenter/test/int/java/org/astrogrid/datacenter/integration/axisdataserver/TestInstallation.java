/*$Id: TestInstallation.java,v 1.7 2003/09/17 14:53:02 nw Exp $
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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
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
       //junit.swingui.TestRunner.run(TestInstallation.class);
       // necessary to do as below, to disable dynamic classloading (which causes xml classes to not be found)
       junit.swingui.TestRunner runner = new junit.swingui.TestRunner();
       runner.setLoading(false);
       runner.start(new String[]{TestInstallation.class.getName()});       
    }
    /*
     * @see TestCase#setUp()
     */
     /** set up connection properties. will use defaults defined as constants in this class,
      * unless system properties are defined (under the corresponding keys).
      */
    protected void setUp()  {
        try {
       baseURL = new URL (System.getProperty(BASE_URL_KEY,BASE_URL_DEFAULT)); // trailing / is important here.
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(BASE_URL_KEY + " is not a correctly formatted URL: " + e.getMessage());
        }
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
    
    
    public void testDisplaySettings() {
        System.out.println("Running with these settings: (adjust by runnng with -Dkey=value");
        System.out.println(BASE_URL_KEY + "=" + baseURL.toString());
        System.out.println(SERVICE_NAME_KEY + "=" + serviceName);
        System.out.println(QUERY_FILE_KEY + "=" + queryFile.getPath());
    }
    
    public void testTomcatInstallation() {
        try {
            URL tomcatURL = new URL(baseURL.getProtocol(),baseURL.getHost(),baseURL.getPort(),"/");
            System.out.println("Connecting to " + tomcatURL.toString());
            String frontPage = HsqlTestCase.streamToString(tomcatURL.openConnection().getInputStream());
            assertNotNull("Tomcat front page is null",frontPage);
            assertTrue("Tomcat front page is empty",frontPage.trim().length() > 0);
            assertEquals("Tomcat front page contains string 'error'",-1,frontPage.toLowerCase().indexOf("error"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not retrieve tomcat front page " + e.getMessage());
        }
        
    }    

    /** check that tomcat is running and the axis server is running */
    public void testAxisInstallation() {
        try {
        System.out.println("Connecting to " + baseURL.toString());
        String frontPage = HsqlTestCase.streamToString( baseURL.openConnection().getInputStream() );
        assertNotNull("Axis front page is null",frontPage);
        assertTrue("Axis front page is empty",frontPage.trim().length() > 0);
        assertEquals("Axis front page contains string 'error'",-1,frontPage.toLowerCase().indexOf("error"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not retrieve axis front page " + e.getMessage());
        }
    }
    public void testHappyAxis() {
        try {
        URL checkURL =  new URL(baseURL,"happyaxis.jsp");
        System.out.println("Connecting to " + checkURL.toString());
        String checkPage = HsqlTestCase.streamToString(checkURL.openConnection().getInputStream() );
        assertNotNull("Axis configuration checking page is null",checkPage);
        assertTrue("Axis configuration checking page is empty",checkPage.trim().length() > 0);
        assertEquals("Axis configuration checking page contains string 'error'",-1,checkPage.toLowerCase().indexOf("error"));
    } catch (IOException e) {
        e.printStackTrace();
        fail("Could not retrieve axis configuration checking page : " + e.getMessage());        
    }
    }
    
    /** check the datacenter web service is installed, by attempting to get the wsdl for it. */
    public void testDatacenterWSDL()  {
        try {
         URL wsdlURL = new URL(baseURL,"services/" + serviceName + "?wsdl");
         System.out.println("Connecting to " + wsdlURL.toString());
        InputStream wsdlStream =  wsdlURL.openConnection().getInputStream();
        assertNotNull("WSDL for dataservice is null",wsdlStream);
        Document wsdlDoc = XMLUtils.newDocument(wsdlStream);
        assertNotNull("Could not parse WSDL into document",wsdlDoc);
        assertEquals("WSDL not in expected schema","definitions",wsdlDoc.getDocumentElement().getLocalName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("URL for web service is invalid: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read WSDL: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Could not configure XML parser: " + e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Could not parse WSDL document: " + e.getMessage());
        }
        
    }
    
    public void testGetRegistryMetadata() {
        DatacenterDelegate del = createDelegate();
        try {
            Element result = del.getRegistryMetadata();
            assertNotNull(result);
            assertEquals("Registry Metadata not in expected format","DataCenterMetaData",result.getLocalName());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not get registryMetaData: " + e.getMessage());
        }
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
    public void testDoQuery()  {
        DatacenterDelegate del = createDelegate();
        if (queryFile.exists()) { // on local file system.
            if (queryFile.isFile()) {
                System.out.println ("Taking VOQL query from local file: " + queryFile.getPath());
                try {
                    doQuery(del,new FileInputStream(queryFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fail("Could not find file: " + e.getMessage());
                }
            } else if (queryFile.isDirectory()) {
                System.out.println("Taking VOQL queries from files in local directory: " + queryFile.getPath());
                File[] inputs = queryFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {                        
                        return pathname.getName().toLowerCase().endsWith(".xml");
                    }
                });
                for (int i  = 0; i < inputs.length; i++) {
                    System.out.println("Processing file: " + inputs[i].getName());
                    try {
                        doQuery(del,new FileInputStream(inputs[i]));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        fail("Could not find file: " + e.getMessage());
                    }
                }
            }
        } else { // try loading from classpath.
            System.out.println("Taking VOQL query from classpath resource" + queryFile.getPath());
            InputStream is = this.getClass().getResourceAsStream(queryFile.getPath()); 
            assertNotNull("VOQL query file :" + queryFile.getPath() + " not found on filesystem, or as resource",is);
            doQuery(del,is);
        }
    }
    /** helper method to create a datacenter delegate, based on properties initialized in {@link setUp} */
    protected DatacenterDelegate createDelegate() {
        URL serviceURL = null;
        try {
            serviceURL = new URL(baseURL,"services/" + serviceName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Service endpoint URL malformed: " + e.getMessage());
        }
        System.out.println("Connecting to datacenter service at " + serviceURL.toString());
        DatacenterDelegate del = null;
        try {
            del = DatacenterDelegate.makeDelegate( serviceURL.toString()) ;// pity it can't take a URL
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception while creating delegate: " + e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            fail("Could not access service: " + e.getMessage());
        }
        assertNotNull("Could not create delegate",del);
        return del;
    }
    /** helper method to do a query */
    protected void doQuery(DatacenterDelegate del,InputStream is)  {
        Document doc = null;
        try {
            doc = XMLUtils.newDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error reading query document: " + e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            fail("Could not parse query document: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail("Problem initializing XML parser: " + e.getMessage());
        }
        assertNotNull("VOQL document is null",doc);
        Element input = doc.getDocumentElement();
        assertNotNull("VOQL document has no root element",input);
        Element result =null;
        try {
            result = del.query(input);
        } catch (IOException e1) {           
            e1.printStackTrace();
            fail("Call to web service failed with exception: " + e1.getMessage());
        }
        assertNotNull("Result of query was null",result); 
        assertEquals("Result of query not in expected format","DatacenterResults",result.getLocalName());
        System.out.println("Results for query");
        System.out.println(XMLUtils.ElementToString(result));

    }
    

}


/* 
$Log: TestInstallation.java,v $
Revision 1.7  2003/09/17 14:53:02  nw
tidied imports

Revision 1.6  2003/09/16 13:25:47  nw
fixed to use new delegate api

Revision 1.5  2003/09/16 12:49:51  nw
integration / installation testing

Revision 1.3  2003/09/11 11:39:25  nw
fixed integration test to expect new input formats

Revision 1.2  2003/09/11 11:05:33  nw
fixed to work with changes to query input format

Revision 1.1  2003/09/10 13:05:05  nw
added integration -testing hierarchy
 
*/