/*$Id: AxisNodeWrapper.java,v 1.4 2005/04/28 20:42:04 clq2 Exp $
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
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.Attribute;
import org.astrogrid.filemanager.common.Child;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.NodeUtils;
import org.astrogrid.filemanager.common.TransferInfo;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.tree.TreeNode;

/** Implementation of FileManagerNode that wraps around the primitive Node bean that is transported over the soap call.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *
 */
public class AxisNodeWrapper extends Observable implements FileManagerNode, NodeMetadata {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileManagerNode.class);

    /** Construct a new AxisNodeWrapper
     * 
     */
    public AxisNodeWrapper(org.astrogrid.filemanager.common.Node bean, NodeDelegate nd) {
       this.bean = bean;
       this.nodeDelegate = nd;
    }
    /** the primitive data bean */
    protected org.astrogrid.filemanager.common.Node bean;
    /** node delegate used to do all the donkey work */
    protected final NodeDelegate nodeDelegate;
    
   

    /**will return a homespace-rooted ivorn where possible. For unrooted nodes, returns the nodeId.
     * who logged into the delegate).
     * @see org.astrogrid.filemanager.client.FileManagerNode#getIvorn()
     */
    public Ivorn getIvorn() throws FileManagerFault, NodeNotFoundFault, RemoteException {
        List path = new ArrayList();
        FileManagerNode current = this;
        while(current.getParentNode() != null) {
            path.add(current.getName());
            current = current.getParentNode();
        }
        Collections.reverse(path);
        StringBuffer buff= new StringBuffer();
        Object hasHomeSpace = current.getMetadata().getAttributes().get(NodeMetadata.HOME_SPACE);
        if (hasHomeSpace != null) { // this node is associated with a home space..
            buff.append(hasHomeSpace.toString());
            buff.append("#");
            //buff.append("/");        
            for( Iterator i = path.iterator();i.hasNext();) {
                buff.append(i.next());
                if (i.hasNext()) {
                    buff.append("/");
                }
            }
        } else { // best effort is just to return the nodeIvorn - as node is not accessible any other way.
            logger.warn("Cannot build homespace ivorn for " + this);
            buff.append(this.getMetadata().getNodeIvorn());
        }
        try {
            return  new Ivorn(buff.toString());  
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unexpected -",e);
        }

    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#getName()
     */
    public String getName() {
        return bean.getName().toString();
    }

    /** uses node delegate to retreive the node that this node refers to as parent.
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#getParent()
     */
    public FileManagerNode getParentNode() throws FileManagerFault, NodeNotFoundFault, RemoteException {
        NodeIvorn parent = bean.getParent();
        if (parent == null) {
            return null;
        } else {
            return nodeDelegate.getNode(parent);
        }
    }
    
    /** implementation of the javax.swing.. TreeNode interface method. */
    public TreeNode getParent() {
        try {
            return getParentNode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#delete()
     */
    public void delete() throws FileManagerFault, NodeNotFoundFault, RemoteException {
        nodeDelegate.delete(this.getNodeIvorn());
    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#isFile()
     */
    public boolean isFile() {
        return bean.getType().equals(NodeTypes.FILE);
    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#isFolder()
     */
    public boolean isFolder() {
        return bean.getType().equals(NodeTypes.FOLDER);
    }

    /** does some sanity checking before passing over to server (where all this sanity checking is repeated, as we can't assume every client will be wellbehaved
     * @throws DuplicateNodeFault
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#copy(java.lang.String, org.astrogrid.filemanager.client.Node, org.astrogrid.store.Ivorn)
     */
    public FileManagerNode copy(String name, FileManagerNode targetParent, Ivorn location) throws DuplicateNodeFault, FileManagerFault, NodeNotFoundFault, RemoteException {
        if (name == null && targetParent == null && location == null) {
            throw new IllegalArgumentException("Nothing happens");
        }
        NodeIvorn newParentIvorn = null;
        NodeName nName = name == null ? null : new NodeName(name);
        
        if (targetParent != null) {
            if (! targetParent.isFolder()){
                throw new UnsupportedOperationException("The parent node is not a container");
            }       
            if (! (targetParent instanceof AxisNodeWrapper)) {
                throw new IllegalArgumentException("Parent node must be of same class - " + this.getClass().getName());
            }
            AxisNodeWrapper axisNode = ((AxisNodeWrapper)targetParent);
            newParentIvorn = axisNode.getNodeIvorn();  
            if (name != null && axisNode.hasChild(nName) != null) {
                throw new DuplicateNodeFault("New Parent already has a child named " + name);
            }
        }
        try {
        URI locationIvorn = location == null ? null :  new URI(location.toString());
        return nodeDelegate.copy(this.getNodeIvorn(),newParentIvorn,nName,locationIvorn);
        } catch (URI.MalformedURIException e) {
            throw new RuntimeException("Really unlikey - just converted from one uri to another",e);
        }
    }

    /** sanity checks, and passes on to server.
     * @throws DuplicateNodeFault
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#move(java.lang.String, org.astrogrid.filemanager.client.Node, org.astrogrid.store.Ivorn)
     */
    public void move(String name, FileManagerNode targetParent, Ivorn location) throws DuplicateNodeFault, FileManagerFault, NodeNotFoundFault, RemoteException {
        if (name == null && targetParent == null && location == null) {
            throw new IllegalArgumentException("Nothing happens");
        }
        NodeIvorn newParentIvorn = null;
        NodeName nName = name == null ? null : new NodeName(name);
        
        if (targetParent != null) {
            if (!targetParent.isFolder()){
            throw new UnsupportedOperationException("The parent node is not a container");
            }                        
            if (! (targetParent instanceof AxisNodeWrapper)) {
                throw new IllegalArgumentException("Parent node must be of same class - " + this.getClass().getName());
            }            
            AxisNodeWrapper axisNode = ((AxisNodeWrapper)targetParent);
            newParentIvorn = axisNode.getNodeIvorn(); 
            if (name != null &&  axisNode.hasChild(nName) != null) {
                    throw new DuplicateNodeFault("New Parent already has a child named " + name);                
            }
        }
        try {
            URI locationIvorn = location == null ? null :  new URI(location.toString());
            nodeDelegate.move(this.getNodeIvorn(),newParentIvorn,nName,locationIvorn);
            } catch (URI.MalformedURIException e) {
                throw new RuntimeException("Really unlikey - just converted from one uri to another",e);
            }
    }

    /** sanity checks, then delegates to server.
     * @throws DuplicateNodeFault
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#addFolder(java.lang.String)
     */
    public FileManagerNode addFolder(String name) throws UnsupportedOperationException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault, RemoteException {
        if (!isFolder()) {
            throw new UnsupportedOperationException("This node is not a container");
        }          
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        NodeName nName = new NodeName(name);
        if (hasChild(nName) != null) {
            throw new DuplicateNodeFault("This node already has a child called " + name);
        }
        
        return nodeDelegate.addNode(this.getNodeIvorn(),nName,NodeTypes.FOLDER);
    }
    
    /** find the ivorn associated with a named child, or null if no child of that name */
   protected NodeIvorn hasChild(NodeName childName) {
        return NodeUtils.findChild(this.bean,childName);
    }
    

    /** sanity checking, then delegates
     * @throws DuplicateNodeFault
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#addFile(java.lang.String)
     */
    public FileManagerNode addFile(String name) throws UnsupportedOperationException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault, RemoteException {
        if (!isFolder()) {
            throw new UnsupportedOperationException("This node is not a container");
        }        
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }        
        return nodeDelegate.addNode(this.getNodeIvorn(),new NodeName(name),NodeTypes.FILE);
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#getChild(java.lang.String)
     */
    public FileManagerNode getChild(String childName) throws UnsupportedOperationException, FileManagerFault, NodeNotFoundFault, RemoteException {
        if (!isFolder()) {
            throw new UnsupportedOperationException("This node is not a container");
        }        
        if (childName == null) {
            throw new IllegalArgumentException("null path");
        }
        NodeIvorn childIvorn = hasChild(new NodeName(childName));
        if (childIvorn == null) {
            throw new NodeNotFoundFault("This node does not have a child called " + childName);
        }
        return nodeDelegate.getNode(childIvorn);
    }

    /** returns a stream that writes to storage, and that calls {@link #transferCompleted()} when stream closed.
     * @see org.astrogrid.filemanager.client.FileManagerNode#writeContent()
     */
    public OutputStream writeContent() throws IOException, UnsupportedOperationException,
            NodeNotFoundFault, FileManagerFault {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }
        URL url =  new URL(nodeDelegate.writeContent(this.getNodeIvorn()).getUri().toString());
        FileStoreOutputStream stream = new FileStoreOutputStream(url) {
            public void close() throws IOException {
                super.close();
                try {
                    transferCompleted();
                } catch (IOException e) { // rethrow any remote exceptions and faults.
                    throw e;
                } 
            }
        };
        stream.open();
        return stream;
    }
    
    /**@see #writeContent()
     * @see org.astrogrid.filemanager.client.FileManagerNode#appendContent()
     */
    public OutputStream appendContent() throws IOException, UnsupportedOperationException,
            NodeNotFoundFault, FileManagerFault {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }
        URL url =  new URL(nodeDelegate.appendContent(this.getNodeIvorn()).toString());
        FileStoreOutputStream stream = new FileStoreOutputStream(url) {
            public void close() throws IOException {
                super.close();
                try {
                    transferCompleted();
                } catch (IOException e) { // rethrow any remote exceptions and faults.
                    throw e;
                } 
            }
        };
        stream.open();
        return stream;
    }   

    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#readContent()
     */
    public InputStream readContent() throws IOException, UnsupportedOperationException,
            NodeNotFoundFault, FileManagerFault {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }
        URL url =  new URL(nodeDelegate.readContent(this.getNodeIvorn()).getUri().toString());
        FileStoreInputStream stream = new FileStoreInputStream(url);
        stream.open();
        return stream;
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @throws MalformedURLException
     * @see org.astrogrid.filemanager.client.FileManagerNode#contentURL()
     */
    public URL contentURL() throws UnsupportedOperationException, FileManagerFault, NodeNotFoundFault, MalformedURLException, RemoteException {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }
        return new URL(nodeDelegate.readContent(this.getNodeIvorn()).getUri().toString());
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws MalformedURIException
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#copyURLToContent(java.net.URL)
     */
    public void copyURLToContent(URL url) throws UnsupportedOperationException, FileManagerFault, NodeNotFoundFault, RemoteException, MalformedURIException {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }
        nodeDelegate.copyURLToContent(this.getNodeIvorn(),new URI(url.toString()));
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws MalformedURIException
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#copyContentToURL(java.net.URL)
     */
    public void copyContentToURL(URL url) throws UnsupportedOperationException, FileManagerFault, NodeNotFoundFault, RemoteException, MalformedURIException {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }        
        TransferInfo props = new TransferInfo();
        props.setMethod("PUT");
        props.setUri(new URI(url.toString()));
        
        nodeDelegate.copyContentToURL(this.getNodeIvorn(),props);
    }

    /**
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @see org.astrogrid.filemanager.client.FileManagerNode#refresh()     
     * NB - refresh is implemented by just getting the node again from the server, ignoring the cache.
     * due to bunching, this may cause other nodes to be refreshed too..
     */
    public void refresh() throws FileManagerFault, NodeNotFoundFault, RemoteException {
         nodeDelegate.getNodeIgnoreCache(this.getNodeIvorn());
    }
    
    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#transferCompleted()
     */
    public void transferCompleted() throws NodeNotFoundFault, FileManagerFault, RemoteException {
        nodeDelegate.transferCompleted(this.getNodeIvorn());
    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerNode#iterator()
     */
    public NodeIterator iterator() throws NodeNotFoundFault, FileManagerFault {
        if (!isFolder()) {
            throw new UnsupportedOperationException("This node is not a container");
        }                
        final Child[] children = bean.getChild() != null ? bean.getChild() : new Child[0];
        return new NodeIterator() {
            protected int i = 0;
            protected FileManagerNode current;
            
            public FileManagerNode nextNode() throws FileManagerFault, NodeNotFoundFault, RemoteException {
                NodeIvorn childIvorn= children[i++].getIvorn();
                current = AxisNodeWrapper.this.nodeDelegate.getNode(childIvorn);
                return current;
            }

            public void remove() {
                if (current != null) {
                    try {
                        current.delete();
                    } catch (NodeNotFoundFault e) {
                        throw new RuntimeException("NodeNotFoundFault",e);
                    } catch (FileManagerFault e) {
                        throw new RuntimeException("FileManagerFault",e);
                    } catch (RemoteException e) {
                        throw new RuntimeException("RemoteException",e);
                    }
                }
            }

            public boolean hasNext() {
                return i < children.length;
            }

            public Object next() {
                try {
                    return nextNode();
                } catch (NodeNotFoundFault e) {
                    throw new RuntimeException("NodeNotFoundFault",e);
                } catch (FileManagerFault e) {
                    throw new RuntimeException("FileManagerFault",e);
                } catch (RemoteException e) {
                    throw new RuntimeException("RemoteException",e);
                }
            }
        };
    }

    /** this class implements the metadata interface too.
     * @see org.astrogrid.filemanager.client.FileManagerNode#getMetadata()
     */
    public NodeMetadata getMetadata() {
        return this;
    }

    /** metadata method.
     * @see org.astrogrid.filemanager.client.NodeMetadata#getContentLocation()
     */
    public java.net.URI getContentLocation() throws UnsupportedOperationException {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }     
        if (bean.getLocation() == null || bean.getLocation().getUri()== null) {
            return null;
        }
        try {
            return new  java.net.URI(bean.getLocation().getUri().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("unexpected",e);
        }
    }
    /** metadata method */
    public String getContentId() throws UnsupportedOperationException {
        if (!isFile()) {
            throw new UnsupportedOperationException("This node is not a file");
        }  
        if (bean.getLocation() == null) {
            return null;
        }
        return bean.getLocation().getIdent();
    }

    /**
     * @see org.astrogrid.filemanager.client.NodeMetadata#getSize()
     */
    public Long getSize() {
        return bean.getSize();
    }

    /**
     * @see org.astrogrid.filemanager.client.NodeMetadata#getCreateDate()
     */
    public Calendar getCreateDate() {
        return bean.getCreateDate();
    }

    /**
     * @see org.astrogrid.filemanager.client.NodeMetadata#getModifyDate()
     */
    public Calendar getModifyDate() {
        return bean.getModifyDate();
    }

    /**
     * @see org.astrogrid.filemanager.client.NodeMetadata#getNodeIvorn()
     */
    public NodeIvorn getNodeIvorn() {
        return this.bean.getIvorn();
    }
    /** returns an unmodifiable map of attributes for this node */
    public Map getAttributes() {
        Attribute[] attr = this.bean.getAttributes();
        if (attr == null) {
            return Collections.unmodifiableMap(Collections.EMPTY_MAP);
        }
        Map m = new HashMap(attr.length);
        for (int i = 0; i < attr.length; i++) {
            m.put(attr[i].getKey(),attr[i].getValue());
        }
        return Collections.unmodifiableMap(m);
    }


    /** update the wrapped bean - and notify any observers of this.*/
    public void setBean(org.astrogrid.filemanager.common.Node newBean) {
        if (newBean != null && ! this.bean.equals(newBean)) { // only update if different to the current one.
            this.bean = newBean;
            super.setChanged();
            super.notifyObservers();
        }
    }

    /** important method - caching and notifying observers of changes relies upon it */
    public boolean equals(Object obj) {
        if (! (obj instanceof AxisNodeWrapper)) {
            return false;
        }
        AxisNodeWrapper other = (AxisNodeWrapper)obj;
        return this.bean.equals(other.bean);
    }
    public int hashCode() {
        return this.bean.hashCode();
    }
    public String toString() {
        return this.bean.toString();
    }

    /** swing api method.
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    public int getChildCount() {
        if (this.bean.getChild() ==null) {
            return 0;
        } else {
            return this.bean.getChild().length;
        }
    }

    /**swing api method.
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
        return isFolder();
    }

    /**swing api method.
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return isFile();
    }

    /**swing api method - wraps {@link #iterator()}
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @see javax.swing.tree.TreeNode#children()
     */
    public Enumeration children() {
        final Iterator i;
        try {
            i = this.iterator();
        } catch (NodeNotFoundFault e) {
            throw new RuntimeException("children()",e);
        } catch (FileManagerFault e) {
            throw new RuntimeException("children()",e);
        }
        return new Enumeration() {

            public boolean hasMoreElements() {
                return i.hasNext();
            }

            public Object nextElement() {
                return i.next();
            }
            
        };
    }


    /**swing api method.
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt(int childIndex) {
        try {
            return nodeDelegate.getNode(bean.getChild(childIndex).getIvorn());
        } catch (FileManagerFault e) {
            throw new RuntimeException("getChildAt",e);
        } catch (NodeNotFoundFault e) {
            throw new RuntimeException("getChildAt",e);
        } catch (RemoteException e) {
            throw new RuntimeException("getChildAt",e);
        }
    }

    /**
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    public int getIndex(TreeNode node) {
        AxisNodeWrapper ax = (AxisNodeWrapper)node;
        NodeIvorn childIvorn = ax.getNodeIvorn();
        Child[] children = bean.getChild();
        for (int i = 0; i < children.length; i++) {
            if (children[i].getIvorn().equals(childIvorn)) {
                return i;
            }
        }
        return -1;
    }


}


/* 
$Log: AxisNodeWrapper.java,v $
Revision 1.4  2005/04/28 20:42:04  clq2
1035

Revision 1.2.22.1  2005/04/27 10:46:50  nw
minor fix - enables move.

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.5  2005/03/01 23:41:14  nw
split code inito client and server projoects again.

Revision 1.1.2.4  2005/03/01 15:07:29  nw
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