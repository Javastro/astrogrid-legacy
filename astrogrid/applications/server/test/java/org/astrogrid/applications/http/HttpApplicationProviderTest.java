/*$Id: HttpApplicationProviderTest.java,v 1.2 2009/02/26 12:47:04 pah Exp $
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
import static org.junit.Assert.*;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.http.test.FileUnmarshaller;
import org.astrogrid.applications.http.test.TestHttpApplicationLibrary;
import org.astrogrid.applications.http.test.TestWebServer;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.test.MockMonitor;
import org.astrogrid.security.SecurityGuard;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of the HttpApplication backend to CEA
 * @author JDT/Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Jun 2008
 *
 */
public class HttpApplicationProviderTest {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplicationProviderTest.class);

    private static final String AUTHORITY = "ivo://org.astrogrid.test";
    private static final int PORT = 8078;
  /*
     * @see TestCase#setUp()
     */
    @BeforeClass
    static public void setUpBeforeClass() throws Exception {
        //Fire up an in process server
        TestWebServer.getServer(PORT);
        IdGen idgen = new InMemoryIdGen();
        DefaultProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
        monitor = new MockMonitor();
        AppAuthorityIDResolver aresolver = new TestAuthorityResolver();
        new InternalCeaComponentFactory(protocolLib, idgen, aresolver);
        TestHttpApplicationLibrary querier = new TestHttpApplicationLibrary(new MockNonSpringConfiguredConfig());
        numberOfApps = querier.getApplicationNames().length;
        new MockNonSpringConfiguredConfig();
	applicationDescriptionLibrary = querier;
        assertNotNull(applicationDescriptionLibrary);
    }
    protected static ApplicationDescriptionLibrary applicationDescriptionLibrary;
    protected static MockMonitor monitor;

    protected final SecurityGuard user = new SecurityGuard();
    private static int numberOfApps;
    private FileUnmarshaller toolUnmarshaller = new FileUnmarshaller(Tool.class);
    public void testLibrary() throws Exception {
        String[] names = applicationDescriptionLibrary.getApplicationNames();
        assertNotNull(names);
        assertEquals(numberOfApps + 1,names.length); // 1 extra built-in app
    }

    @org.junit.Test
    public void testNoDescriptionFound() {
        try {
            applicationDescriptionLibrary.getDescription(AUTHORITY+"/dadsfadgadfgadgd");
        } catch (ApplicationDescriptionNotFoundException e) {
            return; //expected
        }
        fail("Expected an ApplicationDescriptionNotFoundException");
    }

    @Test
    public void testHelloWorld() throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("testHelloWorld() - start");
        }

        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/HelloWorld");

        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-helloWorld1.xml");

        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("Result", resultName);

        String resultVal = results.getResult().get(0).getValue();
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
        ApplicationDescription hw = applicationDescriptionLibrary.getDescription(appName);
        assertNotNull(hw);
        assertEquals(appName,hw.getId());
        ApplicationInterface iface = hw.getInterfaces()[0];
        assertNotNull(iface);
        return hw;
    }
    @Test
    public void testHelloYou() throws Exception {
        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/HelloYou");

        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-helloYou1.xml");

        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("Result", resultName);

        String resultVal = results.getResult().get(0).getValue();
        assertNotNull(resultVal);
        log.debug("testHelloYou()" + resultVal);
        assertEquals("Hello Old King Cole was a Merry Old Soul",resultVal);
    }

    @Test
    public void testEcho1() throws Exception {
        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Echoer");

        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-echo1.xml");

        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("Reply", resultName);

        String resultVal = results.getResult().get(0).getValue();
        assertNotNull(resultVal);
        log.debug("testEcho1()" + resultVal);
        assertEquals("{x=kerry4potus}",resultVal);
    }

    @Test
    public void testEcho2() throws Exception {
        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Echoer");

        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-echo2.xml");

        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("Reply", resultName);

        String resultVal = results.getResult().get(0).getValue();
        assertNotNull(resultVal);
        log.debug("testEcho2()" + resultVal);
        assertEquals("{y=bush4theHague}",resultVal);
    }

    public void testAdd() throws Exception {
        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Adder");

        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");

        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("sum", resultName);

        String resultVal = results.getResult().get(0).getValue();
        assertNotNull(resultVal);
        log.debug("testAdd()" + resultVal);
        assertEquals("8",resultVal);
    }

    /**
     * As testAdd, but using a file:// param
     * @throws Exception
     */
    @Test
    public void testAddIndirectParameter() throws Exception {
        ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Adder");



        Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");
        // Replace a parameter by an indirect value.  Can't do this in the xml file
        // since we don't know where your local tmp directory is going to be.
        URL url_x = this.getClass().getResource("test/xinput.txt");
        log.debug("Located x input file at "+url_x);
        ParameterValue pval_x = tool.getInput().findParameter("x");
        log.debug("Changing x value in tools document from "+pval_x.getValue());
        pval_x.setValue(url_x.toString());
        log.debug("to " + pval_x.getValue());
        pval_x.setIndirect(true);
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        this.execute(app);
        monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String resultName=results.getResult().get(0).getId();
        assertEquals("sum", resultName);

        String resultVal = results.getResult().get(0).getValue();
        assertNotNull(resultVal);
        log.debug("testAdd()" + resultVal);
        assertEquals("8",resultVal);
    }

    /**
     * What happens if the URL returns a 404?
     * @throws Exception
     */
    @Test
    public void test404() throws Exception {
            ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Bad");

            Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");

            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            this.execute(app);
            monitor.waitFor(10);
            assertTrue(monitor.sawError);
            Status status = app.getStatus();
            assert(status.equals(Status.ERROR));
    }

    /**
     * What happens if the URL timesout?
     * @throws Exception
     */
    @Test
    public void testTimeOut() throws Exception {
            ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Badder");

            Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");

            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            this.execute(app);
            monitor.waitFor(10);
            assertTrue(monitor.sawError);
            Status status = app.getStatus();
            assert(status.equals(Status.ERROR));
    }

    /**
     * What happens if the URL is malformed?
     * @throws Exception
     */
    @Test
    public void testMalformedURL() throws Exception {
            ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/Baddest");

            Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");

            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            this.execute(app);
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
    @Test
    public void testPreProcess() throws Exception {

            ApplicationDescription hw = getApplicationDescription(AUTHORITY+"/AdderPreProcess");

            Tool tool  = (Tool) toolUnmarshaller.unmarshallCastorFromFile("tool-Adder1.xml");

            Application app = hw.initializeApplication("testrun",user,tool);
            assertNotNull(app);
            app.addObserver(monitor);
            this.execute(app);
            monitor.waitFor(10);
            //Should fail internally with an UnsupportedOperationException
            assertTrue(monitor.sawError);
    }

    //@TODO what if the registry entry is garbage?

  /**
   * Starts the execution of a given Application.
   * This is a convenience method to hide the thread handling.
   *
   * @param app The Application to be executed.
   * @throws CeaException if the Application fails to provide a Runnable task.
   */
  private void execute(Application app) throws CeaException {
    Runnable r = app.createExecutionTask();
    assertNotNull(r);
    r.run();
//    Thread t = new Thread(r);
//    t.start();

  }
}


