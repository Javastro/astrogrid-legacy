/*
 * $Id: TabularDbResources.java,v 1.2 2005/03/10 22:39:17 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

/**
 * Generates Resource elements for tabular sky services.
 */

public class TabularDbResources extends VoResourceSupport implements VoResourcePlugin  {
   
   
   /** Generates a voResource element about the database.
    *  */
   public String getVoResource() throws IOException {

      TableMetaDocInterpreter reader = new TableMetaDocInterpreter();

      StringBuffer tabularDb = new StringBuffer(
         makeVoResourceElement("tdb:TabularDB")+
         makeCore("TDB")+
         "<tdb:db>"
      );
      
      //only one catalog in resource at the moment
      String catalog = reader.getCatalogs()[0];
      tabularDb.append(
         "<tdb:name>"+catalog+"</tdb:name>"+
         "<tdb:description/>"
      );
      
      TableInfo[] tables = reader.getTables(catalog);
      for (int t = 0; t < tables.length; t++) {
         tabularDb.append(
            "<tdb:table>"+
               "<name>"+tables[t].getName()+"</name>"+
               "<description>"+XmlPrinter.transformSpecials(tables[t].getDescription())+"</description>"
         );
         
         ColumnInfo[] columns = reader.getColumns(catalog, tables[t].getName());
         
         for (int c = 0; c < columns.length; c++) {
            tabularDb.append(
               "<column>"+
                  "<name>"+columns[c].getName()+"</name>"+
                  "<description>"+XmlPrinter.transformSpecials(columns[c].getDescription())+"</description>"+
                  "<ucd>"+columns[c].getUcd("1")+"</ucd>"+
                  "<unit>"+columns[c].getUnits()+"</unit>"+
               "</column>"
            );
         }
         
         tabularDb.append(
            "</tdb:table>");
         
      }

      tabularDb.append(
         "</tdb:db>"+
         "</"+VORESOURCE_ELEMENT+">");
      
      return tabularDb.toString();
   }
   
}


