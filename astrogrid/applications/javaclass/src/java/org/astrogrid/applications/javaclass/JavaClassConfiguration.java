package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.contracts.Configuration;

/**
 * A configuration interface specialized for the JC-CEC.
 * This kind of configuration has a method to get the
 * class implementing the applications.
 *
 * @author Guy Rixon
 */
public interface JavaClassConfiguration extends Configuration {
  
  /**
   * Gets the class implementing the applications.
   * Note that this gets the Class object, not an
   * instance of the class; the caller has to instantiate
   * the class.
   *
   * @return The class.
   */
  public Class getApplicationClass();
}
