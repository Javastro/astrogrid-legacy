package org.astrogrid.applications.component;

import junit.framework.TestCase;

/**
 * JUnit tests for 
 * {@link org.astrogrid.applications.component.EmptyCEAComponentmanager}.
 * The tests check whether the manager is functional after the default
 * components are registered.
 *
 * @author Guy Rixon
 */
public class BaseCEAComponentManagerTest extends TestCase {
  
  public void testDefaultComponents() throws Exception {
    BaseCEAComponentManager m = new BaseCEAComponentManager();
    m.start();
    m.getContainer().verify();
    
    // All these calls will throw exceptions if the relevant components
    // have not been correctly registered.
    m.getControlService();
    m.getMetadataService();
    m.getMetadataService();
    m.getQueryService();
    m.getRegistryUploaderService();
    m.getExecutionController();
  }
  
}
