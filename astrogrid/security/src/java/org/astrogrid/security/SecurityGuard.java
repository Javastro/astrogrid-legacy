package org.astrogrid.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;


/**
 * A container for security information.
 *
 * This is a Java-bean class in which the properties are various
 * credentials and/or principals for secured messaging. It is a standard way, 
 * within its package, of passing security information within the same JVM.
 *
 * Applications may use this class directly, but are more likely to
 * use one of the sub-classes that deal with messaging systems.
 *
 * "Principals" are identities that have been authenticated using
 * credentials. Until authentication succeeds, a SecurityGuard contains
 * no principals. An object representing an identity may be stored as
 * a credential before authentication and as both a credential and a 
 * principal after authentication.
 *
 * In the current implementation, the information is stored in a JAAS 
 * Subject. Special accessors are provided for certain kinds of principals
 * and credentials. A general accessor is also available to retrieve the
 * entire Subject, and this serves where dedicated accessors are not yet
 * available. However, this general accessor may be later be protected
 * against use from outside the current package; application code should
 * use the specialized accessors where they are available.
 *
 * @author Guy Rixon
 */
public class SecurityGuard {
  
  /**
   * The JAAS Subject for all credentials and principals.
   */
  protected Subject subject;

  /**
   * Constructs a SecurityGuard with empty
   * JAAS subjects.
   */
  public SecurityGuard () {
    this.subject = new Subject();
  }

  /**
   * Constructs a SecurityGuard with a
   * given JAAS subject for grid credentials.
   * No SSO credentials are set.
   */
  public SecurityGuard (Subject s) {
    this.subject = this.cloneSubject(s);
  }
  
  /**
   * Creates a SecurityGuard with credentials.
   * The credentials are copied from a given SecurityGuard.
   *
   * @param sg The source of the credentials.
   */
   public SecurityGuard (SecurityGuard sg) {
     this.subject = this.cloneSubject(sg.getSubject());
  }

  
  /**
   * Retrieves the entire JAAS subject.
   * Software outside the org.astrogrid.security package
   * should avoid this method.
   * 
   * @return - the subject (never null).
   */
  public Subject getSubject() {
    return this.subject;
  }

  /**
   * Retrieves the entire JAAS subject.
   * Provided only for backward compatibility with the workbench.
   * 
   * @return - the subject (never null).
   * @deprecated - Use {@link getSubject} instead.
   */
  public Subject getSsoSubject() {
    return this.subject;
  }
  
  /**
   * Retrieves the entire JAAS subject.
   * Provided only for backward compatibility with the workbench.
   * 
   * @return - the subject (never null).
   * @deprecated - Use {@link getSubject} instead.
   */
  public Subject getGridSubject() {
    return this.subject;
  }
  
  /**
   * Sets the account name credential for single sign-on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @param name The account name.
   */
  public void setSsoUsername (String name) {
    AccountName account = new AccountName(name);
    this.subject.getPublicCredentials().add(account);
  }

  /**
   * Retrieves the account name credential for single sign on.
   * This account name is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @return the account name
   */
  public String getSsoUsername () {
    Set names = this.subject.getPublicCredentials(AccountName.class);
    return (names.size() > 0)? ((AccountName)(names.iterator().next())).getName() : null;
  }

  /**
   * Sets the password for single sign-on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @param word the password
   */
  public void setSsoPassword (String word) {
    this.subject.getPrivateCredentials().add(word);
  }

  /**
   * Retrieves the password for single sign-on.
   * This password is used when the user first
   * signs on to the grid. It may be different to the
   * account name used in authenticating to services
   * within the grid.
   *
   * @return the password
   */
  public String getSsoPassword () {
    Set passwords = this.subject.getPrivateCredentials(String.class);
    if (passwords.size() == 0) {
      return null;
    }
    else {
      return (String)(passwords.iterator().next());
    }
  }
  
  /**
   * Retrieves the X500 distinguished name.
   *
   * @return - the name (null if not authenticated by signature).
   */
  public X500Principal getX500Principal() {
    Set principals = this.subject.getPrincipals(X500Principal.class);
    return (principals.size() > 0)? (X500Principal)(principals.iterator().next()) : null;
  }
  
  /**
   * Records the X500 distinguished name which has been authenticated.
   *
   * @return - the name (null if not authenticated by signature).
   */
  public void setX500Principal(X500Principal p) {
    this.subject.getPrincipals().add(p);
  }
  
