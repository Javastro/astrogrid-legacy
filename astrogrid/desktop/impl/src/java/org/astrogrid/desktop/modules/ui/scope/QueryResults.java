package org.astrogrid.desktop.modules.ui.scope;

import java.util.HashMap;
import java.util.Map;

import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** keeps a summary of the query result of each retriever.

 *  */
public final class QueryResults {
    private final Map<Retriever, QueryResult> m = new HashMap<Retriever, QueryResult>();
    private final Map<TreeNode, Retriever> nodeMap = new HashMap<TreeNode, Retriever>();
    
    public QueryResults.QueryResult getResult(final Retriever r) {
        return m.get(r);
    }
    void addResult(final QueryResults.QueryResult qr) {
        if (qr == null) {
            throw new IllegalArgumentException("null query result");
        }
        m.put(qr.retriever,qr);
    }

    void clear() {
        m.clear();
        nodeMap.clear();
    }
    
    /** for a given retriever, find the associated tree node, if any */
    public TreeNode findTreeNode(final Retriever r) {
        final QueryResults.QueryResult result = getResult(r);
        if (result != null) {
            return result.node;
        } else {
            return null;
            
        }
    }
    /** for a given tree node, find the associated retriever */
    public Retriever findRetriever(final TreeNode t) {
        return nodeMap.get(t);
    }
    /** associate a node with a query result.
     * @param qr
     * @param serviceNode
     */
    public void associateNode(final QueryResults.QueryResult qr, final TreeNode node) {
        if (qr == null) {
            throw new IllegalArgumentException("null query result");
        }
        if (node == null) {
            throw new IllegalArgumentException("null node");
        }        
        if (m.containsKey(qr.retriever)) {
            nodeMap.put(node,qr.retriever);
            qr.node = node;
        } else {
            throw new IllegalArgumentException("Unknown query result");
        }
    }
/** structure representing a single query result */
public static final class QueryResult {
    public QueryResult(final Retriever r,final FileObjectView resultsDir) {
        this.retriever = r;
        this.resultsDir = resultsDir;
    }
    /** an error message  - may be null */
   public String error;
   /** count of results returned. only expected to be valid if error == null */
   public Integer count = PENDING;
   /** the retriever this result pertains to */
   public final Retriever retriever;
   /** the virtual results directory containing pointers to the result files */
   public  final FileObjectView resultsDir;
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

/** helper method to format the result count for the results filterwheel. */
public String getSummarizedResultCount() {
    if (error != null || count == null) {
        return "Failed";             
    } else {
        switch(count.intValue()) {
            case -1:
                return "Pending";
            case 0:
                return "No results";
            default:
                return "Results";
        }
    }
    
}
}


   /** constant for result count of a service that has not yet been queried */
   public static final Integer PENDING = -1; // autoboxed
}
