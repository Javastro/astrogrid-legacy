/*$Id: NodeDelegate.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 16-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.filemanager.client.delegate;

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Ivorn;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.TransferInfo;

import org.apache.axis.types.URI;

import java.rmi.RemoteException;

/**
 * Low-level client api into the filemanager.
 * <p>
 * provides all the methods required to implement the operations underlying the {@link org.astrogrid.filemanager.client.FileManagerNode} interface,
 * plus some other methods for doing admin on the server - creating and deleting accounts for example.
 * 
 * This interface lifts the axis-generated delegate (which operates in terms of file references)
 * up to a node-based delegate (operating in terms of nodes)
 * <p>
 * Implementations are assumed to cache nodes to some degree.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public interface NodeDelegate {        
    
    // delegate and lift each method
    /** add a new account */
    public abstract org.astrogrid.filemanager.client.FileManagerNode addAccount(AccountIdent ident)
            throws RemoteException, DuplicateNodeFault, FileManagerFault;

    /** add a new node

     */
    public abstract org.astrogrid.filemanager.client.FileManagerNode addNode(NodeIvorn parentIvorn, NodeName newNodeName, NodeTypes nodeType)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault;
    /** append content to the node
     * 
     * @param nodeIvorn node to append to
     * @return details of how to write content to the storage for this node.
     * @throws RemoteException
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     */
    public abstract TransferInfo appendContent(NodeIvorn nodeIvorn) throws RemoteException,
            FileManagerFault, NodeNotFoundFault;
    /** make a copy of this node */
    public abstract org.astrogrid.filemanager.client.FileManagerNode copy(NodeIvorn node,NodeIvorn newParent, NodeName newNodeName, URI  newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault;
   /** export the content of a nodel to an external resource */
    public abstract void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault;
   /** import an external resource into this nodes' content */
    public abstract void copyURLToContent(NodeIvorn nodeIvorn, URI url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault;
  /** delete a node */
    public abstract void delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault;
  /** get an account */
    public abstract org.astrogrid.filemanager.client.FileManagerNode getAccount(AccountIdent ident)
            throws RemoteException, FileManagerFault, NodeNotFoundFault;
  /** get the ivorn (registry key) for the remote filemanager this delegate is connected to */
    public abstract Ivorn getIdentifier() throws RemoteException;

    /** retreive a node from the server - possibly using cache.*/
    public abstract org.astrogrid.filemanager.client.FileManagerNode getNode(NodeIvorn nodeIvorn)
            throws RemoteException, FileManagerFault, NodeNotFoundFault;
    
    /** definately retreive a node from the server - ignore any previously cached versions */
    public abstract org.astrogrid.filemanager.client.FileManagerNode getNodeIgnoreCache(NodeIvorn nodeIvorn)
    throws RemoteException, FileManagerFault, NodeNotFoundFault;
    /** move a node */
    public abstract void move(NodeIvorn node,NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault;
 /** read contents of a node
  * 
  * @param nodeIvorn node to read
  * @return details of how to access the node contents
  * @throws RemoteException
  * @throws FileManagerFault
  * @throws NodeNotFoundFault
  */
    public abstract TransferInfo readContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault;
  /** notify the server that a transfer of data to a node has completed, so it can update it's records. */
    public abstract void  transferCompleted(NodeIvorn nodeIvorn)
            throws RemoteException, FileManagerFault, NodeNotFoundFault;
/** write data to a node 
 * 
 * @param nodeIvorn node to write to
 * @return details of how to write to the node contents
 * @throws RemoteException
 * @throws FileManagerFault
 * @throws NodeNotFoundFault
 */
    public abstract TransferInfo writeContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault;
    
    /** if this node delegate is doing caching for optimization, clear the cache */
    public abstract void clearCache();
}

/* 
 $Log: NodeDelegate.java,v $
 Revision 1.2  2005/03/11 13:37:05  clq2
 new filemanager merged with filemanager-nww-jdt-903-943

 Revision 1.1.2.3  2005/03/01 15:07:28  nw
 close to finished now.

 Revision 1.1.2.2  2005/02/27 23:03:12  nw
 first cut of talking to filestore

 Revision 1.1.2.1  2005/02/18 15:50:14  nw
 lots of changes.
 introduced new schema-driven soap binding, got soap-based unit tests
 working again (still some failures)
 
 */