/*
 * $Id: CommandLineApplication.java,v 1.25 2007/02/19 16:18:47 gtr Exp $
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
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    /** time to wait for the outstreams to finish writing in milliseconds
    */
   private static final int OUTSTREAM_WAITTIME = 2000;
   /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineApplication.class);

   private Thread exeThread;
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
   public CommandLineApplication(String jobStepId,  Tool t,ApplicationInterface interf, CommandLineApplicationEnvironment env,ProtocolLibrary lib)  {
      super(new DefaultIDs(jobStepId,env.getExecutionId()),t, interf,lib);
      this.applicationEnvironment = env;
   }
   
    public Runnable createExecutionTask() throws CeaException {
      return this;
    }
    /** override  so that commandline parameters are returned
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
     */
    protected ParameterAdapter instantiateAdapter( ParameterValue pval, ParameterDescription desr, ExternalValue indirectVal) {                
        CommandLineParameterDescription clpd = (CommandLineParameterDescription)desr;
             logger.debug("creating parameter adapter for " + pval.getName());
             return new DefaultCommandLineParameterAdapter(getApplicationInterface(),pval, (CommandLineParameterDescription)desr,indirectVal,applicationEnvironment);
         
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
     logger.debug("postParamSetupHook - completed");
     
     //create a list of all the parameter adapters and sort them.
     List allAdapterList = IteratorUtils.toList(parameterAdapters());
     Collections.sort(allAdapterList, new Comparator(){
       public int compare(Object o1, Object o2) {
           //FIXME - need to check that the ordering of "equal" objects stays the same with thiss algorithm
            DefaultCommandLineParameterAdapter p1 = (DefaultCommandLineParameterAdapter)o1;
            DefaultCommandLineParameterAdapter p2 = (DefaultCommandLineParameterAdapter)o2;
            int pos1 = p1.cmdParamDesc.getCommandPosition();
            int pos2 = p2.cmdParamDesc.getCommandPosition();
            if(pos1 == -1) // indicates that it is not a position dependent
                          // parameter
            {
                if(pos2 == -1)
                {
                    return 0; // they are equivalent
                }
                else
                {
                    return 1; // p2 should be before p1
                    }
            }
            else // it is a position dependent parameter
            {
                if (pos2 == -1) {
                    return -1;  // the positional parameters should come first
                }
                else {
                    if (pos1 == pos2)
                    {
                        return 0; 
                    }
                    else
                    {
                        return pos1 > pos2 ? 1 : -1;
                    }
                    }
                }
                    
            };
        }
     );
     
     // iterate over all the parameter adapters now that they have been sorted...
     for (Iterator i = allAdapterList.iterator(); i.hasNext(); ) {
         ParameterAdapter adapter = (ParameterAdapter)i.next();
             adapter.process();
      }
     
     //iterate over the paramters adapters adding the commandline switches
     for (Iterator i = allAdapterList.iterator(); i.hasNext();) {
        CommandLineParameterAdapter adapter = (CommandLineParameterAdapter)i.next();
        List vals = adapter.addSwitches();
        if (vals != null) {
            for (Iterator j = vals.iterator(); j.hasNext();) {
                argvals.add(j.next().toString());
            } 
        }                  
    }
     
     
     reportMessage("Parameters for application: " + argvals.toString());
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
           reportError("Failed to run the command " + args[0] + " in a subprocess", e1);
           File executable = new File(args[0]);
           if (!executable.exists()) {
             logger.error(executable + ", the configured executable, does not exist.");
           }
           throw new ApplicationExecutionException("Cannot create the application process", e1);
        }
      
        errPiper = new StreamPiper("err", process.getErrorStream(), stderr);

        //this call to process.getInputStream has to be a bug in the JDK 0
        outPiper = new StreamPiper("out", process.getInputStream(), stdout);
     }


    /**
     * run a thread runs and waits for the application.
     */
    public void run() {
        try {
            try {
                setupParameters();
            }
            catch (CeaException e) {
                reportError("problem setting up parameters", e);
                return;
            }
            reportMessage("Calling preRunHook");
            preRunHook();
            reportMessage("PreRunHook - completed");
            try {
                startApplication();

            }
            catch (ApplicationExecutionException e1) {
                reportError("problem running application", e1);
                return;
            }

            reportMessage("waiting for " + this.toString() + " to finish....");
            try {
                exitStatus = process.waitFor();
                reportMessage(this.toString() + " finished");
                endApplication();
            }
            catch (InterruptedException e) {
                // TODO need to work out if it was interrupted on purpose and do
                // something appropriate...
                reportError("application " + this.toString()
                        + " was interrupted", e);
            }
        }
        catch (Throwable e) {
            reportError("unexpected runtime error in application"
                    + this.toString() +" "+ ids.getId()+" "+ids.getJobStepId() , e);
        }
    }

