/*$Id: InstallationSelfCheck.java,v 1.3 2003/12/09 12:36:01 nw Exp $
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

import java.lang.reflect.Constructor;

import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.util.Workspace;

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
    

    
    public void testLoadPlugin() {
       String pluginClass = SimpleConfig.getProperty(QuerierManager.DATABASE_QUERIER_KEY);
       assertNotNull(QuerierManager.DATABASE_QUERIER_KEY + " is not defined",pluginClass);
       // try to load plugin class.
       Class plugin = null;
       try {
          plugin = Class.forName(pluginClass);
          assertNotNull(QuerierManager.DATABASE_QUERIER_KEY + " could not be found",plugin);
          // check its type
          assertTrue(QuerierManager.DATABASE_QUERIER_KEY + " does not extend Querier",Querier.class.isAssignableFrom(plugin));
          // we expect a contructor as follows
          Constructor constr = plugin.getConstructor(new Class[]{String.class,_query.class});
          assertNotNull("Plugin class must provide constructor(String,_query)",constr);
          // if its not the plugin querier, then we stop.
          if (! PluginQuerier.class.isAssignableFrom(plugin)) {
             return;
          }
       } catch (Exception e) {
          fail("Pluigin class " + plugin.getName() + "could not be loaded");
       }
        // else, go on and check the SPI too.
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
    
   public void testCanCreateWorkspace() {
      try {
      Workspace ws = new Workspace("test-workspace");
      assertNotNull("Could not create test workspace - returned null", ws);
      ws.close();      
      } catch (Exception e) {
         fail("Could not create test workspace");
      }
   }
   /** @todo implement this to check than myspace is accessible */
   public void testCanSeeMySpace() {
      try {
      } catch (Exception e) {
         fail("Could not connect to myspace");
      }
   }
   

}


/*
$Log: InstallationSelfCheck.java,v $
Revision 1.3  2003/12/09 12:36:01  nw
improved installation-tests

Revision 1.2  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.1  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested
 
*/
