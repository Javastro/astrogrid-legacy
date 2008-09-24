/*
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.Identify;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.description.execution.LogLevel;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.security.SecurityGuard;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.FutureTask;

/**
 * Basic implementation of {@link org.astrogrid.applications.Application}
 * <p />
 * This is an abstract class that CEA providers must extend. 
 * 
 * <I>This class provides a lot of protected-visibility helper methods, private fields, final methods, etc - it tries to 
 * impose some structure on the form the extension takes. This may mean that it'd be better to split this class into a framework class, and a plugin class. But we'll see</i>
 * <h2>Abstract Methods</h2>
 * Extenders must implement the {@link #createExecutionTask()} method.
 * <h2>Overridable Methods</h2>
 * The {@link #instantiateAdapter(ParameterValue, ParameterDescription, ExternalValue)}
 * method may be overridden to return instances of a custom 
 * {@link org.astrogrid.applications.parameter.ParameterAdapter} if needed.<br/>
 * The default implementation of {@link #attemptAbort(boolean)} does nothing. Providers may extend this if suitable.<br />
 * The {@link #createTemplateMessage()} method can also be overridden to return more application-specific information, similarly {@link #toString()} if desired.<br/>
 * 
 * <h2>Helper Methods</h2>
 * This class provides helper methods for working with parameters, parameter adapters, and for progress reporting.
*  <h3>Parameters</h3>
*    There are methods to iterate through input parameters, output parameters, 
 *   and all parameters, and to find input / output / either by name
*  <h3>Parameter Adapters</h3>
*    There are methods to iterate through input parameterAdapters, 
 * output parameterAdapters, and all parameterAdapters - ant to 
 * find input / output / either kind of adapter by parameter name.
*    All these methods have {@link #createAdapters()} as a precondition - 
 * this initializes the adapter sets.
*  <h3>Progress Reporting</h3>
* There's a set of methods that hide the details of notifying observers<p>
*  Calls to {@link #setStatus(Status)} update the status field of the application, and also send a change notification to all observers registered to this application. The 
* {@link #reportMessage(String)} method will send a message to each observer, as well as adding it to the log. Similarly with {@link #reportWarning}. Finally {@link #reportError}
* will send a message <b>and</b> set the status of this application to {@link org.astrogrid.applications.Status#ERROR}. The overridable method {@link #createTemplateMessage()} 
* is used to create the template messages for this methods.
*  @author Noel Winstanley
 * @author Paul Harrison
 * @author Guy Rixon
 * @since iteration4.1
 * @see org.astrogrid.applications.javaclass.JavaClassApplication as an example extension
 * @see org.astrogrid.applications.Application
 * @see org.astrogrid.applications.description.ApplicationDescription
 * @see org.astrogrid.applications.parameter.ParameterAdapter
 */
