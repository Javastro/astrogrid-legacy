/*$Id: JavaClassProviderTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.TestCase;

/** Test of the JavaClass backend to CEa
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class JavaClassProviderTest extends TestCase {
    /**
     * Constructor for JavaClassCEATest.
     * @param arg0
     */
    public JavaClassProviderTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        IdGen idgen = new InMemoryIdGen();
        IndirectionProtocolLibrary protocolLib = new DefaultIndirectionProtocolLibrary();
        monitor = new MockMonitor();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib);
        JavaClassApplicationDescriptionLibrary.Community community = new TestCommunity();
        lib = new JavaClassApplicationDescriptionLibrary(SampleJavaClassApplications.class,community,env);
        assertNotNull(lib);
    }
    protected ApplicationDescriptionLibrary lib;
    protected MockMonitor monitor;
    
    protected final User user = new User();
    public void testLibrary() throws Exception {
        String[] names = lib.getApplicationNames();
        assertNotNull(names);
        assertEquals(3,names.length);        
    }
    
    public void testHelloWorld() throws Exception {
        ApplicationDescription hw = lib.getDescription("org.astrogrid.test/helloWorld");
        assertNotNull(hw);
        assertEquals("org.astrogrid.test/helloWorld",hw.getName());
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
    public void testSum() throws Exception {
        ApplicationDescription hw = lib.getDescription("org.astrogrid.test/sum");
        assertNotNull(hw);
        ApplicationInterface iface = hw.getInterfaces()[0];
        String[] inputParameterNames = iface.getArrayofInputs();
        assertNotNull(iface);
        Tool tool = new Tool();
        Input input = new Input();
        tool.setInput(input);
        Output output = new Output();
        tool.setOutput(output);
        ParameterValue a = new ParameterValue();
        ParameterValue b = new ParameterValue();        
        a.setName(inputParameterNames[0]);
        b.setName(inputParameterNames[1]);
        a.setValue("1");
        b.setValue("2");
        input.addParameter(a);
        input.addParameter(b);
        ParameterValue result = new ParameterValue();
        result.setName(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);        
        app.addObserver(monitor);
        app.execute();
        monitor.waitFor(20);
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String o = results.getResult(0).getValue();
        assertNotNull(o);
        System.out.println(o);
        assertEquals("3",o);
    }    
    

}


/* 
$Log: JavaClassProviderTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/