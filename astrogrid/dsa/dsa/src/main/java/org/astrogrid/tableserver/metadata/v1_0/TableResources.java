/*
 * $Id: TableResources.java,v 1.2 2009/10/21 19:01:00 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata.v1_0;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.ucd.UcdVersions;

/**
 * Generates Table descriptions for CatalogService catalog resources.
 *
 */

public class TableResources {
   
   /** Generates the table descriptions for the CatalogService registration
    * for the specified catalog.
    */
   public static String getTableDescriptions(String catalogID) 
      throws IOException, MetadataException {

      String ucdVersion = UcdVersions.getUcdVersion();
      StringBuffer tables = new StringBuffer();
      TableInfo[] tablesInfo = TableMetaDocInterpreter.getTablesInfoByID(catalogID);
      for (int t = 0; t < tablesInfo.length; t++) {
         tables.append(
           "  <table>\n" +
           "    <name>"+tablesInfo[t].getName()+"</name>\n" +
           "    <description>"+XmlPrinter.transformSpecials(tablesInfo[t].getDescription())+"</description>\n"
          );
         
         ColumnInfo[] columns = TableMetaDocInterpreter.getColumnsInfoByID(catalogID, tablesInfo[t].getId());
         
         for (int c = 0; c < columns.length; c++) {
            tables.append(
               "    <column>\n"+
               "      <name>"+columns[c].getName()+"</name>\n"+
               "      <description>"+XmlPrinter.transformSpecials(columns[c].getDescription())+"</description>\n"
            );
            if ((columns[c].getUnits() != null) && (columns[c].getUnits().toString().trim().length()>0)) {
               tables.append(
                  "      <unit>"+columns[c].getUnits()+"</unit>\n"
               );
            }
            String columnUcd = columns[c].getUcd(ucdVersion);
            if ((columnUcd != null) && (columnUcd.trim().length()>0)) {
               tables.append(
                  "      <ucd>"+columnUcd.trim()+"</ucd>\n"
               );
            }

            //
            // Patch fix - Moved the xml fragment generation to VoTypes.
            // KONA TOFIX look at the points below
            // Todo - Need access to the column size for strings.
            // Todo - Need to create a VoType format for dates.
            tables.append("    "+
                VoTypes.getVoTypeXml(columns[c].getJavaType()) + "\n"
            );
            tables.append("    </column>\n");
         }
         tables.append("  </table>\n");
      }
      return tables.toString();
   }
}


