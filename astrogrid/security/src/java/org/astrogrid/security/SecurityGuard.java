package org.astrogrid.security;

import java.io.File;
import java.util.Properties;
import java.util.Set;
import java.security.Principal;
import javax.security.auth.Subject;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;


/**
 * Access to the security credentials pertaining web-service operations.
 *
 * This is a Java-bean class in which the properties are various
 * credentials for secured messaging. It is a standard way, within its
 * package, of passing credentials within the same JVM.
 *
 * Applications may use this class directly, but are more likely to
 * use one of the sub-classes that deal with messaging systems.
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
   * A file from which the user's credentials can be read.
   * The file must yield a java.security.KeyStore, and the
   * key-store is expected to contain the user's certificate
   * chain and private key.
   */
  protected File keyStoreFile;

  /**
   * The type of key-store contained in the keyStoreFile property.
   * Types known to Java are "jks" and "pkcs12", but the latter
   * should not be used as the JRE fails to read certificate
   * chains from this type of store.
   */
  protected String keyStoreType = "jks";

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
   * @return The subject (never null).
   */
  public Subject getSsoSubject () {
    return this.ssoSubject;
  }

  /**
   * Sets the account name for single sign on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @param name The account name.
   */
  public void setSsoUsername (String name) {
    AccountName account = new AccountName(name);
    this.ssoSubject.getPrincipals().add(account);
  }

  /**
   * Retrieves the account name for single sign on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @return the account name
   */
  public String getSsoUsername () {
    Set names = this.ssoSubject.getPrincipals();
    if (names.size() == 0) {
      return null;
    }
    else {
      return ((Principal) names.iterator().next()).getName();
    }
  }

  /**
   * Sets the password for single sign on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @param word the password
   */
  public void setSsoPassword (String word) {
    this.ssoSubject.getPrivateCredentials().add(word);
  }

  /**
   * Retrieves the password for single sign on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @return the password
   */
  public String getSsoPassword () {
    Set passwords = this.ssoSubject.getPrivateCredentials(String.class);
    if (passwords.size() == 0) {
      return null;
    }
    else {
      return (String)(passwords.iterator().next());
    }
  }

  /**
   * Nominates the key-store file from which credentials can be got locally.
   *
   * @param keyStoreFile The file from which the java.security.KeyStore can be loaded.
   */
  public void setSsoKeyStore(File keyStoreFile) {
    this.keyStoreFile = keyStoreFile;
  }

  /**
   * Retrieves the user identity from the grid Subject.
   *
   * @return The first Principal in the grid subject; null if no Principals are present.
   */
  public Principal getGridPrincipal() {
    Set principals = this.gridSubject.getPrincipals();
    return (Principal)((principals.toArray())[0]);
  }

  /**
   * Uses the SSO credentials to obtain grid credentials.
   */
  public void signOn() throws Exception {

    // Set the properties controlling use of the key-store. Several properties
    // of the store are packaged as one java.util.Properties and that object is
    // set as a single property of the security handler. Note that the name of
    // the handler property is defined by AstroGrid and only works with AstroGrid's
    // sub-class of the handler.
    Properties p = new Properties();
    p.setProperty("org.apache.ws.security.crypto.merlin.file", this.keyStoreFile.getCanonicalPath());
    p.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", this.keyStoreType);
    p.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", this.getSsoPassword());
    p.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", this.getSsoUsername());

    // Create and cache a new Crypto using given properties.
    Crypto  c = CryptoFactory.getInstance("org.apache.ws.security.components.crypto.Merlin", p);
    this.gridSubject.getPrivateCredentials().add(c);
  }

}
