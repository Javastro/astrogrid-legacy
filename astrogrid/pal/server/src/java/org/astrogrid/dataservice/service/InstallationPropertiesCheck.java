/*$Id: InstallationPropertiesCheck.java,v 1.7 2007/02/20 12:22:16 clq2 Exp $
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
      int bad = 0;
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
      if (!checkSet("datacenter.url", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.metadoc.file", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.querier.plugin", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.sqlmaker.xslt", accum)) { bad = bad+1; }
      if (!checkSet("db.trigfuncs.in.radians", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.max.return", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.max.queries", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.sql.timeout", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.self-test.table", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.self-test.column1", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.self-test.column2", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.implements.conesearch", accum)) { bad = bad+1; }
      String cone = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch");
      if ((cone != null) && (cone.toLowerCase().equals("true")) ) {
        if (!checkSet("conesearch.table", accum)) { bad = bad+1; }
        if (!checkSet("conesearch.ra.column", accum)) { bad = bad+1; }
        if (!checkSet("conesearch.dec.column", accum)) { bad = bad+1; }
        if (!checkSet("conesearch.columns.units", accum)) { bad = bad+1; }
        if (!checkSet("conesearch.radius.limit", accum)) { bad = bad+1; }
      }
      if (!checkSet("datacenter.name", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.description", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.authorityId", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.resourceKey", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.publisher", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.contact.name", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.contact.email", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.data.creator.name", accum)) { bad = bad+1; }

      // Assume we need at least one resource (something to publish)
      if (!checkSet("datacenter.resource.plugin.1", accum)) { bad = bad+1; }

      if (!checkSet("org.astrogrid.registry.admin.endpoint", accum)) { bad = bad+1; }
      if (!checkSet("org.astrogrid.registry.query.endpoint", accum)) { bad = bad+1; }
      if (!checkSet("org.astrogrid.registry.query.altendpoint", accum)) { bad = bad+1; }
      if (!checkSet("cea.component.manager.class", accum)) { bad = bad+1; }

      // These ones aren't compulsory:
      // datacenter.ucd.version
      //

      // These ones are used by the jdbc plugin
      if ("org.astrogrid.tableserver.jdbc.JdbcPlugin".equals(plugin)) {
        if (!checkSet("datacenter.plugin.jdbc.user", accum)) { bad = bad+1; }
        if (!checkSet("datacenter.plugin.jdbc.drivers", accum)) { bad = bad+1; }
        if (!checkSet("datacenter.plugin.jdbc.url", accum)) { bad = bad+1; }
        // This one is allowed to be empty - it's possible that the RDBMS
        // has no password
        //allOK = allOK && checkSet("datacenter.plugin.jdbc.password", accum);
      }

      String accumString = "";
      for (int i = 0; i < accum.size(); i++) {
        accumString = accumString + (String)accum.elementAt(i);
      }
      if (bad > 0) {
        allOK = false;
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
