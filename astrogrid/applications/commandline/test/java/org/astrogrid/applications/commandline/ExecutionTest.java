/*$Id: ExecutionTest.java,v 1.2 2007/09/28 18:03:36 clq2 Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.component.AbstractComponentManagerTestCase;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.workflow.beans.v1.Tool;
import org.picocontainer.PicoContainer;

/** 
 * JUnit test for execution of a job. This exercises
 * {@link CommandLineApplication} and {@link QueuedJobList}.
 *
 */
public class ExecutionTest extends TestCase {
    
    CommandLineCEAComponentManager manager;
    
    public void setUp() throws Exception {
    
      this.manager = new CommandLineCEAComponentManager();
      
      BasicCommandLineConfiguration configuration
         = new BasicCommandLineConfiguration();
      
      // Impose a registry template; use one bundled in the jar.
      try {
        URL source = this.getClass().getResource("/CEARegistryTemplate.xml"); 
        assertNotNull(source);
        configuration.setRegistrationTemplate(source); 
      }
      catch (Exception e) {
        fail("Can't set the registry template for the test. " + e);
      }
      
      // Impose an application description file; use one bundled in the jar.
      try { 
        URL source = this.getClass().getResource("/TestApplicationConfig.xml"); 
        assertNotNull(source);
        configuration.setApplicationDescription(source);
      }
      catch (Exception e) {
        fail("Can't set the application description for the test. " + e);
      }
      this.manager.setCommandLineConfigurationInstance(configuration);
      
      this.manager.start();
    }
   
   
   public void testUse () throws Exception
   {
     System.out.println("testUse()");
     SimpleConfig.getSingleton().setProperty("cea.webapp.url", "http://foo.bar/baz");
     
      // TODO This is only a very basic stab at a test - main intention was to be able to run the querier test.
      CommandLineDescriptionsLoader dl = (CommandLineDescriptionsLoader) manager.getContainer().getComponentInstanceOfType(CommandLineDescriptionsLoader.class);
      assertNotNull("CommandLineDescriptionLoader was instantiated", dl);
      CommandLineApplicationDescription desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
      assertNotNull("Application description was obtained", desc);
      Toolbuilder.fixupExecutionPath(desc);
      
      ExecutionController ec = manager.getExecutionController();
      ExecutionHistory eh = (ExecutionHistory) manager.getContainer().getComponentInstanceOfType(ExecutionHistory.class);
      
      Tool tool = Toolbuilder.buildTool("0", desc);
      Toolbuilder.fillDirect(tool);
      String exid = ec.init(tool, "CommandLineCEAComponentManagerTest");
      Application app = eh.getApplicationFromCurrentSet(exid);
      assertTrue(app instanceof CommandLineApplication);
      
      app.setRunTimeLimit(20000);
      
      // When execute() is called, the job status should change immediately
      // from NEW to INITIALIZED. It should then change asynchronously to
      // RUNNING and then to COMPLETED. Because of the asynchronicity, we may
      // not see the intermediate states. Therefore, we can only test to
      // see that it changes from NEW.
      assertEquals(Status.NEW, app.getStatus());
      assertTrue(ec.execute(exid));
      assertFalse(app.getStatus().equals(Status.NEW));
      
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
