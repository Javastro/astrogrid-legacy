/*
 * $Id: RdbmsResourceReader.java,v 1.1 2004/11/11 20:42:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.rmi.RemoteException;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.metadata.MetadataException;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * Interprets the RdbmsResource document.  Generally speaking we want to
 * return details from the document instead of the database itself for two reaons:
 * 1) The database may not be locally available (if it's proxied) or it might not
 * really exist - the service may be simulating one
 * 2) The sysadmin will have decided which tables/columns are to be published
 * and whihc are not, and the ones to be publihsed are in the resource document
 */

public class RdbmsResourceReader {
   
   public static Element getTable(String table) throws IOException {
      Element rdbmsRes = VoDescriptionServer.getResource(RdbmsResourceGenerator.XSI_TYPE);
      if (rdbmsRes == null) {
         throw new RemoteException("No "+RdbmsResourceGenerator.XSI_TYPE+" on server");
      }
      Element[] tables = DsaDomHelper.getChildrenByTagName(rdbmsRes, "Table");
      for (int i = 0; i < tables.length; i++) {
         String tableName = DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(tables[i], "Name"));
         if (tableName.trim().toLowerCase().equals(table.toLowerCase())) {
            return tables[i];
         }
      }
      throw new MetadataException("Table "+table+" not found");
   }

   public static Element getColumn(String table, String column) throws IOException {
      Element tableRes = getTable(table);

      Element[] cols = DsaDomHelper.getChildrenByTagName(tableRes, "Column");
      for (int i = 0; i < cols.length; i++) {
         String colName = DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(cols[i], "Name"));
         if (colName.trim().toLowerCase().equals(column.toLowerCase())) {
            return cols[i];
         }
      }
      throw new MetadataException("Column "+column+" not found in table "+table);
   }
   
   public static Element[] getTables() throws IOException {
      Element rdbmsRes = VoDescriptionServer.getResource(RdbmsResourceGenerator.XSI_TYPE);
      if (rdbmsRes == null) {
         throw new RemoteException("No "+RdbmsResourceGenerator.XSI_TYPE+" on server");
      }
      return DsaDomHelper.getChildrenByTagName(rdbmsRes, "Table");
   }
   
   public static Element[] getColumns(String tableName) throws IOException {
      Element tableRes = getTable(tableName);
      return DsaDomHelper.getChildrenByTagName(tableRes, "Column");
   }
   
   /** Returns the units of the given column in the given table.  */
   public static String getUnitsOf(String tableName, String columnName) throws IOException {
      Element colRes = getColumn(tableName, columnName);
      return DomHelper.getValue(colRes, "Unit");
   }
   
}


