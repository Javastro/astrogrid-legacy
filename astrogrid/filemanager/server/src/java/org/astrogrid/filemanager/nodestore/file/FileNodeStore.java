/*$Id: FileNodeStore.java,v 1.3 2005/03/31 16:01:11 dave Exp $
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

import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.Attribute;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.Child;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.NodeUtils;
import org.astrogrid.filemanager.nodestore.NodeIvornFactory;
import org.astrogrid.filemanager.nodestore.NodeStore;
import org.astrogrid.filemanager.nodestore.TreeBundler;

import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/** Simple implementation of a file node store - used to develop xstream serialization.
 * Don't use - in particular, there's no transactional support. 
 * Implementation of the inteface methods is split into the general business logic, and the specific persistence bit.
 * This allows this class to be extended easily to integrate a different storage mechanism, while inheriting all the tricky logic bits.
 * @see org.astrogrid.filemanager.nodestore.file.TransactionalFileNodeStore 
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
public class FileNodeStore implements NodeStore {
    private static final String ENCODING = "UTF-8";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileNodeStore.class);

    
    /** Construct a new FileNodeStore
     * @param fac factory to create new node ivorns
     * @param defaultLocation default storage location for new nodes
     * @param storeDir location on local disk to store serialized node metadata
     * 
     */
    public FileNodeStore(NodeIvornFactory fac, URI defaultLocation,File storeDir) {
        super();
        this.storeDir = storeDir;
        this.nodeIvornFactory = fac;
        this.defaultLocation = defaultLocation;
        
        this.bundler = new TreeBundler(new BundlePreferences(),this);
        xs = new XStream(new AxisObjectPureJavaReflectionProvider());
        xs.alias("node",Node.class);
        xs.alias("node-ivorn",NodeIvorn.class);
        xs.alias("attribute",Attribute.class);
        xs.alias("child",Child.class);
        xs.setMode(XStream.NO_REFERENCES);
        xs.registerConverter(new URIConvertor());
        xs.registerConverter(new NodeTypeConvertor());
        xs.registerConverter(new NodeNameConvertor());
        
        checkStorage("nodes storage",storeDir);
        
    }
    
    /** test whether this directory exists, (or is creatable), and is readable and writable */
    protected static final void checkStorage(String storageDescription,File baseDir) {
        if (! baseDir.exists()) {
            logger.info("creating storage for " + storageDescription);
            baseDir.mkdirs();            
        }
        if (!baseDir.exists()) {
            String msg = "Base directory for " + storageDescription + "does not exist, and cannot be created";
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);            
        }
        if (!baseDir.isDirectory()) {
            String msg = "Base for " + storageDescription+"must be a directory";
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);
        }
        if (! (baseDir.canRead() && baseDir.canWrite())) {
            String msg = "Must be able to read and write to base directory for " + storageDescription;
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);
        }
        // now set up node and account dir;
        
        logger.info("storage locations seem ok");
        
    }
    
    protected final NodeIvornFactory nodeIvornFactory;
    protected final File storeDir;
    protected final TreeBundler bundler;
    /** serialization library */
    protected final XStream xs;
    protected final URI defaultLocation;
    
    private File makeAccountFile(AccountIdent ident)  { 
        return makeUriFile(ident.getValue());
    }

    private File makeNodeFile(NodeIvorn ident)  {
        return makeUriFile(ident.getValue());        
    }
    
    /**
     * @param i
     * @return
     * @throws RuntimeException
     */
    private File makeUriFile(URI i) throws RuntimeException {
        try {
            File acc = new File(storeDir,URLEncoder.encode(i.toString(),ENCODING));
            logger.debug(acc);
            return acc;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Really shouldn't happen -JVM is required to support uf-8");
        }
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#addAccount(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.AccountIdent, org.astrogrid.filemanager.common.Node)
     */
    public void addAccount(Transaction t, AccountIdent ident, Node root) throws DuplicateNodeFault, FileManagerFault {        
        if (this.hasAccount(ident)) {
            throw new DuplicateNodeFault("Account " + ident + " already exists");
        }
        Writer out = null;
        try {
            out = openAccountWriter(t,ident);
            xs.toXML(root.getIvorn(),out);
        } catch (Exception e) {
            FileManagerFault f = new FileManagerFault("Could not add account " + ident);
            f.setFaultReason(e.getMessage());
            throw f;           
        } finally {
            if (out != null) {
                try { 
                    out.close();
                } catch (IOException e) {
                    logger.warn("exception when closing output stream");
                }
            }
        }
    }
        
    /**
     * @param ident
     * @return
     * @throws IOException
     */
    protected Writer openAccountWriter(Transaction t,AccountIdent ident) throws Exception {
        return new FileWriter(makeAccountFile(ident));
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#createNode(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.NodeName, org.astrogrid.filemanager.common.NodeTypes)
     */
    public Node createNode(Transaction t, Node parent, NodeName name, NodeTypes type) throws FileManagerFault, NodeNotFoundFault, DuplicateNodeFault {
        NodeIvorn newIvorn = this.nodeIvornFactory.createNewNodeIvorn();
        Node newNode = NodeUtils.newNode(parent.getIvorn(),name,type,newIvorn,defaultLocation);
        putNewNode(t,newNode);
        
        //link it to parent.
        NodeUtils.addChild(parent,name,newIvorn);
        putNode(t,parent);
        return newNode;
    }
    /**
     * @throws FileManagerFault
     * @throws DuplicateNodeFault
     * @see org.astrogrid.filemanager.nodestore.NodeStore#createRootNode(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.NodeName, AccountIdent)
     */
    public Node createRootNode(Transaction t, NodeName name, AccountIdent account) throws DuplicateNodeFault, FileManagerFault {
        NodeIvorn newIvorn = this.nodeIvornFactory.createNewNodeIvorn();
        Node newNode = NodeUtils.newNode(null,name,NodeTypes.FOLDER,newIvorn,defaultLocation);
        Map m = Collections.singletonMap(NodeMetadata.HOME_SPACE,account.toString()); // record in attributes who this belongs to..
        NodeUtils.mergeAttribs(newNode,m); // add this attribute into the node.        
        putNewNode(t,newNode);
        return newNode;
    }
    
    
    /**
     * @throws FileManagerFault
     * @see org.astrogrid.filemanager.nodestore.NodeStore#createTransaction()
     */
    public Transaction createTransaction() throws FileManagerFault {
        return new Transaction(){

            public void commit() {
            }

            public void rollback() {
            }

            public void readyToCommit() {                
            }

            public void commitIfReadyElseRollback() {
            }
        };
    }
    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#delAccount(Transaction, org.astrogrid.filemanager.common.AccountIdent)
     */
    public void delAccount(Transaction t, AccountIdent ident) throws NodeNotFoundFault, FileManagerFault {
        File acc = makeAccountFile(ident);
        if (!acc.exists()) {
            throw new NodeNotFoundFault("Account " + ident + "doesn't exist");
        }
        if (!acc.delete() && acc.exists()) { // failed to delete?
            logger.warn("Failed to delete account " + ident);
            throw new FileManagerFault("Failed to delete account" + ident);
        }
    }
    
    
    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#deleteNode(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.Node)
     */
    public void deleteNode(Transaction t, Node node, Node parent) throws NodeNotFoundFault, FileManagerFault {    
        if (!hasNode(node.getIvorn())) {//it's gone. 
            if (NodeUtils.findChild(parent,node.getName()) == null ){ // remove it anyhow.
                logger.warn("Found a ghost child" + node + "\n" + parent);
                NodeUtils.removeChild(parent,node.getName());
                this.putNode(t,parent);
            }
            throw new NodeNotFoundFault(node.toString());
        }
        
        doDeleteNode( t,node);
        NodeUtils.removeChild(parent,node.getName());
        this.putNode(t,parent);
    }
    /**
     * @param node
     * @throws FileManagerFault
     */
    protected void doDeleteNode(Transaction t,Node node) throws FileManagerFault {
        File doomed = makeNodeFile(node.getIvorn());        
        if (!doomed.delete() && doomed.exists()) {
            logger.warn("Could not delete record for " + node);
            throw new FileManagerFault("Could not delete record for " + node);
        }
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#getAccount(org.astrogrid.filemanager.common.AccountIdent)
     */
    public Node getAccount(AccountIdent ident) throws NodeNotFoundFault, FileManagerFault {
        if (false == this.hasAccount(ident)) {
            throw new NodeNotFoundFault(ident.toString());
        }
        Reader in = null;
        Object obj;
        try {
            in = openAccountReader(ident);
            obj = xs.fromXML(in);
        } catch (Exception e) {
            logger.error(e);
            FileManagerFault f =  new FileManagerFault("Could not read account for " + ident);
            f.setFaultReason(e.getMessage());
            throw f;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("Could not close reader",e);
                }
            }
        }
        if (!(obj instanceof NodeIvorn)) {
            logger.error(obj);
            throw new FileManagerFault("Could not read account for " + ident + "\ndeserialized data not in expected format "
                        + obj.getClass().getName());
        }
       NodeIvorn iv =(NodeIvorn)obj;
        logger.debug(iv);        
        return getNode(iv);
    }
    /**
     * @param ident
     * @return
     * @throws FileNotFoundException
     */
    protected Reader openAccountReader(AccountIdent ident) throws Exception {
        return new FileReader(makeAccountFile(ident));
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#getNode(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public Node getNode(NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault {
        return getNodeInTransaction(null,ident);
    }


    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#getNodeInTransaction(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.NodeIvorn)
     */
    public Node getNodeInTransaction(Transaction t, NodeIvorn ident) throws NodeNotFoundFault, FileManagerFault {
        if (!hasNode(ident)) {
            throw new NodeNotFoundFault(ident.toString());
        }
        Reader in = null;
        Object obj = null;
        try {
            in = openNodeReader(t,ident);
            obj = xs.fromXML(in);
        } catch (Exception e) {
            logger.error(e);
            FileManagerFault f =  new FileManagerFault("Could not read node for " + ident);
            f.setFaultReason(e.getMessage());
            throw f;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("Could not close reader",e);
                }
            }
        }    
        if (!(obj instanceof Node)) {
            logger.error(obj);
            throw new FileManagerFault("Could not read nodet for " + ident + "\ndeserialized data not in expected format "
                        + obj.getClass().getName());
        }
        return (Node)obj;        
    }
    
    /**  Transaction may be null...
     * @param ident
     * @return
     * @throws FileNotFoundException
     */
    protected Reader openNodeReader(Transaction t,NodeIvorn ident) throws Exception {
        return new FileReader(makeNodeFile(ident));
    }    
    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#hasAccount(org.astrogrid.filemanager.common.AccountIdent)
     */
    public boolean hasAccount(AccountIdent ident) throws FileManagerFault {
        return makeAccountFile(ident).exists();
    }
    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#hasNode(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public boolean hasNode(NodeIvorn ident) throws FileManagerFault {
        return makeNodeFile(ident).exists();
    }
    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#putNode(org.astrogrid.filemanager.store.NodeStore.Transaction, org.astrogrid.filemanager.common.Node)
     */
    public void putNode(Transaction t, Node node) throws NodeNotFoundFault, FileManagerFault {
        if (!hasNode(node.getIvorn())) {
            throw new NodeNotFoundFault(node.getIvorn().toString());
        }
        Writer out = null;
        try {
            out = openNodeWriter(t,node);
            xs.toXML(node,out);
        } catch (Exception e) {
            FileManagerFault f =  new FileManagerFault("Unable to store node " + node.getIvorn());
            f.setFaultReason(e.getMessage());
            throw f;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("Exception closing output stream",e);
            }
        }
        }
    }
    
    public void putNewNode(Transaction t, Node node) throws DuplicateNodeFault, FileManagerFault {
        if (hasNode(node.getIvorn())) {
            throw new DuplicateNodeFault (node.getIvorn().toString());
        }
        Writer out = null;
        try {
            out = openNodeWriter(t,node);
            xs.toXML(node,out);
        } catch (Exception e) {
            FileManagerFault f =  new FileManagerFault("Unable to store node " + node.getIvorn());
            f.setFaultReason(e.getMessage());
            throw f;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("Exception closing output stream",e);
            }
        }
        }
    }    
    /**
     * @param node
     * @return
     * @throws IOException
     */
    protected Writer openNodeWriter(Transaction t,Node node) throws Exception {
        return new FileWriter(makeNodeFile(node.getIvorn()));
    }

    /**
     * @see org.astrogrid.filemanager.nodestore.NodeStore#bundleTree(org.astrogrid.filemanager.common.Node, org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] bundleTree(Node node, BundlePreferences hints) {
        return bundler.bundleTree(node,hints);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileNodeStore:");
        buffer.append(" nodeIvornFactory: ");
        buffer.append(nodeIvornFactory);
        buffer.append(" storeDir: ");
        buffer.append(storeDir);
        buffer.append(" bundler: ");
        buffer.append(bundler);
        buffer.append(" xs: ");
        buffer.append(xs);
        buffer.append(" defaultLocation: ");
        buffer.append(defaultLocation);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: FileNodeStore.java,v $
Revision 1.3  2005/03/31 16:01:11  dave
Fixed inverted if(xx) to if(false == xx)

Revision 1.2.10.1  2005/03/30 09:02:41  dave
Fixed NullPointerException in nodestore ...

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