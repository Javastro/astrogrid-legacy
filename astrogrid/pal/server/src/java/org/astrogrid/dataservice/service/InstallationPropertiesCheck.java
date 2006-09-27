/*$Id: InstallationPropertiesCheck.java,v 1.5 2006/09/27 13:08:55 kea Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service;

import java.util.Vector;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.tableserver.test.SampleStarsPlugin;


/** Unit test for checking an installation - checks that all properties
 * are at least present.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 * (MCH Dec ratpak - removed 'fails' so that original exceptions propogate nicely
 * to nice web self-test page)
 */
public class InstallationPropertiesCheck extends TestCase {
   
   /** Checks that all expected properties are set */
   public void testAllPropertiesSet()
   {
      boolean allOK = true;
      String test;
      Vector accum = new Vector();

      String plugin;
      try {
        plugin = ConfigFactory.getCommonConfig().getString(
              "datacenter.querier.plugin");
      }
      catch (PropertyNotFoundException e) {
        // Ignore this one so we can report it to the user later
        plugin = null;
      }

      // Catch any properties set by the sample plugin
      if ("org.astrogrid.tableserver.test.SampleStarsPlugin".equals(plugin)) {
         SampleStarsPlugin.initConfig();
      }

      // Check all properties are set
      allOK = allOK && checkSet("datacenter.url", accum);
      allOK = allOK && checkSet("datacenter.metadoc.file", accum);
      allOK = allOK && checkSet("datacenter.querier.plugin", accum);
      allOK = allOK && checkSet("datacenter.sqlmaker.xslt", accum);
      allOK = allOK && checkSet("db.trigfuncs.in.radians", accum);

      allOK = allOK && checkSet("datacenter.max.return", accum);
      allOK = allOK && checkSet("datacenter.max.queries", accum);
      allOK = allOK && checkSet("datacenter.sql.timeout", accum);

      allOK = allOK && checkSet("datacenter.self-test.table", accum);
      allOK = allOK && checkSet("datacenter.self-test.column1", accum);
      allOK = allOK && checkSet("datacenter.self-test.column2", accum);

      allOK = allOK && checkSet("datacenter.implements.conesearch", accum);
      allOK = allOK && checkSet("conesearch.table", accum);
      allOK = allOK && checkSet("conesearch.ra.column", accum);
      allOK = allOK && checkSet("conesearch.dec.column", accum);
      allOK = allOK && checkSet("conesearch.columns.units", accum);

      allOK = allOK && checkSet("datacenter.name", accum);
      allOK = allOK && checkSet("datacenter.description", accum);

      allOK = allOK && checkSet("datacenter.authorityId", accum);
      allOK = allOK && checkSet("datacenter.resourceKey", accum);

      // Assume we need at least one resource (something to publish)
      allOK = allOK && checkSet("datacenter.resource.plugin.1", accum);

      allOK = allOK && checkSet("org.astrogrid.registry.admin.endpoint", accum);
      allOK = allOK && checkSet("org.astrogrid.registry.query.endpoint", accum);
      allOK = allOK && checkSet("org.astrogrid.registry.query.altendpoint", accum);
      allOK = allOK && checkSet("cea.component.manager.class", accum);
      // These ones aren't compulsory:
      // datacenter.publisher
      // datacenter.contact.name
      // datacenter.contact.email
      // datacenter.ucd.version
      //

      // These ones are used by the jdbc plugin
      if ("org.astrogrid.tableserver.jdbc.JdbcPlugin".equals(plugin)) {
        allOK = allOK && checkSet("datacenter.plugin.jdbc.user", accum);
        allOK = allOK && checkSet("datacenter.plugin.jdbc.password", accum);
        allOK = allOK && checkSet("datacenter.plugin.jdbc.drivers", accum);
        allOK = allOK && checkSet("datacenter.plugin.jdbc.url", accum);
      }

      String accumString = "";
      for (int i = 0; i < accum.size(); i++) {
        accumString = accumString + (String)accum.elementAt(i);
      }
      assertTrue("SOME PROPERTIES ARE NOT SET!<br/>\n" + accumString,allOK);
   }

   protected boolean checkSet(String name, Vector accum)
   {
      String property;
      try {
         property = ConfigFactory.getCommonConfig().getString(name);
      } 
      catch (PropertyNotFoundException e) {
        // Ignore this one so we can report it to the user later
        property = null;
      }
      if (property == null) {
        accum.add(
          "<br/>\nProperty '" + name + "' is not set, please set it!"); 

        return false;
      }
      if (property.equals("")) {
        accum.add(
          "<br/>\nProperty '" + name + "' is not set, please set it!"); 
        return false;
      }
      return true;
   }
}