/**
  *stop reader and writer threads and free up some resources 
  
 * @throws CeaException
 */
private final void endApplication()  {
      reportMessage("Execution post application processes");
      //wait for the error stream a little....
      errPiper.join(OUTSTREAM_WAITTIME);
      errPiper.terminate();
      outPiper.terminate();
      process = null;
      
      setStatus(Status.WRITINGBACK);
      // call the hook to allow manipulation by subclasses
      reportMessage("Calling preWritebackHook");
      preWritebackHook();
      logger.debug("preWritebackHook - completed");
      // copy back any output parameters
      ApplicationInterface applicationInterface = getApplicationInterface();
      for (Iterator i = outputParameterAdapters(); i.hasNext(); ) {   
         ParameterAdapter adapter = (ParameterAdapter)i.next();
         try {         
            adapter.writeBack(null);
         } catch (CeaException e) {                        
                reportWarning("There was a problem writing back parameter "+adapter.getWrappedParameter().getName(),e);
                //set non-zero exit status if not already set to force the reporting of standard error below....
                exitStatus = exitStatus == 0? -1 : exitStatus;
         }
      }        

      reportMessage("The application has completed with exit status="+exitStatus);
      //report how to get hold of the log
      StringBuffer sb = new StringBuffer("standard out at ");
      //FIXME - very ugly to use the Simpleconfig here - endpoint url needs to be better encapsulated
      sb.append(SimpleConfig.getSingleton().getProperty("cea.webapp.url"));
      sb.append("/cec-http?method=getexecutionlog&type=out&id=");
      try {
         sb.append(URLEncoder.encode(applicationEnvironment.getExecutionId(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
         logger.error("internal error - encoding for log access url bad - need to fix code", e);
      }
      reportMessage(sb.toString());

      if (exitStatus != 0) {
         reportStandardError(true);// send the stderr output as well
      } else {
         // clean up the input files if the command was successful
          cleanInputFiles();
          setStatus(Status.COMPLETED);//it notifies that results are ready to be consumed.
      }
   }
   
   private void cleanInputFiles() {
      for (Iterator iter = inputParameterAdapters(); iter.hasNext();) {
         DefaultCommandLineParameterAdapter adapter = (DefaultCommandLineParameterAdapter) iter.next();
         
        
         if (adapter.cmdParamDesc.isFileRef()) {
            File f;
            if((f = adapter.getReferenceFile()) != null)
            {
               if(!f.delete())
               {
                  logger.warn("cannot delete working file "+f.getAbsolutePath());
               }
            }
         }
      }
 }

   /**
 * Report the standard error output to the listeners.
 */
private void reportStandardError(boolean doError) {
 
   File logfile;
   String streamName;
   if(doError)
   {
      logfile = applicationEnvironment.getErrorLog();
      streamName="error";
   }
   else
   {
      logfile = applicationEnvironment.getOutputLog();
      streamName="output";
   }

   try {
       
      BufferedReader errReader = new BufferedReader( new FileReader(logfile));
        StringBuffer errMsg = new StringBuffer();
        String line;
        while((line = errReader.readLine()) != null)
        {
           //TODO - check that there are not other strings that should be filtered...
           line = line.replace('\u001b', ' '); //get rid of escape characters...
           errMsg.append(line);
           errMsg.append('\n');
        }
        //TODO - need to think about limiting the size of the returned error messages...
        reportError("The standard "+streamName+" from the command line application follows\n"+errMsg.toString());
        errReader.close();
    }
    catch (IOException e) {
       reportError("cannot write back standard "+streamName, e);
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
