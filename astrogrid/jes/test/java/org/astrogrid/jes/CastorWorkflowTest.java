/*$Id: CastorWorkflowTest.java,v 1.2 2004/03/04 01:57:35 nw Exp $
 * Created on 03-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.StringWriter;

import junit.framework.TestCase;

/** helper-tests to test assumptions / behaviour of castor-generated model.
 * something breaking in this test is serious - means that libraries have been upgraded, and previous behaviour has changed.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Mar-2004
 *
 */
public class CastorWorkflowTest extends TestCase {
    /**
     * Constructor for CastorWorkflowTest.
     * @param arg0
     */
    public CastorWorkflowTest(String arg0) {
        super(arg0);
    }
    
    /** not really a test, just see what they look like */
    public void testFormOfSimpleWorkflow() throws Exception{
        Workflow wf = new Workflow();
        Credentials creds = new Credentials();
        Account acc = new Account();
        acc.setName("fred");
        acc.setCommunity("jodrell");
        Group group = new Group();
        group.setName("users");
        group.setCommunity("jodrell");
        creds.setAccount(acc);
        creds.setGroup(group);
        creds.setSecurityToken("");
        wf.setCredentials(creds);
        wf.setDescription("Text description");
        wf.setName("demonstration workflow");
        
        Sequence seq = new Sequence();
        Step step = new Step();
        Tool tool = new Tool();
        step.setTool(tool);
        step.setName("single step");
        tool.setName("dummy");
        tool.setInterface("unknown");
        seq.addActivity(step);
        
        wf.setSequence(seq);
        
        //
        assertTrue(wf.isValid());
        StringWriter sw = new StringWriter();
        wf.marshal(sw);
        sw.close();
        System.out.println(sw.toString());
    }
}


/* 
$Log: CastorWorkflowTest.java,v $
Revision 1.2  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/