/*
 * $Id: TabularSkyServicePlugin.java,v 1.2 2005/03/08 18:05:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;

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
   public String getVoResource() throws IOException {

      //return empty list for now
      return "";
   }


   /** Generates a voResource element about the database.
    *  */
   public String makeTabularSkySurveyResource(DatabaseMetaData metadata) throws IOException {

      StringWriter sw = new StringWriter();
      try {
         /** Alternative XmlWriter form */
         XmlAsciiWriter xw = new XmlAsciiWriter(sw, false);

         XmlPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='DataService'" });

         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   
}


