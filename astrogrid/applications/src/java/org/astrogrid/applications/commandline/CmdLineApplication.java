/*
 * $Id: CmdLineApplication.java,v 1.17 2004/03/04 01:36:59 nw Exp $
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.Result;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.common.io.StreamPiper;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobMonitor;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
/**
 * A generic model for a command line application. This generally assumes that the application can be run from a command line obtaining all of its parameters from commandline arguments and possibly standard in. 
 * The application can interact with the filesystem.
 * 
 * This is achieved with the {@link java.lang.Runtime#exec(java.lang.String[], java.lang.String[], java.io.File) }call,
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

   private String[] args = null;
   protected List argvals = new ArrayList();
   protected String[] envp = null;
   protected Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   /**
    * This constructor is only here to allow a public constructor for derived classes, so that they can use Class.newInstance()
    */
   protected CmdLineApplication() {
      super();
   }

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
      preRunHook();
      startApplication();
      waitForApplication();
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
      // allow last minute manipulation of parameters before the application runs
     postParamSetupHook();
      // TODO check for position parameters - really need to sort the parameter list based on the parameter position information.
      // TODO need to get good way to process repeated parameters also
      // for now just go through the arguments and add
      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter param = (Parameter)iter.next();
         argvals.addAll(param.getArgValue());
      }
   }
   /**
    *stop reader and writer threads and free up some resources 
    */
   private final void endApplication() {
      errPiper.terminate();
      outPiper.terminate();
      process = null;
      // call the hook to allow manipulation by subclasses
      preWritebackHook();
      // copy back any output parameters
      for (Iterator iter = parameters.iterator(); iter.hasNext();) {
         Parameter outparam = (Parameter)iter.next();
         if (applicationInterface.parameterType(outparam.getName())
            == ApplicationInterface.ParameterDirection.OUTPUT) {
               
            outparam.writeBack();
         }

      }
      
      // The application has now finished - including any manipulations of results.
      status = Status.COMPLETED;

      //inform the job monitor that we have finished - only if there is an endpoint specified...     
      if (jobMonitorURL != null && jobMonitorURL.length() > 0) {
        //NWW: updated to new job montior delegate.
         JobMonitor delegate = JesDelegateFactory.createJobMonitor(jobMonitorURL);

         try {
             MessageType ack = new MessageType();
             ack.setValue("comment");
             ack.setLevel(LogLevel.info);
             ack.setPhase(ExecutionPhase.COMPLETED);
             ack.setSource("CmdLineApplication");
             ack.setTimestamp(Calendar.getInstance());
             
             JobIdentifierType id = new JobIdentifierType(jobStepID);           
             delegate.monitorJob(id,ack);
            //delegate.monitorJob(jobStepID, delegate.STATUS_COMPLETED, "comment");
            // NWW end update
         }
         catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (JesDelegateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      //TODO should really log this information to the applicationController database.

   }
   
   /**
     * Hook to allow for manipulation of the environment before the application gets run. Occurs when the command line has been built for the application. This does nothing in the default implementation.
     *
     */
    protected void preRunHook() {
       // do nothing special by default
    }


   /**
    *  Hook to allow special parameter processing. This occurs after the parameters have had their default treatment. This does nothing in the default implementation.
    */
   protected void postParamSetupHook() {
      // do nothing
   }



   /**
    * Hook to allow the manipulation of results before they are written back to the caller. This does nothing in the default implementation.
    */
   protected void preWritebackHook() {
      // default is to do nothing special
   }

   /**
    * creates a new thread to wait for the application to finish....
    */
   private void waitForApplication() {
      appWaitThread = new Thread(this);
      appWaitThread.start();

   }

   /**
    * Actual implementation of the application wait
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
   private void startApplication() throws ApplicationExecutionException {
      FileOutputStream stdout, stderr;

      try {
         stdout = new FileOutputStream(applicationEnvironment.getOutputLog());
         stderr = new FileOutputStream(applicationEnvironment.getErrorLog());
      }
      catch (FileNotFoundException e) {
         throw new ApplicationExecutionException("Cannot open stdout or stderr files", e);
      }
      try {
         args = (String[])argvals.toArray(new String[0]);

         if (logger.isDebugEnabled())
            logger.debug(args);
         status = Status.RUNNING;

         process =
            runtime.exec(args, envp, applicationEnvironment.getExecutionDirectory());
      }
      catch (IOException e1) {
         status =Status.ERROR;

         throw new ApplicationExecutionException(
            "Cannot create the application process",
            e1);
 
      }
      
      errPiper = new StreamPiper("err", process.getErrorStream(), stderr);

      //this call to process.getInputStream has to be a bug in the JDK 0
      outPiper = new StreamPiper("out", process.getInputStream(), stdout);

   }

   protected CmdLineApplicationEnvironment applicationEnvironment;
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
      return exitStatus;
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
