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

public class WorkspaceTest extends TestCase
{
   
   /**
    * Tests the workspace functions
    */
   public void testWorkspace() throws IOException
   {
      Workspace workspace = new Workspace("Test");
      
      File workspaceFile = workspace.getWorkspace();
      assertNotNull(workspaceFile);
      assertTrue(workspaceFile.isDirectory());

      //check you can't make duplicates
      try
      {
         Workspace dup = new Workspace("Test");
         
         throw new RuntimeException("Workspace creation allowed two called 'Test'");
      }
      catch (IllegalArgumentException iae) {} // ignore, correct behaviour
      
      //make some random files in it
      File temp1 = File.createTempFile("test",null,workspaceFile);
      File temp2 = File.createTempFile("test",null,workspaceFile);
      File temp3 = File.createTempFile("test",null,workspaceFile);
      File temp4 = File.createTempFile("test",null,workspaceFile);
      File temp5 = File.createTempFile("test",null,workspaceFile);

      //check there are 5 files in there
      File[] contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("5 files created but not 5 files in workspace",0,contents.lengh);
      
      //empty the workspace
      workspace.empty();
      
      //check there 0 files
      contents = workspaceFile.listFiles();
      assertNotNull(contents);
      assertEquals("workspace emptied but 0 files in workspace",0,contents.length);

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
Revision 1.2  2003/09/04 09:24:32  nw
added martin's changes

Revision 1.1  2003/08/29 15:27:20  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.1  2003/08/27 18:12:35  mch
Workspace tester

*/