public abstract class AbstractApplication extends Observable implements Application, Identify {
   /** interface to the set of identifiers for an application
    * @deprecated put everything in the {@link ApplicationEnvironment} */
   public static interface IDs {
        /** the cea-assigned id for this application execution */
       public String getId();
       /** the user-assigned id for this application execution
        * @return id for this execution
        */
       public String getJobStepId();
       /** identifier for the user who own this application execution */
       public User getUser();
   }
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractApplication.class);


   /** the interface being used by this application */
   private final ApplicationInterface applicationInterface;
   
    /** list of parameter adapters for the inputs to the application. Empty at start. */
   private final List inputAdapters = new ArrayList();
   /** library of indirection protocol handlers */
   protected final ProtocolLibrary lib;
   /** list of parameter adapters for the outputs of the application. empty at start */
   private final List outputAdapters = new ArrayList();
   /** list type containing results of the application execution. obviously empty to start with */
   private final ResultListType results = new ResultListType();
   /** The time when the application job should be destroyed - the application itself does not actively do this */
   private Date destruction;
   
   
   /**
    * Class to define a run of application.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public static class ApplicationTask extends FutureTask<String> {
 //IMPL perhaps this is not necessary - can just cast directly.
    private Application app;

    public ApplicationTask(Runnable runnable, String result, Application application) {
	super(runnable, result);
	this.app =application;
	
    }

    public Application getApp() {
        return app;
    }
       
   }
   protected ApplicationTask task = null;

   /**
    * the application status
    */
   private Status status = Status.NEW;
   /** tool object defines the parameters, etc to the application */
   private final Tool tool;
   
   /** construct a new application execution
    * @param ids identifiers for this application execution
    * @param tool defines the parameters of this execution, and which application / interface is to be called.
    * @param applicationInterface the description interface this application conforms to. //IMPL perhaps neater to send the applicationDescription + inteface name here - would not need so many links to applications within the CEAApplication Structure... 
    * @param lib library of indirection handlers - used to read remote parameters using various protocols
 * @param env 
     */
   public AbstractApplication(Tool tool, ApplicationInterface applicationInterface, ApplicationEnvironment env,ProtocolLibrary lib) 
   {      
      this.applicationInterface = applicationInterface;
      this.tool = tool;
      this.lib = lib;
      this.startTime = null;
      this.endTime = null;
      this.deadline = null;
      this.status = Status.NEW;
      this.applicationEnvironment = env;
      
      // This class has no mandate to set run-time limits: those are imposed
      // where needed by sub-classes. However, the machinery requires a value to
      // be present, so set a very long limit.
      this.runTimeLimitMs = 1000 * 365 * 24 * 60 * 60 * 1000;
   }
    /** default implementation of attemptAbort 
     */
    public boolean attemptAbort(boolean external) {
	//TODO remember the distinction between the external and internal abort somewhere
	logger.info("attempting to abort job="+getId());
	boolean aborted = true;
	if(task != null){// only try to kill the task if the job is running...
         aborted = this.task.cancel(true);
	}

        if(aborted) {
            setStatus(Status.ABORTED);
            recordEndOfExecution();
        }
        return aborted;
    }
    
  /**
   * The instant at which execution of the application started.
   * Should be null until the execution starts, which includes the
   * time the application waits on the queue.
   * This is used for execution time-outs and reporting.
   */
   protected Date startTime;
   
  /**
   * The instant at which the execution of the application will be aborted
   * if it has not already finished.
   */
   protected Date deadline;
   
  /**
   * The instant at which execution of the application ended.
   * Should be null until the execution starts, which includes the
   * time the application waits on the queue and the actual execution.
   * This is used for execution time-outs and reporting.
   */
   protected Date endTime;
   
  /**
   * The execution time, in milliseconds, allowed for the application.
   * If this time is exceeded then the application will be aborted.
   */
  protected long runTimeLimitMs;


