/*$Id: AbstractTestForIntegration.java,v 1.1 2004/04/21 13:42:26 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.integration;

import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.TestCase;

/** abstract test for integration - sets up the Astorgrid scripting object, commonly used objects, etc.
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
        // credentials object
        acc = ag.getObjectHelper().createAccount("frog",COMMUNITY); // will want to change this to a standard user later.
        group = ag.getObjectHelper().createGroup("devel",COMMUNITY);
        creds = ag.getObjectHelper().createCredendtials(acc,group);

        //equivalent user object
        user = new User();
        user.setAccount(creds.getAccount().getName() + "@" + creds.getAccount().getCommunity());
        user.setGroup(creds.getGroup().getName() + "@" + creds.getGroup().getCommunity());
        user.setToken(creds.getSecurityToken());        

        wf = ag.getWorkflowManager().getWorkflowBuilder().createWorkflow(creds,"test workflow","a description");    
    }
   
    protected Astrogrid ag;
    protected Account acc;
    protected Group group;   
    protected Credentials creds;
    protected User user;
    protected Workflow wf;
    
    public static final String COMMUNITY = "org.astrogrid.localhost";
    public static final String MYSPACE = COMMUNITY + "/myspace";
    public static final String TESTDSA = COMMUNITY + "/testdsa";
    public static final String TESTAPP = COMMUNITY + "/testapp";
    
   protected Ivorn createIVORN(String path)
   {
      return new Ivorn(MYSPACE,user.getUserId()+path);
   }
   
}


/* 
$Log: AbstractTestForIntegration.java,v $
Revision 1.1  2004/04/21 13:42:26  nw
moved abstract test up to root of package hierarchy

Revision 1.5  2004/04/19 11:42:41  pah
added createIVORN

Revision 1.4  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.3  2004/04/15 23:11:20  nw
tweaks

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/