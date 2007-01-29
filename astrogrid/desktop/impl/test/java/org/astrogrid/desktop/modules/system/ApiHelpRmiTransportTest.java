/*$Id: ApiHelpRmiTransportTest.java,v 1.4 2007/01/29 10:42:48 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.TestingFinder;

/** repeat tests on apiHelp component, but using java rmi mechanism.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Aug-2005
 *
 */
public class ApiHelpRmiTransportTest extends ApiHelpIntegrationTest {
    protected ACR getACR() throws Exception{
        return (new TestingFinder()).find();
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ApiHelpRmiTransportTest.class));
    }
}


/* 
$Log: ApiHelpRmiTransportTest.java,v $
Revision 1.4  2007/01/29 10:42:48  nw
tidied.

Revision 1.3  2007/01/23 20:07:33  nw
fixes to use subclass of finder, and to work in a hub setting.

Revision 1.2  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.1  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/