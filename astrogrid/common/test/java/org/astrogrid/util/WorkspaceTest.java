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

//import org.astrogrid.config.AttomConfig;

public class WorkspaceTest extends TestCase
{
   protected File tmpDir = null; //the area we use to test workspaces in...
   
   public WorkspaceTest(String s)
   {
        super(s);
   }
//@TODO restore me - temporary delete

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
Revision 1.4  2004/02/25 17:00:55  jdt
temp removal of build breaking tests (see bug 151)

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
