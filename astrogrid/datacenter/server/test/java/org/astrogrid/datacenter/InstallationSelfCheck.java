/*$Id: InstallationSelfCheck.java,v 1.17 2004/03/13 23:38:56 mch Exp $
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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.community.Account;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.datacenter.service.v041.AxisDataServer_v0_4_1;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.util.Workspace;

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
   
   /** Checks we can create the various interfaces */
   public void testInstantiateServer() throws IOException {
      new AxisDataServer_v0_4_1();
      //new SkyNode();
   }
   
   /** Checks the characteristics of the plugin */
   public void testPluginDefinition() throws Exception {
      String pluginClass = SimpleConfig.getSingleton().getString(QuerierPluginFactory.PLUGIN_KEY);
      assertNotNull(QuerierPluginFactory.PLUGIN_KEY + " is not defined",pluginClass);
      // try to load plugin class.
      Class plugin = null;
//      try {
      plugin = Class.forName(pluginClass);
      assertNotNull(QuerierPluginFactory.PLUGIN_KEY + " could not be found",plugin);
      // check its type
      assertTrue(QuerierPluginFactory.PLUGIN_KEY + " does not extend QuerierPlugin",QuerierPlugin.class.isAssignableFrom(plugin));
      // we expect a contructor as follows
      Constructor constr = plugin.getConstructor(new Class[]{ Querier.class });
      assertNotNull("Plugin class must provide constructor(String,Query)",constr);
   }

   /** Checks the querier/plugin operates - runs a cone query that will exercise it - so
    * this will also test the connection to the backend database. */
   public void testConeSearch() throws Exception {
      StringWriter sw = new StringWriter(); //although we throw away the results
      Querier querier = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,2), sw, QueryResults.FORMAT_VOTABLE);
   }

   public void testCanCreateWorkspace() throws IOException {
         Workspace ws = new Workspace("test-workspace");
         assertNotNull("Could not create test workspace - returned null", ws);
         ws.close();
   }
   
   /** Checks that the default myspace is accessible, if given
    */
   public void testCanSeeMySpace() throws IOException, Exception {

      URL defaultMyspace = SimpleConfig.getSingleton().getUrl(QuerierManager.DEFAULT_MYSPACE, null);

      if (defaultMyspace == null) {
         return;
      }
      
      StoreClient myspace = StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl(new Msrl(defaultMyspace)));
      
      myspace.listFiles("*");
   }
   
   
   /**
    * Checks metadata is OK
    */
   public void testMetadata() throws IOException {
      MetadataServer.getVODescription();
   }
   
}


/*
 $Log: InstallationSelfCheck.java,v $
 Revision 1.17  2004/03/13 23:38:56  mch
 Test fixes and better front-end JSP access

 Revision 1.16  2004/03/13 14:32:25  mch
 Removed Cea test

 Revision 1.15  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.14  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.13  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.12  2004/03/07 00:33:50  mch
 Started to separate It4.1 interface from general server services

 Revision 1.11  2004/03/06 22:34:58  mch
 Added metadata test

 Revision 1.10  2004/03/02 01:32:18  mch
 Replaced Account in StoreClient with User

 Revision 1.9  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

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
