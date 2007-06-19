/*$Id: InstallationPropertiesCheck.java,v 1.10 2007/06/19 11:42:51 clq2 Exp $
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
   
   // Which plugin to use
   protected String plugin = null;

   // Sets up initial configuration for samplestars if required
   protected void initConfig()
   {
      try {
         plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
      }
      catch (PropertyNotFoundException e) {
         // PropertiesCheck will catch this case
         plugin = null;
      }
      // Catch any properties set by the sample plugin
      if ("org.astrogrid.tableserver.test.SampleStarsPlugin".equals(plugin)) {
         SampleStarsPlugin.initConfig();
      }
   }
   /** Checks that all expected properties are set */

   public void testAllPropertiesSet()
   {
      boolean allOK = true;
      int bad = 0;
      String test;
      Vector accum = new Vector();

      initConfig();

      // Check all properties are set
      if (!checkSet("datacenter.url", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.metadoc.file", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.querier.plugin", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.sqlmaker.xslt", accum)) { bad = bad+1; }
      if (!checkSet("db.trigfuncs.in.radians", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.max.return", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.sql.timeout", accum)) { bad = bad+1; }

      //if (!checkSet("datacenter.max.queries", accum)) { bad = bad+1; }
      if (!checkUnset("datacenter.max.queries", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.max.async.queries", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.max.sync.queries", accum)) { bad = bad+1; }

      // KONA PUT BACK LATER FOR MULTICAT DSAs
      //if (!checkSet("datacenter.self-test.catalog", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.self-test.table", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.self-test.column1", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.self-test.column2", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.implements.conesearch", accum)) { bad = bad+1; }
      String cone = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch");
      if ((cone != null) && (cone.toLowerCase().equals("true")) ) {
        if (!checkSet("conesearch.radius.limit", accum)) { bad = bad+1; }
      }
      // Old conesearch settings - check not present
      if (!checkUnset("conesearch.table", accum)) { bad = bad+1; }
      if (!checkUnset("conesearch.ra.column", accum)) { bad = bad+1; }
      if (!checkUnset("conesearch.dec.column", accum)) { bad = bad+1; }
      if (!checkUnset("conesearch.columns.units", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.name", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.description", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.authorityId", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.resourceKey", accum)) { bad = bad+1; }

      if (!checkSet("datacenter.publisher", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.contact.name", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.contact.email", accum)) { bad = bad+1; }
      if (!checkSet("datacenter.data.creator.name", accum)) { bad = bad+1; }

      // Catch old resource plugin settings (shouldn't be more than 3)
      if (!checkUnset("datacenter.resource.plugin.1", accum)) { bad = bad+1; }
      if (!checkUnset("datacenter.resource.plugin.2", accum)) { bad = bad+1; }
      if (!checkUnset("datacenter.resource.plugin.3", accum)) { bad = bad+1; }

      // Check resource version settings
      if (!checkSet("datacenter.resource.register.v0_10", accum)) { 
         bad = bad+1; 
      }
      else {
         if (!checkValue("datacenter.resource.register.v0_10", "enabled", "Property 'datacenter.resource.register.v0_10' MUST be enabled for now", accum)) { bad = bad+1; }
      }
      if (!checkSet("datacenter.resource.register.v1_0", accum)) { 
         bad = bad+1;
      }
      else {
         if (!checkValue("datacenter.resource.register.v1_0", "disabled", "Property 'datacenter.resource.register.v1_0' MUST be disabled for now", accum)) { bad = bad+1; }
      }
      /*
      // CURRENTLY DON'T HAVE AN AUTHORITY ID PLUGIN!
      // Check for authority ID setting
      if (!checkSet("datacenter.resource.register.authID", accum)) { bad = bad+1; }
      */

      // Check registry and other compulsory settings
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
      assertTrue("SOME PROPERTIES ARE NOT CORRECT!<br/>\n" + accumString,allOK);
   }

   /**
    * Checks that the specified property is set, returning false if it isn't
    * and adding an error message to the accumulation vector. 
    */
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
      if ((property == null) || ("".equals(property)) ) {
        accum.add(
          "<br/>\nProperty '" + name + "' is not set, please set it!"); 

        return false;
      }
      return true;
   }
   /**
    * Checks that the specified property is set to the specified value, 
    * returning false if it isn't and adding an error message to the 
    * accumulation vector. 
    */
   protected boolean checkValue(String name, String value, String error, Vector accum)
   {
      String property;
      try {
         property = ConfigFactory.getCommonConfig().getString(name);
      } 
      catch (PropertyNotFoundException e) {
        // Ignore this one so we can report it to the user later
        property = null;
      }
      if ((property == null) || ("".equals(property)) ) {
        accum.add(
          "<br/>\nProperty '" + name + "' is not set, please set it!"); 
        return false;
      }
      else if (!value.equals(property)) {
         accum.add("<br/>\n"+error);
         return false;
      }
      return true;
   }

   /**
    * Checks that the specified property is unset, returning false if it isn't
    * and adding an error message to the accumulation vector. 
    */
   protected boolean checkUnset(String name, Vector accum)
   {
      String property;
      try {
         property = ConfigFactory.getCommonConfig().getString(name);
      } 
      catch (PropertyNotFoundException e) {
        property = null;   // This is fine
      }
      if ( (property == null) || ("".equals(property)) ) {
         return true;
      }
      else {
        accum.add(
          "<br/>\nProperty '" + name + "' is no longer in use, please remove it from your configuration."); 
        return false;
      }
   }
}
