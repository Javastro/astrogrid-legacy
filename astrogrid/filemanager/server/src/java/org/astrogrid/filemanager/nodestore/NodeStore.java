/*$Id: NodeStore.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
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

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;

/** Interface into a transactional store of node metadata
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public interface NodeStore {
    
    /** interface to a transaction - on retreiving a node in a the transaction,
     * it cannot be retreived in other transactions - and so can only be written to in this transaction.
     *  */
    public interface Transaction {
       /**commit changes made in this transaction into the store..
     * @throws FileManagerFault*/
       public void commit() throws FileManagerFault ;
       /** rollback changes made in this transaction. 
     * @throws FileManagerFault*/
       public void rollback() throws FileManagerFault;
       
       /** call this to signal operations in transaction are complete, and we  are now ready to commit.*/
       public void readyToCommit();
      
       /** commit if {@link #readyToCommit()} has been called previously, else rollback .
        * convenient to place in <code>finally</code> block, to ensure the transaction is finished, one way or another.s
     * @throws FileManagerFault*/
       public void commitIfReadyElseRollback() throws FileManagerFault;
      
    }

        /**
         * Add a new account to our store.
         * @param t
         * @param ident
         *            The account identifier.
         * @param node
         *            The root node for the account.
         * 
         * @return A new root node for the account.
         * @throws DuplicateNodeException
         *             If the the node already exists.
         * @throws FileManagerServiceException
         *             If a problem occurs when handling the request.
         *  
         */
        public void addAccount(Transaction t, AccountIdent ident, Node root)
                throws DuplicateNodeFault, FileManagerFault;
    
    /** create a node.
     * parent needs to be in a transaction. child we be added to same transaction.
     * transaction needs to be commited for everything to become final.
     * @param parent
     * @param name
     * @param type
     * @return
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws DuplicateNodeFault
     */
    public Node createNode(Transaction t,Node parent,NodeName name,NodeTypes type)
        throws FileManagerFault, NodeNotFoundFault, DuplicateNodeFault;

    /** Create a top-level node.
     * @param t
     * @param name
     * @param account @todo
     * @param folder
     * @return
     * @throws FileManagerFault
     * @throws DuplicateNodeFault
     */
    public Node createRootNode(Transaction t, NodeName name, AccountIdent account) throws DuplicateNodeFault, FileManagerFault;
    
    /** create a new transaction object */
    public Transaction createTransaction() throws FileManagerFault;

        /**
         * Remove an account from our store.
         * @param t @todo
         * @param ident
         *            The account identifier.
         * 
         * @throws NodeNotFoundException
         *             If the node does not exist.
         * @throws FileManagerServiceException
         *             If a problem occurs when handling the request.
         *  
         */
        public void delAccount(Transaction t, AccountIdent ident) throws NodeNotFoundFault,
                FileManagerFault;
        
    /** mark a node as deleted. parent and current node need to be in same transaction,
     * transaction needs to be commited for everything to become final
     * @param t
     * @param ident node to remove
     * @param parent parent of node to remove
     * @throws NodeNotFoundFault
     * @throws FileManagerFault*/
    public void deleteNode(Transaction t,Node ident, Node parent) throws NodeNotFoundFault, FileManagerFault;

        /**
         * Get the root node for an account.
         * 
         * @param ident
         *            The account identifier.
         * @return The root node for the account.
         * @throws NodeNotFoundException
         *             If the account does not exist.
         * @throws FileManagerServiceException
         *             If a problem occurs when handling the request.
         *  
         */
        public Node getAccount(AccountIdent ident)
                throws NodeNotFoundFault, FileManagerFault;
    
    /** access a node, for read-only operation */
    public Node getNode(NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault;
    
    /** access a node, for read-write operation. will be added to the set of nodes reserved by the supplied transaction */
    public Node getNodeInTransaction(Transaction t,NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault;


        /**
         * Check if the store contains an account.
         * 
         * @param indent
         *            The account identifier.
         * @return true if the store contains the account.
         * @throws FileManagerServiceException
         *             If a problem occurs when handling the request.
         *  
         */
        public boolean hasAccount(AccountIdent ident) throws FileManagerFault;
    
        /** check that the store contains a node */
    public boolean hasNode(NodeIvorn ident) throws FileManagerFault;
    
    /** write changes in node back to store
     * 
     * @param t transaction to write changes back in (must be transaction node was read in using originally)
     * @param node node to write back to store.
     * @throws NodeNotFoundFault if record for node does not exist.
     * @throws FileManagerFault
     */
    public void putNode(Transaction t,Node node) throws NodeNotFoundFault, FileManagerFault;
    
    /** write a newly-created node out to the store 
     * 
     * @param t transaction to perform changes in
     * @param node new node
     * @throws DuplicateNodeFault if node already exists.
     * @throws FileManagerFault
     */
    public void putNewNode(Transaction t,Node node) throws DuplicateNodeFault, FileManagerFault; 
    
    /** prefetch an array of nodes, comprising children, parent, etc of the current node.
     * this method should fail silently and recover by retruning a singleton array on problems -  as it's only an optimization.
     * Hence it doesn't throw any exceptions.
     * @param node target node to prefetch a bundle around.
     * @param hints hints to prefetching mechanism.
     * @return array of one or more nodes, based on hinting. always contains <code>node</code>*/
    public Node[] bundleTree(Node node, BundlePreferences hints) ;

     
}


/* 
$Log: NodeStore.java,v $
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