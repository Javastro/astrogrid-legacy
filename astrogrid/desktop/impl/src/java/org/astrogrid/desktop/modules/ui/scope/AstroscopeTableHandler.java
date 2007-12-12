package org.astrogrid.desktop.modules.ui.scope;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** extension to the starlink tablehandler that produces a summary of what it's parsed 
 * and access to where the resuls are stored */
public interface AstroscopeTableHandler extends VotableHandler {
    /** return a count of the number of rows parsed - or {@link QueryResultCollector#ERROR} if failed to parse */
    public int getResultCount();
    /** return an optional message about the results of the parse */
    public String getMessage();
    /** return the service tree node with all the results attached to it */
    public TreeNode getServiceNode();

}