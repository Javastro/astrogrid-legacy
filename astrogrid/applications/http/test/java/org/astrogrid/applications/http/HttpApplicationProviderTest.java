/*$Id: HttpApplicationProviderTest.java,v 1.7 2004/09/17 01:23:09 nw Exp $
 * Created on 30-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.test.FileUnmarshaller;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.http.test.TestWebServer;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/** 
 * Test of the HttpApplication backend to CEA
 * @author JDT/Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class HttpApplicationProviderTest extends TestCase {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplicationProviderTest.class);

    private static final String COMMUNITY = "org.astrogrid.test";
    private static final int PORT = 8078;
    /**
     * Constructor 
     * @param arg0
     */
    public HttpApplicationProviderTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        //Fire up an in process server
        TestWebServer.getServer(PORT);
        IdGen idgen = new InMemoryIdGen();
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary();
        monitor = new MockMonitor();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib);
        HttpApplicationDescriptionLibrary.Community community = new TestCommunity();
        RegistryQuerier querier = new TestRegistryQuerier();
        numberOfApps = querier.getHttpApplications().size();
        applicationDescriptionLibrary = new HttpApplicationDescriptionLibrary(querier, community,env);
        assertNotNull(applicationDescriptionLibrary);
    }
    protected ApplicationDescriptionLibrary applicationDescriptionLibrary;
    protected MockMonitor monitor;
    
    protected final User user = new User();
    private int numberOfApps;
    private FileUnmarshaller toolUnmarshaller = new FileUnmarshaller(Tool.class);
    public void testLibrary() throws Exception {
        String[] names = applicationDescriptionLibrary.getApplicationNames();
        assertNotNull(names);
        assertEquals(numberOfApps,names.length);        
    }
    
    public void testNoDescriptionFound() {
        try {
            ApplicationDescription hw = applicationDescriptionLibrary.getDescription(COMMUNITY+"/dadsfadgadfgadgd");
        } catch (ApplicationDescriptionNotFoundException e) {
            return; //expected
        }
        fail("Expected an ApplicationDescriptionNotFoundException");
    }
 
    public void testHelloWorld() throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("testHelloWorld() - start");
        }

        ApplicationDescription hw = getApplicationDescription("/HelloWorld");
        
        Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-helloWorld1.xml");
        
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String resultName=results.getResult(0).getName();
        assertEquals("Result", resultName);
        
        String resultVal = results.getResult(0).getValue();
        assertNotNull(resultVal);
        log.debug("testHelloWorld()" + resultVal);
        assertEquals("HelloWorld",resultVal);

        if (log.isTraceEnabled()) {
            log.trace("testHelloWorld() - end");
        }
    }
    
    /**
     * get an application library and run a few basic checks
     * @param appName
     * @return
     * @throws ApplicationDescriptionNotFoundException
     */
    private ApplicationDescription getApplicationDescription(final String appName) throws ApplicationDescriptionNotFoundException {
        ApplicationDescription hw = applicationDescriptionLibrary.getDescription(COMMUNITY+appName);
        assertNotNull(hw);
        assertEquals(COMMUNITY+appName,hw.getName());
        ApplicationInterface iface = hw.getInterfaces()[0];
        assertNotNull(iface);
        return hw;
    }
    public void testHelloYou() throws Exception {
        ApplicationDescription hw = getApplicationDescription("/HelloYou");
        
        Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-helloYou1.xml");
        
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String resultName=results.getResult(0).getName();
        assertEquals("result", resultName);
        
        String resultVal = results.getResult(0).getValue();
        assertNotNull(resultVal);
        log.debug("testHelloYou()" + resultVal);
        assertEquals("Hello Old King Cole was a Merry Old Soul",resultVal);
    }
    
    public void testEcho1() throws Exception {
        ApplicationDescription hw = getApplicationDescription("/Echoer");
       
        Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-echo1.xml");
        
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String resultName=results.getResult(0).getName();
        assertEquals("Reply", resultName);
        
        String resultVal = results.getResult(0).getValue();
        assertNotNull(resultVal);
        log.debug("testEcho1()" + resultVal);
        assertEquals("{x=kerry4potus}",resultVal);
    }
    
    public void testEcho2() throws Exception {
        ApplicationDescription hw = getApplicationDescription("/Echoer");
        
        Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-echo2.xml");
        
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String resultName=results.getResult(0).getName();
        assertEquals("Reply", resultName);
        
        String resultVal = results.getResult(0).getValue();
        assertNotNull(resultVal);
        log.debug("testEcho2()" + resultVal);
        assertEquals("{y=bush4theHague}",resultVal); 
    }
    
    public void testAdd() throws Exception {
        ApplicationDescription hw = getApplicationDescription("/Adder");
        
        Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
        
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String resultName=results.getResult(0).getName();
        assertEquals("sum", resultName);
        
        String resultVal = results.getResult(0).getValue();
        assertNotNull(resultVal);
        log.debug("testAdd()" + resultVal);
        assertEquals("8",resultVal);
    }
    
    /**
     * Post isn't supported yet
     * @throws Exception
     */
    public void testPost() throws Exception {
    		fail("This test is not ready yet, since the in-process webserver doesn't seem very happy with post");
            ApplicationDescription hw = getApplicationDescription("/AdderPost");
            
            Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
            
            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            app.execute();
            monitor.waitFor(30);
            assertTrue(monitor.sawExit);
    }
    /**
     * What happens if the URL returns a 404?
     * @throws Exception
     */
    public void test404() throws Exception {
            ApplicationDescription hw = getApplicationDescription("/Bad");
            
            Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
            
            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            app.execute();
            monitor.waitFor(10);
            assertTrue(monitor.sawError);
            Status status = app.getStatus();
            assert(status.equals(Status.ERROR));
    }
    
    /**
     * What happens if the URL timesout?
     * @throws Exception
     */
    public void testTimeOut() throws Exception {
            ApplicationDescription hw = getApplicationDescription("/Badder");
            
            Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
            
            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            app.execute();
            monitor.waitFor(10);
            assertTrue(monitor.sawError);
            Status status = app.getStatus();
            assert(status.equals(Status.ERROR));
    }
    
    /**
     * What happens if the URL is malformed?
     * @throws Exception
     */
    public void testMalformedURL() throws Exception {
            ApplicationDescription hw = getApplicationDescription("/Baddest");
            
            Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
            
            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            app.execute();
            monitor.waitFor(10);
            assertTrue(monitor.sawError);
            Status status = app.getStatus();
            assert(status.equals(Status.ERROR));
    }
    
    /**
     * Apps containing preprocessing scripts should fail at the moment.
     * @throws Exception
     * @todo NWW: this isn't observable anymore. 
     */
    public void testPreProcess() throws Exception {
            try {
                ApplicationDescription hw = getApplicationDescription("/AdderPreProcess");
                
                Tool tool  = (Tool) toolUnmarshaller.unmarshallFromFile("tool-Adder1.xml");
                
                Application app = hw.initializeApplication("testrun",user,tool);
                assertNotNull(app);
                app.addObserver(monitor);
                app.execute();
            }   catch (UnsupportedOperationException e) {
                return; //expected
            }
            fail("Expected an UnsupportedOperationException");
    }
    
    //@TODO what if the registry entry is garbage?

}


/* 
$Log: HttpApplicationProviderTest.java,v $
Revision 1.7  2004/09/17 01:23:09  nw
updated tests

Revision 1.6  2004/09/14 16:26:26  jdt
Attempt to get the http-post working.  Upgraded http-client, to no avail.  Either http client
or the embedded test webserver isn't handling post correctly.  Flagged tests
to ensure this is dealt with.

Revision 1.5  2004/09/01 16:17:07  jdt
fixed unit test - file name was wrong case you great pillock.

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.1.2.8  2004/08/15 10:58:19  jdt
more test updates

Revision 1.1.2.7  2004/08/12 13:16:58  jdt
fixed some tests

Revision 1.1.2.6  2004/08/11 22:55:35  jdt
Refactoring, and a lot of new unit tests.

Revision 1.1.2.5  2004/08/06 13:31:52  jdt
Registry interface stuff

Revision 1.1.2.4  2004/08/02 18:05:28  jdt
Added more tests and refactored the test apps to be set up
from xml.

Revision 1.1.2.3  2004/07/30 17:03:27  jdt
renamed the test web server class

Revision 1.1.2.2  2004/07/30 16:59:50  jdt
limping along.

Revision 1.1.2.1  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.


 
*/