package org.astrogrid.security;

import java.util.Set;
import java.security.Principal;
import javax.security.auth.Subject;


/**
 * Access to the security credentials pertaining web-service operations.
 *
 * This is a Java-bean class in which the properties are various
 * credentials for secured messaging. It is a standard way, within its
 * package, of passing credentials within the same JVM.
 *
 * Applications may use this class directly, but are more likely to
 * use one of the two subclasses {@link ClientSecurityGuard} and
 * {@link ServiceSecurityGuard}. The latter two classes add methods
 * to interact with JAX-RPC handler-chains and thus to use the
 * credentials in SOAP messages.
 *
 * @see ClientSecurityGuard
 * @see ServiceSecurityGuard
 *
 * @author Guy Rixon
 */
public class SecurityGuard {

  /**
   * The JAAS subject.
   */
  protected Subject subject;


  /**
   * Constructs a SecurityGuard with an empty
   * JAAS subject.
   */
  public SecurityGuard () {
    this.subject = new Subject();
  }


  /**
   * Constructs a SecurityGuard with a
   * given JAAS subject.
   */
  public SecurityGuard (Subject s) {
    this.subject = s;
  }


  /**
   * Sets the username property.
   *
   * @param name the user-name
   */
  public void setUsername (String name) {
    AccountName account = new AccountName(name);
    this.subject.getPrincipals().add(account);
  }

  /**
   * Returns the username property.
   *
   * @return the user-name (may be null if the property is not set)
   */
  public String getUsername () {
    Set names = this.subject.getPrincipals();
    if (names.size() == 0) {
      return null;
    }
    else {
      return ((Principal) names.iterator().next()).getName();
    }
  }


  /**
   * Sets the password property.
   */
  public void setPassword (Password pass) {
    this.subject.getPrivateCredentials().add(pass);
  }

  /**
   * Returns the password property.
   *
   * @return the password (may be null if the property is not set)
   */
   public Password getPassword () {
     Set passwords = this.subject.getPrivateCredentials(Password.class);
     if (passwords.size() == 0) {
       return null;
     }
     else {
       return (Password) passwords.iterator().next();
     }
   }


  /**
   * Sets an AstroGrid security token.  The token as a
   * whole is set as a private credential and the
   * account name derivd from the token is set as
   * a Principal.
   */
  public void setNonceToken(NonceToken t) {
    this.subject.getPrivateCredentials().add(t);
    AccountName n = new AccountName(t.getAccount());
    this.subject.getPrincipals().add(n);
  }


  /**
   * Returns an AstroGrid scurity token.
   *
   * @return the token (null if no token is set)
   */
  public NonceToken getNonceToken () {
    Set tokens = this.subject.getPrivateCredentials(NonceToken.class);
    if (tokens.size() > 0) {
      return (NonceToken) tokens.iterator().next();
    }
    else {
      return null;
    }
  }


  /**
   * Returns the JAAS Subject.  The Subject
   * contains the credentials and "principals"
   * (i.e. identities) already set on the SecurityGuard.
   * If this method is called immediately after construction
   * then an empty Subject is returned. Note that altering
   * the returned subject alters the information
   * inside the SecurityGuard.
   *
   * @return the subject (never null)
   */
  public Subject getSubject () {
    return this.subject;
  }

}
