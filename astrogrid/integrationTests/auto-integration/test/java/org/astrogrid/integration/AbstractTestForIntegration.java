/*$Id: AbstractTestForIntegration.java,v 1.18 2004/11/22 15:42:08 jdt Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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
    private static final Log memLog = LogFactory.getLog("MEMORY");
        
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
// don't do this for now - using other monitoring tools   - might speed up general progress slightly....     
//        suggestGC();
//        checkMemory("pre:" + this.getClass().getName());
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
    
    protected void tearDown() throws Exception {
//        checkMemory("post: " + this.getClass().getName());
    }
    
    /** call page on tomcat that produces memory statistics */
    protected void checkMemory(String checkpoint) {
        try {
        WebConversation conv = new WebConversation();
        conv.setAuthorization(SimpleConfig.getProperty("org.astrogrid.memory.user"),SimpleConfig.getProperty("org.astrogrid.memory.pass"));
        GetMethodWebRequest gmr = new GetMethodWebRequest(SimpleConfig.getProperty("org.astrogrid.memory.endpoint"));        
        WebResponse resp = conv.sendRequest(gmr);
        Document dom = resp.getDOM();
        Element el = (Element)dom.getElementsByTagName("memory").item(0);
        StringBuffer message = new StringBuffer();
        message.append(checkpoint);
        message.append(",");
        message.append(el.getAttribute("free"));
        message.append(",");
        message.append(el.getAttribute("total"));        
        message.append(",");
        message.append(el.getAttribute("max"));        
        System.out.println("MEMORY: " + message);
        memLog.info(message);
        } catch (Throwable t) {
            memLog.warn("Failed to check memory at " + checkpoint,t);
        }
    }
    /** suggest to tomcat that a garbage collection might be a good idea */
    protected void suggestGC() {
        try {
           URL u = new URL(SimpleConfig.getProperty("org.astrogrid.gc.endpoint"));
           u.openStream().read(); //should be enough to trigger the jsp.
        } catch (Throwable t) {
            memLog.warn("failed to suggest GC ",t);
        }
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
    public static final String AUTHORITYID = SimpleConfig.getProperty("org.astrogrid.registry.authorityid", "org.astrogrid.localhost");
    public static final String COMMUNITY = SimpleConfig.getProperty("org.astrogrid.community.ident", "org.astrogrid.localhost");
    public static final String MYSPACE = AUTHORITYID + "/myspace";
    public static final String TESTDSA = AUTHORITYID + "/pal-sample/ceaApplication";
    public static final String TESTAPP = AUTHORITYID + "/testapp";
    public static final String TESTAPP2 = AUTHORITYID + "/testap2"; //note it isn't double 'p'
    public static final String HELLO_WORLD = AUTHORITYID + "/helloWorld";
    public static final String HELLO_YOU = AUTHORITYID + "/helloYou";
    public static final String SUM = AUTHORITYID + "/sum";
    //New Http-based apps
    public static final String HTTP_HELLO_WORLD=AUTHORITYID+"/HelloWorldHttpApp";
    public static final String HTTP_ADDER_GET = AUTHORITYID+"/AdderHttpGetApp";
    public static final String HTTP_ADDER_POST = AUTHORITYID+"/AdderHttpPostApp";
    public static final String HTTP_INVALID = AUTHORITYID+"/InvalidHttpApp";
    public static final String HTTP_HELLO_YOU = AUTHORITYID+"/HelloYouXMLHttpApp";
    
   protected Ivorn createIVORN(String path)
   {
      return new Ivorn(MYSPACE,user.getUserId()+path);
   }

   /**
    * Adds the current timestamp to create a unique file name.
    * @param path The MySpace file path (without the user name).
    * @param path The MySpace file name (without the type extension).
    * @param path The MySpace file type (.vot, .job etc ..).
    * @return A new MySpace Ivorn 'ivo://[myspace-service]/[user]/[path]/[name]-[timestamp].[type]'
    * @todo This needs refactoring to use Ivorn factories.
    *
    */
   protected Ivorn createUniqueIVORN(String path, String name, String type)
   {
   StringBuffer buffer = new StringBuffer() ;
   buffer.append(
     user.getUserId()
     ) ;
   buffer.append("/") ;
   buffer.append(path) ;
   buffer.append("/") ;
   buffer.append(name) ;
   buffer.append("-") ;
   buffer.append(
     String.valueOf(
       System.currentTimeMillis()
       )
     ) ;
   buffer.append(".") ;
   buffer.append(type) ;
   return new Ivorn(
     MYSPACE,
     buffer.toString()
     );
   }
}


/* 
$Log: AbstractTestForIntegration.java,v $
Revision 1.18  2004/11/22 15:42:08  jdt
Fix for applications/testdsa problem.

Revision 1.17  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.15  2004/09/30 15:19:53  pah
remove the GC force

Revision 1.14  2004/09/30 10:39:15  pah
get community and authorityid from the config if possible

Revision 1.13  2004/09/14 23:25:43  nw
added call to garbage-collection jsp before each test.

Revision 1.12  2004/09/14 17:02:19  nw
added code to record memory usage.

Revision 1.11  2004/09/14 16:35:15  jdt
Added tests for an http-post service.

Revision 1.10  2004/09/14 08:32:06  pah
added an authorityID constant - this is different from to the community in general

Revision 1.9  2004/09/13 18:14:53  jdt
Added a new cea-http integration test to see if saving xml to
mySpace is causing any problems.

Revision 1.8  2004/09/09 01:19:50  dave
Updated MIME type handling in MySpace.
Extended test coverage for MIME types in FileStore and MySpace.
Added VM memory data to community ServiceStatusData.

Revision 1.7.6.1  2004/09/07 13:27:03  dave
Added unique ivorn factory to abstract test.

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