/*$Id: CoreFileManager.java,v 1.7 2005/11/04 17:31:05 clq2 Exp $
 * Created on 16-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.filemanager.server;

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DataLocation;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.common.Ivorn;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.NodeUtils;
import org.astrogrid.filemanager.common.TransferInfo;
import org.astrogrid.filemanager.datastore.StoreFacade;
import org.astrogrid.filemanager.nodestore.NodeStore;
import org.astrogrid.filestore.common.exception.FileStoreException;
import org.astrogrid.filestore.common.file.FileProperties;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/** Core implementation of the filemanager.
 * <p>
 * Receives requests from clients (which are assumed to have been validated, logged and authenticated by decorators before
 reaching this class).
 * <p>
 * Processes requests by using a {@link org.astrogrid.filemanager.nodestore.NodeStore} that holds the local metadata, and a
 * {@link org.astrogrid.filemanager.datastore.StoreFacade} that provides access to the remote services where the content data
 resides.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public class CoreFileManager implements FileManagerPortType {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CoreFileManager.class);

    /** Construct a new TheFileManager
     *  aggregates together other components that do the real work -
     * @param config configuration object.
     * @param store storage for metadata (i.e. nodes).
     * @param fsfacade delegate to storage for data (i.e. content)
     */
    public CoreFileManager(FileManagerConfig config,NodeStore store, StoreFacade fsfacade) {
        this.store = store;
        this.config = config;
        this.fsfacade = fsfacade;
    }

    protected final FileManagerConfig config;
    protected final StoreFacade fsfacade;
    protected final NodeStore store;

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#getNode(org.astrogrid.filemanager.common.NodeIvorn,
     org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] getNode(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        // @oddity.
        // the node ivorn may either be just point to a node number, or it may have a node-number plus trailing path.
        // this is the only point where a node-ivorn of the seconfd form can leak into the server
        // - so handle here as a node traversal.
        // necessary so Filemanagerclient.node() can get to a starting node without fetching all children itself.
        // all other places that nodeIvorns are passed in, are expected to just be a node number.
        Node n = null;

        if (nodeIvorn.getValue().getFragment().indexOf('/') != -1) { // it's a node-number plus trailing path...
            StringTokenizer tok = new StringTokenizer(nodeIvorn.getValue().getFragment(),"/");
            try {
                nodeIvorn.getValue().setFragment(tok.nextToken());
            } catch (MalformedURIException e) {
                throw new NodeNotFoundFault(e.getMessage());
            }
            n = store.getNode(nodeIvorn); // get root.
            while (tok.hasMoreTokens()) { // traverse down the path.
                String childName = tok.nextToken();
                NodeIvorn childNodeIvorn = NodeUtils.findChild(n,new NodeName(childName));
                if (childNodeIvorn== null) {
                    throw new NodeNotFoundFault("did not find child " + childName);
                }
                n = store.getNode(childNodeIvorn);
            }
        } else { // just a straight access to a node id.
            n = store.getNode(nodeIvorn);
        }
        return store.bundleTree(n,hints);
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#addAccount(org.astrogrid.filemanager.common.AccountIdent,
     org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] addAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            DuplicateNodeFault, FileManagerFault {
        if (store.hasAccount(ident)) {
            throw new DuplicateNodeFault("Account " + ident +" already exists");
        }
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node root = store.createRootNode(t,new NodeName("home"), ident); // create new node, called 'home'
            store.addAccount(t,ident, root);
            t.readyToCommit();
            return new Node[]{root};
        } finally {
            t.commitIfReadyElseRollback();
        }

    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#getAccount(org.astrogrid.filemanager.common.AccountIdent,
     org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] getAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        Node root = store.getAccount(ident);
        return store.bundleTree(root,hints);
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#getIdentifier()
     */
    public Ivorn getIdentifier() throws RemoteException {
        return new Ivorn(config.getFileManagerIvorn().toString());
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#delete(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public Node[] delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        Node parent = null;
        Node node = null;
        try{
            node = store.getNodeInTransaction(t,nodeIvorn);
            if (node.getType().equals(NodeTypes.FOLDER) && node.getChild().length > 0) {
                throw new FileManagerFault("Cannot delete a folder without first deleting all its children");
            }
            parent = store.getNodeInTransaction(t,node.getParent());
            store.deleteNode(t,node,parent);
            t.readyToCommit();
        } finally {
            t.commitIfReadyElseRollback();
        }
        if (node.getLocation().getIdent() != null) {//remove content too
            try {
                StoreFacade.Store  contentStore = fsfacade.resolve(node.getLocation().getUri());
                contentStore.delete(node.getLocation().getIdent());
            } catch (FileStoreException e) {
                throw buildFault("Could not get FileStoreDelegate",e);// dunno whether we should throw here,
                                                                      // or just return to the user..
            }
        }
        return new Node[]{parent}; // only node which has changed.
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#readContent(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public TransferInfo readContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        Node node = store.getNode(nodeIvorn);
        if (! node.getType().equals(NodeTypes.FILE)) {
            throw new FileManagerFault("Invalid operation, not a file node");
        }
        DataLocation location = node.getLocation();
        if (location.getIdent() == null) { // no content.
            throw new NodeNotFoundFault("Node does not yet have any data");
        }
        try {
            StoreFacade.Store filestore = fsfacade.resolve(location.getUri());
            TransferInfo ti = filestore.requestReadFromStore(location.getIdent());
            // FileStore may not set the method field. Since only one method,
            // the short-term workaround is to hard-code it here.
            ti.setMethod("GET");
            return ti;
        } catch (FileStoreException e) {
            throw  buildFault("Could not resolve file store",e);
        }
    }




    /** helper method to build a nice fault.
     * @param e
     * @return
     */
    private FileManagerFault buildFault(String message, Exception e) {
        logger.debug(message,e);
        FileManagerFault f = new FileManagerFault(message);
        f.setFaultString(message + " :" + e.getMessage());
        StringWriter w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);
        e.printStackTrace(pw);
        f.setFaultDetailString(w.toString());
        return f;
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#writeContent(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public TransferInfo writeContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return doAppendOrWrite(nodeIvorn,true);
    }
    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#appendContent(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public TransferInfo appendContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return doAppendOrWrite(nodeIvorn,false);
    }
    /**
     * @param nodeIvorn
     * @return
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     */
    private TransferInfo doAppendOrWrite(NodeIvorn nodeIvorn,boolean overwrite) throws FileManagerFault, NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node node = store.getNodeInTransaction(t,nodeIvorn);
            if (! node.getType().equals(NodeTypes.FILE)) {
                throw new FileManagerFault("Invalid operation, not a file node");
            }
            DataLocation location= node.getLocation();
            if (location == null) {
                location = new DataLocation();
                node.setLocation(location);
                store.putNode(t,node);
            }
            if (location.getUri() == null) {
                location.setUri(config.getDefaultStorageServiceURI());
                store.putNode(t,node);
            }
            // set default target..
