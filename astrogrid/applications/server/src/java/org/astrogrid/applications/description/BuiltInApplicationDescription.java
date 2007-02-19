package org.astrogrid.applications.description;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.BuiltInApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An {@link ApplicationDescription} whose details are known at compile time.
 *
 * @author Guy Rixon
 */
public class BuiltInApplicationDescription extends AbstractApplicationDescription {
  
  
  /**
   * Constructs a BuiltInApplicationDescription.
   */
  public BuiltInApplicationDescription(ApplicationDescriptionEnvironment env) 
      throws CeaException {
    super(env);
    
    // This is the default application that is always present in a CEA service.
    this.setName("org.astrogrid.unregistered/default");
    
    // There is one output parameter and it is a string.
    BaseParameterDescription out = new BaseParameterDescription();
    out.setName("out");
    out.setType(ParameterTypes.TEXT);
    this.addParameterDescription(out);
    
    // The first input parameter is a double-precisions number.
    BaseParameterDescription in1 = new BaseParameterDescription();
    in1.setName("in1");
    in1.setType(ParameterTypes.DOUBLE);
    this.addParameterDescription(in1);
    
    // The second input parameter is a string.
    BaseParameterDescription in2 = new BaseParameterDescription();
    in2.setName("in2");
    in2.setType(ParameterTypes.TEXT);
    this.addParameterDescription(in2);
    
    // Group the parameters into an interface.
    BaseApplicationInterface ai = new BaseApplicationInterface("default", this);
    ai.addInputParameter("in1");
    ai.addInputParameter("in2");
    ai.addOutputParameter("out");
    this.addInterface(ai);
  }
  
  /**
   * Sets up a job using the described application.
   */
  public Application initializeApplication(String userAssignedId,
                                           User   user,
                                           Tool   tool)
      throws InterfaceDescriptionNotFoundException {
    DefaultIDs ids = new DefaultIDs(userAssignedId, 
                                    this.env.getIdGen().getNewID(), 
                                    user);
    return new BuiltInApplication(ids,
                                  tool,
                                  this.getInterfaceForTool(tool),
                                  this.env.getProtocolLib());
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
                    this.getName() +
                    " does not have an interface called " +
                    tool.getInterface()
                );
    }
  }
  
}
