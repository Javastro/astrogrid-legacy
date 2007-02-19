package org.astrogrid.applications;

import java.util.ArrayList;
import java.util.Iterator;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An implementation of {@link Application} which is entirely self-contained.
 * The application logic is built into this class which in turn is built in
 * to every copy of the CEA core. This Application can be used for testing.
 *
 * The built-in application itself is to echo the input arguments to the 
 * output argument. This can be changed, in a sub-class, by overriding the
 * {@link go()} method.
 *
 * @author Guy Rixon
 */
public class BuiltInApplication extends AbstractApplication implements Runnable {
  
  /**
   * Constructs a BuiltInApplication.
   */
  public BuiltInApplication(IDs                  ids,
                            Tool                 tool, 
                            ApplicationInterface applicationInterface,
                            ProtocolLibrary      lib) {
    super(ids, tool, applicationInterface, lib);
  }
  
  /** 
   * Makes the application executable.
   * Processes all input parameters, sets initial status, then nominates its
   * containing object as the execution task: i.e. the class containing this
   * method must implement Runnable.
   * @todo bug here - we assume our parameters are in the correct order to pass to the java method. should sort them into correct order first.
   * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.ApplicationExitMonitor)
   */
  public Runnable createExecutionTask() throws CeaException {
    super.createAdapters();
    setStatus(Status.INITIALIZED);
    return this;
  }
  
  /**
   * Runs the application to completion.
   * Acquires input parameters and writes back output parameters.
   */
  public void run() {
    
    // Acquire the input parameters.
    ArrayList args = new ArrayList();
    try {
      for (Iterator i = super.inputParameterAdapters(); i.hasNext(); ) {
        ParameterAdapter a = (ParameterAdapter)i.next();
        args.add(a.process());
      }             
    } 
    catch (CeaException e1) {
      super.reportError("Failed to read input parameters.", e1);
      return;
    }
    catch (IllegalArgumentException e2) {
      super.reportError("Input parameters are invalid.", e2);
      return;
    }
    
    // Run the application.
    super.setStatus(Status.RUNNING);
    Object resultVal = null;
    resultVal = this.go(args.toArray());
    
    // Write the internal reult to the output parameter.
    // Assume there's only one such parameter.'
    super.setStatus(Status.WRITINGBACK);
    ParameterAdapter result = (ParameterAdapter)outputParameterAdapters().next();
    try {
      result.writeBack(resultVal);
    } catch (CeaException e) {
      super.reportError("Cannot write back the output parameter.", e);
      return;
    }
    
    setStatus(Status.COMPLETED);    
  }
  
  /**
   * Runs the application to completion.
   * Input parameters must be acquired before calling this method.
   * The output parameter must be written back afterwards.
   * The "application" is simply to echo the input arguments as a string.
   * This could be overridden by a sub-class to make a different application.
   */
  protected Object go(Object[] args) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i].toString());
      sb.append(" ");
    }
    return sb.toString().trim();
  }
}
