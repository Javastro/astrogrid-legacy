/*
 * Created on 01-Feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.myspace.filesystem;

import junit.framework.TestCase;
import org.astrogrid.portal.myspace.filesystem.*;
import org.astrogrid.store.Ivorn;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode ;
import org.astrogrid.filemanager.common.exception.*;
import org.apache.log4j.Logger;


/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileTest extends FileSystemTestCase {
    
    private Tree tree ;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FileTest.class);
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
        File file = newFile() ;
        String treeXml = file.toTreeViewXmlString( new StringBuffer(256) ).toString() ;
        System.out.println( "treeXml: " + treeXml ) ;
    }

    public void testToDetailXmlString() throws Exception {
        File file = newFile() ;
        String detailXml = file.toDetailsViewXmlString() ;
        System.out.println( "detailXml: " + detailXml ) ; 
    }

    public void testFile() throws Exception {
		File file = newFile() ;		
		assertNotNull ( file ) ;
    } 
    

    public void testGetSafeName() {
        String safeName = Item.getSafeName( "frog/workflow@qw/work flow-1.wf" ) ;
        System.out.println( "Safe name: " + safeName ) ;
        assert( safeName.equals( "frog_workflow_qw_work_flow-1.wf") ) ;       
    }
    

    public void testGetFolderPath() {
        String folderPath = Item.getFolderPath( "frog/workflow/workflow-1.wf", "workflow-1.wf" ) ;
        System.out.println( "Folder path: " + folderPath ) ;
        assertEquals( folderPath, "frog/workflow/" ) ;
    }
    
    public void testGetFolderPath_DuffParms() {
        String folderPath = Item.getFolderPath( "frog/workflow/workflow-1.wf", "work-1.wf" ) ;
        System.out.println( "Folder path: " + folderPath ) ;
        assertEquals( folderPath, "" ) ;
    }


    public void testGetMyNode() throws Exception {
        File file = newFile() ;
        FileManagerNode node = file.getNode() ;
        assertNotNull( node ) ;
    }


    /*
     * Class under test for StringBuffer path()
     */
    public void testPath() throws Exception {
        File file = newFile() ;
        String path = file.path().toString() ;
        System.out.println( "path: " + path ) ;
        assertEquals( path, "home/workflow/workflow-01.wf") ;
    }

    
    public void testIsRoot() throws Exception {
        File file = newFile() ;
        assertFalse( file.isRoot() ) ;
    }

    
    public void testGetRoot() throws Exception {
        File file = newFile() ;
        assertFalse( file.isRoot() ) ;
    }
    

    public void testIsTree() throws Exception {
        File file = newFile() ;
        assertFalse( file.isTree() ) ;
    }

    public void testGetTree() throws Exception {
        File file = newFile() ;
        Tree tree = file.getTree() ;
        assertNotNull( tree ) ;
    }

    public void testGetParent() throws Exception {
        File file = newFile() ;
        Directory directory = file.getParent() ;
        assertNotNull( directory ) ;
    }
    
    private File newFile() throws Exception {
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

		//
		// Add a file.
		FileManagerNode workflow01 = workflow.addFile( "workflow-01.wf" );
		
		this.tree = new Tree( ivorn, client, home ) ;
		Directory directory = new Directory( workflow, tree ) ;
		File file = new File( workflow01, directory ) ;
		
		return file ;
		
    } // end of newFile()

} // end of class FileTest
