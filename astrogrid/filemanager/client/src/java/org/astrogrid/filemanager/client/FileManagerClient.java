/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClient.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClient.java,v $
 *   Revision 1.3  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.2.4.5  2005/03/01 15:07:29  nw
 *   close to finished now.
 *
 *   Revision 1.2.4.4  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.2.4.3  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.2.4.2  2005/02/11 14:27:52  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.2.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
package org.astrogrid.filemanager.client;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * Public interface for the FileManager client. The this interface hides all of
 * the implementation details required to connect to the Registry, Community and
 * FileManager services.
 *  
 */
public interface FileManagerClient {
    /**
     * Access to the root node for the registered account space.
     * 
     * @return The root node of the account space.
     * @throws RegistryException
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *             If the node does not exist.
     * @throws FileManagerIdentifierException
     *             If unable to parse the Ivorn.
     * @throws FileManagerResolverException
     *             If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     * 
     *  
     */
    public FileManagerNode home() throws FileManagerFault, NodeNotFoundFault, RegistryException, CommunityException, RemoteException, URISyntaxException;
    /**
     * Access to a node in a file manager service.
     * 
     * @param ivorn
     *            The identifier for the node - in homespace form (i.e. <code>ivo:// <i>community</i> / <i>user</i> # <i>path in homespace</i></code>)
     *    or  in nodeIvorn form (i.e. <code>ivo://<i>authority</i> / <i>service</i> # <i>node-number</i></code>) 
     * @return The FileManagerNode for the Ivorn.
     * @throws RegistryException
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *             sIf the node does not exist.
     * @throws FileManagerIdentifierException
     *             If unable to parse the Ivorn.
     * @throws FileManagerResolverException
     *             If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode node(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RegistryException, CommunityException, RemoteException, URISyntaxException;
    
    /**
     * Access to a node in a file manager service.
     * 
     * @param ivorn
     *            The identifier for the node - in nodeIvorn form (i.e. <code>ivo://<i>authority</i> / <i>service</i> # <i>node-number</i></code>) 
     * @return The FileManagerNode for the Ivorn.
     * @throws RegistryException
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *             sIf the node does not exist.
     * @throws FileManagerIdentifierException
     *             If unable to parse the Ivorn.
     * @throws FileManagerResolverException
     *             If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode node(NodeIvorn ivorn)throws FileManagerFault, NodeNotFoundFault, RegistryException, CommunityException, RemoteException, URISyntaxException;
    
    
    /**
     *  check whether a resource exists.
     * @param ivorn ivorn of resource to check for.
     * @return returns type of this resource, or null if not present.
     * 
     * @throws FileManagerFault
     * @throws RegistryException
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     */
    public NodeTypes exists(Ivorn ivorn)  throws FileManagerFault, RegistryException, CommunityException, RemoteException, URISyntaxException;
    
    /** create a new folder at this resource location
     *  will also create any required parent folders.
     * @param ivorn ivorn of the folder to create.
     * @return node for this newly created folder.
     * @throws FileManagerFault
     * @throws DuplicateNodeFault if folder already exists.
     * @throws RegistryException 
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     */
    public FileManagerNode createFolder(Ivorn ivorn)  throws FileManagerFault, DuplicateNodeFault,RegistryException, CommunityException, RemoteException, URISyntaxException;
    
    /** creae a new file at this resource location
     * will also create any required parent folders.
     * 
     * @param ivorn ivorn of the file to create.
     * @return node for this newly created file.
     * @throws FileManagerFault
     * @throws DuplicateNodeFault if file already exists.
     * @throws RegistryException
     * @throws CommunityException
     * @throws RemoteException
     * @throws URISyntaxException
     */
    public FileManagerNode createFile(Ivorn ivorn)  throws FileManagerFault, DuplicateNodeFault, RegistryException, CommunityException, RemoteException, URISyntaxException;
    

}

