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

import org.astrogrid.datacenter.config.Configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WorkspaceTest extends TestCase
{
    /** sets up a working directory */    
    protected void setUp() throws IOException {
        tmpDir = File.createTempFile("workspace-test","dir");
        // gah, want a dir, not a file.
        tmpDir.delete();
        tmpDir.mkdir();
        Configuration.setProperty(Workspace.WORKSPACE_DIRECTORY,tmpDir.getAbsolutePath());

    }
    /** working directory */
    protected File tmpDir;
    /** deletes working directory -- otherwise tests are not repeatable */
    protected void tearDown() {
        if (tmpDir != null && tmpDir.exists()) {
            File[] files = tmpDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            tmpDir.delete();
        }
    }
    
   public void testCreateWorkspace() throws Exception {
       Workspace ws = new Workspace("Test");
       assertNotNull(ws);
       File wsFile = ws.getWorkspace();
       assertNotNull(wsFile);
       assertTrue(wsFile.exists());
       assertTrue(wsFile.isDirectory());
   }
   
   public void testDuplicate() throws Exception {
       Workspace ws1 = new Workspace("dup");
       assertNotNull(ws1);
       try {
       Workspace ws2 = new Workspace("dup");
       fail("Expected workspace to prevent duplicates");
       } catch (IllegalArgumentException e) {}
   }
   /**
    * Tests the workspace functions
    */
   public void testEmptying() throws IOException
   {
      Workspace workspace = new Workspace("Test");

      //make some random files in it
      File workspaceFile = workspace.getWorkspace();
      assertNotNull(workspaceFile);
      File temp1 = File.createTempFile("test",null,workspaceFile);
      File temp2 = File.createTempFile("test",null,workspaceFile);
      File temp3 = File.createTempFile("test",null,workspaceFile);
      File temp4 = File.createTempFile("test",null,workspaceFile);
      File temp5 = File.createTempFile("test",null,workspaceFile);

      //check there are 5 files in there
      File[] contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("5 files created but not 5 files in workspace",5,contents.length);
      
      //empty the workspace
      workspace.empty();
      
      //check there 0 files
      contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("workspace emptied but 0 files in workspace",0,contents.length);

   }
   
   public void testMakeWorkFile() throws IOException {
       Workspace workspace = new Workspace("Test");
       File f = workspace.makeWorkFile("fred");
       assertNotNull(f);     
       assertFalse(f.exists()); // not created, if impl changes, client code must change too.
       assertTrue(f.createNewFile());
       assertTrue(f.exists());
       
   }
   
   /*no prevention from this at the moment
   public void testMakeDuplicateFile() throws IOException {
       Workspace workspace = new Workspace("Test");
       File f= workspace.makeWorkFile("fred");
       assertNotNull(f)
       try {
       File g = workspace.makeWorkFile("fred");
       fail("Created duplicate files");
       } catch (IllegalArgumentException e) {
       }
   }*/
   
   public void testMakeManyFilesAndEmpty() throws IOException {
       Workspace workspace = new Workspace("Test");
       String[] fileNames = new String[] {"Fred","Wilma","Barney","Betty","BamBam"};
       File[] files = new File[5];
       for (int i = 0; i < fileNames.length; i++) {
           File f = workspace.makeWorkFile(fileNames[i]);
           assertNotNull(f);
           assertTrue(f.createNewFile());
           assertTrue(f.exists());
           files[i] = f;
       }
       workspace.empty();
       for (int i = 0; i < files.length; i++) {
           assertFalse(files[i].exists());
       }
   }
   
   /** fals at moment - as close routine commented out of workspace for debugging. this is a reminder to put it back in. */
   public void testOpenAndClose() throws Exception {
       Workspace ws = new Workspace("Test");
       File f = ws.makeWorkFile("foo");
       assertTrue(f.createNewFile());
       assertTrue(f != null && f.exists() && f.isFile());
       ws.close();
      // should also prevent us from creating new files, in one way or another
      try {
          File g = ws.makeWorkFile("bar");
          assertNull(g); // retirn null?
      } catch (IllegalStateException e) {
          // ok, it bombs)
      }
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
