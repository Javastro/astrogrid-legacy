/*$Id: InstallationSelfCheck.java,v 1.2 2003/12/01 20:58:42 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter;

import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.service.AxisDataServer;

/** Unit test for checking an installation - checks location of config files, etc.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class InstallationSelfCheck extends TestCase {

    /**
     * Constructor for InstallationSelfCheck.
     * @param arg0
     */
    public InstallationSelfCheck(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(InstallationSelfCheck.class);
    }

    public void testInstantiateServer() {
        // needs to come first.
        AxisDataServer serv = new AxisDataServer(); // loads properties in.
        
    }
    

    
    public void testLoadSpi() {
        String spiClass = SimpleConfig.getProperty(PluginQuerier.QUERIER_SPI_KEY);
        assertNotNull(PluginQuerier.QUERIER_SPI_KEY + " is not defined",spiClass);
        Class clazz = null;
        try {
            clazz = Class.forName(spiClass);
            assertNotNull("SPI class " + spiClass + " could not be found",clazz);
        } catch (Exception e) {
            fail("SPI class " + spiClass + " could not be found");
        }
        // try to instantiate it.
        try {
            Object o = clazz.newInstance();
        } catch (Exception e) {
            fail("Could not instantiate SPI Object of class " + spiClass);
        }
        
    }
    
    public void testDatabaseConnection() {
    }
   

}


/*
$Log: InstallationSelfCheck.java,v $
Revision 1.2  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.1  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested
 
*/
