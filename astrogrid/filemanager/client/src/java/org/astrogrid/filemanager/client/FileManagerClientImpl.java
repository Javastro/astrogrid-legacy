/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClientImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 12:44:10 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientImpl.java,v $
 *   Revision 1.3  2005/02/10 12:44:10  jdt
 *   Merge from dave-dev-200502010902
 *
 *   Revision 1.2.2.1  2005/02/01 16:10:52  dave
 *   Updated FileManagerClient and factory to support full mock services ..
 *
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.3  2005/01/26 09:51:38  dave
 *   Fixed bug in client node resolver ...
 *
 *   Revision 1.1.2.2  2005/01/25 08:01:16  dave
 *   Added tests for FileManagerClientFactory ....
 *
 *   Revision 1.1.2.1  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import java.net.URL;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn;

import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.CommunityTokenResolver;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;

import org.astrogrid.filemanager.client.delegate.FileManagerDelegate;
import org.astrogrid.filemanager.client.exception.FileManagerLoginException;

import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl;

/**
 * Implementation of the FileManager client interface.
 *
 */
public class FileManagerClientImpl
    implements FileManagerClient
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerClientImpl.class);

    /**
     * Protected constructor, using the default registry.
     *
     */
    protected FileManagerClientImpl()
        {
        this(
            null,
            null
            );
        }

    /**
     * Protected constructor, with a specific registry endpoint.
     * @param registry The registry endpoint URL.
     *
     */
    protected FileManagerClientImpl(URL registry)
        {
        this(
            registry,
            null,
			null
            );
        }

    /**
     * Protected constructor, with a security token.
     * @param token A valid security token.
     *
     */
    protected FileManagerClientImpl(SecurityToken token)
        {
        this(
            null,
            token,
            null
            );
        }

    /**
     * Protected constructor, with a specific registry endpoint and security token.
     * @param registry The registry endpoint URL.
     * @param token    A valid security token.
     *
     */
    protected FileManagerClientImpl(URL registry, SecurityToken token)
        {
        this(
            registry,
            token,
            null
            );
		}

    /**
     * Protected constructor, with a specific registry endpoint and security token.
     * @param registry The registry endpoint URL.
     * @param token    A valid security token.
     * @param resolver The FileManagerDelegateResolver to use.
     *
     */
    protected FileManagerClientImpl(URL registry, SecurityToken token, FileManagerDelegateResolver resolver)
        {
        this.token    = token ;
        this.registry = registry ;
        //
        // Create our token resolver.
        if (null != registry)
            {
            tokenResolver = new CommunityTokenResolver(
                registry
                );
            }
        else {
            tokenResolver = new CommunityTokenResolver();
            }
        //
        // Create our account home space resolver.
        if (null != registry)
            {
            accountResolver = new CommunityAccountSpaceResolver(
                registry
                );
            }
        else {
            accountResolver = new CommunityAccountSpaceResolver();
            }
        //
        // Create our filemanager resolver.
		if (null != resolver)
			{
			managerResolver = resolver ;
			}
		else {
	        if (null != registry)
	            {
	            managerResolver = new FileManagerDelegateResolverImpl(
	                registry
	                );
	            }
	        else {
	            managerResolver = new FileManagerDelegateResolverImpl();
	            }
			}
        }

    /**
     * Our local registry endpoint.
     *
     */
    private URL registry ;

    /**
     * Our Community token resolver.
     *
     */
    private CommunityTokenResolver tokenResolver ;

    /**
     * Our Community account space resolver.
     *
     */
    private CommunityAccountSpaceResolver accountResolver ;

    /**
     * Our filemanager resolver.
     *
     */
    private FileManagerDelegateResolver managerResolver ;

    /**
     * Our current security token.
     *
     */
    private SecurityToken token ;

    /**
     * Split our security token to create a new one.
     * @return A new security token.
     *
     */
    protected SecurityToken token()
        throws FileManagerLoginException
        {
        log.debug("FileManagerClientImpl.token()");
        log.debug("  Token : " + ((null != token) ? token.toString() : "null"));
        //
        // If we have a current token.
        if (null != token)
            {
            try {
                //
                // Split our current token.
                Object[] array = tokenResolver.splitToken(
                    this.token,
                    2
                    );
                //
                // Keep one.
                this.token = (SecurityToken) array[0];
                //
                // Return the other.
                return (SecurityToken) array[1];
                }
            catch (Exception ouch)
                {
//                this.token = null ;
                throw new FileManagerLoginException(
                    "Unable to validate token",
                    ouch
                    );
                }
            }
        //
        // If we don't have a token.
        else {
            throw new FileManagerLoginException(
                "No security token"
                );
            }
        }

    /**
     * Our map of FileManager delegates, indexed by service ivorn.
     *
     */
    private Map delegates = new HashMap();

    /**
     * Resolve a FileManager Ivorn into a FileManager node.
     * @param ivorn The target ivorn.
     * @param retry True to allow recursive lookup via community account.
     * @return A FileManagerNode for the target ivorn.
     * @throws NodeNotFoundException If unable to find the node.
     * @throws FileManagerIdentifierException If unable to parse the ivorn.
     * @throws FileManagerResolverException If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    private FileManagerNode resolveNode(Ivorn ivorn, boolean retry)
        throws
            NodeNotFoundException,
            FileManagerResolverException,
            FileManagerIdentifierException,
            FileManagerServiceException
        {
        log.debug("FileManagerClientImpl.resolveNode(Ivorn)");
        log.debug("  Ivorn : " + ivorn.toString());
        //
        // Parse the ivorn as a FileManager ivorn.
        FileManagerIvornParser parser =
            new FileManagerIvornParser(
                ivorn
                );
        log.debug("  Ident : " + parser.getServiceIdent());
        //
        // Check if we already have a delegate in our map.
        if (delegates.containsKey(parser.getServiceIdent()))
            {
            log.debug("Found cached manager delegate ....");
            //
            // Get the delegate from our map.
            FileManagerDelegate delegate =
                (FileManagerDelegate) delegates.get(
                    parser.getServiceIdent()
                    );
            //
            // Call the delegate to resolve the node.
            return delegate.getNode(
                ivorn
                ) ;
            }
        //
        // If we don't have a delegate for this service.
        else {
            log.debug("Unknown ivorn, resolving ....");
            //
            // Try to resolve the ivorn into a FileManager.
            try {
                log.debug("Trying FileManager resolver ....");
                //
                // Resolve the manager delegate.
                FileManagerDelegate delegate =
                    managerResolver.resolve(
                        parser.getServiceIvorn()
                        );
                //
                // Add it to our map.
                delegates.put(
                    parser.getServiceIdent(),
                    delegate
                    );
                //
                // Call the delegate to resolve the node.
                return delegate.getNode(
                    ivorn
                    ) ;
                }
            //
            // If we failed to resolve the ivorn to a FileManager.
            catch(Exception ouch)
                {
                log.debug("FileManager resolver failed");
                log.debug("  Exception : " + ouch.toString());
                log.debug("  Message   : " + ouch.getMessage());
                //
                // If we can retry via community.
                if (retry)
                    {
                    log.debug("Trying CommunityHome resolver ....");
                    //
                    // Try to resolve via the community account home.
                    return resolveNode(
                        resolveHome(
                            ivorn
                            ),
                        false
                        ) ;
                    }
                //
                // If we can't retry.
                else {
                    throw new FileManagerResolverException(
                        "Unable to resolve delegate for " + ivorn.toString()
                        );
                    }
                }
            }
        }

    /**
     * Resolve a Community Ivorn into a FileManager Ivorn.
     * @param ivorn The target ivorn.
     * @return A FileManager Ivorn for the account home.
     * @throws FileManagerIdentifierException If unable to parse the ivorn.
     * @throws FileManagerResolverException If unable to resolve the ivorn into a community account.
     *
     */
    private Ivorn resolveHome(Ivorn ivorn)
        throws
            FileManagerResolverException,
            FileManagerIdentifierException
        {
        log.debug("FileManagerClientImpl.resolveHome(Ivorn)");
        log.debug("  Ivorn   : " + ivorn.toString());
        //
        // Try to resolve the ivorn as a community ivorn.
        try {
            //
            // Parse the ivorn as a Community ivorn.
            CommunityIvornParser parser =
                new CommunityIvornParser(
                    ivorn
                    );
            log.debug("  Account : " + parser.getAccountIdent());

            //
            // Check if we already have a delegate for this account ?
            //

            //
            // Resolve the home ivorn.
            Ivorn home = 
                accountResolver.resolve(
                    parser.getAccountIvorn()
                    );
            //
            // Store the home in our cache ?
            //

            //
            // Build a new FileManager ivorn.
            FileManagerIvornFactory factory = new FileManagerIvornFactory();
            return factory.ivorn(
                home,
                parser.getRemainder()
                );
            }
        catch(Exception ouch)
            {
            log.debug("CommunityHome resolver failed");
            log.debug("  Exception : " + ouch.toString());
            log.debug("  Message   : " + ouch.getMessage());
            throw new FileManagerResolverException(
                "Unable to resolve account for " + ivorn.toString()
                );
            }
        }

    /**
     * The Ivorn for our account home.
     *
     */
    private Ivorn homeIvorn ;

    /**
     * The node for our account home.
     *
     */
    private FileManagerNode homeNode ;

    /**
     * Access to the root node for the registered account space.
     * @return The root node of the account space.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerIdentifierException If unable to parse the Ivorn.
     * @throws FileManagerResolverException If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode home()
        throws
            NodeNotFoundException,
            FileManagerResolverException,
            FileManagerServiceException,
            FileManagerIdentifierException
        {
        log.debug("FileManagerClientImpl.home()");
        if (null != homeNode)
            {
            log.debug("Already found home, returning existing node");
            return homeNode ;
            }
        else {
            log.debug("Not found home yet, resolving ....");
            if (null != homeIvorn)
                {
                log.debug("Already found ivorn, resolving node");
                homeNode = node(
                    homeIvorn
                    );
                return homeNode ;
                }
            else {
                log.debug("Not found ivorn yet, resolving ....");
                if (null != token)
                    {
                    try {
                        log.debug("Already found token, resolving ivorn");
                        homeIvorn = new Ivorn(
                            token.getAccount()
                            );
                        }
                    catch (Exception ouch)
                        {
                        throw new FileManagerIdentifierException(
                            "Unable to resolve account ivorn from token"
                            );
                        }
                    log.debug("Found ivorn, resolving node");
                    homeNode = node(
                        homeIvorn
                        );
                    return homeNode ;
                    }
                else {
                    throw new NodeNotFoundException(
                        "Not logged in"
                        );
                    }
                }
            }
        }

    /**
     * Access to a node in a file manager service.
     * @param ivorn The identifier for the node.
     * @return The FileManagerNode for the Ivorn.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerIdentifierException If unable to parse the Ivorn.
     * @throws FileManagerResolverException If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     */
    public FileManagerNode node(Ivorn ivorn)
        throws
            FileManagerResolverException,
            FileManagerServiceException,
            FileManagerIdentifierException,
            NodeNotFoundException
        {
        log.debug("FileManagerClientImpl.node(Ivorn)");
        log.debug("  Ivorn : " + ivorn.toString());
        //
        // Resolve the node ...
        return resolveNode(
            ivorn,
            true
            ) ;
        }
    }


