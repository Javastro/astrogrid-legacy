/*$Id: AbstractApplicationTest.java,v 1.11 2008/10/06 12:16:16 pah Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications;


import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDefinition;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.LogLevel;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.intnl.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.security.SecurityGuard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test to exercise the basic functionality of abstract appliction
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 * 
 */
public class AbstractApplicationTest  {
    /**
     * Constructor for AbstractApplicationTest.
     * 
     * @param arg0
     */
    public AbstractApplicationTest() {
	
    }
    
    @BeforeClass
    public static void beforeClass()
    {
	idgen = new InMemoryIdGen();
	
    }

    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
	

	ProtocolLibrary protolib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	AppAuthorityIDResolver resolv = new TestAuthorityResolver();
	CeaApplication appMeta = new CeaApplication();



	BaseParameterDefinition x = new BaseParameterDefinition();
	x.setId("x");
	BaseParameterDefinition y = new BaseParameterDefinition();
	y.setId("y");

	appMeta.addParameterDescription(x);
	appMeta.addParameterDescription(y);

	InterfaceDefinition interf = new InterfaceDefinition(
		"onlyInterface", description);
	interf.addInputParameter("x");
	interf.addOutputParameter("y");
	appMeta.addInterface(interf);
	
	
	assertTrue("application definition invalid", appMeta.getApplicationDefinition().isValid());

	Configuration conf = new MockNonSpringConfiguredConfig();
	description = new InternallyConfiguredApplicationDescription(appMeta,  conf ) {
	    public Application initializeApplication(String jobStepID,
		    SecurityGuard secGuard, Tool tool) throws Exception {
		return null;
	    }
	    
	};

	
	//
	tool = new Tool();
	tool.setInterface("onlyInterface");
	ListOfParameterValues input = new ListOfParameterValues();
	tool.setInput(input  );
	ListOfParameterValues output = new ListOfParameterValues();
	tool.setOutput(output);
	ParameterValue xVal = new ParameterValue();
	xVal.setId(x.getId());
	xVal.setValue("fred");
	input.addParameter(xVal);

	ParameterValue yVal = new ParameterValue();
	yVal.setId(y.getId());
	yVal.setValue("barney");
	output.addParameter(yVal);

