/*$Id: BasicWorkflowBuilderTest.java,v 1.2 2004/03/11 13:53:51 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class BasicWorkflowBuilderTest extends TestCase {
    /**
     * Constructor for BasicWorkflowBuilderTest.
     * @param arg0
     */
    public BasicWorkflowBuilderTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        builder = new BasicWorkflowBuilder();

    }
    protected BasicWorkflowBuilder builder;
    public static Credentials creds;
    static {
        creds = new Credentials();
        Account acc = new Account();
        acc.setCommunity("jodrell");
        acc.setName("noel");
        creds.setAccount(acc);        
        Group group = new Group();
        group.setCommunity("jodrell");
        group.setName("developers");
        creds.setGroup(group);
        creds.setSecurityToken("foo");
    }
    
    /** sanity check test */
    public void testValidCredentials() {
        assertNotNull(creds);
        assertTrue (creds.isValid());
    }
    
    public void testCreateWorkflowIsValid() throws Exception {
        Workflow wf = builder.createWorkflow(creds,"name","description");
        assertNotNull(wf);
        assertTrue(wf.isValid()); 
    }
}


/* 
$Log: BasicWorkflowBuilderTest.java,v $
Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/