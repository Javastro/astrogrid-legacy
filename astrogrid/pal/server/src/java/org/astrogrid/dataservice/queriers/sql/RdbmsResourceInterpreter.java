/*
 * $Id: RdbmsResourceInterpreter.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import java.io.IOException;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.dataservice.metadata.queryable.ConeConfigQueryableResource;
import org.astrogrid.dataservice.metadata.queryable.QueryableResourceReader;
import org.astrogrid.dataservice.metadata.queryable.SearchField;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.dataservice.metadata.tables.ColumnInfo;
import org.astrogrid.dataservice.metadata.tables.TableInfo;
import org.w3c.dom.Element;

/**
 * Interprets the RdbmsResource document.  Generally speaking we want to
 * return details from the document instead of the database itself for two reaons:
 * 1) The database may not be locally available (if it's proxied) or it might not
 * really exist - the service may be simulating one
 * 2) The sysadmin will have decided which tables/columns are to be published
 * and whihc are not, and the ones to be publihsed are in the resource document
 * 4) There are many extra bits of information that we may need to know (eg units)
 * that are not available from the db's natural metadata
 * @todo - at the moment just use the old conesearch config stuff, should look
 * at database description instead, perhaps some marker as to which cols are right

 */

public class RdbmsResourceInterpreter extends ConeConfigQueryableResource implements QueryableResourceReader
{
   
   Element rdbmsRes;
   
   public RdbmsResourceInterpreter() throws IOException {
      rdbmsRes = VoDescriptionServer.getResource(RdbmsResourceGenerator.XSI_TYPE);
      if (rdbmsRes == null) {
         throw new MetadataException("No "+RdbmsResourceGenerator.XSI_TYPE+" on server");
      }
   }

   /** Returns the RDBMS element describing the table with the given ID/name.
    * Looks through all the elements so that it can check case insensitively.
    */
   public Element getTableElement(String table) {

      //assertions
      if (table == null) { throw new IllegalArgumentException("No table specified"); }
      
      Element[] tables = DomHelper.getChildrenByTagName(rdbmsRes, "Table");
      for (int i = 0; i < tables.length; i++) {
         String tableId = tables[i].getAttribute("ID");
         if ((tableId == null) || (tableId.length()==0)) { //no ID, use name
            tableId = DomHelper.getValueOf(tables[i], "Name");
         }
         if (tableId.trim().toLowerCase().equals(table.toLowerCase())) {
            return tables[i];
         }
      }
      return null;
   }

   /** Return table info on the column with the given ID/name */
   public TableInfo getTable(String datasetId, String table) {
      return makeTableInfo(getTableElement(table));
   }

