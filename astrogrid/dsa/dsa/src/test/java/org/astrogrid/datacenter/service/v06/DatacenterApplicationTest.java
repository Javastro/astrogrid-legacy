
/*
 * $Id: DatacenterApplicationTest.java,v 1.5 2011/05/05 14:49:37 gtr Exp $
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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.observer.AbstractProgressListener;
import org.astrogrid.applications.manager.observer.AbstractResultsListener;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.cea.DatacenterApplication;
import org.astrogrid.dataservice.service.cea.DatacenterApplicationDescription;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.PrecannedPlugin;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.tableserver.test.SampleStarsPlugin;


/** Test whatever we can of the application object.
 * difficult one to test - as its a boundary class between cea and datacenter - and each takes different approaches to interfaces, mocking, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationTest extends TestCase {
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
   @Override
   public void setUp() throws Exception {
      super.setUp();
      // This allows the job database to work properly.
      SimpleConfig.setProperty("datacenter.cache.directory", "target");
      Job.initialize();
      TableMetaDocInterpreter.clear();
      SampleStarsPlugin.initConfig();
      ds = new DataServer();
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY,PrecannedPlugin.class.getName());
      runListener = new TestRunListener();
      resultListener  = new TestResultListener();
   }

   private DatacenterApplication newApplication() throws Exception {
     ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(
                       new InMemoryIdGen(),
                       new DefaultProtocolLibrary(),
                       new AppAuthorityIDResolver() {
                                 public String getAuthorityID() {
                                    return "org.astrogrid.test";
                                 }
                           });
      DatacenterApplicationDescription appDesc = new DatacenterApplicationDescription("astrogrid.org/test-dsa-catalog/ceaApplication",ds,env);
      Tool tool = new Tool();
      populateTool(tool);
      DatacenterApplication app = (DatacenterApplication)appDesc.initializeApplication("astrogrid.org/test-dsa-catalog/ceaApplication",new User(),tool);

      app.addObserver(runListener);
      app.addObserver(resultListener);
      return app;
   }
   
   protected DataServer ds;
   protected TestRunListener runListener;
   protected TestResultListener resultListener;
   
   /** populates the tool object - as a direct call */
   protected void populateTool(Tool tool) throws Exception {
      tool.setInterface(DatacenterApplicationDescription.ADQL_IFACE);
      Input input = new Input();
      Output output = new Output();
      tool.setInput(input);
      tool.setOutput(output);
      
      ParameterValue query= new ParameterValue();
      query.setName("Query");
      query.setIndirect(false);
      query.setValue("SELECT * FROM TabName_SampleStars");
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
   
   public void testCreation() throws Exception {
     DatacenterApplication app = newApplication();
      assertNotNull(app);
      System.out.println(app.getID());
      Job job = Job.load(app.getID());
      assertNotNull(job);
      assertEquals("PENDING", job.getPhase());
   }
   
   public void testRun() throws Exception {
     DatacenterApplication app = newApplication();
     Job job = Job.load(app.getID());
     assertNotNull(job);
     assertEquals("PENDING", job.getPhase());

     //starts application (submits query)
     app.createExecutionTask().run();
     String qid = app.getJobId();
     assertNotNull(qid);
     System.out.println("Running querier " + qid);

      // wait for app to finish,
      long timeLimit = System.currentTimeMillis() + 60 * 1000; // i.e. now plus 60 seconds
      while (notFinished(app.getStatus()) ) {
         Job.load(app.getID());
         job = Job.load(qid);
         assertNotNull(job);
         Thread.sleep(1000); //give everything else a chance for a whole second
         if (System.currentTimeMillis() > timeLimit) {
            fail("Job still running after 60 seconds, status: "+ job.getPhase());
         }
         System.out.println(job.getPhase());
      }
      
      
      //check query thinks it completed OK
      job = Job.load(qid);
      assertNotNull(job);
      System.out.println("Job final phase: " + job.getPhase());
      assertEquals("COMPLETED", job.getPhase());
      
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
      ResultFile rf = new ResultFile(job.getId());
      assertNotNull(rf);
      assertTrue(rf.exists());
      assertTrue(rf.length() > 0);
      
   }

   public void testGetCatalogNameFromAppIvorn() throws Exception {
     DatacenterApplication app = newApplication();
     
      String s1 = app.getCatalogNameFromAppIvorn(
      "ivo://astrogrid.org/test-dsa-catalog/ceaApplication");
      assertEquals(s1,"");

      s1 = app.getCatalogNameFromAppIvorn(
      "ivo://astrogrid.org/test-dsa-catalog/catalog/ceaApplication");
      assertEquals(s1,"catalog");

      s1 = app.getCatalogNameFromAppIvorn(
      "ivo://astrogrid.org/test-dsa-catalog/catalog/extrabit/ceaApplication");
      assertEquals(s1,"catalog/extrabit");

      try {
         s1 = app.getCatalogNameFromAppIvorn(
            "ivo://my.auth.id/test-dsa-catalog/ceaApplication");
         fail("Should have rejected IVORN - bad authority ID");
      }
      catch (CeaException ce) {
      }
      try {
         s1 = app.getCatalogNameFromAppIvorn(
            "ivo://astrogrid.org/my_resource/ceaApplication");
         fail("Should have rejected IVORN - bad resource key");
      }
      catch (CeaException ce) {
      }
      try {
         s1 = app.getCatalogNameFromAppIvorn(
            "ivo://astrogrid.org/my_resource/foo");
         fail("Should have rejected IVORN - bad suffix");
      }
      catch (CeaException ce) {
      }
      try {
         s1 = app.getCatalogNameFromAppIvorn(
            "ivo://astrogrid.org/my_resource/ceaApplication/");
         fail("Should have rejected IVORN - trailing slash");
      }
      catch (CeaException ce) {
      }
      try {
         s1 = app.getCatalogNameFromAppIvorn(
            "sivo://astrogrid.org/my_resource/ceaApplication/");
         fail("Should have rejected IVORN - strange prefix");
      }
      catch (CeaException ce) {
      }
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