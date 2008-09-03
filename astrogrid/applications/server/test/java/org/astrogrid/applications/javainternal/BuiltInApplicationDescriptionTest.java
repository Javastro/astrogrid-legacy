/*
 * $Id: BuiltInApplicationDescriptionTest.java,v 1.2 2008/09/03 14:19:07 pah Exp $
 * 
 * Created on 21 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.javainternal;

import static org.junit.Assert.*;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BuiltInApplicationDescription;
import org.astrogrid.applications.description.SimpleApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.community.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BuiltInApplicationDescriptionTest {

    protected Application app;
    protected ApplicationDescriptionLibrary lib;
    protected BuiltInApplicationDescription appDesc;
    protected User user;
    protected Tool tool;
    protected ExecutionHistory history;
    private static DefaultProtocolLibrary protocolLib;
    protected static CEAConfiguration conf;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	       protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	       assertTrue(protocolLib.isProtocolSupported("file"));
               @SuppressWarnings("unused")
	       InternalCeaComponentFactory internal = new InternalCeaComponentFactory(protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());
		   }

    
    @Before
    public void setUp() throws Exception {
	conf = new MockNonSpringConfiguredConfig();
	appDesc = new BuiltInApplicationDescription( conf);
	assertNotNull(appDesc);
	user = new User("pah@test.org","group","token");

        history = new InMemoryExecutionHistory();
      
        lib = new SimpleApplicationDescriptionLibrary(new MockNonSpringConfiguredConfig());
	tool = new Tool();
	tool.setInterface("default");
	tool.getInput().addParameter(new ParameterValue("in1","1.1"));
	tool.getInput().addParameter(buildParameter2());
	ParameterValue param = new ParameterValue();
	param.setId("in3");
	param.setValue(sleeptime());
	tool.getInput().addParameter(param);
	param = new ParameterValue();
	param.setId("out");
	tool.getOutput().addParameter(param );
	tool.setId("ivo://org.astrogrid.unregistered/default");
    }

    protected ParameterValue buildParameter2() {
	return new ParameterValue("in2", getPar2value());
    }


    protected String sleeptime() {
	return "1";
    }


    @Test
    public void testInitializeApplication() throws CeaException {
 	
	
	app = appDesc.initializeApplication("test1", user , tool );
    }

    @Test
    public void testRunApplication() throws CeaException {
	testInitializeApplication();
	Runnable task = app.createExecutionTask();
	task.run();
	ResultListType result = app.getResult();
	assertNotNull(result);
	assertTrue("result has one member", result.getResult().size() == 1);
	assertEquals("value of result", "1.1 "+getPar2value()+" "+sleeptime(), result.getResult().get(0).getValue());
    }


    protected String getPar2value() {
        return "2";
    }
}


/*
 * $Log: BuiltInApplicationDescriptionTest.java,v $
 * Revision 1.2  2008/09/03 14:19:07  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.8  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.7  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.6  2008/06/16 21:58:59  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.1.2.5  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.1.2.4  2008/06/10 20:01:39  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.1.2.3  2008/05/17 16:45:01  pah
 * tidy tests to make sure more are passing
 *
 * Revision 1.1.2.2  2008/05/01 15:22:47  pah
 * updates to tool
 *
 * Revision 1.1.2.1  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 */
