/*
 * $Id: AVODemoRunner.java,v 1.3 2004/01/26 12:51:17 pah Exp $
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.ApplicationControllerDelegate;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class AVODemoRunner implements Runnable {

   private User user = AVODemoConstants.USERBEAN;
   private String jobstepid = "dummy"; // we are not running jobs...
   private final String MYSPACEBASE = "/" + AVODemoConstants.ACCOUNT + "/serv1/";
   private String toAddress = "pah@jb.man.ac.uk";
   private final String fromAddress = "pah@jb.man.ac.uk";

   private String myspaceBaseRef;
   private String sector = "sect23";
   private String hemi = "n";
   ApplicationController controller;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(AVODemoRunner.class);
   /**
    * 
    */
   public AVODemoRunner() {

      controller = DelegateFactory.createDelegate(AVODemoConstants.appconEndPoint);

   }
   public static void main(String[] args) {

      AVODemoRunner demo = new AVODemoRunner();
      demo.runit();

   }

   public void runit() {

       Thread runthread = new Thread(this);
       runthread.start();

   }
   private void runworkflow() {
        MySpaceClient myspacedel = null;
         try {
            myspacedel =
               MySpaceDelegateFactory.createDelegate(AVODemoConstants.mySpaceEndPoint);
         }
         catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         myspaceBaseRef = MYSPACEBASE + sector + hemi ;
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
//      message.setFrom(new InternetAddress("paul-harrison@ntlworld.com"));
      message.setRecipients(Message.RecipientType.TO, dests);
      message.setSubject("AVO Demo Results for " + sector + " " + hemi);
      if(url != null)
      {
      
      message.setContent(url, "text/plain");
      }
      else
      {
         message.setContent("problem running job","text/plain");
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
