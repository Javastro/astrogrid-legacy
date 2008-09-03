package org.astrogrid.applications.manager;


/**
 * An implementation of BaseApplicationDescriptionLibrary.AppAuthorityIDResolver
 * that uses the AstroGrid configuration to get its value.
 * This is required by the ApplicationDescriptionEnvironment.
 * 
 * @author Guy Rixon
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jun 2008
 */
public class DefaultAppAuthorityIDResolver implements AppAuthorityIDResolver {
  
  private final String authority;

  public DefaultAppAuthorityIDResolver(String auth)
  {
      // used to look at the key cea.application.authorityid to get this value
      this.authority=auth;
  }
public String getAuthorityID() {
     return authority;
  }
  
}