protected final ApplicationEnvironment applicationEnvironment;

  /**
   * Reveals the time in milliseconds for which the application has been
   * executing. This is zero if the application has been accepted by the server
   * but execution has not yet begun.
   */
  protected long getExecutionDuration() {
    if (this.startTime == null) {
      return 0;
    }
    else {
      if (this.endTime == null) {
        return new Date().getTime() - this.startTime.getTime();
      }
      else {
        return this.endTime.getTime() - this.startTime.getTime();
      }
    }
  }
  
  /**
   * Reveals the instant at which the application will be aborted. This method
   * returns null if no deadline has been set. This will be generally be true
   * before and after the execution of the job and may be true at all times
   * if an implementation does not impose deadlines.
   */
  public Date getDeadline() {
    return this.deadline;
  }
  
  /**
   * Reveals the instant at which the job started execution.
   * This will be null if the execution has not yet started.
   */
  public Date getStartInstant() {
    return this.startTime;
  }
  
  /**
   * Reveals the instant at which the job finished execution.
   * This will be null if the execution has not yet finished.
   */
  public Date getEndInstant() {
    return this.endTime;
  }
  
  /**
   * Specifies the allowed duration of execution, in milliseconds.
   */
  public void setRunTimeLimit(long ms) {
    this.runTimeLimitMs = ms;
    if (this.startTime != null) {
      this.deadline = new Date(this.startTime.getTime() + ms);
    }
  }
  
  /**
   * Reveals the allowed duration of excution, in milliseconds.
   */
  public long getRunTimeLimit() {
    return this.runTimeLimitMs;
  }
  
  /**
   * Reveals whether the application has over-run its allowed deadline and
   * hence been aborted.
   */
  public boolean isTimedOut() {
    return (this.endTime != null && 
            this.deadline != null && 
            this.endTime.after(this.deadline));
  }
  
  /**
   * Records the instant at which execution of the application began.
   * Annuls the end-time property.
   * Updates the deadline for completion of the job.
   */
  protected void recordStartOfExecution() {
    this.startTime = new Date();
    this.endTime = null;
    this.deadline = new Date(this.startTime.getTime() + this.runTimeLimitMs);
  }
  
  /**
   * Records the instant at which execution of the application ended.
   */
  protected void recordEndOfExecution() {
    this.endTime = new Date();
  }
  
  /**
   * Reveals whether the job has outrun its allowed execution time.
   */
  protected boolean beforeDeadline() {
    if (this.deadline == null) {
      return true;
    }
    else {
      return new Date().before(this.deadline);
    }
  }

    /** hook that specialized subclasses can overried - to return a custom adapter  
     * used in {@link #createAdapters}
     * @return a {@link DefaultParameterAdapter}
     */
    protected ParameterAdapter instantiateAdapter(ParameterValue pval, ParameterDescription descr,ExternalValue indirectVal) {
        return new DefaultParameterAdapter(pval,descr,indirectVal);
    }
    
    /** sets up the list of input and output parameter adapters
     * must be called before any of the methods that access / query parameter adapters.
     * @throws ParameterDescriptionNotFoundException
     * @throws ParameterAdapterException
     * @see #createAdapters() for customization.
     */
    protected final void createAdapters() throws ParameterDescriptionNotFoundException, ParameterAdapterException {
        inputAdapters.clear();
        outputAdapters.clear();
        results.getResult().clear();
        for  (Iterator params = inputParameterValues(); params.hasNext();){
             ParameterValue param = (ParameterValue)params.next();       
            ExternalValue iVal = (param.isIndirect() ? lib.getExternalValue(param, applicationEnvironment.getSecGuard()) : null);
             ParameterAdapter adapter = this.instantiateAdapter(param,getApplicationDescription().getParameterDescription(param.getId()),iVal);
             inputAdapters.add(adapter);
          }
        for  (Iterator params = outputParameterValues(); params.hasNext();){
            ParameterValue param = (ParameterValue)params.next();       
           ExternalValue iVal = (param.isIndirect() ? lib.getExternalValue(param, applicationEnvironment.getSecGuard()) : null);
            ParameterAdapter adapter = this.instantiateAdapter(param,getApplicationDescription().getParameterDescription(param.getId()),iVal);
            outputAdapters.add(adapter);
            results.getResult().add(adapter.getWrappedParameter());
         }          
    }
   
   /**
     * Checks that all the parameters have been specified. 
     * It does this by looking through the interface that has been specified and checking that all the mandatory parameters have been specified at least once.
     * It will throw exception at the first error.
 * @throws ParameterNotInInterfaceException
 * @throws MandatoryParameterNotPassedException
 * @throws ParameterDescriptionNotFoundException
     */
    public boolean checkParameterValues()
            throws ParameterNotInInterfaceException,
            MandatoryParameterNotPassedException, ParameterDescriptionNotFoundException {
        //first check that the parameters are allowed at all
        ParameterValue[] inputs = tool.getInput().getParameter();
        checkParametersInDefinition(inputs);
        ParameterValue[] outputs = tool.getOutput().getParameter();
        checkParametersInDefinition(outputs);
               
 
        // Then check on parameter cardinalities
        String[] inputNames = applicationInterface.getArrayofInputs();
        for (int i = 0; i < inputNames.length; i++) {
            String inputName = inputNames[i];
            checkCardinality(inputName, true);

        }
        String[] outputNames = applicationInterface.getArrayofOutputs();
        for (int i = 0; i < outputNames.length; i++) {
            String outputName = outputNames[i];
            checkCardinality(outputName, false);
        }
        status = Status.INITIALIZED;
        return true;
   }

    /**
     * Checks that all of the {@link ParameterValue}s in an array are allowed by the application definition. It will throw an exeception on the first error found.
     * @param pv the array of parameter values to be checked
     * @throws ParameterDescriptionNotFoundException
     */
    private void checkParametersInDefinition(ParameterValue[] pv) throws ParameterDescriptionNotFoundException
    {
        for (int i = 0; i < pv.length; i++) {
            ParameterValue value = pv[i];
            getApplicationDescription().getParameterDescription(value.getId()); //checks if the parameter exists in description
             
        }
    }
    /**
     * Checks whether parameters specified conform with their cardinality. At the moment this only checks to see if a required parameter is missing.
 * @param inputName the name of the paramter to be checked.
 * @param isInput is the parameter an input parameter.
 * @throws ParameterDescriptionNotFoundException
 * @throws ParameterNotInInterfaceException
 * @throws MandatoryParameterNotPassedException
 */
