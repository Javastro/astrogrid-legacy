/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.manager.persist;

import static org.junit.Assert.*;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class InMemoryExecutionHistoryTest   {
    /**
     * Constructor for InMemoryExecutionHistoryTest.
     * @param arg0
     */
    public InMemoryExecutionHistoryTest() {
      
    }
    
    @BeforeClass
    public static void beforeClass()
    {
        ProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	internal = new InternalCeaComponentFactory(protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());

    }
    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
       
        
        lib = new org.astrogrid.applications.description.SimpleApplicationDescriptionLibrary( new MockNonSpringConfiguredConfig());
        appDesc = lib.getDescription(lib.getApplicationNames()[0]);
        assertNotNull(appDesc);
        Tool tool = new Tool();
        ListOfParameterValues input = new ListOfParameterValues();
	tool.setInput(input );
        ListOfParameterValues output = new ListOfParameterValues();
	tool.setOutput(output );
	System.out.println("init app in execution history memory test setup()");
        app = appDesc.initializeApplication("foo",new User(),tool);
        eh = new InMemoryExecutionHistory();
        id = app.getId();
    }
    protected ExecutionHistory eh;
    protected ApplicationDescriptionLibrary lib;
    protected ApplicationDescription appDesc;
    protected Application app;
    protected String id;
    private static InternalCeaComponentFactory internal;
    
    @Test
    public void testCurrentSet() throws Exception {
        assertFalse(eh.isApplicationInCurrentSet(id));
        eh.addApplicationToCurrentSet(app);
        assertTrue(eh.isApplicationInCurrentSet(id));
        Application app1 = eh.getApplicationFromCurrentSet(id);
        assertEquals(app,app1);
    }
    
    @Test
    public void testArchive() throws Exception {
        eh.addApplicationToCurrentSet(app);
        eh.moveApplicationFromCurrentSetToArchive(id);
        assertFalse(eh.isApplicationInCurrentSet(id));
        ExecutionSummaryType summary = eh.getApplicationFromArchive(id);
        assertNotNull(summary);
        //test a couple of values to make sure that the save and retrieve really happened.
        assertEquals("app id", appDesc.getId(), summary.getApplicationName());
        assertEquals("job id", id,summary.getJobId());
        //TODO test more of the summary values....
     }
    
    @Test
    public void testArchiveMissing() throws Exception {
        try {
            eh.getApplicationFromArchive("unknown");
            fail("expected to barf");
        } catch (ExecutionIDNotFoundException e) {
            // expected
        }
    }
    
    @Test
    public void testCurrentSetMissing() throws Exception {
        try {
            eh.getApplicationFromCurrentSet("unknown");
            fail("expected to barf");
        } catch (ExecutionIDNotFoundException e) {
            // expected
        }        
    }
}
