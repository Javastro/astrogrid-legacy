/*
 * $Id: TableMetaDocInterpreter.java,v 1.1 2005/03/10 13:49:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.tables;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.tables.ColumnInfo;
import org.astrogrid.dataservice.metadata.tables.TableInfo;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.xml.XmlTypes;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Provides a set of convenience routines for accessing the TableMetaDoc that
 * defines tabular datasets.  Generally speaking we want to
 * return details from the document instead of the database itself for two reaons:
 * 1) The database may not be locally available (if it's proxied) or it might not
 * really exist - the service may be simulating one
 * 2) The sysadmin will have decided which tables/columns are to be published
 * and whihc are not, and the ones to be publihsed are in the resource document
 * 4) There are many extra bits of information that we may need to know (eg units)
 * that are not available from the db's natural metadata
 
 */

public class TableMetaDocInterpreter
{
   
   Element metadoc;
// Element[] catalogs; //root list of catalogs
   
   public final static String TABLE_METADOC_KEY = "datacenter.metadoc.url";
   
   /** Construct to interpret the table metadoc given in the config file */
   public TableMetaDocInterpreter() throws IOException {
      this(SimpleConfig.getSingleton().getString(TABLE_METADOC_KEY));
   }

   
   public TableMetaDocInterpreter(String url) throws IOException {
      try {
         metadoc = DomHelper.newDocument(new URL(url)).getDocumentElement();
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException(e);
      }
      catch (SAXException e) {
         throw new MetadataException("Server's TableMetaDoc at "+url+" is invalid XML: "+e);
      }
//    catalogs = DomHelper.getChildrenByTagName(metadoc, "Catalog");
   }

   /** Returns the element describing the catalog (group of tables, not necessarily
    * a sky catalog) with the given ID/name.
    * Looks through all the elements so that it can check case insensitively.
    */
   public Element getCatalogElement(String catalogName) {
      //assertions
      if (catalogName == null) { throw new IllegalArgumentException("No table specified"); }
      
      Element[] cats = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      for (int i = 0; i < cats.length; i++) {
         String catId = cats[i].getAttribute("ID");
         if ((catId == null) || (catId.length()==0)) { //no ID, use name
            catId = DomHelper.getValueOf(cats[i], "Name");
         }
         if (catId.trim().toLowerCase().equals(catalogName.toLowerCase())) {
            return cats[i];
         }
      }
      return null;
   }

      
   /** Returns the element describing the table with the given ID/name.
    * Looks through all the elements so that it can check case insensitively.
    */
   public Element getTableElement(Element catalog, String table) {

      //assertions
      if (table == null) { throw new IllegalArgumentException("No table specified"); }
      
      Element[] tables = DomHelper.getChildrenByTagName(catalog, "Table");
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
   public TableInfo getTable(String catalog, String table) {
      return makeTableInfo(getTableElement(getCatalogElement(catalog), table));
   }

   /** Return column info with the given name/id in the given table name/id */
   public Element getColumnElement(String catalog, String table, String column)  {
      
      //assertions
      if (column == null) { throw new IllegalArgumentException("No column specified"); }

      Element tableRes = getTableElement(getCatalogElement(catalog), table);

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
      return makeColumnInfo(table, getColumnElement(datasetId, table, column));
   }

   /** Used by SQL results where only the column name is known. Returns the element
    * corresponding to the only matching column, if there are several or none
    * throws an exception*/
   public ColumnInfo guessColumn(String[] scope, String column) throws IOException {

      Element foundCol = null;
      String foundTable = null;

      //loop through all the catalogs
      String[] cats = getCatalogs();
      for (int d = 0; d < cats.length; d++) {
      
         Element catNode = getCatalogElement(cats[d]);
         
         //if scope is not given, use all tables
         if (scope==null) {
            Element[] tables = DomHelper.getChildrenByTagName(catNode, "Table");
            scope = new String[tables.length];
            for (int i = 0; i < scope.length; i++) {
               scope[i] = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(tables[i], "Name"));
            }
         }
         
         for (int t = 0; t < scope.length; t++) {
            String tableName = scope[t];
   
            Element[] cols = DomHelper.getChildrenByTagName(getTableElement(catNode, tableName), "Column");
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
      }
      if (foundCol == null) {
         throw new MetadataException("Column "+column+" not found in any table");
      }
      else {
         return makeColumnInfo(foundTable, foundCol);
      }
   }

   /** Returns the list of names of all the catalogs in the metadoc */
   public String[] getCatalogs()  {
      Element[] elements = DomHelper.getChildrenByTagName(metadoc, "Catalog");
      String[] catNames = new String[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         catNames[i] = DomHelper.getValueOf(elements[i], "Name");
      }
      return catNames;
   }

   public TableInfo[] getTables(String catalog)  {
      Element[] elements = DomHelper.getChildrenByTagName(getCatalogElement(catalog), "Table");
      TableInfo[] infos = new TableInfo[elements.length];
      for (int i = 0; i < elements.length; i++)
      {
         infos[i] = makeTableInfo(elements[i]);
         infos[i].setDataset(catalog);
      }
      return infos;
   }
   
   public ColumnInfo[] getColumns(String catalog, String table) throws MetadataException {
      Element tableElement = getTableElement(getCatalogElement(catalog), table);
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
      info.setErrorField(nullIfEmpty(DomHelper.getValueOf(element, "ErrorColumn")));

      Element[] ucdNodes = DomHelper.getChildrenByTagName(element, "UCD");
      for (int u = 0; u < ucdNodes.length; u++) {
         info.setUcd(DomHelper.getValueOf(ucdNodes[u]), ucdNodes[u].getAttribute("version"));
      }
      
      if (DomHelper.getValueOf(element, "Datatype") == null) {
         throw new MetadataException("Column "+info.getName()+" has no DataType element in RdbmsResourec");
      }
      
      info.setDatatype(nullIfEmpty(DomHelper.getValueOf(element, "Datatype")));
      if (info.getDatatype() != null) {
         info.setJavaType(XmlTypes.getJavaType(info.getDatatype()));
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

   /** Returns a mini 'catalog' that lists the tables and fields that define
    * sky coordinates, for things like cone searches */
   

}


