/*
 * $Id: CmdLineApplication.java,v 1.6 2004/01/04 14:51:22 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.Result;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.common.io.StreamPiper;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorDelegate;
/**
 * A generic model for a command line application. This generally assumes that the application can be run from a command line obtaining all of its parameters from commandline arguments and possibly standard in. 
 * The application can interact with the filesystem.
 * 
 * This is achieved with the {@link java.lang.Runtime#exec(java.lang.String[], java.lang.String[], java.io.File) call,
 * 
 * It was programmed/tested under unix, but I think that the code would work with windows based systems with very little alteration.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CmdLineApplication extends AbstractApplication implements Runnable {
   private Thread appWaitThread;
   private StreamPiper outPiper;
   private StreamPiper errPiper;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CmdLineApplication.class);
 

   protected String[] args = null;
   protected List argvals = new ArrayList();
   protected String[] envp = null;
   protected Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   /**
    * @param controller
    * @param user
    */
   public CmdLineApplication(AbstractApplicationController controller, User user) {
      super(controller, user);
      // TODO Auto-generated constructor stub
   }

   public boolean execute() throws ApplicationExecutionException {

      setupParameters();
      startApplication();
      waitForApplication();
      status = Status.RUNNING;
      return true;
      //TODO set this to something sensible whether the application fails or not.
   }

   /**
    * 
    */
   protected void setupParameters() {
      // just setup the actual command line for now

      argvals.clear();
      argvals.add(applicationDescription.getExecutionPath());

      // need to set up each of the parameters

      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter param = (Parameter)iter.next();
         if (!param.process()) {
            logger.error("problem processing parameter " + param.getName());
         }

      }

      // TODO check for position parameters - really need to sort the parameter list based on the parameter position information.
      // for now just go through the arguments and add
      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter param = (Parameter)iter.next();
         argvals.addAll(param.getArgValue());
      }
      args = (String[])argvals.toArray(new String[0]);
   }

   /**
    *stop reader and writer threads and free up some resources 
    */
   protected void endApplication() {
      status = Status.COMPLETED;
      errPiper.terminate();
      outPiper.terminate();
      process = null;
 
 //inform the job monitor that we have finished     
      JobMonitorDelegate 
      delegate = JobMonitorDelegate.buildDelegate( jobMonitorURL );
      
      // FIXME need to call with a proper joburn and to make the jobstep an integer - requires jobcontroller interface change....
//      try {
//// FIXME         delegate.monitorJob("joburn123",Integer.parseInt(jobStepID),delegate.STATUS_COMPLETED,"comment" );
//      }
//      catch (NumberFormatException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
//      catch (JesDelegateException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }


   }

   /**
    * creates a new thread to wait for the application to finish....
    */
   protected void waitForApplication() {
      appWaitThread = new Thread(this);
      appWaitThread.start();
    
  

   }

   /**
    * 
    */
   private void waitForApplicationImp() {
      logger.info("waiting for " + applicationDescription.getName() + " to finish....");
      try {
         exitStatus = process.waitFor();
         logger.info(applicationDescription.getName() + "finished");
    
         endApplication();

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
         process =
            runtime.exec(args, envp, applicationEnvironment.getExecutionDirectory());
      }
      catch (IOException e1) {
         throw new ApplicationExecutionException(
            "Cannot create the application process",
            e1);

      }

      errPiper = new StreamPiper("err", process.getErrorStream(), stderr);

      //this call to process.getInputStream has to be a bug in the JDK 0
      outPiper = new StreamPiper("out", process.getInputStream(), stdout);

   }

   private CmdLineApplicationEnvironment applicationEnvironment;
   /**
    * @return
    */
   public CmdLineApplicationEnvironment getApplicationEnvironment() {
      return applicationEnvironment;
   }

   /**
    * @param environment
    */
   public void setApplicationEnvironment(CmdLineApplicationEnvironment environment) {
      applicationEnvironment = environment;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.AbstractApplication#createLocalTempFile()
    */
   public File createLocalTempFile() {
      return applicationEnvironment.getTempFile();
   }
   
   

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public int completionStatus() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CmdLineApplication.completionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#retrieveResult()
    */
   public Result[] retrieveResult() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CmdLineApplication.retrieveResult() not implemented");
   }

   /**
    * run a thread that waits for the application.
    */
   public void run() {
      waitForApplicationImp();
   }

}
