/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.util;


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
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;

public class WorkspaceTest extends TestCase
{
   protected File tmpDir = null; //the area we use to test workspaces in...
   
   public WorkspaceTest(String s)
   {
        super(s);
   }

   /** Set up the workspace area that we are going to create our temporary
    * workspaces in...  We specify this explicitly so that we can look inside
    * the workspace using ordinary java file handling to check the operations
    * have completed correctly
    */
   public static File setUpWorkspaceArea() throws IOException {

      Workspace.PERSIST = false; //make sure they tidy up properly
      String workspaceProperty = SimpleConfig.getSingleton().getString(Workspace.WORKSPACE_DIRECTORY_KEY, null);
      File dir = null;
      if ( workspaceProperty == null)
      {
        //specify a working directory area - need to do this so we can
         //find the workspace to examine the contents
        dir = File.createTempFile("workspace-test","");
        // gah, want a dir, not a file.
        dir.delete();
        dir.mkdir();
    
      }
      else
      {
         dir = new File(workspaceProperty);
      }
      return dir;
   }

   /**
    * Using setUp ensures that the space is only set up once per test.
    * Doing this in the constructor can be difficult as exceptions are not properly caught.
    */
    protected void setUp() throws Exception{
        this.tmpDir = setUpWorkspaceArea();
        //set the configuratio property so Workspace can find it
        SimpleConfig.setProperty(Workspace.WORKSPACE_DIRECTORY_KEY, tmpDir.getAbsolutePath());
    }

    /** deletes working directory -- otherwise tests are not repeatable
     * should be automatic...
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
       ws.close();
   }

   public void testDuplicate() throws Exception {
       Workspace ws1 = new Workspace("TestDuplicateWS");
       assertNotNull(ws1);
       try {
          Workspace ws2 = new Workspace("TestDuplicateWS");
          fail("Expected workspace to prevent duplicates");
       } catch (IllegalArgumentException e) {}
       ws1.close();
   }

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
      workspace.close();
   }

   public void testMakeWorkFile() throws IOException {
       Workspace workspace = new Workspace("TestMakeWork");
       File f = workspace.makeWorkFile("fred");
       assertNotNull(f);
       assertTrue(f.exists());

      f = workspace.makeTempFile("prefix");
      assertNotNull(f);
      assertTrue(f.exists());

      f = workspace.makeTempFile("prefix","suffix");
      assertNotNull(f);
      assertTrue(f.exists());
      
      workspace.close();
   }

   /**
    * Tests that subdirectories are created and removed correctly
    */
   public void testSubspace() throws IOException
   {
      Workspace parentSpace = new Workspace("TestMakeSubWork");
      parentSpace.makeWorkFile("fred");

      File subspace = parentSpace.makeTempDir("thelma");
      File subFile = File.createTempFile("louise", "", subspace);
      assertNotNull(subFile);
      assertTrue(subFile.exists());

      parentSpace.close();

      assertFalse("Should have deleted subfile", subFile.exists());
      assertFalse("Should have deleted subdir", subspace.exists());

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
       
       workspace.close();
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

      String wsId = "TestOpenClose";
      
      //test for given id
       Workspace ws = new Workspace(wsId);
       File f = ws.makeWorkFile("foo");
       assertTrue(f != null && f.exists() && f.isFile());

      //check directory exists where we expect it to (otherwise test below is meaningless)
      File wsf = new File(tmpDir, wsId);
      assertTrue(wsf.exists());

      ws.close();

      // check can't close again
      try {
         ws.close();
         fail("Should not be able to close workspace after its closed");
      }
      catch (IllegalStateException e) {  /* ok, it should fail */   }

      // check can't empty
      try {
         ws.empty();
         fail("Should not be able to empty workspace after its closed");
      }
      catch (IllegalStateException e) {  /* ok, it should fail */   }

      // should also prevent us from creating new files, in one way or another
      try {
         ws.makeWorkFile("bar");
         ws.makeTempFile("prefix","suffix");
         ws.makeTempFile("prefix");

         fail("Should not be able to create workfile after its closed");
      }
      catch (IllegalStateException e) {  /* ok, it should fail */   }
      catch (AssertionError e) {  /* ok, it should fail */    }

      //check directory no longer exists
      assertFalse(wsf.exists());

      //test for no id
       ws = new Workspace();
       f = ws.makeWorkFile("foo");
       assertTrue(f != null && f.exists() && f.isFile());
       ws.close();

   }

   /** If a workspace is not explicitly closed, it should close when the
    * finalise method is called.  This tests the finalise method */
   
   /* I don't think we can count on finalize being called
   public void testFinalise() throws Exception {

      String wsId = "TestFinalise";
      Workspace ws = new Workspace(wsId);
      
      ws.makeWorkFile("fubar");
      ws.makeWorkFile("snafu");

      //can't actually call the finalize method, so leave for visual inspection
      LogFactory.getLog(WorkspaceTest.class).info("Check "+tmpDir+" does not contain TestFinalise");
   }
    */
    
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
Revision 1.6  2004/03/12 16:48:10  mch
Fixes to close workspaces

Revision 1.5  2004/03/01 14:18:35  mch
Fixed test following new Failback config

Revision 1.3  2004/02/17 14:59:14  mch
Fix for new AttomConfig getString throwing exception

Revision 1.2  2004/02/17 03:40:21  mch
Changed to use AttomConfig

Revision 1.1  2003/11/18 11:12:33  mch
Moved Workspace to common

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.12  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.11  2003/09/24 21:13:16  nw
moved setup fromo constructor to setUp() - then can call from other tests.

Revision 1.10  2003/09/17 14:53:02  nw
tidied imports

Revision 1.9  2003/09/15 18:01:45  mch
Better test coverage

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
