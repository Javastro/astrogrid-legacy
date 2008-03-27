package org.astrogrid.community.resolver ;

import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate ;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.globus.gsi.bc.BouncyCastleUtil;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.proxy.ProxyPathValidator;
import org.globus.myproxy.MyProxy;
import org.ietf.jgss.GSSCredential;

/**
 * A utility to resolve passwords into other credentials.
 *
 */
public class CommunityPasswordResolver {
  
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityPasswordResolver.class);

  /**
    * Public constructor, using the default Registry service.
    *
    */
  public CommunityPasswordResolver() {
    this.securityResolver = new SecurityServiceResolver();
    this.myProxyResolver = new CommunityMyProxyResolver();
  }

  /**
    * Public constructor, for a specific Registry service.
    * @param registry The endpoint address for our RegistryDelegate.
    *
    */
  public CommunityPasswordResolver(URL registry) {
    this.securityResolver = new SecurityServiceResolver(registry);
    this.myProxyResolver  = new CommunityMyProxyResolver(registry);
  }

  /**
   * Constructs a resolver using a given registry-delegate.
   * 
   * @param registry The registry delegate.
   */
  public CommunityPasswordResolver(RegistryService registry) {
    this.securityResolver = new SecurityServiceResolver(registry);
    this.myProxyResolver  = new CommunityMyProxyResolver(registry);
  }
  
  /**
   * Our SecurityServiceResolver.
   */
    private SecurityServiceResolver securityResolver ;
    private CommunityMyProxyResolver myProxyResolver;

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public SecurityToken checkPassword(String account, String password)
        throws RegistryException, CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityPasswordResolver.checkPassword()") ;
        log.debug("  Account : " + account) ;
        //
        // Check for null param.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Parse the account ident into an ivorn.
        CommunityIvornParser parser = new CommunityIvornParser(account) ;
        //
        // Resolve the ivorn into a SecurityServiceDelegate.
        SecurityServiceDelegate delegate = this.securityResolver.resolve(parser);
        //
        // Ask the SecurityServiceDelegate to check the token.
        return delegate.checkPassword(
            account,
            password
            ) ;
        }
 
  /**
   * Resolves an account-name and password into a set of cryptographic
   * credentials and a user alias. This implementation talks directly
   * to a MyProxy service. The credentials are transformed into a
   * java.security.PrivateKey and a java.security.cert.CertPath (i.e.
   * a certificate chain) and these are packaged and returned in
   * a JAAS Subject.
   * 
   * @param account IVOID for account.
   * @param password Plain-text password
   * @param lifetime Desired length of validity of credentials in seconds.
   * @param trustAnchors Name of directory holding trusted certificates.
   * @return The credentials.
   */
  public Subject checkPassword(String account, 
                               String password, 
                               int    lifetime,
                               String trustAnchors) 
      throws RegistryException,
             CommunityResolverException,
             CommunityIdentifierException   {

    Subject s = null;
    try {
      log.info("Logging in " + account + " at the community.");
      s = checkPasswordAtCommunity(account, password, lifetime, trustAnchors);
      log.info("Got credentials from the community.");
    }
    catch (Exception e1) {
      log.info("Failed to log in " + account + " at the community: " + e1);
      log.info("Logging in " + account + " at MyProxy");
      try {
        s = checkPasswordAtMyProxy(account, password, lifetime, trustAnchors);
      }
      catch (Exception e2) {
        log.info("Failed to log in " + account + " at MyProxy: " + e2);
        log.info("No credentials are available for " + account);
        throw new CommunityResolverException("No credentials are available for " + account);
      }
    }
    return s;
  }
  
  /**
   * Resolves an account-name and password into a set of cryptographic
   * credentials and a user alias. This implementation talks directly
   * to a MyProxy service. The credentials are transformed into a
   * java.security.PrivateKey and a java.security.cert.CertPath (i.e.
   * a certificate chain) and these are packaged and returned in
   * a JAAS Subject.
   * 
   * @param account IVOID for account.
   * @param password Plain-text password
   * @param lifetime Desired length of validity of credentials in seconds.
   * @param trustAnchors Name of directory holding trusted certificates.
   * @return The credentials.
   */
  protected Subject checkPasswordAtMyProxy(String account, 
                                           String password, 
                                           int    lifetime,
                                           String trustAnchors) 
      throws RegistryException,
             CommunityResolverException,
             CommunityIdentifierException   {
    
    // Record where the trusted certificates are kept. The MyProxy
    // class from Globus needs this information specifically attached
    // to this system property.
    log.debug("Trusted-certificates are in " + trustAnchors);
    System.setProperty("X509_CERT_DIR", trustAnchors);
    
    // Parse the account IVORN. This gives access to details of
    // both the account and the community.
    CommunityIvornParser parser = new CommunityIvornParser(account);
    
    // Get the account name from the account IVOID.
    String name = parser.getAccountName();
    log.info(name + " is trying to log in using MyProxy.");
    
    // Get a delegate for the MyProxy service associated with the account.
    MyProxy myProxy = this.myProxyResolver.resolve(parser.getIvorn());
    
    // Get credentials from the MyProxy service using jGlobus. The
    // credentials come back as a GSS object which is so generic that
    // it doesn't have accessors for the useful parts of the credentials.
    // However, the Globus implementation of the GSS interface does have
    // the right accessors.
    GlobusGSSCredentialImpl globusCred;
    try {
      GSSCredential gssCred = myProxy.get(name, password, lifetime);
      globusCred = (GlobusGSSCredentialImpl)gssCred;
    }
    catch (Exception e) {
      throw new CommunityResolverException(
          "Failed to get credentials from the MyProxy service.", e);
    }
    
    // Package and return the results.
    try {
      Subject subject = new Subject();
      subject.getPrivateCredentials().add(globusCred.getPrivateKey());
      List certList = Arrays.asList(globusCred.getCertificateChain());
      CertificateFactory factory = CertificateFactory.getInstance("X509");
      CertPath certPath = factory.generateCertPath(certList);
      subject.getPublicCredentials().add(certPath);
      recordX500Principal(subject);
      log.info(name + " has logged in using MyProxy.");
      return subject;
    }
    catch (Exception e) {
      throw new CommunityResolverException("Credentials were obtained but " +
          "the resolver failed to package them in a JAAS Subject.", e);
    }
  }
  
  /**
   * Resolves an account-name and password into a set of cryptographic
   * credentials and a user alias. This implementation talks directly
   * to a MyProxy service. The credentials are transformed into a
   * java.security.PrivateKey and a java.security.cert.CertPath (i.e.
   * a certificate chain) and these are packaged and returned in
   * a JAAS Subject.
   * 
   * @param account IVOID for account.
   * @param password Plain-text password
   * @param lifetime Desired length of validity of credentials in seconds.
   * @param trustAnchors Name of directory holding trusted certificates.
   * @return The credentials.
   */
  protected Subject checkPasswordAtCommunity(String account, 
                                             String password, 
                                             int    lifetime,
                                             String trustAnchors) 
      throws RegistryException,
             CommunityResolverException,
             CommunityIdentifierException   {
    
    // Parse the account IVORN. This gives access to details of
    // both the account and the community.
    CommunityIvornParser parser = new CommunityIvornParser(account);
    
    // Get the account name from the account IVOID.
    String userName = parser.getAccountName();
    log.info(userName + " is trying to log in using the community accounts protocol.");
    
    // Get the endpoint of the SSO service.
    // KLUDGE: hard-code the endpoint
    CommunityEndpointResolver resolver = new CommunityEndpointResolver();
    URL endpoint = 
        resolver.resolve(parser.getCommunityIvorn(), 
                         "ivo://org.astrogrid/std/Community/accounts");
    
    // Get and return the credentials.
    SsoClient client = new SsoClient(endpoint.toString());
    Subject s = client.authenticate(userName, password, lifetime, trustAnchors);
    recordX500Principal(s);
    log.info(userName + " has logged in using the community accounts protocol.");
    return s;
  }

  /**
   * Finds the user's X500Principal in the certificate chain and
   * records it. This records successful authentication.
   */
  protected void recordX500Principal(Subject subject) {
    Set s = subject.getPublicCredentials(CertPath.class);
    CertPath path = (CertPath) (s.iterator().next());
    if (path != null) {
      ProxyPathValidator v = new ProxyPathValidator();
      List l = path.getCertificates();
      X509Certificate[] a = new X509Certificate[l.size()];
      for (int i = 0; i < l.size(); i++) {
        a[i] = (X509Certificate)(l.get(i));
      }
      try {
        X509Certificate identity = BouncyCastleUtil.getIdentityCertificate(a);
        if (identity != null) {
          subject.getPrincipals().add(identity.getSubjectX500Principal());
        }
      }
      catch (CertificateException e) {
        // This shouldn't happen, given that the MyProxy and accounts clients
        // always produce proper certificate chains.
        throw new RuntimeException("Can't find the EEC in the certificate chain", e);
      }
    }
  }
}