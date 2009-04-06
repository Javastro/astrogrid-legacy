/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.QueryResults.QueryResult;

import edu.berkeley.guir.prefuse.graph.TreeNode;
/** Unit test for qyert results
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20087:57:00 AM
 */
public class QueryResultsUnitTest extends TestCase {

    private FileObject fileObject;
    private Retriever ret;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fileObject = createNiceMock(FileObject.class);
        ret = createMock(Retriever.class);
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /** test the single bean */
    public void testQueryResult() throws Exception {
        replay(fileObject,ret);
        final FileObjectView v1 = new FileObjectView(fileObject,null);
        final QueryResult queryResult = new QueryResults.QueryResult(ret,v1);        
        assertNull(queryResult.error);
        assertEquals(QueryResults.PENDING,queryResult.count);
        assertSame(ret,queryResult.retriever);
        assertSame(v1,queryResult.resultsDir);
        assertEquals("Pending",queryResult.getFormattedResultCount());
        queryResult.count = new Integer(5);
        assertEquals(queryResult.count,queryResult.getFormattedResultCount());
        queryResult.error = "ERROR";
        assertEquals("Failed",queryResult.getFormattedResultCount());        
        verify(fileObject,ret);
        
    }
    /** test the collection.*/
    public void testQueryResults() throws Exception {
        final TreeNode treeNode = createMock(TreeNode.class);
        final QueryResults qr = new QueryResults();
        replay(fileObject,ret,treeNode);
        final FileObjectView v1 = new FileObjectView(fileObject,null);
        final QueryResult queryResult = new QueryResults.QueryResult(ret,v1); 
        
        // adding and removing query results.
        assertNull(qr.getResult(ret));
        qr.addResult(queryResult);
        assertSame(queryResult,qr.getResult(ret));
        
        qr.clear();
        assertNull(qr.getResult(ret));
        
        // association between tree nodes and retrievers.
        assertNull(qr.findRetriever(treeNode));
        assertNull(qr.findTreeNode(ret));
        //add the result
        qr.addResult(queryResult);
        assertNull(qr.findRetriever(treeNode));
        assertNull(qr.findTreeNode(ret));        
        // add an association
        qr.associateNode(queryResult,treeNode);
        assertSame(ret,qr.findRetriever(treeNode));
        assertSame(treeNode,qr.findTreeNode(ret));
        // clear clears associations too.
        qr.clear();
        assertNull(qr.findRetriever(treeNode));
        assertNull(qr.findTreeNode(ret)); 
        
// erroneious inputs.        
        // associating a non-existent result.
        try {
            qr.associateNode(queryResult,treeNode);
            fail("expected to fail");
        } catch (final IllegalArgumentException e) {
            // ok.
        }
        try {
            qr.associateNode(null,treeNode);
            fail("expected to fail");
        } catch (final IllegalArgumentException e) {
            // ok.
        }
        try {
            qr.associateNode(queryResult,null);
            fail("expected to fail");
        } catch (final IllegalArgumentException e) {
            // ok.
        }        
        try {
            qr.addResult(null);
            fail("expected to fail");
        } catch (final IllegalArgumentException e) {
            // ok.
        }        
        // passing in null
        assertNull(qr.getResult(null));
        assertNull(qr.findTreeNode(null));
        assertNull(qr.findRetriever(null));
        verify(fileObject,ret,treeNode);
    }

}
