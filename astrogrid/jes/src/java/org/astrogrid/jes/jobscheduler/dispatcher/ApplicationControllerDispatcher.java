/*$Id: ApplicationControllerDispatcher.java,v 1.10 2004/04/08 14:43:26 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobMonitor;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Implementation of a Dispactcher that calls an application controller web service to execute job steps.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class ApplicationControllerDispatcher implements Dispatcher, ComponentDescriptor {
   private static final Log logger =
      LogFactory.getLog(ApplicationControllerDispatcher.class);
   /** Configuration component for Application Controller Dispatcher
    * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
    *
    */
   public interface MonitorEndpoint {
      URL getURL();
   }
   /** Construct a new ApplicationControllerDispatcher    
    * @param locator tool locator component to use to resolve endpoints
    * @param endpoint configuration component that specifies the endpoint of the JobMonitor service. This is used by the ApplicationController to 
    * return execution information back to the JES server.
    */
   public ApplicationControllerDispatcher(Locator locator, MonitorEndpoint endpoint) {
      this.locator = locator;
      this.monitorURL = endpoint.getURL();
      assert monitorURL != null;
      logger.info("monitor URL set to " + monitorURL.toString());
   }
   /** tool locator component */
   protected final Locator locator;
   /** endpoint of local jobmonitor service - used as a callback */
   protected final URL monitorURL;
   /**
    * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(java.lang.String, org.astrogrid.jes.job.JobStep)
    */
   public void dispatchStep(Workflow job, Step js) throws JesException {
      boolean succeeded = false;

      String toolLocation = locator.locateTool(js);    
      CommonExecutionConnectorClient appController =
         DelegateFactory.createDelegate(toolLocation);

      String xpath = job.getXPathFor(js);
      JobIdentifierType id =
         JesUtil.createJobId(job.getJobExecutionRecord().getJobId(), xpath);
      logger.debug(
         "Calling application controller at "
            + toolLocation
            + " for "
            + js.getTool().getName()
            + ", "
            + id.getValue());
      try {
         String applicationID =
            appController.execute(js.getTool(), id, monitorURL.toString());

      }
      catch (CEADelegateException e) {
         logger.error(
            id.getValue() + " : Failed to communitcate with application controller",
            e);
         throw new JesException("Failed to communicate with application controller", e);
      }

   }

   /**
    * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
    */
   public String getName() {
      return "ApplicationController Dispatcher";
   }

   /**
    * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
    */
   public String getDescription() {
      return "Dispatcher that executes job steps by calling application controllers"
         + " Configured to tell application controllers to call back to:\n"
         + monitorURL.toString();
   }

   /**
    * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
    */
   public Test getInstallationTest() {
      TestSuite suite = new TestSuite("Tests for ApplicationControllerDispatcher");
      suite.addTest(new InstallationTest("testCanConnectMonitorURL"));
      suite.addTest(new InstallationTest("testCanCallMonitorURL"));
      return suite;
   }

   protected class InstallationTest extends TestCase {
      public InstallationTest(String s) {
         super(s);
      }
      public void testCanConnectMonitorURL() throws Exception {
         URLConnection conn = monitorURL.openConnection();
         assertNotNull(conn);
         conn.connect();

      }
      public void testCanCallMonitorURL() throws Exception {
         JobMonitor mon = JesDelegateFactory.createJobMonitor(monitorURL.toString());
         assertNotNull(mon);
         // call, with null parameters. will be ignored by other end - if it gets there. we're checking it gets there..
         mon.monitorJob(null, null);
      }
   }

}

/* 
$Log: ApplicationControllerDispatcher.java,v $
Revision 1.10  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.9  2004/03/24 08:05:08  pah
call the new CommonExecutionConnector Delegate

Revision 1.8  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.7  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

Revision 1.6  2004/03/12 15:32:14  nw
improved logging

Revision 1.5  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:07  nw
rearranging code
 
*/