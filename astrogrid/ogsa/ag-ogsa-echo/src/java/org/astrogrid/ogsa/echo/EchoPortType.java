package org.astrogrid.ogsa.echo;

import java.rmi.RemoteException;


/**
 * Defines the operations of the echo port-type.
 */
public interface EchoPortType {

  /**
   * Returns the given string to the caller.
   */ 
  public String echo (String s);
  
  
  /**
   * Returns the caller's identity, taken from the security
   * credentials.
   */
  public String whoAmI ();
  
  
  /**
   * Tests the presence of a delegate security-credential.
   * Such a credential should be present if the caller uses
   * message-level security in the call to this operation
   * with delegation of privileges turned on.
   *
   * @return the name from the credential
   */
  public String testDelegatedCredentials () throws RemoteException;
}
