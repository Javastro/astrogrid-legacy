/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityPasswordResolver.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/04/06 17:44:25 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPasswordResolver.java,v $
 *   Revision 1.6  2006/04/06 17:44:25  clq2
 *   wb-gtr-1537.
 *
 *   Revision 1.5.140.3  2006/03/06 17:29:38  gtr
 *   I changed it to return a JAAS subject instead of a CredentialStore.
 *
 *   Revision 1.5.140.2  2006/03/02 19:25:08  gtr
 *   Various fixes after tests with the workbench. Login in via my Proxy now works.
 *
 *   Revision 1.5.140.1  2006/02/28 14:48:35  gtr
 *   This supports access to cryptographic credentials via MyProxy.
 *
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.32.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URI;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.util.Arrays;
import java.util.List;
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
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
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
    public CommunityPasswordResolver()
        {
        resolver = new SecurityServiceResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public CommunityPasswordResolver(URL registry)
        {
        resolver = new SecurityServiceResolver(registry) ;
        }

    /**
     * Our SecurityServiceResolver resolver.
     *
     */
    private SecurityServiceResolver resolver ;

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
        SecurityServiceDelegate delegate = resolver.resolve(parser) ;
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
    
    // Get a delegate for the MyProxy service associated with the account.
    CommunityMyProxyResolver resolver = new CommunityMyProxyResolver();
    MyProxy myProxy = resolver.resolve(parser.getIvorn());
    
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
      return subject;
    }
    catch (Exception e) {
      throw new CommunityResolverException("Credentials were obtained but " +
          "the resolver failed to package them in a JAAS Subject.", e);
    }
  }

}