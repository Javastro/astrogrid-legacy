/*
 * $Id: RdbmsMetadataResources.java,v 1.3 2005/03/21 18:45:55 mch Exp $
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
 * Deprecated RdbmsMetadata resource - use TabuluarDB or Queryable when it arrives...
 */

public class RdbmsMetadataResources extends VoResourceSupport implements VoResourcePlugin  {
   
   
   /** Generates a voResource element about the database.
    *  */
   public String getVoResource() throws IOException {

      TableMetaDocInterpreter reader = new TableMetaDocInterpreter();

      StringBuffer tabularDb = new StringBuffer(
         makeVoResourceElement("RdbmsMetadata")+
         makeCore("rdbms")
      );
      
      //only one catalog in resource at the moment
      String catalog = reader.getCatalogs()[0];
      tabularDb.append(
         "<Catalog>"+catalog+"</Catalog>"
      );
      
      TableInfo[] tables = reader.getTables(catalog);
      for (int t = 0; t < tables.length; t++) {
         tabularDb.append(
            "<Table>"+
               "<Name>"+tables[t].getName()+"</Name>"+
               "<Description>"+XmlPrinter.transformSpecials(tables[t].getDescription())+"</Description>"
         );
         
         ColumnInfo[] columns = reader.getColumns(catalog, tables[t].getName());
         
         for (int c = 0; c < columns.length; c++) {
            tabularDb.append(
               "<Column datatype='"+columns[c].getDatatype()+"'>"+
                  "<Name>"+columns[c].getName()+"</Name>"+
                  "<Datatype>"+columns[c].getDatatype()+"</Datatype>"+
                  "<Description>"+XmlPrinter.transformSpecials(columns[c].getDescription())+"</Description>"
            );
            if (columns[c].getUcd("1") != null) {
               tabularDb.append(
                  "<UCD>"+columns[c].getUcd("1")+"</UCD>"
               );
            }
            if (columns[c].getUcd("1") != null) {
               tabularDb.append(
                  "<Units>"+columns[c].getUnits()+"</Units>"
               );
            }
            tabularDb.append(
               "</Column>"
            );
         }
         
         tabularDb.append(
            "</Table>");
         
      }
      
      tabularDb.append(
         "</"+VORESOURCE_ELEMENT+">");
      
      return tabularDb.toString();
   }
   
}


