/*$Id: AllAstrogridTransportTests.java,v 1.6 2008/08/04 16:37:24 nw Exp $
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
public class AllAstrogridTransportTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllAstrogridTransportTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Transport Tests for AstroGrid Module");

        suite.addTestSuite(MyspaceRmiTransportTest.class);
        suite.addTestSuite(ApplicationsRmiTransportTest.class);

        return new ARTestSetup(suite,true);       
    }
}


/* 
$Log: AllAstrogridTransportTests.java,v $
Revision 1.6  2008/08/04 16:37:24  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.5  2008/04/23 11:24:19  nw
removed obsolete tests.

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