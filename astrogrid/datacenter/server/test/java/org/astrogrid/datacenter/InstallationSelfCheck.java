/*$Id: InstallationSelfCheck.java,v 1.8 2004/02/17 03:38:40 mch Exp $
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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.community.Account;
import org.astrogrid.config.AttomConfig;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.util.Workspace;
import org.astrogrid.vospace.delegate.VoSpaceClient;
import org.astrogrid.vospace.delegate.VoSpaceDelegateFactory;

/** Unit test for checking an installation - checks location of config files, etc.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 * (MCH Dec ratpak - removed 'fails' so that original exceptions propogate nicely
 * to nice web self-test page)
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
   
   public void testInstantiateServer() throws IOException {
      // needs to come first.
      AxisDataServer serv = new AxisDataServer(); // loads properties in.
      
   }
   
   
   
   public void testLoadPlugin() throws Exception {
      String pluginClass = AttomConfig.getString(QuerierManager.DATABASE_QUERIER_KEY);
      assertNotNull(QuerierManager.DATABASE_QUERIER_KEY + " is not defined",pluginClass);
      // try to load plugin class.
      Class plugin = null;
//      try {
         plugin = Class.forName(pluginClass);
         assertNotNull(QuerierManager.DATABASE_QUERIER_KEY + " could not be found",plugin);
         // check its type
         assertTrue(QuerierManager.DATABASE_QUERIER_KEY + " does not extend Querier",Querier.class.isAssignableFrom(plugin));
         // we expect a contructor as follows
         Constructor constr = plugin.getConstructor(new Class[]{String.class,Query.class});
         assertNotNull("Plugin class must provide constructor(String,Query)",constr);
         // if its not the plugin querier, then we stop.
         if (! PluginQuerier.class.isAssignableFrom(plugin)) {
            return;
         }
//      } catch (ClassNotFoundException e) {
//         fail("Plugin class " + pluginClass + " not found");
//      } catch (NoSuchMethodException e) {
//         fail("Plugin class "+pluginClass+" missing correct constructor ");
//      }
      // else, go on and check the SPI too.
      String spiClass = AttomConfig.getString(PluginQuerier.QUERIER_SPI_KEY);
      assertNotNull(PluginQuerier.QUERIER_SPI_KEY + " is not defined",spiClass);
      Class clazz = null;
//      try {
         clazz = Class.forName(spiClass);
         assertNotNull("SPI class " + spiClass + " could not be found",clazz);
//      } catch (ClassNotFoundException e) {
//         fail("SPI class " + spiClass + " could not be found");
//      }
      // try to instantiate it.
//      try {
         Object o = clazz.newInstance();
//      } catch (Exception e) {
//         fail("Could not instantiate SPI Object of class " + spiClass);
//      }
      
   }
   
   public void testCanCreateWorkspace() throws IOException {
         Workspace ws = new Workspace("test-workspace");
         assertNotNull("Could not create test workspace - returned null", ws);
         ws.close();
   }
   
   /** Checks that the default myspace is accessible, if given
    */
   public void testCanSeeMySpace() throws IOException, Exception {

      URL defaultMyspace = AttomConfig.getUrl(QuerierManager.DEFAULT_MYSPACE, null);

      if (defaultMyspace == null) {
         return;
      }

      VoSpaceClient myspace = VoSpaceDelegateFactory.createDelegate(Account.ANONYMOUS, defaultMyspace.toString());
      
      myspace.getEntries(Account.ANONYMOUS, "*");
   }
   
   
}


/*
 $Log: InstallationSelfCheck.java,v $
 Revision 1.8  2004/02/17 03:38:40  mch
 Various fixes for demo

 Revision 1.7  2004/02/16 23:07:04  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.6  2004/02/15 23:20:25  mch
 Fixes to use It04.1 myspaces

 Revision 1.5  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.4.4.3  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.4.4.2  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.4.4.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.4  2003/12/16 11:09:00  mch
 Added myspace check, removed some fails to let exceptions through

 Revision 1.3  2003/12/09 12:36:01  nw
 improved installation-tests

 Revision 1.2  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.1  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested
 
 */
