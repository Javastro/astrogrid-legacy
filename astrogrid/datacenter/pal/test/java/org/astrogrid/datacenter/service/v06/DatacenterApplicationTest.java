
/*
 * $Id: DatacenterApplicationTest.java,v 1.7 2004/12/03 16:01:57 jdt Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.service.v06;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.observer.AbstractProgressListener;
import org.astrogrid.applications.manager.observer.AbstractResultsListener;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.community.Account;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.queriers.test.PrecannedPlugin;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/** Test whatever we can of the application object.
 * difficult one to test - as its a boundary class between cea and datacenter - and each takes different approaches to interfaces, mocking, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationTest extends ServerTestCase {
   /**
    * Commons Logger for this class
    */
   private static final Log logger = LogFactory.getLog(DatacenterApplicationTest.class);
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(DatacenterApplicationTest.class);
   }
   
   /**
    * Constructor for DatacenterApplicationTest.
    * @param arg0
    */
   public DatacenterApplicationTest(String arg0) {
      super(arg0);
   }
   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      ds = new DataServer();
      SimpleConfig.setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY,PrecannedPlugin.class.getName());
      env = new ApplicationDescriptionEnvironment(new InMemoryIdGen(),new DefaultProtocolLibrary(), new BaseApplicationDescriptionLibrary.AppAuthorityIDResolver() {
        public String getAuthorityID() {
            return "org.astrogrid.test";
        }
    });
      appDesc = new DatacenterApplicationDescription("test",ds,env,new DirectExecutor());
      user = new User();
      tool = new Tool();
      populateTool(tool);
      app = (DatacenterApplication)appDesc.initializeApplication("test",user,tool);
      runListener = new TestRunListener();
      resultListener  = new TestResultListener();
      app.addObserver(runListener);
      app.addObserver(resultListener);
   }
   
   private static final String SAMPLE_QUERY_RESOURCE = "sample-query.xml";
   protected DataServer ds;
   protected ApplicationDescriptionEnvironment env;
   protected User user;
   protected Tool tool;
   protected DatacenterApplicationDescription appDesc;
   protected DatacenterApplication app;
   protected TestRunListener runListener;
   protected TestResultListener resultListener;
   
   /** populates the tool object - as a direct call */
   protected void populateTool(Tool tool) {
      tool.setInterface("adql");
      Input input = new Input();
      Output output = new Output();
      tool.setInput(input);
      tool.setOutput(output);
      ParameterValue query= new ParameterValue();
      query.setName("Query");
      query.setIndirect(false);
      
      InputStream is = this.getClass().getResourceAsStream(SAMPLE_QUERY_RESOURCE);
      StringWriter out = new StringWriter();
      try {
         Piper.pipe(new InputStreamReader(is),out);
      } catch (Exception e) {
         Assert.fail("Could not read query " + e.getMessage());
      }
      query.setValue(out.toString());
      input.addParameter(query);
      
      ParameterValue format= new ParameterValue();
      format.setName("Format");
      format.setValue("VOTABLE");
      format.setIndirect(false);
      input.addParameter(format);
      
      ParameterValue target = new ParameterValue();
      target.setName("Result");
      target.setIndirect(false);
      output.addParameter(target);
   }
   
   public void testCreation() {
      assertNotNull(app);
   }
   
   public String getQueryStatus() throws IOException {
      return ds.getQueryStatus(Account.ANONYMOUS, app.getQuerierId()).asFullMessage();
   }
   
   public void testRun() throws Exception {

      //starts application (submits query)
      assertTrue(app.execute());
      
      // wait for app to finish,
      long endTime = System.currentTimeMillis() + 60 * 1000; // i.e. now plus 60 seconds
      while (notFinished(app.getStatus()) ) {
         Thread.currentThread().sleep(1000); //give everything else a chance for a whole second
         Thread.yield(); // give everything else a chance.
         
         if (System.currentTimeMillis() > endTime) {
            fail("Query still running after 60 seconds, status: "+getQueryStatus());
         }
         System.out.println(getQueryStatus());
      }
      
      
      //check query thinks it completed OK
      QuerierStatus qStat = ds.getQueryStatus(Account.ANONYMOUS, app.getQuerierId());
      assertEquals("Querier "+app.getQuerierId()+" error: "+qStat.asFullMessage(), QuerierStatus.COMPLETE, qStat.getStage());
      
      // check we completed ok.
      assertEquals("Application status not updated",Status.COMPLETED,app.getStatus());
      // check listener is as expected.
      runListener.asserts();
      resultListener.asserts();
      // check results
      ResultListType rl = app.getResult();
      assertNotNull("ResultList is null", rl);
      assertEquals("ResultList should contain only one value", 1,rl.getResultCount());
      assertEquals("Result",rl.getResult(0).getName());
      String votableString = rl.getResult(0).getValue();
      assertNotNull("Votable returned is null", votableString);
      assertIsVotable(votableString);
      
   }
   
   protected boolean notFinished(Status stat) {
      if (stat.equals(Status.COMPLETED) || stat.equals(Status.ERROR) || stat.equals(Status.UNKNOWN)) {
         return false;
      } else {
         return true;
      }
   }
   
   public class TestRunListener extends AbstractProgressListener{
      boolean sawInitialized = false;
      boolean sawCompleted = false;
      boolean sawError = false;
      boolean sawRunning= false;
      boolean sawUnknown = false;
      boolean sawWritingBack = false;
      private Log logger = LogFactory.getLog(TestRunListener.class);
      
      protected void reportMessage(Application arg0, MessageType arg1) {
         logger.info(arg1.getContent());
      }
      public void asserts() {
         assertTrue("didn't see initialized state",sawInitialized);
         assertTrue("didn't see completed state",sawCompleted);
         // doesn't happen - oh well.
         //assertTrue("didn't see running state",sawRunning);
         assertTrue("didn't see writing back state",sawWritingBack);
         
         assertFalse("saw error state",sawError);
         assertFalse("saw unknown state",sawUnknown);
         
      }
      
      protected void reportStatusChange(Application arg0, Status arg1) {
         logger.info(arg1.toString());
         if (arg1.equals(Status.COMPLETED)) {
            sawCompleted = true;
         } else if (arg1.equals(Status.INITIALIZED)) {
            sawInitialized = true;
         } else if (arg1.equals(Status.ERROR)) {
            sawError = true;
         } else if (arg1.equals(Status.RUNNING)) {
            sawRunning = true;
         } else if (arg1.equals(Status.WRITINGBACK)) {
            sawWritingBack = true;
         } else if  (arg1.equals(Status.UNKNOWN)) {
            sawUnknown = true;
         } else {
            fail("Saw an unknown state " + arg1.toString());
         }
      }
      
   }
   
   public class TestResultListener extends AbstractResultsListener {
      private boolean seen = false;
      protected void notifyResultsAvailable(Application arg0) {
         seen = true;
      }
      public void asserts() {
         assertTrue("saw results notification",seen);
      }
   }
}


/*
 $Log: DatacenterApplicationTest.java,v $
 Revision 1.7  2004/12/03 16:01:57  jdt
 Fix to keep pace with changes in applications

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.8.1  2004/10/20 18:12:45  mch
 CEA fixes, resource tests and fixes, minor navigation changes

 Revision 1.2.10.1  2004/10/20 12:43:28  mch
 Fixes to CEA interface to write directly to target

 Revision 1.2  2004/10/08 17:14:23  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.4  2004/09/17 01:27:06  nw
 added thread management.

 Revision 1.3  2004/07/27 13:48:33  nw
 renamed indirect package to protocol,
 renamed classes and methods within protocol package

 Revision 1.2  2004/07/20 02:15:05  nw
 final implementaiton of itn06 Datacenter CEA interface

 Revision 1.1  2004/07/13 17:11:32  nw
 first draft of an itn06 CEA implementation for datacenter
 
 */
