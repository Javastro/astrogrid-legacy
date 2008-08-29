/*$Id: ExecutionTest.java,v 1.1 2008/08/29 07:28:28 pah Exp $
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
import org.astrogrid.applications.commandline.DescriptionBaseTestCase.TestAppInfo;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ConfigFileReadingDescriptionLibrary;
import org.astrogrid.applications.description.execution.Tool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
 * JUnit test for execution of a job. This exercises
 * {@link CommandLineApplication} and {@link QueuedJobList}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class) 
public class ExecutionTest extends AbstractCmdLineAppTestCase {
 
    @Override
    protected TestAppInfo setupApplication() {
        return appInfo; // do nothing - the default that is setup is correct for the "test application"
    }
  
    @Test
    public void testUse () throws Exception
   {
       System.out.println("testUse()");
      // TODO This is only a very basic stab at a test - main intention was to be able to run the querier test.
      ApplicationDescriptionLibrary dl =  manager.getApplicationDescriptionLibrary();
      assertNotNull("CommandLineDescriptionLoader was instantiated", dl);
      CommandLineApplicationDescription desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
      assertNotNull("Application description was obtained", desc);
      Toolbuilder.fixupExecutionPath(desc);
      
      ExecutionController ec = manager.getExecutionController();
      ExecutionHistory eh = manager.getExecutionHistoryService();
      
      Tool tool = Toolbuilder.buildTool("0", desc);
      Toolbuilder.fillDirect(tool);
      String exid = ec.init(tool, "CommandLineCEAComponentManagerTest");
      Application app = eh.getApplicationFromCurrentSet(exid);
      assertTrue(app instanceof CommandLineApplication);
           
      // When execute() is called, the job status should change immediately
      // from INITIALIZED to QUEUED. It should then change asynchronously to
      // RUNNING and then to COMPLETED.
      assertEquals(Status.INITIALIZED, app.getStatus());
      assertTrue(ec.execute(exid));
      assertTrue(app.getStatus().equals(Status.QUEUED));
      
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

    @Override
    protected Tool buildTool(String delay) throws Exception {
	      return Toolbuilder.buildTool(delay, testAppDescr);
   }
 
    @Test
    public void testAbort() throws Exception{
	      ApplicationDescriptionLibrary dl =  manager.getApplicationDescriptionLibrary();
	      assertNotNull("CommandLineDescriptionLoader was instantiated", dl);
	      CommandLineApplicationDescription desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
	      assertNotNull("Application description was obtained", desc);
	      Toolbuilder.fixupExecutionPath(desc);
	      
	      ExecutionController ec = manager.getExecutionController();
	      ExecutionHistory eh = manager.getExecutionHistoryService();
	      
	      Tool tool = Toolbuilder.buildTool("15", desc);
	      Toolbuilder.fillDirect(tool);
	      String exid = ec.init(tool, "CommandLineCEAComponentManagerTest");
	      Application app = eh.getApplicationFromCurrentSet(exid);
	      assertTrue(app instanceof CommandLineApplication);
	           
	      // When execute() is called, the job status should change immediately
	      // from INITIALIZED to QUEUED. It should then change asynchronously to
	      // RUNNING and then to COMPLETED.
	      assertEquals(Status.INITIALIZED, app.getStatus());
	      assertTrue(ec.execute(exid));
	      assertTrue(app.getStatus().equals(Status.QUEUED)); // should catch this in time
	      Thread.sleep(1000); // wait for it to get going
	      assertEquals("Running Status", Status.RUNNING,app.getStatus());
              assertTrue("abort failed", ec.abort(exid));
              assertTrue(app.getStatus().equals(Status.ABORTED));
    }
    
    
    @Test
    public void testAppOverrunDeadline() throws Exception{
    ApplicationDescriptionLibrary dl =  manager.getApplicationDescriptionLibrary();
    assertNotNull("CommandLineDescriptionLoader was instantiated", dl);
    CommandLineApplicationDescription desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
    assertNotNull("Application description was obtained", desc);
    Toolbuilder.fixupExecutionPath(desc);
    
    ExecutionController ec = manager.getExecutionController();
    ExecutionHistory eh = manager.getExecutionHistoryService();
    
    Tool tool = Toolbuilder.buildTool("15", desc);
    Toolbuilder.fillDirect(tool);
    String exid = ec.init(tool, "CommandLineCEAComponentManagerTest");
    Application app = eh.getApplicationFromCurrentSet(exid);
    assertTrue(app instanceof CommandLineApplication);
         
    // When execute() is called, the job status should change immediately
    // from INITIALIZED to QUEUED. It should then change asynchronously to
    // RUNNING and then to COMPLETED.
    assertEquals(Status.INITIALIZED, app.getStatus());
    assertTrue(ec.execute(exid));
    assertTrue(app.getStatus().equals(Status.QUEUED)); // should catch this in time
    Thread.sleep(1000); // wait for it to get going
    assertEquals("Running Status", Status.RUNNING,app.getStatus());
    Thread.sleep(6000); //wait for the execution controller to have time to kill past deadline
    assertEquals("Final phase", Status.ABORTED, app.getStatus());
    }
 
   
}
