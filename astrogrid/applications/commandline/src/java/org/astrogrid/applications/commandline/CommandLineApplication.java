/*
 * $Id: CommandLineApplication.java,v 1.5 2004/07/26 12:03:33 nw Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.ApplicationExecutionException;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class CommandLineApplication extends AbstractApplication implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineApplication.class);

   private Thread appWaitThread;
   private StreamPiper outPiper;
   private StreamPiper errPiper;
   
   private String[] args = null;
   protected final List argvals = new ArrayList();
   protected String[] envp = null;
   protected final Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   protected final CommandLineApplicationEnvironment applicationEnvironment;

   /**
    * @param controller
    * @param user
    */
   public CommandLineApplication(String jobStepId,  Tool t,ApplicationInterface interf, CommandLineApplicationEnvironment env,ProtocolLibrary lib) {
      super(new DefaultIDs(jobStepId,env.getExecutionId()),t, interf,lib);
      this.applicationEnvironment = env;
   }



   public boolean execute() throws CeaException {
      logger.debug("executing.. " + this.toString());
      setupParameters();
      super.reportMessage("Calling preRunHook");
      preRunHook();
      super.reportMessage("PreRunHook - completed");
      startApplication();
      appWaitThread = new Thread(this);
      appWaitThread.start();
      return true;
   }

    /** override  so that commandline parameters are returned
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
     */
    protected ParameterAdapter instantiateAdapter( ParameterValue pval, ParameterDescription desr, ExternalValue indirectVal) {                
        CommandLineParameterDescription clpd = (CommandLineParameterDescription)desr;
         if (!clpd.isFile()) {
             logger.debug("treating " + pval.getName() + " as inline parameter");
          return new InlineCommandLineParameterAdapter(pval, (CommandLineParameterDescription)desr, indirectVal);
         } else {
             logger.debug("treating " + pval.getName() +" as reference parameter");
             return new ReferenceCommandLineParameterAdapter(getApplicationInterface(),pval, (CommandLineParameterDescription)desr,indirectVal,applicationEnvironment);
         }
      }
   protected void setupParameters() throws CeaException {
      // just setup the actual command line for now
      reportMessage("Setting up parameters");
      argvals.clear();
      argvals.add(((CommandLineApplicationDescription)getApplicationDescription()).getExecutionPath());

    createAdapters();
      // allow last minute manipulation of parameters before the application runs
      reportMessage("Calling postParamSetupHook");
     postParamSetupHook();
     reportMessage("postParamSetupHook - completed");     
     for (Iterator i = inputParameterAdapters(); i.hasNext(); ) {
         ParameterAdapter adapter = (ParameterAdapter)i.next();
         List vals = (List)adapter.process();
         for (Iterator j = vals.iterator(); j.hasNext(); ) {
            argvals.add(j.next().toString());
         }                  
     }
     
     for (Iterator i = outputParameterAdapters(); i.hasNext(); ) {
         ParameterAdapter adapter = (ParameterAdapter)i.next();
         List vals = (List)adapter.process();
         for (Iterator j = vals.iterator(); j.hasNext(); ) {
            argvals.add(j.next().toString());
         }                      
     }     
     reportMessage("Parameters for application: " + argvals.toString());
      // TODO check for position parameters - really need to sort the parameter list based on the parameter position information.
      // TODO need to get good way to process repeated parameters also
   }

   private void startApplication() throws ApplicationExecutionException {
        FileOutputStream stdout, stderr;
        logger.info("Starting Application " +this.toString());
        try {
           stdout = new FileOutputStream(applicationEnvironment.getOutputLog());
           stderr = new FileOutputStream(applicationEnvironment.getErrorLog());
        }
        catch (FileNotFoundException e) {
          reportError("Could not find required logs", e);
           throw new ApplicationExecutionException("Cannot open stdout or stderr files", e);
        }
        try {
           args = (String[])argvals.toArray(new String[0]);
           logger.debug("Args will be " + argvals.toString());
           setStatus(Status.RUNNING);
           process =  runtime.exec(args, envp, applicationEnvironment.getExecutionDirectory());
        }
        catch (IOException e1) {
           reportError("IOException when running app.", e1);
           throw new ApplicationExecutionException("Cannot create the application process", e1);
        }
      
        errPiper = new StreamPiper("err", process.getErrorStream(), stderr);

        //this call to process.getInputStream has to be a bug in the JDK 0
        outPiper = new StreamPiper("out", process.getInputStream(), stdout);
     }


    /**
     * run a thread that waits for the application.
     */
    public void run() {
       reportMessage("waiting for " +this.toString()+ " to finish....");
       try {
          exitStatus = process.waitFor();
          reportMessage(this.toString() + "finished");
          endApplication();
       }
       catch (InterruptedException e) {
          // TODO need to work out if it was interrupted on purpose and do something appropriate...
          reportError("application "+this.toString()+" was interrupted", e);
       } catch (CeaException e) {
           reportError("application " + this.toString() + "encountered exception while ending",e);
       }
    }

/**
    *stop reader and writer threads and free up some resources 
    */
   /**
 * @throws CeaException
 */
private final void endApplication() throws CeaException {
      reportMessage("Ending application");
      errPiper.terminate();
      outPiper.terminate();
      process = null;
      
      setStatus(Status.WRITINGBACK);
      // call the hook to allow manipulation by subclasses
      reportMessage("Calling preWritebackHook");
      preWritebackHook();
      reportMessage("preWritebackHook - completed");
      // copy back any output parameters
      ApplicationInterface applicationInterface = getApplicationInterface();
      for (Iterator i = outputParameterAdapters(); i.hasNext(); ) {   
         ParameterAdapter adapter = (ParameterAdapter)i.next();
         try {         
            adapter.writeBack(null);
         } catch (ParameterWriteBackException e) {                        
                reportError("There was a problem writing back parameter "+adapter.getWrappedParameter().getName(),e);
         }            
      }        

      reportMessage("The application has completed with exit status="+exitStatus);
      if (exitStatus != 0) {
          setStatus(Status.ERROR); // but its probably not a very big error...
      } else {
          setStatus(Status.COMPLETED);//it notifies that results are ready to be consumed.
      }
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
    * @return
    */
   public CommandLineApplicationEnvironment getApplicationEnvironment() {
      return applicationEnvironment;
   }




   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
    /* or this
   public int completionStatus() {
      return exitStatus;
   }
   */




   
}
