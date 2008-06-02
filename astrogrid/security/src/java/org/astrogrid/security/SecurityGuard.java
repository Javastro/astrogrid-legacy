package org.astrogrid.security;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import org.astrogrid.security.authorization.AccessPolicy;
import org.astrogrid.security.community.CommunityEndpointResolver;
import org.astrogrid.security.community.CommunityIvornParser;
import org.astrogrid.security.ssl.GullibleX509TrustManager;
import org.astrogrid.security.community.SsoClient;

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
public class SecurityGuard implements X509KeyManager {
  
  /**
   * The JAAS Subject for all credentials and principals.
   */
  protected Subject subject;

  protected AccessPolicy accessPolicy;

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
   * A reference to the access policy of the given guard is copied into
   * the new one.
   *
   * @param sg The source of the credentials.
   */
   public SecurityGuard (SecurityGuard sg) {
     this.subject = this.cloneSubject(sg.getSubject());
     this.accessPolicy = sg.accessPolicy;
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
   * @deprecated - Use {@link #getSubject()} instead.
   */
  public Subject getSsoSubject() {
    return this.subject;
  }
  
  /**
   * Retrieves the entire JAAS subject.
   * Provided only for backward compatibility with the workbench.
   * 
   * @return - the subject (never null).
   * @deprecated - Use {@link #getSubject()} instead.
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
    Iterator<X500Principal> i = 
        this.subject.getPrincipals(X500Principal.class).iterator();
    return (i.hasNext())? i.next(): null;
  }
  
  
  /**
   * Records the X500 distinguished name which has been authenticated.
   *
   * @param p The distinguished name.
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
    Iterator<CertPath> i = this.subject.getPublicCredentials(CertPath.class).iterator();
    if (i.hasNext()) {
      List l = i.next().getCertificates();
      X509Certificate[] chain = new X509Certificate[l.size()];
      for (int c = 0; c < chain.length; c++) {
        chain[c] = (X509Certificate)(l.get(c));
      }
      return chain;
    }
    else {
      return new X509Certificate[0];
    }
  }
  
  /**
   * Sets the certificate-chain public-credential. This is
   * expressed as an array of X.509 certificates ordered such that
   * the signature on certificate i may be checked using the public key
   * in certificate i+1.
   *
   * @param chain The chain (never null; zero-length array if no certificates).
   * @throws CertificateException If the JRE does not support X.509.
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
   * Sets the certificate-chain public-credential.
   *
   * @param chain The chain (never null; may be empty of certificates).
   */
  public void setCertificateChain(CertPath chain) {
    this.subject.getPublicCredentials().add(chain);
  }
  
  /**
   * Sets the X500Principal from the previously-set certificate chain.
   * The identity certificate - the first EEC - in the chain is located and
   * its subject is recorded as a principal. This method should be called
   * when a certificate chain is authenticated: setting the principal
   * records the authentication.
   */
  public void setX500PrincipalFromCertificateChain() {
    X509Certificate x = getIdentityCertificate();
    if (x != null) {
      this.subject.getPrincipals().add(x.getSubjectX500Principal());
    }
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
    
    // RFC 3820 defines the ProxyCertInfo extension to an X.509 certificate
    // and specifies the OID for that extension. The RFC says that all
    // proxy certificates must have the extension and no end-entity
    // certificate may have such an extension. Before RFC 3820 was written, the
    // extension had an OID in the Globus Alliance's private namespace, and
    // some PCs use this older OID. Therefore, to find the
    // identity certificate, we traverse the certificate chain and return the
    // first certificate that doesn't have an extension under either of these
    // two OIDs.
    final String ietfProxyCertInfoOid = "1.3.6.1.5.5.7.1.14";
    final String globusProxyCertInfoOid = "1.3.6.1.4.1.3536.1.222";
    X509Certificate[] chain = getCertificateChain();
    for (int i = 0; i < chain.length; i++) {
      if (chain[i].getExtensionValue(ietfProxyCertInfoOid) == null &&
          chain[i].getExtensionValue(globusProxyCertInfoOid) == null){
        return chain[i];
      }
    }
    return null;
  }
  
  /**
   * Reveals the location of the user's home space, if known.
   *
   * @return The URI for the homespace (null if not known).
   */
  public String getHomespaceLocationAsString() {
    Iterator<HomespaceLocation> homes = 
        this.subject.getPrincipals(HomespaceLocation.class).iterator();
    return (homes.hasNext())? homes.next().getName() : null;
  }
  
  /**
   * Sets the home-space location. The home-space URI is recorded as a 
   * principal of type {@link HomespaceLocation}.
   *
   * @param location The home-space URI.
   */
  public void setHomespaceLocation(String location) {
    this.subject.getPrincipals().add(new HomespaceLocation(location));
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
   * Sets the access policy that makes authorization decisions.
   */
  public void setAccessPolicy(AccessPolicy policy) {
    this.accessPolicy = policy;
  }
  
  /**
   * Makes an authorization decision based on the current access-policy.
   */
  public Map decide(Map request) throws SecurityException, 
                                        GeneralSecurityException, 
                                        Exception {
    if (this.accessPolicy == null) {
      throw new GeneralSecurityException("No access policy is loaded");
    }
    else {
      return this.accessPolicy.decide(this, request);
    }
  }
  
  /**
   * Signs a user into the IVO. This provides, cached in the SecurityGuard,
   * credentials valid for a specified duration.
   * The user and community are identified by
   * an account ivorn, e.g. ivo://gtr@uk.ac.cam.ast/community for user
   * gtr at the community service ivo://uk.ac.cam.ast/community. After signing
   * on, the subject contains (at least) two principals, one AccountIvorn and 
   * one X500Principal.
   *
   * @param account The IVORN for the user's account.
   * @param password The password, undigested and unencrypted.
   * @param lifetime The duration of validity of the credentials, in seconds.
   */
  public void signOn(String account, String password, int lifetime) throws Exception {
    
    // Sign on at the community's accounts service.
    // This adds to the subject a certificate chain and private key but
    // does nor record any principals.
    CommunityIvornParser p = new CommunityIvornParser(account);
    CommunityEndpointResolver r = 
        new CommunityEndpointResolver(p.getCommunityIvorn().toString());
    if (r.getAccounts() == null) {
      throw new Exception("No endpoint is registered for the accounts service for " + account);
    }
    SsoClient s = new SsoClient(r.getAccounts().toString());
    s.authenticate(p.getAccountName(), password, lifetime, this);
    s.home(p.getAccountName(), this);
    
    // Record the account IVORN as a principal.
    AccountIvorn i = new AccountIvorn(p.getAccountIvorn().toString());
    this.subject.getPrincipals().add(i);
  }
  
  /**
   * Configures an HTTPS connection. TLS is set as the protocol.
   * Authentication of the server is disabled. If the connection is not an
   * HTTPS connection then this method returns silently. This method must be
   * called before the connection is opened;
   *
   * @param connection The connection to be configured.
   */
  public void configureHttps(HttpURLConnection connection) {
    assert connection != null;
    HttpsURLConnection https = null;
    if (connection instanceof HttpsURLConnection) {
      https = (HttpsURLConnection) connection;
    }
    else {
      return;
    }
    
    // The connection is configured by setting up a pre-configured
    // SSLcontext, deriving from that an SSLSocket factory and imposing
    // the factory on the connection.
    try {
      SSLContext ssl = SSLContext.getInstance("TLS");
      TrustManager[] tms = {new GullibleX509TrustManager()};
      KeyManager[]   kms = {this};
      ssl.init(kms, tms, null);
      https.setSSLSocketFactory(ssl.getSocketFactory());
    }
    catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to configure HTTPS", e);
    }
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

  /**
   * Lists the aliases available for a given type of key and a given set of
   * certificate authorities (CAs). The returned aliases should be those for 
   * which keys of the specified type are available and for which there is a 
   * certificate chain leading to the root certificate of one of the specified CAs.
   * <p>
   * This implementation is kludged: it ignores the conditions and always
   * returns the alias "default" if the user is signed in. The use of a default
   * alias is correct, since a given SecurityGuard only contains the credentials
   * for one identity. However, this implementation should be extended to
   * respect the given conditions.
   *
   * @param keyType The type of key required for authentication.
   * @param cas The certificate authorities whose certificates are accepted by the service.
   * @return The aliases (empty array if user is not signed in).
   */
  public String[] getClientAliases(String keyType, Principal[] cas) {
    return (getX500Principal() == null)? new String[0] : new String[]{"default"};
  }

  /**
   * Chooses an alias for the client based on a specified key-type and a
   * specified set of credential issuers.
   */
  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
    return (getX500Principal() == null)? null : "default";
  }

  /**
   * Lists the aliases available for a given type of key and a given set of
   * certificate authorities (CAs). The returned aliases should be those for 
   * which keys of the specified type are available and for which there is a 
   * certificate chain leading to the root certificate of one of the specified CAs.
   * <p>
   * This implementation is kludged: it always returns an empty array. This
   * class is just not expected to be used in an SSL server.
   */
  public String[] getServerAliases(String string, Principal[] principal) {
    return new String[0];
  }

  public String chooseServerAlias(String string, Principal[] principal, Socket socket) {
    return null;
  }

  /**
   * Reveals the certificate chain for the given alias.
   *
   * @param alias The alias.
   * @return The chain (null if the alias is not "default").
   */
  public X509Certificate[] getCertificateChain(String alias) {
    return (alias.equals("default"))? getCertificateChain() : null;
  }

  /**
   * Reveals the private key for the given alias.
   *
   * @param alias The alias.
   * @return The key (null if the alias is not "default").
   */
  public PrivateKey getPrivateKey(String alias) {
    return (alias.equals("default"))? getPrivateKey() : null;
  }
  
}
