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
 * The SecurityGuard maintains two sets of credentials: "single-sign-on"
 * (SSO) and "grid".  The SSO credentials are used to sign on to the
 * grid via some portal that manages user accounts. The grid credentials
 * are used to authenticate messages to services in the grid. A user
 * obtains the grid credentials by signing on with the SSO credentials.
 *
 * The two sets of credentials are stored in a pair of JAAS Subjects.
 * These are available to applications as the properties ssoSubject and
 * gridSubject. Note that this property may be got but not set; a
 * caller is not allowed to impose a complete new subject or to make
 * a subject null.  However, a caller may get a reference to one of the
 * subjects and change that subject's contents.
 *
 * @see {@link ClientSecurityGuard}
 * @see {@link ServiceSecurityGuard}
 *
 * @author Guy Rixon
 */
public class SecurityGuard {

  /**
   * The JAAS subject for grid credentials.
   */
  protected Subject gridSubject;

  /**
   * The JAAS subject for single-sign-on credentials.
   */
  protected Subject ssoSubject;


  /**
   * Constructs a SecurityGuard with empty
   * JAAS subjects.
   */
  public SecurityGuard () {
    this.gridSubject = new Subject();
    this.ssoSubject  = new Subject();
  }


  /**
   * Constructs a SecurityGuard with a
   * given JAAS subject for grid credentials.
   * No SSO credentials are set.
   */
  public SecurityGuard (Subject s) {
    this.gridSubject = s;
    this.ssoSubject  = new Subject();
  }


  /**
   * Returns the JAAS Subject for grid credentials.
   * The Subject contains the credentials and "principals"
   * (i.e. identities) already set on the SecurityGuard.
   * If this method is called immediately after construction
   * then an empty Subject is returned. Note that altering
   * the returned subject alters the information
   * inside the SecurityGuard.
   *
   * @return the subject (never null)
   */
  public Subject getGridSubject () {
    return this.gridSubject;
  }

  /**
   * Returns the JAAS Subject for single-sign-on credentials.
   * The Subject contains the credentials and "principals"
   * (i.e. identities) already set on the SecurityGuard.
   * If this method is called immediately after construction
   * then an empty Subject is returned. Note that altering
   * the returned subject alters the information
   * inside the SecurityGuard.
   *
   * @return the subject (never null)
   */
  public Subject getSsoSubject () {
    return this.ssoSubject;
  }


  /**
   * Sets the username property.
   *
   * @param name the user-name
   */
  public void setUsername (String name) {
    AccountName account = new AccountName(name);
    this.gridSubject.getPrincipals().add(account);
  }

  /**
   * Returns the username property.
   *
   * @return the user-name (may be null if the property is not set)
   */
  public String getUsername () {
    Set names = this.gridSubject.getPrincipals();
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
  public void setPassword (String word) {
    try {
      Password p = new Password(word, false);
      this.gridSubject.getPrivateCredentials().add(p);
    }
    catch (Exception e) {
      // Ignore the exception for now.
      // This is a horrible kludge; needs refactoring out.
    }
  }

  /**
   * Returns the password property.
   *
   * @return the password (may be null if the property is not set)
   */
   public String getPassword () {
     Set passwords = this.gridSubject.getPrivateCredentials(Password.class);
     if (passwords.size() == 0) {
       return null;
     }
     else {
       return ((Password) passwords.iterator().next()).getPlainPassword();
     }
   }


  /**
   * Sets an AstroGrid security token.  The token as a
   * whole is set as a private credential and the
   * account name derivd from the token is set as
   * a Principal.
   */
  public void setNonceToken(NonceToken t) {
    this.gridSubject.getPrivateCredentials().add(t);
    AccountName n = new AccountName(t.getAccount());
    this.gridSubject.getPrincipals().add(n);
  }


  /**
   * Returns an AstroGrid scurity token.
   *
   * @return the token (null if no token is set)
   */
  public NonceToken getNonceToken () {
    Set tokens = this.gridSubject.getPrivateCredentials(NonceToken.class);
    if (tokens.size() > 0) {
      return (NonceToken) tokens.iterator().next();
    }
    else {
      return null;
    }
  }


  /**
   * Sets the account name for single sign on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @Todo store this name in a separate SSO Subject.
   *
   * @param name the account name
   */
  public void setSsoUsername (String name) {
    this.setUsername(name);
  }

  /**
   * Retrieves the account name for single sign on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @Todo store this name in a separate SSO Subject.
   *
   * @return the account name
   */
  public String getSsoUsername () {
    return this.getUsername();
  }


  /**
   * Sets the password for single sign on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @Todo store this password in a separate SSO Subject.
   *
   * @param word the password
   */
  public void setSsoPassword (String word) {
    this.setPassword(word);
  }

  /**
   * Retrieves the password for single sign on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @Todo store this name in a separate SSO Subject.
   *
   * @return the password
   */
  public String getSsoPassword () {
    return this.getPassword();
  }

}
