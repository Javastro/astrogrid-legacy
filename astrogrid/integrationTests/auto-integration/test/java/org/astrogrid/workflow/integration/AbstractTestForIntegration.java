/*$Id: AbstractTestForIntegration.java,v 1.2 2004/04/08 14:50:54 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.TestCase;

/** abstract test for integration - just sets up the Astorgrid scripting object
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class AbstractTestForIntegration extends TestCase {
    /**
     * Constructor for AbstractTestForIntegration.
     * @param arg0
     */
    public AbstractTestForIntegration(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ag = Astrogrid.getInstance();
        acc = ag.getObjectHelper().createAccount("noel","jodrell"); // will want to change this to a standard user later.
        group = ag.getObjectHelper().createGroup("devel","jodell");
        creds = ag.getObjectHelper().createCredendtials(acc,group);

        wf = ag.getWorkflowManager().getWorkflowBuilder().createWorkflow(creds,"test workflow","a description");    
    }
    
    protected Astrogrid ag;
    protected Account acc;
    protected Group group;   
    protected Credentials creds;
    protected Workflow wf;
}


/* 
$Log: AbstractTestForIntegration.java,v $
Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/