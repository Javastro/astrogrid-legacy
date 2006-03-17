/*$Id: JavaClassProviderTest.java,v 1.11 2006/03/17 17:50:58 clq2 Exp $
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
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.javaclass.BaseJavaClassConfiguration;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
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
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary();
        monitor = new MockMonitor();
        AppAuthorityIDResolver aresolver = new TestAuthorityResolver();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib,aresolver);
        lib = new JavaClassApplicationDescriptionLibrary(new BaseJavaClassConfiguration(), env);
        assertNotNull(lib);
    }
    protected ApplicationDescriptionLibrary lib;
    protected MockMonitor monitor;
    
    protected final User user = new User();
    public void testLibrary() throws Exception {
        String[] names = lib.getApplicationNames();
        assertNotNull(names);
        assertEquals(4,names.length);        
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
        app.createExecutionTask().run();
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

        app.createExecutionTask().run();
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String o = results.getResult(0).getValue();
        assertNotNull(o);
        System.out.println(o);
        assertEquals("3",o);
    }    
    

    public void testEchoDifferentArgs() throws Exception {
        ApplicationDescription hw = lib.getDescription("org.astrogrid.test/echoDifferentArgs");
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
        a.setValue("Hello");
        b.setValue("2");
        input.addParameter(a);
        input.addParameter(b);
        ParameterValue result = new ParameterValue();
        result.setName(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",user,tool);
        assertNotNull(app);        
        app.addObserver(monitor);

        app.createExecutionTask().run();
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        String o = results.getResult(0).getValue();
        assertNotNull(o);
        System.out.println(o);
        assertEquals("Hello2",o);
    }    

}


/* 
$Log: JavaClassProviderTest.java,v $
Revision 1.11  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.9  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.6.34.2  2006/02/01 12:09:54  gtr
Refactored and fixed to allow the tests to work with the new configuration.

Revision 1.6.34.1  2005/12/18 14:48:25  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

Revision 1.6  2005/07/15 14:44:32  jdt
merge from cea_jdt_1295

Revision 1.5.84.1  2005/07/15 14:28:17  jdt
Fixed the parameter order in the application interface, and added a test.

Revision 1.5  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.16.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/09/17 01:22:12  nw
updated tests

Revision 1.3.52.1  2004/09/14 13:44:59  nw
runs in same thread.

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

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