/*
 * $Id: TabularDbResources.java,v 1.3 2007/09/07 09:30:52 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata.v0_10;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.ucd.UcdVersions;
import org.astrogrid.ucd.UcdException;

/**
 * Generates VoResource elements for tabular sky services.
 */

public class TabularDbResources extends VoResourceSupport implements VoResourcePlugin  {
   
   
   /** Generates a voResource element about the database.
    *  */
   public String getVoResource() throws IOException {

      String ucdVersion = UcdVersions.getUcdVersion();

      //TableMetaDocInterpreter reader = new TableMetaDocInterpreter();

      StringBuffer tabularDb = new StringBuffer("");
      
      //String[] catalogNames = reader.getCatalogNames();
      String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();
      String[] catalogDescs = TableMetaDocInterpreter.getCatalogDescriptions();
      if (catalogNames.length == 0) {
         throw new MetadataException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
      }

      // If we get here, have at least one catalog.  
      for (int i = 0 ; i < catalogNames.length; i++) {
         String catalogName = catalogNames[i];
         if (catalogName == null) {
            throw new MetadataException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
         }
         String catNamePrefix = "";
         if (catalogNames.length > 1) {
            //More than one catalog, need a cat-specific prefix
            catNamePrefix = catalogName+"/";
         }
         tabularDb.append(
            makeVoResourceElement(
                "tdb:TabularDB",
                // Namespaces
                "xmlns:vod='http://www.ivoa.net/xml/VODataService/v0.5' " +
                "xmlns:tdb ='urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3' ",
                // Schema locations
                "http://www.ivoa.net/xml/VODataService/v0.5 http://www.ivoa.net/xml/VODataService/v0.5" + " " + 
                "urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3 http://software.astrogrid.org/schema/vo-resource-types/TabularDB/v0.3/TabularDB.xsd"
                ) +
            makeCore(catNamePrefix+"TDB","")
         );

         tabularDb.append(
            "<tdb:db>\n");
         String desc = catalogDescs[i];
         tabularDb.append(
            "<tdb:name>"+catalogName+"</tdb:name>\n"+
            "<tdb:description>"+ 
               XmlPrinter.transformSpecials(desc) + "</tdb:description>\n");
         
         TableInfo[] tables = 
             TableMetaDocInterpreter.getTablesInfoByName(catalogName);
         for (int t = 0; t < tables.length; t++) {
            tabularDb.append(
               "<tdb:table xmlns='http://www.ivoa.net/xml/VODataService/v0.5' >\n"+ //default namnespace for table descriptions
                  "<name>"+tables[t].getName()+"</name>\n"+
                  "<description>"+
                  XmlPrinter.transformSpecials(tables[t].getDescription())+
                  "</description>\n"
            );
            
            ColumnInfo[] columns = 
               TableMetaDocInterpreter.getColumnsInfoByName(
                     catalogName, tables[t].getName());
            
            for (int c = 0; c < columns.length; c++) {
               tabularDb.append(
                  "<column>\n"+
                     "<name>"+columns[c].getName()+"</name>\n"+
                     "<description>"+XmlPrinter.transformSpecials(columns[c].getDescription())+"</description>\n"
               );

            //
            // Patch fix - Moved the xml fragment generation to VoTypes.
            // Todo - Need acces to the column size for strings.
            // Todo - Need to create a VoType format for dates.
            tabularDb.append(
                VoTypes.getVoTypeXml(columns[c].getJavaType())
                );
   /*
    *
               String datatype = VoTypes.getVoType(columns[c].getJavaType());
               if (datatype.equals("char")) {
                  tabularDb.append(
                        "<datatype arraysize='*'>char</datatype>"
                  );
               }
               else {
                  tabularDb.append(
                        "<datatype>"+datatype+"</datatype>"
                  );
               }
    *
    */         
               if ((columns[c].getUnits() != null) && (columns[c].getUnits().toString().trim().length()>0)) {
                  tabularDb.append(
                     "<unit>"+columns[c].getUnits()+"</unit>\n"
                  );
               }
               String columnUcd = columns[c].getUcd(ucdVersion);
               if ((columnUcd != null) && (columnUcd.trim().length()>0)) {
                  tabularDb.append(
                     "<ucd>"+columnUcd.trim()+"</ucd>\n"
                  );
               }
               tabularDb.append(
                  "</column>\n"
               );
            }
            
            tabularDb.append(
               "</tdb:table>\n");
            
         }
         tabularDb.append(
            "</tdb:db>\n");
         tabularDb.append(
            "</"+VORESOURCE_ELEMENT+">\n");
      }
      // Finished processing all catalogs now
      
      return tabularDb.toString();
   }
   
}
