/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClient.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:57 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClient.java,v $
 *   Revision 1.2  2005/01/28 10:43:57  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.3  2005/01/25 08:01:15  dave
 *   Added tests for FileManagerClientFactory ....
 *
 *   Revision 1.1.2.2  2005/01/23 06:16:09  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client ;

import org.astrogrid.store.Ivorn;

import org.astrogrid.filemanager.resolver.FileManagerResolverException ;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;

/**
 * Public interface for the FileManager client.
 * The this interface hides all of the implementation details required to connect to the Registry, Community and FileManager services.
 *
 */
public interface FileManagerClient
    {
    /**
     * Access to the root node for the registered account space.
     * @return The root node of the account space.
     * @throws NodeNotFoundException If the node does not exist.
     * @throws FileManagerIdentifierException If unable to parse the Ivorn.
     * @throws FileManagerResolverException If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException If a problem occurs when handling the request.
     *
     *
     */
    public FileManagerNode home()
        throws
            FileManagerResolverException,
            FileManagerServiceException,
            FileManagerIdentifierException,
            NodeNotFoundException;

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
            NodeNotFoundException;


    }


