/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.manager.persist;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class InMemoryExecutionHistoryTest extends TestCase {
    /**
     * Constructor for InMemoryExecutionHistoryTest.
     * @param arg0
     */
    public InMemoryExecutionHistoryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(new InMemoryIdGen(),null, new TestAuthorityResolver());
        lib = new BaseApplicationDescriptionLibrary(env);
        appDesc = lib.getDescription(lib.getApplicationNames()[0]);
        assertNotNull(appDesc);
        Tool tool = new Tool();
        tool.setInput(new Input());
        tool.setOutput(new Output());
        app = appDesc.initializeApplication("foo",new User(),tool);
        eh = new InMemoryExecutionHistory();
        id = app.getID();
    }
    protected ExecutionHistory eh;
    protected ApplicationDescriptionLibrary lib;
    protected ApplicationDescription appDesc;
    protected Application app;
    protected String id;
    
    public void testCurrentSet() throws Exception {
        assertFalse(eh.isApplicationInCurrentSet(id));
        eh.addApplicationToCurrentSet(app);
        assertTrue(eh.isApplicationInCurrentSet(id));
        Application app1 = eh.getApplicationFromCurrentSet(id);
        assertEquals(app,app1);
    }
    
    public void testArchive() throws Exception {
        eh.addApplicationToCurrentSet(app);
        eh.moveApplicationFromCurrentSetToArchive(id);
        assertFalse(eh.isApplicationInCurrentSet(id));
        ExecutionSummaryType summary = eh.getApplicationFromArchive(id);
        assertNotNull(summary);
    }
    
    public void testArchiveMissing() throws Exception {
        try {
            eh.getApplicationFromArchive("unknown");
            fail("expected to barf");
        } catch (ExecutionIDNotFoundException e) {
            // expected
        }
    }
    
    public void testCurrentSetMissing() throws Exception {
        try {
            eh.getApplicationFromCurrentSet("unknown");
            fail("expected to barf");
        } catch (ExecutionIDNotFoundException e) {
            // expected
        }        
    }
}
