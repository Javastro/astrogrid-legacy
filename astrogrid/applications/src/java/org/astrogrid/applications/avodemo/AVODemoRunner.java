/*
 * $Id: AVODemoRunner.java,v 1.6 2004/02/11 14:00:32 pah Exp $
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

import java.io.IOException;
import java.rmi.RemoteException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.astrogrid.applications.Status;
import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.portal.workflow.design.Cardinality;
import org.astrogrid.portal.workflow.design.Sequence;
import org.astrogrid.portal.workflow.design.Step;
import org.astrogrid.portal.workflow.design.Tool;
import org.astrogrid.portal.workflow.design.Workflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @bugzilla 
 */
public class AVODemoRunner implements Runnable {

   private User user = AVODemoConstants.USERBEAN;
   org.astrogrid.community.User fulluser = new org.astrogrid.community.User(user.getAccount(),user.getGroup(), user.getToken());
   private String jobstepid = "dummy"; // we are not running jobs...
   private final String MYSPACEBASE = "/" + AVODemoConstants.ACCOUNT + "/serv1/";
   private String toAddress = "pah@jb.man.ac.uk";
   private final String fromAddress = "pah@jb.man.ac.uk";

   private InternetAddress ccdests[];

   private String myspaceBaseRef;
   private String sector = "sect23";
   private String hemi = "s";
   ApplicationController controller;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(AVODemoRunner.class);
   /**
    * 
    */
   public AVODemoRunner() {

      controller = DelegateFactory.createDelegate(AVODemoConstants.appconEndPoint);
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
   
   public void createWorkflow()
   {
      MySpaceClient myspacedel = null;
       try {
          myspacedel = ApplicationControllerConfig.getInstance().getMySpaceManager();
       }
       catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
       }
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

      Workflow workflow = Workflow.createWorkflow(fulluser.toSnippet(), "AVODEMO - "+sector+hemi, "Workflow involving multi-band sextractor run followed by a data federation and hyperz run");
      Sequence sequence = new Sequence(workflow);
      workflow.setChild(sequence);
      Step step = sequence.createStep(0);
      populateSExtractorStep(step, "b");
      step = sequence.createStep(1);
           populateSExtractorStep(step, "v");
      step = sequence.createStep(2);
           populateSExtractorStep(step, "i");
      step = sequence.createStep(3);
           populateSExtractorStep(step, "z");
      step = sequence.createStep(4);
           populateDftStep(step);
      step = sequence.createStep(5);
           populateHyperZStep(step);
           
    try {
      myspacedel.saveDataHolding(fulluser.getUserId(), fulluser.getCommunity(), fulluser.getToken(), MYSPACEBASE+"workflow/"+workflow.getName(),
        workflow.constructWorkflowXML(fulluser.toSnippet()), "workflow", myspacedel.OVERWRITE);
   }
   catch (Exception e) {
      logger.error("problem saving generated workflow...", e);
         }
     
          
           
      
      
      
 
      
   }
   private void runworkflow() {
      MySpaceClient myspacedel = null;
      try {
         myspacedel = ApplicationControllerConfig.getInstance().getMySpaceManager();
      }
      catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
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
      try {
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
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (MessagingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private String runHyperZ() {
      ParameterValues parameters = null;
      String monitorURL = null;
      String applicationid = "HyperZ";
      parameters = new ParameterValues();
      String exid = null;
      parameters.setMethodName("simple");
      //      parameters.setParameterSpec("<tool><input><parameter name='config_file'>/home/applications/demo/hyperz/zphot.param</parameter><parameter name='input_catalog'>/home/applications/demo/hyperz/bviz-mag-sample.cat</parameter></input><output><parameter name='output_catalog'>out1file</parameter></output></tool>");
      parameters.setParameterSpec(
         "<tool><input><parameter name='config_file'>/home/applications/demo/hyperz/zphot.param</parameter>"
            + "<parameter name='input_catalog'>"
            + myspaceBaseRef
            + "merged</parameter></input>"
            + "<output><parameter name='output_catalog'>"
            + myspaceBaseRef
            + "hyperzout</parameter></output></tool>");
      try {
         exid =
            controller.initializeApplication(
               applicationid,
               jobstepid,
               monitorURL,
               user,
               parameters);
         controller.executeApplication(exid);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return exid;
   }

   private void populateHyperZStep(Step step) {
      String applicationid = "HyperZ";
      org.astrogrid.portal.workflow.design.Tool tool = new Tool(applicationid);
      Cardinality cardinality = new Cardinality(1, 1);
      org.astrogrid.portal.workflow.design.Parameter param =
         tool.newInputParameter("config_file");
      param.setCardinality(cardinality);
      param.setType("agpd:FileReference");
      param.setValue("/home/applications/demo/hyperz/zphot.param");

      param = tool.newInputParameter("input_catalog");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "merged");

      param = tool.newOutputParameter("output_catalog");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "hyperzout");
      step.setTool(tool);
       step.setName("hyperz");

   }

   private String runDft() {
      ParameterValues parameters = null;
      String monitorURL = null;
      String applicationid = "CrossMatcher";
      parameters = new ParameterValues();
      String exid = null;
      parameters.setMethodName("simple");
      parameters.setParameterSpec(
         "<tool><input><parameter name='targets'>"
            + myspaceBaseRef
            + "sexout_z</parameter>"
            + "<parameter name='matches'>"
            + myspaceBaseRef
            + "sexout_b</parameter>"
            + "<parameter name='matches'>"
            + myspaceBaseRef
            + "sexout_v</parameter>"
            + "<parameter name='matches'>"
            + myspaceBaseRef
            + "sexout_i</parameter>"
            + "</input><output><parameter name='merged_output'>"
            + myspaceBaseRef
            + "merged</parameter></output></tool>");
      try {
         exid =
            controller.initializeApplication(
               applicationid,
               jobstepid,
               monitorURL,
               user,
               parameters);
         controller.executeApplication(exid);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return exid;
   }

   private void populateDftStep(Step step) {
      String applicationid = "CrossMatcher";

      org.astrogrid.portal.workflow.design.Tool tool = new Tool(applicationid);
      Cardinality cardinality = new Cardinality(1, 1);
      org.astrogrid.portal.workflow.design.Parameter param =
         tool.newInputParameter("targets");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "sexout_z");

      cardinality = new Cardinality(1, 3);

      param = tool.newInputParameter("matches");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "sexout_b");

      param = tool.newInputParameter("matches");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "sexout_v");

      param = tool.newInputParameter("matches");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "sexout_i");

