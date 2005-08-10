/*
 * $Id: AbstractApplication.java,v 1.11 2005/08/10 17:45:10 clq2 Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Basic implementation of {@link org.astrogrid.applications.Application}
 * <p />
 * This is an abstract class that CEA providers must extend. 
 * 
 * <I>This class provides a lot of protected-visibility helper methods, private fields, final methods, etc - it tries to 
 * impose some structure on the form the extension takes. This may mean that it'd be better to split this class into a framework class, and a plugin class. But we'll see</i>
 * <h2>Abstract Methods</h2>
 * Extenders must implement the {@link #execute()} method.
 * <h2>Overridable Methods</h2>
 * The {@link #instantiateAdapter(ParameterValue, ParameterDescription, IndirectParameterValue)} method may be overridden to return instances of a custom {@link org.astrogrid.applications.parameter.ParameterAdapter} if needed<br/>
 * The default implementaiton of {@link #attemptAbort()} does nothing. Providers may extend this if suitable.<br />
 * The {@link #createTemplateMessage()} method can also be overridden to return more application-specific information, similarly {@link #toString()} if desired.<br/>
 * 
 * <h2>Helper Methods</h2>
 * This class provides helper methods for working with parameters, parameter adapters, and for progress reporting.
*  <h3>Parameters</h3>
*    There's methods to iterate through input parameters, output parameters, and all parameters, and to find input / output / either by name
*  <h3>Parameter Adapters</h3>
*    There's methods to iterate through input parameterAdapters, output parameterAdapters, and all parameterAdapters - ant to find input / output / eitther kind of adapter by parameter name.
*    All these methods have {@link #createAdapters()} as a precondition - this initializes the adapter sets.
*  <h3>Progress Reporting</h3>
* There's a set of methods that hide the details of notifying observers<p>
*  Calls to {@link #setStatus(Status)} update the status field of the application, and also send a change notification to all observers registered to this application. The 
* {@link #reportMessage(String)} method will send a message to each observer, as well as adding it to the log. Similarly with {@link #reportWarning}. Finally {@link #reportError}
* will send a message <b>and</b> set the status of this application to {@link org.astrogrid.applications.Status#ERROR}. The overridable method {@link #createTemplateMessage()} 
* is used to create the template messages for this methods.
*  @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @see org.astrogrid.applications.javaclass.JavaClassApplication as an example extension
 * @see org.astrogrid.applications.Application
 * @see org.astrogrid.applications.description.ApplicationDescription
 * @see org.astrogrid.applications.parameter.ParameterAdapter
 */
public abstract class AbstractApplication extends Observable implements Application {
   /** interface to the set of identifiers for an application */
   public static interface IDs {
        /** the cea-assigned id for this application execution */
       public String getId();
       /** the user-assigned id for this application execution
        * @todo rename to something less jes-specific
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
   
   protected final IDs ids;
   /** list of parameter adapters for the inputs to the application. emty at start */
   private final List inputAdapters = new ArrayList();
   /** library of indirection protocol handlers */
   protected final ProtocolLibrary lib;
   /** list of parameter adapters for the outputs of the application. empty at start */
   private final List outputAdapters = new ArrayList();
   /** list type containing results of the application execution. obviously empty to start with */
   private final ResultListType results = new ResultListType();

   /**
    * the application status
    */
   private Status status = Status.NEW;
   /** tool object defines the parameters, etc to the application */
   private final Tool tool;
   
   /** construct a new application execution
    * @param ids identifiers for this application execution
    * @param tool defines the parameters of this exeuction, and which application / interface is to be called.
    * @param applicationInterface the descrptioni interface this application conforms to.
    * @param lib library of indirection handlers - used to read remote parameters using various protocols
     */
   public AbstractApplication(IDs ids,Tool tool, ApplicationInterface applicationInterface,ProtocolLibrary lib) 
   {      
      this.applicationInterface = applicationInterface;
      this.ids = ids;
      this.tool = tool;
      this.lib = lib;

   }
    /** default implementation of attemptAbort - always fails, and returns false.
     */
    public boolean attemptAbort() {
        return false;
    }

    /** hook that specialized subclasses can overried - to return a custom adapter  
     * used in {@link #createAdapters}
     * @return a {@link DefaultParameterAdapter}
     * @TODO perhaps make this create different types of adapter dependent on parameter type.
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
        results.clearResult();
        for  (Iterator params = inputParameterValues(); params.hasNext();){
             ParameterValue param = (ParameterValue)params.next();       
            ExternalValue iVal = (param.getIndirect() ? lib.getExternalValue(param) : null);
             ParameterAdapter adapter = this.instantiateAdapter(param,getApplicationDescription().getParameterDescription(param.getName()),iVal);
             inputAdapters.add(adapter);
          }
        for  (Iterator params = outputParameterValues(); params.hasNext();){
            ParameterValue param = (ParameterValue)params.next();       
           ExternalValue iVal = (param.getIndirect() ? lib.getExternalValue(param) : null);
            ParameterAdapter adapter = this.instantiateAdapter(param,getApplicationDescription().getParameterDescription(param.getName()),iVal);
            outputAdapters.add(adapter);
            results.addResult(adapter.getWrappedParameter());
         }          
    }
   
   /**
     * Checks that all the parameters have been specified. 
     * It does this by looking through the interface that has been specified and checking that all the mandatory parameters have been specified at least once.
     * It will throw exception at the first error.
     * @TODO it would be better to gather all of the errors together and thow an exception with all of them in, rather than just the first....
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
            getApplicationDescription().getParameterDescription(value.getName()); //checks if the parameter exists in description
             
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
                    tool.getName()+ " -- The interface " + applicationInterface.getName()
                            + " expects "+ (isInput?"input":"output") +" parameter " + inputName);
        }
    }
}
    /** can be extended by subclasses to provide more info */
   public MessageType createTemplateMessage() {
       MessageType mt = new MessageType();
       mt.setSource(this.toString() + "\nid:" + this.getID() + "\nassignedId: " + this.getJobStepID());
       mt.setTimestamp(new Date());
       mt.setPhase(status.toExecutionPhase());
       return mt;
   }

