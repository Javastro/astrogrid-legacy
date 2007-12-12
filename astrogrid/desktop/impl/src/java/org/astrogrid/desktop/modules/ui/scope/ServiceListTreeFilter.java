/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.Action;
import edu.berkeley.guir.prefuse.action.filter.Filter;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** exension of the standard 'build a visualization tree' which 
 * filteres out some branches.
 * 
 * close, but not there yet.
 *  - only removes children of service node - services still visible.
 *  - doesn't remove children of serrvices created before filtering
 *      (probably need to issue a repaint for this).
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 10, 200711:47:44 PM
 */
public class ServiceListTreeFilter extends Filter {

    /**
     * @param itemClass
     * @param gc
     */
    public ServiceListTreeFilter() {
        super(ITEM_CLASSES,true);
    }
    
    public static final String[] ITEM_CLASSES = 
    {ItemRegistry.DEFAULT_NODE_CLASS, ItemRegistry.DEFAULT_EDGE_CLASS};


    private Node m_root;

    // based on windowedTreefilter, but using an asstribute to halt recursion rather than doi.
    public void run(ItemRegistry registry, double frac) {
        Graph graph = registry.getGraph();
        Tree ftree = new DefaultTree();
        
        NodeItem root = registry.getNodeItem(((Tree)graph).getRoot(),true,true);
        ftree.setRoot(root);
        
        // process each node's edges
        List queue = new ArrayList();
        queue.add(root);
        while (! queue.isEmpty()) {
            NodeItem ni = (NodeItem)queue.remove(0);
            Node n = (Node)ni.getEntity();           
            boolean filtered = Boolean.valueOf(n.getAttribute(VizualizationsPanel.SERVICE_FILTERED_ATTR)).booleanValue();
            if (filtered) {
                ni.setVisible(false);
            } else {
                Iterator iter = n.getEdges();
                int i = 0;
                while (iter.hasNext()) {
                    Edge ne = (Edge)iter.next();
                    Node nn = (Node)ne.getAdjacentNode(n);
                    NodeItem nni = registry.getNodeItem(nn);
                    boolean recurse = nni == null || nni.getDirty() > 0;
                    if (recurse) {
                        nni = registry.getNodeItem(nn,true,true);
                    }
                    EdgeItem nne = registry.getEdgeItem(ne,true);
                    if (recurse) {
                        ni.addChild(nne);
                        queue.add(nni);
                    } else {
                        nne.getFirstNode().addEdge(nne);
                        nne.getSecondNode().addEdge(nne);
                    }
                }
            }

        }
        // update the registry's filtered graph
        registry.setFilteredGraph(ftree);
        
        // optional garbage collection
        super.run(registry, frac);
        
    } //   
}