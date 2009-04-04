/*$Id: JavaClassProviderTest.java,v 1.18 2009/04/04 20:41:54 pah Exp $
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

import junit.framework.TestCase;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.SimpleApplicationDescriptionLibrary;
import org.astrogrid.applications.description.TestServiceDefinition;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.test.MockMonitor;
import org.astrogrid.security.SecurityGuard;

/** Test of the JavaClass backend to CEa
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 3 Apr 2009 update to reflect the new way that javaclass application behave.
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
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        secGuard = new SecurityGuard();//TODO should put in some real credenials perhaps
        IdGen idgen = new InMemoryIdGen();
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
        monitor = new MockMonitor();
        AppAuthorityIDResolver aresolver = new TestAuthorityResolver();
        InternalCeaComponentFactory cf = new InternalCeaComponentFactory(protocolLib,idgen, aresolver);
	lib = new SimpleApplicationDescriptionLibrary(new MockNonSpringConfiguredConfig());
        ((SimpleApplicationDescriptionLibrary)lib).addApplicationDescription(new JavaClassApplicationDescription("org.astrogrid.applications.javaclass.SampleJavaClassApplications", new MockNonSpringConfiguredConfig(), 
                new TestServiceDefinition()));
        assertNotNull(lib);
    }
    protected ApplicationDescriptionLibrary lib;
    protected MockMonitor monitor;
    
    private SecurityGuard secGuard;
    public void testLibrary() throws Exception {
        String[] names = lib.getApplicationNames();
        assertNotNull(names);
        assertEquals(2,names.length); // 1 from provider , 1 built-in       
    }
    
    public void testHelloWorld() throws Exception {
        ApplicationDescription hw = lib.getDescription("ivo://local.test/sampleJavaApp");
        assertNotNull(hw);
        assertEquals("ivo://local.test/sampleJavaApp",hw.getId());
        ApplicationInterface iface = hw.getInterface("helloWorld");
        assertNotNull(iface);
        Tool tool = new Tool();
        tool.setInterface("helloWorld");
        ListOfParameterValues output= new ListOfParameterValues();
	tool.setOutput(output);
        ParameterValue result = new ParameterValue();
        result.setId(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",secGuard,tool);
        assertNotNull(app);
        app.addObserver(monitor);
        app.createExecutionTask().run();
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String o = results.getResult().get(0).getValue();
        assertNotNull(o);
        System.out.println(o);
    }
    public void testSum() throws Exception {
        ApplicationDescription hw = lib.getDescription("ivo://local.test/sampleJavaApp");
        assertNotNull(hw);
        ApplicationInterface iface = hw.getInterface("sum");
        String[] inputParameterNames = iface.getArrayofInputs();
        assertNotNull(iface);
        Tool tool = new Tool();
        tool.setInterface("sum");
        ListOfParameterValues input= new ListOfParameterValues();
	tool.setInput(input);
        ListOfParameterValues output= new ListOfParameterValues();
	tool.setOutput(output);
        ParameterValue a = new ParameterValue();
        ParameterValue b = new ParameterValue();        
        a.setId(inputParameterNames[0]);
        b.setId(inputParameterNames[1]);
        a.setValue("1");
        b.setValue("2");
        input.addParameter(a);
        input.addParameter(b);
        ParameterValue result = new ParameterValue();
        result.setId(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",secGuard,tool);
        assertNotNull(app);        
        app.addObserver(monitor);

        app.createExecutionTask().run();
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String o = results.getResult().get(0).getValue();
        assertNotNull(o);
        System.out.println(o);
        assertEquals("3",o);
    }    
    

    public void testEchoDifferentArgs() throws Exception {
        ApplicationDescription hw = lib.getDescription("ivo://local.test/sampleJavaApp");
        assertNotNull(hw);
        ApplicationInterface iface = hw.getInterface("echoDifferent");
        String[] inputParameterNames = iface.getArrayofInputs();
        assertNotNull(iface);
        Tool tool = new Tool();
        tool.setInterface("echoDifferent");
        ListOfParameterValues input= new ListOfParameterValues();
	tool.setInput(input);
        
        ListOfParameterValues output = new ListOfParameterValues();
	tool.setOutput(output);
        ParameterValue a = new ParameterValue();
        ParameterValue b = new ParameterValue();        
        a.setId(inputParameterNames[0]);
        b.setId(inputParameterNames[1]);
        a.setValue("Hello");
        b.setValue("2");
        input.addParameter(a);
        input.addParameter(b);
        ParameterValue result = new ParameterValue();
        result.setId(iface.getArrayofOutputs()[0]);
        output.addParameter(result);
        Application app = hw.initializeApplication("testrun",secGuard,tool);
        assertNotNull(app);        
        app.addObserver(monitor);

        app.createExecutionTask().run();
        assertTrue(monitor.sawExit);
        ResultListType results= app.getResult();
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        String o = results.getResult().get(0).getValue();
        assertNotNull(o);
        System.out.println(o);
        assertEquals("Hello2",o);
    }    

}


/* 
$Log: JavaClassProviderTest.java,v $
Revision 1.18  2009/04/04 20:41:54  pah
ASSIGNED - bug 2113: better configuration for java CEC
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
Introduced annotations

Revision 1.17  2008/10/06 12:16:14  pah
factor out classes common to server and client

Revision 1.16  2008/09/24 13:40:49  pah
package naming changes

Revision 1.15  2008/09/13 09:51:02  pah
code cleanup

Revision 1.14  2008/09/10 23:27:14  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.4  2008/09/03 14:18:44  pah
result of merge of pah_cea_1611 branch

Revision 1.3.2.4  2008/08/02 13:33:41  pah
safety checkin - on vacation

Revision 1.3.2.3  2008/05/13 15:14:07  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.3.2.2  2008/03/27 13:37:24  pah
now producing correct registry documents

Revision 1.3.2.1  2008/03/19 23:28:58  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.3  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.2  2007/02/19 16:20:21  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.12.2.1  2007/01/18 13:38:06  gtr
A built-in application is provided for testing.

Revision 1.12  2006/11/06 16:39:54  gtr
There are 5 applications now, not 4.

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