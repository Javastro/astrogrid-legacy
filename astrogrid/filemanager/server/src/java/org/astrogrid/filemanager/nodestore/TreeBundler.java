/*$Id: TreeBundler.java,v 1.3 2005/03/31 16:00:11 dave Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore;

import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.Child;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/** Helper object that takes care of node bundling - can be used in any implementation of {@link NodeStore}, as just operates over
 * the mehtods of this interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
* @todo write unit tests.
 */
public class TreeBundler {
    /** default default number of nodes to prefetch */
    protected static final int DEFAULT_MAX_NODES = 100;
    /** default default depth to prefetch to */
    protected static final int DEFAULT_DEPTH = 2;
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TreeBundler.class);

    /** Construct a new NodeBundler
     * @param defaults set of default prefetching parameters. if any of the fields are null, values are supplied by the constants in this class.
     * @param store store to prefetch nodes from.
     * 
     */
    public TreeBundler(BundlePreferences defaults,NodeStore store) {
        super();
        massageDefaults(defaults); 
        this.defaults = defaults;
        this.store = store;
    }
    
    /** fill in any missing values in the supplied deffaults. */
    protected final static void massageDefaults(BundlePreferences defaults) {
        if (defaults.getMaxExtraNodes() == null) {
            defaults.setMaxExtraNodes(new Integer(DEFAULT_MAX_NODES));            
        }        
        if (defaults.getPrefetchDepth() == null) {
            defaults.setPrefetchDepth(new Integer(DEFAULT_DEPTH));
        }
        
    }
    
    protected final BundlePreferences defaults;
    protected final NodeStore store;
    
    /** prefetch a bunch of nodes, representing a subtree centered around the target node.
     * @param node target to prefetch around.
     * @param prefs hints to configure prefetching. if null, will use defaults.
     * @return an array of at least one node ( the target node). Probably more - but the target node will always be the first item in the array.
     */
    public Node[] bundleTree(Node node, BundlePreferences prefs) {
//      int prefetchDepth = prefs.getMaxExtraNodes() == null ? defaults.getMaxExtraNodes().intValue() :prefs.getPrefetchDepth().intValue();
//      int maxExtraNodes = prefs.getPrefetchDepth() == null ? defaults.getPrefetchDepth().intValue(): prefs.getMaxExtraNodes().intValue();

        int prefetchDepth = defaults.getPrefetchDepth().intValue();
        int maxExtraNodes = defaults.getMaxExtraNodes().intValue();
        if (null != prefs)
            {
            if (null != prefs.getPrefetchDepth())
                {
                prefetchDepth = prefs.getPrefetchDepth().intValue();
                }
            if (null != prefs.getMaxExtraNodes())
                {
                maxExtraNodes = prefs.getMaxExtraNodes().intValue();
                }
            }

        // check for negatives - which means 'all' and replace with a really big number - makes the following code easier..
        if (prefetchDepth < 0) prefetchDepth = Integer.MAX_VALUE;
        if (maxExtraNodes < 0) maxExtraNodes = Integer.MAX_VALUE;
        
        // check if any prefetching at all is wanted..
        if (maxExtraNodes == 0) {
            return new Node[]{node};
        }
        List results = new ArrayList();
        results.add(node);
        
        // parents
        if (null != prefs)
            {
	        if (prefs.isFetchParents() && prefetchDepth > 0) {
	                List ancestors = this.getAncestors(prefetchDepth,node);
	                results.addAll(ancestors);
	                maxExtraNodes -= ancestors.size();
	        	}
            }
        //bail out now, if full.
        if (maxExtraNodes < 1) {
            return  (Node[])results.toArray(new Node[results.size()]);        
        }
        
        // children - fetch breadth-first, bounded by depth and total number.
        List nextTier = new ArrayList();
        List tier = results; // odd starting conditions - as there's parents in this list. so only want to process the first one.
        int tierSize =1;
     getChildren: { // labeled block - so we can jump out of whole thing using a 'break ' in a nested loop.
        for (int i = prefetchDepth; i > 0; i--) {     // iterate down tiers         
            for (int j = 0; j < tierSize; j++) { // iterate across nodes in the tier.
                Node n = (Node)tier.get(j);
                if (!n.getType().equals(NodeTypes.FOLDER)) { // skip if not a folder.
                    continue;
                }
                List children = getChildren(n);
                maxExtraNodes -=children.size();
                nextTier.addAll(children);
                if (maxExtraNodes < 1) {// got enought nodes. finish off.
                    results.addAll(nextTier);
                    break getChildren;
                }
            }  // finished one tier.. 
            if (nextTier.isEmpty()) {// no more to follow. return;
                break getChildren;
            }
            // move down to next tier.
            results.addAll(nextTier);            
            tier = nextTier;
            nextTier = new ArrayList(); // a little inefficient. otherwise gets really complicated.
            tierSize = tier.size();
        }
     }

        // return our bundle of nodes.
        return (Node[])results.toArray(new Node[results.size()]);        
    }

    /** get the ancestors - parent and parents parents, etc fo a node as a list
     * 
     * @param levels number of generations to go back up.
     * @param n node to start from
     * @return list of parentage nodes.
     */
    protected List getAncestors(int levels,Node n) {
        List results = new ArrayList();
        Node current = n;
        for (int i = levels; i > 0; i++ ) {
            current = getNode(current.getParent());
            if (current == null) {
                break;
            }
            results.add(current);            
        }
        return results;
    }
    /** access a list of children of a node */    
    protected List getChildren(Node node) {
        Child[] children = node.getChild();
        List results = new ArrayList(children.length);
        for (int i = 0; i < children.length; i++) {
            Node n = getNode(children[i].getIvorn());
            if (n != null) {
                results.add(n);
            }
        }        
        return results;
    }
    
    /** access a node from the store in a failsafe manner
     * - traps all exceptions, and handles nulls, etc.
     * point is that a failure in the store shouldn't impact prefetching - as it's only an optimization.
     * @param ivorn
     * @return
     */
    protected Node getNode(NodeIvorn ivorn) {
        if (ivorn == null) {
            return null;
        }
        try {
        return store.getNode(ivorn);
    } catch(FileManagerFault f) {
        logger.warn("exception while prefetching",f);
    } catch (NodeNotFoundFault f) {
        logger.warn("exception while prefetching",f);
    }
    return null;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TreeBundler:");
        buffer.append(" DEFAULT_MAX_NODES: ");
        buffer.append(DEFAULT_MAX_NODES);
        buffer.append(" DEFAULT_DEPTH: ");
        buffer.append(DEFAULT_DEPTH);
        buffer.append(" defaults: ");
        buffer.append(defaults);
        buffer.append(" store: ");
        buffer.append(store);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: TreeBundler.java,v $
Revision 1.3  2005/03/31 16:00:11  dave
Fixed a null pointer exception.

Revision 1.2.10.1  2005/03/30 09:02:41  dave
Fixed NullPointerException in nodestore ...

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:36  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/
