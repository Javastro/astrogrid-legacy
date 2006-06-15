/*$Id: ConfigurationRmiIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
 * Created on 26-Jul-2005
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
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.ACRTestSetup;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/** perform same tests on configuration component, but this time going through java rmi.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 *
 */
public class ConfigurationRmiIntegrationTest extends ConfigurationIntegrationTest {

   
    
    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ConfigurationRmiIntegrationTest.class));
    }

}


/* 
$Log: ConfigurationRmiIntegrationTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/