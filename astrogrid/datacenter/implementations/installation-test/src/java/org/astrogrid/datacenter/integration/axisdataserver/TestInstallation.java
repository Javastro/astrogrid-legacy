/*$Id: TestInstallation.java,v 1.1 2003/11/18 12:14:14 nw Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.AbstractTestInstallation;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.w3c.dom.Document;
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
public class TestInstallation extends AbstractTestInstallation { 

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
    
    protected void setUp() throws Exception{
        super.setUp();
        tomcatURL = new URL(serviceURL.getProtocol(),serviceURL.getHost(),serviceURL.getPort(),"/");
    }
    protected URL tomcatURL;
    
    public void testTomcatInstallation() {
        try {
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
            URL axisURL = new URL(tomcatURL,"axis");
        System.out.println("Connecting to " + axisURL.toString());
        String frontPage = HsqlTestCase.streamToString( axisURL.openConnection().getInputStream() );
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
        URL checkURL =  new URL(tomcatURL,"axis/happyaxis.jsp");
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
    public void testDatacenterWSDL() {
        try {
         URL wsdlURL = new URL(serviceURL.toString() + "?wsdl");
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
    

    

}


/* 
$Log: TestInstallation.java,v $
Revision 1.1  2003/11/18 12:14:14  nw
mavenized installation-test

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.10  2003/09/19 12:01:02  nw
factored out common base class

Revision 1.9  2003/09/18 13:14:55  nw
renamed delegate methods to match those in web service

Revision 1.8  2003/09/17 18:52:12  nw
added more to the installation test

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