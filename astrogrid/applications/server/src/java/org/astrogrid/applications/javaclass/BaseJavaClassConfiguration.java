package org.astrogrid.applications.javaclass;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/**
 * A configuration object specialized for the JC-CEC.
 * This kind of configuration has a method to get the
 * class implementing the applications.
 *
 * This class uses SimpleConfig to get the information.
 *
 * @author Guy Rixon
 */
public class BaseJavaClassConfiguration 
    extends CEAConfiguration 
    implements JavaClassConfiguration, ComponentDescriptor {
  
  private static final Log logger 
      = LogFactory.getLog(BaseJavaClassConfiguration.class);
  
  @SuppressWarnings("unchecked")
  private Class applicationClass;
  
  /**
   * Constructs a BaseJavaClassConfiguration.
   *
   * @throws IOException If the superclass constructor fails. 
   */
  public BaseJavaClassConfiguration(String classname) throws IOException {
    super();
    this.initializeApplicationClass(classname);
  }
  
  /**
   * Determines the name of the class implementing the application.
   * The class name is read from the external environment. If not
   * set in the environment, a default class is used.
   */
  protected void initializeApplicationClass(String className) {
    try {
      this.applicationClass = Class.forName(className);
      logger.info("The application is implemented by Java class " + className);
    }
    catch (Exception e) {
      logger.warn("The class configured in cea.javaclass.server.class " +
                   "cannot be found. " + 
                   SampleJavaClassApplications.class.getName() +
                   " will be used instead.");
      this.applicationClass = SampleJavaClassApplications.class;
    }
  }
  
  /**
   * Obtains the class implementing the application.
   *
   * @return The class.
   */
  public Class getApplicationClass() {
    return this.applicationClass;
  }

  /**
   * Reveals the name of the component.
   */
  @Override
public String getName() {
    return "Configuration for a Java-class CEC.";
  }
  
  /**
   * Describes the component and its current state.
   */
  @Override
public String getDescription() {
    StringBuffer sb = new StringBuffer();
    sb.append("Class providing the applications: ");
    sb.append(this.getApplicationClass().getName());
    sb.append("\n");
    return super.getDescription() + sb.toString();
  }
}