  /**
   * Gets the certificate-chain public-credential. This is
   * expressed as an array of X.509 certificates ordered such that
   * the signature on certificate i may be checked using the public key
   * in certificate i+1.
   *
   * @return - the chain (never null; zero-length array if no certificates).
   */
  public X509Certificate[] getCertificateChain() {
    Set s = this.subject.getPublicCredentials(CertPath.class);
    CertPath p = (CertPath)(s.iterator().next());
    List l = p.getCertificates();
    X509Certificate[] chain = new X509Certificate[l.size()];
    for (int i = 0; i < chain.length; i++) {
      chain[i] = (X509Certificate)(l.get(i));
    }
    return chain;
  }
  
  /**
   * Sets the certificate-chain public-credential. This is
   * expressed as an array of X.509 certificates ordered such that
   * the signature on certificate i may be checked using the public key
   * in certificate i+1.
   *
   * @param chain - the chain (never null; zero-length array if no certificates).
   * @throws CertificateException - if the JRE does not support X.509.
   */
  public void setCertificateChain(X509Certificate[] chain) throws CertificateException {
    CertificateFactory f = CertificateFactory.getInstance("X509");
    List l = new ArrayList(chain.length);
    for (int i = 0; i < chain.length; i++) {
      assert chain[i] != null;
      l.add(chain[i]);
    }
    CertPath p = f.generateCertPath(l);
    this.subject.getPublicCredentials().add(p);
  }
  
  /**
   * Retreives the private key for signing messages.
   *
   * @return - the key (null if no key is present).
   */
  public PrivateKey getPrivateKey() {
    Set s = this.subject.getPrivateCredentials(PrivateKey.class);
    return (s.size() > 0)? (PrivateKey)(s.iterator().next()) : null;
  }
  
  /**
   * Defines the private key for signing messages. If a private key
   * was previously set, then it is replaced by this key. Setting a
   * null key removes the previous key.
   *
   * @param newKey - the new key.
   */
  public void setPrivateKey(PrivateKey newKey) {
    PrivateKey oldKey = this.getPrivateKey();
    if (oldKey != null) {
      this.subject.getPrivateCredentials().remove(oldKey);
    }
    if (newKey != null) {
      this.subject.getPrivateCredentials().add(newKey);
    }
  }
  
  /**
   * Retrieves the X.509 certificate carrying the authenticated identity.
   * If the caller has been authenticated using a chain of certificates that
   * includes limited or impersonation proxy certificates, the identity
   * certificate is the first non-proxy certificate in the chain.
   */
  public X509Certificate getIdentityCertificate() {
    Set s = this.subject.getPublicCredentials(X509Certificate.class);
    return (s.size() > 0)? (X509Certificate)(s.iterator().next()) : null;
  }
  
  /**
   * Records the X.509 certificate carrying the authenticated identity.
   * If the caller has been authenticated using a chain of certificates that
   * includes limited or impersonation proxy certificates, the identity
   * certificate is the first non-proxy certificate in the chain.
   */
  public void setIdentityCertificate(X509Certificate newCert) {
    X509Certificate oldCert = this.getIdentityCertificate();
    if (oldCert != null) {
      this.subject.getPublicCredentials().remove(oldCert);
    }
    this.subject.getPublicCredentials().add(newCert);
  }
  
  /**
   * Extracts the first Principal of a given type.
   * @param clazz - The class of principal required.
   * @return The Principal, or null if none of the requested type are present.
   */
  public Object getFirstPrincipal(Class clazz) {
    Object[] a = this.subject.getPrincipals(clazz).toArray();
    return (a.length > 0)? a[0] : null;
  }

  /**
   * Extracts the first private credential of a given type.
   * @param clazz - The class of credental required.
   * @return The credential, or null if none of the requested type are present.
   */
  public Object getFirstPrivateCredential(Class clazz) {
    Object[] a = this.subject.getPrivateCredentials(clazz).toArray();
    return (a.length > 0)? a[0] : null;
  }
  
  /**
   * Copy the contents of a JAAS Subject. A shallow cloning is performed on the
   * sets of principals and credentials. After the cloning, operations on the
   * sets in the original do not affect the sets in the copy (and vice versa);
   * however, operations on the members of those sets in the original affect
   * the copy (and vice versa). E.g., replacing a PrivateKey in the original
   * with a new object will not change the key in the copy; but writing to the
   * contents of a PrivateKey objecct in the orginal changes both original and 
   * copy.
   *
   * @param given - the subject to be copied.
   * @return - the copy.
   */
  protected Subject cloneSubject(Subject given) {
    if (given == null) {
      return null;
    }
    else {
      return new Subject(false,
                         new HashSet(given.getPrincipals()),
                         new HashSet(given.getPublicCredentials()), 
                         new HashSet(given.getPrivateCredentials()));
    }
  }
}
