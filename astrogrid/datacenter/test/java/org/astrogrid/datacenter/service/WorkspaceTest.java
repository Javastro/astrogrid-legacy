/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.datacenter.service;


/**
 * Unit tests for the Workspace class
 *
 * @author M Hill
 */

import java.io.File;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.config.Configuration;

public class WorkspaceTest extends TestCase
{
    /** working directory */
    protected File tmpDir;

   /** Constructor makes a temporary directory to create workspaces in. *Could*
    * do this in setUp(), but that applies to each test, whereas Workspaces
    * should be running in a simple single 'working' directory.
    * It appears a new instance is run for every test...
    */
   public WorkspaceTest(String s) throws IOException
   {
        super(s);
      
        Workspace.PERSIST = false; //make sure they tidy up properly

      String workspaceProperty = Configuration.getProperty(Workspace.WORKSPACE_DIRECTORY);
      
      if ( workspaceProperty == null)
      {
        //specify a working directory area - need to do this so we can
         //find the workspace to examine the contents
        tmpDir = File.createTempFile("workspace-test","");
        // gah, want a dir, not a file.
        tmpDir.delete();
        tmpDir.mkdir();
      
        //set the configuratio property so Workspace can find it
        Configuration.setProperty(Workspace.WORKSPACE_DIRECTORY,tmpDir.getAbsolutePath());
      }
      else
      {
         tmpDir = new File(workspaceProperty);
      }
   }
    
    /** deletes working directory -- otherwise tests are not repeatable
    protected void tearDown() {
        if (tmpDir != null && tmpDir.exists()) {
            File[] files = tmpDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            tmpDir.delete();
        }
    }
    */
 
 
   public void testCreateWorkspace() throws Exception {
       Workspace ws = new Workspace("TestCreate");
       assertNotNull(ws);
       File wsFile = ws.makeWorkFile("Test");
       assertNotNull(wsFile);
       assertTrue(wsFile.exists());
//       assertTrue(wsFile.isDirectory());
   }
   
   public void testDuplicate() throws Exception {
       Workspace ws1 = new Workspace("TestDuplicateWS");
       assertNotNull(ws1);
       try {
          Workspace ws2 = new Workspace("TestDuplicateWS");
          fail("Expected workspace to prevent duplicates");
       } catch (IllegalArgumentException e) {}
   }
   /**
    * Tests the workspace functions
    */
   public void testEmptying() throws IOException
   {
      Workspace workspace = new Workspace("TestEmptying");
      File workspaceFile = new File(tmpDir+File.separator+"TestEmptying");

      //make some random files in it
      assertNotNull(workspace);
      File temp1 = workspace.makeWorkFile("wibble");
      File temp2 = workspace.makeWorkFile("wobble");
      File temp3 = workspace.makeTempFile("test");
      File temp4 = workspace.makeTempFile("test");
      File temp5 = workspace.makeTempFile("test");

      //check there are 5 files in there
      File[] contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("5 files created but not 5 files in workspace",5,contents.length);
      
      //empty the workspace
      workspace.empty();
      
      //check there 0 files
      contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("workspace emptied but not 0 files in workspace",0,contents.length);

   }
   
   public void testMakeWorkFile() throws IOException {
       Workspace workspace = new Workspace("TestMakeWork");
       File f = workspace.makeWorkFile("fred");
       assertNotNull(f);
       //assertFalse(f.exists()); // not created, if impl changes, client code must change too.
       //assertTrue(f.createNewFile());
       assertTrue(f.exists());
       
   }
   
   public void testMakeDuplicateFile() throws IOException {
       Workspace workspace = new Workspace("TestDuplicateFile");
       File f= workspace.makeWorkFile("fred");
       assertNotNull(f);
       try {
       File g = workspace.makeWorkFile("fred");
       fail("Created duplicate files");
       }
       catch (IllegalArgumentException e) { /* SOK expect a crash */ }
       catch (IOException e) { /* SOK expect a crash */ }
   }
   
   public void testMakeManyFilesAndEmpty() throws IOException {
       Workspace workspace = new Workspace("TestMakeManyAndEmpty");
       String[] fileNames = new String[] {"Fred","Wilma","Barney","Betty","BamBam"};
       File[] files = new File[5];
       for (int i = 0; i < fileNames.length; i++) {
           File f = workspace.makeWorkFile(fileNames[i]);
           assertNotNull(f);
           //assertTrue(f.createNewFile());
           assertTrue(f.exists());
           files[i] = f;
       }
       workspace.empty();
       for (int i = 0; i < files.length; i++) {
           assertFalse(files[i].exists());
       }
   }
   
   /**  */
   public void testOpenAndClose() throws Exception {
      
      //test for given id
       Workspace ws = new Workspace("TestOpenClose");
       File f = ws.makeWorkFile("foo");
       assertTrue(f != null && f.exists() && f.isFile());

      //check directory exists where we expect it to (otherwise test below is meaningless)
      File wsf = new File(tmpDir+File.separator+"TestOpenClose");
      assertTrue(wsf.exists());
      
      ws.close();
      // should also prevent us from creating new files, in one way or another
      try {
          File g = ws.makeWorkFile("bar");
          assertNull(g); // retirn null?
      }
      catch (IllegalStateException e) {  /* ok, it should fail */   }
      catch (AssertionError e) {  /* ok, it should fail */    }
      
      //check directory no longer exists
      wsf = new File(tmpDir+File.separator+"TestOpenClose");
      assertFalse(f.exists());
      
      //test for no id
       ws = new Workspace();
       f = ws.makeWorkFile("foo");
       assertTrue(f != null && f.exists() && f.isFile());
       ws.close();

   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(WorkspaceTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

   
}

/*
$Log: WorkspaceTest.java,v $
Revision 1.8  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.7  2003/09/08 19:39:55  mch
More bugfixes and temporary file locations

Revision 1.6  2003/09/08 18:35:54  mch
Fixes for bugs raised by WorkspaceTest

Revision 1.5  2003/09/05 13:24:53  nw
added forgotten constructor (is this still needed for unit tests?)

Revision 1.4  2003/09/05 01:03:01  nw
extended to test workspace thoroughly

Revision 1.3  2003/09/04 10:49:16  nw
fixed typo

Revision 1.2  2003/09/04 09:24:32  nw
added martin's changes

Revision 1.1  2003/08/29 15:27:20  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.1  2003/08/27 18:12:35  mch
Workspace tester

*/
