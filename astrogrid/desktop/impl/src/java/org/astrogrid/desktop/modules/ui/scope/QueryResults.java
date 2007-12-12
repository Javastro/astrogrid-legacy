package org.astrogrid.desktop.modules.ui.scope;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Service;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** class that keeps a summary of the query result of each service */
public final class QueryResults {
    private final Map m = new HashMap();
    private final Map nodeMap = new HashMap();
    public QueryResults.QueryResult getResult(Service s) {
        return (QueryResults.QueryResult) m.get(s);
    }
    void addResult(QueryResults.QueryResult qr) {
        m.put(qr.service,qr);
    }

    void clear() {
        m.clear();
        nodeMap.clear();
    }
    
    /** for a given service, find the associated tree node, if any */
    public TreeNode findTreeNode(Service s) {
        QueryResults.QueryResult result = getResult(s);
        if (result != null) {
            return result.node;
        } else {
            return null;
            
        }
    }
    /** for a given tree node, find the associated service */
    public Service findService(TreeNode t) {
        return (Service)nodeMap.get(t);
    }
    /** associate a node with a query result.
     * @param qr
     * @param serviceNode
     */
    public void associateNode(QueryResults.QueryResult qr, TreeNode node) {
        nodeMap.put(node,qr.service);
        qr.node = node;
    }
/** structure representing a single query result */
public static final class QueryResult {
    public QueryResult(Service s,FileObject resultsDir) {
        this.service = s;
        this.resultsDir = resultsDir;
    }
    /** an error message  - may be null */
   public String error;
   /** count of results returned. only expected to be valid if error == null */
   public Integer count = PENDING;
   /** the service this result pertains to */
   public final Service service;
   /** the virtual results directory containing pointers to the result files */
   public  final FileObject resultsDir;
   /** the tree node assocoated with this result */
   private TreeNode node;
   /** helper method to format the result count - taking into account errors, and pending status */
   public Object getFormattedResultCount() {
       if (error != null) {
           return "Failed";
       } else if (PENDING.equals(count)) {
           return "Pending";	           
       } else {
           return count;
       }
   }
}
   /** constant for result count of a service that has not yet been queried */
   public static final Integer PENDING = new Integer(-1);
}