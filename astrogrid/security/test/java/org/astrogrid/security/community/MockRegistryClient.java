package org.astrogrid.security.community;

/**
 *
 * @author Guy Rixon
 */
public class MockRegistryClient extends RegistryClient {
  
  /**
   * Constructs a MockRegistryClient.
   */
  public MockRegistryClient() {
    super(new MockRegistry());
  }
  
}
