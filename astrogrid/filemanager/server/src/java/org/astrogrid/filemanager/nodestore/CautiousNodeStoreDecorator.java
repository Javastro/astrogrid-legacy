/*$Id: CautiousNodeStoreDecorator.java,v 1.3 2005/04/28 20:42:04 clq2 Exp $
 * Created on 17-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore;

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.NodeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** A Decorator for another node store that checks all parameters and preconditions, and logs method entry and exit.
 * writing this as a decorator means that the implementations themsevles can be kept quite simple.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2005
 *
 */
public class CautiousNodeStoreDecorator implements NodeStore {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CautiousNodeStoreDecorator.class);

    /** Construct a new CautiousNodeStoreDecorator
     * 
     */
    public CautiousNodeStoreDecorator(NodeStore ns) {
        super();
        this.ns = ns;
    }
    
    public Node createNode(Transaction t, Node parent, NodeName name, NodeTypes type)
            throws FileManagerFault, NodeNotFoundFault, DuplicateNodeFault {
        if (logger.isDebugEnabled()) {
            logger.debug("createNode(t = " + t + ", parent = " + parent + ", name = " + name
                    + ", type = " + type + ") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        if (name == null) {
            throw new IllegalArgumentException("Null node name");
        }
        if (type == null) {
            throw new IllegalArgumentException("Null node type");
        }
        
        if (NodeUtils.findChild(parent,name) != null) {
            throw new DuplicateNodeFault(name + " already exists");
        }

        
        Node returnNode = this.ns.createNode(t, parent, name, type);
        if (logger.isDebugEnabled()) {
            logger.debug("createNode() - end");
        }
        return returnNode;
    }
    public Transaction createTransaction() throws FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("createTransaction() - start");
        }

        Transaction returnTransaction = this.ns.createTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug("createTransaction() - end");
        }
        return returnTransaction;
    }
    public void deleteNode(final Transaction t, final Node ident, final Node parent) throws NodeNotFoundFault,
            FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("deleteNode(t = " + t + ", ident = " + ident + ", parent = " + parent
                    + ") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        if (ident == null) {
            throw new IllegalArgumentException("Null node");
        }
        if (NodeUtils.findChild(parent,ident.getName()) == null || !ident.getParent().equals(parent.getIvorn())){
            throw new FileManagerFault("the node to be deleted does not have this parent");            
        }
     
        this.ns.deleteNode(t, ident, parent);

        if (logger.isDebugEnabled()) {
            logger.debug("deleteNode() - end");
        }
    }
    public Node getNode(NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("getNode(ident = " + ident + ") - start");
        }

        if (ident == null) {
            throw new IllegalArgumentException("Null node identifier");
        }        
        if (!ns.hasNode(ident)) {
            throw new NodeNotFoundFault("node not found");
        }        
        Node returnNode = this.ns.getNode(ident);
        if (logger.isDebugEnabled()) {
            logger.debug("getNode() - end");
        }
        return returnNode;
    }
    public Node getNodeInTransaction(Transaction t, NodeIvorn ident) throws NodeNotFoundFault,
            FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("getNodeInTransaction(t = " + t + ", ident = " + ident + ") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (ident == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        if (!ns.hasNode(ident)) {
            throw new NodeNotFoundFault("node not found");
        }           
        Node returnNode = this.ns.getNodeInTransaction(t, ident);
        if (logger.isDebugEnabled()) {
            logger.debug("getNodeInTransaction() - end");
        }
        return returnNode;
    }
    public boolean hasNode(NodeIvorn ident) throws FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("hasNode(ident = " + ident + ") - start");
        }

        if (ident == null) {
            throw new IllegalArgumentException("Null node identifier");
        }        
        boolean returnboolean = this.ns.hasNode(ident);
        if (logger.isDebugEnabled()) {
            logger.debug("hasNode() - end");
        }
        return returnboolean;
    }
    public void putNode(Transaction t, Node node) throws NodeNotFoundFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("putNode(t = " + t + ", node = " + node + ") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (node == null) {
            throw new IllegalArgumentException("Null node identifier");
        }        
        if (!ns.hasNode(node.getIvorn())) {
            throw new NodeNotFoundFault("node not found");
        }               
        this.ns.putNode(t, node);

        if (logger.isDebugEnabled()) {
            logger.debug("putNode() - end");
        }
    }
    public void putNewNode(Transaction t, Node node) throws DuplicateNodeFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("putNewNode(t = " + t + ", node = " + node + ") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (node == null) {
            throw new IllegalArgumentException("Null node identifier");
        }        
        if (ns.hasNode(node.getIvorn())) {
            throw new DuplicateNodeFault(node.getIvorn().toString());
        }               
        this.ns.putNewNode(t, node);

        if (logger.isDebugEnabled()) {
            logger.debug("putNewNode() - end");
        }
    }
    protected final  NodeStore ns;
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CautiousNodeStoreDecorator:");
        buffer.append(ns.toString());
        buffer.append("]");
        return buffer.toString();
    }


    public Node createRootNode(Transaction t, NodeName name, AccountIdent account) throws DuplicateNodeFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("createRootNode(t = " + t + ", name = " + name + ", account = " + account +") - start");
        }

        if (t == null) {
            throw new IllegalArgumentException("Null Transaction");
        }
        if (name == null) {
            throw new IllegalArgumentException("Null node name");
        }      
        if (account == null) {
            throw new IllegalArgumentException("Null account");
        }
        //@todo check for existing with same node...if (ns.hasNode(new No)
        Node returnNode = this.ns.createRootNode(t, name, account);
        if (logger.isDebugEnabled()) {
            logger.debug("createRootNode() - end");
        }
        return returnNode;        
    }

    public void addAccount(Transaction t, AccountIdent ident, Node root)
            throws DuplicateNodeFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("addAccount(ident = " + ident + ", root = " + root + ") - start");
        }

        if (null == ident) {
            throw new IllegalArgumentException("Null account identifier");
        }
        if (null == root) {
            throw new IllegalArgumentException("Null account node");
        }
        if (ns.hasAccount(ident)) {
            throw new DuplicateNodeFault("Account " + ident  + " already exists");
        }        
        this.ns.addAccount(t, ident, root);

        if (logger.isDebugEnabled()) {
            logger.debug("addAccount() - end");
        }
    }
    public void delAccount(Transaction t, AccountIdent ident) throws NodeNotFoundFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("delAccount(ident = " + ident + ") - start");
        }

        if (ident == null) {
            throw new IllegalArgumentException("Null account identifier");            
        }
        if (!ns.hasAccount(ident)) {
            throw new NodeNotFoundFault("Account not found");
        }                
        this.ns.delAccount(t, ident);

        if (logger.isDebugEnabled()) {
            logger.debug("delAccount() - end");
        }
    }
    public Node getAccount(AccountIdent ident) throws NodeNotFoundFault,
            FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("getAccount(ident = " + ident + ") - start");
        }

        if (ident == null) {
            throw new IllegalArgumentException("Null account identifier");            
        }
        if (!ns.hasAccount(ident)) {
            throw new NodeNotFoundFault("Account not found");
        }        
        Node returnNode = this.ns.getAccount(ident);
        if (logger.isDebugEnabled()) {
            logger.debug("getAccount() - end");
        }
        return returnNode;
    }
    public boolean hasAccount(AccountIdent ident) throws FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("hasAccount(ident = " + ident + ") - start");
        }

        boolean returnboolean = this.ns.hasAccount(ident);
        if (logger.isDebugEnabled()) {
            logger.debug("hasAccount() - end");
        }
        return returnboolean;
    }

    /**
     * @throws NodeNotFoundFault, FileManagerFault
     * @see org.astrogrid.filemanager.nodestore.NodeStore#bundleTree(org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] bundleTree(Node node, BundlePreferences hints)  {
        if (node == null) {
            throw new IllegalArgumentException("Null node");
        }
        try {
        if (!ns.hasNode(node.getIvorn())) {
            logger.warn("Node not foound");
            return new Node[]{node};
        }
        } catch (FileManagerFault e) {
            logger.warn("Failed to hasNode",e);
            return new Node[]{node};
        }
        return ns.bundleTree(node,hints);
    }    
}


/* 
$Log: CautiousNodeStoreDecorator.java,v $
Revision 1.3  2005/04/28 20:42:04  clq2
1035

Revision 1.2.22.1  2005/04/27 11:15:25  nw
fixed delete bug - a one-liner.

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:35  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/