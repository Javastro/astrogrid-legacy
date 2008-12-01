package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Extension to the starlink tablehandler that produces a summary of what it's parsed 
 * and access to where the results are stored */
public interface AstroscopeTableHandler extends VotableHandler {
    /** return a count of the number of rows parsed - or {@link QueryResultCollector#ERROR} if failed to parse */
    public int getResultCount();
    /** return the service tree node with all the results attached to it */
    public TreeNode getServiceNode();

}