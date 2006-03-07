package org.astrogrid.applications.manager;

/**
 * An implementation of
 * {org.astrogrid.applications.manager.AppAuthorityIDResolver}
 * for testing. This class sets a fixed ID at construction.
 *
 * @author Guy Rixon
 */
public class TestAppAuthorityIDResolver implements AppAuthorityIDResolver{
  
  /**
   * The authority ID, set at construction.
   */
  private String authorityID;
  
  /**
   * Creates a new instance of TestAppAuthorityIDResolver 
   */
  public TestAppAuthorityIDResolver(String id) {
    this.authorityID = id;
  }
  
  /**
   * Obtains the authority ID set at construction.
   *
   * @return The ID
   */
  public String getAuthorityID() {
    return this.authorityID;
  }
}