	ApplicationEnvironment runenv = new ApplicationEnvironment("testId",new SecurityGuard(), idgen, conf);
	//
	app = new AbstractApplication( tool, interf, runenv , protolib) {

	    public ResultListType retrieveResult() {
		return null;
	    }

	    @Override
	    public Runnable createRunnable() {
		return null;
	    }

	};
	obs = new TestObserver();
	app.addObserver(obs);
    }

    protected InternallyConfiguredApplicationDescription description;
    protected Tool tool;
    protected AbstractApplication app;
    protected TestObserver obs;
    private static IdGen idgen;

 
    @Test
    public void testGetApplicationInterface() throws Exception {
	ApplicationInterface intf = app.getApplicationInterface();
	assertEquals(intf.getId(), tool.getInterface());
    }

    @Test
    public void testGetApplicationDescription() {
	ApplicationDescription descr = app.getApplicationDescription();
	assertSame(description, descr);
    }

    @Test
    public void testStatus() {
	assertEquals(Status.NEW, app.getStatus()); // fresh apps have new
						    // status.
	app.setStatus(Status.RUNNING);
	assertEquals(Status.RUNNING, app.getStatus());
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	assertEquals(Status.RUNNING, obs.arg);
    }

    @Test
    public void testFindInputParameter() {
	ParameterValue x1 = app.findInputParameter("x");
	assertNotNull(x1);
	assertEquals("x", x1.getId());
    }

    @Test
    public void testFindOutputParameter() {
	ParameterValue y1 = app.findOutputParameter("y");
	assertNotNull(y1);
	assertEquals("y", y1.getId());
    }

    @Test
    public void testFindUnknownParameter() {
	assertNull(app.findInputParameter("unknown"));
    }

    @Test
    public void testCreateAdaptersAndFindParameterAdapter() throws Exception {
	assertFalse(app.inputParameterAdapters().hasNext());
	assertFalse(app.outputParameterAdapters().hasNext());
	app.createAdapters();
	// check each now has one adapater.
	Iterator i = app.inputParameterAdapters();
	assertTrue(i.hasNext());
	i.next();
	assertFalse(i.hasNext());
	i = app.outputParameterAdapters();
	assertTrue(i.hasNext());
	i.next();
	assertFalse(i.hasNext());
	// now find some adapters.
	ParameterAdapter pA = app.findParameterAdapter("y");
	assertNotNull(pA);
	assertEquals("y", pA.getWrappedParameter().getId());
    }

    @Test
    public void testParameterAdapterIterators()
	    throws ParameterDescriptionNotFoundException,
	    ParameterAdapterException {
	app.createAdapters();
	Iterator all = app.parameterAdapters();
	Iterator inputs = app.inputParameterAdapters();
	Iterator outputs = app.outputParameterAdapters();
	assertTrue(all.hasNext());
	assertTrue(inputs.hasNext());
	assertTrue(outputs.hasNext());
	while (all.hasNext()) {
	    if (inputs.hasNext()) {
		assertEquals(inputs.next(), all.next());
	    } else {
		assertEquals(outputs.next(), all.next());
	    }
	}
	assertFalse(inputs.hasNext());
	assertFalse(outputs.hasNext());
    }

    @Test
    public void testCreateTemplateMessage() {
	MessageType mt = app.createTemplateMessage();
	assertNotNull(mt);
	assertEquals(mt.getPhase(), app.getStatus().toExecutionPhase());
    }

    @Test
    public void testReportError() {
	app.reportError("test error");
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	System.out.println(obs.arg.getClass().getName());
	MessageType mt = (MessageType) obs.arg;
	assertEquals(LogLevel.ERROR, mt.getLevel());
	assertEquals("test error", mt.getContent());
	assertEquals(Status.ERROR, app.getStatus());

    }

    @Test
    public void testReportWarning() {
	Status orig = app.getStatus();
	app.reportWarning("test warning");
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	assertTrue(obs.arg instanceof MessageType);
	MessageType mt = (MessageType) obs.arg;
	assertEquals(LogLevel.WARN, mt.getLevel());
	assertEquals("test warning", mt.getContent());
	assertEquals(orig, app.getStatus()); // app status remains unchanged
    }

    @Test
    public void testReportMessage() {
	Status orig = app.getStatus();
	app.reportMessage("test warning");
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	assertTrue(obs.arg instanceof MessageType);
	MessageType mt = (MessageType) obs.arg;
	assertEquals(LogLevel.INFO, mt.getLevel());
	assertEquals("test warning", mt.getContent());
	assertEquals(orig, app.getStatus()); // app status remains unchanged

    }

    @Test
    public void testReportErrorException() {
	Exception e = new Exception();
	app.reportError("test error", e);
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	System.out.println(obs.arg.getClass().getName());
	MessageType mt = (MessageType) obs.arg;
	assertEquals(LogLevel.ERROR, mt.getLevel());
	assertTrue(mt.getContent().startsWith("test error"));
	assertEquals(Status.ERROR, app.getStatus());
    }

    @Test
    public void testReportWarningException() {
	Status orig = app.getStatus();
	Exception e = new Exception();
	app.reportWarning("test warning", e);
	assertTrue(obs.called);
	assertNotNull(obs.arg);
	assertTrue(obs.arg instanceof MessageType);
	MessageType mt = (MessageType) obs.arg;
	assertEquals(LogLevel.WARN, mt.getLevel());
	assertTrue(mt.getContent().startsWith("test warning"));
	assertEquals(orig, app.getStatus()); // app status remains unchanged
    }

    @Test
    public void testEnqueue() throws Exception {
	app.enqueue();
	assertEquals(Status.QUEUED, app.getStatus());
    }

    protected static class TestObserver implements Observer {
	public boolean called;
	public Object arg;

	/**
	 * @see java.util.Observer#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
	    if (!called) { // only preserve first message
		called = true;
		this.arg = arg;
	    }
	}
    }

}

/*
 * $Log: AbstractApplicationTest.java,v $
 * Revision 1.11  2008/10/06 12:16:16  pah
 * factor out classes common to server and client
 *
 * Revision 1.10  2008/09/24 13:40:50  pah
 * package naming changes
 *
 * Revision 1.9  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 * Revision 1.8  2008/09/03 14:19:04  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.7.2.10  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.7.2.9  2008/08/29 07:28:30  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.7.2.8  2008/06/16 21:58:59  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.7.2.7  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.7.2.6  2008/06/10 20:01:39  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.7.2.5  2008/05/01 15:22:48  pah
 * updates to tool
 *
 * Revision 1.7.2.4  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 * Revision 1.7.2.3  2008/04/17 16:08:33  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.7.2.2  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 * Revision 1.7.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * Revision 1.7 2007/09/28 18:03:36 clq2
 * apps_gtr_2303
 * 
 * Revision 1.6.82.1 2007/09/25 08:29:03 gtr I added a test for enqueue(). The
 * app in setUp() now implements createExecutionTask() instead of execute().
 * 
 * Revision 1.6 2005/08/10 17:45:10 clq2 cea-server-nww-improve-tests
 * 
 * Revision 1.5.88.1 2005/07/21 18:12:38 nw fixed up tests - got all passing,
 * improved coverage a little. still could do with testing the java apps.
 * 
 * Revision 1.5 2004/11/27 13:20:02 pah result of merge of pah_cea_bz561 branch
 * 
 * Revision 1.4.70.1 2004/11/09 09:21:16 pah initial attempt to rationalise
 * authorityID use & self registering
 * 
 * Revision 1.4 2004/07/26 12:07:38 nw renamed indirect package to protocol,
 * renamed classes and methods within protocol package javadocs
 * 
 * Revision 1.3 2004/07/22 16:34:48 nw cleaned up application / parameter
 * adapter interface.
 * 
 * Revision 1.2 2004/07/01 11:16:22 nw merged in branch
 * nww-itn06-componentization
 * 
 * Revision 1.1.2.3 2004/07/01 01:42:47 nw final version, before merge
 * 
 * Revision 1.1.2.2 2004/06/17 09:21:23 nw finished all major functionality
 * additions to core
 * 
 * Revision 1.1.2.1 2004/06/14 08:56:58 nw factored applications into
 * sub-projects, got packaging of wars to work again
 * 
 */