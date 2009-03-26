/*$Id: RemoteFinderIntegrationTest.java,v 1.5 2009/03/26 18:01:22 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/** test finder can connect via rmi to an already running acr
 * the acr happens to be in the same process, but that isn't important.
 * 
 * although this extends from a 'separate test', this test is well behaved, and can be run in conjunction with others.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 28-Jul-2005
 *
 */
public class RemoteFinderIntegrationTest  extends InProcessFinderSeparateTest{

    /** Construct a new RemoteFinderTest
     * 
     */
    public RemoteFinderIntegrationTest() {
        super();
    }
    
    @Override
    protected void tearDown() {
        // override superclass, and don't call it - fixtue takes care of tearing down.
    }
    
    /** start a fixture that creates an acr before running the tests */
    public static Test suite() {
        return new ARTestSetup(new TestSuite(RemoteFinderIntegrationTest.class));
    }

}


/* 
$Log: RemoteFinderIntegrationTest.java,v $
Revision 1.5  2009/03/26 18:01:22  nw
added override annotations

Revision 1.4  2007/01/29 10:38:40  nw
documentation fixes.

Revision 1.3  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/