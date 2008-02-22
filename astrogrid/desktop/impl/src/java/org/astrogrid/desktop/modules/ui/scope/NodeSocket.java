package org.astrogrid.desktop.modules.ui.scope;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Defines somewhere you can place a tree node.
 *
 * @author   Mark Taylor
 * @since    22 Feb 2008
 */
public interface NodeSocket {

    /**
     * Adds a given child node.  The child is in general not required to be
     * part of any tree.  This method may be called multiple times to insert
     * multiple nodes at the same place.
     *
     * @param  child   node to add
     */
    void addNode(TreeNode child);
}
