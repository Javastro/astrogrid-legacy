/*
 * $Id: TabularSkyServicePlugin.java,v 1.1 2004/10/08 17:14:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;

/**
 * Generates Resource elements for tabular sky services.
 */

public class TabularSkyServicePlugin implements VoResourcePlugin  {
   
   protected static Log log = LogFactory.getLog(TabularSkyServicePlugin.class);
   
   /** Used in its resource plugin role */
   public TabularSkyServicePlugin() {
   }

   
   
   /** Generates a voResource element about the database.
    *  */
   public String[] getVoResources() throws IOException {

      //return empty list for now
      return new String[] {};
   }


   /** Generates a voResource element about the database.
    *  */
   public String makeTabularSkySurveyResource(DatabaseMetaData metadata) throws IOException {

      StringWriter sw = new StringWriter();
      try {
         /** Alternative XmlWriter form */
         XmlPrinter xw = new XmlPrinter(sw, false);

         XmlTagPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='DataService'" });

         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   
}