private void checkCardinality(String inputName, boolean isInput) throws ParameterDescriptionNotFoundException, ParameterNotInInterfaceException, MandatoryParameterNotPassedException {
    Cardinality car = applicationInterface
            .getParameterCardinality(inputName);
    if (car.getMinOccurs() != 0) {
        ParameterValue pval = isInput?findInputParameter(inputName):findOutputParameter(inputName);
        if (pval == null) {
            throw new MandatoryParameterNotPassedException(
                    tool.getId()+ " -- The interface " + applicationInterface.getId()
                            + " expects "+ (isInput?"input":"output") +" parameter " + inputName);
        }
    }
}
    /** can be extended by subclasses to provide more info */
   public MessageType createTemplateMessage() {
       MessageType mt = new MessageType();
       mt.setSource(this.toString() + "\nid:" + this.getId() + "\nassignedId: " + this.getJobStepID());
       
    mt.setTimestamp(new DateTime());
       mt.setPhase(status.toExecutionPhase());
       return mt;
   }
   
   /** subclassing helper - find a parameter by name in the tool inputs */
   protected final ParameterValue findInputParameter(String name) {
       return (ParameterValue)tool.getInput().findParameter(name);

   }
   
  // querying parameter adapters. 
  /** find the parameter adapter for the named input parameter */
  protected final ParameterAdapter findInputParameterAdapter(String name) {
      for (Iterator i = inputAdapters.iterator(); i.hasNext(); ) {
          ParameterAdapter a = (ParameterAdapter)i.next();
          if (a.getWrappedParameter().getId().equals(name)) {
              return a;
          }
      }
      return null;
  }
   /** subclassing helper - find a parameter by name in the tool outputs */
   protected final ParameterValue findOutputParameter(String name) {
       return (ParameterValue)tool.getOutput().findParameter(name);
       
   }
  /** find the parameter adapter for the named output parameter */
   protected final ParameterAdapter findOutputParameterAdapter(String name) {   
      for (Iterator i = outputAdapters.iterator(); i.hasNext(); ) {
          ParameterAdapter a = (ParameterAdapter)i.next();
          if (a.getWrappedParameter().getId().equals(name)) {
              return a;
          }
      }      
      return null;      
  }
  
  /** find the parameter adapter for the named parameter (which may be either input or output) */
  protected final ParameterAdapter findParameterAdapter(String name) {
      ParameterAdapter a = findInputParameterAdapter(name);
      if (a == null) {
          a = findOutputParameterAdapter(name);
      }
      return a;
  }

  /** access the description associated with this application */
   public final ApplicationDescription getApplicationDescription() {
      return applicationInterface.getApplicationDescription();
   }
 
