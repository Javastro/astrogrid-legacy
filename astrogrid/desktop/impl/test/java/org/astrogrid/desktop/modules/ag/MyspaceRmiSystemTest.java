/*$Id: MyspaceRmiSystemTest.java,v 1.2 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** exercise myspace, via rmi interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class MyspaceRmiSystemTest extends MyspaceSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(MyspaceRmiSystemTest.class),true); // login.
    }    
}


/* 
$Log: MyspaceRmiSystemTest.java,v $
Revision 1.2  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/