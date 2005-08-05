/*$Id: WebServerRmiTest.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class WebServerRmiTest extends WebServerTest {
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(WebServerRmiTest.class));
    }
}


/* 
$Log: WebServerRmiTest.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/