/*$Id: VanillaNodeDelegate.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
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


import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.common.Ivorn;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.TransferInfo;

import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

/** Basic implemenrtation of NodeDelegate - no caching.
 * <B>abstract, as should not be used - as doesn't provide enough funcitonality.
 * Clients rely on caching behaviour for observer notification to work..</b>
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public abstract class VanillaNodeDelegate implements  NodeDelegate {
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(NodeDelegate.class);

    /** Construct a new VanillaNodeDelegate
     * @param fm the filemanager delegate (or real server instance!!) to operate upon.
     * @param hints set of preferences for bundling behaviour.
     * 
     */
    public VanillaNodeDelegate(FileManagerPortType fm, BundlePreferences hints) {
        super();
        this.fm = fm;
        this.hints = hints;
    }
    
    protected final FileManagerPortType fm;
    protected final BundlePreferences hints;
 
    
    /** create a full node out of the first of the list
     * subclasses can extend this to give opportunities for caching, etc.);
     * @param ns
     */
    protected  org.astrogrid.filemanager.client.FileManagerNode returnFirst(Node[] ns) {
        return new AxisNodeWrapper(ns[0],this);
    }
    
    // delegate and lift each method

    public org.astrogrid.filemanager.client.FileManagerNode addAccount(AccountIdent ident) throws RemoteException,
            DuplicateNodeFault, FileManagerFault {
        Node[] arr = this.fm.addAccount(ident, hints);
        return returnFirst(arr);
    }
    public org.astrogrid.filemanager.client.FileManagerNode addNode(NodeIvorn parentIvorn, NodeName newNodeName, NodeTypes nodeType)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        Node[] arr =  this.fm.addNode(parentIvorn, newNodeName, nodeType);
        return returnFirst(arr);
    }
    public TransferInfo appendContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.appendContent(nodeIvorn);
    }
    public org.astrogrid.filemanager.client.FileManagerNode copy(NodeIvorn node,NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        Node[] arr =  this.fm.copy(node,newParent, newNodeName, newLocation);
        return returnFirst(arr);
    }
    public void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        this.fm.copyContentToURL(nodeIvorn, url);
    }
    public void copyURLToContent(NodeIvorn nodeIvorn, URI url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        returnFirst(this.fm.copyURLToContent(nodeIvorn, url)); // note we don't want to return anything - but givinig caching a chance.
    }
    public void delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        returnFirst( this.fm.delete(nodeIvorn));
    }
    public org.astrogrid.filemanager.client.FileManagerNode getAccount(AccountIdent ident) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return returnFirst(this.fm.getAccount(ident, hints));
    }
    public Ivorn getIdentifier() throws RemoteException {
        return this.fm.getIdentifier();
    }
    public org.astrogrid.filemanager.client.FileManagerNode getNode(NodeIvorn nodeIvorn) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return returnFirst(this.fm.getNode(nodeIvorn, hints));
    }
    /** in this implementation, equivalent to {@lin #getNode}
     * @see org.astrogrid.filemanager.client.delegate.NodeDelegate#getNodeIgnoreCache(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public FileManagerNode getNodeIgnoreCache(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault, NodeNotFoundFault {
        return getNode(nodeIvorn);
    }    
    public void move(NodeIvorn node,NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        returnFirst( this.fm.move(node,newParent, newNodeName, newLocation));
    }
    public TransferInfo readContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.readContent(nodeIvorn);
    }
    public void transferCompleted(NodeIvorn nodeIvorn) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        returnFirst(this.fm.refresh(nodeIvorn, hints));
    }
    public TransferInfo writeContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.writeContent(nodeIvorn);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[VanillaNodeDelegate:");
        buffer.append(" logger: ");
        buffer.append(logger);
        buffer.append(" fm: ");
        buffer.append(fm);
        buffer.append(" hints: ");
        buffer.append(hints);
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @see org.astrogrid.filemanager.client.delegate.NodeDelegate#clearCache()
     */
    public void clearCache() {
        // does nothing in this implementation.
    }


}


/* 
$Log: VanillaNodeDelegate.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.3  2005/03/01 15:07:29  nw
close to finished now.

Revision 1.1.2.2  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/