   /**<b>Deprecated</b> 
    * to be overridden- should start the application running, and then return
    * <p>
    * Usual pattern.
    * <ol>
    * <li>(optional) inspect / adjust parameter values
    * <li> call {@link #createAdapters()} to create parameterAdapters 
    * <li>iterate through input parameter adapters, calling {@link ParameterAdapter#process()} on each, collecting returned parameter values
    * <li>set application state to {@link Status#INITIALIZED}
    * <li>before returning, start background thread which
    * <ol>
    *   <li>sets application state to {@link Status#RUNNING}
    *   <li>performs applicatioin execution in some way, passing in parameter values
    *   <li>reports progress via {@link #reportMessage(String)}, etc.
    *   <li> on application complettion, set application state to {@link Status#WRITINGBACK}
    *   <li>iterate through output parameter adapters, calling {@link ParameterAdapter#writeBack(Object)} on each.
    *   <li>set application state to {@link Status#COMPLETED}
    * </ol>
    *</ol>
    *@deprecated - use {@link #createExecutionTask()} instead
  */
   public  boolean execute() throws CeaException {
       return false;
   }
   
   /** subclassing helper - find a parameter by name in the tool inputs */
   protected final ParameterValue findInputParameter(String name) {
       return (ParameterValue)tool.findXPathValue("input/parameter[name='" + name + "']");

   }
   
  // querying parameter adapters. 
  /** find the parameter adapter for the named input parameter */
  protected final ParameterAdapter findInputParameterAdapter(String name) {
      for (Iterator i = inputAdapters.iterator(); i.hasNext(); ) {
          ParameterAdapter a = (ParameterAdapter)i.next();
          if (a.getWrappedParameter().getName().equals(name)) {
              return a;
          }
      }
      return null;
  }
   /** subclassing helper - find a parameter by name in the tool outputs */
   protected final ParameterValue findOutputParameter(String name) {
       return (ParameterValue)tool.findXPathValue("output/parameter[name='" + name + "']");
       
   }
  /** find the parameter adapter for the named output parameter */
   protected final ParameterAdapter findOutputParameterAdapter(String name) {   
      for (Iterator i = outputAdapters.iterator(); i.hasNext(); ) {
          ParameterAdapter a = (ParameterAdapter)i.next();
          if (a.getWrappedParameter().getName().equals(name)) {
              return a;
          }
      }      
      return null;      
  }
  
    /** subclassing helper - find a parameter by name in the tool inputs or tool outputs */
   protected final ParameterValue findParameter(String name)   {
      return (ParameterValue)tool.findXPathValue("input/parameter[name='" + name + "'] | output/parameter[name='" + name + "']");
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
    public final String getID() {
        return ids.getId();
    }
    
    public final Tool getTool() {
        return tool;
    }
   
   public final ParameterValue[] getInputParameters() {
       return tool.getInput().getParameter();
   }

   public final String getJobStepID() {
      return ids.getJobStepId();
   }

   
   public final ResultListType getResult() {
       return results;
   }
   

   /**
    * @return
    */
   public final  Status getStatus() {
      return status;
   }

   public final User getUser() {
      return ids.getUser();
   }
   /** iterator over all parameter values in the tool inputs */
   protected final  Iterator inputParameterValues() {
       return tool.findXPathIterator("input/parameter");
   }
   /** iterate over all parameter values in the tool outputs */
   protected final Iterator outputParameterValues() {
       return tool.findXPathIterator("output/parameter");
   }
   /** iterator over all parameterValues in the tool inputs and outputs */
   protected final Iterator parameterValues() {
       return tool.findXPathIterator("input/parameter | output/parameter");
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
    * @param status
    */
   public final void setStatus(Status status) {
      this.status = status;
      setChanged();
      notifyObservers(status);
   }


   public String toString() {
      return getApplicationDescription().getName() + "#" + getApplicationInterface().getName();
   }

   /*** to be implemented - create a runnable for applicaton execution, and then return
    * <p>
    * Usual body of method does
    * <ol>
    * <li>(optional) inspect / adjust parameter values
    * <li> call {@link #createAdapters()} to create parameterAdapters
    * <li> create runnable result object
    * </ol>
    * responsibilities of runnable are
    * <ol> 
    * <li>iterate through input parameter adapters, calling {@link ParameterAdapter#process()} on each, collecting returned parameter values
    * <li>set application state to {@link Status#INITIALIZED}
    *   <li>sets application state to {@link Status#RUNNING}
    *   <li>performs applicatioin execution in some way, passing in parameter values
    *   <li>reports progress via {@link #reportMessage(String)}, etc.
    *   <li> on application complettion, set application state to {@link Status#WRITINGBACK}
    *   <li>iterate through output parameter adapters, calling {@link ParameterAdapter#writeBack(Object)} on each.
    *   <li>set application state to {@link Status#COMPLETED}
    * </ol>
    *</ol>
    * 
    *  default implementation of this method - for backwards compatability
    * if this method returns null, then the deprecated {@link #execute()} will be called instead
    * @see org.astrogrid.applications.Application#createExecutionTask()
    * @return null 
    */
    public Runnable createExecutionTask() throws CeaException {
        return null;
    }
}

