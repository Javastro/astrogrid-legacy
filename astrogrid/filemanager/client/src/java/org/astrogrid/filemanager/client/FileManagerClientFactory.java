package org.astrogrid.filemanager.client;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.filemanager.resolver.NodeDelegateResolverImpl;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * A factory for instances of FileManagerClient.
 * <p>
 * Login using one of the login() methods to gain access to a {@link FileManagerClient} instance.
 * 
 * @modified nww - defined constructors in terms of each other,
 * @modified nww - preserved whole object passing into FileManagerClientImpl -
 *                   as has most of the same needs.
 * @modified nww - moved creation of final resolver into this class.
 * @modified nww - no need to hang onto registry endpoint anymore - removed.
 */
public class FileManagerClientFactory {
    /**
     * Public constructor using the default Registry endpoint and default bundling behaviour.
     *  
     */
    public FileManagerClientFactory() {
        this(new BundlePreferences());
    }
    
    /** Construct a new FileManagerClientFactory, using default registry endpoint.
     * @param prefs descirption of the prefetching to do by this client.
     */
    public FileManagerClientFactory(BundlePreferences prefs) {
        this(new NodeDelegateResolverImpl(prefs));
    }

  
    /**
     * Public constructor using a specific Registry endpoint and FileManager
     * delegate resolver.
     *  
     */
    public FileManagerClientFactory(URL registryEndpoint, NodeDelegateResolver resolver) {
        accountResolver = new CommunityAccountSpaceResolver(registryEndpoint);
        this.managerResolver = resolver;
    }

    /**
     * Public constructor using a specific FileManager delegate resolver.
     *  
     */
    public FileManagerClientFactory(NodeDelegateResolver resolver) {
    	this(resolver,new CommunityAccountSpaceResolver());
    }
    public FileManagerClientFactory(NodeDelegateResolver resolver
    		,CommunityAccountSpaceResolver accountResolver) {
        this.managerResolver = resolver;
        this.accountResolver = accountResolver;
    }

    /**
     * Our FileManager delegate resolver.
     *  
     */
    private final NodeDelegateResolver managerResolver;

    /**
     * Our Community account space resolver. - not actually used in the factory,
     * but created in same way, and required by the FileManagerCLient
     *  
     */
    private final CommunityAccountSpaceResolver accountResolver;

    /**
     * Obtains a file-manager client for an account with the given IVORN.
     * The password argument is a historical relic and is not used; this method
     * does not authenticate the user at the community.
     * 
     * @param account The Community account identifier.
     * @param password The Community account password (not used; may be null).
     * @return A FileManagerClient authenticated using the account identifier
     *               and password.
     * @throws RegistryException
     * @throws CommunityException
     * @throws URISyntaxException

     *  
     */
    public FileManagerClient login(Ivorn account, String password) throws URISyntaxException {
      if (account == null) {
        throw new IllegalArgumentException("Cannot pass in null account ivorn");
      }
      if (password == null) {
        throw new IllegalArgumentException("Cannot pass in null password");
      }
      // This is a fake token for compatibility with the old API.
      SecurityToken token = new SecurityToken(account.toString(), null);
      return new FileManagerClientImpl(this, token);
    }

    /**
     * Login with a security token.
     * The token is only used to carry the account identifier; it doesn't have
     * any value for authentication.
     * 
     * @param token A security token containing the account details.
     * @return A FileManagerClient authenticated using the security token.
     * @throws CommunityException
     * @throws RegistryException
     * @throws URISyntaxException
     *  
     */
    public FileManagerClient login(SecurityToken token) throws URISyntaxException {
      return new FileManagerClientImpl(this, token);
    }

    /**
     * Login anonymously.
     *  @todo don't like this.. but need to leave this in for now.
     * @return A FileManagerClient with no associated account - cannot access <code>home</code>
     *  
     */
    public FileManagerClient login() {
        //
        // Create a FileManagerClient with no authentication.
        return new FileManagerClientImpl(this);
    }

    protected NodeDelegateResolver getManagerResolver() {
        return this.managerResolver;
    }

    protected CommunityAccountSpaceResolver getAccountResolver() {
        return this.accountResolver;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerClientFactory:");
        buffer.append(" managerResolver: ");
        buffer.append(managerResolver);
        buffer.append(" accountResolver: ");
        buffer.append(accountResolver);
        buffer.append("]");
        return buffer.toString();
    }
}

