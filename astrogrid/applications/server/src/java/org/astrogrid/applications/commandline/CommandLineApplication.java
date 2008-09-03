package org.astrogrid.applications.commandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.ApplicationExecutionException;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/**
 * A generic model for a command line application. This generally assumes that 
 * the application can be run from a command line obtaining all of its 
 * parameters from commandline arguments and possibly standard in. 
 * The application can interact with the filesystem.
 * 
 * This is achieved with the {@link java.lang.Runtime#exec(java.lang.String[], 
 * java.lang.String[], java.io.File) } call so the job runs in a sub-process
 * of the JVM.
 *
 * It was programmed/tested under unix, but I think that the code would work 
 * with windows based systems with very little alteration.
 *
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO push as much functionality up as possible to share with java class style apps
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
   protected final List<String> argvals = new ArrayList<String>();
   protected String[] envp = null;
   protected final Runtime runtime = Runtime.getRuntime();
   protected Process process;
   protected int exitStatus;

   /**
    * Indicates whether the sub-process returned naturally or was forced
    * to return. Always
    *  false until the process actually returns.
    */
   protected boolean isAborted = false;

   /**
    * @param controller
    * @param user
    */
   public CommandLineApplication(String jobStepId,  Tool t, ApplicationInterface interf, ApplicationEnvironment env,ProtocolLibrary lib)  {
      super(t, interf,env, lib);
   }
   
 
    /** override  so that commandline parameters are returned
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.indirect.IndirectParameterValue)
     */
    protected ParameterAdapter instantiateAdapter( ParameterValue pval, ParameterDescription desr, ExternalValue indirectVal) {                
        CommandLineParameterDefinition clpd = (CommandLineParameterDefinition)desr;
             logger.debug("creating parameter adapter for " + pval.getId());
             return new DefaultCommandLineParameterAdapter(getApplicationInterface(),pval, (CommandLineParameterDefinition)desr,indirectVal,applicationEnvironment);
         
      }
    
  /**
   * Runs the application to completion. The Execution controllor should
   * call this method in a dedicated thread.
   * @TODO need to code so that it is possible to interrupt during different stages - currently interruption is only really possible when the commandline app has been spawned, not in the parameter copying phases...
   */
  public void run() {
    try {
      
      // Capture the values of the input parameters.
      // Set up the routing of the results.
      try {
        setupParameters();
      }
      catch (Exception e) {
        reportError("problem setting up parameters", e);
        return;
      }
      
      // Run a hook by which sub-classes can inject local processing.
      reportMessage("Calling preRunHook");
      preRunHook();
      reportMessage("PreRunHook - completed");
      
      // Start the job as a sub-process.
      try {
        startApplication();
      }
      catch (ApplicationExecutionException e1) {
        reportError("problem running application", e1);
        return;
      }

      // Wait for the sub-process to exit, enforcing a time limit.
      reportMessage("waiting for " + this.toString() + " to finish....");
      try {
          this.exitStatus = process.waitFor();
          reportMessage(this.toString() + " finished");
          endApplication();
      }
      catch (InterruptedException e) {
          //the job was interrupted
          // 
	  errPiper.terminate();
	  outPiper.terminate();
	  process.destroy(); // kill off the subprocess
          reportError("application " + this.toString()
                  + " was interrupted", e);
      }
      
     
    }
    catch (Throwable e) {
      reportError("unexpected runtime error in application"
                   + this.toString() +" "+ applicationEnvironment.getExecutionId()+" "+ applicationEnvironment.getJobStepId() , e);
    }
  }
  
  
   protected void setupParameters() throws CeaException {
      // just setup the actual command line for now
      reportMessage("Setting up parameters");
      argvals.clear();
      argvals.addAll(((CommandLineApplicationDescription)getApplicationDescription()).getExecutionCmdline());

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
        recordStartOfExecution();
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
           this.process =  runtime.exec(args, envp, applicationEnvironment.getExecutionDirectory());
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
   * Reports the results of execution.
   * If the application ran to completion, then the output parameters are
   * written back to their pre-specified destinations. 
   */
  private final void endApplication()  {
    logger.debug("endApplication()");
    reportMessage("Execution post application processes");
    
    // Tidy up the application's output and error streams.
    errPiper.join(OUTSTREAM_WAITTIME);
    errPiper.terminate();
    outPiper.terminate();
    this.process = null;
    
    // Send the output parameters to their pre-specified locations.
    String writeBackErrors = null;
    if (this.exitStatus == 0 && !this.isAborted) {
      writeBackErrors = writeBackOutputParameters();        
    }
    
    // Application succeeded: throw away the files holding values of input
    // parameters and tell the client that the output parameters are ready
    // to collect.
    if (this.exitStatus == 0 && writeBackErrors == null && !this.isAborted) {
      cleanInputFiles();
      setStatus(Status.COMPLETED);
    }
    
    // Application failed: make the standard-error log available and notify the
    // client not to bother with the output parameters.
    else {
      reportFailure(writeBackErrors);
      reportStandardError(true);
      setStatus(Status.ERROR);
    }
   }

  /**
   * Writes the values of the output parameters to their designated location.
   * This should only be called if the application has run to completion.
   */
  private String writeBackOutputParameters() {
    StringBuffer errors = new StringBuffer();
    int errorCount = 0;
    
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
      } 
      catch (CeaException e) {
        errorCount++;
        errors.append("There was a problem writing back parameter ");
        errors.append(adapter.getWrappedParameter().getId());
        errors.append(": ");
        errors.append(e);
        errors.append('\n');
        reportWarning("There was a problem writing back parameter "+adapter.getWrappedParameter().getId(),e);
      }
    }
    
    return (errorCount == 0)? null : errors.toString();
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
    if(doError) {
      logfile = applicationEnvironment.getErrorLog();
      streamName="error";
    }
    else {
      logfile = applicationEnvironment.getOutputLog();
      streamName="output";
    }

    try {
      StringBuffer errMsg = new StringBuffer();
      this.copyFileToStringBuffer(logfile, errMsg);
      //TODO - need to think about limiting the size of the returned error messages...
      reportError("The standard "+streamName+" from the command line application follows\n"+errMsg.toString());
    }
    catch (IOException e) {
      reportError("cannot write back standard "+streamName, e);
    }
  }

  /**
   * Reports the failure of the application to the client.
   * This report adds one unregistered parameter, cea-error,
   * to the results list that client will fetch. That parameter
   * contains both standard output and standard error captured
   * from the wrapped application. The parameter is always a
   * direct parameter; since it is not registered, the client
   * has no way to redirect it anywhere.
   */
  private void reportFailure(String writeBackErrors) {
    try {
      StringBuffer message = 
          new StringBuffer("Possible causes of failure\n\n");
      
      // Report whether the process was aborted.
      message.append("Early termination by the service:\n");
      if (this.isTimedOut()) {
        message.append("Application was aborted due to a time-out.\n\n");
      }
      else if (this.isAborted) {
        message.append("Application was aborted by a command to the service.\n\n");
      }
      else {
        message.append("Application was allowed by the service to finish.\n\n");
      }
      
      // Collate the report as a single string.
      message.append("Application's standard output:\n");
      this.copyFileToStringBuffer(applicationEnvironment.getOutputLog(), message);
      message.append("\n\n");
      message.append("Application's standard error:\n");
      this.copyFileToStringBuffer(applicationEnvironment.getErrorLog(), message);
      message.append("\n\n");
      message.append("Errors in writing out parameters to external storage:\n");
      message.append((writeBackErrors == null)? "No errors." : writeBackErrors);
      
      this.logger.info(message.toString());
      
      // Make a value object to carry the report as a parameter.
      ParameterValue report = new ParameterValue();
      report.setId("cea-error");
      report.setIndirect(false);
      report.setValue(message.toString());
      
      // Add the packaged report to the list of results such that the client
      // gets it together with (or instead of) the output parameters.
      this.getResult().getResult().add(report);  
    } 
    
    // If the error reporting fails, then there's nothing to do but log it and
    // go on.
    catch (Exception ex) {
      this.logger.error("Failed to make an error report to the client: ", ex);
    }
  }
  
  /**
   * Captures the contents of a file into a StringBuffer.
   */
  private void copyFileToStringBuffer(File source, StringBuffer sink) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(source));
    String line;
    while((line = reader.readLine()) != null) {
      //TODO - check that there are not other strings that should be filtered...
      line = line.replace('\u001b', ' '); //get rid of escape characters...
      sink.append(line);
      sink.append('\n');
    }
    reader.close();
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
   public ApplicationEnvironment getApplicationEnvironment() {
      return applicationEnvironment;
   }


@Override
public Runnable createRunnable() {
    return this;
}

}
