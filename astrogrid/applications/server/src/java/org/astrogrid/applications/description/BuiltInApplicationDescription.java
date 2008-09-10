package org.astrogrid.applications.description;

import net.ivoa.resource.cea.CeaApplication;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationKind;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.environment.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.environment.WorkingDirectoryAlreadyExists;
import org.astrogrid.applications.javainternal.BuiltInApplication;
import org.astrogrid.security.SecurityGuard;

/**
 * An {@link ApplicationDescription} whose details are known at compile time. The application is known as
 * ivo://org.astrogrid.unregistered/default
 *
 * @author Guy Rixon
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 Mar 2008
 */
public class BuiltInApplicationDescription extends InternallyConfiguredApplicationDescription {
  
  
  private static final CeaApplication app = new CeaApplication();
  
  static {
      // This is the default application that is always present in a CEA service.
      app.setIdentifier("ivo://org.astrogrid.unregistered/default");
      app.setShortName("built-in");
      app.getApplicationDefinition().getApplicationType().add(ApplicationKind.PROCESSING);
      app.getContent().setDescription("A small mock application that is always present in all CEA Servers for testing purposes");
      addParameter(app,"out",ParameterTypes.TEXT,"out","There is one output parameter and it is a string");
      addParameter(app,"in1",ParameterTypes.REAL,"in1","The first input parameter is a double-precisions number");
      addParameter(app,"in2",ParameterTypes.TEXT,"int2","The second input parameter is a string.");
      addParameter(app,"in3",ParameterTypes.INTEGER,"sleeptime","The time for the applicaton to sleep in seconds");
      InterfaceDefinition intf = addInterface(app, "default");
      intf.addInput("in1");
      intf.addInput("in2");
      intf.addInput("in3");
      intf.addOutput("out");
  }

/**
   * Constructs a BuiltInApplicationDescription. 
 * @param conf 
   */
  public BuiltInApplicationDescription(Configuration conf) 
      throws CeaException {
      
    super(app, conf);
    
  }
  
  /**
   * Sets up a job using the described application.
 * @throws CannotCreateWorkingDirectoryException 
 * @throws WorkingDirectoryAlreadyExists 
   */
  public Application initializeApplication(String userAssignedId,
                                           SecurityGuard   secGuard,
                                           Tool   tool)
      throws InterfaceDescriptionNotFoundException, CannotCreateWorkingDirectoryException, WorkingDirectoryAlreadyExists {
      
    
    ApplicationEnvironment env = new ApplicationEnvironment(userAssignedId, secGuard, getInternalComponentFactory().getIdGenerator(), conf);
  
    return new BuiltInApplication(
                                  tool,
                                  this.getInterfaceForTool(tool),
                                  env ,getInternalComponentFactory().getProtocolLibrary());
  }
  
  /**
   * Checks the requested interface; returns the default interface if OK.
   */
  private ApplicationInterface getInterfaceForTool(Tool tool)
      throws InterfaceDescriptionNotFoundException {
    String requestedInterfaceName = tool.getInterface();
    if (requestedInterfaceName == null) {
      return this.getInterface("default");
    }
    else if (requestedInterfaceName.equals("default")) {
      return this.getInterface("default");
    }
    else {
      throw new InterfaceDescriptionNotFoundException(
                    "Application " +
                    this.getId() +
                    " does not have an interface called " +
                    tool.getInterface()
                );
    }
  }
  
}