/** access the interface of {@link #getApplicationDescription()} that is to be executed */
   public final ApplicationInterface getApplicationInterface()  {
       return applicationInterface;
   }
    public final String getId() {
        return applicationEnvironment.getExecutionId();
    }
    
    public final Tool getTool() {
        return tool;
    }
   
   public final ParameterValue[] getInputParameters() {
       return tool.getInput().getParameter();
   }

   public final String getJobStepID() {
      return applicationEnvironment.getJobStepId();
   }

   
   public final ResultListType getResult() {
       return results;
   }
   

   /**
    * Reveals the status of the job.
    *
    * @return The status.
    */
   public final  Status getStatus() {
      return status;
   }

   public final SecurityGuard getSecurityGuard() {
      return applicationEnvironment.getSecGuard();
   }
   /** iterator over all parameter values in the tool inputs */
   protected final  Iterator inputParameterValues() {
       return tool.getInput().getParameterOrParameterGroup().iterator(); //FIXME this could be parameter groups etc
   }
   /** iterate over all parameter values in the tool outputs */
   protected final Iterator outputParameterValues() {
       return tool.getOutput().getParameterOrParameterGroup().iterator(); //FIXME this could include groups...
   }
  
   /** iterator over all input parameter adapters */
   protected final Iterator inputParameterAdapters() {
       return inputAdapters.iterator();
   } 
   /** iterator over all output parameter adapters */
   protected final Iterator outputParameterAdapters() {
       return outputAdapters.iterator();
   }
   /** iterator over all input and output parameter adapters */
   protected final Iterator parameterAdapters() {
       IteratorChain i = new IteratorChain();
       i.addIterator(inputAdapters.iterator());
       i.addIterator(outputAdapters.iterator());       
       return i;
   }

    /** report an error message - to the log, and to all observers 
     * sets status to {@link Status#ERROR}
     * @see #setStatus(Status)
     * */
    protected final void reportError(String msg) {
        logger.error(msg); 
        MessageType mt = createTemplateMessage();
        mt.setContent(msg);
        mt.setLevel(LogLevel.ERROR);
        setChanged();
        notifyObservers(mt);
        setStatus(Status.ERROR);        
    }
    /** report an exception - to the log, and to all observers 
     * sets status to {@link Status#ERROR}
     * @see #setStatus(Status)
     * */
    protected final void reportError(String msg, Throwable e) {
        logger.error(msg,e); 
        MessageType mt = createTemplateMessage();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(msg);
        pw.println(e.getMessage());
        e.printStackTrace(pw);
        pw.close();
        mt.setContent(sw.toString());
        mt.setLevel(LogLevel.ERROR);
        setChanged();
        notifyObservers(mt);
        setStatus(Status.ERROR);
        
    }
    /** report a warning - to the log and all observers.
     * does not change status of application
     * @param msg
     */
    protected final void reportWarning(String msg) {
        logger.warn(msg);
        MessageType mt = createTemplateMessage();
        mt.setContent(msg);
        mt.setLevel(LogLevel.WARN);
        setChanged();
        notifyObservers(mt);
    }
    
    /** report a warning - to the log and all observers
     * does not change status of applicaiton
     * @param msg
     * @param e
     */
    protected final void reportWarning(String msg,Throwable e) {
        logger.warn(msg,e);
        MessageType  mt = createTemplateMessage();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(msg);
        pw.println(e.getMessage());
        e.printStackTrace(pw);
        pw.close();
        mt.setContent(sw.toString());
        mt.setLevel(LogLevel.WARN);
        setChanged();
        notifyObservers(mt);
    }
    
    /** report an arbitrary message - to the log, and also to all observers */
    protected final void reportMessage(String msg) {
        logger.info(msg); 
        MessageType mt = createTemplateMessage();
        mt.setContent(msg);
        mt.setLevel(LogLevel.INFO);
        setChanged();
        notifyObservers(mt);         
    }

   /**
    * change the status of this application and notify all observers 
    * @param status The status to set.
    */
   public final void setStatus(Status status) {
      this.status = status;
      if(status.equals(Status.RUNNING)){
	  recordStartOfExecution();
      }
      else if (status.equals(Status.ABORTED)||status.equals(Status.COMPLETED)||status.equals(Status.ERROR))
      {
	  recordEndOfExecution();
      }
      setChanged();
      notifyObservers(status);
   }


   @Override
public String toString() {
      return getApplicationDescription().getId() + "#" + getApplicationInterface().getId() + " runid=" + getId(); 
   }

  /** 
   * Creates a Runnable that will execute the job.
   * This must be suitably implemented in the sub-classes for particular
   * types of application-server.
   * <p>
   * Usual body of method does
   * <ol>
   * <li>(optional) inspect / adjust parameter values</li>
   * <li> call {@link #createAdapters()} to create parameterAdapters</li>
   * <li> create runnable result object</li>
   * </ol>
   * responsibilities of runnable are
   * <ol> 
   * <li>iterate through input parameter adapters, calling 
   *     {@link ParameterAdapter#process()} on each, collecting returned
   *     parameter values;</li>
   * <li>set application state to {@link Status#INITIALIZED};</li>
   * <li>sets application state to {@link Status#RUNNING};</li>
   * <li>performs applicatioin execution in some way, passing in parameter values;</li>
   * <li>reports progress via {@link #reportMessage(String)}, etc.;</li>
   * <li>on application complettion, set application 
   *      state to {@link Status#WRITINGBACK};</li>
   * <li>iterate through output parameter adapters, calling 
   *     {@link ParameterAdapter#writeBack(Object)} on each;</li>
   * <li>set application state to {@link Status#COMPLETED}.</li>
   * </ol>
   *
   * @see org.astrogrid.applications.Application#createExecutionTask()
   * @return null 
   */
  public abstract Runnable createRunnable();
   
  public FutureTask<String> createExecutionTask() throws CeaException{
      if(task == null ){
      createAdapters();
      task = new ApplicationTask(createRunnable(), getId(), this);
      }
      return task;
  }
    
  /**
   * Notes that the application is committed for execution but is held
   * on a queue. Sets the status to QUEUED.
   *
   * @since 2007.2.02
   */
  public void enqueue() {
    this.setStatus(Status.QUEUED);
  }
public Date getDestruction() {
    return destruction;
}
public void setDestruction(Date destruction) {
    this.destruction = destruction;
}
}

