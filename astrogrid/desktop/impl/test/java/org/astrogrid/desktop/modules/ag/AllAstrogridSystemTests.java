/*$Id: AllAstrogridSystemTests.java,v 1.9 2008/05/28 12:26:18 nw Exp $
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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/** run all the tests of the ag module in a single acr instance.
 * these tests connect to external services - hence are called 'system tests'
 *  - as is possible that will expose errors / temporary failure in remote services, 
 *  rather than local implementation.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Aug-2005
 *
 */
public class AllAstrogridSystemTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllAstrogridSystemTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("System Tests for AstroGrid Module");
        //deprecate myspace now - moving over to vfs.
//        suite.addTestSuite(MyspaceSystemTest.class);     
       suite.addTestSuite(ApplicationsSystemTest.class); 
       suite.addTestSuite(StapSystemTest.class);


        return new ARTestSetup(suite);       
    }
}


/* 
$Log: AllAstrogridSystemTests.java,v $
Revision 1.9  2008/05/28 12:26:18  nw
improved tsts

Revision 1.8  2008/04/23 11:23:16  nw
removed obsolete tests.

Revision 1.7  2007/10/23 09:26:00  nw
RESOLVED - bug 2189: How to query stap services
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2189

Revision 1.6  2007/06/18 16:14:09  nw
commented out myspace system tests - too troublesome, and hopefully not long for this world.

Revision 1.5  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.4  2007/03/08 17:44:01  nw
first draft of voexplorer

Revision 1.3  2007/01/29 10:42:48  nw
tidied.

Revision 1.2  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.1  2007/01/09 16:12:19  nw
improved tests - still need extending though.

Revision 1.2  2006/10/11 10:40:28  nw
removed - won't test deprecated classes.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/