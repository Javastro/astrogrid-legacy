package org.astrogrid.security;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * A client for a sign-on service.
 * The service, of which there can be several different kinds, supplies
 * cryptographic credentials in exchange for a user-name and password.
 *
 * @author Guy Rixon
 */
public interface SignOnClient {

  /**
   * Authenticates the user-name to the servcie and receives the user's
   * credentials. The credentials are written into a given {@link SecurityGuard}.
   *
   * @param userName The user name to be authenticated.
   * @param password The password to authenticate the user-name.
   * @param lifetime The requested lifetime of the credentials, in seconds.
   * @param guard    The object to receive the credentials.
   * @throws IOException If the sign-on service cannot be contacted.
   * @throws GeneralSecurityException If authentication fails.
   */
  public void authenticate(String        userName,
                           String        password,
                           int           lifetime,
                           SecurityGuard guard) throws IOException,
                                                       GeneralSecurityException;

  /**
   * Determines the location of the user's home-space in VOSpace.
   * The result is stored in the given security-guard as a Principal.
   * Not all sign-on protocols supply this information. If a protocol cannot
   * give an answer it should return silently with no action. The client
   * should check the result in the security guard.
   *
   * @param userName The identity for which the home space is requested.
   * @param guard The object to receive the information.
   * @throws IOException If the sign-on service cannot be contacted.
   */
    public void home(String        userName,
                     SecurityGuard guard) throws IOException,
                                                 GeneralSecurityException;


  /**
   * Changes the user's password.
   * The request is authenticated by the old password.
   *
   * @param userName The user name known to the community.
   * @param oldPassword The old password known to the community.
   * @param newPassword The replacement password.
   */
  public void changePassword(String        userName,
                             String        oldPassword,
                             String        newPassword,
                             SecurityGuard guard) throws GeneralSecurityException,
                                                         IOException;
}
