/*
 * $Id: AVODemoRunner.java,v 1.8 2004/03/29 12:33:31 pah Exp $
 * 
 * Created on 23-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.avodemo;

import java.io.StringWriter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.manager.externalservices.MySpaceFromConfig;
import org.astrogrid.applications.manager.externalservices.MySpaceLocator;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @bugzilla 
 */
public class AVODemoRunner implements Runnable {

   private JobIdentifierType jobstepid = new JobIdentifierType();
   org.astrogrid.community.User fulluser =
      new org.astrogrid.community.User(AVODemoConstants.ACCOUNT, AVODemoConstants.GROUP, AVODemoConstants.TOKEN);
   private final String MYSPACEBASE = "/" + AVODemoConstants.ACCOUNT + "/serv1/";
   private String toAddress = "pah@jb.man.ac.uk";
   private final String fromAddress = "pah@jb.man.ac.uk";
   private Credentials credentials = new Credentials();
   // TODO need to fill these in with something...

   private InternetAddress ccdests[];

   private String myspaceBaseRef;
   private String sector = "sect23";
   private String hemi = "s";
   CommonExecutionConnectorClient controller;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(AVODemoRunner.class);

   private CeaControllerConfig config;
   private MySpaceLocator myspaceLocator;

   /**
    * Small class to indicate that we really do want to create a CeaControllerConfig
    * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
    * @version $Name:  $
    * @since iteration5
    */
   private static class ThisConfig extends CeaControllerConfig {
      public static CeaControllerConfig getInstance() {
         return CeaControllerConfig.getInstance();
      }
   }

