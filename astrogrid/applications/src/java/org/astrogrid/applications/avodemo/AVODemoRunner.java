/*
 * $Id: AVODemoRunner.java,v 1.1 2004/01/23 19:20:22 pah Exp $
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

import java.rmi.RemoteException;

import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.ApplicationControllerDelegate;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class AVODemoRunner {

   private User user = AVODemoConstants.USERBEAN;
   private String jobstepid = "dummy"; // we are not running jobs...
   private final String myspaceBaseRef="/"+AVODemoConstants.ACCOUNT+"/serv1/";
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
   
   public void runit()
   {
      String exid;
      exid = runSExtractor("b");
      waitForCompletion(exid);
      exid = runSExtractor("v");
      waitForCompletion(exid);
      exid = runSExtractor("i");
      waitForCompletion(exid);
      exid = runSExtractor("z");
      waitForCompletion(exid);
     
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
         "<tool><input><parameter name='config_file'>/home/applications/demo/hyperz/zphot.param</parameter><parameter name='input_catalog'>/home/applications/demo/hyperz/join.xml</parameter></input><output><parameter name='output_catalog'>hyperzout</parameter></output></tool>");
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
      
      String paramstr=
      "<tool><input><parameter name='DetectionImage'>/home/applications/data/GOODS/h_nz_sect23_v1.0_drz_img.fits</parameter>"
      +"<parameter name='PhotoImage'>/home/applications/data/GOODS/h_n"+band+"_sect23_v1.0_drz_img.fits</parameter>"
      +"<parameter name='config_file'>/home/applications/demo/h_goods_n"+band+"_r1.0z_phot_sex.txt</parameter><parameter name='PARAMETERS_NAME'>/home/applications/demo/std.param</parameter></input>" 
      +"<output><parameter name='CATALOG_NAME'>"+myspaceBaseRef+"sexout_"+band+"</parameter></output></tool>";
      parameters.setParameterSpec(paramstr);
      try {
         exid = controller.initializeApplication(applicationid, jobstepid, monitorURL, user, parameters);
         controller.executeApplication(exid);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
     return exid;
   }

   
   private void waitForCompletion(String executionId)
   {
     
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
      else
      {
         logger.error("the executionid is null - application error");
      }

   }

}
