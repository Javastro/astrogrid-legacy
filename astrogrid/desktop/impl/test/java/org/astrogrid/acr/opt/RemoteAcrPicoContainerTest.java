/*$Id: RemoteAcrPicoContainerTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.opt;

import org.astrogrid.acr.RemoteFinderTest;
import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** test the acr pico container, over a remote acr.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class RemoteAcrPicoContainerTest extends AcrPicoContainerTest {

    /** Construct a new RemoteAcrPicoContainerTest
     * 
     */
    public RemoteAcrPicoContainerTest() {
        super();
    }
    
    /** start a fixture that creates an acr before running the tests */
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RemoteAcrPicoContainerTest.class));
    }

}


/* 
$Log: RemoteAcrPicoContainerTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/