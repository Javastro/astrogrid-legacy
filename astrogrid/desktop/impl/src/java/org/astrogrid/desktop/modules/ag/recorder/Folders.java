/*$Id: Folders.java,v 1.6 2006/06/27 10:25:45 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import jdbm.RecordManager;
import jdbm.btree.BTree;
import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.desktop.modules.ag.MessageRecorderImpl;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;


/** represents a collection of folders - models their arrangement into a hierarchy, and
 * manages their storage in the JDBM
 *  
 * 
 * very little bounds checking on methods - clients use with care.
 * @todo work out whether all the syncronization overhead is necessary.
 * */
public class Folders implements TreeModel {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Folders.class);

    /** key that the folders are stored under in the JDBM */ 
    private final static String TREE_NAME = "recorder.folders";
    private static Comparator comp = new URIComparator();       
    /** factory method for folders object */
    public static Folders findOrCreate(RecordManager rec) throws IOException {
        long code = rec.getNamedObject(TREE_NAME);
        if (code ==0) {
            Folders fs =  new Folders(BTree.createInstance(rec,comp),rec);
            fs.initialize();
            return fs;
        } else {
            return new Folders(BTree.load(rec,code),rec);
        }      
}

    protected Folders(BTree tree, RecordManager rec) throws IOException {
        this.b = tree;
        this.rec = rec;
        this.root = (FolderImpl)getFolder(MessageRecorderImpl.ROOT); // will return null the first time
    }
    private FolderImpl root;
    private final BTree b;
    private final RecordManager rec;        
    private final  EventListenerList listenerList = new EventListenerList();        
    
    // initialise the first time..
    private synchronized void initialize() throws IOException {
        rec.setNamedObject(TREE_NAME,b.getRecid());
        root = new FolderImpl(
                new ExecutionInformation(MessageRecorderImpl.ROOT,
                        "Messages"
                        ,"Root folder"
                        ,ExecutionInformation.UNKNOWN
                        ,null,null
                        ),null);
        FolderImpl jobs = new FolderImpl(
                new ExecutionInformation(MessageRecorderImpl.JOBS,
                        "Workflows"
                        ,"Workflow folder"
                        ,ExecutionInformation.UNKNOWN
                        ,null,null
                        ),MessageRecorderImpl.ROOT);
        FolderImpl tasks = new FolderImpl(
                new ExecutionInformation(MessageRecorderImpl.TASKS,
                        "Tasks"
                        ,"Data processing tasks"
                        ,ExecutionInformation.UNKNOWN
                        ,null,null
                        ),MessageRecorderImpl.ROOT);
        FolderImpl queries = new FolderImpl(
                new ExecutionInformation(MessageRecorderImpl.QUERIES,
                        "Queries"
                        ,"Dataset query  tasks"
                        ,ExecutionInformation.UNKNOWN
                        ,null,null
                        ),MessageRecorderImpl.ROOT);        
      
        // don't think we need any user objects here.
        root.getChildKeyList().add(MessageRecorderImpl.QUERIES);
        root.getChildKeyList().add(MessageRecorderImpl.TASKS);
        root.getChildKeyList().add(MessageRecorderImpl.JOBS);            
        b.insert(MessageRecorderImpl.ROOT,root,false);
        b.insert(MessageRecorderImpl.JOBS,jobs,false);
        b.insert(MessageRecorderImpl.TASKS,tasks,false);
        b.insert(MessageRecorderImpl.QUERIES,queries,false);
        rec.commit();            
        final TreeModelEvent e = new TreeModelEvent(this, new Object[]{root});                    
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i >= 0; i-=2) {
                    if (listeners[i] == TreeModelListener.class) {
                        ((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
                    }
            }
            }
        });
    }
    
    
    //
    /** retrieve a folder 
     * @throws IOException*/
    public Folder getFolder(URI key) throws IOException {
         return (Folder)b.find(key);        
    }
    
    /** returns a List of URI */
    public List listLeaves() throws IOException {
        List result = new ArrayList(b.size() - 4); 
        TupleBrowser browser = b.browse();
        Tuple t = new Tuple();
        while (browser.getNext(t)) {
            FolderImpl f = (FolderImpl)t.getValue();
            if ((! f.isDeleted()) && f.getChildKeyList().size() == 0) { // add it if it has no children.
                result.add(f.getKey());
            }
        }
        return result;
        
    }
    
    /** remove all folders previously marked as 'deleted' 
     * @throws IOException */
    public void cleanup() throws IOException  {
        TupleBrowser browser = b.browse();
        Tuple t = new Tuple();
        while (browser.getNext(t)) {
            FolderImpl f = (FolderImpl)t.getValue();
            if (f.isDeleted()) {
            	b.remove(f); // @todo this may not work - in which case need to do 2 passes - first list, then delete 
            }
        }   
        rec.commit();
    }	
    
    
    public synchronized Folder createFolder(URI parentKey, ExecutionInformation newProcess) throws IOException {
        final FolderImpl f = new FolderImpl(newProcess,parentKey);
        b.insert(newProcess.getId(),f,false);
        if (parentKey != null) {
            FolderImpl parent = (FolderImpl)getFolder(parentKey);
            parent.getChildKeyList().add(newProcess.getId());
            b.insert(parentKey,parent,true);
        }
        rec.commit();
        FolderImpl parent = (FolderImpl)getFolder(f.getParentKey());
        final TreeModelEvent e = new TreeModelEvent(this
                , getPathToRoot(parent) 
        , new int[]{parent.getChildKeyList().indexOf(f.getKey())}
        , new Object[]{f});
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                    if (listeners[i]==TreeModelListener.class) {                   
                        ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
                    }          
                }
            }
        });
        return f;
    }
    
    
    public synchronized void deleteFolder(Folder g) throws IOException {
        FolderImpl doomed = (FolderImpl)g;
        if (doomed == null) {
            return;
        }
        FolderImpl parent = (FolderImpl)getFolder(doomed.getParentKey());
        // adjust parent
        int pos = parent.getChildKeyList().indexOf(doomed.getKey());
        parent.getChildKeyList().remove(pos);
        b.insert(parent.getKey(),parent,true);
        // just mark child as deleted.
        doomed.setDeleted(true); // will be cleaned up on shutdown
        //b.remove(doomed.getKey());
        rec.commit();
        final TreeModelEvent e =  new TreeModelEvent(this
                , getPathToRoot(parent) 
        , new int[]{pos}
        , new Object[]{doomed});
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {            
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                    if (listeners[i]==TreeModelListener.class) {
                        ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
                    }          
                }
            }
        });                        
    }
    
    public synchronized void updateFolder(Folder g) throws IOException {
       FolderImpl f = (FolderImpl)g;
        b.insert(f.getKey(),f,true); // overwrites previous definition.
        rec.commit();            
        FolderImpl parent = (FolderImpl)getFolder(f.getParentKey());
        final TreeModelEvent e = new TreeModelEvent(this
                , getPathToRoot(parent) 
        , new int[]{parent.getChildKeyList().indexOf(f.getKey())}
        , new Object[]{f}); 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {               
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                    if (listeners[i]==TreeModelListener.class) {        
                        ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
                    }          
                }
            }
        });
    }
    /** cribed from DefaultTreeModel - wish that class was more reusable.
     * Builds the parents of node up to and including the root node,
     * where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     * 
     * @param aNode the TreeNode to get the path for
     * @throws IOException
     */
    private Folder[] getPathToRoot(Folder aNode) throws IOException {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     * 
     * @param aNode  the TreeNode to get the path for
     * @param depth  an int giving the number of steps already taken towards
     *        the root (on recursive calls), used to size the returned array
     * @return an array of TreeNodes giving the path from the root to the
     *         specified node 
     * @throws IOException
     */
    private Folder[] getPathToRoot(Folder aNode, int depth) throws IOException {
        Folder[]              retNodes;
    // This method recurses, traversing towards the root in order
    // size the array. On the way back, it fills in the nodes,
    // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if(aNode == null) {
            if(depth == 0)
                return null;
            else
                retNodes = new Folder[depth];
        }
        else {
            depth++;
            if(aNode == root)
                retNodes = new Folder[depth];
            else {
                Folder parent = getFolder(((FolderImpl)aNode).getParentKey());
                retNodes = getPathToRoot(parent, depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    
    
    // Tree-model methods.
    /**
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public synchronized Object getRoot() {
        return root;
    }

    public int getChildCount(Object parent) {
        FolderImpl f = (FolderImpl)parent;
        return f.getChildKeyList().size();
    }

    public boolean isLeaf(Object node) {
       FolderImpl f =(FolderImpl)node;
    try {
        //is a leaf if it has a message folder associated.
        return 0 != rec.getNamedObject(f.getKey().toString());
    } catch (IOException e) {
        return false;
    }
    }

    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    public Object getChild(Object parent, int index) {
        URI id = (URI)((FolderImpl)parent).getChildKeyList().get(index);
        try {
            return getFolder(id);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        FolderImpl p = (FolderImpl)parent;
        FolderImpl c= (FolderImpl)child;
        return p.getChildKeyList().indexOf(c.getKey());
    }


    public void valueForPathChanged(TreePath path, Object newValue) {
        // doesn't apply - this isn't a user-editable tree.
    }
}

/* 
$Log: Folders.java,v $
Revision 1.6  2006/06/27 10:25:45  nw
findbugs tweaks

Revision 1.5  2006/06/15 09:47:51  nw
changed constructor visibility, to ease testing.

Revision 1.4  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.3.8.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3  2006/01/31 14:50:33  jdt
The odd typo and a bit of tidying.

Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.2  2005/11/23 18:09:37  nw
tuned up

Revision 1.1.2.1  2005/11/23 04:51:41  nw
changed return type.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/