/*
$Log: HttpApplicationProviderTest.java,v $
Revision 1.2  2009/02/26 12:47:04  pah
separate more out into cea-common for both client and server

Revision 1.1  2008/09/10 23:27:17  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.19  2008/09/03 14:18:33  pah
result of merge of pah_cea_1611 branch

Revision 1.18.2.2  2008/08/02 13:32:32  pah
safety checkin - on vacation

Revision 1.18.2.1  2008/04/01 13:50:07  pah
http service also passes unit tests with new jaxb metadata config

Revision 1.18  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.17  2007/02/19 16:19:26  gtr
Branch apps-gtr-1061 is merged.

Revision 1.16.32.2  2007/01/18 13:38:21  gtr
A built-in application is provided for testing.

Revision 1.16.32.1  2007/01/17 18:10:34  gtr
The deprecated method Application.execute() has been removed.

Revision 1.16  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.14  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.11.34.2  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.11.34.1  2005/12/22 13:56:03  gtr
Refactored to match the other kinds of CEC.

Revision 1.11  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.10.68.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.10.54.1  2005/06/08 22:10:45  pah
make http applications v10 compliant

Revision 1.10  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.9  2004/11/08 17:59:21  jdt
Fixed a unit test.

Revision 1.8  2004/09/26 23:34:47  jdt
Added a unit test that checks that indirect input params are handled right.

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