//
// Looks to me like this ignores location and always writes to default store ?
            URI target = config.getDefaultStorageServiceURI();
            StoreFacade.Store filestore = fsfacade.resolve(target);
            TransferInfo ti;
            if (location.getIdent() != null) {
//
// Bug - Saving changes to a file does not work, always get the same (original) file back.
// Problem - On importInit() the FileStore will return a new ident for the file.
// Patch for now, treat this as a new import (copied code from the else).
// Side effect is FileStore won't know that the old file is to be replaced.
// Symptoms suggest that it didn't remove the old one anyway - need to fix this asap or we will run out of disk space.
/*
                String ident = location.getIdent();
                t.readyToCommit();
                ti =  filestore.requestWriteToStore(ident,overwrite);
 */
                StoreFacade.NewResource nu = filestore.requestWriteToStore();
                location.setIdent(nu.ident);
                ti = nu.transfer;
            } else { // create a new storage location.
                StoreFacade.NewResource nu = filestore.requestWriteToStore();
                location.setIdent(nu.ident);
                ti = nu.transfer;
            }
            // FileStore may not set the method field. Since only one method,
            // the short-term workaround is to hard-code it here.
            ti.setMethod("PUT");
            node.setModifyDate(Calendar.getInstance()); //new date.
            store.putNode(t,node);
            t.readyToCommit();
            return ti;

        } catch (FileStoreException e) {
            throw buildFault("Could not resolve file store",e);
        } finally {
            t.commitIfReadyElseRollback();
        }
    }



    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#copyURLToContent(org.astrogrid.filemanager.common.NodeIvorn,
     org.apache.axis.types.URI)
     */
    public Node[] copyURLToContent(NodeIvorn nodeIvorn, URI url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node node = store.getNodeInTransaction(t,nodeIvorn);
            if (! node.getType().equals(NodeTypes.FILE)) {
                throw new FileManagerFault("Invalid operation, not a file node");
            }
            DataLocation location= node.getLocation();
            // set default target..
            URI target = config.getDefaultStorageServiceURI();
            if (location!= null && location.getUri() != null) {
                target = location.getUri();
            }
            StoreFacade.Store filestore = fsfacade.resolve(target);
            TransferInfo nfo = new TransferInfo();
            nfo.setUri(url);
            nfo.setMethod("GET");
            if (location != null && location.getIdent() != null) {
                filestore.readIn(location.getIdent(),nfo);
            } else { // create a new storage location.
                String ident = filestore.readIn(nfo);
                location= new DataLocation();
                location.setIdent(ident);
                location.setUri(target);
                node.setLocation(location);

            }
            node.setModifyDate(Calendar.getInstance()); //new date.
            store.putNode(t,node);
            t.readyToCommit();
           return new Node[]{node};
        } catch (FileStoreException e) {
            throw buildFault("Could not resolve file store",e);
        } finally {
            t.commitIfReadyElseRollback();
        }
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#copyContentToURL(org.astrogrid.filemanager.common.NodeIvorn,
     org.apache.axis.types.URI)
     */
    public void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo info) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        Node node = store.getNode(nodeIvorn);
        if (! node.getType().equals(NodeTypes.FILE)) {
            throw new FileManagerFault("Invalid operation, not a file node");
        }
        DataLocation location = node.getLocation();
        if (location.getIdent() == null) { // no content.
            throw new NodeNotFoundFault("Node does not yet have any data");
        }
        try {
            StoreFacade.Store filestore = fsfacade.resolve(location.getUri());
            filestore.writeOut(location.getIdent(),info);
        } catch (FileStoreException e) {
            throw buildFault("Could not resolve file store",e);
        }
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#move(org.astrogrid.filemanager.common.NodeIvorn,
     org.astrogrid.filemanager.common.NodeIvorn, org.astrogrid.filemanager.common.NodeName,
     org.astrogrid.filemanager.common.LocationIvorn)
     */
    public Node[] move(NodeIvorn nodeIvorn, NodeIvorn newParentIvorn, NodeName newNodeName,URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {

        NodeStore.Transaction t = store.createTransaction();
        try {
            Node node = store.getNodeInTransaction(t,nodeIvorn);
            boolean nodeMoves = newParentIvorn != null && ! newParentIvorn.equals(node.getParent());
            boolean nodeRenames = newNodeName != null && ! newNodeName.equals(node.getName());

            boolean storeMoves = newLocation != null && node.getLocation() != null && ! newLocation.equals(node.getLocation().
            getUri());

            if (! (nodeMoves || nodeRenames || storeMoves)) {
                throw new FileManagerFault("Nothing happens");
            }

            if (nodeMoves && node.getType().equals(NodeTypes.FOLDER)) {
                throw new FileManagerFault("Cannot move folders at the momoent - only rename them");
            }

            NodeIvorn targetParentIvorn = nodeMoves ? newParentIvorn: node.getParent();
            NodeName targetNodeName = nodeRenames ? newNodeName : node.getName();
            Node targetParent = store.getNodeInTransaction(t,targetParentIvorn);

            if ((nodeMoves || nodeRenames) && NodeUtils.findChild(targetParent,targetNodeName) != null) {
                throw new DuplicateNodeFault("Parent already has a child named " + targetNodeName.toString());
            }
            if (storeMoves ) {
                if (node.getLocation().getIdent() != null) { // has data
                    StoreFacade.Store targetStore = fsfacade.resolve(newLocation);
                    URI sourceLocation = node.getLocation().getUri();
                    String sourceIdent = node.getLocation().getIdent();
                    StoreFacade.Store sourceStore = fsfacade.resolve(sourceLocation);
                    TransferInfo nfo = sourceStore.requestReadFromStore(sourceIdent);
                    String newIdent = targetStore.readIn(nfo);
                    node.getLocation().setUri(newLocation);
                    node.getLocation().setIdent(newIdent);
                    sourceStore.delete(sourceIdent);
                } else { // no data
                    node.getLocation().setUri(newLocation);
                }
            }
            List results = new ArrayList(); // variable amount of results.
            results.add(node);

            // pay attention, here comes the tricky bits..

           if(nodeMoves) { // remove it from old parent.
               Node oldParent = store.getNodeInTransaction(t,node.getParent());
               node.setParent(targetParent.getIvorn());
               NodeUtils.removeChild(oldParent,node.getName());
               results.add(oldParent);
               store.putNode(t,oldParent);
           }
           if (nodeRenames) { // sneaky - need to do this here, before removing from one parent and adding to another.
               if (! nodeMoves) { // renaming only - so remove old name from current parent.
                   NodeUtils.removeChild(targetParent,node.getName());
               }
               node.setName(targetNodeName);
           }
           if (nodeMoves || nodeRenames) { // any movement or renaming needs to be recorded in the target parent.
               NodeUtils.addChild(targetParent,node.getName(),node.getIvorn());
               results.add(targetParent);
               store.putNode(t,targetParent);
           }
            store.putNode(t,node);

            t.readyToCommit();
            return (Node[])results.toArray(new Node[results.size()]);
        } catch (FileStoreException e) {
            throw buildFault("Could not get FileStoreDelegate",e);
        } finally {
            t.commitIfReadyElseRollback();
        }
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#copy(org.astrogrid.filemanager.common.NodeIvorn,
     org.astrogrid.filemanager.common.NodeIvorn, org.astrogrid.filemanager.common.NodeName,
     org.astrogrid.filemanager.common.LocationIvorn)
     */
    public Node[] copy(NodeIvorn nodeIvorn, NodeIvorn newParentIvorn, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node node = store.getNodeInTransaction(t,nodeIvorn);
            if (!node.getType().equals(NodeTypes.FILE)) {
                throw new FileManagerFault("Copy only supported for nodes of type FILE at the moment");
            }
            boolean nodeMoves = newParentIvorn != null && ! newParentIvorn.equals(node.getParent());
            boolean nodeRenames = newNodeName != null && ! newNodeName.equals(node.getName());
            boolean storeMoves = newLocation != null && node.getLocation() != null && ! newLocation.equals(node.getLocation().
            getUri());

            if (! (nodeMoves || nodeRenames || storeMoves)) {
                throw new FileManagerFault("Nothing happens");
            }

            NodeIvorn targetParentIvorn = nodeMoves ? newParentIvorn: node.getParent();
            NodeName targetNodeName = nodeRenames ? newNodeName : node.getName();
            Node newParent = store.getNodeInTransaction(t,targetParentIvorn);

            Node targetNode = store.createNode(t,newParent,targetNodeName,node.getType());
            targetNode.setAttributes(node.getAttributes()); // aliasing, but will be stored in a moment..
            if (storeMoves) {// storage location is to change
                if (node.getLocation().getIdent() != null) { // node has content
                    StoreFacade.Store sourceStore = fsfacade.resolve(node.getLocation().getUri());
                    StoreFacade.Store targetStore = fsfacade.resolve(newLocation);
                    TransferInfo nfo = sourceStore.requestReadFromStore(node.getLocation().getIdent());
                    String newIdent = targetStore.readIn(nfo);
                    DataLocation newDataLocation = new DataLocation();
                    newDataLocation.setUri(newLocation);
                    newDataLocation.setIdent(newIdent);
                    targetNode.setLocation(newDataLocation);
                } else {//no content yet..
                    DataLocation newDataLocation = node.getLocation();
                    newDataLocation.setUri(newLocation);
                    targetNode.setLocation(node.getLocation());
                }
            } else { // storage location remains the same
                if (node.getLocation().getIdent() != null) { // node has content - take a copy.
                    StoreFacade.Store sourceStore = fsfacade.resolve(node.getLocation().getUri());
                    DataLocation newDataLocation= node.getLocation();
                    newDataLocation.setIdent(sourceStore.duplicate(newDataLocation.getIdent()));
                    targetNode.setLocation(newDataLocation);
                }
            }

            store.putNewNode(t,targetNode);
            t.readyToCommit();
            return new Node[]{targetNode,newParent};
        } catch (FileStoreException e) {
            throw buildFault("Could not get FileStoreDelegate",e);
        } finally {
            t.commitIfReadyElseRollback();
        }
    }

    /** @todo rename to something more descriptive.
     * called by transferComplete() on the client side - get properties from the filestore,save and changes, and pass them back to
     the client.
     * @see org.astrogrid.filemanager.common.FileManagerPortType#refresh(org.astrogrid.filemanager.common.NodeIvorn,
     org.astrogrid.filemanager.common.BundlePreferences)
     */
    public Node[] refresh(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node n = store.getNodeInTransaction(t,nodeIvorn);
            if (n.getLocation().getIdent() != null) {// node has content
                StoreFacade.Store fs = fsfacade.resolve(n.getLocation().getUri());
                Map attribs = fs.getAttributes(n.getLocation().getIdent());
                if (attribs!= null && attribs.size() > 0) {
                    // copy across attribs - special treatment for size.
                    if (attribs.containsKey(FileProperties.CONTENT_SIZE_PROPERTY)) {
                        n.setSize(Long.valueOf(attribs.get(FileProperties.CONTENT_SIZE_PROPERTY).toString()));
                    }
                    NodeUtils.mergeAttribs(n,attribs);
                    store.putNode(t,n);
                }
            }
            t.readyToCommit();
            return store.bundleTree(n,hints);
        } catch (FileStoreException e) {
            throw buildFault("Could not get FileStoreDelegate",e);
        } finally {
            t.commitIfReadyElseRollback();
        }
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#addNode(org.astrogrid.filemanager.common.NodeIvorn,
     org.astrogrid.filemanager.common.NodeName, org.astrogrid.filemanager.common.NodeTypes)
     */
    public Node[] addNode(NodeIvorn parentIvorn, NodeName newNodeName, NodeTypes nodeType)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        NodeStore.Transaction t = store.createTransaction();
        try {
            Node parent = store.getNodeInTransaction(t,parentIvorn);
            Node newNode = store.createNode(t,parent,newNodeName,nodeType);
            t.readyToCommit();
            return new Node[]{newNode,parent};
        } finally {
            t.commitIfReadyElseRollback();
        }
    }

    /**
     * @see
     org.astrogrid.filemanager.common.FileManagerPortType#dummyMessageWorkAroundForAxis(org.astrogrid.filemanager.common.Ivorn,
     org.astrogrid.filemanager.common.TransferInfo)
     */
    public void dummyMessageWorkAroundForAxis(Ivorn ignored, TransferInfo ignoredToo) throws RemoteException {
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CoreFileManager:");
        buffer.append(" config: ");
        buffer.append(config);
        buffer.append(" resolver: ");
        buffer.append(fsfacade);
        buffer.append(" store: ");
        buffer.append(store);
        buffer.append("]");
        return buffer.toString();
    }
}


/*
$Log: CoreFileManager.java,v $
Revision 1.7  2005/11/04 17:31:05  clq2
axis_gtr_1046

Revision 1.6.32.1  2005/10/25 08:21:30  gtr
I hard-coded the method property of TransferInfo to get round some serialization problems.

Revision 1.6  2005/05/04 08:37:04  clq2
fixed deleting from portal

Revision 1.3.8.1  2005/04/11 11:30:59  nw
refactored nameGen into a component component

Revision 1.3  2005/03/31 14:59:29  dave
Patch fix to enable overwriting of file with the same name.
** This needs to be refactored when we get time **

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:36  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/03/01 15:07:34  nw
close to finished now.

Revision 1.1.2.3  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.2  2005/02/25 12:33:27  nw
finished transactional store

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

*/

