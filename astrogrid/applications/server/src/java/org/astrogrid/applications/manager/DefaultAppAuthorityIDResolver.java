package org.astrogrid.applications.manager;

import org.astrogrid.config.SimpleConfig;

/**
 * An implementation of BaseApplicationDescriptionLibrary.AppAuthorityIDResolver
 * that uses the AstroGrid configuration to get its value.
 * This is required by the ApplicationDescriptionEnvironment.
 * This implementation was refactored from an anonymous inner class in 
 * EmptyCEAComponentManager. The anonymous class works in unit tests and in
 * the web service but Picocontainer cannot use it when called in a JSP;
 * presumably the class-loading details are different there.
 *
 * @author Guy Rixon
 */
public class DefaultAppAuthorityIDResolver implements AppAuthorityIDResolver {
  
  public String getAuthorityID() {
    return SimpleConfig.getSingleton().getString("cea.application.authorityid", 
                                                 "org.astrogrid.localhost");
  }
  
}
