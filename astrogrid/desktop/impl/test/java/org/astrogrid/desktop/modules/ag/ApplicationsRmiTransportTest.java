/*$Id: ApplicationsRmiTransportTest.java,v 1.2 2007/01/23 11:53:38 nw Exp $
 * Created on 09-Aug-2005
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

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class ApplicationsRmiTransportTest extends ApplicationsSystemTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ApplicationsRmiTransportTest.class),true); // login.
    }        

}


/* 
$Log: ApplicationsRmiTransportTest.java,v $
Revision 1.2  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.1  2007/01/09 16:12:19  nw
improved tests - still need extending though.

Revision 1.2  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/