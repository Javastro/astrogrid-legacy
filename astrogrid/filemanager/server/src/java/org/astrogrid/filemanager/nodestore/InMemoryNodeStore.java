/*$Id: InMemoryNodeStore.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 16-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore;

import org.astrogrid.filemanager.client.NodeMetadata;
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
import org.astrogrid.filemanager.nodestore.NodeStore.Transaction;

import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.memory.TransactionalMapWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** In memory implementaiton of node store 
 * <b>not persistent- used for testing purposes only.</b>
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public class InMemoryNodeStore implements NodeStore{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(InMemoryNodeStore.class);

    /** Construct a new InMemoryNodeStore
     * 
     */
    public InMemoryNodeStore(NodeIvornFactory fac, URI defaultLocation) {
        super();
        nodeIvornFactory = fac;
        this.defaultLocation = defaultLocation;
        bundler = new TreeBundler(new BundlePreferences(),this);
    }
    /** map of accounts */
    protected final Map accMap = new HashMap();
    /** transacitonal map of nodes */
    protected final TransactionalMapWrapper nodeMap = new TransactionalMapWrapper(new HashMap());
    /** factory to create new node identifiers */
    protected final NodeIvornFactory nodeIvornFactory;
    protected final TreeBundler bundler;
    /** default storage location for new nodes */
    protected final URI defaultLocation;

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#hasNode(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public boolean hasNode(NodeIvorn ident) throws FileManagerFault {
        return nodeMap.containsKey(ident);
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#getNode(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public Node getNode(NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault {
        Node original = (Node)nodeMap.get(ident);        
        return NodeUtils.cloneNode(original);
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#createTransaction()
     */
    public Transaction createTransaction() {
        nodeMap.startTransaction();
        return new InMemoryTransaction();
    }
    
    protected class InMemoryTransaction implements NodeStore.Transaction {

        private boolean readyToCommit = false;
        public void commit() {
            nodeMap.commitTransaction();
        }
        public void rollback() {
            nodeMap.rollbackTransaction();
        }

        public void readyToCommit() {
            readyToCommit = true;
        }

        public void commitIfReadyElseRollback() {
            if (readyToCommit) {
                commit();
            } else {
                rollback();
            }
        }
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#getNodeInTransaction(org.astrogrid.filemanager.common.nodestore.NodeStore.Transaction, org.astrogrid.filemanager.common.NodeIvorn)
     */
    public Node getNodeInTransaction(Transaction t, NodeIvorn ident) throws NodeNotFoundFault,
            FileManagerFault {
        return getNode(ident);
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#deleteNode(org.astrogrid.filemanager.common.nodestore.NodeStore.Transaction, org.astrogrid.filemanager.common.Node)
     */
    public void deleteNode(Transaction t, Node ident, Node parent) throws NodeNotFoundFault, FileManagerFault {
        NodeUtils.removeChild(parent,ident.getName());
        this.putNode(t,parent);
        nodeMap.remove(ident.getIvorn());
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#createNode(org.astrogrid.filemanager.common.nodestore.NodeStore.Transaction, org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.NodeName, org.astrogrid.filemanager.common.NodeTypes)
     */
    public Node createNode(Transaction t, Node parent, NodeName name, NodeTypes type)
            throws FileManagerFault {
        // create new node.
        NodeIvorn newIvorn = this.nodeIvornFactory.createNewNodeIvorn(); 
        Node newNode = NodeUtils.newNode(parent.getIvorn(), name, type, newIvorn,defaultLocation);
        nodeMap.put(newIvorn,NodeUtils.cloneNode(newNode));
        
        // link it into parent.
        NodeUtils.addChild(parent,name,newIvorn);
        nodeMap.put(parent.getIvorn(),NodeUtils.cloneNode(parent));
        return newNode;
        
    }
    /**
     * @throws FileManagerFault
     *  */
    public Node createRootNode(Transaction t, NodeName name, AccountIdent account) throws FileManagerFault {
        NodeIvorn newIvorn = this.nodeIvornFactory.createNewNodeIvorn(); 
        Node newNode = NodeUtils.newNode(null,name,NodeTypes.FOLDER, newIvorn,defaultLocation);
        Map m = Collections.singletonMap(NodeMetadata.HOME_SPACE,account.toString()); // record in attributes who this belongs to..
        NodeUtils.mergeAttribs(newNode,m); // add this attribute into the node.        
          
        nodeMap.put(newIvorn,NodeUtils.cloneNode(newNode));
        return newNode;
    }

    /**
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     * @see org.astrogrid.filemanager.nodestore.NodeStore#putNode(org.astrogrid.filemanager.common.nodestore.NodeStore.Transaction, org.astrogrid.filemanager.common.Node)
     */
    public void putNode(Transaction t, Node node) throws FileManagerFault, NodeNotFoundFault {
        if (! hasNode(node.getIvorn())) {
            throw new NodeNotFoundFault(node.getIvorn().toString());
        }        
        nodeMap.put(node.getIvorn(),NodeUtils.cloneNode(node));
    }
    
    public void putNewNode(Transaction t, Node node) throws FileManagerFault, DuplicateNodeFault {
        if (hasNode(node.getIvorn())) {
            throw new DuplicateNodeFault(node.getIvorn().toString());
        }        
        nodeMap.put(node.getIvorn(),NodeUtils.cloneNode(node));
    }    


    /**
     * @see org.astrogrid.filemanager.nodestore.AccountStore#hasAccount(org.astrogrid.filemanager.common.AccountIdent)
     */
    public boolean hasAccount(AccountIdent ident) throws FileManagerFault {
        return accMap.containsKey(ident);
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.AccountStore#addAccount(Transaction, org.astrogrid.filemanager.common.AccountIdent, org.astrogrid.filemanager.common.Node)
     */
    public void addAccount(Transaction t, AccountIdent ident, Node root) throws DuplicateNodeFault, FileManagerFault {
        accMap.put(ident,root.getIvorn()); // i.e. just store a reference to it.
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.AccountStore#getAccount(org.astrogrid.filemanager.common.AccountIdent)
     */
    public Node getAccount(AccountIdent ident) throws NodeNotFoundFault, FileManagerFault {
        NodeIvorn rootNode = (NodeIvorn)accMap.get(ident);
        return this.getNode(rootNode);
    }

    /** @todo - should this remove the contents of the account too?
     * @see org.astrogrid.filemanager.nodestore.AccountStore#delAccount(Transaction, org.astrogrid.filemanager.common.AccountIdent)
     */
    public void delAccount(Transaction t, AccountIdent ident) throws NodeNotFoundFault, FileManagerFault {
        accMap.remove(ident);
    }

    /** 
     * @see org.astrogrid.filemanager.nodestore.NodeStore#bundleTree(org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] bundleTree(Node node, BundlePreferences hints)  {
        return bundler.bundleTree(node,hints);
    }

  
        
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[InMemoryNodeStore:");
        buffer.append(" accMap: ");
        buffer.append(accMap);
        buffer.append(" nodeMap: ");
        buffer.append(nodeMap);
        buffer.append(" nodeIvornFactory: ");
        buffer.append(nodeIvornFactory);
        buffer.append(" bundler: ");
        buffer.append(bundler);
        buffer.append(" defaultLocation: ");
        buffer.append(defaultLocation);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: InMemoryNodeStore.java,v $
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