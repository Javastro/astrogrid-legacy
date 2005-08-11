/*$Id: RemoteFinderTest.java,v 1.1 2005/08/11 10:15:01 nw Exp $
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

import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** test finder can connect via rmi to an already running acr
 * the acr happens to be in the same process, but that isn't important.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class RemoteFinderTest  extends InProcessFinderTest{

    /** Construct a new RemoteFinderTest
     * 
     */
    public RemoteFinderTest() {
        super();
    }
    
    protected void tearDown() {
        // override superclass, and don't call it - fixtue takes care of tearing down.
    }
    
    /** start a fixture that creates an acr before running the tests */
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RemoteFinderTest.class));
    }

}


/* 
$Log: RemoteFinderTest.java,v $
Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/