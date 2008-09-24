package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;

/**
 * A configuration interface specialized for the JC-CEC.
 * This kind of configuration has a method to get the
 * class implementing the applications.
 *
 * @author Guy Rixon
 * @deprecated any application class such as this should be a direct parameter of the {@link ApplicationDescriptionLibrary}- this ties the class to tightly with other configuration parameters - now we have the possibility of multiple libraries in the same CEC.
 */
public interface JavaClassConfiguration extends Configuration {
  
  /**
   * Gets the class implementing the applications.
   * Note that this gets the Class object, not an
   * instance of the class; the caller has to instantiate
   * the class.
   *
   * @return The class.
 * @throws ClassNotFoundException 
   */
  public Class getApplicationClass() throws ClassNotFoundException;
}
