package org.astrogrid.desktop.modules.ui.scope;

import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.render.ImageFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

/** represents the internal model of the vizualization - a tree of results, plus selections, etc.
 * model is shared between all vizualizations.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jan-2006
 *
 */
public  class VizModel {

    private final TreeNode rootNode;
    
    private final FocusSet selectionFocusSet;
    private final NodeSizingMap nodeSizingMap;

    private final Tree tree;
    private final DalProtocolManager protocols;
    public VizModel(DalProtocolManager protocols) {
        this.protocols = protocols;        
        this.nodeSizingMap = new NodeSizingMap();
        this.selectionFocusSet = new DefaultFocusSet();       
        rootNode = new DefaultTreeNode();
        rootNode.setAttribute(Retriever.LABEL_ATTRIBUTE,"Search Results");
        
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            p.setVizModel(this);
            TreeNode primary = p.getPrimaryNode();
            primary.setAttribute(Retriever.LABEL_ATTRIBUTE,p.getName());
           
            DefaultEdge primaryEdge = new DefaultEdge(rootNode,primary);
            primaryEdge.setAttribute(Retriever.WEIGHT_ATTRIBUTE,"3");
            rootNode.addChild(primaryEdge);            
        }

        tree = new DefaultTree(rootNode);          
    }

    /** clears the graph, plus all selections */
    public void clear() {
        getNodeSizingMap().clear();
        getSelectionFocusSet().clear();
        ((DefaultTableModel)protocols.getQueryResultTable()).setNumRows(0); // clear the service list table.
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            p.getPrimaryNode().removeAllChildren();
        }         
        //just for sanity go make sure the remaining nodes which are really just
        //siaNode, rootNode, coneNode are set to false for the selected attribute.
        for (Iterator i = getTree().getNodes(); i.hasNext(); ) {
            Node n = (Node)i.next();
            n.setAttribute("selected","false");
        }                
    }        
   

   
    
    public TreeNode getRootNode() {
        return rootNode;
    }        

    /** focus set used to maintain list of nodes selected for download.
     * focus set is shared between vizualizaitons- so changes in one will be seen in others.
     */
    public FocusSet getSelectionFocusSet() {
        return selectionFocusSet;
    }


    /**access the shared data model */
    public Tree getTree() {
        return tree;
    }
    
    /** returns true if this node is == to the root of one of the result branches
     */
    public boolean isPrimaryNode(TreeNode t) {
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            if (t == p.getPrimaryNode()) {
                return true;
            }
        }
        return false;
    }

    public DalProtocolManager getProtocols() {
        return this.protocols;
    }
    
    public NodeSizingMap getNodeSizingMap() {
        return nodeSizingMap;
    }

    /** helper method - find node by label
     * 
     * @param label label to search for
     * @param startNode starting point to search downwards from. if null, use {@link #getRootNode()}
     * @return treenode with matching label, or null;
     */
    protected TreeNode findNode(String label, TreeNode startNode) {
        if(startNode == null)  {
            startNode = getRootNode();
        }
        Iterator iter = startNode.getChildren();
        while(iter.hasNext()) {
            TreeNode n = (TreeNode)iter.next();
            if(n.getAttribute(Retriever.LABEL_ATTRIBUTE).equals(label)) {
                return n;
            }
        }
        return null;
    }

}