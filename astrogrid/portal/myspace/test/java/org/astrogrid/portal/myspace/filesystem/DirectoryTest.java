/*
 * Created on 15-Feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import java.util.List ;

import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.store.Ivorn;

import junit.framework.TestCase;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DirectoryTest extends FileSystemTestCase {
    
    private Tree tree ;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DirectoryTest.class);
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
        tree = null ;
        super.tearDown();
    }

    public void testToTreeXmlString() throws Exception {
        Directory d = newFileSet() ;
        String treeXml = d.toTreeViewXmlString( new StringBuffer(256) ).toString() ;
        System.out.println( "treeXml: " + treeXml ) ;
    }

    public void testToDetailXmlString() throws Exception {
        Directory d = newFileSet() ;
        String detailXml = d.toDetailsViewXmlString() ;
        System.out.println( "detailXml: " + detailXml ) ; 
    }

    public void testDirectory() throws Exception {
		Directory directory = newFileSet() ;		
		assertNotNull ( directory ) ;
    }

    public void testIsEmpty() throws Exception {
        Directory directory = newFileSet() ;
        assert( directory.isEmpty() == false ) ;
    }

//    public void _testAdd() throws Exception {
//        Directory directory = newFileSet() ;
//        int size = directory.getChildren().size() ;
//		FileManagerNode node = directory.getNode().addFile( "workflow-XX.wf" ) ;
//        directory.add( new File( node, directory ) ) ;
//        assert( directory.getNode().size() == size + 1 ) ;
//    }
//
//    public void _testGetChildren() throws Exception {
//        Directory directory = newFileSet() ;
//        List list = directory.getChildren() ;
//        assertNotNull( list ) ;
//    }
//
//    public void _testSetChildren() throws Exception {
//        Directory directory = newFileSet() ;
//    }

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
        Directory d = newFileSet() ;
        FileManagerNode node = d.getNode() ;
        assertNotNull( node ) ;
    }

    public void testPath() throws Exception {
        Directory d = newFileSet() ;
        String path = d.path().toString() ;
        System.out.println( "path: " + path ) ;
        assertEquals( path, "home/workflow/") ;
    }

    public void testIsRoot() throws Exception {
        Directory d = newFileSet() ;
        assertFalse( d.isRoot() ) ;
    }

    public void testGetRoot() throws Exception {
        Directory d = newFileSet() ;
        assertFalse( d.isRoot() ) ;
    }

    public void testIsTree() throws Exception {
        Directory d = newFileSet() ;
        assertFalse( d.isTree() ) ;
    }

    public void testGetTree() throws Exception {
        Directory d = newFileSet() ;
        Tree tree = d.getTree() ;
        assertNotNull( tree ) ;
    }

    public void testGetParent() throws Exception {
        Directory d = newFileSet() ;
        Directory directory = d.getParent() ;
        assertNotNull( directory ) ;
    }

    private Directory newFileSet() throws Exception {
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
		FileManagerNode workflow = home.addNode( "workflow"	);
		
		this.tree = new Tree( ivorn, client, home ) ;
		Directory directory = new Directory( workflow, tree ) ;
		
		//
		// Add files.
		FileManagerNode 
			workflow01 = workflow.addFile( "workflow-01.wf" ),
			workflow02 = workflow.addFile( "workflow-02.wf" ),
			workflow03 = workflow.addFile( "workflow-03.wf" ),
			workflow04 = workflow.addFile( "workflow-04.wf" ),
			workflow05 = workflow.addFile( "workflow-05.wf" ) ;
		
		File 
			file01 = new File( workflow01, directory ),
			file02 = new File( workflow02, directory ),
			file03 = new File( workflow03, directory ),
			file04 = new File( workflow04, directory ),
			file05 = new File( workflow05, directory ) ;
			
		directory
			.add( file01 )
			.add( file02 )
			.add( file03 )
			.add( file04 )
			.add( file05 ) ;

		return directory ;
		
    } // end of newFileSet()
    
} // end of class DirectoryTest
