/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClientFactory.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:57 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientFactory.java,v $
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
package org.astrogrid.filemanager.client ;

import java.net.URL;

import org.astrogrid.store.Ivorn;

import org.astrogrid.community.common.security.data.SecurityToken;

import org.astrogrid.community.resolver.CommunityTokenResolver;
import org.astrogrid.community.resolver.CommunityPasswordResolver;

import org.astrogrid.filemanager.client.exception.FileManagerLoginException;

/**
 * A factory for instances of the FileManager client interface.
 *
 */
public class FileManagerClientFactory
    {
    /**
     * Public constructor using the default Registry endpoint.
     *
     */
    public FileManagerClientFactory()
        {
        this.registry = null;
        //
        // Create our resolvers.
        tokenResolver = new CommunityTokenResolver();
        loginResolver = new CommunityPasswordResolver();
        }

    /**
     * Public constructor using a specific Registry endpoint.
     *
     */
    public FileManagerClientFactory(URL registry)
        {
        this.registry = registry;
        //
        // Create our resolvers.
        tokenResolver = new CommunityTokenResolver(
            this.registry
            );
        loginResolver = new CommunityPasswordResolver(
            this.registry
            );
        }

    /**
     * Our registry endpoint URL.
     *
     */
    private URL registry ;

    /**
     * Our Community token resolver.
     *
     */
    private CommunityTokenResolver tokenResolver ;

    /**
     * Our Community password resolver.
     *
     */
    private CommunityPasswordResolver loginResolver ;

    /**
     * Login to a Community account using acciunt the identifier and password.
     * @param account  The Community account identifier.
     * @param password The Community account password.
     * @return A FileManagerClient authenticated using the account identifier and password.
     * @throws FileManagerLoginException If the login fails.
     *
     */
    public FileManagerClient login(Ivorn account, String password)
        throws FileManagerLoginException
        {
        try {
            //
            // Validate the account and password.
// Add an Ivorn method to the community resolver.
            SecurityToken token = loginResolver.checkPassword(
                account.toString(),
                password
                );
            //
            // Create a FileManagerClient with the new token.
            return new FileManagerClientImpl(
                this.registry,
                token
                );
            }
        catch (Exception ouch)
            {
            throw new FileManagerLoginException(
                "Unable to login",
                ouch
                );
            }
        }

    /**
     * Login with a security token.
     * @param token A valid security token containing the account details and authentication.
     * @return A FileManagerClient authenticated using the security token.
     * @throws FileManagerLoginException If the unable to validate the token.
     *
     */
    public FileManagerClient login(SecurityToken token)
        throws FileManagerLoginException
        {
        try {
            //
            // Validate the security token.
            token = tokenResolver.checkToken(token);
            //
            // Create a FileManagerClient with the new token.
            return new FileManagerClientImpl(
                this.registry,
                token
                );
            }
        catch (Exception ouch)
            {
            throw new FileManagerLoginException(
                "Unable to validate token",
                ouch
                );
            }
        }

    /**
     * Login anonymously.
     * @return A FileManagerClient with no associated account.
     *
     */
    public FileManagerClient login()
        {
        //
        // Create a FileManagerClient with no authentication.
        return new FileManagerClientImpl(
            this.registry
            );
        }
    }


