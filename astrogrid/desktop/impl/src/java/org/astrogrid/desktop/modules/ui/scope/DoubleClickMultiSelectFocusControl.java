package org.astrogrid.desktop.modules.ui.scope;



import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.collections.BreadthFirstTreeIterator;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** focus control - on double click, adds / removes node and children from focus set. */
public class DoubleClickMultiSelectFocusControl extends ControlAdapter {
    
    public DoubleClickMultiSelectFocusControl(VizualizationController viz) {
        set = viz.getVizModel().getSelectionFocusSet();
        this.viz = viz;
    }
    private final VizualizationController viz;
    private final FocusSet set;
    public void itemClicked(VisualItem item, MouseEvent e) {            
        if ( e.getClickCount() ==2 && 
                item instanceof NodeItem && 
                SwingUtilities.isLeftMouseButton(e)) {               
            TreeNode node = (TreeNode)item.getEntity();
            
            if (set.contains(node)) {// do a remove of this, and all children, and any parents.
                deselectSubtree(node,set);
            } else { // an add of this, and all children
                selectSubtree(node,set);
            }
            viz.reDrawGraphs();
           
        }//if
    }
	public static void selectSubtree(TreeNode node, FocusSet set) {
		for (Iterator i = new BreadthFirstTreeIterator(node); i.hasNext(); ) {
		    
		    TreeNode n = (TreeNode)i.next();
		    n.setAttribute("selected","true"); // yechh.
		    if (! set.contains(n)) {
		        set.add(n);
		    }
		}
	}
	public static void deselectSubtree(TreeNode node, FocusSet set) {
		for (Iterator i = new BreadthFirstTreeIterator(node); i.hasNext(); ) {
		    TreeNode n = (TreeNode)i.next();
		    set.remove(n);
		    n.setAttribute("selected","false"); // attribute used to speed up coloring function.
		}
		while (node.getParent() != null) {
		    node = node.getParent();
		    if (set.contains(node)) {
		        node.setAttribute("selected","false");
		        set.remove(node);
		    }
		}
	}
}