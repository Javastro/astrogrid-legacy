/*$Id: CommandLineCEAComponentManagerTest.java,v 1.4 2009/05/15 22:51:19 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.execution.Tool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Test the component manager assembled for the commandline cea is valid - i.e. has all the necessary components registered.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author pharriso@eso.org 03-Jun-2005 - make it do a "semi-live" test
 *
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/ceaspringTest.xml"}) 
public class CommandLineCEAComponentManagerTest 
    extends AbstractComponentManagerTestCase {
    private ApplicationDescriptionLibrary dl;
    private ExecutionController ec;
    private CommandLineApplicationDescription desc;
    private ExecutionHistory eh;
    private SecurityGuard secGuard = new SecurityGuard();//TODO will want to test the operation of securityGuard in future

    @Override
    public void setUp() throws Exception {
	super.setUp();
	ec = this.manager.getExecutionController();
	dl =  manager.getApplicationDescriptionLibrary();
	eh = (ExecutionHistory) manager.getExecutionHistoryService();
	assertNotNull("CommandLineDescriptionLoader was not instantiated ", dl);
	desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
	assertNotNull("Application description was not obtained", desc);
	Toolbuilder.fixupExecutionPath(desc);
	
    }

   @Test
    public void testExecutionHistory() throws Exception {
     assertNotNull(eh);
   }
   
   @Test
  public void testExecutionController() throws Exception {
     assertNotNull(ec);
     assertTrue("execution controller is actually "+ ec.getClass().getCanonicalName(),ec instanceof ThreadPoolExecutionController);
   }
   
   @Test
  public void testParameterValues() throws Exception
   {
       Tool tool = Toolbuilder.buildTool("0", desc);
       Toolbuilder.fillDirect(tool);
       String exid = ec.init(tool, "CommandLineCEAComponentManagerTest", secGuard);
       Application app = eh.getApplicationFromCurrentSet(exid);
       app.checkParameterValues();  
   }

   @Test
   public void testParameterSetup() throws Exception
   {
       Tool tool = Toolbuilder.buildTool("0", desc);
       Toolbuilder.fillDirect(tool);
       String exid = ec.init(tool, "CommandLineCEAComponentManagerTest", secGuard);
       Application app = eh.getApplicationFromCurrentSet(exid);
       assertTrue(app instanceof CommandLineApplication);
       CommandLineApplication capp = (CommandLineApplication) app;
       capp.setupParameters();
       
       //TODO should test something
       
   }

  @Test
  public void testRun () throws Exception
   {
       
       //FIXME - this does not complete immediately if the app fails
     System.out.println("testUse()");     
      
      Tool tool = Toolbuilder.buildTool("0", desc);
      Toolbuilder.fillDirect(tool);
      String exid = ec.init(tool, "CommandLineCEAComponentManagerTest", secGuard);
      Application app = eh.getApplicationFromCurrentSet(exid);
      assertTrue(app instanceof CommandLineApplication);
      
      app.setRunTimeLimit(20000);
      
      // When execute() is called, the job status should change immediately
      // from INITIALIZED to QUEUED. It should then change asynchronously to
      // RUNNING and then to COMPLETED. Because of the asynchronicity, we may
      // not see the intermediate states. Therefore, we can only test to
      // see that it changes from INITIALIZED.
      assertEquals(Status.INITIALIZED, app.getStatus());
      assertTrue(ec.execute(exid, secGuard));
      assertFalse(app.getStatus().equals(Status.INITIALIZED));
      
      // The execution controller should move the application from current set
      // to archive when the application completes or fails. This should happen
      // well within 20 seconds.
      int i = 0;
      while (eh.isApplicationInCurrentSet(exid)) {
        Thread.sleep(2000);
        i++;
        if (i > 10) {         
          fail("Application did not complete in 20 seconds.");
        }
      }
      assertEquals(Status.COMPLETED, app.getStatus());
      
      QueryService qs = manager.getQueryService(); 
      
      File file = qs.getLogFile(exid, ApplicationEnvironmentRetriver.StdIOType.out);
      assertNotNull(file);
      BufferedReader rd = new BufferedReader(new FileReader(file));
      String line = rd.readLine();
      while(line != null)
      {
         System.err.println(line);
         line = rd.readLine();
      }
    
       
   }
   
   
}


/* 
$Log: CommandLineCEAComponentManagerTest.java,v $
Revision 1.4  2009/05/15 22:51:19  pah
ASSIGNED - bug 2911: improve authz configuration
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
combined agast and old stuff
refactored to a more specific CEA policy interface
made sure that there are decision points nearly everywhere necessary  - still needed on the saved history

Revision 1.3  2008/09/13 09:51:04  pah
code cleanup

Revision 1.2  2008/09/04 19:10:52  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.1  2008/08/29 07:28:27  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.10.2.8  2008/08/02 13:32:23  pah
safety checkin - on vacation

Revision 1.10.2.7  2008/05/01 15:35:55  pah
incorporated chiba xforms

Revision 1.10.2.6  2008/04/23 14:13:50  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.10.2.5  2008/04/17 16:16:55  pah
removed all castor marshalling - even in the web service layer - unit tests passing
some uws functionality present - just the bare bones.

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.10.2.4  2008/04/04 15:34:52  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.10.2.3  2008/03/28 16:44:35  pah
RESOLVED - bug 2683: treatment of boolean values
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2683

Revision 1.10.2.2  2008/03/26 17:29:50  pah
Unit tests pass

Revision 1.10.2.1  2008/03/19 23:15:42  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.10  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.9  2007/09/28 18:03:36  clq2
apps_gtr_2303

Revision 1.8.50.4  2007/09/25 11:03:41  gtr
Buggered around to get the tests to pass. Stupid code, but so much is broken that I can't clean it up.

Revision 1.8.50.3  2007/09/25 09:15:59  gtr
It distinguishes time-outs from other failures.

Revision 1.8.50.2  2007/09/20 13:12:07  gtr
Queuing changes

Revision 1.8.50.1  2007/09/18 12:33:59  gtr
Job-queue changes from apps-gtr-2011 have been merged.

Revision 1.8.44.3  2007/06/18 15:56:08  gtr
I now set the configuration key cea.webapp.url to avoid a confusing error report.

Revision 1.8.44.2  2007/06/18 14:03:13  gtr
I added extra assertions to find out where it's failing.

Revision 1.8.44.1  2007/06/17 22:43:09  gtr
I added tests on the ExecutionHistory and ExecutionController, to see if picocontainer is working properly.

Revision 1.8  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.6  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.3.34.4  2006/01/31 21:39:06  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.3.34.3  2006/01/26 13:16:34  gtr
BasicCommandLineConfiguration has absorbed the functions of TestCommandLineConfiguration.

Revision 1.3.34.2  2005/12/19 18:51:03  gtr
New class, generated in the refactoring for BZ1492.

Revision 1.3.34.1  2005/12/19 18:12:30  gtr
Refactored: changes in support of the fix for 1492.

Revision 1.3  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.166.2  2005/06/09 22:17:58  pah
tweaking the log getter

Revision 1.2.166.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.152.2  2005/06/08 22:10:45  pah
make http applications v10 compliant

Revision 1.2.152.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:48  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/