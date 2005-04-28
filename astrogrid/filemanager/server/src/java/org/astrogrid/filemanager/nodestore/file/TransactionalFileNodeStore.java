/*$Id: TransactionalFileNodeStore.java,v 1.4 2005/04/28 21:38:06 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore.file;

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.nodestore.NodeIvornFactory;

import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.file.ResourceManagerSystemException;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/** Transactional implementation of a file-persistent node store.
 * <p>
 * Inherits business logic from {@link FileNodeStore}, to which this class adds the 'FileResourceManager' from <tt>commons-transactions</tt>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
public class TransactionalFileNodeStore extends FileNodeStore {
    /** default transactio timeout is 10 seconds */
    private static final int DEFAULT_TRANSACTION_TIMEOUT = 10 * 1000;
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TransactionalFileNodeStore.class);

    /** Construct a new TransactionalFileNodeStore
     * @param fac factory for new identifiers.
     * @param defaultLocation default storage location for new nodes.
     * @param nodeDir directory on local disk where persisteed nodes are stored.
     * @param workDir directory on local disk where transactions, etc are performed.
     * @throws ResourceManagerSystemException if someting goes badly wrong.
     */
    public TransactionalFileNodeStore(NodeIvornFactory fac, URI defaultLocation,File nodeDir, File workDir) throws ResourceManagerSystemException {
        super(fac, defaultLocation,nodeDir);
        FileNodeStore.checkStorage("working dir",workDir);
        manager = new FileResourceManager(nodeDir.toString(),workDir.toString(),true,new CommonsLoggingFacade(logger));
        manager.setDefaultTransactionTimeout(DEFAULT_TRANSACTION_TIMEOUT);        
        manager.start();
    }
    
    protected final FileResourceManager manager;
    
  
    
    

    public Transaction createTransaction() throws FileManagerFault {
        try {
            return new ResourceManagerTransaction();
        } catch (ResourceManagerException e) {
            FileManagerFault f = new FileManagerFault("Could not create transaction");
            f.setFaultReason(e.getMessage());
            throw f;
        }
    }
    /** implementation of the transaction interface on top of the features provided by commons-transactions FileResourceManager 
     * @todo review what the best isolation level is - currently set to READ_COMMITTED
     * */
    class ResourceManagerTransaction implements Transaction {
            public ResourceManagerTransaction() throws ResourceManagerException {
                txid = manager.generatedUniqueTxId();
                manager.startTransaction(txid);                
                manager.setIsolationLevel(txid,ResourceManager.ISOLATION_LEVEL_READ_COMMITTED);
            }
            protected final String txid;
            protected boolean ready = false;

            public void commit()  throws FileManagerFault{
                try {
                manager.commitTransaction(txid);
                } catch (ResourceManagerException e) {
                    FileManagerFault f = new FileManagerFault("Could not commit transaction");
                    f.setFaultReason(e.getMessage());
                    throw f;
                }                
            }

            public void rollback() throws FileManagerFault {
                try {
                manager.rollbackTransaction(txid);
                } catch (ResourceManagerException e) {
                    FileManagerFault f = new FileManagerFault("Could not rollback transaction");
                    f.setFaultReason(e.getMessage());
                    throw f;
                }                
            }

            public void readyToCommit() {
                ready = true;
            }

            public void commitIfReadyElseRollback() throws FileManagerFault{
                if (ready) {
                    commit();
                } else {
                    rollback();
                }
            }
        };
        
        
        /** for implementing createAccount */
    protected Writer openAccountWriter(Transaction t, AccountIdent ident) throws Exception {
        Object id = getTxId(t);
        manager.createResource(id,ident);
        return new OutputStreamWriter(manager.writeResource(id,ident));
    }
    
    
    public void delAccount(Transaction t, AccountIdent ident) throws NodeNotFoundFault,
            FileManagerFault {
        try {
        Object id = getTxId(t);
        if (!manager.resourceExists(t,ident)) {
            throw new NodeNotFoundFault("Account " + ident +" doesn't exist");
        }
        manager.deleteResource(id,ident);
        } catch (ResourceManagerException e) {
            logger.warn("Failed to delete account " + ident);
            throw new FileManagerFault("Failed to delete account" + ident);
        }
    }
    
    
    protected void doDeleteNode(Transaction t, Node node) throws FileManagerFault {
        try {
            manager.deleteResource(t,node.getIvorn());
        } catch (ResourceManagerException e) {
            logger.warn("could node delete record for " + node);
            FileManagerFault f = new FileManagerFault("Could not delete record for " + node);
            f.setFaultReason(e.getMessage());
            throw f;
        }
    }
    /** for implementing getAccount */
    protected Reader openAccountReader(AccountIdent ident) throws Exception {
      return new InputStreamReader (manager.readResource(ident));
    }
    
    
    
    
    /** implementing getNode, getNodeInTransaction */
    protected Reader openNodeReader(Transaction t, NodeIvorn ident) throws Exception {
        InputStream is = null;
        if (t == null) {
            is = manager.readResource(ident);
        } else {
            Object tx = getTxId(t);
            is = manager.readResource(tx,ident);
        }
      return new InputStreamReader(is);
    }
    
    
    
    
    public boolean hasAccount(AccountIdent ident) throws FileManagerFault {
        try {
            return manager.resourceExists(ident);
        } catch (ResourceManagerException e) {
            FileManagerFault f =  new FileManagerFault("Could not check for presence of " + ident);
            f.setFaultReason(e.getMessage());
            throw f;
        }
    }
    public boolean hasNode(NodeIvorn ident) throws FileManagerFault {
        try {
            return manager.resourceExists(ident);
        } catch (ResourceManagerException e) {
            FileManagerFault f =  new FileManagerFault("Could not check for presence of " + ident);
            f.setFaultReason(e.getMessage());
            throw f;
        }
    }
    
    /** for putNode and putNewNode - so need to create file if doesn't already exist.. */
    protected Writer openNodeWriter(Transaction t, Node node) throws Exception {
        Object tx = getTxId(t);
        manager.createResource(tx,node.getIvorn(),true);
        return new OutputStreamWriter(manager.writeResource(tx,node.getIvorn()));
    }
    /**
     * @param t
     * @return
     */
    private Object getTxId(Transaction t) {
        return ((ResourceManagerTransaction)t).txid;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TransactionalFileNodeStore:");
        buffer.append(" DEFAULT_TRANSACTION_TIMEOUT: ");
        buffer.append(DEFAULT_TRANSACTION_TIMEOUT);
        buffer.append(" manager: ");
        buffer.append(manager);
        buffer.append("]");
        return buffer.toString();
    }
    }



/* 
$Log: TransactionalFileNodeStore.java,v $
Revision 1.4  2005/04/28 21:38:06  clq2
roll back before 1035

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/