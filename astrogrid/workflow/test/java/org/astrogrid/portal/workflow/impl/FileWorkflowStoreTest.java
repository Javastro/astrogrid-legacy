/*$Id: FileWorkflowStoreTest.java,v 1.2 2004/03/11 13:53:51 nw Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class FileWorkflowStoreTest extends TestCase {
    /**
     * Constructor for FileWorkflowStoreTest.
     * @param arg0
     */
    public FileWorkflowStoreTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        File f = File.createTempFile(this.getClass().getName(),null);
        f.delete();
        f.mkdir();
        assertTrue(f.exists());
        assertTrue(f.isDirectory());
        store = new FileWorkflowStore(f);
        creds = BasicWorkflowBuilderTest.creds;
        assertNotNull(creds);
        wf = (new BasicWorkflowBuilder()).createWorkflow(creds,"test workflow","description");
        assertNotNull(wf);
    }
    
    protected FileWorkflowStore store;
    protected Workflow wf;
    protected Credentials creds;
    
    public void testLoadSaveLoadWorkflow() throws Exception {
        try {
            store.readWorkflow(creds.getAccount(),wf.getName());
            fail("expected to barf");
        } catch (WorkflowInterfaceException e) {
            // thats fine.
        } 
        store.saveWorkflow(creds.getAccount(),wf);
        
        Workflow wf1 = store.readWorkflow(creds.getAccount(),wf.getName());
        assertNotNull(wf1);
        assertEquals(wf1.getName(),wf.getName());
    }
    
    public void testDeleteSaveDeleteLoadWorkflow() throws Exception {
        store.deleteWorkflow(creds.getAccount(),wf.getName()); // should fail silently.
        
        store.saveWorkflow(creds.getAccount(),wf);
        store.deleteWorkflow(creds.getAccount(),wf.getName());
        
        try {
            store.readWorkflow(creds.getAccount(),wf.getName());
            fail("expected to barf");
        } catch(WorkflowInterfaceException e) {
            // that'll do
        }
    }
    
    public void testListSaveListWorkflow() throws Exception {
        String[] list = store.listWorkflows(creds.getAccount());
        assertNotNull(list);
        assertEquals(0,list.length);
        
        store.saveWorkflow(creds.getAccount(),wf);
        
        String[] list1 = store.listWorkflows(creds.getAccount());
        assertNotNull(list1);
        assertEquals(1,list1.length);
        assertEquals(list1[0],wf.getName());        
    }
    
    public void testQueries() throws Exception {
        String[] list = store.listQueries(creds.getAccount());
        assertNotNull(list);
        assertEquals(0,list.length);
        
        try {
            store.readQuery(creds.getAccount(),"foo");
            fail("expected to barf");
        } catch (WorkflowInterfaceException e) {
            // ok
        }
    }
}


/* 
$Log: FileWorkflowStoreTest.java,v $
Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/