   /**
    * 
    */
   public AVODemoRunner() {
      config = ThisConfig.getInstance();

      controller = DelegateFactory.createDelegate(AVODemoConstants.appconEndPoint);
      myspaceLocator = new MySpaceFromConfig(config);
      try {
         ccdests = new InternetAddress[] { new InternetAddress("pah@jb.man.ac.uk")};
      }
      catch (AddressException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }
   public static void main(String[] args) {

      AVODemoRunner demo = new AVODemoRunner();
      demo.runit();

   }

   public void runit() {

      Thread runthread = new Thread(this);
      runthread.start();

   }

   public void createWorkflow() throws Exception {
      MySpaceClient myspacedel = null;
      myspacedel = myspaceLocator.getClient();
      myspaceBaseRef = MYSPACEBASE + sector + hemi;
      try {
         myspacedel.createContainer(
            "avodemo",
            "test.astrogrid.org",
            "dummy",
            myspaceBaseRef);
         myspaceBaseRef += "/";
      }
      catch (Exception e2) {
         // TODO Auto-generated catch block
         e2.printStackTrace();
      }

      Workflow workflow = new Workflow();
      workflow.setName("AVODEMO - " + sector + hemi);
      workflow.setDescription(
         "Workflow involving multi-band sextractor run followed by a data federation and hyperz run");
      Sequence sequence = new Sequence();
      workflow.setSequence(sequence);
      Step step = new Step();
      populateSExtractorStep(step, "b");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "v");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "i");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "z");
      sequence.addActivity(step);

      step = new Step();
      populateDftStep(step);
      sequence.addActivity(step);

      step = new Step();
      populateHyperZStep(step);
      sequence.addActivity(step);

      StringWriter outstr = new StringWriter();

      workflow.marshal(outstr);
      myspacedel.saveDataHolding(
         fulluser.getUserId(),
         fulluser.getCommunity(),
         fulluser.getToken(),
         MYSPACEBASE + "workflow/" + workflow.getName(),
         outstr.toString(),
         "workflow",
         myspacedel.OVERWRITE);

   }
   private void runworkflow() throws Exception {
      MySpaceClient myspacedel = null;
      myspacedel = myspaceLocator.getClient();
      myspaceBaseRef = MYSPACEBASE + sector + hemi;
      myspacedel.createContainer(
         "avodemo",
         "test.astrogrid.org",
         "dummy",
         myspaceBaseRef);
      myspaceBaseRef += "/";

      String exid;
      exid = runSExtractor("b");
      waitForCompletion(exid);
      exid = runSExtractor("v");
      waitForCompletion(exid);
      exid = runSExtractor("i");
      waitForCompletion(exid);
      exid = runSExtractor("z");
      waitForCompletion(exid);
      exid = runDft();
      waitForCompletion(exid);
      exid = runHyperZ();
      waitForCompletion(exid);
      String urlout = null;
      if (myspacedel != null) {

         urlout =
            myspacedel.getDataHoldingUrl(
               "avodemo",
               "test.astrogrid.org",
               "dummy",
               myspaceBaseRef + "hyperzout");
      }
      mailresult(urlout);
   }

   private String runHyperZ() throws CEADelegateException {
      Step step = new Step();
      String monitorURL = null;
      String applicationid = "HyperZ";
      String exid = null;
      populateHyperZStep(step);
      controller.execute(step.getTool(), jobstepid, monitorURL);

      return exid;
   }

   private void populateHyperZStep(Step step) {
      Input input = new Input();
      Output output = new Output();
      String applicationid = "HyperZ";
      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setType(ParameterTypes.FILEREFERENCE);
      param.setValue("/home/applications/demo/hyperz/zphot.param");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("input_catalog");

      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "merged");

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("output_catalog");

      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "hyperzout");
      step.setTool(tool);
      step.setName("hyperz");

   }

   private String runDft() throws CEADelegateException {
      String monitorURL = null;
      Step step = new Step();
      populateDftStep(step);
      String exid = controller.execute(step.getTool(), jobstepid, monitorURL);
      return exid;
   }

   private void populateDftStep(Step step) {
      Output output = new Output();
      Input input = new Input();
      String applicationid = "CrossMatcher";

      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("targets");

      param.setType(ParameterTypes.MYSPACE_VOTABLEREFERENCE);
      param.setValue(myspaceBaseRef + "sexout_z");

      param = new ParameterValue();
      param.setName("matches");
      input.addParameter(param);
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "sexout_b");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "sexout_v");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "sexout_i");

      param = new ParameterValue();
      param.setName("merged_output");
      output.addParameter(param);
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "merged");
      step.setTool(tool);
      step.setName("dft");
   }

   private String runSExtractor(String band) throws CEADelegateException {
      Step step = new Step();
      String monitorURL = null;
      String applicationid = "SExtractor";
      populateSExtractorStep(step, band);
      String exid = null;
      exid = controller.execute(step.getTool(), jobstepid, monitorURL);
      return exid;
   }

   private void populateSExtractorStep(Step step, String band) {
      Output output = new Output();
      Input input = new Input();
      String applicationid = "SExtractor";
      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("DetectionImage");

      param.setType(ParameterTypes.FILEREFERENCE);
      param.setValue(
         "/home/applications/data/GOODS/h_"
            + hemi
            + "z_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PhotoImage");

      param.setType(ParameterTypes.FILEREFERENCE);
      param.setValue(
         "/home/applications/data/GOODS/h_"
            + hemi
            + band
            + "_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setType(ParameterTypes.FILEREFERENCE);
      param.setValue(
         "/home/applications/demo/h_goods_" + hemi + band + "_r1.0z_phot_sex.txt");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PARAMETERS_NAME");

      param.setType(ParameterTypes.FILEREFERENCE);
      param.setValue("/home/applications/demo/std.param");

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("CATALOG_NAME");

      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(myspaceBaseRef + "sexout_" + band);

      step.setTool(tool);
      step.setName("sextractor - " + band);

   }

   private void waitForCompletion(String executionId) {

      if (executionId != null) {
         try {
            MessageType runStatus = controller.queryExecutionStatus(executionId);
            try {
               while (runStatus.getPhase() != ExecutionPhase.COMPLETED) {
                  Thread.sleep(20000);
                  runStatus = controller.queryExecutionStatus(executionId);

               }

            }
            catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         catch (CEADelegateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      else {
         logger.error("the executionid is null - application error");
      }

   }
   private void mailresult(String url) throws MessagingException {
      Session session = config.mailSessionInstance();
      Message message = new MimeMessage(session);

      InternetAddress dests[] = new InternetAddress[] { new InternetAddress(toAddress)};
      message.setFrom(new InternetAddress(fromAddress));
      message.setRecipients(Message.RecipientType.TO, dests);
      message.addRecipients(Message.RecipientType.BCC, ccdests);
      message.setSubject("AVO Demo Results for " + sector + " " + hemi);
      if (url != null) {

         message.setContent(url, "text/plain");
      }
      else {
         message.setContent("problem running job", "text/plain");
      }

      Transport.send(message);
   }

   /**
    * @return
    */
   public String getHemi() {
      return hemi;
   }

   /**
    * @return
    */
   public String getSector() {
      return sector;
   }

   /**
    * @return
    */
   public String getToAddress() {
      return toAddress;
   }

   /**
    * @param string
    */
   public void setHemi(String string) {
      hemi = string;
   }

   /**
    * @param string
    */
   public void setSector(String string) {
      sector = string;
   }

   /**
    * @param string
    */
   public void setToAddress(String string) {
      toAddress = string;
   }

   /* (non-Javadoc)
    * @see java.lang.Runnable#run()
    */
   public void run() {
      try {
         runworkflow();
      }
      catch (Exception e) {
         throw new RuntimeException("problem running the job", e);
      }
   }

}