      param = tool.newOutputParameter("merged_output");
      param.setCardinality(new Cardinality(1, 1));
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "merged");
      step.setTool(tool);
      step.setName("dft");
   }

   private String runSExtractor(String band) {
      ParameterValues parameters = null;
      String monitorURL = null;
      String applicationid = "SExtractor";
      parameters = new ParameterValues();
      String exid = null;
      parameters.setMethodName("Simple");

      String paramstr =
         "<tool><input><parameter name='DetectionImage'>/home/applications/data/GOODS/h_"
            + hemi
            + "z_"
            + sector
            + "_v1.0_drz_img.fits</parameter>"
            + "<parameter name='PhotoImage'>/home/applications/data/GOODS/h_"
            + hemi
            + band
            + "_"
            + sector
            + "_v1.0_drz_img.fits</parameter>"
            + "<parameter name='config_file'>/home/applications/demo/h_goods_"
            + hemi
            + band
            + "_r1.0z_phot_sex.txt</parameter>"
            + "<parameter name='PARAMETERS_NAME'>/home/applications/demo/std.param</parameter></input>"
            + "<output><parameter name='CATALOG_NAME'>"
            + myspaceBaseRef
            + "sexout_"
            + band
            + "</parameter></output></tool>";
      parameters.setParameterSpec(paramstr);
      try {
         exid =
            controller.initializeApplication(
               applicationid,
               jobstepid,
               monitorURL,
               user,
               parameters);
         controller.executeApplication(exid);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return exid;
   }

   private void populateSExtractorStep(Step step, String band) {
      String applicationid = "SExtractor";
      org.astrogrid.portal.workflow.design.Tool tool = new Tool(applicationid);
      Cardinality cardinality = new Cardinality(1, 1);
      org.astrogrid.portal.workflow.design.Parameter param =
         tool.newInputParameter("DetectionImage");
      param.setCardinality(cardinality);
      param.setType("agpd:FileReference");
      param.setValue(
         "/home/applications/data/GOODS/h_"
            + hemi
            + "z_"
            + sector
            + "_v1.0_drz_img.fits");

      param = tool.newInputParameter("PhotoImage");
      param.setCardinality(cardinality);
      param.setType("agpd:FileReference");
      param.setValue(
         "/home/applications/data/GOODS/h_"
            + hemi
            + band
            + "_"
            + sector
            + "_v1.0_drz_img.fits");

      param = tool.newInputParameter("config_file");
      param.setCardinality(cardinality);
      param.setType("agpd:FileReference");
      param.setValue(
         "/home/applications/demo/h_goods_" + hemi + band + "_r1.0z_phot_sex.txt");

      param = tool.newInputParameter("PARAMETERS_NAME");
      param.setCardinality(cardinality);
      param.setType("agpd:FileReference");
      param.setValue("/home/applications/demo/std.param");

      param = tool.newOutputParameter("CATALOG_NAME");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(myspaceBaseRef + "sexout_" + band);

      step.setTool(tool);
      step.setName("sextractor - "+band);
      

   }

   private void waitForCompletion(String executionId) {

      if (executionId != null) {
         try {
            String runStatus = controller.queryApplicationExecutionStatus(executionId);
            try {
               while (!runStatus.equals(Status.COMPLETED.toString())) {
                  Thread.sleep(20000);
                  runStatus = controller.queryApplicationExecutionStatus(executionId);

               }

            }
            catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      else {
         logger.error("the executionid is null - application error");
      }

   }
   private void mailresult(String url) throws MessagingException {
      Session session = ApplicationControllerConfig.getInstance().mailSessionInstance();
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
     runworkflow();
   }

}
