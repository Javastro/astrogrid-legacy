/*$Id: HttpApplicationProviderTest.java,v 1.2 2004/07/30 14:54:47 jdt Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.test.GetEchoerHTTPD;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.TestCase;

/** 
 * Test of the HttpApplication backend to CEa
 * @author JDT/Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class HttpApplicationProviderTest extends TestCase {
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
        GetEchoerHTTPD.getServer(PORT);
        IdGen idgen = new InMemoryIdGen();
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary();
        monitor = new MockMonitor();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib);
        HttpApplicationDescriptionLibrary.Community community = new TestCommunity();
        RegistryQuerier querier = new TestRegistryQuerier(PORT);
        lib = new HttpApplicationDescriptionLibrary(querier, community,env);
        assertNotNull(lib);
    }
    protected ApplicationDescriptionLibrary lib;
    protected MockMonitor monitor;
    
    protected final User user = new User();
    public void testLibrary() throws Exception {
        String[] names = lib.getApplicationNames();
        assertNotNull(names);
        assertEquals(1,names.length);        
    }
    
    public void testEcho() throws Exception {
        ApplicationDescription hw = lib.getDescription("org.astrogrid.test/echoer");
        assertNotNull(hw);
        assertEquals("org.astrogrid.test/echoer",hw.getName());
        ApplicationInterface iface = hw.getInterfaces()[0];
        assertNotNull(iface);
        Tool tool = new Tool();
        Output output = new Output();
        tool.setOutput(output);
        ParameterValue result = new ParameterValue();
        result.setName(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.execute();
         monitor.waitFor(10);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String o = results.getResult(0).getValue();
        assertNotNull(o);
        System.out.println(o);
    }

    

}


/* 
$Log: HttpApplicationProviderTest.java,v $
Revision 1.2  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.1.2.1  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.


 
*/