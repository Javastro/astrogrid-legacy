/*
 * $Id: CmdLineApplication.java,v 1.2 2003/12/07 01:09:48 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.common.io.StreamPiper;
public class CmdLineApplication extends AbstractApplication {
   private StreamPiper outPiper;
   private StreamPiper errPiper;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CmdLineApplication.class);
public CmdLineApplication(){}

 
   protected String[] args = null;
   protected List argvals = new ArrayList();
   protected String[] envp = null;
   protected Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   public boolean execute() throws ApplicationExecutionException{
      
      setupParameters();
      startApplication();
      waitForApplication();
      endApplication();
      
      return true; //TODO set this to something sensible whether the application fails or not.
   }
   
   /**
    * 
    */
   protected void setupParameters() {
      // just setup the actual command line for now
     
     argvals.clear();
     argvals.add(applicationDescription.getExecutionPath());
     
     args = (String[])argvals.toArray(new String[0]);
   }

   /**
    *stop reader and writer threads and free up some resources 
    */
   protected void endApplication() {
      errPiper.terminate();
      outPiper.terminate();
      process = null;
   }

   /**
    * 
    */
   protected void waitForApplication() {
      logger.info("waiting for "+ applicationDescription.getName() + " to finish....");
      try {
         exitStatus = process.waitFor();
         logger.info(applicationDescription.getName() + "finished");
      }
      catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      

   }

   /**
    * 
    */
   protected void startApplication() throws ApplicationExecutionException {
      FileOutputStream stdout, stderr;
      
      try {
          stdout = new FileOutputStream(applicationEnvironment.getOutputLog());
          stderr = new FileOutputStream(applicationEnvironment.getErrorLog());
      }
      catch (FileNotFoundException e) {
        throw new ApplicationExecutionException("Cannot open stdout or stderr files", e);
      }
      try {
         process = runtime.exec(args, envp, applicationEnvironment.getExecutionDirectory());
      }
      catch (IOException e1) {
         throw new ApplicationExecutionException("Cannot create the application process", e1);

      }
      
      errPiper = new StreamPiper("err", process.getErrorStream(), stderr);
      
      //this call to process.getInputStream has to be a bug in the JDK 0
      outPiper = new StreamPiper("out", process.getInputStream(), stdout);
      
      

   }

   private ApplicationEnvironment applicationEnvironment;
   /**
    * @return
    */
   public ApplicationEnvironment getApplicationEnvironment() {
      return applicationEnvironment;
   }

   /**
    * @param environment
    */
   public void setApplicationEnvironment(ApplicationEnvironment environment) {
      applicationEnvironment = environment;
   }
   
   

}
