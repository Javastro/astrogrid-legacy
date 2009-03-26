/*$Id: MyspaceSystemTest.java,v 1.12 2009/03/26 18:01:22 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.NodeInformation;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;
import org.astrogrid.io.Piper;

/** Exercise the myspace interface.
 * tests are a bit order dependent - can't be helped really.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Aug-2005
 *
 */
public class MyspaceSystemTest extends InARTestCase {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MyspaceSystemTest.class);
    
    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myspace = assertServiceExists(Myspace.class,"astrogrid.myspace");
        

    }
    @Override
    protected void tearDown() throws Exception {
    	super.tearDown();
    	myspace = null;
    	
    }
    protected static URI testDir;
    protected static URI testFile;
    protected Myspace myspace;
    public static final String TEST_DATA = "some test data";
    protected static File tempFile; 

    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(MyspaceSystemTest.class),true); // login.
    }    

    public void testGetHome() throws Exception{
        URI uri = myspace.getHome();
        assertNotNull(uri);
        
    }

    public void testCreateFolder()throws Exception {
        testDir = new URI("#systemtest/" + this.getClass().getName() + "-" + System.currentTimeMillis());
        testFile = new URI(testDir +"/testfile.txt");             
        assertFalse(myspace.exists(testDir));
        myspace.createFolder(testDir);
        assertTrue(myspace.exists(testDir));           
    }
    
    public void testCreateFile() throws Exception{
        assertFalse(myspace.exists(testFile));
        myspace.createFile(testFile);
        assertTrue(myspace.exists(testFile));        
    }

    public void testCreateChildFolder() throws Exception{
        URI newFolder = myspace.createChildFolder(testDir,"newFolder");        
        assertTrue(myspace.exists(newFolder));
        try { // create a file that already exists.
            newFolder = myspace.createChildFolder(testDir,"newFolder");
            fail("expected to barf");
        } catch (InvalidArgumentException e) {
            // expected
        }
        try { // create a folder as child of a file
            newFolder = myspace.createChildFolder(testFile,"newFolder");
            fail("expected to barf");
        } catch (InvalidArgumentException e) {
            // expected
        }      
    }
    public void testCreateChildFile()throws Exception {
        URI newFile= myspace.createChildFile(testDir,"newFile");
        assertTrue(myspace.exists(newFile));        
        try {
            newFile = myspace.createChildFile(testDir,"newFile");
            fail("expected to barf");
        } catch (ACRException e) {
            // expected
        }
        try {
            newFile = myspace.createChildFile(testFile,"newFile");
            fail("expected to barf");
        } catch (ACRException e) {
            // expected
        }
    }

    public void testGetParent() throws Exception{
        URI home = myspace.getHome();
        assertEquals(home.resolve(testDir),myspace.getParent(testFile)); // parent of a file
        assertEquals(home,myspace.getParent(myspace.getParent(testDir)));
    }
    public void testList() throws Exception{
        String[] names = myspace.list(testDir);
        assertTrue(Arrays.asList(names).contains("testfile.txt"));
        try {
            myspace.list(testFile);
            fail("expected to chuck");
        } catch (ACRException e) {
            // expected.
        }
    }
    public void testListIvorns() throws Exception{
        URI home = myspace.getHome();
        URI[] ivorns = myspace.listIvorns(testDir);
        assertTrue(Arrays.asList(ivorns).contains(home.resolve(testFile)));
        assertFalse(Arrays.asList(ivorns).contains(home.resolve(testDir)));
        try {
            myspace.listIvorns(testFile);
            fail("expected to chuck");
        } catch (ACRException e) { // slightly loose test here - to allow xmlrpc tests to pass too.
            // expected
        }
    }
    
    public void testListNodeInformation() throws Exception{
        URI home = myspace.getHome();
        NodeInformation[] ivorns = myspace.listNodeInformation(testDir);     
    }

    public void testReadWrite() throws Exception {
        myspace.write(testFile, TEST_DATA);
        String result = myspace.read(testFile);
        assertEquals(TEST_DATA,result);
    }
    
    public void testReadWriteBinary() throws Exception {
        myspace.writeBinary(testFile, TEST_DATA.getBytes());
        byte[] result = myspace.readBinary(testFile);
        assertTrue(Arrays.equals(TEST_DATA.getBytes(), result));
    }
    
    
    public void testGetWriteContentURL() throws Exception{
        URL url = myspace.getWriteContentURL(testFile);
        assertNotNull(url);
        FileStoreOutputStream os = new FileStoreOutputStream(url);
        os.open(); // @todo - why is this necesary - breaks the contract.
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
        Piper.pipe(is,os);
        is.close();
        os.close();
        
    }
    
    public void testGetWriteContentURLForFileWithNoContent() throws Exception{
        URI testFile1 = new URI(testFile.toString() + "1");
        assertFalse(myspace.exists(testFile1));
        myspace.createFile(testFile1);
        assertTrue(myspace.exists(testFile1));
        URL url = myspace.getWriteContentURL(testFile1);
        assertNotNull(url);
        FileStoreOutputStream os = new FileStoreOutputStream(url);
        os.open(); // @todo - why is this necesary - breaks the contract.
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
        Piper.pipe(is,os);
        is.close();
        os.close();
        String content = myspace.read(testFile1);
        assertEquals(TEST_DATA,content);
    }    
   
    public void testGetReadContentURL() throws Exception{
        myspace.write(testFile, TEST_DATA);
        URL url = myspace.getReadContentURL(testFile);
        assertNotNull(url);
        FileStoreInputStream is = new FileStoreInputStream(url);
        is.open(); // @todo stupid.
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Piper.pipe(is,os);
        is.close();
        os.close();
        assertEquals(TEST_DATA,os.toString());
    }    

    
    public void testCopyLocalURLToContent() throws Exception{
            tempFile= File.createTempFile("MyspaceSystemTest","tmp");
            tempFile.deleteOnExit();     
        // test on file example.
        tempFile.delete();
        FileOutputStream os = new FileOutputStream(tempFile);
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
        Piper.pipe(is,os);
        is.close();
        os.close();
        myspace.copyURLToContent(tempFile.toURL(),testFile);
                                
    }
        
    public void testCopyContentToLocalURL() throws Exception{
        tempFile.delete();
        myspace.copyContentToURL(testFile,tempFile.toURL());
        FileInputStream is = new FileInputStream(tempFile);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Piper.pipe(is,os);
        is.close();
        os.close();
        assertEquals(TEST_DATA,os.toString());
    }
    
    //@todo problems with testing this probably - timings.
    public void testCopyRemoteURLToContent() throws Exception {
        URL remoteURL = new URL("http://www.astrogrid.org");
        myspace.copyURLToContent(remoteURL, testFile);
        // verify it, - problems with timing, it seems. 
        /*
        URL url = myspace.readContent(testFile);
        assertNotNull(url);
        
        FileStoreInputStream is = new FileStoreInputStream(url);
        is.open();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Piper.pipe(is,os);
        is.close();
        os.close();
        
       // assertTrue(os.toString().indexOf("<html") != -1);
        * */
        
    }
    
    // not easy to test copytContent to remote URL
    
    public void testGetMetadataFolder() throws Exception {
        NodeInformation nd = myspace.getNodeInformation(testDir);
        assertNotNull(nd);
        assertEquals(testDir,nd.getId());
        assertFalse(nd.isFile());
        assertTrue(nd.isFolder());
        assertNotNull(nd.getCreateDate());
        assertNotNull(nd.getModifyDate());
        assertEquals(0,nd.getSize());
    }
      
    public void testGetMetadataFile() throws Exception {
        NodeInformation nd = myspace.getNodeInformation(testFile);
        assertNotNull(nd);
        assertEquals(testFile,nd.getId());
        assertTrue(nd.isFile());
        assertFalse(nd.isFolder());
        assertNotNull(nd.getCreateDate());
        assertNotNull(nd.getModifyDate());
        assertTrue(nd.getSize() > 0);                                
    }    
    
    public void testRefresh() throws Exception{
        myspace.refresh(testDir); // nothing we can observe really - just verify it doesn't throw.
    }



    public void testCopy()throws Exception {
        URI copy = myspace.copy(testFile, testDir, "anotherNewFile");
        assertNotNull(copy);
        assertFalse(copy.equals(testFile));
        assertTrue(myspace.exists(copy));
        assertTrue(myspace.exists(testFile));
        assertEquals(myspace.getParent(copy), myspace.getParent(testFile));
    }

    public void testRename()throws Exception {
        if (!myspace.exists(testFile)) {
            myspace.createFile(testFile);
        }        
        assertTrue(myspace.exists(testFile));
        URI renamed = myspace.rename(testFile,"renamed");
        assertNotNull(renamed);
        assertFalse(myspace.exists(testFile));
        assertTrue(myspace.exists(renamed));
    }

    public void testMove() throws Exception{
        if (!myspace.exists(testFile)) {
            myspace.createFile(testFile);
        }
        assertTrue(myspace.exists(testFile));        
        URI moved =myspace.move(testFile, testDir, "moved");
        assertFalse(myspace.exists(testFile));
        assertTrue(myspace.exists(moved));
    }
    public void testDelete() throws Exception{
        if (!myspace.exists(testFile)) {
            myspace.createFile(testFile);
        }
        myspace.delete(testFile);
        assertFalse(myspace.exists(testFile));
    }


}


/* 
$Log: MyspaceSystemTest.java,v $
Revision 1.12  2009/03/26 18:01:22  nw
added override annotations

Revision 1.11  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.10  2007/03/08 17:44:01  nw
first draft of voexplorer

Revision 1.9  2007/01/29 10:42:48  nw
tidied.

Revision 1.8  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.7  2007/01/09 16:12:19  nw
improved tests - still need extending though.

Revision 1.6  2006/08/31 21:06:36  nw
doc fixes

Revision 1.5  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.4  2005/10/13 18:33:47  nw
fixes supporting getWriteContentURL

Revision 1.3  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.2  2005/08/16 13:19:32  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/