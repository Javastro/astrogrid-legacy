/*
 * Created on 15-Feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.store.Ivorn;


/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeTest extends FileSystemTestCase {
    
    private Ivorn accountSpaceIvorn ;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TreeTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        this.accountSpaceIvorn = null ;
        super.tearDown();
    }

    public void testNewTree() throws Exception {
        FileManagerClient fmClient = newFileSystem() ;
        Tree tree = Tree.constructTree( this.accountSpaceIvorn, fmClient ) ;
        assertNotNull( tree ) ;
        // System.out.println( "tree.toXmlString(): \n" + tree.toXmlString() ) ;
        File queryFile05 = tree.getFile( "home/query-1/query-1.1/query-05.adql" ) ;
        assertNotNull( queryFile05 ) ;
        System.out.println( "Path for queryFile05: " + queryFile05.path() ) ;
        File queryFile10 = tree.getFile( "home/query-1/query-1.1/query-1.1.1/query-10.adql" ) ;
        assertNotNull( queryFile10 ) ;
        System.out.println( "Path for queryFile10: " + queryFile10.path() ) ;
        File workflowFile08 = tree.getFile( "home/workflow-1/workflow-1.1/workflow-1.1.1/workflow-1.1.1.1/workflow-08.wf" ) ;
        assertNotNull( workflowFile08 ) ;
        System.out.println( "Path for workflowFile08: " + workflowFile08.path() ) ;
        File workflowFile07x = tree.getFile( "home/workflow-1/workflow-1.1/workflow-1.1.2/workflow-07x.wf" ) ;
        assertNotNull( workflowFile07x ) ;
        System.out.println( "Path for workflowFile07x: " + workflowFile07x.path() ) ;
        File file793 = tree.getFile( "home/big/dir-74/file-793" ) ;
        assertNotNull( file793 ) ;
        System.out.println( "Path for file793: " + file793.path() ) ;
        File workflowFileZZZZ = tree.getFile( "home/workflow-1/workflow-1.1/workflow-1.1.2/workflow-ZZZZ.wf" ) ;
        assertNull( workflowFileZZZZ ) ;
    }

    public void testTree() throws Exception {
        Tree tree= newTree() ;
        assertNotNull( tree ) ;
    }

    public void testBuildBranch() throws Exception {
        Tree tree = newTree() ;
        Directory 
        	queryDirectory = tree.getDirectory( "home/query/" ),
        	querySubDirectory = null ;
        assertNotNull( queryDirectory ) ;
        tree.constructBranch( queryDirectory ) ;
        querySubDirectory = tree.getDirectory( "home/query/query-sub/" ) ;
        assertNotNull( querySubDirectory ) ;
        File queryFile04 = tree.getFile( "home/query/query-sub/query-04.adql" ) ;
        System.out.println( "Path for queryFile04: " + queryFile04.path() ) ;
        assertNotNull( queryFile04 ) ;
    }

    public void testNewDirectory() throws Exception {
        Tree tree = newTree() ;
        Directory directory = tree.getDirectory( "home/workflow/" ) ;
        Directory newDirectory = tree.newDirectory( "workflow-new", "home/workflow/") ;
        assertNotNull( newDirectory ) ;
        newDirectory = tree.newDirectory( "votables", "home/") ;
        assertNotNull( newDirectory ) ;
        System.out.println( "Path for new directory votables: " + newDirectory.path() ) ;
    }

    public void testGetFile() throws Exception {
        Tree tree = newTree() ;
        File file = tree.getFile( "home/workflow/workflow-03.wf" ) ;
        assertNotNull( file ) ;
    }

    public void testGetDirectory() throws Exception {
        Tree tree = newTree() ;
        Directory directory = tree.getDirectory( "home/workflow/" ) ;
        assertNotNull( directory ) ;
    }

    public void _testNewFile() {
    }

    public void testToXmlString() throws Exception {
        Tree tree = newTree() ;
        String xmlString = tree.toXmlString() ;
        System.out.println( "xmlString: " + xmlString ) ;
    }

    public void _testGetAccountSpace() {
    }

    public void _testSetAccountSpace() {
    }

    public void testFormNodeIvorn() throws Exception {
        Tree tree = newTree() ;
        Ivorn ivorn = tree.formNodeIvorn( tree.getFile( "home/workflow/workflow-03.wf" ).path() ) ;
        System.out.println( "Ivorn.toString(): " + ivorn.toString() ) ;
    }

    public void testToTreeXmlString() throws Exception {
        Tree tree = newTree() ;
        String treeXml = tree.toTreeViewXmlString(new StringBuffer(256) ).toString() ;
        System.out.println( "toTreeXmlString(): " + treeXml ) ;
    }

    public void testToDetailXmlString() throws Exception {
        Tree tree = newTree() ;
        String detailXml = tree.toDetailsViewXmlString() ;
        assertNull( detailXml ) ;
    }

    public void _testDirectory() {
    }

    public void testIsEmpty() throws Exception {
        Tree tree = newTree() ;
        assert( tree.isEmpty() == false ) ;
    }

    public void _testAdd() {
    }

    public void _testGetException() {
    }

    public void _testSetException() {
    }

    public void _testIsFilled() {
    }

    public void _testSetFilled() {
    }

    public void _testIsValid() {
    }

    public void _testSetValid() {
    }

    public void _testGetSafeName() {
    }

    public void _testGetFolderPath() {
    }


    public void testGetNode() throws Exception {
        Tree tree = newTree() ;
        FileManagerNode node = tree.getNode() ;
        assertNotNull( node ) ;
    }

    public void testPath() throws Exception {
        Tree tree = newTree() ;
        String path = tree.path().toString() ;
        System.out.println( "path: " + path ) ;
        assertEquals( path, "home/") ;
    }

    public void testIsRoot() throws Exception {
        Tree tree = newTree() ;
        assert( tree.isRoot() ) ;
    }

    public void testGetRoot() throws Exception {
        Tree tree = newTree() ;
        Directory root = tree.getRoot() ;
        assertNotNull ( root ) ;
    }

    public void testIsTree() throws Exception {
        Tree tree = newTree() ;
        assert( tree.isTree() ) ;
    }

    public void testGetTree() throws Exception {
        Tree tree = newTree().getTree() ;
        assertNotNull( tree ) ;
    }

    public void testGetParent() throws Exception {
        Tree tree = newTree() ;
        Directory directory = tree.getParent() ;
        assertNull( directory ) ;
    }
    
    private Tree newTree() throws Exception {
        //
        // Register our accounts.
        Ivorn ivorn = register();
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory 
        	factory = new FileManagerClientFactory( getFileManagerResolver() );
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login( ivorn, ACCOUNT_PASSWORD );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode 
			workflowNode = home.addNode( "workflow"	),
			queryNode = home.addNode( "query" ),
			querySubNode = queryNode.addNode( "query-sub") ;
		
		home.addFile( "config.txt" ) ;
		
		//
		// Add files.
		FileManagerNode 
			workflow01 = workflowNode.addFile( "workflow-01.wf" ),
			workflow02 = workflowNode.addFile( "workflow-02.wf" ),
			workflow03 = workflowNode.addFile( "workflow-03.wf" ),
			workflow04 = workflowNode.addFile( "workflow-04.wf" ),
			workflow05 = workflowNode.addFile( "workflow-05.wf" ),
			query01 = querySubNode.addFile( "query-01.adql" ),
			query02 = querySubNode.addFile( "query-02.adql" ),
			query03 = querySubNode.addFile( "query-03.adql" ),
			query04 = querySubNode.addFile( "query-04.adql" ),
			query05 = querySubNode.addFile( "query-05.adql" ),
			query06 = querySubNode.addFile( "query-06.adql" ) ;
		
		Tree tree = new Tree( ivorn, client, home ) ;
		
		Directory 
			workflowDirectory = tree.constructDirectory( workflowNode, tree ),
			queryDirectory = tree.constructDirectory( queryNode, tree ) ;
	
		File 
			file01 = tree.constructFile( workflow01, workflowDirectory ),
			file02 = tree.constructFile( workflow02, workflowDirectory ),
			file03 = tree.constructFile( workflow03, workflowDirectory ),
			file04 = tree.constructFile( workflow04, workflowDirectory ),
			file05 = tree.constructFile( workflow05, workflowDirectory ) ;
			
		workflowDirectory
			.add( file01 )
			.add( file02 )
			.add( file03 )
			.add( file04 )
			.add( file05 ) ;

		return tree ;
		
    } // end of newTree()

    
    private FileManagerClient newFileSystem() throws Exception {
        //
        // Register our accounts.
        this.accountSpaceIvorn = register();
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory 
        	factory = new FileManagerClientFactory( getFileManagerResolver() );
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login( this.accountSpaceIvorn, ACCOUNT_PASSWORD );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode 
			workflowNode_1 = home.addNode( "workflow-1"	),
			workflowNode_1_1 = workflowNode_1.addNode( "workflow-1.1"	),
			workflowNode_1_1_1 = workflowNode_1_1.addNode( "workflow-1.1.1"	),
			workflowNode_1_1_2 = workflowNode_1_1.addNode( "workflow-1.1.2"	),
			workflowNode_1_1_1_1 = workflowNode_1_1_1.addNode( "workflow-1.1.1.1"	),
			workflowNode_1_1_1_1_1 = workflowNode_1_1_1_1.addNode( "workflow-1.1.1.1.1"	),
			queryNode_1 = home.addNode( "query-1" ),
			queryNode_1_1 = queryNode_1.addNode( "query-1.1" ),
			queryNode_1_1_1 = queryNode_1_1.addNode( "query-1.1.1" ),
			bigNode = home.addNode( "big"),
			node = null ;
		
		// Just to complicate things, here is a bit of capacity...
		for( int i=0; i < 300; i++ ) {
		    node = bigNode.addNode( "dir-" + i ) ;
		    if( i%37 == 0 ) {
		        for( int j=0; j < 1000; j++ ) {
			        node.addFile( "file-" + j ) ;
			    }
		    }
		    else if( i%3 == 0 ) {
		        node.addFile( "file-" + i ) ;
		    }
		    
		}
		
		
		//
		// Add files.
		FileManagerNode 
			workflow01 = workflowNode_1.addFile( "workflow-01.wf" ),
			workflow02 = workflowNode_1.addFile( "workflow-02.wf" ),
			workflow03 = workflowNode_1.addFile( "workflow-03.wf" ),
			workflow04 = workflowNode_1.addFile( "workflow-04.wf" ),
			workflow05 = workflowNode_1.addFile( "workflow-05.wf" ),
			workflow06 = workflowNode_1_1.addFile( "workflow-06.wf" ),
			workflow07 = workflowNode_1_1_1.addFile( "workflow-07.wf" ),
			workflow07x = workflowNode_1_1_2.addFile( "workflow-07x.wf" ),
			workflow08 = workflowNode_1_1_1_1.addFile( "workflow-08.wf" ),
			workflow09 = workflowNode_1_1_1_1_1.addFile( "workflow-09.wf" ),
			query01 = queryNode_1_1.addFile( "query-01.adql" ),
			query02 = queryNode_1_1.addFile( "query-02.adql" ),
			query03 = queryNode_1_1.addFile( "query-03.adql" ),
			query04 = queryNode_1_1.addFile( "query-04.adql" ),
			query05 = queryNode_1_1.addFile( "query-05.adql" ),
			query06 = queryNode_1_1.addFile( "query-06.adql" ),
			query07 = queryNode_1_1_1.addFile( "query-07.adql" ),
			query08 = queryNode_1_1_1.addFile( "query-08.adql" ),
			query09 = queryNode_1_1_1.addFile( "query-09.adql" ),
			query10 = queryNode_1_1_1.addFile( "query-10.adql" ),
			query11 = queryNode_1_1_1.addFile( "query-11.adql" ),
			query12 = queryNode_1_1_1.addFile( "query-12.adql" ) ;
		
		return client ;
		
    } // end of newFileSystem()
    
}
