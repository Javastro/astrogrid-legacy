package org.astrogrid.applications.javainternal;

import java.util.ArrayList;
import java.util.Iterator;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

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
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 21 Apr 2008
 */
public class BuiltInApplication extends JavaInternalApplication implements Runnable {
  
  /**
   * Constructs a BuiltInApplication.
 * @param appEnv 
   */
  public BuiltInApplication(
                            Tool                 tool, 
                            ApplicationInterface applicationInterface, ApplicationEnvironment appEnv,
                            ProtocolLibrary      lib) {
    super(tool, applicationInterface, appEnv, lib);
  }
  
  /**
   * Runs the application to completion.
   * Acquires input parameters and writes back output parameters.
   */
  public void run() {
    
      reportMessage("starting builtin application");
      super.setStatus(Status.RUNNING);//N.B. for testing purposes it is easier if this does not have a Status.READINGPARAMETERS status
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
    ParameterAdapter pa = findInputParameterAdapter("in3");
    //default parameter adapter returns a string....
    int i;
    try {
   for(i = 0;i < 1000; i++)
    {
	System.out.println(i);
	if(Thread.currentThread().isInterrupted())
	{
	    throw new InterruptedException("Built-in app killed during tight loop");
	}
    }
   try {
	i = Integer.parseInt((String)pa.process());
    } catch (Exception e2) {
	reportError("could not parse value for sleep time parameter in3", e2);
	return;
    }
	Thread.sleep(i*1000); // sleep for a while to simulate a long asynchronous job.
    } catch (InterruptedException e1) {
	reportWarning("stdapp sleep interrupted", e1);
	setStatus(Status.ABORTED);
	return;
    }
    reportMessage("finishing builtin application");
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