   /** Return column info with the given name/id in the given table name/id */
   public Element getColumnElement(String table, String column)  {
      
      //assertions
      if (column == null) { throw new IllegalArgumentException("No column specified"); }

      Element tableRes = getTableElement(table);

      if (tableRes == null) {
         //no such table
         throw new IllegalArgumentException("Table '"+table+"' not found in RdbmsRes");
      }

      Element[] cols = DomHelper.getChildrenByTagName(tableRes, "Column");
      
      if (cols == null) {
         //no columns in given table
         throw new IllegalArgumentException("No Columns found in table "+table);
      }
      
      for (int i = 0; i < cols.length; i++) {
         String colName = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(cols[i], "Name"));
         if (colName.trim().toLowerCase().equals(column.toLowerCase())) {
            return cols[i];
         }
      }
      return null;
   }
   
   /** Return column info with the given name/id in the given table name/id */
   public ColumnInfo getColumn(String datasetId, String table, String column) throws MetadataException {
      return makeColumnInfo(table, getColumnElement(table, column));
   }

   /** Used by SQL results where only the column name is known. Returns the element
    * corresponding to the only matching column, if there are several or none
    * throws an exception*/
   public ColumnInfo guessColumn(String[] scope, String column) throws IOException {
      Element[] tables = DomHelper.getChildrenByTagName(rdbmsRes, "Table");
      Element foundCol = null;
      String foundTable = null;
      
      //look through scope; if scope not given, use all tables
      if (scope==null) {
         scope = new String[tables.length];
         for (int i = 0; i < scope.length; i++) {
            scope[i] = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(tables[i], "Name"));
         }
      }
      
      for (int t = 0; t < scope.length; t++) {
         String tableName = scope[t];

         Element[] cols = DomHelper.getChildrenByTagName(getTableElement(tableName), "Column");
         for (int c = 0; c < cols.length; c++) {
            String colName = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(cols[c], "Name"));
            if (colName.trim().toLowerCase().equals(column.toLowerCase())) {
               if (foundCol == null) {
                  foundCol = cols[c];
                  foundTable = tableName;
               }
               else {
                  throw new MetadataException("Column "+column+" found more than once");
               }
            }
         }
      }
      if (foundCol == null) {
         throw new MetadataException("Column "+column+" not found in any table");
      }
      else {
         return makeColumnInfo(foundTable, foundCol);
      }
   }

   public TableInfo[] getTables(String datasetId)  {
      Element[] elements = DomHelper.getChildrenByTagName(rdbmsRes, "Table");
      TableInfo[] infos = new TableInfo[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         infos[i] = makeTableInfo(elements[i]);
      }
      return infos;
   }
   
   public ColumnInfo[] getColumns(String datasetId, String table) throws MetadataException {
      Element tableElement = getTableElement(table);
      Element[] elements = DomHelper.getChildrenByTagName(tableElement, "Column");
      ColumnInfo[] infos = new ColumnInfo[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         infos[i] = makeColumnInfo(table, elements[i]);
      }
      return infos;
   }
   

   /** Creates a TableInfo instance from the given RDBMS Resource table element */
   public TableInfo makeTableInfo(Element element) {
      
      if (element == null) { return null; }
      
      TableInfo info = new TableInfo();
      info.setName(DomHelper.getValueOf(element, "Name"));
      String id = element.getAttribute("ID");
      if (id == null) id = info.getName();
      info.setId(id);
      info.setDescription(nullIfEmpty(DomHelper.getValueOf(element, "Description")));
      return info;
   }
   
   /** Creates a ColumnInfo instance from the given RDBMS Resource column element */
   public ColumnInfo makeColumnInfo(String table, Element element) throws MetadataException {
      
      if (element == null) { return null; }
      
      ColumnInfo info = new ColumnInfo();
      info.setName(DomHelper.getValueOf(element, "Name"));
      info.setGroup(table);

      String id = element.getAttribute("ID");
      if ((id == null) || (id.length()==0)) {
         id = info.getGroup()+"."+info.getName();
      }
      info.setId(id);
      info.setDescription(nullIfEmpty(DomHelper.getValueOf(element, "Description")));
      info.setUnits(nullIfEmpty(DomHelper.getValueOf(element, "Units")));
      info.setUcd(nullIfEmpty(DomHelper.getValueOf(element, "UCD")));
      info.setUcdPlus(nullIfEmpty(DomHelper.getValueOf(element, "UcdPlus")));
      info.setErrorField(nullIfEmpty(DomHelper.getValueOf(element, "ErrorColumn")));
      
      if (DomHelper.getValueOf(element, "DataType") == null) {
         throw new MetadataException("Column "+info.getName()+" has no DataType element in RdbmsResourec");
      }
      
      info.setDatatype(nullIfEmpty(DomHelper.getValueOf(element, "DataType")));
      if (info.getDatatype() != null) {
         info.setJavaType(VoTypes.getJavaType(info.getDatatype()));
      }
      return info;
   }
   
   /** Returns null if given string is empty/whitespace, or the string otherwise.
    * This makes it easier to check for unknown properties, such as UCDs, where you can
    * just check for nulls */
   public String nullIfEmpty(String s) {
      if (s.trim().length()==0) {
         return null;
      }
      return s;
   }

   /** Returns field information about the field (ie column) with the given ID within the parent group (ie table) */
   public SearchField getField(SearchGroup table, String colId) throws IOException  {
      return getColumn(table.getGroup(), table.getId(), colId);
   }
   
   /** Returns group information about the group (ie table) with the given ID within the parent
    * group (ie dataset) */
   public SearchGroup getGroup(SearchGroup table, String colId) throws IOException  {
      return getTable(table.getId(), colId);
   }
   
   /** Returns the list of groups (ie tables) in the given parent group (ie dataset) */
   public SearchGroup[] getGroups(SearchGroup dataset) throws IOException   {
      return getTables(dataset.getName());
   }
   
   /** Returns the list of 'root' groups - ie tables from a rdbms resource document*/
   public SearchGroup[] getRootGroups() throws IOException  {
      return getTables(null);
   }
   
   /** Returns the list of fields in the given parent group */
   public SearchField[] getFields(SearchGroup parent) throws IOException   {
      return getColumns(parent.getGroup(), parent.getId());
   }
   
   

}


