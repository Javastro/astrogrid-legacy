/*
 * $Id: CmdLineApplication.java,v 1.3 2003/12/11 13:23:02 pah Exp $
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
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.common.io.StreamPiper;
/**
 * A generic model for a command line application. This generally assumes that the application can be run from a command line obtaining all of its parameters from commandline arguments and possibly standard in. 
 * The application can interact with the filesystem.
 * 
 * This is achieved with the {@link java.lang.Runtime#exec(java.lang.String[], java.lang.String[], java.io.File) call, and note should be taken of the warnings given in the {@link java.lang.Process} documentation 
 * where it indicates that this method will not work well for shell scripts in certain circumstances. It appears that if the command
 *  line parameters are such that the invoking shell wants to output errors to standard error before this class has time to set up the readers then it will hang (under red hat linux 9 it displays this behaviour).
 * 
 * It was programmed/tested under unix, but I think that the code would work with windows based systems with very little alteration.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CmdLineApplication extends AbstractApplication {
   private StreamPiper outPiper;
   private StreamPiper errPiper;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CmdLineApplication.class);
   public CmdLineApplication() {
   }

   protected String[] args = null;
   protected List argvals = new ArrayList();
   protected String[] envp = null;
   protected Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   public boolean execute() throws ApplicationExecutionException {

      setupParameters();
      startApplication();
      waitForApplication();
      endApplication();

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
      errPiper.terminate();
      outPiper.terminate();
      process = null;
   }

   /**
    * 
    */
   protected void waitForApplication() {
      logger.info("waiting for " + applicationDescription.getName() + " to finish....");
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
