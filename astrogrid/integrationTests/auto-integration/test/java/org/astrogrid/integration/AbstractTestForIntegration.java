/*$Id: AbstractTestForIntegration.java,v 1.7 2004/09/02 11:18:09 jdt Exp $
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

import net.sourceforge.groboutils.junit.v1.IntegrationTestCase;

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
public class AbstractTestForIntegration extends IntegrationTestCase {
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
        assertNotNull("astrogrid instance is null",ag);
        // credentials object
        assertNotNull("object builder is null",ag.getObjectBuilder());
        acc = ag.getObjectBuilder().createAccount(USERNAME,COMMUNITY); // will want to change this to a standard user later.
        group = ag.getObjectBuilder().createGroup("devel",COMMUNITY);
        creds = ag.getObjectBuilder().createCredendtials(acc,group);

        //equivalent user object
        user = new User();
        user.setAccount(creds.getAccount().getName() + "@" + creds.getAccount().getCommunity());
        user.setGroup(creds.getGroup().getName() + "@" + creds.getGroup().getCommunity());
        user.setToken(creds.getSecurityToken());        
        userIvorn = new Ivorn("ivo://"+COMMUNITY+"/"+USERNAME);
        mySpaceIvorn = new Ivorn("ivo://"+MYSPACE);
      

        wf = ag.getWorkflowManager().getWorkflowBuilder().createWorkflow(creds,"test workflow","a description");    
    }
   
    protected Astrogrid ag;
    protected Account acc;
    protected Group group;   
    protected Credentials creds;
    protected User user;
    protected Workflow wf;
   protected Ivorn userIvorn;
   protected Ivorn mySpaceIvorn;
    
    public static final String USERNAME = "frog";
    public static final String COMMUNITY = "org.astrogrid.localhost";
    public static final String MYSPACE = COMMUNITY + "/myspace";
    public static final String TESTDSA = COMMUNITY + "/testdsa";
    public static final String TESTAPP = COMMUNITY + "/testapp";
    public static final String TESTAPP2 = COMMUNITY + "/testap2"; //note it isn't double 'p'
    public static final String HELLO_WORLD = COMMUNITY + "/helloWorld";
    public static final String HELLO_YOU = COMMUNITY + "/helloYou";
    public static final String SUM = COMMUNITY + "/sum";
    //New Http-based apps
    public static final String HTTP_HELLO_WORLD=COMMUNITY+"/HelloWorldHttpApp";
    public static final String HTTP_ADDER = COMMUNITY+"/AdderHttpApp";
    public static final String HTTP_INVALID = COMMUNITY+"/InvalidHttpApp";
    
   protected Ivorn createIVORN(String path)
   {
      return new Ivorn(MYSPACE,user.getUserId()+path);
   }
   
}


/* 
$Log: AbstractTestForIntegration.java,v $
Revision 1.7  2004/09/02 11:18:09  jdt
Merges from case 3 branch for SIAP.

Revision 1.6  2004/08/31 23:45:17  nw
added sanity checking assertions

Revision 1.5  2004/08/10 11:06:09  nw
fixed breakage due to my refactoring of astrogrid-scripting.
sorry about that.

Revision 1.4  2004/07/01 11:47:17  nw
made our integration-test base case extend groboUtils IntegrationTestCase
this means that 'softAssertions' can be used in any of the integration tests now.
soft assertion - the test will continue, even if the assertion fails
(very handy for testing a lot of things after a complex initialization).
each soft assertion appears as a separate test in the reports.

Revision 1.3  2004/05/17 12:37:31  pah
Improve CEA tests that call application controller directly

Revision 1.2  2004/04/23 00:27:13  nw
added constant for testap2

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