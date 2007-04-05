/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClientFactory.java,v $</cvs:source>
 * <cvs:author>$Author: nw $</cvs:author>
 * <cvs:date>$Date: 2007/04/05 00:03:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientFactory.java,v $
 *   Revision 1.5  2007/04/05 00:03:08  nw
 *   added a constructor to make it possible to specify which implementation of the community resolver to use.
 *
 *   Revision 1.4  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.3.2.5  2005/03/01 15:07:29  nw
 *   close to finished now.
 *
 *   Revision 1.3.2.4  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.3.2.3  2005/02/18 15:50:15  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.3.2.2  2005/02/11 14:27:52  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.3.2.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.3  2005/02/10 12:44:10  jdt
 *   Merge from dave-dev-200502010902
 *
 *   Revision 1.2.2.1  2005/02/01 16:10:51  dave
 *   Updated FileManagerClient and factory to support full mock services ..
 *
 *   Revision 1.2  2005/01/28 10:43:57  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.3  2005/01/25 08:01:15  dave
 *   Added tests for FileManagerClientFactory ....
 *
 *   Revision 1.1.2.2  2005/01/23 06:16:10  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.CommunityTokenResolver;
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
        //
        // Create our resolvers.
        tokenResolver = new CommunityTokenResolver(registryEndpoint);
        loginResolver = new CommunityPasswordResolver(registryEndpoint);
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
        //
        // Create our resolvers.
        tokenResolver = new CommunityTokenResolver();
        loginResolver = new CommunityPasswordResolver();
        this.accountResolver = accountResolver;
    }

    /**
     * Our Community token resolver.
     *  
     */
    private final CommunityTokenResolver tokenResolver;

    /**
     * Our Community password resolver.
     *  
     */
    private final CommunityPasswordResolver loginResolver;

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
     * Login to a Community account using acciunt the identifier and password.
     * 
     * @param account
     *                    The Community account identifier.
     * @param password
     *                    The Community account password.
     * @return A FileManagerClient authenticated using the account identifier
     *               and password.
     * @throws RegistryException
     * @throws CommunityException
     * @throws URISyntaxException

     *  
     */
    public FileManagerClient login(Ivorn account, String password) throws CommunityException,  RegistryException, URISyntaxException{
        if (account == null) {
            throw new IllegalArgumentException("Cannot pass in null account ivorn");
        }
        if (password == null) {
            throw new IllegalArgumentException("Cannot pass in null password");
        }
            SecurityToken token = loginResolver.checkPassword(account.toString(), password);
        return new FileManagerClientImpl(this, token);

    }

    /**
     * Login with a security token.
     * 
     * @param token
     *                    A valid security token containing the account details and
     *                    authentication.
     * @return A FileManagerClient authenticated using the security token.
     * @throws CommunityException
     * @throws RegistryException
     * @throws URISyntaxException
     *  
     */
    public FileManagerClient login(SecurityToken token) throws CommunityException,RegistryException, URISyntaxException{

            token = tokenResolver.checkToken(token);

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

    protected CommunityPasswordResolver getLoginResolver() {
        return this.loginResolver;
    }

    protected NodeDelegateResolver getManagerResolver() {
        return this.managerResolver;
    }

    protected CommunityAccountSpaceResolver getAccountResolver() {
        return this.accountResolver;
    }

    protected CommunityTokenResolver getTokenResolver() {
        return this.tokenResolver;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerClientFactory:");
        buffer.append(" tokenResolver: ");
        buffer.append(tokenResolver);
        buffer.append(" loginResolver: ");
        buffer.append(loginResolver);
        buffer.append(" managerResolver: ");
        buffer.append(managerResolver);
        buffer.append(" accountResolver: ");
        buffer.append(accountResolver);
        buffer.append("]");
        return buffer.toString();
    }
}

