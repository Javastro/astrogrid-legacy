/*$Id: CommandLineCEAComponentManagerTest.java,v 1.10 2008/02/12 12:10:56 pah Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.workflow.beans.v1.Tool;
import org.picocontainer.PicoContainer;

/** Test the component manager assembled for the commandline cea is valid - i.e. has all the necessary components registered.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author pharriso@eso.org 03-Jun-2005 - make it do a "semi-live" test
 *
 */
public class CommandLineCEAComponentManagerTest 
    extends AbstractComponentManagerTestCase {
    /**
     * Constructor for CommandLineCEAComponentManagerTest.
     * @param arg0
     */
    public CommandLineCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    
    protected CEAComponentManager createManager() {
        return new CommandLineCEAComponentManager();
    }
    
    protected void configureManager() throws Exception {
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
      
      // Impose this configuration on the manager.
      CommandLineCEAComponentManager manager 
          = (CommandLineCEAComponentManager)(this.manager);
      manager.setCommandLineConfigurationInstance(configuration);
    }
   
   public void testIsValid() {
     super.testIsValid();
   }
   
   public void testExecutionHistory() throws Exception {
     PicoContainer pico = manager.getContainer();
     assertNotNull(pico);
     ExecutionHistory history 
         = (ExecutionHistory) pico.getComponentInstanceOfType(ExecutionHistory.class);
     assertNotNull(history);
   }
   
   public void testExecutionController() throws Exception {
     ExecutionController ec = this.manager.getExecutionController();
     assertNotNull(ec);
     assertTrue(ec instanceof QueuedJobList);
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


/* 
$Log: CommandLineCEAComponentManagerTest.java,v $
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